package vn.com.sonca.Touch.CustomView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

public class TouchAnimationView extends View {
	
	private Paint paintRectBorder = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintRect = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintText = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public static int NOTHING = 0;
	public static int DOWNLOAD = 1;
	public static int WIFI = 2;
	public static int PASS = 3;
	public static int SUCCESS = 4;
	public static int ON_USER = 5;
	public static int OFF_USER = 6;
	public static int ER_PASS_USER = 7;
	public static int OFF_USER_SUCCESS = 8;
	public static int ON_USER_SUCCESS = 9;
	private int state = NOTHING;
	
	private final boolean RUNNING = true;
	private final boolean DONE = false;
	private boolean STATE = DONE;
	
	private float TX0 , TY0 , TX1 , TY1 , TX2 , TY2;
	private float left, right, top, bottom;
	private float padding_left, padding_right, padding_top, padding_bottom;
	private float KD , KA , KB;
	private float X0 , Y0;

	private Path pathLine = new Path();
	private ArrayList<Text> listTexts = new ArrayList<Text>();
	
	public TouchAnimationView(Context context) {
		super(context);
		initView(context);
	}

	public TouchAnimationView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchAnimationView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private Drawable drawFrist;
	private Drawable drawSong;
	private Drawable drawShow;
	private float centerX = 80;
	private float centerY = 80;
	private float widthView = 0;
	
	private Drawable zlightBackgroup;
	private Drawable zlightdrawFrist;
	private Drawable zlightdrawSong;
	
	private String textDownload= "Dang tai du lieu";
	private String textWait = "Xin doi trong giay lat";
	private String textConectError = "Khong the ket noi";
	private String textConectError_01 = "Khong the ket noi";
	private String textSK9000 = "Xin ket noi lai";
	private String textLine0 = "";
	private String textLine1 = "";
	
	private Drawable zgreen_drawFirst, zgreen_drawSong;
	
	private void initView(Context context) {
		this.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		textDownload = getResources().getString(R.string.animview_download);
		textWait = getResources().getString(R.string.animview_wait);
		textConectError = getResources().getString(R.string.animview_connect_error);
		textConectError_01 = getResources().getString(R.string.animview_connect_error_1);
		textSK9000 = getResources().getString(R.string.animview_connect_sk);
		
		drawFrist = getResources().getDrawable(R.drawable.ydark_image_1st_cham);
		drawSong = getResources().getDrawable(R.drawable.touch_icon_not_nhac_1);
		
		zlightBackgroup = getResources().getDrawable(R.drawable.zlight_boder_popup);
		zlightdrawFrist = getResources().getDrawable(R.drawable.zlight_ydark_image_1st_inactive);
		zlightdrawSong = getResources().getDrawable(R.drawable.zlight_icon_not_nhac_1);
				
		paintText.setStyle(Style.FILL);
		paintText.setARGB(255 , 0 , 253 , 253);
		
		paintRect.setStyle(Style.FILL);
		paintRect.setARGB(240 , 27 , 74 , 116);
		
		paintRectBorder.setStyle(Style.STROKE);
		paintRectBorder.setARGB(240 , 27 , 74 , 116);
		
		paintLine.setStyle(Style.STROKE);
		paintLine.setARGB(255 , 0 , 253 , 253);

	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		widthView = w;
		
		if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			KD = 60*h/1080; 
			X0 = (float) (0.6*w);
			Y0 = (float) (0.91*h);
			left = w/2 - w/6;
			right = w/2 + w/6;
			float wr = 2*(right - left)/4;
			top = h/2 - wr/2;
			bottom = h/2 + wr/2;

			float line = (float) (0.1*wr);
			paintLine.setStrokeWidth((float) (0.02*wr));
			pathLine = new Path();
			pathLine.moveTo(left, top + line);
			pathLine.lineTo(left, top);
			pathLine.lineTo(left + line, top);
			pathLine.moveTo(right - line, top);
			pathLine.lineTo(right, top);
			pathLine.lineTo(right, top + line);
			pathLine.moveTo(right, bottom - line);
			pathLine.lineTo(right, bottom);
			pathLine.lineTo(right - line, bottom);
			pathLine.moveTo(left + line, bottom);
			pathLine.lineTo(left, bottom);
			pathLine.lineTo(left, bottom - line);
			
			float textSize = (float) (0.1*(right-left));
			paintText.setTextSize(textSize);
			TY0 = (float) (0.45*h + textSize/2.5);
			TY1 = (float) (0.55*h + textSize/2.5);
		} else {
			KD = 80*h/1080; 
			X0 = 1855*w/1920;
			Y0 = 53*h/1080;
			left = w/2 - w/6;
			right = w/2 + w/6;
			float wr = 2*(right - left)/4;
			top = h/2 - wr/2;
			bottom = h/2 + wr/2;

			float line = (float) (0.1*wr);
			paintLine.setStrokeWidth((float) (0.02*wr));
			pathLine = new Path();
			pathLine.moveTo(left, top + line);
			pathLine.lineTo(left, top);
			pathLine.lineTo(left + line, top);
			pathLine.moveTo(right - line, top);
			pathLine.lineTo(right, top);
			pathLine.lineTo(right, top + line);
			pathLine.moveTo(right, bottom - line);
			pathLine.lineTo(right, bottom);
			pathLine.lineTo(right - line, bottom);
			pathLine.moveTo(left + line, bottom);
			pathLine.lineTo(left, bottom);
			pathLine.lineTo(left, bottom - line);
			
			float textSize = (float) (0.1*(right-left));
			paintText.setTextSize(textSize);
			TY0 = (float) (0.45*h + textSize/2.5);
			TY1 = (float) (0.55*h + textSize/2.5);
		}
		
