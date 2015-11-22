package j40p.logu.log;
import java.util.Date;
import java.util.List;




public interface LogUtil {
	LogUtil i = new Default();
	enum Level{
		Off,
		Trace,
		Debug,
		Info,
		Warnning,
		Fatal,
		Sever
	};
	enum Service{
		Time,
		Location
		
	};
	class LoggerConf{
		String name;
		Level level;
		List<Appender> appenders;
	}
	interface Appender{
		void append(Date time,String loc,Object... msg);
	}
	void log(Class<? extends MID> mid,Level level,Object... msgs) throws SomeException;
	void log(Object context,Level level,Object... msgs);
	void log(Object context,Class<? extends MID> mid,Level level,Object... msgs);
	
	void log(String name,Level level,Object...  msgs);
	
	
	

	//void log(Object context,MID mid,Level level,Object... msgs);
	//void log(Object context,Class<?> clz,Level level,Object... msgs);
	class Default implements LogUtil {private Default(){};
		public static void main(String[] args) {
			class $mid implements MID{};
			LogUtil.i.log($mid.class, Level.Debug, "/formatStr","hehe","xxx");
		}

		@Override
		public void log(Class<? extends MID> mid, Level level, Object... msgs)
				throws SomeException {
			// TODO Auto-generated method stub
			
		}



		@Override
		public void log(String name, Level level, Object... msgs) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void log(Object context, Class<? extends MID> mid, Level level,
				Object... msgs) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void log(Object context, Level level, Object... msgs) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
