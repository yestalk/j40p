package j40p.infou.platformjmx.def.impl;

import java.util.LinkedList;

import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.TabularType;

public class Builder4TabularType {
	public String typeName;
	public String description;
	public CompositeType rowType;
	public LinkedList<String> indexNames=new LinkedList<String>();
	
	public TabularType get(){
		try {
			return new TabularType(this.typeName,
				this.description, 
				this.rowType,
				this.indexNames.toArray(new String[]{}));
		} catch (OpenDataException e) {
			throw new RuntimeException(e);
		}
	}
}
