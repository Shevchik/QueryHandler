package queryhandler;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commands implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!sender.hasPermission("queryhandler.cmds")) {
			sender.sendMessage(ChatColor.RED + "No perms");
		}
		if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
			Config.loadConfig();
			sender.sendMessage(ChatColor.YELLOW + "Config reloaded");
		}
		return true;
	}

}
