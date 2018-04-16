package vn.com.sonca.database;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import vn.com.sonca.utils.AppConfig;
import vn.com.sonca.utils.AppConfig.LANG_INDEX;
import vn.com.sonca.utils.AppConfig.MEDIA_TYPE;
import vn.com.sonca.utils.FileManager;
import vn.com.sonca.utils.XmlUtils;
import vn.com.sonca.zzzzz.MyApplication;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.database.DBInstance;
import vn.com.sonca.database.DBInstance.SearchMode;
import vn.com.sonca.database.DBInstance.SearchType;
//import vn.com.sonca.database.DBInstance.TOC_TYPE;
import vn.com.sonca.params.Language;
import vn.com.sonca.params.Musician;
import vn.com.sonca.params.Singer;
import vn.com.sonca.params.Song;
import vn.com.sonca.params.SongType;

public class DBInterface {
	
	static DBInstance dbInstance; // = DBInstance.getInstance(null);
	static Context mContext; 
	public static String[] AlphabetData = new String[]{"-","\"","0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	public static ArrayList<String> Alphabet;

	public static final int SONG_DEFAULT_MODE = 0;  
	public static final int SONG_HDD_MODE = 10; 
	public static final int SONG_NEWVOL_MODE = 11; 
	public static final int SONG_SINGER_MODE = 12; 
	
//	public static int curLangID = -1; 
	public static LANG_INDEX curLangID = LANG_INDEX.ALL_LANGUAGE; 
	public static int curSongMode = SONG_DEFAULT_MODE; 
	public static String dbName = ""; 
	
	public DBInterface(Context context)
	{
		if (context != null) {
			mContext = context; 
			dbInstance = DBInstance.getInstance(mContext, dbName); 
		}
	}
	
	
	public static void DBReloadDatabase(Context context, String name, int toctype, int tocHDDVol, int tocHDDType, int indexType)
	{
		MyLog.e("DBReloadDatabase",".................toctype:" + toctype);
		MyLog.e("",".................tocname:" + name);
		MyLog.e("",".................tocHDDVol:" + tocHDDVol);
		MyLog.e("",".................tocHDDType:" + tocHDDType);
		MyLog.e("",".................indexType:" + indexType);
		DBCloseDatabase(context); 

		dbName = name; 

		DBInitializeDatabase(context); 
		dbInstance.setTOCType(toctype); 
		dbInstance.setHDDToc(tocHDDVol, tocHDDType, indexType);
	}
	
	public static void DBSetDatabaseName(Context context, String name)
	{
		dbName = name; 
	}
	
	public static void DBCloseDatabase(Context context)
	{
		dbInstance = DBInstance.getInstance(context, dbName); 
		try {
			dbInstance.closePermanent(); 
			dbName = ""; 
			dbInstance = null; 
		}catch (Exception ex)
		{
			
		}
	}

	public static void DBSetCurrentLanguageID(LANG_INDEX langID) 
	{
//		if(langID == SONG_HDD_MODE || langID == SONG_NEWVOL_MODE || langID ==SONG_SINGER_MODE) {
//			curSongMode = langID; 
//		}else {
			curSongMode = SONG_DEFAULT_MODE; 
			DBInterface.curLangID = langID; 
//		}
	}
	
	public static boolean DBExtHDDAvailable() {
		return DBInstance.isHDDAvailable(); 
	}
	
	public static void DBAddHDDStoreWithPath(String path, Context context) {
//		DBAddPersistentStoreWithPath(path, context); 
		DBInstance.setHDDAvailable(true); 
	}
	
	public static void DBRemoveHDDStore(String path, Context context) {
//		DBRemovePersistentStore(path, context); 
		DBInstance.setHDDAvailable(false); 
	}
	
	public static void DBAddPersistentStoreWithPath(String filePath, Context context) {
		dbInstance = DBInstance.getInstance(context, dbName);
		File aFile = new File(filePath); 
		String asName = aFile.getName(); 
		asName = asName.substring(0, asName.lastIndexOf(".")); 
		try {
			dbInstance.open(); 	
			dbInstance.attachNewDatabase(filePath, asName); 
		} catch (Exception e) {
//			Log.e(AppConfig.DBDebugTag, "addPersistentStoreWithPath"); 
//			Log.e(AppConfig.DBDebugTag, e.getLocalizedMessage()); 
		} finally {
			if(dbInstance != null){
				dbInstance.close();	
			} 
		}
	}
	
	public static ByteArrayInputStream DBGetSongAudioStream(String songIndex, Context context) {
		dbInstance = DBInstance.getInstance(context, dbName);
		ByteArrayInputStream audioStream = null; 
		try {
			dbInstance.open(); 	
			audioStream = dbInstance.getSongAudioStream(songIndex); 
		} catch (Exception e) {
//			Log.e(AppConfig.DBDebugTag, "DBGetSongAudioStream"); 
//			Log.e(AppConfig.DBDebugTag, e.getLocalizedMessage()); 
		} finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		return audioStream; 
	}
	
	public static ByteArrayInputStream DBGetSongLyricStream(String songIndex, Context context) {
		dbInstance = DBInstance.getInstance(context, dbName);
		ByteArrayInputStream lyricStream = null; 
		try {
			dbInstance.open(); 	
			lyricStream = dbInstance.getSongLyricStream(songIndex); 
		} catch (Exception e) {
//			Log.e(AppConfig.DBDebugTag, "DBGetSongLyricStream"); 
//			Log.e(AppConfig.DBDebugTag, e.getLocalizedMessage()); 
		} finally {
			if(dbInstance != null){
				dbInstance.close();	
			} 
		}
		return lyricStream; 
	}
	
	public static void DBRemovePersistentStore(String filePath, Context context) {
		dbInstance = DBInstance.getInstance(context, dbName);
		try {
			dbInstance.open(); 	
			File aFile = new File(filePath); 
			String asName = aFile.getName(); 
			asName = asName.substring(0, asName.lastIndexOf(".")); 
			dbInstance.detachDatabase(asName); 
		} catch (Exception e) {
//			Log.e(AppConfig.DBDebugTag, "removePersistentStore"); 
//			Log.e(AppConfig.DBDebugTag, e.getLocalizedMessage()); 
		} finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
	}
	
	public static boolean DBUpdateSongPlayCount(Song aSong, Context context) {
		dbInstance = DBInstance.getInstance(context, dbName);
		boolean result = false; 
		try {
			dbInstance.open(); 	
			result = dbInstance.updateSongPlayCount(aSong); 
		} catch (Exception e) {
//			Log.e(AppConfig.DBDebugTag, "DBUpdateSongPlayCount"); 
//			Log.e(AppConfig.DBDebugTag, e.getLocalizedMessage()); 
		} finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		return result; 
	}
	
	public static boolean DBSetFavouriteSong(String idStr, String typeABC, boolean isFavour, Context context) {
		dbInstance = DBInstance.getInstance(context, dbName);
		boolean result = false; 
		try {
			dbInstance.open(); 	
			result = dbInstance.setFavouriteSong(idStr, typeABC, isFavour); 
		} catch (Exception e) {
//			Log.e(AppConfig.DBDebugTag, "DBSetFavouriteSong"); 
//			Log.e(AppConfig.DBDebugTag, e.getLocalizedMessage()); 
		} finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		return result; 
	}
	
 	public static ArrayList<Song> DBSearchSongID(String idStr, String typeABC, Context context) {
		dbInstance = DBInstance.getInstance(context, dbName);
		ArrayList<Song>result = new ArrayList<Song>(); 
		try {
			dbInstance.open(); 	
			result = dbInstance.searchSongID(idStr, typeABC); 
		} catch (Exception e) {
//			Log.e(AppConfig.DBDebugTag, "DBSearchSongID"); 
//			Log.e(AppConfig.DBDebugTag, e.getLocalizedMessage()); 
		} finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		return result; 
	}
 	
 	public static ArrayList<Song> DBSearchSongID_YouTube(String idStr, String typeABC, Context context) {
		dbInstance = DBInstance.getInstance(context, dbName);
		ArrayList<Song>result = new ArrayList<Song>(); 
		try {
			dbInstance.open(); 	
			result = dbInstance.searchSongID_YouTube(idStr, typeABC); 
		} catch (Exception e) {
//			Log.e(AppConfig.DBDebugTag, "DBSearchSongID"); 
//			Log.e(AppConfig.DBDebugTag, e.getLocalizedMessage()); 
		} finally {
			if(dbInstance != null){
				dbInstance.close();	
			} 
		}
		return result; 
	}	
 	
 	public static ArrayList<Song> DBSearchSongIDList(String[] idStr, String[] typeABC, Context context) {
		dbInstance = DBInstance.getInstance(context, dbName);
		ArrayList<Song> result = new ArrayList<Song>(); 
		try {
			dbInstance.open(); 	
			for (int i=0; i<idStr.length; i++)
				result.add(dbInstance.searchSongID(idStr[i], typeABC[i]).get(0)); 
		} catch (Exception e) {
//			Log.e(AppConfig.DBDebugTag, "DBSearchSongID"); 
//			Log.e(AppConfig.DBDebugTag, e.getLocalizedMessage()); 
		} finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		return result; 
	}
 	
 	public static ArrayList<Song> DBSearchSongIDList(int[] idStr, int[] typeABC, Context context) {
		dbInstance = DBInstance.getInstance(context, dbName);
		ArrayList<Song> result = new ArrayList<Song>(); 
		try {
			dbInstance.open(); 	
			result = dbInstance.searchSongIDList(idStr, typeABC); 
		} catch (Exception e) {
//			Log.e(AppConfig.DBDebugTag, "DBSearchSongID"); 
//			Log.e(AppConfig.DBDebugTag, e.getLocalizedMessage()); 
		} finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		return result; 
	}
 	
 	public static ArrayList<Song> DBSearchSongWithLyric(String literal, SearchMode mode, int offset, int resultCount, Context context){
		dbInstance = DBInstance.getInstance(context, dbName);
		ArrayList<Song>result = new ArrayList<Song>(); 
		try {
			dbInstance.open(); 	
			result = dbInstance.searchSongWithLyricMode(literal, curLangID, mode, offset, resultCount);
		} catch (Exception e) {
			//Log.e(AppConfig.DBDebugTag, "DBSearchSong"); 
			//Log.e(AppConfig.DBDebugTag, e.getLocalizedMessage()); 
		} finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		return result;
	}
 	
 	public static ArrayList<Song> DBSearchSong(String literal, SearchMode mode, int offset, int resultCount, Context context){
		dbInstance = DBInstance.getInstance(context, dbName);
		ArrayList<Song>result = new ArrayList<Song>(); 
		try {
			dbInstance.open(); 	
			//Log.e("", "cr rfdsf: " + curSongMode); 
			if(curSongMode == SONG_HDD_MODE) {
				result = dbInstance.searchSongHDD(literal, mode, offset, resultCount); 
			}else if(curSongMode == SONG_SINGER_MODE) {
				result = dbInstance.searchSongSinger(literal, mode, offset, resultCount); 
			}else if(curSongMode == SONG_NEWVOL_MODE) {
				result = dbInstance.searchSongNewVol(literal, mode, offset, resultCount); 
			}else {
				result = dbInstance.searchSong(literal, curLangID, mode, offset, resultCount);
			}
		} catch (Exception e) {
			//Log.e(AppConfig.DBDebugTag, "DBSearchSong"); 
			//Log.e(AppConfig.DBDebugTag, e.getLocalizedMessage()); 
		} finally {
			if(dbInstance != null){
				dbInstance.close();	
			} 
		}
		return result;
	}
	
	public static ArrayList<Song> DBGetSongWithTypeID(SearchType type, String idStr, int offset, int resultCount, Context context) {
		dbInstance = DBInstance.getInstance(context, dbName);
		ArrayList<Song>result = new ArrayList<Song>(); 
		try {
			dbInstance.open(); 	
			if(curSongMode == SONG_HDD_MODE) {
				result = dbInstance.searchSongWithTypeIDHDD(type, idStr, offset, resultCount); 
			}else if(curSongMode == SONG_SINGER_MODE) {
				result = dbInstance.searchSongWithTypeIDSongSinger(type, idStr, offset, resultCount); 
			}else if(curSongMode == SONG_NEWVOL_MODE) {
				result = dbInstance.searchSongWithTypeIDNewVol(type, idStr, offset, resultCount); 
			}else {
				result = dbInstance.searchSongWithTypeID(type, idStr, offset, resultCount); 
			}
			
		} catch (Exception e) {
//			Log.e(AppConfig.DBDebugTag, "DBGetSongWithTypeID"); 
//			Log.e(AppConfig.DBDebugTag, e.getLocalizedMessage()); 
		} finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		return result;
	}
		
	public static ArrayList<Song> DBGetFreeSongList(int offset, int resultCount, Context context) {
		dbInstance = DBInstance.getInstance(context, dbName);
		ArrayList<Song>result = new ArrayList<Song>(); 
		try {
			dbInstance.open(); 	
			result = dbInstance.getFreeSongList(offset, resultCount); 
		} catch (Exception e) {
//			Log.e(AppConfig.DBDebugTag, "DBGetFreeSongList"); 
//			Log.e(AppConfig.DBDebugTag, e.getLocalizedMessage()); 
		} finally {
			if(dbInstance != null){
				dbInstance.close();	
			} 
		}
		return result;
	}
	
	public static ArrayList<Song> DBGetFavouriteSongList(int offset, int resultCount, Context context) {
		dbInstance = DBInstance.getInstance(context, dbName);
		ArrayList<Song>result = new ArrayList<Song>(); 
		try {
			dbInstance.open(); 	
			result = dbInstance.getFavouriteSongList(offset, resultCount); 
		} catch (Exception e) {
//			Log.e(AppConfig.DBDebugTag, "DBCreateAllTable"); 
//			Log.e(AppConfig.DBDebugTag, e.getLocalizedMessage()); 
		} finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		return result;
	}
	
	public static ArrayList<Singer> DBSearchSinger(String literal, LANG_INDEX language, SearchMode mode, int offset, int resultCount, Context context) {
		dbInstance = DBInstance.getInstance(context, dbName);
		ArrayList<Singer>result = new ArrayList<Singer>(); 
		try {
			dbInstance.open(); 	
			if(curSongMode == SONG_HDD_MODE) {
				result = dbInstance.searchSingerHDD(literal, curLangID, mode, offset, resultCount); 
			}else if(curSongMode == SONG_SINGER_MODE) {
				result = dbInstance.searchSingerSongSinger(literal, curLangID, mode, offset, resultCount); 
			}else if(curSongMode == SONG_NEWVOL_MODE) {
				result = dbInstance.searchSingerNewVol(literal, curLangID, mode, offset, resultCount); 
			}else {
				result = dbInstance.searchSinger(literal, language, mode, offset, resultCount); 
			}
		} catch (Exception e) {
//			Log.e(AppConfig.DBDebugTag, "DBSearchSinger"); 
//			Log.e(AppConfig.DBDebugTag, e.getLocalizedMessage()); 
		} finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		return result; 
	}
	
	public static ArrayList<Musician> DBSearchMusician(String literal, LANG_INDEX language, SearchMode mode, int offset, int resultCount, Context context) {
		dbInstance = DBInstance.getInstance(context, dbName);
		ArrayList<Musician>result = new ArrayList<Musician>(); 
		try {
			dbInstance.open(); 	
			if(curSongMode == SONG_HDD_MODE) {
				result = dbInstance.searchMusicianHDD(literal, curLangID, mode, offset, resultCount); 
			}else if(curSongMode == SONG_SINGER_MODE) {
				result = dbInstance.searchMusicianSongSinger(literal, curLangID, mode, offset, resultCount); 
			}else if(curSongMode == SONG_NEWVOL_MODE) {
				result = dbInstance.searchMusicianNewVol(literal, curLangID, mode, offset, resultCount); 
			}else {
				result = dbInstance.searchMusician(literal, language, mode, offset, resultCount); 
			}
		} catch (Exception e) {
//			Log.e(AppConfig.DBDebugTag, "DBSearchMusician"); 
//			Log.e(AppConfig.DBDebugTag, e.getLocalizedMessage()); 
		} finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}		
		return result; 
	}
	
