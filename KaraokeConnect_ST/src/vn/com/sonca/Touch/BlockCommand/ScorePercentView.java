package vn.com.sonca.Touch.BlockCommand;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.zzzzz.MyApplication;

public class ScorePercentView extends View {
	
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintSimple = new Paint(Paint.ANTI_ALIAS_FLAG);
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	
	public ScorePercentView(Context context) {
		super(context);
		initView(context);
	}

	public ScorePercentView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public ScorePercentView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	OnScorePercentListener listener;
	public interface OnScorePercentListener {
		public void OnPercentListener(int percent);
	}
	
	public void setOnScorePercentListener(OnScorePercentListener listener){
		this.listener = listener;
	}
	
	private Drawable drawIN;
	private Drawable drawAC;
	private Drawable drawCheck;
	private Drawable drawHover;
	
	private Drawable zlightdrawIN;
	private Drawable zlightdrawAC;
	private Drawable zlightdrawCheck;
	private Drawable zlightdrawHover;
	
	private void initView(Context context) {
		drawCheck = getResources().getDrawable(R.drawable.check_icon);
		drawHover = getResources().getDrawable(R.drawable.image_boder_song);
		drawIN = getResources().getDrawable(R.drawable.touch_mc_socre_inactive_percent);
		drawAC = getResources().getDrawable(R.drawable.touch_mc_socre_on_percent);
		
		zlightdrawCheck = getResources().getDrawable(R.drawable.zlight_check_icon_white);
		zlightdrawHover = getResources().getDrawable(R.drawable.zlight_score_check);
		zlightdrawIN = getResources().getDrawable(R.drawable.zlight_score);
		zlightdrawAC = getResources().getDrawable(R.drawable.zlight_score_check);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	private int LINE1, LINE2;
	private int line1X, line1Y, line1S;
	private int line2X, line2Y, line2S;
	private int line3X, line3Y, line3S;
	private int widthLayout, heightLayout;
	private LinearGradient gradient;
	private Rect rectView = new Rect();
	private Rect rectBut1 = new Rect();
	private Rect rectBut2 = new Rect();
	private Rect rectBut3 = new Rect();
	private Rect check1 = new Rect();
	private Rect check2 = new Rect();
	private Rect check3 = new Rect();
	private Rect rectHover1 = new Rect();
	private Rect rectHover2 = new Rect();
	private Rect rectHover3 = new Rect();
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		widthLayout = w;
		heightLayout = h;
		gradient = new LinearGradient(0, 0, w, 0, 
				Color.argb(255, 0, 42, 80), 
				Color.argb(255, 1, 76, 141), 
				TileMode.CLAMP);
		rectView.set(0, 0, w - 1, h - 1);
		LINE1 = h/3;
		LINE2 = 2*h/3;
		
		rectHover1.set(0, 0, w, LINE1);
		rectHover2.set(0, LINE1, w, LINE2);
		rectHover3.set(0, LINE2, w, heightLayout);
		
		int height = h/3;
		line1S = line2S = line3S = (int) (0.35*height);
		line1X = line2X = line3X = (int) (1.4*height);
		float per = 0.5f;
		line1Y = (int) (per*LINE1 + 0.5*line1S);
		line2Y = (int) (LINE1 + per*LINE1 + 0.5*line1S);
		line3Y = (int) (LINE2 + per*LINE1 + 0.5*line1S);
		
		int hr = LINE1;
		int wr = hr; 
		int xx = (int) (0.2*LINE1);
		rectBut1.set(xx, 0, wr + xx, hr);
		rectBut2.set(xx, LINE1, wr + xx, LINE1 + hr);
		rectBut3.set(xx, LINE2, wr + xx, LINE2 + hr);
		
		hr = (int) (0.3*LINE1);
		wr = 160*hr/70; 
		int tamY = (int) (0.5*LINE1);
		int tamX = (int) (20);
		check1.set(w - wr - tamX, tamY - hr, w - tamX , tamY + hr);
		tamY = (int) (0.5*(LINE1 + LINE2));
		check2.set(w - wr - tamX, tamY - hr, w - tamX , tamY + hr);
		tamY = (int) (0.5*(LINE2 + heightLayout));
		check3.set(w - wr - tamX, tamY - hr, w - tamX , tamY + hr);
		
	}
	
