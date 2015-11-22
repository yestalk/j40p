package j40p.jjson.parser;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;

public class Channelhal {
	void process(ReadableByteChannel ch){
		
		NIOParser cuparser=null;
		ByteBuffer bf=null;
		int in=0;
		try {
			out:while(true){
				
				for(;(bf= cuparser.parse(bf))!=null && (in=ch.read(bf))>0;){
				
				}
				if(in==-1){
					//finished. // or connection closed
				}else if(in==0){
					break out; // wait for been selected again.
				}
				// implied bf==null &&
				bf = NIOParser.i.getRemaining();
				if((cuparser=cuparser.getNextParser())!=null  ){
					continue out;
				}else{ // no more parser
					
				}
						
			
			}	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

	}
		
}

