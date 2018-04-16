package vn.com.sonca.Touch.CustomView;

import java.util.Timer;
import java.util.TimerTask;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TouchDanceLinkView extends View {

	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	
	public TouchDanceLinkView(Context context) {
		super(context);
		initView(context);
	}

	public TouchDanceLinkView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchDanceLinkView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Drawable drawAC;
	private Drawable drawNO;
	private Drawable drawbackground;
		
	private Drawable zlightdrawAC;
	private Drawable zlightdrawNO;
	private Drawable zlightdrawbackground;
	
	private String danceNO , danceYES;
	private String dance1 = "";
	private String dance2 = "";
	private String dance3 = "";
		
	private void initView(Context context) {
		drawbackground = getResources().getDrawable(R.drawable.boder_note_1116x399);
		drawAC = getResources().getDrawable(R.drawable.boder_cokhong_active_129x74);
		drawNO = getResources().getDrawable(R.drawable.boder_cokhong_inactive_129x74);
		
		zlightdrawbackground = getResources().getDrawable(R.drawable.zlight_boder_popup);
		zlightdrawAC = getResources().getDrawable(R.drawable.zlight_boder_cokhong_hover_129x74);
		zlightdrawNO = getResources().getDrawable(R.drawable.zlight_boder_cokhong_inactive_129x74);
		
		danceNO = getResources().getString(R.string.dance_yes);
		danceYES = getResources().getString(R.string.dance_no);
		textPaint.setStyle(Style.FILL);
		textPaint.setARGB(255 , 182 , 253 , 255);
	}
	
	private OnStopShowDanceListener listener;
	public interface OnStopShowDanceListener {
		public void OnStop(boolean isActive, boolean layout);
	}
	
	public void setOnStopShowDanceListener(OnStopShowDanceListener listener){
		this.listener = listener;
	}
	
	public static boolean NODANCE = false;
	public static boolean DANCE = true;
	private boolean layout = NODANCE;
	public void setLayout(boolean layout){
		MyApplication.flagDance = layout;
		this.layout = layout;
		if (layout) {
			dance1 = getResources().getString(R.string.dance_view_4);
			dance2 = getResources().getString(R.string.dance_view_5);
			dance3 = getResources().getString(R.string.dance_view_6);
		} else {
			dance1 = getResources().getString(R.string.dance_view_1);
			dance2 = getResources().getString(R.string.dance_view_2);
			dance3 = getResources().getString(R.string.dance_view_3);
		}
	}
	public boolean getLayout(){
		return layout;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
	}
	
	private int widthView;
	private int heightView;
	private int widthScreen;
	private int heightScreen;
	private float textS , textSB;
	private float textX , ty1 , ty2 , ty3;
	private float textBY , textNoX , textYesX;
	private Rect rectNO , rectYES;
	private Rect rectbackground;

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		widthScreen = w;
		heightScreen = h;
		widthView = 1116*w/1920;
		heightView = 399*h/1080;
		
		int wh = (int) (0.25*heightView);
		int wr = 129*wh/74;
		int tamX = (int) (0.4*w);
		int tamY = (int) (0.59*h);
		rectYES = new Rect(tamX - wr/2, tamY - wh/2, tamX + wr/2, tamY + wh/2);
		tamX = (int) (0.6*w);
		rectNO = new Rect(tamX - wr/2, tamY - wh/2, tamX + wr/2, tamY + wh/2);
		
		textS = (float) (0.21*wr);
		if (MyApplication.flagHong) {
			textS = (float) (0.18*wr);
		}
		textPaint.setTextSize(textS);
		textX = (float) (0.25*w);
		ty1 = (float) (0.41*h);
		ty2 = (float) (0.46*h);
		ty3 = (float) (0.51*h);
		rectbackground = new Rect(w/2 - widthView/2, 
				h/2 - heightView/2, w/2 + widthView/2, h/2 + heightView/2);
		
		textSB = (float) (0.08*heightView);
		textBY  = (float) (tamY + textSB/2.5);
		textPaint.setTextSize(textSB);
		float width = textPaint.measureText(danceNO);
		textNoX = (int) ((rectNO.left + rectNO.right)/2 - width/2);
		width = textPaint.measureText(danceYES);
		textYesX = (int) ((rectYES.left + rectYES.right)/2 - width/2);
		
	}
	

	public void ShowDanceLink(){
		CreateTimerBack();
	}
	
	private int color_01;
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		// MyApplication.intColorScreen = MyApplication.SCREEN_LIGHT;
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			color_01 = Color.argb(255, 182, 253, 255);
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			color_01 = Color.parseColor("#005249");
		}
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			textPaint.setColor(color_01);
			if (isShow) {
				int width = getWidth();
				int height = getHeight();
				paintMain.setStyle(Style.FILL);
				paintMain.setARGB(intAphaBlack, 0, 0, 0);
				canvas.drawRect(0, 0, width, height, paintMain);
			}
			drawbackground.setBounds(rectbackground);
			drawbackground.draw(canvas);

			textPaint.setTextSize(textS);
			canvas.drawText(dance1, textX, ty1, textPaint);
			canvas.drawText(dance2, textX, ty2, textPaint);
			canvas.drawText(dance3, textX, ty3, textPaint);

			if (activeNo) {
				drawAC.setBounds(rectNO);
				drawAC.draw(canvas);
			} else {
				drawNO.setBounds(rectNO);
				drawNO.draw(canvas);
			}
			if (activeYes) {
				drawAC.setBounds(rectYES);
				drawAC.draw(canvas);
			} else {
				drawNO.setBounds(rectYES);
				drawNO.draw(canvas);
			}
			textPaint.setTextSize(textSB);
			canvas.drawText(danceNO, textNoX, textBY, textPaint);
			canvas.drawText(danceYES, textYesX, textBY, textPaint);
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {

			textPaint.setColor(color_01);
			if (isShow) {
				int width = getWidth();
				int height = getHeight();
				paintMain.setStyle(Style.FILL);
				paintMain.setARGB(intAphaBlack, 0, 0, 0);
				canvas.drawRect(0, 0, width, height, paintMain);
			}
			zlightdrawbackground.setBounds(rectbackground);
			zlightdrawbackground.draw(canvas);

			textPaint.setTextSize(textS);
			canvas.drawText(dance1, textX, ty1, textPaint);
			canvas.drawText(dance2, textX, ty2, textPaint);
			canvas.drawText(dance3, textX, ty3, textPaint);

			if (activeNo) {
				zlightdrawAC.setBounds(rectNO);
				zlightdrawAC.draw(canvas);
			} else {
				zlightdrawNO.setBounds(rectNO);
				zlightdrawNO.draw(canvas);
			}
			if (activeYes) {
				zlightdrawAC.setBounds(rectYES);
				zlightdrawAC.draw(canvas);
			} else {
				zlightdrawNO.setBounds(rectYES);
				zlightdrawNO.draw(canvas);
			}
			textPaint.setTextSize(textSB);
			canvas.drawText(danceNO, textNoX, textBY, textPaint);
			canvas.drawText(danceYES, textYesX, textBY, textPaint);

		}
	}
	
	private boolean boolListener = false;
	private boolean activeNo = false;
	private boolean activeYes = false;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			if(rectbackground != null){
				float x = event.getX();
				float y = event.getY();
				if(x>=rectbackground.left && x<widthScreen/2 &&
					y>=heightScreen/2 && y<= rectbackground.bottom){
					activeNo = false;
					activeYes = true;
				}
				if(x>widthScreen/2 && x<=rectbackground.right && 
					y>=heightScreen/2 && y<= rectbackground.bottom){
					activeNo = true;
					activeYes = false;
					boolListener = true;
				}
				invalidate();
			}
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			activeNo = false;
			activeYes = false;
			HideSingerLink();
		}
		return true;
	}
	
	private boolean isShow = false;
	private int intAphaBlack = 0;
	private Timer timerBack = null;
	private void CreateTimerBack(){
		StopTimerBack();
		isShow = true;
		intAphaBlack = 0;
		activeNo = false;
		activeYes = false;
		boolListener = false;
		timerBack = new Timer();
		timerBack.schedule(new TimerTask() {
			@Override public void run() {
				intAphaBlack += 10;
				if(intAphaBlack > 150){
					intAphaBlack = 150;
					handlerTimerBack.sendEmptyMessage(1);
				}else{
					handlerTimerBack.sendEmptyMessage(0);
				}
			}
		}, 10, 10);
	}
	private void HideSingerLink(){
		StopTimerBack();
		isShow = true;
		timerBack = new Timer();
		timerBack.schedule(new TimerTask() {
			@Override public void run() {
				intAphaBlack -= 10;
				if(intAphaBlack < 0){
					intAphaBlack = 0;
					timerBack.cancel();
					handlerTimerBack.removeMessages(2);
					handlerTimerBack.sendEmptyMessage(2);
				}else{
					handlerTimerBack.sendEmptyMessage(0);
				}
			}
		}, 10, 10);
	}
	private void StopTimerBack(){
		if(timerBack != null){
			timerBack.cancel();
			timerBack = null;
		}
	}
	private Handler handlerTimerBack = new Handler(){
		public void handleMessage(Message msg) {
			if(msg.what == 0){
				invalidate();
			}
			if(msg.what == 1){
				StopTimerBack();
			}
			if(msg.what == 2){
				if(listener != null){
					StopTimerBack();
					invalidate();
					if(listener != null){
						listener.OnStop(boolListener , layout);
					}
				}
			}
		};
	};

}
