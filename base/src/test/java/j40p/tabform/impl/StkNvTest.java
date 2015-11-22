package j40p.tabform.impl;

import j40p.base.cache.ObjectABuf;
import j40p.base.cache.ObjectCacheX;
import j40p.tabform.iface.ObjPath;

public class StkNvTest {

	public static void main(String[] args) {
		StkNv nstx =new StkNv(1000, ObjectCacheX.t.lookup(ObjectABuf.class, ObjectABuf.getCriteriaByCapacity(16), 500));
		
		for(int i =0;i<=500;i++){
			nstx.push(new Ev(i));
		}
		
		System.out.println("size:_"+nstx.size());
		for(int i =0;i<=250;i++){
			
			System.out.println("pop:_"+nstx.pop( ).subject);
		}
		System.out.println("after pop size:_"+nstx.size());
		ObjPath nstxpt=nstx.shiftedRoot(100);
		//nstxpt=nstxpt.shiftedRoot(1);
//		nstxpt=nstxpt.shiftedRoot(1);
//		nstxpt=nstxpt.shiftedRoot(1);
		System.out.println("size:_"+nstxpt.size());
		for(Object i :nstxpt){
			System.out.println(i);
		}
		
		
	}

}
