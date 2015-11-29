package impl.j40p.base;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class HKeye extends HKey {

	byte[] dk;
	int hashv = -1;
	
	HKeye(String str){
		this.dk=str.getBytes(StandardCharsets.UTF_8);
	}

	@Override
	public int compareTo(Object o) {
		if (o instanceof HKeye) {
			HKeye oh = (HKeye) o;
			byte[] st = (oh.dk.length >= this.dk.length) ? this.dk : oh.dk;
			byte[] lo = (st == this.dk) ? oh.dk : this.dk;
			int out = 0;
			for (int i = 0, l = st.length-1; i <= l; i++) {
				int stb = st[i] & 0x00ff;
				int lob = lo[i] & 0x00ff;
				if (stb == lob)
					continue;
				else if (stb > lob){
					out = 1;
					break;
				}else{
					out = -1;
					break;
				}

			}
			if (out == 0) {
				if (st.length == lo.length)
					return 0;
				else
					return (this.dk == st) ? -1 : 1;
			} else {
				return (this.dk == st) ? out : -out;
			}
		} else
			throw new RuntimeException("type not match");

	}

	@Override
	public int dhash(int d) {
		if (d == 0) {
			if (this.hashv == -1)
				this.hashv = MPHG.hash(0, this.dk);
			return this.hashv;
		} else
			return MPHG.hash(d, this.dk);

	}

	@Override
	public boolean equals(Object obj) {

		return Arrays.equals(this.dk, ((HKeye) obj).dk);
	}

	@Override
	public String toString() {
		
		return Arrays.toString(this.dk);
	}
	
	

}
