package vn.com.sonca.ColorLyric;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.ColorLyric.LoadColorLyric.OnLoadColorLyricListener;
import vn.com.sonca.ColorLyric.LyricBack.OnBackLyric;
import vn.com.sonca.ColorLyric.MyPlayer.OnPlayerListener;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.RemoteControl.RemoteIRCode;
import vn.com.sonca.Touch.CustomView.DownVideoView;
import vn.com.sonca.Touch.CustomView.DownVideoView.OnDownVideoViewListener;
import vn.com.sonca.Touch.CustomView.TouchNextView;
import vn.com.sonca.Touch.CustomView.TouchNextView.OnNextListener;
import vn.com.sonca.Touch.CustomView.TouchPauseView;
import vn.com.sonca.Touch.CustomView.TouchPauseView.OnPauseListener;
import vn.com.sonca.Touch.CustomView.TouchRepeatView;
import vn.com.sonca.Touch.CustomView.TouchRepeatView.OnRepeatListener;
import vn.com.sonca.Touch.CustomView.TouchSingerView;
import vn.com.sonca.Touch.CustomView.TouchSingerView.OnSingerListener;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity.OnColorLyricListener;
import vn.com.sonca.database.DBInterface;
import vn.com.sonca.params.Musician;
import vn.com.sonca.params.ServerStatus;
import vn.com.sonca.params.Singer;
import vn.com.sonca.params.Song;
import vn.com.sonca.samba.Samba;
import vn.com.sonca.smartkaraoke.NetworkSocket;
import vn.com.sonca.utils.AppConfig.MEDIA_TYPE;
import vn.com.sonca.zktv.main.KTVMainActivity;
import vn.com.sonca.zzzzz.MyApplication;
import vn.com.sonca.zzzzz.MyApplication.OnReceiverInfoLyricListener;
import vn.com.sonca.zzzzz.MyApplication.OnReceiverLyricVidLinkListener;
import vn.com.sonca.zzzzz.MyApplication.OnReceiverScoreInfoListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import app.sonca.Dialog.ScoreLayout.ScoreDialog;
import app.sonca.Dialog.ScoreLayout.BaseDialog.OnBaseDialogListener;
import android.widget.TextView;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.MediaPlayer;
import tv.danmaku.ijk.media.widget.VideoView;
//import android.widget.VideoView;
import android.widget.ViewFlipper;



public class FragmentKaraoke extends Fragment implements OnPlayerListener, OnColorLyricListener{
	
private String audioUrl;
	
	private final String TAG = "FragmentKaraoke";
	private final int LOOP_PERIOD = 3;
	private final int LOOP_MAX_TIMES = 10;
	private int TIMEPLUSVIDEO = 3000;
	private int TIMECheckPause = 300;
	private int PeriodGetTime=3000;
	private boolean flag801 = false;
	//private final int TYPE_MEN = 0;
	//private final int TYPE_WOMEN = 1;
	//private final int TYPE_ALL = 2;

//	private final int SPACE_PERCENT_LEFT = 15;
	private final int SPACE_PERCENT_RIGHT = 5;
	
	private ViewFlipper myFlipperImage;
	
	private MyPlayer mPlayer;
	public SongColor mSong;	
	private Lyrics mLyricItems;
	private MagicTextView mTopLine;
	private MagicTextView mBottomLine;
	private MagicTextView mText1Line;
	private MagicTextView mtxtTitleSong;
	private MagicTextView mtxtAuthorSong;
	private ImageView mImageCounter;
	private ImageView mIconSingerTop;
	private ImageView mIconSingerBot;
	private int cntIconSinger=0;
	private String currentIconSinger="";
	private SentenceTextView mTopColorLine;
	private SentenceTextView mBottomColorLine;	
		
	private boolean isStarted = true;
	private int currentLyricBlock = 0;
	private boolean bIsShowLastLine = false;
	private boolean bIsShowPanLine = false;
	private SentenceSong sentence1 = null;
	private SentenceSong sentence2 = null;
	private SentenceSong lastSentence = null;

	private int colorShowBody;
	private int colorShowBorder;
	private int colorPanBody[];
	private int colorPanBorder;

	private boolean isChangeSentence;

	private long timeLoopChangeColor, timeLoopService;
	private long timeStartCount;
	private long savePauseTime;
	private long saveTimePlayerCurrentInMsec;
	private boolean isStartingKaraoke;
	private int curLyricMaxWidth = 0;
	private int curLyricSize = 0;
	private int saveCurLyricSize = 0;
	private int curLyricSizePanDone = 0;
	private String curCntdownWord = "";
	private boolean bIsEndBlock = false;
	private boolean bIsLastWordOfLine = false;
	private Timer timerService;
	private int timerCounter = 0;
	private int OldtimerCounter = 0;
	private int iRepeat = 0;
	private int screenWidthInPixels;
	private int maxWidthOfTextSize;
	
	private TouchPauseView pauseView;
	private TouchNextView nextView;
//	private RepeatView repeatView;
	//private DownVideoView downVideoView;
	private TouchSingerView btnSinger;
	private LyricBack lyricBack;
	private String titleSong="";
	
	private boolean iFrishcheckPLay=true;
	private boolean iFrishcheckVocalSinger=false;
	View view;
	private Context context;
	private Window window;
	private int idSongCurrent;
	private int intMediaTypeCurrent;
	private int intMidiShiftTimeCurrent;
	private int intMainTypeABC; 
	private Song curSong;
	private boolean iCoupleSinger=false;
	private boolean iVocalSinger=false;
	private boolean iId0=false;
	private RelativeLayout rootLayout;
	private LinearLayout activity_karaoke_Control;
	private RelativeLayout activity_karaoke_InfoSong;
	private OnKaraokeFragmentListener listener;
	private TouchMainActivity mainActivity;
	private KTVMainActivity ktvMainActivity;
	
	String greetingLine1="";
	String greetingLine2="";
	private MediaPlayer mMediaPlayer; 
	 private Samba samba = null; 
    String IpDevice="";
   boolean iPlayVideoSamba=false;    
    private RelativeLayout progressBar;
	
    private long longTimeExit = 0;
    
	public interface OnKaraokeFragmentListener{		
		public void OnBackKaraoke();
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		listener = (OnKaraokeFragmentListener)activity;
		if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			ktvMainActivity = (KTVMainActivity) activity;
		} else {
			mainActivity = (TouchMainActivity) activity;	
		}
		
	}
	
	public FragmentActivity getMyActivity(){
		if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			return this.ktvMainActivity;
		} else {
			return this.mainActivity;
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		MyLog.d(TAG, "==onCreateView==");
		view = inflater.inflate(R.layout.fragment_karaoke, container, false);
		context = getActivity().getApplicationContext();
		window = getActivity().getWindow();
		if(MyApplication.enableSamba){
			if(samba == null){//samba
				samba = new Samba(getActivity().getApplicationContext()); 
			}
		}
		greetingLine1=getMyActivity().getResources().getString(R.string.karaoke_Greeting_Line1);
		greetingLine2=getMyActivity().getResources().getString(R.string.karaoke_Greeting_Line2);		
		
		longTimeExit = System.currentTimeMillis() - 1000;
		
		initView();			
		
		 lyricBack = (LyricBack)view.findViewById(R.id.lyricBack);
		//lyricBack.setTitleView(mainActivity.getResources().getString(R.string.painting_lyric_1));
		 lyricBack.setCurrectSong(getMyActivity().getResources().getString(R.string.painting_lyric_1));
		lyricBack.setOnBackLyric(new OnBackLyric() {
			@Override public void OnBack() {
				exitKaraoke();
			}
		});	
		
		if(MyApplication.curHiW_firmwareInfo != null){
			if(MyApplication.curHiW_firmwareInfo.getDaumay_version() >= 200){
				PeriodGetTime = 10000;
			} else {
				PeriodGetTime = 3000;
			}
		} else {
			PeriodGetTime = 3000;
		}
		
		audioUrl="/mnt/sdcard/Download/music.mp3";
		OnCheckVideo();
		int id=getArguments().getInt("id");	
		int intMediaType=getArguments().getInt("intMediaType");
		int midiShifTime = getArguments().getInt("midiShifTime");
		IpDevice= getArguments().getString("IP");
		iFrishcheckPLay=getArguments().getBoolean("Pause");	
		int cnt=getArguments().getInt("cntPlaylist");
		int mainTypeABC = getArguments().getInt("mainTypeABC");
		iFrishcheckVocalSinger=getArguments().getBoolean("VocalSinger");
		
		String serverName = getArguments().getString("ServerName");
		
		lyricBack.setSumSong(cnt);
		lyricBack.setServerName(serverName);
		
		MyLog.d(TAG, "=start=id="+id+"=cnt="+cnt+"=iFrishcheckPLay="+iFrishcheckPLay+"=iFrishcheckVocalSinger="+iFrishcheckVocalSinger);
		if(id==0 && cnt==0){
			iId0=true;
			checkShowGreeting(0);
		}else
			loadLyricSong(id, intMediaType, midiShifTime, mainTypeABC, false);
		try{
		MEDIA_TYPE mediaType= MEDIA_TYPE.values()[intMediaType];
		if (mediaType != MEDIA_TYPE.VIDEO)
		playVideoDefault(null,false);
		}catch(Exception e){}
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(baseDialog != null){
			baseDialog.dismissDialog();
		}
	}
	
	public void exitKaraoke(){
		stopKaraoke();	
		if(iPlayVideoSamba){
			stopVideo();				
		}
		if(listener != null){
			listener.OnBackKaraoke();
		}
	}
	
	private boolean iNoScore=false;
	private ScoreDialog baseDialog;
	private void showScoreDialog(){
		MyLog.d(TAG, "==showScoreDialog=");
		if(baseDialog == null && !iNoScore){
		((MyApplication)context).getScoreInfo();
		((MyApplication)context).setOnReceiverScoreInfoListener(new OnReceiverScoreInfoListener() {
			
			@Override
			public void OnReceiverScoreInfo(List<Integer> listScore) {
				MyLog.e("", "OnReceiverScoreInfo:"+listScore.size());
				if(listScore != null && listScore.size() >= 2){	
					iNoScore=false;
					int scoreFromModel = listScore.get(0); // same with svrModel
					int latestScore = listScore.get(1);
					MyLog.d(" ", "HAVE SCORE: scoreFromModel="+scoreFromModel);
					for (Integer score : listScore) {
						MyLog.d(" ", score + "");
					}
					if(scoreFromModel==MyApplication.SONCA){
						if(latestScore >=0 && latestScore <= 100){
							listScore.remove(0);
							listScore.remove(0);
							 showScoreDialog(latestScore,0, true, listScore);
						}	
					}else{						
						if(latestScore >=0 && latestScore <= 100){
							 showScoreDialog(latestScore,0, false, null);
						}	
					}					

				} else {
					MyLog.d(" ", "NO SCORE");
					iNoScore=true;
				}
			}
		});
		}
	}
	
	private void hideScoreDialog(){
		if(baseDialog != null){
			baseDialog.dismissDialog();
		}
	}
	
	private void showScoreDialog(int score, int showTime, 
		boolean isSK900, List<Integer> listInteger){
		if(baseDialog == null){
//			for (Integer t : listInteger) {
//				MyLog.d(TAG, "=showScoreDialog=="+t);
//			}
			baseDialog = new ScoreDialog(context, window, score, showTime, isSK900, listInteger);
			// baseDialog.setDisplayScore(score, showTime);
			baseDialog.setOnBaseDialogListener(new OnBaseDialogListener() {
				@Override public void OnFinishDialog() {
					baseDialog = null;
				}
				@Override public void OnShowDialog() {
				}
			});
			baseDialog.showDialog();
		}
	}
	
	private void initView() {
		myFlipperImage = (ViewFlipper) view.findViewById(R.id.myFlipper);
		
		prepareFlipperImages();
		
		activity_karaoke_Control = (LinearLayout) view.findViewById(R.id.activity_karaoke_Control);
		activity_karaoke_InfoSong = (RelativeLayout) view.findViewById(R.id.activity_karaoke_InfoSong);
		
		rootLayout=(RelativeLayout) view.findViewById(R.id.activity_karaoke_root);
		rootLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(System.currentTimeMillis() - longTimeExit < 2000){
					return;
				}
				
				MyLog.d(TAG, "=rootLayout click==");
				exitKaraoke();
			}
		});
		mVideoView = (VideoView) view.findViewById(R.id.myVideo);
		
		pauseView = (TouchPauseView) view.findViewById(R.id.karaoke_PausePlay_button);
		// pauseView.setPauseView(serverStatus.isPaused());
		pauseView.setOnPauseListener(new OnPauseListener() {
			@Override
			public void onPause(boolean isPlaying) {				
				if(MyApplication.intWifiRemote == MyApplication.SONCA){
					if (isPlaying) {
						((MyApplication)context).sendCommand(
								NetworkSocket.REMOTE_CMD_PLAY, 0);
					} else {
						((MyApplication)context).sendCommand(
								NetworkSocket.REMOTE_CMD_PAUSE, 0);
					}
				}else{
					MyLog.e(TAG, "pauseView : " + isPlaying);
					((MyApplication)context).sendCommandKartrol(
							(byte) RemoteIRCode.IRC_PAUSEPLAY,
							0);
				}
				
			}
			@Override
			public void OnInActive() {	
				
			}
			});
			
		nextView = (TouchNextView) view.findViewById(R.id.karaoke_next_button);
		nextView.setOnNextListener(new OnNextListener() {
			@Override
			public void onNext() {	
				longTimeExit = System.currentTimeMillis();
				
					if(MyApplication.intWifiRemote == MyApplication.SONCA){
						((MyApplication)context).sendCommand(
							NetworkSocket.REMOTE_CMD_NEXT, 0);
					}else{
						MyLog.e(TAG, "nextView : send ok");
						((MyApplication)context).sendCommandKartrol(
								(byte) RemoteIRCode.IRC_STOP,
								0);
					}
				
			}

			@Override
			public void OnInActive() {
				
			}
		});
		
