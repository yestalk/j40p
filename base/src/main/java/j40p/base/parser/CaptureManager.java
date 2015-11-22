package j40p.base.parser;

import j40p.base.UTF8ByteStr;
import j40p.base.UTF8ByteStr.DT.Strimpl;
import j40p.base.cache.ABBBuf;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.LinkedList;

public abstract class CaptureManager {
	private static ABBBuf thebfcache;
	public static void setBFCache(ABBBuf abbf){
		CaptureManager.thebfcache=abbf;
	}

	public static final int regSize = 3;

	public static final int Cmd_capture = 1;
	public static final int Cmd_init_capture = 0;
	public static final int Cmd_non_capture = -1;

	public static final int label_cp_state = 0;
	public static final int label_cp_start = 1;
	public static final int label_cp_length = 2;

	protected int start;
	protected int length;
	protected int limt = -1;
	//protected boolean haslimt = false;

	protected LinkedList<ByteBuffer> trl = new LinkedList<ByteBuffer>();
	protected ByteBuffer cutrunk;

	// protected LinkedList<EsNode> esdetail = new
	// LinkedList<CaptureManager.EsNode>();
	protected IntBuilder eslplst = new IntBuilder();
	private byte[] estk;
	protected int espoint = -1;

	// protected boolean escled = true;
	protected int eslen = -1;

	protected WritableByteChannel temp;

	protected byte[] perfix;

	// private boolean isexac=true;
	//
	// protected class EsNode {
	// // Wboolean closed = false;
	// int pl;
	// byte dis;
	//
	// EsNode() {
	// this.pl = CaptureManager.this.length;
	// }
	// }

	public boolean isCapturing() {
		return this.trl.size() > 0;
	}

	public boolean isExtractable() {
		//System.out.println("temp here?????????????????????????????" + (this.temp == null));
		return this.temp == null;
	}

	// public byte[] toArray() {
	// return this.copy(new byte[this.length], this.cutrunk.capacity());
	// }

	public void forward() {
		// System.out.print ( this.start+",");
		this.length++;
		//System.out.println("limit, length__"+this.limt+"   "+this.length);
		if (this.limt>0 && this.length > this.limt) {
			//System.out.println("here forward limit"+this.length+"  "+this.limt);
			if (this.temp == null) {
				throw new RuntimeException("exceed the intened limit for capturing:_" + this.limt + " length:_" + this.length);
			} else if(this.trl.size()>1){
				this.out();
				//System.out.println("after out lenthg"+this.length+"  "+this.limt);
			}
		}

	}

	public void forward(int step) {

		this.length += step;
		if (this.limt>0 && this.length > this.limt) {
			
			if (this.temp == null)
				throw new RuntimeException("exceed the intened limit for capturing:_" + this.limt + " length:_" + this.length);
			else if(this.trl.size()>1)
				this.out();
		}
	}

	public void openEscape(int openpoint) {
		if (this.trl.size() == 0)
			throw new RuntimeException("must in capture sate.");
		if (this.eslen <= 0)
			throw new RuntimeException("escape length must be at least   1.");
		this.forward();

		if (this.espoint == -1)
			this.espoint = openpoint;
		else
			throw new RuntimeException("unclosed espcape case.");

		this.estk = cutrunk.array();
		this.eslplst.add(this.length);

		// this.esdetail.add(new EsNode());

	}

	public void cancelEscape() {
		// System.out.println(this.escled);

		if (this.espoint == -1)
			throw new RuntimeException("no more opening espace case for cancel.");
		else {
			this.espoint = -1;
			this.estk = null;

		}
		this.forward();
		this.eslplst.removeLast();

	}

	public void replacement(byte c) {
		// System.out.println("replacement "+c);
		this.forward();
		// EsNode len = this.esdetail.getLast();

		if (this.espoint == -1)
			throw new RuntimeException("no more opening espace case for filling.");
		else {
			//System.out.println("\r\nreplacement po "+this.espoint);
			this.estk[this.espoint] = c;
			this.espoint = -1;
			this.estk = null;
		}
	}

