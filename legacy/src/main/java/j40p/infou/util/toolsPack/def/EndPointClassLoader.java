package j40p.infou.util.toolsPack.def;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;

public class EndPointClassLoader extends URLClassLoader {
	private LinkedList<ClassLoader> plist;
	private Method findexistingclass;

	// private Method findclz;
	public EndPointClassLoader(URL[] urls, ClassLoader parent) {
		super(urls, parent);
		LinkedList<ClassLoader> lld = new LinkedList<ClassLoader>();
		lld.add(ClassLoader.getSystemClassLoader());
		ClassLoader ccl = this;
		while ((ccl = ccl.getParent()) != null) {
			lld.add(ccl);
		}
		if (lld.getLast() == lld.getFirst())
			lld.removeLast();
		this.plist = lld;
		try {
			// this.findclz=ClassLoader.class.getDeclaredMethod("findClass",
			// String.class);
			this.findexistingclass = ClassLoader.class.getDeclaredMethod("findLoadedClass", String.class);
			this.findexistingclass.setAccessible(true);
		} catch (SecurityException e) {
			throw new RuntimeException("no privilege to access findLoadedClass.");
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("findLoadedClass method no found.");
		}
	}

	protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		Class<?> rtv = null;
		for (ClassLoader i : this.plist) {
			try {
				rtv = (Class<?>) this.findexistingclass.invoke(i, new Object[] { name });
				if (rtv != null) {
					// System.out.println("saved:_ "+name);
					// this.resolveClass((Class<?>)rtv);
					return rtv;
				}
			} catch (IllegalArgumentException e) {
				throw new RuntimeException(e);
			} catch (IllegalAccessException e) {
				throw new RuntimeException(e);
			} catch (InvocationTargetException e) {
				throw new RuntimeException(e.getTargetException());
			}
		}

		rtv = this.findLoadedClass(name);
		if (rtv == null) {
			try {
				rtv = this.findClass(name);
				if (resolve) 
					this.resolveClass(rtv);
				return rtv;
			} catch (ClassNotFoundException ce) {
				// System.out.println("		pushbackname:_ "+name);
				rtv= super.loadClass(name, resolve);
			}
		
		}

		return rtv;

	}



	@Override
	protected void finalize() throws Throwable {

		super.finalize();
		System.out.println("Oops! EndPointClassLoader have been unloaded.");
	}

}


// protected Class<?> findClass(String name) throws ClassNotFoundException{
// // Object rtv = null;
// // for(ClassLoader i : this.plist){
// // try {
// // rtv = this.findexistingclass.invoke(i, new Object[]{name});
// // if(rtv!=null){
// // System.out.println("			everhere?:__"+name);
// // return (Class<?>)rtv;
// // }
// // } catch (IllegalArgumentException e) {
// // throw new RuntimeException(e);
// // } catch (IllegalAccessException e) {
// // throw new RuntimeException(e);
// // } catch (InvocationTargetException e) {
// // throw new RuntimeException(e.getTargetException());
// // }
// // }
// // if(rtv==null){
// try{
// Class<?> rtv =super.findClass(name);
// ///System.out.println("	endpoint:_"+name);
// return rtv;
// }catch(ClassNotFoundException ce){
// //System.out.println("		pushbackname:_ "+name);
// return this.getParent().loadClass(name);
// }
// // }
//
// //return (Class<?>)rtv;
// }