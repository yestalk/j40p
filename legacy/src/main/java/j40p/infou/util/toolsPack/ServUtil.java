package j40p.infou.util.toolsPack;

import j40p.infou.util.toolsPack.def.BuessinessLogicDriver;
import j40p.infou.util.toolsPack.def.S;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.HashMap;



public interface ServUtil {
	enum ListType{
		ServWhite,
		ServBlack;
	}
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	@interface SV {
		ListType value();
	};
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	@interface Selected {};
	
	boolean needService(Object driver,Method md);
	
	BuessinessLogicDriver electDriver(BuessinessLogicDriver[] divers,Method iface);
	
	BuessinessLogicDriver electDriver(HashMap<Class<?>,BuessinessLogicDriver> diversmap,Method ifacemd);
	
	ThreadLocal<Object[]> Msgs=new ThreadLocal<Object[]>(); 
	public enum AlertMessageType{
		INFO ,
		WARNING ,
		ERROR 
	}
	public class AlertMsgEntry{
		public AlertMessageType alertMsgType;
		public Throwable e;
		public String msg;
	}
	void sendRuntimeAlertMessage(AlertMessageType alertMsgType ,Throwable e);
	void sendRuntimeAlertMessage(AlertMessageType alertMsgType ,String msg);
	AlertMsgEntry[] getAlertMsgEntries();
	ServUtil instance = S.instance.singleton(ServUtil.class);
	
}
