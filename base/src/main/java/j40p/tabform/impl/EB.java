package j40p.tabform.impl;

import j40p.base.DefUtil;
import j40p.base.UTF8ByteStr;
import j40p.base.cache.ObjectABuf;
import j40p.base.cache.ObjectCacheX;
import j40p.base.l.Func;
import j40p.tabform.iface.Ele;
import j40p.tabform.iface.TabFormHdl;

import java.util.LinkedList;

public class EB implements EBX { // stand for event builder

	 

	private static final int act_Conclued = 0;
	private static final int act_Create = 1;
	private static final int act_LoopPop = 2;
	// static final int act_Leave=3;

	private static final int[] sibcase_hasprv = new int[] { EB.act_Conclued, EB.act_Create, -1 };
	private static final int[] createOnly = new int[] { EB.act_Create, -1 };
	private static final int[] popcase = new int[] { EB.act_Conclued, EB.act_LoopPop, EB.act_Create, -1 };

	private static final int StackLimt = 500;
	TabFormHdl lhdl;
	Eleimple cuelv;
	UTF8ByteStr cukey;

	int level = 0;
	int lastlevel = 0;
	Ev previousNode = null;
	StkNv stk = new StkNv(EB.StackLimt,ObjectCacheX.t.lookup(ObjectABuf.class, ObjectABuf.getCriteriaByCapacity(16), 500));
	LinkedList<Object> total = new LinkedList<Object>();

	// int hehesend = 0;

	public void setTabFormHdl(TabFormHdl hdl){
		this.lhdl=hdl;
	}

	@Override
	public int token(int context, UTF8ByteStr data) {
		//System.out.println( "tk context: "+context);
		switch (context) {
			case cEleName:
				if (this.cuelv == null) {
					this.cuelv = new Eleimple();
					this.cuelv.type = Ele.FEle;
					this.cuelv.name = data;
					return -1;
				} else
					throw new RuntimeException("un concluded element exist.");
			case cJKeyName:
				if (this.cuelv == null) {
					this.cuelv = new Eleimple();
					this.cuelv.type = Ele.JKV;
					this.cuelv.name = data;
					return -1;
				} else
					throw new RuntimeException("un concluded element exist.");
			case cJInstant:
				this.cuelv.instant.put(this.cuelv.instant.size(), data);
				if (this.cuelv.instant.size() > 1)
					throw new RuntimeException("instant value of Jelement can't be more than one.");
				return -1;
			case cPropKey:
				this.cukey = data;
				return -1;
			case cPropV:
				if (this.cukey == null)
					throw new RuntimeException("key is null");
				else
					this.cuelv.attr.put(this.cukey, data);
				this.cukey = null;
				return -1;
			case cInstV:
				if (this.cukey != null)
					this.cukey = null;
				this.cuelv.instant.put(this.cuelv.instant.size(), data);
				return -1;

		}
		int leavecode = -1;
		Ev cunode = new Ev();
		Ev prvNode = this.previousNode;

		Ev parent_node = null;
		int lc = this.level - this.lastlevel;

		int[] codepice = null;
		Object rtobj = null;
		Ev conclued = null;
		switch (context) {
			default:
				throw new RuntimeException("unknow context, unknow action.");
			case sEventStreamEnd:

				lc = -EB.StackLimt;
				break;
			case cJEEnd:
				if (this.cuelv == null) {
					this.cuelv = new Eleimple();
					this.cuelv.type = Ele.JKV;
					this.cuelv.name = null;
				}
			case cEleEnd:
				cunode.subject = this.cuelv;
				cunode.datatype = TabFormHdl.Element;
				this.cuelv = null;
				break;
			case cTxt:
				cunode.subject = data;
				cunode.datatype = TabFormHdl.Txt;
				data.subTyping(cunode.datatype);
				break;
			case cConmment:
				if (data == null) {
					cunode.subject = data = UTF8ByteStr.t.FromString(DefUtil.placeHolderString);
				} else
					cunode.subject = data;
				cunode.datatype = TabFormHdl.Comments;
				data.subTyping(cunode.datatype);
				break;
		}
		switch (lc) {
			case -EB.StackLimt:
				lc = -this.lastlevel;
				conclued = prvNode;
				cunode = null;
				codepice = EB.popcase;
				break;
			case 0:// sbling case;

				if (prvNode != null) {
					conclued = prvNode;
					codepice = EB.sibcase_hasprv;

				} else {
					codepice = EB.createOnly;
				}
				break;
			case 1: // child case
				if (prvNode != null) {
					this.stk.push(prvNode);
				} else {
					System.out.println("cunode.subject:__" + cunode.subject);
					throw new RuntimeException("no parent been created.");
				}
				codepice = EB.createOnly;
				break;
			default:// pop case;
				if (lc < 0) {
					conclued = prvNode;
					codepice = EB.popcase;
				} else {
					// System.out.println("lc:_" + lc);
					// System.out.println("lastleve:_" + this.lastlevel);
					// System.out.println("culeve:_" + this.level);
					// System.out.println("cusubject:_" + cunode.subject);
					throw new RuntimeException("mis nested doc.");
				}
		}

		ceoutter: for (int li = 0, ix = 0; (ix = codepice[li]) != -1; li++) {
			// System.out.println("li2___"+li);
			switch (ix) {
				case EB.act_Conclued:
					if (conclued != null) {
						// System.out.println("xxx:__"+conclued.subject);
						rtobj = this.lhdl.concludeNode(this.stk, conclued.subject, conclued.children);

						if (rtobj != TabFormHdl.EscapeStructure) {
							if (rtobj == null) {

								rtobj = EvNodeListParser.i.packSubjectWithChildrenList(conclued);

							}
							parent_node = this.stk.peekTop();
							if (parent_node != null)
								parent_node.children.add(rtobj);
							else
								this.total.add(rtobj);
						}
					}
					continue;
				case EB.act_Create:
					if (cunode != null) {
						switch (cunode.datatype) {
							case TabFormHdl.Element:
								rtobj = this.lhdl.createNode(this.stk, (Ele) cunode.subject);
								break;
							case TabFormHdl.Comments:
							case TabFormHdl.Txt:
								rtobj = this.lhdl.createNode(this.stk, cunode.datatype, (UTF8ByteStr) cunode.subject);
								break;
						}

						if (rtobj != TabFormHdl.EscapeStructure) {
							if (rtobj != null)
								cunode.subject = rtobj;
						} else {
							// System.out.println("ignore level:"+this.level);
							cunode = null;
							leavecode = this.level;
							break ceoutter;
						}

					}
					continue;
				case EB.act_LoopPop:
					if (lc < 0) {

						conclued = this.stk.pop();
						// System.out.println(conclued.subject);
						lc++;
						li -= 2;
						// System.out.println("li___"+li);
					}
					continue;
			}
		}
		if (context == EBX.sEventStreamEnd) {
			this.lhdl.overAll(this.total);
			return -1;
		}

		this.lastlevel = this.level;
		this.level = 0;
		this.previousNode = cunode;
		return leavecode;

	}

	@Override
	public <TO, TI> TO token(Func<TO, TI> context, TI data) {
		Object rev = null;
		if (context == EBX.cLevel) {
			this.level = (Integer) data;
		} else if (context == EBX.cIsCommentsIgnored) {
			return (TO) (rev = this.lhdl.iscommentsIgnore());
		}
		return null;
	}

}
