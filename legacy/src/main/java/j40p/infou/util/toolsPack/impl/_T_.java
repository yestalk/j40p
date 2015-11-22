package j40p.infou.util.toolsPack.impl;

import j40p.infou.util.toolsPack.DefUtil;
import j40p.infou.util.toolsPack.T_;
import j40p.infou.util.toolsPack.def.P;
import j40p.infou.util.toolsPack.def.PList;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;


public class _T_ implements T_ {

	@Override
	public <T extends P<?>> T def(T nom){//,String ref) {
		
		Impl<?> rtv = new Impl();
		//rtv.ref=ref;
		return this.defas(nom, rtv);
	}
	@Override
	public <T extends PList<?>> T defList(T nom){//,String ref) {
		
		Impl<?> rtv = new _l();
		//rtv.ref=ref;
		return this.defas(nom, rtv);
	}
	
	private <T extends P<?>> T defas(T nom,Impl type) {
		// new Throwable().printStackTrace();
		Impl<?> rtv = type;
		StackTraceElement stre = Thread.currentThread().getStackTrace()[3];
		//new Throwable().printStackTrace();
		// System.out.println(stre.getMethodName());
		if (stre.getMethodName().equals("<clinit>")) {
			try {
				rtv.belongingClz=Class.forName(stre.getClassName());
				//System.out.println("clinit loader:"+rtv.belongingClz.getClassLoader().getClass());
			} catch (ClassNotFoundException e) {
				throw new RuntimeException();
			}
		}else
			throw new RuntimeException("only can be invoked at the class initi stage.");
		return (T) rtv;
	}

	private static AtomicReference<ConcurrentHashMap<Class<?>, Object>> clzinitmap =new AtomicReference<ConcurrentHashMap<Class<?>,Object>>(); 
	private static  ConcurrentHashMap<Class<?>, Object> getSetupMap(){
		ConcurrentHashMap<Class<?>, Object> ret =  new ConcurrentHashMap<Class<?>, Object>();
		boolean seted = _T_.clzinitmap.compareAndSet(null,ret );
		if(seted)
			return ret;
		else
			return _T_.clzinitmap.get();
	}
	private static  void clearSetupMap(){
		
		Thread td = new Thread(new Runnable() {
			
			@Override
			public void run() {
				ConcurrentHashMap<Class<?>, Object> ret = _T_.getSetupMap();
				try {
					Thread.sleep(1000*60*2);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(ret.size()==0)
					_T_.clzinitmap.compareAndSet(ret, null);
			}
		});
		td.setDaemon(true);
		td.start();
	}
	static private class _l<TE> extends Impl<TE> implements PList<TE> {}
	static private class Impl<TE> implements P<TE> {
		//static int id=0;
		private Class<?> belongingClz;
		private Type holded;
		volatile private String spname;
		private String fullname;
		//int _id=_.id++;
		//String ref;

		@Override
		public Class<?> getBelongingClazz() {
			return this.belongingClz;
		}

		@Override
		public String getSimpleName() {
			if (this.spname != null)
				return this.spname;
			else {
				Object inited = _T_.getSetupMap().putIfAbsent(this.belongingClz, DefUtil.placeHolderObject);
				do {
					
					if (inited == null) {// class not been initalized
						Field[] fds = this.belongingClz.getFields();
						for (Field i : fds) {
							Object lo;
							try {
								lo = i.get(this.belongingClz);
//								if(lo==this)
//									System.out.println("hereh!!!!!!!!!!!!!this!!!");
							} catch (IllegalArgumentException e) {
								throw new RuntimeException(e);
							} catch (IllegalAccessException e) {
								throw new RuntimeException(e);
							}
							if (lo instanceof Impl) {
								Impl<?> lt = (Impl<?>) lo;
								lt.spname = i.getName();
								
								//System.out.println("this.belongingClz==lt.belongingClz____"+(this.belongingClz==lt.belongingClz));
								//System.out.println("this.belongingClz.getClassLoader()==lt.belongingClz.getClassLoader()____"+(this.belongingClz.getClassLoader()==lt.belongingClz.getClassLoader()));
								//System.out.println("this.belongingClz:_"+this.belongingClz.getClassLoader().getClass());
								//System.out.println("lt.belongingClz:_"+lt.belongingClz.getClassLoader().getClass());
								//System.out.println("lt.spname:_"+lt.spname);
								
								lt.holded=i.getGenericType();
							}
						}
						//System.out.println("this.belongingClz loader:_"+this.belongingClz.getClassLoader().getClass());
						//System.out.println("this loader:_"+this.getClass().getClassLoader().getClass());
						//System.out.println("this.holded:_"+this.holded);
						//System.out.println("this.spname:_"+this.spname);
						//System.out.println("this.getClass:_"+this.getClass());
						_T_.getSetupMap().remove(Impl.this.belongingClz); // init finished.
						_T_.clearSetupMap();

					} else{
						Thread.yield();
					}
				} while (this.spname == null);
				return this.spname;
			}

		}

		@Override
		public String getPathName() {
			if (this.fullname != null)
				return this.fullname;
			else
				return this.fullname = this.belongingClz.getName() + "." + this.getSimpleName();
		}

//		@Override
//		public void setBelongingClazz(Class<?> clz) {
//			//System.out.println("!!!!" + clz.getName());
//			if(this.belongingClz==null)
//				this.belongingClz = clz;
//		}

		// @Override
		// public void setSimpleName(String spname) {
		// this.spname=spname;
		//
		// }

	}

}
