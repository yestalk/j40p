package j40p.trans.hdl.template;

import j40p.trans.hdl.insd.CompoundedEntry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

public class TempOutputer {
	public HashMap<String, String> refs = new HashMap<>();
	public HashSet<String> withkeyset = new HashSet<>();
	public HashMap<String ,TemplateRules> tms =new HashMap<>();
	
	
	public void output(File out,CompoundedEntry data){
//		for(Entry<String,TemplateRules> i : tms.entrySet()){
//			System.out.println("Key:__"+i.getKey());
//			TemplateRules ti = i.getValue();
//			System.out.println("matchname:__"+ti.matchname);
//			HashSet<String> escpn = ti.escapeNames;
//			if (escpn!=null){
//				System.out.println("escapename size:__"+ti.escapeNames.size());
//				for(String ei :escpn){
//					System.out.println("\tescapename:___"+ei);
//				}
//			}
//			System.out.println("-------------------\r\n");
//
//		}
		TemplateRules roottp = this.tms.get("/");
		try {
			PrintStream ps;
			try {
				ps = new PrintStream(out,StandardCharsets.UTF_8.name());
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
			roottp.express(ps, data, this,"");
			ps.close();
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
}