//		downVideoView = (DownVideoView) view.findViewById(R.id.karaoke_Repeate_button);
//		downVideoView.setOnDownVideoViewListener(new OnDownVideoViewListener() {
//			
//			@Override
//			public void OnDownVideo() {
//				pingTime = System.currentTimeMillis();
//				InterruptPingServerTask tTask = new InterruptPingServerTask();
//				tTask.setFlagPingVideo(true);
//				tTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//				
//				startPingTocServer(99);
//			}
//		});
		
		btnSinger = (TouchSingerView) view.findViewById(R.id.SingerView);
		// btnSinger.setSingerView(serverStatus.isSingerOn());
		btnSinger.setOnSingerListener(new OnSingerListener() {

			@Override
			public void onSinger(boolean isSingerOn) {				
				if(MyApplication.intWifiRemote == MyApplication.SONCA){
					((MyApplication)context).sendCommand(
						NetworkSocket.REMOTE_CMD_SINGER, isSingerOn ? 1 : 0);
				}else{
					MyLog.e(TAG, "btnSigner : " + isSingerOn);
					((MyApplication)context).sendCommandKartrol(
							(byte) RemoteIRCode.IRC_MELODY,
							0);
				}
			}

			@Override
			public void OnInActive() {				
			}
		});
		
		//txtNote=(TextView) view.findViewById(R.id.txtKaraokeNoTe);
		
		mTopLine = (MagicTextView) view.findViewById(R.id.activity_karaoke_texttopline);
		
		mTopColorLine = (SentenceTextView) view.findViewById(R.id.activity_karaoke_colortopline);		

		mBottomLine = (MagicTextView) view.findViewById(R.id.activity_karaoke_textbottomline);		

		mBottomColorLine = (SentenceTextView) view.findViewById(R.id.activity_karaoke_colorbottomline);
		
		mText1Line = (MagicTextView) view.findViewById(R.id.activity_karaoke_text1);
		mtxtTitleSong = (MagicTextView) view.findViewById(R.id.activity_karaoke_txtTitleSong);
		mtxtAuthorSong = (MagicTextView) view.findViewById(R.id.activity_karaoke_txtAuthorSong);
		mImageCounter = (ImageView) view.findViewById(R.id.activity_karaoke_counter_image);
		mIconSingerTop= (ImageView) view.findViewById(R.id.activity_karaoke_IconTop);
		mIconSingerBot= (ImageView) view.findViewById(R.id.activity_karaoke_IconBot);
		// Font
		initFontColor();

		// Screen
		setDisplayFontForLanguage();
		setTextSizeFitScreen();
		FixSizeBackGroundLyric();
		
