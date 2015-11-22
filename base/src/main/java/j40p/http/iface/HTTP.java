package j40p.http.iface;

import j40p.base.UTF8ByteStr;
import j40p.base.parser.def.ex.ParsingException;

public interface HTTP {
	
	 static class BodyContentAuditException extends ParsingException{
		public BodyContentAuditException(String msg){
			super(msg);
		}
	}

	UTF8ByteStr M_POST=UTF8ByteStr.t.FromString("POST");
	UTF8ByteStr M_GET=UTF8ByteStr.t.FromString("GET");
	UTF8ByteStr M_HEAD=UTF8ByteStr.t.FromString("HEAD");
	UTF8ByteStr M_PUT=UTF8ByteStr.t.FromString("PUT");
	UTF8ByteStr M_DELETE=UTF8ByteStr.t.FromString("DELETE");
	UTF8ByteStr M_OPTIONS=UTF8ByteStr.t.FromString("OPTIONS");
	UTF8ByteStr M_TRACE=UTF8ByteStr.t.FromString("TRACE");
	UTF8ByteStr M_CONNECT=UTF8ByteStr.t.FromString("CONNECT");
	

	String H_accept_str="Accept";
	//UTF8ByteStr H_accept=UTF8ByteStr.t.FromString(HTTP.H_accept_str);
	String HeaderVComp_accept_image_str="image/webp";
	
	UTF8ByteStr H_Connection=UTF8ByteStr.t.FromString("Connection");
	
	String H_Content_Length_str= "Content-Length";
	UTF8ByteStr H_Content_Length=UTF8ByteStr.t.FromString(HTTP.H_Content_Length_str);
	
	String H_Content_Type_str="Content-Type";
	UTF8ByteStr H_Content_Type=UTF8ByteStr.t.FromString(HTTP.H_Content_Type_str);
	
	String H_Content_Disposition_str= "Content-Disposition" ;
		UTF8ByteStr H_Content_Disposition=UTF8ByteStr.t.FromString(H_Content_Disposition_str);
	
	UTF8ByteStr HeaderVComp_boundary=UTF8ByteStr.t.FromString("boundary");
	UTF8ByteStr HeaderVComp_filename=UTF8ByteStr.t.FromString("filename");
	UTF8ByteStr HeaderVComp_mpfieldName=UTF8ByteStr.t.FromString("name");
	
	UTF8ByteStr req_Connection=UTF8ByteStr.t.FromString("keep-alive");
	UTF8ByteStr respon_Connection=UTF8ByteStr.t.FromString("close");
	String MIME_form_urlencoded_str= "application/x-www-form-urlencoded" ;
	UTF8ByteStr MIME_form_urlencoded=UTF8ByteStr.t.FromString(HTTP.MIME_form_urlencoded_str);
	String MIME_multipart_form_data_str= "multipart/form-data" ;
	UTF8ByteStr MIME_multipart_form_data=UTF8ByteStr.t.FromString(HTTP.MIME_multipart_form_data_str);
	UTF8ByteStr MIME_application_octet_stream=UTF8ByteStr.t.FromString("application/octet-stream");
	
	
	
	UTF8ByteStr MIME_text_html=UTF8ByteStr.t.FromString("text/html; charset=UTF-8");
	UTF8ByteStr MIME_text_javascrip=UTF8ByteStr.t.FromString("text/javascript; charset=UTF-8");
	 
}
