package vn.com.sonca.database;

import vn.com.sonca.utils.AppConfig;
import android.content.Context; 
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.util.Log;


public class DbHelper extends SQLiteOpenHelper{
	public static final int SongType_CoupleSinger = -1; 
	public static final int SongType_Remix = -2; 
	public static final int SongType_NewSongClub = -3; 
	public static final int SongType_NewVol = -4; 
	public static final int SongType_HotSong = -5; 
	public static final int SongType_LiveSong = -6; 
	public static final int SongType_UpdateSong = -7; 
	public static final int SongType_China = -8;
	public static final int SongType_Online = -9;
	public static final int SongType_Youtube = -10;
	
	public static final String TABLE_SONGS = "ZSONGS";
	public static final String TABLE_SONGS_NEW = "ZSONGS_NEW";
	public static final String TABLE_SONGS_YOUTUBE = "ZSONGS_YOUTUBE";
	public static final String SONG_PK = "Z_PK";
	public static final String SONG_PK_NEW = "Z_PK_NEW";
	public static final String SONG_ID = "ZID"; 
    public static final String SONG_ID5 = "ZINDEX5"; 
	public static final String SONG_SO_MD = "ZSO_MD"; 
	public static final String SONG_SORT = "ZSORT"; 
	public static final String SONG_NAME = "ZNAME"; 
	public static final String SONG_SHORTNAME = "ZSHORTNAME"; 
	public static final String SONG_RAWNAME = "ZTITLERAW"; 
	public static final String SONG_LYRIC = "ZLYRIC"; 
	public static final String SONG_NUMWORDS = "ZNUMWORDS"; 
	public static final String SONG_FAVOUR = "ZFAVOURITE"; 
	public static final String SONG_FREE = "ZFREE"; 
	public static final String SONG_REMIX = "ZREMIX"; 
	public static final String SONG_MEDIATYPE = "ZTYPE"; 
	public static final String SONG_MED_SINGER = "ZMELODY"; 
	public static final String SONG_NEWVOL = "ZNEWSONG"; 
	public static final String SONG_SHOWENABLE = "ZSHOW"; 
	public static final String SONG_ONETOUCH = "ZONETOUCH"; 
	public static final String SONG_SINGER_ID = "ZSINGERID"; 
	public static final String SONG_MUSICIAN_ID = "ZMUSICIANID"; 
	public static final String SONG_LANGUAGE_ID = "ZLANGUAGEID"; 
	public static final String SONG_TYPE_ID = "ZTYPEID"; 
	public static final String SONG_VIDEO_ID = "ZVIDEOINFO"; 
	public static final String SONG_MEDIANAME = "ZMEDIANAME"; 
	public static final String SONG_VOCAL_NAME = "ZVOCAL"; 
	public static final String SONG_PLAYCNT	= "ZPLAYCNT"; 
    public static final String SONG_PRIORITY = "ZPRIORITY"; 
    public static final String SONG_ABCTYPE = "ZABC"; 
    public static final String SONG_EXTRAINFO = "ZEXTRA"; 
    public static final String SONG_CPRIGHT = "ZSCA"; 
    public static final String COMPARE_TYPE = "mType";
    public static final String SONG_SINGERNAME = "ZSINGERNAME"; // rieng cho youtube
    public static final String SONG_THELOAINAME = "ZTHELOAINAME"; // rieng cho youtube
    public static final String SONG_MIDIDOWNLINK = "ZMIDIDOWNLINK"; // rieng cho youtube 9018
    public static final String SONG_2STREAM = "Z2STREAM"; // rieng cho youtube 9018
    public static final String SONG_VOCALSINGER = "ZVOCALSINGER"; // rieng cho youtube 9018
       
    public static final String SONG_YT_PLAYLINK = "ZPLAYLINK";
    public static final String SONG_YT_DOWNLINK = "ZDOWNLINK";
    public static final String SONG_YT_SB = "ZSAMBA";
    public static final String SONG_YT_SBPATH = "ZSAMBAPATH";
    
