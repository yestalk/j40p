package j40p.base.cache;


public class ObjectABuf extends ObjectCacheX<Object[]> {

	public static class C extends K implements Criteria<ObjectABuf> {
	};

	public static C getCriteriaByCapacity(int cap) {
		C rtv = new C();
		rtv.vs = (new Object[] { C.class, cap });
		return rtv;
	}

	private int bfcap;

	private ObjectABuf(int csize) {
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
		ObjectABuf iabf = ObjectCacheX.t.lookup(ObjectABuf.class, ObjectABuf.getCriteriaByCapacity(128), 50);
		System.out.println(iabf.get().length);
	}

}
