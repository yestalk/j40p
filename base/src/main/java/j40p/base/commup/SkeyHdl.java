package j40p.base.commup;

import j40p.base.commup.def.InteractiveContext;
import j40p.base.commup.def.OutPutController;
import j40p.base.commup.def.SelectionEventProcessor;
import j40p.base.parser.def.BFController;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.ReentrantLock;

public class SkeyHdl implements OutPutController, SelectionEventProcessor {

	private static final ArrayList<ByteBuffer> terminator = new ArrayList<ByteBuffer>(0);
	private static final ByteBuffer[] thebfarray = new ByteBuffer[0];

	private static Executor tex;
	private InteractiveContext cx;
	private BFController scc;

	private ConcurrentLinkedQueue<ArrayList<ByteBuffer>> opuq = new ConcurrentLinkedQueue<ArrayList<ByteBuffer>>();
	private ReentrantLock wlock = new ReentrantLock();

	private Writer writer = new Writer();
	private Reader parser = new Reader();

	private SelectionKey sk;

	public static void setExceutor(Executor ex) {
		SkeyHdl.tex = ex;
	}

	public void setInteractiveContext(InteractiveContext cx) {
		this.cx = cx;
		cx.setOutPutController(this);

	}

	public void setBFController(BFController bfctrl) {
		this.scc = bfctrl;
	}

	@Override
	public int interested() {
		if (this.opuq.peek() != null)
			return SelectionKey.OP_WRITE | SelectionKey.OP_READ;
		else
			return SelectionKey.OP_READ;

	}

	@Override
	public void bind(SelectionKey sk) {
		this.sk = sk;
	}

	@Override
	public void put(ArrayList<ByteBuffer> bflist) {
		
		//System.out.println("here? skeyhdl write");
		this.opuq.offer(bflist);
		SkeyHdl.tex.execute(this.writer);

	}

	@Override
	public void putAndFinish(ArrayList<ByteBuffer> bflist) {

		this.opuq.offer(bflist);
		this.opuq.offer(SkeyHdl.terminator);
		this.shutdownInput();
		SkeyHdl.tex.execute(this.writer);
	}

	@Override
	public void action() {
		 
		SelectionKey lsk = this.sk;
		//System.out.println("here whatever action for:__"+lsk);
		int introps = lsk.interestOps();
		int rdops = lsk.readyOps();
		lsk.interestOps(introps & ~rdops);
		
		if (lsk.isValid()) {
			if ((introps & SelectionKey.OP_WRITE) != 0 && (rdops & SelectionKey.OP_WRITE )!= 0) {
				SkeyHdl.tex.execute(this.writer);
			}
			if ((rdops & SelectionKey.OP_READ )!= 0) {
				 
				//System.out.println("here read action for:__"+lsk);
				SkeyHdl.tex.execute(this.parser);
			}
		} else {
			System.out.println("action invailded.");
			this.finishConn();
		}
		
	}

	private void finishConn() {
		System.out.println("connection finished.");
		this.opuq.clear();
		this.sk.cancel();
		try {
			this.sk.channel().close();
		} catch (IOException e) { }
		

	}
	
	private void shutdownInput(){
		try {
			((SocketChannel) this.sk.channel()).shutdownInput();
		} catch (IOException e) { }
	}

	void write(GatheringByteChannel wbc, SelectionKey lsk) {
		//System.out.println("here? skeyhdl write");
		ConcurrentLinkedQueue<ArrayList<ByteBuffer>> lopuqq = this.opuq;
		ArrayList<ByteBuffer> bfalst;
		ByteBuffer[] lbf;
		//int introps = lsk.interestOps();
		int bflstlen = -1;
		while ((bfalst = lopuqq.peek()) != null) {
			if (bfalst == SkeyHdl.terminator) {
				this.finishConn();
				return;
			} else if ((bflstlen = bfalst.size()) == 0) {
				continue;
			} else {
				bflstlen -= 1;
				lbf = bfalst.toArray(SkeyHdl.thebfarray);
//				for(ByteBuffer i : lbf){
//					System.out.println(i);
//				}
				while (lbf[bflstlen].hasRemaining()) {
					try {
						switch ((int) wbc.write(lbf)) {
							case 0:
								lsk.interestOps(lsk.interestOps() | SelectionKey.OP_WRITE);
								lsk.selector().wakeup();
								return;
							 
						}
					} catch (IOException e) {
						this.finishConn();
						return;
					}
				}
				lopuqq.poll();
			}
		}
		lsk.interestOps(lsk.interestOps() & ~SelectionKey.OP_WRITE);
		//lsk.selector().wakeup();
		//System.out.println("write sealed:_"+((lsk.interestOps() & SelectionKey.OP_WRITE)==0));
		
		
	}

	class Reader implements Runnable {

		@Override
		public void run() {
			//System.out.println("skeyhdl read here");
			SelectionKey lsk = SkeyHdl.this.sk;
			ReadableByteChannel fch = (ReadableByteChannel) SkeyHdl.this.sk.channel();
			int lastread = 0;
			ByteBuffer inbuffer = null;
			try {
				while ((inbuffer = SkeyHdl.this.scc.intaking(inbuffer, lastread)) != null) {
					lastread = fch.read(inbuffer);
//					while (inbuffer.hasRemaining()) {			 
//						if ((lastread = fch.read(inbuffer)) <= 0){					 
//							break;
//						}			 
//					}
				}
				if(lastread==-1)
					throw new RuntimeException();
				lsk.interestOps(lsk.interestOps() | SelectionKey.OP_READ);
				lsk.selector().wakeup();
			} catch ( Exception    e) {
				e.printStackTrace();  
				SkeyHdl.this.opuq.add(SkeyHdl.terminator);
				SkeyHdl.tex.execute(SkeyHdl.this.writer);
				SkeyHdl.this.shutdownInput();
				return;
			}
 
			
		
			//System.out.println("here? reinteresting in op read for:_"+lsk);
		 
		}
	}

	
	
	class Writer implements Runnable {
		@Override
		public void run() {
			SelectionKey lsk = SkeyHdl.this.sk;
			if (lsk == null)
				return;
			if (!SkeyHdl.this.wlock.tryLock())
				return;
			try {
				SkeyHdl.this.write((GatheringByteChannel) lsk.channel(), lsk);
			} finally {
				SkeyHdl.this.wlock.unlock();
			}

		}
	}
}
