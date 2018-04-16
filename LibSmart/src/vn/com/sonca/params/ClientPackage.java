package vn.com.sonca.params;

import java.util.Arrays;

public class ClientPackage {

	private byte command;
	private int dataLen;
	private byte[] data;

	private int CommandStart = 0;
	private int PackageLenStart = 1;
	private int DataStart = 5;

	public void parseClientPackage(byte[] sentPackage) {
		command = sentPackage[CommandStart];
		dataLen = ByteUtils.byteToInt32(sentPackage, PackageLenStart);
		data = Arrays.copyOfRange(sentPackage, DataStart, dataLen);
	}

	public byte getCommand() {
		return command;
	}

	public int getDataLength() {
		return dataLen;
	}

	public byte[] getData() {
		return data;
	}

}
