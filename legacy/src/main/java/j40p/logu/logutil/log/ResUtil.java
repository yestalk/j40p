package j40p.logu.logutil.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.concurrent.ConcurrentHashMap;

import com.tibco.as.util.toolsPack.DefUtil;
import com.tibco.as.util.toolsPack.def.MID;

public interface ResUtil {
	ResUtil i = S.i.ngleton(ResUtil.class);



	<T> T getMidIns(Class<T> midz);
	void initStaticStrNames(Class<?> mid);
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD,ElementType.TYPE,ElementType.METHOD})
	@interface Managed {}
	
	
}
