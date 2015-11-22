package j40p.jjson.json.parser;

import j40p.jjson.json.parser.list.ArrayTokenList;
import j40p.jjson.json.parser.list.LiteralString;
import j40p.jjson.json.parser.state.FType;
import j40p.jjson.json.parser.state.FrameContainer;
import j40p.jjson.json.parser.state.Global;


public class FrameJsArray extends FrameContainer {
	
	Global g ;
	public FrameJsArray(Global g) {
		this.tokenlist= new ArrayTokenList();
		this.tokenStage=FrameContainer.stage_arrayv;
		this.type=FType.array;
		this.g=g;
	}
	@Override
	public void accept(byte value) {
		//System.out.println("jsArray accepted value:___"+(char)value);
		//System.out.println("jsArray stage:"+this.tokenStage);
		switch(value){
			case ']':
				switch(this.tokenStage){
					case FrameContainer.stage_middle:		
					case FrameContainer.stage_arrayv:
					case FrameContainer.stage_enforcedarrayv:	
						this.dealMiddleStage(this.g);
					case FrameContainer.stage_postarrayv:
						if(this.g.temptoken!=null){
							this.tokenlist.add(this.g.temptoken);
							this.g.temptoken=null;
							this.g.stk.pop();
							this.g.temptoken=this.tokenlist;
							//System.out.println("js array here?__"+this.tokenStage);
							this.dealUpper(this.g);
							break;
						}else if(this.tokenlist.size()==0){
							this.g.stk.pop();
							this.g.temptoken=this.tokenlist;
							this.dealUpper(this.g);
						}else	
							throw new RuntimeException("error while } closing,no value");
						break;
					case FrameContainer.stage_key:
					case FrameContainer.stage_postkey:
						throw new RuntimeException("error while } closing,no key");
				}
				break;
			case ',':
				switch(this.tokenStage){
					case FrameContainer.stage_middle:
					case FrameContainer.stage_arrayv:	
					case FrameContainer.stage_enforcedarrayv:
						this.dealMiddleStage(this.g);
					case FrameContainer.stage_postarrayv:
						if(this.g.temptoken!=null){
							this.tokenlist.add(this.g.temptoken);
							this.g.temptoken=null;
							this.tokenStage=FrameContainer.stage_enforcedarrayv;
							break;
						}else
							throw new RuntimeException("error while , spliting, no value.");
					case FrameContainer.stage_key:
					case FrameContainer.stage_postkey:
						throw new RuntimeException("error while , spliting ,no key");
				}
				break;
			default:
				this.acceptMiddleStage(this.g, value);
		}
		
	}
	
}
