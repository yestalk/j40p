package j40p.jjson.json.parser.list;


import java.util.Iterator;
import java.util.LinkedList;



public abstract class TokenList<T> implements Iterable<T> {

	private LinkedList<Object> list = new LinkedList<Object>();
	
	public void add(Object e){
		this.list.add(e);
	}
	
	public int size(){
		return this.list.size();
	}
	
	protected Iterator<Object> getListIterator(){
		return this.list.iterator();
	}
	protected String getLastToken(){
		Object last = this.list.getLast();
		if(last instanceof LiteralString)
			return last.toString();
		else if(last instanceof String)
			return (String)last;
		else
			throw new RuntimeException("key must be string.");
	}
}
