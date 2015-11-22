package j40p.infou.platformjmx.builders;

import java.util.LinkedList;

import javax.management.MBeanNotificationInfo;

public class Builder4MBeanNotificationInfo {
	public LinkedList<String> notifTypes=new LinkedList<String>();
	public String name;
	public String description;
	public Builder4DescriptorSupport descriptor=new Builder4DescriptorSupport();
	
	MBeanNotificationInfo get(){
		return new MBeanNotificationInfo(this.notifTypes.toArray(new String[]{}),
			this.name, 
			this.description,
			this.descriptor.get()) ;
	}
	
	
}
