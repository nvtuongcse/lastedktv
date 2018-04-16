package vn.com.sonca.Touch.CustomView;

import vn.com.hanhphuc.karremote.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TouchRectangleView extends View {
	
	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	//private Context context;
	private Bitmap cornerBitmap;
	private String text;
	
	private int widthLayout = 400;
    private int heightLayout = 400;
    private static final int PADDING = 10;
    
    private boolean isActive = false;
    
    private int centerX, centerY;
    
    private static final int RECTANGLE_STROKE_WIDTH = 2;
    private static final String RECTANGLE_STROKE_COLOR = "#014783";
    private float textScale;
    
    private static final String INACTIVE_TEXT_COLOR = "#91d2d6";
    private static final String ACTIVE_TEXT_COLOR = "#00fdfd";
    private static final String OUTER_TEXT_COLOR = "#0863ce";

	public TouchRectangleView(Context context) {
		super(context);
		initView(context);
	}

	public TouchRectangleView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchRectangleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private void initView(Context context) {
		//this.context = context;
		cornerBitmap = BitmapFactory.decodeResource(getResources(), 
				R.drawable.touch_category_item_corner);
		text = "TẤT CẢ";
	}
	
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		textScale = getWidth() / 100;
		
		if (!isActive)
		{
			resetPaint();
			mPaint.setStyle(Style.STROKE);
			mPaint.setStrokeWidth(RECTANGLE_STROKE_WIDTH);
			mPaint.setColor(Color.parseColor(RECTANGLE_STROKE_COLOR));
			canvas.drawRect(PADDING, PADDING, widthLayout - PADDING, heightLayout - PADDING, mPaint);
			
			resetPaint();
			float textSize = mPaint.getTextSize();
			mPaint.setTextSize((float) (textSize * textScale));
			mPaint.setColor(Color.parseColor(INACTIVE_TEXT_COLOR));
			Rect boundRect = new Rect();
			mPaint.getTextBounds(text, 0, text.length(), boundRect);
			canvas.drawText(text, centerX - boundRect.width() / 2, centerY + boundRect.height() / 2, mPaint);
			mPaint.setTextSize(textSize);
		}
		else
		{
			resetPaint();
			mPaint.setStyle(Style.STROKE);
			mPaint.setStrokeWidth(RECTANGLE_STROKE_WIDTH);
			mPaint.setColor(Color.parseColor(RECTANGLE_STROKE_COLOR));
			canvas.drawRect(PADDING, PADDING, widthLayout - PADDING, heightLayout - PADDING, mPaint);
			
			resetPaint();
			mPaint.setMaskFilter(new BlurMaskFilter(5, Blur.NORMAL));
			canvas.drawBitmap(cornerBitmap, PADDING, PADDING, mPaint);
			
			canvas.save();
			canvas.rotate(90, centerX, centerY);
			canvas.translate((widthLayout - heightLayout) / 2, -(widthLayout - heightLayout) / 2);
			canvas.drawBitmap(cornerBitmap, PADDING, PADDING, mPaint);
			canvas.restore();
			
			canvas.save();
			canvas.rotate(180, centerX, centerY);
			canvas.drawBitmap(cornerBitmap, PADDING, PADDING, mPaint);
			canvas.restore();
			
			canvas.save();
			canvas.rotate(270, centerX, centerY);
			canvas.translate((widthLayout - heightLayout) / 2, -(widthLayout - heightLayout) / 2);
			canvas.drawBitmap(cornerBitmap, PADDING, PADDING, mPaint);
			canvas.restore();
			
			resetPaint();
			mPaint.setTextSize(textScale);
			mPaint.setColor(Color.parseColor(OUTER_TEXT_COLOR));
			Rect boundRect = new Rect();
			mPaint.getTextBounds(text, 0, text.length(), boundRect);
			mPaint.setMaskFilter(new BlurMaskFilter(5, Blur.NORMAL));
			canvas.drawText(text, centerX - boundRect.width() / 2, centerY + boundRect.height() / 2, mPaint);
			mPaint.setMaskFilter(null);
			mPaint.setColor(Color.parseColor(ACTIVE_TEXT_COLOR));
			canvas.drawText(text, centerX - boundRect.width() / 2, centerY + boundRect.height() / 2, mPaint);
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int desiredWidth = 100;
	    int desiredHeight = 100;

	    int widthMode = MeasureSpec.getMode(widthMeasureSpec);
	    int widthSize = MeasureSpec.getSize(widthMeasureSpec);
	    int heightMode = MeasureSpec.getMode(heightMeasureSpec);
	    int heightSize = MeasureSpec.getSize(heightMeasureSpec);

	    int width;
	    int height;

	    //Measure Width
	    if (widthMode == MeasureSpec.EXACTLY) {
	        //Must be this size
	        width = widthSize;
	    } else if (widthMode == MeasureSpec.AT_MOST) {
	        //Can't be bigger than...
	        width = Math.min(desiredWidth, widthSize);
	    } else {
	        //Be whatever you want
	        width = desiredWidth;
	    }

	    //Measure Height
	    if (heightMode == MeasureSpec.EXACTLY) {
	        //Must be this size
	        height = heightSize;
	    } else if (heightMode == MeasureSpec.AT_MOST) {
	        //Can't be bigger than...
	        height = Math.min(desiredHeight, heightSize);
	    } else {
	        //Be whatever you want
	        height = desiredHeight;
	    }

	    //MUST CALL THIS
	    setMeasuredDimension(width, height);
	}
	
    @Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		widthLayout = w;
		heightLayout = h;
		centerX = widthLayout / 2;
		centerY = heightLayout / 2;
	} 
    
    @SuppressLint("ClickableViewAccessibility")
	@Override
    public boolean onTouchEvent(MotionEvent event) {
    	if (Math.abs(event.getX() - centerX) > centerX || Math.abs(event.getY() - centerY) > centerY)
    	{
    		return false;
    	}
    	switch (event.getAction())
    	{
    	case MotionEvent.ACTION_DOWN:
    		isActive = !isActive;
    		invalidate();
    		return true;
    	}
    	
    	return false;
    }

    private void resetPaint()
    {
    	mPaint.reset();
    	mPaint.setAntiAlias(true);
    	mPaint.setShader(null);
    }
}