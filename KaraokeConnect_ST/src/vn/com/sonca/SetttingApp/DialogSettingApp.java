package vn.com.sonca.SetttingApp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewManager;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.SetttingApp.MyTextSettingView;
import vn.com.sonca.SettingAll.PopSettingView;
import vn.com.sonca.SetttingApp.AppInfoView.OnAppInfoViewListener;
import vn.com.sonca.SetttingApp.ButonOnOff.OnOnOffCommandListener;
import vn.com.sonca.SetttingApp.ButtonSettingView.OnButSettingListener;
import vn.com.sonca.SetttingApp.ItemScreenView.OnItemScreenListener;
import vn.com.sonca.SetttingApp.ItemTimerView.OnItemTimerListener;
import vn.com.sonca.SetttingApp.PopSettingView.OnPopSettingListener;
import vn.com.sonca.SetttingApp.TopSettingView.OnBackListener;
import vn.com.sonca.SleepDevice.Sleep;
import vn.com.sonca.Touch.CustomView.DownVideoView;
import vn.com.sonca.Touch.CustomView.DownVideoView.OnDownVideoViewListener;
import vn.com.sonca.Touch.CustomView.XoaVideoView;
import vn.com.sonca.Touch.CustomView.XoaVideoView.OnXoaVideoViewListener;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.utils.AppSettings;
import vn.com.sonca.utils.XmlUtils;
import vn.com.sonca.zktv.main.KTVMainActivity;
import vn.com.sonca.zzzzz.MyApplication;

