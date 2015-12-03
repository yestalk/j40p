package j40p.base.l;

public interface HashKey  extends Comparable<Object> {
	int dhash(int d);
	boolean equals(Object obj);
}
