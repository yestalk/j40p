package j40p.tabform.impl;

import j40p.base.cache.ObjectCacheX;
import j40p.tabform.iface.ObjPath;

import java.util.ArrayList;

public abstract class Stk2<E> {

 

	protected int limt;
	private ObjectCacheX<E[]> evcache;
	private int sizelimit = -1;
	protected int size = 0;
	private int pointer = -1;
	protected ArrayList<E[]> stk = new ArrayList<E[]>(10);
	protected E[] cu;

	public Stk2(int sizelimit, ObjectCacheX<E[]> abbf) {
		if (sizelimit > 0)
			this.sizelimit = sizelimit;
		this.evcache = abbf;
		this.cu = abbf.get();
		this.limt = this.cu.length;
	}

	 

	public void push(E obj) {
		// System.out.println("stk size" + this.pointer + "__" + this.size);
		if (size == this.sizelimit)
			throw new RuntimeException("size limit been reached.");
		this.pointer++;
		this.size++;
		if (this.pointer == this.limt) {

			this.pointer = 0;
			E[] x = this.evcache.get();
			x[0] = obj;
			this.stk.add(this.cu);

			this.cu = x;
		} else {

			this.cu[this.pointer] = obj;
		}

		// System.out.println("cupointer:_"+this.pointer);
		// System.out.println("stktsz:_"+this.stk.size());
		//return obj;
	}

	public E pop() {

		if (this.size == 0)
			throw new RuntimeException("has no more to pop.");
		// System.out.println("pop cupointer:_"+this.pointer);
		// System.out.println("pop stktsz:_"+this.stk.size());

		E rt = this.cu[this.pointer];
		this.cu[this.pointer--] = null;

		if (this.pointer < 0 && this.stk.size() > 0) {

			this.pointer = this.limt - 1;
			this.evcache.recycle(this.cu);
			this.cu = this.stk.remove(this.stk.size() - 1);

		}

		this.size--;
		return rt;
	}

	public E peekTop() {
		if (this.size == 0)
			return null;

		return this.cu[this.pointer];
	}

	 
	public int size() {
		return this.size;
	}

}
