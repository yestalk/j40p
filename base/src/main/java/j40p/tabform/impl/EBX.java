package j40p.tabform.impl;

 
 
import j40p.base.TypeUtil;
import j40p.base.l.Func;
import j40p.base.parser.def.Hdl;

public interface EBX extends Hdl {
	
	Func<Void,Integer> cLevel = TypeUtil.i.deFunc(EBX.cLevel);
	Func<Boolean,Void> cIsCommentsIgnored = TypeUtil.i.deFunc(EBX.cIsCommentsIgnored);
	//F<Void,TabFormHdl> pTabFormHdl = TypeUtil.i.defunc(EBX.pTabFormHdl);
	
	int cEleName=2;
	int cEleEnd=3;
	
	//int cJLabelMark=12;
	int cJKeyName=4;
	int cJEEnd=5;
	int cJInstant=6;
	

	int cPropKey=7;
	int cPropV=8;
	int cInstV=12;
	

	int cTxt=9;
	int cConmment=10;
	
	int sEventStreamEnd=11;
	
}
