package j40p.logu.logutil.log;

import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import com.tibco.as.util.toolsPack.DefUtil;
import com.tibco.as.util.toolsPack.def.MID;

class LogUtilimpl implements LogUtil {
	private LogUtilimpl() {
		// ... read conf file to initialize Appenders and Loggers
		if (this.rootLogger == null) {
			DefaultLogger rootl = new DefaultLogger();
			rootl.name = "";
			rootl.level = Level.Info;
			rootl.appenders = new LinkedList<Appender>();
			rootl.svs = new TreeSet<Service>();
			rootl.svs.add(Service._Time);
			rootl.svs.add(Service._Location);
			DefaultAppender dapd = new DefaultAppender();
			dapd.setName("");
			rootl.appenders.add(dapd);
			this.rootLogger = rootl;
		}
		Thread cmter = new Thread(new Committer());
		cmter.setName("loggingMsgCommitor");
		// cmter.setDaemon(true);
		cmter.start();

	}

	public class DefaultLogger implements Logger {
		private String name;
		private Level level;
		private List<Appender> appenders;
		private Set<Service> svs;

		@Override
		public void setSeveric(TreeSet<Service> sv) {
			this.svs = sv;

		}

		public void c(Level level, Class<? extends MID> midz,
			EventGroup eventGroup, int lineNumber, Object... msgs) {
			int loggerl = this.level.ordinal();
			int loggingPointl = level.ordinal();
			if (loggerl != Level.Off.ordinal() && loggerl <= loggingPointl) {
				// System.out.println("::::"+msgs.length);
				// do commit to appender queue.
				String[] lmsgs = new String[msgs.length];
				LinkedList<String> ufi = new LinkedList<String>();
				LinkedList<Object> loginfo = new LinkedList<Object>();
				ufi.add(midz.getEnclosingClass().getName());
				ufi.add(midz.getEnclosingMethod().getName());
				ufi.add(eventGroup.name);
				if (msgs.length % 2 != 0)
					throw new RuntimeException("wrong logging msg format.");
				for (Service i : this.svs) {
					switch (i) {
						case _Location:
							ufi.add(i.name());
							// new Throwable().printStackTrace();
							StackTraceElement[] ste = Thread.currentThread()
								.getStackTrace();
							String caller = ste[2].toString();
							if (caller.startsWith("log.LogUtilimpl.c("))
								caller = ste[3].toString();
							loginfo.add(caller);
							break;
						case _Time:
							ufi.add(i.name());
							loginfo.add(new Date());
							break;
					}
				}
				int ni = 0;
				for (Object i : msgs) {
					if (ni % 2 == 0) {
						if (i instanceof MsgLabel)
							ufi.add(((MsgLabel) i).name);
						else
							throw new RuntimeException(
								"wrong logging msg label format.");
					} else
						loginfo.add(i);
					ni++;
				}

				ufi.add(Integer.toString(lineNumber));
				ufi.add(level.name());
				for (Appender i : appenders) {
					try {
						LogUtilimpl.this.loggingQueue.put(new Object[] { i,
							ufi, loginfo.toArray() });
					} catch (InterruptedException e) {
						break;
					}
				}
			}
		}
	}

	static class DefaultFormatter implements Formatter {

		@Override
		public char[] format(List<String> UFI, Object[] msgs) {
			String[] lbs = UFI.toArray(DefUtil.stringArray);
			StringBuffer strbf = new StringBuffer();

			String javafile = lbs[0];
			int begain = javafile.lastIndexOf('.') + 1;
			int end = javafile.lastIndexOf('$') - 1;
			end = (end < 0) ? javafile.length() : end;
			javafile = javafile.substring(begain, end);

			strbf.append('[').append(lbs[lbs.length - 1]).append("] ")
				.append(lbs[0]).append(".").append(lbs[1]).append("(")
				.append(javafile).append(".java:").append(lbs[lbs.length - 2])
				.append(")").append(":\n");

			int ni = 0;
			for (Object i : msgs) {
				String lb = lbs[ni + 3];
				String vl = null;

				vl = i.toString().replaceAll("\n", "\n\t> ");

				strbf.append(lb).append(":\n\t> ").append(vl).append("\n");
				ni++;
			}
			strbf.append("\n------reasonable-spliter--------\n");
			return strbf.toString().toCharArray();
		}

	}

