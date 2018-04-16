package vn.com.sonca.Touch.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.utils.AppConfig.MEDIA_TYPE;
import vn.com.sonca.zktv.main.KTVMainActivity;
import vn.com.sonca.zzzzz.MyApplication;

public class TouchChangePerfectView extends View {

	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);

	public TouchChangePerfectView(Context context) {
		super(context);
		initView(context);
	}

	public TouchChangePerfectView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchChangePerfectView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private OnChangePerfectListener listener;

	public interface OnChangePerfectListener {
		public void onClick();
		public void OnInActive();
	}

	public void setOnChangePerfectListener(OnChangePerfectListener listener) {
		this.listener = listener;
	}

	private Drawable drawAc;
	private Drawable drawNo;
	private Drawable drawNoCon;
	private Drawable drawPerfectAC;
	private Drawable drawPerfectIN;
	
	private Drawable zlightdrawAc;
	private Drawable zlightdrawNo;
	private Drawable zlightdrawNoCon;
	private Drawable zlightdrawPerfectAC;
	private Drawable zlightdrawPerfectIN;	
	
	private String textdoi = "";
	private String texthoaam = "";
	
	private void initView(Context context) {
		textdoi = getResources().getString(R.string.text_chane_perfect_0);
		texthoaam = getResources().getString(R.string.text_chane_perfect_1);
		drawNoCon = getResources().getDrawable(
				R.drawable.touch_boder_xam);
		drawAc = getResources().getDrawable(
				R.drawable.boder_default_active_182x182);
		drawNo = getResources().getDrawable(
				R.drawable.boder_default_inactive_182x182);
		
		drawPerfectAC = getResources().getDrawable(
				R.drawable.touch_icon_hoaam);
		drawPerfectIN = getResources().getDrawable(
				R.drawable.touch_icon_hoaam_xam);
		
		zlightdrawNoCon = getResources().getDrawable(
				R.drawable.zlight_boder_default_xam);
		zlightdrawAc = getResources().getDrawable(
				R.drawable.zlight_boder_default_hover);
		zlightdrawNo = getResources().getDrawable(
				R.drawable.zlight_boder_default);
		zlightdrawPerfectAC = getResources().getDrawable(
				R.drawable.zlight_icon_hoaam);
		zlightdrawPerfectIN = getResources().getDrawable(
				R.drawable.zlight_icon_hoaam_xam_other);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = (int) (0.45*MeasureSpec.getSize(widthMeasureSpec));
		int measuredHeight = (int) (182 * width / 82);
		setMeasuredDimension(measuredHeight, heightMeasureSpec);
	}

	private int width;
	private int height;
	private float Line;
	private Rect rectDraw = new Rect();
	private Rect zlightrectDraw = new Rect();
	private Rect rectPerfect = new Rect();
	private Path pathGoc = new Path();
	private int textsize = 0;
	private float textY1 = 0;
	private float textY2 = 0;
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		width = (int) (1.3*w);
		height = h;
		rectDraw.set(0, 0, w, h);
		
		int pad = (int) (0.1*h);
		zlightrectDraw.set(pad, pad, w - pad, h - pad);
		
		int tamX = (int) (0.5*h);
		int tamY = (int) (0.5*h);
		int wr = (int) (0.33*h);
		if(MyApplication.flagHong){
			tamX = (int) (0.35*h);
			wr = (int) (0.25*h);
		}
		rectPerfect.set(tamX - wr, tamY - wr, tamX + wr, tamY + wr);
				
		textsize = (int) (0.28*h);
		textY1 = (float) (0.46 * h);
		textY2 = (float) (0.76 * h);
		if(MyApplication.flagHong){
			textsize = (int) (0.25*h);
			textY1 = (float) (0.46 * h);
			textY2 = (float) (0.76 * h);
		}
		
		int widthLayout = w;
		int heightLayout = h;
		Line = (float) (0.15*heightLayout);
		
		pathGoc.reset();
		
		pathGoc.moveTo(0 , 0 + Line);
		pathGoc.lineTo(0, 0);	
		pathGoc.lineTo(0 + Line , 0);
		
		pathGoc.moveTo(widthLayout , 0 + Line);
		pathGoc.lineTo(widthLayout , 0);
		pathGoc.lineTo(widthLayout - Line , 0);
		
		pathGoc.moveTo(widthLayout , heightLayout - Line);
		pathGoc.lineTo(widthLayout, heightLayout);
		pathGoc.lineTo(widthLayout - Line , heightLayout);
		
		pathGoc.moveTo(0 , heightLayout - Line);
		pathGoc.lineTo(0 , heightLayout);
		pathGoc.lineTo(0 + Line , heightLayout);
	}

	private int color_01;
	private int color_02;
	
	private boolean isConnected;
	private boolean boolBlockComand = false;
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);	
		
		isConnected = TouchMainActivity.serverStatus != null || KTVMainActivity.serverStatus != null;
		boolBlockComand = false;

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
				int retur1 = (MyApplication.intCommandMedium & (clear << 20)) >> 20; // next
				int retur2 = (MyApplication.intCommandMedium & (clear << 0)) >> 0; // first
				if ((MyApplication.flagDeviceUser == true && retur1 != 0)
						|| (MyApplication.flagDeviceUser == true && retur2 != 0)
						|| (MyApplication.flagDeviceUser == false && retur1 == 2)
						|| (MyApplication.flagDeviceUser == false && retur2 == 2)) {
					boolBlockComand = true;
				} else {
					boolBlockComand = false;
				}
			} else {
				if (((MyApplication.intCommandEnable & 2048) != 2048) || ((MyApplication.intCommandEnable & 2) != 2)) {
					boolBlockComand = true;
				} else {
					boolBlockComand = false;
				}
			}
		}

		if(MyApplication.intSvrModel == MyApplication.SONCA_KARTROL || MyApplication.intSvrModel == MyApplication.SONCA_KM1){
			boolBlockComand = true;
		}
		// MyApplication.intColorScreen = MyApplication.SCREEN_LIGHT;
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			color_01 = Color.GRAY;
			color_02 = Color.argb(255, 180, 253, 253);
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			color_01 = Color.WHITE;
			color_02 = Color.parseColor("#005249");
		}
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			
			if (boolBlockComand) {
				drawNoCon.setBounds(rectDraw);
				drawNoCon.draw(canvas);
				drawPerfectIN.setBounds(rectPerfect);
				drawPerfectIN.draw(canvas);
				paintMain.setStyle(Style.FILL);
				paintMain.setTextSize(textsize);
				paintMain.setColor(color_01);
				paintMain.setTypeface(Typeface.DEFAULT_BOLD);
				float size = paintMain.measureText(textdoi);
				canvas.drawText(textdoi, (float) (width / 2 - size / 2), textY1, paintMain);
				size = paintMain.measureText(texthoaam);
				canvas.drawText(texthoaam, (float) (width / 2 - size / 2), textY2, paintMain);
				return;
			}

			boolean flagNull = false;
			if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				if (KTVMainActivity.serverStatus == null){
					flagNull = true;					
				}
			} else {
				if (TouchMainActivity.serverStatus == null){
					flagNull = true;					
				}
			}
			
			if (flagNull) {
				drawNoCon.setBounds(rectDraw);
				drawNoCon.draw(canvas);
				drawPerfectIN.setBounds(rectPerfect);
				drawPerfectIN.draw(canvas);
				paintMain.setStyle(Style.FILL);
				paintMain.setTextSize(textsize);
				paintMain.setColor(color_01);
				paintMain.setTypeface(Typeface.DEFAULT_BOLD);
				float size = paintMain.measureText(textdoi);
				canvas.drawText(textdoi, (float) (width / 2 - size / 2), textY1, paintMain);
				size = paintMain.measureText(texthoaam);
				canvas.drawText(texthoaam, (float) (width / 2 - size / 2), textY2, paintMain);
				return;
			}

			boolean flag99 = false;
			if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				if (KTVMainActivity.serverStatus.getReservedSongCount() >= 99) {
					flag99 = true;
				}
			} else {
				if (TouchMainActivity.serverStatus.getReservedSongCount() >= 99) {
					flag99 = true;
				}
			}
			
			if (flag99) {
				drawNoCon.setBounds(rectDraw);
				drawNoCon.draw(canvas);
				drawPerfectIN.setBounds(rectPerfect);
				drawPerfectIN.draw(canvas);
				paintMain.setStyle(Style.FILL);
				paintMain.setTextSize(textsize);
				paintMain.setColor(color_01);
				paintMain.setTypeface(Typeface.DEFAULT_BOLD);
				float size = paintMain.measureText(textdoi);
				canvas.drawText(textdoi, (float) (width / 2 - size / 2), textY1, paintMain);
				size = paintMain.measureText(texthoaam);
				canvas.drawText(texthoaam, (float) (width / 2 - size / 2), textY2, paintMain);
				return;
			}

			if (MyApplication.intWifiRemote == MyApplication.SONCA) {
				if (isConnected) {
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
						drawNoCon.setBounds(rectDraw);
						drawNoCon.draw(canvas);
						drawPerfectIN.setBounds(rectPerfect);
						drawPerfectIN.draw(canvas);
						paintMain.setStyle(Style.FILL);
						paintMain.setTextSize(textsize);
						paintMain.setColor(color_01);
						paintMain.setTypeface(Typeface.DEFAULT_BOLD);
						float size = paintMain.measureText(textdoi);
						canvas.drawText(textdoi, (float) (width / 2 - size / 2), textY1, paintMain);
						size = paintMain.measureText(texthoaam);
						canvas.drawText(texthoaam, (float) (width / 2 - size / 2), textY2, paintMain);
						return;
					}
				}
			}

			if (isConnected) {
				boolean isDraw = false;
				
				if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
					int type = KTVMainActivity.serverStatus.getMediaType();
					int ABC = KTVMainActivity.serverStatus.getPlayingSongTypeABC();
					if (type != 0x07) {
						if (type != -1) {
							MEDIA_TYPE aType = MEDIA_TYPE.values()[type];
							if (aType != MEDIA_TYPE.MIDI || (aType == MEDIA_TYPE.MIDI && ABC == 0)) {
								isDraw = true;
							}
						}
					} else {
						isDraw = true;
					}
				} else {
					int type = TouchMainActivity.serverStatus.getMediaType();
					int ABC = TouchMainActivity.serverStatus.getPlayingSongTypeABC();
					if (type != 0x07) {
						if (type != -1) {
							MEDIA_TYPE aType = MEDIA_TYPE.values()[type];
							if (aType != MEDIA_TYPE.MIDI || (aType == MEDIA_TYPE.MIDI && ABC == 0)) {
								isDraw = true;
							}
						}
					} else {
						isDraw = true;
					}	
				}
				
				
				if (isDraw == true) {
					drawNoCon.setBounds(rectDraw);
					drawNoCon.draw(canvas);
					drawPerfectIN.setBounds(rectPerfect);
					drawPerfectIN.draw(canvas);
					paintMain.setStyle(Style.FILL);
					paintMain.setTextSize(textsize);
					paintMain.setColor(color_01);
					paintMain.setTypeface(Typeface.DEFAULT_BOLD);
					float size = paintMain.measureText(textdoi);
					canvas.drawText(textdoi, (float) (width / 2 - size / 2), textY1, paintMain);
					size = paintMain.measureText(texthoaam);
					canvas.drawText(texthoaam, (float) (width / 2 - size / 2), textY2, paintMain);
					return;
				}
			}

			if (isActive) {
				drawAc.setBounds(rectDraw);
				drawAc.draw(canvas);
			} else {
				drawNo.setBounds(rectDraw);
				drawNo.draw(canvas);
			}
			drawPerfectAC.setBounds(rectPerfect);
			drawPerfectAC.draw(canvas);

			paintMain.setStyle(Style.FILL);
			paintMain.setTextSize(textsize);
			paintMain.setColor(color_02);
			paintMain.setTypeface(Typeface.DEFAULT_BOLD);
			float size = paintMain.measureText(textdoi);
			canvas.drawText(textdoi, (float) (width / 2 - size / 2), textY1, paintMain);
			size = paintMain.measureText(texthoaam);
			canvas.drawText(texthoaam, (float) (width / 2 - size / 2), textY2, paintMain);

		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {

			if (boolBlockComand) {
				zlightdrawNoCon.setBounds(zlightrectDraw);
				zlightdrawNoCon.draw(canvas);
				zlightdrawPerfectIN.setBounds(rectPerfect);
				zlightdrawPerfectIN.draw(canvas);
				paintMain.setStyle(Style.FILL);
				paintMain.setTextSize(textsize);
				paintMain.setColor(color_01);
				paintMain.setTypeface(Typeface.DEFAULT_BOLD);
				float size = paintMain.measureText(textdoi);
				canvas.drawText(textdoi, (float) (width / 2 - size / 2), textY1, paintMain);
				size = paintMain.measureText(texthoaam);
				canvas.drawText(texthoaam, (float) (width / 2 - size / 2), textY2, paintMain);
				return;
			}

			boolean flagNull = false;
			if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				if (KTVMainActivity.serverStatus == null){
					flagNull = true;					
				}
			} else {
				if (TouchMainActivity.serverStatus == null){
					flagNull = true;					
				}
			}
			
			if (flagNull) {
				zlightdrawNoCon.setBounds(zlightrectDraw);
				zlightdrawNoCon.draw(canvas);
				zlightdrawPerfectIN.setBounds(rectPerfect);
				zlightdrawPerfectIN.draw(canvas);
				paintMain.setStyle(Style.FILL);
				paintMain.setTextSize(textsize);
				paintMain.setColor(color_01);
				paintMain.setTypeface(Typeface.DEFAULT_BOLD);
				float size = paintMain.measureText(textdoi);
				canvas.drawText(textdoi, (float) (width / 2 - size / 2), textY1, paintMain);
				size = paintMain.measureText(texthoaam);
				canvas.drawText(texthoaam, (float) (width / 2 - size / 2), textY2, paintMain);
				return;
			}

			boolean flag99 = false;
			if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				if (KTVMainActivity.serverStatus.getReservedSongCount() >= 99) {
					flag99 = true;
				}
			} else {
				if (TouchMainActivity.serverStatus.getReservedSongCount() >= 99) {
					flag99 = true;
				}
			}
			
			if (flag99) {
				zlightdrawNoCon.setBounds(zlightrectDraw);
				zlightdrawNoCon.draw(canvas);
				zlightdrawPerfectIN.setBounds(rectPerfect);
				zlightdrawPerfectIN.draw(canvas);
				paintMain.setStyle(Style.FILL);
				paintMain.setTextSize(textsize);
				paintMain.setColor(color_01);
				paintMain.setTypeface(Typeface.DEFAULT_BOLD);
				float size = paintMain.measureText(textdoi);
				canvas.drawText(textdoi, (float) (width / 2 - size / 2), textY1, paintMain);
				size = paintMain.measureText(texthoaam);
				canvas.drawText(texthoaam, (float) (width / 2 - size / 2), textY2, paintMain);
				return;
			}

			if (MyApplication.intWifiRemote == MyApplication.SONCA) {
				if (isConnected) {
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
						zlightdrawNoCon.setBounds(zlightrectDraw);
						zlightdrawNoCon.draw(canvas);
						zlightdrawPerfectIN.setBounds(rectPerfect);
						zlightdrawPerfectIN.draw(canvas);
						paintMain.setStyle(Style.FILL);
						paintMain.setTextSize(textsize);
						paintMain.setColor(color_01);
						paintMain.setTypeface(Typeface.DEFAULT_BOLD);
						float size = paintMain.measureText(textdoi);
						canvas.drawText(textdoi, (float) (width / 2 - size / 2), textY1, paintMain);
						size = paintMain.measureText(texthoaam);
						canvas.drawText(texthoaam, (float) (width / 2 - size / 2), textY2, paintMain);
						return;
					}
				}
			}

			if (isConnected) {
				boolean isDraw = false;
				
				if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
					int type = KTVMainActivity.serverStatus.getMediaType();
					int ABC = KTVMainActivity.serverStatus.getPlayingSongTypeABC();
					if (type != 0x07) {
						if (type != -1) {
							MEDIA_TYPE aType = MEDIA_TYPE.values()[type];
							if (aType != MEDIA_TYPE.MIDI || (aType == MEDIA_TYPE.MIDI && ABC == 0)) {
								isDraw = true;
							}
						}
					} else {
						isDraw = true;
					}
				} else {
					int type = TouchMainActivity.serverStatus.getMediaType();
					int ABC = TouchMainActivity.serverStatus.getPlayingSongTypeABC();
					if (type != 0x07) {
						if (type != -1) {
							MEDIA_TYPE aType = MEDIA_TYPE.values()[type];
							if (aType != MEDIA_TYPE.MIDI || (aType == MEDIA_TYPE.MIDI && ABC == 0)) {
								isDraw = true;
							}
						}
					} else {
						isDraw = true;
					}	
				}
				
				if (isDraw == true) {
					zlightdrawNoCon.setBounds(zlightrectDraw);
					zlightdrawNoCon.draw(canvas);
					zlightdrawPerfectIN.setBounds(rectPerfect);
					zlightdrawPerfectIN.draw(canvas);
					paintMain.setStyle(Style.FILL);
					paintMain.setTextSize(textsize);
					paintMain.setColor(color_01);
					paintMain.setTypeface(Typeface.DEFAULT_BOLD);
					float size = paintMain.measureText(textdoi);
					canvas.drawText(textdoi, (float) (width / 2 - size / 2), textY1, paintMain);
					size = paintMain.measureText(texthoaam);
					canvas.drawText(texthoaam, (float) (width / 2 - size / 2), textY2, paintMain);
					return;
				}
			}

			if (isActive) {
				zlightdrawAc.setBounds(zlightrectDraw);
				zlightdrawAc.draw(canvas);
			} else {
				zlightdrawNo.setBounds(zlightrectDraw);
				zlightdrawNo.draw(canvas);
			}
			zlightdrawPerfectAC.setBounds(rectPerfect);
			zlightdrawPerfectAC.draw(canvas);

			paintMain.setStyle(Style.FILL);
			paintMain.setTextSize(textsize);
			paintMain.setColor(color_02);
			paintMain.setTypeface(Typeface.DEFAULT_BOLD);
			float size = paintMain.measureText(textdoi);
			canvas.drawText(textdoi, (float) (width / 2 - size / 2), textY1, paintMain);
			size = paintMain.measureText(texthoaam);
			canvas.drawText(texthoaam, (float) (width / 2 - size / 2), textY2, paintMain);

		}
	}

	private boolean isActive = false;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		boolean flagNull = false;
		if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			if (KTVMainActivity.serverStatus == null){
				flagNull = true;					
			}
		} else {
			if (TouchMainActivity.serverStatus == null){
				flagNull = true;					
			}
		}
		
		if (flagNull) {
			return true;
		}
		
		if(boolBlockComand){
			return true;
		}	
		
		boolean flag99 = false;
		if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			if (KTVMainActivity.serverStatus.getReservedSongCount() >= 99) {
				flag99 = true;
			}
		} else {
			if (TouchMainActivity.serverStatus.getReservedSongCount() >= 99) {
				flag99 = true;
			}
		}
		
		if (flag99) {
			return true;
		}
		
		if(MyApplication.intWifiRemote == MyApplication.SONCA){
			if (isConnected) {
				boolean flag = false;
				if (MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
					if (KTVMainActivity.serverStatus.getPlayingSongID() == 0) {
						flag = true;
					}
				} else {
					if (TouchMainActivity.serverStatus.getPlayingSongID() == 0) {
						flag = true;
					}
				}

				if (flag) {
					return true;
				}
			}
		}
		
		if(isConnected){
			int type = 0x07;
			if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				type = KTVMainActivity.serverStatus.getMediaType();
			} else {
				type = TouchMainActivity.serverStatus.getMediaType();	
			}
			
			if (type == 0x07) {
				return true;
			}
			
			if(type != -1){
				int ABC = 0;
				if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
					ABC = KTVMainActivity.serverStatus.getPlayingSongTypeABC();
				} else {
					ABC = TouchMainActivity.serverStatus.getPlayingSongTypeABC();
				}

				MEDIA_TYPE aType = MEDIA_TYPE.values()[type];
				if(aType != MEDIA_TYPE.MIDI || (aType == MEDIA_TYPE.MIDI && ABC == 0)){
					return true;
				}
			}
		}
		if (isConnected) {
			super.onTouchEvent(event);
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				isActive = true;
				invalidate();
				break;
			case MotionEvent.ACTION_UP:
				playSoundEffect(SoundEffectConstants.CLICK);
				isActive = false;
				invalidate();
				if (listener != null) {
					listener.onClick();
				}
				break;
			default:
				break;
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
	
	private final int INTCOMMAND = 128;
	public void setCommandEnable(boolean bool){
		if (bool) {
			MyApplication.intCommandEnable |= INTCOMMAND;
		} else {
			MyApplication.intCommandEnable &= (~INTCOMMAND);
		}
		invalidate();
	}
	
	public boolean getCommandEnable(){
		return (MyApplication.intCommandEnable & INTCOMMAND) == INTCOMMAND;
	}

}
