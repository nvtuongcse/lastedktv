package vn.com.sonca.params;

public class Download90xxInfo {

	private int id = 0;
	private String fileType = "";
	private boolean flagVocal = false;
	private int folderType = 3;
	private String singerName = "";
	private String songName = "";
	private String urlPath = "";

	@Override
	public String toString() {
		return "Download90xxInfo [id=" + id + ", fileType=" + fileType
				+ ", flagVocal=" + flagVocal + ", folderType=" + folderType
				+ ", singerName=" + singerName + ", songName=" + songName
				+ ", urlPath=" + urlPath + "]";
	}

	public Download90xxInfo(int id, String fileType, boolean flagVocal,
			int folderType, String singerName, String songName, String urlPath) {
		super();
		this.id = id;
		this.fileType = fileType;
		this.flagVocal = flagVocal;
		this.folderType = folderType;
		this.singerName = singerName;
		this.songName = songName;
		this.urlPath = urlPath;
	}

	public int getFolderType() {
		return folderType;
	}

	public void setFolderType(int folderType) {
		this.folderType = folderType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public boolean isFlagVocal() {
		return flagVocal;
	}

	public void setFlagVocal(boolean flagVocal) {
		this.flagVocal = flagVocal;
	}

	public String getSingerName() {
		return singerName;
	}

	public void setSingerName(String singerName) {
		this.singerName = singerName;
	}

	public String getSongName() {
		return songName;
	}

	public void setSongName(String songName) {
		this.songName = songName;
	}

	public String getUrlPath() {
		return urlPath;
	}

	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}

}
