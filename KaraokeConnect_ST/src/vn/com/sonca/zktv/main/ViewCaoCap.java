package vn.com.sonca.zktv.main;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.utils.AppConfig.MEDIA_TYPE;
import vn.com.sonca.zzzzz.MyApplication;

public class ViewCaoCap extends View {
	
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	
	private Drawable drawable;
	
	public ViewCaoCap(Context context) {
		super(context);
		initView(context);
	}

	public ViewCaoCap(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public ViewCaoCap(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Animation anime;
	private void initView(Context context) {
		anime = AnimationUtils.loadAnimation(context, R.anim.animation_song);
		// drawImage = context.getResources().getDrawable(R.drawable.ktv_caocap_dance);
		textPaint.setStyle(Style.FILL);
		textPaint.setColor(Color.WHITE);
		textPaint.setTextAlign(Align.CENTER);
		
		toneDrawables = new Drawable[3];
		toneDrawables[0] = getResources().getDrawable(R.drawable.ktv_caocap_tone_nam);
		toneDrawables[1] = getResources().getDrawable(R.drawable.ktv_caocap_tone);
		toneDrawables[2] = getResources().getDrawable(R.drawable.ktv_caocap_tone_nu);

		textGioiTinh = new String[3];
		textGioiTinh[0] = getResources().getString(R.string.tone_nam);
		textGioiTinh[1] = getResources().getString(R.string.tone_man_nu);
		textGioiTinh[2] = getResources().getString(R.string.tone_nu);
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	private int textS, textX, textY;
	private Rect rectImage = new Rect();
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		int tamX = (int) (0.5*w);
		int tamY = (int) (0.37*h);
		int hh = (int) (0.24*h);
		int ww = 112*hh/64;
		rectImage.set(tamX-ww, tamY-hh, tamX+ww, tamY+hh);
		
		textS = (int) (0.2*h);
		textX = (int) (0.5*w);
		textY = (int) (0.75*h + 0.5*textS);
		textPaint.setTextSize(textS);
		
	}
	
	private boolean isConnect = false;
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		isConnect = processEnableView();
		
		if(isConnect){
			if(stateView == View.VISIBLE){
//				MyLog.e("ViewCaoCap", "state view VISIBLE");
				
				if (drawImage != null) {
					drawImage.setAlpha(255);	
				}	
				
				if(name.equals(getResources().getString(R.string.ktv_caocap_8))){ // TAM DUNG					
					
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
					
//					if(isPlaying){
//						if (drawImage != null) {
//							drawImage.setAlpha(255);
//							drawImage.setBounds(rectImage);
//							drawImage.draw(canvas);
//						}
//						
//						if(name != null){
//							textPaint.setTextSize(textS);
//							textPaint.setColor(Color.WHITE);
//							canvas.drawText(name, textX, textY, textPaint);
//						}
//					} else {
//						drawable = getResources().getDrawable(R.drawable.ktv_caocap_play);
//						drawable.setBounds(rectImage);
//						drawable.draw(canvas);
//						
//						String str = getResources().getString(R.string.ktv_caocap_8b);
//						textPaint.setTextSize(textS);
//						textPaint.setColor(Color.WHITE);
//						canvas.drawText(str, textX, textY, textPaint);
//						
//					}					
					
				} else if(name.equals(getResources().getString(R.string.ktv_caocap_12))){ // CHAM DIEM
					drawable = getResources().getDrawable(R.drawable.ktv_caocap_score_tat);
					if(scoreState == 1){			
						drawable.setAlpha(255);
						drawable = getResources().getDrawable(R.drawable.ktv_caocap_score);
					} else if(scoreState == 2){
						drawable = getResources().getDrawable(R.drawable.ktv_caocap_score_pro);
					}					
					drawable.setBounds(rectImage);
					drawable.draw(canvas);
					
					if(name != null){
						textPaint.setTextSize(textS);
						textPaint.setColor(Color.WHITE);
						canvas.drawText(name, textX, textY, textPaint);
					}
					
				} else if (name.equals(getResources().getString(R.string.ktv_caocap_3))) { // TONE
					drawable = toneDrawables[1];
					String str = textGioiTinh[1];
					if (selectedTone < 4) {
						drawable = toneDrawables[selectedTone];
						str = textGioiTinh[selectedTone];
					}				
					drawable.setBounds(rectImage);
					drawable.draw(canvas);
					
					textPaint.setTextSize(textS);
					textPaint.setColor(Color.WHITE);
					canvas.drawText(str, textX, textY, textPaint);
					
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
				
			} else {
//				MyLog.e("ViewCaoCap", "state view INVISIBLE");
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
//			MyLog.e("ViewCaoCap", "not connect");
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
		if (isConnect && stateView == View.VISIBLE) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				break;
			case MotionEvent.ACTION_UP:
				this.startAnimation(anime);

				if (listener != null) {
					listener.onClick(this);
				}
				break;
			default:
				break;
			}
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

	public void setEnableView(int value) {
		this.stateView = value;
		invalidate();
	}

	public int getStateView() {
		return this.stateView;
	}

	// TODO
	private boolean processEnableView(){
		if(this.name.equals("")){
			return false;
		}
		
		boolean boolBlockComand = false;
		boolean flag = false;
		if(KTVMainActivity.serverStatus != null){
			flag = true;
		}
		
		if(name.equals(getResources().getString(R.string.ktv_caocap_7))){ // NGAT TIENG
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
		
		if(name.equals(getResources().getString(R.string.ktv_caocap_8))){ // TAM DUNG
			if (KTVMainActivity.serverStatus != null) {
				if (KTVMainActivity.serverStatus.isOnOffControlFullAPI()) {
					int clear = 0x00000003;
					int retur = (MyApplication.intCommandMedium & (clear << INTMEDIUM_TAMDUNG)) >> INTMEDIUM_TAMDUNG;
					if ((MyApplication.flagDeviceUser == true && retur != 0)
							|| (MyApplication.flagDeviceUser == false && retur == 2)) {
						boolBlockComand = true;
					} else {
						boolBlockComand = false;
					}
				} else {
					if ((MyApplication.intCommandEnable & INTCOMMAND_TAMDUNG) != INTCOMMAND_TAMDUNG) {
						boolBlockComand = true;
					} else {
						boolBlockComand = false;
					}
				}
			}
			
			if(boolBlockComand){
				flag = false;
			} else {
				if (KTVMainActivity.serverStatus != null) {
					flag = KTVMainActivity.serverStatus.getPlayingSongID() != 0;
				}
				
				if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && MyApplication.flagPlayingYouTube){
					flag = true;
				}

				if (MyApplication.intWifiRemote == MyApplication.SONCA_KARTROL) {
					flag = true;
				}
				
			}
			
		}
		
		if (name.equals(getResources().getString(R.string.ktv_caocap_9))
				|| name.equals(getResources().getString(R.string.ktv_caocap_10))) { // GIAM TONE + TANG TONE
			if (KTVMainActivity.serverStatus != null) {
				if (KTVMainActivity.serverStatus.isOnOffControlFullAPI()) {
					int clear = 0x00000003;
					int retur = (MyApplication.intCommandMedium & (clear << INTMEDIUM_KEY)) >> INTMEDIUM_KEY;
					if ((MyApplication.flagDeviceUser == true && retur != 0)
							|| (MyApplication.flagDeviceUser == false && retur == 2)) {
						boolBlockComand = true;
					} else {
						boolBlockComand = false;
					}
				} else {
					if ((MyApplication.intCommandEnable & INTCOMMAND_KEY) != INTCOMMAND_KEY) {
						boolBlockComand = true;
					} else {
						boolBlockComand = false;
					}
				}
			}
			
			if(boolBlockComand){
				flag = false;
			} else {
				if(MyApplication.intWifiRemote != MyApplication.SONCA_KARTROL){
					if (KTVMainActivity.serverStatus != null) {		
						
						if (KTVMainActivity.serverStatus != null) {
							flag = KTVMainActivity.serverStatus.getPlayingSongID() != 0;
						}	
						
						if (KTVMainActivity.serverStatus.isPaused()) {
							flag = false;	
						}	
						
						if(MyApplication.flagDance){
							flag = false;
						}
						
					}	
				}				
				
			}
			
		}
		
		if(name.equals(getResources().getString(R.string.ktv_main_5))){ // HAT LAI
			if (KTVMainActivity.serverStatus != null) {
				if (KTVMainActivity.serverStatus.isOnOffControlFullAPI()) {
					int clear = 0x00000003;
					int retur = (MyApplication.intCommandMedium & (clear << INTMEDIUM_REPLAY)) >> INTMEDIUM_REPLAY;
					if((MyApplication.flagDeviceUser == true && retur != 0) || 
						(MyApplication.flagDeviceUser == false && retur == 2)){
						boolBlockComand = true;
					}else{
						boolBlockComand = false;
					}
				} else {
					if((MyApplication.intCommandEnable & INTCOMMAND_REPLAY) != INTCOMMAND_REPLAY){
						boolBlockComand = true;
					}else{
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
		
		if(name.equals(getResources().getString(R.string.ktv_caocap_12))){ // CHAM DIEM
			if (KTVMainActivity.serverStatus != null) {
				if (KTVMainActivity.serverStatus.isOnOffControlFullAPI()) {
					int clear = 0x00000003;
					int retur = (MyApplication.intCommandMedium & (clear << INTMEDIUM_SCORE)) >> INTMEDIUM_SCORE;
					if ((MyApplication.flagDeviceUser == true && retur != 0)
							|| (MyApplication.flagDeviceUser == false && retur == 2)) {
						boolBlockComand = true;
					} else {
						boolBlockComand = false;
					}
				} else {
					if ((MyApplication.intCommandEnable & INTCOMMAND_SCORE) != INTCOMMAND_SCORE) {
						boolBlockComand = true;
					} else {
						boolBlockComand = false;
					}
				}
			}
			
			if(boolBlockComand){
				flag = false;
			} else {
				if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && MyApplication.flagPlayingYouTube){
					flag = false;
				}
				
				if(MyApplication.flagDance){
					flag = false;
				}
				
			}
			
		}
		
		if (name.equals(getResources().getString(R.string.ktv_caocap_1))) { // MELODY
			if (KTVMainActivity.serverStatus != null) {
				if (KTVMainActivity.serverStatus.isOnOffControlFullAPI()) {
					int clear = 0x00000003;
					int retur = (MyApplication.intCommandMedium & (clear << INTMEDIUM_MELODY)) >> INTMEDIUM_MELODY;
					if((MyApplication.flagDeviceUser == true && retur != 0) || 
						(MyApplication.flagDeviceUser == false && retur == 2)){
						boolBlockComand = true;
					}else{
						boolBlockComand = false;
					}
				} else {
					if((MyApplication.intCommandEnable & INTCOMMAND_MELODY) != INTCOMMAND_MELODY){
						boolBlockComand = true;
					}else{
						boolBlockComand = false;
					}
				}
			}
			
			if(boolBlockComand){
				flag = false;
			} else {
				if(MyApplication.intWifiRemote != MyApplication.SONCA_KARTROL){
					if (KTVMainActivity.serverStatus != null) {		
						
						if (KTVMainActivity.serverStatus != null) {
							flag = KTVMainActivity.serverStatus.getPlayingSongID() != 0;
						}	
						
						if (KTVMainActivity.serverStatus.isPaused()) {
							flag = false;	
						}			
						
					}	
				}
				
				
			}
			
		}
		
		if (name.equals(getResources().getString(R.string.ktv_caocap_2))) { // TEMPO
			if (KTVMainActivity.serverStatus != null) {
				if (KTVMainActivity.serverStatus.isOnOffControlFullAPI()) {
					int clear = 0x00000003;
					int retur = (MyApplication.intCommandMedium & (clear << INTMEDIUM_TEMPO)) >> INTMEDIUM_TEMPO;
					if((MyApplication.flagDeviceUser == true && retur != 0) || 
						(MyApplication.flagDeviceUser == false && retur == 2)){
						boolBlockComand = true;
					}else{
						boolBlockComand = false;
					}
				} else {
					if((MyApplication.intCommandEnable & INTCOMMAND_TEMPO) != INTCOMMAND_TEMPO){
						boolBlockComand = true;
					}else{
						boolBlockComand = false;
					}
				}
			}
			
			if(boolBlockComand){
				flag = false;
			} else {
				if(MyApplication.intWifiRemote != MyApplication.SONCA_KARTROL){
					if (KTVMainActivity.serverStatus != null) {		
						
						if (KTVMainActivity.serverStatus != null) {
							flag = KTVMainActivity.serverStatus.getPlayingSongID() != 0;
						}	
						
						if (KTVMainActivity.serverStatus.isPaused()) {
							flag = false;	
						}			
						
					}	
				}
				
				
			}
			
		}
		
		if (name.equals(getResources().getString(R.string.ktv_caocap_3))) { // TONE
			if (KTVMainActivity.serverStatus != null) {
				if (KTVMainActivity.serverStatus.isOnOffControlFullAPI()) {
					int clear = 0x00000003;
					int retur = (MyApplication.intCommandMedium & (clear << INTMEDIUM_TONE)) >> INTMEDIUM_TONE;
					if ((MyApplication.flagDeviceUser == true && retur != 0)
							|| (MyApplication.flagDeviceUser == false && retur == 2)) {
						boolBlockComand = true;
					} else {
						boolBlockComand = false;
					}
				} else {
					if ((MyApplication.intCommandEnable & INTCOMMAND_TONE) != INTCOMMAND_TONE) {
						boolBlockComand = true;
					} else {
						boolBlockComand = false;
					}
				}
			}
			
			if(boolBlockComand){
				flag = false;
			} else {
				if(MyApplication.intWifiRemote != MyApplication.SONCA_KARTROL){
					if (KTVMainActivity.serverStatus != null) {		
						
						if (KTVMainActivity.serverStatus != null) {
							flag = KTVMainActivity.serverStatus.getPlayingSongID() != 0;
						}	
						
						if (KTVMainActivity.serverStatus.isPaused()) {
							flag = false;	
						}			
						
					}	
				}
				
				
			}
			
		}
		
		if(name.equals(getResources().getString(R.string.ktv_caocap_4))){ // HOA AM
			if (KTVMainActivity.serverStatus != null) {
				if (KTVMainActivity.serverStatus.isOnOffControlFullAPI()) {
					int clear = 0x00000003;
					int retur1 = (MyApplication.intCommandMedium & (clear << 20)) >> 20; // next
					int retur2 = (MyApplication.intCommandMedium & (clear << 0)) >> 0; // first
					if ((MyApplication.flagDeviceUser == true && retur1 != 0)
							|| (MyApplication.flagDeviceUser == true && retur2 != 0)
							|| (MyApplication.flagDeviceUser == false && retur1 == 2)
							|| (MyApplication.flagDeviceUser == false && retur2 == 2)) {
						boolBlockComand = true;
					} else {
						boolBlockComand = false;
					}
				} else {
					if (((MyApplication.intCommandEnable & 2048) != 2048) || ((MyApplication.intCommandEnable & 2) != 2)) {
						boolBlockComand = true;
					} else {
						boolBlockComand = false;
					}
				}
			}
			
			if(boolBlockComand){
				flag = false;
			} else {					
				if (KTVMainActivity.serverStatus != null) {
					flag = KTVMainActivity.serverStatus.getPlayingSongID() != 0;
				}	
				
				if(MyApplication.intSvrModel == MyApplication.SONCA_KARTROL || MyApplication.intSvrModel == MyApplication.SONCA_KM1){
					flag = false;
				}
				
				if (KTVMainActivity.serverStatus != null) {		

					if (KTVMainActivity.serverStatus.getReservedSongCount() >= 99) {
						flag = false;
					}
					
					if (KTVMainActivity.serverStatus.isPaused()) {
						flag = false;	
					}	

					boolean isDrawBlock = false;
					int type = KTVMainActivity.serverStatus.getMediaType();
					int ABC = KTVMainActivity.serverStatus.getPlayingSongTypeABC();
					if (type != 0x07) {
						if (type != -1) {
							MEDIA_TYPE aType = MEDIA_TYPE.values()[type];
							if (aType != MEDIA_TYPE.MIDI
									|| (aType == MEDIA_TYPE.MIDI && ABC == 0)) {
								isDrawBlock = true;
							}
						}
					} else {
						isDrawBlock = true;
					}

					if (isDrawBlock) {
						flag = false;
					}
				}
			}
			
		}
		
		if (name.equals(getResources().getString(R.string.ktv_caocap_5))) { // MAC DINH
			if (KTVMainActivity.serverStatus != null) {
				if (KTVMainActivity.serverStatus.isOnOffControlFullAPI()) {
					int clear = 0x00000003;
					int retur1 = (MyApplication.intCommandMedium & (clear << 4)) >> 4;
					int retur2 = (MyApplication.intCommandMedium & (clear << 6)) >> 6;
					int retur3 = (MyApplication.intCommandMedium & (clear << 8)) >> 8;
					int retur4 = (MyApplication.intCommandMedium & (clear << 10)) >> 10;
					if ((MyApplication.flagDeviceUser == true && retur1 != 0)
							|| (MyApplication.flagDeviceUser == true && retur2 != 0)
							|| (MyApplication.flagDeviceUser == true && retur3 != 0)
							|| (MyApplication.flagDeviceUser == true && retur4 != 0)
							|| (MyApplication.flagDeviceUser == false && retur1 == 2)
							|| (MyApplication.flagDeviceUser == false && retur2 == 2)
							|| (MyApplication.flagDeviceUser == false && retur3 == 2)
							|| (MyApplication.flagDeviceUser == false && retur4 == 2)) {
						boolBlockComand = true;
					} else {
						boolBlockComand = false;
					}
				} else {
					if (((MyApplication.intCommandEnable & 64) != 64) || ((MyApplication.intCommandEnable & 32) != 32)
							|| ((MyApplication.intCommandEnable & 16) != 16)
							|| ((MyApplication.intCommandEnable & 8) != 8)) {
						boolBlockComand = true;
					} else {
						boolBlockComand = false;
					}
				}
			}
			
			if(boolBlockComand){
				flag = false;
			} else {
				if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && MyApplication.flagPlayingYouTube){
					flag = false;
				}
				
				if (KTVMainActivity.serverStatus != null) {		
					
					if (KTVMainActivity.serverStatus != null) {
						flag = KTVMainActivity.serverStatus.getPlayingSongID() != 0;
					}	
					
					if (MyApplication.intWifiRemote != MyApplication.SONCA) {
						flag = false;
					}
					
					if (!MyApplication.flagSongPlayPause) {
						flag = false;
					}
					
					if (KTVMainActivity.serverStatus.isPaused()) {
						flag = false;	
					}	
					
					boolean isDrawBlock = false;
					int type = 0x07;
					if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
						type = KTVMainActivity.serverStatus.getMediaType();
					} else {
						type = TouchMainActivity.serverStatus.getMediaType();
					}

					if (type != 0x07) {
						if (type != -1) {
							MEDIA_TYPE aType = MEDIA_TYPE.values()[type];
							if (aType != MEDIA_TYPE.MIDI) {
								isDrawBlock = true;
							}
						}
					} else {
						isDrawBlock = true;
					}
					
					if(isDrawBlock){
						flag = false;
					}
					
				}
				
			}
			
		}
		
		return flag;
	}
	
/////////////////////////////////////// NUT NGAT TIENG
	private boolean isMute;

	public void setMute(boolean isMute) {
		this.isMute = isMute;
		invalidate();
	}

	public boolean getMute() {
		return isMute;
	}
	
	public static final int INTCOMMAND_VOLUME = 4;
	public static void setCommandEnable_Volume(boolean bool){
		if (bool) {
			MyApplication.intCommandEnable |= INTCOMMAND_VOLUME;
		} else {
			MyApplication.intCommandEnable &= (~INTCOMMAND_VOLUME);
		}
	}
	
	public static boolean getCommandEnable_Volume(){
		return (MyApplication.intCommandEnable & INTCOMMAND_VOLUME) == INTCOMMAND_VOLUME;
	}

	public static final int INTMEDIUM_VOLUME = 2;
	public static void setCommandMedium_Volume(int value){
		int clear = 0x00000003;
		MyApplication.intCommandMedium &= (~(clear << INTMEDIUM_VOLUME));
		MyApplication.intCommandMedium |= (value << INTMEDIUM_VOLUME);
	}
	
	public static int getCommandMedium_Volume(){
		int clear = 0x00000003;
		int retur = (MyApplication.intCommandMedium & (clear << INTMEDIUM_VOLUME)) >> INTMEDIUM_VOLUME;
		return retur;
	}
	
/////////////////////////////////////// NUT TAM DUNG
	
	private boolean isPlaying = true;
	public void setPlayPause(boolean isPlaying){
		this.isPlaying = isPlaying;
		invalidate();
	}
	
	public boolean isPlayPause(){
		return this.isPlaying;
	}
	
	public static final int INTCOMMAND_TAMDUNG = 8192;
	public static final int INTMEDIUM_TAMDUNG = 24;
	
/////////////////////////////////////// NUT GIAM TONE + TANG TONE ~~ KEY
	
	private int intKey = 0;
	public void setKey(int value){
		if(MyApplication.intWifiRemote == MyApplication.SONCA){
			if(stateView != View.INVISIBLE){
				intKey = value;
				if (intKey >= 6) {
					intKey = 6;
				}
				if(intKey <= -6){
					intKey = -6;
				}
			}
		}
	}
	
	public int getKey(){
		return intKey;
	}
	
	public static final int INTCOMMAND_KEY = 8;
	public static final int INTMEDIUM_KEY = 4;

	
/////////////////////////////////////// NUT CHAM DIEM
	
	private int scoreState = 1;
	
	public void setScoreView(int score){
		if(MyApplication.intWifiRemote == MyApplication.SONCA){
			this.scoreState = score;
			invalidate();
		}
	}
	
	public int getScoreView(){
		return this.scoreState;
	}
	
	public static final int INTCOMMAND_SCORE = 512;
	public static final int INTMEDIUM_SCORE = 16;
	
/////////////////////////////////////// NUT MELODY
	
	private int intMelody = 0;

	public void setMelody(int value) {
		if (MyApplication.intWifiRemote == MyApplication.SONCA) {
			if (stateView != View.INVISIBLE) {
				intMelody = value;
			}
		}
	}

	public int getMelody() {
		return intMelody;
	}

	public static final int INTCOMMAND_MELODY = 64;
	public static final int INTMEDIUM_MELODY = 10;
	
/////////////////////////////////////// NUT MELODY
	
	private int intTempo = 0;

	public void setTempo(int value) {
		if (MyApplication.intWifiRemote == MyApplication.SONCA) {
			if (stateView != View.INVISIBLE) {
				intTempo = value;
			}
		}
	}

	public int getTempo() {
		return intTempo;
	}

	public static final int INTCOMMAND_TEMPO = 16;
	public static final int INTMEDIUM_TEMPO = 6;

/////////////////////////////////////// NUT TONE
	private int selectedTone = 1;
	
	public void setToneView(int tone){
		if(MyApplication.intWifiRemote == MyApplication.SONCA){
			if(tone > 2 || tone < 0)
				tone = 0; 
			selectedTone = tone;
			invalidate();
		}
	}
	
	public int getToneView(){
		return selectedTone;
	}
	
	private Drawable[] toneDrawables;
	private Drawable selectedDrawable;
	private String textTone;
	private String[] textGioiTinh;

    public static final int INTCOMMAND_TONE = 32;
    public static final int INTMEDIUM_TONE = 8;
    
/////////////////////////////////////// NUT HAT LAI
public static final int INTCOMMAND_REPLAY = 1024;
public static final int INTMEDIUM_REPLAY = 18;
    
}
