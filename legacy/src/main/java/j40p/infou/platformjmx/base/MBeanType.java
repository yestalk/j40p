package j40p.infou.platformjmx.base;

import j40p.infou.platform.commmon.OInstance;
import j40p.infou.platform.commmon.OType;
import j40p.infou.platform.commmon.convdef.Convertor;
import j40p.infou.platform.commmon.convdef.ConvertorGroup;
import j40p.infou.platformjmx.def.MBeanInstance;
import j40p.infou.platformjmx.def.MBeanItemType;
import j40p.infou.platformjmx.def.MBeanOType;
import j40p.infou.util.toolsPack.BeanUtil;
import j40p.infou.util.toolsPack.ServUtil;
import j40p.infou.util.toolsPack.def.BuessinessLogicDriver;
import j40p.infou.util.toolsPack.def.CastableBean;
import j40p.infou.util.toolsPack.def.CastableBean.Accessor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.ReflectionException;
import javax.management.openmbean.OpenMBeanInfoSupport;

public class MBeanType implements MBeanOType {

	private Class<? extends CastableBean> bindingtype;
	private OpenMBeanInfoSupport mbinfo;
	private LinkedList<RequestHandler> mwlist = new LinkedList<MBeanType.RequestHandler>();

	public MBeanType(OpenMBeanInfoSupport mbinfo, LinkedList<RequestHandler> mwlist, Class<? extends CastableBean> bindingtype) {
		this.mbinfo = mbinfo;
		this.mwlist = mwlist;
		this.bindingtype = bindingtype;
	}

	@Override
	public MBeanInstance getIns(HashMap<Class<?>, BuessinessLogicDriver> bdrivmap, CastableBean cb) {
		JMBean jmb = new JMBean();
		jmb.mbeantype=this;

		for(RequestHandler i :this.mwlist){
			MDins oinsi = (MDins)i.getIns(bdrivmap);
			oinsi.cb=cb;
			jmb.mbitems.put(oinsi.getKey(), oinsi);
		}
		
		
		return jmb;
	}

	@Override
	public Class<? extends CastableBean> getBindingType() {
		return this.bindingtype;
	}

	public static class MDins implements OInstance {
		private RequestHandler theotype;

		private Object driver;
		private boolean needservice;

		private Object key;

		private CastableBean cb;

		@Override
		public OType getOType() {
			return this.theotype;
		}

		@Override
		public Object getKey() {
			return this.key;
		}

	}

	public static class RequestHandler implements OType {
		private MBeanItemType mit;
		
		private String alias;
		private Method targetMethod;
		private Accessor[] accessList; // the full access list for original
										// method prama list;

		private String[] maskedSingnature;
		private ConvertorGroup maskedConvList;
		private Convertor outValueConv;

		
		public RequestHandler(MBeanItemType mit,
			String alias,
			Method targetMethod,
			Accessor[] accessList,
			String[] maskedSingnature,
			ConvertorGroup maskedConvList,
			Convertor outValueConv){
			
		}


		@Override
		public Object invoke(OInstance cx, Object[] params) {
			Object rtv = null;
			MDins mdins = (MDins) cx;

			params = this.maskedConvList.conv(params,null);
			Object[] tparml = BeanUtil.instance.$access(mdins.cb, this.accessList, params);

			if (mdins.needservice) {
				try {
					rtv = ((InvocationHandler) mdins.driver).invoke(mdins.driver, this.targetMethod, tparml);
				} catch (Throwable e) {
					throw new RuntimeException(e);
				}
			} else {
				try {
					rtv = this.targetMethod.invoke(mdins.driver, tparml);
				} catch (IllegalArgumentException e) {
					throw new RuntimeException(e);
				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				} catch (InvocationTargetException e) {
					throw new RuntimeException(e.getCause());
				}
			}

			return this.outValueConv.conv(rtv);
		}

