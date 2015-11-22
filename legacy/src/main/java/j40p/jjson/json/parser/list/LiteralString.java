package j40p.jjson.json.parser.list;

import java.nio.charset.Charset;


public class LiteralString {
	String data;
	public LiteralString(byte[] b){
		this.data= new String(b,Charset.forName("UTF-8"));
	}
	@Override
	public String toString() {
		return this.data;
	}
	
	
}
