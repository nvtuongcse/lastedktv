package vn.com.sonca.RemoteControl;

import java.util.ArrayList;

public class RemoteModel {
	private int Manufaturer;
	private int Model;
	private int PackageSize;
	private int NumberCode;
	private int SystemCode; // 2 byte
	private int Reserved; // 2 byte
	private int pCodeTable; // 4 byte
	private int uCodeTableSize; // 4 byte
	
	private ArrayList<RemoteIRCode> listRemoteIRCode;

	public ArrayList<RemoteIRCode> getListRemoteIRCode() {
		if(listRemoteIRCode == null){
			listRemoteIRCode = new ArrayList<RemoteIRCode>();
		}
		return listRemoteIRCode;
	}

	public void setListRemoteIRCode(ArrayList<RemoteIRCode> listRemoteIRCode) {
		this.listRemoteIRCode = listRemoteIRCode;
	}

	public RemoteModel() {
		
	}

	public RemoteModel(int manufaturer, int model, int packageSize,
			int numberCode, int systemCode, int reserved, int uCodeTableSize,
			int pCodeTable) {
		Manufaturer = manufaturer;
		Model = model;
		PackageSize = packageSize;
		NumberCode = numberCode;
		SystemCode = systemCode;
		Reserved = reserved;
		this.uCodeTableSize = uCodeTableSize;
		this.pCodeTable = pCodeTable;
	}

	public int getManufaturer() {
		return Manufaturer;
	}

	public void setManufaturer(int manufaturer) {
		Manufaturer = manufaturer;
	}

	public int getModel() {
		return Model;
	}

	public void setModel(int model) {
		Model = model;
	}

	public int getPackageSize() {
		return PackageSize;
	}

	public void setPackageSize(int packageSize) {
		PackageSize = packageSize;
	}

	public int getNumberCode() {
		return NumberCode;
	}

	public void setNumberCode(int numberCode) {
		NumberCode = numberCode;
	}

	public int getSystemCode() {
		return SystemCode;
	}

	public void setSystemCode(int systemCode) {
		SystemCode = systemCode;
	}

	public int getReserved() {
		return Reserved;
	}

	public void setReserved(int reserved) {
		Reserved = reserved;
	}

	public int getuCodeTableSize() {
		return uCodeTableSize;
	}

	public void setuCodeTableSize(int uCodeTableSize) {
		this.uCodeTableSize = uCodeTableSize;
	}

	public int getpCodeTable() {
		return pCodeTable;
	}

	public void setpCodeTable(int pCodeTable) {
		this.pCodeTable = pCodeTable;
	}
}
