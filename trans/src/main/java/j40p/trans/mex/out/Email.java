package j40p.trans.mex.out;

public class Email implements ValueOutPuter {

	public static final Email i=new Email();

	String odata;
	@Override
	public String out(String lang) {
		// TODO Auto-generated method stub
		return this.odata;
	}
	@Override
	public void SetData(String data){
		this.odata=data;
	}

}
