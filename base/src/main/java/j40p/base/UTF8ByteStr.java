package j40p.base;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public interface UTF8ByteStr {
	ToolBox t=new DT();
	
	
	interface ToolBox{
		
		UTF8ByteStr FromString(String str);
	}
	void setData(byte[] d);
	void setLen(int l);
	int subTyping(Integer type);
	byte[] getData();
	String asString();
	
	//long txt2long();
	
	public class DT implements UTF8ByteStr.ToolBox {
		 
		@Override
		public UTF8ByteStr FromString(String str) {

			Strimpl simp = new Strimpl();
			if(str==DefUtil.placeHolderString)
				simp.data=DefUtil.emptybtyes;
			else
				simp.data = str.getBytes(StandardCharsets.UTF_8);
			simp.iskey = true;
			simp.len = simp.data.length;
			if(simp.len==0)
				simp.hashcode=DefUtil.i.placeHolderString.hashCode();
			else
				simp.hashcode = Arrays.hashCode(simp.data);
			return simp;
		};

		public static class Strimpl implements UTF8ByteStr {
			byte[] data;
			int len;
			private Integer hashcode;
			private boolean iskey = false;

			private int subtype = -1;
			
			

			@Override
			public byte[] getData() {
				return this.data;
				
			}

			@Override
			public int subTyping(Integer type) {
				if (type == null)
					return this.subtype;
				else
					this.subtype = type;
				return -1;
			}

			@Override
			public void setData(byte[] d) {
				if (d == null)
					d = new byte[0];
				if (!this.iskey) {
					this.data = d;
					this.len = d.length;
					if (this.hashcode != null)
						this.hashcode = null;
				}

			}

			@Override
			public void setLen(int l) {
				if (!this.iskey) {
					this.len = l;
					if (this.hashcode != null)
						this.hashcode = null;
				}

			}

			@Override
			public int hashCode() {

				if (this.hashcode == null) {
					if (this.len != this.data.length) {
						if (this.len > this.data.length)
							throw new RuntimeException(
									"length can only smaller than data size");
						this.data = Arrays.copyOf(this.data, this.len);
					}
					this.hashcode = Arrays.hashCode(this.data);
				}

				return this.hashcode;
			}

			@Override
			public String toString() {
				// TODO Auto-generated method stub
				if (this.subtype == 0)
					return "//" + this.asString();
				return this.asString();
			}
			String mirror;
			@Override
			public String asString() {
				if(this.mirror==null){
					if(this.data.length==0)
						this.mirror=DefUtil.placeHolderString;
					else
						this.mirror= new String(this.data, StandardCharsets.UTF_8);
				}
				return this.mirror;
			}

			@Override
			public boolean equals(Object obj) {
				boolean rz = false;
				if (obj == this)
					return true;
				else if (!obj.getClass().equals(Strimpl.class))
					return false;
				else if (obj.hashCode() != this.hashCode())
					return false;
				else {
					Strimpl t = (Strimpl) obj;
					rz = Arrays.equals(t.data, this.data);
					if (rz && t.data != this.data) {
						if (t.iskey)
							this.data = t.data;
						else
							t.data = this.data;
					}
				}
				return rz;
			}

		}

	}

}