	static class DefaultAppender implements Appender {
		String name;

		@Override
		public String getName() {
			return this.name;
		}

		@Override
		public void setName(String Name) {
			this.name = Name;

		}

		@Override
		public void append(List<String> UFI, Object[] msgs) {
			Formatter fmt = LogUtilimpl.i.getFormatter(UFI);
			System.out.println(fmt.format(UFI, msgs));
		}
	}

	class Committer implements Runnable {
		@Override
		public void run() {
			while (true) {
				try {
					Object[] msgs = loggingQueue.take();
					// System.out.println(msgs.length);
					Appender apd = (Appender) msgs[0];
					List<String> ufi = (List<String>) msgs[1];
					Object[] logmsg = (Object[]) msgs[2];
					apd.append(ufi, logmsg);
				} catch (InterruptedException e) {
					break;
				}
			}
		}
	}

	ConcurrentHashMap<Class<? extends MID>, Object> loggers = new ConcurrentHashMap<Class<? extends MID>, Object>();
	DefaultLogger rootLogger;
	Set<DefaultLogger> loggerSet = new HashSet<DefaultLogger>();
	// ConcurrentHashMap<Appender,LinkedBlockingQueue> appendingQueues = new
	// ConcurrentHashMap<>();
	LinkedBlockingQueue<Object[]> loggingQueue = new LinkedBlockingQueue<Object[]>(
		100000);
	ConcurrentHashMap<String, Object> eventlabels = new ConcurrentHashMap<String, Object>();
	ConcurrentHashMap<String, Object> msglabels = new ConcurrentHashMap<String, Object>();
	DefaultFormatter dff = new DefaultFormatter();

	@Override
	public void c(Level level, Class<? extends MID> midz,
		EventGroup eventGroup, int lineNumber, Object... msgs) {
		Logger logger = this.getLogger(midz);
		logger.c(level, midz, eventGroup, lineNumber, msgs);

	}

	@Override
	public Logger getLogger(Class<? extends MID> midz) {
		DefaultLogger logger = null;
		while (logger == null) {
			Object loggero = this.loggers.putIfAbsent(midz,
				DefUtil.placeHolderObject);
			if (loggero == null) {
				String lpointc = midz.getEnclosingClass().getName();
				String lpointm = midz.getEnclosingMethod().getName();
				String lpoint = lpointc + "::." + lpointm + "()";
				String culoggerName = null;
				for (DefaultLogger i : this.loggerSet) {
					String iname = i.name;
					if ((culoggerName == null || iname.startsWith(culoggerName))
						&& lpoint.startsWith(iname)) {
						logger = i;
						culoggerName = iname;
					}
				}
				if (logger == null)
					logger = this.rootLogger;
				this.loggers.replace(midz, logger);
			} else if (loggero == DefUtil.placeHolderObject) {
				Thread.yield();
			} else if (loggero instanceof DefaultLogger) {
				logger = (DefaultLogger) loggero;
			} else
				throw new RuntimeException("i can't understand this situation.");
		}
		return logger;

	}

	public EventGroup getEventGroup(String name) {
		EventGroup rt = null;
		while (true) {
			Object rto = this.eventlabels.putIfAbsent(name,
				DefUtil.placeHolderObject);
			if (rto == null) {
				rt = new EventGroup(name);
				this.eventlabels.replace(name, rt);
				return rt;
			} else if (rto instanceof EventGroup) {
				return (EventGroup) rto;
			} else if (rto == DefUtil.placeHolderObject) {
				Thread.yield();
			}
		}
	}

	public MsgLabel getMsgLabel(String name) {
		MsgLabel rt = null;
		while (true) {
			Object rto = this.msglabels.putIfAbsent(name,
				DefUtil.placeHolderObject);
			if (rto == null) {
				rt = new EventGroup(name);
				this.msglabels.replace(name, rt);
				return rt;
			} else if (rto instanceof EventGroup) {
				return (MsgLabel) rto;
			} else if (rto == DefUtil.placeHolderObject) {
				Thread.yield();
			}
		}
	}

	@Override
	public Formatter getFormatter(List<String> UFI) {
		// TODO not done yet.
		return this.dff;
	}

}
