package vn.com.sonca.Touch.CustomView;

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
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnLongClickListener;

public class TouchMyGroupPlaylist extends View  implements OnLongClickListener {
	
	private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintSimple = new Paint(Paint.ANTI_ALIAS_FLAG);
	private TextPaint mainText = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    private int widthLayout = 400;
    private int heightLayout = 400;
	private Drawable dFavourite;
	private Drawable dSinger;
	
	private String textSong = "Tam biet mua ha, Tam biet mua ha, Tam biet mua ha";
	private String textLyric = "Mot hai ba bon man sau bay tam chin muoi";
	private String textSinger = "Anh Khanh, Truong Chi";
	private String textABC = "";
	private String textID = "001251 | 512345";
	
	private int position;
	private int intABC = 0;
	private boolean isActive;
	private boolean isFavourity;
	private boolean isRemix;
	private boolean isSinger;
	private boolean isUser;
	private MEDIA_TYPE ismedia;
	
	public TouchMyGroupPlaylist(Context context) {
		super(context);
		initView(context);
	}

	public TouchMyGroupPlaylist(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchMyGroupPlaylist(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private OnGroupPlaylistListener listener;
	public interface OnGroupPlaylistListener {
		public void OnSingerLink(boolean bool , String nameString, int[] idSinger);
		public void OnFristRes(boolean bool, Song song , int position, float x , float y);
		public void OnFavourity(boolean bool , Song song);
		public void OnActive(boolean bool , Song song, String ipSong , int position, float x , float y);
		public void OnShowLyric(int position, String idSong);
		public void OnDelete(Song song , int position);
		public void OnDownYouTube(Song song);
		public void onPlayYouTube(Song song);
	}
	
	public void setOnGroupPlaylistListener(OnGroupPlaylistListener listener){
		this.listener = listener;
	}

	private String xoa;
	private String yeuthich;
	private String updown;
	
	private Drawable drawable;
	private Drawable drawSinger;
	private Drawable drawMove, drawUser;
	private Drawable drawActive , drawInActive;
	private Drawable drawRemix, drawMidi, drawMV, drawMVVid;
	private Drawable drawFavourite, drawFavouriteNO;
	private Drawable drawMoveXam;
	private Drawable drawChangPerfect;
	private Drawable drawCheck;
	
	private Drawable zlightdrawSinger;
	private Drawable zlightdrawMove, zlightdrawUser;
	private Drawable zlightdrawActive , zlightdrawInActive;
	private Drawable zlightdrawRemix, zlightdrawMidi, zlightdrawMV;
	private Drawable zlightdrawFavourite, zlightdrawFavouriteNO;
	private Drawable zlightdrawMoveXam;
	private Drawable zlightdrawChangPerfect;
	private Drawable zlightdrawCheck;
	
	private Drawable drawYouTube, drawDownload, drawDownloadStop, drawXemThu;
	
	private void initView(Context context) {
		setOnLongClickListener(this);
		xoa = getResources().getString(R.string.xoa);
		updown = getResources().getString(R.string.updown);
		yeuthich = getResources().getString(R.string.type_favourite);
		
		drawInActive = getResources().getDrawable(R.drawable.touch_image_remove_hover_83x83);
		drawActive = getResources().getDrawable(R.drawable.touch_image_remove_83x83);
		drawFavourite = getResources().getDrawable(R.drawable.touch_image_favourite_active_45x45);
		drawFavouriteNO = getResources().getDrawable(R.drawable.touch_image_favourite_45x45);
		drawMoveXam = getResources().getDrawable(R.drawable.touch_image_updown_xam);
		drawMove = getResources().getDrawable(R.drawable.touch_image_updown_70x100);
		drawSinger = getResources().getDrawable(R.drawable.touch_song_vocal_48x48);
		drawRemix = getResources().getDrawable(R.drawable.touch_image_remix_62x35);
		drawMidi = getResources().getDrawable(R.drawable.new_midi);
		drawUser = getResources().getDrawable(R.drawable.touch_icon_user);
		drawMV = context.getResources().getDrawable(R.drawable.icon_ktv_1note);
		drawMVVid = context.getResources().getDrawable(R.drawable.icon_ktv_video);
		drawCheck = getResources().getDrawable(R.drawable.touch_image_check_47x46);
		drawChangPerfect = getResources().getDrawable(R.drawable.touch_icon_hoaam);
		
		zlightdrawActive = context.getResources().getDrawable(R.drawable.zlight_image_remove_83x83);
		zlightdrawInActive = context.getResources().getDrawable(R.drawable.zlight_image_remove_hover_83x83);
		zlightdrawFavourite = context.getResources().getDrawable(R.drawable.zlight_image_favourite_active_45x45);
		zlightdrawFavouriteNO = context.getResources().getDrawable(R.drawable.zlight_image_favourite_inactive_45x45);
		zlightdrawMoveXam = getResources().getDrawable(R.drawable.zlight_image_updown_70x100_xam);
		zlightdrawSinger = context.getResources().getDrawable(R.drawable.zlight_song_vocal_48x48);
		zlightdrawRemix = context.getResources().getDrawable(R.drawable.zlight_image_remix_62x35);
		zlightdrawMidi = context.getResources().getDrawable(R.drawable.zlight_image_midi_50x35);
		zlightdrawUser = context.getResources().getDrawable(R.drawable.zlight_icon_user);
		zlightdrawMV = context.getResources().getDrawable(R.drawable.zlight_ktv_50x30);
		zlightdrawMove = getResources().getDrawable(R.drawable.zlight_image_updown_70x100);
		zlightdrawChangPerfect = getResources().getDrawable(R.drawable.zlight_icon_hoaam);
		
		song = new Song();
		isActive = false;
		isUser = true;
		/*
		isFavourity = true;
		ismedia = MEDIA_TYPE.MIDI;
		isSinger = true;
		isRemix = true;
		*/
		
		drawYouTube = context.getResources().getDrawable(R.drawable.youtube_icon);
		drawDownload = context.getResources().getDrawable(R.drawable.youtube_icon_download);
		drawDownloadStop = context.getResources().getDrawable(R.drawable.youtube_icon_dungdown);
		drawXemThu = getResources().getDrawable(R.drawable.youtube_icon_xemtruoc);
		
	}
	
	private Song song;
	private boolean boolRed = false;
	public void setContentView(int position , Song song){
		// this.textSong = cutText(30, song.getName());
		boolRed = song.getId() == song.getIndex5();
		if (boolRed) {
			this.textID = convertIdSong(song.getId()) + " | -";
		} else {
			
			this.textID = convertIdSong(song.getId()) + " | " + song.getIndex5();
		}
		if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && (MyApplication.flagSmartK_801 || MyApplication.flagSmartK_CB) 
				&& song.isOfflineSong() == false){
			this.textID = convertIdSong(song.getId()) + " | ";
		}
		if(song.isRealYoutubeSong()){
			this.textSong = song.getName();
			this.textSinger = "";
			this.textLyric = "";
		} else {
			this.textSong = song.getSpannable().toString();	
			this.textSinger = cutText(14, song.getSinger().getName());
			this.textLyric = cutText(40, song.getLyric());
		}		
		this.position = position;
		this.isActive = song.isActive();
		this.isFavourity = song.isFavourite();
		this.isRemix = song.isRemix();
		this.isSinger = song.isMediaSinger();
		this.ismedia = song.getMediaType();
		this.isUser = !song.isSoncaSong();
		
		textABC = "";
		intABC = song.getTypeABC();
		/*
		int intABC = song.getTypeABC();
		switch (intABC) {
		case 1:		textABC = "(A)";		break;
		case 2:		textABC = "(B)";		break;
		case 3:		textABC = "(C)";		break;
		default:	break;
		}
		*/
		this.song = song;
		isClickable = false;
		isFirst = false;

		if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && song.isYoutubeSong()){
			this.textSinger = cutText(14,song.getSingerName());
		} else {
			if(ismedia == MEDIA_TYPE.MIDI){
				this.textSinger = cutText(14, song.getMusician().getName());
			}else{
				this.textSinger = cutText(14, song.getSinger().getName());
			}
		}
		
		invalidate();

	}
	
