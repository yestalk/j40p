package j40p.infou.platformjmx.def.impl;

import java.util.LinkedList;

import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.OpenType;

public class Builder4CompositeType {
	public String typeName;
	public String description;
	public LinkedList<String> itemNames=new LinkedList<String>();
	public LinkedList<String> itemDescriptions=new LinkedList<String>();
	public LinkedList<OpenType<?>> itemTypes=new LinkedList<OpenType<?>>();
	
	public CompositeType get(){
		try {
			return new CompositeType(this.typeName, 
				this.description, 
				this.itemNames.toArray(new String[]{}), 
				this.itemDescriptions.toArray(new String[]{}), 
				this.itemTypes.toArray(new OpenType<?>[]{}));
		} catch (OpenDataException e) {
			throw new RuntimeException(e);
		}
	}
}
