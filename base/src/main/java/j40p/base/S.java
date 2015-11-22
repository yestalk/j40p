package j40p.base;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;


public interface S {

    S i = new I();

    <T> T ngleton(Class<T> iface);

    class I implements S {

        private I() {

            if (S.i != null) {
                throw new RuntimeException("singleton violation when get instance of S");
            }
        };

        @Override
        public <T> T ngleton(Class<T> iface) {

            Object rv = null;
            try {
                Field ifield = iface.getField("i");
               
                ifield.setAccessible(true);
                rv = ifield.get(iface);

            } catch (IllegalArgumentException e1) {
                throw new RuntimeException(e1);
            } catch (SecurityException e1) {
                throw new RuntimeException(e1);
            } catch (IllegalAccessException e1) {
                throw new RuntimeException(e1);
            } catch (NoSuchFieldException e1) {
            	 System.out.println("which iface:_"+iface);
                throw new RuntimeException(e1);
            }
            if (rv != null)
                return (T) rv;
            try {

            	String ifacename = iface.getName();
            	ifacename="impl."+ifacename+"._000_";
//            	int splitpoint = ifacename.lastIndexOf(".")+1;
//            	String perfixname =ifacename.substring(0,splitpoint);
//            	ifacename=ifacename.substring(splitpoint);
//            	ifacename=perfixname+"impl"+"._"+ifacename;
            	
            	Class<?> c = Class.forName(ifacename, true, iface.getClassLoader());
                try {
                    Constructor<?> cs = c.getDeclaredConstructor(new Class<?>[]{});
                    cs.setAccessible(true);
                    try {
                        return (T) cs.newInstance();
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

    }
}
