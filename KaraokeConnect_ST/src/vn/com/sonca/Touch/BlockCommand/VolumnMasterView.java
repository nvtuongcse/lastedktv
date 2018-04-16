package vn.com.sonca.Touch.BlockCommand;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.zktv.main.KTVMainActivity;
import vn.com.sonca.zzzzz.MyApplication;

public class VolumnMasterView extends View {

	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	
	private final int CONGTRU = 0;
	private final int CONG = 1;
	private final int TRU = -1;
	private int intCongTru = CONGTRU;
	
	private String TAB = "VolumnMasterView";
	private ArrayList<Integer> Items;
	private Drawable drawable;

	private float DP;
	private boolean boolFocus = false;
	private int width = 350;
	private int height = 350;
	private int mRadius;
	private int angle = 180;
	private int intVolumn = 1;
	private int intSaveVolumn = intVolumn;

	private boolean isFocusMute;
	private boolean isMute;

	private int stateView = View.INVISIBLE;

	public void setEnableView(int value) {
		stateView = value;
		if(value == View.INVISIBLE){
			boolFocus = false;
		}
		invalidate();
	}

	public VolumnMasterView(Context context) {
		super(context);
		this.context = context;
		initView();
	}

	public VolumnMasterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
	}

	public VolumnMasterView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initView();
	}

	private OnVolumnListener listener;

	public interface OnVolumnListener {
		public void onVolumn(int value);
		public void OnInActive();
		public void onMute(boolean isMute);
// 		public void OnShowTab(int intSliderVolumn);
	}

	public void setOnVolumnListener(OnVolumnListener listener) {
		this.listener = listener;
	}

	private Context context;
	
	private Drawable drawValue;
	private Drawable drawBackr;
	private Drawable drawMuteAC;
	private Drawable drawActive;
	private Drawable drawInActive;
	// private Drawable drawShowTabAC;
	private Drawable drawShowTabIN;
	private Drawable drawHoverCong;
	private Drawable drawHoverTru;
	private Drawable drawCongTruAC;
	private Drawable drawCongTruIN;
	
	private Drawable zlightdrawValue;
	private Drawable zlightdrawBackr;
	private Drawable zlightdrawMuteAC;
	private Drawable zlightdrawActive;
	private Drawable zlightdrawInActive;
	// private Drawable zlightdrawShowTabAC;
	private Drawable zlightdrawShowTabIN;
	private Drawable zlightdrawHoverCong;
	private Drawable zlightdrawHoverTru;
	private Drawable zlightdrawCongTruAC;
	private Drawable zlightdrawCongTruIN;
	
	private void initView() {
		drawMuteAC = getResources().getDrawable(R.drawable.touch_loa_cham_boder_98x98);
		drawActive = getResources().getDrawable(R.drawable.touch_vongtron_melody_new_button);
		drawInActive = getResources().getDrawable(R.drawable.touch_vongtron_melody_xam_new_button);
		// drawShowTabAC = getResources().getDrawable(R.drawable.icon_active_new_button);
		drawShowTabIN = getResources().getDrawable(R.drawable.touch_icon_xam_new_button);
		drawHoverCong = getResources().getDrawable(R.drawable.touch_hover_cong);
		drawHoverTru = getResources().getDrawable(R.drawable.touch_hover_tru);
		drawCongTruAC = getResources().getDrawable(R.drawable.touch_active_congtru);
		drawCongTruIN = getResources().getDrawable(R.drawable.touch_xam_congtru);
		
		zlightdrawMuteAC = getResources().getDrawable(R.drawable.zlight_loa_off);
		zlightdrawActive = getResources().getDrawable(R.drawable.zlight_vongtron_melody_new_button);
		zlightdrawInActive = getResources().getDrawable(R.drawable.zlight_vongtron_melody_xam_new_button);
		// zlightdrawShowTabAC = getResources().getDrawable(R.drawable.zlight_icon_active);
		zlightdrawShowTabIN = getResources().getDrawable(R.drawable.zlight_icon_xam);
		zlightdrawHoverCong = getResources().getDrawable(R.drawable.zlight_hover_cong);
		zlightdrawHoverTru = getResources().getDrawable(R.drawable.zlight_hover_tru);
		zlightdrawCongTruAC = getResources().getDrawable(R.drawable.zlight_active_congtru);
		zlightdrawCongTruIN = getResources().getDrawable(R.drawable.zlight_xam_congtru);
		
		isMute = true;
		DP = getResources().getDisplayMetrics().density;		
		Items = new ArrayList<Integer>();
		Items.add(106);
		Items.add(120);
		Items.add(135);
		Items.add(150);
		Items.add(165);
		Items.add(180);
		Items.add(195);

		Items.add(210);
		Items.add(225);
		Items.add(240);
		Items.add(255);
		Items.add(270);

		Items.add(285);
		Items.add(300);
		Items.add(315);
		Items.add(330);
		Items.add(345);
	}

	public void setVolumn(int intVolumn) {
		if(MyApplication.intWifiRemote == MyApplication.SONCA){
			if(stateView != View.INVISIBLE){
				if (intVolumn >= Items.size() - 1) {
					this.intVolumn = Items.size() - 1;
				} else {
					this.intVolumn = intVolumn;
				}
				angle = Items.get(this.intVolumn) + 5;
			}
		}
	}

	private long longtimersync = 0;
	public void setMute(boolean isMute) {
		if(MyApplication.intWifiRemote == MyApplication.SONCA){
			if(System.currentTimeMillis() - longtimersync 
				<= MyApplication.TIMER_SYNC){
				return;
			}
			this.isMute = isMute;
			if (isMute == false) {
				this.intVolumn = 0;
				angle = Items.get(intVolumn) + 5;
			}
			invalidate();
		}
	}

	public boolean isMute() {
		return isMute;
	}
	
	public boolean getEnableStatus(){
		if(stateView == View.INVISIBLE){
			return false;
		}
		
		if(boolBlockComand){
			return false;
		}
		
		if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			if(KTVMainActivity.serverStatus != null){
				if(KTVMainActivity.serverStatus.isPaused()){
					return false;
				}
			}
		} else {
			if(TouchMainActivity.serverStatus != null){
				if(TouchMainActivity.serverStatus.isPaused()){
					return false;
				}
			}	
		}
		
		
		return true;
	}
	

	private float KT1 = 0;
	private int widthView;
	private int heightView;
	private float strokeArc;
	private float valueX, valueY, valueS;
	private float titleX, titleY, titleS;
	private int offsetImageX, offsetImageY;
	private Rect rectVien = new Rect();
	private Rect rectTangGiam = new Rect();
	private Rect rectShowTab = new Rect();
	private Rect rectMute = new Rect();
	private RectF rectfNac = new RectF();
	private float K;
	private int RDL, RDR, RDT, RDB;
	private float tamXM, tamYM, RM, RS;
	private int LINETAB1, LINETAB2;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		widthView = w ; heightView = h;
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
		
		//--------------------//
		
		K = w / 2;
		RM = (float) (19.5 * K / 75);
		RS = (float) (3 * K / 75);
		tamXM = (float) (0.80*h);
		tamYM = tamXM;

		RDL = (int) (tamXM - RM - RS / 64);
		RDT = (int) (tamXM - RM - RS / 64);
		RDR = (int) (tamXM + RM + RS / 64);
		RDB = (int) (tamXM + RM + RS / 64);
		
		LINETAB1 = (int) (0.43*h);
		LINETAB2 = (int) (0.58*h);
		
	}

	private int color_01;
	private int color_02;
	private int color_03;
	private int color_04;
	private int color_05;
	private int color_06;
	private int color_07;
	private boolean boolBlockComand = false;
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		MyLog.e(TAB, "onDraw - VolumnMasterView");
		
		/*
		intVolumn = 5;
		stateView = View.VISIBLE;
		MyApplication.intWifiRemote = MyApplication.SONCA;
		MyApplication.intColorScreen = MyApplication.SCREEN_BLUE;
		*/

		stateView = View.VISIBLE;
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			color_01 = Color.argb(0, 152, 152, 152);
			color_02 = Color.argb(255, 152, 152, 152);
			color_03 = Color.argb(255, 0, 253, 253);
			color_04 = Color.argb(255, 255, 255, 255);
			color_05 = Color.RED;
			color_06 = Color.argb(51, 255, 255, 255);
			color_07 = Color.argb(204, 180, 254, 255);
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			color_01 = Color.argb(0, 152, 152, 152);
			color_02 = Color.argb(255, 152, 152, 152);
			color_03 = Color.parseColor("#21BAA9");
			color_04 = Color.parseColor("#66696C");
			color_05 = Color.RED;
			color_06 = Color.argb(51, 255, 255, 255);
			color_07 = Color.parseColor("#21BAA9");
		}
		
