package j40p.infou.util.toolsPack.impl;

import j40p.infou.util.toolsPack.DefUtil;
import j40p.infou.util.toolsPack.InfoUtil;
import j40p.infou.util.toolsPack.def.BuessinessLogicDriver;
import j40p.infou.util.toolsPack.def.ConfClassHostClassLoader;
import j40p.infou.util.toolsPack.def.EndPointClassLoader;
import j40p.infou.util.toolsPack.def.NVP;
import j40p.infou.util.toolsPack.def.P;
import j40p.infou.util.toolsPack.def.PMap;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

class _InfoUtil implements InfoUtil {

	@Override
	public <T> NVP nvp(P<T> type, T value) {
		return new NVP(type, value);
	}

	@Override
	public <T> T bind(T... values) {
		if (values.length > 0)
			return values[0];
		return null;
	}

	@Override
	public InfoSession getInfoSession(Object caller) {

		InfoSessionimpl infose = new InfoSessionimpl();
		// infose.ucloader=ucloader;
		// infose.pset=pset;
		return infose;
	}

	@Override
	public <T> T makeEventSource(Class<T> iface, T hook) {
		if (!iface.isInterface())
			throw new RuntimeException("only accept interface");
//		System.out.println("!!!!!!contextclassloader__"+Thread.currentThread().getContextClassLoader());
//		System.out.println("!!!!!!systemclassloader__"+ClassLoader.getSystemClassLoader());
//		System.out.println("!!!!!!ifaceclassclassloader__"+iface.getClass().getClassLoader());
//		System.out.println("!!!!!!argclassclassloader__"+EventSource.class.getClassLoader());
//		System.out.println("!!!!!!selfclassloader__"+this.getClass().getClassLoader());
		ClassLoader lcld = Thread.currentThread().getContextClassLoader();
		ClassLoader current = InfoUtil.EventSource.class.getClassLoader();
		if(lcld==null  || lcld.getParent()!=current)
			lcld=current;
		System.out.println("getContextClassLoader:__"+lcld);
		System.out.println("type class loader:__"+current.getClass().getClassLoader());
		return (T) Proxy.newProxyInstance(lcld, new Class<?>[] { InfoUtil.EventSource.class , iface}, new EventSourceHandler(hook));
	}

	private static class InfoSessionimpl implements InfoSession {
		public static final int composingStage = 0;

		// private LinkedList<Class<?>> conflist=new LinkedList<Class<?>>();

		private int configingStage = -1;
		private Generator<?> cugen;

		private _InfoUtil tools = (_InfoUtil) InfoUtil.instance;

		private HashMap<LinkedList<Class<?>>, Object> portalcache = new HashMap<LinkedList<Class<?>>, Object>();

		private HashMap<Method, PMap> methodinfos = new HashMap<Method, PMap>();
		private HashMap<Method, LinkedList<PMap>> paraminfos = new HashMap<Method, LinkedList<PMap>>();

		private HashMap<Class<?>, BuessinessLogicDriver> dirvermap = new HashMap<Class<?>, BuessinessLogicDriver>();

		@Override
		public <DRIVER extends BuessinessLogicDriver> InfoSession federate(Class<? super DRIVER> iface, DRIVER driver) {
			iface.getDeclaredClasses();
			Class<?>[] ifclzs = driver.getClass().getInterfaces();
			for (Class<?> ci : ifclzs) {
				ci.getDeclaredClasses();
				ci.getDeclaredAnnotations();
				ci.getDeclaredFields();
			}

			this.dirvermap.put(iface, driver);

			return this;
		}

