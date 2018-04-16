package vn.com.sonca.Touch.Connect;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.CustomView.TouchDeviceAdmin;
import vn.com.sonca.Touch.CustomView.TouchDeviceAdmin.OnDeviceAdminListener;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity.ToDeviveFragment;
import vn.com.sonca.zzzzz.MyApplication;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TouchAdminFragment extends Fragment implements ToDeviveFragment {
	
	private String TAB = "AdminFragment";
	private String NameDevice;
	private String IPDevice;
	private int flagLayout;
	
	@Override
	public void onAttach(Activity activity) {
		listener = (OnAdminFragmentListener)activity;
		super.onAttach(activity);
	}
	
	private OnAdminFragmentListener listener;
	public interface OnAdminFragmentListener{
		public void OnSendAdmin(String pass, int flag);
		public void OnBackList(int flag);
	}
	
	private View viewColorLine;
	private View viewBackGround;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		IPDevice = getArguments().getString("IP", "");
		NameDevice = getArguments().getString("Name", "");
		flagLayout = getArguments().getInt("layout", 0);
		View view = inflater.inflate(R.layout.touch_fragment_admin, container, false);
		
		viewBackGround = (View)view.findViewById(R.id.color_screen_layout);
		viewColorLine = (View)view.findViewById(R.id.view_background_listdevice);		
		
		TouchDeviceAdmin deviceAdmin = (TouchDeviceAdmin)view.findViewById(R.id.deviceAdmin);
		deviceAdmin.setDataDevice(IPDevice, NameDevice, flagLayout);
		deviceAdmin.setOnDeviceAdminListener(new OnDeviceAdminListener() {
			
			@Override
			public void OnSendPass(String pass) {
				if(listener != null){
					listener.OnSendAdmin(pass, flagLayout);
				}
			}
			
			@Override
			public void OnBackLayout(int flag) {
				if(listener != null){
					listener.OnBackList(flag);
				}
			}
		});
		
		changeColorScreen();
		
		
		return view;
	}

	@Override
	public void OnConnected() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnUpdateView() {
		changeColorScreen();
	}

	private void changeColorScreen(){
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			viewBackGround.setBackgroundColor(Color.parseColor("#004c8c"));
			viewColorLine.setBackgroundResource(R.drawable.touch_shape_line_ver);
			viewBackGround.setAlpha(0.95f);
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			viewBackGround.setBackgroundColor(Color.argb(255, 193, 255, 232));
			viewColorLine.setBackgroundResource(R.drawable.zlight_shape_line_ver);
			viewBackGround.setAlpha(1.0f);
		}
	}
}