	private int color_01;
	private int color_02;
	private int color_03;
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		// MyApplication.intColorScreen = MyApplication.SCREEN_LIGHT;
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			color_01 = Color.argb(255, 0, 253, 253);
			color_02 = Color.argb(255, 180, 254, 255);
			color_03 = Color.WHITE;
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			color_01 = Color.parseColor("#005249");
			color_02 = Color.parseColor("#FFFFFF");
			color_03 = Color.WHITE;
		}

		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			paintSimple.setShader(gradient);
			paintSimple.setStrokeWidth(getHeight());
			canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, paintSimple);
	
			paintMain.setStyle(Style.STROKE);
			paintMain.setColor(color_03);
			paintMain.setStrokeWidth(1);
			canvas.drawRect(rectView, paintMain);
			canvas.drawLine(0, LINE1, widthLayout, LINE1, paintMain);
			canvas.drawLine(0, LINE2, widthLayout, LINE2, paintMain);
	
			if (STATEVIEW == 2) {
				drawAC.setBounds(rectBut1);
				drawAC.draw(canvas);
				drawCheck.setBounds(check1);
				drawCheck.draw(canvas);
				drawHover.setBounds(rectHover1);
				drawHover.draw(canvas);
			} else {
				drawIN.setBounds(rectBut1);
				drawIN.draw(canvas);
			}
			if (STATEVIEW == 0) {
				drawAC.setBounds(rectBut2);
				drawAC.draw(canvas);
				drawCheck.setBounds(check2);
				drawCheck.draw(canvas);
				drawHover.setBounds(rectHover2);
				drawHover.draw(canvas);
			} else {
				drawIN.setBounds(rectBut2);
				drawIN.draw(canvas);
			}
			if (STATEVIEW == 1) {
				drawAC.setBounds(rectBut3);
				drawAC.draw(canvas);
				drawCheck.setBounds(check3);
				drawCheck.draw(canvas);
				drawHover.setBounds(rectHover3);
				drawHover.draw(canvas);
			} else {
				drawIN.setBounds(rectBut3);
				drawIN.draw(canvas);
			}
	
			textPaint.setStyle(Style.FILL);
			textPaint.setTextSize(line1S);
			if (STATEVIEW == 2) {
				textPaint.setColor(color_01);
			} else {
				textPaint.setColor(color_02);
			}
			String text = getResources().getString(R.string.command_6a);
			canvas.drawText(text, line1X, line1Y, textPaint);
			if (STATEVIEW == 0) {
				textPaint.setColor(color_01);
			} else {
				textPaint.setColor(color_02);
			}
			text = getResources().getString(R.string.command_6b);
			canvas.drawText(text, line2X, line2Y, textPaint);
			if (STATEVIEW == 1) {
				textPaint.setColor(color_01);
			} else {
				textPaint.setColor(color_02);
			}
			text = getResources().getString(R.string.command_6c);
			canvas.drawText(text, line3X, line3Y, textPaint);

		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			
			paintMain.setStyle(Style.FILL);
			paintMain.setColor(Color.parseColor("#21BAA9"));
			canvas.drawRect(0, 0, widthLayout, heightLayout, paintMain);
			
			paintMain.setStyle(Style.STROKE);
			paintMain.setColor(color_01);
			paintMain.setStrokeWidth(2);
			canvas.drawRect(rectView, paintMain);
			canvas.drawLine(0, LINE1, widthLayout, LINE1, paintMain);
			canvas.drawLine(0, LINE2, widthLayout, LINE2, paintMain);
	
			if (STATEVIEW == 2) {
				zlightdrawAC.setBounds(rectBut1);
				zlightdrawAC.draw(canvas);
				zlightdrawCheck.setBounds(check1);
				zlightdrawCheck.draw(canvas);
			} else {
				zlightdrawIN.setBounds(rectBut1);
				zlightdrawIN.draw(canvas);
			}
			if (STATEVIEW == 0) {
				zlightdrawAC.setBounds(rectBut2);
				zlightdrawAC.draw(canvas);
				zlightdrawCheck.setBounds(check2);
				zlightdrawCheck.draw(canvas);
			} else {
				zlightdrawIN.setBounds(rectBut2);
				zlightdrawIN.draw(canvas);
			}
			if (STATEVIEW == 1) {
				zlightdrawAC.setBounds(rectBut3);
				zlightdrawAC.draw(canvas);
				zlightdrawCheck.setBounds(check3);
				zlightdrawCheck.draw(canvas);
			} else {
				zlightdrawIN.setBounds(rectBut3);
				zlightdrawIN.draw(canvas);
			}
	
			textPaint.setStyle(Style.FILL);
			textPaint.setTextSize(line1S);
			if (STATEVIEW == 2) {
				textPaint.setColor(color_01);
			} else {
				textPaint.setColor(color_02);
			}
			String text = getResources().getString(R.string.command_6a);
			canvas.drawText(text, line1X, line1Y, textPaint);
			if (STATEVIEW == 0) {
				textPaint.setColor(color_01);
			} else {
				textPaint.setColor(color_02);
			}
			text = getResources().getString(R.string.command_6b);
			canvas.drawText(text, line2X, line2Y, textPaint);
			if (STATEVIEW == 1) {
				textPaint.setColor(color_01);
			} else {
				textPaint.setColor(color_02);
			}
			text = getResources().getString(R.string.command_6c);
			canvas.drawText(text, line3X, line3Y, textPaint);

		}
	}
	
	private int STATEVIEW = 0;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {	
		case MotionEvent.ACTION_UP:{
			float y = event.getY();
			if(y >= 0 && y <= LINE1){
				STATEVIEW = 2;
			}else if(y >= LINE1 && y <= LINE2){
				STATEVIEW = 0;
			}else if(y >= LINE2 && y <= heightLayout){
				STATEVIEW = 1;
			}
			if(listener != null){
				listener.OnPercentListener(STATEVIEW);
			}
			invalidate();
		}break;
		default:	break;
		}
		return true;		
		
	}
	
	public void setStateView(int state){
		this.STATEVIEW = state;
		invalidate();
	}
	
}