	private Typeface typeface;
	public void setTypeface(Typeface typeface){
		this.typeface = typeface;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	    int myHeight = (int) (0.6188*getResources().getDisplayMetrics().heightPixels/5);
	    if(MyApplication.flagHong){
	    	myHeight = (int) (0.6188*getResources().getDisplayMetrics().heightPixels/6);
	    }
	    setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(myHeight, MeasureSpec.UNSPECIFIED));
	}
	
	private Rect rectMV;
	private Rect rectMIDI;
	private Rect rectSinger;
	private Rect rectRemix;
	private Rect rectMove;
	private Rect rectUser;
	private Rect rectDelete;
	private Rect rectFavourite;
	private float textPX, textPY, textPS;
	private float ytX , ytY , ytS;
	private float udX , udY , udS;
	private float idS, idX, idY;
	
	private Rect rectCheck;
	private Rect rectYouTube, rectYouTube2, rectOnline;
	private Rect rectDownYouTube;
	private LinearGradient gradient;
	private Rect rectChangPerfect = new Rect();
    @Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		setK(w,h);
		
		//------------------//

		int hr = (int) (0.35 * h);
		int wr = 47 * hr / 46;
		rectCheck = new Rect(0, 0, wr, hr);
		gradient = new LinearGradient(0, 0, w, 0, 
				Color.argb(255, 0, 42, 80),
				Color.argb(255, 1, 76, 141), 
				TileMode.CLAMP);

		//------------------//
		
		textPS = (float) (0.2*h);
		textPX = (float) (0.005*w);
		textPY = (float) (0.87*h);
		
		float offsetX = (float) (0.9*w);
		float offsetY = (float) (0.68*h);
		float vuong = (float) (0.18*h);
		ytS = (float) (0.1*h);
		mainText.setTextSize(ytS);
		ytX = offsetX - mainText.measureText(yeuthich)/2;
		ytY = (float) (0.95*h);
		rectFavourite = new Rect(
				(int)(offsetX - vuong), 
				(int)(offsetY - vuong), 
				(int)(offsetX + vuong), 
				(int)(offsetY + vuong));
		offsetX = (float) (0.69*w);
		offsetY = (float) (0.8*h);
		float vuongH = (float) (0.13*h);
		float vuongW = vuongH * 68 / 38;
		rectMIDI = new Rect(
				(int)(offsetX - vuongW), 
				(int)(offsetY - vuongH), 
				(int)(offsetX + vuongW), 
				(int)(offsetY + vuongH));
		
		offsetX = (float) (0.85*w);
		offsetY = (float) (0.79*h);
		if(MyApplication.flagHong){
			offsetX = (float) (0.85*w);
			offsetY = (float) (0.79*h);
		}
		vuongH = (float) (0.11*h);
		vuongW = vuongH*36/26;
		rectYouTube = new Rect(
				(int)(offsetX - vuongW), 
				(int)(offsetY - vuongH), 
				(int)(offsetX + vuongW), 
				(int)(offsetY + vuongH));
		
		offsetX = (float) (0.86*w);
		offsetY = (float) (0.79*h);
		vuongH = (float) (0.11*h);
		vuongW = vuongH*14/17;
		rectYouTube2 = new Rect(
				(int)(offsetX - vuongW), 
				(int)(offsetY - vuongH), 
				(int)(offsetX + vuongW), 
				(int)(offsetY + vuongH));
		
		offsetX = (float) (0.84*w);
		offsetY = (float) (0.68*h);
		vuongH = (float) (0.15*h);
		vuongW = vuongH*28/30;
		rectDownYouTube = new Rect(
				(int)(offsetX - vuongW), 
				(int)(offsetY - vuongH), 
				(int)(offsetX + vuongW), 
				(int)(offsetY + vuongH));		
		
		offsetX = (float) (0.69*w);
		offsetY = (float) (0.8*h);
		vuongH = (float) (0.13*h);
		vuongW = vuongH*84/38;
		rectMV = new Rect(
				(int)(offsetX - vuongW), 
				(int)(offsetY - vuongH), 
				(int)(offsetX + vuongW), 
				(int)(offsetY + vuongH));
		offsetX = (float) (0.74*w);
		offsetY = (float) (0.79*h);
		vuongH = (float) (0.11*h);
		vuongW = vuongH*45/38;
		rectSinger = new Rect(
				(int)(offsetX - vuongW), 
				(int)(offsetY - vuongH), 
				(int)(offsetX + vuongW), 
				(int)(offsetY + vuongH));
		rectChangPerfect.set(
				(int)(offsetX - vuongW), 
				(int)(offsetY - vuongW), 
				(int)(offsetX + vuongW), 
				(int)(offsetY + vuongW));
		offsetX = (float) (0.79*w);
		offsetY = (float) (0.79*h);
		vuongH = (float) (0.13*h);
		vuongW = vuongH*86/38;
		rectRemix = new Rect(
				(int)(offsetX - vuongW), 
				(int)(offsetY - vuongH), 
				(int)(offsetX + vuongW), 
				(int)(offsetY + vuongH));
		offsetY = (float) (0.5*h);
		vuongW = (float) (0.055*w);
		vuongH = (float) (0.4*h);
		rectMove = new Rect(
				(int)(w - 0.01*w - vuongW), 
				(int)(offsetY - vuongH), 
				(int)(w - 0.01*w), 
				(int)(offsetY + vuongH));
		
		offsetX = (float) (0.85*w);
		offsetY = (float) (0.86*h);
		vuongH = (float) (0.07*h);
		vuongW = vuongH*42/22;
		rectUser = new Rect(
				(int)(offsetX - vuongW), 
				(int)(offsetY - vuongH), 
				(int)(offsetX + vuongW), 
				(int)(offsetY + vuongH));
		
		offsetX = (float) (0.25*w);
		offsetY = (float) (0.72*h);
		vuongH = (float) (0.15*h);
		vuongW = vuongH*42/26;
		rectOnline = new Rect(
				(int)(offsetX - vuongW), 
				(int)(offsetY - vuongH), 
				(int)(offsetX + vuongW), 
				(int)(offsetY + vuongH));
		
		udS = (float) (0.1*h);
		mainText.setTextSize(udS);
		udX = (float) (0.94*w);
		udY = (float) (0.95*h);
		
		FirstSong = heightLayout;
    	SingerLink = ActiveSong = ytX;
    	
    	idS = (float) (0.25*h);
    	idX = heightLayout;
    	idY = 5*heightLayout/6;
		
    }
    
    private void resetPaint(){
    	mainText.reset();
    	mainText.setAntiAlias(true);
    	mainText.setShader(null);
    		//------//
    	mainPaint.reset();
    	mainPaint.setAntiAlias(true);
    	mainPaint.setShader(null);
    }
    
    private int color_01;
    private int color_02;
    private int color_03;
    private int color_04;
    private int color_05;
    private int color_06;
    private int color_07;
    private int color_08;
    private int color_09;
    private int color_10;
    private int color_11;
    private int color_12;
    private int color_13;
    @Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
