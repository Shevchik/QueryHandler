package queryhandler.api.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import queryhandler.Config;

public class QueryRequestEvent extends Event {

	private String motd;
	private String version;
	private int maxPlayers;
	private List<String> players;

	public QueryRequestEvent() {
		this.motd = Config.getMotd();
		this.maxPlayers = Config.getMaxPlayers();
		this.players = new ArrayList<>();
		this.version = Config.getVersion();
		for (Player player : Bukkit.getOnlinePlayers()) {
			players.add(player.getName());
		}
	}

	public String getMotd() {
		return motd;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public void setMotd(String motd) {
		this.motd = motd;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public List<String> getPlayers() {
		return players;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	private static final HandlerList handlers = new HandlerList();

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
