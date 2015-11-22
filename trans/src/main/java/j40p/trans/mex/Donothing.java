package j40p.trans.mex;

import j40p.trans.common.GroupEntryEnhancer;
import j40p.trans.hdl.def.TypeDefEntry;
import j40p.trans.hdl.insd.CompoundedEntry;
import j40p.trans.hdl.insd.DEntry;
import j40p.trans.hdl.insd.SpecialTypedEntry;
import j40p.trans.mex.out.TimeRange;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeSet;

//@GroupEntryEnhancer.MatchType("/workExperience")
public class Donothing implements GroupEntryEnhancer {
	

	@Override
	public LinkedList<DEntry> enhance(LinkedList<DEntry> data, HashMap<String, TypeDefEntry> typdefmap) {
		
		return data;

	}

}
