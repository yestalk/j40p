package j40p.infou.util.toolsPack.impl;

import j40p.infou.util.toolsPack.BeanUtil;
import j40p.infou.util.toolsPack.DefUtil;
import j40p.infou.util.toolsPack.InfoUtil;
import j40p.infou.util.toolsPack.InfoUtil.AugSession;
import j40p.infou.util.toolsPack.def.CastableBean;
import j40p.infou.util.toolsPack.def.CastableBean.Accessor;
import j40p.infou.util.toolsPack.impl._InfoUtil.AugSessionimpl;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

class _BeanUtil implements BeanUtil {	private _BeanUtil() {}
	private static final HashMap<Method, Integer> emptyAttrMap = new HashMap<Method, Integer>();
	//private static final Logger logger = LogManager.getLogger(_BeanUtil.class);



	
	private ConcurrentHashMap<Class<? extends CastableBean>, HashMap<Method, Integer>> typemap = new ConcurrentHashMap<Class<? extends CastableBean>, HashMap<Method, Integer>>();
	private ConcurrentHashMap<Class<? extends CastableBean>,Method[]> rttypemap = new ConcurrentHashMap<Class<? extends CastableBean>,Method[]>();
	//private ConcurrentWeakMap<Object, Object[]> proxyField = new ConcurrentWeakMap<Object, Object[]>();

	
	
	@Override
	public <T extends CastableBean> T get(Class<T> type) {
		// type.getInterfaces()[]

		while (true) {
			HashMap<Method, Integer> attrmap = this.typemap.putIfAbsent((Class<? extends CastableBean>) type, this.emptyAttrMap);
			if (attrmap == null) {// create and replace;
				TreeSet<Method> mset = new TreeSet<Method>(DefUtil.globelMethodComparator);
				mset.addAll(Arrays.asList(type.getMethods()));
				// String[] keys= new String[mset.size()];
				attrmap = new HashMap<Method, Integer>();
				Method[] rtvmap = new Method[mset.size()];
				int oit = 0;
				for (Method i : mset) {
					rtvmap[oit]=i;
					attrmap.put(i, oit++);
				}
				this.rttypemap.put((Class<? extends CastableBean>) type, rtvmap);
				this.typemap.replace((Class<? extends CastableBean>) type, attrmap);
			} else if (attrmap == this.emptyAttrMap) {
				Thread.yield();
			} else {
				Method[] rtvmap = this.rttypemap.get((Class<? extends CastableBean>) type);
				Object[] fobj = new Object[rtvmap.length];
				int ni =0;
				for(Method i : rtvmap){
					fobj[ni++]=DefUtil.TypeDefaultValue.get(i.getReturnType());
				}
				ClassLoader cld = Thread.currentThread().getContextClassLoader();
				//System.out.println("!!!!!!!_"+cld);
				if(cld==null)
					cld=_BeanUtil.class.getClassLoader();
				Object obj = Proxy.newProxyInstance(cld,new Class[] { type }, new CastableBeanHandler(fobj));
				return (T) obj;
			}
		}

	}

	public Object[] $values(CastableBean proxy) {
		
		//CastableBeanHandler cbh = (CastableBeanHandler)Proxy.getInvocationHandler(proxy);
		return ((CastableBeanHandler)Proxy.getInvocationHandler(proxy)).values;
		//return this.proxyField.get(proxy);
	}
	
	public Object[] $demovalues(CastableBean proxy) {
		
		Class<?>[] ltypes = this.$types(proxy);
		Object[] rtvs = ((CastableBeanHandler)Proxy.getInvocationHandler(proxy)).values;
		int ti = 0;
		for(Class<?> i : ltypes){
			if(i.isPrimitive())
				rtvs[ti]=DefUtil.TypeDefaultValue.get(i);
			ti++;
		}
		
		return rtvs;
		//return this.proxyField.get(proxy);
	}

	public String[] $keys(CastableBean proxy) {
		Method[] rtvmap = this.rttypemap.get(proxy.getClass().getInterfaces()[0]);
		String[] keys = new String[rtvmap.length];
		int oit = 0;
		for (Method i : rtvmap) {
			keys[oit++] = i.getName();
		}
		return keys;
	}
	
	



	public Class<?>[] $types(CastableBean proxy) {
		Method[] rtvmap = this.rttypemap.get(proxy.getClass().getInterfaces()[0]);
		Class<?>[] types = new Class<?>[rtvmap.length];
		int oit = 0;
		for (Method i : rtvmap) {
			types[oit++] = i.getReturnType();
		}
		return types;
	}

	public String[] $indices(CastableBean proxy) {
		Method[] rtvmap = this.rttypemap.get(proxy.getClass().getInterfaces()[0]);
		LinkedList<String> indls = new LinkedList<String>();
		for (Method i : rtvmap) {
			if ((i.getAnnotation(CastableBean.Index.class) == null))
				continue;
			indls.add(i.getName());
		}
		return indls.toArray(new String[] {});
	}
	
	




	@Override
	public String[] $helpinfo(CastableBean proxy) {
		Method[] rtvmap = this.rttypemap.get(proxy.getClass().getInterfaces()[0]);
		String[] rvstr= new String[rtvmap.length];
		int rvi = 0;
		for(Method i : rtvmap){
			CastableBean.Help help=  i.getAnnotation(CastableBean.Help.class);
			if(help!=null){
				rvstr[rvi]=help.value();
			}else
				rvstr[rvi]=DefUtil.blank;
			rvi++;
		}
		return rvstr;
	}






