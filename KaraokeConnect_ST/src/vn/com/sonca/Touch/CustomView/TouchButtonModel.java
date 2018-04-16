package vn.com.sonca.Touch.CustomView;

import vn.com.hanhphuc.karremote.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

public class TouchButtonModel extends View {
	
	private TextPaint paintText = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	
	public TouchButtonModel(Context context) {
		super(context);
		initView(context);
	}

	public TouchButtonModel(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchButtonModel(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private String name = "CHON";
	private Drawable drawAC;
	private Drawable drawHO;
	private void initView(Context context) {
		drawAC = getResources().getDrawable(R.drawable.boder_lammoi);
		drawHO = getResources().getDrawable(R.drawable.boder_lammoi_hover);
		name = getResources().getString(R.string.top_model_1);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int height = (int) (0.08*getResources().getDisplayMetrics().heightPixels);
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
		
	}
	
	
	private boolean isClick = false;
	@Override
	public void setPressed(boolean pressed) {
		super.setPressed(pressed);
		isClick = pressed;
		invalidate();
	}

}
