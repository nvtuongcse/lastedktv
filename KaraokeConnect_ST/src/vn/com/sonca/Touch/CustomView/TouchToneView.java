package vn.com.sonca.Touch.CustomView;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.utils.AppConfig.MEDIA_TYPE;
import vn.com.sonca.zktv.main.KTVMainActivity;
import vn.com.sonca.zzzzz.MyApplication;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;

public class TouchToneView extends View {
	
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
	//private Context context;
	// 0: Male, 2: Female, 1: Both
	private Drawable[] toneDrawables;
	private Drawable selectedDrawable;
	private Drawable drawableInActive;
	private int selectedTone;
	private String text;
	private String[] textGioiTinh;
	
    private boolean isTouch = false;
    
    private static final String STROKE_COLOR_1 = "#91d2d6";
    private static final String STROKE_COLOR_2 = "#001a31";
    private static final String STROKE_COLOR_3 = "#34495c";
    private static final String STROKE_COLOR_4 = "#011b32";
    private static final String TEXT_COLOR = "#00fdfd";

    public static final int MALE_TONE = 0;
    public static final int FEMALE_TONE = 2;
    public static final int BOTH_TONE = 1;
    
	private Drawable drawActive;
	private Drawable drawHover;
    
    private OnToneListener listener;
	
    public interface OnToneListener {
		public void onTone(int toneType);
		public void OnInActive();
	}
	
	public void setOnToneListener(OnToneListener listener){
		this.listener = listener;
	}
	
	public TouchToneView(Context context) {
		super(context);
		initView(context);
	}

	public TouchToneView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchToneView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private String textTitle = "";
	private String textState = "";
	
	private Drawable zlight_drawActive, zlight_drawHover, zlight_drawXam,
			zlight_icon_nam, zlight_icon_nu, zlight_icon_both,
			zlight_drawableInActive;
	private Drawable[] zlight_toneDrawables;

	private void initView(Context context) {
		// this.context = context;
		drawHover = getResources()
				.getDrawable(R.drawable.touch_ctr_actv_singer_bgrnd);
		drawActive = getResources().getDrawable(
				R.drawable.touch_image_singer_vongtronngoai_active);
		drawableInActive = getResources().getDrawable(
				R.drawable.touch_mc_tone_off_96x96);

		toneDrawables = new Drawable[3];
		toneDrawables[0] = getResources().getDrawable(
				R.drawable.touch_mc_tone_male_96x96);
		toneDrawables[1] = getResources().getDrawable(
				R.drawable.touch_mc_tone_both_96x96);
		toneDrawables[2] = getResources().getDrawable(
				R.drawable.touch_mc_tone_female_96x96);

		textGioiTinh = new String[3];
		textGioiTinh[0] = getResources().getString(R.string.tone_nam);
		textGioiTinh[1] = getResources().getString(R.string.tone_man_nu);
		textGioiTinh[2] = getResources().getString(R.string.tone_nu);

		textTitle = "TONE";

		zlight_drawActive = getResources().getDrawable(
				R.drawable.zlight_round_boder_active);
		zlight_drawHover = getResources().getDrawable(
				R.drawable.zlight_round_boder_hover);
		zlight_drawXam = getResources().getDrawable(
				R.drawable.zlight_round_boder_xam);

		zlight_drawableInActive = getResources().getDrawable(
				R.drawable.zlight_mc_tone_off_96x96);

		zlight_toneDrawables = new Drawable[3];
		zlight_toneDrawables[0] = getResources().getDrawable(
				R.drawable.zlight_mc_tone_male_96x96);
		zlight_toneDrawables[1] = getResources().getDrawable(
				R.drawable.zlight_mc_tone_both_96x96);
		zlight_toneDrawables[2] = getResources().getDrawable(
				R.drawable.zlight_mc_tone_female_96x96);
		
	}
	
