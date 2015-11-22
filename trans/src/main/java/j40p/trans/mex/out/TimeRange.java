package j40p.trans.mex.out;

import j40p.trans.hdl.insd.CompoundedEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class TimeRange implements ValueOutPuter {
	public final static SimpleDateFormat sp = Birthday.sp;
	//String odata;
	//int totalmonth;
	public Date startd ;
	public Date endd ;
	public CompoundedEntry container;
	@Override
	public String out(String lang) {
		String out =null;
		//if(this.odata==null){
			out=this.sp.format(this.startd)+"/"+this.sp.format(this.endd)+this.ftxt(this.startd, this.endd,lang);
		//}
		return out;
	}
	@Override
	public void SetData(String data){
		String[] timeds = data.split("/");
		if(timeds.length==2){
			try {
				Date start = TimeRange.sp.parse(timeds[0]);
				Date end = TimeRange.sp.parse(timeds[1]);
				//this.odata=data+ftxt(start, end);
				//this.totalmonth=tmonths;
				this.startd=start;
				this.endd=end;
			}catch(ParseException e){
				throw new RuntimeException(e);
			}
		}else
			throw new RuntimeException("wrong time range format.");
	}
	private String ftxt( Date start, Date end,String lang) {
		String rtv=null;
		int days = (int)Duration.between(start.toInstant(), end.toInstant()).toDays();
		int tyears = days/356;
		int tmonths = days/30;
		int rmonths =(tyears==0)?tmonths:(tmonths%12);
		if(tyears==0)
			if(lang.equals("zh"))
				rtv="_|_("+rmonths+" 个月)";
			else
				rtv="_|_("+rmonths+" months)";
		else if(rmonths==0){		
			if(lang.equals("zh"))
				rtv="_|_("+tyears+" 年)";
			else
				rtv="_|_("+tyears+" year)";
		}else{
			if(lang.equals("zh"))
				rtv="_|_("+tyears+" 年 "+rmonths+" 个月)";
			else
				rtv="_|_("+tyears+" year "+rmonths+" months)";
		}
		return rtv;
	}
	
	public int getGap(TimeRange after){
		int days = (int)Duration.between(this.endd.toInstant(), after.startd.toInstant()).toDays();
		int tmonths = days/30;
		return tmonths;
	}
}
