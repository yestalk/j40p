package j40p.trans.hdl.def;

import j40p.base.UTF8ByteStr;
import j40p.tabform.iface.Ele;
import j40p.tabform.iface.ObjPath;
import j40p.tabform.iface.TabFormHdl;
import j40p.tabform.impl.EvNodeListParser;
import j40p.trans.common.DOMType;
import j40p.trans.common.I18nEntry;

import java.util.HashMap;
import java.util.LinkedList;

public class TypeDefHdl implements TabFormHdl {
	LinkedList<TypeDefEntry> tdlist = new LinkedList<>();

	@Override
	public boolean iscommentsIgnore() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Object createNode(ObjPath ancestors, Ele subject) {
		// Object cudata = pathOfdata.getLast();
		// cudata = STDEle.fromdata((String) cudata);
		// STDEle custd = (STDEle) cudata;

		if (subject.getName().asString().indexOf("i18n") >= 0) {
			I18nEntry cui18n = new I18nEntry(subject);

			return cui18n;
		} else {
			TypeDefEntry te = new TypeDefEntry(subject);

			Object x = null;
			if (ancestors.size() > 0) {
				if ((x = ancestors.getLast()) instanceof TypeDefEntry)
					te.pathName = ((TypeDefEntry) x).pathName + te.pathName;
				else
					throw new RuntimeException("wrong parent for typedef entry");
			}

			return te;// label.
		}

	}

	@Override
	public Object createNode(ObjPath ancestors, int type, UTF8ByteStr subject) {
		if (subject.asString().startsWith("()list")) {

			TypeDefEntry te = new TypeDefEntry((TypeDefEntry) ancestors.getLast());
			return te;
		}
		return null;
	}

	@Override
	public Object createNode(ObjPath ancestors, Object subject) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object concludeNode(ObjPath ancestors, Object subject, LinkedList<?> children) {
		// Object cudata = pathOfdata.getLast();
		if (subject instanceof I18nEntry) {
			I18nEntry i18ncu = (I18nEntry) subject;
			i18ncu.content = EvNodeListParser.i.packSiblingList(children);

		} else if (subject instanceof TypeDefEntry) {
			TypeDefEntry cutpdef = (TypeDefEntry) subject;
			if (children != null) {
				for (Object i : children) {
					if (i instanceof I18nEntry) {
						I18nEntry i18ni = (I18nEntry) i;

						cutpdef.keyI18nName.put(i18ni.langType, i18ni.getkeynamedata());
					} else if (i instanceof TypeDefEntry) {
						if (cutpdef.domType.equals(DOMType.txt))
							cutpdef.domType = DOMType.compounded;
						else if (cutpdef.domType instanceof Class)
							throw new RuntimeException("wrong defination.");
					}

				}

			}
			this.tdlist.add(cutpdef);

		}
		return subject;
	}

	@Override
	public void overAll(LinkedList<?> total) {
		// TODO Auto-generated method stub

	}

	public HashMap<String, TypeDefEntry> getTypeMap() {
		HashMap<String, TypeDefEntry> rvmap = new HashMap<>();

		for (TypeDefEntry i : this.tdlist) {
			TypeDefEntry tdefi = (TypeDefEntry) i;
			rvmap.put(tdefi.pathName, tdefi);
		}
		return rvmap;
	}

}
