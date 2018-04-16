package vn.com.sonca.zktv.FragData;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import vn.com.hanhphuc.karremote.R;

public class MyTextView extends View {

	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	
	public MyTextView(Context context) {
		super(context);
		initView(context);
	}

	public MyTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public MyTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Drawable drawImage;
	private void initView(Context context) {
		drawImage = context.getResources().getDrawable(R.drawable.ktv_song_boder_search);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	private Rect rectImage = new Rect();
	private int textS, textX1, textX2, textY;
	private int padImage;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		textS = (int) (0.5*h);
		textY = (int) (0.5*h + 0.4*textS);
		textX1 = (int) (0.05*w);
		
		textPaint.setTextSize(textS);
		
		rectImage.set(0, 0, w, h);
		padImage = 4 * w / 450;
		rectImage.set(0 + padImage, 0 + padImage, w - padImage * 4, h - padImage);

	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		drawImage.setBounds(rectImage);
		drawImage.draw(canvas);
		
		float s = textPaint.measureText(title);
		textX2 = (int) (textX1 + s);
		textPaint.setARGB(255, 255, 255, 255);
		canvas.drawText(title, textX1, textY, textPaint);
		textPaint.setARGB(255, 255, 255, 255);
		canvas.drawText(" " + number, textX2, textY, textPaint);
		
	}
	
	private String title = "";
	private String number = "";
	public void setDataNumber(String title, String number){
		this.number = number;
		this.title = title;
		invalidate();
	}

}
