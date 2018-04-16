package vn.com.sonca.Touch.CustomView;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TouchSearchDance extends View {
	
	private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	
	public TouchSearchDance(Context context) {
		super(context);
		initView(context);
	}

	public TouchSearchDance(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchSearchDance(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private OnSearchDanceClick listener;
	public interface OnSearchDanceClick {
		public void OnRandom();
	}
	public void setOnSearchDanceClick(OnSearchDanceClick listener){
		this.listener = listener;
	}

	private String baihat;
	private Drawable drawRanDomAC;
	private Drawable drawRanDomIN;
	private Drawable drawRanDomNO;
	
	private Drawable zlightdrawRanDomAC;
	private Drawable zlightdrawRanDomIN;
	private Drawable zlightdrawRanDomNO;
	
	private void initView(Context context) {
		baihat = getResources().getString(R.string.search_4);
		drawRanDomAC = getResources().getDrawable(R.drawable.touch_image_radom_active_121x70);
		drawRanDomIN = getResources().getDrawable(R.drawable.touch_image_radom_inactive_121x70);
		drawRanDomNO = getResources().getDrawable(R.drawable.touch_image_radom_xam_121x70);
		
		zlightdrawRanDomAC = getResources().getDrawable(R.drawable.zlight_image_radom_hover_121x70);
		zlightdrawRanDomIN = getResources().getDrawable(R.drawable.zlight_image_radom_active_121x70);
		zlightdrawRanDomNO = getResources().getDrawable(R.drawable.zlight_image_radom_xam);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
	}
	
	private int sumsong = 0;
	public void setSumSong(int sumsong , boolean isShow){
		this.sumsong = sumsong;
		if(isShow){
			invalidate();
		}
	}
	
	private float RanDom;
    private int widthLayout = 400;
    private int heightLayout = 400;
    private float trS , trX , trY;
    private float textS , textX , textY;
    private Rect rectRanDom = new Rect();
    @Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		widthLayout = w;
		heightLayout = h;
		textS = (float) (0.5*h);
		textX = (float) (0.03*w);
		textY = (float) (0.69*h);
		
		RanDom = (float) (0.84*w);
		
		int tamX = (int) (0.92*w);
		int tamY = (int) (0.5*h);
		int hr = (int) (0.5*h);
		int wr = 121*hr/70;
		rectRanDom = new Rect(tamX - wr, tamY - hr, tamX + wr, tamY + hr);
		trS = (float) (0.2*h);
		textPaint.setTextSize(trS);
		float x = textPaint.measureText("RANDOM");
		trX = tamX - x/2;
		trY = (float) (0.83*h);

	}
    
    private int color_01;
    private int color_02;
    private int color_03;
    private int color_04;
    
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		// MyApplication.intColorScreen = MyApplication.SCREEN_LIGHT;
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			color_01 = Color.argb(255, 1, 55, 102);
			color_02 = Color.CYAN;
			color_03 = Color.argb(255, 182, 253, 255);
			color_04 = Color.GRAY;
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			color_01 = Color.parseColor("#21BAA9");
			color_02 = Color.parseColor("#7B1FA2");
			color_03 = Color.parseColor("#21BAA9");
			color_04 = Color.parseColor("#FFFFFF");
		}
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			mainPaint.setStyle(Style.FILL);
			mainPaint.setColor(color_01);
			canvas.drawRect(0, 0, widthLayout, heightLayout, mainPaint);
			textPaint.setStyle(Style.FILL);
			textPaint.setColor(color_02);
			textPaint.setTextSize(textS);
			canvas.drawText(sumsong + " " + baihat, textX, textY, textPaint);
			if (MyApplication.isMoveList == true) {
				if (isTouch) {
					drawRanDomAC.setBounds(rectRanDom);
					drawRanDomAC.draw(canvas);
				} else {
					drawRanDomIN.setBounds(rectRanDom);
					drawRanDomIN.draw(canvas);
				}
			} else {
				drawRanDomNO.setBounds(rectRanDom);
				drawRanDomNO.draw(canvas);
			}
			textPaint.setTextSize(trS);
			if (MyApplication.isMoveList == true) {
				textPaint.setColor(color_03);
			} else {
				textPaint.setColor(color_04);
			}
			canvas.drawText("RANDOM", trX, trY, textPaint);
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			mainPaint.setStyle(Style.FILL);
			mainPaint.setColor(color_01);
			canvas.drawRect(0, 0, widthLayout, heightLayout, mainPaint);
			textPaint.setStyle(Style.FILL);
			textPaint.setColor(color_02);
			textPaint.setTextSize(textS);
			canvas.drawText(sumsong + " " + baihat, textX, textY, textPaint);
			if (MyApplication.isMoveList == true) {
				if (isTouch) {
					zlightdrawRanDomAC.setBounds(rectRanDom);
					zlightdrawRanDomAC.draw(canvas);
				} else {
					zlightdrawRanDomIN.setBounds(rectRanDom);
					zlightdrawRanDomIN.draw(canvas);
				}
			} else {
				zlightdrawRanDomNO.setBounds(rectRanDom);
				zlightdrawRanDomNO.draw(canvas);
			}
			textPaint.setTextSize(trS);
			if (MyApplication.isMoveList == true) {
				textPaint.setColor(color_03);
			} else {
				textPaint.setColor(color_04);
			}
			canvas.drawText("RANDOM", trX, trY, textPaint);

		}
	}
	
	private boolean isTouch = false;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		if(MyApplication.isMoveList == false){
			return true;
		}
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			float x = event.getX();
			if (x>=RanDom && x<=widthLayout) {
				isTouch = true;
				invalidate();
				if(listener != null){
					listener.OnRandom();
				}
			}
		}
		if(event.getAction() == MotionEvent.ACTION_UP){
			isTouch = false;
			invalidate();
		}
		return true;
	}

}
