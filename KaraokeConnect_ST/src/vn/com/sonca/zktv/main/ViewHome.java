package vn.com.sonca.zktv.main;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import vn.com.hanhphuc.karremote.R;

public class ViewHome extends View {
	
	public ViewHome(Context context) {
		super(context);
		initView(context);
	}

	public ViewHome(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public ViewHome(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Drawable drawable;
	private Drawable drawable2;
	private Animation anime;
	private void initView(Context context) {
		drawable = context.getResources().getDrawable(R.drawable.ktv_icon_home);
		drawable2 = context.getResources().getDrawable(R.drawable.ktv_icon_logo);
		anime = AnimationUtils.loadAnimation(context, R.anim.animation_song);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int width = (int) (3.1*height);
		setMeasuredDimension(width, height);
	}
	
	private Rect rectImage = new Rect();
	private Rect rectImage2 = new Rect();
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		int tamX = (int) (0.5*h);
		int tamY = (int) (0.5*h);
		int hh = (int) (0.5*h);
		int ww = hh;
		rectImage.set(tamX-ww, 0, tamX+ww, tamY+hh);
		
		tamX = (int) (2.0*h);
		tamY = (int) (0.5*h);
		hh = (int) (0.42*h);
		ww = 248*hh/96;
		rectImage2.set(tamX-ww, tamY-hh, tamX+ww, tamY+hh);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (drawable != null) {
			drawable.setBounds(rectImage);
			drawable.draw(canvas);
		}
		if (drawable2 != null) {
			drawable2.setBounds(rectImage2);
			drawable2.draw(canvas);
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
			if(listener != null){
				listener.onClick(this);
			}
			this.startAnimation(anime);
			break;
		default:
			break;
		}
		return true;
	}
	

}
