package vn.com.sonca.Touch.Dance;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.CustomView.SliderMuteView;
import vn.com.sonca.Touch.CustomView.SliderMuteView.OnMuteListener;
import vn.com.sonca.Touch.CustomView.SliderView;
import vn.com.sonca.Touch.CustomView.SliderView.OnSliderListener;
import vn.com.sonca.Touch.CustomView.TouchDanceView;
import vn.com.sonca.Touch.CustomView.TouchNextView;
import vn.com.sonca.Touch.CustomView.TouchPauseView;
import vn.com.sonca.Touch.CustomView.TouchRepeatView;
import vn.com.sonca.Touch.CustomView.TouchSearchDance;
import vn.com.sonca.Touch.CustomView.TouchListView;
import vn.com.sonca.Touch.CustomView.TouchVolumnView;
import vn.com.sonca.Touch.CustomView.TouchDanceView.OnDancetListener;
import vn.com.sonca.Touch.CustomView.TouchNextView.OnNextListener;
import vn.com.sonca.Touch.CustomView.TouchPauseView.OnPauseListener;
import vn.com.sonca.Touch.CustomView.TouchRepeatView.OnRepeatListener;
import vn.com.sonca.Touch.CustomView.TouchSearchDance.OnSearchDanceClick;
import vn.com.sonca.Touch.CustomView.TouchVolumnView.OnVolumnListener;
import vn.com.sonca.Touch.Listener.TouchIAdapter;
import vn.com.sonca.Touch.touchcontrol.TouchFragmentBase;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity.OnMainListener;
import vn.com.sonca.database.DBInterface;
import vn.com.sonca.params.ServerStatus;
import vn.com.sonca.params.Song;
import vn.com.sonca.smartkaraoke.NetworkSocket;
import vn.com.sonca.utils.Lock;
import vn.com.sonca.zktv.main.KTVMainActivity;
import vn.com.sonca.zzzzz.MyApplication;

public class TouchFragmentDance extends TouchFragmentBase implements OnMainListener{
	
	private String TAB = "FragmentSong";
	private TouchMainActivity mainActivity;
	private KTVMainActivity ktvMainActivity;
	private OnFragmentDanceListener listener;

