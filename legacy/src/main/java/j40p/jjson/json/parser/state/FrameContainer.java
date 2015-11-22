package j40p.jjson.json.parser.state;

import j40p.jjson.json.parser.list.LiteralString;
import j40p.jjson.json.parser.list.TokenList;

public abstract class FrameContainer extends JsTokenFrame {

	public static final int stage_key=0;
	public static final int stage_postkey=1;
	public static final int stage_value=2;
	public static final int stage_postvalue=3;
	//static final int stage_enforcedvalue=6;
	public static final int stage_arrayv=4;
	public static final int stage_postarrayv=5;
	public static final int stage_enforcedarrayv=6;
	
	public static final int stage_middle=7;
	
	public int tokenStage; // for jsonobj
	public TokenList<?> tokenlist; // for array ,jsonobj 
	
	protected void acceptMiddleStage(Global g,byte value){
		switch(value){
			case ' ':
			case '\t':
			case '\b':
			case '\f':
			case '\r':
			case '\n':
				if(this.tokenStage==FrameContainer.stage_middle)
					g.sealed=true;
				break;
			default:
				//System.out.println("class:___"+this.getClass());
				//System.out.println("state:___"+this.tokenStage+"\r\n-------\r\n");
				switch (this.tokenStage) {
					case FrameContainer.stage_postkey:
					case FrameContainer.stage_postvalue:
					case FrameContainer.stage_postarrayv:
						throw new RuntimeException("garbge info after string token");
					case FrameContainer.stage_middle:
						if(!g.sealed){
							g.bappender.append(value);
							//this.tokenStage=JsTokenFrame.stage_middle;
						}else
							throw new RuntimeException("char in wrong place. ");
						break;
					default:
						//System.out.println((char)value);
						//System.out.println("size:__"+this.g.bappender.size());
						//System.out.println("out size 0:__"+(this.g.bappender.size()!=0));
						if(g.bappender.size()!=0){
							//System.out.println("size:__"+this.g.bappender.size());
							//System.out.println("size 0:__"+(this.g.bappender.size()!=0));
							//System.out.println(this.g.bappender.toBytes().length);
							//System.out.println(new String(this.g.bappender.toBytes(),StandardCharsets.UTF_8));
							throw new RuntimeException("bappend not cleared.");
						}else{
							this.tokenStage=FrameContainer.stage_middle;
							g.bappender.append(value);
						}
							
				}
		}
	}
	protected void dealUpper(Global g){

		JsTokenFrame uppero = g.stk.peek();
		if(uppero instanceof FrameContainer){
			FrameContainer upper=(FrameContainer)uppero;
			switch(upper.tokenStage){
				case FrameContainer.stage_arrayv:
				case FrameContainer.stage_value:
					upper.tokenStage++;
					break;
				case FrameContainer.stage_enforcedarrayv:
					upper.tokenStage=FrameContainer.stage_postarrayv;
			}
		}
	}
	protected void dealMiddleStage(Global g){
		if(g.bappender.size()!=0){
			g.temptoken=new LiteralString(g.bappender.toBytes()) ;
			g.bappender.clear();
			g.sealed=false;
		}else
			g.temptoken=null;		
	}
}
