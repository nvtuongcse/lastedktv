package vn.com.sonca.Touch.BlockCommand;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewManager;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.zzzzz.MyApplication;
import android.view.animation.Animation.AnimationListener;

public class DialogControl {
	
	private String TAB = "DialogControl";
	public static final int PHONG_HAT = 1;
	public static final int MAC_DINH = 2;
	
	private Context context;
	private Window window;
	private TouchMainActivity activity;
	private FragmentActivity tempActivity;
	private LayoutInflater layoutInflater;	
	private WindowManager.LayoutParams params_relative;
	public DialogControl(Context context, Window window, TouchMainActivity activity) {
		params_relative = new WindowManager.LayoutParams();
		params_relative.height = WindowManager.LayoutParams.MATCH_PARENT;
		params_relative.width = WindowManager.LayoutParams.MATCH_PARENT;
		params_relative.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		params_relative.type = WindowManager.LayoutParams.TYPE_TOAST;
		params_relative.format = PixelFormat.TRANSLUCENT;
		this.activity = activity;
		this.context = context;
		this.window = window;
	}
	
	public DialogControl(Context context, Window window, FragmentActivity activity) {
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
	
	private OnDialogControlListener listener;
	public interface OnDialogControlListener{
		public void OnFinishListener();
		public void OnYesControl();
		public void OnNoControl();
	}
	
	public void setOnOnDialogControlListenerListener(OnDialogControlListener listener){
		this.listener = listener;
	}
	
	private View viewToast = null;
	private Animation animaFaceIn;
	private Animation animaFaceOut;
	private boolean enableClick = false;
	
	public void showToast(int layout){		
		if (window == null || viewToast != null) {
			return;
		}
		if(viewToast == null){
			layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewToast = layoutInflater.inflate(R.layout.dialog_control,null);
			viewToast.findViewById(R.id.Layout).setOnClickListener(new OnClickListener() {
				@Override public void onClick(View arg0) {
					hideToastBox();
				}
			});
			LinearLayout linearLayout = (LinearLayout)viewToast.findViewById(R.id.LinearLayout);
			linearLayout.setOnClickListener(new OnClickListener() {
				@Override public void onClick(View arg0) {}
			});
			
			ButtonControl butKhong = (ButtonControl)viewToast.findViewById(R.id.butKhong);
			butKhong.setTextTitle(context.getString(R.string.dance_no));
			butKhong.setOnClickListener(new OnClickListener() {
				@Override public void onClick(View arg0) {
					hideToastBox();
					if(listener != null){
						listener.OnNoControl();
					}
				}
			});
			ButtonControl butCo = (ButtonControl)viewToast.findViewById(R.id.butCo);
			butCo.setTextTitle(context.getString(R.string.dance_yes));
			butCo.setOnClickListener(new OnClickListener() {
				@Override public void onClick(View arg0) {
					hideToastBox();
					if(listener != null){
						listener.OnYesControl();
					}
				}
			});
			
			TextView textView = (TextView)viewToast.findViewById(R.id.textView);
			if(layout == PHONG_HAT){
				textView.setText(context.getString(R.string.text_thong_bao_3a));
			}else if(layout == MAC_DINH){
				textView.setText(context.getString(R.string.text_thong_bao_3b));
			}
			
			if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				linearLayout.setBackgroundResource(R.drawable.icon_boder_popup);
				textView.setTextColor(Color.parseColor("#B4FEFF"));
			}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
				linearLayout.setBackgroundResource(R.drawable.zlight_boder_popup);
				textView.setTextColor(Color.parseColor("#005249"));
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
		if (enableClick && viewToast != null && window != null) {
			viewToast.startAnimation(animaFaceOut);
		}
	}
	
	private void hideToast(){
		if(listener != null){
			listener.OnFinishListener();
		}
		if(viewToast != null && window != null){
			((ViewManager)viewToast.getParent()).removeView(viewToast);
			viewToast = null;
		}
	}


}
