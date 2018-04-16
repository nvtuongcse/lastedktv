package vn.com.sonca.Touch.CustomView;

import java.util.ArrayList;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.zktv.main.KTVMainActivity;
import vn.com.sonca.zzzzz.MainActivity;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;

public class TouchMelodyView extends View {
	
	private String TAB = "MelodyView";
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	
	private final int CONGTRU = 0;
	private final int CONG = 1;
	private final int TRU = -1;
	private int intCongTru = CONGTRU;
	
	private ArrayList<Float> Items;
	
	private float DP;
	private boolean boolFocus = false;
	private int width = 350;
	private int height = 350;
//	private int mRadius;
	private int angle = 270;
	private int intMelody = 1;

	public TouchMelodyView(Context context) {
		super(context);
		this.context = context;
		initView();
	}
	
	public TouchMelodyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
	}
	
	public TouchMelodyView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initView();
	}
	
	private OnMelodyListener listener;
	public interface OnMelodyListener {
		public void onMelody(int value);
		public void OnInActive();
		public void OnShowTab(int intSliderVolumn);
	}
	
	public void setOnMelodyListener(OnMelodyListener listener){
		this.listener = listener;
	}
	
	private Typeface typeFace;
	private Drawable drawActive;
	private Drawable drawInActive;
	private Drawable drawShowTabAC;
	private Drawable drawShowTabIN;
	private Drawable drawClickOK;
	private Drawable drawClickNO;
	private Drawable drawClickHO;

	private Drawable zlightdrawActive;
	private Drawable zlightdrawInActive;
	private Drawable zlightdrawShowTabAC;
	private Drawable zlightdrawShowTabIN;
	private Drawable zlightdrawClickOK;
	private Drawable zlightdrawClickNO;
	private Drawable zlightdrawClickHO;
	
	private Drawable drawHoverCong;
	private Drawable drawHoverTru;
	private Drawable drawCongTruAC;
	private Drawable drawCongTruIN;
	private Drawable zlightdrawHoverCong;
	private Drawable zlightdrawHoverTru;
	private Drawable zlightdrawCongTruAC;
	private Drawable zlightdrawCongTruIN;
	
	private Context context;
	private void initView(){
		typeFace = Typeface.DEFAULT_BOLD;
		
		drawActive = getResources().getDrawable(R.drawable.touch_vongtron_melody_new_button);
		drawInActive = getResources().getDrawable(R.drawable.touch_vongtron_melody_xam_new_button);
		drawShowTabAC = getResources().getDrawable(R.drawable.touch_icon_active_new_button);
		drawShowTabIN = getResources().getDrawable(R.drawable.touch_icon_xam_new_button);
		drawClickOK = getResources().getDrawable(R.drawable.touch_melody_active);
		drawClickHO = getResources().getDrawable(R.drawable.touch_melody_hover);
		drawClickNO = getResources().getDrawable(R.drawable.touch_melody_xam);
		drawHoverCong = getResources().getDrawable(R.drawable.touch_hover_cong);
		drawHoverTru = getResources().getDrawable(R.drawable.touch_hover_tru);
		drawCongTruAC = getResources().getDrawable(R.drawable.touch_active_congtru);
		drawCongTruIN = getResources().getDrawable(R.drawable.touch_xam_congtru);
		
		zlightdrawActive = getResources().getDrawable(R.drawable.zlight_vongtron_melody_new_button);
		zlightdrawInActive = getResources().getDrawable(R.drawable.zlight_vongtron_melody_xam_new_button);
		zlightdrawShowTabAC = getResources().getDrawable(R.drawable.zlight_icon_active);
		zlightdrawShowTabIN = getResources().getDrawable(R.drawable.zlight_icon_xam);
		zlightdrawClickOK = getResources().getDrawable(R.drawable.zlight_melody_active);
		zlightdrawClickHO = getResources().getDrawable(R.drawable.zlight_melody_hover);
		zlightdrawClickNO = getResources().getDrawable(R.drawable.zlight_melody_xam);
		zlightdrawHoverCong = getResources().getDrawable(R.drawable.zlight_hover_cong);
		zlightdrawHoverTru = getResources().getDrawable(R.drawable.zlight_hover_tru);
		zlightdrawCongTruAC = getResources().getDrawable(R.drawable.zlight_active_congtru);
		zlightdrawCongTruIN = getResources().getDrawable(R.drawable.zlight_xam_congtru);
		
		typeFace = Typeface.DEFAULT_BOLD;
		
		DP = getResources().getDisplayMetrics().density;
		Items = new ArrayList<Float>();
		Items.add((float) 112.5);
		
		Items.add((float) 135);
		Items.add((float) 157.5);
		Items.add((float) 180);
		Items.add((float) 202.5);
		Items.add((float) 225);
		
		Items.add((float) 247.5);
		Items.add((float) 270);
		Items.add((float) 292.5);
		Items.add((float) 315);
		Items.add((float) 337.5);
	}

	private int stateView = View.VISIBLE;
	public void setEnableView(int value){
		if(MyApplication.intWifiRemote == MyApplication.SONCA){
			stateView = value;
			if(value == View.INVISIBLE){
				boolFocus = false;
				intMelody = 0;
			}
			invalidate();
		}
	}
	
	public boolean getEnableStatus(){
		if(stateView == View.INVISIBLE){
			return false;
		}
		
		if(boolBlockComand){
			return false;
		}
		
		if (TouchMainActivity.serverStatus != null || KTVMainActivity.serverStatus != null) {
			boolean flag = false;
			if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				if (KTVMainActivity.serverStatus.isPaused()) {
					flag = true;
				}
			} else {
				if (TouchMainActivity.serverStatus.isPaused()) {
					flag = true;
				}
			}
			
			if (flag) {
				return false;
			}
		}
		
		return true;
	}
	

	private float KT1 = 0;
	private float strokeArc;
	private float valueX, valueY, valueS;
	private float titleX, titleY, titleS;
	private int offsetImageX, offsetImageY;
	private Rect rectVien = new Rect();
	private Rect rectTangGiam = new Rect();
	private Rect rectShowTab = new Rect();
	private RectF rectfNac = new RectF();
	private float K;
	private int RDL, RDR, RDT, RDB;
	private float tamXM, tamYM, RM, RS;
	private int LINETAB1, LINETAB2;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		width = w ; height = h;
		KT1 = (float) (24 * w/2/75);
		offsetImageY = (int) (0.5*h);
		offsetImageX = (int) (0.41*w);
		int wd = offsetImageX;
		titleX = wd;
		titleS = (float) (0.11*h);
		titleY = (float) (0.5*h + 0.4*titleS);
		
		strokeArc = (float) (0.23*wd);
		int padArc = (int) (0.85*wd);
		int padImage = (int) (0.98*wd);
		rectfNac.set(offsetImageX - padArc, offsetImageY - padArc, 
				offsetImageX + padArc, offsetImageY + padArc);
		rectVien.set(offsetImageX - padImage, offsetImageY - padImage, 
				offsetImageX + padImage, offsetImageY + padImage);
		int padTGiamX = (int) (0.98*wd);
		int padTGiamY = 180*padTGiamX/175;
		rectTangGiam.set(offsetImageX - padTGiamX, offsetImageY - padTGiamY, 
				offsetImageX + padTGiamX, offsetImageY + padTGiamY);
		int tamY = (int) (0.5*h);
		int wr = (int) (0.17*w);
		int hr = 75*wr/80;
		rectShowTab.set(w - wr - 5, (int)(tamY - hr), w - 5, (int)(tamY + hr));
		valueS = (float) (0.38*wd);
		valueY = tamY - hr - 5;
		valueX = (float) (w - 0.5*wr);
		
		// --------------------//

		K = w / 2;
		RM = (float) (19.5 * K / 75);
		RS = (float) (3 * K / 75);
		tamXM = (float) (0.80 * h);
		tamYM = tamXM;

		RDL = (int) (tamXM - RM - RS / 64);
		RDT = (int) (tamXM - RM - RS / 64);
		RDR = (int) (tamXM + RM + RS / 64);
		RDB = (int) (tamXM + RM + RS / 64);

		LINETAB1 = (int) (0.43 * h);
		LINETAB2 = (int) (0.58 * h);
	}
	
	private int color_01;
	private int color_02;
	private int color_03;
	private int color_04;
	private int color_05;
	
	private Drawable drawBackr;
	private boolean boolBlockComand = false;
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		/*
		paintMain.setStyle(Style.FILL);
		paintMain.setColor(Color.RED);
		canvas.drawLine(0, LINETAB1, width, LINETAB1, paintMain);
		canvas.drawLine(0, LINETAB2, width, LINETAB2, paintMain);
		*/
		
		/*
		intMelody = 5;
		stateView = View.VISIBLE;
		MyApplication.intWifiRemote = MyApplication.SONCA;
		MyApplication.intColorScreen = MyApplication.SCREEN_LIGHT;
		*/
		
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			color_01 = Color.argb(0, 152, 152, 152);
			color_02 = Color.argb(255, 152, 152, 152);
			color_03 = Color.argb(255, 0, 253, 253);
			color_04 = Color.argb(255, 255, 255, 255);
			color_05 = Color.YELLOW;
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			color_01 = Color.argb(0, 152, 152, 152);
			color_02 = Color.argb(255, 152, 152, 152);
			color_03 = Color.parseColor("#21BAA9");
			color_04 = Color.parseColor("#66696C");
			color_05 = Color.parseColor("#FFF200");
		}
		
		// BACKVIEW
		
		if (TouchMainActivity.serverStatus != null || KTVMainActivity.serverStatus != null) {
			boolean flagControlFullAPI = false;
			if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				if (KTVMainActivity.serverStatus.isOnOffControlFullAPI()) {
					flagControlFullAPI = true;
				}
			} else {
				if (TouchMainActivity.serverStatus.isOnOffControlFullAPI()) {
					flagControlFullAPI = true;
				}
			}
			
			if (flagControlFullAPI) {
				int clear = 0x00000003;
				int retur = (MyApplication.intCommandMedium & (clear << INTMEDIUM)) >> INTMEDIUM;
				if((MyApplication.flagDeviceUser == true && retur != 0) || 
					(MyApplication.flagDeviceUser == false && retur == 2)){
					boolBlockComand = true;
				}else{
					boolBlockComand = false;
				}
			}else{
				if((MyApplication.intCommandEnable & INTCOMMAND) != INTCOMMAND){
					boolBlockComand = true;
				}else{
					boolBlockComand = false;
				}
			}
		}
		
		if(boolBlockComand == true){
			drawInActive.setBounds(rectVien);
			drawInActive.draw(canvas);
			drawShowTabIN.setBounds(rectShowTab);
			drawShowTabIN.draw(canvas);
			drawCongTruIN.setBounds(rectTangGiam);
			drawCongTruIN.draw(canvas);
			
			paintMain.setStyle(Style.STROKE);
			paintMain.setStrokeWidth(strokeArc);
			for (int i = Items.size() - 1; i > 0; i--) {
				paintMain.setColor(color_01);
				canvas.drawArc(rectfNac, Items.get(i), (float) 18.5, false, paintMain);
			}
			paintMain.setColor(color_02);
			canvas.drawArc(rectfNac, Items.get(0), (float) 18.5, false, paintMain);	
			
			String valueText = 0 + " ";
			textPaint.setStyle(Style.FILL);
			textPaint.setColor(color_02);
			textPaint.setTypeface(Typeface.DEFAULT_BOLD);
			textPaint.setTextSize(valueS);
			float wid = (float)(valueX - 0.5*textPaint.measureText(valueText));
			canvas.drawText(valueText, wid, valueY, textPaint);
			
			valueText = "MELODY";
			textPaint.setStyle(Style.FILL);
			textPaint.setColor(color_02);
			textPaint.setTypeface(Typeface.DEFAULT_BOLD);
			textPaint.setTextSize(titleS);
			wid = (float)(titleX - 0.5*textPaint.measureText(valueText));
			canvas.drawText(valueText, wid, titleY, textPaint);
			
			return;
		}
		
		if (TouchMainActivity.serverStatus != null || KTVMainActivity.serverStatus != null) {
			boolean flag = false;
			if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				if (KTVMainActivity.serverStatus.isPaused()) {
					flag = true;
				}
			} else {
				if (TouchMainActivity.serverStatus.isPaused()) {
					flag = true;
				}
			}
			
			if (flag) {
				
				drawInActive.setBounds(rectVien);
				drawInActive.draw(canvas);
				drawShowTabIN.setBounds(rectShowTab);
				drawShowTabIN.draw(canvas);
				drawCongTruIN.setBounds(rectTangGiam);
				drawCongTruIN.draw(canvas);
				
				paintMain.setStyle(Style.STROKE);
				paintMain.setStrokeWidth(strokeArc);
				for (int i = Items.size() - 1; i > 0; i--) {
					paintMain.setColor(color_01);
					canvas.drawArc(rectfNac, Items.get(i), (float) 18.5, false, paintMain);
				}
				paintMain.setColor(color_02);
				canvas.drawArc(rectfNac, Items.get(0), (float) 18.5, false, paintMain);	
				
				String valueText = 0 + " ";
				textPaint.setStyle(Style.FILL);
				textPaint.setColor(color_02);
				textPaint.setTypeface(Typeface.DEFAULT_BOLD);
				textPaint.setTextSize(valueS);
				float wid = (float)(valueX - 0.5*textPaint.measureText(valueText));
				canvas.drawText(valueText, wid, valueY, textPaint);
				
				valueText = "MELODY";
				textPaint.setStyle(Style.FILL);
				textPaint.setColor(color_02);
				textPaint.setTypeface(Typeface.DEFAULT_BOLD);
				textPaint.setTextSize(titleS);
				wid = (float)(titleX - 0.5*textPaint.measureText(valueText));
				canvas.drawText(valueText, wid, titleY, textPaint);
				
				return;
			}
		}
		
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			// DRAW 0
			if(MyApplication.intWifiRemote == MyApplication.SONCA){
				if(stateView == View.VISIBLE){
					drawActive.setBounds(rectVien);
					drawActive.draw(canvas);
					drawShowTabAC.setBounds(rectShowTab);
					drawShowTabAC.draw(canvas);
					switch (intCongTru) {
					case CONGTRU:
						drawCongTruAC.setBounds(rectTangGiam);
						drawCongTruAC.draw(canvas);
						break;
					case CONG:
						drawHoverCong.setBounds(rectTangGiam);
						drawHoverCong.draw(canvas);
						break;
					case TRU:
						drawHoverTru.setBounds(rectTangGiam);
						drawHoverTru.draw(canvas);
						break;
					default: break;
					}
					paintMain.setStyle(Style.STROKE);
					paintMain.setStrokeWidth(strokeArc);
					for (int i = Items.size() - 1; i > 0; i--) {
						if (i > intMelody) {
							paintMain.setColor(color_01);
						} else {
							paintMain.setColor(color_03);
						}
						canvas.drawArc(rectfNac, Items.get(i), (float) 18.5, false, paintMain);
					}
					paintMain.setColor(color_05);
					canvas.drawArc(rectfNac, Items.get(0), (float) 18.5, false, paintMain);	
					
					String valueText = intMelody + " ";
					textPaint.setStyle(Style.FILL);
					textPaint.setColor(color_03);
					textPaint.setTypeface(Typeface.DEFAULT_BOLD);
					textPaint.setTextSize(valueS);
					float wid = (float)(valueX - 0.5*textPaint.measureText(valueText));
					canvas.drawText(valueText, wid, valueY, textPaint);
					
					valueText = "MELODY";
					textPaint.setStyle(Style.FILL);
					textPaint.setColor(color_04);
					textPaint.setTypeface(Typeface.DEFAULT_BOLD);
					textPaint.setTextSize(titleS);
					wid = (float)(titleX - 0.5*textPaint.measureText(valueText));
					canvas.drawText(valueText, wid, titleY, textPaint);
				}else{
					drawInActive.setBounds(rectVien);
					drawInActive.draw(canvas);
					drawShowTabIN.setBounds(rectShowTab);
					drawShowTabIN.draw(canvas);
					drawCongTruIN.setBounds(rectTangGiam);
					drawCongTruIN.draw(canvas);
					
					paintMain.setStyle(Style.STROKE);
					paintMain.setStrokeWidth(strokeArc);
					for (int i = Items.size() - 1; i > 0; i--) {
						paintMain.setColor(color_01);
						canvas.drawArc(rectfNac, Items.get(i), (float) 18.5, false, paintMain);
					}
					paintMain.setColor(color_02);
					canvas.drawArc(rectfNac, Items.get(0), (float) 18.5, false, paintMain);	
					
					String valueText = 0 + " ";
					textPaint.setStyle(Style.FILL);
					textPaint.setColor(color_02);
					textPaint.setTypeface(Typeface.DEFAULT_BOLD);
					textPaint.setTextSize(valueS);
					float wid = (float)(valueX - 0.5*textPaint.measureText(valueText));
					canvas.drawText(valueText, wid, valueY, textPaint);
					
					valueText = "MELODY";
					textPaint.setStyle(Style.FILL);
					textPaint.setColor(color_02);
					textPaint.setTypeface(Typeface.DEFAULT_BOLD);
					textPaint.setTextSize(titleS);
					wid = (float)(titleX - 0.5*textPaint.measureText(valueText));
					canvas.drawText(valueText, wid, titleY, textPaint);
				}
				
			} else {

				drawActive.setBounds(rectVien);
				drawActive.draw(canvas);
				if (boolFocus) {
					drawClickHO.setBounds(rectTangGiam);
					drawClickHO.draw(canvas);
				} else {
					drawClickOK.setBounds(rectTangGiam);
					drawClickOK.draw(canvas);
				}
				paintMain.setStyle(Style.STROKE);
				paintMain.setStrokeWidth(strokeArc);

				String valueText = "MELODY";
				textPaint.setStyle(Style.FILL);
				textPaint.setColor(color_04);
				textPaint.setTypeface(Typeface.DEFAULT_BOLD);
				textPaint.setTextSize(titleS);
				float wid = (float)(titleX - 0.5*textPaint.measureText(valueText));
				canvas.drawText(valueText, wid, titleY, textPaint);
			
			}
			
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){

			// DRAW 0
			if(MyApplication.intWifiRemote == MyApplication.SONCA){
				if(stateView == View.VISIBLE){
					zlightdrawActive.setBounds(rectVien);
					zlightdrawActive.draw(canvas);
					zlightdrawShowTabAC.setBounds(rectShowTab);
					zlightdrawShowTabAC.draw(canvas);
					switch (intCongTru) {
					case CONGTRU:
						zlightdrawCongTruAC.setBounds(rectTangGiam);
						zlightdrawCongTruAC.draw(canvas);
						break;
					case CONG:
						zlightdrawHoverCong.setBounds(rectTangGiam);
						zlightdrawHoverCong.draw(canvas);
						break;
					case TRU:
						zlightdrawHoverTru.setBounds(rectTangGiam);
						zlightdrawHoverTru.draw(canvas);
						break;
					default: break;
					}
					paintMain.setStyle(Style.STROKE);
					paintMain.setStrokeWidth(strokeArc);
					for (int i = Items.size() - 1; i > 0; i--) {
						if (i > intMelody) {
							paintMain.setColor(color_01);
						} else {
							paintMain.setColor(color_03);
						}
						canvas.drawArc(rectfNac, Items.get(i), (float) 18.5, false, paintMain);
					}
					paintMain.setColor(color_05);
					canvas.drawArc(rectfNac, Items.get(0), (float) 18.5, false, paintMain);	
					
					String valueText = intMelody + " ";
					textPaint.setStyle(Style.FILL);
					textPaint.setColor(color_03);
					textPaint.setTypeface(Typeface.DEFAULT_BOLD);
					textPaint.setTextSize(valueS);
					float wid = (float)(valueX - 0.5*textPaint.measureText(valueText));
					canvas.drawText(valueText, wid, valueY, textPaint);
					
					valueText = "MELODY";
					textPaint.setStyle(Style.FILL);
					textPaint.setColor(color_04);
					textPaint.setTypeface(Typeface.DEFAULT_BOLD);
					textPaint.setTextSize(titleS);
					wid = (float)(titleX - 0.5*textPaint.measureText(valueText));
					canvas.drawText(valueText, wid, titleY, textPaint);
				}else{
					zlightdrawInActive.setBounds(rectVien);
					zlightdrawInActive.draw(canvas);
					zlightdrawShowTabIN.setBounds(rectShowTab);
					zlightdrawShowTabIN.draw(canvas);
					zlightdrawCongTruIN.setBounds(rectTangGiam);
					zlightdrawCongTruIN.draw(canvas);
					
					paintMain.setStyle(Style.STROKE);
					paintMain.setStrokeWidth(strokeArc);
					for (int i = Items.size() - 1; i > 0; i--) {
						paintMain.setColor(color_01);
						canvas.drawArc(rectfNac, Items.get(i), (float) 18.5, false, paintMain);
					}
					paintMain.setColor(color_02);
					canvas.drawArc(rectfNac, Items.get(0), (float) 18.5, false, paintMain);	
					
					String valueText = 0 + " ";
					textPaint.setStyle(Style.FILL);
					textPaint.setColor(color_02);
					textPaint.setTypeface(Typeface.DEFAULT_BOLD);
					textPaint.setTextSize(valueS);
					float wid = (float)(valueX - 0.5*textPaint.measureText(valueText));
					canvas.drawText(valueText, wid, valueY, textPaint);
					
					valueText = "MELODY";
					textPaint.setStyle(Style.FILL);
					textPaint.setColor(color_02);
					textPaint.setTypeface(Typeface.DEFAULT_BOLD);
					textPaint.setTextSize(titleS);
					wid = (float)(titleX - 0.5*textPaint.measureText(valueText));
					canvas.drawText(valueText, wid, titleY, textPaint);
				}
				
			} else {

				zlightdrawActive.setBounds(rectVien);
				zlightdrawActive.draw(canvas);
				if (boolFocus) {
					zlightdrawClickHO.setBounds(rectTangGiam);
					zlightdrawClickHO.draw(canvas);
				} else {
					zlightdrawClickOK.setBounds(rectTangGiam);
					zlightdrawClickOK.draw(canvas);
				}
				paintMain.setStyle(Style.STROKE);
				paintMain.setStrokeWidth(strokeArc);
//				for (int i = Items.size() - 1; i > 0; i--) {
//					paintMain.setColor(color_03);
//					canvas.drawArc(rectfNac, Items.get(i), (float) 18.5, false, paintMain);
//				}
//				paintMain.setColor(color_05);
//				canvas.drawArc(rectfNac, Items.get(0), (float) 18.5, false, paintMain);	
				
				String valueText = "MELODY";
				textPaint.setStyle(Style.FILL);
				textPaint.setColor(color_04);
				textPaint.setTypeface(Typeface.DEFAULT_BOLD);
				textPaint.setTextSize(titleS);
				float wid = (float)(titleX - 0.5*textPaint.measureText(valueText));
				canvas.drawText(valueText, wid, titleY, textPaint);
			
			}
			
		
		}
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
		int parentWidth = MeasureSpec.getSize(widthMeasureSpec);		
		if (parentHeight > parentWidth) {
			setMeasuredDimension(parentWidth, parentWidth);
		} else {
			setMeasuredDimension(parentHeight, parentHeight);
		}
	}
	
	private float ysave = 0;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(boolBlockComand == true){
			return true;
		}
		if (MyApplication.intWifiRemote == MyApplication.SONCA) {
			if (TouchMainActivity.serverStatus != null || KTVMainActivity.serverStatus != null) {
				boolean flag = false;
				if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
					if (KTVMainActivity.serverStatus.isPaused()) {
						flag = true;
					}
				} else {
					if (TouchMainActivity.serverStatus.isPaused()) {
						flag = true;
					}
				}
				
				if (flag) {
					return true;
				}
			}
			if(stateView == VISIBLE){
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN: {
					float x = event.getX();
					float y = event.getY();
					if(y >= LINETAB1 && y <= LINETAB2){
						intCongTru = CONGTRU;
						invalidate();
						break;
					}
					if (x>=RDL && x<=RDR && y>=RDT && y<=RDB) {
						invalidate();
						break;
					}
					if(x <= RDL){
						if(y >= 0 && y <= height/2){
							intCongTru = CONG;
						}else {
							intCongTru = TRU;
						}	
					}						
					invalidate();
				}break;
				case MotionEvent.ACTION_MOVE: {
					float x = event.getX();
					float y = event.getY();
					if(y >= LINETAB1 && y <= LINETAB2){
						intCongTru = CONGTRU;
						invalidate();
						break;
					}
					if(rectShowTab != null)
					if(x >= rectShowTab.left && x <= rectShowTab.right &&
						y >= rectShowTab.top && y <= rectShowTab.bottom){
						intCongTru = CONGTRU;
						invalidate();
						break;
					}
					if(x <= RDL){
						if(y >= 0 && y <= height/2){
							intCongTru = CONG;
						}else {
							intCongTru = TRU;
						}
					}
					invalidate();
				}break;
				case MotionEvent.ACTION_UP: {
					float x = event.getX();
					intCongTru = CONGTRU;
					float y = event.getY();
					if(y >= LINETAB1 && y <= LINETAB2){
						if(x >= rectShowTab.left && x <= rectShowTab.right){
							if(listener != null){
								listener.OnShowTab(intMelody);
							}	
						}						
						invalidate();
						break;
					}
					if(rectShowTab != null){
						if(x >= rectShowTab.left && x <= rectShowTab.right &&
							y >= rectShowTab.top && y <= rectShowTab.bottom){
							if(listener != null){
								listener.OnShowTab(intMelody);
							}
							break;
						}
					}
					if(x > rectShowTab.left){
						if(listener != null){
							listener.OnShowTab(intMelody);
						}
						invalidate();
						break;
					}
					
					if(x <= RDL){
						if(y >= 0 && y <= height/2){
							intMelody++;
							if(intMelody >= 10){
								intMelody = 10;
							}
						}else {
							intMelody--;
							if(intMelody <= 0){
								intMelody = 0;
							}
						}
						if (listener != null) {
							longtimersync = System.currentTimeMillis();
							listener.onMelody(intMelody);
						}	
					}					
					invalidate();
				}break;
				default : break;
				}
			} else {
				if(event.getAction() == MotionEvent.ACTION_UP){
					if(listener != null){
						listener.OnInActive();
					}
				}
			}
			return true;
		}else{
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				boolFocus = true;
				invalidate();
				break;
				
			case MotionEvent.ACTION_UP:
				boolFocus = false;
				invalidate();
				if(listener != null) {
					listener.onMelody(1);
				}
				break;
			default:	break;
			}
			return true;
		}
	}
	
	private long longtimersync = 0;
	public void setMelody(int melody){
		if(MyApplication.intWifiRemote == MyApplication.SONCA){
			if(System.currentTimeMillis() - longtimersync 
				<= MyApplication.TIMER_SYNC){
				return;
			}
			if(stateView != View.INVISIBLE){
				intMelody = melody;
				angle = (int) (Items.get(intMelody) + 10);
				// if(getCommandEnable()){
					invalidate();
				// }
			}
		}
	}
	
	public int getMelody(){
		return intMelody;
	}

	public static final int INTCOMMAND = 64;
	public static void setCommandEnable(boolean bool){
		if (bool) {
			MyApplication.intCommandEnable |= INTCOMMAND;
		} else {
			MyApplication.intCommandEnable &= (~INTCOMMAND);
		}
	}
	
	public static boolean getCommandEnable(){
		return (MyApplication.intCommandEnable & INTCOMMAND) == INTCOMMAND;
	}
	
	public static final int INTMEDIUM = 10;
	public static void setCommandMedium(int value){
		int clear = 0x00000003;
		MyApplication.intCommandMedium &= (~(clear << INTMEDIUM));
		MyApplication.intCommandMedium |= (value << INTMEDIUM);
	}
	
	public static int getCommandMedium(){
		int clear = 0x00000003;
		int retur = (MyApplication.intCommandMedium & (clear << INTMEDIUM)) >> INTMEDIUM;
		return retur;
	}

}
