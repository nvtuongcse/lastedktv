package app.sonca.flower;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import app.sonca.Dialog.ScoreLayout.BaseDialog;
import app.sonca.flower.ViewFlowerTop.OnFlowerBackListener;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.params.ServerStatus;
import vn.com.sonca.utils.FileUtilities;
import vn.com.sonca.zktv.main.KTVMainActivity;
import vn.com.sonca.zzzzz.MyApplication;

public class DialogFlower extends BaseDialog {
	
	private TouchMainActivity mainActivity;
	private KTVMainActivity ktvMainActivity;
	private String namedevice = "";
	private Context context;
	public DialogFlower(TouchMainActivity mainActivity, Context context, Window window, String namedevice) {
		super(context, window);
		this.namedevice = namedevice;
		this.mainActivity = mainActivity;
		this.context = context;
	}
	
	public DialogFlower(KTVMainActivity mainActivity, Context context, Window window, String namedevice) {
		super(context, window);
		this.namedevice = namedevice;
		this.ktvMainActivity = mainActivity;
		this.context = context;
	}
	
	private OnDialogFlowerListener listener;
	public interface OnDialogFlowerListener {
		public void OnFlower(String name, int avatar, int number);
		public void OnDoXucxac();
		public void OnLuckyRoll();
	}
	
	public void setOnDialogFlowerListener(OnDialogFlowerListener listener){
		this.listener = listener;
	}

	@Override
	protected int getIdLayout() {
		return R.layout.dialog_flower;
	}

	@Override
	protected int getTimerShow() {
		return 0;
	}

	private ViewFlowerTop flowerTop;
	private TextView txtLucky_result;
	private LuckRollView luckRollView;
	
	@Override
	protected View getView(View contentView) {
		final GroupAvatar groupAvatar = (GroupAvatar)contentView.findViewById(R.id.groupAvatar);
		final GroupGiveFlower giveFlower = (GroupGiveFlower)contentView.findViewById(R.id.groupGiveFlower);
		ViewFlowerBut button = (ViewFlowerBut)contentView.findViewById(R.id.button);
		
		ViewFlowerBut button1 = (ViewFlowerBut)contentView.findViewById(R.id.button1);
		button1.setNameBut(context.getString(R.string.doxucxac2));

		ViewFlowerBut button2 = (ViewFlowerBut)contentView.findViewById(R.id.button2);
		button2.setNameBut(context.getString(R.string.tang_4));

		luckRollView = (LuckRollView)contentView.findViewById(R.id.luckRollView);
		txtLucky_result = (TextView)contentView.findViewById(R.id.txtLucky_result);
				
		luckRollView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(listener != null){
					listener.OnLuckyRoll();
				}
			}
		});
		
		if(mainActivity.serverStatus != null){
			if(mainActivity.serverStatus.isPlayingLucky()){
				luckRollView.setCurrentLuck(mainActivity.serverStatus.getCurrentLucky());
				luckRollView.startTimerLuckyRoll();
			} else {
				luckRollView.setCurrentLuck(mainActivity.serverStatus.getCurrentLucky());
			}			
		}		
		
		flowerTop = (ViewFlowerTop)contentView.findViewById(R.id.topFlower);
		if(namedevice != null){
			flowerTop.setNameDevice(namedevice);
		}
		button.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View arg0) {
				if(listener != null){
					listener.OnFlower(
						"SONCA", 
						groupAvatar.getAvatar(), 
						giveFlower.getNumberFlowers());
				}
			}
		});
		
		button1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(listener != null){
					listener.OnDoXucxac();
				}
			}
		});
		
		button2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(listener != null){
					listener.OnLuckyRoll();
				}
			}
		});
		
		flowerTop.setOnFlowerBackListener(new OnFlowerBackListener() {
			@Override public void OnFlowerBackListener() {
				dismissDialog();
			}
		});
