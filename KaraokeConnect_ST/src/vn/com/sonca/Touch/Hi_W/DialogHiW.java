package vn.com.sonca.Touch.Hi_W;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.text.format.Time;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewManager;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewDebug.HierarchyTraceType;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.Hi_W.BottomHiWView.OnClickKiWListener;
import vn.com.sonca.Touch.Hi_W.CheckPassWifiView.OnCheckChangeListener;
import vn.com.sonca.Touch.Hi_W.DialogEditText.OnEditTextListener;
import vn.com.sonca.Touch.Hi_W.DialogEditTextWifiMoRong.OnEditTextWifiMoRongListener;
import vn.com.sonca.Touch.Hi_W.DialogUpdateFirmwareFromServer.OnUpdateFirmwareFromServer;
import vn.com.sonca.Touch.Hi_W.InforFirmwareView.OnInForFirwaveListener;
import vn.com.sonca.Touch.Hi_W.LoadCheckUpdateFirmware.OnLoadCheckUpdateFirmwareListener;
import vn.com.sonca.Touch.Hi_W.TopHiWView.OnBackListener;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.params.ConvertString;
import vn.com.sonca.utils.AppSettings;
import vn.com.sonca.zktv.main.KTVMainActivity;
import vn.com.sonca.zzzzz.MyApplication;

public class DialogHiW implements OnFocusChangeListener, OnLoadCheckUpdateFirmwareListener{
	
	private String TAB = "DialogHiW";
	
	private Context context;
	private Window window;
	private TouchMainActivity activity;
	private KTVMainActivity ktvActivity;
	private LayoutInflater layoutInflater;	
	private WindowManager.LayoutParams params_relative;
	public DialogHiW(Context context, Window window, TouchMainActivity activity) {
		params_relative = new WindowManager.LayoutParams();
		params_relative.height = WindowManager.LayoutParams.MATCH_PARENT;
		params_relative.width = WindowManager.LayoutParams.MATCH_PARENT;
		params_relative.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		params_relative.type = WindowManager.LayoutParams.TYPE_TOAST;
		params_relative.format = PixelFormat.TRANSLUCENT;
		this.activity = activity;
		this.context = context;
		this.window = window;
		
		this.activity.isProcessingSomething = true;
		
	}
	
	public DialogHiW(Context context, Window window, KTVMainActivity activity) {
		params_relative = new WindowManager.LayoutParams();
		params_relative.height = WindowManager.LayoutParams.MATCH_PARENT;
		params_relative.width = WindowManager.LayoutParams.MATCH_PARENT;
		params_relative.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		params_relative.type = WindowManager.LayoutParams.TYPE_TOAST;
		params_relative.format = PixelFormat.TRANSLUCENT;
		this.ktvActivity = activity;
		this.context = context;
		this.window = window;
		
		this.ktvActivity.isProcessingSomething = true;
		
	}
	
