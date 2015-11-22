package j40p.base;

import j40p.base.l.DRes;

public interface ResAssigner {
	<T> T allocate  (DRes<T> context,Object ref);

}
