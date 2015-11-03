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

	private static String queryaddr = "0.0.0.0";
	private static int queryport = 25565;
	private static String motd = Bukkit.getMotd();
	private static int maxplayers = Bukkit.getMaxPlayers();
	private static String version = Utils.getVersion();
	private static boolean sendplugins = true;

	public static void loadConfig() {
		File configfile = new File(plugin.getDataFolder(), "config.yml");
		YamlConfiguration config = YamlConfiguration.loadConfiguration(configfile);
		queryaddr = config.getString("listener.address", queryaddr);
		queryport = config.getInt("listener.port", queryport);
		motd = config.getString("info.motd", motd);
		maxplayers = config.getInt("info.maxplayers", maxplayers);
		sendplugins = config.getBoolean("info.plugins", sendplugins);
		version = config.getString("info.version", version);
		config.set("listener.address", queryaddr);
		config.set("listener.port", queryport);
		config.set("info.motd", motd);
		config.set("info.maxplayers", maxplayers);
		config.set("info.plugins", sendplugins);
		config.set("info.version", version);
		try {
			config.save(configfile);
		} catch (IOException e) {
		}
	}

	public static String getQueryAddress() {
		return queryaddr;
	}

	public static int getQueryPort() {
		return queryport;
	}

	public static final String getMotd() {
		return motd;
	}

	public static int getMaxPlayers() {
		return maxplayers;
	}

	public static boolean isSendingPluginsEnabled() {
		return sendplugins;
	}

	public static String getVersion() {
		return version;
	}

}
