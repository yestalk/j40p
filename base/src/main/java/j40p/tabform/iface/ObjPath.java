package j40p.tabform.iface;

public interface ObjPath extends Iterable<Object> {
	
	Object getFrist();
	Object getLast();
	Object getith(int i );
	int size();
	
	ObjPath shiftedRoot(int offest);
	
}
