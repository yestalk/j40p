package j40p.infou.platformjmx.convimpl;

import j40p.infou.platform.commmon.convdef.Convertor;
import j40p.infou.util.toolsPack.BeanUtil;
import j40p.infou.util.toolsPack.def.CastableBean;

import java.util.List;

import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.TabularDataSupport;
import javax.management.openmbean.TabularType;

public class BeanList2TabularData implements Convertor{
	
//	int dx;
//	@Override
//	public void setCorrespondingIndex(int dx) {
//		this.dx=dx;
//		
//	}
//
//	@Override
//	public int getCorrespondingIndex() {
//		
//		return dx;
//	}
	TabularType desttype;
	@Override
	public void setDestType(Object type) {
		this.desttype=(TabularType)type;
		
	}
	
	@Override
	public Object conv(Object value) {
		//System.out.println("BeanList2Tabular convertor here");
		//System.out.println("BeanList2Tabular convertor value:_"+value);
		if(value==null)
			return null;
		//System.out.println("BeanList2Tabular convertor value not null");
		List<?> vl = (List<?>)value;
		//System.out.println("BeanList2Tabular convertor list size:_"+vl.size());
		TabularDataSupport rv = new TabularDataSupport(this.desttype); 
		CompositeType ctyp = this.desttype.getRowType();
		for(Object i: vl){
			CastableBean cb = (CastableBean)i;
			CompositeDataSupport cpsp=null;
			try {
				cpsp = new CompositeDataSupport( ctyp, BeanUtil.instance.$keys(cb),  BeanUtil.instance.$values(cb));
			} catch (OpenDataException e) {
				throw new RuntimeException(e);
			}
			rv.put(cpsp);
		}
		//System.out.println("BeanList2Tabular convertor:_"+rv.size());
		return rv;
	}
}
