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
public class WorkExpEnh implements GroupEntryEnhancer {
	class Gap implements Comparable<Gap> {
		int size;
		Object value;
		TimeRange tmr;

		@Override
		public int compareTo(Gap o) {
			// TODO Auto-generated method stub
			return (o.size < this.size) ? -1 : 1;
		}

	}

	class Ximgorder implements Comparable<Ximgorder> {
		int age;
		int size;
		CompoundedEntry obj;

		@Override
		public int compareTo(Ximgorder o) {
			if(o.obj==this.obj)
				return 0;
			
			if (o.age < this.age) {
				return 1;
			} else if (o.age == this.age) {
				if (o.size < this.size) {
					return -1;
				} else
					return 1;

			} else
				return -1;

		}
		


	}

	class Wkc implements Comparator<TimeRange> {

		@Override
		public int compare(TimeRange o1, TimeRange o2) {
			if (o1.startd.before(o2.startd)) {
				return -1;
			} else {
				
				return 1;
			}
		}

	}

	@Override
	public LinkedList<DEntry> enhance(LinkedList<DEntry> data, HashMap<String, TypeDefEntry> typdefmap) {
		DEntry fd = data.getFirst();
		TreeSet<TimeRange> rexp = new TreeSet<TimeRange>(new Wkc());
		TreeSet<Ximgorder> gapfillers = new TreeSet<>();
		LinkedList<Object> wkexp = new LinkedList<>();
		TreeSet<Gap> tgap = new TreeSet<WorkExpEnh.Gap>();
		TimeRange last = null;

		if (fd.fullPathName.equals("/workExperience")) {

			for (DEntry i : data) {
				if (i instanceof CompoundedEntry) {
					CompoundedEntry icom = (CompoundedEntry) i;
					DEntry tmr = icom.childrenlookup.get("/timeRange");
					if (tmr == null) {
						Ximgorder lxi = new Ximgorder();
						lxi.obj = icom;
						lxi.age = Integer.parseInt(icom.attrs.get("aging"));
						lxi.size = Integer.parseInt(icom.attrs.get("size"));
						gapfillers.add(lxi);
					} else {
						SpecialTypedEntry stmr = (SpecialTypedEntry) tmr;
						TimeRange trstmr = (TimeRange) stmr.outputer;
						trstmr.container = icom;
						rexp.add(trstmr);

					}
				}
			}
			//wkexp.addAll(rexp);
			for (TimeRange i : rexp) {

				if (last != null) {
					int lgp = last.getGap(i);
					if (lgp > 3) {
						
						TimeRange ctmrg = new TimeRange();
						ctmrg.startd = last.endd;
						ctmrg.endd = i.startd;
						
						Gap lgpo = new Gap();
						lgpo.tmr = ctmrg;
						lgpo.size = lgp;
						tgap.add(lgpo);
						wkexp.add(lgpo);
						
						
					}
					
				}
				wkexp.add(i.container);
				last = i;

			}
			//System.out.println(gapfillers.size());
			for (Gap i : tgap) {
				Ximgorder fst = gapfillers.first();
				i.value = fst.obj;
				gapfillers.remove(fst);
				//System.out.println(i.tmr.out("en"));
			}
			//System.out.println(gapfillers.size());
			//System.out.println(tgap.size());
			LinkedList<DEntry> wkexp2 = new LinkedList<DEntry>();
			for (Object i : wkexp) {
				if (i instanceof CompoundedEntry) {
					wkexp2.addFirst((CompoundedEntry) i);
				} else if (i instanceof Gap) {
					Gap fgapi = (Gap) i;
					SpecialTypedEntry tmspcal = new SpecialTypedEntry(null);
					tmspcal.fullPathName = "/workExperience/timeRange";
					tmspcal.relativePathName = "/timeRange";
					tmspcal.typedef = typdefmap.get(tmspcal.fullPathName);
					tmspcal.outputer = fgapi.tmr;

					CompoundedEntry lwexp = (CompoundedEntry) fgapi.value;
					lwexp.children.addFirst(tmspcal);
					lwexp.childrenlookup.put("/timeRange", tmspcal);

					wkexp2.addFirst(lwexp);
				}
			}
			return wkexp2;
		}
		return data;

	}

}
