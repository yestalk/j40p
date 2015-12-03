package j40p.base;

import j40p.base.l.HashKey;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.TreeSet;

public interface DefUtil {

	String placeHolderString = new String("");
	Object placeHolderObject = placeHolderString;
	String blank = "<blank>";
	String[] stringArray = {};
	Object[] objArray = {};
	byte[] emptybtyes = new byte[0];
	UTF8ByteStr emptyUtf8Str = UTF8ByteStr.t.FromString(DefUtil.placeHolderString);
	Class<?>[] emptyParam = new Class<?>[] {};
	Boolean[] booleanenumvalues = new Boolean[] { true, false };

	int hashCode_code = 0;
	int equals_code = 1;
	int toString_code = 2;

	Comparator<Method> globelMethodComparator = new GlobleMethodComparator();

	DefUtil i = new D();

	Object lookupDefaultValue(Class<?> clz);

	<T> T[] $array(Class<T> type, T... ts);

	int[] $arrayint(int... ints);

	<T> T[] arrayConcat(T[]... arrays);

	<T extends InvocationHandler> T getWrappedSelf(Class<T> type, T self);

	HashMap<String, String> makeMap(HashMap<String, String> rthash, String... strvalues);

	Object mimicObj4Proxy(Method md, Object target, Object[] param);

	Class<?> mapPrimitive2Wrapper(Class<?> pri);

	Object[] prepareArgs(Method md);

	HashMap<Class<?>, Integer> makeMap(Object... pairs);

	int dhash(int d, byte[] k);

	int dhashorig(int d, byte[] k);

	MPHKey[] createMPHProfile(TreeSet<? extends HashKey> hks);

	MPHKey[] createMPHProfileorig(TreeSet<? extends HashKey> hks);

	class GlobleMethodComparator implements Comparator<Method> {
		// public static final GlobleMethodComparator i = new
		// GlobleMethodComparator();
		private GlobleMethodComparator() {
		}

		@Override
		public int compare(Method o1, Method o2) {
			// TODO Auto-generated method stub

			return (o1.getDeclaringClass().getName() + o1.getName()).compareTo((o2.getDeclaringClass().getName() + o2.getName()));
		}
	}

	class MPKeyholder extends MPHKey implements Comparable<MPKeyholder> {
		public LinkedList<HashKey> mpset = new LinkedList<HashKey>();

		public MPHKey extract() {
			MPHKey mk = new MPHKey();
			mk.hkey = this.hkey;
			mk.jumpv = this.jumpv;
			return mk;
		}

		@Override
		public int compareTo(MPKeyholder o) {
			int lsz = this.mpset.size() - o.mpset.size();
			return (lsz < 0) ? 1 : (lsz == 0) ? this.mpset.getFirst().compareTo(o.mpset.getFirst()) : -1;
		}

		@Override
		public String toString() {

			return "\r\n___hk:_" + this.hkey.toString() + "___jmpv:_" + this.jumpv + "\r\n";
		}
	}

	class D implements DefUtil {
		static final private HashMap<Class<?>, Object> TypeDefaultValue = new HashMap<Class<?>, Object>();
		static final private HashMap<Class<?>, Class<?>> primitiveTypeMap = new HashMap<Class<?>, Class<?>>();
		static {

			D.primitiveTypeMap.put(int.class, Integer.class);
			D.primitiveTypeMap.put(long.class, Long.class);
			D.primitiveTypeMap.put(short.class, Short.class);
			D.primitiveTypeMap.put(char.class, Character.class);
			D.primitiveTypeMap.put(byte.class, Byte.class);
			D.primitiveTypeMap.put(double.class, Double.class);
			D.primitiveTypeMap.put(float.class, Float.class);
			D.primitiveTypeMap.put(boolean.class, Boolean.class);

			D.TypeDefaultValue.put(int.class, 0);
			D.TypeDefaultValue.put(long.class, 0l);
			D.TypeDefaultValue.put(short.class, 0);
			D.TypeDefaultValue.put(char.class, '0');
			D.TypeDefaultValue.put(byte.class, 0);
			D.TypeDefaultValue.put(double.class, 0.0d);
			D.TypeDefaultValue.put(float.class, 0.0f);
			D.TypeDefaultValue.put(boolean.class, false);

			D.TypeDefaultValue.put(Integer.class, D.TypeDefaultValue.get(int.class));
			D.TypeDefaultValue.put(Long.class, D.TypeDefaultValue.get(long.class));
			D.TypeDefaultValue.put(Short.class, D.TypeDefaultValue.get(short.class));
			D.TypeDefaultValue.put(Character.class, D.TypeDefaultValue.get(char.class));
			D.TypeDefaultValue.put(Byte.class, D.TypeDefaultValue.get(byte.class));
			D.TypeDefaultValue.put(Double.class, D.TypeDefaultValue.get(double.class));
			D.TypeDefaultValue.put(Float.class, D.TypeDefaultValue.get(float.class));
			D.TypeDefaultValue.put(Boolean.class, D.TypeDefaultValue.get(boolean.class));

			D.TypeDefaultValue.put(String.class, "");
			D.TypeDefaultValue.put(Date.class, new Date());
		};

