package j40p.tabform.impl;

import j40p.base.parser.CaptureManager;
import j40p.base.parser.def.Scanner;

public class TFProcessor implements Scanner {
	private static final int Start = 0;
	private static final int Level = 1;

	private static final int Txt = 2;
	private static final int Commonts = 3;

	private static final int PreCommonts = 4;

	private static final int KeyMark = 5;
	private static final int InstanVMark = 6;

	private static final int V4K = 7;
	private static final int ExplicitInstantV = 8;

	private static final int Vcomp = 9;

	private static final int LabelOrComment = 10;
	private static final int LabelName = 11;
	private static final int EWhiteSpace = 12;

	private static final int JAttrLabelMark = 13;
	private static final int JAttrName = 14;
	private static final int JWhiteSpace = 15;
	private static final int JInstantv = 16;

	private static final int PreLineEnd = 17;

	private static final int Commentsgnored = 18;
	private static final int HdlIgnored = 19;

	private static final int Escape_txt_estart = 20;
	private static final int Escape_txt = 21;
	private static final int Escape_Vcomp = 22;
	private static final int Escape_V4k = 23;
	private static final int Escape_InstantV = 24;

	
	
	
	private EB ebd = new EB();
	private boolean iscommentsIgnored = false;

	private int custate = TFProcessor.Start;
	// private int laststate = -1;
	private int level;
	private int line;
	private int charNum;

	private boolean levelneedenclose = false;// in case all txt is whitespace
	private int commentsLevel = -1;
	private int ignorLevel = -1;

	private byte[] fake = new byte[] { '\n' };

	// private Hdl.Param ebparam = new Hdl.Param();
	

	public void setEBuilder(EB ebd){
		this.ebd=ebd;
		this.iscommentsIgnored=ebd.token(EBX.cIsCommentsIgnored, null);
	}
	

	@Override
	public void init(ParamsReg reg) {
		reg.limitType = -1;
		reg.limitLength = -1;

	}


