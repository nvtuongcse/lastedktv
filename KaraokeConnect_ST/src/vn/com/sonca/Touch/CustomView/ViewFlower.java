package vn.com.sonca.Touch.CustomView;

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
import android.view.View.MeasureSpec;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.zktv.main.KTVMainActivity;
import vn.com.sonca.zzzzz.MyApplication;

public class ViewFlower extends View {
	
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	private static final String TEXT_COLOR = "#00fdfd";
	
	public ViewFlower(Context context) {
		super(context);
		initView(context);
	}

	public ViewFlower(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public ViewFlower(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private String text = "";
	
	private Drawable drawActive;
	private Drawable drawHover;
	private Drawable drawFlowerHO;
	private Drawable drawFlowerIN;
	private void initView(Context context) {
		drawHover = getResources().getDrawable(R.drawable.touch_ctr_actv_singer_bgrnd);
		drawActive = getResources().getDrawable(R.drawable.touch_image_singer_vongtronngoai_active);
		drawFlowerHO = getResources().getDrawable(R.drawable.mc_flower_on_96x96);
		drawFlowerIN = getResources().getDrawable(R.drawable.mc_flower_off_96x96);
		
		text = getResources().getString(R.string.tuongtac_1);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		if (width > height) {
			setMeasuredDimension(height, height);
		} else {
			setMeasuredDimension(width, width);
		}
	}
	
	private float widthLayout;
	private float heightLayout;
	private float centerX, centerY;
	private float radXamTrong, stroXamTrong;
	private float radXamNgoai, stroXamNgoai;
	private Rect rectImage = new Rect();
	private Rect rectVien = new Rect();
	
	private float textS, textY;
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		widthLayout = w;
		heightLayout = h;
		centerX = w / 2;
		centerY = h / 2;
		int r = 0;
		if (w > h) {
			r = h;
		} else {
			r = w;
		}
		stroXamTrong = (float) (0.07 * r);
		radXamTrong = (float) (0.335 * r);
		stroXamNgoai = (float) (0.03 * r);
		radXamNgoai = (float) (0.45 * r);
		rectVien.set(0, 0, w, h);
		
		int tamX = (int) (0.5*w);
		int tamY = (int) (0.5*h);
		int hh = (int) (0.3*h);
		int ww = hh;
		rectImage.set(tamX-ww, tamY-hh, tamX+ww, tamY+hh);
		
		textS = (float) (0.09 * r);
		textY = (float) (h / 2 + hh - 0.3 * textS);
		
	}
	
	boolean booleanBlockCommand = false;
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		booleanBlockCommand = false;
		
		boolean flag = false;
		if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			if (KTVMainActivity.serverStatus == null) {
				flag = true;
			}
		} else {
			if (TouchMainActivity.serverStatus == null) {
				flag = true;
			}
		}		
		if (flag) {
			booleanBlockCommand = true;
		}
		
		
		if(TouchMainActivity.serverStatus != null || KTVMainActivity.serverStatus != null){
			if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				if(KTVMainActivity.serverStatus.getPlayingSongID() == 0){
					booleanBlockCommand = true;		
				}	
			} else {
				if(TouchMainActivity.serverStatus.getPlayingSongID() == 0){
					booleanBlockCommand = true;		
				}	
			}			
			
		}
		
		
		if(MyApplication.flagPlayingYouTube){
			booleanBlockCommand = true;
		}
		
		if(MyApplication.intSvrModel != MyApplication.SONCA_SMARTK){
			booleanBlockCommand = true;
		}
		
		if (booleanBlockCommand) {
			paintMain.setStyle(Style.STROKE);
			paintMain.setStrokeWidth(stroXamNgoai);
			paintMain.setARGB(51, 255, 255, 255);
			canvas.drawCircle(centerX, centerY, radXamNgoai - stroXamNgoai, paintMain);
			paintMain.setStyle(Style.STROKE);
			paintMain.setStrokeWidth(stroXamTrong);
			paintMain.setARGB(51, 255, 255, 255);
			canvas.drawCircle(centerX, centerY, radXamTrong, paintMain);
			drawFlowerIN.setBounds(rectImage);
			drawFlowerIN.draw(canvas);
			
			textPaint.setStyle(Style.FILL);
			textPaint.setTextSize(textS);
			textPaint.setColor(Color.GRAY);
			float width = textPaint.measureText(text);
			canvas.drawText(text,
					(float) (0.5 * widthLayout - 0.5 * width), textY,
					textPaint);
			
			return;
		}
		
		if(isTouchView == true){
			drawHover.setBounds(rectVien);
			drawHover.draw(canvas);
		}else{
			paintMain.setStyle(Style.STROKE);
			paintMain.setStrokeWidth(stroXamTrong);
			paintMain.setARGB(51, 255, 255, 255);
			canvas.drawCircle(centerX, centerY, radXamTrong, paintMain);
			drawActive.setBounds(rectVien);
			drawActive.draw(canvas);
		}
		drawFlowerHO.setBounds(rectImage);
		drawFlowerHO.draw(canvas);
		
		textPaint.setStyle(Style.FILL);
		textPaint.setTextSize(textS);
		textPaint.setColor(Color.parseColor(TEXT_COLOR));
		float width = textPaint.measureText(text);
		canvas.drawText(text,
				(float) (0.5 * widthLayout - 0.5 * width), textY,
				textPaint);
		
	}
	
	private OnClickListener listener;
	@Override
	public void setOnClickListener(OnClickListener l) {
		super.setOnClickListener(l);
		listener = l;
	}
	
	private boolean isTouchView;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(booleanBlockCommand){
			return true;
		}
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isTouchView = true;
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			isTouchView = false;
			invalidate();
			if(listener != null){
				if (TouchMainActivity.serverStatus != null || KTVMainActivity.serverStatus != null) {
					listener.onClick(this);
				}
			}
			break;
		default:
			break;
		}
		return true;
	}
	

}
