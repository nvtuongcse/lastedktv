package vn.com.sonca.Touch.touchcontrol;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.params.Song;
import vn.com.sonca.utils.AppConfig;

public class TouchTestSongInter {
	
	static TouchTestSong dbInstance; // = DBInstance.getInstance(null);
	static Context mContext; 
	public static String[] AlphabetData = new String[]{"-","\"","0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	public static ArrayList<String> Alphabet;

	public static final int SONG_DEFAULT_MODE = 0;  
	public static final int SONG_HDD_MODE = 10; 
	public static final int SONG_NEWVOL_MODE = 11; 
	public static final int SONG_SINGER_MODE = 12; 
	
	public static int curLangID = -1; 
	public static int curSongMode = SONG_DEFAULT_MODE; 
	public static String dbName = ""; 
	
	public TouchTestSongInter(Context context)
	{
		if (context != null) {
			mContext = context; 
			dbInstance = TouchTestSong.getInstance(mContext, dbName); 
		}
	}
	
	public static void DBSetDatabaseName(Context context, String name)
	{
		dbName = name; 
	}
	
	public static void DBCloseDatabase(Context context)
	{
		dbInstance = TouchTestSong.getInstance(context, dbName); 
		try {
			dbInstance.closePermanent(); 
			dbName = ""; 
		}catch (Exception ex)
		{
			
		}
	}

 	public static Song DBSearchSongID(String idStr, Context context) {
		dbInstance = TouchTestSong.getInstance(context, dbName);
		Song cursor = null;
		try {
			dbInstance.open(); 	
			cursor = dbInstance.searchSongID(idStr); 
		} catch (Exception e) {
//			Log.e(AppConfig.DBDebugTag, "DBSearchSongID"); 
//			Log.e(AppConfig.DBDebugTag, e.getLocalizedMessage()); 
		} finally {
			dbInstance.close(); 
		}
		return cursor; 
	}
 	
	public static Song CheckIDSong(Context context, int id){
		dbInstance = TouchTestSong.getInstance(context, dbName);
		Song song = null;
		try {
			boolean bool = dbInstance.CheckIDSong(id);
			if (bool == true) {
				song = new Song();
				song.setId(id);
				return song;
			} else {
				return null;
			}	
		} catch (Exception e) {
//			MyLog.e("Loi Database", e); 
		} finally {
			dbInstance.close(); 
		}
		return song;
	}
	
}
