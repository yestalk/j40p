package j40p.jjson.json.parser.list;

import java.util.Iterator;


public class KeyValueTokenList extends TokenList<KVPair> {

	@Override
	public Iterator<KVPair> iterator() {
		
		return new DoubleIterator(super.getListIterator());
	}
	

	
	class DoubleIterator  implements Iterator<KVPair>{
		Iterator<Object> it;
		DoubleIterator(Iterator<Object> it){
			this.it=it;
		}
		@Override
		public boolean hasNext() {
			
			return this.it.hasNext();
		}

		@Override
		public KVPair next() {
			KVPair kvp = new KVPair();
			Object key = this.it.next();
			if(key instanceof LiteralString)
				kvp.k=key.toString();
			else
				kvp.k=(String)key;
			kvp.v=this.it.next();
			return kvp;
		}

		@Override
		public void remove() {
			throw new RuntimeException("not support operation");
		}
		
	}
}
