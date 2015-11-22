package j40p.infou.platformjmx.convimpl;

import j40p.infou.platform.commmon.convdef.Convertor;
import j40p.infou.util.toolsPack.BeanUtil;
import j40p.infou.util.toolsPack.def.CastableBean;

import java.util.HashMap;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeType;

public class CompositData2CB implements Convertor {
	
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


	protected Class<? extends CastableBean> desttype;
	@Override
	public void setDestType(Object type) {
		this.desttype = (Class<? extends CastableBean>)type;
	}

	
	@Override
	public Object conv(Object value) {
		if(value==null)
			return null;
		CompositeData cpd = (CompositeData)value;
		CastableBean cbo = BeanUtil.instance.get(this.desttype);
		CompositeType cptp = cpd.getCompositeType();
		String[] cpkeys = cptp.keySet().toArray(new String[]{});
		Object[] cpallv = cpd.getAll(cpkeys);
		HashMap<String, Object> vmap = new HashMap<String, Object>();
		int vi =0;
		for(String i: cpkeys){
			vmap.put(i, cpallv[vi++]);
		}
		BeanUtil.instance.$provide(cbo, vmap);
		return cbo;
	}
}
