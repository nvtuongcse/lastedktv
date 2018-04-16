package vn.com.sonca.Touch.CustomView;

import java.util.Timer;
import java.util.TimerTask;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class TouchSettingView extends View{
	
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	private OnTouchSettingViewListener listener;
	public interface OnTouchSettingViewListener {
		public void OnBackSetting();
		public void OnChangeHello();
		public void OnChangePass();
		public void OnExitApp();
	}
	
	public void setOnTouchSettingViewListener(OnTouchSettingViewListener listener){
		this.listener = listener;
	}
	
	public TouchSettingView(Context context) {
		super(context);
		initView(context);
	}

	public TouchSettingView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchSettingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Drawable drawBack;
	private Drawable drawSetting;
	private Drawable drawPassIN, drawPassHO;
	private Drawable drawExitIN, drawExitHO;
	private Drawable drawChaoIN, drawChaoAC, drawChaoHO;
	
	private String textLabel = "";
	private String textDong1;
	private String textDong2;
	private String textDong3;
	private String textDong4;
	private String textDong5;

	private Drawable zlightdrawBack;
	private Drawable zlightdrawSetting;
	private Drawable zlightdrawPassIN, zlightdrawPassHO;
	private Drawable zlightdrawExitIN, zlightdrawExitHO;
	private Drawable zlightdrawChaoIN, zlightdrawChaoAC, zlightdrawChaoHO;
	
	private void initView(Context context) {
		drawBack = getResources().getDrawable(R.drawable.connect_back_48x48);
		drawSetting = getResources().getDrawable(R.drawable.touch_icon_setting);
		drawPassIN = getResources().getDrawable(R.drawable.touch_icon_caidatpassapp);
		drawPassHO = getResources().getDrawable(R.drawable.touch_icon_caidatpassapp_hover);
		drawExitIN = getResources().getDrawable(R.drawable.touch_icon_thoatapp);
		drawExitHO = getResources().getDrawable(R.drawable.touch_icon_thoatapp_hover);
		drawChaoIN = getResources().getDrawable(R.drawable.touch_icon_manhinh_cauchao_xam);
		drawChaoHO = getResources().getDrawable(R.drawable.touch_icon_manhinh_cauchao_hover);
		drawChaoAC = getResources().getDrawable(R.drawable.touch_icon_manhinh_cauchao);
		
		textLabel = getResources().getString(R.string.setting_1);
		textDong1 = getResources().getString(R.string.setting_2);
		textDong2 = getResources().getString(R.string.setting_3);
		textDong3 = getResources().getString(R.string.setting_4);
		textDong4 = getResources().getString(R.string.setting_5);
		textDong5 = getResources().getString(R.string.setting_6);
		
		zlightdrawBack = getResources().getDrawable(R.drawable.connect_back_48x48);
		zlightdrawSetting = getResources().getDrawable(R.drawable.touch_icon_setting);
		zlightdrawPassIN = getResources().getDrawable(R.drawable.touch_icon_caidatpassapp);
		zlightdrawPassHO = getResources().getDrawable(R.drawable.touch_icon_caidatpassapp_hover);
		zlightdrawExitIN = getResources().getDrawable(R.drawable.touch_icon_thoatapp);
		zlightdrawExitHO = getResources().getDrawable(R.drawable.touch_icon_thoatapp_hover);
		zlightdrawChaoIN = getResources().getDrawable(R.drawable.touch_icon_manhinh_cauchao_xam);
		zlightdrawChaoHO = getResources().getDrawable(R.drawable.touch_icon_manhinh_cauchao_hover);
		zlightdrawChaoAC = getResources().getDrawable(R.drawable.touch_icon_manhinh_cauchao);
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int myWidth = 700 * heightMeasureSpec / 1180;
		setMeasuredDimension(myWidth, heightMeasureSpec);
	}
	
	private int widthLayout;
	private float graWidth;
	private float graTopY;
	private float graBottomY;
	private LinearGradient gradient;
	private Rect rectLabel = new Rect();
	private Rect rectBack = new Rect(); 
	private Rect rectLine1 = new Rect();
	private Rect rectLine2 = new Rect();
	private Rect rectSetting = new Rect();
	private Rect rectChao = new Rect();
	private Rect rectPass = new Rect();
	private Rect rectExit = new Rect();
	private float labelX, labelY, labelS;
	private float dong1X, dong1Y, dong1S; 
	private float dong2X, dong2Y, dong2S;
	private float dong3X, dong3Y, dong3S;
	private float dong4X, dong4Y, dong4S;
	private float dong5X, dong5Y, dong5S;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		widthLayout = w;
		graWidth = (float) (0.005 * h);
		graTopY = graWidth / 2;
		graBottomY = (int) (0.09 * h - graWidth / 2);
		gradient = new LinearGradient(0, 0, w / 2, 0, Color.TRANSPARENT,
				Color.CYAN, Shader.TileMode.MIRROR);
		rectLabel.set(0, 0, w, (int) (0.09 * h));
		
		int tx = (int) (0.06 * w);
		int ty = (int) (0.046 * h);
		int wr = (int) (0.08 * w);
		int hr = wr;
		rectBack.set(tx - wr, ty - hr, tx + wr, ty + hr);
		tx = (int) (0.9 * w);
		ty = (int) (0.046 * h);
		wr = (int) (0.05 * w);
		hr = wr;
		rectSetting.set(tx - wr, ty - hr, tx + wr, ty + hr);
		tx = (int) (0.1 * w);
		ty = (int) (0.19 * h);
		wr = (int) (0.07 * w);
		hr = 50*wr/68;
		rectChao.set(tx - wr, ty - hr, tx + wr, ty + hr);
		tx = (int) (0.1 * w);
		ty = (int) (0.34 * h);
		wr = (int) (0.07 * w);
		hr = 64*wr/69;
		rectPass.set(tx - wr, ty - hr, tx + wr, ty + hr);
		tx = (int) (0.1 * w);
		ty = (int) (0.44 * h);
		wr = (int) (0.06 * w);
		hr = 61*wr/63;
		rectExit.set(tx - wr, ty - hr, tx + wr, ty + hr);
		
		labelS = (float) (0.04 * h);
		textPaint.setTextSize(labelS);
		float size = textPaint.measureText(textLabel);
		labelX = (float) (0.13*w);
		labelY = (float) (0.06 * h);
		
		tx = (int) (0.5 * w);
		wr = (int) (0.5*widthLayout);
		hr = (int) (0.05*h);
		rectLine1.set(tx - wr, rectBack.bottom, tx + wr, rectBack.bottom + hr);
		
		ty = (int) (0.26 * h);
		hr = (int) (0.53*hr);
		rectLine2.set(tx - wr, ty - hr, tx + wr, ty + hr);
		
		dong1S = (float) (0.03*h);
		dong1X = (float) (0.025*w);
		dong1Y = (float) (0.126*h);
		
		dong3S = dong1S;
		dong3X = dong1X;
		dong3Y = (float) (0.27*h);
		
		dong2S = (float) (0.035*h);
		dong2X = (float) (0.2*w);
		dong2Y = (float) (0.2*h);
		
		dong4S = dong2S;
		dong4X = dong2X;
		dong4Y = (float) (0.35*h);
		
		dong5S = dong2S;
		dong5X = dong2X;
		dong5Y = (float) (0.45*h);
		
	}
	
	private int color_01;
	private int color_02;
	private int color_03;
	private int color_04;
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		//MyApplication.intColorScreen = MyApplication.SCREEN_LIGHT;
		
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			color_01 = Color.argb(180, 36, 110, 160);
			color_02 = Color.argb(255, 1, 45, 82);
			color_03 = Color.argb(255, 180, 254, 255);
			color_04 = Color.CYAN;
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			color_01 = Color.argb(180, 36, 110, 160);
			color_02 = Color.argb(255, 1, 45, 82);
			color_03 = Color.argb(255, 180, 254, 255);
			color_04 = Color.CYAN;
		}
		
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){

			mainPaint.setStyle(Style.FILL);
			mainPaint.setColor(color_01);
			canvas.drawRect(rectLabel, mainPaint);

			mainPaint.setShader(gradient);
			mainPaint.setStrokeWidth(graWidth);
			canvas.drawLine(0, graTopY, widthLayout, graTopY, mainPaint);
			canvas.drawLine(0, graBottomY, widthLayout, graBottomY, mainPaint);
			mainPaint.setShader(null);

			paint.setStyle(Style.FILL);
			paint.setColor(color_02);
			canvas.drawRect(rectLine1, paint);
			canvas.drawRect(rectLine2, paint);

			textPaint.setStyle(Style.FILL);
			textPaint.setTextSize(labelS);
			textPaint.setColor(color_03);
			canvas.drawText(textLabel, labelX, labelY, textPaint);

			textPaint.setStyle(Style.FILL);
			textPaint.setTextSize(dong1S);
			textPaint.setColor(color_04);
			canvas.drawText(textDong1, dong1X, dong1Y, textPaint);
			textPaint.setTextSize(dong3S);
			canvas.drawText(textDong3, dong3X, dong3Y, textPaint);

			textPaint.setStyle(Style.FILL);
			textPaint.setTextSize(dong2S);
			textPaint.setColor(color_03);
			canvas.drawText(textDong2, dong2X, dong2Y, textPaint);
			textPaint.setTextSize(dong4S);
			canvas.drawText(textDong4, dong4X, dong4Y, textPaint);
			textPaint.setTextSize(dong5S);
			canvas.drawText(textDong5, dong5X, dong5Y, textPaint);

			drawSetting.setBounds(rectSetting);
			drawSetting.draw(canvas);

			drawBack.setBounds(rectBack);
			drawBack.draw(canvas);

			if (stateHover == HELLO) {
				drawChaoHO.setBounds(rectChao);
				drawChaoHO.draw(canvas);
			} else {
				if (isHelloActive) {
					drawChaoAC.setBounds(rectChao);
					drawChaoAC.draw(canvas);
				} else {
					drawChaoIN.setBounds(rectChao);
					drawChaoIN.draw(canvas);
				}
			}

			if (stateHover == PASSW) {
				drawPassHO.setBounds(rectPass);
				drawPassHO.draw(canvas);
			} else {
				drawPassIN.setBounds(rectPass);
				drawPassIN.draw(canvas);
			}

			if (stateHover == EXIT) {
				drawExitHO.setBounds(rectExit);
				drawExitHO.draw(canvas);
			} else {
				drawExitIN.setBounds(rectExit);
				drawExitIN.draw(canvas);
			}

		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){

			mainPaint.setStyle(Style.FILL);
			mainPaint.setColor(color_01);
			canvas.drawRect(rectLabel, mainPaint);

			mainPaint.setShader(gradient);
			mainPaint.setStrokeWidth(graWidth);
			canvas.drawLine(0, graTopY, widthLayout, graTopY, mainPaint);
			canvas.drawLine(0, graBottomY, widthLayout, graBottomY, mainPaint);
			mainPaint.setShader(null);

			paint.setStyle(Style.FILL);
			paint.setColor(color_02);
			canvas.drawRect(rectLine1, paint);
			canvas.drawRect(rectLine2, paint);

			textPaint.setStyle(Style.FILL);
			textPaint.setTextSize(labelS);
			textPaint.setColor(color_03);
			canvas.drawText(textLabel, labelX, labelY, textPaint);

			textPaint.setStyle(Style.FILL);
			textPaint.setTextSize(dong1S);
			textPaint.setColor(color_04);
			canvas.drawText(textDong1, dong1X, dong1Y, textPaint);
			textPaint.setTextSize(dong3S);
			canvas.drawText(textDong3, dong3X, dong3Y, textPaint);

			textPaint.setStyle(Style.FILL);
			textPaint.setTextSize(dong2S);
			textPaint.setColor(color_03);
			canvas.drawText(textDong2, dong2X, dong2Y, textPaint);
			textPaint.setTextSize(dong4S);
			canvas.drawText(textDong4, dong4X, dong4Y, textPaint);
			textPaint.setTextSize(dong5S);
			canvas.drawText(textDong5, dong5X, dong5Y, textPaint);

			drawSetting.setBounds(rectSetting);
			drawSetting.draw(canvas);

			drawBack.setBounds(rectBack);
			drawBack.draw(canvas);

			if (stateHover == HELLO) {
				drawChaoHO.setBounds(rectChao);
				drawChaoHO.draw(canvas);
			} else {
				if (isHelloActive) {
					drawChaoAC.setBounds(rectChao);
					drawChaoAC.draw(canvas);
				} else {
					drawChaoIN.setBounds(rectChao);
					drawChaoIN.draw(canvas);
				}
			}

			if (stateHover == PASSW) {
				drawPassHO.setBounds(rectPass);
				drawPassHO.draw(canvas);
			} else {
				drawPassIN.setBounds(rectPass);
				drawPassIN.draw(canvas);
			}

			if (stateHover == EXIT) {
				drawExitHO.setBounds(rectExit);
				drawExitHO.draw(canvas);
			} else {
				drawExitIN.setBounds(rectExit);
				drawExitIN.draw(canvas);
			}

		}

	}
	
	public static final int HELLO = 1;
	public static final int PASSW = 2;
	public static final int EXIT = 3;
	private int stateHover = 0;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:{
			float x = event.getX();
			float y = event.getY();
			if(rectBack != null){
				if(x >= rectBack.left && x <= rectBack.right && 
					y >= rectBack.top && y <= rectBack.bottom){
					if(listener != null){
						listener.OnBackSetting();
					}
				}
			}
			if(rectChao != null){
				if(x >= rectChao.left && x <= rectChao.right && 
					y >= rectChao.top && y <= rectChao.bottom){
					stateHover = HELLO;
				}
			}
			if(rectPass != null){
				if(x >= rectPass.left && x <= rectPass.right && 
					y >= rectPass.top && y <= rectPass.bottom){
					stateHover = PASSW;
				}
			}
			if(rectExit != null){
				if(x >= rectExit.left && x <= rectExit.right &&
					y >= rectExit.top && y <= rectExit.bottom){
					stateHover = EXIT;
				}
			}
			invalidate();
		}break;
		
		case MotionEvent.ACTION_UP:{
			if(listener != null){
				if(stateHover == HELLO && isHelloActive){
					listener.OnChangeHello();
				}
				if(stateHover == PASSW){
					listener.OnChangePass();
				}
				if(stateHover == EXIT){
					listener.OnExitApp();
				}
			}
			stateHover = 0;
			invalidate();
		}break;
			
		case MotionEvent.ACTION_MOVE:{
			executeDelay500();
		}break;
		default:break;
		}
		return true;
	}
	
/////////////////////////////////////////////////
	
	private boolean isHelloActive = false;
	public void setHelloActive(boolean active){
		isHelloActive = active;
		invalidate();
	}
	
	private Timer timerDelay500 = null;
	private void removeDelay500(){
		if(timerDelay500 != null){
			timerDelay500.cancel();
			timerDelay500 = null;
		}
	}
	
	public void executeDelay500(){
		removeDelay500();
		timerDelay500 = new Timer();
		timerDelay500.schedule(new TimerTask() {
			@Override public void run() {
				handlerDelay500.sendEmptyMessage(0);
			}
		}, 500);
	}
	
	private Handler handlerDelay500 = new Handler(){
		public void handleMessage(android.os.Message msg) {
			stateHover = 0;
			invalidate();
		};
	};

}
