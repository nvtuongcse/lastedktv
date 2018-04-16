package vn.com.sonca.params;

import android.text.Spannable;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.database.DBInstance.SearchMode;
import vn.com.sonca.utils.AppConfig.LANG_INDEX;
import vn.com.sonca.utils.AppConfig.MEDIA_TYPE;
import vn.com.sonca.zzzzz.MyApplication;

public class Song{
	private int id=0;
    private int index5 = 0; 
	private int singerId[];
	private int[] musicianId;
//	private int typeId; 
	private int langID; 
	private String 	name="";
	private String shortname;
	private String mTitleRaw; 
	private String lyric = "";
	private boolean isFavourite = false;
	private boolean isRemix = false;
	private LANG_INDEX songLanguage; 
	private MEDIA_TYPE mediaType; 
	private SearchMode mode; 
	private int typeABC; 
//	private boolean isMediaSinger; 
	private Singer mSinger = new Singer(); 
	private Musician mMusician = new Musician(); 
	private int extraInfo; 
//	private String mVideo; 
//	private SongType mSongType; 
//	private String mediaName; 
//	private String vocalName; 	
//	private boolean mIsFilled = false;
//	private int mPriority = 1;
//	public int mSpace[] = {-1, -1, -1, -1};
//	private int mIndex;	
//	private boolean isSelected; 
	
	public Song(){}
	
