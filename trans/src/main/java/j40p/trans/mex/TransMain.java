package j40p.trans.mex;

import j40p.trans.common.GroupEntryEnhancer;
import j40p.trans.common.I18nEntry;
import j40p.trans.hdl.def.TypeDefHdl;
import j40p.trans.hdl.insd.CompoundedEntry;
import j40p.trans.hdl.insd.DEntry;
import j40p.trans.hdl.insd.DEntryGroup;
import j40p.trans.hdl.insd.GDataHdl;
import j40p.trans.hdl.insd.TxtEntry;
import j40p.trans.hdl.template.TemplatSetHdl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import parser.impl.DefaultPathParser;

public class TransMain {
	public static void main(String[] args) {
		File tgfolder = new File(args[0]);
		if(!tgfolder.isDirectory())
			throw new RuntimeException("param is not a dir.");
		File ddef = new File(tgfolder,"ddef.txt");
		File dataf = new File(tgfolder,"dins.txt");
		File tempf = new File(tgfolder,"template.txt");
		File outf = new File(tgfolder,"out.html");
		try {
			outf.createNewFile();
		} catch (IOException e) {
			throw new RuntimeException("cant create output file.");
		}
		if(!dataf.exists() || !tempf.exists() || !ddef.exists())
			throw new RuntimeException("file set not ready.");
		
		TemplatSetHdl tshdl = new TemplatSetHdl();
		TypeDefHdl tdfhdl = new TypeDefHdl();
		
		DefaultPathParser dfp = new DefaultPathParser();
		dfp.parse(ddef, tdfhdl);
		
//		GroupEntryEnhancer.MatchType eanno = WorkExpEnh.class.getAnnotation(GroupEntryEnhancer.MatchType.class);
//		HashMap<String, GroupEntryEnhancer> gpmap = new HashMap<>();
//		gpmap.put(eanno.value(), );
		GDataHdl gdhdl = new GDataHdl(tdfhdl.getTypeMap(),new WorkExpEnh());
		//GDataHdl gdhdl = new GDataHdl(tdfhdl.getTypeMap(),new Donothing());
		dfp.parse(dataf, gdhdl);
		
		
		dfp.parse(tempf, tshdl);
		
		CompoundedEntry lcp = gdhdl.getRoot();
		//hehe(lcp,0);
		tshdl.getoutputer().output(outf, lcp);
		
		
	}

	private static void hehe(CompoundedEntry lcp,int dtab) {
		for(int x=0;x<dtab;x++){
			System.out.print("\t");
		}
		System.out.println(lcp.fullPathName);
		int dd=dtab+1;
		StringBuilder sbdd= new StringBuilder();
		//System.out.println(lcp.children);
		if(lcp.children!=null){
			for(int x=0;x<dd;x++){
				sbdd.append("\t");
			}
			for(DEntry i :lcp.children){
				
				if(i.typedef!=null)
					System.out.print(i.typedef.keyI18nName.size()+"___");
				else
					System.out.print("null"+"___");
				if(i instanceof CompoundedEntry)
					hehe((CompoundedEntry)i,dd);
				else{
					System.out.print(sbdd);
					System.out.println(i.fullPathName);

					StringBuilder sbdd2 = new StringBuilder(sbdd);
					sbdd2.append("\t");
					if(i instanceof TxtEntry){
						TxtEntry ti = (TxtEntry)i;

						for(Entry<String,I18nEntry> i18i :ti.txtvaule.entrySet()){
							System.out.print(sbdd2);
							System.out.println(i18i.getKey());
							System.out.print(sbdd2);
							System.out.println(i18i.getValue().content);
						}
					}else if( i instanceof DEntryGroup){
						//System.out.println("!!!!!group");
						DEntryGroup di =(DEntryGroup)i;
						//System.out.println(di.typegroup.size());
						for(DEntry ddi :di.typegroup){
							if(ddi instanceof CompoundedEntry)
								hehe((CompoundedEntry)ddi,dd+1);
						}
					}
				}
			}
		}
	}
}
