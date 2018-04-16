package vn.com.sonca.SettingAll;


import android.content.Context;
import android.graphics.PixelFormat;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewManager;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import vn.com.hanhphuc.karremote.R;import vn.com.sonca.SettingAll.PopSettingView.OnPopSettingListener;
import vn.com.sonca.SetttingApp.ButtonSettingView.OnButSettingListener;
import vn.com.sonca.SetttingApp.ItemScreenView.OnItemScreenListener;
import vn.com.sonca.SetttingApp.ItemTimerView;
import vn.com.sonca.SetttingApp.ItemTimerView.OnItemTimerListener;
import vn.com.sonca.SleepDevice.Sleep;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.utils.AppSettings;
import vn.com.sonca.zktv.main.KTVMainActivity;
import vn.com.sonca.zzzzz.MyApplication;

public class DialogSettingAll implements OnItemTimerListener, 
	OnItemScreenListener, OnButSettingListener {
	
	private String TAB = "DialogSettingAll";
	
	private Window window;
	private Context context;
	private LayoutInflater layoutInflater;	
	private WindowManager.LayoutParams params_relative;
	private TouchMainActivity mainActivity;
	private KTVMainActivity ktvMainActivity;
	public DialogSettingAll(Context context, Window window, TouchMainActivity mainActivity) {
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
	
	public DialogSettingAll(Context context, Window window, KTVMainActivity mainActivity) {
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
	
	private OnDialogSettingAllListener listener;
	public interface OnDialogSettingAllListener {
		public void OnFinishListener();
		public void OnChangeSwitchState();
	}
	
	public void setOnDialogSettingAllListener(OnDialogSettingAllListener listener){
		this.listener = listener;
	}
	
	private View viewToast = null;
	private View viewBottomScreen = null;
	private Animation animaFaceIn;
	private Animation animaFaceOut;
	private LinearLayout layoutScreen;
	private LinearLayout layoutCenter;
	private TopSettingView topSettingView;
	private View viewLine1;
	
	private PopSettingView popSetingView;
	private MyTextSettingView myTextSettingView1;
	private MyTextSettingView myTextSettingView4;
	private ItemTimerView itemTimer1;
	private ItemTimerView itemTimer2;
	private ItemTimerView itemTimer3;
	private ItemTimerView itemTimer4;
	private ItemTimerView itemTimer5;
	private TextView text_pop_1;
	private View viewSpace1;
	private View viewSpace2;
	private TextView textTimerLine1;
	
	private LinearLayout layouSwitch;
		
	private boolean enableClick = false;
	public void showToast(){		
		if (window == null || viewToast != null) {
			return;
		}
		if(viewToast == null){
			layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewToast = layoutInflater.inflate(R.layout.dialog_settingall, null);

			layoutScreen = (LinearLayout)viewToast.findViewById(R.id.layoutScreen);
			layouSwitch = (LinearLayout)viewToast.findViewById(R.id.layouSwitch);
			layoutScreen.setBackgroundResource(R.drawable.mainbg);
			
			topSettingView = (TopSettingView)viewToast.findViewById(R.id.TopSettingView);
			
			if(TouchMainActivity.serverStatus != null || KTVMainActivity.serverStatus != null){
				if(((MyApplication)getMyActivity().getApplication()).getDeviceCurrent() != null){
					topSettingView.setNameDevice(((MyApplication)getMyActivity().getApplication()).getDeviceCurrent().getName());
				}				
			} else {
				topSettingView.setNameDevice("");
			}
			
			topSettingView.setOnBackListener(new vn.com.sonca.SettingAll.TopSettingView.OnBackListener() {
				
				@Override
				public void OnBackListener() {
					hideToastBox();
				}
			});
			
			viewBottomScreen = (View)viewToast.findViewById(R.id.viewBottomScreen);
			layoutCenter = (LinearLayout)viewToast.findViewById(R.id.layoutCenter);

			myTextSettingView4 = (MyTextSettingView)viewToast.findViewById(R.id.myTextSettingView4);
			myTextSettingView4.setTitleView(context.getString(R.string.settingall_2));
			
			popSetingView = (PopSettingView)viewToast.findViewById(R.id.popSettingView);
			popSetingView.setTitleView(context.getString(R.string.settingall_2a));
			popSetingView.setOnPopSettingListener(new OnPopSettingListener() {
				
				@Override
				public void onSetingPopup(boolean flagOnPopup) {
					MyApplication.flagOnWifiVideo = flagOnPopup;
					AppSettings set = AppSettings.getInstance(context);
					set.saveWIFIVideoSetting(flagOnPopup);
					
					if(flagOnPopup){
						text_pop_1.setText(context.getString(R.string.settingall_2b));
						layouSwitch.setVisibility(View.VISIBLE);
					} else {
						text_pop_1.setText(context.getString(R.string.settingall_2c));
						layouSwitch.setVisibility(View.GONE);
					}
					
					if(listener != null){
						listener.OnChangeSwitchState();
					}
				}
			});
			
			text_pop_1 = (TextView)viewToast.findViewById(R.id.text_pop_1);
			AppSettings set = AppSettings.getInstance(context);
			boolean flagTemp = set.getWIFIVideoSetting();
			if(flagTemp){
				text_pop_1.setText(context.getString(R.string.settingall_2b));
				layouSwitch.setVisibility(View.VISIBLE);
			} else {
				text_pop_1.setText(context.getString(R.string.settingall_2c));
				layouSwitch.setVisibility(View.GONE);
			}
			popSetingView.setOnPopup(flagTemp);
			
			// ------------
			myTextSettingView1 = (MyTextSettingView)viewToast.findViewById(R.id.myTextSettingView1);
			myTextSettingView1.setTitleView(context.getString(R.string.settingall_3));
			
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
			Sleep sleep2 = new Sleep(10, context.getString(R.string.settingall_3a) + " "+ 10 + giay, 
					(MyApplication.switchTime == 10));
			Sleep sleep3 = new Sleep(30, context.getString(R.string.settingall_3a) + " "+ 30 + giay, 
					(MyApplication.switchTime == 30));
			Sleep sleep4 = new Sleep(60, context.getString(R.string.settingall_3a) + " "+ 1 + phut, 
					(MyApplication.switchTime == 60));
			Sleep sleep5 = new Sleep(120, context.getString(R.string.settingall_3a) + " "+ 2 + phut, 
					(MyApplication.switchTime == 120));
			
			itemTimer1.setData(sleep1);
			itemTimer2.setData(sleep2);
			itemTimer3.setData(sleep3);
			itemTimer4.setData(sleep4);
			itemTimer5.setData(sleep5);		
			
			textTimerLine1 = (TextView)viewToast.findViewById(R.id.text_timer_line_1);
			setTextTimer((int) MyApplication.switchTime);
			
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
		
	}

	public void updateScreen(){

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

	@Override
	public void OnButClick() {
		hideToastBox();
	}
	
}
