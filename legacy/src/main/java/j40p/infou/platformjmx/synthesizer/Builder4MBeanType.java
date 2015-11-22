package j40p.infou.platformjmx.synthesizer;

import j40p.infou.platformjmx.base.CombainKey;
import j40p.infou.platformjmx.base.MBeanType;
import j40p.infou.platformjmx.base.MBeanType.JMBean;
import j40p.infou.platformjmx.base.MBeanType.RequestHandler;
import j40p.infou.platformjmx.builders.Builder4DescriptorSupport;
import j40p.infou.platformjmx.builders.Builder4OpenMBeanAttributeInfoSupport;
import j40p.infou.platformjmx.builders.Builder4OpenMBeanInfoSupport;
import j40p.infou.util.toolsPack.def.CastableBean;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import javax.management.openmbean.OpenMBeanInfoSupport;

public class Builder4MBeanType {
	//public OpenMBeanInfoSupport mbinfo;
	public HashMap<String ,Builder4MethodWrapper> attrs;
	public HashMap<String ,Builder4MethodWrapper> wattrs;
	public HashMap<CombainKey ,Builder4MethodWrapper> opers;
	public Class<? extends CastableBean> bindingtype;
	
	public LinkedList<Builder4MethodWrapper> mwraperList = new LinkedList<Builder4MethodWrapper>();
	
	//public LinkedList<Builder4MethodWrapper> oplist = new LinkedList<Builder4MethodWrapper>();
	//public LinkedList<Builder4MethodWrapper> attrlist = new LinkedList<Builder4MethodWrapper>();
	//public LinkedList<Builder4MethodWrapper> wattrlist = new LinkedList<Builder4MethodWrapper>();
	
	public MBeanType get(){
		this.attrs=new HashMap<String, Builder4MethodWrapper>();
		this.wattrs=new HashMap<String, Builder4MethodWrapper>();
		this.opers=new HashMap<CombainKey, Builder4MethodWrapper>();
		
		for(Builder4MethodWrapper i : this.mwraperList){
			System.out.println(" all sb item name :_"+i.MbeanItemName);
			switch (i.MbeanItemType) {
				case AttributeGetter:
					System.out.println("attr item name :_"+i.MbeanItemName);
					this.attrs.put(i.MbeanItemName, i);
					break;
				case AttributeSetter:
					this.wattrs.put(i.MbeanItemName, i);
					break;
				case Operation:
					this.opers.put(i.getCombkey4Operation(), i);
					break;
				default:
					break;
			}
		}
		
		Set<String> wakey = wattrs.keySet();
		for(String i : wakey){
			Builder4MethodWrapper aa = attrs.get(i);
			if(aa==null){
				throw new RuntimeException("no read only attr please.");
			}else{
				if(aa.targetMethod.getReturnType().equals(wattrs.get(i).AttrParamType))
					;
				else
					throw new RuntimeException("can't go this far, writable attr must have the same type of param as the return type of its corresponding readable attr..");
			}
		}
		//OpenMBeanInfoSupport mbinfo = this.generate();
		HashMap<String ,RequestHandler> lattrs=new HashMap<String, RequestHandler>();
		for(Map.Entry<String, Builder4MethodWrapper>  i :this.attrs.entrySet()){
			lattrs.put(i.getKey(), i.getValue().get());
		}
		
		HashMap<String ,RequestHandler> lwattrs=new HashMap<String, RequestHandler>();
		for(Map.Entry<String, Builder4MethodWrapper>  i :this.wattrs.entrySet()){
			lwattrs.put(i.getKey(), i.getValue().get());
		}
		
		HashMap<CombainKey ,RequestHandler> lopers=new HashMap<CombainKey, RequestHandler>();
		for(Map.Entry<CombainKey, Builder4MethodWrapper>  i :this.opers.entrySet()){
			lopers.put(i.getKey(), i.getValue().get());
		}
		
		return new MBeanType(this.generate(),this.getMWrapperList(), this.bindingtype);
	}
	private LinkedList<RequestHandler> getMWrapperList(){
		LinkedList<RequestHandler> rvlist = new LinkedList<MBeanType.RequestHandler>();
		for(Builder4MethodWrapper bmwi : this.attrs.values()){
			rvlist.add(bmwi.get());
		}
		for(Builder4MethodWrapper bmwi : this.wattrs.values()){
			rvlist.add(bmwi.get());
		}
		for(Builder4MethodWrapper bmwi : this.opers.values()){
			rvlist.add(bmwi.get());
		}
		return rvlist;
	}
	private OpenMBeanInfoSupport generate(){
		Builder4OpenMBeanInfoSupport sp = new Builder4OpenMBeanInfoSupport();
		sp.className=JMBean.class.getName();
		sp.description="jmbean";
		
		//sp.descriptor = new Builder4DescriptorSupport();
		sp.descriptor.setField(Builder4DescriptorSupport.immutableInfo, "true");
		sp.descriptor.setField(Builder4DescriptorSupport.interfaceClassName, sp.className);
		sp.descriptor.setField(Builder4DescriptorSupport.mxbean,"false");
		//sp.descriptor.setField(Builder4DescriptorSupport.legalValues, null);
		
		// build attr
		//sp.openAttributes=new LinkedList<Builder4OpenMBeanAttributeInfoSupport>();
		for(Map.Entry<String, Builder4MethodWrapper> i : this.attrs.entrySet()){
			Builder4MethodWrapper attrwp =  i.getValue();
			String attrname = i.getKey();
			Builder4OpenMBeanAttributeInfoSupport attrinfosp = attrwp.getAttrInfo();
			
			if(this.wattrs.get(attrname)!=null){
				attrinfosp.isWritable=true;
			}
			sp.openAttributes.add(attrinfosp);
		}
		
		//build oper
		//sp.openOperations=new LinkedList<Builder4OpenMBeanOperationInfoSupport>();
		
		for(Map.Entry<CombainKey, Builder4MethodWrapper> i : this.opers.entrySet()){
			Builder4MethodWrapper opmwrapper = i.getValue();
			sp.openOperations.add(opmwrapper.getOperationInfo());
		}
		return sp.get();
	}
}
