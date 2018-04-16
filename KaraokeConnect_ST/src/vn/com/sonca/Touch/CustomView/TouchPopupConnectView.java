package vn.com.sonca.Touch.CustomView;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.zzzzz.MyApplication;
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

public class TouchPopupConnectView extends View {
	
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public static final int LAYOUT_ASK = 0;
	public static final int LAYOUT_ASK_MSG = 1;
	private int popupLayout = LAYOUT_ASK;
	
	public TouchPopupConnectView(Context context) {
		super(context);
		initView(context);
	}

	public TouchPopupConnectView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchPopupConnectView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private OnPopupConnectViewListener listener;
	public interface OnPopupConnectViewListener {
		public void OnNoListener();
		public void OnYesListener();
	}
	
	public void setOnPopupConnectViewListener(OnPopupConnectViewListener listener){
		this.listener = listener;
	}
	
	public void setPopupLayout(int layout) {
		this.popupLayout = layout;
		invalidate();
	}
	
	private String msg1 = "";
	private String msg2 = "";
	
	public void setDialogMessage(String msg1, String msg2){
		this.msg1 = msg1;
		this.msg2 = msg2;
		invalidate();
	}
	
	private String lineQ0 = "";
	private String lineQ1 = "";
	private String titleButLeft = "";
	private String titleButRight = "";
	
	private Drawable drawBackGround;
	private Drawable butActive;
	private Drawable butInActive;
	
	private Drawable zlightdrawBackGround;
	private Drawable zlightbutActive;
	private Drawable zlightbutInActive;
	private void initView(Context context) {
		zlightdrawBackGround = getResources().getDrawable(R.drawable.zlight_boder_popup);
		zlightbutActive = getResources().getDrawable(R.drawable.zlight_boder_cokhong_hover_129x74);
		zlightbutInActive = getResources().getDrawable(R.drawable.zlight_boder_cokhong_inactive_129x74);		
		
		drawBackGround = getResources().getDrawable(R.drawable.icon_boder_popup);
		butActive = getResources().getDrawable(R.drawable.boder_cokhong_active_129x74);
		butInActive = getResources().getDrawable(R.drawable.boder_cokhong_inactive_129x74);
		
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int myHeight = (int) (0.34*getResources().getDisplayMetrics().heightPixels);
		int myWidth = (int) (2.0*myHeight);	
	    setMeasuredDimension(myWidth, myHeight);
	}
	
	private int widthView;
	private int heightView;
	private Rect rectButLeft = new Rect();
	private Rect rectButRight = new Rect();
	private float textLQ1X, textLQ1Y, textLQ1S;
	private float textLQ2X, textLQ2Y, textLQ2S;
	private float textB1X, textB1Y, textB1S;
	private float textB2X, textB2Y, textB2S;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		widthView = w;
		heightView = h;
		
		if(msg1.equals("") && msg2.equals("")){
			lineQ0 = getResources().getString(R.string.popup_connect_1);
			lineQ1 = getResources().getString(R.string.popup_connect_2);

			titleButLeft = getResources().getString(R.string.popup_connect_4);
			titleButRight = getResources().getString(R.string.popup_connect_3);			

			textLQ1S = (float) (0.1*h);
			textLQ1X = (float) (0.15*w);
			textLQ1Y = (float) (0.3*h);
			
			textLQ2S = textLQ1S;
			textLQ2X = textLQ1X;
			textLQ2Y = (float) (0.45*h);
			
		} else {
			lineQ0 = msg1;
			lineQ1 = msg2;

			titleButLeft = getResources().getString(R.string.popup_upall_c3);
			titleButRight = getResources().getString(R.string.popup_connect_3);			

			textLQ1S = (float) (0.1*h);
			textLQ1X = (float) (0.13*w);
			textLQ1Y = (float) (0.3*h);
			
			textLQ2S = textLQ1S;
			textLQ2X = textLQ1X;
			textLQ2Y = (float) (0.45*h);
			
		}	
		
		int height = (int) (0.13*h);
		int width = 129*height/74;
		
		textB1S = (float) (0.1*h);
		textB2S = textB1S;
		
