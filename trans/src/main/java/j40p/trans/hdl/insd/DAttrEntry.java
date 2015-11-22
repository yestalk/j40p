package j40p.trans.hdl.insd;

import j40p.tabform.iface.Ele;

import java.util.HashMap;
import java.util.LinkedList;

 



public class DAttrEntry extends DEntry {

	
	public HashMap<String,String> attrs;
	public LinkedList<String> instantValues;
	
	public DAttrEntry(Ele info){
		if(info!=null){
			this.attrs=info.attrs;
			this.instantValues=info.instantValues;
		}
		//System.out.println("whatever.");
		
	}
	

	
}
