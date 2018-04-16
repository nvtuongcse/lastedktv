package vn.com.sonca.Touch.Song;

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
import vn.com.sonca.Touch.touchcontrol.TouchFragmentBase;
import vn.com.sonca.Touch.touchcontrol.TouchItemBack;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity.OnMainListener;
import vn.com.sonca.database.DBInstance.SearchMode;
import vn.com.sonca.database.DBInstance.SearchType;
import vn.com.sonca.database.DBInterface;
import vn.com.sonca.database.DbHelper;
import vn.com.sonca.params.ServerStatus;
import vn.com.sonca.params.Song;
import vn.com.sonca.zzzzz.MainActivity;
import vn.com.sonca.zzzzz.MyApplication;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.utils.AppConfig.MEDIA_TYPE;
import vn.com.sonca.utils.AppSettings;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

public class TouchFragmentSong extends TouchFragmentBase implements
		OnMainListener, OnLoadListener {

	private String TAB = "FragmentSong";
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
	
	@Override
	public void onDetach() {
		if(timerDelaySearch != null){
			timerDelaySearch.cancel();
			timerDelaySearch = null;
		}
		stopAutoLoadNextFirst();
		stopTimerCheckSong();
		super.onDetach();
	}

	private int state1;
	private int state2;
	private Context context;
	private String typeFragment = "";
	private String idFragment = "";
	private ListView listView;
	private TouchAdapterSong adapterSong = null;
	private TouchTotalView totalView;
	private LinearLayout layoutShowThongBao;
	private ArrayList<Song> listArraySongs = null;

	private TouchStatusTotalView statusTotalView;
	private TextView tvThongBao1;
	private TextView tvThongBao2;	
	
	public String getTypeFragment() {
		return typeFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity().getApplicationContext();
		View view = inflater.inflate(R.layout.touch_fragment_song, container,
				false);
		layoutShowThongBao = (LinearLayout)view.findViewById(R.id.layoutShowThongBao);
		layoutShowThongBao.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					if(listener != null){
						listener.OnHideKeyBoard();
					}
				}
				return false;
			}
		});
		view.findViewById(R.id.layoutShow).setOnTouchListener(new OnTouchListener() {
			@Override public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN){
					if(listener != null){
						listener.OnHideKeyBoard();
					}
				}
				return false;
			}
		});
		
		tvThongBao1 = (TextView)view.findViewById(R.id.tvThongBao1);
		tvThongBao2 = (TextView)view.findViewById(R.id.tvThongBao2);
		
