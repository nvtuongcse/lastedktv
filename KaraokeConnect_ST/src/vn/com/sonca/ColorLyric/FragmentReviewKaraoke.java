package vn.com.sonca.ColorLyric;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

public class FragmentReviewKaraoke extends Fragment implements  OnColorLyricListener{

	private final String TAG = "FragmentReviewKaraoke";	
	private final int SPACE_PERCENT_RIGHT = 5;
	private final int LOOP_PERIOD = 3;
	private final int LOOP_MAX_TIMES = 10;
	private ViewFlipper myFlipperImage;	
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
	
	View view;
	private Context context;
	private Window window;
	
	private boolean iCoupleSinger=false;
	private RelativeLayout rootLayout;
	private RelativeLayout activity_karaoke_InfoSong;
	private OnReviewKaraokeFragmentListener listener;
	private TouchMainActivity mainActivity;
	private MediaPlayer mMediaPlayer; 
	 private Samba samba = null; 
    String IpDevice="";
   boolean iPlayVideoSamba=false;    

	private long timerPlayer = 0;
	
	public interface OnReviewKaraokeFragmentListener{		
		public void OnBackReviewKaraoke(boolean iError);
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		listener = (OnReviewKaraokeFragmentListener)activity;
		mainActivity = (TouchMainActivity) activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		MyLog.d(TAG, "==onCreateView==");
		view = inflater.inflate(R.layout.fragment_reviewkaraoke, container, false);
		context = getActivity().getApplicationContext();
		window = getActivity().getWindow();
		if(MyApplication.enableSamba){
			if(samba == null){//samba
				samba = new Samba(getActivity().getApplicationContext()); 
			}
		}
		
		initView();			
		
		OnCheckVideo();
		int id=getArguments().getInt("id");	
		int intMediaType=getArguments().getInt("intMediaType");		
		IpDevice= getArguments().getString("IP");
		loadLyricSong(id,intMediaType);
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
	}
	
	public void exitKaraoke(boolean iError){
		stopKaraoke();	
		if(iPlayVideoSamba){
			stopVideo();				
		}
		if(listener != null){
			listener.OnBackReviewKaraoke(iError);
		}
	}
	
