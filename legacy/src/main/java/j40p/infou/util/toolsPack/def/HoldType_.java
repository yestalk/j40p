package j40p.infou.util.toolsPack.def;

public class HoldType_<E>{
	public static HoldType_<Boolean> replace= new HoldType_<Boolean>(Boolean.class);
	Class<E> clz;
	public HoldType_(Class<?> o) {
		this.clz=(Class<E>)o;
	}
	
	public Class<E> getTypeClass(){
		return this.clz;
	}
	
	
}