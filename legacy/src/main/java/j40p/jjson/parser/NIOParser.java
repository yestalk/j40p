package j40p.jjson.parser;

import java.nio.ByteBuffer;

public interface NIOParser {
	NIOParser i = null;
	ByteBuffer parse(ByteBuffer data);

	ByteBuffer getRemaining();

	NIOParser getNextParser();

}