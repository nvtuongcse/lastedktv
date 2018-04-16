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

public class DownVideoView extends View {

	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Path path = new Path();
	// private Context context;
	private Drawable activeBackground;
	private Drawable drawable;
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
	private static final String FILL_COLOR = "#03223f";
	private static final String STROKE_COLOR_1 = "#004e90";
	private static final String STROKE_COLOR_2 = "#264e67";
	private static final String TEXT_COLOR = "#b4feff";

	private OnDownVideoViewListener listener;

	public interface OnDownVideoViewListener {
		public void OnDownVideo();
	}

	public void setOnDownVideoViewListener(OnDownVideoViewListener listener) {
		this.listener = listener;
	}

	public DownVideoView(Context context) {
		super(context);
		initView(context);
	}

	public DownVideoView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public DownVideoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private boolean flagBigSize = false;
	public void setBigLayout(boolean flagBigSize){
		this.flagBigSize = flagBigSize;
		invalidate();
	}

	private Drawable drawBG;
	private Drawable activeBackgroundHover;
	
	private Drawable zlight_activeBackground, zlight_activeBackgroundHover,
			zlight_drawable, zlight_drawBG;
	
	private void initView(Context context) {
		// this.context = context;
		activeBackground = getResources().getDrawable(
				R.drawable.off_boder_vuong);
		activeBackgroundHover = getResources().getDrawable(
				R.drawable.cham_boder_vuong);
		drawable = getResources().getDrawable(R.drawable.mc_down_96x96);
		drawBG = getResources().getDrawable(R.drawable.videodown_inactive);

		text = getResources().getString(R.string.msg_kara_4);
		
		zlight_activeBackground = getResources().getDrawable(
				R.drawable.zlight_control_boder_active);
		zlight_activeBackgroundHover = getResources().getDrawable(
				R.drawable.zlight_control_boder_hover);
		zlight_drawable = getResources().getDrawable(R.drawable.zlight_mc_down_96x96);
		zlight_drawBG = getResources().getDrawable(
				R.drawable.zlight_videodown_inactive);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
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

		heightLayout = getHeight() * 1f;
		widthLayout = getHeight() * 1f * 4 / 3;

		STROKE_WIDTH = widthLayout / 100;
		PADDING = STROKE_WIDTH;

		drawableDY = heightLayout / 11;
		textDY = widthLayout / 6f;
		textScale = widthLayout / 12;
		backgroundWidth = 0.9f * widthLayout;
		backgroundHeight = 1.3f * heightLayout;
		drawableWidth = drawableHeight = 0.7f * heightLayout;		

		widthView = widthLayout - PADDING * 2;
		heightView = heightLayout - PADDING * 2;

		canvas.save();
		canvas.translate((getWidth() - widthLayout) / 2,
				(getHeight() - heightLayout) / 2);

		isConnected = TouchMainActivity.serverStatus != null || KTVMainActivity.serverStatus != null;

		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE  || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI || flagBigSize) { // THEME-BLUE
			if (boolBlockComand == true) {
				drawBG.setBounds(
						(int) ((widthLayout - backgroundWidth) / 2),
						(int) ((heightLayout - backgroundHeight) / 2),
						(int) ((widthLayout - backgroundWidth) / 2 + backgroundWidth),
						(int) ((heightLayout - backgroundHeight) / 2 + backgroundHeight));
				drawBG.draw(canvas);
				return;
			}

			if (MyApplication.intWifiRemote == MyApplication.SONCA) {
				if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
					if (KTVMainActivity.serverStatus != null) {
						isConnected = KTVMainActivity.serverStatus.getPlayingSongID() != 0;
					}
				} else {
					if (TouchMainActivity.serverStatus != null) {
						isConnected = TouchMainActivity.serverStatus.getPlayingSongID() != 0;
					}	
				}
				
			}
			
			isConnected = true;
			
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

				drawable.setBounds(
						(int) ((widthLayout - drawableWidth) / 2),
						(int) ((heightLayout - drawableHeight) / 2 - drawableDY),
						(int) ((widthLayout - drawableWidth) / 2 + drawableWidth),
						(int) ((heightLayout - drawableHeight) / 2
								+ drawableHeight - drawableDY));
				drawable.draw(canvas);

