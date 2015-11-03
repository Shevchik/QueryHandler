package queryhandler.sockethandler;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import queryhandler.api.datapool.ValueFunc;

public class QueryDataPool {

	private static final QueryDataPool instance = new QueryDataPool();

	public static QueryDataPool getInstance() {
		return instance;
	}

	private final ConcurrentHashMap<String, ValueFunc> datapool = new ConcurrentHashMap<>();

	public boolean hasData(String key) {
		return datapool.containsKey(key);
	}

	public void addData(String key, ValueFunc value) {
		datapool.put(key, value);
	}

	public void removeData(String key) {
		datapool.remove(key);
	}

	public LinkedHashMap<String, ValueFunc> getDataPool() {
		return new LinkedHashMap<>(datapool);
	}

	public void append(QueryResponse response, byte[] payload) throws IOException {
		response.write("queryhandler");
		response.write(datapool.size());
		for (Entry<String, ValueFunc> entry : datapool.entrySet()) {
			response.write(entry.getKey());
			response.write(entry.getValue().get(payload));
		}
	}

}
