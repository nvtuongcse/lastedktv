package vn.com.sonca.Touch.CustomView;

import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TouchStatusTotalView extends View {

	private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Path mainPath;
	private int widthLayout = 350;
	private int heightLayout = 350;

	private String strTitle = "";

	public String getStrTitle() {
		return strTitle;
	}

	public void setStrTitle(String strTitle) {
		this.strTitle = strTitle;
		invalidate();
	}

	public TouchStatusTotalView(Context context) {
		super(context);
		initView(context);
	}

	public TouchStatusTotalView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchStatusTotalView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private Context context;

	private void initView(Context context) {
		this.context = context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int myHeight = (int) (0.35 * getResources().getDisplayMetrics().heightPixels / 5);
		if (MyApplication.flagHong) {
			myHeight = (int) (0.3 * getResources().getDisplayMetrics().heightPixels / 5);
		}
		setMeasuredDimension(widthMeasureSpec,
				MeasureSpec.makeMeasureSpec(myHeight, MeasureSpec.UNSPECIFIED));
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		setK(w, h);
	}

	private int color_01;
	private int color_02;

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			color_01 = Color.argb(255, 1, 150, 234);
			color_02 = Color.argb(255, 182, 253, 255);
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			color_01 = Color.parseColor("#C1FFE8");
			color_02 = Color.parseColor("#21BAA9");
		}

		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			mainPaint.setStyle(Style.FILL);
			mainPaint.setColor(color_01);
			canvas.drawRect(0, 0, widthLayout, heightLayout, mainPaint);

			// ----------------------------------
			mainPaint.setStyle(Style.FILL);
			mainPaint.setTextSize(RT1S);
			mainPaint.setColor(color_02);

			String str = strTitle;
			float textWidth = mainPaint.measureText(str);
			canvas.drawText(str, widthLayout / 2 - textWidth / 2, RT1Y,
					mainPaint);

		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			mainPaint.setStyle(Style.FILL);
			mainPaint.setColor(color_01);
			canvas.drawRect(0, 0, widthLayout, heightLayout, mainPaint);

			// ----------------------------------
			mainPaint.setStyle(Style.FILL);
			mainPaint.setTextSize(RT1S);
			mainPaint.setColor(color_02);

			String str = strTitle;
			float textWidth = mainPaint.measureText(str);
			canvas.drawText(str, widthLayout / 2 - textWidth / 2, RT1Y,
					mainPaint);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return true;
	}

	private float RT1S, RT1X, RT1Y;
	private float RT2S, RT2Y;
	private float RT1L, RT1R, RT1T, RT1B;
	private float RX1, RY1;
	private float RT2L, RT2R, RT2T, RT2B;

	private void setK(int w, int h) {
		widthLayout = w;
		heightLayout = h;

		RX1 = 1 * w / 480;
		RY1 = 1 * h / 100;

		RT1S = 55 * h / 100;
		RT1X = 12 * w / 480;
		RT1Y = 65 * h / 100;

		RT2S = 35 * h / 100;
		RT2Y = 60 * h / 100;

		RT1L = 350 * w / 480;
		RT1R = 450 * w / 480;
		RT1T = 25 * h / 100;
		RT1B = 75 * h / 100;

		RT2L = 20 * w / 480;
		RT2R = 80 * w / 480;
		RT2T = 10 * h / 100;
		RT2B = 90 * h / 100;
	}

}
