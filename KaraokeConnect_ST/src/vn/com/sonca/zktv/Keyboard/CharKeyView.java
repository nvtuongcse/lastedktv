package vn.com.sonca.zktv.Keyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import vn.com.hanhphuc.karremote.R;

public class CharKeyView extends View {
	
	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	
	public CharKeyView(Context context) {
		super(context);
		initView(context);
	}

	public CharKeyView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public CharKeyView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Animation anime;
	private String text = "";
	private void initView(Context context) {
		anime = AnimationUtils.loadAnimation(context, R.anim.animation_song);
		textPaint.setTypeface(Typeface.DEFAULT_BOLD);
		text = "KeyBoard";
	}

	private int cx, cy, radius;
	private int textS, textX, testY;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
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
		
		paint.setStyle(Style.FILL);
		paint.setARGB(255, 27, 64, 88);
		canvas.drawCircle(cx, cy, radius, paint);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(3);
		paint.setARGB(255, 103, 127, 143);
		canvas.drawCircle(cx, cy, radius, paint);
		textPaint.setStyle(Style.FILL);
		textPaint.setTextSize(textS);
		textPaint.setColor(Color.WHITE);
		textPaint.setAlpha(255);
		float x = (float) (textX - 0.5*textPaint.measureText(text));
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
	
	public void setData(String text){
		this.text = text;
		invalidate();
	}
	
	public String getData(){
		return text;
	}
	
}
