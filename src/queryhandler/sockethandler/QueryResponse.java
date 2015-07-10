package queryhandler.sockethandler;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class QueryResponse {

	private ByteArrayOutputStream buffer;
	private DataOutputStream stream;

	public QueryResponse(int length) {
		buffer = new ByteArrayOutputStream(length);
		stream = new DataOutputStream(this.buffer);
	}

	public void write(byte[] array) throws IOException {
		stream.write(array, 0, array.length);
	}

	public void write(String string) throws IOException {
		this.stream.writeBytes(string);
		this.stream.write(0);
	}

	public void write(int i) throws IOException {
		this.stream.write(i);
	}

	public void write(short s) throws IOException {
		this.stream.writeShort(Short.reverseBytes(s));
	}

	public byte[] toByteArray() {
		return this.buffer.toByteArray();
	}

}
