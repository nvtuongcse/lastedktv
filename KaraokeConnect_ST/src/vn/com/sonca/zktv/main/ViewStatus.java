package vn.com.sonca.zktv.main;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import vn.com.hanhphuc.karremote.R;

public class ViewStatus extends View {
	
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	
	public ViewStatus(Context context) {
		super(context);
		initView(context);
	}

	public ViewStatus(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public ViewStatus(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Animation anime;
	private void initView(Context context) {
		anime = AnimationUtils.loadAnimation(context, R.anim.animation_song);
	}
		
	private int widthLayout, heightLayout;
	private Rect rectImage = new Rect();
	private int textS, textX, textY;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		widthLayout = w;
		heightLayout = h;
		
		int tamX = (int) (0.5*h);
		int tamY = (int) (0.5*h);
		int hh = (int) (0.5*h);
		int ww = 120*hh/88;
		rectImage.set(tamX-ww, 0, tamX+ww, tamY+hh);
		
		textS = (int) (0.3*h);
		textX = (int) (0.5*w);
		textY = (int) (0.5*h + 0.4*textS);
		
		textPaint.setTextSize(textS);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		textPaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
		
		if(isAction){			
			textPaint.setColor(Color.WHITE);
			float mWidth = textPaint.measureText(nameAction);
			float leftText = (widthLayout - mWidth)/2;
			canvas.drawText(nameAction, leftText, textY, textPaint);
			
			if (drawAction != null) {
				int tamY = (int) (0.5*heightLayout);
				int hh = (int) (0.25*heightLayout);
				int ww = 112*hh/64;
				if(nameAction.equalsIgnoreCase(getResources().getString(R.string.ktv_caocap_11c))){
					hh = (int) (0.4*heightLayout);
					ww = 120*hh/88;
				}
				if(isTheLoai){
					hh = (int) (0.3*heightLayout);
					ww = 180*hh/128;
				}
				int tamX = (int)(leftText - ww/2 - 80 * widthLayout / 1920);
				rectImage.set(tamX-ww, tamY-hh, tamX+ww, tamY+hh);
				
				drawAction.setBounds(rectImage);
				drawAction.draw(canvas);
			}
			
		} else {
			textPaint.setColor(Color.WHITE);
			float mWidth = textPaint.measureText(statusName);
			canvas.drawText(statusName, (widthLayout - mWidth)/2 , textY, textPaint);
		}
		
	}

	private boolean isAction = false;
	private Drawable drawAction;
	private String nameAction = "";
	public void setStatusAction(Drawable drawAction, String nameAction){
		this.isTheLoai = false;
		this.isAction = true;
		this.drawAction = drawAction;
		this.nameAction = nameAction;
		invalidate();
	}
	
	private String statusName = "";
	public void setStatusName(String statusName){
		this.isTheLoai = false;
		this.isAction = false;
		this.statusName = statusName;
		invalidate();
	}
	
	private boolean isTheLoai = false;
	public void setStatusTheLoai(Drawable drawAction, String nameAction){
		this.isTheLoai = true;
		this.isAction = true;
		this.drawAction = drawAction;
		this.nameAction = nameAction;
		invalidate();
	}
	
}
