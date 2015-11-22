package j40p.trans.hdl.template.css;

import j40p.base.UTF8ByteStr;
import j40p.tabform.iface.Ele;
import j40p.tabform.iface.ObjPath;
import j40p.tabform.iface.TabFormHdl;

import java.util.HashMap;
import java.util.LinkedList;

 

public class CssHdl implements TabFormHdl {
	HashMap<String,CssClass> ccset = new HashMap<>();
	
	
	
	
	
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
		if(ancestors.size()==1){
			CssClass cscz = new CssClass();
			cscz.info(( ((UTF8ByteStr)ancestors.getFrist()).asString()), this.ccset);
			this.ccset.put(cscz.name, cscz);
			return cscz;
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
		Object x = subject;
		if(x instanceof CssClass){
			CssClass cx = (CssClass)x;
			if(children!=null){
				for(Object i : children){
					if(i instanceof String){
						String si = (String)i;
						if(!si.startsWith("//"))
							cx.cssrules.add(si);
					}
				}
			}

		}
		return null;
	}

	@Override
	public void overAll(LinkedList<?> total) {
		// TODO Auto-generated method stub
		
	}

 

	public String getTxt (){
		StringBuilder sb = new StringBuilder();
		for(CssClass i : this.ccset.values()){
			i.mergNames();
			sb.append(i.result);
		}
		return sb.toString();
	}
}
