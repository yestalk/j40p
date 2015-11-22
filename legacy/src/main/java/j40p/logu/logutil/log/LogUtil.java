package j40p.logu.logutil.log;
import j40p.logu.logutil.log.LogUtil.Service;

import java.io.OutputStream;
import java.nio.CharBuffer;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import javax.print.attribute.standard.Severity;

import com.tibco.as.util.toolsPack.DefUtil;
import com.tibco.as.util.toolsPack.def.MID;


public interface LogUtil {
	LogUtil i = S.i.ngleton(LogUtil.class);
	enum Level{
		Trace,
		Debug,
		Info,
		Warnning,
		Severe,
		Fatal,
		Off
	};
	enum Service{
		_Time,
		_Location;
	};
	class MsgLabel{
		String name;
		MsgLabel(String name){
			this.name=name;
		}
		@Override
		public boolean equals(Object obj) {
			if(obj instanceof MsgLabel)
				return this.name.equals(obj);
			else
				return false;
		}

		@Override
		public int hashCode() {
			return this.name.hashCode();
		}

		@Override
		public String toString() {
			return this.name;
		}
		
	}
	class EventGroup extends MsgLabel{
		EventGroup(String name){
			super(name);
		}
	}
	
	interface Appender{
		String getName();
		void setName(String Name);
		void append(List<String> UFI,Object[] msgs);// UFI stand for universal formatter identifier
		//void append(char[] msg,int start,int offset);
	}
	interface Formatter{
		char[] format(List<String> UFI,Object[] msgs);
		//void format(OutputStream dest,String[] msgs);
	}
	interface Logger{
		void setSeveric(TreeSet<Service> sv);
		void c(Level level,Class<? extends MID> midz,EventGroup eventGroup,int lineNumber,Object... msgs);
	}
	// c stand for "commit".
	void c(Level level,Class<? extends MID> midz,EventGroup eventGroup,int lineNumber,Object... msgs);
	Logger getLogger(Class<? extends MID> midz);
	
	MsgLabel getMsgLabel(String name);
	EventGroup getEventGroup(String name);
	Formatter getFormatter(List<String> UFI);

	
}
