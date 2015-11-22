package j40p.infou.platform.commmon.convdef;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;

public class ConvertorGroup {
	HashMap<Integer, Convertor> cvmap = new HashMap<Integer, Convertor>();
	int[] accids;

	Convertor[] cvs;
	public ConvertorGroup add(int accessid ,Convertor conv){
		this.cvmap.put(accessid, conv);
		return this;
	}
	
	
	public Object[] conv(Object[] args ,Peeler peeler){
		Object[] retargs = null;
		if(args.getClass().getComponentType().equals(Object.class)){
			retargs=args;
		}else
			retargs=new Object[args.length];
			
		HashSet<Integer> rmset =null;
		if(peeler!=null){
			rmset =new HashSet<Integer>();
			int siz = args.length;
			for(int i =0,l=siz-1;i<=l;i++){
				rmset.add(i);
			}
			
		}
		
		if(this.cvs!=null){			
			int aci = 0;
			for(int i : this.accids){
				if(peeler!=null){
					retargs[i]=cvs[aci].conv(peeler.peel(args[i]));
					rmset.remove(i);
				}else
					retargs[i]=cvs[aci].conv(args[i]);
				aci++;
			}
		}else{
			LinkedList<Integer> accidlst = new LinkedList<Integer>();
			LinkedList<Convertor> convlst = new LinkedList<Convertor>();
			for(Entry<Integer, Convertor> i : this.cvmap.entrySet()){
				int accid = i.getKey();
				Convertor conv = i.getValue();
				if(peeler!=null){
					retargs[accid]=conv.conv(peeler.peel(args[accid]));
					rmset.remove(accid);
				}else
					retargs[accid]=conv.conv(args[accid]);
				accidlst.add(accid);
				convlst.add(conv);
			}
			this.accids=new int[accidlst.size()];
			int ai = 0;
			for(Integer i : accidlst){
				this.accids[ai]=i;
				ai++;
			}
			this.cvs=convlst.toArray(new Convertor[]{});
			this.cvmap=null;
		}
		
		if(rmset!=null){
			for(Integer i : rmset){
				retargs[i]=peeler.peel(args[i]);
			}
		}
		return retargs;
	}
	
//	private void init(Object[] args,Peeler peeler){
//
//	}
}
