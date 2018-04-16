package vn.com.sonca.Touch.BlockCommand;

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
import android.view.View.MeasureSpec;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.zktv.main.KTVMainActivity;
import vn.com.sonca.zzzzz.MyApplication;

public class ButonOnOff extends View {
	
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	
	public ButonOnOff(Context context) {
		super(context);
		initView(context);
	}

	public ButonOnOff(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public ButonOnOff(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private OnOnOffCommandListener listener;
	public interface OnOnOffCommandListener{
		public void OnCommand(int id, int value);
		public void OnCommand(int id, boolean bool, boolean isShowDialog);
	}
	
	public void setOnOnOffCommandListener(OnOnOffCommandListener listener){
		this.listener = listener;
	}
	
	private Drawable drawON;
	private Drawable drawOFF;
	private Drawable drawOFFCU;
	private Drawable drawON_Hover;
	private Drawable drawOFF_Hover;
	private Drawable drawON1;
	private Drawable drawON2;
	private Drawable drawIconIN;
	private Drawable drawIconAC;
	
	private Drawable zlightdrawON;
	private Drawable zlightdrawOFF;
	private Drawable zlightdrawOFFCU;
	private Drawable zlightdrawON_Hover;
	private Drawable zlightdrawOFF_Hover;
	private Drawable zlightdrawON1;
	private Drawable zlightdrawON2;
	private Drawable zlightdrawIconIN;
	private Drawable zlightdrawIconAC;
	
	private String dataON = "";
	private String dataOFF = "";
	private void initView(Context context) {
		
		drawON = getResources().getDrawable(R.drawable.khoa_bat);
		drawOFF = getResources().getDrawable(R.drawable.touch_kdk_mo);
		drawOFFCU = getResources().getDrawable(R.drawable.khoa_tat);
		drawON_Hover = getResources().getDrawable(R.drawable.khoa_bat_hover);
		drawOFF_Hover = getResources().getDrawable(R.drawable.khoa_tat_hover);
		drawON1 = getResources().getDrawable(R.drawable.touch_kdk_khoahet);
		drawON2 = getResources().getDrawable(R.drawable.touch_kdk_khoadienthoai);
		/*
		drawIconIN = getResources().getDrawable(R.drawable.icon_volume_xam_tablet);
		drawIconAC = getResources().getDrawable(R.drawable.icon_volume);
		*/
		zlightdrawON = getResources().getDrawable(R.drawable.khoa_bat);
		zlightdrawOFF = getResources().getDrawable(R.drawable.zlight_kdk_mo);
		zlightdrawOFFCU = getResources().getDrawable(R.drawable.khoa_tat);
		zlightdrawON_Hover = getResources().getDrawable(R.drawable.khoa_bat_hover);
		zlightdrawOFF_Hover = getResources().getDrawable(R.drawable.khoa_tat_hover);
		zlightdrawON1 = getResources().getDrawable(R.drawable.zlight_kdk_khoahet);
		zlightdrawON2 = getResources().getDrawable(R.drawable.zlight_kdk_khoadienthoai);
		/*
		zlightdrawIconIN = getResources().getDrawable(R.drawable.icon_volume_xam_tablet);
		zlightdrawIconAC = getResources().getDrawable(R.drawable.icon_volume);
		*/
		dataON = getResources().getString(R.string.command_2);
		dataOFF = getResources().getString(R.string.command_3);
		
		title = getResources().getString(R.string.command_4c);
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
	private Rect rectBut = new Rect();
	private Rect rectIcon = new Rect();
	private Rect rectOnOff = new Rect();
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
		
		titleS = (float) (0.22*h);
		titleX = (float) (2.2*wr);
		titleY = (float) (h/2 - 0.1*titleS);
		
		stateS = (float) (0.13*h);
		stateX = titleX;
		stateY = (float) (h/2 + 1.2*stateS);
		
		tamY = (int) (0.5*h);
		hr = (int) (0.3*h);
		wr = 190*hr/100;
		rectOnOff.set(w - 2*wr, tamY - hr, w, tamY + hr);
		
		intStartKhoa = rectOnOff.left;
		intKhoaHet = intStartKhoa + 2*wr/3;
		intKhoaDienT = intStartKhoa + 4*wr/3;
		
	}
	
	private int color_01;
	private int color_02;
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
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
		}
	}
	
	public static final int KHOAHET = 2;
	public static final int KHOADIENTHOAI = 1;
	public static final int MOKHOA = 0;
	private int state = MOKHOA;
	
	private int intStartKhoa = 0;
	private int intKhoaHet = 0;
	
	private int intKhoaDienT = 0;
	private boolean isClick = false;
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
				/*
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					if(rectBut != null){
						float x = event.getX();
						float y = event.getY();
						if(x >= rectBut.left && x <= rectBut.right &&
							y >= rectBut.top && y <= rectBut.bottom){
							isClick = true;
							invalidate();
						}
					}
					break;
				case MotionEvent.ACTION_UP:
					isClick = false;
					invalidate();
					if(rectBut != null){
						float x = event.getX();
						float y = event.getY();
						if(x >= rectBut.left && x <= rectBut.right &&
							y >= rectBut.top && y <= rectBut.bottom){
							statebool = !statebool;
							if(listener != null){
								longtimersync = System.currentTimeMillis();
								listener.OnCommand(this.getId() , statebool);
							}
						}
					}
					break;
				default: break;
				}
				return true;
				*/
				

				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:{
					float x = event.getX();
					float y = event.getY();
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
	
	private String title = "";
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
	
}
