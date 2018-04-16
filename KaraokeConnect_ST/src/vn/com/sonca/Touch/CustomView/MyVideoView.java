package vn.com.sonca.Touch.CustomView;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.zktv.main.KTVMainActivity;
import vn.com.sonca.zzzzz.MyApplication;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MyVideoView extends View {

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

	private OnMyVideoViewListener listener;

	public interface OnMyVideoViewListener {
		public void OnClick();

		public void OnInActive();
	}

	public void setOnMyVideoViewListener(OnMyVideoViewListener listener) {
		this.listener = listener;
	}

	public MyVideoView(Context context) {
		super(context);
		initView(context);
	}

	public MyVideoView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public MyVideoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private Drawable activeBackground;
	private Drawable activeBackgroundHover;
	private Drawable drawBG, drawVideoImage, drawVideoImageOff;

	/*
	 * SETTING THEME-LIGHT
	 */
	private Drawable zlight_drawBG_active, zlight_drawBG_hover, zlight_drawVideo, zlight_drawVideo_xam;

	private void initView(Context context) {
		// this.context = context;
		activeBackground = getResources().getDrawable(
				R.drawable.off_boder_vuong);
		activeBackgroundHover = getResources().getDrawable(
				R.drawable.cham_boder_vuong);
		drawBG = getResources().getDrawable(
				R.drawable.image_boder_next_inactive);
		drawVideoImage = getResources().getDrawable(R.drawable.image_video);
		drawVideoImageOff = getResources().getDrawable(
				R.drawable.image_video_xam);

		text = "WIFI VIDEO";

		zlight_drawBG_active = getResources().getDrawable(
				R.drawable.zlight_control_boder_active);
		zlight_drawBG_hover = getResources().getDrawable(
				R.drawable.zlight_control_boder_hover);
		zlight_drawVideo = getResources().getDrawable(R.drawable.zlight_dance_icon_video);
		zlight_drawVideo_xam = getResources().getDrawable(R.drawable.zlight_video_xam);
	}

	public void setTextName(String text) {
		this.text = text;
	}

	private boolean isConnected;

	public void setEnableView(boolean isConnected) {
		this.isConnected = isConnected;
		invalidate();
	}
	
	private boolean boolBlockComand = false;

	public boolean getBlockStatus(){
		return this.boolBlockComand;
	}
	
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
		textDY = widthLayout / 5f;
		textScale = widthLayout / 11;
		backgroundWidth = 0.9f * widthLayout;
		backgroundHeight = 1.3f * heightLayout;
		drawableWidth = drawableHeight = 0.7f * heightLayout;

		widthView = widthLayout - PADDING * 2;
		heightView = heightLayout - PADDING * 2;

		canvas.save();
		canvas.translate((getWidth() - widthLayout) / 2,
				(getHeight() - heightLayout) / 2);
		
		boolBlockComand = false;
		
		if(isConnected){
//			if(TouchMainActivity.serverStatus.getPlayingSongID() == 0){
//				boolBlockComand = true;
//			}
//			
//			if(TouchMainActivity.serverStatus.getMediaType() == 0x07){
//				boolBlockComand = true;
//			}
			
//			if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && MyApplication.flagPlayingYouTube){
//				boolBlockComand = true;
//			}
			
			if(MyApplication.flagOnWifiVideo == false){
				boolBlockComand = true;
			}
						
			if(MyApplication.intSvrModel == MyApplication.SONCA_KM1 || MyApplication.intSvrModel == MyApplication.SONCA_KARTROL){
				boolBlockComand = true;
			}
			
//			if(MyApplication.intSvrModel == MyApplication.SONCA_KM2 || MyApplication.intSvrModel == MyApplication.SONCA_KBX9
//					|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
//					|| MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI){
//				if(MyApplication.curHiW_firmwareInfo != null){
//					if(MyApplication.curHiW_firmwareInfo.getWifi_version() < 130){
//						boolBlockComand = true;
//					}
//					
//					if(MyApplication.curHiW_firmwareInfo.getWifi_version() == 130 && MyApplication.curHiW_firmwareInfo.getWifi_revision() < 7014){
//						boolBlockComand = true;
//					}
//					 
//				}
//			}
//			
//			if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_TBT){
//				if(MyApplication.curHiW_firmwareInfo != null){
//					if(MyApplication.curHiW_firmwareInfo.getDaumay_version() < 200){
//						boolBlockComand = true;
//					}
//					
//					if(MyApplication.curHiW_firmwareInfo.getWifi_version() < 130){
//						boolBlockComand = true;
//					}
//					
//					if(MyApplication.curHiW_firmwareInfo.getWifi_version() == 130 && MyApplication.curHiW_firmwareInfo.getWifi_revision() < 7014){
//						boolBlockComand = true;
//					}
//				}
//			}
			
			if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				if(KTVMainActivity.serverStatus.getCurrentTempo() != 0){
					boolBlockComand = true;
				}
			} else {
				if(TouchMainActivity.serverStatus.getCurrentTempo() != 0){
					boolBlockComand = true;
				}	
			}
			
//			if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && MyApplication.flagSmartK_801){
//				boolBlockComand = true;
//			}
			
		}
		
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) { // THEME-BLUE				
			if (boolBlockComand == true) {
				drawBG.setBounds(
						(int) ((widthLayout - backgroundWidth) / 2),
						(int) ((heightLayout - backgroundHeight) / 2),
						(int) ((widthLayout - backgroundWidth) / 2 + backgroundWidth),
						(int) ((heightLayout - backgroundHeight) / 2 + backgroundHeight));
				drawBG.draw(canvas);

				drawVideoImageOff
						.setBounds(
								(int) ((widthLayout - backgroundWidth) / 2),
								(int) ((heightLayout - backgroundHeight) / 2),
								(int) ((widthLayout - backgroundWidth) / 2 + backgroundWidth),
								(int) ((heightLayout - backgroundHeight) / 2 + backgroundHeight));
				drawVideoImageOff.draw(canvas);

				mPaint.setTextSize(textScale);
				mPaint.setTypeface(Typeface.create(Typeface.DEFAULT,
						Typeface.BOLD));
				mPaint.setARGB(255, 69, 85, 88);
				Rect boundRect = new Rect();
				mPaint.getTextBounds(text, 0, text.length(), boundRect);
				canvas.drawText(text, widthLayout / 2 - boundRect.width() / 2,
						heightLayout / 2 + boundRect.height() / 2 + textDY,
						mPaint);

				resetPaint();
				
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

					drawVideoImage
							.setBounds(
									(int) ((widthLayout - backgroundWidth) / 2),
									(int) ((heightLayout - backgroundHeight) / 2),
									(int) ((widthLayout - backgroundWidth) / 2 + backgroundWidth),
									(int) ((heightLayout - backgroundHeight) / 2 + backgroundHeight));
					drawVideoImage.draw(canvas);
					
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

					drawVideoImageOff
							.setBounds(
									(int) ((widthLayout - backgroundWidth) / 2),
									(int) ((heightLayout - backgroundHeight) / 2),
									(int) ((widthLayout - backgroundWidth) / 2 + backgroundWidth),
									(int) ((heightLayout - backgroundHeight) / 2 + backgroundHeight));
					drawVideoImageOff.draw(canvas);

					mPaint.setTextSize(textScale);
					mPaint.setTypeface(Typeface.create(Typeface.DEFAULT,
							Typeface.BOLD));
					mPaint.setARGB(255, 69, 85, 88);
					Rect boundRect = new Rect();
					mPaint.getTextBounds(text, 0, text.length(), boundRect);
					canvas.drawText(text, widthLayout / 2 - boundRect.width() / 2,
							heightLayout / 2 + boundRect.height() / 2 + textDY,
							mPaint);

					resetPaint();

					canvas.restore();
				}
			} else {
				drawBG.setBounds(
						(int) ((widthLayout - backgroundWidth) / 2),
						(int) ((heightLayout - backgroundHeight) / 2),
						(int) ((widthLayout - backgroundWidth) / 2 + backgroundWidth),
						(int) ((heightLayout - backgroundHeight) / 2 + backgroundHeight));
				drawBG.draw(canvas);

				drawVideoImageOff
						.setBounds(
								(int) ((widthLayout - backgroundWidth) / 2),
								(int) ((heightLayout - backgroundHeight) / 2),
								(int) ((widthLayout - backgroundWidth) / 2 + backgroundWidth),
								(int) ((heightLayout - backgroundHeight) / 2 + backgroundHeight));
				drawVideoImageOff.draw(canvas);

				mPaint.setTextSize(textScale);
				mPaint.setTypeface(Typeface.create(Typeface.DEFAULT,
						Typeface.BOLD));
				mPaint.setARGB(255, 69, 85, 88);
				Rect boundRect = new Rect();
				mPaint.getTextBounds(text, 0, text.length(), boundRect);
				canvas.drawText(text, widthLayout / 2 - boundRect.width() / 2,
						heightLayout / 2 + boundRect.height() / 2 + textDY,
						mPaint);

				resetPaint();

				canvas.restore();
			}
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) { // THEME-LIGHT		
			
			if (boolBlockComand == true) {
				zlight_drawVideo_xam
						.setBounds(
								(int) ((widthLayout - backgroundWidth) / 2),
								(int) ((heightLayout - backgroundHeight) / 2),
								(int) ((widthLayout - backgroundWidth) / 2 + backgroundWidth),
								(int) ((heightLayout - backgroundHeight) / 2 + backgroundHeight));
				zlight_drawVideo_xam.draw(canvas);

				mPaint.setTextSize(textScale);
				mPaint.setTypeface(Typeface.create(Typeface.DEFAULT,
						Typeface.BOLD));
				mPaint.setARGB(255, 239, 239, 239);
				Rect boundRect = new Rect();
				mPaint.getTextBounds(text, 0, text.length(), boundRect);
				canvas.drawText(text, widthLayout / 2 - boundRect.width() / 2,
						heightLayout / 2 + boundRect.height() / 2 + textDY,
						mPaint);
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

					zlight_drawVideo
							.setBounds(
									(int) ((widthLayout - backgroundWidth) / 2),
									(int) ((heightLayout - backgroundHeight) / 2),
									(int) ((widthLayout - backgroundWidth) / 2 + backgroundWidth),
									(int) ((heightLayout - backgroundHeight) / 2 + backgroundHeight));
					zlight_drawVideo.draw(canvas);
					
					mPaint.setTextSize(textScale);
					mPaint.setTypeface(Typeface.create(Typeface.DEFAULT,
							Typeface.BOLD));
					mPaint.setARGB(255, 255, 255, 255);
					Rect boundRect = new Rect();
					mPaint.getTextBounds(text, 0, text.length(), boundRect);
					canvas.drawText(text, widthLayout / 2 - boundRect.width() / 2,
							heightLayout / 2 + boundRect.height() / 2 + textDY,
							mPaint);
				} else {
					zlight_drawVideo_xam
							.setBounds(
									(int) ((widthLayout - backgroundWidth) / 2),
									(int) ((heightLayout - backgroundHeight) / 2),
									(int) ((widthLayout - backgroundWidth) / 2 + backgroundWidth),
									(int) ((heightLayout - backgroundHeight) / 2 + backgroundHeight));
					zlight_drawVideo_xam.draw(canvas);

					mPaint.setTextSize(textScale);
					mPaint.setTypeface(Typeface.create(Typeface.DEFAULT,
							Typeface.BOLD));
					mPaint.setARGB(255, 239, 239, 239);
					Rect boundRect = new Rect();
					mPaint.getTextBounds(text, 0, text.length(), boundRect);
					canvas.drawText(text, widthLayout / 2 - boundRect.width() / 2,
							heightLayout / 2 + boundRect.height() / 2 + textDY,
							mPaint);

					canvas.restore();
				}
			} else {
				zlight_drawVideo_xam
						.setBounds(
								(int) ((widthLayout - backgroundWidth) / 2),
								(int) ((heightLayout - backgroundHeight) / 2),
								(int) ((widthLayout - backgroundWidth) / 2 + backgroundWidth),
								(int) ((heightLayout - backgroundHeight) / 2 + backgroundHeight));
				zlight_drawVideo_xam.draw(canvas);

				mPaint.setTextSize(textScale);
				mPaint.setTypeface(Typeface.create(Typeface.DEFAULT,
						Typeface.BOLD));
				mPaint.setARGB(255, 239, 239, 239);
				Rect boundRect = new Rect();
				mPaint.getTextBounds(text, 0, text.length(), boundRect);
				canvas.drawText(text, widthLayout / 2 - boundRect.width() / 2,
						heightLayout / 2 + boundRect.height() / 2 + textDY,
						mPaint);

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
	

}
