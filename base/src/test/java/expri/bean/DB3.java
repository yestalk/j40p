package expri.bean;

import j40p.base.DBean;
import j40p.base.TypeUtil;
import j40p.base.l.P;

public interface DB3 extends DB1,DB2,DBean {

	P<Long> psmb1= TypeUtil.i.defProperty (DB3.psmb1);
	P<String> psmbc= TypeUtil.i.defProperty (DB3.psmbc);
	
	static class X{
		public static void main(String[] args) {
			DB3 db3 = DBean.t.create(DB3.class);
			System.out.println("hehe?");
			long nots = System.nanoTime();
			db3.s(DB3.psmb1, 99999l);
			System.out.println("set:"+(System.nanoTime()-nots));
			long not = System.nanoTime();
			//long not = System.nanoTime();
			System.out.println(db3.g(DB4.psmb1));
			System.out.println("get:"+(System.nanoTime()-not));
			//System.out.println(System.nanoTime()-not);
			//currentTimeMillis
			//nanoTime
		}
	}
}
