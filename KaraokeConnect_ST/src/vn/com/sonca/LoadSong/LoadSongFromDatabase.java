package vn.com.sonca.LoadSong;

import java.util.ArrayList;

import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.CustomView.TouchSearchView;
import vn.com.sonca.Touch.Language.LanguageStore;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.database.DBInstance.SearchMode;
import vn.com.sonca.database.DBInstance.SearchType;
import vn.com.sonca.database.DBInterface;
import vn.com.sonca.database.DbHelper;
import vn.com.sonca.params.Song;
import vn.com.sonca.utils.AppConfig.MEDIA_TYPE;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

public class LoadSongFromDatabase extends BaseLoadSong {
	
	private Context context;
	private String where;
	private String type;
	private String id;
	private int IdThread;
	private SearchMode searchMode = SearchMode.MODE_MIXED;
	private MEDIA_TYPE typeMedia;
	
	private boolean flagFilterOnline = false;
	
	public LoadSongFromDatabase(String where, String type, String id, int state1, int state2, 
			ArrayList<Song> listSongFragment, Context context , TouchMainActivity mainActivity , int IdThread) {
		super(IdThread, context, where, listSongFragment);
		this.flagFilterOnline = false;
		this.context = context;
		this.where = where;
		this.type = type;
		this.id = id;
		this.IdThread = IdThread;
		switch (state2) {
			case TouchSearchView.VIDEO:	typeMedia = MEDIA_TYPE.VIDEO;		break;
			case TouchSearchView.MIDI:	typeMedia = MEDIA_TYPE.MIDI;		break;
			case TouchSearchView.TATCA:	typeMedia = MEDIA_TYPE.ALL;			break;
			case TouchSearchView.ONLINE:{
				typeMedia = MEDIA_TYPE.ALL;		
				this.flagFilterOnline = true;
			}
			break;
			default: typeMedia = MEDIA_TYPE.ALL; break;
		}
	}
	
///////////////////////// - QUERY - ////////////////////////////

	private Cursor DBSearch(String type , String id , String where, int offset , int sum) {
		if (type.equals(TouchMainActivity.SINGER)) {
			return DBInterface.DBGetSongTypeIDCursor(context, "", SearchType.SEARCH_SINGER, typeMedia, Integer.valueOf(id), offset, sum);
		} else if (type.equals(TouchMainActivity.MUSICIAN)) {
			return DBInterface.DBGetSongTypeIDCursor(context, "", SearchType.SEARCH_MUSICIAN, typeMedia, Integer.valueOf(id), offset, sum);
		} else if (type.equals(TouchMainActivity.SONGTYPE)) {
			if(Integer.valueOf(id) == DbHelper.SongType_NewVol && (MyApplication.intSvrModel == MyApplication.SONCA_KM1
					|| MyApplication.intSvrModel == MyApplication.SONCA_KM2 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
					|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
					|| MyApplication.intSvrModel == MyApplication.SONCA_KBX9)){
				return DBInterface.DBGetSongTypeIDCursor_KM(context, where, SearchType.SEARCH_TYPE, typeMedia, Integer.valueOf(id), offset, sum);
			} else {
				if(id.equals("" + DbHelper.SongType_China)){
					int[] intIDs = new int[1]; 
					intIDs[0] = 3; // Nhac Hoa
					return DBInterface.DBGetSongCursor(context, intIDs, where, searchMode, typeMedia,offset, sum);
				}
				if(id.equals("" + DbHelper.SongType_Online)){		
					return null;
				}
				return DBInterface.DBGetSongTypeIDCursor(context, where, SearchType.SEARCH_TYPE, typeMedia, Integer.valueOf(id), offset, sum);
			}
		} else if (type.equals(TouchMainActivity.LANGUAGE)) {
			Cursor cursor = DBInterface.DBGetSongTypeIDCursor(context, where, SearchType.SEARCH_LANGUAGE, typeMedia, Integer.valueOf(id), offset, sum);
			// MyLog.i(TAB, "Song = " + cursor.getCount());
			return cursor;
		} else if (type.equals("")) {
			if(flagFilterOnline){
				return null;
			}
			LanguageStore store = new LanguageStore(context); 
			ArrayList<String>langIDs = store.getListIDActive(); 
			int[] intIDs = new int[langIDs.size()]; 
			for(int i = 0; i < langIDs.size(); i++) {
				intIDs[i] = Integer.parseInt(langIDs.get(i)); 
			}
			
			return DBInterface.DBGetSongCursor(context, intIDs, where, searchMode, typeMedia,offset, sum);
		} else {
			return null;
		}
	}
	
