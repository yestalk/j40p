package expri.toolc;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.CharBuffer;

public class HttpDumper {
	static int hexup = (byte) 0x000f;
	static char[] hexmap = new char[] { '0', '1', '2', '3', '4', '5', '6', '7',
			'8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static void main(String[] args) throws IOException {
		ServerSocket sss= new ServerSocket(80);
		System.out.println(sss.getInetAddress());
		int hehebfsz=4096;
		int cd = 0;
		byte[] bf = new byte[hehebfsz];
		CharBuffer btbf = CharBuffer.allocate(hehebfsz);
		while(true){

			Socket skt = sss.accept();
			InputStream fin = skt.getInputStream();
			int data ;
			while ((cd = fin.read(bf)) != -1) {

				for (int i = 0, l = cd - 1; i <= l; i++) {

					int d = bf[i]&255;
					switch (d) {
						case '\r':
						case '\n':
							btbf.put((char) d);
							break;
						default:

							if (d < 32 || d == 127) {

								btbf.put('_');

							} else if (d > 127) {

								btbf.put(hexmap[d >> 4]);
								//btbf.put(hexmap[d & hexup]);

							} else {
								btbf.put((char) d);
							}
					}
					if (btbf.remaining() < 2) {
						//System.out.println("!!!!!remaining:__"+btbf.remaining());
						btbf.flip();
						
					
						System.out.append(btbf);
						btbf.clear();
							
						//System.out.println("\r\n____!!!!!remaining:__"+btbf.remaining());
						
					}
				}
				if(btbf.length()>0){
					//System.out.println("\r\n!!!!!belen:__"+btbf.length());
					btbf.flip();
					
					
					System.out.append(btbf);
					btbf.clear();
					
					//System.out.println("\r\n!!!!!len:__"+btbf.length());
					//System.out.println("\r\n!!!!!remaining:__"+btbf.remaining());
				}
				

			}
			fin.close();
		}
		
//		FileInputStream fin = new FileInputStream(new File(
//				"D:\\p\\dloaded\\JavaFixTrader.zip"));

		

		

	}
}
