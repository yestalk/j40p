package j40p.infou.platform.commmon;

import j40p.infou.util.toolsPack.def.BuessinessLogicDriver;

import java.util.HashMap;

public interface OType {
	Object invoke(OInstance cx,Object[] params);
	OInstance getIns(HashMap<Class<?>,BuessinessLogicDriver> bdrivmap);
}
