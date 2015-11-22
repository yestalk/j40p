package j40p.jjson.json.parser.list;

import java.util.Iterator;


public class ArrayTokenList extends TokenList<Object> {

	@Override
	public Iterator<Object> iterator() {
		return this.getListIterator();
	}

}
