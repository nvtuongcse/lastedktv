package vn.com.sonca.Touch.CustomView;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TouchDeleteSearchView extends View {
	
	private int widthLayout = 0;
	private int heightLayout = 0;
	
	public TouchDeleteSearchView(Context context) {
		super(context);
		initView(context);
	}

	public TouchDeleteSearchView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchDeleteSearchView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private OnDeleteSearchListener listener;
	public interface OnDeleteSearchListener {
		public void OnDelete();
	}
	
	public void setOnDeleteSearchListener(OnDeleteSearchListener listener){
		this.listener = listener;
	}
	
	private Drawable drawable;
	private Drawable zlightdrawable;
	private void initView(Context context) {
		drawable = getResources().getDrawable(R.drawable.del_icon_72x72);
		zlightdrawable = getResources().getDrawable(R.drawable.zlight_del_icon_72x72);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = (int) (1.5*MeasureSpec.getSize(heightMeasureSpec));
		setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
	}
	
	private Rect rect = new Rect();
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		widthLayout = w;
		heightLayout = h;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE) {
			int tamX = (int) (0.652*widthLayout);
			if(TouchMainActivity.SAVETYPE == TouchMainActivity.YOUTUBE){
				tamX = (int) (0.685*widthLayout);
				if(MyApplication.flagYoutubeKaraokeOnly){
					tamX = (int) (0.95*widthLayout);
				}
			}
			if(MyApplication.flagSearchOnline){
				tamX = (int) (0.63*widthLayout);
			}
			int tamY = (int) (0.47*heightLayout);
			int hr = (int) (0.7*heightLayout);
			rect = new Rect(tamX - hr/2, tamY - hr/2, tamX + hr/2, tamY + hr/2);
			
			drawable.setBounds(rect);
			drawable.draw(canvas);
		} else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			zlightdrawable.setBounds(rect);
			zlightdrawable.draw(canvas);
		}
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			float x = event.getX();
			float y = event.getY();
			if(x >= rect.left && x <= rect.right){
				if(listener != null){
					listener.OnDelete();
				}
				return true;
			}
		}
		return false;
	}

}
