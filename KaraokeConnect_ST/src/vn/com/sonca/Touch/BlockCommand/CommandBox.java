package vn.com.sonca.Touch.BlockCommand;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewManager;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.LinearLayout;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.Dialog.BlockMelody.DialogBlockMelody;
import vn.com.sonca.Dialog.BlockMelody.DialogBlockMelody.OnDialogBlockMelodyListener;
import vn.com.sonca.Dialog.BlockVolumn.DialogBlockVolumn;
import vn.com.sonca.Dialog.BlockVolumn.DialogBlockVolumn.OnDialogBlockVolumnListener;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.BlockCommand.ButonOnOff.OnOnOffCommandListener;
import vn.com.sonca.Touch.BlockCommand.ButtonMelodyOnOff.OnMelodyOnOffCommandListener;
import vn.com.sonca.Touch.BlockCommand.ButtonScoreOnOff.OnScoreOnOffCommandListener;
import vn.com.sonca.Touch.BlockCommand.ButtonVolumnOnOff.OnVolumnOnOffCommandListener;
import vn.com.sonca.Touch.BlockCommand.CommandBack.OnBackLyric;
import vn.com.sonca.Touch.BlockCommand.DialogCancelPhong.OnCancelPhongListener;
import vn.com.sonca.Touch.BlockCommand.DialogControl.OnDialogControlListener;
import vn.com.sonca.Touch.BlockCommand.DialogScorePercent.OnDialogScorePercentListener;
import vn.com.sonca.Touch.BlockCommand.VolumnMasterView.OnVolumnListener;
import vn.com.sonca.Touch.CustomView.TouchDanceView;
import vn.com.sonca.Touch.CustomView.TouchKeyView;
import vn.com.sonca.Touch.CustomView.TouchMelodyView;
import vn.com.sonca.Touch.CustomView.TouchNextView;
import vn.com.sonca.Touch.CustomView.TouchPauseView;
import vn.com.sonca.Touch.CustomView.TouchRepeatView;
import vn.com.sonca.Touch.CustomView.TouchScoreView;
import vn.com.sonca.Touch.CustomView.TouchScoreView.OnScoreListener;
import vn.com.sonca.Touch.CustomView.TouchSingerView;
import vn.com.sonca.Touch.CustomView.TouchTempoView;
import vn.com.sonca.Touch.CustomView.TouchToneView;
import vn.com.sonca.Touch.CustomView.TouchVolumnView;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.params.SKServer;
import vn.com.sonca.params.ServerStatus;
import vn.com.sonca.zktv.main.KTVMainActivity;
import vn.com.sonca.zzzzz.MyApplication;

public class CommandBox implements OnOnOffCommandListener{
	
private String TAB = "CommandBox";
	
	private Context context;
	private Window window;
	private LayoutInflater layoutInflater;	
	private WindowManager.LayoutParams params_relative;
	
	private TouchMainActivity activity;
	private KTVMainActivity ktvActivity;
	
	public CommandBox(Context context, Window window, TouchMainActivity activity) {
		params_relative = new WindowManager.LayoutParams();
		params_relative.height = WindowManager.LayoutParams.MATCH_PARENT;
		params_relative.width = WindowManager.LayoutParams.MATCH_PARENT;
		params_relative.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		params_relative.type = WindowManager.LayoutParams.TYPE_TOAST;
		params_relative.format = PixelFormat.TRANSLUCENT;
		this.activity = activity;
		this.context = context;
		this.window = window;

		this.activity.isProcessingSomething = true;
	}
	
	public CommandBox(Context context, Window window, KTVMainActivity activity) {
		params_relative = new WindowManager.LayoutParams();
		params_relative.height = WindowManager.LayoutParams.MATCH_PARENT;
		params_relative.width = WindowManager.LayoutParams.MATCH_PARENT;
		params_relative.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		params_relative.type = WindowManager.LayoutParams.TYPE_TOAST;
		params_relative.format = PixelFormat.TRANSLUCENT;
		this.ktvActivity = activity;
		this.context = context;
		this.window = window;

		this.ktvActivity.isProcessingSomething = true;
	}
	
	private OnCommandBoxListener listener;
	public interface OnCommandBoxListener{
		public void OnScorePercentListener(int percent);
		public void OnFinishListener();
		public void OnCommandEnable(int id);
		public void OnSendScore(int isScoreOn);
		public void OnSendVolumn(int value);
		public void OnSendMute(boolean isMute);
		
		public void OnVolumnMIDI(int value);
		public void OnVolumnKTV(int value);
		public void OnVolumnMASTER(int value);
		public void OnVolumnDANCE(int value);
		public void OnVolumnPIANO(int value);
		
		public void OnMelodyDefault(int value);
	}
	
	public void setOnDialogConnectListener(OnCommandBoxListener listener){
		this.listener = listener;
	}
	
