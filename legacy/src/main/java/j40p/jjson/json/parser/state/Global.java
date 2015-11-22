package j40p.jjson.json.parser.state;

import j40p.jjson.json.parser.tool.ByteAppender;

import java.util.Stack;

public class Global {
	public Stack<JsTokenFrame> stk = new Stack<JsTokenFrame>();
	public ByteAppender bappender = new ByteAppender();
	public Object temptoken;
	
	public byte[] unicode;
	public int unisize;
	public boolean sealed;
	
	
	
	
}
