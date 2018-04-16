package vn.com.sonca.AddDataSong;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

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
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.Layout.Alignment;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnLongClickListener;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.database.DBInterface;
import vn.com.sonca.params.Song;
import vn.com.sonca.utils.AppConfig.MEDIA_TYPE;
import vn.com.sonca.zzzzz.MyApplication;

public class ViewDelSong extends View implements OnLongClickListener {

	private String TAB = "ViewDelSong";
	private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintSimple = new Paint(Paint.ANTI_ALIAS_FLAG);
	private TextPaint mainText = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    private int widthLayout = 0;
    private int heightLayout = 0;
	private Drawable dFavourite;
	private Drawable dSinger;
	
	private String textSong = "";
	private String textLyric = "";
	private String textSinger = "";
	private String textABC = "";
	private String textID = "";
	
	private int position;
	private int intABC = 0;
	private boolean isActive = false;
	private boolean isFavourity;
	private boolean isRemix;
	private boolean isSinger;
	private boolean isUser = false;
	private MEDIA_TYPE ismedia;
	
	public ViewDelSong(Context context) {
		super(context);
		initView(context);
	}

	public ViewDelSong(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public ViewDelSong(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private OnViewDelListener listener;
	public interface OnViewDelListener {
		public void OnActive(boolean bool , Song song , String idSong , float x , float y);
		public void OnLockNotify();
		public void OnUnLockNotify();		
	}
	
	public void setOnViewDelListener(OnViewDelListener listener){
		this.listener = listener;
	}

	private String yeuthich;
	private String uutien;
	private Context context;

	private Drawable drawSinger, drawUser;
	private Drawable drawRemix, drawMidi, drawMV;
	private Drawable drawFavourite, drawFavouriteNO;
	private Drawable drawActive,drawInActive ,drawHover;
	private Drawable drawFirstXam;
	private Drawable drawChangPerfect;
	
	private Drawable drawCheck;
	private Drawable drawYouTube, drawDownload, drawDownloadStop, drawXemThu;
	
	private Drawable zlightdrawSinger, zlightdrawUser;
	private Drawable zlightdrawRemix, zlightdrawMidi, zlightdrawMV;
	private Drawable zlightdrawFavourite, zlightdrawFavouriteNO;
	private Drawable zlightdrawActive,zlightdrawInActive ,zlightdrawHover;
	private Drawable zlightdrawFirstXam;
	private Drawable zlightdrawChangPerfect;
	
	private Drawable zlightdrawCheck;
	
	
	private void initView(Context context) {
		this.context = context;
		setOnLongClickListener(this);
		mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mainText = new TextPaint(Paint.ANTI_ALIAS_FLAG);
		uutien = getResources().getString(R.string.uutien);
		yeuthich = getResources().getString(R.string.type_favourite);
		drawCheck = getResources().getDrawable(R.drawable.touch_image_check_47x46);
		drawFirstXam = getResources().getDrawable(R.drawable.ydark_image_1st_xam);
		drawChangPerfect = getResources().getDrawable(R.drawable.touch_icon_hoaam);
		drawYouTube = getResources().getDrawable(R.drawable.youtube_icon);
		drawDownload = getResources().getDrawable(R.drawable.youtube_icon_download);
		drawDownloadStop = getResources().getDrawable(R.drawable.youtube_icon_dungdown);
		drawXemThu = getResources().getDrawable(R.drawable.youtube_icon_xemtruoc);
		
		zlightdrawChangPerfect = getResources().getDrawable(R.drawable.zlight_icon_hoaam);
		zlightdrawFirstXam = getResources().getDrawable(R.drawable.ydark_image_1st_xam);
		
		/*
		drawActive = getResources().getDrawable(R.drawable.ydark_image_1st_active);
		drawInActive = getResources().getDrawable(R.drawable.ydark_image_1st_inactive);
		drawHover = getResources().getDrawable(R.drawable.ydark_image_1st_cham);
		drawFavourite = getResources().getDrawable(R.drawable.image_favourite_active_45x45);
		drawFavouriteNO = getResources().getDrawable(R.drawable.image_favourite_45x45);
		drawSinger = getResources().getDrawable(R.drawable.song_vocal_48x48);
		drawUser = getResources().getDrawable(R.drawable.icon_user);
		drawRemix = getResources().getDrawable(R.drawable.image_remix_62x35);
		drawMidi = getResources().getDrawable(R.drawable.image_midi_50x35);
		drawMV = getResources().getDrawable(R.drawable.ktv_50x30);
		ismedia = MEDIA_TYPE.MIDI;
		isFavourity = true;
		isSinger = true;
		isActive = true;
		isRemix = true;
		isUser = true;

		song = new Song();
		isActive = false;
		
		String text = "01234567890123456789012345678901234567890123456789012345678901234567890123456789";
		textSong = replaceAll(text);
		textLyric = "Mot hai ba bon man sau bay tam chin muoi";
		textSinger = "Anh Khanh, Truong Chi";
		textID = "001251";
		wordtoSpan = createSpannable(text , textSong ,"AN", 1);
		*/
		
	}
	
	public void setDrawable(Drawable drawActive , Drawable drawInActive , Drawable drawHover,
			Drawable drawFavourite , Drawable drawFavouriteNO,
			Drawable drawSinger , Drawable drawRemix, Drawable drawMidi , 
			Drawable drawMV, Drawable drawUser){
		this.drawInActive = drawInActive;
		this.drawActive = drawActive;
		this.drawHover = drawHover;
		this.drawFavourite = drawFavourite;
		this.drawFavouriteNO = drawFavouriteNO;
		this.drawSinger = drawSinger;
		this.drawRemix = drawRemix;
		this.drawMidi = drawMidi;
		this.drawUser = drawUser;
		this.drawMV = drawMV;
	}
	
	public void setDrawLight(Drawable zlightdrawActive, Drawable zlightdrawInActive , 
			Drawable zlightdrawHover, Drawable zlightdrawFavourite,
			Drawable zlightdrawFavouriteNO, Drawable zlightdrawSinger , 
			Drawable zlightdrawRemix , Drawable zlightdrawMidi , Drawable zlightdrawMV, 
			Drawable zlightdrawUser){
		this.zlightdrawActive = zlightdrawActive;
		this.zlightdrawInActive = zlightdrawInActive;
		this.zlightdrawHover = zlightdrawHover;
		this.zlightdrawFavourite = zlightdrawFavourite;
		this.zlightdrawFavouriteNO = zlightdrawFavouriteNO;
		this.zlightdrawSinger = zlightdrawSinger;
		this.zlightdrawRemix = zlightdrawRemix;
		this.zlightdrawMidi = zlightdrawMidi;
		this.zlightdrawUser = zlightdrawUser;
		this.zlightdrawMV = zlightdrawMV;
	}
	
	private int ordinarily = -1;
	public void setOrdinarilyPlaylist(int ordinarily){
		if (ordinarily <= 0) {
			this.ordinarily = -1;
		} else {
			this.ordinarily = ordinarily;
		}
	}
	
	private float transX;
	private float transY;
    private String textSearch = "";
//	private Spannable wordtoSpan = null;
	
	private Spannable wordtoSpan = new SpannableString("");
	public void setSongName(String name , boolean isActive , Spannable wordtoSpan){		
 		this.textSong = name;
		this.isActive = isActive;
		if (wordtoSpan == null) {
			this.wordtoSpan = new SpannableString(textSong);
		} else {
			this.wordtoSpan = wordtoSpan;
		}	
	}
    
	private Song song;
	private boolean boolRed = false;
//	private Spannable spanNemberID = new SpannableString("888000 | 33888");
	private Spannable spanNemberID = new SpannableString("");
	public void setContentView(int position, Song song){		
		this.spanNemberID = song.getSpannableNumber();
		this.textLyric = song.getLyric();
		this.position = position;
		this.isFavourity = song.isFavourite();
		this.isRemix = song.isRemix();
		this.isSinger = song.isMediaSinger();
		this.ismedia = song.getMediaType();
		this.isUser = !song.isSoncaSong();
		int id = song.getId();
		if (id != 0) {
			this.idSong = String.valueOf(id);
		} else {
			this.idSong = "";
		}
		
		int id5 = song.getIndex5();
		if (id5 != 0) {
			this.idSong5 = String.valueOf(id5);
		} else {
			this.idSong5 = "";
		}
		
		intABC = song.getTypeABC();
		textABC = "";
		/*
		int intABC = song.getTypeABC();
		switch (intABC) {
		case 1:		textABC = "(A)";		break;
		case 2:		textABC = "(B)";		break;
		case 3:		textABC = "(C)";		break;
		default:	break;
		}
		*/
		
		this.isActive = song.isActive();
		
		this.song = song;		
		isClick = false;
		
			//--------------------//
		
		if(ismedia == MEDIA_TYPE.MIDI){
			this.textSinger = song.getMusician().getNamecut();
		}else{
			this.textSinger = song.getSinger().getNamecut();
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
	private Rect rectFavourite;
	private Rect rectUser;
	private Rect rectChangPerfect = new Rect();
	private float ytX , ytY , ytS;
	private float utX , utY , utS;
	private float otX , otY , otS;
	private Path pathGoc = new Path();
	private Path pathKhung = new Path();
	private SpannableStringBuilder builder = new SpannableStringBuilder();
	private SpannableStringBuilder builderNumber = new SpannableStringBuilder();

	private Rect rectCheck;
	private Rect rectYouTube;
	private Rect rectDownYouTube;
	private LinearGradient gradient;
	
    @Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		setK(w,h);
		
		//------------------//
		
		int hr = (int) (0.35*h);
		int wr = 47*hr/46;
		rectCheck = new Rect(0, 0, wr, hr);
		gradient = new LinearGradient(0, 0, w, 0, 
				Color.argb(255, 0, 42, 80), 
				Color.argb(255, 1, 76, 141), 
				TileMode.CLAMP);
		
		//------------------//
		
		utS = (float) (0.11*h);
		mainText.setTextSize(utS);
		utX = CircleX - mainText.measureText(uutien)/2;
		utY = (float) (0.93*h);
		
		float offsetX = (float) (0.935*w);
		float offsetY = (float) (0.68*h);
		float vuong = (float) (0.17*h);
		ytS = (float) (0.1*h);
		mainText.setTextSize(ytS);
		ytX = offsetX - mainText.measureText(yeuthich)/2;
		ytY = (float) (0.95*h);
		rectFavourite = new Rect(
				(int)(offsetX - vuong), 
				(int)(offsetY - vuong), 
				(int)(offsetX + vuong), 
				(int)(offsetY + vuong));
		
		// offsetX = 0;
		// offsetX = (float) (0.878*w);
		offsetX = (float) (0.72*w);
		offsetY = (float) (0.8*h);
		float vuongH = (float) (0.13*h);
		float vuongW = vuongH * 68 / 38;
		rectMIDI = new Rect(
				(int)(offsetX - vuongW), 
				(int)(offsetY - vuongH), 
				(int)(offsetX + vuongW), 
				(int)(offsetY + vuongH));
		
		offsetX = (float) (0.875*w);
		offsetY = (float) (0.68*h);
		vuongH = (float) (0.15*h);
		vuongW = vuongH*28/30;
		rectDownYouTube = new Rect(
				(int)(offsetX - vuongW), 
				(int)(offsetY - vuongH), 
				(int)(offsetX + vuongW), 
				(int)(offsetY + vuongH));		
		
		offsetX = (float) (0.72*w);
		offsetY = (float) (0.8*h);
		vuongH = (float) (0.13*h);
		vuongW = vuongH*68/38;
		rectMV = new Rect(
				(int)(offsetX - vuongW), 
				(int)(offsetY - vuongH), 
				(int)(offsetX + vuongW), 
				(int)(offsetY + vuongH));
		
		offsetX = (float) (0.11*w);
		offsetY = (float) (0.32*h);
		if(MyApplication.flagHong){
			offsetX = (float) (0.125*w);
			offsetY = (float) (0.31*h);
		}	
		vuongH = (float) (0.12*h);
		vuongW = vuongH*36/26;
		rectYouTube = new Rect(
				(int)(offsetX - vuongW), 
				(int)(offsetY - vuongH), 
				(int)(offsetX + vuongW), 
				(int)(offsetY + vuongH));
		
		offsetX = (float) (0.77*w);
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
		
		offsetX = (float) (0.825*w);
		offsetY = (float) (0.79*h);
		vuongH = (float) (0.13*h);
		vuongW = vuongH*86/38;		
		rectRemix = new Rect(
				(int)(offsetX - vuongW), 
				(int)(offsetY - vuongH), 
				(int)(offsetX + vuongW), 
				(int)(offsetY + vuongH));
		
		offsetX = (float) (0.885*w);
		offsetY = (float) (0.86*h);
		vuongH = (float) (0.07*h);
		vuongW = vuongH*42/22;
		rectUser = new Rect(
				(int)(offsetX - vuongW), 
				(int)(offsetY - vuongH), 
				(int)(offsetX + vuongW), 
				(int)(offsetY + vuongH));
		
		pathKhung = new Path();
		pathKhung.reset();
		pathKhung.moveTo(widthLayout , 0);
		pathKhung.lineTo(widthLayout, heightLayout);
		pathKhung.lineTo(0 , heightLayout);
		pathKhung.lineTo(0, 0);
		//if(position == 0){
			pathKhung.lineTo(widthLayout , 0);
		//}
		pathGoc = new Path();
		pathGoc.reset();
		pathGoc.moveTo(0 , 0 + Line);
		pathGoc.lineTo(0, 0);
		//if(position == 0){
			pathGoc.lineTo(0 + Line , 0);
		//}
		pathGoc.moveTo(widthLayout , 0 + Line);
		pathGoc.lineTo(widthLayout , 0);
		//if(position == 0){
			pathGoc.lineTo(widthLayout - Line , 0);
		//}
		pathGoc.moveTo(widthLayout , heightLayout - Line);
		pathGoc.lineTo(widthLayout, heightLayout);
		pathGoc.lineTo(widthLayout - Line , heightLayout);
		pathGoc.moveTo(0 , heightLayout - Line);
		pathGoc.lineTo(0 , heightLayout);
		pathGoc.lineTo(0 + Line , heightLayout);
		
		transX = h;
		transY = (float) (0.05*h);
		
		otX = (float) (3);
		otY = (float) (0.9*h);
		otS = (float) (0.2*h);
		
		FirstSong = heightLayout;
		SingerLink = Favourity = ytX;
		
    }
   
    long tgian;
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

		/*
		// BACKVIEW
		isUser = true; 
		isActive = true; 
		MyApplication.bOffFirst = true;
		MyApplication.intWifiRemote = MyApplication.SONCA_KARTROL;
		MyApplication.intColorScreen = MyApplication.SCREEN_LIGHT;
		*/
		
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			RT3S = 350 * heightLayout / 1080;
			RT3X = 1900 * widthLayout / 1920;
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
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			RT3S = 330 * heightLayout / 1080;
			RT3X = 1900 * widthLayout / 1920;
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
			// DRAW 0

			if (MyApplication.intWifiRemote == MyApplication.SONCA) {
				
				resetPaint();
				canvas.translate(0, 0);
				
				if (isActive) {
					paintSimple.setShader(gradient);
					paintSimple.setStrokeWidth(getHeight());
					canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, paintSimple);
			
				}

					// NORMAL SONG
					
					mainPaint.setStyle(Style.STROKE);
					mainPaint.setStrokeWidth((float) 1.0);
					if (isActive) {
						mainPaint.setColor(color_01);
					} else {
						mainPaint.setColor(color_02);
					}
					canvas.drawPath(pathKhung, mainPaint);

					mainPaint.setColor(color_03);
					canvas.drawPath(pathGoc, mainPaint);

					resetPaint();

					// ---------------------------------------

					float FR = (float) (1.5 * CircleR);
					if (MyApplication.bOffFirst == true) {
						if (isTouch) {
							if (drawHover != null) {
								drawHover.setBounds((int) (CircleX - FR), (int) (CircleX - FR), (int) (CircleX + FR),
										(int) (CircleX + FR));
								drawHover.draw(canvas);
							}
						} else {
							if (isActive) {
								if (drawActive != null) {
									drawActive.setBounds((int) (CircleX - FR), (int) (CircleX - FR), (int) (CircleX + FR),
											(int) (CircleX + FR));
									drawActive.draw(canvas);
								}
							} else {
								if (drawInActive != null) {
									drawInActive.setBounds((int) (CircleX - FR), (int) (CircleX - FR), (int) (CircleX + FR),
											(int) (CircleX + FR));
									drawInActive.draw(canvas);
								}
							}
						}
					} else {
						drawFirstXam.setBounds((int) (CircleX - FR), (int) (CircleX - FR), (int) (CircleX + FR),
								(int) (CircleX + FR));
						drawFirstXam.draw(canvas);
					}

					mainText.setTextSize(utS);
					if (MyApplication.bOffFirst == true) {
						mainText.setColor(color_04);
					} else {
						mainText.setColor(color_05);
					}
					// canvas.drawText(uutien, utX, utY, mainText);

					// ----------------------------------

					// if(!textSinger.equals("-")){
					mainText.setStyle(Style.FILL);
					mainText.setTextSize(RT3S);
					mainText.setColor(color_06);
					SingerLink = RT3X - mainText.measureText(textSinger);
					canvas.drawText(textSinger, SingerLink, RT3Y, mainText);
					if (SingerLink > Favourity) {
						SingerLink = Favourity;
					}
					// }

					// ---------Lyric-------------------------

					mainText.setStyle(Style.FILL);
					mainText.setTextSize(RT2S);
					RT2X = idX + mainText.measureText("0000000 | 0000000 ");
					if (isActive) {
						mainText.setColor(color_10);
					} else {
						mainText.setColor(color_11);
					}
					mainText.setTypeface(typeface);
					if (textLyric == null) {
						canvas.drawText("", RT2X, RT2Y, mainText);
					} else {
						int i = mainText.breakText(textLyric, true, (int) (0.7 * widthLayout - RT2X), null);
						if (textLyric.length() > i) {
							String lyric = textLyric.substring(0, i - 3) + "...";
							canvas.drawText(lyric, RT2X, RT2Y, mainText);
						} else {
							canvas.drawText(textLyric, RT2X, RT2Y, mainText);
						}
					}

					// -------------isFavourity---------------------

					resetPaint();
/*					
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
					mainText.setColor(color_12);
					canvas.drawText(yeuthich, ytX, ytY, mainText);
*/
					// --------------MIDI vs MV--------------------

					if (!textID.equals("000000 | -")) {
						if (ismedia == MEDIA_TYPE.MIDI) {
							if (drawMidi != null) {
								drawMidi.setBounds(rectMIDI);
								drawMidi.draw(canvas);
							}
							if (intABC != 0 && (MyApplication.intSvrModel == MyApplication.SONCA || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK)) {
								drawChangPerfect.setBounds(rectChangPerfect);
								drawChangPerfect.draw(canvas);
							}
						} else {
							if (drawMV != null) {
								drawMV.setBounds(rectMV);
								drawMV.draw(canvas);
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
					tgian = System.currentTimeMillis();

					// ---------------isUser----------------------

					if (isUser && drawUser != null) {
						drawUser.setBounds(rectUser);
						drawUser.draw(canvas);
					}

					// ---------------ordinarily-------------------

					if (ordinarily != -1) {
						mainText.setStyle(Style.FILL);
						mainText.setTextSize(otS);
						mainText.setColor(color_13);
						canvas.drawText(String.valueOf(ordinarily), otX, otY, mainText);
					}

					// ---------------wordtoSpan-------------------

					canvas.translate(0, 0);
					
					mainText.setStyle(Style.FILL);
					mainText.setTextSize(RT1S);
					if (isActive) {
						mainText.setColor(color_07);
					} else {
						mainText.setColor(color_08);
					}
					builder.clear();

					int widthSong = (int) (0.6 * widthLayout);
					int sizeSong = (int) mainText.measureText(wordtoSpan.toString() + "..." + textABC);
					if (sizeSong > widthSong) {
						float f = mainText.measureText("..." + textABC);
						int i = mainText.breakText(wordtoSpan.toString(), true, widthSong - f, null);
						builder.append(wordtoSpan.subSequence(0, i));
						builder.append("..." + textABC);
					} else {
						builder.append(wordtoSpan);
						builder.append(textABC);
					}

					StaticLayout layout = new StaticLayout(builder, mainText, widthLayout, Alignment.ALIGN_NORMAL, 1, 0,
							true);
					canvas.translate(transX, transY);
					layout.draw(canvas);
					
					// ------IDSong-------------------------------

					if (spanNemberID != null) {
						if (!spanNemberID.toString().equals("000000 | -")) {
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
							builderNumber.clear();
							builderNumber.append(spanNemberID);
							StaticLayout layout0 = new StaticLayout(builderNumber, mainText, widthLayout,
									Alignment.ALIGN_NORMAL, 1, 0, true);
							canvas.translate(-transX + idX, -transY + idY);
							layout0.draw(canvas);
						}
					}


					/*
					 * mainText.setStyle(Style.FILL);
					 * mainText.setTextSize((float)(0.5*getHeight()));
					 * mainText.setColor(color_07); canvas.drawText(TestIndex + "",
					 * (float)(getWidth()/2), (float)(0.7*getHeight()), mainText);
					 * 
					 */
					

			} else {

				resetPaint();
				canvas.translate(0, 0);

				if (isActive) {
					paintSimple.setShader(gradient);
					paintSimple.setStrokeWidth(getHeight());
					canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, paintSimple);
					drawCheck.setBounds(rectCheck);
					drawCheck.draw(canvas);
				}

				// ---------------------------------------

				mainPaint.setStyle(Style.STROKE);
				mainPaint.setStrokeWidth((float) 1.0);
				if (isClick) {
					mainPaint.setColor(color_01);
				} else {
					mainPaint.setColor(color_02);
				}
				canvas.drawPath(pathKhung, mainPaint);

				mainPaint.setColor(color_03);
				canvas.drawPath(pathGoc, mainPaint);

				resetPaint();

				// ---------------------------------------

				float FR = (float) (2 * CircleR);
				if (isTouch) {
					if (drawHover != null) {
						drawHover.setBounds((int) (CircleX - FR), (int) (CircleX - FR), (int) (CircleX + FR),
								(int) (CircleX + FR));
						drawHover.draw(canvas);
					}
				} else {
					if (isActive) {
						if (drawActive != null) {
							drawActive.setBounds((int) (CircleX - FR), (int) (CircleX - FR), (int) (CircleX + FR),
									(int) (CircleX + FR));
							drawActive.draw(canvas);
						}
					} else {
						if (drawInActive != null) {
							drawInActive.setBounds((int) (CircleX - FR), (int) (CircleX - FR), (int) (CircleX + FR),
									(int) (CircleX + FR));
							drawInActive.draw(canvas);
						}
					}
				}

				mainText.setTextSize(utS);
				mainText.setColor(color_04);
				canvas.drawText(uutien, utX, utY, mainText);

				// ----------------------------------

				// if(!textSinger.equals("-")){
				mainText.setStyle(Style.FILL);
				mainText.setTextSize(RT3S);
				mainText.setColor(color_06);
				SingerLink = RT3X - mainText.measureText(textSinger);

				canvas.drawText(textSinger, SingerLink, RT3Y, mainText);
				if (SingerLink > Favourity) {
					SingerLink = Favourity;
				}
				// }

				// ---------Lyric-------------------------

				mainText.setStyle(Style.FILL);
				mainText.setTextSize(RT2S);
				RT2X = idX + mainText.measureText("0000000 | 0000000 ");
				if (isActive) {
					mainText.setColor(color_10);
				} else {
					mainText.setColor(color_11);
				}
				mainText.setTypeface(typeface);
				if (textLyric == null) {
					canvas.drawText("", RT2X, RT2Y, mainText);
				} else {
					int i = mainText.breakText(textLyric, true, (int) (0.7 * widthLayout - RT2X), null);
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
				mainText.setColor(color_12);
				canvas.drawText(yeuthich, ytX, ytY, mainText);

				// --------------MIDI vs MV--------------------

				if (!textID.equals("000000 | -")) {
					if (ismedia == MEDIA_TYPE.MIDI) {
						if (drawMidi != null) {
							drawMidi.setBounds(rectMIDI);
							drawMidi.draw(canvas);
						}
					} else {
						if (drawMV != null) {
							drawMV.setBounds(rectMV);
							drawMV.draw(canvas);
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
				tgian = System.currentTimeMillis();

				// ---------------isUser----------------------

				if (isUser && drawUser != null) {
					drawUser.setBounds(rectUser);
					drawUser.draw(canvas);
				}

				// ---------------ordinarily-------------------

				if (ordinarily != -1) {
					mainText.setStyle(Style.FILL);
					mainText.setTextSize(otS);
					mainText.setColor(color_13);
					canvas.drawText(String.valueOf(ordinarily), otX, otY, mainText);
				}

				// ---------------wordtoSpan-------------------

				canvas.translate(0, 0);
				
				mainText.setStyle(Style.FILL);
				mainText.setTextSize(RT1S);
				if (isActive) {
					mainText.setColor(color_07);
				} else {
					mainText.setColor(color_08);
				}
				builder.clear();

				int widthSong = (int) (0.6 * widthLayout);
				int sizeSong = (int) mainText.measureText(wordtoSpan.toString() + "..." + textABC);
				if (sizeSong > widthSong) {
					float f = mainText.measureText("..." + textABC);
					int i = mainText.breakText(wordtoSpan.toString(), true, widthSong - f, null);
					builder.append(wordtoSpan.subSequence(0, i));
					builder.append("..." + textABC);
				} else {
					builder.append(wordtoSpan);
					builder.append(textABC);
				}

				StaticLayout layout = new StaticLayout(builder, mainText, widthLayout, Alignment.ALIGN_NORMAL, 1, 0,
						true);
				canvas.translate(transX, transY);
				layout.draw(canvas);
				
				// ------IDSong-------------------------------

				if (spanNemberID != null) {
					if (!spanNemberID.toString().equals("000000 | -")) {
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
						builderNumber.clear();
						builderNumber.append(spanNemberID);
						StaticLayout layout0 = new StaticLayout(builderNumber, mainText, widthLayout,
								Alignment.ALIGN_NORMAL, 1, 0, true);
						canvas.translate(-transX + idX, -transY + idY);
						layout0.draw(canvas);
					}
				}

				/*
				 * mainText.setStyle(Style.FILL);
				 * mainText.setTextSize((float)(0.5*getHeight()));
				 * mainText.setColor(color_07); canvas.drawText(TestIndex + "",
				 * (float)(getWidth()/2), (float)(0.7*getHeight()), mainText);
				 * 
				 */

			}
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {

			// DRAW 0

			if (MyApplication.intWifiRemote == MyApplication.SONCA) {

				resetPaint();
				canvas.translate(0, 0);

				// ---------------------------------------

				mainPaint.setStyle(Style.STROKE);
				mainPaint.setStrokeWidth((float) 1.0);
				if (isActive) {
					mainPaint.setColor(color_01);
				} else {
					mainPaint.setColor(color_02);
				}
				canvas.drawPath(pathKhung, mainPaint);

				mainPaint.setColor(color_03);
				canvas.drawPath(pathGoc, mainPaint);

				resetPaint();

				// ---------------------------------------

				float FR = (float) (2 * CircleR);
				if (MyApplication.bOffFirst == true) {
					if (isTouch) {
						if (zlightdrawHover != null) {
							zlightdrawHover.setBounds((int) (CircleX - FR), (int) (CircleX - FR), (int) (CircleX + FR),
									(int) (CircleX + FR));
							zlightdrawHover.draw(canvas);
						}
					} else {
						if (isActive) {
							if (zlightdrawActive != null) {
								zlightdrawActive.setBounds((int) (CircleX - FR), (int) (CircleX - FR),
										(int) (CircleX + FR), (int) (CircleX + FR));
								zlightdrawActive.draw(canvas);
							}
						} else {
							if (zlightdrawInActive != null) {
								zlightdrawInActive.setBounds((int) (CircleX - FR), (int) (CircleX - FR),
										(int) (CircleX + FR), (int) (CircleX + FR));
								zlightdrawInActive.draw(canvas);
							}
						}
					}
				} else {
					zlightdrawFirstXam.setBounds((int) (CircleX - FR), (int) (CircleX - FR), (int) (CircleX + FR),
							(int) (CircleX + FR));
					zlightdrawFirstXam.draw(canvas);
				}

				mainText.setTextSize(utS);
				if (MyApplication.bOffFirst == true) {
					mainText.setColor(color_04);
				} else {
					mainText.setColor(color_05);
				}
				canvas.drawText(uutien, utX, utY, mainText);

				// ----------------------------------

				// if(!textSinger.equals("-")){
				mainText.setStyle(Style.FILL);
				mainText.setTextSize(RT3S);
				mainText.setColor(color_06);
				SingerLink = RT3X - mainText.measureText(textSinger);
				canvas.drawText(textSinger, SingerLink, RT3Y, mainText);
				if (SingerLink > Favourity) {
					SingerLink = Favourity;
				}
				// }

				// ---------Lyric-------------------------

				mainText.setStyle(Style.FILL);
				mainText.setTextSize(RT2S);
				RT2X = idX + mainText.measureText("0000000 | 0000000 ");
				if (isActive) {
					mainText.setColor(color_10);
				} else {
					mainText.setColor(color_11);
				}
				mainText.setTypeface(typeface);
				if (textLyric == null) {
					canvas.drawText("", RT2X, RT2Y, mainText);
				} else {
					int i = mainText.breakText(textLyric, true, (int) (0.7 * widthLayout - RT2X), null);
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
					if (zlightdrawFavourite != null) {
						zlightdrawFavourite.setBounds(rectFavourite);
						zlightdrawFavourite.draw(canvas);
					}
				} else {
					if (zlightdrawFavouriteNO != null) {
						zlightdrawFavouriteNO.setBounds(rectFavourite);
						zlightdrawFavouriteNO.draw(canvas);
					}
				}
				mainText.setStyle(Style.FILL);
				mainText.setTextSize(ytS);
				mainText.setColor(color_12);
				canvas.drawText(yeuthich, ytX, ytY, mainText);

				// --------------MIDI vs MV--------------------

				if (!textID.equals("000000 | -")) {
					if (ismedia == MEDIA_TYPE.MIDI) {
						if (zlightdrawMidi != null) {
							zlightdrawMidi.setBounds(rectMIDI);
							zlightdrawMidi.draw(canvas);
						}
						if (intABC != 0 && (MyApplication.intSvrModel == MyApplication.SONCA || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK)) {
							zlightdrawChangPerfect.setBounds(rectChangPerfect);
							zlightdrawChangPerfect.draw(canvas);
						}
					} else {
						if (zlightdrawMV != null) {
							zlightdrawMV.setBounds(rectMV);
							zlightdrawMV.draw(canvas);
						}
					}
				}

				// ---------------isSinger-------------------

				if (isSinger && zlightdrawSinger != null) {
					zlightdrawSinger.setBounds(rectSinger);
					zlightdrawSinger.draw(canvas);
				}

				// ---------------isRemix-------------------

				if (isRemix && zlightdrawRemix != null) {
					zlightdrawRemix.setBounds(rectRemix);
					zlightdrawRemix.draw(canvas);
				}
				tgian = System.currentTimeMillis();

				// ---------------isUser----------------------

				if (isUser && zlightdrawUser != null) {
					zlightdrawUser.setBounds(rectUser);
					zlightdrawUser.draw(canvas);
				}

				// ---------------ordinarily-------------------

				if (ordinarily != -1) {
					mainText.setStyle(Style.FILL);
					mainText.setTextSize(otS);
					mainText.setColor(color_13);
					canvas.drawText(String.valueOf(ordinarily), otX, otY, mainText);
				}

				// ---------------wordtoSpan-------------------

				canvas.translate(0, 0);
				
				mainText.setStyle(Style.FILL);
				mainText.setTextSize(RT1S);
				if (isActive) {
					mainText.setColor(color_06);
				} else {
					mainText.setColor(color_06);
				}
				builder.clear();

				int widthSong = (int) (0.6 * widthLayout);
				int sizeSong = (int) mainText.measureText(wordtoSpan.toString() + "..." + textABC);
				if (sizeSong > widthSong) {
					float f = mainText.measureText("..." + textABC);
					int i = mainText.breakText(wordtoSpan.toString(), true, widthSong - f, null);
					builder.append(wordtoSpan.subSequence(0, i));
					builder.append("..." + textABC);
				} else {
					builder.append(wordtoSpan);
					builder.append(textABC);
				}

				StaticLayout layout = new StaticLayout(builder, mainText, widthLayout, Alignment.ALIGN_NORMAL, 1, 0,
						true);
				canvas.translate(transX, transY);
				layout.draw(canvas);
				
				// ------IDSong-------------------------------

				if (spanNemberID != null) {
					if (!spanNemberID.toString().equals("000000 | -")) {
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
						builderNumber.clear();
						builderNumber.append(spanNemberID);
						StaticLayout layout0 = new StaticLayout(builderNumber, mainText, widthLayout,
								Alignment.ALIGN_NORMAL, 1, 0, true);
						canvas.translate(-transX + idX, -transY + idY);
						layout0.draw(canvas);
					}
				}

				/*
				 * mainText.setStyle(Style.FILL);
				 * mainText.setTextSize((float)(0.5*getHeight()));
				 * mainText.setColor(color_07); canvas.drawText(TestIndex + "",
				 * (float)(getWidth()/2), (float)(0.7*getHeight()), mainText);
				 * 
				 */

			} else {

				resetPaint();
				canvas.translate(0, 0);

				if (isClick) {
					paintSimple.setColor(Color.parseColor("#FFFFFF"));
					paintSimple.setStrokeWidth(getHeight());
					canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, paintSimple);
					zlightdrawCheck.setBounds(rectCheck);
					zlightdrawCheck.draw(canvas);
				}

				// ---------------------------------------

				mainPaint.setStyle(Style.STROKE);
				mainPaint.setStrokeWidth((float) 1.0);
				if (isClick) {
					mainPaint.setColor(color_01);
				} else {
					mainPaint.setColor(color_02);
				}
				canvas.drawPath(pathKhung, mainPaint);

				mainPaint.setColor(color_03);
				canvas.drawPath(pathGoc, mainPaint);

				resetPaint();

				// ---------------------------------------

				float FR = (float) (2 * CircleR);
				if (isTouch) {
					if (zlightdrawHover != null) {
						zlightdrawHover.setBounds((int) (CircleX - FR), (int) (CircleX - FR), (int) (CircleX + FR),
								(int) (CircleX + FR));
						zlightdrawHover.draw(canvas);
					}
				} else {
					if (isActive) {
						if (zlightdrawActive != null) {
							zlightdrawActive.setBounds((int) (CircleX - FR), (int) (CircleX - FR), (int) (CircleX + FR),
									(int) (CircleX + FR));
							zlightdrawActive.draw(canvas);
						}
					} else {
						if (zlightdrawInActive != null) {
							zlightdrawInActive.setBounds((int) (CircleX - FR), (int) (CircleX - FR),
									(int) (CircleX + FR), (int) (CircleX + FR));
							zlightdrawInActive.draw(canvas);
						}
					}
				}

				mainText.setTextSize(utS);
				mainText.setColor(color_04);
				canvas.drawText(uutien, utX, utY, mainText);

				// ----------------------------------

				// if(!textSinger.equals("-")){
				mainText.setStyle(Style.FILL);
				mainText.setTextSize(RT3S);
				mainText.setColor(color_06);
				SingerLink = RT3X - mainText.measureText(textSinger);

				canvas.drawText(textSinger, SingerLink, RT3Y, mainText);
				if (SingerLink > Favourity) {
					SingerLink = Favourity;
				}
				// }

				// ---------Lyric-------------------------

				mainText.setStyle(Style.FILL);
				mainText.setTextSize(RT2S);
				RT2X = idX + mainText.measureText("0000000 | 0000000 ");
				if (isActive) {
					mainText.setColor(color_10);
				} else {
					mainText.setColor(color_11);
				}
				mainText.setTypeface(typeface);
				if (textLyric == null) {
					canvas.drawText("", RT2X, RT2Y, mainText);
				} else {
					int i = mainText.breakText(textLyric, true, (int) (0.7 * widthLayout - RT2X), null);
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
					if (zlightdrawFavourite != null) {
						zlightdrawFavourite.setBounds(rectFavourite);
						zlightdrawFavourite.draw(canvas);
					}
				} else {
					if (zlightdrawFavouriteNO != null) {
						zlightdrawFavouriteNO.setBounds(rectFavourite);
						zlightdrawFavouriteNO.draw(canvas);
					}
				}
				mainText.setStyle(Style.FILL);
				mainText.setTextSize(ytS);
				mainText.setColor(color_12);
				canvas.drawText(yeuthich, ytX, ytY, mainText);

				// --------------MIDI vs MV--------------------

				if (!textID.equals("000000 | -")) {
					if (ismedia == MEDIA_TYPE.MIDI) {
						if (zlightdrawMidi != null) {
							zlightdrawMidi.setBounds(rectMIDI);
							zlightdrawMidi.draw(canvas);
						}
					} else {
						if (zlightdrawMV != null) {
							zlightdrawMV.setBounds(rectMV);
							zlightdrawMV.draw(canvas);
						}
					}
				}

				// ---------------isSinger-------------------

				if (isSinger && zlightdrawSinger != null) {
					zlightdrawSinger.setBounds(rectSinger);
					zlightdrawSinger.draw(canvas);
				}

				// ---------------isRemix-------------------

				if (isRemix && zlightdrawRemix != null) {
					zlightdrawRemix.setBounds(rectRemix);
					zlightdrawRemix.draw(canvas);
				}
				tgian = System.currentTimeMillis();

				// ---------------isUser----------------------

				if (isUser && zlightdrawUser != null) {
					zlightdrawUser.setBounds(rectUser);
					zlightdrawUser.draw(canvas);
				}

				// ---------------ordinarily-------------------

				if (ordinarily != -1) {
					mainText.setStyle(Style.FILL);
					mainText.setTextSize(otS);
					mainText.setColor(color_13);
					canvas.drawText(String.valueOf(ordinarily), otX, otY, mainText);
				}

				// ---------------wordtoSpan-------------------

				canvas.translate(0, 0);
				
				mainText.setStyle(Style.FILL);
				mainText.setTextSize(RT1S);
				if (isActive) {
					mainText.setColor(color_06);
				} else {
					mainText.setColor(color_06);
				}
				builder.clear();

				int widthSong = (int) (0.6 * widthLayout);
				int sizeSong = (int) mainText.measureText(wordtoSpan.toString() + "..." + textABC);
				if (sizeSong > widthSong) {
					float f = mainText.measureText("..." + textABC);
					int i = mainText.breakText(wordtoSpan.toString(), true, widthSong - f, null);
					builder.append(wordtoSpan.subSequence(0, i));
					builder.append("..." + textABC);
				} else {
					builder.append(wordtoSpan);
					builder.append(textABC);
				}

				StaticLayout layout = new StaticLayout(builder, mainText, widthLayout, Alignment.ALIGN_NORMAL, 1, 0,
						true);
				canvas.translate(transX, transY);
				layout.draw(canvas);
				
				// ------IDSong-------------------------------

				if (spanNemberID != null) {
					if (!spanNemberID.toString().equals("000000 | -")) {
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
						builderNumber.clear();
						builderNumber.append(spanNemberID);
						StaticLayout layout0 = new StaticLayout(builderNumber, mainText, widthLayout,
								Alignment.ALIGN_NORMAL, 1, 0, true);
						canvas.translate(-transX + idX, -transY + idY);
						layout0.draw(canvas);
					}
				}

				/*
				 * mainText.setStyle(Style.FILL);
				 * mainText.setTextSize((float)(0.5*getHeight()));
				 * mainText.setColor(color_07); canvas.drawText(TestIndex + "",
				 * (float)(getWidth()/2), (float)(0.7*getHeight()), mainText);
				 * 
				 */

			}

		}

	}
    
    
    private boolean isFirst;
    private boolean isClick;
    @Override
    public void setPressed(boolean pressed) {
    	super.setPressed(pressed);
		if(listener != null && pressed == false){
			listener.OnUnLockNotify();
			isClick = false;
			isTouch = false;
			invalidate();
		}
    }
    
    private boolean isEnableLongClick = true;
    private boolean isLongClick = false;
	@Override
	public boolean onLongClick(View view) {
		if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && song.isYoutubeSong()){			
			return false;
		}
		if(!MyApplication.flagEverConnect){
			return false;
		}
		if(listener != null && !idSong.equals("") && isEnableLongClick == true){
			isLongClick = false;
		}
		return false;
	}

    private boolean isTouch = false;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	super.onTouchEvent(event);
    	/*
    	if (event.getAction() == MotionEvent.ACTION_DOWN) {
    		float offsetX = event.getX();
    		float offsetY = event.getY();
    		if (offsetX >= 0 && offsetX <= FirstSong) {
    			isEnableLongClick = false;
    		} else if (offsetX >= Favourity && offsetX <= widthLayout && 
    			offsetY > heightLayout/2 && offsetY <= heightLayout) {
    			isEnableLongClick = false;
    		} else if (offsetX >= SingerLink && offsetX <= widthLayout &&
    			offsetY >= 0 && offsetY <= heightLayout/2) {
    			isEnableLongClick = false;	
    		} else {
    			isEnableLongClick = true;
    		}
    		
    		//---------------------//
    		isClick = true;
    		isLongClick = true;
    		float x = event.getX();
    		if (x >= 0 && x <= FirstSong) {
    			isTouch = true;
    		}
    		if(listener != null){
    			listener.OnLockNotify();
    		}
    		invalidate();
		}
    	*/
    	// if(!idSong.equals(""))
    	if(event.getAction() == MotionEvent.ACTION_UP){
    		isTouch = false;
    		float offsetX = event.getX();
    		float offsetY = event.getY();
    		float offsetTouchX = event.getRawX();
    		float offsetTouchY = event.getRawY();
//    		if (offsetX >= 0 && offsetX <= FirstSong) {
    			if (listener != null) {
	    			int[] location = new int[2];
	    			this.getLocationOnScreen(location);
	    			int ScreenX = location[0] + heightLayout / 2;
	    			int ScreenY = location[1] + heightLayout / 2;
	    			isActive = !isActive;
	    			listener.OnActive(isActive, song, "" + position, ScreenX, ScreenY);
    			}invalidate();	
//    		}
    		
    	}
    	return true;
    }
    
///////////////////////// - CUT TEXT - ////////////////////////////
    
	private String cutText(int maxLength, String content) {
		if(content.length() <= maxLength){
			return content;
		}		
		return content.substring(0, maxLength) + "...";
	}

///////////////////////////////////////////////////////////////////////
	
    private float Line , KT4S;
    private float RT1S , RT1X , RT1Y;
    private float RT2S , RT2X , RT2Y;
    private float RT3S , RT3X , RT3Y;
    private float idS, idX, idY;
    private int RD1 , RD1L , RD1R , RD1T , RD1B;
    private int RD2 , RD2L , RD2R , RD2T , RD2B;
    private int RD3L , RD3R , RD3T , RD3B;
    private int CircleS1 , CircleS2;
    private float CircleX , CircleY , CircleR; 
    private float FirstSong, Favourity , SingerLink;
    private void setK(int w , int h){    	
    	widthLayout = w;
    	heightLayout = h;
    	
    	Line = 150*h/1080;
    	    	
    	CircleS1 = 60*h/1080;
    	CircleS2 = 50*h/1080;
    	CircleX = h/2; 
    	CircleY = h/2; 
    	CircleR = 200*h/1080;
    	
    	RT1S = 400*h/1080;
    	RT1X = heightLayout; 
    	RT1Y = heightLayout/2;
//-EDIT---------------    	
    	
    	idS = (float) (0.25*h);
    	idX = heightLayout;
    	idY = (float) (3.5*heightLayout/6);
    	
    	RT2S = (float) (0.24*h);
    	RT2X = (float) (0.3*widthLayout); 
    	RT2Y = 5*heightLayout/6;
    	
    	
//---------------------  
    	
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
    	
    }
    
//-----------------------------------//
    
    private String idSong;
    private String idSong5;
    
    public String getIdSong() {
		return idSong;
	}

	public void setIdSong(String idSong) {
		this.idSong = idSong;
	}

	private int[]idSinger;
	
    public int[] getIdSinger() {
		return idSinger;
	}

	public void setIdSinger(int[] idSinger) {
		this.idSinger = idSinger;
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

    private String nameSinger;
    
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

    
    
/////////////////////////// - LOAD TEXT - //////////////////////////////////
/*	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		clearData();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		clearData();
	}

	private void clearData() {
		if (loadImage != null) {
			loadImage.cancel(true);
			loadImage = null;
		}
	}

	private LoadImage loadImage;
	private class LoadImage extends AsyncTask<Void, Void, Void> {

		private String song = "";
		private String singer = "";
		private String lyric = "";

		public LoadImage(String song, String singer, String lyric) {
			// MyLog.i(VIEW_LOG_TAG, path);
			this.song = song;
			this.singer = singer;
			this.lyric = lyric;
		}
		

		@Override
		protected Void doInBackground(Void... params) {
			textSong = cutText(RT1S, (float) (0.6*widthLayout), song);
			textSinger = cutText(RT3S, (float) (0.2*widthLayout), singer);
			textLyric = cutText(RT2S, (float) (0.7*widthLayout), lyric);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			invalidate();
		}
	}
*/


	private String TestIndex;
	private Spannable createSpannable(String textSong , String nameraw , String textSearch, int language){
		textSearch.trim();
		ArrayList<Integer> listOffset = new ArrayList<Integer>();
		Spannable wordtoSpan;
		if(textSong.equals("")){
			wordtoSpan = new SpannableString("");
		}else{
			if(textSearch.equals("")){
				return new SpannableString(textSong);
			}
			textSearch.trim();
			String newString = textSong.replaceAll("[ &+=_,-]", "*");
			StringBuffer buffer = new StringBuffer(newString);
				//-------------//
			String[] strings = buffer.toString().split("[*]");
			if(strings.length < textSearch.length()){
				int offset = nameraw.indexOf(textSearch);
				wordtoSpan = new SpannableString(textSong);
				if(offset != -1){
					wordtoSpan.setSpan(new ForegroundColorSpan(Color.GREEN), offset, 
							offset + textSearch.length(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
				return wordtoSpan;
			}
			int count = 0;
 			for (int i = 0; i < strings.length; i++) {
 				int size = strings[i].length();
 				if(size <= 0){
 					count += 1;
 				}else{
 					listOffset.add(count);
 					count += size + 1;
 				}
 				if(listOffset.size() >= textSearch.length()){
 					break;
 				}
			}
 				//-------------//
			wordtoSpan = new SpannableString(textSong);
			if (language == 3) {
				if(!textSearch.equals("")){
					wordtoSpan.setSpan(new ForegroundColorSpan(Color.GREEN), 0, textSong.length(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			} else {
				for (int i = 0; i < listOffset.size(); i++) {
					int offset = getIndex(textSong, listOffset.get(i));
					if(nameraw.charAt(offset) != textSearch.charAt(i)){
						int of = nameraw.indexOf(textSearch);
						SpannableString word = new SpannableString(textSong);
						if(of != -1){
							word.setSpan(new ForegroundColorSpan(Color.GREEN), of, 
									of + textSearch.length(),
									Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						}
						return word;
					}else{
						wordtoSpan.setSpan(new ForegroundColorSpan(Color.GREEN), offset, offset + 1,
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
				}
			}	
		}	
		return wordtoSpan;
	}
	
	private int getIndex(String string , int index){
		switch (string.charAt(index)) {
		case '(': 	return index + 1; 
		case '`': 	return index + 1; 
		case '[': 	return index + 1; 
		default:	return index;
		}
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
	
}