	private FragmentActivity getMyActivity(){
		if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			return this.ktvActivity;
		} else {
			return this.activity;
		}
	}
	
	private View viewToast = null;
	private Animation animaFaceIn;
	private Animation animaFaceOut;
	private ButtonOk buttonOk;
	private boolean enableClick = false;
	private CommandBack commandBack;
	private TouchScoreView scoreView;
	private VolumnMasterView volumnMasterView;
	
	public void showToast(){		
		if (window == null || viewToast != null) {
			return;
		}
		if(viewToast == null){
			layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewToast = layoutInflater.inflate(R.layout.dialog_command,null);
			
			View viewLine1 = (View)viewToast.findViewById(R.id.viewLine1);
			View viewLine2 = (View)viewToast.findViewById(R.id.viewLine2);
			LinearLayout layoutLeft = (LinearLayout)viewToast.findViewById(R.id.layoutLeft);
			LinearLayout layoutCenter = (LinearLayout)viewToast.findViewById(R.id.layoutCenter);
			LinearLayout layoutRight = (LinearLayout)viewToast.findViewById(R.id.layoutRight);
			LinearLayout layoutBottom = (LinearLayout)viewToast.findViewById(R.id.layoutBottom);
			LinearLayout layoutScreen = (LinearLayout)viewToast.findViewById(R.id.LinearLayout);
			layoutScreen.setOnClickListener(new OnClickListener() {
				@Override public void onClick(View arg0) {}
			});
			
			commandBack = (CommandBack)viewToast.findViewById(R.id.CommandBack);
			SKServer skServer = ((MyApplication)getMyActivity().getApplication()).getDeviceCurrent();
			if(skServer != null){
				commandBack.setNameDevice(skServer.getName());
			}
			commandBack.setOnBackLyric(new OnBackLyric() {
				@Override public void OnBack() {
					hideToastBox();
				}

				@Override
				public void OnPhongHatClick() {
					ShowDialogControl(DialogControl.PHONG_HAT);
				}

				@Override
				public void OnMacDinhClick() {
					ShowDialogControl(DialogControl.MAC_DINH);
				}
			});
			buttonOk = (ButtonOk)viewToast.findViewById(R.id.ButtonOk);
			buttonOk.setOnClickListener(new OnClickListener() {
				@Override public void onClick(View arg0) {
					hideToastBox();
				}
			});
			
			DanceCommand = (ButonOnOff) viewToast.findViewById(R.id.DanceCommand);
			UserCommand = (ButonOnOff) viewToast.findViewById(R.id.UserCommand);
			VolumeCommand = (ButtonVolumnOnOff) viewToast.findViewById(R.id.VolumeCommand);
			NextCommand = (ButonOnOff) viewToast.findViewById(R.id.NextCommand);

			MelodyCommand = (ButtonMelodyOnOff) viewToast.findViewById(R.id.MelodyCommand);
			TempoCommand = (ButonOnOff) viewToast.findViewById(R.id.TempoCommand);
			KeyCommand = (ButonOnOff) viewToast.findViewById(R.id.KeyCommand);
			PauseCommand = (ButonOnOff) viewToast.findViewById(R.id.PauseCommand);

			ScoreCommand = (ButtonScoreOnOff) viewToast.findViewById(R.id.ScoreCommand);
			ToneCommand = (ButonOnOff) viewToast.findViewById(R.id.ToneCommand);
			SingerCommand = (ButonOnOff) viewToast.findViewById(R.id.SingerCommand);
			RepeatCommand = (ButonOnOff) viewToast.findViewById(R.id.RepeatCommand);
			volumnMasterView = (VolumnMasterView)viewToast.findViewById(R.id.VolumnMasterView);
			volumnMasterView.setOnVolumnListener(new OnVolumnListener() {
				
				@Override
				public void onVolumn(int value) {
					if(listener != null){
						listener.OnSendVolumn(value);
					}
				} 
				
				@Override
				public void onMute(boolean isMute) {
					if(listener != null){
						listener.OnSendMute(isMute);
					}
				}
				
				@Override
				public void OnInActive() {
					// TODO Auto-generated method stub
					
				}
			});
			scoreView = (TouchScoreView)viewToast.findViewById(R.id.ScoreView);
			if(TouchMainActivity.serverStatus != null){
				scoreView.setScoreView(TouchMainActivity.serverStatus.getCurrentScore());
				ScoreCommand.setStateScode(TouchMainActivity.serverStatus.getCurrentScore());
			}
			scoreView.setOnScoreListener(new OnScoreListener() {
				
				@Override
				public void onScore(int isScoreOn) {
					if(listener != null){
						listener.OnSendScore(isScoreOn);
						ScoreCommand.setStateScode(isScoreOn);
					}
				}
				
				@Override
				public void OnInActive() {
					// TODO Auto-generated method stub
					
				}
			});
			
			//-------------------//
			
			DanceCommand.setOnOnOffCommandListener(this);
			UserCommand.setOnOnOffCommandListener(this);
			if(TouchMainActivity.serverStatus != null){
				if(MyApplication.intSvrCode >= 25){
					int playingMode = TouchMainActivity.serverStatus.getPlayingMode();
					
					if(playingMode == 0){ // KARAOKE
						volumnMasterView.setVolumn(TouchMainActivity.serverStatus.getVolset_default());
					} else if(playingMode == 1){ // DANCE
						volumnMasterView.setVolumn(TouchMainActivity.serverStatus.getVolset_dance());
					} else if(playingMode == 2){ // PIANO
						volumnMasterView.setVolumn(TouchMainActivity.serverStatus.getVolset_default());
					} else {
						volumnMasterView.setVolumn(TouchMainActivity.serverStatus.getCurrentVolume());	
					}				
					volumnMasterView.setMute(TouchMainActivity.serverStatus.isMuted());
					VolumeCommand.setStateScode(TouchMainActivity.serverStatus.getCurrentVolume());	
				} else {
					volumnMasterView.setVolumn(TouchMainActivity.serverStatus.getCurrentVolume());
					volumnMasterView.setMute(TouchMainActivity.serverStatus.isMuted());
					VolumeCommand.setStateScode(TouchMainActivity.serverStatus.getCurrentVolume());	
				}				
				
			}
			VolumeCommand.setOnVolumnOnOffCommandListener(new OnVolumnOnOffCommandListener() {
				
				@Override
				public void OnCommand(int id, boolean bool, boolean isShowDialog) {
					if(isShowDialog == false){
						TouchVolumnView.setCommandEnable(bool);
						volumnMasterView.invalidate();
						if(listener != null){
							listener.OnCommandEnable(id);
						}
					}else{
						ShowDialogCancelPhong(0);	
					}
				}
				
				@Override
				public void OnCommand(int id, int value) {
					TouchVolumnView.setCommandMedium(value);
					volumnMasterView.invalidate();
					if(listener != null){
						listener.OnCommandEnable(id);
					}
				}
				
				@Override
				public void OnShowVolunmD() {
					if(MyApplication.intSvrCode>= 25){
						ShowDialogVolumn();
					} else {
						ShowDialogCancelPhong(0);	
					}
				}
			});
			NextCommand.setOnOnOffCommandListener(this);
			
			MelodyCommand.setOnMelodyOnOffCommandListener(new OnMelodyOnOffCommandListener() {
				
				@Override
				public void OnShowMelodyD() {
					if(MyApplication.intSvrCode>= 25){
						ShowDialogMelody();
					} else {
						ShowDialogCancelPhong(0);	
					}

				}
				
				@Override
				public void OnCommand(int id, boolean bool, boolean isShowDialog) {
					if(isShowDialog == false){
						TouchMelodyView.setCommandEnable(bool);
						if(listener != null){
							listener.OnCommandEnable(id);
						}
					}else{
						ShowDialogCancelPhong(0);	
					}
				}
				
				@Override
				public void OnCommand(int id, int value) {
					TouchMelodyView.setCommandMedium(value);
					if(listener != null){
						listener.OnCommandEnable(id);
					}
				}
			});

			TempoCommand.setOnOnOffCommandListener(this);
			KeyCommand.setOnOnOffCommandListener(this);
			PauseCommand.setOnOnOffCommandListener(this);
			
			ToneCommand.setOnOnOffCommandListener(this);
			SingerCommand.setOnOnOffCommandListener(this);
			RepeatCommand.setOnOnOffCommandListener(this);
			
			ScoreCommand.setOnScoreOffCommandListener(new OnScoreOnOffCommandListener() {
				
				@Override
				public void OnCommand(int id, boolean bool, boolean isShowDialog) {
					TouchScoreView.setCommandEnable(bool);
					scoreView.invalidate();
					if(buttonOk != null){
						buttonOk.setActiveButtonOk(true);
					}
					if(listener != null){
						listener.OnCommandEnable(id);
					}
				}
				
				@Override
				public void OnCommand(int id, int value) {
					TouchScoreView.setCommandMedium(value);
					scoreView.invalidate();
					if(buttonOk != null){
						buttonOk.setActiveButtonOk(true);
					}
					if(listener != null){
						listener.OnCommandEnable(id);
					}
				}

				@Override
				public void OnShowPercent() {
					ShowDialogScorePercent();
				}

				@Override
				public void OnShowUpdateFirmware() {
					ShowDialogCancelPhong(0);
				}
			});
			
			int r01 = 0, r02 = 0, r03 = 0, r04 = 0, r05 = 0;
			int r06 = 0, r07 = 0, r08 = 0, r09 = 0, r10 = 0;
			int r11 = 0, r12 = 0, r13 = 0, r14 = 0, r15 = 0;
			int r16 = 0, r17 = 0, r18 = 0, r19 = 0, r20 = 0;
			int r21 = 0, r22 = 0;
			// BACKVIEW
			if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				r01 = R.drawable.touch_1st_khoa;
				r02 = R.drawable.touch_1st_ios;
				r03 = R.drawable.touch_dance_active_block;
				r04 = R.drawable.touch_image_dance_xam_block;
				r05 = R.drawable.volume_active;
				r06 = R.drawable.volume_khoa;
				r07 = R.drawable.touch_next;
				r08 = R.drawable.touch_next_inactive;
				r09 = R.drawable.touch_icon_melody;
				r10 = R.drawable.touch_icon_melody_xam_tablet;
				r11 = R.drawable.touch_icon_tempo;
				r12 = R.drawable.touch_icon_tempo_xam_tablet;
				r13 = R.drawable.touch_key;
				r14 = R.drawable.touch_key_xam;
				r15 = R.drawable.touch_pause;
				r16 = R.drawable.touch_pause_inactive;
				r17 = R.drawable.touch_tone;
				r18 = R.drawable.touch_tone_xam;
				r19 = R.drawable.touch_vocal;
				r20 = R.drawable.touch_vocal_xam;
				r21 = R.drawable.touch_repeat;
				r22 = R.drawable.touch_repeat_inactive;
					//-------------//
				layoutScreen.setBackgroundResource(R.drawable.mainbg);
				layoutLeft.setBackgroundResource(R.drawable.background_khoadieukhien);
				layoutRight.setBackgroundResource(R.drawable.background_khoadieukhien);
				layoutCenter.setBackgroundResource(R.drawable.background_khoadieukhien);
				layoutBottom.setBackgroundResource(R.drawable.background_khoadieukhien);
				viewLine1.setBackgroundResource(R.drawable.touch_shape_line_ver);
				viewLine2.setBackgroundResource(R.drawable.touch_shape_line_ver);
			}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
				r01 = R.drawable.zlight_touch_1st_khoa;
				r02 = R.drawable.zlight_touch_1st_xam;
				r03 = R.drawable.zlight_dance_active;
				r04 = R.drawable.zlight_dance_xam;
				r05 = R.drawable.zlight_volume_active;
				r06 = R.drawable.zlight_volume_xam;
				r07 = R.drawable.zlight_next_active;
				r08 = R.drawable.zlight_next_inactive;
				r09 = R.drawable.zlight_icon_melody;
				r10 = R.drawable.zlight_icon_melody_xam;
				r11 = R.drawable.zlight_icon_tempo_active;
				r12 = R.drawable.zlight_icon_tempo_xam;
				r13 = R.drawable.zlight_key_active;
				r14 = R.drawable.zlight_key_xam;
				r15 = R.drawable.zlight_pause_active;
				r16 = R.drawable.zlight_pause_inactive;
				r17 = R.drawable.zlight_tone_active;
				r18 = R.drawable.zlight_tone_xam;
				r19 = R.drawable.zlight_singer_active;
				r20 = R.drawable.zlight_singer_xam;
				r21 = R.drawable.zlight_repeat_active;
				r22 = R.drawable.zlight_repeat_inactive;
					//-------------//
				layoutScreen.setBackgroundColor(Color.parseColor("#C1FFE8"));
				layoutLeft.setBackgroundColor(Color.parseColor("#C1FFE8"));
				layoutRight.setBackgroundColor(Color.parseColor("#C1FFE8"));
				layoutBottom.setBackgroundColor(Color.parseColor("#C1FFE8"));
				layoutCenter.setBackgroundColor(Color.parseColor("#C1FFE8"));
				viewLine1.setBackgroundResource(R.drawable.zlight_shape_line_ver);
				viewLine2.setBackgroundResource(R.drawable.zlight_shape_line_ver);
			}
			
			//-------------------//
			
			if(TouchMainActivity.serverStatus != null || KTVMainActivity.serverStatus != null){
				boolean flagControlFullAPI = false;
				
				if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
					if(KTVMainActivity.serverStatus.isOnOffControlFullAPI()){
						flagControlFullAPI = true;
					}
				} else {
					if(TouchMainActivity.serverStatus.isOnOffControlFullAPI()){
						flagControlFullAPI = true;
					}
				}
				
				if(flagControlFullAPI){
					int bool = 0;
					if (getMyActivity() != null) {
						bool = ((MyApplication) getMyActivity().getApplication()).getCommandMedium();
					}
					VolumeCommand.setState(TouchVolumnView.getCommandMedium());
					ScoreCommand.setState(TouchScoreView.getCommandMedium());
					ScoreCommand.setStatePercent(MyApplication.getCommandMediumScoreMethod());
					UserCommand.setDate(bool, context.getString(
							R.string.command_4a),
							context.getResources().getDrawable(r01),
							context.getResources().getDrawable(r02));
					DanceCommand.setDate(TouchDanceView.getCommandMedium(), 
							"Dance",
							context.getResources().getDrawable(r03),
							context.getResources().getDrawable(r04));
					NextCommand.setDate(TouchNextView.getCommandMedium(), 
							context.getString(R.string.command_4c),
							context.getResources().getDrawable(r07),
							context.getResources().getDrawable(r08));
					MelodyCommand.setDate(TouchMelodyView.getCommandMedium(), 
							context.getString(R.string.command_4d),
							context.getResources().getDrawable(r09),
							context.getResources().getDrawable(r10));
					TempoCommand.setDate(TouchTempoView.getCommandMedium(), 
							context.getString(R.string.command_4e),
							context.getResources().getDrawable(r11),
							context.getResources().getDrawable(r12));
					KeyCommand.setDate(TouchKeyView.getCommandMedium(), 
							context.getString(R.string.command_4f),
							context.getResources().getDrawable(r13),
							context.getResources().getDrawable(r14));
					PauseCommand.setDate(TouchPauseView.getCommandMedium(), 
							context.getString(R.string.command_4g),
							context.getResources().getDrawable(r15),
							context.getResources().getDrawable(r16));
					ToneCommand.setDate(TouchToneView.getCommandMedium(), 
							context.getString(R.string.command_4i),
							context.getResources().getDrawable(r17),
							context.getResources().getDrawable(r18));
					SingerCommand.setDate(TouchSingerView.getCommandMedium(), 
							context.getString(R.string.command_4j),
							context.getResources().getDrawable(r19),
							context.getResources().getDrawable(r20));
					RepeatCommand.setDate(TouchRepeatView.getCommandMedium(), 
							context.getString(R.string.command_4k),
							context.getResources().getDrawable(r21),
							context.getResources().getDrawable(r22));
				} else {
					boolean bool = false;
					if (getMyActivity() != null) {
						bool = ((MyApplication) getMyActivity().getApplication()).getCommandEnable();
					}
					VolumeCommand.setStateBoolean(TouchVolumnView.getCommandEnable());					
					ScoreCommand.setStateBoolean(TouchScoreView.getCommandEnable());
					ScoreCommand.setStatePercent(0);
					DanceCommand.setDate(TouchDanceView.getCommandEnable(), 
							"Dance",
							context.getResources().getDrawable(r03),
							context.getResources().getDrawable(r04));
					UserCommand.setDate(bool, context.getString(
							R.string.command_4a),
							context.getResources().getDrawable(r01),
							context.getResources().getDrawable(r02));
					DanceCommand.setDate(TouchDanceView.getCommandEnable(), 
							"Dance",
							context.getResources().getDrawable(r03),
							context.getResources().getDrawable(r04));
					NextCommand.setDate(TouchNextView.getCommandEnable(), 
							context.getString(R.string.command_4c),
							context.getResources().getDrawable(r07),
							context.getResources().getDrawable(r08));
					MelodyCommand.setDate(TouchMelodyView.getCommandEnable(), 
							context.getString(R.string.command_4d),
							context.getResources().getDrawable(r09),
							context.getResources().getDrawable(r10));
					TempoCommand.setDate(TouchTempoView.getCommandEnable(), 
							context.getString(R.string.command_4e),
							context.getResources().getDrawable(r11),
							context.getResources().getDrawable(r12));
					KeyCommand.setDate(TouchKeyView.getCommandEnable(), 
							context.getString(R.string.command_4f),
							context.getResources().getDrawable(r13),
							context.getResources().getDrawable(r14));
					PauseCommand.setDate(TouchPauseView.getCommandEnable(), 
							context.getString(R.string.command_4g),
							context.getResources().getDrawable(r15),
							context.getResources().getDrawable(r16));
					ToneCommand.setDate(TouchToneView.getCommandEnable(), 
							context.getString(R.string.command_4i),
							context.getResources().getDrawable(r17),
							context.getResources().getDrawable(r18));
					SingerCommand.setDate(TouchSingerView.getCommandEnable(), 
							context.getString(R.string.command_4j),
							context.getResources().getDrawable(r19),
							context.getResources().getDrawable(r20));
					RepeatCommand.setDate(TouchRepeatView.getCommandEnable(), 
							context.getString(R.string.command_4k),
							context.getResources().getDrawable(r21),
							context.getResources().getDrawable(r22));
				}
			}
			
			if(MyApplication.intSvrCode < 17){
				ScoreCommand.setFlagBlock(true);
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
		if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			this.ktvActivity.isProcessingSomething = false;
		} else {
			this.activity.isProcessingSomething = false;	
		}
		
		if (enableClick && viewToast != null && window != null) {
			viewToast.startAnimation(animaFaceOut);
		}
		if(dialogControl != null){
			dialogControl.hideToastBox();
		}
		if(dialogScorePercent != null){
			dialogScorePercent.hideToastBox();
		}
		if(dialogBlockVolumn != null){
			dialogBlockVolumn.hideToastBox();
		}
		if(dialogBlockMelody != null){
			dialogBlockMelody.hideToastBox();
		}
	}
	
	private void hideToast(){
		if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			this.ktvActivity.isProcessingSomething = false;
		} else {
			this.activity.isProcessingSomething = false;	
		}
		
		if(listener != null){
			listener.OnFinishListener();
		}
		if(viewToast != null && window != null){
			((ViewManager)viewToast.getParent()).removeView(viewToast);
			viewToast = null;
		}
	}

	@Override
	public void OnCommand(int id, int bool) {
		MyApplication.freezeTime = System.currentTimeMillis();
		switch (id) {
		case R.id.UserCommand:
			if(getMyActivity() != null){
				((MyApplication)getMyActivity().getApplication()).setCommandMedium(bool);
			}
			break;
		case R.id.VolumeCommand:
			TouchVolumnView.setCommandMedium(bool);
			volumnMasterView.invalidate();
			break;
		case R.id.NextCommand:
			TouchNextView.setCommandMedium(bool);
			break;
		case R.id.MelodyCommand:
			TouchMelodyView.setCommandMedium(bool);
			break;
		case R.id.TempoCommand:
			TouchTempoView.setCommandMedium(bool);
			break;
		case R.id.KeyCommand:
			TouchKeyView.setCommandMedium(bool);
			break;
		case R.id.PauseCommand:
			TouchPauseView.setCommandMedium(bool);
			break;
		case R.id.ScoreCommand:
			TouchScoreView.setCommandMedium(bool);
			break;
		case R.id.ToneCommand:
			TouchToneView.setCommandMedium(bool);
			break;
		case R.id.SingerCommand:
			TouchSingerView.setCommandMedium(bool);
			break;
		case R.id.RepeatCommand:
			TouchRepeatView.setCommandMedium(bool);
			break;
		case R.id.DanceCommand:
			TouchDanceView.setCommandMedium(bool);
			break;
		default: break;
		}
		
		if(buttonOk != null){
			buttonOk.setActiveButtonOk(true);
		}
		if(listener != null){
			listener.OnCommandEnable(id);
		}
		
	}
	
	private ButonOnOff UserCommand;
	private ButtonVolumnOnOff VolumeCommand;
	private ButonOnOff NextCommand;
	private ButonOnOff DanceCommand;
	
	private ButtonMelodyOnOff MelodyCommand;
	private ButonOnOff TempoCommand;
	private ButonOnOff KeyCommand;
	private ButonOnOff PauseCommand;
	
	private ButtonScoreOnOff ScoreCommand;
	private ButonOnOff ToneCommand;
	private ButonOnOff SingerCommand;
	private ButonOnOff RepeatCommand;
	
	public void syncScoreFromServer(ServerStatus status){
		if(dialogBlockVolumn != null){
			dialogBlockVolumn.syncVolumnFromServer(status);
		}
		if(dialogBlockMelody != null){
			dialogBlockMelody.syncMelodyFromServer(status);
		}
		if (status.getCurrentScore() != scoreView.getScoreView()){
			scoreView.setScoreView(status.getCurrentScore());	
		}
		if(status.getCurrentScore() != ScoreCommand.getStateScode()){
			ScoreCommand.setStateScode(status.getCurrentScore());
		}
		
		if(MyApplication.intSvrCode >= 25){
			int playingMode = status.getPlayingMode();	
			if(playingMode == 0){ // KARAOKE
				if(status.getVolset_default() != volumnMasterView.getVolumn()){
					volumnMasterView.setVolumn(status.getVolset_default());
					volumnMasterView.setMute(!status.isMuted());
				}
			} else if(playingMode == 1){ // DANCE
				if(status.getVolset_dance() != volumnMasterView.getVolumn()){
					volumnMasterView.setVolumn(status.getVolset_dance());
					volumnMasterView.setMute(!status.isMuted());
				}
			} else if(playingMode == 2){ // PIANO
				if(status.getVolset_piano() != volumnMasterView.getVolumn()){
					volumnMasterView.setVolumn(status.getVolset_piano());
					volumnMasterView.setMute(!status.isMuted());
				}
			} else {
				
			}		
		} else {
			if (status.getCurrentVolume() != volumnMasterView.getVolumn()) {
				volumnMasterView.setVolumn(status.getCurrentVolume());
				volumnMasterView.setMute(!status.isMuted());
			}
		}
		
		if (status.isMuted() == volumnMasterView.isMute()) {
			volumnMasterView.setMute(!status.isMuted());
		}
	}
	
	public void syncFromServer(){
		MyLog.e(TAB, "syncFromServer");
		commandBack.invalidate();
		if(TouchMainActivity.serverStatus != null || KTVMainActivity.serverStatus != null){
			boolean flagControlFullAPI = false;
			
			if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				if(KTVMainActivity.serverStatus.isOnOffControlFullAPI()){
					flagControlFullAPI = true;
				}
			} else {
				if(TouchMainActivity.serverStatus.isOnOffControlFullAPI()){
					flagControlFullAPI = true;
				}
			}
			
			if(flagControlFullAPI){
				if(getMyActivity() != null){
					int isUser = ((MyApplication)getMyActivity().getApplication()).getCommandMedium();
					if(isUser != UserCommand.getState()){
						UserCommand.setState(isUser);
					}
				}
				if(TouchDanceView.getCommandMedium() != DanceCommand.getState()){
					DanceCommand.setState(TouchDanceView.getCommandMedium());
				}
				if(TouchVolumnView.getCommandMedium() != VolumeCommand.getState()){
					VolumeCommand.setState(TouchVolumnView.getCommandMedium());
					volumnMasterView.invalidate();
				}
				if(TouchNextView.getCommandMedium() != NextCommand.getState()){
					NextCommand.setState(TouchNextView.getCommandMedium());
				}
				if(TouchMelodyView.getCommandMedium() != MelodyCommand.getState()){
					MelodyCommand.setState(TouchMelodyView.getCommandMedium());
				}
				if(TouchTempoView.getCommandMedium() != TempoCommand.getState()){
					TempoCommand.setState(TouchTempoView.getCommandMedium());
				}
				if(TouchKeyView.getCommandMedium() != KeyCommand.getState()){
					KeyCommand.setState(TouchKeyView.getCommandMedium());
				}
				if(TouchPauseView.getCommandMedium() != PauseCommand.getState()){
					PauseCommand.setState(TouchPauseView.getCommandMedium());
				}
				if(TouchScoreView.getCommandMedium() != ScoreCommand.getState()){
					ScoreCommand.setState(TouchScoreView.getCommandMedium());
					scoreView.invalidate();
				}
				if(TouchToneView.getCommandMedium() != ToneCommand.getState()){
					ToneCommand.setState(TouchToneView.getCommandMedium());
				}
				if(TouchSingerView.getCommandMedium() != SingerCommand.getState()){
					SingerCommand.setState(TouchSingerView.getCommandMedium());
				}
				if(TouchRepeatView.getCommandMedium() != RepeatCommand.getState()){
					RepeatCommand.setState(TouchRepeatView.getCommandMedium());
				}
				
			} else {
				if(getMyActivity() != null){
					boolean isUser = ((MyApplication)getMyActivity().getApplication()).getCommandEnable();
					if(isUser != UserCommand.getStateBoolean()){
						UserCommand.setStateBoolean(isUser);
					}
				}
				MyLog.e("DANCEVIEW.............", TouchDanceView.getCommandEnable() + "");
				if(TouchDanceView.getCommandEnable() != DanceCommand.getStateBoolean()){
					DanceCommand.setStateBoolean(TouchDanceView.getCommandEnable());
				}
				if(TouchVolumnView.getCommandEnable() != VolumeCommand.getStateBoolean()){
					VolumeCommand.setStateBoolean(TouchVolumnView.getCommandEnable());
					volumnMasterView.invalidate();
				}
				if(TouchNextView.getCommandEnable() != NextCommand.getStateBoolean()){
					NextCommand.setStateBoolean(TouchNextView.getCommandEnable());
				}
				if(TouchMelodyView.getCommandEnable() != MelodyCommand.getStateBoolean()){
					MelodyCommand.setStateBoolean(TouchMelodyView.getCommandEnable());
				}
				if(TouchTempoView.getCommandEnable() != TempoCommand.getStateBoolean()){
					TempoCommand.setStateBoolean(TouchTempoView.getCommandEnable());
				}
				if(TouchKeyView.getCommandEnable() != KeyCommand.getStateBoolean()){
					KeyCommand.setStateBoolean(TouchKeyView.getCommandEnable());
				}
				if(TouchPauseView.getCommandEnable() != PauseCommand.getStateBoolean()){
					PauseCommand.setStateBoolean(TouchPauseView.getCommandEnable());
				}
				if(TouchScoreView.getCommandEnable() != ScoreCommand.getStateBoolean()){
					ScoreCommand.setStateBoolean(TouchScoreView.getCommandEnable());
					scoreView.invalidate();
				}
				if(TouchToneView.getCommandEnable() != ToneCommand.getStateBoolean()){
					ToneCommand.setStateBoolean(TouchToneView.getCommandEnable());
				}
				if(TouchSingerView.getCommandEnable() != SingerCommand.getStateBoolean()){
					SingerCommand.setStateBoolean(TouchSingerView.getCommandEnable());
				}
				if(TouchRepeatView.getCommandEnable() != RepeatCommand.getStateBoolean()){
					RepeatCommand.setStateBoolean(TouchRepeatView.getCommandEnable());
				}
			}
			
			if (flagControlFullAPI) {
				int scorePercent = MyApplication.getCommandMediumScoreMethod();
				if (ScoreCommand.getStatePercent() != scorePercent) {
					ScoreCommand.setStatePercent(scorePercent);
				}			
			} else {
				ScoreCommand.setStatePercent(0);
			}
			
			/*
			if (status.getCurrentScore() != ScoreCommand.getStateScode()){
				scoreView.setScoreView(status.getCurrentScore());	
				ScoreCommand.setStateScode(0);
			}
			if (status.getCurrentScore() != ScoreCommand.getStatePercent()){
				ScoreCommand.setStatePercent(0);
			}
			*/
		}
	}
	
	private DialogControl dialogControl;
	private void ShowDialogControl(final int layout){
		MyApplication.freezeTime = System.currentTimeMillis();
		if(dialogControl == null){
			dialogControl = new DialogControl(context, window, getMyActivity());
			dialogControl.setOnOnDialogControlListenerListener(new OnDialogControlListener() {
				
				@Override
				public void OnYesControl() {
					if(buttonOk != null){
						buttonOk.setActiveButtonOk(true);
					}
					
					dialogControl = null;
					if(listener != null){
						if(TouchMainActivity.serverStatus != null || KTVMainActivity.serverStatus != null){
							boolean flagControlFullAPI = false;
							
							if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
								if(KTVMainActivity.serverStatus.isOnOffControlFullAPI()){
									flagControlFullAPI = true;
								}
							} else {
								if(TouchMainActivity.serverStatus.isOnOffControlFullAPI()){
									flagControlFullAPI = true;
								}
							}
							
							if(flagControlFullAPI){
								if(layout == DialogControl.MAC_DINH){
									MyApplication.intCommandMedium = MyApplication.MODE_MACDINH_3NAC;
								} else if(layout == DialogControl.PHONG_HAT){
									MyApplication.intCommandMedium = MyApplication.MODE_PHONGHAT_3NAC;
								}	
								
								if(getMyActivity() != null){
									int isUser = ((MyApplication)getMyActivity().getApplication()).getCommandMedium();
									UserCommand.setState(isUser);
								}
								DanceCommand.setState(TouchDanceView.getCommandMedium());
								VolumeCommand.setState(TouchVolumnView.getCommandMedium());
								NextCommand.setState(TouchNextView.getCommandMedium());
								MelodyCommand.setState(TouchMelodyView.getCommandMedium());
								TempoCommand.setState(TouchTempoView.getCommandMedium());
								KeyCommand.setState(TouchKeyView.getCommandMedium());
								PauseCommand.setState(TouchPauseView.getCommandMedium());
								ScoreCommand.setState(TouchScoreView.getCommandMedium());
								ToneCommand.setState(TouchToneView.getCommandMedium());
								SingerCommand.setState(TouchSingerView.getCommandMedium());
								RepeatCommand.setState(TouchRepeatView.getCommandMedium());
								scoreView.invalidate();
							} else {
								if(layout == DialogControl.MAC_DINH){
									MyApplication.intCommandEnable = MyApplication.MODE_MACDINH_2NAC;
								}
								
								if(getMyActivity() != null){
									boolean isUser = ((MyApplication)getMyActivity().getApplication()).getCommandEnable();
									UserCommand.setStateBoolean(isUser);
								}
								DanceCommand.setStateBoolean(TouchDanceView.getCommandEnable());
								VolumeCommand.setStateBoolean(TouchVolumnView.getCommandEnable());
								NextCommand.setStateBoolean(TouchNextView.getCommandEnable());
								MelodyCommand.setStateBoolean(TouchMelodyView.getCommandEnable());
								TempoCommand.setStateBoolean(TouchTempoView.getCommandEnable());
								KeyCommand.setStateBoolean(TouchKeyView.getCommandEnable());
								PauseCommand.setStateBoolean(TouchPauseView.getCommandEnable());
								ScoreCommand.setStateBoolean(TouchScoreView.getCommandEnable());
								ToneCommand.setStateBoolean(TouchToneView.getCommandEnable());
								SingerCommand.setStateBoolean(TouchSingerView.getCommandEnable());
								RepeatCommand.setStateBoolean(TouchRepeatView.getCommandEnable());
								scoreView.invalidate();
							}							
						}	
						if(listener != null){
							listener.OnCommandEnable(0);
						}
					}
					MyLog.i(TAB, "OnYesControl()");
				}
				
				@Override
				public void OnNoControl() {
					dialogControl = null;
					MyLog.i(TAB, "OnNoControl()");
				}
				
				@Override
				public void OnFinishListener() {
					dialogControl = null;
				}
			});
			dialogControl.showToast(layout);
		}
	}
	
	private DialogCancelPhong dialogCancelPhong;
	private void ShowDialogCancelPhong(int layout){
		if(dialogCancelPhong == null){
			dialogCancelPhong = new DialogCancelPhong(context, window, getMyActivity());
			dialogCancelPhong.setOnCancelPhongListener(new OnCancelPhongListener() {
				
				@Override
				public void OnYesControl() {
					if(getMyActivity() != null){
						boolean isUser = ((MyApplication)getMyActivity().getApplication()).getCommandEnable();
						UserCommand.setStateBoolean2(isUser);
					}

					DanceCommand.setStateBoolean2(TouchDanceView.getCommandEnable());
					VolumeCommand.setStateBoolean2(TouchVolumnView.getCommandEnable());
					NextCommand.setStateBoolean2(TouchNextView.getCommandEnable());
					MelodyCommand.setStateBoolean2(TouchMelodyView.getCommandEnable());
					TempoCommand.setStateBoolean2(TouchTempoView.getCommandEnable());
					KeyCommand.setStateBoolean2(TouchKeyView.getCommandEnable());
					PauseCommand.setStateBoolean2(TouchPauseView.getCommandEnable());
					ScoreCommand.setStateBoolean2(TouchScoreView.getCommandEnable());
					ToneCommand.setStateBoolean2(TouchToneView.getCommandEnable());
					SingerCommand.setStateBoolean2(TouchSingerView.getCommandEnable());
					RepeatCommand.setStateBoolean2(TouchRepeatView.getCommandEnable());
					commandBack.invalidate();
					scoreView.invalidate();
					dialogCancelPhong = null;
				}
				
				@Override
				public void OnFinishListener() {
					dialogCancelPhong = null;
				}
			});
			dialogCancelPhong.showToast(layout);
		}
	}
	
	private DialogScorePercent dialogScorePercent = null;
	private void ShowDialogScorePercent(){
		if(dialogScorePercent == null){
			dialogScorePercent = new DialogScorePercent(context, window, getMyActivity());
			dialogScorePercent.setOnDialogScorePercentListener(new OnDialogScorePercentListener() {
				
				@Override
				public void OnScorePercentListener(int percent) {
					if(listener != null){
						listener.OnScorePercentListener(percent);
					}
				}
				
				@Override
				public void OnFinishListener() {
					dialogScorePercent = null;
				}
			});
			dialogScorePercent.showToast();
		}
	}
	
	private DialogBlockVolumn dialogBlockVolumn = null;
	private void ShowDialogVolumn(){
		if(dialogBlockVolumn == null){
			dialogBlockVolumn = new DialogBlockVolumn(context, window, getMyActivity());
			dialogBlockVolumn.setOnDialogBlockVolumnListener(new OnDialogBlockVolumnListener() {
				
				@Override
				public void OnFinishListener() {
					dialogBlockVolumn = null;
				}

				@Override
				public void OnVolumnMIDI(int value) {
					if(listener != null){
						listener.OnVolumnMIDI(value);
					}
				}

				@Override
				public void OnVolumnKTV(int value) {
					if(listener != null){
						listener.OnVolumnKTV(value);
					}
				}

				@Override
				public void OnVolumnMASTER(int value) {
					if(listener != null){
						listener.OnVolumnMASTER(value);
					}
				}

				@Override
				public void OnVolumnDANCE(int value) {
					if(listener != null){
						listener.OnVolumnDANCE(value);
					}
				}

				@Override
				public void OnVolumnPIANO(int value) {
					if(listener != null){
						listener.OnVolumnPIANO(value);
					}
				}
				
			});
			dialogBlockVolumn.showToast();
		}
	}
	
	private DialogBlockMelody dialogBlockMelody = null;
	private void ShowDialogMelody(){
		MyLog.e(TAB, "ShowDialogMelody()");
		if(dialogBlockMelody == null){
			dialogBlockMelody = new DialogBlockMelody(context, window, getMyActivity());
			dialogBlockMelody.setOnDialogBlockMelodyListener(new OnDialogBlockMelodyListener() {
				
				@Override
				public void OnMelodyDefault(int value) {
					if(listener != null){
						listener.OnMelodyDefault(value);
					}
				}
				
				@Override
				public void OnFinishListener() {
					dialogBlockMelody = null;
				}
			});
			dialogBlockMelody.showToast();
		}
	}

	@Override
	public void OnCommand(int id, boolean bool, boolean isShowDialog) {
		MyApplication.freezeTime = System.currentTimeMillis();
		if(isShowDialog == false){
				switch (id) {
				case R.id.UserCommand:
					if(getMyActivity() != null){
						((MyApplication)getMyActivity().getApplication()).setCommandEnable(bool);
					}
					break;
				case R.id.VolumeCommand:
					TouchVolumnView.setCommandEnable(bool);
					volumnMasterView.invalidate();
					break;
				case R.id.NextCommand:
					TouchNextView.setCommandEnable(bool);
					break;
				case R.id.MelodyCommand:
					TouchMelodyView.setCommandEnable(bool);
					break;
				case R.id.TempoCommand:
					TouchTempoView.setCommandEnable(bool);
					break;
				case R.id.KeyCommand:
					TouchKeyView.setCommandEnable(bool);
					break;
				case R.id.PauseCommand:
					TouchPauseView.setCommandEnable(bool);
					break;
				case R.id.ScoreCommand:
					TouchScoreView.setCommandEnable(bool);
					break;
				case R.id.ToneCommand:
					TouchToneView.setCommandEnable(bool);
					break;
				case R.id.SingerCommand:
					TouchSingerView.setCommandEnable(bool);
					break;
				case R.id.RepeatCommand:
					TouchRepeatView.setCommandEnable(bool);
					break;
				case R.id.DanceCommand:
					TouchDanceView.setCommandEnable(bool);
					break;
				default: break;
				}
				
				if(buttonOk != null){
					buttonOk.setActiveButtonOk(true);
				}
				if(listener != null){
					listener.OnCommandEnable(id);
				}
		} else {
				MyLog.e(TAB, "Show Dialog Khoa Dien Thoai");
				ShowDialogCancelPhong(0);				
		}
	}

}
