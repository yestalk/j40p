package j40p.jjson.json.parser;



import j40p.jjson.json.parser.state.FType;
import j40p.jjson.json.parser.state.FrameContainer;
import j40p.jjson.json.parser.state.Global;
import j40p.jjson.json.parser.state.JsTokenFrame;

import java.nio.charset.Charset;

public class FrameJsonString extends JsTokenFrame {
	public boolean escape;
	public byte endingSymbol; // for  string 
	Global g ;
	public FrameJsonString(Global g  ) {
		//this.g.bappender=new ByteAppender();
		this.type=FType.string;
		this.g=g;
	}
	@Override
	public void accept(byte value) {
		//System.out.print((char)value);
		//System.out.print((char)this.endingSymbol);
		if(value==this.endingSymbol && this.g.unicode==null && !this.escape){
			if(this.escape)
				this.g.bappender.append(this.endingSymbol);
			this.g.stk.pop();
			
			this.g.temptoken=new String(this.g.bappender.toBytes(),Charset.forName("UTF_8"));
			//System.out.println("here?(string)___"+this.g.temptoken);
			this.g.bappender.clear();
			FrameContainer upper = (FrameContainer)this.g.stk.peek();	
			switch(upper.tokenStage){
				case FrameContainer.stage_arrayv:
				case FrameContainer.stage_value:
				case FrameContainer.stage_key:
					upper.tokenStage++;
					break;
				case FrameContainer.stage_enforcedarrayv:
					upper.tokenStage=FrameContainer.stage_postarrayv;
			}


			//System.out.println("string pop class:__"+upper.getClass());
			//System.out.println("string pop stage:__"+upper.tokenStage);
		}else{
			if(this.escape){
				switch (value) {
					case '\\':
						//this.escape=true;
						this.g.bappender.append((byte)'\\');
						break;
					case 't':
						this.g.bappender.append((byte)'\t');
						break;
					case 'r':
						this.g.bappender.append((byte)'\r');
						break;
					case 'n':
						this.g.bappender.append((byte)'\n');
						break;
					case 'b':
						this.g.bappender.append((byte)'\b');
						break;
					case 'f':
						this.g.bappender.append((byte)'\f');
						break;
					case 'u':
						this.g.unicode=new byte[4];
						break;
					default:
						this.g.bappender.append(new byte[]{'\\',value});
				}
				this.escape=false;
			}else if(this.g.unicode!=null && this.g.unisize<=3){
				this.g.unicode[this.g.unisize]=value;
				if(this.g.unisize==3){
					String str = new String(this.g.unicode);
					char c = (char) Integer.parseInt( str, 16 );
					byte[] b =(""+c).getBytes(Charset.forName("UTF_8"));
					this.g.bappender.append(b);
					this.g.unicode=null;
					this.g.unisize=0;
				}
				this.g.unisize++;
			}else{
				if(value==(byte)'\\')
					this.escape=true;
				else
					this.g.bappender.append(value);
			}
		}
	}
	
}
