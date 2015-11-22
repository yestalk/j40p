package j40p.infou.platform.commmon;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class DelayedTask implements Runnable {
	protected abstract void doTask();
	protected abstract boolean needflush();
	private volatile long last;
	protected volatile long first=-1;
	//private long pre;
	private volatile Thread td;
	private AtomicBoolean running= new AtomicBoolean(false);
	public void reScheduleMission(){
		if(this.first==-1){
			this.first=System.currentTimeMillis();
			this.last=this.first;
		}
		this.last=System.currentTimeMillis();
		
		if(!(td!=null && this.running.get())){
			this.running.set(true);
			this.td=new Thread(this);
			this.td.start();
		}
		
			
	}
	final int pthreshold=2000;
	public void run(){
		int period=pthreshold+1;
		while(true){
			try {
				Thread.sleep(period);
			} catch (InterruptedException e) {
				break;
			}
			period = (int)(System.currentTimeMillis()-this.last);
			if(period>=this.pthreshold || this.needflush()){
				long llast = this.last;
				this.first=-1;
				this.doTask();
				
				if(llast!=this.last){
					period=this.pthreshold+1;
					continue;
				}else{
					this.running.set(false);
					return;
				}
			}else
				period=(period<0)?this.pthreshold:period+1000;
		}
	}
}
