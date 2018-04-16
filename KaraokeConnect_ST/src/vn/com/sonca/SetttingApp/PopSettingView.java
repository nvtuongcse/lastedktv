package vn.com.sonca.SetttingApp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.SetttingApp.AppInfoView.OnAppInfoViewListener;
import vn.com.sonca.zzzzz.MyApplication;

public class PopSettingView extends View {

	private Paint mainText = new Paint(Paint.ANTI_ALIAS_FLAG);

	public PopSettingView(Context context) {
		super(context);
		initView(context);
	}

	public PopSettingView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public PopSettingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private OnPopSettingListener listener;

	public interface OnPopSettingListener {
		public void onSetingPopup(boolean flagOnPopup);
	}

	public void setOnPopSettingListener(OnPopSettingListener listener) {
		this.listener = listener;
	}

	private Drawable drawable;
	private Drawable drawOn, drawOnAc, drawOff, drawOffAc;
	private Drawable zlight_drawOn, zlight_drawOnAc, zlight_drawOff,
			zlight_drawOffAc;

	private void initView(Context context) {
		drawOn = getResources().getDrawable(R.drawable.pop_setting_bat);
		drawOnAc = getResources().getDrawable(R.drawable.pop_setting_bat_hover);
		drawOff = getResources().getDrawable(R.drawable.pop_setting_tat);
		drawOffAc = getResources()
				.getDrawable(R.drawable.pop_setting_tat_hover);

		zlight_drawOn = getResources().getDrawable(
				R.drawable.zlight_pop_setting_bat);
		zlight_drawOnAc = getResources().getDrawable(
				R.drawable.zlight_pop_setting_bat);
		zlight_drawOff = getResources().getDrawable(
				R.drawable.zlight_pop_setting_tat);
		zlight_drawOffAc = getResources().getDrawable(
				R.drawable.zlight_pop_setting_tat);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = (int) (0.07 * width);
		setMeasuredDimension(width, height);
	}

	private float titleS, titleX, titleY;
	private Rect rectButton;

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		titleS = 0.7f * h;
		titleY = 0.5f * h + 0.3f * titleS;
		titleX = 0 * w / 400;

		int tx = 550 * w / 650;
		int ty = h / 2;
		int hr = 20 * w / 650;
		int wr = 3 * hr;
		rectButton = new Rect(tx - wr, ty - hr, tx + wr, ty + hr);
	}

	private int color1;

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			color1 = Color.argb(255, 180, 254, 255);
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			color1 = Color.parseColor("#005249");
		}

		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			mainText.setStyle(Style.FILL);
			mainText.setColor(color1);
			mainText.setTextSize(titleS);
			canvas.drawText(title, titleX, titleY, mainText);

			if (flagOnPopup) {
				if (isHover) {
					drawable = drawOnAc;
				} else {
					drawable = drawOn;
				}
			} else {
				if (isHover) {
					drawable = drawOffAc;
				} else {
					drawable = drawOff;
				}
			}

			drawable.setBounds(rectButton);
			drawable.draw(canvas);

		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			mainText.setStyle(Style.FILL);
			mainText.setColor(color1);
			mainText.setTextSize(titleS);
			canvas.drawText(title, titleX, titleY, mainText);

			if (flagOnPopup) {
				if (isHover) {
					drawable = zlight_drawOnAc;
				} else {
					drawable = zlight_drawOn;
				}
			} else {
				if (isHover) {
					drawable = zlight_drawOffAc;
				} else {
					drawable = zlight_drawOff;
				}
			}

			drawable.setBounds(rectButton);
			drawable.draw(canvas);
		}

	}

	private String title = "";

	public void setTitleView(String title) {
		this.title = title;
		invalidate();
	}

	private boolean flagOnPopup;

	public void setOnPopup(boolean flagOnPopup) {
		this.flagOnPopup = flagOnPopup;
		invalidate();
	}

	private boolean isHover = false;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			float x = event.getX();
			float y = event.getY();
			if (x >= rectButton.left && x <= rectButton.right
					&& y >= rectButton.top && y <= rectButton.bottom) {
				isHover = true;
				invalidate();
			}
		}
			break;
		case MotionEvent.ACTION_UP: {
			isHover = false;
			float x = event.getX();
			float y = event.getY();
			if (x >= rectButton.left && x <= rectButton.right
					&& y >= rectButton.top && y <= rectButton.bottom) {
				if(listener != null){
					flagOnPopup = !flagOnPopup;
					listener.onSetingPopup(flagOnPopup);
				}
			}
		}
			invalidate();
			break;
		default:
			break;
		}

		return true;
	}
}
