package j40p.logu.logutil;

import j40p.logu.logutil.log.LogUtil;
import j40p.logu.logutil.log.ResUtil;
import j40p.logu.logutil.log.LogUtil.EventGroup;
import j40p.logu.logutil.log.LogUtil.Level;
import j40p.logu.logutil.log.LogUtil.Logger;
import j40p.logu.logutil.log.LogUtil.MsgLabel;

import com.tibco.as.util.toolsPack.def.MID;
import com.tibco.as.util.toolsPack.def.S;



public class LoggerTest {
	public static void main(String[] args) {
		LoggerTest lt = new LoggerTest();
		lt.doSomething();
		//lt.doSomething();
		//lt.doSomething();
	}
	
	public void doSomething(){class _mid implements MID{
		EventGroup mabc,mbbc;
		MsgLabel caseNumber,letbe,treenewbee;};
		_mid MK=ResUtil.i.getMidIns(_mid.class),LB=MK;
		
		
		Logger logger = LogUtil.i.getLogger(_mid.class);
		//LOGPOINT
		logger.c(Level.Warnning, _mid.class, MK.mbbc, 29,
			LB.letbe,"hehe\n999\r\nxxx1",
			LB.caseNumber,100,
			LB.treenewbee,88);
		//LOGPOINT
		S.instance.singleton(LogUtil.class).c(Level.Info, _mid.class, MK.mbbc,34, 
			LB.letbe,100);
		//LOGPOINT
		logger.c(Level.Info, _mid.class, MK.mbbc, 40,
			LB.letbe,S.instance.singleton(LogUtil.class)==LogUtil.i,
			LB.caseNumber,100,
			LB.treenewbee,88);
	}
}
