package j40p.http.ins;

import j40p.base.UTF8ByteStr;
import j40p.base.commup.def.OutPutController;
import j40p.base.parser.def.ex.ParsingException;
import j40p.http.iface.HttpEvent;
import j40p.http.iface.HttpHdl;
import j40p.http.iface.Hvalue;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.LinkedList;

public class HttpMsgCenter implements HttpEvent {
	static File rootfolder;
	static byte[] nof404;

	static byte[] headerdata;
	static FileContent contentdata = new FileContent();
	static {
		StringBuilder sb = new StringBuilder();
		sb.append("HTTP/1.1 200 OK\r\n");
		sb.append("Connection:keep-alive\r\n");
		sb.append("Content-Type: text/html; charset=utf-8\r\n");
		sb.append("Content-Length: ");
		HttpMsgCenter.headerdata = sb.toString().getBytes(StandardCharsets.US_ASCII);

		sb = new StringBuilder();
		sb.append("HTTP/1.1 404 Not Found\r\n");
		sb.append("Connection:keep-alive\r\n");
		sb.append("Content-Type: text/html; charset=utf-8\r\n");
		sb.append("Content-Length: 0\r\n\r\n");
		HttpMsgCenter.nof404 = sb.toString().getBytes(StandardCharsets.US_ASCII);
	}

	public HttpMsgCenter() {

	}

	@Override
	public ArrayList<ByteBuffer> concluding(boolean[] shouldClosing) {
		//System.out.println("\r\nconcluding here?");

		if (this.nofd) {
			ArrayList<ByteBuffer> bf = new ArrayList<ByteBuffer>();
			bf.add(ByteBuffer.wrap(HttpMsgCenter.nof404));
			System.out.println("404 for ico responded.");
			return bf;

		}
		shouldClosing[0] = false;
		this.pathdisplay(this.contentdata);
		this.imgs(this.contentdata);
		
		byte[] sz = (this.contentdata.getSize() + "\r\n\r\n").getBytes(StandardCharsets.UTF_8);
		ArrayList<ByteBuffer> bf = this.contentdata.out();

		LinkedList<ByteBuffer> linbf = new LinkedList<ByteBuffer>();
		linbf.add(ByteBuffer.wrap(HttpMsgCenter.headerdata).asReadOnlyBuffer());
		linbf.add(ByteBuffer.wrap(sz));
		bf.addAll(0, linbf);
		return bf;
	}
	
	private  void imgs(FileContent fc){
		String[] imgfs = HttpMsgCenter.rootfolder.list(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				if(name.indexOf(".jpg")>=0){
					return true;
				}else if(name.indexOf(".gif")>=0){
					return true;
				}else if(name.indexOf(".png")>=0){
					return true;
				}
				return false;
			}
		});
		
		for(String i :imgfs){
			fc.put("<img src=\"/"+i+"\" /><br />");
		}
	}

	@Override
	public void setOutPutController(OutPutController out) {
		// TODO Auto-generated method stub

	}

	@Override
	public ArrayList<ByteBuffer> seeking(ParsingException error) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int putHttpVersion(UTF8ByteStr version) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int putHttpMethod(UTF8ByteStr data) {

		return 0;
	}

	boolean nofd = false;
	
	File cupath;

	@Override
	public int putPathComponent(UTF8ByteStr data) {
		
		
		if (data != null) {
			 
			if(this.cupath==null)
				this.cupath=new File(this.rootfolder, data.asString() );
			else
				this.cupath=new File(this.cupath,  data.asString() );
			 
			if (data.asString().equals("favicon.ico")) {
				this.nofd = true;
				System.out.println("here favicon detacted.");
				return 0;
			} else
				this.nofd = false;

			
			
		} else {
			
 
		}

		return 0;
	}
	
	private void pathdisplay(FileContent fc){
		if(this.cupath==null){
			System.out.println(HttpMsgCenter.rootfolder);
			fc.put(("path:_"+HttpMsgCenter.rootfolder.getAbsolutePath()).getBytes(StandardCharsets.UTF_8));
			fc.put("<br/>".getBytes(StandardCharsets.UTF_8));
		}else{
			fc.put(("path:_"+this.cupath.getAbsolutePath()).getBytes(StandardCharsets.UTF_8));
			fc.put("<br/>".getBytes(StandardCharsets.UTF_8));
		}
		
	}

	@Override
	public int putQueryPair(UTF8ByteStr key, UTF8ByteStr v) {
		HttpMsgCenter.contentdata.put("query:_");
		HttpMsgCenter.contentdata.put(key.getData());
		HttpMsgCenter.contentdata.put("::");
		HttpMsgCenter.contentdata.put(v.getData());
		HttpMsgCenter.contentdata.put("<br/>");
		return 0;
	}

	@Override
	public int signal(int sig) {
		switch(sig){
			case HttpEvent.FieldEnd:
				//new Throwable().printStackTrace();
				
				if(this.unfinishedfc!=null){
					
					try {
						this.unfinishedfc.force(true);
						this.unfinishedfc.close();
						
						this.unfinishedfc=null;
						//System.out.println("file finished.");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				break;
			case HttpEvent.Query_start:
				
				break;
			case HttpEvent.UrlEnd:
				break;
		}
		return 0;
	}

	@Override
	public int askHeaderTreatment(UTF8ByteStr hname) {

		return HttpHdl.policy_headerV_scann_tokenlized;
		// return HttpHdl.policy_headerV_scann_rough;
	}

	@Override
	public int putHeader(UTF8ByteStr hname, UTF8ByteStr v) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int putHeader(UTF8ByteStr hname, LinkedList<? extends Hvalue> v) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean keepBodyScanning(long contentlength, UTF8ByteStr contentType) {
		// TODO Auto-generated method stub
		return true;
	}

	int numbimg;
	
	FileChannel unfinishedfc;

	@Override
	public GatheringByteChannel conditonFile(UTF8ByteStr fieldName, UTF8ByteStr fileName, UTF8ByteStr MIMEcontentType) {
		String mime = MIMEcontentType.asString();
		String sufix = "";
		if (mime.indexOf("image/png") >= 0) {
			sufix = ".png";

		} else if (mime.indexOf("image/gif") >= 0) {
			sufix = ".gif";
		} else if (mime.indexOf("image/jpeg") >= 0) {
			sufix = ".jpg";
		}
		FileChannel fch = null;
		File nfl = new File(HttpMsgCenter.rootfolder, "temp" + (this.numbimg++) + sufix);
		try {
			nfl.createNewFile();
			fch = FileChannel.open(nfl.toPath(), StandardOpenOption.WRITE,StandardOpenOption.TRUNCATE_EXISTING);
			//System.out.println(nfl.getName()+" open");
		} catch (IOException e) {
			throw new RuntimeException(e);
			 
		}
		this.unfinishedfc=fch;
		return fch;
	}

	@Override
	public File getFile() {
	 
		return this.cupath;
	}

	public  static void setWorkingDir(File wkdir) {
		HttpMsgCenter.rootfolder=wkdir;
		
	}

}
