package vn.com.sonca.Touch.CustomView;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.zzzzz.MyApplication;
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

public class TouchTopModelView extends View {
	
	private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	
	public TouchTopModelView(Context context) {
		super(context);
		initView(context);
	}

	public TouchTopModelView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchTopModelView(Context context, AttributeSet attrs, int defStyle) {
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
	
	private Drawable zlightdrawDevice;
	private Drawable zlightdrawBack;
	
	private String nameDevice = "SK9038";
	private String label = "";
	private void initView(Context context) {
		drawDevice = getResources().getDrawable(R.drawable.touch_icon_model_active);
		drawBack = getResources().getDrawable(R.drawable.connect_back_48x48);
		
		zlightdrawDevice = getResources().getDrawable(R.drawable.zlight_image_daumay_inactive_71x65);
		zlightdrawBack = getResources().getDrawable(R.drawable.zlight_connect_back_48x48);
		
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
	
	private int color_01;
	private int color_02;
	private int color_03;
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// MyApplication.intColorScreen = MyApplication.SCREEN_LIGHT;
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			color_01 = Color.argb(180, 36, 110, 160);
			color_02 = Color.argb(255, 180, 254, 255);
			color_03 = Color.GREEN;
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			color_01 = Color.parseColor("#21BAA9");
			color_02 = Color.parseColor("#FFFFFF");
			color_03 = Color.parseColor("#FFFFFF");
		}

		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			mainPaint.setStyle(Style.FILL);
			mainPaint.setColor(color_01);
			canvas.drawRect(rectLabel, mainPaint);

			drawBack.setBounds(rectBack);
			drawBack.draw(canvas);

			if(MyApplication.intSvrModel == MyApplication.SONCA_HIW){
				drawDevice = getResources().getDrawable(R.drawable.icon_ktvwwifi_connect);
			} else if(MyApplication.intSvrModel == MyApplication.SONCA_KARTROL){
				drawDevice = getResources().getDrawable(R.drawable.touch_icon_model_active);
			} else if(MyApplication.intSvrModel == MyApplication.SONCA_KM1){
				drawDevice = getResources().getDrawable(R.drawable.icon_model_km1_connect);
			} else if(MyApplication.intSvrModel == MyApplication.SONCA_KM2){
				drawDevice = getResources().getDrawable(R.drawable.icon_model_km2_connect);
			} else if(MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM){
				drawDevice = getResources().getDrawable(R.drawable.icon_kb_oem_active);
			} else if(MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI){
				drawDevice = getResources().getDrawable(R.drawable.icon_km1wifi_active);
			} else if(MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI){
				drawDevice = getResources().getDrawable(R.drawable.icon_kb39c_active);
			} else if(MyApplication.intSvrModel == MyApplication.SONCA_KBX9){
				drawDevice = getResources().getDrawable(R.drawable.daumay_kb_active);
			} else if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
				drawDevice = getResources().getDrawable(R.drawable.daumay_9108);
				if(MyApplication.flagSmartK_CB){
					drawDevice = getResources().getDrawable(R.drawable.cloudbox_active);
				}
				if(MyApplication.flagSmartK_801){
					drawDevice = getResources().getDrawable(R.drawable.sb801_active);
				}
				if(MyApplication.flagSmartK_KM4){
					drawDevice = getResources().getDrawable(R.drawable.km4_active);
				}
				drawDevice.setBounds(rectDevide);
				drawDevice.draw(canvas);
				
				drawDevice = getResources().getDrawable(R.drawable.wifi_9108_4);
			} else if(MyApplication.intSvrModel == MyApplication.SONCA_TBT){
				drawDevice = getResources().getDrawable(R.drawable.daumay_tbt);
				drawDevice.setBounds(rectDevide);
				drawDevice.draw(canvas);
				
				drawDevice = getResources().getDrawable(R.drawable.ktvwifi_4);
			}
			drawDevice.setBounds(rectDevide);
			drawDevice.draw(canvas);

			textPaint.setStyle(Style.FILL);
			textPaint.setColor(color_03);
			textPaint.setTextSize(nameS);
			float left = textPaint.measureText(nameDevice);
			canvas.drawText(nameDevice, nameX - left, nameY, textPaint);
			textPaint.setColor(color_02);
			textPaint.setTextSize(labelS);
			canvas.drawText(label, labelX, labelY, textPaint);

		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {

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
	
	public void setNameDevice(String name){
		nameDevice = name;
		invalidate();
	}

}
