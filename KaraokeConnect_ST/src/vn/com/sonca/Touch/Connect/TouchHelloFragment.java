package vn.com.sonca.Touch.Connect;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.CustomView.TouchGroupHello;
import vn.com.sonca.Touch.CustomView.TouchSettingView;
import vn.com.sonca.Touch.CustomView.TouchGroupHello.OnShowKeybroadListener;
import vn.com.sonca.Touch.CustomView.TouchHelloView;
import vn.com.sonca.Touch.CustomView.TouchHelloView.OnTouchHelloViewListener;
import vn.com.sonca.Touch.CustomView.TouchSettingView.OnTouchSettingViewListener;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity.OnMainListener;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity.OnToHelloListener;
import vn.com.sonca.params.SKServer;
import vn.com.sonca.params.ServerStatus;
import vn.com.sonca.zktv.main.KTVMainActivity;
import vn.com.sonca.zzzzz.MyApplication;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Trace;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnWindowFocusChangeListener;
import android.view.WindowManager;
import android.view.View.OnSystemUiVisibilityChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;

public class TouchHelloFragment extends Fragment implements OnToHelloListener {
	
	private String TAB = "TouchHelloFragment";
	private TouchMainActivity mainActivity;
	private KTVMainActivity ktvMainActivity;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (OnTouchHelloFragmentListener)activity;
			if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				ktvMainActivity = (KTVMainActivity) activity;
				ktvMainActivity.isProcessingSomething = true;
			} else {
				mainActivity = (TouchMainActivity) activity;
				mainActivity.isProcessingSomething = true;	
			}
			
		} catch (Exception ex) {
			MyLog.e(TAB, ex);
		}
	}
	
	public FragmentActivity getMyActivity(){
		if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			return this.ktvMainActivity;
		} else {
			return this.mainActivity;
		}
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			ktvMainActivity.isProcessingSomething = false;
		} else {
			mainActivity.isProcessingSomething = false;	
		}
		
	}
	
	private OnTouchHelloFragmentListener listener;
	public interface OnTouchHelloFragmentListener{
		public void OnHideDrawerLayout();
		public void OnShowHideHello(boolean show);
		public void OnChangeHello(String dong1, String dong2);
		public void OnExitApp();
	}

	private String dong1 = ""; 
	private String dong2 = "";
	private EditText textDong1;
	private EditText textDong2;
	private LinearLayout linearSetting;
	private LinearLayout linearHello;
	private TouchHelloView touchHelloView;
	private TouchGroupHello touchGroupHello;
	private View viewBackground;
	private View viewLine;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.touch_fragment_hello, container, false);
		
		boolean isConnect = getArguments().getBoolean("Connect");
		SKServer skServer = ((MyApplication)getMyActivity().getApplication()).getDeviceCurrent();
		String namedevice = "";
		if(skServer != null){
			namedevice = skServer.getName();
		}
		String[] lines = ((MyApplication)getMyActivity().getApplication()).getHello();
		if(lines != null && lines.length >= 2){
			if(lines[0] != null){
				dong1 = lines[0];
			}
			if(lines[1] != null){
				dong2 = lines[1];
			}
		}
		
		viewLine = (View)view.findViewById(R.id.viewLine);
		viewBackground = (View)view.findViewById(R.id.viewBackground);
		linearHello = (LinearLayout)view.findViewById(R.id.linearHello);
		linearSetting = (LinearLayout)view.findViewById(R.id.linearSetting);
		textDong1 = (EditText)view.findViewById(R.id.EditTextDong1);
		textDong2 = (EditText)view.findViewById(R.id.EditTextDong2);
		textDong1.setText(dong1);
		textDong2.setText(dong2);
		
		touchGroupHello = (TouchGroupHello)view.findViewById(R.id.TouchGroupHello);
		touchGroupHello.setOnShowKeybroadListener(new OnShowKeybroadListener() {
			@Override public void ShowKeybroad() {
				if(getMyActivity() != null){
					if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
						ktvMainActivity.flagRunHide = false;
					} else {
						mainActivity.flagRunHide = false;	
					}
					
					int mUIFlag =
							View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
							| View.SYSTEM_UI_FLAG_IMMERSIVE;
					if (getMyActivity().getWindow().getDecorView().getSystemUiVisibility() != mUIFlag) {
						getMyActivity().getWindow().getDecorView().setSystemUiVisibility(mUIFlag);
					}
				}
			}
		});
		
		TouchSettingView touchSettingView = (TouchSettingView)view.findViewById(R.id.TouchSettingView);
		touchSettingView.setHelloActive(isConnect);
		touchSettingView.setOnTouchSettingViewListener(new OnTouchSettingViewListener() {
			@Override public void OnExitApp() {
				if(listener != null){
					listener.OnExitApp();
				}
			}
			@Override public void OnChangePass() {
				
			}
			@Override public void OnChangeHello() {
				linearHello.setVisibility(View.VISIBLE);
				linearSetting.setVisibility(View.INVISIBLE);
				textDong1.setText(dong1);
				textDong2.setText(dong2);
				
			}
			@Override public void OnBackSetting() {
				if(listener != null){
					listener.OnHideDrawerLayout();
				}
			}
		});
		
		touchHelloView = (TouchHelloView)view.findViewById(R.id.TouchHelloView);
		if(namedevice != null){
			touchHelloView.setNameDevice(namedevice);
		}
		touchHelloView.setOnTouchHelloViewListener(new OnTouchHelloViewListener() {
			@Override
			public void OnShowhideListener(boolean show) {
				if (listener != null) {
					listener.OnShowHideHello(show);
				}
			}
			
			@Override
			public void OnChangeListener() {
				String dong1 = textDong1.getText().toString();
				String dong2 = textDong2.getText().toString();
				if(listener != null){
					if(dong1 == null) dong1 = "";
					if(dong2 == null) dong2 = "";
					listener.OnChangeHello(dong1, dong2);
				}
			}
			
			@Override
			public void OnCancelListener() {
				/*				
				linearHello.setVisibility(View.INVISIBLE);
				linearSetting.setVisibility(View.VISIBLE);
				touchGroupHello.clearFocusView();
				 */				
				if(getMyActivity() != null){
					((MyApplication)getMyActivity().getApplication()).hideVirtualKeyboard(textDong1);
					((MyApplication)getMyActivity().getApplication()).hideVirtualKeyboard(textDong2);
					if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
						ktvMainActivity.flagRunHide = true;
					} else {
						mainActivity.flagRunHide = true;	
					}
					
				}
				if(listener != null){
					listener.OnHideDrawerLayout();
				}
			}
			
			@Override
			public void OnBackListener() {
				/*				
				linearHello.setVisibility(View.INVISIBLE);
				linearSetting.setVisibility(View.VISIBLE);
				touchGroupHello.clearFocusView();
				 */				
				if(getMyActivity() != null){
					((MyApplication)getMyActivity().getApplication()).hideVirtualKeyboard(textDong1);
					((MyApplication)getMyActivity().getApplication()).hideVirtualKeyboard(textDong2);
					if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
						ktvMainActivity.flagRunHide = true;
					} else {
						mainActivity.flagRunHide = true;	
					}
					
				}
				if(listener != null){
					listener.OnHideDrawerLayout();
				}
			}
		});
		
		changeColorScreen();
		
		if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			ktvMainActivity.editDong1 = textDong1;
			ktvMainActivity.editDong2 = textDong2;
		} else {
			mainActivity.editDong1 = textDong1;
			mainActivity.editDong2 = textDong2;	
		}
		
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		
	}

	
	@Override
	public void OnUpdateCommad(ServerStatus status) {
		if((status.getUserCaption() == 1) != 
			touchHelloView.getShowHide()){
			touchHelloView.setShowHide(status.getUserCaption() == 1);
		}
		
	}
	
	private void hideActionBar(){
		int apiLevel = android.os.Build.VERSION.SDK_INT;
		if(apiLevel >= 19){
			int mUIFlag = View.GONE 
					| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
					| View.SYSTEM_UI_FLAG_IMMERSIVE;
			if (this.getActivity().getWindow().getDecorView().getSystemUiVisibility() != mUIFlag) {
				this.getActivity().getWindow().getDecorView().setSystemUiVisibility(mUIFlag);
			}
		}else{
			this.getActivity().getWindow().getDecorView().setSystemUiVisibility(View.GONE);
		}
	}

	@Override
	public void OnUpdateView() {
		if(isAdded()){
			changeColorScreen();
		}
	}

	private void changeColorScreen(){
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			viewLine.setBackgroundResource(R.drawable.touch_shape_line_ver);
			viewBackground.setBackgroundColor(Color.parseColor("#004c8c"));
			viewBackground.setAlpha(0.95f);
		} else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			viewLine.setBackgroundResource(R.drawable.zlight_shape_line_ver);
			viewBackground.setBackgroundColor(Color.argb(255, 193, 255, 232));
			viewBackground.setAlpha(1.0f);
		}
		touchGroupHello.requestLayout();
		touchHelloView.invalidate();
	}
	
}
