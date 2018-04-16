package vn.com.sonca.Touch.CustomView;

import vn.com.sonca.Touch.CustomView.TouchDanceView.OnDancetListener;
import vn.com.sonca.zzzzz.MyApplication;
import vn.com.hanhphuc.karremote.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TouchExitView extends View{

	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Path path = new Path();
	
    private OnExitListener listener;
    public interface OnExitListener {
		public void OnClick();
	}
	
	public void setOnExitListener(OnExitListener listener){
		this.listener = listener;
	}
	
	public TouchExitView(Context context) {
		super(context);
		initView(context);
	}

	public TouchExitView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchExitView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private Drawable drawExitIN;
	private Drawable drawExitAC;
	private Drawable drawExitXAM;
	
	private Drawable zlightdrawExitIN;
	private Drawable zlightdrawExitAC;
	private Drawable zlightdrawExitXAM;
	
	private void initView(Context context) {
		drawExitIN = getResources().getDrawable(R.drawable.touch_image_giaodienktv_codien);
		drawExitAC = getResources().getDrawable(R.drawable.touch_image_giaodienktv_codien_hover);
		drawExitXAM = getResources().getDrawable(R.drawable.touch_image_giaodienktv_codien_xam);
	
		zlightdrawExitIN = getResources().getDrawable(R.drawable.zlight_touch_image_giaodienktv_codien);
		zlightdrawExitAC = getResources().getDrawable(R.drawable.zlight_touch_image_giaodienktv_codien_hover);
		zlightdrawExitXAM = getResources().getDrawable(R.drawable.zlight_touch_image_giaodienktv_codien_xam);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	private Rect rectImage = new Rect();
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		int tamX = w/2;
		int tamY = h/2;
		int hr = (int) (0.4*h);
		int wr = 241*hr/146;
		rectImage = new Rect(tamX - wr, tamY - hr, tamX + wr, tamY + hr);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			if(MyApplication.intWifiRemote == MyApplication.SONCA){
				if (boolActive) {
					drawExitAC.setBounds(rectImage);
					drawExitAC.draw(canvas);
				} else {
					drawExitIN.setBounds(rectImage);
					drawExitIN.draw(canvas);
				}
			} else{
				drawExitXAM.setBounds(rectImage);
				drawExitXAM.draw(canvas);
			}
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			if(MyApplication.intWifiRemote == MyApplication.SONCA){
				if (boolActive) {
					zlightdrawExitAC.setBounds(rectImage);
					zlightdrawExitAC.draw(canvas);
				} else {
					zlightdrawExitIN.setBounds(rectImage);
					zlightdrawExitIN.draw(canvas);
				}
			} else{
				zlightdrawExitXAM.setBounds(rectImage);
				zlightdrawExitXAM.draw(canvas);
			}
		}
		
	}
	
    private boolean boolActive;
	@Override
    public boolean onTouchEvent(MotionEvent event){
    	super.onTouchEvent(event);
    	if(MyApplication.intWifiRemote == MyApplication.SONCA){
	    	switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if(rectImage != null){
					float x = event.getX();
					float y = event.getY();
					if(x>=rectImage.left && x<=rectImage.right &&
						y>=rectImage.top && y<=rectImage.bottom){
						boolActive = true;
					}
				}
				invalidate();
				break;
			case MotionEvent.ACTION_UP:
				boolActive = false;
				if(rectImage != null && listener != null){
					float x = event.getX();
					float y = event.getY();
					if(x>=rectImage.left && x<=rectImage.right &&
						y>=rectImage.top && y<=rectImage.bottom){
						listener.OnClick();
					}
				}
				invalidate();
				break;
			default:
				break;
			}
	    	return true;
    	} else {
    		return true;
    	}
    }

    private void resetPaint() {
    	mPaint.reset();
    	mPaint.setAntiAlias(true);
    }

}
