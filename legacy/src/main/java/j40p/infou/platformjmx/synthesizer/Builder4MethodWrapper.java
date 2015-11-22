package j40p.infou.platformjmx.synthesizer;

import j40p.infou.platform.commmon.convdef.Convertor;
import j40p.infou.platform.commmon.convdef.ConvertorGroup;
import j40p.infou.platform.commmon.covimpl.String2Enum;
import j40p.infou.platformjmx.base.CombainKey;
import j40p.infou.platformjmx.base.MBeanType.RequestHandler;
import j40p.infou.platformjmx.builders.Builder4DescriptorSupport;
import j40p.infou.platformjmx.builders.Builder4OpenMBeanAttributeInfoSupport;
import j40p.infou.platformjmx.builders.Builder4OpenMBeanOperationInfoSupport;
import j40p.infou.platformjmx.builders.Builder4OpenMBeanParameterInfoSupport;
import j40p.infou.platformjmx.convimpl.BeanList2TabularData;
import j40p.infou.platformjmx.convimpl.CastableBean2CompositData;
import j40p.infou.platformjmx.convimpl.CompositData2CB;
import j40p.infou.platformjmx.def.MBeanItemType;
import j40p.infou.platformjmx.def.MBeanUtil;
import j40p.infou.util.toolsPack.DefUtil;
import j40p.infou.util.toolsPack.InfoUtil;
import j40p.infou.util.toolsPack.def.CastableBean;
import j40p.infou.util.toolsPack.def.PMap;
import j40p.infou.util.toolsPack.def.CastableBean.Accessor;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.TabularType;

public class Builder4MethodWrapper {

	public Method targetMethod;
	public Accessor[] accessList;
	
	public Class<? extends CastableBean> bindingBean;
	public MBeanItemType MbeanItemType;
	
	public Class<?> AttrParamType;
	public String MbeanItemName;
	
	public LinkedList<Builder4MethodWrapper> SynthCluster = new LinkedList<Builder4MethodWrapper>();
	
	public PMap TargetMethodInfo;
	public LinkedList<PMap> TargetMethodParamInfo;
	
	
	private String[] TailoredSinglist;
	private LinkedList<PMap> TailoredParamInfo;
	private ConvertorGroup TailoredConvList=new ConvertorGroup();
	private LinkedList<Type> OrgParamTypeList ;

//	MBeanItemType mit,
//	String alias,
//	Method targetMethod,
//	Accessor[] accessList,
//	String[] maskedSingnature,
//	CovertorGroup maskedConvList,
//	Convertor outValueConv
	
	public RequestHandler get(){
		return new RequestHandler(
			this.MbeanItemType,
			this.MbeanItemName,
			this.targetMethod,
			this.accessList,
			this.TailoredSinglist,
			this.TailoredConvList,
			this.getOutValueConv()
			);
	}
	
//	private CovertorGroup getTailoredConvList(){
//		return null;
//	}
	

	public Builder4OpenMBeanOperationInfoSupport getOperationInfo(){
		if(!this.MbeanItemType.equals(MBeanItemType.Operation))
			throw new RuntimeException("this item is not a mbean operation");
		
		
		Builder4OpenMBeanOperationInfoSupport b4op = new Builder4OpenMBeanOperationInfoSupport();
		
		b4op.description=this.TargetMethodInfo.get(InfoUtil.helpsInfo);
		b4op.name=this.MbeanItemName;
		b4op.returnOpenType=MBeanUtil.instance.getOpenType(this.targetMethod.getReturnType());
		Iterator<PMap> ppit = this.TailoredParamInfo.iterator();
		
		//CovertorGroup cgroup = new CovertorGroup();
		
		int orgpi = 0;
		for(Type i: this.OrgParamTypeList){
			PMap lparaminfo = ppit.next();
			Builder4OpenMBeanParameterInfoSupport b4opsp = new Builder4OpenMBeanParameterInfoSupport();
			Class<?> li = null;
			if(i instanceof Class<?>){
				 li = (Class<?>)i;

			}else if(i instanceof ParameterizedType){
				li =  (Class<?>)((ParameterizedType)i).getRawType();
			}else
				throw new RuntimeException("can't declare ParameterizedType paramter other than List<Any Castalbean type>");
			
			b4opsp.openType=MBeanUtil.instance.getOpenType(i);
			b4opsp.name=lparaminfo.get(InfoUtil.paramName);
			b4opsp.description=lparaminfo.get(InfoUtil.helpsInfo);
			b4opsp.descriptor.setField(Builder4DescriptorSupport.originalType, li.getName());
			b4opsp.descriptor.setField(Builder4DescriptorSupport.defaultValue,lparaminfo.get(InfoUtil.paramdemoValue));
			b4opsp.descriptor.setField(Builder4DescriptorSupport.legalValues,this.getLegalValues(li));
			b4op.signature.add(b4opsp);
			
			Convertor pconv = this.getConvertor(i);
			
			if(pconv!=null)
				this.TailoredConvList.add(orgpi, pconv);
			//this.TailoredConvList.add(e);
			//this.TailoredConvList=
			orgpi++;
		}
		
		return b4op;
	}
	