	private Dialog myDialogSlider;
	private SliderView sliderView;
	private SliderMuteView muteView;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (OnFragmentDanceListener) activity;
			if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				ktvMainActivity = (KTVMainActivity) activity;
				layoutMain = ktvMainActivity.getLayoutMain();
			} else {
				mainActivity = (TouchMainActivity) activity;
				layoutMain = mainActivity.getLayoutMain();	
			}
			
		} catch (Exception ex) {}
	}
	
	private FragmentActivity getMyActivity(){
		if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			return this.ktvMainActivity;
		} else {
			return this.mainActivity;
		}
	}
	
	public interface OnFragmentDanceListener {
		public void OnCreateView();
		public void OnStopDance();
		public void onMute(boolean isMute);
		public void OnFirstClick(Song song , int position);
		public void onMoveSong(ArrayList<Song> list);
		public void OnDestroyView(int value , boolean bool);
		public void OnVolumnView(int value);
		public void OnPause(boolean value);
		public void OnRanDom();
		public void OnNext();
		public void OnRepeat();
	}
	
	private Context context;
	private TouchPauseView pauseView;
	private TouchVolumnView volumnView;
	private TouchListView listView;
	private TouchSearchDance searchDance;
	private TouchDanceView danceView;
	private TouchNextView nextView;
	private TouchRepeatView repeatView;
	private TouchAdapterDance adapterSong;
	
	private RelativeLayout layoutMain;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		((MyApplication)getMyActivity().getApplication()).setListActive(new ArrayList<Song>());
		context = getActivity().getApplicationContext();
		
		int volumn = getArguments().getInt("Volumn");
		boolean mute = getArguments().getBoolean("Mute");
		boolean pause = getArguments().getBoolean("Pause");
		
		//----------------DIALOG SLIDER-------------------//
				myDialogSlider = new Dialog(getMyActivity());
				myDialogSlider.requestWindowFeature(Window.FEATURE_NO_TITLE);
				myDialogSlider.getWindow().setBackgroundDrawable(
						new ColorDrawable(android.graphics.Color.TRANSPARENT));
				myDialogSlider.setContentView(R.layout.item_popup_slider);
				myDialogSlider.setCanceledOnTouchOutside(true);

				WindowManager.LayoutParams myPa = myDialogSlider.getWindow()
						.getAttributes();
				myPa.flags =  WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE 
						| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
						| WindowManager.LayoutParams.FLAG_FULLSCREEN;
				
				myDialogSlider.setOnShowListener(new OnShowListener() {
					
					@Override
					public void onShow(DialogInterface dialog) {
						AlphaAnimation alpha = new AlphaAnimation(1F, 0.5F);
						alpha.setDuration(0);
						alpha.setFillAfter(true);
						if(layoutMain != null){
							layoutMain.startAnimation(alpha);	
						}
					}
				});
				
				myDialogSlider.setOnDismissListener(new OnDismissListener() {
					
					@Override
					public void onDismiss(DialogInterface dialog) {
						AlphaAnimation alpha = new AlphaAnimation(0.5F, 1F);
						alpha.setDuration(0);
						alpha.setFillAfter(true);
						if(layoutMain != null){
							layoutMain.startAnimation(alpha);	
						}
						volumnView.setVisibility(View.VISIBLE);
						if(timerHideDialogThanhTruot != null){
							timerHideDialogThanhTruot.cancel();
							timerHideDialogThanhTruot = null;
						}
					}
				});
						
				mainActivity.setMydialogSliderDance(myDialogSlider);
				
				sliderView = (SliderView) myDialogSlider
						.findViewById(R.id.mySlider);
				muteView = (SliderMuteView) myDialogSlider.findViewById(R.id.myMute);
				RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)muteView.getLayoutParams();
				params.setMargins(-40 * getResources().getDisplayMetrics().widthPixels / 1980, 0, 0, 0);
				muteView.setLayoutParams(params);

				muteView.setOnMuteListener(new OnMuteListener() {

					@Override
					public void OnMuteSlider(boolean isMute) {
						boolean flag = muteView.getMute();
						if(MyApplication.intWifiRemote == MyApplication.SONCA){
							((MyApplication) context.getApplicationContext()).sendCommand(
									NetworkSocket.REMOTE_CMD_MUTE, flag ? 0 : 1);
						}
						muteView.setMute(!flag);
					}
				});

				sliderView.setOnSliderListener(new OnSliderListener() {

					@Override
					public void OnSlider(int senderType, int value) {
						MyApplication.freezeTime = System.currentTimeMillis();
						hideDialogThanhTruot();
						// MyLog.e(TAB, "onSlider() receiver  value: " + senderType
						// + "  " + value);
						switch (senderType) {
						case SliderView.SENDER_VOLUMN:
							// if (!volumnView.getMute()) {
							// return;
							// }
							volumnView.setVolumn(value);
							if(MyApplication.intWifiRemote == MyApplication.SONCA){
								((MyApplication) context.getApplicationContext()).sendCommand(
										NetworkSocket.REMOTE_CMD_VOLUME, value);
							}
							break;
						default:
							break;
						}
					}

					@Override
					public void OnSliderIncrease() {
						// not used anymore
					}

					@Override
					public void OnSliderDecrease() {
						// not used anymore
					}

				});
				
				
		View view = inflater.inflate(R.layout.touch_fragment_dancee, container, false);
		searchDance = (TouchSearchDance)view.findViewById(R.id.SearchDance);
		searchDance.setOnSearchDanceClick(new OnSearchDanceClick() {
			@Override public void OnRandom() {
				if(listener != null){
					ArrayList<Song> list = RandomPlayListDevice(listSongs);
					listener.onMoveSong(list);
				}
			}
		});
		danceView = (TouchDanceView)view.findViewById(R.id.DanceView);
		danceView.setOnDancetListener(new OnDancetListener() {
			@Override public void OnClick() {
				if(listener != null){
					listener.OnStopDance();
				}
			}

			@Override
			public void OnInActive() {
				// TODO Auto-generated method stub
				
			}
		});
		volumnView = (TouchVolumnView)view.findViewById(R.id.VolumnView);
		volumnView.setVolumn(volumn);
		volumnView.setMute(mute);
		volumnView.setEnableView(View.VISIBLE);
		volumnView.setOnVolumnListener(new OnVolumnListener() {
			@Override public void onVolumn(int value) {
				if(listener != null){
					listener.OnVolumnView(value);
				}
			}

			@Override
			public void onMute(boolean isMute) {
				if(listener != null){
					listener.onMute(isMute);
				}
			}

			@Override
			public void OnInActive() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void OnShowTab(int intSliderVolumn) {
				muteView.setVisibility(View.VISIBLE);
				if(serverStatus != null){
					muteView.setMute(serverStatus.isMuted());
				}

				volumnView.setVisibility(View.INVISIBLE);

				sliderView.setSliderData(true,
						SliderView.SENDER_VOLUMN, SliderView.SLIDER_TYPE0,
						intSliderVolumn);

				myDialogSlider.getWindow().setGravity(
						Gravity.TOP | Gravity.LEFT);
				WindowManager.LayoutParams params = myDialogSlider
						.getWindow().getAttributes();
				params.x = (int) (5 * getResources().getDisplayMetrics().widthPixels / 1980);
				params.y = (int) (1.75 * getResources().getDisplayMetrics().heightPixels / 4);

				myDialogSlider.getWindow().setAttributes(params);
				
				myDialogSlider.show();
				hideDialogThanhTruot();
			}
		});
		repeatView = (TouchRepeatView) view.findViewById(R.id.repeatView);
		repeatView.setBigLayout(true);
		repeatView.setOnRepeatListener(new OnRepeatListener() {
			
			@Override
			public void onRepeat() {
				if(listener != null){
					listener.OnRepeat();
				}
			}

			@Override
			public void OnInActive() {
				// TODO Auto-generated method stub
				
			}
		});
		
		pauseView = (TouchPauseView)view.findViewById(R.id.pauseView);
		pauseView.setPauseView(pause);
		pauseView.setBigLayout(true);
		pauseView.setOnPauseListener(new OnPauseListener() {
			@Override public void onPause(boolean isPlaying) {
				if(listener != null){
					listener.OnPause(isPlaying);
				}
			}

			@Override
			public void OnInActive() {
				// TODO Auto-generated method stub
				
			}
		});
		nextView = (TouchNextView)view.findViewById(R.id.NextView);
		nextView.setBigLayout(true);
		nextView.setOnNextListener(new OnNextListener() {
			@Override public void onNext() {
				if(listener != null){
					listener.OnNext();
				}
			}

			@Override
			public void OnInActive() {
				// TODO Auto-generated method stub
				
			}
		});
		
		isEnableFirst = true;
		listView = (TouchListView)view.findViewById(R.id.listView);
		listView.setDropListener(onDrop);
		listView.setRemoveListener(onRemove);
		
		listSongs = new ArrayList<Song>();
		// ((MyApplication) mainActivity.getApplication()).setListActive(new ArrayList<Song>());
		loadPlayList = new LoadPlayList(listSongs);
		loadPlayList.start();
		
		adapterSong = new TouchAdapterDance(context, R.layout.touch_item_song_list, listSongs, mainActivity);
		adapterSong.setOnAdapterListener(new TouchIAdapter() {
			@Override public void onPlaySong(Song song, int position, float x, float y) {}
			@Override public void onDeleteSong(Song song, int position) {}
			@Override public void OnSingerLink(boolean bool, String nameSinger, int[] idSinger) {}
			@Override public void OnItemFavourite(int position, Song song) {}
			@Override public void OnItemActive(Song song, String id, float x, float y) {}
			@Override
			public void OnFirstClick(Song song, int position, float x, float y) {
				if(MyApplication.bOffFirst == true){
					if(listener != null && isEnableFirst){
						listener.OnFirstClick(song, position);
						if(position != 0){
							isEnableFirst = false;
							WaitEnableFirst(5000);
						}
					}
				}
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
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onPlayYouTube(Song song) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onDownYouTube(Song song) {
				// TODO Auto-generated method stub
				
			}
		});
		listView.setAdapter(adapterSong);
		danceView.setEnableView(true);
		pauseView.setConnect(true);
		nextView.setConnect(true);
		repeatView.setConnect(true);
		
		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		danceView.setTextName("KARAOKE");
	}
	
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if(timerHideDialogThanhTruot != null){
			timerHideDialogThanhTruot.cancel();
			timerHideDialogThanhTruot = null;
		}
		cancelMove();
		if(listener != null && volumnView != null && pauseView != null){
			((MyApplication)getMyActivity().getApplication()).setListActive(new ArrayList<Song>());
			listener.OnDestroyView(volumnView.getVolumn(), pauseView.getPauseView());
		}
	}
	
