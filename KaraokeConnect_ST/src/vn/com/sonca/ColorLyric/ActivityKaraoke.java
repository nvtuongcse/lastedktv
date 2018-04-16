package vn.com.sonca.ColorLyric;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.ColorLyric.LoadColorLyric;
import vn.com.sonca.ColorLyric.LoadColorLyric.OnLoadColorLyricListener;
import vn.com.sonca.ColorLyric.MyPlayer;
import vn.com.sonca.ColorLyric.MyPlayer.OnPlayerListener;
import vn.com.sonca.ColorLyric.SentenceSong;
import vn.com.sonca.ColorLyric.SentenceTextView;
import vn.com.sonca.ColorLyric.SongColor;
import vn.com.sonca.Lyric.LyricBack;
import vn.com.sonca.Lyric.LyricBack.OnBackLyric;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.params.Song;
import vn.com.sonca.utils.AppConfig.LANG_INDEX;
import vn.com.sonca.zktv.main.KTVMainActivity;
import vn.com.sonca.zzzzz.MyApplication;
import vn.com.sonca.zzzzz.MyApplication.OnReceiverInfoLyricListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.RelativeLayout.LayoutParams;

public class ActivityKaraoke extends FragmentActivity implements OnPlayerListener, OnErrorListener{
	private final int ERROR_AUDIO = 1;
	private String audioUrl;
	private Context context;
	private final String TAG = "ActivityKaraoke";
	private final int LOOP_PERIOD = 3;
	private final int LOOP_MAX_TIMES = 10;
	private int mRandomBackground = 0;
	private List<Integer> mListBackgroundPortrait;
	private List<Integer> mListBackground;
	private MyPlayer mPlayer;
	public SongColor mSong;	
	private Lyrics mLyricItems;
	
	private boolean mCanUseVitamo;
	private boolean mIsStarted;

	private TextView mTopLine;
	private TextView mBottomLine;
	private ImageView mImageCounter;
	private SentenceTextView mTopColorLine;
	private SentenceTextView mBottomColorLine;
	private ImageView mImagePrevious;
	private ImageView mImageFavourite;
	private ImageView mImageRepeat;
	private ImageView mImagePause;
	private ImageView mImageStop;
	private ImageView mImageFullScreen;
	private ImageView mImageRecorder;
	private ImageView mImageSinger;
	private ImageView mImgLyricBackground;
	private TextView mNextSong;
	private TextView mRemainSong;
	private TextView mTitleSong;
	private TextView mRecorderTime;
	private View mLayoutMenu;
	private View mLayoutTitle;
	private View mLayoutVideo;
	private View mLayoutOption;
	private View mLayoutRecoder;
	private View mLayoutLeftControl;
	private View mLayoutRightControl;
	private ImageView mLayoutVideoBackground;
	private VideoView mVideoView;
		
	private boolean isStarted = true;
	private int currentLyricBlock = 0;
	private boolean bIsShowLastLine = false;
	private boolean bIsShowPanLine = false;
	private SentenceSong sentence1 = null;
	private SentenceSong sentence2 = null;
	private SentenceSong lastSentence = null;

	private int colorShowBody[];
	private int colorShowBorder;
	private int colorPanBody;
	private int colorPanBorder;

	protected boolean isDisplayLyric = true;
	protected boolean isPlayNow = false;

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
	
	private int textSize;
	private int screenWidthInPixels;
	private int maxWidthOfTextSize;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		MyLog.d(TAG, "==onCreate start==");
		super.onCreate(savedInstanceState);
		context = this.getApplicationContext();
//		setContentView(R.layout.activity_karaoke);
		initView();
		
		loadLyricSong();
		
