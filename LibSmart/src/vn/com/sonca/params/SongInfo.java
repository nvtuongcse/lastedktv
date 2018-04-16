package vn.com.sonca.params;

import java.util.ArrayList;

public class SongInfo {
	private int abc=0;
	private int id=0;
    private int index5 = 0; 
	private String singer = "-";
	private String author = "-";
	private int mediatype = 0;
	private String title="";
	private String lyric = "-";
	private int singer_vocal = 0;
	private int remix = 0; 
//	private String language = ""; 
//	private String videoName = ""; 
//	private String mediaName = ""; 
//	private String vocalName = ""; 
//	private String midi = "0"; 
//	private String isSinger = "0"; 
//	private String newVol = "0"; 
//	private String preferFont = "";
//	private String priority = "1"; 
//	
//	public String getMediaVocalName() {
//		return vocalName;
//	}
//	public void setMediaVocalName(String vocalName) {
//		this.vocalName = vocalName;
//	}
//	public String getPriority() {
//		return priority;
//	}
//	public void setPriority(String priority) {
//		this.priority = priority;
//	}
	public String getSinger() {
		return this.singer; 
	}
	public void setSinger(String aSinger) {
		this.singer = aSinger; 
	}
	
	public String getAuthor() {
		return this.author; 
	}
	public void setAuthor(String aMusician) {
		this.author = aMusician; 
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
//	public int getIndex5()
//    {
//        return this.index5;
//    }
//    public void setIndex5(int idx5)
//    {
//        this.index5 = idx5;
//    }

	public String getTitle() {
		return this.title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getLyric() {
		return lyric;
	}
	public void setLyric(String lyric) {
		this.lyric = lyric;
	}
	public int getTypeAbc() {
		return abc;
	}
	public void setTypeAbc(int typeabc) {
		this.abc = typeabc;
	}
	
//	public void setSongType(String str) {
//		this.type = str; 
//	}
//	public String getSongType() {
//		return this.type; 
//	}
//	
//	public String getLanguage() {
//		return this.language; 
//	}
//	public void setLanguage(String str) {
//		this.language = str; 
//	}
//	public String getVideoName() {
//		return videoName;
//	}
//	public void setVideoName(String videoName) {
//		this.videoName = videoName;
//	}
//	public String getMediaName() {
//		return mediaName;
//	}
//	public void setMediaName(String mediaName) {
//		this.mediaName = mediaName;
//	}
//	public String getMidi() {
//		return midi;
//	}
//	public void setMidi(String midi) {
//		this.midi = midi;
//	}
//	public String getIsSinger() {
//		return isSinger;
//	}
//	public void setIsSinger(String isSinger) {
//		this.isSinger = isSinger;
//	}
//	public String getNewVol() {
//		return newVol;
//	}
//	public void setNewVol(String newVol) {
//		this.newVol = newVol;
//	}
//	
//	public void setPreferFont(String preferFont) {
//		this.preferFont = preferFont; 
//	}
//	public String getPreferFont() {
//		return preferFont; 
//	}
	
	public int getIndex5() {
		return index5;
	}
	public void setIndex5(int index5) {
		this.index5 = index5;
	}

	public int getMediatype() {
		return mediatype;
	}
	public void setMediatype(int mediatype) {
		this.mediatype = mediatype;
	}

	public int getSingerVocal() {
		return singer_vocal;
	}
	public void setSingerVocal(int isvocal) {
		this.singer_vocal = isvocal;
	}

	public int getRemix() {
		return remix;
	}
	public void setRemix(int remix) {
		this.remix = remix;
	}

	private String vidName;
	public String getVidName() {
		return vidName;
	}
	public void setVidName(String vidName) {
		this.vidName = vidName;
	}
	
	
	private long vidSize;
	public long getVidSize() {
		return vidSize;
	}
	public void setVidSize(long vidSize) {
		this.vidSize = vidSize;
	}
	
	
	
	private boolean isActive = false;
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	
	@Override
	public boolean equals(Object o) {
		return this.id == ((SongInfo)o).id;
	}
	
	// HMINH NEW	
	private boolean isFromServer = false;
	private String versionSong = "1.0.0";
	private boolean zShow = true;
	private int zOneTouch = 0;
	private boolean zNewSong = false;
	private boolean needDelete = false;
	private int zExtra = 0;
	private boolean zSCA = false;
	private int langID = 0;
	private int songTypeID = 0;
	private ArrayList<SingerMusicianInfo> listSinger = new ArrayList<SingerMusicianInfo>();
	private ArrayList<SingerMusicianInfo> listMusician = new ArrayList<SingerMusicianInfo>();
	private ArrayList<String> listLink = new ArrayList<String>();
	
	public boolean isFromServer() {
		return isFromServer;
	}
	public void setFromServer(boolean isFromServer) {
		this.isFromServer = isFromServer;
	}
	
	public String getVersionSong() {
		return versionSong;
	}
	public void setVersionSong(String versionSong) {
		this.versionSong = versionSong;
	}
	public boolean iszShow() {
		return zShow;
	}
	public void setzShow(boolean zShow) {
		this.zShow = zShow;
	}
	public int getzOneTouch() {
		return zOneTouch;
	}
	public void setzOneTouch(int zOneTouch) {
		this.zOneTouch = zOneTouch;
	}
	public boolean iszNewSong() {
		return zNewSong;
	}
	public void setzNewSong(boolean zNewSong) {
		this.zNewSong = zNewSong;
	}
	public boolean isNeedDelete() {
		return needDelete;
	}
	public void setNeedDelete(boolean NeedDelete) {
		this.needDelete = NeedDelete;
	}
	public int getzExtra() {
		return zExtra;
	}
	public void setzExtra(int zExtra) {
		this.zExtra = zExtra;
	}
	public boolean iszSCA() {
		return zSCA;
	}
	public void setzSCA(boolean zSCA) {
		this.zSCA = zSCA;
	}
	public int getLangID() {
		return langID;
	}
	public void setLangID(int langID) {
		this.langID = langID;
	}
	public int getSongTypeID() {
		return songTypeID;
	}
	public void setSongTypeID(int songTypeID) {
		this.songTypeID = songTypeID;
	}
	public ArrayList<SingerMusicianInfo> getListSinger() {
		return listSinger;
	}
	public void setListSinger(ArrayList<SingerMusicianInfo> listSinger) {
		this.listSinger = listSinger;
	}
	public ArrayList<SingerMusicianInfo> getListMusician() {
		return listMusician;
	}
	public void setListMusician(ArrayList<SingerMusicianInfo> listMusician) {
		this.listMusician = listMusician;
	}
	public ArrayList<String> getListLink() {
		return listLink;
	}
	public void setListLink(ArrayList<String> listLink) {
		this.listLink = listLink;
	}
	
	@Override
	public String toString() {
		return "SongInfo [abc=" + abc + ", id=" + id + ", index5=" + index5
				+ ", singer=" + singer + ", author=" + author + ", mediatype="
				+ mediatype + ", title=" + title + ", lyric=" + lyric
				+ ", singer_vocal=" + singer_vocal + ", remix=" + remix
				+ ", vidName=" + vidName + ", vidSize=" + vidSize
				+ ", isActive=" + isActive + ", isFromServer=" + isFromServer
				+ ", versionSong=" + versionSong + ", zShow=" + zShow
				+ ", zOneTouch=" + zOneTouch + ", zNewSong=" + zNewSong
				+ ", needDelete=" + needDelete + ", zExtra=" + zExtra
				+ ", zSCA=" + zSCA + ", langID=" + langID + ", songTypeID="
				+ songTypeID + ", listSinger=" + listSinger + ", listMusician="
				+ listMusician + ", listLink=" + listLink + "]";
	}
	
	public SongInfo() {
		listSinger.add(new SingerMusicianInfo(0, "-", 0, 0, ""));
		listMusician.add(new SingerMusicianInfo(0, "-", 0, 0, ""));
	}			
	
}
