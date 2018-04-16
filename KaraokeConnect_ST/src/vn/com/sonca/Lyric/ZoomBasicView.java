package vn.com.sonca.Lyric;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public abstract class ZoomBasicView extends View {
	
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint mainPait = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public ZoomBasicView(Context context) {
		super(context);
		initView(context);
	}

	public ZoomBasicView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public ZoomBasicView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	protected abstract int getIconIN();
	protected abstract int getIcon();
	protected abstract int getText();
	
	private OnZoomViewListener listener;
	public interface OnZoomViewListener{
		public void OnZoom();
	}
	
	protected void setOnZoomViewListener(OnZoomViewListener listener){
		this.listener = listener;
	}
	
	private String text = "";
	private Drawable drawable;
	private Drawable drawableIN;
	private Drawable zlightDrawable;
	
	private void initView(Context context) {
		drawableIN = getResources().getDrawable(getIconIN());
		drawable = getResources().getDrawable(getIcon());
		text = getResources().getString(getText());
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int myWidth = (int) (1.0*widthMeasureSpec);
		int myHeight = (int) (0.5*widthMeasureSpec);	
	    setMeasuredDimension(myWidth, myHeight);
	}
	
	private int widthLayout;
	private int heightLayout;
	private int textX, textY, textS;
	private float left, right, top, bottom;
	private Path pathLine = new Path();
	private Rect rectBackgroud = new Rect();
	private Rect rectDraw = new Rect();
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		widthLayout = w;
		heightLayout = h;
		
		left = w/10;
		right = w - w/10;
		float wr = 2*(right - left)/4;
		top = h/4;
		bottom = h - h/4;
		float line = (float) (0.1*wr);
				
		paintLine.setStrokeWidth((float) (0.04*wr));
		pathLine.reset();
		pathLine.moveTo(left, top + line);
		pathLine.lineTo(left, top);
		pathLine.lineTo(left + line, top);
		pathLine.moveTo(right - line, top);
		pathLine.lineTo(right, top);
		pathLine.lineTo(right, top + line);
		pathLine.moveTo(right, bottom - line);
		pathLine.lineTo(right, bottom);
		pathLine.lineTo(right - line, bottom);
		pathLine.moveTo(left + line, bottom);
		pathLine.lineTo(left, bottom);
		pathLine.lineTo(left, bottom - line);
		
		rectBackgroud.set((int)left, (int)top, (int)right, (int)bottom);
		
		int tamX = (int) (0.5*w);
		int tamY = (int) (0.5*h);
		int height = (int) (0.25*h);
		int width = 100*height/70;
		rectDraw.set((int)left, tamY - height, 
				(int)(left + 2*width), tamY + height);
		
		textX = (int) (left + 2*width - 5);
		textS = (int) (0.2*h);
		textY = (int) (tamY + textS/2.2);
		
	}
	
	private int color_01;
	private int color_02;
	private int color_03;
	private int color_04;
	private int color_05;
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		/*
		 * isTouch = true; isActiveView = true; MyApplication.intColorScreen =
		 * MyApplication.SCREEN_LIGHT;
		 */
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			color_01 = Color.argb(80, 36, 83, 112);
			color_02 = Color.argb(255, 36, 83, 112);
			color_03 = Color.argb(255, 0, 253, 253);
			color_04 = Color.argb(100, 229, 234, 240);
			color_05 = Color.argb(255, 163, 166, 169);
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			color_01 = Color.parseColor("#FFFFFF");
			color_02 = Color.argb(255, 36, 83, 112);
			color_03 = Color.argb(255, 0, 253, 253);
			color_04 = Color.argb(100, 229, 234, 240);
			color_05 = Color.argb(255, 163, 166, 169);
		}

		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			// DRAW 0
			if (isActiveView == true) {
				mainPait.setStyle(Style.FILL);
				if (isTouch) {
					mainPait.setColor(color_01);
				} else {
					mainPait.setColor(color_02);
				}
				canvas.drawRect(rectBackgroud, mainPait);

				paintLine.setStyle(Style.STROKE);
				paintLine.setColor(color_03);
				canvas.drawPath(pathLine, paintLine);

				drawable.setBounds(rectDraw);
				drawable.draw(canvas);

				textPaint.setStyle(Style.FILL);
				textPaint.setColor(Color.WHITE);
				textPaint.setTextSize(textS);
				canvas.drawText(text, textX, textY, textPaint);
			} else {
				mainPait.setStyle(Style.FILL);
				mainPait.setColor(color_04);
				canvas.drawRect(rectBackgroud, mainPait);

				paintLine.setStyle(Style.STROKE);
				paintLine.setColor(color_05);
				canvas.drawPath(pathLine, paintLine);

				drawableIN.setBounds(rectDraw);
				drawableIN.draw(canvas);

				textPaint.setStyle(Style.FILL);
				textPaint.setColor(color_05);
				textPaint.setTextSize(textS);
				canvas.drawText(text, textX, textY, textPaint);
			}

		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {

			// DRAW 0
			if (isActiveView == true) {
				if (isTouch) {
					zlightDrawable = getResources().getDrawable(
							R.drawable.zlight_boder_ketnoi_hover);
				} else {
					zlightDrawable = getResources().getDrawable(
							R.drawable.zlight_boder_ketnoi);
				}
				if (zlightDrawable != null) {
					zlightDrawable.setBounds(rectBackgroud);
					zlightDrawable.draw(canvas);
				}

				drawable.setBounds(rectDraw);
				drawable.draw(canvas);

				textPaint.setStyle(Style.FILL);
				textPaint.setColor(Color.WHITE);
				textPaint.setTextSize(textS);
				canvas.drawText(text, textX, textY, textPaint);
			} else {
				zlightDrawable = getResources().getDrawable(
						R.drawable.zlight_boder_ketnoi_xam);
				if (zlightDrawable != null) {
					zlightDrawable.setBounds(rectBackgroud);
					zlightDrawable.draw(canvas);
				}

				drawableIN.setBounds(rectDraw);
				drawableIN.draw(canvas);

				textPaint.setStyle(Style.FILL);
				textPaint.setColor(Color.WHITE);
				textPaint.setTextSize(textS);
				canvas.drawText(text, textX, textY, textPaint);
			}

		}
		
	}
	
	private boolean isTouch = false;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(isActiveView == true){
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if(rectBackgroud != null){
					float x = event.getX();
					float y = event.getY();
					if (x >= rectBackgroud.left && x <= rectBackgroud.right &&
						y >= rectBackgroud.top && y<= rectBackgroud.bottom) {
						isTouch = true;
						invalidate();
					}
				}
				break;
				
			case MotionEvent.ACTION_UP:
				if(rectBackgroud != null){
					float x = event.getX();
					float y = event.getY();
					if (x >= rectBackgroud.left && x <= rectBackgroud.right &&
						y >= rectBackgroud.top && y<= rectBackgroud.bottom) {
						if(listener != null){
							listener.OnZoom();
						}
					}
				}
				isTouch = false;
				invalidate();
				break;
			default:
				break;
			}
		}
		return true;
	}
	
	
	public boolean isActiveView() {
		return isActiveView;
	}

	public void setActiveView(boolean isActiveView) {
		this.isActiveView = isActiveView;
		invalidate();
	}

	private boolean isActiveView = true;

}
