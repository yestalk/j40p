package j40p.jjson.parser;

public class Niop2 extends NioParserimpl {

	int offset;
	int size;
	@Override
	protected void accept(byte b, int i) {
		this.offset=this.openOffset(i);
		this.size++;
		this.copyAndCommit(this.offset, this.size);

	}
	
	
	
	@Override
	public NIOParser getNextParser() {
		// TODO Auto-generated method stub
		return null;
	}



	public static void main(String[] args) {
		new Niop2().parse(null);
	}

}
