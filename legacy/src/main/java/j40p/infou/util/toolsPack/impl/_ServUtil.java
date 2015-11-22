package j40p.infou.util.toolsPack.impl;

import j40p.infou.util.toolsPack.ServUtil;
import j40p.infou.util.toolsPack.def.BuessinessLogicDriver;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;

class _ServUtil implements ServUtil {

	private _ServUtil() {
	}

	static final _ServUtil instance = (_ServUtil) ServUtil.instance;

	//private ConcurrentHashMap<Method, Boolean> svmap = new ConcurrentHashMap<Method, Boolean>();
	//private ConcurrentHashMap<Class<?>,Object> svmap2 = new ConcurrentHashMap<Class<?>, Object> ();
	
	@Override
	public boolean needService(Object driver,Method md) {
		

		try {
			Method drvmd = driver.getClass().getMethod(md.getName(), md.getParameterTypes());
			
			Selected mdan = drvmd.getAnnotation(Selected.class);
			SV an= drvmd.getDeclaringClass().getAnnotation(SV.class);
			
			if(an!=null){
				switch(an.value()){
					case ServBlack:
						if(mdan==null)
							return true;
						else
							return false;
						
					case ServWhite:
						if(mdan==null)
							return false;
						else
							return true;
					default:
						throw new RuntimeException("i can't understand this case.");
				}
			}else
				return false;
			
			
		} catch (SecurityException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
		//return false;
		
		
//		Class<?> clzl = driver.getClass();
//		ConcurrentHashMap<Method, Boolean> lsvmap=null;
//		while(true){
//			lsvmap= (ConcurrentHashMap<Method, Boolean>)this.svmap2.putIfAbsent(clzl, DefUtil.placeHolderObject);
//			if(lsvmap==null){
//				ConcurrentHashMap<Method, Boolean> llsvmp = new ConcurrentHashMap<Method, Boolean>();
//				Method[] dmd = clzl.getMethods();
//				SV an = clzl.getAnnotation(SV.class);
//				if(an!=null){
//					for(Method i : dmd){
//						Method imd = null;//AugUtil.instance.exchangeToInterfaceMethod(i);
//						if(imd!=null){
//							Selected mdan = i.getAnnotation(Selected.class);
//							boolean selected = false;
//							boolean white = false;
//							// System.out.println("an==null"+(an==null));
//
//							if (mdan != null)
//								selected = true;
//							white = an.value().equals(ListType.ServWhite);
//							if (white && selected || (!white && !selected)) {
//								llsvmp.putIfAbsent(imd, true);
//							} else {
//								llsvmp.putIfAbsent(imd, false);
//							}
//						}
//					}
//				}
//				this.svmap2.replace(clzl, llsvmp);
//			}else if(lsvmap==DefUtil.placeHolderObject){
//				Thread.yield();
//			}else{
//				break;
//			}
//		}
//		Boolean needrv = lsvmap.get(md);
//		if (needrv != null)
//			return needrv;
//		else
//			return false;

	}

	@Override
	public BuessinessLogicDriver electDriver(HashMap<Class<?>, BuessinessLogicDriver> diversmap, Method ifacemd) {		
		return diversmap.get(ifacemd.getDeclaringClass());
	}

	@Override
	public BuessinessLogicDriver electDriver(BuessinessLogicDriver[] divers, Method iface) {

		for(BuessinessLogicDriver i : divers){
			if(iface.getDeclaringClass().isAssignableFrom(i.getClass())){
				return i;
			}
		}
		
		throw new RuntimeException("no legally driver provided.");
	}

	ThreadLocal<LinkedList<AlertMsgEntry>> alist = new ThreadLocal<LinkedList<AlertMsgEntry>>();

	@Override
	public void sendRuntimeAlertMessage(AlertMessageType alertMsgType,
		Throwable e) {
		LinkedList<AlertMsgEntry> llist = this.alist.get();
		if (llist == null) {
			llist = new LinkedList<_ServUtil.AlertMsgEntry>();
			this.alist.set(llist);
		}
		AlertMsgEntry alme = new AlertMsgEntry();
		alme.alertMsgType = alertMsgType;
		alme.e = e;
		llist.add(alme);
	}

	@Override
	public void sendRuntimeAlertMessage(AlertMessageType alertMsgType,
		String msg) {
		LinkedList<AlertMsgEntry> llist = this.alist.get();
		if (llist == null) {
			llist = new LinkedList<_ServUtil.AlertMsgEntry>();
			this.alist.set(llist);
		}
		AlertMsgEntry alme = new AlertMsgEntry();
		alme.alertMsgType = alertMsgType;
		alme.msg = msg;
		llist.add(alme);

	}

	@Override
	public AlertMsgEntry[] getAlertMsgEntries() {
		LinkedList<AlertMsgEntry> llist = this.alist.get();
		if (llist != null) {
			AlertMsgEntry[] ala = llist
				.toArray(new AlertMsgEntry[llist.size()]);
			llist.clear();
			return ala;
		} else
			return null;
	}

}
