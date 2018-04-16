package vn.com.sonca.Lyric;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class LyricBack extends View {
	
	private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint mainText = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public LyricBack(Context context) {
		super(context);
		initView(context);
	}

	public LyricBack(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public LyricBack(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private OnBackLyric listener;
	public interface OnBackLyric{
		public void OnBack();
	}
	
	public void setOnBackLyric(OnBackLyric listener){
		this.listener = listener;
	}
	
	public void setTitleView(String strTitle){
		this.nameButton = strTitle;
		invalidate();
	}
	
	private Drawable drawable;
	private Drawable zlightdrawable;
	
	private String nameButton = "";
	private void initView(Context context) {
		nameButton = getResources().getString(R.string.lyric_0);
		drawable = getResources().getDrawable(R.drawable.touch_tab_exit_active_144x72);
		zlightdrawable = getResources().getDrawable(R.drawable.zlight_back);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	private float graWidth;
	private float graTopY;
	private float graBottomY;
	private int widthLayout = 0;
	private int heightLayout = 0;
	private float nameX, nameY, nameS;
	private Rect rectDrawable = new Rect();
	private LinearGradient gradient;
	private LinearGradient gradientZLIGHT;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		widthLayout = w;
		heightLayout = h;
		graWidth = (float) (0.05 * h);
		graTopY = (float) (0.5*graWidth);
		graBottomY = (int) (heightLayout - graWidth / 2);
		gradient = new LinearGradient(0, 0, w / 2, 0, Color.TRANSPARENT,
				Color.CYAN, Shader.TileMode.MIRROR);
		gradientZLIGHT = new LinearGradient(0, 0, w / 2, 0, Color.WHITE,
				Color.argb(255, 255, 242, 0), Shader.TileMode.MIRROR);		
		
		nameS = (int) (0.4*h);
		mainText.setTextSize(nameS);
		nameX = (float)(widthLayout/2 - mainText.measureText(nameButton)/2);
		nameY = (float) (0.5*h + nameS/2.5);
		
		int tamX = (int) (50);
		int tamY = (int) (0.5*heightLayout);
		int height = (int) (0.5*heightLayout);
		int width = 72*height/144;
		rectDrawable.set(tamX - width, tamY - height, tamX + width, tamY + height);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// MyApplication.intColorScreen = MyApplication.SCREEN_LIGHT;
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {

			mainPaint.setShader(gradient);
			mainPaint.setStrokeWidth(graWidth);
			canvas.drawLine(0, graTopY, widthLayout, graTopY, mainPaint);
			canvas.drawLine(0, graBottomY, widthLayout, graBottomY, mainPaint);
			mainPaint.setShader(null);

			mainText.setStyle(Style.FILL);
			mainText.setARGB(255, 180, 254, 255);
			canvas.drawText(nameButton, nameX, nameY, mainText);

			drawable.setBounds(rectDrawable);
			drawable.draw(canvas);

		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {

			mainPaint.setStyle(Style.FILL);
			mainPaint.setColor(Color.parseColor("#21BAA9"));
			canvas.drawRect(0, 0, widthLayout, heightLayout, mainPaint);

			mainPaint.setAlpha(255);
			mainPaint.setShader(gradientZLIGHT);
			mainPaint.setStrokeWidth(graWidth);
			canvas.drawLine(0, graTopY, widthLayout, graTopY, mainPaint);
			canvas.drawLine(0, graBottomY, widthLayout, graBottomY, mainPaint);
			mainPaint.setShader(null);

			mainText.setStyle(Style.FILL);
			mainText.setColor(Color.parseColor("#FFFFFF"));
			canvas.drawText(nameButton, nameX, nameY, mainText);

			zlightdrawable.setBounds(rectDrawable);
			zlightdrawable.draw(canvas);

		}
		
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_UP){
			float x = event.getX();
			if(x >= 0 && x <= 0.1*widthLayout){
				if(listener != null){
					listener.OnBack();
				}
			}
		}
		return true;
	}

}
