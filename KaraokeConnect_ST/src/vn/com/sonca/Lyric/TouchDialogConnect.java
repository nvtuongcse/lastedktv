package vn.com.sonca.Lyric;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.CustomView.TouchPopupConnectView;
import vn.com.sonca.Touch.CustomView.TouchPopupConnectView.OnPopupConnectViewListener;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewManager;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;

public class TouchDialogConnect {
	private String TAB = "TouchDialogConnect";
	private Context context;
	private Window window;
	private WindowManager.LayoutParams params_relative;
	public TouchDialogConnect(Context context, Window window) {
		params_relative = new WindowManager.LayoutParams();
		/*window.setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
		         WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);*/
		params_relative.height = WindowManager.LayoutParams.MATCH_PARENT;
		params_relative.width = WindowManager.LayoutParams.MATCH_PARENT;
		params_relative.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		params_relative.type = WindowManager.LayoutParams.TYPE_TOAST;
		params_relative.format = PixelFormat.TRANSLUCENT;
		this.context = context;
		this.window = window;
		enableClick = false;
	}
	
	private OnDialogConnectListener listener;
	public interface OnDialogConnectListener{
		public void OnYesListener();
		public void OnFinishListener();
	}
	
	public void setOnDialogConnectListener(OnDialogConnectListener listener){
		this.listener = listener;
	}
	
	private View viewToast = null;
	private Animation animaFaceIn;
	private Animation animaFaceOut;
	private boolean enableClick = false;
	
	private TouchPopupConnectView connectView;
	
	public void showToast(){
		if (window == null || viewToast != null) {
			return;
		}
		if(viewToast == null){
			LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewToast = layoutInflater.inflate(R.layout.touch_dialog_connect,null);
			connectView = (TouchPopupConnectView)viewToast.findViewById(R.id.PopupConnectView);
			if(msg1.equals("") && msg2.equals("")){
				connectView.setPopupLayout(TouchPopupConnectView.LAYOUT_ASK);	
			} else {
				connectView.setPopupLayout(TouchPopupConnectView.LAYOUT_ASK_MSG);
				connectView.setDialogMessage(msg1, msg2);
			}	
			connectView.setOnPopupConnectViewListener(new OnPopupConnectViewListener() {
				@Override public void OnYesListener() {
					if(listener != null){
						listener.OnYesListener();
					}
					hideToastBox();
				}
				@Override public void OnNoListener() {
					hideToastBox();
				}
			});			
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
			MyLog.e("TAB", "showToast() - 3");
			viewToast.setOnClickListener(new OnClickListener() {
				@Override public void onClick(View arg0) {
					hideToastBox();
				}
			});
			window.addContentView(viewToast, params_relative);
			viewToast.startAnimation(animaFaceIn);
		}
	}
	
	public void hideToastBox(){
		if (enableClick && viewToast != null && window != null) {
			viewToast.startAnimation(animaFaceOut);
		}
	}
	
	public void hideToast(){
		if(viewToast != null && window != null){
			((ViewManager)viewToast.getParent()).removeView(viewToast);
			viewToast = null;
			if(listener != null){
				listener.OnFinishListener();
			}
		}
	}

	private String msg1 = "";
	private String msg2 = "";
	
	public void setDialogMessage(String msg1, String msg2){
		this.msg1 = msg1;
		this.msg2 = msg2;
	}
	
}