	public static final String TABLE_MUSICIAN = "ZMUSICIANS";
	public static final String MUSICIAN_ID = "Z_PK"; 
	public static final String MUSICIAN_SORT = "ZSORT"; 
	public static final String MUSICIAN_NAME = "ZNAME"; 
	public static final String MUSICIAN_SHORTNAME = "ZSHORTNAME"; 
	public static final String MUSICIAN_RAWNAME = "ZTITLERAW"; 
	public static final String MUSICIAN_COVER = "ZCOVER"; 
    public static final String MUSICIAN_SONGCOUNT = "ZCOUNT"; 
	public static final String MUSICIAN_COVERID = "ZCID";
	public static final String MUSICIAN_COVERDATA = "ZCDATA";
	public static final String MUSICIAN_LANG_ID = "ZLANGID"; 
	public static final String MUSICIAN_PLAYCNT	= "ZPLAYCNT"; 
    public static final String MUSICIAN_PRIORITY = "ZPRIORITY"; 
	
	public static final String TABLE_SINGER = "ZSINGERS";
	public static final String SINGER_ID = "Z_PK"; 
	public static final String SINGER_SORT = "ZSORT"; 
	public static final String SINGER_NAME = "ZNAME"; 
	public static final String SINGER_SHORTNAME = "ZSHORTNAME"; 
	public static final String SINGER_RAWNAME = "ZTITLERAW"; 
	public static final String SINGER_COVER = "ZCOVER";
    public static final String SINGER_SONGCOUNT = "ZCOUNT"; 
	public static final String SINGER_COVERID = "ZCID";
	public static final String SINGER_COVERDATA = "ZCDATA";
	public static final String SINGER_LANG_ID = "ZLANGID"; 
	public static final String SINGER_PLAYCNT	= "ZPLAYCNT"; 
    public static final String SINGER_PRIORITY = "ZPRIORITY"; 
	
	public static final String TABLE_SONGTYPE = "ZSONGTYPES"; 
	public static final String TYPE_ID = "Z_PK"; 
	public static final String TYPE_SORT = "ZSORT"; 
	public static final String TYPE_NAME = "ZNAME"; 
	public static final String TYPE_SHORTNAME = "ZSHORTNAME"; 
	public static final String TYPE_RAWNAME = "ZTITLERAW"; 
	public static final String TYPE_COVER = "ZCOVER"; 
	public static final String TYPE_COVERID = "ZCID";
	public static final String TYPE_COVERDATA = "ZCDATA";
    public static final String TYPE_SONGCOUNT = "ZCOUNT"; 
	public static final String TYPE_LANG_ID = "ZLANGID"; 
	public static final String TYPE_PLAYCNT	= "ZPLAYCNT"; 
    public static final String TYPE_PRIORITY = "ZPRIORITY"; 
	
	public static final String TABLE_LANGUAGES = "ZLANGUAGES"; 
	public static final String LANG_ID = "Z_PK"; 
	public static final String LANG_SORT = "ZSORT"; 
	public static final String LANG_INDEX = "ZINDEX"; 
	public static final String LANG_NAME = "ZNAME"; 
	public static final String LANG_SHORTNAME = "ZSHORTNAME"; 
	public static final String LANG_RAWNAME = "ZTITLERAW"; 
	public static final String LANG_COVER = "ZCOVER"; 
	public static final String LANG_FONT = "ZFONT"; 
	
	public static final String TABLE_SONGSINGER = "ZSONGSINGERS";
	public static final String SONGSINGER_SOPK = "ZSO_PK"; 
	public static final String SONGSINGER_ATPK = "ZAT_PK"; 
	
	public static final String TABLE_SONGMUSICIAN = "ZSONGMUSICIANS";
	public static final String SONGMUSICIAN_SOPK = "ZSO_PK"; 
	public static final String SONGMUSICIAN_ATPK = "ZAT_PK"; 
	
	public static final String TABLE_SONGMODEL = "ZSONGMODEL"; 
	public static final String SONGMODEL_SOMD = "ZSO_MD";  
	public static final String SONGMODEL_MODEL = "ZMODEL";
	public static final String SONGMODEL_INDEX5 = "ZINDEX5";
	
