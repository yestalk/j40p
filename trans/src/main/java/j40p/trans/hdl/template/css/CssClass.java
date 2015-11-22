package j40p.trans.hdl.template.css;

import java.util.HashMap;
import java.util.HashSet;

public class CssClass {
	public String name;
	public HashSet<String> fnameset;
	public String result = "";
	public HashSet<CssClass> subcssclzs = new HashSet<>();
	public HashSet<String> cssrules = new HashSet<>();

	public void info(String data, HashMap<String, CssClass> czset) {
		String cname = data.trim();
		String[] spname = null;
		if (data.indexOf(" ()ext") >= 0) {
			String[] names = data.split("\\s\\(\\)ext");
//		for(String si: names){
//			System.out.println(si);
//		}
			spname = names[1].trim().split("\\s+");
			cname = names[0].trim();

		}
		this.name = cname;
		if (spname != null) {
			for (String i : spname) {
				//System.out.println(czset.size());
				//System.out.println(i+";");
//				System.out.println(i);
//				System.out.println(czset.get(i));
//				for(String si: czset.keySet()){
//					System.out.println(si);
//				}
				czset.get("."+i).subcssclzs.add(this);
			}
		}

	}

	public HashSet<String> mergNames() {
		HashSet<String> nameset = this.fnameset;
		if (nameset == null) {

			nameset = new HashSet<>();
			if(!this.name.startsWith(".(a)"))
				nameset.add(this.name);
			for (CssClass i : this.subcssclzs) {
				nameset.addAll(i.mergNames());
			}
		}

		if (this.result.equals("") && this.cssrules.size() > 0) {
			StringBuilder lmergedns = new StringBuilder();
			for (String i : nameset) {
				lmergedns.append(i).append(',');
			}
			lmergedns.deleteCharAt(lmergedns.length() - 1);
			lmergedns.append('{');
			for (String i : this.cssrules) {
				lmergedns.append(i).append(';');
			}
			//lmergedns.deleteCharAt(lmergedns.length() - 1);
			lmergedns.append('}');
			this.result = lmergedns.toString();
		}

		return nameset;
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return this.name.hashCode();
	}

	@Override
	public boolean equals(Object arg0) {
		if (arg0 instanceof CssClass)
			return this.name.equals(((CssClass) arg0).name);
		else
			return false;
	}
}
