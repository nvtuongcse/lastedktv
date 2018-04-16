package app.sonca.Dialog.ScoreLayout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

public class ScoreBackgroud extends View {
	
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public ScoreBackgroud(Context context) {
		super(context);
		initView(context);
	}

	public ScoreBackgroud(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public ScoreBackgroud(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private void initView(Context context) {}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(width, height);
	}

	private int paddingFrame = 0;
	private RectF resctBackgroud = new RectF();
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		int padding = 5;
		paddingFrame = 2;
		resctBackgroud.set(padding, padding, w - padding, h - padding);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		paintMain.setStyle(Style.FILL);
		paintMain.setARGB(255, 146, 150, 155);
		canvas.drawRoundRect(resctBackgroud, 10, 10, paintMain);
		
		paintMain.setStyle(Style.STROKE);
		paintMain.setARGB(255, 168, 171, 174);
		paintMain.setStrokeWidth(paddingFrame);
		canvas.drawRoundRect(resctBackgroud, 10, 10, paintMain);	
		
	}


}
