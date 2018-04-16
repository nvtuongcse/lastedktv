package vn.sonca.LoadDataServer;

public class ItemApp extends Object {
	
	private String TAB = "ItemApp";
	
	private int icon;
	private String styleApp = "";
	private String nameApp = "";		//
	private String namePack = "";	//
	private String linkPack = "";	//
	private String infoPack = "";	//
	private String linkIconDevice = "";	//
	private String linkIconServer = "";	//
	private boolean isInstaller;
	private boolean isFocus;
	private String version = "0.0.0.0";
	
	private String linkIconDeviceDep;
	private String linkIconServerDep = "";	//
	
	public int getIcon() {
		return icon;
	}
	public void setIcon(int icon) {
		this.icon = icon;
	}
	public String getNameApp() {
		return nameApp;
	}
	public void setNameApp(String nameApp) {
		this.nameApp = nameApp;
	}
	public String getNamePack() {
		return namePack;
	}
	public void setNamePack(String namePack) {
		this.namePack = namePack;
	}
	public boolean isInstaller() {
		return isInstaller;
	}
	public void setInstaller(boolean isInstaller) {
		this.isInstaller = isInstaller;
	}
	public boolean isFocus() {
		return isFocus;
	}
	public void setFocus(boolean isFocus) {
		this.isFocus = isFocus;
	}
	public String getLinkPack() {
		return linkPack;
	}
	public void setLinkPack(String linkPack) {
		this.linkPack = linkPack;
	}
	public String getInfoPack() {
		return infoPack;
	}
	public void setInfoPack(String infoPack) {
		this.infoPack = infoPack;
	}
	public String getStyleApp() {
		return styleApp;
	}
	public void setStyleApp(String styleApp) {
		this.styleApp = styleApp;
	}
	public String getLinkIconServer() {
		return linkIconServer;
	}
	public void setLinkIconServer(String linkIconServer) {
		this.linkIconServer = linkIconServer;
	}
	public String getLinkIconDevice() {
		return linkIconDevice;
	}
	public void setLinkIconDevice(String linkIconDevice) {
		this.linkIconDevice = linkIconDevice;
	}
	
	public void setLinkIconDeviceDep(String linkIconDevice) {
		this.linkIconDeviceDep = linkIconDevice;
	}
	public String getLinkIconDeviceDep() {
		return linkIconDeviceDep;
	}
	
	public String getLinkIconServerDep() {
		return linkIconServerDep;
	}
	public void setLinkIconServerDep(String linkIconServer) {
		this.linkIconServerDep = linkIconServer;
	}
	
	@Override
	public boolean equals(Object o) {
		return this.namePack.equals(((ItemApp)o).namePack);
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	


}