		LyricBack lyricBack = (LyricBack)findViewById(R.id.lyricBack);
		lyricBack.setTitleView(getResources().getString(R.string.painting_lyric_1));
		lyricBack.setOnBackLyric(new OnBackLyric() {
			@Override public void OnBack() {
				closeActivity(true,RESULT_OK);
				//playKaraoke();
			}
		});
		MyLog.d(TAG, "==onCreate end==");
	}

	private void initView() {
		mTopLine = (TextView) findViewById(R.id.activity_karaoke_texttopline);
		

		mTopColorLine = (SentenceTextView) findViewById(R.id.activity_karaoke_colortopline);
		

		mBottomLine = (TextView) findViewById(R.id.activity_karaoke_textbottomline);
		

		mBottomColorLine = (SentenceTextView) findViewById(R.id.activity_karaoke_colorbottomline);
		

		mImageCounter = (ImageView) findViewById(R.id.activity_karaoke_counter_image);
		
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
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		((MyApplication) getApplication()).cancelSyncServerStatus();
		((MyApplication) getApplication()).disconnectFromRemoteHost();
	}

	@Override 
	public void onAttachedToWindow() {
		try {
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		} catch (Exception ex) {
			MyLog.e(TAG, ex);
		}
		super.onAttachedToWindow();
	}
	
	
	private void initFontColor() {
		try {
			int selectedColor = 0;
			
			colorShowBody = new int[] { Color.rgb(255, 255, 0), Color.rgb(0, 255, 0) }; // man: yellow, woman: pink
			colorShowBorder = Color.rgb(0, 0, 160);// dark blue
			colorPanBody = Color.rgb(0, 0, 255);// blue
			colorPanBorder = Color.rgb(255, 255, 255); // white

			switch (selectedColor) {
			case 0:
				colorShowBody = new int[] { Color.rgb(255, 255, 0), Color.rgb(255, 128, 0) }; // nam: vang, nu: orange
				colorShowBorder = Color.rgb(0, 0, 0);// den
				colorPanBody = Color.rgb(0, 0, 255); // blue
				colorPanBorder = Color.rgb(255, 255, 255);// trang
				break;
			case 1:
				colorShowBody = new int[] { Color.rgb(0, 128, 0), Color.rgb(255, 0, 128) }; // nam: xanh dam, nu: hong
				colorShowBorder = Color.rgb(255, 255, 255);// trang
				colorPanBody = Color.rgb(255, 255, 0); // yellow
				colorPanBorder = Color.rgb(255, 0, 0); // red
				break;
			case 2:
				colorShowBody = new int[] { Color.rgb(0, 255, 255), Color.rgb(255, 128, 255) }; // nam: xanh ngoc, nu: hong
				colorShowBorder = Color.rgb(0, 0, 0);// den
				colorPanBody = Color.rgb(255, 0, 0); // red
				colorPanBorder = Color.rgb(255, 255, 255);// trang
				break;
			}
		} catch (Exception ex) {			
			MyLog.e(TAG, ex);
		}
	}

	private void setDisplayFontForLanguage() {
		try {
			String fontLyric = "arialbd.ttf";
			Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/" + fontLyric);
			setFontLyrics(tf, mTopLine, colorShowBorder, 14);
			setFontLyrics(tf, mBottomLine, colorShowBorder, 14);
			setFontLyrics(tf, mTopColorLine, colorPanBorder, 14);
			setFontLyrics(tf, mBottomColorLine, colorPanBorder, 14);
		} catch (Exception ex) {
			MyLog.e(TAG, ex);
		}
	}

	private void setFontLyrics(Typeface tf, TextView tv, int borderColor, int padding) {
		try {
			int padLeft = padding;
			int padTop = 6;
			int padRight = padding;
			int padBottom = 0;

			tv.setTypeface(tf, Typeface.BOLD);
			tv.setPadding(padLeft, padTop, padRight, padBottom);

		} catch (Exception ex) {
			MyLog.e(TAG, ex);
		}
	}
	
	private void setTextSizeFitScreen() {
		try {
				mTopLine.setText(getString(R.string.activity_karaoke_sample_string));
				screenWidthInPixels = getResources().getDisplayMetrics().widthPixels;
				for (int i = 1; i < 1000; i++) {
					mTopLine.setTextSize(i);
					mTopLine.measure(0, 0);
					if (mTopLine.getMeasuredWidth() >= (screenWidthInPixels * 80 / 100)) {
						maxWidthOfTextSize = i;
						break;
					}
				}
				
				mTopColorLine.setTextSize(maxWidthOfTextSize);
				mBottomColorLine.setTextSize(maxWidthOfTextSize);
				mTopLine.setTextSize(maxWidthOfTextSize);
				mBottomLine.setTextSize(maxWidthOfTextSize);
				mTopLine.setText("");
				textSize = maxWidthOfTextSize;
			
		} catch (Exception ex) {
			MyLog.e(TAG, ex);
		}
	}

	private void FixSizeBackGroundLyric() {
		try {
			int heightOfBackground = 0;
			mTopLine.setText("A");
			mTopLine.measure(0, 0);
			heightOfBackground += mTopLine.getMeasuredHeight();

			mBottomLine.setText("B");
			mBottomLine.measure(0, 0);
			heightOfBackground += mBottomLine.getMeasuredHeight();

			mTopLine.setText("");
			mBottomLine.setText("");
		} catch (Exception ex) {
			MyLog.e(TAG, ex);
		}
	}
	
	private void loadLyricSong() {
		if (((MyApplication) getApplication()).getSocket() == null) {	
			((MyApplication) getApplication()).onStart();
		}		
		
		// TODO Auto-generated method stub
		audioUrl="/mnt/sdcard/Download/music.mp3";
		 //láº¥y intent gá»�i Activity nĂ y
		 Intent callerIntent=getIntent();
		 //cĂ³ intent rá»“i thĂ¬ láº¥y Bundle dá»±a vĂ o MyPackage
		 Bundle packageFromCaller= callerIntent.getBundleExtra("MyPackage");
		 //CĂ³ Bundle rá»“i thĂ¬ láº¥y cĂ¡c thĂ´ng sá»‘ dá»±a vĂ o soa, sob
		 int id=packageFromCaller.getInt("id");
		 MyLog.d(TAG, "===loadLyricSong start==id="+id);
		LoadColorLyric loadLyric = new LoadColorLyric(context, this.getWindow());
		Song objSong = new Song();
		//objSong.setId(5855); 
		objSong.setId(id); 
		//objSong.setId(803175); 
		
//		loadLyric.setData(objSong , 0);
		loadLyric.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
		
		loadLyric.setOnLoadColorLyricListener(new OnLoadColorLyricListener() {
			
			@Override
			public void OnReceiveColorData(Lyrics lyricItems) {
				if(lyricItems != null){
					mLyricItems = lyricItems;
					MyLog.d(TAG,"=LyricView="+ mLyricItems.getLyricView()+"=CountStart="+ mLyricItems.getLastSentences(0)
							+"=Sentence="+ mLyricItems.getSentenceFromPosition(0)+"=getSongAuthor="+ mLyricItems.getSongAuthor()+"=getSongSinger="+ mLyricItems.getSongSinger()+
							"=getSongTitle="+ mLyricItems.getSongTitle()+"=CountStart="+ mLyricItems.getCountStartFromPosition(0));		
					if(iPlayMP3)
						mHandlerInitMp3.sendEmptyMessage(0);
					else
						playKaraoke1();
				} else {
					MyLog.d(TAG,"NULL NULL NULL NULL	");						
				}
			}
		});
		
		//audioUrl="http://kos.soncamedia.com/v1300/pub.aspx?transID=w3cbsetbya1tzo2dwxaasjiq&task=LoadFileMP3&index=000001&device=Android";
		
//		mLyricItems = new Lyrics(loadLyric.getLyricItems());
//		MyLog.d(TAG,"=LyricsView="+ mLyricItems.getLyricView()+"=CountStart="+ mLyricItems.getLastSentences(0)
//				+"=Sentence="+ mLyricItems.getSentenceFromPosition(0)+"=getSongAuthor="+ mLyricItems.getSongAuthor()+"=getSongSinger="+ mLyricItems.getSongSinger()+
//				"=getSongTitle="+ mLyricItems.getSongTitle()+"=CountStart="+ mLyricItems.getCountStartFromPosition(0));
		
		//mHandlerInitMp3.sendEmptyMessage(0);
		
		
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
			MyLog.d(TAG, "===playKaraoke1 start=="+mPlayer);			
			
			mLyricItems.prepare(mTopLine);
			lyricDisplayStart();
			
			
			MyLog.d(TAG, "===playKaraoke1 end==");
			return true;
		} catch (Exception ex) {
			MyLog.e(TAG, ex);
			return false;
		}
	}
	
	
	private void closeActivity(boolean stop, int result) {
		try {
			MyLog.d(TAG, "===closeActivity start==stop="+stop);
			if (stop == true)
				stopKaraoke();
			
			mHandlerCloseActivity.sendEmptyMessage(result);
		} catch (Exception ex) {
			MyLog.d(TAG, "===closeActivity end===");
			MyLog.e(TAG, ex);
		}
	}
	
	private Handler mHandlerCloseActivity = new Handler(new Handler.Callback() {
		@Override public boolean handleMessage(Message msg) {
			try {
				setResult(msg.what);
				Intent mainIntent = new Intent(ActivityKaraoke.this,
						TouchMainActivity.class);
				if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
					mainIntent = new Intent(ActivityKaraoke.this,
							KTVMainActivity.class);
				}
				startActivity(mainIntent);
				finish();				
			} catch (Exception ex) {
				MyLog.e(TAG, ex);
			}
			return false;
		}
	});
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
	private void stopKaraoke() {
		try {	
			MyLog.d(TAG, "====stopKaraoke====");
			stopPlayer();
			stopService();
			stopTimerLyricInfo();
			clear2Lines(0);			
			displayCountdown("");

			if (mSong != null)
				mSong = null;
			if (mImageCounter != null)
				mImageCounter.setImageDrawable(null);

			System.gc();
		} catch (Exception ex) {
			MyLog.e(TAG, ex);
		}
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
					@Override public void run() {
						try {
							MyLog.d(TAG, "==clear2Lines==");
							if (mTopLine != null)
								mTopLine.setText("");

							if (mTopColorLine != null) {
								mTopColorLine.setText("");
								mTopColorLine.getLayoutParams().width = mTopLine.getLayoutParams().width;
							}

							if (mBottomLine != null)
								mBottomLine.setText("");

							if (mBottomColorLine != null) {
								mBottomColorLine.setText("");
								mBottomColorLine.getLayoutParams().width = mBottomLine.getLayoutParams().width;
							}
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
					MyLog.d(TAG, "==displayCountdown=isStartingKaraoke="+isStartingKaraoke+"==CntdownWord="+CntdownWord);
					if (CntdownWord.equalsIgnoreCase("3")) {
						mImageCounter.setImageResource(R.drawable.ic_red_counter3);
					} else if (CntdownWord.equalsIgnoreCase("2")) {
						mImageCounter.setImageResource(R.drawable.ic_yellow_counter2);
					} else if (CntdownWord.equalsIgnoreCase("1")) {
						mImageCounter.setImageResource(R.drawable.ic_blue_counter1);
					}
					if (isStartingKaraoke) {
//						LayoutParams param = (LayoutParams) mImageCounter.getLayoutParams();
//						if (param != null) {
//							int left = 0;
//							if (sentence1 != null)
//								left = (getWidthScreen() - (int) (sentence1.getWidthOfText() + mTopLine.getPaddingLeft() + mTopLine.getPaddingRight())) / 2;
//							mImageCounter.measure(0, 0);
//							left -= mImageCounter.getMeasuredWidth();
//							if (left < 0)
//								left = 0;
//							param.setMargins(left, 0, 0, 0);
//							mImageCounter.setLayoutParams(param);
//						}
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
	
	private void lyricDisplayStart() {
		try {
			MyLog.d(TAG, "=lyricDisplayStart==");
			currentLyricBlock = 0;
			curLyricMaxWidth = 0;
			curLyricSize = 0;

			isStartingKaraoke = false;
			curCntdownWord = "";

			timeLoopChangeColor = System.currentTimeMillis();
			timeLoopService = timeLoopChangeColor;
			timeStartCount = timeLoopChangeColor;

			savePauseTime = 0;
			saveTimePlayerCurrentInMsec = 0;
			isChangeSentence = true;

			mTopLine.setText("");
			mTopColorLine.setText("");

			mBottomLine.setText("");
			mBottomColorLine.setText("");

			startService();
		} catch (Exception ex) {
			MyLog.d(TAG, "=lyricDisplayStart fail==");
			MyLog.e(TAG, ex);
			
		}
	}
	
	private void startService() {
		try {
			MyLog.d(TAG, "=startService start==");
			startTimer();
			displayTitle(0, 20000);
			startTimerLyricInfo();
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
			if (bIsShow) {
				mTopColorLine.setText("");
				mBottomColorLine.setText("");
				mTopLine.setText("");
				mBottomLine.setText("");
			}
		} catch (Exception ex) {
			MyLog.e(TAG, ex);
		}
	}
	
	private int getWidthScreen() {
		try {
			return getResources().getDisplayMetrics().widthPixels;
		} catch (Exception ex) {
			MyLog.e(TAG, ex);
			return 0;
		}
	}
	
	
	
	private Handler mHandlerInitMp3 = new Handler(new Handler.Callback() {
		private void initMp3Song() {
			try {
				MyLog.d(TAG, "==initMp3Song===");
				if (mPlayer == null) {
					mPlayer = new MyPlayer(ActivityKaraoke.this);
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
			//MyLog.d(TAG, "=GetCurrentTimeMillis0=timeNow="+timeNow+"=timeLoopChangeColor="+timeLoopChangeColor+"=timeLoopService="+timeLoopService);
			if (timeNow > 0 && timePrev > 0 && timeNow > timePrev) {
				timeReference = timeNow;
				if (bIsChangeColorService)
					timeLoopChangeColor = timeReference;
				else
					timeLoopService = timeReference;

				curTimeDateMillis = (int) (timeReference - timeStartCount);
				if (curTimeDateMillis < 0)
					curTimeDateMillis = 0;

				if (bIsSyncTimeWithMedia || bIsEndBlock || curLyricSize >= curLyricMaxWidth || savePauseTime != 0) {
					int curTimePlayerMillis=0;
					if(iPlayMP3)
						curTimePlayerMillis = mPlayer.getCurrentPlaybackPosition();
					else
						curTimePlayerMillis =timerPlayer/90;// TODO SK9xxx/90->milsecon, # ko chia
					if (saveTimePlayerCurrentInMsec != curTimePlayerMillis && curTimeDateMillis != curTimePlayerMillis) {
						curTimeDateMillis = curTimePlayerMillis;
						timeStartCount = timeReference - curTimeDateMillis;
						saveTimePlayerCurrentInMsec = curTimePlayerMillis;
					}
					//MyLog.d(TAG, "=GetCurrentTimeMillis1==curTimePlayerMillis="+curTimePlayerMillis+"==="+timeNow);
					
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
					if (s == null || (currentPosition > s.getTimeStart() + s.getTimeLenght() + 10)) // Here is start																						// lyrics
					{
						WordSong wordsCnt = mLyricItems.getCountStartFromPosition(currentPosition);
						//MyLog.i(TAG, "=changeLine2=wordsCnt="+wordsCnt+"=s="+s);
						
						if (wordsCnt != null && (wordsCnt.getWord().equals("0") || wordsCnt.getWord().equals("00")) && bIsEndBlock == false) {
							MyLog.i(TAG, "=changeLine2=wordsCnt="+wordsCnt.getWord()+"=bIsEndBlock="+bIsEndBlock+"=bIsShowLastLine="+bIsShowLastLine);
							bIsEndBlock = true;

							curLyricSize = 0;
							curLyricSizePanDone = 0;
							curLyricMaxWidth = 0;
							curCntdownWord = "";
							bIsShowLastLine = false;
							bIsShowPanLine = false;

							clear2Lines(300);
							if (wordsCnt.getWord().equals("0")) { // show title
								displayTitle(2000, 20000);
							}else if (wordsCnt.getWord().equals("00")) { // show title
								MyLog.i(TAG, "=changeLine3=iPlayMP3="+iPlayMP3);
								
								if(!iPlayMP3)
									stopKaraoke();
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
										}
										saveCurLyricSize = curLyricSize;
										if (saveCurLyricSize > 0)
											bIsShowPanLine = false;
									}
									if ((sentence1 != s) || (!bIsShowLastLine && mLyricItems.isLastSentences(currentPosition))) {
										if (!bIsShowLastLine && mLyricItems.isLastSentences(currentPosition)) {
											bIsShowLastLine = true;
										}
										saveCurLyricSize = 0;
										curLyricSize = 0;
										curLyricSizePanDone = 0;
										sentence1 = s;
										mTopColorLine.getLayoutParams().width = 0;
										mTopColorLine.setTextColor(colorPanBody);
										mTopLine.setTextColor(colorShowBody[sentence1.getType()]);
										mTopLine.setText(s.getSentence());
										//MyLog.i(TAG, "=changeLine4=currentPosition="+currentPosition+"=s="+s.getLine());
										
									}
								}
							});
						} else {
							mBottomLine.post(new Runnable() {
								@Override public void run() {
									if (saveCurLyricSize == 0 || saveCurLyricSize != curLyricSize) {
										if (saveCurLyricSize == 0 && !bIsShowPanLine) {
											mBottomColorLine.setText(s.getSentence());
											bIsShowPanLine = true;
										}
										saveCurLyricSize = curLyricSize;
										if (saveCurLyricSize > 0)
											bIsShowPanLine = false;
									}

									if ((sentence2 != s) || (!bIsShowLastLine && mLyricItems.isLastSentences(currentPosition))) {
										if (!bIsShowLastLine && mLyricItems.isLastSentences(currentPosition)) {
											bIsShowLastLine = true;
										}
										saveCurLyricSize = 0;
										curLyricSize = 0;
										curLyricSizePanDone = 0;
										sentence2 = s;
										mBottomColorLine.getLayoutParams().width = 0;
										mBottomColorLine.setTextColor(colorPanBody);
										mBottomLine.setTextColor(colorShowBody[sentence2.getType()]);
										mBottomLine.setText(s.getSentence());
										//MyLog.i(TAG, "=changeLine5=currentPosition="+currentPosition+"=s="+s.getLine());
										
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
										mTopColorLine.setText(s1.getSentence());
										if (sentence1 != s1) {
											sentence1 = s1;
											mTopColorLine.getLayoutParams().width = 0;
											mTopColorLine.setTextColor(colorPanBody);
											mTopLine.setTextColor(colorShowBody[sentence1.getType()]);
											//MyLog.i(TAG, "=changeLine7=currentPosition="+currentPosition+"=sentence1="+sentence1);
											
										}
									}
								});
							} else {
								mBottomLine.post(new Runnable() {
									@Override public void run() {
										mBottomLine.setText(s1.getSentence());
										mBottomColorLine.setText(s1.getSentence());
										if (sentence2 != s1) {
											sentence2 = s1;
											mBottomColorLine.getLayoutParams().width = 0;
											mBottomColorLine.setTextColor(colorPanBody);
											mBottomLine.setTextColor(colorShowBody[sentence2.getType()]);
											//MyLog.i(TAG, "=changeLine7=currentPosition="+currentPosition+"=sentence2="+sentence2);
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
									if (curCntdownWord.equalsIgnoreCase("4")) {
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
									} else if (curCntdownWord.equalsIgnoreCase("3") || curCntdownWord.equalsIgnoreCase("2") || curCntdownWord.equalsIgnoreCase("1")) {
										isStartingKaraoke = true;
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
						if (index != -1) {
							if (isStartingKaraoke) {
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
								if (!s.getSentence().equals(mTopColorLine.getText().toString())) {
									return false;
								}
								updatePanWidth(mTopColorLine);
							} else {
								if (!s.getSentence().equals(mBottomColorLine.getText().toString())) {
									return false;
								}
								updatePanWidth(mBottomColorLine);
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

	private void updatePanWidth(SentenceTextView tvColorLine) {
		try {
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tvColorLine.getLayoutParams();
			//MyLog.d(TAG, "=updatePanWidth=width="+params.width+"=curLyricSize="+curLyricSize+"=curLyricMaxWidth="+curLyricMaxWidth);
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
	public boolean onError(MediaPlayer mp, int what, int extra) {
		// TODO Auto-generated method stub
		return false;
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
	private int PeriodGetTime=3000;
	private boolean iPlayMP3=false;
	
	private void startTimerLyricInfo() {
		stopTimerLyricInfo();
		MyLog.e("startTimerLyricInfo", ".....................");
		timerLyricInfo = new Timer();
		timerLyricInfo.schedule(new LyricInfoSyncTask(), 0, PeriodGetTime);//lai
	}

	private void stopTimerLyricInfo() {
		MyLog.e("stopTimerLyricInfo", ".....................");
		if (timerLyricInfo != null) {
			timerLyricInfo.cancel();
			timerLyricInfo = null;
		}		
	}
	
	class LyricInfoSyncTask extends TimerTask {
		@Override
		public void run() {
			if (((MyApplication)getApplication()).getSocket() == null) {
				MyLog.e(TAG,"==LyricInfoSyncTask socket null==");
				return;
			}
			try {
				runOnUiThread(new Runnable() {
					public void run() {							
						
							((MyApplication)getApplication()).getLyricInfo();
							((MyApplication)getApplication()).setOnReceiverInfoLyricListener(new OnReceiverInfoLyricListener() {
								
								@Override
								public void OnReceiverInfoLyric(String resultString) {
									if(resultString == null){
										resultString = "";
									}else{
										timerPlayer=Integer.parseInt(resultString);
									}
									
									//MyLog.e(TAG,"==LyricInfoSyncTask=="+ resultString+"=timerPlayer="+timerPlayer);
								}
							});		
							
						}
					
				});
			} catch (Exception e) {
				e.printStackTrace();
			}		
		}
	}
}
