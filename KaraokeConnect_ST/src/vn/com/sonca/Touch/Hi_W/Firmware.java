package vn.com.sonca.Touch.Hi_W;

public class Firmware {
	

	public static final int HiW = 0;
	public static final int KM1 = 1;

	
	private String link = "";
	private String name = "";
	private int revision = 0;
	private int version = 0;
	private int device = 0;
	private int size = 0;
	
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getRevision() {
		return revision;
	}
	public void setRevision(int revision) {
		this.revision = revision;
	}
	public int getVersion() {
		return version;
	}
	public void setVersion(int version) {
		this.version = version;
	}
	public int getDevice() {
		return device;
	}
	public void setDevice(int device) {
		this.device = device;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	

}
