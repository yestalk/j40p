package j40p.base.parser;

import j40p.base.cache.ABBBuf;
import j40p.base.parser.def.BFController;
import j40p.base.parser.def.Scanner;
import j40p.base.parser.def.Scanner.ParamsReg;
import j40p.base.parser.def.ex.ScanningLimitException;

import java.nio.ByteBuffer;

public class BFHandler implements BFController {
	private static ABBBuf thebfcache;
	public static void setBFCache(ABBBuf abbf){
		BFHandler.thebfcache=abbf;
		CaptureManager.setBFCache(abbf);
		
	}
	// public static final L<OutPutController> FOutPutC =
	// TypeUtil.i.def(ChannelHandler.FOutPutC);

	// private static ByteBuffer neilbf = ByteBuffer.allocate(0);
	private Scanner sc;
	// private OutPutController opc;

	private long limit = -1;
	private long limitcount = 0;
	private int limitType = -1;

	private Cmi cpm = new Cmi();
	private ByteBuffer lastinbf;
	private ParamsReg scparam = new ParamsReg();
	private int[] regs = new int[CaptureManager.regSize];

	public void setScanner(Scanner sc) {
		this.sc = sc;
		this.sc.init(this.scparam);
		this.limit = this.scparam.limitLength;
		this.limitType = this.scparam.limitType;

	}

