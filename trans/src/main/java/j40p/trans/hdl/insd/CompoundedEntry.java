package j40p.trans.hdl.insd;

import j40p.tabform.iface.Ele;
import j40p.trans.common.GroupEntryEnhancer;
import j40p.trans.hdl.def.TypeDefEntry;

import java.awt.List;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

 

public class CompoundedEntry extends DAttrEntry {

	public CompoundedEntry(Ele info) {
		super(info);

	}

	public HashMap<String, DEntry> childrenlookup = new HashMap<>();
	public LinkedList<DEntry> children = new LinkedList<>();

	public void setChildren(LinkedList<?> children,HashMap<String, TypeDefEntry> typeDefMap,GroupEntryEnhancer lenhancers) {
		LinkedHashMap<String, Object> gp = new LinkedHashMap<>();
		for (Object i : children) {
			if (i instanceof DEntry) {
				DEntry di = (DEntry) i;
				Object ele = gp.get(di.fullPathName);
				if (ele == null) {
					gp.put(di.fullPathName, di);
				} else if (ele instanceof LinkedList) {
					LinkedList<DEntry> elelink = (LinkedList<DEntry>) ele;
					elelink.add(di);
				} else {
					LinkedList<DEntry> elelink2 = new LinkedList<>();
					elelink2.add((DEntry)ele);
					elelink2.add(di);
					gp.put(di.fullPathName, elelink2);

				}
			}
		}
		for (Entry<String, Object> ei : gp.entrySet()) {
			Object culi = ei.getValue();
			
			if (culi instanceof LinkedList) {
				LinkedList<DEntry> culilst = (LinkedList<DEntry>)culi;
				
				DEntryGroup deg = new DEntryGroup();
				deg.fullPathName = ei.getKey()+ "()list";
				deg.typegroup = lenhancers.enhance(culilst, typeDefMap);
				deg.typedef=typeDefMap.get(deg.fullPathName);
				
				
				this.children.add(deg);
				this.childrenlookup.put(deg.relativePathName, deg);

			}else if(culi instanceof DEntry){
				DEntry culidi =(DEntry)culi;
				this.children.add(culidi);
				this.childrenlookup.put(culidi.relativePathName, culidi);
			}

	
		}
	}

}
