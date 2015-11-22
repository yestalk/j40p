package j40p.http.impl;

import j40p.base.ConfBean;
import j40p.base.Configurable;
import j40p.base.TypeUtil;
import j40p.base.commup.SkeyHdl;
import j40p.base.commup.def.SelectionEventProcessor;
import j40p.base.l.Res;
import j40p.base.parser.BFHandler;
import j40p.http.iface.HttpEvent;

import java.util.concurrent.Executor;

public class HConnHdlFactory implements SelectionEventProcessor.Factory, Configurable {

	public static final Res<Executor> exer = TypeUtil.i.defResPoint(HConnHdlFactory.exer);
	public static final Res<HttpEvent.Factory> eventFactory = TypeUtil.i.defResPoint(HConnHdlFactory.eventFactory);

	HttpEvent.Factory reqf;

	@Override
	public void config(ConfBean current) {
		this.reqf = current.g(HConnHdlFactory.eventFactory);
		SkeyHdl.setExceutor(current.g(HConnHdlFactory.exer));

	}

	@Override
	public SelectionEventProcessor createNew() {
		//System.out.println("here?");
		SkeyHdl keydialer = new SkeyHdl();
		BFHandler bufferhdler = new BFHandler();
		HttpHeaderScaner msgscanner = new HttpHeaderScaner();
		TokenCenter heventbuilder = new TokenCenter();

		heventbuilder.setHttpEventFactory(this.reqf);
		 

		msgscanner.setEBuilder(heventbuilder);
		bufferhdler.setScanner(msgscanner);
		keydialer.setInteractiveContext(heventbuilder);
		keydialer.setBFController(bufferhdler);
		return keydialer;
	}

}
