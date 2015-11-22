package j40p.jjson.json;

import j40p.jjson.json.Json.Jmd;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Stack;

import com.tibco.as.util.toolsPack.DefUtil;

class JsonToolBoximpl implements JsonToolBox {
	private JsonToolBoximpl(){};
	@Override
	public Json getJson() {
		return (Json)Proxy.newProxyInstance(JsonToolBox.class.getClassLoader(), new Class<?>[]{Json.class},
			new JsonImplHandler());
	}

	@Override
	public Object fromUtf8JsJson(byte[] jsdata) {
		byte[] stack = new byte[1000];
		int sp = 0;
		return null;
	}

	@Override
	public byte[] toUtf8JsObjArray(Object objs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] toUtf8JsJson(Json json) {
		JsonImplHandler jih = (JsonImplHandler) Proxy.getInvocationHandler(json);
		jih.jsonmap.get("any");
		return null;
	}

	static class JsonImplHandler implements InvocationHandler {
		private static HashMap<Method, Jmd> mdmap = new HashMap<Method,Jmd>();
		
		private HashMap<Object, Object> jsonmap = new HashMap<Object,Object>();
		
		static {
			Jmd[] jmds = Jmd.values();
			HashMap<String, Jmd> jmdmap = new HashMap<String,Jmd>();
			for (Jmd i : jmds) {
				jmdmap.put(i.name(), i);
			}
			Method[] md = Json.class.getMethods();
			String mname = null;
			for (Method i : md) {
				mname = i.getName();
				Jmd cpmd = jmdmap.get(mname);
				if (cpmd != null)
					mdmap.put(i, cpmd);

			}
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)throws Throwable {
			Object value = DefUtil.instance.mimicObj4Proxy(method, proxy, args);
			if(value!=null)
				return value;
			
			
			Object key = args[0];
			boolean asklist = false;
			boolean storedlist = false;
			
			//boolean getop = false;
			//Object value = null;
			
			if (key == null)
				throw new RuntimeException("key can not be null.");
			if(key instanceof Enum<?>){
				key=((Enum<?>)key).name();
			}
			Jmd cujmd = JsonImplHandler.mdmap.get(method);
			switch (cujmd) {

				case getBoolean:
				case getInt:
				case getString:
				case getDouble:
				case getLong:
				case getFloat:
				case getJson:
								
				case setBoolean:
				case setInt:
				case setString:
				case setDouble:
				case setLong:
				case setFloat:
				case setJson:
					asklist=false;break;
				case getBooleanList:
				case getIntList:
				case getStringList:
				case getDoubleList:
				case getLongList:
				case getFloatList:
				case getJsonList:
					
				case setJsonList:
				case setBooleanList:
				case setIntList:
				case setStringList:
				case setDoubleList:
				case setLongList:
				case setFloatList:
					asklist=true;break;
				case getData:
					break;
				case setData:
					break;
				default:
					break;

			}
			switch (cujmd) {
				case getData:
					return this.jsonmap.get(key);
				case getBoolean:
				case getInt:
				case getString:
				case getDouble:
				case getLong:
				case getFloat:
				case getJson:
					
				case getBooleanList:
				case getIntList:
				case getStringList:
				case getDoubleList:
				case getLongList:
				case getFloatList:
				case getJsonList:
					//getop=true;
					value = this.jsonmap.get(key);
					if (value != null){
						storedlist = value.getClass().isArray();
						Class<?> asktp = method.getReturnType();
						Class<?> rtcomptp = null;
						//Class<?> storetp = null;
						if(asklist){
							rtcomptp=asktp.getComponentType();	
						}else{
							rtcomptp=asktp;
						}
						if(asklist && !storedlist){ // return list
							//rtcomptp=asktp.getComponentType();	
							if(rtcomptp.isAssignableFrom(value.getClass())){
								Object avalue = Array.newInstance(rtcomptp, 1);
								Array.set(avalue, 0, value);
								return avalue;
							}
							if(value instanceof String){
								if(rtcomptp.equals(String.class)){ 
									return new String[]{ (String)value};
								}else if(rtcomptp.equals(Json.class)){
									throw new RuntimeException("conver from String to Json is not supported.");
								}else
									value= new String[]{(String)value};
							}else{
								if(rtcomptp.equals(String.class)){ 
									return new String[]{ value.toString()};
								}else // 
									throw new RuntimeException("wrong data type to get from the stored value,type not convertable from each other.");
							}
						}else if(!asklist && storedlist){ // single value
							//rtcomptp=asktp;
							int slength = Array.getLength(value);
							if(slength>0){
								value = Array.get(value, 0);
								if(rtcomptp.isAssignableFrom(value.getClass()))
									return value;
								if(value instanceof String){
									if(rtcomptp.equals(String.class)){ 
										return  value;
									}else if(rtcomptp.equals(Json.class)){
										throw new RuntimeException("conver from String to Json is not supported.");
									}
								}else{
									if(rtcomptp.equals(String.class)){ 
										return  value.toString();
									}else // 
										throw new RuntimeException("wrong data type to get from the stored value,type not convertable from each other.");
								}
							}else
								return null;
						}
//						else if(asklist && storedlist){
//							//rtcomptp=asktp.getComponentType();	
//						}else{ // !asklist && ! storedlist
//							//rtcomptp=asktp;	
//						}
						else if(method.getReturnType().isAssignableFrom(value.getClass())){
							return value;
						}else{
							if(value instanceof String[] || value instanceof String){
								Method cvmd = rtcomptp.getMethod("valueOf", new Class<?>[]{String.class});
								if(asklist){ //list return 
									
									String[] strv = (String[])value;
									int sl =  strv.length;
									Object rtarray = Array.newInstance(rtcomptp, sl);
									
									if(sl>0){
										for(int i =0;i<sl;i++){
											Object covertv = 	cvmd.invoke(rtcomptp, (Object)strv[i]);
											Array.set(rtarray, i, covertv);
										}
										return rtarray;
									}else
										return Array.newInstance(rtcomptp, 0);
									
								}else{ // single return
									String strv = (String)value;
									return cvmd.invoke(rtcomptp, (Object)strv);
								}
							}else{
								if(rtcomptp.equals(String.class)){ 
									if(asklist){
										String[] rvstr = new String[Array.getLength(value)];
										int rvsl = rvstr.length;
										for(int i =0;i<rvsl;i++){
											rvstr[i]=Array.get(value, i).toString();
										}
										return rvstr;
									}else
										return  value.toString();
								}
								throw new RuntimeException("wrong data type to get from the stored value,type not convertable from each other.");
							}	
						}
					}else
						return null;
					break;
				case setBoolean:
				case setInt:
				case setString:
				case setDouble:
				case setLong:
				case setFloat:
				case setJson:
					
				case setJsonList:
				case setBooleanList:
				case setIntList:
				case setStringList:
				case setDoubleList:
				case setLongList:
				case setFloatList:
				case setData:
					//getop=false;
					this.jsonmap.put(key, args[1]);
					return null;
				default:
					break;
			}
			return null;
		}

	}

}