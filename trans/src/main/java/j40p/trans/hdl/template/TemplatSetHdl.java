package j40p.trans.hdl.template;

import j40p.base.UTF8ByteStr;
import j40p.tabform.iface.Ele;
import j40p.tabform.iface.ObjPath;
import j40p.tabform.iface.TabFormHdl;
import j40p.trans.hdl.template.css.CssHdl;
import j40p.trans.hdl.template.withkey.WithKeyHdl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import parser.iface.PathDocHandler;
import parser.impl.NodeListParser;
import parser.impl.NodeListParser.NodeList;
import parser.impl.STDEle;

public class TemplatSetHdl implements TabFormHdl {

 
	
	HashMap<String, String> refs = new HashMap<>();
	HashSet<String> withkeyset = new HashSet<>();
	HashMap<String ,TemplateRules> tms =new HashMap<>();
	@Override
	public Object processRelation(LinkedList<?> pathOfdata, LinkedList<?> children) {
		Object head = pathOfdata.getFirst();
		Object cudata = pathOfdata.getLast();

		if (head instanceof CssHdl && head!=cudata) {
			pathOfdata.removeFirst();
			return ((CssHdl) head).processRelation(pathOfdata, children);
		} else if (head instanceof WithKeyHdl && head!=cudata) {
			pathOfdata.removeFirst();
			return ((WithKeyHdl) head).processRelation(pathOfdata, children);
		} else if (cudata instanceof TemplateRules) {
			TemplateRules cuforeach = (TemplateRules) cudata;
			if((children==null || children.size()==0)){
				if(cuforeach instanceof ForEach){
					Accesser cacc = new Accesser(null);
					cacc.acctype=Accesser.acc_current_v;
					cacc.matchname="";
					LinkedList<Object> llst =new LinkedList<>();
					llst.add(cacc);
					cuforeach.setRules(NodeListParser.i.packSiblingList(llst));
					return cuforeach;
				}else{
					throw new RuntimeException("empty template with no rules is not acceptable.");
				}
			}else{
				
				for(Object i : children){
					
					if(i instanceof Accesser){
						heheacces(cuforeach, (Accesser)i);
					}else if( i instanceof NodeList){
						for(Object nli : (NodeList)i){
							//System.out.println(i);
							if(nli instanceof Accesser){
								
								heheacces(cuforeach, (Accesser)nli);
							}
						}
					}
				}
				Object firstobj = children.getFirst();
				if(children.size()>1)
					cuforeach.setRules(NodeListParser.i.packSiblingList(children));
				else if(children.size()==1 && firstobj instanceof NodeList)
					cuforeach.setRules((NodeList)firstobj);
				
				if(cudata instanceof ForEach)
					return cuforeach;
				else{
					this.tms.put(cuforeach.matchname, cuforeach);
					return PathDocHandler.EscapeStructure;
				}
			}

				
				
		} else if (cudata instanceof CssHdl) {
			CssHdl csshead = ((CssHdl) head);
			csshead.overAll();
			this.refs.put("/css", csshead.getTxt());
			return PathDocHandler.EscapeStructure;

		} else if (cudata instanceof WithKeyHdl) {
			WithKeyHdl withkeyhead = ((WithKeyHdl) head);
			withkeyhead.overAll();
			
			this.withkeyset=withkeyhead.getResult();
			return PathDocHandler.EscapeStructure;

		}
		

		return null;
	}
	private void heheacces(TemplateRules cuforeach, Accesser iacc) {
		//System.out.println(iacc);
		//Accesser iacc =(Accesser)i;
		if(cuforeach instanceof ForEach && !iacc.matchname.equals(""))
			throw new RuntimeException("wildcard anonymous teamplate can not have named acccssor.");
		if(cuforeach.escapeNames==null)
			cuforeach.escapeNames=new HashSet<>();
		//System.out.println(iacc.matchname);
		if(!iacc.matchname.equals(""))
			cuforeach.escapeNames.add(iacc.matchname);
	}
	public TempOutputer getoutputer(){
		TempOutputer op =new TempOutputer();
		op.refs=this.refs;
		op.tms=this.tms;
		op.withkeyset=this.withkeyset;
		
		
		return op;
	}
	
	@Override
	public void overAll() {
		// TODO Auto-generated method stub

	}
	@Override
	public boolean iscommentsIgnore() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public Object createNode(ObjPath ancestors, Ele subject) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object createNode(ObjPath ancestors, int type, UTF8ByteStr subject) {
		Object head = ancestors.getFirst();
		if (head instanceof CssHdl) {
			pathOfdata.removeFirst();
			return ((CssHdl) head).createNode(pathOfdata);
		} else if (head instanceof WithKeyHdl) {
			pathOfdata.removeFirst();
			return ((WithKeyHdl) head).createNode(pathOfdata);
		} 
		
		Object cudata = pathOfdata.getLast();
		
		cudata = STDEle.fromdata((String) cudata);
		
		STDEle custd = (STDEle) cudata;
		if (custd.eventType == STDEle.ETYPE_Ele) {
			if (custd.eventValue.equals("...")) {
				return new ForEach(custd);
			} else if (custd.eventValue.equals("tm")) {
				return new TemplateRules(custd);
			} else if (custd.eventValue.equals("#")) {
				return new Accesser(custd);
			} else if (custd.eventValue.startsWith("h:")) {
				return new HtmlLabel(custd);
			} else if (custd.eventValue.equals("ref")) {
				return new RefLink(custd);
			}else if (custd.eventValue.equals("css")) {
				return new CssHdl();
			}else if (custd.eventValue.equals("withkey")) {
				return new WithKeyHdl();
			}
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
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void overAll(LinkedList<?> total) {
		// TODO Auto-generated method stub
		
	}

}
