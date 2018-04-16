package vn.com.sonca.SetttingApp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.params.Song;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class AppInfoView extends View {

	private String defVersion = "1.6.0";
	private String defHotLine = "839107612";
	private String defHotLineStr = "(08) 3910 7612";
	private String defHotLine2 = "0901373798";
	private String defWebsite = "www.soncamedia.com";
	private String defEmail = "sonca@sonca.com.vn";
	private String defComName = "";
	private String defComAddress = "";
	private String defDataVersion = "DISC V5601";

	private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint italicPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private int widthLayout = 300;
	private int heightLayout = 300;
	private Drawable drawable;

	private Context context;

	public AppInfoView(Context context) {
		super(context);
		initView(context);
	}

	public AppInfoView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public AppInfoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private Drawable drawUpdate, drawUpdateHover;

	private void initView(Context context) {
		this.context = context;
		drawUpdate = getResources().getDrawable(R.drawable.off_boder_vuong);
		drawUpdateHover = getResources().getDrawable(
				R.drawable.cham_boder_vuong);
			
		defComName = getResources().getString(R.string.info_4);
		defComAddress = getResources().getString(R.string.info_5);

		String appVersion = "";
		try {
			PackageInfo pInfo = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			appVersion = pInfo.versionName;
		} catch (Exception e) {
			appVersion = "";
		}

		//MyLog.e("defVersion", defVersion);
		if (appVersion.length() > 0) {
			defVersion = appVersion;
		}
	 
	}

	public void setLayoutUpdateData(String defDataVersion) {
		this.defDataVersion = defDataVersion;
	}

	private boolean flagUpdateData = false;
	public void setBooleanUpdateData(boolean flag){
		MyLog.e("AppInfoView", "setBooleanUpdateData = " + flag);
		this.flagUpdateData = flag;
		invalidate();
	}
	
	private boolean flagUpdateApp = false;	
	public void setLayoutUpdateApp() {
		this.flagUpdateApp = true;
		invalidate();
	}

	private OnAppInfoViewListener listener;

	public interface OnAppInfoViewListener {
		public void OnBack();

		public void OnStore();

		public void OnHotLine(String strHotLine);

		public void OnWebsite(String strWebsite);

		public void OnEmail(String strEmail);

		public void OnUpdateApp();
		
		public void OnUpdateInfoData();
		
		public void OnResetPackageDB();
	}

	public void setOnAppInfoViewListener(OnAppInfoViewListener listener) {
		this.listener = listener;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int myWidth = widthMeasureSpec;
		setMeasuredDimension(myWidth, heightMeasureSpec);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		setK(w, h);
	}

	private Rect rectDanhGia, rectHotline, rectWebsite, rectEmail;

	private int color1;
	private int color2;
	private int color3;
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		setK(getWidth(), getHeight());

		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			color1 = Color.argb(255, 180, 254, 255);
			color2 = Color.argb(255, 255, 255, 255);
			color3 = Color.argb(255, 180, 254, 255);
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			color1 = Color.parseColor("#21BAA9");
			color2 = Color.parseColor("#21BAA9");
			color3 = Color.parseColor("#21BAA9");
		}
		
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){		
			// ---------THONG TIN UNG DUNG---------//
			String text = "";
			float textWidth;
			
			drawable = getResources().getDrawable(R.drawable.info_acnos_icon);
			drawable.setBounds(KD1L, KD1T, KD1R, KD1B);
			drawable.draw(canvas);

			mainPaint.setTextSize(KT1);
			mainPaint.setColor(color1);
			text = "Karaoke Connect";
			canvas.drawText(text, KT3X, KT3Y, mainPaint);

			mainPaint.setTextSize(NT1S);
			mainPaint.setARGB(255, 115, 167, 174);
			text = getResources().getString(R.string.info_2);
			canvas.drawText(text, KT3X, NTY1, mainPaint);
						
			italicPaint.setTextSize(KT4);
			italicPaint.setTextSkewX(-0.2f);
			italicPaint.setARGB(255, 13, 122, 255);
			if(hoverUpdate1){
				italicPaint.setColor(Color.GREEN);
			}
			text = defVersion;
			KD4L = KT4X + italicPaint.measureText(text);
			KD4B = KT4Y;
			KD4T = KT4Y - KT4;
			canvas.drawText(text, KT4X, KT4Y, italicPaint);
			
			if (flagUpdateApp) {
				text = " " + getResources().getString(R.string.info_upd_1);
				KD4R = KD4L + italicPaint.measureText(text);
				canvas.drawText(text, KD4L, KT4Y, italicPaint);
			}
			
			// PHIEN BAN DU LIEU---------//
			mainPaint.setTextSize(NT1S);
			mainPaint.setARGB(255, 115, 167, 174);
			text = getResources().getString(R.string.info_7);
			canvas.drawText(text, KT3X, NTY2, mainPaint);
			
			italicPaint.setTextSize(KT4);
			italicPaint.setARGB(255, 13, 122, 255);
			if(hoverUpdate2){
				italicPaint.setColor(Color.GREEN);
			}
			text = defDataVersion;
			KD5L = KT4X_A + italicPaint.measureText(text);
			KD5B = KT4Y_A;
			KD5T = KT4Y_A - KT4;
			canvas.drawText(text, KT4X_A, KT4Y_A, italicPaint);
			
			if (flagUpdateData) {
				text = " " + getResources().getString(R.string.info_upd_1);
				KD5R = KD5L + italicPaint.measureText(text);
				canvas.drawText(text, KD5L, KT4Y_A, italicPaint);
			}
			
			// DANH GIA UNG DUNG---------//
			italicPaint.setTextSize(KT5);
			italicPaint.setARGB(255, 13, 122, 255);
			if(hoverUpdate3){
				italicPaint.setColor(Color.GREEN);
			}
			text = getResources().getString(R.string.info_upd_2);
			textWidth = italicPaint.measureText(text);
			canvas.drawText(text, KT5X, KT5Y, italicPaint);
			
			rectDanhGia = new Rect((int) KT5X, (int) (KT5Y - 2 * KT5 / 3),
					(int) (KT5X + textWidth), (int) (KT5Y + 3 * heightLayout / 800));
			
			
			// ---------THONG TIN LIEN HE---------//
			mainPaint.setTextSize(KT1);
			mainPaint.setColor(color1);
			text = defComName;
			canvas.drawText(text, KT6X, KT6Y, mainPaint);

			mainPaint.setTextSize(KT4);
			mainPaint.setARGB(255, 115, 167, 174);
			text = defComAddress;
			canvas.drawText(text, KT7X, KT7Y, mainPaint);

			mainPaint.setTextSize(KT4);
			mainPaint.setARGB(255, 115, 167, 174);
			text = getResources().getString(R.string.info_6);
			canvas.drawText(text, KT8X, KT8Y, mainPaint);
			// ---------HOT LINE---------//
			mainPaint.setTextSize(KT2);
			mainPaint.setARGB(255, 115, 167, 174);
			text = "Phone    :";
			textWidth = mainPaint.measureText(text);
			canvas.drawText(text, KT9X, KT9Y, mainPaint);

			italicPaint.setTextSize(KT2);
			italicPaint.setColor(color3);
			if(hover1){
				italicPaint.setColor(Color.GREEN);
			}
			text = defHotLineStr;
			float startX = KT9X + textWidth + 7 * widthLayout / 480;
			textWidth = italicPaint.measureText(text);
			canvas.drawText(text, startX, KT9Y, italicPaint);
			rectHotline = new Rect((int) startX, (int) (KT9Y - KT2),
					(int) (startX + textWidth),
					(int) (KT9Y + 3 * heightLayout / 800));
			// ---------WEBSITE---------//
			mainPaint.setTextSize(KT2);
			mainPaint.setARGB(255, 115, 167, 174);
			text = "Website :";
			textWidth = mainPaint.measureText(text);
			canvas.drawText(text, KT10X, KT10Y, mainPaint);

			italicPaint.setTextSize(KT2);
			italicPaint.setColor(color3);
			if(hover2){
				italicPaint.setColor(Color.GREEN);
			}
			text = defWebsite;
			startX = KT10X + textWidth + 7 * widthLayout / 480;
			textWidth = italicPaint.measureText(text);
			canvas.drawText(text, startX, KT10Y, italicPaint);
			canvas.drawLine(startX, KT10Y + 3 * heightLayout / 800, startX
					+ textWidth, KT10Y + 3 * heightLayout / 800, italicPaint);
			rectWebsite = new Rect((int) startX, (int) (KT10Y - KT2),
					(int) (startX + textWidth),
					(int) (KT10Y + 3 * heightLayout / 800));
			// ---------EMAIL---------//
			mainPaint.setTextSize(KT2);
			mainPaint.setARGB(255, 115, 167, 174);
			text = "Email     :";
			textWidth = mainPaint.measureText(text);
			canvas.drawText(text, KT11X, KT11Y, mainPaint);

			italicPaint.setTextSize(KT2);
			italicPaint.setColor(color3);
			if(hover3){
				italicPaint.setColor(Color.GREEN);
			}
			text = defEmail;
			startX = KT11X + textWidth + 7 * widthLayout / 480;
			textWidth = italicPaint.measureText(text);
			canvas.drawText(text, startX, KT11Y, italicPaint);
			rectEmail = new Rect((int) startX, (int) (KT11Y - KT2),
					(int) (startX + textWidth),
					(int) (KT11Y + 3 * heightLayout / 800));
			// ---------KHOI PHUC DU LIEU---------//
//			mainPaint.setTextSize(KT7);
//			mainPaint.setColor(color2);
//			text = "* " + getResources().getString(R.string.info_upd_2);
//			
//			KD6L = KT12X + mainPaint.measureText(text);
//			KD6B = KT12Y;
//			KD6T = KT12Y - KT7;
//			canvas.drawText(text, KT11X, KT12Y, mainPaint);
//			
//			italicPaint.setTextSize(KT7);
//			italicPaint.setARGB(255, 13, 122, 255);
//			if(hoverUpdate4){
//				italicPaint.setColor(Color.GREEN);
//			}
//			text = " " + getResources().getString(R.string.info_upd_3);
//			KD6R = KD6L + italicPaint.measureText(text);
//			canvas.drawText(text, KD6L, KT12Y, italicPaint);
			
			// ---------ICON HOTLINE---------//
			drawable = getResources().getDrawable(R.drawable.hotline);
			drawable.setBounds(KD2L, KD2T, KD2R, KD2B);
			drawable.draw(canvas);
		} else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){		
			// ---------THONG TIN UNG DUNG---------//
			String text = "";
			float textWidth;
			
			drawable = getResources().getDrawable(R.drawable.info_acnos_icon);
			drawable.setBounds(KD1L, KD1T, KD1R, KD1B);
			drawable.draw(canvas);

			mainPaint.setTextSize(KT1);
			mainPaint.setColor(color1);
			text = "Karaoke Connect";
			canvas.drawText(text, KT3X, KT3Y, mainPaint);

			mainPaint.setTextSize(NT1S);
			mainPaint.setARGB(255, 115, 167, 174);
			text = getResources().getString(R.string.info_2);
			canvas.drawText(text, KT3X, NTY1, mainPaint);
						
			italicPaint.setTextSize(KT4);
			italicPaint.setTextSkewX(-0.2f);
			italicPaint.setARGB(255, 13, 122, 255);
			if(hoverUpdate1){
				italicPaint.setColor(Color.GREEN);
			}
			text = defVersion;
			KD4L = KT4X + italicPaint.measureText(text);
			KD4B = KT4Y;
			KD4T = KT4Y - KT4;
			canvas.drawText(text, KT4X, KT4Y, italicPaint);
			
			if (flagUpdateApp) {
				text = " " + getResources().getString(R.string.info_upd_1);
				KD4R = KD4L + italicPaint.measureText(text);
				canvas.drawText(text, KD4L, KT4Y, italicPaint);
			}
			
			// PHIEN BAN DU LIEU---------//
			mainPaint.setTextSize(NT1S);
			mainPaint.setARGB(255, 115, 167, 174);
			text = getResources().getString(R.string.info_7);
			canvas.drawText(text, KT3X, NTY2, mainPaint);
			
			italicPaint.setTextSize(KT4);
			italicPaint.setARGB(255, 13, 122, 255);
			if(hoverUpdate2){
				italicPaint.setColor(Color.GREEN);
			}
			text = defDataVersion;
			KD5L = KT4X_A + italicPaint.measureText(text);
			KD5B = KT4Y_A;
			KD5T = KT4Y_A - KT4;
			canvas.drawText(text, KT4X_A, KT4Y_A, italicPaint);
			
			if (flagUpdateData) {
				text = " " + getResources().getString(R.string.info_upd_1);
				KD5R = KD5L + italicPaint.measureText(text);
				canvas.drawText(text, KD5L, KT4Y_A, italicPaint);
			}
			
			// DANH GIA UNG DUNG---------//
			italicPaint.setTextSize(KT5);
			italicPaint.setARGB(255, 13, 122, 255);
			if(hoverUpdate3){
				italicPaint.setColor(Color.GREEN);
			}
			text = getResources().getString(R.string.info_upd_2);
			textWidth = italicPaint.measureText(text);
			canvas.drawText(text, KT5X, KT5Y, italicPaint);
			
			rectDanhGia = new Rect((int) KT5X, (int) (KT5Y - 2 * KT5 / 3),
					(int) (KT5X + textWidth), (int) (KT5Y + 3 * heightLayout / 800));
			
			
			// ---------THONG TIN LIEN HE---------//
			mainPaint.setTextSize(KT1);
			mainPaint.setColor(color1);
			text = defComName;
			canvas.drawText(text, KT6X, KT6Y, mainPaint);

			mainPaint.setTextSize(KT4);
			mainPaint.setARGB(255, 115, 167, 174);
			text = defComAddress;
			canvas.drawText(text, KT7X, KT7Y, mainPaint);

			mainPaint.setTextSize(KT4);
			mainPaint.setARGB(255, 115, 167, 174);
			text = getResources().getString(R.string.info_6);
			canvas.drawText(text, KT8X, KT8Y, mainPaint);
			// ---------HOT LINE---------//
			mainPaint.setTextSize(KT2);
			mainPaint.setARGB(255, 115, 167, 174);
			text = "Phone    :";
			textWidth = mainPaint.measureText(text);
			canvas.drawText(text, KT9X, KT9Y, mainPaint);

			italicPaint.setTextSize(KT2);
			italicPaint.setColor(color3);
			if(hover1){
				italicPaint.setColor(Color.GREEN);
			}
			text = defHotLineStr;
			float startX = KT9X + textWidth + 7 * widthLayout / 480;
			textWidth = italicPaint.measureText(text);
			canvas.drawText(text, startX, KT9Y, italicPaint);
			rectHotline = new Rect((int) startX, (int) (KT9Y - KT2),
					(int) (startX + textWidth),
					(int) (KT9Y + 3 * heightLayout / 800));
			// ---------WEBSITE---------//
			mainPaint.setTextSize(KT2);
			mainPaint.setARGB(255, 115, 167, 174);
			text = "Website :";
			textWidth = mainPaint.measureText(text);
			canvas.drawText(text, KT10X, KT10Y, mainPaint);

			italicPaint.setTextSize(KT2);
			italicPaint.setColor(color3);
			if(hover2){
				italicPaint.setColor(Color.GREEN);
			}
			text = defWebsite;
			startX = KT10X + textWidth + 7 * widthLayout / 480;
			textWidth = italicPaint.measureText(text);
			canvas.drawText(text, startX, KT10Y, italicPaint);
			canvas.drawLine(startX, KT10Y + 3 * heightLayout / 800, startX
					+ textWidth, KT10Y + 3 * heightLayout / 800, italicPaint);
			rectWebsite = new Rect((int) startX, (int) (KT10Y - KT2),
					(int) (startX + textWidth),
					(int) (KT10Y + 3 * heightLayout / 800));
			// ---------EMAIL---------//
			mainPaint.setTextSize(KT2);
			mainPaint.setARGB(255, 115, 167, 174);
			text = "Email     :";
			textWidth = mainPaint.measureText(text);
			canvas.drawText(text, KT11X, KT11Y, mainPaint);

			italicPaint.setTextSize(KT2);
			italicPaint.setColor(color3);
			if(hover3){
				italicPaint.setColor(Color.GREEN);
			}
			text = defEmail;
			startX = KT11X + textWidth + 7 * widthLayout / 480;
			textWidth = italicPaint.measureText(text);
			canvas.drawText(text, startX, KT11Y, italicPaint);
			rectEmail = new Rect((int) startX, (int) (KT11Y - KT2),
					(int) (startX + textWidth),
					(int) (KT11Y + 3 * heightLayout / 800));
			// ---------KHOI PHUC DU LIEU---------//
//			mainPaint.setTextSize(KT7);
//			mainPaint.setColor(color2);
//			text = "* " + getResources().getString(R.string.info_upd_2);
//			
//			KD6L = KT12X + mainPaint.measureText(text);
//			KD6B = KT12Y;
//			KD6T = KT12Y - KT7;
//			canvas.drawText(text, KT11X, KT12Y, mainPaint);
//			
//			italicPaint.setTextSize(KT7);
//			italicPaint.setARGB(255, 13, 122, 255);
//			if(hoverUpdate4){
//				italicPaint.setColor(Color.GREEN);
//			}
//			text = " " + getResources().getString(R.string.info_upd_3);
//			KD6R = KD6L + italicPaint.measureText(text);
//			canvas.drawText(text, KD6L, KT12Y, italicPaint);
			
			// ---------ICON HOTLINE---------//
			drawable = getResources().getDrawable(R.drawable.hotline);
			drawable.setBounds(KD2L, KD2T, KD2R, KD2B);
			drawable.draw(canvas);
		}	
		
	}

	private boolean hoverUpdate1 = false;
	private boolean hoverUpdate2 = false;	
	private boolean hoverUpdate3 = false;
	private boolean hoverUpdate4 = false;
	
	private boolean hover1 = false;
	private boolean hover2 = false;	
	private boolean hover3 = false;
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			hoverUpdate1 = false;
			hoverUpdate2 = false;
			hoverUpdate3 = false;
			hoverUpdate4 = false;
			hover1 = false;
			hover2 = false;
			hover3 = false;
			
			float x = event.getX();
			float y = event.getY();
			if (x >= KT4X && x <= KD4R && y >= KD4T && y <= KD4B
					&& flagUpdateApp) {
				hoverUpdate1 = true;
				invalidate();
			}			
			if (x >= KT4X_A && x <= KD5R && y >= KD5T && y <= KD5B
					&& flagUpdateData) {
				hoverUpdate2 = true;
				invalidate();
			}