		@Override
		public <PORTAL extends Portal<PORTAL>> PORTAL exchange4Portal(Generator<PORTAL> transfromer) {
			
			 LinkedList<Class<?>> conflist=new LinkedList<Class<?>>();
				
				Class<?> clz = transfromer.getClass();clz.getDeclaredClasses();
				ClassLoader clzl = clz.getClassLoader();
				URL jarurl = clz.getProtectionDomain().getCodeSource().getLocation();
				System.out.println("jarurl:_"+jarurl);
				EndPointClassLoader ucloader = new EndPointClassLoader(new URL[]{jarurl} ,clzl);
				
				PathSet pset = new _InfoUtil.PathSet(jarurl);
				for(String i : pset){
					//System.out.println("clzset:_"+i);
		            String className = i.substring(0,i.length()-6);
		            className = className.replace('/', '.');
		            Class<?> c=null;
					try {
						c = ucloader.loadClass(className);//Class.forName(className, ucloader);//
//						System.out.println(className);
//						System.out.println("	loader:_"+c.getClassLoader());
						this.tools.filterClass(conflist, c);
					} catch (ClassNotFoundException e1) {
						throw new RuntimeException("why class no found? i don't know.",e1);
					}
				}
				
				
				
				this.cugen=transfromer;
				Class<?> tranfclz = transfromer.getClass();
				Set<Class<?>> ifaceset = this.dirvermap.keySet();	
				TreeSet<Class<?>> templetset = new TreeSet<Class<?>>(new ClzNameComparetor());
				LinkedList<Class<?>> templetkey = new LinkedList<Class<?>>();
				templetset.addAll(ifaceset);
				templetkey.addAll(templetset);
				templetkey.add(tranfclz);
				Object portalrtv = this.portalcache.get(templetkey);
				if(portalrtv!=null)
					return (PORTAL)portalrtv;
				
				
				
				
				this.tools.filterClassesByBizIface(conflist, ifaceset.toArray(new Class<?>[]{}));
				LinkedList<Class<?>> composers = this.tools.filterClassesByGenerator(conflist, transfromer.getClass());
				HashSet<String> confclzset = new HashSet<String>();
				//HashSet<String> confclzsetsubset = new HashSet<String>();
				for(Class<?> clzi : composers)		
					confclzset.add(clzi.getName());

				for(Class<?> clzi : conflist)
					confclzset.add(clzi.getName());

				ConfClassHostClassLoader cfccl = new ConfClassHostClassLoader(ucloader.getURLs(), clzl, confclzset);
				
				ResovRun<PORTAL> comperrun = new ResovRun<PORTAL>();
				comperrun.infosimpl=this;
				comperrun.transfromer=transfromer;
				comperrun.composers=composers;
				comperrun.conflist=conflist;
				comperrun.cfccl=cfccl;
				comperrun.ifaceset=ifaceset;
				comperrun.templetkey=templetkey;
				Thread confrunth = new Thread(comperrun);
				confrunth.setContextClassLoader(cfccl);
				confrunth.start();
				try {
					confrunth.join();
				} catch (InterruptedException e1) {
					throw new RuntimeException(e1);
				}
				return comperrun.rtv;
		}
	}

	private static class ResovRun<PORTAL extends Portal<PORTAL>>  implements Runnable {
		InfoSessionimpl infosimpl;
		Generator<PORTAL> transfromer;
		
		LinkedList<Class<?>> composers;
		LinkedList<Class<?>> conflist;
		
