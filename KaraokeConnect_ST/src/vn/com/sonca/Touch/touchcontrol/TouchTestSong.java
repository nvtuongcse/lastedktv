package vn.com.sonca.Touch.touchcontrol;

import java.util.ArrayList;

import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.database.DBQueryBuilder;
import vn.com.sonca.database.DbHelper;
import vn.com.sonca.params.Singer;
import vn.com.sonca.params.Song;
import vn.com.sonca.utils.AppConfig.MEDIA_TYPE;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TouchTestSong {
	
	public static final String HANDLER_MESSAGE_ID = "id";
	public static final String SPECIAL_CHAR = "-";	
	public enum SearchType {SEARCH_SONG, SEARCH_MUSICIAN, SEARCH_SINGER, SEARCH_TYPE, SEARCH_NUMBER}; 
	public enum SearchMode {MODE_PINYIN, MODE_FULL, MODE_SIGNED}; 
	
	private static boolean isHDDAvailable; 
	private static String HDDDBName = DbHelper.HDD_DBName; 
	
	private static TouchTestSong instance;
	private static DbHelper openHelper;
	public  SQLiteDatabase db;
	private Context mContext;
	private String curDBName; 
	
	
	private TouchTestSong( SQLiteDatabase ins) {
		db = ins;
		if(curDBName == null || curDBName.equals("")){
			curDBName = DbHelper.DBName; 
		}
	}
	
	private TouchTestSong(Context context, String dbName) {
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
			db = SQLiteDatabase.openDatabase(mContext.getDatabasePath(curDBName).getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE); 
		} catch (Exception e) {
//			Log.e("OPEN DB ERROR", e.toString());
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
	public static TouchTestSong getInstance(Context context, String dbName) {
		if(instance == null) {
			instance = new TouchTestSong(context, dbName);
		} 
		return instance;
	}
	
	public static boolean isHDDAvailable() {
		return isHDDAvailable;
	}

	public static void setHDDAvailable(boolean isHDDAvailable) {
		TouchTestSong.isHDDAvailable = isHDDAvailable;
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
	
	private Song parseSongInfoQueryResult(Cursor cursor) {
		if(cursor.moveToNext()){
			Song song = new Song();
			song.setId(cursor.getInt(0));
			song.setIndex5(cursor.getInt(1));
			song.setName(cursor.getString(2));
	//		song.setShortName(cursor.getString(3));
	//		song.setTitleRaw(cursor.getString(4));
			song.setLyric(cursor.getString(3));
			song.setMediaType(MEDIA_TYPE.values()[cursor.getInt(4)]);
			song.setRemix(cursor.getInt(5) == 1);
			song.setFavourite(cursor.getInt(6) == 1);
			song.setTypeABC(cursor.getInt(7));
	//		song.setSingerId(cursor.getInt(6));
			/*
			song.setSinger(new Singer(cursor.getString(8))); 
			String musicianID = cursor.getString(9); 
			String[] idxs = musicianID.split(","); 
			int[] idxInt = new int[idxs.length]; 
			for(int i =0; i < idxInt.length; i++)
			{
				idxInt[i] = Integer.parseInt(idxs[i]); 
			}
			song.setMusicianId(idxInt);
			*/
			cursor.close();
			cursor = null;
			return song;
		}else{
			cursor.close();
			cursor = null;
			return null;
		}
	}
	
	private void createSongInfoQueryString(StringBuilder sb, String dbName)
	{
		if(dbName == null || dbName.equals("")) 
			dbName = "MAIN"; 
		
		DBQueryBuilder.appendSelect(sb); 
		DBQueryBuilder.appendColumns(sb, DbHelper.TABLE_SONGS, new String[]{DbHelper.SONG_ID, DbHelper.SONG_ID5, DbHelper.SONG_NAME/*,DbHelper.SONG_SHORTNAME, DbHelper.SONG_RAWNAME*/, DbHelper.SONG_LYRIC, 
				DbHelper.SONG_MEDIATYPE, DbHelper.SONG_REMIX, DbHelper.SONG_FAVOUR});
		sb.append(","); 
		sb.append("group_concat(" + DBQueryBuilder.getColumnString(DbHelper.TABLE_MUSICIAN, DbHelper.MUSICIAN_NAME) + ")"); 
		DBQueryBuilder.appendTables(sb, dbName + "].[" + DbHelper.TABLE_SONGS); 
		DBQueryBuilder.appendInnerJoin(sb, dbName + "].[" + DbHelper.TABLE_SONGMUSICIAN,  
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_ID)
				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMUSICIAN, DbHelper.SONGMUSICIAN_SOPK)); 
		DBQueryBuilder.appendInnerJoin(sb, dbName + "].[" + DbHelper.TABLE_MUSICIAN, 
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGMUSICIAN, DbHelper.SONGMUSICIAN_ATPK)
				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_MUSICIAN, DbHelper.MUSICIAN_ID)); 
		
		DBQueryBuilder.appendInnerJoin(sb, dbName + "].[" + DbHelper.TABLE_SONGSINGER,  
				DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_ID)
				+ "=" + DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGSINGER, DbHelper.SONGSINGER_SOPK)); 
	}
	

	public Song searchSongID(String idStr) { 
		int id = 0; 
		try {
			id = Integer.parseInt(idStr); 
			if (id <= 0) 
			{
				return null; 
			}
		} catch (Exception e) {
//			Log.e("", "searchSongID" + idStr); 
			return null; 
		}
		
		StringBuilder strBuilder = new StringBuilder(); 
		
		createSongInfoQueryString(strBuilder, ""); 
		
		if(idStr != null && !idStr.equals(""))
		{
			DBQueryBuilder.appendWhereFromFormat(strBuilder, "%K=%@ OR %K=%@", DbHelper.SONG_ID, id, DbHelper.SONG_ID5, id); 
		}
		if(isHDDAvailable){
			String hddPath = mContext.getDatabasePath(DbHelper.HDD_DBName + DbHelper.DBExtension).getAbsolutePath(); 
			attachNewDatabase(hddPath, HDDDBName); 
			
			strBuilder.append(" UNION ALL "); 
			
			createSongInfoQueryString(strBuilder, HDDDBName); 
		
			if(idStr != null && !idStr.equals(""))
			{
				DBQueryBuilder.appendWhereFromFormat(strBuilder, "%K=%@ OR %K=%@", DbHelper.SONG_ID, id, DbHelper.SONG_ID5, id); 
			}
		}
		
		DBQueryBuilder.appendGroupBy(strBuilder, DBQueryBuilder.getColumnString(DbHelper.TABLE_SONGS, DbHelper.SONG_SORT));
		DBQueryBuilder.appendLimitOffset(strBuilder, 1, 0); 
		Cursor cursorResult = db.rawQuery(strBuilder.toString(), null); 
		
		Song songs = parseSongInfoQueryResult(cursorResult); 
		
		if(isHDDAvailable){
			detachDatabase(HDDDBName); 
		}
		return songs;
		
	}
	
	public boolean CheckIDSong(int id){
		String where = "SELECT 1 FROM ZSONGS WHERE ZID = " + id;
		Cursor cursor = db.rawQuery(where, null);
		boolean bool = false;
		if (cursor.moveToFirst()) {
			bool = true;
		} 
		cursor.close();
		cursor = null;
		return bool;
	}
	
}
