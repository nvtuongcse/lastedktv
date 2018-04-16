package vn.com.sonca.Touch.CustomView;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.params.SKServer;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Shader.TileMode;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;

public class TouchOneDeviceView extends View {
	
	private Paint paintSimple = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintGlow = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int widthLayout = 0;
    private int heightLayout = 0;
	private SKServer skServer;

	private int stateWifi = SKServer.SAVED;
	private boolean isPassword = true;
	private int modelDevice;
	private String nameRemote;
	
	public TouchOneDeviceView(Context context) {
		super(context);
		initView(context);
	}

	public TouchOneDeviceView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchOneDeviceView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Drawable drawGlow;
	private Drawable drawKetNoi;
	private Drawable drawKetNoiHover;
	
	private Drawable drawSaveSK9;
	private Drawable drawConnectSK9;
	private Drawable drawBroadcastSK9;
	private Drawable drawSaveKar;
	private Drawable drawConnectKar;
	private Drawable drawBroadcastKar;
	private Drawable drawSaveKM1;
	private Drawable drawConnectKM1;
	private Drawable drawBroadcastKM1;
	private Drawable drawSaveKM2;
	private Drawable drawConnectKM2;
	private Drawable drawBroadcastKM2;
	private Drawable drawSaveKTV;
	private Drawable drawConnectKTV;
	private Drawable drawBroadcastKTV;
	
	private Drawable zlightdrawGlow;
	private Drawable zlightdrawKetNoi;
	private Drawable zlightdrawKetNoiHover;
	
	private Drawable zlightdrawSaveSK9;
	private Drawable zlightdrawConnectSK9;
	private Drawable zlightdrawBroadcastSK9;
	
	private Drawable zlightdrawSaveKar;
	private Drawable zlightdrawConnectKar;
	private Drawable zlightdrawBroadcastKar;
	
	private Drawable zlightdrawSaveKM1;
	private Drawable zlightdrawConnectKM1;
	private Drawable zlightdrawBroadcastKM1;
	
	private Drawable zlightdrawSaveKM2;
	private Drawable zlightdrawConnectKM2;
	private Drawable zlightdrawBroadcastKM2;
	
	private Drawable zlightdrawSaveKTV;
	private Drawable zlightdrawConnectKTV;
	private Drawable zlightdrawBroadcastKTV;
	
	private Drawable zlightDrawable;
	
	private String dalua = "";
	private String xoalua = "";
	private String ketnoi = "";
	private String dangketnoi = "";
	private String chuaketnoi = "";
	private String chonModel = "";
	private String dauMay = "";
	private String caidat = "";
	private String daukaraoke = "";
	
	private Drawable tempDrawable;
	
	private void initView(Context context) {
		paintGlow.setStyle(Style.STROKE);
	    paintGlow.setColor(Color.argb(235, 74, 138, 255));
	    paintGlow.setStrokeWidth(1);
	    
		drawGlow = getResources().getDrawable(R.drawable.image_boder_song);
	    drawKetNoi = getResources().getDrawable(R.drawable.boder_ketnoi);
	    drawKetNoiHover = getResources().getDrawable(R.drawable.boder_ketnoi_hover);
	    
	    drawBroadcastSK9 = getResources().getDrawable(R.drawable.image_daumay_chuaketnoi_71x65);
		drawConnectSK9 = getResources().getDrawable(R.drawable.image_daumay_daketnoi_71x65);
		drawSaveSK9 = getResources().getDrawable(R.drawable.image_daumay_daluu_inactive_71x65);
		
		drawBroadcastKar = getResources().getDrawable(R.drawable.touch_icon_model_daluu);
		drawConnectKar = getResources().getDrawable(R.drawable.touch_icon_model_active);
		drawSaveKar = getResources().getDrawable(R.drawable.touch_icon_model_xam);
		
		drawBroadcastKM1 = getResources().getDrawable(R.drawable.icon_model_km1_broadcast);
		drawConnectKM1 = getResources().getDrawable(R.drawable.icon_model_km1_connect);
		drawSaveKM1 = getResources().getDrawable(R.drawable.icon_model_km_inactive);
		
		drawBroadcastKM2 = getResources().getDrawable(R.drawable.icon_model_km2_broadcast);
		drawConnectKM2 = getResources().getDrawable(R.drawable.icon_model_km2_connect);
		drawSaveKM2 = getResources().getDrawable(R.drawable.icon_model_km_inactive);
		
		drawBroadcastKTV = getResources().getDrawable(R.drawable.icon_ktvwwifi_broadcast);
		drawConnectKTV = getResources().getDrawable(R.drawable.icon_ktvwwifi_connect);
		drawSaveKTV = getResources().getDrawable(R.drawable.image_daumay_daluu_inactive_71x65);
		
		//-----------------------//
		
		zlightdrawGlow = getResources().getDrawable(R.drawable.image_boder_song);
		zlightdrawKetNoi = getResources().getDrawable(R.drawable.zlight_boder_ketnoi);
		zlightdrawKetNoiHover = getResources().getDrawable(R.drawable.zlight_boder_ketnoi_hover);
		
		zlightdrawBroadcastSK9 = getResources().getDrawable(R.drawable.zlight_image_daumay_daluu);
		zlightdrawConnectSK9 = getResources().getDrawable(R.drawable.zlight_image_daumay_dangketnoi);
		zlightdrawSaveSK9 = getResources().getDrawable(R.drawable.zlight_image_daumay_offline);
		
		zlightdrawBroadcastKar = getResources().getDrawable(R.drawable.icon_light_wr_daluu);
		zlightdrawConnectKar = getResources().getDrawable(R.drawable.zlight_icon_wr_active);
		zlightdrawSaveKar = getResources().getDrawable(R.drawable.icon_light_wr_xam);
		
		zlightdrawBroadcastKM1 = getResources().getDrawable(R.drawable.icon_km1_daluu);
		zlightdrawConnectKM1 = getResources().getDrawable(R.drawable.icon_km1_active);
		zlightdrawSaveKM1 = getResources().getDrawable(R.drawable.zlight_km_xam);
		
		zlightdrawBroadcastKM2 = getResources().getDrawable(R.drawable.zlight_km2_daluu);
		zlightdrawConnectKM2 = getResources().getDrawable(R.drawable.zlight_km2_dangketnoi);
		zlightdrawSaveKM2 = getResources().getDrawable(R.drawable.zlight_km_xam);
		
		zlightdrawBroadcastKTV = getResources().getDrawable(R.drawable.zlight_image_daumay_daluu_ktvwwifi);
		zlightdrawConnectKTV = getResources().getDrawable(R.drawable.zlight_image_daumay_dangketnoi_ktvwwifi);
		zlightdrawSaveKTV = getResources().getDrawable(R.drawable.image_daumay_daluu_inactive_71x65);
		
		//-----------------------//
		
		dangketnoi = getResources().getString(R.string.one_device_1);
		chuaketnoi = getResources().getString(R.string.one_device_2);
		dalua = getResources().getString(R.string.one_device_3);
		xoalua = getResources().getString(R.string.one_device_5);
		chonModel = getResources().getString(R.string.one_device_6);
		dauMay = getResources().getString(R.string.one_device_7);
		caidat = getResources().getString(R.string.one_device_8);
		daukaraoke = getResources().getString(R.string.one_device_9);
		
		skServer = new SKServer();
		
/*
		
		skServer.setActive(true);
		skServer.setSave(false);
		skServer.setModelDevice(KARTROL);
		skServer.setState(SKServer.SAVED);
		skServer.setName("KTV-Hiw 9000");
		skServer.setNameRemote("SONCA");
		skServer.setConnectedIPStr("000.000.000.000");
		
		modelDevice = skServer.getModelDevice();
		MyApplication.intColorScreen = MyApplication.SCREEN_LIGHT;
*/
	}
	
