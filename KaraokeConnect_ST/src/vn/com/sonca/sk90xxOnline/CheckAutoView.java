package vn.com.sonca.sk90xxOnline;

import vn.com.hanhphuc.karremote.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;

public class CheckAutoView extends View {
	
	private int widthLayout = 0;
	private int heightLayout = 0;
	
	public CheckAutoView(Context context) {
		super(context);
		initView(context);
	}

	public CheckAutoView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public CheckAutoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private OnCheckAutoListener listener;
	public interface OnCheckAutoListener {
		public void OnCheckAuto(boolean flagCheck);
	}
	
	public void setOnCheckAutoListener(OnCheckAutoListener listener){
		this.listener = listener;
	}
	
	private Drawable drawableOn, drawableOff;
	private void initView(Context context) {
		drawableOn = getResources().getDrawable(R.drawable.touch_icon_check_fw_on);
		drawableOff = getResources().getDrawable(R.drawable.touch_icon_check_off);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int height = (int) (0.5*MeasureSpec.getSize(heightMeasureSpec));
		int width = height;
		setMeasuredDimension(width, height);
	}
	
	private Rect rect = new Rect();
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		widthLayout = w;
		heightLayout = h;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int tamX = (int) (0.652*widthLayout);
		int tamY = (int) (0.47*heightLayout);
		int hr = (int) (0.7*heightLayout);
		rect = new Rect(tamX - hr/2, tamY - hr/2, tamX + hr/2, tamY + hr/2);
		
		if(flagCheck){
			drawableOn.setBounds(rect);
			drawableOn.draw(canvas);
		} else {
			drawableOff.setBounds(rect);
			drawableOff.draw(canvas);
		}
		
			
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			float x = event.getX();
			float y = event.getY();
			if(x >= rect.left && x <= rect.right){
				flagCheck = !flagCheck;
				if(listener != null){
					listener.OnCheckAuto(flagCheck);
				}
				invalidate();
				return true;
			}
		}
		return false;
	}

	private boolean flagCheck = false;
	public boolean getCheckState(){
		return this.flagCheck;
	}
	
	public void setCheckState(boolean flagCheck){
		this.flagCheck = flagCheck;
		invalidate();
	}
}
