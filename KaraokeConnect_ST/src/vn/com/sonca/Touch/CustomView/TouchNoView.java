package vn.com.sonca.Touch.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Shader;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

public class TouchNoView extends View {
	
	private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int widthLayout = 400;
    private int heightLayout = 400;

	public TouchNoView(Context context) {
		super(context);
		initView(context);
	}

	public TouchNoView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchNoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private void initView(Context context) {
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		setK(getWidth() , getHeight());
		
		mainPaint.setStyle(Style.STROKE);
		mainPaint.setStrokeWidth(KS);
		mainPaint.setARGB(255 , 9 , 109 , 255);
		canvas.drawLine(widthLayout - KS , 0 , widthLayout - KS , heightLayout, mainPaint);
		
		PointF pointFirst = new PointF(widthLayout , 0);
		PointF pointLast = new PointF(widthLayout , heightLayout);
		mainPaint = createHorizontalLines(pointFirst, pointLast);   
		canvas.drawLine(widthLayout - KS , 0 , widthLayout - KS , heightLayout , mainPaint);
		mainPaint.setShader(null);	
		
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		setK(w, h);
		widthLayout = w;
		heightLayout = h;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int myWidth = 12 * heightMeasureSpec / 22;
		setMeasuredDimension(myWidth, heightMeasureSpec);
	}
	
    private Paint createHorizontalLines(PointF pointFirst , PointF pointLast){
    	Paint paint = new Paint();
    	paint.setStyle(Style.FILL);
    	paint.setStrokeWidth(KS);
		LinearGradient gradient = new LinearGradient(
				pointFirst.x , pointFirst.y/2 , 
				pointLast.x , pointLast.y/2 ,
				Color.TRANSPARENT, Color.argb(255 , 2 , 235, 252) , 
				Shader.TileMode.MIRROR);
		paint.setShader(gradient);
		return paint;
    }
	
    private int KS;
    private float K , KT;
	private void setK(int w , int h){
		K = h/22;
		KT = (float) (62*h/1080);
		KS = (int) (6*h/1080);
	}

}