	private OnOneDeviceViewListener listener;
	public interface OnOneDeviceViewListener {
		public void OnPerformConnect(SKServer skServer);
		public void OnRemoveDevice(SKServer skServer);
		public void OnDisConnect(SKServer skServer);
		public void OnSelectModel(SKServer skServer);
		public void OnShowHi_W(SKServer skServer);
	}
	
	public void setOnOneDeviceViewListener(OnOneDeviceViewListener listener){
		this.listener = listener;
	}
	
	private boolean flagSmartK_801 = false;
	private boolean flagSmartK_KM4 = false;
	private boolean flagSmartK_CB = false;
	
	public void setDevice(SKServer skServer){
		this.skServer = skServer; 
		stateWifi = skServer.getState();
		isPassword = skServer.isSave();
		modelDevice = skServer.getModelDevice();
		flagSmartK_801 = false;
		flagSmartK_KM4 = false;
		flagSmartK_CB = false;
		
		if(modelDevice == MyApplication.SONCA_SMARTK_9108_SYSTEM){
			modelDevice = MyApplication.SONCA_SMARTK;
		}
		
		if(modelDevice == MyApplication.SONCA_SMARTK_KM4_SYSTEM){
			modelDevice = MyApplication.SONCA_SMARTK_KM4;
		}
		
		if(modelDevice == MyApplication.SONCA_SMARTK_CB){
			modelDevice = MyApplication.SONCA_SMARTK;
			flagSmartK_CB = true;
		}
		
		if(modelDevice == MyApplication.SONCA_SMARTK_801){
			modelDevice = MyApplication.SONCA_SMARTK;
			flagSmartK_801 = true;
		}
		if(modelDevice == MyApplication.SONCA_SMARTK_KM4){
			modelDevice = MyApplication.SONCA_SMARTK;
			flagSmartK_KM4 = true;
		}
		nameRemote = skServer.getNameRemote();
		requestLayout();
	}
	
	public SKServer getDevice(){
		return skServer;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec , heightMeasureSpec);
		
		/*
		int myHeight;	
		if(skServer.isActive()){
			myHeight = (int) (0.6188*getResources().getDisplayMetrics().heightPixels/4);
		}else{
			myHeight = (int) (0.6188*getResources().getDisplayMetrics().heightPixels/7);
		}
	    setMeasuredDimension(widthMeasureSpec , myHeight);
	    */
		
