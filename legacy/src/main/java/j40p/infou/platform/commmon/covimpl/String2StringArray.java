package j40p.infou.platform.commmon.covimpl;

import j40p.infou.platform.commmon.convdef.Convertor;

import java.util.LinkedList;

public class String2StringArray  implements Convertor  {


	@Override
	public Object conv(Object value) {
		String sd = (String)value;
		String[] sl = sd.split(",");
		LinkedList<String> sl2 =new LinkedList<String>();
		

		for(int i=0,sll=sl.length-1;i<=sll;i++){
			String stri = sl[i];
			if(stri.equals("") || stri.matches("\\s+")){
				continue;
			}else{
				sl2.add(stri);
			}
		}
		return sl2.toArray(new String[sl2.size()]);
	}

	@Override
	public void setDestType(Object type) {
		// TODO Auto-generated method stub

	}

}