		@Override
		public int dhash(int d, byte[] k) {
			// System.out.println("???here?" +d);
			// System.out.println(new String(k, StandardCharsets.UTF_8) + ":__"
			// + d);
			d = (d == 0) ? 0x811C9DC5 : d;
			for (byte c : k) {
				d ^= c;
				d = (d % k.length == 0) ? d * 16777630 : d * 16777619;
				
			}

			return d & 0x7fffffff;
		}

		@Override
		public int dhashorig(int d, byte[] k) {
			// System.out.println("???here?" +d);
			// System.out.println(new String(k, StandardCharsets.UTF_8) + ":__"
			// + d);
			d = (d == 0) ? 0x811C9DC5 : d;
			for (byte c : k) {
				d ^= c;
				d *= 16777619;
			}

			return d & 0x7fffffff;
		}

		@Override
		public MPHKey[] createMPHProfileorig(TreeSet<? extends HashKey> hks) {
			for (HashKey i : hks) {
				System.out.println(i);
			}
			int tosz = hks.size();
			MPHKey[] lmpks = new MPHKey[tosz];
			TreeSet<Integer> freeset = new TreeSet<Integer>();
			for (int i = 0, l = tosz - 1; i <= l; i++)
				freeset.add(i);

			HashSet<Integer> tempfreeset = new HashSet<Integer>();
			TreeSet<MPKeyholder> clidset = new TreeSet<MPKeyholder>();
			for (HashKey i : hks) {
				int dj = i.dhash(0) % tosz;
				MPKeyholder lm = (MPKeyholder) lmpks[dj];
				if (lm == null) {
					lm = new MPKeyholder();
					lmpks[dj] = lm;

					// lm.hkey = i;

				}
				lm.mpset.add(i);

			}

			for (int i = 0, l = lmpks.length - 1; i <= l; i++) {
				MPKeyholder lm = (MPKeyholder) lmpks[i];
				if (lm != null)
					clidset.add(lm);

			}
			System.out.println("propertysize:" + tosz);

			System.out.println("collision.size():__" + clidset.size());
			Iterator<MPKeyholder> itmpk = clidset.iterator();
			while (itmpk.hasNext()) {
				MPKeyholder i = itmpk.next();
				if (i.mpset.size() <= 1)
					break;
				LinkedHashSet<Integer> lst = new LinkedHashSet<Integer>();
				int dd = 1;
				System.out.println("free.size():__" + Arrays.toString(freeset.toArray()));
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for (HashKey ki : i.mpset) {
					System.out.println(ki.toString());
					System.out.println("dhash:" + ki.dhash(1));
					// System.exit(0);
				}
				System.out.println("\r\n====\r\n");
				ddp: do {
					System.out.println("dd:" + dd);
					for (HashKey ki : i.mpset) {
						int rethehs = ki.dhash(dd);
						int dd2dis = rethehs % tosz;
						System.out.print("\r\n" + ki);
						System.out.print("\t" + rethehs);
						System.out.println("\tplace:" + dd2dis);
						System.out.println("free.size():__" + Arrays.toString(freeset.toArray()));
						if (!freeset.remove(dd2dis)) {
							freeset.addAll(tempfreeset);
							tempfreeset.clear();
							lst.clear();
							dd++;
							System.out.println("-----failure------");
							continue ddp;
						} else {
							tempfreeset.add(dd2dis);
							// freeset.remove(dd2dis);
							lst.add(dd2dis);
						}

					}
					tempfreeset.clear();
					break;
				} while (true);// while (i.mpset.size()!=lst.size());

				// }while(lst.size()!=i.mpset.size());
				// System.out.println(Arrays.toString(lst.toArray()));

				i.jumpv = dd;
				HashKey[] rhk = i.mpset.toArray(new HashKey[] {});
				// int zplc = rhk[0].dhash(0) % tosz;
				// if (lmpks[zplc] == null)
				// lmpks[zplc] = new MPKeyholder();
				// lmpks[zplc].jumpv = dd;
				int rhki = 0;
				for (int di : lst) {
					// System.out.println(di);
					if (lmpks[di] == null)

						lmpks[di] = new MPKeyholder();

					lmpks[di].hkey = rhk[rhki++];
				}
				freeset.removeAll(lst);
				itmpk.remove();
				System.out.println("\r\n------\r\n");
			}

			System.out.println("free.size():__" + Arrays.toString(freeset.toArray()));

			for (MPKeyholder i : clidset) {
				int fx = freeset.first();
				freeset.remove(fx);
				HashKey hi = i.mpset.getFirst();
				int indx = hi.dhash(0) % tosz;
				System.out.println("indx:" + indx);
				MPHKey mpkh = lmpks[indx];

				mpkh.jumpv = 0 - fx - 1;
				mpkh = lmpks[fx];
				if (mpkh == null)
					mpkh = lmpks[fx] = new MPKeyholder();
				mpkh.hkey = hi;
			}

			for (int i = 0, l = lmpks.length - 1; i <= l; i++) {
				if (lmpks[i] != null)
					System.out.println(lmpks[i]);
				else
					System.out.println(i);
				// lmpks[i] = ((MPKeyholder) lmpks[i]).extract();
			}

			return lmpks;
		}

