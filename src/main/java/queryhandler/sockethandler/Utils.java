package queryhandler.sockethandler;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import queryhandler.Config;

public class Utils {

	public static int getToken(byte[] data, int start, int length) {
		if (0 <= length - start - 4) {
			return data[start] << 24 | (data[start + 1] & 255) << 16 | (data[start + 2] & 255) << 8 | data[start + 3] & 255;
		}
		return 0;
	}

	public static String getIP() {
		String bukkitIP = Bukkit.getIp();
		if (bukkitIP == null || bukkitIP.isEmpty()) {
			return "0.0.0.0";
		}
		return bukkitIP;
	}

	public static String getPlugins() {
		final StringBuilder result = new StringBuilder();
		result.append(Bukkit.getServer().getName());
		result.append(" on Bukkit ");
		result.append(Bukkit.getServer().getBukkitVersion());
		Plugin[] plugins = Bukkit.getPluginManager().getPlugins();
		if (Config.isSendingPluginsEnabled() && plugins.length > 0) {
			result.append(": ");
			for (int i = 0; i < plugins.length; ++i) {
				if (i > 0) {
					result.append("; ");
				}
				result.append(plugins[i].getDescription().getName());
				result.append(" ");
				result.append(plugins[i].getDescription().getVersion().replaceAll(";", ","));
			}
		}
		return result.toString();
	}

	public static String getVersion() {
		String bukkitversion = Bukkit.getVersion();
		int index = bukkitversion.indexOf("MC: ");
		if (index != -1) {
			return bukkitversion.substring(index + 4, bukkitversion.length() - 1);
		}
		return "Unknown";
	}

}
