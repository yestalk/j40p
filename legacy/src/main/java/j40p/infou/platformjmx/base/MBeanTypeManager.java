package j40p.infou.platformjmx.base;

import j40p.infou.util.toolsPack.BeanUtil;
import j40p.infou.util.toolsPack.def.CastableBean;

import java.util.HashMap;

public class MBeanTypeManager {
	private HashMap<Class<? extends CastableBean>, MBeanType> typemap=new HashMap<Class<? extends CastableBean>, MBeanType>();
	
	public MBeanTypeManager(MBeanType[] mbts){
		//System.out.println("MBMB-----------------");
		for(MBeanType i : mbts){
			//System.out.println(i);
			this.typemap.put(i.getBindingType(), i);
			
		}
	}
	public MBeanType getType(CastableBean cb){
		//System.out.println("----------------------");
//		for(Entry<Class<? extends CastableBean>, MBeanType>i:this.typemap.entrySet()){
//			System.out.println(i.getKey());
//			System.out.println(i.getValue());
//		}
		//System.out.println(BeanUtil.instance.$beantype(cb));
		//System.out.println("----------------------");
		return this.typemap.get(BeanUtil.instance.$beantype(cb));
	}


}

//WrapLinkingInfo getDriverInfo(MethodWrapper mw){
//	return this.drivermap.get(mw);
//}
//

//public void linkingWithDrivers(BuessinessLogicDriver... drivers) {
//HashMap<MethodWrapper,WrapLinkingInfo> dmap=null;
//
//for( MBeanType i: this.typemap.values()){
//	if(dmap!=null){
//		for(Entry<MethodWrapper,WrapLinkingInfo> ei: i.linkingWithDrivers(drivers).entrySet()){
//			dmap.put(ei.getKey(), ei.getValue());
//		}
//	}else
//		dmap = i.linkingWithDrivers(drivers);
//}
//this.drivermap=dmap;
//}
