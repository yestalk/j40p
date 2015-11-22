package j40p.http.iface;

import j40p.base.TypeUtil;
import j40p.base.commup.def.InteractiveContext;
import j40p.base.l.Func;
import j40p.base.parser.def.Hdl;

import java.nio.channels.GatheringByteChannel;

public interface HttpHdl extends Hdl,InteractiveContext {
	 
	Func<byte[],Void> aMpartBoundary = TypeUtil.i.deFunc(HttpHdl.aMpartBoundary);
	Func<Long,Void> aContentLength = TypeUtil.i.deFunc(HttpHdl.aContentLength);
	Func<GatheringByteChannel,Void> aneedTempStorage = TypeUtil.i.deFunc(HttpHdl.aneedTempStorage);
	
	int sig_UrlEnd=25;
	int sig_Query_start=0;
	//int sig_mpsection=29;
	//int sig_Query_end=1;
	//int sig_path_end=2;
	int sig_conclude=3;
	int sig_HeaderVcomp_end=24;
 
	int tkcxHversion=4;
	int tkcxOpCode=5;
	int tkcxPathComp=6;
	int tkcxQueryKey=7;
	int tkcxQueryV=8;
	int tkcxHvtoken_key=9;
	int tkcxHvtoken_vcomp=10;
	//int ttkcxHvtoken_vcomp_end=23;
	int tkcxHvtoken_v4k=11;
	//int tkcxHvtoken_v4k_end=22;
	int tkcxHeader_roughValue=12;
	
	int tkcxMPContentValue=26;
	int tkcxMPContentDiv=27;
	
	
	 
		
	int ask_HeaderValue_treatment=13;
		int policy_headerV_scann_rough=14;
		int policy_headerV_scann_tokenlized=15;

	int ask_keepscann=16;
		int answer_nullBody=17;
		int answer_as_multipart=18;
		int answer_as_form_urlecoded=19;
		int answer_as_bin=20;
		int answer_Close=21;
 

	void setHttpEventFactory(HttpEvent.Factory cp);
	
}
