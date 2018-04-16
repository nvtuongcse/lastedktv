package vn.com.sonca.mac;

import java.util.ArrayList;
import java.util.Collections;

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

public class MacPopupLogin extends View {

	private int widthLayout = 400;
	private int heightLayout = 400;
	private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Drawable drawable;

	private boolean hoverUser = false;
	private boolean hoverPass = false;
	private boolean hoverSerial = false;
	private boolean hoverCancel = false;
	private boolean hoverOK = false;

	private String txtUser = "";
	private String txtPass = "";
	private String txtSerial = "";
	private String txtHint = "";

	public void insertChar(char key) {
		try {
			if (hoverUser) {
				if (key == '@') {
					this.txtUser = this.txtUser.substring(0,
							txtUser.length() - 1);
				} else {
					txtUser += key;
				}
			}
			if (hoverPass) {
				if (key == '@') {
					this.txtPass = this.txtPass.substring(0,
							txtPass.length() - 1);
				} else {
					txtPass += key;
				}
			}
			if (hoverSerial) {
				if (key == '@') {
					this.txtSerial = this.txtSerial.substring(0,
							txtSerial.length() - 1);
				} else {
					txtSerial += key;
				}
			}
		} catch (Exception e) {
			return;
		}
		invalidate();
	}

	private Context context;

	public MacPopupLogin(Context context) {
		super(context);
		initView(context);
	}