	public Song(int id , int typeABC) {
		this.typeABC = typeABC;
		this.index5 = id;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIndex5()
    {
        return this.index5;
    }
    public void setIndex5(int idx5)
    {
        this.index5 = idx5;
    }
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShortName() {
		return shortname;
	}
	public void setShortName(String shortName) {
		this.shortname = shortName;
	}
	public String getLyric() {
		return lyric;
	}
	public void setLyric(String lyric) {
		this.lyric = lyric;
	}
	
	public void setTitleRaw(String str)
	{
		this.mTitleRaw = str; 
	}
	
	public String getTitleRaw() {
		return mTitleRaw; 
	}
	
	public boolean isFavourite() {
		return isFavourite;
	}
	public void setFavourite(boolean isFavourite) {
		this.isFavourite = isFavourite;
	}
		
	public boolean isMediaMidi() {
		return mediaType == MEDIA_TYPE.MIDI;
	}
//	public void setMediaMidi(boolean isMediaMidi) {
//		this.isMediaMidi = isMediaMidi;
//	}
	
	public boolean isMediaSinger() {
		if (isNewFormat()) {
	        return ((extraInfo & 0x20) != 0); 
	    }
		return mediaType == MEDIA_TYPE.SINGER;
	}
	
	public boolean isRemix() {
		return isRemix;
	}
	public void setRemix(boolean isRemix) {
		this.isRemix = isRemix;
	}
	
	public boolean isHDDSong() {
		return this.mediaType == MEDIA_TYPE.VIDEO; 
	}
	public MEDIA_TYPE getMediaType() {
		return mediaType;
	}
	public void setMediaType(MEDIA_TYPE mediaType) {
		this.mediaType = mediaType;
	}

	public int getTypeABC() {
		return typeABC;
	}
	public void setTypeABC(int typeABC) {
		this.typeABC = typeABC;
	}
	
//	public String getVideoName() {
//		return mVideo;
//	}
//	public void setVideoName(String mVideo) {
//		this.mVideo = mVideo;
//	}
	
//	public void setMediaSinger(boolean isMediaSinger) {
//		this.isMediaSinger = isMediaSinger;
//	}
	
//	public String getMediaName() {
//		return mediaName;
//	}
//	public void setMediaName(String mediaName) {
//		this.mediaName = mediaName;
//	}
//
//	public String getMediaVocalName() {
//		return vocalName;
//	}
//	public void setMediaVocalName(String vocalName) {
//		this.vocalName = vocalName;
//	}
	
	public int getLanguageID() {
		return langID;
	}
	public void setLanguageID(int langID) {
		this.langID = langID;
	}
	
	public LANG_INDEX getSongLanguage() {
		return songLanguage;
	}
	public void setSongLanguage(LANG_INDEX songLanguage) {
		this.songLanguage = songLanguage;
	}

	public int[] getSingerId() {
		return singerId;
	}
	
	public void setSingerId(int[] singerId) {
		this.singerId = singerId;
	}

	public Singer getSinger() {
		return this.mSinger; 
	}
	public void setSinger(Singer aSinger) {
		this.mSinger = aSinger; 
	}
	
	public int[] getMusicianId() {
		return musicianId;
	}
	public void setMusicianId(int[] musicianId) {
		this.musicianId = musicianId;
	}

	public Musician getMusician() {
		return this.mMusician; 
	}
	public void setMusician(Musician aMusician) {
		this.mMusician = aMusician; 
	}
	
//	public int getTypeId() {
//		return typeId;
//	}
//	public void setTypeId(int typeId) {
//		this.typeId = typeId;
//	}
//
//	private SongType getSongType() {
//		return this.mSongType; 
//	}
//	private void setSongType(SongType aSongType) {
//		this.mSongType = aSongType; 
//	}

//	private int Showhide;
//	public int getShowhide() {
//		return Showhide;
//	}
//	public void setShowhide(int showhide) {
//		Showhide = showhide;
//	}

//	public void setPriority(int priority) 
//	{
//		mPriority = priority;
//		
//	}
//	public int getPriority() {
//		return mPriority;
//	}
//	
//	public void setIndex(int indexInArray) {
//		mIndex = indexInArray;
//	}
//	public int getIndex() {
//		return mIndex;
//	}
//
//
//	public void setIsFilled(boolean isFilled) 
//	{
//		mIsFilled = isFilled;
//		
//	}
//	public boolean getIsFilled() {
//		return mIsFilled;
//	}
//	
//	public void setSpace(int space[]) {
//		mSpace = space;
//	}
//	
//	public int[] getSpace()
//	{
//		return mSpace;
//	}
//	public void setSelected(boolean b) {
//		this.isSelected = b;  
//	}
//	
//	public boolean isSelected() 
//	{
//		return isSelected; 
//	}
//	
	
	@Override
	public boolean equals(Object o) {
		if(o == null){
			return false;
		}
		if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
				|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
				 || MyApplication.intSvrModel == MyApplication.SONCA_KBX9 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
				 || MyApplication.intSvrModel == MyApplication.SONCA_TBT){
			return (this.index5 == ((Song) o).index5 || this.index5 == ((Song) o).id);
		} else {
			return (this.index5 == ((Song) o).index5 || this.index5 == ((Song) o).id)
					&& (this.typeABC == ((Song) o).typeABC);	
		}
	}
	
	public Song copySong() {
		Song song = new Song();
		song.id = this.id;
		song.index5 = this.index5;
		song.singerId = this.singerId;
		song.musicianId = this.musicianId;
		song.langID = this.langID;
		song.name = this.name;
		song.shortname = this.shortname;
		song.mTitleRaw = this.mTitleRaw;
		song.lyric = this.lyric;
		song.isFavourite = this.isFavourite;
		song.isRemix = this.isRemix;
		song.songLanguage = this.songLanguage;
		song.mediaType = this.mediaType;
		song.mSinger = this.mSinger;
		song.mMusician = this.mMusician;
		song.typeABC = this.typeABC;
		song.isActive = this.isActive;
		song.isFavourite = this.isFavourite;
		song.spannable = this.spannable;
		return song;
	}
	
//--------------------------------//

	private boolean isActive = false;
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	

	public boolean isSingerLink() {
		return isSingerLink;
	}
	public void setSingerLink(boolean isSingerLink) {
		this.isSingerLink = isSingerLink;
	}

	public Spannable getSpannable() {
		return spannable;
	}

	public void setSpannable(Spannable spannable) {
		this.spannable = spannable;
	}

