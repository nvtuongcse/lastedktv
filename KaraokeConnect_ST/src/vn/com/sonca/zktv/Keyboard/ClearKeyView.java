package vn.com.sonca.zktv.Keyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import vn.com.hanhphuc.karremote.R;

public class ClearKeyView extends View {
	
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);

	public ClearKeyView(Context context) {
		super(context);
		initView(context);
	}

	public ClearKeyView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public ClearKeyView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Animation anime;
	private void initView(Context context) {
		anime = AnimationUtils.loadAnimation(context, R.anim.animation_song);
		textPaint.setTypeface(Typeface.DEFAULT_BOLD);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	private int cx, cy, radius;
	private int textS, textX, testY;
	private RectF rectImage = new RectF();
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		int tamX = (int) (0.5*w);
		int tamY = (int) (0.5*h);
		int hh = (int) (0.4*h);
		int ww = (int) (0.45*w);
		rectImage.set(tamX - ww, tamY - hh, tamX + ww, tamY + hh);
		
		textS = (int) (0.45*h);
		textX = (int) (0.5*w);
		testY = (int) (0.48*h + 0.4*textS);
		
		cx = w/2;
		cy = h/2;
		if(w > h){
			radius = (int) (0.4*h);
		}else{
			radius = (int) (0.4*w);
		}
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		paintMain.setStyle(Style.FILL);
		paintMain.setARGB(255, 27, 64, 88);
		canvas.drawRoundRect(rectImage, radius, radius, paintMain);
		
		paintMain.setStyle(Style.STROKE);
		paintMain.setStrokeWidth(3);
		paintMain.setARGB(255, 103, 127, 143);
		canvas.drawRoundRect(rectImage, radius, radius, paintMain);
		
		textPaint.setStyle(Style.FILL);
		textPaint.setTextSize(textS);
		textPaint.setColor(Color.WHITE);
		textPaint.setAlpha(255);
		float x = (float) (textX - 0.5 * textPaint.measureText(text));
		canvas.drawText(text, x, testY, textPaint);

	}
	

	private boolean isClick = false;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isClick = true;
			break;
		case MotionEvent.ACTION_UP:
			isClick = false;
			this.startAnimation(anime);
			if(clickListener != null){
				clickListener.onClick(this);
			}
			break;
		default: break;
		}
		return true;
	}
	
	private OnClickListener clickListener;
	@Override
	public void setOnClickListener(OnClickListener l) {
		super.setOnClickListener(l);
		clickListener = l;
	}
	
	private String text = "";
	public void setData(String text){
		this.text = text;
		invalidate();
	}
	

}
