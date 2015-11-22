package j40p.http.impl;

import j40p.base.UTF8ByteStr;
import j40p.http.iface.Hvalue;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.Set;

class HVHdl {
	//UTF8ByteStr namev;
	UTF8ByteStr keyv;
	HashMap<UTF8ByteStr, UTF8ByteStr> attrs ;
	LinkedList<HV> listhv = new LinkedList<HVHdl.HV>();

	LinkedList<HV> getHVList() {
		LinkedList<HV> rv = this.listhv;
		this.listhv = new LinkedList<HVHdl.HV>();

		return rv;

	}
	
	public boolean keystage(){
		return this.keyv!=null;
	}

	void setValue(UTF8ByteStr d) {

		 
		HV lhv = new HV();
		lhv.namev = d;
		lhv.attrs = this.attrs =new HashMap<UTF8ByteStr, UTF8ByteStr>();
		this.listhv.add(lhv);

	}

	void setKey(UTF8ByteStr d) {
		this.keyv = d;
	}

	void setV4K(UTF8ByteStr d) {
		if (this.keyv == null) {
			throw new RuntimeException();
		} else {
			this.attrs.put(this.keyv, d);
			this.keyv = null;
		}
	}

	int size() {
		return this.listhv.size();
	}

	class HV implements Hvalue {

		UTF8ByteStr namev;

		HashMap<UTF8ByteStr, UTF8ByteStr> attrs = new HashMap<UTF8ByteStr, UTF8ByteStr>();
		
		
		public  void put(UTF8ByteStr key, UTF8ByteStr v){
			this.attrs.put(key, v);
		}

		@Override
		public UTF8ByteStr getValue() {

			return this.namev;
		}

		@Override
		public UTF8ByteStr getAttr(UTF8ByteStr key) {

			return this.attrs.get(key);
		}

		@Override
		public Set<Entry<UTF8ByteStr, UTF8ByteStr>> getEntrys() {

			return this.attrs.entrySet();
		}
	}
}
