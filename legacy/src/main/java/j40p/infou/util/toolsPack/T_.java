package j40p.infou.util.toolsPack;

import j40p.infou.util.toolsPack.def.P;
import j40p.infou.util.toolsPack.def.PList;
import j40p.infou.util.toolsPack.def.S;



public interface T_ {
	T_ instance = S.instance.singleton(T_.class);
	<T extends P<?>> T def(T nom);//,String ref);
	<T extends PList<?>> T defList(T nom);//,String ref);
}
