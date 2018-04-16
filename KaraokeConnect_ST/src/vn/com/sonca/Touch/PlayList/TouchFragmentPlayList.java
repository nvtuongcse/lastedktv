package vn.com.sonca.Touch.PlayList;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.LinearLayout;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.Lyric.LoadLyric;
import vn.com.sonca.Lyric.LoadLyric.OnLoadLyricListener;
import vn.com.sonca.Lyric.LoadLyricNew;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.CustomView.TouchListView;
import vn.com.sonca.Touch.Listener.TouchIAdapter;
import vn.com.sonca.Touch.Listener.TouchIBaseFragment;
import vn.com.sonca.Touch.touchcontrol.TouchFragmentBase;
import vn.com.sonca.Touch.touchcontrol.TouchItemBack;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity.OnMainListener;
import vn.com.sonca.database.DBInterface;
import vn.com.sonca.params.ServerStatus;
import vn.com.sonca.params.Song;
import vn.com.sonca.utils.Lock;
import vn.com.sonca.zzzzz.MyApplication;

public class TouchFragmentPlayList extends TouchFragmentBase implements OnMainListener{
	
	private String TAB = "FragmentPlayList";
	private TouchMainActivity mainActivity;
	private TouchIBaseFragment listener;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mainActivity = (TouchMainActivity) activity;
			listener = (TouchIBaseFragment) activity;
		} catch (Exception ex) {}
	}
	
	private boolean booltest = false;
	private Context context;
	private TouchListView listView;
	private LinearLayout layoutShowThongBao;
	private TouchAdapterPlayList adapterSong;
	
	private TextView tvThongBao1;
	private TextView tvThongBao2;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.touch_fragment_playlist, container, false);
		layoutShowThongBao = (LinearLayout)view.findViewById(R.id.layoutShowThongBao);
		context = getActivity().getApplicationContext();
		if(listener != null){
			listener.OnNameSearch("", "");
		}
		listView = (TouchListView)view.findViewById(R.id.listView);
		
		tvThongBao1 = (TextView)view.findViewById(R.id.tvThongBao1);
		tvThongBao2 = (TextView)view.findViewById(R.id.tvThongBao2);
		
		listSongs = new ArrayList<Song>();
		loadPlayList = new LoadPlayList(listSongs , true , false);
		loadPlayList.start();
		
		isEnableFirst = true;
		listView.setDropListener(onDrop);
		listView.setRemoveListener(onRemove);

		adapterSong = new TouchAdapterPlayList(context, R.layout.touch_item_song_list, listSongs, mainActivity);
		adapterSong.setOnAdapterListener(new TouchIAdapter() {
			@Override public void OnItemActive(Song song, String id , float x , float y) {
				if (listener != null) {
//					MyLog.i("Fragment", "idSong : " + id);
					listener.onClickItem(song , id, TouchMainActivity.PLAYLIST , "", -1, 0 , 0);
				}
			}
			
			@Override public void OnItemFavourite(int position, Song song) {
				if(listSongs != null){
					boolean bool = song.isFavourite();
					int index = listSongs.indexOf(song);
					if(index != -1){
						listSongs.get(index).setFavourite(bool);
					}
				}
				if(adapterSong != null){
					adapterSong.notifyDataSetChanged();
				}
			}
			
			@Override public void OnFirstClick(Song song, int position, float x, float y) {
				if(MyApplication.bOffFirst == true){
					if(listener != null && isEnableFirst){
						listener.onFirstClick(song , TouchMainActivity.PLAYLIST , position, x , y);
						if(position != 0){
							isEnableFirst = false;
							WaitEnableFirst(5000);
						}
					}
				}
			}
			@Override
			public void onPlaySong(Song song, int position, float x, float y) {
				if(listener != null){
					listener.onPlaySong(song , TouchMainActivity.PLAYLIST , position, x , y);
				}
			}
			@Override public void onDeleteSong(Song song , int position) {
				if(listener != null){
					listener.onDeleteSong(song, position);
				}
			}
			@Override
			public void OnSingerLink(boolean bool, String name, int[] idSinger) {
				if(listener != null && !name.equals("-")){
					((MyApplication)context).addListBack(
							new TouchItemBack(TouchMainActivity.PLAYLIST, "", -1));
					listener.OnSingerLink(bool, name, idSinger);
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
				if(listSongs != null){
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
			}

			@Override
			public void onPlayYouTube(Song song) {
				// TODO Auto-generated method stub
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
		listView.setAdapter(adapterSong);
		
		changeColorScreen();
		
		return view;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		cancelMove();
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
		if(loadPlayList != null)
		if(loadPlayList.getStateThread()){
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
	}

	private Handler handlerMove = new Handler() {
		public void handleMessage(Message msg) {
			if (listener != null && listSongs != null) {
				//ArrayList<Song> list = UpdatePlayListDevice(listSongs);
				ArrayList<Song> list = new ArrayList<Song>(listSongs);
				listener.onMoveSong(list);
			}
		};
	};
	
/////////////////////////////////////////////////////////////////////////
	
	private ArrayList<Song> UpdatePlayListDevice(ArrayList<Song> listIn){
		ArrayList<Song> playlist = ((MyApplication)mainActivity.getApplication()).getListActive();
		ArrayList<Song> listCompare = new ArrayList<Song>(playlist);
		ArrayList<Song> listInPut = new ArrayList<Song>(listIn);
		ArrayList<Song> output = new ArrayList<Song>();
		for (int i = 0; i < listInPut.size() ; i++) {
			Song song = listInPut.get(i);
			if(listCompare.contains(song)){
				output.add(song);
				listCompare.remove(song);
			}else{
				listInPut.remove(song);
			}
		}
		output.addAll(listCompare);
		return output;
	}
	
	private boolean isEnableFirst = true;
	class LoadPlayList extends Thread {
		private boolean isFavourity;
		private ArrayList<Song> listCurrent;
		private ArrayList<Song> listReturn;
		private boolean isCreate;
		
//		private Lock lock = new Lock();
		public LoadPlayList(ArrayList<Song> listCurrent , boolean isCreate , boolean isFavourity) {
			this.listCurrent = listCurrent;
			this.isFavourity = isFavourity;
			listReturn = new ArrayList<Song>();
			this.isCreate = isCreate;
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
				
				ArrayList<Song> list = ((MyApplication) mainActivity
						.getApplication()).getListActive();
				
				ArrayList<Song> playlist = new ArrayList<Song>(list);
				
				if (playlist.size() - listCurrent.size() >= 3) {
					if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
						listReturn = DBInterface.DBSearchListSongIDPlusYoutube(playlist, context);
						
						// search them trong list
						if(listReturn.size() != list.size()){
							if(TouchMainActivity.listRealYoutube != null && TouchMainActivity.listRealYoutube.size() > 0){
								listReturn = processRealYoutubeList(list, listReturn);
							}
						}
					} else {
						listReturn = DBInterface.DBSearchListSongID(playlist, context);
					}
										
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
							} else {
								if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
									ArrayList<Song> songsYoutube = DBInterface.DBSearchSongID_YouTube(id, abc, context);
									if (!songsYoutube.isEmpty()) {
										listReturn.add(songsYoutube.get(0));
									} else {
										Song mSong = processSongRealYoutube(song.getIndex5());
										if(mSong != null){
											listReturn.add(mSong);	
										}
										
									}
								}								
								
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
				if(listSongs != null){
					if(listSongs.size() > 0){
						layoutShowThongBao.setVisibility(View.INVISIBLE);
					}else{
						layoutShowThongBao.setVisibility(View.VISIBLE);
					}
				}else{
					layoutShowThongBao.setVisibility(View.VISIBLE);
				}
//				if(isFavourity){
//					isEnableFirst = true;
//				}else{
//					WaitEnableFirst(500);
//				}
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
	
///////////////////////////// - LISTENER - //////////////////////////////
	
	private boolean isMoveDone = true;
	private TouchListView.DropListener onDrop = new TouchListView.DropListener() {
		@Override
		public void drop(int from, int to) {
			Song item = listSongs.get(from);
			listSongs.remove(from);
			listSongs.add(to, item);
			adapterSong.notifyDataSetChanged();
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
	

	@Override
	protected void UpdateAdapter() {
		if(isAdded())
		if(adapterSong != null){
			
		}
	}

	@Override
	public void OnSearchMain(int state1, int state2, String search) {
		// TODO Auto-generated method stub
	}

	private LoadPlayList loadPlayList = null;
	@Override
	public void OnSK90009() {
		if(isAdded() && loadPlayList != null){
			if(isMoveDone && loadPlayList.getStateThread()){
				loadPlayList = new LoadPlayList(listSongs , false , false);
				loadPlayList.start();
			}
		}
	}

	@Override
	public void OnLoadSucessful() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnUpdateImage() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnUpdateCommad(ServerStatus status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnUpdateView() {
		if(isAdded()){
			if(adapterSong != null){
				adapterSong.notifyDataSetChanged();
			}
		}
		
		changeColorScreen();
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
	
	private ArrayList<Song> processRealYoutubeList(ArrayList<Song> listActive, ArrayList<Song> resultNormal){
		
		MyLog.e(TAB, "processRealYoutubeList");
		
		ArrayList<Song> resultList = new ArrayList<Song>();
		ArrayList<Song> resultYoutube = TouchMainActivity.listRealYoutube;
		
		for(int i = 0; i < listActive.size(); i++) {
			boolean flagFound = false;
			
			for(int j = 0; j < resultNormal.size(); j++) {
				Song song = resultNormal.get(j); 
				if((song.getId() == listActive.get(i).getIndex5() || song.getIndex5() == listActive.get(i).getIndex5())) {
					resultList.add(song); 
					flagFound = true;
					break;
				}
				
			}
			
			if(flagFound == false){
				for(int j = 0; j < resultYoutube.size(); j++) {
					Song song = resultYoutube.get(j); 					
					if((song.getId() == listActive.get(i).getIndex5() || song.getIndex5() == listActive.get(i).getIndex5())) {
						resultList.add(song); 
						break;
					}
					
				}
				
			}
			
		}
		
		return resultList;
		
	}
	
	private Song processSongRealYoutube(int id){
		MyLog.e(TAB, "processSongRealYoutube -- " + id);
		
		ArrayList<Song> resultYoutube = TouchMainActivity.listRealYoutube;
		
		for(int j = 0; j < resultYoutube.size(); j++) {
			Song song = resultYoutube.get(j); 
			if((song.getId() == id || song.getIndex5() == id)) {
				return song;
			}
			
		}
		
		return null;
		
	}

	@Override
	public void OnClosePopupYouTube(int position) {
		// TODO Auto-generated method stub
		
	}

}
