package j40p.http.impl;

import j40p.base.DefUtil;
import j40p.base.UTF8ByteStr;
import j40p.base.parser.CaptureManager;
import j40p.base.parser.def.Scanner;
import j40p.http.iface.HttpHdl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.channels.GatheringByteChannel;
import java.nio.charset.StandardCharsets;

public class HttpHeaderScaner implements Scanner {

	// static final UTF8ByteStr expb = StrToolbox.i.FromString("expb");
	private static int mpmbfsz = 4096;

	static final int auxStart = 0;
	static final int auxFirstLine_div1 = 1;
	static final int auxPathdiv = 2;
	static final int auxFirstLine_div2 = 3;
	static final int auxQuerydiv = 4;
	static final int auxQuery_kv_div = 5;
	static final int auxQuery_pair_div = 6;
	static final int auxGdiv = 7;
	static final int auxGend = 8;
	static final int auxHeader_div = 9;
	static final int auxHeader_end_UrlformbodyStart = 10;
	static final int auxHeader_Nv_div = 11;
	static final int auxHeader_comp_div = 12; // ;
	static final int auxHeader_comp_kv_div = 13; // =
	static final int auxHeader_v_preQuote = 14;

	static final int tOpcode = 15;
	static final int tHversion = 16;

	static final int tHeader_v_rough = 17;
	static final int tHeaderName = 18;
	static final int tHeader_comp_v = 19;
	static final int tHeader_comp_v4k = 20;
	static final int tHeader_v_Quotev = 21;

	static final int tUEsp_Pathcomp = 22;
	static final int tPathcomp = 23;

	static final int tUEsp_Querykey = 24;
	static final int tQuerykey = 25;

	static final int tUEsp_Queryv = 26;
	static final int tQueryv = 27;

	static final int OP_fw = 0;
	static final int OP_cap = 1;
	static final int OP_tkex = 2;
	static final int OP_tknell = 3;

	private static final int auxHeader_end_mpbodyStart = 35;
	private static final int bondaryStartMark = 28;
	private static final int bondaryID = 29;
	private static final int bondaryIDPost = 30;
	private static final int bondaryEndMark = 31;

	private static final int despdiv = 32;
	private static final int despgend = 33;
	private static final int mbodycontent = 34;
	private static final int mbodyPerContent = 37;

	static final byte[] UrlEncodetablestr = "0123456789ABCDEF".getBytes(StandardCharsets.US_ASCII);
	static final byte[] UrlDecodetable = new byte[71];

	static {

		for (int i = 0, l = UrlEncodetablestr.length - 1; i <= l; i++) {
			HttpHeaderScaner.UrlDecodetable[HttpHeaderScaner.UrlEncodetablestr[i]] = (byte) i;
		}
	}

	static private int headerlimit = 4096;

	private TokenCenter eb = new TokenCenter();

	private int escapcount = 0;
	private byte urlendata = 0;

	private int custate;

	private int headerpolicy;

	// multipart.
	private byte[] bondary;
	private int bdp = 0;
	private int mpstate = -1;

	private void clear() {
		this.custate = HttpHeaderScaner.auxStart;
		// this.limitype = Scanner.limitbreak_constrain;
		this.escapcount = 0;
		this.urlendata = 0;
		// this.headerpolicy = -1;
	}

	public void setEBuilder(TokenCenter ebd) {
		this.eb = ebd;

	}

	@Override
	public void init(ParamsReg reg) {
		reg.limitLength = HttpHeaderScaner.headerlimit;
		reg.limitType = Scanner.limitbreak_constrain;
	}

	private static final byte[] pfx1 = "\r".getBytes(StandardCharsets.US_ASCII);
	private static final byte[] pfx2 = "\r\n".getBytes(StandardCharsets.US_ASCII);
	private static final byte[] pfx3 = "\r\n-".getBytes(StandardCharsets.US_ASCII);
	private static final byte[] pfx4 = "\r\n--".getBytes(StandardCharsets.US_ASCII);

