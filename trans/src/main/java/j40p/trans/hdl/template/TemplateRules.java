package j40p.trans.hdl.template;

import j40p.tabform.impl.EvNodeListParser.EvNodeList;
import j40p.trans.common.I18nEntry;
import j40p.trans.hdl.insd.CompoundedEntry;
import j40p.trans.hdl.insd.DEntry;
import j40p.trans.hdl.insd.DEntryGroup;
import j40p.trans.hdl.insd.SpecialTypedEntry;
import j40p.trans.hdl.insd.TxtEntry;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import parser.iface.PathDocHandler;
import parser.impl.NodeListParser;
import parser.impl.NodeListParser.NodeList;
import parser.impl.STDEle;

public class TemplateRules {
	public String matchname;
	EvNodeList outPutRule;
	public HashSet<String> escapeNames;

	public void setRules(EvNodeList rules) {
		this.outPutRule = rules;
	}

	public TemplateRules(STDEle info) {
		if (info != null && info.instantValues != null)
			this.matchname = info.instantValues.getFirst();
	}

	public void express(PrintStream out, DEntry data, TempOutputer outattr, String lang) {
//		System.out.println(this.getClass().getName());
//		System.out.println(this.matchname);
//		System.out.println(lang);
//		System.out.println("============");
		NodeListParser.i.parse(this.outPutRule, new ExpressHdl(out, data, outattr, lang, this));
	}
	
	public void layouttxtlist(PrintStream out,String data){
		NodeListParser.i.parse(this.outPutRule, new TxtLayoutHdl(out,data) );
	}
	
	class TxtLayoutHdl implements PathDocHandler{
		PrintStream out;
		String data;


		TxtLayoutHdl(PrintStream out,String data){
			this.out = out;	
			this.data = data;
		}
		@Override
		public Object createNode(LinkedList<?> pathOfdata) {
			Object cudata = pathOfdata.getLast();
			if (cudata instanceof HtmlLabel) {
				HtmlLabel culab = (HtmlLabel) cudata;
				culab.putStartLabel(out);
			} else if (cudata instanceof Accesser) {
				this.out.append(data);
			}else if(cudata instanceof String)
				this.out.append((String)cudata);
			return null;
		}

		@Override
		public Object processRelation(LinkedList<?> pathOfdata, LinkedList<?> children) {
			Object cudata = pathOfdata.getLast();
			if (cudata instanceof HtmlLabel) {
				HtmlLabel culab = (HtmlLabel) cudata;
				culab.putEndLabel(out);
			}
			return PathDocHandler.EscapeStructure;
		}

		@Override
		public void overAll() {
			// TODO Auto-generated method stub
			
		}
		
	}

	class ExpressHdl implements PathDocHandler {

		PrintStream out;
		DEntry data;
		TempOutputer outattr;
		String lang;
		TemplateRules bindedrules;

		ExpressHdl(PrintStream out, DEntry data, TempOutputer outattr, String lang, TemplateRules bindedrules) {
			this.out = out;
			this.outattr = outattr;
			this.data = data;
			this.lang = lang;
			this.bindedrules = bindedrules;
		}

