package j40p.base.cache;


public class IntABuf extends ObjectCacheX<int[]> {

	public static class C extends K implements Criteria<IntABuf> {
	};

	public static C getCriteriaByCapacity(int cap) {
		C rtv = new C();
		rtv.vs = (new Object[] { C.class, cap });
		return rtv;
	}

	private int bfcap;

	private IntABuf(int csize) {
		super(csize);
	}

	@Override
	protected void init(Criteria<?> param) {
		C lpm = (C) param;
		this.bfcap = (Integer) (lpm.vs[1]);
	}

	@Override
	protected int[] createNewOne() {
		return new int[this.bfcap];
	}

	@Override
	protected boolean checkOne(int[] x) {
		if (x.length == this.bfcap) {
			return true;
		} else
			return false;
	}
	
	public static void main(String[] args) {
		IntABuf iabf = ObjectCacheX.t.lookup(IntABuf.class, IntABuf.getCriteriaByCapacity(128), 50);
		System.out.println(iabf.get().length);
	}

}
