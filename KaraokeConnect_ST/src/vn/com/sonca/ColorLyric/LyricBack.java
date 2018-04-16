package vn.com.sonca.ColorLyric;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.database.DBInterface;
import vn.com.sonca.params.Song;
import vn.com.sonca.utils.AppConfig.MEDIA_TYPE;
import vn.com.sonca.zktv.main.KTVMainActivity;
import vn.com.sonca.zzzzz.MainActivity;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class LyricBack extends View {
	private final String TAG = "LyricBack";
	private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint mainText = new Paint(Paint.ANTI_ALIAS_FLAG);
	private boolean isPlay = true;
	private String currectSong = "";	
	private Context context;	
	
	public LyricBack(Context context) {
		super(context);
		initView(context);
	}

	public LyricBack(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public LyricBack(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private OnBackLyric listener;
	public interface OnBackLyric{
		public void OnBack();
	}
	
	public void setOnBackLyric(OnBackLyric listener){
		this.listener = listener;
	}
	
	
	private Drawable drawable,drawablePlaylist,drawableStatusPlay,drawableStatusPause;	
	private Drawable zlightdrawable,zlightdrawablePlaylist,zlightdrawableStatusPlay,zlightdrawableStatusPause;
	
	private Drawable drawStatusNext, drawMidi, drawKTVMid, drawKTVVid;
	private Drawable zlightdrawStatusNext, zlightdrawMidi, zlightdrawKTV;
	
	private Drawable drawDevice, zlightDevice;
	private Drawable drawWifi, zlightDrawWifi;
	
	private Drawable drawWifi0, drawWifi1, drawWifi2, drawWifi3, drawWifi4;
	private Drawable drawWifi0_hiw, drawWifi1_hiw, drawWifi2_hiw, drawWifi3_hiw, drawWifi4_hiw;
	private Drawable drawWifi0_smartk, drawWifi1_smartk, drawWifi2_smartk, drawWifi3_smartk, drawWifi4_smartk;
	
	private Drawable zlight_drawWifi0, zlight_drawWifi1, zlight_drawWifi2, zlight_drawWifi3, zlight_drawWifi4;
	private Drawable zlight_drawWifi0_hiw, zlight_drawWifi1_hiw, zlight_drawWifi2_hiw, zlight_drawWifi3_hiw, zlight_drawWifi4_hiw;
	private Drawable zlight_drawWifi0_smartk, zlight_drawWifi1_smartk, zlight_drawWifi2_smartk, zlight_drawWifi3_smartk, zlight_drawWifi4_smartk;
	
	private Drawable drawablePlaylist_Off, zlight_drawablePlaylist_Off;
	
	//private String nameButton = "";
	//private String cntPlaylist = "0";
	private void initView(Context context) {
		this.context = context;		
		CreateTimer();
		//nameButton = getResources().getString(R.string.lyric_0);
		drawable = getResources().getDrawable(R.drawable.pbstatus_next_ipad);
		drawablePlaylist= getResources().getDrawable(
				R.drawable.touch_tab_playlist_hover_144x144);
		drawableStatusPlay = getResources().getDrawable(
				R.drawable.touch_pbstatus_play_48x48);
		drawableStatusPause = getResources().getDrawable(
				R.drawable.touch_pbstatus_pause_48x48);
		
		drawablePlaylist_Off = getResources().getDrawable(
				R.drawable.touch_tab_playlist_inactive_65x65);
		
		zlightdrawable = getResources().getDrawable(R.drawable.zlight_pbstatus_next);
		zlightdrawablePlaylist= getResources().getDrawable(
				R.drawable.zlight_tab_playlist_inactive_144x144);
		zlightdrawableStatusPlay = getResources().getDrawable(
				R.drawable.zlight_pbstatus_play_48x48);
		zlightdrawableStatusPause = getResources().getDrawable(
				R.drawable.zlight_pbstatus_pause_48x48);
		
		zlight_drawablePlaylist_Off = getResources().getDrawable(
				R.drawable.zlight_tab_playlist_inactive_144x144);
		
		drawStatusNext = getResources().getDrawable(
				R.drawable.pbstatus_next_ipad);
		drawMidi = getResources().getDrawable(
				R.drawable.tomau_midi);
		drawKTVMid = getResources().getDrawable(R.drawable.tomau_ktv_midi);
		drawKTVVid = getResources().getDrawable(R.drawable.tomau_ktv_video);
					
		zlightdrawStatusNext = getResources().getDrawable(
				R.drawable.zlight_pbstatus_next);
		zlightdrawMidi = getResources().getDrawable(
				R.drawable.zlight_image_midi_50x35);
		zlightdrawKTV = getResources().getDrawable(
				R.drawable.zlight_ktv_50x30);
		
		zlight_drawablePlaylist_Off = getResources().getDrawable(
				R.drawable.zlight_tab_playlist_inactive_144x144);
		
		drawDevice = getResources().getDrawable(R.drawable.touch_daumay_chuaketnoi_56x50);
		
		drawWifi0 = getResources().getDrawable(R.drawable.wifi_0);
		drawWifi1 = getResources().getDrawable(R.drawable.wifi_1);
		drawWifi2 = getResources().getDrawable(R.drawable.wifi_2);
		drawWifi3 = getResources().getDrawable(R.drawable.wifi_3);
		drawWifi4 = getResources().getDrawable(R.drawable.wifi_4);
		
		drawWifi0_hiw = getResources().getDrawable(R.drawable.ktvwifi_0);
		drawWifi1_hiw = getResources().getDrawable(R.drawable.ktvwifi_1);
		drawWifi2_hiw = getResources().getDrawable(R.drawable.ktvwifi_2);
		drawWifi3_hiw = getResources().getDrawable(R.drawable.ktvwifi_3);
		drawWifi4_hiw = getResources().getDrawable(R.drawable.ktvwifi_4);
		
		drawWifi0_smartk = getResources().getDrawable(R.drawable.wifi_9108_0);
		drawWifi1_smartk = getResources().getDrawable(R.drawable.wifi_9108_1);
		drawWifi2_smartk = getResources().getDrawable(R.drawable.wifi_9108_2);
		drawWifi3_smartk = getResources().getDrawable(R.drawable.wifi_9108_3);
		drawWifi4_smartk = getResources().getDrawable(R.drawable.wifi_9108_4);	
				
		zlight_drawWifi0 = getResources().getDrawable(R.drawable.zlight_wifi_0);
		zlight_drawWifi1 = getResources().getDrawable(R.drawable.zlight_wifi_1);
		zlight_drawWifi2 = getResources().getDrawable(R.drawable.zlight_wifi_2);
		zlight_drawWifi3 = getResources().getDrawable(R.drawable.zlight_wifi_3);
		zlight_drawWifi4 = getResources().getDrawable(R.drawable.zlight_wifi_4);
		
		zlight_drawWifi0_hiw = getResources().getDrawable(R.drawable.zlight_ktvwifi_0);
		zlight_drawWifi1_hiw = getResources().getDrawable(R.drawable.zlight_ktvwifi_1);
		zlight_drawWifi2_hiw = getResources().getDrawable(R.drawable.zlight_ktvwifi_2);
		zlight_drawWifi3_hiw = getResources().getDrawable(R.drawable.zlight_ktvwifi_3);
		zlight_drawWifi4_hiw = getResources().getDrawable(R.drawable.zlight_ktvwifi_4);
		
		zlight_drawWifi0_smartk = getResources().getDrawable(R.drawable.zlight_wifi_9108_0);
		zlight_drawWifi1_smartk = getResources().getDrawable(R.drawable.zlight_wifi_9108_1);
		zlight_drawWifi2_smartk = getResources().getDrawable(R.drawable.zlight_wifi_9108_2);
		zlight_drawWifi3_smartk = getResources().getDrawable(R.drawable.zlight_wifi_9108_3);
		zlight_drawWifi4_smartk = getResources().getDrawable(R.drawable.zlight_wifi_9108_4);
			
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	private int widthLayout = 0;
	private int heightLayout = 0;
	private float nameX, nameY, nameS;	
	private float cntX;	
	private Rect rectMIDI_KTV = new Rect();
	private Rect rectDrawable = new Rect();
	private Rect rectDrawablePlaylist = new Rect();
	private Rect rectDrawableStatusPlay = new Rect();
	private float nameServerS, nameServerY;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		widthLayout = w;
		heightLayout = h;
		
		int tamX = (int) (95 * widthLayout / 1980);
		int tamY = (int) (0.35*heightLayout);
		int height = (int) (0.3*heightLayout);
		int width = 71*height/65;
		rectDrawable.set(tamX - width, tamY - height, tamX + width, tamY + height);
		
		tamX = (int) (220 * widthLayout / 1980);
		tamY = (int) (0.5*heightLayout);
		height = (int) (0.5*heightLayout);
		width =height;
		rectDrawableStatusPlay.set(tamX - width, tamY - height, tamX + width, tamY + height);
		int right=tamX + width;
		tamX = (int) (0.92*widthLayout);
		
		rectDrawablePlaylist.set(tamX - width, tamY - height, tamX + width, tamY + height);
		
		nameServerS = 20f * heightLayout / 100;
		nameServerY = 85f * heightLayout / 100;

		nameS = (int) (0.5 * h);
		mainText.setTextSize(nameS);
		
		nameX=(int) (right + 10 * widthLayout / 1980);
		//nameX = (float)(widthLayout/2 - mainText.measureText(nameButton)/2);
		nameY = (float) (0.5*h + nameS/3);

		cntX = (tamX + width-10);
		
		tamX = (int) (right + 40 * widthLayout / 1980);
		tamY = (int) (0.5*heightLayout);
		height = (int) (0.3*heightLayout);
		width = 50*height/35;
		rectMIDI_KTV.set(tamX - width, tamY - height, tamX + width, tamY + height);	
	}
	
	private float maxLenghtSongName = 0;
	private float maxLenghtSingerName = 0;
	
	private boolean isConnected = true;
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//MyLog.d(TAG, "=onDraw=currectSong="+currectSong+"=sumSong="+sumSong+"=isPlay="+isPlay+"=flagStateNextSong="+flagStateNextSong);
		String strStatus = "";
				
		String str="";
		
		isConnected = TouchMainActivity.serverStatus != null;
		if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			isConnected = KTVMainActivity.serverStatus != null;
		}
				
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			
			mainPaint.setStyle(Style.FILL);
			mainPaint.setARGB(99, 0, 0, 0);
			canvas.drawRect(0, 0, widthLayout, heightLayout, mainPaint);
			
			if(isConnected){
				drawDevice = getResources().getDrawable(
						R.drawable.icon_daumay_daketnoi_71x65);
				if(MyApplication.intSvrModel == MyApplication.SONCA_HIW){
					drawDevice = getResources().getDrawable(
							R.drawable.icon_daumay_ktvwifi_bottom);
				}
				if(MyApplication.intSvrModel == MyApplication.SONCA_KM2){
					drawDevice = getResources().getDrawable(
							R.drawable.icon_daumay_km2_bottom);
				}	
				if(MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM){
					drawDevice = getResources().getDrawable(
							R.drawable.kb_oem_active);
				}	
				if(MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI){
					drawDevice = getResources().getDrawable(
							R.drawable.km1wifi_active);
				}
				if(MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI){
					drawDevice = getResources().getDrawable(
							R.drawable.kb39c_daluu);
				}
				if(MyApplication.intSvrModel == MyApplication.SONCA_KBX9){
					drawDevice = getResources().getDrawable(
							R.drawable.daumay_kb);
				}	
				if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
					drawDevice = getResources().getDrawable(R.drawable.daumay_9108);
					if(MyApplication.flagSmartK_CB){
						drawDevice = getResources().getDrawable(R.drawable.cloudbox_active);
					}
					if(MyApplication.flagSmartK_801){
						drawDevice = getResources().getDrawable(R.drawable.sb801_active);
					}
					if(MyApplication.flagSmartK_KM4){
						drawDevice = getResources().getDrawable(R.drawable.km4_active);
					}
				}
				if(MyApplication.intSvrModel == MyApplication.SONCA_TBT){
					drawDevice = getResources().getDrawable(R.drawable.daumay_tbt);
				}
				
				drawDevice.setBounds(rectDrawable);
				drawDevice.draw(canvas);
				
				drawWifi = drawWifi0;
				
				if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
						 || MyApplication.intSvrModel == MyApplication.SONCA_KBX9
						 || MyApplication.intSvrModel == MyApplication.SONCA_TBT){
					switch (MyApplication.levelWifi) {
					case 0:
						drawWifi = drawWifi0_hiw;
						break;
					case 1:
						drawWifi = drawWifi1_hiw;
						break;
					case 2:
						drawWifi = drawWifi2_hiw;
						break;
					case 3:
						drawWifi = drawWifi3_hiw;
						break;
					case 4:
						drawWifi = drawWifi4_hiw;
						break;
					case 5:
						drawWifi = drawWifi4_hiw;
						break;
					default:
						break;
					}
					
				} else if (MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
					switch (MyApplication.levelWifi) {
					case 0:
						drawWifi = drawWifi0_smartk;
						break;
					case 1:
						drawWifi = drawWifi1_smartk;
						break;
					case 2:
						drawWifi = drawWifi2_smartk;
						break;
					case 3:
						drawWifi = drawWifi3_smartk;
						break;
					case 4:
						drawWifi = drawWifi4_smartk;
						break;
					case 5:
						drawWifi = drawWifi4_smartk;
						break;
					default:
						break;
					}
					
				} else {
					switch (MyApplication.levelWifi) {
					case 0:
						drawWifi = drawWifi0;
						break;
					case 1:
						drawWifi = drawWifi1;
						break;
					case 2:
						drawWifi = drawWifi2;
						break;
					case 3:
						drawWifi = drawWifi3;
						break;
					case 4:
						drawWifi = drawWifi4;
						break;
					case 5:
						drawWifi = drawWifi4;
						break;
					default:
						break;
					}
					
				}				
				
				drawWifi.setBounds(rectDrawable);
				drawWifi.draw(canvas);				
				
				mainPaint.setStyle(Style.STROKE);
				mainPaint.setColor(Color.GREEN);
				mainPaint.setTextSize(nameServerS);
				str=cutText(nameServerS, (float)rectDrawable.width() *6/3,serverName);
				float mWidth = mainPaint.measureText(str);
				canvas.drawText(str, rectDrawable.centerX() - mWidth/2, nameServerY, mainPaint);
				
			} else {
				drawDevice = getResources().getDrawable(
						R.drawable.image_daumay_inactive_71x65_x);
				
				drawDevice.setBounds(rectDrawable);
				drawDevice.draw(canvas);
				
				switch (countAutoConnect) {
				case 0:
					drawWifi = drawWifi0;
					break;
				case 1:
					drawWifi = drawWifi1;
					break;
				case 2:
					drawWifi = drawWifi2;
					break;
				case 3:
					drawWifi = drawWifi3;
					break;
				case 4:
					drawWifi = drawWifi4;
					break;
				default:
					drawWifi = drawWifi0;
					break;
				}
				
				drawWifi.setBounds(rectDrawable);
				drawWifi.draw(canvas);
				
				serverName = getResources().getString(R.string.connect_auto);
				
				mainPaint.setStyle(Style.STROKE);
				mainPaint.setColor(Color.GRAY);
				mainPaint.setTextSize(nameServerS);
				float mWidth = mainPaint.measureText(serverName);
				canvas.drawText(serverName, rectDrawable.centerX() - mWidth/2, nameServerY, mainPaint);
				
			}
					
			
			if (isPlay) {
				strStatus = getResources().getString(
						R.string.karaoke_left_3a);
				
				if (flagStateNextSong) {
					drawStatusNext.setBounds(rectDrawableStatusPlay);
					drawStatusNext.draw(canvas);
				} else {
					drawableStatusPlay.setBounds(rectDrawableStatusPlay);
					drawableStatusPlay.draw(canvas);	
				}				
			} else {
				strStatus = getResources().getString(
						R.string.karaoke_left_3b);
				drawableStatusPause.setBounds(rectDrawableStatusPlay);
				drawableStatusPause.draw(canvas);
			}
			
			mainText.setStyle(Style.FILL);
			mainText.setARGB(255, 255, 224, 2);
			mainText.setTypeface(Typeface.DEFAULT);
			
			if (flagStateNextSong) {
				strStatus = getResources().getString(
						R.string.karaoke_left_3c);
				
				canvas.drawText(strStatus, nameX, nameY, mainText);				
				
			} else {
//				canvas.drawText(strStatus, nameX, nameY,
//						mainText);				
			}
		
			mainText.setStyle(Style.FILL);
			mainText.setARGB(255, 0, 253, 253);									
			
			if(flagStateNextSong){
				maxLenghtSongName = rectDrawablePlaylist.left - (nameX+mainText.measureText(strStatus)) - 40 * widthLayout / 1980;
				str=cutText(nameS, maxLenghtSongName ,currectSong);
				
				canvas.drawText(str, (nameX+mainText.measureText(strStatus)) + 20 * widthLayout / 1980, nameY, mainText);
				
			} else {
				if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && MyApplication.flagPlayingYouTube){
					maxLenghtSongName = rectDrawablePlaylist.left - rectDrawableStatusPlay.right - 40 * widthLayout / 1980;
					
					str=cutText(nameS, maxLenghtSongName ,currectSong);							
					canvas.drawText(str, rectDrawableStatusPlay.right + 10 * widthLayout / 1980, nameY, mainText);
				} else {						
				
				if(saveSong != null){					
					boolean flagMIDI = saveSong.isMediaMidi();
					if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
						MEDIA_TYPE ismedia = saveSong.getMediaType();
						flagMIDI = ismedia == MEDIA_TYPE.MIDI || ismedia == MEDIA_TYPE.MP3;
					}
					if(flagMIDI){
						String musicianName = saveSong.getMusician().getName();
								
						if(musicianName.equals("-")){
							maxLenghtSongName = rectDrawablePlaylist.left - rectDrawableStatusPlay.right - 40 * widthLayout / 1980;
							
							str=cutText(nameS, maxLenghtSongName ,currectSong);							
							canvas.drawText(str, rectDrawableStatusPlay.right + 10 * widthLayout / 1980, nameY, mainText);	
							
							float nextX = rectDrawableStatusPlay.right + 10 * widthLayout / 1980 + mainText.measureText(str);
							
							int tamX = (int) (nextX + 80 * widthLayout / 1980);
							int tamY = (int) (0.5*heightLayout);
							int height = (int) (0.3*heightLayout);
							int width = 50*height/35;							
							rectMIDI_KTV.set(tamX - width, tamY - height, tamX + width, tamY + height);		
							
							drawMidi.setBounds(rectMIDI_KTV);
							drawMidi.draw(canvas);
						} else {
							float maxTempLength = rectDrawablePlaylist.left - rectDrawableStatusPlay.right - 40 * widthLayout / 1980;
							maxLenghtSongName = maxTempLength * 0.7f;
							
							str=cutText(nameS, maxLenghtSongName ,currectSong);							
							canvas.drawText(str, rectDrawableStatusPlay.right + 10 * widthLayout / 1980, nameY, mainText);
							
							float nextX = rectDrawableStatusPlay.right + 10 * widthLayout / 1980 + mainText.measureText(str);
							
							int tamX = (int) (nextX + 80 * widthLayout / 1980);
							int tamY = (int) (0.5*heightLayout);
							int height = (int) (0.3*heightLayout);
							int width = 50*height/35;							
							rectMIDI_KTV.set(tamX - width, tamY - height, tamX + width, tamY + height);		
							
							drawMidi.setBounds(rectMIDI_KTV);
							drawMidi.draw(canvas);
							
							float availableLength = rectDrawablePlaylist.left - rectMIDI_KTV.right - 40 * widthLayout / 1980;
							mainText.setARGB(255, 1, 165, 254);							
							musicianName=cutText(nameS, availableLength ,musicianName);						
							canvas.drawText(musicianName, rectMIDI_KTV.right + 20 * widthLayout / 1980, nameY, mainText);
						}
					} else {
						String singerName = saveSong.getSinger().getName();
						
						if(singerName.equals("-")){
							maxLenghtSongName = rectDrawablePlaylist.left - rectDrawableStatusPlay.right - 40 * widthLayout / 1980;
							
							str=cutText(nameS, maxLenghtSongName ,currectSong);							
							canvas.drawText(str, rectDrawableStatusPlay.right + 10 * widthLayout / 1980, nameY, mainText);	
							
							float nextX = rectDrawableStatusPlay.right + 10 * widthLayout / 1980 + mainText.measureText(str);
							
							int tamX = (int) (nextX + 80 * widthLayout / 1980);
							int tamY = (int) (0.5*heightLayout);
							int height = (int) (0.3*heightLayout);
							int width = 50*height/35;							
							rectMIDI_KTV.set(tamX - width, tamY - height, tamX + width, tamY + height);		
							
							if(saveSong.getMediaType() == MEDIA_TYPE.VIDEO){
								drawKTVVid.setBounds(rectMIDI_KTV);
								drawKTVVid.draw(canvas);	
							} else {
								drawKTVMid.setBounds(rectMIDI_KTV);
								drawKTVMid.draw(canvas);
							}
						} else {
							float maxTempLength = rectDrawablePlaylist.left - rectDrawableStatusPlay.right - 40 * widthLayout / 1980;
							maxLenghtSongName = maxTempLength * 0.7f;
							
							str=cutText(nameS, maxLenghtSongName ,currectSong);							
							canvas.drawText(str, rectDrawableStatusPlay.right + 10 * widthLayout / 1980, nameY, mainText);
							
							float nextX = rectDrawableStatusPlay.right + 10 * widthLayout / 1980 + mainText.measureText(str);
							
							int tamX = (int) (nextX + 80 * widthLayout / 1980);
							int tamY = (int) (0.5*heightLayout);
							int height = (int) (0.3*heightLayout);
							int width = 50*height/35;							
							rectMIDI_KTV.set(tamX - width, tamY - height, tamX + width, tamY + height);		
							
							if(saveSong.getMediaType() == MEDIA_TYPE.VIDEO){
								drawKTVVid.setBounds(rectMIDI_KTV);
								drawKTVVid.draw(canvas);	
							} else {
								drawKTVMid.setBounds(rectMIDI_KTV);
								drawKTVMid.draw(canvas);
							}
							
							float availableLength = rectDrawablePlaylist.left - rectMIDI_KTV.right - 40 * widthLayout / 1980;
							mainText.setARGB(255, 1, 165, 254);							
							singerName=cutText(nameS, availableLength ,singerName);							
							canvas.drawText(singerName, rectMIDI_KTV.right + 20 * widthLayout / 1980, nameY, mainText);
						}
					}
				}
				}
			}
			
			if(isConnected){
				drawablePlaylist.setBounds(rectDrawablePlaylist);
				drawablePlaylist.draw(canvas);
								
				mainText.setStyle(Style.FILL);
				mainText.setColor(Color.GREEN);
				canvas.drawText(String.valueOf(sumSong), cntX, nameY, mainText);				
				
			} else {
				drawablePlaylist_Off.setBounds(rectDrawablePlaylist);
				drawablePlaylist_Off.draw(canvas);
				
				mainText.setStyle(Style.FILL);
				mainText.setColor(Color.GRAY);
				canvas.drawText(String.valueOf(sumSong), cntX, nameY, mainText);
				
			}
			
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {

		}
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
//		if(event.getAction() == MotionEvent.ACTION_UP){
//			float x = event.getX();
//			if(x >= 0 && x <= 0.1*widthLayout){
//				if(listener != null){
//					listener.OnBack();
//				}
//			}
//		}
		return true;
	}

	

	private int saveSumSong = 0;
	private int countAnimation = 0;
	private int sumSong = 0;
	
	public void setSumSong(int sumSong) {
		this.sumSong = sumSong;
		if (sumSong != saveSumSong) {
			countAnimation = 0;
			CreateTimerAnimation();
		}
		saveSumSong = sumSong;
		if(saveSumSong==0){
			flagStateNextSong = false;
			clearTimer();
		}
			
		invalidate();
	}
	
	public int getSumSong(){
		return this.saveSumSong;
	}
	private boolean flagStateNextSong = false;
	private String nextSongName = "";
	private String nowSongName = "";
	private int currentNextIdx5 = -1;

	public String getNextSongName(){
		return this.nextSongName;
	}

	public int getCurrentNextIdx5(){
		return this.currentNextIdx5;
	}

