package j40p.trans.hdl.template;

import parser.impl.STDEle;

public class RefLink {
	public String link;
	public RefLink(STDEle info){
		
		this.link=info.instantValues.getFirst();
	}
}
