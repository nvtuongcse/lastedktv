package vn.com.sonca.Touch.Hi_W;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnFocusChangeListener;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.zzzzz.MyApplication;

public class CheckPassWifiView extends View {
	
	private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint mainText = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public CheckPassWifiView(Context context) {
		super(context);
		initView(context);
	}

	public CheckPassWifiView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public CheckPassWifiView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Drawable drawFocusNO;
	private Drawable drawFocusOK;
	private Drawable drawInActive;
	private Drawable drawCheckNO;
	private Drawable drawCheckOK;
	
	private Drawable zlightdrawFocusNO;
	private Drawable zlightdrawFocusOK;
	private Drawable zlightdrawInActive;
	private Drawable zlightdrawCheckNO;
	private Drawable zlightdrawCheckOK;
	
	private void initView(Context context) {
		drawFocusNO = getResources().getDrawable(R.drawable.boder_lammoi);
		drawFocusOK = getResources().getDrawable(R.drawable.boder_lammoi_hover);
		drawInActive =  getResources().getDrawable(R.drawable.touch_boder_xam_hiw);
		drawCheckNO = getResources().getDrawable(R.drawable.touch_icon_check_off);
		drawCheckOK = getResources().getDrawable(R.drawable.touch_icon_check_fw_on);
		
		zlightdrawFocusNO = getResources().getDrawable(R.drawable.zlight_boder_nhapip_active);
		zlightdrawFocusOK = getResources().getDrawable(R.drawable.zlight_boder_nhapip_hover);
		zlightdrawInActive =  getResources().getDrawable(R.drawable.zlight_boder_default_xam);
		zlightdrawCheckNO = getResources().getDrawable(R.drawable.zlight_icon_check_off);
		zlightdrawCheckOK = getResources().getDrawable(R.drawable.zlight_icon_check_fw_on);
				
		title = getResources().getString(R.string.hiw_text_center_2);
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
	private Rect rectCheck = new Rect();
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		int tx = (int) (0.07*w);
		int hw = (int) (0.08*w); 
		rectCheck.set(tx, 0, tx + hw, hw);
		
		titleS = (int) (0.22*h);
		titleY = (int) (0.22*h + 0.3*titleS);
		titleX = rectCheck.right + 2;
		
		rectImage.set(tx, (int) (0.35*h), w - tx, (int) (0.9*h));
		
		
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
		 * isActive = false; isFocus = false; isCheck = true;
		 */
		// MyApplication.intColorScreen = MyApplication.SCREEN_LIGHT;
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
				if (isCheck) {
					drawCheckOK.setBounds(rectCheck);
					drawCheckOK.draw(canvas);
				} else {
					drawCheckNO.setBounds(rectCheck);
					drawCheckNO.draw(canvas);
				}
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
				if (isCheck) {
					drawCheckOK.setBounds(rectCheck);
					drawCheckOK.draw(canvas);
				} else {
					drawCheckNO.setBounds(rectCheck);
					drawCheckNO.draw(canvas);
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
				if (isCheck) {
					zlightdrawCheckOK.setBounds(rectCheck);
					zlightdrawCheckOK.draw(canvas);
				} else {
					zlightdrawCheckNO.setBounds(rectCheck);
					zlightdrawCheckNO.draw(canvas);
				}
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
				if (isCheck) {
					zlightdrawCheckOK.setBounds(rectCheck);
					zlightdrawCheckOK.draw(canvas);
				} else {
					zlightdrawCheckNO.setBounds(rectCheck);
					zlightdrawCheckNO.draw(canvas);
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
	
	private String title = "";
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
	public void setCheckView(boolean isActive){
		this.isActive = isActive;
		this.isCheck = isActive;
		invalidate();
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
	
	private OnCheckChangeListener listenerCheck;
	public interface OnCheckChangeListener{
		public void OnCheckChange(View view, boolean isCheck);
	}
	
	public void setOnCheckChangeListener(OnCheckChangeListener listenerCheck){
		this.listenerCheck = listenerCheck;
	}
	
	private boolean isCheck = true;
	public boolean isCheckView() {
		return isCheck;
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:{
			float x = event.getX();
			float y = event.getY();
			if(rectImage != null){
				if (x >= rectImage.left && x <= rectImage.right
						&& y >= rectImage.top && y <= rectImage.bottom
						&& MyApplication.intWifiRemote == MyApplication.SONCA) {
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
				if (x >= rectImage.left && x <= rectImage.right
						&& y >= rectImage.top && y <= rectImage.bottom
						&& MyApplication.intWifiRemote == MyApplication.SONCA) {
					if (listener != null && isActive == true) {
						listener.onFocusChange(this, true);
					}
				}
			}
			if(rectCheck != null){
				if (x >= rectCheck.left && x <= rectCheck.right
						&& y >= rectCheck.top && y <= rectCheck.bottom
						&& MyApplication.intWifiRemote == MyApplication.SONCA) {
					isCheck = !isCheck;
					isActive = isCheck;
					invalidate();
					if (listenerCheck != null) {
						listenerCheck.OnCheckChange(this, isCheck);
					}
				}
			}
		}break;
		default: break;
		}
		return true;
	}

	private int intMinimum = 0;
	
	public int getIntMinimum() {
		return intMinimum;
	}

	public void setIntMinimum(int intMinimum) {
		this.intMinimum = intMinimum;
	}
	
}
