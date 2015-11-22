package j40p.treestru.gui;

import j40p.treestru.TextNode;

import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.DropMode;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;



public class DynamicTree extends JPanel {
    //protected DefaultMutableTreeNode rootNode;
    protected JTreeModel treeModel;
    protected JTree tree;
    private Toolkit toolkit = Toolkit.getDefaultToolkit();

    public DynamicTree() {
        super(new GridLayout(1,0));
        
        //rootNode = new DefaultMutableTreeNode("Root Node");
        treeModel = new JTreeModel();
        //treeModel.addTreeModelListener(new MyTreeModelListener());
        tree = new JTree(treeModel);
        // tree.setCellEditor(new JtreeED());
        tree.setCellRenderer(new JtreeRDer());
        tree.setRootVisible(true);
        tree.setDragEnabled(true);
        tree.setDropMode(DropMode.ON_OR_INSERT);
        tree.setTransferHandler(new TreeTransH());
        tree.setEditable(true);
        tree.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);

        JScrollPane scrollPane = new JScrollPane(tree);
        add(scrollPane);
    }


    
	public void addNodeAction(String msg){
		TreePath parentPath = this.tree.getSelectionPath();
		TextNode newNode = new TextNode(msg);
		this.treeModel.addChild(parentPath, newNode);
		
		tree.scrollPathToVisible(new TreePath(newNode.getPath().toArray()));
	}
	
	public void removeCurrentNode(){
		//TextNode cund = null,lparent=null;
		TreePath parentPath = this.tree.getSelectionPath();
		this.treeModel.removeChild(parentPath);		
	}

}