	public MacPopupLogin(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public MacPopupLogin(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private Drawable drawOutBorder;
	private Drawable drawInBorder;
	private Drawable drawInBorderAc;
	private Drawable drawButton;
	private Drawable drawButtonAc;

	private void initView(Context context) {
		this.context = context;

		drawOutBorder = getResources().getDrawable(R.drawable.boder_list);
		drawInBorder = getResources().getDrawable(
				R.drawable.boder_login_inactive);
		drawInBorderAc = getResources().getDrawable(
				R.drawable.boder_login_active);
		drawButton = getResources().getDrawable(
				R.drawable.boder_karaoke_dance_221x147);
		drawButtonAc = getResources().getDrawable(
				R.drawable.boder_karaoke_dance_active_221x147);
	}

	private OnMACPopupLoginListener listener;

	public interface OnMACPopupLoginListener {
		public void OnCancel();

		public void OnOK(String user, String pass, String serial);
	}

	public void setOnMACPopupLoginListener(OnMACPopupLoginListener listener) {
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
		int myWidth = widthMeasureSpec;
		int myHeight = heightMeasureSpec;
		myWidth = (int) (7 * getResources().getDisplayMetrics().widthPixels / 10);
		setMeasuredDimension(
				MeasureSpec.makeMeasureSpec(myWidth, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(myHeight, MeasureSpec.UNSPECIFIED));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		setK(getWidth(), getHeight());

		// ----------3 KHUNG OUTSIDE-----------//
		drawOutBorder.setBounds(KD1L, KD1T, KD1R, KD1B);
		drawOutBorder.draw(canvas);
		drawOutBorder.setBounds(KD2L, KD2T, KD2R, KD2B);
		drawOutBorder.draw(canvas);
		drawOutBorder.setBounds(KD3L, KD3T, KD3R, KD3B);
		drawOutBorder.draw(canvas);
		// ----------3 KHUNG INSIDE-----------//
		if (hoverUser) {
			drawable = drawInBorderAc;
		} else {
			drawable = drawInBorder;
		}
		drawable.setBounds(KD4L, KD4T, KD4R, KD4B);
		drawable.draw(canvas);
		if (hoverPass) {
			drawable = drawInBorderAc;
		} else {
			drawable = drawInBorder;
		}
		drawable.setBounds(KD5L, KD5T, KD5R, KD5B);
		drawable.draw(canvas);
		if (hoverSerial) {
			drawable = drawInBorderAc;
		} else {
			drawable = drawInBorder;
		}
		drawable.setBounds(KD6L, KD6T, KD6R, KD6B);
		drawable.draw(canvas);
		// ----------3 LABEL-----------//
		mainPaint.setStyle(Style.FILL);
		mainPaint.setTextSize(RTS1);
		mainPaint.setARGB(255, 180, 254, 255);
		String text = "USER NAME";
		float textWidth = mainPaint.measureText(text);
		canvas.drawText(text, (KD1L + KD4L - textWidth) / 2, RT1Y, mainPaint);
		text = "PASSWORD";
		textWidth = mainPaint.measureText(text);
		canvas.drawText(text, (KD1L + KD4L - textWidth) / 2, RT2Y, mainPaint);
		text = "SERIAL NUMBER";
		textWidth = mainPaint.measureText(text);
		canvas.drawText(text, (KD1L + KD4L - textWidth) / 2, RT3Y, mainPaint);
		// ----------2 BUTTON-----------//
		if (hoverCancel) {
			drawable = drawButtonAc;
		} else {
			drawable = drawButton;
		}
		drawable.setBounds(KD7L, KD7T, KD7R, KD7B);
		drawable.draw(canvas);
		if (hoverOK) {
			drawable = drawButtonAc;
		} else {
			drawable = drawButton;
		}
		drawable.setBounds(KD8L, KD8T, KD8R, KD8B);
		drawable.draw(canvas);
		// ----------2 TEXT BUTTON-----------//
		mainPaint.setStyle(Style.FILL);
		mainPaint.setTextSize(RTS2);
		mainPaint.setARGB(255, 180, 254, 255);
		text = "CANCEL";
		textWidth = mainPaint.measureText(text);
		canvas.drawText(text, (KD7L + KD7R - textWidth) / 2, RTS2Y, mainPaint);
		text = "OK";
		textWidth = mainPaint.measureText(text);
		canvas.drawText(text, (KD8L + KD8R - textWidth) / 2, RTS2Y, mainPaint);
		// ----------3 INPUT-----------//
		mainPaint.setStyle(Style.FILL);
		mainPaint.setTextSize(RTS1);
		if (txtUser.equals("")) {
			mainPaint.setARGB(255, 99, 147, 149);
			txtHint = getResources().getString(R.string.mac_login_user);
			textWidth = mainPaint.measureText(txtHint);
			canvas.drawText(txtHint, (KD4L + KD4R - textWidth) / 2, RT1Y,
					mainPaint);
		} else {
			if (hoverUser) {
				mainPaint.setColor(Color.GREEN);
			} else {
				mainPaint.setARGB(255, 0, 253, 255);
			}
			textWidth = mainPaint.measureText(txtUser);
			canvas.drawText(txtUser, (KD4L + KD4R - textWidth) / 2, RT1Y,
					mainPaint);
		}

		if (txtPass.equals("")) {
			mainPaint.setARGB(255, 99, 147, 149);
			txtHint = getResources().getString(R.string.mac_login_pass);
			textWidth = mainPaint.measureText(txtHint);
			canvas.drawText(txtHint, (KD5L + KD5R - textWidth) / 2, RT2Y,
					mainPaint);
		} else {
			text = "";
			for (int i = 0; i < txtPass.length(); i++) {
				text += "*";
			}
			textWidth = mainPaint.measureText(text);
			if (hoverPass) {
				mainPaint.setColor(Color.GREEN);
			} else {
				mainPaint.setARGB(255, 0, 253, 255);
			}
			canvas.drawText(text, (KD5L + KD5R - textWidth) / 2, RT2Y,
					mainPaint);
		}

		if (txtSerial.equals("")) {
			mainPaint.setARGB(255, 99, 147, 149);
			txtHint = getResources().getString(R.string.mac_login_serial);
			textWidth = mainPaint.measureText(txtHint);
			canvas.drawText(txtHint, (KD6L + KD6R - textWidth) / 2, RT3Y,
					mainPaint);
		} else {
			textWidth = mainPaint.measureText(txtSerial);
			if (hoverSerial) {
				mainPaint.setColor(Color.GREEN);
			} else {
				mainPaint.setARGB(255, 0, 253, 255);
			}
			canvas.drawText(txtSerial, (KD6L + KD6R - textWidth) / 2, RT3Y,
					mainPaint);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			float x = event.getX();
			float y = event.getY();
			if (x > KD4L && x < KD4R && y > KD4T && y < KD4B) {
				hoverUser = true;
				hoverPass = false;
				hoverSerial = false;
			}
			if (x > KD5L && x < KD5R && y > KD5T && y < KD5B) {
				hoverPass = true;
				hoverUser = false;
				hoverSerial = false;
			}
			if (x > KD6L && x < KD6R && y > KD6T && y < KD6B) {
				hoverSerial = true;
				hoverPass = false;
				hoverUser = false;
			}
			if (x > KD7L && x < KD7R && y > KD7T && y < KD7B) {
				hoverCancel = true;
			}
			if (x > KD8L && x < KD8R && y > KD8T && y < KD8B) {
				hoverOK = true;
			}
			invalidate();
		}
			break;
		case MotionEvent.ACTION_UP: {
			float x = event.getX();
			float y = event.getY();
			hoverCancel = false;
			hoverOK = false;
			if (x > KD7L && x < KD7R && y > KD7T && y < KD7B) {
				if (listener != null) {
					listener.OnCancel();
				}
			}
			if (x > KD8L && x < KD8R && y > KD8T && y < KD8B) {
				if (listener != null) {
					listener.OnOK(txtUser, txtPass, txtSerial);
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

	private void handleFocusInput() {
		if (hoverUser) {
			hoverPass = false;
			hoverSerial = false;
		}
		if (hoverPass) {
			hoverUser = false;
			hoverSerial = false;
		}
		if (hoverSerial) {
			hoverPass = false;
			hoverUser = false;
		}
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

		KD1L = 180 * w / 1920;
		KD1R = 1620 * w / 1920;
		KD1T = 200 * h / 1080;
		KD1B = 350 * h / 1080;

		KD2L = 180 * w / 1920;
		KD2R = 1620 * w / 1920;
		KD2T = 370 * h / 1080;
		KD2B = 520 * h / 1080;

		KD3L = 180 * w / 1920;
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

		RTS1 = 40 * h / 960;
		RT1Y = 265 * h / 960;
		RT2Y = 415 * h / 960;
		RT3Y = 565 * h / 960;

		KD7L = 300 * h / 1080;
		KD7R = 600 * h / 1080;
		KD7T = 750 * h / 1080;
		KD7B = 950 * h / 1080;

		KD8L = 700 * h / 1080;
		KD8R = 1000 * h / 1080;
		KD8T = 750 * h / 1080;
		KD8B = 950 * h / 1080;

		RTS2 = 40 * h / 960;
		RTS2Y = 765 * h / 960;
	}
}
