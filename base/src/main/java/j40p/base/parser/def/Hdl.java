package j40p.base.parser.def;

import j40p.base.UTF8ByteStr;
import j40p.base.l.Func;
import j40p.base.parser.def.ex.CancelScanningException;

public interface Hdl {
	int token(int context, UTF8ByteStr data) throws CancelScanningException;
	<TO,TI> TO token(Func<TO,TI> context,TI data )throws CancelScanningException;
	

}