	public static ArrayList<SongType> DBSearchSongType(String literal, SearchMode mode, int offset, int resultCount, Context context) {
		dbInstance = DBInstance.getInstance(context, dbName);
		ArrayList<SongType>result = new ArrayList<SongType>(); 
		try {
			dbInstance.open(); 	
			if(curSongMode == SONG_HDD_MODE) {
				result = dbInstance.searchSongTypeHDD(literal, curLangID, mode, offset, resultCount); 
			}else if(curSongMode == SONG_SINGER_MODE) {
				result  = dbInstance.searchSongTypeSongSinger(literal, curLangID, mode, offset, resultCount); 
			}else if(curSongMode == SONG_NEWVOL_MODE) {
				result = dbInstance.searchSongTypeNewVol(literal, curLangID, mode, offset, resultCount); 
			}else {
				result = dbInstance.searchSongType(literal, curLangID, mode, offset, resultCount); 
			}
		} catch (Exception e) {
//			Log.e(AppConfig.DBDebugTag, "DBSearchSongType"); 
//			Log.e(AppConfig.DBDebugTag, e.getLocalizedMessage()); 
		} finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		return result; 
	}
	
	public static ArrayList<Song> DBGetMostPlaySongList(int offset, int resultCount, Context context) {
		dbInstance = DBInstance.getInstance(context, dbName);
		ArrayList<Song>result = new ArrayList<Song>(); 
		try {
			dbInstance.open(); 	
			if(curSongMode == SONG_HDD_MODE) {
				
			}else if(curSongMode == SONG_SINGER_MODE) {
				
			}else if(curSongMode == SONG_NEWVOL_MODE) {
				
			}else {
				result = dbInstance.getMostPlaySongList(curLangID, offset, resultCount); 
			}
		} catch (Exception e) {
//			Log.e(AppConfig.DBDebugTag, "DBGetMostPlaySongList"); 
//			Log.e(AppConfig.DBDebugTag, e.getLocalizedMessage()); 
		} finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		return result;
	}
	
