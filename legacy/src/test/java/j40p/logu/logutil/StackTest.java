package j40p.logu.logutil;

public class StackTest {
	public static void main(String[] args) {
		System.out.println("dfdf".startsWith(""));
		StackTest stt = new StackTest();
		stt.abc();
	}
	
	
	void abc(){
		StackTest.location();
		//here
	}
	
	static void location(){
		System.out.println(Thread.currentThread().getStackTrace()[1].toString());
	}
}
