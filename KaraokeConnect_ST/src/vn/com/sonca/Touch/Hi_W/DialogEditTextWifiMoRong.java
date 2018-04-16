package vn.com.sonca.Touch.Hi_W;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewManager;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.BlockCommand.ButtonControl;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.zzzzz.MyApplication;

public class DialogEditTextWifiMoRong {
	
	private String TAB = "DialogHiW";
	
	private int MINIMUM = 0;
	private int CONSTANT = 0;
	private int IDVIEW = 0;
	
	private Context context;
	private Window window;
	private TouchMainActivity activity;
	private FragmentActivity tempActivity;
	private LayoutInflater layoutInflater;	
	private WindowManager.LayoutParams params_relative;
		
	public DialogEditTextWifiMoRong(Context context, Window window, FragmentActivity activity) {
		matkhau = context.getString(R.string.toast_hiw_info_1);
		toithieu = context.getString(R.string.toast_hiw_info_2);
		chuso = context.getString(R.string.toast_hiw_info_3);
		params_relative = new WindowManager.LayoutParams();
		params_relative.height = WindowManager.LayoutParams.MATCH_PARENT;
		params_relative.width = WindowManager.LayoutParams.MATCH_PARENT;
		params_relative.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		params_relative.type = WindowManager.LayoutParams.TYPE_TOAST;
		params_relative.format = PixelFormat.TRANSLUCENT;
		this.tempActivity = activity;
		this.context = context;
		this.window = window;
	}
	
	private FragmentActivity getMyActivity(){
		return this.tempActivity;
	}
	
	private OnEditTextWifiMoRongListener listener;
	public interface OnEditTextWifiMoRongListener{
		public void OnSendData(String data1, String data2);
		public void OnFinishListener();
	}
	
	public void setOnEditTextWifiMoRongListener(OnEditTextWifiMoRongListener listener){
		this.listener = listener;
	}
	
	private View viewToast = null;
	private EditText editText1 = null;
	private EditText editText2 = null;
	private Animation animaFaceIn;
	private Animation animaFaceOut;
	private boolean enableClick = false;
	