	@Override
	public Object[] $access(CastableBean cx, Accessor[] acc, Object[] inparam) {
		Object[] vz = this.$values(cx);
		Object[] rtv = new Object[acc.length] ;
		int rvi = 0;
		for(Accessor i : acc){
			if(i instanceof BeanAttrAccessor)
				rtv[rvi]=vz[((BeanAttrAccessor)i).sid];
			else
				rtv[rvi]=inparam[((ArgListAccessor)i).sid];
			rvi++;
		}
		System.out.println("after access");
		return rtv;
	}
	
	

	
	@Override
	public void $provide(CastableBean proxy, Map<String, Object> data) {
		String[] keys = this.$keys(proxy);
		Object[] values= ((CastableBeanHandler)((Proxy)proxy).getInvocationHandler(proxy)).values;
		if(keys.length!=values.length)
			throw new RuntimeException("i don't understand this.");
		int vi =0;
		for(String i: keys){
			values[vi++]=data.get(i);
		}
		
	}



	@Override
	public Class<?> $beantype(CastableBean proxy) {
		return proxy.getClass().getInterfaces()[0];
	}

	@Override
	public <T extends CastableBean> T getAsBindingLabel(Class<T> type,AugSession augse) {
		if(this.typemap.get(type)==null)
			this.get(type);
		Object obj = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),new Class[] { type }, new BindingLableHandler((AugSessionimpl)augse));
			
		return (T)obj;
	}
	
	public Accessor[] prepareAccessorList(Accessor[] accs,Class<? extends CastableBean>[] ref) {
		BeanAttrAccessor current = null;
		for(int i=0,u=0,lz=accs.length-1;i<=lz;i++){
			Accessor accsi = accs[i];
			if(accsi==null || !(accsi instanceof BeanAttrAccessor))
				accsi=new ArgListAccessor(u++);
			else{
				if(current!=null)
					if(!current.targetType().equals(accsi.targetType()))
						throw new RuntimeException("method only can binding to a single castablebean");
				current=(BeanAttrAccessor)accs[i];
			}
			
		}
		ref[0]=current.targetType();
		
		return accs;
	}

	static class CastableBeanHandler implements InvocationHandler {
		private Object[] values;
		public CastableBeanHandler(Object[] values) {
			this.values = values;
		}
	
		@Override
		public Object invoke(Object proxy, Method method, Object[] param) {
			if (!(proxy instanceof CastableBean))
				throw new RuntimeException("");
	
			Object rto = DefUtil.instance.mimicObj4Proxy(method, proxy, param);
			if (rto != null) 	
				return rto;
			else{
				CastableBeanHandler ftarget = (CastableBeanHandler)Proxy.getInvocationHandler(proxy);
				Class<?> ttype = proxy.getClass().getInterfaces()[0];	
				HashMap<Method, Integer> attrimap = ((_BeanUtil)BeanUtil.instance).typemap.get(ttype);
				if(param.length<1)
					throw new RuntimeException("the property parmameter must desing as variable length style  : proertyName(Type... value)");	
				Object beanparm=param[0];
				// HashMap<Method,Integer> attrimap =
				// this.df.typemap.get(ttype);
				int findx = attrimap.get(method);
				Object[] pfd = ftarget.values;
				Class<?> rttype = method.getReturnType();
				if (beanparm == null) { // set null case;
					pfd[findx] = null;
					if(rttype.isPrimitive())
						return DefUtil.TypeDefaultValue.get(rttype);
				}else if( beanparm.getClass().isArray()){
					if(Array.getLength(beanparm)<1){ // get case : array length == 0;
						Object rev = pfd[findx];
						if(rev == null && rttype.isPrimitive())
							return DefUtil.TypeDefaultValue.get(rttype);
						else
							return rev;
					}else{ // set value case; // also need return value is the return type is primitive type;
						pfd[findx] = Array.get(beanparm, 0);
						if(rttype.isPrimitive())
							return DefUtil.TypeDefaultValue.get(rttype);
					}
				}else
					throw new RuntimeException("the property parmameter must desing as variable length style  : proertyName(Type... value)");	
			} 
			return null;	
		}
	}

	static class BindingLableHandler implements InvocationHandler{
		AugSessionimpl augse;
		BindingLableHandler(AugSessionimpl augse){
			this.augse=augse;
		}
		@Override
		public Object invoke(Object proxy, Method method, Object[] param) throws Throwable {
			Class<?> ttype = proxy.getClass().getInterfaces()[0];	
			int sindex = ((_BeanUtil)BeanUtil.instance).typemap.get(ttype).get(method);
			this.augse.setBinding( 
				InfoUtil.instance.nvp(BeanUtil.bindingAccessor, 
				new BeanAttrAccessor(sindex,(Class<? extends CastableBean>)ttype)));
			Class<?> rttype = method.getReturnType();
			if(rttype.isPrimitive())
				return DefUtil.TypeDefaultValue.get(rttype);
			else
				return null;
		}
		
	}
	
	public static class BeanAttrAccessor implements Accessor{
		int sid;
		Class<? extends CastableBean>  target;
		BeanAttrAccessor(int sid ,Class<? extends CastableBean> target){
			this.sid=sid;
			this.target=target;
		}
		@Override
		public boolean isBeanAccessor() {
			return true;
		}
		@Override
		public Class<? extends CastableBean> targetType() {
			
			return this.target;
		}
		
		
	}
	static class ArgListAccessor implements Accessor{
		int sid;
		ArgListAccessor(int sid){
			this.sid=sid;
		}
		@Override
		public boolean isBeanAccessor() {
			return false;
		}
		@Override
		public Class<? extends CastableBean> targetType() {
			return null;
		}
		
		
	}
	
}
