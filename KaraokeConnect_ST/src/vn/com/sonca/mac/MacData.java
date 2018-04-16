package vn.com.sonca.mac;

import com.google.gson.annotations.SerializedName;

public class MacData {
	@SerializedName("id")
	private int id;

	@SerializedName("macAddress")
	private String macAddress;

	@SerializedName("serialId")
	private String serialId;

	@SerializedName("dateCreate")
	private String dateCreate;

	public MacData() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getSerialId() {
		return serialId;
	}

	public void setSerialId(String serialId) {
		this.serialId = serialId;
	}

	public String getDateCreate() {
		return dateCreate;
	}

	public void setDateCreate(String dateCreate) {
		this.dateCreate = dateCreate;
	}
}