package j40p.infou.util.toolsPack.def;

public interface P<T> { // P stand for property.
	Class<?> getBelongingClazz();
	String getSimpleName();
	String getPathName();
	
	//void setBelongingClazz(Class<?> clz);
	//void setSimpleName(String spname);
}