//			if (x >= KT12X && x <= KD6R && y >= KD6T && y <= KD6B) {
//				hoverUpdate4 = true;
//				invalidate();
//			}	
			if (x >= rectDanhGia.left && x <= rectDanhGia.right
					&& y >= rectDanhGia.top && y <= rectDanhGia.bottom) {
				hoverUpdate3 = true;
				invalidate();
			}
//			if (x >= rectHotline.left && x <= rectHotline.right
//					&& y >= rectHotline.top && y <= rectHotline.bottom) {
//				hover1 = true;
//				invalidate();
//			}
//			if (x >= rectWebsite.left && x <= rectWebsite.right
//					&& y >= rectWebsite.top && y <= rectWebsite.bottom) {
//				hover2 = true;
//				invalidate();
//			}
//			if (x >= rectEmail.left && x <= rectEmail.right
//					&& y >= rectEmail.top && y <= rectEmail.bottom) {
//				hover3 = true;
//				invalidate();
//			}
		}
			break;
		case MotionEvent.ACTION_UP: {
			hoverUpdate1 = false;
			hoverUpdate2 = false;
			hoverUpdate3 = false;
			hoverUpdate4 = false;
			hover1 = false;
			hover2 = false;
			hover3 = false;
			
			float x = event.getX();
			float y = event.getY();
			if (x >= KD0L && x <= (KD0R * 3) && y >= KD0T && y <= KD0B) {
				if (listener != null) {
					listener.OnBack();
				}
			}
			if (x >= rectDanhGia.left && x <= rectDanhGia.right
					&& y >= rectDanhGia.top && y <= rectDanhGia.bottom) {
				if (listener != null) {
					listener.OnResetPackageDB();
				}
			}
			if (x >= rectHotline.left && x <= rectHotline.right
					&& y >= rectHotline.top && y <= rectHotline.bottom) {
				if (listener != null) {
					listener.OnHotLine(defHotLine);
				}
			}
			if (x >= rectWebsite.left && x <= rectWebsite.right
					&& y >= rectWebsite.top && y <= rectWebsite.bottom) {
				if (listener != null) {
					listener.OnWebsite(defWebsite);
				}
			}
			if (x >= rectEmail.left && x <= rectEmail.right
					&& y >= rectEmail.top && y <= rectEmail.bottom) {
				if (listener != null) {
					listener.OnEmail(defEmail);
				}
			}
			if (x >= KD2L && x <= KD2R && y >= KD2T && y <= KD2B) {
				if (listener != null) {
					listener.OnHotLine(defHotLine2);
				}
			}
			if (x >= KT4X && x <= KD4R && y >= KD4T && y <= KD4B
					&& flagUpdateApp) {
				if (listener != null) {
					listener.OnUpdateApp();
				}
			}			
			if (x >= KT4X_A && x <= KD5R && y >= KD5T && y <= KD5B
					&& flagUpdateData) {
				if (listener != null) {
					listener.OnUpdateInfoData();
				}
			}
			if (x >= KT12X && x <= KD6R && y >= KD6T && y <= KD6B) {
				if (listener != null) {
					listener.OnResetPackageDB();
				}
			}	
		}
			invalidate();
			break;
		default:
			break;
		}

		return true;
	}

	// //////////////////////////////////////////////////////////////////

	private float KG;
	private float KT1, KT1Y, KT3X, KT3Y;
	private int KG1L, KG1R, KG1T, KG1B;
	private int KG2L, KG2R, KG2T, KG2B;
	private int KD0L, KD0R, KD0T, KD0B;
	private float KT2, KT2X, KT2Y, KT6X, KT6Y, KT9X, KT9Y, KT10X, KT10Y, KT11X,
			KT11Y, KT12X, KT12Y;
	private int KD1L, KD1R, KD1T, KD1B;
	private float KT4, KT4X, KT4Y, KT8X, KT8Y;
	private float KT4X_A, KT4Y_A;
	private float KT5, KT5X, KT5Y;
	private float KT7, KT7X, KT7Y;
	private int KD2L, KD2R, KD2T, KD2B;
	private int KD3L, KD3R, KD3T, KD3B;
	private float KTU, KTUX, KTUY;
	private float KD4L, KD4R, KD4T, KD4B;
	private float KD5L, KD5R, KD5T, KD5B;
	private float KD6L, KD6R, KD6T, KD6B;
	
	private float NT1S, NTY1, NTY2;

	private void setK(int w, int h) {
		widthLayout = w;
		heightLayout = h;

		KG = 3 * h / 800;

		KT1 = 40 * h / 800;
		KT1Y = 52 * h / 800;

		KG1L = 0;
		KG1R = w;
		KG1T = 0;
		KG1B = 3 * h / 800;

		KG2L = 0;
		KG2R = w;
		KG2T = 80 * h / 800;
		KG2B = 83 * h / 800;

		KD0L = 15 * w / 480;
		KD0R = 40 * w / 480;
		KD0T = 10 * h / 800;
		KD0B = 65 * h / 800;

		KT2 = 35 * h / 800;
		KT2Y = 130 * h / 800;
		KT2X = 10 * w / 480;

		KD1L = 20 * w / 480;
		KD1R = KD1L + 180 * h / 800;
		KD1T = 10 * h / 800; 
		KD1B = KD1T + 180 * h / 800;

		KT3Y = 40 * h / 800;
		KT3X = KD1R + 10 * w / 480;

		KT4 = 28 * h / 800;
		KT4Y = 120 * h / 800;
		KT4X = KD1R + 10 * w / 480;
		
		KT4Y_A = 195 * h / 800;
		KT4X_A = KD1R + 10 * w / 480;

		KTUX = KD1R + 10 * w / 480;

		KT5 = 35 * h / 800;
		KT5Y = 240 * h / 800;
		KT5X = 20 * w / 480;

		KT6Y = 320 * h / 800;
		KT6X = 20 * w / 480;

		KT7 = 35 * h / 800;
		KT7Y = 365 * h / 800;
		KT7X = 20 * w / 480;

		KT8Y = 400 * h / 800;
		KT8X = 20 * w / 480;

		KT9Y = 445 * h / 800;
		KT9X = 20 * w / 480;

		KT11Y = 490 * h / 800;
		KT11X = 20 * w / 480;

		KT10Y = 535 * h / 800;
		KT10X = 20 * w / 480;
		
		KT12Y = 580 * h / 800;
		KT12X = 20 * w / 480;

		KD2L = 40 * w / 480;
		KD2R = widthLayout - 40 * w / 480;
		KD2T = (int)KT12Y + 50 * h / 800;
		KD2B = heightLayout - 10 * h / 800;

		KD3L = 140 * w / 480;
		KD3R = KD3L + 170 * w / 480;
		KD3B = KD2T - 30 * h / 800;
		KD3T = KD3B - 54 * h / 800;
		
		NT1S = 35 * h / 800;
		NTY1 = 85 * h / 800;
		NTY2 = 160 * h / 800;
	}
}