		@Override
		public OInstance getIns(HashMap<Class<?>, BuessinessLogicDriver> bdrivmap) {
			MDins mdins = new MDins();
			mdins.theotype = this;

			mdins.driver = ServUtil.instance.electDriver(bdrivmap, this.targetMethod);
			mdins.needservice = ServUtil.instance.needService(mdins.driver, this.targetMethod);

			switch (this.mit) {
				case AttributeGetter:
					mdins.key = new CombainAttrKey(this.alias, MBeanItemType.AttributeGetter);
					break;
				case AttributeSetter:
					mdins.key = new CombainAttrKey(this.alias, MBeanItemType.AttributeSetter);
					break;
				case Operation:
					mdins.key = new CombainKey(this.alias, this.maskedSingnature);
					break;
				default:
					break;
			}
			return mdins;
		}

		public RequestHandler(Method targetMethod, Accessor[] accessList, ConvertorGroup maskedConvList, Convertor outValueConv) {
			this.targetMethod = targetMethod;
			this.accessList = accessList;
			this.maskedConvList = maskedConvList;
			this.outValueConv = outValueConv;
		}

	}

	public class JMBean implements MBeanInstance {

		// MBeanTypeManager mtm;

		
		//private CastableBean cb;

		private MBeanType mbeantype;
		private HashMap<Object, OInstance> mbitems=new HashMap<Object, OInstance>();

		@Override
		public MBeanInfo getMBeanInfo() {
			// System.out.println("?????"+this.mbtp);
			// System.out.println( "attrs:_"+this.mbtp.mbinfo.getAttributes());
			// System.out.println(
			// "classname:_"+this.mbtp.mbinfo.getClassName());
			// System.out.println(
			// "Description:_"+this.mbtp.mbinfo.getDescription());
			// System.out.println(
			// "Constructors:_"+this.mbtp.mbinfo.getConstructors());
			// System.out.println(
			// "Operations:_"+this.mbtp.mbinfo.getOperations());
			// System.out.println(
			// "Notifications:_"+this.mbtp.mbinfo.getNotifications());
			// System.out.println(
			// "Descriptor:_"+this.mbtp.mbinfo.getDescriptor());

			return this.mbeantype.mbinfo;
		}

		@Override
		public Object getAttribute(String attribute) throws AttributeNotFoundException, MBeanException, ReflectionException {

			System.out.println("getAttribute:" + attribute);
			// this.mbtp.showinfo();
			OInstance tg = this.mbitems.get(new CombainAttrKey(attribute, MBeanItemType.AttributeGetter));
			//((MDins) tg).cb = this.cb;
			Object rtv = tg.getOType().invoke(tg, new Object[] {});
			// System.out.println(rtv==null);
			return rtv;
		}

		@Override
		public AttributeList getAttributes(String[] attributes) {
			AttributeList alist = new AttributeList();
			for (String i : attributes) {
				try {
					alist.add(this.getAttribute(i));
				} catch (AttributeNotFoundException e) {
					throw new RuntimeException(e);
				} catch (MBeanException e) {
					throw new RuntimeException(e);
				} catch (ReflectionException e) {
					throw new RuntimeException(e);
				}
			}
			return alist;
		}

		@Override
		public void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {

			OInstance tg = this.mbitems.get(new CombainAttrKey(attribute.getName(), MBeanItemType.AttributeSetter));
			//((MDins) tg).cb = this.cb;
			Object rtv = tg.getOType().invoke(tg, new Object[] { attribute.getValue() });

		}

		@Override
		public AttributeList setAttributes(AttributeList attributes) {

			for (Object i : attributes) {
				Attribute ai = (Attribute) i;
				try {
					this.setAttribute(ai);
				} catch (AttributeNotFoundException e) {
					throw new RuntimeException(e);
				} catch (InvalidAttributeValueException e) {
					throw new RuntimeException(e);
				} catch (MBeanException e) {
					throw new RuntimeException(e);
				} catch (ReflectionException e) {
					throw new RuntimeException(e);
				}
			}
			return attributes;
		}

		@Override
		public Object invoke(String actionName, Object[] params, String[] signature) throws MBeanException, ReflectionException {
			System.out.println("invoke");
			// this.mbeantype.showinfo();
			OInstance tg = this.mbitems.get(new CombainKey(actionName, signature));
			//((MDins) tg).cb = this.cb;
			Object rtv = tg.getOType().invoke(tg, params);
			return rtv;
		}

	}

}

