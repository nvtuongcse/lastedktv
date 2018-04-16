package vn.com.sonca.Touch.Hi_W;

public class HiW_FirmwareInfo {
	private String daumay_name = "";
	private int daumay_version = -1;
	private int wifi_version = -1;
	private int wifi_revision = -1;

	public HiW_FirmwareInfo() {

	}

	public HiW_FirmwareInfo(String daumay_name, int daumay_version,
			int wifi_version, int wifi_revision) {
		this.daumay_name = daumay_name;
		this.daumay_version = daumay_version;
		this.wifi_version = wifi_version;
		this.wifi_revision = wifi_revision;
	}

	public String getDaumay_name() {
		return daumay_name;
	}

	public void setDaumay_name(String daumay_name) {
		this.daumay_name = daumay_name;
	}

	public int getDaumay_version() {
		return daumay_version;
	}

	public void setDaumay_version(int daumay_version) {
		this.daumay_version = daumay_version;
	}

	public int getWifi_version() {
		return wifi_version;
	}

	public void setWifi_version(int wifi_version) {
		this.wifi_version = wifi_version;
	}

	public int getWifi_revision() {
		return wifi_revision;
	}

	public void setWifi_revision(int wifi_revision) {
		this.wifi_revision = wifi_revision;
	}

}
