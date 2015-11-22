package expri.toolc;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class BerDumper {
	public static void main(String[] args) {

	}

	static int b30tag = 0x1f;
	static int idp6 = 0x20;
	static int idclz = 0xc0;
	static int ltag7 = 0x7f;

	void dump(File f) throws IOException {
		FileInputStream fin = new FileInputStream(f);
		int d = 0;
		// byte bd=0;

		int clz = -1;
		boolean type = false;
		int tgn = -1;

		boolean longtagn = false;
		ArrayList<Integer> altagn=new ArrayList<Integer>();
		while ((d = fin.read()) != -1) {
			
			if (longtagn) {
				
				altagn.add(d&ltag7);

				if(d<200){
					longtagn=false;
				}
			} else {
				clz = ((d & idclz) >>> 6);
				type = (d & idp6) == idp6;
				tgn = d & b30tag;

				if (tgn == b30tag)
					longtagn = true;

			}

		}
	}
}
