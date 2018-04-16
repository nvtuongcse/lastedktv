package vn.com.sonca.Touch.BlockCommand;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.zzzzz.MyApplication;

public class ButtonControl extends View {
	
	private TextPaint textPaint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
	
	public ButtonControl(Context context) {
		super(context);
		initView(context);
	}

	public ButtonControl(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public ButtonControl(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Drawable drawAC;
	private Drawable drawHO;
	
	private Drawable zlightdrawAC;
	private Drawable zlightdrawHO;
	
	private String textTitle = "KHONG";
	private void initView(Context context){
		drawAC = getResources().getDrawable(R.drawable.boder_cokhong_inactive_129x74);
		drawHO = getResources().getDrawable(R.drawable.boder_cokhong_active_129x74);
		
		zlightdrawAC = getResources().getDrawable(R.drawable.zlight_boder_cokhong_inactive_129x74);
		zlightdrawHO = getResources().getDrawable(R.drawable.zlight_boder_cokhong_hover_129x74);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int width = 129*height/74;
		setMeasuredDimension(width , height);
	}
	
	private int widthLayout;
	private int textS, textX, textY;
	private Rect rectBackgroup = new Rect();
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		rectBackgroup.set(0, 0, w, h);
		textS = (int) (0.35*h);
		textY = (int) (0.5*h + 0.5*textS);
		widthLayout = w;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			if (isClick) {
				drawHO.setBounds(rectBackgroup);
				drawHO.draw(canvas);
			} else {
				drawAC.setBounds(rectBackgroup);
				drawAC.draw(canvas);
			}			
			textPaint.setStyle(Style.FILL);
			textPaint.setTextSize(textS);
			textPaint.setARGB(255, 180, 254, 255);
			textX = (int) (widthLayout/2 - textPaint.measureText(textTitle)/2);
			canvas.drawText(textTitle, textX, textY, textPaint);
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			if (isClick) {
				zlightdrawHO.setBounds(rectBackgroup);
				zlightdrawHO.draw(canvas);
			} else {
				zlightdrawAC.setBounds(rectBackgroup);
				zlightdrawAC.draw(canvas);
			}
			textPaint.setStyle(Style.FILL);
			textPaint.setTextSize(textS);
			textPaint.setColor(Color.parseColor("#005249"));
			textX = (int) (widthLayout/2 - textPaint.measureText(textTitle)/2);
			canvas.drawText(textTitle, textX, textY, textPaint);
		}
		
	}
	
	private boolean isClick = false;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
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
		default: break;
		}
		return true;
	}
	
	private OnClickListener listener; 
	@Override
	public void setOnClickListener(OnClickListener l) {
		super.setOnClickListener(l);
		listener = l;
	}
	
	public void setTextTitle(String title){
		textTitle = title;
		invalidate();
	}
	

}
