package j40p.http.impl;

import j40p.base.UTF8ByteStr;
import j40p.base.commup.def.OutPutController;
import j40p.base.l.Func;
import j40p.base.parser.def.ex.CancelScanningException;
import j40p.base.parser.def.ex.ParsingException;
import j40p.http.iface.HTTP;
import j40p.http.iface.HttpEvent;
import j40p.http.iface.HttpHdl;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class TokenCenter implements HttpHdl {

	private OutPutController out;
	private HttpFileServ hfs = new HttpFileServ();
	private UTF8ByteStr HMethod;
	private UTF8ByteStr cuHeaderName;
	private UTF8ByteStr Content_type;
	private UTF8ByteStr querykey;
	private byte[] multipart_boundary;
	private long Content_L;
	private HttpEvent.Factory clip;
	private HttpEvent he;
	private HVHdl hveb = new HVHdl();
	private int bodyanswer = -1;
	private File rtfile;
	private HVHdl.HV mpfiledetail;
	private UTF8ByteStr mpfieldname;

	public void setHttpEventFactory(HttpEvent.Factory cp) {
		this.clip = cp;
		this.he = cp.createNew();
	}

	@Override
	public void setOutPutController(OutPutController out) {
		this.he.setOutPutController(out);
		this.out = out;

	}

	@Override
	public ArrayList<ByteBuffer> seeking(ParsingException error) {
		return this.he.seeking(error);

	}

	@Override
	public int token(int context, UTF8ByteStr data) throws CancelScanningException {
		int rv;
		// if(data!=null)
		// System.out.println("tk:_"+data.asString());
		switch (context) {
			case HttpHdl.tkcxMPContentDiv:

				this.he.signal(HttpEvent.FieldEnd);
				this.mpfiledetail = null;
				this.mpfieldname = null;
				// this.mpfiledetail = null;
				// this.mpfieldname = null;
				break;
			case HttpHdl.tkcxMPContentValue:
				//System.out.println("mp field name:_"+this.mpfieldname);
				
				this.he.putQueryPair(this.mpfieldname, data);
				// this.mpfiledetail=null;
				this.mpfiledetail = null;
				this.mpfieldname = null;
				break;

			case HttpHdl.ask_keepscann:
				int rtvan = -1;
				if (this.bodyanswer < 0 && (this.HMethod.equals(HTTP.M_GET) || this.HMethod.equals(HTTP.M_HEAD))) {
					rtvan = HttpHdl.answer_nullBody;

				} else {
					if (this.he.keepBodyScanning(this.Content_L, this.Content_type))
						rtvan = this.bodyanswer;
					else {
						this.out.putAndFinish(this.he.seeking(new HTTP.BodyContentAuditException("")));
						rtvan = HttpHdl.answer_Close;
					}
				}
				this.bodyanswer = -1;
				return rtvan;
			case HttpHdl.sig_Query_start:
				this.he.signal(HttpEvent.Query_start);
				break;
			case HttpHdl.sig_UrlEnd:
				this.he.signal(HttpEvent.UrlEnd);
				this.querykey = null;
				break;

			case HttpHdl.sig_conclude:
				if (this.rtfile != null) {
					//System.out.println("here?");
					ArrayList<ByteBuffer> labf = new ArrayList<ByteBuffer>();
					ByteBuffer lbf = ByteBuffer.wrap(this.hfs.serv(this.rtfile));
					labf.add(lbf);
					this.out.put(labf);
					this.rtfile = null;
					this.he = this.clip.createNew();
					break;
				}
				boolean[] breg = new boolean[1];
				ArrayList<ByteBuffer> labf = this.he.concluding(breg);
				if (breg[0]) {
					this.out.putAndFinish(labf);
				} else
					this.out.put(labf);
				this.he = this.clip.createNew();
				break;
			case HttpHdl.sig_HeaderVcomp_end:
				if (this.cuHeaderName == null)
					throw new RuntimeException();
				//System.out.println("cuhdname:_"+this.cuHeaderName.asString());
				switch (this.cuHeaderName.asString()) {
					case HTTP.H_accept_str:
						HVHdl.HV llhv = this.hveb.getHVList().getFirst();
						if (llhv.getValue().asString().indexOf("image/") >= 0) {
							this.rtfile = this.he.getFile();

						}
						break;
					case HTTP.H_Content_Type_str:
						if (this.mpfiledetail != null) {
							this.mpfiledetail.put(this.cuHeaderName, this.hveb.getHVList().getFirst().getValue());
							break;
						}
						llhv = this.hveb.getHVList().getFirst();
						String ctpstr = llhv.getValue().asString();
						
						switch (ctpstr) {
							case HTTP.MIME_multipart_form_data_str:
								//System.out.println("content type:__"+ctpstr);
								this.multipart_boundary = llhv.getAttr(HTTP.HeaderVComp_boundary).getData();
								this.bodyanswer = HttpHdl.answer_as_multipart;
								break;
							case HTTP.MIME_form_urlencoded_str:
								this.bodyanswer = HttpHdl.answer_as_form_urlecoded;
								break;

						}
						break;
					case HTTP.H_Content_Disposition_str:

						llhv = this.hveb.getHVList().getFirst();
						this.mpfieldname = llhv.getAttr(HTTP.HeaderVComp_mpfieldName);
						//System.out.println("mp field name:_"+this.mpfieldname);
						if (llhv.getAttr(HTTP.HeaderVComp_filename) != null) {

							this.mpfiledetail = llhv;

						}

					default:
						this.he.putHeader(this.cuHeaderName, this.hveb.getHVList());
						break;
				}

				this.cuHeaderName = null;
				break;
			case HttpHdl.ask_HeaderValue_treatment:

				this.cuHeaderName = data;
				switch (data.asString()) {
					default:
						return this.he.askHeaderTreatment(data);
					case HTTP.H_Content_Disposition_str:
						//System.out.println("H_Content_Disposition_str tokenlized.");
					case HTTP.H_Content_Type_str:
						return HttpHdl.policy_headerV_scann_tokenlized;
					case HTTP.H_Content_Length_str:
						return HttpHdl.policy_headerV_scann_rough;
				}

			case HttpHdl.tkcxOpCode:
				this.HMethod = data;
				this.he.putHttpMethod(data);
				break;
			case HttpHdl.tkcxPathComp:

				this.he.putPathComponent(data);
				break;
			case HttpHdl.tkcxHversion:
				this.he.putHttpVersion(data);
				break;
			case HttpHdl.tkcxHeader_roughValue:

				switch (this.cuHeaderName.asString()) {
					default:
						this.he.putHeader(this.cuHeaderName, data);
						break;
					case HTTP.H_Content_Length_str:
						this.Content_L = Long.parseLong(data.asString());
						break;
				}
				break;
			case HttpHdl.tkcxHvtoken_key:
				this.hveb.setKey(data);
				break;
			case HttpHdl.tkcxHvtoken_vcomp:
				this.hveb.setValue(data);
				break;
			case HttpHdl.tkcxHvtoken_v4k:
				this.hveb.setV4K(data);
				break;
			case HttpHdl.tkcxQueryKey:
				this.querykey = data;
				break;
			case HttpHdl.tkcxQueryV:
				if (this.querykey != null)
					this.he.putQueryPair(this.querykey, data);
				else
					throw new RuntimeException();
				break;
		}
		return 0;
	}

	@Override
	public <TO, TI> TO token(Func<TO, TI> context, TI data) throws CancelScanningException {
		Object rtv = null;
		if (context == HttpHdl.aContentLength) {
			rtv = this.Content_L;
		} else if (context == HttpHdl.aMpartBoundary) {
			rtv = this.multipart_boundary;
		} else if (context == HttpHdl.aneedTempStorage) {
			if (this.mpfiledetail != null) {
				UTF8ByteStr flname = this.mpfiledetail.getAttr(HTTP.HeaderVComp_filename);
				if (flname != null) {
					rtv = this.he.conditonFile(this.mpfieldname, flname, this.mpfiledetail.getAttr(HTTP.H_Content_Type));
					
					
				} else
					rtv = null;
			} else
				rtv = null;
		}

		return (TO) rtv;
	}

}