	public CaptureManager capture(int start, int limit, GatheringByteChannel temp, byte[] perfix) {
		
		this.perfix = perfix;
		return this.capture(start, limit, temp);
	}

	public CaptureManager capture(int start, int limt, int eslen, boolean open_escpae, byte[] perfix) {
		this.perfix = perfix;
		return this.capture(start, limt, eslen, open_escpae);

	}

	public CaptureManager capture(int start, int limit, GatheringByteChannel temp) {
		//new Throwable().printStackTrace();
		//System.out.println("file capture !!!!!!!!!!!!!!!!");
		// System.out.println("\r\ncapture start po:" + start);
		ByteBuffer ltk = this.cutrunk;
		LinkedList<ByteBuffer> ltrl = this.trl;
		if (ltrl.size() > 0)
			throw new RuntimeException("unfinished capture exist.");
		if (ltk == null)
			throw new RuntimeException("current trunk not been initied.");
		ltrl.add(ltk);
		this.temp = temp;

		this.start = start;
		this.length = 1;

		if (limit > 0) {
			 
			this.limt = limit;
		} else {
			 
			this.limt = -1;
		}
		return this;
	}

	public CaptureManager capture(int start, int limit, int eslen, boolean open_escpae) {
		// System.out.println("\r\ncapture start po:" + start);
		ByteBuffer ltk = this.cutrunk;
		// System.out.println
		// ("cap start bf:_"+System.identityHashCode(ltk)+"__");
		// System.out.println ("cap start:_"+start+"__");
		// System.out.println((char)ltk.array()[start]);

		LinkedList<ByteBuffer> ltrl = this.trl;
		if (ltrl.size() > 0)
			throw new RuntimeException("unfinished capture exist.");
		if (ltk == null)
			throw new RuntimeException("current trunk not been initied.");
		if (eslen > ltk.capacity() + 1)
			throw new RuntimeException("hard to support escape size bigger than the trunk size.");

		ltrl.add(ltk);
		this.start = start;
		this.length = 1;
		if (limit > 0) {
			 
			this.limt = limit;
		} else {
			 
			this.limt = -1;
		}
		if (eslen > 0) {
			this.eslen = eslen;
			if (open_escpae) {
				 //System.out.println("\r\ncapture open start:" + start);
				// System.out.println("open es here?");

				// this.esdetail.add(new EsNode());
				if (this.espoint == -1) {
					this.espoint = start;
					this.estk = ltk.array();
					this.eslplst.add(1);
				} else
					throw new RuntimeException("unclosed espcape case.");
			}
		} else
			eslen = -1;

		return this;
	}

	// public byte[] toArray() {
	// return this.copy(new byte[this.length], this.cutrunk.capacity());
	// }

	public boolean isAllWhitespace() {
		// System.out.println("isAllWhitespace:__"+this.trl.size());
		if (this.espoint != -1)
			throw new RuntimeException("unclosed espcape case.");
		int al = this.length;
		LinkedList<ByteBuffer> precy = new LinkedList<ByteBuffer>();
		int bflth = cutrunk.capacity();
		ByteBuffer fstbf = this.trl.peek();
		byte[] in = fstbf.array();
		for (int ti = 1, oi = this.start; ti <= al; ti++) {
			switch (in[oi]) {
				case '\t':
				case ' ':
				case '\r':
					break;
				default: {
					// if (this.trl.size()== 0) {
					// this.trl.add(fstbf);
					// }

					this.trl.addAll(0, precy);

					// for (ByteBuffer i : precy) {
					// this.trl.addFirst(i);
					// //System.out.println("here?");
					// }
					// System.out.println("isAllWhitespace after:__"+this.trl.size());
					return false;
				}

			}
			if (oi + 1 == bflth) {
				oi = 0;
				if (this.trl.size() > 0) {
					// System.out.println("precy addFirst?");
					precy.addFirst(this.trl.removeFirst());
					fstbf = this.trl.peek();

					in = fstbf.array();

				}
			} else
				oi++;
		}

		if (this.trl.size() > 1)
			throw new RuntimeException("length do not match");
		else
			this.trl.clear();
		for (ByteBuffer i : precy) {
			CaptureManager.thebfcache.recycle(i);
		}
		return true;
	}