		// ShowMessage(WIFI);
		
	}
	
	public void ShowMessage(int state){
		if(state == NOTHING){
			this.state = NOTHING;
		}else if(state == DOWNLOAD){
			this.state = DOWNLOAD;
			textLine0 = textDownload;
			textLine1 = textWait;
		}else if(state == WIFI){
			this.state = WIFI;
			textLine0 = textConectError;
			textLine1 = textSK9000;
		}else if(state == PASS){
			this.state = PASS;
			textLine0 = textConectError_01;
			textLine1 = textSK9000;
		}
		invalidate();
	}
	
	public void showAdminUser(int state){
		this.state = state;
		if(state == ON_USER){
			textLine0 = getResources().getString(R.string.adminfrag_4a);
			textLine1 = getResources().getString(R.string.adminfrag_5);
		}else if (state == OFF_USER){
			textLine0 = getResources().getString(R.string.adminfrag_4b);
			textLine1 = getResources().getString(R.string.adminfrag_5);
		}else if(state == ON_USER_SUCCESS){
			textLine0 = getResources().getString(R.string.adminfrag_4a);
			textLine1 = getResources().getString(R.string.adminfrag_5a);
		} else if (state == OFF_USER_SUCCESS){
			textLine0 = getResources().getString(R.string.adminfrag_4b);
			textLine1 = getResources().getString(R.string.adminfrag_5a);
		} else if(state == ER_PASS_USER){
			textLine0 = getResources().getString(R.string.adminfrag_6);
			textLine1 = getResources().getString(R.string.adminfrag_7);
		}
		invalidate();
	}
	
	public void showSuccessConnect(String daumay){
		this.state = SUCCESS;
		textLine0 = getResources().getString(R.string.conect_success);
		textLine1 = getResources().getString(R.string.conect_success_0) + " " + daumay;
		invalidate();
	}

	private Handler handlerView = new Handler(){
		public void handleMessage(android.os.Message msg) {
			invalidate();
		};
	};
	
	public static final boolean FRIST = false;
	public static final boolean SONG = true;
	
	private float tang = 0;
	private Timer timerAnimation = new Timer();
	public void startAnimation(float x , float y , boolean type){
		if (type == FRIST){
			if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
				drawShow = drawFrist;
			} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
				drawShow = zlightdrawFrist;
			}
		}			
		if (type == SONG) {
			if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
				drawShow = drawSong;
			} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
				drawShow = zlightdrawSong;
			}	
		}
		
		if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			KA = (Y0 - y)/(X0 - x);
			KB = Y0 - X0*KA;
			centerX = x;
			tang = 0;
			
			if(centerX > X0){
				
				if(true){ // if(centerX - X0 > 100){
					timerAnimation = new Timer();
					timerAnimation.schedule(new TimerTask() {
						
						@Override
						public void run() {
							STATE = RUNNING;
							centerX = centerX - tang;
							centerY = KA*centerX + KB;
							if(centerX <= X0){
								STATE = DONE;
								cancel();
							}
							handlerView.sendEmptyMessage(0);
							tang = (float) (tang + 0.2);
							if(tang >= 10){
								tang = 10;
							}
						}
					}, 8, 8);	
				} else { // bay qua nhanh - tang len
					timerAnimation = new Timer();
					timerAnimation.schedule(new TimerTask() {
						
						@Override
						public void run() {
							STATE = RUNNING;
							centerX = centerX - tang;
							centerY = KA*centerX + KB;
							if(centerX <= X0){
								STATE = DONE;
								cancel();
							}
							handlerView.sendEmptyMessage(0);
							tang = (float) (tang + 0.2);
							if(tang >= 10){
								tang = 10;
							}
						}
					}, 20, 20);	
				}
				
			} else {
				
				if(true){ // X0 - centerX > 100
					timerAnimation = new Timer();
					timerAnimation.schedule(new TimerTask() {
						
						@Override
						public void run() {
							STATE = RUNNING;
							centerX = centerX + tang;
							centerY = KA*centerX + KB;
							if(centerX >= X0){
								STATE = DONE;
								cancel();
							}
							handlerView.sendEmptyMessage(0);
							tang = (float) (tang + 0.2);
							if(tang >= 10){
								tang = 10;
							}
						}
					}, 8, 8);	
				} else { // bay qua nhanh - tang len
					timerAnimation = new Timer();
					timerAnimation.schedule(new TimerTask() {
						
						@Override
						public void run() {
							STATE = RUNNING;
							centerX = centerX + tang;
							centerY = KA*centerX + KB;
							if(centerX >= X0){
								STATE = DONE;
								cancel();
							}
							handlerView.sendEmptyMessage(0);
							tang = (float) (tang + 0.2);
							if(tang >= 10){
								tang = 10;
							}
						}
					}, 20, 20);
				}
					
				
			}			
				
		} else {
			KA = (Y0 - y)/(X0 - x);
			KB = Y0 - X0*KA;
			centerX = x;
			tang = 0;
			timerAnimation = new Timer();
			timerAnimation.schedule(new TimerTask() {
				
				@Override
				public void run() {
					STATE = RUNNING;
					centerX = centerX + tang;
					centerY = KA*centerX + KB;
					if(centerX >= X0){
						STATE = DONE;
						cancel();
					}
					handlerView.sendEmptyMessage(0);
					tang = (float) (tang + 0.4);
					if(tang >= 13){
						tang = 13;
					}
				}
			}, 8, 8);	
			
		}		
				
	}
	
	private Timer timerWifi = new Timer();
	private void ClearShowWifi(){
		if(timerWifi != null){
			timerWifi.cancel();
			timerWifi = null;
		}
		timerWifi = new Timer();
		timerWifi.schedule(new TimerTask() {
			@Override public void run() {
				state = NOTHING;
				handlerWifi.sendEmptyMessage(0);
			}
		}, 3500);
	}
	
	private Handler handlerWifi = new Handler(){
		public void handleMessage(android.os.Message msg) {
			invalidate();
		};
	};
	
	public void stopAnimation(){
		if(timerAnimation != null){
			STATE = DONE;
			timerAnimation.cancel();
			timerAnimation = null;
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		/*
		state = NOTHING + 1;
		MyApplication.intColorScreen = MyApplication.SCREEN_LIGHT;
		*/		
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			paintText.setARGB(255 , 0 , 253 , 253);		
			paintRect.setARGB(240 , 27 , 74 , 116);		
			paintLine.setARGB(255 , 0 , 253 , 253);
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			paintText.setColor(Color.parseColor("#005249"));	
			paintRect.setARGB(240 , 148 , 196 , 133);		
			paintLine.setColor(Color.parseColor("#005249"));
			paintRectBorder.setARGB(255 , 255 , 255 , 255);
		}		
		
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			if(STATE == RUNNING){
				drawShow.setBounds((int) (centerX - KD/2) , (int) (centerY - KD/2) , 
						(int) (centerX + KD/2) , (int) (centerY + KD/2));
				drawShow.draw(canvas);
			}	
			if(state != NOTHING){
				float width = paintText.measureText(textLine0);
				TX0 = widthView/2 - width/2;
				width = paintText.measureText(textLine1);
				TX1 = widthView/2 - width/2;
				canvas.drawRect(left, top, right, bottom, paintRect);
				if (MyApplication.intColorScreen == MyApplication.SCREEN_GREEN) {
					canvas.drawRect(left, top, right, bottom, paintRectBorder);	
				}			
				
				float textSize = 0;
				if(state == SUCCESS){
					textSize = (float) (0.08*(right-left));
					paintText.setTextSize(textSize);
				} else {
					textSize = (float) (0.1*(right-left));
					paintText.setTextSize(textSize);
				}
				float w = paintText.measureText(textLine0);
				TX0 = widthView/2 - w/2;
				w = paintText.measureText(textLine1);
				TX1 = widthView/2 - w/2;
				canvas.drawPath(pathLine, paintLine);
				canvas.drawText(textLine0, TX0, TY0, paintText);
				canvas.drawText(textLine1, TX1, TY1, paintText);
				if (state == WIFI || state == PASS || state == SUCCESS
						|| state == ON_USER || state == OFF_USER
						|| state == ON_USER_SUCCESS
						|| state == OFF_USER_SUCCESS || state == ER_PASS_USER) {
					ClearShowWifi();
				}
			}
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){

			if(STATE == RUNNING){
				drawShow.setBounds((int) (centerX - KD/2) , (int) (centerY - KD/2) , 
						(int) (centerX + KD/2) , (int) (centerY + KD/2));
				drawShow.draw(canvas);
			}	
			if(state != NOTHING){
				float width = paintText.measureText(textLine0);
				TX0 = widthView/2 - width/2;
				width = paintText.measureText(textLine1);
				TX1 = widthView/2 - width/2;
				canvas.drawRect(left, top, right, bottom, paintRect);
				
				zlightBackgroup.setBounds((int)left, (int)top, (int)right, (int)bottom);
				zlightBackgroup.draw(canvas);
				
				float textSize = 0;
				if(state == SUCCESS){
					textSize = (float) (0.08*(right-left));
					paintText.setTextSize(textSize);
				} else {
					textSize = (float) (0.1*(right-left));
					paintText.setTextSize(textSize);
				}
				float w = paintText.measureText(textLine0);
				TX0 = widthView/2 - w/2;
				w = paintText.measureText(textLine1);
				TX1 = widthView/2 - w/2;
				canvas.drawText(textLine0, TX0, TY0, paintText);
				canvas.drawText(textLine1, TX1, TY1, paintText);
				if (state == WIFI || state == PASS || state == SUCCESS
						|| state == ON_USER || state == OFF_USER
						|| state == ON_USER_SUCCESS
						|| state == OFF_USER_SUCCESS || state == ER_PASS_USER) {
					ClearShowWifi();
				}
			}
		
		}
	}
	
	
	class Text {
		private float offsetX;
		private float offsetY;
		private String Message;
		
		public float getOffsetX() {
			return offsetX;
		}
		public void setOffsetX(float offsetX) {
			this.offsetX = offsetX;
		}
		public float getOffsetY() {
			return offsetY;
		}
		public void setOffsetY(float offsetY) {
			this.offsetY = offsetY;
		}
		public String getMessage() {
			return Message;
		}
		public void setMessage(String message) {
			Message = message;
		}
	}
	
}
