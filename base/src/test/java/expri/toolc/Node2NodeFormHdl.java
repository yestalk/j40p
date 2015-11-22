package expri.toolc;

 
import j40p.base.UTF8ByteStr;
import j40p.tabform.iface.Ele;
import j40p.tabform.iface.ObjPath;
import j40p.tabform.iface.TabFormHdl;
import j40p.tabform.impl.EvNodeListParser;

import java.util.LinkedList;

public class Node2NodeFormHdl implements TabFormHdl {

	@Override
	public boolean iscommentsIgnore() {
		// TODO Auto-generated method stub
		return this.hdltst.iscommentsIgnore();
	}

	int ev = 0;

	PrintHdl hdltst = new PrintHdl();

	@Override
	public Object createNode(ObjPath ancestors, Object subject) {
		return null;
	}

	@Override
	public Object createNode(ObjPath ancestors, Ele subject) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object createNode(ObjPath ancestors, int type, UTF8ByteStr subject) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object concludeNode(ObjPath ancestors, Object subject, LinkedList<?> children) {
		//System.out.println("here? tab2node concludeNode");
		return null;
	}

	@Override
	public void overAll(LinkedList<?> total) {
		 System.out.println("node 2 node.");
		EvNodeListParser.i.parse( EvNodeListParser.i.packSiblingList(total), hdltst);
//		 for(Object i : total){
//			 System.out.println(i);
//		 }
//		EvNodeList enl = EvNodeListParser.i.packSiblingList(total);
//		for( Ev i : enl){
//			System.out.println(i);
//		}
		 
	

	}

}
