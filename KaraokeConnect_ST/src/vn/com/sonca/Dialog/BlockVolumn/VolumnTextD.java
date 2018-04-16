package vn.com.sonca.Dialog.BlockVolumn;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import vn.com.sonca.zzzzz.MyApplication;

public class VolumnTextD extends View {
	
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	
	public VolumnTextD(Context context) {
		super(context);
		initView(context);
	}

	public VolumnTextD(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public VolumnTextD(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private String textLine1 = "Volumn DANCE";
	private String textLine2 = "Tinh chinh volumn mac dinh";
	private String textLine3 = "cho che do DANCE";
	private void initView(Context context) {
		textPaint.setStyle(Style.FILL);
		textPaint.setTextAlign(Align.RIGHT);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
	}
	
	private int line1S, line1X, line1Y;
	private int line2S, line2X, line2Y;
	private int line3S, line3X, line3Y;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		line1S = (int) (0.37*h);
		line1X = (int) (w - 10);
		line1Y = (int) (0.22*h + 0.5*line1S);
		
		line2S = (int) (0.2*h);
		line2X = (int) (w - 10);
		line2Y = (int) (0.55*h + 0.5*line2S);
		
		line3S = line2S;
		line3X = (int) (w - 10);
		line3Y = (int) (0.8*h + 0.5*line3S);
		
	}
	
	private int color_01 = 0;
	private int color_02 = 0;
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			color_01 = Color.argb(255, 0, 253, 253);
			color_02 = Color.argb(255, 180, 254, 255);
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			color_01 = Color.argb(255, 255, 255, 255);
			color_02 = Color.argb(255, 0, 82, 73);
		}
		
		textPaint.setTextSize(line1S);
		textPaint.setColor(color_01);
		textPaint.setTypeface(Typeface.DEFAULT_BOLD);
		canvas.drawText(textLine1, line1X, line1Y, textPaint);
		
		textPaint.setTextSize(line2S);
		textPaint.setColor(color_02);
		textPaint.setTypeface(Typeface.DEFAULT);
		canvas.drawText(textLine2, line2X, line2Y, textPaint);
		
		textPaint.setTextSize(line3S);
		textPaint.setColor(color_02);
		textPaint.setTypeface(Typeface.DEFAULT);
		canvas.drawText(textLine3, line3X, line3Y, textPaint);
		
	}
	
	public void setTextView(String line1, String line2, String line3){
		textLine1 = line1;
		textLine2 = line2;
		textLine3 = line3;
		invalidate();
	}
	

}
