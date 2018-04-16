package app.sonca.Dialog.ScoreLayout;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

public class ScoreFrameView extends View {
	
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public ScoreFrameView(Context context) {
		super(context);
		initView(context);
	}

	public ScoreFrameView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public ScoreFrameView(Context context, AttributeSet attrs, int defStyle) {
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
	
	private int rcicle = 0;
	private int strokeFrame = 5;
	private int strokeText = 5;
	private int textS, textX, textY;
	private RectF rectFrame = new RectF();
	private Rect rectImage = new Rect();
	private int topImage, bottomImage;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		int padding = 10;
		
		strokeFrame = 4;
		textS = (int) (0.12*h);
		textX = (int) (0.5*w);
		strokeText = (int) (0.05*textS);
		int paddingTop = (int) (0.9*textS);
		textY = (int) (paddingTop + 0.25*textS);
		rectFrame.set(padding, paddingTop, w - padding, h - padding);

		textPaint.setTypeface(Typeface.DEFAULT_BOLD);
		textPaint.setTextSize(textS);
		
		rcicle = padding;
		
		int tamX = 0;
		int tamY = (int) (0.08*h);
		int height = (int) (0.12*h);
		topImage = (int) (tamY - 0.5*height);
		bottomImage = (int) (tamY + 0.5*height);
		
	}
	
	// private String textFrame = "Your Score";
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		paintMain.setStyle(Style.FILL);
		paintMain.setARGB(255, 74, 76, 79);
		canvas.drawRoundRect(rectFrame, rcicle, rcicle, paintMain);
		
		paintMain.setStyle(Style.STROKE);
		paintMain.setARGB(255, 201, 203, 205);
		paintMain.setStrokeWidth(strokeFrame);
		canvas.drawRoundRect(rectFrame, rcicle, rcicle, paintMain);	
		
/*
		float w = (float) (textX - 0.5*textPaint.measureText(textFrame));
		
		textPaint.setStyle(Style.FILL);
		textPaint.setColor(Color.RED);
		canvas.drawText(textFrame, w, textY, textPaint);
		
		textPaint.setStyle(Style.STROKE);
		textPaint.setColor(Color.WHITE);		
		textPaint.setStrokeWidth(strokeText);
		canvas.drawText(textFrame, w, textY, textPaint);
*/
		
		drawable.setBounds(rectImage);
		drawable.draw(canvas);
		
		
	}
	
////////////////////////////////////
	
	private Drawable drawable;
	public void setNameFrames(Drawable drawable){
		// textFrame = name;
		int height = bottomImage - topImage;
		int width = (int) (0.5*drawable.getMinimumWidth()*height/drawable.getMinimumHeight());
		rectImage.set(textX - width, topImage, textX + width, bottomImage);
		this.drawable = drawable;
		invalidate();
	}

}
