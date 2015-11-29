package impl.j40p.base;

public abstract class HKey implements Comparable<Object> {
	public abstract int dhash(int d);
	public abstract boolean equals(Object obj);
}
