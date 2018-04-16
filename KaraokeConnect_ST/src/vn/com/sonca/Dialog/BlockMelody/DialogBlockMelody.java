package vn.com.sonca.Dialog.BlockMelody;

import android.content.Context;
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
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.Dialog.BlockMelody.MelodyDragView.OnMelodyDragViewListener;
import vn.com.sonca.Dialog.BlockVolumn.VolumnClickView.OnVolumnClickViewListener;
import vn.com.sonca.Dialog.BlockVolumn.VolumnDragView.OnVolumnDragViewListener;
import vn.com.sonca.Touch.BlockCommand.ButtonPercent;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.params.ServerStatus;
import vn.com.sonca.zktv.main.KTVMainActivity;
import vn.com.sonca.zzzzz.MyApplication;

public class DialogBlockMelody {
	
	private String TAB = "DialogBlockVolumn";
	private Context context;
	private Window window;
	private TouchMainActivity activity;
	private FragmentActivity tempActivity;
	private LayoutInflater layoutInflater;	
	private WindowManager.LayoutParams params_relative;
	public DialogBlockMelody(Context context, Window window, TouchMainActivity activity) {
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
	
	public DialogBlockMelody(Context context, Window window, FragmentActivity activity) {
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
	
	private OnDialogBlockMelodyListener listener;
	public interface OnDialogBlockMelodyListener{
		public void OnFinishListener();
		public void OnMelodyDefault(int value);
	}
	
	public void setOnDialogBlockMelodyListener(OnDialogBlockMelodyListener listener){
		this.listener = listener;
	}
	
	private View viewToast = null;
	private Animation animaFaceIn;
	private Animation animaFaceOut;
	private boolean enableClick = false;
	private MelodyDragView melodyDragView;
	
	public void showToast(){		
		if (window == null || viewToast != null) {
			return;
		}
		if(viewToast == null){
			layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewToast = layoutInflater.inflate(R.layout.dialog_block_melody,null);
			viewToast.findViewById(R.id.Layout).setOnClickListener(new OnClickListener() {
				@Override public void onClick(View arg0) {
					// hideToastBox();
				}
			});
			
			melodyDragView = (MelodyDragView)viewToast.findViewById(R.id.melodyDragView);
			melodyDragView.setOnMelodyDragViewListener(new OnMelodyDragViewListener() {
				
				@Override
				public void OnMelodyDrag(int value) {
					if(listener != null){
						listener.OnMelodyDefault(value);
					}
				}
			});
			
			if(TouchMainActivity.serverStatus != null){
				melodyDragView.setValueMelody(TouchMainActivity.serverStatus.getVolset_melody());
			}
			
			if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				if(KTVMainActivity.serverStatus != null){
					melodyDragView.setValueMelody(KTVMainActivity.serverStatus.getVolset_melody());
				}
			}
			
			ButtonPercent button = (ButtonPercent)viewToast.findViewById(R.id.buttonCancel);
			button.setOnClickListener(new OnClickListener() {
				@Override public void onClick(View arg0) {
					hideToastBox();
				}
			});
			
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

	
	public void syncMelodyFromServer(ServerStatus status){
		if(status != null && melodyDragView != null){
			melodyDragView.setValueMelody(status.getVolset_melody());
		}
	}

}
