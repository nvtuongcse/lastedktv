package vn.com.sonca.Touch.CustomView;

import java.util.ArrayList;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.zzzzz.MyApplication;
import android.R.integer;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;

public class SliderView extends View {
	private int typeDeviceConnect = 0; // test - khong xai nua, luon giu 0
	private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Path mainPath;
	private Drawable drawable = null;
	private int widthLayout = 350;
	private int heightLayout = 350;

	private Context context;

	public static final int SLIDER_TYPE0 = 0; // from 0 to x
	public static final int SLIDER_TYPE1 = 1; // from -x to +x
	private int sliderType = SLIDER_TYPE0;

	public static final int SENDER_MELODY = 0;
	public static final int SENDER_TEMPO = 1;
	public static final int SENDER_KEY = 2;
	public static final int SENDER_VOLUMN = 3;
	private int senderType = SENDER_VOLUMN;

	private int intSliderVolumn = 0;
	private boolean isDisplay = true;

	private ArrayList<Rect> sliderMelody;
	private ArrayList<Rect> sliderTempo;
	private ArrayList<Rect> sliderKey;
	private ArrayList<Rect> sliderVolumn;
	private ArrayList<Rect> sliderItems = sliderMelody;

	public SliderView(Context context) {
		super(context);
		this.context = context;
		initView();
	}

