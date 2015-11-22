package j40p.trans.hdl.insd;

import j40p.tabform.iface.TabFormHdl;
import j40p.trans.common.DOMType;
import j40p.trans.common.GroupEntryEnhancer;
import j40p.trans.common.I18nEntry;
import j40p.trans.common.Lang;
import j40p.trans.hdl.def.TypeDefEntry;
import j40p.trans.hdl.def.TypeDefHdl;
import j40p.trans.mex.out.Email;
import j40p.trans.mex.out.Mobile;
import j40p.trans.mex.out.Salary;
import j40p.trans.mex.out.TimeRange;
import j40p.trans.mex.out.ValueOutPuter;

import java.util.HashMap;
import java.util.LinkedList;

import parser.iface.PathDocHandler;
import parser.impl.NodeListParser;
import parser.impl.STDEle;

public class GDataHdl implements TabFormHdl {
	HashMap<String, TypeDefEntry> typeDefMap;
	GroupEntryEnhancer enhancers;

	LinkedList<Object> rootchildlist = new LinkedList<>();

	private CompoundedEntry rootrv;

	public CompoundedEntry getRoot() {
		return this.rootrv;
	}

	public GDataHdl(HashMap<String, TypeDefEntry> typemap, GroupEntryEnhancer gpenher) {
		this.typeDefMap = typemap;
		this.enhancers = gpenher;
	}

	@Override
	public Object createNode(LinkedList<?> pathOfdata) {

		//Object rootele = pathOfdata.getFirst();
		Object cudata = pathOfdata.removeLast();
		Object parent = null;
		if (pathOfdata.size() > 0)
			parent = pathOfdata.removeLast();
		if ( parent instanceof I18nEntry  || parent instanceof String || parent instanceof SpecialTypedEntry)
			return null;

		cudata = STDEle.fromdata((String) cudata);

		STDEle custd = (STDEle) cudata;
		if (custd.eventType == STDEle.ETYPE_Ele) {

			if (custd.eventValue.indexOf("s:i18n") >= 0)
				return new I18nEntry(custd);

			String pathname = "/" + custd.eventValue;
			String rpathname = pathname;

			if (parent instanceof CompoundedEntry)
				pathname = ((DEntry) parent).fullPathName + pathname;
			else if(parent!=null){
				System.out.println(parent);
				throw new RuntimeException("wrong parent for data entry");
			}
			//System.out.println(pathname);
			TypeDefEntry etype = this.typeDefMap.get(pathname);
			//System.out.println(etype);
//			if (etype==null){
//				for(Object i :pathOfdata){
//					System.out.println(i);
//				}
//				System.out.println(pathname);
//			}
			if (etype.domType == DOMType.compounded) {
				CompoundedEntry cpe = new CompoundedEntry(custd);
				cpe.typedef = etype;
				cpe.fullPathName = pathname;
				cpe.relativePathName = rpathname;
				return cpe;
			} else if (etype.domType instanceof Class<?>) {
				SpecialTypedEntry tpde = new SpecialTypedEntry(custd);
				tpde.typedef = etype;
				tpde.fullPathName = pathname;
				tpde.relativePathName = rpathname;
				if (etype.domType instanceof Class) {
					try {
						tpde.outputer = (ValueOutPuter) ((Class) etype.domType).newInstance();
					} catch (InstantiationException e) {
						throw new RuntimeException("error with VauleOutputer", e);
					} catch (IllegalAccessException e) {
						throw new RuntimeException("error with VauleOutputer", e);
					}
				}

				return tpde;
			} else {
				TxtEntry txte = new TxtEntry(custd);
				txte.typedef = etype;
				txte.fullPathName = pathname;
				txte.relativePathName = rpathname;

				return txte;
			}

		}

		return null;
	}

	@Override
	public Object processRelation(LinkedList<?> pathOfdata, LinkedList<?> children) {
		Object cud = pathOfdata.getLast();
		if (cud instanceof TxtEntry) {
			TxtEntry tcud = (TxtEntry) cud;
			LinkedList<Object> onode = new LinkedList<>();
			boolean hasi18n = false;
			for (Object i : children) {
				if (i instanceof I18nEntry) {
					hasi18n = true;
					I18nEntry i18ne =(I18nEntry)i;
					tcud.txtvaule.put(i18ne.langType, i18ne);
				} else {
					onode.add(i);

				}
			}
			if (!hasi18n && onode.size() > 0) {
				I18nEntry i18ne = new I18nEntry(null);
				i18ne.langType = Lang.en.name();
				i18ne.content = NodeListParser.i.packSiblingList(onode);
				tcud.txtvaule.put(Lang.en.name(), i18ne);
			}
			if (pathOfdata.size() == 1)
				this.rootchildlist.add(tcud);
			return cud;
		} else if (cud instanceof I18nEntry) {
			I18nEntry i18ne = (I18nEntry) cud;
			i18ne.content = NodeListParser.i.packSiblingList(children);
			return cud;
		} else if (cud instanceof CompoundedEntry) {

			CompoundedEntry cude = (CompoundedEntry) cud;

			cude.setChildren(children,this.typeDefMap,this.enhancers);
			if (pathOfdata.size() == 1)
				this.rootchildlist.add(cude);
			return cud;
		} else if (cud instanceof SpecialTypedEntry) {
			SpecialTypedEntry scud = (SpecialTypedEntry) cud;
			scud.outputer.SetData((String) children.getFirst());
			if (pathOfdata.size() == 1)
				this.rootchildlist.add(scud);
			return cud;
		}
		return null;
	}

	@Override
	public void overAll() {
		CompoundedEntry root = new CompoundedEntry(null);
		root.fullPathName = "/";
		//System.out.println(this.rootchildlist.size());
		root.setChildren(this.rootchildlist,this.typeDefMap,this.enhancers);
		this.rootrv = root;

	}
}
