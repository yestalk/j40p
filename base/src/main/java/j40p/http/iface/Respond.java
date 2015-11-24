package j40p.http.iface;

import j40p.base.UTF8ByteStr;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Date;
import java.util.HashMap;

public interface Respond {
	
	void setQuitConnectionLater();
	
	void cancelConnectionNow();
	
	//void writeTxt(UTF8ByteStr data);
	void writeTxt(String data);
	void writeTxt(ByteBuffer data);
	
	void respondFileAcrooding2suffix(File file,HashMap<String, String> str);
	void respondFile(File file,UTF8ByteStr MIMEType);
	void respondData(byte[] data,UTF8ByteStr MIMEType);
	
	void setHeader(UTF8ByteStr headerName, UTF8ByteStr headerValue);
	
	void setCookie(UTF8ByteStr key,UTF8ByteStr value,UTF8ByteStr path,UTF8ByteStr domain,Date Expires,boolean isSecure,boolean isHttpOnly);
	void setCookie(UTF8ByteStr key,UTF8ByteStr value,UTF8ByteStr path,UTF8ByteStr domain,long maxage,boolean isSecure,boolean isHttpOnly);
}
