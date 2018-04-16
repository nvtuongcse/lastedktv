package vn.com.sonca.Touch.Connect;

import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.CustomView.TouchDeviceView;
import vn.com.sonca.Touch.CustomView.TouchDeviceView.OnDeviceViewListener;
import vn.com.sonca.Touch.CustomView.TouchDeviceViewIP;
import vn.com.sonca.Touch.CustomView.TouchDeviceViewIP.OnDeviceViewIPListener;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity.ToDeviveFragment;
import vn.com.sonca.zktv.main.KTVMainActivity;
import vn.com.sonca.zzzzz.MyApplication;
import vn.com.hanhphuc.karremote.R;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TouchDeviceFragment extends Fragment implements ToDeviveFragment {
	
	private String TAB = "DeviceFragment";
	private TouchMainActivity mainActivity;
	private KTVMainActivity ktMainActivity;
	
	private String NameDevice;
	private String IPDevice;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (OnDeviceFragmentListener) activity;
			if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				ktMainActivity = (KTVMainActivity) activity;
			} else {
				mainActivity = (TouchMainActivity) activity;	
			}
		} catch (Exception ex) {}
	}
	
	private OnDeviceFragmentListener listener;
	public interface OnDeviceFragmentListener {
		public void OnConectIpPass(String IP , String Pass, String name);
		public void OnConnect(String IP); 
		public void OnSendPass(String pass);
		public void OnDeviceBackLayout();
	}
	
	private View viewColorLine;
	private View viewBackGround;
	private TouchDeviceView deviceView;
	private TouchDeviceViewIP deviceViewIP;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		IPDevice = getArguments().getString("IP" , "");
		NameDevice = getArguments().getString("Name", "");
		View view = inflater.inflate(R.layout.touch_fragment_device, container, false);
		if (IPDevice.equals("")) {
			view.findViewById(R.id.LayoutPass).setVisibility(View.INVISIBLE);
			view.findViewById(R.id.LayoutIPPass).setVisibility(View.VISIBLE);
		} else {
			view.findViewById(R.id.LayoutPass).setVisibility(View.VISIBLE);
			view.findViewById(R.id.LayoutIPPass).setVisibility(View.INVISIBLE);
		}
		
		viewColorLine = (View)view.findViewById(R.id.viewColorLine);
		viewBackGround = (View)view.findViewById(R.id.color_screen_layout);
		deviceView = (TouchDeviceView)view.findViewById(R.id.deviceView);
		deviceView.setDataDevice(IPDevice, NameDevice);
		deviceView.setOnDeviceViewListener(new OnDeviceViewListener() {
			@Override public void OnSendPass(String pass) {
				if(pass != null && listener != null){
					listener.OnConectIpPass(IPDevice , pass, NameDevice);
				}
			}
			@Override public void OnBackLayout() {
				if(listener != null){
					listener.OnDeviceBackLayout();
				}
			}
		});
		
		deviceViewIP = (TouchDeviceViewIP)view.findViewById(R.id.DeviceViewIP);
		deviceViewIP.setOnDeviceViewIPListener(new OnDeviceViewIPListener() {
			@Override public void OnConectIpPass(String IP, String pass) {
				if(pass != null && IP != null && listener != null){
					listener.OnConectIpPass(IP, pass, NameDevice);
				}
			}
			@Override public void OnBackLayout() {
				if(listener != null){
					listener.OnDeviceBackLayout();
				}
			}
		});
		
		changeColorScreen();
		
		return view;
	}

	@Override
	public void OnConnected() {
		
	}

	@Override
	public void OnUpdateView() {
		if(isAdded()){
			changeColorScreen();
		}
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
		deviceView.invalidate();
		deviceViewIP.invalidate();
	}
}