		@Override
		public MPHKey[] createMPHProfile(TreeSet<? extends HashKey> hks) {
			for (HashKey i : hks) {
				System.out.println(i);
			}
			int tosz = hks.size();
			MPHKey[] lmpks = new MPHKey[hks.size()];
			HashSet<Integer> freeset = new HashSet<Integer>();
			HashSet<Integer> tempfreeset = new HashSet<Integer>();
			TreeSet<MPKeyholder> clidset = new TreeSet<MPKeyholder>();
			for (HashKey i : hks) {
				int dj = i.dhash(0) % tosz;
				MPKeyholder lm = (MPKeyholder) lmpks[dj];
				if (lm == null) {
					lm = new MPKeyholder();
					lmpks[dj] = lm;
					lm.hkey = i;

				} else {
					lm.mpset.add(i);
				}
			}

			for (int i = 0, l = lmpks.length - 1; i <= l; i++) {
				MPKeyholder lm = (MPKeyholder) lmpks[i];
				if (lm != null) {
					if (lm.mpset.size() > 0)
						clidset.add(lm);
				} else
					freeset.add(i);
			}
			System.out.println("propertysize:" + tosz);

			System.out.println("collision.size():__" + clidset.size());
			for (MPKeyholder i : clidset) {
				LinkedHashSet<Integer> lst = new LinkedHashSet<Integer>();
				int dd = 1;
				System.out.println("free.size():__" + Arrays.toString(freeset.toArray()));
				for (HashKey ki : i.mpset) {
					System.out.println(ki.toString());
					System.out.println("dhash:" + ki.dhash(1));
					// System.exit(0);
				}
				System.out.println("\r\n====\r\n");
				ddp: do {
					System.out.println("dd:" + dd);
					for (HashKey ki : i.mpset) {
						int rethehs = ki.dhash(dd);
						int dd2dis = rethehs % tosz;
						System.out.print("\r\n" + ki);
						System.out.print("\t" + rethehs);
						System.out.println("\tplace:" + dd2dis);
						System.out.println("free.size():__" + Arrays.toString(freeset.toArray()));
						if (!freeset.remove(dd2dis)) {
							freeset.addAll(tempfreeset);
							tempfreeset.clear();
							lst.clear();
							dd++;
							System.out.println("-----failure------");
							continue ddp;
						} else {
							tempfreeset.add(dd2dis);
							// freeset.remove(dd2dis);
							lst.add(dd2dis);
						}

					}
					tempfreeset.clear();
					break;
				} while (true);// while (i.mpset.size()!=lst.size());

				// }while(lst.size()!=i.mpset.size());
				// System.out.println(Arrays.toString(lst.toArray()));
				i.jumpv = dd;
				HashKey[] rhk = i.mpset.toArray(new HashKey[] {});
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
				System.out.println("\r\n------\r\n");
			}

			if (freeset.size() != 0) {
				System.out.println(Arrays.toString(freeset.toArray()));
				throw new RuntimeException("can't be...");
			}

			for (int i = 0, l = lmpks.length - 1; i <= l; i++) {
				lmpks[i] = ((MPKeyholder) lmpks[i]).extract();
			}

			return lmpks;
		}

