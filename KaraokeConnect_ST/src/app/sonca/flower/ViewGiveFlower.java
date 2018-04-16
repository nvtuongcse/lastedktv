package app.sonca.flower;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;

public class ViewGiveFlower extends View {
	
	private String TAB = "ViewGiveFlower";
	
	public ViewGiveFlower(Context context) {
		super(context);
		initView(context);
	}

	public ViewGiveFlower(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public ViewGiveFlower(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private OnCheckGiveFlowerListener listener;
	public interface OnCheckGiveFlowerListener{
		public void OnCheck(View view);
	}
	
	public void setOnCheckGiveFlowerListener(OnCheckGiveFlowerListener listener){
		this.listener = listener;
	}
	
	private Drawable drawFlower;
	private Drawable drawCheckAC;
	private Drawable drawCheckIN;
	private void initView(Context context) {
		drawFlower = getResources().getDrawable(R.drawable.kc_pk_hoa);
		drawCheckAC = getResources().getDrawable(R.drawable.kc_check_active);
		drawCheckIN = getResources().getDrawable(R.drawable.kc_check_inactive);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int width = 6*height;
		setMeasuredDimension(width, height);
	}
	
	private Rect rectCheck = new Rect();
	private Rect rectFlower1 = new Rect();
	private Rect rectFlower2 = new Rect();
	private Rect rectFlower3 = new Rect();
	private Rect rectFlower4 = new Rect();
	private Rect rectFlower5 = new Rect();
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		int tamY = (int)(0.5*h);
		int hh = (int) (0.25*h);
		int ww = hh;
		
		int tamX = (int)(0.5*h);
		rectCheck.set(tamX-ww, tamY-hh, tamX+ww, tamY+hh);
		
		hh = (int) (0.4*h);
		ww = hh;
		tamX = (int)(1.5*h);
		rectFlower1.set(tamX-ww, tamY-hh, tamX+ww, tamY+hh);
		tamX = (int)(2.5*h);
		rectFlower2.set(tamX-ww, tamY-hh, tamX+ww, tamY+hh);
		tamX = (int)(3.5*h);
		rectFlower3.set(tamX-ww, tamY-hh, tamX+ww, tamY+hh);
		tamX = (int)(4.5*h);
		rectFlower4.set(tamX-ww, tamY-hh, tamX+ww, tamY+hh);
		tamX = (int)(5.5*h);
		rectFlower5.set(tamX-ww, tamY-hh, tamX+ww, tamY+hh);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if(isActiveView == true){
			drawCheckAC.setBounds(rectCheck);
			drawCheckAC.draw(canvas);
		}else{
			drawCheckIN.setBounds(rectCheck);
			drawCheckIN.draw(canvas);
		}
		
		if(numberFlowers >= 1){
			drawFlower.setBounds(rectFlower1);
			drawFlower.draw(canvas);
		}
		if(numberFlowers >= 2){
			drawFlower.setBounds(rectFlower2);
			drawFlower.draw(canvas);
		}
		if(numberFlowers >= 3){
			drawFlower.setBounds(rectFlower3);
			drawFlower.draw(canvas);
		}
		if(numberFlowers >= 4){
			drawFlower.setBounds(rectFlower4);
			drawFlower.draw(canvas);
		}
		if(numberFlowers >= 5){
			drawFlower.setBounds(rectFlower5);
			drawFlower.draw(canvas);
		}
	}
	
	private int numberFlowers = 5;
	public void setNumberFlowers(int number){
		numberFlowers = number;
		invalidate();
	}
	
	public int getNumberFlowers(){
		return numberFlowers;
	}
	
	private boolean isActiveView = false;
	public void setActiveView(boolean isActiveView){
		this.isActiveView = isActiveView; 
		invalidate();
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			
			break;
		case MotionEvent.ACTION_UP:
			if(rectCheck != null){
				float x = event.getX();
				float y = event.getY();
				if(x >= rectCheck.left && x <= rectCheck.right &&
					y >= rectCheck.top && y <= rectCheck.bottom){
					isActiveView = true;
					invalidate();
					if(listener != null){
						listener.OnCheck(this);
					}
				}
			}
			break;
		default:
			break;
		}
		return true;
	}
	

}
