package impl.j40p.base.t;


import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.TreeSet;

public class MPHG {
	//static Entry<byte[],byte[]> etr;
//	static {
//		MPHG.etr.getKey();
//		HashSet<UTF8ByteStr> hsub = new HashSet<UTF8ByteStr>();
//	 
//	}
	
	static public int dhash(int d, byte[] k) {
		d = (d == 0) ? 0x811C9DC5 : d;
		for (byte c : k){
				d ^= c;
			d=(d%k.length==0)?d*16777630:d*16777619;
//			if (d%k.length==0)
//				d*=429496729;
//			else
//				d*=16777619;
		}
		return d & 0x7fffffff;
	}

	static public MPKey[] create(TreeSet<? extends HKey<?>> hks) {
		int tosz = hks.size();
		MPKeyholder[] lmpks = new MPKeyholder[hks.size()];
		HashSet<Integer> freeset = new HashSet<Integer>();
		HashSet<Integer> tempfreeset = new HashSet<Integer>();
		TreeSet<MPKeyholder> clidset = new TreeSet<MPKeyholder>();
		for (HKey i : hks) {
			int dj = i.dhash(0) % tosz;
			MPKeyholder lm = lmpks[dj];
			if (lm == null) {
				lm = new MPKeyholder();
				lmpks[dj] = lm;
				lm.hkey = i;

			} else {
				lm.mpset.add(i);
			}
		}

		for (int i = 0, l = lmpks.length - 1; i <= l; i++) {
			MPKeyholder lm = lmpks[i];
			if (lm != null) {
				if (lm.mpset.size() > 0)
					clidset.add(lm);
			} else
				freeset.add(i);
		}

		System.out.println("free.size():__" + Arrays.toString(freeset.toArray()));
		System.out.println("clidset.size():__" + clidset.size());
		for (MPKeyholder i : clidset) {
			LinkedHashSet<Integer> lst = new LinkedHashSet<Integer>();
			int dd = 1;
			ddp: do {
				for (HKey ki : i.mpset) {
					int dd2dis = ki.dhash(dd) % tosz;

					if (!freeset.remove(dd2dis)) {
						freeset.addAll(tempfreeset);
						tempfreeset.clear();
						lst.clear();
						dd++;
						continue ddp;
					} else {
						tempfreeset.add(dd2dis);
						//freeset.remove(dd2dis);
						lst.add(dd2dis);
					}

				}
				tempfreeset.clear();
				break;
			} while(true);//while (i.mpset.size()!=lst.size());

			// }while(lst.size()!=i.mpset.size());
			// System.out.println(Arrays.toString(lst.toArray()));
			i.jumpv = dd;
			HKey[] rhk = i.mpset.toArray(new HKey[] {});
			int rhki = 0;
			for (int di : lst) {
				// System.out.println(di);
				if (lmpks[di] == null) {
					MPKeyholder x = new MPKeyholder();
					lmpks[di] = x;
					x.hkey = rhk[rhki++];
				} else
					throw new RuntimeException("what's wrong?");
			}
			freeset.removeAll(lst);
		}

		if (freeset.size() != 0) {
			System.out.println(Arrays.toString(freeset.toArray()));
			throw new RuntimeException("can't be...");
		}

		return lmpks;
	}

	public static void main(String[] args) {
		TreeSet<HKeye> tset = new TreeSet<HKeye>();
//		tset.add(new HKeye("expri.bean.DB1.p1"));
//		tset.add(new HKeye("expri.bean.DB1.p2"));
//		tset.add(new HKeye("expri.bean.DB1.p3"));
//		tset.add(new HKeye("expri.bean.DB2.p1"));
//		tset.add(new HKeye("expri.bean.DB2.p2"));
//		tset.add(new HKeye("expri.bean.DB2.p3"));
//		tset.add(new HKeye("expri.bean.DB3.p1"));
//		tset.add(new HKeye("expri.bean.DB3.p4"));
		
//		tset.add(new HKeye("a1.p1"));
//		tset.add(new HKeye("a1.p2"));
//		tset.add(new HKeye("a1.p3"));
//		tset.add(new HKeye("a2.p1"));
//		tset.add(new HKeye("a2.p2"));
//		tset.add(new HKeye("a2.p3"));
//		tset.add(new HKeye("a3.p1"));
//		tset.add(new HKeye("a3.p2"));
//		

		
//		tset.add(new HKeye("p0.99.dd.a1.p1"));
//		tset.add(new HKeye("p0.99.dd.a1.p2"));
//		tset.add(new HKeye("p0.99.dd.a1.p3"));
//		tset.add(new HKeye("p0.99.dd.a2.p1"));
//		tset.add(new HKeye("p0.99.dd.a2.p2"));
//		tset.add(new HKeye("p0.99.dd.a2.p3"));
//		tset.add(new HKeye("p0.99.dd.a3.p1"));
//		tset.add(new HKeye("p0.99.dd.a3.p2"));
		
//		
//		tset.add(new HKeye("expri.bean.DB1.abc"));
//		tset.add(new HKeye("expri.bean.DB1.yui"));
//		tset.add(new HKeye("expri.bean.DB1.e7"));
//		tset.add(new HKeye("expri.bean.DB2.m1"));
//		tset.add(new HKeye("expri.bean.DB2.tty"));
//		tset.add(new HKeye("expri.bean.DB2.lin"));
//		tset.add(new HKeye("expri.bean.DB3.base"));
//		tset.add(new HKeye("expri.bean.DB3.jack"));
//		
		
		MPKey[] out = MPHG.create(tset);
		System.out.println(Arrays.toString(out));

	}
	
//	public static void main(String[] args) {
//		byte x255 = (byte)128;
//		byte x1 = 1;
//		System.out.println(x255>x1);
//	}

	// public static void main(String[] args) {
	// LinkedHashSet<Integer> itgs = new LinkedHashSet<Integer>();
	// itgs.add(100);
	// itgs.add(1);
	// itgs.add(3);
	// itgs.add(3);
	//
	// itgs.add(1);
	// System.out.println(Arrays.toString(itgs.toArray()));
	// }
	//
	// public static void main(String[] args) {
	// // byte[] bak = MPHG.class.getName().getBytes(StandardCharsets.UTF_8);
	// // System.out.println(MPHG.hash(0, bak));
	// // System.out.println(MPHG.hash(1, bak));
	// // System.out.println(MPHG.hash(2, bak));
	// // System.out.println(MPHG.hash(3, bak));
	// // System.out.println(0x00ff);
	//
	// TreeSet<Integer> freeset = new TreeSet<Integer>();
	// // freeset.add(10);
	// // freeset.add(4);
	// // freeset.add(1);
	// // freeset.add(9);
	// // freeset.
	// System.out.println(Arrays.toString(freeset.toArray()));
	// }
}
