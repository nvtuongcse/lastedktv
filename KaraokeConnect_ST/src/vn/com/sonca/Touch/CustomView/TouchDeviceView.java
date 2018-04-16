package vn.com.sonca.Touch.CustomView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TouchDeviceView extends View {

	public static final char CLEAR = '@';

	private TextPaint mainText = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private ArrayList<Circle> listCircles;
	private int widthLayout = 0;
	private int heightLayout = 0;

	private String password = "";

	public TouchDeviceView(Context context) {
		super(context);
		initView(context);
	}

	public TouchDeviceView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchDeviceView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private Drawable drawaActive;
	private Drawable drawaInActive;
	private Drawable drawDevice;
	private Drawable drawBack;
	private Drawable drawClearAC;
	private Drawable drawClearIN;
	private Drawable drawClear;
	private Drawable drawNhapPass;
	private Drawable drawHuy;
	private Drawable drawKetnoi;
	private Drawable drawButIn;
	private Drawable drawButHover;
	
	private Drawable zlightdrawaActive;
	private Drawable zlightdrawaInActive;
	private Drawable zlightdrawDevice;
	private Drawable zlightdrawBack;
	private Drawable zlightdrawClearAC;
	private Drawable zlightdrawClearIN;
	private Drawable zlightdrawClear;
	private Drawable zlightdrawNhapPass;
	private Drawable zlightdrawHuy;
	private Drawable zlightdrawKetnoi;
	private Drawable zlightdrawButIn;
	private Drawable zlightdrawButHover;
	
	private String nameDevice = "";
	private String ipDevice = "";
	private String textLabel = "";
	private String nhapmatkhau = "";
	private String textLeft, textRight;

	private void initView(Context context) {
		listCircles = new ArrayList<Circle>();
		
		drawaActive = getResources().getDrawable(R.drawable.image_boder_active_118x102);
		drawaInActive = getResources().getDrawable(R.drawable.image_boder_inactive_118x102);
		drawDevice = getResources().getDrawable(R.drawable.touch_daumay_chuaketnoi_56x50);
		drawBack = getResources().getDrawable(R.drawable.connect_back_48x48);
		drawClearIN = getResources().getDrawable(R.drawable.image_xoa_inactive_244x102);
		drawClearAC = getResources().getDrawable(R.drawable.image_xoa_active_244x102);
		drawClear = getResources().getDrawable(R.drawable.del_icon_72x72);
		
		drawNhapPass = getResources().getDrawable(R.drawable.touch_boder_ip_active);
		drawHuy = getResources().getDrawable(R.drawable.boder_lammoi);
		drawKetnoi = getResources().getDrawable(R.drawable.boder_lammoi);
		drawButIn = getResources().getDrawable(R.drawable.touch_boder_ketnoi_inactive);
		drawButHover = getResources().getDrawable(R.drawable.boder_lammoi_hover);
		
		//-------------------//
		
		zlightdrawaActive = getResources().getDrawable(R.drawable.zlight_keyboard_hover);
		zlightdrawaInActive = getResources().getDrawable(R.drawable.zlight_keyboard_active);
		zlightdrawDevice = getResources().getDrawable(R.drawable.zlight_image_daumay_dangketnoi);
		zlightdrawBack = getResources().getDrawable(R.drawable.zlight_connect_back_48x48);
		zlightdrawClearIN = getResources().getDrawable(R.drawable.zlight_image_xoa_active_244x102);
		zlightdrawClearAC = getResources().getDrawable(R.drawable.zlight_image_xoa_hover_244x102);
		zlightdrawClear = getResources().getDrawable(R.drawable.zlight_del_icon_72x72);
		zlightdrawNhapPass = getResources().getDrawable(R.drawable.zlight_boder_nhapip_hover);
		zlightdrawHuy = getResources().getDrawable(R.drawable.zlight_boder_ketnoi);
		zlightdrawKetnoi = getResources().getDrawable(R.drawable.zlight_boder_ketnoi);
		zlightdrawButIn = getResources().getDrawable(R.drawable.zlight_boder_ketnoi_xam);
		zlightdrawButHover = getResources().getDrawable(R.drawable.zlight_boder_ketnoi_hover);
		
		//-------------------//
		
		textLabel = getResources().getString(R.string.connect_1);
		nhapmatkhau = getResources().getString(R.string.connect_device_1);
		textLeft = getResources().getString(R.string.device_4);
		textRight = getResources().getString(R.string.device_6);
		
	}

	private OnDeviceViewListener listener;

	public interface OnDeviceViewListener {
		public void OnSendPass(String pass);
		public void OnBackLayout();
	}

	public void setOnDeviceViewListener(OnDeviceViewListener listener) {
		this.listener = listener;
	}

	private boolean lockSendPass = false;

	private void delaySendPass(){
		lockSendPass = true;
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				lockSendPass = false;
			}
		}, 3000);
	}
	
	public void setDataDevice(String ip, String name) {
		ipDevice = ip;
		nameDevice = name;
	}

	private int color_01;
	private int color_02;
	private int color_03;
	private int color_04;
	private int color_05;
	private int color_06;
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		// BACKVIEW

		// MyApplication.intColorScreen = MyApplication.SCREEN_LIGHT;
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			color_01 = Color.argb(180, 36, 110, 160);
			color_02 = Color.argb(255, 180, 254, 255);
			color_03 = Color.argb(255, 0, 253, 255);
			color_04 = Color.GRAY;
			color_05 = Color.GREEN;
			color_06 = Color.argb(255, 156, 226, 234);
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			color_01 = Color.parseColor("#21BAA9");
			color_02 = Color.parseColor("#FFFFFF");
			color_03 = Color.parseColor("#005249");
			color_04 = Color.GRAY;
			color_05 = Color.parseColor("#21BAA9");
			color_06 = Color.parseColor("#21BAA9");
		}

		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			// DRAW 0
			mainPaint.setStyle(Style.FILL);
			mainPaint.setColor(color_01);
			canvas.drawRect(rectLabel, mainPaint);

			drawNhapPass.setBounds(rectSearch);
			drawNhapPass.draw(canvas);

			if (isHuy) {
				drawButHover.setBounds(rectButLeft);
				drawButHover.draw(canvas);
			} else {
				drawHuy.setBounds(rectButLeft);
				drawHuy.draw(canvas);
			}

			if (!password.equals("")) {
				if (isConnect) {
					drawButHover.setBounds(rectButRight);
					drawButHover.draw(canvas);
				} else {
					drawKetnoi.setBounds(rectButRight);
					drawKetnoi.draw(canvas);
				}
			} else {
				drawButIn.setBounds(rectButRight);
				drawButIn.draw(canvas);
			}

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
				drawDevice.setBounds(rectDevide);
				drawDevice.draw(canvas);
				
				drawDevice = getResources().getDrawable(R.drawable.wifi_9108_4);
			} else if(MyApplication.intSvrModel == MyApplication.SONCA_TBT){
				drawDevice = getResources().getDrawable(R.drawable.daumay_tbt);
				drawDevice.setBounds(rectDevide);
				drawDevice.draw(canvas);
				
				drawDevice = getResources().getDrawable(R.drawable.ktvwifi_4);
			}
			drawDevice.setBounds(rectDevide);
			drawDevice.draw(canvas);

			drawBack.setBounds(rectBack);
			drawBack.draw(canvas);

			drawClear.setBounds(rectCl);
			drawClear.draw(canvas);

			textPaint.setStyle(Style.FILL);
			textPaint.setTextSize(labelS);
			textPaint.setColor(color_02);
			canvas.drawText(textLabel, labelX, labelY, textPaint);

			textPaint.setStyle(Style.FILL);
			textPaint.setTextSize(nameS);
			textPaint.setColor(color_03);
			canvas.drawText(nameDevice, nameX, nameY, textPaint);

			textPaint.setStyle(Style.FILL);
			textPaint.setTextSize(ipS);
			textPaint.setColor(color_03);
			canvas.drawText(ipDevice, ipX, ipY, textPaint);

			textPaint.setStyle(Style.FILL);
			textPaint.setTextSize(matkhauS);
			textPaint.setColor(color_02);
			canvas.drawText(nhapmatkhau, matkhauX, matkhauY, textPaint);

			textPaint.setStyle(Style.FILL);
			textPaint.setTextSize(textLeftS);
			textPaint.setColor(color_03);
			canvas.drawText(textLeft, textLeftX, textLeftY, textPaint);

			textPaint.setTextSize(textRightS);
			if (!password.equals("")) {
				textPaint.setColor(color_03);
			} else {
				textPaint.setColor(color_04);
			}
			canvas.drawText(textRight, textRightX, textRightY, textPaint);

			for (int i = 0; i < listCircles.size() - 2; i++) {
				Circle circle = listCircles.get(i);
				if (circle.isActive()) {
					drawaActive.setBounds(circle.getRectF());
					drawaActive.draw(canvas);
				} else {
					drawaInActive.setBounds(circle.getRectF());
					drawaInActive.draw(canvas);
				}
			}

			boolean bool1 = listCircles.get(listCircles.size() - 1).isActive();
			boolean bool2 = listCircles.get(listCircles.size() - 2).isActive();
			if (bool1 || bool2) {
				drawClearAC.setBounds(rectClear);
				drawClearAC.draw(canvas);
			} else {
				drawClearIN.setBounds(rectClear);
				drawClearIN.draw(canvas);
			}
			textPaint.setTextSize(passwordS);
			textPaint.setColor(color_05);
			float size = passwordX - textPaint.measureText(password) / 2;
			canvas.drawText(password, size, passwordY, textPaint);
			mainPaint.setColor(color_05);
			mainPaint.setStrokeWidth(3);
			float x = passwordX + textPaint.measureText(password) / 2 + 1;
			canvas.drawLine(x, intPassTop, x, intPassBottom, mainPaint);

			textPaint.setColor(color_02);
			textPaint.setTextSize((float) (0.06 * heightLayout));
			for (int i = 0; i < listCircles.size() - 2; i++) {
				Circle circle = listCircles.get(i);
				canvas.drawText(String.valueOf(circle.getName()),
						circle.getTextX(), circle.getTextY(), textPaint);
			}

			mainPaint.setShader(gradient);
			mainPaint.setStrokeWidth(graWidth);
			canvas.drawLine(0, graTopY, widthLayout, graTopY, mainPaint);
			canvas.drawLine(0, graBottomY, widthLayout, graBottomY, mainPaint);
			mainPaint.setShader(null);

			// ---------TEXT INFO 1---------//
			Paint tempPain = new Paint(Paint.ANTI_ALIAS_FLAG);
			String textInfo1 = getResources().getString(
					R.string.connect_device_4);
			tempPain.setTextSize(infoS);
			tempPain.setColor(color_06);
			tempPain.setTypeface(Typeface.create(Typeface.DEFAULT,
					Typeface.ITALIC));
			canvas.drawText(textInfo1, info1X, info1Y, tempPain);
			// ---------TEXT INFO 2---------//
			String textInfo2 = getResources().getString(
					R.string.connect_device_5);
			tempPain.setTextSize(infoS);
			tempPain.setColor(color_06);
			tempPain.setTypeface(Typeface.create(Typeface.DEFAULT,
					Typeface.ITALIC));
			canvas.drawText(textInfo2, info1X, info2Y, tempPain);

		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {

		}
		
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int myWidth = 700 * heightMeasureSpec / 1180;
		setMeasuredDimension(myWidth, heightMeasureSpec);
	}

	private Rect rectLabel;
	private float graWidth;
	private float graTopY;
	private float graBottomY;
	private int draW, drawH;
	private Rect rectSearch;
	private Rect rectDevide;
	private Rect rectButLeft;
	private Rect rectButRight;
	private Rect rectClear;
	private Rect rectBack;
	private Rect rectCl;
	private LinearGradient gradient;
	private LinearGradient gradientLight;
	private int passwordX, passwordY, passwordS;
	private int matkhauX, matkhauY, matkhauS;
	private float labelX, labelY, labelS;
	private int nameX, nameY, nameS;
	private int ipX, ipY, ipS;

	private int textLeftX, textLeftY, textLeftS;
	private int textRightX, textRightY, textRightS;

	private int intKey, intButton;
	private int intBackTop, intBackBottom;
	private int intPassTop , intPassBottom;
	private float infoS, info1X, info1Y, info2Y;
	private int MAXX, MAXY;
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		MAXY = 50*h/1032;
		MAXX = 10*h/1032;
		
		intBackTop = (int) (0.08 * h);
		intBackBottom = (int) (0.2 * h);
		intButton = (int) (0.32 * h);
		intKey = (int) (0.42 * h);

		widthLayout = w;
		heightLayout = h;

		matkhauX = (int) (0.14 * w);
		matkhauY = (int) (0.23 * h);
		matkhauS = (int) (0.035 * h);

		nameX = (int) (0.3 * w);
		nameY = (int) (0.14 * h);
		nameS = (int) (0.04 * h);

		ipX = (int) (0.3 * w);
		ipY = (int) (0.18 * h);
		ipS = (int) (0.035 * h);

		int tx = (int) (0.2 * w);
		int ty = (int) (0.145 * h);
		int wr = (int) (0.063 * w);
		int hr = wr;
		rectDevide = new Rect(tx - wr, ty - hr, tx + wr, ty + hr);

		tx = (int) (0.8 * w);
		ty = (int) (0.282 * h);
		wr = (int) (0.05 * w);
		hr = wr;
		rectCl = new Rect(tx - wr, ty - hr, tx + wr, ty + hr);

		tx = (int) (0.06 * w);
		ty = (int) (0.046 * h);
		wr = (int) (0.08 * w);
		hr = wr;
		rectBack = new Rect(tx - wr, ty - hr, tx + wr, ty + hr);

		tx = (int) (0.5 * w);
		ty = (int) (0.282 * h);
		hr = (int) (0.065 * w);
		wr = (int) (6 * hr);
		rectSearch = new Rect(tx - wr, ty - hr, tx + wr, ty + hr);
		
		int line = (int) (0.4*hr);
		intPassTop = ty - hr + line;
		intPassBottom = ty + hr - line;

		passwordS = (int) (0.05 * h);
		passwordX = (int) (0.5 * w);
		passwordY = (int) (ty + passwordS / 2.5);

		ty = (int) (0.37 * h);
		tx = (int) (0.35 * w);
		hr = (int) (0.08 * w);
		wr = (int) (1.7 * hr);
		rectButLeft = new Rect(tx - wr, ty - hr, tx + wr, ty + hr);
		tx = (int) (0.65 * w);
		rectButRight = new Rect(tx - wr, ty - hr, tx + wr, ty + hr);

		textLeftS = textRightS = (int) (0.035 * h);
		textLeftY = textRightY = (int) (ty + textLeftS / 2.5);
		textPaint.setTextSize(textLeftS);
		float size = textPaint.measureText(textLeft);
		textLeftX = (int) ((rectButLeft.right + rectButLeft.left) / 2 - size / 2);
		size = textPaint.measureText(textRight);
		textRightX = (int) ((rectButRight.right + rectButRight.left) / 2 - size / 2);

		ty = (int) (0.855 * h);
		tx = (int) (0.63 * w);
		hr = (int) (0.06 * h);
		wr = 244 * hr / 102;
		rectClear = new Rect(tx - wr, ty - hr, tx + wr, ty + hr);

		rectLabel = new Rect(0, 0, w, (int) (0.09 * h));
		graWidth = (float) (0.005 * h);
		graTopY = graWidth / 2;
		graBottomY = (int) (0.09 * h - graWidth / 2);
		gradient = new LinearGradient(0, 0, w / 2, 0, Color.TRANSPARENT,
				Color.CYAN, Shader.TileMode.MIRROR);
		gradientLight = new LinearGradient(0, 0, w / 2, 0, Color.WHITE,
				Color.parseColor("#FFF200"), Shader.TileMode.MIRROR);		
		
		labelS = (float) (0.045 * h);
		textPaint.setTextSize(labelS);
		size = textPaint.measureText(textLabel);
		labelX = w / 2 - size / 2;
		labelY = (float) (0.058 * h);

		infoS = (float) (0.025 * h);
		info1X = (float) (0.01 * w);
		info1Y = (float) (0.943 * h);
		info2Y = (float) (0.975 * h);

