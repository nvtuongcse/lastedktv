package vn.com.sonca.Lyric;

import java.util.Timer;
import java.util.TimerTask;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.params.Song;
import vn.com.sonca.utils.AppConfig.MEDIA_TYPE;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DialogLyricView extends View {
	
	private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint mainText = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public DialogLyricView(Context context) {
		super(context);
		initView(context);
	}

	public DialogLyricView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public DialogLyricView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private OnDialogLyricView listener;
	public interface OnDialogLyricView {
		public void OnFavourite(boolean fav, Song song);
		public void OnShowLyric(Song song);
		public void OnShowReviewKaraoke(Song song);
		public void OnPopupPlayYouTube(Song song);
		public void OnPopupDownYouTube(Song song);
	}
	
	public void setOnDialogLyricView(OnDialogLyricView listener){
		this.listener = listener;
	}
	
	private String yeuthich;
	private String nameSong = "";
	private String nameSinger = "";
	private String nameButton = "";
	private String reviewKaraokeButton = "";
	
	private Drawable drawable;
	private Drawable drawLyric;
	private Drawable drawReviewKaraoke;
	private Drawable drawBackgroud;
	private Drawable drawSinger, drawUser;
	private Drawable drawRemix, drawMidi, drawMV, drawMVVid;
	private Drawable drawFavourite, drawFavouriteNO;
	
	private Drawable zlightdrawLyric;
	private Drawable zlightdrawReviewKaraoke;
	private Drawable zlightdrawBackgroud;
	private Drawable zlightdrawSinger, zlightdrawUser;
	private Drawable zlightdrawRemix, zlightdrawMidi, zlightdrawMV;
	private Drawable zlightdrawFavourite, zlightdrawFavouriteNO;
	
	private void initView(Context context) {
		nameButton = getResources().getString(R.string.lyric_0);
		reviewKaraokeButton = getResources().getString(R.string.lyric_7);
		yeuthich = getResources().getString(R.string.type_favourite);
		
		drawBackgroud = getResources().getDrawable(R.drawable.icon_boder_popup);
		drawFavourite = getResources().getDrawable(R.drawable.touch_image_favourite_active_45x45);
		drawFavouriteNO = getResources().getDrawable(R.drawable.touch_image_favourite_45x45);
		drawSinger = getResources().getDrawable(R.drawable.touch_song_vocal_48x48);
		drawUser = getResources().getDrawable(R.drawable.touch_icon_user);
		drawRemix = getResources().getDrawable(R.drawable.touch_image_remix_62x35);
		drawMidi = getResources().getDrawable(R.drawable.touch_image_midi_50x35);
		drawMV = getResources().getDrawable(R.drawable.icon_ktv_1note);
		drawMVVid = getResources().getDrawable(R.drawable.icon_ktv_video);
		drawLyric = getResources().getDrawable(R.drawable.lyric_92x92);
		drawReviewKaraoke = getResources().getDrawable(R.drawable.review_92x92);
		
		zlightdrawBackgroud = getResources().getDrawable(R.drawable.zlight_boder_popup);
		zlightdrawFavourite = getResources().getDrawable(R.drawable.zlight_image_favourite_active_45x45);
		zlightdrawFavouriteNO = getResources().getDrawable(R.drawable.zlight_image_favourite_inactive_45x45);
		zlightdrawSinger = getResources().getDrawable(R.drawable.zlight_song_vocal_48x48);
		zlightdrawUser = getResources().getDrawable(R.drawable.zlight_icon_user);
		zlightdrawRemix = getResources().getDrawable(R.drawable.zlight_image_remix_62x35);
		zlightdrawMidi = getResources().getDrawable(R.drawable.zlight_image_midi_50x35);
		zlightdrawMV = getResources().getDrawable(R.drawable.zlight_ktv_50x30);
		zlightdrawLyric = getResources().getDrawable(R.drawable.zlight_lyric_92x92);		
		zlightdrawReviewKaraoke = getResources().getDrawable(R.drawable.zlight_review_92x92);		
		
		nameSong = "";
		nameSinger = "";
		/*
		isFavourity = true;
		ismedia = MEDIA_TYPE.MIDI;
		isSinger = true;
		isRemix = true;
		*/
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int myHeight = (int) (0.34*getResources().getDisplayMetrics().heightPixels);
		int myWidth = (int) (3.5*myHeight);
		if((MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && song.isYoutubeSong()) || 
				(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && (MyApplication.flagSmartK_801 || MyApplication.flagSmartK_CB) && song.isOfflineSong() == false)){
			myHeight = (int) (0.45*getResources().getDisplayMetrics().heightPixels);
			myWidth = (int) (2*myHeight);
		}	
	    setMeasuredDimension(myWidth, myHeight);
	}
	
	private int width;
	private int height;
	private int widthView;
	private int heightView;
	private float KDP, K30;
	private float ytX , ytY , ytS;
	private Rect rectMV;
	private Rect rectMIDI;
	private Rect rectSinger;
	private Rect rectRemix;
	private Rect rectFavourite;
	private Rect rectUser;
	private Rect rectLyric;
	private Rect rectReviewKaraoke;
	private Rect rectBackgroud = new Rect();
	private int singerX, singerY, singerS;
	private int nameX, nameY, nameS;
	private int buttonX, buttonY, buttonS,buttonX1;
	private float lineY = 0;
	private float checkArea = 0;
	
	private Rect rectOnline, rectYouTube, rectXemTruoc, rectDownload, rectDownload2, rectDownload3;
	private float KT1S, KT2S, KT1Y, KT2Y, KT3Y, KT4Y;	
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		widthView = w;
		heightView = h;
		rectBackgroud.set(0, 0, w, h);
		
		KDP = (float) (0.02*heightView);
		K30 = (float) (0.01*widthView);
		lineY = (float) (0.55*h);
		
		nameX = (int) (0.05*w);
		nameY = (int) (0.25*h);
		nameS = (int) (0.16*h);
		
		singerX = (int) (0.05*w);
		singerY = (int) (0.45*h);
		singerS = (int) (0.14*h);
		
		float offsetX = (float) (0.9*w);
		float offsetY = (float) (0.36*h);
		float vuong = (float) (0.08*h);
		ytS = (float) (0.04*h);
		mainText.setTextSize(ytS);
		ytX = offsetX - mainText.measureText(yeuthich)/2;
		ytY = (float) (0.5*h);
		rectFavourite = new Rect(
				(int)(offsetX - vuong), 
				(int)(offsetY - vuong), 
				(int)(offsetX + vuong), 
				(int)(offsetY + vuong));
		offsetX = (float) (0.82*w);
		offsetY = (float) (0.42*h);
		float vuongH = (float) (0.07*h);
		float vuongW = vuongH*50/35;
		rectMIDI = new Rect(
				(int)(offsetX - vuongW), 
				(int)(offsetY - vuongH), 
				(int)(offsetX + vuongW), 
				(int)(offsetY + vuongH));
		offsetX = (float) (0.83*w);
		vuongH = (float) (0.06*h);
		vuongW = vuongH*84/38;
		rectMV = new Rect(
				(int)(offsetX - vuongW), 
				(int)(offsetY - vuongH), 
				(int)(offsetX + vuongW), 
				(int)(offsetY + vuongH));
		offsetX = (float) (0.75*w);
		vuongH = (float) (0.09*h);
		vuongW = vuongH*45/40;
		rectSinger = new Rect(
				(int)(offsetX - vuongW), 
				(int)(offsetY - vuongH), 
				(int)(offsetX + vuongW), 
				(int)(offsetY + vuongH));
		offsetX = (float) (0.665*w);
		offsetY = (float) (0.42*h);
		vuongH = (float) (0.07*h);
		vuongW = vuongH*96/48;
		rectRemix = new Rect(
				(int)(offsetX - vuongW), 
				(int)(offsetY - vuongH), 
				(int)(offsetX + vuongW), 
				(int)(offsetY + vuongH));
		
		offsetX = (float) (0.57*w);
		offsetY = (float) (0.42*h);
		vuongH = (float) (0.055*h);
		vuongW = vuongH*50/28;
		rectUser = new Rect(
				(int)(offsetX - vuongW), 
				(int)(offsetY - vuongH), 
				(int)(offsetX + vuongW), 
				(int)(offsetY + vuongH));
		
		
		offsetX = (float) (0.35*w);
		if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
			offsetX = (float) (0.1*w);	
		}
		offsetY = (float) (0.75*h);
		vuongH = (float) (0.15*h);
		vuongW = vuongH;
		rectLyric = new Rect(
				(int)(offsetX - vuongW), 
				(int)(offsetY - vuongH), 
				(int)(offsetX + vuongW), 
				(int)(offsetY + vuongH));
		
		buttonX = (int) (offsetX + vuongW + 10);
		buttonY = (int) (0.8*h);
		buttonS = (int) (0.14*h);
		
		offsetX = (float) (0.55*w);
		rectReviewKaraoke=new Rect(
				(int)(offsetX - vuongW), 
				(int)(offsetY - vuongH), 
				(int)(offsetX + vuongW), 
				(int)(offsetY + vuongH));
		if(MyApplication.intSvrModel != MyApplication.SONCA_SMARTK){
			rectReviewKaraoke = new Rect(0,0,0,0);
		}
		buttonX1 = (int) (offsetX + vuongW + 10);
		
		// special
		if((MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && song.isYoutubeSong()) || 
				(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && (MyApplication.flagSmartK_801 || MyApplication.flagSmartK_CB) && song.isOfflineSong() == false)){
			KDP = (float) (0.01*heightView);
			K30 = (float) (0.01*widthView);
			lineY = (float) (0.4*h);
			
			offsetX = (float) (0.09*w);
			offsetY = (float) (0.16*h);
			vuongH = (float) (0.04*h);
			vuongW = vuongH*44/26;
			rectOnline = new Rect(
					(int)(offsetX - vuongW), 
					(int)(offsetY - vuongH), 
					(int)(offsetX + vuongW), 
					(int)(offsetY + vuongH));
			
			nameX = rectOnline.right + (int) (0.02*w);
			nameY = (int) (0.2*h);
			nameS = (int) (0.1*h);
			
			singerX = (int) (0.06*w);
			singerY = (int) (0.33*h);
			singerS = (int) (0.08*h);
			
			offsetX = (float) (0.92*w);
			offsetY = (float) (0.3*h);
			vuongH = (float) (0.04*h);
			vuongW = vuongH*36/26;
			rectYouTube = new Rect(
					(int)(offsetX - vuongW), 
					(int)(offsetY - vuongH), 
					(int)(offsetX + vuongW), 
					(int)(offsetY + vuongH));
						
			offsetX = (float) (0.84*w);
			offsetY = (float) (0.3*h);
			vuongH = (float) (0.04*h);
			vuongW = vuongH*96/48;
			rectRemix = new Rect(
					(int)(offsetX - vuongW), 
					(int)(offsetY - vuongH), 
					(int)(offsetX + vuongW), 
					(int)(offsetY + vuongH));
			
			offsetX = (float) (0.775*w);
			offsetY = (float) (0.3*h);
			vuongH = (float) (0.03*h);
			vuongW = vuongH*45/40;
			rectSinger = new Rect(
					(int)(offsetX - vuongW), 
					(int)(offsetY - vuongH), 
					(int)(offsetX + vuongW), 
					(int)(offsetY + vuongH));
		
			offsetX = (float) (0.7*w);
			offsetY = (float) (0.3*h);
			vuongH = (float) (0.04*h);
			vuongW = vuongH*84/38;
			rectMIDI = new Rect(
					(int)(offsetX - vuongW), 
					(int)(offsetY - vuongH), 
					(int)(offsetX + vuongW), 
					(int)(offsetY + vuongH));
			
			offsetX = (float) (0.7*w);
			offsetY = (float) (0.3*h);
			vuongH = (float) (0.04*h);
			vuongW = vuongH*84/38;
			rectMV = new Rect(
					(int)(offsetX - vuongW), 
					(int)(offsetY - vuongH), 
					(int)(offsetX + vuongW), 
					(int)(offsetY + vuongH));
						
			offsetX = (float) (0.25*w);
			offsetY = (float) (0.55*h);
			vuongH = (float) (0.08*h);
			vuongW = vuongH*50/43;
			rectXemTruoc = new Rect(
					(int)(offsetX - vuongW), 
					(int)(offsetY - vuongH), 
					(int)(offsetX + vuongW), 
					(int)(offsetY + vuongH));
			
			offsetX = (float) (0.25*w);
			offsetY = (float) (0.78*h);
			vuongH = (float) (0.07*h);
			vuongW = vuongH*32/39;
			rectDownload = new Rect(
					(int)(offsetX - vuongW), 
					(int)(offsetY - vuongH), 
					(int)(offsetX + vuongW), 
					(int)(offsetY + vuongH));
			
			offsetX = (float) (0.25*w);
			offsetY = (float) (0.78*h);
			vuongH = (float) (0.07*h);
			vuongW = vuongH*14/17;
			rectDownload2 = new Rect(
					(int)(offsetX - vuongW), 
					(int)(offsetY - vuongH), 
					(int)(offsetX + vuongW), 
					(int)(offsetY + vuongH));	
			
			offsetX = (float) (0.8*w);
			offsetY = (float) (0.78*h);
			vuongH = (float) (0.05*h);
			vuongW = vuongH*39/39;
			rectDownload3 = new Rect(
					(int)(offsetX - vuongW), 
					(int)(offsetY - vuongH), 
					(int)(offsetX + vuongW), 
					(int)(offsetY + vuongH));
			
			KT1S = 0.09f*h;
			KT2S = 0.06f*h;
			
			KT1Y = 0.57f*h;
			KT2Y = 0.8f*h;
			KT3Y = 0.82f*h;
			KT4Y = 0.78f*h;
		}
	}
	
	private int color_01;
	private int color_02;
	private int color_03;	
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		// MyApplication.intColorScreen = MyApplication.SCREEN_LIGHT;
		
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE){
			color_01 = Color.GREEN;
			color_02 = Color.argb(255, 182, 253, 255);
			color_03 = Color.YELLOW;
			if((MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && song.isYoutubeSong()) || 
					(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && (MyApplication.flagSmartK_801 || MyApplication.flagSmartK_CB) && song.isOfflineSong() == false)){
				color_02 = Color.argb(255, 255, 238, 0);
			}
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			color_01 = Color.GREEN;
			color_02 = Color.parseColor("#005249");
			color_03 = Color.parseColor("#005249");
		}
		
		if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && song.isYoutubeSong()){
			drawBackgroud.setBounds(rectBackgroud);
			drawBackgroud.draw(canvas);
			
			mainPaint.setStrokeWidth(KDP);
			mainPaint.setColor(color_01);
			float leftLayout = (float) (0.05 * widthView);
			float righttLayout = widthView - leftLayout;
			LinearGradient gradient = new LinearGradient(leftLayout - K30, KDP / 2, widthView / 2, KDP / 2,
					Color.TRANSPARENT, Color.CYAN, Shader.TileMode.MIRROR);
			mainPaint.setShader(gradient);
			canvas.drawLine(leftLayout - K30, lineY, righttLayout + K30, lineY, mainPaint);
			mainPaint.setShader(null);
			
			drawable = getResources().getDrawable(R.drawable.icon_icloud);
			drawable.setBounds(rectOnline);
			drawable.draw(canvas);
			
			mainText.setStyle(Style.FILL);
			mainText.setTextSize(nameS);
			mainText.setColor(color_02);
			int widthSong = (int) (0.8 * widthView);
			int sizeSong = (int) mainText.measureText(nameSong + textABC);
			if (sizeSong > widthSong) {
				float f = mainText.measureText("..." + textABC);
				int i = mainText.breakText(nameSong, true, widthSong - f, null);
				canvas.drawText(nameSong.subSequence(0, i).toString() + "..." + textABC, nameX, nameY, mainText);
			} else {
				canvas.drawText(nameSong + textABC, nameX, nameY, mainText);
			}
			
			mainText.setTextSize(singerS);
			mainText.setARGB(255, 1, 165, 254);
			widthSong = (int) (0.55 * widthView);
			sizeSong = (int) mainText.measureText(nameSinger);
			if (sizeSong > widthSong) {
				int i = mainText.breakText(nameSinger, true, widthSong - 15, null);
				canvas.drawText(nameSinger.subSequence(0, i).toString() + "...", singerX, singerY, mainText);
			} else {
				canvas.drawText(nameSinger, singerX, singerY, mainText);
			}
			
			if(MyApplication.youtube_Download_ID == song.getId()){
				float RT3X = rectXemTruoc.right + 5 * widthView / 480;
				
				if(countDownload == -1){
					startTimerDownload();
				} else {
					switch (countDownload) {
					case 0:
						drawable = getResources().getDrawable(R.drawable.youtube_down);
						break;
					case 1:
						drawable = getResources().getDrawable(R.drawable.youtube_down_1);
						break;
					case 2:
						drawable = getResources().getDrawable(R.drawable.youtube_down_2);
						break;
					case 3:
						drawable = getResources().getDrawable(R.drawable.youtube_down_3);
						break;
					default:
						drawable = getResources().getDrawable(R.drawable.youtube_down);
						break;
					}
					drawable.setBounds(rectDownload2);
					drawable.draw(canvas);	
				}
				
				// PERCENT
				mainPaint.setStyle(Style.STROKE);
				mainPaint.setStrokeWidth((float) 4);
				mainPaint.setARGB(255, 129, 151, 172);
				
				float maxPercentWidth = rectDownload3.left - RT3X - 10 * widthView / 480;
				canvas.drawLine(RT3X, KT3Y, RT3X + maxPercentWidth, KT3Y, mainPaint);
				
				float progressStop = 0;
				if(MyApplication.youtube_Download_percent > 0){
					progressStop = maxPercentWidth * MyApplication.youtube_Download_percent / 100;
				}									
				mainPaint.setStyle(Style.STROKE);
				mainPaint.setStrokeWidth((float) 4);
				mainPaint.setARGB(255, 0, 253, 253);
				canvas.drawLine(RT3X, KT3Y, RT3X + progressStop, KT3Y, mainPaint);						
				
				mainPaint.setStyle(Style.FILL);
				mainPaint.setTextSize(KT2S);
				mainPaint.setARGB(255, 0, 253, 253);
				String str = MyApplication.youtube_Download_percent + "%";
				float maxStrWidth = mainPaint.measureText(str);
				canvas.drawText(str, RT3X + maxPercentWidth - maxStrWidth, KT4Y, mainPaint);
				
				if(MyApplication.youtube_Download_percent != 100){
					drawable = getResources().getDrawable(R.drawable.popup_huydown);
					drawable.setBounds(rectDownload3);
					drawable.draw(canvas);	
				}				
				
			} else {
				stopTimerDownload();
				
				drawable = getResources().getDrawable(R.drawable.popup_download);
				if(MyApplication.flagOnAdminOnline == false){						
					drawable = getResources().getDrawable(R.drawable.popup_download_xam);
				}
				drawable.setBounds(rectDownload);
				drawable.draw(canvas);
			}	
			
			mainText.setStyle(Style.FILL);
			mainText.setTextSize(KT1S);
			mainText.setARGB(255, 182, 253, 255);
			if(MyApplication.flagOnAdminOnline == false){	
				mainText.setColor(Color.GRAY);
			}
			String str = getResources().getString(R.string.popup_yt_1);
			canvas.drawText(str, rectXemTruoc.right + 5 * widthView / 480, KT2Y, mainText);
						
			drawable = getResources().getDrawable(R.drawable.youtube_icon);
			drawable.setBounds(rectYouTube);
			drawable.draw(canvas);
			// --------------xem truoc--------------------
			if(!song.isSambaSong()){
				drawable = getResources().getDrawable(R.drawable.youtube_xemtruoc);
				drawable.setBounds(rectXemTruoc);
				drawable.draw(canvas);
				
				mainText.setStyle(Style.FILL);
				mainText.setTextSize(KT1S);
				mainText.setARGB(255, 182, 253, 255);
				str = getResources().getString(R.string.popup_yt_3);
				canvas.drawText(str, rectXemTruoc.right + 5 * widthView / 480, KT1Y, mainText);	
			}			
						
			// --------------MIDI vs MV--------------------
			if (drawMVVid != null) {
				drawMVVid.setBounds(rectMV);
				drawMVVid.draw(canvas);
			}

			// ---------------isSinger-------------------
			if (isSinger && drawSinger != null) {
				drawSinger.setBounds(rectSinger);
				drawSinger.draw(canvas);
			}

			// ---------------isRemix-------------------
			if (isRemix && drawRemix != null) {
				drawRemix.setBounds(rectRemix);
				drawRemix.draw(canvas);
			}
			
		} else if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && (MyApplication.flagSmartK_801 || MyApplication.flagSmartK_CB) 
				&& song.isOfflineSong() == false){

			drawBackgroud.setBounds(rectBackgroud);
			drawBackgroud.draw(canvas);
			
			mainPaint.setStrokeWidth(KDP);
			mainPaint.setColor(color_01);
			float leftLayout = (float) (0.05 * widthView);
			float righttLayout = widthView - leftLayout;
			LinearGradient gradient = new LinearGradient(leftLayout - K30, KDP / 2, widthView / 2, KDP / 2,
					Color.TRANSPARENT, Color.CYAN, Shader.TileMode.MIRROR);
			mainPaint.setShader(gradient);
			canvas.drawLine(leftLayout - K30, lineY, righttLayout + K30, lineY, mainPaint);
			mainPaint.setShader(null);
			
			drawable = getResources().getDrawable(R.drawable.icon_icloud);
			drawable.setBounds(rectOnline);
			drawable.draw(canvas);
			
			mainText.setStyle(Style.FILL);
			mainText.setTextSize(nameS);
			mainText.setColor(color_02);
			int widthSong = (int) (0.8 * widthView);
			int sizeSong = (int) mainText.measureText(nameSong + textABC);
			if (sizeSong > widthSong) {
				float f = mainText.measureText("..." + textABC);
				int i = mainText.breakText(nameSong, true, widthSong - f, null);
				canvas.drawText(nameSong.subSequence(0, i).toString() + "..." + textABC, nameX, nameY, mainText);
			} else {
				canvas.drawText(nameSong + textABC, nameX, nameY, mainText);
			}
			
			mainText.setTextSize(singerS);
			mainText.setARGB(255, 1, 165, 254);
			widthSong = (int) (0.55 * widthView);
			sizeSong = (int) mainText.measureText(nameSinger);
			if (sizeSong > widthSong) {
				int i = mainText.breakText(nameSinger, true, widthSong - 15, null);
				canvas.drawText(nameSinger.subSequence(0, i).toString() + "...", singerX, singerY, mainText);
			} else {
				canvas.drawText(nameSinger, singerX, singerY, mainText);
			}
			
			if(MyApplication.youtube_Download_ID == song.getId()){
				float RT3X = rectXemTruoc.right + 5 * widthView / 480;
				
				if(countDownload == -1){
					startTimerDownload();
				} else {
					switch (countDownload) {
					case 0:
						drawable = getResources().getDrawable(R.drawable.youtube_down);
						break;
					case 1:
						drawable = getResources().getDrawable(R.drawable.youtube_down_1);
						break;
					case 2:
						drawable = getResources().getDrawable(R.drawable.youtube_down_2);
						break;
					case 3:
						drawable = getResources().getDrawable(R.drawable.youtube_down_3);
						break;
					default:
						drawable = getResources().getDrawable(R.drawable.youtube_down);
						break;
					}
					drawable.setBounds(rectDownload2);
					drawable.draw(canvas);	
				}
				
				// PERCENT
				mainPaint.setStyle(Style.STROKE);
				mainPaint.setStrokeWidth((float) 4);
				mainPaint.setARGB(255, 129, 151, 172);
				
				float maxPercentWidth = rectDownload3.left - RT3X - 10 * widthView / 480;
				canvas.drawLine(RT3X, KT3Y, RT3X + maxPercentWidth, KT3Y, mainPaint);
				
				float progressStop = 0;
				if(MyApplication.youtube_Download_percent > 0){
					progressStop = maxPercentWidth * MyApplication.youtube_Download_percent / 100;
				}									
				mainPaint.setStyle(Style.STROKE);
				mainPaint.setStrokeWidth((float) 4);
				mainPaint.setARGB(255, 0, 253, 253);
				canvas.drawLine(RT3X, KT3Y, RT3X + progressStop, KT3Y, mainPaint);						
				
				mainPaint.setStyle(Style.FILL);
				mainPaint.setTextSize(KT2S);
				mainPaint.setARGB(255, 0, 253, 253);
				String str = MyApplication.youtube_Download_percent + "%";
				float maxStrWidth = mainPaint.measureText(str);
				canvas.drawText(str, RT3X + maxPercentWidth - maxStrWidth, KT4Y, mainPaint);
				
				if(MyApplication.youtube_Download_percent != 100){
					drawable = getResources().getDrawable(R.drawable.popup_huydown);
					drawable.setBounds(rectDownload3);
					drawable.draw(canvas);	
				}				
				
			} else {
				stopTimerDownload();
				
				drawable = getResources().getDrawable(R.drawable.popup_download);
				drawable.setBounds(rectDownload);
				drawable.draw(canvas);
			}	
			
			mainText.setStyle(Style.FILL);
			mainText.setTextSize(KT1S);
			mainText.setARGB(255, 182, 253, 255);
			String str = getResources().getString(R.string.popup_yt_1);
			canvas.drawText(str, rectXemTruoc.right + 5 * widthView / 480, KT2Y, mainText);
						
			drawable = getResources().getDrawable(R.drawable.youtube_icon);
			drawable.setBounds(rectYouTube);
			drawable.draw(canvas);
			// --------------xem truoc--------------------
			drawable = getResources().getDrawable(R.drawable.youtube_xemtruoc);
			drawable.setBounds(rectXemTruoc);
			drawable.draw(canvas);
			
			mainText.setStyle(Style.FILL);
			mainText.setTextSize(KT1S);
			mainText.setARGB(255, 182, 253, 255);
			str = getResources().getString(R.string.popup_yt_3);
			canvas.drawText(str, rectXemTruoc.right + 5 * widthView / 480, KT1Y, mainText);
			
			// --------------MIDI vs MV--------------------
			if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
				if (ismedia == MEDIA_TYPE.MIDI || ismedia == MEDIA_TYPE.MP3) {
					if (drawMidi != null) {
						drawMidi.setBounds(rectMIDI);
						drawMidi.draw(canvas);
					}
				} else {
					if (ismedia == MEDIA_TYPE.VIDEO) {
						if (drawMVVid != null) {
							drawMVVid.setBounds(rectMV);
							drawMVVid.draw(canvas);
						}
					} else {
						if (drawMV != null) {
							drawMV.setBounds(rectMV);
							drawMV.draw(canvas);
						}
					}	
				}
			} else {
				if (ismedia == MEDIA_TYPE.MIDI) {
					if (drawMidi != null) {
						drawMidi.setBounds(rectMIDI);
						drawMidi.draw(canvas);
					}
				} else {
					if (ismedia == MEDIA_TYPE.VIDEO) {
						if (drawMVVid != null) {
							drawMVVid.setBounds(rectMV);
							drawMVVid.draw(canvas);
						}
					} else {
						if (drawMV != null) {
							drawMV.setBounds(rectMV);
							drawMV.draw(canvas);
						}
					}	
				}
			}

			// ---------------isSinger-------------------
			if (isSinger && drawSinger != null) {
				drawSinger.setBounds(rectSinger);
				drawSinger.draw(canvas);
			}

			// ---------------isRemix-------------------
			if (isRemix && drawRemix != null) {
				drawRemix.setBounds(rectRemix);
				drawRemix.draw(canvas);
			}
			
		
		} else {
			drawBackgroud.setBounds(rectBackgroud);
			drawBackgroud.draw(canvas);

			mainPaint.setStrokeWidth(KDP);
			mainPaint.setColor(color_01);
			float leftLayout = (float) (0.1 * widthView);
			float righttLayout = widthView - leftLayout;
			LinearGradient gradient = new LinearGradient(leftLayout - K30, KDP / 2, widthView / 2, KDP / 2,
					Color.TRANSPARENT, Color.CYAN, Shader.TileMode.MIRROR);
			mainPaint.setShader(gradient);
			canvas.drawLine(leftLayout - K30, lineY, righttLayout + K30, lineY, mainPaint);
			mainPaint.setShader(null);

			mainText.setStyle(Style.FILL);
			mainText.setTextSize(nameS);
			mainText.setColor(color_02);
			int widthSong = (int) (0.9 * widthView);
			int sizeSong = (int) mainText.measureText(nameSong + textABC);
			if (sizeSong > widthSong) {
				float f = mainText.measureText("..." + textABC);
				int i = mainText.breakText(nameSong, true, widthSong - f, null);
				canvas.drawText(nameSong.subSequence(0, i).toString() + "..." + textABC, nameX, nameY, mainText);
			} else {
				canvas.drawText(nameSong + textABC, nameX, nameY, mainText);
			}

			mainText.setTextSize(singerS);
			mainText.setARGB(255, 1, 165, 254);
			widthSong = (int) (0.45 * widthView);
			sizeSong = (int) mainText.measureText(nameSinger);
			if (sizeSong > widthSong) {
				int i = mainText.breakText(nameSinger, true, widthSong - 15, null);
				canvas.drawText(nameSinger.subSequence(0, i).toString() + "...", singerX, singerY, mainText);
			} else {
				canvas.drawText(nameSinger, singerX, singerY, mainText);
			}

			mainText.setTextSize(buttonS);
			mainText.setColor(color_02);
			canvas.drawText(nameButton, buttonX, buttonY, mainText);
			if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
				canvas.drawText(reviewKaraokeButton, buttonX1, buttonY, mainText);	
			}
			if(checkArea==0)
				checkArea=buttonX+mainText.measureText(nameButton);
			// -------------isFavourity---------------------

			if (isFavourity) {
				if (drawFavourite != null) {
					drawFavourite.setBounds(rectFavourite);
					drawFavourite.draw(canvas);
				}
			} else {
				if (drawFavouriteNO != null) {
					drawFavouriteNO.setBounds(rectFavourite);
					drawFavouriteNO.draw(canvas);
				}
			}
			mainText.setStyle(Style.FILL);
			mainText.setTextSize(ytS);
			mainText.setColor(color_03);
			canvas.drawText(yeuthich, ytX, ytY, mainText);

			// --------------MIDI vs MV--------------------

			if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
				if (ismedia == MEDIA_TYPE.MIDI || ismedia == MEDIA_TYPE.MP3) {
					if (drawMidi != null) {
						drawMidi.setBounds(rectMIDI);
						drawMidi.draw(canvas);
					}
				} else {
					if (ismedia == MEDIA_TYPE.VIDEO) {
						if (drawMVVid != null) {
							drawMVVid.setBounds(rectMV);
							drawMVVid.draw(canvas);
						}
					} else {
						if (drawMV != null) {
							drawMV.setBounds(rectMV);
							drawMV.draw(canvas);
						}
					}	
				}
			} else {
				if (ismedia == MEDIA_TYPE.MIDI) {
					if (drawMidi != null) {
						drawMidi.setBounds(rectMIDI);
						drawMidi.draw(canvas);
					}
				} else {
					if (ismedia == MEDIA_TYPE.VIDEO) {
						if (drawMVVid != null) {
							drawMVVid.setBounds(rectMV);
							drawMVVid.draw(canvas);
						}
					} else {
						if (drawMV != null) {
							drawMV.setBounds(rectMV);
							drawMV.draw(canvas);
						}
					}	
				}
			}
			
			// ---------------isSinger-------------------

			if (isSinger && drawSinger != null) {
				drawSinger.setBounds(rectSinger);
				drawSinger.draw(canvas);
			}

			// ---------------isRemix-------------------

			if (isRemix && drawRemix != null) {
				drawRemix.setBounds(rectRemix);
				drawRemix.draw(canvas);
			}

			// ---------------isUser----------------------

			if (isUser && drawUser != null) {
				drawUser.setBounds(rectUser);
				drawUser.draw(canvas);
			}

			drawLyric.setBounds(rectLyric);
			drawLyric.draw(canvas);
			drawReviewKaraoke.setBounds(rectReviewKaraoke);
			drawReviewKaraoke.draw(canvas);		
		}
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_UP){
			float x = event.getX();
			float y = event.getY();
			
			if((MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && song.isYoutubeSong()) || 
					(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && (MyApplication.flagSmartK_801 || MyApplication.flagSmartK_CB) && song.isOfflineSong() == false)){
				
				if (y >= rectXemTruoc.top && y <= rectXemTruoc.bottom) {
					if (listener != null) {
						listener.OnPopupPlayYouTube(song);
						return true;
					}
				}
				
				if (y > rectXemTruoc.bottom) {
					if(MyApplication.youtube_Download_ID == song.getId()){
						if(MyApplication.youtube_Download_percent != 100){
							if(x>=(rectDownload3.left-10 * widthView / 480))
								listener.OnPopupDownYouTube(song);	
						}
					} else {
						if (listener != null) {
							if(MyApplication.flagOnAdminOnline == false){
								return true;
							}
							listener.OnPopupDownYouTube(song);
							return true;
						}
					}
					
				}
			} else {
				if(rectFavourite != null){
					if(x >= rectFavourite.left && x <= rectFavourite.right &&
						y >= rectFavourite.top && y <= rectFavourite.bottom){
						isFavourity = !isFavourity;
						if(listener != null){
							listener.OnFavourite(isFavourity, song);
						}
						invalidate();
					}
				}
				if(y >= heightView/2 && y <= heightView){
					if(listener != null){					
						if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
							if(x>=checkArea)
								listener.OnShowReviewKaraoke(song);
							else
								listener.OnShowLyric(song);	
						} else {
							listener.OnShowLyric(song);
						}
					}
				}	
			}
			
		}
		return true;
	}
	
