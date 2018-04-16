package vn.com.sonca.SetttingApp;

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
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TopSettingView extends View {
	
	private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint mainText = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public TopSettingView(Context context) {
		super(context);
		initView(context);
	}

	public TopSettingView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TopSettingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private OnBackListener listener;
	public interface OnBackListener{
		public void OnBackListener();
	}
	public void setOnBackListener(OnBackListener listener){
		this.listener = listener;
	}
	
	private Drawable drawable;
	private Drawable drawDevice;
	private Drawable zlightdrawable;
	private Drawable zlightdrawDevice;
	private String titleButton = "";
	private String namedevice = "";
	private void initView(Context context) {
		titleButton = getResources().getString(R.string.sw_title);
		drawable = getResources().getDrawable(R.drawable.touch_tab_exit_active_144x72);
		drawDevice = getResources().getDrawable(R.drawable.image_daumay_chuaketnoi_71x65); 
		zlightdrawable = getResources().getDrawable(R.drawable.zlight_back);
		zlightdrawDevice = getResources().getDrawable(R.drawable.zlight_image_daumay_inactive_71x65); 
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
	private float titleX, titleY, titleS;
	private float nameX, nameY, nameS;
	
	private Rect rectDrawable = new Rect();
	private Rect rectDevice = new Rect();
	private LinearGradient gradient;
	private LinearGradient gradientLIGHT;
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
		gradientLIGHT = new LinearGradient(0, 0, w / 2, 0, Color.WHITE,
				Color.argb(255, 255, 242, 0), Shader.TileMode.MIRROR);
		
		titleS = (int) (0.4*h);
		if(MyApplication.flagHong){
			titleS = (int) (0.3*h);
		}
		mainText.setTextSize(titleS);
		titleX = (float) (0.5*w);
		titleY = (float) (0.5*h + titleS/2.5);
		
		int tamX = (int) (50);
		int tamY = (int) (0.5*heightLayout);
		int height = (int) (0.5*heightLayout);
		int width = 72*height/144;
		rectDrawable.set(10 , tamY - height, 10 + 2*width, tamY + height);
		
		tamX = (int) (w - 1.25*heightLayout);
		tamY = (int) (0.4*heightLayout);
		height = (int) (0.3*heightLayout);
		width = 71*height/65;
		rectDevice.set(tamX - width , tamY - height, tamX + width, tamY + height);
		
		nameS = (int) (0.2*h);
		nameY = (float) (0.9*h);
		
	}
	
	private int color_01;
	private int color_02;
	private int color_03;
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		// MyApplication.intColorScreen = MyApplication.SCREEN_LIGHT;
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			color_01 = Color.argb(255, 7, 40, 10);
			color_02 = Color.argb(255, 180, 254, 255);
			color_03 = Color.GREEN;
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			color_01 = Color.argb(255, 7, 40, 10);
			color_02 = Color.parseColor("#FFFFFF");
			color_03 = Color.parseColor("#FFFFFF");
		}
		
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			
			drawable.setBounds(rectDrawable);
			drawable.draw(canvas);

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
				drawDevice.setBounds(rectDevice);
				drawDevice.draw(canvas);
				
				drawDevice = getResources().getDrawable(R.drawable.wifi_9108_4);
			} else if(MyApplication.intSvrModel == MyApplication.SONCA_TBT){
				drawDevice = getResources().getDrawable(R.drawable.daumay_tbt);
				drawDevice.setBounds(rectDevice);
				drawDevice.draw(canvas);
				
				drawDevice = getResources().getDrawable(R.drawable.ktvwifi_4);
			}
			drawDevice.setBounds(rectDevice);
			drawDevice.draw(canvas);			
			
			mainPaint.setShader(gradient);
			mainPaint.setStrokeWidth(graWidth);
			canvas.drawLine(0, graTopY, widthLayout, graTopY, mainPaint);
			canvas.drawLine(0, graBottomY, widthLayout, graBottomY, mainPaint);
			mainPaint.setShader(null);
			
			mainText.setStyle(Style.FILL);
			mainText.setTextSize(titleS);
			mainText.setColor(color_02);
			float xx = (float) (titleX - 0.5*mainText.measureText(titleButton));
			canvas.drawText(titleButton, xx, titleY, mainText);
			
			mainText.setStyle(Style.FILL);
			mainText.setTextSize(nameS);
			mainText.setColor(color_03);
			nameX = (rectDevice.left + rectDevice.right)/2 - mainText.measureText(namedevice)/2;
			canvas.drawText(namedevice, nameX, nameY, mainText);
			
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){

		}
		
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
		}else if(event.getAction() == MotionEvent.ACTION_UP){
			float x = event.getX();
			if(rectDrawable != null){
				if(x >= rectDrawable.left && x <= rectDrawable.right){
					if(listener != null){
						listener.OnBackListener();
					}
				}
			}
		}
		return true;
	}

	
	public void setNameDevice(String namedevice){
		this.namedevice = namedevice;
		invalidate();
	}
	
}
