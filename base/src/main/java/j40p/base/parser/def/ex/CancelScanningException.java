package j40p.base.parser.def.ex;

public class CancelScanningException extends RuntimeException{
	public CancelScanningException(String msg){super(msg);};
	public CancelScanningException(Exception e){super(e);};
}