		int myWidth = MeasureSpec.getSize(widthMeasureSpec);
		int myHeight = 0;
		if(skServer.isActive()){
			myHeight = (int) (0.32*myWidth);
		}else{
			myHeight = (int) (0.15*myWidth);
		}
		setMeasuredDimension(myWidth , myHeight);
		
	}
	
	private float Line;
	private Rect rectConnect;
	private Path pathGoc = new Path();
	private Path pathKhung = new Path();
	
	private float idX, idY, idS;
	private float nameX, nameY, nameS;
	private float ketnoiX, ketnoiY, ketnoiS;
	private float daluuX, daluuY, daluuS;
	private float xoaluuX;
	private Rect rectBoder;
	private LinearGradient gradient;
	private Rect rectButtonConnect;
	private Rect rectButtonModel;
	
	private int connectX, connectY, connectS;
	private int chonModelX, chonModelY, chonModelS;
	private int daumayX, daumayY, daumayS;
	
	private int daukaraokeX, caidatX;
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		widthLayout = w;
		heightLayout = h;
		rectBoder = new Rect(0, 0, w, h);
		gradient = new LinearGradient(0, 0, w, 0, 
				Color.argb(89, 0, 42, 80), 
				Color.argb(89, 1, 76, 141), 
				TileMode.CLAMP);
		
		float tyle = 2.1f;
		
		if (skServer.isActive()) {
			Line = (float) (150*h/1080/tyle);
		} else {
			Line = 150*h/1080;
		}
		pathKhung = new Path();
		pathKhung.reset();
		pathKhung.moveTo(widthLayout , 0);
		pathKhung.lineTo(widthLayout, heightLayout);
		pathKhung.lineTo(0 , heightLayout);
		pathKhung.lineTo(0, 0);
		//if(position == 0){
			pathKhung.lineTo(widthLayout , 0);
		//}
		pathGoc = new Path();
		pathGoc.reset();
		pathGoc.moveTo(0 , 0 + Line);
		pathGoc.lineTo(0, 0);
		//if(position == 0){
			pathGoc.lineTo(0 + Line , 0);
		//}
		pathGoc.moveTo(widthLayout , 0 + Line);
		pathGoc.lineTo(widthLayout , 0);
		//if(position == 0){
			pathGoc.lineTo(widthLayout - Line , 0);
		//}
		pathGoc.moveTo(widthLayout , heightLayout - Line);
		pathGoc.lineTo(widthLayout, heightLayout);
		pathGoc.lineTo(widthLayout - Line , heightLayout);
		pathGoc.moveTo(0 , heightLayout - Line);
		pathGoc.lineTo(0 , heightLayout);
		pathGoc.lineTo(0 + Line , heightLayout);
		
		int tamX = 0;
		int rw = 0;
		if (skServer.isActive()) {
			tamX = (int) (0.49*h/tyle);
			rw = (int) (0.32*h/tyle);
		} else {
			tamX = (int) (0.49*h);
			rw = (int) (0.32*h);
		}
		int rr = 71*rw/65;
		rectConnect = new Rect(tamX - rw, tamX - rr, tamX + rw, tamX + rr);
		
		if (skServer.isActive()) {
			nameS = (float) (0.35*h/tyle);
			nameX = (float) (h/tyle + 0.01*w);
			nameY = (float) (0.4*h/tyle); 
			idS = (float) (0.3*h/tyle);
			idX = (float) (h/tyle + 0.01*w);
			idY = (float) (0.8*h/tyle); 
		} else {
			nameS = (float) (0.35*h);
			nameX = (float) (h + 0.01*w);
			nameY = (float) (0.4*h); 
			idS = (float) (0.3*h);
			idX = (float) (h + 0.01*w);
			idY = (float) (0.8*h); 
		}
		
		int txx = (int) (0.78*w);
		int hrr = (int) (0.05*w);
		int wrr = (int) (3.5*hrr);
		ketnoiS = (float) (0.75*idS);
		textPaint.setTextSize(ketnoiS);
		float space;
		if(skServer.getState() != SKServer.CONNECTED){
			space = textPaint.measureText(chuaketnoi) + 2;
			ketnoiX = txx + wrr - space;
		}else{
			space = textPaint.measureText(dangketnoi) + 2;
			ketnoiX = txx + wrr - space;
		}
		ketnoiY = idY;
		
		daluuS = ketnoiS;
		textPaint.setTextSize(daluuS);
		daluuX = ketnoiX - textPaint.measureText(dalua + "....");
		daluuY = (float) (0.8*h);
		
		if(skServer.isActive()){
			if(skServer.getState() != SKServer.CONNECTED){
				int hr = (int) (0.06*w);
				int wr = (int) (2.5*hr);
				int ty = (int) (0.7*h);
				// int tx = (int) (0.78*w);
				int tx = w - (h-ty-hr) - wr;
				rectButtonConnect = new Rect(tx - wr, ty - hr, tx + wr,  ty + hr);
				rectButtonModel = new Rect(tx - 3*wr - 15, ty - hr, tx - wr - 15,  ty + hr);
				
				ketnoi = getResources().getString(R.string.one_device_4);
				connectS = (int) (0.7*hr);
				textPaint.setTextSize(connectS);
				float width = textPaint.measureText(ketnoi);
				connectX = (int) (tx - width/2);
				connectY = (int) (ty + connectS/2.5);
				
				width = textPaint.measureText(xoalua);
				xoaluuX = (int) (tx - width/2);
			}else{
				int hr = (int) (0.06*w);
				int wr = (int) (2.8*hr);
				int ty = (int) (0.7*h);
				// int tx = (int) (0.78*w);
				int tx = w - (h-ty-hr) - wr;
				rectButtonConnect = new Rect(tx - wr, ty - hr, tx + wr,  ty + hr);
				rectButtonModel = new Rect(tx - 3*wr - 15, ty - hr, tx - wr - 15,  ty + hr);
				
				ketnoi = getResources().getString(R.string.connect_one_5);
				connectS = (int) (0.65*hr);
				textPaint.setTextSize(connectS);
				float width = textPaint.measureText(ketnoi);
				connectX = (int) (tx - width/2);
				connectY = (int) (ty + connectS/2.5);
			}
			
			ketnoiS = (float) (0.75*idS);
			textPaint.setTextSize(ketnoiS);
			if(skServer.getState() != SKServer.CONNECTED){
				space = textPaint.measureText(chuaketnoi) + 2;
			}else{
				space = textPaint.measureText(dangketnoi) + 2;
			}
			ketnoiX = rectButtonConnect.right - space;
			ketnoiY = idY;
			
			daluuS = ketnoiS;
			textPaint.setTextSize(daluuS);
			daluuX = rectButtonConnect.right - textPaint.measureText(dalua + "....") - space;
			daluuY = (float) (0.8*h);
			
			
			daumayS = chonModelS = connectS;
			float taX = (rectButtonModel.right + rectButtonModel.left)/2;
			float taY = (rectButtonModel.bottom + rectButtonModel.top)/2;
			float wi = textPaint.measureText(chonModel);
			chonModelX = (int) (taX - wi/1.65);
			wi = textPaint.measureText(dauMay);
			daumayX = (int) (taX - wi/1.65);
			chonModelY = (int) (taY - connectS/3);
			daumayY = (int) (taY + connectS);
			
			wi = textPaint.measureText(daukaraoke);
			daukaraokeX = (int) (taX - wi/2);
			wi = textPaint.measureText(caidat);
			caidatX = (int) (taX - wi/2);
		}
	}

	private int color_01;
	private int color_02;
	private int color_03;
	private int color_04;
	private int color_05;
	private int color_06;
	private int color_07;
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		/*
		MyApplication.intColorScreen = MyApplication.SCREEN_LIGHT;
		*/
		// BACKVIEW

		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			color_01 = Color.WHITE;
			color_02 = Color.CYAN;
			color_03 = Color.GREEN;
			color_04 = Color.argb(255, 180, 253, 255);
			color_05 = Color.argb(255, 180, 254, 255);
			color_06 = Color.argb(255, 0, 78, 144);
			color_07 = Color.GRAY;
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			color_01 = Color.WHITE;
			color_02 = Color.parseColor("#64949F");
			color_03 = Color.parseColor("#21BAA9");
			color_04 = Color.parseColor("#21BAA9");
			color_05 = Color.parseColor("#FFFFFF");
			color_06 = Color.argb(255, 0, 78, 144);
			color_07 = Color.parseColor("#E6E7E8");
		}
		
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			// DRAW 0
			if (skServer.getState() != SKServer.SAVED) {
				paintSimple.setShader(gradient);
				paintSimple.setStrokeWidth(getHeight());
				canvas.drawLine(0, heightLayout / 2, widthLayout, heightLayout / 2, paintSimple);
			}

			// -CONNECTED-------------------

			if (skServer.getState() == SKServer.CONNECTED) {
				if (skServer.isActive()) {
					
					drawGlow.setBounds(rectBoder);
					drawGlow.draw(canvas);

					modelDevice = MyApplication.intSvrModel;
					flagSmartK_801 = MyApplication.flagSmartK_801;
					flagSmartK_KM4 = MyApplication.flagSmartK_KM4;
					flagSmartK_CB = MyApplication.flagSmartK_CB;
					
					if(modelDevice == MyApplication.SONCA){
						drawConnectSK9.setBounds(rectConnect);
						drawConnectSK9.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_KARTROL){
						drawConnectKar.setBounds(rectConnect);
						drawConnectKar.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_HIW){
						drawConnectKTV.setBounds(rectConnect);
						drawConnectKTV.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_KM1){
						drawConnectKM1.setBounds(rectConnect);
						drawConnectKM1.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_KM2){
						drawConnectKM2.setBounds(rectConnect);
						drawConnectKM2.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_KB_OEM){
						tempDrawable = getResources().getDrawable(R.drawable.icon_kb_oem_active);
						tempDrawable.setBounds(rectConnect);
						tempDrawable.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_KM1_WIFI){
						tempDrawable = getResources().getDrawable(R.drawable.icon_km1wifi_active);
						tempDrawable.setBounds(rectConnect);
						tempDrawable.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_KB39C_WIFI){
						tempDrawable = getResources().getDrawable(R.drawable.icon_kb39c_active);
						tempDrawable.setBounds(rectConnect);
						tempDrawable.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_KBX9){
						tempDrawable = getResources().getDrawable(R.drawable.daumay_kb_active);
						tempDrawable.setBounds(rectConnect);
						tempDrawable.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_SMARTK){
						tempDrawable = getResources().getDrawable(R.drawable.daumay_9108);
						if(flagSmartK_CB){
							tempDrawable = getResources().getDrawable(R.drawable.cloudbox_active);
						}
						if(flagSmartK_801){
							tempDrawable = getResources().getDrawable(R.drawable.sb801_active);
						}
						if(flagSmartK_KM4){
							tempDrawable = getResources().getDrawable(R.drawable.km4_active);
						}
						tempDrawable.setBounds(rectConnect);
						tempDrawable.draw(canvas);
						
						tempDrawable = getResources().getDrawable(R.drawable.wifi_9108_4);
						tempDrawable.setBounds(rectConnect);
						tempDrawable.draw(canvas);						
					}else if(modelDevice == MyApplication.SONCA_TBT){
						tempDrawable = getResources().getDrawable(R.drawable.daumay_tbt);
						tempDrawable.setBounds(rectConnect);
						tempDrawable.draw(canvas);
						
						tempDrawable = getResources().getDrawable(R.drawable.ktvwifi_4);
						tempDrawable.setBounds(rectConnect);
						tempDrawable.draw(canvas);						
					}

					textPaint.setColor(color_03);
					textPaint.setTextSize(nameS);
					canvas.drawText(skServer.getName(), nameX, nameY, textPaint);
					textPaint.setTextSize(idS);
					if (modelDevice == MyApplication.SONCA || modelDevice == MyApplication.SONCA_HIW || modelDevice == MyApplication.SONCA_KM2
							|| modelDevice == MyApplication.SONCA_KB_OEM || modelDevice == MyApplication.SONCA_KM1_WIFI
							|| modelDevice == MyApplication.SONCA_KBX9 || modelDevice == MyApplication.SONCA_KB39C_WIFI
							|| modelDevice == MyApplication.SONCA_SMARTK || modelDevice == MyApplication.SONCA_TBT) {
						textPaint.setColor(color_04);
						canvas.drawText(skServer.getConnectedIPStr(), idX, idY, textPaint);
					} else {
						textPaint.setColor(color_04);
						canvas.drawText(skServer.getNameRemote(), idX, idY, textPaint);
					}
					if (isClickBut == true) {
						drawKetNoiHover.setBounds(rectButtonConnect);
						drawKetNoiHover.draw(canvas);
					} else {
						drawKetNoi.setBounds(rectButtonConnect);
						drawKetNoi.draw(canvas);
					}
					textPaint.setColor(color_05);
					textPaint.setTextSize(connectS);
					canvas.drawText(ketnoi, connectX, connectY, textPaint);

					if(modelDevice != MyApplication.SONCA && modelDevice != MyApplication.SONCA_SMARTK){
						if (isClickModel == true) {
							drawKetNoiHover.setBounds(rectButtonModel);
							drawKetNoiHover.draw(canvas);
						} else {
							drawKetNoi.setBounds(rectButtonModel);
							drawKetNoi.draw(canvas);
						}
						
						textPaint.setColor(color_05);
						textPaint.setTextSize(connectS);
						canvas.drawText(caidat, caidatX, chonModelY, textPaint);
						canvas.drawText(daukaraoke, daukaraokeX, daumayY, textPaint);
					}
					// mainPaint.setStyle(Style.STROKE);
					// mainPaint.setColor(color_03);
					// canvas.drawRect(rectButtonConnect, mainPaint);

					textPaint.setTextSize(ketnoiS);
					textPaint.setColor(color_03);
					int offsetX = (int) (rectButtonConnect.right - textPaint.measureText(dangketnoi) - 2);
					canvas.drawText(dangketnoi, offsetX, ketnoiY, textPaint);
				} else {
					mainPaint.setStyle(Style.STROKE);
					mainPaint.setColor(color_06);
					canvas.drawPath(pathKhung, mainPaint);
					mainPaint.setColor(color_02);
					canvas.drawPath(pathGoc, mainPaint);

					if(modelDevice == MyApplication.SONCA){
						drawConnectSK9.setBounds(rectConnect);
						drawConnectSK9.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_KARTROL){
						drawConnectKar.setBounds(rectConnect);
						drawConnectKar.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_HIW){
						drawConnectKTV.setBounds(rectConnect);
						drawConnectKTV.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_KM1){
						drawConnectKM1.setBounds(rectConnect);
						drawConnectKM1.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_KM2){
						drawConnectKM2.setBounds(rectConnect);
						drawConnectKM2.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_KB_OEM){
						tempDrawable = getResources().getDrawable(R.drawable.icon_kb_oem_active);
						tempDrawable.setBounds(rectConnect);
						tempDrawable.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_KM1_WIFI){
						tempDrawable = getResources().getDrawable(R.drawable.icon_km1wifi_active);
						tempDrawable.setBounds(rectConnect);
						tempDrawable.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_KB39C_WIFI){
						tempDrawable = getResources().getDrawable(R.drawable.icon_kb39c_active);
						tempDrawable.setBounds(rectConnect);
						tempDrawable.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_KBX9){
						tempDrawable = getResources().getDrawable(R.drawable.daumay_kb_active);
						tempDrawable.setBounds(rectConnect);
						tempDrawable.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_SMARTK){
						tempDrawable = getResources().getDrawable(R.drawable.daumay_9108);
						if(flagSmartK_CB){
							tempDrawable = getResources().getDrawable(R.drawable.cloudbox_active);
						}
						if(flagSmartK_801){
							tempDrawable = getResources().getDrawable(R.drawable.sb801_active);
						}
						if(flagSmartK_KM4){
							tempDrawable = getResources().getDrawable(R.drawable.km4_active);
						}
						tempDrawable.setBounds(rectConnect);
						tempDrawable.draw(canvas);
						
						tempDrawable = getResources().getDrawable(R.drawable.wifi_9108_4);
						tempDrawable.setBounds(rectConnect);
						tempDrawable.draw(canvas);						
					}else if(modelDevice == MyApplication.SONCA_TBT){
						tempDrawable = getResources().getDrawable(R.drawable.daumay_tbt);
						tempDrawable.setBounds(rectConnect);
						tempDrawable.draw(canvas);
						
						tempDrawable = getResources().getDrawable(R.drawable.ktvwifi_4);
						tempDrawable.setBounds(rectConnect);
						tempDrawable.draw(canvas);						
					}

					textPaint.setColor(color_03);
					textPaint.setTextSize(nameS);
					canvas.drawText(skServer.getName(), nameX, nameY, textPaint);

					textPaint.setTextSize(idS);
					if (modelDevice == MyApplication.SONCA || modelDevice == MyApplication.SONCA_HIW || modelDevice == MyApplication.SONCA_KM2
							|| modelDevice == MyApplication.SONCA_KB_OEM || modelDevice == MyApplication.SONCA_KM1_WIFI
							|| modelDevice == MyApplication.SONCA_KBX9 || modelDevice == MyApplication.SONCA_KB39C_WIFI
							|| modelDevice == MyApplication.SONCA_SMARTK || modelDevice == MyApplication.SONCA_TBT) {
						textPaint.setColor(color_04);
						canvas.drawText(skServer.getConnectedIPStr(), idX, idY, textPaint);
					} else {
						textPaint.setColor(color_04);
						canvas.drawText(skServer.getNameRemote(), idX, idY, textPaint);
					}
					textPaint.setTextSize(ketnoiS);
					textPaint.setColor(color_03);
					canvas.drawText(dangketnoi, ketnoiX, ketnoiY, textPaint);
				}
			}

			// -BROADCASTED-------------------

			if (skServer.getState() == SKServer.BROADCASTED) {
				if (skServer.isActive()) {
					drawGlow.setBounds(rectBoder);
					drawGlow.draw(canvas);

					mainPaint.setStyle(Style.STROKE);
					mainPaint.setColor(color_01);
					canvas.drawPath(pathKhung, mainPaint);
					mainPaint.setColor(color_02);
					canvas.drawPath(pathGoc, mainPaint);

					if(modelDevice == MyApplication.SONCA){
						drawBroadcastSK9.setBounds(rectConnect);
						drawBroadcastSK9.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_KARTROL){
						drawBroadcastKar.setBounds(rectConnect);
						drawBroadcastKar.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_HIW){
						drawBroadcastKTV.setBounds(rectConnect);
						drawBroadcastKTV.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_KM1){
						drawBroadcastKM1.setBounds(rectConnect);
						drawBroadcastKM1.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_KM2){
						drawBroadcastKM2.setBounds(rectConnect);
						drawBroadcastKM2.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_KB_OEM){
						tempDrawable = getResources().getDrawable(R.drawable.kb_oem_inactive);
						tempDrawable.setBounds(rectConnect);
						tempDrawable.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_KM1_WIFI){
						tempDrawable = getResources().getDrawable(R.drawable.km1wifi_inactive);
						tempDrawable.setBounds(rectConnect);
						tempDrawable.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_KB39C_WIFI){
						tempDrawable = getResources().getDrawable(R.drawable.kb39c_inactive);
						tempDrawable.setBounds(rectConnect);
						tempDrawable.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_KBX9){
						tempDrawable = getResources().getDrawable(R.drawable.daumay_kb_inactive);
						tempDrawable.setBounds(rectConnect);
						tempDrawable.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_SMARTK){
						tempDrawable = getResources().getDrawable(R.drawable.daumay_9108_inactive);
						if(flagSmartK_CB){
							tempDrawable = getResources().getDrawable(R.drawable.cloudbox_inactive);
						}
						if(flagSmartK_801){
							tempDrawable = getResources().getDrawable(R.drawable.sb801_inactive);
						}
						if(flagSmartK_KM4){
							tempDrawable = getResources().getDrawable(R.drawable.km4_inactive);
						}
						tempDrawable.setBounds(rectConnect);
						tempDrawable.draw(canvas);						
					}else if(modelDevice == MyApplication.SONCA_TBT){
						tempDrawable = getResources().getDrawable(R.drawable.daumay_tbt);
						tempDrawable.setBounds(rectConnect);
						tempDrawable.draw(canvas);
						
						tempDrawable = getResources().getDrawable(R.drawable.ktvwifi_4);
						tempDrawable.setBounds(rectConnect);
						tempDrawable.draw(canvas);						
					}

					textPaint.setColor(color_02);
					textPaint.setTextSize(nameS);
					canvas.drawText(skServer.getName(), nameX, nameY, textPaint);
					textPaint.setTextSize(idS);
					if (modelDevice == MyApplication.SONCA || modelDevice == MyApplication.SONCA_HIW || modelDevice == MyApplication.SONCA_KM2
							|| modelDevice == MyApplication.SONCA_KB_OEM || modelDevice == MyApplication.SONCA_KM1_WIFI
							|| modelDevice == MyApplication.SONCA_KBX9 || modelDevice == MyApplication.SONCA_KB39C_WIFI
							|| modelDevice == MyApplication.SONCA_SMARTK || modelDevice == MyApplication.SONCA_TBT) {
						textPaint.setColor(color_02);
						canvas.drawText(skServer.getConnectedIPStr(), idX, idY, textPaint);
					} else {
						textPaint.setColor(color_04);
						canvas.drawText(skServer.getNameRemote(), idX, idY, textPaint);
					}
					if (isClickBut == true) {
						drawKetNoiHover.setBounds(rectButtonConnect);
						drawKetNoiHover.draw(canvas);
					} else {
						drawKetNoi.setBounds(rectButtonConnect);
						drawKetNoi.draw(canvas);
					}
					textPaint.setColor(color_05);
					textPaint.setTextSize(connectS);
					canvas.drawText(ketnoi, connectX, connectY, textPaint);

					// mainPaint.setStyle(Style.STROKE);
					// mainPaint.setColor(color_02);
					// canvas.drawRect(rectButtonConnect, mainPaint);

					textPaint.setTextSize(ketnoiS);
					textPaint.setColor(color_02);
					canvas.drawText(chuaketnoi, ketnoiX, idY, textPaint);

					if (isPassword) {
						canvas.drawText(dalua, daluuX, idY, textPaint);
					}

				} else {
					mainPaint.setStyle(Style.STROKE);
					mainPaint.setColor(color_06);
					canvas.drawPath(pathKhung, mainPaint);
					mainPaint.setColor(color_02);
					canvas.drawPath(pathGoc, mainPaint);
					
					if(modelDevice == MyApplication.SONCA){
						drawBroadcastSK9.setBounds(rectConnect);
						drawBroadcastSK9.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_KARTROL){
						drawBroadcastKar.setBounds(rectConnect);
						drawBroadcastKar.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_HIW){
						drawBroadcastKTV.setBounds(rectConnect);
						drawBroadcastKTV.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_KM1){
						drawBroadcastKM1.setBounds(rectConnect);
						drawBroadcastKM1.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_KM2){
						drawBroadcastKM2.setBounds(rectConnect);
						drawBroadcastKM2.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_KB_OEM){
						tempDrawable = getResources().getDrawable(R.drawable.kb_oem_inactive);
						tempDrawable.setBounds(rectConnect);
						tempDrawable.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_KM1_WIFI){
						tempDrawable = getResources().getDrawable(R.drawable.km1wifi_inactive);
						tempDrawable.setBounds(rectConnect);
						tempDrawable.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_KB39C_WIFI){
						tempDrawable = getResources().getDrawable(R.drawable.kb39c_inactive);
						tempDrawable.setBounds(rectConnect);
						tempDrawable.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_KBX9){
						tempDrawable = getResources().getDrawable(R.drawable.daumay_kb_inactive);
						tempDrawable.setBounds(rectConnect);
						tempDrawable.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_SMARTK){
						tempDrawable = getResources().getDrawable(R.drawable.daumay_9108_inactive);
						if(flagSmartK_CB){
							tempDrawable = getResources().getDrawable(R.drawable.cloudbox_inactive);
						}
						if(flagSmartK_801){
							tempDrawable = getResources().getDrawable(R.drawable.sb801_inactive);
						}
						if(flagSmartK_KM4){
							tempDrawable = getResources().getDrawable(R.drawable.km4_inactive);
						}
						tempDrawable.setBounds(rectConnect);
						tempDrawable.draw(canvas);						
					}else if(modelDevice == MyApplication.SONCA_TBT){
						tempDrawable = getResources().getDrawable(R.drawable.daumay_tbt);
						tempDrawable.setBounds(rectConnect);
						tempDrawable.draw(canvas);
						
						tempDrawable = getResources().getDrawable(R.drawable.ktvwifi_4);
						tempDrawable.setBounds(rectConnect);
						tempDrawable.draw(canvas);						
					}

					textPaint.setColor(color_02);
					textPaint.setTextSize(nameS);
					canvas.drawText(skServer.getName(), nameX, nameY, textPaint);

					textPaint.setColor(color_02);
					textPaint.setTextSize(idS);
					if (modelDevice == MyApplication.SONCA || modelDevice == MyApplication.SONCA_HIW || modelDevice == MyApplication.SONCA_KM2
							|| modelDevice == MyApplication.SONCA_KB_OEM || modelDevice == MyApplication.SONCA_KM1_WIFI
							|| modelDevice == MyApplication.SONCA_KBX9 || modelDevice == MyApplication.SONCA_KB39C_WIFI
							|| modelDevice == MyApplication.SONCA_SMARTK || modelDevice == MyApplication.SONCA_TBT) {
						textPaint.setColor(color_02);
						canvas.drawText(skServer.getConnectedIPStr(), idX, idY, textPaint);
					} else {
						textPaint.setColor(color_04);
						canvas.drawText(skServer.getNameRemote(), idX, idY, textPaint);
					}
					textPaint.setTextSize(ketnoiS);
					textPaint.setColor(color_02);
					canvas.drawText(chuaketnoi, ketnoiX, ketnoiY, textPaint);
					if (isPassword) {
						canvas.drawText(dalua, daluuX, daluuY, textPaint);
					}
				}
			}

			// -SAVED-----------------------

			if (skServer.getState() == SKServer.SAVED) {
				if (skServer.isActive()) {
					drawGlow.setBounds(rectBoder);
					drawGlow.draw(canvas);

					if(modelDevice == MyApplication.SONCA || modelDevice == MyApplication.SONCA_SMARTK){
						if(flagSmartK_801){
							tempDrawable = getResources().getDrawable(R.drawable.sb801_xam);
							tempDrawable.setBounds(rectConnect);
							tempDrawable.draw(canvas);
						} else if (flagSmartK_KM4){
							tempDrawable = getResources().getDrawable(R.drawable.km4_xam);
							tempDrawable.setBounds(rectConnect);
							tempDrawable.draw(canvas);
						} else if (flagSmartK_CB){
							tempDrawable = getResources().getDrawable(R.drawable.cloudbox_xam);
							tempDrawable.setBounds(rectConnect);
							tempDrawable.draw(canvas);
						} else {
							drawSaveSK9.setBounds(rectConnect);
							drawSaveSK9.draw(canvas);	
						}		
					}else if(modelDevice == MyApplication.SONCA_KARTROL){
						drawSaveKar.setBounds(rectConnect);
						drawSaveKar.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_HIW){
						drawSaveKTV.setBounds(rectConnect);
						drawSaveKTV.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_KM1){
						drawSaveKM1.setBounds(rectConnect);
						drawSaveKM1.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_KM2){
						drawSaveKM2.setBounds(rectConnect);
						drawSaveKM2.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_KB_OEM){
						tempDrawable = getResources().getDrawable(R.drawable.kb_oem_xam);
						tempDrawable.setBounds(rectConnect);
						tempDrawable.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_KM1_WIFI){
						tempDrawable = getResources().getDrawable(R.drawable.km1wifi_xam);
						tempDrawable.setBounds(rectConnect);
						tempDrawable.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_KB39C_WIFI){
						tempDrawable = getResources().getDrawable(R.drawable.kb39c_xam);
						tempDrawable.setBounds(rectConnect);
						tempDrawable.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_KBX9){
						tempDrawable = getResources().getDrawable(R.drawable.daumay_kb_xam);
						tempDrawable.setBounds(rectConnect);
						tempDrawable.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_TBT){
						tempDrawable = getResources().getDrawable(R.drawable.daumay_tbt_xam);
						tempDrawable.setBounds(rectConnect);
						tempDrawable.draw(canvas);				
					}

					textPaint.setColor(color_07);
					textPaint.setTextSize(nameS);
					canvas.drawText(skServer.getName(), nameX, nameY, textPaint);
					textPaint.setTextSize(idS);
					if (modelDevice == MyApplication.SONCA || modelDevice == MyApplication.SONCA_HIW || modelDevice == MyApplication.SONCA_KM2
							|| modelDevice == MyApplication.SONCA_KB_OEM || modelDevice == MyApplication.SONCA_KM1_WIFI
							|| modelDevice == MyApplication.SONCA_KBX9 || modelDevice == MyApplication.SONCA_KB39C_WIFI
							|| modelDevice == MyApplication.SONCA_SMARTK || modelDevice == MyApplication.SONCA_TBT) {
						canvas.drawText(skServer.getConnectedIPStr(), idX, idY, textPaint);
					} else {
						canvas.drawText(skServer.getNameRemote(), idX, idY, textPaint);
					}
					if (isClickBut == true) {
						drawKetNoiHover.setBounds(rectButtonConnect);
						drawKetNoiHover.draw(canvas);
					} else {
						drawKetNoi.setBounds(rectButtonConnect);
						drawKetNoi.draw(canvas);
					}

					// mainPaint.setStyle(Style.STROKE);
					// canvas.drawRect(rectButtonConnect, mainPaint);

					/*
					 * textPaint.setTextSize(idS); int offsetX = (int)
					 * (rectButtonConnect.right -
					 * textPaint.measureText(xoalua)); canvas.drawText(xoalua ,
					 * offsetX, idY, textPaint);
					 */
					// if (isPassword) {
					textPaint.setTextSize(ketnoiS);
					int offsetX = (int) (rectButtonConnect.right - textPaint.measureText(dalua) - 2);
					canvas.drawText(dalua, offsetX, idY, textPaint);
					// }
					textPaint.setColor(color_05);
					textPaint.setTextSize(connectS);
					canvas.drawText(xoalua, xoaluuX, connectY, textPaint);
				} else {
					if(modelDevice == MyApplication.SONCA || modelDevice == MyApplication.SONCA_SMARTK){
						if(flagSmartK_801){
							tempDrawable = getResources().getDrawable(R.drawable.sb801_xam);
							tempDrawable.setBounds(rectConnect);
							tempDrawable.draw(canvas);
						} else if(flagSmartK_CB){
							tempDrawable = getResources().getDrawable(R.drawable.cloudbox_xam);
							tempDrawable.setBounds(rectConnect);
							tempDrawable.draw(canvas);
						} else if (flagSmartK_KM4){
							tempDrawable = getResources().getDrawable(R.drawable.km4_xam);
							tempDrawable.setBounds(rectConnect);
							tempDrawable.draw(canvas);
						} else {
							drawSaveSK9.setBounds(rectConnect);
							drawSaveSK9.draw(canvas);	
						}		
					}else if(modelDevice == MyApplication.SONCA_KARTROL){
						drawSaveKar.setBounds(rectConnect);
						drawSaveKar.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_HIW){
						drawSaveKTV.setBounds(rectConnect);
						drawSaveKTV.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_KM1){
						drawSaveKM1.setBounds(rectConnect);
						drawSaveKM1.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_KM2){
						drawSaveKM2.setBounds(rectConnect);
						drawSaveKM2.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_KB_OEM){
						tempDrawable = getResources().getDrawable(R.drawable.kb_oem_xam);
						tempDrawable.setBounds(rectConnect);
						tempDrawable.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_KM1_WIFI){
						tempDrawable = getResources().getDrawable(R.drawable.km1wifi_xam);
						tempDrawable.setBounds(rectConnect);
						tempDrawable.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_KB39C_WIFI){
						tempDrawable = getResources().getDrawable(R.drawable.kb39c_xam);
						tempDrawable.setBounds(rectConnect);
						tempDrawable.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_KBX9){
						tempDrawable = getResources().getDrawable(R.drawable.daumay_kb_xam);
						tempDrawable.setBounds(rectConnect);
						tempDrawable.draw(canvas);
					}else if(modelDevice == MyApplication.SONCA_TBT){
						tempDrawable = getResources().getDrawable(R.drawable.daumay_tbt_xam);
						tempDrawable.setBounds(rectConnect);
						tempDrawable.draw(canvas);				
					}

					mainPaint.setStyle(Style.STROKE);
					mainPaint.setColor(color_07);
					canvas.drawPath(pathGoc, mainPaint);

					textPaint.setColor(color_07);
					textPaint.setTextSize(nameS);
					canvas.drawText(skServer.getName(), nameX, nameY, textPaint);

					textPaint.setColor(color_07);
					textPaint.setTextSize(idS);
					if (modelDevice == MyApplication.SONCA || modelDevice == MyApplication.SONCA_HIW || modelDevice == MyApplication.SONCA_KM2
							|| modelDevice == MyApplication.SONCA_KB_OEM || modelDevice == MyApplication.SONCA_KM1_WIFI
							|| modelDevice == MyApplication.SONCA_KBX9 || modelDevice == MyApplication.SONCA_KB39C_WIFI
							|| modelDevice == MyApplication.SONCA_SMARTK || modelDevice == MyApplication.SONCA_TBT) {
						canvas.drawText(skServer.getConnectedIPStr(), idX, idY, textPaint);
					} else {
						canvas.drawText(skServer.getNameRemote(), idX, idY, textPaint);
					}
					textPaint.setTextSize(ketnoiS);
					textPaint.setColor(color_07);
					canvas.drawText(dalua, daluuX, daluuY, textPaint);
				}
			}

		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {

		}
	}
	
	private boolean isClickBut = false;
	private boolean isClickModel = false;
	@Override
	public void setPressed(boolean pressed) {
		super.setPressed(pressed);
		setClickable(pressed);
		if(pressed == false){
			isClickBut = false;
			isClickModel = false;
			invalidate();
			if(modelDevice != MyApplication.SONCA && modelDevice != MyApplication.SONCA_SMARTK){
				if(rectButtonModel != null){
					if(offsetX >= rectButtonModel.left && 
						offsetX <= rectButtonModel.right &&
						offsetY >= rectButtonModel.top &&
						offsetY <= rectButtonModel.bottom){
						if(listener != null){
							if(skServer.getState() == SKServer.CONNECTED){
								if(modelDevice != MyApplication.SONCA && modelDevice != MyApplication.SONCA_SMARTK){
									listener.OnShowHi_W(skServer);
								}
							}
						}
					}
				}
			}
			if(rectButtonConnect != null){
				if(offsetX >= rectButtonConnect.left && 
					offsetX <= rectButtonConnect.right &&
					offsetY >= rectButtonConnect.top &&
					offsetY <= rectButtonConnect.bottom){
					if(listener != null){
						if(skServer.getState() == SKServer.CONNECTED){
							listener.OnDisConnect(skServer);
						}else if (skServer.getState() == SKServer.SAVED){
							listener.OnRemoveDevice(skServer);
						}else{
							listener.OnPerformConnect(skServer);
						}
					}
				}
			}
		}else{
			if(rectButtonModel!= null){
				if(offsetX >= rectButtonModel.left && 
					offsetX <= rectButtonModel.right &&
					offsetY >= rectButtonModel.top &&
					offsetY <= rectButtonModel.bottom){
					isClickModel = true;
					invalidate();
				}
			}
			if(rectButtonConnect != null){
				if(offsetX >= rectButtonConnect.left && 
					offsetX <= rectButtonConnect.right &&
					offsetY >= rectButtonConnect.top &&
					offsetY <= rectButtonConnect.bottom){
					isClickBut = true;
					invalidate();
				}
			}
		}
	}

	private float offsetX;
	private float offsetY;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			offsetX = event.getX();
			offsetY = event.getY();
			break;
		default:
			break;
		}
		return false;
	}
	
	
}
