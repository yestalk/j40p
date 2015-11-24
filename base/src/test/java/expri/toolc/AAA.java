package expri.toolc;

import java.util.Iterator;

public class AAA    {
	enum X  implements Iterable<String>{
		x1,
		x2,
		x3;
		public static final String z="z";
		@Override
		public Iterator<String> iterator() {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	static final X ax1=X.x1;
	static final X ax2=X.x2;
	void swt(int xi){{
//		 
//				switch(xi){
//					case AAA.ax2:
//						
//						
//				}
	}
		
	}
	void theloop() throws InterruptedException{
		
		while(true){
			System.out.println("l1");
			Thread.sleep(1000);
			System.out.println("l2");
			Thread.sleep(1000);
			System.out.println("l3");
			Thread.sleep(1000);
			System.out.println("l4");
			Thread.sleep(1000);
		}
	}
	public static void main(String[] args) {
		try {
			new AAA().theloop();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
