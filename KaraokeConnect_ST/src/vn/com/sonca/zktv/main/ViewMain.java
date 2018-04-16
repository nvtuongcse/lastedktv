package vn.com.sonca.zktv.main;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.zzzzz.MyApplication;

public class ViewMain extends View {
	
	private TextPaint textNumber = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public ViewMain(Context context) {
		super(context);
		initView(context);
	}

	public ViewMain(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public ViewMain(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Animation anime, animeLong;
	private void initView(Context context) {
		anime = AnimationUtils.loadAnimation(context, R.anim.animation_song);
		animeLong = AnimationUtils.loadAnimation(context, R.anim.animation_song_long);
		// drawImage = context.getResources().getDrawable(R.drawable.ktv_control_hethong);
		textPaint.setStyle(Style.FILL);
		textPaint.setColor(Color.WHITE);
		textPaint.setTextAlign(Align.CENTER);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	private Drawable drawable;
	private int textS, textX, textY;
	private Rect rectImage = new Rect();
	private int poX, poY, poS;
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		int tamX = (int) (0.5*w);
		int tamY = (int) (0.37*h);
		int hh = (int) (0.25*h);
		int ww = 120*hh/88;
		rectImage.set(tamX-ww, tamY-hh, tamX+ww, tamY+hh);
		
		textS = (int) (0.2*h);
		textX = (int) (0.5*w);
		textY = (int) (0.75*h + 0.5*textS);
		textPaint.setTextSize(textS);
		
		poS = (int) (0.2*h);
		poX = (int) (0.54*w);
		poY = (int) (0.15*h);
		
		cirY = (int) (0.2*h);
		cirX = (int) (0.42*h);
		radius = (int) (0.14*h);
		
		numberS = (int) (0.15*h);
		numberX = (int) (0.42*h);
		numberY = (int) (0.2*h + 0.38*numberS);
		textNumber.setTextSize(numberS);
	}
	
	private boolean isConnect = false;
	
	private int numberX, numberY, numberS;
	private int cirX, cirY, radius;
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		isConnect = processEnableView();		
		
		if(isConnect){
			if(stateView == View.VISIBLE){
				
				if(name.equals(getResources().getString(R.string.ktv_main_4))){ // TACH LOI
					
					if (drawImage != null) {
						drawImage.setAlpha(255);
					}
					
					drawable = getResources().getDrawable(R.drawable.ktv_control_vocal);
					drawable.setAlpha(255);
					drawable.setBounds(rectImage);
					drawable.draw(canvas);
					
					if(name != null){
						textPaint.setTextSize(textS);
						textPaint.setColor(Color.WHITE);
						canvas.drawText(name, textX, textY, textPaint);
					}	
					
//					if(isSingerOn){
//						drawable = getResources().getDrawable(R.drawable.ktv_control_vocal);
//						drawable.setAlpha(255);
//						drawable.setBounds(rectImage);
//						drawable.draw(canvas);
//						
//						if(name != null){
//							textPaint.setTextSize(textS);
//							textPaint.setColor(Color.WHITE);
//							canvas.drawText(name, textX, textY, textPaint);
//						}									
//						
//					} else {
//						drawable = getResources().getDrawable(R.drawable.ktv_control_vocal_off);
//						drawable.setAlpha(255);
//						drawable.setBounds(rectImage);
//						drawable.draw(canvas);
//						
//						String str = getResources().getString(R.string.ktv_main_4b);
//						textPaint.setTextSize(textS);
//						textPaint.setColor(Color.WHITE);
//						canvas.drawText(str, textX, textY, textPaint);		
//						
//					}
					
				} else if (name.equals(getResources().getString(R.string.ktv_caocap_11))) { // DANCE
					if (isDanceMode) {
						drawable = getResources().getDrawable(R.drawable.ktv_caocap_karaoke);
						drawable.setBounds(rectImage);
						drawable.draw(canvas);

						String str = getResources().getString(R.string.ktv_caocap_11b);
						textPaint.setTextSize(textS);
						textPaint.setColor(Color.WHITE);
						canvas.drawText(str, textX, textY, textPaint);

					} else {
						if (drawImage != null) {
							drawImage.setAlpha(255);
							drawImage.setBounds(rectImage);
							drawImage.draw(canvas);
						}

						if (name != null) {
							textPaint.setTextSize(textS);
							textPaint.setColor(Color.WHITE);
							canvas.drawText(name, textX, textY, textPaint);
						}

					}

				} else {
					if (drawImage != null) {
						drawImage.setAlpha(255);
						drawImage.setBounds(rectImage);
						drawImage.draw(canvas);
					}
					
					if(name != null){
						textPaint.setTextSize(textS);
						textPaint.setColor(Color.WHITE);
						canvas.drawText(name, textX, textY, textPaint);
					}
					
				}
								
				if(name.equals(getResources().getString(R.string.ktv_main_6))){ // Tong so bai trong playlist	
					
					if(countPlaylist > 0){
						paintMain.setStyle(Style.FILL);
						textNumber.setStyle(Style.FILL);
						textNumber.setTextAlign(Align.CENTER);
						textNumber.setColor(Color.WHITE);
						textNumber.setTypeface(Typeface.DEFAULT_BOLD);
						
						paintMain.setColor(getResources().getColor(R.color.ktv_color_image_7));
						canvas.drawCircle(cirX, cirY, radius, paintMain);
						
						canvas.drawText("" + countPlaylist, numberX, numberY, textNumber);
					}					
				}
				
			} else {
				if (drawImageIN != null) {
//					drawImage.setAlpha(100);
					drawImageIN.setBounds(rectImage);
					drawImageIN.draw(canvas);
				}
				
				if(name != null){
					textPaint.setTextSize(textS);
					textPaint.setColor(Color.GRAY);
					canvas.drawText(name, textX, textY, textPaint);
				}
			}
			
		} else {
			if (drawImageIN != null) {
//				drawImage.setAlpha(100);
				drawImageIN.setBounds(rectImage);
				drawImageIN.draw(canvas);
			}
			
			if(name != null){
				textPaint.setTextSize(textS);
				textPaint.setColor(Color.GRAY);
				canvas.drawText(name, textX, textY, textPaint);
			}
		}
		
		
	}
	
	private OnClickListener listener;
	@Override
	public void setOnClickListener(OnClickListener l) {
		// super.setOnClickListener(l);
		listener = l;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_UP:
			if (isConnect && stateView == View.VISIBLE) {
				this.startAnimation(anime);
			}
			if (listener != null) {
				listener.onClick(this);
			}
			break;
		default:
			break;
		}
		return true;
	}
	
///////////////////////////////////////
	
