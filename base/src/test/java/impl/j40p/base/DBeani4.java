package impl.j40p.base;

import impl.j40p.base.BHBuf.C;
import j40p.base.ConfBean;
import j40p.base.Configurable;
import j40p.base.DBean;
import j40p.base.DefUtil;
import j40p.base.MPHKey;
import j40p.base.cache.ObjectABuf;
import j40p.base.cache.ObjectCacheX;
import j40p.base.cache.ObjectCacheX.Criteria;
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

public abstract class DBeani4 implements DBean {
	static private class BType {
		private MPHKey[] profile;
		private ObjectABuf trunkCache;
	}

	static private class Bins {
		private BType type;
		private Object[] datatrunk;
	}

	private static final BType inull = new BType();
	private static final ConcurrentHashMap<Class<?>, BType> typemap = new ConcurrentHashMap<Class<?>, BType>();
	private static final ConcurrentHashMap<WeakReference<DBean>, Bins> occupiedRes = new ConcurrentHashMap<WeakReference<DBean>, Bins>();
	private static final ReferenceQueue<DBean> clearedNoteQ = new ReferenceQueue<DBean>();
	
	private Bins data;
	private Class<?> dbclz;
	
	static {
		Thread dt = new Thread(new Rc());
		dt.setDaemon(true);
		dt.start();
	}



	private void init(Class<?> filter) {
		BType orderpf = null;
		while (orderpf == null || orderpf == inull) {
			orderpf = DBeani4.typemap.putIfAbsent(this.dbclz, inull);
			if (orderpf != null && orderpf != inull) {
				Bins lnb = new Bins();
				this.data = lnb;
				lnb.type = orderpf;
				lnb.datatrunk = orderpf.trunkCache.get();

				DBeani4.occupiedRes.put(new WeakReference<DBean>(this, DBeani4.clearedNoteQ), lnb);
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

		MPHKey[] mphpl = DefUtil.i.createMPHProfileorig(thefis);
		BType thebtp = new BType();
		thebtp.profile = mphpl;
		thebtp.trunkCache = ObjectCacheX.t.lookup(ObjectABuf.class, ObjectABuf.getCriteriaByCapacity(mphpl.length), 500);
		DBeani4.typemap.replace(this.dbclz, thebtp);
		return;

	}

	public DBeani4() {
		Class<?>[] clz = this.getClass().getInterfaces();
		if (clz.length == 1 && DBean.class.isAssignableFrom(clz[0])) {
			this.dbclz = clz[0];
			this.init(P.class);
		} else {
			throw new RuntimeException("dbean type not properly defined.");
		}
	}

	public DBeani4(Class<?> clz) {

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
		int tot = this.data.datatrunk.length;
		int idx = label.dhash(0) % tot;
		MPHKey thekey = this.data.type.profile[idx];
		if (thekey.hkey == label) {
			this.data.datatrunk[idx] = value;
			return;
		}

		int dh = thekey.jumpv;
		if (dh < 0)
			idx = 0 - dh - 1;
		else
			idx = label.dhash(dh) % tot;

		thekey = this.data.type.profile[idx];
		if (thekey.hkey == label) {
			this.data.datatrunk[idx] = value;
			return;
		} else
			throw new RuntimeException("invalid property name,no such property name.");

	}

	@Override
	public <T_> T_ g(L<T_> label) {

		int tot = this.data.datatrunk.length;
		int idx = label.dhash(0) % tot;
		MPHKey thekey = this.data.type.profile[idx];
		if (thekey.hkey == label)
			return (T_) this.data.datatrunk[idx];

		int dh = thekey.jumpv;
		if (dh < 0)
			idx = 0 - dh - 1;
		else
			idx = label.dhash(dh) % tot;

		thekey = this.data.type.profile[idx];
		if (thekey.hkey == label)
			return (T_) this.data.datatrunk[idx];
		else
			throw new RuntimeException("invalid property name,no such property name.");

	}

 

	public static class ConfBeani extends DBeani4 implements ConfBean {
		public ConfBeani(Class<?> clz) {
			super(clz);
		}
	}

	static class Rc implements Runnable {

		@Override
		public void run() {
			while (true) {
				try {

					Bins res = DBeani4.occupiedRes.remove(DBeani4.clearedNoteQ.remove());
					res.type.trunkCache.recycle(res.datatrunk);

				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}

		}

	}

}
