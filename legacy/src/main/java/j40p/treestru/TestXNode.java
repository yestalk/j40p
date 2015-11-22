package j40p.treestru;

public class TestXNode {
public static void main(String[] args) {
		
		TextNode tln = new TextNode();
	
		for(int i =0;i<20000;i++){
			
			TextNode nd2 = new TextNode(""+i);
			tln.addLast(nd2);
		}
		System.out.println("sz:___"+tln.getChildrenSize());


		for(int i =0,sz=tln.getChildrenSize()-1;i<=sz;i++){
			System.out.print(((TextNode)tln.getNodeByIndex(i)).data+",");
		}
		
		
		System.out.println();
		
		
		TextNode snd2;
		
		TextNode snd = (TextNode)tln.getNodeByIndex(37);
		snd2 = (TextNode)tln.getNodeByIndex(21);

		tln.delete(snd2);
		tln.addAfter(snd, snd2);
		
		snd = (TextNode)tln.getNodeByIndex(69);
		snd2 =(TextNode)tln.getNodeByIndex(11);
		tln.delete(snd2);
		tln.addBefore(snd, snd2);
		
		snd = (TextNode)tln.getNodeByIndex(70);
		snd2 = (TextNode)tln.getNodeByIndex(1);
		tln.delete(snd2);
		tln.addBefore(snd, snd2);
		
		System.out.println("size after switch inertsion:__"+tln.getChildrenSize());
		for(int i =0,x=100,sz=5000;i<sz;i++){
			snd2 = (TextNode)tln.getNodeByIndex(x);
			if(snd2!=null){
				//System.out.println("going to delete:"+snd2.data);
				tln.delete(snd2);
				//System.out.println("size after 1 deletion:"+tln.getChildrenSize());
				//System.out.println("---------");
			}else{
				System.out.println(i+": index null?");
				continue;
			}
		}
		System.out.println();
		System.out.println("size after range deletion:__"+tln.getChildrenSize());
		
		TextNode it = (TextNode)tln.getNodeByIndex(0);
		//System.out.println(it.nextSibling().data);
		while(it!=null){
			System.out.println(((TextNode)it).data);
			it=(TextNode)it.nextSibling();
		}
//		for(int i =0,sz=tln.getChildrenSize()-1;i<=sz;i++){
//			snd2 = tln.getNodeByIndex(i);
//			if(snd2!=null)
//				System.out.print(i+": "+snd2.data+",\r\n");
//			else
//				System.out.println(i+": index null?");
//		}
		System.out.println("sz:___"+tln.getChildrenSize());
	}

}
