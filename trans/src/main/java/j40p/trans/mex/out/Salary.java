package j40p.trans.mex.out;

public class Salary implements ValueOutPuter {
	public static final float usdcny=6.14f;
	float amount;
	String tailunit="";
	String munit;
	String annual;
	String origd;
	@Override
	public String out(String lang) {
		String rtv =null;
		String lannaul=this.annual;
		
		if(lang.equals("zh")){
			if(this.origd.indexOf("年")>=0)
				return this.origd.replace("年", "每年");
			else if(this.origd.indexOf("月")>=0 )
				return this.origd.replace("月", "每月");
			
		}else{
			if(lannaul.equals("年")){
				lannaul="year";
			}else if(lannaul.equals("月")){
				lannaul="month";
			}
			if(munit.equals("CNY")){
				
				rtv=Math.round((this.amount/Salary.usdcny)) +this.tailunit+" USD per "+lannaul;
			}else{
				return this.origd;
			}
		}
		return rtv;
	}
	@Override
	public void SetData(String data){
		this.origd=data;
		String[] parts = data.trim().split("\\s+");
		if(parts.length==3){
			String amt = parts[0].trim();
			if(amt.matches("[\\d]+k")){
				this.amount=Float.parseFloat(amt.replace("k", ""));
				this.tailunit="k";
			}else if(data.matches("[\\d]+")){
				this.amount=Float.parseFloat(amt);
			}else
				throw new RuntimeException("wrong formart.");
			this.munit=parts[1].trim();
			this.annual=parts[2].trim();
		}else
			throw new RuntimeException("wrong formart.");

	}

}