		ConfClassHostClassLoader cfccl;
		Set<Class<?>> ifaceset;
		LinkedList<Class<?>> templetkey;
		
		
		PORTAL rtv;
		@Override
		public void run() {

		

//				for(String clznmi : confclzsetsubset){
//					try {
//						cfccl.loadClass(clznmi);
//					} catch (ClassNotFoundException e) {
//						throw new RuntimeException(e);
//					}
//				}
				
				
				Iterator<Class<?>> compit = composers.iterator();
				LinkedList<Class<?>> composers2= new LinkedList<Class<?>>();
				while(compit.hasNext()){
					Class<?> cucz= compit.next();
					try {
						composers2.add(cfccl.loadClass(cucz.getName()));
					} catch (ClassNotFoundException e) {
						throw new RuntimeException(e);
					}
					
				}
				composers=composers2;
				Iterator<Class<?>> conflistit = conflist.iterator();
				LinkedList<Class<?>> conflist2= new LinkedList<Class<?>>();
				while(conflistit.hasNext()){
					Class<?> cucz= conflistit.next();
					try {
						conflist2.add(cfccl.loadClass(cucz.getName()));
					} catch (ClassNotFoundException e) {
						throw new RuntimeException(e);

					}
				}
				conflist=conflist2;
				
				//ucloader=null;System.gc();
				
				
				Object valuedefault = transfromer.getDefaultAttrValue();
				for(Class<?> i : ifaceset){
					for(Method mi : i.getMethods()){
						PMap pmp = new PMap();
						pmp.loadDefault(valuedefault);
						Description mhelpinfo = mi.getAnnotation(Description.class);
						if(mhelpinfo!=null)
							pmp.add(InfoUtil.instance.nvp(InfoUtil.helpsInfo, mhelpinfo.value()));
						infosimpl.methodinfos.put(mi, pmp);
						LinkedList<PMap> arglist =new LinkedList<PMap>(); 
						infosimpl.paraminfos.put(mi,arglist);
						Annotation[][] paraannolistinfos = mi.getParameterAnnotations();
						int aid=0;
						for(Class<?> agri : mi.getParameterTypes()){

							pmp= new PMap();
							pmp.loadDefault(valuedefault);
							arglist.add(pmp);
							
							pmp.add(InfoUtil.instance.nvp(InfoUtil.helpsInfo,DefUtil.blank));
							pmp.add(InfoUtil.instance.nvp(InfoUtil.paramName,Integer.toString(aid)));
							
							Annotation[] pramannoinfo = paraannolistinfos[aid];
							if(pramannoinfo!=null){
								for(Annotation ai: pramannoinfo){
									if(ai instanceof Description){
										pmp.add(InfoUtil.instance.nvp(InfoUtil.helpsInfo, ((Description)ai).value()));
									}else if(ai instanceof ParamName){
										String paramnamestr =  ((ParamName)ai).value();
										//paramnamemap.add(paramnamestr);
										pmp.add(InfoUtil.instance.nvp(InfoUtil.paramName,paramnamestr));
									}
								}
							}
							aid++;
						}
						HashSet<String> paramnamemap = new HashSet<String>();
						for(PMap pmi : arglist){
							paramnamemap.add(pmi.get(InfoUtil.paramName));
						}
						if((paramnamemap.size())!=aid)
							throw new RuntimeException("duplicated paramName for method:_"+mi.getName());
						
					
						
					}
					
				}
				
				AugSessionimpl augsessionimp = new AugSessionimpl();
				augsessionimp.hq=infosimpl;
				
				
				Method auginfomd=null;
				try {
					auginfomd = AugInfo.class.getDeclaredMethod("setAugSession",AugSession.class);
				} catch (NoSuchMethodException e1) {
					throw new RuntimeException(e1);
				} catch (SecurityException e1) {
					throw new RuntimeException(e1);
				}
				
				for(Class<?> confc : conflist ){
					Object confins=null;
					try {
						confins = confc.newInstance();
					} catch (InstantiationException e) {
						throw new RuntimeException(e);
					} catch (IllegalAccessException e) {
						throw new RuntimeException(e);
					}
					
					try {
						//System.out.println("auginfomd Name:__"+auginfomd.getName());
						auginfomd.invoke(confins, new Object[]{augsessionimp});
					} catch (IllegalArgumentException e) {
						throw new RuntimeException(e);
					} catch (SecurityException e) {
						throw new RuntimeException(e);
					} catch (InvocationTargetException e) {
						throw new RuntimeException(e);
					} catch (IllegalAccessException e) {
						throw new RuntimeException(e);
					}
					Class<?> ifacebase =null;
					for(Class<?> ifacei : ifaceset){
						if(ifacei.isAssignableFrom(confc)){
							ifacebase=ifacei;
							break;
						}
					}
					if(ifacebase==null)
						throw new RuntimeException("something wrong with the class filter.");
					for(Method ifmi : ifacebase.getMethods()){
						try {
							Object[] mipms = DefUtil.instance.prepareArgs(ifmi);
							augsessionimp.augconfCheck=ifmi;
							ifmi.invoke(confins, mipms);
							augsessionimp.augconfCheck=null;
						} catch (IllegalArgumentException e) {
							throw new RuntimeException(e);
						} catch (IllegalAccessException e) {
							throw new RuntimeException(e);
						} catch (InvocationTargetException e) {
							throw new RuntimeException(e);
						}
					}
				}
				
				
				
				augsessionimp.augconfCheck=null;
				this.infosimpl.configingStage=InfoSessionimpl.composingStage;
	
				for(Class<?> compi : this.composers){
					Composer cpz=null;
					try {
						cpz = (Composer)compi.newInstance();
						
					} catch (InstantiationException e) {
						throw new RuntimeException(e);
					} catch (IllegalAccessException e) {
						throw new RuntimeException(e);
					}
					cpz.info(augsessionimp);
				}
				
				
				
				
				PORTAL rtv = transfromer.generate(infosimpl.methodinfos, infosimpl.paraminfos);
				this.infosimpl.portalcache.put(templetkey, rtv);
				
				rtv.linkingWithDrivers(infosimpl.dirvermap);
				HashMap<Method, LinkedList<DataNoticeListener>> hmp = rtv.getEventCaseMap();
				for(BuessinessLogicDriver bidi : infosimpl.dirvermap.values()){
					EventSource[] esources = bidi.getEventSource();
					if(esources!=null){
						for(EventSource ei : esources){
							ei.setCaseListeners(hmp);
						}
					}
				}
				
			this.rtv=rtv;
		}
	}

	private static class ConfRun implements Runnable {
		AugSessionimpl augsessionimp;
		LinkedList<Class<?>> composers;

