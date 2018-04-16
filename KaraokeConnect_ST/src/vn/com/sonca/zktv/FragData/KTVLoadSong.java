package vn.com.sonca.zktv.FragData;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import vn.com.sonca.LoadSong.BaseLoadSong;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.Language.LanguageStore;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.database.DBInterface;
import vn.com.sonca.database.DbHelper;
import vn.com.sonca.database.DBInstance.SearchMode;
import vn.com.sonca.database.DBInstance.SearchType;
import vn.com.sonca.params.Song;
import vn.com.sonca.utils.AppConfig.MEDIA_TYPE;
import vn.com.sonca.zzzzz.MyApplication;

public class KTVLoadSong extends BaseLoadSong {

	private String TAB = "KTVLoadSong";
	private final int SUM = 6;
	private Context context;
	private String where;
	private String type;
	private MEDIA_TYPE typeMedia;
	private int intPageNumber;
	private SearchMode searchMode = SearchMode.MODE_MIXED;
	public KTVLoadSong(int IdThread, Context context, String where, 
		ArrayList<Song> listSongFragment, String type, int intPageNumber) {
		super(IdThread, context, where, listSongFragment);
		typeMedia = MEDIA_TYPE.ALL;
		this.intPageNumber = intPageNumber;
		this.context = context;
		this.where = where;
		this.type = type;
	}
	
	public int getPageNumber(){
		return intPageNumber;
	}

	@Override
	protected void OnRunLoad() throws Exception {
		Cursor cursor = DBSearch(type, "" + DbHelper.SongType_Remix , 
				where, intPageNumber*SUM, SUM);
		if (isCancel()) {
			cursor.close();
			cursor = null;
			setRunning(false);
			return;
		}
		if (cursor != null) {
			if (moveToFirstCursor(cursor)) {
				ArrayList<Song> list = new ArrayList<Song>();
				for (int i = 0; i < SUM; i++) {
					if (isCancel()) {
						cursor.close();
						cursor = null;
						publishProgress(FINISH);
						setRunning(false);
						return;
					}
					Song song = new Song();
					parseSongInfoQueryResult(song, cursor);
					list.add(song);
					if (!cursor.moveToNext()) {
						break;
					}
				}
				cursor.close();
				cursor = null;
				setListSong(list);
				publishProgress(START);
			} else {
				cursor.close();
				cursor = null;
			}
		}else{}
	}
	
	private Cursor DBSearch(String type , String id , String where, int offset , int sum) {
		// return DBInterface.DBGetSongCursor(context, intIDs, where, searchMode, typeMedia,offset, sum);
		return DBInterface.DBGetSongRemix(context, where, typeMedia,offset, sum);
	}

}
