package vn.com.sonca.params;

public class ItemUSB {
	private int usbNumber;
	private String usbName;

	public ItemUSB(){
		
	}
	
	public ItemUSB(int usbNumber, String usbName) {
		super();
		this.usbNumber = usbNumber;
		this.usbName = usbName;
	}

	public int getUsbNumber() {
		return usbNumber;
	}

	public void setUsbNumber(int usbNumber) {
		this.usbNumber = usbNumber;
	}

	public String getUsbName() {
		return usbName;
	}

	public void setUsbName(String usbName) {
		this.usbName = usbName;
	}

}
