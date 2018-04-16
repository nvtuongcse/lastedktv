package vn.com.sonca.LoadSong;

import java.util.ArrayList;
import java.util.Arrays;

import vn.com.sonca.database.DBInterface;
import vn.com.sonca.database.ItemLoad;
import vn.com.sonca.database.ManagerLoad;
import vn.com.sonca.database.DBInstance.SearchMode;
import vn.com.sonca.database.DBInstance.SearchType;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.CustomView.TouchSearchView;
import vn.com.sonca.params.Musician;
import vn.com.sonca.params.Singer;
import vn.com.sonca.params.Song;
import vn.com.sonca.utils.Lock;
import vn.com.sonca.utils.PinyinHelper;
import vn.com.sonca.utils.AppConfig.MEDIA_TYPE;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

public class LoadRemixFromDatabase extends BaseLoadSong {
	
	private Context context;
	private String where;
	private MEDIA_TYPE typeMedia;
	
	public LoadRemixFromDatabase(String where, ArrayList<Song> listSongs, 
			int state2 , Context context) {
		super(0, context, where, listSongs);
		this.where = where;		
		switch (state2) {
		case TouchSearchView.VIDEO:	typeMedia = MEDIA_TYPE.VIDEO;		break;
		case TouchSearchView.MIDI:	typeMedia = MEDIA_TYPE.MIDI;		break;
		case TouchSearchView.TATCA:	typeMedia = MEDIA_TYPE.ALL;			break;
		default: typeMedia = MEDIA_TYPE.ALL; break;
		}
	}
	
///////////////////////// - QUERY - ////////////////////////////

	private Cursor DBSearch(String where, int offset, int sumData) {
		Cursor cursor = DBInterface.DBGetSongRemix(context, where , typeMedia, offset, sumData);
//		MyLog.e(TAB, "DBSearch - " + cursor.getCount());
		return cursor;
	}

	private int TotalDBSearch(String where) {
		int count = DBInterface.DBCountTotalSongRemix(context, where , typeMedia);
//		MyLog.e(TAB, "TotalDBSearch - " + count);
		return count;
	}
	
///////////////////////// - THREAD - ////////////////////////////
	
	@Override
	protected void OnRunLoad() throws Exception {
		int searchLength = 0;
		if(where != null && where.trim().length() > 0){
			searchLength = where.trim().length();
		}
		
		int tempRowStart = BaseLoadSong.ROWSTART;
		
		switch (searchLength) {
		case 0:
			tempRowStart = BaseLoadSong.ROWSTART;
			break;
		case 1:
			tempRowStart = BaseLoadSong.ROWSTART_1CHAR;
			break;
		case 2:
			tempRowStart = BaseLoadSong.ROWSTART_2CHAR;
			break;
		default:
			tempRowStart = BaseLoadSong.ROWSTART_3CHAR;
			break;
		}
		
		Cursor cursor = DBSearch(where, 0, tempRowStart);
		if (isCancel()) {
			cursor.close();
			cursor = null;
			setRunning(false);
			return;
		}
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				ArrayList<Song> list = new ArrayList<Song>();
				for (int i = 0; i < tempRowStart; i++) {
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
			} else {
				cursor.close();
				cursor = null;
			}
		}
		publishProgress(START);

		// -----------------------------------//

		if (checkSize() < tempRowStart) {
			MyLog.i(TAB, "Full Data : " + checkSize());
			publishProgress(FINISH);
			setRunning(false);
			return;
		} else {
			MyLog.i(TAB, "Start Loaded : " + checkSize());
			waitLoad();
			if (isCancel()) {
				publishProgress(FINISH);
				setRunning(false);
				return;
			}
			MyLog.i(TAB, "Loading....");
			int countPage = 0;
			
			if(where != null && where.trim().length() > 0){
				MyLog.e(TAB, "Loading STYLE NEW");
				publishProgress(LOADING_FULL);

				cursor = DBSearch(where, 0, 0);
				cursor.moveToPosition(tempRowStart);

				publishProgress(LOADING_FIN);

				if (isCancel()) {
					cursor.close();
					cursor = null;
					publishProgress(FINISH);
					setRunning(false);
					return;
				}

				while (!isCancel()) {
					countPage++;
					if (cursor != null) {
//						if(countPage > 1){
//							publishProgress(LOADING_NEXT);	
//						}	
						if (cursor.moveToPosition(tempRowStart + (countPage - 1)
								* ROWNEXT)) {
							ArrayList<Song> list = new ArrayList<Song>();
							for (int i = 0; i < ROWNEXT; i++) {
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

							setListSong(list);
							publishProgress(LOADING);
						} else {
							cursor.close();
							cursor = null;
						}
					}

					if (checkSize() < ROWNEXT) {
						MyLog.i(TAB, "Full Data : " + checkSize());
						publishProgress(FINISH);
						setRunning(false);
						return;
					} else {
						MyLog.i(TAB, "Next Loaded : " + checkSize());
					}
					waitLoad();
					if (isCancel()) {
						publishProgress(FINISH);
						setRunning(false);
						return;
					}
					MyLog.i(TAB, "Loading....");
				}
			} else {
				MyLog.e(TAB, "Loading STYLE OLD");
				while (!isCancel()) {
					cursor = DBSearch(where, tempRowStart + countPage * ROWNEXT, ROWNEXT);
					if (isCancel()) {
						cursor.close();
						cursor = null;
						publishProgress(FINISH);
						setRunning(false);
						return;
					}
					countPage++;
					if (cursor != null) {
						if (cursor.moveToFirst()) {
							ArrayList<Song> list = new ArrayList<Song>();
							for (int i = 0; i < ROWNEXT; i++) {
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
							publishProgress(LOADING);
						} else {
							cursor.close();
							cursor = null;
						}
					}
					if (checkSize() < ROWNEXT) {
						publishProgress(LOADING);
						MyLog.i(TAB, "Full Data : " + checkSize());
						break;
					} else {
						MyLog.i(TAB, "Next Loaded : " + checkSize());
					}
					waitLoad();
					if (isCancel()) {
						publishProgress(FINISH);
						setRunning(false);
						return;
					}
					MyLog.i(TAB, "Loading....");
				}
			}
		}
	}
	
}
