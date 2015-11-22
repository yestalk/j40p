package j40p.http.ins;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class FileContent {
	
	private static ByteBuffer b1,b2;
	 
	
	public static void filec(File f){
		try {
			FileInputStream hehe = new FileInputStream(f);
			ByteArrayOutputStream lbao=new ByteArrayOutputStream();
			int bx=-1;
			while((bx=hehe.read())>=0){
				lbao.write(bx);
			}
			hehe.close();
			String xstr = lbao.toString(StandardCharsets.UTF_8.name());
			lbao.close();
			String[] xstrs=xstr.split("\\{replacement here!\\}");
			FileContent.b1=ByteBuffer.wrap (xstrs[0].getBytes(StandardCharsets.UTF_8));
			FileContent.b2=ByteBuffer.wrap (xstrs[1].getBytes(StandardCharsets.UTF_8));
		} catch (FileNotFoundException e) {
 
			e.printStackTrace();
		} catch (IOException e) {
 
			e.printStackTrace();
		}
	}
	
	ByteArrayOutputStream bao=new ByteArrayOutputStream();
	public void  put(byte[] data){
		try {
			this.bao.write(data);
		} catch (IOException e) {
			 
			e.printStackTrace();
		}
	}
	public void  put(byte data){
		this.bao.write(data);
	}

	public void  put(String data){
		try {
			this.bao.write(data.getBytes(StandardCharsets.UTF_8));
		} catch (IOException e) {
			 
			e.printStackTrace();
		}
	}
	
	public int getSize(){
		return FileContent.b1.remaining() + FileContent.b2.remaining()+this.bao.size();
	}
	
	public ArrayList<ByteBuffer> out(){
		 ArrayList<ByteBuffer> abl = new ArrayList<ByteBuffer>(3);
		 abl.add(FileContent.b1.asReadOnlyBuffer());
		 abl.add(ByteBuffer.wrap(this.bao.toByteArray()));
		 abl.add(FileContent.b2.asReadOnlyBuffer());
		
		 try {
			this.bao.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 this.bao=new ByteArrayOutputStream();
		 return abl;
	}
}
