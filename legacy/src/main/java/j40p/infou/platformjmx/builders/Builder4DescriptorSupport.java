package j40p.infou.platformjmx.builders;

import j40p.infou.util.toolsPack.T_;
import j40p.infou.util.toolsPack.def.P;

import java.util.Set;

import javax.management.modelmbean.DescriptorSupport;
import javax.management.openmbean.OpenType;


public class Builder4DescriptorSupport {
	public static final P<Object> defaultValue = T_.instance.def(Builder4DescriptorSupport.defaultValue);
	public static final P<String> deprecated = T_.instance.def(Builder4DescriptorSupport.deprecated);
	public static final P<String> descriptionResource  = T_.instance.def(Builder4DescriptorSupport.descriptionResource);
	public static final P<String> BundleBaseName  = T_.instance.def(Builder4DescriptorSupport.BundleBaseName);
	public static final P<String> descriptionResourceKey  = T_.instance.def(Builder4DescriptorSupport.descriptionResourceKey);
	
	public static final P<String> enabled  = T_.instance.def(Builder4DescriptorSupport.enabled);
	public static final P<String> immutableInfo  = T_.instance.def(Builder4DescriptorSupport.immutableInfo);
	public static final P<Long> infoTimeout  = T_.instance.def(Builder4DescriptorSupport.infoTimeout);
	public static final P<String> interfaceClassName  = T_.instance.def(Builder4DescriptorSupport.interfaceClassName);
	public static final P<Set<?>> legalValues  = T_.instance.def(Builder4DescriptorSupport.legalValues);
	public static final P<Object> maxValue  = T_.instance.def(Builder4DescriptorSupport.maxValue);
	public static final P<Object> minValue  = T_.instance.def(Builder4DescriptorSupport.minValue);
	public static final P<String> metricType  = T_.instance.def(Builder4DescriptorSupport.metricType);
	
	public static final P<String> mxbean  = T_.instance.def(Builder4DescriptorSupport.mxbean);
	public static final P<OpenType<?>> openType  = T_.instance.def(Builder4DescriptorSupport.openType);
	public static final P<String> originalType  = T_.instance.def(Builder4DescriptorSupport.originalType);
	public static final P<Integer> severity  = T_.instance.def(Builder4DescriptorSupport.severity);
	public static final P<String> since  = T_.instance.def(Builder4DescriptorSupport.since);
	public static final P<String> units  = T_.instance.def(Builder4DescriptorSupport.units);
	DescriptorSupport sp;
	public Builder4DescriptorSupport(){
		this.sp=new DescriptorSupport();
	}
	public <T> void setField(P<T> name,T value){
		this.sp.setField(name.getSimpleName(), value);
	}
	public DescriptorSupport get (){
		return this.sp;
	}
}
