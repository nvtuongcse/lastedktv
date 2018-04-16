package vn.com.sonca.SleepDevice;

public class Sleep {
	
	private String name;
	private int giay;
	
	private boolean isActive = false;
	
	public Sleep(int giay, String name, boolean isActive) {
		this.isActive = isActive;
		this.name = name;
		this.giay = giay;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public int getGiay() {
		return giay;
	}

	public void setGiay(int giay) {
		this.giay = giay;
	}

}
