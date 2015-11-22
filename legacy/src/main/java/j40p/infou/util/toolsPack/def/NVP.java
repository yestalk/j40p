package j40p.infou.util.toolsPack.def;

public class NVP{ //name value pair
	
	public P<?> type;
	public Object value;
	public <T> NVP(P<T> type ,T value) {
		this.type=type;
		this.value=value;
	}
}