///////////////////////////////////////////
	
	private Song song;
	private String textABC = "";
	private MEDIA_TYPE ismedia = MEDIA_TYPE.MIDI;
	private boolean isFavourity = false;
	private boolean isRemix = false;
	private boolean isSinger = false;
	private boolean isUser = false;
	public void setContent(Song song){
		this.song = song;
		this.isFavourity = song.isFavourite();
		this.isRemix = song.isRemix();
		this.isSinger = song.isMediaSinger();
		this.ismedia = song.getMediaType();
		this.isUser = !song.isSoncaSong();
		
		this.nameSinger = song.getSinger().getNamecut();
		try {
			if(ismedia == MEDIA_TYPE.MIDI){
				this.nameSinger = song.getMusician().getNamecut();
			}
			if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && song.isYoutubeSong()){
				this.nameSinger = song.getSingerName();
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}		
		
//		int intABC = song.getTypeABC();
//		switch (intABC) {
//		case 1:		textABC = "(A)";		break;
//		case 2:		textABC = "(B)";		break;
//		case 3:		textABC = "(C)";		break;
//		default:	break;
//		}
		
		String name = "";
		if(song.isRealYoutubeSong()){
			name = song.getName();	
		} else {
			name = song.getSpannable().toString();
		}	
		this.nameSong = name;
		invalidate();
	}

	private Timer timerDownload = null;

	private void stopTimerDownload() {
		if (timerDownload != null) {
			timerDownload.cancel();
			timerDownload = null;
		}
		countDownload = -1;
	}

	private int countDownload = -1;

	private void startTimerDownload() {
		stopTimerDownload();
		countDownload = 0;
		timerDownload = new Timer();
		timerDownload.schedule(new TimerTask() {

			@Override
			public void run() {

				countDownload++;
				if (countDownload > 3) {
					countDownload = 0;
				}

				handlerInvalidate.sendEmptyMessage(0);
			}
		}, 200, 400);
	}

	private Handler handlerInvalidate = new Handler() {
		public void handleMessage(Message msg) {
			invalidate();
		};
	};
	
}
