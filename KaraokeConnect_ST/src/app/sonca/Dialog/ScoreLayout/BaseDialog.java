package app.sonca.Dialog.ScoreLayout;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewManager;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import vn.com.sonca.MyLog.MyLog;

public abstract class BaseDialog {
	private final String TAB = "BaseDialog";	
	private Context context;
	private Window window;
	private LayoutInflater layoutInflater;	
	private WindowManager.LayoutParams params_relative;
	
	public BaseDialog(Context context, Window window) {
		int h = context.getResources().getDisplayMetrics().heightPixels;
		params_relative = new WindowManager.LayoutParams();
		params_relative.height = WindowManager.LayoutParams.MATCH_PARENT;
		params_relative.width = WindowManager.LayoutParams.MATCH_PARENT;
		params_relative.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		params_relative.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;
		params_relative.type = WindowManager.LayoutParams.TYPE_TOAST;
		params_relative.format = PixelFormat.TRANSLUCENT;		
		this.context = context;
		this.window = window;
	}
	
	private OnBaseDialogListener listener;
	public interface OnBaseDialogListener {
		public void OnShowDialog();
		public void OnFinishDialog();
	}
	
	public void setOnBaseDialogListener(OnBaseDialogListener listener){
		this.listener = listener;
	}
	
	protected abstract int getIdLayout();
	protected abstract int getTimerShow();
	protected abstract View getView(View contentView);
	protected abstract void OnShow();
	protected abstract void OnDismiss();
	protected abstract void OnReceiveDpad(KeyEvent event, int key);
	
	private View viewToast = null;
	private Animation animaFaceIn;
	private Animation animaFaceOut;
	private boolean enableClick = false;
	public void showDialog(){
		MyLog.d(TAB, "==showDialog=");
		if (window == null || viewToast != null) {
			return;
		}
		if(viewToast == null){
			layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);      
			viewToast = layoutInflater.inflate(getIdLayout() , null);
			viewToast.setOnClickListener(new OnClickListener() {
				@Override public void onClick(View arg0) {}
			});
			/*
			viewToast.setBackgroundColor(Color.BLACK);
			viewToast.setAlpha(0.8f);
			*/
			getView(viewToast);
			window.addContentView(viewToast, params_relative);
			animaFaceIn = AnimationUtils.loadAnimation(context, android.R.anim.fade_in); 
			animaFaceOut = AnimationUtils.loadAnimation(context, android.R.anim.fade_out); 
			animaFaceIn.setAnimationListener(new AnimationListener() {
				@Override public void onAnimationStart(Animation animation) {}
				@Override public void onAnimationRepeat(Animation animation) {}
				@Override public void onAnimationEnd(Animation animation) {
					OnShow();
					startTimerHide();
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
			if(listener != null){
				MyLog.d(TAB, "==showDialog1=");
				listener.OnShowDialog();
			}
		}
	}
	
	public void dismissDialog(){
		if (enableClick && viewToast != null && window != null) {
			viewToast.startAnimation(animaFaceOut);
		}
	}
	
	public void OnReceiveDpadFromMain(KeyEvent event, int key){
		OnReceiveDpad(event, key);
	}
	
	private void hideToast(){
		OnDismiss();
		if(listener != null){
			listener.OnFinishDialog();
		}
		if(viewToast != null && window != null){
			((ViewManager)viewToast.getParent()).removeView(viewToast);
			viewToast = null;
			animaFaceIn = null;
			animaFaceOut = null;
		}
	}
	
	public void dismissDialog(boolean animated)
	{
		if(animated) {
			dismissDialog(); 
		}else {
			hideToast(); 
		}
	}
	
	private Timer timerHide = null;
	public void startTimerHide(){
		cancelTimerHide();
		if(getTimerShow() > 0){
			timerHide = new Timer();
			timerHide.schedule(new TimerTask() {
				@Override public void run() {
					handlerHide.sendEmptyMessage(0);
				}
			}, getTimerShow());
		}
	}
	
	private void cancelTimerHide(){
		if(timerHide != null){
			timerHide.cancel();
			timerHide = null;
		}
	}
	
	private Handler handlerHide = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 0){
				dismissDialog();
			}
		};
	};

}
