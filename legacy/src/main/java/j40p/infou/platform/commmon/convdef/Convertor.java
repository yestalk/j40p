package j40p.infou.platform.commmon.convdef;

public interface Convertor {
//	void setCorrespondingIndex(int dx);
//	int getCorrespondingIndex();
	Object conv(Object value);
	void setDestType(Object type);
}
