package queryhandler;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import queryhandler.sockethandler.Utils;

public class Config {

	private static QueryHandler plugin;
	public static void setPlugin(QueryHandler plugin) {
		Config.plugin = plugin;
	}

	private static String  queryAddress = "0.0.0.0";
	private static int     queryPort    = 25565;
	private static String  motd         = Bukkit.getMotd();
	private static int     maxPlayers   = Bukkit.getMaxPlayers();
	private static String  version      = Utils.getVersion();
	private static boolean sendPlugins  = true;

	public static void loadConfig() {
		File configfile = new File(plugin.getDataFolder(), "config.yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(configfile);
		queryAddress = config.getString ("listener.address", queryAddress);
		queryPort    = config.getInt    ("listener.port",    queryPort);
		motd         = config.getString ("info.motd",        motd);
		maxPlayers   = config.getInt    ("info.maxplayers",  maxPlayers);
		sendPlugins  = config.getBoolean("info.plugins",     sendPlugins);
		version      = config.getString ("info.version",     version);
		config.set("listener.address", queryAddress);
		config.set("listener.port",    queryPort);
		config.set("info.motd",        motd);
		config.set("info.maxplayers",  maxPlayers);
		config.set("info.plugins",     sendPlugins);
		config.set("info.version",     version);
		try {
			config.save(configfile);
		} catch (IOException e) {
		}
	}

	public static String getQueryAddress() {
		return queryAddress;
	}

	public static int getQueryPort() {
		return queryPort;
	}

	public static final String getMotd() {
		return motd;
	}

	public static int getMaxPlayers() {
		return maxPlayers;
	}

	public static boolean isSendingPluginsEnabled() {
		return sendPlugins;
	}

	public static String getVersion() {
		return version;
	}

}
