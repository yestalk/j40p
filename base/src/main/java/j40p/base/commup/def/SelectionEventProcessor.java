package j40p.base.commup.def;

import j40p.base.ObjFactory;

import java.nio.channels.SelectionKey;

public interface SelectionEventProcessor {
	//public static final DRes<ContentProvider> ContentProvider = TypeUtil.i.defDResPoint(SkeyHdl.ContentProvider);
	
	interface Factory extends ObjFactory<SelectionEventProcessor>{}
	void setInteractiveContext(InteractiveContext cx);
	void action();
	void bind(SelectionKey sk);
	int interested();
}
