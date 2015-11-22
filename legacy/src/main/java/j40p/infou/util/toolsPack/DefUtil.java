package j40p.infou.util.toolsPack;

import j40p.infou.util.toolsPack.def.S;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashMap;

public interface DefUtil { // definition utility
	String blank="<blank>";
	String[] stringArray ={};
	Object[] objArray ={};
	Boolean[] booleanenumvalues= new Boolean[]{true,false};
	
	String placeHolderString = "";
	Object placeHolderObject = placeHolderString;
	
	int hashCode_code=0;
	int equals_code=1;
	int toString_code=2;
	
	
	HashMap<Class<?>, Object> TypeDefaultValue = new HashMap<Class<?>,Object>();

	Comparator<Method> globelMethodComparator = new GlobleMethodComparator();

	Class<?>[] emptyParam = new Class<?>[]{};
	
	<T> T[] $array(Class<T> type,T... ts );
	int[] $arrayint(int... ints);
	
	<T> T[] arrayConcat(T[]... arrays);
	
	<T extends InvocationHandler> T getWrappedSelf(Class<T> type, T self);
	HashMap<String,String> makeMap(HashMap<String,String> rthash ,String... strvalues);
	Object mimicObj4Proxy(Method md,Object target,Object[] param);
	Class<?> mapPrimitive2Wrapper(Class<?> pri);
	
	Object[] prepareArgs(Method md);
	
//	int getSWT(Object ins);
//	int initSWTID(Class<?> clz);
	HashMap<Class<?>,Integer> makeMap(Object... pairs);

	DefUtil instance = S.instance.singleton(DefUtil.class);// Default.i;
	

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

}
