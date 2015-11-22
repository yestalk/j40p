package j40p.infou.util.toolsPack.def;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;

public class ConfClassHostClassLoader extends URLClassLoader {

	private HashSet<String> clznames;

	public ConfClassHostClassLoader(URL[] urls, ClassLoader parent,HashSet<String> clznames) {
		super(urls, parent);
		this.clznames=clznames;
	}

	protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		Class<?> rtv = null;
		if(this.inrange(name)){
			rtv = this.findClass(name);
			if (resolve) 
				this.resolveClass(rtv);
		}else
			rtv=super.loadClass(name, resolve);

		return rtv;

	}

	private boolean inrange(String name){
		if(this.clznames.contains(name))
			return true;
		for(String i : this.clznames){
			if(name.indexOf(i)>=0)
				return true;
		}
		return false;
	}


	@Override
	protected void finalize() throws Throwable {

		super.finalize();
		System.out.println("Oops! ConfClassHostClassLoader have been unloaded.");
	}

}