//		mTopLine.setText("line top 1");
//		mTopColorLine.setText("line top 2");
//		mBottomLine.setText("line Bottom 1");
//		mBottomColorLine.setText("line Bottom 2");
//		mImageCounter.setImageResource(R.drawable.ic_red_counter3);
//		mIconSingerTop.setImageResource(R.drawable.playkaraokeman);
//		mIconSingerBot.setImageResource(R.drawable.playkaraokewoman);
	progressBar=(RelativeLayout)view.findViewById(R.id.loadingPanel);
	}
	
	private void OnCheckVideo() {
		try {
			String rootPath = android.os.Environment.getExternalStorageDirectory().toString().concat("/SONCA");
			  
			  File parentFolder = new File(rootPath.concat("/VIDEO"));

			  if(!parentFolder.exists()){
				  parentFolder.mkdirs();
			  }
			  
//			  int fileCount = parentFolder.listFiles().length;
//			  if (fileCount == 0) {
//				  new Handler().postDelayed(new Runnable() {
//						@Override
//						public void run() {
//							pingTime = System.currentTimeMillis();
//							InterruptPingServerTask tTask = new InterruptPingServerTask();
//							tTask.setFlagPingVideo(true);
//							tTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//							
//							startPingTocServer(99);
//						}
//					}, 100);
//				 
//			  }	
		} catch (Exception e) {
			
		}
		
		
	}
	
	private void initFontColor() {
		try {
			int selectedColor = 0;
			
//			colorPanBody  = new int[] { Color.rgb(255, 255, 0), Color.rgb(0, 255, 0) }; // man: yellow, woman: pink
//			colorShowBorder = Color.rgb(0, 0, 160);// dark blue
//			colorShowBody = Color.rgb(0, 0, 255);// blue
//			colorPanBorder = Color.rgb(255, 255, 255); // white

			switch (selectedColor) {
			case 0:
				colorPanBody = new int[] { Color.rgb(0, 0, 255), Color.rgb(255, 0, 255), Color.rgb(255, 128, 0) }; // nam: blue, nu: pink
				colorShowBorder = Color.rgb(0, 0, 0);// den
				colorShowBody  = Color.rgb(255, 255, 255); // trang
				colorPanBorder = Color.rgb(255, 255, 255);// trang
				break;
//			case 1:
//				colorPanBody  = new int[] { Color.rgb(0, 128, 0), Color.rgb(255, 0, 128) , Color.rgb(142, 0, 255)}; // nam: xanh dam, nu: hong
//				colorShowBorder = Color.rgb(255, 255, 255);// trang
//				colorShowBody = Color.rgb(255, 255, 0); // yellow
//				colorPanBorder = Color.rgb(255, 0, 0); // red
//				break;
//			case 2:
//				colorPanBody  = new int[] { Color.rgb(0, 255, 255), Color.rgb(255, 128, 255), Color.rgb(142, 0, 255) }; // nam: xanh ngoc, nu: hong
//				colorShowBorder = Color.rgb(0, 0, 0);// den
//				colorShowBody = Color.rgb(255, 0, 0); // red
//				colorPanBorder = Color.rgb(255, 255, 255);// trang
//				break;
			}
		} catch (Exception ex) {			
			MyLog.e(TAG, ex);
		}
	}

	private void setDisplayFontForLanguage() {
		try {
			String fontLyric = "ROBOTOBLACK_new.ttf";
			Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/" + fontLyric);
			
			setFontLyrics(tf, mTopLine, colorShowBorder, 14, Typeface.BOLD);
			setFontLyrics(tf, mBottomLine, colorShowBorder, 14, Typeface.BOLD);
			setFontLyrics(tf, mTopColorLine, colorPanBorder, 14, Typeface.BOLD);
			setFontLyrics(tf, mBottomColorLine, colorPanBorder, 14, Typeface.BOLD);
			
			fontLyric = "ROBOTOBLACK.ttf";			
			tf = Typeface.createFromAsset(context.getAssets(), "fonts/" + fontLyric);
			setFontLyrics(tf, mText1Line, colorPanBorder, 14, Typeface.NORMAL);
			setFontLyrics(tf, mtxtTitleSong, colorPanBorder, 14, Typeface.NORMAL);
			setFontLyrics(tf, mtxtAuthorSong, colorPanBorder, 14, Typeface.NORMAL);
		} catch (Exception ex) {
			MyLog.e(TAG, ex);
		}
	}

	private void setFontLyrics(Typeface tf, TextView tv, int borderColor, int padding, int italic) {
		try {
			int padLeft = 0;
			int padTop = 6;
			int padRight = padLeft;
			int padBottom = padTop;

			tv.setTypeface(tf, italic);
			tv.setPadding(padLeft, padTop, padRight, padBottom);

		} catch (Exception ex) {
			MyLog.e(TAG, ex);
		}
	}
	
	private void setTextSizeFitScreen() {
		try {
				mTopLine.setText(getString(R.string.activity_karaoke_sample_string));
				screenWidthInPixels = getMyActivity().getResources().getDisplayMetrics().widthPixels;
				for (int i = 1; i < 1000; i++) {
					mTopLine.setTextSize(i);
					mTopLine.measure(0, 0);
					if (mTopLine.getMeasuredWidth() >= (screenWidthInPixels * 75 / 100)) {
						maxWidthOfTextSize = i;
						break;
					}
				}
				
//				maxWidthOfTextSize *=1.2f;
				
				//MyLog.e(TAG, "=setTextSizeFitScreen=="+maxWidthOfTextSize);
				mTopColorLine.setTextSize(maxWidthOfTextSize);
				mBottomColorLine.setTextSize(maxWidthOfTextSize);
				mTopLine.setTextSize(maxWidthOfTextSize);
				mBottomLine.setTextSize(maxWidthOfTextSize);
				mTopLine.setText("");
				
				mText1Line.setTextSize(maxWidthOfTextSize);
				mtxtTitleSong.setTextSize(maxWidthOfTextSize);
				mtxtAuthorSong.setTextSize(maxWidthOfTextSize);
			
		} catch (Exception ex) {
			MyLog.e(TAG, ex);
		}
	}

	private void FixSizeBackGroundLyric() {
		try {
			int heightOfBackground = 0;
			int heightOfLine = 0;
			mTopLine.setText("A");
			mTopLine.measure(0, 0);
			heightOfLine = mTopLine.getMeasuredHeight();
			heightOfBackground += mTopLine.getMeasuredHeight();

			mBottomLine.setText("B");
			mBottomLine.measure(0, 0);
			heightOfBackground += mBottomLine.getMeasuredHeight();

			mTopLine.setText("");
			mBottomLine.setText("");
			
//			LayoutParams llp = (LayoutParams) mBottomLine.getLayoutParams();			
//		    llp.setMargins(0, -(heightOfLine / 5), 0, 0);
//		    mBottomLine.setLayoutParams(llp);
//		    
//		    llp = (LayoutParams) mBottomColorLine.getLayoutParams();			
//		    llp.setMargins(0, -(heightOfLine / 5), 0, 0);
//		    mBottomColorLine.setLayoutParams(llp);
			
		} catch (Exception ex) {
			MyLog.e(TAG, ex);
		}
	}
	
	private void loadLyricSong(int id, int intMediaType, int midiShifTime, int mainTypeABC, boolean forceLoadLyric) {
//		if(true){
//			getGreeting();
//			songNosupport();
//			return;
//		}
		
		flagBreakReceiveTime = false;
		
		MyLog.d(" ", " ");MyLog.d(" ", " ");MyLog.d(" ", " ");
		MyLog.d(TAG, "===loadLyricSong start0==id=" + id +"==mainTypeABC==" + mainTypeABC+"=intMediaType="+intMediaType);
		MyLog.d(" ", " ");MyLog.d(" ", " ");MyLog.d(" ", " ");
		cntCheckErrorWowza=0;
		stopTimer();
		stopTimerLyricInfo();
		iStartTimerVideo=false;
		iPrepare=false;
		idSongCurrent = id;
		intMediaTypeCurrent = intMediaType;
		intMidiShiftTimeCurrent = midiShifTime;
		intMainTypeABC = mainTypeABC;	
		if (id == 0){
//			if(iPlayVideoSamba){
//				playVideoDefault(null, false);
//			}
			return;	
		}
		Cursor cursor = DBInterface.DBGetSongNumberCursor(context, id + "", 0, 0,
				MEDIA_TYPE.ALL);
		if (cursor.moveToFirst()) {
			int showEnable = cursor.getInt(14);
			if (showEnable == 1) { // PIANO or DANCE
				MyLog.e(TAG, "PIANO KIA PIANO KIA");
				if(iPlayVideoSamba||!mVideoView.isPlaying()){
					playVideoDefault(null, false);
				}
				iId0 = true;
				checkShowGreeting(0);
				return;
			}
			id = cursor.getInt(0);
			int intExtraInfo = cursor.getInt(9);

			if ((intExtraInfo & 8) != 0) {
				iCoupleSinger = true;
			} else {
				iCoupleSinger = false;
			}

			if ((intExtraInfo & 0x80) != 0) {
				if ((intExtraInfo & 0x20) != 0) {
					iVocalSinger = true;
				} else {
					iVocalSinger = false;
				}
			}

			titleSong = cursor.getString(2);

			int intABC = cursor.getInt(8);
			if(mainTypeABC >= 0){
				intABC = mainTypeABC;
			}	
			
			String nameSinger = cursor.getString(10);
			String nameMusician = cursor.getString(12);
			MEDIA_TYPE mediaType=MEDIA_TYPE.values()[0];
			try{
			 mediaType = MEDIA_TYPE.values()[intMediaType];
			}catch(Exception e){}
			Song objSong = new Song();
			objSong.setId(id);
			objSong.setTypeABC(intABC);
			objSong.setMediaType(mediaType);
			objSong.setSinger(new Singer(nameSinger, nameSinger));
			objSong.setMusician(new Musician(nameMusician, nameMusician));
			
			curSong = objSong;

			if (iVocalSinger)
				btnSinger.setVisibility(View.VISIBLE);
			if (iFrishcheckVocalSinger) {
				btnSinger.setSingerView(iFrishcheckVocalSinger);
				iFrishcheckVocalSinger = false;
			}		

			if (!iFrishcheckPLay) {
				pausePlayKaraoke(iFrishcheckPLay);
				iFrishcheckPLay = true;
			}
			
			if (mediaType == MEDIA_TYPE.VIDEO || MyApplication.flagPlayingYouTube) {
				MyLog.d(TAG, "==mediaType is VIDEO=enableSamba="+MyApplication.enableSamba+"=flagPlayingYouTube="+MyApplication.flagPlayingYouTube
						+"=intSvrModel="+MyApplication.intSvrModel+"=mediaType="+mediaType);				
				if(MyApplication.intSvrModel ==MyApplication.SONCA_SMARTK  && MyApplication.enableSamba){
					((MyApplication)context).getLyricVidLink();
					((MyApplication)context).setOnReceiverLyricVidLinkListener(new OnReceiverLyricVidLinkListener() {
						
						@Override
						public void OnReceiverLyricVidLink(String resultString) {
							// TODO Auto-generated method stub
							//String str="smb://;root:root@192.168.10.82/HDD/"+resultString.substring(26);
							
							MyLog.d(TAG, "=OnReceiverLyricVidLink=="+resultString);
							
							if(resultString == null || resultString.equals("")){
								resultString = "";										
								songNosupport();
							}else{
								if(resultString.startsWith("http")){
									MyLog.e(" ", " ");MyLog.e(" ", " ");
									MyLog.e(TAG, "SPECIAL WOWZA LINK");
									MyLog.e(" ", " ");MyLog.e(" ", " ");
									//songNosupport();
									/*String[]arr=resultString.split("[&]");
									
									String pathEdit="";
									for(int i=0;i<arr.length;i++){
										if(arr[i].startsWith("wowzaplaystart")){											
											String[]arr1=arr[i].split("[=]");
											arr[i]=arr1[0]+"="+(Long.parseLong(arr1[1])+2000);
										}
										if(i==(arr.length-1))
											pathEdit+=arr[i];
										else
											pathEdit+=arr[i]+"&";
									}*/

//									flag801 = true;
									TIMEPLUSVIDEO = 7000;
									iPlayVideoSamba=true;
									playVideoDefault(resultString,true);
									//playVideoDefault(pathEdit,true);
									displayTitleWithMode(false);
									mText1Line.setVisibility(View.GONE);
									lyricBack.setCurrectSong(titleSong);
									lyricBack.setSaveSong(curSong);
									lyricBack.setVisibility(View.VISIBLE);
									mTopLine.setText("");
									mBottomLine.setText("");
									activity_karaoke_Control.setVisibility(View.VISIBLE);
									startTimerLyricInfo(PeriodGetTime);
									return;
								}
								
								if(resultString.contains("emulated")){
									resultString = "";										
									songNosupport();
								} else {
									String str="smb://;root:root@"+IpDevice+"/HDD/"+resultString.substring(26);
									iPlayVideoSamba=true;
									String path=samba.getUrl(str, true);
									playVideoDefault(path,true);
									displayTitleWithMode(false);
									mText1Line.setVisibility(View.GONE);
									lyricBack.setCurrectSong(titleSong);
									lyricBack.setSaveSong(curSong);
									lyricBack.setVisibility(View.VISIBLE);
									mTopLine.setText("");
									mBottomLine.setText("");
									activity_karaoke_Control.setVisibility(View.VISIBLE);
									startTimerLyricInfo(PeriodGetTime);
								}
								
			}
						}
					});
				}else{
					songNosupport();					
			
				}
				return;	
				
			}
			if(iPlayVideoSamba||!mVideoView.isPlaying()){
				playVideoDefault(null, false);
			}
			
			lyricBack.setSaveSong(objSong);

			if(forceLoadLyric == false){
				// CHECK SPECIAL CASE
				if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
					
					if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
						if(ktvMainActivity != null){
							ktvMainActivity.callDownloadFileMidiFromPlayer(idSongCurrent);
							return;
						}
					} else {
						if(mainActivity != null){
							mainActivity.callDownloadFileMidiFromPlayer(idSongCurrent);
							return;
						}
					}
					
					
				}	
			}
			
			MyLog.d(TAG, "===loadLyricSong start==id=" + id + "=iCoupleSinger="
					+ iCoupleSinger + "=mediaType=" + mediaType
					+ "=iVocalSinger=" + iVocalSinger + "=intExtraInfo="
					+ intExtraInfo + "=mediaType=" + mediaType + "=intABC="
					+ intABC + "=intMidiShiftTimeCurrent="+intMidiShiftTimeCurrent);
			LoadColorLyric loadLyric = new LoadColorLyric(context, this
					.getActivity().getWindow());		
			
			// if(id==803175)
			// loadLyric.setData(objSong , 0,false);
			// else
			loadLyric.setData(objSong, 0, true, midiShifTime);
			loadLyric.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

			loadLyric
					.setOnLoadColorLyricListener(new OnLoadColorLyricListener() {

						@Override
						public void OnReceiveColorData(Lyrics lyricItems) {
							if(idSongCurrent == 0){
								checkShowGreeting(lyricBack.getSumSong());
								return;
							}
							if (lyricItems != null) {
								setDisplayControl(true);
								mLyricItems = lyricItems;
								// MyLog.d(TAG,"=LyricView="+
								// mLyricItems.getLyricView()+"=CountStart="+
								// mLyricItems.getLastSentences(0)
								// +"=Sentence="+
								// mLyricItems.getSentenceFromPosition(0)+"=getSongAuthor="+
								// mLyricItems.getSongAuthor()+"=getSongSinger="+
								// mLyricItems.getSongSinger()+
								// "=getSongTitle="+
								// mLyricItems.getSongTitle()+"=CountStart="+
								// mLyricItems.getCountStartFromPosition(0));
								if (iPlayMP3)
									mHandlerInitMp3.sendEmptyMessage(0);
								else {
									playKaraoke1();
									MyLog.d(TAG, "==iFrishcheckPLay=="
											+ iFrishcheckPLay);
									displayTitle(0, 15000);
								}
							} else {
								MyLog.d(TAG, "==OnReceiveColorData NULL==");
								songNosupport();
							}
						}
					});

		} else {
			if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && MyApplication.flagSmartK_CB ){
				ArrayList<Song> songsYoutube = DBInterface.DBSearchSongID_YouTube(id+"", "0", (MyApplication)context);
				if (!songsYoutube.isEmpty()) {
					MyLog.d(TAG, "=search list youtube=="+songsYoutube.get(0));
					//listReturn.add(songsYoutube.get(0));
					((MyApplication)context).getLyricVidLink();
					((MyApplication)context).setOnReceiverLyricVidLinkListener(new OnReceiverLyricVidLinkListener() {
						
						@Override
						public void OnReceiverLyricVidLink(String resultString) {
							// TODO Auto-generated method stub
							//String str="smb://;root:root@192.168.10.82/HDD/"+resultString.substring(26);
							
							MyLog.d(TAG, "=OnReceiverLyricVidLink=="+resultString);
							
							if(resultString == null || resultString.equals("")){
								resultString = "";										
								songNosupport();
							}else{
								if(resultString.startsWith("http")){
									MyLog.e(" ", " ");MyLog.e(" ", " ");
									MyLog.e(TAG, "SPECIAL WOWZA LINK");
									MyLog.e(" ", " ");MyLog.e(" ", " ");
									//songNosupport();
									/*String[]arr=resultString.split("[&]");
									
									String pathEdit="";
									for(int i=0;i<arr.length;i++){
										if(arr[i].startsWith("wowzaplaystart")){											
											String[]arr1=arr[i].split("[=]");
											arr[i]=arr1[0]+"="+(Long.parseLong(arr1[1])+2000);
										}
										if(i==(arr.length-1))
											pathEdit+=arr[i];
										else
											pathEdit+=arr[i]+"&";
									}*/
									
//									flag801 = true;
									TIMEPLUSVIDEO = 7000;									
									iPlayVideoSamba=true;
									playVideoDefault(resultString,true);
									displayTitleWithMode(false);
									mText1Line.setVisibility(View.GONE);
									lyricBack.setCurrectSong(titleSong);
									lyricBack.setSaveSong(curSong);
									lyricBack.setVisibility(View.VISIBLE);
									mTopLine.setText("");
									mBottomLine.setText("");
									activity_karaoke_Control.setVisibility(View.VISIBLE);
									startTimerLyricInfo(PeriodGetTime);
									return;
								}else{
									MyLog.d(TAG, "==search list youtube No Song==");
									songNosupport();
								}
								
							}
						}
					});
				} else {					
					MyLog.d(TAG, "==search list youtube No Song==");
					songNosupport();
				}
			}else{
				MyLog.d(TAG, "==OnReceiveColorData No Song==");
				songNosupport();	
			}
		}
	}

	private void songNosupport(){	
		MyLog.d(TAG,"==songNosupport=titleSong="+titleSong);
		if(iPlayVideoSamba||!mVideoView.isPlaying()){
			playVideoDefault(null, false);
		}
			mText1Line.setText(getMyActivity().getResources().getString(R.string.activity_karaoke_noLyric));
			mText1Line.setVisibility(View.VISIBLE);
			if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
				if(MyApplication.flagPlayingYouTube){
					if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
						titleSong = ktvMainActivity.getCurrentSongName();
					} else {
						titleSong = mainActivity.getCurrentSongName();	
					}
					
				}
				
			}	
			lyricBack.setCurrectSong(titleSong);
			lyricBack.setSaveSong(curSong);
			lyricBack.setVisibility(View.VISIBLE);
			mTopLine.setText("");
			mBottomLine.setText("");
			activity_karaoke_Control.setVisibility(View.VISIBLE);
					

			
			//setDisplayControl(false);			
		}

	private void setDisplayControl(boolean iControl){
		MyLog.d(TAG, "===setDisplayControl=="+iControl);
		if(iControl){
			lyricBack.setCurrectSong(titleSong);
			//activity_karaoke_Control.setVisibility(View.VISIBLE);
			//txtNote.setVisibility(View.INVISIBLE);
//			repeatView.setVisibility(View.VISIBLE);
//			pauseView.setVisibility(View.VISIBLE);
//			nextView.setVisibility(View.VISIBLE);
			checkShowGreeting(1);
		}else{
			
			activity_karaoke_Control.setVisibility(View.INVISIBLE);
			btnSinger.setVisibility(View.INVISIBLE);
			//lyricBack.setTitleView(mainActivity.getResources().getString(R.string.activity_karaoke_noLyric));
			//txtNote.setVisibility(View.VISIBLE);
//			repeatView.setVisibility(View.INVISIBLE);
//			pauseView.setVisibility(View.INVISIBLE);
//			nextView.setVisibility(View.INVISIBLE);
			iFrishcheckPLay=true;
			iFrishcheckVocalSinger=false;	
		}
	}
	
	private boolean playKaraoke(MyPlayer mediaPlayer) {
		try {
			MyLog.d(TAG, "===playKaraoke start=="+mPlayer);			
			
			mLyricItems.prepare(mTopLine);
			lyricDisplayStart();
			
			mediaPlayer.play();
			MyLog.d(TAG, "===playKaraoke end==");
			return true;
		} catch (Exception ex) {
			MyLog.e(TAG, ex);
			return false;
		}
	}
	
	private boolean playKaraoke1() {
		try {
			MyLog.d(TAG, "===playKaraoke1 start=mPlayer="+mPlayer);			
			
			mLyricItems.prepare(mTopLine);
			lyricDisplayStart();			
			return true;
		} catch (Exception ex) {
			MyLog.e(TAG, ex);
			return false;
		}
	}

	private void stopPlayer() {
		if (mPlayer != null) {
			try {
				mPlayer.stop(false);
			} catch (Exception ex) {
				MyLog.e(TAG, ex);
				// Error ignore, not serious
			}
		}
	}
	public void stopKaraoke() {
		try {	
			MyLog.d(TAG, "====stopKaraoke====");
			clear2Lines(0);	
			stopPlayer();
			stopService();
			stopTimerLyricInfo();					
			displayCountdown("");
			resetVariant();
			//isStartingKaraoke = false;
			//timerPlayer=0;
			if (mSong != null)
				mSong = null;
			if (mImageCounter != null)
				mImageCounter.setImageDrawable(null);
			
			System.gc();
		} catch (Exception ex) {
			MyLog.e(TAG, ex);
		}
	}
	
	private void resetVariant() {
		hideScoreDialog();
		isStarted = true;
		currentLyricBlock = 0;
		bIsShowLastLine = false;
		bIsShowPanLine = false;
		MyLog.i(TAG, "=resetVariant===");
		sentence1 = null;
		sentence2 = null;
		lastSentence = null;
		isChangeSentence = false;
		timeLoopChangeColor=0;
		 timeLoopService=0;
		timeStartCount=0;
		savePauseTime=0;
		saveTimePlayerCurrentInMsec=0;
		isStartingKaraoke = false;
		timerPlayer=0;
		curLyricMaxWidth = 0;
		curLyricSize = 0;
		saveCurLyricSize = 0;
		curLyricSizePanDone = 0;
		curCntdownWord = "";
		bIsEndBlock = false;
		bIsLastWordOfLine = false;
		timerCounter = 0;	
		OldtimerCounter=0;
		//iRepeat= 0;
		cntIconSinger=0;
		currentIconSinger="";
		setDisplayControl(false);
		iStart=false;
		iVocalSinger=false;
		lyricBack.setPlayPause(true);
		displayTitle(0, 0);		
		mText1Line.setVisibility(View.INVISIBLE);
		dismisLoading();
	}

	private void stopService() {
		try {
			stopTimer();

			if (mHandleChangeColor != null)
				mHandleChangeColor.removeCallbacks(mRunnableChangeColor);

			if (mHandleServiceChangeLine != null)
				mHandleServiceChangeLine.removeCallbacks(mRunnableChangeLine);

			isChangeSentence = false;

			if (mTopLine != null)
				mTopLine.removeCallbacks(null);

			if (mBottomLine != null)
				mBottomLine.removeCallbacks(null);

			if (mTopColorLine != null)
				mTopColorLine.removeCallbacks(null);

			if (mBottomColorLine != null)
				mBottomColorLine.removeCallbacks(null);

			sentence1 = null;
			sentence2 = null;
			lastSentence = null;
		} catch (Exception ex) {
			MyLog.e(TAG, ex);
		}
	}
	
	private void clear2Lines(int delay) {
		try {
			if (delay == 0) // clear immediately
			{
				mTopLine.post(new Runnable() {
					@Override
					public void run() {
						try {
							MyLog.d(TAG, "==clear2Lines==");

							if (mTopLine != null)
								mTopLine.setText("");

							if (mTopColorLine != null) {
								mTopColorLine.setText("");
								mTopColorLine.getLayoutParams().width = mTopLine
										.getLayoutParams().width;
							}

							if (mBottomLine != null)
								mBottomLine.setText("");

							if (mBottomColorLine != null) {
								mBottomColorLine.setText("");
								mBottomColorLine.getLayoutParams().width = mBottomLine
										.getLayoutParams().width;
							}

							if (mIconSingerTop != null)
								mIconSingerTop.setImageDrawable(null);
							if (mIconSingerBot != null)
								mIconSingerBot.setImageDrawable(null);
						} catch (Exception ex) {
							MyLog.e(TAG, ex);
						}
					}
				});
			}
		} catch (Exception ex) {
			MyLog.e(TAG, ex);
		}
	}
	
	private void displayCountdown(String curWord) {
		try {
			final String CntdownWord = curWord;
			isStartingKaraoke = !CntdownWord.equalsIgnoreCase("");
			mTopLine.post(new Runnable() {
				@Override public void run() {
					//MyLog.d(TAG, "==displayCountdown=isStartingKaraoke="+isStartingKaraoke+"==CntdownWord="+CntdownWord);
					if (CntdownWord.equalsIgnoreCase("3")) {
						mImageCounter.setImageResource(R.drawable.ic_red_counter3);
					} else if (CntdownWord.equalsIgnoreCase("2")) {
						mImageCounter.setImageResource(R.drawable.ic_yellow_counter2);
					} else if (CntdownWord.equalsIgnoreCase("1")) {
						mImageCounter.setImageResource(R.drawable.ic_blue_counter1);
					}
					if (isStartingKaraoke) {
						// TODO counter 123 sat line 1
						LayoutParams param = (LayoutParams) mImageCounter.getLayoutParams();
						if (param != null) {
							int left = 0;
							if (sentence1 != null)
								left = screenWidthInPixels * SPACE_PERCENT_RIGHT / 100;
							
//							mImageCounter.measure(mImageCounter.getHeight(), mImageCounter.getHeight());
//							left -= mImageCounter.getMeasuredWidth();
							
							int newHeight = mTopLine.getHeight() * 2 / 3;
							param.height = newHeight;
							param.width = newHeight;							
							
							param.setMargins(left, 0, mTopLine.getHeight() / 5, 0);
							mImageCounter.setLayoutParams(param);
//							if (sentence1 != null && sentence2 != null){
//								//MyLog.d(TAG, "==countdown here====sentence1="+sentence1.getSentence()+"=sentence2="+sentence2.getSentence());
//								mTopLine.setText(sentence1.getSentence());
//								mBottomLine.setText(sentence2.getSentence());
//							}
						}
					} else {
						mImageCounter.setImageDrawable(null);
					}
				}
			});
		} catch (Exception ex) {
			MyLog.d(TAG, "==displayCountdown=fail=");
			
			MyLog.e(TAG, ex);
		}
	}
		
	private void displayIconSinger(int line, int type, String singerType) {
//		MyLog.d(TAG, "=displayIconSinger=line=" + line + "=type=" + type
//				+ "=singerType=" + singerType+"=cntIconSinger="+cntIconSinger
//				+"=currentIconSinger="+currentIconSinger+"=curCntdownWord="+curCntdownWord
//				);
				
//		MyLog.d("displayIconSinger", "line = " + line);
//		if(line == 0){
//			MyLog.d(" ", "mTopLine = " + mTopLine.getText());			
//		}
//		if(line == 1){
//			MyLog.d(" ", "mBottomLine = " + mBottomLine.getText());			
//		}
//		MyLog.d(" ", "singerType = " + singerType);
//		MyLog.d(" ", "currentIconSinger = " + currentIconSinger);
//		MyLog.d(" ", "cntIconSinger = " + cntIconSinger);

//		if (mIconSingerBot != null)
//			mIconSingerBot.setImageDrawable(null);
//		if (mIconSingerTop != null)
//			mIconSingerTop.setImageDrawable(null);	
		if(iCoupleSinger){
		if (singerType.equals("LINW") || singerType.equals("LINM")|| singerType.equals("LINA")) {			
			
			if(!currentIconSinger.equals(singerType)){
				currentIconSinger=singerType;
				cntIconSinger=1;
			}else{
				if(!curCntdownWord.equals("")){
					if(curCntdownWord.equals("4")||curCntdownWord.equals("3")||curCntdownWord.equals("2")){
						if (mIconSingerBot != null);
							mIconSingerBot.setImageDrawable(null);						
						line = 0;
					}else{
					cntIconSinger++;
					if (line == 1){						
						if (mIconSingerBot != null)
							mIconSingerBot.setImageDrawable(null);
						if(cntIconSinger>2 && mIconSingerTop != null)
							mIconSingerTop.setImageDrawable(null);							
					}else if (line == 0){						
						if (mIconSingerTop != null)
							mIconSingerTop.setImageDrawable(null);						
						if(cntIconSinger>2 && mIconSingerBot != null)
							mIconSingerBot.setImageDrawable(null);
					}
					return;
					}
				}else{
					//cntIconSinger++;
					if (line == 1){							
							if (mIconSingerTop != null)
								mIconSingerTop.setImageDrawable(null);							
					}else if (line == 0){		
						if (mIconSingerBot != null)
							mIconSingerBot.setImageDrawable(null);						
					}
					//return;
				}
			}
			
			if (cntIconSinger == 1) {
				if (line == 0) {
//				
					if (type == SentenceSong.TYPE_MAN)
						mIconSingerTop
								.setImageResource(R.drawable.playkaraokeman);
					else if (type == SentenceSong.TYPE_WOMAN)
						mIconSingerTop
								.setImageResource(R.drawable.playkaraokewoman);
					else if (type == SentenceSong.TYPE_ALL)
						mIconSingerTop
								.setImageResource(R.drawable.playkaraokeall);

					LayoutParams param = (LayoutParams) mIconSingerTop.getLayoutParams();
					if (param != null) {
						if (sentence1 != null){
							int left = 0;
							int newHeight = mTopLine.getHeight() * 2 / 3;							
							
							left = newHeight;
							
							param.height = newHeight;
							param.width = newHeight;
							if (type == SentenceSong.TYPE_ALL){
								param.width = newHeight * 2; 
								left = newHeight * 2; 
							}		
							
							left += 5;		
							
							param.setMargins(screenWidthInPixels * SPACE_PERCENT_RIGHT / 100, (mTopLine.getHeight() / 5), 0, 0);
							
							mIconSingerTop.setLayoutParams(param);
							
							RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) mTopLine.getLayoutParams();						
							int left2 = screenWidthInPixels * SPACE_PERCENT_RIGHT / 100 + left;		
							params2.setMargins(left2, 0, 0, 0);
							mTopLine.setLayoutParams(params2);	
							
							params2 = (RelativeLayout.LayoutParams) mTopColorLine.getLayoutParams();		
							params2.setMargins(left2, 0, 0, 0);
							mTopColorLine.setLayoutParams(params2);	
						}						
						
					}
				} else if (line == 1) {				
//					
					if (type == SentenceSong.TYPE_MAN)
						mIconSingerBot
								.setImageResource(R.drawable.playkaraokeman);
					else if (type == SentenceSong.TYPE_WOMAN)
						mIconSingerBot
								.setImageResource(R.drawable.playkaraokewoman);
					else if (type == SentenceSong.TYPE_ALL)
						mIconSingerBot
								.setImageResource(R.drawable.playkaraokeall);

					LayoutParams param = (LayoutParams) mIconSingerBot.getLayoutParams();
					if (param != null) {
						if (sentence2 != null){
							int left = 0;
							int newHeight = mBottomLine.getHeight() * 2 / 3;							
							
							left = newHeight;
							
							param.height = newHeight;
							param.width = newHeight;
							if (type == SentenceSong.TYPE_ALL){
								param.width = newHeight * 2; 
								left = newHeight * 2; 
							}					
							
							left += 5;
							
							param.setMargins(-left, (mBottomLine.getHeight() / 5), 0, 0);
							mIconSingerBot.setLayoutParams(param);
						}		
					}
				}
			}
		} 
//		else {
//			if (line == 0)
//				mIconSingerTop.setImageDrawable(null);
//			else if (line == 1)
//				mIconSingerBot.setImageDrawable(null);
//		}
		}
	}
	
	private int getWidthScreen() {
		try {
			return getMyActivity().getResources().getDisplayMetrics().widthPixels;
		} catch (Exception ex) {
			MyLog.e(TAG, ex);
			return 0;
		}
	}
	
	private void lyricDisplayStart() {
		try {
			MyLog.d(TAG, "=lyricDisplayStart==");
			currentLyricBlock = 0;
			curLyricMaxWidth = 0;
			curLyricSize = 0;
			iId0=false;
			isStartingKaraoke = false;
			curCntdownWord = "";
			mText1Line.setVisibility(View.INVISIBLE);
			progressBar.setVisibility(View.GONE);
//			timeLoopChangeColor = System.currentTimeMillis();
//			timeLoopService = timeLoopChangeColor;
//			timeStartCount = timeLoopChangeColor;

			savePauseTime = 0;
			saveTimePlayerCurrentInMsec = 0;
			isChangeSentence = true;
			bIsShowPanLine = false;
			mTopLine.setText("");
			mTopColorLine.setText("");

			mBottomLine.setText("");
			mBottomColorLine.setText("");
			OldtimerCounter=0;
			iRepeat=0;
			startService();
		} catch (Exception ex) {
			MyLog.d(TAG, "=lyricDisplayStart fail==");
			MyLog.e(TAG, ex);
			
		}
	}
	
	private boolean iStart=false;
	private int cntDelay=0;
	private void startService() {
		try {
			MyLog.d(TAG, "=startService start==");
			iStart=true;
			startTimerLyricInfo(PeriodGetTime);
			//startTimer();
			//displayTitle(0, 20000);
			
		} catch (Exception ex) {
			MyLog.d(TAG, "=startService fail==");
			MyLog.e(TAG, ex);
		}
	}
	
	private void displayTitle(int showAfterMillis, int hideAfterMillis) {
		try {
			MyLog.d(TAG, "====displayTitle start===hideAfterMillis="+hideAfterMillis+"=showAfterMillis="+showAfterMillis);
			if (hideAfterMillis > showAfterMillis) {
				// show
				mTopLine.postDelayed(new Runnable() {
					@Override public void run() {
						displayTitleWithMode(true);
					}
				}, showAfterMillis);

				// hide
				if (hideAfterMillis > showAfterMillis) {
					mTopLine.postDelayed(new Runnable() {
						@Override public void run() {
							displayTitleWithMode(false);
						}
					}, hideAfterMillis);
				}
			} else {
				mTopLine.post(new Runnable() {
					@Override public void run() {
						displayTitleWithMode(false);
					}
				});
			}
		} catch (Exception ex) {
			MyLog.d(TAG, "====displayTitle fail====");
			MyLog.e(TAG, ex);
		}
	}

	private void startTimer() {
		try {
			MyLog.d(TAG, "====startTimer start====");
			stopTimer();

			timerService = new Timer();
			timerService.scheduleAtFixedRate(new TimerTask() {
				public void run() {
					timerCounter++;
					if (timerCounter < 0 || timerCounter > LOOP_MAX_TIMES) {
						timerCounter = 0;
						mHandleServiceChangeLine.post(mRunnableChangeLine);
					}
					mHandleChangeColor.post(mRunnableChangeColor);
				}
			}, 0, LOOP_PERIOD);
		} catch (Exception ex) {
			MyLog.d(TAG, "====startTimer fail====");
			MyLog.e(TAG, ex);
		}
	}
	Boolean iStartTimerVideo=false;
	Boolean iPrepare=false;
	long time=0;
	private void startTimerVideo(final int currentTime, int current) {
		try {
			//MyLog.d(TAG, "=startTimerVideo start=currentTime="+currentTime+"=current="+current+"=isPlaying="+mVideoView.isPlaying()+"=iStartTimerVideo="+iStartTimerVideo);
			
			if(Math.abs(getCurrentPositionVideo()-current) <TIMECheckPause){
				//MyLog.d(TAG, "=startTimerVideo 1=");
			 if(iStartTimerVideo){	 
				 stopTimer();
					iStartTimerVideo=false;																											
					dismisLoading();
			 }
			 if(pauseView.getPauseView())
					resumeVideo();	
			 return;
		 }
			iStartTimerVideo=true;
			stopTimer();
			pauseVideo();
			//mVideoView.pause();
			//MyLog.d(TAG, "=startTimerVideo 2=");
			timerCounter=current+120;		
			 time=System.currentTimeMillis();
			timerService = new Timer();
			timerService.scheduleAtFixedRate(new TimerTask() {
				public void run() {
					
					//mTopLine.post(new Runnable() {
					//	public void run() {		
					
							 long t1=System.currentTimeMillis();
							 timerCounter+=t1-time;
							
							//timerCounter+=LOOP_MAX_TIMES;
							//MyLog.d(TAG, "=startTimerVideo start1=timerCounter="+timerCounter+"=="+mVideoView.isPlaying()+"=t1="+t1+"=time="+time);
							time=t1;
							if(timerCounter>=currentTime){	
								//MyLog.d(TAG, "====startTimerVideo end=timerCounter="+timerCounter+"=currentTime="+getCurrentPositionVideo());
						iStartTimerVideo=false;
								stopTimer();
						if(pauseView.getPauseView())
							mVideoView.start(); 
						//mVideoView.resume();
								
						dismisLoading();
						//MyLog.d(TAG, "====startTimerVideo end1=="+getCurrentPositionVideo());
						
					}
					//	}
					//});
					
					
				}
			}, 0, LOOP_MAX_TIMES);

		} catch (Exception ex) {
			MyLog.d(TAG, "====startTimer fail====");
			MyLog.e(TAG, ex);
		}
	}
	private void stopTimer() {
		try {
			if (timerService != null) {
				timerService.cancel(); // stop timer
				timerService.purge();
				timerService = null;
			}
		} catch (Exception ex) {
			MyLog.e(TAG, ex);
		}
	}
	
	
	private void displayTitleWithMode(boolean bIsShow) {
		try {
//			MyLog.d(TAG, "=displayTitleWithMode=bIsShow="+bIsShow+"=Title="+mLyricItems.getSongTitle()+"=Author="+mLyricItems.getSongAuthor());
			if (bIsShow) {
				mtxtTitleSong.setText(mLyricItems.getSongTitle());				
				mtxtAuthorSong.setText(getMyActivity().getResources().getString(R.string.msg_kara_2) + " " +mLyricItems.getSongAuthor());
				activity_karaoke_InfoSong.setVisibility(View.VISIBLE);
				mText1Line.setVisibility(View.INVISIBLE);
				mTopColorLine.setText("");
				mBottomColorLine.setText("");
				mTopLine.setText("");
				mBottomLine.setText("");
				if(mImageCounter != null)
					mImageCounter.setImageDrawable(null);	
				if (mIconSingerTop != null)
					mIconSingerTop.setImageDrawable(null);
				if (mIconSingerBot != null)
					mIconSingerBot.setImageDrawable(null);
			}else{				
				activity_karaoke_InfoSong.setVisibility(View.INVISIBLE);
				mtxtTitleSong.setText("");				
				mtxtAuthorSong.setText("");
			}
		} catch (Exception ex) {
			MyLog.e(TAG, ex);
		}
	}
	
	private Handler mHandlerInitMp3 = new Handler(new Handler.Callback() {
		private void initMp3Song() {
			try {
				MyLog.d(TAG, "==initMp3Song===");
				if (mPlayer == null) {
					mPlayer = new MyPlayer(context);
				}

				boolean successful = mPlayer.initPlayer(audioUrl);
				
				if (successful == false) {
					MyLog.d(TAG, "==initMp3Song fail===");
					throw new Exception();
				}
			} catch (Exception ex) {
				MyLog.d(TAG, "==initMp3Song fail1===");
				MyLog.e(TAG, ex);				
			}
		}

		@Override public boolean handleMessage(Message msg) {
			try {
				initMp3Song();
			} catch (Exception ex) {
				MyLog.e(TAG, ex);
			}
			return false;
		}
	});
	
	private int GetCurrentTimeMillis(boolean bIsChangeColorService, boolean bIsSyncTimeWithMedia) {
		try {
			long timeReference = bIsChangeColorService ? timeLoopChangeColor : timeLoopService;
			int curTimeDateMillis = 0;
			long timePrev = timeReference;
			long timeNow = System.currentTimeMillis();
			//MyLog.d(TAG, "=GetCurrentTimeMillis0=timeNow="+timeNow+"=timeLoopChangeColor="+timeLoopChangeColor+"=timeLoopService="+timeLoopService+"=timeReference="+timeReference);
			if (timeNow > 0 && timePrev > 0 && timeNow > timePrev) {
				timeReference = timeNow;
				if (bIsChangeColorService)
					timeLoopChangeColor = timeReference;
				else
					timeLoopService = timeReference;

				curTimeDateMillis = (int) (timeReference - timeStartCount);
				//MyLog.d(TAG, "=GetCurrentTimeMillis1=curTimeDateMillis="+curTimeDateMillis+"=timeReference="+timeReference+"=timeStartCount="+timeStartCount+"=timerPlayer="+timerPlayer+"=cntDelay="+cntDelay);
				
				if (curTimeDateMillis < 0)
					curTimeDateMillis = 0;

				if (bIsSyncTimeWithMedia || bIsEndBlock || curLyricSize >= curLyricMaxWidth || savePauseTime != 0) {
					int curTimePlayerMillis=0;
					if(iPlayMP3)
						curTimePlayerMillis = mPlayer.getCurrentPlaybackPosition();
					else{

						/*if(MyApplication.intSvrModel == MyApplication.SONCA || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){

							curTimePlayerMillis =timerPlayer/90+cntDelay;// TODO SK9xxx/90->milsecon, # ko chia
						}else{
							curTimePlayerMillis =timerPlayer+cntDelay;
						}*/
						curTimePlayerMillis =timerPlayer+cntDelay;
					}
					if (saveTimePlayerCurrentInMsec != curTimePlayerMillis && curTimeDateMillis != curTimePlayerMillis) {
						curTimeDateMillis = curTimePlayerMillis;
						timeStartCount = timeReference - curTimeDateMillis;
						saveTimePlayerCurrentInMsec = curTimePlayerMillis;
						//MyLog.d(TAG, "=GetCurrentTimeMillis2=curTimeDateMillis="+curTimeDateMillis+"=timeReference="+timeReference+"=timeStartCount="+timeStartCount);
						
					}
					//MyLog.d(TAG, "=curTimeDateMillis="+curTimeDateMillis);
					
				}
			}
			return curTimeDateMillis;
		} catch (Exception ex) {
			MyLog.e(TAG, ex);
			return 0;
		}
	}

	private Handler mHandleServiceChangeLine = new Handler(new Handler.Callback() {
		@Override public boolean handleMessage(Message msg) {
			return false;
		}
	});

	private Runnable mRunnableChangeLine = new Runnable() {
		@Override public void run() {
			try {
				changeLine();
			} catch (Exception ex) {
				MyLog.e(TAG, ex);
			}
		}

		private void changeLine() {
			try {
				//MyLog.i(TAG, "=changeLine=isChangeSentence="+isChangeSentence);
				
				if (isChangeSentence) {
					final int currentPosition = GetCurrentTimeMillis(false, false);
					//MyLog.i(TAG, "=changeLine1=currentPosition="+currentPosition+"=savePauseTime="+savePauseTime);
					
					if (currentPosition == 0)
						return;
					if (savePauseTime != 0)
						return;

					final SentenceSong s = mLyricItems.getSentenceFromPosition(currentPosition);
					if (s == null || (currentPosition > s.getTimeStart() + s.getTimeLenght())) // Here is start																						// lyrics
					{
						WordSong wordsCnt = mLyricItems.getCountStartFromPosition(currentPosition);
						//MyLog.i(TAG, "=changeLine2=wordsCnt="+wordsCnt+"=s="+s);
						
						if (wordsCnt != null && (wordsCnt.getWord().equals("0") || wordsCnt.getWord().equals("00")) && bIsEndBlock == false) {
							//MyLog.i(TAG, "=changeLine2=wordsCnt="+wordsCnt.getWord()+"=bIsEndBlock="+bIsEndBlock+"=bIsShowLastLine="+bIsShowLastLine);
							bIsEndBlock = true;

							curLyricSize = 0;
							curLyricSizePanDone = 0;
							curLyricMaxWidth = 0;
							curCntdownWord = "";
							cntIconSinger=0;
							currentIconSinger="";
							bIsShowLastLine = false;
							bIsShowPanLine = false;
							//MyLog.i(TAG, "=here5="+mBottomColorLine.getText().toString());
							clear2Lines(0);
							if (wordsCnt.getWord().equals("0")) { // show title
								displayTitle(1000, 15000);
								//startTimerLyricInfo(1000);
							}else if (wordsCnt.getWord().equals("00")) { // show title
								//MyLog.i(TAG, "=changeLine3=iPlayMP3="+iPlayMP3);
								
								if(!iPlayMP3){
//									MyLog.d(TAG, "==timeStartCount="+timeStartCount
//										+"==timeLoopChangeColor="+timeLoopChangeColor+"==timeLoopService="+timeLoopService
//										+"=GetCurrentTimeMillis="+GetCurrentTimeMillis(true, true)+"=timerPlayer="+timerPlayer
//										);
									
									stopKaraoke();
									checknewSong();
								}
							}
						}
						return;
					}

					if (s != null && currentPosition >= s.getTimeStart()) {
						//MyLog.i(TAG, "=changeLine3=currentPosition="+currentPosition+"=s="+s.getLine());
						
						if (s.getLine() == 0) {
							mTopLine.post(new Runnable() {
								@Override public void run() {
									
									if (saveCurLyricSize == 0 || saveCurLyricSize != curLyricSize) {
										if (saveCurLyricSize == 0 && !bIsShowPanLine) {
											mTopColorLine.setText(s.getSentence());
											bIsShowPanLine = true;
											//MyLog.i(TAG, "=here1="+s.getSentence());
										}
										saveCurLyricSize = curLyricSize;
										if (saveCurLyricSize > 0){
											bIsShowPanLine = false;
											//MyLog.i(TAG, "=here2=");
										}
									}
									//MyLog.i(TAG, "=changeLine31=mTopLine="+mTopLine+"=bIsShowLastLine="+bIsShowLastLine);
									
									
									if ((sentence1 != s) || (!bIsShowLastLine && mLyricItems.isLastSentences(currentPosition))) {
										if (!bIsShowLastLine && mLyricItems.isLastSentences(currentPosition)) {
											bIsShowLastLine = true;
										}
										saveCurLyricSize = 0;
										curLyricSize = 0;
										curLyricSizePanDone = 0;
										sentence1 = s;
										mTopColorLine.getLayoutParams().width = 0;
										mTopColorLine.setTextColor(colorPanBody[sentence1.getType()]);
										mTopLine.setTextColor(colorShowBody);
										mTopLine.setText(s.getSentence());
										if(activity_karaoke_InfoSong.getVisibility()==View.VISIBLE)
											displayTitleWithMode(false);
										mTopLine.measure(0, 0);
										maxTopWidth = mTopLine.getMeasuredWidth();
										
										RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mTopLine.getLayoutParams();						
										int left = screenWidthInPixels * SPACE_PERCENT_RIGHT / 100;
										params.setMargins(left, 0, 0, 0);
										mTopLine.setLayoutParams(params);
										
										params = (RelativeLayout.LayoutParams) mTopColorLine.getLayoutParams();
										params.setMargins(left, 0, 0, 0);
										mTopColorLine.setLayoutParams(params);
										
										displayIconSinger(s.getLine(),sentence1.getType(),sentence1.getTypeSinger());
										//MyLog.i(TAG, "=changeLine4=getSentence="+s.getSentence()+"=getType="+sentence1.getType()+"=colorPanBody="+colorPanBody[sentence1.getType()]+"=getTypeSinger="+sentence1.getTypeSinger());
										
									}
								}
							});
						} else {
							mBottomLine.post(new Runnable() {
								@Override public void run() {
									//MyLog.d(TAG, "=changeline bot=saveCurLyricSize="+saveCurLyricSize+"=curLyricSize="+curLyricSize+"=bIsShowPanLine="+bIsShowPanLine);
									if (saveCurLyricSize == 0 || saveCurLyricSize != curLyricSize) {
										if (saveCurLyricSize == 0 && !bIsShowPanLine) {
											mBottomColorLine.setText(s.getSentence());
											bIsShowPanLine = true;
											//MyLog.i(TAG, "=here3="+mBottomColorLine.getText().toString());
										}
										saveCurLyricSize = curLyricSize;
										if (saveCurLyricSize > 0){
											bIsShowPanLine = false;
											//MyLog.i(TAG, "=here4="+mBottomColorLine.getText().toString());
										}
									}
									//MyLog.i(TAG, "=changeLine51=mBottomLine="+mBottomLine+"=bIsShowLastLine="+bIsShowLastLine);
									
									if ((sentence2 != s) || (!bIsShowLastLine && mLyricItems.isLastSentences(currentPosition))) {
										if (!bIsShowLastLine && mLyricItems.isLastSentences(currentPosition)) {
											bIsShowLastLine = true;
										}
										saveCurLyricSize = 0;
										curLyricSize = 0;
										curLyricSizePanDone = 0;
										sentence2 = s;
										mBottomColorLine.getLayoutParams().width = 0;
										mBottomColorLine.setTextColor(colorPanBody[sentence2.getType()]);
										mBottomLine.setTextColor(colorShowBody);
										mBottomLine.setText(s.getSentence());		
										if(activity_karaoke_InfoSong.getVisibility()==View.VISIBLE)
											displayTitleWithMode(false);
										mBottomLine.measure(0, 0);
										maxBottomWidth = mBottomLine.getMeasuredWidth();

										RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mBottomLine.getLayoutParams();
										int left = (int)(screenWidthInPixels - screenWidthInPixels * SPACE_PERCENT_RIGHT / 100 - mBottomLine.getMeasuredWidth());
										params.setMargins(left, 0, 0, 0);
										mBottomLine.setLayoutParams(params);	
										
										params = (RelativeLayout.LayoutParams) mBottomColorLine.getLayoutParams();
										params.setMargins(left, 0, 0, 0);
										mBottomColorLine.setLayoutParams(params);
										
										displayIconSinger(s.getLine(),sentence2.getType(),sentence2.getTypeSinger());
										//MyLog.i(TAG, "=changeLine5=getSentence="+s.getSentence()+"=getType="+sentence2.getType()+"=colorPanBody="+colorPanBody[sentence2.getType()]+"=getTypeSinger="+sentence2.getTypeSinger());
										
									}
								}
							});
						}
					}

					if (isStarted) {
						final SentenceSong s1 = mLyricItems.getSentenceFromPosition(currentPosition, true);
						
						if (s1 != null && currentPosition >= s1.getTimeStart()) {
							//MyLog.i(TAG, "=changeLine6=currentPosition="+currentPosition+"=s="+s1.getLine());
							
							if (s1.getLine() == 0) {
								mTopLine.post(new Runnable() {
									@Override public void run() {
										mTopLine.setText(s1.getSentence());		
										if(activity_karaoke_InfoSong.getVisibility()==View.VISIBLE)
											displayTitleWithMode(false);
										mTopLine.measure(0, 0);
										maxTopWidth = mTopLine.getMeasuredWidth();

										RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mTopLine.getLayoutParams();						
										int left = screenWidthInPixels * SPACE_PERCENT_RIGHT / 100;
										params.setMargins(left, 0, 0, 0);
										mTopLine.setLayoutParams(params);
										
										params = (RelativeLayout.LayoutParams) mTopColorLine.getLayoutParams();		
										params.setMargins(left, 0, 0, 0);
										mTopColorLine.setLayoutParams(params);
										
										mTopColorLine.setText(s1.getSentence());
										if (sentence1 != s1) {
											sentence1 = s1;
											mTopColorLine.getLayoutParams().width = 0;
											mTopColorLine.setTextColor(colorPanBody[sentence1.getType()]);
											
											mTopLine.setTextColor(colorShowBody);
											displayIconSinger(s1.getLine(),sentence1.getType(),sentence1.getTypeSinger());
											//MyLog.i(TAG, "=changeLine7=getSentence="+s.getSentence()+"=getType="+sentence1.getType()+"=colorPanBody="+colorPanBody[sentence1.getType()]+"=getTypeSinger="+sentence1.getTypeSinger());
											
										}
									}
								});
							} else {
								mBottomLine.post(new Runnable() {
									@Override public void run() {
										mBottomLine.setText(s1.getSentence());	
										if(activity_karaoke_InfoSong.getVisibility()==View.VISIBLE)
											displayTitleWithMode(false);
										mBottomLine.measure(0, 0);
										maxBottomWidth = mBottomLine.getMeasuredWidth();

										RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mBottomLine.getLayoutParams();
										int left = (int)(screenWidthInPixels - screenWidthInPixels * SPACE_PERCENT_RIGHT / 100 - mBottomLine.getMeasuredWidth());
										params.setMargins(left, 0, 0, 0);
										mBottomLine.setLayoutParams(params);	
										
										params = (RelativeLayout.LayoutParams) mBottomColorLine.getLayoutParams();		
										params.setMargins(left, 0, 0, 0);
										mBottomColorLine.setLayoutParams(params);
										
										mBottomColorLine.setText(s1.getSentence());
										if (sentence2 != s1) {
											sentence2 = s1;
											mBottomColorLine.getLayoutParams().width = 0;
											mBottomColorLine.setTextColor(colorPanBody[sentence2.getType()]);
											mBottomLine.setTextColor(colorShowBody);
											displayIconSinger(s1.getLine(),sentence2.getType(),sentence2.getTypeSinger());
											//MyLog.i(TAG, "=changeLine8=getSentence="+s.getSentence()+"=getType="+sentence2.getType()+"=colorPanBody="+colorPanBody[sentence2.getType()]+"=getTypeSinger="+sentence2.getTypeSinger());
										}
									}

									
								});
							}

							isStarted = false;
							lastSentence = mLyricItems.getLastSentences(currentPosition);
						}
					} else {
						int d = mLyricItems.getLyricBlock(currentPosition);
						if (currentLyricBlock != d && d != -1) {
							isStarted = true;
							currentLyricBlock = d;
						}
					}

					WordSong words = mLyricItems.getCountStartFromPosition(currentPosition);
					//MyLog.i(TAG, "=changeLine8=currentPosition="+currentPosition+"=words="+words);
					if (words != null) {
						if (!words.getWord().equals(curCntdownWord)) {
							curCntdownWord = words.getWord();
							//MyLog.i(TAG, "=changeLine9=currentPosition="+currentPosition+"=curCntdownWord="+curCntdownWord);
							
							mTopLine.post(new Runnable() {
								@Override public void run() {
									if (curCntdownWord.equalsIgnoreCase("4") || curCntdownWord.equalsIgnoreCase("3")) {
										bIsEndBlock = false;
										displayTitle(0, 0); // force clear

//										if (((MyApplication) getApplication()).isColoringLyric()) {
//											mImgLyricBackground.post(new Runnable() {
//												@Override public void run() {
//													mImgLyricBackground.setBackgroundColor(Color.argb(80, 0, 0, 0)); // black
//																														// trans
//																														// 50%
//													mImgLyricBackground.setVisibility(RelativeLayout.VISIBLE);
//												}
//											});
//										}
									} /*else*/ 
									if (curCntdownWord.equalsIgnoreCase("3") || curCntdownWord.equalsIgnoreCase("2") || curCntdownWord.equalsIgnoreCase("1")) {
										isStartingKaraoke = true;
										bIsEndBlock = false;
										displayCountdown(curCntdownWord);
									}
								}
							});
						}
					}
					//stopTimer();
				}
			} catch (Exception ex) {
				// Ignore
				// MyLog.e(TAG, ex);
			}
		}

		private void checknewSong() {
			if(iRepeat!=0)return;
			//OldtimerCounter= mLyricItems.getSentenceFromPosition(mLyricItems.s, true);
			MyLog.e(TAG, "=checknewSong=OldtimerCounter="+OldtimerCounter+"=iRepeat="+iRepeat+"=timerPlayer="+timerPlayer);
			iRepeat=2;
//			stopKaraoke();
			startTimerLyricInfo(1000);
		}
	};

	private Handler mHandleChangeColor = new Handler(new Handler.Callback() {
		@Override public boolean handleMessage(Message msg) {
			return false;
		}
	});

	private Runnable mRunnableChangeColor = new Runnable() {
		@Override public void run() {
			try {
				changeColor();
			} catch (Exception ex) {
				//MyLog.e(TAG, ex);
				
			}
		}

		private boolean changeColor() {
			try {
				int currentPositionColor = GetCurrentTimeMillis(true, true);
				//MyLog.d(TAG, "===changeColor==savePauseTime="+savePauseTime+"=bIsEndBlock="+bIsEndBlock+"=currentPositionColor="+currentPositionColor+"=isStartingKaraoke="+isStartingKaraoke);
				
				if (savePauseTime != 0)
					return false;
				if (bIsEndBlock)
					return false;
				if (currentPositionColor > 0) {
					SentenceSong s = sentence2;
					
					if (s == null || currentPositionColor < s.getWords().get(0).getTimeStart()) {
						s = sentence1;
						//MyLog.d(TAG, "===changeColor01==currentPositionColor="+currentPositionColor);
					}
					if (lastSentence != null && currentPositionColor >= lastSentence.getWords().get(0).getTimeStart()) {
						s = lastSentence;
						//MyLog.d(TAG, "===changeColor02==currentPositionColor="+currentPositionColor	);
					}
					
					
					if (s != null) {
						int index = s.getWordFromPosition(currentPositionColor);
						//MyLog.d(TAG, "===changeColor1=index="+index);
						//MyLog.d(TAG, "===changeColor01=="+s.getWords().get(0).getTimeStart()+"=="+s.getWords().get(0).getWord());
						//MyLog.d(TAG, "===changeColor02=="+lastSentence.getWords().get(0).getTimeStart()+"=="+lastSentence.getWords().get(0).getWord());
						int curCnt = 0;
						try {
							curCnt = Integer.parseInt(curCntdownWord); 
						} catch (Exception e) {							
						}
						if (index != -1) {
							if (isStartingKaraoke && curCnt <= 1) {
								displayCountdown("");
							}

							final WordSong w = s.getWords().get(index);
							
							bIsLastWordOfLine = (index == s.getWords().size() - 1) ? true : false; // last word in line
							float panFactor = 1.1f; // bIsLastWordOfLine ? 1.05f
													// : 1.00f;

							float currentSize = 0;
							float wordSize = w.getSizeLength();
							int wordTime = w.getTimeLenght();
							//MyLog.d(TAG, "===changeColor2=WordSong="+w+"=wordSize="+wordSize+"=wordTime="+wordTime);
							if (bIsLastWordOfLine) {
								wordSize += 15; // Because the last word has
												// space, but size not count,
												// here I add-in
								if (wordTime > 1500)
									wordTime = 1500;
							} else {
								wordSize += 4;
							}

							if (wordTime > 0) {
								currentSize = (int) (((currentPositionColor - w.getTimeStart()) * (wordSize * panFactor)) / wordTime);
							}

							if (currentSize < 0)
								currentSize = 0;
							else if (currentSize >= wordSize)
								currentSize = wordSize;

							curLyricSize = (int) (w.getSizeStart() + currentSize);
							curLyricMaxWidth = (int) (w.getSizeStart() + wordSize);
							//MyLog.d(TAG, "===changeColor3=curLyricSize="+curLyricSize+"=curLyricMaxWidth="+curLyricMaxWidth+"=getLine()="+s.getLine());
							
							if (!bIsShowLastLine) {
								if (curLyricSize <= curLyricSizePanDone && curLyricSize != 0 && curLyricSizePanDone != 0) {
									return false;
								}
							}

							if (curLyricSize >= curLyricMaxWidth) // each word
							{
								curLyricSize = curLyricMaxWidth;
								if(bIsLastWordOfLine){
									if (s.getLine() % 2 == 0) {										
										curLyricSize = maxTopWidth;
									} else {
										curLyricSize = maxBottomWidth;
									}	
								}
							}

							if (s.getLine() % 2 == 0) {
								//MyLog.d(TAG, "=changeColor0=="+s.getSentence().equals(mTopColorLine.getText().toString())+"=s="+s.getSentence()+"=mTopColorLine="+mTopColorLine.getText().toString());
								if (!s.getSentence().equals(mTopColorLine.getText().toString())) {
									return false;
								}
								updatePanWidth(mTopColorLine, 0);
							} else {
								//MyLog.d(TAG, "=changeColor1=="+s.getSentence().equals(mBottomColorLine.getText().toString())+"=s="+s.getSentence()+"=mBottomColorLine="+mBottomColorLine.getText().toString());
								
								if (!s.getSentence().equals(mBottomColorLine.getText().toString())) {
									return false;
								}
								updatePanWidth(mBottomColorLine, 1);
							}
							//stopTimer();
							return true;
						}
					}
				}
				return false;
			} catch (Exception ex) {
				MyLog.e(TAG, ex);
				return false;
			}
		}
	};
	
	private int maxTopWidth = 0;
	private int maxBottomWidth = 0;

	private void updatePanWidth(SentenceTextView tvColorLine, int line) {
		try {
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tvColorLine.getLayoutParams();
			//MyLog.d(TAG, "=updatePanWidth=line="+line+"=width="+params.width+"=curLyricSize="+curLyricSize+"=curLyricMaxWidth="+curLyricMaxWidth);
			if (params.width < curLyricSize) {
				params.width = curLyricSize;
								
				tvColorLine.setLayoutParams(params);
				curLyricSizePanDone = curLyricSize;
				if (bIsLastWordOfLine && curLyricSize >= curLyricMaxWidth) {
					curLyricSizePanDone = 0;
				}
			}
		} catch (Exception ex) {
			MyLog.e(TAG, ex);
		}
	}


	@Override
	public void onPlayerStop(MyPlayer mediaPlayer) {
		try {
			stopKaraoke();
		} catch (Exception ex) {
			MyLog.e(TAG, ex);
		}
	}

	@Override
	public void onPlayerError(String path, int what, int extra) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPlayerReadyToPlay(MyPlayer mediaPlayer) {
		// TODO Auto-generated method stub
		if(iPlayMP3)
			playKaraoke(mediaPlayer);
//		final MyPlayer tam=mediaPlayer;
//		//playKaraoke(mediaPlayer);
//		
//		mTopLine.postDelayed(new Runnable() {
//			@Override public void run() {
//				playKaraoke(tam);
//			}
//		}, 5000);
	}
	
	private Timer timerLyricInfo;
	private int timerPlayer = 0;
	private boolean iPlayMP3=false;
	private int cntCheckErrorWowza=0;
