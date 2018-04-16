package vn.com.sonca.zktv.FragPlaylist;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.database.DBInterface;
import vn.com.sonca.params.ServerStatus;
import vn.com.sonca.params.Singer;
import vn.com.sonca.params.Song;
import vn.com.sonca.zktv.FragData.ButPage;
import vn.com.sonca.zktv.FragData.MyTextView;
import vn.com.sonca.zktv.FragPlaylist.MyGlowPlayList.OnGroupPlaylistListener;
import vn.com.sonca.zktv.main.KTVMainActivity;
import vn.com.sonca.zktv.main.OnFragDataListener;
import vn.com.sonca.zktv.main.KTVMainActivity.OnKTVMainListener;
import vn.com.sonca.zzzzz.MyApplication;

public class FragPlaylist extends Fragment implements OnKTVMainListener, OnGroupPlaylistListener {
	
	private String TAB = "FragPlaylist";
	private MyGlowPlayList songView1;
	private MyGlowPlayList songView2;
	private MyGlowPlayList songView3;
	private MyGlowPlayList songView4;
	private MyGlowPlayList songView5;
	private MyGlowPlayList songView6;
	
	private MyTextView textPageTotal;
	private MyTextView textPageCurrent;
	private ButPage butLeft, butRight;
	
	private Context context;
	private OnFragDataListener listener;
	private MyApplication application;
	private Activity activity;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		listener = (OnFragDataListener)activity;
		application = (MyApplication)activity.getApplication();
		this.activity = activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.ktv_fragment_playlist, container, false);

		songView1 = (MyGlowPlayList)view.findViewById(R.id.song1);
		songView2 = (MyGlowPlayList)view.findViewById(R.id.song2);
		songView3 = (MyGlowPlayList)view.findViewById(R.id.song3);
		songView4 = (MyGlowPlayList)view.findViewById(R.id.song4);
		songView5 = (MyGlowPlayList)view.findViewById(R.id.song5);
		songView6 = (MyGlowPlayList)view.findViewById(R.id.song6);
		
		songView1.setOnGroupPlaylistListener(this);
		songView2.setOnGroupPlaylistListener(this);
		songView3.setOnGroupPlaylistListener(this);
		songView4.setOnGroupPlaylistListener(this);
		songView5.setOnGroupPlaylistListener(this);
		songView6.setOnGroupPlaylistListener(this);
		
		songView1.setApplication(application);
		songView2.setApplication(application);
		songView3.setApplication(application);
		songView4.setApplication(application);
		songView5.setApplication(application);
		songView6.setApplication(application);
		
		context = getActivity().getApplicationContext();
		
		textPageTotal = (MyTextView)view.findViewById(R.id.textPageTotal);
		textPageCurrent = (MyTextView)view.findViewById(R.id.textPageCurrent);
		
		butLeft = (ButPage)view.findViewById(R.id.butLeft);
		butRight = (ButPage)view.findViewById(R.id.butRight);
		
		butLeft.setImageView(context.getResources().getDrawable(R.drawable.ktv_icon_backpage_inactive), false);
		butRight.setImageView(context.getResources().getDrawable(R.drawable.ktv_icon_nextpage), true);
		
		butLeft.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View arg0) {
				if(flagBlockFlipPage){
					return;
				}
				intNumberPage--;
				startFlipPage(true); // true for left page
				startAutoHidePopup(500);
			}
		});
		butRight.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View arg0) {
				if(flagBlockFlipPage){
					return;
				}
				intNumberPage++;
				startFlipPage(false);
				startAutoHidePopup(500);
			}
		});
		
