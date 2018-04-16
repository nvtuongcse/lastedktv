package vn.com.sonca.sk90xxHidden;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.AddDataSong.AdapterAddSong;
import vn.com.sonca.AddDataSong.AdapterAddSong.OnAddSongListener;
import vn.com.sonca.LoadSong.BaseLoadSong;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.CustomView.TouchSearchView;
import vn.com.sonca.Touch.CustomView.TouchStatusTotalView;
import vn.com.sonca.Touch.Language.LanguageStore;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.database.DBInterface;
import vn.com.sonca.database.DbHelper;
import vn.com.sonca.database.DBInstance.SearchMode;
import vn.com.sonca.database.DBInstance.SearchType;
import vn.com.sonca.params.Song;
import vn.com.sonca.params.SongInfo;
import vn.com.sonca.sk90xxHidden.AdapterDelSong.OnDelSongListener;
import vn.com.sonca.sk90xxHidden.LoadDelSong.OnLoadDelListener;
import vn.com.sonca.utils.AppConfig.MEDIA_TYPE;
import vn.com.sonca.zzzzz.MyApplication;

public class FragDeleteSong extends Fragment implements OnLoadDelListener {
	
	private OnFragDeleteSongListener listener;
	public interface OnFragDeleteSongListener {
		public void OnClickDel(Song song);
		public void OnHideKeyBoard();
		public void OnUpdateTotal(int total);
	}
	public void setOnFragDeleteSongListener(OnFragDeleteSongListener listener){
		this.listener = listener;
	}
	
	private LinearLayout layoutShowThongBao;
	
