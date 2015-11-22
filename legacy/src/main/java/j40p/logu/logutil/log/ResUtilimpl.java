package j40p.logu.logutil.log;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.concurrent.ConcurrentHashMap;

import com.tibco.as.util.toolsPack.DefUtil;
import com.tibco.as.util.toolsPack.def.MID;

class ResUtilimpl implements ResUtil {
	private ResUtilimpl() {
	}

	private void setFinalStatic(Field field, Object newValue) throws Exception {
		field.setAccessible(true);
		Field modifiersField = Field.class.getDeclaredField("modifiers");
		modifiersField.setAccessible(true);
		modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
		field.set(null, newValue);
	}

	ConcurrentHashMap<Class<?>, Object> midIns = new ConcurrentHashMap<Class<?>, Object>();

	@Override
	public void initStaticStrNames(Class<?> clz) {
		Field[] fs = clz.getDeclaredFields();
		for (Field i : fs) {
			if (Modifier.isStatic(i.getModifiers())) {
				String name = i.getName();
				try {
					this.setFinalStatic(i, name);
					// System.out.println(i.get(null));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	public <T> T getMidIns(Class<T> midz) {
		while (true) {
			Object midi = this.midIns.putIfAbsent(midz,
				DefUtil.placeHolderObject);
			if (midi == null) {
				if (!(MID.class.isAssignableFrom(midz) && midz.isLocalClass()))
					throw new RuntimeException("wrong class type");

				try {
					Constructor<?> cs = null;
					try {
						cs = midz.getDeclaredConstructor(new Class<?>[] { midz
							.getEnclosingClass() });
					} catch (NoSuchMethodException e) {
						throw new RuntimeException(e);
					}
					// Constructor<?>[] css = midz.getDeclaredConstructors();
					// System.out.println(css.length);
					// Constructor<?> cs = css[0];
					// System.out.println(Arrays.toString(cs.getParameterTypes()));

					cs.setAccessible(true);
					midi = cs.newInstance(new Object[] { null });
					Field[] fs = midz.getDeclaredFields();
					// System.out.println(fs.length);
					Field.setAccessible(fs, true);
					for (Field i : fs) {
						Class<?> cutype = i.getType();
						Object cuvl = null;
						String cuname = i.getName();
						if (LogUtil.MsgLabel.class.isAssignableFrom(cutype)) {
							// i.setAccessible(true);
							if (LogUtil.EventGroup.class
								.isAssignableFrom(cutype)) {
								cuvl = LogUtil.i.getEventGroup(cuname);
							} else
								cuvl = LogUtil.i.getMsgLabel(cuname);
							if (i.get(midi) == null && cuvl != null) {
								i.set(midi, cuvl);
							}

						} else if (String.class.isAssignableFrom(cutype)) {
							if (i.get(midi) == null) {
								i.set(midi, cuname);
							}
						}
					}
					this.midIns.replace(midz, midi);
				} catch (InstantiationException e) {
					throw new RuntimeException(e);
				}catch(IllegalAccessException e){
					throw new RuntimeException(e);
				}catch(SecurityException e){
					throw new RuntimeException(e);
				}catch(IllegalArgumentException e){
					throw new RuntimeException(e);
				}catch(InvocationTargetException e){
					throw new RuntimeException(e);
				}
			} else if (midi == DefUtil.placeHolderObject) {
				Thread.yield();
			} else
				return (T) midi;
		}
	}
}