	public Builder4OpenMBeanAttributeInfoSupport getAttrInfo(){
		if(!(this.MbeanItemType.equals(MBeanItemType.AttributeGetter) || this.MbeanItemType.equals(MBeanItemType.AttributeSetter)))
			throw new RuntimeException("this item is not a mbean operation");
		
		Builder4OpenMBeanAttributeInfoSupport b4attr = new Builder4OpenMBeanAttributeInfoSupport();
		b4attr.description="attr";
		b4attr.isReadable=true; // no write only attr
		b4attr.name=this.MbeanItemName;
		b4attr.isIs=false;
		b4attr.openType=MBeanUtil.instance.getOpenType(this.targetMethod.getGenericReturnType());
		return b4attr;
	}
	
	public CombainKey getCombkey4Operation(){
		if(!this.MbeanItemType.equals(MBeanItemType.Operation))
			throw new RuntimeException("this item is not a mbean operation");
		if(this.TargetMethodParamInfo.size()==accessList.length)
			throw new RuntimeException("argument property map list don't match with the access list");
		
		Type[] pmtp = this.targetMethod.getGenericParameterTypes();
		String[]  sig = new String[pmtp.length];
		int ci =0;
		for(Type i : pmtp){
			if(i instanceof ParameterizedType)
				sig[ci]=((Class<?>)((ParameterizedType) i).getRawType()).getName();
			else if(i instanceof Class<?>)	
				sig[ci]=((Class<?>)i).getName();
			ci++;
		}
		ci=0;
		int wtypei =-1;
		LinkedList<String> singlist = new LinkedList<String>();
		LinkedList<Type> lotypelist = new LinkedList<Type>();
		
		LinkedList<PMap> tailpplist = new LinkedList<PMap>();
		Iterator<PMap> tialit = this.TargetMethodParamInfo.iterator();
		
		for(Accessor i : this.accessList){
			PMap itmap = tialit.next();
			if(!i.isBeanAccessor()){
				if(this.MbeanItemType.equals(MBeanItemType.AttributeGetter))
					throw new RuntimeException("mbean atter whould receive all param form binding castableBean");
				singlist.add(sig[ci]);
				lotypelist.add(pmtp[ci]);
				wtypei=ci;
				
				tailpplist.add(itmap);
			}
			ci++;
		}
		this.TailoredParamInfo=tailpplist;
		this.OrgParamTypeList=lotypelist;
		this.TailoredSinglist=singlist.toArray(new String[]{});
		if(singlist.size()==1){
			this.AttrParamType=(Class<?>)pmtp[wtypei];
		}
		return new CombainKey(this.MbeanItemName,singlist.toArray(new String[]{}));
	}

	private Convertor getConvertor(Object desttype){
		Convertor rtv =null;
		Class<?> classtype=null;
		Class<? extends CastableBean> beantype=null;
		ParameterizedType ptype = null;
		OpenType<?> opentype = null;
		if(desttype instanceof ParameterizedType){
			ptype=(ParameterizedType)ptype;
			OpenType<?> ltabtp = MBeanUtil.instance.getOpenType(ptype);
			return this.getConvertor(ltabtp);
		}else if (desttype instanceof Class<?>){
			classtype=(Class<?>)desttype;
			if(Enum.class.isAssignableFrom(classtype))
				rtv=new String2Enum();
			else if(CastableBean.class.isAssignableFrom(classtype))
				rtv = new CompositData2CB();
		}else if(desttype instanceof OpenType<?>){
			opentype=(OpenType<?>)desttype;
			if(opentype instanceof CompositeType){
				//bean to composite
				rtv= new CastableBean2CompositData();
			}else if(opentype instanceof TabularType){
				//list of bean to tabular
				rtv= new BeanList2TabularData();
			}
		}else
			return null;
		rtv.setDestType(desttype);
		//rtv.setCorrespondingIndex(paramindex);
		return rtv;
		
	}
	

	private Convertor getOutValueConv(){
		Type rvtyp = this.targetMethod.getGenericReturnType();
		OpenType<?> opentp =  MBeanUtil.instance.getOpenType(rvtyp);
		return this.getConvertor(opentp);
	}
	