	private byte[] getperfix() {
		if (this.bdp == 1)
			return HttpHeaderScaner.pfx4;
		else {
			ByteArrayOutputStream bao = new ByteArrayOutputStream(this.bondary.length + 4);
			try {
				bao.write(pfx4);
				bao.write(this.bondary, 0, this.bdp - 1);
				bao.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

			return bao.toByteArray();

		}
	}

	@Override
	public Scanner scan(byte[] data, int start, int end, boolean sectionbreak, CaptureManager cm, ParamsReg paramsreg) {
		int fwdf = -1;
		
		int caplimit = -1;
		int capes = -1;
		boolean capopen = false;
		
		int tkid=-10;
		UTF8ByteStr exd=null;
		// System.out.println("starting state:_" + this.custate);
		// System.out.println("\r\ntotal size:_ " + (end-start));
		if (data == null && start == end && end == -1)
			return null;
		int bdsz = -1;
		if (this.bondary != null)
			bdsz = this.bondary.length;

		byte cudata = -1;
		int lcustate = -1;
		// ;
		// System.out.println("starting char:_" + data[i]);
		scanning: for (int i = start; i < end; i++) {
			cudata = data[i];
			// char xchar=(char)cudata;
			// xchar=(xchar!=10 && xchar !=13 & (xchar>126 || xchar <32
			// ))?'?':xchar;
			// System.out.print(xchar);

			lcustate = this.custate;
			// System.out.println("custate:_"+lcustate);
			fw: switch (lcustate) {

				case HttpHeaderScaner.auxHeader_end_mpbodyStart:
					// System.out.println("here auxHeader_end_mpbodyStart");
					switch (cudata) {
						case '-':
							this.custate = HttpHeaderScaner.bondaryStartMark;
							continue;
						default:
							throw new RuntimeException();
					}
				case HttpHeaderScaner.bondaryStartMark:
					// System.out.println("here bondaryStartMark");
					switch (cudata) {

						case '-':
							this.custate = HttpHeaderScaner.bondaryID;
							// System.out.println("here? bondaryStartMark");
							continue;
						default:
							if (this.mpstate > 0) {
								if (cm.isCapturing())
									cm.forward(4);
								else
									this.mpcap(cm, i, HttpHeaderScaner.pfx3);

								this.custate = HttpHeaderScaner.mbodycontent;
								continue;
							} else
								throw new RuntimeException();
					}
				case HttpHeaderScaner.bondaryIDPost:
					switch (cudata) {
						case '\r':
							this.custate = HttpHeaderScaner.auxGdiv;
							continue;
						case '-':
							this.custate = HttpHeaderScaner.bondaryEndMark;
							continue;
						default:
							throw new RuntimeException();
					}
				case HttpHeaderScaner.bondaryEndMark:
					switch (cudata) {
						case '\n':
							// System.out.println("here? last \\n?????");
							this.custate = HttpHeaderScaner.auxStart;
							this.mpstate = -1;
							// end
							// section break here
							paramsreg.brkconfirming = true;
							paramsreg.lastposition = i;
							// paramsreg.limitType =
							// Scanner.limitbreak_constrain;
							paramsreg.limitLength = HttpHeaderScaner.headerlimit;
							this.eb.token(HttpHdl.sig_conclude, null);
							this.clear();
							return this;
						case '-':
						case '\r':
							continue;

						default:
							throw new RuntimeException();
					}
				case HttpHeaderScaner.bondaryID:
					if (cudata == this.bondary[this.bdp++]) {

						if (this.bdp == bdsz) {
							// System.out.println("bondaryID confirm!!!!!");
							this.custate = HttpHeaderScaner.bondaryIDPost;
							this.bdp = 0;
							if (cm.isCapturing()) {
								// System.out.println("bondaryID about to extract!!!!!");
								if (cm.isExtractable()) {
									// System.out.println("bondaryID   extract confirm!!!!!");
									//this.eb.token(HttpHdl.tkcxMPContentValue, cm.extract());

									fwdf=HttpHeaderScaner.OP_tkex;
									tkid=HttpHdl.tkcxMPContentValue;
									break fw;
								} else {
									// System.out.println("\r\n !!!!!!!!!!!!!!!bondaryID cap before ending!!!!!!!!!!!!!!\r\n");
									cm.ending();
									//this.eb.token(HttpHdl.tkcxMPContentDiv, null);

									fwdf=HttpHeaderScaner.OP_tknell;
									tkid=HttpHdl.tkcxMPContentDiv;
									break fw;
									
									// System.out.println("\r\n !!!!!!!!!!!!!!!bondaryID cap ending!!!!!!!!!!!!!!\r\n");
								}
							} else {
								//this.eb.token(HttpHdl.tkcxMPContentDiv, null);

								fwdf=HttpHeaderScaner.OP_tknell;
								tkid=HttpHdl.tkcxMPContentDiv;
								break fw;
							}
							//continue;
						}
						// System.out.println("bondaryID matching");
					} else {
						if (this.mpstate > 0) {

							if (cm.isCapturing())
								cm.forward(this.bdp + 4);// \r\n--
							else
								this.mpcap(cm, i, this.getperfix());
							this.custate = HttpHeaderScaner.mbodycontent;

						} else
							throw new RuntimeException();

					}
					continue;
				case HttpHeaderScaner.despdiv:
					switch (cudata) {
						default:
							if (cm.isCapturing())
								cm.forward(2);
							else
								this.mpcap(cm, i, HttpHeaderScaner.pfx1);
							this.custate = HttpHeaderScaner.mbodycontent;
							continue;
						case '\n':
							this.custate = HttpHeaderScaner.despgend;
							continue;
					}
				case HttpHeaderScaner.despgend:

					switch (cudata) {
						default:
							if (cm.isCapturing())
								cm.forward(3);
							else
								this.mpcap(cm, i, HttpHeaderScaner.pfx2);
							this.custate = HttpHeaderScaner.mbodycontent;
							continue;
						case '-':
							this.custate = HttpHeaderScaner.bondaryStartMark;
							continue;
					}

				case HttpHeaderScaner.mbodyPerContent:
					switch (cudata) {
						default:
							// System.out.println("\r\n !!!!!!!!!!!!!!!mbodyPerContent cap!!!!!!!!!!!!!!\r\n");
							this.mpcap(cm, i, null);
							this.custate = HttpHeaderScaner.mbodycontent;
							continue;

						case '\r':
							this.custate = HttpHeaderScaner.despdiv;
							continue;

					}

				case HttpHeaderScaner.mbodycontent:
					switch (cudata) {
						default:
							fwdf = HttpHeaderScaner.OP_fw;
							break fw;
						case '\r':
							this.custate = HttpHeaderScaner.despdiv;
							continue;
					}

					// header

				case HttpHeaderScaner.auxStart:
					if (cudata >= 65 && cudata <= 90) {
						//cm.capture(i, 20, -1, false);
						this.custate = HttpHeaderScaner.tOpcode;
						fwdf=OP_cap;
						caplimit=20;
						capes=-1;
						capopen=false;
						break fw;
					} else {
						// System.out.println((char)cudata);
						throw new RuntimeException("cudata:_" + cudata);
					}
				case HttpHeaderScaner.auxFirstLine_div1:
					switch (cudata) {
						case '/':
							this.custate = HttpHeaderScaner.auxPathdiv;
							continue;
						default:
							System.out.println("here?__" + (char) cudata);
							throw new RuntimeException();
					}

				case HttpHeaderScaner.auxQuery_pair_div:
					switch (cudata) {
						case ' ':
						case '\r':
							throw new RuntimeException();
						case '+':
							data[i] = ' ';
						default:
							//cm.capture(i, -1, 2, false);
							this.custate = HttpHeaderScaner.tQuerykey;
							fwdf=OP_cap;
							caplimit=-1;
							capes=2;
							capopen=false;
							break fw;
						case '%':
							//cm.capture(i, -1, 2, true);
							this.custate = HttpHeaderScaner.tUEsp_Querykey;
							fwdf=OP_cap;
							caplimit=-1;
							capes=2;
							capopen=true;
							break fw;

					}

				case HttpHeaderScaner.auxQuerydiv:
					switch (cudata) {
						case '+':
							data[i] = ' ';
						default:
							//cm.capture(i, -1, 2, false);
							this.custate = HttpHeaderScaner.tQuerykey;
							fwdf=OP_cap;
							caplimit=-1;
							capes=2;
							capopen=false;
							break fw;

						case '\r':
							throw new RuntimeException();
						case ' ':
							//this.eb.token(HttpHdl.sig_UrlEnd, null);
							this.custate = HttpHeaderScaner.auxFirstLine_div2;

							fwdf=HttpHeaderScaner.OP_tknell;
							tkid=HttpHdl.sig_UrlEnd;
							break fw;
							 
						case '%':
							//cm.capture(i, -1, 2, true);
							this.custate = HttpHeaderScaner.tUEsp_Querykey;

							fwdf=OP_cap;
							caplimit=-1;
							capes=2;
							capopen=true;
							break fw;
					}
					// case HttpHeaderScaner.auxQuerydiv:
					// case HttpHeaderScaner.auxQuery_pair_div:

				case HttpHeaderScaner.auxPathdiv:
					switch (cudata) {
						default:
							//cm.capture(i, -1, 2, false);
							this.custate = HttpHeaderScaner.tPathcomp;
							fwdf=OP_cap;
							caplimit=-1;
							capes=2;
							capopen=false;
							break fw;
						case '+':
							data[i] = ' ';
							//cm.capture(i, -1, 2, false);
							this.custate = HttpHeaderScaner.tPathcomp;
							fwdf=OP_cap;
							caplimit=-1;
							capes=2;
							capopen=false;
							break fw;
							//continue;
						case '%':
							//cm.capture(i, -1, 2, true);
							this.custate = HttpHeaderScaner.tUEsp_Pathcomp;
							fwdf=OP_cap;
							caplimit=-1;
							capes=2;
							capopen=true;
							break fw;
						case '/':
							//this.eb.token(HttpHdl.tkcxPathComp, null);

							fwdf=HttpHeaderScaner.OP_tknell;
							tkid=HttpHdl.tkcxPathComp;
							break fw;
							 
						case ' ':
							//this.eb.token(HttpHdl.sig_UrlEnd, null);
							this.custate = HttpHeaderScaner.auxFirstLine_div2;

							fwdf=HttpHeaderScaner.OP_tknell;
							tkid=HttpHdl.sig_UrlEnd;
							break fw;
						 
						case '?':
							//this.eb.token(HttpHdl.sig_Query_start, null);
							this.custate = HttpHeaderScaner.auxQuerydiv;

							fwdf=HttpHeaderScaner.OP_tknell;
							tkid=HttpHdl.sig_Query_start;
							break fw; 
					}

				case HttpHeaderScaner.auxQuery_kv_div:
					switch (cudata) {
						default:
							//cm.capture(i, -1, 2, false);
							this.custate = HttpHeaderScaner.tQueryv;

							fwdf=OP_cap;
							caplimit=-1;
							capes=2;
							capopen=false;
							break fw;
						case '+':
							data[i] = ' ';
							//cm.capture(i, -1, 2, false);
							this.custate = HttpHeaderScaner.tQueryv;

							fwdf=OP_cap;
							caplimit=-1;
							capes=2;
							capopen=false;
							break fw;
						case '%':
							//cm.capture(i, -1, 2, true);
							this.custate = HttpHeaderScaner.tUEsp_Queryv;
							fwdf=OP_cap;
							caplimit=-1;
							capes=2;
							capopen=true;
							break fw;
						case '&':
							this.custate = HttpHeaderScaner.auxQuery_pair_div;
							continue;
						case ' ':
							//this.eb.token(HttpHdl.sig_UrlEnd, null);
							this.custate = HttpHeaderScaner.auxFirstLine_div2;


							fwdf=HttpHeaderScaner.OP_tknell;
							tkid=HttpHdl.sig_UrlEnd;
							break fw; 

					}
				case HttpHeaderScaner.auxFirstLine_div2:
					if (cudata == 'H') {
						//cm.capture(i, 8, -1, false);
						this.custate = HttpHeaderScaner.tHversion;
						fwdf=OP_cap;
						caplimit=8;
						capes=-1;
						capopen=false;
						break fw;
					} else
						throw new RuntimeException("protocal id wrong.");
					//continue;
				case HttpHeaderScaner.auxHeader_comp_div:
					switch (cudata) {
						case '\r':
							//this.eb.token(HttpHdl.sig_HeaderVcomp_end, null);
							this.custate = HttpHeaderScaner.auxGdiv;


							fwdf=HttpHeaderScaner.OP_tknell;
							tkid=HttpHdl.sig_HeaderVcomp_end;
							break fw; 
						case ',':
						case ';':
						case ' ':
							continue;

						case '"':
						case '(':
							this.custate = HttpHeaderScaner.auxHeader_v_preQuote;
							continue;
						default:
							//cm.capture(i, -1, -1, false);
							this.custate = HttpHeaderScaner.tHeader_comp_v;
							fwdf=OP_cap;
							caplimit=-1;
							capes=-1;
							capopen=false;
							break fw;
					}
				case HttpHeaderScaner.auxHeader_Nv_div:
					switch (cudata) {
						case ' ':
							continue;
						default:
							//cm.capture(i, -1, -1, false);
							switch (this.headerpolicy) {
								default:
									throw new RuntimeException();
								case HttpHdl.policy_headerV_scann_rough:
									this.custate = HttpHeaderScaner.tHeader_v_rough;
									break;
								case HttpHdl.policy_headerV_scann_tokenlized:
									this.custate = HttpHeaderScaner.tHeader_comp_v;
									break;
							}
							this.headerpolicy = -1;
							fwdf=OP_cap;
							caplimit=-1;
							capes=-1;
							capopen=false;
							break fw;
					}
				case HttpHeaderScaner.tHeader_comp_v4k:
					switch (cudata) {
						case ';':
						case '"':
						case ',':
							//this.eb.token(HttpHdl.tkcxHvtoken_v4k, cm.extract());
							this.custate = HttpHeaderScaner.auxHeader_comp_div;


							fwdf=HttpHeaderScaner.OP_tkex;
							tkid=HttpHdl.tkcxHvtoken_v4k;
							break fw; 
						case '\r':
							this.eb.token(HttpHdl.tkcxHvtoken_v4k, cm.extract());
							this.eb.token(HttpHdl.sig_HeaderVcomp_end, null);
							this.custate = HttpHeaderScaner.auxGdiv;
							continue;
						default:
							fwdf = HttpHeaderScaner.OP_fw;
							break fw;

					}
				case HttpHeaderScaner.auxHeader_comp_kv_div:
					switch (cudata) {
						default:
							//cm.capture(i, -1, -1, false);
							this.custate = HttpHeaderScaner.tHeader_comp_v4k;
							fwdf=OP_cap;
							caplimit=-1;
							capes=-1;
							capopen=false;
							break fw;
						case '"':
						case '(':
							this.custate = HttpHeaderScaner.auxHeader_v_preQuote;
							continue;
					}
				case HttpHeaderScaner.auxHeader_v_preQuote:
					switch (cudata) {
						default:
							//cm.capture(i, -1, -1, false);
							this.custate = HttpHeaderScaner.tHeader_v_Quotev;
							fwdf=OP_cap;
							caplimit=-1;
							capes=-1;
							capopen=false;
							break fw;
							//continue;
						case '"':
							this.eb.token(HttpHdl.tkcxHvtoken_v4k, DefUtil.emptyUtf8Str);
						case ')':
							this.custate = HttpHeaderScaner.auxHeader_comp_div;
							continue;
						case '\r':
							throw new RuntimeException();
					}
				case HttpHeaderScaner.auxGend:
					// this.eb.token(HttpHdl.sig_HeaderVcomp_end, null);
					switch (cudata) {
						default:
							//cm.capture(i, 30, -1, false);
							this.custate = HttpHeaderScaner.tHeaderName;
							fwdf=OP_cap;
							caplimit=30;
							capes=-1;
							capopen=false;
							break fw;
							//continue;
						case '\r':
							this.custate = HttpHeaderScaner.auxHeader_div;
							continue;

					}
				case HttpHeaderScaner.auxGdiv:
					switch (cudata) {
						default:
							throw new RuntimeException();
						case '\n':

							this.custate = HttpHeaderScaner.auxGend;
							continue;
					}
				case HttpHeaderScaner.auxHeader_end_UrlformbodyStart: // in case
																		// of
																		// query
																		// body.
					switch (cudata) {
						case '\n':
						case '\r':
						case ' ':
							throw new RuntimeException();
						case '+':
							data[i] = ' ';
						default:
							//cm.capture(i, -1, 2, false);
							this.custate = HttpHeaderScaner.tQuerykey;
							fwdf=OP_cap;
							caplimit=-1;
							capes=2;
							capopen=false;
							break fw;
							//continue;
						case '%':
							//cm.capture(i, -1, 2, true);
							this.custate = HttpHeaderScaner.tUEsp_Querykey;
							fwdf=OP_cap;
							caplimit=-1;
							capes=2;
							capopen=true;
							break fw;
					}
				case HttpHeaderScaner.auxHeader_div:
					switch (cudata) {
						default:
							throw new RuntimeException();
						case '\n':
							if (this.mpstate >= 0) {
								this.custate = HttpHeaderScaner.mbodyPerContent;
								if (this.mpstate == 0)
									this.mpstate = 1;
								continue;
							}

							switch (this.eb.token(HttpHdl.ask_keepscann, null)) {
								case HttpHdl.answer_Close:
									return null;
								case HttpHdl.answer_nullBody:
									this.eb.token(HttpHdl.sig_conclude, null);
									paramsreg.brkconfirming = true;
									paramsreg.lastposition = i;
									paramsreg.limitType = Scanner.limitbreak_constrain;
									paramsreg.limitLength = 4096;
									this.custate = HttpHeaderScaner.auxStart;
									// System.out.println("ending state:_" +
									// this.custate);
									return this;
								case HttpHdl.answer_as_form_urlecoded:
									// System.out.println("answer_as_form_urlecoded");
									paramsreg.brkconfirming = true;
									paramsreg.lastposition = i;
									paramsreg.limitType = Scanner.limitbreak_event;
									paramsreg.limitLength = this.eb.token(HttpHdl.aContentLength, null);
									this.custate = HttpHeaderScaner.auxHeader_end_UrlformbodyStart;
									// System.out.println("ending state:_" +
									// this.custate);
									// System.out.println("ending char:_" +
									// cudata);
									return this;
								case HttpHdl.answer_as_multipart:

									paramsreg.brkconfirming = true;
									paramsreg.lastposition = i;
									paramsreg.limitType = Scanner.limitbreak_constrain;
									paramsreg.limitLength = this.eb.token(HttpHdl.aContentLength, null);
									this.bondary = this.eb.token(HttpHdl.aMpartBoundary, null);
									this.custate = HttpHeaderScaner.auxHeader_end_mpbodyStart;
									this.mpstate = 0;

									// System.out.println("mutipart scan here");
									// System.out.println("----and :___"+this.custate);
									return this;

								case HttpHdl.answer_as_bin:
									// not support
									return null;

							}

							continue;
					}
				case HttpHeaderScaner.tHeader_v_rough:
					switch (cudata) {
						default:
							fwdf = HttpHeaderScaner.OP_fw;
							break fw;
						case '\r':
							//this.eb.token(HttpHdl.tkcxHeader_roughValue, cm.extract());
							this.custate = HttpHeaderScaner.auxGdiv;

							fwdf=HttpHeaderScaner.OP_tkex;
							tkid=HttpHdl.tkcxHeader_roughValue;
							break fw; 
							//continue;
					}

				case HttpHeaderScaner.tHeaderName:
					switch (cudata) {
						default:
							fwdf = HttpHeaderScaner.OP_fw;
							break fw;
						case ':':
							this.headerpolicy = this.eb.token(HttpHdl.ask_HeaderValue_treatment, cm.extract());
							this.custate = HttpHeaderScaner.auxHeader_Nv_div;
 
							continue;
					}

				case HttpHeaderScaner.tHeader_comp_v:
					switch (cudata) {
						default:
							fwdf = HttpHeaderScaner.OP_fw;
							break fw;
						case '=':
							//this.eb.token(HttpHdl.tkcxHvtoken_key, cm.extract());
							this.custate = HttpHeaderScaner.auxHeader_comp_kv_div;

							fwdf=HttpHeaderScaner.OP_tkex;
							tkid=HttpHdl.tkcxHvtoken_key;
							break fw; 
						case '\r':
							this.eb.token(HttpHdl.tkcxHvtoken_vcomp, cm.extract());
							this.eb.token(HttpHdl.sig_HeaderVcomp_end, null);
							this.custate = HttpHeaderScaner.auxGdiv;
							continue;
						case ' ':
						case ';':
						case ',':
							//this.eb.token(HttpHdl.tkcxHvtoken_vcomp, cm.extract());
							this.custate = HttpHeaderScaner.auxHeader_comp_div;

							fwdf=HttpHeaderScaner.OP_tkex;
							tkid=HttpHdl.tkcxHvtoken_vcomp;
							break fw; 
					}
				case HttpHeaderScaner.tHeader_v_Quotev:
					switch (cudata) {

						case '\r':
						case '\n':
							throw new RuntimeException();
						default:
							fwdf = HttpHeaderScaner.OP_fw;
							break fw;
						case ')':
							//this.eb.token(HttpHdl.tkcxHvtoken_vcomp, cm.extract());
							this.custate = HttpHeaderScaner.auxHeader_comp_div;


							fwdf=HttpHeaderScaner.OP_tkex;
							tkid=HttpHdl.tkcxHvtoken_vcomp;
							break fw; 
						case '"':
							//this.eb.token(HttpHdl.tkcxHvtoken_v4k, cm.extract());
							this.custate = HttpHeaderScaner.auxHeader_comp_div;


							fwdf=HttpHeaderScaner.OP_tkex;
							tkid=HttpHdl.tkcxHvtoken_v4k;
							break fw; 
					}

				case HttpHeaderScaner.tUEsp_Pathcomp:
				case HttpHeaderScaner.tUEsp_Querykey:
				case HttpHeaderScaner.tUEsp_Queryv:

					if ((cudata >= 48 && cudata <= 57) || cudata >= 65 && cudata <= 70) {
						// System.out.println("here url decode.");
						switch (this.escapcount) {
							case 0:
								// System.out.println("decode data1:_"+(char)cudata);
								cm.forward();
								this.urlendata = (byte) (HttpHeaderScaner.UrlDecodetable[cudata] << 4);
								this.escapcount++;
								continue;
							case 1:
								// System.out.println("decode data2:_"+(char)cudata);
								cm.replacement((byte) (this.urlendata | HttpHeaderScaner.UrlDecodetable[cudata]));
								this.escapcount = 0;
								this.urlendata = 0;

								switch (lcustate) {
									case HttpHeaderScaner.tUEsp_Pathcomp:
										this.custate = HttpHeaderScaner.tPathcomp;
										continue;
									case HttpHeaderScaner.tUEsp_Querykey:
										this.custate = HttpHeaderScaner.tQuerykey;
										continue;
									case HttpHeaderScaner.tUEsp_Queryv:
										this.custate = HttpHeaderScaner.tQueryv;
										continue;
								}
						}

					} else
						throw new RuntimeException("url encoding error:_" + (char) cudata + " escont:_" + this.escapcount);
				case HttpHeaderScaner.tPathcomp:
					switch (cudata) {
						case '+':
							data[i] = ' ';
						default:
							fwdf = HttpHeaderScaner.OP_fw;
							break fw;
						case ' ':
							this.eb.token(HttpHdl.tkcxPathComp, cm.extract());
							this.eb.token(HttpHdl.sig_UrlEnd, null);
							this.custate = HttpHeaderScaner.auxFirstLine_div2;
							continue;
						case '?':
							this.eb.token(HttpHdl.tkcxPathComp, cm.extract());
							this.eb.token(HttpHdl.sig_Query_start, null);
							this.custate = HttpHeaderScaner.auxQuerydiv;
							continue;
						case '%':
							cm.openEscape(i);
							this.custate = HttpHeaderScaner.tUEsp_Pathcomp;
							continue;
						case '/':
							//this.eb.token(HttpHdl.tkcxPathComp, cm.extract());
							this.custate = HttpHeaderScaner.auxPathdiv;


							fwdf=HttpHeaderScaner.OP_tkex;
							tkid=HttpHdl.tkcxPathComp;
							break fw; 
					}
				case HttpHeaderScaner.tOpcode:
					if (cudata >= 65 && cudata <= 90) {
						fwdf = HttpHeaderScaner.OP_fw;
						break fw;
					} else if (cudata == ' ') {
						//this.eb.token(HttpHdl.tkcxOpCode, cm.extract());
						this.custate = HttpHeaderScaner.auxFirstLine_div1;


						fwdf=HttpHeaderScaner.OP_tkex;
						tkid=HttpHdl.tkcxOpCode;
						break fw; 
					} else
						throw new RuntimeException("wrong http method.");
				case HttpHeaderScaner.tHversion:
					switch (cudata) {
						default:
							fwdf = HttpHeaderScaner.OP_fw;
							break fw;
						case '\r':
							//this.eb.token(HttpHdl.tkcxHversion, cm.extract());
							this.custate = HttpHeaderScaner.auxGdiv;


							fwdf=HttpHeaderScaner.OP_tkex;
							tkid=HttpHdl.tkcxHversion;
							break fw; 
					}
				case HttpHeaderScaner.tQuerykey:
					switch (cudata) {
						case '+':
							data[i] = ' ';
						default:
							fwdf = HttpHeaderScaner.OP_fw;
							break fw;
						case '=':
							//this.eb.token(HttpHdl.tkcxQueryKey, cm.extract());
							this.custate = HttpHeaderScaner.auxQuery_kv_div;


							fwdf=HttpHeaderScaner.OP_tkex;
							tkid=HttpHdl.tkcxQueryKey;
							break fw; 
						case '%':
							cm.openEscape(i);
							this.custate = HttpHeaderScaner.tUEsp_Querykey;
							continue;
						case '&':
						case '\r':
							throw new RuntimeException();
					}
				case HttpHeaderScaner.tQueryv:
					switch (cudata) {
						case '+':
							data[i] = ' ';
						default:
							fwdf = HttpHeaderScaner.OP_fw;
							break fw;
						case '%':
							cm.openEscape(i);
							this.custate = HttpHeaderScaner.tUEsp_Queryv;
							continue;
						case '&':
							//this.eb.token(HttpHdl.tkcxQueryV, cm.extract());
							this.custate = HttpHeaderScaner.auxQuery_pair_div;


							fwdf=HttpHeaderScaner.OP_tkex;
							tkid=HttpHdl.tkcxQueryV;
							break fw; 
						case ' ':
							//this.eb.token(HttpHdl.tkcxQueryV, cm.extract());
							this.custate = HttpHeaderScaner.auxFirstLine_div2;


							fwdf=HttpHeaderScaner.OP_tkex;
							tkid=HttpHdl.tkcxQueryV;
							break fw; 
					}
			}
			
			switch (fwdf) {
				case HttpHeaderScaner.OP_fw:
					cm.forward();
					//fwdf = -1;
					continue;
				case HttpHeaderScaner.OP_cap:
					cm.capture(i, caplimit, capes, capopen);
					//fwdf = -1;
//					caplimit = -1;
//					capes = -1;
//					capopen = false;
					continue;
				case HttpHeaderScaner.OP_tkex:
					exd=cm.extract();
				case HttpHeaderScaner.OP_tknell:
					//System.out.println("exstat  " +lcustate + "  "+this.custate);
					this.eb.token(tkid, exd);
					exd=null;
					continue;

			}
		}

		if (sectionbreak) {
			switch (paramsreg.limitType) {
				case Scanner.limitbreak_constrain:
					paramsreg.brkconfirming = false;
					break;
				case Scanner.limitbreak_event:
					paramsreg.brkconfirming = true;
					paramsreg.lastposition = end - 1;
					paramsreg.limitType = Scanner.limitbreak_constrain;
					paramsreg.limitLength = HttpHeaderScaner.headerlimit;
					// this.custate = HttpHeaderScaner.auxStart;
					// this.limitype=Scanner.limitbreak_constrain;
					switch (this.custate) {
						case HttpHeaderScaner.tQueryv:
							this.eb.token(HttpHdl.tkcxQueryV, cm.extract());
							this.eb.token(HttpHdl.sig_conclude, null);
							break;
						case HttpHeaderScaner.auxQuery_kv_div:
							// this.eb.token(HttpHdl.tkcxQueryV, null);
							this.eb.token(HttpHdl.sig_conclude, null);
							break;
						default:
							// System.out.println("start:_"+start);
							// System.out.println("end:_"+end);
							throw new RuntimeException("body url format error." + lcustate);
					}
					this.clear();
			}

		}
		// System.out.println("ending state:_" + this.custate);
		// System.out.println("ending char:_" + cudata);
		return this;

	}

	private void mpcap(CaptureManager cm, int i, byte[] pfx) {
		GatheringByteChannel gbch = this.eb.token(HttpHdl.aneedTempStorage, null);

		if (gbch != null) {
			if (pfx == null)
				cm.capture(i, HttpHeaderScaner.mpmbfsz, gbch);
			else
				cm.capture(i, HttpHeaderScaner.mpmbfsz, gbch, pfx);

		} else {
			if (pfx == null)
				cm.capture(i, -1, -1, false);
			else
				cm.capture(i, -1, -1, false, pfx);
		}
		this.custate = HttpHeaderScaner.mbodycontent;
	}

}
