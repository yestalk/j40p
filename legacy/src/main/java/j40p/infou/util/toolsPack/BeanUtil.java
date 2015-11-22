package j40p.infou.util.toolsPack;

import j40p.infou.util.toolsPack.InfoUtil.AugSession;
import j40p.infou.util.toolsPack.def.CastableBean;
import j40p.infou.util.toolsPack.def.P;
import j40p.infou.util.toolsPack.def.PList;
import j40p.infou.util.toolsPack.def.S;
import j40p.infou.util.toolsPack.def.CastableBean.Accessor;

import java.util.Map;

public interface BeanUtil {
	BeanUtil instance  = S.instance.singleton(BeanUtil.class);
	public static final PList<Accessor> bindingAccessor = T_.instance.defList(BeanUtil.bindingAccessor);//,"bindingAccessor");
	public static final P<Accessor[]> bindingAccessorList = T_.instance.def(BeanUtil.bindingAccessorList);//,"bindingAccessorList");
	public static final P<Class<? extends CastableBean>> bindingBeanType = T_.instance.def(BeanUtil.bindingBeanType);//,"bindingBeanType");
	
	<CB extends CastableBean> CB get(Class<CB> type);
	
	<CB extends CastableBean> CB getAsBindingLabel(Class<CB> type,AugSession augse);
	
	//Accessor[] prepareAccessorList(Accessor[] accs);
	Object[] $demovalues(CastableBean proxy);
	Object[] $values(CastableBean proxy);
	String[] $keys(CastableBean proxy);
	String[] $indices (CastableBean proxy);
	Class<?>[] $types(CastableBean proxy);
	Class<?> $beantype(CastableBean proxy);
	
	String[] $helpinfo(CastableBean proxy);
	
	void $provide(CastableBean proxy,Map<String,Object> data);
	Object[] $access(CastableBean cx,Accessor[] acc,Object[] inparam);
 
}
