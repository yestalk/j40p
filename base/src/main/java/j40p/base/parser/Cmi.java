package j40p.base.parser;

import java.nio.ByteBuffer;

public class Cmi extends CaptureManager {
	
	public void restore() {
		if (this.trl.size() > 0 || this.eslplst.size() > 0){
			System.out.println("this.trl tk cap"+this.trl.getFirst().capacity());
			System.out.println("this.trl.size()"+this.trl.size());
			System.out.println("this.eslplst.size()"+this.eslplst.size());
			throw new RuntimeException("capture state not been conclued. by scanner");
		}
		this.cutrunk = null;
		this.limt = -1;
	}
	
	public void release() {
		System.out.println("zero read midle cap release");
		this.cutrunk = null;
		this.trl.removeLast();
		if(this.trl.size()==0)
			throw new RuntimeException("i don't understand this situation.");
	 
	}

	public void setCurrentBuffer(ByteBuffer bf) {
		if (this.cutrunk != bf) {
			this.cutrunk = bf;
			if (this.trl.size() > 0) {
				this.trl.add(bf);
				// System.out.println("!!!!!!!!!!!!!!!!!!setCurrentBuffer");
			}
		}

	}

	public void capCondition(int[] cond) { // -1,0,1 non_cp, init_cp, cp
		int tksz = this.trl.size();

		switch (tksz) {
			case 0:
				cond[CaptureManager.label_cp_state] = CaptureManager.Cmd_non_capture;
				break;
			case 1:
				cond[CaptureManager.label_cp_state] = CaptureManager.Cmd_init_capture;
				cond[CaptureManager.label_cp_start] = this.start;
				cond[CaptureManager.label_cp_length] = this.length;
				break;
			default:
				cond[CaptureManager.label_cp_state] = CaptureManager.Cmd_capture;
				break;
		}

	}

	public void adjust() {
		// System.out.println("cmi adjust before:__"+this.start);
		if(this.espoint!=-1)
			this.espoint-=this.start;
		this.start = 0;

	}

}