// private HashMap<String ,MethodWrapper> attrs;// let assuming every writable
// Attribute has its getter(in other words , there is no write only attribute)
// private HashMap<String ,MethodWrapper> wattrs;

// Object invoke(CastableBean cb,WrapLinkingInfo driverinfo,Object[] args){
// try {
// for(Convertor i: this.maskedConvList){
// int corri =i.getCorrespondingIndex();
// args[corri]=i.conv(args[corri]);
// }
//
// Object[] tparml = BeanUtil.instance.$access(cb,this.accessList, args);
// //System.out.println( "after methodwraper param access");
// //System.out.println(driverinfo.driver.getClass());
// // for(Object i: tparml){
// // System.out.println(i.getClass());
// // }
// System.out.println( "before methodwraper invoke");
// Object rtobj =null;
// if(driverinfo.needservice){
// System.out.println("method need service.");
// rtobj = ((InvocationHandler)driverinfo.driver).invoke(driverinfo.driver,
// this.targetMethod, tparml);
// }else{
// rtobj = this.targetMethod.invoke(driverinfo.driver,tparml);
// }
// System.out.println( "after methodwraper invoke");
// System.out.println("mwarper conver name :_"+this.outValueConv.getClass().getName());
//
// return this.outValueConv.conv(rtobj);
// } catch (IllegalArgumentException e) {
// throw new RuntimeException(e);
// } catch (IllegalAccessException e) {
// throw new RuntimeException(e);
// } catch (InvocationTargetException e) {
// throw new RuntimeException(e);
// } catch (Throwable e) {
// throw new RuntimeException(e);
// }
// }

// public WrapLinkingInfo selectDriver(Object... driver){
// WrapLinkingInfo myinfo = new WrapLinkingInfo();
// for(Object i: driver){
// if(this.targetMethod.getDeclaringClass().isAssignableFrom(i.getClass())){
// myinfo.driver=i;
// myinfo.needservice=ServUtil.instance.needService(i, this.targetMethod);
// return myinfo;
// }
// }
// throw new RuntimeException("no driver have implmented this method.");
// }

// public void showinfo(){
//
// if(this.attrs!=null){
// System.out.println("attrs:");
// for(String i : this.attrs.keySet()){
// System.out.println(i);
// }
// System.out.println("-------");
// }
// if(this.opers!=null){
// System.out.println("oper:");
// for(CombainKey i : this.opers.keySet()){
// System.out.println(i);
// }
// System.out.println("-------");
// }
//
//
// }
// public HashMap<MethodWrapper,WrapLinkingInfo>
// linkingWithDrivers(BuessinessLogicDriver... drivers){
//
// HashMap<MethodWrapper,WrapLinkingInfo> rvmap = new HashMap<MethodWrapper,
// WrapLinkingInfo>();
// for(MethodWrapper i : this.attrs.values()){
// WrapLinkingInfo linkingdriver= i.selectDriver((Object[])drivers);
//
// rvmap.put(i, linkingdriver);
//
//
// }
// for(MethodWrapper i : this.opers.values()){
// WrapLinkingInfo linkingdriver= i.selectDriver((Object[])drivers);
// rvmap.put(i, linkingdriver);
// }
// return rvmap;
// }

// Object[] drivers;
// HashMap<MethodWrapper,Object> drivermap;
// public JMBean(CastableBean mbinstance,MBeanTypeManager mtm){
// this.mbinstance=mbinstance;
// this.mbtp=mtm.getType(mbinstance);
// //this.mtm=mtm;
// }
// for(Entry<Object ,OInstance> i : this.mbitems.entrySet()){
// Object ekey = i.getKey();
// OInstance tg = this.mbitems.get(new CombainAttrKey(attribute,
// MBeanItemType.AttributeGetter));
// ((MDins)tg).cb=this.cb;
// Object rtv = tg.getOType().invoke(tg,new Object[]{});
// }
//
// for(Entry<String,MethodWrapper> i: this.mbeantype.attrs.entrySet()){
// MethodWrapper tg = i.getValue();
// alist.add( new
// Attribute(i.getKey(),tg.invoke(this.cb,this.mtm.getDriverInfo(tg),new
// Object[]{})));
// }
// //System.out.println(alist.size());