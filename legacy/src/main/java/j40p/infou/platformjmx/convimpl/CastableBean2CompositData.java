package j40p.infou.platformjmx.convimpl;

import j40p.infou.platform.commmon.convdef.Convertor;
import j40p.infou.util.toolsPack.BeanUtil;
import j40p.infou.util.toolsPack.def.CastableBean;

import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;

public class CastableBean2CompositData implements Convertor {
	
//	private int dx;
//	@Override
//	public void setCorrespondingIndex(int dx) {
//		// TODO Auto-generated method stub
//		this.dx=dx;
//	}
//
//	@Override
//	public int getCorrespondingIndex() {
//		// TODO Auto-generated method stub
//		return this.dx;
//	}


	protected CompositeType desttype;
	@Override
	public void setDestType(Object type) {
		this.desttype = (CompositeType)type;
	}

	
	@Override
	public Object conv(Object value) {
		if(value==null)
			return null;
		CastableBean cb = (CastableBean)value;
		Object retv =null;
		try {
			retv= new CompositeDataSupport( this.desttype, BeanUtil.instance.$keys(cb),  BeanUtil.instance.$values(cb)) ;
		} catch (OpenDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return retv;
	}
}