// BACKVIEW
		
		if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
				|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
				 || MyApplication.intSvrModel == MyApplication.SONCA_KBX9 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
				 || MyApplication.intSvrModel == MyApplication.SONCA_TBT || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
			if(Items.size() == 17){
				Items.remove(16);
			}
		} else {
			if(Items.size() != 17){
				Items = new ArrayList<Integer>();
				Items.add(106);
				Items.add(120);
				Items.add(135);
				Items.add(150);
				Items.add(165);
				Items.add(180);
				Items.add(195);

				Items.add(210);
				Items.add(225);
				Items.add(240);
				Items.add(255);
				Items.add(270);

				Items.add(285);
				Items.add(300);
				Items.add(315);
				Items.add(330);
				Items.add(345);
			}
		}
		
		/*
		intVolumn = 10;
		stateView = View.VISIBLE;
		MyApplication.intColorScreen = 1;
		MyApplication.intWifiRemote = MyApplication.SONCA;
		MyApplication.intColorScreen = MyApplication.SCREEN_BLUE;		
		*/
		if(TouchMainActivity.serverStatus != null || KTVMainActivity.serverStatus != null){
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
			drawable = getResources().getDrawable(R.drawable.touch_mc_mute_inactive);
			drawable.setBounds(RDL, RDT, RDR, RDB);
			drawable.draw(canvas);

			paintMain.setStyle(Style.STROKE);
			paintMain.setColor(color_06);
			paintMain.setStrokeWidth(RS);
			canvas.drawCircle(tamXM, tamXM, RM, paintMain);

			paintMain.setStyle(Style.STROKE);
			paintMain.setStrokeWidth(strokeArc);
			for (int i = Items.size() - 1; i > 0; i--) {
				paintMain.setColor(color_01);
				canvas.drawArc(rectfNac, Items.get(i), 11, false, paintMain);
			}
			paintMain.setColor(color_02);
			canvas.drawArc(rectfNac, Items.get(0) , (float) 11, false , paintMain);
			
			String valueText = 0 + " ";
			textPaint.setStyle(Style.FILL);
			textPaint.setColor(color_02);
			textPaint.setTypeface(Typeface.DEFAULT_BOLD);
			textPaint.setTextSize(valueS);
			float wid = (float)(valueX - 0.5*textPaint.measureText(valueText));
			canvas.drawText(valueText, wid, valueY, textPaint);
			
			valueText = "VOLUME";
			textPaint.setStyle(Style.FILL);
			textPaint.setColor(color_02);
			textPaint.setTypeface(Typeface.DEFAULT_BOLD);
			textPaint.setTextSize(titleS);
			wid = (float)(titleX - 0.5*textPaint.measureText(valueText));
			canvas.drawText(valueText, wid, titleY, textPaint);
			
			return;
		}
		
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			// DRAW 0
			if(MyApplication.intWifiRemote == MyApplication.SONCA){
				
				if(stateView == View.VISIBLE){
					drawActive.setBounds(rectVien);
					drawActive.draw(canvas);
					drawShowTabIN.setBounds(rectShowTab);
					drawShowTabIN.draw(canvas);
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
					if (isMute) {
						drawable = getResources().getDrawable(R.drawable.touch_mc_mute_off_96x96);
					} else {
						drawable = getResources().getDrawable(R.drawable.touch_mc_mute_on_96x96);
					}
					if (stateView == View.INVISIBLE) {
						drawable = getResources().getDrawable(R.drawable.touch_mc_mute_inactive);
					}
					drawable.setBounds(RDL, RDT, RDR, RDB);
					drawable.draw(canvas);
					if(clickMute){
						drawMuteAC.setBounds(RDL, RDT, RDR, RDB);
						drawMuteAC.draw(canvas);
					}else{
						paintMain.setStyle(Style.STROKE);
						if (stateView == View.INVISIBLE) {
							paintMain.setColor(color_06);
						}else{
							paintMain.setColor(color_07);
						}
						paintMain.setStrokeWidth(RS);
						canvas.drawCircle(tamXM, tamXM, RM, paintMain);
					}
					paintMain.setStyle(Style.STROKE);
					paintMain.setStrokeWidth(strokeArc);
					if(!isMute){
						for (int i = Items.size() - 1; i > 0; i--) {
							paintMain.setColor(color_01);
							canvas.drawArc(rectfNac, Items.get(i), 11, false, paintMain);
						}
					}else{
						for (int i = Items.size() - 1; i > 0; i--) {
							if (i > intVolumn) {
								paintMain.setColor(color_01);
							} else {
								paintMain.setColor(color_03);				
							}
							canvas.drawArc(rectfNac, Items.get(i), 11, false, paintMain);
						}
					}
					paintMain.setColor(color_05);
					canvas.drawArc(rectfNac, Items.get(0) , (float) 11, false , paintMain);
					String valueText = "";
					if (!isMute) {
						valueText = 0 + " ";
					} else {
						valueText = intVolumn + " ";
					}
					textPaint.setStyle(Style.FILL);
					textPaint.setColor(color_03);
					textPaint.setTypeface(Typeface.DEFAULT_BOLD);
					textPaint.setTextSize(valueS);
					float wid = (float)(valueX - 0.5*textPaint.measureText(valueText));
					canvas.drawText(valueText, wid, valueY, textPaint);
					
					valueText = "VOLUME";
					textPaint.setStyle(Style.FILL);
					textPaint.setColor(color_04);
					textPaint.setTypeface(Typeface.DEFAULT_BOLD);
					textPaint.setTextSize(titleS);
					wid = (float)(titleX - 0.5*textPaint.measureText(valueText));
					canvas.drawText(valueText, wid, titleY, textPaint);
				}else{
/*
					drawInActive.setBounds(rectVien);
					drawInActive.draw(canvas);
					
					drawShowTabIN.setBounds(rectShowTab);
					drawShowTabIN.draw(canvas);
					
					drawCongTruIN.setBounds(rectTangGiam);
					drawCongTruIN.draw(canvas);
					drawable = getResources().getDrawable(R.drawable.mc_mute_inactive);
					drawable.setBounds(RDL, RDT, RDR, RDB);
					drawable.draw(canvas);

					paintMain.setStyle(Style.STROKE);
					paintMain.setColor(color_06);
					paintMain.setStrokeWidth(RS);
					canvas.drawCircle(tamXM, tamXM, RM, paintMain);

					paintMain.setStyle(Style.STROKE);
					paintMain.setStrokeWidth(strokeArc);
					for (int i = Items.size() - 1; i > 0; i--) {
						paintMain.setColor(color_01);
						canvas.drawArc(rectfNac, Items.get(i), 11, false, paintMain);
					}
					paintMain.setColor(color_02);
					canvas.drawArc(rectfNac, Items.get(0) , (float) 11, false , paintMain);
					
					String valueText = 0 + " ";
					textPaint.setStyle(Style.FILL);
					textPaint.setColor(color_02);
					textPaint.setTypeface(Typeface.DEFAULT_BOLD);
					textPaint.setTextSize(valueS);
					float wid = (float)(valueX - 0.5*textPaint.measureText(valueText));
					// canvas.drawText(valueText, wid, valueY, textPaint);
					
					valueText = "VOLUME";
					textPaint.setStyle(Style.FILL);
					textPaint.setColor(color_02);
					textPaint.setTypeface(Typeface.DEFAULT_BOLD);
					textPaint.setTextSize(titleS);
					wid = (float)(titleX - 0.5*textPaint.measureText(valueText));
					canvas.drawText(valueText, wid, titleY, textPaint);
*/
				}
				
			} else {
				
				drawActive.setBounds(rectVien);
				drawActive.draw(canvas);
				switch (intVomOther) {
				case 0:
					drawCongTruAC.setBounds(rectTangGiam);
					drawCongTruAC.draw(canvas);
					break;
				case 1:
					drawHoverCong.setBounds(rectTangGiam);
					drawHoverCong.draw(canvas);
					break;
				case -1:
					drawHoverTru.setBounds(rectTangGiam);
					drawHoverTru.draw(canvas);
					break;
				default: break;
				}
				drawable = getResources().getDrawable(R.drawable.touch_mc_mute_off_96x96);
				drawable.setBounds(RDL, RDT, RDR, RDB);
				drawable.draw(canvas);
				if(clickMute){
					drawMuteAC.setBounds(RDL, RDT, RDR, RDB);
					drawMuteAC.draw(canvas);
				}else{
					paintMain.setStyle(Style.STROKE);
					if (stateView == View.INVISIBLE) {
						paintMain.setColor(color_06);
					}else{
						paintMain.setColor(color_07);
					}
					paintMain.setStrokeWidth(RS);
					canvas.drawCircle(tamXM, tamXM, RM, paintMain);
				}
				paintMain.setStyle(Style.STROKE);
				paintMain.setStrokeWidth(strokeArc);
				
				String valueText = "VOLUME";
				textPaint.setStyle(Style.FILL);
				textPaint.setColor(color_04);
				textPaint.setTypeface(Typeface.DEFAULT_BOLD);
				textPaint.setTextSize(titleS);
				float wid = (float)(titleX - 0.5*textPaint.measureText(valueText));
				canvas.drawText(valueText, wid, titleY, textPaint);
			}
			
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {

			// DRAW 0
			if(MyApplication.intWifiRemote == MyApplication.SONCA){
				
				if(stateView == View.VISIBLE){
					zlightdrawActive.setBounds(rectVien);
					zlightdrawActive.draw(canvas);
					zlightdrawShowTabIN.setBounds(rectShowTab);
					zlightdrawShowTabIN.draw(canvas);
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
					if (isMute) {
						drawable = getResources().getDrawable(R.drawable.zlight_loa_off);
					} else {
						drawable = getResources().getDrawable(R.drawable.zlight_loa_on);
					}
					if (stateView == View.INVISIBLE) {
						drawable = getResources().getDrawable(R.drawable.touch_mc_mute_inactive);
					}
					drawable.setBounds(RDL, RDT, RDR, RDB);
					drawable.draw(canvas);
					/*
					if(clickMute){
						zlightdrawMuteAC.setBounds(RDL, RDT, RDR, RDB);
						zlightdrawMuteAC.draw(canvas);
					}else{
						paintMain.setStyle(Style.STROKE);
						if (stateView == View.INVISIBLE) {
							paintMain.setColor(color_06);
						}else{
							paintMain.setColor(color_07);
						}
						paintMain.setStrokeWidth(RS);
						canvas.drawCircle(tamXM, tamXM, RM, paintMain);
					}
					*/
					paintMain.setStyle(Style.STROKE);
					paintMain.setStrokeWidth(strokeArc);
					if(!isMute){
						for (int i = Items.size() - 1; i > 0; i--) {
							paintMain.setColor(color_01);
							canvas.drawArc(rectfNac, Items.get(i), 11, false, paintMain);
						}
					}else{
						for (int i = Items.size() - 1; i > 0; i--) {
							if (i > intVolumn) {
								paintMain.setColor(color_01);
							} else {
								paintMain.setColor(color_03);				
							}
							canvas.drawArc(rectfNac, Items.get(i), 11, false, paintMain);
						}
					}
					paintMain.setColor(color_05);
					canvas.drawArc(rectfNac, Items.get(0) , (float) 11, false , paintMain);
					String valueText = "";
					if (!isMute) {
						valueText = 0 + " ";
					} else {
						valueText = intVolumn + " ";
					}
					textPaint.setStyle(Style.FILL);
					textPaint.setColor(color_03);
					textPaint.setTypeface(Typeface.DEFAULT_BOLD);
					textPaint.setTextSize(valueS);
					float wid = (float)(valueX - 0.5*textPaint.measureText(valueText));
					canvas.drawText(valueText, wid, valueY, textPaint);
					
					valueText = "VOLUME";
					textPaint.setStyle(Style.FILL);
					textPaint.setColor(color_04);
					textPaint.setTypeface(Typeface.DEFAULT_BOLD);
					textPaint.setTextSize(titleS);
					wid = (float)(titleX - 0.5*textPaint.measureText(valueText));
					canvas.drawText(valueText, wid, titleY, textPaint);
				}else{
/*
					zlightdrawInActive.setBounds(rectVien);
					zlightdrawInActive.draw(canvas);
					
					zlightdrawShowTabIN.setBounds(rectShowTab);
					zlightdrawShowTabIN.draw(canvas);
					
					zlightdrawCongTruIN.setBounds(rectTangGiam);
					zlightdrawCongTruIN.draw(canvas);
					drawable = getResources().getDrawable(R.drawable.mc_mute_inactive);
					drawable.setBounds(RDL, RDT, RDR, RDB);
					drawable.draw(canvas);

					paintMain.setStyle(Style.STROKE);
					paintMain.setColor(color_06);
					paintMain.setStrokeWidth(RS);
					canvas.drawCircle(tamXM, tamXM, RM, paintMain);

					paintMain.setStyle(Style.STROKE);
					paintMain.setStrokeWidth(strokeArc);
					for (int i = Items.size() - 1; i > 0; i--) {
						paintMain.setColor(color_01);
						canvas.drawArc(rectfNac, Items.get(i), 11, false, paintMain);
					}
					paintMain.setColor(color_02);
					canvas.drawArc(rectfNac, Items.get(0) , (float) 11, false , paintMain);
					
					String valueText = 0 + " ";
					textPaint.setStyle(Style.FILL);
					textPaint.setColor(color_02);
					textPaint.setTypeface(Typeface.DEFAULT_BOLD);
					textPaint.setTextSize(valueS);
					float wid = (float)(valueX - 0.5*textPaint.measureText(valueText));
					// canvas.drawText(valueText, wid, valueY, textPaint);
					
					valueText = "VOLUME";
					textPaint.setStyle(Style.FILL);
					textPaint.setColor(color_02);
					textPaint.setTypeface(Typeface.DEFAULT_BOLD);
					textPaint.setTextSize(titleS);
					wid = (float)(titleX - 0.5*textPaint.measureText(valueText));
					canvas.drawText(valueText, wid, titleY, textPaint);
*/				
				}
				
			} else {
				
				zlightdrawActive.setBounds(rectVien);
				zlightdrawActive.draw(canvas);
				switch (intVomOther) {
				case 0:
					zlightdrawCongTruAC.setBounds(rectTangGiam);
					zlightdrawCongTruAC.draw(canvas);
					break;
				case 1:
					zlightdrawHoverCong.setBounds(rectTangGiam);
					zlightdrawHoverCong.draw(canvas);
					break;
				case -1:
					zlightdrawHoverTru.setBounds(rectTangGiam);
					zlightdrawHoverTru.draw(canvas);
					break;
				default: break;
				}
				drawable = getResources().getDrawable(R.drawable.touch_mc_mute_off_96x96);
				drawable.setBounds(RDL, RDT, RDR, RDB);
				drawable.draw(canvas);
				if(clickMute){
					zlightdrawMuteAC.setBounds(RDL, RDT, RDR, RDB);
					zlightdrawMuteAC.draw(canvas);
				}else{
					paintMain.setStyle(Style.STROKE);
					if (stateView == View.INVISIBLE) {
						paintMain.setColor(color_06);
					}else{
						paintMain.setColor(color_07);
					}
					paintMain.setStrokeWidth(RS);
					canvas.drawCircle(tamXM, tamXM, RM, paintMain);
				}
				paintMain.setStyle(Style.STROKE);
				paintMain.setStrokeWidth(strokeArc);
				
				String valueText = "VOLUME";
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

	private int intVomOther = 0;
	private boolean clickMute = false;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(boolBlockComand == true){
			return true;
		}
		if(MyApplication.intWifiRemote == MyApplication.SONCA){
				if (stateView == VISIBLE) {
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
							clickMute = true;
							invalidate();
							break;
						}
						if(x <= RDL){
							if(y >= 0 && y <= heightView/2){
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
							clickMute = false;
							intCongTru = CONGTRU;
							invalidate();
							break;
						}
						if (x>=RDL && x<=RDR && y>=RDT && y<=RDB) {
							clickMute = true;
							intCongTru = CONGTRU;
							invalidate();
							break;
						}
						if(x <= RDL){
							if(y >= 0 && y <= heightView/2){
								intCongTru = CONG;
							}else {
								intCongTru = TRU;
							}		
							clickMute = false;
						}					
						invalidate();
					}break;
					case MotionEvent.ACTION_UP: {
						float x = event.getX();
						float y = event.getY();
						clickMute = false;
						boolFocus = false;
						isFocusMute = false;
						intCongTru = CONGTRU;
						if(y >= LINETAB1 && y <= LINETAB2){
							if(listener != null){
								// listener.OnShowTab(intVolumn);
							}
							invalidate();
							break;
						}
						if(rectShowTab != null){
							if(x >= rectShowTab.left && x <= rectShowTab.right &&
								y >= rectShowTab.top && y <= rectShowTab.bottom){
								if(listener != null){
									// listener.OnShowTab(intVolumn);
								}
								break;
							}
						}
						if (x>=RDL && x<=RDR && y>=RDT && y<=RDB) {
							isMute = !isMute;
							if (listener != null) {
								longtimersync = System.currentTimeMillis();
								listener.onMute(isMute);
							}
							invalidate();
							break;
						}
						if(x <= RDL){
							if(y >= 0 && y <= heightView/2){
								intVolumn++;
								if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
										|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
										 || MyApplication.intSvrModel == MyApplication.SONCA_KBX9
										 || MyApplication.intSvrModel == MyApplication.SONCA_TBT || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
									if(intVolumn >= 15){
										intVolumn = 15;
									}
								} else {
									if(intVolumn >= 16){
										intVolumn = 16;
									}
								}
							}else {
								intVolumn--;
								if(intVolumn <= 0){
									intVolumn = 0;
								}
							}
							if (listener != null) {
								longtimersync = System.currentTimeMillis();
								listener.onVolumn(intVolumn);
							}	
						}					
						invalidate();
					}break;
					default: break;
					}
				} else {
					if(event.getAction() == MotionEvent.ACTION_UP){
						if(listener != null){
							listener.OnInActive();
						}
					}
				}
				return true;
		} else {
			
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:{
					float x = event.getX();
					float y = event.getY();
					if (x>=RDL && x<=RDR && y>=RDT && y<=RDB) {
						intVomOther = 0;
						clickMute = true;
						invalidate();
						break;
					}
					if(y <= heightView/2){
						intVomOther = 1;
						invalidate();
					}else{
						intVomOther = -1;
						invalidate();
					}
				}break;
	
				case MotionEvent.ACTION_MOVE:{
					float x = event.getX();
					float y = event.getY();
					if (x>=RDL && x<=RDR && y>=RDT && y<=RDB) {
						intVomOther = 0;
						clickMute = true;
						invalidate();
						break;
					}
					clickMute = false;
					if(y <= heightView/2){
						intVomOther = 1;
						invalidate();
					}else{
						intVomOther = -1;
						invalidate();
					}
				}break;
				
				case MotionEvent.ACTION_UP:{
					clickMute = false;
					float x = event.getX();
					float y = event.getY();
					if (x>=RDL && x<=RDR && y>=RDT && y<=RDB) {
						intVomOther = 0;
						invalidate();
						if (listener != null) {
							listener.onMute(isMute);
						}
					}else{
						if(y <= heightView/2){
							intVomOther = 1;
							if(listener != null){
								listener.onVolumn(1);
							}
							intVomOther = 0;
							invalidate();
						}else{
							intVomOther = -1;
							if(listener != null){
								listener.onVolumn(-1);
							}
							intVomOther = 0;
							invalidate();
						}
					}
				}break;
					
				default:	break;
				}
				return true;
		}
	}

	public int getVolumn() {
		return intVolumn;
	}

	public static final int INTCOMMAND = 4;
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

	public static final int INTMEDIUM = 2;
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
