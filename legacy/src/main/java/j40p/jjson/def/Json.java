package j40p.jjson.def;


public interface Json {

	<T> T get(Convertor<T> convertor,Object key);
    <T> void set(Constrain<T> constrain ,Object key,T value);
}