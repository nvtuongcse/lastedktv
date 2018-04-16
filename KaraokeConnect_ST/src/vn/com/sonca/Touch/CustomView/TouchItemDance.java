package vn.com.sonca.Touch.CustomView;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.params.Song;
import vn.com.sonca.utils.AppConfig.MEDIA_TYPE;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;

public class TouchItemDance extends View {
	
	private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private TextPaint mainText = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	
	public TouchItemDance(Context context) {
		super(context);
		initView(context);
	}

	public TouchItemDance(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchItemDance(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private OnGroupDanceListener listener;
	public interface OnGroupDanceListener {
		public void OnFristRes(int position , Song song);
		public void OnActive(Song song);
	}
	
	public void setOnGroupDanceListener(OnGroupDanceListener listener){
		this.listener = listener;
	}

	private String uutien;
	private String updown;
	private Drawable drawMove;
	private Drawable drawMoveXam;
	private Drawable drawActive, drawInActive;
	private Drawable drawFirstXam;
	
	private Drawable zlightdrawMove;
	private Drawable zlightdrawMoveXam;
	private Drawable zlightdrawActive, zlightdrawInActive;
	private Drawable zlightdrawFirstXam;
	
	private void initView(Context context) {
		uutien = getResources().getString(R.string.uutien);
		updown = getResources().getString(R.string.updown);
		drawMove = getResources().getDrawable(R.drawable.touch_image_updown_70x100);
		drawMoveXam = getResources().getDrawable(R.drawable.touch_image_updown_xam);
		drawActive = getResources().getDrawable(R.drawable.ydark_image_1st_cham);
		drawInActive = getResources().getDrawable(R.drawable.ydark_image_1st_inactive);
		drawFirstXam = getResources().getDrawable(R.drawable.touch_ydark_image_1st_xam);
		
		zlightdrawMove = getResources().getDrawable(R.drawable.zlight_image_updown_70x100);
		zlightdrawMoveXam = getResources().getDrawable(R.drawable.zlight_image_updown_70x100_xam);
		zlightdrawActive = getResources().getDrawable(R.drawable.zlight_ydark_image_1st_inactive);
		zlightdrawInActive = getResources().getDrawable(R.drawable.zlight_ydark_image_1st_inactive);
		zlightdrawFirstXam = getResources().getDrawable(R.drawable.ydark_image_1st_xam);
		
	}
	
	private Song song;
	private int position;
	private String textSong = "";
	public void setContentView(int position , Song song){
		this.textSong = song.getSpannable().toString();
		isFirst = isActive = false;
		this.position = position;
		this.song = song;
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
	
	private float Line;
    private int widthLayout = 0;
    private int heightLayout = 0;
    private float FirstSong;
	private float utX , utY , utS;
	private float CircleR , CircleX;
	private Path pathGoc = new Path();
	private Path pathKhung = new Path();
	
	private Rect rectMove;
	private float udX , udY , udS;
	private float RT1S , RT1X , RT1Y;
    @Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		widthLayout = w;
    	heightLayout = h;
    	FirstSong = (float) (1.5*h);
    	Line = 150*h/1080;
    	CircleR = 200*h/1080;
    	CircleX = h/2; 
    	
    	utS = (float) (0.11*h);
		mainText.setTextSize(utS);
		utX = CircleX - mainText.measureText(uutien)/2;
		utY = (float) (0.93*h);
    	
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
		
		offsetY = (float) (0.5*h);
		float vuongW = (float) (0.055*w);
		float vuongH = (float) (0.4*h);
		rectMove = new Rect(
				(int)(w - 0.01*w - vuongW), 
				(int)(offsetY - vuongH), 
				(int)(w - 0.01*w), 
				(int)(offsetY + vuongH));
		udS = (float) (0.1*h);
		udX = (float) (0.94*w);
		udY = (float) (0.95*h);
		
		RT1S = 500*h/1080;
    	RT1X = heightLayout; 
    	RT1Y = (float) (0.65*heightLayout);
		
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
    
    @Override
    protected void onDraw(Canvas canvas) {
    	super.onDraw(canvas);
    	/*
    	isActive = false;
    	MyApplication.intColorScreen = MyApplication.SCREEN_LIGHT;
    	*/
    	if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
	    	color_01 = Color.argb(50, 0, 0, 0);
	    	color_02 = Color.argb(255, 0, 78, 144);
	    	color_03 = Color.argb(255, 0, 253, 253);
	    	color_04 = Color.CYAN;
	    	color_05 = Color.GRAY;
	    	color_06 = Color.argb(255, 0, 254, 255);
	    	color_07 = Color.argb(255, 182, 253, 255);
    	} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
    		color_01 = Color.parseColor("#FFFFFF");
	    	color_02 = Color.parseColor("#E6E7E8");
	    	color_03 = Color.parseColor("#C1FFE8");
	    	color_04 = Color.parseColor("#10B008");
	    	color_05 = Color.GRAY;
	    	color_06 = Color.parseColor("#000000");
	    	color_07 = Color.parseColor("#000000");
    	}
    	
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			resetPaint();
			if (isActive) {
				mainPaint.setStyle(Style.FILL);
				mainPaint.setColor(color_01);
				canvas.drawRect(0, 0, widthLayout, heightLayout, mainPaint);
			}
			mainPaint.setStyle(Style.STROKE);
			mainPaint.setStrokeWidth((float) 1.0);
			mainPaint.setColor(color_02);
			canvas.drawPath(pathKhung, mainPaint);
			mainPaint.setColor(color_03);
			canvas.drawPath(pathGoc, mainPaint);
			resetPaint();
			float FR = (float) (2 * CircleR);
			if (MyApplication.bOffFirst == true) {
				if (isFirst) {
					drawActive.setBounds((int) (CircleX - FR), (int) (CircleX - FR), (int) (CircleX + FR),
							(int) (CircleX + FR));
					drawActive.draw(canvas);
				} else {
					drawInActive.setBounds((int) (CircleX - FR), (int) (CircleX - FR), (int) (CircleX + FR),
							(int) (CircleX + FR));
					drawInActive.draw(canvas);
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
			canvas.drawText(uutien, utX, utY, mainText);
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
			mainText.setStyle(Style.FILL);
			mainText.setTextSize(RT1S);
			if (isActive) {
				mainText.setColor(color_06);
				canvas.drawText(textSong, RT1X, RT1Y, mainText);
			} else {
				mainText.setColor(color_07);
				canvas.drawText(textSong, RT1X, RT1Y, mainText);
			}
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			resetPaint();
			if (isActive) {
				mainPaint.setStyle(Style.FILL);
				mainPaint.setColor(color_01);
				canvas.drawRect(0, 0, widthLayout, heightLayout, mainPaint);
			}else{
				mainPaint.setStyle(Style.FILL);
				mainPaint.setColor(color_02);
				canvas.drawRect(0, 0, widthLayout, heightLayout, mainPaint);
			}
			mainPaint.setStyle(Style.STROKE);
			mainPaint.setColor(color_03);
			canvas.drawRect(0, 0, widthLayout, heightLayout, mainPaint);
			resetPaint();
			float FR = (float) (2 * CircleR);
			if (MyApplication.bOffFirst == true) {
				if (isFirst) {
					zlightdrawActive.setBounds((int) (CircleX - FR), (int) (CircleX - FR), (int) (CircleX + FR),
							(int) (CircleX + FR));
					zlightdrawActive.draw(canvas);
				} else {
					zlightdrawInActive.setBounds((int) (CircleX - FR), (int) (CircleX - FR), (int) (CircleX + FR),
							(int) (CircleX + FR));
					zlightdrawInActive.draw(canvas);
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
			if (MyApplication.bOffFirst == true) {
				if (MyApplication.isMoveList == false) {
					zlightdrawMoveXam.setBounds(rectMove);
					zlightdrawMoveXam.draw(canvas);
				} else {
					zlightdrawMove.setBounds(rectMove);
					zlightdrawMove.draw(canvas);
				}
			} else {
				zlightdrawMoveXam.setBounds(rectMove);
				zlightdrawMoveXam.draw(canvas);
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
			mainText.setStyle(Style.FILL);
			mainText.setTextSize(RT1S);
			if (isActive) {
				mainText.setAlpha(222);
				mainText.setColor(color_06);
				canvas.drawText(textSong, RT1X, RT1Y, mainText);
				mainText.setAlpha(255);
			} else {
				mainText.setAlpha(222);
				mainText.setColor(color_07);
				canvas.drawText(textSong, RT1X, RT1Y, mainText);
				mainText.setAlpha(255);
			}
		}
		
    }
    
    private boolean isFirst;
    private boolean isTouch;
    private boolean isActive = false;
    @Override
    public void setPressed(boolean pressed) {
    	super.setPressed(pressed);
    	setClickable(pressed);
    	isFirst = isActive = pressed;
    	if (offsetX >= 0 && offsetX <= widthLayout) {
			if (pressed) {
				if (listener != null) {
					listener.OnFristRes(position , song);
				}
			}invalidate();	
		}
		invalidate();	
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

///////////////////////////////////////////////////////////////////////
	    
    private String idSong;
    public String getIdSong() {
		return idSong;
	}
	public void setIdSong(String idSong) {
		this.idSong = idSong;
		invalidate();
	}
    

}
