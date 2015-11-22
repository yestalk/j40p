package j40p.tabform.impl;

import java.util.LinkedList;

public class Ev {
	  int nodetype;
	  int datatype;
	  Object subject;
	
	  LinkedList<Object> children=new LinkedList<Object>();

	  
	  Ev(){
		  
	  }
	  Ev(int v){
		  this.subject=v;
	  }
	@Override
	public String toString() {
		String strnt="";
		switch(this.nodetype){
			case EvNodeListParser.hasBoth:
				strnt="hasboth";
				break;
			case EvNodeListParser.hasNone:
				strnt="hasNone";
				break;
			case EvNodeListParser.hasChild:
				strnt="hasChild";
				break;
			case EvNodeListParser.hasSibling:
				strnt="hasSibling";
				break;
		}
		int chsz=(this.children==null)?-1:this.children.size();
		///System.out.println(this.subject);
		return "Ev [nodetype=" + strnt + ", datatype=" + datatype + ", subject=" + subject + ", childrensize=" + chsz + "]";
	}
	
	
}
