package j40p.http.ins;

import j40p.base.ConfBean;
import j40p.base.cache.ABBBuf;
import j40p.base.cache.IntABuf;
import j40p.base.cache.ObjectCacheX;
import j40p.base.commup.ServerStarter;
import j40p.base.parser.BFHandler;
import j40p.base.parser.IntBuilder;
import j40p.http.impl.HConnHdlFactory;

import java.io.File;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ServerMain {
	public static void main(String[] args) {
		 
		BFHandler.setBFCache(ObjectCacheX.t.lookup(ABBBuf.class, ABBBuf.getCriteriaBy(1402+32,false), 5000));
		 
		IntBuilder.setBFCache(ObjectCacheX.t.lookup(IntABuf.class, IntABuf.getCriteriaByCapacity(4096), 5000));
		
		File rootdir = new File("c://httpinstest");
		FileContent.filec(new File(rootdir,"f.html"));
		ServerStarter sv = new ServerStarter();
		ConfBean svcfb = ConfBean.t.create(ServerStarter.class);
		HConnHdlFactory hcf = new HConnHdlFactory(); 
		ConfBean hcfcfb = ConfBean.t.create(HConnHdlFactory.class);
		ReqObjFactory reqobjf = new ReqObjFactory();
		ConfBean reqobjfb = ConfBean.t.create(ReqObjFactory.class);
		reqobjfb.s(ReqObjFactory.WorkingDir, rootdir);
		reqobjf.config(reqobjfb);
		hcfcfb.s(HConnHdlFactory.eventFactory, reqobjf);
		hcfcfb.s(HConnHdlFactory.exer,  new ThreadPoolExecutor(20, 500, 60, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>()));
		hcf.config(hcfcfb);
		svcfb.s(ServerStarter.Port, 80);
		svcfb.s(ServerStarter.ConnFactory, hcf);
		sv.config(svcfb);
		sv.start();
	}
}
