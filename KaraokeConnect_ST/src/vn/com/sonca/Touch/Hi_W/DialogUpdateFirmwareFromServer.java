package vn.com.sonca.Touch.Hi_W;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
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
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.BlockCommand.ButtonControl;
import vn.com.sonca.Touch.Hi_W.DownloadFileFirmwareFromServer.OnDownloadFileFirmwareFromServer;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.zzzzz.MyApplication;

public class DialogUpdateFirmwareFromServer implements OnDownloadFileFirmwareFromServer{
	
	private String TAB = "DialogHiW";
	
	private Context context;
	private Window window;
	private TouchMainActivity activity;
	private FragmentActivity tempActivity;
	private LayoutInflater layoutInflater;	
	private WindowManager.LayoutParams params_relative;
	public DialogUpdateFirmwareFromServer(Context context, Window window, TouchMainActivity activity) {
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
	
	public DialogUpdateFirmwareFromServer(Context context, Window window, FragmentActivity activity) {
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
	
	private OnUpdateFirmwareFromServer listener;
	public interface OnUpdateFirmwareFromServer{
		public void OnFinishListener();
		public void OnCancel();
	}
	
	public void setOnUpdateFirmwareFromServer(OnUpdateFirmwareFromServer listener){
		this.listener = listener;
	}
	
	private SeekBar seekBar;
	private TextView textCount;
	private TextView textMax;
	private LinearLayout layoutInfo;
	private LinearLayout layoutDown;
	
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
			viewToast = layoutInflater.inflate(R.layout.dialog_firmware_server, null);
			viewToast.findViewById(R.id.Layout).setOnClickListener(new OnClickListener() {
				@Override public void onClick(View arg0) {}
			});
				//-----------------------//
			LinearLayout layoutImaga1 = (LinearLayout)viewToast.findViewById(R.id.layoutImage1);
			LinearLayout layoutImaga2 = (LinearLayout)viewToast.findViewById(R.id.layoutImage2);
			ButtonControl butLeft = (ButtonControl)viewToast.findViewById(R.id.butLeft);
			ButtonControl butRight = (ButtonControl)viewToast.findViewById(R.id.butRight);
			TextView textTitle = (TextView)viewToast.findViewById(R.id.textTitle);
			layoutInfo = (LinearLayout)viewToast.findViewById(R.id.layout_info);
			layoutDown = (LinearLayout)viewToast.findViewById(R.id.layout_download);
			textMax = (TextView)viewToast.findViewById(R.id.textCount);
			textCount = (TextView)viewToast.findViewById(R.id.textMax);
			seekBar = (SeekBar)viewToast.findViewById(R.id.seekBar);
			
			layoutInfo.setVisibility(View.VISIBLE);
			layoutDown.setVisibility(View.INVISIBLE);
			seekBar.setMax(100);
			
			butLeft.setTextTitle("OK");
			butRight.setTextTitle("CANCEL");
			
			butLeft.setOnClickListener(new OnClickListener() {
				@Override public void onClick(View arg0) {
					layoutInfo.setVisibility(View.INVISIBLE);
					layoutDown.setVisibility(View.VISIBLE);
					downloadFilrFirmwareFromServer();
				}
			});
			butRight.setOnClickListener(new OnClickListener() {
				@Override public void onClick(View arg0) {
					hideToastBox();
					if(listener != null){
						listener.OnCancel();
					}
				}
			});
			
			if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				layoutImaga1.setBackgroundResource(R.drawable.icon_boder_popup);
				layoutImaga2.setBackgroundResource(R.drawable.icon_boder_popup);
				textTitle.setTextColor(Color.parseColor("#B4FEFF"));
				textCount.setTextColor(Color.parseColor("#B4FEFF"));
				textMax.setTextColor(Color.parseColor("#B4FEFF"));
			}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
				layoutImaga1.setBackgroundResource(R.drawable.zlight_boder_popup);
				layoutImaga2.setBackgroundResource(R.drawable.zlight_boder_popup);
				textTitle.setTextColor(Color.parseColor("#005249"));
				textCount.setTextColor(Color.parseColor("#005249"));
				textMax.setTextColor(Color.parseColor("#005249"));
			}
			
				//-----------------------//
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
			
			layoutInfo.setVisibility(View.INVISIBLE);
			layoutDown.setVisibility(View.VISIBLE);
			downloadFilrFirmwareFromServer();
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
	
	private int versionFirmware = 0;
	private int revisionFirmware = 0;
	private ArrayList<Firmware> linkFirmware;
	public void setLinkDownload(ArrayList<Firmware> list, int version, int revision){
		this.versionFirmware = version;
		this.revisionFirmware = revision;
		this.linkFirmware = list;
	}
	
	private DownloadFileFirmwareFromServer download = null;
	private void downloadFilrFirmwareFromServer(){
		if(download == null){
			download = new DownloadFileFirmwareFromServer(context, 
					linkFirmware, versionFirmware, revisionFirmware);
			download.setOnDownloadFileFirmwareFromServer(this);
			download.execute();
		}else{
			if(download.getStatus() == AsyncTask.Status.FINISHED){
				download = null;
				download = new DownloadFileFirmwareFromServer(context, 
						linkFirmware, versionFirmware, revisionFirmware);
				download.setOnDownloadFileFirmwareFromServer(this);
				download.execute();
			}
		}
	}

	@Override
	public void OnRunning(int progress) {
		if(download != null){
			int lenght = download.getLengthFileFirmware();
			seekBar.setProgress(progress);
			textMax.setText((int)(lenght / 1024) + "KB");
			textCount.setText(progress + "%");
		}
	}

	@Override
	public void OnFinish() {
		hideToastBox();
	}
	
	
}
