package j40p.trans.hdl.template;

import j40p.tabform.iface.Ele;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map.Entry;

import parser.impl.STDEle;

public class HtmlLabel {
	String qnmae;
	HashMap<String,String> attrs;
	public HtmlLabel(Ele info){
		this.qnmae=info.getName().asString().replace("h:", "");
		this.attrs=info.attrs;
		if(this.qnmae.indexOf("meta")>=0){
			String metct = this.attrs.get("content");
			if(metct!=null){
				this.attrs.put("content",metct.replace("\\_", " "));
			}
		}
	}
	
	public void putStartLabel(PrintStream out){
		out.append('<');
		out.append(this.qnmae);
		
		if(this.attrs!=null){
			for(Entry<String,String> i : this.attrs.entrySet()){
				out.append(' ');
				out.append(i.getKey());
				out.append("=\"");
				out.append(i.getValue());
				out.append("\"");
			}
			out.append(' ');
		}
		out.append('>');

	}
	
	public void putEndLabel(PrintStream out){
		out.append("</");
		out.append(this.qnmae);
		out.append('>');
	}
}