//		MyLog.e("FragmentSong", "onCreateView()");
		Bundle bundle = getArguments();
		boolean data = bundle.getBoolean("data");
		if (data == true) {
			idFragment = getArguments().getString("id");
			typeFragment = getArguments().getString("type");
			state2 = getArguments().getInt("state");
			if (state2 == 0) {
				state2 = TouchSearchView.TATCA;
			}
			search = getArguments().getString("search");
			if (search == null) {
				search = "";
			}
			if (listener != null) {
				listener.OnNameSearch(idFragment, typeFragment);
			}
		} else {
			search = "";
			state1 = bundle.getInt("state1");
			state2 = bundle.getInt("state2");
		}
		// -----------------//
		listView = (ListView) view.findViewById(R.id.listview);

		totalView = (TouchTotalView) view.findViewById(R.id.totalview);
		totalView.setDisplay(true);
		totalView.setVisibility(View.VISIBLE);

		if(bundle.getBoolean("clickTheloai", false)){
			totalView.setContext(context);
			totalView.setLayoutTheLoai(idFragment);
		}
		
		statusTotalView = (TouchStatusTotalView) view.findViewById(R.id.statusTotalView);
		statusTotalView.setVisibility(View.GONE);
		
		stateSearch = state2;
		// MyLog.e(TAB, "idFragment : " + idFragment);
		// MyLog.e(TAB, "typeFragment : " + typeFragment);
		// MyLog.e(TAB, "search : " + search);
		// MyLog.e(TAB, "state1 : " + state1);
		// MyLog.e(TAB, "state2 : " + state2);

		changeColorScreen();
		
		CreateLoadData(state1, state2, search);
		startTimerCheckSong();
		
		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}
	
	public void forceCloseDB() {
		if (loadData != null) {
			MyLog.e("FragmentSong","forceCloseDB START");
			loadData.cancel(true);	
//			while(loadData.isRunning()){
//
//			}
			
			MyLog.e("FragmentSong","forceCloseDB STOP");
			loadData = null;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		
		if(timerDelaySearch != null){
			timerDelaySearch.cancel();
			timerDelaySearch = null;
		}

		stopLoadTotalSong();
		stopAutoLoadNextFirst();
		stopTimerCheckSong();
		if (listener != null) {
			listener.OnNameSearch("", "");
		}
		if (loadData != null) {
			loadData.cancel(true);
//			while (loadData.isRunning()) {
//
//			}
			MyLog.e("FragmentSong", "onDestroy");
			loadData = null;
		}	
	}
	
	// /////////////////////////// - DATABASE - //////////////////////////////

	private Timer timerDelaySearch;
	private void startTimerDelaySearch(final int state1, final int state2, final String search) {
		long delayTime = 200;
		if(search.trim().equals("") || search.trim().equals(" ")){
			delayTime = 200;
		}
		if(timerDelaySearch != null){
			timerDelaySearch.cancel();
			timerDelaySearch = null;
		}
		timerDelaySearch = new Timer();
		timerDelaySearch.schedule(new TimerTask() {
			
			@Override
			public void run() {
				stopAutoLoadNextFirst();
				StopLoadData();
				handler.sendEmptyMessage(0);
			}
			
			private Handler handler = new Handler(){
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					
					IdThread++;
					
					countLoadNext = 0;
					loadNextTime = System.currentTimeMillis();
					
					AppSettings setting = AppSettings.getInstance(context);
//					if (setting.loadServerLastUpdate() != 0) {
						// if(setting.isUpdated()) {
						
						if(layoutShowThongBao != null){
							layoutShowThongBao.setVisibility(View.INVISIBLE);	
						}
						
						int idLanguage = 0;
						if (typeFragment.equals(TouchMainActivity.LANGUAGE)) {
							idLanguage = Integer.valueOf(idFragment);
						}
//						if(listArraySongs != null){
//							listArraySongs.clear();
//							adapterSong.notifyDataSetChanged();
//						}
							//----------//
						listArraySongs = new ArrayList<Song>();
						adapterSong = new TouchAdapterSong(context, R.layout.touch_item_song_list,
								listArraySongs, search, idLanguage, mainActivity);
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
							public void OnSingerLink(boolean isMIDI, String name, int[] idSinger) {
								if (listener != null) {
									if(isMIDI == true){
										if (!typeFragment.equals(TouchMainActivity.MUSICIAN) && !name.equals("-")) {
											((MyApplication) context)
													.addListBack(new TouchItemBack(
															TouchMainActivity.SONG,
															typeFragment, idFragment,
															search, stateSearch, 0));
											listener.OnSingerLink(true, name, idSinger);
										}
									} else {
										if (!typeFragment.equals(TouchMainActivity.SINGER) && !name.equals("-")) {
											((MyApplication) context)
													.addListBack(new TouchItemBack(
															TouchMainActivity.SONG,
															typeFragment, idFragment,
															search, stateSearch, 0));
											listener.OnSingerLink(false, name, idSinger);
										}
									}
								}
							}

							@Override
							public void OnLockNotify() {}

							@Override
							public void OnUnLockNotify() {}

							@Override
							public void OnShowLyric(int position, Song song) {
								MyLog.e(TAB, "OnShowLyric : " + song.getName());
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
							//----------//
//						StopLoadData();
							//----------//
						
						loadData = new LoadSongFromDatabase(search, typeFragment,
								idFragment, state1, state2, listArraySongs, context,
								mainActivity, IdThread);
						loadData.setOnLoadListener(TouchFragmentSong.this);
						loadData.execute();
						
						if(search != null && search.trim().length() > 0){
							timeDelayLoadNext = 3000;
						} else {
							timeDelayLoadNext = 0;
						}
//					}
				
				}
			};
		}, delayTime);
		
	}
	
	private void StopLoadData() {
		if (loadData != null) {
			loadData.cancel(true);
			loadData = null;
		}
	}

	private int IdThread = 0;
	private LoadSongFromDatabase loadData;

	private void CreateLoadData(int state1, int state2, final String search) {
		startTimerDelaySearch(state1, state2, search);
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
			case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
				if (listArraySongs != null && listArraySongs.size() == 6) {
					totalView.setDisplay(flagDisplay);
					if (flagDisplay) {
						totalView.setVisibility(View.VISIBLE);
					} else {
						totalView.setVisibility(View.GONE);
					}
					flagDisplay = !flagDisplay;
				}
				if(listener != null){
					listener.OnHideKeyBoard();
				}
				break;
			case OnScrollListener.SCROLL_STATE_IDLE:
				/*
				if(loadData != null){
					loadData.loadNextPage();
				}
				*/
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
				
				if(flagFilterOnline){
					if(MyApplication.flagFinishLoadDBOnline){
						if (System.currentTimeMillis() - loadNextTime > 2000) {
							loadData.loadNextPage();
							loadNextTime = System.currentTimeMillis();
							countLoadNext++;
						}
					}
					return;
				}
				
				if(total - tempRowTrigger > 0){
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
							if (countLoadNext == 0) {
								autoLoadNextFirst();
								
							} else {
								loadData.loadNextPage();
								loadNextTime = System.currentTimeMillis();
							}
							
						}
						
						
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
	
	public void stopAutoLoadNextFirst(){
		if(timerAutoLoadNextFirst != null){
			timerAutoLoadNextFirst.cancel();
			timerAutoLoadNextFirst = null;
		}		
	}
	
	private Timer timerAutoLoadNextFirst = null;
	private void autoLoadNextFirst(){
		if(timerAutoLoadNextFirst != null){
			timerAutoLoadNextFirst.cancel();
			timerAutoLoadNextFirst = null;
		}
		timerAutoLoadNextFirst = new Timer();
		timerAutoLoadNextFirst.schedule(new TimerTask() {
			
			@Override
			public void run() {
				if (loadData != null) {
					loadData.loadNextPage();
					loadNextTime = System.currentTimeMillis();
					countLoadNext++;					
				}
			}
		}, 3000);
	}

	@Override
	public void OnClearList() {
		if (this.isAdded() && adapterSong != null) {
			adapterSong.notifyDataSetChanged();
		}
	}
	
	@Override
	public void OnStartLoad(int idThread) {
		if (this.isAdded()) {
//			if (MyApplication.intWifiRemote == MyApplication.SONCA) {
//				if (listArraySongs.size() <= 6) {
//					swipping = true;
//					ActivitySwipeDetector activitySwipeDetector = new ActivitySwipeDetector();
//					listView.setOnTouchListener(activitySwipeDetector);
//				} else {
//					swipping = false;
//					listView.setOnTouchListener(null);
//					stateScrollListView = OnScrollListener.SCROLL_STATE_IDLE;
//				}
//			} else {
//				swipping = false;
//			}
			
			if(adapterSong != null){
				// MyLog.e("LoadSongFromDatabase", idThread + " - notifyDataSetChanged()");
				adapterSong.notifyDataSetChanged();
				if(!listArraySongs.isEmpty()){
					listView.setSelection(0);
				}
			}
		}
		
		startLoadTotalSong(search, typeFragment,
				idFragment, state1, state2, listArraySongs, context,
				mainActivity, IdThread);
		
	}

	@Override
	public void OnLoading() {
//		MyLog.e("OnLoading", "...................................");
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
	public void OnLoadSucessful() {}

	private String search = "";
	private int stateSearch = TouchSearchView.TATCA;

	@Override
	public void OnSearchMain(int state1, int state2, String search) {
		if (isAdded()) {
			MyApplication.flagFinishLoadDBOnline = true;			
			if(this.state2 != state2 || !this.search.equals(search.trim())){
				CreateLoadData(state1, state2, search.trim());
			}
			stateSearch = state2;
			this.search = search.trim();
			this.state1 = state1;
			this.state2 = state2;
		}
	}

	@Override
	protected void UpdateAdapter() {
	}

	@Override
	public void OnUpdateImage() {

	}

	@Override
	public void OnUpdateCommad(ServerStatus status) {

	}

	///////////////////////////////////////////////////////////////
//	private boolean swipping = false;
//	private boolean isRunningSwipe = false;
//	class ActivitySwipeDetector implements View.OnTouchListener {
//
//		static final String logTag = "ActivitySwipeDetector";
//		static final int MIN_DISTANCE = 0;
//		private float downX, downY, upX, upY, saveDownX;
//		private float rawDownX, rawDownY, rawUpX, rawUpY;
//		private Timer timerMove;
//		private ArrayList<Song> listSwipeSong;
//		private double startY;
//		
//		public ActivitySwipeDetector() {
//			listSwipeSong = new ArrayList<Song>();
//			startY = (float)listView.getHeight() / 2.2f;
//			timerMove = new Timer();
//		}
//
//		public void onRightToLeftSwipe() {
////			MyLog.e(logTag, "RightToLeftSwipe!");
//		}
//
//		public void onLeftToRightSwipe() {
////			MyLog.e(logTag, "LeftToRightSwipe!");
//		}
//
//		private Handler handlerMove = new Handler() {
//			public void handleMessage(Message msg) {
////				OnStartLoad();
//			};
//		};
//		
//		private void processListSwipe(){			
//			float rowHeight = (float) listView.getHeight() / 6f;
//			
////			MyLog.e("processListSwipe", "downY = " + downY
////					+ " ---- upY = " + upY + " ---- rowHeight = " + rowHeight);
//
//			int startPos = 0;
//
//			int numItem = (int) ((upY - downY) / rowHeight) + 1;
//			startPos = (int) (downY / rowHeight);
//			startY = (float) listView.getHeight() / 2.2f + startPos
//					* (float) listView.getHeight() / 6.7;
//
////			MyLog.e("processListSwipe", "startPos = " + startPos
////					+ " ---- startY = " + startY + " ---- numItem = " + numItem);
//
//			listSwipeSong = new ArrayList<Song>();
//			int count = 0;
//			for (int i = 0; i < listArraySongs.size(); i++) {
//				if (i >= startPos && count < numItem) {
//					listSwipeSong.add(listArraySongs.get(i));
//					count++;
//				}
//			}
//		}
//		
//		public void onTopToBottomSwipe() {
////			MyLog.e(logTag, "onTopToBottomSwipe!");
//			try {
//				if (saveDownX > listView.getHeight() / 6) { // ACTIVE
//					saveDownX = 0;
//					isRunningSwipe = true;
////					MyLog.e("onTopToBottomSwipe", "ACTIVE");
//					processListSwipe();
//					for (int i = 0; i < listSwipeSong.size(); i++) {
////						MyLog.e("listSwipeSong", i + "...............................");
//						final int index = i;
//						timerMove.schedule(new TimerTask() {
//							@Override
//							public void run() {
//								if (listener != null) {
//									try {
//										mainActivity.setFlagSwipe(true);
//										listener.onClickItem(listSwipeSong.get(index),
//												listSwipeSong.get(index).getId() + "",
//												TouchMainActivity.SONG, "", -1, rawDownX,
//												(float)startY);
//
//										mainActivity.setFlagSwipe(false);
//										startY += (float)listView.getHeight() / 6.7;
//										handlerMove.sendEmptyMessage(0);	
//									} catch (Exception e) {										
//									}									
//								}
//							}
//						}, i * 1000);
//						if(i == listSwipeSong.size()-1){
//							timerMove.schedule(new TimerTask() {
//								@Override
//								public void run() {
//									isRunningSwipe = false;
//									if (listener != null) {			
//										handlerMove.sendEmptyMessage(0);
//									}
//								}
//							}, listSwipeSong.size() * 1000);
//						}
//					}
//					return;
//				}
//			} catch (Exception e) {
//				
//			}			
//		}
//
//		public void onBottomToTopSwipe() {
////			MyLog.e(logTag, "onBottomToTopSwipe!");
//
//		}
//
//		public boolean onTouch(View v, MotionEvent event) {	
//			
//			if(TouchMainActivity.serverStatus == null){
//				return false;
//			}
//			
//			switch (event.getAction()) {			
//			case MotionEvent.ACTION_MOVE: {
//				if (downX == 0) {
//					if(isRunningSwipe){
//						
//					} else {
//						downX = event.getX();
//						saveDownX = downX;
//						downY = event.getY();
//						rawDownX = event.getRawX();
//						rawDownY = event.getRawY();	
//					}					
//				}				
//				return false;
//			}
//			case MotionEvent.ACTION_UP: {
//				
//				upX = event.getX();
//				upY = event.getY();
//				rawUpX = event.getRawX();
//				rawUpY = event.getRawY();
//
//				float deltaX = downX - upX;
//				float deltaY = downY - upY;
//				
//				downX = 0;
//
//				// swipe horizontal
//				if (Math.abs(deltaX) > Math.abs(deltaY)) {
//					if (Math.abs(deltaX) > MIN_DISTANCE) {
//						// left or right
//						if (deltaX < 0) {
//							this.onRightToLeftSwipe();
//							return false;
//						}
//						if (deltaX > 0) {
//							this.onLeftToRightSwipe();
//							return false;
//						}
//					} else {
////						MyLog.e(logTag,
////								"Horizontal Swipe was only " + Math.abs(deltaX)
////										+ " long, need at least "
////										+ MIN_DISTANCE);
//						return false;
//					}
//				}
//				// swipe vertical
//				else {
//					if (Math.abs(deltaY) > MIN_DISTANCE) {
//						// top or down
//						if (deltaY < 0) {
//							this.onTopToBottomSwipe();
//							return false;
//						}
//						if (deltaY > 0) {
//							this.onBottomToTopSwipe();
//							return false;
//						}
//					} else {
////						MyLog.e(logTag,
////								"Vertical Swipe was only " + Math.abs(deltaX)
////										+ " long, need at least "
////										+ MIN_DISTANCE);
//						return false;
//					}
//				}
//
//				return false;
//			}
//			}
//			return false;
//		
//		
//		}
//	}

	@Override
	public void OnUpdateView() {
		if(isAdded()){
			if(adapterSong != null){
				adapterSong.notifyDataSetChanged();
			}
			totalView.invalidate();
			
			changeColorScreen();
			
		}
	}

	@Override
	public void OnFinishLoad() {
		if (isAdded()) {
			if(adapterSong != null){
				adapterSong.notifyDataSetChanged();
			}
		}
	}
	
	private boolean flagFilterOnline = false;
	private Timer timerLoadTotalSong = null;
	private void startLoadTotalSong(final String where, final String type, final String id, int state1, final int state2, 
			ArrayList<Song> listSongs, final Context context , TouchMainActivity mainActivity , int IdThread){
		stopLoadTotalSong();
		timerLoadTotalSong = new Timer();
		timerLoadTotalSong.schedule(new TimerTask() {
			
			@Override
			public void run() {
				flagFilterOnline = false;
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
	
	private int checkTotalCount = 0;
	private Handler handlerLoadTotalSong = new Handler(){
		public void handleMessage(Message msg) {
			int total = msg.getData().getInt("TotalSong");
			checkTotalCount = total;
			MyLog.e(TAB, "handlerLoadTotalSong : " + total);
			if (total > 0) {
				layoutShowThongBao.setVisibility(View.INVISIBLE);
			} else {
				layoutShowThongBao.setVisibility(View.VISIBLE);
			}
			totalView.setData(total, TouchSearchView.SONG, search, state2);
		};
	};
	

	private Timer timerCheckSong;
	private int countFail = 0;

	class CheckSongTask extends TimerTask {

		@Override
		public void run() {
			//MyLog.e("timerCheckSong", "......................idFragment = " + idFragment);
			
			if(!idFragment.equals("")){
				return;
			}
			
			if(MyApplication.intWifiRemote != MyApplication.SONCA || state2 != 3){
				return;
			}
			
			if(totalView == null){
				countFail = 0;
				return;
			}
			
			//MyLog.e("", totalView.getSum() + "");
			
			if(totalView.getSum() > 0){
				countFail = 0;
				return;
			}
			
			if(search != null && !search.equals("")){
				countFail = 0;
				return;
			}

			countFail++;
			MyLog.e("", "countFail = " + countFail);
			if(countFail > 30){
				handlerCheckSong.sendEmptyMessage(0);
			}
		}
	};
	
	private void startTimerCheckSong() {
		if(true){
			return;
		}
		stopTimerCheckSong();
		//MyLog.e("startTimerCheckSong", ".....................");
		timerCheckSong = new Timer();
		timerCheckSong.schedule(new CheckSongTask(), 3000, 1000);
	}

	private void stopTimerCheckSong() {
		//MyLog.e("startTimerCheckSong", ".....................");
		if (timerCheckSong != null) {
			timerCheckSong.cancel();
			timerCheckSong = null;
		}
	}
	
	private Handler handlerCheckSong = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			forceCloseDB();
			mainActivity.revertDB();
			stopTimerCheckSong();
		}
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
