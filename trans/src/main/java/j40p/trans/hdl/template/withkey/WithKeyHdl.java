package j40p.trans.hdl.template.withkey;

import java.util.HashSet;
import java.util.LinkedList;

import parser.iface.PathDocHandler;

public class WithKeyHdl implements PathDocHandler {

	@Override
	public Object createNode(LinkedList<?> pathOfdata) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object processRelation(LinkedList<?> pathOfdata, LinkedList<?> children) {
		String vl = (String) pathOfdata.getLast();
		int pzidx = vl.indexOf(" +");
		String acum = "";
		if (pzidx >= 1) {
			vl = vl.substring(0, pzidx).trim();
			if (pathOfdata.size() > 1) {
				pathOfdata.removeLast();
				for (Object i : pathOfdata) {
					if (i instanceof String) {
						acum = acum + (String) i;
					} else
						throw new RuntimeException("? with key?");
				}
				this.withkeyset.add(acum + vl);
			} else {
				this.withkeyset.add(vl);
			}

		}

		return null;
	}

	@Override
	public void overAll() {
		// TODO Auto-generated method stub

	}

	HashSet<String> withkeyset = new HashSet<>();

	public HashSet<String> getResult() {
		return this.withkeyset;
	}

}