public class DialogSettingApp implements OnItemTimerListener, 
	OnItemScreenListener, OnButSettingListener {
	
	private String TAB = "DialogSettingApp";
	
	private Window window;
	private Context context;
	private LayoutInflater layoutInflater;	
	private WindowManager.LayoutParams params_relative;
	private TouchMainActivity mainActivity;
	private KTVMainActivity ktvMainActivity;
	public DialogSettingApp(Context context, Window window, TouchMainActivity mainActivity) {
		params_relative = new WindowManager.LayoutParams();
		params_relative.height = WindowManager.LayoutParams.MATCH_PARENT;
		params_relative.width = WindowManager.LayoutParams.MATCH_PARENT;
		params_relative.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		params_relative.type = WindowManager.LayoutParams.TYPE_TOAST;
		params_relative.format = PixelFormat.TRANSLUCENT;
		this.mainActivity = mainActivity;
		this.context = context;
		this.window = window;
	}
	
	public DialogSettingApp(Context context, Window window, KTVMainActivity mainActivity) {
		params_relative = new WindowManager.LayoutParams();
		params_relative.height = WindowManager.LayoutParams.MATCH_PARENT;
		params_relative.width = WindowManager.LayoutParams.MATCH_PARENT;
		params_relative.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		params_relative.type = WindowManager.LayoutParams.TYPE_TOAST;
		params_relative.format = PixelFormat.TRANSLUCENT;
		this.ktvMainActivity = mainActivity;
		this.context = context;
		this.window = window;
	}
	
	private FragmentActivity getMyActivity(){
		if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			return this.ktvMainActivity;
		} else {
			return this.mainActivity;
		}
	}
	
	private OnDialogSettingAppListener listener;
	public interface OnDialogSettingAppListener {
		public void OnFinishListener();
		public void OnChangeColorScreen(int colorScreen);
		public void OnClickSleep();
		
		public void OnInfoUpdateApp();
		public void OnInfoUpdateData();
		public void OnInfoResetDB();
		
		public void OnChangeSwitchState();
		public void OnChangeAdminYouTube();
		public void OnUpdateKhoaYouTube();
	}
	
	public void setOnDialogSettingAppListener(OnDialogSettingAppListener listener){
		this.listener = listener;
	}
	
	private View viewToast = null;
	private Animation animaFaceIn;
	private Animation animaFaceOut;
	private ItemScreenView itemScreen1;
	private ItemScreenView itemScreen2;
	private LinearLayout layoutRight;
	private LinearLayout layoutCenter;
	private TextView textScreenLine1;
	private TopSettingView topSettingView;
	private LinearLayout layoutScreen;
	private MyTextSettingView myTextSettingView2;
	private MyTextSettingView myTextSettingView3;
	private AppInfoView appInfoView;
	private View viewLine1;
	
	private vn.com.sonca.SetttingApp.PopSettingView popSetingView;
	private MyTextSettingView myTextSettingView4;
	private MyTextSettingView myTextSettingViewNew;
	private TextView text_pop_1;
	private View viewSpace1;
	private View viewSpace2;
	
	private DownVideoView downVideoView;
	private XoaVideoView xoaVideoView;
	
	private vn.com.sonca.SettingAll.MyTextSettingView myTextSettingViewA1;
	private vn.com.sonca.SettingAll.PopSettingView popSetingViewA1;
	private TextView text_pop_a1;
	
	private vn.com.sonca.SettingAll.MyTextSettingView myTextSettingViewA2;
	private vn.com.sonca.SettingAll.PopSettingView popSetingViewA2;
	private TextView text_pop_a2;
	
	private vn.com.sonca.SettingAll.MyTextSettingView myTextSettingViewA3;
	private vn.com.sonca.SettingAll.PopSettingView popSetingViewA3;
	private TextView text_pop_a3;
	
	private vn.com.sonca.SettingAll.MyTextSettingView myTextSettingViewA4;
	private vn.com.sonca.SettingAll.PopSettingView popSetingViewA4;
	private TextView text_pop_a4;
	
	private vn.com.sonca.SettingAll.MyTextSettingView myTextSettingViewA5;
	private vn.com.sonca.SettingAll.PopSettingView popSetingViewA5;
	private TextView text_pop_a5;
	
	//-----------------
	private LinearLayout layoutLeft;
	private LinearLayout layouSwitch; 
	private vn.com.sonca.SettingAll.MyTextSettingView myTextSettingView0;
	private vn.com.sonca.SettingAll.MyTextSettingView myTextSettingView1;
	private PopSettingView popSetingView0;
	private TextView text_pop_0;
	private ItemTimerView itemTimer1;
	private ItemTimerView itemTimer2;
	private ItemTimerView itemTimer3;
	private ItemTimerView itemTimer4;
	private ItemTimerView itemTimer5;
	private TextView textTimerLine1;
	
	private ButonOnOff btnCommandYouTube;
	
	private boolean enableClick = false;
	public void showToast(){		
		if (window == null || viewToast != null) {
			return;
		}
		if(viewToast == null){
			layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewToast = layoutInflater.inflate(R.layout.dialog_settingapp, null);
			
			layoutScreen = (LinearLayout)viewToast.findViewById(R.id.layoutScreen);
			layoutScreen.setOnClickListener(new OnClickListener() {
				@Override public void onClick(View arg0) {}
			});
			topSettingView = (TopSettingView)viewToast.findViewById(R.id.TopSettingView);
			
			if(TouchMainActivity.serverStatus != null || KTVMainActivity.serverStatus != null){
				if(((MyApplication)getMyActivity().getApplication()).getDeviceCurrent() != null){
					topSettingView.setNameDevice(((MyApplication)getMyActivity().getApplication()).getDeviceCurrent().getName());
				}				
			} else {
				topSettingView.setNameDevice("");
			}
			
			topSettingView.setOnBackListener(new OnBackListener() {
				@Override public void OnBackListener() {
					hideToastBox();
				}
			});
			layoutCenter = (LinearLayout)viewToast.findViewById(R.id.layoutCenter);
			layoutRight = (LinearLayout)viewToast.findViewById(R.id.layoutRight);

			textScreenLine1 = (TextView)viewToast.findViewById(R.id.text_timer_screen_1);
			myTextSettingView2 = (MyTextSettingView)viewToast.findViewById(R.id.myTextSettingView2);
			myTextSettingView3 = (MyTextSettingView)viewToast.findViewById(R.id.myTextSettingView3);
			myTextSettingView4 = (MyTextSettingView)viewToast.findViewById(R.id.myTextSettingView4);
			myTextSettingViewNew = (MyTextSettingView)viewToast.findViewById(R.id.myTextSettingViewNew);
			
			downVideoView = (DownVideoView)viewToast.findViewById(R.id.downVideoView);
			downVideoView.setOnDownVideoViewListener(new OnDownVideoViewListener() {
				
				@Override
				public void OnDownVideo() {
					MyLog.d(TAB, "OnDownVideo");
					if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
						ktvMainActivity.flagLeftVideoDown = true;
						ktvMainActivity.onUpdateVideoLyric(1);
					} else {
						mainActivity.flagLeftVideoDown = true;
						mainActivity.onUpdateVideoLyric(1);	
					}
					
				}
			});			
			
			xoaVideoView = (XoaVideoView)viewToast.findViewById(R.id.xoaVideoView);			
			if (checkVideoExist()) {
				xoaVideoView.setConnect(true);
			} else {
				xoaVideoView.setConnect(false);
			}
			xoaVideoView.setOnXoaVideoViewListener(new OnXoaVideoViewListener() {
				
				@Override
				public void OnXoaVideo() {
					if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
						ktvMainActivity.xoaVideoView = xoaVideoView;
						ktvMainActivity.showMyPopupAsking(0, null, context.getString(R.string.clip_xoa_1), context.getString(R.string.clip_xoa_2));
					} else {
						mainActivity.xoaVideoView = xoaVideoView;
						mainActivity.showMyPopupAsking(0, null, context.getString(R.string.clip_xoa_1), context.getString(R.string.clip_xoa_2));	
					}
					
				}
			});
			
			if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				ktvMainActivity.xoaVideoView = xoaVideoView;
			} else {
				mainActivity.xoaVideoView = xoaVideoView;	
			}			
			
			viewLine1 = (View)viewToast.findViewById(R.id.viewLine1);
			viewSpace1 = (View)viewToast.findViewById(R.id.viewSpace1);
			viewSpace2 = (View)viewToast.findViewById(R.id.viewSpace2);
			
			itemScreen1 = (ItemScreenView) viewToast.findViewById(R.id.button_screen_1);
			itemScreen2 = (ItemScreenView) viewToast.findViewById(R.id.button_screen_2);
			
			itemScreen1.setOnItemScreenListener(this);
			itemScreen2.setOnItemScreenListener(this);
			
			popSetingView = (vn.com.sonca.SetttingApp.PopSettingView)viewToast.findViewById(R.id.popSettingView);
			
			myTextSettingView2.setTitleView(context.getString(R.string.settingapp_2));
			myTextSettingView3.setTitleView(context.getString(R.string.settingapp_3));
			myTextSettingView4.setTitleView(context.getString(R.string.settingapp_4));
			myTextSettingViewNew.setTitleView(context.getString(R.string.settingapp_5));
			
			popSetingView.setTitleView(context.getString(R.string.setting_pop_1));
			popSetingView.setOnPopSettingListener(new OnPopSettingListener() {
				
				@Override
				public void onSetingPopup(boolean flagOnPopup) {
					MyApplication.flagOnPopup = flagOnPopup;
					AppSettings set = AppSettings.getInstance(context);
					set.savePopupSetting(flagOnPopup);
					
					if(flagOnPopup){
						text_pop_1.setText(context.getString(R.string.setting_pop_2));
					} else {
						text_pop_1.setText(context.getString(R.string.setting_pop_3));
					}
				}
			});
						
			text_pop_1 = (TextView)viewToast.findViewById(R.id.text_pop_1);
			AppSettings set = AppSettings.getInstance(context);
			boolean flagTemp = set.getPopupSetting();
			if(flagTemp){
				text_pop_1.setText(context.getString(R.string.setting_pop_2));
			} else {
				text_pop_1.setText(context.getString(R.string.setting_pop_3));
			}
			popSetingView.setOnPopup(flagTemp);
			
			appInfoView = (AppInfoView)viewToast.findViewById(R.id.appInfoView);
			appInfoView.setOnAppInfoViewListener(new OnAppInfoViewListener() {
				
				@Override
				public void OnWebsite(String strWebsite) {
					
				}
				
				@Override
				public void OnUpdateInfoData() {
					if(listener != null){
						listener.OnInfoUpdateData();
					}			
				}
				
				@Override
				public void OnUpdateApp() {
					if(listener != null){
						listener.OnInfoUpdateApp();
					}	
				}
				
				@Override
				public void OnStore() {

				}
				
				@Override
				public void OnResetPackageDB() {
					if(listener != null){
						listener.OnInfoResetDB();
					}	
				}
				
				@Override
				public void OnHotLine(String strHotLine) {

				}
				
				@Override
				public void OnEmail(String strEmail) {

				}
				
				@Override
				public void OnBack() {
					
				}
			});			
			
			processUpdateApp();
			processGetUpdateData();
			
			itemScreen1.setData(context.getString(R.string.setting_but_screen_1),
					MyApplication.intColorScreen == MyApplication.SCREEN_BLUE);
			itemScreen2.setData(context.getString(R.string.setting_but_screen_2),
					MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI);
			
			// ---------------------------------------------------
			layouSwitch = (LinearLayout)viewToast.findViewById(R.id.layouSwitch);
			layoutLeft = (LinearLayout)viewToast.findViewById(R.id.layoutLeft);
			
			myTextSettingView1 = (vn.com.sonca.SettingAll.MyTextSettingView)viewToast.findViewById(R.id.myTextSettingView1);
			myTextSettingView1.setTitleView(context.getString(R.string.settingall_2a));
			
			popSetingView0 = (PopSettingView)viewToast.findViewById(R.id.popSettingView0);
			popSetingView0.setTitleView(context.getString(R.string.settingall_2a));
			popSetingView0.setOnPopSettingListener(new vn.com.sonca.SettingAll.PopSettingView.OnPopSettingListener() {
				
				@Override
				public void onSetingPopup(boolean flagOnPopup) {
					MyApplication.flagOnWifiVideo = flagOnPopup;
					AppSettings set = AppSettings.getInstance(context);
					set.saveWIFIVideoSetting(flagOnPopup);
					
					if(flagOnPopup){
						text_pop_0.setText(context.getString(R.string.settingall_2b));
						layouSwitch.setVisibility(View.VISIBLE);
					} else {
						text_pop_0.setText(context.getString(R.string.settingall_2c));
						layouSwitch.setVisibility(View.GONE);
					}
					
					if(listener != null){
						listener.OnChangeSwitchState();
					}
				}
			});
			
			text_pop_0 = (TextView)viewToast.findViewById(R.id.text_pop_0);
			flagTemp = set.getWIFIVideoSetting();
			if(flagTemp){
				text_pop_0.setText(context.getString(R.string.settingall_2b));
				layouSwitch.setVisibility(View.VISIBLE);
			} else {
				text_pop_0.setText(context.getString(R.string.settingall_2c));
				layouSwitch.setVisibility(View.GONE);
			}
			popSetingView0.setOnPopup(flagTemp);
			
			myTextSettingView0 = (vn.com.sonca.SettingAll.MyTextSettingView)viewToast.findViewById(R.id.myTextSettingView0);
			myTextSettingView0.setTitleView(context.getString(R.string.settingall_3));
			
			itemTimer1 = (ItemTimerView) viewToast.findViewById(R.id.button_timer_1);
			itemTimer2 = (ItemTimerView) viewToast.findViewById(R.id.button_timer_2);
			itemTimer3 = (ItemTimerView) viewToast.findViewById(R.id.button_timer_3);
			itemTimer4 = (ItemTimerView) viewToast.findViewById(R.id.button_timer_4);
			itemTimer5 = (ItemTimerView) viewToast.findViewById(R.id.button_timer_5);
			
			itemTimer1.setOnItemTimerListener(this);
			itemTimer2.setOnItemTimerListener(this);
			itemTimer3.setOnItemTimerListener(this);
			itemTimer4.setOnItemTimerListener(this);
			itemTimer5.setOnItemTimerListener(this);
			
			String giay = context.getString(R.string.text_change_sleep_0);
			String phut = context.getString(R.string.text_change_sleep_1);
			Sleep sleep1 = new Sleep(0, context.getString(R.string.settingall_3z), 
					(MyApplication.switchTime == 0)); 
			Sleep sleep2 = new Sleep(30, context.getString(R.string.settingall_3a) + " "+ 30 + giay, 
					(MyApplication.switchTime == 30));
			Sleep sleep3 = new Sleep(60, context.getString(R.string.settingall_3a) + " "+ 1 + phut, 
					(MyApplication.switchTime == 60));
			Sleep sleep4 = new Sleep(120, context.getString(R.string.settingall_3a) + " "+ 2 + phut, 
					(MyApplication.switchTime == 120));
			Sleep sleep5 = new Sleep(300, context.getString(R.string.settingall_3a) + " "+ 5 + phut, 
					(MyApplication.switchTime == 300));
			
			itemTimer1.setData(sleep1);
			itemTimer2.setData(sleep2);
			itemTimer3.setData(sleep3);
			itemTimer4.setData(sleep4);
			itemTimer5.setData(sleep5);		
			
			textTimerLine1 = (TextView)viewToast.findViewById(R.id.text_timer_line_1);
			setTextTimer((int) MyApplication.switchTime);
			
			//------------------------------------------------
			myTextSettingViewA1 = (vn.com.sonca.SettingAll.MyTextSettingView)viewToast.findViewById(R.id.myTextSettingViewA1);
			popSetingViewA1 = (vn.com.sonca.SettingAll.PopSettingView)viewToast.findViewById(R.id.popSettingViewA1);
			text_pop_a1 = (TextView)viewToast.findViewById(R.id.text_pop_a1);
			
			myTextSettingViewA1.setTitleView(context.getString(R.string.settingall_4a));
			
			popSetingViewA1.setTitleView(context.getString(R.string.settingall_4));
			popSetingViewA1.setOnPopSettingListener(new vn.com.sonca.SettingAll.PopSettingView.OnPopSettingListener() {
				
				@Override
				public void onSetingPopup(boolean flagOnPopup) {
					MyApplication.flagOnAdminYouTube = flagOnPopup;
					AppSettings set = AppSettings.getInstance(context);
					set.saveAdminYouTube(flagOnPopup);
					
					if(flagOnPopup){
						text_pop_a1.setText(context.getString(R.string.settingall_4b));
					} else {
						text_pop_a1.setText(context.getString(R.string.settingall_4c));
					}
					
					if(listener != null){
						listener.OnChangeAdminYouTube();
					}
				}
			});
			
			flagTemp = set.getAdminYouTube();
			if(flagTemp){
				text_pop_a1.setText(context.getString(R.string.settingall_4b));
			} else {
				text_pop_a1.setText(context.getString(R.string.settingall_4c));
			}
			popSetingViewA1.setOnPopup(flagTemp);
			
			// YOUTUBE MOI
			btnCommandYouTube = (ButonOnOff)viewToast.findViewById(R.id.youtubeCommand);
			btnCommandYouTube.setOnOnOffCommandListener(new OnOnOffCommandListener() {
				
				@Override
				public void OnCommand(int id, boolean bool, boolean isShowDialog) {
					
				}
				
				@Override
				public void OnCommand(int id, int value) {
					if(TouchMainActivity.serverStatus != null || KTVMainActivity.serverStatus != null){
						MyApplication.setCommandMediumYouTube(value);
						if(listener != null){
							listener.OnUpdateKhoaYouTube();
						}
						
					}
				}
			});
						
			btnCommandYouTube.setState(MyApplication.getCommandMediumYouTube());
			
			//------------------------------------------------
			myTextSettingViewA2 = (vn.com.sonca.SettingAll.MyTextSettingView)viewToast.findViewById(R.id.myTextSettingViewA2);
			popSetingViewA2 = (vn.com.sonca.SettingAll.PopSettingView)viewToast.findViewById(R.id.popSettingViewA2);
			text_pop_a2 = (TextView)viewToast.findViewById(R.id.text_pop_a2);
			
			myTextSettingViewA2.setTitleView(context.getString(R.string.settingall_5a));
			
			popSetingViewA2.setTitleView(context.getString(R.string.settingall_5));
			popSetingViewA2.setOnPopSettingListener(new vn.com.sonca.SettingAll.PopSettingView.OnPopSettingListener() {
				
				@Override
				public void onSetingPopup(boolean flagOnPopup) {
					MyApplication.flagOnAdminOnline = flagOnPopup;
					AppSettings set = AppSettings.getInstance(context);
					set.saveAdminOnline(flagOnPopup);
					
					if(flagOnPopup){
						text_pop_a2.setText(context.getString(R.string.settingall_5b));
					} else {
						text_pop_a2.setText(context.getString(R.string.settingall_5c));
					}
				}
			});
			
			flagTemp = set.getAdminOnline();
			if(flagTemp){
				text_pop_a2.setText(context.getString(R.string.settingall_5b));
			} else {
				text_pop_a2.setText(context.getString(R.string.settingall_5c));
			}
			popSetingViewA2.setOnPopup(flagTemp);
			
			//------------------------------------------------
			
			myTextSettingViewA3 = (vn.com.sonca.SettingAll.MyTextSettingView)viewToast.findViewById(R.id.myTextSettingViewA3);
			popSetingViewA3 = (vn.com.sonca.SettingAll.PopSettingView)viewToast.findViewById(R.id.popSettingViewA3);
			text_pop_a3 = (TextView)viewToast.findViewById(R.id.text_pop_a3);
			
			myTextSettingViewA3.setTitleView(context.getString(R.string.settingall_6a));
			
			popSetingViewA3.setTitleView(context.getString(R.string.settingall_6));
			popSetingViewA3.setOnPopSettingListener(new vn.com.sonca.SettingAll.PopSettingView.OnPopSettingListener() {
				
				@Override
				public void onSetingPopup(boolean flagOnPopup) {					
					if(flagOnPopup){
						text_pop_a3.setText(context.getString(R.string.settingall_6b));
					} else {
						text_pop_a3.setText(context.getString(R.string.settingall_6c));
					}
					
					mainActivity.processSwitchKeyboard(flagOnPopup);
				}
			});
			
			flagTemp = MyApplication.flagRealKeyboard;
			if(flagTemp){
				text_pop_a3.setText(context.getString(R.string.settingall_6b));
			} else {
				text_pop_a3.setText(context.getString(R.string.settingall_6c));
			}
			popSetingViewA3.setOnPopup(flagTemp);
			
			//------------------------------------------------
			myTextSettingViewA4 = (vn.com.sonca.SettingAll.MyTextSettingView)viewToast.findViewById(R.id.myTextSettingViewA4);
			popSetingViewA4 = (vn.com.sonca.SettingAll.PopSettingView)viewToast.findViewById(R.id.popSettingViewA4);
			text_pop_a4 = (TextView)viewToast.findViewById(R.id.text_pop_a4);
			
			myTextSettingViewA4.setTitleView(context.getString(R.string.settingall_7a));
			
			popSetingViewA4.setTitleView(context.getString(R.string.settingall_7));
			popSetingViewA4.setOnPopSettingListener(new vn.com.sonca.SettingAll.PopSettingView.OnPopSettingListener() {
				
				@Override
				public void onSetingPopup(boolean flagOnPopup) {
					MyApplication.flagAllowSearchOnline = flagOnPopup;
					AppSettings set = AppSettings.getInstance(context);
					set.saveSearchOnline(flagOnPopup);
					
					if(flagOnPopup){
						text_pop_a4.setText(context.getString(R.string.settingall_7b));
					} else {
						text_pop_a4.setText(context.getString(R.string.settingall_7c));
					}
					
					if(mainActivity != null){
						mainActivity.resetTheLoaiList();	
					}
					
				}
			});
			
			flagTemp = MyApplication.flagAllowSearchOnline;
			if(flagTemp){
				text_pop_a4.setText(context.getString(R.string.settingall_7b));
			} else {
				text_pop_a4.setText(context.getString(R.string.settingall_7c));
			}
			popSetingViewA4.setOnPopup(flagTemp);
			
			//------------------------------------------------
			myTextSettingViewA5 = (vn.com.sonca.SettingAll.MyTextSettingView)viewToast.findViewById(R.id.myTextSettingViewA5);
			popSetingViewA5 = (vn.com.sonca.SettingAll.PopSettingView)viewToast.findViewById(R.id.popSettingViewA5);
			text_pop_a5 = (TextView)viewToast.findViewById(R.id.text_pop_a5);
			
			myTextSettingViewA5.setTitleView(context.getString(R.string.settingall_8a));
			
			popSetingViewA5.setTitleView(context.getString(R.string.settingall_8));
			popSetingViewA5.setOnPopSettingListener(new vn.com.sonca.SettingAll.PopSettingView.OnPopSettingListener() {
				
				@Override
				public void onSetingPopup(boolean flagOnPopup) {
					MyApplication.flagAllowAdvSong = flagOnPopup;
					AppSettings set = AppSettings.getInstance(context);
					set.saveAdvSong(flagOnPopup);
					
					if(flagOnPopup){
						text_pop_a5.setText(context.getString(R.string.settingall_8b));
					} else {
						text_pop_a5.setText(context.getString(R.string.settingall_8c));
					}
					
					if(mainActivity != null){
						mainActivity.resetAdvAllow();	
					}
					
				}
			});
			
			flagTemp = MyApplication.flagAllowAdvSong;
			if(flagTemp){
				text_pop_a5.setText(context.getString(R.string.settingall_8b));
			} else {
				text_pop_a5.setText(context.getString(R.string.settingall_8c));
			}
			popSetingViewA5.setOnPopup(flagTemp);
			
			//------------------------------------------------
			
			changeColorScreen();
			updateScreenDisplay();
			
			window.addContentView(viewToast, params_relative);
			animaFaceIn = AnimationUtils.loadAnimation(context,R.anim.fade_in); 
			animaFaceOut = AnimationUtils.loadAnimation(context,R.anim.fade_out); 
			animaFaceIn.setAnimationListener(new AnimationListener() {
				@Override public void onAnimationStart(Animation animation) {}
				@Override public void onAnimationRepeat(Animation animation) {}
				@Override public void onAnimationEnd(Animation animation) {
					enableClick = true;
				}
			});
			animaFaceOut.setAnimationListener(new AnimationListener() {
				@Override public void onAnimationStart(Animation animation) {}
				@Override public void onAnimationRepeat(Animation animation) {}
				@Override public void onAnimationEnd(Animation animation) {
					hideToast();
				}
			});
			viewToast.startAnimation(animaFaceIn);
		}
			
	}
	
	public void hideToastBox(){
		if (enableClick && viewToast != null && window != null) {
			viewToast.startAnimation(animaFaceOut);
		}
	}
	
	private void hideToast(){
		if(viewToast != null && window != null){
			((ViewManager)viewToast.getParent()).removeView(viewToast);
			viewToast = null;
			if(listener != null){
				listener.OnFinishListener();
			}
		}
	}

	@Override
	public void OnScreenClick(int id) {
		if(id == R.id.button_screen_1){
			if(listener != null){
				listener.OnChangeColorScreen(MyApplication.SCREEN_BLUE);
			}
		}else if(id == R.id.button_screen_2){
			if(listener != null){
				listener.OnChangeColorScreen(MyApplication.SCREEN_KTVUI);
			}
		}		
	}
	
	private void updateScreenDisplay(){
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE){
			textScreenLine1.setText(context.getString(R.string.setting_screen_1));
		}
		
		if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			textScreenLine1.setText(context.getString(R.string.setting_screen_2));
		}
	}

	public void updateScreen(){
		itemScreen1.setData(context.getString(R.string.setting_but_screen_1),
				MyApplication.intColorScreen == MyApplication.SCREEN_BLUE);
		itemScreen2.setData(context.getString(R.string.setting_but_screen_2),
				MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI);
	}
	
	@Override
	public void OnTimerClick(int id, int tgian) {
		Sleep sleep1 = itemTimer1.getSleep();
		Sleep sleep2 = itemTimer2.getSleep();
		Sleep sleep3 = itemTimer3.getSleep();
		Sleep sleep4 = itemTimer4.getSleep();
		Sleep sleep5 = itemTimer5.getSleep();
		
		sleep1.setActive(sleep1.getGiay() == tgian);
		sleep2.setActive(sleep2.getGiay() == tgian);
		sleep3.setActive(sleep3.getGiay() == tgian);
		sleep4.setActive(sleep4.getGiay() == tgian);
		sleep5.setActive(sleep5.getGiay() == tgian);
		
		itemTimer1.setData(sleep1);
		itemTimer2.setData(sleep2);
		itemTimer3.setData(sleep3);
		itemTimer4.setData(sleep4);
		itemTimer5.setData(sleep5);
		
		MyApplication.switchTime = tgian;
		AppSettings setting = AppSettings.getInstance(context);
		setting.saveSwitchTime(MyApplication.switchTime);
		setTextTimer(tgian);
		
	}
	
	private void setTextTimer(int tgian){
		String str1 = "";
		switch (tgian) {
		case 0:
			str1 = context.getString(R.string.settingall_3b);
			break;
		case 10:
		case 30:
			str1 = context.getString(R.string.settingall_3c) + " " + 
					tgian + context.getString(R.string.text_change_sleep_0);
			break;
		case 60:
		case 120:
		case 300:
			str1 = context.getString(R.string.settingall_3c) + " " + 
					tgian/60 + context.getString(R.string.text_change_sleep_1);
			break;
		default:
			str1 = context.getString(R.string.settingall_3c) + " " + 
					tgian + context.getString(R.string.text_change_sleep_0);
			break;
		}
		textTimerLine1.setText(str1);
	}
	
	private void changeColorScreen(){
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			layoutScreen.setBackgroundResource(R.drawable.mainbg);
			layoutCenter.setBackgroundResource(R.drawable.background_khoadieukhien);
			layoutRight.setBackgroundResource(R.drawable.background_khoadieukhien);
			viewLine1.setBackgroundResource(R.drawable.touch_shape_line_ver);
			textScreenLine1.setTextColor(Color.parseColor("#B4FEFF"));
			textScreenLine1.setText(context.getString(R.string.setting_screen_2));
			text_pop_1.setTextColor(Color.parseColor("#B4FEFF"));
			viewSpace1.setBackgroundColor(Color.parseColor("#01192D"));
			viewSpace2.setBackgroundColor(Color.parseColor("#01192D"));
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			layoutScreen.setBackgroundColor(Color.parseColor("#C1FFE8"));
			layoutCenter.setBackgroundColor(Color.parseColor("#C1FFE8"));
			layoutRight.setBackgroundColor(Color.parseColor("#C1FFE8"));
			viewLine1.setBackgroundResource(R.drawable.zlight_shape_line_ver);
			textScreenLine1.setTextColor(Color.parseColor("#21BAA9"));
			textScreenLine1.setText(context.getString(R.string.setting_screen_1));
			text_pop_1.setTextColor(Color.parseColor("#21BAA9"));
			viewSpace1.setBackgroundColor(Color.TRANSPARENT);
			viewSpace2.setBackgroundColor(Color.TRANSPARENT);
		}
		topSettingView.invalidate();
		myTextSettingView2.invalidate();
		myTextSettingView3.invalidate();
		myTextSettingView4.invalidate();
		myTextSettingViewNew.invalidate();
		popSetingView.invalidate();
		appInfoView.invalidate();
		appInfoView.invalidate();
		downVideoView.invalidate();
		xoaVideoView.invalidate();
	}

	@Override
	public void OnButClick() {
		hideToastBox();
	}

	
	
	// ---------- CHECK VERSION APP
	public void processUpdateApp() {
		new PingAppVersionTask().execute("");
	}

	class PingAppVersionTask extends AsyncTask<String, Void, Void> {

		protected Void doInBackground(String... urls) {
			try {
				if (!isAppVersionURLReachable(context)) {
					// do nothing
					return null;
				}

				processDownloadVersionFile();
				return null;
			} catch (Exception e) {
				return null;
			}
		}

		protected void onPostExecute(Void feed) {
		}
	}

	private boolean isAppVersionURLReachable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			try {
				URL url = new URL(
						"https://kos.soncamedia.com/firmware/KarConnect/check_update.xml");
				HttpURLConnection urlc = (HttpURLConnection) url
						.openConnection();
				urlc.setConnectTimeout(3 * 1000); // 3 s.
				urlc.connect();
				if (urlc.getResponseCode() == 200) { // 200 = "OK" code (http
														// connection is fine).
					// Log.wtf("Connection", "Success !");
					return true;
				} else {
					return false;
				}
			} catch (MalformedURLException e1) {
				return false;
			} catch (IOException e) {
				return false;
			}
		}
		return false;
	}

	private void processDownloadVersionFile() {
		// MyLog.e("processDownloadVersionFile", "...........................");
		String appVersion = "";
		try {
			PackageInfo pInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			appVersion = pInfo.versionName;
		} catch (Exception e) {
			return;
		}

		String appRootPath = android.os.Environment
				.getExternalStorageDirectory()
				.toString()
				.concat(String.format("/%s/%s", "Android/data",
						context.getPackageName()));
		File appBundle = new File(appRootPath);
		if (!appBundle.exists())
			appBundle.mkdir();

		final String appDir = appRootPath.concat("/APP");
		File appDirFile = new File(appDir);
		if (!appDirFile.exists()) {
			appDirFile.mkdir();
		}

		final String updateInfo_path = appDir.concat("/check_update.xml");
		File tempF = new File(updateInfo_path);
		if (tempF.exists()) {
			tempF.delete();
		}

		new Thread(new Runnable() {
			public void run() {
				downloadAppVersionFile(
						"https://kos.soncamedia.com/firmware/KarConnect/check_update.xml",
						updateInfo_path);
			}
		}).start();
	}

	public void downloadAppVersionFile(String url, String dest_file_path) {
		try {
			File dest_file = new File(dest_file_path);
			URL u = new URL(url);
			DataInputStream fis = new DataInputStream(u.openStream());
			DataOutputStream fos = new DataOutputStream(new FileOutputStream(
					dest_file));

			int bytes_read = 0;
			int buffer_size = 1024 * 1024;
			byte[] buffer = new byte[buffer_size];

			while ((bytes_read = fis.read(buffer, 0, buffer_size)) > 0) {
				fos.write(buffer, 0, bytes_read);
			}

			fis.close();
			fos.flush();
			fos.close();

			checkAppVersion();
		} catch (Exception e) {
			clearDownloadAppResource();
			return;
		}
	}

	private void clearDownloadAppResource() {
		try {
			String appRootPath = android.os.Environment
					.getExternalStorageDirectory()
					.toString()
					.concat(String.format("/%s/%s", "Android/data",
							context.getPackageName()));
			String xmlPath = appRootPath.concat("/APP/check_update.xml");
			File f = new File(xmlPath);
			if (f.exists()) {
				f.delete();
			}
		} catch (Exception e) {
		}
	}

	private boolean checkAppVersion() {
		try {
			final String appRootPath = android.os.Environment
					.getExternalStorageDirectory()
					.toString()
					.concat(String.format("/%s/%s", "Android/data",
							context.getPackageName()));
			String updateInfo_path = appRootPath
					.concat("/APP/check_update.xml");

			// ----- READ FILE check_update.xml to find info
			InputStream is = new FileInputStream(updateInfo_path);
			Document doc = XmlUtils.convertToDocument(is);
			NodeList nodeListLyric = doc.getElementsByTagName("app_version");

			Element eLyric = (Element) nodeListLyric.item(0);
			NodeList nodeList = eLyric.getElementsByTagName("android");

			String serverVersion = "";
			for (int i = 0; i < nodeList.getLength(); i++) {
				Node node = nodeList.item(i);
				Element e = (Element) node;

				NodeList nlTemp = e.getElementsByTagName("ver");
				Node nTemp = nlTemp.item(0);
				serverVersion = nTemp.getTextContent();
			}

			String appVersion = "";
			PackageInfo pInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			appVersion = pInfo.versionName;

			// MyLog.e("checkAppVersion", "app = " + appVersion +
			// " -- server = "
			// + serverVersion);

			// Compare Version
			String[] appData = appVersion.split("\\.");
			String[] serverData = serverVersion.split("\\.");

			int intAppVersion = Integer.valueOf(appData[0]) * 100
					+ Integer.valueOf(appData[1]) * 10
					+ Integer.valueOf(appData[2]);
			int intServerVersion = 0;
			intServerVersion = Integer.valueOf(serverData[0]) * 100
					+ Integer.valueOf(serverData[1]) * 10
					+ Integer.valueOf(serverData[2]);

			// MyLog.e("checkAppVersion", "app = " + intAppVersion
			// + " -- server = " + intServerVersion);

			clearDownloadAppResource();

			if (intServerVersion > intAppVersion) {
				appInfoView.setLayoutUpdateApp();
			}
		} catch (Exception e) {
			clearDownloadAppResource();
		}
		return true;
	}

	// -------END CHECK VERSION APP---------//
	
	// ------- GET RUNNING DATA ---------//
	private void processGetUpdateData(){
		if(appInfoView == null){
			return;
		}
		
		String resultStr = "";
		AppSettings settings = AppSettings
				.getInstance(context.getApplicationContext());
		
		if(MyApplication.intSvrModel == MyApplication.SONCA || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
			resultStr = "V." + settings.loadServerLastUpdate(1);
			resultStr += "-" + settings.loadServerLastUpdate(3);
		} else if(MyApplication.intSvrModel == MyApplication.SONCA_KARTROL || MyApplication.intSvrModel == MyApplication.SONCA_KM1){ // KARTROL
			resultStr = "V" + settings.loadHiWDISCVersion();	
		} else if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
				|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
				 || MyApplication.intSvrModel == MyApplication.SONCA_KBX9 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
				 || MyApplication.intSvrModel == MyApplication.SONCA_TBT){
//			if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
//				if (KTVMainActivity.intTocHDDVol == 0) {
//					if(AppSettings.checkContentUSB(MyApplication.intMTocType)){
//						resultStr = "USB V" + KTVMainActivity.intTocDISCVol / 100 + " - REV" + KTVMainActivity.intTocDISCVol % 100;	
//					} else {
//						resultStr = "DISC V" + KTVMainActivity.intTocDISCVol / 100 + " - REV" + KTVMainActivity.intTocDISCVol % 100;
//					}	
//				} else {
//					resultStr = "HDD V" + KTVMainActivity.intTocHDDVol;
//				}
//			} else {
//				if (TouchMainActivity.intTocHDDVol == 0) {
//					if(AppSettings.checkContentUSB(MyApplication.intMTocType)){
//						resultStr = "USB V" + TouchMainActivity.intTocDISCVol / 100 + " - REV" + TouchMainActivity.intTocDISCVol % 100;	
//					} else {
//						resultStr = "DISC V" + TouchMainActivity.intTocDISCVol / 100 + " - REV" + TouchMainActivity.intTocDISCVol % 100;
//					}	
//				} else {
//					resultStr = "HDD V" + TouchMainActivity.intTocHDDVol;
//				}
//				
//			}
			resultStr = "V." + settings.loadHiWHDDVersion();
			resultStr += "-" + settings.loadHiWDISCVersion();
			
		}
		
		appInfoView.setLayoutUpdateData(resultStr);
	}
	// -------END GET RUNNING DATA---------//
	
	private boolean checkVideoExist(){
		try {
			String rootPath = android.os.Environment.getExternalStorageDirectory().toString().concat("/SONCA");
			
			File parentFolder = new File(rootPath.concat("/VIDEO"));

			int fileCount = parentFolder.listFiles().length;
			if (fileCount == 0) {
				return false;
			}
			
			return true;
		} catch (Exception e) {
			return false;
		}
		
	}
	
	public void updateXoaVideoView(){
		if(xoaVideoView == null){
			return;
		}
		
		MyLog.d(TAB, "updateXoaVideoView");
		
		if (checkVideoExist()) {
			xoaVideoView.setConnect(true);
		} else {
			xoaVideoView.setConnect(false);
		}
	}
	
	// -----------------------------------------------------------
	public void syncFromServer() {
		if (TouchMainActivity.serverStatus != null || KTVMainActivity.serverStatus != null) {
			if (MyApplication.intSvrModel == MyApplication.SONCA_SMARTK) {
				if (btnCommandYouTube.getState() != MyApplication.getCommandMediumYouTube()) {
					btnCommandYouTube.setState(MyApplication.getCommandMediumYouTube());
				}
			}
		}
	}
}
