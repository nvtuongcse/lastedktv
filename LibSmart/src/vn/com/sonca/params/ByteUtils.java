package vn.com.sonca.params;

public class ByteUtils {
	
	public static boolean compareByte(byte[] src, byte[] dst)
	{
		int len = Math.min(src.length, dst.length); 
		for(int i = 0; i < len; i++)
		{
			if(src[i] != dst[i]) return false; 
		}
		return true; 
	}
	
	public static void intToBytes(byte[] data, int pos, int val) {
		data[pos + 0] = (byte) ((val >> 24) & 0xff);
		data[pos + 1] = (byte) ((val >> 16) & 0xff);
		data[pos + 2] = (byte) ((val >> 8) & 0xff);
		data[pos + 3] = (byte) ((val >> 0) & 0xff);
	}
	
	public static void intToBytesL(byte[] data, int pos, int val) {
		data[pos + 0] = (byte) ((val >> 0) & 0xff);
		data[pos + 1] = (byte) ((val >> 8) & 0xff);
		data[pos + 2] = (byte) ((val >> 16) & 0xff);
		data[pos + 3] = (byte) ((val >> 24) & 0xff);
	}

	public static void int16ToBytes(byte[] data, int pos, int val) {
		data[pos + 0] = (byte) ((val >> 8) & 0xff);
		data[pos + 1] = (byte) ((val >> 0) & 0xff);
	}
	
	public static void int16ToBytesL(byte[] data, int pos, int val) {
		data[pos + 1] = (byte) ((val >> 8) & 0xff);
		data[pos + 0] = (byte) ((val >> 0) & 0xff);
	}
	
	public static void int24ToBytes(byte[] data, int pos, int val) {
		data[pos + 0] = (byte) ((val >> 16) & 0xff);
		data[pos + 1] = (byte) ((val >> 8) & 0xff);
		data[pos + 2] = (byte) ((val >> 0) & 0xff);
	}

	public static int byteToInt24(byte[] data, int pos) {
		int val = 0;
		val |= (((int) data[pos + 0]) << 16) & 0x00FF0000;
		val |= (((int) data[pos + 1]) << 8) & 0x0000FF00;
		val |= (((int) data[pos + 2]) << 0) & 0x000000FF;
		return val;
	}
	
	public static int byteToInt32(byte[] data, int pos) {
		int val = 0;
		val |= (((int) data[pos + 0]) << 24) & 0xFF000000;
		val |= (((int) data[pos + 1]) << 16) & 0x00FF0000;
		val |= (((int) data[pos + 2]) << 8) & 0x0000FF00;
		val |= (((int) data[pos + 3]) << 0) & 0x000000FF;
		return val;
	}
	
	public static int byteToInt32L(byte[] data, int pos) {
		int val = 0;
		val |= (((int) data[pos + 0]) << 0) & 0x000000FF;
		val |= (((int) data[pos + 1]) << 8) & 0x0000FF00;
		val |= (((int) data[pos + 2]) << 16) & 0x00FF0000;
		val |= (((int) data[pos + 3]) << 24) & 0xFF000000;
		return val;
	}
	
	public static int byteToInt16(byte[] data, int pos) {
		int val = 0;
		val |= (((int) data[pos + 0]) << 8) & 0x0000FF00;
		val |= (((int) data[pos + 1]) << 0) & 0x000000FF;
		return val;
	}
	
	public static int byteToInt16L(byte[] data, int pos) {
		int val = 0;
		val |= (((int) data[pos + 0]) << 0) & 0x000000FF;
		val |= (((int) data[pos + 1]) << 8) & 0x0000FF00;
		return val;
	}
	
	public static int byteToInt8(byte[] data, int pos) {
		int val = 0;
		val |= (((int) data[pos + 0]) << 0) & 0x000000FF;
		return val;
	}	
	
	public static void copy(byte[] src, int offset, byte[] dst, int start, int length) {
		for (int i = 0; i < length; i++) {
			dst[start + i] = src[offset + i];
		}
	}
	
	public static byte intToByte(int in){
		if (in >= 0) {
			return (byte)(in | 0x00);
		} else {
			return (byte)(Math.abs(in) | 0x10);
		}
	}
}
