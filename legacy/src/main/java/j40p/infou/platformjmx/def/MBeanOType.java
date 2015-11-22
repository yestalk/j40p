package j40p.infou.platformjmx.def;

import j40p.infou.util.toolsPack.def.BuessinessLogicDriver;
import j40p.infou.util.toolsPack.def.CastableBean;

import java.util.HashMap;

public interface MBeanOType {
	Class<? extends CastableBean>  getBindingType();
	MBeanInstance getIns(HashMap<Class<?>,BuessinessLogicDriver> bdrivmap,CastableBean cb);
}
