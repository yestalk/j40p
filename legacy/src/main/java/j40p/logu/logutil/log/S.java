package j40p.logu.logutil.log;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public interface S {S i=new _();	
	<T> T ngleton(Class<T> iface);	
class _ implements S{private _(){if(S.i!=null)throw new RuntimeException("singleton violation when get instance of S");};
	
	@Override
	public <T> T ngleton(Class<T> iface) {
		Object rv = null;
		try {
			rv = iface.getField("i").get(iface);
		} catch (IllegalArgumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NoSuchFieldException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(rv!=null)
			return (T)rv;
		try {
			Class<?> c= Class.forName(iface.getName()+"impl",true,iface.getClassLoader());
			try {
				Constructor<?> cs =  c.getDeclaredConstructor(new Class<?>[]{});
				cs.setAccessible(true);
				try {
					return (T)cs.newInstance();
				} catch (InstantiationException e) {
					throw new RuntimeException(e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				} catch (IllegalArgumentException e) {
					throw new RuntimeException(e);
				} catch (InvocationTargetException e) {
					throw new RuntimeException(e);
				}
			} catch (NoSuchMethodException e) {
				throw new RuntimeException(e);
			} catch (SecurityException e) {
				throw new RuntimeException(e);
			}
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
		//SingleReturnClause.showStreamInfo(SingleReturnClause.class.getResourceAsStream(iface.getSimpleName()+".single"));
	}
	
}}
