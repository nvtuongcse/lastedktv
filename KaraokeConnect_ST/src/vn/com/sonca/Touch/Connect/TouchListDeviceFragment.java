package vn.com.sonca.Touch.Connect;

import java.util.ArrayList;

import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.Connect.TouchAdapterDevice.OnDeviceListner;
import vn.com.sonca.Touch.CustomView.TouchListDeviceView;
import vn.com.sonca.Touch.CustomView.TouchListDeviceView.OnListDeviceView;
import vn.com.sonca.Touch.CustomView.TouchOneDeviceView.OnOneDeviceViewListener;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity.OnListDeviceListener;
import vn.com.sonca.params.SKServer;
import vn.com.sonca.utils.AppSettings;
import vn.com.sonca.zzzzz.MyApplication;
import vn.com.hanhphuc.karremote.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TouchListDeviceFragment extends Fragment implements
		OnListDeviceListener {

	private String TAB = "ListDeviceFragment";
//	private TouchMainActivity mainActivity;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
//			mainActivity = (TouchMainActivity) activity;
			listener = (OnListDeviceFragmentListener) activity;
		} catch (Exception ex) {
		}
	}

	private OnListDeviceFragmentListener listener;

	public interface OnListDeviceFragmentListener {
		public void OnShowConnect(SKServer skServer);

		public void OnDisConnect(SKServer skServer);

		public void OnRefrresh();

		public void OnAddDevice();

		public void OnSetupWifi();

		public void OnBackListDevice();
		
		public void OnChangeHello();
		
		public void OnChangeFlagOffUser();
		
		public void OnSelectModel(SKServer skServer);
		
		public void OnBlockCommand();
		
		public void OnShowLanguage();
		
		public void OnSettingSleep();
		
		public void OnShowHi_W(SKServer skServer);
		
		public void OnKM1_SelectList();
		public void OnUSBTOC();
		
		public void OnSettingAll();
	}

	private Context context;
	private ListView listView;
	private TouchAdapterDevice adapter;
	private ArrayList<SKServer> listDevices;
	private LinearLayout layoutItemDevice;
	private LinearLayout layoutProgress;
	private LinearLayout layoutList;
	private RelativeLayout layoutBackground;
	private static TouchListDeviceView listDeviceView;
	private TextView textSearchDevice;
	private View viewBackground;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.touch_fragment_listdevice,
				container, false);
		context = this.getActivity().getApplicationContext();
		boolean isconnect = getArguments().getBoolean("Connect", false);
		
		textSearchDevice = (TextView)view.findViewById(R.id.textSearchDevice);
		viewBackground = (View)view.findViewById(R.id.view_background_listdevice);
		layoutBackground = (RelativeLayout)view.findViewById(R.id.layout_background_listdevice);

		listDeviceView = (TouchListDeviceView) view
				.findViewById(R.id.listDeviceView);
		listDeviceView.setHelloEnable(isconnect);
		listDeviceView.setOnListDeviceViewListener(new OnListDeviceView() {
			@Override
			public void OnSetupWifi() {
				if (listener != null) {
					listener.OnSetupWifi();
				}
			}

			@Override
			public void OnRefresh() {
				if (listener != null) {
					listener.OnRefrresh();
				}
			}

			@Override
			public void OnAddDevice() {
				if (listener != null) {
					listener.OnAddDevice();
				}
			}
			
			@Override
			public void OnBackLayout() {
				if (listener != null) {
					listener.OnBackListDevice();
				}
			}

			@Override
			public void OnChangeHello() {
				if(listener != null){
					listener.OnChangeHello();
				}
			}

			@Override
			public void OnChangeFlagOffUser() {
				if(listener != null){
					listener.OnChangeFlagOffUser();
				}
			}
			
			@Override
			public void OnBlockCommand() {
				if(listener != null){
					listener.OnBlockCommand();
				}
			}

			@Override
			public void OnShowLanguage() {
				if(listener != null){
					listener.OnShowLanguage();
				}
			}

			@Override
			public void OnSettingSleep() {
				if(listener != null){
					listener.OnSettingSleep();
				}
			}

			@Override
			public void OnSelectList() {
				if(listener != null){
					listener.OnKM1_SelectList();
				}	
			}
			
			@Override
			public void OnUSBTOC() {
				if(listener != null){
					listener.OnUSBTOC();
				}	
			}

			@Override
			public void OnSettingAll() {
				if(listener != null){
					listener.OnSettingAll();
				}
			}
			
		});

		
		layoutProgress = (LinearLayout) view.findViewById(R.id.layoutProgress);
		layoutProgress.setVisibility(View.GONE);
		layoutList = (LinearLayout) view.findViewById(R.id.layoutList);
		layoutList.setVisibility(View.VISIBLE);

		ArrayList<SKServer> list = ((MyApplication) getActivity()
				.getApplication()).getListServers();
		
		listView = (ListView) view.findViewById(R.id.ListView);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long arg3) {
				for (int i = 0; i < listDevices.size(); i++) {
					if (position == i) {
						if(saveIdx == i){
							listDevices.get(i).setActive(false);
							saveIdx = -1;
						} else {
							listDevices.get(i).setActive(true);
							saveIdx = i;
						}						
					} else {
						listDevices.get(i).setActive(false);
					}
				}
				adapter.notifyDataSetChanged();
			}
		});
		
		changeColorScreen();
		CreateAdapterDevice(list);
		
		return view;
	}
	
	private int saveIdx = -1;

	@Override
	public void onStart() {
		super.onStart();
		SKServer skServer = ((MyApplication) getActivity().getApplication())
				.getDeviceCurrent();
		if (skServer != null && !skServer.getConnectedIPStr().equals("")
				&& skServer.getState() == SKServer.CONNECTED) {
			((MyApplication) getActivity().getApplication())
					.SaveDevice(skServer);
			MyLog.e(TAB, "DeviceCurrent : " + skServer.getConnectedIPStr());
		}
		ArrayList<SKServer> list = ((MyApplication) getActivity()
				.getApplication()).getListServers();
		CreateAdapterDevice(list);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		boolean isNetwork = checkNetwork();		
		if (isNetwork) {
			layoutProgress.setVisibility(View.VISIBLE);
			layoutList.setVisibility(View.GONE);
			textSearchDevice.setText(R.string.search_daumay);
			textSearchDevice.setVisibility(View.VISIBLE);
		} else {
			textSearchDevice.setText(R.string.touch_list_device_0);
			textSearchDevice.setVisibility(View.VISIBLE);
		}
		if(getActivity() != null)
//		if(((MyApplication)mainActivity.getApplication()).checkSearchDeviceComplete()){
			if(listener != null){
				listener.OnRefrresh();
			}
//		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	// ////////////////////////////////////////////

	private ArrayList<SKServer> createListDeviceView() {
		ArrayList<SKServer> listOut = new ArrayList<SKServer>();

		SKServer sk1 = new SKServer();
		sk1.setName("SK9010");
		sk1.setConnectedIPStr("192.168.10.100");
		sk1.setActive(false);
		listOut.add(sk1);

		SKServer sk2 = new SKServer();
		sk2.setName("SK9011");
		sk2.setConnectedIPStr("192.168.10.101");
		sk2.setActive(false);
		listOut.add(sk2);

		SKServer sk3 = new SKServer();
		sk3.setName("SK9012");
		sk3.setConnectedIPStr("192.168.10.102");
		sk3.setActive(false);
		listOut.add(sk3);

		SKServer sk4 = new SKServer();
		sk4.setName("SK9013");
		sk4.setConnectedIPStr("192.168.10.103");
		sk4.setActive(false);
		listOut.add(sk4);

		return listOut;
	}

	// ////////////////////////////////////////////

	private void CreateAdapterDevice(ArrayList<SKServer> listServers) {
		// listDevices = createListDeviceView();
		listDevices = listServers;
/*	GIAI LAP	
		SKServer sk1 = new SKServer();
		sk1.setName("PHONG 1");
		sk1.setConnectedIPStr("192.168.0.100");
		sk1.setState(SKServer.BROADCASTED);
		sk1.setSave(true);
		listDevices.add(sk1);
		
		SKServer sk2 = new SKServer();
		sk2.setName("PHONG 1");
		sk2.setConnectedIPStr("192.168.0.101");
		sk2.setState(SKServer.BROADCASTED);
		sk2.setSave(false);
		listDevices.add(sk2);
		
		SKServer sk3 = new SKServer();
		sk3.setName("PHONG 1");
		sk3.setConnectedIPStr("192.168.0.102");
		sk3.setState(SKServer.SAVED);
		sk3.setSave(true);
		listDevices.add(sk3);
		
		SKServer sk4 = new SKServer();
		sk4.setName("PHONG 1");
		sk4.setConnectedIPStr("192.168.0.103");
		sk4.setState(SKServer.SAVED);
		sk4.setSave(true);
		listDevices.add(sk4);
*/		
	

		adapter = new TouchAdapterDevice(context,
				R.layout.touch_item_device_list, listDevices);
		adapter.setOnDeviceListner(new OnDeviceListner() {
			@Override
			public void OnConnect(SKServer skServer) {
				if (listener != null) {
					if(listDevices != null && !listDevices.isEmpty()){
						for (int i = 0; i < listDevices.size(); i++) {
							SKServer server = listDevices.get(i);
							if(server != null && server.getState() == SKServer.CONNECTED){
								// server.setState(SKServer.BROADCASTED);
								server.setSave(true);
							}
						}
						adapter.notifyDataSetChanged();
					}
					listener.OnShowConnect(skServer);
				}
			}

			@Override
			public void OnDisConnect(SKServer skServer) {
				if (listener != null) {
					listener.OnDisConnect(skServer);
					adapter.notifyDataSetChanged();
				}
			}

			@Override
			public void OnRemoveDevice(SKServer skServer) {
//				MyLog.e(TAB, " - " + skServer.getConnectedIPStr());
				((MyApplication) getActivity().getApplication())
						.ClearDevice(skServer);
				listDevices.remove(skServer);
				adapter.notifyDataSetChanged();
				
				AppSettings setting = AppSettings.getInstance(getActivity()
						.getApplicationContext());
				if (setting.loadServerIP().equals(skServer.getConnectedIPStr())) {
					setting.saveServerIP("");
				}	
				
				/*
				 * ArrayList<SKServer> list =
				 * ((MyApplication)mainActivity.getApplication
				 * ()).getListServers(); MyLog.e(TAB, "OnRemoveDevice() - " +
				 * list.size()); if (list != null && listView != null) {
				 * CreateAdapterDevice(list); }
				 */
			}
			

			@Override
			public void OnSelectModel(SKServer skServer) {
				if(listener != null){
					listener.OnSelectModel(skServer);
				}
				
			}
			
			@Override
			public void OnShowHi_W(SKServer skServer) {
				if(listener != null){
					listener.OnShowHi_W(skServer);
				}
			}
		});
		listView.setAdapter(adapter);
	}

	// ////////////////////////////////////////////

	@Override
	public void OnShowListDevice() {
		if (isAdded() && getActivity() != null) {
			SKServer skServer = ((MyApplication) getActivity().getApplication())
					.getDeviceCurrent();
			if (skServer != null && !skServer.getConnectedIPStr().equals("")
					&& skServer.getState() == SKServer.CONNECTED) {
				listDeviceView.setHelloEnable(true);
				((MyApplication) getActivity().getApplication())
						.SaveDevice(skServer);
			}else{
				listDeviceView.setHelloEnable(false);
			}
			ArrayList<SKServer> list = ((MyApplication) getActivity()
					.getApplication()).getListServers();
			if (list != null && listView != null) {
				if(list.isEmpty()){
					if(checkNetwork()){
						textSearchDevice.setText(R.string.touch_list_device_1);
					}	else {
						textSearchDevice.setText(R.string.touch_list_device_0);
					}
				}
				CreateAdapterDevice(list);
			}
		}
	}

	@Override
	public void OnDisplayProgressScan(boolean isScanning) {
		if(isAdded() && getActivity() != null){
			boolean isNetwork = checkNetwork();
			if(layoutProgress != null && layoutList != null){
				if (isScanning) {
					textSearchDevice.setVisibility(View.VISIBLE);
					if (isNetwork) {
						layoutProgress.setVisibility(View.VISIBLE);
						layoutList.setVisibility(View.GONE);
						textSearchDevice.setText(R.string.search_daumay);
					} else {
						textSearchDevice.setText(R.string.touch_list_device_0);
					}
				} else {
					if (isNetwork) {
						ArrayList<SKServer> list = ((MyApplication) getActivity()
								.getApplication()).getListServers();
						if(list.isEmpty()){
							textSearchDevice.setVisibility(View.VISIBLE);
							textSearchDevice.setText(R.string.touch_list_device_1);
						}else{
							textSearchDevice.setVisibility(View.GONE);
						}
					} else {
						textSearchDevice.setVisibility(View.VISIBLE);
					}
					layoutProgress.setVisibility(View.GONE);
					layoutList.setVisibility(View.VISIBLE);
				}	
			}	
		}
	}

	@Override
	public void OnUpdateView() {
		if(isAdded()){
			changeColorScreen();
		}
	}
	
	private boolean checkNetwork(){
		if(context == null){
			return false;
		}
		ConnectivityManager cm = (ConnectivityManager)context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		boolean isConnected = activeNetwork != null &&
		                      activeNetwork.isConnectedOrConnecting();
		if(listDeviceView != null){
			listDeviceView.setWifiEnable(isConnected);
		}
		return isConnected;
	}

	private void changeColorScreen(){
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			textSearchDevice.setTextColor(Color.CYAN);
			layoutBackground.setBackgroundColor(Color.parseColor("#004c8c"));
			viewBackground.setBackgroundResource(R.drawable.touch_shape_line_ver);
			layoutBackground.setAlpha(0.95f);
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			textSearchDevice.setTextColor(Color.parseColor("#21BAA9"));
			layoutBackground.setBackgroundColor(Color.argb(255, 193, 255, 232));
			viewBackground.setBackgroundResource(R.drawable.zlight_shape_line_ver);
			layoutBackground.setAlpha(1.0f);
		}
		listDeviceView.invalidate();
		if(adapter != null){
			adapter.notifyDataSetChanged();
		}
	}
	
	public static void refreshListView(){
		if(listDeviceView != null){
			listDeviceView.invalidate();
		}
	}
	
}
