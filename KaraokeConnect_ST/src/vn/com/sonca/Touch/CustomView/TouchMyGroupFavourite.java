package vn.com.sonca.Touch.CustomView;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.params.Song;
import vn.com.sonca.utils.AppConfig.MEDIA_TYPE;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TouchMyGroupFavourite extends View {
	
	private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint mainText = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int widthLayout = 400;
    private int heightLayout = 400;
	private Drawable dFavourite;
	private Drawable dSinger;
	
	private String textSong = "Tam biet mua ha";
	private String textLyric = "Mot hai ba bon man sau bay tam chin muoi";
	private String textSinger = "Anh Khanh, Truong Chi";
	
	private int position;
	private boolean isActive;
	private boolean isFavourity;
	private boolean isRemix;
	private boolean isSinger;
	private MEDIA_TYPE ismedia;
	
	public TouchMyGroupFavourite(Context context) {
		super(context);
		initView(context);
	}

	public TouchMyGroupFavourite(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchMyGroupFavourite(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private OnGroupFavouriteListener listener;
	public interface OnGroupFavouriteListener {
		public void OnSingerLink(boolean bool , String name, int[] idSinger);
		public void OnFristRes(boolean bool, Song song, int position, float x , float y);
		public void OnFavourity(boolean bool , Song song);
		public void OnActive(boolean bool , Song song , String id , float x , float y);
	}
	
	public void setOnGroupFavouriteListener(OnGroupFavouriteListener listener){
		this.listener = listener;
	}

	private String yeuthich;
	private String uutien;
	private Drawable drawSinger;
	private Drawable drawDelete; 
	private Drawable drawActive,drawInActive;
	private Drawable drawRemix, drawMidi, drawMV;
	private Drawable drawFavourite, drawFavouriteNO;
	private void initView(Context context) {
		uutien = getResources().getString(R.string.uutien);
		yeuthich = getResources().getString(R.string.type_favourite);
		drawActive = getResources().getDrawable(R.drawable.touch_image_1st_cham);
		drawInActive = getResources().getDrawable(R.drawable.touch_image_1st);
		drawFavourite = getResources().getDrawable(R.drawable.touch_image_favourite_active_45x45);
		drawFavouriteNO = getResources().getDrawable(R.drawable.touch_image_favourite_45x45);
		drawSinger = getResources().getDrawable(R.drawable.touch_song_vocal_48x48);
		drawRemix = getResources().getDrawable(R.drawable.touch_image_remix_62x35);
		drawMidi = getResources().getDrawable(R.drawable.new_midi);
		drawMV = getResources().getDrawable(R.drawable.touch_ktv_50x30);
		song = new Song();
		isActive = false;
	}
	
	private Song song;
	public void setContentView(int position , Song song){
		this.textSong = cutText(30, song.getName());
		this.textSinger = cutText(14, song.getSinger().getName());
		this.textLyric = cutText(40, song.getLyric());
		this.position = position;
		this.isActive = song.isActive();
		this.isFavourity = song.isFavourite();
		this.isRemix = song.isRemix();
		this.isSinger = song.isMediaSinger();
		this.ismedia = song.getMediaType();
		this.song = song;
	}
	
	private int ordinarily = -1;
	public void setOrdinarilyPlaylist(int ordinarily){
		this.ordinarily = ordinarily;
	}
	
	private Typeface typeface;
	public void setTypeface(Typeface typeface){
		this.typeface = typeface;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	    int myHeight = (int) (0.6188*getResources().getDisplayMetrics().heightPixels/5);
	    setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(myHeight, MeasureSpec.UNSPECIFIED));
	}
	
	private Rect rectMV;
	private Rect rectMIDI;
	private Rect rectSinger;
	private Rect rectRemix;
	private Rect rectFavourite;
	private float ytX , ytY , ytS;
	private float utX , utY , utS;
	private float otX , otY , otS;
    @Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		setK(w,h);
		
		utS = (float) (0.11*h);
		mainText.setTextSize(utS);
		utX = CircleX - mainText.measureText(uutien)/2;
		utY = (float) (0.93*h);
		
		float offsetX = (float) (0.965*w);
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
		offsetX = (float) (0.9*w);
		offsetY = (float) (0.75*h);
		float vuongH = (float) (0.15*h);
		float vuongW = vuongH*50/35;
		rectMIDI = new Rect(
				(int)(offsetX - vuongW), 
				(int)(offsetY - vuongH), 
				(int)(offsetX + vuongW), 
				(int)(offsetY + vuongH));
		vuongW = vuongH*50/30;
		rectMV = new Rect(
				(int)(offsetX - vuongW), 
				(int)(offsetY - vuongH), 
				(int)(offsetX + vuongW), 
				(int)(offsetY + vuongH));
		offsetX = (float) (0.85*w);
		vuong = (float) (0.2*h);
		rectSinger = new Rect(
				(int)(offsetX - vuong), 
				(int)(offsetY - vuong), 
				(int)(offsetX + vuong), 
				(int)(offsetY + vuong));
		offsetX = (float) (0.79*w);
		offsetY = (float) (0.75*h);
		vuongH = (float) (0.15*h);
		vuongW = vuongH*96/48;
		rectRemix = new Rect(
				(int)(offsetX - vuongW), 
				(int)(offsetY - vuongH), 
				(int)(offsetX + vuongW), 
				(int)(offsetY + vuongH));
		
		otX = (float) (3);
		otY = (float) (0.9*h);
		otS = (float) (0.2*h);
		
    }
    @Override
    protected void onDraw(Canvas canvas) {
    	super.onDraw(canvas);
    	resetPaint();
		
    	mainPaint.setStyle(Style.STROKE);
    	mainPaint.setStrokeWidth((float) 1.0);
		
		Path path = new Path();
		path.moveTo(widthLayout , 0);
		path.lineTo(widthLayout, heightLayout);
		path.lineTo(0 , heightLayout);
		path.lineTo(0, 0);
		// if(position == 0){
			path.lineTo(widthLayout , 0);
		// }
			
		if (isActive) {
			mainPaint.setARGB(255, 180, 254, 255);
		} else {
			mainPaint.setARGB(255, 0, 78, 144);
		}
		canvas.drawPath(path, mainPaint);
//---------------------------------------
		path = new Path();
		path.moveTo(0 , 0 + Line);
		path.lineTo(0, 0);
		// if(position == 0){
			path.lineTo(0 + Line , 0);
		// }
		path.moveTo(widthLayout , 0 + Line);
		path.lineTo(widthLayout , 0);
		// if(position == 0){
			path.lineTo(widthLayout - Line , 0);
		// }
		path.moveTo(widthLayout , heightLayout - Line);
		path.lineTo(widthLayout, heightLayout);
		path.lineTo(widthLayout - Line , heightLayout);
		path.moveTo(0 , heightLayout - Line);
		path.lineTo(0 , heightLayout);
		path.lineTo(0 + Line , heightLayout);
		mainPaint.setARGB(255 , 0 , 253 , 253);
		canvas.drawPath(path, mainPaint);
//---------------------------------------
		float FR = (float) (2*CircleR);
		if (isFirst) {
			drawActive.setBounds((int)(CircleX - FR), 
					(int)(CircleX - FR), (int)(CircleX + FR), (int)(CircleX + FR));
			drawActive.draw(canvas);
		} else {
			drawInActive.setBounds((int)(CircleX - FR), 
					(int)(CircleX - FR), (int)(CircleX + FR), (int)(CircleX + FR));
			drawInActive.draw(canvas);
		}
		mainText.setTextSize(utS);
		mainText.setColor(Color.CYAN);
		canvas.drawText(uutien, utX, utY, mainText);
		
//----------------------------------		
		mainText.setStyle(Style.FILL);
		mainText.setTextSize(RT1S);
				
		if (isActive) {
			mainText.setARGB(255, 0, 254, 255);
			canvas.drawText(textSong , RT1X , RT1Y , mainText);
		} else {
			mainText.setARGB(255 , 182 , 253 , 255);
			canvas.drawText(textSong , RT1X , RT1Y , mainText);
		}
		
		mainText.setStyle(Style.FILL);
		mainText.setTextSize(RT3S);
		mainText.setARGB(255 , 1 , 165 , 254);
		float width = mainText.measureText(textSinger);
		canvas.drawText(textSinger , RT3X - width , RT3Y , mainText);
		
		mainText.setStyle(Style.FILL);
		mainText.setTextSize(RT2S);
		if (isActive) {
			mainText.setARGB(255 , 114 , 172 , 185);
		} else {
			mainText.setARGB(255 , 1 , 100 , 131);
		}
		mainText.setTypeface(typeface);
		if (textLyric == null) {
			canvas.drawText("" , RT2X , RT2Y , mainText);
		} else {
			canvas.drawText(textLyric , RT2X , RT2Y , mainText);
		}
		
//-------------isFavourity---------------------	
		
		resetPaint();
		if(isFavourity){
			drawFavourite.setBounds(rectFavourite);
			drawFavourite.draw(canvas);
		}else{
			drawFavouriteNO.setBounds(rectFavourite);
			drawFavouriteNO.draw(canvas);
		}
		mainText.setStyle(Style.FILL);
		mainText.setTextSize(ytS);
		mainText.setColor(Color.CYAN);
		canvas.drawText(yeuthich, ytX, ytY, mainText);
		
//--------------MIDI vs MV--------------------	
		if(ismedia == MEDIA_TYPE.MIDI){
			drawMidi.setBounds(rectMIDI);
			drawMidi.draw(canvas);
		}
		if(ismedia == MEDIA_TYPE.VIDEO){
			drawMV.setBounds(rectMV);
			drawMV.draw(canvas);
		}
//---------------isSinger-------------------	
		if(isSinger){
			drawSinger.setBounds(rectSinger);
			drawSinger.draw(canvas);
		}
//---------------isRemix-------------------	
		if(isRemix){
			drawRemix.setBounds(rectRemix);
			drawRemix.draw(canvas);
		}

// ---------------ordinarily-------------------

		if (ordinarily != -1) {
			mainText.setStyle(Style.FILL);
			mainText.setTextSize(otS);
			mainText.setColor(Color.GREEN);
			canvas.drawText(String.valueOf(ordinarily), otX, otY, mainText);
		}
		
    }
    
    private boolean isFirst;
    @Override
    public void setPressed(boolean pressed) {
    	super.setPressed(pressed);
		setClickable(pressed);
		if (offsetX >= 0 && offsetX <= FirstSong) {
			isFirst = pressed;
			if (pressed) {
				if (listener != null) {
					int[] location = new int[2];
					this.getLocationOnScreen(location);
					int ScreenX = location[0] + heightLayout / 2;
					int ScreenY = location[1] + heightLayout / 2;
					listener.OnFristRes(isFirst, song, position, ScreenX, ScreenY);
				}
			}invalidate();	
		}
		if (offsetX > FirstSong && offsetX < Favourity) {
			if (pressed) {
				if (isActive == false) {
					// KHIEM - isActive = true;
				}
				if (listener != null) {
					int[] location = new int[2];
					this.getLocationOnScreen(location);
					int ScreenX = location[0] + heightLayout / 2;
					int ScreenY = location[1] + heightLayout / 2;
					listener.OnActive(isActive, song, idSong , ScreenX, ScreenY);
				}invalidate();	
			}
		}
		if (offsetX >= Favourity && offsetX <= widthLayout && 
				offsetY > heightLayout/2 && offsetY <= heightLayout) {
				if (pressed) {
					isFavourity = !isFavourity;
					if (listener != null) {
						listener.OnFavourity(isFavourity, song);
					}invalidate();	
				}
			}
		if (offsetX >= Favourity && offsetX <= widthLayout && 
			offsetY >= 0 && offsetY <= heightLayout / 2) {
			if (pressed) {
				if (listener != null) {
					listener.OnSingerLink(true, nameSinger, idSinger);
				}
				invalidate();
			}
		}
    }

    
    private float offsetX = -1;
    private float offsetY = -1;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	super.onTouchEvent(event);
    	if (event.getAction() == MotionEvent.ACTION_DOWN) {
			offsetX = event.getX();
			offsetY = event.getY();
		}
    	return false;
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
    private float FirstSong, Favourity;
    private void setK(int w , int h){    	
    	widthLayout = w - 1;
    	heightLayout = h;
    	FirstSong = heightLayout;
    	Favourity = (float) (widthLayout - 0.25*widthLayout);
 
    	Line = 150*h/1080;
    	
    	CircleS1 = 60*h/1080;
    	CircleS2 = 50*h/1080;
    	CircleX = h/2; 
    	CircleY = h/2; 
    	CircleR = 200*h/1080;
    	
    	RT1S = 400*h/1080;
    	RT1X = heightLayout; 
    	RT1Y = heightLayout/2;
    	
    	RT2S = 270*h/1080;
    	RT2X = heightLayout; 
    	RT2Y = 5*heightLayout/6;
    	
    	RT3S = 350*h/1080;
    	RT3X = 1900*w/1920; 
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
    
//-----------------------------------//
    
    private String idSong;
    private int[] idSinger;
    private String nameSinger;
    
    public String getIdSong() {
		return idSong;
	}

	public void setIdSong(String idSong) {
		this.idSong = idSong;
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

	

}
