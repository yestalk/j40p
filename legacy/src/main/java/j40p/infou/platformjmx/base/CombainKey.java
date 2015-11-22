package j40p.infou.platformjmx.base;

import java.util.Arrays;

public class CombainKey {
	String opname;
	String[] sing;
	public CombainKey(String opname ,String[] sing){
		if(opname == null || sing==null)
			throw new RuntimeException("null is not accepctable.");
		this.opname=opname;
		this.sing=sing;
	}
	@Override
	public String toString() {
		return this.opname+Arrays.toString(this.sing);
	};
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((opname == null) ? 0 : opname.hashCode());
		result = prime * result + Arrays.hashCode(sing);
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof CombainKey))
			return false;
		CombainKey other = (CombainKey) obj;
		if (!opname.equals(other.opname))
			return false;
		else
			return Arrays.equals(sing, other.sing);
	}

	
}
