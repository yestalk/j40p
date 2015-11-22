package j40p.base;


import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

public interface DefUtil {
	

	String placeHolderString = new String("");
	Object placeHolderObject = placeHolderString;
	String blank="<blank>";
	String[] stringArray ={};
	Object[] objArray ={};
	byte[] emptybtyes =new byte[0];
	UTF8ByteStr emptyUtf8Str=UTF8ByteStr.t.FromString(DefUtil.placeHolderString);
	Class<?>[] emptyParam = new Class<?>[]{};
	Boolean[] booleanenumvalues= new Boolean[]{true,false};
	
	int hashCode_code=0;
	int equals_code=1;
	int toString_code=2;
	
	
	
	
	Comparator<Method> globelMethodComparator = new GlobleMethodComparator();
	
	DefUtil i = new D();
	
	Object lookupDefaultValue(Class<?> clz);
	
	<T> T[] $array(Class<T> type,T... ts );
	int[] $arrayint(int... ints);
	
	<T> T[] arrayConcat(T[]... arrays);
	
	<T extends InvocationHandler> T getWrappedSelf(Class<T> type, T self);
	HashMap<String,String> makeMap(HashMap<String,String> rthash ,String... strvalues);
	Object mimicObj4Proxy(Method md,Object target,Object[] param);
	Class<?> mapPrimitive2Wrapper(Class<?> pri);
	
	Object[] prepareArgs(Method md);
	
	HashMap<Class<?>,Integer> makeMap(Object... pairs);
	
	class GlobleMethodComparator implements Comparator<Method>{
		//public static final GlobleMethodComparator i = new GlobleMethodComparator();
		private GlobleMethodComparator(){}
		@Override
		public int compare(Method o1, Method o2) {
			// TODO Auto-generated method stub
			
			return (o1.getDeclaringClass().getName()+o1.getName()).compareTo
					((o2.getDeclaringClass().getName()+o2.getName()));
		}
	}
	
	class D implements DefUtil{
		static final private HashMap<Class<?>, Object> TypeDefaultValue = new HashMap<Class<?>,Object>();
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

			D.TypeDefaultValue.put(Integer.class,
				D.TypeDefaultValue.get(int.class));
			D.TypeDefaultValue.put(Long.class,
				D.TypeDefaultValue.get(long.class));
			D.TypeDefaultValue.put(Short.class,
				D.TypeDefaultValue.get(short.class));
			D.TypeDefaultValue.put(Character.class,
				D.TypeDefaultValue.get(char.class));
			D.TypeDefaultValue.put(Byte.class,
				D.TypeDefaultValue.get(byte.class));
			D.TypeDefaultValue.put(Double.class,
				D.TypeDefaultValue.get(double.class));
			D.TypeDefaultValue.put(Float.class,
				D.TypeDefaultValue.get(float.class));
			D.TypeDefaultValue.put(Boolean.class,
				D.TypeDefaultValue.get(boolean.class));

			D.TypeDefaultValue.put(String.class, "");
			D.TypeDefaultValue.put(Date.class, new Date());
		};
		
 

		@Override
		public Object lookupDefaultValue(Class<?> clz) {
			// TODO Auto-generated method stub
			return D.TypeDefaultValue.get(clz);
		}

		@Override
		public Object[] prepareArgs(Method md) {
			Class<?>[] mdtps = md.getParameterTypes();
			Object[] paramvs = new Object[mdtps.length];
			int ni =0;
			for(Class<?> i : mdtps){
				paramvs[ni++]=D.TypeDefaultValue.get(i);
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
			if(lcld==null || lcld.getParent()!=current)
				lcld=current;

			Object obj = Proxy.newProxyInstance(lcld,new Class[] { type }, self);
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
}