	public UTF8ByteStr extract() {
		// System.out.println("\r\nexact start !!!!!!!!!!!!!!------------");
		if (this.espoint != -1)
			throw new RuntimeException("unclosed espcape case.");

		int dl, al;
		int pfxl = this.perfixlength();
		if (this.eslplst.size() > 0) {

			al = this.length;
			dl = al - this.eslplst.size() * this.eslen + pfxl;
		} else
			al = dl = this.length + pfxl;

		byte[] out = new byte[dl];
		int bflth = cutrunk.capacity();
		if (al == dl) {
			Strimpl rt = new Strimpl();
			rt.setData(this.copy(out, bflth));
			this.perfix = null;
			return rt;
		}
		this.copyperfix(out);
		int esnd = this.eslplst.removeFirst();
		ByteBuffer fstbf = this.trl.removeFirst();
		byte[] in = fstbf.array();
		int leslen = this.eslen;
		//System.out.println("\r\nextra start:"+this.start);
		for (int ti = 1, di = 0, oi = this.start; ti <= al; ti++) {
			//System.out.println("inoi:"+in[oi]);
			out[di++] = in[oi];
			if (ti == esnd) {
				// out[di++] = in[oi];
				ti += leslen;
				if (oi + leslen + 1 >= bflth) {
					oi = leslen - bflth + oi + 1;

					if (this.trl.size() > 0) {
						CaptureManager.thebfcache.recycle(fstbf);
						fstbf = this.trl.removeFirst();
						in = fstbf.array();

					}
				} else
					oi += leslen + 1;
				if (this.eslplst.size() > 0)
					esnd = this.eslplst.removeFirst();
			} else {
				// out[di++] = in[oi];
				if (oi + 1 == bflth) {
					oi = 0;

					if (this.trl.size() > 0) {
						CaptureManager.thebfcache.recycle(fstbf);
						fstbf = this.trl.removeFirst();
						in = fstbf.array();

					}
				} else
					oi++;
				// System.out.println(in[oi]);
			}

		}

		// if (this.trl.size() > 0)
		// throw new RuntimeException("length do not match");
		dealending();
		Strimpl rt = new Strimpl();
		rt.setData(out);
		this.perfix = null;
		// System.out.println("\r\nexact over !!!!!!!!!!!!!!------------");
		return rt;
	}

	// private int transoi(int oi,int step,int trunksize ,byte[] in,ByteBuffer
	// fstbf){
	//
	// return oi;
	// }

	// public byte[] toArray() {
	// return this.copy(new byte[this.length], this.cutrunk.capacity());
	// }

