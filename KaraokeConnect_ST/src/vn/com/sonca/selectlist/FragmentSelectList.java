package vn.com.sonca.selectlist;

import java.util.Timer;
import java.util.TimerTask;

import android.R.id;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.CustomView.LanguageButtonModel;
import vn.com.sonca.Touch.touchcontrol.TouchFragmentBase;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity.OnMainListener;
import vn.com.sonca.params.ServerStatus;
import vn.com.sonca.selectlist.TopSelectListView.OnTopSelectListViewListener;
import vn.com.sonca.zzzzz.MyApplication;

public class FragmentSelectList  extends TouchFragmentBase implements OnMainListener {
	private final String TAB = "FragmentSelectList";
	
	private ListView listView;
	private TouchMainActivity mainActivity;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listenerSelectList = (OnSelectListFragmentListener) activity;			
		} catch (Exception ex) {}
	}
	
	private OnSelectListFragmentListener listenerSelectList;
	public interface OnSelectListFragmentListener {
		public void OnSelectListFragmentBackList();
		public void OnClickSelectItem();
		public void OnCloseSelectList();
	}
	
	private Context context;
	private ItemSelectListView itemSonca;
	private ItemSelectListView itemUser;
	private SharedPreferences sharedselect;
	private View viewBackground;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		context = getActivity().getApplicationContext();
		View view = inflater.inflate(R.layout.fragment_selectlist, container, false);
		sharedselect = context.getSharedPreferences("sharedselect", Context.MODE_PRIVATE);
		MyApplication.intSelectList = sharedselect.getInt("intselect", 1);
		
		viewBackground = (View)view.findViewById(R.id.color_screen_layout);
		
		TopSelectListView topview = (TopSelectListView)view.findViewById(R.id.TopModelView);
		topview.setOnTopSelectListViewListener(new OnTopSelectListViewListener() {
			
			@Override
			public void OnBackLayout() {
				if(listenerSelectList != null){
					listenerSelectList.OnSelectListFragmentBackList();
				}
			}
		});
		
		itemSonca = (ItemSelectListView)view.findViewById(R.id.itemSonca);
		itemSonca.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View arg0) {
//				MyLog.i(TAB, "onClick SONCA");
//				if((MyApplication.intSelectList & 1) == 0){
//					MyApplication.intSelectList |= 1;
//				}else{
//					MyApplication.intSelectList &= 0xfffe;
//				}
//				if(MyApplication.intSelectList == 0){
//					MyApplication.intSelectList = 1;
//				}
//				
//				SharedPreferences.Editor editor = sharedselect.edit();
//				editor.putInt("intselect", MyApplication.intSelectList);
//				editor.commit();
//				
//				itemSonca.setData("SONCA", ((MyApplication.intSelectList & 1) == 1));
//				
//				reloadDataSong();
			}
		});
		itemUser = (ItemSelectListView)view.findViewById(R.id.itemUser);
		itemUser.setOnClickListener(new OnClickListener() {	
			@Override public void onClick(View arg0) {
				MyLog.i(TAB, "onClick USER");
				if((MyApplication.intSelectList & 2) == 0){
					MyApplication.intSelectList |= 2;
				}else{
					MyApplication.intSelectList &= 0xfffd;
				}
				if(MyApplication.intSelectList == 0){
					MyApplication.intSelectList = 2;
				}
				
				SharedPreferences.Editor editor = sharedselect.edit();
				editor.putInt("intselect", MyApplication.intSelectList);
				editor.commit();
				
				itemUser.setData("USER", ((MyApplication.intSelectList & 2) == 2));
				
				reloadDataSong();
			}
		});
		itemSonca.setData("SONCA", ((MyApplication.intSelectList & 1) == 1));
		itemUser.setData("USER", ((MyApplication.intSelectList & 2) == 2));
		
		LanguageButtonModel button = (LanguageButtonModel)view.findViewById(R.id.ButtonModel);
		button.setOnClickListener(new OnClickListener() {	
			@Override public void onClick(View arg0) {
				if(listenerSelectList != null){
					listenerSelectList.OnCloseSelectList();
				}
			}
		});
		
		changeColorScreen();
		
		return view;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		SharedPreferences.Editor editor = sharedselect.edit();
		editor.putInt("intselect", MyApplication.intSelectList);
		editor.commit();
		
		if(timerReload != null){
			timerReload.cancel();
			timerReload = null;
		}
	}
	
	private Timer timerReload;
	private void reloadDataSong(){
		if(timerReload != null){
			timerReload.cancel();
			timerReload = null;
		}
		timerReload = new Timer();
		timerReload.schedule(new TimerTask() {
			@Override public void run() {
				handlerReload.sendEmptyMessage(0);
			}
		}, 700);		
	}
	
	private Handler handlerReload = new Handler(){
		public void handleMessage(Message msg) {
			listenerSelectList.OnClickSelectItem();
		}
	};
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	@Override
	public void OnLoadSucessful() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnUpdateCommad(ServerStatus status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnSearchMain(int state1, int state2, String search) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnSK90009() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnUpdateImage() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnUpdateView() {
		changeColorScreen();
	}

	@Override
	protected void UpdateAdapter() {
		// TODO Auto-generated method stub
		
	}
	
	private void changeColorScreen(){
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			viewBackground.setBackgroundColor(Color.parseColor("#004c8c"));
			viewBackground.setAlpha(0.95f);
		} else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			viewBackground.setBackgroundColor(Color.argb(255, 193, 255, 232));
			viewBackground.setAlpha(1.0f);
		}
	}

	@Override
	public void OnClosePopupYouTube(int position) {
		// TODO Auto-generated method stub
		
	}

}
