package vn.com.sonca.Touch.CustomView;

import vn.com.hanhphuc.karremote.R;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TopModelView extends View {
	
	private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	
	public TopModelView(Context context) {
		super(context);
		initView(context);
	}

	public TopModelView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TopModelView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private OnTopModelListener listener;
	public interface OnTopModelListener {
		public void OnBackLayout();
	}
	
	public void setOnTopModelListener(OnTopModelListener listener) {
		this.listener = listener;
	}
	
	private Drawable drawDevice;
	private Drawable drawBack;
	private String nameDevice = "SK9038";
	private String label = "";
	private void initView(Context context) {
		drawDevice = getResources().getDrawable(R.drawable.image_daumay_daketnoi_71x65);
		drawBack = getResources().getDrawable(R.drawable.connect_back_48x48);
		label = getResources().getString(R.string.top_model_0);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int height = (int) (0.09*getResources().getDisplayMetrics().heightPixels);
		setMeasuredDimension(widthMeasureSpec, height);
	}
	
	private float graWidth;
	private float graTopY;
	private float graBottomY;
	private int widthLayout;
	private int heightLayout;
	private float nameS, nameX, nameY;
	private float labelS, labelX, labelY;
	private Rect rectDevide = new Rect();
	private Rect rectLabel = new Rect();
	private Rect rectBack = new Rect();
	private LinearGradient gradient;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		widthLayout = w;
		heightLayout = h;
		
		nameS = (float) (0.2*h);
		nameX = (float) (0.98*w);
		nameY = (float) (0.85*h);
		
		labelS = (float) (0.4*h);
		labelX = (float) (0.12*w);
		labelY = (float) (0.5*h + 0.3*labelS);
		
		rectLabel.set(0, 0, w, h);
		
		int tx = (int) (0.06 * w);
		int ty = (int) (0.5 * h);
		int wr = (int) (0.08 * w);
		int hr = wr;
		rectBack = new Rect(tx - wr, ty - hr, tx + wr, ty + hr);
		
		tx = (int) (0.93 * w);
		ty = (int) (0.38 * h);
		hr = (int) (0.25 * h);
		wr = hr;
		rectDevide = new Rect(tx - wr, ty - hr, tx + wr, ty + hr);
		
		rectLabel = new Rect(0, 0, w, h);
		
		graWidth = (float) (0.005*getResources().getDisplayMetrics().heightPixels);
		graTopY = graWidth / 2;
		graBottomY = (int) (h - graWidth / 2);
		gradient = new LinearGradient(0, 0, w / 2, 0, Color.TRANSPARENT,
				Color.CYAN, Shader.TileMode.MIRROR);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		mainPaint.setStyle(Style.FILL);
		mainPaint.setARGB(180, 36, 110, 160);
		canvas.drawRect(rectLabel, mainPaint);
		
		drawBack.setBounds(rectBack);
		drawBack.draw(canvas);
		
		drawDevice.setBounds(rectDevide);
		drawDevice.draw(canvas);
		
		
		textPaint.setStyle(Style.FILL);
		textPaint.setColor(Color.GREEN);
		textPaint.setTextSize(nameS);
		float left = textPaint.measureText(nameDevice);
		canvas.drawText(nameDevice, nameX - left, nameY, textPaint);
		textPaint.setARGB(255, 180, 254, 255);
		textPaint.setTextSize(labelS);
		canvas.drawText(label, labelX, labelY, textPaint);
		
		mainPaint.setShader(gradient);
		mainPaint.setStrokeWidth(graWidth);
		canvas.drawLine(0, graTopY, widthLayout, graTopY, mainPaint);
		canvas.drawLine(0, graBottomY, widthLayout, graBottomY, mainPaint);
		mainPaint.setShader(null);
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_UP){
			float offsetX = event.getX();
			float offsetY = event.getY();
			if (offsetY >= rectBack.top && offsetY <= rectBack.bottom) {
				if (offsetX >= rectBack.left && offsetX <= rectBack.right){
					if (listener != null) {
						listener.OnBackLayout();
					}	
				}
			}
		}
		return true;
	}

}
