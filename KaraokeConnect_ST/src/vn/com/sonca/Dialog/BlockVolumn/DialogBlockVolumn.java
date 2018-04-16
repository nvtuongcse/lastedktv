package vn.com.sonca.Dialog.BlockVolumn;

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
import vn.com.sonca.Dialog.BlockVolumn.VolumnClickView.OnVolumnClickViewListener;
import vn.com.sonca.Dialog.BlockVolumn.VolumnDragView.OnVolumnDragViewListener;
import vn.com.sonca.Touch.BlockCommand.ButtonPercent;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.params.ServerStatus;
import vn.com.sonca.zktv.main.KTVMainActivity;
import vn.com.sonca.zzzzz.MyApplication;

public class DialogBlockVolumn {
	
	private String TAB = "DialogBlockVolumn";
	private Context context;
	private Window window;
	private TouchMainActivity activity;
	private FragmentActivity tempActivity;
	private LayoutInflater layoutInflater;	
	private WindowManager.LayoutParams params_relative;
	public DialogBlockVolumn(Context context, Window window, TouchMainActivity activity) {
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
	
	public DialogBlockVolumn(Context context, Window window, FragmentActivity activity) {
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
	
	private OnDialogBlockVolumnListener listener;
	public interface OnDialogBlockVolumnListener{
		public void OnFinishListener();
		public void OnVolumnMIDI(int value);
		public void OnVolumnKTV(int value);
		public void OnVolumnMASTER(int value);
		public void OnVolumnDANCE(int value);
		public void OnVolumnPIANO(int value);
	}
	
	public void setOnDialogBlockVolumnListener(OnDialogBlockVolumnListener listener){
		this.listener = listener;
	}
	
	private View viewToast = null;
	private Animation animaFaceIn;
	private Animation animaFaceOut;
	private boolean enableClick = false;
	
	private VolumnClickView volumnMIDI;
	private VolumnClickView volumnKTV;
	private VolumnDragView volumnMASTER;
	private VolumnDragView volumnDANCE;
	private VolumnDragView volumnPIANO;
	
	public void showToast(){		
		if (window == null || viewToast != null) {
			return;
		}
		if(viewToast == null){
			layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewToast = layoutInflater.inflate(R.layout.dialog_block_volumn,null);
			viewToast.findViewById(R.id.Layout).setOnClickListener(new OnClickListener() {
				@Override public void onClick(View arg0) {
					// hideToastBox();
				}
			});
			
			volumnMIDI = (VolumnClickView)viewToast.findViewById(R.id.volumnMIDI);
			volumnKTV = (VolumnClickView)viewToast.findViewById(R.id.volumnKTV);
			volumnMASTER = (VolumnDragView)viewToast.findViewById(R.id.volumnMASTER);
			volumnDANCE = (VolumnDragView)viewToast.findViewById(R.id.volumnDANCE);
			volumnPIANO = (VolumnDragView)viewToast.findViewById(R.id.volumnPIANO);
			
			if(TouchMainActivity.serverStatus != null){
				volumnMASTER.setValueVolumn(TouchMainActivity.serverStatus.getVolset_default());
				volumnDANCE.setValueVolumn(TouchMainActivity.serverStatus.getVolset_dance());
				volumnPIANO.setValueVolumn(TouchMainActivity.serverStatus.getVolset_piano());
				
				volumnMIDI.setMasterVolumn(TouchMainActivity.serverStatus.getVolset_default());
				volumnKTV.setMasterVolumn(TouchMainActivity.serverStatus.getVolset_default());
				
				volumnMIDI.setValueVolumn(TouchMainActivity.serverStatus.getVolset_offsetMidi());
				volumnKTV.setValueVolumn(TouchMainActivity.serverStatus.getVolset_offsetKTV());
			}			
			
			if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				if(KTVMainActivity.serverStatus != null){
					volumnMASTER.setValueVolumn(KTVMainActivity.serverStatus.getVolset_default());
					volumnDANCE.setValueVolumn(KTVMainActivity.serverStatus.getVolset_dance());
					volumnPIANO.setValueVolumn(KTVMainActivity.serverStatus.getVolset_piano());
					
					volumnMIDI.setMasterVolumn(KTVMainActivity.serverStatus.getVolset_default());
					volumnKTV.setMasterVolumn(KTVMainActivity.serverStatus.getVolset_default());
					
					volumnMIDI.setValueVolumn(KTVMainActivity.serverStatus.getVolset_offsetMidi());
					volumnKTV.setValueVolumn(KTVMainActivity.serverStatus.getVolset_offsetKTV());
				}		
			}
			
			volumnMIDI.setOnVolumnClickViewListener(new OnVolumnClickViewListener() {
				@Override public void OnVolumnClick(int value) {
					if(listener != null){
						listener.OnVolumnMIDI(value);
					}
				}
			});
			volumnKTV.setOnVolumnClickViewListener(new OnVolumnClickViewListener() {
				@Override public void OnVolumnClick(int value) {
					if(listener != null){
						listener.OnVolumnKTV(value);
					}
				}
			});
			volumnMASTER.setOnVolumnDragViewListener(new OnVolumnDragViewListener() {
				@Override public void OnVolumnDrag(int value) {
					if(volumnMIDI != null && volumnKTV != null){
						volumnMIDI.setMasterVolumn(value);
						volumnKTV.setMasterVolumn(value);
					}
					if(listener != null){
						listener.OnVolumnMASTER(value);
					}
				}
			});
			volumnDANCE.setOnVolumnDragViewListener(new OnVolumnDragViewListener() {
				@Override public void OnVolumnDrag(int value) {
					if(listener != null){
						listener.OnVolumnDANCE(value);
					}
				}
			});
			volumnPIANO.setOnVolumnDragViewListener(new OnVolumnDragViewListener() {
				@Override public void OnVolumnDrag(int value) {
					if(listener != null){
						listener.OnVolumnPIANO(value);
					}
				}
			});
			
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

	
	public void syncVolumnFromServer(ServerStatus status){
		if(status != null && volumnMASTER != null){
			volumnMASTER.setValueVolumn(status.getVolset_default());
			volumnDANCE.setValueVolumn(status.getVolset_dance());
			volumnPIANO.setValueVolumn(status.getVolset_piano());
			
			volumnMIDI.setMasterVolumn(status.getVolset_default());
			volumnKTV.setMasterVolumn(status.getVolset_default());
			
			volumnMIDI.setValueVolumn(status.getVolset_offsetMidi());
			volumnKTV.setValueVolumn(status.getVolset_offsetKTV());
		}		
	}

}