		@Override
		public Object createNode(LinkedList<?> pathOfdata) {
			Object cudata = pathOfdata.getLast();
			if (cudata instanceof HtmlLabel) {
				HtmlLabel culab = (HtmlLabel) cudata;
				culab.putStartLabel(out);
			} else if (cudata instanceof Accesser) {
				Accesser lacc = (Accesser) cudata;

				String llang = "en";
				if (lacc.lang.equals("")) {
					if (!this.lang.equals(""))
						llang = this.lang;

				} else
					llang = lacc.lang;
				String lfullpth =(this.data.fullPathName.equals("/"))?"":this.data.fullPathName;
				String apath = lfullpth + lacc.matchname;

				boolean withkey = this.outattr.withkeyset.contains(apath);
				DEntry subdata = null;
				switch (lacc.acctype) {
					case Accesser.acc_current_key:
						outPutKey(llang, this.data);
						break;
					case Accesser.acc_subTarget_v:
						//System.out.println(apath);
						//System.out.println(this.data.fullPathName);
						if(this.bindedrules instanceof ForEach)
							throw new RuntimeException("wildcard anonymous rule can not have target accesser. ");
						
						if(!(this.data instanceof CompoundedEntry))
							throw new RuntimeException("only CompoundedEntry can have target accesser.");
						CompoundedEntry lcope = (CompoundedEntry) this.data;
						subdata = lcope.childrenlookup.get(lacc.matchname);
//						for(String si: lcope.childrenlookup.keySet()){
//							System.out.println("	si:__"+si);
//							
//						}
//						System.out.println("	"+lacc.matchname);
//						System.out.println("	"+subdata);
						TemplateRules subrule = this.outattr.tms.get(apath);
						reExpress(this.out,llang, subdata, subrule,this.outattr.tms);
						break;
					case Accesser.acc_current_v:
						if (withkey)
							outPutKey(llang, this.data);
						if(this.bindedrules instanceof ForEach){
							reExpress(this.out,llang, this.data, this.outattr.tms.get(this.data.fullPathName),this.outattr.tms);
						}else{
							if (this.data instanceof CompoundedEntry) 
								throw new RuntimeException("infinit CompoundedEntry access. ");
							else if(this.data instanceof TxtEntry){
								TxtEntry subtx =(TxtEntry)this.data;
								subtx.put(out, llang);
							}else if(this.data instanceof SpecialTypedEntry){
								SpecialTypedEntry subtx =(SpecialTypedEntry)this.data;
								subtx.putvalue(out,llang);
							}
						}
						break;

				}

			} else if (cudata instanceof ForEach) {
				String llang = (this.lang.equals("")) ? "en" : this.lang;
				ForEach forcudata = (ForEach) cudata;
				if (this.data instanceof CompoundedEntry) {
					CompoundedEntry compdata = (CompoundedEntry) this.data;
					// System.out.println(this.bindedrules.matchname);

					if (this.bindedrules.escapeNames != null) {
						// for (String mi : this.bindedrules.escapeNames) {
						// System.out.println("escapeNames:__"+mi);
						// }
						// System.out.println("=========");
						for (DEntry i : compdata.children) {
							if (!this.bindedrules.escapeNames.contains(i.relativePathName)) {
								forcudata.express(this.out, i, this.outattr, llang);
							}
						}
					} else {
						for (DEntry i : compdata.children) {

							forcudata.express(this.out, i, this.outattr, llang);

						}
					}

				} else if (this.data instanceof DEntryGroup) {
					DEntryGroup gpdata = (DEntryGroup) this.data;

					for (DEntry i : gpdata.typegroup) {

						forcudata.express(this.out, i, this.outattr, this.lang);

					}
				}else if(this.data instanceof TxtEntry){
					TxtEntry txel = (TxtEntry)this.data;
					I18nEntry li18netx = txel.txtvaule.get(llang);
					if(li18netx!=null && li18netx.content!=null){
						for(Object i : li18netx.content){
							if(i instanceof String)
								forcudata.layouttxtlist(this.out, (String)i);
						}
					}
				}

			} else if (cudata instanceof RefLink) {
				this.out.append(outattr.refs.get(((RefLink) cudata).link));

			} else if (cudata instanceof String)
				this.out.append((String) cudata);
			return null;
		}

		private void reExpress(PrintStream out,String llang, DEntry subdata, TemplateRules subrule,HashMap<String ,TemplateRules> tms) {
			if (subdata != null) {
				if(subrule!=null)
					subrule.express(this.out, subdata, this.outattr, llang);
				else{
					if(subdata instanceof CompoundedEntry){
						CompoundedEntry lcompd = (CompoundedEntry)subdata;
						for(DEntry i : lcompd.children){
							this.reExpress(out,llang, i, tms.get(i.fullPathName), tms);
						}
					}else if(subdata instanceof DEntryGroup){
						DEntryGroup lcompd = (DEntryGroup)subdata;
						for(DEntry i : lcompd.typegroup){
							this.reExpress(out,llang, i, tms.get(i.fullPathName), tms);
						}
					}else if(subdata instanceof TxtEntry){
						
						TxtEntry subtx =(TxtEntry)subdata;
						subtx.put(out,llang);
						//System.out.println(subtx.fullPathName);
					}else if(subdata instanceof SpecialTypedEntry){
						SpecialTypedEntry subtx =(SpecialTypedEntry)subdata;
						subtx.putvalue(out,llang);
					}
				}
				
			}
		}

		private void outPutKey(String llang, DEntry subdata) {
			if (subdata.typedef != null && subdata.typedef.keyI18nName != null && subdata.typedef.keyI18nName.get(llang) != null) {
				this.out.append(subdata.typedef.keyI18nName.get(llang));
			} else
				this.out.append("blank_key");
		}

		@Override
		public Object processRelation(LinkedList<?> pathOfdata, LinkedList<?> children) {

			Object cudata = pathOfdata.getLast();
			if (cudata instanceof HtmlLabel) {
				HtmlLabel culab = (HtmlLabel) cudata;
				culab.putEndLabel(out);
			}
			return PathDocHandler.EscapeStructure;
		}

		@Override
		public void overAll() {

		}
	}

}




//if (this.data instanceof CompoundedEntry) {
//	// access subdata
//	CompoundedEntry lcope = (CompoundedEntry) this.data;
//	if (lacc.matchname.equals(""))
//		subdata = this.data;
//	else {
//		//System.out.println(lacc.matchname);
//		subdata = lcope.childrenlookup.get(lacc.matchname);
//	}
//	TemplateRules subrule = this.outattr.tms.get(apath);
////	System.out.println("subrulematchname:__" + apath);
////	System.out.println("subdata:__" + subdata);
////	System.out.println("subrule:__" + subrule);
//	if (subdata != null && subrule != null) {
//
//		//System.out.println("subrule.matchname:__" + subrule.matchname);
//
//		subrule.express(this.out, subdata, this.outattr, llang);
//	}
//
//} else
//	subdata = this.data;
//
//if (subdata != null) {
//
//	if (subdata instanceof TxtEntry) {
//		((TxtEntry) subdata).put(out, llang);
//	} else if (subdata instanceof SpecialTypedEntry) {
//		((SpecialTypedEntry) subdata).putvalue(out);
//	}
//}
//break;
