package impl.j40p.base;

import java.util.LinkedList;

public   class MPKeyholder extends MPKey implements Comparable<MPKeyholder>{
	 public LinkedList<HKey> mpset=new LinkedList<HKey>();
	// public int location;
	@Override
	public int compareTo(MPKeyholder o) {
		int lsz = this.mpset.size()-o.mpset.size();
		return (lsz<0)?1:(lsz==0)?this.hkey.compareTo(o.hkey):-1;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "\r\n___hk:_"+this.hkey.toString()+"___jmpv:_"+this.jumpv+"\r\n";
	}
}
