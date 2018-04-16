package vn.com.sonca.zktv.FragData;

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

public class ButSinger extends View {
	
	public ButSinger(Context context) {
		super(context);
		initView(context);
	}

	public ButSinger(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public ButSinger(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Drawable drawable;
	private Animation anime;
	private void initView(Context context) {
		drawable = context.getResources().getDrawable(R.drawable.ktv_playlist_tranguoccasi);
		anime = AnimationUtils.loadAnimation(context, R.anim.animation_song);
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
		int hh = (int) (0.3*h);
		int ww = 328*hh/98;
		rectImage.set(tamX-ww, tamY-hh, tamX+ww, tamY+hh);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(isDrawView == false){
			return;
		}
		if (drawable != null) {
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
		if(isDrawView == false){
			return true;
		}
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
	
	private boolean isDrawView = false;
	public void setDrawView(boolean isDrawView){
		this.isDrawView = isDrawView;
		invalidate();
	}
	

}
