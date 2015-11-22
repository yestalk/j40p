package j40p.jjson.json;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

public interface Json {

	enum Jmd{
		
		getData,
		setData,
		
		getBoolean,	
		getInt,	
		getString,
		getDouble,
		getLong,
		getFloat,
		getJson,

		setBoolean,
		setInt,
		setString,
		setDouble,
		setLong,
		setFloat,
		setJson,
		
		getBooleanList,
		getIntList,
		getStringList,
		getDoubleList,
		getLongList,
		getFloatList,
		getJsonList,
		
		setJsonList,
		setBooleanList,
		setIntList,
		setStringList,
		setDoubleList,
		setLongList,
		setFloatList
		
	}
	Boolean getBoolean(Object key);
    Boolean[] getBooleanList(Object key);
    
    Integer getInt(Object key);
    Integer[] getIntList(Object key);
    
    String getString(Object key);
    String[] getStringList(Object key);
    
    Double getDouble(Object key);
    Double[] getDoubleList(Object key);
    
    Long getLong(Object key);
    Long[] getLongList(Object key);
    
    Float getFloat(Object key);
    Float[] getFloatList(Object key);
    
    Json[] getJsonList(Object key);
	Json getJson(Object key);
	
    Object getData(Object key);
    void setData(Object key ,Object data);
    
    void setJson(Object key ,Json value);
    void setJsonList(Object key ,Json... value);
    
    void setBoolean(Object key ,Boolean value);
    void setBooleanList(Object key ,Boolean... value);
    
    void setInt(Object key ,Integer value);
    void setIntList(Object key ,Integer... value);
    
    void setString(Object key ,String value);
    void setStringList(Object key ,String... value);
    
    void setDouble(Object key ,Double value);
    void setDoubleList(Object key ,Double... value);
    
    void setLong(Object key ,Long value);
    void setLongList(Object key ,Long... value);
    
    void setFloat(Object key ,Float value);
    void setFloatList(Object key ,Float... value);
    
   
    	
    
    
}