	private void initView() {
		myFlipperImage = (ViewFlipper) view.findViewById(R.id.myFlipper);
		
		prepareFlipperImages();
		activity_karaoke_InfoSong = (RelativeLayout) view.findViewById(R.id.activity_karaoke_InfoSong);
		
		rootLayout=(RelativeLayout) view.findViewById(R.id.activity_karaoke_root);
		rootLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyLog.d(TAG, "=rootLayout click==");
				exitKaraoke(false);
			}
		});
		mVideoView = (VideoView) view.findViewById(R.id.myVideo);
		
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
				colorPanBody = new int[] { Color.rgb(0, 0, 255), Color.rgb(255, 0, 255), Color.rgb(255, 109, 0) }; // nam: blue, nu: pink
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
			String fontLyric = "ROBOTOBLACK.ttf";
			
			Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/" + fontLyric);
			setFontLyrics(tf, mTopLine, colorShowBorder, 14,Typeface.ITALIC);
			setFontLyrics(tf, mBottomLine, colorShowBorder, 14,Typeface.ITALIC);
			setFontLyrics(tf, mTopColorLine, colorPanBorder, 14,Typeface.ITALIC);
			setFontLyrics(tf, mBottomColorLine, colorPanBorder, 14,Typeface.ITALIC);
			setFontLyrics(tf, mText1Line, colorPanBorder, 14,Typeface.NORMAL);
			setFontLyrics(tf, mtxtTitleSong, colorPanBorder, 14,Typeface.NORMAL);
			setFontLyrics(tf, mtxtAuthorSong, colorPanBorder, 14,Typeface.NORMAL);
		} catch (Exception ex) {
			MyLog.e(TAG, ex);
		}
	}

	private void setFontLyrics(Typeface tf, TextView tv, int borderColor, int padding, int italic) {
		try {
			int padLeft = padding;
			int padTop = 6;
			int padRight = padding;
			int padBottom = 0;

			tv.setTypeface(tf, italic);
			//tv.setPadding(padLeft, padTop, padRight, padBottom);

		} catch (Exception ex) {
			MyLog.e(TAG, ex);
		}
	}
	
	private void setTextSizeFitScreen() {
		try {
				mTopLine.setText(getString(R.string.activity_karaoke_sample_string));
				screenWidthInPixels = mainActivity.getResources().getDisplayMetrics().widthPixels;
				for (int i = 1; i < 1000; i++) {
					mTopLine.setTextSize(i);
					mTopLine.measure(0, 0);
					if (mTopLine.getMeasuredWidth() >= (screenWidthInPixels * 70 / 100)) {
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
	
	private void loadLyricSong(int id, int intMediaType) {

		MyLog.d(" ", " ");MyLog.d(" ", " ");MyLog.d(" ", " ");
		MyLog.d(TAG, "===loadLyricSong start0==id=" + id+"=intMediaType="+intMediaType);
		MyLog.d(" ", " ");MyLog.d(" ", " ");MyLog.d(" ", " ");
	
		Cursor cursor = DBInterface.DBGetSongNumberCursor(context, id + "", 0, 0,
				MEDIA_TYPE.ALL);
		if (cursor.moveToFirst()) {
			int showEnable = cursor.getInt(14);
			if (showEnable == 1) { // PIANO or DANCE
				MyLog.e(TAG, "PIANO KIA PIANO KIA");							
				
				return;
			}
			id = cursor.getInt(0);
			int intExtraInfo = cursor.getInt(9);

			if ((intExtraInfo & 8) != 0) {
				iCoupleSinger = true;
			} else {
				iCoupleSinger = false;
			}

			MEDIA_TYPE mediaType=MEDIA_TYPE.values()[0];
			try{
			 mediaType = MEDIA_TYPE.values()[intMediaType];
			}catch(Exception e){}
			
			
			if (mediaType == MEDIA_TYPE.VIDEO || MyApplication.flagPlayingYouTube) {
				MyLog.d(TAG, "==mediaType is VIDEO=id="+id);
				if(MyApplication.intSvrModel ==MyApplication.SONCA_SMARTK  && MyApplication.enableSamba){
					((MyApplication)context).getLyricVidLink(""+id,"0");
					((MyApplication)context).setOnReceiverLyricVidLinkListener(new OnReceiverLyricVidLinkListener() {
						
						@Override
						public void OnReceiverLyricVidLink(String resultString) {
							// TODO Auto-generated method stub
							//String str="smb://;root:root@192.168.10.82/HDD/"+resultString.substring(26);
							
							MyLog.d(TAG, "=OnReceiverLyricVidLink=="+resultString);
							
							if(resultString == null || resultString.equals("") || resultString.equals("Khong co link")){
								resultString = "";										
								songNosupport();
							}else{
								if(resultString.startsWith("http")){
									MyLog.e(" ", " ");MyLog.e(" ", " ");
									MyLog.e(TAG, "SPECIAL WOWZA LINK");
									MyLog.e(" ", " ");MyLog.e(" ", " ");
									songNosupport();
//									iPlayVideoSamba=true;
//									playVideoDefault(resultString,true);
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
									}
								}
								
						}
					});
//					String str="smb://;root:root@"+IpDevice+"/HDD/sda1/KARAOKE/XUSER/KTV/120596.mkv";
//					iPlayVideoSamba=true;
//					String path=samba.getUrl(str, true);
//					playVideoDefault(path,true);
				}else{
					songNosupport();					
			
				}
				return;	
				
			}
			
			LoadColorLyric loadLyric = new LoadColorLyric(context, this
					.getActivity().getWindow());		
			Song objSong = new Song();
			objSong.setId(id);
			objSong.setTypeABC(100);
			loadLyric.setData(objSong, 0, true, 0);
			loadLyric.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

			loadLyric
					.setOnLoadColorLyricListener(new OnLoadColorLyricListener() {

						@Override
						public void OnReceiveColorData(Lyrics lyricItems) {
							
							if (lyricItems != null) {								
								mLyricItems = lyricItems;								
									playKaraoke1();									
									displayTitle(0, 15000);								
							} else {
								MyLog.d(TAG, "==OnReceiveColorData NULL==");
								songNosupport();
							}
						}
					});

		} else {
			MyLog.d(TAG, "==OnReceiveColorData No Song==");
			songNosupport();
		}
	}

	private void songNosupport(){	
		MyLog.d(TAG,"==songNosupport==");		
			if(iPlayVideoSamba){
				playVideoDefault(null, false);
			}
			mText1Line.setText(mainActivity.getResources().getString(R.string.activity_karaoke_noLyric));
			mText1Line.setVisibility(View.VISIBLE);					
	}
	
	private boolean playKaraoke1() {
		try {			
			mLyricItems.prepare(mTopLine);
			lyricDisplayStart();			
			return true;
		} catch (Exception ex) {
			MyLog.e(TAG, ex);
			return false;
		}
	}

	public void stopKaraoke() {
		try {	
			MyLog.d(TAG, "====stopKaraoke====");
			clear2Lines(0);			
			stopService();
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
		displayTitle(0, 0);		
		mText1Line.setVisibility(View.INVISIBLE);		
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
			return mainActivity.getResources().getDisplayMetrics().widthPixels;
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
			
			isStartingKaraoke = false;
			curCntdownWord = "";
			mText1Line.setVisibility(View.INVISIBLE);			
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
			
			timeLoopChangeColor = System.currentTimeMillis();
			timeLoopService = timeLoopChangeColor;
			timeStartCount = timeLoopChangeColor;
			timerPlayer=timeLoopChangeColor;
			startTimer();
			
		} catch (Exception ex) {
			MyLog.d(TAG, "=lyricDisplayStart fail==");
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
				mtxtAuthorSong.setText(mainActivity.getResources().getString(R.string.msg_kara_2) + " " +mLyricItems.getSongAuthor());
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
	
	
	private int GetCurrentTimeMillis(boolean bIsChangeColorService, boolean bIsSyncTimeWithMedia) {
		try {
			//MyLog.d(TAG, "=GetCurrentTimeMillis==");
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
					curTimePlayerMillis =(int)(timeNow-timerPlayer);
					
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
				//MyLog.i(TAG, "=changeLine=isChangeSentence="+isChangeSentence+"=savePauseTime="+savePauseTime+"=iEndLyric="+mLyricItems.iEndLyric());
				if(mLyricItems.iEndLyric()){
					MyLog.i(TAG, "=enddddddd===");
					exitKaraoke(false);
					return;
				}
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
						//MyLog.i(TAG, "=changeLine2=wordsCnt="+wordsCnt.getWord()+"=bIsEndBlock="+bIsEndBlock+"=iEndLyric="+mLyricItems.iEndLyric());
						
						if (wordsCnt != null && (wordsCnt.getWord().equals("0") || wordsCnt.getWord().equals("00")) && bIsEndBlock == false) {
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
								exitKaraoke(false);
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
	public void OnMain_Dance(boolean flagDance) {
		try {
			if(mainActivity.isDownloadingVideo){
				return;
			}
			
			MyLog.e(TAG, "OnMain_Dance -- flagDance = " + flagDance);
			//if(flagDance)
			//	exitKaraoke(false);	
		} catch (Exception e) {
		
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
				MyLog.d(TAG, "===onError==");
				if(iPlayVideoSamba)
					exitKaraoke(true);
				else{
				myFlipperImage.setVisibility(View.VISIBLE);
				mVideoView.setVisibility(View.GONE);
				startFlippingImage();
				}
				return true;
			}

		});
		
		mVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
			
			@Override
			public void onPrepared(IMediaPlayer mp) {
				//AudioManager  mAudioManager = (AudioManager)mainActivity.getSystemService(Context.AUDIO_SERVICE);
				// mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0); 
				// TODO Auto-generated method stub
				mMediaPlayer = (MediaPlayer) mp; 
				mMediaPlayer.setVolume(0, 0);
				//boolean iDecode=((tv.danmaku.ijk.media.player.MediaPlayer)mp).isHardwareDecode();
				//MyLog.d(TAG, "=onPrepared=="+iDecode);
				
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
		
		mVideoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
			
			@Override
			public void onCompletion(IMediaPlayer mp) {
				// TODO Auto-generated method stub
				MyLog.d(TAG, "=play video onCompletion==iPlayVideoSamba="+iPlayVideoSamba);
				if(iPlayVideoSamba)
					exitKaraoke(false);
				else
				playVideoDefault(null,false);
			}
		});
		
		/*mVideoView.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				playVideoDefault(null,false);
			}
		});*/
		
//		MediaController mc = new MediaController(mainActivity);
//		mVideoView.setMediaController(mc);
		//if(idTest==100115 && iSamba)
		//	path=android.os.Environment.getExternalStorageDirectory().toString().concat("/127777_H265.mkv");
		//MyLog.d(TAG,"id="+idTest+"=path=="+path);
		mVideoView.setVideoPath(path);

		mVideoView.requestFocus();
		if(!iSamba)
		mVideoView.start();
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
					if (!isTocURLReachable(mainActivity)) {
						// do nothing
						
						return null;
					}
					
					flagInterrupt = false;
					if(mainActivity != null){
						mainActivity.runOnUiThread(new Runnable() {
							public void run() {
								new Handler().postDelayed(new Runnable() {
									@Override
									public void run() {
										mainActivity.showDialogMessageKaraoke(mainActivity.getResources().getString(R.string.msg_kara_6));
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
			ConnectivityManager cm = (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
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
							mainActivity.getPackageName()));

			File parentFolder = new File(rootPath.concat("/FLIPPER"));

			int imageCount = parentFolder.listFiles().length;

			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.FILL_PARENT,
					RelativeLayout.LayoutParams.FILL_PARENT);

			for (int count = 0; count < imageCount; count++) {
				ImageView imageView = new ImageView(mainActivity);
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
	
		private ServerStatus myStatus;
		
		public void syncFromServer(ServerStatus status){
			
//			myStatus = status;
			
////			MyLog.e(" ", " ");
////			MyLog.e(TAG, "syncFromServer");
//			if (status.isSingerOn() != btnSinger.isSingerView()){
////				MyLog.d(TAG, "switch isSingerView");
//				btnSinger.setSingerView(status.isSingerOn());
//			}
//			
//			if (status.isPaused() == pauseView.getPauseView()){
////				MyLog.d(TAG, "switch pause");
//				pausePlayKaraoke(!status.isPaused());
//			}
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
			private void setCurrentPositionVideo(int position,int time) {
				
		    	if(mVideoView != null){
		    		
		    		MyLog.i(TAG, "=setCurrentPositionVideo="+position);
		    		
		    		stopTimer();
		    		//pauseVideo();
		    		
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

				@Override
				public void OnMain_PausePlay(boolean flagPlay) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void OnMain_NewSong(int songID, int intMediaType, int midiShifTime, int typeABC) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void OnMain_GetTimeAgain() {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void OnMain_setCntPlaylist(int i) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void OnMain_setNextSongPlaylist(String resultName, int resultID) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void OnMain_setCurrSongPlaylist(String resultName) {
					
				}

				@Override
				public void OnMain_VocalSinger(boolean flagVocalSinger) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void OnMain_CallVideoDefault() {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void OnMain_RemoveSocket() {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void OnMain_UpdateSocket(String serverName) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void OnMain_UpdateWifi() {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void OnMain_StartTimerAutoConnect() {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void OnMain_StopTimerAutoConnect() {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void OnMain_UpdateControl() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void OnMain_DownloadMidiResult(boolean result, int id) {
					// TODO Auto-generated method stub
					
				}
		  
}
