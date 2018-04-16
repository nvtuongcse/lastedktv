package vn.com.sonca.Touch.CustomView;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.zktv.main.KTVMainActivity;
import vn.com.sonca.zzzzz.MyApplication;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TouchDanceView extends View {

	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Path path = new Path();
	// private Context context;
	private String text;

	private float widthLayout;
	private float heightLayout;
	private float widthView;
	private float heightView;

	private boolean isTouch = false;

	private float backgroundWidth;
	private float backgroundHeight;
	private float drawableWidth;
	private float drawableHeight;

	private float drawableDY;
	private float textDY;
	private float textScale;

	private float STROKE_WIDTH;
	private float PADDING;
	private static final String TEXT_COLOR = "#b4feff";

	private OnDancetListener listener;

	public interface OnDancetListener {
		public void OnClick();
		public void OnInActive();
	}

	public void setOnDancetListener(OnDancetListener listener) {
		this.listener = listener;
	}

	public TouchDanceView(Context context) {
		super(context);
		initView(context);
	}

	public TouchDanceView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchDanceView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Drawable activeBackground;
	private Drawable activeBackgroundHover;
	private Drawable drawBG, drawDanceImage, drawDanceImageOff;
	private Drawable drawDanceMicro, drawDanceMicroOff;
	
	/*
	 * SETTING THEME-LIGHT
	 */
	private Drawable zlight_drawBG_active, zlight_drawBG_hover,
			zlight_drawBG_xam, zlight_drawDance, zlight_drawDance_xam,
			zlight_drawMicro, zlight_drawMicro_xam;

	private void initView(Context context) {
		// this.context = context;
		activeBackground = getResources().getDrawable(
				R.drawable.off_boder_vuong);
		activeBackgroundHover = getResources().getDrawable(
				R.drawable.cham_boder_vuong);
		drawBG = getResources().getDrawable(
				R.drawable.image_boder_next_inactive);
		drawDanceImage = getResources().getDrawable(R.drawable.image_dance);
		drawDanceImageOff = getResources().getDrawable(
				R.drawable.image_dance_xam);
		drawDanceMicro = getResources().getDrawable(R.drawable.micro_karaoke);
		drawDanceMicroOff = getResources().getDrawable(R.drawable.karaoke_xam);

		text = "DANCE";

		zlight_drawBG_active = getResources().getDrawable(
				R.drawable.zlight_control_boder_active);
		zlight_drawBG_hover = getResources().getDrawable(
				R.drawable.zlight_control_boder_hover);
		zlight_drawBG_xam = getResources().getDrawable(
				R.drawable.zlight_control_boder_xam);
		zlight_drawDance = getResources().getDrawable(R.drawable.zlight_dance_icon_karaoke);
		zlight_drawDance_xam = getResources().getDrawable(R.drawable.zlight_dance_xam);
		zlight_drawMicro = getResources().getDrawable(R.drawable.zlight_dance_icon_micro);
		zlight_drawMicro_xam = getResources().getDrawable(R.drawable.zlight_dance_karaoke_xam);
	}

	public void setTextName(String text) {
		this.text = text;
	}

	private boolean isConnected;
	public void setEnableView(boolean isConnected){
		this.isConnected = isConnected;
		invalidate();
	}

	private boolean boolBlockComand = false;
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		isConnected = TouchMainActivity.serverStatus != null || KTVMainActivity.serverStatus != null;

		heightLayout = getHeight() * 1f;
		widthLayout = getHeight() * 1f * 4 / 3;

		STROKE_WIDTH = widthLayout / 100;
		PADDING = STROKE_WIDTH;

		drawableDY = heightLayout / 10;
		textDY = widthLayout / 7;
		textScale = widthLayout / 7;
		backgroundWidth = 0.9f * widthLayout;
		backgroundHeight = 1.3f * heightLayout;
		drawableWidth = drawableHeight = 0.7f * heightLayout;

		widthView = widthLayout - PADDING * 2;
		heightView = heightLayout - PADDING * 2;

		canvas.save();
		canvas.translate((getWidth() - widthLayout) / 2,
				(getHeight() - heightLayout) / 2);
		
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
		
		if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && (MyApplication.flagSmartK_801 || MyApplication.flagSmartK_CB)){
			boolBlockComand = true;
		}
		
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) { // THEME-BLUE			
			if (boolBlockComand == true) {
				drawBG.setBounds(
						(int) ((widthLayout - backgroundWidth) / 2),
						(int) ((heightLayout - backgroundHeight) / 2),
						(int) ((widthLayout - backgroundWidth) / 2 + backgroundWidth),
						(int) ((heightLayout - backgroundHeight) / 2 + backgroundHeight));
				drawBG.draw(canvas);

				if (text.equals("DANCE")) {
					drawDanceImageOff
							.setBounds(
									(int) ((widthLayout - backgroundWidth) / 2),
									(int) ((heightLayout - backgroundHeight) / 2),
									(int) ((widthLayout - backgroundWidth) / 2 + backgroundWidth),
									(int) ((heightLayout - backgroundHeight) / 2 + backgroundHeight));
					drawDanceImageOff.draw(canvas);
				} else {
					drawDanceMicroOff
							.setBounds(
									(int) ((widthLayout - backgroundWidth) / 2),
									(int) ((heightLayout - backgroundHeight) / 2),
									(int) ((widthLayout - backgroundWidth) / 2 + backgroundWidth),
									(int) ((heightLayout - backgroundHeight) / 2 + backgroundHeight));
					drawDanceMicroOff.draw(canvas);
				}

				return;
			}

			if (MyApplication.intWifiRemote == MyApplication.SONCA) {
				if (isConnected) {
					if (isTouch) {
						activeBackgroundHover
								.setBounds(
										(int) ((widthLayout - backgroundWidth) / 2),
										(int) ((heightLayout - backgroundHeight) / 2),
										(int) ((widthLayout - backgroundWidth) / 2 + backgroundWidth),
										(int) ((heightLayout - backgroundHeight) / 2 + backgroundHeight));
						activeBackgroundHover.draw(canvas);
					} else {
						activeBackground
								.setBounds(
										(int) ((widthLayout - backgroundWidth) / 2),
										(int) ((heightLayout - backgroundHeight) / 2),
										(int) ((widthLayout - backgroundWidth) / 2 + backgroundWidth),
										(int) ((heightLayout - backgroundHeight) / 2 + backgroundHeight));
						activeBackground.draw(canvas);
					}

					if (text.equals("DANCE")) {
						drawDanceImage
								.setBounds(
										(int) ((widthLayout - backgroundWidth) / 2),
										(int) ((heightLayout - backgroundHeight) / 2),
										(int) ((widthLayout - backgroundWidth) / 2 + backgroundWidth),
										(int) ((heightLayout - backgroundHeight) / 2 + backgroundHeight));
						drawDanceImage.draw(canvas);
					} else {
						drawDanceMicro
								.setBounds(
										(int) ((widthLayout - backgroundWidth) / 2),
										(int) ((heightLayout - backgroundHeight) / 2),
										(int) ((widthLayout - backgroundWidth) / 2 + backgroundWidth),
										(int) ((heightLayout - backgroundHeight) / 2 + backgroundHeight));
						drawDanceMicro.draw(canvas);

						resetPaint();
						mPaint.setTextSize(textScale);
						mPaint.setColor(Color.parseColor(TEXT_COLOR));
						Rect boundRect = new Rect();
						mPaint.getTextBounds(text, 0, text.length(), boundRect);
						canvas.drawText(text,
								widthLayout / 2 - boundRect.width() / 2,
								(float) (0.6 * heightLayout), mPaint);

						canvas.restore();
					}
				} else {
					drawBG.setBounds(
							(int) ((widthLayout - backgroundWidth) / 2),
							(int) ((heightLayout - backgroundHeight) / 2),
							(int) ((widthLayout - backgroundWidth) / 2 + backgroundWidth),
							(int) ((heightLayout - backgroundHeight) / 2 + backgroundHeight));
					drawBG.draw(canvas);

					drawDanceImageOff
							.setBounds(
									(int) ((widthLayout - backgroundWidth) / 2),
									(int) ((heightLayout - backgroundHeight) / 2),
									(int) ((widthLayout - backgroundWidth) / 2 + backgroundWidth),
									(int) ((heightLayout - backgroundHeight) / 2 + backgroundHeight));
					drawDanceImageOff.draw(canvas);

					resetPaint();
					// mPaint.setTextSize(textScale);
					// mPaint.setColor(Color.GRAY);
					// Rect boundRect = new Rect();
					// mPaint.getTextBounds(text, 0, text.length(), boundRect);
					// canvas.drawText(text, widthLayout / 2 - boundRect.width()
					// /
					// 2,
					// (float) (0.6 * heightLayout), mPaint);

					canvas.restore();
				}
			} else {
				drawBG.setBounds(
						(int) ((widthLayout - backgroundWidth) / 2),
						(int) ((heightLayout - backgroundHeight) / 2),
						(int) ((widthLayout - backgroundWidth) / 2 + backgroundWidth),
						(int) ((heightLayout - backgroundHeight) / 2 + backgroundHeight));
				drawBG.draw(canvas);

				drawDanceImageOff
						.setBounds(
								(int) ((widthLayout - backgroundWidth) / 2),
								(int) ((heightLayout - backgroundHeight) / 2),
								(int) ((widthLayout - backgroundWidth) / 2 + backgroundWidth),
								(int) ((heightLayout - backgroundHeight) / 2 + backgroundHeight));
				drawDanceImageOff.draw(canvas);

				resetPaint();
				// mPaint.setTextSize(textScale);
				// mPaint.setColor(Color.GRAY);
				// Rect boundRect = new Rect();
				// mPaint.getTextBounds(text, 0, text.length(), boundRect);
				// canvas.drawText(text, widthLayout / 2 - boundRect.width() /
				// 2,
				// (float) (0.6 * heightLayout), mPaint);

				canvas.restore();
			}
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) { // THEME-LIGHT			
			if (boolBlockComand == true) {
				if (text.equals("DANCE")) {
					zlight_drawDance_xam
							.setBounds(
									(int) ((widthLayout - backgroundWidth) / 2),
									(int) ((heightLayout - backgroundHeight) / 2),
									(int) ((widthLayout - backgroundWidth) / 2 + backgroundWidth),
									(int) ((heightLayout - backgroundHeight) / 2 + backgroundHeight));
					zlight_drawDance_xam.draw(canvas);
				} else {
					zlight_drawMicro_xam
							.setBounds(
									(int) ((widthLayout - backgroundWidth) / 2),
									(int) ((heightLayout - backgroundHeight) / 2),
									(int) ((widthLayout - backgroundWidth) / 2 + backgroundWidth),
									(int) ((heightLayout - backgroundHeight) / 2 + backgroundHeight));
					zlight_drawMicro_xam.draw(canvas);
				}

				return;
			}

			if (MyApplication.intWifiRemote == MyApplication.SONCA) {
				if (isConnected) {
					if (isTouch) {
						zlight_drawBG_hover
								.setBounds(
										(int) ((widthLayout - backgroundWidth) / 2),
										(int) ((heightLayout - backgroundHeight) / 2),
										(int) ((widthLayout - backgroundWidth) / 2 + backgroundWidth),
										(int) ((heightLayout - backgroundHeight) / 2 + backgroundHeight));
						zlight_drawBG_hover.draw(canvas);
					} else {
						zlight_drawBG_active
								.setBounds(
										(int) ((widthLayout - backgroundWidth) / 2),
										(int) ((heightLayout - backgroundHeight) / 2),
										(int) ((widthLayout - backgroundWidth) / 2 + backgroundWidth),
										(int) ((heightLayout - backgroundHeight) / 2 + backgroundHeight));
						zlight_drawBG_active.draw(canvas);
					}

					if (text.equals("DANCE")) {
						zlight_drawDance
								.setBounds(
										(int) ((widthLayout - backgroundWidth) / 2),
										(int) ((heightLayout - backgroundHeight) / 2),
										(int) ((widthLayout - backgroundWidth) / 2 + backgroundWidth),
										(int) ((heightLayout - backgroundHeight) / 2 + backgroundHeight));
						zlight_drawDance.draw(canvas);
					} else {
						zlight_drawMicro
								.setBounds(
										(int) ((widthLayout - backgroundWidth) / 2),
										(int) ((heightLayout - backgroundHeight) / 2),
										(int) ((widthLayout - backgroundWidth) / 2 + backgroundWidth),
										(int) ((heightLayout - backgroundHeight) / 2 + backgroundHeight));
						zlight_drawMicro.draw(canvas);

						resetPaint();
						mPaint.setTextSize(textScale);
						mPaint.setColor(Color.WHITE);
						Rect boundRect = new Rect();
						mPaint.getTextBounds(text, 0, text.length(), boundRect);
						canvas.drawText(text,
								widthLayout / 2 - boundRect.width() / 2,
								(float) (0.6 * heightLayout), mPaint);

						canvas.restore();
					}
				} else {
					zlight_drawDance_xam
							.setBounds(
									(int) ((widthLayout - backgroundWidth) / 2),
									(int) ((heightLayout - backgroundHeight) / 2),
									(int) ((widthLayout - backgroundWidth) / 2 + backgroundWidth),
									(int) ((heightLayout - backgroundHeight) / 2 + backgroundHeight));
					zlight_drawDance_xam.draw(canvas);

					resetPaint();
					// mPaint.setTextSize(textScale);
					// mPaint.setColor(Color.GRAY);
					// Rect boundRect = new Rect();
					// mPaint.getTextBounds(text, 0, text.length(), boundRect);
					// canvas.drawText(text, widthLayout / 2 - boundRect.width()
					// /
					// 2,
					// (float) (0.6 * heightLayout), mPaint);

					canvas.restore();
				}
			} else {
				zlight_drawDance_xam
						.setBounds(
								(int) ((widthLayout - backgroundWidth) / 2),
								(int) ((heightLayout - backgroundHeight) / 2),
								(int) ((widthLayout - backgroundWidth) / 2 + backgroundWidth),
								(int) ((heightLayout - backgroundHeight) / 2 + backgroundHeight));
				zlight_drawDance_xam.draw(canvas);

				resetPaint();
				// mPaint.setTextSize(textScale);
				// mPaint.setColor(Color.GRAY);
				// Rect boundRect = new Rect();
				// mPaint.getTextBounds(text, 0, text.length(), boundRect);
				// canvas.drawText(text, widthLayout / 2 - boundRect.width() /
				// 2,
				// (float) (0.6 * heightLayout), mPaint);

				canvas.restore();
			}
		}

	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (boolBlockComand == true) {
			return true;
		}
		if (MyApplication.intWifiRemote == MyApplication.SONCA) {
			if (isConnected) {
				if (event.getX() < (getWidth() - widthLayout) / 2
						|| (getWidth() - event.getX()) < (getWidth() - widthLayout) / 2
						|| event.getY() < (getHeight() - heightLayout) / 2
						|| (getHeight() - event.getY()) < (getHeight() - heightLayout) / 2) {
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
						listener.OnClick();
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
			return true;
		}
	}

	private void resetPaint() {
		mPaint.reset();
		mPaint.setAntiAlias(true);
	}

	private static final int INTCOMMAND = 4096;

	public static void setCommandEnable(boolean bool) {
		if (bool) {
			MyApplication.intCommandEnable |= INTCOMMAND;
		} else {
			MyApplication.intCommandEnable &= (~INTCOMMAND);
		}
	}

	public static boolean getCommandEnable() {
		return (MyApplication.intCommandEnable & INTCOMMAND) == INTCOMMAND;
	}

	public static final int INTMEDIUM = 22;

	public static void setCommandMedium(int value) {
		int clear = 0x00000003;
		MyApplication.intCommandMedium &= (~(clear << INTMEDIUM));
		MyApplication.intCommandMedium |= (value << INTMEDIUM);
	}

	public static int getCommandMedium() {
		int clear = 0x00000003;
		int retur = (MyApplication.intCommandMedium & (clear << INTMEDIUM)) >> INTMEDIUM;
		return retur;
	}
}