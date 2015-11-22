package j40p.infou.platformjmx.builders;

import javax.management.openmbean.OpenMBeanAttributeInfoSupport;
import javax.management.openmbean.OpenType;

public class Builder4OpenMBeanAttributeInfoSupport {
	public String name;
	public String description;
	public OpenType<?> openType;
	public boolean isReadable;
	public boolean isWritable;
	public boolean isIs;
	
	
	//public Object defaultValue;
	//public Comparable<Object> minValue;
	//public Comparable<Object> maxValue;
	
	//public Object[] legalValues;
	
	public Builder4DescriptorSupport descriptor=new Builder4DescriptorSupport();
	
	
	public OpenMBeanAttributeInfoSupport get(){
		
		return new OpenMBeanAttributeInfoSupport(
			this.name, 
			this.description,
			this.openType, 
			this.isReadable, 
			this.isWritable, 
			this.isIs, 
			this.descriptor.get());
		
	}
}


//try {
//	if(this.legalValues!=null){
//		return new OpenMBeanAttributeInfoSupport(
//			this.name, 
//			this.description,
//			this.openType, 
//			this.isReadable, 
//			this.isWritable, 
//			this.isIs, 
//			this.defaultValue, 
//			this.legalValues);
//	}else if(  this.minValue==null && this.maxValue==null){
//
//		return new OpenMBeanAttributeInfoSupport(
//			this.name, 
//			this.description,
//			this.openType, 
//			this.isReadable, 
//			this.isWritable, 
//			this.isIs, 
//			this.defaultValue, 
//			this.minValue,
//			this.maxValue);
//	}else if(this.defaultValue!=null){
//		return new OpenMBeanAttributeInfoSupport(
//			this.name, 
//			this.description,
//			this.openType, 
//			this.isReadable, 
//			this.isWritable, 
//			this.isIs, 
//			this.defaultValue);
//	}else{
//		return new OpenMBeanAttributeInfoSupport(
//			this.name, 
//			this.description,
//			(OpenType<?>)this.openType, 
//			this.isReadable, 
//			this.isWritable, 
//			this.isIs, 
//			this.descriptor.get());
//	}
//
//} catch (OpenDataException e) {
//	
//	//e.printStackTrace();
//	throw new RuntimeException(e);
//}