//	private boolean isSentenceLeftRight = false;
	
	private void startTimerLyricInfo(int time) {
		stopTimerLyricInfo();
		MyLog.e(TAG, "........startTimerLyricInfo............."+time);
		timerLyricInfo = new Timer();
		timerLyricInfo.schedule(new LyricInfoSyncTask(), 0, time);
		
	}

	private void stopTimerLyricInfo() {
		MyLog.e(TAG, ".......stopTimerLyricInfo..............");
		if (timerLyricInfo != null) {
			timerLyricInfo.cancel();
			timerLyricInfo = null;
		}		
	}
	
	class LyricInfoSyncTask extends TimerTask {
		@Override
		public void run() {
			//MyLog.e(TAG,"==LyricInfoSyncTask ==");
			if (((MyApplication)context).getSocket() == null || flagRemoveSocket) {
				MyLog.e(TAG,"==LyricInfoSyncTask socket null==");
				return;
			}
			try {
				mTopLine.post(new Runnable() {
					public void run() {							
						final long t1=System.currentTimeMillis();
							((MyApplication)context).getLyricInfo();
							((MyApplication)context).setOnReceiverInfoLyricListener(new OnReceiverInfoLyricListener() {
								
								@Override
								public void OnReceiverInfoLyric(String resultString) {
									
									if(resultString == null || resultString.equals("0")){
										MyLog.d(TAG, "=syncTime=null="+resultString);
										resultString = "";										
										 if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && MyApplication.flagSmartK_CB && iPlayVideoSamba && iPrepare)
											 pauseVideo();
										//stopKaraoke();
									}else{
										try{
											int resultValue=Integer.parseInt(resultString);
											
											if(MyApplication.intSvrModel == MyApplication.SONCA||MyApplication.intSvrModel ==MyApplication.SONCA_SMARTK){
												resultValue /=90;// TODO SK9xxx/90->milsecon, # ko chia
											}
											MyLog.d(TAG, "=syncTime start=resultValue="+resultValue+"=timerPlayer="+timerPlayer+"=CurrentPosition="+getCurrentPositionVideo()+"=iPrepare="+iPrepare);
											timerPlayer=resultValue;											
											cntDelay=(int)(System.currentTimeMillis()-t1)/2;
											if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && MyApplication.flagSmartK_CB){
												if(timerPlayer>PeriodGetTime && iPlayVideoSamba &&!iPrepare){
													cntCheckErrorWowza++;
													MyLog.e(TAG, "link wowza error, play again cntCheckErrorWowza"+cntCheckErrorWowza);
													if(cntCheckErrorWowza>2)
													OnMain_NewSong(idSongCurrent, intMediaTypeCurrent, intMidiShiftTimeCurrent, intMainTypeABC);
												}
												if((resultValue+TIMEPLUSVIDEO+TIMECheckPause+PeriodGetTime) < timerPlayer  && !flagBreakReceiveTime){
													MyLog.e(" ", " ");MyLog.e(" ", " ");
													MyLog.e(TAG, "timerPlayer lớn hơn get time ");//repeat
													MyLog.e(" ", " ");MyLog.e(" ", " ");
													OnMain_NewSong(idSongCurrent, intMediaTypeCurrent, intMidiShiftTimeCurrent, intMainTypeABC);
										
												}
											}else{											
											if(resultValue < timerPlayer && !flagBreakReceiveTime){
												MyLog.e(" ", " ");MyLog.e(" ", " ");
												MyLog.e(TAG, "ONE MAIN NEW SONG SPECIAL");
												MyLog.e(" ", " ");MyLog.e(" ", " ");
												OnMain_NewSong(idSongCurrent, intMediaTypeCurrent, intMidiShiftTimeCurrent, intMainTypeABC);
																						
											}
											}
										
										if(MyApplication.enableSamba && iPlayVideoSamba && iPrepare){
										 if(timerPlayer!=0x7fffffff ){
											  resultValue=timerPlayer+cntDelay;
											 //MyLog.d(TAG, "=syncTime=currentTime="+getCurrentPositionVideo()+"=resultValue="+resultValue/*+"=timerPlayer="+timerPlayer*/+"=cntDelay="+cntDelay+"=iStartTimerVideo="+iStartTimerVideo+"=isPlaying="+mVideoView.isPlaying());
											 if(Math.abs(getCurrentPositionVideo()-resultValue) <TIMECheckPause){												
												// MyLog.d(TAG, "=syncTime1=");
												 if(iStartTimerVideo){	 
													 stopTimer();
														iStartTimerVideo=false;	
														dismisLoading();
												 }
												 if(pauseView.getPauseView())
														resumeVideo();	
												 
											 }else if(getCurrentPositionVideo()>resultValue){
												// MyLog.d(TAG, "=syncTime2=");
												 startTimerVideo(getCurrentPositionVideo(), resultValue);												
											 }else{
												// MyLog.d(TAG, "=syncTime3=");
												 if(getCurrentPositionVideo()<timerPlayer-TIMECheckPause){
													 if(!iStartTimerVideo && isSeeking == false){
														// MyLog.d(TAG, "=syncTime5=");
													 setCurrentPositionVideo(timerPlayer+cntDelay,TIMEPLUSVIDEO);
											 }
											 }
										 }
										 }
										 return;
										}
										if(iStart && timerPlayer!=0x7fffffff){
//											if(timerPlayer>10000){
//												displayTitle(0,0);
//											}else{
//												displayTitle(0, 15000);
//											}
											
											iStart=false;
											timeLoopChangeColor = System.currentTimeMillis();
											timeLoopService = timeLoopChangeColor;
											timeStartCount = timeLoopChangeColor;
											startTimer();
											if(timerPlayer<10000)
												displayTitle(0, 15000);
										}

//										if(MyApplication.curHiW_firmwareInfo != null){
//											if(MyApplication.curHiW_firmwareInfo.getDaumay_version() >= 200){
//												// allow loop timer
//											} else {
//												if(iRepeat<2)
//													stopTimerLyricInfo();	
//											}
//										} else {
//											if(iRepeat<2)
//												stopTimerLyricInfo();	
//										}	
										
										if(iRepeat!=0){
											if(iRepeat==1)
												iRepeat=0;
											else if(iRepeat==2){
												OldtimerCounter=timerPlayer;
												iRepeat=3;
											}
											//MyLog.e(TAG,"=iRepeat=true=timerPlayer=="+ timerPlayer+"=OldtimerCounter="+OldtimerCounter+"=idSongCurrent="+idSongCurrent);
											if(timerPlayer<OldtimerCounter){
												iRepeat= 0;
												stopKaraoke();
												loadLyricSong(idSongCurrent, intMediaTypeCurrent, intMidiShiftTimeCurrent, intMainTypeABC, false);
												//OldtimerCounter=timerPlayer;
											}
											if(timerPlayer==0x7fffffff){
												OldtimerCounter=timerPlayer;
												//showScoreDialog();
											}//else
												//hideScoreDialog();
										}
										//return;
										}catch(Exception e){}
									}
									
									// MyLog.e(TAG,"==LyricInfoSyncTask=="+ resultString+"=timerPlayer="+timerPlayer+"=OldtimerCounter="+OldtimerCounter+"=iRepeat="+iRepeat+"=cntDelay="+cntDelay);
									//stopTimerLyricInfo();
									
								}
							});		
							
						}
					
				});
			} catch (Exception e) {
				e.printStackTrace();
			}		
		}
	}
	
	
	
	// -------------------- HMINH - OnColorLyricListener
	
	@Override
	public void OnMain_StartTimerAutoConnect() {
		try {
			MyLog.e(" ", " ");MyLog.e(" ", " ");MyLog.e(" ", " ");
			MyLog.e(TAG, "OnMain_StartTimerAutoConnect");

			lyricBack.StartTimerAutoConnect();
			
		} catch (Exception e) {

		}
		
	}
	
	@Override
	public void OnMain_StopTimerAutoConnect() {
		try {
			MyLog.e(" ", " ");MyLog.e(" ", " ");MyLog.e(" ", " ");
			MyLog.e(TAG, "OnMain_StopTimerAutoConnect");

			lyricBack.StopTimerAutoConnect();
			
		} catch (Exception e) {

		}
		
	}
	
	@Override
	public void OnMain_UpdateWifi() {
		try {
			MyLog.e(" ", " ");MyLog.e(" ", " ");MyLog.e(" ", " ");
			MyLog.e(TAG, "OnMain_UpdateWifi");

			lyricBack.invalidate();
			
		} catch (Exception e) {

		}
	}
	
	private boolean flagRemoveSocket = false;
	
	@Override
	public void OnMain_RemoveSocket() {
		try {			
			MyLog.e(" ", " ");MyLog.e(" ", " ");MyLog.e(" ", " ");
			MyLog.e(TAG, "OnMain_RemoveSocket");
			
			flagRemoveSocket = true;
			
			btnSinger.setVisibility(View.INVISIBLE);
			pauseView.invalidate();
			nextView.invalidate();
			
		} catch (Exception e) {
			
		}
	}
	
	@Override
	public void OnMain_UpdateControl() {
		try {			
			MyLog.e(" ", " ");MyLog.e(" ", " ");MyLog.e(" ", " ");
			MyLog.e(TAG, "OnMain_UpdateControl");
			
			pauseView.invalidate();
			nextView.invalidate();
			btnSinger.invalidate();
			
		} catch (Exception e) {
			
		}
	}
	
	@Override
	public void OnMain_UpdateSocket(String serverName) {
		try {			
			MyLog.e(" ", " ");MyLog.e(" ", " ");MyLog.e(" ", " ");
			MyLog.e(TAG, "OnMain_UpdateSocket");
			
			flagRemoveSocket = false;
			
			if(iVocalSinger)
				btnSinger.setVisibility(View.VISIBLE);
			else
				btnSinger.setVisibility(View.INVISIBLE);
			pauseView.invalidate();
			nextView.invalidate();
			
			lyricBack.setServerName(serverName);
			
			stopKaraoke();
			
//			iId0 = true;			
//			checkShowGreeting(lyricBack.getSumSong());
		} catch (Exception e) {
			
		}
	}
	
	@Override
	public void OnMain_PausePlay(boolean flagPlay) {
		try {
			if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				if(ktvMainActivity.isDownloadingVideo){
					return;
				}
			} else {
				if(mainActivity.isDownloadingVideo){
					return;
				}	
			}
			
			
			MyLog.e(TAG, "OnMain_PausePlay -- flagPlay = " + flagPlay);
			pausePlayKaraoke(flagPlay);
			
//			pauseView.invalidate();
//			nextView.invalidate();
//			repeatView.invalidate();	
		} catch (Exception e) {
			
		}
		
	}
	
	private boolean flagBreakReceiveTime = false;

	@Override
	public void OnMain_NewSong(final int songID, final int intMediaType, final int midiShifTime, final int mainTypeABC) {
		try {
			String rootPath = "";
			if (android.os.Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED)) {
				rootPath = android.os.Environment
						.getExternalStorageDirectory()
						.toString()
						.concat(String.format("/%s/%s", "Android/data",
								mainActivity.getPackageName()));
				File appBundle = new File(rootPath);
				if (!appBundle.exists())
					appBundle.mkdir();
			}
						
			String savePath = rootPath.concat("/" + MyApplication.MIDI_FILE);
			File file = new File(savePath);			
			if(!file.exists()){
				file.delete();
			}
			
			maxTopWidth = 0;
			maxBottomWidth = 0;
			
			flag801 = false;
			
			if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				if(ktvMainActivity.isDownloadingVideo){
					return;
				}
			} else {
				if(mainActivity.isDownloadingVideo){
					return;
				}	
			}
			
			MyLog.e(" ", " ");MyLog.e(" ", " ");MyLog.e(" ", " ");MyLog.e(" ", " ");
			MyLog.e(TAG, "OnMain_NewSong -- songID = " + songID + " -- intMediaType = " + intMediaType);
				if(!iPlayMP3){
					flagBreakReceiveTime = true;
					stopKaraoke();
				}
				iId0=true;
				
			idSongCurrent = songID;
				
			if(songID==0 && lyricBack.getSumSong()==0){
//				if(!iPlayMP3)
//					stopKaraoke();
//				iId0=true;
				
				checkShowGreeting(lyricBack.getSumSong());
				return;
			}
			
			//if(MyApplication.intSvrModel == MyApplication.SONCA){
