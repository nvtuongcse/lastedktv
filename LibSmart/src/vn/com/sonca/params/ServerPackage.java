package vn.com.sonca.params;

import java.util.Arrays;

import android.util.Log;

public class ServerPackage {
	
	private byte command;
	private int dataLen;
	private byte status;
	private byte[] data;
	private int modelDevice = 0;
	private String errorMessage; 
	
	private int CommandStart = 0;
	private int StatusStart = 1;
	private int PackageLenStart = 2;
	private int DataStart = 6;

	public byte[] createServerPackage() {
		int packageLen = 1 + 1 + 4 + data.length + 1;
		byte[] response = new byte[packageLen];
		response[CommandStart] = command;
		response[StatusStart] = status;
		ByteUtils.intToBytes(response, PackageLenStart, (int) data.length);
		ByteUtils.copy(data, 0, response, DataStart, data.length);
		response[response.length - 1] = (byte) 0xFF;
		return response;
	}

	public void parseServerPackage(byte[] receiPackage) {
		try {
			command = receiPackage[CommandStart];
			status = receiPackage[StatusStart];
			dataLen = ByteUtils.byteToInt32(receiPackage, PackageLenStart);
			// KHIEM System.out.printf("\nData len: %d\n", dataLen);
			if(dataLen>1000){
				status = 0x01;
				return;
			}
			data = Arrays.copyOfRange(receiPackage, DataStart, DataStart + dataLen);
			if(data.length >= 57){
				modelDevice = (int)(data[56] & 0xff);
			}else{ // SK9xxx
				modelDevice = 0;
			}
		} catch (Exception e) {
			Log.d("", "=parseServerPackage error");
			status = 0x01;
		}		
	}

	public void setCommand(int aCmd) {
		command = (byte) aCmd;
	}

	public int getCommand() {
		return command & 0xff;
	}

	public void setData(byte[] value) {
		this.data = value;
	}

	public byte[] getData() {
		return data;
	}

	public void setStatus(int value) {
		this.status = (byte) value;
	}

	public int getStatus() {
		return status;
	}

	public int getContentLength() {
		return dataLen;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public int getModelDevice() {
		return modelDevice;
	}

	public void setModelDevice(int modelDevice) {
		this.modelDevice = modelDevice;
	}

	final char[] hexArray = "0123456789ABCDEF".toCharArray();
	public String bytesToHex2(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		String str = new String(hexChars);
		String resultStr = "";
		for (int i = 0; i < str.length(); i++) {
			resultStr += str.charAt(i);
			if(i % 2 == 1){
				resultStr += " ";
			}
			
		}
		return resultStr;
	}
}
