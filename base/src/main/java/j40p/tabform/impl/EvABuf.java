package j40p.tabform.impl;
 

import j40p.base.cache.K;
import j40p.base.cache.ObjectCacheX;

 
 

public class EvABuf extends ObjectCacheX<Ev[]> {
	
	public static class C extends K implements Criteria<EvABuf>{
		C(Object[] pvs){
			this.vs=pvs;
		}
	};
	public static C getCriteriaByCapacity(int cap){
		C rtv = new C((new Object[]{C.class,cap}));
	 
		return rtv;
	}
	
	private int bfcap;

	private EvABuf(int csize) {
		super(csize);

	}
	
	public int getTrunkSize(){
		return this.bfcap;
	}

	@Override
	protected void init( Criteria<?> param) {
		C lpm = (C)param;
		this.bfcap=(Integer)(lpm.getVs(1));
		 
	}

	@Override
	protected Ev[] createNewOne() {

		return new Ev[this.bfcap];
	}

	@Override
	protected boolean checkOne(Ev[] x) {
		if(x.length==this.bfcap){
			for( int i=0,l=x.length-1 ; i<=l;i++){
				if(x[i]!=null)
					x[i]=null;
			}
			//Arrays.fill(x, null);
			return true;
		}else 
			return false;
	}

 

}