		// LinkedList<Class<?>> ugs;
		@Override
		public void run() {

			for (Class<?> compi : composers) {
				Composer cpz = null;
				try {
					cpz = (Composer) compi.newInstance();

				} catch (InstantiationException e) {
					throw new RuntimeException(e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				}
				cpz.info(augsessionimp);
			}

		}

	}

	public static class AugSessionimpl implements AugSession {
		// selector cache
		private HashMap<Class<?>, Object> mdselectormap = new HashMap<Class<?>, Object>();

		private InfoSessionimpl hq;
		private Method augconfCheck;

		private PMap mdattr;
		private LinkedList<PMap> mdparamattr;
		private NVP bindingAcc;

		private NVP madeNVP;

		public void setBinding(NVP bd) {
			this.bindingAcc = bd;
		}

		@Override
		public <TP> NVP vpair(P<TP> lable, TP value) {
			return InfoUtil.instance.nvp(lable, value);
		}

		@Override
		public void augCurrentMethod(NVP... pz) {
			if (this.hq.configingStage == InfoSessionimpl.composingStage)
				throw new RuntimeException("using augCurrentMethod(NVP... pz) method during composing stage in denied because of ambiguity");
			PMap lmdattr = new PMap();
			// if(this.mdattr==null){
			// this.mdattr=new PMap();
			// System.out.println("aug current method:_");
			// for(NVP pzi:pz){
			// System.out.println("	"+this.augconfCheck.getName());
			// System.out.println("		"+pzi.value);
			// }
			// }
			lmdattr.add(pz);
			PMap mdpropset = this.hq.methodinfos.get(this.augconfCheck);
			// System.out.println("	"+this.augconfCheck.getName());

			mdpropset.merge(lmdattr);

			this.mdattr = null;
			this.mdparamattr = null;
		}

		@Override
		public <TP> TP augMethod(Class<TP> iface, NVP... pz) {
			if (this.mdattr == null)
				this.mdattr = new PMap();
			this.mdattr.add(pz);
			return this.getMSelector(iface);
		}

		@Override
		public <TP> TP make(P<Method> label, Class<TP> clz) {
			TP lms = this.getMSelector(clz);
			((MethodSelector) Proxy.getInvocationHandler(lms)).mdattrlable = label;
			return lms;
		}

		@Override
		public NVP mpair(Object... obj) {

			return this.madeNVP;// download;
		}

		@Override
		public <TP> TP augParam(TP value, NVP... pz) {
			PMap paramattr = new PMap();
			paramattr.add(pz);
			if (this.bindingAcc != null) {
				paramattr.add(this.bindingAcc);
				this.bindingAcc = null;
			}
			if (this.mdparamattr == null)
				this.mdparamattr = new LinkedList<PMap>();
			this.mdparamattr.add(paramattr);
			return value;
		}

		private <T> T getMSelector(Class<T> iface) {
			if (iface.isInterface()) {
				Object rtv = this.mdselectormap.get(iface);
				if (rtv != null)
					return (T) rtv;
				MethodSelector mds = new MethodSelector();
				mds.aughub = this;
				return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[] { iface }, mds);
			} else
				throw new RuntimeException("passed in must a interface class type.");
		}

	}

