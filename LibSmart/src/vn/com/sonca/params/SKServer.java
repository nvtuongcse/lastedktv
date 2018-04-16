package vn.com.sonca.params;

import android.util.Log;

public class SKServer {
	private int authenID; 
	private int connectedIP; 
	private String connectedIPStr = "";
	private String name = ""; 
	private String deviceID;
	private String connectPass = ""; 
	private int modelDevice = 0;
	
	private String nameRemote = "";
	private int ircRemote = -1;
	
	public int getAuthenID() {
		return authenID;
	}
	public void setAuthenID(int authenID) {
		this.authenID = authenID;
	}
	public int getConnectedIP() {
		return connectedIP;
	}
	public void setConnectedIP(int connectedIP) {
		this.connectedIP = connectedIP;
	}
	public String getConnectedIPStr() {
		return connectedIPStr;
	}
	public void setConnectedIPStr(String connectedIPStr) {
		this.connectedIPStr = connectedIPStr;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDeviceID() {
		return deviceID;
	}
	public void setDeviceID(String deviceID) {
		this.deviceID = deviceID;
	}
	public String getConnectPass() {
		return connectPass;
	}
	
	public void setConnectPass(String connectPass) {
		this.connectPass = connectPass;
	} 
	
/////////////////////////////////////////
	
	public long ipToLong(String ipAddress) {
 		String[] ipAddressInArray = ipAddress.split("\\.");
		long result = 0;
		for (int i = 0; i < ipAddressInArray.length; i++) {
			int power = 3 - i;
			int ip = Integer.parseInt(ipAddressInArray[i]);
			result += ip * Math.pow(256, power);
		}
		return result;
	}
	
	@Override
	public boolean equals(Object o) {
		// Log.i("SKServer", "this - " + this.iddevice);
		// Log.i("SKServer", "object - " + ((SKServer)o).iddevice);
		// Log.i("SKServer", "      ");
		// return (this.iddevice == ((SKServer)o).iddevice);
		return (this.connectedIPStr.equals(((SKServer)o).connectedIPStr));
	}
	
	private long iddevice = -1;
	public static final int SAVED = 0;
	public static final int CONNECTED = 1;
	public static final int BROADCASTED = 2;
	
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isSave() {
		return isSave;
	}
	public void setSave(boolean isSave) {
		this.isSave = isSave;
	}

	public int getModelDevice() {
		return modelDevice;
	}
	public void setModelDevice(int modelDevice) {
		this.modelDevice = modelDevice;
	}

	public String getNameRemote() {
		return nameRemote;
	}
	public void setNameRemote(String nameRemote) {
		this.nameRemote = nameRemote;
	}

	public int getIrcRemote() {
		return ircRemote;
	}
	public void setIrcRemote(int ircRemote) {
		this.ircRemote = ircRemote;
	}

	private int state;
	
	private boolean isActive;
	
	private boolean isSave;

}
