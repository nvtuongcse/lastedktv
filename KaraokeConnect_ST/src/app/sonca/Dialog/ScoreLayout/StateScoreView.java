package app.sonca.Dialog.ScoreLayout;

import vn.com.sonca.MyLog.MyLog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

public class StateScoreView extends View {
	
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private TextPaint textStroke = new TextPaint(Paint.ANTI_ALIAS_FLAG);

	public StateScoreView(Context context) {
		super(context);
		initView(context);
	}

	public StateScoreView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public StateScoreView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private void initView(Context context) {}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	private int textX, textY, textS;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		textS = (int) (0.5*h);
		textX = (int) (0.5*w);
		textY = (int) (0.05*h + textS);
		
		textPaint.setTypeface(Typeface.DEFAULT_BOLD);
		textPaint.setStyle(Style.FILL);
		textPaint.setColor(Color.WHITE);
		textPaint.setTextSize(textS);
		
		textStroke.setTypeface(Typeface.DEFAULT_BOLD);
		textStroke.setStyle(Style.STROKE);
		textStroke.setColor(Color.WHITE);
		textStroke.setTextSize(textS);
		textStroke.setStrokeWidth((int) (0.03*textS));
	}
	
	private String textScore = "Chuyen Nghiep";
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		MyLog.e("StateScoreView", "onDraw : " + textScore);
		float w = (float) (textX - 0.5*textPaint.measureText(textScore));
		canvas.drawText(textScore, w, textY, textPaint);
		canvas.drawText(textScore, w, textY, textStroke);
	}
	
	public void setTextScore(String namestate){
		MyLog.e("StateScoreView", "setTextScore : " + namestate);
		textScore = namestate;
		invalidate();
	}
	
}
