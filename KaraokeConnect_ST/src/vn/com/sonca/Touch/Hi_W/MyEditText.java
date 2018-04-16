package vn.com.sonca.Touch.Hi_W;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnFocusChangeListener;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.zzzzz.MyApplication;

public class MyEditText extends View {
	
	private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint mainText = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public MyEditText(Context context) {
		super(context);
		initView(context);
	}

	public MyEditText(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public MyEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Drawable drawFocusNO;
	private Drawable drawFocusOK;
	private Drawable drawInActive;
	
	private Drawable zlightdrawFocusNO;
	private Drawable zlightdrawFocusOK;
	private Drawable zlightdrawInActive;
	
	private void initView(Context context) {
		drawFocusNO = getResources().getDrawable(R.drawable.boder_lammoi);
		drawFocusOK = getResources().getDrawable(R.drawable.boder_lammoi_hover);
		drawInActive =  getResources().getDrawable(R.drawable.touch_boder_xam_hiw);
		
		zlightdrawFocusNO = getResources().getDrawable(R.drawable.zlight_boder_nhapip_active);
		zlightdrawFocusOK = getResources().getDrawable(R.drawable.zlight_boder_nhapip_hover);
		zlightdrawInActive =  getResources().getDrawable(R.drawable.zlight_boder_default_xam);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = (int) (0.22*width);
		setMeasuredDimension(width, height);
	}

	private int titleS, titleX, titleY;
	private int dataS, dataX, dataY;
	private Rect rectImage = new Rect();
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		titleS = (int) (0.22*h);
		titleY = (int) (0.22*h + 0.3*titleS);
		titleX = (int) (0.07*w);
		
		rectImage.set(titleX, (int) (0.35*h), w - titleX, (int) (0.9*h));
		
		
		dataS = (int) (0.3*h);
		dataY = (int) (0.5*(0.35*h + 0.9*h) + 0.5*titleS);
		dataX = (int) (0.1*w);
		
	}
	
	private int color_01;
	private int color_02;
	private int color_03;
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		/*
		 * isActive = false; isFocus = false; MyApplication.intColorScreen =
		 * MyApplication.SCREEN_LIGHT;
		 */
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			color_01 = Color.argb(255, 180, 253, 253);
			color_02 = Color.GRAY;
			color_03 = Color.GREEN;
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			color_01 = Color.parseColor("#005249");
			color_02 = Color.parseColor("#FFFFFF");
			color_03 = Color.parseColor("#21BAA9");
		}

		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			if (!isActive) {
				drawInActive.setBounds(rectImage);
				drawInActive.draw(canvas);
				mainText.setStyle(Style.FILL);
				mainText.setColor(color_01);
				mainText.setTextSize(titleS);
				canvas.drawText(title, titleX, titleY, mainText);
				mainText.setStyle(Style.FILL);
				mainText.setTextSize(dataS);
				mainText.setColor(color_02);
				canvas.drawText(data, dataX, dataY, mainText);
			} else {
				if (isFocus) {
					drawFocusOK.setBounds(rectImage);
					drawFocusOK.draw(canvas);
				} else {
					drawFocusNO.setBounds(rectImage);
					drawFocusNO.draw(canvas);
				}
				mainText.setStyle(Style.FILL);
				mainText.setColor(color_01);
				mainText.setTextSize(titleS);
				canvas.drawText(title, titleX, titleY, mainText);
				mainText.setStyle(Style.FILL);
				mainText.setTextSize(dataS);
				if (isFocus) {
					mainText.setColor(color_03);
				} else {
					mainText.setColor(color_01);
				}
				canvas.drawText(data, dataX, dataY, mainText);
			}
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			if (!isActive) {
				zlightdrawInActive.setBounds(rectImage);
				zlightdrawInActive.draw(canvas);
				mainText.setStyle(Style.FILL);
				mainText.setColor(color_01);
				mainText.setTextSize(titleS);
				canvas.drawText(title, titleX, titleY, mainText);
				mainText.setStyle(Style.FILL);
				mainText.setTextSize(dataS);
				mainText.setColor(color_02);
				canvas.drawText(data, dataX, dataY, mainText);
			} else {
				if (isFocus) {
					zlightdrawFocusOK.setBounds(rectImage);
					zlightdrawFocusOK.draw(canvas);
				} else {
					zlightdrawFocusNO.setBounds(rectImage);
					zlightdrawFocusNO.draw(canvas);
				}
				mainText.setStyle(Style.FILL);
				mainText.setColor(color_01);
				mainText.setTextSize(titleS);
				canvas.drawText(title, titleX, titleY, mainText);
				mainText.setStyle(Style.FILL);
				mainText.setTextSize(dataS);
				if (isFocus) {
					mainText.setColor(color_03);
				} else {
					mainText.setColor(color_01);
				}
				canvas.drawText(data, dataX, dataY, mainText);
			}
		}
	}
	
	private String title = "Mat khau Wifi (toi thieu 8 chu hoac so)";
	public void setTitleView(String title){
		this.title = title;
		invalidate();
	}
	public String getTitleView(){
		return title;
	}
	
	private String data = "";
	public void setDataView(String data){
		this.data = data;
		invalidate();
	}
	public String getDataView(){
		return data;
	}
	
	private boolean isActive = true;
	public void setActiveView(boolean isActive){
		this.isActive = isActive;
		invalidate();
	}
	private int intConstant = 0;
	public void setConstantView(int intConstant){
		this.intConstant = intConstant;
	}
	public int getConstantView(){
		return intConstant;
	}
	
	private boolean isFocus = false;
	/*
	@Override
	public void setFocusable(boolean focusable) {
		super.setFocusable(focusable);
		isFocus = focusable;
		invalidate();
	}
	
	@Override
	public void clearFocus() {
		super.clearFocus();
		isFocus = false;
		invalidate();
	}
	*/
	private OnFocusChangeListener listener;
	@Override
	public void setOnFocusChangeListener(OnFocusChangeListener l) {
		super.setOnFocusChangeListener(l);
		listener = l;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isActive == false) {
			return true;
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:{
			float x = event.getX();
			float y = event.getY();
			if(rectImage != null){
				if(x >= rectImage.left && x <= rectImage.right &&
					y >= rectImage.top && y <= rectImage.bottom){
					isFocus = true;
					invalidate();
				}
			}
			break;
		}
		case MotionEvent.ACTION_UP:{
			isFocus = false;
			invalidate();
			float x = event.getX();
			float y = event.getY();
			if(rectImage != null){
				if(x >= rectImage.left && x <= rectImage.right &&
					y >= rectImage.top && y <= rectImage.bottom){
					if(listener != null){
						listener.onFocusChange(this, true);
					}
				}
			}
		}break;
		default: break;
		}
		return true;
	}
	
}
