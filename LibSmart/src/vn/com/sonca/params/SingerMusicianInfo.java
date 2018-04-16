package vn.com.sonca.params;

public class SingerMusicianInfo {
	private int pk = 0;
	private String name = "-";
	private int coverID = 1;
	private int langID = 0;
	private String pathImage = "";

	public SingerMusicianInfo() {
		super();
	}

	public SingerMusicianInfo(int pk, String name, int coverID, int langID,
			String pathImage) {
		super();
		this.pk = pk;
		this.name = name;
		this.coverID = coverID;
		this.langID = langID;
		this.pathImage = pathImage;
	}

	public int getPk() {
		return pk;
	}

	public void setPk(int pk) {
		this.pk = pk;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getCoverID() {
		return coverID;
	}

	public void setCoverID(int coverID) {
		this.coverID = coverID;
	}

	public int getLangID() {
		return langID;
	}

	public void setLangID(int langID) {
		this.langID = langID;
	}

	public String getPathImage() {
		return pathImage;
	}

	public void setPathImage(String pathImage) {
		this.pathImage = pathImage;
	}

}
