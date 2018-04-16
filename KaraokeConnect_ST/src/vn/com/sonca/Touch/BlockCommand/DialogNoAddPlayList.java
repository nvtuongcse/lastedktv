package vn.com.sonca.Touch.BlockCommand;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
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
import vn.com.sonca.zzzzz.MyApplication;
import android.view.animation.Animation.AnimationListener;

public class DialogNoAddPlayList {
	
	private Context context;
	private Window window;
	private LayoutInflater layoutInflater;	
	private WindowManager.LayoutParams params_relative;
	public DialogNoAddPlayList(Context context, Window window) {
		params_relative = new WindowManager.LayoutParams();
		params_relative.height = WindowManager.LayoutParams.MATCH_PARENT;
		params_relative.width = WindowManager.LayoutParams.MATCH_PARENT;
		params_relative.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		params_relative.type = WindowManager.LayoutParams.TYPE_TOAST;
		params_relative.format = PixelFormat.TRANSLUCENT;
		this.context = context;
		this.window = window;
		this.flagAutoClose = true;
	}
	
	private boolean flagAutoClose = true;
	public void setAutoClose(boolean flagAutoClose){
		this.flagAutoClose = flagAutoClose;
	}
	
	private OnDialogNoAddPlayListListener listener;
	public interface OnDialogNoAddPlayListListener{
		public void OnYesListener();
		public void OnFinishListener();
	}
	
	public void setOnDialogNoAddPlayListListener(OnDialogNoAddPlayListListener listener){
		this.listener = listener;
	}
	
	private View viewToast = null;
	private Animation animaFaceIn;
	private Animation animaFaceOut;
	private boolean enableClick = false;
	public void showToast(String data){
		if (window == null || viewToast != null) {
			return;
		}
		if(viewToast == null){
			layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewToast = layoutInflater.inflate(R.layout.dialog_noadd_playlist, null);
				//------------------------//
			LinearLayout layout = (LinearLayout)viewToast.findViewById(R.id.LinearLayout);
			
			TextView textView = (TextView)viewToast.findViewById(R.id.textView);
			textView.setText(data);
			ButtonControl button = (ButtonControl)viewToast.findViewById(R.id.butCo);
			button.setTextTitle("OK");
			button.setOnClickListener(new OnClickListener() {
				@Override public void onClick(View arg0) {
					hideToastBox();
					if(listener != null){
						listener.OnYesListener();
					}
				}
			});
			
			if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				layout.setBackgroundResource(R.drawable.icon_boder_popup);
				textView.setTextColor(Color.parseColor("#B4FEFF"));
			}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
				layout.setBackgroundResource(R.drawable.zlight_boder_popup);
				textView.setTextColor(Color.parseColor("#005249"));
			}
			
				//------------------------//
			window.addContentView(viewToast, params_relative);
			animaFaceIn = AnimationUtils.loadAnimation(context,R.anim.fade_in); 
			animaFaceOut = AnimationUtils.loadAnimation(context,R.anim.fade_out); 
			animaFaceIn.setAnimationListener(new AnimationListener() {
				@Override public void onAnimationStart(Animation animation) {}
				@Override public void onAnimationRepeat(Animation animation) {}
				@Override public void onAnimationEnd(Animation animation) {
					enableClick = true;
					if(flagAutoClose){
						autoHideToastBox();	
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
			viewToast.setOnClickListener(new OnClickListener() {
				@Override public void onClick(View arg0) {
					hideToastBox();
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
	
	private Timer timerAutoHide = null;
	private void autoHideToastBox(){
		if(timerAutoHide != null){
			timerAutoHide.cancel();
			timerAutoHide = null;
		}
		timerAutoHide = new Timer();
		timerAutoHide.schedule(new TimerTask() {
			
			@Override
			public void run() {
				handlerAutoHide.sendEmptyMessage(0);
			}
		}, 3000);
	}
	
	private Handler handlerAutoHide = new Handler(){
		public void handleMessage(android.os.Message msg) {
			hideToastBox();
		};
	};

	public void stopAutoHideToastBox(){
		if(timerAutoHide != null){
			timerAutoHide.cancel();
			timerAutoHide = null;
		}		
	}
}