	private static class MethodSelector implements InvocationHandler {
		private AugSessionimpl aughub;
		private P<Method> mdattrlable;

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			if (mdattrlable != null) {
				aughub.madeNVP = InfoUtil.instance.nvp(this.mdattrlable, method);// upload,
																					// would
																					// be
																					// downloaded
																					// at
																					// next
																					// step.
				this.mdattrlable = null;
			} else {// conclusion
				if (this.aughub.hq.configingStage == InfoSessionimpl.composingStage && this.aughub.hq.cugen instanceof InfoUtil.SessionAwareGenerator) {
					((SessionAwareGenerator) this.aughub.hq.cugen).commitSession(method, this.aughub.mdattr, this.aughub.mdparamattr);
				}

				PMap mdpropset = this.aughub.hq.methodinfos.get(method);
				mdpropset.merge(this.aughub.mdattr);

				if (this.aughub.mdparamattr != null) {
					LinkedList<PMap> mdparampropset = this.aughub.hq.paraminfos.get(method);
					if (mdparampropset.size() != this.aughub.mdparamattr.size())
						throw new RuntimeException("args list not properly configed.");

					Iterator<PMap> pmiit = this.aughub.mdparamattr.iterator();
					for (PMap parami : mdparampropset) {
						parami.merge(pmiit.next());
					}
				}

				aughub.mdattr = null;
				aughub.mdparamattr = null;
				// aughub.augconfCheck=null;
			}
			return DefUtil.TypeDefaultValue.get(method.getReturnType());
		}

	}

	static class ClzNameComparetor implements Comparator {

		@Override
		public int compare(Object c1, Object c2) {

			return ((Class<?>) c1).getCanonicalName().compareTo(((Class<?>) c2).getCanonicalName());
		}

	}

	static class EventSourceHandler implements InvocationHandler {
		ConcurrentHashMap<Method, LinkedList<DataNoticeListener>> caseMap;

		EventSourceHandler(Object hook) {
			this.hook = hook;
		}

		Object hook;

		@Override
		public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			Object robj = null;
			// System.out.println("method belonging:___"+method.getDeclaringClass());
			if (this.caseMap == null && EventSource.class.isAssignableFrom(method.getDeclaringClass())) {// the
																											// setEventCase
																											// method
																											// of
																											// EventSource
				// System.out.println("!!!!!set the case map.......");
				ConcurrentHashMap<Method, LinkedList<DataNoticeListener>> ch = new ConcurrentHashMap<Method, LinkedList<DataNoticeListener>>();
				ch.putAll((HashMap<Method, LinkedList<DataNoticeListener>>) args[0]);
				this.caseMap = ch;
				// this.hook=args[1];
			} else {
				if (hook != null)
					robj = method.invoke(this.hook, args);
				// System.out.println("case map null:___"+(this.caseMap==null));
				if (this.caseMap != null) {
					LinkedList<DataNoticeListener> dlsners = this.caseMap.get(method);
					//System.out.println("notice handler method name:_"+method);
					if (dlsners != null) {
						for (DataNoticeListener di : dlsners) {
							//System.out.println("ami methodname:_"+((AsyncAmiMethodIfaceImpl)di).getName());
							di.onNotice(proxy, method, args);
						}
					}
				}
			}
			return robj;
		}

	}

	static class PathSet implements Iterable<String> {
		URL res;

		PathSet(URL res) {
			this.res = res;
		}

		@Override
		public Iterator<String> iterator() {
			// System.out.println("here?");
			File fl = null;
			
				
			fl = new File(this.res.getFile());
			

			if (fl.isDirectory()) {
				return new PathIt(fl);
			} else if (fl.getName().endsWith(".jar")) {
				try {
					return new JarIt(new JarFile(fl));
				} catch (IOException e) {
					throw new RuntimeException(e);

				}

			} else {
				throw new RuntimeException("only suppor jar file and folder");
			}

		}

	}

	static class PathIt implements Iterator<String> {
		File[] clzPathNames;
		int pointer = 0;
		int lz;
		int headsz;

		PathIt(File dir) {
			this.headsz = dir.getAbsolutePath().length() + 1;
			this.clzPathNames = this.travelsalDir(dir, null);
			this.lz = clzPathNames.length - 1;
		}

		@Override
		public boolean hasNext() {

			return this.pointer <= this.lz;
		}

		@Override
		public String next() {
			String outstr = this.clzPathNames[this.pointer++].getAbsolutePath();
			return outstr.substring(this.headsz, outstr.length()).replace('\\', '/');
		}

		private File[] travelsalDir(File directory, LinkedList<File> files) {
			if (files == null)
				files = new LinkedList<File>();
			ClassNameFl ffter = new ClassNameFl();
			// File directory = new File(directoryName);

			// get all the files from a directory
			File[] fList = directory.listFiles(ffter);
			for (File file : fList) {
				if (file.isFile()) {
					files.add(file);
				} else if (file.isDirectory()) {
					this.travelsalDir(file, files);
				}
			}
			return files.toArray(new File[] {});
		}

	}

	static class ClassNameFl implements FileFilter {

		@Override
		public boolean accept(File fl) {
			// System.out.println(fl);
			return fl.getName().endsWith(".class") || fl.isDirectory();
		}

	}

	static class JarIt implements Iterator<String> {
		Enumeration<JarEntry> ej;

		JarIt(JarFile jf) {
			this.ej = jf.entries();
		}

		@Override
		public boolean hasNext() {

			return ej.hasMoreElements();
		}

		@Override
		public String next() {
			JarEntry je = null;
			while (true) {
				je = ej.nextElement();
				if (je.isDirectory() || !je.getName().endsWith(".class"))
					continue;
				else
					break;
			}
			return je.getName();
		}

	}

	private void filterClass(LinkedList<Class<?>> rtlist, Class<?> clz) {

		// System.out.println("filtering:_"+clz);
		boolean istmp = InfoUtil.Composer.class.isAssignableFrom(clz);
		boolean isauginfo = InfoUtil.AugInfo.class.isAssignableFrom(clz);
		UesedBy useinfo = clz.getAnnotation(UesedBy.class);
		if ((istmp || isauginfo) && useinfo != null && useinfo.value() != null && useinfo.value().length > 0)
			rtlist.add(clz);

	}

	private void filterClassesByBizIface(LinkedList<Class<?>> clzs, Class<?>[] ifaces) {
		Iterator<Class<?>> it = clzs.iterator();
		while (it.hasNext()) {
			Class<?> i = it.next();
			boolean istmp = InfoUtil.Composer.class.isAssignableFrom(i);
			if (!istmp) {
				boolean ok = false;
				for (Class<?> ifi : ifaces) {
					ok = ifi.isAssignableFrom(i);
					if (ok == true)
						break;
				}
				if (!ok)
					it.remove();
			}
		}
	}

	private LinkedList<Class<?>> filterClassesByGenerator(LinkedList<Class<?>> clzs, Class<? extends Generator> generator) {

		Iterator<Class<?>> it = clzs.iterator();
		while (it.hasNext()) {
			Class<?> ci = it.next();
			UesedBy glist = ci.getAnnotation(UesedBy.class);
			Class<? extends Generator<?>>[] ugs = glist.value();
			boolean ok = false;
			for (Class<? extends Generator<?>> i : ugs) {
				if (generator.equals(i)) {
					ok = true;
					break;
				}
			}
			if (!ok)
				it.remove();
		}

		LinkedList<Class<?>> compserlist = new LinkedList<Class<?>>();
		it = clzs.iterator();
		while (it.hasNext()) {
			Class<?> ci = it.next();
			if (Composer.class.isAssignableFrom(ci)) {
				it.remove();
				compserlist.add(ci);
			}

		}
		return compserlist;
	}

}

