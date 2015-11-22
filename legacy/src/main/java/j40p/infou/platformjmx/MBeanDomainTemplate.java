package j40p.infou.platformjmx;


import j40p.infou.platformjmx.base.MBeanTypeManager;
import j40p.infou.platformjmx.def.CBeanInstanceManager;
import j40p.infou.platformjmx.def.InstanceListener;
import j40p.infou.platformjmx.def.MBeanInstance;
import j40p.infou.platformjmx.def.MBeanUtil;
import j40p.infou.util.toolsPack.InfoUtil;
import j40p.infou.util.toolsPack.InfoUtil.DataNoticeListener;
import j40p.infou.util.toolsPack.def.BuessinessLogicDriver;
import j40p.infou.util.toolsPack.def.CastableBean;

import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;

import javax.management.InstanceAlreadyExistsException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

public class MBeanDomainTemplate  implements InfoUtil.Portal<MBeanDomainTemplate>{
	
	
	//private HashMap<CastableBean, JMBean> jmmap = new HashMap<CastableBean, JMBean>();
	private MBeanTypeManager mtm;
	
	
	
	private DomainInstance dmins;
	
	public MBeanDomainTemplate(MBeanTypeManager mtm){
		this.mtm=mtm;
//		if(mdomain!=null)
//			this.mdomain=mdomain;
//		this.mbs=ManagementFactory.getPlatformMBeanServer();
		
	}

	@Override
	public void linkingWithDrivers(HashMap<Class<?>,BuessinessLogicDriver> drivermap) {
		this.dmins = new DomainInstance();
		dmins.tmplate=this;
		dmins.drivermap=drivermap;
	}
	
	@Override
	public HashMap<Method, LinkedList<DataNoticeListener>> getEventCaseMap() {
		return new HashMap<Method, LinkedList<DataNoticeListener>>();
	}
	
	
	public void submitInstance(String domain,CBeanInstanceManager cbin){
		cbin.add(this.dmins);
		if(domain!=null)
			this.dmins.mdomain=domain;
		this.dmins=null;
	}
	
	

	
	public static class DomainInstance implements InstanceListener{
		private MBeanDomainTemplate tmplate;
		
		private MBeanServer mbs;
		private String mdomain="jmxd";
		private HashMap<Class<?>,BuessinessLogicDriver> drivermap;
		
		private DomainInstance(){
			this.mbs=ManagementFactory.getPlatformMBeanServer();
		}
		
		public void add(CastableBean cb){
			
			//JMBean jmb = new JMBean(cb,this.mtm);
			MBeanInstance  jmb = this.tmplate.mtm.getType(cb).getIns(this.drivermap, cb);
			try {
				ObjectName oname =   MBeanUtil.instance.getObjectName(this.mdomain, cb);
				
				if(!this.mbs.isRegistered(oname))
					this.mbs.registerMBean(jmb,oname );
			} catch (InstanceAlreadyExistsException e) {
				throw new RuntimeException(e);
			} catch (MBeanRegistrationException e) {
				throw new RuntimeException(e);
			} catch (NotCompliantMBeanException e) {
				throw new RuntimeException(e);
			}
		}

		public void remove(CastableBean cb){
			try {
				
				this.mbs.unregisterMBean(MBeanUtil.instance.getObjectName(this.mdomain, cb));
			} catch (MBeanRegistrationException e) {
				throw new RuntimeException(e);
			} catch (InstanceNotFoundException e) {
				throw new RuntimeException(e);
			}
			//this.jmmap.remove(cb);
		}
	}

}
