package vn.com.sonca.Touch.CustomView;

import vn.com.hanhphuc.karremote.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

public class TouchConnectSuccessView extends View {
	
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	
	public TouchConnectSuccessView(Context context) {
		super(context);
		initView(context);
	}

	public TouchConnectSuccessView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchConnectSuccessView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Drawable drawBR;
	private String stringData;
	private void initView(Context context) {
		drawBR = getResources().getDrawable(R.drawable.zgreen_icon_boder_popup);
		stringData = getResources().getString(R.string.conect_success);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = getResources().getDisplayMetrics().widthPixels;
		int height = getResources().getDisplayMetrics().heightPixels;
		setMeasuredDimension(width, height);
	}

	private Rect rectBR = new Rect();
	private float textX, textY, textS;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		textS = (float) (0.05*h);
		textY = (float) (h/2 + 0.5*textS);
		textPaint.setTextSize(textS);
		float width = textPaint.measureText(stringData);
		textX = w/2 - width/2;
		
		
		rectBR.set((int)(textX - 1.5*textS), 
				(int)(textY - 2.35*textS), 
				(int)(textX + width + 1.5*textS), 
				(int)(textY + 1.65*textS));
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		drawBR.setBounds(rectBR);
		drawBR.draw(canvas);
		
		/*
		textPaint.setStyle(Style.FILL);
		textPaint.setARGB(255, 127, 127, 127);
		canvas.drawRect(rectBR, textPaint);
		*/
		textPaint.setStyle(Style.FILL);
		textPaint.setTextSize(textS);
		textPaint.setARGB(255, 255, 255, 255);
		canvas.drawText(stringData, textX, textY, textPaint);
		
		
	}
	

}