	private String name = "";
	private Drawable drawImage, drawImageIN;
	public void setDataView(String name, Drawable drawImage, Drawable drawImageIN){
		this.drawImage = drawImage;
		this.drawImageIN = drawImageIN;
		this.name = name;
		invalidate();
	}
	
///////////////////////////////////////
	
	private int stateView = View.VISIBLE;
	public void setEnableView(int value){
		this.stateView = value;
		invalidate();
	}
	
	public int getStateView(){
		return this.stateView;
	}
	
	public boolean processEnableView(){
		if(this.name.equals("")){
			return false;
		}
		
		if(name.equals(getResources().getString(R.string.ktv_main_9))){ // QUAY LAI
			return true;
		}
		
		boolean boolBlockComand = false;
		boolean flag = false;
		if(KTVMainActivity.serverStatus != null){
			flag = true;
		}
		
		if(name.equals(getResources().getString(R.string.ktv_main_2))){ // WIFI VIDEO
			if(MyApplication.intSvrModel == MyApplication.SONCA_KM1 || MyApplication.intSvrModel == MyApplication.SONCA_KARTROL){
				boolBlockComand = true;
			}
			
//			if(MyApplication.intSvrModel == MyApplication.SONCA_KM2 || MyApplication.intSvrModel == MyApplication.SONCA_KBX9
//					|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
//					|| MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI){
//				if(MyApplication.curHiW_firmwareInfo != null){
//					if(MyApplication.curHiW_firmwareInfo.getWifi_version() < 130){
//						boolBlockComand = true;
//					}
//					
//					if(MyApplication.curHiW_firmwareInfo.getWifi_version() == 130 && MyApplication.curHiW_firmwareInfo.getWifi_revision() < 7014){
//						boolBlockComand = true;
//					}
//					 
//				}
//			}
//			
//			if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_TBT){
//				if(MyApplication.curHiW_firmwareInfo != null){
//					if(MyApplication.curHiW_firmwareInfo.getDaumay_version() < 200){
//						boolBlockComand = true;
//					}
//					
//					if(MyApplication.curHiW_firmwareInfo.getWifi_version() < 130){
//						boolBlockComand = true;
//					}
//					
//					if(MyApplication.curHiW_firmwareInfo.getWifi_version() == 130 && MyApplication.curHiW_firmwareInfo.getWifi_revision() < 7014){
//						boolBlockComand = true;
//					}
//				}
//			}
			
			if (KTVMainActivity.serverStatus != null) {
				if(KTVMainActivity.serverStatus.getCurrentTempo() != 0){
					boolBlockComand = true;
				}	
			}		
			
			if(MyApplication.flagOnWifiVideo == false){
				boolBlockComand = true;
			}
			
//			if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && MyApplication.flagSmartK_801){
//				boolBlockComand = true;
//			}
			
			if(boolBlockComand){
				flag = false;
			} else {
				if (KTVMainActivity.serverStatus != null) {
					if(KTVMainActivity.serverStatus.danceMode() == 1){
						flag = false;
					}
					
				}
			}
			
		}
		
		if(name.equals(getResources().getString(R.string.ktv_main_3))){ // QUA BAI
			if (KTVMainActivity.serverStatus != null) {
				if (KTVMainActivity.serverStatus.isOnOffControlFullAPI()) {
					int clear = 0x00000003;
					int retur = (MyApplication.intCommandMedium & (clear << INTMEDIUM_NEXT)) >> INTMEDIUM_NEXT;
					if ((MyApplication.flagDeviceUser == true && retur != 0)
							|| (MyApplication.flagDeviceUser == false && retur == 2)) {
						boolBlockComand = true;
					} else {
						boolBlockComand = false;
					}
				} else {
					if ((MyApplication.intCommandEnable & INTCOMMAND_NEXT) != INTCOMMAND_NEXT) {
						boolBlockComand = true;
					} else {
						boolBlockComand = false;
					}
				}
			}
			
			if(boolBlockComand){
				flag = false;
			} else {
				if (MyApplication.intWifiRemote == MyApplication.SONCA) {
					if (KTVMainActivity.serverStatus != null) {
						flag = KTVMainActivity.serverStatus.getPlayingSongID() != 0;
					}
				}
				
				if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && MyApplication.flagPlayingYouTube){
					flag = true;
				}
				
			}
			
		}
		
