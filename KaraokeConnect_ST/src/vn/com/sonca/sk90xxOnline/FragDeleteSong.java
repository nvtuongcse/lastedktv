package vn.com.sonca.sk90xxOnline;

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
import vn.com.sonca.Touch.CustomView.TouchStatusTotalView;
import vn.com.sonca.database.DBInterface;
import vn.com.sonca.database.DbHelper;
import vn.com.sonca.database.DBInstance.SearchMode;
import vn.com.sonca.database.DBInstance.SearchType;
import vn.com.sonca.params.Song;
import vn.com.sonca.params.SongInfo;
import vn.com.sonca.sk90xxOnline.AdapterDelSong.OnDelSongListener;
import vn.com.sonca.sk90xxOnline.LoadDelSong.OnLoadDelListener;
import vn.com.sonca.utils.AppConfig.MEDIA_TYPE;

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
	
	public void sendNotifyUpdateFrag(){
		if(adapterDelSong != null){
			adapterDelSong.notifyDataSetChanged();
		}	
	}
	
	private ArrayList<Song> listOldCheck = new ArrayList<Song>();
	private int stateFilter = 0;
	public void loadDatabase(String where, ArrayList<Song> listOldCheck, int stateFilter){
		this.listOldCheck = listOldCheck;
		this.stateFilter = stateFilter;
		CreateLoadData(where);
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
			statTimerShowThongBao();
			adapterDelSong.notifyDataSetChanged();
		}
	}
	
	@Override
	public void OnFinish() {
		if(adapterDelSong != null){
			statTimerShowThongBao();
		}
	}

	@Override
	public void OnStartLoad(int idThread) {
		if(adapterDelSong != null){
			statTimerShowThongBao();
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
				if(loadData != null){
					loadData.loadNextPage();
				}
				break;
			}
		}

		@Override
		public void onScroll(AbsListView view, int first, int visible, int total) {
			
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
						if(listener != null){
							listener.OnUpdateTotal(adapterDelSong.getCount());
						}
					}
				}
			};
		}, 2000);
	}
		
}