	private FragmentActivity getMyActivity(){
		if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			return this.ktvActivity;
		} else {
			return this.activity;
		}
	}
	
	private OnHiWListener listener;
	public interface OnHiWListener{
		public void OnFinishListener();
		public void OnUpdateFirwaveFromWiFi();
		public void OnDownloadFirwaveFromServer();
		public void OnUpdateFirmwareConfig();
		public void OnSendMessage(String msg);
	}
	
	public void setOnHiWListener(OnHiWListener listener){
		this.listener = listener;
	}
	
	private View viewToast = null;
	private Animation animaFaceIn;
	private Animation animaFaceOut;
	private boolean enableClick = false;
	
	private MyEditText editCenter1;
	private MyEditText editCenter2;
	private MyEditText editCenter3;
	private MyEditText editRight1;
	private MyEditText editRight2;
	private CheckPassWifiView checkPassWifiView;
	private CheckNetworkWifi checkNetworkWifi;
	private InforFirmwareView inforFirmwareView;
	public void showToast(String namedevice){		
		if (window == null || viewToast != null) {
			return;
		}
		if(viewToast == null){
			layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);      
			viewToast = layoutInflater.inflate(R.layout.fagment_hi_w, null);
			
			View viewLine1 = (View)viewToast.findViewById(R.id.viewline1);
			View viewLine2 = (View)viewToast.findViewById(R.id.viewline2);
			LinearLayout layoutLeft = (LinearLayout)viewToast.findViewById(R.id.layoutLeft);
			LinearLayout layoutCenter = (LinearLayout)viewToast.findViewById(R.id.layoutCenter);
			LinearLayout layoutRight = (LinearLayout)viewToast.findViewById(R.id.layoutRight);
			LinearLayout layoutScreen = (LinearLayout)viewToast.findViewById(R.id.LayoutScreen);
			layoutScreen.setOnClickListener(new OnClickListener() {
				@Override public void onClick(View arg0) {
					// hideToastBox();
				}
			});
			MyTextView titleLeft = (MyTextView)viewToast.findViewById(R.id.title_left);
			MyTextView titleCenter = (MyTextView)viewToast.findViewById(R.id.title_center);
			MyTextView titleRight = (MyTextView)viewToast.findViewById(R.id.title_right);
			TopHiWView topHiWView = (TopHiWView)viewToast.findViewById(R.id.TopHiWView);
			BottomHiWView bottomHiWView = (BottomHiWView)viewToast.findViewById(R.id.BottomHiWView);
			titleLeft.setTitleView(context.getString(R.string.hiw_title_left));
			titleCenter.setTitleView(context.getString(R.string.hiw_title_center));
			titleRight.setTitleView(context.getString(R.string.hiw_title_right));
			topHiWView.setNameDevice(namedevice);
			topHiWView.setOnBackListener(new OnBackListener() {
				@Override public void OnBackListener() {
					hideToastBox();
				}
			});
			bottomHiWView.setOnClickKiWListener(new OnClickKiWListener() {
				@Override public void OnChange() {
					hideToastBox();
				}
				@Override public void OnCancel() {
					hideToastBox();
				}
			});
			checkNetworkWifi = (CheckNetworkWifi)viewToast.findViewById(R.id.checkNet);   
			checkNetworkWifi.setOnCheckChangeListener(new CheckNetworkWifi.OnCheckChangeListener() {
				@Override public void OnCheckChange(View view, boolean isCheck) {
					editRight1.setActiveView(isCheck);
					editRight2.setActiveView(isCheck);
					
					if(MyApplication.curHiW_firmwareConfig != null){
						if(isCheck){
							MyApplication.curHiW_firmwareConfig.setMode(3);	
							
							ShowDialogEditTextWifiMoRong();
							
						} else {
							MyApplication.curHiW_firmwareConfig.setMode(2);	
							MyApplication.curHiW_firmwareConfig.setStationID("");
							MyApplication.curHiW_firmwareConfig.setStationPass("");
							
							editRight1.setDataView("");
							editRight2.setDataView("");

							if(listener != null){
								listener.OnUpdateFirmwareConfig();
							}
							
						}						
					}
					
				}
			});
			inforFirmwareView = (InforFirmwareView)viewToast.findViewById(R.id.infofrimware);
			
			String resultStr = "";
			AppSettings settings = AppSettings.getInstance(context);
			
			if(MyApplication.intSvrModel == MyApplication.SONCA || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
				resultStr = "VOL " + settings.loadServerLastUpdate(1);
				resultStr += " - " + settings.loadServerLastUpdate(3);
				resultStr += " - " + settings.loadServerLastUpdate(4);
			} else if(MyApplication.intSvrModel == MyApplication.SONCA_KARTROL || MyApplication.intSvrModel == MyApplication.SONCA_KM1){ // KARTROL
				resultStr = "DISC VOL " + settings.loadHiWDISCVersion();			
			} else if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
					|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
					|| MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
					|| MyApplication.intSvrModel == MyApplication.SONCA_TBT || MyApplication.intSvrModel == MyApplication.SONCA_KBX9){
				if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
					if (KTVMainActivity.intTocHDDVol == 0) {
						if(AppSettings.checkContentUSB(MyApplication.intMTocType)){
							resultStr = "USB VOL " + KTVMainActivity.intTocDISCVol / 100 + "." + KTVMainActivity.intTocDISCVol % 100;	
						} else {
							resultStr = "DISC VOL " + KTVMainActivity.intTocDISCVol / 100 + "." + KTVMainActivity.intTocDISCVol % 100;
						}				
					} else {
						resultStr = "HDD VOL " + KTVMainActivity.intTocDISCVol / 100 + "." + KTVMainActivity.intTocDISCVol % 100;
						resultStr += " : " + KTVMainActivity.intTocHDDVol / 100 + "." + KTVMainActivity.intTocHDDVol % 100;
					}
				} else {
					if (TouchMainActivity.intTocHDDVol == 0) {
						if(AppSettings.checkContentUSB(MyApplication.intMTocType)){
							resultStr = "USB VOL " + TouchMainActivity.intTocDISCVol / 100 + "." + TouchMainActivity.intTocDISCVol % 100;	
						} else {
							resultStr = "DISC VOL " + TouchMainActivity.intTocDISCVol / 100 + "." + TouchMainActivity.intTocDISCVol % 100;
						}				
					} else {
						resultStr = "HDD VOL " + TouchMainActivity.intTocDISCVol / 100 + "." + TouchMainActivity.intTocDISCVol % 100;
						resultStr += " : " + TouchMainActivity.intTocHDDVol / 100 + "." + TouchMainActivity.intTocHDDVol % 100;
					}
				}
				
			}			
			inforFirmwareView.setVersionHDD(resultStr);
			inforFirmwareView.setOnInForFirwaveListener(new OnInForFirwaveListener() {
				
				@Override
				public void OnUpdateFirwaveFromWiFi() {
					if(listener != null){
						listener.OnUpdateFirwaveFromWiFi();
					}
				}
				
				@Override
				public void OnDownloadFirwaveFromServer() {
					CheckUpdateFirmwareFromServer(context);
				}
			});
			checkPassWifiView = (CheckPassWifiView)viewToast.findViewById(R.id.checkpass);
			checkPassWifiView.setIntMinimum(8);
			checkPassWifiView.setOnCheckChangeListener(new OnCheckChangeListener() {
				@Override public void OnCheckChange(View view, boolean isCheck) {
					
					if(MyApplication.intWifiRemote != MyApplication.SONCA){
						return;
					}
					
					clearFocusAllView();
					checkPassWifiView.setFocusable(isCheck);

					if(isCheck){
						MyApplication.curHiW_firmwareConfig.setApPass(checkPassWifiView.getDataView());
					} else {
						MyApplication.curHiW_firmwareConfig.setApPass("");
					}
					
					if(isCheck){
						ShowDialogEditText(
								checkPassWifiView.getTitleView(), 
								checkPassWifiView.getDataView(), 
								0, checkPassWifiView.getIntMinimum() , 
								checkPassWifiView.getId());
					} else {
						if(listener != null){
							listener.OnUpdateFirmwareConfig();
						}	
					}
					
				}
			});
			checkPassWifiView.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override public void onFocusChange(View arg0, boolean arg1) {
					
					if(MyApplication.intWifiRemote != MyApplication.SONCA){
						return;
					}
					
					ShowDialogEditText(
							checkPassWifiView.getTitleView(), 
							checkPassWifiView.getDataView(), 
							0, checkPassWifiView.getIntMinimum() , 
							checkPassWifiView.getId());
				}
			});
			editCenter1 = (MyEditText)viewToast.findViewById(R.id.edit_center1);
			editCenter2 = (MyEditText)viewToast.findViewById(R.id.edit_center2);
			editCenter3 = (MyEditText)viewToast.findViewById(R.id.edit_center3);
			editRight1 = (MyEditText)viewToast.findViewById(R.id.edit_right1);
			editRight2 = (MyEditText)viewToast.findViewById(R.id.edit_right2);
			editCenter1.setTitleView(context.getString(R.string.hiw_text_center_1));
			editCenter2.setTitleView(context.getString(R.string.hiw_text_center_3));
			editCenter3.setTitleView(context.getString(R.string.hiw_text_center_4));
			editRight1.setTitleView(context.getString(R.string.hiw_text_right_4));
			editRight2.setTitleView(context.getString(R.string.hiw_text_right_5));
			editCenter1.setOnFocusChangeListener(this);
			editCenter2.setOnFocusChangeListener(this);
			editCenter3.setOnFocusChangeListener(this);
			editRight1.setOnFocusChangeListener(this);
			editRight2.setOnFocusChangeListener(this);
			editCenter1.setConstantView(0);
			editCenter2.setConstantView(4);
			editCenter3.setConstantView(4);
			editRight1.setConstantView(0);
			editRight2.setConstantView(0);
			
			checkNetworkWifi.setCheckView(true);
			boolean isNet = checkNetworkWifi.isCheckView();
			editRight1.setActiveView(isNet);
			editRight2.setActiveView(isNet);
			
			checkPassWifiView.setCheckView(true);
			
			if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				layoutScreen.setBackgroundResource(R.drawable.mainbg);
				layoutLeft.setBackgroundResource(R.drawable.background_khoadieukhien);
				layoutCenter.setBackgroundResource(R.drawable.background_khoadieukhien);
				layoutRight.setBackgroundResource(R.drawable.background_khoadieukhien);
				viewLine1.setBackgroundResource(R.drawable.touch_shape_line_ver);
				viewLine2.setBackgroundResource(R.drawable.touch_shape_line_ver);
			}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
				layoutScreen.setBackgroundColor(Color.parseColor("#C1FFE8"));
				layoutLeft.setBackgroundColor(Color.parseColor("#C1FFE8"));
				layoutCenter.setBackgroundColor(Color.parseColor("#C1FFE8"));
				layoutRight.setBackgroundColor(Color.parseColor("#C1FFE8"));
				viewLine1.setBackgroundResource(R.drawable.zlight_shape_line_ver);
				viewLine2.setBackgroundResource(R.drawable.zlight_shape_line_ver);
			}
			
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
		if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			ktvActivity.isProcessingSomething = false;
		} else {
			activity.isProcessingSomething = false;	
		}
		
		if (enableClick && viewToast != null && window != null) {
			viewToast.startAnimation(animaFaceOut);
		}
	}
	
	private void hideToast(){
		if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			ktvActivity.isProcessingSomething = false;
		} else {
			activity.isProcessingSomething = false;	
		}
		
		HideToastInfo();
		if(listener != null){
			listener.OnFinishListener();
		}
		if(viewToast != null && window != null){
			((ViewManager)viewToast.getParent()).removeView(viewToast);
			viewToast = null;
		}
	}
	
	private DialogEditTextWifiMoRong dialogEditTextWifiMoRong;
	private void ShowDialogEditTextWifiMoRong(){
		if(dialogEditTextWifiMoRong == null){
			dialogEditTextWifiMoRong = new DialogEditTextWifiMoRong(context, window, getMyActivity());
			dialogEditTextWifiMoRong.setOnEditTextWifiMoRongListener(new OnEditTextWifiMoRongListener() {
				
				@Override
				public void OnSendData(String data1, String data2) {
					MyLog.e(TAB, "dialogEditTextWifiMoRong TEN : " + data1);
					MyLog.e(TAB, "dialogEditTextWifiMoRong PASS : " + data2);
					
					try {			
						if(data1 == null || data1.length() == 0 || data1.trim().equals(" ")){
							if(listener != null){
								listener.OnSendMessage(getMyActivity().getResources().getString(R.string.msg_14a));
							}	
							return;
						}
						
						if(data2 == null || data2.length() == 0 || data2.trim().equals(" ")){
							if(listener != null){
								listener.OnSendMessage(getMyActivity().getResources().getString(R.string.msg_14a));
							}	
							return;
						}
						
						for (char tempChar : data1.toCharArray()) {
							String hexLastChar = String.format("%04x", (int) tempChar)
									.toUpperCase();
							if (!Arrays.asList(hiwTenWifi).contains(hexLastChar)) {
								listener.OnSendMessage(getMyActivity().getResources().getString(R.string.check_2));
								return;
							}
						}
						
						for (char tempChar : data2.toCharArray()) {
							String hexLastChar = String.format("%04x", (int) tempChar)
									.toUpperCase();
							if (!Arrays.asList(hiwTenWifi).contains(hexLastChar)) {
								listener.OnSendMessage(getMyActivity().getResources().getString(R.string.check_2));
								return;
							}
						}
						
						ConvertString converter = new ConvertString();
						if(converter.ConvertUnicodeToTCVN(data1).length > 15){
							listener.OnSendMessage(getMyActivity().getResources().getString(R.string.check_3));
							return;
						}
						
						if(converter.ConvertUnicodeToTCVN(data2).length > 15){
							listener.OnSendMessage(getMyActivity().getResources().getString(R.string.check_4));
							return;
						}
						
						editRight1.setDataView(data1);
						editRight2.setDataView(data2);
						
						if(MyApplication.curHiW_firmwareConfig != null && MyApplication.curHiW_firmwareConfig.getStationID() != data1){
							MyApplication.curHiW_firmwareConfig.setStationID(data1);
						}
						
						if(MyApplication.curHiW_firmwareConfig != null && MyApplication.curHiW_firmwareConfig.getStationPass() != data2){
							MyApplication.curHiW_firmwareConfig.setStationPass(data2);
						}
						
						if(listener != null){
							listener.OnUpdateFirmwareConfig();
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				@Override
				public void OnFinishListener() {
					dialogEditTextWifiMoRong = null;
					
					if(MyApplication.curHiW_firmwareConfig.getStationID() == null || MyApplication.curHiW_firmwareConfig.getStationID().equals("")){
						checkNetworkWifi.setFocusable(false);
						checkNetworkWifi.setCheckView(false);
						editRight1.setActiveView(false);
						editRight2.setActiveView(false);
					} else {
						checkNetworkWifi.setFocusable(true);
						checkNetworkWifi.setCheckView(true);
						editRight1.setActiveView(true);
						editRight2.setActiveView(true);
					}
				}
			});
			
			String data1 = null;
			String data2 = null;
			String title = context.getString(R.string.hiw_text_right_6);
			if(editRight1 != null && editRight1 != null){
				data1 = editRight1.getDataView();
				data2 = editRight2.getDataView();
			}
			dialogEditTextWifiMoRong.showToast(title , data1, data2);
		}
	}

	private DialogEditText dialogEditText;
	private void ShowDialogEditText(String title, String data, int constant, int minimum , final int id){
		if(dialogEditText == null){
			dialogEditText = new DialogEditText(context, window, getMyActivity());
			dialogEditText.setOnEditTextListener(new OnEditTextListener() {
				
				@Override
				public void OnFinishListener() {
					dialogEditText = null;
					if(id == R.id.checkpass){
						if(MyApplication.curHiW_firmwareConfig.getApPass() == null || MyApplication.curHiW_firmwareConfig.getApPass().equals("")){
							checkPassWifiView.setFocusable(false);
							checkPassWifiView.setCheckView(false);
						} else {
							checkPassWifiView.setFocusable(true);
							checkPassWifiView.setCheckView(true);
						}
					}	
				}

				@Override
				public void OnSendData(String data, int id) {
					boolean isUpdate = false;
					
					// validate data
					if(id == R.id.edit_center2 || id == R.id.edit_center3){
						try {
							if(data.length() != 4){
								if(listener != null){
									listener.OnSendMessage(getMyActivity().getResources().getString(R.string.msg_14));
								}	
								return;
							}
							
							int tempInt = Integer.parseInt(data);
							if(tempInt > 9999 || tempInt < 0){
								if(listener != null){
									listener.OnSendMessage(getMyActivity().getResources().getString(R.string.msg_14));
								}	
								return;
							}
						} catch (Exception e) {
							if(listener != null){
								listener.OnSendMessage(getMyActivity().getResources().getString(R.string.msg_14));
							}
							return;
						}												
					}		
					
					if(id == R.id.edit_center1){
						try {
							if(data == null || data.length() == 0 || data.trim().equals(" ")){
								if(listener != null){
									listener.OnSendMessage(getMyActivity().getResources().getString(R.string.msg_14a));
								}	
								return;
							}
							
							for (char tempChar : data.toCharArray()) {
								String hexLastChar = String.format("%04x", (int) tempChar)
										.toUpperCase();
								if (!Arrays.asList(hiwTenWifi).contains(hexLastChar)) {
									listener.OnSendMessage(getMyActivity().getResources().getString(R.string.check_2));
									return;
								}
							}
							
							ConvertString converter = new ConvertString();
							if(converter.ConvertUnicodeToTCVN(data).length > 15){
								listener.OnSendMessage(getMyActivity().getResources().getString(R.string.check_1));
								return;
							}
						} catch (Exception e) {
							
						}
					}
					
					if(id == R.id.checkpass || id == R.id.edit_right1 || id == R.id.edit_right2){
						try {									
							for (char tempChar : data.toCharArray()) {
								String hexLastChar = String.format("%04x", (int) tempChar)
										.toUpperCase();
								if (!Arrays.asList(hiwTenWifi).contains(hexLastChar)) {
									listener.OnSendMessage(getMyActivity().getResources().getString(R.string.check_2));
									return;
								}
							}
							
							if(id == R.id.checkpass){
								ConvertString converter = new ConvertString();
								if(converter.ConvertUnicodeToTCVN(data).length > 15){
									listener.OnSendMessage(getMyActivity().getResources().getString(R.string.check_5));
									return;
								}
							}
							
							if(id == R.id.edit_right1){
								ConvertString converter = new ConvertString();
								if(converter.ConvertUnicodeToTCVN(data).length > 15){
									listener.OnSendMessage(getMyActivity().getResources().getString(R.string.check_3));
									return;
								}
							}
							
							if(id == R.id.edit_right2){
								ConvertString converter = new ConvertString();
								if(converter.ConvertUnicodeToTCVN(data).length > 15){
									listener.OnSendMessage(getMyActivity().getResources().getString(R.string.check_4));
									return;
								}
							}
						} catch (Exception e) {
							
						}
					}
					
					switch (id) {
					case R.id.edit_center1:
						editCenter1.setDataView(data);
						if(MyApplication.curHiW_firmwareConfig != null && MyApplication.curHiW_firmwareConfig.getApID() != data){
							isUpdate = true;
							MyApplication.curHiW_firmwareConfig.setApID(data);
						}
						break;
					case R.id.checkpass:
						checkPassWifiView.setDataView(data);
						if(MyApplication.curHiW_firmwareConfig != null && MyApplication.curHiW_firmwareConfig.getApPass() != data){
							isUpdate = true;
							MyApplication.curHiW_firmwareConfig.setApPass(data);
						}
						break;
					case R.id.edit_center2:
						editCenter2.setDataView(data);
						if(MyApplication.curHiW_firmwareConfig != null && MyApplication.curHiW_firmwareConfig.getPassConnect() != data){
							isUpdate = true;
							MyApplication.curHiW_firmwareConfig.setPassConnect(data);
						}
						break;
					case R.id.edit_center3:
						editCenter3.setDataView(data);
						if(MyApplication.curHiW_firmwareConfig != null && MyApplication.curHiW_firmwareConfig.getPassAdmin() != data){
							isUpdate = true;
							MyApplication.curHiW_firmwareConfig.setPassAdmin(data);
						}
						break;
					case R.id.edit_right1:
						editRight1.setDataView(data);
						if(MyApplication.curHiW_firmwareConfig != null && MyApplication.curHiW_firmwareConfig.getStationID() != data){
							isUpdate = false;
							MyApplication.curHiW_firmwareConfig.setStationID(data);
						}
						break;
					case R.id.edit_right2:
						editRight2.setDataView(data);
						if(MyApplication.curHiW_firmwareConfig != null && MyApplication.curHiW_firmwareConfig.getStationPass() != data){
							isUpdate = true;
							MyApplication.curHiW_firmwareConfig.setStationPass(data);
						}
						break;
					default:
						break;
					}
					
					if(listener != null && isUpdate){
						listener.OnUpdateFirmwareConfig();
					}					
					
				}
			});
			dialogEditText.setConstantMinimum(constant, minimum, id);
			dialogEditText.showToast(title , data);
		}
	}
	
	private String[] hiwTenWifi = { "002A", "002B", "002C", "002D", "002E",
			"002F", "003A", "003B", "003C", "003D", "003E", "003F", "004A",
			"004B", "004C", "004D", "004E", "004F", "005A", "005B", "005C",
			"005D", "005E", "005F", "006A", "006B", "006C", "006D", "006E",
			"006F", "007A", "007B", "007C", "007D", "007E", "0020", "0021",
			"0022", "0023", "0024", "0025", "0026", "0027", "0028", "0029",
			"0030", "0031", "0032", "0033", "0034", "0035", "0036", "0037",
			"0038", "0039", "0040", "0041", "0042", "0043", "0044", "0045",
			"0046", "0047", "0048", "0049", "0050", "0051", "0052", "0053",
			"0054", "0055", "0056", "0057", "0058", "0059", "0060", "0061",
			"0062", "0063", "0064", "0065", "0066", "0067", "0068", "0069",
			"0070", "0071", "0072", "0073", "0074", "0075", "0076", "0077",
			"0078", "0079"};
	
	private void clearFocusAllView(){
		if(viewToast != null){
			checkPassWifiView.clearFocus();
			editCenter1.clearFocus();
			editCenter2.clearFocus();
			editCenter3.clearFocus();
			editRight1.clearFocus();
			editRight2.clearFocus();
		}
	}

	@Override
	public void onFocusChange(View view, boolean hasFocus) {
		clearFocusAllView();
		MyEditText editText = ((MyEditText)view);
		editText.setFocusable(hasFocus);
		if(view.getId() == R.id.edit_right1 || view.getId() == R.id.edit_right2){
			ShowDialogEditTextWifiMoRong();
			return;
		}
		ShowDialogEditText(
				editText.getTitleView(), 
				editText.getDataView(), 
				editText.getConstantView(), 0 , view.getId());
	}

	
	public void setFirmwareInfoData(HiW_FirmwareInfo firmwareInfo){
		if(inforFirmwareView == null){
			return;
		}
		inforFirmwareView.setFirmwareData(firmwareInfo);		
	}
	
	public void setFirmwareConfigData(HiW_FirmwareConfig firmwareConfig){
		try {
			int mode = firmwareConfig.getMode();
			
			boolean isNet = false;
			
			switch (mode) {
			case 1:
				isNet = true;
				break;
			case 2:
				isNet = false;
				break;
			case 3:
				isNet = true;
				break;
			default:
				isNet = false;
				break;
			}
			
			editRight1.setActiveView(isNet);
			editRight2.setActiveView(isNet);		
			checkNetworkWifi.setFocusable(isNet);
			checkNetworkWifi.setCheckView(isNet);
			
			editCenter1.setDataView(firmwareConfig.getApID());
			checkPassWifiView.setDataView(firmwareConfig.getApPass());
			if(firmwareConfig.getApPass() == null || firmwareConfig.getApPass().equals("")){
				checkPassWifiView.setFocusable(false);
				checkPassWifiView.setCheckView(false);
			} else {
				checkPassWifiView.setFocusable(true);
				checkPassWifiView.setCheckView(true);
			}
			
			editCenter2.setDataView(firmwareConfig.getPassConnect());
			editCenter3.setDataView(firmwareConfig.getPassAdmin());
			
			editRight1.setDataView(firmwareConfig.getStationID());
			editRight2.setDataView(firmwareConfig.getStationPass());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private LoadCheckUpdateFirmware checkUpdateFirmware;
	private void CheckUpdateFirmwareFromServer(Context context){
		if(checkUpdateFirmware == null){
			checkUpdateFirmware = new LoadCheckUpdateFirmware(context);
			checkUpdateFirmware.setOnLoadCheckUpdateFirmwareListener(this);
			checkUpdateFirmware.execute();
		}else{
			if(checkUpdateFirmware.getStatus() == AsyncTask.Status.FINISHED){
				checkUpdateFirmware = null;
				checkUpdateFirmware = new LoadCheckUpdateFirmware(context);
				checkUpdateFirmware.setOnLoadCheckUpdateFirmwareListener(this);
				checkUpdateFirmware.execute();
			}
		}
	}

	@Override
	public void OnCheckFinish(ArrayList<Firmware> list, int version, int revision) {
		if(!list.isEmpty()){
			ShowDialogUpdateFirmware(list, version, revision);
		}else{
			ShowToastInfo(context.getString(R.string.update_firmware_1));
		}
	}
	
	private DialogUpdateFirmwareFromServer dialogFirmware;
	private void ShowDialogUpdateFirmware(ArrayList<Firmware> list, int version, int revision){
		if(dialogFirmware == null){
			dialogFirmware = new DialogUpdateFirmwareFromServer(context, window, getMyActivity());
			dialogFirmware.setOnUpdateFirmwareFromServer(new OnUpdateFirmwareFromServer() {
				
				@Override
				public void OnFinishListener() {
					dialogFirmware = null;
				}

				@Override
				public void OnCancel() {
					// TODO Auto-generated method stub
					
				}
			});
			dialogFirmware.setLinkDownload(list, version, revision);
			dialogFirmware.showToast();
		}
	}
	
	private Toast toastInfo = null;
	private void ShowToastInfo(String info){
		if(layoutInflater == null){
			return;
		}
		if(toastInfo != null){
			toastInfo.cancel();
			toastInfo = null;
		}
		View toastRoot = layoutInflater.inflate(R.layout.touch_toast_info_hiw, null);
		toastInfo = new Toast(context);
		toastInfo.setView(toastRoot);
		
		LinearLayout layout = (LinearLayout)toastRoot.findViewById(R.id.layout);
		TextView textView = (TextView)toastRoot.findViewById(R.id.textPercentLyric);
		textView.setText(info);
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			layout.setBackgroundResource(R.drawable.icon_boder_popup);
			textView.setTextColor(Color.parseColor("#B4FEFF"));
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			layout.setBackgroundResource(R.drawable.zlight_boder_popup);
			textView.setTextColor(Color.parseColor("#005249"));
		}
		toastInfo.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,0, 0);
		toastInfo.setDuration(Toast.LENGTH_SHORT);
		toastInfo.show();
	}
	
	private void HideToastInfo(){
		if(toastInfo != null){
			toastInfo.cancel();
			toastInfo = null;
		}
	}

	
}
