package impl.j40p.base;

import j40p.base.cache.ObjectCacheX;


static class BHBuf extends ObjectCacheX<Object[]> {

	public static class C extends K implements Criteria<BHBuf> {
	};

	public static C getCriteriaByCapacity(int cap) {
		C rtv = new C();
		rtv.vs = (new Object[] { C.class, cap });
		return rtv;
	}

	private int bfcap;

	private BHBuf(int csize) {
		super(csize);
	}

	@Override
	protected void init(Criteria<?> param) {
		C lpm = (C) param;
		this.bfcap = (Integer) (lpm.vs[1]);
	}

	@Override
	protected Object[] createNewOne() {
		return new Object[this.bfcap];
	}

	@Override
	protected boolean checkOne(Object[] x) {
		if (x.length == this.bfcap) {
			return true;
		} else
			return false;
	}
	
	public static void main(String[] args) {
		BHBuf iabf = ObjectCacheX.t.lookup(BHBuf.class, BHBuf.getCriteriaByCapacity(128), 50);
		System.out.println(iabf.get().length);
	}

}
