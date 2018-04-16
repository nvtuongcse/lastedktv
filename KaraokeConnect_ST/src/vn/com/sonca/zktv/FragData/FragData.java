package vn.com.sonca.zktv.FragData;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.RemoteControl.RemoteIRCode;
import vn.com.sonca.Touch.Language.LanguageStore;
import vn.com.sonca.database.DBInterface;
import vn.com.sonca.database.DBInstance.SearchMode;
import vn.com.sonca.params.ServerStatus;
import vn.com.sonca.params.Singer;
import vn.com.sonca.params.Song;
import vn.com.sonca.utils.PinyinHelper;
import vn.com.sonca.utils.AppConfig.MEDIA_TYPE;
import vn.com.sonca.zktv.FragData.BaseTaskSong.OnBaseTaskListener;
import vn.com.sonca.zktv.FragData.MyGlowView.OnGroupSongListener;
import vn.com.sonca.zktv.main.FragBase;
import vn.com.sonca.zktv.main.KTVMainActivity;
import vn.com.sonca.zktv.main.OnFragDataListener;
import vn.com.sonca.zzzzz.MyApplication;

public abstract class FragData extends FragBase implements OnGroupSongListener {
	
	private String TAB = "FragData";
	private MyGlowView songView1;
	private MyGlowView songView2;
	private MyGlowView songView3;
	private MyGlowView songView4;
	private MyGlowView songView5;
	private MyGlowView songView6;
	
	private MyTextView textPageTotal;
	private MyTextView textPageCurrent;
	private ButPage butLeft, butRight;
	
	private Context context;

	protected int idSinger;
	protected int intTheLoai = -99;
	
	protected abstract void OnCreateFragment(Bundle saved);
	protected abstract int loadTotalSong(String where);
	protected abstract Cursor loadSongData(String where, int offset, int sum);
	protected abstract String getNameFrag();
	
	private OnFragDataListener listener;
	public void setOnFragDataListener(OnFragDataListener listener){
		this.listener = listener;
	}
	
