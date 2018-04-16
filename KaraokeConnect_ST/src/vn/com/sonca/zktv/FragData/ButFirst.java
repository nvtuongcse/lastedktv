package vn.com.sonca.zktv.FragData;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.Touch.CustomView.TouchMyGroupSong.OnMyGroupSongListener;
import vn.com.sonca.params.Song;
import vn.com.sonca.zzzzz.MyApplication;

public class ButFirst extends View {
	
	private int heightLayout;

	public ButFirst(Context context) {
		super(context);
		initView(context);
	}

	public ButFirst(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public ButFirst(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private OnButFirstListener listener;
	public interface OnButFirstListener {
		public void OnFristRes(View view, int x, int y);
	}
	
	public void setOnButFirstListener(OnButFirstListener listener){
		this.listener = listener;
	}
	
	private Drawable drawable, drawableXam;
	private Animation anime;
	private void initView(Context context) {
		drawable = context.getResources().getDrawable(R.drawable.ktv_playlist_1st);
		drawableXam = context.getResources().getDrawable(R.drawable.ktv_playlist_1st_xam);
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
		
		heightLayout = h;
		
		int tamX = (int) (0.5*w);
		int tamY = (int) (0.5*h);
		int hh = (int) (0.3*h);
		int ww = 194*hh/98;
		rectImage.set(tamX-ww, tamY-hh, tamX+ww, tamY+hh);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(isDrawView == false){
			return;
		}
		if (drawable != null) {
			if(MyApplication.bOffFirst){
				drawable.setBounds(rectImage);
				drawable.draw(canvas);	
			} else {
				drawableXam.setBounds(rectImage);
				drawableXam.draw(canvas);	
			}
			
		}
	}
	
//	private OnClickListener listener;
//	@Override
//	public void setOnClickListener(OnClickListener l) {
//		super.setOnClickListener(l);
//		listener = l;
//	}
	
	private boolean isTouchView;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(isDrawView == false){
			return true;
		}
		if(MyApplication.bOffFirst){
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				isTouchView = true;
				break;
			case MotionEvent.ACTION_UP:
				isTouchView = false;
				if(listener != null){
					int[] location = new int[2];
	    			this.getLocationOnScreen(location);
	    			int ScreenX = location[0] + heightLayout / 2;
	    			int ScreenY = location[1] + heightLayout / 2;
					listener.OnFristRes(this, ScreenX, ScreenY);
				}
				this.startAnimation(anime);
				break;
			default:
				break;
			}	
		}
		
		return true;
	}
	
	private boolean isDrawView = false;
	public void setDrawView(boolean isDrawView){
		this.isDrawView = isDrawView;
		invalidate();
	}
	

}