// --------123--------------------------------------------

		listCircles.clear();
		
		drawH = (int) (0.06 * h);
		draW = 118 * drawH / 102;
		textPaint.setTextSize((float) (0.06 * h));

		// MyApplication.intColorScreen = MyApplication.SCREEN_LIGHT;
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			drawH = (int) (0.06 * h);
			draW = 118 * drawH / 102;
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			drawH = (int) (0.055 * h);
			draW = 118 * drawH / 102;
		}
				
		int tamX = (int) (0.25 * widthLayout);
		int tamY = (int) (0.479 * heightLayout);
		int left = (int) (tamX - draW), right = (int) (tamX + draW);
		int top = (int) (tamY - drawH), bottom = (int) (tamY + drawH);
		float width = textPaint.measureText("1");
		int textX = (int) (tamX - width / 2);
		int textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('1', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		tamX = (int) (0.5 * widthLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = textPaint.measureText("2");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('2', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		tamX = (int) (0.75 * widthLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = textPaint.measureText("3");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('3', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		// --------456--------------------------------------------

		tamX = (int) (0.25 * widthLayout);
		tamY = (int) (0.604 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = textPaint.measureText("4");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('4', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		tamX = (int) (0.5 * widthLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = textPaint.measureText("5");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('5', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		tamX = (int) (0.75 * widthLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = textPaint.measureText("6");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('6', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		// --------789--------------------------------------------

		tamX = (int) (0.25 * widthLayout);
		tamY = (int) (0.73 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = textPaint.measureText("7");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('7', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		tamX = (int) (0.5 * widthLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = textPaint.measureText("8");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('8', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		tamX = (int) (0.75 * widthLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = textPaint.measureText("9");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('9', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		// --------0--------------------------------------------

		tamX = (int) (0.25 * widthLayout);
		tamY = (int) (0.855 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = textPaint.measureText("0");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('0', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		tamX = (int) (0.5 * widthLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = textPaint.measureText("2");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('2', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		tamX = (int) (0.75 * widthLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = textPaint.measureText("3");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('3', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));
	}

	private boolean isHuy = false;
	private boolean isConnect = false;
	private boolean isMove = false;
	private float moveX, moveY;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			setClickable(true);
			float offsetX = event.getX();
			float offsetY = event.getY();
			isMove = false;
			moveX = offsetX;
			moveY = offsetY;
			if (offsetY >= rectBack.top && offsetY <= rectBack.bottom) {
				if (offsetX >= rectBack.left && offsetX <= rectBack.right){
					
				}
			} else if (offsetY >= intButton && offsetY <= intKey) {
				if(offsetX >= 0 && offsetX <= widthLayout/2){
					isHuy = true;
					isConnect = false;
					
				}else{
					isHuy = false;
					isConnect = true;
					if (password != null) {
						
					}
				}
				invalidate();
			} else if (offsetY >= intKey && offsetY <= heightLayout) {
				int minItem = -1;
				float minR = (int) (0.1 * heightLayout);
				for (int i = 0; i < listCircles.size(); i++) {
					Circle circle = listCircles.get(i);
					circle.setActive(false);
					float centerX = circle.getCenterX();
					float centerY = circle.getCenterY();
					float R = (float) Math.sqrt((offsetX - centerX)
							* (offsetX - centerX) + (offsetY - centerY)
							* (offsetY - centerY));
					if (minR > R) {
						minR = R;
						minItem = i;
					}
				}
				if (minItem != -1) {
					listCircles.get(minItem).setActive(true);
					if (listener != null) {
						if (minItem == (listCircles.size() - 1)
								|| minItem == (listCircles.size() - 2)) {
							sendClear();
						} else {
							if(password.length()<4){
								password += listCircles.get(minItem).getName();	
								if(listener != null && password.length() == 4 && !lockSendPass){
									listener.OnSendPass(password);
									delaySendPass();
								}
							}
							invalidate();
						}
					}
				}
			}

			if (offsetX >= rectCl.left && offsetX <= rectCl.right
					&& offsetY >= rectCl.top && offsetY <= rectCl.bottom) {
				password = "";
			}
		}break;
		case MotionEvent.ACTION_UP: {
			stopClear();
			isHuy = false;
			isConnect = false;
			setClickable(false);
			
			if(isMove == true){
				isMove = false;
				invalidate();
				break;
			}
			
			float offsetX = event.getX();
			float offsetY = event.getY();
			moveX = 0;
			moveY = 0;
			if (offsetY >= rectBack.top && offsetY <= rectBack.bottom) {
				if (offsetX >= rectBack.left && offsetX <= rectBack.right){
					if (listener != null) {
						listener.OnBackLayout();
					}	
				}
			} else if (offsetY >= intButton && offsetY <= intKey) {
				if(offsetX >= 0 && offsetX <= widthLayout/2){
					isHuy = false;
					isConnect = false;
					invalidate();
					if (listener != null) {
						listener.OnBackLayout();
					}
				}else{
					isHuy = false;
					isConnect = false;
					invalidate();
					if (password != null) {
						if (password.length() > 0 && !lockSendPass) {
							listener.OnSendPass(password);
							delaySendPass();
						}
					}
				}
			} else if (offsetY >= intKey && offsetY <= heightLayout) {
				int minItem = -1;
				float minR = (int) (0.1 * heightLayout);
				for (int i = 0; i < listCircles.size(); i++) {
					Circle circle = listCircles.get(i);
					circle.setActive(false);
					float centerX = circle.getCenterX();
					float centerY = circle.getCenterY();
					float R = (float) Math.sqrt((offsetX - centerX)
							* (offsetX - centerX) + (offsetY - centerY)
							* (offsetY - centerY));
					if (minR > R) {
						minR = R;
						minItem = i;
					}
				}
				if (minItem != -1) {
					listCircles.get(minItem).setActive(true);
					if (listener != null) {
						if (minItem == (listCircles.size() - 1)
								|| minItem == (listCircles.size() - 2)) {
							invalidate();
						} else {		
							invalidate();
						}
					}
				}
			}
			if (offsetX >= rectCl.left && offsetX <= rectCl.right
					&& offsetY >= rectCl.top && offsetY <= rectCl.bottom) {
				password = "";
			}
			for (int i = 0; i < listCircles.size(); i++) {
				listCircles.get(i).setActive(false);
			}
			invalidate();
		}
			break;
		case MotionEvent.ACTION_MOVE: {
//			MyLog.e("DeviceView", "ACTION_MOVE");
			float x = Math.abs(moveX - event.getX());
			float y = Math.abs(moveY - event.getY());
			if(x > MAXX || y > MAXY){
				isHuy = false;
				isConnect = false;
				isMove = true;
				for (int i = 0; i < listCircles.size(); i++) {
					listCircles.get(i).setActive(false);
				}
				invalidate();
			}
			if (delay500 != null) {
				delay500.cancel(true);
				delay500 = null;
			}
			delay500 = new Delay500();
			delay500.execute();
		}
			break;
		}
		return true;
	}

	// ///////////////////////////////////////////////////////////////

	class Circle {
		private boolean isActive = false;
		private char name;
		private Rect rectF;
		private int centerX;
		private int centerY;
		private int textX;
		private int textY;

		public Circle(char name, int centerX, int centerY, int textX,
				int textY, Rect rectF) {
			this.centerX = centerX;
			this.centerY = centerY;
			this.textX = textX;
			this.textY = textY;
			this.rectF = rectF;
			this.name = name;
		}

		public char getName() {
			return name;
		}

		public int getTextX() {
			return textX;
		}

		public int getTextY() {
			return textY;
		}

		public int getCenterX() {
			return centerX;
		}

		public int getCenterY() {
			return centerY;
		}

		public Rect getRectF() {
			return rectF;
		}

		public boolean isActive() {
			return isActive;
		}

		public void setActive(boolean isActive) {
			this.isActive = isActive;
		}
	}

	// ////////////////////////////////////////////////////////////

	private Delay500 delay500 = null;

	class Delay500 extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			long m = System.currentTimeMillis();
			while ((System.currentTimeMillis() - m) < 500) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			isHuy = false;
			isConnect = false;
			for (int i = 0; i < listCircles.size(); i++) {
				listCircles.get(i).setActive(false);
			}
			invalidate();
		}
	}

	// //////////////////////////////////////////////////////////////

	private Timer timerClear;

	private void sendClear() {
		timerClear = new Timer();
		timerClear.schedule(new TimerTask() {
			@Override
			public void run() {
				handlerClear.sendEmptyMessage(0);
			}
		}, 1);
	}

	Handler handlerClear = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (password.length() >= 1) {
				password = password.substring(0, password.length() - 1);
			}
			invalidate();
		};
	};

	private void stopClear() {
		if (timerClear != null) {
			timerClear.cancel();
			timerClear = null;
		}
	}
	
}
