package vn.com.sonca.SetttingApp;

import vn.com.hanhphuc.karremote.R;
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

public class ButtonSettingView extends View {
	
	private TextPaint paintText = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	
	public ButtonSettingView(Context context) {
		super(context);
		initView(context);
	}

	public ButtonSettingView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public ButtonSettingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private OnButSettingListener listener;
	public interface OnButSettingListener {
		public void OnButClick();
	}
	
	public void setOnButSettingListener(OnButSettingListener listener){
		this.listener = listener;
	}
	
	private String name = "OK";
	private Drawable drawAC;
	private Drawable drawHO;
	
	private Drawable zlightdrawAC;
	private Drawable zlightdrawHO;
	private void initView(Context context) {
		drawAC = getResources().getDrawable(R.drawable.boder_lammoi);
		drawHO = getResources().getDrawable(R.drawable.boder_lammoi_hover);
		zlightdrawAC = getResources().getDrawable(R.drawable.zlight_boder_ketnoi);
		zlightdrawHO = getResources().getDrawable(R.drawable.zlight_boder_ketnoi_hover);
		name = "OK";
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int height = (int) (0.06*getResources().getDisplayMetrics().heightPixels);
		int myWidth = (int) (2.5*height);
		setMeasuredDimension(myWidth , height);
	}
	
	private float nameS, nameX, nameY;
	private Rect rectB = new Rect();
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		rectB.set(0, 0, w, h);
		
		nameS = (float) (0.4*h);
		paintText.setTextSize(nameS);
		float wi = paintText.measureText(name);
		nameX = (float) (w/2 - wi/2);
		nameY = (float) (0.5*h + 0.35*nameS);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			if (isClick) {
				drawHO.setBounds(rectB);
				drawHO.draw(canvas);
			} else {
				drawAC.setBounds(rectB);
				drawAC.draw(canvas);
			}
			paintText.setStyle(Style.FILL);
			paintText.setTextSize(nameS);
			paintText.setARGB(255, 180, 253, 255);
			canvas.drawText(name, nameX, nameY, paintText);
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			if (isClick) {
				zlightdrawHO.setBounds(rectB);
				zlightdrawHO.draw(canvas);
			} else {
				zlightdrawAC.setBounds(rectB);
				zlightdrawAC.draw(canvas);
			}
			paintText.setStyle(Style.FILL);
			paintText.setTextSize(nameS);
			paintText.setColor(Color.parseColor("#FFFFFF"));
			canvas.drawText(name, nameX, nameY, paintText);
		}
		
	}
	
	
	private boolean isClick = false;
/*
	@Override
	public void setPressed(boolean pressed) {
		super.setPressed(pressed);
		isClick = pressed;
		invalidate();
	}
*/
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isClick = true;
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			isClick = false;
			invalidate();
			if(listener != null){
				listener.OnButClick();
			}
			break;
		default:
			break;
		}
		return true;
	}
	
}
