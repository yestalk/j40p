package j40p.infou.util.toolsPack;

import j40p.infou.util.toolsPack.def.BuessinessLogicDriver;
import j40p.infou.util.toolsPack.def.NVP;
import j40p.infou.util.toolsPack.def.P;
import j40p.infou.util.toolsPack.def.PMap;
import j40p.infou.util.toolsPack.def.S;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public interface InfoUtil {
	InfoUtil instance = S.instance.singleton(InfoUtil.class);

	P<String> helpsInfo = T_.instance.def(InfoUtil.helpsInfo);//,"helpinfo");
	P<String> paramName = T_.instance.def(InfoUtil.paramName);//,"paramName");
	P<String> methodAlias_gen = T_.instance.def(InfoUtil.methodAlias_gen);//,"methodAlias_gen");
	P<Object> paramdemoValue = T_.instance.def(InfoUtil.paramdemoValue);//,"paramdemoValue");
	
	
	
	InfoSession getInfoSession(Object caller);
	
	<T> NVP nvp(P<T> type, T value);
	
	<T> T makeEventSource(Class<T> iface, T hook);

	<T> T bind(T... values);
	
	
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.METHOD,ElementType.PARAMETER})
	@interface Description{
		String value();
	};
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.PARAMETER)
	@interface ParamName{
		String value();
	};
	
	
	
	
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.TYPE})
	@interface UesedBy{
		Class<? extends Generator<?>>[] value();
	};
	
//	@Retention(RetentionPolicy.RUNTIME)
//	@Target({ElementType.METHOD,ElementType.PARAMETER})
//	@interface AugInfo{};
	
	abstract class AugInfo{
		protected AugSession augs;
		public void setAugSession(AugSession augs){this.augs=augs;}
	}
	
	interface Composer {
		void info(AugSession aughdl);
	}
	
	
	
	
	interface Avoid{
		Void done(AugSession stub);
	}
	interface AugSession {
		void augCurrentMethod(NVP... pz);
		<TP> TP augMethod(Class<TP> iface,NVP... pz);
		
		<TP> NVP vpair(P<TP> lable, TP value);
		
		<TP> TP make(P<Method> label,Class<TP> clz);
		NVP mpair(Object... obj);
		
		<TP> TP augParam(TP value,NVP... pz);
	}
	
	interface InfoSession{
		
		//<IFACE , DRIVER extends IFACE>InfoSession federate(Class<IFACE> iface,DRIVER driver);	
		
		<DRIVER extends BuessinessLogicDriver> InfoSession federate(Class<? super DRIVER> iface,DRIVER driver);
		<PORTAL extends Portal<PORTAL>> PORTAL exchange4Portal(Generator<PORTAL> pt);
	}
	
	interface Portal<E> {
		
		void linkingWithDrivers(HashMap<Class<?>,BuessinessLogicDriver> drivermap);
		HashMap<Method, LinkedList<DataNoticeListener>> getEventCaseMap();
	}
	
	interface Generator<TEMPLATE extends Portal<TEMPLATE>> {
		TEMPLATE generate(HashMap<Method, PMap> methodinfos, HashMap<Method, LinkedList<PMap>> paraminfos);
		Object getDefaultAttrValue();
	}
	
	interface SessionAwareGenerator<TE  extends Portal<TE>> extends Generator<TE>{
		void commitSession(Method pivoit ,PMap methodinfos,LinkedList<PMap> paraminfos);
	}

	interface EventSource {
		void setCaseListeners(Map<Method, LinkedList<DataNoticeListener>> ecasemap);
	}

	interface DataNoticeListener {
		void onNotice(Object eventSource, Method eventCase, Object... msgs);
		Set<Method> getListenningCase();
//		void addListeningCase(List<Method> lsnCase);
//

	}
}
