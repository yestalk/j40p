package j40p.tabform.iface;

import j40p.base.UTF8ByteStr;



public interface Ele {
	public static final int JKV=0;
	public static final int FEle=1;
	int getType();
	
	UTF8ByteStr getName();
	
	UTF8ByteStr getAttr(UTF8ByteStr key);
	int getAttrSize();
	
	UTF8ByteStr getInstant(int key);
	int getInstantSize();
}
