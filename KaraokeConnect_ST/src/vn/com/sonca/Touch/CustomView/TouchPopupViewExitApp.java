package vn.com.sonca.Touch.CustomView;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TouchPopupViewExitApp extends View {

	private int widthLayout = 400;
	private int heightLayout = 400;
	private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Drawable drawable;

	private boolean isActiveYes = false;
	private boolean isActiveNo = false;

	public TouchPopupViewExitApp(Context context) {
		super(context);
		initView(context);
	}

	public TouchPopupViewExitApp(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchPopupViewExitApp(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private Drawable drawKhung, drawButtonActive, drawButtonInactive;
	private Drawable zgreen_drawKhung, zgreen_drawButtonActive,
			zgreen_drawButtonInactive;
	private Drawable zgreen_drawLine, drawLine;
	
	private void initView(Context context) {
		drawKhung = getResources().getDrawable(R.drawable.boder_note_1116x399);
		drawButtonActive = getResources().getDrawable(
				R.drawable.boder_cokhong_active_129x74);
		drawButtonInactive = getResources().getDrawable(
				R.drawable.boder_cokhong_inactive_129x74);
		
		zgreen_drawKhung = getResources().getDrawable(
				R.drawable.zgreen_boder_note_1116x399);
		zgreen_drawButtonActive = getResources().getDrawable(
				R.drawable.zgreen_boder_cokhong_active_129x74);
		zgreen_drawButtonInactive = getResources().getDrawable(
				R.drawable.zgreen_boder_cokhong_inactive_129x74);
				
		drawLine = getResources().getDrawable(R.drawable.line_header_480x3);
		zgreen_drawLine = getResources().getDrawable(
				R.drawable.zgreen_line_header_480x3);		
	}

	private OnPopupExitAppListener listener;

	public interface OnPopupExitAppListener {
		public void OnExitYes();

		public void OnExitNo();
	}

	public void setOnPopupExitAppListener(OnPopupExitAppListener listener) {
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

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		setK(getWidth(), getHeight());

		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			// ----------KHUNG-----------//
			drawable = drawKhung;
			drawable.setBounds(0, 0, widthLayout, heightLayout);
			drawable.draw(canvas);
			// ----------GRADIENT-----------//
			drawable = drawLine;
			drawable.setBounds(RG1L, RG1T, RG1R, RG1B);
			drawable.draw(canvas);
			// ----------TEXT INFO-----------//
			String text = "";
			mainPaint.setStyle(Style.FILL);
			mainPaint.setTextSize(KTS1);
			mainPaint.setARGB(255, 0, 253, 255);		
			text = getResources().getString(R.string.popup_exit_1);
			float textWidth = mainPaint.measureText(text);
			canvas.drawText(text, widthLayout / 2 - textWidth / 2, KT1Y, mainPaint);

			mainPaint.setTextSize(KTS2);
			text = getResources().getString(R.string.popup_exit_2);
			textWidth = mainPaint.measureText(text);
			canvas.drawText(text, widthLayout / 2 - textWidth / 2, KT2Y, mainPaint);
			// ----------YES BORDER-----------//
			if (isActiveYes) {
				drawable = drawButtonActive;
			} else {
				drawable = drawButtonInactive;
			}
			drawable.setBounds(KD1L, KD1T, KD1R, KD1B);
			drawable.draw(canvas);
			// ----------NO BORDER-----------//
			if (isActiveNo) {
				drawable = drawButtonActive;
			} else {
				drawable = drawButtonInactive;
			}
			drawable.setBounds(KD2L, KD2T, KD2R, KD2B);
			drawable.draw(canvas);
			// ----------YES NO TEXT-----------//
			mainPaint.setTextSize(KTS3);
			
			text = getResources().getString(R.string.popup_no);
			textWidth = mainPaint.measureText(text);
			canvas.drawText(text, (KD1R + KD1L) / 2 - textWidth / 2, KT3Y,
					mainPaint);

			text = getResources().getString(R.string.popup_yes);
			textWidth = mainPaint.measureText(text);
			canvas.drawText(text, (KD2R + KD2L) / 2 - textWidth / 2, KT3Y,
					mainPaint);
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_GREEN) {

		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			float x = event.getX();
			float y = event.getY();
			if (x >= KD1L && x <= KD1R && y >= KD1T && y <= KD1B) {
				isActiveYes = true;
			}

			if (x >= KD2L && x <= KD2R && y >= KD2T && y <= KD2B) {
				isActiveNo = true;
			}
			invalidate();
		}
			break;
		case MotionEvent.ACTION_UP: {
			float x = event.getX();
			float y = event.getY();
			isActiveYes = false;
			isActiveNo = false;

			if (x >= KD1L && x <= KD1R && y >= KD1T && y <= KD1B) {
				if (listener != null) {
					listener.OnExitNo();
				}
			}

			if (x >= KD2L && x <= KD2R && y >= KD2T && y <= KD2B) {
				if (listener != null) {
					listener.OnExitYes();
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

	private float KTS1, KT1Y, KTS2, KT2Y, KTS3, KT3Y;
	private int KD1L, KD1R, KD1T, KD1B;
	private int KD2L, KD2R, KD2T, KD2B;
	private int RG1L, RG1R, RG1T, RG1B;

	private void setK(int w, int h) {
		widthLayout = w;
		heightLayout = h;

		KTS1 = 25 * h / 200;
		KTS2 = 20 * h / 200;
		KTS3 = 17 * h / 200;

		KT1Y = 45 * h / 200;
		KT2Y = 93 * h / 200;
		KT3Y = 145 * h / 200;

		KD1L = 110 * w / 400;
		KD1R = 190 * w / 400;
		KD1T = 120 * h / 200;
		KD1B = 160 * h / 200;

		KD2L = 220 * w / 400;
		KD2R = 300 * w / 400;
		KD2T = 120 * h / 200;
		KD2B = 160 * h / 200;
		
		RG1L = 50 * w / 400;
		RG1R = 350 * w / 400;
		RG1T = 56 * h / 200;
		RG1B = 58 * h / 200;

	}
}
