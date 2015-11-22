package j40p.treestru.gui;

import j40p.treestru.TextNode;
import j40p.treestru.XNode;

import java.util.HashSet;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class JTreeModel implements TreeModel {
	private HashSet<TreeModelListener> mlisns = new HashSet<TreeModelListener>();
	private TextNode root = new TextNode("root");
	

	@Override
	public void addTreeModelListener(TreeModelListener lsn) {
		this.mlisns.add(lsn);
		
	}
	@Override
	public void removeTreeModelListener(TreeModelListener lsn) {
		this.mlisns.remove(lsn);
		
	}
	
	@Override
	public Object getChild(Object subject, int index) {
		//System.out.println(index);
		TextNode lxnd = (TextNode)subject;
		return lxnd.getNodeByIndex(index);
	}

	@Override
	public int getChildCount(Object subject) {
		XNode lsubj = (XNode)subject;
		return lsubj.getChildrenSize();
	}

	@Override
	public int getIndexOfChild(Object subject, Object child) {
		XNode lsubj = (XNode)subject;
		return lsubj.getIndex((XNode)child);
	}

	@Override
	public Object getRoot() {
		return this.root;
	}

	@Override
	public boolean isLeaf(Object subject) {
		XNode lsubj = (XNode)subject;
		return lsubj.getChildrenSize()==0;
	}



	@Override
	public void valueForPathChanged(TreePath path, Object newValue) {
		// TODO Auto-generated method stub
		
	}

	
//	public void addChild(XNode current,XNode child){
//		if(current==null)
//			current=root;
//		current.addLast(child);
//		Object[] ancestor = current.getPath4Event();
//		TreeModelEvent e = new TreeModelEvent
//				(this, ancestor, new int[]{current.getIndex(child)}, new Object[]{child});
//		//System.out.println("tree model listener size:___"+this.mlisns.size());
//		for (TreeModelListener i : this.mlisns){
//			i.treeNodesInserted(e);
//		}
//	}
	public void addChild(TreePath parentPath,XNode child){
		XNode current=null;
		if(parentPath!=null){
			current=(TextNode)parentPath.getLastPathComponent();
			if(current==null)
				current=this.root;
			current.addLast(child);
			//Object[] ancestor = current.getPath4Event();
			TreeModelEvent e = new TreeModelEvent
					(this, parentPath, new int[]{current.getIndex(child)}, new Object[]{child});
			//System.out.println("tree model listener size:___"+this.mlisns.size());
			for (TreeModelListener i : this.mlisns){
				i.treeNodesInserted(e);
			}
		}
	}
	
	public void removeChild(TreePath parentPath){
		//System.out.println("deletion started:___");
		TextNode cund = null,lparent=null;
		//Object[] ancestor =null;
		if(parentPath!=null){
			//ancestor = parentPath.getPath();
			cund=(TextNode)parentPath.getLastPathComponent();
			//System.out.println("deletion target:___"+cund.data);
			int size = parentPath.getPathCount();
			//System.out.println("path size:"+size);
			if(size>=2)
				lparent=(TextNode)parentPath.getPathComponent(parentPath.getPathCount()-2);	
			if(lparent!=null){
				int eindex=lparent.getIndex(cund);
				//System.out.println("deletion index:___"+eindex);
				TreeModelEvent e = new TreeModelEvent
					(this, parentPath.getParentPath(), new int[]{eindex}, new Object[]{cund});
				//System.out.println("tree model listener size:___"+this.mlisns.size());
				for (TreeModelListener i : this.mlisns){
					i.treeNodesRemoved(e);
				}
				//System.out.println("deletion event broadcasting complete.");
				lparent.delete(cund);
				System.out.println("deletion complete:___"+eindex+"__"+cund.data);
			}
		}
	}
//	public void removeChild(XNode current){
//		if(current==null)
//			return;
//		Object[] ancestor = current.getPath4Event();
//		XNode lparent=null;
//		if(ancestor.length>=2)
//			lparent=(XNode)ancestor[ancestor.length-2];
//		if(lparent!=null)
//			lparent.delete(current);
//		TreeModelEvent e = new TreeModelEvent
//				(this, ancestor, new int[]{lparent.getIndex(current)}, new Object[]{current});
//		//System.out.println("tree model listener size:___"+this.mlisns.size());
//		for (TreeModelListener i : this.mlisns){
//			i.treeNodesRemoved(e);
//			
//		}
//	}

}