	private String TAB = "FragDeleteSong";
	private Context context;
	private ListView listView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.touch_fragment_song, container, false);
		context = this.getActivity().getApplicationContext();
		listView = (ListView)view.findViewById(R.id.listview);
		
		layoutShowThongBao = (LinearLayout) view.findViewById(R.id.layoutShowThongBao);
		
		TouchStatusTotalView statusTotalView = (TouchStatusTotalView) view.findViewById(R.id.statusTotalView);
		statusTotalView.setVisibility(View.GONE);
		
		CreateLoadData("");
		return view;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		StopLoadData();
		stopTimerShowThongBao();
	}
	
	private ArrayList<Song> listOldCheck = new ArrayList<Song>();
	private int stateFilter = 0;
	public void loadDatabase(String where, ArrayList<Song> listOldCheck, int stateFilter){
		this.listOldCheck = listOldCheck;
		this.stateFilter = stateFilter;
		CreateLoadData(where);
	}
	
	public void sendNotifyCheckList(){
		if(listArraySongs != null){
			for (Song song : listArraySongs) {
				song.setHidden(MyApplication.checkInsideListHidden90xx(song.getId()));
			}
			
//			adapterDelSong = new AdapterDelSong(context, R.layout.touch_item_song_list, listArraySongs, "");
//			adapterDelSong.setOnDelSongListener(new OnDelSongListener() {
//				
//				@Override
//				public void OnClickDel(Song song) {
//					if(listener != null){
//						listener.OnClickDel(song);
//					}
//				}
//			});
//			
//			listView.setFastScrollEnabled(true);
//			listView.setOnScrollListener(onScrollListener);
//			listView.setAdapter(adapterDelSong);
			
			if(adapterDelSong != null){
				adapterDelSong.notifyDataSetChanged();
			}
		}
		
	}
	
	public void sendNotifyToFrag(boolean checkAll){
		if(listArraySongs != null){
			if(checkAll){
				for (Song song : listArraySongs) {
					song.setActive(true);
				}
			} else {
				for (Song song : listArraySongs) {
					song.setActive(false);
				}
			}	
			
			adapterDelSong = new AdapterDelSong(context, R.layout.touch_item_song_list, listArraySongs, "");
			adapterDelSong.setOnDelSongListener(new OnDelSongListener() {
				
				@Override
				public void OnClickDel(Song song) {
					if(listener != null){
						listener.OnClickDel(song);
					}
				}
			});
			
			listView.setFastScrollEnabled(true);
			listView.setOnScrollListener(onScrollListener);
			listView.setAdapter(adapterDelSong);
			
			if(adapterDelSong != null){
				adapterDelSong.notifyDataSetChanged();
			}
		}
		
	}
	
	public ArrayList<Song> getAllList(){
		return this.listArraySongs;
	}
	
	private LoadDelSong loadData;
	private String searchSave = "";
	private ArrayList<Song> listArraySongs;
	private AdapterDelSong adapterDelSong;
	private void CreateLoadData(String search) {
		MyLog.e(TAB, "CreateLoadData : " + search);
		searchSave = search;
		if(layoutShowThongBao.getVisibility() != View.INVISIBLE)
			layoutShowThongBao.setVisibility(View.INVISIBLE);
		
		listArraySongs = new ArrayList<Song>();
		adapterDelSong = new AdapterDelSong(context, R.layout.touch_item_song_list,
				listArraySongs, search);
		adapterDelSong.setOnDelSongListener(new OnDelSongListener() {
			
			@Override
			public void OnClickDel(Song song) {
				if(listener != null){
					listener.OnClickDel(song);
				}
			}
		});
		
		listView.setFastScrollEnabled(true);
		listView.setOnScrollListener(onScrollListener);
		listView.setAdapter(adapterDelSong);
			//----------//
		StopLoadData();
			//----------//
		loadData = new LoadDelSong(0, context, search, listArraySongs, listOldCheck, stateFilter);
		loadData.setOnLoadDelListener(this);
		loadData.execute();
			
	}
	
	
	private void StopLoadData(){
		if (loadData != null) {
			loadData.cancel(true);
			loadData = null;
		}
	}

	@Override
	public void OnLoading() {
		if(adapterDelSong != null){
			adapterDelSong.notifyDataSetChanged();
		}
		
		startLoadTotalSong(searchSave, "", "", -1, TouchSearchView.TATCA, listArraySongs, context, null, -1);
	}
	
	@Override
	public void OnFinish() {
		if(adapterDelSong != null){
			adapterDelSong.notifyDataSetChanged();
		}
		
		startLoadTotalSong(searchSave, "", "", -1, TouchSearchView.TATCA, listArraySongs, context, null, -1);
	}

	@Override
	public void OnStartLoad(int idThread) {
		if(adapterDelSong != null){
			adapterDelSong.notifyDataSetChanged();
		}
	}
	
	private int countLoadNext;
	private long loadNextTime;
	private long timeDelayLoadNext = 2000;
	private int stateScrollListView = OnScrollListener.SCROLL_STATE_IDLE;
	private OnScrollListener onScrollListener = new OnScrollListener() {
		private int mLastFirstVisibleItem = 0;
		private boolean flagDisplay = false;
		
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			stateScrollListView = scrollState;
			switch (scrollState) {
			case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
				if (listArraySongs != null && listArraySongs.size() == 6) {
					
				}
				if(listener != null){
					listener.OnHideKeyBoard();
				}
				break;
			case OnScrollListener.SCROLL_STATE_IDLE:
//				if(loadData != null){
//					loadData.loadNextPage();
//				}
				break;
			}
		}

		@Override
		public void onScroll(AbsListView view, int first, int visible, int total) {
			if (loadData == null) {
				return;
			}else{						
				int searchLength = 0;
				if(searchSave != null && searchSave.trim().length() > 0){
					searchLength = searchSave.trim().length();
				}
				
				int tempRowTrigger = BaseLoadSong.ROWTRIGGER;
				
				switch (searchLength) {
				case 0:
					tempRowTrigger = BaseLoadSong.ROWTRIGGER;
					break;
				case 1:
					tempRowTrigger = BaseLoadSong.ROWTRIGGER_1CHAR;
					break;
				case 2:
					tempRowTrigger = BaseLoadSong.ROWTRIGGER_2CHAR;
					break;
				default:
					tempRowTrigger = BaseLoadSong.ROWTRIGGER_3CHAR;
					break;
				}
				
				if(total - tempRowTrigger > 0){
					if((first + visible) > (total - tempRowTrigger)){
						if(loadData != null){
							loadData.loadNextPage();
						}
					}	
				}
				
			}
			
			try {
				if (mLastFirstVisibleItem < first) {
				}
				if (mLastFirstVisibleItem > first) {
				}
				mLastFirstVisibleItem = first;
				// --------------//
			} catch (IndexOutOfBoundsException ex) {
				ex.printStackTrace();
				return;
			}
		}
	};
	
	private Timer timerShowThongBao;
	
	private void stopTimerShowThongBao(){
		if(timerShowThongBao != null){
			timerShowThongBao.cancel();
			timerShowThongBao = null;
		}
	}
	
	private void statTimerShowThongBao(){
		stopTimerShowThongBao();
		timerShowThongBao = new Timer();
		timerShowThongBao.schedule(new TimerTask() {
			
			@Override
			public void run() {
				handler.sendEmptyMessage(0);
			}
			
			private Handler handler = new Handler(){
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					if(adapterDelSong != null){
						if(adapterDelSong.getCount() > 0){
							if(layoutShowThongBao.getVisibility() != View.INVISIBLE)
								layoutShowThongBao.setVisibility(View.INVISIBLE);
						} else {
							if(layoutShowThongBao.getVisibility() != View.VISIBLE)
								layoutShowThongBao.setVisibility(View.VISIBLE);
						}
					}
				}
			};
		}, 2000);
	}
	
	private Timer timerLoadTotalSong = null;
	private void startLoadTotalSong(final String where, final String type, final String id, int state1, final int state2, 
			ArrayList<Song> listSongs, final Context context , TouchMainActivity mainActivity , int IdThread){
		stopLoadTotalSong();
		timerLoadTotalSong = new Timer();
		timerLoadTotalSong.schedule(new TimerTask() {
			
			@Override
			public void run() {
				boolean flagFilterOnline = false;
				MEDIA_TYPE typeMedia;
				switch (state2) {
				case TouchSearchView.VIDEO:	typeMedia = MEDIA_TYPE.VIDEO;		break;
				case TouchSearchView.MIDI:	typeMedia = MEDIA_TYPE.MIDI;		break;
				case TouchSearchView.TATCA:	typeMedia = MEDIA_TYPE.ALL;			break;
				case TouchSearchView.ONLINE:{
					typeMedia = MEDIA_TYPE.ALL;		
					flagFilterOnline = true;
				}
				break;
				default: typeMedia = MEDIA_TYPE.ALL; break;
				}
				int count = 0;
				if (type.equals(TouchMainActivity.SINGER)) {
					count = DBInterface.DBCountTotalSongTypeID(context, "", SearchType.SEARCH_SINGER, typeMedia, Integer.valueOf(id));
				} else if (type.equals(TouchMainActivity.MUSICIAN)) {
					count = DBInterface.DBCountTotalSongTypeID(context, "", SearchType.SEARCH_MUSICIAN, typeMedia, Integer.valueOf(id));
				} else if (type.equals(TouchMainActivity.SONGTYPE)) {					
					if(Integer.valueOf(id) == DbHelper.SongType_NewVol && (MyApplication.intSvrModel == MyApplication.SONCA_KM1
							|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
							|| MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
							|| MyApplication.intSvrModel == MyApplication.SONCA_KM2 || MyApplication.intSvrModel == MyApplication.SONCA_KBX9)){
						count = DBInterface.DBCountTotalSongTypeID_KM(context, where, SearchType.SEARCH_TYPE, typeMedia, Integer.valueOf(id));
					} else {
						if(id.equals("" + DbHelper.SongType_China)){
							int[] intIDs = new int[1]; 
							intIDs[0] = 3; // Nhac Hoa
							count = DBInterface.DBCountTotalSongFull(context, intIDs, where, SearchMode.MODE_MIXED, typeMedia);
						} else if(id.equals("" + DbHelper.SongType_Online)){
							count = -99;
						} else {
							count = DBInterface.DBCountTotalSongTypeID(context, where, SearchType.SEARCH_TYPE, typeMedia, Integer.valueOf(id));	
						}						
					}					
				} else if (type.equals(TouchMainActivity.LANGUAGE)) {
					count = DBInterface.DBCountTotalSongTypeID(context,where, SearchType.SEARCH_LANGUAGE, typeMedia, Integer.valueOf(id));
					// MyLog.e(TAB, "Total = " + count);
				} else if (type.equals("")) {
					if(flagFilterOnline){
						count = -99;
					} else {
						LanguageStore store = new LanguageStore(context); 
						ArrayList<String>langIDs = store.getListIDActive(); 
						int[] intIDs = new int[langIDs.size()]; 
						for(int i = 0; i < langIDs.size(); i++) {
							intIDs[i] = Integer.parseInt(langIDs.get(i)); 
						}
						if(where.equals("")){
							count = DBInterface.DBCountTotalSongFull(context, intIDs, where, SearchMode.MODE_MIXED, typeMedia);
						}else{
							count = DBInterface.DBCountTotalSong(context, intIDs, where, SearchMode.MODE_MIXED, typeMedia);
						}
					}
					
				} else {}
				
				if(count != -99 && !checkNotLetter(where) && !where.equals("") && type.equals("")){
					count += DBInterface.DBGetTtotalSongNumberCursor(context, where, typeMedia);
				}
				
				if(count != -99 && MyApplication.intSvrModel == MyApplication.SONCA_SMARTK 
						&& where != null && !where.equals("") && type.equals("") && where.length() > 0) {
					LanguageStore store = new LanguageStore(context); 
					ArrayList<String>langIDs = store.getListIDActive(); 
					int[] intIDs = new int[langIDs.size()]; 
					for(int i = 0; i < langIDs.size(); i++) {
						intIDs[i] = Integer.parseInt(langIDs.get(i)); 
					}
					
					count += DBInterface.DBCountTotalSong_YouTube(context, intIDs, where,
							SearchMode.MODE_MIXED, typeMedia);	
				}
				
				if(count == -99){ // special case the loai online
					LanguageStore store = new LanguageStore(context); 
					ArrayList<String>langIDs = store.getListIDActive(); 
					int[] intIDs = new int[langIDs.size()]; 
					for(int j = 0; j < langIDs.size(); j++) {
						intIDs[j] = Integer.parseInt(langIDs.get(j)); 
					}		
					
					ArrayList<Song> songYouTube = new ArrayList<Song>();
					Cursor cursor = DBInterface.DBGetSongCursor_YouTube(context,
							intIDs, where, SearchMode.MODE_MIXED, typeMedia, 0, 0);
					if(cursor.moveToFirst()){
						for (int i = 0; i < cursor.getCount(); i++) {
							Song song = new Song();
							song.setId(cursor.getInt(0));
							song.setIndex5(cursor.getInt(1));
							song.setYoutubeSong(true);
							songYouTube.add(song);
							if (!cursor.moveToNext()) {
								break;
							}
						}
						cursor.close();
						cursor = null;
					}
					
					if(songYouTube.size() > 0){
						ArrayList<Song> songExisted = DBInterface.DBSearchListSongID(songYouTube, context);
						if(songExisted.size() > 0){
							ArrayList<Song> newList = new ArrayList<Song>();	 
							
							for (Song song : songYouTube) {
								if(songExisted.contains(song)){
//									MyLog.d("remove bot song trong youtube", song.getName() + " ");
								} else {
									newList.add(song);
								}
							}
							
							songYouTube = newList;
						}
					}
					
					count = songYouTube.size();
				}
				
				Message message = new Message();
				Bundle data = new Bundle();
				data.putInt("TotalSong", count);
				message.setData(data);
				handlerLoadTotalSong.sendMessage(message);
			}
		}, 500);
	}
	
	private boolean checkNotLetter(String where){
		for (int i = 0; i < where.length(); i++) {
			if(Character.isLetter(where.charAt(i))){
				return true;
			}
		}
		return false;
	}
	
	private void stopLoadTotalSong(){
		if(timerLoadTotalSong != null){
			timerLoadTotalSong.cancel();
			timerLoadTotalSong = null;
		}
	}
		
	private Handler handlerLoadTotalSong = new Handler(){
		public void handleMessage(Message msg) {
			int total = msg.getData().getInt("TotalSong");
			if (total > 0) {
				layoutShowThongBao.setVisibility(View.INVISIBLE);
			} else {
				layoutShowThongBao.setVisibility(View.VISIBLE);
			}
			if(listener != null){
				listener.OnUpdateTotal(total);
			}
		};
	};
	
}
