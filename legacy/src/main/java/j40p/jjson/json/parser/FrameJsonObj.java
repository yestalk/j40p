package j40p.jjson.json.parser;

//import java.nio.charset.StandardCharsets;

import j40p.jjson.json.parser.list.KeyValueTokenList;
import j40p.jjson.json.parser.list.LiteralString;
import j40p.jjson.json.parser.state.FType;
import j40p.jjson.json.parser.state.FrameContainer;
import j40p.jjson.json.parser.state.Global;

public class FrameJsonObj extends FrameContainer {
	Global g ;
	public FrameJsonObj(Global g  ) {
		this.tokenlist= new KeyValueTokenList();
		//this.JsonObjValueStage=false;
		this.tokenStage=FrameContainer.stage_key;
		//System.out.println("jsonobj initial stage:___"+this.tokenStage);
		this.type=FType.jsonobj;
		this.g=g;
	}
	@Override
	public void accept(byte value) {
		//System.out.println("jsonobj accepted value:___"+(char)value);
		//System.out.println("jsonobj stage:"+this.tokenStage);
		switch(value){
			case '}':
				//System.out.println("jsonobj accepted value:___"+(char)value);
				//System.out.println("jsonobj accepted stage:___"+this.tokenStage);
				switch(this.tokenStage){
					case FrameContainer.stage_middle:		
					case FrameContainer.stage_value:			
						this.dealMiddleStage(this.g);
					case FrameContainer.stage_postvalue:
						if(this.g.temptoken!=null){
							this.tokenlist.add(this.g.temptoken);
							this.g.temptoken=null;
							this.g.stk.pop();
							this.g.temptoken=this.tokenlist;
							//System.out.println("??????here(json)?___"+this.tokenlist);
							this.dealUpper(this.g);
							break;
						}else{

							throw new RuntimeException("error while } closing,no value");
						}
						//break;
					case FrameContainer.stage_key:
						if(this.tokenlist.size()==0){
							this.g.stk.pop();
							this.g.temptoken=this.tokenlist;
							this.dealUpper(this.g);
							break;
						}else
							throw new RuntimeException("error while } closing,no key");
					case FrameContainer.stage_postkey:
						throw new RuntimeException("error while } closing,no value");
				}
				break;
			case ',':
				switch(this.tokenStage){
					case FrameContainer.stage_middle:
					case FrameContainer.stage_value:			
						this.dealMiddleStage(this.g);
					case FrameContainer.stage_postvalue:
						if(this.g.temptoken!=null){
							this.tokenlist.add(this.g.temptoken);
							this.g.temptoken=null;
							this.tokenStage=FrameContainer.stage_key;
							break;
						}else
							throw new RuntimeException("error while , spliting, no value.");
					case FrameContainer.stage_key:
					case FrameContainer.stage_postkey:
						throw new RuntimeException("error while , spliting ,no key");
				}
				break;

			case ':':
				//System.out.println("1st line json \":\" case:___"+this.tokenStage);
				switch(this.tokenStage){
					case FrameContainer.stage_middle:
					case FrameContainer.stage_key:	
						this.dealMiddleStage(this.g);
					case FrameContainer.stage_postkey:
						if(this.g.temptoken!=null){
							this.tokenlist.add(this.g.temptoken);
							this.g.temptoken=null;
							this.tokenStage=FrameContainer.stage_value;
							//System.out.println("json \":\" case:___"+this.tokenStage);
							break;
						}else
							throw new RuntimeException("error while , spliting, no value.");
					case FrameContainer.stage_value:
					case FrameContainer.stage_postvalue:
						throw new RuntimeException("error while } closing,no key");
				}
				break;
			default:
				this.acceptMiddleStage(this.g, value);
		}
		
	}
	
}
