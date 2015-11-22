package j40p.jjson.json.parser;

import j40p.jjson.json.parser.list.TokenList;
import j40p.jjson.json.parser.state.FType;
import j40p.jjson.json.parser.state.FrameContainer;
import j40p.jjson.json.parser.state.Global;
import j40p.jjson.json.parser.state.JsTokenFrame;


public class JsonPaser extends JsTokenFrame {
	Global g =new Global();
	public JsonPaser() {
		//this.tokenStage=-1;
		this.type=FType.general;
		this.g.stk.push(this);
	}
	
	public TokenList<?> getTokenList(){
		return (TokenList<?>)this.g.temptoken;
	}
	@Override
	public void accept(byte value) {
		//System.out.print((char)value+",");
		JsTokenFrame cuframe = this.g.stk.peek();
		JsTokenFrame openedframe = null;
		byte endingsb = 0;	

		label_out:while(true){
			label_open:while(true){
				label_accept:switch (cuframe.type) {
					case string:	
						break label_accept;		
					case general:	
						switch(value){			
							case '"':
							case '\'':
							default:
								throw new RuntimeException("josn data can only be stared with '{' or '[' ");
							case ' ':
							case '\t':
							case '\b':
							case '\f':
							case '\r':
							case '\n':
								break label_out;
							case '{':
							case '[':
						}
					default:
						switch(value){
							default:
								break label_accept;
							case '{':
								//openedframe = (openedframe==null)?new FrameJsArray(this.g):openedframe;
								openedframe = new FrameJsonObj(this.g);
								break label_open;	
							case '[':
								openedframe = new FrameJsArray(this.g);
								break label_open;	
							case '"':
								endingsb='"';
							case '\'':
								endingsb = (endingsb==0)?(byte)'\'':endingsb;
								FrameJsonString fm = new FrameJsonString(this.g);
								fm.endingSymbol=endingsb;
								openedframe = fm;

								break label_open;	
						}
				}
				cuframe.accept(value);
				break label_out;
			}
			if(cuframe instanceof FrameContainer){
				FrameContainer cuConFram = (FrameContainer)cuframe;
				switch(cuConFram.tokenStage){
					case FrameContainer.stage_postarrayv :
					case FrameContainer.stage_postkey:
					case FrameContainer.stage_postvalue:
						throw new RuntimeException("duplicated value token.");
					case FrameContainer.stage_middle:
						throw new RuntimeException("garbge info before string token.");
					case FrameContainer.stage_key:
						if(openedframe instanceof FrameContainer){
							System.out.println(openedframe.getClass());
							throw new RuntimeException("key must be string, can't be array or jsonobj.");
						}
				}
			}
			this.g.stk.push(openedframe);
			break label_out;
		}
		
//		JsTokenFrame cuframe = this.g.stk.peek();
//		boolean outsymbol=false;
//		byte endingsb = 0;
//		switch(value){
//		case '{':
//		case '[':
//		case '"':
//		case '\'':
//			//outsymbol=false;
//			break;
//		default:
//			outsymbol=true;
//	}

		
//		if(cuframe.type.equals(FType.string) || outsymbol)
//			cuframe.accept(value);
//		else{
//			switch(cuframe.tokenStage){
//				case JsTokenFrame.stage_postarrayv :
//				case JsTokenFrame.stage_postkey:
//				case JsTokenFrame.stage_postvalue:
//					throw new RuntimeException("duplicated value token.");
//				case JsTokenFrame.stage_middle:
//					throw new RuntimeException("garbge info before string token.");
//			}
//			switch(value){
//				case '{':
//					this.g.stk.push(new FrameJsonObj(this.g));
//					break;
//				case '[':
//					this.g.stk.push(new FrameJsArray(this.g));
//					break;
//				case '"':
//					endingsb='"';
//				case '\'':
//					endingsb = (endingsb==0)?(byte)'\'':endingsb;
//					JsTokenFrame fm = new FrameJsonString(this.g);
//					fm.endingSymbol=endingsb;
//					this.g.stk.push(fm);
//					break;
//			}
//		}
	}
}
