package app.sonca.Dialog.ScoreLayout;

import vn.com.hanhphuc.karremote.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

public class MyTextScore extends View {

	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private TextPaint textStroke = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public MyTextScore(Context context) {
		super(context);
		initView(context);
	}

	public MyTextScore(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public MyTextScore(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	

	private String text = "";
	private void initView(Context context) {
		text = getResources().getString(R.string.dialog_score_text_0);
		int color = Color.argb(178, 77, 208, 225);
		textPaint.setStyle(Style.FILL);
		textPaint.setColor(Color.WHITE);
		textPaint.setTextAlign(Align.CENTER);
		paintMain.setStyle(Style.FILL);
		paintMain.setColor(color);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	private Rect rect = new Rect();
	private int textS, textX, textY;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		rect.set(0, 0, w, h);
		textS = (int) (0.7*h);
		textX = (int) (0.5*w);
		textY = (int) (0.5*h + 0.35*textS);

		textPaint.setTypeface(Typeface.DEFAULT_BOLD);
		textPaint.setStyle(Style.FILL);
		textPaint.setARGB(255, 2, 98, 147);
		textPaint.setTextSize(textS);

		textStroke.setTypeface(Typeface.DEFAULT_BOLD);
		textStroke.setStyle(Style.STROKE);
		textStroke.setColor(Color.WHITE);
		textStroke.setTextSize(textS);
		textStroke.setStrokeWidth((int) (0.03*textS));
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// canvas.drawRect(rect, paintMain);
		float w = (float) (textX - 0.5*textPaint.measureText(text));
		canvas.drawText(text, textX, textY, textPaint);
		canvas.drawText(text, w, textY, textStroke);
	}
	
	

}
