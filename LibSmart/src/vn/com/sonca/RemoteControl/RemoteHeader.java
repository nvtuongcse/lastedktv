package vn.com.sonca.RemoteControl;

import java.util.ArrayList;

public class RemoteHeader {
	private int header; // 4 byte
	private int version;
	private int numModel;
	private int reserved; // 2 byte
	private int pModelTable; // 4 byte
	private int uModelTableSize; // 4 byte
	
	private ArrayList<RemoteModel> listRemoteModel;
	
	public ArrayList<RemoteModel> getListRemoteModel() {
		if(listRemoteModel == null){
			listRemoteModel = new ArrayList<RemoteModel>();
		}
		return listRemoteModel;
	}

	public void setListRemoteModel(ArrayList<RemoteModel> listRemoteModel) {
		this.listRemoteModel = listRemoteModel;
	}

	public RemoteHeader() {
		
	}

	public RemoteHeader(int header, int version, int numModel, int reserved,
			int pModelTable, int uModelTableSize) {
		this.header = header;
		this.version = version;
		this.numModel = numModel;
		this.reserved = reserved;
		this.pModelTable = pModelTable;
		this.uModelTableSize = uModelTableSize;
	}

	public int getHeader() {
		return header;
	}

	public void setHeader(int header) {
		this.header = header;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getNumModel() {
		return numModel;
	}

	public void setNumModel(int numModel) {
		this.numModel = numModel;
	}

	public int getReserved() {
		return reserved;
	}

	public void setReserved(int reserved) {
		this.reserved = reserved;
	}

	public int getpModelTable() {
		return pModelTable;
	}

	public void setpModelTable(int pModelTable) {
		this.pModelTable = pModelTable;
	}

	public int getuModelTableSize() {
		return uModelTableSize;
	}

	public void setuModelTableSize(int uModelTableSize) {
		this.uModelTableSize = uModelTableSize;
	}
}
