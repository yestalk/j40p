package j40p.base;

import impl.j40p.base.DBeani2;
import j40p.base.l.L;

public interface ConfBean {
	ToolBox t = new D();

	<T_> void s(L<T_> label, T_ value);

	<T_> T_ g(L<T_> label);

	interface ToolBox {

		ConfBean create(Class<?> clz);

	}

	class D implements ToolBox {
		private D() {

		}

		@Override
		public ConfBean create(Class<?> clz) {
			// TODO Auto-generated method stub
			return new DBeani2.ConfBeani(clz);
		}

	}

}