//
// JarFile jarFile=null;
// try {
// jarFile = new JarFile(jarurl.getFile());
// } catch (IOException e2) {
// throw new RuntimeException(e2);
// }
// Enumeration<JarEntry> ej = jarFile.entries();
//
//
//
// while (ej.hasMoreElements()) {
// JarEntry je = (JarEntry) ej.nextElement();
// if(je.isDirectory() || !je.getName().endsWith(".class")){
// continue;
// }
// // -6 because of .class
// String className = je.getName().substring(0,je.getName().length()-6);
// className = className.replace('/', '.');
// Class<?> c=null;
// try {
// c = Class.forName(className, true, ucloader);//ucloader.loadClass(className);
// this.filterClass(rtvlist, c);
// } catch (ClassNotFoundException e1) {
// throw new RuntimeException("why class no found? i don't know.",e1);
// }
//
// }

// @Override
// public InfoSession getInfoSession(Object caller) {
// Class<?> clz = caller.getClass();
// ClassLoader clzl = clz.getClassLoader();
// URL jarurl = clz.getProtectionDomain().getCodeSource().getLocation();
// System.out.println("jarurl:_"+jarurl);
// EndPointClassLoader ucloader = new EndPointClassLoader(new URL[]{jarurl}
// ,clzl);
//
//
// LinkedList<Class<?>> rtvlist = new LinkedList<Class<?>>();
//
// PathSet pset = new PathSet(jarurl);
// for(String i : pset){
// //System.out.println("clzset:_"+i);
// String className = i.substring(0,i.length()-6);
// className = className.replace('/', '.');
// Class<?> c=null;
// try {
// c = ucloader.loadClass(className,true);//Class.forName(className, true,
// ucloader)
// // System.out.println(className);
// // System.out.println("	loader:_"+c.getClassLoader());
// this.filterClass(rtvlist, c);
// } catch (ClassNotFoundException e1) {
// throw new RuntimeException("why class no found? i don't know.",e1);
// }
// }
// System.out.println("rtvlistsize:_"+rtvlist.size());
//
// InfoSessionimpl infose = new InfoSessionimpl();
// infose.conflist=rtvlist;
// return infose;
// }

