package j40p.tabform.impl;

import j40p.base.UTF8ByteStr;
import j40p.base.cache.ObjectABuf;
import j40p.base.cache.ObjectCacheX;
import j40p.tabform.iface.DocParser;
import j40p.tabform.iface.Ele;
import j40p.tabform.iface.TabFormHdl;

import java.util.LinkedList;

public class EvNodeListParser implements DocParser<EvNodeListParser.EvNodeList> {
	public static final EvNodeListParser i = new EvNodeListParser();
	public static final int hasNone = 0;
	public static final int hasBoth = 1;
	public static final int hasChild = 2;
	public static final int hasSibling = 3;
	
	
 
	public void parse(EvNodeList medium, TabFormHdl dochdl) {
		StkNv lstk = new StkNv(255,ObjectCacheX.t.lookup(ObjectABuf.class, ObjectABuf.getCriteriaByCapacity(16), 500));
		LinkedList<Object> total = new LinkedList<Object>();

		boolean ignoring = false;
		int numstk = -1;

		Object idata = null;
		Ev lev = null;
		Ev nlev = null;
		int lndtp = -1;
		loop: for (Ev i : medium) {
			lndtp = i.nodetype;
			if (ignoring) {
				switch (lndtp) {

					case EvNodeListParser.hasBoth:
						numstk++;
						continue loop;
					case EvNodeListParser.hasNone:
						numstk--;
						if (numstk == 0)
							ignoring = false;
					case EvNodeListParser.hasChild:
					case EvNodeListParser.hasSibling:
						continue loop;
				}
			}

			switch (i.datatype) {
				case TabFormHdl.Element:
					idata = dochdl.createNode(lstk, (Ele) i.subject);
					break;
				case TabFormHdl.Txt:
				case TabFormHdl.Comments:
					idata = dochdl.createNode(lstk, i.datatype, (UTF8ByteStr) i.subject);
					break;
				case TabFormHdl.Binary:
					idata = dochdl.createNode(lstk, i.subject);
					break;
			}

			if (idata == TabFormHdl.EscapeStructure) {
				switch (lndtp) {

					case EvNodeListParser.hasBoth:

					case EvNodeListParser.hasChild:
						numstk = 1;
						ignoring = true;
					case EvNodeListParser.hasSibling:
						continue loop;
					case EvNodeListParser.hasNone:
						ignoring = true;
						break;
				}

			} else if (idata != null)
				i.subject = idata;
			switch (lndtp) {
				case EvNodeListParser.hasChild:
				case EvNodeListParser.hasBoth:
					lstk.push(i);
					continue loop;
				case EvNodeListParser.hasSibling: // sib case
				case EvNodeListParser.hasNone:
					if (!ignoring) {
						idata = dochdl.concludeNode(lstk, i.subject, null);
						if (idata != TabFormHdl.EscapeStructure) {
							if (idata == null)// no children, no packing;
								idata = this.packSubjectWithChildrenList(i);// Ev
																			// nodetype
																			// will
																			// been
																			// changed
																			// in
																			// this
																			// method.
							lev = lstk.peekTop();
							if (lev != null)
								lev.children.add(idata);
							else {
								total.add(idata);
								continue loop;
							}
						}
					} else
						ignoring = false;

					if (lndtp == EvNodeListParser.hasSibling)
						continue loop;

					nlev = lstk.peekTop();
					while (nlev != null) {
						// System.out.println("nlev2:?___"+nlev);
						// System.out.println("stksize:?___"+lstk.size());
						lev = lstk.pop();
						lndtp = lev.nodetype;

						idata = dochdl.concludeNode(lstk, lev.subject, lev.children);
						if (idata != TabFormHdl.EscapeStructure) {// element on
																	// stack
																	// must has
																	// children,
																	// packing;
							if (idata == null)
								idata = this.packSubjectWithChildrenList(lev);// also
																				// Ev
																				// nodetype
																				// would
																				// be
																				// change
																				// in
																				// this
																				// method.

							nlev = lstk.peekTop();
							if (nlev != null) {
								nlev.children.add(idata);
							} else
								total.add(idata);
						} else
							nlev = lstk.peekTop();
						if (lndtp == EvNodeListParser.hasBoth)
							continue loop;
						// System.out.println("after peek:?___"+lstk.size());
					}
			}
		}
		dochdl.overAll(total);
	}

	public static class EvNodeList extends LinkedList<Ev> {
		private static final long serialVersionUID = 1L;

	}

	public EvNodeList packSiblingList(LinkedList<?> childrenList) {
		if (childrenList == null)
			return null;
		EvNodeList ndl = new EvNodeList();
		Object last = null;
		int csize = childrenList.size();
		if (csize < 1)
			return null;
		else
			last = childrenList.removeLast();

		for (Object i : childrenList) {
			if (i instanceof EvNodeList) {
				EvNodeList ni = (EvNodeList) i;
				Ev nintype = ni.getFirst();
				nintype.nodetype = this.SiblingUpgrade(nintype.nodetype);
				ndl.addAll(ni);
			} else if (i instanceof Ev) {
				Ev sub = (Ev) i;
				sub.nodetype = EvNodeListParser.hasSibling;
				ndl.add(sub);
			} else {
				Ev sub = new Ev();
				if (i instanceof Ele)
					sub.datatype = TabFormHdl.Element;
				else if (i instanceof UTF8ByteStr) {
					sub.datatype = ((UTF8ByteStr) i).subTyping(null);
				} else {
					sub.datatype = TabFormHdl.Binary;
				}
				sub.nodetype = EvNodeListParser.hasSibling;
				ndl.add(sub);
			}
		}
		if (last instanceof Ev) {
			Ev sub = (Ev) last;
			sub.nodetype = EvNodeListParser.hasNone;
			ndl.add(sub);
			// System.out.println("here for last ev:__"+sub);
		} else if (last instanceof EvNodeList) {
			if (childrenList.size() == 0) {
				return (EvNodeList) last;
			} else {
				ndl.addAll((EvNodeList) last);
			}
			// System.out.println("here for last evlist:__"+last);
		} else {
			Ev sub = new Ev();
			if (last instanceof Ele)
				sub.datatype = TabFormHdl.Element;
			else if (last instanceof UTF8ByteStr) {
				sub.datatype = ((UTF8ByteStr) last).subTyping(null);
			} else {
				sub.datatype = TabFormHdl.Binary;
			}
			sub.nodetype = EvNodeListParser.hasNone;
			ndl.add(sub);
			System.out.println("here for last bin:__" + sub);
		}

		return ndl;
	}

	public Object packSubjectWithChildrenList(Ev subject) {
		// System.out.println(subject);
		// System.out.println(subject);
		LinkedList<Object> x = subject.children;
		if (x == null || x.size() == 0) {
			subject.nodetype = EvNodeListParser.hasNone;
			subject.children = null;
			return subject;
		} else {
			EvNodeList ndl = this.packSiblingList(subject.children);
			subject.nodetype = EvNodeListParser.hasChild;
			subject.children.clear();
			ndl.addFirst(subject);
			return ndl;
		}

	}

	private int SiblingUpgrade(int ntype) {
		switch (ntype) {
			case EvNodeListParser.hasNone:
				return EvNodeListParser.hasSibling;
			case EvNodeListParser.hasChild:
				return EvNodeListParser.hasBoth;
			default:
				throw new RuntimeException("why this happen:_" + ntype);
		}

	}

}
