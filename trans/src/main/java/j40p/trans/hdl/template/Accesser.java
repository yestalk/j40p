package j40p.trans.hdl.template;

import j40p.base.UTF8ByteStr;
import j40p.tabform.iface.Ele;



public class Accesser {
	final public static int acc_current_key = 0;
	final public static int acc_current_v = 1;
	final public static int acc_subTarget_v = 2;

	public String matchname = "";
	public int acctype = 1;
	public String lang = "";

	public Accesser(Ele info) {
		if (info == null)
			return;

		if (  info.getInstantSize() > 0) {

			String lstr = info.getInstant(0).asString();
			if (lstr.indexOf("()key") >= 0) {
				this.acctype = Accesser.acc_current_key;
				// this.matchname="";
			} else {
				this.matchname = lstr.substring(1);
				this.acctype = Accesser.acc_subTarget_v;
				// this.acctype=acc_v;
			}

			// else{
			// //this.matchname="";
			// this.acctype=acc_v;
			// }
		}else
			this.acctype = Accesser.acc_current_v;
		 
			String llang = info.getAttr(UTF8ByteStr.t.FromString("lang")).asString();
			if (llang != null)
				this.lang = llang;
		 

	}
}

// if(info.instantValues.size()>1){
// this.matchname=info.instantValues.removeFirst().substring(1);
// String lacctype=info.instantValues.removeFirst();
// if(lacctype.indexOf("()key")>=0){
// this.acctype=Accesser.acc_current_key;
// }
// }else if(info.instantValues.size()>0){