// @Override
// public <PORTAL extends Portal<PORTAL>> PORTAL
// exchange4Portal(Generator<PORTAL> transfromer) {
//
// LinkedList<Class<?>> conflist=new LinkedList<Class<?>>();
//
// Class<?> clz = transfromer.getClass();clz.getDeclaredClasses();
// ClassLoader clzl = clz.getClassLoader();
// URL jarurl = clz.getProtectionDomain().getCodeSource().getLocation();
// System.out.println("jarurl:_"+jarurl);
// EndPointClassLoader ucloader = new EndPointClassLoader(new URL[]{jarurl}
// ,clzl);
//
// PathSet pset = new _InfoUtil.PathSet(jarurl);
// for(String i : pset){
// //System.out.println("clzset:_"+i);
// String className = i.substring(0,i.length()-6);
// className = className.replace('/', '.');
// Class<?> c=null;
// try {
// c = ucloader.loadClass(className);//Class.forName(className, ucloader);//
// // System.out.println(className);
// // System.out.println("	loader:_"+c.getClassLoader());
// this.tools.filterClass(conflist, c);
// } catch (ClassNotFoundException e1) {
// throw new RuntimeException("why class no found? i don't know.",e1);
// }
// }
//
//
//
// this.cugen=transfromer;
// Class<?> tranfclz = transfromer.getClass();
// Set<Class<?>> ifaceset = this.dirvermap.keySet();
// TreeSet<Class<?>> templetset = new TreeSet<Class<?>>(new
// ClzNameComparetor());
// LinkedList<Class<?>> templetkey = new LinkedList<Class<?>>();
// templetset.addAll(ifaceset);
// templetkey.addAll(templetset);
// templetkey.add(tranfclz);
// Object portalrtv = this.portalcache.get(templetkey);
// if(portalrtv!=null)
// return (PORTAL)portalrtv;
//
//
//
//
// this.tools.filterClassesByBizIface(conflist, ifaceset.toArray(new
// Class<?>[]{}));
// LinkedList<Class<?>> composers =
// this.tools.filterClassesByGenerator(conflist, transfromer.getClass());
// HashSet<String> confclzset = new HashSet<String>();
// //HashSet<String> confclzsetsubset = new HashSet<String>();
//
//
// ConfClassHostClassLoader cfccl = new
// ConfClassHostClassLoader(ucloader.getURLs(), clzl, confclzset);
// new
// for(Class<?> clzi : composers){
//
// confclzset.add(clzi.getName());
// Class<?>[] clzidclzes = clzi.getDeclaredClasses();
// for(Class<?> clzidclzesi: clzidclzes){
// confclzset.add(clzidclzesi.getName());
// //confclzsetsubset.add(clzidclzesi.getName());
// }
// }
// for(Class<?> clzi : conflist){
// confclzset.add(clzi.getName());
// Class<?>[] clzidclzes = clzi.getDeclaredClasses();
// for(Class<?> clzidclzesi: clzidclzes){
// confclzset.add(clzidclzesi.getName());
// //confclzsetsubset.add(clzidclzesi.getName());
// }
// }
// for(String clznmi : confclzsetsubset){
// try {
// cfccl.loadClass(clznmi);
// } catch (ClassNotFoundException e) {
// throw new RuntimeException(e);
// }
// }

