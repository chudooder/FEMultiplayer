package chu.engine.network;

public class Message {
	
	public static final byte INIT = 0;
	
	public byte origin;
	public byte type;
	public int length;
	public byte[] message;
	private static final byte[] BEGIN = new byte[]{0x42,0x45,0x47,0x49,0x4e};
	
	public Message(int origin, byte type, byte[] message) {
		this.origin = (byte)origin;
		this.length = message.length;
		this.type = type;
		this.message = message;
	}
	
	public byte[] getBytes() {
		byte[] out = new byte[length+8];
		System.arraycopy(BEGIN, 0, out, 0, 5);
		out[5] = origin;
		out[6] = type;
		out[7] = (byte) message.length;
		System.arraycopy(message, 0, out, 8, length);
		return out;
	}
}
