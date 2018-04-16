package vn.com.sonca.Touch.BlockCommand;

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
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.Lyric.LyricBack.OnBackLyric;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.zktv.main.KTVMainActivity;
import vn.com.sonca.zzzzz.MainActivity;
import vn.com.sonca.zzzzz.MyApplication;

public class CommandBack extends View {
	
	private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint mainText = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public CommandBack(Context context) {
		super(context);
		initView(context);
	}

	public CommandBack(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public CommandBack(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private OnBackLyric listener;
	public interface OnBackLyric{
		public void OnBack();
		public void OnPhongHatClick();
		public void OnMacDinhClick();
	}
	
	public void setOnBackLyric(OnBackLyric listener){
		this.listener = listener;
	}
	
	private Drawable drawable;
	private Drawable drawDevice;
	private Drawable drawableTemp;
	private Drawable drawAC;
	private Drawable drawHO;
	private Drawable drawXam;
	
	private Drawable zlightdrawable;
	private Drawable zlightdrawDevice;
	private Drawable zlightdrawableTemp;
	private Drawable zlightdrawAC;
	private Drawable zlightdrawHO;
	private Drawable zlightdrawXam;
	
	private String titleButton = "";
	private String namedevice = "SK9018KTV-W";
	private String CHEDO = "";
	private String PHONGHAT = "";
	private String MACDINH = "";
	private void initView(Context context) {
		titleButton = getResources().getString(R.string.command_0);
		drawable = getResources().getDrawable(R.drawable.touch_tab_exit_active_144x72);
		drawDevice = getResources().getDrawable(R.drawable.image_daumay_chuaketnoi_71x65); 
		
		drawAC = getResources().getDrawable(R.drawable.touch_boder_chedo_phonghat_active);
		drawHO = getResources().getDrawable(R.drawable.touch_boder_chedo_phonghat_hover);
		drawXam = getResources().getDrawable(R.drawable.touch_boder_chedo_phonghat_xam);
		
		zlightdrawable = getResources().getDrawable(R.drawable.zlight_back);
		zlightdrawDevice = getResources().getDrawable(R.drawable.zlight_image_daumay_inactive_71x65); 
		zlightdrawAC = getResources().getDrawable(R.drawable.zlight_boder_chedo_phonghat_active);
		zlightdrawHO = getResources().getDrawable(R.drawable.zlight_boder_chedo_phonghat_hover);
		zlightdrawXam = getResources().getDrawable(R.drawable.zlight_boder_chedo_phonghat_xam);
				
		CHEDO = getResources().getString(R.string.command_5);
		PHONGHAT = getResources().getString(R.string.command_5a);
		MACDINH = getResources().getString(R.string.command_5b);
		
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
	
	private float but1aX, but1aY, but1aS;
	private float but1bX, but1bY, but1bS;
	private float but2aX, but2aY, but2aS;
	private float but2bX, but2bY, but2bS;
	
	private Rect rectDrawable = new Rect();
	private Rect rectDevice = new Rect();
	private Rect rectModePH = new Rect();
	private Rect rectModeMD = new Rect();
	private LinearGradient gradient;
	private LinearGradient gradientGreen;
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
		gradientGreen = new LinearGradient(0, 0, w / 2, 0, Color.TRANSPARENT,
				Color.argb(255, 255, 234, 0), Shader.TileMode.MIRROR);
		gradientLIGHT = new LinearGradient(0, 0, w / 2, 0, Color.WHITE,
				Color.parseColor("#FFF200"), Shader.TileMode.MIRROR);
		
		titleS = (int) (0.4*h);
		mainText.setTextSize(titleS);
		titleX = (float) (2.7*h);
		titleY = (float) (0.5*h + titleS/2.5);
		
		int tamX = (int) (50);
		int tamY = (int) (0.5*heightLayout);
		int height = (int) (0.5*heightLayout);
		int width = 72*height/144;
		rectDrawable.set(10 , tamY - height, 10 + 2*width, tamY + height);
		
		tamX = (int) (1.5*heightLayout);
		tamY = (int) (0.4*heightLayout);
		height = (int) (0.3*heightLayout);
		width = 71*height/65;
		rectDevice.set(tamX - width , tamY - height, tamX + width, tamY + height);
		
		tamY = (int) (0.5*heightLayout);
		height = (int) (0.4*heightLayout);
		width = 174*height/74;
		rectModeMD.set(widthLayout - 2*width - 10 , tamY - height, widthLayout - 10, tamY + height);
		
		int wr = widthLayout - 2*width - 15;
		rectModePH.set(wr - 2*width - 10 , tamY - height, wr - 10, tamY + height);
		
		nameS = (int) (0.2*h);
		nameY = (float) (0.9*h);
		
		
		but1aS = but1bS = but2aS = but2bS = (float) (0.3*h);
		mainText.setTextSize(but1aS);
		but1aY = but2aY = (float) (0.6*h - 0.5*but1aS);
		but1bY = but2bY = (float) (0.6*h + 0.5*but1aS);
		but1aX = (rectModePH.left + rectModePH.right)/2 - mainText.measureText(CHEDO)/2;
		but2aX = (rectModeMD.left + rectModeMD.right)/2 - mainText.measureText(CHEDO)/2;
		but1bX = (rectModePH.left + rectModePH.right)/2 - mainText.measureText(PHONGHAT)/2;
		but2bX = (rectModeMD.left + rectModeMD.right)/2 - mainText.measureText(MACDINH)/2;
		
	}
	
	private int color_01;
	private int color_02;
	private int color_03;
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		// MyApplication.intColorScreen = MyApplication.SCREEN_LIGHT;
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			color_01 = Color.argb(255, 7, 40, 10);
			color_02 = Color.argb(255, 180, 254, 255);
			color_03 = Color.GREEN;
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			color_01 = Color.argb(255, 7, 40, 10);
			color_02 = Color.parseColor("#FFFFFF");
			color_03 = Color.parseColor("#FFFFFF");
		}
		
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
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

			flagSameValuePhongHat = false;
			flagSameValueMacDinh = false;
			
			if (TouchMainActivity.serverStatus != null || KTVMainActivity.serverStatus != null) {
				boolean flagControlFullAPI = false;
				
				if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
					if(KTVMainActivity.serverStatus.isOnOffControlFullAPI()){
						flagControlFullAPI = true;
					}
				} else {
					if(TouchMainActivity.serverStatus.isOnOffControlFullAPI()){
						flagControlFullAPI = true;
					}
				}
				
				if (flagControlFullAPI) {
					if (MyApplication.checkModePhongHat()) {
						flagSameValuePhongHat = true;
					}
					if (MyApplication.intCommandMedium == MyApplication.MODE_MACDINH_3NAC) {
						flagSameValueMacDinh = true;
					}
				} else {
					if (MyApplication.intCommandEnable == MyApplication.MODE_MACDINH_2NAC) {
						flagSameValueMacDinh = true;
					}
				}
			}
			
