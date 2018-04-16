package vn.com.sonca.Touch.SongFollow;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import vn.com.sonca.LoadSong.BaseLoadSong;
import vn.com.sonca.LoadSong.BaseLoadSong.OnLoadListener;
import vn.com.sonca.LoadSong.LoadSongFromDatabase;
import vn.com.sonca.Lyric.LoadLyric;
import vn.com.sonca.Lyric.LoadLyric.OnLoadLyricListener;
import vn.com.sonca.Lyric.LoadLyricNew;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.CustomView.TouchSearchView;
import vn.com.sonca.Touch.CustomView.TouchStatusTotalView;
import vn.com.sonca.Touch.CustomView.TouchTotalView;
import vn.com.sonca.Touch.Language.LanguageStore;
import vn.com.sonca.Touch.Listener.TouchIAdapter;
import vn.com.sonca.Touch.Listener.TouchIBaseFragment;
import vn.com.sonca.Touch.Song.TouchAdapterSong;
import vn.com.sonca.Touch.touchcontrol.TouchFragmentBase;
import vn.com.sonca.Touch.touchcontrol.TouchItemBack;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity.OnMainListener;
import vn.com.sonca.database.DBInstance.SearchMode;
import vn.com.sonca.database.DBInstance.SearchType;
import vn.com.sonca.database.DBInterface;
import vn.com.sonca.params.ServerStatus;
import vn.com.sonca.params.Song;
import vn.com.sonca.zzzzz.MyApplication;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.utils.AppConfig.MEDIA_TYPE;
import vn.com.sonca.utils.AppSettings;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.TextView;