	public static ArrayList<Singer> DBGetMostPlaySingerList(int offset, int resultCount, Context context) {
		dbInstance = DBInstance.getInstance(context, dbName);
		ArrayList<Singer>result = new ArrayList<Singer>(); 
		try {
			dbInstance.open(); 	
			if(curSongMode == SONG_HDD_MODE) {
				
			}else if(curSongMode == SONG_SINGER_MODE) {
				
			}else if(curSongMode == SONG_NEWVOL_MODE) {
				
			}else {
				result = dbInstance.getMostPlaySingerList(curLangID, offset, resultCount); 
			}
		} catch (Exception e) {
//			Log.e(AppConfig.DBDebugTag, "DBGetMostPlaySingerList"); 
//			Log.e(AppConfig.DBDebugTag, e.getLocalizedMessage()); 
		} finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		return result;
	}
	
	public static ArrayList<Musician> DBGetMostPlayMusicianList(int offset, int resultCount, Context context) {
		dbInstance = DBInstance.getInstance(context, dbName);
		ArrayList<Musician>result = new ArrayList<Musician>(); 
		try {
			dbInstance.open(); 	
			if(curSongMode == SONG_HDD_MODE) {
				
			}else if(curSongMode == SONG_SINGER_MODE) {
				
			}else if(curSongMode == SONG_NEWVOL_MODE) {
				
			}else {
				result = dbInstance.getMostPlayMusicianList(curLangID, offset, resultCount); 
			}
		} catch (Exception e) {
//			Log.e(AppConfig.DBDebugTag, "DBGetMostPlayMusicianList"); 
//			Log.e(AppConfig.DBDebugTag, e.getLocalizedMessage()); 
		} finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		return result;
	}
	