			if (stateclick == intmacdinh) {
				drawableTemp = drawHO;
			} else {
				drawableTemp = drawAC;
			}
			if (flagSameValueMacDinh) {
				drawableTemp = drawXam;
			}
			drawableTemp.setBounds(rectModeMD);
			drawableTemp.draw(canvas);
			// PHONG HAT
			if (TouchMainActivity.serverStatus != null || KTVMainActivity.serverStatus != null) {
				boolean flagControlFullAPI = false;
				
				if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
					if(KTVMainActivity.serverStatus.isOnOffControlFullAPI()){
						flagControlFullAPI = true;
					}
				} else {
					if(TouchMainActivity.serverStatus.isOnOffControlFullAPI()){
						flagControlFullAPI = true;
					}
				}
				
				if (!flagControlFullAPI) {
					drawXam.setBounds(rectModePH);
					drawXam.draw(canvas);
				} else {
					if (stateclick == intphonghat) {
						drawableTemp = drawHO;
					} else {
						drawableTemp = drawAC;
					}
					if (flagSameValuePhongHat) {
						drawableTemp = drawXam;
					}
					drawableTemp.setBounds(rectModePH);
					drawableTemp.draw(canvas);
				}
			}
			
			mainPaint.setShader(gradient);
			mainPaint.setStrokeWidth(graWidth);
			canvas.drawLine(0, graTopY, widthLayout, graTopY, mainPaint);
			canvas.drawLine(0, graBottomY, widthLayout, graBottomY, mainPaint);
			mainPaint.setShader(null);

			mainText.setStyle(Style.FILL);
			mainText.setTextSize(but1aS);
			mainText.setColor(color_01);
			canvas.drawText(CHEDO, but2aX, but2aY, mainText);
			canvas.drawText(MACDINH, but2bX, but2bY, mainText);
			canvas.drawText(CHEDO, but1aX, but1aY, mainText);
			canvas.drawText(PHONGHAT, but1bX, but1bY, mainText);

			mainText.setStyle(Style.FILL);
			mainText.setTextSize(titleS);
			mainText.setColor(color_02);
			String strMode = getResources().getString(R.string.command_5c);
			if (flagSameValuePhongHat) {
				strMode = getResources().getString(R.string.command_5a);
			}
			if (flagSameValueMacDinh) {
				strMode = getResources().getString(R.string.command_5b);
			}
			String strTitle = titleButton + " ( " + strMode + " )";
			canvas.drawText(strTitle, titleX, titleY, mainText);

			mainText.setStyle(Style.FILL);
			mainText.setTextSize(nameS);
			mainText.setColor(color_03);
			nameX = (rectDevice.left + rectDevice.right) / 2
					- mainText.measureText(namedevice) / 2;
			canvas.drawText(namedevice, nameX, nameY, mainText);

		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {

		}
		
	}
	
	private final int intphonghat = 1;
	private final int intmacdinh = 2;
	private boolean flagSameValuePhongHat = false;
	private boolean flagSameValueMacDinh = false;
	private int stateclick = 0;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			float x = event.getX();
			if(rectModePH != null && rectModeMD != null){
				if(x >= rectModePH.left && x <= rectModePH.right){
					stateclick = intphonghat;
					invalidate();
				}else if (x >= rectModeMD.left && x <= rectModeMD.right){
					stateclick = intmacdinh;
					invalidate();
				}
			}
		}else if(event.getAction() == MotionEvent.ACTION_UP){
			float x = event.getX();
			stateclick = 0;
			invalidate();
			if(x >= 0 && x <= 0.1*widthLayout){
				if(listener != null){
					listener.OnBack();
				}
			}if(x >= rectModePH.left && x <= rectModePH.right){
				if(listener != null){
					if(!flagSameValuePhongHat){
						if (TouchMainActivity.serverStatus != null || KTVMainActivity.serverStatus != null) {
							boolean flagControlFullAPI = false;
							
							if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
								if(KTVMainActivity.serverStatus.isOnOffControlFullAPI()){
									flagControlFullAPI = true;
								}
							} else {
								if(TouchMainActivity.serverStatus.isOnOffControlFullAPI()){
									flagControlFullAPI = true;
								}
							}
							
							if(flagControlFullAPI){
								listener.OnPhongHatClick();	
							}
						}
					}				
				}
			}else if (x >= rectModeMD.left && x <= rectModeMD.right){
				if(listener != null){
					if(!flagSameValueMacDinh){
						listener.OnMacDinhClick();	
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
