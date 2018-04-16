package app.sonca.flower;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import vn.com.hanhphuc.karremote.R;
import android.view.View.MeasureSpec;

public class GroudRightFlower extends ViewGroup {
	
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);

	public GroudRightFlower(Context context) {
		super(context);
		initView(context);
	}

	public GroudRightFlower(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public GroudRightFlower(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private String title, title2;
	private void initView(Context context) {
		title = getResources().getString(R.string.doxucxac1);
		title2 = getResources().getString(R.string.tang_3);
		setWillNotDraw(false);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	private int text1S, text1X, text1Y;
	private int text2S, text2X, text2Y;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		text1S = (int) (0.05*h);
		text1X = (int) (0.5*w);
		text1Y = (int) (0.07*h);
		
		text2S = (int) (0.05*h);
		text2X = (int) (0.1*w);
		text2Y = (int) (0.33*h);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		textPaint.setARGB(255, 180, 254, 255);
		textPaint.setTextAlign(Align.CENTER);
		textPaint.setTextSize(text1S);
		canvas.drawText(title, text1X, text1Y, textPaint);
		canvas.drawText(title2, text1X, text2Y, textPaint);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int width = getWidth();
		int height = getHeight();
				
		int left = (int) (0.3*width);
		int right = (int) (0.7*width);
		int top = (int) (0.11*height);
		int bottom = (int) (0.23*height);
		View but = getChildAt(0);
		but.layout(left, top, right, bottom);

		top = (int) (0.85*height);
		bottom = (int) (0.97*height);
		View but2 = getChildAt(1);
		but2.layout(left, top, right, bottom);
		
		top = (int) (0.36*height);
		bottom = (int) (0.66*height);
		left = (int) (0.35*width);
		right = left + (bottom - top);
		View luckroll = getChildAt(2);
		luckroll.layout(left, top, right, bottom);
		
		top = (int) (0.68*height);
		bottom = (int) (0.85*height);
		left = (int) (0.1*width);
		right = (int) (0.9*width);
		View txtResult = getChildAt(3);
		txtResult.layout(left, top, right, bottom);		
		
	}
	

}
