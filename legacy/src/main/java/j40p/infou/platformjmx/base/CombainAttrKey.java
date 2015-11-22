package j40p.infou.platformjmx.base;

import j40p.infou.platformjmx.def.MBeanItemType;

public class CombainAttrKey {
	String itemName;
	MBeanItemType itemtp;
	public CombainAttrKey(String itemname ,MBeanItemType itemtp){
		if(this.itemtp == null || this.itemName==null)
			throw new RuntimeException("null is not accepctable.");
		this.itemName=itemname;
		this.itemtp=itemtp;
	}
	@Override
	public String toString() {
		return this.itemName+itemtp.toString();
	};
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((itemName == null) ? 0 : itemName.hashCode());
		result = prime * result + this.itemtp.hashCode();
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof CombainAttrKey))
			return false;
		CombainAttrKey other = (CombainAttrKey) obj;
		if (!itemName.equals(other.itemName))
			return false;
		else
			return this.itemtp.equals(other.itemtp);
	}

	
}