	public void showToast(String title, String data1, String data2){		
		if (window == null || viewToast != null) {
			return;
		}
		if(viewToast == null){
			layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewToast = layoutInflater.inflate(R.layout.dialog_edittext_wifimorong, null);
			viewToast.findViewById(R.id.Layout).setOnClickListener(new OnClickListener() {
				@Override public void onClick(View arg0) {}
			});
			
			LinearLayout layoutBackGroup = (LinearLayout)viewToast.findViewById(R.id.layoutBackgroup);
			ImageView imageView = (ImageView)viewToast.findViewById(R.id.ImageView1);
			TextView textView = (TextView)viewToast.findViewById(R.id.TextView);
			textView.setText(title);
			
			editText1 = (EditText)viewToast.findViewById(R.id.EditText1);
			editText1.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View paramView, boolean paramBoolean) {
					MyLog.e(TAB, "Focus : " + paramBoolean);
					if (paramBoolean) {}else{
						InputMethodManager imm = (InputMethodManager) getMyActivity()
				                .getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(paramView.getWindowToken(), 0);
					}
				}
			});
			editText1.setText(data1);
			
			editText2 = (EditText)viewToast.findViewById(R.id.EditText2);
			editText2.setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View paramView, boolean paramBoolean) {
					MyLog.e(TAB, "Focus : " + paramBoolean);
					if (paramBoolean) {}else{
						InputMethodManager imm = (InputMethodManager) getMyActivity()
				                .getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(paramView.getWindowToken(), 0);
					}
				}
			});
			editText2.setText(data2);
			
			ButtonControl butLeft = (ButtonControl)viewToast.findViewById(R.id.butLeft);
			ButtonControl butRight = (ButtonControl)viewToast.findViewById(R.id.butRight);
			butLeft.setTextTitle("OK");
			butRight.setTextTitle("CANCEL");			
			butLeft.setOnClickListener(new OnClickListener() {
				@Override public void onClick(View paramView) {
					String data1 = editText1.getText().toString();
					if (MINIMUM != 0 && data1.length() < MINIMUM) {
						ShowToastInfo(matkhau + toithieu + MINIMUM + chuso);
						return;
					}
					if(CONSTANT != 0 && data1.length() != CONSTANT){
						if(checkNoNumber(data1)){
							ShowToastInfo(matkhau + CONSTANT + chuso);
							return;
						}
					}
					String data2 = editText2.getText().toString();
					if (MINIMUM != 0 && data2.length() < MINIMUM) {
						ShowToastInfo(matkhau + toithieu + MINIMUM + chuso);
						return;
					}
					if(CONSTANT != 0 && data2.length() != CONSTANT){
						if(checkNoNumber(data2)){
							ShowToastInfo(matkhau + CONSTANT + chuso);
							return;
						}
					}
					if(listener != null){
						listener.OnSendData(
							editText1.getText().toString(), 
							editText2.getText().toString());
					}
					hideToastBox();
				}
			});
			butRight.setOnClickListener(new OnClickListener() {
				@Override public void onClick(View paramView) {
					hideToastBox();
				}
			});
			
			if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				layoutBackGroup.setBackgroundResource(R.drawable.icon_boder_popup);
				imageView.setBackgroundResource(R.drawable.touch_boder_ip_active);
				textView.setTextColor(Color.parseColor("#B4FEFF"));
				editText1.setTextColor(Color.GREEN);
				editText2.setTextColor(Color.GREEN);
			}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
				layoutBackGroup.setBackgroundResource(R.drawable.zlight_boder_popup);
				imageView.setBackgroundResource(R.drawable.zlight_boder_nhapip_hover);
				textView.setTextColor(Color.parseColor("#005249"));
				editText1.setTextColor(Color.parseColor("#21BAA9"));
				editText2.setTextColor(Color.parseColor("#21BAA9"));
			}
			
			window.addContentView(viewToast, params_relative);
			animaFaceIn = AnimationUtils.loadAnimation(context,R.anim.fade_in); 
			animaFaceOut = AnimationUtils.loadAnimation(context,R.anim.fade_out); 
			animaFaceIn.setAnimationListener(new AnimationListener() {
				@Override public void onAnimationStart(Animation animation) {}
				@Override public void onAnimationRepeat(Animation animation) {}
				@Override public void onAnimationEnd(Animation animation) {
					enableClick = true;
					if(editText1 != null){
						editText1.setFocusable(true);
						editText1.requestFocus();
						InputMethodManager imm = (InputMethodManager) getMyActivity()
				                .getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.showSoftInput(editText1, InputMethodManager.SHOW_IMPLICIT);
					}
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
			if(editText1 != null){
				editText1.clearFocus();
			}
			viewToast.startAnimation(animaFaceOut);
		}
	}
	
	private void hideToast(){
		if(listener != null){
			listener.OnFinishListener();
		}
		HideToastInfo();
		if(viewToast != null && window != null){
			((ViewManager)viewToast.getParent()).removeView(viewToast);
			viewToast = null;
		}
	}
	
	public void setConstantMinimum(int constant, int minimum, int id){
		CONSTANT = constant;
		MINIMUM = minimum;
		IDVIEW = id;
	}
	
	private String matkhau = "";
	private String toithieu = "";
	private String chuso = "";
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
		TextView textView = (TextView)toastRoot.findViewById(R.id.textPercentLyric);
		textView.setText(info);
		toastInfo.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,0, 150);
		toastInfo.setDuration(Toast.LENGTH_SHORT);
		toastInfo.show();
	}
	
	private void HideToastInfo(){
		if(toastInfo != null){
			toastInfo.cancel();
			toastInfo = null;
		}
	}
	
	private boolean checkNoNumber(String str) {
		for (int i = 0; i < str.length(); i++) {
			 if(str.charAt(i) > 48 && str.charAt(i) < 58){} else {
				return true;
			}
		}
		return false;
	}

}
