package j40p.trans.hdl.insd;

import j40p.trans.common.I18nEntry;

import java.io.PrintStream;
import java.util.HashMap;

import parser.impl.STDEle;



public class TxtEntry extends DAttrEntry {

	
	public HashMap<String,I18nEntry> txtvaule=new HashMap<>();
	
	public TxtEntry(STDEle info){
		
		super(info);
	}
	
	public void put(PrintStream out,String lang){
		I18nEntry efv = null;
		try {
			efv=this.txtvaule.get(lang);
			if(efv==null)
				efv = this.txtvaule.get("en");

			efv.putHtmlList(out);
		} catch (Exception e) {
			System.out.println(this.fullPathName);
			System.out.println(lang);
			System.out.println(efv);
			System.out.println("keysize:___"+this.txtvaule.values().size());
			for(String i :this.txtvaule.keySet()){
				System.out.println("key:___"+i);
			}
			throw new RuntimeException(e);
		}
		
	}

	
}
