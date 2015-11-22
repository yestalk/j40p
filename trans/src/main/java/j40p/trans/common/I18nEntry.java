package j40p.trans.common;

import j40p.base.UTF8ByteStr;
import j40p.tabform.iface.Ele;
import j40p.tabform.iface.ObjPath;
import j40p.tabform.iface.TabFormHdl;
import j40p.tabform.impl.EvNodeListParser;
import j40p.tabform.impl.EvNodeListParser.EvNodeList;

import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;

public class I18nEntry {
	public String langType;
	public EvNodeList content;

	public I18nEntry(Ele data) {
		if (data != null) {
			String str = data.getInstant(0).asString();
			if (str == null)
				this.langType = "en";
			else
				this.langType = str;
		}
	
	}

	public String getkeynamedata() {
	
	 //this.content.removeFirst();
	// Ev
	 return "what's that mean?";//this.content.getFirst()
	
	 }

	public void putOrg(PrintStream out) {
		Orgtxthdl ortx = new Orgtxthdl();
		ortx.out = out;
		EvNodeListParser.i.parse(this.content, ortx);
	}

	public void putHtmlList(PrintStream out) {
		if (this.content.size() == 1)
			out.append(this.content.get(1).toString());
		else {
			out.append("<ul class=\"txtlistout\">");
			Htmllisthdl ortx = new Htmllisthdl();
			ortx.out = out;
			// Htmllisthdldebug ortxdebug = new Htmllisthdldebug();
			// NodeListParser.i.parse(this.content, ortxdebug);
			// System.out.println(this.content);
			EvNodeListParser.i.parse(this.content, ortx);
			out.append("</ul>");
		}
	}

	class Orgtxthdl implements TabFormHdl {
		PrintStream out;

		@Override
		public boolean iscommentsIgnore() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public Object createNode(ObjPath ancestors, Ele subject) {

			return null;

		}

		@Override
		public Object createNode(ObjPath ancestors, int type, UTF8ByteStr subject) {
			int lsz = ancestors.size();
			for (int i = 1; i < lsz; i++) {
				this.out.append('	');
			}

			try {
				this.out.write(subject.getData());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.out.append("\r\n");
			return null;
		}

		@Override
		public Object createNode(ObjPath ancestors, Object subject) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object concludeNode(ObjPath ancestors, Object subject, LinkedList<?> children) {
			return TabFormHdl.EscapeStructure;
		}

		@Override
		public void overAll(LinkedList<?> total) {
			// TODO Auto-generated method stub

		}

	}

	class Htmllisthdldebug implements TabFormHdl {

		@Override
		public boolean iscommentsIgnore() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Object createNode(ObjPath ancestors, Ele subject) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object createNode(ObjPath ancestors, int type, UTF8ByteStr subject) {
			System.out.println(ancestors);

			return null;
		}

		@Override
		public Object createNode(ObjPath ancestors, Object subject) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object concludeNode(ObjPath ancestors, Object subject, LinkedList<?> children) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void overAll(LinkedList<?> total) {
			// TODO Auto-generated method stub

		}

	}

	class Htmllisthdl implements TabFormHdl {
		PrintStream out;

		UTF8ByteStr unsertaindata;

		@Override
		public boolean iscommentsIgnore() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public Object createNode(ObjPath ancestors, Ele subject) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object createNode(ObjPath ancestors, int type, UTF8ByteStr subject) {
			if (this.unsertaindata != null) {
				this.out.append("<div>");
				// pathOfdata.removeLast();//who fucking added this line?
				try {
					this.out.write(this.unsertaindata.getData());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				this.out.append("</div>");
				this.out.append("<ul  class=\"txtlist\"> ");
			}
			this.out.append("<li class=\"txtitem\" >");

			this.unsertaindata = subject;
			;

			return null;

		}

		@Override
		public Object createNode(ObjPath ancestors, Object subject) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Object concludeNode(ObjPath ancestors, Object subject, LinkedList<?> children) {
			if (children != null && children.size() > 0)
				this.out.append("</ul>");
			else {
				try {
					this.out.write(((UTF8ByteStr) subject).getData());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			this.out.append("</li>");

			this.unsertaindata = null;
			return null;

		}

		@Override
		public void overAll(LinkedList<?> total) {
			// TODO Auto-generated method stub

		}

	}

}
