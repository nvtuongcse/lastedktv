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
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TouchPauseView extends View {

	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Path path = new Path();
	// private Context context;
	private Drawable activeBackground;
	private Drawable pauseDrawable;
	private Drawable playDrawable;
	private String pauseText;
	private String playText;
	private String pauseplayText;

	private float widthLayout;
	private float heightLayout;
	private float widthView;
	private float heightView;

	private boolean isTouch = false;
	private boolean isPlaying = true;

	private float backgroundWidth;
	private float backgroundHeight;
	private float drawableWidth;
	private float drawableHeight;

	private float drawableDY;
	private float textDY;
	private float textScale;

	private float STROKE_WIDTH;
	private float PADDING;
	private static final String FILL_COLOR = "#03223f";
	private static final String STROKE_COLOR_1 = "#004e90";
	private static final String STROKE_COLOR_2 = "#264e67";
	private static final String TEXT_COLOR = "#b4feff";

	private OnPauseListener listener;

	public interface OnPauseListener {
		public void onPause(boolean isPlaying);
		public void OnInActive();
	}

	public void setOnPauseListener(OnPauseListener listener) {
		this.listener = listener;
	}

	public TouchPauseView(Context context) {
		super(context);
		initView(context);
	}

	public TouchPauseView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchPauseView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private long longtimersync = 0;
	public boolean setPauseView(boolean isActive) {
		if(MyApplication.intWifiRemote == MyApplication.SONCA){
			if(System.currentTimeMillis() - longtimersync 
				<= MyApplication.TIMER_SYNC){
				return false;
			}
			this.isPlaying = isActive;
		} else {
			this.isPlaying = false;
		}
		invalidate();
		return true;
	}

	public boolean getPauseView() {
		return isPlaying;
	}
	
	private boolean flagBigSize = false;
	public void setBigLayout(boolean flagBigSize){
		this.flagBigSize = flagBigSize;
		invalidate();
	}

	private Drawable drawBG;
	private Drawable activeBackgroundHover;
	
	private Drawable zlight_activeBackground, zlight_activeBackgroundHover,
			zlight_pauseDrawable, zlight_playDrawable, zlight_drawBG, zlight_pauseplayDrawable;

	private Drawable pauseplayDrawable;
	
	private void initView(Context context) {
		// this.context = context;

		activeBackground = getResources().getDrawable(
				R.drawable.off_boder_vuong);
		activeBackgroundHover = getResources().getDrawable(
				R.drawable.cham_boder_vuong);
		pauseDrawable = getResources().getDrawable(R.drawable.touch_mc_pause_96x96);
		playDrawable = getResources().getDrawable(R.drawable.touch_mc_play_96x96);
		pauseplayDrawable = getResources().getDrawable(
				R.drawable.touch_mc_pauseplay_96x96);

		drawBG = getResources().getDrawable(R.drawable.touch_pause_inactive);

		pauseText = getResources().getString(R.string.main_left_6b);
		playText = getResources().getString(R.string.main_left_6a);
		pauseplayText = "PLAY/PAUSE";

		zlight_activeBackground = getResources().getDrawable(
				R.drawable.zlight_control_boder_active);
		zlight_activeBackgroundHover = getResources().getDrawable(
				R.drawable.zlight_control_boder_hover);
		zlight_pauseDrawable = getResources().getDrawable(
				R.drawable.zlight_mc_pause);
		zlight_playDrawable = getResources().getDrawable(
				R.drawable.zlight_mc_play);
		zlight_drawBG = getResources().getDrawable(
				R.drawable.zlight_pause_inactive);
		zlight_pauseplayDrawable = getResources().getDrawable(
				R.drawable.zlight_mc_pauseplay);
	}

	private boolean isConnected;
	public void setConnect(boolean isConnected){
		this.isConnected = isConnected;
		invalidate();
	}
	
	private boolean boolBlockComand = false;
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if (MyApplication.intWifiRemote != MyApplication.SONCA) {
			isPlaying = false;
		}

		heightLayout = getHeight() * 1f;
		widthLayout = getHeight() * 1f * 4 / 3;

		STROKE_WIDTH = widthLayout / 100;
		PADDING = STROKE_WIDTH;

		drawableDY = heightLayout / 11;
		textDY = widthLayout / 7f;
		textScale = widthLayout / 10;
		backgroundWidth = 0.9f * widthLayout;
		backgroundHeight = 1.3f * heightLayout;
		drawableWidth = drawableHeight = 0.7f * heightLayout;

		widthView = widthLayout - PADDING * 2;
		heightView = heightLayout - PADDING * 2;

		canvas.save();
		canvas.translate((getWidth() - widthLayout) / 2,
				(getHeight() - heightLayout) / 2);

		isConnected = TouchMainActivity.serverStatus != null || KTVMainActivity.serverStatus != null;
		
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

		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI || flagBigSize) { // THEME-BLUE
			if (boolBlockComand == true) {
				drawBG.setBounds(
						(int) ((widthLayout - backgroundWidth) / 2),
						(int) ((heightLayout - backgroundHeight) / 2),
						(int) ((widthLayout - backgroundWidth) / 2 + backgroundWidth),
						(int) ((heightLayout - backgroundHeight) / 2 + backgroundHeight));
				drawBG.draw(canvas);
				return;
			}

			if (TouchMainActivity.serverStatus != null || KTVMainActivity.serverStatus != null) {
				boolean flag = false;
				if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
					if (KTVMainActivity.serverStatus.getPlayingSongID() == 0) {
						flag = true;
					}
				} else {
					if (TouchMainActivity.serverStatus.getPlayingSongID() == 0) {
						flag = true;
					}
				}
				
				if (flag) {
					isConnected = false;
				} else {
					isConnected = true;
				}
			}
			
			if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && MyApplication.flagPlayingYouTube){
				isConnected = true;
			}

			if (MyApplication.intWifiRemote == MyApplication.SONCA_KARTROL) {
				isConnected = true;
			}

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

				if (isPlaying) {
					if (MyApplication.intWifiRemote == MyApplication.SONCA) {
						pauseDrawable
								.setBounds(
										(int) ((widthLayout - drawableWidth) / 2),
										(int) ((heightLayout - drawableHeight) / 2 - drawableDY),
										(int) ((widthLayout - drawableWidth) / 2 + drawableWidth),
										(int) ((heightLayout - drawableHeight)
												/ 2 + drawableHeight - drawableDY));
						pauseDrawable.draw(canvas);
					}

					resetPaint();

					mPaint.setTextSize(textScale);
					mPaint.setTypeface(Typeface.create(Typeface.DEFAULT,
							Typeface.BOLD));
					mPaint.setColor(Color.parseColor(TEXT_COLOR));
					Rect boundRect = new Rect();
					mPaint.getTextBounds(pauseText, 0, pauseText.length(),
							boundRect);
					canvas.drawText(pauseText,
							widthLayout / 2 - boundRect.width() / 2,
							heightLayout / 2 + boundRect.height() / 2 + textDY,
							mPaint);
				} else {
					if (MyApplication.intWifiRemote == MyApplication.SONCA) {
						playDrawable
								.setBounds(
										(int) ((widthLayout - drawableWidth) / 2),
										(int) ((heightLayout - drawableHeight) / 2 - drawableDY),
										(int) ((widthLayout - drawableWidth) / 2 + drawableWidth),
										(int) ((heightLayout - drawableHeight)
												/ 2 + drawableHeight - drawableDY));
						playDrawable.draw(canvas);

						resetPaint();
						mPaint.setTextSize(textScale);
						mPaint.setTypeface(Typeface.create(Typeface.DEFAULT,
								Typeface.BOLD));
						mPaint.setColor(Color.parseColor(TEXT_COLOR));
						Rect boundRect = new Rect();
						mPaint.getTextBounds(playText, 0, playText.length(),
								boundRect);
						canvas.drawText(playText,
								widthLayout / 2 - boundRect.width() / 2,
								heightLayout / 2 + boundRect.height() / 2
										+ textDY, mPaint);
					} else {
						pauseplayDrawable
								.setBounds(
										(int) ((widthLayout - drawableWidth) / 2),
										(int) ((heightLayout - drawableHeight) / 2 - drawableDY),
										(int) ((widthLayout - drawableWidth) / 2 + drawableWidth),
										(int) ((heightLayout - drawableHeight)
												/ 2 + drawableHeight - drawableDY));
						pauseplayDrawable.draw(canvas);

						resetPaint();
						mPaint.setTextSize(textScale);
						mPaint.setTypeface(Typeface.create(Typeface.DEFAULT,
								Typeface.BOLD));
						mPaint.setColor(Color.parseColor(TEXT_COLOR));
						Rect boundRect = new Rect();
						mPaint.getTextBounds(pauseplayText, 0,
								pauseplayText.length(), boundRect);
						canvas.drawText(pauseplayText, widthLayout / 2
								- boundRect.width() / 2, heightLayout / 2
								+ boundRect.height() / 2 + textDY, mPaint);
					}
				}
				canvas.restore();
			} else {
				drawBG.setBounds(
						(int) ((widthLayout - backgroundWidth) / 2),
						(int) ((heightLayout - backgroundHeight) / 2),
						(int) ((widthLayout - backgroundWidth) / 2 + backgroundWidth),
						(int) ((heightLayout - backgroundHeight) / 2 + backgroundHeight));
				drawBG.draw(canvas);
			}

			canvas.restore();
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) { // THEME-LIGHT
			if (boolBlockComand == true) {
				zlight_drawBG
						.setBounds(
								(int) ((widthLayout - backgroundWidth) / 2),
								(int) ((heightLayout - backgroundHeight) / 2),
								(int) ((widthLayout - backgroundWidth) / 2 + backgroundWidth),
								(int) ((heightLayout - backgroundHeight) / 2 + backgroundHeight));
				zlight_drawBG.draw(canvas);

				resetPaint();
				mPaint.setTextSize(textScale);
				mPaint.setTypeface(Typeface.create(Typeface.DEFAULT,
						Typeface.BOLD));
				mPaint.setARGB(178, 255, 255, 255);
				Rect boundRect = new Rect();
				mPaint.getTextBounds(pauseplayText, 0, pauseplayText.length(),
						boundRect);
				canvas.drawText(pauseplayText,
						widthLayout / 2 - boundRect.width() / 2, heightLayout
								/ 2 + boundRect.height() / 2 + textDY, mPaint);
				return;
			}

			if (TouchMainActivity.serverStatus != null) {
				if (TouchMainActivity.serverStatus.getPlayingSongID() != 0) {
					isConnected = true;
				} else {
					isConnected = false;
				}
			}
			
			if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && MyApplication.flagPlayingYouTube){
				isConnected = true;
			}

			if (MyApplication.intWifiRemote == MyApplication.SONCA_KARTROL) {
				isConnected = true;
			}

			if (isConnected) {

				if (isTouch) {
					zlight_activeBackgroundHover
							.setBounds(
									(int) ((widthLayout - backgroundWidth) / 2),
									(int) ((heightLayout - backgroundHeight) / 2),
									(int) ((widthLayout - backgroundWidth) / 2 + backgroundWidth),
									(int) ((heightLayout - backgroundHeight) / 2 + backgroundHeight));
					zlight_activeBackgroundHover.draw(canvas);
				} else {
					zlight_activeBackground
							.setBounds(
									(int) ((widthLayout - backgroundWidth) / 2),
									(int) ((heightLayout - backgroundHeight) / 2),
									(int) ((widthLayout - backgroundWidth) / 2 + backgroundWidth),
									(int) ((heightLayout - backgroundHeight) / 2 + backgroundHeight));
					zlight_activeBackground.draw(canvas);
				}

				if (isPlaying) {
					if (MyApplication.intWifiRemote == MyApplication.SONCA) {
						zlight_pauseDrawable
								.setBounds(
										(int) ((widthLayout - drawableWidth) / 2),
										(int) ((heightLayout - drawableHeight) / 2 - drawableDY),
										(int) ((widthLayout - drawableWidth) / 2 + drawableWidth),
										(int) ((heightLayout - drawableHeight)
												/ 2 + drawableHeight - drawableDY));
						zlight_pauseDrawable.draw(canvas);
					}

					resetPaint();

					mPaint.setTextSize(textScale);
					mPaint.setTypeface(Typeface.create(Typeface.DEFAULT,
							Typeface.BOLD));
					mPaint.setColor(Color.WHITE);
					Rect boundRect = new Rect();
					mPaint.getTextBounds(pauseText, 0, pauseText.length(),
							boundRect);
					canvas.drawText(pauseText,
							widthLayout / 2 - boundRect.width() / 2,
							heightLayout / 2 + boundRect.height() / 2 + textDY,
							mPaint);
				} else {
					if (MyApplication.intWifiRemote == MyApplication.SONCA) {
						zlight_playDrawable
								.setBounds(
										(int) ((widthLayout - drawableWidth) / 2),
										(int) ((heightLayout - drawableHeight) / 2 - drawableDY),
										(int) ((widthLayout - drawableWidth) / 2 + drawableWidth),
										(int) ((heightLayout - drawableHeight)
												/ 2 + drawableHeight - drawableDY));
						zlight_playDrawable.draw(canvas);

						resetPaint();
						mPaint.setTextSize(textScale);
						mPaint.setTypeface(Typeface.create(Typeface.DEFAULT,
								Typeface.BOLD));
						mPaint.setColor(Color.WHITE);
						Rect boundRect = new Rect();
						mPaint.getTextBounds(playText, 0, playText.length(),
								boundRect);
						canvas.drawText(playText,
								widthLayout / 2 - boundRect.width() / 2,
								heightLayout / 2 + boundRect.height() / 2
										+ textDY, mPaint);
					} else {
						zlight_pauseplayDrawable
								.setBounds(
										(int) ((widthLayout - drawableWidth) / 2),
										(int) ((heightLayout - drawableHeight) / 2 - drawableDY),
										(int) ((widthLayout - drawableWidth) / 2 + drawableWidth),
										(int) ((heightLayout - drawableHeight)
												/ 2 + drawableHeight - drawableDY));
						zlight_pauseplayDrawable.draw(canvas);

						resetPaint();
						mPaint.setTextSize(textScale);
						mPaint.setTypeface(Typeface.create(Typeface.DEFAULT,
								Typeface.BOLD));
						mPaint.setColor(Color.WHITE);
						Rect boundRect = new Rect();
						mPaint.getTextBounds(pauseplayText, 0,
								pauseplayText.length(), boundRect);
						canvas.drawText(pauseplayText, widthLayout / 2
								- boundRect.width() / 2, heightLayout / 2
								+ boundRect.height() / 2 + textDY, mPaint);
					}
				}
				canvas.restore();
			} else {
				zlight_drawBG
						.setBounds(
								(int) ((widthLayout - backgroundWidth) / 2),
								(int) ((heightLayout - backgroundHeight) / 2),
								(int) ((widthLayout - backgroundWidth) / 2 + backgroundWidth),
								(int) ((heightLayout - backgroundHeight) / 2 + backgroundHeight));
				zlight_drawBG.draw(canvas);

				resetPaint();
				mPaint.setTextSize(textScale);
				mPaint.setTypeface(Typeface.create(Typeface.DEFAULT,
						Typeface.BOLD));
				mPaint.setARGB(178, 255, 255, 255);
				Rect boundRect = new Rect();
				mPaint.getTextBounds(pauseplayText, 0, pauseplayText.length(),
						boundRect);
				canvas.drawText(pauseplayText,
						widthLayout / 2 - boundRect.width() / 2, heightLayout
								/ 2 + boundRect.height() / 2 + textDY, mPaint);
			}

			canvas.restore();
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(boolBlockComand == true){
			return true;
		}
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
				isPlaying = !isPlaying;
				invalidate();
				longtimersync = System.currentTimeMillis();
				if (listener != null) {
					listener.onPause(isPlaying);
				}
				return true;
			case MotionEvent.ACTION_DOWN:
				isTouch = true;
				invalidate();
				return true;
			}
		} else {
			if(event.getAction() == MotionEvent.ACTION_UP){
				if(listener != null){
					listener.OnInActive();
				}
			}
		}
		return true;
	}

	private void resetPaint() {
		mPaint.reset();
		mPaint.setAntiAlias(true);
	}
	
	public static final int INTCOMMAND = 8192;
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
	
	public static final int INTMEDIUM = 24;
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