package vn.com.sonca.AddDataSong;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.AddDataSong.AdapterAddSong.OnAddSongListener;
import vn.com.sonca.AddDataSong.LoadAddSong.OnLoadAddListener;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.CustomView.TouchStatusTotalView;
import vn.com.sonca.params.SongInfo;

public class FragAddSong extends Fragment implements OnLoadAddListener {
	
	private OnFragAddSongListener listener;
	public interface OnFragAddSongListener{
		public void OnAddClick(SongInfo info);
	}
	
	public void setOnFragAddSongListener(OnFragAddSongListener listener){
		this.listener = listener;
	}
	
	private String TAB = "FragDeleteSong";
	private Context context;
	private ListView listView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.touch_fragment_song, container, false);
		context = this.getActivity().getApplicationContext();
		listView = (ListView)view.findViewById(R.id.listview);
		
		TouchStatusTotalView statusTotalView = (TouchStatusTotalView) view.findViewById(R.id.statusTotalView);
		statusTotalView.setVisibility(View.GONE);
		
		CreateLoadData("");
		return view;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		StopLoadData();
	}
	
	public void loadDatabase(ArrayList<SongInfo> list){
		listArraySongs = list;
		adapterDelSong = new AdapterAddSong(context, R.layout.touch_item_song_list, list, "");
		adapterDelSong.setOnAddSongListener(new OnAddSongListener() {
			
			@Override
			public void OnAddClick(SongInfo info) {
				if(listener != null){
					listener.OnAddClick(info);
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
	
	private LoadAddSong loadData;
	private String searchSave = "";
	private ArrayList<SongInfo> listArraySongs;
	
	public void selectAllSong(){
		if(listArraySongs != null && listArraySongs.size() > 0){
			for (SongInfo songInfo : listArraySongs) {
				songInfo.setActive(true);
			}
			
			adapterDelSong = new AdapterAddSong(context, R.layout.touch_item_song_list, listArraySongs, "");
			adapterDelSong.setOnAddSongListener(new OnAddSongListener() {
				
				@Override
				public void OnAddClick(SongInfo info) {
					if(listener != null){
						listener.OnAddClick(info);
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
		
	public ArrayList<SongInfo> getAllList(){
		return this.listArraySongs;
	}
	
	private AdapterAddSong adapterDelSong;
	private void CreateLoadData(String search) {
		MyLog.e(TAB, "CreateLoadData : " + search);
		listArraySongs = new ArrayList<SongInfo>();
		adapterDelSong = new AdapterAddSong(context, R.layout.touch_item_song_list,
				listArraySongs, search);
		adapterDelSong.setOnAddSongListener(new OnAddSongListener() {
			
			@Override
			public void OnAddClick(SongInfo info) {
				if(listener != null){
					listener.OnAddClick(info);
				}
			}
		});
		
		listView.setFastScrollEnabled(true);
		listView.setOnScrollListener(onScrollListener);
		listView.setAdapter(adapterDelSong);
			//----------//
		StopLoadData();
			//----------//
		loadData = new LoadAddSong(0, context, search, listArraySongs);
		loadData.setOnLoadAddListener(this);
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
	
}

