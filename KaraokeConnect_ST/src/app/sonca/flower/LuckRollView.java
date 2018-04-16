package app.sonca.flower;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;

public class LuckRollView extends View {

	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	
	private float widthLayout;
	private float heightLayout;
	
	private Context context;

	public LuckRollView(Context context) {
		super(context);
		initView(context);
	}

	public LuckRollView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public LuckRollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private ArrayList<Float> Items;
	private RectF rectfNac = new RectF();
	
	private void initView(Context context) {
		this.context = context;
		
		Items = new ArrayList<Float>();
		Items.add((float) -15);
		Items.add((float) 15);
		Items.add((float) 45);
		Items.add((float) 75);
		Items.add((float) 105);
		Items.add((float) 135);		
		Items.add((float) 165);
		Items.add((float) 195);
		Items.add((float) 225);
		Items.add((float) 255);
		Items.add((float) 285);
		Items.add((float) 315);		
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}
	
	private float centerX, centerY;
	private float radius, radius2;
	private Rect rectLucky_main;
	
	private float STROKE_1, textS;

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		widthLayout = getWidth();
		heightLayout = getHeight();
		
		centerX = widthLayout / 2;
		centerY = heightLayout / 2;
		
		STROKE_1 = widthLayout / 100;
		
		textS = widthLayout / 30;
		
		radius = (float) (0.48*widthLayout);
		radius2 = (float) (0.4*widthLayout);
		
		rectfNac.set(centerX- radius/2, centerY - radius/2, centerX + radius/2, centerY + radius/2);
		rectLucky_main = new Rect((int)(centerX - radius2/2), (int)(centerY - radius2/2), 
				(int)(centerX + radius2/2), (int)(centerY + radius2/2));
				
		
		int check = currentLucky;
		if(flagRunning){
			check = countRun;
		}
		
		resetPaint();
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(radius);
		for (int i = 0; i < Items.size(); i++) {
			mPaint.setColor(pickColorFromIndex(i));
			if(check == (i + 1)){
				mPaint.setAlpha(255);	
			} else {
				mPaint.setAlpha(50);
			}			
			canvas.drawArc(rectfNac, Items.get(i), (float) 30, false, mPaint);
		}
		
		resetPaint();
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeWidth(STROKE_1);
		mPaint.setARGB(255, 0, 0, 0);
		canvas.save();
		for (int i = 0; i < Items.size() * 2; i++) {
			canvas.rotate(15,centerX,centerY);
			if(i % 2 == 0){
				canvas.drawLine(centerX,centerY,centerX + radius,centerY,mPaint);	
			}
		}
		canvas.restore();
		
//		resetPaint();
//		textPaint.setStyle(Style.FILL);
//		textPaint.setARGB(255, 255, 255, 255);
//		textPaint.setTextSize(textS);
//		canvas.save();
//		for (int i = 0; i < Items.size() * 2; i++) {
//			canvas.rotate(30,centerX,centerY);
//			canvas.drawText(listLucky.get(currentLucky - 1), rectLucky_main.right, centerY, textPaint);
//		}
//		canvas.restore();
		
		drawLucky_main = getResources().getDrawable(R.drawable.lucky_main);
		if(bmpImage != null){
			bmpImage.setBounds(rectLucky_main);
			bmpImage.draw(canvas);
		} else {
			if(drawLucky_main != null){
				drawLucky_main.setBounds(rectLucky_main);
				drawLucky_main.draw(canvas);
			}
		}	
		
		resetPaint();
		mPaint.setStyle(Style.STROKE);
		mPaint.setStrokeWidth(STROKE_1);
		mPaint.setARGB(255, 0, 0, 0);
		canvas.drawCircle(centerX, centerY, radius2 / 2, mPaint);
		canvas.drawCircle(centerX, centerY, radius, mPaint);
	}
	
	private int pickColorFromIndex(int idx){
		int color = Color.argb(0, 0, 0, 0);
		switch (idx) {
		case 0:
			color = Color.argb(255, 254, 254, 51);
			break;
		case 1:
			color = Color.argb(255, 208, 234, 43);
			break;
		case 2:
			color = Color.argb(255, 102, 176, 50);
			break;
		case 3:
			color = Color.argb(255, 3, 145, 206);
			break;
		case 4:
			color = Color.argb(255, 2, 71, 254);
			break;
		case 5:
			color = Color.argb(255, 61, 1, 164);
			break;
		case 6:
			color = Color.argb(255, 134, 1, 175);
			break;
		case 7:
			color = Color.argb(255, 165, 25, 75);
			break;
		case 8:
			color = Color.argb(255, 254, 39, 18);
			break;
		case 9:
			color = Color.argb(255, 253, 83, 8);
			break;
		case 10:
			color = Color.argb(255, 251, 153, 2);
			break;	
		case 11:
			color = Color.argb(255, 250, 188, 2);
			break;
		default:
			Color.argb(0, 0, 0, 0);
			break;
		}
		return color;
	}

	private void resetPaint() {
		mPaint.reset();
		mPaint.setAntiAlias(true);
		mPaint.setShader(null);
	}	

	private Drawable drawLucky_main;
	public void setLuckyMainImage(Drawable drawLucky_main){
		this.drawLucky_main = drawLucky_main;
		invalidate();
	}
	
	private int currentLucky = 0;
	public void setCurrentLuck(int currentLucky){
		stopTimerLuckyRoll();
		this.currentLucky = currentLucky;
		invalidate();
	}
	
	public int getCurrentLuck(){
		return this.currentLucky;
	}
	
	private ArrayList<String> listLucky = new ArrayList<String>();
	public void setLuckyData(ArrayList<String> listLucky){
		this.listLucky = listLucky;
		invalidate();
	}
	
	private int countRun = 0;
	private boolean flagRunning = false;
	public void startTimerLuckyRoll(){
		stopTimerLuckyRoll();
		flagRunning = true;
		countRun = currentLucky;
		
		timerLuckyRoll = new Timer();
		timerLuckyRoll.schedule(new LuckyRollTask(), 500, 100);
	}
	
	public void stopTimerLuckyRoll(){
		flagRunning = false;
		if (timerLuckyRoll != null) {
			timerLuckyRoll.cancel();
			timerLuckyRoll = null;
		}
	}
	
	private Timer timerLuckyRoll;
	private class LuckyRollTask extends TimerTask {	
				
		@Override		
		public void run() {	    	 
	    	 countRun++;			    	 
	    	 if(countRun > 12){
	    		 countRun = 1;
	    	 }	
	    	 handlerLuckyRoll.sendEmptyMessage(0);			
		}
	}
	
	private Handler handlerLuckyRoll = new Handler(){
		public void handleMessage(android.os.Message msg) {
			invalidate();
		};
	};
	
	private Drawable bmpImage;
	public void setLogoImage(Drawable bmpImage){
		this.bmpImage = bmpImage;
		invalidate();
	}
	
}