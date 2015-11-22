package j40p.base.cache;

import java.util.Arrays;

public class K {
	protected Object[] vs;
	private int hsc = -1;
	
//	protected void set(Object[] o){
//		if(this.vs==null)
//			this.vs=o;
//	}

	
	public Object getVs(int id){
		return this.vs[id];
	}
	@Override
	public int hashCode() {
		if (this.hsc == -1)
			return this.hsc = Arrays.hashCode(this.vs);
		else
			return this.hsc;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		else if (obj instanceof K) {
			K objk = (K) obj;
			if (objk.hashCode() == this.hashCode()) {
				return Arrays.equals(this.vs, objk.vs);
			} else
				return false;

		} else
			return false;
	}

}