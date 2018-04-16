package app.sonca.flower;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.EditText;
import vn.com.hanhphuc.karremote.R;

public class GroupLeftFlower extends ViewGroup {
	
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);

	public GroupLeftFlower(Context context) {
		super(context);
		initView(context);
	}

	public GroupLeftFlower(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public GroupLeftFlower(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Context context;
	private String title1, title2;
	private String name1, name2, name3;
	private void initView(Context context) {
		title1 = getResources().getString(R.string.thongtinnguoitang);
		title2 = getResources().getString(R.string.thongtinbaihat);
		name1 = getResources().getString(R.string.chonavatar);
		name2 = getResources().getString(R.string.sohoamuontang);
		name3 = getResources().getString(R.string.baidanghat);
		textPaint.setStyle(Style.FILL);
		setWillNotDraw(false);
		
		paintMain.setStyle(Style.FILL);
		paintMain.setColor(Color.RED);
		
		this.context = context;
		
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	private Rect rectTest = new Rect();
	private int textT1X, textT1Y, textT1S;
	private int textT2X, textT2Y, textT2S;
	private int textA1X, textA1Y, textA1S;
	private int textA2X, textA2Y, textA2S;
	private int textA3X, textA3Y, textA3S;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		rectTest.set(0, 0, w, h);
		
		textT1S = textT2S = (int) (0.05*h);
		textT1X = textT2X = (int) (0.5*w);
		textT1Y = (int) (0.07*h);
		textT2Y = (int) (0.68*h);
		
		textA1S = textA2S = textA3S = (int) (0.04*h);
		textA1X = textA2X = textA3X = (int) (0.1*w);
		textA1Y = (int) (0.13*h);
		textA2Y = (int) (0.33*h);
		textA3Y = (int) (0.76*h);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		textPaint.setARGB(255, 180, 254, 255);
		textPaint.setTextAlign(Align.CENTER);
		textPaint.setTextSize(textT1S);
		canvas.drawText(title1, textT1X, textT1Y, textPaint);
		// canvas.drawText(title2, textT2X, textT2Y, textPaint);
		textPaint.setARGB(255, 1, 165, 254);
		textPaint.setTextAlign(Align.LEFT);
		textPaint.setTextSize(textT1S);
		canvas.drawText(name1, textA1X, textA1Y, textPaint);
		canvas.drawText(name2, textA2X, textA2Y, textPaint);
		// canvas.drawText(name3, textA3X, textA3Y, textPaint);
		
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int width = getWidth();
		int height = getHeight();
		
		int left = (int) (0.1*width);
		int right = (int) (0.9*width);
		int top = (int) (0.14*height);
		int bottom = (int) (0.28*height);
		/*
		left = (int) (0.1*width);
		right = (int) (0.9*width);
		top = (int) (0.37*height);
		bottom = (int) (0.55*height);
		*/
		ViewGroup avatar = (ViewGroup)getChildAt(0);
		avatar.measure(
				MeasureSpec.makeMeasureSpec(right-left, MeasureSpec.EXACTLY), 
				MeasureSpec.makeMeasureSpec(bottom-top, MeasureSpec.EXACTLY));
		avatar.layout(left, top, right, bottom);
		
		left = (int) (0.1*width);
		right = (int) (0.9*width);
		top = (int) (0.36*height);
		bottom = (int) (0.83*height);
		ViewGroup flower = (ViewGroup)getChildAt(1);
		flower.measure(
				MeasureSpec.makeMeasureSpec(right-left, MeasureSpec.EXACTLY), 
				MeasureSpec.makeMeasureSpec(bottom-top, MeasureSpec.EXACTLY));
		flower.layout(left, top, right, bottom);
		
		left = (int) (0.15*width);
		right = (int) (0.55*width);
		top = (int) (0.85*height);
		bottom = (int) (0.97*height);
		View but = getChildAt(2);
		but.layout(left, top, right, bottom);
		
		
	}
	
	private float pixelsToSp(Context context, float px) {
	    float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
	    return px/scaledDensity;
	}

}