	public SliderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
	}

	public SliderView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initView();
	}

	public void setDefaultValue() {
		if (senderType == SENDER_MELODY || senderType == SENDER_TEMPO
				|| senderType == SENDER_KEY) {
			intSliderVolumn = 0;
		} else {
			intSliderVolumn = 8;
		}
		invalidate();
	}

	public void setVolumn(int volumn) {
		intSliderVolumn = volumn;
		oldSliderValue = volumn;
		invalidate();
	}

	public int getMainVolumn() {
		return this.intSliderVolumn;
	}

	public int getSaveVolumn() {
		return this.oldSliderValue;
	}

	private OnSliderListener listener;

	public interface OnSliderListener {
		public void OnSlider(int senderType, int value);

		public void OnSliderIncrease();

		public void OnSliderDecrease();
	}

	public void setOnSliderListener(OnSliderListener listener) {
		this.listener = listener;
	}

	public void forceInitView() {
		initView();
		invalidate();
	}
	
	private Drawable drawThangIn, drawThangAc;
	private Drawable drawGiangIn, drawGiangAc;
	private Drawable drawBinhIn, drawBinhAc;
	
	private Drawable zlightdrawThangIn, zlightdrawThangAc;
	private Drawable zlightdrawGiangIn, zlightdrawGiangAc;
	private Drawable zlightdrawBinhIn, zlightdrawBinhAc;

	private void initView() {
		setK(getWidth(), getHeight());
		
		drawThangIn = getResources().getDrawable(R.drawable.touch_icon_thang);
		drawThangAc = getResources().getDrawable(R.drawable.touch_icon_thang_new);
		drawGiangIn = getResources().getDrawable(R.drawable.touch_icon_giang);
		drawGiangAc = getResources().getDrawable(R.drawable.touch_icon_giang_new);
		drawBinhIn = getResources().getDrawable(R.drawable.touch_icon_binh_inactive_new);
		drawBinhAc = getResources().getDrawable(R.drawable.touch_icon_binh_active_new);
		
		zlightdrawThangIn = getResources().getDrawable(R.drawable.zlight_icon_thang_white);
		zlightdrawThangAc = getResources().getDrawable(R.drawable.zlight_icon_thang_white);
		zlightdrawGiangIn = getResources().getDrawable(R.drawable.zlight_icon_giang_white);
		zlightdrawGiangAc = getResources().getDrawable(R.drawable.zlight_icon_giang_white);
		zlightdrawBinhIn = getResources().getDrawable(R.drawable.zlight_icon_binh_white);
		zlightdrawBinhAc = getResources().getDrawable(R.drawable.zlight_icon_binh_white);

		// MELODY - 11 items 0 -> 10
		sliderMelody = new ArrayList<Rect>();
		
		Rect startRect = new Rect(35, 89, 75, 95); // value 0
		Rect realRect = new Rect(startRect.left * widthLayout / 100,
				startRect.top * heightLayout / 100, startRect.right
						* widthLayout / 100, startRect.bottom * heightLayout
						/ 100);
		int rectSize = realRect.bottom - realRect.top;
		int rectSpace = 5 * widthLayout / 100;
		for (int i = 0; i < 11; i++) {
			int decrease = i * (rectSize + rectSpace);
			Rect myRect = new Rect(realRect.left, realRect.top - decrease,
					realRect.right, realRect.bottom - decrease);
			sliderMelody.add(myRect);
		}

		// TEMPO - 9 items -4 -> 4
		sliderTempo = new ArrayList<Rect>();

		startRect = new Rect(35, 89, 75, 95); // value 0
		realRect = new Rect(startRect.left * widthLayout / 100, startRect.top
				* heightLayout / 100, startRect.right * widthLayout / 100,
				startRect.bottom * heightLayout / 100);
		rectSize = realRect.bottom - realRect.top;
		rectSpace = 9 * widthLayout / 100;
		for (int i = 0; i < 9; i++) {
			int decrease = i * (rectSize + rectSpace);
			Rect myRect = new Rect(realRect.left, realRect.top - decrease,
					realRect.right, realRect.bottom - decrease);
			sliderTempo.add(myRect);
		}

		// 13 items -6 -> 6
		sliderKey = new ArrayList<Rect>();
		
		startRect = new Rect(40, 85, 80, 90);
		realRect = new Rect(startRect.left * widthLayout / 100, startRect.top
				* heightLayout / 100, startRect.right * widthLayout / 100,
				startRect.bottom * heightLayout / 100);
		rectSize = realRect.bottom - realRect.top;
		rectSpace = 4 * widthLayout / 100;
		for (int i = 0; i < 13; i++) {
			int decrease = i * (rectSize + rectSpace);
			Rect myRect = new Rect(realRect.left, realRect.top - decrease,
					realRect.right, realRect.bottom - decrease);
			sliderKey.add(myRect);
		}	

		// 17 items 0 -> 16
		sliderVolumn = new ArrayList<Rect>();
		
		startRect = new Rect(35, 88, 75, 92);
		realRect = new Rect(startRect.left * widthLayout / 100, startRect.top
				* heightLayout / 100, startRect.right * widthLayout / 100,
				startRect.bottom * heightLayout / 100);
		rectSize = realRect.bottom - realRect.top;
		rectSpace = 3 * widthLayout / 100;
		if(MyApplication.flagHong){
			rectSpace = 4 * widthLayout / 100;	
		}		
		int maxTick = 17;
		if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
				|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
				 || MyApplication.intSvrModel == MyApplication.SONCA_KBX9|| MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
				 || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK || MyApplication.intSvrModel == MyApplication.SONCA_TBT){
			maxTick = 16;
		}
		for (int i = 0; i < maxTick; i++) {
			int decrease = i * (rectSize + rectSpace);
			Rect myRect = new Rect(realRect.left, realRect.top - decrease,
					realRect.right, realRect.bottom - decrease);
			sliderVolumn.add(myRect);
		}	

		sliderItems = sliderMelody;
		rectTicker = new Rect(0, 0, 0, 0);
	}

	private int maxSliderVolumn = 0;
	private int minSliderVolumn = 0;

	public void setSliderData(boolean isDisplay, int senderType,
			int sliderType, int intSliderVolumn) {
		this.senderType = senderType;
		this.sliderType = sliderType;
		this.isDisplay = isDisplay;
		this.intSliderVolumn = intSliderVolumn;

		switch (senderType) {
		case SENDER_MELODY:
			minSliderVolumn = 0;
			maxSliderVolumn = 10;
			break;
		case SENDER_TEMPO:
			minSliderVolumn = -4;
			maxSliderVolumn = 4;
			break;
		case SENDER_KEY:
			minSliderVolumn = -6;
			maxSliderVolumn = 6;
			break;
		case SENDER_VOLUMN:
			minSliderVolumn = 0;
			maxSliderVolumn = 16;
			break;
		default:
			break;
		}
		invalidate();
	}

	public int getSenderType() {
		return this.senderType;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		setK(w, h);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int myHeight = (int) (1.1 * getResources().getDisplayMetrics().heightPixels / 2);
		int myWidth = (int) (1.1 * getResources().getDisplayMetrics().widthPixels / 9);
		setMeasuredDimension(
				MeasureSpec.makeMeasureSpec(myWidth, MeasureSpec.UNSPECIFIED),
				MeasureSpec.makeMeasureSpec(myHeight, MeasureSpec.UNSPECIFIED));
	}

	private int color_01;
	private int color_02;
	private int color_03;
	private int color_04;
	private int color_05;
	private int color_06;
	private int color_07;
	private int color_08;
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		initView();
		// MyLog.e("","senderType = " + senderType);
		if (!isDisplay) {
			return;
		}
