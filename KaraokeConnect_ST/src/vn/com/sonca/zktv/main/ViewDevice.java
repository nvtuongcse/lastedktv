package vn.com.sonca.zktv.main;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.zzzzz.MyApplication;

public class ViewDevice extends View {
	
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	
	public ViewDevice(Context context) {
		super(context);
		initView(context);
	}

	public ViewDevice(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public ViewDevice(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Drawable drawWifi0, drawWifi1, drawWifi2, drawWifi3, drawWifi4;
	private Drawable drawWifi0_hiw, drawWifi1_hiw, drawWifi2_hiw, drawWifi3_hiw, drawWifi4_hiw;
	private Drawable drawWifi0_smartk, drawWifi1_smartk, drawWifi2_smartk, drawWifi3_smartk, drawWifi4_smartk;
	private Drawable drawWifi0_xam;
	
	private Drawable drawableW, drawable;
	private Drawable drawable2;
	private Animation anime;
	private void initView(Context context) {
		drawableW = context.getResources().getDrawable(R.drawable.ktv_icon_daumay);
		drawable2 = context.getResources().getDrawable(R.drawable.ktv_icon_khoa);
		anime = AnimationUtils.loadAnimation(context, R.anim.animation_song);
		textPaint.setStyle(Style.FILL);
		textPaint.setColor(Color.RED);
		textPaint.setTypeface(Typeface.DEFAULT_BOLD);
		
		drawWifi0 = getResources().getDrawable(R.drawable.wifi_0_shadow);
		drawWifi1 = getResources().getDrawable(R.drawable.wifi_1_shadow);
		drawWifi2 = getResources().getDrawable(R.drawable.wifi_2_shadow);
		drawWifi3 = getResources().getDrawable(R.drawable.wifi_3_shadow);
		drawWifi4 = getResources().getDrawable(R.drawable.wifi_4_shadow);
		
		drawWifi0_hiw = getResources().getDrawable(R.drawable.ktvwifi_0_shadow);
		drawWifi1_hiw = getResources().getDrawable(R.drawable.ktvwifi_1_shadow);
		drawWifi2_hiw = getResources().getDrawable(R.drawable.ktvwifi_2_shadow);
		drawWifi3_hiw = getResources().getDrawable(R.drawable.ktvwifi_3_shadow);
		drawWifi4_hiw = getResources().getDrawable(R.drawable.ktvwifi_4_shadow);
		
		drawWifi0_smartk = getResources().getDrawable(R.drawable.wifi_9108_0_shadow);
		drawWifi1_smartk = getResources().getDrawable(R.drawable.wifi_9108_1_shadow);
		drawWifi2_smartk = getResources().getDrawable(R.drawable.wifi_9108_2_shadow);
		drawWifi3_smartk = getResources().getDrawable(R.drawable.wifi_9108_3_shadow);
		drawWifi4_smartk = getResources().getDrawable(R.drawable.wifi_9108_4_shadow);		
		
		drawWifi0_xam = getResources().getDrawable(R.drawable.wifi_0_xam_shadow);
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int width = (int) (2.1*height);
		setMeasuredDimension(width, height);
	}
	
	private int textS, textX, textY;
	private Rect rectImage = new Rect();
	private Rect rectImage2 = new Rect();
	private Rect rectImage3 = new Rect();
	private float widthLayout;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
		widthLayout = w;
		
		int tamX = (int) (0.5*w);
		int tamY = (int) (0.4*h);
		int hh = (int) (0.3*h);
		int ww = 90*hh/70;
		rectImage.set(tamX-ww, tamY-hh, tamX+ww, tamY+hh);
		
		tamX = (int) (0.29*w);
		tamY = (int) (0.54*h);
		hh = (int) (0.15*h);
		ww = hh;
		rectImage2.set(tamX-ww, tamY-hh, tamX+ww, tamY+hh);
		
		tamX = (int) (0.51*w);
		tamY = (int) (0.38*h);
		hh = (int) (0.3*h);
		ww = 71*hh/65;
		rectImage3.set(tamX-ww, tamY-hh, tamX+ww, tamY+hh);
		
		textS = (int) (0.2*h);
		textX = (int) (0.5*w);
		textY = (int) (0.9*h);
		
		textPaint.setTextSize(textS);
		
	}
	
	private boolean isConnect = false;	
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
				
		isConnect = KTVMainActivity.serverStatus != null;
		isblock = !MyApplication.flagDeviceUser;
		
		if(isConnect){
			// DAU MAY
			drawable = getResources().getDrawable(
					R.drawable.icon_daumay_daketnoi_71x65_shadow);
			if(MyApplication.intSvrModel == MyApplication.SONCA_HIW){
				drawable = getResources().getDrawable(
						R.drawable.icon_daumay_ktvwifi_bottom_shadow);
			}
			if(MyApplication.intSvrModel == MyApplication.SONCA_KM2){
				drawable = getResources().getDrawable(
						R.drawable.icon_daumay_km2_bottom_shadow);
			}
			if(MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM){
				drawable = getResources().getDrawable(
						R.drawable.kboem_shadow);
			}	
			if(MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI){
				drawable = getResources().getDrawable(
						R.drawable.icon_daumay_km1wifi_bottom_shadow);
			}
			if(MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI){
				drawable = getResources().getDrawable(
						R.drawable.icon_daumay_kb39c_bottom_shadow);
			}
			if(MyApplication.intSvrModel == MyApplication.SONCA_KBX9){
				drawable = getResources().getDrawable(
						R.drawable.daumay_kb_shadow);
			}
			if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
				drawable = getResources().getDrawable(R.drawable.daumay_9108_shadow);
				if(MyApplication.flagSmartK_CB){
					drawable = getResources().getDrawable(R.drawable.cloudbox_active_shadow);
				}
				if(MyApplication.flagSmartK_801){
					drawable = getResources().getDrawable(R.drawable.sb801_active_shadow);
				}
				if(MyApplication.flagSmartK_KM4){
					drawable = getResources().getDrawable(R.drawable.km4_active_shadow);
				}
			}
			if(MyApplication.intSvrModel == MyApplication.SONCA_TBT){
				drawable = getResources().getDrawable(R.drawable.daumay_tbt_shadow);
			}
			if(MyApplication.intSvrModel == MyApplication.SONCA_KM1){
				drawable = getResources().getDrawable(
						R.drawable.icon_daumay_km1_bottom_shadow);
			}
			if(MyApplication.intSvrModel == MyApplication.SONCA_KARTROL){
				drawable = getResources().getDrawable(
						R.drawable.icon_daumay_kartrol_bottom_shadow);
			}
			
			drawable.setBounds(rectImage3);
			drawable.draw(canvas);
			
			if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
					 || MyApplication.intSvrModel == MyApplication.SONCA_KBX9
					 || MyApplication.intSvrModel == MyApplication.SONCA_TBT){
				switch (MyApplication.levelWifi) {
				case 0:
					drawable = drawWifi0_hiw;
					break;
				case 1:
					drawable = drawWifi1_hiw;
					break;
				case 2:
					drawable = drawWifi2_hiw;
					break;
				case 3:
					drawable = drawWifi3_hiw;
					break;
				case 4:
					drawable = drawWifi4_hiw;
					break;
				case 5:
					drawable = drawWifi4_hiw;
					break;
				default:
					break;
				}
			} else if (MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
				switch (MyApplication.levelWifi) {
				case 0:
					drawable = drawWifi0_smartk;
					break;
				case 1:
					drawable = drawWifi1_smartk;
					break;
				case 2:
					drawable = drawWifi2_smartk;
					break;
				case 3:
					drawable = drawWifi3_smartk;
					break;
				case 4:
					drawable = drawWifi4_smartk;
					break;
				case 5:
					drawable = drawWifi4_smartk;
					break;
				default:
					break;
				}
			} else {
				switch (MyApplication.levelWifi) {
				case 0:
					drawable = drawWifi0;
					break;
				case 1:
					drawable = drawWifi1;
					break;
				case 2:
					drawable = drawWifi2;
					break;
				case 3:
					drawable = drawWifi3;
					break;
				case 4:
					drawable = drawWifi4;
					break;
				case 5:
					drawable = drawWifi4;
					break;
				default:
					break;
				}
			}			
			drawable.setBounds(rectImage3);
			drawable.draw(canvas);
			
			
			// KHOA
			if (isblock == true && drawable2 != null) {
				drawable2.setBounds(rectImage2);
				drawable2.draw(canvas);
			}
			
