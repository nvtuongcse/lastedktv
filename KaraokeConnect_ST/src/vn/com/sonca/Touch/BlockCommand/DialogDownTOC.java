package vn.com.sonca.Touch.BlockCommand;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
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
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.zktv.main.KTVMainActivity;
import vn.com.sonca.zzzzz.MyApplication;

public class DialogDownTOC {
	
	private String TAB = "DialogDownTOC";
	private Context context;
	private Window window;
	private TouchMainActivity activity;
	private KTVMainActivity ktvActivity;
	private LayoutInflater layoutInflater;	
	private WindowManager.LayoutParams params_relative;
	
	public DialogDownTOC(Context context, Window window, TouchMainActivity activity) {
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
	
	public DialogDownTOC(Context context, Window window, KTVMainActivity activity) {
		params_relative = new WindowManager.LayoutParams();
		params_relative.height = WindowManager.LayoutParams.MATCH_PARENT;
		params_relative.width = WindowManager.LayoutParams.MATCH_PARENT;
		params_relative.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		params_relative.type = WindowManager.LayoutParams.TYPE_TOAST;
		params_relative.format = PixelFormat.TRANSLUCENT;
		this.ktvActivity = activity;
		this.context = context;
		this.window = window;
	}
	
	private OnDialogDownTOCListener listener;
	public interface OnDialogDownTOCListener{
		public void OnFinishListener();
		public void OnRetryControl();
		public void OnContinueControl();
	}
	
	public void setOnDialogDownTOCListener(OnDialogDownTOCListener listener){
		this.listener = listener;
	}
	
	private View viewToast = null;
	private Animation animaFaceIn;
	private Animation animaFaceOut;
	private boolean enableClick = false;
	
	public void showToast(){		
		if (window == null || viewToast != null) {
			return;
		}
		if(viewToast == null){
			layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewToast = layoutInflater.inflate(R.layout.dialog_control,null);
			viewToast.findViewById(R.id.Layout).setOnClickListener(new OnClickListener() {
				@Override public void onClick(View arg0) {
					// hideToastBox();
				}
			});
			LinearLayout linearLayout =  (LinearLayout)viewToast.findViewById(R.id.LinearLayout);
			linearLayout.setOnClickListener(new OnClickListener() {
				@Override public void onClick(View arg0) {}
			});
			
			ButtonControl butContinue = (ButtonControl)viewToast.findViewById(R.id.butCo);
			butContinue.setTextTitle(context.getString(R.string.busy_download_2));
			butContinue.setOnClickListener(new OnClickListener() {
				@Override public void onClick(View arg0) {
					hideToastBox();
					if(listener != null){
						listener.OnContinueControl();
					}
				}
			});
			ButtonControl butRetry = (ButtonControl)viewToast.findViewById(R.id.butKhong);
			butRetry.setTextTitle(context.getString(R.string.busy_download_3));
			butRetry.setOnClickListener(new OnClickListener() {
				@Override public void onClick(View arg0) {
					hideToastBox();
					if(listener != null){
						listener.OnRetryControl();
					}
				}
			});
			
			TextView textView = (TextView)viewToast.findViewById(R.id.textView);
			textView.setText(context.getString(R.string.busy_download_1));
			
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