	// public void ending() {
	// // System.out.println("ending...1");
	// this.outperfix();
	// // System.out.println("ending...2");
	// int cp = this.cutrunk.capacity();
	// int tsz = this.trl.size();
	// if (tsz > 1) {
	// // System.out.println("ending...3");
	// ByteBuffer bfl = this.trl.removeLast();
	// ByteBuffer bff = this.trl.removeFirst();
	// bff.position(this.start).limit(cp);
	// while (bff.hasRemaining()) {
	// try {
	// this.temp.write(bff);
	//
	// } catch (IOException e) {
	// throw new RuntimeException(e);
	//
	// }
	// }
	// BFCache.i.recycle(bff);
	// // System.out.println("ending...5");
	// for (ByteBuffer i : this.trl) {
	// i.clear();
	// while (i.hasRemaining()) {
	// try {
	// this.temp.write(i);
	//
	// } catch (IOException e) {
	// throw new RuntimeException(e);
	// }
	// }
	// BFCache.i.recycle(i);
	// }
	// // System.out.println("ending...6");
	// ByteBuffer bflr = bfl.asReadOnlyBuffer();
	// // System.out.println("ending...8");
	// bflr.limit((this.start + this.length) % cp).position(0);
	// // System.out.println("ending...9");
	// while (bflr.hasRemaining()) {
	// // System.out.println("ending...10");
	// try {
	// this.temp.write(bflr);
	//
	// } catch (IOException e) {
	// throw new RuntimeException(e);
	//
	// }
	// }
	//
	// // BFCache.i.recycle(bfl);
	// this.trl.clear();
	// // System.out.println("ending...7");
	// } else if (tsz > 0) {
	// // System.out.println("ending...4");
	// ByteBuffer bff = this.trl.removeFirst();
	// ByteBuffer bflr = bff.asReadOnlyBuffer();
	// // System.out.println("\r\nstart po:" + this.start);
	// // System.out.println("limit po:" + (this.start + this.length - 1));
	// // System.out.println("cap size:" + cp);
	// bflr.position(this.start).limit(this.start + this.length - 1);
	// while (bflr.hasRemaining()) {
	// try {
	// // System.out.println("temp:_"+this.temp);
	// this.temp.write(bflr);
	//
	// } catch (IOException e) {
	// throw new RuntimeException("temp:_" + this.temp, e);
	//
	// }
	// }
	// // BFCache.i.recycle(bff);
	// }
	// this.temp = null;
	// // this.isexac=true;
	// }
	public void ending() {
		//System.out.println("endding");
		//new Throwable().printStackTrace();
		this.outperfix();
		this.mdout(this.length);
		dealending();

	}

	private void out() {
		//System.out.println("middle out!!!!!!");
		this.outperfix();
		this.start = this.mdout(this.length - 1);
		//System.out.println("middle out cap__"+this.isCapturing());
		this.length = 1;
		
	}

