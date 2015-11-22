package j40p.infou.platformjmx.def;

import j40p.infou.util.toolsPack.def.CastableBean;

public interface InstanceListener {
	void add(CastableBean cb);
	void remove(CastableBean cb);
}
