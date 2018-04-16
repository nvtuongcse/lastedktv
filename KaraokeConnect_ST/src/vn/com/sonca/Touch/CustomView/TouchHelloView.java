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
import android.graphics.Shader;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.graphics.Rect;
import android.os.Handler;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TouchHelloView extends View {
	
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	private OnTouchHelloViewListener listener;
	public interface OnTouchHelloViewListener{
		public void OnBackListener();
		public void OnCancelListener();
		public void OnChangeListener();
		public void OnShowhideListener(boolean show);
	}
	
	public void setOnTouchHelloViewListener (OnTouchHelloViewListener listener){
		this.listener = listener;
	}
	
	public TouchHelloView(Context context) {
		super(context);
		initView(context);
	}

	public TouchHelloView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchHelloView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Drawable drawBack;
	private Drawable drawDevice;
	private Drawable drawButIn;
	private Drawable drawButtonAC, drawButtonIN;
	private Drawable drawDongAC, drawDongIN;
	
	private Drawable drawAnIn, drawAnAc;
	private Drawable drawHienIn, drawHienAc;
	
	private Drawable zlightdrawBack;
	private Drawable zlightdrawDevice;
	private Drawable zlightdrawButIn;
	private Drawable zlightdrawButtonAC, zlightdrawButtonIN;
	private Drawable zlightdrawDongAC, zlightdrawDongIN;
	private Drawable zlightdrawAnIn, zlightdrawAnAc;
	private Drawable zlightdrawHienIn, zlightdrawHienAc;

	private String textLabel = "";
	private String textNameDevice = "";
	private String textAnhien = "";
	private String cauChaoDong1;
	private String cauChaoDong2;
	private String textLeft;
	private String textRight;
	private void initView(Context context) {
		drawBack = getResources().getDrawable(R.drawable.connect_back_48x48);
		drawDevice = getResources().getDrawable(R.drawable.touch_daumay_chuaketnoi_56x50);
		
		drawAnIn = getResources().getDrawable(R.drawable.touch_cauchao_an);
		drawAnAc = getResources().getDrawable(R.drawable.touch_cauchao_an_hover);
		drawHienIn = getResources().getDrawable(R.drawable.touch_cauchao_hien);
		drawHienAc = getResources().getDrawable(R.drawable.touch_cauchao_hien_hover);
		
		drawButtonAC = getResources().getDrawable(R.drawable.boder_lammoi_hover);
		drawButtonIN = getResources().getDrawable(R.drawable.boder_lammoi);
		drawButIn = getResources().getDrawable(R.drawable.touch_boder_ketnoi_inactive);

		drawDongAC = getResources().getDrawable(R.drawable.touch_boder_ip_active);
		drawDongIN = getResources().getDrawable(R.drawable.touch_boder_ip_inactive);
		
			//------------//
		
		zlightdrawBack = getResources().getDrawable(R.drawable.zlight_connect_back_48x48);
		zlightdrawDevice = getResources().getDrawable(R.drawable.zlight_image_daumay_inactive_71x65);
		zlightdrawAnIn = getResources().getDrawable(R.drawable.zlight_cauchao_an);
		zlightdrawAnAc = getResources().getDrawable(R.drawable.zlight_cauchao_an);
		zlightdrawHienIn = getResources().getDrawable(R.drawable.zlight_cauchao_hien);
		zlightdrawHienAc = getResources().getDrawable(R.drawable.zlight_cauchao_hien);
		zlightdrawButtonAC = getResources().getDrawable(R.drawable.zlight_boder_ketnoi_hover);
		zlightdrawButtonIN = getResources().getDrawable(R.drawable.zlight_boder_ketnoi);
		zlightdrawButIn = getResources().getDrawable(R.drawable.zlight_boder_ketnoi_xam);
		zlightdrawDongAC = getResources().getDrawable(R.drawable.zlight_boder_nhapip_hover);
		zlightdrawDongIN = getResources().getDrawable(R.drawable.zlight_boder_nhapip_active);		
		
			//------------//
		
		textLabel = getResources().getString(R.string.hello_1);
		textAnhien = getResources().getString(R.string.hello_2);
		cauChaoDong1 = getResources().getString(R.string.hello_3);
		cauChaoDong2 = getResources().getString(R.string.hello_4);
		textLeft = getResources().getString(R.string.hello_5);
		textRight = getResources().getString(R.string.hello_6);
		
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
	private LinearGradient gradientLight;
	private Rect rectLabel = new Rect();
	private Rect rectBack = new Rect(); 
	private Rect rectDevice = new Rect();
	private Rect rectLine = new Rect();
	private Rect rectAnHien = new Rect();
	private Rect rectCau1 = new Rect();
	private Rect rectCau2 = new Rect();
	private Rect rectButLeft = new Rect();
	private Rect rectButRight = new Rect();
	private float labelX, labelY, labelS;
	private float deviceX, deviceY, deviceS;
	private float anHienX, anHienY, anHienS;
	private float cauChao_1X, cauChao_1Y, cauChao_1S;
	private float cauChao_2X, cauChao_2Y, cauChao_2S;
	private float leftX, leftY, leftS;
	private float rightX, rightY, rightS;
	private int ShowTop, ShowBottom, ShowMiddle, ShowLeft;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		widthLayout = w;
		
		graWidth = (float) (0.005 * h);
		graTopY = graWidth / 2;
		graBottomY = (int) (0.09 * h - graWidth / 2);
		gradient = new LinearGradient(0, 0, w / 2, 0, Color.TRANSPARENT,
				Color.CYAN, Shader.TileMode.MIRROR);
		gradientLight = new LinearGradient(0, 0, w / 2, 0, Color.WHITE,
				Color.parseColor("#FFF200"), Shader.TileMode.MIRROR);		
		
		rectLabel.set(0, 0, w, (int) (0.09 * h));
		
		labelS = (float) (0.04 * h);
		textPaint.setTextSize(labelS);
		float size = textPaint.measureText(textLabel);
		labelX = (float) (0.13*w);
		labelY = (float) (0.06 * h);
		
		int tx = (int) (0.06 * w);
		int ty = (int) (0.046 * h);
		int wr = (int) (0.08 * w);
		int hr = wr;
		rectBack.set(tx - wr, ty - hr, tx + wr, ty + hr);
		
		tx = (int) (0.88 * w);
		ty = (int) (0.04 * h);
		wr = (int) (0.04 * w);
		hr = wr;
		rectDevice.set(tx - wr, ty - hr, tx + wr, ty + hr);
		
		deviceS = (float) (0.018 * h);
		textPaint.setTextSize(deviceS);
		deviceX = tx - textPaint.measureText(textNameDevice)/2;
		deviceY = (int) (0.08 * h);
		
		ty = (int) (0.17 * h);
		hr = (int) (0.01 * h);
		rectLine.set(0, ty - hr, w, ty + hr);
		
		anHienS = (float) (0.035 * h);
		anHienX = (float) (0.02 * w);
		anHienY = (float) (0.14*h);
		
		tx = (int) (0.88 * w);
		ty = (int) (0.13 * h);
		wr = (int) (0.07 * w);
		hr = 37*wr/107;
		rectAnHien.set(tx - wr, ty - hr, tx + wr, ty + hr);
		
		cauChao_1S = (float) (0.035 * h);
		cauChao_1X = (float) (0.02 * w);
		cauChao_1Y = (float) (0.22*h);
		
		tx = (int) (0.5 * w);
		ty = (int) (0.265 * h);
		hr = (int) (0.058*w);
		rectCau1.set((int) cauChao_1X, ty - hr, (int) (w - cauChao_1X), ty + hr);
		
		cauChao_2S = cauChao_1S;
		cauChao_2X = cauChao_1X;
		cauChao_2Y = (float) (0.34*h);
		
		ty = (int) (0.385 * h);
		rectCau2.set((int) cauChao_2X, ty - hr, (int) (w - cauChao_2X), ty + hr);
		
		tx = (int) (0.3 * w);
		ty = (int) (0.46 * h);
		wr = (int) (0.15 * w);
		hr = (int) (0.36*wr);
		leftS = (float) (0.03 * h);
		textPaint.setTextSize(leftS);
		leftX = tx - textPaint.measureText(textLeft)/2;
		leftY = (float) (ty + 0.35*leftS);
		rectButLeft.set(tx - wr, ty - hr, tx + wr, ty + hr);
		
		tx = (int) (0.7 * w);
		rightS = leftS;
		rightX = tx - textPaint.measureText(textRight)/2;
		rightY = leftY;
		rectButRight.set(tx - wr, ty - hr, tx + wr, ty + hr);
		
		ShowTop = rectLabel.bottom;
		ShowBottom = rectLine.top;
		ShowLeft = (int) (0.76*widthLayout);
		ShowMiddle = (ShowLeft + widthLayout)/2;
	}
	
	private int color_01;
	private int color_02;
	private int color_03;
	private int color_04;
	private int color_black;
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
//		MyApplication.intColorScreen = MyApplication.SCREEN_LIGHT;
		
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			color_01 = Color.argb(180, 36, 110, 160);
			color_02 = Color.argb(255, 180, 254, 255);
			color_03 = Color.argb(255, 1, 45, 82);
			color_04 = Color.CYAN;
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			color_01 = Color.parseColor("#21BAA9");
			color_02 = Color.parseColor("#FFFFFF");
			color_03 = Color.parseColor("#64949F");
			color_04 = Color.parseColor("#21BAA9");
			color_black = Color.parseColor("#005249");
		}
			
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				// DRAW 0
				mainPaint.setStyle(Style.FILL);
				mainPaint.setColor(color_01);
				canvas.drawRect(rectLabel, mainPaint);
				
				textPaint.setStyle(Style.FILL);
				textPaint.setTextSize(labelS);
				textPaint.setColor(color_02);
				canvas.drawText(textLabel, labelX, labelY, textPaint);
				
				textPaint.setTextSize(deviceS);
				textPaint.setColor(Color.GREEN);
				canvas.drawText(textNameDevice, deviceX, deviceY, textPaint);
				
				textPaint.setTextSize(anHienS);
				textPaint.setColor(color_02);
				canvas.drawText(textAnhien, anHienX, anHienY, textPaint);
				
				textPaint.setTextSize(cauChao_1S);
				textPaint.setColor(color_02);
				canvas.drawText(cauChaoDong1, cauChao_1X, cauChao_1Y, textPaint);
				
				textPaint.setTextSize(cauChao_2S);
				textPaint.setColor(color_02);
				canvas.drawText(cauChaoDong2, cauChao_2X, cauChao_2Y, textPaint);
				
				paint.setStyle(Style.FILL);
				paint.setColor(color_03);
				canvas.drawRect(rectLine, paint);
		
				if(intdong == DONG_1){
					drawDongAC.setBounds(rectCau1);
					drawDongAC.draw(canvas);
				}else{
					drawDongIN.setBounds(rectCau1);
					drawDongIN.draw(canvas);
				}
				
				if(intdong == DONG_2){
					drawDongAC.setBounds(rectCau2);
					drawDongAC.draw(canvas);
				}else{
					drawDongIN.setBounds(rectCau2);
					drawDongIN.draw(canvas);
				}
			
				drawBack.setBounds(rectBack);
				drawBack.draw(canvas);
				
				if(MyApplication.intSvrModel == MyApplication.SONCA_HIW){
					drawDevice = getResources().getDrawable(R.drawable.icon_ktvwwifi_connect);
				} else if(MyApplication.intSvrModel == MyApplication.SONCA_KARTROL){
					drawDevice = getResources().getDrawable(R.drawable.touch_icon_model_active);
				} else if(MyApplication.intSvrModel == MyApplication.SONCA_KM1){
					drawDevice = getResources().getDrawable(R.drawable.icon_model_km1_connect);
				} else if(MyApplication.intSvrModel == MyApplication.SONCA_KM2){
					drawDevice = getResources().getDrawable(R.drawable.icon_model_km2_connect);
				} else if(MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM){
					drawDevice = getResources().getDrawable(R.drawable.icon_kb_oem_active);
				} else if(MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI){
					drawDevice = getResources().getDrawable(R.drawable.icon_km1wifi_active);
				} else if(MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI){
					drawDevice = getResources().getDrawable(R.drawable.icon_kb39c_active);
				} else if(MyApplication.intSvrModel == MyApplication.SONCA_KBX9){
					drawDevice = getResources().getDrawable(R.drawable.daumay_kb_active);
				} else if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
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
					drawDevice.setBounds(rectDevice);
					drawDevice.draw(canvas);
					
					drawDevice = getResources().getDrawable(R.drawable.wifi_9108_4);
				} else if(MyApplication.intSvrModel == MyApplication.SONCA_TBT){
					drawDevice = getResources().getDrawable(R.drawable.daumay_tbt);
					drawDevice.setBounds(rectDevice);
					drawDevice.draw(canvas);
					
					drawDevice = getResources().getDrawable(R.drawable.ktvwifi_4);
				}
				drawDevice.setBounds(rectDevice);
				drawDevice.draw(canvas);
			
				if (isShowClick) {
					if (isShowActive) {
						drawHienAc.setBounds(rectAnHien);
						drawHienAc.draw(canvas);
					} else {
						drawAnAc.setBounds(rectAnHien);
						drawAnAc.draw(canvas);
					}
				} else {
					if (isShowActive) {
						drawHienIn.setBounds(rectAnHien);
						drawHienIn.draw(canvas);
					} else {
						drawAnIn.setBounds(rectAnHien);
						drawAnIn.draw(canvas);
					}
				}
				
				if (Button == Button2) {
					drawButtonAC.setBounds(rectButLeft);
					drawButtonAC.draw(canvas);
				} else {
					drawButtonIN.setBounds(rectButLeft);
					drawButtonIN.draw(canvas);
				}
				
				if (isEnableClickChange && isShowActive) {
					if (Button == Button3) {
						drawButtonAC.setBounds(rectButRight);
						drawButtonAC.draw(canvas);
					} else {
						drawButtonIN.setBounds(rectButRight);
						drawButtonIN.draw(canvas);
					}
				} else {
					drawButIn.setBounds(rectButRight);
					drawButIn.draw(canvas);
				}
				
				textPaint.setTextSize(leftS);
				textPaint.setColor(color_04);
				canvas.drawText(textLeft, leftX, leftY, textPaint);
				
				textPaint.setTextSize(rightS);
				textPaint.setColor(color_04);
				canvas.drawText(textRight, rightX, rightY, textPaint);
				
				mainPaint.setShader(gradient);
				mainPaint.setStrokeWidth(graWidth);
				canvas.drawLine(0, graTopY, widthLayout, graTopY, mainPaint);
				canvas.drawLine(0, graBottomY, widthLayout, graBottomY, mainPaint);
				mainPaint.setShader(null);
		
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {

		}
	}
	
	private int Button1 = 1;
	private int Button2 = 2;
	private int Button3 = 3;
	private int Button = 0;
	private boolean isShowActive = true;
	private boolean isShowClick = false;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			float x = event.getX();
			float y = event.getY();
			if(rectBack != null){
				if(x >= rectBack.left && x <= rectBack.right &&
					y >= rectBack.top && y <= rectBack.bottom){
					Button = Button1;
				}
			}
			if(rectButLeft != null){
				if(x >= rectButLeft.left && x <= rectButLeft.right &&
					y >= rectButLeft.top && y<= rectButLeft.bottom){
					Button = Button2;
				}	
			}
			if(rectButRight != null){
				if(x >= rectButRight.left && x <= rectButRight.right && 
					y >= rectButRight.top && y <= rectButRight.bottom){
					Button = Button3;
				}
			}
			if(x >= ShowLeft && x <= widthLayout &&
				y >= ShowTop && y <= ShowBottom){
				isShowClick = true;
			}
			invalidate();
		}break;

		case MotionEvent.ACTION_UP:{
			if(Button == Button1){
				if(listener != null){
					listener.OnBackListener();
				}
			}
			if(Button == Button2){
				if(listener != null){
					listener.OnCancelListener();
				}
			}
			if(Button == Button3){
				if(listener != null && isEnableClickChange && isShowActive){
					listener.OnChangeListener();
				}
			}
			if(isShowClick == true){
				float x = event.getX();
				float y = event.getY();
				if(x >= ShowLeft && x <= ShowMiddle && 
					y >= ShowTop && y <= ShowBottom){
					isShowActive = false;
					if(listener != null){
						listener.OnShowhideListener(isShowActive);
					}
				}
				if(x >= ShowMiddle && x <= widthLayout && 
					y >= ShowTop && y <= ShowBottom){
					isShowActive = true;
					if(listener != null){
						listener.OnShowhideListener(isShowActive);
					}
				}
			}
			isShowClick = false;
			Button = 0;
			invalidate();
		}break;
		
		case MotionEvent.ACTION_MOVE:{
			executeDelay500();
		}break;
		default:
			break;
		}
		return true;
		
	}
	
/////////////////////////////////////////
	
	public static int DONG_1 = 1;
	public static int DONG_2 = 2;
	private int intdong = 0;
	public void setDongFocus(int dong){
		intdong = dong;
		invalidate();
	}
	
	public void setShowHide(boolean show){
		isShowActive = show;
		invalidate();
	}
	
	public boolean getShowHide(){
		return isShowActive;
	}
	
	public void setNameDevice(String name){
		textNameDevice = name;
		invalidate();
	}
	
	private boolean isEnableClickChange;
	public void setEnableClickChange(boolean do1, boolean do2){
		isEnableClickChange = do1 && do2;
		invalidate();
	}
	
	//----------------------//
	
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
			Button = 0;
			invalidate();
		};
	};
	
}