	private int mdout(int len) {
		int rtv = 0;
		int trlsz = this.trl.size();
		ByteBuffer thebf = this.trl.removeFirst();
		int cp = this.cutrunk.capacity();
		
		if (trlsz == 1) {
			rtv = this.start + len;
			//try{
			thebf.limit(rtv).position(this.start);
			//}catch(Exception e){
			//	throw new RuntimeException("rtv illegal:_"+rtv,e);
			//}
			while (thebf.hasRemaining()) {
				try {
					this.temp.write(thebf);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			if (rtv == 0)
				throw new RuntimeException("rtv==0");

		} else  if (trlsz > 1) {
			//try{
			thebf.limit(cp).position(this.start); // set limit first, otherwise position could be bigger than limit.
//			}catch(Exception e){
//				System.out.println("ilegl start:"+this.start);
//				System.out.println("ilegl limit:"+cp);
//				throw new RuntimeException(e);
//			}
			int outed = thebf.remaining();
			while (thebf.hasRemaining()) {
				try {
					this.temp.write(thebf);
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			CaptureManager.thebfcache.recycle(thebf);
			boolean bk = false;
			// thebf = this.trl.removeLast();
			for (ByteBuffer i : this.trl) {

				outed += cp;
				if (outed > len) {
					thebf = i;
					i = i.asReadOnlyBuffer();
					rtv = cp - (outed - len);
					//try{
					i.limit(rtv).position(0);
//					}catch(Exception e){
//						System.out.println("illegal rtv limit_"+rtv);
//						throw new RuntimeException(e);
//					}
					bk = true;

				} else if (outed == len) {
					thebf = i;
					rtv = 0;
					bk = true;
				}
				if (!bk)
					i.clear();
				while (i.hasRemaining()) {
					try {
						this.temp.write(i);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				}
				if (!bk) {
					CaptureManager.thebfcache.recycle(i);
				} else
					break;

			}
			ByteBuffer thebf2 = null;

			while ((thebf2 = this.trl.removeFirst()) != thebf) {
				continue;
			}
			if (rtv > 0 || this.trl.size()==0)
				this.trl.addFirst(thebf2);

		}
		return rtv;
	}

	

	// private void out() {
	//
	// // this.isexac=false;
	// this.outperfix();
	// int cp = this.cutrunk.capacity();
	// int tsz = this.trl.size();
	// if (tsz > 1) {
	// System.out.println("\r\never happen out???? 33333333333");
	// ByteBuffer bfl = this.trl.removeLast();
	// ByteBuffer bff = this.trl.removeFirst();
	// bff.position(this.start).limit(cp);
	// while (bff.hasRemaining()) {
	// try {
	// this.temp.write(bff);
	//
	// } catch (IOException e) {
	// throw new RuntimeException(e);
	//
	// }
	// }
	// BFCache.i.recycle(bff);
	// for (ByteBuffer i : this.trl) {
	// i.clear();
	// while (i.hasRemaining()) {
	// try {
	// this.temp.write(i);
	//
	// } catch (IOException e) {
	// throw new RuntimeException(e);
	// }
	// }
	// BFCache.i.recycle(i);
	// }
	// this.length -= ((cp - this.start) + this.trl.size() * cp);
	// System.out.println("\r\nlength after out:" + this.length);
	// this.start = 0;
	// this.trl.add(bfl);
	// }
	//
	// }

	private void outperfix() {
		if (this.perfix != null) {
			ByteBuffer perbf = ByteBuffer.wrap(this.perfix);
			this.perfix = null;
			while (perbf.hasRemaining()) {
				try {
					this.temp.write(perbf);

				} catch (IOException e) {
					throw new RuntimeException(e);

				}
			}
		}
	}

	private int perfixlength() {
		if (this.perfix == null)
			return 0;
		else
			return this.perfix.length;
	}

	private void copyperfix(byte[] out) {
		byte[] lperfx = this.perfix;

		if (lperfx != null && lperfx.length > 0) {
			for (int i = 0, l = lperfx.length - 1; i <= l; i++) {
				out[i] = lperfx[i];
			}
		}
	}

	private byte[] copy(byte[] out, int bflth) {
		// System.out.println(this.trl.size());
		this.copyperfix(out);
		ByteBuffer fstbf = this.trl.removeFirst();
		byte[] in = fstbf.array();
		// System.out.println ("copy start:_"+this.start+"__");
		// System.out.println ("copy start bf:_"+System.identityHashCode(fstbf)
		// +"__");
		// System.out.println ("copy start char:_"+(char)in[this.start]+"__");
		// System.out.println("__cplength:__"+al);
		// System.out.println("__trunksize:__"+(this.trl.size()+1));
		for (int ti = 1, di = 0, al = out.length, oi = this.start; ti <= al; ti++) {
			// System.out.println("__oi:__"+oi);
			out[di++] = in[oi];
			if (oi + 1 == bflth) {
				oi = 0;
				if (this.trl.size() > 0) {
					CaptureManager.thebfcache.recycle(fstbf);
					fstbf = this.trl.removeFirst();
					in = fstbf.array();

				}
			} else
				oi++;
		}

		// System.out.print("--");
		// for(int dpi=0,dpl=out.length-1;dpi<=dpl;dpi++){
		// System.out.print((char)out[dpi]);
		// }
		// System.out.println();
		dealending();

		// if (this.trl.size() > 0){
		// //System.out.println(new String(out,StandardCharsets.UTF_8));
		// for(ByteBuffer i : this.trl){
		// System.out.print(new String(i.array(),StandardCharsets.UTF_8));
		// }
		// throw new
		// RuntimeException("length do not match:"+this.trl.size()+"___"+out.length);
		// }
		// System.out.println("\r\ncopy over !!!!!!!!!!!!!!------------");
		return out;
	}

	private void dealending() {
		if (this.trl.size() > 0) {
			ByteBuffer lcubf = this.cutrunk;
			ByteBuffer lcubf2 = null;

			while ((lcubf2 = this.trl.removeFirst()) != lcubf) {
				CaptureManager.thebfcache.recycle(lcubf2);
			}
			this.trl.clear();
		}
		this.temp=null;
		this.length=-1;
		this.limt=-1;
		this.eslen=-1;
	}
}
