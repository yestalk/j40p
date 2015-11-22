package j40p.infou.platformjmx.builders;

import javax.management.openmbean.OpenMBeanParameterInfoSupport;
import javax.management.openmbean.OpenType;

public class Builder4OpenMBeanParameterInfoSupport {
	public String name;
	public String description;
	public OpenType<?> openType;
	public Builder4DescriptorSupport descriptor=new Builder4DescriptorSupport();
	
	
	//public Object defaultValue;
	//Object[] legalValues;
	
	
	//Comparable<Object> minValue;
	//Comparable<Object> maxValue;
	
	
	
	OpenMBeanParameterInfoSupport get(){
		
		return new OpenMBeanParameterInfoSupport(
			this.name, 
			this.description,
			this.openType, 
			this.descriptor.get());
		
	}
	
}


//try {
//	if(this.legalValues!=null){
//		return new OpenMBeanParameterInfoSupport(
//			this.name, 
//			this.description,
//			this.openType, 
//			this.defaultValue, 
//			this.legalValues);
//	}else if(  this.minValue==null && this.maxValue==null){
//
//		return new OpenMBeanParameterInfoSupport(
//			this.name, 
//			this.description,
//			this.openType, 
//			this.defaultValue, 
//			this.minValue,
//			this.maxValue);
//	}else if(this.defaultValue!=null){
//		return new OpenMBeanParameterInfoSupport(
//			this.name, 
//			this.description,
//			this.openType, 
//			this.defaultValue);
//	}else{
//		return new OpenMBeanParameterInfoSupport(
//			this.name, 
//			this.description,
//			(OpenType<?>)this.openType, 
//			this.descriptor.get());
//	}
//
//} catch (OpenDataException e) {
//	
//	//e.printStackTrace();
//	throw new RuntimeException(e);
//}
