package vn.com.sonca.Touch.Musician;

import java.util.ArrayList;

import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.CustomView.TouchSearchView;
import vn.com.sonca.Touch.CustomView.TouchTotalView;
import vn.com.sonca.Touch.CustomView.TouchTotalView.OnTotalViewListener;
import vn.com.sonca.Touch.Listener.TouchIBaseFragment;
import vn.com.sonca.Touch.touchcontrol.TouchFragmentBase;
import vn.com.sonca.Touch.touchcontrol.TouchItemBack;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity.OnMainListener;
import vn.com.sonca.database.DBInstance.SearchMode;
import vn.com.sonca.database.DBInterface;
import vn.com.sonca.params.Musician;
import vn.com.sonca.params.ServerStatus;
import vn.com.sonca.zzzzz.MyApplication;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.utils.AppConfig.LANG_INDEX;
import vn.com.sonca.utils.AppSettings;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;

public class TouchFragmentMusician extends TouchFragmentBase implements
		OnMainListener {
	private String TAB = "FragmentMusician";
	private Context context;
	private TouchIBaseFragment listener;
	private GridView gridView;
	private TouchTotalView totalView;
	private LinearLayout layoutShowThongBao;
	private TouchMainActivity mainActivity;
	
	private TextView tvThongBao1;
	private TextView tvThongBao2;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (TouchIBaseFragment) activity;
			mainActivity = (TouchMainActivity) activity;
		} catch (Exception ex) {
		}
	}

	private TouchMusicianAdapter adapter;
	private ArrayList<Musician> musicianList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.touch_fragment_musician,
				container, false);
		context = getActivity().getApplicationContext();
		if (listener != null) {
			listener.OnNameSearch("", "");
		}
		
		tvThongBao1 = (TextView)view.findViewById(R.id.tvThongBao1);
		tvThongBao2 = (TextView)view.findViewById(R.id.tvThongBao2);
		
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
		Bundle bundle = getArguments();
		search = bundle.getString("data");
		searchState = bundle.getInt("state");

		totalView = (TouchTotalView) view.findViewById(R.id.totalview);
		totalView.setOnTotalViewListener(new OnTotalViewListener() {

			@Override
			public void OnUpdatePic() {
				mainActivity.OnUpdatePicFromSingerTab();
			}
		});
		totalView.setVisibility(View.VISIBLE);
		totalView.setDisplay(true);
		
		gridView = (GridView) view.findViewById(R.id.musician_gridview);
		gridView.setOnScrollListener(onScrollListener);
		
		int indexPosition = bundle.getInt("idxPos", -1);
		if (indexPosition != -1) {
			gridView.setSelection(indexPosition);
		}
		
		gridView.setFastScrollEnabled(true);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (listener != null) {
					((MyApplication) context).addListBack(new TouchItemBack(
							TouchMainActivity.MUSICIAN, search, searchState, position));
					listener.onClickItem(null,
							String.valueOf(musicianList.get(position).getID()),
							TouchMainActivity.MUSICIAN, search, searchState, 0,
							0);
				}
			}
		});
		
		changeColorScreen();
		
		if (searchState == -1) {
			LoadDatabase(search, TouchSearchView.ALL);
		} else {
			LoadDatabase(search, searchState);
		}
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (listener != null) {
			listener.OnNameSearch("", "");
		}
		if (musicianList != null) {
			musicianList.clear();
			musicianList = null;
		}
	}
	
	// /////////////////////////// - LOAD DATA - /////////////////////////////

	private void LoadDatabase(String search, int language) {
		AppSettings setting = AppSettings.getInstance(context);
//		if (setting.loadServerLastUpdate() != 0) {
			// if(setting.isUpdated()) {
			LANG_INDEX index;
			switch (language) {
			case TouchSearchView.ALL:
				index = LANG_INDEX.ALL_LANGUAGE;
				break;
			case TouchSearchView.OTHER:
				index = LANG_INDEX.ALL_EXCEPT_VIETNAMESE;
				break;
			case TouchSearchView.VIETNAM:
				index = LANG_INDEX.VIETNAMESE;
				break;
			default:
				index = LANG_INDEX.ALL_LANGUAGE;
				break;
			}
			musicianList = DBInterface.DBSearchMusician(search, index,
					SearchMode.MODE_MIXED, 0, 0, context);
			adapter = new TouchMusicianAdapter(getActivity(),
					R.layout.touch_musician_gridview_item, search, musicianList);
			gridView.setAdapter(adapter);
			totalView.setData(musicianList.size(), -1, search, searchState);
//		}
	}

	// /////////////////////////// - LISTENER - //////////////////////////////

	private String search;
	private int searchState;

	@Override
	public void OnSearchMain(int state1, int state2, String search) {
		if (isAdded()) {
			if(searchState != state2 || !this.search.equals(search.trim())){
				LoadDatabase(search.trim(), state2);
			}
			this.search = search.trim();
			searchState = state2;
			
			if(musicianList != null){
				if(musicianList.size() > 0){
					layoutShowThongBao.setVisibility(View.INVISIBLE);
				}else{
					layoutShowThongBao.setVisibility(View.VISIBLE);
				}
			}else{
				layoutShowThongBao.setVisibility(View.VISIBLE);
			}
			
		}
	}

	@Override
	public void OnSK90009() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void UpdateAdapter() {
		// TODO Auto-generated method stub
	}

	@Override
	public void OnLoadSucessful() {
		// TODO Auto-generated method stub
	}

	@Override
	public void OnUpdateImage() {
		if(isAdded()){
			if (searchState == -1) {
				LoadDatabase(search, TouchSearchView.ALL);
			} else {
				LoadDatabase(search, searchState);
			}
		}
	}

	@Override
	public void OnUpdateCommad(ServerStatus status) {
		// TODO Auto-generated method stub

	}

	// TODO HMINH
	OnScrollListener onScrollListener = new OnScrollListener() {
		private int mLastFirstVisibleItem = 0;

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			case 1: // SCROLL_STATE_TOUCH_SCROLL
				if(listener != null){
					listener.OnHideKeyBoard();
				}
				break;
			}
		}

		@Override
		public void onScroll(AbsListView view, int first, int visible, int total) {			
			if (musicianList == null || musicianList.size() == 0) {
				if (totalView != null) {
					if(totalView != null){
						totalView.setVisibility(View.VISIBLE);
						totalView.setDisplay(true);
						totalView.setTotalSum(0);
					}
				}
				return;
			}
			try {
				if (mLastFirstVisibleItem < first) {
					if (totalView.getdisplay() == true) {
						totalView.setDisplay(false);
						totalView.setVisibility(View.GONE);
					}
				}
				if (mLastFirstVisibleItem > first) {
					if (totalView.getdisplay() == false) {
						totalView.setVisibility(View.VISIBLE);
						totalView.setDisplay(true);
					}
				}
				mLastFirstVisibleItem = first;
				/*
				 * totalView.setData(musicianList.size(), -1, search,
				 * searchState);
				 */
			} catch (Exception ex) {
				MyLog.e(TAB, ex);
				return;
			}
		}
	};

	@Override
	public void OnUpdateView() {
		if(isAdded()){
			if(adapter != null){
				adapter.notifyDataSetChanged();
			}
			totalView.invalidate();
			
			changeColorScreen();
		}
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