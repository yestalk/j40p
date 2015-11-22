package j40p.jjson.json;

import j40p.jjson.json.Json.Jmd;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;

class JsonToolBoximpl3_old implements JsonToolBox {

	@Override
	public Json getJson() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object fromUtf8JsJson(byte[] jsdata) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] toUtf8JsObjArray(Object objs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public byte[] toUtf8JsJson(Json json) {
		// TODO Auto-generated method stub
		return null;
	}

	static class JimplHandler implements InvocationHandler {
		static HashMap<Method, Jmd> mdmap = new HashMap<Method,Jmd>();
		
		HashMap<Object, Object> jsonmap = new HashMap<Object,Object>();
		
		static {
			Jmd[] jmds = Jmd.values();
			HashMap<String, Jmd> jmdmap = new HashMap<String,Jmd>();
			for (Jmd i : jmds) {
				jmdmap.put(i.name(), i);
			}
			Method[] md = Json.class.getMethods();
			String mname = null;
			for (Method i : md) {
				mname = i.getName();
				Jmd cpmd = jmdmap.get(mname);
				if (cpmd != null)
					mdmap.put(i, cpmd);

			}
		}

		@Override
		public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
			Object key = args[0];
			Boolean asklist = null;
			Boolean getop = null;
			// boolean needbreak = false;
			if (key == null)
				throw new RuntimeException("key can not be null.");
			switch (JimplHandler.mdmap.get(method)) {
				case getData:
					return this.jsonmap.get(key);
				case setData:
					this.jsonmap.put(key, args[1]);
					return null;
				case getBoolean:
				case getInt:
				case getString:
				case getDouble:
				case getLong:
				case getFloat:
				case getJson:
					getop = (getop == null) ? true : getop;
				case setBoolean:
				case setInt:
				case setString:
				case setDouble:
				case setLong:
				case setFloat:
				case setJson:
					asklist = (asklist == null) ? false : asklist;
				case setJsonList:
				case setBooleanList:
				case setIntList:
				case setStringList:
				case setDoubleList:
				case setLongList:
				case setFloatList:
					getop = (getop == null) ? false : getop;
				case getBooleanList:
				case getIntList:
				case getStringList:
				case getDoubleList:
				case getLongList:
				case getFloatList:
				case getJsonList:
					getop = (getop == null) ? true : getop;
					asklist = (asklist == null) ? true : asklist;
					break;
				default:
					break;

			}
			Object value = null;
			boolean storedlist = false;
			if (getop) {
				value = this.jsonmap.get(key);
				if (value != null)
					storedlist = value.getClass().isArray();
			} else {
				this.jsonmap.put(key, args[1]);
				return null;
			}
			switch (JimplHandler.mdmap.get(method)) {
				case getData:
				case setData:
					break;
				case getBoolean:
					// case setBoolean:
				case getBooleanList:
					// case setBooleanList:
					if (asklist) { // list
						if (storedlist) {
							if (value instanceof String[]) {
								String[] strv = (String[]) value;
								if (strv.length > 0) {
									Boolean[] rttpv = new Boolean[strv.length];
									int ni = 0;
									for (String i : strv) {
										rttpv[ni++] = Boolean.valueOf(i);
									}
									return rttpv;
								} else
									return null;
							} else
								return (Boolean[]) value;
						} else {
							if (value instanceof String) {
								return new Boolean[] { Boolean
									.valueOf((String) value) };
							} else
								return new Boolean[] { (Boolean) value };
						}
					} else { // single
						if (storedlist) {
							if (value instanceof String[]) {
								String[] strv = (String[]) value;
								if (strv.length > 0)
									return Boolean
										.valueOf(((String[]) value)[0]);
								else
									return null;
							} else {
								Boolean[] rttpv = (Boolean[]) value;
								if (rttpv.length > 0)
									return ((Boolean[]) value)[0];
								else
									return null;
							}
						} else {
							if (value instanceof String) {
								return Boolean.valueOf((String) value);
							} else
								return (Boolean) value;
						}
					}

				case getInt:
					// case setInt:
				case getIntList:
					// case setIntList:
					if (asklist) { // list
						if (storedlist) {
							if (value instanceof String[]) {
								String[] strv = (String[]) value;
								if (strv.length > 0) {
									Integer[] rttpv = new Integer[strv.length];
									int ni = 0;
									for (String i : strv) {
										rttpv[ni++] = Integer.valueOf(i);
									}
									return rttpv;
								} else
									return null;
							} else
								return (Integer[]) value;
						} else {
							if (value instanceof String) {
								return new Integer[] { Integer
									.valueOf((String) value) };
							} else
								return new Integer[] { (Integer) value };
						}
					} else { // single
						if (storedlist) {
							if (value instanceof String[]) {
								String[] strv = (String[]) value;
								if (strv.length > 0)
									return Integer
										.valueOf(((String[]) value)[0]);
								else
									return null;
							} else {
								Integer[] rttpv = (Integer[]) value;
								if (rttpv.length > 0)
									return ((Integer[]) value)[0];
								else
									return null;
							}
						} else {
							if (value instanceof String) {
								return Integer.valueOf((String) value);
							} else
								return (Integer) value;
						}
					}

				case getString:
					// case setString:
				case getStringList:
					// case setStringList:
					if (asklist) { // list
						if (storedlist) {
							if (value instanceof String[]) {
								return value;
							} else {

								int vlength = Array.getLength(value);
								String[] strv = null;
								if (vlength > 0)
									strv = new String[vlength];
								else
									return new String[] {};
								for (int i = 0; i < vlength; i++) {
									strv[i] = Array.get(value, i).toString();
								}
								return strv;
							}
						} else {
							if (value instanceof String) {
								return value;
							} else {
								return value.toString();
							}
						}
					} else { // single
						if (storedlist) {
							if (value instanceof String[]) {
								String[] strv = (String[]) value;
								if (strv.length > 0)
									return Boolean
										.valueOf(((String[]) value)[0]);
								else
									return null;
							} else
								return ((Boolean[]) value)[0];
						} else {
							if (value instanceof String) {
								return Boolean.valueOf((String) value);
							} else
								return (Boolean) value;
						}
					}

				case getDouble:
					// case setDouble:
				case getDoubleList:
					// case setDoubleList:
					if (asklist) { // list
						if (storedlist) {
							if (value instanceof String[]) {
								String[] strv = (String[]) value;
								if (strv.length > 0) {
									Double[] rttpv = new Double[strv.length];
									int ni = 0;
									for (String i : strv) {
										rttpv[ni++] = Double.valueOf(i);
									}
									return rttpv;
								} else
									return null;
							} else
								return (Double[]) value;
						} else {
							if (value instanceof String) {
								return new Double[] { Double
									.valueOf((String) value) };
							} else
								return new Double[] { (Double) value };
						}
					} else { // single
						if (storedlist) {
							if (value instanceof String[]) {
								String[] strv = (String[]) value;
								if (strv.length > 0)
									return Double
										.valueOf(((String[]) value)[0]);
								else
									return null;
							} else {
								Double[] rttpv = (Double[]) value;
								if (rttpv.length > 0)
									return ((Double[]) value)[0];
								else
									return null;
							}
						} else {
							if (value instanceof String) {
								return Double.valueOf((String) value);
							} else
								return (Double) value;
						}
					}

				case getLong:
					// case setLong:
				case getLongList:
					// case setLongList:

					if (asklist) { // list
						if (storedlist) {
							if (value instanceof String[]) {
								String[] strv = (String[]) value;
								if (strv.length > 0) {
									Long[] rttpv = new Long[strv.length];
									int ni = 0;
									for (String i : strv) {
										rttpv[ni++] = Long.valueOf(i);
									}
									return rttpv;
								} else
									return null;
							} else
								return (Long[]) value;
						} else {
							if (value instanceof String) {
								return new Long[] { Long
									.valueOf((String) value) };
							} else
								return new Long[] { (Long) value };
						}
					} else { // single
						if (storedlist) {
							if (value instanceof String[]) {
								String[] strv = (String[]) value;
								if (strv.length > 0)
									return Long.valueOf(((String[]) value)[0]);
								else
									return null;
							} else {
								Long[] rttpv = (Long[]) value;
								if (rttpv.length > 0)
									return ((Long[]) value)[0];
								else
									return null;
							}
						} else {
							if (value instanceof String) {
								return Long.valueOf((String) value);
							} else
								return (Long) value;
						}
					}

				case getFloat:
					// case setFloat:
				case getFloatList:
					// case setFloatList:

					if (asklist) { // list
						if (storedlist) {
							if (value instanceof String[]) {
								String[] strv = (String[]) value;
								if (strv.length > 0) {
									Float[] rttpv = new Float[strv.length];
									int ni = 0;
									for (String i : strv) {
										rttpv[ni++] = Float.valueOf(i);
									}
									return rttpv;
								} else
									return null;
							} else
								return (Float[]) value;
						} else {
							if (value instanceof String) {
								return new Float[] { Float
									.valueOf((String) value) };
							} else
								return new Float[] { (Float) value };
						}
					} else { // single
						if (storedlist) {
							if (value instanceof String[]) {
								String[] strv = (String[]) value;
								if (strv.length > 0)
									return Float.valueOf(((String[]) value)[0]);
								else
									return null;
							} else {
								Float[] rttpv = (Float[]) value;
								if (rttpv.length > 0)
									return ((Float[]) value)[0];
								else
									return null;
							}
						} else {
							if (value instanceof String) {
								return Float.valueOf((String) value);
							} else
								return (Float) value;
						}
					}

				case getJson:
					// case setJson:
				case getJsonList:
					// case setJsonList:
					if (asklist) { // list
						if (storedlist) {
							return (Json[]) value;
						} else {
							return new Json[] { (Json) value };
						}
					} else { // single
						if (storedlist) {
							Json[] rttpv = (Json[]) value;
							if (rttpv.length > 0)
								return ((Json[]) value)[0];
							else
								return null;
						} else {
							return (Json) value;
						}
					}
				default:
					break;

			}
			return null;
		}

	}

}