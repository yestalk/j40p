package j40p.tabform.impl;

import java.util.Iterator;

import j40p.tabform.iface.ObjPath;

class She implements ObjPath {
	private StkNv back;
	private int offe;

	She(StkNv back, int offe) {
		this.back = back;
		this.offe = offe;
	}

	She() {

	}

	@Override
	public Object getFrist() {

		return this.getith(this.offe);
	}

	@Override
	public Object getLast() {

		return this.back.getLast();
	}

	@Override
	public Object getith(int i) {

		return this.back.getith(i + this.offe);
	}

	@Override
	public int size() {
		return this.back.size() - this.offe;
	}

	@Override
	public ObjPath shiftedRoot(int offest) {
		She nshe = new She();
		nshe.back = this.back;
		nshe.offe= this.offe+ offest;
		//System.out.println(nshe.offe);
		return nshe;
	}

	@Override
	public Iterator<Object> iterator() {
		//System.out.println("offe:"+this.offe);

		return this.back.shiftedIterator(this.offe);
	}
}
