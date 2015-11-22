package j40p.tabform.impl;

import j40p.base.DefUtil;
import j40p.base.UTF8ByteStr;
import j40p.base.UTF8ByteStr.DT.Strimpl;
import j40p.tabform.iface.Ele;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class Eleimple implements Ele {
	int type;
	UTF8ByteStr name;
	HashMap<UTF8ByteStr, UTF8ByteStr> attr = new HashMap<UTF8ByteStr, UTF8ByteStr>();;
	LinkedHashMap<Integer, UTF8ByteStr> instant = new LinkedHashMap<Integer, UTF8ByteStr>();;

	
	
	@Override
	public int getAttrSize() {
		 
		return this.attr.size();
	}

	@Override
	public int getInstantSize() {
		 
		return this.instant.size();
	}

	@Override
	public int getType() {
		// TODO Auto-generated method stub
		return this.type;
	}

	@Override
	public UTF8ByteStr getName() {
		if(this.name==null)
			return DefUtil.emptyUtf8Str;

		return this.name;
	}

	@Override
	public UTF8ByteStr getAttr(UTF8ByteStr key) {
		return this.attr.get((Strimpl) key);
	}

	@Override
	public UTF8ByteStr getInstant(int key) {

		return this.instant.get(key);
	}

	Instit values() {
		Instit rv = new Instit();
		rv.inv = this.instant.entrySet().iterator();
		return rv;

	}
	
	
	
	@Override
	public String toString() {
		//System.out.println("attrsize:_"+this.attr.size());
		//System.out.println("instantsz:_"+this.instant.size());
		String rtname =null;
		if(this.name!=null)
			rtname=this.name.toString();
		else{
			if(this.type==Ele.JKV){
				rtname=":";
			}else if(this.type==Ele.FEle){
				rtname="/";
			}
		}
		
		StringBuilder sbb = new StringBuilder();
		Iterator<UTF8ByteStr> vs = this.values();
		//System.out.println("instanthasnext:_"+vs.hasNext());
		sbb.append("[element:]");
		sbb.append(rtname);
		sbb.append( ":__");
		while(vs.hasNext()){
			sbb.append(vs.next().toString());
			sbb.append(" ");
		}
		sbb.append( ":__");
		Iterator<Entry<UTF8ByteStr,UTF8ByteStr>> vs2= this.kvps();
		//System.out.println("attrhasnext:_"+vs2.hasNext());
		while(vs2.hasNext()){
			Entry<UTF8ByteStr,UTF8ByteStr> e1=vs2.next();
			sbb.append(e1.getKey().toString());
			sbb.append("=");
			sbb.append(e1.getValue());
			sbb.append(" ");
		}

		return  sbb.toString();
	}

	Kvp kvps() {

		Kvp rv = new Kvp();
		rv.inv = this.attr.entrySet().iterator();
		return rv;


	}

	static class Instit implements Iterator<UTF8ByteStr> {
		Iterator<Entry<Integer, UTF8ByteStr>> inv;

		@Override
		public boolean hasNext() {
			
			return this.inv.hasNext();
		}

		@Override
		public UTF8ByteStr next() {
			return this.inv.next().getValue();
		}

		@Override
		public void remove() {
			
		}
		

	}
	
	static class Kvp implements Iterator<Entry<UTF8ByteStr, UTF8ByteStr>> {
		Iterator<Entry<UTF8ByteStr, UTF8ByteStr>> inv;

		@Override
		public boolean hasNext() {
			
			return this.inv.hasNext();
		}

		@Override
		public Entry<UTF8ByteStr, UTF8ByteStr> next() {
			Entry<UTF8ByteStr, UTF8ByteStr> xx = this.inv.next();
			KpsE rtv = new KpsE();
			rtv.key=xx.getKey();
			rtv.v=xx.getValue();
			
			
			return rtv;
			
			
			
		}

		@Override
		public void remove() {
			
		}
		

	}
	static class KpsE implements  Entry<UTF8ByteStr, UTF8ByteStr>{
		
		
		UTF8ByteStr key;
		UTF8ByteStr v;
		
		
 
		
		@Override
		public UTF8ByteStr getKey() {
		 
			return key;
		}

		@Override
		public UTF8ByteStr getValue() {
			 
			return v;
		}

		@Override
		public UTF8ByteStr setValue(UTF8ByteStr value) {
			return null;
		}
		
	}

}