	public static ArrayList<SongType> DBGetMostPlaySongTypeList(int offset, int resultCount, Context context) {
		dbInstance = DBInstance.getInstance(context, dbName);
		ArrayList<SongType>result = new ArrayList<SongType>(); 
		try {
			dbInstance.open(); 	
			if(curSongMode == SONG_HDD_MODE) {
				
			}else if(curSongMode == SONG_SINGER_MODE) {
				
			}else if(curSongMode == SONG_NEWVOL_MODE) {
				
			}else {
				result = dbInstance.getMostPlaySongTypeList(curLangID, offset, resultCount); 
			}
		} catch (Exception e) {
//			Log.e(AppConfig.DBDebugTag, "DBGetMostPlaySongTypeList"); 
//			Log.e(AppConfig.DBDebugTag, e.getLocalizedMessage()); 
		} finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		return result;
	}
	
	public static ArrayList<Language> DBSearchSongLanguage(String literal, SearchMode mode, int offset, int resultCount, Context context) {
		
		dbInstance = DBInstance.getInstance(context, dbName);
		ArrayList<Language>result = new ArrayList<Language>(); 
		try {
			dbInstance.open(); 	
			result = dbInstance.searchSongLanguage(literal, mode, offset, resultCount); 
		} catch (Exception e) {
//			Log.e(AppConfig.DBDebugTag, "DBSearchSongLanguage"); 
//			Log.e(AppConfig.DBDebugTag, e.getLocalizedMessage()); 
		} finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		return result; 
	}
	
