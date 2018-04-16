package vn.com.sonca.Touch.CustomView;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.utils.AppConfig.MEDIA_TYPE;
import vn.com.sonca.zktv.main.KTVMainActivity;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.View;

public class TouchDefaultControlView extends View {

	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);

	public TouchDefaultControlView(Context context) {
		super(context);
		initView(context);
	}

	public TouchDefaultControlView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchDefaultControlView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private OnDefaultListener listener;

	public interface OnDefaultListener {
		public void onClick();
		public void OnInActive();
	}

	public void setOnDefaultListener(OnDefaultListener listener) {
		this.listener = listener;
	}

	private Drawable drawAc;
	private Drawable drawNo;
	private Drawable drawNoCon;
	
	private Drawable zlightdrawAc;
	private Drawable zlightdrawNo;
	private Drawable zlightdrawNoCon;
	
	private String macdinh = "";

	private void initView(Context context) {
		macdinh = getResources().getString(R.string.default_view);
		drawNoCon = getResources().getDrawable(
				R.drawable.touch_boder_xam);
		drawAc = getResources().getDrawable(
				R.drawable.boder_default_active_182x182);
		drawNo = getResources().getDrawable(
				R.drawable.boder_default_inactive_182x182);
		
		zlightdrawNoCon = getResources().getDrawable(
				R.drawable.zlight_boder_default_xam);
		zlightdrawAc = getResources().getDrawable(
				R.drawable.zlight_boder_default_hover);
		zlightdrawNo = getResources().getDrawable(
				R.drawable.zlight_boder_default);
		
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
	private Path pathGoc = new Path();	
	private int textsize = 0;
	private float textY = 0;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		width = w;
		height = h;
		rectDraw = new Rect(0, 0, w, h);
		
		int pad = (int) (0.1*h);
		zlightrectDraw.set(pad, pad, w - pad, h - pad);
		
		textsize = (int) (0.35 * h);
		textY = (float) (0.65 * height);
		if (MyApplication.flagHong) {
			textsize = (int) (0.30 * h);
			textY = (float) (0.60 * h);
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
	public void setConnect(boolean isConnected){
		this.isConnected = isConnected;
		invalidate();
	}
	
	private boolean boolBlockComand = false;
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);	
		
		super.onDraw(canvas);

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
				int retur1 = (MyApplication.intCommandMedium & (clear << 4)) >> 4;
				int retur2 = (MyApplication.intCommandMedium & (clear << 6)) >> 6;
				int retur3 = (MyApplication.intCommandMedium & (clear << 8)) >> 8;
				int retur4 = (MyApplication.intCommandMedium & (clear << 10)) >> 10;
				if ((MyApplication.flagDeviceUser == true && retur1 != 0)
						|| (MyApplication.flagDeviceUser == true && retur2 != 0)
						|| (MyApplication.flagDeviceUser == true && retur3 != 0)
						|| (MyApplication.flagDeviceUser == true && retur4 != 0)
						|| (MyApplication.flagDeviceUser == false && retur1 == 2)
						|| (MyApplication.flagDeviceUser == false && retur2 == 2)
						|| (MyApplication.flagDeviceUser == false && retur3 == 2)
						|| (MyApplication.flagDeviceUser == false && retur4 == 2)) {
					boolBlockComand = true;
				} else {
					boolBlockComand = false;
				}
			} else {
				if (((MyApplication.intCommandEnable & 64) != 64) || ((MyApplication.intCommandEnable & 32) != 32)
						|| ((MyApplication.intCommandEnable & 16) != 16)
						|| ((MyApplication.intCommandEnable & 8) != 8)) {
					boolBlockComand = true;
				} else {
					boolBlockComand = false;
				}
			}
		}
		
		if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && MyApplication.flagPlayingYouTube){
			boolBlockComand = true;
		}

		// BACKVIEW
		// MyApplication.intColorScreen = MyApplication.SCREEN_LIGHT;
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			color_01 = Color.GRAY;
			color_02 = Color.argb(255, 180, 253, 253);
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			color_01 = Color.WHITE;
			color_02 = Color.parseColor("#005249");
		}
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			
			if (boolBlockComand == true) {
				drawNoCon.setBounds(rectDraw);
				drawNoCon.draw(canvas);
				paintMain.setColor(color_01);
				paintMain.setTextSize(textsize);
				paintMain.setTypeface(Typeface.DEFAULT_BOLD);
				paintMain.setStyle(Style.FILL);
				float size = paintMain.measureText(macdinh);
				canvas.drawText(macdinh, (float) (width / 2 - size / 2), textY, paintMain);
				return;
			}
			
			if (TouchMainActivity.serverStatus != null || KTVMainActivity.serverStatus != null) {
				boolean flag = false;
				if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
					if (KTVMainActivity.serverStatus.isPaused()) {
						flag = true;
					}
				} else {
					if (TouchMainActivity.serverStatus.isPaused()) {
						flag = true;
					}
				}
				
				if (flag) {
					drawNoCon.setBounds(rectDraw);
					drawNoCon.draw(canvas);
					paintMain.setColor(color_01);
					paintMain.setTextSize(textsize);
					paintMain.setTypeface(Typeface.DEFAULT_BOLD);
					paintMain.setStyle(Style.FILL);
					float size = paintMain.measureText(macdinh);
					canvas.drawText(macdinh, (float) (width / 2 - size / 2), textY, paintMain);
					return;
				}
			}
			if (MyApplication.intWifiRemote == MyApplication.SONCA) {
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
						drawNoCon.setBounds(rectDraw);
						drawNoCon.draw(canvas);
						paintMain.setColor(color_01);
						paintMain.setTextSize((float) (textsize));
						paintMain.setTypeface(Typeface.DEFAULT_BOLD);
						paintMain.setStyle(Style.FILL);
						float size = paintMain.measureText(macdinh);
						canvas.drawText(macdinh, (float) (width / 2 - size / 2), textY, paintMain);
						return;
					}
				}
			}
			if (TouchMainActivity.serverStatus != null || KTVMainActivity.serverStatus != null) {
				boolean isDraw = false;
				int type = 0x07;
				if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
					type = KTVMainActivity.serverStatus.getMediaType();
				} else {
					type = TouchMainActivity.serverStatus.getMediaType();
				}

				if (type != 0x07) {
					if (type != -1) {
						MEDIA_TYPE aType = MEDIA_TYPE.values()[type];
						if (aType != MEDIA_TYPE.MIDI) {
							isDraw = true;
						}
					}
				} else {
					isDraw = true;
				}
				if (isDraw == true) {
					drawNoCon.setBounds(rectDraw);
					drawNoCon.draw(canvas);
					paintMain.setColor(color_01);
					paintMain.setTextSize((float) (textsize));
					paintMain.setTypeface(Typeface.DEFAULT_BOLD);
					paintMain.setStyle(Style.FILL);
					float size = paintMain.measureText(macdinh);
					canvas.drawText(macdinh, (float) (width / 2 - size / 2), textY, paintMain);
					return;
				}
			}
			isConnected = TouchMainActivity.serverStatus != null || KTVMainActivity.serverStatus != null;
			if (MyApplication.intWifiRemote != MyApplication.SONCA) {
				isConnected = false;
			}
			if (!MyApplication.flagSongPlayPause) {
				isConnected = false;
			}
			if (!isConnected) {
				drawNoCon.setBounds(rectDraw);
				drawNoCon.draw(canvas);
			} else {
				if (isActive) {
					drawAc.setBounds(rectDraw);
					drawAc.draw(canvas);
				} else {
					drawNo.setBounds(rectDraw);
					drawNo.draw(canvas);
				}
			}
			if (isConnected) {
				paintMain.setColor(color_02);
			} else {
				paintMain.setColor(color_01);
			}
			paintMain.setTextSize(textsize);
			paintMain.setTypeface(Typeface.DEFAULT_BOLD);
			paintMain.setStyle(Style.FILL);
			float size = paintMain.measureText(macdinh);
			canvas.drawText(macdinh, (float) (width / 2 - size / 2), textY, paintMain);
			
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {

			if (boolBlockComand == true) {
				zlightdrawNoCon.setBounds(zlightrectDraw);
				zlightdrawNoCon.draw(canvas);
				paintMain.setColor(color_01);
				paintMain.setTextSize(textsize);
				paintMain.setTypeface(Typeface.DEFAULT_BOLD);
				paintMain.setStyle(Style.FILL);
				float size = paintMain.measureText(macdinh);
				canvas.drawText(macdinh, (float) (width / 2 - size / 2), textY, paintMain);
				return;
			}
			if (TouchMainActivity.serverStatus != null || KTVMainActivity.serverStatus != null) {
				boolean flag = false;
				if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
					if (KTVMainActivity.serverStatus.isPaused()) {
						flag = true;
					}
				} else {
					if (TouchMainActivity.serverStatus.isPaused()) {
						flag = true;
					}
				}
				
				if (flag) {
					zlightdrawNoCon.setBounds(zlightrectDraw);
					zlightdrawNoCon.draw(canvas);
					paintMain.setColor(color_01);
					paintMain.setTextSize(textsize);
					paintMain.setTypeface(Typeface.DEFAULT_BOLD);
					paintMain.setStyle(Style.FILL);
					float size = paintMain.measureText(macdinh);
					canvas.drawText(macdinh, (float) (width / 2 - size / 2), textY, paintMain);
					return;
				}
			}
			if (MyApplication.intWifiRemote == MyApplication.SONCA) {
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
						zlightdrawNoCon.setBounds(zlightrectDraw);
						zlightdrawNoCon.draw(canvas);
						paintMain.setColor(color_01);
						paintMain.setTextSize((float) (textsize));
						paintMain.setTypeface(Typeface.DEFAULT_BOLD);
						paintMain.setStyle(Style.FILL);
						float size = paintMain.measureText(macdinh);
						canvas.drawText(macdinh, (float) (width / 2 - size / 2), textY, paintMain);
						return;
					}
				}
			}
			if (TouchMainActivity.serverStatus != null || KTVMainActivity.serverStatus != null) {
				boolean isDraw = false;
				int type = 0x07;
				if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
					type = KTVMainActivity.serverStatus.getMediaType();
				} else {
					type = TouchMainActivity.serverStatus.getMediaType();
				}
				
				if (type != 0x07) {
					if (type != -1) {
						MEDIA_TYPE aType = MEDIA_TYPE.values()[type];
						if (aType != MEDIA_TYPE.MIDI) {
							isDraw = true;
						}
					}
				} else {
					isDraw = true;
				}
				if (isDraw == true) {
					zlightdrawNoCon.setBounds(zlightrectDraw);
					zlightdrawNoCon.draw(canvas);
					paintMain.setColor(color_01);
					paintMain.setTextSize((float) (textsize));
					paintMain.setTypeface(Typeface.DEFAULT_BOLD);
					paintMain.setStyle(Style.FILL);
					float size = paintMain.measureText(macdinh);
					canvas.drawText(macdinh, (float) (width / 2 - size / 2), textY, paintMain);
					return;
				}
			}
			isConnected = TouchMainActivity.serverStatus != null || KTVMainActivity.serverStatus != null;
			if (MyApplication.intWifiRemote != MyApplication.SONCA) {
				isConnected = false;
			}
			if (!MyApplication.flagSongPlayPause) {
				isConnected = false;
			}
			if (!isConnected) {
				zlightdrawNoCon.setBounds(zlightrectDraw);
				zlightdrawNoCon.draw(canvas);
			} else {
				if (isActive) {
					zlightdrawAc.setBounds(zlightrectDraw);
					zlightdrawAc.draw(canvas);
				} else {
					zlightdrawNo.setBounds(zlightrectDraw);
					zlightdrawNo.draw(canvas);
				}
			}
			if (isConnected) {
				paintMain.setColor(color_02);
			} else {
				paintMain.setColor(color_01);
			}
			paintMain.setTextSize(textsize);
			paintMain.setTypeface(Typeface.DEFAULT_BOLD);
			paintMain.setStyle(Style.FILL);
			float size = paintMain.measureText(macdinh);
			canvas.drawText(macdinh, (float) (width / 2 - size / 2), textY, paintMain);
			
		
		}

	}

	private boolean isActive = false;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(boolBlockComand == true){
			return true;
		}
		
		if (TouchMainActivity.serverStatus != null || KTVMainActivity.serverStatus != null) {
			boolean flag = false;
			if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				if (KTVMainActivity.serverStatus.isPaused()) {
					flag = true;
				}
			} else {
				if (TouchMainActivity.serverStatus.isPaused()) {
					flag = true;
				}
			}
			
			if (flag) {
				return true;
			}
		}
		
		if(((MyApplication.intCommandEnable & 64) != 64) || 
			((MyApplication.intCommandEnable & 32) != 32) ||
			((MyApplication.intCommandEnable & 16) != 16) || 
			((MyApplication.intCommandEnable & 8) != 8)){
			return true;
		}
		
		if (TouchMainActivity.serverStatus != null || KTVMainActivity.serverStatus != null) {
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
				MEDIA_TYPE aType = MEDIA_TYPE.values()[type];
				if(aType != MEDIA_TYPE.MIDI){
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
