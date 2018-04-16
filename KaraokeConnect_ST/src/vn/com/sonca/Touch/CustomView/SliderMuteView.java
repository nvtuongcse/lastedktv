package vn.com.sonca.Touch.CustomView;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;

public class SliderMuteView extends View {
	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Path mPath;
	private Drawable drawable = null;

	private float widthLayout;
	private float heightLayout;

	private float centerX, centerY;
	private float radius;

	private float STROKE_WIDTH_1;
	private float PADDING;

	private static final String STROKE_COLOR_1 = "#91d2d6";

	private boolean isMute;

	public void setMute(boolean isMute) {
		this.isMute = isMute;
		invalidate();
		requestLayout();
	}

	public boolean getMute() {
		return isMute;
	}

	public SliderMuteView(Context context) {
		super(context);
		initView(context);
	}

	public SliderMuteView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public SliderMuteView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private void initView(Context context) {
		isMute = true;
	}

	private OnMuteListener listener;

	public interface OnMuteListener {
		public void OnMuteSlider(boolean isMute);
	}

	public void setOnMuteListener(OnMuteListener listener) {
		this.listener = listener;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int myHeight = (int) (1 * getResources().getDisplayMetrics().heightPixels / 10);
		int myWidth = myHeight;
		setMeasuredDimension(
				MeasureSpec.makeMeasureSpec(myWidth, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(myHeight, MeasureSpec.UNSPECIFIED));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		setK(getWidth(), getHeight());

		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			if (isMute) {
				drawable = getResources().getDrawable(R.drawable.loa_off);
			} else {
				drawable = getResources().getDrawable(R.drawable.loa_on);
			}
			drawable.setBounds(KD1L, KD1T, KD1R, KD1B);
			drawable.draw(canvas);
		} else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			if (isMute) {
				drawable = getResources().getDrawable(R.drawable.zlight_loa_on);
			} else {
				drawable = getResources().getDrawable(R.drawable.zlight_loa_off);
			}
			drawable.setBounds(KD1L, KD1T, KD1R, KD1B);
			drawable.draw(canvas);
		}
	}

	private int KD1L, KD1R, KD1T, KD1B;

	private void setK(int w, int h) {
		widthLayout = w;
		heightLayout = h;

		KD1L = 1 * w / 72;
		KD1R = KD1L + 50 * h / 80;
		KD1T = 7 * h / 80;
		KD1B = KD1T + 50 * h / 80;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_UP: {
			if (listener != null) {
				listener.OnMuteSlider(isMute);
			}
		}
			invalidate();
			break;
		}
		return true;
	}
}
