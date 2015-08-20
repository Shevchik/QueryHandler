package queryhandler.sockethandler;

import java.net.DatagramPacket;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class QueryStatusChallenge {

	private static final Random random = new Random();

	private long receiveTime = System.currentTimeMillis();

	private int token = random.nextInt(16777216);
	private byte[] identity = new byte[4];
	private byte[] response;

	public QueryStatusChallenge(DatagramPacket packet) {
        System.arraycopy(packet.getData(), 3, identity, 0, identity.length);
		response = String.format("\t%s%d\u0000", new String(this.identity), token).getBytes(StandardCharsets.UTF_8);
	}

	public boolean isStale() {
		return System.currentTimeMillis() - receiveTime > 30000L;
	}

	public int getToken() {
		return token;
	}

	public byte[] getResponse() {
		return response;
	}

	public byte[] getIdentity() {
		return identity;
	}

}
