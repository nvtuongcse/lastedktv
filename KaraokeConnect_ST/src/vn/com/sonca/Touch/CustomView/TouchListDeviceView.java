package vn.com.sonca.Touch.CustomView;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.zktv.main.KTVMainActivity;
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
import android.os.AsyncTask;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TouchListDeviceView extends View {

	private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private int widthLayout = 0;
    private int heightLayout = 0;
    
    public static final int NOTHING = 0;
    public static final int REFRESH = 1;
    public static final int ADDDEV = 2;
    public static final int SETUP = 3;
    public static final int CHANGE = 4;
    public static final int OFFUSER = 5;
    public static final int BLOCK = 6;
    public static final int LANGUAGE = 7;
    public static final int SLEEP = 8;
    public static final int SELECTLIST = 9;
    public static final int USBTOC = 10;
    public static final int SETTINGALL = 11;
    private int state = NOTHING;
    
    private boolean isEmptyList = false;

	public void setEmptyList(boolean isEmptyList) {
		this.isEmptyList = isEmptyList;
		invalidate();
	}
    
	public TouchListDeviceView(Context context) {
		super(context);
		initView(context);
	}

	public TouchListDeviceView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchListDeviceView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private OnListDeviceView listener;
	public interface OnListDeviceView{
		public void OnRefresh();
		public void OnAddDevice();
		public void OnSetupWifi();
		public void OnBackLayout();
		public void OnChangeHello();
		public void OnChangeFlagOffUser();
		public void OnBlockCommand();
		public void OnShowLanguage();
		public void OnSettingSleep();
		public void OnSelectList();
		public void OnUSBTOC();
		public void OnSettingAll();
	}
	

	public void setOnListDeviceViewListener(OnListDeviceView listener){
		this.listener = listener;
	} 
	
	private Drawable drawAC;
	private Drawable drawIN;
	private Drawable drawNO;
	private Drawable drawAddAC;
	private Drawable drawAddHO;
	private Drawable drawRefreshAC;
	private Drawable drawRefreshHO;
	private Drawable drawWifi;
	private Drawable drawBack;
	private Drawable drawChange;
	private Drawable drawChangeXam;
	private Drawable drawBlock;
	private Drawable drawBlockXam;
	private Drawable drawSelectList;
	private Drawable drawSelectListXam;
	private Drawable drawLanguege;
	private Drawable drawSleep;
	private Drawable drawOffUserOn, drawOffUserOff, drawOffUserXam, drawOffUserOn_mp4, drawOffUserOff_mp4, drawOffUserXam1, drawOffUserXam2;
	private Drawable drawUSB, drawUSBXam;
	private Drawable drawUSBIN;
	private Drawable drawSettingAll, drawSettingAllXam;	
	
	private String textLabel = "";
	private void initView(Context context) {
		drawUSB = getResources().getDrawable(R.drawable.icon_quanlibaihat);
		drawUSBIN = getResources().getDrawable(R.drawable.icon_quanlibaihat_xam);
		drawIN = getResources().getDrawable(R.drawable.boder_lammoi);
		drawAC = getResources().getDrawable(R.drawable.boder_lammoi_hover);
		drawAddAC = getResources().getDrawable(R.drawable.them_daumay_157x65);
		drawAddHO = getResources().getDrawable(R.drawable.zgreen_them_daumay_xam_157x65);
		drawRefreshAC = getResources().getDrawable(R.drawable.touch_dotim_daumay_157x65);
		drawRefreshHO = getResources().getDrawable(R.drawable.zgreen_dotim_daumay_xam_157x65);
		drawWifi = getResources().getDrawable(R.drawable.touch_caidat_wifi_157x65);
		drawBack = getResources().getDrawable(R.drawable.connect_back_48x48);
		textLabel = getResources().getString(R.string.connect_0);
		drawBack = getResources().getDrawable(R.drawable.connect_back_48x48);
		drawChange = getResources().getDrawable(R.drawable.touch_doicauchao);
		drawChangeXam = getResources().getDrawable(R.drawable.zgreen_touch_doicauchao_xam);
		drawNO = getResources().getDrawable(R.drawable.touch_boder_xam);
		drawOffUserOn = getResources().getDrawable(R.drawable.touch_bat_user);
		drawOffUserOff = getResources().getDrawable(R.drawable.touch_tat_user);
		drawOffUserXam = getResources().getDrawable(R.drawable.touch_battat_user);
		drawBlock = getResources().getDrawable(R.drawable.touch_icon_khoadieukhien);
		drawBlockXam = getResources().getDrawable(R.drawable.touch_icon_khoadieukhien_xam);
		drawSelectList = getResources().getDrawable(R.drawable.chonlistnhac_active);
		drawSelectListXam = getResources().getDrawable(R.drawable.chonlistnhac_xam);
		drawLanguege = getResources().getDrawable(R.drawable.touch_icon_chonngonngu);
		drawSleep = getResources().getDrawable(R.drawable.icon_tatmanhinh);
		
		drawOffUserOn_mp4 = getResources().getDrawable(R.drawable.bat_user_mp4);
		drawOffUserOff_mp4 = getResources().getDrawable(R.drawable.tat_user_2);
		drawOffUserXam1 = getResources().getDrawable(R.drawable.bat_user_xam);
		drawOffUserXam2 = getResources().getDrawable(R.drawable.tat_user_xam_2);
		
		drawSettingAll = getResources().getDrawable(R.drawable.settingall);
		drawSettingAllXam = getResources().getDrawable(R.drawable.settingall_xam);
	}

	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    super.onMeasure(widthMeasureSpec , heightMeasureSpec);
		int myWidth = 700*heightMeasureSpec/1180;
	    setMeasuredDimension(myWidth, heightMeasureSpec);
	}
	
	private Rect rectLabel;
	private Rect rectBut1;
	private Rect rectBut2;
	private Rect rectBut3;
	private Rect rectBut4;
	private Rect rectBut5;
	private Rect rectBut6;
	private Rect rectBut7;
	private Rect rectBut8;
	private Rect rectBut9;
	private Rect rectBut10;
	private Rect rectBut11;
	private Rect rectBack;
	private float graWidth;
	private float graTopY;
	private float graBottomY;
	private LinearGradient gradient;
	private LinearGradient gradientLight;
	
	private float labelX, labelY , labelS;
	private float KT4, KT4Y;
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		widthLayout = w;
		heightLayout = h;

		rectLabel = new Rect(0, 0, w, (int) (0.09 * h));
		graWidth = (float) (0.005 * h);
		graTopY = graWidth / 2;
		graBottomY = (int) (0.09 * h - graWidth / 2);
		gradient = new LinearGradient(0, 0, w / 2, 0, Color.TRANSPARENT,
				Color.CYAN, Shader.TileMode.MIRROR);
		gradientLight = new LinearGradient(0, 0, w / 2, 0, Color.WHITE,
				Color.parseColor("#FFF200"), Shader.TileMode.MIRROR);
		
		int wr = (int) (0.16 * w);
		int hr = (int) (65 * wr / 157);
		int tamY = (int) (0.14 * h);
		int tamX = (int) (0.175 * w);
		rectBut1 = new Rect(tamX - wr, tamY - hr, tamX + wr, tamY + hr);
		tamX = (int) (0.5 * w);
		rectBut2 = new Rect(tamX - wr, tamY - hr, tamX + wr, tamY + hr);
		tamX = (int) (0.825 * w);
		rectBut3 = new Rect(tamX - wr, tamY - hr, tamX + wr, tamY + hr);
		
		// cau chao + off user
		tamY = (int) (0.94 * h);
		tamX = (int) (0.175 * w);
		rectBut4 = new Rect(tamX - wr, tamY - hr, tamX + wr, tamY + hr);
		tamX = (int) (0.5 * w);
		rectBut5 = new Rect(tamX - wr, tamY - hr, tamX + wr, tamY + hr);
		tamX = (int) (0.825 * w);
		rectBut6 = new Rect(tamX - wr, tamY - hr, tamX + wr, tamY + hr);
		tamY = (int) (0.85 * h);
		tamX = (int) (0.175 * w);
		rectBut7 = new Rect(tamX - wr, tamY - hr, tamX + wr, tamY + hr);
		tamX = (int) (0.5 * w);
		rectBut8 = new Rect(tamX - wr, tamY - hr, tamX + wr, tamY + hr);
		tamX = (int) (0.825 * w);
		rectBut9 = new Rect(tamX - wr, tamY - hr, tamX + wr, tamY + hr);
		
		tamY = (int) (0.76 * h);
		tamX = (int) (0.175 * w);
		rectBut11 = new Rect(tamX - wr, tamY - hr, tamX + wr, tamY + hr);
		rectBut11 = new Rect();
		tamY = (int) (0.76 * h);
		tamX = (int) (0.5 * w);
		rectBut10 = new Rect(tamX - wr, tamY - hr, tamX + wr, tamY + hr);
				
		labelS = (float) (0.045 * h);
		textPaint.setTextSize(labelS);
		float size = textPaint.measureText(textLabel);
		labelX = w / 2 - size / 2;
		labelY = (float) (0.058 * h);

		KT4 = 20 * h / 800;
		KT4Y = 230 * h / 800;
		
		tamX = (int) (0.06 * w);
		tamY = (int) (0.046 * h);
		wr = (int) (0.08 * w);
		hr = wr;
		rectBack = new Rect(tamX - wr, tamY - hr, tamX + wr, tamY + hr);
		
	}

	private int color_01;
	private int color_02;
	private int color_03;
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		boolean flagQuanLyBai = false;
		if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK || 
				(MyApplication.intSvrModel == MyApplication.SONCA && MyApplication.intSvrCode >= MyApplication.newAPI90xxVersion)){
			flagQuanLyBai = true;
		}
		
		if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			flagQuanLyBai = false;
		}
		
		if(flagQuanLyBai == false){
//			rectBut10 = new Rect(0,0,0,0);				
		}
		
		isconnect = TouchMainActivity.serverStatus != null || KTVMainActivity.serverStatus != null;
		// MyApplication.intColorScreen = MyApplication.SCREEN_LIGHT;

		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			color_01 = Color.argb(180, 36, 110, 160);
			color_02 = Color.argb(255, 180, 254, 255);
			color_03 = Color.GREEN;
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			color_01 = Color.parseColor("#21BAA9");
			color_02 = Color.parseColor("#FFFFFF");
			color_03 = Color.GREEN;
		}

		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {

			mainPaint.setShader(null);

			mainPaint.setStyle(Style.FILL);
			mainPaint.setColor(color_01);
			canvas.drawRect(rectLabel, mainPaint);

			drawBack.setBounds(rectBack);
			drawBack.draw(canvas);

			textPaint.setStyle(Style.FILL);
			textPaint.setTextSize(labelS);
			textPaint.setColor(color_02);
			canvas.drawText(textLabel, labelX, labelY, textPaint);

			drawBack.setBounds(rectBack);
			drawBack.draw(canvas);

			mainPaint.setStyle(Style.STROKE);
			mainPaint.setColor(color_03);
			/*
			 * canvas.drawRect(rectBut1, mainPaint); canvas.drawRect(rectBut2,
			 * mainPaint); canvas.drawRect(rectBut3, mainPaint);
			 * canvas.drawRect(rectBut4, mainPaint);
			 */

			if (iswifi) {
				drawRefreshAC.setBounds(rectBut1);
				drawRefreshAC.draw(canvas);
			} else {
				drawRefreshHO.setBounds(rectBut1);
				drawRefreshHO.draw(canvas);
			}
			if (iswifi) {
				if (state == REFRESH) {
					drawAC.setBounds(rectBut1);
					drawAC.draw(canvas);
				} else {
					drawIN.setBounds(rectBut1);
					drawIN.draw(canvas);
				}
			} else {
				drawNO.setBounds(rectBut1);
				drawNO.draw(canvas);
			}

			if (iswifi) {
				drawAddAC.setBounds(rectBut2);
				drawAddAC.draw(canvas);
			} else {
				drawAddHO.setBounds(rectBut2);
				drawAddHO.draw(canvas);
			}
			if (iswifi) {
				if (state == ADDDEV) {
					drawAC.setBounds(rectBut2);
					drawAC.draw(canvas);
				} else {
					drawIN.setBounds(rectBut2);
					drawIN.draw(canvas);
				}
			} else {
				drawNO.setBounds(rectBut2);
				drawNO.draw(canvas);
			}

			drawWifi.setBounds(rectBut3);
			drawWifi.draw(canvas);
			if (state == SETUP) {
				drawAC.setBounds(rectBut3);
				drawAC.draw(canvas);
			} else {
				drawIN.setBounds(rectBut3);
				drawIN.draw(canvas);
			}

			drawLanguege.setBounds(rectBut7);
			drawLanguege.draw(canvas);
			if (state == LANGUAGE) {
				drawAC.setBounds(rectBut7);
				drawAC.draw(canvas);
			} else {
				drawIN.setBounds(rectBut7);
				drawIN.draw(canvas);
			}
						
			drawSleep.setBounds(rectBut8);
			drawSleep.draw(canvas);
			if (state == SLEEP) {
				drawAC.setBounds(rectBut8);
				drawAC.draw(canvas);
			} else {
				drawIN.setBounds(rectBut8);
				drawIN.draw(canvas);
			}
			
			if (isconnect && MyApplication.intWifiRemote == MyApplication.SONCA_KARTROL) {
				drawSelectList.setBounds(rectBut9);
				drawSelectList.draw(canvas);
			} else {
				drawSelectListXam.setBounds(rectBut9);
				drawSelectListXam.draw(canvas);
			}
			if (isconnect && MyApplication.intWifiRemote == MyApplication.SONCA_KARTROL) {
				if (state == SELECTLIST) {
					drawAC.setBounds(rectBut9);
					drawAC.draw(canvas);
				} else {
					drawIN.setBounds(rectBut9);
					drawIN.draw(canvas);
				}
			} else {
				drawNO.setBounds(rectBut9);
				drawNO.draw(canvas);
			}

			if (isconnect && MyApplication.intWifiRemote != MyApplication.SONCA_KARTROL) {
				drawChange.setBounds(rectBut4);
				drawChange.draw(canvas);
			} else {
				drawChangeXam.setBounds(rectBut4);
				drawChangeXam.draw(canvas);
			}
			
			if (isconnect && MyApplication.intWifiRemote != MyApplication.SONCA_KARTROL) {
				if (state == CHANGE) {
					drawAC.setBounds(rectBut4);
					drawAC.draw(canvas);
				} else {
					drawIN.setBounds(rectBut4);
					drawIN.draw(canvas);
				}
			} else {
				drawNO.setBounds(rectBut4);
				drawNO.draw(canvas);
			}

			if (isconnect && MyApplication.intWifiRemote != MyApplication.SONCA_KARTROL) {
				if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && (MyApplication.flagSmartK_801 || MyApplication.flagSmartK_CB)){
					if(MyApplication.bOffUserList){
						drawOffUserXam1.setBounds(rectBut5);
						drawOffUserXam1.draw(canvas);	
					} else {
						drawOffUserXam2.setBounds(rectBut5);
						drawOffUserXam2.draw(canvas);
					}	
				} else {
					if(MyApplication.bOffUserList){
						drawOffUserOn_mp4.setBounds(rectBut5);
						drawOffUserOn_mp4.draw(canvas);	
					} else {
						drawOffUserOff_mp4.setBounds(rectBut5);
						drawOffUserOff_mp4.draw(canvas);
					}	
				}	
			} else {
				if(MyApplication.bOffUserList){
					drawOffUserXam1.setBounds(rectBut5);
					drawOffUserXam1.draw(canvas);	
				} else {
					drawOffUserXam2.setBounds(rectBut5);
					drawOffUserXam2.draw(canvas);
				}	
			}
			if (isconnect && MyApplication.intWifiRemote != MyApplication.SONCA_KARTROL) {
				if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && (MyApplication.flagSmartK_801 || MyApplication.flagSmartK_CB)){
					drawNO.setBounds(rectBut5);
					drawNO.draw(canvas);
				} else {
					if (state == OFFUSER) {
						drawAC.setBounds(rectBut5);
						drawAC.draw(canvas);
					} else {
						drawIN.setBounds(rectBut5);
						drawIN.draw(canvas);
					}	
				}
			} else {
				drawNO.setBounds(rectBut5);
				drawNO.draw(canvas);
			}

			if (isconnect && (MyApplication.intSvrModel == MyApplication.SONCA || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK)) {
				if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && (MyApplication.flagSmartK_801 || MyApplication.flagSmartK_CB)){
					drawBlockXam.setBounds(rectBut6);
					drawBlockXam.draw(canvas);
				} else {
					drawBlock.setBounds(rectBut6);
					drawBlock.draw(canvas);	
				}	
			} else {
				drawBlockXam.setBounds(rectBut6);
				drawBlockXam.draw(canvas);
			}
			if (isconnect && (MyApplication.intSvrModel == MyApplication.SONCA || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK)) {
				if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && (MyApplication.flagSmartK_801 || MyApplication.flagSmartK_CB)){
					drawNO.setBounds(rectBut6);
					drawNO.draw(canvas);
				} else {
					if (state == BLOCK) {
						drawAC.setBounds(rectBut6);
						drawAC.draw(canvas);
					} else {
						drawIN.setBounds(rectBut6);
						drawIN.draw(canvas);
					}						
				}
			} else {
				drawNO.setBounds(rectBut6);
				drawNO.draw(canvas);
			}
			
			if (isconnect == true && 
					((MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && !MyApplication.flagPlayingYouTube)
					|| (MyApplication.intSvrModel == MyApplication.SONCA && MyApplication.intSvrCode >= MyApplication.newAPI90xxVersion))) {
				if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && (MyApplication.flagSmartK_801 || MyApplication.flagSmartK_CB)){
					drawNO.setBounds(rectBut10);
					drawNO.draw(canvas);
				} else {
					if (state == USBTOC) {
						drawAC.setBounds(rectBut10);
						drawAC.draw(canvas);
					} else {
						drawIN.setBounds(rectBut10);
						drawIN.draw(canvas);
					}
				}
			} else {
				drawNO.setBounds(rectBut10);
				drawNO.draw(canvas);
			}
			
			if (isconnect == true && 
					((MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && !MyApplication.flagPlayingYouTube)
					|| (MyApplication.intSvrModel == MyApplication.SONCA && MyApplication.intSvrCode >= MyApplication.newAPI90xxVersion))) {
				if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && (MyApplication.flagSmartK_801 || MyApplication.flagSmartK_CB)){
					drawUSBIN.setBounds(rectBut10);
					drawUSBIN.draw(canvas);
				} else {
					drawUSB.setBounds(rectBut10);
					drawUSB.draw(canvas);						
				}
			}else{
				drawUSBIN.setBounds(rectBut10);
				drawUSBIN.draw(canvas);
			}
			
			if (isconnect && MyApplication.intWifiRemote != MyApplication.SONCA_KARTROL) {
				if (state == SETTINGALL) {
					drawAC.setBounds(rectBut11);
					drawAC.draw(canvas);
				} else {
					drawIN.setBounds(rectBut11);
					drawIN.draw(canvas);
				}
			} else {
				drawNO.setBounds(rectBut11);
				drawNO.draw(canvas);
			}
			
			if (isconnect && MyApplication.intWifiRemote != MyApplication.SONCA_KARTROL) {
				drawSettingAll.setBounds(rectBut11);
				drawSettingAll.draw(canvas);
			}else{
				drawSettingAllXam.setBounds(rectBut11);
				drawSettingAllXam.draw(canvas);
			}

			mainPaint.setShader(gradient);
			mainPaint.setStrokeWidth(graWidth);
			canvas.drawLine(0, graTopY, widthLayout, graTopY, mainPaint);
			canvas.drawLine(0, graBottomY, widthLayout, graBottomY, mainPaint);

			if (isEmptyList) {
				// ---------TEXT EMPTY---------//
				String textEmpty = getResources().getString(R.string.connect_7);
				mainPaint.setShader(null);
				mainPaint.setStyle(Style.FILL);
				mainPaint.setTextSize(KT4);
				mainPaint.setColor(Color.WHITE);
				float tw = mainPaint.measureText(textEmpty);
				canvas.drawText(textEmpty, widthLayout / 2 - tw / 2, KT4Y,
						mainPaint);
			}

		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {

		}
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:{
			float x = event.getX();
			float y = event.getY();
			
			if(rectLabel != null){
				if(y>=0 && y<=rectLabel.bottom &&
					x>=0 && x<=0.15*widthLayout){
					if (listener != null) {
						listener.OnBackLayout();
					}
				}
			}
			
			if(rectBut1 != null){
				if(x>=rectBut1.left && x<=rectBut1.right &&
					y>=rectBut1.top && y<=rectBut1.bottom){
					state = REFRESH;
				}
			}
			if(rectBut2 != null){
				if(x>=rectBut2.left && x<=rectBut2.right &&
					y>=rectBut2.top && y<=rectBut2.bottom){
					state = ADDDEV;
				}
			}
			if(rectBut3 != null){
				if(x>=rectBut3.left && x<=rectBut3.right &&
					y>=rectBut3.top && y<=rectBut3.bottom){
					state = SETUP;	
				}
			}
			if(rectBut4 != null){
				if(x>=rectBut4.left && x<=rectBut4.right &&
					y>=rectBut4.top && y<=rectBut4.bottom){
					state = CHANGE;	
				}
			}
			if(rectBut5 != null){
				if(x>=rectBut5.left && x<=rectBut5.right &&
					y>=rectBut5.top && y<=rectBut5.bottom){
					state = OFFUSER;	
				}
			}
			if (rectBut6 != null) {
				if (x >= rectBut6.left && x <= rectBut6.right
						&& y >= rectBut6.top && y <= rectBut6.bottom) {
					state = BLOCK;	
				}
			}
			if (rectBut7 != null) {
				if (x >= rectBut7.left && x <= rectBut7.right
						&& y >= rectBut7.top && y <= rectBut7.bottom) {
					state = LANGUAGE;	
				}
			}
			if (rectBut8 != null) {
				if (x >= rectBut8.left && x <= rectBut8.right
						&& y >= rectBut8.top && y <= rectBut8.bottom) {
					state = SLEEP;
				}
			}
			if (rectBut9 != null) {
				if (x >= rectBut9.left && x <= rectBut9.right
						&& y >= rectBut9.top && y <= rectBut9.bottom) {
					state = SELECTLIST;	
				}
			}
			if (rectBut10 != null) {
				if (x >= rectBut10.left && x <= rectBut10.right
					&& y >= rectBut10.top && y <= rectBut10.bottom) {
					state = USBTOC;
				}
			}
			if (rectBut11 != null) {
				if (x >= rectBut11.left && x <= rectBut11.right
					&& y >= rectBut11.top && y <= rectBut11.bottom) {
					state = SETTINGALL;
				}
			}
			invalidate();
		}break;
		case MotionEvent.ACTION_UP:{
			float x = event.getX();
			float y = event.getY();
			if(rectBut1 != null){
				if(x>=rectBut1.left && x<=rectBut1.right &&
					y>=rectBut1.top && y<=rectBut1.bottom){
					if(listener != null && iswifi){
						listener.OnRefresh();
					}
				}
			}
			if(rectBut2 != null){
				if(x>=rectBut2.left && x<=rectBut2.right &&
					y>=rectBut2.top && y<=rectBut2.bottom){
					if(listener != null && iswifi){
						listener.OnAddDevice();
					}	
				}
			}
			if(rectBut3 != null){
				if(x>=rectBut3.left && x<=rectBut3.right &&
					y>=rectBut3.top && y<=rectBut3.bottom){
					if(listener != null){
						listener.OnSetupWifi();
					}		
				}
			}
			if(rectBut4 != null){
				if(x>=rectBut4.left && x<=rectBut4.right &&
					y>=rectBut4.top && y<=rectBut4.bottom){
					if(listener != null && 
						MyApplication.intWifiRemote == MyApplication.SONCA){
						listener.OnChangeHello();
					}	
				}
			}
			if (rectBut5 != null) {
				if (x >= rectBut5.left && x <= rectBut5.right
						&& y >= rectBut5.top && y <= rectBut5.bottom) {
					if (listener != null
							&& MyApplication.intWifiRemote == MyApplication.SONCA) {
						if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && (MyApplication.flagSmartK_801 || MyApplication.flagSmartK_CB)){
							
						} else {
							listener.OnChangeFlagOffUser();	
						}	
					}
				}
			}
			if (rectBut6 != null) {
				if (x >= rectBut6.left && x <= rectBut6.right
						&& y >= rectBut6.top && y <= rectBut6.bottom) {
					if (listener != null
							&& MyApplication.intWifiRemote == MyApplication.SONCA) {
						if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && (MyApplication.flagSmartK_801 || MyApplication.flagSmartK_CB)){
							
						} else {
							listener.OnBlockCommand();	
						}
					}
				}
			}
			if (rectBut7 != null) {
				if (x >= rectBut7.left && x <= rectBut7.right
						&& y >= rectBut7.top && y <= rectBut7.bottom) {
					if (listener != null) {
						listener.OnShowLanguage();
					}
				}
			}
			if (rectBut8 != null) {
				if (x >= rectBut8.left && x <= rectBut8.right
						&& y >= rectBut8.top && y <= rectBut8.bottom) {
					if (listener != null) {
						listener.OnSettingSleep();
					}
				}
			}
			if (rectBut9 != null) {
				if (x >= rectBut9.left && x <= rectBut9.right
						&& y >= rectBut9.top && y <= rectBut9.bottom) {
					if (listener != null) {
						listener.OnSelectList();
					}
				}
			}
			if (rectBut10 != null) {
				if (x >= rectBut10.left && x <= rectBut10.right
						&& y >= rectBut10.top && y <= rectBut10.bottom) {
					if (isconnect == true && 
							((MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && !MyApplication.flagPlayingYouTube)
							|| (MyApplication.intSvrModel == MyApplication.SONCA && MyApplication.intSvrCode >= MyApplication.newAPI90xxVersion))) {
						if (listener != null) {
							if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && (MyApplication.flagSmartK_801 || MyApplication.flagSmartK_CB)){
								
							} else {
								listener.OnUSBTOC();								
							}
						}	
					}
					
				}
			}
			if (rectBut11 != null && isconnect == true && 
					MyApplication.intWifiRemote == MyApplication.SONCA) {
					if (x >= rectBut11.left && x <= rectBut11.right
							&& y >= rectBut11.top && y <= rectBut11.bottom) {
						if (listener != null) {
							listener.OnSettingAll();
						}	
						
					}
				}
			state = NOTHING;
			invalidate();
		}break;
		default : break;
		case MotionEvent.ACTION_MOVE:
			if (delay500 != null) {
				delay500.cancel(true);
				delay500 = null;
			}
			delay500 = new Delay500();
			delay500.execute();
			break;
		}
		return true;
	}
	
	private Delay500 delay500 = null;
	class Delay500 extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			long m = System.currentTimeMillis();
			while ((System.currentTimeMillis() - m) < 500) {}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			state = NOTHING;
			invalidate();
		}
	}

	private boolean isconnect = false;
	public void setHelloEnable(boolean isconnect){
		this.isconnect = isconnect;
		invalidate();
	}
	
	private boolean iswifi = true;
	public void setWifiEnable(boolean iswifi){
		this.iswifi = iswifi;
		invalidate();
	}
	
}