	private long longtimersync = 0;
	public void setToneView(int tone){
		if(MyApplication.intWifiRemote == MyApplication.SONCA){
			if(System.currentTimeMillis() - longtimersync 
				<= MyApplication.TIMER_SYNC){
				return;
			}
			if(stateView != View.INVISIBLE){
				if(tone > 2 || tone < 0)
					tone = 0; 
				selectedTone = tone;
				invalidate();
			}
		}
	}
	
	public int getToneView(){
		return selectedTone;
	}
	
	private int stateView = View.VISIBLE;
	public void setEnableView(int value){
		stateView = value;
		selectedTone = 4;
		invalidate();
	}
	
	private float widthLayout;
	private float heightLayout;
	private float centerX, centerY;
	private float titleY, titleS;
	private float textStateS, textStateY;
	private float radXamTrong, stroXamTrong;
	private float radXamNgoai, stroXamNgoai;
	private Rect rectVien = new Rect();
	private Rect rectImageKar = new Rect();
	private Rect rectImageSonca = new Rect();
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		rectVien.set(0, 0, w, h);
		widthLayout = w;
		heightLayout = h;
		centerX = w/2;
		centerY = h/2;
		int r = 0;
		if (w > h) {
			r = h;
		} else {
			r = w;
		}
		stroXamTrong = (float) (0.07*r);
		radXamTrong = (float) (0.335*r);
		stroXamNgoai = (float) (0.03*r);
		radXamNgoai = (float) (0.45*r);
		
		int tamX = w/2;
		int tamY = h/2;
		int wi = (int) (0.28*r);
		int hi = wi;
		rectImageSonca.set(tamX - wi, tamY - hi, tamX + wi, tamY + hi);
		
