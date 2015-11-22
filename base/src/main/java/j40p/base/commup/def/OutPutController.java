package j40p.base.commup.def;

import java.nio.ByteBuffer;
import java.util.ArrayList;


public interface OutPutController {

	
	void put(ArrayList<ByteBuffer> bflist);
	void putAndFinish(ArrayList<ByteBuffer> bflist);
	 
}
