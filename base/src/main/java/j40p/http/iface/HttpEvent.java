package j40p.http.iface;

import j40p.base.ObjFactory;
import j40p.base.UTF8ByteStr;
import j40p.base.commup.def.ContentProvider;

import java.io.File;
import java.nio.channels.GatheringByteChannel;
import java.util.LinkedList;

public interface HttpEvent extends ContentProvider{
	
	interface Factory extends ObjFactory<HttpEvent>{}
	
	 
	
	static int Query_start=0;
	static int UrlEnd=1;
	static int FieldEnd=2;
	
	int signal(int sig);
	int putHttpMethod(UTF8ByteStr data);
	int putPathComponent(UTF8ByteStr data);
	int putHttpVersion(UTF8ByteStr version);
	
	int putQueryPair(UTF8ByteStr key,UTF8ByteStr v);
	
	
	int askHeaderTreatment(UTF8ByteStr hname);
	int putHeader(UTF8ByteStr hname,UTF8ByteStr v);
	int putHeader(UTF8ByteStr hname,LinkedList<? extends Hvalue> v);
	
	
	boolean keepBodyScanning(long contentlength,UTF8ByteStr contentType);
	
	GatheringByteChannel conditonFile(UTF8ByteStr fieldName,UTF8ByteStr fileName,UTF8ByteStr MIMEcontentType);
	
	File getFile();
	
	void setRespond(Respond resp);
	boolean shouldServeFile(Respond resp);
	void conclude(Respond resp);
	
	//int putFile(File f,Hvalue meta);
}
