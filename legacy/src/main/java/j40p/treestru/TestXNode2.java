package j40p.treestru;

import java.util.Random;

public class TestXNode2 {
public static void main(String[] args) {
		
		TextNode tln = new TextNode();
		TextNode nd2 ; 
		for(int i =0;i<20000;i++){
			
			nd2 = new TextNode(""+i);
			tln.addLast(nd2);
		}
		System.out.println("sz:___"+tln.getChildrenSize());

		TextNode getnd,insertnd;
		Random rdm = new Random();

		int cto = 0;
		while(true){
			int ct=0;
			while(ct<1000){
				int getp = rdm.nextInt(19999);
				int insertp = rdm.nextInt(19999);
				while(getp==insertp){
					insertp = rdm.nextInt(19999);
				}
				getnd = (TextNode)tln.getNodeByIndex(getp);
				insertnd = (TextNode)tln.getNodeByIndex(insertp);
				tln.delete(getnd);
				if(getp % 2==0)
					tln.addAfter(insertnd, getnd);
				else
					tln.addBefore(insertnd, getnd);
				
				//System.out.println("getp:___"+getp);
				//System.out.println("insertp:___"+insertp);
				ct++;
				
			}
			
			//System.out.println("stage1 over");
			for(int i =0,k=6;i<2000;i++){
				getnd = (TextNode)tln.getNodeByIndex(k);
				tln.delete(getnd);
			}
			//System.out.println("stage2 over");
			for(int i =0;i<2000;i++){
				
				nd2 = new TextNode(""+i);
				tln.addFirst(nd2);
			}
			//System.out.println("stage3 over");
			System.out.println((cto++));
			System.out.println(tln.getChildrenSize());
		}

		
	}

}
