package queryhandler.sockethandler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.PortUnreachableException;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import queryhandler.api.events.QueryRequestEvent;

public class QueryListenerThread extends Thread {

	private static final String WORLD = Bukkit.getWorlds().get(0).getName();

	private byte[] buffer = new byte[1460];
	private HashMap<SocketAddress, QueryStatusChallenge> challenges = new HashMap<>();

	private DatagramSocket socket;

	public QueryListenerThread(InetAddress addr, int port) throws SocketException {
		socket = new DatagramSocket(port, addr);
		socket.setSoTimeout(500);
	}

	private volatile boolean run = true;

	public void shutdown() {
		run = false;
	}

	@Override
	public void run() {
		while (run) {
			try {
				Iterator<Entry<SocketAddress, QueryStatusChallenge>> iterator = challenges.entrySet().iterator();
				while (iterator.hasNext()) {
					Entry<SocketAddress, QueryStatusChallenge> entry = iterator.next();
					if (entry.getValue().isStale()) {
						iterator.remove();
					}
				}
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				socket.receive(packet);
				processPacket(packet);
			} catch (SocketTimeoutException | PortUnreachableException e) {
			} catch (Throwable t) {
				System.err.println("Query error");
				t.printStackTrace();
				run = false;
			}
		}
		socket.close();
	}

	private void processPacket(DatagramPacket packet) throws IOException {
		byte[] data = packet.getData();
		int length = packet.getLength();
		if (3 > length || -2 != data[0] || -3 != data[1]) {
			return;
		}
		switch (data[2]) {
			case 9: {
				sendChallenge(packet);
				return;
			}
			case 0: {
				if (!isRequestValid(packet)) {
					return;
				}
				if (length > 15) {
					sendDataPoolInfo(packet, Arrays.copyOfRange(data, 15, length));
					return;
				}
				QueryRequestEvent event = new QueryRequestEvent();
				Bukkit.getPluginManager().callEvent(event);
				if (15 == length) {
					sendFullInfo(packet, event);
					return;
				}
				sendShortInfo(packet, event);
				return;
			}
		}
	}

	private boolean isRequestValid(DatagramPacket packet) {
		SocketAddress socketAddress = packet.getSocketAddress();
		if (!challenges.containsKey(socketAddress)) {
			return false;
		}
		byte[] data = packet.getData();
		if (challenges.get(socketAddress).getToken() != Utils.getToken(data, 7, packet.getLength())) {
			return false;
		}
		return true;
	}

	private void sendChallenge(DatagramPacket packet) throws IOException {
		QueryStatusChallenge challenge = new QueryStatusChallenge(packet);
		challenges.put(packet.getSocketAddress(), challenge);
		sendPacket(packet.getSocketAddress(), challenge.getResponse());
	}

	private void sendShortInfo(DatagramPacket packet, QueryRequestEvent event) throws IOException {
		QueryResponse response = new QueryResponse(1460);
		response.write(0);
		response.write(challenges.get(packet.getSocketAddress()).getIdentity());
		response.write(event.getMotd());
		response.write("SMP");
		response.write(WORLD);
		response.write(Integer.toString(Bukkit.getOnlinePlayers().size()));
		response.write(Integer.toString(event.getMaxPlayers()));
		response.write((short) Bukkit.getPort());
		response.write(Utils.getIP());
		sendPacket(packet.getSocketAddress(), response.toByteArray());
	}

	private void sendFullInfo(DatagramPacket packet, QueryRequestEvent event) throws IOException {
		QueryResponse response = new QueryResponse(1460);
		response.write(0);
		response.write(challenges.get(packet.getSocketAddress()).getIdentity());
		response.write("splitnum");
		response.write(128);
		response.write(0);
		response.write("hostname");
		response.write(event.getMotd());
		response.write("gametype");
		response.write("SMP");
		response.write("game_id");
		response.write("MINECRAFT");
		response.write("version");
		response.write(event.getVersion());
		response.write("plugins");
		response.write(Utils.getPlugins());
		response.write("map");
		response.write(WORLD);
		response.write("numplayers");
		response.write(Integer.toString(Bukkit.getOnlinePlayers().size()));
		response.write("maxplayers");
		response.write(Integer.toString(event.getMaxPlayers()));
		response.write("hostport");
		response.write(Integer.toString(Bukkit.getPort()));
		response.write("hostip");
		response.write(Utils.getIP());
		response.write(0);
		response.write(1);
		response.write("player_");
		response.write(0);
		for (Player player : Bukkit.getOnlinePlayers()) {
			response.write(player.getName());
		}
		response.write(0);
		sendPacket(packet.getSocketAddress(), response.toByteArray());
	}

	private void sendDataPoolInfo(DatagramPacket packet, byte[] payload) throws IOException {
		QueryResponse response = new QueryResponse(1460);
		response.write(0);
		response.write(challenges.get(packet.getSocketAddress()).getIdentity());
		QueryDataPool.getInstance().append(response, payload);
	}

	private void sendPacket(SocketAddress to, byte[] data) throws IOException {
		socket.send(new DatagramPacket(data, data.length, to));
	}

}