public class TouchFragmentSongFollow extends TouchFragmentBase implements
		OnMainListener, OnLoadListener {

	private String TAB = "FragmentSongFollow";
	private TouchMainActivity mainActivity;
	private TouchIBaseFragment listener;

	private long timeDelayLoadNext = 2000;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (TouchIBaseFragment) activity;
			mainActivity = (TouchMainActivity) activity;
		} catch (Exception ex) {
		}
	}

	private int state1;
	private int state2;
	private Context context;
	private String typeFragment = "";
	private String idFragment = "";
	private ListView listView;
	private TouchAdapterSongFollow adapterSong = null;
	private TouchTotalView totalView;
	private LinearLayout layoutShowThongBao;
	
	private TouchStatusTotalView statusTotalView;
	private TextView tvThongBao1;
	private TextView tvThongBao2;
	
	private ArrayList<Song> listArraySongs = null;
	
	public String getTypeFragment() {
		return typeFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity().getApplicationContext();
		View view = inflater.inflate(R.layout.touch_fragment_song, container,
				false);
		idFragment = getArguments().getString("data");
		typeFragment = getArguments().getString("fragment");
		if (listener != null) {
			listener.OnNameSearch(idFragment, typeFragment);
		}
		// -----------------//
		layoutShowThongBao = (LinearLayout)view.findViewById(R.id.layoutShowThongBao);		
		listView = (ListView) view.findViewById(R.id.listview);

		tvThongBao1 = (TextView)view.findViewById(R.id.tvThongBao1);
		tvThongBao2 = (TextView)view.findViewById(R.id.tvThongBao2);
		
		totalView = (TouchTotalView) view.findViewById(R.id.totalview);
		totalView.setDisplay(true);
		totalView.setVisibility(View.VISIBLE);
		
		statusTotalView = (TouchStatusTotalView) view.findViewById(R.id.statusTotalView);
		statusTotalView.setVisibility(View.GONE);
		
		changeColorScreen();
		
		CreateLoadData(state1, TouchSearchView.TATCA, "");

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (loadData != null) {
			loadData.cancel(true);
			loadData = null;
		}
	}
	
	// /////////////////////////// - DATABASE - //////////////////////////////

	private void StopLoadData() {
		if (loadData != null) {
			loadData.cancel(true);
			loadData = null;
		}
	}

	private int IdThread = 0;
	private LoadSongFromDatabase loadData;

	private void CreateLoadData(int state1, int state2, String search) {
//		MyLog.e(TAB, "CreateLoadData : " + search);
		AppSettings setting = AppSettings.getInstance(context);
//		if (setting.loadServerLastUpdate() != 0) {
//		if(listArraySongs != null){
//			listArraySongs.clear();
//			adapterSong.notifyDataSetChanged();
//		}
			//----------//
		listArraySongs = new ArrayList<Song>();
		adapterSong = new TouchAdapterSongFollow(context,
				R.layout.touch_item_song_list, listArraySongs, search, mainActivity);
		adapterSong.setOnAdapterListener(new TouchIAdapter() {
			@Override
			public void OnItemActive(Song song, String id, float x, float y) {
				if (listener != null) {
					listener.onClickItem(song, id, TouchMainActivity.SONG, "",
							-1, x, y);
				}
			}

			@Override
			public void OnItemFavourite(int position, Song song) {
				if (listener != null) {
					listener.OnClickFavourite();
				}
			}

			@Override
			public void OnFirstClick(Song song, int position, float x,
					float y) {
				if (listener != null) {
					listener.onFirstClick(song, TouchMainActivity.SONG,
							position, x, y);
				}
			}

			@Override
			public void onDeleteSong(Song song, int position) {
			}

			@Override
			public void onPlaySong(Song song, int position, float x, float y) {
				if (listener != null) {
					listener.onPlaySong(song, TouchMainActivity.SONG, 0, x, y);
				}
			}

			@Override
			public void OnSingerLink(boolean bool, String name,
					int[] idSinger) {
			}

			@Override
			public void OnLockNotify() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void OnUnLockNotify() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void OnShowLyric(int position, Song song) {
				LoadLyricNew loadLyric = new LoadLyricNew(context, getActivity().getWindow());
				loadLyric.setData(song , MyApplication.NameFileLyric);
				loadLyric.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				loadLyric.setOnLoadLyricListener(new LoadLyricNew.OnLoadLyricListener() {
					@Override public void OnFavourite(Song song) {
						if(adapterSong != null){
							adapterSong.notifyDataSetChanged();
						}
					}

					@Override
					public void OnShowReviewKaraoke(Song song) {
						// TODO Auto-generated method stub
						if(listener != null){
							listener.OnShowReviewKaraoke(song);
						}
					}
					
					@Override
					public void OnPopupPlayYouTube(Song song) {
						if(listener != null){
							listener.onPlayYouTube(song);
						}
					}

					@Override
					public void OnPopupDownYouTube(Song song) {
						if(listener != null){
							listener.onDownYouTube(song);
						}
					}
					
				});
				if(listener != null){
					listener.OnShowLyric();
				}
			}

			@Override
			public void onPlayYouTube(Song song) {
				if(listener != null){
					listener.onPlayYouTube(song);
				}
			}

			@Override
			public void onDownYouTube(Song song) {
				if(listener != null){
					listener.onDownYouTube(song);
				}
			}

		});
		listView.setOnScrollListener(onScrollListener);
		listView.setFastScrollEnabled(true);
		listView.setAdapter(adapterSong);
			//------------//
		StopLoadData();
		loadData = new LoadSongFromDatabase(search, typeFragment,
				idFragment, state1, state2, listArraySongs, context,
				mainActivity, IdThread);
		loadData.setOnLoadListener(this);
		loadData.execute();
		startLoadTotalSong(search, typeFragment,
				idFragment, state1, state2, listArraySongs, context,
				mainActivity, IdThread);
		
		countLoadNext = 0;			
		loadNextTime = 0;
//		}
	}
	
	private int countLoadNext;
	private long loadNextTime;

	private int position;
	private int stateScrollListView = OnScrollListener.SCROLL_STATE_IDLE;
	private OnScrollListener onScrollListener = new OnScrollListener() {
		private int mLastFirstVisibleItem = 0;
		private boolean flagDisplay = false;

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			stateScrollListView = scrollState;
			switch (scrollState) {
			case 1: // SCROLL_STATE_TOUCH_SCROLL
				if (listArraySongs != null && listArraySongs.size() == 6) {
					totalView.setDisplay(flagDisplay);
					if (flagDisplay) {
						totalView.setVisibility(View.VISIBLE);
					} else {
						totalView.setVisibility(View.GONE);
					}
					flagDisplay = !flagDisplay;
				}
				break;
			}
		}

		@Override
		public void onScroll(AbsListView view, int first, int visible, int total) {
			if (loadData == null) {
				return;
			}else{						
				int searchLength = 0;
				if(search != null && search.trim().length() > 0){
					searchLength = search.trim().length();
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
				
				if(!typeFragment.equals("")){
					tempRowTrigger = BaseLoadSong.ROWTRIGGER;
				}
				
				if((first + visible) > (total - tempRowTrigger)){
					if ((search != null && search.trim().length() > 0) || !typeFragment.equals("")) {
						if (countLoadNext == 0) {
							loadData.loadNextPage();
							loadNextTime = System.currentTimeMillis();
							countLoadNext++;
						} else {
							if (countLoadNext == 1) {
								if (System.currentTimeMillis() - loadNextTime > timeDelayLoadNext) {
									loadData.loadNextPage();
									loadNextTime = System.currentTimeMillis();
									countLoadNext++;
								}
							} else {
								if (System.currentTimeMillis() - loadNextTime > 500) {
									loadData.loadNextPage();
									loadNextTime = System.currentTimeMillis();
									countLoadNext++;
								}
							}

						}
					} else {
						loadData.loadNextPage();
						loadNextTime = System.currentTimeMillis();
					}
					
					
				}	
				
			}
			
			try {
				if (mLastFirstVisibleItem < first) {
					totalView.setDisplay(false);
					totalView.setVisibility(View.GONE);
				}
				if (mLastFirstVisibleItem > first) {
					totalView.setVisibility(View.VISIBLE);
					totalView.setDisplay(true);
				}
				mLastFirstVisibleItem = first;
				// --------------//
			} catch (IndexOutOfBoundsException ex) {
				ex.printStackTrace();
				return;
			}
		}
	};
	
	@Override
	public void OnClearList() {
		if (this.isAdded() && adapterSong != null) {
			adapterSong.notifyDataSetChanged();
		}
	}

	@Override
	public void OnStartLoad(int idThread) {
		if (this.isAdded()) {
			if (MyApplication.intWifiRemote == MyApplication.SONCA) {
				if (listArraySongs.size() <= 6) {
					// MyLog.e("OnLoading", "set swipe touch");
					swipping = true;
					ActivitySwipeDetector activitySwipeDetector = new ActivitySwipeDetector();
					listView.setOnTouchListener(activitySwipeDetector);
				} else {
					// MyLog.e("OnLoading", "set touch null");
					swipping = false;
					listView.setOnTouchListener(null);
					stateScrollListView = OnScrollListener.SCROLL_STATE_IDLE;
				}
			} else {
				swipping = false;
			}
			
			if (isAdded()) {
				if(adapterSong != null){
					adapterSong.notifyDataSetChanged();
				}
				if(!listArraySongs.isEmpty()){
					listView.setSelection(0);
				}
			}
		}
	}

	@Override
	public void OnLoading() {
		if (this.isAdded() && adapterSong != null) {
			adapterSong.notifyDataSetChanged();
		}
	}

	@Override
	public void OnStopLoad(String search) {
		if (this.isAdded() && adapterSong != null) {
			adapterSong.notifyDataSetChanged();
		}
	}

	// -------------------//

	@Override
	public void OnSK90009() {
		if (this.isAdded() && adapterSong != null) {
			adapterSong.notifyDataSetChanged();
		}
	}

	@Override
	public void OnLoadSucessful() {
		// TODO Auto-generated method stub

	}

	private String search = "";
	private int stateSearch;

	@Override
	public void OnSearchMain(int state1, int state2, String search) {
		this.search = search;
		this.stateSearch = state2;
		CreateLoadData(state1, state2, search);
	}

	@Override
	protected void UpdateAdapter() {
	}

	@Override
	public void OnUpdateImage() {
		// TODO Auto-generated method stub

	}

	@Override
	public void OnUpdateCommad(ServerStatus status) {
		// TODO Auto-generated method stub

	}
	
	////////////////////////////////////////////////
	
	private boolean swipping = false;
	private boolean isRunningSwipe = false;
	class ActivitySwipeDetector implements View.OnTouchListener {

		static final String logTag = "ActivitySwipeDetector";
		static final int MIN_DISTANCE = 0;
		private float downX, downY, upX, upY, saveDownX;
		private float rawDownX, rawDownY, rawUpX, rawUpY;
		private Timer timerMove;
		private ArrayList<Song> listSwipeSong;
		private double startY;

		public ActivitySwipeDetector() {
			listSwipeSong = new ArrayList<Song>();
			startY = (float) listView.getHeight() / 2.2f;
			timerMove = new Timer();
		}

		public void onRightToLeftSwipe() {
			// MyLog.e(logTag, "RightToLeftSwipe!");
		}

		public void onLeftToRightSwipe() {
			// MyLog.e(logTag, "LeftToRightSwipe!");
		}

		private Handler handlerMove = new Handler() {
			public void handleMessage(Message msg) {
				// OnStartLoad();
			};
		};

		private void processListSwipe() {
			float rowHeight = (float) listView.getHeight() / 6f;

			// MyLog.e("processListSwipe", "downY = " + downY
			// + " ---- upY = " + upY + " ---- rowHeight = " + rowHeight);

			int startPos = 0;

			int numItem = (int) ((upY - downY) / rowHeight) + 1;
			startPos = (int) (downY / rowHeight);
			startY = (float) listView.getHeight() / 2.2f + startPos
					* (float) listView.getHeight() / 6.7;

			// MyLog.e("processListSwipe", "startPos = " + startPos
			// + " ---- startY = " + startY + " ---- numItem = " + numItem);

			listSwipeSong = new ArrayList<Song>();
			int count = 0;
			for (int i = 0; i < listArraySongs.size(); i++) {
				if (i >= startPos && count < numItem) {
					listSwipeSong.add(listArraySongs.get(i));
					count++;
				}
			}
		}

		public void onTopToBottomSwipe() {
			// MyLog.e(logTag, "onTopToBottomSwipe!");
			try {
				if (saveDownX > listView.getHeight() / 6) { // ACTIVE
					saveDownX = 0;
					isRunningSwipe = true;
				// MyLog.e("onTopToBottomSwipe", "ACTIVE");
					processListSwipe();
					for (int i = 0; i < listSwipeSong.size(); i++) {
						// MyLog.e("listSwipeSong", i +
						// "...............................");
						final int index = i;
						timerMove.schedule(new TimerTask() {
							@Override
							public void run() {
								if (listener != null) {
									try {
										mainActivity.setFlagSwipe(true);
										listener.onClickItem(
												listSwipeSong.get(index),
												listSwipeSong.get(index).getId()
														+ "",
												TouchMainActivity.SONG, "", -1,
												rawDownX, (float) startY);

										mainActivity.setFlagSwipe(false);
										startY += (float) listView.getHeight() / 6.7;
										handlerMove.sendEmptyMessage(0);	
									} catch (Exception e) {										
									}									
								}
							}
						}, i * 1000);
						if (i == listSwipeSong.size() - 1) {
							timerMove.schedule(new TimerTask() {
								@Override
								public void run() {
									isRunningSwipe = false;
									if (listener != null) {
										handlerMove.sendEmptyMessage(0);
									}
								}
							}, listSwipeSong.size() * 1000);
						}
					}
					return;
				}
			} catch (Exception e) {

			}
		}

		public void onBottomToTopSwipe() {
			// MyLog.e(logTag, "onBottomToTopSwipe!");

		}

		public boolean onTouch(View v, MotionEvent event) {
			
			if(TouchMainActivity.serverStatus == null){
				return false;
			}
			
			switch (event.getAction()) {
			case MotionEvent.ACTION_MOVE: {
				if (downX == 0) {
					if(isRunningSwipe){
						
					} else {
						downX = event.getX();
						saveDownX = downX;
						downY = event.getY();
						rawDownX = event.getRawX();
						rawDownY = event.getRawY();	
					}					
				}				
				return false;
			}
			case MotionEvent.ACTION_UP: {
				
				upX = event.getX();
				upY = event.getY();
				rawUpX = event.getRawX();
				rawUpY = event.getRawY();

				float deltaX = downX - upX;
				float deltaY = downY - upY;

				downX = 0;
				
				// swipe horizontal
				if (Math.abs(deltaX) > Math.abs(deltaY)) {
					if (Math.abs(deltaX) > MIN_DISTANCE) {
						// left or right
						if (deltaX < 0) {
							this.onRightToLeftSwipe();
							return false;
						}
						if (deltaX > 0) {
							this.onLeftToRightSwipe();
							return false;
						}
					} else {
						// MyLog.e(logTag,
						// "Horizontal Swipe was only " + Math.abs(deltaX)
						// + " long, need at least "
						// + MIN_DISTANCE);
						return false;
					}
				}
				// swipe vertical
				else {
					if (Math.abs(deltaY) > MIN_DISTANCE) {
						// top or down
						if (deltaY < 0) {
							this.onTopToBottomSwipe();
							return false;
						}
						if (deltaY > 0) {
							this.onBottomToTopSwipe();
							return false;
						}
					} else {
						// MyLog.e(logTag,
						// "Vertical Swipe was only " + Math.abs(deltaX)
						// + " long, need at least "
						// + MIN_DISTANCE);
						return false;
					}
				}

				return false;
			}
			}
			return false;
		}
	}
	@Override
	public void OnUpdateView() {
		if(adapterSong != null){
			adapterSong.notifyDataSetChanged();
		}
		totalView.invalidate();
		
		changeColorScreen();
	}

	@Override
	public void OnFinishLoad() {
		if (this.isAdded() && adapterSong != null) {
			adapterSong.notifyDataSetChanged();
		}
	}

	
	private Timer timerLoadTotalSong = null;
	private void startLoadTotalSong(final String where, final String type, final String id, int state1, final int state2, 
			ArrayList<Song> listSongs, final Context context , TouchMainActivity mainActivity , int IdThread){
		stopLoadTotalSong();
		timerLoadTotalSong = new Timer();
		timerLoadTotalSong.schedule(new TimerTask() {
			
			@Override
			public void run() {
				MEDIA_TYPE typeMedia;
				switch (state2) {
				case TouchSearchView.VIDEO:	typeMedia = MEDIA_TYPE.VIDEO;		break;
				case TouchSearchView.MIDI:	typeMedia = MEDIA_TYPE.MIDI;		break;
				case TouchSearchView.TATCA:	typeMedia = MEDIA_TYPE.ALL;			break;
				default: typeMedia = MEDIA_TYPE.ALL; break;
				}
				int count = 0;
				if (type.equals(TouchMainActivity.SINGER)) {
					count = DBInterface.DBCountTotalSongTypeID(context, "", SearchType.SEARCH_SINGER, typeMedia, Integer.valueOf(id));
				} else if (type.equals(TouchMainActivity.MUSICIAN)) {
					count = DBInterface.DBCountTotalSongTypeID(context, "", SearchType.SEARCH_MUSICIAN, typeMedia, Integer.valueOf(id));
				} else if (type.equals(TouchMainActivity.SONGTYPE)) {
					count = DBInterface.DBCountTotalSongTypeID(context, where, SearchType.SEARCH_TYPE, typeMedia, Integer.valueOf(id));
				} else if (type.equals(TouchMainActivity.LANGUAGE)) {
					count = DBInterface.DBCountTotalSongTypeID(context,where, SearchType.SEARCH_LANGUAGE, typeMedia, Integer.valueOf(id));
					// MyLog.e(TAB, "Total = " + count);
				} else if (type.equals("")) {
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
				} else {}
				Message message = new Message();
				Bundle data = new Bundle();
				data.putInt("TotalSong", count);
				message.setData(data);
				handlerLoadTotalSong.sendMessage(message);
			}
		}, 500);
	}
	
	private void stopLoadTotalSong(){
		if(timerLoadTotalSong != null){
			timerLoadTotalSong.cancel();
			timerLoadTotalSong = null;
		}
	}
	
	private int checkTotalCount = 0;
	
	private Handler handlerLoadTotalSong = new Handler(){
		public void handleMessage(Message msg) {
			int total = msg.getData().getInt("TotalSong");
			checkTotalCount = total;
			if (total > 0) {
				layoutShowThongBao.setVisibility(View.INVISIBLE);
			} else {
				layoutShowThongBao.setVisibility(View.VISIBLE);
			}
			totalView.setData(total, TouchSearchView.SONG, search, state2);
		};
	};

	@Override
	public void OnLoading_Full() {
//		if(statusTotalView != null){
//			statusTotalView.setStrTitle(getResources().getString(R.string.status_loading_1) + "...");
//			statusTotalView.setVisibility(View.VISIBLE);
//		}
	}

	@Override
	public void OnLoading_Next() {
//		if(statusTotalView != null){
//			statusTotalView.setStrTitle(getResources().getString(R.string.status_loading_2) + "...");
//			statusTotalView.setVisibility(View.VISIBLE);
//		}
	}

	@Override
	public void OnLoading_Fin() {
//		if(statusTotalView != null){
//			statusTotalView.setVisibility(View.GONE);
//		}
	}
	
	private void changeColorScreen(){
		if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			tvThongBao1.setTextColor(Color.parseColor("#21BAA9"));
			tvThongBao2.setTextColor(Color.parseColor("#21BAA9"));
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			tvThongBao1.setTextColor(getResources().getColor(R.color.thong_bao_1));
			tvThongBao2.setTextColor(getResources().getColor(R.color.thong_bao_1));
		}
		
		layoutShowThongBao.invalidate();
	}

	@Override
	public void OnClosePopupYouTube(int position) {
		// TODO Auto-generated method stub
		
	}
}