///////////////////////////// - SEND MOVE - //////////////////////////////	
	
	private void cancelMove() {
		if (timerMove != null) {
			timerMove.cancel();
			timerMove = null;
		}
	}

	private Timer timerMove;

	private void sendMove() {
		cancelMove();
		timerMove = new Timer();
		timerMove.schedule(new TimerTask() {
			@Override
			public void run() {
				handlerMove.removeMessages(0);
				handlerMove.sendEmptyMessage(0);
			}
		}, 1000);
	}

	private Handler handlerMove = new Handler() {
		public void handleMessage(Message msg) {
			if (listener != null && listSongs != null) {
				ArrayList<Song> list = UpdatePlayListDevice(listSongs);
				listener.onMoveSong(list);
			}
		};
	};
	
/////////////////////////////////////////////////////////
	
	private ArrayList<Song> RandomPlayListDevice(ArrayList<Song> listIn){
		ArrayList<Song> output = new ArrayList<Song>();
		ArrayList<Song> listInPut = new ArrayList<Song>(listIn);
		for (int i = 0; i < listInPut.size(); i++) {
			if(i < 5){
				if(listIn.size() == 0) break;
				int random = (int) (listInPut.size()*Math.random());
				Song song = listInPut.get(random).copySong();
				listInPut.remove(random);
				output.add(song);
			}else{
				break;
			}
		}
		output.addAll(listInPut);
		return output;
	}

	private ArrayList<Song> UpdatePlayListDevice(ArrayList<Song> listIn){
		ArrayList<Song> output = new ArrayList<Song>();
		ArrayList<Song> listInPut = new ArrayList<Song>(listIn);
		ArrayList<Song> playlist = ((MyApplication)getMyActivity().getApplication()).getListActive();
		for (int i = 0; i < listInPut.size() ; i++) {
			Song song = listInPut.get(i);
			if(playlist.contains(song)){
				output.add(listInPut.get(i));
				playlist.remove(song);
			}else{
				listInPut.remove(song);
			}
		}
		output.addAll(playlist);
			//----------------
		return output;
	}
	
	private boolean isMoveDone = true;
	private TouchListView.DropListener onDrop = new TouchListView.DropListener() {
		@Override
		public void drop(int from, int to) {
			Song item = adapterSong.getItem(from);
			adapterSong.remove(item);
			adapterSong.insert(item, to);
			sendMove();
			isMoveDone = true; 
		}
		@Override public void start() {
			isMoveDone = false;
		}
		@Override
		public void drop() {
			isMoveDone = true; 
		}
	};

	private TouchListView.RemoveListener onRemove = new TouchListView.RemoveListener() {
		@Override public void remove(int which) {}
	};

