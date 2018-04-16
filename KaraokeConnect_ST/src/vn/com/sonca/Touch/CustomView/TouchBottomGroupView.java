package vn.com.sonca.Touch.CustomView;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class TouchBottomGroupView extends View {
	
	private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Drawable drawable;
	
	public TouchBottomGroupView(Context context) {
		super(context);
		initView(context);
	}

	public TouchBottomGroupView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchBottomGroupView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private void initView(Context context) {}
/*	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	    int myHeight = (int) (0.05*getResources().getDisplayMetrics().heightPixels);
	    setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(myHeight, MeasureSpec.UNSPECIFIED));
	}
*/	
    @Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		setK(w,h);
	}
    
    @Override
    protected void onDraw(Canvas canvas) {
    	super.onDraw(canvas);
    	
		 if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){   	
		    	drawable = getResources().getDrawable(R.drawable.touch_guide_vocal_48x48);
		    	drawable.setBounds(KD1L , KDT , KD1R , KDB);
				drawable.draw(canvas);
				
				drawable = getResources().getDrawable(R.drawable.touch_guide_tone_48x48);
		    	drawable.setBounds(KD2L , KDT , KD2R , KDB);
				drawable.draw(canvas);
				
				drawable = getResources().getDrawable(R.drawable.touch_guide_score_48x48);
		    	drawable.setBounds(KD3L , KDT , KD3R , KDB);
				drawable.draw(canvas);
				
				mainPaint.setStyle(Style.FILL);
				mainPaint.setTextSize(TSize);
				mainPaint.setARGB(255 , 182 , 253 , 255);
				canvas.drawText(getResources().getString(R.string.main_bottom_1) , KTX1 , KTY , mainPaint);
				canvas.drawText(getResources().getString(R.string.main_bottom_2) , KTX2 , KTY , mainPaint);
				canvas.drawText(getResources().getString(R.string.main_bottom_3) , KTX3 , KTY , mainPaint);
		 
		 } else if (MyApplication.intColorScreen == MyApplication.SCREEN_GREEN){
			 	
			 	drawable = getResources().getDrawable(R.drawable.zgreen_guide_vocal_48x48);
		    	drawable.setBounds(KD1L , KDT , KD1R , KDB);
				drawable.draw(canvas);
				
				drawable = getResources().getDrawable(R.drawable.zgreen_guide_tone_48x48);
		    	drawable.setBounds(KD2L , KDT , KD2R , KDB);
				drawable.draw(canvas);
				
				drawable = getResources().getDrawable(R.drawable.zgreen_guide_score_48x48);
		    	drawable.setBounds(KD3L , KDT , KD3R , KDB);
				drawable.draw(canvas);
				
				mainPaint.setStyle(Style.FILL);
				mainPaint.setTextSize(TSize);
				mainPaint.setARGB(255 , 255 , 255 , 255);
				canvas.drawText(getResources().getString(R.string.main_bottom_1) , KTX1 , KTY , mainPaint);
				canvas.drawText(getResources().getString(R.string.main_bottom_2) , KTX2 , KTY , mainPaint);
				canvas.drawText(getResources().getString(R.string.main_bottom_3) , KTX3 , KTY , mainPaint);
		 
		 }
    }
    
    private int TSize  , KD , KDT , KDB;
    private float KTY , KTX1 , KTX2, KTX3, KTX4;
    private int KD1L , KD1R ,KD2L , KD2R , KD3L , KD3R , KD4L , KD4R;
    private void setK(int w , int h){ 
    	TSize = h/2;
    	KTY = (float) (h/2 + TSize/2.5);
    	KD = (int) (0.8*h);
   	
    	KDT = (int) (h/2 - KD/2.3);
    	KDB = (int) (h/2 + KD/1.7);
 
    	KD1L = 25*w/1920;
    	KD1R = KD1L + KD;
    	
    	KD2L = 650*w/1920;
    	KD2R = KD2L + KD;
    	
    	KD3L = 1270*w/1920;
    	KD3R = KD3L + KD;
    	
    	KD4L = 1150*w/1920;
    	KD4R = KD4L + KD;
    	/*
    	KTX1 = 100*w/1920; 
    	KTX2 = 480*w/1920; 
    	KTX3 = 860*w/1920; 
    	KTX4 = 1230*w/1920; 
    	*/
    	
    	KTX1 = 100*w/1920; 
    	KTX2 = 710*w/1920; 
    	KTX3 = 1330*w/1920; 
    	// KTX4 = 1230*w/1920; 
    }

}
