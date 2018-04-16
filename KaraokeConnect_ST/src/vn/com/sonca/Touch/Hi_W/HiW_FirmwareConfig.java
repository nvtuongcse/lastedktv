package vn.com.sonca.Touch.Hi_W;

public class HiW_FirmwareConfig {
	private int mode;
	// 0 - null mode; 1 - station; 2 - ap; 3 - station + ap
	private String stationID;
	private String stationPass;
	private String apID;
	private String apPass;
	private String passConnect;
	private String passAdmin;

	public HiW_FirmwareConfig() {
	}

	public HiW_FirmwareConfig(int mode, String stationID, String stationPass,
			String apID, String apPass, String passConnect, String passAdmin) {
		this.mode = mode;
		this.stationID = stationID;
		this.stationPass = stationPass;
		this.apID = apID;
		this.apPass = apPass;
		this.passConnect = passConnect;
		this.passAdmin = passAdmin;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public String getStationID() {
		return stationID;
	}

	public void setStationID(String stationID) {
		this.stationID = stationID;
	}

	public String getStationPass() {
		return stationPass;
	}

	public void setStationPass(String stationPass) {
		this.stationPass = stationPass;
	}

	public String getApID() {
		return apID;
	}

	public void setApID(String apID) {
		this.apID = apID;
	}

	public String getApPass() {
		return apPass;
	}

	public void setApPass(String apPass) {
		this.apPass = apPass;
	}

	public String getPassConnect() {
		return passConnect;
	}

	public void setPassConnect(String passConnect) {
		this.passConnect = passConnect;
	}

	public String getPassAdmin() {
		return passAdmin;
	}

	public void setPassAdmin(String passAdmin) {
		this.passAdmin = passAdmin;
	}

}
