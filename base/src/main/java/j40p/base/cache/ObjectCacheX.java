package j40p.base.cache;

import java.lang.ref.SoftReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ObjectCacheX<E> {

	public interface Criteria<X> {
		boolean equals(Object obj);
		int hashCode();
	}

	public interface Manager {
		<C extends ObjectCacheX<?>> C lookup(Class<C> type, Criteria< C> cond, int csize);

	}
	
	public static final Manager t = new D();

	protected abstract void init(Criteria<?> param);

	protected abstract E createNewOne();

	protected abstract boolean checkOne(E x);

	

	private ArrayBlockingQueue<SoftReference<E>> abbf;

	protected ObjectCacheX(int csize) {
		this.abbf = new ArrayBlockingQueue<SoftReference<E>>(csize);
	}

	public E get() {

		while (true) {

			SoftReference<E> curef = this.abbf.poll();
			if (curef != null) {
				E rtv = curef.get();
				if (rtv == null)
					continue;
				else
					return rtv;
			} else
				return this.createNewOne();

		}
	}

	public void recycle(E a) {
		if (this.checkOne(a)) {

			this.abbf.offer(new SoftReference<E>(a));
		} else
			throw new RuntimeException("mismatched recycle bin");
	}

	private static class D implements Manager {
		private D() {
		};

		private ConcurrentHashMap<Criteria<?>, ObjectCacheX<?>> chbfa = new ConcurrentHashMap<Criteria<?>, ObjectCacheX<?>>();

		@Override
		public <C extends ObjectCacheX<?>> C lookup(Class<C> type, Criteria<C> cond, int csize) {
			ObjectCacheX<?> rtv, temp;
			try {
				if ((rtv = this.chbfa.get(cond)) == null) {

					Constructor<C> cs= type.getDeclaredConstructor(int.class);
					cs.setAccessible(true);
					rtv = temp =cs.newInstance(csize);
					rtv.init(cond);

					rtv = this.chbfa.putIfAbsent(cond, rtv);
					if (rtv == null)
						rtv = temp;
				}
				return (C) rtv;
			} catch (InstantiationException e) {
				new RuntimeException(e);
			} catch (IllegalAccessException e) {
				new RuntimeException(e);
			} catch (IllegalArgumentException e) {
				new RuntimeException(e);
			} catch (InvocationTargetException e) {
				new RuntimeException(e);
			} catch (NoSuchMethodException e) {
				new RuntimeException(e);
			} catch (SecurityException e) {
				new RuntimeException(e);
			}
			throw new RuntimeException("should not be there.");
		}
	}

}
