package impl.j40p.base.t;


public abstract class HKey<E> implements Comparable<E> {
	public abstract int dhash(int d);
	public abstract boolean equals(Object obj);
}
