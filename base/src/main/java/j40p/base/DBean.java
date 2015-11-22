package j40p.base;

import j40p.base.l.L;

import java.util.concurrent.ConcurrentHashMap;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public interface DBean {
	ToolBox t = new DT();

	<T_> void s(L<T_> label, T_ value);

	<T_> T_ g(L<T_> label);

	interface ToolBox {
		<T_ extends DBean> T_ create(Class<T_> iclz);
	}

	class DT extends ClassLoader implements ToolBox {
		private DT() {
		}

		private static ConcurrentHashMap<Class<?>, Class<?>> insmap = new ConcurrentHashMap<Class<?>, Class<?>>();
		private static String backbean= "impl/j/DBeani2";
		@Override
		public <T_ extends DBean> T_ create(Class<T_> iclz) {
			if (iclz.isInterface() && DBean.class.isAssignableFrom(iclz)) {
				Class<?> insc = insmap.get(iclz);
				if (insc == null) {
					synchronized (iclz) {
						insc = insmap.get(iclz);
						if (insc == null) {
							String cqn = iclz.getName().replace('.', '/');
							String binn = new StringBuilder().append("gimpl.").append(iclz.getName()).append(".I0").toString();
							String qn = binn.replace('.', '/');
							ClassWriter cw = new ClassWriter(0);
							cw.visit(Opcodes.V1_1, Opcodes.ACC_PUBLIC + Opcodes.ACC_SUPER, qn, null,DT.backbean, new String[] { cqn });
							{
								MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
								mv.visitCode();
								// Label l0 = new Label();
								// mv.visitLabel(l0);
								// mv.visitLineNumber(5, l0);
								mv.visitVarInsn(Opcodes.ALOAD, 0);
								mv.visitMethodInsn(Opcodes.INVOKESPECIAL, DT.backbean, "<init>", "()V", false);
								// Label l1 = new Label();
								// mv.visitLabel(l1);
								// mv.visitLineNumber(5, l1);
								mv.visitInsn(Opcodes.RETURN);
								// Label l2 = new Label();
								// mv.visitLabel(l2);
								// mv.visitLocalVariable("this", "Lexpri/I;",
								// null, l0, l2, 0);
								mv.visitMaxs(1, 1);
								mv.visitEnd();
							}
							cw.visitEnd();

							byte[] code = cw.toByteArray();
							insc = this.defineClass(binn, code, 0, code.length);
							insmap.put(iclz, insc);
						}

					}
				}

				try {
					// System.out.println("xi.i___"+XiI.class.getConstructors().length);
					// System.out.println("I0___"+insc.getConstructors().length);
					return (T_) insc.newInstance();

					// return (T_) insc.getConstructor(new Class<?>[]
					// {}).newInstance(new Object[] {});

				} catch (InstantiationException e) {
					throw new RuntimeException(e);

				} catch (IllegalAccessException e) {
					throw new RuntimeException(e);
				} catch (IllegalArgumentException e) {
					throw new RuntimeException(e);
				} catch (SecurityException e) {
					throw new RuntimeException(e);
				}

			} else
				throw new RuntimeException("not a dbean interface" + iclz.getName());

		}
	}
}
