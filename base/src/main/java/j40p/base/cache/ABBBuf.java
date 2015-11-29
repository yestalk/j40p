package j40p.base.cache;

import java.nio.ByteBuffer;

public class ABBBuf extends ObjectCacheX<ByteBuffer> {

	public static class C extends K implements Criteria<ABBBuf> {
	};

	public static C getCriteriaBy(int cap, boolean isdire) {
		C rtv = new C();
		rtv.vs = (new Object[] { C.class, cap, isdire });
		return rtv;
	}

	private int bfcap = -1;
	private boolean isdire;

	private ABBBuf(int csize) {
		super(csize);

	}

	@Override
	protected void init(Criteria<?> param) {
		if (this.bfcap == -1) {
			C lpm = (C) param;
			this.bfcap = (Integer) (lpm.vs[1]);
			this.isdire = (boolean) (lpm.vs[2]);
		}

	}

	@Override
	protected ByteBuffer createNewOne() {
		if (this.isdire)
			return ByteBuffer.allocateDirect(this.bfcap);
		else
			return ByteBuffer.allocate(this.bfcap);
	}

	@Override
	protected boolean checkOne(ByteBuffer x) {
		if (!x.isReadOnly() && x.capacity() == this.bfcap && x.isDirect()== this.isdire) { 
			x.clear();
			return true;
		} else
			return false;
	}

	byte[] getA() {
		if (this.isdire)
			throw new RuntimeException("not support");
		else
			return this.get().array();
	}

	void recycleA(byte[] a) {
		if (this.isdire)
			throw new RuntimeException("not support");
		else
			this.recycle(ByteBuffer.wrap(a));
	}

}
