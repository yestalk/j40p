package j40p.trans.mex.out;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class Birthday implements ValueOutPuter {
	public final static SimpleDateFormat sp = new SimpleDateFormat("yyyy.MM.dd"); 
	public static final Birthday i=new Birthday();
	String odata;
	int age;
	@Override
	public String out(String lang) {
		String out=null;
		if(lang.equals("zh"))
			out=this.odata+"("+this.age+" 周岁)";
		else
			out=this.odata+"("+this.age+" years old)";
		return out;
	}
	@Override
	public void SetData(String data){
		//this.odata=data;
		
		Date bthd,now;
		try {
			bthd = Birthday.sp.parse(data);
			now = new Date();
			this.age = (int)Duration.between(bthd.toInstant(), now.toInstant()).toDays()/365;
			
			this.odata=data;
			
		}catch(ParseException e){
			throw new RuntimeException(e);
		}
	}
}