	public static ArrayList<Language> DBGetSongLanguageWithID(String langID, Context context) {
		if(curSongMode == SONG_HDD_MODE || curSongMode == SONG_SINGER_MODE || curSongMode == SONG_NEWVOL_MODE)
			return DBSearchSongLanguage(context.getResources().getString(R.string.db_default_language), SearchMode.MODE_SIGNED, 0, 1, context); 
			
		dbInstance = DBInstance.getInstance(context, dbName);
		ArrayList<Language>result = new ArrayList<Language>(); 
		try {
			dbInstance.open(); 	
			result = dbInstance.searchSongLanguageID(langID); 
		} catch (Exception e) {
//			Log.e(AppConfig.DBDebugTag, "DBSearchSongLanguage"); 
//			Log.e(AppConfig.DBDebugTag, e.getLocalizedMessage()); 
		} finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		return result; 
	}
	
	public static void DBSetFreeSongList(String filePath, Context context) {
		dbInstance = DBInstance.getInstance(context, dbName);
		try {
			dbInstance.open();
			dbInstance.db.beginTransaction(); 
			InputStream inputStream = new FileInputStream(filePath);
			Document dataDOM = XmlUtils.convertToDocument(inputStream); 
			dataDOM.getDocumentElement().normalize(); 
			NodeList taskNodeList = dataDOM.getDocumentElement().getElementsByTagName("index"); 
			int len = taskNodeList.getLength(); 
			for (int i = 0; i < len; i++) {
				Node taskNode = taskNodeList.item(i); 
				String idStr = ""; 
				if(taskNode.getNodeType() == Node.ELEMENT_NODE){
					idStr = taskNode.getNodeValue(); 
				}
				dbInstance.setFreeSong(idStr, true); 
			}// end node task
			dbInstance.db.setTransactionSuccessful();
		} catch (Exception e) {
//			Log.e(AppConfig.DBDebugTag, "DBSetFreeSongList"); 
//			Log.e(AppConfig.DBDebugTag, e.getLocalizedMessage()); 
		}
		finally {
			if (dbInstance.db.isOpen()) {
			dbInstance.db.endTransaction(); 
			}
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
	}
	
	public static String getApplicationDatabasePath(Context context) {
		return context.getDatabasePath(DbHelper.DBName).getAbsolutePath(); 
	}

	public static boolean databaseFileIsExist(Context context) {
		String dbPath = getApplicationDatabasePath(context); 
		return FileManager.fileExistAtPath(dbPath); 
	}
	
	public static void DBDropAllTable(Context context) {
		dbInstance = DBInstance.getInstance(context, dbName); 
		try {
			dbInstance.open();
			dbInstance.dropAllTable();
		}
		catch (Exception e) {
//			Log.e(AppConfig.DBDebugTag, "DBCreateAllTable"); 
		}
		finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
	}
	
	public static void DBTruncateAllTable(Context context) {
		dbInstance = DBInstance.getInstance(context, dbName);
		try {
			dbInstance.open();
			dbInstance.truncateAllTable(); 
		}
		catch (Exception e) {
//			Log.e(AppConfig.DBDebugTag, "DBCreateAllTable"); 
		}finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
	}
	
	public static void DBInitializeDatabase(Context context)
    {
        dbInstance = DBInstance.getInstance(context, dbName);
        try
        {
            dbInstance.open();
//            File dataFile =  null;
            dbInstance.createAllTable();
/*
            //
            // Initialize song data database
            // Read from main database to get all song data file
            ArrayList<String> files = dbInstance.getAllSongDataFile();
            // File not exist
            if (files == null || files.size() <= 0)
            {
                // 1.1 Create song data file 
//                dataFile = context.getDatabasePath(DbHelper.DATA_DBName + DbHelper.DBExtension);
            	dataFile = FileManager.getExtStorageFilePath(DbHelper.DATA_DBName + DbHelper.DBExtension); 
                if (!dataFile.exists())
                {
                    dataFile.createNewFile(); 
                }

                dbInstance.attachNewDatabase(dataFile.getAbsolutePath(), DbHelper.DATA_DBName);
                dbInstance.createSongDataTable(DbHelper.DATA_DBName);

                //// 1.2 Update song data to database
                dbInstance.insertSongDataFile(DbHelper.DATA_DBName);
                dbInstance.detachDatabase(DbHelper.DATA_DBName);
            }
            // Attach these song data file to main database
            else 
            {
                for (String curFile : files)
                {
//                    dataFile = context.getDatabasePath(curFile + DbHelper.DBExtension);
                	dataFile = FileManager.getExtStorageFilePath(curFile + DbHelper.DBExtension); 
                }
            }
*/
            
            dbInstance.close();
        }
        catch (Exception e)
        {
//        	Log.e("", "DBInitializeDatabase Exception"); 
        }
        finally
        {
        	if(dbInstance != null){
				dbInstance.close();	
			}
        }
    }
	
	public static void DBCreateAllTable(Context context) {
		dbInstance = DBInstance.getInstance(context, dbName);
		try {
			dbInstance.open();
			dbInstance.createAllTable(); 
		}
		catch (Exception e) {
//			Log.e(AppConfig.DBDebugTag, "DBCreateAllTable"); 
		}finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
	}

	public static int DBCountTotalSong(Context context, int[]langIDs, String literal, SearchMode mode, MEDIA_TYPE mediaType)
	{
		int count = 0; 
		dbInstance = DBInstance.getInstance(context, dbName);
		try {
			dbInstance.open();
			count = dbInstance.countTotalSong(langIDs, literal, mode, mediaType); 
		}
		catch (Exception e) {
//			Log.e(AppConfig.DBDebugTag, "DBCountTotalSong"); 
		}finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		return count; 
	}
	
	public static int DBCountTotalSongFull(Context context, int[]langIDs, String literal, SearchMode mode, MEDIA_TYPE mediaType)
	{
		int count = 0; 
		dbInstance = DBInstance.getInstance(context, dbName);
		if(dbInstance == null){
			return count;
		}
		try {
			dbInstance.open();
			count = dbInstance.countTotalSongFull(langIDs, literal, mode, mediaType); 
		}
		catch (Exception e) {
//			Log.e(AppConfig.DBDebugTag, "DBCountTotalSong"); 
		}finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		return count; 
	}
	
	public static int DBCountTotalSong_YouTube(Context context, int[]langIDs, String literal, SearchMode mode, MEDIA_TYPE mediaType)
	{
		int count = 0; 
		dbInstance = DBInstance.getInstance(context, dbName);
		try {
			dbInstance.open();
			count = dbInstance.countTotalSong_YouTube(langIDs, literal, mode, mediaType); 
		}
		catch (Exception e) {
//			Log.e(AppConfig.DBDebugTag, "DBCountTotalSong"); 
		}finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		return count; 
	}
	
	public static int DBCountTotalSongTypeID_KM(Context context, String literal, SearchType type, MEDIA_TYPE mediaType, int typeID)
	{
		int count = 0; 
		dbInstance = DBInstance.getInstance(context, dbName);
		try {
			dbInstance.open();
			count = dbInstance.countTotalSongTypeID_KM(literal, type, mediaType, typeID);
		}
		catch (Exception e) {
//			Log.e(AppConfig.DBDebugTag, "DBCountTotalSongTypeID"); 
		}finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		return count; 
	}
	
	public static int DBCountTotalSongTypeID(Context context, String literal, SearchType type, MEDIA_TYPE mediaType, int typeID)
	{
		int count = 0; 
		dbInstance = DBInstance.getInstance(context, dbName);
		try {
			dbInstance.open();
			count = dbInstance.countTotalSongTypeID(literal, type, mediaType, typeID);
		}
		catch (Exception e) {
//			Log.e(AppConfig.DBDebugTag, "DBCountTotalSongTypeID"); 
		}finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		return count; 
	}
	
	public static Cursor DBGetSongCursor_YouTube(Context context, int[]langIDs, String literal, SearchMode mode, MEDIA_TYPE mediaType, int offset, int count)
	{
		Cursor cursor = null; 
		dbInstance = DBInstance.getInstance(context, dbName);
		try {
			dbInstance.open();
			cursor = dbInstance.getMySongCursor_YouTube(langIDs, literal, mode, mediaType, offset, count);
		}
		catch (Exception e) {
//			Log.e(AppConfig.DBDebugTag, "DBGetSongCursor"); 
		}finally {
			if(dbInstance != null){
				dbInstance.close();	
			} 
		}
		return cursor;
	}
	
	public static int DBCountTotalSongRemix(Context context, String literal,MEDIA_TYPE mediaType)
	{
		int count = 0; 
		dbInstance = DBInstance.getInstance(context, dbName);
		try {
			dbInstance.open();
			count = dbInstance.countTotalSongRemix(literal, mediaType); 
		}
		catch (Exception e) {
//			Log.e(AppConfig.DBDebugTag, "DBCountTotalSongRemix"); 
		}finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		return count; 
	}
	
	public static Cursor DBGetSongCursor(Context context, int[]langIDs, String literal, SearchMode mode, MEDIA_TYPE mediaType, int offset, int count)
	{
		Cursor cursor = null; 
		dbInstance = DBInstance.getInstance(context, dbName);
		try {
			dbInstance.open();
			
			if (literal != null && literal.trim().length() > 0) {
				cursor = dbInstance.getMySongCursor(langIDs, literal, mode, mediaType, offset, count);
			} else {
				if(MyApplication.flagNewSongTable){
					cursor = dbInstance.getSongCursor_New(langIDs, literal, mode, mediaType, offset, count);
				} else {
					cursor = dbInstance.getSongCursor(langIDs, literal, mode, mediaType, offset, count);	
				}
				
			}
		}
		catch (Exception e) {
//			Log.e(AppConfig.DBDebugTag, "DBGetSongCursor"); 
		}finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		return cursor;
	}
	
	public static ArrayList<Integer> DBGetAuthorIDFromSong(Context context, int songID, int typeABC)
	{
		ArrayList<Integer> result = new ArrayList<Integer>(); 
		dbInstance = DBInstance.getInstance(context, dbName);
		try {
			dbInstance.open();
			result = dbInstance.getAuthorIDFromSong(songID, typeABC); 
		}
		catch (Exception e) {
//			Log.e(AppConfig.DBDebugTag, "DBGetSongRemix"); 
		}finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		return result;
	}
	
	public static Cursor DBGetSongTypeIDCursor_KM(Context context, String literal, SearchType type, MEDIA_TYPE mediaType, int typeID, int offset, int count) {
		Cursor cursor = null; 
		dbInstance = DBInstance.getInstance(context, dbName);
		try {
			dbInstance.open();
			cursor = dbInstance.getSongTypeIDCursor_KM(literal, SearchMode.MODE_MIXED, type, mediaType, typeID, offset, count);
		}
		catch (Exception e) {
//			Log.e(AppConfig.DBDebugTag, "DBGetSongTypeIDCursor"); 
		}finally {
			if(dbInstance != null){
				dbInstance.close();	
			} 
		}
		return cursor;
	}
	
	public static Cursor DBGetSongTypeIDCursor(Context context, String literal, SearchType type, MEDIA_TYPE mediaType, int typeID, int offset, int count) {
		Cursor cursor = null; 
		dbInstance = DBInstance.getInstance(context, dbName);
		try {
			dbInstance.open();
			cursor = dbInstance.getSongTypeIDCursor(literal, SearchMode.MODE_MIXED, type, mediaType, typeID, offset, count);
		}
		catch (Exception e) {
//			Log.e(AppConfig.DBDebugTag, "DBGetSongTypeIDCursor"); 
		}finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		return cursor;
	}
	
	public static Cursor  DBGetSongRemix(Context context, String literal, MEDIA_TYPE mediaType, int offset, int sumData)
	{
		Cursor cursor = null; 
		dbInstance = DBInstance.getInstance(context, dbName);
		try {
			dbInstance.open();
			cursor = dbInstance.getSongRemix(literal, SearchMode.MODE_MIXED, mediaType, offset, sumData); 
		}
		catch (Exception e) {
//			Log.e(AppConfig.DBDebugTag, "DBGetSongRemix"); 
		}finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		return cursor;
	}
	
//////////////////////////////////////////////////
	
	public static ArrayList<Song> DBSearchListSongID(ArrayList<Song> playlist, Context context) {		
		dbInstance = DBInstance.getInstance(context, dbName);
		ArrayList<Song> result = new ArrayList<Song>(); 
		try {

			int[]idList = new int[playlist.size()]; 
			int[]typeABCList = new int[playlist.size()]; 
			for (int i = 0; i < playlist.size() ; i++) {
				idList[i] = playlist.get(i).getIndex5();
				typeABCList[i] = playlist.get(i).getTypeABC();
			}
			
			dbInstance.open(); 	
			result = dbInstance.searchSongIDList(idList, typeABCList); 
		} catch (Exception e) {
//			Log.e(AppConfig.DBDebugTag, "DBSearchSongID"); 
//			Log.e(AppConfig.DBDebugTag, e.getLocalizedMessage()); 
		} finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		return result;		
	}
	
	public static ArrayList<Song> DBSearchListSongIDPlusYoutube(ArrayList<Song> playlist, Context context) {
		int[]idList = new int[playlist.size()]; 
		int[]typeABCList = new int[playlist.size()]; 
		for (int i = 0; i < playlist.size() ; i++) {
			idList[i] = playlist.get(i).getIndex5();
			typeABCList[i] = playlist.get(i).getTypeABC();
		}
		
		dbInstance = DBInstance.getInstance(context, dbName);
		ArrayList<Song> resultList = new ArrayList<Song>(); 
		try {
			dbInstance.open(); 	
			
			ArrayList<Song> resultNormal = new ArrayList<Song>(); 
			resultNormal = dbInstance.searchSongIDList(idList, typeABCList); 
			
			ArrayList<Song> resultYoutube = new ArrayList<Song>(); 
			resultYoutube = dbInstance.searchSongIDList_YouTube(idList, typeABCList);	
						
			for(int i = 0; i < idList.length; i++) {
				boolean flagFound = false;
				
				for(int j = 0; j < resultNormal.size(); j++) {
					Song song = resultNormal.get(j); 
					if(MyApplication.intSvrModel == MyApplication.SONCA_HIW  || MyApplication.intSvrModel == MyApplication.SONCA_KM2
							|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
							|| MyApplication.intSvrModel == MyApplication.SONCA_KBX9 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
							|| MyApplication.intSvrModel == MyApplication.SONCA_TBT){
						if((song.getId() == idList[i] || song.getIndex5() == idList[i])) {
							resultList.add(song); 
							flagFound = true;
							break;
						}	
					} else {
						if((song.getId() == idList[i] || song.getIndex5() == idList[i]) && song.getTypeABC() == typeABCList[i]) {
							resultList.add(song);
							flagFound = true;
							break;
						}
					}
					
				}
				
				if(flagFound == false){
					for(int j = 0; j < resultYoutube.size(); j++) {
						Song song = resultYoutube.get(j); 
						if(song.getId() == idList[i] || song.getIndex5() == idList[i]) {
							resultList.add(song); 
							break;
						}
						
					}
					
				}
				
			}
			
		} catch (Exception e) {
//			Log.e(AppConfig.DBDebugTag, "DBSearchSongID"); 
//			Log.e(AppConfig.DBDebugTag, e.getLocalizedMessage()); 
		} finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		return resultList;		
	}
	
	public static String DBGetNameSinger(Context context, String idSinger){
		String str = "";
		
		dbInstance = DBInstance.getInstance(context, dbName);
		try {
			dbInstance.open();
			str = dbInstance.GetNameSinger(idSinger);
		}
		catch (Exception e) {
			
		}finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		
		return str;
		
	}
	
	public static String DBGetNameMusician(Context context, String idMusician){
		String str = "";
		
		dbInstance = DBInstance.getInstance(context, dbName);
		try {
			dbInstance.open();
			str = dbInstance.GetNameMusician(idMusician);
		}
		catch (Exception e) {
			
		}finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		
		return str;
		
	}
	
	public static Singer DBGetOneSinger(Context context, String idSinger){
		Singer singer = new Singer();
		
		dbInstance = DBInstance.getInstance(context, dbName);
		try {
			dbInstance.open();
			singer = dbInstance.GetOneSinger(idSinger);
		}
		catch (Exception e) {
			
		}finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		
		return singer;
		
	}
	
	public static Musician DBGetOneMusician(Context context, String idMusician){		
		Musician musician = new Musician();
		
		dbInstance = DBInstance.getInstance(context, dbName);
		try {
			dbInstance.open();
			musician = dbInstance.GetOneMusician(idMusician);
		}
		catch (Exception e) {
			
		}finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		
		return musician;
		
	}
	
	public static String DBGetNameSong(Context context, String idSong){
		String str = "";
		
		dbInstance = DBInstance.getInstance(context, dbName);
		try {
			dbInstance.open();
			str = dbInstance.GetNameSong(idSong);
		}
		catch (Exception e) {
			
		}finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		
		return str;
		
	}
	
	public static String DBGetNameSong_YouTube(Context context, String idSong){
		String str = "";
		
		dbInstance = DBInstance.getInstance(context, dbName);
		try {
			dbInstance.open();
			str = dbInstance.GetNameSong_YouTube(idSong);
		}
		catch (Exception e) {
			
		}finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		
		return str;
		
	}
	
	public static List<Integer> DBGetTotalTypeABCSong(Context context, String idSong, String name){		
		List<Integer> list = new ArrayList<Integer>();
		
		dbInstance = DBInstance.getInstance(context, dbName);
		try {
			dbInstance.open();
			list = dbInstance.GetTotalTypeABCSong(idSong, name);
		}
		catch (Exception e) {
			
		}finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		
		return list;
		
	}
	
	public static Cursor DBGetSongNumberCursor(Context context, String where, int count, int offset, MEDIA_TYPE mediaType){
		Cursor cursor = null;
		dbInstance = DBInstance.getInstance(context, dbName);
		try {
			dbInstance.open();
			if(MyApplication.intSvrModel == MyApplication.SONCA || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
				cursor = dbInstance.GetSongNumber(where, count, offset, mediaType);
			} else {
				cursor = dbInstance.GetSongNumber2(where, count, offset, mediaType);
			}
		}
		catch (Exception e) {
			
		}finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		
		return cursor;
		
	}
	
	public static int DBGetTtotalSongNumberCursor(Context context, String where, MEDIA_TYPE mediaType){		
		int count = 0;
		dbInstance = DBInstance.getInstance(context, dbName);
		try {
			dbInstance.open();
			count = dbInstance.GetTotalSongNumber(where, mediaType);	
		}
		catch (Exception e) {
			
		}finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		
		return count;
	}
	
	public static void DBProcessNewSongTable(Context context){
		dbInstance = DBInstance.getInstance(context, dbName);
		try {
			dbInstance.open();
			dbInstance.processNewSongTable();
		}
		catch (Exception e) {
			
		}finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		
	}
		
	public static void createNewIndexSong(Context context){		
		dbInstance = DBInstance.getInstance(context, dbName);
		try {
			dbInstance.open();
			dbInstance.createIndexSong(DbHelper.INDEX_SONGS, DbHelper.TABLE_SONGS);
		}
		catch (Exception e) {
			
		}finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		
	}	
	
	public static void DBProcessNewYoutubeTable(Context context){
		dbInstance = DBInstance.getInstance(context, dbName);
		try {
			dbInstance.open();
			dbInstance.processNewYoutubeTable();
		}
		catch (Exception e) {
			
		}finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		
	}
	
	public static void DBProcessInsertIntoYoutubeTable(Context context, List<Song> listData){
		dbInstance = DBInstance.getInstance(context, dbName);
		try {
			dbInstance.open();
			dbInstance.processInsertIntoYoutubeTable(listData);
		}
		catch (Exception e) {
			
		}finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		
	}
	
	public static void DBProcessInsertIntoSongTable(Context context, ArrayList<Song> listData){
		dbInstance = DBInstance.getInstance(context, dbName);
		try {
			dbInstance.open();
			dbInstance.processInsertIntoSongTable(listData);
		}
		catch (Exception e) {
			
		}finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		
	}
	
	public static void DBProcessDeleteIntoSongTable(Context context, ArrayList<Integer> listDelete){
		dbInstance = DBInstance.getInstance(context, dbName);
		try {
			dbInstance.open();
			dbInstance.processDeleteIntoSongTable(listDelete);
		}
		catch (Exception e) {
			
		}finally {
			if(dbInstance != null){
				dbInstance.close();	
			}
		}
		
	}
	
}
