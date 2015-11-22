package j40p.jjson.parser;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;

public abstract class NioParserimpl implements NIOParser {
	static int trunksz = 512;
	
	LinkedList<byte[]> dbf = new LinkedList<byte[]>();
	
	int bap;
	int op;
	int np;
	
	int pp; //copy pin point
	public ByteBuffer parse(ByteBuffer data){
		if(data==null){
			byte[] lbfa = this.getBFArray();
			this.dbf.add(lbfa);
			return  ByteBuffer.wrap(lbfa);
		}
		byte[] buff = data.array();
		this.np=data.position();
		for(int i=this.op,l=this.np;i<=l;i++){
			this.accept(buff[i],i);
		}
		this.op=this.getOP();
		
		return ByteBuffer.wrap(this.dbf.getLast(), this.op, this.getLimt());
	}
	
	public ByteBuffer getRemaining(){
		return null;
	}
	
	abstract public NIOParser getNextParser();
	
	protected byte[] copyAndCommit(int offset,int size){
		int start = this.pp+offset;
		int end = start+size;
		
		
		this.pp=end+1;
		return null;
	}
	protected int openOffset(int i){
		if(this.bap==0){
			if(i<this.pp)
				return trunksz-this.pp+i;
			else if(i==this.pp)
				return this.pp;
			else
				return i-this.pp;
		}else{
			return this.bap*trunksz+i;
		}
	}
	abstract protected void accept(byte b,int i);

	private int getOP(){
		if(this.np==-1) // all committed/copied
			return 0;
		else if(this.np+1==trunksz)
			return 0;
		else
			return this.np+1;
		//return 0;
	}

	private int getLimt(){
		if(bap==0){
			if(this.op>this.pp)
				return trunksz;
			else
				return this.pp;
		}else
			return trunksz;
	}

	private byte[] getBFArray(){
		return new byte[trunksz];
	}
	public static void main(String[] args) {
		byte[] ba = new byte[100];
		ByteBuffer btf = ByteBuffer.wrap(ba, 50, 20);
		btf.put((byte)79);
		
		
		System.out.println(btf.position());
		System.out.println(btf.limit());
	}
}
