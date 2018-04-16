package vn.com.sonca.sk90xxOnline;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class GlowViewOnline extends View {
	
	private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintSimple = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintGlow = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public GlowViewOnline(Context context) {
		super(context);
		initView(context);
	}

	public GlowViewOnline(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public GlowViewOnline(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Drawable drawable;
	private Drawable drawCheck;

	private void initView(Context context) {
		drawCheck = getResources().getDrawable(R.drawable.image_check_47x46_111);
		drawable = getResources().getDrawable(R.drawable.image_boder_song);
		paintGlow.setStyle(Style.STROKE);
	    paintGlow.setColor(Color.argb(235, 74, 138, 255));
	    paintGlow.setStrokeWidth(1);
		
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	    int myHeight = (int) (0.6188*getResources().getDisplayMetrics().heightPixels/5);
	    if(MyApplication.flagHong){
	    	myHeight = (int) (0.6188*getResources().getDisplayMetrics().heightPixels/6);
	    }
	    setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(myHeight, MeasureSpec.UNSPECIFIED));
	}
	
	private Rect rectBoder;
	private Rect rectCheck;
	private int widthLayout;
	private int heightLayout;
	private LinearGradient gradient;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		rectBoder = new Rect(0, 0, w, h);
		widthLayout = w;
		heightLayout = h;
			//-------------//
		int hr = (int) (0.35*h);
		int wr = 47*hr/46;
		rectCheck = new Rect(0, 0, wr, hr);
		gradient = new LinearGradient(0, 0, w, 0, 
				Color.argb(255, 0, 42, 80), 
				Color.argb(255, 1, 76, 141), 
				TileMode.CLAMP);
	}
	
	private boolean isActive = false;
	public void setActive(boolean isActive){
		this.isActive = isActive;
		invalidate();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
//		MyApplication.intColorScreen = MyApplication.SCREEN_GREEN;
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			if(isActive){
				paintSimple.setShader(gradient);
				paintSimple.setStrokeWidth(getHeight());
				canvas.drawLine(0, getHeight()/2, getWidth(), getHeight()/2, paintSimple);
	//			drawable.setBounds(rectBoder);
	//			drawable.draw(canvas);
				drawCheck.setBounds(rectCheck);
				drawCheck.draw(canvas);
			}
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			mainPaint.setStyle(Style.FILL);
			if(isActive){
				mainPaint.setColor(Color.parseColor("#FFFFFF"));
			}else{
				mainPaint.setColor(Color.parseColor("#E6E7E8"));
			}
			canvas.drawRect(0, 0, widthLayout, heightLayout, mainPaint);
		}
	}

	
}
