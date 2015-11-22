package j40p.tabform.impl;

import j40p.base.cache.ObjectABuf;
import j40p.base.cache.ObjectCacheX;
import j40p.tabform.iface.ObjPath;

import java.util.Iterator;

public class StkNv extends Stk<Ev> implements ObjPath {

	StkNv(int sizelimit, ObjectABuf abbf) {
		super(sizelimit,abbf);

	}

	@Override
	public Object getFrist() {
		if (size > 0)
			return ((Ev)this.stk.get(0)[0]).subject;
		return null;
	}

	@Override
	public Object getLast() {
		return this.peekTop().subject;
	}

	@Override
	public Object getith(int i) {
		if (i < 0) {
			i += this.size;
			if (i < 0)
				throw new RuntimeException("out bondry, negetive index.");
		}
		if (i > this.size - 1)
			throw new RuntimeException("out bondry");
		int row = i / (this.limt);
		int col = i % (this.limt);
		Object[] lcu = null;
		if (row > this.stk.size() - 1)
			lcu = this.cu;
		else
			lcu = this.stk.get(row);
		return ((Ev)lcu[col]).subject;
	}



	@Override
	public ObjPath shiftedRoot(int offest) {
		if(offest<this.size)
			return new She(this, offest);
		else
			throw new RuntimeException("offseted too much , out of bondary.");

	}

	@Override
	public Iterator<Object> iterator() {
		return new Ii(0);
	}

	protected Iterator<Object> shiftedIterator(int stp) {
		return new Ii(stp);
	}

	class Ii implements Iterator<Object> {
		private int startpoint;

		Ii(int spt) {
			this.startpoint = spt;
		}

		@Override
		public boolean hasNext() {
			//System.out.println("startp:_"+this.startpoint);
			return this.startpoint  < StkNv.this.size;
		}

		@Override
		public Object next() {

			return StkNv.this.getith(this.startpoint++);
		}

	}

}
