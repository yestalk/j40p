package j40p.base.commup;

import j40p.base.ConfBean;
import j40p.base.Configurable;
import j40p.base.TypeUtil;
import j40p.base.commup.def.SelectionEventProcessor;
import j40p.base.l.Res;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class ServerStarter extends Thread implements Configurable {

	public static final Res<Integer> Port = TypeUtil.i.defResPoint(ServerStarter.Port);
	public static final Res<SelectionEventProcessor.Factory> ConnFactory = TypeUtil.i.defResPoint(ServerStarter.ConnFactory);

	// public static final DRes<SelectionEventProcessor> connectionHdl =
	// TypeUtil.i.defDResPoint(ServerStarter.connectionHdl);

	int port;
	SelectionEventProcessor.Factory factory;

	@Override
	public void config(ConfBean conf) {

		this.factory = conf.g(ServerStarter.ConnFactory);
		this.port = conf.g(ServerStarter.Port);

	}

	@Override
	public void run() {
		int port = this.port;
		try {
			Selector lsl = Selector.open();
			InetAddress ind = InetAddress.getLocalHost();
			System.out.println(ind);

			ServerSocketChannel schannel = ServerSocketChannel.open();
			InetSocketAddress isa = new InetSocketAddress(ind, port);
			schannel.bind(isa);

			schannel.configureBlocking(false);

			

			schannel.register(lsl, SelectionKey.OP_ACCEPT );
			
			for (;;) {
				if (lsl.select() == 0){
//					for(SelectionKey i : lsl.keys()){
//						System.out.println(i.toString()+"__"+((i.interestOps()& SelectionKey.OP_READ)!=0));
//					}
//					System.out.println("----cut-----");
					continue;
				}

				// String cut = Thread.currentThread().toString();
				Set<SelectionKey> selectedKeys = lsl.selectedKeys();
				//System.out.println("here select:_" + selectedKeys.size());
				Iterator<SelectionKey> it = selectedKeys.iterator();

				while (it.hasNext()) {

					SelectionKey key = it.next();
					it.remove();
					Object keat = key.attachment();

					if (key.isAcceptable()) {
						//System.out.println("still accepting:_"+((key.interestOps() & SelectionKey.OP_ACCEPT)!=0));
						ServerSocketChannel ssch = (ServerSocketChannel) key.channel();
						SocketChannel clch = ssch.accept();

						// System.out.println(" accept here?");
						if (clch != null) {
							clch.configureBlocking(false);
							SelectionEventProcessor chll = this.factory.createNew();
							key = clch.register(lsl, chll.interested(), chll);
							chll.bind(key);

						}
						
						//key.interestOps(key.interestOps() | SelectionKey.OP_ACCEPT);

					} else if (keat instanceof SelectionEventProcessor) {

						((SelectionEventProcessor) keat).action();

					}
				}
				//lsl.wakeup();
				// System.out.println(cut + "  :go back");

			}

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

}