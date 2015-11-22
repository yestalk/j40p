package j40p.infou.util.toolsPack.def;

import j40p.infou.util.toolsPack.InfoUtil;

import java.lang.reflect.InvocationHandler;

public interface BuessinessLogicDriver  extends InvocationHandler{
	//HoldType<Boolean> needSerivce=new HoldType<Boolean>(Boolean.class);
	void close();
	InfoUtil.EventSource[] getEventSource() ;
}