	private int TotalDBSearch(String type, String id, String where) {
		int count = 0;
		if (type.equals(TouchMainActivity.SINGER)) {
			count = DBInterface.DBCountTotalSongTypeID(context, "", SearchType.SEARCH_SINGER, typeMedia, Integer.valueOf(id));
		} else if (type.equals(TouchMainActivity.MUSICIAN)) {
			count = DBInterface.DBCountTotalSongTypeID(context, "", SearchType.SEARCH_MUSICIAN, typeMedia, Integer.valueOf(id));
		} else if (type.equals(TouchMainActivity.SONGTYPE)) {
			count = DBInterface.DBCountTotalSongTypeID(context, where, SearchType.SEARCH_TYPE, typeMedia, Integer.valueOf(id));
		} else if (type.equals(TouchMainActivity.LANGUAGE)) {
			count = DBInterface.DBCountTotalSongTypeID(context,where, SearchType.SEARCH_LANGUAGE, typeMedia, Integer.valueOf(id));
			// MyLog.i(TAB, "Total = " + count);
		} else if (type.equals("")) {
			LanguageStore store = new LanguageStore(context); 
			ArrayList<String>langIDs = store.getListIDActive(); 
			int[] intIDs = new int[langIDs.size()]; 
			for(int i = 0; i < langIDs.size(); i++) {
				intIDs[i] = Integer.parseInt(langIDs.get(i)); 
			}
			count = DBInterface.DBCountTotalSong(context, intIDs, where, SearchMode.MODE_MIXED, typeMedia);
			// cursor = DBInterface.DBTuTestLay(where, searchMode, typeMedia, offset, sum);
		} else {}
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
		
		if(!type.equals("")){
			tempRowStart = BaseLoadSong.ROWSTART;
		}
		
		ROWNEXT = 50;
		
		if(id.equals("" + DbHelper.SongType_Online) || flagFilterOnline){
			MyLog.i(TAB, "OnRunLoad() - 0 - SPECIAL THE LOAI ONLINE");
			
			ROWNEXT = 300;
			
			ArrayList<Song> songYouTube = new ArrayList<Song>();
			if (MyApplication.intSvrModel == MyApplication.SONCA_SMARTK) {
				MyApplication.flagFinishLoadDBOnline = false;
				
				LanguageStore store = new LanguageStore(context);
				ArrayList<String> langIDs = store.getListIDActive();
				int[] intIDs = new int[langIDs.size()];
				for (int i = 0; i < langIDs.size(); i++) {
					intIDs[i] = Integer.parseInt(langIDs.get(i));
				}
				
				Cursor cursor = DBInterface.DBGetSongCursor_YouTube(context,
						intIDs, where, searchMode, typeMedia, 0, tempRowStart);
								
				if (isCancel()) {
					cursor.close();
					cursor = null;
					setRunning(false);
					return;
				}
				
				int countRemove = 0;
				
				MyLog.i(TAB, "OnRunLoad() - 2");
				
				if (cursor != null) {
					if (moveToFirstCursor(cursor)) {
						songYouTube = new ArrayList<Song>();
						for (int i = 0; i < tempRowStart; i++) {
							if (isCancel()) {
								cursor.close();
								cursor = null;
								publishProgress(FINISH);
								setRunning(false);
								return;
							}							
							Song song = new Song();
							parseSongInfoQueryResult_YouTube(song, cursor);
							song.setYoutubeSong(true);
							songYouTube.add(song);
							if (!cursor.moveToNext()) {
								break;
							}
						}
						cursor.close();
						cursor = null;
						
						setListSong(songYouTube);
					}
				}
				publishProgress(START);

				MyLog.i(TAB, "OnRunLoad() - 3");
						
				if ((checkSize() + countRemove) < tempRowStart) {
					MyLog.i(TAB, "Full Data : " + checkSize());
					publishProgress(FINISH);
					setRunning(false);
					return;
				} else {
					MyLog.i(TAB, IdThread + "-  Start Loaded : " + checkSize() + " : " + where);
					waitLoad();
					if (isCancel()) {
						publishProgress(FINISH);
						setRunning(false);
						return;
					}
					MyLog.i(TAB, "Loading....");
					int countPage = 0;
					
					MyLog.e(TAB, "Loading STYLE OLD");
					while (!isCancel()) {
						MyApplication.flagFinishLoadDBOnline = false;
						cursor = DBInterface.DBGetSongCursor_YouTube(context,
								intIDs, where, searchMode, typeMedia, tempRowStart + countPage * ROWNEXT, ROWNEXT);
						if (isCancel()) {
							cursor.close();
							cursor = null;
							publishProgress(FINISH);
							setRunning(false);
							return;
						}
						countPage++;
						if (cursor != null) {
							if (moveToFirstCursor(cursor)) {
								countRemove = 0;
								songYouTube = new ArrayList<Song>();
								for (int i = 0; i < ROWNEXT; i++) {
									if (isCancel()) {
										cursor.close();
										cursor = null;
										publishProgress(FINISH);
										setRunning(false);
										return;
									}
									Song song = new Song();
									parseSongInfoQueryResult_YouTube(song, cursor);
									song.setYoutubeSong(true);
									songYouTube.add(song);
									if (!cursor.moveToNext()) {
										break;
									}
								}
								cursor.close();
								cursor = null;
								
								setListSong(songYouTube);
								publishProgress(LOADING);
							} else {
								cursor.close();
								cursor = null;
							}
						}						
						
						if ((checkSize() + countRemove) < ROWNEXT) {
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
				
					
				}
				
			}
			
			return;
		}
		
		MyLog.i(TAB, "OnRunLoad() - 0 -- typeFragment = " + type);
		
		
		// -----------------------------------//
		ArrayList<Song> songMunberID = new ArrayList<Song>();
		if (!checkNotLetter(where) && !where.equals("") && type.equals("")) {
			Cursor cursor = DBInterface.DBGetSongNumberCursor(context, where, 0, 0, typeMedia);
			if (isCancel()) {
				cursor.close();
				cursor = null;
				setRunning(false);
				return;
			}
			if (cursor != null) {
				if (moveToFirstCursor(cursor)) {
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
						songMunberID.add(song);
						if (!cursor.moveToNext()) {
							break;
						}
					}
				}
				cursor.close();
				cursor = null;
				for (int i = 0; i < songMunberID.size(); i++) {
					Song song = songMunberID.get(i);
					String textID = "";
					boolean boolRed = song.getId() == song.getIndex5();
					if (MyApplication.intWifiRemote == MyApplication.SONCA_KARTROL
							&& MyApplication.intRemoteModel != 0) {
						textID = song.getIndex5() + "";
					} else {
						if (boolRed) {
							textID = convertIdSong(song.getId()) + " | -";
						} else {
							textID = convertIdSong(song.getId()) + " | " + song.getIndex5();
						}
					}
					SpannableString wordtoSpan = new SpannableString(textID);
					if (where.equals("" + song.getId())) {
						wordtoSpan.setSpan(new ForegroundColorSpan(Color.GREEN), 6 - where.length(), 6,
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					} else if (where.equals("" + song.getIndex5())) {
						wordtoSpan.setSpan(new ForegroundColorSpan(Color.GREEN), 9, textID.length(),
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
					song.setSpannableNumber(wordtoSpan);
				}
			}
		}
		
		// -----------------------------------//
		ArrayList<Song> songFav = new ArrayList<Song>();
		if (where != null && where.equals("") && type.equals("")) {
				songFav = DBInterface.DBGetFavouriteSongList(0, 0, context);
				
				ArrayList<Song> newList = new ArrayList<Song>();
				if(typeMedia == MEDIA_TYPE.MIDI){
					for(Song song:songFav){
						if(song.getMediaType().ordinal() == MEDIA_TYPE.MIDI.ordinal()){
							newList.add(song);
						}
					}
					songFav = newList;
				} else if(typeMedia == MEDIA_TYPE.VIDEO){
					for(Song song:songFav){
						if(song.getMediaType().ordinal() != MEDIA_TYPE.MIDI.ordinal()){
							newList.add(song);
						}
					}
					songFav = newList;
				} else {
					
				}
		}
		
		// -----------------------------------//
		ArrayList<Song> songYouTube = new ArrayList<Song>();
		if (MyApplication.intSvrModel == MyApplication.SONCA_SMARTK
				&& where != null && !where.equals("") && type.equals("")
				&& where.length() > 0) {
			LanguageStore store = new LanguageStore(context);
			ArrayList<String> langIDs = store.getListIDActive();
			int[] intIDs = new int[langIDs.size()];
			for (int i = 0; i < langIDs.size(); i++) {
				intIDs[i] = Integer.parseInt(langIDs.get(i));
			}

			Cursor cursor = DBInterface.DBGetSongCursor_YouTube(context,
					intIDs, where, SearchMode.MODE_MIXED, typeMedia, 0, 0);
			if (isCancel()) {
				cursor.close();
				cursor = null;
				setRunning(false);
				return;
			}
			if (cursor != null) {
				if (moveToFirstCursor(cursor)) {
					for (int i = 0; i < tempRowStart; i++) {
						if (isCancel()) {
							cursor.close();
							cursor = null;
							publishProgress(FINISH);
							setRunning(false);
							return;
						}
						Song song = new Song();
						parseSongInfoQueryResult_YouTube(song, cursor);
						song.setYoutubeSong(true);
						songYouTube.add(song);
						if (!cursor.moveToNext()) {
							break;
						}
					}
				}
				cursor.close();
				cursor = null;
			}
		}
		
		// -----------------------------------//
				
		Cursor cursor = DBSearch(type, id, where, 0, tempRowStart);
		if (isCancel()) {
			cursor.close();
			cursor = null;
			setRunning(false);
			return;
		}
		if (cursor != null) {
			if (moveToFirstCursor(cursor)) {
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
				int countTemp = list.size();
				if (!songMunberID.isEmpty()) {
					list.addAll(0, songMunberID);
				}
				if (!songFav.isEmpty()) {
					list.addAll(0, songFav);
				}
				if (countTemp < tempRowStart) {
					if (!songYouTube.isEmpty()) {
						list.addAll(songYouTube);
					}
				}
				setListSong(list);
			} else {
				cursor.close();
				cursor = null;
				ArrayList<Song> list = new ArrayList<Song>();
				if (!songYouTube.isEmpty()) {
					list.addAll(0, songYouTube);
				}
				if (!songMunberID.isEmpty()) {
					list.addAll(0, songMunberID);
				}				
				if (!songFav.isEmpty()) {
					list.addAll(0, songFav);
				}
				setListSong(list);
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
			MyLog.i(TAB, IdThread + "-  Start Loaded : " + checkSize() + " : " + where);
			waitLoad();
			if (isCancel()) {
				publishProgress(FINISH);
				setRunning(false);
				return;
			}
			MyLog.i(TAB, "Loading....");
			int countPage = 0;
			
			if((where != null && where.trim().length() > 0) || !type.equals("")){
//				if(MyApplication.intSvrModel == MyApplication.SONCA){
//				if(where != null && !where.equals("")){ // if(where != null && !where.equals("")){
					MyLog.e(TAB, "Loading STYLE NEW");
					publishProgress(LOADING_FULL);
					
					cursor = DBSearch(type, id, where, 0, 0);
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
//							if(countPage > 1){
//								publishProgress(LOADING_NEXT);	
//							}	
													
							if (cursor.moveToPosition(tempRowStart + (countPage - 1) * ROWNEXT)) {
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

								int countTemp = list.size();
								if (countTemp < ROWNEXT) {
									if (!songYouTube.isEmpty()) {
										list.addAll(songYouTube);
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
					cursor = DBSearch(type, id, where, tempRowStart + countPage * ROWNEXT, ROWNEXT);
					if (isCancel()) {
						cursor.close();
						cursor = null;
						publishProgress(FINISH);
						setRunning(false);
						return;
					}
					countPage++;
					if (cursor != null) {
						if (moveToFirstCursor(cursor)) {
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
			}
		}

	}
}
