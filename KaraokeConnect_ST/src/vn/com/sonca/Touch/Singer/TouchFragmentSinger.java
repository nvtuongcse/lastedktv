package vn.com.sonca.Touch.Singer;

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
import vn.com.sonca.params.ServerStatus;
import vn.com.sonca.params.Singer;
import vn.com.sonca.zzzzz.MainActivity;
import vn.com.sonca.zzzzz.MyApplication;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.utils.AppSettings;
import vn.com.sonca.utils.AppConfig.LANG_INDEX;
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

public class TouchFragmentSinger extends TouchFragmentBase implements
		OnMainListener {
	private String TAB = "FragmentSinger";

	private ArrayList<Singer> singerList;
	private TouchSingerAdapter adapter;
	private TouchIBaseFragment listener;
	private GridView gridView;
	private Context context;
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.touch_fragment_singer, container,
				false);
		context = this.getActivity().getApplicationContext();
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
		if(bundle != null){
			search = bundle.getString("data");
			stateSearch = bundle.getInt("state");
		}

		gridView = (GridView) view.findViewById(R.id.singer_gridview);
		gridView.setOnScrollListener(onScrollListener);

		tvThongBao1 = (TextView)view.findViewById(R.id.tvThongBao1);
		tvThongBao2 = (TextView)view.findViewById(R.id.tvThongBao2);
		
		totalView = (TouchTotalView) view.findViewById(R.id.totalview);
		totalView.setOnTotalViewListener(new OnTotalViewListener() {

			@Override
			public void OnUpdatePic() {
				mainActivity.OnUpdatePicFromSingerTab();
			}
		});
		totalView.setVisibility(View.VISIBLE);
		totalView.setDisplay(true);
		
		if(bundle != null){
			int indexPosition = bundle.getInt("idxPos", -1);
			if (indexPosition != -1) {
				gridView.setSelection(indexPosition);
			}	
		}		

		gridView.setFastScrollEnabled(true);
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (listener != null) {
					((MyApplication) context).addListBack(new TouchItemBack(
							TouchMainActivity.SINGER, search, stateSearch,
							position));
					listener.onClickItem(null,
							String.valueOf(singerList.get(position).getID()),
							TouchMainActivity.SINGER, search, stateSearch, 0, 0);
				}
			}
		});

		changeColorScreen();
		
		if (stateSearch == -1) {
			LoadDatabase(search, TouchSearchView.ALL);
		} else {
			LoadDatabase(search, stateSearch);
		}

		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (listener != null) {
			listener.OnNameSearch("", "");
		}
		if (singerList != null) {
			singerList.clear();
			singerList = null;
		}
	}
	
	// ////////////////////////////// - LOAD DATABASE -
	// /////////////////////////////////////////////

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
			singerList = DBInterface.DBSearchSinger(search, index,
					SearchMode.MODE_MIXED, 0, 0, getActivity());
			adapter = new TouchSingerAdapter(context,
					R.layout.touch_singer_gridview_item, search, singerList);
			gridView.setAdapter(adapter);
			totalView.setData(singerList.size(), TouchSearchView.SINGER, search,
					stateSearch);
//		}
	}

	// /////////////////////////// - LISTENER - //////////////////////////////

	private String search = "";
	private int stateSearch;

	@Override
	public void OnSearchMain(int state1, int state2, String search) {
		if (isAdded()) {
			if(stateSearch != state2 || !this.search.equals(search.trim())){
				LoadDatabase(search.trim(), state2);
			}
			this.search = search.trim();
			stateSearch = state2;
			if(singerList != null){
				if(singerList.size() > 0){
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
		if (isAdded()) {
			if (stateSearch == -1) {
				LoadDatabase(search, TouchSearchView.ALL);
			} else {
				LoadDatabase(search, stateSearch);
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
			if (singerList == null || singerList.size() == 0) {
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
				 * totalView.setData(singerList.size(), TouchSearchView.SINGER,
				 * search, stateSearch);
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