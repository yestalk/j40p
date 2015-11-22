package j40p.http.iface;

import j40p.base.UTF8ByteStr;

import java.util.Map;
import java.util.Set;

public interface Hvalue {
	UTF8ByteStr getValue();
	UTF8ByteStr getAttr(UTF8ByteStr key);
	Set<Map.Entry<UTF8ByteStr,UTF8ByteStr>> getEntrys();
}
