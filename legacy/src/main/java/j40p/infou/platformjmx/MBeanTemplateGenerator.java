package j40p.infou.platformjmx;

import j40p.infou.platformjmx.base.MBeanType;
import j40p.infou.platformjmx.base.MBeanTypeManager;
import j40p.infou.platformjmx.def.MBeanItemType;
import j40p.infou.platformjmx.synthesizer.Builder4MBeanType;
import j40p.infou.platformjmx.synthesizer.Builder4MethodWrapper;
import j40p.infou.util.toolsPack.BeanUtil;
import j40p.infou.util.toolsPack.InfoUtil;
import j40p.infou.util.toolsPack.T_;
import j40p.infou.util.toolsPack.def.CastableBean;
import j40p.infou.util.toolsPack.def.P;
import j40p.infou.util.toolsPack.def.PMap;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

public class MBeanTemplateGenerator implements InfoUtil.SessionAwareGenerator<MBeanDomainTemplate> {
	//public static final P<String> sing = T_.instance.def(MBeanTemplateGenerator.sing);
	public static final P<Boolean> isexport = T_.instance.def(MBeanTemplateGenerator.isexport);
	public static final P<MBeanItemType> lMBeanItemType = T_.instance.def(MBeanTemplateGenerator.lMBeanItemType);
	public static final P<String> MBeanItemName = T_.instance.def(MBeanTemplateGenerator.MBeanItemName);
	public static final P<Object[]> paramLegalChoices=T_.instance.def(MBeanTemplateGenerator.paramLegalChoices);
	
	

	private LinkedList<Builder4MethodWrapper> mwraperList = new LinkedList<Builder4MethodWrapper>();
	private LinkedList<Builder4MBeanType> mbtypeList = new LinkedList<Builder4MBeanType>();
	@Override
	public MBeanDomainTemplate generate(HashMap<Method, PMap> methodInfos, HashMap<Method, LinkedList<PMap>> pramasInfos ) {
		
		HashMap<Class<?>,LinkedList<Builder4MethodWrapper>> tmap = new HashMap<Class<?>, LinkedList<Builder4MethodWrapper>>();
		//Make MBeanType list/ MbeanTypeManager here
		//System.out.println("size-----------");
		//System.out.println(this.mwraperList.size());
		for(Builder4MethodWrapper i : this.mwraperList){
			System.out.println("generator:_"+i.MbeanItemName);
			i.TargetMethodInfo=methodInfos.get(i.targetMethod);
			i.TargetMethodParamInfo=pramasInfos.get(i.targetMethod);
			LinkedList<Builder4MethodWrapper> tlist =tmap.get(i.bindingBean);
			if(tlist==null){
				tlist = new LinkedList<Builder4MethodWrapper>();
				tmap.put(i.bindingBean, tlist);
			}
			tlist.add(i);
		}
		Set<Class<?>> keys= tmap.keySet();
		for(Class<?> i : keys){
			Builder4MBeanType b4mbt = new Builder4MBeanType();
			b4mbt.bindingtype=(Class<? extends CastableBean>)i;
			b4mbt.mwraperList.addAll(tmap.get(i));
			this.mbtypeList.add(b4mbt );
		}
		MBeanType[] mbta = new MBeanType[this.mbtypeList.size()];
		int mbti  =0;
		for(Builder4MBeanType i : this.mbtypeList){
			mbta[mbti++]=i.get();
		}
		MBeanTypeManager mbm = new MBeanTypeManager (mbta);
		MBeanDomainTemplate mbtp = new MBeanDomainTemplate( mbm);
		return mbtp;
		
		
	}
	


	@Override
	public void commitSession(Method pivoit, PMap methodinfos, LinkedList<PMap> paraminfos) {
		//System.out.println("here????");
		System.out.println("sessioncommits:_"+pivoit.getName());
		if(methodinfos.get(MBeanTemplateGenerator.lMBeanItemType)!=null){
			Builder4MethodWrapper bmw = new Builder4MethodWrapper();
			
			bmw.accessList=methodinfos.get(BeanUtil.bindingAccessorList);;
			bmw.bindingBean= methodinfos.get(BeanUtil.bindingBeanType);
			bmw.targetMethod=pivoit;
			bmw.MbeanItemName=methodinfos.get(MBeanTemplateGenerator.MBeanItemName);
			if(bmw.MbeanItemName==null)
				bmw.MbeanItemName=pivoit.getName();
			bmw.MbeanItemType=methodinfos.get(MBeanTemplateGenerator.lMBeanItemType);
			
			methodinfos.remove(BeanUtil.bindingAccessorList);
			methodinfos.remove(BeanUtil.bindingBeanType);
			
			this.mwraperList.add(bmw);
		}
	}
	
	@Override
	public Object getDefaultAttrValue() {
		
		return PMap.makeDefault(
			InfoUtil.instance.nvp(MBeanTemplateGenerator.isexport, true),
			InfoUtil.instance.nvp(MBeanTemplateGenerator.lMBeanItemType, MBeanItemType.Operation)
			);
	}
	
	
	private void preGenterate(HashMap<Method, PMap> methodInfos, HashMap<Method, LinkedList<PMap>> pramasInfos){
		
		
	}

	

		
}
