package vn.com.sonca.Touch.BlockCommand;

import java.util.Timer;
import java.util.TimerTask;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.zzzzz.MyApplication;
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
import android.view.animation.Animation.AnimationListener;

public class DialogConfirm {
	
	private Context context;
	private Window window;
	private LayoutInflater layoutInflater;	
	private WindowManager.LayoutParams params_relative;
	public DialogConfirm(Context context, Window window) {
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
	
	private OnDialogConfirmListener listener;
	public interface OnDialogConfirmListener{
		public void OnYesListener();
		public void OnNoListener();
		public void OnFinishListener();
	}
	
	public void setOnDialogConfirmListener(OnDialogConfirmListener listener){
		this.listener = listener;
	}
	
	private boolean flagAutoClose = true;
	public void setAutoClose(boolean flagAutoClose){
		this.flagAutoClose = flagAutoClose;
	}
	
	private View viewToast = null;
	private Animation animaFaceIn;
	private Animation animaFaceOut;
	private boolean enableClick = false;
	private TextView textView1;
	private TextView textView2;
	
	public void showToast(String data1, String data2){
		if (window == null || viewToast != null) {
			return;
		}
		if(viewToast == null){
			layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewToast = layoutInflater.inflate(R.layout.dialog_confirm, null);
				//------------------------//
			LinearLayout layout = (LinearLayout)viewToast.findViewById(R.id.LinearLayout);
			textView1 = (TextView)viewToast.findViewById(R.id.textView1);
			textView1.setText(data1);
			textView2 = (TextView)viewToast.findViewById(R.id.textView2);
			textView2.setText(data2);
			
			if(data2.equals("")){
				textView2.setVisibility(View.GONE);
			}
			
			ButtonControl button = (ButtonControl)viewToast.findViewById(R.id.butCo);
			button.setTextTitle(context.getString(R.string.confirm_b));
			button.setOnClickListener(new OnClickListener() {
				@Override public void onClick(View arg0) {
					hideToastBox();
					if(listener != null){
						listener.OnNoListener();
					}
				}
			});
			
			ButtonControl buttonKhong = (ButtonControl)viewToast.findViewById(R.id.butKhong);
			buttonKhong.setTextTitle(context.getString(R.string.confirm_a));
			buttonKhong.setOnClickListener(new OnClickListener() {
				@Override public void onClick(View arg0) {
					hideToastBox();
					if(listener != null){
						listener.OnYesListener();
					}
				}
			});
			
			if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				layout.setBackgroundResource(R.drawable.icon_boder_popup);
				textView1.setTextColor(Color.parseColor("#B4FEFF"));
				textView2.setTextColor(Color.GREEN);
			}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
				layout.setBackgroundResource(R.drawable.zlight_boder_popup);
				textView1.setTextColor(Color.parseColor("#005249"));
				textView2.setTextColor(Color.parseColor("#7B1FA2"));
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
//						autoHideToastBox();	
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
	
	public void stopAutoHideToastBox(){
		if(timerAutoHide != null){
			timerAutoHide.cancel();
			timerAutoHide = null;
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

	public void updateStringData(String data1, String data2){
		if(textView1 != null){
			textView1.setText(data1);	
		}	
		
		if(textView2 != null){
			textView2.setText(data2);	
		}	
		
		if(data2.equals("")){
			textView2.setVisibility(View.GONE);
		}
	}
}
