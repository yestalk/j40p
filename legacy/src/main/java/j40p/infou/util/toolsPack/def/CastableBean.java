package j40p.infou.util.toolsPack.def;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface CastableBean {
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	@interface Index {};
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	@interface Help{
		String value();
	};
	//P<Accessor> binding=T_.instance.def(CastableBean.binding);
	
	public interface Accessor{
		int bean=1;
		int param=2;
		boolean isBeanAccessor();
		Class<? extends CastableBean> targetType ();
		//int getType();
		// int getIndex();
	};
//	class Accessor{
//		public static final int bean=1;
//		public static final int param=2;
//		int type;
//		int index;
//	}
}
