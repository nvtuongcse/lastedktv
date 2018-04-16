package app.sonca.Dialog.ScoreLayout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

public class ScoreItem extends View {
	
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public ScoreItem(Context context) {
		super(context);
		initView(context);
	}

	public ScoreItem(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public ScoreItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private void initView(Context context) {}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		setMeasuredDimension(width, height);
	}
	
	private int lineY, lineW;
	private int paddingLine;
	private int textS, textX, textY;
	private int textSS, textXX, textYY;
	private Rect rectImage = new Rect();
	private RectF rectLine = new RectF();
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		paddingLine = (int) (0.07*w);
		lineW = 2;
		lineY = h - lineW;

		int tamY = (int) (0.45*h);
		int tamW = (int) (0.3*h);
		int tamH = tamW;
		int tamX = (int) (tamH + 0.07*h);
		rectImage.set(tamX - tamW, tamY - tamW, tamX + tamH, tamY + tamW);
		rectLine.set(paddingLine, lineY - lineW, w - paddingLine, lineY + lineW);
		
		textS = (int) (0.9*h);
		textX = (int) (0.5*w);
		textY = (int) (0.5*h + 0.4*textS);
		
		textSS = (int) (0.9*h);
		textXX = (int) (0.1*w);
		textYY = (int) (0.5*h + 0.4*textS);
		
		textPaint.setStyle(Style.FILL);
		textPaint.setTypeface(Typeface.DEFAULT_BOLD);
		
	}
	
	private String textScore = "50";
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
/*		
		paintMain.setStyle(Style.FILL);
		paintMain.setColor(Color.GREEN);
		canvas.drawRect(rectImage, paintMain);
		canvas.drawRoundRect(rectLine, lineW, lineW, paintMain);
*/		
		
		textPaint.setTextSize(textS);
		textPaint.setARGB(255, 255, 228, 0);
		textPaint.setTextAlign(Align.LEFT);
		canvas.drawText(textScore, textX, textY, textPaint);
		textPaint.setTextSize(textSS);
		textPaint.setTextAlign(Align.CENTER);
		canvas.drawText(lineScore, textXX, textYY, textPaint);
		
	}
	
	private String lineScore = "0";
	public void setValueScore(int line, int value){
		textScore = value + "";
		lineScore = line + "";
	}
	
	public void updateView(){
		post(new Runnable() {
			@Override public void run() {
				invalidate();
			}
		});
	}


}
