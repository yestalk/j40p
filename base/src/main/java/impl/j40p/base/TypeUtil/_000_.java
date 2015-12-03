package impl.j40p.base.TypeUtil;

import j40p.base.DefUtil;
import j40p.base.TypeUtil;
import j40p.base.l.DRes;
import j40p.base.l.Func;
import j40p.base.l.L;
import j40p.base.l.Ls;
import j40p.base.l.P;
import j40p.base.l.Res;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public class _000_ implements TypeUtil {

	@Override
	public <T extends P<?>> T defProperty(T nom) {// ,String ref) {

		Impl<?> rtv = new _5();

		return (T) this.defas(rtv);
	}

	@Override
	public <T extends Ls<?>> T defList(T nom) {// ,String ref) {

		Impl<?> rtv = new _l();

		return (T) this.defas(rtv);
	}

	@Override
	public <T_ extends Res<?>> T_ defResPoint(T_ nom) {
		Impl<?> rtv = new _4();

		return (T_) this.defas(rtv);
	}

	@Override
	public <T_ extends DRes<?>> T_ defDResPoint(T_ nom) {
		Impl<?> rtv = new _3();

		return (T_) this.defas(rtv);
	}

	@Override
	public <T_ extends Func<?, ?>> T_ deFunc(T_ nom) {
		Impl<?> rtv = new _2();

		return (T_) this.defas(rtv);
	}

	private Object defas(Impl type) {
		// new Throwable().printStackTrace();
		Impl<?> rtv = type;
		StackTraceElement stre = Thread.currentThread().getStackTrace()[3];
		// new Throwable().printStackTrace();
		// System.out.println(stre.getMethodName());
		if (stre.getMethodName().equals("<clinit>")) {
			try {
				rtv.belongingClz = Class.forName(stre.getClassName());
				// System.out.println("clinit loader:"+rtv.belongingClz.getClassLoader().getClass());
			} catch (ClassNotFoundException e) {
				throw new RuntimeException();
			}
		} else
			throw new RuntimeException("only can be invoked at the class initi stage.");
		return rtv;
	}

	// private static AtomicReference<ConcurrentHashMap<Class<?>, Object>>
	// clzinitmap =new AtomicReference<ConcurrentHashMap<Class<?>,Object>>();
	// private static ConcurrentHashMap<Class<?>, Object> getSetupMap(){
	// ConcurrentHashMap<Class<?>, Object> ret = new ConcurrentHashMap<Class<?>,
	// Object>();
	// boolean seted = _000_.clzinitmap.compareAndSet(null,ret );
	// if(seted)
	// return ret;
	// else
	// return _000_.clzinitmap.get();
	// }
	// private static void clearSetupMap(){
	//
	// Thread td = new Thread(new Runnable() {
	//
	// @Override
	// public void run() {
	// ConcurrentHashMap<Class<?>, Object> ret = _000_.getSetupMap();
	// try {
	// Thread.sleep(1000*60*2);
	// } catch (InterruptedException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// if(ret.size()==0)
	// _000_.clzinitmap.compareAndSet(ret, null);
	// }
	// });
	// td.setDaemon(true);
	// td.start();
	// }
	static private class _4<_O> extends Impl<_O> implements Res<_O> {
	}

	static private class _3<_O> extends Impl<_O> implements DRes<_O> {
	}

	static private class _2<_O, _I> extends Impl<_O> implements Func<_O, _I> {
	}

	static private class _l<TE> extends Impl<TE> implements Ls<TE> {
	}

	static private class _5<TE> extends Impl<TE> implements P<TE> {
	}

	static private abstract class Impl<TE> implements L<TE> {
		// static int id=0;
		private Class<?> belongingClz;
		private Type holded;
		volatile private String spname;
		private String fullname;
		byte[] gid;
		int hashv = -1;

		// int _id=_.id++;
		// String ref;
		private byte[] getGid(){
			if (this.gid == null)
				return this.gid = this.getPathName().getBytes(StandardCharsets.UTF_8);
			else
				return this.gid;
		}
		@Override
		public int dhash(int d) {
			if (this.gid == null)
				this.gid = this.getPathName().getBytes(StandardCharsets.UTF_8);
			if (d == 0) {
				if (this.hashv == -1)
					this.hashv = DefUtil.i.dhashorig(0, this.gid);
				return this.hashv;
			} else{
				
				return DefUtil.i.dhash(d, this.gid);
			}

		}

		@Override
		public int hashCode() {
			return this.dhash(0);
		}

		@Override
		public int compareTo(Object o) {
			if (o instanceof Impl) {
				Impl<?> oh = (Impl<?>) o;
				byte[] shorty = (oh.getGid().length >= this.getGid().length) ? this.gid : oh.gid;
				boolean amishorty = shorty == this.gid;
				byte[] lengthy = (amishorty) ? oh.gid : this.gid;
				int out = 0;
				for (int i = 0, l = shorty.length - 1; i <= l; i++) {
					int stb = shorty[i] & 0x00ff;
					int lob = lengthy[i] & 0x00ff;

					if (stb == lob)
						continue;
					else if (stb > lob) {
						out = 1;
						break;
					} else {
						out = -1;
						break;
					}
				}
				if (out == 0) {
					if (shorty.length == lengthy.length)
						return 0;
					else
						return amishorty ? -1 : 1;
				} else {
					return amishorty ? out : -out;
				}
			} else {
				throw new RuntimeException("lable type does not match.");
			}

		}

 

		@Override
		public Class<?> getBelongingClz() {

			return this.belongingClz;
		}

		private void initbelongingclz(Class<?> clzbelonging) {
			// System.out.println("typeutil initbelongingclz:__"+clzbelonging);
			Field[] fds = this.belongingClz.getFields();
			for (Field i : fds) {
				Object lo;
				try {
					// i.setAccessible(true);
					lo = i.get(null);

				} catch (IllegalArgumentException e) {
					throw new RuntimeException(e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
				if (lo instanceof Impl) {
					Impl<?> lt = (Impl<?>) lo;
					lt.holded = i.getGenericType();
					//System.out.println("typename of lable:"+lt.holded.getTypeName());
					lt.spname = i.getName();

				}
			}
		}

		@Override
		public String getSimpleName() {
			if (this.spname != null)
				return this.spname;
			else {

				synchronized (this.belongingClz) {
					if (this.spname != null)
						return this.spname;
					this.initbelongingclz(this.belongingClz);
				}
				if (this.spname != null)
					return this.spname;
				else
					throw new RuntimeException("i don't understand, type simplename init.");
			}
		}

		@Override
		public String getPathName() {
			
			
			if (this.fullname != null)
				return this.fullname;
			else{
				this.fullname = (new StringBuilder()).append(belongingClz.getName().replace('.', '_')).append('_').append(this.getSimpleName()).toString();
				//System.out.println(this.holded.getTypeName());
				return this.fullname;
			}
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return this.getPathName();
		}

		// @Override
		// public void setBelongingClazz(Class<?> clz) {
		// //System.out.println("!!!!" + clz.getName());
		// if(this.belongingClz==null)
		// this.belongingClz = clz;
		// }

		// @Override
		// public void setSimpleName(String spname) {
		// this.spname=spname;
		//
		// }

	}

}