	public SearchMode getCompareMode() {
		return mode;
	}

	public void setCompareMode(SearchMode mode) {
		this.mode = mode;
	}

	public int getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(int extraInfo) {
		this.extraInfo = extraInfo;
	}

	public boolean isNewFormat()
	{
		return (extraInfo & 0x80) != 0; 
	}
	
	public boolean isCoupleSingers()
	{
		return (extraInfo & 0x08) != 0;
	}
	
	public boolean isSoncaSong() {
		if(isNewFormat())
			return (extraInfo & 0x40) != 0;
		
		return true; 
	}
	
	public boolean isCritical(){
		return ((extraInfo & 0x04) != 0);
	}
	
	public Spannable getSpannableGreen() {
		return spannableLight;
	}

	public void setSpannableGreen(Spannable spannableLight) {
		this.spannableLight = spannableLight;
	}
	
	public Spannable getSpannableNumber() {
		return spannableNumber;
	}

	public void setSpannableNumber(Spannable spannableNumber) {
		this.spannableNumber = spannableNumber;
	}

	private boolean isSingerLink = true;
	private Spannable spannable;
	private Spannable spannableLight;
	private Spannable spannableNumber;
	
	private String playLink = "";
	private String downLink = "";
	private String midiDownLink = "";
	private boolean is2Stream = false;
	private boolean isVocalSinger = false;

	public boolean isVocalSinger() {
		return isVocalSinger;
	}

	public void setVocalSinger(boolean isVocalSinger) {
		this.isVocalSinger = isVocalSinger;
	}

	public boolean isIs2Stream() {
		return is2Stream;
	}

	public void setIs2Stream(boolean is2Stream) {
		this.is2Stream = is2Stream;
	}

	public String getMidiDownLink() {
		return midiDownLink;
	}

	public void setMidiDownLink(String midiDownLink) {
		this.midiDownLink = midiDownLink;
	}

	public String getPlayLink() {
		return playLink;
	}

	public void setPlayLink(String playLink) {
		this.playLink = playLink;
	}

	public String getDownLink() {
		return downLink;
	}

	public void setDownLink(String downLink) {
		this.downLink = downLink;
	}
		
	private boolean isYoutubeSong;

	public boolean isYoutubeSong() {
		return isYoutubeSong;
	}

	public void setYoutubeSong(boolean isYoutubeSong) {
		this.isYoutubeSong = isYoutubeSong;
	}
	
	private String theloaiName = ""; // only used for export list

	public String getTheloaiName() {
		return theloaiName;
	}

	public void setTheloaiName(String theloaiName) {
		this.theloaiName = theloaiName;
	}
	
	private String singerName = ""; // only used for export list

	public String getSingerName() {
		return singerName;
	}

	public void setSingerName(String singerName) {
		this.singerName = singerName;
	}
	
	private boolean isRealYoutubeSong;
	public boolean isRealYoutubeSong() {
		return isRealYoutubeSong;
	}
	
	public void setRealYoutubeSong(boolean isYoutubeSong) {
		this.isRealYoutubeSong = isYoutubeSong;
		this.isYoutubeSong = isYoutubeSong;
	}
	
	private boolean isOfflineSong = false;

	public boolean isOfflineSong() {
		return isOfflineSong;
	}

	public void setOfflineSong(boolean isOfflineSong) {
		this.isOfflineSong = isOfflineSong;
	}
	
	private boolean isHidden = false;

	public boolean isHidden() {
		return isHidden;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}	
	
	private boolean isSambaSong;
	public boolean isSambaSong() {
		return isSambaSong;
	}

	public void setSambaSong(boolean isSambaSong) {
		this.isSambaSong = isSambaSong;
	}	
	
	public String sambaPath = "";
	public String getSambaPath() {
		return sambaPath;
	}

	public void setSambaPath(String sambaPath) {
		this.sambaPath = sambaPath;
	}
	
}
