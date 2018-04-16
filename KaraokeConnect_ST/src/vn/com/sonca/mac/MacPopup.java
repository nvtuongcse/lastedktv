package vn.com.sonca.mac;

import java.util.ArrayList;
import java.util.Collections;

import vn.com.hanhphuc.karremote.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MacPopup extends View {

	private int widthLayout = 400;
	private int heightLayout = 400;
	private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Drawable drawable;

	public static final int LAYOUT_NOWIFI = 0;
	public static final int LAYOUT_FAILSERVER = 1;
	public static final int LAYOUT_WRONG = 2;
	public static final int LAYOUT_FILEWRONG = 3;
	public static final int LAYOUT_NOMAC = 4;
	private int layout = 2;

	public void setLayout(int layout) {
		this.layout = layout;
		requestLayout();
		invalidate();
	}

	private Context context;

	public MacPopup(Context context) {
		super(context);
		initView(context);
	}

	public MacPopup(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public MacPopup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private void initView(Context context) {
		this.context = context;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		setK(w, h);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int myWidth = 0;
		int myHeight = 0;
		myWidth = (int) (4 * getResources().getDisplayMetrics().widthPixels / 8);
		myHeight = (int) (1.6 * getResources().getDisplayMetrics().heightPixels / 4);
		setMeasuredDimension(
				MeasureSpec.makeMeasureSpec(myWidth, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(myHeight, MeasureSpec.UNSPECIFIED));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		setK(getWidth(), getHeight());

		if (layout == LAYOUT_NOWIFI) {
			// ----------KHUNG-----------//
			drawable = getResources().getDrawable(
					R.drawable.boder_note_1116x399);
			drawable.setBounds(0, 0, widthLayout, heightLayout);
			drawable.draw(canvas);
			// ----------TEXT INFO-----------//
			String text1, text2, text3;
			float tw1, tw2, tw3, maxWidth = 0;
			mainPaint.setStyle(Style.FILL);
			mainPaint.setTextSize(KTS1);
			mainPaint.setARGB(255, 180, 254, 255);

			text1 = getResources().getString(R.string.mac_popup_wifi1);
			tw1 = mainPaint.measureText(text1);
			text2 = getResources().getString(R.string.mac_popup_wifi2);
			tw2 = mainPaint.measureText(text2);
			text3 = getResources().getString(R.string.mac_popup_wifi3);
			tw3 = mainPaint.measureText(text3);

			ArrayList<Float> list = new ArrayList<Float>();
			list.add(tw1);
			list.add(tw2);
			list.add(tw3);
			maxWidth = Collections.max(list);

			canvas.drawText(text1, widthLayout / 2 - maxWidth / 2, KT1Y,
					mainPaint);
			canvas.drawText(text2, widthLayout / 2 - maxWidth / 2, KT2Y,
					mainPaint);
			canvas.drawText(text3, widthLayout / 2 - maxWidth / 2, KT3Y,
					mainPaint);
		}
		if (layout == LAYOUT_FAILSERVER) {
			// ----------KHUNG-----------//
			drawable = getResources().getDrawable(
					R.drawable.boder_note_1116x399);
			drawable.setBounds(0, 0, widthLayout, heightLayout);
			drawable.draw(canvas);
			// ----------TEXT INFO-----------//
			String text1, text2, text3;
			float tw1, tw2, tw3, maxWidth = 0;
			mainPaint.setStyle(Style.FILL);
			mainPaint.setTextSize(KTS1);
			mainPaint.setARGB(255, 180, 254, 255);

			text1 = getResources().getString(R.string.mac_popup_failserver1);
			tw1 = mainPaint.measureText(text1);
			text2 = getResources().getString(R.string.mac_popup_failserver2);
			tw2 = mainPaint.measureText(text2);
			text3 = getResources().getString(R.string.mac_popup_failserver3);
			tw3 = mainPaint.measureText(text3);

			ArrayList<Float> list = new ArrayList<Float>();
			list.add(tw1);
			list.add(tw2);
			list.add(tw3);
			maxWidth = Collections.max(list);

			canvas.drawText(text1, widthLayout / 2 - maxWidth / 2, KT1Y,
					mainPaint);
			canvas.drawText(text2, widthLayout / 2 - maxWidth / 2, KT2Y,
					mainPaint);
			canvas.drawText(text3, widthLayout / 2 - maxWidth / 2, KT3Y,
					mainPaint);
		}
		if (layout == LAYOUT_WRONG) {
			// ----------KHUNG-----------//
			drawable = getResources().getDrawable(
					R.drawable.boder_note_1116x399);
			drawable.setBounds(0, 0, widthLayout, heightLayout);
			drawable.draw(canvas);
			// ----------TEXT INFO-----------//
			String text1, text2, text3;
			float tw1, tw2, tw3, maxWidth = 0;
			mainPaint.setStyle(Style.FILL);
			mainPaint.setTextSize(KTS1);
			mainPaint.setARGB(255, 180, 254, 255);

			text1 = getResources().getString(R.string.mac_popup_wrong1);
			tw1 = mainPaint.measureText(text1);
			text2 = getResources().getString(R.string.mac_popup_wrong2);
			tw2 = mainPaint.measureText(text2);
			text3 = getResources().getString(R.string.mac_popup_wrong3);
			tw3 = mainPaint.measureText(text3);

			ArrayList<Float> list = new ArrayList<Float>();
			list.add(tw1);
			list.add(tw2);
			list.add(tw3);
			maxWidth = Collections.max(list);

			canvas.drawText(text1, widthLayout / 2 - maxWidth / 2, KT1Y,
					mainPaint);
			canvas.drawText(text2, widthLayout / 2 - maxWidth / 2, KT2Y,
					mainPaint);
			canvas.drawText(text3, widthLayout / 2 - maxWidth / 2, KT3Y,
					mainPaint);
		}

		if (layout == LAYOUT_FILEWRONG) {
			// ----------KHUNG-----------//
			drawable = getResources().getDrawable(
					R.drawable.boder_note_1116x399);
			drawable.setBounds(0, 0, widthLayout, heightLayout);
			drawable.draw(canvas);
			// ----------TEXT INFO-----------//
			String text1, text2, text3;
			float tw1, tw2, tw3, maxWidth = 0;
			mainPaint.setStyle(Style.FILL);
			mainPaint.setTextSize(KTS1);
			mainPaint.setARGB(255, 180, 254, 255);

			text1 = getResources().getString(R.string.mac_popup_wrongfile1);
			tw1 = mainPaint.measureText(text1);
			text2 = getResources().getString(R.string.mac_popup_wrongfile2);
			tw2 = mainPaint.measureText(text2);
			text3 = getResources().getString(R.string.mac_popup_wrongfile3);
			tw3 = mainPaint.measureText(text3);

			ArrayList<Float> list = new ArrayList<Float>();
			list.add(tw1);
			list.add(tw2);
			list.add(tw3);
			maxWidth = Collections.max(list);

			canvas.drawText(text1, widthLayout / 2 - maxWidth / 2, KT1Y,
					mainPaint);
			canvas.drawText(text2, widthLayout / 2 - maxWidth / 2, KT2Y,
					mainPaint);
			canvas.drawText(text3, widthLayout / 2 - maxWidth / 2, KT3Y,
					mainPaint);
		}
		if (layout == LAYOUT_NOMAC) {
			// ----------KHUNG-----------//
			drawable = getResources().getDrawable(
					R.drawable.boder_note_1116x399);
			drawable.setBounds(0, 0, widthLayout, heightLayout);
			drawable.draw(canvas);
			// ----------TEXT INFO-----------//
			String text1, text2, text3;
			float tw1, tw2, tw3, maxWidth = 0;
			mainPaint.setStyle(Style.FILL);
			mainPaint.setTextSize(KTS1);
			mainPaint.setARGB(255, 180, 254, 255);

			text1 = getResources().getString(R.string.mac_popup_mac1);
			tw1 = mainPaint.measureText(text1);
			text2 = getResources().getString(R.string.mac_popup_mac2);
			tw2 = mainPaint.measureText(text2);
			text3 = getResources().getString(R.string.mac_popup_mac3);
			tw3 = mainPaint.measureText(text3);

			ArrayList<Float> list = new ArrayList<Float>();
			list.add(tw1);
			list.add(tw2);
			list.add(tw3);
			maxWidth = Collections.max(list);

			canvas.drawText(text1, widthLayout / 2 - maxWidth / 2, KT1Y,
					mainPaint);
			canvas.drawText(text2, widthLayout / 2 - maxWidth / 2, KT2Y,
					mainPaint);
			canvas.drawText(text3, widthLayout / 2 - maxWidth / 2, KT3Y,
					mainPaint);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		return true;
	}

	private float KTS1, KT1Y, KT2Y, KT3Y;
	private int KD1L, KD1R, KD1T, KD1B;
	private int KD2L, KD2R, KD2T, KD2B;
	private int KD3L, KD3R, KD3T, KD3B;
	private int KD4L, KD4R, KD4T, KD4B;
	private int KD5L, KD5R, KD5T, KD5B;
	private int KD6L, KD6R, KD6T, KD6B;
	private float RTS1, RT1Y, RT2Y, RT3Y;
	private float RTS2, RTS2Y, RT4Y, RT5Y;
	private int KD7L, KD7R, KD7T, KD7B;
	private int KD8L, KD8R, KD8T, KD8B;

	private void setK(int w, int h) {
		widthLayout = w;
		heightLayout = h;

		KTS1 = 130 * h / 960;
		KT1Y = 330 * h / 960;
		KT2Y = 530 * h / 960;
		KT3Y = 730 * h / 960;

		KD1L = 300 * w / 1920;
		KD1R = 1620 * w / 1920;
		KD1T = 200 * h / 1080;
		KD1B = 350 * h / 1080;

		KD2L = 300 * w / 1920;
		KD2R = 1620 * w / 1920;
		KD2T = 370 * h / 1080;
		KD2B = 520 * h / 1080;

		KD3L = 300 * w / 1920;
		KD3R = 1620 * w / 1920;
		KD3T = 540 * h / 1080;
		KD3B = 690 * h / 1080;

		KD4L = 800 * w / 1920;
		KD4R = 1590 * w / 1920;
		KD4T = 220 * h / 1080;
		KD4B = 330 * h / 1080;

		KD5L = 800 * w / 1920;
		KD5R = 1590 * w / 1920;
		KD5T = 390 * h / 1080;
		KD5B = 500 * h / 1080;

		KD6L = 800 * w / 1920;
		KD6R = 1590 * w / 1920;
		KD6T = 560 * h / 1080;
		KD6B = 670 * h / 1080;

		RTS1 = 45 * h / 960;
		RT1Y = 265 * h / 960;
		RT2Y = 415 * h / 960;
		RT3Y = 565 * h / 960;

		KD7L = 550 * h / 1080;
		KD7R = 850 * h / 1080;
		KD7T = 750 * h / 1080;
		KD7B = 950 * h / 1080;

		KD8L = 950 * h / 1080;
		KD8R = 1250 * h / 1080;
		KD8T = 750 * h / 1080;
		KD8B = 950 * h / 1080;

		RTS2 = 40 * h / 960;
		RTS2Y = 765 * h / 960;
	}
}