//		MyApplication.intColorScreen = MyApplication.SCREEN_GREEN;
		
		// BACKVIEW
		
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			RT3S = 350 * heightLayout / 1080;
			RT3X = 1780 * widthLayout / 1920;
			RT3Y = (float) (2.5 * heightLayout / 6);
			color_01 = Color.argb(255, 180, 254, 255);
			color_02 = Color.argb(255, 0, 78, 144);
			color_03 = Color.argb(255, 0, 253, 253);
			color_04 = Color.CYAN;
			color_05 = Color.GRAY;
			color_06 = Color.argb(255, 1, 165, 254);
			color_07 = Color.argb(255, 0, 254, 255);
			color_08 = Color.argb(255, 182, 253, 255);
			color_09 = Color.parseColor("#ffe002");
			color_10 = Color.argb(255, 114, 172, 185);
			color_11 = Color.argb(255, 1, 100, 131);
			color_12 = Color.YELLOW;
			color_13 = Color.GREEN;
			if((MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && song.isYoutubeSong()) || 
					(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && (MyApplication.flagSmartK_801 || MyApplication.flagSmartK_CB)
					&& song.isOfflineSong() == false)){
				color_07 = Color.argb(255, 255, 238, 0);
				color_08 = Color.argb(255, 255, 238, 0);
			}
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			RT3S = 330 * heightLayout / 1080;
			RT3X = 1780 * widthLayout / 1920;
			RT3Y = (float) (2.3 * heightLayout / 6);
			color_01 = Color.parseColor("#C1FFE8");
			color_02 = Color.parseColor("#C1FFE8");
			color_03 = Color.argb(0, 0, 253, 253);
			color_04 = Color.parseColor("#10B008");
			color_05 = Color.GRAY;
			color_06 = Color.argb(222, 0, 0, 0);
			color_07 = Color.parseColor("#7B1FA2");
			color_08 = Color.parseColor("#7B1FA2");
			color_09 = Color.parseColor("#64949F");
			color_10 = Color.parseColor("#64949F");
			color_11 = Color.parseColor("#64949F");
			color_12 = Color.parseColor("#005249");
			color_13 = Color.parseColor("#10B008");

		}
		
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			
			if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && song.isYoutubeSong()){

				// YOUTUBE SONG
				resetPaint();
				
				float offsetX = (float) (0.9*widthLayout);
				float offsetY = (float) (0.73*heightLayout);
				float vuongH = (float) (0.22*heightLayout);
				float vuongW = vuongH*50/43;
				rectFavourite = new Rect(
						(int)(offsetX - vuongW), 
						(int)(offsetY - vuongH), 
						(int)(offsetX + vuongW), 
						(int)(offsetY + vuongH));

				if (isClickable) {
					paintSimple.setShader(gradient);
					paintSimple.setStrokeWidth(getHeight());
					canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, paintSimple);
					// drawCheck.setBounds(rectCheck);
					// drawCheck.draw(canvas);
				}

				mainPaint.setStyle(Style.STROKE);
				mainPaint.setStrokeWidth((float) 1.0);
				mainPaint.setColor(color_02);

				Path path = new Path();
				path.moveTo(widthLayout, 0);
				path.lineTo(widthLayout, heightLayout);
				path.lineTo(0, heightLayout);
				path.lineTo(0, 0);
				// if(position == 0){
				path.lineTo(widthLayout, 0);
				// }
				canvas.drawPath(path, mainPaint);

				// ---------------------------------------

				path = new Path();
				path.moveTo(0, 0 + Line);
				path.lineTo(0, 0);
				// if(position == 0){
				path.lineTo(0 + Line, 0);
				// }
				path.moveTo(widthLayout, 0 + Line);
				path.lineTo(widthLayout, 0);
				// if(position == 0){
				path.lineTo(widthLayout - Line, 0);
				// }
				path.moveTo(widthLayout, heightLayout - Line);
				path.lineTo(widthLayout, heightLayout);
				path.lineTo(widthLayout - Line, heightLayout);
				path.moveTo(0, heightLayout - Line);
				path.lineTo(0, heightLayout);
				path.lineTo(0 + Line, heightLayout);
				mainPaint.setColor(color_03);
				canvas.drawPath(path, mainPaint);

				// ---------------------------------------

				float FR = (float) (1.9 * CircleR);
				if (!isFirst) {
					drawActive.setBounds((int) (CircleX - FR), (int) (CircleY - FR), (int) (CircleX + FR),
							(int) (CircleY + FR));
					drawActive.draw(canvas);
				} else {
					drawInActive.setBounds((int) (CircleX - FR), (int) (CircleY - FR), (int) (CircleX + FR),
							(int) (CircleY + FR));
					drawInActive.draw(canvas);
				}

				mainText.setColor(color_04);
				mainText.setTextSize((float) (0.18 * heightLayout));
				float w = mainText.measureText(xoa);
				canvas.drawText(xoa, CircleX - w / 2, (float) (0.9 * heightLayout), mainText);

				// ----------------------------------

				mainText.setStyle(Style.FILL);
				mainText.setTextSize(RT1S);

				int widthSong = (int) (0.55 * widthLayout);
				if (MyApplication.flagHong) {
					widthSong = (int) (0.50 * widthLayout);
				}
				
				int sizeSong = (int) mainText.measureText(textSong + "..." + textABC);
				if (sizeSong > widthSong) {
					float f = mainText.measureText("..." + textABC);
					int i = mainText.breakText(textSong, true, widthSong - f, null);
					textSong = textSong.subSequence(0, i) + "...";
				}
				if (isClickable()) {
					mainText.setColor(color_07);
					canvas.drawText(textSong + textABC, RT1X, RT1Y, mainText);
				} else {
					mainText.setColor(color_08);
					canvas.drawText(textSong + textABC, RT1X, RT1Y, mainText);
				}
				// if(!textSinger.equals("-")){
				mainText.setStyle(Style.FILL);
				mainText.setTextSize(RT3S);
				mainText.setColor(color_06);
				SingerLink = RT3X - mainText.measureText(textSinger);
				canvas.drawText(textSinger, SingerLink, RT3Y, mainText);
				if (SingerLink > ActiveSong) {
					SingerLink = ActiveSong;
				}
				// }

				// ------IDSong-------------------------------

				if (!textID.equals("000000 | -")) {
					mainText.setStyle(Style.FILL);
					mainText.setTextSize(idS);
					mainText.setARGB(255, 244, 67, 54);
					canvas.drawText(textID, idX, idY, mainText);
				}

				// ----------------------------------
				
				resetPaint();
				mainText.setTextSize(idS);
				RT2X = idX + mainText.measureText("0000000 | 0000000 ");
				mainText.setStyle(Style.FILL);
				mainText.setTextSize(RT2S);
				if (isActive) {
					mainText.setColor(color_10);
				} else {
					mainText.setColor(color_11);
				}
				mainText.setTypeface(typeface);
				if (textLyric == null) {
					canvas.drawText("", RT2X, RT2Y, mainText);
				} else {
					int i = mainText.breakText(textLyric, true, (int) (0.65 * widthLayout - RT2X), null);
					if (textLyric.length() > i) {
						String lyric = textLyric.substring(0, i - 3) + "...";
						canvas.drawText(lyric, RT2X, RT2Y, mainText);
					} else {
						canvas.drawText(textLyric, RT2X, RT2Y, mainText);
					}
				}
				
				resetPaint();
				
				// -------------xem truoc---------------------
				if(!song.isSambaSong()){
					drawable = getResources().getDrawable(R.drawable.youtube_xemtruoc);
					drawable.setBounds(rectFavourite);
					drawable.draw(canvas);	
				}
				
				
				if(MyApplication.youtube_Download_ID == song.getId()){
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
						drawable.setBounds(rectYouTube2);
						drawable.draw(canvas);	
					}
					
					mainText.setStyle(Style.FILL);
					mainText.setTextSize(ptS);
					mainText.setARGB(255, 0, 253, 253);
					String str = MyApplication.youtube_Download_percent + "%";
					float mWidth = mainText.measureText(str);
					canvas.drawText(str, rectYouTube2.left - mWidth - 2 * widthLayout / 480, ptY, mainText);
				} else {
					stopTimerDownload();
					
					drawable = getResources().getDrawable(R.drawable.youtube_icon);
					drawable.setBounds(rectYouTube);
					drawable.draw(canvas);	
				}	
				
				// --------------MIDI vs MV--------------------

				if (ismedia == MEDIA_TYPE.MIDI) {
					drawMidi.setBounds(rectMV);
					drawMidi.draw(canvas);
					if (intABC != 0 && (MyApplication.intSvrModel == MyApplication.SONCA || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK)) {
						drawChangPerfect.setBounds(rectChangPerfect);
						drawChangPerfect.draw(canvas);
					}
				} else {
					drawMVVid.setBounds(rectMV);
					drawMVVid.draw(canvas);
				}

				// ---------------isSinger-------------------

				if (isSinger) {
					drawSinger.setBounds(rectSinger);
					drawSinger.draw(canvas);
				}

				// ---------------online icon----------------------
				drawable = getResources().getDrawable(R.drawable.icon_icloud);
				drawable.setBounds(rectOnline);
				drawable.draw(canvas);	

				// ---------------isRemix-------------------

				if (isRemix) {
					drawRemix.setBounds(rectRemix);
					drawRemix.draw(canvas);
				}
				if (MyApplication.bOffFirst == true) {
					if (MyApplication.isMoveList == false) {
						drawMoveXam.setBounds(rectMove);
						drawMoveXam.draw(canvas);
					} else {
						drawMove.setBounds(rectMove);
						drawMove.draw(canvas);
					}
				} else {
					drawMoveXam.setBounds(rectMove);
					drawMoveXam.draw(canvas);
				}
				
				mainText.setStyle(Style.FILL);
				mainText.setTextSize(udS);
				if (MyApplication.bOffFirst == true) {
					if (MyApplication.isMoveList == false) {
						mainText.setColor(color_05);
					} else {
						mainText.setColor(color_04);
					}
				} else {
					mainText.setColor(color_05);
				}
				canvas.drawText(updown, udX, udY, mainText);

				/*
				 * drawDelete.setBounds(rectDelete); drawDelete.draw(canvas);
				 */
				mainText.setStyle(Style.FILL);
				mainText.setColor(color_13);
				mainText.setTextSize(textPS);
				canvas.drawText(String.valueOf(position), textPX, textPY, mainText);

				resetPaint();
				
			
				
			} else if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && (MyApplication.flagSmartK_801 || MyApplication.flagSmartK_CB)
					&& song.isOfflineSong() == false){
				// ONLINE SONG	
				
				resetPaint();

				if (isClickable) {
					paintSimple.setShader(gradient);
					paintSimple.setStrokeWidth(getHeight());
					canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, paintSimple);
					// drawCheck.setBounds(rectCheck);
					// drawCheck.draw(canvas);
				}

				mainPaint.setStyle(Style.STROKE);
				mainPaint.setStrokeWidth((float) 1.0);
				mainPaint.setColor(color_02);

				Path path = new Path();
				path.moveTo(widthLayout, 0);
				path.lineTo(widthLayout, heightLayout);
				path.lineTo(0, heightLayout);
				path.lineTo(0, 0);
				// if(position == 0){
				path.lineTo(widthLayout, 0);
				// }
				canvas.drawPath(path, mainPaint);

				// ---------------------------------------

				path = new Path();
				path.moveTo(0, 0 + Line);
				path.lineTo(0, 0);
				// if(position == 0){
				path.lineTo(0 + Line, 0);
				// }
				path.moveTo(widthLayout, 0 + Line);
				path.lineTo(widthLayout, 0);
				// if(position == 0){
				path.lineTo(widthLayout - Line, 0);
				// }
				path.moveTo(widthLayout, heightLayout - Line);
				path.lineTo(widthLayout, heightLayout);
				path.lineTo(widthLayout - Line, heightLayout);
				path.moveTo(0, heightLayout - Line);
				path.lineTo(0, heightLayout);
				path.lineTo(0 + Line, heightLayout);
				mainPaint.setColor(color_03);
				canvas.drawPath(path, mainPaint);

				// ---------------------------------------

				float FR = (float) (1.9 * CircleR);
				if (!isFirst) {
					drawActive.setBounds((int) (CircleX - FR), (int) (CircleY - FR), (int) (CircleX + FR),
							(int) (CircleY + FR));
					drawActive.draw(canvas);
				} else {
					drawInActive.setBounds((int) (CircleX - FR), (int) (CircleY - FR), (int) (CircleX + FR),
							(int) (CircleY + FR));
					drawInActive.draw(canvas);
				}

				mainText.setColor(color_04);
				mainText.setTextSize((float) (0.18 * heightLayout));
				float w = mainText.measureText(xoa);
				canvas.drawText(xoa, CircleX - w / 2, (float) (0.9 * heightLayout), mainText);

				// ----------------------------------

				mainText.setStyle(Style.FILL);
				mainText.setTextSize(RT1S);

				int widthSong = (int) (0.55 * widthLayout);
				if (MyApplication.flagHong) {
					widthSong = (int) (0.50 * widthLayout);
				}
				
				int sizeSong = (int) mainText.measureText(textSong + "..." + textABC);
				if (sizeSong > widthSong) {
					float f = mainText.measureText("..." + textABC);
					int i = mainText.breakText(textSong, true, widthSong - f, null);
					textSong = textSong.subSequence(0, i) + "...";
				}
				if (isClickable()) {
					mainText.setColor(color_07);
					canvas.drawText(textSong + textABC, RT1X, RT1Y, mainText);
				} else {
					mainText.setColor(color_08);
					canvas.drawText(textSong + textABC, RT1X, RT1Y, mainText);
				}
				// if(!textSinger.equals("-")){
				mainText.setStyle(Style.FILL);
				mainText.setTextSize(RT3S);
				mainText.setColor(color_06);
				SingerLink = RT3X - mainText.measureText(textSinger);
				canvas.drawText(textSinger, SingerLink, RT3Y, mainText);
				if (SingerLink > ActiveSong) {
					SingerLink = ActiveSong;
				}
				// }

				// ------IDSong-------------------------------

				if (!textID.equals("000000 | -")) {
					mainText.setStyle(Style.FILL);
					mainText.setTextSize(idS);
					mainText.setARGB(255, 244, 67, 54);
					canvas.drawText(textID, idX, idY, mainText);
				}

				// ----------------------------------
				
				resetPaint();
				mainText.setTextSize(idS);
				RT2X = idX + mainText.measureText("0000000 | 0000000 ");
				mainText.setStyle(Style.FILL);
				mainText.setTextSize(RT2S);
				if (isActive) {
					mainText.setColor(color_10);
				} else {
					mainText.setColor(color_11);
				}
				mainText.setTypeface(typeface);
				if (textLyric == null) {
					canvas.drawText("", RT2X, RT2Y, mainText);
				} else {
					int i = mainText.breakText(textLyric, true, (int) (0.65 * widthLayout - RT2X), null);
					if (textLyric.length() > i) {
						String lyric = textLyric.substring(0, i - 3) + "...";
						canvas.drawText(lyric, RT2X, RT2Y, mainText);
					} else {
						canvas.drawText(textLyric, RT2X, RT2Y, mainText);
					}
				}
				
				resetPaint();			
				if (isFavourity) {
					drawFavourite.setBounds(rectFavourite);
					drawFavourite.draw(canvas);
				} else {
					drawFavouriteNO.setBounds(rectFavourite);
					drawFavouriteNO.draw(canvas);
				}
				mainText.setStyle(Style.FILL);
				mainText.setTextSize(ytS);
				mainText.setColor(color_12);
				canvas.drawText(yeuthich, ytX, ytY, mainText);
				
				if(MyApplication.youtube_Download_ID == song.getId()){
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
						drawable.setBounds(rectYouTube2);
						drawable.draw(canvas);	
					}
					
					mainText.setStyle(Style.FILL);
					mainText.setTextSize(ptS);
					mainText.setARGB(255, 0, 253, 253);
					String str = MyApplication.youtube_Download_percent + "%";
					float mWidth = mainText.measureText(str);
					canvas.drawText(str, rectYouTube2.left - mWidth - 2 * widthLayout / 480, ptY, mainText);
				} else {
					stopTimerDownload();
					
					drawable = getResources().getDrawable(R.drawable.youtube_icon);
					drawable.setBounds(rectYouTube);
					drawable.draw(canvas);	
				}	

				// --------------MIDI vs MV--------------------

				if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
					if (ismedia == MEDIA_TYPE.MIDI || ismedia == MEDIA_TYPE.MP3) {
						drawMidi.setBounds(rectMV);
						drawMidi.draw(canvas);
						if (intABC != 0 && (MyApplication.intSvrModel == MyApplication.SONCA || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK)) {
							drawChangPerfect.setBounds(rectChangPerfect);
							drawChangPerfect.draw(canvas);
						}
					} else {
						if(ismedia == MEDIA_TYPE.VIDEO){
							drawMVVid.setBounds(rectMV);
							drawMVVid.draw(canvas);
						} else {
							drawMV.setBounds(rectMV);
							drawMV.draw(canvas);						
						}
					}
				} else {
					if (ismedia == MEDIA_TYPE.MIDI) {
						drawMidi.setBounds(rectMV);
						drawMidi.draw(canvas);
						if (intABC != 0 && (MyApplication.intSvrModel == MyApplication.SONCA || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK)) {
							drawChangPerfect.setBounds(rectChangPerfect);
							drawChangPerfect.draw(canvas);
						}
					} else {
						if(ismedia == MEDIA_TYPE.VIDEO){
							drawMVVid.setBounds(rectMV);
							drawMVVid.draw(canvas);
						} else {
							drawMV.setBounds(rectMV);
							drawMV.draw(canvas);						
						}
					}
				}

				// ---------------isSinger-------------------

				if (isSinger) {
					drawSinger.setBounds(rectSinger);
					drawSinger.draw(canvas);
				}

				// ---------------online icon----------------------
				drawable = getResources().getDrawable(R.drawable.icon_icloud);
				drawable.setBounds(rectOnline);
				drawable.draw(canvas);	

				// ---------------isRemix-------------------

				if (isRemix) {
					drawRemix.setBounds(rectRemix);
					drawRemix.draw(canvas);
				}
				if (MyApplication.bOffFirst == true) {
					if (MyApplication.isMoveList == false) {
						drawMoveXam.setBounds(rectMove);
						drawMoveXam.draw(canvas);
					} else {
						drawMove.setBounds(rectMove);
						drawMove.draw(canvas);
					}
				} else {
					drawMoveXam.setBounds(rectMove);
					drawMoveXam.draw(canvas);
				}
				
				mainText.setStyle(Style.FILL);
				mainText.setTextSize(udS);
				if (MyApplication.bOffFirst == true) {
					if (MyApplication.isMoveList == false) {
						mainText.setColor(color_05);
					} else {
						mainText.setColor(color_04);
					}
				} else {
					mainText.setColor(color_05);
				}
				canvas.drawText(updown, udX, udY, mainText);

				/*
				 * drawDelete.setBounds(rectDelete); drawDelete.draw(canvas);
				 */
				mainText.setStyle(Style.FILL);
				mainText.setColor(color_13);
				mainText.setTextSize(textPS);
				canvas.drawText(String.valueOf(position), textPX, textPY, mainText);

				resetPaint();
				
			} else {
				// NORMAL SONG
				resetPaint();

				float offsetX = (float) (0.9*widthLayout);
				float offsetY = (float) (0.68*heightLayout);
				float vuong = (float) (0.18*heightLayout);
				rectFavourite = new Rect(
						(int)(offsetX - vuong), 
						(int)(offsetY - vuong), 
						(int)(offsetX + vuong), 
						(int)(offsetY + vuong));
				
				if (isClickable) {
					paintSimple.setShader(gradient);
					paintSimple.setStrokeWidth(getHeight());
					canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, paintSimple);
					// drawCheck.setBounds(rectCheck);
					// drawCheck.draw(canvas);
				}

				mainPaint.setStyle(Style.STROKE);
				mainPaint.setStrokeWidth((float) 1.0);
				mainPaint.setColor(color_02);

				Path path = new Path();
				path.moveTo(widthLayout, 0);
				path.lineTo(widthLayout, heightLayout);
				path.lineTo(0, heightLayout);
				path.lineTo(0, 0);
				// if(position == 0){
				path.lineTo(widthLayout, 0);
				// }
				canvas.drawPath(path, mainPaint);

				// ---------------------------------------

				path = new Path();
				path.moveTo(0, 0 + Line);
				path.lineTo(0, 0);
				// if(position == 0){
				path.lineTo(0 + Line, 0);
				// }
				path.moveTo(widthLayout, 0 + Line);
				path.lineTo(widthLayout, 0);
				// if(position == 0){
				path.lineTo(widthLayout - Line, 0);
				// }
				path.moveTo(widthLayout, heightLayout - Line);
				path.lineTo(widthLayout, heightLayout);
				path.lineTo(widthLayout - Line, heightLayout);
				path.moveTo(0, heightLayout - Line);
				path.lineTo(0, heightLayout);
				path.lineTo(0 + Line, heightLayout);
				mainPaint.setColor(color_03);
				canvas.drawPath(path, mainPaint);

				// ---------------------------------------

				float FR = (float) (1.9 * CircleR);
				if (!isFirst) {
					drawActive.setBounds((int) (CircleX - FR), (int) (CircleY - FR), (int) (CircleX + FR),
							(int) (CircleY + FR));
					drawActive.draw(canvas);
				} else {
					drawInActive.setBounds((int) (CircleX - FR), (int) (CircleY - FR), (int) (CircleX + FR),
							(int) (CircleY + FR));
					drawInActive.draw(canvas);
				}

				mainText.setColor(color_04);
				mainText.setTextSize((float) (0.18 * heightLayout));
				float w = mainText.measureText(xoa);
				canvas.drawText(xoa, CircleX - w / 2, (float) (0.9 * heightLayout), mainText);

				// ----------------------------------

				mainText.setStyle(Style.FILL);
				mainText.setTextSize(RT1S);

				int widthSong = (int) (0.55 * widthLayout);
				if (MyApplication.flagHong) {
					widthSong = (int) (0.50 * widthLayout);
				}
				
				int sizeSong = (int) mainText.measureText(textSong + "..." + textABC);
				if (sizeSong > widthSong) {
					float f = mainText.measureText("..." + textABC);
					int i = mainText.breakText(textSong, true, widthSong - f, null);
					textSong = textSong.subSequence(0, i) + "...";
				}
				if (isClickable()) {
					mainText.setColor(color_07);
					canvas.drawText(textSong + textABC, RT1X, RT1Y, mainText);
				} else {
					mainText.setColor(color_08);
					canvas.drawText(textSong + textABC, RT1X, RT1Y, mainText);
				}
				// if(!textSinger.equals("-")){
				mainText.setStyle(Style.FILL);
				mainText.setTextSize(RT3S);
				mainText.setColor(color_06);
				SingerLink = RT3X - mainText.measureText(textSinger);
				canvas.drawText(textSinger, SingerLink, RT3Y, mainText);
				if (SingerLink > ActiveSong) {
					SingerLink = ActiveSong;
				}
				// }

				// ------IDSong-------------------------------

				if (!textID.equals("000000 | -")) {
					mainText.setStyle(Style.FILL);
					mainText.setTextSize(idS);
					if (!isUser) {
						if (isActive) {
							mainText.setColor(color_07);
						} else {
							mainText.setColor(color_08);
						}
					} else {
						mainText.setColor(color_09);
					}
					canvas.drawText(textID, idX, idY, mainText);
				}

				// ----------------------------------

				resetPaint();
				mainText.setTextSize(idS);
				RT2X = idX + mainText.measureText("0000000 | 0000000 ");
				mainText.setStyle(Style.FILL);
				mainText.setTextSize(RT2S);
				if (isActive) {
					mainText.setColor(color_10);
				} else {
					mainText.setColor(color_11);
				}
				mainText.setTypeface(typeface);
				if (textLyric == null) {
					canvas.drawText("", RT2X, RT2Y, mainText);
				} else {
					int i = mainText.breakText(textLyric, true, (int) (0.65 * widthLayout - RT2X), null);
					if (textLyric.length() > i) {
						String lyric = textLyric.substring(0, i - 3) + "...";
						canvas.drawText(lyric, RT2X, RT2Y, mainText);
					} else {
						canvas.drawText(textLyric, RT2X, RT2Y, mainText);
					}
				}

				// -------------isFavourity---------------------

				resetPaint();
				if (isFavourity) {
					drawFavourite.setBounds(rectFavourite);
					drawFavourite.draw(canvas);
				} else {
					drawFavouriteNO.setBounds(rectFavourite);
					drawFavouriteNO.draw(canvas);
				}
				mainText.setStyle(Style.FILL);
				mainText.setTextSize(ytS);
				mainText.setColor(color_12);
				canvas.drawText(yeuthich, ytX, ytY, mainText);

				// --------------MIDI vs MV--------------------

				if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
					if (ismedia == MEDIA_TYPE.MIDI || ismedia == MEDIA_TYPE.MP3) {
						drawMidi.setBounds(rectMV);
						drawMidi.draw(canvas);
						if (intABC != 0 && (MyApplication.intSvrModel == MyApplication.SONCA || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK)) {
							drawChangPerfect.setBounds(rectChangPerfect);
							drawChangPerfect.draw(canvas);
						}
					} else {
						if(ismedia == MEDIA_TYPE.VIDEO){
							drawMVVid.setBounds(rectMV);
							drawMVVid.draw(canvas);
						} else {
							drawMV.setBounds(rectMV);
							drawMV.draw(canvas);						
						}
					}
				} else {
					if (ismedia == MEDIA_TYPE.MIDI) {
						drawMidi.setBounds(rectMV);
						drawMidi.draw(canvas);
						if (intABC != 0 && (MyApplication.intSvrModel == MyApplication.SONCA || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK)) {
							drawChangPerfect.setBounds(rectChangPerfect);
							drawChangPerfect.draw(canvas);
						}
					} else {
						if(ismedia == MEDIA_TYPE.VIDEO){
							drawMVVid.setBounds(rectMV);
							drawMVVid.draw(canvas);
						} else {
							drawMV.setBounds(rectMV);
							drawMV.draw(canvas);						
						}
					}
				}
				

				// ---------------isSinger-------------------

				if (isSinger) {
					drawSinger.setBounds(rectSinger);
					drawSinger.draw(canvas);
				}

				// ---------------isUser----------------------

				if (isUser && drawUser != null) {
					drawUser.setBounds(rectUser);
					drawUser.draw(canvas);
				}

				// ---------------isRemix-------------------

				if (isRemix) {
					drawRemix.setBounds(rectRemix);
					drawRemix.draw(canvas);
				}
				if (MyApplication.bOffFirst == true) {
					if (MyApplication.isMoveList == false) {
						drawMoveXam.setBounds(rectMove);
						drawMoveXam.draw(canvas);
					} else {
						drawMove.setBounds(rectMove);
						drawMove.draw(canvas);
					}
				} else {
					drawMoveXam.setBounds(rectMove);
					drawMoveXam.draw(canvas);
				}

				mainText.setStyle(Style.FILL);
				mainText.setTextSize(udS);
				if (MyApplication.bOffFirst == true) {
					if (MyApplication.isMoveList == false) {
						mainText.setColor(color_05);
					} else {
						mainText.setColor(color_04);
					}
				} else {
					mainText.setColor(color_05);
				}
				canvas.drawText(updown, udX, udY, mainText);

				/*
				 * drawDelete.setBounds(rectDelete); drawDelete.draw(canvas);
				 */
				mainText.setStyle(Style.FILL);
				mainText.setColor(color_13);
				mainText.setTextSize(textPS);
				canvas.drawText(String.valueOf(position), textPX, textPY, mainText);

				resetPaint();
				
			}
			
		} else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			
		}

	}
    
    private boolean isFirst;
    private boolean isClickable = false;
    @Override
    public void setPressed(boolean pressed) {
    	super.setPressed(pressed);
    	isClickable = pressed;
		if(listener != null && pressed == false){
			isTouch = false;
			isFirst = false;
			invalidate();
		}
    }
    
    private boolean isEnableLongClick = true;
    private boolean isLongClick = false;
	@Override
	public boolean onLongClick(View view) {
		MyLog.e("MySongView", "onLongClick");
		if(song.isRealYoutubeSong()){
			return false;
		}
		if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && song.isSambaSong()){
			return false;
		}
		if(listener != null && !idSong.equals("") && isEnableLongClick == true){
			isFirst = false;
			isLongClick = false;
			if(song != null){
				String id5 = String.valueOf(song.getIndex5());
				listener.OnShowLyric(position, id5);
			}
		}
		return false;
	}

    private boolean isTouch = false;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	super.onTouchEvent(event);
    	if (event.getAction() == MotionEvent.ACTION_DOWN) {
    		float offsetX = event.getX();
    		float offsetY = event.getY();
    		if (offsetX >= 0 && offsetX <= FirstSong) {
    			isEnableLongClick = false;
    		} else if(offsetX > ActiveSong && offsetX <= rectMove.left
    			&& offsetY > heightLayout/2 && offsetY <= heightLayout) {
    			isEnableLongClick = false;
    		} else if(offsetX > SingerLink && offsetX <= rectMove.left
    			&& offsetY >= 0 && offsetY <= heightLayout/2){
    			isEnableLongClick = false;
    		} else if(offsetX > rectMove.left && offsetX < widthLayout){
    			isEnableLongClick = false;
    			return false;
    		} else {
    			isEnableLongClick = true;
    		}
    		
    		//---------------------//
    		
    		isLongClick = true;
    		isClickable = true;
    		float x = event.getX();
    		if (x >= 0 && x <= FirstSong) {
    			isFirst = true;
    		}
    		invalidate();
		}
    	if(!idSong.equals(""))
    	if(event.getAction() == MotionEvent.ACTION_UP && isLongClick){
    		isFirst = false;
    		isClickable = false;
    		float offsetX = event.getX();
    		float offsetY = event.getY();
    		if (offsetX >= 0 && offsetX <= FirstSong) {
    			if (listener != null) {
	    			listener.OnDelete(song , position);
    			}invalidate();	
    		}
    		if (offsetX > FirstSong && offsetX < SingerLink) {
    			if (listener != null) {
    				int[] location = new int[2];
					this.getLocationOnScreen(location);
					int ScreenX = location[0] + heightLayout / 2;
					int ScreenY = location[1] + heightLayout / 2;
    				listener.OnActive(isActive, song , idSong, position, ScreenX, ScreenY);
    			}invalidate();	
    		}
    		if(offsetX > ActiveSong && offsetX <= rectMove.left
    			&& offsetY > heightLayout/2 && offsetY <= heightLayout) {
    			//MyLog.d("", "=onTouchEvent==onPlayYouTube=");
    			if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && song.isYoutubeSong() && MyApplication.youtube_Download_percent != 100){
    				if (listener != null) {
						listener.onPlayYouTube(song);
					}
    				return true;
    			}
    			
    			isFavourity = !isFavourity;
    			if (listener != null) {
    				listener.OnFavourity(isFavourity, song);
    			}invalidate();	
    		}		
    		if(offsetX > SingerLink && offsetX <= rectMove.left
    			&& offsetY >= 0 && offsetY <= heightLayout/2){
    			if (listener != null) {
    				if(!textID.equals("000000 | -")){
						if(ismedia == MEDIA_TYPE.MIDI){
							listener.OnSingerLink(true, nameMusician, idMusician);
						}else{
							listener.OnSingerLink(false, nameSinger, idSinger);
						}
    				}
    			}invalidate();		
    		}
    	}
    	return true;
    }
    
    private String convertIdSong(int id){
		String stringId = String.valueOf(id);
		switch (stringId.length()) {
		case 1: 	return "00000" + stringId;
		case 2: 	return "0000" + stringId;
		case 3: 	return "000" + stringId;
		case 4: 	return "00" + stringId;
		case 5: 	return "0" + stringId;
		default: 	break;
		}
		return stringId;
	}
    
