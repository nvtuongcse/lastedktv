package vn.com.sonca.mac;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class MacGSON {
	@SerializedName("Action")
	private String action;

	@SerializedName("User")
	private String user;

	@SerializedName("Pass")
	private String pass;

	@SerializedName("Result")
	private String result;

	@SerializedName("MacData")
	private List<MacData> macDatas = new ArrayList<MacData>();

	public MacGSON() {
	}

	public static String createSetMacJSON(String user, String pass,
			String macAddress, String serialId) {
		String json = "";

		MacData tempMac = new MacData();
		tempMac.setId(-1);
		tempMac.setMacAddress(macAddress);
		tempMac.setSerialId(serialId);
		tempMac.setDateCreate(" ");
		List<MacData> listMac = new ArrayList<MacData>();
		listMac.add(tempMac);

		MacGSON temp = new MacGSON();
		temp.setAction("set");
		temp.setUser(user);
		temp.setPass(pass);
		temp.setResult("SEND");
		temp.setMacDatas(listMac);

		Gson gson = new Gson();
		json = gson.toJson(temp, MacGSON.class);

		return json;
	}

	public static String createGetMacJSON() {
		String json = "";

		MacData tempMac = new MacData();
		tempMac.setId(-1);
		tempMac.setMacAddress("-1");
		tempMac.setSerialId("-1");
		tempMac.setDateCreate(" ");
		List<MacData> listMac = new ArrayList<MacData>();
		listMac.add(tempMac);

		MacGSON temp = new MacGSON();
		temp.setAction("get");
		temp.setUser("user");
		temp.setPass("pass");
		temp.setResult("SEND");
		temp.setMacDatas(listMac);

		Gson gson = new Gson();
		json = gson.toJson(temp, MacGSON.class);

		return json;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public List<MacData> getMacDatas() {
		return macDatas;
	}

	public void setMacDatas(List<MacData> macDatas) {
		this.macDatas = macDatas;
	}

}
