package j40p.treestru.gui;

import j40p.treestru.TextNode;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;

public class JtreeRDer extends JComponent  implements TreeCellRenderer {

	@Override
	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
		
		//System.out.println("cell row:"+row);
		
		

		this.tree=tree;
		this.value=value;
		this.row=row;
		this.selected=selected;
		this.leaf=leaf;
		this.hasFocus=hasFocus;
		return this;
		
	}
	public JtreeRDer() {
		this.setBackground(Color.BLACK);
		this.setForeground(Color.WHITE);
		this.setVisible(true);
		//this.addMouseListener(this);
		
		//this.setd

	}
	boolean needraw;
	boolean selected;
	boolean leaf;
	boolean hasFocus;
	int row;
	Object value;
	JTree tree;
	@Override
	protected void paintComponent(Graphics g) {

		//super.paintComponent(g);
		//System.out.println(g.getClip());
		//System.out.println("here? paint");
		//Rectangle rct = this.getBounds();
		if(this.row % 2 ==0)
			g.setColor(this.getBackground());
		else
			g.setColor(Color.BLUE);
		g.fillRect(0,0,this.getWidth(),this.getHeight());
		TextNode tnd = (TextNode)value;
		g.setColor(this.getForeground());
		g.drawString(tnd.data, 20, 15);
		g.dispose();
		System.out.println("pp:"+value);
	}
	
	@Override
	public Dimension preferredSize() {
		// TODO Auto-generated method stub
		//System.out.println("here? preferedSize");
		//System.out.println("width:___"+);
		int ht;
//		System.out.println( "p row:____"+this.roww);
//		if(this.roww % 2 ==0)
//			ht=80;
//		else
//			ht=30;
		return new Dimension(500,40);
	}



	

}
