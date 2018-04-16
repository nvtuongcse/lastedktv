package vn.com.sonca.Touch.Hi_W;

import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;

public class MyTextView extends View {
	
	private Paint mainText = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public MyTextView(Context context) {
		super(context);
		initView(context);
	}

	public MyTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public MyTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private void initView(Context context) {}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = (int) (0.10*width);
		setMeasuredDimension(width, height);
	}

	private int titleS, titleX, titleY;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		titleS = (int) (0.7*h);
		titleY = (int) (0.5*h + 0.3*titleS);
		titleX = (int) (0.5*w);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			mainText.setStyle(Style.FILL);
			mainText.setARGB(255, 0, 253, 253);
			mainText.setTextSize(titleS);
			float xx = (float) (titleX - 0.5*mainText.measureText(title));
			canvas.drawText(title, xx, titleY, mainText);
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			mainText.setStyle(Style.FILL);
			mainText.setColor(Color.parseColor("#005249"));
			mainText.setTextSize(titleS);
			float xx = (float) (titleX - 0.5*mainText.measureText(title));
			canvas.drawText(title, xx, titleY, mainText);
		}
		
	}
	
	private String title = "Thong tin dau may";
	public void setTitleView(String title){
		this.title = title;
		invalidate();
	}
	
	
	
}
