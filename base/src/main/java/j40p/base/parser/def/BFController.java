package j40p.base.parser.def;

import j40p.base.parser.def.ex.ScanningLimitException;

import java.nio.ByteBuffer;

public interface BFController {
	 ByteBuffer intaking(ByteBuffer lbf,  int lastread ) throws ScanningLimitException;
}
