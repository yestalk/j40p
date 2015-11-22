package j40p.jjson.json.parser.tool;

import java.nio.ByteBuffer;

public class ByteAppender{
	private ByteBuffer bf = ByteBuffer.allocate(1024);
	public ByteAppender append(byte bd){
		if(this.bf.remaining()>0){
			this.bf.put(bd);
		}else{
			ByteBuffer nbf = ByteBuffer.allocate(bf.capacity()+1024);
			nbf.put((ByteBuffer)this.bf.flip()); 
			this.bf=nbf.put(bd);
		}
		
		return this;
	}
	public int size(){
		int sz = this.bf.position();
		return sz;
	}
	public void clear (){
		this.bf.clear();
	}
	public ByteAppender append(byte[] bd){
		if(this.bf.remaining()>=bd.length){
			this.bf.put(bd);
		}else{
			ByteBuffer nbf = ByteBuffer.allocate(bf.capacity()+1024);
			nbf.put((ByteBuffer)this.bf.flip()); 
			this.bf=nbf.put(bd);
		}
		
		return this;
	}
	
	public ByteAppender removeLast(){
		bf.position(bf.position()-1);
		return this;
	}
	
	public byte getLast(){
		return bf.get(bf.position());
	}
	
	public byte[] toBytes(){
		this.bf.flip();
		byte[] rebarr = new byte[this.bf.remaining()];
		this.bf.get(rebarr);
		this.bf.position(this.bf.limit());
		this.bf.limit(this.bf.capacity());
		return rebarr;
	}

}
