package vn.com.sonca.database;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

import vn.com.sonca.sk90xxHidden.ViewSearchAdd;
import vn.com.sonca.utils.AppConfig;
import vn.com.sonca.utils.AppConfig.LANG_INDEX;
import vn.com.sonca.utils.AppConfig.MEDIA_TYPE;
import vn.com.sonca.utils.FileManager;
import vn.com.sonca.utils.StringUtils;
import vn.com.sonca.zzzzz.MyApplication;
import vn.com.sonca.MyLog.MyLog;
// import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.params.Language;
import vn.com.sonca.params.Musician;
import vn.com.sonca.params.Singer;
import vn.com.sonca.params.Song;
import vn.com.sonca.params.SongType;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

public class DBInstance {
	public static final String HANDLER_MESSAGE_ID = "id";
	public static final String SPECIAL_CHAR = "-";
	
	public enum SearchType {SEARCH_SONG, SEARCH_MUSICIAN, SEARCH_SINGER, SEARCH_TYPE, SEARCH_NUMBER, SEARCH_LANGUAGE}; 
	public enum SearchMode {MODE_PINYIN, MODE_FULL, MODE_SIGNED, LYRICS, MODE_MIXED}; 

//	public enum TOC_TYPE {	TOCTYPE_SK9000, TOCTYPE_STAR_DISC, TOCTYPE_STAR_USB, TOCTYPE_PLUS_NOCHINESE, 
//							TOCTYPE_PLUS_CHINESE, TOCTYPE_PLUS_NOCHINESE_TYPEABC, TOCTYPE_PLUS_CHINESE_TYPEABC, 
//							TOCTYPE_P2F, TOCTYPE_P1S}; 
//	private TOC_TYPE curTocType = TOC_TYPE.TOCTYPE_SK9000; 
	
	public static final int TOCTYPE_SK9000 = 0;
	public static final int TOCTYPE_STAR_DISC = 1;
	public static final int TOCTYPE_STAR_USB = 2;
	public static final int TOCTYPE_PLUS_NOCHINESE = 3;
	public static final int TOCTYPE_PLUS_CHINESE = 4;
	public static final int TOCTYPE_PLUS_NOCHINESE_TYPEABC = 5;
	public static final int TOCTYPE_PLUS_CHINESE_TYPEABC = 6;
	public static final int TOCTYPE_P2F = 7;
	public static final int TOCTYPE_P1S = 8;
	public static final int TOC_TYPE_BB1 = 9;
	public static final int TOC_TYPE_HIW_HDD_AVI = 10; // hddType = 0
	public static final int TOCTYPE_ARIRIANG = 32;
	public static final int TOCTYPE_MUSIC_CORE = 33;
	public static final int TOCTYPE_VIET_KTV = 34;
	public static final int TOC_TYPE_HIW_HDD_500G = 35; // hddType = 1
	
	private int curTocType = TOCTYPE_SK9000; 
	private int curHDDToc = 0; 
	
	private static boolean isHDDAvailable; 
	private static String HDDDBName = DbHelper.HDD_DBName; 
	
	private static DBInstance instance;
	private static DbHelper openHelper;
	public  SQLiteDatabase db;
	private Context mContext;
	private String curDBName; 
	
	private DBInstance( SQLiteDatabase ins) {
		db = ins;
		if(curDBName == null || curDBName.equals("")){
			curDBName = DbHelper.DBName; 
		}
	}
	
	private DBInstance(Context context, String dbName) {
		try {
			if(dbName == null || dbName.equals("")){
				dbName = DbHelper.DBName; 
			}
			curDBName = dbName; 
			openHelper = new DbHelper(context, curDBName);
			db = openHelper.getWritableDatabase();
		} catch (Exception e) {
//			Log.e("DATABASE ERROR", e.getMessage() + "");
		}

		this.mContext = context;
	}
	public void open(){
		try {
			if(db != null && db.isOpen()) return; 
//        	String dbPath = FileManager.getAppBundlePath(mContext) +"/" + DbHelper.DBName;
//        	if(FileManager.fileExistAtPath(dbPath)){
//	        	this.db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE);   
//        	}else {
//        		String dbFileAssert = FileManager.getAssetsFile(DbHelper.DBName, mContext); 
//        		FileManager.copyFile(new File(dbFileAssert), new File(dbPath)); 
//        		//this.mContext.openOrCreateDatabase(DbHelper.DBName, Context.MODE_PRIVATE, null);
//        	}
			
			db = SQLiteDatabase.openDatabase(mContext.getDatabasePath(curDBName).getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE); 

			//db.setForeignKeyConstraintsEnabled(true);

//			db = mContext.openOrCreateDatabase(DbHelper.DBName, Context.MODE_PRIVATE, null);
		} catch (Exception e) {
//			Log.e("OPEN DB ERROR", e.toString());
		}
	}

	public void setTOCType(int toctype) {
		curTocType = toctype; 
	}
	
	public void setHDDToc(int hddToc, int hddTocType, int indexType)
	{
		if (hddToc > 0) {			
			if(hddTocType > 0){
				curHDDToc = 34 + hddTocType + indexType * 255;				
			} else {
				curHDDToc = TOC_TYPE_HIW_HDD_AVI + indexType * 255;				
			}
		}else {
			curHDDToc = 0; 
		}
	}
	
	public void closePermanent() {
		if(db.isOpen()){
			db.close(); 
			db = null; 
		}
		instance = null; 
	}
	
	public void close() {
//		if (db.isOpen()) {
//			db.close();
//		}
	}
	public static DBInstance getInstance(Context context, String dbName) {
		if(instance == null) {
			instance = new DBInstance(context, dbName);
		} 
		return instance;
	}
	
	public static boolean isHDDAvailable() {
		return isHDDAvailable;
	}

	public static void setHDDAvailable(boolean isHDDAvailable) {
		DBInstance.isHDDAvailable = isHDDAvailable;
	}
	
	private void createSongLyricQueryString(StringBuilder sb, String dbName) {
		if(dbName == null || dbName.equals("")) 
			dbName = "MAIN"; 
		
		DBQueryBuilder.appendSelect(sb); 
		DBQueryBuilder.appendColumns(sb, DbHelper.TABLE_SONG_DATA, new String[]{DbHelper.DATA_INDEX, DbHelper.DATA_LYR});
		DBQueryBuilder.appendTables(sb, dbName + "].[" + DbHelper.TABLE_SONG_DATA); 
	}
	
	private void createSongAudioQueryString(StringBuilder sb, String dbName) {
		if(dbName == null || dbName.equals("")) 
			dbName = "MAIN"; 
		
		DBQueryBuilder.appendSelect(sb); 
		DBQueryBuilder.appendColumns(sb, DbHelper.TABLE_SONG_DATA, new String[]{DbHelper.DATA_INDEX, DbHelper.DATA_AUDIO});
		DBQueryBuilder.appendTables(sb, dbName + "].[" + DbHelper.TABLE_SONG_DATA); 
	}
	
	private void createSongInfoQueryStringHIW(StringBuilder sb){
		String dbName = "MAIN"; 
		
		DBQueryBuilder.appendSelect(sb); 
		
		DBQueryBuilder.appendColumns(sb, DbHelper.TABLE_SONGS, new String[]{DbHelper.SONG_ID});
		sb.append(","); 
		DBQueryBuilder.appendColumns(sb, DbHelper.TABLE_SONGMODEL, new String[]{DbHelper.SONGMODEL_INDEX5});
		sb.append(","); 
		DBQueryBuilder.appendColumns(sb, DbHelper.TABLE_SONGS, new String[]{DbHelper.SONG_NAME});
		
		DBQueryBuilder.appendTables(sb, dbName + "].[" + DbHelper.TABLE_SONGS); 
		DBQueryBuilder.appendInnerJoin(sb, dbName + "].[" + DbHelper.TABLE_SONGMODEL,  
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SO_MD)
				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_SOMD));
	}

	private void createSongInfoQueryString(StringBuilder sb, String dbName) {
		createSongInfoQueryString(sb, dbName, -1); 
	}
	
	private void createSongInfoQueryString(StringBuilder sb, String dbName, int type)
	{
		if(dbName == null || dbName.equals("")) 
			dbName = "MAIN"; 
		
		DBQueryBuilder.appendSelect(sb); 
		
		if(curTocType != TOCTYPE_SK9000) {
			DBQueryBuilder.appendColumns(sb, DbHelper.TABLE_SONGS, new String[]{DbHelper.SONG_ID});
			sb.append(","); 
			DBQueryBuilder.appendColumns(sb, DbHelper.TABLE_SONGMODEL, new String[]{DbHelper.SONGMODEL_INDEX5});
			sb.append(","); 
			DBQueryBuilder.appendColumns(sb, DbHelper.TABLE_SONGS, new String[]{DbHelper.SONG_NAME/*,DbHelper.SONG_SHORTNAME*/, DbHelper.SONG_RAWNAME, DbHelper.SONG_LYRIC, 
					DbHelper.SONG_MEDIATYPE, DbHelper.SONG_REMIX, DbHelper.SONG_FAVOUR,DbHelper.SONG_ABCTYPE, DbHelper.SONG_EXTRAINFO});
		}else {
			DBQueryBuilder.appendColumns(sb, DbHelper.TABLE_SONGS, new String[]{DbHelper.SONG_ID, DbHelper.SONG_ID5, DbHelper.SONG_NAME/*,DbHelper.SONG_SHORTNAME*/, DbHelper.SONG_RAWNAME, DbHelper.SONG_LYRIC, 
				DbHelper.SONG_MEDIATYPE, DbHelper.SONG_REMIX, DbHelper.SONG_FAVOUR,DbHelper.SONG_ABCTYPE, DbHelper.SONG_EXTRAINFO});
		}
		
		
//		sb.append(","); 
//		sb.append(","); 
//		DBQueryBuilder.appendColumns(sb, DbHelper.TABLE_MUSICIAN, new String[]{DbHelper.MUSICIAN_ID, DbHelper.MUSICIAN_NAME});
//		sb.append(","); 
//		DBQueryBuilder.appendColumns(sb, DbHelper.TABLE_SINGER, new String[]{DbHelper.SINGER_ID, DbHelper.SINGER_NAME});
	
		sb.append(","); 
		sb.append("group_concat(distinct " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SINGER, DbHelper.SINGER_NAME) + ")"); 
		sb.append(","); 
		sb.append("group_concat(distinct " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SINGER, DbHelper.SINGER_ID) + ")"); 
		
		sb.append(","); 
		sb.append("group_concat(distinct " + DBQueryBuilder.getColumnString(DbHelper.TABLE_MUSICIAN, DbHelper.MUSICIAN_NAME) + ")"); 
		sb.append(","); 
		sb.append("group_concat(distinct " + DBQueryBuilder.getColumnString(DbHelper.TABLE_MUSICIAN, DbHelper.MUSICIAN_ID) + ")"); 
		
		if(type != -1) {
			sb.append(",'" + type + "' AS " + DbHelper.COMPARE_TYPE); 
		}
//		sb.append(","); 
//		DBQueryBuilder.appendColumns(sb, DbHelper.TABLE_LANGUAGES, new String[]{DbHelper.LANG_INDEX});
		
		DBQueryBuilder.appendTables(sb, dbName + "].[" + DbHelper.TABLE_SONGS); 
		
		DBQueryBuilder.appendInnerJoin(sb, dbName + "].[" + DbHelper.TABLE_SONGSINGER,  
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_PK)
				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGSINGER, DbHelper.SONGSINGER_SOPK));
		DBQueryBuilder.appendInnerJoin(sb, dbName + "].[" + DbHelper.TABLE_SINGER, 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGSINGER, DbHelper.SONGSINGER_ATPK)
				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SINGER, DbHelper.SINGER_ID));
		
		DBQueryBuilder.appendInnerJoin(sb, dbName + "].[" + DbHelper.TABLE_SONGMUSICIAN,  
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_PK)
				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMUSICIAN, DbHelper.SONGMUSICIAN_SOPK)); 
		DBQueryBuilder.appendInnerJoin(sb, dbName + "].[" + DbHelper.TABLE_MUSICIAN, 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMUSICIAN, DbHelper.SONGMUSICIAN_ATPK)
				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_MUSICIAN, DbHelper.MUSICIAN_ID));
		
		if(curTocType != TOCTYPE_SK9000) {
			DBQueryBuilder.appendInnerJoin(sb, dbName + "].[" + DbHelper.TABLE_SONGMODEL,  
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SO_MD)
					+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_SOMD));
		}
		
		
//		DBQueryBuilder.appendInnerJoin(sb, dbName + "].[" + DbHelper.TABLE_MUSICIAN, 
//				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMUSICIAN, DbHelper.SONGMUSICIAN_ATPK)
//				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_MUSICIAN, DbHelper.MUSICIAN_ID)); 
		
		
//		DBQueryBuilder.appendInnerJoin(sb, dbName + "].[" + DbHelper.TABLE_SINGER, 
//				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SINGER_ID)
//				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SINGER, DbHelper.SINGER_ID)); 
//		DBQueryBuilder.appendInnerJoin(sb, dbName + "].[" + DbHelper.TABLE_SONGTYPE, 
//				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_TYPE_ID)
//				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGTYPE, DbHelper.TYPE_ID)); 
//		DBQueryBuilder.appendInnerJoin(sb, dbName + "].[" + DbHelper.TABLE_LANGUAGES, 
//				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_LANGUAGE_ID)
//				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_LANGUAGES, DbHelper.LANG_ID)); 
	}
	
	private void createSongInfoQueryString_YouTube(StringBuilder sb, String dbName, int type)
	{
		if(dbName == null || dbName.equals("")) 
			dbName = "MAIN"; 
		
		DBQueryBuilder.appendSelect(sb); 
		
		DBQueryBuilder.appendColumns(sb, DbHelper.TABLE_SONGS_YOUTUBE, new String[]{DbHelper.SONG_ID, DbHelper.SONG_ID5, DbHelper.SONG_NAME/*,DbHelper.SONG_SHORTNAME*/, DbHelper.SONG_RAWNAME, DbHelper.SONG_LYRIC, 
				DbHelper.SONG_MEDIATYPE, DbHelper.SONG_REMIX, DbHelper.SONG_FAVOUR,DbHelper.SONG_ABCTYPE, DbHelper.SONG_EXTRAINFO});

		sb.append(","); 
		DBQueryBuilder.appendColumns(sb, DbHelper.TABLE_SONGS_YOUTUBE, new String[]{DbHelper.SONG_YT_PLAYLINK, DbHelper.SONG_YT_DOWNLINK, DbHelper.SONG_SINGERNAME, DbHelper.SONG_THELOAINAME, DbHelper.SONG_MIDIDOWNLINK, DbHelper.SONG_2STREAM, DbHelper.SONG_VOCALSINGER, DbHelper.SONG_YT_SB, DbHelper.SONG_YT_SBPATH});
		
		if(type != -1) {
			sb.append(",'" + type + "' AS " + DbHelper.COMPARE_TYPE); 
		}

		DBQueryBuilder.appendTables(sb, dbName + "].[" + DbHelper.TABLE_SONGS_YOUTUBE); 
		
		if(curTocType != TOCTYPE_SK9000) {
			DBQueryBuilder.appendInnerJoin(sb, dbName + "].[" + DbHelper.TABLE_SONGMODEL,  
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SO_MD)
					+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_SOMD));
		}
		
	}
	
	private void createSongInfoQueryString_New(StringBuilder sb, String dbName, int type)
	{
		if(dbName == null || dbName.equals("")) 
			dbName = "MAIN"; 
		
		DBQueryBuilder.appendSelect(sb); 
		
		if(curTocType != TOCTYPE_SK9000) {
			DBQueryBuilder.appendColumns(sb, DbHelper.TABLE_SONGS_NEW, new String[]{DbHelper.SONG_ID});
			sb.append(","); 
			DBQueryBuilder.appendColumns(sb, DbHelper.TABLE_SONGMODEL, new String[]{DbHelper.SONGMODEL_INDEX5});
			sb.append(","); 
			DBQueryBuilder.appendColumns(sb, DbHelper.TABLE_SONGS_NEW, new String[]{DbHelper.SONG_NAME/*,DbHelper.SONG_SHORTNAME*/, DbHelper.SONG_RAWNAME, DbHelper.SONG_LYRIC, 
					DbHelper.SONG_MEDIATYPE, DbHelper.SONG_REMIX, DbHelper.SONG_FAVOUR,DbHelper.SONG_ABCTYPE, DbHelper.SONG_EXTRAINFO});
		}else {
			DBQueryBuilder.appendColumns(sb, DbHelper.TABLE_SONGS_NEW, new String[]{DbHelper.SONG_ID, DbHelper.SONG_ID5, DbHelper.SONG_NAME/*,DbHelper.SONG_SHORTNAME*/, DbHelper.SONG_RAWNAME, DbHelper.SONG_LYRIC, 
				DbHelper.SONG_MEDIATYPE, DbHelper.SONG_REMIX, DbHelper.SONG_FAVOUR,DbHelper.SONG_ABCTYPE, DbHelper.SONG_EXTRAINFO});
		}
		
		
//		sb.append(","); 
//		sb.append(","); 
//		DBQueryBuilder.appendColumns(sb, DbHelper.TABLE_MUSICIAN, new String[]{DbHelper.MUSICIAN_ID, DbHelper.MUSICIAN_NAME});
//		sb.append(","); 
//		DBQueryBuilder.appendColumns(sb, DbHelper.TABLE_SINGER, new String[]{DbHelper.SINGER_ID, DbHelper.SINGER_NAME});
	
		sb.append(","); 
		sb.append("group_concat(distinct " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SINGER, DbHelper.SINGER_NAME) + ")"); 
		sb.append(","); 
		sb.append("group_concat(distinct " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SINGER, DbHelper.SINGER_ID) + ")"); 
		
		sb.append(","); 
		sb.append("group_concat(distinct " + DBQueryBuilder.getColumnString(DbHelper.TABLE_MUSICIAN, DbHelper.MUSICIAN_NAME) + ")"); 
		sb.append(","); 
		sb.append("group_concat(distinct " + DBQueryBuilder.getColumnString(DbHelper.TABLE_MUSICIAN, DbHelper.MUSICIAN_ID) + ")"); 
		
		if(type != -1) {
			sb.append(",'" + type + "' AS " + DbHelper.COMPARE_TYPE); 
		}
//		sb.append(","); 
//		DBQueryBuilder.appendColumns(sb, DbHelper.TABLE_LANGUAGES, new String[]{DbHelper.LANG_INDEX});
		
		DBQueryBuilder.appendTables(sb, dbName + "].[" + DbHelper.TABLE_SONGS_NEW); 
		
		DBQueryBuilder.appendInnerJoin(sb, dbName + "].[" + DbHelper.TABLE_SONGSINGER,  
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_NEW, DbHelper.SONG_PK)
				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGSINGER, DbHelper.SONGSINGER_SOPK));
		DBQueryBuilder.appendInnerJoin(sb, dbName + "].[" + DbHelper.TABLE_SINGER, 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGSINGER, DbHelper.SONGSINGER_ATPK)
				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SINGER, DbHelper.SINGER_ID));
		
		DBQueryBuilder.appendInnerJoin(sb, dbName + "].[" + DbHelper.TABLE_SONGMUSICIAN,  
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_NEW, DbHelper.SONG_PK)
				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMUSICIAN, DbHelper.SONGMUSICIAN_SOPK)); 
		DBQueryBuilder.appendInnerJoin(sb, dbName + "].[" + DbHelper.TABLE_MUSICIAN, 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMUSICIAN, DbHelper.SONGMUSICIAN_ATPK)
				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_MUSICIAN, DbHelper.MUSICIAN_ID));
		
		if(curTocType != TOCTYPE_SK9000) {
			DBQueryBuilder.appendInnerJoin(sb, dbName + "].[" + DbHelper.TABLE_SONGMODEL,  
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_NEW, DbHelper.SONG_SO_MD)
					+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_SOMD));
		}
		
		
//		DBQueryBuilder.appendInnerJoin(sb, dbName + "].[" + DbHelper.TABLE_MUSICIAN, 
//				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMUSICIAN, DbHelper.SONGMUSICIAN_ATPK)
//				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_MUSICIAN, DbHelper.MUSICIAN_ID)); 
		
		
//		DBQueryBuilder.appendInnerJoin(sb, dbName + "].[" + DbHelper.TABLE_SINGER, 
//				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SINGER_ID)
//				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SINGER, DbHelper.SINGER_ID)); 
//		DBQueryBuilder.appendInnerJoin(sb, dbName + "].[" + DbHelper.TABLE_SONGTYPE, 
//				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_TYPE_ID)
//				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGTYPE, DbHelper.TYPE_ID)); 
//		DBQueryBuilder.appendInnerJoin(sb, dbName + "].[" + DbHelper.TABLE_LANGUAGES, 
//				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_LANGUAGE_ID)
//				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_LANGUAGES, DbHelper.LANG_ID)); 
	}
	
	private void createSongInfoQueryString_ID(StringBuilder sb, String dbName, int type)
	{
		if(dbName == null || dbName.equals("")) 
			dbName = "MAIN"; 
		
		DBQueryBuilder.appendSelect(sb); 
		DBQueryBuilder.appendColumns(sb, DbHelper.TABLE_SONGS, new String[]{DbHelper.SONG_ID});

		DBQueryBuilder.appendTables(sb, dbName + "].[" + DbHelper.TABLE_SONGS); 
		
		DBQueryBuilder.appendInnerJoin(sb, dbName + "].[" + DbHelper.TABLE_SONGSINGER,  
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_PK)
				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGSINGER, DbHelper.SONGSINGER_SOPK));
		DBQueryBuilder.appendInnerJoin(sb, dbName + "].[" + DbHelper.TABLE_SINGER, 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGSINGER, DbHelper.SONGSINGER_ATPK)
				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SINGER, DbHelper.SINGER_ID));
		
		DBQueryBuilder.appendInnerJoin(sb, dbName + "].[" + DbHelper.TABLE_SONGMUSICIAN,  
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_PK)
				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMUSICIAN, DbHelper.SONGMUSICIAN_SOPK)); 
	}
	
	private void createSingerInfoQueryString(StringBuilder sb, String dbName, boolean join)
	{
		createSingerInfoQueryString(sb, dbName, join, -1); 
	}
	
	private void createSingerInfoQueryString(StringBuilder sb, String dbName, boolean join, int type) {
		if(dbName == null || dbName.equals("")) 
			dbName = "MAIN";
		
		DBQueryBuilder.appendSelect(sb); 
		DBQueryBuilder.appendColumns(sb, DbHelper.TABLE_SINGER, new String[]{DbHelper.SINGER_ID, DbHelper.SINGER_NAME, DbHelper.SINGER_SHORTNAME, DbHelper.SINGER_COVERID/*, DbHelper.SINGER_COVER*//*, DbHelper.SINGER_COVERDATA/*, DbHelper.SINGER_PRIORITY*/}); 
		
		if(type != -1) {
			sb.append(",'" + type + "' AS " + DbHelper.COMPARE_TYPE); 
		}
		
		DBQueryBuilder.appendTables(sb, dbName + "].[" + DbHelper.TABLE_SINGER); 
		if(join)
			DBQueryBuilder.appendInnerJoin(sb, dbName + "].[" + DbHelper.TABLE_SONGS, 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SINGER_ID)
				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SINGER, DbHelper.SINGER_ID)); 
	}
	
	private void createMusicianInfoQueryString(StringBuilder strBuilder, String dbName, boolean join) 
	{
		createMusicianInfoQueryString(strBuilder, dbName, join, -1); 
	}
	
	private void createMusicianInfoQueryString(StringBuilder strBuilder, String dbName, boolean join, int type) {
		if(dbName == null || dbName.equals("")) 
			dbName = "MAIN";
		
		DBQueryBuilder.appendSelect(strBuilder); 
		DBQueryBuilder.appendColumns(strBuilder, DbHelper.TABLE_MUSICIAN, new String[]{DbHelper.MUSICIAN_ID, DbHelper.MUSICIAN_NAME, DbHelper.MUSICIAN_SHORTNAME, DbHelper.MUSICIAN_COVERID/*, DbHelper.MUSICIAN_COVER, DbHelper.MUSICIAN_COVERDATA*//*, DbHelper.MUSICIAN_PRIORITY*/});
		if(type != -1) {
			strBuilder.append(",'" + type + "' AS " + DbHelper.COMPARE_TYPE); 
		}
		DBQueryBuilder.appendTables(strBuilder, dbName + "].[" + DbHelper.TABLE_MUSICIAN); 
		if(join)
		DBQueryBuilder.appendInnerJoin(strBuilder, dbName + "].[" + DbHelper.TABLE_SONGS, 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_MUSICIAN_ID)
				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_MUSICIAN, DbHelper.MUSICIAN_ID)); 
	}
	
	private void createSongTypeInfoQueryString(StringBuilder strBuilder, String dbName, boolean join) {
		if(dbName == null || dbName.equals("")) 
			dbName = "MAIN";
		
		DBQueryBuilder.appendSelect(strBuilder); 
		DBQueryBuilder.appendColumns(strBuilder, DbHelper.TABLE_SONGTYPE, new String[]{DbHelper.TYPE_ID, DbHelper.TYPE_NAME/*, DbHelper.TYPE_COVER*/, DbHelper.TYPE_COVERDATA/*, DbHelper.TYPE_PRIORITY*/}); 
		DBQueryBuilder.appendTables(strBuilder, dbName + "].[" + DbHelper.TABLE_SONGTYPE); 
		if(join)
		DBQueryBuilder.appendInnerJoin(strBuilder, dbName + "].[" + DbHelper.TABLE_SONGS, 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_TYPE_ID)
				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGTYPE, DbHelper.TYPE_ID)); 
	}
	
//	private void createUpdateInfoQueryString(StringBuilder strBuilder, String dbName) {
//		if(dbName == null || dbName.equals("")) 
//			dbName = "MAIN"; 
//		
//		DBQueryBuilder.appendSelect(strBuilder); 
//		DBQueryBuilder.appendColumns(strBuilder, new String[]{DbHelper.UPDATE_ID, DbHelper.UPDATE_VOL, DbHelper.UPDATE_DATE, DbHelper.UPDATE_NAME, DbHelper.UPDATE_DESC, DbHelper.UPDATE_COVER}); 
//		if(dbName.equals("")){
//		DBQueryBuilder.appendTables(strBuilder, DbHelper.TABLE_UPDATE_INF); 
//		}else {
//			DBQueryBuilder.appendTables(strBuilder, dbName + "].[" + DbHelper.TABLE_UPDATE_INF); 
//		}
//	}
	
	private ArrayList<Song> parseSongInfoQueryResult(Cursor cursorResult) {

		ArrayList<Song> arr = new ArrayList<Song>();
// 		if (cursorResult.getCount() > 0) {
			while (cursorResult.moveToNext()) {
				Song s = new Song();
				s.setId(cursorResult.getInt(0));
				s.setIndex5(cursorResult.getInt(1));
				String name = cursorResult.getString(2);
				s.setName(name);
				// s.setShortName(cursorResult.getString(3));
				String nameraw = cursorResult.getString(3);
				s.setLyric(cutText(40, cursorResult.getString(4)));
				s.setMediaType(MEDIA_TYPE.values()[cursorResult.getInt(5)]);
				s.setRemix(cursorResult.getInt(6) == 1);
				s.setFavourite(cursorResult.getInt(7) == 1);
				s.setTypeABC(cursorResult.getInt(8));
				s.setExtraInfo(cursorResult.getInt(9));
					//-----------------//
				String nameSinger = cursorResult.getString(10);
				s.setSinger(new Singer(nameSinger, cutText(14, nameSinger)));
				String singerID = cursorResult.getString(11);
				String[] idxs = singerID.split(",");
				int[] idxInt = new int[idxs.length];
				for (int i = 0; i < idxInt.length; i++) {
					idxInt[i] = Integer.parseInt(idxs[i]);
				}
				s.setSingerId(idxInt);
				//-----------------//
				if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && (MyApplication.flagSmartK_801 || MyApplication.flagSmartK_CB)){
					boolean check = MyApplication.checkOfflineSong(s.getId());
					s.setOfflineSong(check);
				}
				
				boolean flagSpecial801 = false;
				if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && (MyApplication.flagSmartK_801 || MyApplication.flagSmartK_CB)
						&& s.isOfflineSong() == false){
					flagSpecial801 = true;
				}
				
				//-----------------//
				String textID = "";
				boolean boolRed = s.getId() == s.getIndex5();
				if(MyApplication.intWifiRemote == MyApplication.SONCA_KARTROL 
						&& MyApplication.intRemoteModel != 0){
					textID = s.getIndex5() + "";
				} else if (flagSpecial801){
					textID = convertIdSong(s.getId()) + " | ";
				} else {
					if (boolRed) {
						textID = convertIdSong(s.getId()) + " | -";
					} else {			
						textID = convertIdSong(s.getId()) + " | " + s.getIndex5();
					}
				}
				SpannableString wordtoSpan = new SpannableString(textID);
				s.setSpannableNumber(wordtoSpan);
			
				//-----------------//
				String nameMusician = cursorResult.getString(12);
				s.setMusician(new Musician(nameMusician, cutText(14, nameMusician)));
				String musicianID = cursorResult.getString(13);
				idxs = musicianID.split(",");
				idxInt = new int[idxs.length];
				for (int i = 0; i < idxInt.length; i++) {
					idxInt[i] = Integer.parseInt(idxs[i]);
				}
				s.setMusicianId(idxInt);
				s.setSpannable(createSpannable(name, nameraw , "", 1));
				arr.add(s); 
 			} 
//		} 
		cursorResult.close();
		return arr; 
	}
	
	private ArrayList<Song> parseSongInfoQueryResult_YouTube(Cursor cursorResult) {

		ArrayList<Song> arr = new ArrayList<Song>();
// 		if (cursorResult.getCount() > 0) {
			while (cursorResult.moveToNext()) {
				Song s = new Song();
				s.setId(cursorResult.getInt(0));
				s.setIndex5(cursorResult.getInt(1));
				String name = cursorResult.getString(2);
				s.setName(name);
				// s.setShortName(cursorResult.getString(3));
				String nameraw = cursorResult.getString(3);
				s.setLyric(cutText(40, cursorResult.getString(4)));
				s.setMediaType(MEDIA_TYPE.values()[cursorResult.getInt(5)]);
				s.setRemix(cursorResult.getInt(6) == 1);
				s.setFavourite(cursorResult.getInt(7) == 1);
				s.setTypeABC(cursorResult.getInt(8));
				s.setExtraInfo(cursorResult.getInt(9));
				// -----------------//
				s.setSinger(new Singer("-", "-"));
				int[] idxInt = new int[1];
				idxInt[0] = 0;
				s.setSingerId(idxInt);
				// -----------------//
				s.setPlayLink(cursorResult.getString(10));
				s.setDownLink(cursorResult.getString(11));
				s.setSingerName(cursorResult.getString(12));
				s.setTheloaiName(cursorResult.getString(13));
				
				s.setMidiDownLink(cursorResult.getString(14));
				s.setIs2Stream(cursorResult.getInt(15) == 1);
				s.setVocalSinger(cursorResult.getInt(16) == 1);
				
				s.setSambaSong(cursorResult.getInt(17) == 1);
				s.setSambaPath(cursorResult.getString(18));
				
				s.setYoutubeSong(true);
				
				//-----------------//			
				s.setSpannable(createSpannable(name, nameraw , "", 1));
				arr.add(s); 
				
				arr.add(s); 
 			} 
//		} 
		cursorResult.close();
		return arr; 
	}	
	
	private String convertIdSong(int id){
		String stringId = String.valueOf(id);
		switch (stringId.length()) {
		case 1: 	return "00000" + stringId;
		case 2: 	return "0000" + stringId;
		case 3: 	return "000" + stringId;
		case 4: 	return "00" + stringId;
		case 5: 	return "0" + stringId;
		default: 	break;
		}
		return stringId;
	}
	
	private ArrayList<Singer> parseSingerInfoQueryResult(Cursor resultCursor) {
		ArrayList<Singer> arr = new ArrayList<Singer>();
//		if(resultCursor.getCount() > 0) {
			while(resultCursor.moveToNext())
			{
				Singer aSinger = new Singer(); 
				aSinger.setID(resultCursor.getInt(resultCursor.getColumnIndex(DbHelper.SINGER_ID))); 
				aSinger.setName(resultCursor.getString(resultCursor.getColumnIndex(DbHelper.SINGER_NAME))); 
				aSinger.setShortName(resultCursor.getString(resultCursor.getColumnIndex(DbHelper.SINGER_SHORTNAME))); 
				aSinger.setCoverID(resultCursor.getInt(resultCursor.getColumnIndex(DbHelper.SINGER_COVERID)));
/*				
				aSinger.setCover(resultCursor.getString(resultCursor.getColumnIndex(DbHelper.SINGER_COVER))); 
				aSinger.setPriority(resultCursor.getInt(resultCursor.getColumnIndex(DbHelper.SINGER_PRIORITY))); 
				byte[] data = resultCursor.getBlob(resultCursor.getColumnIndex(DbHelper.SINGER_COVERDATA)); 
				if(data != null && data.length > 0)
					aSinger.setCoverImageStream(new ByteArrayInputStream(data));
*/				
				int compareType = resultCursor.getColumnIndex(DbHelper.COMPARE_TYPE); 
				if(compareType != -1) {
					aSinger.setCompareMode(SearchMode.values()[resultCursor.getInt(compareType)]); 
				}
				
				arr.add(aSinger); 
			}
//		}
		resultCursor.close();
		return arr; 
	}
	
	private ArrayList<Musician> parseMuscianInfoQueryResult(Cursor resultCursor) {
		ArrayList<Musician> arr = new ArrayList<Musician>();
//		if(resultCursor.getCount() > 0) {
			while(resultCursor.moveToNext()){			
				Musician aMusician = new Musician(); 
				aMusician.setID(resultCursor.getInt(resultCursor.getColumnIndex(DbHelper.MUSICIAN_ID))); 
				aMusician.setName(resultCursor.getString(resultCursor.getColumnIndex(DbHelper.MUSICIAN_NAME))); 
				aMusician.setShortName(resultCursor.getString(resultCursor.getColumnIndex(DbHelper.MUSICIAN_SHORTNAME))); 
				aMusician.setCoverID(resultCursor.getInt(resultCursor.getColumnIndex(DbHelper.MUSICIAN_COVERID)));
//				Log.e("", "cover id = " + aMusician.getCoverID()); 
//				aMusician.setCover(resultCursor.getString(resultCursor.getColumnIndex(DbHelper.MUSICIAN_COVER))); 
//				aMusician.setPriority(resultCursor.getInt(resultCursor.getColumnIndex(DbHelper.MUSICIAN_PRIORITY))); 
		//		byte[] data = resultCursor.getBlob(resultCursor.getColumnIndex(DbHelper.MUSICIAN_COVERDATA)); 
		//		if(data != null && data.length > 0)
		//			aMusician.setCoverImageStream(new ByteArrayInputStream(data));
				
				int compareType = resultCursor.getColumnIndex(DbHelper.COMPARE_TYPE); 
				if(compareType != -1) {
					aMusician.setCompareMode(SearchMode.values()[resultCursor.getInt(compareType)]); 
				}
				
				arr.add(aMusician);
			}
//		}
		resultCursor.close();
		return arr; 
	}
	
	private ArrayList<SongType> parseSongTypeInfoQueryResult(Cursor resultCursor) {
		ArrayList<SongType> arr = new ArrayList<SongType>();
		if(resultCursor.getCount() > 0) {
			while(resultCursor.moveToNext())
			{
				SongType aMusician = new SongType(); 
				aMusician.setID(resultCursor.getInt(resultCursor.getColumnIndex(DbHelper.TYPE_ID))); 
				aMusician.setName(resultCursor.getString(resultCursor.getColumnIndex(DbHelper.TYPE_NAME))); 
//				aMusician.setCover(resultCursor.getString(resultCursor.getColumnIndex(DbHelper.TYPE_COVER))); 
//				aMusician.setmPriority(resultCursor.getInt(resultCursor.getColumnIndex(DbHelper.TYPE_PRIORITY))); 
				byte[] data = resultCursor.getBlob(resultCursor.getColumnIndex(DbHelper.TYPE_COVERDATA)); 
				if(data != null && data.length > 0)
					aMusician.setCoverImageStream(new ByteArrayInputStream(data)); 
				arr.add(aMusician); 
			}
		}
		
		resultCursor.close();
		return arr; 
	}
/*	
	private ArrayList<UpdateInfo> parseUpdateInfoQueryResult(Cursor resultCursor) {
		ArrayList<UpdateInfo> arr = new ArrayList<UpdateInfo>();
		if(resultCursor.getCount() > 0) {
			while(resultCursor.moveToNext())
			{
				UpdateInfo info = new UpdateInfo(); 
				info.setId(resultCursor.getInt(resultCursor.getColumnIndex(DbHelper.UPDATE_ID))); 
				info.setVol(resultCursor.getInt(resultCursor.getColumnIndex(DbHelper.UPDATE_VOL)));
				info.setDate(resultCursor.getLong(resultCursor.getColumnIndex(DbHelper.UPDATE_DATE))); 
				info.setName(resultCursor.getString(resultCursor.getColumnIndex(DbHelper.UPDATE_NAME))); 
				info.setDescription(resultCursor.getString(resultCursor.getColumnIndex(DbHelper.UPDATE_DESC))); 
				
				byte[] data = resultCursor.getBlob(resultCursor.getColumnIndex(DbHelper.UPDATE_COVER)); 
				if(data != null && data.length > 0)
					info.setCoverImageStream(new ByteArrayInputStream(data)); 
				arr.add(info); 
			}
		}
		return arr; 
	}
*/	
	public ByteArrayInputStream getSongAudioStream(String songIndex) {
		StringBuilder strBuilder = new StringBuilder();  
		ArrayList<String> dataFiles = getAllSongDataFile(); 
		
		boolean hasUnion = false; 
		String dataPath = ""; 
		for(String dbName : dataFiles) {
			
//			dataPath = mContext.getDatabasePath(dbName + DbHelper.DBExtension).getAbsolutePath(); 
			dataPath = FileManager.getExtStorageFilePath(dbName + DbHelper.DBExtension).getAbsolutePath(); 
			attachNewDatabase(dataPath, dbName); 
			
			if(hasUnion) {
				strBuilder.append(" UNION ALL "); 
			}
			createSongAudioQueryString(strBuilder, dbName); 
			DBQueryBuilder.appendWhereFromFormat(strBuilder, "%K=%@", DbHelper.DATA_INDEX, songIndex); 
			
			hasUnion = true; 
		}
		DBQueryBuilder.appendLimitOffset(strBuilder, 1, 0); 
		
		Cursor resultCursor = db.rawQuery(strBuilder.toString(), null); 
		
		ByteArrayInputStream inputStream = null; 
		if(resultCursor.getCount() > 0) {
			while(resultCursor.moveToNext()){
				inputStream = new ByteArrayInputStream(resultCursor.getBlob(resultCursor.getColumnIndex(DbHelper.DATA_AUDIO))); 
			}
		}
		for(String dbName : dataFiles) {
			detachDatabase(dbName); 
		}
		
		resultCursor.close();
		
		return inputStream; 
	}
	
	public ByteArrayInputStream getSongLyricStream(String songIndex) {
		StringBuilder strBuilder = new StringBuilder();  
		ArrayList<String> dataFiles = getAllSongDataFile(); 
		
		String dataPath = ""; 
		boolean hasUnion = false; 
		for(String dbName : dataFiles) {
			// Attach data database
//			dataPath = mContext.getDatabasePath(dbName + DbHelper.DBExtension).getAbsolutePath();
			dataPath = FileManager.getExtStorageFilePath(dbName + DbHelper.DBExtension).getAbsolutePath();
			attachNewDatabase(dataPath, dbName); 
			
			if(hasUnion) {
				strBuilder.append(" UNION ALL "); 
			}
			createSongLyricQueryString(strBuilder, dbName); 
			DBQueryBuilder.appendWhereFromFormat(strBuilder, "%K=%@", DbHelper.DATA_INDEX, songIndex); 
			
			hasUnion = true; 
		}
		DBQueryBuilder.appendLimitOffset(strBuilder, 1, 0); 
		
		Cursor resultCursor = db.rawQuery(strBuilder.toString(), null); 
		
		ByteArrayInputStream inputStream = null; 
		if(resultCursor.getCount() > 0) {
			while(resultCursor.moveToNext()){
				inputStream = new ByteArrayInputStream(resultCursor.getBlob(resultCursor.getColumnIndex(DbHelper.DATA_LYR))); 
			}
		}
		
		for(String dbName : dataFiles) {
			detachDatabase(dbName); 
		}
		
		resultCursor.close();
		
		return inputStream; 
	}
			
	public ArrayList<Song> searchSongIDList_YouTube(int[] idList, int[]typeABCList) {
		StringBuilder idString = new StringBuilder("(");
		for(int i = 0; i < idList.length; i++) {
			if(i < idList.length -1) {
				idString.append(idList[i] + ","); 
			}else {
				idString.append(idList[i]); 
			}
		}
		idString.append(")"); 
//		Log.e("", "ids: " + idString.toString()); 
		
		StringBuilder strBuilder = new StringBuilder(); 
		createSongInfoQueryString_YouTube(strBuilder, "", -1); 
		
		DBQueryBuilder.appendWhereFromFormat(strBuilder, false, "(%K IN %@) OR (%K IN %@)", DbHelper.SONG_ID, idString.toString(), DbHelper.SONG_ID5, idString.toString());
		
		DBQueryBuilder.appendGroupBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE, DbHelper.SONG_PK));
		
		ArrayList<Song> songList = new ArrayList<Song>(); 
		
		Cursor cursorResult = db.rawQuery(strBuilder.toString(), null);  
		songList = parseSongInfoQueryResult_YouTube(cursorResult); 
	
		ArrayList<Song> resultList = new ArrayList<Song>(); 
		for(int i = 0; i < idList.length; i++) {
			for(int j = 0; j < songList.size(); j++) {
				Song song = songList.get(j); 
				if(song.getId() == idList[i] || song.getIndex5() == idList[i]) {
					resultList.add(song); 
					break;
				}
				
			}
		}
		
		cursorResult.close();
		
		return resultList; 
	}
	
	public ArrayList<Song> searchSongIDList(int[] idList, int[]typeABCList) {
		StringBuilder idString = new StringBuilder("(");
		for(int i = 0; i < idList.length; i++) {
			if(i < idList.length -1) {
				idString.append(idList[i] + ","); 
			}else {
				idString.append(idList[i]); 
			}
		}
		idString.append(")"); 
//		Log.e("", "ids: " + idString.toString()); 
		
		StringBuilder strBuilder = new StringBuilder(); 
		createSongInfoQueryString(strBuilder, ""); 
		
		if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
				|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
				 || MyApplication.intSvrModel == MyApplication.SONCA_KBX9 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
				 || MyApplication.intSvrModel == MyApplication.SONCA_TBT){
		if(curTocType != TOCTYPE_SK9000) {
				StringBuilder modelSb = new StringBuilder(); 
				modelSb.append('('); 
				modelSb.append(curTocType); 
				
				if(curHDDToc > 0) {
					modelSb.append("," + curHDDToc);
				}
				modelSb.append(')'); 
				DBQueryBuilder.appendWhereFromFormat(strBuilder, "%K IN %@", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_MODEL),
						modelSb.toString());
				
				DBQueryBuilder.appendFilterVol(strBuilder);
			}else {
				DBQueryBuilder.appendWhereFromFormat(strBuilder, false, "%K=%@", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_MODEL), curTocType);
			}
			
			DBQueryBuilder.appendFromFormat(strBuilder, " AND ((%K IN %@) OR (%K IN %@))", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_ID), idString.toString(), 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONG_ID5), idString.toString());
			
		} else {
			DBQueryBuilder.appendWhereFromFormat(strBuilder, false, "(%K IN %@) OR (%K IN %@)", DbHelper.SONG_ID, idString.toString(), DbHelper.SONG_ID5, idString.toString());	
		}
		
		DBQueryBuilder.appendGroupBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_PK));
//		Log.e("", "sql: " + strBuilder.toString()); 
		
		ArrayList<Song> songList = new ArrayList<Song>(); 
		Cursor cursorResult = db.rawQuery(strBuilder.toString(), null);  
		songList = parseSongInfoQueryResult(cursorResult); 

		ArrayList<Song> resultList = new ArrayList<Song>(); 
		for(int i = 0; i < idList.length; i++) {
			for(int j = 0; j < songList.size(); j++) {
				Song song = songList.get(j); 
				if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
						|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
						 || MyApplication.intSvrModel == MyApplication.SONCA_KBX9 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
						 || MyApplication.intSvrModel == MyApplication.SONCA_TBT){
					if((song.getId() == idList[i] || song.getIndex5() == idList[i])) {
						resultList.add(song); 
						break;
					}	
				} else {
					if((song.getId() == idList[i] || song.getIndex5() == idList[i]) && song.getTypeABC() == typeABCList[i]) {
						resultList.add(song); 
						// HMINH
						break;
					}
				}
			}
		}
		
		cursorResult.close();
		
		return resultList; 
	}
	
	public ArrayList<Song> searchSongID(String idStr, String typeABC) { 
		int id = 0; 
		try {
			id = Integer.parseInt(idStr); 
			if (id <= 0) 
			{
				return new ArrayList<Song>(); 
			}
		} catch (Exception e) {
//			Log.e("", "searchSongID" + idStr); 
			return new ArrayList<Song>(); 
		}
		
		StringBuilder strBuilder = new StringBuilder(); 
		
		createSongInfoQueryString(strBuilder, ""); 
		
		if(idStr != null && !idStr.equals(""))
		{
			if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
					|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
					 || MyApplication.intSvrModel == MyApplication.SONCA_KBX9 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
					 || MyApplication.intSvrModel == MyApplication.SONCA_TBT){
				if(curTocType != TOCTYPE_SK9000) {
					StringBuilder modelSb = new StringBuilder(); 
					modelSb.append('('); 
					modelSb.append(curTocType); 
					
					if(curHDDToc > 0) {
						modelSb.append("," + curHDDToc);
					}
					modelSb.append(')'); 
					DBQueryBuilder.appendWhereFromFormat(strBuilder, "%K IN %@", 
							DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_MODEL),
							modelSb.toString());
					
					DBQueryBuilder.appendFilterVol(strBuilder);
				}else {
					DBQueryBuilder.appendWhereFromFormat(strBuilder, false, "%K=%@", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_MODEL), curTocType);
				}
				
				DBQueryBuilder.appendFromFormat(strBuilder, " AND (%K=%@ OR %K=%@)", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_ID), id, 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_INDEX5), id); 
			}else {
				DBQueryBuilder.appendWhereFromFormat(strBuilder, "(%K=%@ OR %K=%@) AND (%K=%@)", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_ID), id, 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_ID5), id,
						DbHelper.SONG_ABCTYPE, typeABC); 
			} 
		}
		if(isHDDAvailable){
			String hddPath = mContext.getDatabasePath(DbHelper.HDD_DBName + DbHelper.DBExtension).getAbsolutePath(); 
			attachNewDatabase(hddPath, HDDDBName); 
			
			strBuilder.append(" UNION ALL "); 
			
			createSongInfoQueryString(strBuilder, HDDDBName); 
		
			if(idStr != null && !idStr.equals(""))
			{
				DBQueryBuilder.appendWhereFromFormat(strBuilder, "(%K=%@ OR %K=%@) AND (%K=%@)", DbHelper.SONG_ID, id, DbHelper.SONG_ID5, id, DbHelper.SONG_ABCTYPE, typeABC); 
			}
		}
		
		DBQueryBuilder.appendGroupBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SORT));
		DBQueryBuilder.appendLimitOffset(strBuilder, 1, 0); 
		Cursor cursorResult = db.rawQuery(strBuilder.toString(), null); 
		
		ArrayList<Song> songs = parseSongInfoQueryResult(cursorResult); 
		
		if(isHDDAvailable){
			detachDatabase(HDDDBName); 
		}
		
		cursorResult.close();
		
		return songs;
		
	}
	
	public ArrayList<Song> searchSongID_YouTube(String idStr, String typeABC) { 
		int id = 0; 
		try {
			id = Integer.parseInt(idStr); 
			if (id <= 0) 
			{
				return new ArrayList<Song>(); 
			}
		} catch (Exception e) {
//			Log.e("", "searchSongID" + idStr); 
			return new ArrayList<Song>(); 
		}
		
		StringBuilder strBuilder = new StringBuilder(); 
		
		createSongInfoQueryString_YouTube(strBuilder, "", -1);
		
		if(idStr != null && !idStr.equals(""))
		{
//			DBQueryBuilder.appendWhereFromFormat(strBuilder, "(%K=%@ OR %K=%@) AND (%K=%@)", 
//					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE, DbHelper.SONG_ID), id, 
//					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE, DbHelper.SONG_ID5), id,
//					DbHelper.SONG_ABCTYPE, typeABC); 
			
			DBQueryBuilder.appendWhereFromFormat(strBuilder, " (%K=%@ OR %K=%@) ", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE, DbHelper.SONG_ID), id, 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE, DbHelper.SONG_ID5), id); 
		}
		if(isHDDAvailable){
			String hddPath = mContext.getDatabasePath(DbHelper.HDD_DBName + DbHelper.DBExtension).getAbsolutePath(); 
			attachNewDatabase(hddPath, HDDDBName); 
			
			strBuilder.append(" UNION ALL "); 
			
			createSongInfoQueryString_YouTube(strBuilder, HDDDBName, -1); 
		
			if(idStr != null && !idStr.equals(""))
			{
				DBQueryBuilder.appendWhereFromFormat(strBuilder, "(%K=%@ OR %K=%@) AND (%K=%@)", DbHelper.SONG_ID, id, DbHelper.SONG_ID5, id, DbHelper.SONG_ABCTYPE, typeABC); 
			}
		}
		
		DBQueryBuilder.appendGroupBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE, DbHelper.SONG_SORT));
		DBQueryBuilder.appendLimitOffset(strBuilder, 1, 0); 
		Cursor cursorResult = db.rawQuery(strBuilder.toString(), null); 
		
		ArrayList<Song> songs = parseSongInfoQueryResult_YouTube(cursorResult); 
		
		if(isHDDAvailable){
			detachDatabase(HDDDBName); 
		}
		
		cursorResult.close();
		
		return songs;
		
	}
	
	public ArrayList<Song> searchUpdateSongSinger(String literal, SearchMode mode, int offset, int resultCount) {
		StringBuilder strBuilder = new StringBuilder(); 
		
		createSongInfoQueryString(strBuilder, DbHelper.UPDATE_DBName); 

		String colSearch = ""; 
		if(mode == SearchMode.MODE_FULL){
			colSearch = DbHelper.SONG_RAWNAME; 
		}else {
			colSearch = DbHelper.SONG_SHORTNAME; 
		}
		
		if(literal != null && !literal.equals(""))
		{
			DBQueryBuilder.appendWhereFromFormat(strBuilder, "(%K LIKE %@) AND (%K = %@)", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, colSearch), 
					literal + "%", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_MED_SINGER), 
					"1"
					); 
		}else {
			DBQueryBuilder.appendWhereFromFormat(strBuilder, "%K = %@", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_MED_SINGER), 
					"1"
					); 
		}
		DBQueryBuilder.appendGroupBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SORT));
		DBQueryBuilder.appendLimitOffset(strBuilder, resultCount, offset); 
//		Log.d("", strBuilder.toString()); 
		Cursor cursorResult = db.rawQuery(strBuilder.toString(), null); 

		return parseSongInfoQueryResult(cursorResult); 
	}
	
	public ArrayList<Song> searchSongSinger(String literal, SearchMode mode, int offset, int resultCount) {
		StringBuilder strBuilder = new StringBuilder(); 
		
		createSongInfoQueryString(strBuilder, ""); 

		String colSearch = ""; 
		if(mode == SearchMode.MODE_FULL){
			colSearch = DbHelper.SONG_RAWNAME; 
		}else {
			colSearch = DbHelper.SONG_SHORTNAME; 
		}
		
		if(literal != null && !literal.equals(""))
		{
			DBQueryBuilder.appendWhereFromFormat(strBuilder, "(%K LIKE %@) AND (%K = %@)", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, colSearch), 
					literal + "%", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_MED_SINGER), 
					"1"
					); 
		}else {
			DBQueryBuilder.appendWhereFromFormat(strBuilder, "%K = %@", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_MED_SINGER), 
					"1"
					); 
		}
		DBQueryBuilder.appendGroupBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SORT));
		DBQueryBuilder.appendLimitOffset(strBuilder, resultCount, offset); 
//		Log.d("", strBuilder.toString()); 
		Cursor cursorResult = db.rawQuery(strBuilder.toString(), null); 

		return parseSongInfoQueryResult(cursorResult); 
	}
	
	public ArrayList<Song> searchSongNewVol(String literal, SearchMode mode, int offset, int resultCount) {
		StringBuilder strBuilder = new StringBuilder(); 

		createSongInfoQueryString(strBuilder, ""); 

		String colSearch = ""; 
		if(mode == SearchMode.MODE_FULL){
			colSearch = DbHelper.SONG_RAWNAME; 
		}else {
			colSearch = DbHelper.SONG_SHORTNAME; 
		}

		if(literal != null && !literal.equals(""))
		{
			DBQueryBuilder.appendWhereFromFormat(strBuilder, "(%K LIKE %@) AND (%K = %@)", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, colSearch), 
					literal + "%", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_NEWVOL), 
					"1"
					); 
		}else {
			DBQueryBuilder.appendWhereFromFormat(strBuilder, "%K = %@", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_NEWVOL), 
					"1"
					); 
		}
		DBQueryBuilder.appendGroupBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SORT));
		DBQueryBuilder.appendLimitOffset(strBuilder, resultCount, offset); 
//		Log.d("", strBuilder.toString()); 
		Cursor cursorResult = db.rawQuery(strBuilder.toString(), null); 

		return parseSongInfoQueryResult(cursorResult); 
	}
	
	public ArrayList<Song> searchSongHDD(String literal, SearchMode mode, int offset, int resultCount) {
		if(!isHDDAvailable) return new ArrayList<Song>(); 
		
		String hddPath = mContext.getDatabasePath(DbHelper.HDD_DBName + DbHelper.DBExtension).getAbsolutePath(); 
		attachNewDatabase(hddPath, HDDDBName); 
		
		StringBuilder strBuilder = new StringBuilder(); 
		
		createSongInfoQueryString(strBuilder, HDDDBName); 
		
		String colSearch = ""; 
		if(mode == SearchMode.MODE_FULL){
			colSearch = DbHelper.SONG_RAWNAME; 
		}else {
			colSearch = DbHelper.SONG_SHORTNAME; 
		}

		if(literal != null && !literal.equals(""))
		{
			DBQueryBuilder.appendWhereFromFormat(strBuilder, "(%K LIKE %@)", // AND (%K = %@)
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, colSearch), 
					literal + "%" //, 
//					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_MIDI), 
//					String.valueOf(MEDIA_TYPE.MKV.ordinal())
					); 
		}
		DBQueryBuilder.appendGroupBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SORT));
		DBQueryBuilder.appendLimitOffset(strBuilder, resultCount, offset); 
		
		Cursor cursorResult = db.rawQuery(strBuilder.toString(), null); 

		ArrayList<Song> songs = parseSongInfoQueryResult(cursorResult); 

		detachDatabase(HDDDBName);
		
		cursorResult.close();
		
		return songs;
	}
	
//---------------------- KHIEM -----------------------------//
	
	public Cursor getCursorSong(String literal, LANG_INDEX langID, SearchMode mode, int offset, int resultCount){
		StringBuilder strBuilder = new StringBuilder(); 
		createSongInfoQueryString(strBuilder, ""); 
		String colSearch = ""; 
		if(mode == SearchMode.MODE_FULL){
			colSearch = DbHelper.SONG_RAWNAME; 
		}else {
			colSearch = DbHelper.SONG_SHORTNAME; 
		}
		if (langID == LANG_INDEX.ALL_LANGUAGE) {
			if(literal != null && !literal.equals(""))
			{
				DBQueryBuilder.appendWhereFromFormat(strBuilder, "(%K LIKE %@) AND (%K=%@)", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, colSearch), 
						literal + "%", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHOWENABLE),
						"0"); 
			}else {
				DBQueryBuilder.appendWhereFromFormat(strBuilder, "(%K=%@)", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHOWENABLE),
						"0"); 
			}
		}else {
			if(literal != null && !literal.equals(""))
			{
				DBQueryBuilder.appendWhereFromFormat(strBuilder, "(%K LIKE %@) AND (%K = %@)", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, colSearch), 
						literal + "%", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_LANGUAGE_ID), 
						langID.ordinal()
						); 
			}else {
				DBQueryBuilder.appendWhereFromFormat(strBuilder, "%K = %@", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_LANGUAGE_ID), 
						langID.ordinal()); 
			}
		}
		DBQueryBuilder.appendGroupBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SORT));
		DBQueryBuilder.appendLimitOffset(strBuilder, resultCount, offset); 
		Cursor cursorResult = db.rawQuery(strBuilder.toString(), null); 
		return cursorResult;
	}

//----------------------------------------------------------//
	
	public ArrayList<Song> searchSong(String literal, LANG_INDEX langID, SearchMode mode, int offset, int resultCount)
	{
		StringBuilder strBuilder = new StringBuilder(); 
		
		createSongInfoQueryString(strBuilder, ""); 
		
		String colSearch = ""; 
		if(mode == SearchMode.MODE_FULL){
			colSearch = DbHelper.SONG_RAWNAME; 
		}else {
			colSearch = DbHelper.SONG_SHORTNAME; 
		}
		if (langID == LANG_INDEX.ALL_LANGUAGE) {
			if(literal != null && !literal.equals(""))
			{
				DBQueryBuilder.appendWhereFromFormat(strBuilder, "(%K LIKE %@) AND (%K=%@)", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, colSearch), 
						literal + "%", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHOWENABLE),
						"0"); 
			}else {
				DBQueryBuilder.appendWhereFromFormat(strBuilder, "(%K=%@)", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHOWENABLE),
						"0"); 
			}
		}else {
			if(literal != null && !literal.equals(""))
			{
				DBQueryBuilder.appendWhereFromFormat(strBuilder, "(%K LIKE %@) AND (%K = %@)", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, colSearch), 
						literal + "%", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_LANGUAGE_ID), 
						langID.ordinal()
						); 
			}else {
				DBQueryBuilder.appendWhereFromFormat(strBuilder, "%K = %@", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_LANGUAGE_ID), 
						langID.ordinal()); 
			}
		}
		DBQueryBuilder.appendGroupBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SORT));
		DBQueryBuilder.appendLimitOffset(strBuilder, resultCount, offset); 
		Cursor cursorResult = db.rawQuery(strBuilder.toString(), null); 
		return parseSongInfoQueryResult(cursorResult); 
	}
	
	public ArrayList<Song> searchSongWithTypeID(SearchType type, String idStr, int offset, int resultCount) {
		
		int id = 0; 
		try {
			id = Integer.parseInt(idStr); 
			if (id <= 0){
				return new ArrayList<Song>(); 
			}
		} catch (Exception e) {
//			Log.e("", "searchSongWithTypeID: " + idStr); 
			return new ArrayList<Song>(); 
		}
		
		String columnType = ""; 
		switch (type) {
		case SEARCH_SONG:
			columnType = DbHelper.SONG_ID; 
			break;
		
		case SEARCH_MUSICIAN: 
			columnType = DbHelper.SONG_MUSICIAN_ID; 
			break; 
			
		case SEARCH_SINGER: 
			columnType = DbHelper.SONG_SINGER_ID; 
			break; 
			
		case SEARCH_TYPE: 
			columnType = DbHelper.SONG_TYPE_ID; 
			break; 
			
		default:
			break;
		}
		
		StringBuilder strBuilder = new StringBuilder(); 
		
		createSongInfoQueryString(strBuilder, ""); 
		
		if(idStr != null && !idStr.equals(""))
		{
			DBQueryBuilder.appendWhereFromFormat(strBuilder, "%K=%@", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, columnType)
				,id); 
		}

		DBQueryBuilder.appendGroupBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SORT));
		DBQueryBuilder.appendLimitOffset(strBuilder, resultCount, offset); 
		Cursor cursorResult = db.rawQuery(strBuilder.toString(), null); 
		
		return parseSongInfoQueryResult(cursorResult); 
	}

	public ArrayList<Song> searchSongWithTypeIDHDD(SearchType type, String idStr, int offset, int resultCount) {

		if(!isHDDAvailable) return new ArrayList<Song>(); 

		int id = 0; 
		try {
			id = Integer.parseInt(idStr); 
			if (id <= 0) 
			{
				return new ArrayList<Song>(); 
			}
		} catch (Exception e) {
//			Log.e("", "searchSongWithTypeID: " + idStr); 
			return new ArrayList<Song>(); 
		}

		String columnType = ""; 
		switch (type) {
		case SEARCH_SONG:
			columnType = DbHelper.SONG_ID; 
			break;

		case SEARCH_MUSICIAN: 
			columnType = DbHelper.SONG_MUSICIAN_ID; 
			break; 

		case SEARCH_SINGER: 
			columnType = DbHelper.SONG_SINGER_ID; 
			break; 

		case SEARCH_TYPE: 
			columnType = DbHelper.SONG_TYPE_ID; 
			break; 

		default:
			break;
		}

		String hddPath = mContext.getDatabasePath(DbHelper.HDD_DBName + DbHelper.DBExtension).getAbsolutePath(); 
		attachNewDatabase(hddPath, HDDDBName); 

		StringBuilder strBuilder = new StringBuilder(); 

		createSongInfoQueryString(strBuilder, HDDDBName); 

		if(idStr != null && !idStr.equals(""))
		{
			DBQueryBuilder.appendWhereFromFormat(strBuilder, "%K=%@", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, columnType)
					,id); 
		}
		DBQueryBuilder.appendGroupBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SORT));
		DBQueryBuilder.appendLimitOffset(strBuilder, resultCount, offset); 
		Cursor cursorResult = db.rawQuery(strBuilder.toString(), null); 

		ArrayList<Song> songs = parseSongInfoQueryResult(cursorResult); 

		detachDatabase(DbHelper.HDD_DBName); 

		cursorResult.close();
		
		return songs;
	}
	
	public ArrayList<Song> searchSongWithTypeIDSongSinger(SearchType type, String idStr, int offset, int resultCount) {

		int id = 0; 
		try {
			id = Integer.parseInt(idStr); 
			if (id <= 0) 
			{
				return new ArrayList<Song>(); 
			}
		} catch (Exception e) {
//			Log.e("", "searchSongWithTypeID: " + idStr); 
			return new ArrayList<Song>(); 
		}

		String columnType = ""; 
		switch (type) {
		case SEARCH_SONG:
			columnType = DbHelper.SONG_ID; 
			break;

		case SEARCH_MUSICIAN: 
			columnType = DbHelper.SONG_MUSICIAN_ID; 
			break; 

		case SEARCH_SINGER: 
			columnType = DbHelper.SONG_SINGER_ID; 
			break; 

		case SEARCH_TYPE: 
			columnType = DbHelper.SONG_TYPE_ID; 
			break; 

		default:
			break;
		}

		StringBuilder strBuilder = new StringBuilder(); 

		createSongInfoQueryString(strBuilder, ""); 

		if(idStr != null && !idStr.equals(""))
		{
			DBQueryBuilder.appendWhereFromFormat(strBuilder, "(%K=%@)AND(%K=%@)", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, columnType), id,  
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_MED_SINGER), 1); 
		}

		DBQueryBuilder.appendGroupBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SORT));
		DBQueryBuilder.appendLimitOffset(strBuilder, resultCount, offset); 
		Cursor cursorResult = db.rawQuery(strBuilder.toString(), null); 

		return parseSongInfoQueryResult(cursorResult); 
	}

	public ArrayList<Song> searchSongWithTypeIDNewVol(SearchType type, String idStr, int offset, int resultCount) {

		int id = 0; 
		try {
			id = Integer.parseInt(idStr); 
			if (id <= 0) 
			{
				return new ArrayList<Song>(); 
			}
		} catch (Exception e) {
//			Log.e("", "searchSongWithTypeID: " + idStr); 
			return new ArrayList<Song>(); 
		}

		String columnType = ""; 
		switch (type) {
		case SEARCH_SONG:
			columnType = DbHelper.SONG_ID; 
			break;

		case SEARCH_MUSICIAN: 
			columnType = DbHelper.SONG_MUSICIAN_ID; 
			break; 

		case SEARCH_SINGER: 
			columnType = DbHelper.SONG_SINGER_ID; 
			break; 

		case SEARCH_TYPE: 
			columnType = DbHelper.SONG_TYPE_ID; 
			break; 

		default:
			break;
		}

		StringBuilder strBuilder = new StringBuilder(); 

		createSongInfoQueryString(strBuilder, ""); 

		if(idStr != null && !idStr.equals(""))
		{
			DBQueryBuilder.appendWhereFromFormat(strBuilder, "(%K=%@)AND(%K=%@)", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, columnType), id,  
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_NEWVOL), 1); 
		}

		DBQueryBuilder.appendGroupBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SORT));
		DBQueryBuilder.appendLimitOffset(strBuilder, resultCount, offset); 
		Cursor cursorResult = db.rawQuery(strBuilder.toString(), null); 

		return parseSongInfoQueryResult(cursorResult); 
	}
	
	public ArrayList<Song> getMostPlaySongList(LANG_INDEX langID, int offset, int resultCount) {
		StringBuilder strBuilder = new StringBuilder(); 
		
		createSongInfoQueryString(strBuilder, ""); 
		
		
		DBQueryBuilder.appendWhereFromFormat(strBuilder, "%K = %@", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_LANGUAGE_ID), 
				langID.ordinal()
				); 
		DBQueryBuilder.appendGroupBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SORT));
		DBQueryBuilder.appendLimitOffset(strBuilder, resultCount, offset); 
		Cursor cursorResult = db.rawQuery(strBuilder.toString(), null); 
		
		return parseSongInfoQueryResult(cursorResult); 
	}
	
	public ArrayList<Singer> getMostPlaySingerList(LANG_INDEX langID, int offset, int resultCount) {
		StringBuilder strBuilder = new StringBuilder(); 
		
		createSingerInfoQueryString(strBuilder, "", false); 
		
		DBQueryBuilder.appendWhereFromFormat(strBuilder, "%K = %@", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SINGER
						, DbHelper.SINGER_LANG_ID), 
				langID.ordinal()
				); 
//		DBQueryBuilder.appendOrderBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SINGER, DbHelper.SINGER_PLAYCNT), true); 
		DBQueryBuilder.appendLimitOffset(strBuilder, resultCount, offset); 
		Cursor cursorResult = db.rawQuery(strBuilder.toString(), null); 
		
		return parseSingerInfoQueryResult(cursorResult); 
	}
	
	public ArrayList<Musician> getMostPlayMusicianList(LANG_INDEX langID, int offset, int resultCount) {
		StringBuilder strBuilder = new StringBuilder(); 
		
		createMusicianInfoQueryString(strBuilder, "", false); 
		
		DBQueryBuilder.appendWhereFromFormat(strBuilder, "%K = %@", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_MUSICIAN, DbHelper.MUSICIAN_LANG_ID), 
				langID.ordinal()
				); 
//		DBQueryBuilder.appendOrderBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_MUSICIAN, DbHelper.MUSICIAN_PLAYCNT), true); 
		DBQueryBuilder.appendLimitOffset(strBuilder, resultCount, offset); 
		Cursor cursorResult = db.rawQuery(strBuilder.toString(), null); 
		
		return parseMuscianInfoQueryResult(cursorResult); 
	}
	
	public ArrayList<SongType> getMostPlaySongTypeList(LANG_INDEX langID, int offset, int resultCount) {
		StringBuilder strBuilder = new StringBuilder(); 
		
		createSongTypeInfoQueryString(strBuilder, "", false); 
		
		
		DBQueryBuilder.appendWhereFromFormat(strBuilder, "%K = %@", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGTYPE, DbHelper.TYPE_LANG_ID), 
				langID.ordinal()
				); 
//		DBQueryBuilder.appendOrderBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGTYPE, DbHelper.TYPE_PLAYCNT), true); 
		DBQueryBuilder.appendLimitOffset(strBuilder, resultCount, offset); 
		Cursor cursorResult = db.rawQuery(strBuilder.toString(), null); 
		
		return parseSongTypeInfoQueryResult(cursorResult); 
	}
	
	public ArrayList<Song> getFreeSongList(int offset, int resultCount) {
		StringBuilder strBuilder = new StringBuilder(); 
		
		createSongInfoQueryString(strBuilder, ""); 
		
		DBQueryBuilder.appendWhereFromFormat(strBuilder, "%K=%@", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_FREE)
				,"1"); 

		DBQueryBuilder.appendGroupBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SORT));
		DBQueryBuilder.appendLimitOffset(strBuilder, resultCount, offset); 
		Cursor cursorResult = db.rawQuery(strBuilder.toString(), null); 
		
		return parseSongInfoQueryResult(cursorResult); 
	}
	
	public ArrayList<Song> getFavouriteSongList(int offset, int resultCount) {
		StringBuilder strBuilder = new StringBuilder(); 
		
		createSongInfoQueryString(strBuilder, ""); 
		
		DBQueryBuilder.appendWhereFromFormat(strBuilder, "%K=%@", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_FAVOUR)
				,"1"); 
		
		filterWithHiddenSK90xx(strBuilder, DbHelper.TABLE_SONGS);
		
		if(isHDDAvailable) {
			String hddPath = mContext.getDatabasePath(DbHelper.HDD_DBName + DbHelper.DBExtension).getAbsolutePath(); 
			attachNewDatabase(hddPath, HDDDBName); 
			
			createSongInfoQueryString(strBuilder, HDDDBName); 
			
			DBQueryBuilder.appendWhereFromFormat(strBuilder, "%K=%@", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_FAVOUR)
					,"1"); 
		}
		DBQueryBuilder.appendRefreshDevice(strBuilder, MyApplication.bOffUserList);
		DBQueryBuilder.appendKM1SelectList(strBuilder, MyApplication.intSelectList);
		DBQueryBuilder.appendGroupBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SORT));
		DBQueryBuilder.appendLimitOffset(strBuilder, resultCount, offset); 
		// MyLog.e("database", "favourity");
		// MyLog.d("database", strBuilder.toString());
		Cursor cursorResult = db.rawQuery(strBuilder.toString(), null); 
		
		ArrayList<Song> songs = parseSongInfoQueryResult(cursorResult); 
		if(isHDDAvailable)
			detachDatabase(HDDDBName);
		
		cursorResult.close();
		
		return songs;
	}
/*	
	public UpdateInfo getDatabaseUpdateInfo(String dbName) {
		if(dbName == null || dbName.equals("")) {
			dbName = ""; 
		}
		StringBuilder strBuilder = new StringBuilder(); 
		createUpdateInfoQueryString(strBuilder, dbName); 
		DBQueryBuilder.appendLimitOffset(strBuilder, 1, 0); 
		Cursor resultCursor = db.rawQuery(strBuilder.toString(), null); 
		
		ArrayList<UpdateInfo> infos = parseUpdateInfoQueryResult(resultCursor); 
		if(infos.size() > 0) {
			return infos.get(0); 
		}
		return new UpdateInfo(); 
		
	}
*/	
	public ArrayList<Singer> searchSinger(String literal, LANG_INDEX langID, SearchMode mode, int offset, int resultCount)
	{
		String colSearch = ""; 
		if(mode == SearchMode.MODE_FULL){
			colSearch = DbHelper.SINGER_RAWNAME; 
		}else if (mode == SearchMode.MODE_SIGNED) {
			colSearch = DbHelper.SINGER_NAME; 
		}else {
			colSearch = DbHelper.SINGER_SHORTNAME; 
		}
		
		StringBuilder strBuilder = new StringBuilder(); 
		createSingerInfoQueryString(strBuilder, "", false, SearchMode.MODE_PINYIN.ordinal()); 
		
		DBQueryBuilder.appendWhereFromFormat(strBuilder, "(%K!=%@)", DbHelper.SINGER_NAME, "-"); 
		
		if (literal != null && !literal.equals("")) {
			DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K LIKE %@)", 
					DbHelper.SINGER_SHORTNAME, literal + "%");
		}
		
		if (langID == LANG_INDEX.ALL_LANGUAGE) {
			
		}else if(langID == LANG_INDEX.ALL_EXCEPT_VIETNAMESE) {
			DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K!=%@)", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SINGER, DbHelper.SINGER_LANG_ID),
					LANG_INDEX.VIETNAMESE.ordinal());
		}else {
			DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K=%@)", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SINGER, DbHelper.SINGER_LANG_ID), 
					langID.ordinal());
		}
		
		StringBuilder sqlFull = new StringBuilder(); 
		if(mode == SearchMode.MODE_MIXED && literal != null && !literal.equals(""))
		{
			createSingerInfoQueryString(sqlFull, "", false, SearchMode.MODE_FULL.ordinal()); 
			
			DBQueryBuilder.appendWhereFromFormat(sqlFull, "(%K!=%@)", DbHelper.SINGER_NAME, "-"); 
			
			if (literal != null && !literal.equals("")) {
				DBQueryBuilder.appendFromFormat(sqlFull, "AND (%K NOT LIKE %@ AND %K LIKE %@)",
						DbHelper.SINGER_SHORTNAME, literal + "%", 
						DbHelper.SINGER_RAWNAME, "%" + literal + "%");
			}
			
			if (langID == LANG_INDEX.ALL_LANGUAGE) {
				
			}else if(langID == LANG_INDEX.ALL_EXCEPT_VIETNAMESE) {
				DBQueryBuilder.appendFromFormat(sqlFull, "AND (%K!=%@)", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SINGER, DbHelper.SINGER_LANG_ID),
						LANG_INDEX.VIETNAMESE.ordinal());
			}else {
				DBQueryBuilder.appendFromFormat(sqlFull, "AND (%K=%@)", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SINGER, DbHelper.SINGER_LANG_ID), 
						langID.ordinal());
			}
			
			String str = strBuilder.toString(); 
			strBuilder = new StringBuilder("Select * from (" + str + ") "); 
			strBuilder.append("UNION ALL Select * from (" + sqlFull.toString() + ") "); 
		}
		
//		DBQueryBuilder.appendOrderBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SINGER, DbHelper.SINGER_LANG_ID));
		DBQueryBuilder.appendLimitOffset(strBuilder, resultCount, offset); 
		// KHIEM
		Cursor resultCursor = db.rawQuery(strBuilder.toString(), null); 		
		return parseSingerInfoQueryResult(resultCursor); 
	}

	public ArrayList<Singer> searchSingerHDD(String literal, LANG_INDEX langID, SearchMode mode, int offset, int resultCount)
	{
		if(!isHDDAvailable) return new ArrayList<Singer>(); 
		
		String hddPath = mContext.getDatabasePath(DbHelper.HDD_DBName + DbHelper.DBExtension).getAbsolutePath(); 
		attachNewDatabase(hddPath, HDDDBName); 
		
		StringBuilder strBuilder = new StringBuilder(); 
		createSingerInfoQueryString(strBuilder, HDDDBName, false); 
		
		String colSearch = ""; 
		if(mode == SearchMode.MODE_FULL){
			colSearch = DbHelper.SINGER_RAWNAME; 
		}else if (mode == SearchMode.MODE_SIGNED) {
			colSearch = DbHelper.SINGER_NAME; 
		}else {
			colSearch = DbHelper.SINGER_SHORTNAME; 
		}
		
		if(literal != null && !literal.equals(""))
		{
			DBQueryBuilder.appendWhereFromFormat(strBuilder, "(%K LIKE %@)", colSearch, literal + "%"); // AND (%K = %@), DbHelper.SINGER_LANG_ID, langID
		}
//		else {
//			DBQueryBuilder.appendWhereFromFormat(strBuilder, "%K = %@", DbHelper.SINGER_LANG_ID, langID); 
//		}

//		DBQueryBuilder.appendOrderBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SINGER, DbHelper.SINGER_LANG_ID));
		DBQueryBuilder.appendLimitOffset(strBuilder, resultCount, offset); 
		Cursor resultCursor = db.rawQuery(strBuilder.toString(), null); 
		
		ArrayList<Singer> singers = parseSingerInfoQueryResult(resultCursor); 
		detachDatabase(HDDDBName); 
		
		resultCursor.close();
		
		return singers; 
	}
	
	public ArrayList<Singer> searchSingerSongSinger(String literal, LANG_INDEX langID, SearchMode mode, int offset, int resultCount)
	{
		StringBuilder strBuilder = new StringBuilder(); 
		createSingerInfoQueryString(strBuilder, "", true); 
		
		String colSearch = ""; 
		if(mode == SearchMode.MODE_FULL){
			colSearch = DbHelper.SINGER_RAWNAME; 
		}else if (mode == SearchMode.MODE_SIGNED) {
			colSearch = DbHelper.SINGER_NAME; 
		}else {
			colSearch = DbHelper.SINGER_SHORTNAME; 
		}
		
		if(literal != null && !literal.equals(""))
		{
			DBQueryBuilder.appendWhereFromFormat(strBuilder, "(%K=%@)AND(%K LIKE %@)", DbHelper.SONG_MED_SINGER, "1", DBQueryBuilder.getColumnString(DbHelper.TABLE_SINGER, colSearch), literal + "%" ); 
		}else {
			DBQueryBuilder.appendWhereFromFormat(strBuilder, "(%K=%@)",DbHelper.SONG_MED_SINGER, "1"); 
		}
		DBQueryBuilder.appendGroupBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SINGER, DbHelper.SINGER_ID)); 
//		DBQueryBuilder.appendOrderBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SINGER, DbHelper.SINGER_LANG_ID));
		Cursor resultCursor = db.rawQuery(strBuilder.toString(), null); 
		
		return parseSingerInfoQueryResult(resultCursor); 
	}
	
	public ArrayList<Singer> searchSingerNewVol(String literal, LANG_INDEX langID, SearchMode mode, int offset, int resultCount)
	{
		StringBuilder strBuilder = new StringBuilder(); 
		createSingerInfoQueryString(strBuilder, "", true); 
		
		String colSearch = ""; 
		if(mode == SearchMode.MODE_FULL){
			colSearch = DbHelper.SINGER_RAWNAME; 
		}else if (mode == SearchMode.MODE_SIGNED) {
			colSearch = DbHelper.SINGER_NAME; 
		}else {
			colSearch = DbHelper.SINGER_SHORTNAME; 
		}
		
		if(literal != null && !literal.equals(""))
		{
			DBQueryBuilder.appendWhereFromFormat(strBuilder, "(%K=%@)AND(%K LIKE %@)", DbHelper.SONG_NEWVOL, "1", DBQueryBuilder.getColumnString(DbHelper.TABLE_SINGER, colSearch), literal + "%" ); 
		}else {
			DBQueryBuilder.appendWhereFromFormat(strBuilder, "(%K=%@)",DbHelper.SONG_NEWVOL, "1"); 
		}
		DBQueryBuilder.appendGroupBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SINGER, DbHelper.SINGER_ID)); 
//		DBQueryBuilder.appendOrderBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SINGER, DbHelper.SINGER_LANG_ID));
//		Log.e("", strBuilder.toString()); 
		Cursor resultCursor = db.rawQuery(strBuilder.toString(), null); 
		
		return parseSingerInfoQueryResult(resultCursor); 
	}
	
	public ArrayList<Musician> searchMusician(String literal, LANG_INDEX langID, SearchMode mode, int offset, int resultCount) {
		String colSearch = ""; 
		if(mode == SearchMode.MODE_FULL){
			colSearch = DbHelper.MUSICIAN_RAWNAME; 
		}else if (mode == SearchMode.MODE_SIGNED) {
			colSearch = DbHelper.MUSICIAN_NAME; 
		}else {
			colSearch = DbHelper.MUSICIAN_SHORTNAME; 
		}
		
		StringBuilder strBuilder = new StringBuilder(); 
		createMusicianInfoQueryString(strBuilder, "", false, SearchMode.MODE_PINYIN.ordinal()); 
		
		DBQueryBuilder.appendWhereFromFormat(strBuilder, "(%K!=%@)", DbHelper.MUSICIAN_NAME, "-"); 
		
		if (literal != null && !literal.equals("")) {
			DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K LIKE %@)", 
					DbHelper.MUSICIAN_SHORTNAME, literal + "%");
		}
		
		if (langID == LANG_INDEX.ALL_LANGUAGE) {
			
		}else if(langID == LANG_INDEX.ALL_EXCEPT_VIETNAMESE) {
			DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K!=%@)", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_MUSICIAN, DbHelper.MUSICIAN_LANG_ID),
					LANG_INDEX.VIETNAMESE.ordinal());
		}else {
			DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K=%@)", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_MUSICIAN, DbHelper.MUSICIAN_LANG_ID), 
					langID.ordinal());
		}
		
		StringBuilder sqlFull = new StringBuilder(); 
		if(mode == SearchMode.MODE_MIXED && literal != null && !literal.equals(""))
		{
			createMusicianInfoQueryString(sqlFull, "", false, SearchMode.MODE_FULL.ordinal()); 
			
			DBQueryBuilder.appendWhereFromFormat(sqlFull, "(%K!=%@)", DbHelper.MUSICIAN_NAME, "-"); 
			
			if (literal != null && !literal.equals("")) {
				DBQueryBuilder.appendFromFormat(sqlFull, "AND (%K NOT LIKE %@ AND %K LIKE %@)", 
						DbHelper.MUSICIAN_SHORTNAME, literal + "%",  
						DbHelper.MUSICIAN_RAWNAME, "%" + literal + "%");
			}
			
			if (langID == LANG_INDEX.ALL_LANGUAGE) {
				
			}else if(langID == LANG_INDEX.ALL_EXCEPT_VIETNAMESE) {
				DBQueryBuilder.appendFromFormat(sqlFull, "AND (%K!=%@)", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_MUSICIAN, DbHelper.MUSICIAN_LANG_ID),
						LANG_INDEX.VIETNAMESE.ordinal());
			}else {
				DBQueryBuilder.appendFromFormat(sqlFull, "AND (%K=%@)", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_MUSICIAN, DbHelper.MUSICIAN_LANG_ID), 
						langID.ordinal());
			}
			
			String str = strBuilder.toString(); 
			strBuilder = new StringBuilder("Select * from (" + str + ") "); 
			strBuilder.append("UNION ALL Select * from (" + sqlFull.toString() + ") "); 
		}
		
//		DBQueryBuilder.appendOrderBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_MUSICIAN, DbHelper.MUSICIAN_LANG_ID));
		DBQueryBuilder.appendLimitOffset(strBuilder, resultCount, offset); 

		// KHIEM
//		MyLog.e("      ", "          ");
//		long tgian = System.currentTimeMillis();
		Cursor resultCursor = db.rawQuery(strBuilder.toString(), null); 
//		MyLog.e("Query", " : " + (System.currentTimeMillis() - tgian));
		return parseMuscianInfoQueryResult(resultCursor); 
	}
	
	public ArrayList<Song> searchSongWithLyricMode(String literal, LANG_INDEX langID, SearchMode mode, int offset, int resultCount) {
		/*
		String sql = ""; 
		literal = literal.toLowerCase(); 
		if(literal == null || literal.length() == 0){
	        sql = "select zid,zpinyin,ztext from zvlyrics";
	    }else {
//	        searchItem = [NSString stringWithFormat:@"%%%@%%", searchItem];
	        sql = "select zid,zpinyin,ztext from zvlyrics where ztext match '\"" + literal + "\"'";
	    }
		
		Log.e("", "Sqlite:==>" + sql); 
		
		Cursor resultCursor = db.rawQuery(sql, null); 
		
		ArrayList<Integer> idList = new ArrayList<Integer>(); 	
		if(resultCursor.getCount() > 0) {
			while(resultCursor.moveToNext())
			{
				idList.add(resultCursor.getInt(0)); 
			}
		}
		

		Log.e("", "FTS Result: " + idList.size()); 
		
		ArrayList<Song> resultList = new ArrayList<Song>(); 
		for(int i = 0; i < idList.size(); i++)
		{
			ArrayList<Song> aSongs = searchSongID(String.valueOf(idList.get(i))); 
			if (aSongs.size() > 0) {
				resultList.add(aSongs.get(0)); 
			}
		}
		
		return resultList; 
		*/
		StringBuilder strBuilder = new StringBuilder(); 
		createSongInfoQueryString(strBuilder, ""); 
		DBQueryBuilder.appendInnerJoin(strBuilder, "main" + "].[" + DbHelper.TABLE_SONGLYRICS, 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_ID5)
				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGLYRICS, DbHelper.LYRICS_ID));
		
		String colSearch = ""; 
		if(mode == SearchMode.MODE_FULL){
			colSearch = DbHelper.LYRICS_TEXT; 
		}else if (mode == SearchMode.MODE_SIGNED) {
			colSearch = DbHelper.LYRICS_TEXT; 
		}else {
			colSearch = DbHelper.LYRICS_PINYIN; 
		}
		if (langID == LANG_INDEX.ALL_LANGUAGE) {
			if(literal != null && !literal.equals(""))
			{
				DBQueryBuilder.appendWhereFromFormat(strBuilder, "%K match %@", colSearch, "\"" + literal + "\""); 
			}
		}else {
			if(literal != null && !literal.equals(""))
			{
				DBQueryBuilder.appendWhereFromFormat(strBuilder, "(%K match \"%@\") AND (%K = %@)", colSearch, "\"" + literal + "\"", DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_LANGUAGE_ID), langID.ordinal()); 
			}else {
				DBQueryBuilder.appendWhereFromFormat(strBuilder, "%K = %@", DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_LANGUAGE_ID), langID.ordinal()); 
			}
		}
		DBQueryBuilder.appendGroupBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SORT));
		DBQueryBuilder.appendLimitOffset(strBuilder, resultCount, offset); 
//		Log.e("",strBuilder.toString()); 
		Cursor resultCursor = db.rawQuery(strBuilder.toString(), null); 
		
		return parseSongInfoQueryResult(resultCursor); 
		
	}
	
	public ArrayList<Musician> searchMusicianHDD(String literal, LANG_INDEX langID, SearchMode mode, int offset, int resultCount) {
		if(!isHDDAvailable) return new ArrayList<Musician>(); 
		
		String hddPath = mContext.getDatabasePath(DbHelper.HDD_DBName + DbHelper.DBExtension).getAbsolutePath(); 
		attachNewDatabase(hddPath, HDDDBName); 
		StringBuilder strBuilder = new StringBuilder(); 
		createMusicianInfoQueryString(strBuilder, HDDDBName, false); 
		
		String colSearch = ""; 
		if(mode == SearchMode.MODE_FULL){
			colSearch = DbHelper.MUSICIAN_RAWNAME; 
		}else if (mode == SearchMode.MODE_SIGNED) {
			colSearch = DbHelper.MUSICIAN_NAME; 
		}else {
			colSearch = DbHelper.MUSICIAN_SHORTNAME; 
		}
		
		if(literal != null && !literal.equals(""))
		{
			DBQueryBuilder.appendWhereFromFormat(strBuilder, "(%K LIKE %@)", colSearch, literal + "%"); //  AND (%K = %@), , DbHelper.MUSICIAN_LANG_ID, langID
		}
//		else {
//			DBQueryBuilder.appendWhereFromFormat(strBuilder, "%K = %@", DbHelper.MUSICIAN_LANG_ID, langID); 
//		}
//		DBQueryBuilder.appendOrderBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_MUSICIAN, DbHelper.MUSICIAN_LANG_ID));
		DBQueryBuilder.appendLimitOffset(strBuilder, resultCount, offset); 
		Cursor resultCursor = db.rawQuery(strBuilder.toString(), null); 
		
		ArrayList<Musician> musicians = parseMuscianInfoQueryResult(resultCursor); 
		detachDatabase(HDDDBName);
		
		resultCursor.close();
		
		return musicians; 
	}

	public ArrayList<Musician> searchMusicianSongSinger(String literal, LANG_INDEX langID, SearchMode mode, int offset, int resultCount) {
		StringBuilder strBuilder = new StringBuilder(); 
		createMusicianInfoQueryString(strBuilder, "", true); 
		
		String colSearch = ""; 
		if(mode == SearchMode.MODE_FULL){
			colSearch = DbHelper.MUSICIAN_RAWNAME; 
		}else if (mode == SearchMode.MODE_SIGNED) {
			colSearch = DbHelper.MUSICIAN_NAME; 
		}else {
			colSearch = DbHelper.MUSICIAN_SHORTNAME; 
		}
		
		if(literal != null && !literal.equals(""))
		{
			DBQueryBuilder.appendWhereFromFormat(strBuilder, "(%K=%@)AND(%K LIKE %@)", DbHelper.SONG_MED_SINGER, "1", DBQueryBuilder.getColumnString(DbHelper.TABLE_MUSICIAN, colSearch), literal + "%" ); 
		}else {
			DBQueryBuilder.appendWhereFromFormat(strBuilder, "(%K=%@)",DbHelper.SONG_MED_SINGER, "1"); 
		}
		DBQueryBuilder.appendGroupBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_MUSICIAN, DbHelper.MUSICIAN_ID)); 
//		DBQueryBuilder.appendOrderBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_MUSICIAN, DbHelper.MUSICIAN_LANG_ID));
		DBQueryBuilder.appendLimitOffset(strBuilder, resultCount, offset); 
		Cursor resultCursor = db.rawQuery(strBuilder.toString(), null); 
		
		return parseMuscianInfoQueryResult(resultCursor); 
	}
	
	public ArrayList<Musician> searchMusicianNewVol(String literal, LANG_INDEX langID, SearchMode mode, int offset, int resultCount) {
		StringBuilder strBuilder = new StringBuilder(); 
		createMusicianInfoQueryString(strBuilder, "", true); 
		
		String colSearch = ""; 
		if(mode == SearchMode.MODE_FULL){
			colSearch = DbHelper.MUSICIAN_RAWNAME; 
		}else if (mode == SearchMode.MODE_SIGNED) {
			colSearch = DbHelper.MUSICIAN_NAME; 
		}else {
			colSearch = DbHelper.MUSICIAN_SHORTNAME; 
		}
		
		if(literal != null && !literal.equals(""))
		{
			DBQueryBuilder.appendWhereFromFormat(strBuilder, "(%K = %@)AND(%K LIKE %@)", DbHelper.SONG_NEWVOL, "1", DBQueryBuilder.getColumnString(DbHelper.TABLE_MUSICIAN, colSearch), literal + "%" ); 
		}else {
			DBQueryBuilder.appendWhereFromFormat(strBuilder, "%K=%@", DbHelper.SONG_NEWVOL, "1"); 
		}
		DBQueryBuilder.appendGroupBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_MUSICIAN, DbHelper.MUSICIAN_ID)); 
//		DBQueryBuilder.appendOrderBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_MUSICIAN, DbHelper.MUSICIAN_LANG_ID));
		DBQueryBuilder.appendLimitOffset(strBuilder, resultCount, offset); 
		Cursor resultCursor = db.rawQuery(strBuilder.toString(), null); 
		
		return parseMuscianInfoQueryResult(resultCursor); 
	}
	
	public ArrayList<SongType> searchSongType(String literal, LANG_INDEX langID, SearchMode mode, int offset, int resultCount) {
		StringBuilder strBuilder = new StringBuilder(); 
		createSongTypeInfoQueryString(strBuilder, "", false); 
		
		String colSearch = ""; 
		if(mode == SearchMode.MODE_FULL){
			colSearch = DbHelper.TYPE_RAWNAME; 
		}else if (mode == SearchMode.MODE_SIGNED) {
			colSearch = DbHelper.TYPE_NAME; 
		}else {
			colSearch = DbHelper.TYPE_SHORTNAME; 
		}

		if (langID == LANG_INDEX.ALL_LANGUAGE) {
			if(literal != null && !literal.equals(""))
			{
				DBQueryBuilder.appendWhereFromFormat(strBuilder, "%K LIKE %@", colSearch, literal + "%"); 
			}else {
				DBQueryBuilder.appendWhereFromFormat(strBuilder, "%K!=%@", DbHelper.TYPE_ID, "0"); 
			}
		}else {
			if(literal != null && !literal.equals(""))
			{
				DBQueryBuilder.appendWhereFromFormat(strBuilder, "(%K LIKE %@) AND (%K = %@)", colSearch, literal + "%", DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGTYPE, DbHelper.TYPE_LANG_ID), langID.ordinal()); 
			}else {
				DBQueryBuilder.appendWhereFromFormat(strBuilder, "%K = %@", DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGTYPE, DbHelper.TYPE_LANG_ID), langID.ordinal()); 
			}
		}
//		DBQueryBuilder.appendOrderBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGTYPE, DbHelper.TYPE_SORT));
		DBQueryBuilder.appendLimitOffset(strBuilder, resultCount, offset); 
		Cursor resultCursor = db.rawQuery(strBuilder.toString(), null); 
		
		return parseSongTypeInfoQueryResult(resultCursor); 
	}
	
	public ArrayList<SongType> searchSongTypeHDD(String literal, LANG_INDEX langID, SearchMode mode, int offset, int resultCount) {
		if(!isHDDAvailable) return new ArrayList<SongType>(); 
		
		String hddPath = mContext.getDatabasePath(DbHelper.HDD_DBName + DbHelper.DBExtension).getAbsolutePath(); 
		attachNewDatabase(hddPath, HDDDBName); 
		
		StringBuilder strBuilder = new StringBuilder(); 
		createSongTypeInfoQueryString(strBuilder, "", false); 
		
		String colSearch = ""; 
		if(mode == SearchMode.MODE_FULL){
			colSearch = DbHelper.TYPE_RAWNAME; 
		}else if (mode == SearchMode.MODE_SIGNED) {
			colSearch = DbHelper.TYPE_NAME; 
		}else {
			colSearch = DbHelper.TYPE_SHORTNAME; 
		}
		
		if(literal != null && !literal.equals(""))
		{
			DBQueryBuilder.appendWhereFromFormat(strBuilder, "(%K LIKE %@)", colSearch, literal + "%"); //  AND (%K = %@), , DbHelper.TYPE_LANG_ID, langID
		}
//		else {
//			DBQueryBuilder.appendWhereFromFormat(strBuilder, "%K = %@", DbHelper.TYPE_LANG_ID, langID); 
//		}
//		DBQueryBuilder.appendOrderBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGTYPE, DbHelper.TYPE_SORT));
		DBQueryBuilder.appendLimitOffset(strBuilder, resultCount, offset); 
		Cursor resultCursor = db.rawQuery(strBuilder.toString(), null); 
		
		ArrayList<SongType> songTypes = parseSongTypeInfoQueryResult(resultCursor); 
		detachDatabase(HDDDBName); 
		
		resultCursor.close();
		
		return songTypes; 
	}
	
	public ArrayList<SongType> searchSongTypeSongSinger(String literal, LANG_INDEX langID, SearchMode mode, int offset, int resultCount) {
		StringBuilder strBuilder = new StringBuilder(); 
		createSongTypeInfoQueryString(strBuilder, "", true); 
		
		String colSearch = ""; 
		if(mode == SearchMode.MODE_FULL){
			colSearch = DbHelper.TYPE_RAWNAME; 
		}else if (mode == SearchMode.MODE_SIGNED) {
			colSearch = DbHelper.TYPE_NAME; 
		}else {
			colSearch = DbHelper.TYPE_SHORTNAME; 
		}
		
		if(literal != null && !literal.equals(""))
		{
			DBQueryBuilder.appendWhereFromFormat(strBuilder, "(%K=%@)AND(%K LIKE %@)", DbHelper.SONG_MED_SINGER, "1", DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGTYPE, colSearch), literal + "%" ); 
		}else {
			DBQueryBuilder.appendWhereFromFormat(strBuilder, "(%K=%@)",DbHelper.SONG_MED_SINGER, "1"); 
		}
		DBQueryBuilder.appendGroupBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGTYPE, DbHelper.TYPE_ID)); 
//		DBQueryBuilder.appendOrderBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGTYPE, DbHelper.TYPE_SORT));
		DBQueryBuilder.appendLimitOffset(strBuilder, resultCount, offset); 
		Cursor resultCursor = db.rawQuery(strBuilder.toString(), null); 
		
		return parseSongTypeInfoQueryResult(resultCursor); 
	}
	
	public ArrayList<SongType> searchSongTypeNewVol(String literal, LANG_INDEX langID, SearchMode mode, int offset, int resultCount) {
		StringBuilder strBuilder = new StringBuilder(); 
		createSongTypeInfoQueryString(strBuilder, "", true); 
		
		String colSearch = ""; 
		if(mode == SearchMode.MODE_FULL){
			colSearch = DbHelper.TYPE_RAWNAME; 
		}else if (mode == SearchMode.MODE_SIGNED) {
			colSearch = DbHelper.TYPE_NAME; 
		}else {
			colSearch = DbHelper.TYPE_SHORTNAME; 
		}
		
		if(literal != null && !literal.equals(""))
		{
			DBQueryBuilder.appendWhereFromFormat(strBuilder, "(%K=%@)AND(%K LIKE %@)", DbHelper.SONG_NEWVOL, "1", DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGTYPE, colSearch), literal + "%" ); 
		}else {
			DBQueryBuilder.appendWhereFromFormat(strBuilder, "(%K=%@)",DbHelper.SONG_NEWVOL, "1"); 
		}
		DBQueryBuilder.appendGroupBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGTYPE, DbHelper.TYPE_ID)); 
//		DBQueryBuilder.appendOrderBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGTYPE, DbHelper.TYPE_SORT));
		DBQueryBuilder.appendLimitOffset(strBuilder, resultCount, offset); 
		Cursor resultCursor = db.rawQuery(strBuilder.toString(), null); 
		
		return parseSongTypeInfoQueryResult(resultCursor); 
	}
	
	public int getLanguageID(String name) {
		ArrayList<Language> langs = searchSongLanguage(name, SearchMode.MODE_SIGNED, 0, 1); 
		int langID = 0; 
		if(langs.size() > 0)
			langID = langs.get(0).getID(); 
		
		return langID; 
	}
	
	public ArrayList<Language> searchSongLanguage(String literal, SearchMode mode, int offset, int resultCount) {
		StringBuilder strBuilder = new StringBuilder(); 
		DBQueryBuilder.appendSelect(strBuilder); 
		DBQueryBuilder.appendColumns(strBuilder, new String[]{DbHelper.LANG_ID, DbHelper.LANG_NAME, DbHelper.LANG_COVER, DbHelper.LANG_FONT, DbHelper.LANG_SORT, DbHelper.LANG_INDEX}); 
		DBQueryBuilder.appendTables(strBuilder, DbHelper.TABLE_LANGUAGES); 
		
		String colSearch = ""; 
		if(mode == SearchMode.MODE_FULL){
			colSearch = DbHelper.LANG_RAWNAME; 
		}else if (mode == SearchMode.MODE_SIGNED) {
			colSearch = DbHelper.LANG_NAME; 
		}else {
			colSearch = DbHelper.LANG_SHORTNAME; 
		}
		
		if(literal != null && !literal.equals(""))
		{
			DBQueryBuilder.appendWhereFromFormat(strBuilder, "%K LIKE %@ and (%K!=%@ AND %K!=%@)", colSearch, literal + "%", DbHelper.LANG_ID, LANG_INDEX.FRENCH.ordinal(), DbHelper.LANG_ID, AppConfig.LANG_INDEX_OTHER);
			if(!MyApplication.flagEverConnect){
				DBQueryBuilder.appendFromFormat(strBuilder, " AND (%K=%@)", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_LANGUAGES, DbHelper.LANG_ID), 0);
			}
		}else {
			if (curTocType != TOCTYPE_SK9000) {
				StringBuilder modelSb = new StringBuilder(); 
				modelSb.append('('); 
				modelSb.append(curTocType); 
				
				if(curHDDToc > 0) {
					modelSb.append("," + curHDDToc);
				}
				modelSb.append(')'); 
				
				DBQueryBuilder.appendWhereFromFormat(strBuilder, "z_pk in (select distinct (zlanguageid) from zsongs inner join zsongmodel on zsongs.zso_md=zsongmodel.zso_md where zsongmodel.zmodel in"+ modelSb.toString() +")"/*"(%K!=%@ AND %K!=%@)" DbHelper.LANG_ID, LANG_INDEX.FRENCH.ordinal(), DbHelper.LANG_ID, AppConfig.LANG_INDEX_OTHER*/);	 
			}else {
				DBQueryBuilder.appendWhereFromFormat(strBuilder, "z_pk in (select distinct (zlanguageid) from zsongs)"/*"(%K!=%@ AND %K!=%@)" DbHelper.LANG_ID, LANG_INDEX.FRENCH.ordinal(), DbHelper.LANG_ID, AppConfig.LANG_INDEX_OTHER*/); 
			}
			if(!MyApplication.flagEverConnect){
				DBQueryBuilder.appendFromFormat(strBuilder, " AND (%K=%@)", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_LANGUAGES, DbHelper.LANG_ID), 0);
			}
		}
		DBQueryBuilder.appendOrderBy(strBuilder, DbHelper.LANG_SORT); 
		DBQueryBuilder.appendLimitOffset(strBuilder, resultCount, offset); 
		Cursor resultCursor = db.rawQuery(strBuilder.toString(), null); 
		
		ArrayList<Language> arr = new ArrayList<Language>();
		if(resultCursor.getCount() > 0) {
			while(resultCursor.moveToNext())
			{
				Language aMusician = new Language(); 
				aMusician.setID(resultCursor.getInt(resultCursor.getColumnIndex(DbHelper.LANG_ID))); 
				aMusician.setName(resultCursor.getString(resultCursor.getColumnIndex(DbHelper.LANG_NAME))); 
				aMusician.setCover(resultCursor.getString(resultCursor.getColumnIndex(DbHelper.LANG_COVER))); 
				aMusician.setFont(resultCursor.getString(resultCursor.getColumnIndex(DbHelper.LANG_FONT))); 
				aMusician.setSort(resultCursor.getInt(resultCursor.getColumnIndex(DbHelper.LANG_SORT))); 
				int index = resultCursor.getInt(resultCursor.getColumnIndex(DbHelper.LANG_INDEX)); 
				aMusician.setIndex(LANG_INDEX.values()[index]); 
				arr.add(aMusician); 
			}
		}
		
		resultCursor.close();
		
		return arr; 
	}
	
	public ArrayList<Language> searchSongLanguageID(String langID) {
		StringBuilder strBuilder = new StringBuilder(); 
		DBQueryBuilder.appendSelect(strBuilder); 
		DBQueryBuilder.appendColumns(strBuilder, new String[]{DbHelper.LANG_ID, DbHelper.LANG_NAME, DbHelper.LANG_COVER, DbHelper.LANG_FONT, DbHelper.LANG_SORT, DbHelper.LANG_INDEX}); 
		DBQueryBuilder.appendTables(strBuilder, DbHelper.TABLE_LANGUAGES); 
		DBQueryBuilder.appendWhereFromFormat(strBuilder, "%K = %@", DbHelper.LANG_ID, langID); 

		DBQueryBuilder.appendOrderBy(strBuilder, DbHelper.LANG_SORT);  
		Cursor resultCursor = db.rawQuery(strBuilder.toString(), null); 
		
		ArrayList<Language> arr = new ArrayList<Language>();
		if(resultCursor.getCount() > 0) {
			while(resultCursor.moveToNext())
			{
				Language aMusician = new Language(); 
				aMusician.setID(resultCursor.getInt(resultCursor.getColumnIndex(DbHelper.LANG_ID))); 
				aMusician.setName(resultCursor.getString(resultCursor.getColumnIndex(DbHelper.LANG_NAME))); 
				aMusician.setCover(resultCursor.getString(resultCursor.getColumnIndex(DbHelper.LANG_COVER))); 
				aMusician.setFont(resultCursor.getString(resultCursor.getColumnIndex(DbHelper.LANG_FONT))); 
				aMusician.setSort(resultCursor.getInt(resultCursor.getColumnIndex(DbHelper.LANG_SORT))); 
				int index = resultCursor.getInt(resultCursor.getColumnIndex(DbHelper.LANG_INDEX)); 
				aMusician.setIndex(LANG_INDEX.values()[index]); 
				arr.add(aMusician); 
			}
		}
		
		resultCursor.close();
		
		return arr; 
	}
/*	
	private KVObject[] getAllSongs(String langID) 
	{
		StringBuilder queryStr = new StringBuilder(); 
		DBQueryBuilder.appendSelect(queryStr); 
		DBQueryBuilder.appendColumns(queryStr, new String[]{DbHelper.SONG_NAME, DbHelper.SONG_PK}); 
		DBQueryBuilder.appendTables(queryStr, DbHelper.TABLE_SONGS); 
		DBQueryBuilder.appendWhereFromFormat(queryStr, "%K LIKE %@", DbHelper.SONG_LANGUAGE_ID, langID); 
		
		DBQueryBuilder.appendOrderBy(queryStr, DbHelper.SONG_NAME); 
		Cursor resultCursor = db.rawQuery(queryStr.toString(), null); 
		
		KVObject[] arr = new KVObject[resultCursor.getCount()];
		int i = 0; 
		if(resultCursor.getCount() > 0) {
			while(resultCursor.moveToNext())
			{
				String key = resultCursor.getString(resultCursor.getColumnIndex(DbHelper.SONG_NAME)); 
				String value = resultCursor.getString(resultCursor.getColumnIndex(DbHelper.SONG_PK)); 
				
				arr[i++] = new KVObject(key, value); 
			}
		}
		return arr; 
	}
	
	private KVObject[] getAllMusicians(String langID) 
	{
		StringBuilder queryStr = new StringBuilder(); 
		DBQueryBuilder.appendSelect(queryStr); 
		DBQueryBuilder.appendColumns(queryStr, new String[]{DbHelper.MUSICIAN_NAME, DbHelper.MUSICIAN_ID}); 
		DBQueryBuilder.appendTables(queryStr, DbHelper.TABLE_MUSICIAN); 

		DBQueryBuilder.appendWhereFromFormat(queryStr, "%K LIKE %@", DbHelper.SONG_LANGUAGE_ID, langID); 
		DBQueryBuilder.appendOrderBy(queryStr, DbHelper.MUSICIAN_NAME); 
		
		Cursor resultCursor = db.rawQuery(queryStr.toString(), null); 
		
		KVObject[] arr = new KVObject[resultCursor.getCount()];
		int i = 0; 
		if(resultCursor.getCount() > 0) {
			while(resultCursor.moveToNext())
			{
				String key = resultCursor.getString(resultCursor.getColumnIndex(DbHelper.MUSICIAN_NAME)); 
				String value = resultCursor.getString(resultCursor.getColumnIndex(DbHelper.MUSICIAN_ID)); 

				arr[i++] = new KVObject(key, value);  
			}
		}
		return arr; 
	}
	
	private KVObject[] getAllSingers(String langID) 
	{
		StringBuilder queryStr = new StringBuilder(); 
		DBQueryBuilder.appendSelect(queryStr); 
		DBQueryBuilder.appendColumns(queryStr, new String[]{DbHelper.SINGER_NAME, DbHelper.SINGER_ID}); 
		DBQueryBuilder.appendTables(queryStr, DbHelper.TABLE_SINGER); 

		DBQueryBuilder.appendWhereFromFormat(queryStr, "%K LIKE %@", DbHelper.SONG_LANGUAGE_ID, langID); 
		DBQueryBuilder.appendOrderBy(queryStr, DbHelper.SINGER_NAME); 
		
		Cursor resultCursor = db.rawQuery(queryStr.toString(), null); 
		
		KVObject[] arr = new KVObject[resultCursor.getCount()];
		int i = 0; 
		if(resultCursor.getCount() > 0) {
			while(resultCursor.moveToNext())
			{
				String key = resultCursor.getString(resultCursor.getColumnIndex(DbHelper.SINGER_NAME)); 
				String value = resultCursor.getString(resultCursor.getColumnIndex(DbHelper.SINGER_ID)); 

				arr[i++] = new KVObject(key, value); 
			}
		}
		return arr; 
	}
	
	private KVObject[] getAllSongTypes(String langID) 
	{
		StringBuilder queryStr = new StringBuilder(); 
		DBQueryBuilder.appendSelect(queryStr); 
		DBQueryBuilder.appendColumns(queryStr, new String[]{DbHelper.TYPE_NAME, DbHelper.TYPE_ID}); 
		DBQueryBuilder.appendTables(queryStr, DbHelper.TABLE_SONGTYPE); 

		DBQueryBuilder.appendWhereFromFormat(queryStr, "%K LIKE %@", DbHelper.SONG_LANGUAGE_ID, langID); 
		DBQueryBuilder.appendOrderBy(queryStr, DbHelper.SINGER_NAME); 
		
		Cursor resultCursor = db.rawQuery(queryStr.toString(), null); 
		
		KVObject[] arr = new KVObject[resultCursor.getCount()];
		int i = 0; 
		if(resultCursor.getCount() > 0) {
			while(resultCursor.moveToNext())
			{
				String key = resultCursor.getString(resultCursor.getColumnIndex(DbHelper.TYPE_NAME)); 
				String value = resultCursor.getString(resultCursor.getColumnIndex(DbHelper.TYPE_ID)); 

				arr[i++] = new KVObject(key, value); 
			}
		}
		return arr; 
	}
	
	private KVObject[] getAllLanguages() 
	{
		StringBuilder queryStr = new StringBuilder(); 
		DBQueryBuilder.appendSelect(queryStr); 
		DBQueryBuilder.appendColumns(queryStr, new String[]{DbHelper.LANG_NAME, DbHelper.LANG_ID}); 
		DBQueryBuilder.appendTables(queryStr, DbHelper.TABLE_LANGUAGES); 
		
//		DBQueryBuilder.appendOrderBy(queryStr, DbHelper.SINGER_NAME); 
		
		Cursor resultCursor = db.rawQuery(queryStr.toString(), null); 
		
		KVObject[] arr = new KVObject[resultCursor.getCount()];
		int i = 0; 
		if(resultCursor.getCount() > 0) {
			while(resultCursor.moveToNext())
			{
				String key = resultCursor.getString(resultCursor.getColumnIndex(DbHelper.LANG_NAME)); 
				String value = resultCursor.getString(resultCursor.getColumnIndex(DbHelper.LANG_ID)); 

				arr[i++] = new KVObject(key, value); 
			}
		}
		return arr; 
	}
*/	
	public ArrayList<String> getAllSongDataFile()
    {
        StringBuilder queryStr = new StringBuilder();
        DBQueryBuilder.appendSelect(queryStr);
        DBQueryBuilder.appendColumns(queryStr, new String[] { DbHelper.FILE_NAME });
        DBQueryBuilder.appendTables(queryStr, DbHelper.TABLE_FILE_DATA);

        DBQueryBuilder.appendOrderBy(queryStr, DbHelper.FILE_NAME);

        Cursor resultCursor = db.rawQuery(queryStr.toString(), null);

        ArrayList<String> arr = new ArrayList<String>();

        if (resultCursor.getCount() > 0)
        {
            while (resultCursor.moveToNext())
            {
                String key = resultCursor.getString(resultCursor.getColumnIndex(DbHelper.FILE_NAME));
                if (key == null || key.equals(""))
                    continue;
                arr.add(key);
            }
        }
        
        resultCursor.close();
        
        return arr;
    }
/*	
	public void reArrangeSongTitle(String langID) {
		KVObject[] array = getAllSongs(langID); 
		Log.e("", "Song count: " + array.length); 
//		DualPivotQuickSort.sort(array);
		DualPivotQuickSort.sort(array, 0, array.length); 
		for (int i = 0; i < array.length; i++) {
			updateSortValueTableSongs(array[i], i+1); 
		}
		
//		
//		// Print sorted array to file
//		String tempPath; 
//		try {
//			tempPath = FileManager.getExtStorageAppBundlePath() + "/sortArray.txt";
//
//			FileOutputStream foStream = new FileOutputStream(new File(tempPath)); 
//
//			for (int i = 0; i < array.length; i++) {
//
//				String strIndex = String.format(AppConfig.curLocale, "%06d", i+1); 
//				byte[] buffer = strIndex.getBytes(); 
//				foStream.write(buffer); 
//				foStream.write('_'); 
//				buffer = array[i].getKey().getBytes(); 
//				foStream.write(buffer); 
//				foStream.write('_'); 
//				buffer = array[i].getValue().getBytes(); 
//				foStream.write(buffer); 
//				foStream.write('\n'); 
//			}
//
//			foStream.flush();
//			foStream.close(); 
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		} 
//		
	}
	
	public void reArrangeMusicianName(String langID) {
		KVObject[] array = getAllMusicians(langID); 
		DualPivotQuickSort.sort(array); 
		for (int i = 0; i < array.length; i++) {
			updateSortValueTableMusicians(array[i], i+1); 
		}
	}
	
	public void reArrangeSingerName(String langID) {
		KVObject[] array = getAllSingers(langID); 
		DualPivotQuickSort.sort(array); 
		for (int i = 0; i < array.length; i++) {
			updateSortValueTableSingers(array[i], i+1); 
		}
	}
	
	public void reArrangeSongTypeName(String langID) {
		KVObject[] array = getAllSongTypes(langID); 
		DualPivotQuickSort.sort(array); 
		for (int i = 0; i < array.length; i++) {
			updateSortValueTableSongTypes(array[i], i+1); 
		}
	}
	
	public void reArrangeLanguageName() {
		KVObject[] array = getAllLanguages(); 
		
		
//		KVObject[] tempArr = new KVObject[array.length]; 
//		for(int i =0; i < array.length; i++) {
//			String name = array[i].getKey(); 
//			if(name.equals(mContext.getResources().getString(R.string.lang_vni))) {
//				tempArr[LANG_INDEX.VIETNAMESE.ordinal() + 1].setKey(array[i].getKey()); 
//				tempArr[LANG_INDEX.VIETNAMESE.ordinal() + 1].setValue(array[i].getValue());
//			}else if(name.equals(mContext.getResources().getString(R.string.lang_engl))) {
//				tempArr[LANG_INDEX.ENGLISH.ordinal() + 1].setKey(array[i].getKey()); 
//				tempArr[LANG_INDEX.ENGLISH.ordinal() + 1].setValue(array[i].getValue()); 
//			}else if(name.equals(mContext.getResources().getString(R.string.lang_fren))) {
//				tempArr[LANG_INDEX.FRENCH.ordinal() + 1].setKey(array[i].getKey()); 
//				tempArr[LANG_INDEX.FRENCH.ordinal() + 1].setValue(array[i].getValue()); 
//			}else if(name.equals(mContext.getResources().getString(R.string.lang_chne))) {
//				tempArr[LANG_INDEX.CHINESE.ordinal() + 1].setKey(array[i].getKey()); 
//				tempArr[LANG_INDEX.CHINESE.ordinal() + 1].setValue(array[i].getValue()); 
//			}else if(name.equals(mContext.getResources().getString(R.string.lang_phil))) {
//				tempArr[LANG_INDEX.PHILIPINESE.ordinal() + 1].setKey(array[i].getKey()); 
//				tempArr[LANG_INDEX.PHILIPINESE.ordinal() + 1].setValue(array[i].getValue()); 
//			}else if(name.equals(mContext.getResources().getString(R.string.lang_hndi))) {
//				tempArr[LANG_INDEX.HINDI.ordinal() + 1].setKey(array[i].getKey()); 
//				tempArr[LANG_INDEX.HINDI.ordinal() + 1].setValue(array[i].getValue()); 
//			}else {
//				
//			}
//		}
		
		
//		DualPivotQuickSort.sort(array); 
//		for (int i = 0; i < array.length; i++) {
//		updateSortValueTableLanguage(array[i], i+1); 
//	}
		
		for(int i =0; i < array.length; i++) {
			String name = array[i].getKey(); 
			if(name.equals(mContext.getResources().getString(R.string.lang_vni))) {
				updateSortValueTableLanguage(array[i],LANG_INDEX.VIETNAMESE.ordinal() + 1); 
			}else if(name.equals(mContext.getResources().getString(R.string.lang_engl))) {
				updateSortValueTableLanguage(array[i], LANG_INDEX.ENGLISH.ordinal()+1); 
			}else if(name.equals(mContext.getResources().getString(R.string.lang_fren))) {
				updateSortValueTableLanguage(array[i], LANG_INDEX.FRENCH.ordinal()+1); 
			}else if(name.equals(mContext.getResources().getString(R.string.lang_chne))) {
				updateSortValueTableLanguage(array[i], LANG_INDEX.CHINESE.ordinal()+1); 
			}else if(name.equals(mContext.getResources().getString(R.string.lang_phil))) {
				updateSortValueTableLanguage(array[i], LANG_INDEX.PHILIPINESE.ordinal()+1); 
			}else if(name.equals(mContext.getResources().getString(R.string.lang_hndi))) {
				updateSortValueTableLanguage(array[i], LANG_INDEX.HINDI.ordinal()+1); 
			}else {
				updateSortValueTableLanguage(array[i], LANG_INDEX.values().length+1); 
			}
		}
	}

	public boolean deleteSong(String idStr) {
		int rows = 0;
		try {
			rows = db.delete(DbHelper.TABLE_SONGS, "?=?", 
					new String[]{DbHelper.SONG_ID, idStr}); 
		}
		catch (Exception e) {
		}
		finally {
			SQLiteDatabase.releaseMemory(); 
		}
		return (rows != 0); 
	}
	
	private boolean updateSortValueTableSongs(KVObject anObject, int index) {
		int rows = 0; 
		try {
			ContentValues value = new ContentValues(); 
			value.put(DbHelper.SONG_SORT, index); 
			
			rows = db.updateWithOnConflict(DbHelper.TABLE_SONGS, value, DbHelper.SONG_PK +"=?", 
					new String[]{anObject.getValue()},SQLiteDatabase.CONFLICT_IGNORE); 
			
//			Log.d("", "rows:" + rows +" song id:"+anObject.getValue()); 
		} catch (Exception e) {
			Log.e("", "updateSortValueTableSong exception"); 
		}
		finally {
			//SQLiteDatabase.releaseMemory(); 
		}
		return (rows != 0); 
	}
	
	private boolean updateSortValueTableMusicians(KVObject anObject, int index) {
		int rows = 0; 
		try {
			ContentValues value = new ContentValues(); 
			value.put(DbHelper.MUSICIAN_SORT, index); 
			
			rows = db.updateWithOnConflict(DbHelper.TABLE_MUSICIAN, value,  DbHelper.MUSICIAN_ID + "=?", 
					new String[]{anObject.getValue()},SQLiteDatabase.CONFLICT_IGNORE); 
			
//			Log.d("", "rows:" + rows +" musician id:"+anObject.getValue()); 
		} catch (Exception e) {
			Log.e("", "updateSortValueTableMusicians exception"); 
		}
		finally {
			//SQLiteDatabase.releaseMemory(); 
		}
		return (rows != 0); 
	}
	
	private boolean updateSortValueTableSingers(KVObject anObject, int index) {
		int rows = 0; 
		try {
			ContentValues value = new ContentValues(); 
			value.put(DbHelper.SINGER_SORT, index); 
			
			rows = db.updateWithOnConflict(DbHelper.TABLE_SINGER, value,  DbHelper.SINGER_ID+"=?", 
					new String[]{anObject.getValue()},SQLiteDatabase.CONFLICT_IGNORE); 
			
//			Log.d("", "rows:" + rows +" singer id:"+anObject.getValue()); 
		} catch (Exception e) {
			Log.e("", "updateSortValueTableSingers exception"); 
		}
		finally {
			//SQLiteDatabase.releaseMemory(); 
		}
		return (rows != 0); 
	}
	
	private boolean updateSortValueTableSongTypes(KVObject anObject, int index) {
		int rows = 0; 
		try {
			ContentValues value = new ContentValues(); 
			value.put(DbHelper.TYPE_SORT, index); 
			
			rows = db.updateWithOnConflict(DbHelper.TABLE_SONGTYPE, value,  DbHelper.TYPE_ID+"=?", 
					new String[]{anObject.getValue()},SQLiteDatabase.CONFLICT_IGNORE); 
			
//			Log.d("", "rows:" + rows +" song type id:"+anObject.getValue()); 
		} catch (Exception e) {
			Log.e("", "updateSortValueTableSongTypes exception"); 
		}
		finally {
			//SQLiteDatabase.releaseMemory(); 
		}
		return (rows != 0); 
	}
	
	private boolean updateSortValueTableLanguage(KVObject anObject, int index) {
		int rows = 0; 
		try {
			ContentValues value = new ContentValues(); 
			value.put(DbHelper.LANG_SORT, index); 
			
			rows = db.updateWithOnConflict(DbHelper.TABLE_LANGUAGES, value,  DbHelper.LANG_ID+"=?", 
					new String[]{anObject.getValue()},SQLiteDatabase.CONFLICT_IGNORE); 
			
//			Log.d("", "rows:" + rows +" song type id:"+anObject.getValue()); 
		} catch (Exception e) {
			Log.e("", "updateSortValueTableLanguage exception"); 
		}
		finally {
			//SQLiteDatabase.releaseMemory(); 
		}
		return (rows != 0); 
	}
*/	
	public boolean updateSongPlayCount(Song aSong) {
		
		String sqlStatement = "UPDATE "+ DbHelper.TABLE_SONGS + " SET " + DbHelper.SONG_PLAYCNT + "=" +DbHelper.SONG_PLAYCNT+"+1 "+
							   	"WHERE "+ DbHelper.SONG_ID+ "='"+aSong.getId()+"'"; 
		db.execSQL(sqlStatement); 
		
		sqlStatement = "UPDATE "+ DbHelper.TABLE_MUSICIAN + " SET " + DbHelper.MUSICIAN_PLAYCNT + "=" +DbHelper.MUSICIAN_PLAYCNT+"+1 "+
				   				"WHERE "+ DbHelper.MUSICIAN_ID+ "='"+aSong.getMusicianId()+"'"; 
		db.execSQL(sqlStatement); 
		
		sqlStatement = "UPDATE "+ DbHelper.TABLE_SINGER + " SET " + DbHelper.SINGER_PLAYCNT + "=" +DbHelper.SINGER_PLAYCNT+"+1 "+
								"WHERE "+ DbHelper.SINGER_ID+ "='"+aSong.getSingerId()+"'"; 
		db.execSQL(sqlStatement); 

//		sqlStatement = "UPDATE "+ DbHelper.TABLE_SONGTYPE + " SET " + DbHelper.TYPE_PLAYCNT + "=" +DbHelper.TYPE_PLAYCNT+"+1 "+
//				"WHERE "+ DbHelper.TYPE_ID+ "='"+aSong.getTypeId()+"'"; 
		db.execSQL(sqlStatement); 
		

		if(isHDDAvailable) {
			String hddPath = mContext.getDatabasePath(DbHelper.HDD_DBName + DbHelper.DBExtension).getAbsolutePath(); 
			attachNewDatabase(hddPath, HDDDBName); 
			
			sqlStatement = "UPDATE "+ HDDDBName + "." + DbHelper.TABLE_SONGS + " SET " + DbHelper.SONG_PLAYCNT + "=" +DbHelper.SONG_PLAYCNT+"+1 "+
					"WHERE "+ DbHelper.SONG_ID+ "='"+aSong.getId()+"'"; 
			db.execSQL(sqlStatement); 

			sqlStatement = "UPDATE "+ HDDDBName + "." + DbHelper.TABLE_MUSICIAN + " SET " + DbHelper.MUSICIAN_PLAYCNT + "=" +DbHelper.MUSICIAN_PLAYCNT+"+1 "+
					"WHERE "+ DbHelper.MUSICIAN_ID+ "='"+aSong.getMusicianId()[0]+"'"; 
			db.execSQL(sqlStatement); 

			sqlStatement = "UPDATE "+ HDDDBName + "." + DbHelper.TABLE_SINGER + " SET " + DbHelper.SINGER_PLAYCNT + "=" +DbHelper.SINGER_PLAYCNT+"+1 "+
					"WHERE "+ DbHelper.SINGER_ID+ "='"+aSong.getSingerId()+"'"; 
			db.execSQL(sqlStatement); 

//			sqlStatement = "UPDATE "+ HDDDBName + "." + DbHelper.TABLE_SONGTYPE + " SET " + DbHelper.TYPE_PLAYCNT + "=" +DbHelper.TYPE_PLAYCNT+"+1 "+
//					"WHERE "+ DbHelper.TYPE_ID+ "='"+aSong.getTypeId()+"'"; 
			db.execSQL(sqlStatement); 
			
			detachDatabase(HDDDBName); 
		}
		return true; 
	}
/*	
	public boolean updateSong(SongInfo songInfo) {
		int rows = 0;
		try {
			long langID = insertSongLanguage(songInfo.getLanguage(), songInfo.getPreferFont()); 
			
			long authorID = insertMusician(songInfo.getAuthor(), String.format("%d", langID)); 
			long singerID = insertSinger(songInfo.getSinger(), String.format("%d", langID)); 
			long typeID = insertSongType(songInfo.getSongType(), String.format("%d", langID)); 
			
			ArrayList<Language> langs = searchSongLanguageID(String.valueOf(langID)); 
			LANG_INDEX idx = LANG_INDEX.VIETNAMESE; 
			if(langs.size() > 0) {
				idx = langs.get(0).getIndex(); 
			}
			
			ContentValues value = new ContentValues(); 
			
			value.put(DbHelper.SONG_ID, songInfo.getId()); 
            value.put(DbHelper.SONG_ID5, songInfo.getIndex5()); 
			value.put(DbHelper.SONG_NAME, songInfo.getTitle()); 
			value.put(DbHelper.SONG_SHORTNAME, StringUtils.getPinyinString(songInfo.getTitle(), idx));
			value.put(DbHelper.SONG_RAWNAME, StringUtils.getRawString(songInfo.getTitle(), idx)); 
			value.put(DbHelper.SONG_LYRIC, songInfo.getLyric()); 
			value.put(DbHelper.SONG_MIDI, songInfo.getMidi()); 
			value.put(DbHelper.SONG_MED_SINGER, songInfo.getIsSinger()); 
			value.put(DbHelper.SONG_NEWVOL, songInfo.getNewVol()); 
			value.put(DbHelper.SONG_MUSICIAN_ID, authorID); 
			value.put(DbHelper.SONG_SINGER_ID, singerID);
			value.put(DbHelper.SONG_LANGUAGE_ID, langID); 
			value.put(DbHelper.SONG_TYPE_ID, typeID); 
			value.put(DbHelper.SONG_VIDEO_ID, songInfo.getVideoName()); 
			value.put(DbHelper.SONG_MEDIANAME, songInfo.getMediaName()); 
			
			rows = db.updateWithOnConflict(DbHelper.TABLE_SONGS, value, DbHelper.SONG_ID + "=?", 
					new String[]{String.format("%06d", songInfo.getId())},SQLiteDatabase.CONFLICT_IGNORE); 
		}
		catch (Exception e) {
		}
		finally {
			SQLiteDatabase.releaseMemory(); 
		}
		return (rows != 0); 
	}
	
	public boolean updateMusician(String name, String langID) {
		int rows = 0;
		try {
			ArrayList<Language> langs = searchSongLanguageID(langID); 
			LANG_INDEX idx = LANG_INDEX.VIETNAMESE; 
			if(langs.size() > 0) {
				idx = langs.get(0).getIndex(); 
			}
			
			ContentValues authorVal = new ContentValues(); 
			authorVal.put(DbHelper.MUSICIAN_NAME, name); 
			authorVal.put(DbHelper.MUSICIAN_RAWNAME, StringUtils.getRawString(name, idx));
			authorVal.put(DbHelper.MUSICIAN_SHORTNAME, StringUtils.getPinyinString(name, idx));
			authorVal.put(DbHelper.SINGER_LANG_ID, langID); 
			
			String coverFullPath = FileManager.getExtStoragePicturesDir() 
					+ String.format(AppConfig.curLocale, "/%s.jpg", name); 
			authorVal.put(DbHelper.MUSICIAN_COVER, coverFullPath);
			byte[] photo = getPhotoBlobData(coverFullPath); 
			authorVal.put(DbHelper.MUSICIAN_COVERDATA, photo); 
			
			rows = db.updateWithOnConflict(DbHelper.TABLE_MUSICIAN, authorVal, DbHelper.MUSICIAN_NAME + "=?", 
					new String[]{name},SQLiteDatabase.CONFLICT_IGNORE); 
		}
		catch (Exception e) {
		}
		finally {
			SQLiteDatabase.releaseMemory(); 
		}
		return (rows != 0); 
	}
	
	public boolean updateSinger(String name, String langID) {
		int rows = 0;
		try {
			ArrayList<Language> langs = searchSongLanguageID(langID); 
			LANG_INDEX idx = LANG_INDEX.VIETNAMESE; 
			if(langs.size() > 0) {
				idx = langs.get(0).getIndex(); 
			}
			
			ContentValues singerVal = new ContentValues(); 
			singerVal.put(DbHelper.SINGER_NAME, name); 
			singerVal.put(DbHelper.SINGER_RAWNAME, StringUtils.getRawString(name, idx));
			singerVal.put(DbHelper.SINGER_SHORTNAME, StringUtils.getPinyinString(name, idx));
			singerVal.put(DbHelper.SINGER_LANG_ID, langID); 
			
			String coverFullPath = FileManager.getExtStoragePicturesDir() 
					+ String.format(AppConfig.curLocale, "/%s.jpg", name); 
			singerVal.put(DbHelper.SINGER_COVER, coverFullPath);
			byte[] photo = getPhotoBlobData(coverFullPath); 
			singerVal.put(DbHelper.SINGER_COVERDATA, photo); 
			
			rows = db.updateWithOnConflict(DbHelper.TABLE_SINGER, singerVal, DbHelper.SINGER_NAME + "=?", 
					new String[]{name},SQLiteDatabase.CONFLICT_IGNORE); 
		}
		catch (Exception e) {
		}
		finally {
			SQLiteDatabase.releaseMemory(); 
		}
		return (rows != 0); 
	}

	public boolean updateSongType(String name, String langID) {
		int rows = 0;
		try {
			ArrayList<Language> langs = searchSongLanguageID(langID); 
			LANG_INDEX idx = LANG_INDEX.VIETNAMESE; 
			if(langs.size() > 0) {
				idx = langs.get(0).getIndex(); 
			}
			
			ContentValues typeVal = new ContentValues(); 
			typeVal.put(DbHelper.TYPE_NAME, name); 
			typeVal.put(DbHelper.TYPE_RAWNAME, StringUtils.getRawString(name, idx));
			typeVal.put(DbHelper.TYPE_SHORTNAME, StringUtils.getPinyinString(name, idx));
			typeVal.put(DbHelper.TYPE_LANG_ID, langID); 
			
			String coverFullPath = FileManager.getExtStoragePicturesDir() 
					+ String.format(AppConfig.curLocale, "/%s.jpg", name); 
			typeVal.put(DbHelper.TYPE_COVER, coverFullPath);
			byte[] photo = getPhotoBlobData(coverFullPath); 
			typeVal.put(DbHelper.TYPE_COVERDATA, photo); 
			
			rows = db.updateWithOnConflict(DbHelper.TABLE_SONGTYPE, typeVal, DbHelper.TYPE_NAME + "=?", 
					new String[]{name},SQLiteDatabase.CONFLICT_IGNORE); 
		}
		catch (Exception e) {
		}
		finally {
			SQLiteDatabase.releaseMemory(); 
		}
		return (rows != 0); 
	}
*/
	public boolean setFreeSong(String idStr, boolean isFree) {
		int rows = 0;
		try {
			ContentValues value = new ContentValues(); 
			value.put(DbHelper.SONG_FREE, isFree); 
			rows = db.updateWithOnConflict(DbHelper.TABLE_SONGS, value, DbHelper.SONG_ID+"=?", 
					new String[]{idStr},SQLiteDatabase.CONFLICT_IGNORE); 
		} catch (Exception e) {

		}
		finally {
			SQLiteDatabase.releaseMemory(); 
		}
		return (rows != 0); 
	}
	
	public boolean setFavouriteSong(String idStr, String typeABC, boolean isFavour) {
		int rows = 0; 
		try {
			ContentValues value = new ContentValues(); 
			value.put(DbHelper.SONG_FAVOUR, isFavour); 
			rows = db.updateWithOnConflict(DbHelper.TABLE_SONGS, value, DbHelper.SONG_ID+"=? and " + DbHelper.SONG_ABCTYPE +"=?", 
					new String[]{idStr, typeABC},SQLiteDatabase.CONFLICT_IGNORE); 
			if(rows == 0 && isHDDAvailable) {
				String hddPath = mContext.getDatabasePath(DbHelper.HDD_DBName + DbHelper.DBExtension).getAbsolutePath(); 
				attachNewDatabase(hddPath, HDDDBName); 
				rows = db.updateWithOnConflict(HDDDBName + "." + DbHelper.TABLE_SONGS, value, DbHelper.SONG_ID+"=?", 
						new String[]{idStr},SQLiteDatabase.CONFLICT_IGNORE); 
				detachDatabase(HDDDBName); 
			}
		} catch (Exception e) {

		}
		finally {
			SQLiteDatabase.releaseMemory(); 
		}
		return (rows != 0); 
	}
	
	public long insertSongDataFile(String name)
    {
        long fileId = 0;
        try
        {
            name = name.replace("'", "''");

            ContentValues fileVal = new ContentValues();
            fileVal.put(DbHelper.FILE_NAME, name);

            fileId = db.insertWithOnConflict(DbHelper.TABLE_FILE_DATA, null, fileVal, SQLiteDatabase.CONFLICT_IGNORE);
        }
        catch (Exception e)
        {

        }
        finally
        {
            SQLiteDatabase.releaseMemory();
        }
        return fileId;
    }
/*	
	public long insertMusician(String name, String langID)
	{
		long authorId = 0; 
		try {
			
			String tmpName = name.replace("'", "''"); 
			ArrayList<Musician>  musicians = searchMusician(tmpName, langID, SearchMode.MODE_SIGNED, 0, 1);
			if(musicians.size() > 0 ) {
				return musicians.get(0).getID();
			}
			
			ArrayList<Language> langs = searchSongLanguageID(langID); 
			LANG_INDEX idx = LANG_INDEX.VIETNAMESE; 
			if(langs.size() > 0) {
				idx = langs.get(0).getIndex(); 
			}
			
			ContentValues authorVal = new ContentValues(); 
			authorVal.put(DbHelper.MUSICIAN_NAME, name); 
			authorVal.put(DbHelper.MUSICIAN_RAWNAME, StringUtils.getRawString(name,idx));
			authorVal.put(DbHelper.MUSICIAN_SHORTNAME, StringUtils.getPinyinString(name,idx));
			authorVal.put(DbHelper.MUSICIAN_LANG_ID, langID); 
			
			String coverFullPath = FileManager.getExtStoragePicturesDir() 
										+ String.format(AppConfig.curLocale, "/%s.jpg", name); 
			authorVal.put(DbHelper.MUSICIAN_COVER, coverFullPath);
			byte[] photo = getPhotoBlobData(coverFullPath); 
			authorVal.put(DbHelper.MUSICIAN_COVERDATA, photo); 
			
//			if (FileManager.fileExistAtPath(coverFullPath)) {
//				authorVal.put(DbHelper.MUSICIAN_COVER, coverFullPath);
//			}else  {
//				authorVal.put(DbHelper.MUSICIAN_COVER, "");
//			}
			
			
			authorId = db.insertWithOnConflict(DbHelper.TABLE_MUSICIAN, null, authorVal, SQLiteDatabase.CONFLICT_IGNORE);
			if(authorId <= 0)
			{
				
			}
		}
		catch (Exception e) {
		}
		finally {
			SQLiteDatabase.releaseMemory(); 
		}
		return authorId;
	}
	public long insertSinger(String name, String langID) {
		long singerId = 0; 
		try {
			String tmpName = name.replace("'", "''"); 
			ArrayList<Singer> singers = searchSinger(tmpName, langID, SearchMode.MODE_SIGNED, 0, 1);
			if(singers.size() > 0 ) {
				return singers.get(0).getID();
			}
			
			ArrayList<Language> langs = searchSongLanguageID(langID); 
			LANG_INDEX idx = LANG_INDEX.VIETNAMESE; 
			if(langs.size() > 0) {
				idx = langs.get(0).getIndex(); 
			}
			
			ContentValues singerVal = new ContentValues(); 
			singerVal.put(DbHelper.SINGER_NAME, name); 
			singerVal.put(DbHelper.SINGER_RAWNAME, StringUtils.getRawString(name, idx));
			singerVal.put(DbHelper.SINGER_SHORTNAME, StringUtils.getPinyinString(name, idx));
			singerVal.put(DbHelper.SINGER_LANG_ID, langID); 
			
			String coverFullPath = FileManager.getExtStoragePicturesDir() 
					+ String.format(AppConfig.curLocale, "/%s.jpg", name); 
			singerVal.put(DbHelper.SINGER_COVER, coverFullPath);
			
			byte[] photo = getPhotoBlobData(coverFullPath); 
			singerVal.put(DbHelper.SINGER_COVERDATA, photo); 
			
			
//			if (FileManager.fileExistAtPath(coverFullPath)) {
//				singerVal.put(DbHelper.SINGER_COVER, coverFullPath);
//			}else  {
//				singerVal.put(DbHelper.SINGER_COVER, "");
//			}
			
			singerId = db.insertWithOnConflict(DbHelper.TABLE_SINGER, null, singerVal, SQLiteDatabase.CONFLICT_IGNORE); 
			if(singerId <= 0)
			{
				
			}
		}
		catch (Exception e) {
		}
		finally {
			SQLiteDatabase.releaseMemory(); 
		}
		return singerId; 
	}

	private byte[] getPhotoBlobData(String cover) {
		if(!FileManager.fileExistAtPath(cover)) return null; 
		FileInputStream fInputStream;
		ByteArrayBuffer baf = null; 
		try {
			fInputStream = new FileInputStream(cover);
			BufferedInputStream bis = new BufferedInputStream(fInputStream);
			baf = new ByteArrayBuffer(500);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}
			fInputStream.close(); 
			bis.close(); 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return baf.toByteArray(); 
	}

	public long insertSongType(String name, String langID) {
		long typeId = 0;
		try {
			String tmpName = name.replace("'", "''"); 
			ArrayList<SongType> types = searchSongType(tmpName, langID, SearchMode.MODE_SIGNED, 0, 1);
			if(types.size() > 0 ) {
				return types.get(0).getID();
			}
			
			ArrayList<Language> langs = searchSongLanguageID(langID); 
			LANG_INDEX idx = LANG_INDEX.VIETNAMESE; 
			if(langs.size() > 0) {
				idx = langs.get(0).getIndex(); 
			}
			
			ContentValues typeVal = new ContentValues(); 
			typeVal.put(DbHelper.TYPE_NAME, name); 
			typeVal.put(DbHelper.TYPE_RAWNAME, StringUtils.getRawString(name, idx));
			typeVal.put(DbHelper.TYPE_SHORTNAME, StringUtils.getPinyinString(name, idx));
			typeVal.put(DbHelper.TYPE_LANG_ID, langID); 
			
			String coverFullPath = FileManager.getExtStoragePicturesDir() 
					+ String.format(AppConfig.curLocale, "/%s.jpg", name); 
			typeVal.put(DbHelper.TYPE_COVER, coverFullPath);
			byte[] photo = getPhotoBlobData(coverFullPath); 
			typeVal.put(DbHelper.TYPE_COVERDATA, photo); 
			
//			if (FileManager.fileExistAtPath(coverFullPath)) {
//				typeVal.put(DbHelper.TYPE_COVER, coverFullPath);
//			}else  {
//				typeVal.put(DbHelper.TYPE_COVER, "");
//			}
			

			typeId = db.insertWithOnConflict(DbHelper.TABLE_SONGTYPE, null, typeVal, SQLiteDatabase.CONFLICT_IGNORE); 
			if (typeId <= 0) {
				
			}
		}
		catch (Exception e) {
		}
		finally {
			SQLiteDatabase.releaseMemory(); 
		}
		return typeId; 
	}
	
	public long insertSongLanguage(String name, String preferFont) {
		long typeId = 0;
		try {
			String tmpName = name.replace("'", "''"); 
			ArrayList<Language> languages = searchSongLanguage(tmpName, SearchMode.MODE_SIGNED, 0, 1); 
			if(languages.size() > 0 ) {
				return languages.get(0).getID();
			}
			
			LANG_INDEX langID = LANG_INDEX.VIETNAMESE; 
			if(name.equals(mContext.getResources().getString(R.string.lang_vni))) {
				langID = LANG_INDEX.VIETNAMESE; 
			}else if(name.equals(mContext.getResources().getString(R.string.lang_engl))) {
				langID = LANG_INDEX.ENGLISH; 
			}else if(name.equals(mContext.getResources().getString(R.string.lang_fren))) {
				langID = LANG_INDEX.FRENCH; 
			}else if(name.equals(mContext.getResources().getString(R.string.lang_chne))) {
				langID = LANG_INDEX.CHINESE; 
			}else if(name.equals(mContext.getResources().getString(R.string.lang_phil))) {
				langID = LANG_INDEX.PHILIPINESE; 
			}else if(name.equals(mContext.getResources().getString(R.string.lang_hndi))) {
				langID = LANG_INDEX.HINDI; 
			}
			
			ContentValues typeVal = new ContentValues(); 
			typeVal.put(DbHelper.LANG_NAME, name); 
			typeVal.put(DbHelper.LANG_RAWNAME, StringUtils.getRawString(name, LANG_INDEX.VIETNAMESE));
			typeVal.put(DbHelper.LANG_SHORTNAME, StringUtils.getPinyinString(name, LANG_INDEX.VIETNAMESE));
			typeVal.put(DbHelper.LANG_FONT, preferFont); 
			typeVal.put(DbHelper.LANG_INDEX, langID.ordinal()); 
			String coverFullPath = FileManager.getExtStoragePicturesDir() 
					+ String.format(AppConfig.curLocale, "/%s.jpg", name); 
			typeVal.put(DbHelper.LANG_COVER, coverFullPath);
			
			
//			if (FileManager.fileExistAtPath(coverFullPath)) {
//				typeVal.put(DbHelper.TYPE_COVER, coverFullPath);
//			}else  {
//				typeVal.put(DbHelper.TYPE_COVER, "");
//			}
			

			typeId = db.insertWithOnConflict(DbHelper.TABLE_LANGUAGES, null, typeVal, SQLiteDatabase.CONFLICT_IGNORE); 
			if (typeId <= 0) {
				
			}
		}
		catch (Exception e) {
		}
		finally {
			SQLiteDatabase.releaseMemory(); 
		}
		return typeId; 
	}

	public long insertVolUpdateInfo(UpdateInfo updateInfo)
    {
        long dataID = -1;
        try
        {
            ContentValues updateData = new ContentValues();
//            updateData.put(DbHelper.UPDATE_ID, updateInfo.getId());
            updateData.put(DbHelper.UPDATE_VOL, updateInfo.getVol());
            updateData.put(DbHelper.UPDATE_DATE, updateInfo.getDate()); 
            updateData.put(DbHelper.UPDATE_NAME, updateInfo.getName());
            updateData.put(DbHelper.UPDATE_DESC, updateInfo.getDescription());
            updateData.put(DbHelper.UPDATE_COVER, getPhotoBlobData(updateInfo.getCover()));

            dataID = db.insertWithOnConflict(DbHelper.TABLE_UPDATE_INF, null, updateData, SQLiteDatabase.CONFLICT_IGNORE);
            if (dataID <= 0)
            {
                db.updateWithOnConflict(DbHelper.TABLE_UPDATE_INF, updateData, DbHelper.UPDATE_ID + "=?", new String[] { String.valueOf(updateInfo.getId()) }, SQLiteDatabase.CONFLICT_IGNORE);
            }
        }
        catch (Exception ex)
        {

        }
        finally
        {
            SQLiteDatabase.releaseMemory();
        }
        return dataID;
    }

	public long insertSong(SongInfo songInfo) {
		long songID  = -1; 
		try {
			long langID = insertSongLanguage(songInfo.getLanguage(), songInfo.getPreferFont()); 
			
			long authorID = insertMusician(songInfo.getAuthor(), String.format("%d", langID)); 
			long singerID = insertSinger(songInfo.getSinger(), String.format("%d", langID)); 
			long typeID = insertSongType(songInfo.getSongType(), String.format("%d", langID)); 
			ContentValues value = new ContentValues(); 
			
			ArrayList<Language> langs = searchSongLanguageID(String.valueOf(langID)); 
			LANG_INDEX idx = LANG_INDEX.VIETNAMESE; 
			if(langs.size() > 0) {
				idx = langs.get(0).getIndex(); 
			}
			
			if(songInfo.getIndex5() == 0)
				songInfo.setIndex5(songInfo.getId()); 
			
			value.put(DbHelper.SONG_ID, songInfo.getId()); 
            value.put(DbHelper.SONG_ID5, songInfo.getIndex5()); 
			value.put(DbHelper.SONG_NAME, songInfo.getTitle()); 
			value.put(DbHelper.SONG_SHORTNAME, StringUtils.getPinyinString(songInfo.getTitle(), idx));
			value.put(DbHelper.SONG_RAWNAME, StringUtils.getRawString(songInfo.getTitle(), idx)); 
			value.put(DbHelper.SONG_LYRIC, songInfo.getLyric()); 
			value.put(DbHelper.SONG_MIDI, songInfo.getMidi()); 
			value.put(DbHelper.SONG_MED_SINGER, songInfo.getIsSinger()); 
			value.put(DbHelper.SONG_NEWVOL, songInfo.getNewVol()); 
			value.put(DbHelper.SONG_MUSICIAN_ID, authorID); 
			value.put(DbHelper.SONG_SINGER_ID, singerID);
			value.put(DbHelper.SONG_LANGUAGE_ID, langID); 
			value.put(DbHelper.SONG_TYPE_ID, typeID); 
			value.put(DbHelper.SONG_VIDEO_ID, songInfo.getVideoName()); 
			value.put(DbHelper.SONG_MEDIANAME, songInfo.getMediaName()); 
			value.put(DbHelper.SONG_PRIORITY, songInfo.getPriority()); 
			value.put(DbHelper.SONG_VOCAL_NAME, songInfo.getMediaVocalName()); 
			
			songID = db.insertWithOnConflict(DbHelper.TABLE_SONGS, null, value, SQLiteDatabase.CONFLICT_IGNORE);
			
			if(songID <= 0) {
				songID = db.updateWithOnConflict(DbHelper.TABLE_SONGS, value, DbHelper.SONG_ID+"=?", new String[]{String.format("%d", songInfo.getId())}, SQLiteDatabase.CONFLICT_IGNORE); 
			}
		}catch(Exception ex) {
			Log.e("", ex.getMessage()); 
		}finally {
			SQLiteDatabase.releaseMemory(); 
		}
		return songID; 
	}
	
	public long insertMediaObject(MediaObject anItem) {
		long songID  = -1; 
		try {
			ContentValues value = new ContentValues(); 
			
			value.put(DbHelper.SONG_NAME, anItem.getMediaName()); 
			value.put(DbHelper.SONG_SHORTNAME, StringUtils.getPinyinString(anItem.getMediaName(), LANG_INDEX.VIETNAMESE));
			value.put(DbHelper.SONG_RAWNAME, StringUtils.getRawString(anItem.getMediaName(), LANG_INDEX.VIETNAMESE)); 
			value.put(DbHelper.SONG_MIDI, anItem.getMediaType().ordinal() + 1); 
			value.put(DbHelper.SONG_MED_SINGER, anItem.getMediaStore().ordinal() + 1); 
			value.put(DbHelper.SONG_MEDIANAME, anItem.getMediaPath()); 
			value.put(DbHelper.SONG_PRIORITY, anItem.getPriority()); 
			value.put(DbHelper.SONG_LANGUAGE_ID, anItem.getMediaStore().ordinal() + 1); 
			
			songID = db.insertWithOnConflict(DbHelper.TABLE_SONGS, null, value, SQLiteDatabase.CONFLICT_IGNORE);
			
			if(songID <= 0) {
				songID = db.updateWithOnConflict(DbHelper.TABLE_SONGS, value, DbHelper.SONG_MEDIANAME+" like ?", new String[]{anItem.getMediaPath()}, SQLiteDatabase.CONFLICT_IGNORE); 
			}
		}catch(Exception ex) {
			Log.e("", ex.getMessage()); 
		}finally {
			SQLiteDatabase.releaseMemory(); 
		}
		return songID; 
	}
	
	public void mergeAllDataTableFromOtherDatabase(String dbSrc, String dbDest, String tableName) {
		StringBuilder sqlBuilder = new StringBuilder(); 

		sqlBuilder.append("INSERT OR REPLACE INTO ["); 
		if(!dbDest.equals("")) {
			sqlBuilder.append(dbDest + "].[" + tableName + "]");
		}else {
			sqlBuilder.append(tableName + "]");
		}
		
		sqlBuilder.append(" SELECT "); 
		sqlBuilder.append("*"); 
		sqlBuilder.append(" FROM ["); 
		
		if(!dbDest.equals("")) {
			sqlBuilder.append(dbSrc + "].[" + tableName + "]");
		}else {
			sqlBuilder.append(tableName + "]");
		}
		
//		Log.d("", sqlBuilder.toString()); 
		db.execSQL(sqlBuilder.toString()); 
	}
	
	public void mergeSongInfoDataTableFromOtherDatabase(String dbSrc, String dbDest) {
		StringBuilder sqlBuilder = new StringBuilder(); 
		String tableName = DbHelper.TABLE_SONGS; 
		StringBuilder cols = new StringBuilder(); 
		DBQueryBuilder.appendColumns(cols, new String[]{DbHelper.SONG_ID, DbHelper.SONG_ID5, DbHelper.SONG_NAME, DbHelper.SONG_SHORTNAME, 
				DbHelper.SONG_RAWNAME, DbHelper.SONG_LYRIC, DbHelper.SONG_MIDI, DbHelper.SONG_MED_SINGER, DbHelper.SONG_NEWVOL, 
				DbHelper.SONG_SINGER_ID, DbHelper.SONG_MUSICIAN_ID, DbHelper.SONG_LANGUAGE_ID, DbHelper.SONG_TYPE_ID, 
				DbHelper.SONG_VIDEO_ID, DbHelper.SONG_MEDIANAME, DbHelper.SONG_VOCAL_NAME, DbHelper.SONG_PRIORITY}); 
		
		sqlBuilder.append("INSERT OR REPLACE INTO ["); 
		if(!dbDest.equals("")) {
			sqlBuilder.append(dbDest + "].[" + tableName + "]");
		}else {
			sqlBuilder.append(tableName + "]");
		}
		
		sqlBuilder.append("(");
		sqlBuilder.append(cols.toString()); 
		sqlBuilder.append(") SELECT "); 
		sqlBuilder.append(cols.toString()); 
		
		if(!dbSrc.equals("")) {
			DBQueryBuilder.appendTables(sqlBuilder, dbSrc + "].[" + tableName); 
		}else {
			DBQueryBuilder.appendTables(sqlBuilder, tableName); 
		}
//		Log.d("", sqlBuilder.toString()); 
		db.execSQL(sqlBuilder.toString()); 
	}
	
	public void mergeMusicianDataTableFromOtherDatabase(String dbSrc, String dbDest) {
		StringBuilder sqlBuilder = new StringBuilder(); 
		String tableName = DbHelper.TABLE_MUSICIAN; 
		
		StringBuilder cols = new StringBuilder(); 
		DBQueryBuilder.appendColumns(cols, new String[]{DbHelper.MUSICIAN_ID, DbHelper.MUSICIAN_NAME, DbHelper.MUSICIAN_SHORTNAME, 
				DbHelper.MUSICIAN_RAWNAME, DbHelper.MUSICIAN_COVER, DbHelper.MUSICIAN_COVERDATA, DbHelper.MUSICIAN_LANG_ID, DbHelper.MUSICIAN_PRIORITY}); 
		
		sqlBuilder.append("INSERT OR REPLACE INTO ["); 
		if(!dbDest.equals("")) {
			sqlBuilder.append(dbDest + "].[" + tableName + "]");
		}else {
			sqlBuilder.append(tableName + "]");
		}
		
		sqlBuilder.append("(");
		sqlBuilder.append(cols.toString()); 
		sqlBuilder.append(") SELECT "); 
		sqlBuilder.append(cols.toString()); 
		
		if(!dbSrc.equals("")) {
			DBQueryBuilder.appendTables(sqlBuilder, dbSrc + "].[" + tableName); 
		}else {
			DBQueryBuilder.appendTables(sqlBuilder, tableName); 
		}
//		Log.d("", sqlBuilder.toString()); 
		db.execSQL(sqlBuilder.toString()); 
	}
	
	public void mergeSingersDataTableFromOtherDatabase(String dbSrc, String dbDest) {
		StringBuilder sqlBuilder = new StringBuilder(); 
		String tableName = DbHelper.TABLE_SINGER; 
		
		StringBuilder cols = new StringBuilder(); 
		DBQueryBuilder.appendColumns(cols, new String[]{DbHelper.SINGER_ID, DbHelper.SINGER_NAME, DbHelper.SINGER_SHORTNAME, 
				DbHelper.SINGER_RAWNAME, DbHelper.SINGER_COVER, DbHelper.SINGER_COVERDATA, DbHelper.SINGER_LANG_ID, DbHelper.SINGER_PRIORITY}); 
		
		sqlBuilder.append("INSERT OR REPLACE INTO ["); 
		if(!dbDest.equals("")) {
			sqlBuilder.append(dbDest + "].[" + tableName + "]");
		}else {
			sqlBuilder.append(tableName + "]");
		}
		
		sqlBuilder.append("(");
		sqlBuilder.append(cols.toString()); 
		sqlBuilder.append(") SELECT "); 
		sqlBuilder.append(cols.toString()); 
		
		if(!dbSrc.equals("")) {
			DBQueryBuilder.appendTables(sqlBuilder, dbSrc + "].[" + tableName); 
		}else {
			DBQueryBuilder.appendTables(sqlBuilder, tableName); 
		}
//		Log.d("", sqlBuilder.toString()); 
		db.execSQL(sqlBuilder.toString()); 
	}
	
	public void mergeSongDataTableFromOtherDatabase(String dbSrc, String dbDest) {
		StringBuilder sqlBuilder = new StringBuilder(); 
		String tableName = DbHelper.TABLE_SONG_DATA; 
		
		StringBuilder cols = new StringBuilder(); 
		DBQueryBuilder.appendColumns(cols, new String[]{DbHelper.DATA_INDEX, DbHelper.DATA_TYPE, DbHelper.DATA_NAME, 
				DbHelper.DATA_LYR, DbHelper.DATA_AUDIO}); 
		
		sqlBuilder.append("INSERT OR REPLACE INTO ["); 
		if(!dbDest.equals("")) {
			sqlBuilder.append(dbDest + "].[" + tableName + "]");
		}else {
			sqlBuilder.append(tableName + "]");
		}
		
		sqlBuilder.append("(");
		sqlBuilder.append(cols.toString()); 
		sqlBuilder.append(") SELECT "); 
		sqlBuilder.append(cols.toString()); 
		
		if(!dbSrc.equals("")) {
			DBQueryBuilder.appendTables(sqlBuilder, dbSrc + "].[" + tableName); 
		}else {
			DBQueryBuilder.appendTables(sqlBuilder, tableName); 
		}
		Log.d("", sqlBuilder.toString()); 
		db.execSQL(sqlBuilder.toString()); 
	}
	
	public void mergeSongDataTableFromOtherDatabaseWithList(String dbSrc, String dbDest, Handler uiHandler) {
		
		int songCount = 0; 
		StringBuilder sqlBuilder = new StringBuilder(); 
		String tableName = DbHelper.TABLE_SONG_DATA; 
		
		sqlBuilder.append("SELECT Count(*) FROM [" + dbSrc + "].[" + tableName +"];"); 
		Log.d("", sqlBuilder.toString()); 
//		db.execSQL(sqlBuilder.toString()); 
		
		
		Cursor resultCursor = db.query(tableName, new String[]{"Count(*)"}, null, null, null, null, null); 
		if(resultCursor.getCount() > 0){
			while(resultCursor.moveToNext()){
				songCount = resultCursor.getInt(resultCursor.getColumnIndex("Count(*)")); 
			}
		}
		
//		Log.e("", "Song count: " + songCount); 
		
		StringBuilder mainQuery = new StringBuilder(); 
		DBQueryBuilder.appendSelect(mainQuery); 
		DBQueryBuilder.appendColumns(mainQuery, new String[]{DbHelper.DATA_INDEX, DbHelper.DATA_TYPE, DbHelper.DATA_NAME, 
				DbHelper.DATA_LYR, DbHelper.DATA_AUDIO}); 
		if(!dbSrc.equals("")) {
			DBQueryBuilder.appendTables(mainQuery, dbSrc + "].[" + tableName); 
		}else {
			DBQueryBuilder.appendTables(mainQuery, tableName); 
		}
		
		int curIdx = 0; 
		ContentValues updateContent = new ContentValues(); 
		for(curIdx = 0; curIdx < songCount; curIdx+=10) {
			sqlBuilder = new StringBuilder(mainQuery.toString()); 
			DBQueryBuilder.appendLimitOffset(sqlBuilder, 10, curIdx); 			
//			Log.e("", "Sql query: " + sqlBuilder.toString()); 
			
			resultCursor = db.rawQuery(sqlBuilder.toString(), null); 
			if(resultCursor.getCount()> 0) {
				while (resultCursor.moveToNext()) {
					updateContent.clear(); 
					updateContent.put(DbHelper.DATA_INDEX, resultCursor.getInt(resultCursor.getColumnIndex(DbHelper.DATA_INDEX))); 
//					updateContent.put(DbHelper.DATA_TYPE, resultCursor.getInt(resultCursor.getColumnIndex(DbHelper.DATA_TYPE))); 
//					updateContent.put(DbHelper.DATA_NAME, resultCursor.getString(resultCursor.getColumnIndex(DbHelper.DATA_NAME))); 
					updateContent.put(DbHelper.DATA_LYR, resultCursor.getBlob(resultCursor.getColumnIndex(DbHelper.DATA_LYR))); 
					updateContent.put(DbHelper.DATA_AUDIO, resultCursor.getBlob(resultCursor.getColumnIndex(DbHelper.DATA_AUDIO))); 
					
					long row = db.insertWithOnConflict("["+dbDest+"].["+tableName+"]", null, updateContent, SQLiteDatabase.CONFLICT_IGNORE); 
					if(row <= 0){
						row = db.updateWithOnConflict("["+dbDest+"].["+tableName+"]", updateContent, DbHelper.DATA_INDEX +"=?", 
								new String[]{updateContent.getAsString(DbHelper.DATA_INDEX)},SQLiteDatabase.CONFLICT_IGNORE); 
					}
					
//					if(row <= 0) {
//						Log.e("", "Update Failed with song indx: " + updateContent.getAsString(DbHelper.DATA_INDEX)); 
//					}else {
//						Log.e("", "Update Success with song indx: " + updateContent.getAsString(DbHelper.DATA_INDEX) + ", row id: "+row); 
//					}
				
				}
			}else {
				continue; 
			}
			
//			long row = db.insertWithOnConflict("["+dbDest+"].["+tableName+"]", null, updateContent, SQLiteDatabase.CONFLICT_IGNORE); 
//			if(row <= 0){
//				Log.e("","Need Update"); 
//				row = db.updateWithOnConflict("["+dbDest+"].["+tableName+"]", updateContent, DbHelper.DATA_INDEX +"=?", 
//						new String[]{updateContent.getAsString(DbHelper.DATA_INDEX)},SQLiteDatabase.CONFLICT_IGNORE); 
//			}
//			
//			if(row <= 0) {
//				Log.e("", "Update Failed with song indx: " + updateContent.getAsString(DbHelper.DATA_INDEX)); 
//			}else {
//				Log.e("", "Update Success with song indx: " + updateContent.getAsString(DbHelper.DATA_INDEX) + ", row id: "+row); 
//			}
			
			if(uiHandler != null){
				Message uiMesg = new Message(); 
				uiMesg.what = UPDATE_PROGRESS.PROGRESS.ordinal(); 
				uiMesg.obj =  String.format("%2f", (float)curIdx / songCount);
				uiHandler.sendMessage(uiMesg); 
			}
				
		}
		if(uiHandler != null){
			Message uiMesg = new Message(); 
			uiMesg.what = UPDATE_PROGRESS.PROGRESS.ordinal(); 
			uiMesg.obj =  String.format("%2f", 1.0);; 
			uiHandler.sendMessage(uiMesg); 
		}
//		Log.e("", "Completed Query Update"); 
		
	}
	
	public void mergeSongTypesDataTableFromOtherDatabase(String dbSrc, String dbDest) {
		StringBuilder sqlBuilder = new StringBuilder(); 
		String tableName = DbHelper.TABLE_SONGTYPE; 
		
		StringBuilder cols = new StringBuilder(); 
		DBQueryBuilder.appendColumns(cols, new String[]{DbHelper.TYPE_ID, DbHelper.TYPE_NAME, DbHelper.TYPE_SHORTNAME, 
				DbHelper.TYPE_RAWNAME, DbHelper.TYPE_COVER, DbHelper.TYPE_COVERDATA, DbHelper.TYPE_LANG_ID, DbHelper.TYPE_PRIORITY}); 
		
		sqlBuilder.append("INSERT OR REPLACE INTO ["); 
		if(!dbDest.equals("")) {
			sqlBuilder.append(dbDest + "].[" + tableName + "]");
		}else {
			sqlBuilder.append(tableName + "]");
		}
		
		sqlBuilder.append("(");
		sqlBuilder.append(cols.toString()); 
		sqlBuilder.append(") SELECT "); 
		sqlBuilder.append(cols.toString()); 
		
		if(!dbSrc.equals("")) {
			DBQueryBuilder.appendTables(sqlBuilder, dbSrc + "].[" + tableName); 
		}else {
			DBQueryBuilder.appendTables(sqlBuilder, tableName); 
		}
//		Log.d("", sqlBuilder.toString()); 
		db.execSQL(sqlBuilder.toString()); 
	}
	
	public void mergeSongLanguagesTableFromOtherDatabase(String dbSrc, String dbDest) {
		StringBuilder sqlBuilder = new StringBuilder(); 
		String tableName = DbHelper.TABLE_LANGUAGES; 
		
		StringBuilder cols = new StringBuilder(); 
		DBQueryBuilder.appendColumns(cols, new String[]{DbHelper.LANG_ID, DbHelper.LANG_SORT, DbHelper.LANG_NAME, DbHelper.LANG_SHORTNAME, 
				DbHelper.LANG_RAWNAME, DbHelper.LANG_COVER, DbHelper.LANG_FONT, DbHelper.LANG_INDEX}); 
		
		sqlBuilder.append("INSERT OR IGNORE INTO ["); 
		if(!dbDest.equals("")) {
			sqlBuilder.append(dbDest + "].[" + tableName + "]");
		}else {
			sqlBuilder.append(tableName + "]");
		}
		
		sqlBuilder.append("(");
		sqlBuilder.append(cols.toString()); 
		sqlBuilder.append(") SELECT "); 
		sqlBuilder.append(cols.toString()); 
		
		if(!dbSrc.equals("")) {
			DBQueryBuilder.appendTables(sqlBuilder, dbSrc + "].[" + tableName); 
		}else {
			DBQueryBuilder.appendTables(sqlBuilder, tableName); 
		}
//		Log.d("", sqlBuilder.toString()); 
		db.execSQL(sqlBuilder.toString()); 
	}
	
	public void mergeSongUpdateInfoTableFromOtherDatabase(String dbSrc, String dbDest) {
		StringBuilder sqlBuilder = new StringBuilder(); 
		String tableName = DbHelper.TABLE_UPDATE_INF; 
		
		StringBuilder cols = new StringBuilder(); 
		DBQueryBuilder.appendColumns(cols, new String[]{DbHelper.UPDATE_VOL, DbHelper.UPDATE_DATE, DbHelper.UPDATE_NAME, DbHelper.UPDATE_DESC, DbHelper.UPDATE_COVER}); 
		
		sqlBuilder.append("INSERT OR IGNORE INTO ["); 
		if(!dbDest.equals("")) {
			sqlBuilder.append(dbDest + "].[" + tableName + "]");
		}else {
			sqlBuilder.append(tableName + "]");
		}
		
		sqlBuilder.append("(");
		sqlBuilder.append(cols.toString()); 
		sqlBuilder.append(") SELECT "); 
		sqlBuilder.append(cols.toString()); 
		
		if(!dbSrc.equals("")) {
			DBQueryBuilder.appendTables(sqlBuilder, dbSrc + "].[" + tableName); 
		}else {
			DBQueryBuilder.appendTables(sqlBuilder, tableName); 
		}
//		Log.d("", sqlBuilder.toString()); 
		db.execSQL(sqlBuilder.toString()); 
	}
*/
	
	public void truncateTable(String tableName) {
		db.execSQL("TRUNCATE TABLE " + tableName); 
	}
	public void truncateAllTable() {
		try {
//			db.beginTransaction(); 
			truncateTable(DbHelper.TABLE_SONGS); 
			truncateTable(DbHelper.TABLE_SINGER); 
			truncateTable(DbHelper.TABLE_MUSICIAN); 
		}catch (Exception e) {

		} 
		finally {
//			db.setTransactionSuccessful(); 
//			db.endTransaction(); 
		}
	}
	
	public void dropTable(String tableName) {
		db.execSQL("DROP TABLE IF EXISTS " + tableName); 
	}
	public void dropAllTable() {
		try {
//			db.beginTransaction(); 
			dropTable(DbHelper.TABLE_SONGS); 
			dropTable(DbHelper.TABLE_SINGER); 
			dropTable(DbHelper.TABLE_MUSICIAN); 
			dropTable(DbHelper.TABLE_SONGTYPE); 
			
			// KHIEM -------------
			
			dropIndexSong(DbHelper.INDEX_SONGS);
			
		}catch (Exception e) {

		} 
		finally {
//			db.setTransactionSuccessful(); 
//			db.endTransaction(); 
		}
	}
	
	public void createAllTable() {
		try {
//			db.beginTransaction(); 
			createAuthorTable(); 
			createSingerTable(); 
			createSongSingerTable(); 
			createSongMusicianTable(); 
			createSongTypeTable(); 
			createSongLanguageTable(); 
			createSongModelTable(); 
//			createVideoTable(); 
//            createSongDataFileTable(); 
			createSongTable();
            createSongUpdateInfoTable(); 
            createTableSongLyric(); 
//            createPictureTable(); 
            
         // KHIEM --------------
           createIndexSong(DbHelper.INDEX_SONGS, DbHelper.TABLE_SONGS);
            
		}
		catch (Exception e) {

		} 
		finally {
//			db.setTransactionSuccessful(); 
//			db.endTransaction(); 
		}
	}
	
	public void createMediaTableTable() {
		Map<String, String> attr = new LinkedHashMap<String, String>();
		attr.put(DbHelper.SONG_PK, "INTEGER PRIMARY KEY AUTOINCREMENT"); 
		attr.put(DbHelper.SONG_SORT, "INTEGER"); 
		attr.put(DbHelper.SONG_NAME, "CHAR(255)"); 
		attr.put(DbHelper.SONG_SHORTNAME, "CHAR(255)"); 
		attr.put(DbHelper.SONG_RAWNAME, "CHAR(255)"); 
		attr.put(DbHelper.SONG_MEDIATYPE, "INTEGER"); 
		attr.put(DbHelper.SONG_MED_SINGER, "INTEGER"); 
		attr.put(DbHelper.SONG_PRIORITY, "INTEGER NOT NULL DEFAULT '1'");
		attr.put(DbHelper.SONG_MEDIANAME, "CHAR(255)  UNIQUE  DEFAULT ' '"); 
		attr.put(DbHelper.SONG_LANGUAGE_ID, "INTEGER"); 
		
		createTable(DbHelper.TABLE_SONGS, attr); 
	}
	
	public void createSongTable() {
		Map<String, String> attr = new LinkedHashMap<String, String>();
		attr.put(DbHelper.SONG_PK, "INTEGER PRIMARY KEY AUTOINCREMENT"); 
		attr.put(DbHelper.SONG_ID, "INTEGER"); 
        attr.put(DbHelper.SONG_ID5, "INTEGER");
        attr.put(DbHelper.SONG_SO_MD, "INTEGER");
		attr.put(DbHelper.SONG_NAME, "CHAR(255)"); 
		attr.put(DbHelper.SONG_SHORTNAME, "CHAR(30)"); 
		attr.put(DbHelper.SONG_RAWNAME, "CHAR(255)");  
		attr.put(DbHelper.SONG_LYRIC, "CHAR(255)"); 
		attr.put(DbHelper.SONG_NUMWORDS, "INTEGER DEFAULT '0'"); 
		attr.put(DbHelper.SONG_FAVOUR, "BOOLEAN DEFAULT '0'"); 
//		attr.put(DbHelper.SONG_FREE, "INTEGER");  
		attr.put(DbHelper.SONG_REMIX, "BOOLEAN DEFAULT '0'"); 
		attr.put(DbHelper.SONG_MEDIATYPE, "TINYINT"); 
//		attr.put(DbHelper.SONG_MED_SINGER, "INTEGER"); 
		attr.put(DbHelper.SONG_SHOWENABLE, "BOOLEAN DEFAULT '0'"); 
		attr.put(DbHelper.SONG_ONETOUCH, "TINYINT"); 
		attr.put(DbHelper.SONG_NEWVOL, "BOOLEAN DEFAULT '0'");
		attr.put(DbHelper.SONG_ABCTYPE, "TINYINT"); 
		attr.put(DbHelper.SONG_EXTRAINFO, "INTEGER DEFAULT '0'"); 
		attr.put(DbHelper.SONG_CPRIGHT, "BOOLEAN DEFAULT '0'"); 
		attr.put(DbHelper.SONG_SORT, "INTEGER UNIQUE"); 
//		attr.put(DbHelper.SONG_SINGER_ID, "CHAR(25)"); 
//		attr.put(DbHelper.SONG_MUSICIAN_ID, "CHAR(25)"); 
		attr.put(DbHelper.SONG_LANGUAGE_ID, "INTEGER"); 
		attr.put(DbHelper.SONG_TYPE_ID, "INTEGER"); 
		
//		attr.put(DbHelper.SONG_VIDEO_ID, "INTEGER"); 
//		attr.put(DbHelper.SONG_PLAYCNT, "INTEGER NOT NULL DEFAULT '0'"); 
//        attr.put(DbHelper.SONG_PRIORITY, "INTEGER NOT NULL DEFAULT '1'");
//		attr.put(DbHelper.SONG_MEDIANAME, "CHAR(255)"); 
//        attr.put(DbHelper.SONG_VOCAL_NAME, "VARCHAR(255)");
		
		attr.put("FOREIGN KEY( " + DbHelper.SONG_LANGUAGE_ID + ")", "REFERENCES " + DbHelper.TABLE_LANGUAGES+"("+DbHelper.LANG_ID+")"); 
		attr.put("FOREIGN KEY( " + DbHelper.SONG_TYPE_ID + ")", "REFERENCES " + DbHelper.TABLE_SONGTYPE+"("+DbHelper.TYPE_ID+")"); 
		
		createTable(DbHelper.TABLE_SONGS, attr); 
	}

	public void createYoutubeTable() {
		Map<String, String> attr = new LinkedHashMap<String, String>();
		attr.put(DbHelper.SONG_PK, "INTEGER PRIMARY KEY AUTOINCREMENT"); 
		attr.put(DbHelper.SONG_ID, "INTEGER"); 
        attr.put(DbHelper.SONG_ID5, "INTEGER");
        attr.put(DbHelper.SONG_SO_MD, "INTEGER");
		attr.put(DbHelper.SONG_NAME, "CHAR(255)"); 
		attr.put(DbHelper.SONG_SHORTNAME, "CHAR(30)"); 
		attr.put(DbHelper.SONG_RAWNAME, "CHAR(255)");  
		attr.put(DbHelper.SONG_LYRIC, "CHAR(255)"); 
		attr.put(DbHelper.SONG_NUMWORDS, "INTEGER DEFAULT '0'"); 
		attr.put(DbHelper.SONG_FAVOUR, "BOOLEAN DEFAULT '0'");
		attr.put(DbHelper.SONG_REMIX, "BOOLEAN DEFAULT '0'"); 
		attr.put(DbHelper.SONG_MEDIATYPE, "TINYINT"); 
		attr.put(DbHelper.SONG_SHOWENABLE, "BOOLEAN DEFAULT '0'"); 
		attr.put(DbHelper.SONG_ONETOUCH, "TINYINT"); 
		attr.put(DbHelper.SONG_NEWVOL, "BOOLEAN DEFAULT '0'");
		attr.put(DbHelper.SONG_ABCTYPE, "TINYINT"); 
		attr.put(DbHelper.SONG_EXTRAINFO, "INTEGER DEFAULT '0'"); 
		attr.put(DbHelper.SONG_CPRIGHT, "BOOLEAN DEFAULT '0'"); 
		attr.put(DbHelper.SONG_SORT, "INTEGER UNIQUE"); 
		attr.put(DbHelper.SONG_LANGUAGE_ID, "INTEGER"); 
		attr.put(DbHelper.SONG_TYPE_ID, "INTEGER"); 
		attr.put(DbHelper.SONG_YT_PLAYLINK, "CHAR(255)");
		attr.put(DbHelper.SONG_YT_DOWNLINK, "TEXT");
		attr.put(DbHelper.SONG_SINGERNAME, "CHAR(255)");
		attr.put(DbHelper.SONG_THELOAINAME, "CHAR(255)");
		
		attr.put(DbHelper.SONG_MIDIDOWNLINK, "TEXT");
		attr.put(DbHelper.SONG_2STREAM, "BOOLEAN DEFAULT '0'"); 
		attr.put(DbHelper.SONG_VOCALSINGER, "BOOLEAN DEFAULT '0'"); 
		
		attr.put(DbHelper.SONG_YT_SB, "BOOLEAN DEFAULT '0'");
		attr.put(DbHelper.SONG_YT_SBPATH, "TEXT");
		
		attr.put("FOREIGN KEY( " + DbHelper.SONG_LANGUAGE_ID + ")", "REFERENCES " + DbHelper.TABLE_LANGUAGES+"("+DbHelper.LANG_ID+")"); 
		attr.put("FOREIGN KEY( " + DbHelper.SONG_TYPE_ID + ")", "REFERENCES " + DbHelper.TABLE_SONGTYPE+"("+DbHelper.TYPE_ID+")");
				
		createTable(DbHelper.TABLE_SONGS_YOUTUBE, attr); 
	}
	
	public void createNewSongTable() {
		Map<String, String> attr = new LinkedHashMap<String, String>();
		attr.put(DbHelper.SONG_PK_NEW, "INTEGER PRIMARY KEY AUTOINCREMENT"); 
		attr.put(DbHelper.SONG_PK, "INTEGER"); 
		attr.put(DbHelper.SONG_ID, "INTEGER"); 
        attr.put(DbHelper.SONG_ID5, "INTEGER");
        attr.put(DbHelper.SONG_SO_MD, "INTEGER");
		attr.put(DbHelper.SONG_NAME, "CHAR(255)"); 
		attr.put(DbHelper.SONG_SHORTNAME, "CHAR(30)"); 
		attr.put(DbHelper.SONG_RAWNAME, "CHAR(255)");  
		attr.put(DbHelper.SONG_LYRIC, "CHAR(255)"); 
		attr.put(DbHelper.SONG_NUMWORDS, "INTEGER DEFAULT '0'"); 
		attr.put(DbHelper.SONG_FAVOUR, "BOOLEAN DEFAULT '0'"); 
//		attr.put(DbHelper.SONG_FREE, "INTEGER");  
		attr.put(DbHelper.SONG_REMIX, "BOOLEAN DEFAULT '0'"); 
		attr.put(DbHelper.SONG_MEDIATYPE, "TINYINT"); 
//		attr.put(DbHelper.SONG_MED_SINGER, "INTEGER"); 
		attr.put(DbHelper.SONG_SHOWENABLE, "BOOLEAN DEFAULT '0'"); 
		attr.put(DbHelper.SONG_ONETOUCH, "TINYINT"); 
		attr.put(DbHelper.SONG_NEWVOL, "BOOLEAN DEFAULT '0'");
		attr.put(DbHelper.SONG_ABCTYPE, "TINYINT"); 
		attr.put(DbHelper.SONG_EXTRAINFO, "INTEGER DEFAULT '0'"); 
		attr.put(DbHelper.SONG_CPRIGHT, "BOOLEAN DEFAULT '0'"); 
		attr.put(DbHelper.SONG_SORT, "INTEGER UNIQUE"); 
//		attr.put(DbHelper.SONG_SINGER_ID, "CHAR(25)"); 
//		attr.put(DbHelper.SONG_MUSICIAN_ID, "CHAR(25)"); 
		attr.put(DbHelper.SONG_LANGUAGE_ID, "INTEGER"); 
		attr.put(DbHelper.SONG_TYPE_ID, "INTEGER"); 
		
//		attr.put(DbHelper.SONG_VIDEO_ID, "INTEGER"); 
//		attr.put(DbHelper.SONG_PLAYCNT, "INTEGER NOT NULL DEFAULT '0'"); 
//        attr.put(DbHelper.SONG_PRIORITY, "INTEGER NOT NULL DEFAULT '1'");
//		attr.put(DbHelper.SONG_MEDIANAME, "CHAR(255)"); 
//        attr.put(DbHelper.SONG_VOCAL_NAME, "VARCHAR(255)");
		
		attr.put("FOREIGN KEY( " + DbHelper.SONG_LANGUAGE_ID + ")", "REFERENCES " + DbHelper.TABLE_LANGUAGES+"("+DbHelper.LANG_ID+")"); 
		attr.put("FOREIGN KEY( " + DbHelper.SONG_TYPE_ID + ")", "REFERENCES " + DbHelper.TABLE_SONGTYPE+"("+DbHelper.TYPE_ID+")"); 
		
		createTable(DbHelper.TABLE_SONGS_NEW, attr); 
	}
	
	public void createAuthorTable() {
		Map<String, String> attr = new LinkedHashMap<String, String>();
		attr.put(DbHelper.MUSICIAN_ID, "INTEGER PRIMARY KEY AUTOINCREMENT");
		attr.put(DbHelper.MUSICIAN_NAME, "VARCHAR(255)"); 
		attr.put(DbHelper.MUSICIAN_SHORTNAME, "CHAR(30)"); 
		attr.put(DbHelper.MUSICIAN_RAWNAME, "CHAR(255)"); 
		attr.put(DbHelper.MUSICIAN_SONGCOUNT, "INTEGER"); 
		attr.put(DbHelper.MUSICIAN_SORT, "INTEGER UNIQUE");  
//		attr.put(DbHelper.MUSICIAN_PLAYCNT, "INTEGER NOT NULL DEFAULT '0'"); 
//        attr.put(DbHelper.MUSICIAN_PRIORITY, "INTEGER NOT NULL DEFAULT '1'");
//		attr.put(DbHelper.MUSICIAN_COVER, "CHAR(255)");  
		attr.put(DbHelper.MUSICIAN_COVERID, "INTEGER"); 
//		attr.put(DbHelper.MUSICIAN_COVERDATA, "BLOB"); 
		attr.put(DbHelper.MUSICIAN_LANG_ID, "INTEGER DEFAULT '0'"); 
		
		createTable(DbHelper.TABLE_MUSICIAN, attr); 
	}
	public void createSingerTable() {
		Map<String, String> attr = new LinkedHashMap<String, String>();
		attr.put(DbHelper.SINGER_ID, "INTEGER PRIMARY KEY AUTOINCREMENT"); 
		attr.put(DbHelper.SINGER_NAME, "VARCHAR(255)"); 
		attr.put(DbHelper.SINGER_SHORTNAME, "CHAR(30)"); 
		attr.put(DbHelper.SINGER_RAWNAME, "CHAR(255)"); 
		attr.put(DbHelper.SINGER_SONGCOUNT, "INTEGER"); 
		attr.put(DbHelper.SINGER_SORT, "INTEGER UNIQUE"); 
//		attr.put(DbHelper.SINGER_PLAYCNT, "INTEGER NOT NULL DEFAULT '0'"); 
//        attr.put(DbHelper.SINGER_PRIORITY, "INTEGER NOT NULL DEFAULT '1'");
//		attr.put(DbHelper.SINGER_COVER, "CHAR(255)"); 
		attr.put(DbHelper.SINGER_COVERID, "INTEGER"); 
//		attr.put(DbHelper.SINGER_COVERDATA, "BLOB"); 
		attr.put(DbHelper.SINGER_LANG_ID, "INTEGER DEFAULT '0'"); 
		
		createTable(DbHelper.TABLE_SINGER, attr); 
	}
	
	public void createSongSingerTable() {
		Map<String, String> attr = new LinkedHashMap<String, String>();
		attr.put(DbHelper.SONGSINGER_SOPK, "INTEGER"); 
		attr.put(DbHelper.SONGSINGER_ATPK, "INTEGER"); 
		attr.put("PRIMARY KEY" , "( " + DbHelper.SONGSINGER_SOPK + "," + DbHelper.SONGSINGER_ATPK+")"); 
		attr.put("FOREIGN KEY( " + DbHelper.SONGSINGER_SOPK + ")", "REFERENCES " + DbHelper.TABLE_SONGS+"("+DbHelper.SONG_PK+") ON DELETE CASCADE"); 
		attr.put("FOREIGN KEY( " + DbHelper.SONGSINGER_ATPK + ")", "REFERENCES " + DbHelper.TABLE_SINGER+"("+DbHelper.SINGER_ID+")"); 
		
		createTable(DbHelper.TABLE_SONGSINGER, attr); 
	}
	
	public void createSongMusicianTable() {
		Map<String, String> attr = new LinkedHashMap<String, String>();
		attr.put(DbHelper.SONGMUSICIAN_SOPK, "INTEGER"); 
		attr.put(DbHelper.SONGMUSICIAN_ATPK, "INTEGER"); 
		attr.put("PRIMARY KEY" , "( " + DbHelper.SONGMUSICIAN_SOPK  + "," + DbHelper.SONGMUSICIAN_ATPK+")");  
		attr.put("FOREIGN KEY( " + DbHelper.SONGMUSICIAN_SOPK + ")", "REFERENCES " + DbHelper.TABLE_SONGS+"("+DbHelper.SONG_PK+") ON DELETE CASCADE"); 
		attr.put("FOREIGN KEY( " + DbHelper.SONGMUSICIAN_ATPK + ")", "REFERENCES " + DbHelper.TABLE_MUSICIAN+"("+DbHelper.MUSICIAN_ID+")"); 
		
		createTable(DbHelper.TABLE_SONGMUSICIAN, attr); 
	}
	
	public void createSongModelTable() {
		Map<String, String> attr = new LinkedHashMap<String, String>();
		attr.put(DbHelper.SONGMODEL_SOMD, "INTEGER"); 
		attr.put(DbHelper.SONGMODEL_MODEL, "INTEGER"); 
		attr.put(DbHelper.SONGMODEL_INDEX5, "INTEGER");
		attr.put("PRIMARY KEY" , "( " + DbHelper.SONGMODEL_SOMD  + "," + DbHelper.SONGMODEL_MODEL+")");  
		
		createTable(DbHelper.TABLE_SONGMODEL, attr); 
	}
	
	public void createSongTypeTable() {
		Map<String, String> attr = new LinkedHashMap<String, String>();
		attr.put(DbHelper.TYPE_ID, "INTEGER PRIMARY KEY AUTOINCREMENT"); 
		attr.put(DbHelper.TYPE_NAME, "VARCHAR(255)"); 
		attr.put(DbHelper.TYPE_SHORTNAME, "CHAR(30)"); 
		attr.put(DbHelper.TYPE_RAWNAME, "CHAR(255)"); 
		attr.put(DbHelper.TYPE_SONGCOUNT, "INTEGER"); 
		attr.put(DbHelper.TYPE_SORT, "INTEGER UNIQUE"); 
//		attr.put(DbHelper.TYPE_PLAYCNT, "INTEGER NOT NULL DEFAULT '0'"); 
//        attr.put(DbHelper.TYPE_PRIORITY, "INTEGER NOT NULL DEFAULT '1'");
//		attr.put(DbHelper.TYPE_COVER, "CHAR(255)"); 
//		attr.put(DbHelper.TYPE_COVERID, "INTEGER"); 
		attr.put(DbHelper.TYPE_COVERDATA, "BLOB"); 
		attr.put(DbHelper.TYPE_LANG_ID, "INTEGER NOT NULL DEFAULT '0'"); 
		
		createTable(DbHelper.TABLE_SONGTYPE, attr); 
	}
	
	public void createSongLanguageTable() {
		Map<String, String> attr = new LinkedHashMap<String, String>();
		attr.put(DbHelper.LANG_ID, "INTEGER PRIMARY KEY AUTOINCREMENT"); 
		attr.put(DbHelper.LANG_SORT, "INTEGER UNIQUE"); 
		attr.put(DbHelper.LANG_NAME, "VARCHAR(255) UNIQUE"); 
		attr.put(DbHelper.LANG_SHORTNAME, "CHAR(255)"); 
		attr.put(DbHelper.LANG_RAWNAME, "CHAR(255)"); 
		attr.put(DbHelper.LANG_COVER, "CHAR(255)"); 
		attr.put(DbHelper.LANG_FONT, "CHAR(255)"); 
		attr.put(DbHelper.LANG_INDEX, "INTEGER"); 
		
		createTable(DbHelper.TABLE_LANGUAGES, attr); 
	}
	
	public void createVideoTable() {
		Map<String, String> attr = new LinkedHashMap<String, String>();
		attr.put(DbHelper.VIDEO_ID, "INTEGER PRIMARY KEY AUTOINCREMENT"); 
		attr.put(DbHelper.VIDEO_INDEX, "INTEGER UNIQUE"); 
		attr.put(DbHelper.VIDEO_DESC, "VARCHAR(255) UNIQUE"); 
		attr.put(DbHelper.VIDEO_PATH, "CHAR(255)"); 
		
		createTable(DbHelper.TABLE_VIDEOS, attr); 
	}
	

    public void createSongDataFileTable()
    {
    	Map<String, String> attr = new LinkedHashMap<String, String>();
        attr.put(DbHelper.FILE_ID, "INTEGER PRIMARY KEY AUTOINCREMENT");
        attr.put(DbHelper.FILE_NAME, "VARCHAR(255) UNIQUE");
        attr.put(DbHelper.FILE_SIZE, "INTEGER NOT NULL DEFAULT '0'");
        attr.put(DbHelper.FILE_RECORD, "INTEGER NOT NULL DEFAULT '0'");

        createTable(DbHelper.TABLE_FILE_DATA, attr);
    }

    public void createSongDataTable(String dbName)
    {
    	Map<String, String> attr = new LinkedHashMap<String, String>();
        attr.put(DbHelper.DATA_ID, "INTEGER PRIMARY KEY AUTOINCREMENT");
        attr.put(DbHelper.DATA_INDEX, "INTEGER UNIQUE NOT NULL DEFAULT '0'");
        attr.put(DbHelper.DATA_TYPE, "INTEGER NOT NULL DEFAULT '0'");
        attr.put(DbHelper.DATA_NAME, "VARCHAR(255)");
        attr.put(DbHelper.DATA_LYR, "BLOB");
        attr.put(DbHelper.DATA_AUDIO, "BLOB");

        createTable(dbName + "].[" + DbHelper.TABLE_SONG_DATA, attr);
    }
	
    public void createPictureTable()
    {
    	Map<String, String> attr = new LinkedHashMap<String, String>();
        attr.put(DbHelper.PICTURE_ID, "INTEGER UNIQUE");
        attr.put(DbHelper.PICTURE_DATA, "BLOB");

        createTable(DbHelper.TABLE_PICTURE, attr);
    }
    
	public void createSongUpdateInfoTable()
    {
		Map<String, String> attr = new LinkedHashMap<String, String>();
        attr.put(DbHelper.UPDATE_ID, "INTEGER PRIMARY KEY AUTOINCREMENT");
        attr.put(DbHelper.UPDATE_VOL, "INTEGER UNIQUE NOT NULL DEFAULT '0'");
        attr.put(DbHelper.UPDATE_DATE, "INT64 UNIQUE NOT NULL DEFAULT '0'"); 
        attr.put(DbHelper.UPDATE_NAME, "VARCHAR(255)");
        attr.put(DbHelper.UPDATE_DESC, "VARCHAR(255)");
        attr.put(DbHelper.UPDATE_COVER, "BLOB");

        createTable(DbHelper.TABLE_UPDATE_INF, attr);
    }
	
	public void createTableSongLyric()
	{
//		Map<String, String> attr = new LinkedHashMap<String, String>();
//		attr.put(DbHelper.LYRICS_ID, "INTEGER PRIMARY KEY");
//		attr.put(DbHelper.LYRICS_PINYIN, "VARCHAR");
//		attr.put(DbHelper.LYRICS_TEXT, "VARCHAR(255)"); 
//
//		createTable(DbHelper.TABLE_SONGLYRICS, attr);
		
		String sql = "CREATE VIRTUAL TABLE IF NOT EXISTS ZVLYRICS USING fts3(zid INTEGER PRIMARY UNIQUE, zpinyin TEXT, ztext TEXT, tokenize=porter);"; 
		db.execSQL(sql); 
	}
	
	public void attachNewDatabase(String path, String asName) {
		String sqlStmt = "ATTACH DATABASE '"+ path + "' AS '" + asName + "'";
//		System.out.println(sqlStmt); 
		db.execSQL(sqlStmt); 
	}
	
	public void detachDatabase(String asName) {
		String sqlStmt = "DETACH DATABASE '" + asName + "'";
//		System.out.println(sqlStmt); 
		db.execSQL(sqlStmt); 
	}
	
	private void createTable(String name, Map<String, String> attribute)
	{
		StringBuilder sqlStatement = new StringBuilder(); 
		sqlStatement.append("CREATE TABLE IF NOT EXISTS [" + name + "] "); 
		sqlStatement.append("("); 
		sqlStatement.append(getHashMapStringKeyValue(attribute)); 
		sqlStatement.append(")"); 
		
		db.execSQL(sqlStatement.toString()); 
	}
	
	private String getHashMapStringKeyValue(Map<String, String> vals)
	{
		Iterator<String> itr = vals.keySet().iterator(); 
		StringBuilder sb = new StringBuilder(); 

		while(itr.hasNext())
		{
			String key = (String)itr.next();
			String value = vals.get(key); 
			sb.append(String.format("%s %s, ", key, value)); 
		}
		String val = sb.substring(0, sb.lastIndexOf(",")); 
		return val;
	}

	public int countTotalSong(int[] langIDs, String literal, SearchMode mode, MEDIA_TYPE mediaType)
	{
		StringBuilder idString = new StringBuilder("(");
		boolean hasChinese = false; 
		for(int i = 0; i < langIDs.length; i++) {
			if(langIDs[i] == LANG_INDEX.CHINESE.ordinal()) hasChinese = true; 
			if(i < langIDs.length -1) {
				idString.append(langIDs[i] + ","); 
			}else {
				idString.append(langIDs[i]); 
			}
		}
		idString.append(")"); 
		
		String colSearch = ""; 
		if(mode == SearchMode.MODE_FULL){
			colSearch = DbHelper.SONG_RAWNAME; 
		}
		else if(mode == SearchMode.MODE_MIXED) {
			colSearch = DbHelper.SONG_SHORTNAME; 
		}
		else {
			colSearch = DbHelper.SONG_SHORTNAME; 
		}
		
		StringBuilder strBuilder = new StringBuilder(); 		
		strBuilder = new StringBuilder("select count(distinct "+ DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SORT) + ")"); 
		DBQueryBuilder.appendTables(strBuilder, DbHelper.TABLE_SONGS); 
		
		DBQueryBuilder.appendInnerJoin(strBuilder, DbHelper.TABLE_SONGMUSICIAN,  
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_PK)
				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMUSICIAN, DbHelper.SONGMUSICIAN_SOPK)); 
		DBQueryBuilder.appendInnerJoin(strBuilder, DbHelper.TABLE_MUSICIAN, 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMUSICIAN, DbHelper.SONGMUSICIAN_ATPK)
				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_MUSICIAN, DbHelper.MUSICIAN_ID)); 

		if(curTocType != TOCTYPE_SK9000) {
			DBQueryBuilder.appendInnerJoin(strBuilder, DbHelper.TABLE_SONGMODEL,  
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SO_MD)
					+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_SOMD)); 
		}
		// Filter with language
		QueryLanguageAndTypeABC(idString, strBuilder);
		
		// CPRIGHT
		if(!MyApplication.flagEverConnectHDD){
			DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K=%@)", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_CPRIGHT), 0);
		}
		
		// Song Search
		if(literal != null && !literal.equals(""))
		{
			int value = 0; 
			try {
				value = Integer.parseInt(literal); 
			} catch (Exception e) {
				value = 0; 
			}			
//			if(hasChinese && value > 0)
//			{
//				DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K LIKE %@ OR %K LIKE %@ OR (%K=%@ AND %K=%@))",
//						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, colSearch), 
//						literal + "%", 
//						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_RAWNAME), 
//						"%" + literal + "%",
//						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_NUMWORDS), 
//						value,
//						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_LANGUAGE_ID), 
//						LANG_INDEX.CHINESE.ordinal());
//			}else {
				DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K LIKE %@ OR %K LIKE %@ OR %K LIKE %@)",
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, colSearch), 
					literal + "%", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_RAWNAME), 
					"%" + literal + "%",
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_NAME), 
					"%" + literal + "%"); 
//			}
		}
		
		// Filter with mediatype
		filterWithMediaType(strBuilder, mediaType, DbHelper.TABLE_SONGS);
		
		// Song shown enable
		DBQueryBuilder.appendFromFormat(strBuilder, "AND %K=%@", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHOWENABLE),
						"0");
		
		if(curTocType != TOCTYPE_SK9000) {
			StringBuilder modelSb = new StringBuilder(); 
			modelSb.append('('); 
			modelSb.append(curTocType); 
			
			if(curHDDToc > 0) {
				modelSb.append("," + curHDDToc);
			}
			modelSb.append(')'); 
			DBQueryBuilder.appendFromFormat(strBuilder, "AND %K IN %@", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_MODEL),
					modelSb.toString());
			
			DBQueryBuilder.appendFilterVol(strBuilder);
		}
		DBQueryBuilder.appendRefreshDevice(strBuilder, MyApplication.bOffUserList);
		DBQueryBuilder.appendKM1SelectList(strBuilder, MyApplication.intSelectList);
// KHIEM		
		// MyLog.e("database", "total song");
		// MyLog.d("database", strBuilder.toString());
		Cursor cursorResult = db.rawQuery(strBuilder.toString(), null); 
		int count = 0; 
		if (cursorResult.moveToNext()) {
			count = cursorResult.getInt(0); 
		}
		
		cursorResult.close();
		
		return count; 
	}
	
	public int countTotalSong_YouTube(int[] langIDs, String literal, SearchMode mode, MEDIA_TYPE mediaType)
	{
		StringBuilder idString = new StringBuilder("(");
		boolean hasChinese = false; 
		for(int i = 0; i < langIDs.length; i++) {
			if(langIDs[i] == LANG_INDEX.CHINESE.ordinal()) hasChinese = true; 
			if(i < langIDs.length -1) {
				idString.append(langIDs[i] + ","); 
			}else {
				idString.append(langIDs[i]); 
			}
		}
		idString.append(")"); 

		String colSearch = ""; 
		if(mode == SearchMode.MODE_FULL){
			colSearch = DbHelper.SONG_RAWNAME; 
		}
		else if(mode == SearchMode.MODE_MIXED) {
			colSearch = DbHelper.SONG_SHORTNAME; 
		}
		else {
			colSearch = DbHelper.SONG_SHORTNAME; 
		}

		StringBuilder strBuilder = new StringBuilder(); 		
		strBuilder = new StringBuilder("select count(distinct "+ DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE, DbHelper.SONG_SORT) + ")"); 
		DBQueryBuilder.appendTables(strBuilder, DbHelper.TABLE_SONGS_YOUTUBE); 
				
		if(curTocType != TOCTYPE_SK9000) {
			DBQueryBuilder.appendInnerJoin(strBuilder, DbHelper.TABLE_SONGMODEL,  
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE, DbHelper.SONG_SO_MD)
					+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_SOMD)); 
		}
		
		DBQueryBuilder.appendWhereFromFormat(strBuilder, "(%K IN %@)", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE, DbHelper.SONG_ABCTYPE),
				"(0,1)");
		
		// CPRIGHT
				if(!MyApplication.flagEverConnectHDD){
					DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K=%@)", 
							DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE, DbHelper.SONG_CPRIGHT), 0);
				}
				
		// Song Search
		if(literal != null && !literal.equals(""))
		{
			int value = 0; 
			try {
				value = Integer.parseInt(literal); 
			} catch (Exception e) {
				value = 0; 
			}			
//			if(hasChinese && value > 0)
//			{
//				DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K LIKE %@ OR %K LIKE %@ OR (%K=%@ AND %K=%@))",
//						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, colSearch), 
//						literal + "%", 
//						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_RAWNAME), 
//						"%" + literal + "%",
//						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_NUMWORDS), 
//						value,
//						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_LANGUAGE_ID), 
//						LANG_INDEX.CHINESE.ordinal());
//			}else {
				DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K LIKE %@ OR %K LIKE %@)",
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE, colSearch), 
						literal + "%", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE, DbHelper.SONG_RAWNAME), 
						"%" + literal + "%"); 
//			}
		}
		
		// Filter with mediatype
		filterWithMediaType(strBuilder, mediaType, DbHelper.TABLE_SONGS_YOUTUBE);
		
		// Song shown enable
		DBQueryBuilder.appendFromFormat(strBuilder, "AND %K=%@", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE, DbHelper.SONG_SHOWENABLE),
						"0");
		
		if(curTocType != TOCTYPE_SK9000) {
			StringBuilder modelSb = new StringBuilder(); 
			modelSb.append('('); 
			modelSb.append(curTocType); 
			
			if(curHDDToc > 0) {
				modelSb.append("," + curHDDToc);
			}
			modelSb.append(')'); 
			DBQueryBuilder.appendFromFormat(strBuilder, "AND %K IN %@", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_MODEL),
					modelSb.toString());
			
			DBQueryBuilder.appendFilterVol(strBuilder);
		}
		
		//DBQueryBuilder.appendRefreshDevice_YouTube(strBuilder, MyApplication.bOffUserList);
		DBQueryBuilder.appendKM1SelectList(strBuilder, MyApplication.intSelectList);
		
//		Log.e("", "sql:\"" + strBuilder.toString() + "\""); 
		Cursor cursorResult = db.rawQuery(strBuilder.toString(), null); 

		int count = 0; 

		if (cursorResult.moveToNext()) {
			count = cursorResult.getInt(0); 
		}
				
		cursorResult.close();

		return count; 
	}
	
	public int countTotalSongFull(int[] langIDs, String literal, SearchMode mode, MEDIA_TYPE mediaType)
	{
		StringBuilder idString = new StringBuilder("(");
		boolean hasChinese = false; 
		for(int i = 0; i < langIDs.length; i++) {
			if(langIDs[i] == LANG_INDEX.CHINESE.ordinal()) hasChinese = true; 
			if(i < langIDs.length -1) {
				idString.append(langIDs[i] + ","); 
			}else {
				idString.append(langIDs[i]); 
			}
		}
		idString.append(")"); 
		
		String colSearch = ""; 
		if(mode == SearchMode.MODE_FULL){
			colSearch = DbHelper.SONG_RAWNAME; 
		}
		else if(mode == SearchMode.MODE_MIXED) {
			colSearch = DbHelper.SONG_SHORTNAME; 
		}
		else {
			colSearch = DbHelper.SONG_SHORTNAME; 
		}
		
		StringBuilder strBuilder = new StringBuilder(); 		
		strBuilder = new StringBuilder("select count(distinct "+ DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SORT) + ")"); 
		DBQueryBuilder.appendTables(strBuilder, DbHelper.TABLE_SONGS); 
		
		DBQueryBuilder.appendInnerJoin(strBuilder, DbHelper.TABLE_SONGMUSICIAN,  
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_PK)
				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMUSICIAN, DbHelper.SONGMUSICIAN_SOPK)); 
		DBQueryBuilder.appendInnerJoin(strBuilder, DbHelper.TABLE_MUSICIAN, 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMUSICIAN, DbHelper.SONGMUSICIAN_ATPK)
				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_MUSICIAN, DbHelper.MUSICIAN_ID)); 

		if(curTocType != TOCTYPE_SK9000) {
			DBQueryBuilder.appendInnerJoin(strBuilder, DbHelper.TABLE_SONGMODEL,  
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SO_MD)
					+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_SOMD)); 
		}
		// Filter with language
		DBQueryBuilder.appendWhereFromFormat(strBuilder, "(%K IN %@)", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_LANGUAGE_ID),
				idString.toString());
		
		filterWithHiddenSK90xx(strBuilder, DbHelper.TABLE_SONGS);		
		
		// CPRIGHT
		if(!MyApplication.flagEverConnectHDD){
			DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K=%@)", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_CPRIGHT), 0);
		}
		
		// Song Search
		if(literal != null && !literal.equals(""))
		{
			int value = 0; 
			try {
				value = Integer.parseInt(literal); 
			} catch (Exception e) {
				value = 0; 
			}			
//			if(hasChinese && value > 0)
//			{
//				DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K LIKE %@ OR %K LIKE %@ OR (%K=%@ AND %K=%@))",
//						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, colSearch), 
//						literal + "%", 
//						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_RAWNAME), 
//						"%" + literal + "%",
//						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_NUMWORDS), 
//						value,
//						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_LANGUAGE_ID), 
//						LANG_INDEX.CHINESE.ordinal());
//			}else {
				DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K LIKE %@ OR %K LIKE %@)",
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, colSearch), 
						literal + "%", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_RAWNAME), 
						"%" + literal + "%"); 
//			}
		}
		
		// Filter with mediatype
		filterWithMediaType(strBuilder, mediaType, DbHelper.TABLE_SONGS);
		
		// Song shown enable
		DBQueryBuilder.appendFromFormat(strBuilder, "AND %K=%@", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHOWENABLE),
						"0");
		
		if(curTocType != TOCTYPE_SK9000) {
			StringBuilder modelSb = new StringBuilder(); 
			modelSb.append('('); 
			modelSb.append(curTocType); 
			
			if(curHDDToc > 0) {
				modelSb.append("," + curHDDToc);
			}
			modelSb.append(')'); 
			DBQueryBuilder.appendFromFormat(strBuilder, "AND %K IN %@", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_MODEL),
					modelSb.toString());
			
			DBQueryBuilder.appendFilterVol(strBuilder);
		}
		DBQueryBuilder.appendRefreshDevice(strBuilder, MyApplication.bOffUserList);
		DBQueryBuilder.appendKM1SelectList(strBuilder, MyApplication.intSelectList);
// KHIEM		
		// MyLog.e("database", "total song");
		// MyLog.d("countTotalSongFull", strBuilder.toString());
		Cursor cursorResult = db.rawQuery(strBuilder.toString(), null); 
		int count = 0; 
		if (cursorResult.moveToNext()) {
			count = cursorResult.getInt(0); 
		}
		
		cursorResult.close();
		
		return count; 
	}
	
	public int countTotalSongTypeID_KM(String literal, SearchType type, MEDIA_TYPE mediaType, int typeID)
	{		
		String columnType = ""; 
		String tableName = DbHelper.TABLE_SONGS; 
		switch (type) {
			case SEARCH_MUSICIAN: 
				columnType = DbHelper.SONGMUSICIAN_ATPK; 
				tableName = DbHelper.TABLE_SONGMUSICIAN; 
				break; 
				
			case SEARCH_SINGER: 
				columnType = DbHelper.SONGSINGER_ATPK; 
				tableName = DbHelper.TABLE_SONGSINGER; 
				break; 
				
			case SEARCH_TYPE: 
				columnType = DbHelper.SONG_TYPE_ID; 
				break; 
				
			case SEARCH_LANGUAGE: 
				columnType = DbHelper.SONG_LANGUAGE_ID; 
				break; 
			
			default:
				return 0; 
		}

		StringBuilder strBuilder = new StringBuilder("select COUNT(distinct " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_PK) + ")"); 
		DBQueryBuilder.appendTables(strBuilder, DbHelper.TABLE_SONGS); 
				
		DBQueryBuilder.appendInnerJoin(strBuilder, DbHelper.TABLE_SONGSINGER,  
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_PK)
				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGSINGER, DbHelper.SONGSINGER_SOPK));
		
		DBQueryBuilder.appendInnerJoin(strBuilder, DbHelper.TABLE_SONGMUSICIAN,  
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_PK)
				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMUSICIAN, DbHelper.SONGMUSICIAN_SOPK));
		
		if(curTocType != TOCTYPE_SK9000) {
			DBQueryBuilder.appendInnerJoin(strBuilder, DbHelper.TABLE_SONGMODEL,  
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SO_MD)
					+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_SOMD)); 
		}
		
		DBQueryBuilder.appendInnerJoin(strBuilder, DbHelper.TABLE_SONGNEWVOL,  
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGNEWVOL, DbHelper.SONGNEWVOL_SOMD)
				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SO_MD));
		
		// show enable
		DBQueryBuilder.appendWhereFromFormat(strBuilder, "%K=%@", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHOWENABLE), 
				"0");
		
		DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K IN %@)", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_ABCTYPE),
				"(0,1)");
		
		// CPRIGHT
				if (!MyApplication.flagEverConnectHDD) {
					DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K=%@)",
							DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS,
									DbHelper.SONG_CPRIGHT), 0);
				}
		
		if(typeID == DbHelper.SongType_CoupleSinger && type == SearchType.SEARCH_TYPE) // The loai Song ca 
		{
			DBQueryBuilder.appendFromFormatMark(strBuilder, true, "AND (%K!=%@)", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + "&8", 
					String.valueOf(0));
		}else if(typeID == DbHelper.SongType_NewSongClub && type == SearchType.SEARCH_TYPE) {
			DBQueryBuilder.appendFromFormatMark(strBuilder, true, "AND (%K!=%@)", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + "&2", 
					String.valueOf(0));
		}else if(typeID == DbHelper.SongType_HotSong && type == SearchType.SEARCH_TYPE) {
			DBQueryBuilder.appendFromFormatMark(strBuilder, true, "AND (%K!=%@)", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + "&1", 
					String.valueOf(0));
		}else if(typeID == DbHelper.SongType_LiveSong && type == SearchType.SEARCH_TYPE) {
			DBQueryBuilder.appendFromFormatMark(strBuilder, true, "AND (%K!=%@)", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + "&8192", 
					String.valueOf(0));
		}else if(typeID == DbHelper.SongType_NewVol && type == SearchType.SEARCH_TYPE) {
//			DBQueryBuilder.appendFromFormatMark(strBuilder, true, "AND (%K=%@)", 
//					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_NEWVOL), 
//					String.valueOf(1));
		}else if(typeID == DbHelper.SongType_UpdateSong && type == SearchType.SEARCH_TYPE) {
			DBQueryBuilder.appendFromFormatMark(strBuilder, true, "AND ((%K!=%@) OR (%K!=%@)) ", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + "&4096", String.valueOf(0),
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + "&2048", String.valueOf(0));
		}else {
			// Type filter
			DBQueryBuilder.appendFromFormat(strBuilder, "AND %K=%@", 
					DBQueryBuilder.getColumnString(tableName, columnType), 
					String.valueOf(typeID));
		}
		
		// search song
		if (literal != null && literal.length() > 0 && (type == SearchType.SEARCH_TYPE || type == SearchType.SEARCH_LANGUAGE)) 
		{
//			if (type == SearchType.SEARCH_LANGUAGE && typeID == LANG_INDEX.CHINESE.ordinal()) 
//			{
//				DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K=%@)", 
//						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_NUMWORDS), 
//						Integer.valueOf(literal));
//			}else {
				DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K LIKE %@ OR %K LIKE %@)", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHORTNAME), 
						literal + "%", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_RAWNAME), 
						"%" + literal + "%"); 
//			}
		}
		
		// Filter with mediatype
		filterWithMediaType(strBuilder, mediaType, DbHelper.TABLE_SONGS);
		
		if(curTocType != TOCTYPE_SK9000) {
			StringBuilder modelSb = new StringBuilder(); 
			modelSb.append('('); 
			modelSb.append(curTocType); 
			
			if(curHDDToc > 0) {
				modelSb.append("," + curHDDToc);
			}
			modelSb.append(')'); 
			DBQueryBuilder.appendFromFormat(strBuilder, "AND %K IN %@", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGNEWVOL, DbHelper.SONGNEWVOL_MODEL),
					modelSb.toString());
			
			DBQueryBuilder.appendFilterVol(strBuilder);
		}
		
		DBQueryBuilder.appendRefreshDevice(strBuilder, MyApplication.bOffUserList);
		DBQueryBuilder.appendKM1SelectList(strBuilder, MyApplication.intSelectList);
		
		MyLog.e("sql new", strBuilder.toString()); 
		Cursor cursorResult = db.rawQuery(strBuilder.toString(), null); 
		
		int count = 0; 
		if (cursorResult.moveToNext()) {
			count = cursorResult.getInt(0); 
		}
		
		cursorResult.close();
		
		return count;
	}
	
	public int countTotalSongTypeID(String literal, SearchType type, MEDIA_TYPE mediaType, int typeID)
	{		
		String columnType = ""; 
		String tableName = DbHelper.TABLE_SONGS; 
		switch (type) {
			case SEARCH_MUSICIAN: 
				columnType = DbHelper.SONGMUSICIAN_ATPK; 
				tableName = DbHelper.TABLE_SONGMUSICIAN; 
				break; 
				
			case SEARCH_SINGER: 
				columnType = DbHelper.SONGSINGER_ATPK; 
				tableName = DbHelper.TABLE_SONGSINGER; 
				break; 
				
			case SEARCH_TYPE: 
				columnType = DbHelper.SONG_TYPE_ID; 
				break; 
				
			case SEARCH_LANGUAGE: 
				columnType = DbHelper.SONG_LANGUAGE_ID; 
				break; 
			
			default:
				return 0; 
		}

		StringBuilder strBuilder = new StringBuilder("select COUNT(distinct " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_PK) + ")"); 
		DBQueryBuilder.appendTables(strBuilder, DbHelper.TABLE_SONGS); 
		
		DBQueryBuilder.appendInnerJoin(strBuilder, DbHelper.TABLE_SONGSINGER,  
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_PK)
				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGSINGER, DbHelper.SONGSINGER_SOPK));
		
		DBQueryBuilder.appendInnerJoin(strBuilder, DbHelper.TABLE_SONGMUSICIAN,  
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_PK)
				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMUSICIAN, DbHelper.SONGMUSICIAN_SOPK));

		if(curTocType != TOCTYPE_SK9000) {
			DBQueryBuilder.appendInnerJoin(strBuilder, DbHelper.TABLE_SONGMODEL,  
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SO_MD)
					+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_SOMD)); 
		}
		
		// show enable
		DBQueryBuilder.appendWhereFromFormat(strBuilder, "%K=%@", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHOWENABLE), 
				"0"); 		

		filterWithHiddenSK90xx(strBuilder, DbHelper.TABLE_SONGS);
		
		DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K IN %@)", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_ABCTYPE),
				"(0,1)");
		// CPRIGHT
		if (!MyApplication.flagEverConnectHDD) {
			DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K=%@)",
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS,
							DbHelper.SONG_CPRIGHT), 0);
		}
		
		if(typeID == DbHelper.SongType_CoupleSinger && type == SearchType.SEARCH_TYPE) // The loai Song ca 
		{
			DBQueryBuilder.appendFromFormatMark(strBuilder, true, "AND (%K!=%@)", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + "&8", 
					String.valueOf(0));
		}else if(typeID == DbHelper.SongType_NewSongClub && type == SearchType.SEARCH_TYPE) {
			DBQueryBuilder.appendFromFormatMark(strBuilder, true, "AND (%K!=%@)", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + "&2", 
					String.valueOf(0));
		}else if(typeID == DbHelper.SongType_HotSong && type == SearchType.SEARCH_TYPE) {
			DBQueryBuilder.appendFromFormatMark(strBuilder, true, "AND (%K!=%@)", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + "&1", 
					String.valueOf(0));
		}else if(typeID == DbHelper.SongType_LiveSong && type == SearchType.SEARCH_TYPE) {
			DBQueryBuilder.appendFromFormatMark(strBuilder, true, "AND (%K!=%@)", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + "&8192", 
					String.valueOf(0));
		}else if(typeID == DbHelper.SongType_UpdateSong && type == SearchType.SEARCH_TYPE) {
			DBQueryBuilder.appendFromFormatMark(strBuilder, true, "AND ((%K!=%@) OR (%K!=%@)) ", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + "&4096", String.valueOf(0),
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + "&2048", String.valueOf(0));
		}else if(typeID == DbHelper.SongType_NewVol && type == SearchType.SEARCH_TYPE) {
			DBQueryBuilder.appendFromFormatMark(strBuilder, true, "AND (%K=%@)", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_NEWVOL), 
					String.valueOf(1));
		} else {
		// Type filter
		DBQueryBuilder.appendFromFormat(strBuilder, "AND %K=%@", 
				DBQueryBuilder.getColumnString(tableName, columnType), 
				String.valueOf(typeID));
		}
		
		// search song
		if (literal != null && literal.length() > 0 && (type == SearchType.SEARCH_TYPE || type == SearchType.SEARCH_LANGUAGE)) 
		{
//			if (type == SearchType.SEARCH_LANGUAGE && typeID == LANG_INDEX.CHINESE.ordinal()) 
//			{
//				DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K=%@)", 
//						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_NUMWORDS), 
//						Integer.valueOf(literal));
//			}else {
				DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K LIKE %@ OR %K LIKE %@)", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHORTNAME), 
						literal + "%", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_RAWNAME), 
						"%" + literal + "%"); 
//			}
		}
		
		// Filter with mediatype
		filterWithMediaType(strBuilder, mediaType, DbHelper.TABLE_SONGS);
		
		if(curTocType != TOCTYPE_SK9000) {
			StringBuilder modelSb = new StringBuilder(); 
			modelSb.append('('); 
			modelSb.append(curTocType); 
			
			if(curHDDToc > 0) {
				modelSb.append("," + curHDDToc);
			}
			modelSb.append(')'); 
			DBQueryBuilder.appendFromFormat(strBuilder, "AND %K IN %@", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_MODEL),
					modelSb.toString());
			
			DBQueryBuilder.appendFilterVol(strBuilder);
		}
// KHIEM	
		DBQueryBuilder.appendRefreshDevice(strBuilder, MyApplication.bOffUserList);
		DBQueryBuilder.appendKM1SelectList(strBuilder, MyApplication.intSelectList);
		// MyLog.e("sql main", "total type :" + type);  
		// MyLog.d("sql main", strBuilder.toString());  
		Cursor cursorResult = db.rawQuery(strBuilder.toString(), null); 
		
		int count = 0; 
		if (cursorResult.moveToNext()) {
			count = cursorResult.getInt(0); 
		}
		
		cursorResult.close();
		
		return count;
	}
	
	public Cursor getSongCursor(int[] langIDs, String literal, SearchMode mode, MEDIA_TYPE mediaType, int offset, int count)
	{
		StringBuilder idString = new StringBuilder("(");
		boolean hasChinese = false; 
		for(int i = 0; i < langIDs.length; i++) {
			if(langIDs[i] == LANG_INDEX.CHINESE.ordinal()) hasChinese = true; 
			if(i < langIDs.length -1) {
				idString.append(langIDs[i] + ","); 
			}else {
				idString.append(langIDs[i]); 
			}
		}
		idString.append(")"); 
		
		int value = 0; 
		if(hasChinese) {
			try {
				value = Integer.parseInt(literal); 
			} catch (Exception e) {
				value = 0; 
			}
		}
		
		StringBuilder strBuilder = new StringBuilder(); 
		createSongInfoQueryString(strBuilder, "", mode.ordinal()); 
		
		String colSearch = ""; 
		if(mode == SearchMode.MODE_FULL){
			colSearch = DbHelper.SONG_RAWNAME; 
		}
		else if(mode == SearchMode.MODE_MIXED) {
			colSearch = DbHelper.SONG_SHORTNAME; 
		}
		else {
			colSearch = DbHelper.SONG_SHORTNAME; 
		}

		// Filter with language
		QueryLanguageAndTypeABC(idString, strBuilder);
		
		// CPRIGHT
		if (!MyApplication.flagEverConnectHDD) {
			DBQueryBuilder.appendFromFormat(strBuilder, " AND (%K=%@)",
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS,
							DbHelper.SONG_CPRIGHT), 0);
		}
				
		// Song Search
		if(literal != null && !literal.equals(""))
		{			
//			if(hasChinese && value > 0)
//			{
//				DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K LIKE %@ OR (%K=%@ AND %K=%@))",
//						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, colSearch), 
//						literal + "%", 
//						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_NUMWORDS), 
//						value,
//						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_LANGUAGE_ID), 
//						LANG_INDEX.CHINESE.ordinal());
//			}else {
				DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K LIKE %@)",
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, colSearch), 
						literal + "%"); 
//			}
		}
		
		// Filter with mediatype
		filterWithMediaType(strBuilder, mediaType, DbHelper.TABLE_SONGS);
		
		DBQueryBuilder.appendFromFormat(strBuilder, "AND %K=%@", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHOWENABLE),
						"0");
		
		
		if(curTocType != TOCTYPE_SK9000) {
			StringBuilder modelSb = new StringBuilder(); 
			modelSb.append('('); 
			modelSb.append(curTocType); 
			
			if(curHDDToc > 0) {
				modelSb.append("," + curHDDToc);
			}
			modelSb.append(')'); 
			DBQueryBuilder.appendFromFormat(strBuilder, "AND %K IN %@", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_MODEL),
					modelSb.toString());
			
			DBQueryBuilder.appendFilterVol(strBuilder);
		}
		DBQueryBuilder.appendRefreshDevice(strBuilder, MyApplication.bOffUserList);
		DBQueryBuilder.appendKM1SelectList(strBuilder, MyApplication.intSelectList);
		DBQueryBuilder.appendGroupBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SORT));
		if(literal != null && !literal.equals("")) {
//			DBQueryBuilder.appendOrderBy(strBuilder, "CASE WHEN " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_LANGUAGE_ID) + "!=" + LANG_INDEX.CHINESE.ordinal() + " THEN 1 ELSE 2 END, length(" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHORTNAME) + ")");
			DBQueryBuilder.appendOrderBy(strBuilder, "length(" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHORTNAME) + ")" + "," + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_LANGUAGE_ID));
		}
		
		StringBuilder sqlFull = new StringBuilder(); 
		if(mode == SearchMode.MODE_MIXED && literal != null && !literal.equals(""))
		{
			createSongInfoQueryString(sqlFull, "", SearchMode.MODE_FULL.ordinal()); 
			
			// Filter with language
			QueryLanguageAndTypeABC(idString, sqlFull);
			
			// CPRIGHT
			if (!MyApplication.flagEverConnectHDD) {
				DBQueryBuilder.appendFromFormat(sqlFull, "AND (%K=%@)",
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS,
								DbHelper.SONG_CPRIGHT), 0);
			}
			
			// Song Search
			if(literal != null && !literal.equals(""))
			{		
//				if(hasChinese && value > 0)
//				{
//					DBQueryBuilder.appendFromFormat(sqlFull, "AND (%K LIKE %@ OR (%K=%@ AND %K=%@))",
//							DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_RAWNAME), 
//							"%" + literal + "%",
//							DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_NUMWORDS), 
//							value,
//							DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_LANGUAGE_ID), 
//							LANG_INDEX.CHINESE.ordinal());
//				}else {
					DBQueryBuilder.appendFromFormat(sqlFull, "AND (%K NOT LIKE %@ AND %K LIKE %@)",
							DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHORTNAME), 
							literal + "%", 
							DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_RAWNAME), 
							"%" + literal + "%"); 
//				}
			}
			
			// Filter with mediatype
			filterWithMediaType(sqlFull, mediaType, DbHelper.TABLE_SONGS);

			DBQueryBuilder.appendFromFormat(sqlFull, "AND %K=%@", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHOWENABLE),
							"0");
			
			if(curTocType != TOCTYPE_SK9000) {
				StringBuilder modelSb = new StringBuilder(); 
				modelSb.append('('); 
				modelSb.append(curTocType); 
				
				if(curHDDToc > 0) {
					modelSb.append("," + curHDDToc);
				}
				modelSb.append(')'); 
				DBQueryBuilder.appendFromFormat(sqlFull, "AND %K IN %@", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_MODEL),
						modelSb.toString());
				
				DBQueryBuilder.appendFilterVol(sqlFull);
			}
			DBQueryBuilder.appendRefreshDevice(sqlFull, MyApplication.bOffUserList);
			DBQueryBuilder.appendKM1SelectList(sqlFull, MyApplication.intSelectList);
			DBQueryBuilder.appendGroupBy(sqlFull, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SORT));
			if(literal != null && !literal.equals("")) {
//				DBQueryBuilder.appendOrderBy(strBuilder, "CASE WHEN " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_LANGUAGE_ID) + "!=" + LANG_INDEX.CHINESE.ordinal() + " THEN 1 ELSE 2 END, length(" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHORTNAME) + ")");
				DBQueryBuilder.appendOrderBy(sqlFull, "length(" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHORTNAME) + ")" + "," + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_LANGUAGE_ID));
			}
			
			String str = strBuilder.toString(); 
			strBuilder = new StringBuilder("Select * from (" + str + ") "); 
			strBuilder.append("UNION ALL Select * from (" + sqlFull.toString() + ") "); 
		}
		DBQueryBuilder.appendLimitOffset(strBuilder, count, offset); 
		// MyLog.e("database", "data song");
		// MyLog.d("database", strBuilder.toString());
		
		db.beginTransaction();
		Cursor cursor = db.rawQuery(strBuilder.toString(), null); 
		db.setTransactionSuccessful();
		db.endTransaction();
		
		return cursor;  
	}
	
	public Cursor getSongCursor_New(int[] langIDs, String literal, SearchMode mode, MEDIA_TYPE mediaType, int offset, int count)
	{
		MyLog.d(" ", " ");
		MyLog.d("database", "getSongCursor_New");
		StringBuilder idString = new StringBuilder("(");
		boolean hasChinese = false; 
		for(int i = 0; i < langIDs.length; i++) {
			if(langIDs[i] == LANG_INDEX.CHINESE.ordinal()) hasChinese = true; 
			if(i < langIDs.length -1) {
				idString.append(langIDs[i] + ","); 
			}else {
				idString.append(langIDs[i]); 
			}
		}
		idString.append(")"); 
		
		int value = 0; 
		if(hasChinese) {
			try {
				value = Integer.parseInt(literal); 
			} catch (Exception e) {
				value = 0; 
			}
		}
		
		StringBuilder strBuilder = new StringBuilder(); 
		createSongInfoQueryString_New(strBuilder, "", mode.ordinal()); 
		
		String colSearch = ""; 
		if(mode == SearchMode.MODE_FULL){
			colSearch = DbHelper.SONG_RAWNAME; 
		}
		else if(mode == SearchMode.MODE_MIXED) {
			colSearch = DbHelper.SONG_SHORTNAME; 
		}
		else {
			colSearch = DbHelper.SONG_SHORTNAME; 
		}

		// Filter with language
		QueryLanguageAndTypeABC_New(idString, strBuilder);
		
		// CPRIGHT
		if (!MyApplication.flagEverConnectHDD) {
			DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K=%@)",
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_NEW,
							DbHelper.SONG_CPRIGHT), 0);
		}
				
		// Song Search
		if(literal != null && !literal.equals(""))
		{			
//			if(hasChinese && value > 0)
//			{
//				DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K LIKE %@ OR (%K=%@ AND %K=%@))",
//						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, colSearch), 
//						literal + "%", 
//						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_NUMWORDS), 
//						value,
//						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_LANGUAGE_ID), 
//						LANG_INDEX.CHINESE.ordinal());
//			}else {
				DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K LIKE %@)",
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_NEW, colSearch), 
						literal + "%"); 
//			}
		}
		
		// Filter with mediatype
		filterWithMediaType(strBuilder, mediaType, DbHelper.TABLE_SONGS_NEW);
		
		DBQueryBuilder.appendFromFormat(strBuilder, "AND %K=%@", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_NEW, DbHelper.SONG_SHOWENABLE),
						"0");
		
		
		if(curTocType != TOCTYPE_SK9000) {
			StringBuilder modelSb = new StringBuilder(); 
			modelSb.append('('); 
			modelSb.append(curTocType); 
			
			if(curHDDToc > 0) {
				modelSb.append("," + curHDDToc);
			}
			modelSb.append(')'); 
			DBQueryBuilder.appendFromFormat(strBuilder, "AND %K IN %@", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_MODEL),
					modelSb.toString());
			
			DBQueryBuilder.appendFilterVol(strBuilder);
		}
		DBQueryBuilder.appendRefreshDevice_New(strBuilder, MyApplication.bOffUserList);
		DBQueryBuilder.appendKM1SelectList_New(strBuilder, MyApplication.intSelectList);
		DBQueryBuilder.appendGroupBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_NEW, DbHelper.SONG_PK_NEW));
		if(literal != null && !literal.equals("")) {
//			DBQueryBuilder.appendOrderBy(strBuilder, "CASE WHEN " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_LANGUAGE_ID) + "!=" + LANG_INDEX.CHINESE.ordinal() + " THEN 1 ELSE 2 END, length(" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHORTNAME) + ")");
			DBQueryBuilder.appendOrderBy(strBuilder, "length(" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_NEW, DbHelper.SONG_SHORTNAME) + ")" + "," + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_NEW, DbHelper.SONG_LANGUAGE_ID));
		}
		
		StringBuilder sqlFull = new StringBuilder(); 
		if(mode == SearchMode.MODE_MIXED && literal != null && !literal.equals(""))
		{
			createSongInfoQueryString_New(sqlFull, "", SearchMode.MODE_FULL.ordinal()); 
			
			// Filter with language
			QueryLanguageAndTypeABC_New(idString, sqlFull);
			
			// CPRIGHT
			if (!MyApplication.flagEverConnectHDD) {
				DBQueryBuilder.appendFromFormat(sqlFull, " AND (%K=%@)",
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_NEW,
								DbHelper.SONG_CPRIGHT), 0);
			}
			
			// Song Search
			if(literal != null && !literal.equals(""))
			{		
//				if(hasChinese && value > 0)
//				{
//					DBQueryBuilder.appendFromFormat(sqlFull, "AND (%K LIKE %@ OR (%K=%@ AND %K=%@))",
//							DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_RAWNAME), 
//							"%" + literal + "%",
//							DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_NUMWORDS), 
//							value,
//							DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_LANGUAGE_ID), 
//							LANG_INDEX.CHINESE.ordinal());
//				}else {
					DBQueryBuilder.appendFromFormat(sqlFull, "AND (%K NOT LIKE %@ AND %K LIKE %@)",
							DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_NEW, DbHelper.SONG_SHORTNAME), 
							literal + "%", 
							DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_NEW, DbHelper.SONG_RAWNAME), 
							"%" + literal + "%"); 
//				}
			}
			
			// Filter with mediatype
			filterWithMediaType(sqlFull, mediaType, DbHelper.TABLE_SONGS_NEW);

			DBQueryBuilder.appendFromFormat(sqlFull, "AND %K=%@", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_NEW, DbHelper.SONG_SHOWENABLE),
							"0");
			
			if(curTocType != TOCTYPE_SK9000) {
				StringBuilder modelSb = new StringBuilder(); 
				modelSb.append('('); 
				modelSb.append(curTocType); 
				
				if(curHDDToc > 0) {
					modelSb.append("," + curHDDToc);
				}
				modelSb.append(')'); 
				DBQueryBuilder.appendFromFormat(sqlFull, "AND %K IN %@", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_MODEL),
						modelSb.toString());
				
				DBQueryBuilder.appendFilterVol(sqlFull);
			}
			DBQueryBuilder.appendRefreshDevice_New(sqlFull, MyApplication.bOffUserList);
			DBQueryBuilder.appendKM1SelectList_New(sqlFull, MyApplication.intSelectList);
			DBQueryBuilder.appendGroupBy(sqlFull, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_NEW, DbHelper.SONG_PK_NEW));
			if(literal != null && !literal.equals("")) {
//				DBQueryBuilder.appendOrderBy(strBuilder, "CASE WHEN " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_LANGUAGE_ID) + "!=" + LANG_INDEX.CHINESE.ordinal() + " THEN 1 ELSE 2 END, length(" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHORTNAME) + ")");
				DBQueryBuilder.appendOrderBy(sqlFull, "length(" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_NEW, DbHelper.SONG_SHORTNAME) + ")" + "," + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_NEW, DbHelper.SONG_LANGUAGE_ID));
			}
			
			String str = strBuilder.toString(); 
			strBuilder = new StringBuilder("Select * from (" + str + ") "); 
			strBuilder.append("UNION ALL Select * from (" + sqlFull.toString() + ") "); 
		}
		DBQueryBuilder.appendLimitOffset(strBuilder, count, offset); 
		// MyLog.e("database", "data song");
//		MyLog.d("database", strBuilder.toString());
		
//		db.beginTransaction();
		Cursor cursor = db.rawQuery(strBuilder.toString(), null); 
//		db.setTransactionSuccessful();
//		db.endTransaction();
		
		return cursor; 
	}	
	
	private String getNextString(String original){
		if(original == null || original.isEmpty()){
			return "";
		}
		
		char lastChar = original.charAt(original.length() - 1);
		int ascii = (int) lastChar;
				
		int nextAscii = ascii + 1;
		
		String oldStr = original.substring(0, original.length()-1);
		String nextChar = String.valueOf(Character.toChars(nextAscii));
		
		return (oldStr + nextChar);
	}
	
	public Cursor getMySongCursor_YouTube(int[] langIDs, String literal, SearchMode mode, MEDIA_TYPE mediaType, int offset, int count)
	{
		StringBuilder idString = new StringBuilder("(");
		boolean hasChinese = false; 
		for(int i = 0; i < langIDs.length; i++) {
			if(langIDs[i] == LANG_INDEX.CHINESE.ordinal()) hasChinese = true; 
			if(i < langIDs.length -1) {
				idString.append(langIDs[i] + ","); 
			}else {
				idString.append(langIDs[i]); 
			}
		}
		idString.append(")"); 
		
		int value = 0; 
		if(hasChinese) {
			try {
				value = Integer.parseInt(literal); 
			} catch (Exception e) {
				value = 0; 
			}
		}
		
		String colSearch = ""; 
		if(mode == SearchMode.MODE_FULL){
			colSearch = DbHelper.SONG_RAWNAME; 
		}
		else if(mode == SearchMode.MODE_MIXED) {
			colSearch = DbHelper.SONG_SHORTNAME; 
		}
		else {
			colSearch = DbHelper.SONG_SHORTNAME; 
		}
		
		// ------------- SQL SEARCH PINYIN 1 WORD
		
		StringBuilder strBuilder = new StringBuilder(); 
		createSongInfoQueryString_YouTube(strBuilder, "", mode.ordinal()); 		

		StringBuilder innerStringBuilder = new StringBuilder(); 		
		innerStringBuilder.append("SELECT " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE, DbHelper.SONG_PK) + " FROM [" + DbHelper.TABLE_SONGS_YOUTUBE + "]" );
		
		if (curTocType != TOCTYPE_SK9000) {
			StringBuilder modelSb = new StringBuilder();
			modelSb.append(" INNER JOIN " + DbHelper.TABLE_SONGMODEL);
			modelSb.append(" ON " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_SOMD));
			modelSb.append(" = " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE, DbHelper.SONG_SO_MD));
			
			innerStringBuilder.append(modelSb.toString());
		}
		
		DBQueryBuilder.appendWhereFromFormat(innerStringBuilder, " %K=%@ ",
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE,
						DbHelper.SONG_NUMWORDS), 1);
		
		DBQueryBuilder.appendFromFormat(innerStringBuilder, "AND (%K IN %@)", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE, DbHelper.SONG_ABCTYPE),
				"(0,1)");
		
		// CPRIGHT
		if (!MyApplication.flagEverConnectHDD) {
			DBQueryBuilder.appendFromFormat(innerStringBuilder, " AND (%K=%@)",
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE,
							DbHelper.SONG_CPRIGHT), 0);
		}
		
		// Song Search
		if (literal != null && !literal.equals("")) {

			if(getNextString(literal).equals("")){
				DBQueryBuilder.appendFromFormat(innerStringBuilder, "AND (%K LIKE %@) ",
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE, colSearch), literal);
			} else {
				DBQueryBuilder.appendFromFormat(innerStringBuilder, "AND (%K >= %@) AND (%K < %@) ",
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE, colSearch), literal.toUpperCase(), 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE, colSearch), getNextString(literal.toUpperCase()));
			}	
		}
				
		// Filter with mediatype
		filterWithMediaType(innerStringBuilder, mediaType, DbHelper.TABLE_SONGS_YOUTUBE);

		DBQueryBuilder.appendFromFormat(innerStringBuilder, "AND %K=%@",
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE,
						DbHelper.SONG_SHOWENABLE), "0");

		if (curTocType != TOCTYPE_SK9000) {
			StringBuilder modelSb = new StringBuilder();
			modelSb.append('(');
			modelSb.append(curTocType);

			if (curHDDToc > 0) {
				modelSb.append("," + curHDDToc);
			}
			modelSb.append(')');
			DBQueryBuilder.appendFromFormat(innerStringBuilder, "AND %K IN %@",
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL,
							DbHelper.SONGMODEL_MODEL), modelSb.toString());
			
			DBQueryBuilder.appendFilterVol(innerStringBuilder);
		}
		//DBQueryBuilder.appendRefreshDevice_YouTube(innerStringBuilder, MyApplication.bOffUserList);	
		DBQueryBuilder.appendKM1SelectList(innerStringBuilder, MyApplication.intSelectList);
		
		// ADD INTO MAIN strBuilder
		strBuilder.append(" WHERE " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE, DbHelper.SONG_PK) + " IN ( "+ innerStringBuilder +" )");
						
		DBQueryBuilder.appendGroupBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE, DbHelper.SONG_SORT));
		if(literal != null && !literal.equals("")) {
			DBQueryBuilder.appendOrderBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE, DbHelper.SONG_LANGUAGE_ID));
		}
		
		// ------------- SQL SEARCH PINYIN > 2 WORD

		StringBuilder strBuilder2 = new StringBuilder(); 
		createSongInfoQueryString_YouTube(strBuilder2, "", mode.ordinal()); 		

		StringBuilder innerStringBuilder2 = new StringBuilder(); 		
		innerStringBuilder2.append("SELECT " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE, DbHelper.SONG_PK) + " FROM [" + DbHelper.TABLE_SONGS_YOUTUBE + "]" );
		
		if (curTocType != TOCTYPE_SK9000) {
			StringBuilder modelSb = new StringBuilder();
			modelSb.append(" INNER JOIN " + DbHelper.TABLE_SONGMODEL);
			modelSb.append(" ON " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_SOMD));
			modelSb.append(" = " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE, DbHelper.SONG_SO_MD));
			
			innerStringBuilder2.append(modelSb.toString());
		}
		
		DBQueryBuilder.appendWhereFromFormat(innerStringBuilder2, " %K>%@ ",
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE,
						DbHelper.SONG_NUMWORDS), 1);
		
		DBQueryBuilder.appendFromFormat(innerStringBuilder2, "AND (%K IN %@)", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE, DbHelper.SONG_ABCTYPE),
				"(0,1)");
		
		// CPRIGHT
		if (!MyApplication.flagEverConnectHDD) {
			DBQueryBuilder.appendFromFormat(innerStringBuilder2, " AND (%K=%@)",
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE,
							DbHelper.SONG_CPRIGHT), 0);
		}
		
		// Song Search
		if (literal != null && !literal.equals("")) {

			if(getNextString(literal).equals("")){
				DBQueryBuilder.appendFromFormat(innerStringBuilder2, "AND (%K LIKE %@) ",
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE, colSearch), literal);
			} else {
				DBQueryBuilder.appendFromFormat(innerStringBuilder2, "AND (%K >= %@) AND (%K < %@) ",
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE, colSearch), literal.toUpperCase(), 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE, colSearch), getNextString(literal.toUpperCase()));
			}	
		}
				
		// Filter with mediatype
		filterWithMediaType(innerStringBuilder2, mediaType, DbHelper.TABLE_SONGS_YOUTUBE);

		DBQueryBuilder.appendFromFormat(innerStringBuilder2, "AND %K=%@",
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE,
						DbHelper.SONG_SHOWENABLE), "0");

		if (curTocType != TOCTYPE_SK9000) {
			StringBuilder modelSb = new StringBuilder();
			modelSb.append('(');
			modelSb.append(curTocType);

			if (curHDDToc > 0) {
				modelSb.append("," + curHDDToc);
			}
			modelSb.append(')');
			DBQueryBuilder.appendFromFormat(innerStringBuilder2, "AND %K IN %@",
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL,
							DbHelper.SONGMODEL_MODEL), modelSb.toString());
			
			DBQueryBuilder.appendFilterVol(innerStringBuilder2);
		}
		//DBQueryBuilder.appendRefreshDevice_YouTube(innerStringBuilder2, MyApplication.bOffUserList);	
		DBQueryBuilder.appendKM1SelectList(innerStringBuilder2, MyApplication.intSelectList);
		
		// ADD INTO MAIN strBuilder
		strBuilder2.append(" WHERE " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE, DbHelper.SONG_PK) + " IN ( "+ innerStringBuilder2 +" )");
						
		DBQueryBuilder.appendGroupBy(strBuilder2, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE, DbHelper.SONG_SORT));
		if(literal != null && !literal.equals("")) {
			DBQueryBuilder.appendOrderBy(strBuilder2, "length(" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE, DbHelper.SONG_SHORTNAME) + ")" + "," + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE, DbHelper.SONG_LANGUAGE_ID));
		}
		
		String strPinyinFull = "SELECT * FROM ("+strBuilder.toString()+")";
		strPinyinFull += " UNION ALL Select * from ( " + strBuilder2.toString() + " ) ";
		
		strBuilder = new StringBuilder(strPinyinFull);		
		
		// ------------- SQL SEARCH FULL WORD
		
		StringBuilder sqlFull = new StringBuilder(); 
		if(mode == SearchMode.MODE_MIXED && literal != null && !literal.equals(""))
		{
			createSongInfoQueryString_YouTube(sqlFull, "", SearchMode.MODE_FULL.ordinal()); 
			
			StringBuilder innerStringFull = new StringBuilder();
			innerStringFull.append("SELECT " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE, DbHelper.SONG_PK) + " FROM [" + DbHelper.TABLE_SONGS_YOUTUBE + "]" );
			
			if (curTocType != TOCTYPE_SK9000) {
				StringBuilder modelSb = new StringBuilder();
				modelSb.append(" INNER JOIN " + DbHelper.TABLE_SONGMODEL);
				modelSb.append(" ON " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_SOMD));
				modelSb.append(" = " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE, DbHelper.SONG_SO_MD));
				
				innerStringFull.append(modelSb.toString());
			}

			DBQueryBuilder.appendWhereFromFormat(innerStringFull, "(%K IN %@)", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE, DbHelper.SONG_ABCTYPE),
					"(0,1)");
			
			// CPRIGHT
			if (!MyApplication.flagEverConnectHDD) {
				DBQueryBuilder.appendFromFormat(innerStringFull, "AND (%K=%@)",
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE,
								DbHelper.SONG_CPRIGHT), 0);
			}
			
			// Song Search
			if (literal != null && !literal.equals("")) {
				DBQueryBuilder.appendFromFormat(innerStringFull,
						"AND (%K NOT LIKE %@ AND %K LIKE %@)", DBQueryBuilder
								.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE,
										DbHelper.SONG_SHORTNAME),
						literal + "%", DBQueryBuilder.getColumnString(
								DbHelper.TABLE_SONGS_YOUTUBE, DbHelper.SONG_RAWNAME),
						"%" + literal + "%");
			}
			
			// Filter with mediatype
			filterWithMediaType(innerStringFull, mediaType, DbHelper.TABLE_SONGS_YOUTUBE);

			DBQueryBuilder.appendFromFormat(innerStringFull, "AND %K=%@", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE, DbHelper.SONG_SHOWENABLE),
							"0");
			
			if(curTocType != TOCTYPE_SK9000) {
				StringBuilder modelSb = new StringBuilder(); 
				modelSb.append('('); 
				modelSb.append(curTocType); 
				
				if(curHDDToc > 0) {
					modelSb.append("," + curHDDToc);
				}
				modelSb.append(')'); 
				DBQueryBuilder.appendFromFormat(innerStringFull, "AND %K IN %@", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_MODEL),
						modelSb.toString());
				
				DBQueryBuilder.appendFilterVol(innerStringFull);
			}
			//DBQueryBuilder.appendRefreshDevice_YouTube(innerStringFull, MyApplication.bOffUserList);	
			DBQueryBuilder.appendKM1SelectList(innerStringFull, MyApplication.intSelectList);
			
			// ADD INTO MAIN strBuilder
			sqlFull.append(" WHERE " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE, DbHelper.SONG_PK) + " IN ( "+ innerStringFull +" )");
						
			DBQueryBuilder.appendGroupBy(sqlFull, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE, DbHelper.SONG_SORT));
			if(literal != null && !literal.equals("")) {
				DBQueryBuilder.appendOrderBy(sqlFull, "length(" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE, DbHelper.SONG_SHORTNAME) + ")" + "," + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_YOUTUBE, DbHelper.SONG_LANGUAGE_ID));
			}
						
			String str = strBuilder.toString(); 
			strBuilder = new StringBuilder("Select * from (" + str + ") "); 
			strBuilder.append("UNION ALL Select * from (" + sqlFull.toString() + ") "); 
			
		}
		
		DBQueryBuilder.appendLimitOffset(strBuilder, count, offset); 
//		MyLog.e("getMySongCursor_YouTube", strBuilder.toString()); 
		
//		db.beginTransaction();
		Cursor cursor = db.rawQuery(strBuilder.toString(), null); 
//		db.setTransactionSuccessful();
//		db.endTransaction();
		
		return cursor; 
	}
	
	public Cursor getMySongCursor(int[] langIDs, String literal, SearchMode mode, MEDIA_TYPE mediaType, int offset, int count)
	{
		StringBuilder idString = new StringBuilder("(");
		boolean hasChinese = false; 
		for(int i = 0; i < langIDs.length; i++) {
			if(langIDs[i] == LANG_INDEX.CHINESE.ordinal()) hasChinese = true; 
			if(i < langIDs.length -1) {
				idString.append(langIDs[i] + ","); 
			}else {
				idString.append(langIDs[i]); 
			}
		}
		idString.append(")"); 
		
		int value = 0; 
		if(hasChinese) {
			try {
				value = Integer.parseInt(literal); 
			} catch (Exception e) {
				value = 0; 
			}
		}
		
		String colSearch = ""; 
		if(mode == SearchMode.MODE_FULL){
			colSearch = DbHelper.SONG_RAWNAME; 
		}
		else if(mode == SearchMode.MODE_MIXED) {
			colSearch = DbHelper.SONG_SHORTNAME; 
		}
		else {
			colSearch = DbHelper.SONG_SHORTNAME; 
		}
		
		boolean flagSpecialChar = true;
		if (literal.matches("[0-9a-zA-Z &+=_,-/]+")) {
			flagSpecialChar = false;
		}
		
		// ------------- SQL SEARCH PINYIN 1 WORD
		
		StringBuilder strBuilder = new StringBuilder(); 
		createSongInfoQueryString(strBuilder, "", mode.ordinal()); 		

		StringBuilder innerStringBuilder = new StringBuilder(); 		
		innerStringBuilder.append("SELECT " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_PK) + " FROM [" + DbHelper.TABLE_SONGS + "]" );
		
		if (curTocType != TOCTYPE_SK9000) {
			StringBuilder modelSb = new StringBuilder();
			modelSb.append(" INNER JOIN " + DbHelper.TABLE_SONGMODEL);
			modelSb.append(" ON " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_SOMD));
			modelSb.append(" = " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SO_MD));
			
			innerStringBuilder.append(modelSb.toString());
		}
		
		DBQueryBuilder.appendWhereFromFormat(innerStringBuilder, " %K=%@ ",
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS,
						DbHelper.SONG_NUMWORDS), 1);		

		filterWithHiddenSK90xx(innerStringBuilder, DbHelper.TABLE_SONGS);		
		
		// Filter with language
		DBQueryBuilder.appendFromFormat(innerStringBuilder, " AND (%K IN %@) ", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_LANGUAGE_ID),
				idString.toString());		

		DBQueryBuilder.appendFromFormat(innerStringBuilder, "AND (%K IN %@)", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_ABCTYPE),
				"(0,1)");
		
		// CPRIGHT
		if (!MyApplication.flagEverConnectHDD) {
			DBQueryBuilder.appendFromFormat(innerStringBuilder, " AND (%K=%@)",
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS,
							DbHelper.SONG_CPRIGHT), 0);
		}
		
		// Song Search
		if (literal != null && !literal.equals("")) {

			if(getNextString(literal).equals("") || flagSpecialChar){
				DBQueryBuilder.appendFromFormat(innerStringBuilder, "AND (%K LIKE %@) ",
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, colSearch), literal);
			} else {
				DBQueryBuilder.appendFromFormat(innerStringBuilder, "AND (%K >= %@) AND (%K < %@) ",
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, colSearch), literal.toUpperCase(), 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, colSearch), getNextString(literal.toUpperCase()));
			}	
		}
				
		// Filter with mediatype
		filterWithMediaType(innerStringBuilder, mediaType, DbHelper.TABLE_SONGS);

		DBQueryBuilder.appendFromFormat(innerStringBuilder, "AND %K=%@",
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS,
						DbHelper.SONG_SHOWENABLE), "0");

		if (curTocType != TOCTYPE_SK9000) {
			StringBuilder modelSb = new StringBuilder();
			modelSb.append('(');
			modelSb.append(curTocType);

			if (curHDDToc > 0) {
				modelSb.append("," + curHDDToc);
			}
			modelSb.append(')');
			DBQueryBuilder.appendFromFormat(innerStringBuilder, "AND %K IN %@",
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL,
							DbHelper.SONGMODEL_MODEL), modelSb.toString());
			
			DBQueryBuilder.appendFilterVol(innerStringBuilder);
		}
		DBQueryBuilder.appendRefreshDevice(innerStringBuilder, MyApplication.bOffUserList);	
		DBQueryBuilder.appendKM1SelectList(innerStringBuilder, MyApplication.intSelectList);
		
		// ADD INTO MAIN strBuilder
		strBuilder.append(" WHERE " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_PK) + " IN ( "+ innerStringBuilder +" )");
						
		DBQueryBuilder.appendGroupBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SORT));
		if(literal != null && !literal.equals("")) {
			DBQueryBuilder.appendOrderBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_LANGUAGE_ID));
		}
		
		// ------------- SQL SEARCH PINYIN > 2 WORD

		StringBuilder strBuilder2 = new StringBuilder(); 
		createSongInfoQueryString(strBuilder2, "", mode.ordinal()); 		

		StringBuilder innerStringBuilder2 = new StringBuilder(); 		
		innerStringBuilder2.append("SELECT " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_PK) + " FROM [" + DbHelper.TABLE_SONGS + "]" );
		
		if (curTocType != TOCTYPE_SK9000) {
			StringBuilder modelSb = new StringBuilder();
			modelSb.append(" INNER JOIN " + DbHelper.TABLE_SONGMODEL);
			modelSb.append(" ON " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_SOMD));
			modelSb.append(" = " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SO_MD));
			
			innerStringBuilder2.append(modelSb.toString());
		}
		
		DBQueryBuilder.appendWhereFromFormat(innerStringBuilder2, " %K>%@ ",
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS,
						DbHelper.SONG_NUMWORDS), 1);		

		filterWithHiddenSK90xx(innerStringBuilder2, DbHelper.TABLE_SONGS);
		
		// Filter with language
		DBQueryBuilder.appendFromFormat(innerStringBuilder2, " AND (%K IN %@) ", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_LANGUAGE_ID),
				idString.toString());		

		DBQueryBuilder.appendFromFormat(innerStringBuilder2, "AND (%K IN %@)", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_ABCTYPE),
				"(0,1)");
		
		// CPRIGHT
		if (!MyApplication.flagEverConnectHDD) {
			DBQueryBuilder.appendFromFormat(innerStringBuilder2, " AND (%K=%@)",
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS,
							DbHelper.SONG_CPRIGHT), 0);
		}
		
		// Song Search
		if (literal != null && !literal.equals("")) {

			if(getNextString(literal).equals("") || flagSpecialChar){
				DBQueryBuilder.appendFromFormat(innerStringBuilder2, "AND (%K LIKE %@) ",
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, colSearch), literal);
			} else {
				DBQueryBuilder.appendFromFormat(innerStringBuilder2, "AND (%K >= %@) AND (%K < %@) ",
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, colSearch), literal.toUpperCase(), 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, colSearch), getNextString(literal.toUpperCase()));
			}	
		}
				
		// Filter with mediatype
		filterWithMediaType(innerStringBuilder2, mediaType, DbHelper.TABLE_SONGS);

		DBQueryBuilder.appendFromFormat(innerStringBuilder2, "AND %K=%@",
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS,
						DbHelper.SONG_SHOWENABLE), "0");

		if (curTocType != TOCTYPE_SK9000) {
			StringBuilder modelSb = new StringBuilder();
			modelSb.append('(');
			modelSb.append(curTocType);

			if (curHDDToc > 0) {
				modelSb.append("," + curHDDToc);
			}
			modelSb.append(')');
			DBQueryBuilder.appendFromFormat(innerStringBuilder2, "AND %K IN %@",
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL,
							DbHelper.SONGMODEL_MODEL), modelSb.toString());
			
			DBQueryBuilder.appendFilterVol(innerStringBuilder2);
		}
		DBQueryBuilder.appendRefreshDevice(innerStringBuilder2, MyApplication.bOffUserList);	
		DBQueryBuilder.appendKM1SelectList(innerStringBuilder2, MyApplication.intSelectList);
		
		// ADD INTO MAIN strBuilder
		strBuilder2.append(" WHERE " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_PK) + " IN ( "+ innerStringBuilder2 +" )");
						
		DBQueryBuilder.appendGroupBy(strBuilder2, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SORT));
		if(literal != null && !literal.equals("")) {
			DBQueryBuilder.appendOrderBy(strBuilder2, "length(" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHORTNAME) + ")" + "," + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_LANGUAGE_ID));
		}
		
		if (literal != null && !literal.equals("") && (literal.length() == 1 || flagSpecialChar)) {
			String strPinyinFull = "SELECT * FROM ("+strBuilder.toString()+")";
			strPinyinFull += " UNION ALL Select * from ( " + strBuilder2.toString() + " ) ";
			
			strBuilder = new StringBuilder(strPinyinFull);
			
		} else {			
			strBuilder = new StringBuilder(strBuilder2);
			
		}
		
		
		// ------------- SQL SEARCH FULL WORD
		
		StringBuilder sqlFull = new StringBuilder(); 
		if(mode == SearchMode.MODE_MIXED && literal != null && !literal.equals(""))
		{
			createSongInfoQueryString(sqlFull, "", SearchMode.MODE_FULL.ordinal()); 
			
			StringBuilder innerStringFull = new StringBuilder();
			innerStringFull.append("SELECT " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_PK) + " FROM [" + DbHelper.TABLE_SONGS + "]" );
			
			if (curTocType != TOCTYPE_SK9000) {
				StringBuilder modelSb = new StringBuilder();
				modelSb.append(" INNER JOIN " + DbHelper.TABLE_SONGMODEL);
				modelSb.append(" ON " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_SOMD));
				modelSb.append(" = " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SO_MD));
				
				innerStringFull.append(modelSb.toString());
			}
			
			// Filter with language
			DBQueryBuilder.appendWhereFromFormat(innerStringFull, "(%K IN %@)", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_LANGUAGE_ID),
					idString.toString());				

			filterWithHiddenSK90xx(innerStringFull, DbHelper.TABLE_SONGS);

			DBQueryBuilder.appendFromFormat(innerStringFull, "AND (%K IN %@)", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_ABCTYPE),
					"(0,1)");
			
			// CPRIGHT
			if (!MyApplication.flagEverConnectHDD) {
				DBQueryBuilder.appendFromFormat(innerStringFull, "AND (%K=%@)",
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS,
								DbHelper.SONG_CPRIGHT), 0);
			}
			
			// Song Search
			if (literal != null && !literal.equals("")) {
				DBQueryBuilder.appendFromFormat(innerStringFull,
						"AND (%K NOT LIKE %@ AND %K LIKE %@ OR %K LIKE %@)", DBQueryBuilder
								.getColumnString(DbHelper.TABLE_SONGS,
										DbHelper.SONG_SHORTNAME),
						literal + "%", DBQueryBuilder.getColumnString(
								DbHelper.TABLE_SONGS, DbHelper.SONG_RAWNAME),
						"%" + literal + "%",DBQueryBuilder
						.getColumnString(DbHelper.TABLE_SONGS,
								DbHelper.SONG_NAME),
				"%" + literal + "%");
			}
			
			// Filter with mediatype
			filterWithMediaType(innerStringFull, mediaType, DbHelper.TABLE_SONGS);

			DBQueryBuilder.appendFromFormat(innerStringFull, "AND %K=%@", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHOWENABLE),
							"0");
			
			if(curTocType != TOCTYPE_SK9000) {
				StringBuilder modelSb = new StringBuilder(); 
				modelSb.append('('); 
				modelSb.append(curTocType); 
				
				if(curHDDToc > 0) {
					modelSb.append("," + curHDDToc);
				}
				modelSb.append(')'); 
				DBQueryBuilder.appendFromFormat(innerStringFull, "AND %K IN %@", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_MODEL),
						modelSb.toString());
				
				DBQueryBuilder.appendFilterVol(innerStringFull);
			}
			DBQueryBuilder.appendRefreshDevice(innerStringFull, MyApplication.bOffUserList);	
			DBQueryBuilder.appendKM1SelectList(innerStringFull, MyApplication.intSelectList);
			
			// ADD INTO MAIN strBuilder
			sqlFull.append(" WHERE " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_PK) + " IN ( "+ innerStringFull +" )");
						
			DBQueryBuilder.appendGroupBy(sqlFull, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SORT));
			if(literal != null && !literal.equals("")) {
				DBQueryBuilder.appendOrderBy(sqlFull, "length(" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHORTNAME) + ")" + "," + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_LANGUAGE_ID));
			}
						
			if(flagSpecialChar){
				String str = sqlFull.toString(); 
				strBuilder = new StringBuilder("Select * from (" + str + ") "); 
			} else {
				if (literal != null && !literal.equals("") && literal.length() == 1) {
					String str = strBuilder.toString(); 
					strBuilder = new StringBuilder("Select * from (" + str + ") "); 
				} else {
					String str = strBuilder.toString(); 
					strBuilder = new StringBuilder("Select * from (" + str + ") "); 
					strBuilder.append("UNION ALL Select * from (" + sqlFull.toString() + ") "); 
				}	
			}	
			
		}
		
		DBQueryBuilder.appendLimitOffset(strBuilder, count, offset); 
//		MyLog.e("sql", strBuilder.toString()); 
		
//		db.beginTransaction();
		Cursor cursor = db.rawQuery(strBuilder.toString(), null); 
//		db.setTransactionSuccessful();
//		db.endTransaction();
		
		return cursor; 
	}
	
	public Cursor getSongTypeIDCursor_KM(String literal, SearchMode mode, SearchType type, MEDIA_TYPE mediaType, int typeID, int offset, int count) {
		String columnType = ""; 
		String tableName = DbHelper.TABLE_SONGS; 
		switch (type) {
			case SEARCH_MUSICIAN: 
				columnType = DbHelper.SONGMUSICIAN_ATPK; 
				tableName = DbHelper.TABLE_SONGMUSICIAN; 
				break; 				
				
			case SEARCH_SINGER: 
				columnType = DbHelper.SONGSINGER_ATPK; 
				tableName = DbHelper.TABLE_SONGSINGER; 
				break; 
				
			case SEARCH_TYPE: 
				columnType = DbHelper.SONG_TYPE_ID; 
				break; 
				
			case SEARCH_LANGUAGE: 
				columnType = DbHelper.SONG_LANGUAGE_ID; 
				break; 
			
			default:
				return null; 
		}
		
		StringBuilder strMain = new StringBuilder(); 
		createSongInfoQueryString(strMain, "", SearchMode.MODE_PINYIN.ordinal());
		
		if(typeID == DbHelper.SongType_NewVol && type == SearchType.SEARCH_TYPE){
			if(MyApplication.intSvrModel == MyApplication.SONCA_KM1 || MyApplication.intSvrModel == MyApplication.SONCA_KM2
					|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
					|| MyApplication.intSvrModel == MyApplication.SONCA_KBX9 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI){
				DBQueryBuilder.appendInnerJoin(strMain, DbHelper.TABLE_SONGNEWVOL,  
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGNEWVOL, DbHelper.SONGNEWVOL_SOMD)
						+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SO_MD));				
			}
		}
		
		if(type == SearchType.SEARCH_SINGER){
			DBQueryBuilder.appendWhereFromFormat(strMain, "%K=%@", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHOWENABLE), 
					"0"); 			

			DBQueryBuilder.appendFromFormat(strMain, "AND (%K IN %@)", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_ABCTYPE),
					"(0,1)");
			
			// CPRIGHT
						if(!MyApplication.flagEverConnectHDD){
							DBQueryBuilder.appendFromFormat(strMain, "AND (%K=%@)", 
									DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_CPRIGHT), 0);
						}
						
			if(curTocType != TOCTYPE_SK9000) {
				StringBuilder modelSb = new StringBuilder(); 
				modelSb.append('('); 
				modelSb.append(curTocType); 
				
				if(curHDDToc > 0) {
					modelSb.append("," + curHDDToc);
				}
				modelSb.append(')'); 
				DBQueryBuilder.appendFromFormat(strMain, "AND %K IN %@", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGNEWVOL, DbHelper.SONGNEWVOL_MODEL),
						modelSb.toString());
				
				DBQueryBuilder.appendFilterVol(strMain);
			}
			
			strMain.append(" AND " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_ID) + " IN (");	
		}	

		StringBuilder strBuilder = new StringBuilder(); 
		if(type == SearchType.SEARCH_SINGER){
			createSongInfoQueryString_ID(strBuilder, "", SearchMode.MODE_PINYIN.ordinal());	
		} else {
			createSongInfoQueryString(strBuilder, "", SearchMode.MODE_PINYIN.ordinal());
		}		
		
		if(typeID == DbHelper.SongType_NewVol && type == SearchType.SEARCH_TYPE){
			if(MyApplication.intSvrModel == MyApplication.SONCA_KM1 || MyApplication.intSvrModel == MyApplication.SONCA_KM2
					|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
					|| MyApplication.intSvrModel == MyApplication.SONCA_KBX9 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI){
				DBQueryBuilder.appendInnerJoin(strBuilder, DbHelper.TABLE_SONGNEWVOL,  
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGNEWVOL, DbHelper.SONGNEWVOL_SOMD)
						+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SO_MD));				
			}
		}
		
		// show enable
		DBQueryBuilder.appendWhereFromFormat(strBuilder, "%K=%@", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHOWENABLE), 
				"0"); 
		
		DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K IN %@)", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_ABCTYPE),
				"(0,1)");
		
		// CPRIGHT
					if(!MyApplication.flagEverConnectHDD){
						DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K=%@)", 
								DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_CPRIGHT), 0);
					}

		if(typeID == DbHelper.SongType_CoupleSinger && type == SearchType.SEARCH_TYPE) // The loai Song ca 
		{
			DBQueryBuilder.appendFromFormatMark(strBuilder, true, "AND (%K!=%@)", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + "&8", 
					String.valueOf(0));
		}else if(typeID == DbHelper.SongType_NewSongClub && type == SearchType.SEARCH_TYPE) {
			DBQueryBuilder.appendFromFormatMark(strBuilder, true, "AND (%K!=%@)", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + "&2", 
					String.valueOf(0));
		}else if(typeID == DbHelper.SongType_HotSong && type == SearchType.SEARCH_TYPE) {
			DBQueryBuilder.appendFromFormatMark(strBuilder, true, "AND (%K!=%@)", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + "&1", 
					String.valueOf(0));
		}else if(typeID == DbHelper.SongType_LiveSong && type == SearchType.SEARCH_TYPE) {
			DBQueryBuilder.appendFromFormatMark(strBuilder, true, "AND (%K!=%@)", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + "&8192", 
					String.valueOf(0));
		}else if(typeID == DbHelper.SongType_UpdateSong && type == SearchType.SEARCH_TYPE) {
			DBQueryBuilder.appendFromFormatMark(strBuilder, true, "AND ((%K!=%@) OR (%K!=%@)) ", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + "&4096", String.valueOf(0),
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + "&2048", String.valueOf(0));
		}else if(typeID == DbHelper.SongType_NewVol && type == SearchType.SEARCH_TYPE) {
//			DBQueryBuilder.appendFromFormatMark(strBuilder, true, "AND (%K=%@)", 
//					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_NEWVOL), 
//					String.valueOf(1));
		}else {
			// Type filter
			DBQueryBuilder.appendFromFormat(strBuilder, "AND %K=%@", 
					DBQueryBuilder.getColumnString(tableName, columnType), 
					String.valueOf(typeID));
		}
		
		// search song
		if (literal != null && literal.length() > 0 && (type == SearchType.SEARCH_TYPE || type == SearchType.SEARCH_LANGUAGE)) 
		{
//			if (type == SearchType.SEARCH_LANGUAGE && typeID == LANG_INDEX.CHINESE.ordinal()) 
//			{
//				DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K=%@)", 
//						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_NUMWORDS), 
//						Integer.valueOf(literal));
//			}else {
				DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K LIKE %@)", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHORTNAME), 
						literal + "%"); 
//			}
		}

		// Filter with mediatype
		filterWithMediaType(strBuilder, mediaType, DbHelper.TABLE_SONGS);
		
		if(type != SearchType.SEARCH_SINGER && curTocType != TOCTYPE_SK9000) {
			StringBuilder modelSb = new StringBuilder(); 
			modelSb.append('('); 
			modelSb.append(curTocType); 
			
			if(curHDDToc > 0) {
				modelSb.append("," + curHDDToc);
			}
			modelSb.append(')'); 
			DBQueryBuilder.appendFromFormat(strBuilder, "AND %K IN %@", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGNEWVOL, DbHelper.SONGNEWVOL_MODEL),
					modelSb.toString());
		}
		if(type != SearchType.SEARCH_SINGER){
			DBQueryBuilder.appendRefreshDevice(strBuilder, MyApplication.bOffUserList);
			DBQueryBuilder.appendKM1SelectList(strBuilder, MyApplication.intSelectList);
		}
		DBQueryBuilder.appendGroupBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SORT));
		if(type == SearchType.SEARCH_TYPE || type == SearchType.SEARCH_LANGUAGE) {
			if(literal != null && !literal.equals("")) {
				DBQueryBuilder.appendOrderBy(strBuilder, "length(" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHORTNAME) + ")" + "," + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_LANGUAGE_ID));
				if(typeID == DbHelper.SongType_UpdateSong && type == SearchType.SEARCH_TYPE) {
					strBuilder.append( " , CASE WHEN (" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + " &2048 ) != 0 THEN 1 ELSE 2 END");
				}
			} else {
				if(typeID == DbHelper.SongType_UpdateSong && type == SearchType.SEARCH_TYPE) {
					DBQueryBuilder.appendOrderBy(strBuilder, "CASE WHEN (" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + " &2048 ) != 0 THEN 1 ELSE 2 END");	
				}
				
			}
		}
		
		StringBuilder sqlFull = new StringBuilder(); 
		if(mode == SearchMode.MODE_MIXED && literal != null && !literal.equals(""))
		{
			createSongInfoQueryString(sqlFull, "", SearchMode.MODE_FULL.ordinal()); 
			
			if(typeID == DbHelper.SongType_NewVol && type == SearchType.SEARCH_TYPE){
				if(MyApplication.intSvrModel == MyApplication.SONCA_KM1 || MyApplication.intSvrModel == MyApplication.SONCA_KM2
						|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
						|| MyApplication.intSvrModel == MyApplication.SONCA_KBX9 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI){
					DBQueryBuilder.appendInnerJoin(sqlFull, DbHelper.TABLE_SONGNEWVOL,  
							DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGNEWVOL, DbHelper.SONGNEWVOL_SOMD)
							+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SO_MD));				
				}
			}
			
			// show enable
			DBQueryBuilder.appendWhereFromFormat(sqlFull, "%K=%@", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHOWENABLE), 
					"0"); 			

			DBQueryBuilder.appendFromFormat(sqlFull, "AND (%K IN %@)", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_ABCTYPE),
					"(0,1)");
			
			// CPRIGHT
						if(!MyApplication.flagEverConnectHDD){
							DBQueryBuilder.appendFromFormat(sqlFull, "AND (%K=%@)", 
									DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_CPRIGHT), 0);
						}

			if(typeID == DbHelper.SongType_CoupleSinger && type == SearchType.SEARCH_TYPE) // The loai Song ca 
			{
				DBQueryBuilder.appendFromFormatMark(sqlFull, true, "AND (%K!=%@)", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + "&8", 
						String.valueOf(0));
			}else if(typeID == DbHelper.SongType_NewSongClub && type == SearchType.SEARCH_TYPE) {
				DBQueryBuilder.appendFromFormatMark(sqlFull, true, "AND (%K!=%@)", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + "&2", 
						String.valueOf(0));
			}else if(typeID == DbHelper.SongType_HotSong && type == SearchType.SEARCH_TYPE) {
				DBQueryBuilder.appendFromFormatMark(sqlFull, true, "AND (%K!=%@)", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + "&1", 
						String.valueOf(0));
			}else if(typeID == DbHelper.SongType_LiveSong && type == SearchType.SEARCH_TYPE) {
				DBQueryBuilder.appendFromFormatMark(sqlFull, true, "AND (%K!=%@)", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + "&8192", 
						String.valueOf(0));
			}else if(typeID == DbHelper.SongType_UpdateSong && type == SearchType.SEARCH_TYPE) {
				DBQueryBuilder.appendFromFormatMark(sqlFull, true, "AND ((%K!=%@) OR (%K!=%@)) ", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + "&4096", String.valueOf(0),
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + "&2048", String.valueOf(0));
			}else if(typeID == DbHelper.SongType_NewVol && type == SearchType.SEARCH_TYPE) {
//				DBQueryBuilder.appendFromFormatMark(sqlFull, true, "AND (%K=%@)", 
//						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_NEWVOL), 
//						String.valueOf(1));
			}else {
				// Type filter
				DBQueryBuilder.appendFromFormat(sqlFull, "AND %K=%@", 
						DBQueryBuilder.getColumnString(tableName, columnType), 
						String.valueOf(typeID));
			}

			// search song
			if (literal != null && literal.length() > 0 && (type == SearchType.SEARCH_TYPE || type == SearchType.SEARCH_LANGUAGE)) 
			{
				if (type == SearchType.SEARCH_LANGUAGE && typeID == LANG_INDEX.CHINESE.ordinal()) 
				{
					
				}else {
					DBQueryBuilder.appendFromFormat(sqlFull, "AND (%K NOT LIKE %@ AND %K LIKE %@)",
							DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHORTNAME), 
							literal + "%", 
							DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_RAWNAME), 
							"%" + literal + "%"); 
				}
			}

			// Filter with mediatype
			filterWithMediaType(sqlFull, mediaType, DbHelper.TABLE_SONGS);
			
			if(type != SearchType.SEARCH_SINGER && curTocType != TOCTYPE_SK9000) {
				StringBuilder modelSb = new StringBuilder(); 
				modelSb.append('('); 
				modelSb.append(curTocType); 
				
				if(curHDDToc > 0) {
					modelSb.append("," + curHDDToc);
				}
				modelSb.append(')'); 
				DBQueryBuilder.appendFromFormat(sqlFull, "AND %K IN %@", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGNEWVOL, DbHelper.SONGNEWVOL_MODEL),
						modelSb.toString());
				
				DBQueryBuilder.appendFilterVol(sqlFull);
			}
			
			DBQueryBuilder.appendRefreshDevice(sqlFull, MyApplication.bOffUserList);	
			DBQueryBuilder.appendKM1SelectList(sqlFull, MyApplication.intSelectList);
			DBQueryBuilder.appendGroupBy(sqlFull, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SORT));
			if(literal != null && !literal.equals("")) {
//				DBQueryBuilder.appendOrderBy(strBuilder, "CASE WHEN " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_LANGUAGE_ID) + "!=" + LANG_INDEX.CHINESE.ordinal() + " THEN 1 ELSE 2 END, length(" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHORTNAME) + ")");
				if(typeID == DbHelper.SongType_UpdateSong && type == SearchType.SEARCH_TYPE) {
					sqlFull.append( " , CASE WHEN (" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + " &2048 ) != 0 THEN 1 ELSE 2 END");
				}
			} else {
				if(typeID == DbHelper.SongType_UpdateSong && type == SearchType.SEARCH_TYPE) {
					DBQueryBuilder.appendOrderBy(sqlFull, "CASE WHEN (" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + " &2048 ) != 0 THEN 1 ELSE 2 END");	
				}
				
			}
			
			String str = strBuilder.toString(); 
			strBuilder = new StringBuilder("Select * from (" + str + ") "); 
			strBuilder.append("UNION ALL Select * from (" + sqlFull.toString() + ") "); 
		}
		
		
		if(type == SearchType.SEARCH_SINGER){
			strMain.append(strBuilder + ")");
			
			DBQueryBuilder.appendRefreshDevice(strMain, MyApplication.bOffUserList);	
			DBQueryBuilder.appendKM1SelectList(strMain, MyApplication.intSelectList);
			DBQueryBuilder.appendGroupBy(strMain, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SORT));
			DBQueryBuilder.appendLimitOffset(strMain, count, offset); 

//			MyLog.e("sql main", strMain.toString()); 
			return db.rawQuery(strMain.toString(), null); 
		} else {
			DBQueryBuilder.appendLimitOffset(strBuilder, count, offset);

			MyLog.d("sql builder", strBuilder.toString()); 
			return db.rawQuery(strBuilder.toString(), null); 
		}
	}
	
	public Cursor getSongTypeIDCursor(String literal, SearchMode mode, SearchType type, MEDIA_TYPE mediaType, int typeID, int offset, int count) {
		String columnType = ""; 
		String tableName = DbHelper.TABLE_SONGS; 
		switch (type) {
			case SEARCH_MUSICIAN: 
				columnType = DbHelper.SONGMUSICIAN_ATPK; 
				tableName = DbHelper.TABLE_SONGMUSICIAN; 
				break; 				
				
			case SEARCH_SINGER: 
				columnType = DbHelper.SONGSINGER_ATPK; 
				tableName = DbHelper.TABLE_SONGSINGER; 
				break; 
				
			case SEARCH_TYPE: 
				columnType = DbHelper.SONG_TYPE_ID; 
				break; 
				
			case SEARCH_LANGUAGE: 
				columnType = DbHelper.SONG_LANGUAGE_ID; 
				break; 
			
			default:
				return null; 
		}
		
		StringBuilder strMain = new StringBuilder(); 
		createSongInfoQueryString(strMain, "", SearchMode.MODE_PINYIN.ordinal());
		
		DBQueryBuilder.appendWhereFromFormat(strMain, "(%K IN %@)", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_ABCTYPE),
				"(0,1)");		

		filterWithHiddenSK90xx(strMain, DbHelper.TABLE_SONGS);		
		
		if(type == SearchType.SEARCH_SINGER){
			DBQueryBuilder.appendFromFormat(strMain, "AND (%K=%@)", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHOWENABLE), 
					"0"); 
			// CPRIGHT
			if(!MyApplication.flagEverConnectHDD){
				DBQueryBuilder.appendFromFormat(strMain, "AND (%K=%@)", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_CPRIGHT), 0);
			}
			
			if(curTocType != TOCTYPE_SK9000) {
				StringBuilder modelSb = new StringBuilder(); 
				modelSb.append('('); 
				modelSb.append(curTocType); 
				
				if(curHDDToc > 0) {
					modelSb.append("," + curHDDToc);
				}
				modelSb.append(')'); 
				DBQueryBuilder.appendFromFormat(strMain, "AND %K IN %@", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_MODEL),
						modelSb.toString());
				
				DBQueryBuilder.appendFilterVol(strMain);
			}
			
			strMain.append(" AND " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_ID) + " IN (");	
		}	

		StringBuilder strBuilder = new StringBuilder(); 
		if(type == SearchType.SEARCH_SINGER){
			createSongInfoQueryString_ID(strBuilder, "", SearchMode.MODE_PINYIN.ordinal());	
		} else {
			createSongInfoQueryString(strBuilder, "", SearchMode.MODE_PINYIN.ordinal());
		}		
		
		// show enable
		DBQueryBuilder.appendWhereFromFormat(strBuilder, "%K=%@", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHOWENABLE), 
				"0");
		
		filterWithHiddenSK90xx(strBuilder, DbHelper.TABLE_SONGS);
		
		if(type != SearchType.SEARCH_SINGER){
			DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K IN %@)", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_ABCTYPE),
					"(0,1)");
		}
		// CPRIGHT
		if (!MyApplication.flagEverConnectHDD) {
			DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K=%@)",
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS,
							DbHelper.SONG_CPRIGHT), 0);
		}

		if(typeID == DbHelper.SongType_CoupleSinger && type == SearchType.SEARCH_TYPE) // The loai Song ca 
		{
			DBQueryBuilder.appendFromFormatMark(strBuilder, true, "AND (%K!=%@)", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + "&8", 
					String.valueOf(0));
		}else if(typeID == DbHelper.SongType_NewSongClub && type == SearchType.SEARCH_TYPE) {
			DBQueryBuilder.appendFromFormatMark(strBuilder, true, "AND (%K!=%@)", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + "&2", 
					String.valueOf(0));
		}else if(typeID == DbHelper.SongType_HotSong && type == SearchType.SEARCH_TYPE) {
			DBQueryBuilder.appendFromFormatMark(strBuilder, true, "AND (%K!=%@)", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + "&1", 
					String.valueOf(0));
		}else if(typeID == DbHelper.SongType_LiveSong && type == SearchType.SEARCH_TYPE) {
			DBQueryBuilder.appendFromFormatMark(strBuilder, true, "AND (%K!=%@)", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + "&8192", 
					String.valueOf(0));
		}else if(typeID == DbHelper.SongType_UpdateSong && type == SearchType.SEARCH_TYPE) {
			DBQueryBuilder.appendFromFormatMark(strBuilder, true, "AND ((%K!=%@) OR (%K!=%@)) ", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + "&4096", String.valueOf(0),
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + "&2048", String.valueOf(0));
		}else if(typeID == DbHelper.SongType_NewVol && type == SearchType.SEARCH_TYPE) {
			DBQueryBuilder.appendFromFormatMark(strBuilder, true, "AND (%K=%@)", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_NEWVOL), 
					String.valueOf(1));
		} else {
		// Type filter
		DBQueryBuilder.appendFromFormat(strBuilder, "AND %K=%@", 
				DBQueryBuilder.getColumnString(tableName, columnType), 
				String.valueOf(typeID));
		}

		// search song
		if (literal != null && literal.length() > 0 && (type == SearchType.SEARCH_TYPE || type == SearchType.SEARCH_LANGUAGE)) 
		{
//			if (type == SearchType.SEARCH_LANGUAGE && typeID == LANG_INDEX.CHINESE.ordinal()) 
//			{
//				DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K=%@)", 
//						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_NUMWORDS), 
//						Integer.valueOf(literal));
//			}else {
				DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K LIKE %@)", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHORTNAME), 
						literal + "%"); 
//			}
		}

		// Filter with mediatype
		filterWithMediaType(strBuilder, mediaType, DbHelper.TABLE_SONGS);
		
		if(type != SearchType.SEARCH_SINGER && curTocType != TOCTYPE_SK9000) {

			if(curTocType != TOCTYPE_SK9000) {
				StringBuilder modelSb = new StringBuilder(); 
				modelSb.append('('); 
				modelSb.append(curTocType); 
				
				if(curHDDToc > 0) {
					modelSb.append("," + curHDDToc);
				}
				modelSb.append(')'); 
				DBQueryBuilder.appendFromFormat(strBuilder, "AND %K IN %@", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_MODEL),
						modelSb.toString());
				
				DBQueryBuilder.appendFilterVol(strBuilder);
			}
		}
		if(type != SearchType.SEARCH_SINGER){
			DBQueryBuilder.appendRefreshDevice(strBuilder, MyApplication.bOffUserList);
			DBQueryBuilder.appendKM1SelectList(strBuilder, MyApplication.intSelectList);
		}
		DBQueryBuilder.appendGroupBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SORT));
		if(type == SearchType.SEARCH_TYPE || type == SearchType.SEARCH_LANGUAGE) {
			if(literal != null && !literal.equals("")) {
				DBQueryBuilder.appendOrderBy(strBuilder, "length(" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHORTNAME) + ")" + "," + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_LANGUAGE_ID));
				if(typeID == DbHelper.SongType_UpdateSong && type == SearchType.SEARCH_TYPE) {
					strBuilder.append( " , CASE WHEN (" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + " &2048 ) != 0 THEN 1 ELSE 2 END");
				}
			} else {
				if(typeID == DbHelper.SongType_UpdateSong && type == SearchType.SEARCH_TYPE) {
					DBQueryBuilder.appendOrderBy(strBuilder, "CASE WHEN (" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + " &2048 ) != 0 THEN 1 ELSE 2 END");	
				}
				
			}
		}
		
		StringBuilder sqlFull = new StringBuilder(); 
		if(mode == SearchMode.MODE_MIXED && literal != null && !literal.equals(""))
		{
			createSongInfoQueryString(sqlFull, "", SearchMode.MODE_FULL.ordinal()); 
			
			// show enable
			DBQueryBuilder.appendWhereFromFormat(sqlFull, "%K=%@", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHOWENABLE), 
					"0"); 
			
			filterWithHiddenSK90xx(sqlFull, DbHelper.TABLE_SONGS);
			
			DBQueryBuilder.appendFromFormat(sqlFull, "AND (%K IN %@)", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_ABCTYPE),
					"(0,1)");
			// CPRIGHT
			if(!MyApplication.flagEverConnectHDD){
				DBQueryBuilder.appendFromFormat(sqlFull, "AND (%K=%@)", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_CPRIGHT), 0);
			}

			if(typeID == DbHelper.SongType_CoupleSinger && type == SearchType.SEARCH_TYPE) // The loai Song ca 
			{
				DBQueryBuilder.appendFromFormatMark(sqlFull, true, "AND (%K!=%@)", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + "&8", 
						String.valueOf(0));
			}else if(typeID == DbHelper.SongType_NewSongClub && type == SearchType.SEARCH_TYPE) {
				DBQueryBuilder.appendFromFormatMark(sqlFull, true, "AND (%K!=%@)", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + "&2", 
						String.valueOf(0));
			}else if(typeID == DbHelper.SongType_HotSong && type == SearchType.SEARCH_TYPE) {
				DBQueryBuilder.appendFromFormatMark(sqlFull, true, "AND (%K!=%@)", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + "&1", 
						String.valueOf(0));
			}else if(typeID == DbHelper.SongType_LiveSong && type == SearchType.SEARCH_TYPE) {
				DBQueryBuilder.appendFromFormatMark(sqlFull, true, "AND (%K!=%@)", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + "&8192", 
						String.valueOf(0));
			}else if(typeID == DbHelper.SongType_UpdateSong && type == SearchType.SEARCH_TYPE) {
				DBQueryBuilder.appendFromFormatMark(sqlFull, true, "AND ((%K!=%@) OR (%K!=%@)) ", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + "&4096", String.valueOf(0),
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + "&2048", String.valueOf(0));
			}else if(typeID == DbHelper.SongType_NewVol && type == SearchType.SEARCH_TYPE) {
				DBQueryBuilder.appendFromFormatMark(sqlFull, true, "AND (%K=%@)", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_NEWVOL), 
						String.valueOf(1));
			} else {
			// Type filter
				DBQueryBuilder.appendFromFormat(sqlFull, "AND %K=%@", 
						DBQueryBuilder.getColumnString(tableName, columnType), 
						String.valueOf(typeID));
			}

			// search song
			if (literal != null && literal.length() > 0 && (type == SearchType.SEARCH_TYPE || type == SearchType.SEARCH_LANGUAGE)) 
			{
				if (type == SearchType.SEARCH_LANGUAGE && typeID == LANG_INDEX.CHINESE.ordinal()) 
				{
					
				}else {
					DBQueryBuilder.appendFromFormat(sqlFull, "AND (%K NOT LIKE %@ AND %K LIKE %@)",
							DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHORTNAME), 
							literal + "%", 
							DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_RAWNAME), 
							"%" + literal + "%"); 
				}
			}

			// Filter with mediatype
			filterWithMediaType(sqlFull, mediaType, DbHelper.TABLE_SONGS);
			
			if(type != SearchType.SEARCH_SINGER && curTocType != TOCTYPE_SK9000) {

				if(curTocType != TOCTYPE_SK9000) {
					StringBuilder modelSb = new StringBuilder(); 
					modelSb.append('('); 
					modelSb.append(curTocType); 
					
					if(curHDDToc > 0) {
						modelSb.append("," + curHDDToc);
					}
					modelSb.append(')'); 
					DBQueryBuilder.appendFromFormat(sqlFull, "AND %K IN %@", 
							DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_MODEL),
							modelSb.toString());
					
					DBQueryBuilder.appendFilterVol(sqlFull);
				}
			}
			
			DBQueryBuilder.appendRefreshDevice(sqlFull, MyApplication.bOffUserList);
			DBQueryBuilder.appendKM1SelectList(sqlFull, MyApplication.intSelectList);
			DBQueryBuilder.appendGroupBy(sqlFull, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SORT));
			if(literal != null && !literal.equals("")) {
//				DBQueryBuilder.appendOrderBy(strBuilder, "CASE WHEN " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_LANGUAGE_ID) + "!=" + LANG_INDEX.CHINESE.ordinal() + " THEN 1 ELSE 2 END, length(" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHORTNAME) + ")");
				DBQueryBuilder.appendOrderBy(sqlFull, "length(" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHORTNAME) + ")" + "," + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_LANGUAGE_ID));
				if(typeID == DbHelper.SongType_UpdateSong && type == SearchType.SEARCH_TYPE) {
					sqlFull.append( " , CASE WHEN (" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + " &2048 ) != 0 THEN 1 ELSE 2 END");
				}
			} else {
				if(typeID == DbHelper.SongType_UpdateSong && type == SearchType.SEARCH_TYPE) {
					DBQueryBuilder.appendOrderBy(sqlFull, "CASE WHEN (" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_EXTRAINFO) + " &2048 ) != 0 THEN 1 ELSE 2 END");	
				}
				
			}
			
			String str = strBuilder.toString(); 
			strBuilder = new StringBuilder("Select * from (" + str + ") "); 
			strBuilder.append("UNION ALL Select * from (" + sqlFull.toString() + ") "); 
		}
		
		// MyLog.e("sql main", "data type :" + type); 
		if(type == SearchType.SEARCH_SINGER){
			strMain.append(strBuilder + ")");
			
			DBQueryBuilder.appendRefreshDevice(strMain, MyApplication.bOffUserList);
			DBQueryBuilder.appendKM1SelectList(strMain, MyApplication.intSelectList);
			DBQueryBuilder.appendGroupBy(strMain, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SORT));
			DBQueryBuilder.appendLimitOffset(strMain, count, offset); 
// KHIEM
			// MyLog.d("sql main", strMain.toString()); 
			return db.rawQuery(strMain.toString(), null); 
		} else {
			DBQueryBuilder.appendLimitOffset(strBuilder, count, offset);
// KHIEM			
			// MyLog.d("sql builder", strBuilder.toString()); 
			return db.rawQuery(strBuilder.toString(), null); 
		}
	}

	public int countTotalSongRemix(String literal, MEDIA_TYPE mediaType) {
		StringBuilder strBuilder = new StringBuilder("select COUNT()"); 
		DBQueryBuilder.appendTables(strBuilder, DbHelper.TABLE_SONGS);
		
		if(curTocType != TOCTYPE_SK9000) {
			DBQueryBuilder.appendInnerJoin(strBuilder, DbHelper.TABLE_SONGMODEL,  
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SO_MD)
					+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_SOMD)); 
		}
		
		// Filter with REMIX
		DBQueryBuilder.appendWhereFromFormat(strBuilder, "(%K=%@)", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_REMIX), 
				1);
		
		filterWithHiddenSK90xx(strBuilder, DbHelper.TABLE_SONGS);
		
		DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K IN %@)", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_ABCTYPE),
				"(0,1)");
		// Song Search
		if(literal != null && !literal.equals(""))
		{
			DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K LIKE %@ OR %K LIKE %@)",
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHORTNAME), 
					literal + "%", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_RAWNAME), 
					"%" + literal + "%"); 
		}

		// Filter with mediatype
		filterWithMediaType(strBuilder, mediaType, DbHelper.TABLE_SONGS);

		// Song shown enable
		DBQueryBuilder.appendFromFormat(strBuilder, "AND %K=%@", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHOWENABLE),
				"0");
		
		if(curTocType != TOCTYPE_SK9000) {
			StringBuilder modelSb = new StringBuilder(); 
			modelSb.append('('); 
			modelSb.append(curTocType); 
			
			if(curHDDToc > 0) {
				modelSb.append("," + curHDDToc);
			}
			modelSb.append(')'); 
			DBQueryBuilder.appendFromFormat(strBuilder, "AND %K IN %@", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_MODEL),
					modelSb.toString());
			
			DBQueryBuilder.appendFilterVol(strBuilder);
		}

		DBQueryBuilder.appendRefreshDevice(strBuilder, MyApplication.bOffUserList);
		DBQueryBuilder.appendKM1SelectList(strBuilder, MyApplication.intSelectList);
		// MyLog.e("database", "total remix");
		// MyLog.d("database", strBuilder.toString());
		Cursor cursorResult = db.rawQuery(strBuilder.toString(), null); 
		
		int count = 0; 
		if (cursorResult.moveToNext()) {
			count = cursorResult.getInt(0); 
		}
		
		cursorResult.close();
		
		return count;
	}

	public Cursor getSongRemix(String literal, SearchMode mode, MEDIA_TYPE mediaType, int offset, int count) {
		String colSearch = ""; 
		if(mode == SearchMode.MODE_FULL){
			colSearch = DbHelper.SONG_RAWNAME; 
		}
		else if(mode == SearchMode.MODE_MIXED) {
			colSearch = DbHelper.SONG_SHORTNAME; 
		}
		else {
			colSearch = DbHelper.SONG_SHORTNAME; 
		}
		
		StringBuilder strBuilder = new StringBuilder(); 
		createSongInfoQueryString(strBuilder, "", mode.ordinal()); 
		
		// Filter with REMIX
		DBQueryBuilder.appendWhereFromFormat(strBuilder, "(%K=%@)", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_REMIX), 
				1);
		
		filterWithHiddenSK90xx(strBuilder, DbHelper.TABLE_SONGS);
		
		DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K IN %@)", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_ABCTYPE),
				"(0,1)");
		// Song Search
		if(literal != null && !literal.equals(""))
		{	
			DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K LIKE %@)",
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, colSearch), 
					literal + "%"); 
		}
		
		// Filter with mediatype
		filterWithMediaType(strBuilder, mediaType, DbHelper.TABLE_SONGS);
		
		DBQueryBuilder.appendFromFormat(strBuilder, "AND %K=%@", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHOWENABLE),
						"0");
		
		if(curTocType != TOCTYPE_SK9000) {
			StringBuilder modelSb = new StringBuilder(); 
			modelSb.append('('); 
			modelSb.append(curTocType); 
			
			if(curHDDToc > 0) {
				modelSb.append("," + curHDDToc);
			}
			modelSb.append(')'); 
			DBQueryBuilder.appendFromFormat(strBuilder, "AND %K IN %@", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_MODEL),
					modelSb.toString());
			
			DBQueryBuilder.appendFilterVol(strBuilder);
		}
		DBQueryBuilder.appendRefreshDevice(strBuilder, MyApplication.bOffUserList);
		DBQueryBuilder.appendKM1SelectList(strBuilder, MyApplication.intSelectList);
		DBQueryBuilder.appendGroupBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SORT));
		if(literal != null && !literal.equals("")) {
//			DBQueryBuilder.appendOrderBy(strBuilder, "CASE WHEN " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_LANGUAGE_ID) + "!=" + LANG_INDEX.CHINESE.ordinal() + " THEN 1 ELSE 2 END, length(" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHORTNAME) + ")");
			DBQueryBuilder.appendOrderBy(strBuilder, "length(" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHORTNAME) + ")" + "," + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_LANGUAGE_ID));
		}
		
		StringBuilder sqlFull = new StringBuilder(); 
		if(mode == SearchMode.MODE_MIXED && literal != null && !literal.equals(""))
		{
			createSongInfoQueryString(sqlFull, "", SearchMode.MODE_FULL.ordinal()); 
			
			// Filter with REMIX
			DBQueryBuilder.appendWhereFromFormat(sqlFull, "(%K=%@)", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_REMIX), 
					1);
			
			filterWithHiddenSK90xx(sqlFull, DbHelper.TABLE_SONGS);			
			
			// Song Search
			if(literal != null && !literal.equals(""))
			{
				DBQueryBuilder.appendFromFormat(sqlFull, "AND (%K NOT LIKE %@ AND %K LIKE %@)",
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHORTNAME), 
						literal + "%", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_RAWNAME), 
						"%" + literal + "%"); 
			}
			
			// Filter with mediatype
			filterWithMediaType(sqlFull, mediaType, DbHelper.TABLE_SONGS);

			DBQueryBuilder.appendFromFormat(sqlFull, "AND %K=%@", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHOWENABLE),
							"0");
			if(curTocType != TOCTYPE_SK9000) {
				StringBuilder modelSb = new StringBuilder(); 
				modelSb.append('('); 
				modelSb.append(curTocType); 
				
				if(curHDDToc > 0) {
					modelSb.append("," + curHDDToc);
				}
				modelSb.append(')'); 
				DBQueryBuilder.appendFromFormat(sqlFull, "AND %K IN %@", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_MODEL),
						modelSb.toString());
				
				DBQueryBuilder.appendFilterVol(sqlFull);
			}
			DBQueryBuilder.appendRefreshDevice(sqlFull, MyApplication.bOffUserList);
			DBQueryBuilder.appendKM1SelectList(sqlFull, MyApplication.intSelectList);
			DBQueryBuilder.appendGroupBy(sqlFull, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SORT));
			if(literal != null && !literal.equals("")) {
//				DBQueryBuilder.appendOrderBy(strBuilder, "CASE WHEN " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_LANGUAGE_ID) + "!=" + LANG_INDEX.CHINESE.ordinal() + " THEN 1 ELSE 2 END, length(" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHORTNAME) + ")");
				DBQueryBuilder.appendOrderBy(sqlFull, "length(" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SHORTNAME) + ")" + "," + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_LANGUAGE_ID));
			}
			
			String str = strBuilder.toString(); 
			strBuilder = new StringBuilder("Select * from (" + str + ") "); 
			strBuilder.append("UNION ALL Select * from (" + sqlFull.toString() + ") "); 
		}
		DBQueryBuilder.appendLimitOffset(strBuilder, count, offset);
		
		// MyLog.e("database", "LOAD REMIX"); 
		// MyLog.d("database", strBuilder.toString()); 
		return db.rawQuery(strBuilder.toString(), null); 
	}
	
	public ArrayList<Integer> getAuthorIDFromSong(int songID, int typeABC)
	{
		StringBuilder sql = new StringBuilder(); 
		DBQueryBuilder.appendSelect(sql); 
		DBQueryBuilder.appendColumns(sql, DbHelper.TABLE_SONGMUSICIAN, new String[]{DbHelper.SONGMUSICIAN_ATPK});
		DBQueryBuilder.appendTables(sql, DbHelper.TABLE_SONGMUSICIAN); 
		DBQueryBuilder.appendInnerJoin(sql, DbHelper.TABLE_SONGS, 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_PK)
				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMUSICIAN, DbHelper.SONGMUSICIAN_SOPK));
		DBQueryBuilder.appendWhereFromFormat(sql, "%K=%@ and %K=%@", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_ID), songID, 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_ABCTYPE), typeABC); 
		
//		Log.e("", "sql: "+ sql.toString()); 
		
		Cursor cursorResult = db.rawQuery(sql.toString(), null); 
		
		ArrayList<Integer> result = new ArrayList<Integer>(); 
		while (cursorResult.moveToNext()) {
			result.add(cursorResult.getInt(0)); 
		}
		
		cursorResult.close();
		
		return result; 
	}
	
//////////////////////////////// - KHIEM - //////////////////////////////////////////////
	
	public Cursor getTuTestLay(String literal, SearchMode mode, MEDIA_TYPE mediaType, int offset, int count){
//		String LIMIT = offset + "," + count;
		String[] columns = new String[]{DbHelper.SONG_ID, "SONG_NAME", DbHelper.SONG_SHORTNAME,
				DbHelper.SONG_LYRIC, DbHelper.SONG_REMIX, DbHelper.SONG_MEDIATYPE, 
				DbHelper.SONG_SINGER_ID, DbHelper.SONG_FAVOUR, "MUSICIAN_NAME"};
		HashMap<String,String> columnMap = new HashMap<String,String>();
		columnMap.put(DbHelper.SONG_ID, DbHelper.TABLE_SONGS + "." + DbHelper.SINGER_ID + " as " + DbHelper.SONG_ID);
		columnMap.put("SONG_NAME", DbHelper.TABLE_SONGS + "." + DbHelper.SONG_NAME + " as SONG_NAME");
		columnMap.put(DbHelper.SONG_LYRIC, DbHelper.TABLE_SONGS + "." + DbHelper.SONG_LYRIC + " as " + DbHelper.SONG_LYRIC);
		columnMap.put(DbHelper.SONG_SHORTNAME, DbHelper.TABLE_SONGS + "." + DbHelper.SONG_SHORTNAME + " as " + DbHelper.SONG_SHORTNAME);
		columnMap.put(DbHelper.SONG_REMIX, DbHelper.TABLE_SONGS + "." + DbHelper.SONG_REMIX + " as " + DbHelper.SONG_REMIX);
		columnMap.put(DbHelper.SONG_MEDIATYPE, DbHelper.TABLE_SONGS + "." + DbHelper.SONG_MEDIATYPE + " as " + DbHelper.SONG_MEDIATYPE);
		columnMap.put(DbHelper.SONG_SINGER_ID, DbHelper.TABLE_SONGS + "." + DbHelper.SONG_SINGER_ID + " as " + DbHelper.SONG_SINGER_ID);
		columnMap.put(DbHelper.SONG_FAVOUR, DbHelper.TABLE_SONGS + "." + DbHelper.SONG_FAVOUR + " as " + DbHelper.SONG_FAVOUR);
		columnMap.put("MUSICIAN_NAME", DbHelper.TABLE_MUSICIAN + "." + DbHelper.MUSICIAN_NAME + " as MUSICIAN_NAME");
		
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(DbHelper.TABLE_SONGS  + ", " + DbHelper.TABLE_MUSICIAN);
		queryBuilder.setProjectionMap(columnMap);
		
		queryBuilder.appendWhere(DbHelper.TABLE_SONGS + "." + DbHelper.SONG_MUSICIAN_ID + " = " + 
						DbHelper.TABLE_MUSICIAN + "." + DbHelper.MUSICIAN_ID);
		Cursor cursor = queryBuilder.query(db, columns, null, null, null, null, "SONG_NAME");
		
		return cursor;
		
	}
	
	private void dropIndexSong(String tableName){
//		MyLog.e("DBInstance", "dropIndexSong");
		db.execSQL("DROP INDEX IF EXISTS " + tableName); 
	}
	
	public void createIndexSong(String index, String table){
		MyLog.e("DBInstance", "createIndexSong");
		dropIndexSong("INDEX_ZSONGS");
//		String query = "CREATE INDEX IF NOT EXISTS INDEX_ZSONGS ON ZSONGS(ZSHOW, ZSHORTNAME, ZTYPE, ZNUMWORDS, Z_PK, ZID, ZINDEX5"
//				+ ", ZNAME, ZTITLERAW, ZLYRIC, ZREMIX, ZFAVOURITE, ZABC, ZEXTRA)";
//		db.execSQL(query);
		
		dropIndexSong("INDEX_ZSONGS_2");
		String query = "CREATE INDEX IF NOT EXISTS INDEX_ZSONGS_2 ON ZSONGS(ZSORT)";
		db.execSQL(query);
		
		dropIndexSong("INDEX_ZSONGS_3");
		query = "CREATE INDEX IF NOT EXISTS INDEX_ZSONGS_3 ON ZSONGS(ZNUMWORDS, ZSHOW, ZSHORTNAME)";
		db.execSQL(query);
	}
	
	public  String GetNameSinger(String idSinger){
		String where = "SELECT ZNAME FROM ZSINGERS WHERE Z_PK = " + idSinger;
		Cursor cursor = db.rawQuery(where, null); 
		if (cursor.moveToFirst()) {
			String name = cursor.getString(0);
			if(name != null){
				cursor.close();
				return name;
			}else{
				cursor.close();
				return "";
			}
		} else {
			cursor.close();
			return "";
		}		
	}
	
	public  String GetNameMusician(String idMusician){
		String where = "SELECT ZNAME FROM ZMUSICIANS WHERE Z_PK = " + idMusician;
		Cursor cursor = db.rawQuery(where, null); 
		if (cursor.moveToFirst()) {
			String name = cursor.getString(0);
			if(name != null){
				cursor.close();
				return name;
			}else{
				cursor.close();
				return "";
			}
		} else {
			cursor.close();
			return "";
		}		
	}
	
	public  String GetNameSong(String idSong){
		if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
				|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
				 || MyApplication.intSvrModel == MyApplication.SONCA_KBX9 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
				 || MyApplication.intSvrModel == MyApplication.SONCA_TBT){
			StringBuilder strBuilder = new StringBuilder(); 
			createSongInfoQueryStringHIW(strBuilder);
			
			if(curTocType != TOCTYPE_SK9000) {
				StringBuilder modelSb = new StringBuilder(); 
				modelSb.append('('); 
				modelSb.append(curTocType); 
				
				if(curHDDToc > 0) {
					modelSb.append("," + curHDDToc);
				}
				modelSb.append(')'); 
				DBQueryBuilder.appendWhereFromFormat(strBuilder, "%K IN %@", 
						DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_MODEL),
						modelSb.toString());
				
				DBQueryBuilder.appendFilterVol(strBuilder);
			}else {
				DBQueryBuilder.appendWhereFromFormat(strBuilder, false, "%K=%@", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_MODEL), curTocType);
			}
			
			DBQueryBuilder.appendFromFormat(strBuilder, " AND (%K=%@ OR %K=%@)", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_ID), idSong, 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_INDEX5), idSong); 
			
			Cursor cursor = db.rawQuery(strBuilder.toString(), null); 
			if (cursor.moveToFirst()) {
				String name = cursor.getString(2);
				if(name != null){
					cursor.close();
					cursor = null;
					return name;
				}else{
					cursor.close();
					cursor = null;
					return "";
				}
			} else {
				cursor.close();
				cursor = null;
				return "";
			}	
			
		}
		
		String where = "SELECT ZNAME FROM ZSONGS WHERE ZID = " + idSong + " OR ZINDEX5 = " + idSong;
		Cursor cursor = db.rawQuery(where, null); 
		if (cursor.moveToFirst()) {
			String name = cursor.getString(0);
			if(name != null){
				cursor.close();
				cursor = null;
				return name;
			}else{
				cursor.close();
				cursor = null;
				return "";
			}
		} else {
			cursor.close();
			cursor = null;
			return "";
		}		
	}
	
	public  Singer GetOneSinger(String idSinger){
		String where = "SELECT ZNAME,ZCID  FROM ZSINGERS WHERE Z_PK = " + idSinger;
		Cursor cursor = db.rawQuery(where, null); 
		Singer singer = new Singer();
		if (cursor.moveToFirst()) {
			singer.setName(cursor.getString(0));
			singer.setCoverID(cursor.getInt(1));
			cursor.close();
			cursor = null;
			return singer;
		} else {
			cursor.close();
			cursor = null;
			return singer;
		}		
	}
	
	public  Musician GetOneMusician(String idMusician){
		String where = "SELECT ZNAME, ZCID FROM ZMUSICIANS WHERE Z_PK = " + idMusician;
		Cursor cursor = db.rawQuery(where, null); 
		Musician singer = new Musician();
		if (cursor.moveToFirst()) {
			singer.setName(cursor.getString(0));
			singer.setCoverID(cursor.getInt(1));
			cursor.close();
			cursor = null;
			return singer;
		} else {
			cursor.close();
			cursor = null;
			return singer;
		}		
	}
	
	private Spannable createSpannable(String textSong , String nameraw , String textSearch, int language){
		textSearch.trim();
		ArrayList<Integer> listOffset = new ArrayList<Integer>();
		Spannable wordtoSpan;
		if(textSong.equals("")){
			wordtoSpan = new SpannableString("");
		}else{
			if(textSearch.equals("")){
				return new SpannableString(textSong);
			}
			textSearch.trim();
			String newString = textSong.replaceAll("[ &+=_,-]", "*");
			StringBuffer buffer = new StringBuffer(newString);
				//-------------//
			String[] strings = buffer.toString().split("[*]");
			int count = 0;
 			for (int i = 0; i < strings.length; i++) {
 				int size = strings[i].length();
 				if(size <= 0){
 					count += 1;
 				}else{
 					listOffset.add(count);
 					count += size + 1;
 				}
 				if(listOffset.size() >= textSearch.length()){
 					break;
 				}
			}
 				//-------------//
			wordtoSpan = new SpannableString(textSong);
			if (language == 3) {
				if(!textSearch.equals("")){
					wordtoSpan.setSpan(new ForegroundColorSpan(Color.GREEN), 0, textSong.length(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			} else {
				for (int i = 0; i < listOffset.size(); i++) {
					int offset = getIndex(textSong, listOffset.get(i));
					wordtoSpan.setSpan(new ForegroundColorSpan(Color.GREEN), offset, offset + 1,
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}	
		}	
		return wordtoSpan;
	}
	
	private int getIndex(String string , int index){
		switch (string.charAt(index)) {
		case '(': 	return index + 1; 
		case '`': 	return index + 1; 
		case '[': 	return index + 1; 
		default:	return index;
		}
		
	}
	
	public  String GetNameSong_YouTube(String idSong){		
		String where = "SELECT ZNAME FROM ZSONGS_YOUTUBE WHERE ZID = " + idSong + " OR ZINDEX5 = " + idSong;
		Cursor cursor = db.rawQuery(where, null); 
		if (cursor.moveToFirst()) {
			String name = cursor.getString(0);
			if(name != null){
				cursor.close();
				cursor = null;
				return name;
			}else{
				cursor.close();
				cursor = null;
				return "";
			}
		} else {
			cursor.close();
			cursor = null;
			return "";
		}		
	}
	
	private String cutText(int maxLength, String content) {				
		if(content == null){
			return "";
		}
		if(content.length() <= maxLength){
			return content;
		}		
		return content.substring(0, maxLength) + "...";
	}
	
	private void QueryLanguageAndTypeABC(StringBuilder idString, StringBuilder strBuilder){
		DBQueryBuilder.appendWhereFromFormat(strBuilder, "(%K IN %@)", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_LANGUAGE_ID),
				idString.toString());
		DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K IN %@)", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_ABCTYPE),
				"(0,1)");
		
		filterWithHiddenSK90xx(strBuilder, DbHelper.TABLE_SONGS);
	}
	
	private void QueryLanguageAndTypeABC_New(StringBuilder idString, StringBuilder strBuilder){
		DBQueryBuilder.appendWhereFromFormat(strBuilder, "(%K IN %@)", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_NEW, DbHelper.SONG_LANGUAGE_ID),
				idString.toString());
		DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K IN %@)", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS_NEW, DbHelper.SONG_ABCTYPE),
				"(0,1)");
	
		filterWithHiddenSK90xx(strBuilder, DbHelper.TABLE_SONGS_NEW);
	}
	
	public List<Integer> GetTotalTypeABCSong(String idSong, String name){
		List<Integer> listResult = new ArrayList<Integer>();
		String where = "SELECT ZABC FROM ZSONGS WHERE (ZID = " + idSong + " OR ZINDEX5 = " + idSong + ") AND ZNAME = '" + name + "'";
		//MyLog.e("GetTotalTypeABCSong",where);
		Cursor cursor = db.rawQuery(where, null); 
		if(cursor != null){
			while(cursor.moveToNext()){
				listResult.add(cursor.getInt(0));
			}
			cursor.close();
			cursor = null;
		}
		return listResult;
	}
	
	public int GetTotalSongNumber(String where, MEDIA_TYPE mediaType){	
		
		int count = 0;		
		if(MyApplication.intSvrModel == MyApplication.SONCA
				|| MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
			Cursor cursor = GetSongNumber(where, 0, 0, mediaType);
			count = cursor.getCount();
			cursor.close();
		} else {
			Cursor cursor = GetSongNumber2(where, 0, 0, mediaType);
			count = cursor.getCount();
			cursor.close();
		}
		
		// MyLog.e("GetTotalSongNumber", count + "");
		
		return count;
	}
	
	public Cursor GetSongNumber2(String where, int offset, int count, MEDIA_TYPE mediaType){
		StringBuilder builder = new StringBuilder("");
		createSongInfoQueryStringNoModel2(builder, "", -1);

		// MAIN WHERE
		DBQueryBuilder.appendWhereFromFormat(builder, " (%K = %@ OR %K = %@) ", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_ID), where,
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_ID5), where);
		
		filterWithHiddenSK90xx(builder, DbHelper.TABLE_SONGS);
				
		// BO HOA AM B,C 
		DBQueryBuilder.appendFromFormat(builder, " AND (%K IN %@)", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_ABCTYPE),
				"(0,1)");
		
		// FILTER WITH MEDIATYPE
		filterWithMediaType(builder, mediaType, DbHelper.TABLE_SONGS);
		
		if(curTocType != TOCTYPE_SK9000) {
			StringBuilder modelSb = new StringBuilder(); 
			modelSb.append('('); 
			modelSb.append(curTocType); 
			
			if(curHDDToc > 0) {
				modelSb.append("," + curHDDToc);
			}
			modelSb.append(')'); 
			DBQueryBuilder.appendFromFormat(builder, " AND %K IN %@ ", 
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_MODEL),
					modelSb.toString());
			
			DBQueryBuilder.appendFilterVol(builder);
		}
		
		// BAT TAT USER		
		DBQueryBuilder.appendRefreshDevice(builder, MyApplication.bOffUserList);
		DBQueryBuilder.appendKM1SelectList(builder, MyApplication.intSelectList);
		
		// MyLog.e("GetSongNumber", builder.toString());
		
		Cursor cursor = db.rawQuery(builder.toString(), null);
		
		Cursor cursorCheck = cursor;
		
		if (cursorCheck.moveToNext()) {
			String strTemp = cursorCheck.getString(2);
			// MyLog.e(" ", "strTemp = " + strTemp);
			if(strTemp == null){
				cursor = db.rawQuery("SELECT ZID FROM ZSONGS WHERE Z_PK = -100", null);
				return cursor;
			} 
		}
		
		return cursor;
	}
	
	public Cursor GetSongNumber(String where, int offset, int count, MEDIA_TYPE mediaType){
		StringBuilder builder = new StringBuilder("");
		createSongInfoQueryStringNoModel(builder, "", -1);

		// MAIN WHERE
		DBQueryBuilder.appendWhereFromFormat(builder, " (%K = %@ OR %K = %@) ", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_ID), where,
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_ID5), where);
		
		filterWithHiddenSK90xx(builder, DbHelper.TABLE_SONGS);
		
		// BO HOA AM B,C 
		DBQueryBuilder.appendFromFormat(builder, " AND (%K IN %@)", 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_ABCTYPE),
				"(0,1)");
		
		// FILTER WITH MEDIATYPE
		filterWithMediaType(builder, mediaType, DbHelper.TABLE_SONGS);
		
		// BAT TAT USER		
		DBQueryBuilder.appendRefreshDevice(builder, MyApplication.bOffUserList);
		DBQueryBuilder.appendKM1SelectList(builder, MyApplication.intSelectList);
		
		// MyLog.e("GetSongNumber", builder.toString());
		
		Cursor cursor = db.rawQuery(builder.toString(), null);
		
		Cursor cursorCheck = cursor;
		
		if (cursorCheck.moveToNext()) {
			String strTemp = cursorCheck.getString(2);
			// MyLog.e(" ", "strTemp = " + strTemp);
			if(strTemp == null){
				cursor = db.rawQuery("SELECT ZID FROM ZSONGS WHERE Z_PK = -100", null);
				return cursor;
			} 
		}
		
		return cursor;
	}
	
	private void createSongInfoQueryStringNoModel2(StringBuilder sb, String dbName, int type){

		if(dbName == null || dbName.equals("")) 
			dbName = "MAIN"; 
		
		DBQueryBuilder.appendSelect(sb); 
		
		if(curTocType != TOCTYPE_SK9000) {
			DBQueryBuilder.appendColumns(sb, DbHelper.TABLE_SONGS, new String[]{DbHelper.SONG_ID});
			sb.append(","); 
			DBQueryBuilder.appendColumns(sb, DbHelper.TABLE_SONGS, new String[]{DbHelper.SONG_ID5});
			sb.append(","); 
			DBQueryBuilder.appendColumns(sb, DbHelper.TABLE_SONGS, new String[]{DbHelper.SONG_NAME/*,DbHelper.SONG_SHORTNAME*/, DbHelper.SONG_RAWNAME, DbHelper.SONG_LYRIC, 
					DbHelper.SONG_MEDIATYPE, DbHelper.SONG_REMIX, DbHelper.SONG_FAVOUR});
			sb.append(","); 
			DBQueryBuilder.appendColumns(sb, DbHelper.TABLE_SONGMODEL, new String[]{DbHelper.SONG_ABCTYPE});
			sb.append(","); 
			DBQueryBuilder.appendColumns(sb, DbHelper.TABLE_SONGS, new String[]{DbHelper.SONG_EXTRAINFO});
			
		}else {
			DBQueryBuilder.appendColumns(sb, DbHelper.TABLE_SONGS, new String[]{DbHelper.SONG_ID, DbHelper.SONG_ID5, DbHelper.SONG_NAME/*,DbHelper.SONG_SHORTNAME*/, DbHelper.SONG_RAWNAME, DbHelper.SONG_LYRIC, 
				DbHelper.SONG_MEDIATYPE, DbHelper.SONG_REMIX, DbHelper.SONG_FAVOUR,DbHelper.SONG_ABCTYPE, DbHelper.SONG_EXTRAINFO});
		}
		
		sb.append(","); 
		sb.append("group_concat(distinct " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SINGER, DbHelper.SINGER_NAME) + ")"); 
		sb.append(","); 
		sb.append("group_concat(distinct " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SINGER, DbHelper.SINGER_ID) + ")"); 
		
		sb.append(","); 
		sb.append("group_concat(distinct " + DBQueryBuilder.getColumnString(DbHelper.TABLE_MUSICIAN, DbHelper.MUSICIAN_NAME) + ")"); 
		sb.append(","); 
		sb.append("group_concat(distinct " + DBQueryBuilder.getColumnString(DbHelper.TABLE_MUSICIAN, DbHelper.MUSICIAN_ID) + ")");
		
		sb.append(","); 
		DBQueryBuilder.appendColumns(sb, DbHelper.TABLE_SONGS, new String[]{DbHelper.SONG_SHOWENABLE});			
		
		if(type != -1) {
			sb.append(",'" + type + "' AS " + DbHelper.COMPARE_TYPE); 
		}
		DBQueryBuilder.appendTables(sb, dbName + "].[" + DbHelper.TABLE_SONGS); 
		DBQueryBuilder.appendInnerJoin(sb, dbName + "].[" + DbHelper.TABLE_SONGSINGER,  
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_PK)
				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGSINGER, DbHelper.SONGSINGER_SOPK));
		DBQueryBuilder.appendInnerJoin(sb, dbName + "].[" + DbHelper.TABLE_SINGER, 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGSINGER, DbHelper.SONGSINGER_ATPK)
				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SINGER, DbHelper.SINGER_ID));
		
		DBQueryBuilder.appendInnerJoin(sb, dbName + "].[" + DbHelper.TABLE_SONGMUSICIAN,  
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_PK)
				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMUSICIAN, DbHelper.SONGMUSICIAN_SOPK)); 
		DBQueryBuilder.appendInnerJoin(sb, dbName + "].[" + DbHelper.TABLE_MUSICIAN, 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMUSICIAN, DbHelper.SONGMUSICIAN_ATPK)
				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_MUSICIAN, DbHelper.MUSICIAN_ID));
		
		if(curTocType != TOCTYPE_SK9000) {
			DBQueryBuilder.appendInnerJoin(sb, dbName + "].[" + DbHelper.TABLE_SONGMODEL,  
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SO_MD)
					+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_SOMD));
		}
	}
	
	private void createSongInfoQueryStringNoModel(StringBuilder sb, String dbName, int type){

		if(dbName == null || dbName.equals("")) 
			dbName = "MAIN"; 
		
		DBQueryBuilder.appendSelect(sb); 
		
		if(curTocType != TOCTYPE_SK9000) {
			DBQueryBuilder.appendColumns(sb, DbHelper.TABLE_SONGS, new String[]{DbHelper.SONG_ID});
			sb.append(","); 
			DBQueryBuilder.appendColumns(sb, DbHelper.TABLE_SONGS, new String[]{DbHelper.SONG_ID5});
			sb.append(","); 
			DBQueryBuilder.appendColumns(sb, DbHelper.TABLE_SONGS, new String[]{DbHelper.SONG_NAME/*,DbHelper.SONG_SHORTNAME*/, DbHelper.SONG_RAWNAME, DbHelper.SONG_LYRIC, 
					DbHelper.SONG_MEDIATYPE, DbHelper.SONG_REMIX, DbHelper.SONG_FAVOUR,DbHelper.SONG_ABCTYPE, DbHelper.SONG_EXTRAINFO});
		}else {
			DBQueryBuilder.appendColumns(sb, DbHelper.TABLE_SONGS, new String[]{DbHelper.SONG_ID, DbHelper.SONG_ID5, DbHelper.SONG_NAME/*,DbHelper.SONG_SHORTNAME*/, DbHelper.SONG_RAWNAME, DbHelper.SONG_LYRIC, 
				DbHelper.SONG_MEDIATYPE, DbHelper.SONG_REMIX, DbHelper.SONG_FAVOUR,DbHelper.SONG_ABCTYPE, DbHelper.SONG_EXTRAINFO});
		}
		
		sb.append(","); 
		sb.append("group_concat(distinct " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SINGER, DbHelper.SINGER_NAME) + ")"); 
		sb.append(","); 
		sb.append("group_concat(distinct " + DBQueryBuilder.getColumnString(DbHelper.TABLE_SINGER, DbHelper.SINGER_ID) + ")"); 
		
		sb.append(","); 
		sb.append("group_concat(distinct " + DBQueryBuilder.getColumnString(DbHelper.TABLE_MUSICIAN, DbHelper.MUSICIAN_NAME) + ")"); 
		sb.append(","); 
		sb.append("group_concat(distinct " + DBQueryBuilder.getColumnString(DbHelper.TABLE_MUSICIAN, DbHelper.MUSICIAN_ID) + ")");
		
		sb.append(","); 
		DBQueryBuilder.appendColumns(sb, DbHelper.TABLE_SONGS, new String[]{DbHelper.SONG_SHOWENABLE});		
		
		if(type != -1) {
			sb.append(",'" + type + "' AS " + DbHelper.COMPARE_TYPE); 
		}
		DBQueryBuilder.appendTables(sb, dbName + "].[" + DbHelper.TABLE_SONGS); 
		DBQueryBuilder.appendInnerJoin(sb, dbName + "].[" + DbHelper.TABLE_SONGSINGER,  
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_PK)
				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGSINGER, DbHelper.SONGSINGER_SOPK));
		DBQueryBuilder.appendInnerJoin(sb, dbName + "].[" + DbHelper.TABLE_SINGER, 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGSINGER, DbHelper.SONGSINGER_ATPK)
				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SINGER, DbHelper.SINGER_ID));
		
		DBQueryBuilder.appendInnerJoin(sb, dbName + "].[" + DbHelper.TABLE_SONGMUSICIAN,  
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_PK)
				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMUSICIAN, DbHelper.SONGMUSICIAN_SOPK)); 
		DBQueryBuilder.appendInnerJoin(sb, dbName + "].[" + DbHelper.TABLE_MUSICIAN, 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMUSICIAN, DbHelper.SONGMUSICIAN_ATPK)
				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_MUSICIAN, DbHelper.MUSICIAN_ID));
		
		if(curTocType != TOCTYPE_SK9000) {
			DBQueryBuilder.appendInnerJoin(sb, dbName + "].[" + DbHelper.TABLE_SONGMODEL,  
					DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SO_MD)
					+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMODEL, DbHelper.SONGMODEL_SOMD));
		}
	}
	
	public void processNewYoutubeTable(){
		// Drop table first
		StringBuilder sqlStatement = new StringBuilder(); 
		sqlStatement.append("DROP TABLE IF EXISTS " + DbHelper.TABLE_SONGS_YOUTUBE+ "; ");		
		db.execSQL(sqlStatement.toString()); 
		
		// Create new youtube table
		createYoutubeTable();	
		
	}
	
	public void processNewSongTable(){
		// Drop table first
		StringBuilder sqlStatement = new StringBuilder(); 
		sqlStatement.append("DROP TABLE IF EXISTS " + DbHelper.TABLE_SONGS_NEW + "; ");		
		db.execSQL(sqlStatement.toString()); 
		
		// Create new song table
		createNewSongTable();
		
		// Transfer data from Song table		
		sqlStatement = new StringBuilder(); 
		sqlStatement.append("INSERT INTO " + DbHelper.TABLE_SONGS_NEW + " ");
		sqlStatement.append("(");
		sqlStatement.append(DbHelper.SONG_PK + ", " + DbHelper.SONG_ID + ", " + DbHelper.SONG_ID5 + ", " + DbHelper.SONG_SO_MD + ", " + DbHelper.SONG_NAME
				 + ", " + DbHelper.SONG_SHORTNAME + ", " + DbHelper.SONG_RAWNAME + ", " + DbHelper.SONG_LYRIC + ", " + DbHelper.SONG_NUMWORDS + ", " + DbHelper.SONG_FAVOUR
				 + ", " + DbHelper.SONG_REMIX + ", " + DbHelper.SONG_MEDIATYPE + ", " + DbHelper.SONG_SHOWENABLE + ", " + DbHelper.SONG_ONETOUCH + ", " + DbHelper.SONG_NEWVOL
				 + ", " + DbHelper.SONG_ABCTYPE + ", " + DbHelper.SONG_EXTRAINFO + ", " + DbHelper.SONG_CPRIGHT + ", " + DbHelper.SONG_SORT + ", " + DbHelper.SONG_LANGUAGE_ID
				 + ", " + DbHelper.SONG_TYPE_ID);
		sqlStatement.append(") ");
		sqlStatement.append("SELECT ");
		sqlStatement.append(DbHelper.SONG_PK + ", " + DbHelper.SONG_ID + ", " + DbHelper.SONG_ID5 + ", " + DbHelper.SONG_SO_MD + ", " + DbHelper.SONG_NAME
				 + ", " + DbHelper.SONG_SHORTNAME + ", " + DbHelper.SONG_RAWNAME + ", " + DbHelper.SONG_LYRIC + ", " + DbHelper.SONG_NUMWORDS + ", " + DbHelper.SONG_FAVOUR
				 + ", " + DbHelper.SONG_REMIX + ", " + DbHelper.SONG_MEDIATYPE + ", " + DbHelper.SONG_SHOWENABLE + ", " + DbHelper.SONG_ONETOUCH + ", " + DbHelper.SONG_NEWVOL
				 + ", " + DbHelper.SONG_ABCTYPE + ", " + DbHelper.SONG_EXTRAINFO + ", " + DbHelper.SONG_CPRIGHT + ", " + DbHelper.SONG_SORT + ", " + DbHelper.SONG_LANGUAGE_ID
				 + ", " + DbHelper.SONG_TYPE_ID);
		sqlStatement.append(" ");
		sqlStatement.append("FROM " + DbHelper.TABLE_SONGS + " ");
		sqlStatement.append("ORDER BY ");
		sqlStatement.append("(");
		sqlStatement.append("CASE WHEN ("+DbHelper.TABLE_SONGS+"."+DbHelper.SONG_EXTRAINFO+" &1 ) != 0 ");
		sqlStatement.append("THEN 0 ");
		sqlStatement.append("ELSE ");
		sqlStatement.append("CASE WHEN "+DbHelper.TABLE_SONGS+"."+DbHelper.SONG_NEWVOL+" = 1 ");
		sqlStatement.append("THEN 1 ");
		sqlStatement.append("ELSE ");
		sqlStatement.append("CASE WHEN "+DbHelper.TABLE_SONGS+"."+DbHelper.SONG_REMIX+" = 1 ");
		sqlStatement.append("THEN 2 ");
		sqlStatement.append("ELSE ");
		sqlStatement.append("CASE WHEN ("+DbHelper.TABLE_SONGS+"."+DbHelper.SONG_EXTRAINFO+" &8192 ) != 0 ");
		sqlStatement.append("THEN 3 ");
		sqlStatement.append("ELSE 4 ");
		sqlStatement.append("END END END END ");
		sqlStatement.append(") ");
		db.execSQL(sqlStatement.toString()); 			
		
	}
	
	public void processInsertIntoYoutubeTable(List<Song> listData){
		db.beginTransactionNonExclusive();
		
		for (Song song : listData) {
			String sql = "INSERT INTO " + DbHelper.TABLE_SONGS_YOUTUBE + " VALUES ";
			sql += " (";
			sql += song.getId() + " , "; // Z_PK
			sql += song.getId() + " , ";
			sql += song.getIndex5() + " , ";
			sql += 0 + " , "; // ZSO_MD
			sql += "'" + song.getName() + "' , ";
			sql += "'" + song.getShortName() + "' , ";
			sql += "'" + song.getTitleRaw() + "' , ";
			sql += "'" + song.getLyric() + "' , ";
			sql += song.getTitleRaw().split(" ").length + " , ";
			sql += (song.isFavourite()?1:0) + " , ";
			sql += (song.isRemix()?1:0) + " , ";
			sql += song.getMediaType().ordinal() + " , ";
			sql += 0 + " , ";  // ZSHOW
			sql += 0 + " , ";  // ZONETOUCH
			sql += 0 + " , ";  // ZNEWSONG
			sql += song.getTypeABC() + " , ";
			sql += 32928 + " , "; // ZEXTRA
			sql += 0 + " , ";  // ZSCA
			sql += song.getId() + " , ";  // ZSORT
			sql += song.getLanguageID() + " , ";
			sql += 1 + " , ";  // ZTYPEID - The loai tre trung
			sql += "'" + song.getPlayLink() + "' , ";
			sql += "'" + song.getDownLink() + "' ,";
			sql += "'" + song.getSingerName() + "' , ";
			sql += "'" + song.getTheloaiName() + "' , ";
			sql += "'" + song.getMidiDownLink() + "' , ";
			sql += (song.isIs2Stream()?1:0) + " , ";
			sql += (song.isVocalSinger()?1:0) + " , ";
			sql += (song.isSambaSong()?1:0) + " , ";
			sql += "'" + song.getSambaPath().replaceAll("'","\'") + "'";
			sql += " )";
		
//			MyLog.d("DBInstance processInsertIntoYoutubeTable", "sql = " + sql);
			
			db.execSQL(sql); 
			
//			sql = "INSERT INTO " + DbHelper.TABLE_SONGSINGER + " VALUES ";
//			sql += " (";
//			sql += song.getId() + " , "; // Z_SOPK
//			sql += 0 + " "; // no singer
//			sql += " )";
//			
//			db.execSQL(sql); 
			
		}
		
		db.setTransactionSuccessful();
		db.endTransaction();
	}
	
	public void processInsertIntoSongTable(List<Song> listData){
		db.beginTransactionNonExclusive();
		
		for (Song song : listData) {
			String sql = "INSERT INTO " + DbHelper.TABLE_SONGS + " VALUES ";
			sql += " (";
			sql += song.getId() + " , "; // Z_PK
			sql += song.getId() + " , ";
			sql += song.getId() + " , ";
			sql += 0 + " , "; // ZSO_MD
			sql += "'" + song.getName() + "' , ";
			sql += "'" + song.getShortName() + "' , ";
			sql += "'" + song.getTitleRaw() + "' , ";
			sql += "'" + song.getLyric() + "' , ";
			sql += song.getTitleRaw().split(" ").length + " , ";
			sql += (song.isFavourite()?1:0) + " , ";
			sql += (song.isRemix()?1:0) + " , ";
			sql += song.getMediaType().ordinal() + " , ";
			sql += 0 + " , ";  // ZSHOW
			sql += 0 + " , ";  // ZONETOUCH
			sql += 0 + " , ";  // ZNEWSONG
			sql += song.getTypeABC() + " , ";
			
			// ZEXTRA
			if (song.getMediaType() == MEDIA_TYPE.MIDI) {
				sql += 34240 + " , "; // ZEXTRA
			} else if (song.getMediaType() == MEDIA_TYPE.VIDEO) {
				sql += 33184 + " , "; // KTV VIDEO				
			} else {
				sql += 33984 + " , "; // MP3 SINGER
			}	
			
			sql += 0 + " , ";  // ZSCA
			sql += song.getId() + " , ";  // ZSORT
			sql += song.getLanguageID() + " , ";
			sql += 1;  // ZTYPEID - The loai tre trung
			sql += " )";
		
//			MyLog.d("DBInstance processInsertIntoSongTable", "sql = " + sql);			
			db.execSQL(sql); 
			
			sql = "INSERT INTO " + DbHelper.TABLE_SONGMUSICIAN + " VALUES ";
			sql += " (";
			sql += song.getId() + " , "; // Z_SOPK
			sql += 0 + " "; // no musician
			sql += " )";			
			db.execSQL(sql); 
			
			String singerName = song.getSingerName().trim();			
			if(singerName.equals("") || singerName.equals("-")){
				sql = "INSERT INTO " + DbHelper.TABLE_SONGSINGER + " VALUES ";
				sql += " (";
				sql += song.getId() + " , "; // Z_SOPK
				sql += 0 + " ";
				sql += " )";			
				db.execSQL(sql);
			} else {
				if(!singerName.contains(",")){
					ArrayList<Singer> listSinger = searchSingerFromName(StringUtils.getRawString(singerName, LANG_INDEX.ALL_LANGUAGE));
//					MyLog.e(" ", "listSinger = " + listSinger.size());
					if(listSinger.size() > 0){
						sql = "INSERT INTO " + DbHelper.TABLE_SONGSINGER + " VALUES ";
						sql += " (";
						sql += song.getId() + " , "; // Z_SOPK
						sql += listSinger.get(0).getID() + " ";
						sql += " )";			
						db.execSQL(sql); 
					} else {
						sql = "INSERT INTO " + DbHelper.TABLE_SONGSINGER + " VALUES ";
						sql += " (";
						sql += song.getId() + " , "; // Z_SOPK
						sql += 0 + " ";
						sql += " )";			
						db.execSQL(sql); 
					}
				} else {
					String[] listName = singerName.split(",");
					boolean everInsertZero = false;
					for (int i = 0; i < listName.length; i++) {
						ArrayList<Singer> listSinger = searchSingerFromName(StringUtils.getRawString(listName[i], LANG_INDEX.ALL_LANGUAGE));
						if(listSinger.size() > 0){
							sql = "INSERT INTO " + DbHelper.TABLE_SONGSINGER + " VALUES ";
							sql += " (";
							sql += song.getId() + " , "; // Z_SOPK
							sql += listSinger.get(0).getID() + " ";
							sql += " )";			
							db.execSQL(sql);
						} else {
							if(everInsertZero == false){
								everInsertZero = true;
								sql = "INSERT INTO " + DbHelper.TABLE_SONGSINGER + " VALUES ";
								sql += " (";
								sql += song.getId() + " , "; // Z_SOPK
								sql += 0 + " ";
								sql += " )";			
								db.execSQL(sql); 
							}							
						}
					}
				}
			}
			
		}
		
		db.setTransactionSuccessful();
		db.endTransaction();
	}
	
	public void processDeleteIntoSongTable(ArrayList<Integer> listDelete){
		db.beginTransactionNonExclusive();
		
		String strIDs = "";
		for (int i = 0; i < listDelete.size(); i++) {
			strIDs += listDelete.get(i) + ",";
		}
		
		if(strIDs.endsWith(",")){
			strIDs = strIDs.substring(0, strIDs.length() - 1);
		}
		
		String sql = "DELETE FROM " + DbHelper.TABLE_SONGS + " WHERE ";
		sql += DbHelper.SONG_ID;
		sql += " IN ";
		sql += " ( ";
		sql += strIDs;
		sql += " ) ";
		MyLog.d("DBInstance processDeleteIntoSongTable", "sql = " + sql);	
		db.execSQL(sql); 		
		
		sql = "DELETE FROM " + DbHelper.TABLE_SONGMUSICIAN + " WHERE ";
		sql += DbHelper.SONGMUSICIAN_SOPK;
		sql += " IN ";
		sql += " ( ";
		sql += strIDs;
		sql += " ) ";
		db.execSQL(sql); 		
		
		sql = "DELETE FROM " + DbHelper.TABLE_SONGSINGER + " WHERE ";
		sql += DbHelper.SONGSINGER_SOPK;
		sql += " IN ";
		sql += " ( ";
		sql += strIDs;
		sql += " ) ";			
		db.execSQL(sql); 
		
		db.setTransactionSuccessful();
		db.endTransaction();
	}
	
	private void filterWithMediaType(StringBuilder strBuilder, MEDIA_TYPE mediaType, String tableSong){
		if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
			if (mediaType == MEDIA_TYPE.MIDI) {
				DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K=%@ OR %K=%@)", 
						DBQueryBuilder.getColumnString(tableSong,DbHelper.SONG_MEDIATYPE), MEDIA_TYPE.MIDI.ordinal(),
						DBQueryBuilder.getColumnString(tableSong,DbHelper.SONG_MEDIATYPE), MEDIA_TYPE.MP3.ordinal());
			} else if (mediaType == MEDIA_TYPE.VIDEO) {
				DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K!=%@ AND %K!=%@)", 
						DBQueryBuilder.getColumnString(tableSong,DbHelper.SONG_MEDIATYPE), MEDIA_TYPE.MIDI.ordinal(),
						DBQueryBuilder.getColumnString(tableSong,DbHelper.SONG_MEDIATYPE), MEDIA_TYPE.MP3.ordinal());
			}
		} else {
			if (mediaType == MEDIA_TYPE.MIDI) {
				DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K=%@)",
						DBQueryBuilder.getColumnString(tableSong,DbHelper.SONG_MEDIATYPE), MEDIA_TYPE.MIDI.ordinal());
			} else if (mediaType == MEDIA_TYPE.VIDEO) {
				DBQueryBuilder.appendFromFormat(strBuilder, "AND (%K!=%@)",
						DBQueryBuilder.getColumnString(tableSong,DbHelper.SONG_MEDIATYPE), MEDIA_TYPE.MIDI.ordinal()); 
			}
		}
		
	}
	
	private void filterWithHiddenSK90xx(StringBuilder strBuilder, String tableSong){
		if(MyApplication.intSvrModel == MyApplication.SONCA && MyApplication.intSvrCode >= MyApplication.newAPI90xxVersion){
			if(MyApplication.flagInsideHidden == false){
				String strList = MyApplication.generateSQLListHidden90xx();
				if(!strList.equals("")){
					DBQueryBuilder.appendFromFormat(strBuilder, " AND (%K NOT IN %@)", 
							DBQueryBuilder.getColumnString(tableSong, DbHelper.SONG_ID),
							strList);
				}
				return;
			}
			
			if(MyApplication.flagInsideHidden && MyApplication.intStateHiddenFilter == ViewSearchAdd.HIDDEN_HID){
				String strList = MyApplication.generateSQLListHidden90xx();
				if(!strList.equals("")){
					DBQueryBuilder.appendFromFormat(strBuilder, " AND (%K IN %@)", 
							DBQueryBuilder.getColumnString(tableSong, DbHelper.SONG_ID),
							strList);
				} else {
					DBQueryBuilder.appendFromFormat(strBuilder, " AND (%K = %@)", 
							DBQueryBuilder.getColumnString(tableSong, DbHelper.SONG_ID),
							-99);
				}
				return;
			}
			
		}
	}
	
	public ArrayList<Singer> searchSingerFromName(String literal)
	{
		StringBuilder strBuilder = new StringBuilder(); 				
		DBQueryBuilder.appendSelect(strBuilder); 
		DBQueryBuilder.appendColumns(strBuilder, DbHelper.TABLE_SINGER, new String[]{DbHelper.SINGER_ID, DbHelper.SINGER_RAWNAME});
		DBQueryBuilder.appendTables(strBuilder, "MAIN" + "].[" + DbHelper.TABLE_SINGER); 
		DBQueryBuilder.appendWhereFromFormat(strBuilder, "(%K LIKE %@)", DBQueryBuilder.getColumnString(DbHelper.TABLE_SINGER, DbHelper.SINGER_RAWNAME), literal);
		DBQueryBuilder.appendGroupBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SINGER, DbHelper.SINGER_ID)); 
		
//		MyLog.i(" ", strBuilder.toString());
		
		Cursor resultCursor = db.rawQuery(strBuilder.toString(), null); 		
		
		ArrayList<Singer> arr = new ArrayList<Singer>();
		while (resultCursor.moveToNext()) {
			Singer aSinger = new Singer();
			aSinger.setID(resultCursor.getInt(resultCursor.getColumnIndex(DbHelper.SINGER_ID)));
			arr.add(aSinger);
		}
		resultCursor.close();
		
		return arr; 
	}
	
}
