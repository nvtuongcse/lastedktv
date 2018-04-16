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
import android.graphics.Typeface;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewDebug.IntToString;

public class TouchDeviceViewIP extends View {

	public static final char CLEAR = '@';
	private String TAB = "DeviceViewIP";
	private TextPaint mainText = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private ArrayList<Circle> listCircles;
	private int widthLayout = 0;
	private int heightLayout = 0;

	private String IPDevice = "";
	private String password = "";

	public TouchDeviceViewIP(Context context) {
		super(context);
		initView(context);
	}

	public TouchDeviceViewIP(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchDeviceViewIP(Context context, AttributeSet attrs, int defStyle) {
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
	private Drawable drawHuy;
	private Drawable drawKetnoi;
	private Drawable drawButIn;
	private Drawable drawButHover;
	private Drawable drawNhapPassFocus;
	private Drawable drawNhapPassNOFocus;
	
	private Drawable zlightdrawaActive;
	private Drawable zlightdrawaInActive;
	private Drawable zlightdrawDevice;
	private Drawable zlightdrawBack;
	private Drawable zlightdrawClearAC;
	private Drawable zlightdrawClearIN;
	private Drawable zlightdrawClear;
	private Drawable zlightdrawHuy;
	private Drawable zlightdrawKetnoi;
	private Drawable zlightdrawButIn;
	private Drawable zlightdrawButHover;
	private Drawable zlightdrawNhapPassFocus;
	private Drawable zlightdrawNhapPassNOFocus;
	
	private String nameDevice = "";
	private String ipDevice = "";
	private String textLabel = "";
	private String nhapIP = "";
	private String nhapmatkhau = "";
	private String textLeft, textRight;
	private String textThemDM = "Them dau may";
	

	private void initView(Context context) {
		listCircles = new ArrayList<Circle>();
		
		drawHuy = getResources().getDrawable(R.drawable.boder_lammoi);
		drawKetnoi = getResources().getDrawable(R.drawable.boder_lammoi);
		drawaActive = getResources().getDrawable(R.drawable.image_boder_active_118x102);
		drawaInActive = getResources().getDrawable(R.drawable.image_boder_inactive_118x102);
		drawDevice = getResources().getDrawable(R.drawable.touch_daumay_chuaketnoi_56x50);
		drawBack = getResources().getDrawable(R.drawable.connect_back_48x48);
		drawClearIN = getResources().getDrawable(R.drawable.touch_xoa_118x90);
		drawClearAC = getResources().getDrawable(R.drawable.touch_xoa_118x90_active);
		drawClear = getResources().getDrawable(R.drawable.del_icon_72x72);
		drawButIn = getResources().getDrawable(R.drawable.touch_boder_ketnoi_inactive);
		drawButHover = getResources().getDrawable(R.drawable.boder_lammoi_hover);
		drawNhapPassFocus = getResources().getDrawable(R.drawable.touch_boder_ip_active);
		drawNhapPassNOFocus = getResources().getDrawable(R.drawable.touch_boder_ip_inactive);

		zlightdrawHuy = getResources().getDrawable(R.drawable.zlight_boder_ketnoi);
		zlightdrawKetnoi = getResources().getDrawable(R.drawable.zlight_boder_ketnoi);
		zlightdrawaActive = getResources().getDrawable(R.drawable.zlight_keyboard_hover);
		zlightdrawaInActive = getResources().getDrawable(R.drawable.zlight_keyboard_active);
		zlightdrawDevice = getResources().getDrawable(R.drawable.zlight_image_daumay_inactive_71x65);
		zlightdrawBack = getResources().getDrawable(R.drawable.zlight_connect_back_48x48);
		zlightdrawClearIN = getResources().getDrawable(R.drawable.zlight_keyboard_xoa);
		zlightdrawClearAC = getResources().getDrawable(R.drawable.zlight_keyboard_xoa_hover);
		zlightdrawClear = getResources().getDrawable(R.drawable.zlight_del_icon_72x72);
		zlightdrawButIn = getResources().getDrawable(R.drawable.zlight_boder_ketnoi_xam);
		zlightdrawButHover = getResources().getDrawable(R.drawable.zlight_boder_ketnoi_hover);
		zlightdrawNhapPassFocus = getResources().getDrawable(R.drawable.zlight_boder_nhapip_hover);
		zlightdrawNhapPassNOFocus = getResources().getDrawable(R.drawable.zlight_boder_nhapip_active);
		
		textLabel = getResources().getString(R.string.connect_device_10);
		nhapmatkhau = getResources().getString(R.string.connect_nhap_ip);
		nhapIP = getResources().getString(R.string.connect_nhap_pass);
		
		textThemDM = getResources().getString(R.string.connect_device_12);
		textLeft = getResources().getString(R.string.device_4);
		textRight = getResources().getString(R.string.device_6);
		
		
	}

	private OnDeviceViewIPListener listener;
	public interface OnDeviceViewIPListener {
		public void OnConectIpPass(String IP , String pass);
		public void OnBackLayout();
	}

	public void setOnDeviceViewIPListener(OnDeviceViewIPListener listener) {
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

			// -------------------------------

			int lineTop = 0;
			int lineBottom = 0;
			if (SearchFocus == FOCUS_IP) {
				int o = (int) (0.8 * (rectSearch.bottom - rectSearch.top));
				lineTop = rectSearch.top + o;
				lineBottom = rectSearch.bottom - o;
				drawNhapPassFocus.setBounds(rectSearch);
				drawNhapPassFocus.draw(canvas);
				drawNhapPassNOFocus.setBounds(rectSearch2);
				drawNhapPassNOFocus.draw(canvas);
			} else {
				int o = (int) (0.8 * (rectSearch2.bottom - rectSearch2.top));
				lineTop = rectSearch2.top + o;
				lineBottom = rectSearch2.bottom - o;
				drawNhapPassNOFocus.setBounds(rectSearch);
				drawNhapPassNOFocus.draw(canvas);
				drawNhapPassFocus.setBounds(rectSearch2);
				drawNhapPassFocus.draw(canvas);
			}

			// -------------------------------

			if (isHuy) {
				drawButHover.setBounds(rectButLeft);
				drawButHover.draw(canvas);
			} else {
				drawHuy.setBounds(rectButLeft);
				drawHuy.draw(canvas);
			}

			drawBack.setBounds(rectBack);
			drawBack.draw(canvas);

			textPaint.setTextSize(themdmS);
			textPaint.setColor(color_03);
			canvas.drawText(textThemDM, themdmX, themdmY, textPaint);

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
			canvas.drawText(nhapIP, nhapipX, nhapipY, textPaint);

			textPaint.setStyle(Style.FILL);
			textPaint.setTextSize(textLeftS);
			textPaint.setColor(color_03);
			canvas.drawText(textLeft, textLeftX, textLeftY, textPaint);

			textPaint.setTextSize(textRightS);
			if (!password.equals("") && !IPDevice.equals("")) {
				textPaint.setColor(color_03);
			} else {
				textPaint.setColor(color_04);
			}
			canvas.drawText(textRight, textRightX, textRightY, textPaint);

			int minItem = -1;
			for (int i = 0; i < listCircles.size() - 1; i++) {
				Circle circle = listCircles.get(i);
				if (circle.isActive()) {
					drawaActive.setBounds(circle.getRectF());
					drawaActive.draw(canvas);
					minItem = i;
				} else {
					drawaInActive.setBounds(circle.getRectF());
					drawaInActive.draw(canvas);
				}
			}

			Circle cir = listCircles.get(listCircles.size() - 1);
			boolean bool1 = cir.isActive();
			if (bool1) {
				drawClearAC.setBounds(cir.getRectF());
				drawClearAC.draw(canvas);
			} else {
				drawClearIN.setBounds(cir.getRectF());
				drawClearIN.draw(canvas);
			}

			textPaint.setTextSize(passwordS);
			textPaint.setColor(color_05);
			float size = passwordX - textPaint.measureText(password) / 2;
			canvas.drawText(password, size, passwordY, textPaint);
			size = ipdeviceX - textPaint.measureText(IPDevice) / 2;
			canvas.drawText(IPDevice, size, ipdeviceY, textPaint);

			mainPaint.setStrokeWidth(3);
			mainPaint.setColor(color_05);
			if (SearchFocus == FOCUS_PASS) {
				float lineX = passwordX + textPaint.measureText(password) / 2
						+ 1;
				canvas.drawLine(lineX, lineTop, lineX, lineBottom, mainPaint);
			} else {
				float lineX = ipdeviceX + textPaint.measureText(IPDevice) / 2
						+ 1;
				canvas.drawLine(lineX, lineTop, lineX, lineBottom, mainPaint);
			}

			textPaint.setColor(color_02);
			textPaint.setTextSize((float) (0.06 * heightLayout));
			for (int i = 0; i < listCircles.size() - 1; i++) {
				Circle circle = listCircles.get(i);
				canvas.drawText(String.valueOf(circle.getName()),
						circle.getTextX(), circle.getTextY(), textPaint);
			}

			if (!password.equals("") && !IPDevice.equals("")) {
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

			// DRAW 0
			mainPaint.setStyle(Style.FILL);
			mainPaint.setColor(color_01);
			canvas.drawRect(rectLabel, mainPaint);

			// -------------------------------

			int lineTop = 0;
			int lineBottom = 0;
			if (SearchFocus == FOCUS_IP) {
				int o = (int) (0.8 * (rectSearch.bottom - rectSearch.top));
				lineTop = rectSearch.top + o;
				lineBottom = rectSearch.bottom - o;
				zlightdrawNhapPassFocus.setBounds(rectSearch);
				zlightdrawNhapPassFocus.draw(canvas);
				zlightdrawNhapPassNOFocus.setBounds(rectSearch2);
				zlightdrawNhapPassNOFocus.draw(canvas);
			} else {
				int o = (int) (0.8 * (rectSearch2.bottom - rectSearch2.top));
				lineTop = rectSearch2.top + o;
				lineBottom = rectSearch2.bottom - o;
				zlightdrawNhapPassNOFocus.setBounds(rectSearch);
				zlightdrawNhapPassNOFocus.draw(canvas);
				zlightdrawNhapPassFocus.setBounds(rectSearch2);
				zlightdrawNhapPassFocus.draw(canvas);
			}

			// -------------------------------

			if (isHuy) {
				zlightdrawButHover.setBounds(rectButLeft);
				zlightdrawButHover.draw(canvas);
			} else {
				zlightdrawHuy.setBounds(rectButLeft);
				zlightdrawHuy.draw(canvas);
			}

			zlightdrawBack.setBounds(rectBack);
			zlightdrawBack.draw(canvas);

			textPaint.setTextSize(themdmS);
			textPaint.setColor(color_03);
			canvas.drawText(textThemDM, themdmX, themdmY, textPaint);

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
			textPaint.setColor(color_03);
			canvas.drawText(nhapmatkhau, matkhauX, matkhauY, textPaint);
			canvas.drawText(nhapIP, nhapipX, nhapipY, textPaint);

			textPaint.setStyle(Style.FILL);
			textPaint.setTextSize(textLeftS);
			textPaint.setColor(color_02);
			canvas.drawText(textLeft, textLeftX, textLeftY, textPaint);

			int minItem = -1;
			for (int i = 0; i < listCircles.size() - 1; i++) {
				Circle circle = listCircles.get(i);
				if (circle.isActive()) {
					zlightdrawaActive.setBounds(circle.getRectF());
					zlightdrawaActive.draw(canvas);
					minItem = i;
				} else {
					zlightdrawaInActive.setBounds(circle.getRectF());
					zlightdrawaInActive.draw(canvas);
				}
			}

			Circle cir = listCircles.get(listCircles.size() - 1);
			boolean bool1 = cir.isActive();
			if (bool1) {
				zlightdrawClearAC.setBounds(cir.getRectF());
				zlightdrawClearAC.draw(canvas);
			} else {
				zlightdrawClearIN.setBounds(cir.getRectF());
				zlightdrawClearIN.draw(canvas);
			}

			textPaint.setTextSize(passwordS);
			textPaint.setColor(color_05);
			float size = passwordX - textPaint.measureText(password) / 2;
			canvas.drawText(password, size, passwordY, textPaint);
			size = ipdeviceX - textPaint.measureText(IPDevice) / 2;
			canvas.drawText(IPDevice, size, ipdeviceY, textPaint);

			mainPaint.setStrokeWidth(3);
			mainPaint.setColor(color_05);
			if (SearchFocus == FOCUS_PASS) {
				float lineX = passwordX + textPaint.measureText(password) / 2
						+ 1;
				canvas.drawLine(lineX, lineTop, lineX, lineBottom, mainPaint);
			} else {
				float lineX = ipdeviceX + textPaint.measureText(IPDevice) / 2
						+ 1;
				canvas.drawLine(lineX, lineTop, lineX, lineBottom, mainPaint);
			}

			textPaint.setColor(color_02);
			textPaint.setTextSize((float) (0.06 * heightLayout));
			for (int i = 0; i < listCircles.size() - 1; i++) {
				Circle circle = listCircles.get(i);
				if (circle.isActive()) {
					textPaint.setColor(color_02);
					canvas.drawText(String.valueOf(circle.getName()),
							circle.getTextX(), circle.getTextY(), textPaint);
				} else {
					textPaint.setColor(color_01);
					canvas.drawText(String.valueOf(circle.getName()),
							circle.getTextX(), circle.getTextY(), textPaint);
				}
			}

			if (!password.equals("") && !IPDevice.equals("")) {
				if (isConnect) {
					zlightdrawButHover.setBounds(rectButRight);
					zlightdrawButHover.draw(canvas);
				} else {
					zlightdrawKetnoi.setBounds(rectButRight);
					zlightdrawKetnoi.draw(canvas);
				}
			} else {
				zlightdrawButIn.setBounds(rectButRight);
				zlightdrawButIn.draw(canvas);
			}
			textPaint.setTextSize(textRightS);
			if (!password.equals("") && !IPDevice.equals("")) {
				textPaint.setColor(color_02);
			} else {
				textPaint.setColor(color_02);
			}
			canvas.drawText(textRight, textRightX, textRightY, textPaint);

			mainPaint.setShader(gradientLight);
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
	private Rect rectSearch2;
	private Rect rectDevide;
	private Rect rectButLeft;
	private Rect rectButRight;
	private Rect rectClear;
	private Rect rectBack;
	private LinearGradient gradient;
	private LinearGradient gradientLight;
	private int matkhauX, matkhauY, matkhauS;
	private int nhapipX , nhapipY , nhapipS;
	private float labelX, labelY, labelS;
	private int nameX, nameY, nameS;
	private int ipX, ipY, ipS;

	private int themdmS , themdmX , themdmY;
	private int textLeftX, textLeftY, textLeftS;
	private int textRightX, textRightY, textRightS;
	private float infoS, info1X, info1Y, info2Y;
	
//-------------------
	private int passwordX, passwordY, passwordS;
	private int ipdeviceX, ipdeviceY, ipdeviceS;
//-------------------
	private int intBackTop, intBackBottom;
	private int intIp, intPass;
	private int intKey, intButton;
//------------------
	private final boolean FOCUS_IP = true;
	private final boolean FOCUS_PASS = false;
	private boolean SearchFocus = FOCUS_IP;
	private int MAXX, MAXY;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		MAXY = 50*h/1032;
		MAXX = 10*h/1032;

		intBackTop = (int) (0.07 * h);
		intBackBottom = (int) (0.15 * h);
		intIp = (int) (0.27 * h);
		intPass = (int) (0.39 * h);
		intButton = (int) (0.47 * h);

		widthLayout = w;
		heightLayout = h;

		matkhauX = (int) (0.14 * w);
		matkhauY = (int) (0.18 * h);
		matkhauS = (int) (0.03 * h);
		
		nhapipS = (int) (0.03 * h);
		nhapipX = (int) (0.14 * w);
		nhapipY = (int) (0.3 * h);

		nameX = (int) (0.3 * w);
		nameY = (int) (0.14 * h);
		nameS = (int) (0.04 * h);

		ipX = (int) (0.3 * w);
		ipY = (int) (0.18 * h);
		ipS = (int) (0.035 * h);

		int tx = (int) (0.175 * w);
		int ty = (int) (0.11 * h);
		int wr = (int) (0.053 * w);
		int hr = wr;
		rectDevide = new Rect(tx - wr, ty - hr, tx + wr, ty + hr);

		tx = (int) (0.06 * w);
		ty = (int) (0.046 * h);
		wr = (int) (0.08 * w);
		hr = wr;
		rectBack = new Rect(tx - wr, ty - hr, tx + wr, ty + hr);

		tx = (int) (0.5 * w);
		ty = (int) (0.23 * h);
		hr = (int) (0.065 * w);
		wr = (int) (6 * hr);
		ipdeviceS = (int) (0.05 * h);
		ipdeviceX = (int) (0.5 * w);
		ipdeviceY = (int) (ty + ipdeviceS / 2.5);
		rectSearch = new Rect(tx - wr, ty - hr, tx + wr, ty + hr);
		ty = (int) (0.348 * h);
		rectSearch2 = new Rect(tx - wr, ty - hr, tx + wr, ty + hr);

		passwordS = (int) (0.05 * h);
		passwordX = (int) (0.5 * w);
		passwordY = (int) (ty + passwordS / 2.5);
		
		ty = (int) (0.43 * h);
		tx = (int) (0.35 * w);
		hr = (int) (0.06 * w);
		wr = (int) (2.3 * hr);
		rectButLeft = new Rect(tx - wr, ty - hr, tx + wr, ty + hr);
		tx = (int) (0.67 * w);
		rectButRight = new Rect(tx - wr, ty - hr, tx + wr, ty + hr);

		textLeftS = textRightS = (int) (0.03 * h);
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
		
		themdmS = (int) (0.035*h);
		textPaint.setTextSize(themdmS);
		size = textPaint.measureText(textThemDM);
		themdmX = (int)(w / 2 - size / 2);
		themdmY = (int) (0.13*h);

		infoS = (float) (0.028 * h);
		info1X = (float) (0.01 * w);
		info1Y = (float) (0.953 * h);
		info2Y = (float) (0.99 * h);

//--------123--------------------------------------------

		listCircles.clear();
		
		drawH = (int) (0.055 * h);
		draW = 118 * drawH / 90;
		textPaint.setTextSize((float) (0.06 * h));
		
		// MyApplication.intColorScreen = MyApplication.SCREEN_LIGHT;
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			drawH = (int) (0.055 * h);
			draW = 118 * drawH / 90;
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			drawH = (int) (0.05 * h);
			draW = 118 * drawH / 90;
		}
		
		int tamX = (int) (0.25 * widthLayout);
		int tamY = (int) (0.527 * heightLayout);
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

//--------456--------------------------------------------

		tamX = (int) (0.25 * widthLayout);
		tamY = (int) (0.642 * heightLayout);
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

//--------789--------------------------------------------

		tamX = (int) (0.25 * widthLayout);
		tamY = (int) (0.757 * heightLayout);
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

//--------0--------------------------------------------

		tamX = (int) (0.25 * widthLayout);
		tamY = (int) (0.872 * heightLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = textPaint.measureText(".");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('.', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		tamX = (int) (0.5 * widthLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = textPaint.measureText("0");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('0', tamX, tamY, textX, textY, new Rect(
				left, top, right, bottom)));

		tamX = (int) (0.75 * widthLayout);
		left = (int) (tamX - draW);
		right = (int) (tamX + draW);
		top = (int) (tamY - drawH);
		bottom = (int) (tamY + drawH);
		width = textPaint.measureText("@");
		textX = (int) (tamX - width / 2);
		textY = (int) (tamY + drawH / 2.6);
		listCircles.add(new Circle('@', tamX, tamY, textX, textY, new Rect(
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
			} else if (offsetY >= intBackBottom && offsetY <= intPass) {
				if (offsetY >= intBackBottom && offsetY <= intIp) {
					/*
					SearchFocus = FOCUS_IP;
					if (IPDevice.length() >= 1) {
						IPDevice = IPDevice.substring(0, IPDevice.length() - 1);
					}
					*/
				} 
				if (offsetY >= intIp && offsetY <= intPass){
					/*
					SearchFocus = FOCUS_PASS;
					if (password.length() >= 1) {
						password = password.substring(0, password.length() - 1);
					}
					*/
				}
				invalidate();
			} else if (offsetY >= intPass && offsetY <= intButton) {
				if(offsetX >= 0 && offsetX <= widthLayout/2){
					isHuy = true;
					isConnect = false;
					
				}else{
					isHuy = false;
					isConnect = true;
					if(listener != null){
							
					}
				}
				invalidate();
			} else if (offsetY >= intButton && offsetY <= heightLayout) {
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
				
//				MyLog.i(TAB, "minR - " + minR + " | minItem - " + minItem);
				
				if (minItem != -1) {
					listCircles.get(minItem).setActive(true);
					if (listener != null) {
						if (minItem == (listCircles.size() - 1)) {
							sendClear();
						} else {
							if (SearchFocus == FOCUS_PASS) {
								if(password.length()<4){
									password += listCircles.get(minItem).getName();	
								}	
							} else {
								if(IPDevice.length()<15){
									IPDevice += listCircles.get(minItem).getName();	
								}
							}	
							invalidate();
						}
					}
				}
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
			} else if (offsetY >= intBackBottom && offsetY <= intPass) {
				if (offsetY >= intBackBottom && offsetY <= intIp) {
					SearchFocus = FOCUS_IP;
					/*
					if (IPDevice.length() >= 1) {
						IPDevice = IPDevice.substring(0, IPDevice.length() - 1);
					}
					*/
				} 
				if (offsetY >= intIp && offsetY <= intPass){
					SearchFocus = FOCUS_PASS;
					/*
					if (password.length() >= 1) {
						password = password.substring(0, password.length() - 1);
					}
					*/
				}
				invalidate();
			} else if (offsetY >= intPass && offsetY <= intButton) {
				if(offsetX >= 0 && offsetX <= widthLayout/2){
					isHuy = false;
					isConnect = false;
					if (listener != null) {
						listener.OnBackLayout();
					}
				}else{
					isHuy = false;
					isConnect = false;
					if(listener != null){
						if(IPDevice !=null && password!=null){
							if(IPDevice.length()>0 && password.length()>0 && !lockSendPass){
								listener.OnConectIpPass(IPDevice , password);		
								delaySendPass();
							}
						}	
					}
				}
				invalidate();
			} else if (offsetY >= intButton && offsetY <= heightLayout) {
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
				
//				MyLog.i(TAB, "minR - " + minR + " | minItem - " + minItem);
				
				if (minItem != -1) {
					listCircles.get(minItem).setActive(true);
					if (listener != null) {
						if (minItem == (listCircles.size() - 1)) {
							invalidate();
						} else {
							invalidate();
						}
					}
				}
			}
			for (int i = 0; i < listCircles.size(); i++) {
				listCircles.get(i).setActive(false);
			}
			invalidate();
		}
			break;
		case MotionEvent.ACTION_MOVE: {
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
			if (SearchFocus == FOCUS_IP) {
				if (IPDevice.length() >= 1) {
					IPDevice = IPDevice.substring(0, IPDevice.length() - 1);
				}
			} 
			if (SearchFocus == FOCUS_PASS){
				if (password.length() >= 1) {
					password = password.substring(0, password.length() - 1);
				}
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