	public static final String TABLE_SONGNEWVOL = "ZSONGNEWVOL";
	public static final String SONGNEWVOL_SOMD = "ZSO_MD";  
	public static final String SONGNEWVOL_MODEL = "ZMODEL";
	
	public static final String TABLE_VIDEOS = "ZVIDEOS"; 
	public static final String VIDEO_ID		= "Z_PK"; 
	public static final String VIDEO_INDEX	= "ZID"; 
	public static final String VIDEO_PATH	= "ZPATH"; 
	public static final String VIDEO_DESC	= "ZDESC"; 
	
    public static String TABLE_FILE_DATA = "ZFILES"; 
    public static String FILE_ID = "ZPK"; 
    public static String FILE_NAME = "ZNAME"; 
    public static String FILE_SIZE = "ZSIZE";
    public static String FILE_RECORD = "ZRECORD";

    public static String TABLE_UPDATE_INF = "ZUPDATE"; 
    public static String UPDATE_ID = "ZPK"; 
    public static String UPDATE_VOL = "ZVERSION"; 
    public static String UPDATE_DATE = "ZDATE"; 
    public static String UPDATE_NAME = "ZNAME"; 
    public static String UPDATE_DESC = "ZDESC"; 
    public static String UPDATE_COVER = "ZCOVER"; 
    
    public static String TABLE_SONGLYRICS = "ZVLYRICS";  
    public static String LYRICS_ID = "zid"; 
    public static String LYRICS_PINYIN = "zpinyin"; 
    public static String LYRICS_TEXT = "ztext"; 
	
	public static String TABLE_FAVOUR = "FAVOURITES";
	public static String TABLE_RECORD = "RECORDER";
	
	public static final String TABLE_SONG_DATA = "SONGDATA"; 
	public static final String DATA_ID = "Z_PK"; 
	public static final String DATA_INDEX = "ZID"; 
	public static final String DATA_TYPE = "ZTYPE"; 
	public static final String DATA_NAME = "ZNAME"; 
	public static final String DATA_LYR = "ZLYRIC"; 
	public static final String DATA_AUDIO = "ZAUDIO"; 

	public static final String TABLE_PICTURE = "ZPICTURES"; 
	public static final String PICTURE_ID = "ZID"; 
	public static final String PICTURE_DATA = "ZDATA"; 
	
	// Database Name For Karaoke YouTube Function 
	public static final String YOUTUBE_DBName = "kokyoutube.sqlite"; 
	
	// Database Name For Media Function 
	public static final String MEDIA_DBName = "mediadb.sqlite"; 
	
	public static final String DBName = "database.db"; 
	public static final String DBNameStar = "database_star.db"; 
	public static final String UPDATE_DBName = "UPDATE"; 
	public static final String HDD_DBName = "HDD"; 
	public static final String DATA_DBName = "DATA"; 
	public static final String DBExtension = ".sqlite"; 
	Context context;
	
	public DbHelper(Context context, String dbName) {
		super(context, dbName, null, AppConfig.dbVersion);
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	@Override
	public void onCreate(final SQLiteDatabase arg0) {
		// TODO Auto-generated method stub
		arg0.beginTransaction();
		try{
//			Log.d(AppConfig.DBDebugTag, "onCreate(final SQLiteDatabase arg0)"); 
			arg0.setLocale(AppConfig.curLocale); 
			arg0.setTransactionSuccessful();
		}
		catch (Exception e){
			
		}
		finally {
			arg0.endTransaction();
		}
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		arg0.execSQL("DROP DATABASE " + DBName);
		arg0.beginTransaction();
		try{
//			Log.d(AppConfig.DBDebugTag, "onUpgrade(SQLiteDatabase arg0, int oldVersion, int newVersion)"); 
			arg0.setTransactionSuccessful();
		}
		catch (Exception e){
			
		}
		finally {
			arg0.endTransaction();	
		}
	}
	
//////////////////////////////////////////////////////////////////////////////
	
	public static final String INDEX_SONGS = "INDEX_ZSONGS";
	
}
