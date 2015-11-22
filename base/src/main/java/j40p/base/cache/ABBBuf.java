package j40p.base.cache;

import java.nio.ByteBuffer;
 

public class ABBBuf extends ObjectCacheX<ByteBuffer> {
	
	public static class C extends K implements Criteria<ABBBuf>{};
	public static C getCriteriaByCapacity(int cap){
		C rtv = new C();
		rtv.vs=(new Object[]{C.class,cap});
		return rtv;
	}
	
	private int bfcap;

	private ABBBuf(int csize) {
		super(csize);

	}

	@Override
	protected void init( Criteria<?> param) {
		C lpm = (C)param;
		this.bfcap=(Integer)(lpm.vs[1]);
		 
	}

	@Override
	protected ByteBuffer createNewOne() {

		return ByteBuffer.allocate(this.bfcap);
	}

	@Override
	protected boolean checkOne(ByteBuffer x) {
		if(x.capacity()==this.bfcap){
			x.clear();
			return true;
		}else 
			return false;
	}

	byte[] getA() {
		return this.get().array();
	}

	void recycleA(byte[] a) {
		this.recycle(ByteBuffer.wrap(a));
	}

}
