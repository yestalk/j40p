package impl.j40p.base;

import j40p.base.ConfBean;
import j40p.base.Configurable;
import j40p.base.DBean;
import j40p.base.l.L;
import j40p.base.l.P;
import j40p.base.l.Res;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public abstract class DBeani implements DBean {
	// private static final TreeSet<Object> inull = new TreeSet<Object>();
	private static final ReentrantReadWriteLock editloc = new ReentrantReadWriteLock();
	private static final HashMap<Key, Integer> sid = new HashMap<DBeani.Key, Integer>();
	private static final HashMap<Class<?>, TreeSet<Object>> attrnum = new HashMap<Class<?>, TreeSet<Object>>();
	private static final ArrayBlockingQueue<Key> kcache = new ArrayBlockingQueue<DBeani.Key>(1000);

 

	public static class ConfBeani extends DBeani implements ConfBean {
		public ConfBeani(Class<?> clz) {
			super(clz);
		}
	}

	private Class<?> dbclz;
	private Object[] slot;

	private void init(Class<?> filter) {
		ReadLock rl = DBeani.editloc.readLock();
		WriteLock wl = DBeani.editloc.writeLock();
		rl.lock();
		TreeSet<Object> anum = DBeani.attrnum.get(this.dbclz);
		if (anum != null) {
			rl.unlock();
			this.slot = new Object[anum.size()];
			return;
		} else {
			rl.unlock();
			wl.lock();

			if ((anum = DBeani.attrnum.get(this.dbclz)) != null) {
				wl.unlock();
				this.slot = new Object[anum.size()];
				return;
			} else {
				this.setup(filter);
				wl.unlock();
				if (this.slot == null)
					throw new RuntimeException("i don't understand this, dbeani init.");
				return;
			}

		}
	}

	private void setup(Class<?> filter) {
		// System.out.println("dbeani  setup:__"+ this.dbclz);
		TreeSet<Object> thefis = new TreeSet<Object>();
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
				thefis.add(oi);
		}
		if (thefis.size() == 0)
			throw new RuntimeException("type definition don't have any property." + this.dbclz);
		int i = 0;
		for (Object si : thefis) {
			DBeani.sid.put(new Key(cuz, (L<?>) si, true), i);
			i++;
		}
		DBeani.attrnum.put(this.dbclz, thefis);

		this.slot = new Object[thefis.size()];
		return;

	}

	public DBeani() {
		Class<?>[] clz = this.getClass().getInterfaces();
		if (clz.length == 1 && DBean.class.isAssignableFrom(clz[0])) {
			this.dbclz = clz[0];
			this.init(P.class);
		} else {
			throw new RuntimeException("dbean type not properly defined.");
		}
	}

	public DBeani(Class<?> clz) {
		// LinkedList<Field> thefis = new LinkedList<Field>();
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

		ReadLock rl = DBeani.editloc.readLock();
		
		//WriteLock wl = DBeani.editloc.writeLock();
		Key qk = DBeani.kcache.poll();
		if (qk == null)
			qk = new Key(this.dbclz, label, false);
		else {
			qk.x[0] = this.dbclz;
			qk.x[1] = label;

		}
		rl.lock();
		Integer lsid = DBeani.sid.get(qk);
		rl.unlock();
		qk.clear();
		DBeani.kcache.offer(qk);

		if (lsid == null) {
			// System.out.println(DBeani.sid.size());
			// System.out.println(this.dbclz);
			// System.out.println(label);
			throw new RuntimeException("illigel key: " + label);
		} else
			this.slot[lsid] = value;

	}

	@Override
	public <T_> T_ g(L<T_> label) {

		ReadLock rl = DBeani.editloc.readLock();
		
		//WriteLock wl = DBeani.editloc.writeLock();
		Key qk = DBeani.kcache.poll();
		if (qk == null)
			qk = new Key(this.dbclz, label, false);
		else {
			qk.x[0] = this.dbclz;
			qk.x[1] = label;

		}
		rl.lock();
		Integer lsid = DBeani.sid.get(qk);
		rl.unlock();
		qk.clear();
		DBeani.kcache.offer(qk);

		if (lsid == null)
			throw new RuntimeException("illigel key");
		else {

			return (T_) this.slot[lsid];
		}
	}

	private static class Key {
		private Integer h = null;
		private Object[] x = new Object[2];
		private boolean lk = false;

		Key(Class<?> base, L<?> lable, boolean iskey) {
			this.x[0] = base;
			this.x[1] = lable;
			this.lk = iskey;
		}

		private void clear() {
			if (this.lk)
				return;
			this.h = null;
			this.x[0] = null;
			this.x[1] = null;

		}

		@Override
		public int hashCode() {
			if (this.h == null) {

				return this.h = Arrays.hashCode(this.x);

			} else
				return this.h;

		}

		@Override
		public boolean equals(Object obj) {
			return Arrays.equals(((Key) obj).x, this.x);

		}

	}

}