///////////////////////// - CUT TEXT - ////////////////////////////
    
	private String cutText(int maxLength, String content) {
		if(content.length() <= maxLength){
			return content;
		}		
		return content.substring(0, maxLength) + "...";
	}
 
    
/*
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

	private String cutTextWord(float textSize, float maxLength, String[] content, Paint paint) {
		float length = 0;
		StringBuffer buffer = new StringBuffer("");
		for (int i = 0; i < content.length; i++) {
			length = paint.measureText(buffer.toString() + " " + content[i]
					+ "...");
			if (length < maxLength) {
				buffer.append(content[i] + " ");
			} else {
				break;
			}
		}
		buffer.append("...");
		return buffer.toString();
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
*/    
///////////////////////////////////////////////////////////////////////
	
    private float Line , KT4S;
    private float RT1S , RT1X , RT1Y;
    private float RT2S , RT2X , RT2Y;
    private float RT3S , RT3X , RT3Y;
    private int RD1 , RD1L , RD1R , RD1T , RD1B;
    private int RD2 , RD2L , RD2R , RD2T , RD2B;
    private int RD3L , RD3R , RD3T , RD3B;
    private int CircleS1 , CircleS2;
    private float CircleX , CircleY , CircleR; 
    private float FirstSong , ActiveSong , SingerLink;
    
    private float ptS, ptY;
    
    private void setK(int w , int h){    	
    	widthLayout = w;
    	heightLayout = h;
    	
    	Line = 150*h/1080;
    	
    	CircleS1 = 60*h/1080;
    	CircleS2 = 50*h/1080;
    	CircleX = h/2; 
    	CircleY = (float) (0.45*h); 
    	CircleR = 200*h/1080;
    	
    	RT1S = 400*h/1080;
    	RT1X = heightLayout; 
    	RT1Y = heightLayout/2;
    	
    	RT2S = 270*h/1080;
    	RT2X = heightLayout; 
    	RT2Y = 5*heightLayout/6;
    	
    	RT3S = 350*h/1080;
    	RT3X = 1780*w/1920; 
    	RT3Y = (float) (2.5*heightLayout/6);
    	
    	RD1 = 400*h/1080;
    	RD1R = 1900*w/1920; 
    	RD1L = RD1R - RD1;
    	RD1B = (int) (5.5*heightLayout/6);
    	RD1T = RD1B - RD1;
    	
    	RD2 = 400*h/1080;
    	RD2R = RD1L - 10*w/1920; 
    	RD2L = RD2R - RD2;
    	RD2B = (int) (5.5*heightLayout/6);
    	RD2T = RD2B - RD2;
    	
    	RD3R = RD2L - 40*w/1920; 
    	RD3L = RD3R - 700*h/1080; 
    	RD3B = (int) (5.1*heightLayout/6);
    	RD3T = (int) (3.5*heightLayout/6);
    	
    	KT4S = 180*h/1080;
    	
    	ptS = 16 * h / 100;
		ptY = 85 * h / 100;
    	
    }
    
