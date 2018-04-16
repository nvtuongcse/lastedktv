package app.sonca.flower;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.text.TextPaint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import vn.com.hanhphuc.karremote.R;

public class ViewFlowerBut extends View {
	
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	
	public ViewFlowerBut(Context context) {
		super(context);
		initView(context);
	}

	public ViewFlowerBut(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public ViewFlowerBut(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private String name;
	private Drawable drawButAC;
	private Drawable drawButIN;
	private void initView(Context context) {
		name = context.getResources().getString(R.string.tang);
		drawButAC = context.getResources().getDrawable(R.drawable.hover_tang);
		drawButIN = context.getResources().getDrawable(R.drawable.boder_tanghoa);
		isTouchView = true;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int width = (int) (2.5*height);
		setMeasuredDimension(width, height);
	}
	
	private float Line;
	private int widthLayout;
	private int heightLayout;
	private Rect rectKhung = new Rect();
	private Path pathGoc = new Path();
	
	private int textS, textX, textY;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		widthLayout = w;
		heightLayout = h;
		
		int padding = 1;
		Line = 150*h/1080;
		rectKhung.set(padding, padding, w - padding, h - padding);
		rectKhung.set(0, 0, w, h);
		
		pathGoc.reset();
		pathGoc.moveTo(padding , padding + Line);
		pathGoc.lineTo(padding, padding);
		pathGoc.lineTo(padding + Line , padding);
		
		pathGoc.moveTo(widthLayout - padding , padding + Line);
		pathGoc.lineTo(widthLayout - padding , padding);
		pathGoc.lineTo(widthLayout - Line - padding , padding);
		
		pathGoc.moveTo(widthLayout - padding , heightLayout - Line - padding);
		pathGoc.lineTo(widthLayout - padding, heightLayout - padding);
		pathGoc.lineTo(widthLayout - Line - padding , heightLayout - padding);
		
		pathGoc.moveTo(padding , heightLayout - Line - padding);
		pathGoc.lineTo(padding , heightLayout - padding);
		pathGoc.lineTo(padding + Line , heightLayout - padding);
		
		
		textS = (int) (0.5*h);
		textX = (int) (0.5*w);
		textY = (int) (0.5*h);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (isTouchView == true) {
			drawButIN.setBounds(rectKhung);
			drawButIN.draw(canvas);
		}else{
			drawButAC.setBounds(rectKhung);
			drawButAC.draw(canvas);
		}
		textPaint.setStyle(Style.FILL);
		textPaint.setARGB(255, 0, 253, 253);
		textPaint.setTextSize(textS);
		float f = textPaint.measureText(name);
		canvas.drawText(name, (int)(textX - 0.5*f), 
				(int)(0.9*textY + 0.5*textS), textPaint);
	}
	
	public void setNameBut(String name){
		this.name = name;
		invalidate();
	}
	
	private OnClickListener listener;
	@Override
	public void setOnClickListener(OnClickListener l) {
		// super.setOnClickListener(l);
		listener = l;
	}
	
	private boolean isTouchView = false;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isTouchView = false;
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			isTouchView = true;
			invalidate();
			if(listener != null){
				listener.onClick(this);
			}
			break;
		default:
			break;
		}
		return true;
	}

}
