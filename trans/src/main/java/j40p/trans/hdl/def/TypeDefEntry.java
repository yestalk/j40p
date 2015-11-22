package j40p.trans.hdl.def;

import j40p.base.UTF8ByteStr;
import j40p.tabform.iface.Ele;
import j40p.trans.common.DOMType;

import java.awt.List;
import java.util.HashMap;

 

public class TypeDefEntry  {
	public String pathName;
	public HashMap<String, String> keyI18nName = new HashMap<>();
	public Object domType;
	
	public TypeDefEntry(TypeDefEntry data){
		this.domType=List.class;
		this.pathName=data.pathName+"()list";
	}

	public TypeDefEntry(Ele data) {
		UTF8ByteStr ltp = null;
		if(data.getAttrSize()>0)
			ltp=data.getAttr(UTF8ByteStr.t.FromString("type"));
		this.pathName="/"+data.getName().asString();
		if (ltp != null) {
			try {
				this.domType=Class.forName(ltp.asString());
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("type no found.");
			}
		}else
			this.domType = DOMType.txt;
	}

}
