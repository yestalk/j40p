package j40p.base.commup.def;

import j40p.base.parser.def.ex.ParsingException;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public interface InteractiveContext {
	void setOutPutController(OutPutController out);
	ArrayList<ByteBuffer> seeking(ParsingException error);
}
