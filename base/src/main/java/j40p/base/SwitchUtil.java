package j40p.base;

import java.util.HashMap;

public interface SwitchUtil {
	<T extends Enum<T>> void submit(Class<?> bindedtype , Class<T> emtype, Enum<T> eminstance);
	<T extends Enum<T>> T swt(Class<?> clz,Class<T> emtype);
	
	SwitchUtil i = new D();
	
	static class D implements SwitchUtil{ private D(){};
	

		
		HashMap<K,Enum<?>> emmap= new HashMap<K, Enum<?>>();
		@Override
		synchronized public <T extends Enum<T>> void submit(Class<?> i, Class<T> et,
				Enum<T> em) {
			if(i!=null)
				this.emmap.put(new K(i,et,false), em);
			
		}

		@Override
		public <T extends Enum<T>> T swt(Class<?> clz,Class<T> e) {
			T rt=(T)this.emmap.get(new K(clz,e,true));
			if(rt==null)
				return e.getEnumConstants()[0];
			else
				return rt;

		}
		
		
		
	}
	
	static class K {
		Class<?> clz;
		Class<?> em;
		boolean lookup;
		int hc=-1;
		
		K(Class<?> clz,Class<?> et,boolean lookup){
			this.clz=clz;
			this.em=et;
			this.lookup=lookup;
			this.hashCode();
		}
		
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof K){
				K base=(K)obj;
				return this.clz.equals(base.clz) &&  this.em.equals(base.em);
			}else
				return false;
			
		}

		@Override
		public int hashCode() {
			if (this.hc==-1)
				return this.hc=this.clz.hashCode()^this.em.hashCode()*31;	
			else
				return this.hc;
			
		}
	}
	
	
}
