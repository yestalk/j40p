package j40p.infou.platformjmx.builders;

import java.util.LinkedList;

import javax.management.MBeanOperationInfo;
import javax.management.openmbean.OpenMBeanOperationInfoSupport;
import javax.management.openmbean.OpenMBeanParameterInfoSupport;
import javax.management.openmbean.OpenType;

public class Builder4OpenMBeanOperationInfoSupport {
	public String name;
	public String description;
	public LinkedList<Builder4OpenMBeanParameterInfoSupport> signature=new LinkedList<Builder4OpenMBeanParameterInfoSupport>();
	public OpenType<?> returnOpenType;
	public int impact=MBeanOperationInfo.UNKNOWN;
	public Builder4DescriptorSupport descriptor=new Builder4DescriptorSupport();
	
	OpenMBeanOperationInfoSupport get(){
		return new OpenMBeanOperationInfoSupport(this.name,
			this.description,
			this.signature.toArray(new OpenMBeanParameterInfoSupport[]{}),
			this.returnOpenType,
			this.impact,
			this.descriptor.get()) ;
	}
	
}
