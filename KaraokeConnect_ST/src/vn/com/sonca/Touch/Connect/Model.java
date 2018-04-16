package vn.com.sonca.Touch.Connect;

public class Model {
	
	private String name;
	private int toc;
	private int irc;
	private int en;
	private String details = "";
	
	private boolean isActive = false;
	
	public Model(int toc, int irc, int en, String name, String details) {
		this.details = details;
		this.name = name;
		this.toc = toc;
		this.irc = irc;
		this.en = en;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getToc() {
		return toc;
	}
	public void setToc(int toc) {
		this.toc = toc;
	}
	public int getIrc() {
		return irc;
	}
	public void setIrc(int irc) {
		this.irc = irc;
	}
	public int getEn() {
		return en;
	}
	public void setEn(int en) {
		this.en = en;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

}
