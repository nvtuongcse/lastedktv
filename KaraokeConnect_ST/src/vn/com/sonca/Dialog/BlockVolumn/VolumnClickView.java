package vn.com.sonca.Dialog.BlockVolumn;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.zzzzz.MyApplication;

public class VolumnClickView extends View {
	
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public VolumnClickView(Context context) {
		super(context);
		initView(context);
	}

	public VolumnClickView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public VolumnClickView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private OnVolumnClickViewListener listener;
	public interface OnVolumnClickViewListener {
		public void OnVolumnClick(int value);
	}
	
	public void setOnVolumnClickViewListener(OnVolumnClickViewListener listener){
		this.listener = listener;
	}

	private Drawable drawActive;
	private Drawable drawInActive;
	private Drawable drawHover;
	private void initView(Context context) {
		
		paintMain.setStyle(Style.FILL);
		paintMain.setColor(Color.parseColor("#008aff"));
		textPaint.setStyle(Style.FILL);
		textPaint.setTypeface(Typeface.DEFAULT_BOLD);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
	}
	
	private int widthView;
	private int heightView;
	private int textLeftX, textRightX;
	private int textSS, textS, textX, textY;
	private Rect rectLeft = new Rect();
	private Rect rectRight = new Rect();
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		widthView = w;
		heightView = h;
		
		textS = (int) (0.5*h);
		textX = (int) (0.45*w);
		textY = (int) (0.425*h + 0.5*textS);
		textSS = (int) (0.4*h);
		
		int hh = (int) (0.4*h);
		int ww = (int) (3.5*hh);
		int tamY = (int) (0.5*h);
		int left = 0;
		int right = ww;
		rectLeft.set(left, tamY - hh, right, tamY + hh);
		rectRight.set(w - ww, tamY - hh, w, tamY + hh);
		
		textLeftX = (int) (0.5*ww);
		textRightX = (int) (w - 0.5*ww);
		
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			drawInActive = getResources().getDrawable(R.drawable.boder_xam_hiw);
			drawActive = getResources().getDrawable(R.drawable.boder_volumn_block_congtru_active);
			drawHover = getResources().getDrawable(R.drawable.boder_volumn_block_congtru_hover);
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			drawInActive = getResources().getDrawable(R.drawable.zlight_boder_volumn_block_congtru_active);
			drawActive = getResources().getDrawable(R.drawable.zlight_boder_volumn_block_congtru_active);
			drawHover = getResources().getDrawable(R.drawable.zlight_boder_volumn_block_congtru_hover);
		}
		
	}
	
	private int color_01 = 0;
	private int color_02 = 0;
	private int color_03 = 0;
	private int valueView = 0;
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			color_01 = Color.parseColor("#8800fdfd");
			color_02 = Color.parseColor("#00fdfd");
			color_03 = Color.parseColor("#00fdfd");
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			color_01 = Color.parseColor("#88ffffff");
			color_02 = Color.argb(255, 33, 186, 169);
			color_03 = Color.parseColor("#ffffff");
		}
		
		// stateClickView = 0;
		
		if(stateClickView == 1){
			if(intMasterVolumn + valueView <= 0){
				drawInActive.setBounds(rectLeft);
				drawInActive.draw(canvas);
			}else{
				drawHover.setBounds(rectLeft);
				drawHover.draw(canvas);
			}
			if(intMasterVolumn + valueView >= 16){
				drawInActive.setBounds(rectRight);
				drawInActive.draw(canvas);
			}else{
				drawActive.setBounds(rectRight);
				drawActive.draw(canvas);
			}
		}else if(stateClickView == 2){
			if(intMasterVolumn + valueView <= 0){
				drawInActive.setBounds(rectLeft);
				drawInActive.draw(canvas);
			}else{
				drawActive.setBounds(rectLeft);
				drawActive.draw(canvas);
			}
			if(intMasterVolumn + valueView >= 16){
				drawInActive.setBounds(rectRight);
				drawInActive.draw(canvas);
			}else{
				drawHover.setBounds(rectRight);
				drawHover.draw(canvas);
			}
		}else{
			if(intMasterVolumn + valueView <= 0){
				drawInActive.setBounds(rectLeft);
				drawInActive.draw(canvas);
			}else{
				drawActive.setBounds(rectLeft);
				drawActive.draw(canvas);
			}
			if(intMasterVolumn + valueView >= 16){
				drawInActive.setBounds(rectRight);
				drawInActive.draw(canvas);
			}else{
				drawActive.setBounds(rectRight);
				drawActive.draw(canvas);
			}
		}
		textPaint.setTextSize(textSS);
		textPaint.setTextAlign(Align.RIGHT);
		textPaint.setColor(color_01);
		canvas.drawText(intMasterVolumn + "", textX, textY, textPaint);
		textPaint.setTextSize(textS);
		textPaint.setTextAlign(Align.LEFT);
		textPaint.setColor(color_03);
		if(valueView >= 0){
			if(valueView >= 10){
				textX = (int) (0.45*widthView);
			} else {
				textX = (int) (0.47*widthView);
			}
			canvas.drawText("+" + valueView, textX, textY, textPaint);
		}else{
			if(valueView <= -10){
				textX = (int) (0.45*widthView);
			} else {
				textX = (int) (0.47*widthView);
			}
			canvas.drawText("" + valueView, textX, textY, textPaint);
		}
		textPaint.setTextAlign(Align.CENTER);
		if(intMasterVolumn + valueView <= 0){
			textPaint.setColor(Color.GRAY);
			canvas.drawText("-", textLeftX, textY, textPaint);
		}else{
			textPaint.setColor(color_02);
			canvas.drawText("-", textLeftX, textY, textPaint);
		}
		if(intMasterVolumn + valueView >= 16){
			textPaint.setColor(Color.GRAY);
			canvas.drawText("+", textRightX, textY, textPaint);
		}else{
			textPaint.setColor(color_02);
			canvas.drawText("+", textRightX, textY, textPaint);
		}
	}
	
	private int stateClickView = 0;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
		case MotionEvent.ACTION_MOVE:{
			float x = event.getX();
			float y = event.getY();
			
			if(x >= 0 && x <= rectRight.left && y >= 0 && y <= heightView){
				stateClickView = 1;
			}else if(x >= rectLeft.left && x <= widthView && y >= 0 && y <= heightView){
				stateClickView = 2;
			}else{
				stateClickView = 0;
			}invalidate();
			
		}break;
		
		case MotionEvent.ACTION_UP: {
			if(stateClickView == 1){
				valueView -= 1;
				if(valueView <= (-intMasterVolumn)){
					valueView = -intMasterVolumn; 
				}
			}else if(stateClickView == 2){
				valueView += 1;
				if(valueView >= (16-intMasterVolumn)){
					valueView = (16-intMasterVolumn); 
				}
			}else{}
			stateClickView = 0;
			invalidate();
			if(listener != null){
				listener.OnVolumnClick(valueView);
			}
		}break;
			
		default:{
			stateClickView = 0;
			invalidate();
			}break;
		}
		return true;
	}
	
//////////////////////////////////////////
	
	public void setValueVolumn(int value){
		valueView = value;
		invalidate();
	}
	
	private int intMasterVolumn = 0;
	public void setMasterVolumn(int master){
		intMasterVolumn = master;
		valueView = 0;
		invalidate();
	}

}