		@Override
		public Object lookupDefaultValue(Class<?> clz) {
			// TODO Auto-generated method stub
			return D.TypeDefaultValue.get(clz);
		}

		@Override
		public Object[] prepareArgs(Method md) {
			Class<?>[] mdtps = md.getParameterTypes();
			Object[] paramvs = new Object[mdtps.length];
			int ni = 0;
			for (Class<?> i : mdtps) {
				paramvs[ni++] = D.TypeDefaultValue.get(i);
			}
			return paramvs;
		}

		@Override
		public Class<?> mapPrimitive2Wrapper(Class<?> pri) {
			// TODO Auto-generated method stub
			return D.primitiveTypeMap.get(pri);
		}

		public <T> T[] $array(Class<T> type, T... ts) {
			return ts;
		}

		public int[] $arrayint(int... ints) {
			return ints;
		}

		@Override
		public <T extends InvocationHandler> T getWrappedSelf(Class<T> type, T self) {
			ClassLoader lcld = Thread.currentThread().getContextClassLoader();
			ClassLoader current = self.getClass().getClassLoader();
			if (lcld == null || lcld.getParent() != current)
				lcld = current;

			Object obj = Proxy.newProxyInstance(lcld, new Class[] { type }, self);
			return (T) obj;
		}

		private HashMap<String, Integer> fmap;

		private D() {
			this.fmap = new HashMap<String, Integer>();
			this.fmap.put("hashCode", DefUtil.hashCode_code);
			this.fmap.put("equals", DefUtil.equals_code);
			this.fmap.put("toString", DefUtil.toString_code);
		}

		public Object mimicObj4Proxy(Method md, Object target, Object[] param) {
			String mdn = md.getName();
			Integer rt = this.fmap.get(mdn);
			int mcode;
			if (rt == null)
				mcode = -1;
			else
				mcode = rt;
			switch (mcode) {
				case DefUtil.hashCode_code:
					return System.identityHashCode(target);
				case DefUtil.equals_code:
					return target == param[0];
				case DefUtil.toString_code:
					return target.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(target)) + ", with InvocationHandler " + this;
				default:
					return null;
			}

		}

		public <T> T[] arrayConcat(T[]... arrays) {
			// System.out.println(arrays.length);
			int totalLen = 0;
			for (T[] arr : arrays) {
				totalLen += arr.length;
			}
			T[] all = (T[]) Array.newInstance(arrays.getClass().getComponentType().getComponentType(), totalLen);
			int copied = 0;
			for (T[] arr : arrays) {
				System.arraycopy(arr, 0, all, copied, arr.length);
				copied += arr.length;
			}
			return all;
		}

		public HashMap<String, String> makeMap(HashMap<String, String> rthash, String... objs) {
			int objsl = objs.length;
			// System.out.println("length:"+objsl);
			if (objsl > 0 && (objsl % 2) == 0) {
				int valuePairCount = objsl / 2;
				for (int i = 0; i < valuePairCount; i++) {
					int key = 2 * i;
					int value = 2 * i + 1;
					// System.out.println(key);
					// System.out.println(value);
					rthash.put(objs[key], objs[value]);
				}
			} else {
				throw new RuntimeException("wrong parameters");
			}
			return rthash;
		}

		@Override
		public HashMap<Class<?>, Integer> makeMap(Object... pairs) {
			HashMap<Class<?>, Integer> rvmap = new HashMap<Class<?>, Integer>();
			if ((pairs.length % 2) == 0) {
				for (int i = 0, len = pairs.length; i < len;) {
					rvmap.put((Class<?>) pairs[i++], (Integer) pairs[i++]);
				}
			} else
				throw new RuntimeException("must be \"Class<?>, int\" pairs.");
			return rvmap;
		}

	}
}
