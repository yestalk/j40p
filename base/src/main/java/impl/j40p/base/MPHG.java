package impl.j40p.base;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.TreeSet;

public class MPHG {

	static public int hash(int d, byte[] k) {
		d = (d == 0) ? 0x811C9DC5 : d;
		for (byte c : k)
			d ^= c * 16777619;
		return d & 0x7fffffff;
	}

	static public MPKey[] create(TreeSet<? extends HKey> hks) {
		int tosz = hks.size();
		MPKeyholder[] lmpks = new MPKeyholder[hks.size()];
		TreeSet<Integer> freeset = new TreeSet<Integer>();
		TreeSet<Integer> tempfreeset = new TreeSet<Integer>();
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

					if (!freeset.contains(dd2dis)) {
						freeset.addAll(tempfreeset);
						tempfreeset.clear();
						lst.clear();
						dd++;
						continue ddp;
					} else {
						tempfreeset.add(dd2dis);
						freeset.remove(dd2dis);
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
		tset.add(new HKeye("hk/jbi/d8i88"));
		tset.add(new HKeye("ee/df/d881"));
		tset.add(new HKeye("d8/34/28/4"));
		tset.add(new HKeye("d/99g/887"));
		tset.add(new HKeye("d/yyui/88h"));
		tset.add(new HKeye("d/778i/88j"));
		tset.add(new HKeye("d/778i/88/j/nb"));
		tset.add(new HKeye("d/778i/88/j/nb1"));
		tset.add(new HKeye("d/778i/88/j/nb2"));
		tset.add(new HKeye("d/778i/88/j/nb3"));
		tset.add(new HKeye("d/778i/88/j/nb4"));
		tset.add(new HKeye("d/778ie/88/j/nb5"));
		tset.add(new HKeye("d/778i/88/j/nb6"));
		tset.add(new HKeye("d/778i/88/j/nb7"));
		tset.add(new HKeye("d/778i/88/j/nb8"));
		tset.add(new HKeye("d/778i/88/j/nb9"));
		tset.add(new HKeye("d/778i/88/j/nb10"));
		tset.add(new HKeye("d/778i/88/j/nb11"));
		tset.add(new HKeye("d/778i/88/j/nb12"));
		tset.add(new HKeye("d/778i/88/j/nb13"));
		tset.add(new HKeye("d/778i/88/j/nb14"));
		tset.add(new HKeye("d/778i/88/j/nb15"));
		tset.add(new HKeye("d/778i/88/j/nb16"));
		tset.add(new HKeye("d/778i/88/j/nb1j"));
		tset.add(new HKeye("d/778i/88/j/nb1k"));
		tset.add(new HKeye("d/778i/88/j/nb1l"));
		tset.add(new HKeye("d/778i/88/j/nb1o"));
		MPKey[] out = MPHG.create(tset);
		System.out.println(Arrays.toString(out));

	}

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
