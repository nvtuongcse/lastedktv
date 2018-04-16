package vn.com.sonca.Touch.CustomView;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;

public class TouchPopupViewDownloadPercent extends View {

	private int widthLayout = 400;
	private int heightLayout = 400;
	private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Drawable drawable;

	private Context context;
	private String downloadPercent = "0 %";
	private String totalSize = "0 MB";
	private float percentWidth = 0;

	public void setDownloadPercent(int downloadPercent) {
		this.downloadPercent = downloadPercent + " %";
		float full = stopX - startX;
		this.percentWidth = downloadPercent * full / 100;
		invalidate();
	}

	public void setTotalSize(float contentLength) {
		this.totalSize = String.format("%.02f", (float)contentLength / 1024 / 1024) + " MB";
		invalidate();
	}

	private boolean isActiveCancel = false;

	public TouchPopupViewDownloadPercent(Context context) {
		super(context);
		initView(context);
	}

	public TouchPopupViewDownloadPercent(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchPopupViewDownloadPercent(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Drawable drawKhung, drawButtonActive, drawButtonInactive;
	private Drawable zlightdrawKhung, zlightdrawButtonActive, zlightdrawButtonInactive;

	private void initView(Context context) {
		drawKhung = getResources().getDrawable(R.drawable.boder_note_1116x399);
		drawButtonActive = getResources().getDrawable(
				R.drawable.boder_cokhong_active_129x74);
		drawButtonInactive = getResources().getDrawable(
				R.drawable.boder_cokhong_inactive_129x74);
		
		zlightdrawKhung = getResources().getDrawable(R.drawable.zlight_boder_popup);
		zlightdrawButtonActive = getResources().getDrawable(
				R.drawable.zlight_boder_cokhong_hover_129x74);
		zlightdrawButtonInactive = getResources().getDrawable(
				R.drawable.zlight_boder_cokhong_inactive_129x74);
	}

	private OnPopupDownloadPercentListener listener;

	public interface OnPopupDownloadPercentListener {
		public void OnCancelDownload();
	}

	public void setOnPopupDownloadPercentListener(
			OnPopupDownloadPercentListener listener) {
		this.listener = listener;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		setK(w, h);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int myWidth = (int) (4 * getResources().getDisplayMetrics().widthPixels / 8);
		int myHeight = (int) (1.6 * getResources().getDisplayMetrics().heightPixels / 4);
		setMeasuredDimension(
				MeasureSpec.makeMeasureSpec(myWidth, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(myHeight, MeasureSpec.UNSPECIFIED));
	}

	private int color_01;
	private int color_02;
	private int color_03;
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		setK(getWidth(), getHeight());

		// MyApplication.intColorScreen = MyApplication.SCREEN_LIGHT;
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			color_01 = Color.argb(255, 180, 254, 255);
			color_02 = Color.argb(255, 0, 253, 255);
			color_03 = Color.GRAY;
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			color_01 = Color.parseColor("#005249");
			color_02 = Color.parseColor("#005249");
			color_03 = Color.GRAY;
		}
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			// ----------KHUNG-----------//
			drawable = drawKhung;
			drawable.setBounds(0, 0, widthLayout, heightLayout);
			drawable.draw(canvas);
			// ----------TEXT INFO-----------//
			String text = "";
			mainPaint.setStyle(Style.FILL);
			mainPaint.setTextSize(KTS1);
			mainPaint.setColor(color_01);

			text = getResources().getString(R.string.progress_2);
			float textWidth = mainPaint.measureText(text);
			canvas.drawText(text, KT1X, KT1Y, mainPaint);
			// ----------CANCEL BORDER-----------//
			if (isActiveCancel) {
				drawable = drawButtonActive;
			} else {
				drawable = drawButtonInactive;
			}
			drawable.setBounds(KD1L, KD1T, KD1R, KD1B);
			drawable.draw(canvas);
			// ----------CANCEL TEXT-----------//
			mainPaint.setStyle(Style.FILL);
			mainPaint.setTextSize(KTS2);
			text = getResources().getString(R.string.progress_3);
			textWidth = mainPaint.measureText(text);
			canvas.drawText(text, (KD1R + KD1L) / 2 - textWidth / 2, KT2Y,
					mainPaint);
			// ----------PERCENT TEXT-----------//
			mainPaint.setStyle(Style.FILL);
			mainPaint.setTextSize(KT3);
			textWidth = mainPaint.measureText(downloadPercent);
			canvas.drawText(downloadPercent, KX330 - textWidth, KT3Y, mainPaint);
			// ----------TOTAL TEXT-----------//
			mainPaint.setStyle(Style.FILL);
			mainPaint.setTextSize(KT3);
			textWidth = mainPaint.measureText(totalSize);
			canvas.drawText(totalSize, KX330 - textWidth, KT4Y, mainPaint);
			// ----------EMPTY LINE-----------//
			linePaint.setAntiAlias(true);
			linePaint.setStrokeWidth(2);
			linePaint.setColor(color_03);
			linePaint.setStyle(Paint.Style.STROKE);
			linePaint.setStrokeJoin(Paint.Join.ROUND);
			canvas.drawLine(startX, startY, stopX, stopY, linePaint);
			// ----------FULL LINE-----------//
			linePaint.setAntiAlias(true);
			linePaint.setStrokeWidth(2.5f);
			linePaint.setColor(color_02);
			linePaint.setStyle(Paint.Style.STROKE);
			linePaint.setStrokeJoin(Paint.Join.ROUND);

			stop2X = startX + percentWidth;
			canvas.drawLine(startX, startY, stop2X, stopY, linePaint);
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			// ----------KHUNG-----------//
			drawable = zlightdrawKhung;
			drawable.setBounds(0, 0, widthLayout, heightLayout);
			drawable.draw(canvas);
			// ----------TEXT INFO-----------//
			String text = "";
			mainPaint.setStyle(Style.FILL);
			mainPaint.setTextSize(KTS1);
			mainPaint.setColor(color_01);

			text = getResources().getString(R.string.progress_2);
			float textWidth = mainPaint.measureText(text);
			canvas.drawText(text, KT1X, KT1Y, mainPaint);
			// ----------CANCEL BORDER-----------//
			if (isActiveCancel) {
				drawable = zlightdrawButtonActive;
			} else {
				drawable = zlightdrawButtonInactive;
			}
			drawable.setBounds(KD1L, KD1T, KD1R, KD1B);
			drawable.draw(canvas);
			// ----------CANCEL TEXT-----------//
			mainPaint.setStyle(Style.FILL);
			mainPaint.setTextSize(KTS2);
			text = getResources().getString(R.string.progress_3);
			textWidth = mainPaint.measureText(text);
			canvas.drawText(text, (KD1R + KD1L) / 2 - textWidth / 2, KT2Y,
					mainPaint);
			// ----------PERCENT TEXT-----------//
			mainPaint.setStyle(Style.FILL);
			mainPaint.setTextSize(KT3);
			textWidth = mainPaint.measureText(downloadPercent);
			canvas.drawText(downloadPercent, KX330 - textWidth, KT3Y, mainPaint);
			// ----------TOTAL TEXT-----------//
			mainPaint.setStyle(Style.FILL);
			mainPaint.setTextSize(KT3);
			textWidth = mainPaint.measureText(totalSize);
			canvas.drawText(totalSize, KX330 - textWidth, KT4Y, mainPaint);
			// ----------EMPTY LINE-----------//
			linePaint.setAntiAlias(true);
			linePaint.setStrokeWidth(2);
			linePaint.setColor(color_03);
			linePaint.setStyle(Paint.Style.STROKE);
			linePaint.setStrokeJoin(Paint.Join.ROUND);
			canvas.drawLine(startX, startY, stopX, stopY, linePaint);
			// ----------FULL LINE-----------//
			linePaint.setAntiAlias(true);
			linePaint.setStrokeWidth(2.5f);
			linePaint.setColor(color_02);
			linePaint.setStyle(Paint.Style.STROKE);
			linePaint.setStrokeJoin(Paint.Join.ROUND);

			stop2X = startX + percentWidth;
			canvas.drawLine(startX, startY, stop2X, stopY, linePaint);
		}
		
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			float x = event.getX();
			float y = event.getY();
			if (x >= KD1L && x <= KD1R && y >= KD1T && y <= KD1B) {
				isActiveCancel = true;
			}

			invalidate();
		}
			break;
		case MotionEvent.ACTION_UP: {
			float x = event.getX();
			float y = event.getY();
			isActiveCancel = false;

			if (x >= KD1L && x <= KD1R && y >= KD1T && y <= KD1B) {
				if (listener != null) {
					listener.OnCancelDownload();
				}
			}

			invalidate();
		}
			break;
		default:
			break;
		}

		return true;
	}

	private float KTS1, KT1Y, KT1X, KTS2, KT2Y, KT3, KT3X, KT3Y, KT4Y;
	private int KD1L, KD1R, KD1T, KD1B;
 	private float startX, startY, stopX, stopY, stop2X;
	private float KX330;

	private void setK(int w, int h) {
		widthLayout = w;
		heightLayout = h;

		KTS1 = 17 * h / 200;
		KT1X = 45 * w / 400;
		KT1Y = 60 * h / 200;

		KD1L = 150 * w / 400;
		KD1R = 230 * w / 400;
		KD1T = 115 * h / 200;
		KD1B = 160 * h / 200;

		KTS2 = 13 * h / 200;
		KT2Y = 143 * h / 200;

		KT3 = 13 * h / 200;
		KT3X = 315 * w / 400;
		KT3Y = 110 * h / 200;

		KX330 = 340 * w / 400;
		KT4Y = 80 * h / 200;

		startX = 45 * w / 400;
		startY = stopY = 90 * h / 200;
		stopX = 350 * w / 400;

		stop2X = startX * w / 400;
	}
}
