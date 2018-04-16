package vn.com.sonca.Touch.CustomView;

import vn.com.hanhphuc.karremote.R;
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

public class PopupViewUpdatePic extends View {

	private int widthLayout = 400;
	private int heightLayout = 400;
	private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Drawable drawable;

	private Context context;

	public static final int LAYOUT_UPTODATE = 0;
	public static final int LAYOUT_UPDATE = 1;
	public static final int LAYOUT_NOWIFI = 2;
	public static final int LAYOUT_UPDATEDAUMAY = 3;
	public static final int LAYOUT_NOWIFI2 = 4;
	public static final int LAYOUT_PROCESSDATA = 5;
	public static final int LAYOUT_FIRSTIME = 6;
	private int popupLayout = LAYOUT_UPTODATE;

	private String serverName = "";

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	private int totalDownSize = 0;

	public void setTotalDown(int totalDownSize) {
		this.totalDownSize = totalDownSize;
		invalidate();
	}

	private boolean isActiveYes = false;
	private boolean isActiveNo = false;

	public PopupViewUpdatePic(Context context) {
		super(context);
		initView(context);
	}

	public PopupViewUpdatePic(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public PopupViewUpdatePic(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public void setPopupLayout(int layout) {
		this.popupLayout = layout;
	}

	private void initView(Context context) {
	}

	private OnPopupUpdatePicListener listener;

	public interface OnPopupUpdatePicListener {
		public void OnUpdatePicYes();

		public void OnUpdatePicNo();
	}

	public void setOnPopupUpdatePicListener(OnPopupUpdatePicListener listener) {
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

		if (popupLayout == LAYOUT_UPDATE) {
			// ----------KHUNG-----------//
			drawable = getResources().getDrawable(
					R.drawable.boder_note_1116x399);
			drawable.setBounds(0, 0, widthLayout, heightLayout);
			drawable.draw(canvas);
			// ----------TEXT INFO-----------//
			String text = "";
			mainPaint.setStyle(Style.FILL);
			mainPaint.setTextSize(KTS3);
			mainPaint.setARGB(255, 0, 254, 255);

			mainPaint.setARGB(255, 180, 254, 255);
			text = getResources().getString(R.string.popup_uppic1b);
			float textWidth = mainPaint.measureText(text);
			canvas.drawText(text, widthLayout / 2 - textWidth / 2, KT2Y,
					mainPaint);

			text = getResources().getString(R.string.popup_uppic1c) + " "
					+ totalDownSize + " MB";
			textWidth = mainPaint.measureText(text);
			canvas.drawText(text, widthLayout / 2 - textWidth / 2, KT3Y,
					mainPaint);
			// ----------YES BORDER-----------//
			if (isActiveYes) {
				drawable = getResources().getDrawable(
						R.drawable.boder_cokhong_active_129x74);
			} else {
				drawable = getResources().getDrawable(
						R.drawable.boder_cokhong_inactive_129x74);
			}
			drawable.setBounds(KD1L, KD1T, KD1R, KD1B);
			drawable.draw(canvas);
			// ----------NO BORDER-----------//
			if (isActiveNo) {
				drawable = getResources().getDrawable(
						R.drawable.boder_cokhong_active_129x74);
			} else {
				drawable = getResources().getDrawable(
						R.drawable.boder_cokhong_inactive_129x74);
			}
			drawable.setBounds(KD2L, KD2T, KD2R, KD2B);
			drawable.draw(canvas);
			// ----------YES NO TEXT-----------//
			text = getResources().getString(R.string.popup_cn);
			textWidth = mainPaint.measureText(text);
			canvas.drawText(text, (KD1R + KD1L) / 2 - textWidth / 2, KT4Y,
					mainPaint);

			text = getResources().getString(R.string.popup_no);
			textWidth = mainPaint.measureText(text);
			canvas.drawText(text, (KD2R + KD2L) / 2 - textWidth / 2, KT4Y,
					mainPaint);
		} else if (popupLayout == LAYOUT_UPTODATE) {
			// ----------KHUNG-----------//
			drawable = getResources().getDrawable(
					R.drawable.boder_note_1116x399);
			drawable.setBounds(0, 0, widthLayout, heightLayout);
			drawable.draw(canvas);
			// ----------TEXT INFO-----------//
			String text = "";
			mainPaint.setStyle(Style.FILL);
			mainPaint.setTextSize(KTS3);
			mainPaint.setARGB(255, 180, 254, 255);

			text = getResources().getString(R.string.popup_uppic2a);
			float textWidth = mainPaint.measureText(text);
			canvas.drawText(text, widthLayout / 2 - textWidth / 2, KT2Y,
					mainPaint);

			KT3Y = 120 * heightLayout / 200;
			text = getResources().getString(R.string.popup_uppic2b);
			textWidth = mainPaint.measureText(text);
			canvas.drawText(text, widthLayout / 2 - textWidth / 2, KT3Y,
					mainPaint);
		} else if (popupLayout == LAYOUT_NOWIFI) {
			// ----------KHUNG-----------//
			drawable = getResources().getDrawable(
					R.drawable.boder_note_1116x399);
			drawable.setBounds(0, 0, widthLayout, heightLayout);
			drawable.draw(canvas);
			// ----------TEXT INFO-----------//
			String text = "";
			mainPaint.setStyle(Style.FILL);
			mainPaint.setTextSize(KTS3);
			mainPaint.setARGB(255, 180, 254, 255);

			text = getResources().getString(R.string.popup_uppic3a);
			float textWidth = mainPaint.measureText(text);
			canvas.drawText(text, widthLayout / 2 - textWidth / 2, KT2Y,
					mainPaint);

			KT3Y = 120 * heightLayout / 200;
			text = getResources().getString(R.string.popup_uppic3b);
			textWidth = mainPaint.measureText(text);
			canvas.drawText(text, widthLayout / 2 - textWidth / 2, KT3Y,
					mainPaint);
		} else if (popupLayout == LAYOUT_UPDATEDAUMAY) {
			KT1Y = 44 * heightLayout / 200;
			KT2Y = 75 * heightLayout / 200;
			KT3Y = 105 * heightLayout / 200;

			// ----------KHUNG-----------//
			drawable = getResources().getDrawable(
					R.drawable.boder_note_1116x399);
			drawable.setBounds(0, 0, widthLayout, heightLayout);
			drawable.draw(canvas);
			// ----------TEXT INFO-----------//
			String text = "";
			mainPaint.setStyle(Style.FILL);
			mainPaint.setTextSize(KTS1);
			mainPaint.setARGB(255, 180, 254, 255);

			text = getResources().getString(R.string.popup_uppic4a);
			float textWidth = mainPaint.measureText(text);
			canvas.drawText(text, widthLayout / 2 - textWidth / 2, KT1Y,
					mainPaint);

			text = getResources().getString(R.string.popup_uppic4b) + " "
					+ serverName;
			textWidth = mainPaint.measureText(text);
			canvas.drawText(text, widthLayout / 2 - textWidth / 2, KT2Y,
					mainPaint);

			text = getResources().getString(R.string.popup_uppic4c) + " "
					+ totalDownSize + " MB";
			textWidth = mainPaint.measureText(text);
			canvas.drawText(text, widthLayout / 2 - textWidth / 2, KT3Y,
					mainPaint);
			// ----------YES BORDER-----------//
			if (isActiveYes) {
				drawable = getResources().getDrawable(
						R.drawable.boder_cokhong_active_129x74);
			} else {
				drawable = getResources().getDrawable(
						R.drawable.boder_cokhong_inactive_129x74);
			}
			drawable.setBounds(KD1L, KD1T, KD1R, KD1B);
			drawable.draw(canvas);
			// ----------NO BORDER-----------//
			if (isActiveNo) {
				drawable = getResources().getDrawable(
						R.drawable.boder_cokhong_active_129x74);
			} else {
				drawable = getResources().getDrawable(
						R.drawable.boder_cokhong_inactive_129x74);
			}
			drawable.setBounds(KD2L, KD2T, KD2R, KD2B);
			drawable.draw(canvas);
			// ----------YES NO TEXT-----------//
			text = getResources().getString(R.string.popup_cn);
			textWidth = mainPaint.measureText(text);
			canvas.drawText(text, (KD1R + KD1L) / 2 - textWidth / 2, KT4Y,
					mainPaint);

			text = getResources().getString(R.string.popup_no);
			textWidth = mainPaint.measureText(text);
			canvas.drawText(text, (KD2R + KD2L) / 2 - textWidth / 2, KT4Y,
					mainPaint);
		} else if (popupLayout == LAYOUT_NOWIFI2) {
			// ----------KHUNG-----------//
			drawable = getResources().getDrawable(
					R.drawable.boder_note_1116x399);
			drawable.setBounds(0, 0, widthLayout, heightLayout);
			drawable.draw(canvas);
			// ----------TEXT INFO-----------//
			String text = "";
			mainPaint.setStyle(Style.FILL);
			mainPaint.setTextSize(KTS3);
			mainPaint.setARGB(255, 180, 254, 255);

			text = getResources().getString(R.string.popup_uppic5a);
			float textWidth = mainPaint.measureText(text);
			canvas.drawText(text, widthLayout / 2 - textWidth / 2, KT2Y,
					mainPaint);

			KT3Y = 120 * heightLayout / 200;
			text = getResources().getString(R.string.popup_uppic5b);
			textWidth = mainPaint.measureText(text);
			canvas.drawText(text, widthLayout / 2 - textWidth / 2, KT3Y,
					mainPaint);
		} else if (popupLayout == LAYOUT_PROCESSDATA) {
			// ----------KHUNG-----------//
			drawable = getResources().getDrawable(
					R.drawable.boder_note_1116x399);
			drawable.setBounds(0, 0, widthLayout, heightLayout);
			drawable.draw(canvas);
			// ----------TEXT INFO-----------//
			String text = "";
			mainPaint.setStyle(Style.FILL);
			mainPaint.setTextSize(KTS3);
			mainPaint.setARGB(255, 180, 254, 255);

			text = getResources().getString(R.string.popup_uppic6a);
			float textWidth = mainPaint.measureText(text);
			canvas.drawText(text, widthLayout / 2 - textWidth / 2, KT2Y,
					mainPaint);

			KT3Y = 120 * heightLayout / 200;
			text = getResources().getString(R.string.popup_uppic6b);
			textWidth = mainPaint.measureText(text);
			canvas.drawText(text, widthLayout / 2 - textWidth / 2, KT3Y,
					mainPaint);
		}else if (popupLayout == LAYOUT_FIRSTIME) {
			KT1Y = 50 * heightLayout / 200;
			KT2Y = 100* heightLayout / 200;
			KT3Y = 150 * heightLayout / 200;

			// ----------KHUNG-----------//
			drawable = getResources().getDrawable(
					R.drawable.boder_note_1116x399);
			drawable.setBounds(0, 0, widthLayout, heightLayout);
			drawable.draw(canvas);
			// ----------TEXT INFO-----------//
			String text = "";
			mainPaint.setStyle(Style.FILL);
			mainPaint.setTextSize(KTS3);
			mainPaint.setARGB(255, 180, 254, 255);

			text = getResources().getString(R.string.popup_uppic7a);
			float textWidth = mainPaint.measureText(text);
			canvas.drawText(text, widthLayout / 2 - textWidth / 2, KT1Y,
					mainPaint);

			text = getResources().getString(R.string.popup_uppic7b) + " "
					+ serverName;
			textWidth = mainPaint.measureText(text);
			canvas.drawText(text, widthLayout / 2 - textWidth / 2, KT2Y,
					mainPaint);

			text = getResources().getString(R.string.popup_uppic7c);
			textWidth = mainPaint.measureText(text);
			canvas.drawText(text, widthLayout / 2 - textWidth / 2, KT3Y,
					mainPaint);			
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (popupLayout == LAYOUT_UPDATE || popupLayout == LAYOUT_UPDATEDAUMAY) {
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
						listener.OnUpdatePicYes();
					}
				}

				if (x >= KD2L && x <= KD2R && y >= KD2T && y <= KD2B) {
					if (listener != null) {
						listener.OnUpdatePicNo();
					}
				}

				invalidate();
			}
				break;
			default:
				break;
			}
		}

		return true;
	}

	private float KTS1, KT1X, KT1Y, KT2Y, KT3Y, KT4Y;
	private int KD1L, KD1R, KD1T, KD1B;
	private int KD2L, KD2R, KD2T, KD2B;
	private float KTS2, KTS3;

	private void setK(int w, int h) {
		widthLayout = w;
		heightLayout = h;

		KTS1 = 17 * h / 200;
		KT1X = 45 * w / 400;
		KT1Y = 50 * h / 200;

		KT2Y = 60 * h / 200;
		KT3Y = 100 * h / 200;

		KD1L = 90 * widthLayout / 400;
		KD1R = 190 * widthLayout / 400;
		KD1T = 125 * heightLayout / 200;
		KD1B = 170 * heightLayout / 200;

		KD2L = 220 * widthLayout / 400;
		KD2R = 320 * widthLayout / 400;
		KD2T = 125 * heightLayout / 200;
		KD2B = 170 * heightLayout / 200;

		KT4Y = 153 * h / 200;

		KTS2 = 17 * h / 200;
		KTS3 = 20 * h / 200;
	}
}