//-----------------------------------//
    
    private String idSong;
    private int[] idSinger;
    private String nameSinger;
    
    public String getIdSong() {
		return idSong;
	}

	public void setIdSong(String idSong) {
		this.idSong = idSong;
		invalidate();
	}
	

	public int[] getIdSinger() {
		return idSinger;
	}

	public void setIdSinger(int[] idSinger) {
		this.idSinger = idSinger;
	}

	public String getNameSinger() {
		return nameSinger;
	}

	public void setNameSinger(String nameSinger) {
		this.nameSinger = nameSinger;
	}

	private int[] idMusician = new int[]{};
	private String nameMusician = "";

	public int[] getIdMusician() {
		return idMusician;
	}

	public void setIdMusician(int[] idMusician) {
		this.idMusician = idMusician;
	}

	public String getNameMusician() {
		return nameMusician;
	}

	public void setNameMusician(String nameMusician) {
		this.nameMusician = nameMusician;
	}
	
	
	private Timer timerDownload = null;
	
	private void stopTimerDownload(){
		if(timerDownload != null){
			timerDownload.cancel();
			timerDownload = null;
		}
		countDownload = -1;
	}
	
	private int countDownload = -1;
	private void startTimerDownload(){
		stopTimerDownload();
		countDownload = 0;
		timerDownload = new Timer();
		timerDownload.schedule(new TimerTask() {
			
			@Override
			public void run() {
				
				countDownload++;
				if(countDownload > 3){
					countDownload = 0;
				}
				
				handlerInvalidate.sendEmptyMessage(0);
			}
		}, 200, 400);
	}
	
	private Handler handlerInvalidate = new Handler(){
		public void handleMessage(Message msg) {
			invalidate();
		};
	};
		
}