/*		
		listSongs = new ArrayList<Song>();
		for (int i = 0; i < 20; i++) {
			Song song = new Song();
			song.setSpannable(new SpannableString("Bai Hat " + i));
			song.setLyric("Mot Hai Ba Bon Nam Sau !");
			song.setSinger(new Singer("Ca Si : " + i, "Ca Si : " + i));
			listSongs.add(song);
		}
		if(listSongs != null){
			int i = (listSongs.size()%6 != 0) ? 1 : 0;
			intTotalPage = listSongs.size()/6 + i;
		}
		intNumberPage = 0;
		LoadPageList(intNumberPage);
*/		
		
		listSongs = new ArrayList<Song>();
		loadPlayList = new LoadPlayList(listSongs);
		loadPlayList.start();
		
		
		return view;
	}
	
	private boolean flagBlockFlipPage = false;
	private Timer timerFlipPage = null;
	private void stopFlipPage(){
		if(timerFlipPage != null){
			timerFlipPage.cancel();
			timerFlipPage = null;
		}
	}
	
	private void startFlipPage(boolean flagLeft){
		stopFlipPage();
		flagBlockFlipPage = true;

		
		boolean re = LoadPageList(intNumberPage);
		if(re == false){
			if(flagLeft){
				intNumberPage = 0;	
			} else {
				intNumberPage = intTotalPage - 1;
			}			
		}
		
		timerFlipPage = new Timer();
		timerFlipPage.schedule(new TimerTask() {
			
			@Override
			public void run() {
				flagBlockFlipPage = false;
			}
		}, 500);
	}
	
	private void clearGroupList(int pos){
		if(listSongs == null){
			return;
		}
		if(pos != 1 && songView1 != null){
			songView1.clearLayoutView();
		}
		if(pos != 2 && songView2 != null){
			songView2.clearLayoutView();
		}
		if(pos != 3 && songView3 != null){
			songView3.clearLayoutView();
		}
		if(pos != 4 && songView4 != null){
			songView4.clearLayoutView();
		}
		if(pos != 5 && songView5 != null){
			songView5.clearLayoutView();
		}
		if(pos != 6 && songView6 != null){
			songView6.clearLayoutView();
		}
	}
	
	private void clearAllGroupList(){
		if(listSongs == null){
			return;
		}
		if(songView1 != null && listSongs != null && listSongs.size() >= 1){
			songView1.clearLayoutView();
		}
		if(songView2 != null && listSongs != null && listSongs.size() >= 2){
			songView2.clearLayoutView();
		}
		if(songView3 != null && listSongs != null && listSongs.size() >= 3){
			songView3.clearLayoutView();
		}
		if(songView4 != null && listSongs != null && listSongs.size() >= 4){
			songView4.clearLayoutView();
		}
		if(songView5 != null && listSongs != null && listSongs.size() >= 5){
			songView5.clearLayoutView();
		}
		if(songView6 != null && listSongs != null && listSongs.size() >= 6){
			songView6.clearLayoutView();
		}
		
	}
	
	private void showListSong(List<Song> arrayList){
		if(arrayList == null || arrayList.size() == 0){
			songView1.setPosition(1, context.getResources().getColor(R.color.ktv_color_song_1), false);
			songView1.setDataSong(null);
			songView2.setPosition(2, context.getResources().getColor(R.color.ktv_color_song_2), false);
			songView2.setDataSong(null);
			songView3.setPosition(3, context.getResources().getColor(R.color.ktv_color_song_3), false);
			songView3.setDataSong(null);
			songView4.setPosition(4, context.getResources().getColor(R.color.ktv_color_song_4), false);
			songView4.setDataSong(null);
			songView5.setPosition(5, context.getResources().getColor(R.color.ktv_color_song_5), false);
			songView5.setDataSong(null);
			songView6.setPosition(6, context.getResources().getColor(R.color.ktv_color_song_6), false);
			songView6.setDataSong(null);
			return;
		}
		if (songView1 != null) {
			if (arrayList.size() >= 1) {
				Song song = arrayList.get(0);
				songView1.setPosition(1, context.getResources().
						getColor(R.color.ktv_color_song_1), false);
				songView1.setDataSong(song);
			}else{
				songView1.setPosition(1, context.getResources().
						getColor(R.color.ktv_color_song_1), false);
				songView1.setDataSong(null);
			}
		}
		if (songView2 != null) {
			if (arrayList.size() >= 2) {
				Song song = arrayList.get(1);
				songView2.setPosition(2, context.getResources().
						getColor(R.color.ktv_color_song_2), false);
				songView2.setDataSong(song);
			}else{
				songView2.setPosition(2, context.getResources().
						getColor(R.color.ktv_color_song_2), false);
				songView2.setDataSong(null);
			}
		}
		if (songView3 != null) {
			if (arrayList.size() >= 3) {
				Song song = arrayList.get(2);
				songView3.setPosition(3, context.getResources().
						getColor(R.color.ktv_color_song_3), false);
				songView3.setDataSong(song);
			}else{
				songView3.setPosition(3, context.getResources().
						getColor(R.color.ktv_color_song_3), false);
				songView3.setDataSong(null);
			}
		}
		if (songView4 != null) {
			if (arrayList.size() >= 4) {
				Song song = arrayList.get(3);
				songView4.setPosition(4, context.getResources().
						getColor(R.color.ktv_color_song_4), false);
				songView4.setDataSong(song);
			}else{
				songView4.setPosition(4, context.getResources().
						getColor(R.color.ktv_color_song_4), false);
				songView4.setDataSong(null);
			}
		}
		if (songView5 != null) {
			if (arrayList.size() >= 5) {
				Song song = arrayList.get(4);
				songView5.setPosition(5, context.getResources().
						getColor(R.color.ktv_color_song_5), false);
				songView5.setDataSong(song);
			}else{
				songView5.setPosition(5, context.getResources().
						getColor(R.color.ktv_color_song_5), false);
				songView5.setDataSong(null);
			}
		}
		if (songView6 != null) {
			if (arrayList.size() >= 6) {
				Song song = arrayList.get(5);
				songView6.setPosition(6, context.getResources().
						getColor(R.color.ktv_color_song_6), false);
				songView6.setDataSong(song);
			}else{
				songView6.setPosition(6, context.getResources().
						getColor(R.color.ktv_color_song_6), false);
				songView6.setDataSong(null);
			}
		}
	}

	private int intTotalPage = 0;
	private int intNumberPage = 0;
	private ArrayList<Song> listSongs = null;
	private LoadPlayList loadPlayList;
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
				ArrayList<Song> list = application.getListActive();
				MyLog.e(TAB, "list size = " + list.size());
				ArrayList<Song> playlist = new ArrayList<Song>(list);
				if (playlist.size() - listCurrent.size() >= 3) {
					if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
						listReturn = DBInterface.DBSearchListSongIDPlusYoutube(playlist, context);
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
				if(listSongs != null){
					int i = (listSongs.size()%6 != 0) ? 1 : 0;
					intTotalPage = listSongs.size()/6 + i;
				}
				if(intNumberPage > intTotalPage){
					intNumberPage = intTotalPage;
				}
				LoadPageList(intNumberPage);
			};
		};
	}
	
	private boolean LoadPageList(int page){
		try{
			List<Song> list = null;
			
			MyLog.e(TAB, "LoadPageList - size : " + listSongs.size() + " -- page: " + page + " -- intTotalPage: " + intTotalPage);
			if((page+1) == intTotalPage){
				int p = listSongs.size()%6;
				MyLog.e(TAB, "LoadPageList1 : " + page + " : " + (page*6) + " : " + (page*6 + p));
				if(p == 0){
					list = listSongs.subList(page*6, page*6 + 6);
				} else {
					list = listSongs.subList(page*6, page*6 + p);
				}				
			}else if(page < intTotalPage){
				MyLog.e(TAB, "LoadPageList2 : " + page + " : " + (page*6) + " : " + (page*6 + 6));
				list = listSongs.subList(page*6, page*6 + 6);
			}else{
				MyLog.e(TAB, "LoadPageList : SPECIAL");
				if(intTotalPage > 0 && page == intTotalPage){
					page--;
					intNumberPage--;
					list = listSongs.subList(page*6, page*6 + 6);
				} else {
					list = new ArrayList<Song>();
					intTotalPage = 0;
				}
				
			}		
			
			showListSong(list);
			if(intTotalPage == 0){
				textPageCurrent.setDataNumber("Trang:", " " + 0);
			}else{
				textPageCurrent.setDataNumber("Trang:", " " + (intNumberPage+1));
			}
			textPageTotal.setDataNumber(context.getResources().getString(R.string.ktv_search_1), " " + intTotalPage + " trang");

			processButPage();
			
			return true;
		}catch(IndexOutOfBoundsException ex){
			MyLog.e(TAB, "ERROR : IndexOutOfBoundsException");
			return false;
		}
	}
	
	private void processButPage(){
		if(intTotalPage <= 0){
			butLeft.setImageView(context.getResources().getDrawable(R.drawable.ktv_icon_backpage_inactive), false);
			butRight.setImageView(context.getResources().getDrawable(R.drawable.ktv_icon_nextpage_inactive), false);
		} else if (intTotalPage == 1){
			butLeft.setImageView(context.getResources().getDrawable(R.drawable.ktv_icon_backpage_inactive), false);
			butRight.setImageView(context.getResources().getDrawable(R.drawable.ktv_icon_nextpage_inactive), false);
		} else {
			if(intNumberPage <= 0){
				butLeft.setImageView(context.getResources().getDrawable(R.drawable.ktv_icon_backpage_inactive), false);
				butRight.setImageView(context.getResources().getDrawable(R.drawable.ktv_icon_nextpage), true);	
			} else if (intNumberPage == (intTotalPage - 1)){
				butLeft.setImageView(context.getResources().getDrawable(R.drawable.ktv_icon_backpage), true);
				butRight.setImageView(context.getResources().getDrawable(R.drawable.ktv_icon_nextpage_inactive), false);
			} else {
				butLeft.setImageView(context.getResources().getDrawable(R.drawable.ktv_icon_backpage), true);
				butRight.setImageView(context.getResources().getDrawable(R.drawable.ktv_icon_nextpage), true);
			}
		}		
	}
	
	@Override
	public void OnFirstClick(Song song, int position) {
		if(song == null){
			return;
		}
		if(listener != null && isEnableFirst){
			listener.OnFirstClick(song, KTVMainActivity.PLAYLIST, intNumberPage * 6 + position - 1, 0, 0);
			isEnableFirst = false;
			WaitEnableFirst(5000);
		}
		
		startAutoHidePopup(500);
	}
	
	private boolean isEnableFirst = true;
	
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

	@Override
	public void OnSingerLink(Song song) {
		if(song == null){
			return;
		}
		if(listener != null){
			listener.OnSingerLink(song, KTVMainActivity.PLAYLIST);
		}
	}

	@Override
	public void OnSongLick(Song song, int position) {
		if(song == null){
			return;
		}
		if(listener != null && isEnableFirst){
			listener.OnFirstClick(song, KTVMainActivity.PLAYLIST, intNumberPage * 6 + position - 1, 0, 0);
			isEnableFirst = false;
			WaitEnableFirst(5000);
		}
		
		startAutoHidePopup(500);
	}
	
	@Override
	public void OnPlayYouTube(Song song) {
		if(song == null){
			return;
		}
		if(listener != null){
			listener.OnPlayYouTube(song, KTVMainActivity.PLAYLIST);
		}
		startAutoHidePopup(500);
	}
	
	@Override
	public void OnDownYouTube(Song song) {
		if(song == null){
			return;
		}
		if(listener != null){
			listener.OnDownYouTube(song, KTVMainActivity.PLAYLIST);
		}
		startAutoHidePopup(500);
	}

	@Override
	public void OnClearLayout(final int pos) {
		
		activity.runOnUiThread(new Runnable() {
			public void run() {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						if(pos == 1 && songView1 != null && listSongs != null && listSongs.size() >= 1){
							songView1.setBoolLongClick(false);
						}						
						if(pos == 2 && songView2 != null && listSongs != null && listSongs.size() >= 2){
							songView2.setBoolLongClick(false);
						}
						if(pos == 3 && songView3 != null && listSongs != null && listSongs.size() >= 3){
							songView3.setBoolLongClick(false);
						}
						if(pos == 4 && songView4 != null && listSongs != null && listSongs.size() >= 4){
							songView4.setBoolLongClick(false);
						}
						if(pos == 5 && songView5 != null && listSongs != null && listSongs.size() >= 5){
							songView5.setBoolLongClick(false);
						}
						if(pos == 6 && songView6 != null && listSongs != null && listSongs.size() >= 6){
							songView6.setBoolLongClick(false);
						}
						
					}
				}, 1000);
			}
		});
		
		startAutoHidePopup(5000);
	}

	@Override
	public void OnKTVSearch(String search) {
		
		
	}

	@Override
	public void OnDeleteClick(Song song, int position) {
		if(song == null){
			return;
		}
		if(listener != null){
			listener.OnDeleteClick(song, KTVMainActivity.PLAYLIST, intNumberPage * 6 + position - 1);
		}
		
		startAutoHidePopup(500);
	}

	@Override
	public void OnLayoutFrag(ServerStatus status) {
		
	}

	@Override
	public void OnSK90009() {
		if(isAdded()){
			if(loadPlayList != null){
				if(loadPlayList.getStateThread()){
					loadPlayList = new LoadPlayList(listSongs);
					loadPlayList.start();
					WaitEnableFirst(500);
				}
			}	
		}
		
	}
	
	private Timer timerAutoHidePopup = null;
	private void stopAutoHidePopup(){
		if(timerAutoHidePopup != null){
			timerAutoHidePopup.cancel();
			timerAutoHidePopup = null;
		}
	}
	
	private void startAutoHidePopup(long time){
		stopAutoHidePopup();
		timerAutoHidePopup = new Timer();
		timerAutoHidePopup.schedule(new TimerTask() {
			
			@Override
			public void run() {
				handlerAutoHidePopup.sendEmptyMessage(0);
			}
		}, time);
	}
	
	private Handler handlerAutoHidePopup = new Handler(){
		public void handleMessage(Message msg) {
			clearAllGroupList();
		};
	};

}
