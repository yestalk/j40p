package j40p.infou.platformjmx.builders;

import java.util.LinkedList;

import javax.management.MBeanNotificationInfo;
import javax.management.openmbean.OpenMBeanAttributeInfoSupport;
import javax.management.openmbean.OpenMBeanConstructorInfoSupport;
import javax.management.openmbean.OpenMBeanInfoSupport;
import javax.management.openmbean.OpenMBeanOperationInfoSupport;

public class Builder4OpenMBeanInfoSupport {
	public String className;
	public String description;
	public LinkedList<Builder4OpenMBeanAttributeInfoSupport> openAttributes=new LinkedList<Builder4OpenMBeanAttributeInfoSupport>();
	public LinkedList<Builder4OpenMBeanOperationInfoSupport> openOperations=new LinkedList<Builder4OpenMBeanOperationInfoSupport>();
	public Builder4DescriptorSupport descriptor=new Builder4DescriptorSupport();
	
	public LinkedList<Builder4MBeanNotificationInfo> notifications=new LinkedList<Builder4MBeanNotificationInfo>();
	public LinkedList<Builder4OpenMBeanConstructorInfoSupport> openConstructors=new LinkedList<Builder4OpenMBeanConstructorInfoSupport>();
	
	
	public OpenMBeanInfoSupport get(){
		return new OpenMBeanInfoSupport(
			this.className,
			this.description,
			this.getAttr(),
			this.getCons(),
			this.getOper(),
			this.getNoti(),
			this.descriptor.get()
			);
	}
	private OpenMBeanAttributeInfoSupport[] getAttr(){
		
		OpenMBeanAttributeInfoSupport[] rtv = new OpenMBeanAttributeInfoSupport[this.openAttributes.size()];
		if(rtv.length==0){
			System.out.println("null componet for mbean info,attr");
			return null;
		}
		int ix = 0;
		for(Builder4OpenMBeanAttributeInfoSupport i : this.openAttributes){
			rtv[ix++]=i.get();
		}
		return rtv;
	}
	private OpenMBeanOperationInfoSupport[] getOper(){
		OpenMBeanOperationInfoSupport[] rtv = new OpenMBeanOperationInfoSupport[this.openOperations.size()];
		if(rtv.length==0){
			System.out.println("null componet for mbean info,oper");
			return null;
		}
		int ix = 0;
		for(Builder4OpenMBeanOperationInfoSupport i : this.openOperations){
			rtv[ix++]=i.get();
		}
		return rtv;
	}
	private MBeanNotificationInfo[] getNoti(){
		MBeanNotificationInfo[] rtv = new MBeanNotificationInfo[this.notifications.size()];
		if(rtv.length==0){
			System.out.println("null componet for mbean info,noti");
			return null;
		}
		int ix = 0;
		for(Builder4MBeanNotificationInfo i : this.notifications){
			rtv[ix++]=i.get();
		}
		return rtv;
	}
	private OpenMBeanConstructorInfoSupport[] getCons(){
		OpenMBeanConstructorInfoSupport[] rtv = new OpenMBeanConstructorInfoSupport[this.openConstructors.size()];
		if(rtv.length==0){
			System.out.println("null componet for mbean info,cons");
			return null;
		}
		int ix = 0;
		for(Builder4OpenMBeanConstructorInfoSupport i : this.openConstructors){
			rtv[ix++]=i.get();
		}
		return rtv;
	}
}
