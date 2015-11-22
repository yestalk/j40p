package j40p.infou.platform.commmon.covimpl;

import j40p.infou.platform.commmon.convdef.Convertor;

public class String2Enum implements Convertor {
	
//	private int dx;
//	@Override
//	public void setCorrespondingIndex(int dx) {
//		
//		this.dx=dx;
//	}
//
//	@Override
//	public int getCorrespondingIndex() {
//		
//		return this.dx;
//	}


	protected Class<? extends Enum> desttype;
	@Override
	public void setDestType(Object type) {
		this.desttype = (Class<? extends Enum>)type;
	}

	
	@Override
	public Object conv(Object value) {
		if(value==null)
			return null;
		String cpd = (String)value;
		Enum<?> enins = Enum.valueOf(this.desttype, cpd);
		return enins;
	}
}