		titleS = (float) (0.1*r);
		titleY = (float) (h/2 + hi - 0.3*titleS);
		textStateS = (float) (0.1*r);
		textStateY = (float) (h/2 - hi + 1.1*titleS);
		
	}
	
	private int colorTextActive;
	private int colorTextXam;
	
	private Drawable drawImage;
	private boolean boolBlockComand = false;
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		// =====================
		/*
		 * selectedTone = 0; isTouch = false; stateView = VISIBLE;
		 * MyApplication.intWifiRemote = MyApplication.SONCA;
		 * MyApplication.intColorScreen = MyApplication.SCREEN_BLUE;
		 * MyApplication.intCommandEnable = ~INTCOMMAND;
		 */
		// =====================

		if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			colorTextActive = Color.argb(255, 0, 82, 73);
			colorTextXam = Color.argb(255, 152, 152, 152);
		}

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

		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			if (boolBlockComand == true) {
				paintMain.setStyle(Style.STROKE);
				paintMain.setStrokeWidth(stroXamTrong);
				paintMain.setARGB(51, 255, 255, 255);
				canvas.drawCircle(centerX, centerY, radXamTrong, paintMain);

				paintMain.setStyle(Style.STROKE);
				paintMain.setStrokeWidth(stroXamNgoai);
				paintMain.setARGB(51, 255, 255, 255);
				canvas.drawCircle(centerX, centerY, radXamNgoai - stroXamNgoai,
						paintMain);

				textPaint.setStyle(Style.FILL);
				textPaint.setTextSize(titleS);
				textPaint.setColor(Color.GRAY);
				float width = textPaint.measureText(textTitle);
				canvas.drawText(textTitle,
						(float) (0.5 * widthLayout - 0.5 * width), titleY,
						textPaint);

				drawImage = drawableInActive;
				drawImage.setBounds(rectImageSonca);
				drawImage.draw(canvas);

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
					paintMain.setStyle(Style.STROKE);
					paintMain.setStrokeWidth(stroXamTrong);
					paintMain.setARGB(51, 255, 255, 255);
					canvas.drawCircle(centerX, centerY, radXamTrong, paintMain);

					paintMain.setStyle(Style.STROKE);
					paintMain.setStrokeWidth(stroXamNgoai);
					paintMain.setARGB(51, 255, 255, 255);
					canvas.drawCircle(centerX, centerY, radXamNgoai
							- stroXamNgoai, paintMain);

					textPaint.setStyle(Style.FILL);
					textPaint.setTextSize(titleS);
					textPaint.setColor(Color.GRAY);
					float width = textPaint.measureText(textTitle);
					canvas.drawText(textTitle,
							(float) (0.5 * widthLayout - 0.5 * width), titleY,
							textPaint);

					drawImage = drawableInActive;
					drawImage.setBounds(rectImageSonca);
					drawImage.draw(canvas);

					return;

				}
			}

			if (MyApplication.intSvrModel == MyApplication.SONCA || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK) {

				if (stateView == VISIBLE) {
					if (isTouch) {
						drawHover.setBounds(rectVien);
						drawHover.draw(canvas);
					} else {
						paintMain.setStyle(Style.STROKE);
						paintMain.setStrokeWidth(stroXamTrong);
						paintMain.setARGB(51, 255, 255, 255);
						canvas.drawCircle(centerX, centerY, radXamTrong,
								paintMain);

						drawActive.setBounds(rectVien);
						drawActive.draw(canvas);
					}

					if (selectedTone < 4) {
						drawImage = toneDrawables[selectedTone];
						textState = textGioiTinh[selectedTone];
					} else {
						drawImage = drawableInActive;
						textState = getResources().getString(
								R.string.main_left_5a);
					}
					drawImage.setBounds(rectImageSonca);
					drawImage.draw(canvas);

					textPaint.setStyle(Style.FILL);
					textPaint.setTextSize(textStateS);
					textPaint.setColor(Color.parseColor(TEXT_COLOR));
					float width = textPaint.measureText(textState);
					canvas.drawText(textState,
							(float) (0.5 * widthLayout - 0.5 * width),
							textStateY, textPaint);

					textPaint.setStyle(Style.FILL);
					textPaint.setTextSize(titleS);
					textPaint.setColor(Color.parseColor(TEXT_COLOR));
					width = textPaint.measureText(textTitle);
					canvas.drawText(textTitle,
							(float) (0.5 * widthLayout - 0.5 * width), titleY,
							textPaint);

				} else {
					paintMain.setStyle(Style.STROKE);
					paintMain.setStrokeWidth(stroXamTrong);
					paintMain.setARGB(51, 255, 255, 255);
					canvas.drawCircle(centerX, centerY, radXamTrong, paintMain);

					paintMain.setStyle(Style.STROKE);
					paintMain.setStrokeWidth(stroXamNgoai);
					paintMain.setARGB(51, 255, 255, 255);
					canvas.drawCircle(centerX, centerY, radXamNgoai
							- stroXamNgoai, paintMain);

					textPaint.setStyle(Style.FILL);
					textPaint.setTextSize(titleS);
					textPaint.setColor(Color.GRAY);
					float width = textPaint.measureText(textTitle);
					canvas.drawText(textTitle,
							(float) (0.5 * widthLayout - 0.5 * width), titleY,
							textPaint);

					drawImage = drawableInActive;
					drawImage.setBounds(rectImageSonca);
					drawImage.draw(canvas);
				}

			} else if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
					|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
					 || MyApplication.intSvrModel == MyApplication.SONCA_KBX9 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
					 || MyApplication.intSvrModel == MyApplication.SONCA_TBT){
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
						if (aType == MEDIA_TYPE.SINGER) {
							isNotSinger = false;
						}
						if (aType == MEDIA_TYPE.VIDEO) {
							isNotSinger = false;
						}
					}
				}
				if (stateView == VISIBLE && isNotSinger) {
					if (isTouch) {
						drawHover.setBounds(rectVien);
						drawHover.draw(canvas);
					} else {
						paintMain.setStyle(Style.STROKE);
						paintMain.setStrokeWidth(stroXamTrong);
						paintMain.setARGB(51, 255, 255, 255);
						canvas.drawCircle(centerX, centerY, radXamTrong,
								paintMain);

						drawActive.setBounds(rectVien);
						drawActive.draw(canvas);
					}

					if (selectedTone < 4) {
						drawImage = toneDrawables[selectedTone];
						textState = textGioiTinh[selectedTone];
					} else {
						drawImage = drawableInActive;
						textState = getResources().getString(
								R.string.main_left_5a);
					}
					drawImage.setBounds(rectImageSonca);
					drawImage.draw(canvas);

					textPaint.setStyle(Style.FILL);
					textPaint.setTextSize(textStateS);
					textPaint.setColor(Color.parseColor(TEXT_COLOR));
					float width = textPaint.measureText(textState);
					canvas.drawText(textState,
							(float) (0.5 * widthLayout - 0.5 * width),
							textStateY, textPaint);

					textPaint.setStyle(Style.FILL);
					textPaint.setTextSize(titleS);
					textPaint.setColor(Color.parseColor(TEXT_COLOR));
					width = textPaint.measureText(textTitle);
					canvas.drawText(textTitle,
							(float) (0.5 * widthLayout - 0.5 * width), titleY,
							textPaint);

				} else {
					paintMain.setStyle(Style.STROKE);
					paintMain.setStrokeWidth(stroXamTrong);
					paintMain.setARGB(51, 255, 255, 255);
					canvas.drawCircle(centerX, centerY, radXamTrong, paintMain);

					paintMain.setStyle(Style.STROKE);
					paintMain.setStrokeWidth(stroXamNgoai);
					paintMain.setARGB(51, 255, 255, 255);
					canvas.drawCircle(centerX, centerY, radXamNgoai
							- stroXamNgoai, paintMain);

					textPaint.setStyle(Style.FILL);
					textPaint.setTextSize(titleS);
					textPaint.setColor(Color.GRAY);
					float width = textPaint.measureText(textTitle);
					canvas.drawText(textTitle,
							(float) (0.5 * widthLayout - 0.5 * width), titleY,
							textPaint);

					drawImage = drawableInActive;
					drawImage.setBounds(rectImageSonca);
					drawImage.draw(canvas);
				}

			} else {
				if (isTouch) {
					drawHover.setBounds(rectVien);
					drawHover.draw(canvas);
				} else {
					paintMain.setStyle(Style.STROKE);
					paintMain.setStrokeWidth(stroXamTrong);
					paintMain.setARGB(51, 255, 255, 255);
					canvas.drawCircle(centerX, centerY, radXamTrong, paintMain);

					drawActive.setBounds(rectVien);
					drawActive.draw(canvas);
				}
				textPaint.setStyle(Style.FILL);
				textPaint.setTextSize(titleS);
				textPaint.setColor(Color.parseColor(TEXT_COLOR));
				float width = textPaint.measureText(textTitle);
				canvas.drawText(textTitle,
						(float) (0.5 * widthLayout - 0.5 * width), titleY,
						textPaint);

				drawImage = toneDrawables[1];
				drawImage.setBounds(rectImageSonca);
				drawImage.draw(canvas);

			}
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {

			if (boolBlockComand == true) {
				zlight_drawXam.setBounds(rectVien);
				zlight_drawXam.draw(canvas);

				zlight_drawableInActive.setBounds(rectImageSonca);
				zlight_drawableInActive.draw(canvas);

				textPaint.setStyle(Style.FILL);
				textPaint.setTextSize(titleS);
				textPaint.setColor(colorTextXam);
				float width = textPaint.measureText(textTitle);
				canvas.drawText(textTitle,
						(float) (0.5 * widthLayout - 0.5 * width), titleY,
						textPaint);

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
					zlight_drawXam.setBounds(rectVien);
					zlight_drawXam.draw(canvas);

					zlight_drawableInActive.setBounds(rectImageSonca);
					zlight_drawableInActive.draw(canvas);

					textPaint.setStyle(Style.FILL);
					textPaint.setTextSize(titleS);
					textPaint.setColor(colorTextXam);
					float width = textPaint.measureText(textTitle);
					canvas.drawText(textTitle,
							(float) (0.5 * widthLayout - 0.5 * width), titleY,
							textPaint);

					return;
				}
			}

			if (MyApplication.intSvrModel == MyApplication.SONCA || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK) {

				if (stateView == VISIBLE) {
					if (isTouch) {
						zlight_drawHover.setBounds(rectVien);
						zlight_drawHover.draw(canvas);
					} else {
						zlight_drawActive.setBounds(rectVien);
						zlight_drawActive.draw(canvas);
					}

					if (selectedTone < 4) {
						drawImage = zlight_toneDrawables[selectedTone];
						textState = textGioiTinh[selectedTone];
					} else {
						drawImage = zlight_drawableInActive;
						textState = getResources().getString(
								R.string.main_left_5a);
					}
					drawImage.setBounds(rectImageSonca);
					drawImage.draw(canvas);

					textPaint.setStyle(Style.FILL);
					textPaint.setTextSize(textStateS);
					textPaint.setColor(colorTextActive);
					float width = textPaint.measureText(textState);
					canvas.drawText(textState,
							(float) (0.5 * widthLayout - 0.5 * width),
							textStateY, textPaint);

					textPaint.setStyle(Style.FILL);
					textPaint.setTextSize(titleS);
					textPaint.setColor(colorTextActive);
					width = textPaint.measureText(textTitle);
					canvas.drawText(textTitle,
							(float) (0.5 * widthLayout - 0.5 * width), titleY,
							textPaint);

				} else {
					zlight_drawXam.setBounds(rectVien);
					zlight_drawXam.draw(canvas);

					zlight_drawableInActive.setBounds(rectImageSonca);
					zlight_drawableInActive.draw(canvas);

					textPaint.setStyle(Style.FILL);
					textPaint.setTextSize(titleS);
					textPaint.setColor(colorTextXam);
					float width = textPaint.measureText(textTitle);
					canvas.drawText(textTitle,
							(float) (0.5 * widthLayout - 0.5 * width), titleY,
							textPaint);
				}

			} else if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
					|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
					 || MyApplication.intSvrModel == MyApplication.SONCA_KBX9 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
					 || MyApplication.intSvrModel == MyApplication.SONCA_TBT){
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
						if (aType == MEDIA_TYPE.SINGER) {
							isNotSinger = false;
						}
						if (aType == MEDIA_TYPE.VIDEO) {
							isNotSinger = false;
						}
					}
				}
				if (stateView == VISIBLE && isNotSinger) {
					if (isTouch) {
						zlight_drawHover.setBounds(rectVien);
						zlight_drawHover.draw(canvas);
					} else {
						zlight_drawActive.setBounds(rectVien);
						zlight_drawActive.draw(canvas);
					}

					if (selectedTone < 4) {
						drawImage = zlight_toneDrawables[selectedTone];
						textState = textGioiTinh[selectedTone];
					} else {
						drawImage = zlight_drawableInActive;
						textState = getResources().getString(
								R.string.main_left_5a);
					}
					drawImage.setBounds(rectImageSonca);
					drawImage.draw(canvas);

					textPaint.setStyle(Style.FILL);
					textPaint.setTextSize(textStateS);
					textPaint.setColor(colorTextActive);
					float width = textPaint.measureText(textState);
					canvas.drawText(textState,
							(float) (0.5 * widthLayout - 0.5 * width),
							textStateY, textPaint);

					textPaint.setStyle(Style.FILL);
					textPaint.setTextSize(titleS);
					textPaint.setColor(colorTextActive);
					width = textPaint.measureText(textTitle);
					canvas.drawText(textTitle,
							(float) (0.5 * widthLayout - 0.5 * width), titleY,
							textPaint);

				} else {
					zlight_drawXam.setBounds(rectVien);
					zlight_drawXam.draw(canvas);

					zlight_drawableInActive.setBounds(rectImageSonca);
					zlight_drawableInActive.draw(canvas);

					textPaint.setStyle(Style.FILL);
					textPaint.setTextSize(titleS);
					textPaint.setColor(colorTextXam);
					float width = textPaint.measureText(textTitle);
					canvas.drawText(textTitle,
							(float) (0.5 * widthLayout - 0.5 * width), titleY,
							textPaint);
				}

			} else {
				if (isTouch) {
					zlight_drawHover.setBounds(rectVien);
					zlight_drawHover.draw(canvas);
				} else {
					zlight_drawActive.setBounds(rectVien);
					zlight_drawActive.draw(canvas);
				}
				textPaint.setStyle(Style.FILL);
				textPaint.setTextSize(titleS);
				textPaint.setColor(colorTextActive);
				float width = textPaint.measureText(textTitle);
				canvas.drawText(textTitle,
						(float) (0.5 * widthLayout - 0.5 * width), titleY,
						textPaint);

				drawImage = zlight_toneDrawables[1];
				drawImage.setBounds(rectImageSonca);
				drawImage.draw(canvas);

			}

		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
	    int height = MeasureSpec.getSize(heightMeasureSpec);
	    if (width > height) {
		    setMeasuredDimension(height, height);
		} else {
			 setMeasuredDimension(width, width);
		}
	}
    
    
    @SuppressLint("ClickableViewAccessibility")
	@Override
    public boolean onTouchEvent(MotionEvent event){
		if (boolBlockComand == true) {
			return true;
		}
		if (MyApplication.intSvrModel == MyApplication.SONCA || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK) {
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
				if (Math.pow(event.getX() - getWidth() / 2, 2)
						+ Math.pow(event.getY() - getHeight() / 2, 2) > Math
							.pow(radXamNgoai, 2)) {
					if (isTouch) {
						isTouch = false;
						invalidate();
						return true;
					}
					return false;
				}
				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					isTouch = false;

					int tone = selectedTone;
					tone++;
					if (tone > 2) {
						tone = 0;
					}
					invalidate();
					longtimersync = System.currentTimeMillis();
					if (listener != null) {
						listener.onTone(tone);
					}
					return true;
				case MotionEvent.ACTION_DOWN:
					isTouch = true;
					invalidate();
					return true;
				}
			} else {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (listener != null) {
						listener.OnInActive();
					}
				}
			}
			return true;
		} else if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
				|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
				 || MyApplication.intSvrModel == MyApplication.SONCA_KBX9 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
				 || MyApplication.intSvrModel == MyApplication.SONCA_TBT){
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
				
				if (mediaType != 0x07 && mediaType != -1) {
					MEDIA_TYPE aType = MEDIA_TYPE.values()[mediaType];
					if (aType == MEDIA_TYPE.SINGER) {
						return true;
					}
					if (aType == MEDIA_TYPE.VIDEO) {
						return true;
					}
				}
			}
			if (stateView == View.VISIBLE) {
				if (Math.pow(event.getX() - getWidth() / 2, 2)
						+ Math.pow(event.getY() - getHeight() / 2, 2) > Math
							.pow(radXamNgoai, 2)) {
					if (isTouch) {
						isTouch = false;
						invalidate();
						return true;
					}
					return false;
				}
				switch (event.getAction()) {
				case MotionEvent.ACTION_UP:
					isTouch = false;
					int tone = selectedTone;
					tone++;
					if (tone > 2) {
						tone = 0;
					}
					invalidate();
					longtimersync = System.currentTimeMillis();
					if (listener != null) {
						listener.onTone(tone);
					}
					return true;
				case MotionEvent.ACTION_DOWN:
					isTouch = true;
					invalidate();
					return true;
				}
			} else {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (listener != null) {
						listener.OnInActive();
					}
				}
			}
			return true;
		} else {
			if (Math.pow(event.getX() - getWidth() / 2, 2)
					+ Math.pow(event.getY() - getHeight() / 2, 2) > Math.pow(
					radXamNgoai, 2)) {
				if (isTouch) {
					isTouch = false;
					invalidate();
					return true;
				}
				return false;
			}
			switch (event.getAction()) {
			case MotionEvent.ACTION_UP:
				isTouch = false;
				invalidate();
				if (listener != null) {
					listener.onTone(1);
				}
				return true;
			case MotionEvent.ACTION_DOWN:
				isTouch = true;
				invalidate();
				return true;
			}

			return false;
		}
    }

    public static final int INTCOMMAND = 32;
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
	
	public static final int INTMEDIUM = 8;
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