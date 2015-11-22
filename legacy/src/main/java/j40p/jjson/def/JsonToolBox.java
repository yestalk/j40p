package j40p.jjson.def;


import com.tibco.as.util.toolsPack.def.S;


public interface JsonToolBox{
	JsonToolBox i = S.instance.singleton(JsonToolBox.class);
	
	Json getJson();
	
	Object fromUtf8JsJson(byte[] jsdata);
	 
	byte[] toUtf8JsObjArray(Object objs);
	//the out most is array : [4,5,{key:v, key2:v2, key3: [4,5,{key:v, key2:v2, key3:v3},”dfdf”]},”dfdf”]
	 
	byte[] toUtf8JsJson(Json json);
	//the out most is json : {key:”v”,key2:”v2”,key3: [4,5,{key:v, key2:v2, key3: [4,5,{key:v, key2:v2, key3:v3},”dfdf”]},”dfdf”]}
}