//				stopKaraoke();
//				iId0=true;
			//}
			loadLyricSong(songID, intMediaType, midiShifTime, mainTypeABC, false);	
			
			pauseView.invalidate();
			nextView.invalidate();
//			repeatView.invalidate();	
		} catch (Exception e) {
			
		}
	}

	@Override
	public void OnMain_Dance(boolean flagDance) {
		try {
			if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				if(ktvMainActivity.isDownloadingVideo){
					return;
				}
			} else {
				if(mainActivity.isDownloadingVideo){
					return;
				}	
			}
			
			MyLog.e(TAG, "OnMain_Dance -- flagDance = " + flagDance);
			if(flagDance)
				exitKaraoke();	
		} catch (Exception e) {
		
		}
		
	}

	@Override
	public void OnMain_GetTimeAgain() {	
		try {
			if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				if(ktvMainActivity.isDownloadingVideo){
					return;
				}
			} else {
				if(mainActivity.isDownloadingVideo){
					return;
				}	
			}
			
			if(iRepeat!=0)return;
			OldtimerCounter=GetCurrentTimeMillis(false, false);
			MyLog.e(TAG, "=OnMain_GetTimeAgain=OldtimerCounter="+OldtimerCounter+"=iRepeat="+iRepeat+"=timerPlayer="+timerPlayer);
			iRepeat=1;
//			stopKaraoke();
			startTimerLyricInfo(PeriodGetTime);	
		} catch (Exception e) {
			
		}
		
	}
	
	private void pausePlayKaraoke(boolean flagPlay) {
		try {
			MyLog.d(TAG, "==pausePlayKaraoke=iPlayMP3="+iPlayMP3+"=flagPlay="+flagPlay);
			if(iVocalSinger)
				btnSinger.invalidate();
			lyricBack.setPlayPause(flagPlay);
			if (iPlayMP3) {
				if (mPlayer != null) {
					boolean isPlaying = mPlayer.isPlaying();
					mPlayer.pausePlay();
					
//						if (!isPlaying) {
//							mImagePausePlay.setImageResource(R.drawable.zlight_play_active);
//							
//						} else {
//							mImagePausePlay.setImageResource(R.drawable.zlight_pause_active);
//							
//						}
					pauseView.setPauseView(!isPlaying);
					lyricDisplayPausePlay(!isPlaying);
				}
			}else{				
//					if (flagPlay) {
//						mImagePausePlay.setImageResource(R.drawable.zlight_pause_active);
//					} else {
//						mImagePausePlay.setImageResource(R.drawable.zlight_play_active);
//						
//					}
				pauseView.setPauseView(flagPlay);
				lyricDisplayPausePlay(flagPlay);
			}
		} catch (Exception ex) {
			MyLog.e(TAG, ex);
		}
	}
	
	private void lyricDisplayPausePlay(boolean isPlay) {
		try {
			MyLog.d(TAG, "=lyricDisplayPausePlay=isPlay="+isPlay+"=="+iPlayVideoSamba);
			if (isPlay) {
				if(iPlayVideoSamba){
					resumeVideo();
					return;
				}
				timeLoopChangeColor = System.currentTimeMillis();
				//timeStartCount += (timeLoopChangeColor - savePauseTime)-plusPausePlay;
				savePauseTime = 0;
				//timeLoopChangeColor = System.currentTimeMillis();
				timeLoopService = timeLoopChangeColor;
				timeStartCount = timeLoopChangeColor;
				startTimerLyricInfo(PeriodGetTime);
			} else {
				if(iPlayVideoSamba){
					pauseVideo();
					return;
				}
				savePauseTime = System.currentTimeMillis();
				
			}
			//MyLog.d(TAG, "=lyricDisplayPausePlay=isPlay"+isPlay+"=savePauseTime="+savePauseTime
					//+"=timeLoopChangeColor="+timeLoopChangeColor+"=timeStartCount="+timeStartCount+"=="+System.currentTimeMillis());
		} catch (Exception ex) {
			MyLog.e(TAG, ex);
		}
	}

	
	// ---------------------------- HMINH - VIDEO BACKGROUND	
	private VideoView mVideoView;
	public void playVideoDefault(String path,boolean iSamba) {
		MyLog.d(TAG, "=playVideoDefault=iPlayVideoSamba="+iPlayVideoSamba+"=="+mVideoView.isPlaying()+"=iSamba="+iSamba);
		if (mVideoView == null) {
			return;
		}
		//stopTimer1();
		stopVideo();
		if(iPrepare)iPrepare=false;
		/*if(iPlayVideoSamba)
			stopVideo();
		else{
		if (mVideoView.isPlaying()) {
			mVideoView.pause();
			mVideoView.stopPlayback();
		}
		}*/
		// mVideoView.setVideoChroma(MediaPlayer.VIDEOCHROMA_RGB565);
		// mVideoView.getHolder().setFormat(PixelFormat.RGBA_8888);
//		mVideoView.setVideoQuality(io.vov.vitamio.MediaPlayer.VIDEOQUALITY_HIGH);
//		mVideoView.setVideoChroma(io.vov.vitamio.MediaPlayer.VIDEOCHROMA_RGB565);
		if(!iSamba){
			iPlayVideoSamba=false;
		String rootPath = android.os.Environment.getExternalStorageDirectory().toString().concat("/SONCA");
		
		File parentFolder = new File(rootPath.concat("/VIDEO"));

		int fileCount = parentFolder.listFiles().length;
		if (fileCount == 0) {
			myFlipperImage.setVisibility(View.VISIBLE);
			mVideoView.setVisibility(View.GONE);
			startFlippingImage();
			return;
		}
				
		for (final File fileEntry : parentFolder.listFiles()) {
			if(fileEntry.getName().toLowerCase().endsWith(".mp4") || fileEntry.getName().toLowerCase().endsWith(".avi")){
				continue;
			}
			
			fileEntry.delete();
		}
		
		fileCount = parentFolder.listFiles().length;
		if (fileCount == 0) {
			myFlipperImage.setVisibility(View.VISIBLE);
			mVideoView.setVisibility(View.GONE);
			startFlippingImage();
			return;
		}
			path=parentFolder.listFiles()[0].getAbsolutePath();
		}
		mVideoView.setVisibility(View.VISIBLE);
		myFlipperImage.setVisibility(View.GONE);
		
//		mVideoView.setOnPreparedListener(new OnPreparedListener() {
//			
//			@Override
//			public void onPrepared(MediaPlayer mp) {
//				mp.setLooping(true);				
//			}
//		});
		
		/*mVideoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
			
			@Override
			public void onCompletion(IMediaPlayer arg0) {
				// TODO Auto-generated method stub
				
				playVideoDefault(null,false);
			}
		});*/
		
		mVideoView.setOnErrorListener(new IMediaPlayer.OnErrorListener() {
			
			@Override
			public boolean onError(IMediaPlayer arg0, int arg1, int arg2) {
				MyLog.d(TAG, "==setOnErrorListener=iPlayVideoSamba="+iPlayVideoSamba);
				
				myFlipperImage.setVisibility(View.VISIBLE);
				mVideoView.setVisibility(View.GONE);
				startFlippingImage();
				
				return true;
			}

		});
		
		mVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
			
			@Override
			public void onPrepared(IMediaPlayer mp) {
				//AudioManager  mAudioManager = (AudioManager)mainActivity.getSystemService(Context.AUDIO_SERVICE);
				// mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0); 
				// TODO Auto-generated method stub
				MyLog.d(TAG, "==onPrepared=iPlayVideoSamba="+iPlayVideoSamba);
				
				mMediaPlayer = (MediaPlayer) mp; 
				mMediaPlayer.setVolume(0, 0);
				/*if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && MyApplication.flagSmartK_CB){
					iPrepare=true;
				}else{*/
				//boolean iDecode=((tv.danmaku.ijk.media.player.MediaPlayer)mp).isHardwareDecode();
				//MyLog.d(TAG, "=onPrepared=="+iDecode);
				mBottomLine.postDelayed(new Runnable() {
					@Override public void run() {
				iPrepare=true;
						if(iPlayVideoSamba  ){
							if(!pauseView.getPauseView())
					pauseVideo();
							else
								mVideoView.start();
						}
							
					}
					}, 500);
				//}
			}
		});
		