/*
		intSliderVolumn = 4;
		senderType = SENDER_VOLUMN;		
		MyApplication.intColorScreen = MyApplication.SCREEN_LIGHT;
*/
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			color_01 = Color.argb(255, 91, 139, 155);
			color_02 = Color.argb(255, 0, 253, 253);
			color_03 = Color.argb(255, 180, 254, 255);
			color_04 = Color.argb(255, 30, 200, 250);
			color_05 = Color.rgb(0, 253, 253);
			color_06 = Color.YELLOW;
			color_07 = Color.RED;
			color_08 = Color.WHITE;
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			color_01 = Color.parseColor("#C1FFE8");
			color_02 = Color.parseColor("#21BAA9");
			color_03 = Color.parseColor("#C1FFE8");
			color_04 = Color.argb(255, 255, 255, 255);
			color_05 = Color.parseColor("#C1FFE8");
			color_06 = Color.YELLOW;
			color_07 = Color.RED;
			color_08 = Color.WHITE;
		}

// VIEWBACK
		
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			float textWidth = -1;
			String text = "";
			switch (senderType) {
			case SENDER_MELODY:
				sliderItems = sliderMelody;
				text = "MELODY";
				break;
			case SENDER_TEMPO:
				sliderItems = sliderTempo;
				text = "TEMPO";
				break;
			case SENDER_KEY:
				sliderItems = sliderKey;
				text = "KEY";
				break;
			case SENDER_VOLUMN:
				sliderItems = sliderVolumn;
				text = "VOLUME";
				break;
			default:
				break;
			}

