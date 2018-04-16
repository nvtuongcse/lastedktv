package vn.com.sonca.Touch.CustomView;

import java.util.ArrayList;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.utils.AppConfig.MEDIA_TYPE;
import vn.com.sonca.zktv.main.KTVMainActivity;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
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

public class TouchKeyView extends View {

	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	
	private String TAB = "KeyView";
	private ArrayList<Integer> Items;
	
	private final int CONGTRU = 0;
	private final int CONG = 1;
	private final int TRU = -1;
	private int intCongTru = CONGTRU;
	
	private float DP;
	private boolean boolFocus = false;
	private int width = 350;
	private int height = 350;
	private int mRadius;
	private int angle = 180;
	private int intKey = 0;
	
	private Context context;

	public TouchKeyView(Context context) {
		super(context);
		this.context = context;
		initView();
	}
	
	public TouchKeyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
	}
	
	public TouchKeyView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initView();
	}
	
	private OnKeyListener listener;
	public interface OnKeyListener {
		public void onKey(int value);
		public void OnInActive();
		public void OnShowTab(int intSliderVolumn);
	}
	
	public void setOnKeyListener(OnKeyListener listener){
		this.listener = listener;
	}
	
	private Drawable drawActive;
	private Drawable drawInActive;
	private Drawable drawShowTabAC;
	private Drawable drawShowTabIN;

	private Drawable drawHoverCong;
	private Drawable drawHoverTru;
	private Drawable drawCongTruAC;
	private Drawable drawCongTruIN;
	
	private Drawable drawThang;
	private Drawable drawGiang;
	private Drawable drawBinhAC;
	private Drawable drawBinhIN;
	
	private Drawable zlightdrawActive;
	private Drawable zlightdrawInActive;
	private Drawable zlightdrawShowTabAC;
	private Drawable zlightdrawShowTabIN;
	private Drawable zlightdrawHoverCong;
	private Drawable zlightdrawHoverTru;
	private Drawable zlightdrawCongTruAC;
	private Drawable zlightdrawCongTruIN;
	private Drawable zlightdrawThang;
	private Drawable zlightdrawGiang;
	private Drawable zlightdrawBinhAC;
	private Drawable zlightdrawBinhIN;
	
	private void initView(){
		drawActive = getResources().getDrawable(R.drawable.touch_vongtron_melody_new_button);
		drawInActive = getResources().getDrawable(R.drawable.touch_vongtron_melody_xam_new_button);
		drawShowTabAC = getResources().getDrawable(R.drawable.touch_icon_active_new_button);
		drawShowTabIN = getResources().getDrawable(R.drawable.touch_icon_xam_new_button);
		
		drawHoverCong = getResources().getDrawable(R.drawable.touch_hover_thang);
		drawHoverTru = getResources().getDrawable(R.drawable.touch_hover_giang);
		drawCongTruAC = getResources().getDrawable(R.drawable.touch_active_thanggiang);
		drawCongTruIN = getResources().getDrawable(R.drawable.touch_xam_thanggiang);
		
		drawThang = getResources().getDrawable(R.drawable.touch_icon_thang_new);
		drawGiang = getResources().getDrawable(R.drawable.touch_icon_giang_new);
		drawBinhAC = getResources().getDrawable(R.drawable.touch_icon_binh_active_new);
		drawBinhIN = getResources().getDrawable(R.drawable.touch_icon_binh_inactive_new);
		
		zlightdrawActive = getResources().getDrawable(R.drawable.zlight_vongtron_melody_new_button);
		zlightdrawInActive = getResources().getDrawable(R.drawable.zlight_vongtron_melody_xam_new_button);
		zlightdrawShowTabAC = getResources().getDrawable(R.drawable.zlight_icon_active);
		zlightdrawShowTabIN = getResources().getDrawable(R.drawable.zlight_icon_xam);
		zlightdrawHoverCong = getResources().getDrawable(R.drawable.zlight_hover_thang);
		zlightdrawHoverTru = getResources().getDrawable(R.drawable.zlight_hover_giang);
		zlightdrawCongTruAC = getResources().getDrawable(R.drawable.zlight_active_thanggiang);
		zlightdrawCongTruIN = getResources().getDrawable(R.drawable.zlight_xam_thanggiang);
		zlightdrawThang = getResources().getDrawable(R.drawable.zlight_icon_thang_new);
		zlightdrawGiang = getResources().getDrawable(R.drawable.zlight_icon_giang_new);
		zlightdrawBinhAC = getResources().getDrawable(R.drawable.zlight_icon_binh_active_new);
		zlightdrawBinhIN = getResources().getDrawable(R.drawable.touch_icon_binh_inactive_new);		
		
		DP = getResources().getDisplayMetrics().density;
		Items = new ArrayList<Integer>();
		Items.add(13);
		Items.add(39);
		Items.add(65);
		Items.add(91);
		Items.add(117);
		Items.add(143);
		
		Items.add(169);
		
		Items.add(195);
		Items.add(221);
		Items.add(247);
		Items.add(273);
		Items.add(299);
		Items.add(325);
	}

	private int stateView = View.VISIBLE;
	public void setEnableView(int value){
		if(MyApplication.intWifiRemote == MyApplication.SONCA){
			stateView = value;
			if(value == View.INVISIBLE){
				boolFocus = false;
				intKey = 0;
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

	private boolean boolBlockComand = false;
	
	private int color_01;
	private int color_02;
	private int color_03;
	private int color_04;
	private int color_05;
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		/*
		 * intKey = 1; stateView = View.VISIBLE; MyApplication.intWifiRemote =
		 * MyApplication.SONCA; MyApplication.intColorScreen =
		 * MyApplication.SCREEN_LIGHT;
		 */
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			color_01 = Color.argb(0, 152, 152, 152);
			color_02 = Color.argb(255, 152, 152, 152);
			color_03 = Color.argb(255, 0, 253, 253);
			color_04 = Color.argb(255, 255, 255, 255);
			color_05 = Color.YELLOW;
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
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
				if ((MyApplication.flagDeviceUser == true && retur != 0)
						|| (MyApplication.flagDeviceUser == false && retur == 2)) {
					boolBlockComand = true;
				} else {
					boolBlockComand = false;
				}
			} else {
				if ((MyApplication.intCommandEnable & INTCOMMAND) != INTCOMMAND) {
					boolBlockComand = true;
				} else {
					boolBlockComand = false;
				}
			}
		}

		if (boolBlockComand == true) {
			drawInActive.setBounds(rectVien);
			drawInActive.draw(canvas);
			drawShowTabIN.setBounds(rectShowTab);
			drawShowTabIN.draw(canvas);
			drawCongTruIN.setBounds(rectTangGiam);
			drawCongTruIN.draw(canvas);
			drawBinhIN.setBounds(rectThangGiang);
			drawBinhIN.draw(canvas);

			paintMain.setStyle(Style.STROKE);
			paintMain.setStrokeWidth(strokeArc);
			for (int i = 6; i > 0; i--) {
				paintMain.setColor(color_01);
				canvas.drawArc(rectfNac, Items.get(i + 6), 22, false, paintMain);
			}
			for (int i = -6; i < 0; i++) {
				paintMain.setColor(color_01);
				canvas.drawArc(rectfNac, Items.get(i + 6), 22, false, paintMain);
			}
			paintMain.setColor(color_02);
			canvas.drawArc(rectfNac, Items.get(6), 22, false, paintMain);

			String valueText = 0 + "";
			textPaint.setStyle(Style.FILL);
			textPaint.setColor(color_02);
			textPaint.setTypeface(Typeface.DEFAULT_BOLD);
			textPaint.setTextSize(valueS);
			float wid = (float) (valueX - 0.5 * textPaint
					.measureText(valueText));
			canvas.drawText(valueText, wid, valueY, textPaint);

			valueText = "KEY";
			textPaint.setStyle(Style.FILL);
			textPaint.setColor(color_02);
			textPaint.setTypeface(Typeface.DEFAULT_BOLD);
			textPaint.setTextSize(titleS);
			wid = (float) (titleX - 0.5 * textPaint.measureText(valueText));
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
				drawBinhIN.setBounds(rectThangGiang);
				drawBinhIN.draw(canvas);

				paintMain.setStyle(Style.STROKE);
				paintMain.setStrokeWidth(strokeArc);
				for (int i = 6; i > 0; i--) {
					paintMain.setColor(color_01);
					canvas.drawArc(rectfNac, Items.get(i + 6), 22, false,
							paintMain);
				}
				for (int i = -6; i < 0; i++) {
					paintMain.setColor(color_01);
					canvas.drawArc(rectfNac, Items.get(i + 6), 22, false,
							paintMain);
				}
				paintMain.setColor(color_02);
				canvas.drawArc(rectfNac, Items.get(6), 22, false, paintMain);

				String valueText = 0 + "";
				textPaint.setStyle(Style.FILL);
				textPaint.setColor(color_02);
				textPaint.setTypeface(Typeface.DEFAULT_BOLD);
				textPaint.setTextSize(valueS);
				float wid = (float) (valueX - 0.5 * textPaint
						.measureText(valueText));
				canvas.drawText(valueText, wid, valueY, textPaint);

				valueText = "KEY";
				textPaint.setStyle(Style.FILL);
				textPaint.setColor(color_02);
				textPaint.setTypeface(Typeface.DEFAULT_BOLD);
				textPaint.setTextSize(titleS);
				wid = (float) (titleX - 0.5 * textPaint.measureText(valueText));
				canvas.drawText(valueText, wid, titleY, textPaint);

				return;
			}
		}
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			// DRAW 0
			if (MyApplication.intSvrModel == MyApplication.SONCA || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK) {
				if (stateView == View.VISIBLE) {
					drawActive.setBounds(rectVien);
					drawActive.draw(canvas);

					boolean flagShow = true;

					if (TouchMainActivity.serverStatus != null || KTVMainActivity.serverStatus != null) {
						int mediaType = 0x07;
						if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
							mediaType = KTVMainActivity.serverStatus.getMediaType();
						} else {
							mediaType = TouchMainActivity.serverStatus.getMediaType();
						}

						if (mediaType != 0x07 && mediaType != -1) {
							MEDIA_TYPE aType = MEDIA_TYPE.values()[mediaType];
							if (aType != MEDIA_TYPE.MIDI) {
								flagShow = false;
							}
						}
					}

					if (flagShow) {
						drawShowTabAC.setBounds(rectShowTab);
						drawShowTabAC.draw(canvas);
					} else {
						drawShowTabIN.setBounds(rectShowTab);
						drawShowTabIN.draw(canvas);
					}

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
					default:
						break;
					}
					paintMain.setStyle(Style.STROKE);
					paintMain.setStrokeWidth(strokeArc);
					for (int i = 6; i > 0; i--) {
						if (i > intKey) {
							paintMain.setColor(color_01);
						} else {
							paintMain.setColor(color_03);
						}
						canvas.drawArc(rectfNac, Items.get(i + 6), 22, false,
								paintMain);
					}
					for (int i = -6; i < 0; i++) {
						if (i < intKey) {
							paintMain.setColor(color_01);
						} else {
							paintMain.setColor(color_03);
						}
						canvas.drawArc(rectfNac, Items.get(i + 6), 22, false,
								paintMain);
					}
					paintMain.setColor(color_05);
					canvas.drawArc(rectfNac, Items.get(6), 22, false, paintMain);

					String valueText = "";
					if (intKey == 0) {
						valueText = 0 + " ";
						drawBinhAC.setBounds(rectThangGiang);
						drawBinhAC.draw(canvas);
					} else if (intKey > 0) {
						valueText = intKey + "";
						drawThang.setBounds(rectThangGiang);
						drawThang.draw(canvas);
					} else {
						valueText = (-intKey) + "";
						drawGiang.setBounds(rectThangGiang);
						drawGiang.draw(canvas);
					}

					textPaint.setStyle(Style.FILL);
					textPaint.setColor(color_03);
					textPaint.setTypeface(Typeface.DEFAULT_BOLD);
					textPaint.setTextSize(valueS);
					float wid = (float) (valueX - 0.5 * textPaint
							.measureText(valueText));
					canvas.drawText(valueText, wid, valueY, textPaint);

					valueText = "KEY";
					textPaint.setStyle(Style.FILL);
					textPaint.setColor(color_04);
					textPaint.setTypeface(Typeface.DEFAULT_BOLD);
					textPaint.setTextSize(titleS);
					wid = (float) (titleX - 0.5 * textPaint
							.measureText(valueText));
					canvas.drawText(valueText, wid, titleY, textPaint);

				} else {

					drawInActive.setBounds(rectVien);
					drawInActive.draw(canvas);
					drawShowTabIN.setBounds(rectShowTab);
					drawShowTabIN.draw(canvas);
					drawCongTruIN.setBounds(rectTangGiam);
					drawCongTruIN.draw(canvas);
					drawBinhIN.setBounds(rectThangGiang);
					drawBinhIN.draw(canvas);

					paintMain.setStyle(Style.STROKE);
					paintMain.setStrokeWidth(strokeArc);
					for (int i = 6; i > 0; i--) {
						paintMain.setColor(color_01);
						canvas.drawArc(rectfNac, Items.get(i + 6), 22, false,
								paintMain);
					}
					for (int i = -6; i < 0; i++) {
						paintMain.setColor(color_01);
						canvas.drawArc(rectfNac, Items.get(i + 6), 22, false,
								paintMain);
					}
					paintMain.setColor(color_02);
					canvas.drawArc(rectfNac, Items.get(6), 22, false, paintMain);

					String valueText = 0 + "";
					textPaint.setStyle(Style.FILL);
					textPaint.setColor(color_02);
					textPaint.setTypeface(Typeface.DEFAULT_BOLD);
					textPaint.setTextSize(valueS);
					float wid = (float) (valueX - 0.5 * textPaint
							.measureText(valueText));
					canvas.drawText(valueText, wid, valueY, textPaint);

					valueText = "KEY";
					textPaint.setStyle(Style.FILL);
					textPaint.setColor(color_02);
					textPaint.setTypeface(Typeface.DEFAULT_BOLD);
					textPaint.setTextSize(titleS);
					wid = (float) (titleX - 0.5 * textPaint
							.measureText(valueText));
					canvas.drawText(valueText, wid, titleY, textPaint);

				}

			} else if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
					|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
					 || MyApplication.intSvrModel == MyApplication.SONCA_KBX9 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
					 || MyApplication.intSvrModel == MyApplication.SONCA_TBT){
				// DRAW HIW
				boolean flagShow = true;
				boolean isNotSinger = true;
				if (TouchMainActivity.serverStatus != null || KTVMainActivity.serverStatus != null) {
					int mediaType = 0x07;
					if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
						mediaType = KTVMainActivity.serverStatus.getMediaType();
					} else {
						mediaType = TouchMainActivity.serverStatus.getMediaType();
					}
					
					if (mediaType != 0x07 && mediaType != -1) {
						MEDIA_TYPE aType = MEDIA_TYPE.values()[mediaType];
						if (aType != MEDIA_TYPE.MIDI) {
							flagShow = false;
						}
						if (aType == MEDIA_TYPE.VIDEO) {
							isNotSinger = false;
						}
					}
				}
				if (stateView == View.VISIBLE && isNotSinger) {
					drawActive.setBounds(rectVien);
					drawActive.draw(canvas);

					if (flagShow) {
						drawShowTabAC.setBounds(rectShowTab);
						drawShowTabAC.draw(canvas);
					} else {
						drawShowTabIN.setBounds(rectShowTab);
						drawShowTabIN.draw(canvas);
					}

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
					default:
						break;
					}
					paintMain.setStyle(Style.STROKE);
					paintMain.setStrokeWidth(strokeArc);
					for (int i = 6; i > 0; i--) {
						if (i > intKey) {
							paintMain.setColor(color_01);
						} else {
							paintMain.setColor(color_03);
						}
						canvas.drawArc(rectfNac, Items.get(i + 6), 22, false,
								paintMain);
					}
					for (int i = -6; i < 0; i++) {
						if (i < intKey) {
							paintMain.setColor(color_01);
						} else {
							paintMain.setColor(color_03);
						}
						canvas.drawArc(rectfNac, Items.get(i + 6), 22, false,
								paintMain);
					}
					paintMain.setColor(color_05);
					canvas.drawArc(rectfNac, Items.get(6), 22, false, paintMain);

					String valueText = "";
					if (intKey == 0) {
						valueText = 0 + " ";
						drawBinhAC.setBounds(rectThangGiang);
						drawBinhAC.draw(canvas);
					} else if (intKey > 0) {
						valueText = intKey + "";
						drawThang.setBounds(rectThangGiang);
						drawThang.draw(canvas);
					} else {
						valueText = (-intKey) + "";
						drawGiang.setBounds(rectThangGiang);
						drawGiang.draw(canvas);
					}

					textPaint.setStyle(Style.FILL);
					textPaint.setColor(color_03);
					textPaint.setTypeface(Typeface.DEFAULT_BOLD);
					textPaint.setTextSize(valueS);
					float wid = (float) (valueX - 0.5 * textPaint
							.measureText(valueText));
					canvas.drawText(valueText, wid, valueY, textPaint);

					valueText = "KEY";
					textPaint.setStyle(Style.FILL);
					textPaint.setColor(color_04);
					textPaint.setTypeface(Typeface.DEFAULT_BOLD);
					textPaint.setTextSize(titleS);
					wid = (float) (titleX - 0.5 * textPaint
							.measureText(valueText));
					canvas.drawText(valueText, wid, titleY, textPaint);

				} else {

					drawInActive.setBounds(rectVien);
					drawInActive.draw(canvas);
					drawShowTabIN.setBounds(rectShowTab);
					drawShowTabIN.draw(canvas);
					drawCongTruIN.setBounds(rectTangGiam);
					drawCongTruIN.draw(canvas);
					drawBinhIN.setBounds(rectThangGiang);
					drawBinhIN.draw(canvas);

					paintMain.setStyle(Style.STROKE);
					paintMain.setStrokeWidth(strokeArc);
					for (int i = 6; i > 0; i--) {
						paintMain.setColor(color_01);
						canvas.drawArc(rectfNac, Items.get(i + 6), 22, false,
								paintMain);
					}
					for (int i = -6; i < 0; i++) {
						paintMain.setColor(color_01);
						canvas.drawArc(rectfNac, Items.get(i + 6), 22, false,
								paintMain);
					}
					paintMain.setColor(color_02);
					canvas.drawArc(rectfNac, Items.get(6), 22, false, paintMain);

					String valueText = 0 + "";
					textPaint.setStyle(Style.FILL);
					textPaint.setColor(color_02);
					textPaint.setTypeface(Typeface.DEFAULT_BOLD);
					textPaint.setTextSize(valueS);
					float wid = (float) (valueX - 0.5 * textPaint
							.measureText(valueText));
					canvas.drawText(valueText, wid, valueY, textPaint);

					valueText = "KEY";
					textPaint.setStyle(Style.FILL);
					textPaint.setColor(color_02);
					textPaint.setTypeface(Typeface.DEFAULT_BOLD);
					textPaint.setTextSize(titleS);
					wid = (float) (titleX - 0.5 * textPaint
							.measureText(valueText));
					canvas.drawText(valueText, wid, titleY, textPaint);

				}

			} else {

				drawActive.setBounds(rectVien);
				drawActive.draw(canvas);
				switch (intKeyOther) {
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
				default:
					break;
				}
				paintMain.setStyle(Style.STROKE);
				paintMain.setStrokeWidth(strokeArc);
				// for (int i = 6; i > 0; i--) {
				// paintMain.setColor(color_03);
				// canvas.drawArc(rectfNac, Items.get(i + 6), 22, false,
				// paintMain);
				// }
				// for (int i = -6; i < 0; i++) {
				// paintMain.setColor(color_03);
				// canvas.drawArc(rectfNac, Items.get(i + 6), 22, false,
				// paintMain);
				// }
				// paintMain.setColor(color_05);
				// canvas.drawArc(rectfNac, Items.get(6) , 22, false ,
				// paintMain);

				String valueText = "KEY";
				textPaint.setStyle(Style.FILL);
				textPaint.setColor(color_04);
				textPaint.setTypeface(Typeface.DEFAULT_BOLD);
				textPaint.setTextSize(titleS);
				float wid = (float) (titleX - 0.5 * textPaint
						.measureText(valueText));
				canvas.drawText(valueText, wid, titleY, textPaint);

			}

		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {

			// DRAW 0
			if (MyApplication.intSvrModel == MyApplication.SONCA || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK) {
				if (stateView == View.VISIBLE) {
					zlightdrawActive.setBounds(rectVien);
					zlightdrawActive.draw(canvas);

					boolean flagShow = true;

					if (TouchMainActivity.serverStatus != null || KTVMainActivity.serverStatus != null) {
						int mediaType = 0x07;
						if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
							mediaType = KTVMainActivity.serverStatus.getMediaType();
						} else {
							mediaType = TouchMainActivity.serverStatus.getMediaType();
						}
						
						if (mediaType != 0x07 && mediaType != -1) {
							MEDIA_TYPE aType = MEDIA_TYPE.values()[mediaType];
							if (aType != MEDIA_TYPE.MIDI) {
								flagShow = false;
							}
						}
					}

					if (flagShow) {
						zlightdrawShowTabAC.setBounds(rectShowTab);
						zlightdrawShowTabAC.draw(canvas);
					} else {
						zlightdrawShowTabIN.setBounds(rectShowTab);
						zlightdrawShowTabIN.draw(canvas);
					}

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
					default:
						break;
					}
					paintMain.setStyle(Style.STROKE);
					paintMain.setStrokeWidth(strokeArc);
					for (int i = 6; i > 0; i--) {
						if (i > intKey) {
							paintMain.setColor(color_01);
						} else {
							paintMain.setColor(color_03);
						}
						canvas.drawArc(rectfNac, Items.get(i + 6), 22, false,
								paintMain);
					}
					for (int i = -6; i < 0; i++) {
						if (i < intKey) {
							paintMain.setColor(color_01);
						} else {
							paintMain.setColor(color_03);
						}
						canvas.drawArc(rectfNac, Items.get(i + 6), 22, false,
								paintMain);
					}
					paintMain.setColor(color_05);
					canvas.drawArc(rectfNac, Items.get(6), 22, false, paintMain);

					String valueText = "";
					if (intKey == 0) {
						valueText = 0 + " ";
						zlightdrawBinhAC.setBounds(rectThangGiang);
						zlightdrawBinhAC.draw(canvas);
					} else if (intKey > 0) {
						valueText = intKey + "";
						zlightdrawThang.setBounds(rectThangGiang);
						zlightdrawThang.draw(canvas);
					} else {
						valueText = (-intKey) + "";
						zlightdrawGiang.setBounds(rectThangGiang);
						zlightdrawGiang.draw(canvas);
					}

					textPaint.setStyle(Style.FILL);
					textPaint.setColor(color_03);
					textPaint.setTypeface(Typeface.DEFAULT_BOLD);
					textPaint.setTextSize(valueS);
					float wid = (float) (valueX - 0.5 * textPaint
							.measureText(valueText));
					canvas.drawText(valueText, wid, valueY, textPaint);

					valueText = "KEY";
					textPaint.setStyle(Style.FILL);
					textPaint.setColor(color_04);
					textPaint.setTypeface(Typeface.DEFAULT_BOLD);
					textPaint.setTextSize(titleS);
					wid = (float) (titleX - 0.5 * textPaint
							.measureText(valueText));
					canvas.drawText(valueText, wid, titleY, textPaint);

				} else {

					zlightdrawInActive.setBounds(rectVien);
					zlightdrawInActive.draw(canvas);
					zlightdrawShowTabIN.setBounds(rectShowTab);
					zlightdrawShowTabIN.draw(canvas);
					zlightdrawCongTruIN.setBounds(rectTangGiam);
					zlightdrawCongTruIN.draw(canvas);
					zlightdrawBinhIN.setBounds(rectThangGiang);
					zlightdrawBinhIN.draw(canvas);

					paintMain.setStyle(Style.STROKE);
					paintMain.setStrokeWidth(strokeArc);
					for (int i = 6; i > 0; i--) {
						paintMain.setColor(color_01);
						canvas.drawArc(rectfNac, Items.get(i + 6), 22, false,
								paintMain);
					}
					for (int i = -6; i < 0; i++) {
						paintMain.setColor(color_01);
						canvas.drawArc(rectfNac, Items.get(i + 6), 22, false,
								paintMain);
					}
					paintMain.setColor(color_02);
					canvas.drawArc(rectfNac, Items.get(6), 22, false, paintMain);

					String valueText = 0 + "";
					textPaint.setStyle(Style.FILL);
					textPaint.setColor(color_02);
					textPaint.setTypeface(Typeface.DEFAULT_BOLD);
					textPaint.setTextSize(valueS);
					float wid = (float) (valueX - 0.5 * textPaint
							.measureText(valueText));
					canvas.drawText(valueText, wid, valueY, textPaint);

					valueText = "KEY";
					textPaint.setStyle(Style.FILL);
					textPaint.setColor(color_02);
					textPaint.setTypeface(Typeface.DEFAULT_BOLD);
					textPaint.setTextSize(titleS);
					wid = (float) (titleX - 0.5 * textPaint
							.measureText(valueText));
					canvas.drawText(valueText, wid, titleY, textPaint);

				}

			} else if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
					|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
					 || MyApplication.intSvrModel == MyApplication.SONCA_KBX9 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
					 || MyApplication.intSvrModel == MyApplication.SONCA_TBT){
				// DRAW HIW
				boolean flagShow = true;
				boolean isNotSinger = true;
				if (TouchMainActivity.serverStatus != null || KTVMainActivity.serverStatus != null) {
					int mediaType = 0x07;
					if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
						mediaType = KTVMainActivity.serverStatus.getMediaType();
					} else {
						mediaType = TouchMainActivity.serverStatus.getMediaType();
					}
					
					if (mediaType != 0x07 && mediaType != -1) {
						MEDIA_TYPE aType = MEDIA_TYPE.values()[mediaType];
						if (aType != MEDIA_TYPE.MIDI) {
							flagShow = false;
						}
						if (aType == MEDIA_TYPE.VIDEO) {
							isNotSinger = false;
						}
					}
				}
				if (stateView == View.VISIBLE && isNotSinger) {
					zlightdrawActive.setBounds(rectVien);
					zlightdrawActive.draw(canvas);

					if (flagShow) {
						zlightdrawShowTabAC.setBounds(rectShowTab);
						zlightdrawShowTabAC.draw(canvas);
					} else {
						zlightdrawShowTabIN.setBounds(rectShowTab);
						zlightdrawShowTabIN.draw(canvas);
					}

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
					default:
						break;
					}
					paintMain.setStyle(Style.STROKE);
					paintMain.setStrokeWidth(strokeArc);
					for (int i = 6; i > 0; i--) {
						if (i > intKey) {
							paintMain.setColor(color_01);
						} else {
							paintMain.setColor(color_03);
						}
						canvas.drawArc(rectfNac, Items.get(i + 6), 22, false,
								paintMain);
					}
					for (int i = -6; i < 0; i++) {
						if (i < intKey) {
							paintMain.setColor(color_01);
						} else {
							paintMain.setColor(color_03);
						}
						canvas.drawArc(rectfNac, Items.get(i + 6), 22, false,
								paintMain);
					}
					paintMain.setColor(color_05);
					canvas.drawArc(rectfNac, Items.get(6), 22, false, paintMain);

					String valueText = "";
					if (intKey == 0) {
						valueText = 0 + " ";
						zlightdrawBinhAC.setBounds(rectThangGiang);
						zlightdrawBinhAC.draw(canvas);
					} else if (intKey > 0) {
						valueText = intKey + "";
						zlightdrawThang.setBounds(rectThangGiang);
						zlightdrawThang.draw(canvas);
					} else {
						valueText = (-intKey) + "";
						zlightdrawGiang.setBounds(rectThangGiang);
						zlightdrawGiang.draw(canvas);
					}

					textPaint.setStyle(Style.FILL);
					textPaint.setColor(color_03);
					textPaint.setTypeface(Typeface.DEFAULT_BOLD);
					textPaint.setTextSize(valueS);
					float wid = (float) (valueX - 0.5 * textPaint
							.measureText(valueText));
					canvas.drawText(valueText, wid, valueY, textPaint);

					valueText = "KEY";
					textPaint.setStyle(Style.FILL);
					textPaint.setColor(color_04);
					textPaint.setTypeface(Typeface.DEFAULT_BOLD);
					textPaint.setTextSize(titleS);
					wid = (float) (titleX - 0.5 * textPaint
							.measureText(valueText));
					canvas.drawText(valueText, wid, titleY, textPaint);

				} else {

					zlightdrawInActive.setBounds(rectVien);
					zlightdrawInActive.draw(canvas);
					zlightdrawShowTabIN.setBounds(rectShowTab);
					zlightdrawShowTabIN.draw(canvas);
					zlightdrawCongTruIN.setBounds(rectTangGiam);
					zlightdrawCongTruIN.draw(canvas);
					zlightdrawBinhIN.setBounds(rectThangGiang);
					zlightdrawBinhIN.draw(canvas);

					paintMain.setStyle(Style.STROKE);
					paintMain.setStrokeWidth(strokeArc);
					for (int i = 6; i > 0; i--) {
						paintMain.setColor(color_01);
						canvas.drawArc(rectfNac, Items.get(i + 6), 22, false,
								paintMain);
					}
					for (int i = -6; i < 0; i++) {
						paintMain.setColor(color_01);
						canvas.drawArc(rectfNac, Items.get(i + 6), 22, false,
								paintMain);
					}
					paintMain.setColor(color_02);
					canvas.drawArc(rectfNac, Items.get(6), 22, false, paintMain);

					String valueText = 0 + "";
					textPaint.setStyle(Style.FILL);
					textPaint.setColor(color_02);
					textPaint.setTypeface(Typeface.DEFAULT_BOLD);
					textPaint.setTextSize(valueS);
					float wid = (float) (valueX - 0.5 * textPaint
							.measureText(valueText));
					canvas.drawText(valueText, wid, valueY, textPaint);

					valueText = "KEY";
					textPaint.setStyle(Style.FILL);
					textPaint.setColor(color_02);
					textPaint.setTypeface(Typeface.DEFAULT_BOLD);
					textPaint.setTextSize(titleS);
					wid = (float) (titleX - 0.5 * textPaint
							.measureText(valueText));
					canvas.drawText(valueText, wid, titleY, textPaint);

				}

			} else {

				zlightdrawActive.setBounds(rectVien);
				zlightdrawActive.draw(canvas);
				switch (intKeyOther) {
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
				default:
					break;
				}
				paintMain.setStyle(Style.STROKE);
				paintMain.setStrokeWidth(strokeArc);
				// for (int i = 6; i > 0; i--) {
				// paintMain.setColor(color_03);
				// canvas.drawArc(rectfNac, Items.get(i + 6), 22, false,
				// paintMain);
				// }
				// for (int i = -6; i < 0; i++) {
				// paintMain.setColor(color_03);
				// canvas.drawArc(rectfNac, Items.get(i + 6), 22, false,
				// paintMain);
				// }
				// paintMain.setColor(color_05);
				// canvas.drawArc(rectfNac, Items.get(6) , 22, false ,
				// paintMain);

				String valueText = "KEY";
				textPaint.setStyle(Style.FILL);
				textPaint.setColor(color_04);
				textPaint.setTypeface(Typeface.DEFAULT_BOLD);
				textPaint.setTextSize(titleS);
				float wid = (float) (titleX - 0.5 * textPaint
						.measureText(valueText));
				canvas.drawText(valueText, wid, titleY, textPaint);

			}

		}

	}
	
	private float KT1 = 0;
	private float strokeArc;
	private float valueX, valueY, valueS;
	private float titleX, titleY, titleS;
	private int offsetImageX, offsetImageY;
	private Rect rectVien = new Rect();
	private Rect rectTangGiam = new Rect();
	private Rect rectShowTab = new Rect();
	private Rect rectThangGiang = new Rect();
	private RectF rectfNac = new RectF();
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
		
		int tamX = (int) (0.815*h);
		tamY = (int) (0.26*h);
		hr = (int) (0.07*w);
		wr = hr;
		rectThangGiang.set(tamX - wr, tamY - hr, tamX + wr, tamY + hr);
		
		LINETAB1 = (int) (0.43*h);
		LINETAB2 = (int) (0.58*h);
		
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

	private int intKeyOther = 0;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(boolBlockComand == true){
			return true;
		}
		if(MyApplication.intSvrModel == MyApplication.SONCA || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
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
				if (stateView == View.VISIBLE) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN: {
						float x = event.getX();
						float y = event.getY();
						if(y >= LINETAB1 && y <= LINETAB2){
							intCongTru = CONGTRU;
							invalidate();
							break;
						}
						boolFocus = true;
						if(x <= rectShowTab.left){
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
						if(rectShowTab != null){
						if(x >= rectShowTab.left && x <= rectShowTab.right &&
							y >= rectShowTab.top && y <= rectShowTab.bottom){
								intCongTru = CONGTRU;
								invalidate();
								break;
							}
						}
						if(x <= rectShowTab.left){
							if(y >= 0 && y <= height/2){
								intCongTru = CONG;
							}else {
								intCongTru = TRU;
							}	
						}						
						invalidate();
					}break;
					case MotionEvent.ACTION_UP: {
						boolFocus = false;
						intCongTru = CONGTRU;
						float x = event.getX();
						float y = event.getY();
						if(y >= LINETAB1 && y <= LINETAB2){
							if(x >= rectShowTab.left && x <= rectShowTab.right){
								if(listener != null){
									listener.OnShowTab(intKey);
								}	
							}							
							invalidate();
							break;
						}
						if(rectShowTab != null){
							if(x >= rectShowTab.left && x <= rectShowTab.right &&
								y >= rectShowTab.top && y <= rectShowTab.bottom){
								if(listener != null){
									listener.OnShowTab(intKey);
								}
								invalidate();
								break;
							}
						}
						if(x <= rectShowTab.left){
							if(y >= 0 && y <= height/2){
								intKey++;
								if(intKey > 6){
									intKey = 0;
								}
							}else {
								intKey--;
								if(intKey < -6){
									intKey = 0;
								}
							}
							if(listener != null) {
								longtimersync = System.currentTimeMillis();
								listener.onKey(intKey);
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
		} else if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
				|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
				 || MyApplication.intSvrModel == MyApplication.SONCA_KBX9 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
				 || MyApplication.intSvrModel == MyApplication.SONCA_TBT){
				// DRAW HIW
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
				if (TouchMainActivity.serverStatus != null || KTVMainActivity.serverStatus != null) {
					int mediaType = 0x07;
					if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
						mediaType = KTVMainActivity.serverStatus.getMediaType();
					} else {
						mediaType = TouchMainActivity.serverStatus.getMediaType();
					}
					if(mediaType != 0x07 && mediaType != -1){
						MEDIA_TYPE aType = MEDIA_TYPE.values()[mediaType];
						if(aType == MEDIA_TYPE.VIDEO){
							return true;
						}
					}	
				}
				if (stateView == View.VISIBLE) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN: {
						float x = event.getX();
						float y = event.getY();
						if(y >= LINETAB1 && y <= LINETAB2){
							intCongTru = CONGTRU;
							invalidate();
							break;
						}
						boolFocus = true;
						if(x <= rectShowTab.left){
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
						if(rectShowTab != null){
						if(x >= rectShowTab.left && x <= rectShowTab.right &&
							y >= rectShowTab.top && y <= rectShowTab.bottom){
								intCongTru = CONGTRU;
								invalidate();
								break;
							}
						}
						if(x <= rectShowTab.left){
							if(y >= 0 && y <= height/2){
								intCongTru = CONG;
							}else {
								intCongTru = TRU;
							}	
						}						
						invalidate();
					}break;
					case MotionEvent.ACTION_UP: {
						boolFocus = false;
						intCongTru = CONGTRU;
						float x = event.getX();
						float y = event.getY();
						if(y >= LINETAB1 && y <= LINETAB2){
							if(listener != null){
								listener.OnShowTab(intKey);
							}
							invalidate();
							break;
						}
						if(y >= LINETAB1 && y <= LINETAB2){
							if(listener != null){
								listener.OnShowTab(intKey);
							}
							invalidate();
							break;
						}
						if(rectShowTab != null){
							if(x >= rectShowTab.left && x <= rectShowTab.right &&
								y >= rectShowTab.top && y <= rectShowTab.bottom){
								if(listener != null){
									listener.OnShowTab(intKey);
								}
								invalidate();
								break;
							}
						}
						if(x <= rectShowTab.left){
							if(y >= 0 && y <= height/2){
								intKey++;
								if(intKey > 6){
									intKey = 0;
								}
							}else {
								intKey--;
								if(intKey < -6){
									intKey = 0;
								}
							}
							if(listener != null) {
								longtimersync = System.currentTimeMillis();
								listener.onKey(intKey);
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
		
		} else {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:{
					float y = event.getY();
					if(y <= width/2){
						intKeyOther = 1;
						invalidate();
					}else{
						intKeyOther = -1;
						invalidate();
					}
				}break;

				case MotionEvent.ACTION_MOVE:{
					float y = event.getY();
					if(y <= width/2){
						intKeyOther = 1;
						invalidate();
					}else{
						intKeyOther = -1;
						invalidate();
					}
				}break;
				
				case MotionEvent.ACTION_UP:{
					float y = event.getY();
					if(y <= width/2){
						intKeyOther = 1;
						if(listener != null){
							listener.onKey(1);
						}
						intKeyOther = 0;
						invalidate();
					}else{
						intKeyOther = -1;
						if(listener != null){
							listener.onKey(-1);
						}
						intKeyOther = 0;
						invalidate();
					}
				}break;
					
				default:	break;
				}
				return true;
		}
	}
	
	private long longtimersync = 0;
	public void setKey(int tempo){
		if(MyApplication.intWifiRemote == MyApplication.SONCA){
			if(System.currentTimeMillis() - longtimersync 
				<= MyApplication.TIMER_SYNC){
				return;
			}
			if(stateView != View.INVISIBLE){
				intKey = tempo;
				if (intKey >= 6) {
					intKey = 6;
				}
				if(intKey <= -6){
					intKey = -6;
				}
				angle = Items.get(intKey + 6) + 11;
				invalidate();
			}
		}
	}
	
	public int getKey(){
		return intKey;
	}
	
	public static final int INTCOMMAND = 8;
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
	
	public static final int INTMEDIUM = 4;
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
