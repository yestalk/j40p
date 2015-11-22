package j40p.http.impl;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class HttpFileServ {
	public byte[] serv(File f){
		
		if(!f.exists()){
			ByteArrayOutputStream bao = new ByteArrayOutputStream(5000);
			PrintStream psm;
			try {
				psm = new PrintStream(bao,true,StandardCharsets.US_ASCII.name());
				psm.print("HTTP/1.1 404 Not Found\r\n");
				psm.print("Connection:keep-alive\r\n\r\n");
				psm.close();
				return bao.toByteArray();
			}catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
		}
		
		ByteArrayOutputStream bao = new ByteArrayOutputStream(5000);
		PrintStream psm;
		try {
			psm = new PrintStream(bao,true,StandardCharsets.US_ASCII.name());
			psm.print("HTTP/1.1 200 OK\r\nConnection:keep-alive\r\nContent-Length: "+f.length()+"\r\nContent-Type: ");
			String sufix = f.getName().split("\\.")[1];
			if(sufix.equals("png")){
				psm.print("image/png");
			}else if(sufix.equals("jpg")){
				psm.print("image/jpeg");
			}else if(sufix.equals("gif")){
				psm.print("image/gif");
			} 
			psm.print("\r\n\r\n");
			psm.flush();
			FileInputStream finput= new FileInputStream(f);

			HttpFileServ.copyStream(finput, bao);
			finput.close();
			psm.close();
			return bao.toByteArray();
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		 

		 

		return null;
	}
	
	public static void copyStream(InputStream input, OutputStream output)
		    throws IOException
		{
		    byte[] buffer = new byte[1024]; // Adjust if you want
		    int bytesRead;
		    while ((bytesRead = input.read(buffer)) != -1)
		    {
		        output.write(buffer, 0, bytesRead);
		    }
		}
}