	private Set<?> getLegalValues(Class<?> clz){
		HashSet<Object> hbset = new HashSet<Object>();
		if(clz.isEnum()){
			Enum[]  econst = (Enum[])clz.getEnumConstants();
			String[] strconst = new String[econst.length];
			int stri=0;
			for(Enum i : econst){
				strconst[stri++]=i.name();
			}
			hbset.addAll(Arrays.asList(strconst));
		}else if(Boolean.class.equals(clz) || boolean.class.equals(clz)){
			hbset.addAll(Arrays.asList(DefUtil.booleanenumvalues));
		}
		
		if(hbset.size()>0)
			return hbset;
		else
			return null;
	}
	
	
//	
//	public String[] tailredmsing(){
//		if(this.paraminfo.size()==acc.length)
//			throw new RuntimeException("argument property map list don't match with the access list");
//		
//		Class<?>[] pmtp = this.tmd.getParameterTypes();
//		String[]  sig = new String[pmtp.length];
//		int ci =0;
//		for(Class<?> i : pmtp){
//			sig[ci++]=i.getName();
//		}
//		ci=0;
//		int wtypei =-1;
//		LinkedList<String> singlist = new LinkedList<String>();
//		LinkedList<Class<?>> lotypelist = new LinkedList<Class<?>>();
//		
//		LinkedList<PMap> tailpplist = new LinkedList<PMap>();
//		Iterator<PMap> tialit = this.paraminfo.iterator();
//		
//		for(Accessor i : this.acc){
//			PMap itmap = tialit.next();
//			if(!i.isBeanAccessor()){
//				if(this.optype.equals(OpType.attr))
//					throw new RuntimeException("mbean atter whould receive all param form binding castableBean");
//				singlist.add(sig[ci]);
//				lotypelist.add(pmtp[ci]);
//				wtypei=ci;
//				
//				tailpplist.add(itmap);
//			}
//			ci++;
//		}
//		this.tailoredparaminfo=tailpplist;
//		this.otypelist=lotypelist;
//		if(singlist.size()==1){
//			this.attrWType=pmtp[wtypei];
//		}
//		return singlist.toArray(new String[]{});
//	}

	
//	private void  bindingCheck(){ // check for if all param accessor of a target method is binding from a single castableBean type.
//		
//	}
//	
//	private void readableAttrCheck(){// readable attr would access all param from binding castablebean.
//		
//	}
//	
//	private void writableAttrCheck(){// writable attr would has its coresponding readable attr , and take one param of type as the same as the readable attr return value.
//		
//	}
}


//public MethodWrapper(Method tmd, Accessor[] acc,OpType optype,String AttrName){
//Class<?>[] ptypes =   tmd.getParameterTypes();
//int len =ptypes.length;
//
//int binacccount=0;
//int paramid=-1;
//int ordercout=0;
//for(Accessor i: acc){
//	if(i.isBeanAccessor())
//		binacccount++;
//	else
//		paramid=ordercout;
//	ordercout++;
//}
//if(optype.equals(OpType.attr)){
//	
//
//	if(len!=binacccount)
//		throw new RuntimeException("read only property can not have extra paramters");
//}
//if(optype.equals(OpType.wattr)){
//	if(len-binacccount!=1)
//		throw new RuntimeException("writable property can have only one paramter");
//	this.attrWType=ptypes[ordercout];
//	
//}
//this.tmd=tmd;
//this.acc=acc;
//this.optype=optype;
//}
//public boolean isParamAllFromBean(){
//for(Accessor i : acc){
//	if(!i.isBeanAccessor())
//		return false;		
//}
//return true;
//}

//private Convertor getInConvertor(Class<?> destclz,int paramindex){
//Convertor rtv =null;
//if(Enum.class.isAssignableFrom(destclz))
//	rtv=new String2Enum();
//else if(CastableBean.class.isAssignableFrom(destclz)){
//	rtv = new CompositData2CB();
//}else if(List.class.isAssignableFrom(destclz)){
//	
//}
//	
//	
//rtv.setDestType(destclz);
//rtv.setCorrespondingIndex(paramindex);
//return rtv;
//}

//private Convertor getOutConvertor(OpenType<?> opentype,int paramindex){
//Convertor rtv =null;
//if(opentype instanceof CompositeType){
//	
//}else if(opentype instanceof TabularType){
//	
//}
//
//return rtv;
//}
//

//private int getArraydim(Class<?> arraytp){
//
//
//String s =arraytp.getName();   
//String patternStr="(\\[)+";
//Pattern p = Pattern.compile(patternStr);
//Matcher m = p.matcher(s);
////System.err.println(s);
//if(m.find()){
//    int count = m.groupCount();
//    if(count==1)
//    	return m.group(0).length();
//    else
//    	throw new RuntimeException("not a array type");
//}else
//	throw new RuntimeException("not a array type");
//}
//private SimpleType<?>  getSimpleType(Class<?> clz){
//
//return null;
//}
