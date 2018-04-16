package vn.com.sonca.Touch.Hi_W;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.zzzzz.MyApplication;

public class InforFirmwareView extends View {
	
	private Paint mainText = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public InforFirmwareView(Context context) {
		super(context);
		initView(context);
	}

	public InforFirmwareView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public InforFirmwareView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private OnInForFirwaveListener listener;
	public interface OnInForFirwaveListener{
		public void OnDownloadFirwaveFromServer();
		public void OnUpdateFirwaveFromWiFi();
	}
	public void setOnInForFirwaveListener(OnInForFirwaveListener listener){
		this.listener = listener;
	}
	
	public void setFirmwareData(HiW_FirmwareInfo firmwareInfo){
		if(firmwareInfo.getDaumay_version() < 100){
			data1 = firmwareInfo.getDaumay_name() + " - V" + firmwareInfo.getDaumay_version() + ".0";
		} else {
			data1 = firmwareInfo.getDaumay_name() + " - V" + String.format("%.2f", firmwareInfo.getDaumay_version() / 100f).replace(",", ".");	
		}
		
		if(MyApplication.intWifiRemote == MyApplication.SONCA_KARTROL){
			data1 = "-";
		}
		
		data2 = "V" + String.format("%.2f", firmwareInfo.getWifi_version() / 100f).replace(",", ".") + " - REV" + firmwareInfo.getWifi_revision();
		invalidate();
	}
	
	public void setVersionHDD(String version){
		data3 = version;
		
		if(MyApplication.intWifiRemote == MyApplication.SONCA_KARTROL){
			data3 = "-";
		}
		
		invalidate();
	}
	
	private String title1 = "";
	private String title2 = "";
	private String title3 = "";
	private String data1 = ""; //SK8030KTV-W.bin - V1.0
	private String data2 = ""; //V1.0 - REV1234
	private String data3 = "";
	
	private String butLeftTop = "";
	private String butLeftBottom = "";
	private String butRightTop = "";
	private String butRightBottom = "";
	
	private Drawable drawFocusNO;
	private Drawable drawFocusOK;

	private Drawable zlightdrawFocusNO;
	private Drawable zlightdrawFocusOK;
	
	private void initView(Context context) {
		drawFocusNO = getResources().getDrawable(R.drawable.boder_lammoi);
		drawFocusOK = getResources().getDrawable(R.drawable.boder_lammoi_hover);
		
		zlightdrawFocusNO = getResources().getDrawable(R.drawable.zlight_boder_ketnoi);
		zlightdrawFocusOK = getResources().getDrawable(R.drawable.zlight_boder_ketnoi_hover);		
		
		title1 = getResources().getString(R.string.hiw_text_left_1);
		title2 = getResources().getString(R.string.hiw_text_left_2);
		title3 = getResources().getString(R.string.hiw_text_left_3);
		butLeftTop = getResources().getString(R.string.hiw_but_left_1);
		butLeftBottom = getResources().getString(R.string.hiw_but_left_2);
		butRightTop = getResources().getString(R.string.hiw_but_left_3);
		butRightBottom = getResources().getString(R.string.hiw_but_left_4);
		
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = (int) (0.8*width);
		setMeasuredDimension(width, height);
	}
	
	private int data1X, data1Y, data1S;
	private int data2X, data2Y, data2S;
	private int data3X, data3Y, data3S;
	private int title1X, title1Y, title1S;
	private int title2X, title2Y, title2S;
	private int title3X, title3Y, title3S;
	private Rect rectButLeft = new Rect();
	private Rect rectButRight = new Rect();
	private int but1X, but1Y, but2X, but2Y, butS;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		butS = (int) (0.07*h);
		int hr = (int) (0.10*h);
		int wr = (int) (2.5*hr);
		int tamY = (int) (0.75*h);
		int tamX = (int) (0.29*w);
		but1X = tamX;
		rectButLeft.set(tamX - wr, tamY - hr, tamX + wr, tamY + hr);
		tamX = (int) (0.71*w);
		but2X = tamX;
		rectButRight.set(tamX - wr, tamY - hr, tamX + wr, tamY + hr);
		but1Y = (int) (tamY - 0.2*butS);
		but2Y = (int) (tamY + 0.9*butS);
		
		title1S = (int) (0.065*h);
		title1X = (int) (0.17*h);
		title1Y = (int) (0.1*h);
		
		title2S = title1S;
		title2X = title1X;
		title2Y = (int) (0.29*h);
		
		title3S = title1S;
		title3X = title1X;
		title3Y = (int) (0.48*h);
		
		data1S = title1S;
		data1X = title1X;
		data1Y = (int) (0.19*h);
		
		data2S = title1S;
		data2X = data1X;
		data2Y = (int) (0.38*h);
		
