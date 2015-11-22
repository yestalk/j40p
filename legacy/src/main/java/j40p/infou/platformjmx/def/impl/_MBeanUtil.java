package j40p.infou.platformjmx.def.impl;

import j40p.infou.platformjmx.def.MBeanUtil;
import j40p.infou.util.toolsPack.BeanUtil;
import j40p.infou.util.toolsPack.def.CastableBean;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.openmbean.ArrayType;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;
import javax.management.openmbean.TabularType;

public class _MBeanUtil implements MBeanUtil {
	private static HashMap<ParameterizedType,TabularType> List2TabularTypeCache = new HashMap<ParameterizedType,TabularType>();
	private static HashMap<Class<? extends CastableBean>,CompositeType> CB2OpenTypeCache = new HashMap<Class<? extends CastableBean>, CompositeType>();
	private static HashMap<Class<?>,SimpleType<?>> SimpleTypeMap = new HashMap<Class<?>, SimpleType<?>>();
	private static HashMap<Class<?>,ArrayType<?>> ArrayTypeMap = new HashMap<Class<?>, ArrayType<?>>();
	static{
		_MBeanUtil.SimpleTypeMap.put(void.class, SimpleType.VOID);
		_MBeanUtil.SimpleTypeMap.put(int.class, SimpleType.INTEGER);
		_MBeanUtil.SimpleTypeMap.put(double.class, SimpleType.DOUBLE);
		_MBeanUtil.SimpleTypeMap.put(float.class, SimpleType.FLOAT);
		_MBeanUtil.SimpleTypeMap.put(long.class, SimpleType.LONG);
		_MBeanUtil.SimpleTypeMap.put(char.class, SimpleType.CHARACTER);
		_MBeanUtil.SimpleTypeMap.put(byte.class, SimpleType.BYTE);
		_MBeanUtil.SimpleTypeMap.put(boolean.class, SimpleType.BOOLEAN);
		
		_MBeanUtil.SimpleTypeMap.put(Void.class, SimpleType.VOID);
		_MBeanUtil.SimpleTypeMap.put(BigDecimal.class, SimpleType.BIGDECIMAL);
		_MBeanUtil.SimpleTypeMap.put(BigInteger.class, SimpleType.BIGINTEGER);     
		_MBeanUtil.SimpleTypeMap.put(Boolean.class, SimpleType.BOOLEAN);    
		_MBeanUtil.SimpleTypeMap.put(Byte.class ,SimpleType.BYTE);    
		_MBeanUtil.SimpleTypeMap.put(Character.class, SimpleType.CHARACTER);    
		_MBeanUtil.SimpleTypeMap.put(Date.class, SimpleType.DATE );     
		_MBeanUtil.SimpleTypeMap.put(Double.class, SimpleType.DOUBLE);     
		_MBeanUtil.SimpleTypeMap.put(Float.class, SimpleType.FLOAT );    
		_MBeanUtil.SimpleTypeMap.put(Integer.class, SimpleType.INTEGER);
		_MBeanUtil.SimpleTypeMap.put(Long.class, SimpleType.LONG );
		_MBeanUtil.SimpleTypeMap.put(Short.class ,SimpleType.SHORT);
		_MBeanUtil.SimpleTypeMap.put(String.class ,SimpleType.STRING);
		
		_MBeanUtil.SimpleTypeMap.put(ObjectName.class,  SimpleType.OBJECTNAME );
	}
	

	
	@Override
	public OpenType<?> getOpenType(Type javalangType){
		Class<?> normalType =null;
		OpenType<?> rtv =null;
		if(javalangType instanceof Class<?>){
			normalType=(Class<?>)javalangType;
			rtv = _MBeanUtil.SimpleTypeMap.get(normalType);
			if(rtv!=null)
				return rtv;
			else if(normalType.isArray())
				return this.getArrayOpenType(normalType);
			else if(CastableBean.class.isAssignableFrom(normalType))
				return this.getCBean2CompType((Class<? extends CastableBean>)normalType);
			else
				throw new RuntimeException("can't convert to opentype");
		}else if(javalangType instanceof ParameterizedType ){
			
			ParameterizedType combinedType = (ParameterizedType)javalangType;
			TabularType rvtabulartype =_MBeanUtil.List2TabularTypeCache.get(combinedType);
			if(rvtabulartype!=null)
				return rvtabulartype;
			Class<?> lrowtype=(Class<?>)combinedType.getRawType();
			Class<? extends CastableBean> innertype = (Class<? extends CastableBean>)combinedType.getActualTypeArguments()[0];
			CastableBean cbl = BeanUtil.instance.get(innertype);
			String[] index = BeanUtil.instance.$indices(cbl);
			
			if(List.class.isAssignableFrom(lrowtype)){
				CompositeType innercompotype = this.getCBean2CompType(innertype);
				rvtabulartype = _MBeanUtil.List2TabularTypeCache.get(innercompotype);
				if(rvtabulartype!=null)
					return rvtabulartype;
				Builder4TabularType builder4tabtype = new Builder4TabularType();
				builder4tabtype.typeName="List:"+innertype.getName();
				builder4tabtype.description="a set of castableBean";
				builder4tabtype.rowType=innercompotype;
				for(String i: index)
					builder4tabtype.indexNames.add(i);

				rvtabulartype = builder4tabtype.get();
				_MBeanUtil.List2TabularTypeCache.put(combinedType,rvtabulartype);
				return rvtabulartype;
			}
	
			throw new RuntimeException("tabular type can't mapping to type other than List<? extends CastableBean>");
		}
		throw new RuntimeException("unexpected Type");
	}

