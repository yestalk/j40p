package j40p.infou.platformjmx.builders;

import java.util.LinkedList;

import javax.management.openmbean.OpenMBeanConstructorInfoSupport;
import javax.management.openmbean.OpenMBeanParameterInfoSupport;

public class Builder4OpenMBeanConstructorInfoSupport {
	public String name;
	public String description;
	public LinkedList<Builder4OpenMBeanParameterInfoSupport> signature=new LinkedList<Builder4OpenMBeanParameterInfoSupport>();;
	public Builder4DescriptorSupport descriptor=new Builder4DescriptorSupport();
	
	OpenMBeanConstructorInfoSupport  get(){
		return new OpenMBeanConstructorInfoSupport(this.name,
			this.description,
			this.signature.toArray(new OpenMBeanParameterInfoSupport[]{}),
			this.descriptor.get());
	}
	
}