//	public void setCntPlaylist(String strTitle){
//		this.cntPlaylist = strTitle;
//		invalidate();
//	}
	
	public void setCurrectSong(String currectSong) {
		this.nowSongName = currectSong;
		//this.flagStateNextSong = false;
		invalidate();
		CreateTimer();
	}
	
	public void setNextSongName(String newName, int newCurrentId) {
					
		if(newName != null){
			this.nextSongName  = newName;
			this.currentNextIdx5 = newCurrentId;
		} else {
			this.nextSongName = "";
		}
		invalidate();
//		 MyLog.e(TAG, "==setNextSongName=newName=" +newName+ " -Id- " + newCurrentId+"=nextSongName="+nextSongName);	
		//CreateTimer();		
	}

	public void setPlayPause(boolean isPlay) {
		//MyLog.d(TAG, "=setPlayPause=="+isPlay+"=="+this.isPlay);
		this.isPlay = isPlay;
		invalidate();
	}

	public boolean getPlayPause() {
		return isPlay;
	}
	
	private Song saveSong;
	public void setSaveSong(Song saveSong){
		this.saveSong = saveSong;
		invalidate();
	}
	
	private Timer timerSong = null;

	private void CreateTimer() {
		if (timerSong != null) {
			timerSong.cancel();
			timerSong = null;
		}
		timerSong = new Timer();
		timerSong.schedule(new TimerTask() {

			@Override
			public void run() {
				flagStateNextSong = !flagStateNextSong;

				if (!isPlay) {
					flagStateNextSong = false;
				}

				ArrayList<Song> list = ((MyApplication) context
						.getApplicationContext()).getListActive();
				if (list != null) {
					if (list.size() == 0) {
						flagStateNextSong = false;
					}
				}
				if (flagStateNextSong) {
					if (list != null) {
						if (list.size() > 0) {
							if (nextSongName.equals("")) {
								final Song song = list.get(0);
								/*
								MyLog.e("search DB for Next Song",
										"timer.........................");
								*/
								String name = DBInterface.DBGetNameSong(context, String
										.valueOf(song.getIndex5()));
								
								if(name.equals("")){
									if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
										name = DBInterface.DBGetNameSong_YouTube(context, String.valueOf(song.getIndex5()));
									}	
								}
								
								nextSongName = name;
								currentNextIdx5 = song.getIndex5();
							}
						}
					}
					currectSong = nextSongName;
					 //MyLog.e("timerSong", "currectSong = nextSongName = " +
					 //nextSongName);
				} else {
					currectSong = nowSongName;

					// MyLog.e("timerSong", "currectSong = nowSongName = " +
					 //nowSongName);
				}
				handlerSong.sendEmptyMessage(0);
			}
		}, 200, 4000);
	}

	private Handler handlerSong = new Handler() {
		public void handleMessage(Message msg) {
			
			invalidate();
		};
	};

	protected void finalize() throws Throwable {
		if (timerSong != null) {
			timerSong.cancel();
			timerSong = null;
		}
		if (timerAnimationSong != null) {
			timerAnimationSong.cancel();
			timerAnimationSong = null;
		}
	};

	private Timer timerAnimationSong = null;

	private void CreateTimerAnimation() {
		if (timerAnimationSong != null) {
			timerAnimationSong.cancel();
			timerAnimationSong = null;
		}
		timerAnimationSong = new Timer();
		timerAnimationSong.schedule(new TimerTask() {

			@Override
			public void run() {
				if (countAnimation == -1) {
					return;
				}

				if (countAnimation > 4) {
					timerAnimationSong.cancel();
					timerAnimationSong = null;
					return;
				}

				countAnimation++;

				handlerSong.sendEmptyMessage(0);
			}
		}, 50, 700);
	}
	
	private void clearTimer(){
		if (timerAnimationSong != null) {
			timerAnimationSong.cancel();
			timerAnimationSong = null;
		}
		if (timerSong != null) {
			timerSong.cancel();
			timerSong = null;
		}
	}
	

	private String cutText(float textSize, float maxLength, String content) {
		if (content == null || content.equals("")) {
			return "";
		}
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setTextSize(textSize);
		float length = paint.measureText(content);
		if (length > maxLength) {
			String[] strings = content.split(" ");
			if (strings.length > 1) {
				// return cutTextWord(textSize, maxLength, strings, paint);
				return cutTextChar(textSize, maxLength, content, paint);
			} else {
				return cutTextChar(textSize, maxLength, content, paint);
			}
		} else {
			return content;
		}
	}

	private String cutTextChar(float textSize, float maxLength, String content, Paint paint) {
		float length = 0;
		StringBuffer buffer = new StringBuffer("");
		for (int i = 0; i < content.length(); i++) {
			length = paint.measureText(buffer.toString() + content.charAt(i)
					+ "...");
			if (length < maxLength) {
				buffer.append(content.charAt(i));
			} else {
				break;
			}
		}
		buffer.append("...");
		return buffer.toString();
	}
	
	// --------------------------
	private int countAutoConnect = -1;
	private Timer timerAutoConnect = null;

	public void StartTimerAutoConnect() {
		if (countAutoConnect != -1) {
			return;
		}

		MyLog.e("TEST TEST KARAOKE", "StartTimerAutoConnect FIRST TIME");

		if (timerAutoConnect != null) {
			timerAutoConnect.cancel();
			timerAutoConnect = null;
		}
		timerAutoConnect = new Timer();
		timerAutoConnect.schedule(new TimerTask() {

			@Override
			public void run() {

				countAutoConnect++;

				if (countAutoConnect > 4) {
					countAutoConnect = 0;
				}

				handlerSong.sendEmptyMessage(0);
			}
		}, 50, 500);
	}

	public void StopTimerAutoConnect() {
		if (timerAutoConnect != null) {
			timerAutoConnect.cancel();
			timerAutoConnect = null;
		}

		countAutoConnect = -1;
		invalidate();
	}
	
	private String serverName = "";
	
	public void setServerName(String serverName){
		this.serverName = serverName;
		invalidate();
	}
	
}