		if(name.equals(getResources().getString(R.string.ktv_main_4))){ // TACH LOI
			if (KTVMainActivity.serverStatus != null) {
				if (KTVMainActivity.serverStatus.isOnOffControlFullAPI()) {
					int clear = 0x00000003;
					int retur = (MyApplication.intCommandMedium & (clear << INTMEDIUM_SINGER)) >> INTMEDIUM_SINGER;
					if ((MyApplication.flagDeviceUser == true && retur != 0)
							|| (MyApplication.flagDeviceUser == false && retur == 2)) {
						boolBlockComand = true;
					} else {
						boolBlockComand = false;
					}
				} else {
					if ((MyApplication.intCommandEnable & INTCOMMAND_SINGER) != INTCOMMAND_SINGER) {
						boolBlockComand = true;
					} else {
						boolBlockComand = false;
					}
				}
			}
			
			if(boolBlockComand){
				flag = false;
			}
			
		}
		
		if(name.equals(getResources().getString(R.string.ktv_caocap_11))){ // DANCE
			if (KTVMainActivity.serverStatus != null) {
				if (KTVMainActivity.serverStatus.isOnOffControlFullAPI()) {
					int clear = 0x00000003;
					int retur = (MyApplication.intCommandMedium & (clear << INTMEDIUM_DANCE)) >> INTMEDIUM_DANCE;
					if ((MyApplication.flagDeviceUser == true && retur != 0)
							|| (MyApplication.flagDeviceUser == false && retur == 2)) {
						boolBlockComand = true;
					} else {
						boolBlockComand = false;
					}
				} else {
					if ((MyApplication.intCommandEnable & INTCOMMAND_DANCE) != INTCOMMAND_DANCE) {
						boolBlockComand = true;
					} else {
						boolBlockComand = false;
					}
				}
			}
			
			if(boolBlockComand){
				flag = false;
			} else {
				if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && (MyApplication.flagSmartK_801 || MyApplication.flagSmartK_CB)){
					flag = false;
				}
				
			}
			
		}
		
		if(name.equals(getResources().getString(R.string.ktv_main_7))
				|| name.equals(getResources().getString(R.string.ktv_main_8))){ // GIAM AM + TANG AM
			if (KTVMainActivity.serverStatus != null) {
				if (KTVMainActivity.serverStatus.isOnOffControlFullAPI()) {
					int clear = 0x00000003;
					int retur = (MyApplication.intCommandMedium & (clear << INTMEDIUM_VOLUME)) >> INTMEDIUM_VOLUME;
					if((MyApplication.flagDeviceUser == true && retur != 0) || 
						(MyApplication.flagDeviceUser == false && retur == 2)){
						boolBlockComand = true;
					}else{
						boolBlockComand = false;
					}
				} else {
					if((MyApplication.intCommandEnable & INTCOMMAND_VOLUME) != INTCOMMAND_VOLUME){
						boolBlockComand = true;
					}else{
						boolBlockComand = false;
					}
				}
			}
			
			if(boolBlockComand){
				flag = false;
			}
		}
		
		if(name.equals(getResources().getString(R.string.ktv_main_6))){ // DA CHON			
			if(MyApplication.flagDance){
				flag = false;
			}
			
		}
		
		return flag;
	}
	