/*
		flowerSong.setNameSong(mainActivity.getCurrentSongName());
	*/	
		
		mainActivity.luckRollView = luckRollView;
		processListLuckyData();
		processDisplayLuckyResult();
		
		return contentView;
	}

	@Override
	protected void OnShow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void OnDismiss() {
		if(luckRollView != null){
			luckRollView.stopTimerLuckyRoll();
			luckRollView = null;
		}
		if(mainActivity.luckRollView != null){
			mainActivity.luckRollView.stopTimerLuckyRoll();
			mainActivity.luckRollView = null;
		}
	}

	@Override
	protected void OnReceiveDpad(KeyEvent event, int key) {
		// TODO Auto-generated method stub
		
	}
	
	public void syncPauPlay(ServerStatus status){
		/*
		if(flowerSong != null && status != null && status.isPlaying() != flowerSong.isPausePlay()){
			//MyLog.e("TEST", "set lai pause play");
			flowerSong.setPausePlay(status.isPlaying());
		}
		*/
	}
	
	public void syncNameSong(String name){
		/*
		if(flowerSong != null){
			//MyLog.e("TEST", "set lai name -- " + name);
			flowerSong.setNameSong(name);
			if(name == null || name.isEmpty() || name.equals("") || name.equals(" ")){
				dismissDialog();
				mainActivity.showDialogMessage(mainActivity.getResources()
						.getString(R.string.msg_tanghoa_2a)
						+ "."
						+ mainActivity.getResources().getString(
								R.string.msg_tanghoa_2b));
			}
		}
		*/
	}
	
	public void forceCloseDialog(){
		dismissDialog();
	}
	
	private ArrayList<String> listLucky = new ArrayList<String>();
	private void processListLuckyData(){
		String rootPath = "";
		
		listLucky = new ArrayList<String>();
		
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			rootPath = android.os.Environment.getExternalStorageDirectory()
					.toString();
			rootPath = rootPath.concat(String.format("/%s/%s", "Android/data",
					context.getPackageName()));
			File appBundle = new File(rootPath);
			if (!appBundle.exists())
				appBundle.mkdirs();
		}
		
		String savePath = rootPath.concat("/VONGXOAY/" + MyApplication.LIST_LUCKYDATA);
		File updateFile = new File(savePath);
		if (!updateFile.exists()) {
			listLucky.add(context.getResources().getString(R.string.luckydata_1));
			listLucky.add(context.getResources().getString(R.string.luckydata_2));
			listLucky.add(context.getResources().getString(R.string.luckydata_3));
			listLucky.add(context.getResources().getString(R.string.luckydata_4));
			listLucky.add(context.getResources().getString(R.string.luckydata_5));
			listLucky.add(context.getResources().getString(R.string.luckydata_6));
			listLucky.add(context.getResources().getString(R.string.luckydata_7));
			listLucky.add(context.getResources().getString(R.string.luckydata_8));
			listLucky.add(context.getResources().getString(R.string.luckydata_9));
			listLucky.add(context.getResources().getString(R.string.luckydata_10));
			listLucky.add(context.getResources().getString(R.string.luckydata_11));
			listLucky.add(context.getResources().getString(R.string.luckydata_12));
			return;
		}
		
		try {
			String data = FileUtilities.getStringFromFile(savePath);
			if(data != null && !data.trim().equals("")){
				String[] datas = data.trim().split("\n");
				if(datas.length >= 12){
					listLucky.add(datas[0].trim());
					listLucky.add(datas[1].trim());
					listLucky.add(datas[2].trim());
					listLucky.add(datas[3].trim());
					listLucky.add(datas[4].trim());
					listLucky.add(datas[5].trim());
					listLucky.add(datas[6].trim());
					listLucky.add(datas[7].trim());
					listLucky.add(datas[8].trim());
					listLucky.add(datas[9].trim());
					listLucky.add(datas[10].trim());
					listLucky.add(datas[11].trim());
				}
			}
		} catch (Exception e) {
			
		}
		
		if(listLucky.size() > 0){
			luckRollView.setLuckyData(listLucky);
		}
			
		savePath = rootPath.concat("/VONGXOAY/" + MyApplication.LIST_LUCKYIMAGE);
		updateFile = new File(savePath);
		if(updateFile.exists()){
			Bitmap myBitmap = BitmapFactory.decodeFile(updateFile.getAbsolutePath());
			Drawable d = new BitmapDrawable(context.getResources(), myBitmap);
			luckRollView.setLogoImage(d);
		} else {
			luckRollView.setLogoImage(null);
		}
		
	}
	
	public void processDisplayLuckyResult(){
		txtLucky_result.setText("");
		if(luckRollView != null){
			int value = luckRollView.getCurrentLuck();
			if(mainActivity.serverStatus != null && mainActivity.serverStatus.getCurrentLucky() <= 12
					&& mainActivity.serverStatus.getCurrentLucky() > 0 && value > 0
					&& listLucky.size() > 0){
				try {
					int color = pickColorFromIndex(value - 1);
					txtLucky_result.setVisibility(View.VISIBLE);
					txtLucky_result.setTextColor(color);
					txtLucky_result.setText(listLucky.get(value - 1));
					startTimerLuckyRoll();	
				} catch (Exception e) {
				
				}
				
			}
			
		}
		
	}
	
	public void processClearLuckyResult(){
		txtLucky_result.setText("");		
	}
	
	private int pickColorFromIndex(int idx){
		int color = Color.argb(0, 0, 0, 0);
		switch (idx) {
		case 0:
			color = Color.argb(255, 254, 254, 51);
			break;
		case 1:
			color = Color.argb(255, 208, 234, 43);
			break;
		case 2:
			color = Color.argb(255, 102, 176, 50);
			break;
		case 3:
			color = Color.argb(255, 3, 145, 206);
			break;
		case 4:
			color = Color.argb(255, 2, 71, 254);
			break;
		case 5:
			color = Color.argb(255, 61, 1, 164);
			break;
		case 6:
			color = Color.argb(255, 134, 1, 175);
			break;
		case 7:
			color = Color.argb(255, 165, 25, 75);
			break;
		case 8:
			color = Color.argb(255, 254, 39, 18);
			break;
		case 9:
			color = Color.argb(255, 253, 83, 8);
			break;
		case 10:
			color = Color.argb(255, 251, 153, 2);
			break;	
		case 11:
			color = Color.argb(255, 250, 188, 2);
			break;
		default:
			Color.argb(0, 0, 0, 0);
			break;
		}
		return color;
	}
	
	
	private int countBlink = 0;
	public void startTimerLuckyRoll(){
		stopTimerLuckyRoll();
		
		countBlink = 0;
		timerLuckyRoll = new Timer();
		timerLuckyRoll.schedule(new LuckyRollTask(), 100, 300);
	}
	
	public void stopTimerLuckyRoll(){
		if (timerLuckyRoll != null) {
			timerLuckyRoll.cancel();
			timerLuckyRoll = null;
		}
	}
	
	private Timer timerLuckyRoll;
	private class LuckyRollTask extends TimerTask {	
		
		@Override		
		public void run() {	    	 
			countBlink++;			    	 
	    	 if(countBlink > 6){
	    		 stopTimerLuckyRoll();
	    		 return;
	    	 }	
	    	 handlerLuckyRoll.sendEmptyMessage(0);			
		}
	}
	
	private Handler handlerLuckyRoll = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(countBlink % 2 == 0){
				txtLucky_result.setVisibility(View.VISIBLE);
			} else {
				txtLucky_result.setVisibility(View.INVISIBLE);
			}
		};
	};
	
}
