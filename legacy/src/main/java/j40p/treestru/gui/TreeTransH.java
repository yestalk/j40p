package j40p.treestru.gui;

import j40p.treestru.XNode;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.TreePath;

public class TreeTransH extends TransferHandler {

//	@Override
//	public boolean canImport(JComponent arg0, DataFlavor[] arg1) {
//		System.out.println(arg0);
//		return super.canImport(arg0, arg1);
//	}

	@Override
	public boolean canImport(TransferSupport support) {
		//System.out.println("!!!!!!!canImport______"+support);
        if(!support.isDrop()) {  
            return false;  
        } 
        //System.out.println();
        JTree.DropLocation dl =(JTree.DropLocation)support.getDropLocation(); 
        TreePath path = dl.getPath();
        Object dtarget = path.getLastPathComponent();
		if(dtarget==this.subject)
			return false;
        
        //support.setShowDropLocation(true);  
		return true;
	}
	Object subject;
	@Override
	protected Transferable createTransferable(JComponent c) {
		System.out.println("!!!!!!!createTransferable______"+c);
		JTree tree = (JTree)c;  
        TreePath path = tree.getSelectionPath(); 
        Object cucmp = path.getLastPathComponent();
        this.subject=cucmp;
		return new NodesTransferable((XNode)cucmp);
	}

//	@Override
//	public void exportAsDrag(JComponent arg0, InputEvent arg1, int arg2) {
//		System.out.println("!!!!!!!create______"+arg0);
//		super.exportAsDrag(arg0, arg1, arg2);
//	}

	@Override
	protected void exportDone(JComponent source, Transferable data, int action) {
		//System.out.println("!!!!!!!exportDone______"+source);
		//super.exportDone(source, data, action);
	}

//	@Override
//	public void exportToClipboard(JComponent arg0, Clipboard arg1, int arg2)
//		throws IllegalStateException {
//		System.out.println("!!!!!!!exportToClipboard______"+arg0);
//		super.exportToClipboard(arg0, arg1, arg2);
//	}

	@Override
	public boolean importData(TransferSupport support) {
		//System.out.println("!!!!!!!importData___"+support);
        if(!this.canImport(support)) {  
            return false;  
        }  
        JTree.DropLocation dl = (JTree.DropLocation)support.getDropLocation();  
        int childIndex = dl.getChildIndex();  
        System.out.println("!!!!!!!importData___"+childIndex);
		return super.importData(support);
	}
	
    @Override
    public int getSourceActions(JComponent c) {  
        return TransferHandler.MOVE;  
    }  
    
    public class NodesTransferable implements Transferable {  
        XNode node;  
   
        public NodesTransferable(XNode node) {  
            this.node = node;  
         }  
   
        public Object getTransferData(DataFlavor flavor)  throws UnsupportedFlavorException {  
//            if(!isDataFlavorSupported(flavor))  
//                throw new UnsupportedFlavorException(flavor);  
            return node;  
        }  
   
        public DataFlavor[] getTransferDataFlavors() {  
            return null;  
        }  
   
        public boolean isDataFlavorSupported(DataFlavor flavor) {  
            return true;  
        }  
    }  
}
