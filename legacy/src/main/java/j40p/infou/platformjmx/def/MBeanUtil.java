package j40p.infou.platformjmx.def;

import j40p.infou.util.toolsPack.def.CastableBean;
import j40p.infou.util.toolsPack.def.S;

import java.lang.reflect.Type;

import javax.management.ObjectName;
import javax.management.openmbean.OpenType;

public interface MBeanUtil {
	MBeanUtil instance= S.instance.singleton(MBeanUtil.class);
	
	OpenType<?> getOpenType(Type clz);
	ObjectName getObjectName(String domain,CastableBean bean);
}
