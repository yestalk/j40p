package j40p.base.parser.def;

import j40p.base.parser.CaptureManager;
import j40p.base.parser.def.ex.CancelScanningException;


public interface Scanner  {
 

//	
	
	int limitbreak_event=9;
	int limitbreak_constrain=10;

 

	
	
	static class ParamsReg{
		
		//public int cmd;
		
		//public int start; // start==end==-1 tell the end of stream.
		//public int end;
		
		
		//public boolean sectionbreak=false;
		public boolean brkconfirming=false;
		public int lastposition;
		
		public int limitType;
		public long limitLength;
		
		
		
		 
		
	}
	void init(ParamsReg reg);
	Scanner  scan(byte[] data, int start,int end,boolean sectionbreak,CaptureManager cm,ParamsReg paramsreg)
	throws CancelScanningException;
		

}