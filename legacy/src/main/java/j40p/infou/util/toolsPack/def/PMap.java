package j40p.infou.util.toolsPack.def;

import java.util.HashMap;
import java.util.LinkedList;

public class PMap {
	HashMap<P<?>,Object> hmp = new HashMap<P<?>,Object>();
	HashMap<P<?>,Object> dfmp;
	
	public static Object makeDefault(NVP... values){
		HashMap<P<?>, Object> ldfmap =new HashMap<P<?>, Object>();
		for(NVP i : values){
			if(i.type instanceof PList){
				Object lo = ldfmap.get(i.type);
				LinkedList<Object> x;
				if(lo==null){
					x = new LinkedList<Object>();
					ldfmap.put(i.type, x);
				}else{
					x = (LinkedList<Object>)lo;
				}
				x.add(i.value);
			}else
				ldfmap.put(i.type, i.value);
		}
		return ldfmap;
	}
	public void loadDefault(Object dfv){
		this.dfmp=(HashMap<P<?>,Object>)dfv;
	}
	
	
	
	public <T> T get(P<T> lable){
		T  rvt =  (T)this.hmp.get(lable);
		
		if(rvt==null){
			rvt= (T)this.dfmp.get(lable);
			//System.out.println("property key:___"+lable.getSimpleName());
			//System.out.println("	defaultvalue:___"+rvt);
		}
		return rvt;
	}
	
	
	public void remove(Object lable){
		this.hmp.remove(lable);
	}
	public <T> LinkedList<T> getList(PList<T> lable){
		return (LinkedList<T>)this.hmp.get(lable);
	}
	
	public void add(NVP nvp){
		
		if(nvp.type instanceof PList){
			Object lo = this.hmp.get(nvp.type);
			LinkedList<Object> x;
			if(lo==null){
				x = new LinkedList<Object>();
				this.hmp.put(nvp.type, x);
			}else{
				x = (LinkedList<Object>)lo;
			}
			x.add(nvp.value);
		}else{
			//System.out.println("					pmap:__"+nvp.value);
			this.hmp.put(nvp.type, nvp.value);
		}
	}
	
	public void add(NVP[] nvps){
		for(NVP i : nvps){
			this.add(i);
		}
	}
	
	public void clear(){
		this.hmp.clear();
	}
	
	public int size(){
		return this.hmp.size();
	}
	
	public void merge(PMap attrmap){
//		System.out.println("		attrmap.size()"+attrmap.size());

		this.hmp.putAll(attrmap.hmp);
//		for(Entry<P<?>, Object> i : this.hmp.entrySet()){
//			System.out.println("		"+i.getKey().getSimpleName());
//			System.out.println("			"+i.getValue());
//		}
	}
}
