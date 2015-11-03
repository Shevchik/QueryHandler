package queryhandler.api.datapool;

import java.util.Map;

import queryhandler.sockethandler.QueryDataPool;

public class Registry {

	public static void register(String key, ValueFunc func) {
		QueryDataPool.getInstance().addData(key, func);
	}

	public static void unregister(String key, ValueFunc func) {
		QueryDataPool.getInstance().removeData(key);
	}

	public static boolean isRegistered(String key) {
		return QueryDataPool.getInstance().hasData(key);
	}

	public static Map<String, ValueFunc> getRegistrations() {
		return QueryDataPool.getInstance().getDataPool();
	}

}
