package vn.com.sonca.sk90xxHidden;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;

public class ViewAddBut extends View {
	
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	
	public ViewAddBut(Context context) {
		super(context);
		initView(context);
	}

	public ViewAddBut(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public ViewAddBut(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Drawable drawAC;
	private Drawable drawIN;
	private Drawable drawXam;
	private void initView(Context context) {
		drawIN = context.getResources().getDrawable(R.drawable.boder_cokhong_inactive_129x74);
		drawAC = context.getResources().getDrawable(R.drawable.boder_cokhong_active_129x74);
		drawXam = context.getResources().getDrawable(R.drawable.boder_cokhong_xam_129x74);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int height = (int) (0.9*MeasureSpec.getSize(heightMeasureSpec));
		int width = 140*height/74;
		setMeasuredDimension(width, height);
	}
	
	private int textS, textX, textY;
	private Rect rectImage = new Rect();
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		int tamX = (int) (0.5*w);
		int tamY = (int) (0.5*h);
		int hh = (int) (0.45*h);
		int ww = 129*hh/74;
		rectImage.set(tamX-ww, tamY-hh, tamX+ww, tamY+hh);

		textS = (int) (0.2*h);
		textX = (int) (0.5*w);
		textY = (int) (0.47*h + 0.5*textS);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if(MyApplication.listHiddenSK90xx != null && MyApplication.listHiddenSK90xx.size() == 0){
			drawXam.setBounds(rectImage);
			drawXam.draw(canvas);
		} else {
			if(isClick == false){
				drawIN.setBounds(rectImage);
				drawIN.draw(canvas);
			}else{
				drawAC.setBounds(rectImage);
				drawAC.draw(canvas);
			}	
		}		
		textPaint.setStyle(Style.FILL);
		textPaint.setTextSize(textS);
		textPaint.setTextAlign(Align.CENTER);
		textPaint.setColor(Color.parseColor("#B4FEFF"));		
		if(MyApplication.listHiddenSK90xx != null && MyApplication.listHiddenSK90xx.size() == 0){
			textPaint.setColor(Color.GRAY);
		}
		canvas.drawText(title, textX, textY, textPaint);
		
	}
	
	private OnClickListener listener;
	@Override
	public void setOnClickListener(OnClickListener l) {
		// super.setOnClickListener(l);
		listener = l;
	}
	
	private boolean isClick = false;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(MyApplication.listHiddenSK90xx != null && MyApplication.listHiddenSK90xx.size() == 0){
			return true;
		}
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isClick = true;
			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			isClick = false;
			invalidate();
			if(listener != null){
				listener.onClick(this);
			}
			break;
		default:
			break;
		}
		return true;
	}
	
	private String title = "";
	public void setTitle(String title){
		this.title = title;
		invalidate();
	}

}
