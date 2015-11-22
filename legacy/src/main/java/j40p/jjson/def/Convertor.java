package j40p.jjson.def;

public interface Convertor<TargetType> extends Constrain<TargetType> {
	TargetType convert(Object data);
	boolean isSupport(Object data);
}
