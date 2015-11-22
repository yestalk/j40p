package j40p.logu.logutil.log;

public class StackTest {
	public static void main(String[] args) {
		StackTest stt = new StackTest();
		stt.abc();
	}
	
	
	void abc(){
		StackTest.location();
	}
	
	static void location(){
		System.out.println(Thread.currentThread().getStackTrace()[2].toString());
		System.out.println("test.StackTest.abc(StackTest.java:12)");
		System.out.println("com.tibco.as.util.conn.StackTest.abc(StackTest.java:12)");
	}
}
