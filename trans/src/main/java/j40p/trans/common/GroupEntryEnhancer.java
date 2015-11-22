package j40p.trans.common;

import j40p.trans.hdl.def.TypeDefEntry;
import j40p.trans.hdl.insd.DEntry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.LinkedList;

public interface GroupEntryEnhancer {
	LinkedList<DEntry> enhance(LinkedList<DEntry> data,HashMap<String, TypeDefEntry> typdef);
	
//	@Retention(RetentionPolicy.RUNTIME)
//	@Target({ElementType.TYPE})
//	public @interface MatchType {
//
//		String value();
//
//	}
}