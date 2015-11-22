package j40p.base.parser;

import j40p.base.cache.IntABuf;
import j40p.base.cache.ObjectCacheX;

import java.util.Arrays;
import java.util.LinkedList;

public class IntBuilder {
	 

	static IntABuf iabf;
	static private int tksize;

	public static void setBFCache(IntABuf abbf) {
		IntBuilder.iabf = abbf;
		IntBuilder.tksize = IntBuilder.iabf.get().length;
	}

	private LinkedList<int[]> al = new LinkedList<int[]>();

	private int[] cu;
	private int cupoint = -1;

	private int[] fcu;
	private int fcupoint = 0;

	private int size = 0;

	public int size() {
		return this.size;
	}

	public void add(int v) {
		if (this.cu == null) {
			if (this.size == 0) {
				this.cu = IntBuilder.iabf.get();
				this.fcu = this.cu;
			} else
				throw new RuntimeException("intbuilder cu size__" + this.size + "   cu__" + this.cu);
		}

		int x = this.nextpoint();
		this.cu[x] = v;
		this.size++;
	}

	public int removeFirst() {
		// System.out.println("hehe:"+this.fcupoint);
		if (this.size == 0)
			throw new RuntimeException();
		int rtv = this.fcu[this.fcupoint];

		if (this.fcu == this.cu) {
			// System.out.println( "	case in line");
			if (this.fcupoint + 1 > this.cupoint) {
				this.fcupoint = 0;
				this.cupoint = -1;
				IntBuilder.iabf.recycle(this.cu);
				this.cu = null;
				this.fcu = null;
			} else
				this.fcupoint++;
		} else if (this.fcupoint + 1 == IntBuilder.tksize) {
			this.fcupoint = 0;
			// System.out.println("sz:"+this.al.size());
			if (this.al.size() > 0) {
				this.fcu = this.al.removeFirst();
			} else
				this.fcu = this.cu;
			// System.out.println("sz:"+this.al.size());

		} else
			this.fcupoint++;

		this.size--;

		return rtv;

	}

	public void removeLast() {
		// int alsz = this.al.size();
		switch (this.cupoint) {

			case 0:
				if (this.al.size() > 0) {
					this.cupoint = IntBuilder.tksize - 1;
					this.cu = this.al.removeLast();
				} else if (this.cu != this.fcu) {
					this.cupoint = IntBuilder.tksize - 1;
					this.cu = this.fcu;
				} else {
					this.cupoint = -1;
					this.fcupoint = 0;
					IntBuilder.iabf.recycle(this.cu);
					this.cu = null;
					this.fcu = null;
				}
				break;
			case -1:
				throw new RuntimeException("no more to remove");
			default:
				this.cupoint--;
		}
		this.size--;

	}

	public int getlast() {
		return this.cu[this.cupoint];
	}

	private int nextpoint() {
		if (this.cupoint + 1 == IntBuilder.tksize) {
			this.cupoint = 0;
			if (this.cu != this.fcu)
				this.al.add(this.cu);
			this.cu = new int[IntBuilder.tksize];
			return 0;
		} else
			return ++this.cupoint;

	}

	private void dump() {
		for (int[] i : this.al) {
			System.out.println(Arrays.toString(i));

		}
		System.out.println(Arrays.toString(this.cu));

		System.out.println("\r\n---dump over ---\r\n");
	}

	public static void main(String[] args) {
		IntBuilder.setBFCache(ObjectCacheX.t.lookup(IntABuf.class, IntABuf.getCriteriaByCapacity(512), 1000));
		IntBuilder ib = new IntBuilder();

		for (int i = 0; i <= 1000; i++) {
			ib.add(i);
		}

		//
		for (int i = 0; i <= 1000; i++) {
			System.out.println("first:_" + ib.removeFirst());
		}
		// for(int i =0;i<=1000;i++){
		// System.out.println("last:_"+ib.getlast());
		// ib.removeLast();
		// }
		System.out.println("last sz:_" + ib.size());
		System.out.println("\r\n------\r\n");

		for (int i = 0; i <= 555; i++) {
			ib.add(i);
		}

		for (int i = 0; i <= 555; i++) {
			System.out.println("first:_" + ib.removeFirst());
		}
		// for(int i =0;i<=555;i++){
		// System.out.println("last:_"+ib.getlast());
		// ib.removeLast();
		// }
		System.out.println("last sz:_" + ib.size());
		System.out.println("\r\n------\r\n");

	}

}