//			mainPaint.setStyle(Style.STROKE);
//			mainPaint.setColor(color_07);
//			mainPaint.setStrokeWidth(3);
//			canvas.drawRect(0, 0, widthLayout, heightLayout, mainPaint);

			mainPaint.setStyle(Style.FILL);

			// --------------INITIALIZE TICKER------------------//
			rectTicker = rectFromValue(intSliderVolumn);

			// --------------TEXT SENDER TYPE------------------//
			mainPaint.setTextSize(KT1S);
			textWidth = mainPaint.measureText(text);
			mainPaint.setColor(color_08);
			Rect textRect = sliderItems.get(0);
			if (text.equals("MELODY") || text.equals("TEMPO")) {
				canvas.drawText(text, (textRect.left + textRect.right) / 2
						- textWidth / 2, KT1Y, mainPaint);
			} else {
				canvas.drawText(text, (textRect.left + textRect.right) / 2
						- textWidth / 2, KT2Y, mainPaint);
			}

			// --------------DRAW SLIDER------------------//
			if (sliderType == SLIDER_TYPE0) { // Melody & Volumn
				for (int i = 0; i < sliderItems.size(); i++) {
					Rect temp = sliderItems.get(i);

					if (i == 0) {
						mainPaint.setColor(color_06);
						if(senderType == SENDER_VOLUMN){
							mainPaint.setColor(color_07);
						}
						canvas.drawRect(temp, mainPaint);
					} else {
						if (i > intSliderVolumn) {
							mainPaint.setColor(color_01);
						} else {
							mainPaint.setColor(color_02);
						}

						canvas.drawRect(temp, mainPaint);
					}

					// DRAW TEXT NUM
					String textNum = i + "";
					if (i != intSliderVolumn) {
						mainPaint.setColor(color_03);
						if(i == 0){
							mainPaint.setColor(color_06);
							if(senderType == SENDER_VOLUMN){
								mainPaint.setColor(color_07);
							}
						}
						mainPaint.setTextSize(KT2S);
						textWidth = mainPaint.measureText(textNum);
						canvas.drawText(textNum, temp.left - textWidth - 2
								* widthLayout / 100,
								temp.bottom - temp.height() / 4, mainPaint);
					} else {
						// if (intSliderVolumn > 0) {
						// textNum = "+" + i;
						// }
						Paint sPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
						sPaint.setColor(color_04);
						sPaint.setTextSize(KT3S);
						sPaint.setShadowLayer(10, 2, 2, color_05);
						textWidth = sPaint.measureText(textNum);
						canvas.drawText(textNum, rectTicker.left - textWidth
								- 2 * widthLayout / 100,
								temp.bottom - temp.height() / 4, sPaint);
					}
				}
			}

			if (sliderType == SLIDER_TYPE1) { // Tempo & Key
				int zeroIndex = sliderItems.size() / 2;

				for (int i = zeroIndex; i > 0; i--) {
					if (i > intSliderVolumn) {
						mainPaint.setColor(color_01);
					} else {
						mainPaint.setColor(color_02);
					}
					Rect temp = sliderItems.get(i + zeroIndex);
					canvas.drawRect(temp, mainPaint);

					// DRAW TEXT NUM
					String textNum = i + "";
					if(i < 0 && senderType == SENDER_KEY){
						textNum = (-i) + "";
					}
					if (i != intSliderVolumn) {
						mainPaint.setColor(color_03);
						if(i == 0){
							mainPaint.setColor(color_06);
						}
						mainPaint.setTextSize(KT2S);
						textWidth = mainPaint.measureText(textNum);
						canvas.drawText(textNum, temp.left - textWidth - 2
								* widthLayout / 100,
								temp.bottom - temp.height() / 4, mainPaint);
						
						if(senderType == SENDER_KEY){
							int rectRight = (int)(temp.left - textWidth - 2
									* widthLayout / 100);
							int rectLeft = rectRight - (int)KT2S;
							int rectBottom = temp.bottom - temp.height() / 4;
							int rectTop = rectBottom - (int)KT2S;
							
							Rect rectThangGiang = new Rect(rectLeft, rectTop, rectRight, rectBottom);
							if (i == 0) {
								drawBinhIn.setBounds(rectThangGiang);
								drawBinhIn.draw(canvas);	
							} else if(i == 1){
								drawThangIn.setBounds(rectThangGiang);
								drawThangIn.draw(canvas);
							}else if(i == -1){
								drawGiangIn.setBounds(rectThangGiang);
								drawGiangIn.draw(canvas);	
							}								
						}
					} else {						
						Paint sPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
						sPaint.setColor(color_04);
						sPaint.setTextSize(KT3S);
						sPaint.setShadowLayer(10, 2, 2, color_05);
						textWidth = sPaint.measureText(textNum);
						canvas.drawText(textNum, rectTicker.left - textWidth
								- 2 * widthLayout / 100,
								temp.bottom - temp.height() / 4, sPaint);
						
						if(senderType == SENDER_KEY){
							int rectRight = (int)(temp.left - textWidth - 4
									* widthLayout / 100);
							int rectLeft = rectRight - (int)KT3S;
							int rectBottom = temp.bottom - temp.height() / 4;
							int rectTop = rectBottom - (int)KT3S;
							
							Rect rectThangGiang = new Rect(rectLeft, rectTop, rectRight, rectBottom);
							drawThangAc.setBounds(rectThangGiang);
							drawThangAc.draw(canvas);						
						}
					}
				}

				for (int i = zeroIndex * -1; i < 0; i++) {
					if (i < intSliderVolumn) {
						mainPaint.setColor(color_01);
					} else {
						mainPaint.setColor(color_02);
					}
					Rect temp = sliderItems.get(i + zeroIndex);
					canvas.drawRect(temp, mainPaint);

					// DRAW TEXT NUM
					String textNum = i + "";
					if(i < 0 && senderType == SENDER_KEY){
						textNum = (-i) + "";
					}
					if (i != intSliderVolumn) {
						mainPaint.setColor(color_03);
						if(i == 0){
							mainPaint.setColor(color_06);
						}
						mainPaint.setTextSize(KT2S);
						textWidth = mainPaint.measureText(textNum);
						canvas.drawText(textNum, temp.left - textWidth - 2
								* widthLayout / 100,
								temp.bottom - temp.height() / 4, mainPaint);
						
						if(senderType == SENDER_KEY){
							int rectRight = (int)(temp.left - textWidth - 2
									* widthLayout / 100);
							int rectLeft = rectRight - (int)KT2S;
							int rectBottom = temp.bottom - temp.height() / 4;
							int rectTop = rectBottom - (int)KT2S;
							
							Rect rectThangGiang = new Rect(rectLeft, rectTop, rectRight, rectBottom);
							if (i == 0) {
								drawBinhIn.setBounds(rectThangGiang);
								drawBinhIn.draw(canvas);	
							} else if(i == 1){
								drawThangIn.setBounds(rectThangGiang);
								drawThangIn.draw(canvas);
							}else if(i == -1){
								drawGiangIn.setBounds(rectThangGiang);
								drawGiangIn.draw(canvas);	
							}		
						}
					} else {						
						Paint sPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
						sPaint.setColor(color_04);
						sPaint.setTextSize(KT3S);
						sPaint.setShadowLayer(10, 2, 2, color_05);
						textWidth = sPaint.measureText(textNum);
						canvas.drawText(textNum, rectTicker.left - textWidth
								- 2 * widthLayout / 100,
								temp.bottom - temp.height() / 4, sPaint);
						
						if(senderType == SENDER_KEY){
							int rectRight = (int)(temp.left - textWidth - 4
									* widthLayout / 100);
							int rectLeft = rectRight - (int)KT3S;
							int rectBottom = temp.bottom - temp.height() / 4;
							int rectTop = rectBottom - (int)KT3S;
							
							Rect rectThangGiang = new Rect(rectLeft, rectTop, rectRight, rectBottom);
							drawGiangAc.setBounds(rectThangGiang);
							drawGiangAc.draw(canvas);						
						}
					}
				}

				// zero position
				mainPaint.setColor(color_06);
				Rect temp = sliderItems.get(zeroIndex);
				canvas.drawRect(temp, mainPaint);

				// DRAW TEXT NUM
				String textNum = 0 + "";
				if (0 != intSliderVolumn) {
					mainPaint.setColor(color_06);
					mainPaint.setTextSize(KT2S);
					textWidth = mainPaint.measureText(textNum);
					canvas.drawText(textNum, temp.left - textWidth - 2
							* widthLayout / 100, temp.bottom - temp.height()
							/ 4, mainPaint);
					
					if(senderType == SENDER_KEY){
						int rectRight = (int)(temp.left - textWidth - 2
								* widthLayout / 100);
						int rectLeft = rectRight - (int)KT2S;
						int rectBottom = temp.bottom - temp.height() / 4;
						int rectTop = rectBottom - (int)KT2S;
						
						Rect rectThangGiang = new Rect(rectLeft, rectTop, rectRight, rectBottom);
						drawBinhIn.setBounds(rectThangGiang);
						drawBinhIn.draw(canvas);
					}
				} else {					
					Paint sPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
					sPaint.setColor(color_04);
					sPaint.setTextSize(KT3S);
					sPaint.setShadowLayer(10, 2, 2, color_05);
					textWidth = sPaint.measureText(textNum);
					canvas.drawText(textNum, rectTicker.left - textWidth - 2
							* widthLayout / 100, temp.bottom - temp.height()
							/ 4, sPaint);
					
					if(senderType == SENDER_KEY){
						int rectRight = (int)(temp.left - textWidth - 4
								* widthLayout / 100);
						int rectLeft = rectRight - (int)KT3S;
						int rectBottom = temp.bottom - temp.height() / 4;
						int rectTop = rectBottom - (int)KT3S;
						
						Rect rectThangGiang = new Rect(rectLeft, rectTop, rectRight, rectBottom);
						drawBinhAc.setBounds(rectThangGiang);
						drawBinhAc.draw(canvas);						
					}
				}

			}

			// --------------DRAW TICKER------------------//
			mainPaint.setColor(color_03);
			rectTicker = rectFromValue(intSliderVolumn);
			canvas.drawRect(rectTicker, mainPaint);
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){

			float textWidth = -1;
			String text = "";
			switch (senderType) {
			case SENDER_MELODY:
				sliderItems = sliderMelody;
				text = "MELODY";
				break;
			case SENDER_TEMPO:
				sliderItems = sliderTempo;
				text = "TEMPO";
				break;
			case SENDER_KEY:
				sliderItems = sliderKey;
				text = "KEY";
				break;
			case SENDER_VOLUMN:
				sliderItems = sliderVolumn;
				text = "VOLUME";
				break;
			default:
				break;
			}

//			mainPaint.setStyle(Style.STROKE);
//			mainPaint.setColor(color_07);
//			mainPaint.setStrokeWidth(3);
//			canvas.drawRect(0, 0, widthLayout, heightLayout, mainPaint);

			mainPaint.setStyle(Style.FILL);

			// --------------INITIALIZE TICKER------------------//
			rectTicker = rectFromValue(intSliderVolumn);

			// --------------TEXT SENDER TYPE------------------//
			mainPaint.setTextSize(KT1S);
			textWidth = mainPaint.measureText(text);
			mainPaint.setColor(color_08);
			Rect textRect = sliderItems.get(0);
			if (text.equals("MELODY") || text.equals("TEMPO")) {
				canvas.drawText(text, (textRect.left + textRect.right) / 2
						- textWidth / 2, KT1Y, mainPaint);
			} else {
				canvas.drawText(text, (textRect.left + textRect.right) / 2
						- textWidth / 2, KT2Y, mainPaint);
			}

			// --------------DRAW SLIDER------------------//
			if (sliderType == SLIDER_TYPE0) { // Melody & Volumn
				for (int i = 0; i < sliderItems.size(); i++) {
					Rect temp = sliderItems.get(i);

					if (i == 0) {
						mainPaint.setColor(color_06);
						if(senderType == SENDER_VOLUMN){
							mainPaint.setColor(color_07);
						}
						canvas.drawRect(temp, mainPaint);
					} else {
						if (i > intSliderVolumn) {
							mainPaint.setColor(color_01);
						} else {
							mainPaint.setColor(color_02);
						}

						canvas.drawRect(temp, mainPaint);
					}

					// DRAW TEXT NUM
					String textNum = i + "";
					if (i != intSliderVolumn) {
						mainPaint.setColor(color_03);
						if(i == 0){
							mainPaint.setColor(color_06);
							if(senderType == SENDER_VOLUMN){
								mainPaint.setColor(color_07);
							}
						}
						mainPaint.setTextSize(KT2S);
						textWidth = mainPaint.measureText(textNum);
						canvas.drawText(textNum, temp.left - textWidth - 2
								* widthLayout / 100,
								temp.bottom - temp.height() / 4, mainPaint);
					} else {
						// if (intSliderVolumn > 0) {
						// textNum = "+" + i;
						// }
						Paint sPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
						sPaint.setColor(color_04);
						sPaint.setTextSize(KT3S);
						sPaint.setShadowLayer(10, 2, 2, color_05);
						textWidth = sPaint.measureText(textNum);
						canvas.drawText(textNum, rectTicker.left - textWidth
								- 2 * widthLayout / 100,
								temp.bottom - temp.height() / 4, sPaint);
					}
				}
			}

			if (sliderType == SLIDER_TYPE1) { // Tempo & Key
				int zeroIndex = sliderItems.size() / 2;

				for (int i = zeroIndex; i > 0; i--) {
					if (i > intSliderVolumn) {
						mainPaint.setColor(color_01);
					} else {
						mainPaint.setColor(color_02);
					}
					Rect temp = sliderItems.get(i + zeroIndex);
					canvas.drawRect(temp, mainPaint);

					// DRAW TEXT NUM
					String textNum = i + "";
					if(i < 0 && senderType == SENDER_KEY){
						textNum = (-i) + "";
					}
					if (i != intSliderVolumn) {
						mainPaint.setColor(color_03);
						if(i == 0){
							mainPaint.setColor(color_06);
						}
						mainPaint.setTextSize(KT2S);
						textWidth = mainPaint.measureText(textNum);
						canvas.drawText(textNum, temp.left - textWidth - 2
								* widthLayout / 100,
								temp.bottom - temp.height() / 4, mainPaint);
						
						if(senderType == SENDER_KEY){
							int rectRight = (int)(temp.left - textWidth - 2
									* widthLayout / 100);
							int rectLeft = rectRight - (int)KT2S;
							int rectBottom = temp.bottom - temp.height() / 4;
							int rectTop = rectBottom - (int)KT2S;
							
							Rect rectThangGiang = new Rect(rectLeft, rectTop, rectRight, rectBottom);
							if (i == 0) {
								zlightdrawBinhIn.setBounds(rectThangGiang);
								zlightdrawBinhIn.draw(canvas);	
							} else if(i == 1){
								zlightdrawThangIn.setBounds(rectThangGiang);
								zlightdrawThangIn.draw(canvas);
							}else if(i == -1){
								zlightdrawGiangIn.setBounds(rectThangGiang);
								zlightdrawGiangIn.draw(canvas);	
							}								
						}
					} else {						
						Paint sPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
						sPaint.setColor(color_04);
						sPaint.setTextSize(KT3S);
						sPaint.setShadowLayer(10, 2, 2, color_05);
						textWidth = sPaint.measureText(textNum);
						canvas.drawText(textNum, rectTicker.left - textWidth
								- 2 * widthLayout / 100,
								temp.bottom - temp.height() / 4, sPaint);
						
						if(senderType == SENDER_KEY){
							int rectRight = (int)(temp.left - textWidth - 4
									* widthLayout / 100);
							int rectLeft = rectRight - (int)KT3S;
							int rectBottom = temp.bottom - temp.height() / 4;
							int rectTop = rectBottom - (int)KT3S;
							
							Rect rectThangGiang = new Rect(rectLeft, rectTop, rectRight, rectBottom);
							zlightdrawThangAc.setBounds(rectThangGiang);
							zlightdrawThangAc.draw(canvas);						
						}
					}
				}

				for (int i = zeroIndex * -1; i < 0; i++) {
					if (i < intSliderVolumn) {
						mainPaint.setColor(color_01);
					} else {
						mainPaint.setColor(color_02);
					}
					Rect temp = sliderItems.get(i + zeroIndex);
					canvas.drawRect(temp, mainPaint);

					// DRAW TEXT NUM
					String textNum = i + "";
					if(i < 0 && senderType == SENDER_KEY){
						textNum = (-i) + "";
					}
					if (i != intSliderVolumn) {
						mainPaint.setColor(color_03);
						if(i == 0){
							mainPaint.setColor(color_06);
						}
						mainPaint.setTextSize(KT2S);
						textWidth = mainPaint.measureText(textNum);
						canvas.drawText(textNum, temp.left - textWidth - 2
								* widthLayout / 100,
								temp.bottom - temp.height() / 4, mainPaint);
						
						if(senderType == SENDER_KEY){
							int rectRight = (int)(temp.left - textWidth - 2
									* widthLayout / 100);
							int rectLeft = rectRight - (int)KT2S;
							int rectBottom = temp.bottom - temp.height() / 4;
							int rectTop = rectBottom - (int)KT2S;
							
							Rect rectThangGiang = new Rect(rectLeft, rectTop, rectRight, rectBottom);
							if (i == 0) {
								zlightdrawBinhIn.setBounds(rectThangGiang);
								zlightdrawBinhIn.draw(canvas);	
							} else if(i == 1){
								zlightdrawThangIn.setBounds(rectThangGiang);
								zlightdrawThangIn.draw(canvas);
							}else if(i == -1){
								zlightdrawGiangIn.setBounds(rectThangGiang);
								zlightdrawGiangIn.draw(canvas);	
							}		
						}
					} else {						
						Paint sPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
						sPaint.setColor(color_04);
						sPaint.setTextSize(KT3S);
						sPaint.setShadowLayer(10, 2, 2, color_05);
						textWidth = sPaint.measureText(textNum);
						canvas.drawText(textNum, rectTicker.left - textWidth
								- 2 * widthLayout / 100,
								temp.bottom - temp.height() / 4, sPaint);
						
						if(senderType == SENDER_KEY){
							int rectRight = (int)(temp.left - textWidth - 4
									* widthLayout / 100);
							int rectLeft = rectRight - (int)KT3S;
							int rectBottom = temp.bottom - temp.height() / 4;
							int rectTop = rectBottom - (int)KT3S;
							
							Rect rectThangGiang = new Rect(rectLeft, rectTop, rectRight, rectBottom);
							zlightdrawGiangAc.setBounds(rectThangGiang);
							zlightdrawGiangAc.draw(canvas);						
						}
					}
				}

				// zero position
				mainPaint.setColor(color_06);
				Rect temp = sliderItems.get(zeroIndex);
				canvas.drawRect(temp, mainPaint);

				// DRAW TEXT NUM
				String textNum = 0 + "";
				if (0 != intSliderVolumn) {
					mainPaint.setColor(color_06);
					mainPaint.setTextSize(KT2S);
					textWidth = mainPaint.measureText(textNum);
					canvas.drawText(textNum, temp.left - textWidth - 2
							* widthLayout / 100, temp.bottom - temp.height()
							/ 4, mainPaint);
					
					if(senderType == SENDER_KEY){
						int rectRight = (int)(temp.left - textWidth - 2
								* widthLayout / 100);
						int rectLeft = rectRight - (int)KT2S;
						int rectBottom = temp.bottom - temp.height() / 4;
						int rectTop = rectBottom - (int)KT2S;
						
						Rect rectThangGiang = new Rect(rectLeft, rectTop, rectRight, rectBottom);
						zlightdrawBinhIn.setBounds(rectThangGiang);
						zlightdrawBinhIn.draw(canvas);
					}
				} else {					
					Paint sPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
					sPaint.setColor(color_04);
					sPaint.setTextSize(KT3S);
					sPaint.setShadowLayer(10, 2, 2, color_05);
					textWidth = sPaint.measureText(textNum);
					canvas.drawText(textNum, rectTicker.left - textWidth - 2
							* widthLayout / 100, temp.bottom - temp.height()
							/ 4, sPaint);
					
					if(senderType == SENDER_KEY){
						int rectRight = (int)(temp.left - textWidth - 4
								* widthLayout / 100);
						int rectLeft = rectRight - (int)KT3S;
						int rectBottom = temp.bottom - temp.height() / 4;
						int rectTop = rectBottom - (int)KT3S;
						
						Rect rectThangGiang = new Rect(rectLeft, rectTop, rectRight, rectBottom);
						zlightdrawBinhAc.setBounds(rectThangGiang);
						zlightdrawBinhAc.draw(canvas);						
					}
				}

			}

			// --------------DRAW TICKER------------------//
			mainPaint.setColor(color_03);
			rectTicker = rectFromValue(intSliderVolumn);
			canvas.drawRect(rectTicker, mainPaint);
		
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (isDisplay) {
			if (typeDeviceConnect == 0) {
				if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
						|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
						 || MyApplication.intSvrModel == MyApplication.SONCA_KBX9|| MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
						 || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK || MyApplication.intSvrModel == MyApplication.SONCA_TBT){
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN: {
						float x = event.getX();
						float y = event.getY();

						UpdateLayout(x, y);
						invalidate();
					}
						break;
					case MotionEvent.ACTION_MOVE: {
						float x = event.getX();
						float y = event.getY();
						UpdateLayoutOnly(x, y);
						invalidate();
					}
					break;
					case MotionEvent.ACTION_UP: {
						float x = event.getX();
						float y = event.getY();
						UpdateLayout(x, y);
						invalidate();
					}
						break;
					default:
						break;
					}	
				} else {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN: {
						float x = event.getX();
						float y = event.getY();

						UpdateLayout(x, y);
						invalidate();
					}
						break;
					case MotionEvent.ACTION_MOVE: {
						float x = event.getX();
						float y = event.getY();
						UpdateLayout(x, y);
						invalidate();
					}
						break;
					default:
						break;
					}	
				}	

			} else if (typeDeviceConnect == 1) { // katrol
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					float x = event.getX();
					float y = event.getY();
					if (rectTicker != null && rectTicker.top != 0) {
						if (y < rectTicker.top) {
							if (intSliderVolumn < maxSliderVolumn) {
								intSliderVolumn++;
								if (listener != null) {
									listener.OnSliderIncrease();
								}
								invalidate();
							}
						} else if (y > rectTicker.bottom) {
							if (intSliderVolumn > minSliderVolumn) {
								intSliderVolumn--;
								if (listener != null) {
									listener.OnSliderDecrease();
								}
								invalidate();
							}
						}
					}
					break;
				default:
					break;
				}
			}
		}
		return true;
	}

	private Rect rectTicker;
	private int heightTicker;
	private int oldSliderValue = 0;

	private void UpdateLayout(float x, float y) {
		int intClick = getIntSlider(x, y);
		if (intClick != -100) {
			intSliderVolumn = intClick;
			if (intSliderVolumn != oldSliderValue) {
				if (listener != null) {
					listener.OnSlider(senderType, intSliderVolumn);
				}
				oldSliderValue = intSliderVolumn;
			}
		}

	}
	
	private void UpdateLayoutOnly(float x, float y) {
		int intClick = getIntSlider(x, y);
		if (intClick != -100) {
			intSliderVolumn = intClick;
			if (intSliderVolumn != oldSliderValue) {
				//oldSliderValue = intSliderVolumn;
			}
		}

	}

	private int getIntSlider(float x, float y) {
		int result = -100;
		for (int i = 0; i < sliderItems.size(); i++) {
			Rect temp = sliderItems.get(i);
			int left = 0;
			int top = temp.top;
			int right = widthLayout;
			int bottom = temp.bottom;
			if (x >= left && x <= right && y >= top && y <= bottom) {
				switch (senderType) {
				case SENDER_MELODY:
					result = i;
					break;
				case SENDER_TEMPO:
					result = i - 4;
					break;
				case SENDER_KEY:
					result = i - 6;
					break;
				case SENDER_VOLUMN:
					result = i;
					break;
				default:
					break;
				}
			}
		}
		return result;
	}

	private float KT1S, KT1X, KT1Y;
	private float KT2S, KT2X, KT2Y, KT3S;

	private void setK(int w, int h) {
		widthLayout = w;
		heightLayout = h;
	}

	private Rect rectFromValue(int value) {
		Rect resultRect = new Rect(0, 0, 0, 0);

		int addIndex = -1;
		switch (senderType) {
		case SENDER_MELODY:
			addIndex = 0;
			break;
		case SENDER_TEMPO:
			addIndex = 4;
			break;
		case SENDER_KEY:
			addIndex = 6;
			break;
		case SENDER_VOLUMN:
			addIndex = 0;
			break;
		default:
			break;
		}

		Rect temp = sliderItems.get(0);
		try {
			temp = sliderItems.get(value + addIndex);
		} catch (Exception e) {
			temp = sliderItems.get(0);
		}

		int left = temp.left - 7 * widthLayout / 100;
		int top = temp.top;
		int right = temp.right + 7 * widthLayout / 100;
		int bottom = temp.bottom;
		heightTicker = (bottom - top) / 2;
		resultRect = new Rect(left, top + heightTicker / 2, right, bottom
				- heightTicker / 2);

		KT1S = heightTicker * 2f;
		KT2S = heightTicker * 1.5f;
		KT3S = heightTicker * 2.5f;

		if (senderType == SENDER_TEMPO) {
			KT3S = heightTicker * 1.5f;
		}

		KT1Y = sliderItems.get(sliderItems.size() - 1).top - 10;
		KT2Y = sliderItems.get(0).bottom + heightTicker * 3;

		return resultRect;
	}
}