	@Override
	public ByteBuffer intaking(ByteBuffer buf, int lastred) {
		Cmi lcm = this.cpm;
		if (buf == null) {
			if (this.lastinbf == null) {
				buf = BFHandler.thebfcache.get();
				buf.mark();
				lcm.setCurrentBuffer(buf);
				//System.out.println("intaking assign bf and mark.");
				//System.out.println("buf.position():__" + buf.position());
				return this.lastinbf = buf;
			} else {
				System.out.println("intaking using marked bf."+this.lastinbf);
				System.out.println("intaking buf."+buf);
				return this.lastinbf;
			}
		} else if (this.lastinbf == null) {
			//System.out.println("ever happen? intaking");
			lcm.setCurrentBuffer(buf);
		}
		//System.out.println("here go on");
		//System.out.println("bufcap"+buf.capacity());
		int[] lreg = this.regs;

		ParamsReg lscparam = this.scparam;

		int inputposition = buf.position(), bufsize = buf.capacity(), occupiedratio, inend, start, end, supposedEnd = -1, rdif, lastscannedpoint;
		long llcount = this.limitcount;
		boolean slot_residue = inputposition < bufsize;
		boolean secbreak = false;
		boolean goonscan = lastred > 0;
		long lclimit = this.limit;
		Scanner cuscanner = this.sc;
		//System.out.println("last readed:_"+lastred);
		//System.out.println("end p:_"+inputposition);
		// lbf.limit(inputpo);
		// System.out.println(buf.);
		buf.reset();
		start = buf.position();
		//System.out.println("start p:_"+start);
		end = inputposition;// lbf.limit();
		inend = end;
		//rdif = end - start;
		boolean data_residue = ( end - start) > 0;
		// lscparam.cmd = asc_in_cmd;

		if (data_residue) {
			scanning: do {

				// instart = start;

				if (lclimit > 0) {
					llcount += (end-start);
					if (llcount >= lclimit) {
						supposedEnd = (int) (end - llcount + lclimit);
						inend = supposedEnd;
						secbreak = true;
						//System.out.println("\r\nhere limit effactive:__"+lclimit);
						//System.out.println("\r\nhere limit count:__"+llcount);
					}

					this.limitcount = llcount;
				}
				//System.out.println("\r\nstart end :___"+ start+"  "+inend);
				lscparam.limitType=this.limitType;
				cuscanner = this.sc.scan(buf.array(), start, inend, secbreak, lcm, lscparam);
				if (cuscanner == null){
					 if(this.lastinbf!=null){
						// BFHandler.thebfcache.get();
						 BFHandler.thebfcache.recycle(this.lastinbf);
						 this.lastinbf = null;
					 }
					lcm.restore();
					return null;
				}
				if (supposedEnd > 0 || lscparam.brkconfirming) {
					lastscannedpoint = lscparam.lastposition + 1;
					if (!lscparam.brkconfirming && supposedEnd > 0 && this.limitType == Scanner.limitbreak_constrain) {
						throw new ScanningLimitException("scanning limit exceeded:__" + this.limit);
					}
					llcount = this.limitcount = 0;
					if (lastscannedpoint < end) {
						//rdif=end-lastscannedpoint;
						start = lastscannedpoint;
						//llcount = this.limitcount = 0;
						lclimit = this.limit = lscparam.limitLength;
						this.limitType = lscparam.limitType;
						if (supposedEnd > 0)
							supposedEnd = -1;
						secbreak = false;
						lscparam.brkconfirming = false;
						this.sc = cuscanner;
						continue scanning;
					} else {
						
						secbreak = false;
						lscparam.brkconfirming = false;
						this.sc = cuscanner;
						this.limitType = lscparam.limitType;
						this.limit = lscparam.limitLength;
						//System.out.println("scanning breaked");
						break scanning;
						
					}
				} else{
					//System.out.println("scanning breaked2");
					break scanning;
				}

			} while (true);

		}

		if (lastred == -1) {
			//System.out.println("here ending -1?");
			this.sc.scan(null, -1, -1, false, lcm, lscparam);
			 if(this.lastinbf!=null){
				 BFHandler.thebfcache.recycle(this.lastinbf);
				 this.lastinbf = null;
			 }
			lcm.restore();
			return null;
		}

		this.cpm.capCondition(lreg);
//		if(!goonscan){
//			System.out.println();
//			System.out.println("cap state"+lreg[CaptureManager.label_cp_state]);
//		}
		cap: switch (lreg[CaptureManager.label_cp_state]) {
			case CaptureManager.Cmd_non_capture:
				if (goonscan) {
					buf.clear(); // continue scanning;
					buf.mark();
					//System.out.println("mark: clear mark");

				} else {
					BFHandler.thebfcache.recycle(buf);
					lcm.restore();
					buf = null;
					//System.out.println("mark: recycled bf");
				}

				break;
			case CaptureManager.Cmd_init_capture:
				occupiedratio = bufsize / lreg[CaptureManager.label_cp_length];
				int lcpstart = lreg[CaptureManager.label_cp_start];
				if (occupiedratio >= 2 && lcpstart > 0) {
					// System.out.println("befor compat:_"+Arrays.toString(lbf.array()));
					// System.out.println("befor compat p:_"+lbf);
					//System.out.println("\r\ncmpat start:"+lcpstart);
					buf.limit(inputposition);
					buf.position(lcpstart);
					buf.compact();
					// System.out.println("after compat:_"+Arrays.toString(lbf.array()));
					// System.out.println("after compat p:_"+lbf);
					buf.mark();
					//System.out.println("mark: compact");
					this.cpm.adjust();
					break cap;
				}
			case CaptureManager.Cmd_capture:
				if (slot_residue) {// not full
					if (!data_residue && inputposition == 0 && !goonscan) {
						BFHandler.thebfcache.recycle(buf);
						lcm.release();
						//lcm.restore();
						buf = null;
						//System.out.println("mark: recycled");
						//System.out.println("ever happened?");
					} else {
						buf.position(inputposition);// lbf.position(lbf.limit());
						buf.mark();
						buf.limit(bufsize);
						//System.out.println("mark: residue cped mark ");
					}

				} else { // full
					buf = BFHandler.thebfcache.get();
					buf.mark();
					this.cpm.setCurrentBuffer(buf);
					//System.out.println("mark: allocated mark ");
				}
				break;
		}

		return this.lastinbf = buf;

	}

}