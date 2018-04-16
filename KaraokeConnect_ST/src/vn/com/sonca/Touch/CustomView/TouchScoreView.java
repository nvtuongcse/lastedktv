package vn.com.sonca.Touch.CustomView;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.zktv.main.KTVMainActivity;
import vn.com.sonca.zzzzz.MyApplication;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TouchScoreView extends View {

	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
	// private Context context;
	private String textState;

	private boolean isTouch = false;
	private int isActive = 0;

	private static final String STROKE_COLOR_1 = "#91d2d6";
	private static final String STROKE_COLOR_2 = "#001a31";
	private static final String STROKE_COLOR_3 = "#34495c";
	private static final String STROKE_COLOR_4 = "#011b32";
	private static final String TEXT_COLOR = "#00fdfd";

	private OnScoreListener listener;

	public interface OnScoreListener {
		public void onScore(int isScoreOn);
		public void OnInActive();
	}

	public void setOnScoreListener(OnScoreListener listener) {
		this.listener = listener;
	}

	public TouchScoreView(Context context) {
		super(context);
		initView(context);
	}

	public TouchScoreView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchScoreView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Drawable drawActive;
	private Drawable drawHover;

	private Drawable zlight_drawActive, zlight_drawHover, zlight_drawXam,
			zlight_icon_on, zlight_icon_off, zlight_icon_chuyennghiep,
			zlight_icon_xam;
	
	private String textTitle = "";
	private void initView(Context context) {
		// this.context = context;
		drawHover = getResources().getDrawable(
				R.drawable.touch_ctr_actv_singer_bgrnd);
		drawActive = getResources().getDrawable(
				R.drawable.touch_image_singer_vongtronngoai_active);
		textTitle = getResources().getString(R.string.main_left_5);
		
		zlight_drawActive = getResources().getDrawable(
				R.drawable.zlight_round_boder_active);
		zlight_drawHover = getResources().getDrawable(
				R.drawable.zlight_round_boder_hover);
		zlight_drawXam = getResources().getDrawable(
				R.drawable.zlight_round_boder_xam);
		zlight_icon_on = getResources().getDrawable(
				R.drawable.zlight_mc_score_on);
		zlight_icon_off = getResources().getDrawable(
				R.drawable.zlight_mc_score_off);
		zlight_icon_chuyennghiep = getResources().getDrawable(
				R.drawable.zlight_mc_score_chuyennghiep);
		zlight_icon_xam = getResources().getDrawable(
				R.drawable.zlight_mc_score_xam);
	}
	
	private long longtimersync = 0;
	public void setScoreView(int isActive) {
		if(MyApplication.intWifiRemote == MyApplication.SONCA){
			if(System.currentTimeMillis() - longtimersync 
				<= MyApplication.TIMER_SYNC){
				return;
			}
			if (stateView != View.INVISIBLE) {
				this.isActive = isActive;
				invalidate();
			}
		}
	}
	
	public int getScoreView(){
		return isActive;
	}
	
	public int getToneView(){
		return isActive;
	}

	private int stateView = View.VISIBLE;

	public void setEnableView(int value) {
		stateView = value;
		isActive = 0;
		invalidate();
	}
	

	private float widthLayout;
	private float heightLayout;
	private float centerX, centerY;
	private float titleY, titleS;
	private float textStateS, textStateY;
	private float radXamTrong, stroXamTrong;
	private float radXamNgoai, stroXamNgoai;
	private Rect rectVien = new Rect();
	private Rect rectImageKar = new Rect();
	private Rect rectImageSonca = new Rect();
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		rectVien.set(0, 0, w, h);
		widthLayout = w;
		heightLayout = h;
		centerX = w/2;
		centerY = h/2;
		int r = 0;
		if (w > h) {
			r = h;
		} else {
			r = w;
		}
		stroXamTrong = (float) (0.07*r);
		radXamTrong = (float) (0.335*r);
		stroXamNgoai = (float) (0.03*r);
		radXamNgoai = (float) (0.45*r);
		
		int tamX = w/2;
		int tamY = h/2;
		int wi = (int) (0.28*r);
		int hi = wi;
		rectImageSonca.set(tamX - wi, tamY - hi, tamX + wi, tamY + hi);
		
		titleS = (float) (0.085*r);
		titleY = (float) (h/2 + hi - 0.5*titleS);
		textStateS = (float) (0.075*r);
		textStateY = (float) (h/2 - hi + 1.3*titleS);
		
		
		
	}
	
	private int colorTextActive;
	private int colorTextXam;

	private Drawable drawImage;
	private boolean boolBlockComand = false;
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		// =====================
		/*
		 * isActive = 2; isTouch = false; stateView = VISIBLE;
		 * MyApplication.intWifiRemote = MyApplication.SONCA;
		 * MyApplication.intColorScreen = MyApplication.SCREEN_BLUE;
		 * MyApplication.intCommandEnable = ~INTCOMMAND;
		 */
		// =====================

		if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			colorTextActive = Color.argb(255, 0, 82, 73);
			colorTextXam = Color.argb(255, 152, 152, 152);
		}

		if (TouchMainActivity.serverStatus != null || KTVMainActivity.serverStatus != null) {
			boolean flagControlFullAPI = false;
			if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				if (KTVMainActivity.serverStatus.isOnOffControlFullAPI()) {
					flagControlFullAPI = true;
				}
			} else {
				if (TouchMainActivity.serverStatus.isOnOffControlFullAPI()) {
					flagControlFullAPI = true;
				}
			}
			
			if (flagControlFullAPI) {
				int clear = 0x00000003;
				int retur = (MyApplication.intCommandMedium & (clear << INTMEDIUM)) >> INTMEDIUM;
				if ((MyApplication.flagDeviceUser == true && retur != 0)
						|| (MyApplication.flagDeviceUser == false && retur == 2)) {
					boolBlockComand = true;
				} else {
					boolBlockComand = false;
				}
			} else {
				if ((MyApplication.intCommandEnable & INTCOMMAND) != INTCOMMAND) {
					boolBlockComand = true;
				} else {
					boolBlockComand = false;
				}
			}
		}

		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			if (boolBlockComand == true) {
				paintMain.setStyle(Style.STROKE);
				paintMain.setStrokeWidth(stroXamNgoai);
				paintMain.setARGB(51, 255, 255, 255);
				canvas.drawCircle(centerX, centerY, radXamNgoai - stroXamNgoai,
						paintMain);

				paintMain.setStyle(Style.STROKE);
				paintMain.setStrokeWidth(stroXamTrong);
				paintMain.setARGB(51, 255, 255, 255);
				canvas.drawCircle(centerX, centerY, radXamTrong, paintMain);

				textPaint.setStyle(Style.FILL);
				textPaint.setTextSize(titleS);
				textPaint.setColor(Color.GRAY);
				float width = textPaint.measureText(textTitle);
				canvas.drawText(textTitle,
						(float) (0.5 * widthLayout - 0.5 * width), titleY,
						textPaint);

				drawImage = getResources().getDrawable(
						R.drawable.touch_mc_socre_off_96x96_old);
				drawImage.setBounds(rectImageSonca);
				drawImage.draw(canvas);

				return;
			}

			boolean flag = false;
			if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				if (KTVMainActivity.serverStatus == null) {
					flag = true;
				}
			} else {
				if (TouchMainActivity.serverStatus == null) {
					flag = true;
				}
			}
			
			if (flag) {
				stateView = INVISIBLE;
			} else {
				stateView = VISIBLE;
			}
			
			if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && MyApplication.flagPlayingYouTube){
				stateView = INVISIBLE;
			}

			if (MyApplication.intWifiRemote == MyApplication.SONCA) {

				if (stateView == VISIBLE) {
					if (isTouch) {
						drawHover.setBounds(rectVien);
						drawHover.draw(canvas);
					} else {
						paintMain.setStyle(Style.STROKE);
						paintMain.setStrokeWidth(stroXamTrong);
						paintMain.setARGB(51, 255, 255, 255);
						canvas.drawCircle(centerX, centerY, radXamTrong,
								paintMain);

						drawActive.setBounds(rectVien);
						drawActive.draw(canvas);
					}

					switch (isActive) {
					case 0:
						textState = getResources().getString(
								R.string.main_left_5a);
						drawImage = getResources().getDrawable(
								R.drawable.touch_mc_socre_off_96x96);
						break;
					case 1:
						textState = getResources().getString(
								R.string.main_left_5b);
						drawImage = getResources().getDrawable(
								R.drawable.touch_mc_socre_on_96x96);
						break;
					case 2:
						textState = getResources().getString(
								R.string.main_left_5c);
						drawImage = getResources().getDrawable(
								R.drawable.touch_mc_socre_on_chuyennghiep);
						break;
					default:
						break;
					}

					textPaint.setStyle(Style.FILL);
					textPaint.setTextSize(textStateS);
					textPaint.setColor(Color.parseColor(TEXT_COLOR));
					float width = textPaint.measureText(textState);
					canvas.drawText(textState,
							(float) (0.5 * widthLayout - 0.5 * width),
							textStateY, textPaint);

					drawImage.setBounds(rectImageSonca);
					drawImage.draw(canvas);

					textPaint.setStyle(Style.FILL);
					textPaint.setTextSize(titleS);
					textPaint.setColor(Color.parseColor(TEXT_COLOR));
					width = textPaint.measureText(textTitle);
					canvas.drawText(textTitle,
							(float) (0.5 * widthLayout - 0.5 * width), titleY,
							textPaint);

				} else {
					paintMain.setStyle(Style.STROKE);
					paintMain.setStrokeWidth(stroXamTrong);
					paintMain.setARGB(51, 255, 255, 255);
					canvas.drawCircle(centerX, centerY, radXamTrong, paintMain);

					paintMain.setStyle(Style.STROKE);
					paintMain.setStrokeWidth(stroXamNgoai);
					paintMain.setARGB(51, 255, 255, 255);
					canvas.drawCircle(centerX, centerY, radXamNgoai
							- stroXamNgoai, paintMain);

					textPaint.setStyle(Style.FILL);
					textPaint.setTextSize(titleS);
					textPaint.setColor(Color.GRAY);
					float width = textPaint.measureText(textTitle);
					canvas.drawText(textTitle,
							(float) (0.5 * widthLayout - 0.5 * width), titleY,
							textPaint);

					drawImage = getResources().getDrawable(
							R.drawable.touch_mc_socre_off_96x96_old);
					drawImage.setBounds(rectImageSonca);
					drawImage.draw(canvas);
				}

			} else {
				if (isTouch) {
					drawHover.setBounds(rectVien);
					drawHover.draw(canvas);
				} else {
					paintMain.setStyle(Style.STROKE);
					paintMain.setStrokeWidth(stroXamTrong);
					paintMain.setARGB(51, 255, 255, 255);
					canvas.drawCircle(centerX, centerY, radXamTrong, paintMain);

					drawActive.setBounds(rectVien);
					drawActive.draw(canvas);
				}
				textPaint.setStyle(Style.FILL);
				textPaint.setTextSize(titleS);
				textPaint.setColor(Color.parseColor(TEXT_COLOR));
				float width = textPaint.measureText(textTitle);
				canvas.drawText(textTitle,
						(float) (0.5 * widthLayout - 0.5 * width), titleY,
						textPaint);

				drawImage = getResources().getDrawable(
						R.drawable.touch_mc_socre_on_96x96);
				drawImage.setBounds(rectImageSonca);
				drawImage.draw(canvas);

			}
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			if (boolBlockComand == true) {
				zlight_drawXam.setBounds(rectVien);
				zlight_drawXam.draw(canvas);

				zlight_icon_xam.setBounds(rectImageSonca);
				zlight_icon_xam.draw(canvas);

				textPaint.setStyle(Style.FILL);
				textPaint.setTextSize(titleS);
				textPaint.setColor(colorTextXam);
				float width = textPaint.measureText(textTitle);
				canvas.drawText(textTitle,
						(float) (0.5 * widthLayout - 0.5 * width), titleY,
						textPaint);

				return;
			}

			boolean flag = false;
			if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				if (KTVMainActivity.serverStatus == null) {
					flag = true;
				}
			} else {
				if (TouchMainActivity.serverStatus == null) {
					flag = true;
				}
			}
			
			if (flag) {
				stateView = INVISIBLE;
			} else {
				stateView = VISIBLE;
			}

			if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && MyApplication.flagPlayingYouTube){
				stateView = INVISIBLE;
			}
			
			if (MyApplication.intWifiRemote == MyApplication.SONCA) {

				if (stateView == VISIBLE) {
					if (isTouch) {
						zlight_drawHover.setBounds(rectVien);
						zlight_drawHover.draw(canvas);
					} else {
						zlight_drawActive.setBounds(rectVien);
						zlight_drawActive.draw(canvas);
					}

					switch (isActive) {
					case 0:
						textState = getResources().getString(
								R.string.main_left_5a);
						drawImage = zlight_icon_off;
						break;
					case 1:
						textState = getResources().getString(
								R.string.main_left_5b);
						drawImage = zlight_icon_on;
						break;
					case 2:
						textState = getResources().getString(
								R.string.main_left_5c);
						drawImage = zlight_icon_chuyennghiep;
						break;
					default:
						break;
					}

					textPaint.setStyle(Style.FILL);
					textPaint.setTextSize(textStateS);
					textPaint.setColor(colorTextActive);
					float width = textPaint.measureText(textState);
					canvas.drawText(textState,
							(float) (0.5 * widthLayout - 0.5 * width),
							textStateY, textPaint);

					drawImage.setBounds(rectImageSonca);
					drawImage.draw(canvas);

					textPaint.setStyle(Style.FILL);
					textPaint.setTextSize(titleS);
					textPaint.setColor(colorTextActive);
					width = textPaint.measureText(textTitle);
					canvas.drawText(textTitle,
							(float) (0.5 * widthLayout - 0.5 * width), titleY,
							textPaint);

				} else {
					zlight_drawXam.setBounds(rectVien);
					zlight_drawXam.draw(canvas);

					zlight_icon_xam.setBounds(rectImageSonca);
					zlight_icon_xam.draw(canvas);

					textPaint.setStyle(Style.FILL);
					textPaint.setTextSize(titleS);
					textPaint.setColor(colorTextXam);
					float width = textPaint.measureText(textTitle);
					canvas.drawText(textTitle,
							(float) (0.5 * widthLayout - 0.5 * width), titleY,
							textPaint);
				}

			} else {
				if (isTouch) {
					zlight_drawHover.setBounds(rectVien);
					zlight_drawHover.draw(canvas);
				} else {
					zlight_drawActive.setBounds(rectVien);
					zlight_drawActive.draw(canvas);
				}
				textPaint.setStyle(Style.FILL);
				textPaint.setTextSize(titleS);
				textPaint.setColor(colorTextXam);
				float width = textPaint.measureText(textTitle);
				canvas.drawText(textTitle,
						(float) (0.5 * widthLayout - 0.5 * width), titleY,
						textPaint);

				zlight_icon_on.setBounds(rectImageSonca);
				zlight_icon_on.draw(canvas);

			}

		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
		int parentWidth = MeasureSpec.getSize(widthMeasureSpec);		
		if (parentHeight > parentWidth) {
			setMeasuredDimension(parentWidth, parentWidth);
		} else {
			setMeasuredDimension(parentHeight, parentHeight);
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (boolBlockComand == true) {
			return true;
		}
		if (MyApplication.intWifiRemote == MyApplication.SONCA) {

			if (stateView == View.VISIBLE) {
				if (Math.pow(event.getX() - getWidth() / 2, 2)
						+ Math.pow(event.getY() - getHeight() / 2, 2) > Math
							.pow(radXamNgoai, 2)) {
					if (isTouch) {
						isTouch = false;
						invalidate();
						return true;
					}
					return false;
				}
				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					isTouch = false;
					isActive += 1;
					if (isActive >= 3) {
						isActive = 0;
					}
					invalidate();
					longtimersync = System.currentTimeMillis();
					if (listener != null) {
						listener.onScore(isActive);
					}
					return true;
				case MotionEvent.ACTION_DOWN:
					isTouch = true;
					invalidate();
					return true;
				}
			} else {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (listener != null) {
						listener.OnInActive();
					}
				}
			}

			return true;

		} else {
			if (Math.pow(event.getX() - getWidth() / 2, 2)
					+ Math.pow(event.getY() - getHeight() / 2, 2) > Math.pow(
					radXamNgoai, 2)) {
				if (isTouch) {
					isTouch = false;
					invalidate();
					return true;
				}
				return false;
			}
			switch (event.getAction()) {
			case MotionEvent.ACTION_UP:
				isTouch = false;
				invalidate();
				if (listener != null) {
					listener.onScore(1);
				}
				return true;
			case MotionEvent.ACTION_DOWN:
				isTouch = true;
				invalidate();
				return true;
			}

			return true;
		}
	}

	public static final int INTCOMMAND = 512;
	public static void setCommandEnable(boolean bool){
		if (bool) {
			MyApplication.intCommandEnable |= INTCOMMAND;
		} else {
			MyApplication.intCommandEnable &= (~INTCOMMAND);
		}
	}
	
	public static boolean getCommandEnable(){
		return (MyApplication.intCommandEnable & INTCOMMAND) == INTCOMMAND;
	}
	
	public static final int INTMEDIUM = 16;
	public static void setCommandMedium(int value){
		int clear = 0x00000003;
		MyApplication.intCommandMedium &= (~(clear << INTMEDIUM));
		MyApplication.intCommandMedium |= (value << INTMEDIUM);
	}
	
	public static int getCommandMedium(){
		int clear = 0x00000003;
		int retur = (MyApplication.intCommandMedium & (clear << INTMEDIUM)) >> INTMEDIUM;
		return retur;
	}
	
}