	private ArrayType<?>  getArrayOpenType(Class<?> clz){
		
		if(!clz.isArray())
			throw new RuntimeException("not a array type passed in");
		
		ArrayType<?> rtv = _MBeanUtil.ArrayTypeMap.get(clz);
		if(rtv!=null)
			return rtv;
		
		Class<?> arrayComponenttype = null;
		int dimension = 1;
		while((arrayComponenttype=clz.getComponentType()).isArray()){
			dimension++;
		}
		
		OpenType<?> opencptyp = this.getOpenType(arrayComponenttype);
		
		try {
			rtv = new ArrayType(dimension,opencptyp );
		} catch (OpenDataException e) {
			throw new RuntimeException(e);
		}
		return rtv;
	}
	
	private CompositeType getCBean2CompType(Class<? extends CastableBean> cbclz){
		CompositeType cachedcomptype = this.CB2OpenTypeCache.get(cbclz);
		if(cachedcomptype!=null)
			return cachedcomptype;
		CastableBean cbinstance = BeanUtil.instance.get(cbclz);
		Builder4CompositeType comptypebuilder = new Builder4CompositeType();
		String[] cbkeys = BeanUtil.instance.$keys(cbinstance);
		//String[] cbindx = BeanUtil.instance.$indices(cbins);
		String[] cbhelps = BeanUtil.instance.$helpinfo(cbinstance);
		Class<?>[] cbtypes =  BeanUtil.instance.$types(cbinstance);
		comptypebuilder.typeName=cbclz.getName();
		comptypebuilder.description="composite type for castablebean";
		int helpi=0;
		for(String i : cbkeys){
			comptypebuilder.itemNames.add(i);
			comptypebuilder.itemDescriptions.add(cbhelps[helpi]);
			//Class<?> attrtp =cbtypes[helpi] ;
			comptypebuilder.itemTypes.add(this.getOpenType(cbtypes[helpi]));
			helpi++;
		}
		cachedcomptype = comptypebuilder.get();
		_MBeanUtil.CB2OpenTypeCache.put(cbclz, cachedcomptype);
		return cachedcomptype;
		
	}
//	@Override
//	public ObjectName getObjectName(String domain,CastableBean bean) {
//		String[]  index = BeanUtil.instance.$indices(bean);
//		String[] key = BeanUtil.instance.$keys(bean);
//		Object[] values = BeanUtil.instance.$values(bean);
//		
//		String oname =domain+":";
//		HashMap<String, Object> vmap = new HashMap<String, Object>();
//		
//		for(int i =0,l=key.length-1;i<=l;i++){
//			vmap.put(key[i], values[i]);
//		}
//		TreeSet<String> indset = new TreeSet<String>();
//		indset.addAll(Arrays.asList(index));
//		for(String i:indset){
//			System.out.println("indexname:"+i);
//			oname+=i+"="+(vmap.get(i).toString())+",";
//		}
//		System.out.println("----------");
//		oname=oname.substring(0, oname.length()-1);
//
//		try {
//			return new ObjectName(oname);
//		} catch (MalformedObjectNameException e) {
//			throw new RuntimeException(e);
//		} catch (NullPointerException e) {
//			throw new RuntimeException(e);
//		}
//	}

	@Override
	public ObjectName getObjectName(String domain,CastableBean bean) {
		String[]  index = BeanUtil.instance.$indices(bean);
		String[] key = BeanUtil.instance.$keys(bean);
		Object[] values = BeanUtil.instance.$values(bean);
		
	
		HashMap<String, Object> vmap = new HashMap<String, Object>();
		Hashtable<String, String> omap = new Hashtable<String, String>();
		for(int i =0,l=key.length-1;i<=l;i++){
			vmap.put(key[i], values[i]);
		}
		
		
		
		for(int i =0,l=index.length-1;i<=l;i++){
			omap.put(index[i], vmap.get(index[i]).toString());
		}
		try {
			return new ObjectName(domain, omap);
		} catch (MalformedObjectNameException e) {
			throw new RuntimeException(e);
		} catch (NullPointerException e) {
			throw new RuntimeException(e);
		}
	}

	
}
