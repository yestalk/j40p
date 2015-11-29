package impl.j40p.base;

import j40p.base.ConfBean;
import j40p.base.Configurable;
import j40p.base.DBean;
import j40p.base.l.L;
import j40p.base.l.P;
import j40p.base.l.Res;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public abstract class DBeani3try implements DBean {

	private static final int cacheQsize = 200;
	private static final TreeSet<L<?>> inull = new TreeSet<L<?>>();
	private static final ConcurrentHashMap<Class<?>, TreeSet<L<?>>> allprofile = new ConcurrentHashMap<Class<?>, TreeSet<L<?>>>();
	private static final ConcurrentHashMap<Integer, ArrayBlockingQueue<SoftReference<HashMap<L<?>, H>>>> cacheQ = new ConcurrentHashMap<Integer, ArrayBlockingQueue<SoftReference<HashMap<L<?>, H>>>>();
	private static final ReferenceQueue<Object> clearedNoteQ = new ReferenceQueue<Object>();
	private static final ConcurrentHashMap<Reference<?>, HashMap<L<?>, H>> occupiedRes = new ConcurrentHashMap<Reference<?>, HashMap<L<?>, H>>();

	static {
		Thread dt = new Thread(new Rc());
		dt.setDaemon(true);
		dt.start();
	}

	private Class<?> dbclz;
	private HashMap<L<?>, H> data;
	private TreeSet<L<?>> profile;

	private void init(Class<?> filter) {
		TreeSet<L<?>> orderpf = null;
		while (orderpf == null || orderpf == inull) {
			orderpf = DBeani3try.allprofile.putIfAbsent(this.dbclz, inull);
			if (orderpf != null && orderpf != inull) {
				int lsz = orderpf.size();
				//int lszci = (lsz <= 4) ? lsz : lsz * 100 / 75;
				ArrayBlockingQueue<SoftReference<HashMap<L<?>, H>>> cq = DBeani3try.getCacheQ(lsz);
				HashMap<L<?>, H> pred = null;
				while (pred == null) {
					SoftReference<HashMap<L<?>, H>> rfmp = cq.poll();
					if (rfmp == null) {
						pred = new HashMap<L<?>, DBeani3try.H>((lsz <= 4) ? lsz : lsz * 100 / 75);
						for (L<?> i : orderpf) {
							pred.put(i, new H());
						}
					} else {
						pred = rfmp.get();
					}
				}
				this.profile = orderpf;
				this.data = pred;
				DBeani3try.occupiedRes.put(new WeakReference<DBeani3try>(this, DBeani3try.clearedNoteQ), pred);
				return;

			} else if (orderpf == inull) {
				synchronized (this.dbclz) {
					try {
						this.dbclz.wait();
					} catch (InterruptedException e) {
						new RuntimeException(e);
					}
				}
			} else if (orderpf == null) {
				this.setup(filter);
				synchronized (this.dbclz) {
					this.dbclz.notifyAll();
				}

			}
		}

	}

	private void setup(Class<?> filter) {
		// System.out.println("dbeani  setup:__"+ this.dbclz);
		TreeSet<L<?>> thefis = new TreeSet<L<?>>();
		Class<?> cuz = this.dbclz;
		Field[] fis = cuz.getFields();

		for (Field i : fis) {
			Object oi;
			try {
				// i.setAccessible(true);
				oi = i.get(null);

			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);

			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			}
			int lmodf = i.getModifiers();
			if (Modifier.isStatic(lmodf) && Modifier.isFinal(lmodf) && filter.isAssignableFrom(oi.getClass()))
				thefis.add((L<?>) oi);
		}
		if (thefis.size() == 0)
			throw new RuntimeException("type definition don't have any property." + this.dbclz);

		DBeani3try.allprofile.replace(this.dbclz, thefis);
		return;

	}

	public DBeani3try() {
		Class<?>[] clz = this.getClass().getInterfaces();
		if (clz.length == 1 && DBean.class.isAssignableFrom(clz[0])) {
			this.dbclz = clz[0];
			this.init(P.class);
		} else {
			throw new RuntimeException("dbean type not properly defined.");
		}
	}

	public DBeani3try(Class<?> clz) {

		if (!clz.isInterface() && Configurable.class.isAssignableFrom(clz)) {
			if (this instanceof ConfBean) {
				this.dbclz = clz;
				this.init(Res.class);

			} else
				throw new RuntimeException("no type other than injection label allowed.");
		} else
			throw new RuntimeException("no interface allowed.");

	}

	@Override
	public <T_> void s(L<T_> label, T_ value) {

		H h = this.data.get(label);
		if (h == null)
			throw new RuntimeException("illigel key");
		else
			h.s = value;

	}

	@Override
	public <T_> T_ g(L<T_> label) {

		H h = this.data.get(label);
		if (h == null)
			throw new RuntimeException("illigel key");
		else
			return (T_) h.s;
	}

	private static ArrayBlockingQueue<SoftReference<HashMap<L<?>, H>>> getCacheQ(int size) {
		ArrayBlockingQueue<SoftReference<HashMap<L<?>, H>>> cq = DBeani3try.cacheQ.get(size);
		ArrayBlockingQueue<SoftReference<HashMap<L<?>, H>>> xq;
		if (cq == null) {
			xq = new ArrayBlockingQueue<SoftReference<HashMap<L<?>, H>>>(DBeani3try.cacheQsize);
			cq = DBeani3try.cacheQ.putIfAbsent(size, xq);
			if (cq == null)
				cq = xq;
		}
		return cq;
	}

	private static class H {
		Object s;
	}

	public static class ConfBeani extends DBeani3try implements ConfBean {
		public ConfBeani(Class<?> clz) {
			super(clz);
		}
	}

	static class Rc implements Runnable {

		@Override
		public void run() {
			while (true) {
				try {
					Reference<? extends Object> ref = DBeani3try.clearedNoteQ.remove();
					if (ref instanceof WeakReference<?>) {
						HashMap<L<?>, H> res = DBeani3try.occupiedRes.remove(ref);
						for (H i : res.values()) {
							i.s = null;
						}
						ArrayBlockingQueue<SoftReference<HashMap<L<?>, H>>> cq = DBeani3try.getCacheQ(res.size());
						cq.offer(new SoftReference<HashMap<L<?>, H>>(res, DBeani3try.clearedNoteQ));
					} else if (ref instanceof SoftReference<?>) {
						for (ArrayBlockingQueue<SoftReference<HashMap<L<?>, H>>> i : DBeani3try.cacheQ.values()) {
							i.remove(ref);
						}
					}

				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}

		}

	}

}
