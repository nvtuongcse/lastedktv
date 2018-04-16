package vn.com.sonca.zktv.FragData;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import vn.com.hanhphuc.karremote.R;

public class ButPage extends View {
	
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);

	public ButPage(Context context) {
		super(context);
		initView(context);
	}

	public ButPage(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public ButPage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Animation anime;
	private void initView(Context context) {
		anime = AnimationUtils.loadAnimation(context, R.anim.animation_song);
		paintMain.setStyle(Style.STROKE);
		paintMain.setColor(Color.RED);
		paintMain.setStrokeWidth(2);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	private Rect rectImage = new Rect();
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		int tamX = (int) (0.5*w);
		int tamY = (int) (0.5*h);
		int hh = (int) (0.45*h);
		int ww = hh;
		rectImage.set(tamX-ww, tamY-hh, tamX+ww, tamY+hh);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
				
		// canvas.drawRect(rectImage, paintMain);
			if(drawable != null){
				drawable.setBounds(rectImage);
				drawable.draw(canvas);
			}
	
		
	}
	
	private OnClickListener listener;
	@Override
	public void setOnClickListener(OnClickListener l) {
		super.setOnClickListener(l);
		listener = l;
	}
	
	private boolean isTouchView;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isTouchView = true;
			break;
		case MotionEvent.ACTION_UP:
			isTouchView = false;
			if(listener != null && isActive){
				float x = event.getX();
				float y = event.getY();
				if(x >= rectImage.left && 
					x <= rectImage.right && 
					y >= rectImage.top && 
					y <= rectImage.bottom){
					this.startAnimation(anime);
					listener.onClick(this);
				}
			}
			break;
		default:
			break;
		}
		return true;
	}
	
	private boolean isActive;
	private Drawable drawable;
	public void setImageView(Drawable drawable, boolean isActive){
		this.drawable = drawable;
		this.isActive = isActive;
		invalidate();
	}
	
}
