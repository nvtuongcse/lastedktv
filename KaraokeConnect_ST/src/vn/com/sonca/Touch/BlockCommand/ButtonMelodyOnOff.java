package vn.com.sonca.Touch.BlockCommand;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.zktv.main.KTVMainActivity;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ButtonMelodyOnOff extends View {
	
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	
	public ButtonMelodyOnOff(Context context) {
		super(context);
		initView(context);
	}

	public ButtonMelodyOnOff(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public ButtonMelodyOnOff(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private OnMelodyOnOffCommandListener listener;
	public interface OnMelodyOnOffCommandListener{
		public void OnCommand(int id, int value);
		public void OnCommand(int id, boolean bool, boolean isShowDialog);
		public void OnShowMelodyD();
	}
	
	public void setOnMelodyOnOffCommandListener(OnMelodyOnOffCommandListener listener){
		this.listener = listener;
	}
	
	private Drawable drawOFF;
	private Drawable drawON1;
	private Drawable drawON2;
	private Drawable drawAC;
	private Drawable drawIN;
	
	private Drawable zlightdrawOFF;
	private Drawable zlightdrawON1;
	private Drawable zlightdrawON2;
	private Drawable zlightdrawAC;
	private Drawable zlightdrawIN;
	
	private String dataON = "";
	private String dataOFF = "";
	private String title = "";
	private void initView(Context context) {
		drawIN = getResources().getDrawable(R.drawable.boder_lammoi);
		drawAC = getResources().getDrawable(R.drawable.boder_lammoi_hover);
		drawOFF = getResources().getDrawable(R.drawable.touch_kdk_mo);
		drawON1 = getResources().getDrawable(R.drawable.touch_kdk_khoahet);
		drawON2 = getResources().getDrawable(R.drawable.touch_kdk_khoadienthoai);
		
		zlightdrawIN = getResources().getDrawable(R.drawable.zlight_boder_ketnoi);
		zlightdrawAC = getResources().getDrawable(R.drawable.zlight_boder_ketnoi_hover);
		zlightdrawOFF = getResources().getDrawable(R.drawable.zlight_kdk_mo);
		zlightdrawON1 = getResources().getDrawable(R.drawable.zlight_kdk_khoahet);
		zlightdrawON2 = getResources().getDrawable(R.drawable.zlight_kdk_khoadienthoai);
		
		dataON = getResources().getString(R.string.command_2);
		dataOFF = getResources().getString(R.string.command_3);
		title = getResources().getString(R.string.command_4b);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int myWidth = MeasureSpec.getSize(widthMeasureSpec);
		int myHeight = (int) (0.3*myWidth);
		setMeasuredDimension(myWidth , myHeight);
	}
	
	private int widthLayout;
	private int heightLayout;
	private float titleS, titleX, titleY;
	private float stateS, stateX, stateY;
	private float chamS, chamX, chamY;
	private float staS, staX, staY;
	private Rect rectBut = new Rect();
	private Rect rectIcon = new Rect();
	private Rect rectOnOff = new Rect();
	private Rect rectBut1 = new Rect();
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		widthLayout = w;
		heightLayout = h;
		
		int tamY = (int) (0.5*h);
		int hr = (int) (0.13*h);
		int wr = 107*hr/37;
		rectBut.set(w - 5 - 2*wr, tamY - hr, w - 5, tamY + hr);
		
		tamY = (int) (0.5*h);
		hr = (int) (0.4*h);
		wr = hr;
		rectIcon.set(5, tamY - hr, 2*wr + 5, tamY + hr);
		
		staS = titleS = (float) (0.2*h);
		staX = titleX = (float) (2.2*wr);
		titleY = (float) (0.45*h - 0.1*titleS);
		staY = (float) (0.25*h + 0.9*staS);
		
		stateS = (float) (0.13*h);
		stateX = titleX;
		stateY = (float) (0.44*h + 1.2*stateS);
		
		tamY = (int) (0.31*h);
		hr = (int) (0.3*h);
		wr = 190*hr/100;
		rectOnOff.set(w - 2*wr, tamY - hr, w, tamY + hr);
		
		intStartKhoa = rectOnOff.left;
		intKhoaHet = intStartKhoa + 2*wr/3;
		intKhoaDienT = intStartKhoa + 4*wr/3;
		
		int yy = (int) (0.65*h);
		rectBut1.set((int)(stateX), yy, w - 5, h - 5);
		chamS = (float) (0.4*(rectBut1.bottom - rectBut1.top));
		chamX = (float) (titleX + 30);
		chamY = (float) (rectBut1.bottom - 0.9*chamS);
		
	}
	
	private int color_01;
	private int color_02;
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// MyApplication.intColorScreen = MyApplication.SCREEN_LIGHT;
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			color_01 = Color.argb(255, 0, 253, 253);
			color_02 = Color.argb(255, 180, 253, 254);
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			color_01 = Color.parseColor("#005249");
			color_02 = Color.parseColor("#005249");
		}
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			switch (state) {
			case MOKHOA:
				drawOFF.setBounds(rectOnOff);
				drawOFF.draw(canvas);
				break;
			case KHOADIENTHOAI:
				drawON2.setBounds(rectOnOff);
				drawON2.draw(canvas);
				break;
			case KHOAHET:
				drawON1.setBounds(rectOnOff);
				drawON1.draw(canvas);
				break;
			default:
				break;
			}
			if (isClickPercent) {
				drawAC.setBounds(rectBut1);
				drawAC.draw(canvas);
			} else {
				drawIN.setBounds(rectBut1);
				drawIN.draw(canvas);
			}
			if (state != KHOAHET) {
				if(drawIconAC != null){
					drawIconAC.setBounds(rectIcon);
					drawIconAC.draw(canvas);
				}
			} else {
				if(drawIconIN != null){
					drawIconIN.setBounds(rectIcon);
					drawIconIN.draw(canvas);
				}
			}
			textPaint.setStyle(Style.FILL);
			textPaint.setTextSize(titleS);
			textPaint.setColor(color_01);
			canvas.drawText(title, titleX, titleY, textPaint);
			textPaint.setTextSize(stateS);
			textPaint.setColor(color_02);
			if (state != KHOAHET) {
				canvas.drawText(dataON, stateX, stateY, textPaint);
			} else {
				canvas.drawText(dataOFF, stateX, stateY, textPaint);
			}
			
			String textState = getResources().getString(R.string.dialog_melody_block_1);
			textPaint.setStyle(Style.FILL);
			textPaint.setTextSize(chamS);
			textPaint.setColor(color_02);
			canvas.drawText(textState, chamX, chamY, textPaint);
			
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			switch (state) {
			case MOKHOA:
				zlightdrawOFF.setBounds(rectOnOff);
				zlightdrawOFF.draw(canvas);
				break;
			case KHOADIENTHOAI:
				zlightdrawON2.setBounds(rectOnOff);
				zlightdrawON2.draw(canvas);
				break;
			case KHOAHET:
				zlightdrawON1.setBounds(rectOnOff);
				zlightdrawON1.draw(canvas);
				break;
			default:
				break;
			}
			if (isClickPercent) {
				zlightdrawAC.setBounds(rectBut1);
				zlightdrawAC.draw(canvas);
			} else {
				zlightdrawIN.setBounds(rectBut1);
				zlightdrawIN.draw(canvas);
			}
			if (state != KHOAHET) {
				if(drawIconAC != null){
					drawIconAC.setBounds(rectIcon);
					drawIconAC.draw(canvas);
				}
			} else {
				if(drawIconIN != null){
					drawIconIN.setBounds(rectIcon);
					drawIconIN.draw(canvas);
				}
			}
			textPaint.setStyle(Style.FILL);
			textPaint.setTextSize(titleS);
			textPaint.setColor(color_01);
			canvas.drawText(title, titleX, titleY, textPaint);
			textPaint.setTextSize(stateS);
			textPaint.setColor(color_02);
			if (state != KHOAHET) {
				if(drawIconAC != null){
					drawIconAC.setBounds(rectIcon);
					drawIconAC.draw(canvas);
				}
				canvas.drawText(dataON, stateX, stateY, textPaint);
			} else {
				canvas.drawText(dataOFF, stateX, stateY, textPaint);
			}
			
			String textState = getResources().getString(R.string.dialog_melody_block_1);
			textPaint.setStyle(Style.FILL);
			textPaint.setTextSize(chamS);
			textPaint.setColor(Color.WHITE);
			canvas.drawText(textState, chamX, chamY, textPaint);
		}
	}
	
	public static final int KHOAHET = 2;
	public static final int KHOADIENTHOAI = 1;
	public static final int MOKHOA = 0;
	private int state = MOKHOA;
	
	private int intStartKhoa = 0;
	private int intKhoaHet = 0;
	
	private int intKhoaDienT = 0;
	private boolean isClickPercent = false;
	private boolean statebool = false;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(TouchMainActivity.serverStatus != null || KTVMainActivity.serverStatus != null){
			boolean flagControlFullAPI = false;
			
			if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				if(KTVMainActivity.serverStatus.isOnOffControlFullAPI()){
					flagControlFullAPI = true;
				}
			} else {
				if(TouchMainActivity.serverStatus.isOnOffControlFullAPI()){
					flagControlFullAPI = true;
				}
			}
			
			if(flagControlFullAPI){
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:{
					float x = event.getX();
					float y = event.getY();
					if(rectBut1 != null){
						if(x >= rectBut1.left && x <= rectBut1.right &&
							y >= rectBut1.top && y <= rectBut1.bottom){
							isClickPercent = true;
							invalidate();
							break;
						}
					}
					if (x >= intStartKhoa && x <= intKhoaHet) {
						state = KHOAHET;
						invalidate();
					} else if (x >= intKhoaHet && x <= intKhoaDienT) {
						state = KHOADIENTHOAI;
						invalidate();
					} else if (x >= intKhoaDienT && x <= widthLayout) {
						state = MOKHOA;
						invalidate();
					}
				}break;
				case MotionEvent.ACTION_MOVE:{
					float x = event.getX();
					float y = event.getY();
					if(rectBut1 != null){
						if(x >= rectBut1.left && x <= rectBut1.right &&
							y >= rectBut1.top && y <= rectBut1.bottom){
							isClickPercent = true;
							invalidate();
							break;
						}else{
							isClickPercent = false;
						}
					}
					if (x >= intStartKhoa && x <= intKhoaHet) {
						state = KHOAHET;
						invalidate();
					} else if (x >= intKhoaHet && x <= intKhoaDienT) {
						state = KHOADIENTHOAI;
						invalidate();
					} else if (x >= intKhoaDienT && x <= widthLayout) {
						state = MOKHOA;
						invalidate();
					}
				}break;
				case MotionEvent.ACTION_UP:{
					float x = event.getX();
					float y = event.getY();
					isClickPercent = false;
					if(rectBut1 != null){
						if(x >= rectBut1.left && x <= rectBut1.right &&
							y >= rectBut1.top && y <= rectBut1.bottom){
							if(listener != null){
								listener.OnShowMelodyD();								
							}
							invalidate();
							break;
						}
					}
					if (x >= intStartKhoa && x <= intKhoaHet) {
						state = KHOAHET;
						invalidate();
						if(listener != null){
							longtimersync = System.currentTimeMillis();
							listener.OnCommand(this.getId(), state);
						}
					} else if (x >= intKhoaHet && x <= intKhoaDienT) {
						state = KHOADIENTHOAI;
						invalidate();
						if(listener != null){
							longtimersync = System.currentTimeMillis();
							listener.OnCommand(this.getId(), state);
						}
					} else if (x >= intKhoaDienT && x <= widthLayout) {
						state = MOKHOA;
						invalidate();
						if(listener != null){
							longtimersync = System.currentTimeMillis();
							listener.OnCommand(this.getId(), state);
						}
					} else {
						invalidate();
						if(listener != null){
							longtimersync = System.currentTimeMillis();
							listener.OnCommand(this.getId(), state);
						}
					}
				}break;
				default: break;
				}
				return true;
			} else {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:{
					float x = event.getX();
					float y = event.getY();
					if(rectBut1 != null){
						if(x >= rectBut1.left && x <= rectBut1.right &&
							y >= rectBut1.top && y <= rectBut1.bottom){
							isClickPercent = true;
							invalidate();
							break;
						}
					}
					if (x >= intStartKhoa && x <= intKhoaHet) {
						state = KHOAHET;
						invalidate();
					} else if (x >= intKhoaHet && x <= intKhoaDienT) {
						state = KHOADIENTHOAI;
						invalidate();
					} else if (x >= intKhoaDienT && x <= widthLayout) {
						state = MOKHOA;
						invalidate();
					}
				}break;
				case MotionEvent.ACTION_MOVE:{
					float x = event.getX();
					float y = event.getY();
					if(rectBut1 != null){
						if(x >= rectBut1.left && x <= rectBut1.right &&
							y >= rectBut1.top && y <= rectBut1.bottom){
							isClickPercent = true;
							invalidate();
							break;
						}else{
							isClickPercent = false;
						}
					}
					if (x >= intStartKhoa && x <= intKhoaHet) {
						state = KHOAHET;
						invalidate();
					} else if (x >= intKhoaHet && x <= intKhoaDienT) {
						state = KHOADIENTHOAI;
						invalidate();
					} else if (x >= intKhoaDienT && x <= widthLayout) {
						state = MOKHOA;
						invalidate();
					}
				}break;
				case MotionEvent.ACTION_UP:{
					float x = event.getX();
					float y = event.getY();
					isClickPercent = false;
					if(rectBut1 != null){
						if(x >= rectBut1.left && x <= rectBut1.right &&
							y >= rectBut1.top && y <= rectBut1.bottom){
							if(listener != null){
								listener.OnShowMelodyD();								
							}
							invalidate();
							break;
						}
					}
					if (x >= intStartKhoa && x <= intKhoaHet) {
						state = KHOAHET;
						invalidate();
						if(listener != null){
							longtimersync = System.currentTimeMillis();
							listener.OnCommand(this.getId(), false, false);
						}
					} else if (x >= intKhoaHet && x <= intKhoaDienT) {
						state = KHOADIENTHOAI;
						invalidate();
						if(listener != null){
							longtimersync = System.currentTimeMillis();
							listener.OnCommand(this.getId(), true, true);
						}
					} else if (x >= intKhoaDienT && x <= widthLayout) {
						state = MOKHOA;
						invalidate();
						if(listener != null){
							longtimersync = System.currentTimeMillis();
							listener.OnCommand(this.getId(), true, false);
						}
					} else {
						invalidate();
						if(listener != null){
							if(state == KHOAHET){
								listener.OnCommand(this.getId(), false, false);
							}else if(state == KHOADIENTHOAI){
								listener.OnCommand(this.getId(), true, true);
							}else if(state == MOKHOA){
								listener.OnCommand(this.getId(), true, false);
							}
						}
					}
				}break;
				default: break;
				}
				return true;
			
				
			}
		}
		return true;
	}
	
	//===============================//
	
	private long longtimersync = 0;
	public void setState(int state){
		if(System.currentTimeMillis() - longtimersync 
			<= MyApplication.TIMER_SYNC){
			return;
		}
		this.state = state;
		invalidate();
	}
	
	public int getState(){
		return state;
	}
	
	public void setStateBoolean(boolean state){
		if(System.currentTimeMillis() - longtimersync 
			<= MyApplication.TIMER_SYNC){
			return;
		}
		this.statebool = state;
		if(state == true){
			this.state = MOKHOA;
		}else{
			this.state = KHOAHET;
		}
		invalidate();
	}
	
	public boolean getStateBoolean(){
		return statebool;
	}
	
	public void setStateBoolean2(boolean state){
		this.statebool = state;
		if(state == true){
			this.state = MOKHOA;
		}else{
			this.state = KHOAHET;
		}
		invalidate();
	}
	
	private int stateScore;
	public void setStateScode(int state){
		stateScore = state;
		invalidate();
	}
	public int getStateScode(){
		return stateScore;
	}
	
	private int statePercent;
	public void setStatePercent(int state){
		statePercent = state;
		invalidate();
	}
	public int getStatePercent(){
		return statePercent;
	}	
	
	private boolean flagBlock = false;
	public void setFlagBlock(boolean flagBlock){
		this.flagBlock = flagBlock;
	}
	
	
	
	private Drawable drawIconIN;
	private Drawable drawIconAC;
	public void setDate(int state, String title, Drawable drawAC, Drawable drawIN){
		drawIconAC = drawAC;
		drawIconIN = drawIN;
		this.title = title;
		this.state = state;
		invalidate();
	}
	
	public void setDate(boolean state, String title, Drawable drawAC, Drawable drawIN){
		drawIconAC = drawAC;
		drawIconIN = drawIN;
		this.title = title;
		this.statebool = state;
		if(state == true){
			this.state = MOKHOA;
		}else{
			this.state = KHOAHET;
		}
		invalidate();
	}
	
}
