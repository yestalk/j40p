package j40p.http.impl;

import j40p.base.cache.ABBBuf;
import j40p.base.cache.ObjectCacheX;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public class OutPutter {
	static ABBBuf abc = ObjectCacheX.t.lookup(ABBBuf.class, ABBBuf.getCriteriaBy(512, true), 500);
	LinkedList<ByteBuffer> lnbf = new LinkedList<ByteBuffer>();
	ByteBuffer cubf = OutPutter.abc.get();
	public void printscreen(byte[] bx){
		this.lnbf.add(this.cubf);
		this.cubf.flip();
		this.cubf = OutPutter.abc.get();
		System.out.println("\r\nlnbf size:"+this.lnbf.size());
		boolean xe=true;
		int ci=0;
		for(ByteBuffer i : this.lnbf){
			while(i.hasRemaining()){
				xe=i.get()==bx[ci++];
				//System.out.write(i.get());
				//System.out.print(i.get());
				//System.out.println("capi:"+i.capacity());
				//System.out.println("_ci:_"+ci++);
				
				//ci++;
			}
			
		}
		System.out.flush();
		//System.out.println("\r\n_ci:_"+ci);
		System.out.println("\r\n_xe:_"+xe);
	}
	long tlen;
	public void write(byte[] data, int offset, int length) {

		int bfcap = this.cubf.capacity();
		int dl = data.length;
		if (length < 0)
			throw new RuntimeException("length <0");
		if (offset + length > dl)
			throw new RuntimeException("out of bondary");

		while (length > 0) {
			int rem = this.cubf.remaining();
			if (rem == 0) {
				this.lnbf.add(this.cubf);
				this.cubf.clear();
				this.cubf = OutPutter.abc.get();
				continue;
			}
			if (rem >= length) {
				//System.out.println("hehe:_"+offset);
				this.cubf.put(data, offset, length);
				this.tlen+=length;
				length = 0;
			} else {

				//System.out.println(offset);
				this.cubf.put(data, offset, rem);
				this.tlen+=rem;
				offset +=  rem;
				length -= rem;
				this.lnbf.add(this.cubf);
				this.cubf.clear();
				this.cubf = OutPutter.abc.get();
				// this.cubf.put
				// this.lnbf.
			}
		}
		
	}

	// public static void main(String[] args) {
	// ByteBuffer bbf = ByteBuffer.allocate(10);
	// bbf.put("1234567890".getBytes());
	// ByteBuffer rbbf1 = bbf.asReadOnlyBuffer();
	// ByteBuffer rbbf2 = rbbf1.asReadOnlyBuffer();
	// System.out.println(rbbf2);
	// System.out.println(rbbf1);
	// System.out.println(rbbf2 == rbbf1);
	// }

	public static void main(String[] args) {
		OutPutter opt = new OutPutter();
		//byte[] bx = "1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n1234567890\r\n"
		//		.getBytes(StandardCharsets.UTF_8);
		
		byte[] bx= "大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平.大梦先觉,自知生平."
				.getBytes(StandardCharsets.UTF_8);
		opt.write(bx, 0, bx.length);
		
//		for(byte i : bx){
//			System.out.print(i);
//			//System.out.write(i);
//		}
		 
		System.out.println("\r\n"+opt.tlen);
		System.out.println(bx.length);
		//System.out.println("r\n");
		opt.printscreen(bx);
	}
}
