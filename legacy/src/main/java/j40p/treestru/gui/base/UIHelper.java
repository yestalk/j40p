package j40p.treestru.gui.base;

import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public interface UIHelper {
	UIHelper i = new  Default();
	void setLookAndFeelByName(String name);
	
	
	
	
	
	class Default implements UIHelper { private Default(){};
		//Nimbus
		public void setLookAndFeelByName(String name){
			try {
				LookAndFeelInfo[] lafinfos = UIManager.getInstalledLookAndFeels();
				//System.out.println("look and feel length:____"+lafinfos.length);
			    for (LookAndFeelInfo info : lafinfos) {
			    	System.out.println("LookAnddFeelName:___"+info.getName());
			    	if (name.equals(info.getName())) {
			        	
			           UIManager.setLookAndFeel(info.getClassName());
			            //LookAndFeel laf = UIManager.getLookAndFeel();
			            //boolean lafsupport = laf.getSupportsWindowDecorations();
			           // String lafn = laf.getName();
			           // System.out.println(lafn+" windowDecorations:___"+lafsupport);
			           break;
			        }
			    }
			} catch (Exception e) {
				// if not available, use the default.
			    // If Nimbus is not available, you can set the GUI to another look and feel.
				// throw new RuntimeException(e);
			}
		}
		
	}
}
