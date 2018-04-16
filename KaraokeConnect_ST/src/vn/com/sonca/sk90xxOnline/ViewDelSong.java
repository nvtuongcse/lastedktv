package vn.com.sonca.sk90xxOnline;

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
import android.os.Message;
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
	private Drawable drawRemix, drawMidi, drawMV, drawMVVid;
	private Drawable drawFavourite, drawFavouriteNO;
	private Drawable drawActive,drawInActive ,drawHover;
	private Drawable drawFirstXam;
	private Drawable drawChangPerfect;
	
	private Drawable drawCheck;
	private Drawable drawYouTube, drawDownload, drawDownloadStop, drawXemThu;	
	
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
	}
	
	public void setDrawable(Drawable drawActive , Drawable drawInActive , Drawable drawHover,
			Drawable drawFavourite , Drawable drawFavouriteNO,
			Drawable drawSinger , Drawable drawRemix, Drawable drawMidi , 
			Drawable drawMV, Drawable drawUser, Drawable drawMVVid){
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
		this.drawMVVid = drawMVVid;
	}
	
	private int ordinarily = -1;
	public void setOrdinarilyPlaylist(int ordinarily){
		if (ordinarily <= 0) {
			this.ordinarily = -1;
		} else {
			this.ordinarily = ordinarily;
		}
		this.ordinarily = -1;
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
		this.isSinger = song.isVocalSinger();
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
		
		this.textSinger = cutText(14,song.getSingerName());
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
	private Rect rectYouTube, rectYouTube2, rectOnline;
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
		vuongW = vuongH*84/38;
		rectMV = new Rect(
				(int)(offsetX - vuongW), 
				(int)(offsetY - vuongH), 
				(int)(offsetX + vuongW), 
				(int)(offsetY + vuongH));
		
		offsetX = (float) (0.88*w);
		offsetY = (float) (0.79*h);
		if(MyApplication.flagHong){
			offsetX = (float) (0.88*w);
			offsetY = (float) (0.79*h);
		}
		vuongH = (float) (0.11*h);
		vuongW = vuongH*36/26;
		rectYouTube = new Rect(
				(int)(offsetX - vuongW), 
				(int)(offsetY - vuongH), 
				(int)(offsetX + vuongW), 
				(int)(offsetY + vuongH));
		
		offsetX = (float) (0.93*w);
		offsetY = (float) (0.79*h);
		if(MyApplication.flagHong){
			offsetX = (float) (0.93*w);
			offsetY = (float) (0.79*h);
		}
		vuongH = (float) (0.11*h);
		vuongW = vuongH*24/26;
		rectYouTube2 = new Rect(
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
		
		offsetX = (float) (0.82*w);
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
		
		offsetX = (float) (0.21*w);
		offsetY = (float) (0.72*h);
		vuongH = (float) (0.15*h);
		vuongW = vuongH*42/26;
		rectOnline = new Rect(
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
   
    private Drawable drawable;
    
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
		
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE) {
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
			if(song.isYoutubeSong()){
				color_07 = Color.argb(255, 255, 238, 0);
				color_08 = Color.argb(255, 255, 238, 0);
			}
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			
		}

		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE) {
			// DRAW 0

			if (MyApplication.intWifiRemote == MyApplication.SONCA) {
				
				resetPaint();
				canvas.translate(0, 0);
				
				if (isActive) {
					paintSimple.setShader(gradient);
					paintSimple.setStrokeWidth(getHeight());
					canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, paintSimple);			
				}

				// YOUTUBE SONG
				float offsetX = (float) (0.935*widthLayout);
				float offsetY = (float) (0.73*heightLayout);
				float vuongH = (float) (0.22*heightLayout);
				float vuongW = vuongH*50/43;
				rectFavourite = new Rect(
						(int)(offsetX - vuongW), 
						(int)(offsetY - vuongH), 
						(int)(offsetX + vuongW), 
						(int)(offsetY + vuongH));
				
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
					
					// -------------xem truoc---------------------
//					drawable = getResources().getDrawable(R.drawable.youtube_xemtruoc);
//					drawable.setBounds(rectFavourite);
//					drawable.draw(canvas);
					
					resetPaint();
					
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
						canvas.drawText(str, rectYouTube2.right + 2 * widthLayout / 480, ptY, mainText);
						
						drawable = getResources().getDrawable(R.drawable.youtube_icon);
						drawable.setBounds(rectYouTube);
						drawable.draw(canvas);	
					} else {
						stopTimerDownload();
						
						drawable = getResources().getDrawable(R.drawable.youtube_icon);
						drawable.setBounds(rectYouTube);
						drawable.draw(canvas);	
					}	
					
					
					// --------------MIDI vs MV--------------------
					if (!textID.equals("000000 | -")) {
						if (ismedia == MEDIA_TYPE.MIDI) {
							if (drawMidi != null) {
								drawMidi.setBounds(rectMV);
								drawMidi.draw(canvas);
							}
							if (intABC != 0 && (MyApplication.intSvrModel == MyApplication.SONCA || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK)) {
								drawChangPerfect.setBounds(rectChangPerfect);
								drawChangPerfect.draw(canvas);
							}
						} else {
							if (drawMVVid != null) {
								drawMVVid.setBounds(rectMV);
								drawMVVid.draw(canvas);
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
					
					// ---------------online icon----------------------
					drawable = getResources().getDrawable(R.drawable.icon_icloud);
					drawable.setBounds(rectOnline);
					drawable.draw(canvas);	

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
							mainText.setARGB(255, 244, 67, 54);
							builderNumber.clear();
							builderNumber.append(spanNemberID);
							StaticLayout layout0 = new StaticLayout(builderNumber, mainText, widthLayout,
									Alignment.ALIGN_NORMAL, 1, 0, true);
							canvas.translate(-transX + idX, -transY + idY);
							layout0.draw(canvas);
						}
					}					

			} else {

			}
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {

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
    	
    	if(MyApplication.flagAutoDownloading == true){
			return true;
		}
    	
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
    
    private float ptS, ptY;
    
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
    	
    	ptS = 16 * h / 100;
		ptY = 85 * h / 100;
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