		data3S = title1S;
		data3X = data1X;
		data3Y = (int) (0.57*h);
		
	}
	
	private int color_01;
	private int color_02;
	private int color_03;
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		// MyApplication.intColorScreen = MyApplication.SCREEN_LIGHT;
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			color_01 = Color.argb(255, 180, 253, 253);
			color_02 = Color.GREEN;
			color_03 = Color.argb(255, 0, 253, 253);
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			color_01 = Color.parseColor("#005249");
			color_02 = Color.parseColor("#21BAA9");
			color_03 = Color.parseColor("#FFFFFF");
		}

		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			if (stateButton == 1) {
				drawFocusOK.setBounds(rectButLeft);
				drawFocusOK.draw(canvas);
			} else {
				drawFocusNO.setBounds(rectButLeft);
				drawFocusNO.draw(canvas);
			}
			if (stateButton == 2) {
				drawFocusOK.setBounds(rectButRight);
				drawFocusOK.draw(canvas);
			} else {
				drawFocusNO.setBounds(rectButRight);
				drawFocusNO.draw(canvas);
			}
			mainText.setStyle(Style.FILL);
			mainText.setColor(color_01);
			mainText.setTextSize(title1S);
			canvas.drawText(title1, title1X, title1Y, mainText);
			canvas.drawText(title2, title2X, title2Y, mainText);
			canvas.drawText(title3, title3X, title3Y, mainText);

			mainText.setColor(color_02);
			mainText.setTextSize(data1S);
			canvas.drawText(data1, data1X, data1Y, mainText);
			canvas.drawText(data2, data2X, data2Y, mainText);
			canvas.drawText(data3, data3X, data3Y, mainText);

			mainText.setColor(color_03);
			mainText.setTextSize(butS);
			float xx = (float) (but1X - 0.5 * mainText.measureText(butLeftTop));
			canvas.drawText(butLeftTop, xx, but1Y, mainText);
			xx = (float) (but1X - 0.5 * mainText.measureText(butLeftBottom));
			canvas.drawText(butLeftBottom, xx, but2Y, mainText);
			xx = (float) (but2X - 0.5 * mainText.measureText(butRightTop));
			canvas.drawText(butRightTop, xx, but1Y, mainText);
			xx = (float) (but2X - 0.5 * mainText.measureText(butRightBottom));
			canvas.drawText(butRightBottom, xx, but2Y, mainText);
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			if (stateButton == 1) {
				zlightdrawFocusOK.setBounds(rectButLeft);
				zlightdrawFocusOK.draw(canvas);
			} else {
				zlightdrawFocusNO.setBounds(rectButLeft);
				zlightdrawFocusNO.draw(canvas);
			}
			if (stateButton == 2) {
				zlightdrawFocusOK.setBounds(rectButRight);
				zlightdrawFocusOK.draw(canvas);
			} else {
				zlightdrawFocusNO.setBounds(rectButRight);
				zlightdrawFocusNO.draw(canvas);
			}
			mainText.setStyle(Style.FILL);
			mainText.setColor(color_01);
			mainText.setTextSize(title1S);
			canvas.drawText(title1, title1X, title1Y, mainText);
			canvas.drawText(title2, title2X, title2Y, mainText);
			canvas.drawText(title3, title3X, title3Y, mainText);

			mainText.setColor(color_02);
			mainText.setTextSize(data1S);
			canvas.drawText(data1, data1X, data1Y, mainText);
			canvas.drawText(data2, data2X, data2Y, mainText);
			canvas.drawText(data3, data3X, data3Y, mainText);

			mainText.setColor(color_03);
			mainText.setTextSize(butS);
			float xx = (float) (but1X - 0.5 * mainText.measureText(butLeftTop));
			canvas.drawText(butLeftTop, xx, but1Y, mainText);
			xx = (float) (but1X - 0.5 * mainText.measureText(butLeftBottom));
			canvas.drawText(butLeftBottom, xx, but2Y, mainText);
			xx = (float) (but2X - 0.5 * mainText.measureText(butRightTop));
			canvas.drawText(butRightTop, xx, but1Y, mainText);
			xx = (float) (but2X - 0.5 * mainText.measureText(butRightBottom));
			canvas.drawText(butRightBottom, xx, but2Y, mainText);
		}
	}
	
	private int stateButton = 0;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:{
			float x = event.getX();
			float y = event.getY();
			if(rectButLeft != null){
				if(x >= rectButLeft.left && x <= rectButLeft.right &&
					y >= rectButLeft.top && y <= rectButLeft.bottom){
					stateButton = 1;
					invalidate();
					break;
				}
			}
			if(rectButRight != null){
				if(x >= rectButRight.left && x <= rectButRight.right &&
					y >= rectButRight.top && y <= rectButRight.bottom){
					stateButton = 2;
					invalidate();
					break;
				}
			}
			stateButton = 0;
			invalidate();
		}break;
		case MotionEvent.ACTION_MOVE:{
			float x = event.getX();
			float y = event.getY();
			if(rectButLeft != null){
				if(x >= rectButLeft.left && x <= rectButLeft.right &&
					y >= rectButLeft.top && y <= rectButLeft.bottom){
					stateButton = 1;
					invalidate();
					break;
				}
			}
			if(rectButRight != null){
				if(x >= rectButRight.left && x <= rectButRight.right &&
					y >= rectButRight.top && y <= rectButRight.bottom){
					stateButton = 2;
					invalidate();
					break;
				}
			}
			stateButton = 0;
			invalidate();
		}break;
		case MotionEvent.ACTION_UP:
			stateButton = 0;
			invalidate();
			float x = event.getX();
			float y = event.getY();
			if(rectButLeft != null){
				if(x >= rectButLeft.left && x <= rectButLeft.right &&
					y >= rectButLeft.top && y <= rectButLeft.bottom){
					if(listener != null){
						listener.OnDownloadFirwaveFromServer();
					}
				}
			}
			if(rectButRight != null){
				if(x >= rectButRight.left && x <= rectButRight.right &&
					y >= rectButRight.top && y <= rectButRight.bottom){
					if(listener != null){
						listener.OnUpdateFirwaveFromWiFi();
					}
				}
			}
			break;
		default:
			break;
		}
		return true;
	}
	
	
	
	
}