/////////////////////////////////////// NUT WIFI VIDEO
	public boolean getBlockStatus(){
		return processEnableView();
	}
	
	
/////////////////////////////////////// NUT QUA BAI
	public static final int INTCOMMAND_NEXT = 2048;
	public static final int INTMEDIUM_NEXT = 20;
	
/////////////////////////////////////// NUT TACH LOI
	private boolean isSingerOn = false;
	public void setSingerView(boolean isActive){
		if(MyApplication.intWifiRemote == MyApplication.SONCA){
			this.isSingerOn = isActive;
			invalidate();
		}
	}
	
	public boolean isSingerView(){
		return isSingerOn;
	}
	
	public static final int INTCOMMAND_SINGER = 256;
	public static final int INTMEDIUM_SINGER = 14;
	
/////////////////////////////////////// NUT DA CHON
	private int countPlaylist = 0;
	public void setSumSong(int count){
		if (isConnect && stateView == View.VISIBLE) {
			if(count > countPlaylist){
				this.startAnimation(animeLong);	
			}			
		}
		this.countPlaylist = count;
		invalidate();
	}
	
	
/////////////////////////////////////// NUT DANCE
	private boolean isDanceMode = false;

	public void setDanceMode(boolean flag) {
		this.isDanceMode = flag;
		invalidate();
	}

	public boolean getDanceMode() {
		return this.isDanceMode;
	}

	private static final int INTCOMMAND_DANCE = 4096;
	public static final int INTMEDIUM_DANCE = 22;
	
/////////////////////////////////////// NUT GIAM AM + TANG AM
	private int intVolume = 0;
	public void setVolume(int intVolume) {
		if(MyApplication.intWifiRemote == MyApplication.SONCA){
			if(stateView != View.INVISIBLE){
				this.intVolume = intVolume;
				invalidate();
			}
		}
	}
	
	public int getVolume() {
		return intVolume;
	}
	
	public static final int INTCOMMAND_VOLUME = 4;
	public static final int INTMEDIUM_VOLUME = 2;
	
	
	
}