//		mVideoView.setOnBufferingUpdateListener(new IMediaPlayer.OnBufferingUpdateListener() {
//			
//			@Override
//			public void onBufferingUpdate(IMediaPlayer arg0, int arg1) {
//				// TODO Auto-generated method stub
//				MyLog.d(TAG, "=onBufferingUpdate=arg0="+arg0+"=arg1="+arg1);
//			}
//		});
		
		mVideoView.setOnSeekCompleteListener(new IMediaPlayer.OnSeekCompleteListener() {
			
			@Override
			public void onSeekComplete(IMediaPlayer mp) {
				// TODO Auto-generated method stub
				isSeeking = false;
				MyLog.d(TAG, "==setOnSeekCompleteListener=CurrentPosition="+getCurrentPositionVideo()+"=isPlaying="+mVideoView.isPlaying()+"=timerPlayer="+timerPlayer+"=cntDelay="+cntDelay);
				if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && MyApplication.flagSmartK_CB){
							
				}else{
					mVideoView.pause();		
							int CurrentPosition=getCurrentPositionVideo();
					if(Math.abs(CurrentPosition-(timerPlayer+cntDelay)) <TIMECheckPause){												
								if(iStartTimerVideo){	 
									 stopTimer();
										iStartTimerVideo=false;																										
										dismisLoading();
								 }
								 if(pauseView.getPauseView())
										resumeVideo();	
								 return;
							 }
							
							if(CurrentPosition>=(timerPlayer+cntDelay)){
								startTimerVideo(CurrentPosition,timerPlayer+cntDelay);	
					}		
							}									
			}
		});
		
		mVideoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
			
			@Override
			public void onCompletion(IMediaPlayer mp) {
				// TODO Auto-generated method stub
				MyLog.d(TAG, "=play video onCompletion==iPlayVideoSamba="+iPlayVideoSamba);
				if(!iPlayVideoSamba)
				playVideoDefault(null,false);
			}
		});
		
		MyLog.d(TAG, "==playVideoDefault=path="+path);
		mVideoView.setVideoPath(path);
		//mVideoView.setUseNativePlayer(true);
		mVideoView.requestFocus();
		
		if(!iSamba){
		mVideoView.start();
	}

	}

	@Override
	public void OnMain_setCntPlaylist(int i) {
		try {
			if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				if(ktvMainActivity.isDownloadingVideo){
					return;
				}
			} else {
				if(mainActivity.isDownloadingVideo){
					return;
				}	
			}
			
			// TODO Auto-generated method stub
			MyLog.d(TAG, "=OnMain_setCntPlaylist=i="+i);
			lyricBack.setSumSong(i);
			//checkShowGreeting(i);	
		} catch (Exception e) {
			
		}
		
		
	}

	private void checkShowGreeting(int cnt){
		MyLog.d(TAG, "==checkShowGreeting=cnt="+cnt+"=iId0="+iId0+"=iPlayVideoSamba="+iPlayVideoSamba+"=isPlaying="+mVideoView.isPlaying());
		final int tam=cnt;
		mTopLine.post(new Runnable() {			
			@Override
			public void run() {
				try {
					if (tam == 0 && iId0) {
						if(iPlayVideoSamba||!mVideoView.isPlaying())
							playVideoDefault(null,false);
						getGreeting();
						
					} else {
						setDisplayFontForLanguage();
						
						iId0 = false;
						mText1Line.setVisibility(View.INVISIBLE);
						lyricBack.setVisibility(View.VISIBLE);
						//lyricBack.setPlayPause(true);
						mTopLine.setStrokeColor(Color.BLACK);
						mBottomLine.setStrokeColor(Color.BLACK);
						if(mTopLine.getText().equals(greetingLine1)||mBottomLine.getText().equals(greetingLine2))
						{
							mTopLine.setText("");
							mBottomLine.setText("");
						}
						if(iVocalSinger)
							btnSinger.setVisibility(View.VISIBLE);
						else
							btnSinger.setVisibility(View.INVISIBLE);
						activity_karaoke_Control.setVisibility(View.VISIBLE);
					}
				} catch (Exception e) {
				}
			}
		});
	}
	
	@Override
	public void OnMain_setNextSongPlaylist(String resultName, int resultID) {
		try {
			if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				if(ktvMainActivity.isDownloadingVideo){
					return;
				}
			} else {
				if(mainActivity.isDownloadingVideo){
					return;
				}	
			}
			// TODO Auto-generated method stub

			MyLog.d(TAG, "=OnMain_setNextSongPlaylist=resultName=" + resultName
					+ "=resultID=" + resultID);
			lyricBack.setNextSongName(resultName, resultID);
		} catch (Exception e) {
		}
	}
	
	@Override
	public void OnMain_setCurrSongPlaylist(String resultName) {
		try {
			if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				if(ktvMainActivity.isDownloadingVideo){
					return;
				}
			} else {
				if(mainActivity.isDownloadingVideo){
					return;
				}	
			}
			
			lyricBack.setCurrectSong(resultName);
		} catch (Exception e) {
		}
	}
	
	@Override
	public void OnMain_VocalSinger(boolean flagVocalSinger) {
		try {
			if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				if(ktvMainActivity.isDownloadingVideo){
					return;
				}
			} else {
				if(mainActivity.isDownloadingVideo){
					return;
				}	
			}
			
			// TODO Auto-generated method stub
			MyLog.e(TAG, "OnMain_VocalSinger -- flagVocalSinger = " + flagVocalSinger);
			btnSinger.setSingerView(flagVocalSinger);	
			if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && MyApplication.flagSmartK_CB && iPlayVideoSamba){
				MyLog.e(TAG, "change volcal wowza");
				OnMain_NewSong(idSongCurrent, intMediaTypeCurrent, intMidiShiftTimeCurrent, intMainTypeABC);
			
		}
		} catch (Exception e) {
		
	}
	}
	
	@Override
	public void OnMain_CallVideoDefault() {
		try {
			if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				if(ktvMainActivity.isDownloadingVideo){
					return;
				}
			} else {
				if(mainActivity.isDownloadingVideo){
					return;
				}	
			}

			MyLog.d(TAG, "=OnMain_CallVideoDefault=");
			playVideoDefault(null,false);	
		} catch (Exception e) {
		}
	}
	
	public void getGreeting(){
		mTopLine.post(new Runnable() {
			public void run() {		
				
				String[] lines = ((MyApplication)context).getHello();
				MyLog.d(TAG, "=getGreeting=="+lines+"=="+lines.length);
					  if(lines != null && lines.length >= 2){
						 try{ 
							   if(lines[0] != null){
								   greetingLine1 = lines[0];
							   }
							   if(lines[1] != null){
								   greetingLine2 = lines[1];
							   }
						 }catch(Exception e){
							 
						 }
					   MyLog.d(TAG, "=getGreeting=dong1="+greetingLine2+"=dong1="+greetingLine2);
				  }		
					  String fontLyric = "ROBOTOBLACK.ttf";
						Typeface tf = Typeface.createFromAsset(context.getAssets(),
								"fonts/" + fontLyric);
		
						setFontLyrics(tf, mTopLine, colorShowBorder, 14, Typeface.NORMAL);
						setFontLyrics(tf, mBottomLine, colorShowBorder, 14, Typeface.NORMAL);
						
					  activity_karaoke_Control.setVisibility(View.INVISIBLE);
						btnSinger.setVisibility(View.INVISIBLE);
						mText1Line.setText(getMyActivity().getResources().getString(R.string.activity_karaoke_moiban));
						mText1Line.setVisibility(View.VISIBLE);
						lyricBack.setVisibility(View.INVISIBLE);
						mTopLine.setStrokeColor(Color.WHITE);
						mBottomLine.setStrokeColor(Color.WHITE);
						mTopLine.setTextColor( Color.rgb(0, 0, 255));
						mTopLine.setText("  " + greetingLine1 + "  ");
						mBottomLine.setTextColor( Color.rgb(255, 0, 255));
						mBottomLine.setText("  " + greetingLine2 + "  ");				

						mTopLine.measure(0, 0);

						RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mTopLine.getLayoutParams();						
						int left = (getWidthScreen() - (int) (mTopLine.getMeasuredWidth() + mTopLine.getPaddingLeft() + mTopLine.getPaddingRight())) / 2;										
						params.setMargins(left, 0, 0, 0);
						mTopLine.setLayoutParams(params);	
						
						mBottomLine.measure(0, 0);

						params = (RelativeLayout.LayoutParams) mBottomLine.getLayoutParams();						
						left = (getWidthScreen() - (int) (mBottomLine.getMeasuredWidth() + mBottomLine.getPaddingLeft() + mBottomLine.getPaddingRight())) / 2;										
						params.setMargins(left, 0, 0, 0);
						mBottomLine.setLayoutParams(params);	  
			}
		});
	}
	
	// ------------------------ PING SERVER
		private PingTocServerTask pingTocServerTask;
		private long pingTime = 0;
		private boolean flagInterrupt = true;
		
		class InterruptPingServerTask extends AsyncTask<String, Void, Void> {

			private boolean flagPingVideo = false;
			public void setFlagPingVideo(boolean flagPingVideo){
				this.flagPingVideo = flagPingVideo;
			}
			
			protected Void doInBackground(String... urls) {
				try {
					do {
						long temp = System.currentTimeMillis();
						if (temp - pingTime > 5000) {
							// ping fail
							stopPingTocServer();
							return null;
						}
					} while (flagInterrupt);
					return null;
				} catch (Exception e) {
					return null;
				}
			}

			protected void onPostExecute(Void feed) {
			}
		}
		
		private void startPingTocServer(int tocType){
			stopPingTocServer();
			pingTocServerTask = new PingTocServerTask(tocType);
			pingTocServerTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
		}
		
		private void stopPingTocServer(){
			if(pingTocServerTask != null){
				pingTocServerTask.cancel(true);
				pingTocServerTask = null;
			}
		}
		
		class PingTocServerTask extends AsyncTask<String, Void, Void> {

			private int tocType;
			
			public PingTocServerTask(int tocType){
				this.tocType = tocType;
			}
			
			protected Void doInBackground(String... urls) {
				try {
					if (!isTocURLReachable(getMyActivity())) {
						// do nothing
						
						return null;
					}
					
					flagInterrupt = false;
					if(getMyActivity() != null){
						getMyActivity().runOnUiThread(new Runnable() {
							public void run() {
								new Handler().postDelayed(new Runnable() {
									@Override
									public void run() {
										if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
											ktvMainActivity.showDialogMessageKaraoke(getMyActivity().getResources().getString(R.string.msg_kara_6));
										} else {
											mainActivity.showDialogMessageKaraoke(getMyActivity().getResources().getString(R.string.msg_kara_6));	
										}
										
									}
								}, 100);
							}
						});
					}
					return null;
					
				} catch (Exception e) {
					return null;
				}
			}

			protected void onPostExecute(Void feed) {
			}
		}

		private boolean isTocURLReachable(Context context) {
			ConnectivityManager cm = (ConnectivityManager) getMyActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo netInfo = cm.getActiveNetworkInfo();
			if (netInfo != null && netInfo.isConnected()) {
				try {
					URL url = new URL(
							"https://kos.soncamedia.com/firmware/karconnect/video/videoinfo.xml");
					HttpURLConnection urlc = (HttpURLConnection) url
							.openConnection();
					urlc.setConnectTimeout(3 * 1000); // 3 s.
					urlc.connect();
					if (urlc.getResponseCode() == 200) { // 200 = "OK" code (http
															// connection is fine).
					// Log.wtf("Connection", "Success !");
						return true;
					} else {
						return false;
					}
				} catch (MalformedURLException e1) {
					return false;
				} catch (IOException e) {
					return false;
				}
			}
			return false;
		}
		
	// ----------------------- FLIPPER IMAGE
		public void prepareFlipperImages() {
			String rootPath = android.os.Environment
					.getExternalStorageDirectory()
					.toString()
					.concat(String.format("/%s/%s", "Android/data",
							getMyActivity().getPackageName()));

			File parentFolder = new File(rootPath.concat("/FLIPPER"));

			int imageCount = parentFolder.listFiles().length;

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.FILL_PARENT,
					RelativeLayout.LayoutParams.FILL_PARENT);

			for (int count = 0; count < imageCount; count++) {
				ImageView imageView = new ImageView(getMyActivity());
				Bitmap imbm = BitmapFactory
						.decodeFile(parentFolder.listFiles()[count]
								.getAbsolutePath());
				imageView.setImageBitmap(imbm);
				imageView.setScaleType(ScaleType.FIT_XY);

				imageView.setLayoutParams(params);
				myFlipperImage.addView(imageView);
			}
		}

		private Runnable runnableFlipper;
		private Handler handlerFlipper;

		private void startFlippingImage() {
			handlerFlipper = new Handler();

			runnableFlipper = new Runnable() {
				@Override
				public void run() {
					handlerFlipper.postDelayed(runnableFlipper, 20000);
//					myFlipperImage.setInAnimation(inFromRightAnimation());
//					myFlipperImage.setOutAnimation(outToLeftAnimation());
					myFlipperImage.showNext();
				}
			};
			handlerFlipper.postDelayed(runnableFlipper, 100);
		}
		
		private void stopFlippingImage(){
			if(handlerFlipper != null){
				handlerFlipper.removeCallbacks(runnableFlipper);	
			}
		}

		private Animation inFromRightAnimation() {

			Animation inFromRight = new TranslateAnimation(
					Animation.RELATIVE_TO_PARENT, +1.2f,
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f);
			inFromRight.setDuration(500);
			inFromRight.setInterpolator(new AccelerateInterpolator());
			return inFromRight;
		}

		private Animation outToLeftAnimation() {
			Animation outtoLeft = new TranslateAnimation(
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, -1.2f,
					Animation.RELATIVE_TO_PARENT, 0.0f,
					Animation.RELATIVE_TO_PARENT, 0.0f);
			outtoLeft.setDuration(500);
			outtoLeft.setInterpolator(new AccelerateInterpolator());
			return outtoLeft;
		}
			
		public void syncFromServer(ServerStatus status){
			if (status.isSingerOn() != btnSinger.isSingerView()){
				MyLog.d(TAG, "switch isSingerView");
				btnSinger.setSingerView(status.isSingerOn());
			}				

			if (status.isPlaying() != pauseView.getPauseView()){
				MyLog.d(TAG, "switch pause");
				pausePlayKaraoke(status.isPlaying());
			}
		}
			
			private int getDurationVideo() {
				
		    	if(mVideoView != null){
		    		return (int)mVideoView.getDuration();
		    	}
		    	return -1;
		    }
			private int getCurrentPositionVideo() {
				
			    	if(mVideoView != null){
			    		return (int)mVideoView.getCurrentPosition();
			    	}
			    	return -1;
			    }
			
			private boolean isSeeking = false;
			private void setCurrentPositionVideo(int position,int time) {
				
		    	if(mVideoView != null){
		    		
		    		stopTimer();
//		    		progressBar.setVisibility(View.VISIBLE);
		    		//pauseVideo();
		    		isSeeking = true;
		    		mVideoView.seekTo(position+time);
		    		//mVideoView.start();
		    	}
		    	
		    }
			private void stopVideo() {	
				MyLog.d(TAG, "=stopVideo=="+mVideoView.isPlaying());
			    	if(mVideoView != null){
			    		mVideoView.stopPlayback();	
			    	}
			    }

			    private void pauseVideo() {			       
			    	if (mVideoView == null) {
			            return;
			        }
			    	MyLog.d(TAG, "=pauseVideo=="+mVideoView.isPlaying());
			    	if(mVideoView.isPlaying())
					{
			        	mVideoView.pause(); 
					}
			    }

			    private void resumeVideo() {
			    	MyLog.d(TAG, "=resumeVideo=="+mVideoView.isPlaying());
			    	 if (mVideoView == null || mVideoView.isPlaying()) {
			             return;
			         }
			        mVideoView.start();//			       
			    }
		private void dismisLoading(){
			if(getMyActivity() != null){
				getMyActivity().runOnUiThread(new Runnable() {
				@Override
				public void run() {
					progressBar.setVisibility(View.GONE);
				}
			});
			}
		}	   
		
		@Override
		public void OnMain_DownloadMidiResult(boolean result, int id) {
			MyLog.e(" ", " ");MyLog.e(" ", " ");MyLog.e(" ", " ");
			MyLog.e(TAG, "OnMain_DownloadMidiResult -- result == " + result + " -- id = " + id);
			MyLog.e(" ", " ");MyLog.e(" ", " ");MyLog.e(" ", " ");
			if(id == idSongCurrent){
				if(result == true){
					LoadColorLyric loadLyric = new LoadColorLyric(context, this
							.getActivity().getWindow());
					loadLyric.setData(null, -99, true, 0);
					loadLyric.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

					loadLyric
							.setOnLoadColorLyricListener(new OnLoadColorLyricListener() {

								@Override
								public void OnReceiveColorData(Lyrics lyricItems) {
									if(idSongCurrent == 0){
										checkShowGreeting(lyricBack.getSumSong());
										return;
									}
									if (lyricItems != null) {
										setDisplayControl(true);
										mLyricItems = lyricItems;
										if (iPlayMP3)
											mHandlerInitMp3.sendEmptyMessage(0);
										else {
											playKaraoke1();
											MyLog.d(TAG, "==iFrishcheckPLay=="
													+ iFrishcheckPLay);
											displayTitle(0, 15000);
										}
									} else {
										MyLog.d(TAG, "==OnReceiveColorData NULL==");
										songNosupport();
									}
								}
							});
				} else {
					loadLyricSong(idSongCurrent, intMediaTypeCurrent, intMidiShiftTimeCurrent, intMainTypeABC, true);					
				}
			}
		}	
		
}