	@Override
	public Scanner  scan(byte[] data, int start,int end,boolean sectionbreak,CaptureManager cm,ParamsReg paramsreg){

 
		boolean fwf=false;
		try {

			for (int i = start ,iend=end; i < iend || (iend == -1 && (data = this.fake) != null && (i = 0) != -1 && (iend = 1) != -1); i++, this.charNum++) {

				byte cudata = data[i];
				//System.out.print((char)cudata);
				
				int lcustat = this.custate;
				//System.out.println(":"+lcustat+"___");
				fw:switch(lcustat){
					case Vcomp:
					case V4K:
					case ExplicitInstantV:
					case JInstantv:
					case JAttrName:	 
						switch(cudata){
							case '\r':
							case '\n':
							case '\t':
							case ' ':
							case '\\':
								break fw;	
							default:
								fwf=true;
								break fw; 
						}
					case Commonts:
					case Txt:
						switch (cudata) {
							case '\r':
							case '\n':
							case '\\':
								//System.out.println("bk fw"+this.line);
								break fw;
							default:
								fwf=true;
								break fw;
						}
							 
					case LabelName:
						switch(cudata){
							case '\r':
							case '\n':
							case '\t':
							case ' ':
								break fw;
							default:
								fwf=true;
								break fw;
						}	
					 
						
				}
				if(fwf){
					cm.forward();
					fwf=false;
					continue;
				}
				statematch: switch (lcustat) {

//					case JInstantv:
//					case JAttrName:
//					case LabelName:
//
//						switch (cudata) {
//							case '\r':
//							case '\n':
//							case '\t':
//							case ' ':
//								break statematch;
//							default:
//								cm.forward();
//								continue;
//						}
					case JAttrLabelMark:

						switch (cudata) {
							case '\r':
							case '\n':
							case '\t':
							case ' ':
								break statematch;
							default:
								cm.capture(i, -1, -1, false);
								this.custate = TFProcessor.JAttrName;
								continue;
						}
	
						
					case JWhiteSpace:
						switch (cudata) {
							case '\r':
							case '\n':
								break statematch;
							case '\t':
							case ' ':
								continue;
							default:
								cm.capture(i, -1, -1, false);
								this.custate = TFProcessor.JInstantv;
								continue;
						}
					case HdlIgnored:
					case Commentsgnored:
						switch (cudata) {
							case '\r':
							case '\n':
								break statematch;
							default:
								continue;
						}
					case PreCommonts:
						switch (cudata) {
							case '\r':
							case '\n':
								break statematch;
							default:
								cm.capture(i, -1, -1, false);
								this.custate = TFProcessor.Commonts;
								continue;
						}

//					case Commonts:
//					case Txt:
//						switch (cudata) {
//							case '\r':
//							case '\n':
//							case '\\':
//								break statematch;
//							default:
//								cm.forward();
//								continue;
//						}

					case LabelOrComment:
						switch (cudata) {
							case '\r':
							case '\n':
							case '\t':
							case ' ':
								break statematch;
							case '/':
								this.commentsLevel = this.level;
								if (this.iscommentsIgnored) {
									this.custate = TFProcessor.Commentsgnored;
								} else {
									this.custate = TFProcessor.PreCommonts;
								}
								continue;
							default:
								cm.capture(i, -1, -1, false);
								this.custate = TFProcessor.LabelName;
								continue;
						}

					case EWhiteSpace:
						switch (cudata) {
							case '\r':
							case '\n':
							case '\\':
								break statematch;
							case '\t':
							case ' ':
								continue;
							case '=':
								this.custate = TFProcessor.InstanVMark;
								continue;
							default: 
								//System.out.println("EWhiteSpace-->Vcomp");
								cm.capture(i, -1, 1, false);
								this.custate = TFProcessor.Vcomp;
								continue;
								
						}
						//break;
						//System.out.println("leak?");
					case Vcomp:
						switch (cudata) {
							case '\r':
							case '\n':
							case '\\':
								break statematch;
							case '=':
								this.ebd.token(EBX.cPropKey, cm.extract());
								this.custate = TFProcessor.KeyMark;
								continue;
						}
						break;
					case Escape_txt_estart:
						switch (cudata) {
							case '\r':
							case '\n':
								break statematch;
							case ':':
								cm.replacement((byte) ':');
							case 't':
								cm.replacement((byte) '\t');
							case '\\':
								cm.replacement((byte) '\\');
							case '/':
								cm.replacement((byte) '/');
								break;
							case 'r':
								cm.replacement((byte) '\r');
								break;
							case 'n':
								cm.replacement((byte) '\n');
								break;
							default:
								cm.cancelEscape();
								break;

						}
						this.custate = TFProcessor.Txt;
						continue;
					case Escape_txt:
						switch (cudata) {
							case '\r':
							case '\n':
								break statematch;
							case 'r':
								cm.replacement((byte) '\r');
								break;
							case 'n':
								cm.replacement((byte) '\n');
								break;
							case '\\':
								cm.replacement((byte) '\\');
								break;
							default:
								cm.cancelEscape();
								break;
						}

						this.custate = TFProcessor.Txt;
						continue;
					case Escape_InstantV:
						switch (cudata) {
							case '\r':
							case '\n':
							case '\t':
							case ' ':
								break statematch;
							case 'r':
								cm.replacement((byte) '\r');
								break;
							case 'n':
								cm.replacement((byte) '\n');
								break;
							case '_':
								cm.replacement((byte) ' ');
								break;
							case 't':
								cm.replacement((byte) '\t');
								break;
							case '\\':
								cm.replacement((byte) '\\');
								break;
							default:
								cm.cancelEscape();
								break;
						}
						this.custate = TFProcessor.ExplicitInstantV;
						continue;
					case Escape_Vcomp:
						switch (cudata) {
							case '\r':
							case '\n':
							case '\t':
							case ' ':
								break statematch;
							case 'r':
								cm.replacement((byte) '\r');
								break;
							case 'n':
								cm.replacement((byte) '\n');
								break;
							case 't':
								cm.replacement((byte) '\t');
							case '_':
								cm.replacement((byte) ' ');
								break;
							case '\\':
								cm.replacement((byte) '\\');
								break;
							default:
								cm.cancelEscape();

						}

						this.custate = TFProcessor.Vcomp;
						continue;
					case Escape_V4k:
						switch (cudata) {
							case '\r':
							case '\n':
							case '\t':
							case ' ':
								break statematch;
							case 'r':
								cm.replacement((byte) '\r');
								break;
							case 'n':
								cm.replacement((byte) '\n');
								break;
							case 't':
								cm.replacement((byte) '\t');
							case '_':
								cm.replacement((byte) ' ');
								break;
							case '\\':
								cm.replacement((byte) '\\');
								break;
							default:
								cm.cancelEscape();

						}

						this.custate = TFProcessor.V4K;
						continue;
					case Start:
						switch (cudata) {
							case '\r':
							case '\n':
								break statematch;
							case '\t':
								this.level++;
								this.custate = TFProcessor.Level;
								continue;
							case '/':
								this.custate = TFProcessor.LabelOrComment;
								continue;
							case ':':
								this.custate = TFProcessor.JAttrLabelMark;
								continue;
							case '\\':
								cm.capture(i, -1, 1, true);
								this.custate = TFProcessor.Escape_txt_estart;
								continue;
							default:
								//System.out.println((char)cudata);
								cm.capture(i, -1, -1, false);
								this.custate = TFProcessor.Txt;
								continue;
						}

					case Level:
						switch (cudata) {
							case '\r':
							case '\n':
								break statematch;
							case '\t':
								this.level++;
								continue;
							default:
								if (this.commentsLevel >= 0 && this.level > this.commentsLevel) {

									if (this.iscommentsIgnored) {
										this.custate = TFProcessor.Commentsgnored;
									} else {

										this.ebd.token(EBX.cLevel, this.level);
										cm.capture(i, -1, -1, false);
										this.custate = TFProcessor.Commonts;
										// System.out.println("culevel:" +
										// this.level);
										// System.out.println("comlevel:"+
										// this.commentsLevel);

									}

								} else if (this.ignorLevel >= 0 && this.level > this.ignorLevel) {
									this.custate = TFProcessor.HdlIgnored;
								} else {
									// System.out.println("this.ignorLevel:__"+this.ignorLevel);
									// System.out.println("this.culevel:__"+this.level);
									this.ebd.token(EBX.cLevel, this.level);
									switch (cudata) {
										case '/':
											this.custate = TFProcessor.LabelOrComment;
											break;
										case ':':
											// this.ebd.token(EBX.cJLabelMark,
											// null);
											this.custate = TFProcessor.JAttrLabelMark;
											break;

										case '\\':
											//System.out.println("level here?");
											cm.capture(i, -1, 1, true);
											this.custate = TFProcessor.Escape_txt_estart;
											break;
										default:
											cm.capture(i, -1, 1, false);
											this.custate = TFProcessor.Txt;
											break;
									}
									if (this.ignorLevel >= 0 || this.commentsLevel >= 0){ // in case all txt is whitespace ,you can only know when it come to the end of line even.
										
										switch(cudata){
											case '/':
											case ':':
											case '\\':
												if (this.commentsLevel >= 0)
													this.commentsLevel = -1;
												if (this.ignorLevel >= 0)
													this.ignorLevel = -1;
												this.levelneedenclose = false;
												break;
											default:
												this.levelneedenclose = true;
												break;
										}
										

									}

								}
								continue;
						}

				}
				switch (cudata) {
					default:
						switch (lcustat) {
//							case Vcomp:
//							case V4K:
//							case ExplicitInstantV:
//								cm.forward();
//								continue;

							
							case KeyMark:
								cm.capture(i, -1, 1, false);
								this.custate = TFProcessor.V4K;
								continue;
							case InstanVMark:
								cm.capture(i, -1, 1, false);
								this.custate = TFProcessor.ExplicitInstantV;
								continue;

						}
						continue;
					case '\t':
					case ' ':
						switch (lcustat) {
							case JAttrName:

								this.ebd.token(EBX.cJKeyName, cm.extract());
								this.custate = TFProcessor.JWhiteSpace;
								continue;
								
							case JAttrLabelMark:
								this.ebd.token(EBX.cJKeyName, null);
								this.custate = TFProcessor.JWhiteSpace;
								continue;
							case LabelOrComment:
								System.out.println("here?");
								this.ebd.token(EBX.cEleName, null);
								this.custate = TFProcessor.EWhiteSpace;
								continue;
							case LabelName:
								this.ebd.token(EBX.cEleName, cm.extract());
								this.custate = TFProcessor.EWhiteSpace;
								continue;
							case InstanVMark:
							case KeyMark:
								this.custate = TFProcessor.EWhiteSpace;
								continue;
							case Escape_Vcomp:
							case Escape_InstantV:
								cm.cancelEscape();
								this.ebd.token(EBX.cInstV, cm.extract());
								this.custate = TFProcessor.EWhiteSpace;
								continue;
							case Escape_V4k:
								cm.cancelEscape();
								this.ebd.token(EBX.cPropV, cm.extract());
								this.custate = TFProcessor.EWhiteSpace;
								continue;
								//System.out.println("here? Escape_V4k");
							case ExplicitInstantV:
							case TFProcessor.Vcomp:
								this.ebd.token(EBX.cInstV, cm.extract());
								this.custate = TFProcessor.EWhiteSpace;
								continue;
							case V4K:
								this.ebd.token(EBX.cPropV, cm.extract());
								this.custate = TFProcessor.EWhiteSpace;
								continue;
						}
						//System.out.println("leak1:_"+cudata+"   "+lcustat);
						//continue;
					case '\\':
						switch (lcustat) {
							case Vcomp:
								cm.openEscape(i);
								this.custate = TFProcessor.Escape_Vcomp;
								continue;
							case EWhiteSpace:
								//System.out.println("EWhiteSpaceW");
								cm.capture(i, -1, 1, true);
								this.custate = TFProcessor.Escape_Vcomp;
								continue;
//							case LabelName:
//								cm.forward();
//								continue;
							case InstanVMark:
								cm.capture(i, -1, 1, true);
								this.custate = TFProcessor.Escape_InstantV;
								continue;
							case KeyMark:
								cm.capture(i, -1, 1, true);
								this.custate = TFProcessor.Escape_V4k;
								continue;
							case V4K:
								cm.openEscape(i);
								this.custate = TFProcessor.Escape_V4k;
								continue;
							case ExplicitInstantV:
								cm.openEscape(i);
								this.custate = TFProcessor.Escape_InstantV;
								continue;
							case Txt:
								//System.out.println();
								System.out.println("here open es txt:_"+cudata+"   "+lcustat);
								cm.openEscape(i);
								this.custate = TFProcessor.Escape_txt;
								continue;

						}
						System.out.println("no match:_"+cudata+"   "+lcustat);
						continue;
					case '\r':
					case '\n':
						
						this.charNum=0;
						this.level = 0;
						switch (cudata) {
							case '\r':
								this.custate = TFProcessor.PreLineEnd;
								break;
							case '\n':
								this.line++;
								this.custate = TFProcessor.Start;
								break;
						}

						switch (lcustat) {
//							case Start:
//							case HdlIgnored:
//							case Commentsgnored:
//							case Level:
//								break;

							case TFProcessor.PreLineEnd:
								switch (cudata) {
									default:
										throw new RuntimeException("\\r must lead to \\n.");
									case '\n':
										break;
								}
								break;
							case JWhiteSpace:
								this.ignorLevel = this.ebd.token(EBX.cJEEnd, null);
								break;
							case JAttrLabelMark:
								this.ignorLevel = this.ebd.token(EBX.cJEEnd, null);
								break;

							case JAttrName:
								this.ebd.token(EBX.cJKeyName, cm.extract());
								this.ignorLevel = this.ebd.token(EBX.cJEEnd, null);
								break;
							case JInstantv:
								this.ebd.token(EBX.cJInstant, cm.extract());
								this.ignorLevel = this.ebd.token(EBX.cJEEnd, null);
								break;
							case Escape_txt_estart:
							case Escape_txt:
								cm.cancelEscape();
								this.ignorLevel = this.ebd.token(EBX.cTxt, cm.extract());
								break;
	
							case Txt:
								//System.out.println("here allwt txt");
								if (!cm.isAllWhitespace())
									this.ignorLevel = this.ebd.token(EBX.cTxt, cm.extract());
								else
									this.levelneedenclose = false;
								break;
							case PreCommonts:
								this.ignorLevel = this.ebd.token(EBX.cConmment, null);
								break;
							case Commonts:
								//System.out.println("here allwt comments");
								if (!cm.isAllWhitespace())
									this.ignorLevel = this.ebd.token(EBX.cConmment, cm.extract());
								else
									this.levelneedenclose = false;
								break;

							case LabelName:

								this.ebd.token(EBX.cEleName, cm.extract());
								this.ignorLevel = this.ebd.token(EBX.cEleEnd, null);
								break;
							case LabelOrComment:
								this.ebd.token(EBX.cEleName, null);
							case EWhiteSpace:
							case InstanVMark:
							case KeyMark:
								this.ignorLevel = this.ebd.token(EBX.cEleEnd, null);
								break;
							case Escape_V4k:
								cm.cancelEscape();
							case V4K:
								this.ebd.token(EBX.cPropV, cm.extract());
								this.ignorLevel = this.ebd.token(EBX.cEleEnd, null);
								break;
							case Escape_InstantV:
							case Escape_Vcomp:
								cm.cancelEscape();	 
							case ExplicitInstantV:
							case Vcomp:
								this.ebd.token(EBX.cInstV, cm.extract());
								this.ignorLevel = this.ebd.token(EBX.cEleEnd, null);
								break;

						}
						if (this.levelneedenclose) {// in case all txt is whitespace ,you can only know when it come to the end of line even.
							// System.out.println("level need enclose.");
							//System.out.println("this.ignoreelvel:__" + this.ignorLevel);
							if (this.commentsLevel >= 0)
								this.commentsLevel = -1;
							if (this.ignorLevel >= 0)
								this.ignorLevel = -1;
							this.levelneedenclose = false;
						}

						continue;

				}
			}

			if ( start ==  end &&  end == -1) {
				//System.out.println("here? scanner sEventStreamEnd ");
				this.ebd.token(EBX.sEventStreamEnd, null);
			}
		} catch (Exception e) {
			throw new RuntimeException("line:_" + (this.line+1) + "___char:_" + this.charNum, e);
		}

		return this;

	}



	// private void dealevel(CaptureManager cm, int i, byte cudata) {
	//
	// }

	// public static void main(String[] args) {
	// TFProcessor tfp = new TFProcessor();
	// tfp.scan(new byte[]{'\n'}, cm, paramsreg)
	// }
}
