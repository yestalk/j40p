package j40p.tabform.iface;

import j40p.base.DefUtil;
import j40p.base.UTF8ByteStr;

import java.util.LinkedList;


public interface TabFormHdl   {
	public static final int Comments=0;
	public static final int Element=1;
	public static final int Txt=2;
	public static final int Binary=3;
	//public static final int User=3;
	
	//public static final int EventStreamEndingSingel=4;
	//public static final int User=4;
	
	
	Object EscapeStructure=DefUtil.placeHolderObject;
	
	
	
	boolean iscommentsIgnore();
	
	Object createNode(ObjPath ancestors,Ele subject);
	Object createNode(ObjPath ancestors,int type,UTF8ByteStr subject);
	
	Object createNode(ObjPath ancestors,Object subject);
	Object concludeNode(ObjPath ancestors,Object subject,LinkedList<?> children);
	void overAll(LinkedList<?> total);
}
