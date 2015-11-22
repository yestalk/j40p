package j40p.trans.hdl.insd;

import j40p.tabform.iface.Ele;
import j40p.trans.mex.out.ValueOutPuter;

import java.io.PrintStream;

import parser.impl.STDEle;



public class SpecialTypedEntry extends DAttrEntry {

	
	//public String value;
	public ValueOutPuter outputer;

	public SpecialTypedEntry(Ele info){
		super(info);
		
	}
	public ValueOutPuter getVOP(){
		return this.outputer;
	}

	public void putvalue(PrintStream out,String lang){
		out.append(this.outputer.out(lang));
	}
	
}
