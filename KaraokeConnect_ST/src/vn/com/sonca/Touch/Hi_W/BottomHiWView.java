package vn.com.sonca.Touch.Hi_W;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.zzzzz.MyApplication;

public class BottomHiWView extends View {
	
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	
	public BottomHiWView(Context context) {
		super(context);
		initView(context);
	}

	public BottomHiWView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public BottomHiWView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Drawable drawOKIN;
	private Drawable drawOKAC;
	private Drawable drawNOOK;
	
	private Drawable zlightdrawOKIN;
	private Drawable zlightdrawOKAC;
	private Drawable zlightdrawNOOK;
	
	private String textOk = "";
	private String textBack = "";
	
	private void initView(Context context){
		drawOKIN = getResources().getDrawable(R.drawable.boder_cokhong_inactive_129x74);
		drawOKAC = getResources().getDrawable(R.drawable.boder_cokhong_active_129x74);
		drawNOOK = getResources().getDrawable(R.drawable.touch_boder_cokhong_xam_129x74);

		zlightdrawOKIN = getResources().getDrawable(R.drawable.zlight_boder_cokhong_inactive_129x74);
		zlightdrawOKAC = getResources().getDrawable(R.drawable.zlight_boder_cokhong_hover_129x74);
		zlightdrawNOOK = getResources().getDrawable(R.drawable.zlight_boder_cokhong_xam_129x74);
		
		textBack = getResources().getString(R.string.hiw_button_left);
		textOk = getResources().getString(R.string.hiw_button_right);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	private int widthView;
	private int heightView;
	private float textS, textX, textY;
	private float backS, backX, backY;
	private Rect rectOK = new Rect();
	private Rect rectBack = new Rect();
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		widthView = w;
		heightView = h;
		
		int tamX = (int) (0.6*w);
		int tamY = h/2;
		int hr = (int) (0.35*h);
		int wr = 129*hr/74;
		rectOK.set(tamX - wr, tamY - hr, tamX + wr, tamY + hr);
		textS = (float) (0.2*h);
		textPaint.setTextSize(textS);
		float wid = textPaint.measureText(textOk);
		textX = tamX - wid/2;
		textY = h/2 + textS/2;
		
		tamX = (int) (0.4*w);
		tamY = h/2;
		hr = (int) (0.35*h);
		wr = 129*hr/74;
		rectBack.set(tamX - wr, tamY - hr, tamX + wr, tamY + hr);
		backS = (float) (0.2*h);
		textPaint.setTextSize(backS);
		wid = textPaint.measureText(textBack);
		backX = tamX - wid/2;
		backY = h/2 + backS/2;
		
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		// MyApplication.intColorScreen = MyApplication.SCREEN_LIGHT;
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			if (isClickOK) {
				drawOKAC.setBounds(rectOK);
				drawOKAC.draw(canvas);
			} else {
				drawOKIN.setBounds(rectOK);
				drawOKIN.draw(canvas);
			}
			if (isClickBack) {
				drawOKAC.setBounds(rectBack);
				drawOKAC.draw(canvas);
			} else {
				drawOKIN.setBounds(rectBack);
				drawOKIN.draw(canvas);
			}
			textPaint.setStyle(Style.FILL);
			textPaint.setARGB(255, 180, 255, 253);
			textPaint.setTextSize(textS);
			canvas.drawText(textOk, textX, textY, textPaint);
			textPaint.setTextSize(backS);
			canvas.drawText(textBack, backX, backY, textPaint);
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			paintMain.setStyle(Style.FILL);
			paintMain.setColor(Color.parseColor("#64949F"));
			paintMain.setAlpha(178);
			canvas.drawRect(0, 0, widthView, heightView, paintMain);
			if (isClickOK) {
				zlightdrawOKAC.setBounds(rectOK);
				zlightdrawOKAC.draw(canvas);
			} else {
				zlightdrawOKIN.setBounds(rectOK);
				zlightdrawOKIN.draw(canvas);
			}
			if (isClickBack) {
				zlightdrawOKAC.setBounds(rectBack);
				zlightdrawOKAC.draw(canvas);
			} else {
				zlightdrawOKIN.setBounds(rectBack);
				zlightdrawOKIN.draw(canvas);
			}
			textPaint.setStyle(Style.FILL);
			textPaint.setColor(Color.parseColor("#005249"));
			textPaint.setTextSize(textS);
			canvas.drawText(textOk, textX, textY, textPaint);
			textPaint.setTextSize(backS);
			canvas.drawText(textBack, backX, backY, textPaint);
		}

	}
	
	private boolean isClickOK = false;
	private boolean isClickBack = false;
	@Override
	public boolean onTouchEvent(MotionEvent event) {		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if(rectOK != null){
				float x = event.getX();
				float y = event.getY();
				if(x >= rectOK.left && x <= rectOK.right &&
					y >= rectOK.top && y <= rectOK.bottom){
					isClickOK = true;
					invalidate();
				}
			}
			if(rectBack != null){
				float x = event.getX();
				float y = event.getY();
				if(x >= rectBack.left && x <= rectBack.right &&
					y >= rectBack.top && y <= rectBack.bottom){
					isClickBack = true;
					invalidate();
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			isClickOK = false;
			isClickBack = false;
			invalidate();
			if(rectOK != null){
				float x = event.getX();
				float y = event.getY();
				if(x >= rectOK.left && x <= rectOK.right &&
					y >= rectOK.top && y <= rectOK.bottom){
					if(listener != null){
						listener.OnCancel();
					}
				}
			}
			if(rectBack != null){
				float x = event.getX();
				float y = event.getY();
				if(x >= rectBack.left && x <= rectBack.right &&
					y >= rectBack.top && y <= rectBack.bottom){
					if(listener != null){
						listener.OnChange();
					}
				}
			}
			break;
		default: 	break;
		}
		return true;
	}
	
	private OnClickKiWListener listener;
	public interface OnClickKiWListener{
		public void OnCancel();
		public void OnChange();
	}
	
	public void setOnClickKiWListener(OnClickKiWListener l) {
		this.listener = l;
	}
	
}
