package queryhandler;

import java.net.InetAddress;

import org.bukkit.plugin.java.JavaPlugin;

import queryhandler.sockethandler.QueryListenerThread;

public class QueryHandler extends JavaPlugin {

	private QueryListenerThread thread;

	@Override
	public void onEnable() {
		try {
			Config.setPlugin(this);
			Config.loadConfig();
			getCommand("queryhandler").setExecutor(new Commands());
			thread = new QueryListenerThread(InetAddress.getByName(Config.getQueryAddress()), Config.getQueryPort());
			thread.start();
		} catch (Throwable t) {
			System.err.println("Failed to launch query");
			t.printStackTrace();
		}
	}

	@Override
	public void onDisable() {
		thread.shutdown();
		try {
			thread.join(2000);
		} catch (InterruptedException e) {
		}
	}

}