			// TEN DAU MAY
			if(nameDevice.equals("")){
				nameDevice = getResources().getString(R.string.connected);
			}			
			String strName = cutText(textS, 0.8f * widthLayout, nameDevice);
			float mWdith = textPaint.measureText(strName);
//			textPaint.setColor(Color.RED);
			textPaint.setARGB(255, 255, 87, 34);
			canvas.drawText(strName, textX - mWdith/2, textY, textPaint);
			
		} else {
//			if (drawableW != null) {
//				drawableW.setAlpha(100);
//				drawableW.setBounds(rectImage);
//				drawableW.draw(canvas);
//			}
			
			// DAU MAY
			drawable = getResources().getDrawable(
					R.drawable.image_daumay_inactive_71x65_x_shadow);
			drawable.setBounds(rectImage3);
			drawable.draw(canvas);
			
			// TEN DAU MAY
			String strNameDevice = getResources().getString(R.string.no_connect);
			if(countAutoConnect != -1){
				strNameDevice = getResources().getString(R.string.connect_auto);
			}
			String strName = cutText(textS, 0.8f * widthLayout, strNameDevice);
			float mWdith = textPaint.measureText(strName);
			textPaint.setARGB(125, 0, 0, 0);
			canvas.drawText(strName, textX - mWdith/2, textY, textPaint);
			
			// RUN DANG KET NOI
			switch (countAutoConnect) {
			case 0:
				drawable = drawWifi0;
				break;
			case 1:
				drawable = drawWifi1;
				break;
			case 2:
				drawable = drawWifi2;
				break;
			case 3:
				drawable = drawWifi3;
				break;
			case 4:
				drawable = drawWifi4;
				break;
			default:
				drawable = drawWifi0_xam;
				break;
			}
			drawable.setBounds(rectImage3);
			drawable.draw(canvas);
			
		}
		
	}
	
	private OnClickListener listener;
	@Override
	public void setOnClickListener(OnClickListener l) {
		super.setOnClickListener(l);
		listener = l;
	}
	
	private boolean isTouchView;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			isTouchView = true;
			break;
		case MotionEvent.ACTION_UP:
			isTouchView = false;
			if(listener != null){
				listener.onClick(this);
			}
			this.startAnimation(anime);
			break;
		default:
			break;
		}
		return true;
	}
	
	private boolean isblock;
	private String nameDevice = "";
	public void setNameDevice(boolean isblock, String nameDevice){
		this.nameDevice = nameDevice;
		this.isblock = isblock;
		invalidate();
	}
	
	public void setNameDevice(String nameDevice){
		this.nameDevice = nameDevice;
		invalidate();
	}

	private String cutText(float textSize, float maxLength, String content) {
		if (content == null || content.equals("")) {
			return "";
		}
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setTextSize(textSize);
		float length = paint.measureText(content);
		if (length > maxLength) {
			String[] strings = content.split(" ");
			if (strings.length > 1) {
				// return cutTextWord(textSize, maxLength, strings, paint);
				return cutTextChar(textSize, maxLength, content, paint);
			} else {
				return cutTextChar(textSize, maxLength, content, paint);
			}
		} else {
			return content;
		}
	}

	private String cutTextChar(float textSize, float maxLength, String content, Paint paint) {
		float length = 0;
		StringBuffer buffer = new StringBuffer("");
		for (int i = 0; i < content.length(); i++) {
			length = paint.measureText(buffer.toString() + content.charAt(i)
					+ "...");
			if (length < maxLength) {
				buffer.append(content.charAt(i));
			} else {
				break;
			}
		}
		buffer.append("...");
		return buffer.toString();
	}
	
	// --------------------------
	private int countAutoConnect = -1;
	private Timer timerAutoConnect = null;

	public void StartTimerAutoConnect() {
		if (countAutoConnect != -1) {
			return;
		}

		MyLog.e("TEST TEST", "StartTimerAutoConnect FIRST TIME");

		if (timerAutoConnect != null) {
			timerAutoConnect.cancel();
			timerAutoConnect = null;
		}
		timerAutoConnect = new Timer();
		timerAutoConnect.schedule(new TimerTask() {

			@Override
			public void run() {

				countAutoConnect++;

				if (countAutoConnect > 4) {
					countAutoConnect = 0;
				}

				handlerInvalidate.sendEmptyMessage(0);
			}
		}, 50, 500);
	}

	public void StopTimerAutoConnect() {
		if (timerAutoConnect != null) {
			timerAutoConnect.cancel();
			timerAutoConnect = null;
		}

		countAutoConnect = -1;
		invalidate();
	}
	
	private Handler handlerInvalidate = new Handler(){
		public void handleMessage(Message msg) {
			invalidate();
		};
	};
	
}
