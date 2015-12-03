package expri.toolc;

 

import j40p.base.UTF8ByteStr;
import j40p.base.cache.ABBBuf;
import j40p.base.cache.IntABuf;
import j40p.base.cache.ObjectCacheX;
import j40p.base.parser.BFHandler;
import j40p.base.parser.IntBuilder;
import j40p.tabform.iface.Ele;
import j40p.tabform.iface.ObjPath;
import j40p.tabform.iface.TabFormHdl;
import j40p.tabform.impl.EvABuf;
import j40p.tabform.impl.FileParser;
import j40p.tabform.impl.Stk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

public class PrintHdl implements TabFormHdl {

	@Override
	public boolean iscommentsIgnore() {
		// TODO Auto-generated method stub
		return true;
	}

	int ev = 0;

	@Override
	public Object createNode(ObjPath ancestors, Object subject) {

		return null;

	}

	@Override
	public Object concludeNode(ObjPath ancestors, Object subject, LinkedList<?> children) {
//		if(subject.toString().equals("8x"))
//			return TabFormHdl.EscapeStructure;
		
		
		for (int i = 0, l = ancestors.size(); i < l; i++) {
			System.out.print("\t");
		}
		children = (children == null) ? new LinkedList<Object>() : children;
		System.out.println("end, childrentSize of:___" + children.size() + "___" + subject);
		return TabFormHdl.EscapeStructure;
		//return TabFormHdl.EscapeStructure;
	}

	@Override
	public void overAll(LinkedList<?> total) {
		System.out.println("!print ending");
	}

	@Override
	public Object createNode(ObjPath ancestors, Ele subject) {
		// this.ev++;
		
		
//		if(subject.getName().asString().equals("e7") || subject.getName().asString().equals("ee7"))
//			return TabFormHdl.EscapeStructure;
		for (int i = 0, l = ancestors.size(); i < l; i++) {
			System.out.print("\t");
		}
		System.out.println(subject);
		return subject;
		
		//return null;
	}

	@Override
	public Object createNode(ObjPath ancestors, int type, UTF8ByteStr subject) {
		
		// this.ev++;
		if(subject.asString().equals("txt12"))
			return TabFormHdl.EscapeStructure;
		for (int i = 0, l = ancestors.size(); i < l; i++) {
			System.out.print("\t");
		}
		System.out.println(subject);
		return subject;
		//return null;
	}
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		BFHandler.setBFCache(ObjectCacheX.t.lookup(ABBBuf.class, ABBBuf.getCriteriaBy(1402+32, false), 10000));
		 
		IntBuilder.setBFCache(ObjectCacheX.t.lookup(IntABuf.class, IntABuf.getCriteriaByCapacity(768), 10000));
		//Stk.setBFCache(ObjectCacheX.t.lookup(EvABuf.class, EvABuf.getCriteriaByCapacity(32), 1000));
		//PrintHdl t2nfh = new PrintHdl();
		Tab2NodeFormHdl t2nfh = new Tab2NodeFormHdl();
		
		FileParser fnio = new FileParser();
		fnio.parse(new File("d:/sftest.txt"), t2nfh);
		

	}

 
}