/////////////////////////////////////////////////////////
	private boolean isEnableFirst = true;
	class LoadPlayList extends Thread {
		
		private ArrayList<Song> listCurrent;
		private ArrayList<Song> listReturn;
		
//		private Lock lock = new Lock();
		public LoadPlayList(ArrayList<Song> listCurrent) {
			this.listCurrent = listCurrent;
			listReturn = new ArrayList<Song>();
			state = false;
		}
		
		private boolean state;
		public boolean getStateThread(){
			return state;
		}
		
		@Override
		public void run() {
			super.run();
			try {
//				lock.lock();
				
				ArrayList<Song> list = ((MyApplication) getMyActivity().getApplication()).getListActive();
				
				ArrayList<Song> playlist = new ArrayList<Song>(list);
				
				if (playlist.size() - listCurrent.size() >= 3) {
					listReturn = DBInterface.DBSearchListSongID(playlist, context);					
				} else {					
					for (int i = 0; i < playlist.size(); i++) {
						Song song = playlist.get(i);
						int index = listCurrent.indexOf(song);
						
						if (index != -1) {
							listReturn.add(listCurrent.get(index));
						} else {
							String id = String.valueOf(song.getIndex5());
							String abc = String.valueOf(song.getTypeABC());
							ArrayList<Song> songs = DBInterface.DBSearchSongID(
									id, abc, context);
							if (!songs.isEmpty()) {
								listReturn.add(songs.get(0));
							}
						}
					}
				}
					
				handler.sendEmptyMessage(0);
//				lock.unlock();
				state = true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		private Handler handler = new Handler(){
			public void handleMessage(Message msg) {
				listSongs.clear();
				listSongs.addAll(listReturn);
				adapterSong.notifyDataSetChanged();
				MyLog.e(TAB, "adapter - notifyDataSetChanged");
				if(searchDance != null && listSongs != null){
					searchDance.setSumSong(listSongs.size(), true);
				}
				WaitEnableFirst(500);
			};
		};
	}
	
	private Timer timerEnableFirst = null;
	private void WaitEnableFirst(int tgian){
		if(timerEnableFirst != null){
			timerEnableFirst.cancel();
			timerEnableFirst = null;
		}
		timerEnableFirst = new Timer();
		timerEnableFirst.schedule(new TimerTask() {
			@Override public void run() {
				isEnableFirst = true;
			}
		}, tgian);
	}
	
/////////////////////////////////////////////////////////

	@Override
	public void OnLoadSucessful() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnSearchMain(int state1, int state2, String search) {
		// TODO Auto-generated method stub
		
	}

	private LoadPlayList loadPlayList;
	@Override
	public void OnSK90009() {
		if(isAdded() && loadPlayList != null){
			if(isMoveDone && loadPlayList.getStateThread()){
//				MyLog.e(TAB, "playlist - OnSK90009()");
				loadPlayList = new LoadPlayList(listSongs);
				loadPlayList.start();
			}
		}
	}

	@Override
	protected void UpdateAdapter() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void OnUpdateImage() {
		// TODO Auto-generated method stub
		
	}

	private ServerStatus serverStatus = null;
	@Override
	public void OnUpdateCommad(ServerStatus status) {
//		MyLog.i(TAB, "Volume - " + status.getCurrentVolume());
//		MyLog.i(TAB, "Mute - " + status.isMuted());

		if(isAdded()){
			if(volumnView != null && pauseView != null){
				if (serverStatus == null) {
					pauseView.setPauseView(!status.isPaused());
					volumnView.setVolumn(status.getCurrentVolume());
					volumnView.setMute(!status.isMuted());
				} else {
					if (status.getCurrentVolume() != volumnView.getVolumn()) {
						volumnView.setVolumn(status.getCurrentVolume());
						volumnView.setMute(!status.isMuted());
						muteView.setMute(status.isMuted());
					}
					if (status.isMuted() == volumnView.isMute()) {
						volumnView.setVolumn(status.getCurrentVolume());
						volumnView.setMute(!status.isMuted());
						muteView.setMute(status.isMuted());
					}
					if(muteView.getMute()!= status.isMuted()){
						muteView.setMute(status.isMuted());
					}
					if (status.isPaused() == pauseView.getPauseView())
						pauseView.setPauseView(!status.isPaused());
					
					if (sliderView != null && myDialogSlider != null
							&& myDialogSlider.isShowing()) {
						int value = sliderView.getMainVolumn();
						switch (sliderView.getSenderType()) {
						case SliderView.SENDER_MELODY:
							if (value != status.getCurrentMelody()) {
								sliderView.setVolumn(status.getCurrentMelody());
							}
							break;
						case SliderView.SENDER_TEMPO:
							if (value != status.getCurrentTempo()) {
								sliderView.setVolumn(status.getCurrentTempo());
							}
							break;
						case SliderView.SENDER_KEY:
							if (value != status.getCurrentKey()) {
								sliderView.setVolumn(status.getCurrentKey());
							}
							break;
						case SliderView.SENDER_VOLUMN:
							if (value != status.getCurrentVolume()) {
								sliderView.setVolumn(status.getCurrentVolume());
							}
							break;
						}
					}
				}
				repeatView.invalidate();
				serverStatus = status;
				

				volumnView.invalidate();
				repeatView.invalidate();
				pauseView.invalidate();
				nextView.invalidate();
				danceView.invalidate();
			
			}
		}
	}

	@Override
	public void OnUpdateView() {
		if(isAdded()){
			if(adapterSong != null){
				adapterSong.notifyDataSetChanged();
			}
		}
	}
	
	private Timer timerHideDialogThanhTruot;
	private void hideDialogThanhTruot(){
		if(timerHideDialogThanhTruot != null){
			timerHideDialogThanhTruot.cancel();
			timerHideDialogThanhTruot = null;
		}
		timerHideDialogThanhTruot = new Timer();
		timerHideDialogThanhTruot.schedule(new TimerTask() {
			
			@Override
			public void run() {
				handler.sendEmptyMessage(0);
			}
			
			private Handler handler = new Handler(){
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					if(myDialogSlider != null){
						myDialogSlider.dismiss();
					}
				}
			};
		}, 5000);
		
	}

	private void changeColorScreen(){
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			
		}
	}

	@Override
	public void OnClosePopupYouTube(int position) {
		// TODO Auto-generated method stub
		
	}
}