				mPaint.setTextSize(textScale);
				mPaint.setTypeface(Typeface.create(Typeface.DEFAULT,
						Typeface.BOLD));
				mPaint.setColor(Color.parseColor(TEXT_COLOR));
				Rect boundRect = new Rect();
				mPaint.getTextBounds(text, 0, text.length(), boundRect);
				canvas.drawText(text, widthLayout / 2 - boundRect.width() / 2,
						heightLayout / 2 + boundRect.height() / 2 + textDY,
						mPaint);

				resetPaint();
			} else {
				drawBG.setBounds(
						(int) ((widthLayout - backgroundWidth) / 2),
						(int) ((heightLayout - backgroundHeight) / 2),
						(int) ((widthLayout - backgroundWidth) / 2 + backgroundWidth),
						(int) ((heightLayout - backgroundHeight) / 2 + backgroundHeight));
				drawBG.draw(canvas);
			}
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) { // THEME-LIGHT
			if (boolBlockComand == true) {
				zlight_drawBG
						.setBounds(
								(int) ((widthLayout - backgroundWidth) / 2),
								(int) ((heightLayout - backgroundHeight) / 2),
								(int) ((widthLayout - backgroundWidth) / 2 + backgroundWidth),
								(int) ((heightLayout - backgroundHeight) / 2 + backgroundHeight));
				zlight_drawBG.draw(canvas);
				
				mPaint.setTextSize(textScale);
				mPaint.setTypeface(Typeface.create(Typeface.DEFAULT,
						Typeface.BOLD));
				mPaint.setARGB(178, 255, 255, 255);
				Rect boundRect = new Rect();
				mPaint.getTextBounds(text, 0, text.length(), boundRect);
				canvas.drawText(text, widthLayout / 2 - boundRect.width() / 2,
						heightLayout / 2 + boundRect.height() / 2 + textDY,
						mPaint);
				return;
			}

			if (MyApplication.intWifiRemote == MyApplication.SONCA) {
				if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
					if (KTVMainActivity.serverStatus != null) {
						isConnected = KTVMainActivity.serverStatus.getPlayingSongID() != 0;
					}
				} else {
					if (TouchMainActivity.serverStatus != null) {
						isConnected = TouchMainActivity.serverStatus.getPlayingSongID() != 0;
					}	
				}
				
			}
			
			isConnected = true;
			
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

				zlight_drawable.setBounds(
						(int) ((widthLayout - drawableWidth) / 2),
						(int) ((heightLayout - drawableHeight) / 2 - drawableDY),
						(int) ((widthLayout - drawableWidth) / 2 + drawableWidth),
						(int) ((heightLayout - drawableHeight) / 2
								+ drawableHeight - drawableDY));
				zlight_drawable.draw(canvas);

				mPaint.setTextSize(textScale);
				mPaint.setTypeface(Typeface.create(Typeface.DEFAULT,
						Typeface.BOLD));
				mPaint.setColor(Color.WHITE);
				Rect boundRect = new Rect();
				mPaint.getTextBounds(text, 0, text.length(), boundRect);
				canvas.drawText(text, widthLayout / 2 - boundRect.width() / 2,
						heightLayout / 2 + boundRect.height() / 2 + textDY,
						mPaint);

				resetPaint();
			} else {
				zlight_drawBG.setBounds(
						(int) ((widthLayout - backgroundWidth) / 2),
						(int) ((heightLayout - backgroundHeight) / 2),
						(int) ((widthLayout - backgroundWidth) / 2 + backgroundWidth),
						(int) ((heightLayout - backgroundHeight) / 2 + backgroundHeight));
				zlight_drawBG.draw(canvas);
				
				mPaint.setTextSize(textScale);
				mPaint.setTypeface(Typeface.create(Typeface.DEFAULT,
						Typeface.BOLD));
				mPaint.setARGB(178, 255, 255, 255);
				Rect boundRect = new Rect();
				mPaint.getTextBounds(text, 0, text.length(), boundRect);
				canvas.drawText(text, widthLayout / 2 - boundRect.width() / 2,
						heightLayout / 2 + boundRect.height() / 2 + textDY,
						mPaint);
			}

		}		
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(boolBlockComand == true){
			return true;
		}
		
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
				listener.OnDownVideo();
			}
			return true;
		case MotionEvent.ACTION_DOWN:
			isTouch = true;
			invalidate();
			return true;
		}
		
		return true;
	}

	private void resetPaint() {
		mPaint.reset();
		mPaint.setAntiAlias(true);
	}
	
	
	public static final int INTCOMMAND = 2048;
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
	
	public static final int INTMEDIUM = 20;
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