package j40p.http.ins;

import j40p.base.ConfBean;
import j40p.base.Configurable;
import j40p.base.TypeUtil;
import j40p.base.l.Res;
import j40p.http.iface.HttpEvent;

import java.io.File;


public  class ReqObjFactory implements HttpEvent.Factory,  Configurable{
	public static final Res<File> WorkingDir = TypeUtil.i.defResPoint(ReqObjFactory.WorkingDir);
	//private static File wkdir;
	@Override
	public HttpEvent createNew() {
		//HttpMsgCenter hmc = new HttpMsgCenter();
		//hmc.setWorkingDir(ReqObjFactory.wkdir);
		return new HttpMsgCenter();
	}

	@Override
	public void config(ConfBean current) {
		HttpMsgCenter.setWorkingDir(current.g(ReqObjFactory.WorkingDir));
	}
 
}