		int tamX = (int) (0.33*w);
		int tamY = (int) (0.7*h);
		textPaint.setTextSize(textB1S);
		float witdthText = textPaint.measureText(titleButLeft);
		textB1X = (float) (tamX - 0.5*witdthText);
		textB1Y = (float) (tamY + 0.4*textB1S);
		rectButLeft.set(tamX - width, tamY - height, tamX + width, tamY + height);
		tamX = (int) (0.67*w);
		witdthText = textPaint.measureText(titleButRight);
		textB2X = (float) (tamX - 0.5*witdthText);
		textB2Y = (float) (tamY + 0.4*textB2S);
		rectButRight.set(tamX - width, tamY - height, tamX + width, tamY + height);
		
	}
	
	private int color_01;
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		// MyApplication.intColorScreen = MyApplication.SCREEN_LIGHT;
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			color_01 = Color.argb(255, 180, 254, 255);
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			color_01 = Color.parseColor("#005249");
		}

		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {

			drawBackGround.setBounds(0, 0, widthView, heightView);
			drawBackGround.draw(canvas);

			if (stateClick == -1) {
				butActive.setBounds(rectButLeft);
				butActive.draw(canvas);
			} else {
				butInActive.setBounds(rectButLeft);
				butInActive.draw(canvas);
			}
			if (stateClick == 1) {
				butActive.setBounds(rectButRight);
				butActive.draw(canvas);
			} else {
				butInActive.setBounds(rectButRight);
				butInActive.draw(canvas);
			}

			textPaint.setStyle(Style.FILL);
			textPaint.setColor(color_01);
			textPaint.setTextSize(textLQ1S);
			canvas.drawText(lineQ0, textLQ1X, textLQ1Y, textPaint);
			canvas.drawText(lineQ1, textLQ2X, textLQ2Y, textPaint);
			textPaint.setColor(color_01);
			textPaint.setTextSize(textB1S);
			canvas.drawText(titleButLeft, textB1X, textB1Y, textPaint);
			canvas.drawText(titleButRight, textB2X, textB2Y, textPaint);

		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {

			zlightdrawBackGround.setBounds(0, 0, widthView, heightView);
			zlightdrawBackGround.draw(canvas);

			if (stateClick == -1) {
				zlightbutActive.setBounds(rectButLeft);
				zlightbutActive.draw(canvas);
			} else {
				zlightbutInActive.setBounds(rectButLeft);
				zlightbutInActive.draw(canvas);
			}
			if (stateClick == 1) {
				zlightbutActive.setBounds(rectButRight);
				zlightbutActive.draw(canvas);
			} else {
				zlightbutInActive.setBounds(rectButRight);
				zlightbutInActive.draw(canvas);
			}

			textPaint.setStyle(Style.FILL);
			textPaint.setColor(color_01);
			textPaint.setTextSize(textLQ1S);
			canvas.drawText(lineQ0, textLQ1X, textLQ1Y, textPaint);
			canvas.drawText(lineQ1, textLQ2X, textLQ2Y, textPaint);
			textPaint.setColor(color_01);
			textPaint.setTextSize(textB1S);
			canvas.drawText(titleButLeft, textB1X, textB1Y, textPaint);
			canvas.drawText(titleButRight, textB2X, textB2Y, textPaint);

		}
	}
	
	private int stateClick;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:{
			float x = event.getX();
			float y = event.getY();
			stateClick = 0;
			if (rectButLeft != null) 
			if(x >= rectButLeft.left && x <= rectButLeft.right &&
				y >= rectButLeft.top && y <= rectButLeft.bottom){
				stateClick = -1;
			}
			if(x >= rectButRight.left && x <= rectButRight.right &&
				y >= rectButRight.top && y <= rectButRight.bottom){
				stateClick = 1;
			}
			invalidate();
		}break;
		case MotionEvent.ACTION_UP:{
			if(listener != null){
				if (stateClick == 1) {
					listener.OnYesListener();
				}
				if (stateClick == -1) {
					listener.OnNoListener();
				}
			}
			stateClick = 0;
			invalidate();
		}break;
		default:	break;
		}
		return true;
	}
	
}