// Iterator<Class<?>> compit = composers.iterator();
// LinkedList<Class<?>> composers2= new LinkedList<Class<?>>();
// while(compit.hasNext()){
// Class<?> cucz= compit.next();
// try {
// composers2.add(cfccl.loadClass(cucz.getName()));
// } catch (ClassNotFoundException e) {
// throw new RuntimeException(e);
// }
//
// }
// composers=composers2;
// Iterator<Class<?>> conflistit = conflist.iterator();
// LinkedList<Class<?>> conflist2= new LinkedList<Class<?>>();
// while(conflistit.hasNext()){
// Class<?> cucz= conflistit.next();
// try {
// conflist2.add(cfccl.loadClass(cucz.getName()));
// } catch (ClassNotFoundException e) {
// throw new RuntimeException(e);
//
// }
// }
// conflist=conflist2;
//
// //ucloader=null;System.gc();
//
//
// Object valuedefault = transfromer.getDefaultAttrValue();
// for(Class<?> i : ifaceset){
// for(Method mi : i.getMethods()){
// PMap pmp = new PMap();
// pmp.loadDefault(valuedefault);
// Description mhelpinfo = mi.getAnnotation(Description.class);
// if(mhelpinfo!=null)
// pmp.add(InfoUtil.instance.nvp(InfoUtil.helpsInfo, mhelpinfo.value()));
// this.methodinfos.put(mi, pmp);
// LinkedList<PMap> arglist =new LinkedList<PMap>();
// this.paraminfos.put(mi,arglist);
// Annotation[][] paraannolistinfos = mi.getParameterAnnotations();
// int aid=0;
// for(Class<?> agri : mi.getParameterTypes()){
//
// pmp= new PMap();
// pmp.loadDefault(valuedefault);
// arglist.add(pmp);
//
// pmp.add(InfoUtil.instance.nvp(InfoUtil.helpsInfo,DefUtil.blank));
// pmp.add(InfoUtil.instance.nvp(InfoUtil.paramName,Integer.toString(aid)));
//
// Annotation[] pramannoinfo = paraannolistinfos[aid];
// if(pramannoinfo!=null){
// for(Annotation ai: pramannoinfo){
// if(ai instanceof Description){
// pmp.add(InfoUtil.instance.nvp(InfoUtil.helpsInfo,
// ((Description)ai).value()));
// }else if(ai instanceof ParamName){
// String paramnamestr = ((ParamName)ai).value();
// //paramnamemap.add(paramnamestr);
// pmp.add(InfoUtil.instance.nvp(InfoUtil.paramName,paramnamestr));
// }
// }
// }
// aid++;
// }
// HashSet<String> paramnamemap = new HashSet<String>();
// for(PMap pmi : arglist){
// paramnamemap.add(pmi.get(InfoUtil.paramName));
// }
// if((paramnamemap.size())!=aid)
// throw new RuntimeException("duplicated paramName for method:_"+mi.getName());
//
//
//
// }
//
// }
//
// AugSessionimpl augsessionimp = new AugSessionimpl();
// augsessionimp.hq=this;
//
//
// Method auginfomd=null;
// try {
// auginfomd =
// AugInfo.class.getDeclaredMethod("setAugSession",AugSession.class);
// } catch (NoSuchMethodException e1) {
// throw new RuntimeException(e1);
// } catch (SecurityException e1) {
// throw new RuntimeException(e1);
// }
//
// for(Class<?> confc : conflist ){
// Object confins=null;
// try {
// confins = confc.newInstance();
// } catch (InstantiationException e) {
// throw new RuntimeException(e);
// } catch (IllegalAccessException e) {
// throw new RuntimeException(e);
// }
//
// try {
// //System.out.println("auginfomd Name:__"+auginfomd.getName());
// auginfomd.invoke(confins, new Object[]{augsessionimp});
// } catch (IllegalArgumentException e) {
// throw new RuntimeException(e);
// } catch (SecurityException e) {
// throw new RuntimeException(e);
// } catch (InvocationTargetException e) {
// throw new RuntimeException(e);
// } catch (IllegalAccessException e) {
// throw new RuntimeException(e);
// }
// Class<?> ifacebase =null;
// for(Class<?> ifacei : ifaceset){
// if(ifacei.isAssignableFrom(confc)){
// ifacebase=ifacei;
// break;
// }
// }
// if(ifacebase==null)
// throw new RuntimeException("something wrong with the class filter.");
// for(Method ifmi : ifacebase.getMethods()){
// try {
// Object[] mipms = DefUtil.instance.prepareArgs(ifmi);
// augsessionimp.augconfCheck=ifmi;
// ifmi.invoke(confins, mipms);
// augsessionimp.augconfCheck=null;
// } catch (IllegalArgumentException e) {
// throw new RuntimeException(e);
// } catch (IllegalAccessException e) {
// throw new RuntimeException(e);
// } catch (InvocationTargetException e) {
// throw new RuntimeException(e);
// }
// }
// }
//
//
//
// augsessionimp.augconfCheck=null;
// this.configingStage=InfoSessionimpl.composingStage;
// ConfRun comperrun = new ConfRun();
// comperrun.composers=composers;
// comperrun.augsessionimp=augsessionimp;
// Thread confrunth = new Thread(comperrun);
// confrunth.setContextClassLoader(cfccl);
// confrunth.start();
// try {
// confrunth.join();
// } catch (InterruptedException e1) {
// throw new RuntimeException(e1);
// }
//
//
// for(Class<?> compi : composers){
// Composer cpz=null;
// try {
// cpz = (Composer)compi.newInstance();
//
// } catch (InstantiationException e) {
// throw new RuntimeException(e);
// } catch (IllegalAccessException e) {
// throw new RuntimeException(e);
// }
// cpz.info(augsessionimp);
// }
//
//
//
//
// PORTAL rtv = transfromer.generate(this.methodinfos, this.paraminfos);
// this.portalcache.put(templetkey, rtv);
//
// rtv.linkingWithDrivers(this.dirvermap);
// HashMap<Method, LinkedList<DataNoticeListener>> hmp = rtv.getEventCaseMap();
// for(BuessinessLogicDriver bidi : this.dirvermap.values()){
// EventSource[] esources = bidi.getEventSource();
// if(esources!=null){
// for(EventSource ei : esources){
// ei.setCaseListeners(hmp);
// }
// }
// }
//
// return rtv;
// }
//
// }
