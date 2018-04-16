package vn.com.sonca.Touch.CustomView;

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
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.zzzzz.MyApplication;

public class LanguageTopModelView extends View {
	
	private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	
	public LanguageTopModelView(Context context) {
		super(context);
		initView(context);
	}

	public LanguageTopModelView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public LanguageTopModelView(Context context, AttributeSet attrs, int defStyle) {
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
	
	private Drawable drawBack;
	private Drawable zlightdrawBack;
	private String label = "";
	private void initView(Context context) {
		drawBack = getResources().getDrawable(R.drawable.connect_back_48x48);
		label = getResources().getString(R.string.top_lang);
		
		zlightdrawBack = getResources().getDrawable(R.drawable.zlight_connect_back_48x48);		
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
	private float labelS, labelX, labelY;
	private Rect rectDevide = new Rect();
	private Rect rectLabel = new Rect();
	private Rect rectBack = new Rect();
	private LinearGradient gradient;
	private LinearGradient gradientLight;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		widthLayout = w;
		heightLayout = h;
		
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
		gradientLight = new LinearGradient(0, 0, w / 2, 0, Color.WHITE,
				Color.parseColor("#FFF200"), Shader.TileMode.MIRROR);
		
	}
	
	private int color_01;
	private int color_02;
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		// MyApplication.intColorScreen = MyApplication.SCREEN_LIGHT;
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			color_01 = Color.argb(180, 36, 110, 160);
			color_02 = Color.argb(255, 180, 254, 255);
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			color_01 = Color.parseColor("#21BAA9");
			color_02 = Color.parseColor("#FFFFFF");
		}

		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			mainPaint.setStyle(Style.FILL);
			mainPaint.setColor(color_01);
			canvas.drawRect(rectLabel, mainPaint);

			drawBack.setBounds(rectBack);
			drawBack.draw(canvas);

			textPaint.setARGB(255, 180, 254, 255);
			textPaint.setColor(color_02);
			textPaint.setTextSize(labelS);
			labelX = (float) (0.5 * widthLayout - 0.5 * textPaint
					.measureText(label));
			canvas.drawText(label, labelX, labelY, textPaint);

			mainPaint.setShader(gradient);
			mainPaint.setStrokeWidth(graWidth);
			canvas.drawLine(0, graTopY, widthLayout, graTopY, mainPaint);
			canvas.drawLine(0, graBottomY, widthLayout, graBottomY, mainPaint);
			mainPaint.setShader(null);
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			mainPaint.setStyle(Style.FILL);
			mainPaint.setColor(color_01);
			canvas.drawRect(rectLabel, mainPaint);

			zlightdrawBack.setBounds(rectBack);
			zlightdrawBack.draw(canvas);

			textPaint.setARGB(255, 180, 254, 255);
			textPaint.setColor(color_02);
			textPaint.setTextSize(labelS);
			labelX = (float) (0.5 * widthLayout - 0.5 * textPaint
					.measureText(label));
			canvas.drawText(label, labelX, labelY, textPaint);

			mainPaint.setShader(gradientLight);
			mainPaint.setStrokeWidth(graWidth);
			canvas.drawLine(0, graTopY, widthLayout, graTopY, mainPaint);
			canvas.drawLine(0, graBottomY, widthLayout, graBottomY, mainPaint);
			mainPaint.setShader(null);
		}
		
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
	
	//---------------------//
	

}