	private MyApplication application;
	private Activity activity;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		listener = (OnFragDataListener)activity;
		application = (MyApplication)activity.getApplication();
		this.activity = activity;
	}
	
	private boolean flagFollow = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.ktv_fragment_song, container, false);
		songView1 = (MyGlowView)view.findViewById(R.id.song1);
		songView2 = (MyGlowView)view.findViewById(R.id.song2);
		songView3 = (MyGlowView)view.findViewById(R.id.song3);
		songView4 = (MyGlowView)view.findViewById(R.id.song4);
		songView5 = (MyGlowView)view.findViewById(R.id.song5);
		songView6 = (MyGlowView)view.findViewById(R.id.song6);
				
		flagFollow = getNameFrag().equals(KTVMainActivity.FOLLOW);
		
		songView1.setApplication(application, flagFollow);
		songView2.setApplication(application, flagFollow);
		songView3.setApplication(application, flagFollow);
		songView4.setApplication(application, flagFollow);
		songView5.setApplication(application, flagFollow);
		songView6.setApplication(application, flagFollow);

		songView1.setOnGroupSongListener(this);
		songView2.setOnGroupSongListener(this);
		songView3.setOnGroupSongListener(this);
		songView4.setOnGroupSongListener(this);
		songView5.setOnGroupSongListener(this);
		songView6.setOnGroupSongListener(this);
		
		context = getActivity().getApplicationContext();
		
		MyLog.e(TAB, "onCreateView : " + getNameFrag());
		
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
				startFlipPage();
				processYouTubePage();
				MyLog.e(TAB, "butLeft : " + intNumberPage);
			}
		});
		butRight.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View arg0) {
				if(flagBlockFlipPage){
					return;
				}
				intNumberPage++;
				startFlipPage();
				MyLog.e(TAB, "butRight : " + intNumberPage);
			}
		});
		OnCreateFragment(savedInstanceState);
		startLoadTotalSong("");
		
		return view;
	}
	
	private void showListSong(ArrayList<Song> arrayList){
		startAutoHidePopup(500);
		if(context == null){
			return;
		}
		if(arrayList == null){
			songView1.setPosition(1, 0, false);
			songView1.setDataSong(null);
			songView2.setPosition(2, 0, false);
			songView2.setDataSong(null);
			songView3.setPosition(3, 0, false);
			songView3.setDataSong(null);
			songView4.setPosition(4, 0, false);
			songView4.setDataSong(null);
			songView5.setPosition(5, 0, false);
			songView5.setDataSong(null);
			songView6.setPosition(6, 0, false);
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

	@Override
	protected void OnLoadSearch(String textSearch) {
//		MyLog.e(TAB, "OnLoadSearch -- " + textSearch);
//		showListSong(null);
		startLoadTotalSong(textSearch);
	}
	
	private int intNumberPage = 0;
	public String searchData = "";
	private void CreateLoadData(final String search) {
		searchData = search;
		if(intNumberPage >= 0 && (intNumberPage+1) <= intTotalPage){
			startLoadPage(search, intNumberPage);
		}else if(intNumberPage < 0){
			intNumberPage = 0;
		}else if((intNumberPage+1) > intTotalPage){
			intNumberPage = intTotalPage - 1;
		}
		
		processButPage();
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
	
////////////////////////////////////////////////////////
	
	private boolean flagBlockFlipPage = false;
	private Timer timerFlipPage = null;
	private void stopFlipPage(){
		if(timerFlipPage != null){
			timerFlipPage.cancel();
			timerFlipPage = null;
		}
	}
	
	private void startFlipPage(){
		stopFlipPage();
		flagBlockFlipPage = true;
		
		CreateLoadData(searchData);
		
		timerFlipPage = new Timer();
		timerFlipPage.schedule(new TimerTask() {
			
			@Override
			public void run() {
				flagBlockFlipPage = false;
			}
		}, 500);
	}
	
////////////////////////////////////////////////////////	
	private Timer timerLoadTotalSong = null;
	private void stopLoadTotalSong(){
		if(timerLoadTotalSong != null){
			timerLoadTotalSong.cancel();
			timerLoadTotalSong = null;
		}
		
		countPart = 0;
		startYouTubePage = 0;
		startYoutube = 0;
		countYouTube = 0;
		songYouTube = new ArrayList<Song>();
		
	}
	
	private int countPart = 0;
	private int countYouTube = 0;
	private int startYouTubePage = 0;
	
	private Message message;
	
	private void startLoadTotalSong(final String where){
		stopLoadTotalSong();
		
		timerLoadTotalSong = new Timer();
		timerLoadTotalSong.schedule(new TimerTask() {
			
			@Override
			public void run() {
				countPart = 0;
				int count = loadTotalSong(where);
				countPart = count;
				
				startYouTubePage = 0;
				startYoutube = 0;
				countYouTube = 0;
				songYouTube = new ArrayList<Song>();
				
				if(!flagFollow && getNameFrag().equals(KTVMainActivity.SONG) && intTheLoai == -99 && idSinger == 0)
				if (MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
					if(where != null && !where.equals("") && where.length() > 0){
						
						LanguageStore store = new LanguageStore(context);
						ArrayList<String> langIDs = store.getListIDActive();
						int[] intIDs = new int[langIDs.size()];
						for (int j = 0; j < langIDs.size(); j++) {
							intIDs[j] = Integer.parseInt(langIDs.get(j));
						}
						
						Cursor cursor = DBInterface.DBGetSongCursor_YouTube(context,
								intIDs, where, SearchMode.MODE_MIXED, MEDIA_TYPE.ALL, 0, 0);
						if (cursor != null) {
							if (cursor.moveToFirst()) {
								for (int i = 0; i < 99999999; i++) {
									if (cancel()) {
										cursor.close();
										cursor = null;
										songYouTube = new ArrayList<Song>();
										break;
									}
									Song song = new Song();
									parseSongInfoQueryResult_YouTube(song, cursor, where);
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
						
						if(songYouTube.size() > 0){
							ArrayList<Song> songExisted = DBInterface.DBSearchListSongID(songYouTube, context);
							if(songExisted.size() > 0){
								ArrayList<Song> newList = new ArrayList<Song>();	 
								
								for (Song song : songYouTube) {
									if(songExisted.contains(song)){
//										MyLog.d("remove bot song trong youtube", song.getName() + " ");
									} else {
										newList.add(song);
									}
								}
								
								songYouTube = newList;
							}
							
						}
						
						countYouTube = songYouTube.size();
						count += songYouTube.size();	
					
					}
					
				}
				
				MyLog.e(TAB, "countFull = " + count);
				MyLog.d(TAB, "countPart = " + countPart);
				MyLog.d(TAB, "countYoutube = " + countYouTube);
				
				message = new Message();
				Bundle data = new Bundle();
				data.putInt("Total", count);
				data.putString("Search", where);
				message.setData(data);
				
				handlerLoadTotalSong.sendMessage(message);
			}
		}, 500);
	}
	
	private int intTotalPage;
	private Handler handlerLoadTotalSong = new Handler(){
		public void handleMessage(Message msg) {
			int total = msg.getData().getInt("Total");
			String search = msg.getData().getString("Search");
			MyLog.d(TAB, "REAL SEARCH -- " + search);
			int i = (total%6 != 0) ? 1 : 0;
			intTotalPage = total/6 + i;
			textPageTotal.setDataNumber(context.getResources().getString(R.string.ktv_search_1), " " + intTotalPage + " trang");
			if(intTotalPage == 0){
				textPageCurrent.setDataNumber("Trang:", " " + 0);
				showListSong(null);
			}else{
				textPageCurrent.setDataNumber("Trang:", " " + 1);
			}
			intNumberPage = 0;
			startLoadPage(search, intNumberPage);
			
			processButPage();
		};
	};
	
/////////////////////////////////////////////////////////////////

	private Timer timerLoadPage = null;
	private void stopLoadPage(){
		if(timerLoadPage != null){
			timerLoadPage.cancel();
			timerLoadPage = null;
		}
	}
	
	private ArrayList<Song> arrayListSong;
	private void startLoadPage(String where, final int numberPage){
		stopLoadPage();
		timerLoadPage = new Timer();
		arrayListSong = new ArrayList<Song>();
		LoadData loadData = new LoadData(where, numberPage, arrayListSong);
		loadData.setOnBaseTaskListener(new OnBaseTaskListener() {
			@Override public void OnLoadFinish(int status) {
				if(status == BaseTaskSong.SUCCESS){
					if(arrayListSong != null){
						showListSong(arrayListSong);
						if(intTotalPage == 0){
							textPageCurrent.setDataNumber("Trang:", " " + 0);
							showListSong(null);
						}else{
							textPageCurrent.setDataNumber("Trang:", " " + (numberPage+1));
						}
					}
				}
			}
		});
		timerLoadPage.schedule(loadData, 100);
	}
	
	private ArrayList<Song> songYouTube = new ArrayList<Song>();
	private int startYoutube = 0;
	
	private class LoadData extends BaseTaskSong {
		private String where = "";
		private int intPageNumber = 0;
		private final int SUM = 6;
		
		public LoadData(String where, int intPageNumber, ArrayList<Song> listSongs) {
			super(listSongs);
			this.intPageNumber = intNumberPage;
			this.where = where;
		}
		
		private int getNumberPage(){
			return intNumberPage;
		}
		
		@Override protected int runTheard() {
			
			Cursor cursor = loadSongData(where, intPageNumber*SUM, SUM);
			if (cancel()) {
				cursor.close();
				cursor = null;
				return ERROR;
			}
			if (cursor != null) {
//				MyLog.d(TAB, "TEST break 1");
				if (moveToFirstCursor(cursor)) {
//					MyLog.d(TAB, "TEST break 2");
					ArrayList<Song> list = new ArrayList<Song>();
					for (int i = 0; i < SUM; i++) {
						if (cancel()) {
							cursor.close();
							cursor = null;
							return ERROR;
						}
						Song song = new Song();
						parseSongInfoQueryResult(where, song, cursor);
						list.add(song);
						if (!cursor.moveToNext()) {
							break;
						}
					}
					cursor.close();
					cursor = null;
					
					if(startYoutube == 0 && songYouTube.size() > 0 && countYouTube > 0 && list.size() < 6){
//						MyLog.d(TAB, "TEST break 3 -- countYouTube = " + countYouTube);
//						MyLog.i(TAB, "TEST break 3 -- list.size() = " + list.size());
						int remainItem = 6 - list.size();
						if(countYouTube > remainItem){
							startYoutube = remainItem;	
						} else {
							startYoutube = countYouTube;
						}
						startYouTubePage = intPageNumber;
//						MyLog.i(TAB, "TEST break 3 -- startYoutube = " + startYoutube);
						ArrayList<Song> listYouTube = new ArrayList(songYouTube.subList(0, startYoutube));
						list.addAll(listYouTube);
					}
					setListSong(list);
					return SUCCESS;
				} else {
					cursor.close();
					cursor = null;
//					MyLog.d(TAB, "TEST break 4 -- startYoutube = " + startYoutube);
					
					if(startYoutube == 0 && songYouTube.size() > 0 && countYouTube > 0){
						if(countYouTube > 6){
							startYoutube = 6;
						} else {
							startYoutube = countYouTube;
						}
						startYouTubePage = intPageNumber;
						ArrayList<Song> list = new ArrayList(songYouTube.subList(0, startYoutube));
						setListSong(list);
						return SUCCESS;
					}
					
					if(startYoutube > 0 && songYouTube.size() > 0 && countYouTube > 0){
//						MyLog.d(TAB, "TEST break 5 -- songYouTube.size() = " + songYouTube.size());
						int remainItem = songYouTube.size() - startYoutube;
						int oldStart = startYoutube;
						if(remainItem > 6){
							startYoutube += 6;
						} else {
							startYoutube += remainItem;
						}
//						MyLog.i(TAB, "TEST break 5 -- startYoutube = " + startYoutube);
						ArrayList<Song> list = new ArrayList(songYouTube.subList(oldStart, startYoutube));
						setListSong(list);
						return SUCCESS;
					}
					
					return NOPAGE;
				}
			}else{
				return NOPAGE;
			}
		}
		
	}
	
	private void processYouTubePage(){
		if(startYoutube > 0 && songYouTube.size() > 0 && countYouTube > 0 && startYouTubePage > 0){
			startYoutube -= (intNumberPage - startYouTubePage + 1) * 6; // - bot 1 page
			if(startYoutube <= 6){
				startYoutube = 0;
			}
//			MyLog.e("processYouTubePage", "startYoutube = " + startYoutube);
		}
	}
	

	private void clearGroupList(int pos){
		if(arrayListSong == null){
			return;
		}
		if(pos == 1 && songView1 != null){
			songView1.clearLayoutView();
		}
		if(pos == 2 && songView2 != null){
			songView2.clearLayoutView();
		}
		if(pos == 3 && songView3 != null){
			songView3.clearLayoutView();
		}
		if(pos == 4 && songView4 != null){
			songView4.clearLayoutView();
		}
		if(pos == 5 && songView5 != null){
			songView5.clearLayoutView();
		}
		if(pos == 6 && songView6 != null){
			songView6.clearLayoutView();
		}
	}
	
	private void clearAllGroupList(){
		if(arrayListSong == null){
			return;
		}
		if(songView1 != null && arrayListSong != null && arrayListSong.size() >= 1){
			songView1.clearLayoutView();
		}
		if(songView2 != null && arrayListSong != null && arrayListSong.size() >= 2){
			songView2.clearLayoutView();
		}
		if(songView3 != null && arrayListSong != null && arrayListSong.size() >= 3){
			songView3.clearLayoutView();
		}
		if(songView4 != null && arrayListSong != null && arrayListSong.size() >= 4){
			songView4.clearLayoutView();
		}
		if(songView5 != null && arrayListSong != null && arrayListSong.size() >= 5){
			songView5.clearLayoutView();
		}
		if(songView6 != null && arrayListSong != null && arrayListSong.size() >= 6){
			songView6.clearLayoutView();
		}
		
	}
	
	
	@Override
	public void OnFirstClick(Song song, int position, int x, int y) {
		if(song == null){
			return;
		}
		if(listener != null){
			listener.OnFirstClick(song, getNameFrag(), position, x, y);
		}
		
		startAutoHidePopup(500);
		MyLog.i(TAB, "OnFirstClick : " + song.getName());
	}
	
	@Override
	public void OnSingerLink(Song song) {
		if(song == null){
			return;
		}
		if(listener != null){
			listener.OnSingerLink(song, getNameFrag());
		}
		MyLog.i(TAB, "OnSingerLink : " + song.getName());
	}
	
	@Override
	public void OnSongLick(Song song, int x, int y) {
		if(song == null){
			return;
		}
		if(listener != null){
			listener.OnSongLick(song, getNameFrag(), x, y);
		}
		MyLog.i(TAB, "OnSongLick : " + song.getName());
	}
	
	@Override
	public void OnPlayYouTube(Song song) {
		if(song == null){
			return;
		}
		if(listener != null){
			listener.OnPlayYouTube(song, getNameFrag());
		}
		startAutoHidePopup(500);
	}
	
	@Override
	public void OnDownYouTube(Song song) {
		if(song == null){
			return;
		}
		if(listener != null){
			listener.OnDownYouTube(song, getNameFrag());
		}
		startAutoHidePopup(500);
	}
	
	private int curPos = 0;
	
	@Override
	public void OnClearLayout(final int pos) {
		
		activity.runOnUiThread(new Runnable() {
			public void run() {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						if(pos == 1 && songView1 != null && arrayListSong != null && arrayListSong.size() >= 1){
							songView1.setBoolLongClick(false);
						}						
						if(pos == 2 && songView2 != null && arrayListSong != null && arrayListSong.size() >= 2){
							songView2.setBoolLongClick(false);
						}
						if(pos == 3 && songView3 != null && arrayListSong != null && arrayListSong.size() >= 3){
							songView3.setBoolLongClick(false);
						}
						if(pos == 4 && songView4 != null && arrayListSong != null && arrayListSong.size() >= 4){
							songView4.setBoolLongClick(false);
						}
						if(pos == 5 && songView5 != null && arrayListSong != null && arrayListSong.size() >= 5){
							songView5.setBoolLongClick(false);
						}
						if(pos == 6 && songView6 != null && arrayListSong != null && arrayListSong.size() >= 6){
							songView6.setBoolLongClick(false);
						}
						
					}
				}, 1000);
			}
		});
		
		startAutoHidePopup(5000);
		
	}
	
	@Override
	public void OnLayoutFrag(ServerStatus status) {
				
	}
	
	@Override
	public void OnSK90009() {
		if(isAdded()){
			if(arrayListSong != null){
				showListSong(arrayListSong);	
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
	
	///---------------------------------------
	private void parseSongInfoQueryResult_YouTube(Song s, Cursor cursorResult, String where) {
		s.setId(cursorResult.getInt(0));
		s.setIndex5(cursorResult.getInt(1));
		String name = cursorResult.getString(2);
		s.setName(name);
		// s.setShortName(cursorResult.getString(3));
		String nameraw = cursorResult.getString(3);
		s.setLyric(cutText(40, cursorResult.getString(4)));
		s.setMediaType(MEDIA_TYPE.values()[cursorResult.getInt(5)]);
		s.setRemix(cursorResult.getInt(6) == 1);
		s.setFavourite(cursorResult.getInt(7) == 1);
		s.setTypeABC(cursorResult.getInt(8));
		s.setExtraInfo(cursorResult.getInt(9));
		// -----------------//
		Singer singer = new Singer("-", "-");
		s.setSinger(singer);
		int[] idxInt = new int[1];
		idxInt[0] = 0;
		s.setSingerId(idxInt);
		// -----------------//
		s.setPlayLink(cursorResult.getString(10));
		s.setDownLink(cursorResult.getString(11));
		s.setSingerName(cursorResult.getString(12));
		s.setTheloaiName(cursorResult.getString(13));
		//-----------------//		
		String textID = "";
		boolean boolRed = s.getId() == s.getIndex5();
		if (MyApplication.intWifiRemote == MyApplication.SONCA_KARTROL
				&& MyApplication.intRemoteModel != 0) {
			textID = s.getIndex5() + "";
		} else {
			if (boolRed) {
				textID = convertIdSong(s.getId()) + " | ";
			} else {
				textID = convertIdSong(s.getId()) + " | " + s.getIndex5();
			}
		}
		SpannableString wordtoSpan = new SpannableString(textID);
		s.setSpannableNumber(wordtoSpan);

		int colorHighlight = Color.GREEN;
		
		if (where.equals("")) {
			s.setSpannable(createSpannable(name, nameraw, where, colorHighlight));
			s.setSpannableGreen(createSpannable(name, nameraw, where, 
					Color.argb(255, 250, 145, 0)));
		} else {
			CharSequence wh1 = name.subSequence(0, 1);
			CharSequence wh2 = name.subSequence(name.length() - 1, name.length());
			if (PinyinHelper.checkChinese(wh1.toString()) || 
				PinyinHelper.checkChinese(wh2.toString())) {
				if(name.contains(where)){
					s.setSpannable(createSpannable(name, 
							PinyinHelper.replaceAll(name), where, colorHighlight));
					s.setSpannableGreen(createSpannable(name, 
							PinyinHelper.replaceAll(name), where, 
							Color.argb(255, 250, 145, 0)));
				}else{
					s.setSpannable(createSpannableChinese(name, where, colorHighlight));
					s.setSpannableGreen(createSpannableChinese(name, where, 
							Color.argb(255, 250, 145, 0)));
				}
			} else {
				if(nameraw.equals("")){
					nameraw = PinyinHelper.replaceAll(name);
				}
				s.setSpannable(createSpannable(name, nameraw, where, colorHighlight));
				s.setSpannableGreen(createSpannable(name, nameraw, where , 
						Color.argb(255, 250, 145, 0)));
			}
		}
	}
	
	private String cutText(int maxLength, String content) {
		if(content == null){
			return "";
		}
		if(content.length() <= maxLength){
			return content;
		}		
		return content.substring(0, maxLength) + "...";
	}
		
	protected String convertIdSong(int id){
		String stringId = String.valueOf(id);
		switch (stringId.length()) {
		case 1: 	return "00000" + stringId;
		case 2: 	return "0000" + stringId;
		case 3: 	return "000" + stringId;
		case 4: 	return "00" + stringId;
		case 5: 	return "0" + stringId;
		default: 	break;
		}
		return stringId;
	}
	
	private Spannable createSpannableChinese(String name , String where, int color){
		Spannable wordtoSpan = new SpannableString(name);
		int countIdx = 0;
		for (int i = 0; i < name.length(); i++) {
			if (countIdx >= where.length()) {
				break;
			}
			String strChar = name.substring(i, i + 1);
			String strSearchChar = where.substring(countIdx, countIdx + 1);
			if (strChar.equals(strSearchChar)) {
				wordtoSpan.setSpan(new ForegroundColorSpan(color), 
						i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				countIdx++;
			} else {
				if (PinyinHelper.checkChinese(strChar)) {
					wordtoSpan.setSpan(new ForegroundColorSpan(color), 
							i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					countIdx++;
				} else {
					continue;
				}
			}
		}
		return wordtoSpan;
	}
	
	private Spannable createSpannable(String textSong , String nameraw , String textSearch, int color){
		textSearch.trim();
		ArrayList<Integer> listOffset = new ArrayList<Integer>();
		Spannable wordtoSpan;
		if(textSong.equals("")){
			wordtoSpan = new SpannableString("");
		}else{
			if(textSearch.equals("")){
				return new SpannableString(textSong);
			}
			textSearch.trim();
			String newString = textSong.replaceAll("[ &+=_,-/]", "*");
			StringBuffer buffer = new StringBuffer(newString);
				//-------------//
			String[] strings = buffer.toString().split("[*]");
			if(strings.length < textSearch.length()){
				int offset = nameraw.indexOf(textSearch);
				wordtoSpan = new SpannableString(textSong);
				if(offset != -1){
					wordtoSpan.setSpan(new ForegroundColorSpan(color), offset, 
							offset + textSearch.length(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
				return wordtoSpan;
			}
			int count = 0;
 			for (int i = 0; i < strings.length; i++) {
 				int size = strings[i].length();
 				if(size <= 0){
 					count += 1;
 				}else{
 					listOffset.add(count);
 					count += size + 1;
 				}
 				if(listOffset.size() >= textSearch.length()){
 					break;
 				}
			}
 				//-------------//
			wordtoSpan = new SpannableString(textSong);
			for (int i = 0; i < listOffset.size(); i++) {
				int offset = getIndex(textSong, listOffset.get(i));
				if(nameraw.charAt(offset) != textSearch.charAt(i)){
					int of = nameraw.indexOf(textSearch);
					SpannableString word = new SpannableString(textSong);
					if(of != -1){
						word.setSpan(new ForegroundColorSpan(color), of, 
								of + textSearch.length(),
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
					return word;
				}else{
					wordtoSpan.setSpan(new ForegroundColorSpan(color), offset, offset + 1,
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
		}	
		return wordtoSpan;
	}
	
	private int getIndex(String string , int index){
		switch (string.charAt(index)) {
		case '(': 	return index + 1; 
		case '`': 	return index + 1; 
		case '[': 	return index + 1; 
		default:	return index;
		}
		
	}
	
}
