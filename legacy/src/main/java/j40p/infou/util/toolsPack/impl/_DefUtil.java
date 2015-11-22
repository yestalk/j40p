package j40p.infou.util.toolsPack.impl;

import j40p.infou.util.toolsPack.DefUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

class _DefUtil implements DefUtil {
	static final private HashMap<Class<?>, Class<?>> primitiveTypeMap = new HashMap<Class<?>, Class<?>>();

	static {
		_DefUtil.primitiveTypeMap.put(int.class, Integer.class);
		_DefUtil.primitiveTypeMap.put(long.class, Long.class);
		_DefUtil.primitiveTypeMap.put(short.class, Short.class);
		_DefUtil.primitiveTypeMap.put(char.class, Character.class);
		_DefUtil.primitiveTypeMap.put(byte.class, Byte.class);
		_DefUtil.primitiveTypeMap.put(double.class, Double.class);
		_DefUtil.primitiveTypeMap.put(float.class, Float.class);
		_DefUtil.primitiveTypeMap.put(boolean.class, Boolean.class);

		DefUtil.TypeDefaultValue.put(int.class, 0);
		DefUtil.TypeDefaultValue.put(long.class, 0l);
		DefUtil.TypeDefaultValue.put(short.class, 0);
		DefUtil.TypeDefaultValue.put(char.class, '0');
		DefUtil.TypeDefaultValue.put(byte.class, 0);
		DefUtil.TypeDefaultValue.put(double.class, 0.0d);
		DefUtil.TypeDefaultValue.put(float.class, 0.0f);
		DefUtil.TypeDefaultValue.put(boolean.class, false);

		DefUtil.TypeDefaultValue.put(Integer.class,
			DefUtil.TypeDefaultValue.get(int.class));
		DefUtil.TypeDefaultValue.put(Long.class,
			DefUtil.TypeDefaultValue.get(long.class));
		DefUtil.TypeDefaultValue.put(Short.class,
			DefUtil.TypeDefaultValue.get(short.class));
		DefUtil.TypeDefaultValue.put(Character.class,
			DefUtil.TypeDefaultValue.get(char.class));
		DefUtil.TypeDefaultValue.put(Byte.class,
			DefUtil.TypeDefaultValue.get(byte.class));
		DefUtil.TypeDefaultValue.put(Double.class,
			DefUtil.TypeDefaultValue.get(double.class));
		DefUtil.TypeDefaultValue.put(Float.class,
			DefUtil.TypeDefaultValue.get(float.class));
		DefUtil.TypeDefaultValue.put(Boolean.class,
			DefUtil.TypeDefaultValue.get(boolean.class));

		DefUtil.TypeDefaultValue.put(String.class, "");
		DefUtil.TypeDefaultValue.put(Date.class, new Date());
	};

	@Override
	public Object[] prepareArgs(Method md) {
		Class<?>[] mdtps = md.getParameterTypes();
		Object[] paramvs = new Object[mdtps.length];
		int ni =0;
		for(Class<?> i : mdtps){
			paramvs[ni++]=DefUtil.TypeDefaultValue.get(i);
		}
		return paramvs;
	}

	@Override
	public Class<?> mapPrimitive2Wrapper(Class<?> pri) {
		// TODO Auto-generated method stub
		return _DefUtil.primitiveTypeMap.get(pri);
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
		if(lcld==null || lcld.getParent()!=current)
			lcld=current;

		Object obj = Proxy.newProxyInstance(lcld,new Class[] { type }, self);
		return (T) obj;
	}

	private HashMap<String, Integer> fmap;

	private _DefUtil() {
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
			case _DefUtil.hashCode_code:
				return System.identityHashCode(target);
			case _DefUtil.equals_code:
				return target == param[0];
			case _DefUtil.toString_code:
				return target.getClass().getName() + "@"
					+ Integer.toHexString(System.identityHashCode(target))
					+ ", with InvocationHandler " + this;
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

	public HashMap<String, String> makeMap(HashMap<String, String> rthash,
		String... objs) {
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

	//@Override
	public int getSWT(Object ins) {
		Class<?> oc = ins.getClass();
		if(Proxy.isProxyClass(oc))
			throw new RuntimeException("not support proxy class.");//oc=oc.getInterfaces()[0];
		
		int rv = 0;

		try {
			Field ifield = oc.getField("swtid");
			ifield.setAccessible(true);
			rv = (Integer)ifield.get(oc);
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		return rv;
	}
	private AtomicLong swtidSeries= new AtomicLong(50l);
	Field lastfield;
	Class<?> lastclass;
	int lastrv;
	//@Override
	synchronized public int initSWTID(Class<?> clz) {
		int checklastrv=0;
		try {
			if(lastfield!=null && lastclass!=null && lastrv!=0){
				checklastrv = (Integer)this.lastfield.get(this.lastclass);
				if(checklastrv!=this.lastrv)
					throw new RuntimeException("some swtid asigned wrong.");
			}
		} catch (IllegalArgumentException e1) {
			throw new RuntimeException(e1);
		} catch (IllegalAccessException e1) {
			throw new RuntimeException(e1);
		}
		
		long rv = 0;
		int cuswt=0;
		try {
			Field ifield = clz.getField("swtid");
			ifield.setAccessible(true);
			cuswt = (Integer)ifield.get(clz);
			if(cuswt==0){
				rv = this.swtidSeries.incrementAndGet();
				this.lastrv=(int)rv;
				this.lastfield=ifield;
				this.lastclass=clz;
			}			
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalArgumentException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return this.lastrv;
	}

	@Override
	public HashMap<Class<?>, Integer> makeMap(Object... pairs) {
		HashMap<Class<?>, Integer> rvmap = new HashMap<Class<?>, Integer>();
		if((pairs.length % 2)==0){
			for(int i=0,len=pairs.length;i<len;){
				rvmap.put((Class<?>)pairs[i++], (Integer)pairs[i++]);
			}
		}else
			throw new RuntimeException("must be \"Class<?>, int\" pairs.");
		return rvmap;
	}
	
	
}
