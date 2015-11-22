package j40p.base;

import j40p.base.l.DRes;
import j40p.base.l.Func;
import j40p.base.l.Ls;
import j40p.base.l.P;
import j40p.base.l.Res;




public interface TypeUtil {
	TypeUtil i = S.i.ngleton(TypeUtil.class);
	
	<T_ extends P<?>> T_ defProperty(T_ nom);
	<T_ extends Ls<?>> T_ defList(T_ nom);
	<T_ extends Func<?,?>> T_ deFunc(T_ nom);
	<T_ extends Res<?>> T_ defResPoint(T_ nom);
	<T_ extends DRes<?>> T_ defDResPoint(T_ nom);
	
}
