package j40p.jjson.json.parser.state;

import j40p.jjson.json.parser.list.LiteralString;
import j40p.jjson.json.parser.list.TokenList;




public abstract class JsTokenFrame {

	public FType type; // for all :  string , jsonobj, jsarray
	
	public abstract void accept(byte value);
	

		
//		switch (this.tokenStage) {
//			case JsTokenFrame.stage_postarrayv:
//			//case JsTokenFrame.stage_postvalue:
//				throw new RuntimeException("garbge info after string token");
//			case JsTokenFrame.stage_middle:
//				if(!this.g.sealed){
//					this.g.bappender.append(value);
//					//this.tokenStage=JsTokenFrame.stage_middle;
//				}else
//					throw new RuntimeException("char in wrong place. ");
//				break;
//			default:
//				if(this.g.bappender.size()!=0)
//					throw new RuntimeException("bappend not cleared.");
//				else{
//					this.tokenStage=JsTokenFrame.stage_middle;
//					this.g.bappender.append(value);
//				}
//		}
	

}
