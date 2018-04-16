package vn.com.sonca.Touch.CustomView;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.SetttingApp.ButonOnOff;
import vn.com.sonca.Touch.CustomView.TouchViewBack;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class TouchSearchView extends View {

	public static final int SONG = 1;
	public static final int PLAYLIST = 2;
	public static final int SINGER = 3;
	public static final int NOLAYOUT = 4;
	public static final int SONGTYPE = 5;
	public static final int REMIX = 6;
	public static final int YOUTUBE = 7;
	private int SaveLayout = SONG;
	
	public static final int MIDI = 1;
	public static final int VIDEO = 2;
	public static final int TATCA = 3;
	public static final int YOUTUBE_KARAOKE = 4;
	public static final int ONLINE = 5;
	
	public static final int VIETNAM = 4;
	public static final int OTHER = 5;
	public static final int ALL = 6;

	private int StateSearch1 = TATCA;
	
	public static final int LOI = 0;
	public static final int TEN = 1;
	public static final int CHU = 2;
	private int StateSearch2 = 1;
	
	float density = getResources().getDisplayMetrics().density;
	private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Drawable drawable;

	private Context context;
	private String textSearch = "";
	private String textHint = "";
	private String textSum = "";
	
    private int widthLayout = 400;
    private int heightLayout = 400;
    
    private boolean isActiveX = false;
    private boolean isActiveSearch = false;
    
    public void setActiveX(boolean isActiveX) {
		this.isActiveX = isActiveX;
	}    
    
	public void setActiveSearch(boolean isActiveSearch) {
		this.isActiveSearch = isActiveSearch;
		invalidate();
	}
	
	public boolean getActiveSearch(){
		return this.isActiveSearch;
	}
	
	public TouchSearchView(Context context) {
		super(context);
		initView(context);
	}

	public TouchSearchView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchSearchView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private Drawable drawUser;
	
	private Drawable zlightdrawable;
	private Drawable zlightdrawUser;
	private Drawable zlightActive;
	private Drawable zlightInActive;
	private Drawable zlightXam;
	private Drawable drawBackgroud;
	private Drawable drawKhung, drawKhungActive, drawkhungHover, drawable2;
	
	private void initView(Context context) {
		drawBackgroud = getResources().getDrawable(R.drawable.boder_search_032016);	
		drawable = getResources().getDrawable(R.drawable.touch_search_icon_72x72);
		drawUser = getResources().getDrawable(R.drawable.touch_icon_user_search);
		
		zlightdrawable = getResources().getDrawable(R.drawable.zlight_search_icon_72x72);
		zlightdrawUser = getResources().getDrawable(R.drawable.zlight_icon_user_search);
		zlightActive = getResources().getDrawable(R.drawable.zlight_boder_ktv_midi_all_active);
		zlightInActive = getResources().getDrawable(R.drawable.zlight_boder_ktv_midi_all_inactive); 
		zlightXam = getResources().getDrawable(R.drawable.zlight_boder_ktv_midi_all_xam); 
		
		drawKhung = getResources().getDrawable(R.drawable.total_khung_inactive);
		drawKhungActive = getResources().getDrawable(
				R.drawable.total_khung_active);
		drawkhungHover = getResources().getDrawable(
				R.drawable.total_khung_hover);
		
	}
	
	private OnSearchViewListener listener;
	public interface OnSearchViewListener {
		public void OnSearch(int state1 , int state2 , String search);
		public void OnShowKeyBoard();
		public void OnCloseKeyBoard();
		public void OnCallEditTextSearchLayout();
	}
	
	public void setOnSearchViewListener(OnSearchViewListener listener){
		this.listener = listener;
	}
	
//---------------------------------------------------//
	
	public int getState1(){
		return StateSearch2;
	}
	
	public int getState2(){
		return StateSearch1;
	}
	
	private String nameCharacter = "";
	public void setNameCharacter(String nameCharacter){
		this.nameCharacter = nameCharacter;
		invalidate();
	}
	
	public void setSongType(int number){
		this.nameCharacter = number + " " + 
				getResources().getString(R.string.frag_songtype);
		SaveLayout = SONGTYPE;
		invalidate();
	}
	
	public void setRequestLayout(){
		nameCharacter = "";
		requestLayout();
	}
	
	public void setSumFavourite(int sum , String type){	
		if (type.equals(TouchMainActivity.FAVOURITE)) {
			textSum = sum + " " + getResources().getString(R.string.search_4a);
			invalidate();
		}
	}
	
	public void setSumPlayList(int sum , String type){
		if (type.equals(TouchMainActivity.PLAYLIST)) {
			textSum = sum + " " + getResources().getString(R.string.search_4b);
			invalidate();
		}
	}
	
	public void setLayoutSearchView(int layout , String hint , int sum , String type , String search , int state){
		textHint = hint;
		positionT = 0;		
		positionX = 0; 	
		SaveLayout = layout;		
		if(layout == SINGER){
			if(state == -1){
				StateSearch1 = ALL;
			}else{
				StateSearch1 = state;
			}
		}
		if(layout == SONG){
			if(state == -1){
				StateSearch1 = TATCA;
			}else{
				StateSearch1 = state;
			}
		}
		if(layout == NOLAYOUT){
			if(state == -1){
				StateSearch1 = TATCA;
			}else{
				StateSearch1 = state;
			}
		}
		if(layout == YOUTUBE){
			if(state == -1){
				StateSearch1 = YOUTUBE_KARAOKE;
			}else{
				StateSearch1 = state;
			}
		}
		if (type.equals(TouchMainActivity.FAVOURITE)) {
			textSum = sum + " " + getResources().getString(R.string.search_4a);
		}
		if(type.equals(TouchMainActivity.PLAYLIST)){
			textSum = sum + " " + getResources().getString(R.string.search_4b);
		}
		if (search == null) {
			textSearch = "";
		} else {
			textSearch = search;
		}
		positionX = getPointerText(4000 , KTSearch , KS1 , textSearch);
		invalidate();
	}
	
	public String getTextSearch(){
		if (textSearch == null) {
			return "";
		} else {
			return textSearch;
		}
	}
	
	private int viewBackWidth = 0;
	public void setPointerText(float x , int size){
		this.viewBackWidth = size;
		int xRight = KD1R;
		int dLeft = KD2L;
		int dRight = KD2R;
		xRight += size;
		dLeft += size;
		dRight += size;
		if(x >= size){
			positionX = getPointerText(x - size, KTSearch, KS1, textSearch);
		}
		if (x > size && x < xRight) {
			if (isActiveX) { // x
				isActiveX = false;
				isActiveSearch = false;
				textSearch = "";
				positionT = 0;
				positionX = getPointerText(x - size, KTSearch, KS1, textSearch);
				if (listener != null) {
					listener.OnCloseKeyBoard();
					listener.OnSearch(StateSearch2, StateSearch1, textSearch);							
				}
			} 
		}
		if (x >= dLeft && x <= dRight && textSearch.trim().length() > 0) { // delete
			isActiveX = true;
			isActiveSearch = true;
			textSearch = "";
			positionT = 0;
			positionX = getPointerText(x - size, KTSearch, KS1, textSearch);
			
			if (listener != null) {
				listener.OnSearch(StateSearch2, StateSearch1, textSearch);
				listener.OnShowKeyBoard();
			}
		}
		invalidate();
	}
	
	public void clearAllCharacterSearchView(){
		isActiveSearch = true;
		textSearch = "";
		positionT = 0;		
		positionX = getPointerText(4000, KTSearch, KS1, textSearch);
		if(listener != null){
			listener.OnSearch(StateSearch2 , StateSearch1 , textSearch);
		}
		invalidate();
	}

	public void clearCharacterSearchView(){
		textSearch = clearCharText(KTSearch , textSearch, KS1);
		if(listener != null){
			listener.OnSearch(StateSearch2 , StateSearch1 , textSearch);
		}
		invalidate();
	}
	
	public void clearCharacterView(){
		// isActiveSearch = false;
		textSearch = "";
		positionT = 0;		
		positionX = getPointerText(4000, KTSearch, KS1, textSearch);
		invalidate();
	}
	
	public void clearFullCharacterView(boolean flagSingerTab){
		// isActiveSearch = false;
		textSearch = "";
		positionT = 0;		
		positionX = getPointerText(4000, KTSearch, KS1, textSearch);
		if(flagSingerTab){
			StateSearch1 = VIETNAM;
		} else {
			StateSearch1 = TATCA;
		}
		invalidate();
	}
	
	public void insertCharacterSearchView(char c){
		if (!isActiveX) {
			isActiveX = true;
		}
		textSearch = insertCharText(c, KTSearch, textSearch, KS1);
		invalidate();
		if(listener != null){
			listener.OnSearch(StateSearch2 , StateSearch1 , textSearch);
		}
	}
	
	public void setFullSearch(String search){
		if (!isActiveX) {
			isActiveX = true;
		}
		textSearch = search;
		isActiveSearch = true;
		if (listener != null) {
			listener.OnSearch(StateSearch2, StateSearch1, textSearch);
		}
		invalidate();
	}
	
//---------------------------------------------------//
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
	}
	
	private float nameY = 0;
	private float nameX = 0;
	private float nameS = 0;
	private Rect rectUser = new Rect();
	private Rect rectBackgroud = new Rect();
    @Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		setK(w, h);
		widthLayout = w;
		heightLayout = h;
		positionX = getPointerText(4000 , KTSearch , KS1 , textSearch);
		
		nameX = 10;
		nameS = (float) (0.4*h);
		nameY = (float) (0.6*h);
		
		int hr = (int) (0.13*h);
		int wr = hr;
		int tamX = (int) (1.7*hr);
		int tamY = (int) (0.9*h - 1.5*hr);
		rectUser.set(tamX - wr, tamY - hr, tamX + wr, tamY + hr);
		rectBackgroud.set(0, 0, w, h);
	}
	
    private int color_001;
    private int color_002;
    private int color_003;
    private int color_004;
    private int color_004_b;
    private int color_005;
    private int color_006;
    private int color_007;
    private int color_008;
    private int color_009;
    private int color_010;
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);		
		/*
		SaveLayout = SINGER;
		StateSearch1 = VIETNAM;
		MyApplication.flagEverConnect = false;
		MyApplication.intColorScreen = MyApplication.SCREEN_LIGHT;
		*/
		
		// BACKVIEW
		// MyApplication.intColorScreen = MyApplication.SCREEN_LIGHT;
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			color_001 = Color.argb(255 , 1 , 55 , 102);
			color_002 = Color.argb(255, 1, 91, 141);
			color_003 = Color.argb(255 , 79 , 115 , 149);
			color_004_b = Color.parseColor("#ffe002");;
			color_004 = Color.argb(255 , 180 , 253 , 253);
			color_005 = Color.parseColor("#B4FEFF");
			color_006 = Color.argb(0 , 0 , 52 , 99);
			color_007 = Color.argb(255 , 1 , 71 , 131);
			color_008 = Color.parseColor("#B4FEFF");
			color_009 = Color.argb(255 , 89 , 154 , 176);
			color_010 = Color.GREEN;
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			color_001 = Color.parseColor("#21BAA9");
			color_002 = Color.parseColor("#F1FCFA");
			color_003 = Color.argb(128 , 255 , 255 , 255);
			color_004 = Color.parseColor("#7B1FA2");
			color_005 = Color.parseColor("#64949F");
			color_006 = Color.parseColor("#21BAA9");
			color_007 = Color.parseColor("#21BAA9");
			color_008 = Color.parseColor("#E6E7E8");
			color_009 = Color.parseColor("#21BAA9");
			color_010 = Color.GREEN;
		}
		
		drawable = getResources().getDrawable(R.drawable.touch_search_icon_72x72);
		drawable.setAlpha(255);
		
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			
				setK(getWidth() , getHeight());
				widthLayout = getWidth();
				heightLayout = getHeight();
				
				Path path = new Path();
				/*
				mainPaint.setStyle(Style.FILL);
				mainPaint.setColor(color_001);
				canvas.drawRect(0 , 0 , getWidth() , getHeight() , mainPaint);
				*/
				
				/*
				drawBackgroud.setBounds(rectBackgroud);
				drawBackgroud.draw(canvas);
				*/
				
				isActiveX = !textSearch.equals("");
				
				KR1R = 1330*widthLayout/1920; 
				
				KR3L = 1540*widthLayout/1920; 
		    	KR3R = 1700*widthLayout/1920; 
		    	KR4R = 1910*widthLayout/1920; 
		    	KR4L = 1710*widthLayout/1920; 
		    	
		    	KR5L = 0; 
		    	KR5R = 0; 
		    	KR5T = 0;
		    	KR5B = 0;
		    	
		    	MyApplication.flagSearchOnline = false;
		    	MyApplication.flagYoutubeKaraokeOnly = false;
		    	
				switch (SaveLayout) {
				case YOUTUBE : {
					
					int valueYouTube = MyApplication.getCommandMediumYouTube();
					if(valueYouTube == ButonOnOff.KHOADIENTHOAI){
						MyApplication.flagYoutubeKaraokeOnly = true;
					}
					
					KR1R = 1390*widthLayout/1920; 
					
					KR3L = 1410*widthLayout/1920; 
			    	KR3R = 1650*widthLayout/1920;
			    	KR4L = 1660*widthLayout/1920; 
			    	KR4R = 1900*widthLayout/1920; 
			    	
			    	if(MyApplication.flagYoutubeKaraokeOnly){
			    		KR1R = 1890*widthLayout/1920;
			    		
			    		KR3L = 0;
				    	KR3R = 0;
				    	KR4L = 0;
				    	KR4R = 0;
			    	}
					
			    	drawable = getResources().getDrawable(R.drawable.touch_search_icon_72x72);
					if (isActiveX) {
						drawable = getResources().getDrawable(R.drawable.touch_ico_clear);
					}
					drawable.setBounds(KD1L , KD1T , KD1R , KD1B);
					drawable.draw(canvas);
				
						//--------------------//
					
					mainPaint.setStyle(Style.FILL);
					mainPaint.setColor(color_002);
					canvas.drawRect(KR1L , KR1T , KR1R , KR1B , mainPaint);
					mainPaint.setStyle(Style.STROKE);
					if (!isActiveSearch) {
						mainPaint.setColor(color_003);
					} else {
						mainPaint.setColor(color_010);
					}
					mainPaint.setStrokeWidth((float) 1.0);
					canvas.drawRect((float) (KR1L) , (float) (KR1T) , (float) (KR1R) ,(float) (KR1B) , mainPaint);
						
					mainPaint.setStyle(Style.FILL);
					mainPaint.setColor(color_004);
					mainPaint.setTextSize(KS1);
					mainPaint.setTypeface(Typeface.DEFAULT);
					if (textSearch.equals("")) {
						mainPaint.setTextSize(KS1_hint);
						mainPaint.setColor(color_005);
						canvas.drawText(textHint , KTSearch , KTY1 , mainPaint);
					} else {						
						mainPaint.setColor(color_010);
						if(!MyApplication.flagRealKeyboard){
							canvas.drawText(textSearch , KTSearch , KTY1 , mainPaint);	
						}						
					}
					if(isActiveSearch){
						mainPaint.setColor(color_010);
						if(!MyApplication.flagRealKeyboard){
							canvas.drawLine(positionX , KLY1 , positionX , KLY2 , mainPaint);	
						}							
					}	
					
					if(MyApplication.flagYoutubeKaraokeOnly == false){
							//--------------------//
						drawable2 = drawKhung;
						if(StateSearch1 == YOUTUBE_KARAOKE){
							mainPaint.setColor(color_004_b);
							drawable2 = drawKhungActive;
						} else {
							mainPaint.setColor(color_008);
						}
						drawable2.setBounds((int)KR3L, (int)KR3T, (int)KR3R, (int)KR3B);
						drawable2.draw(canvas);
						
						mainPaint.setTextSize(KS2);
						mainPaint.setStyle(Style.FILL);
						float lengthText = mainPaint.measureText("KARAOKE");
						canvas.drawText("KARAOKE", (KR3L + KR3R)/2 - lengthText/2 , (float) ((KR3T + KR3B)/2 + KS2/2.5) , mainPaint);
							//--------------------//	
						drawable2 = drawKhung;
						if(StateSearch1 == TATCA){							
							mainPaint.setColor(color_004_b);
							drawable2 = drawKhungActive;
						} else {
							mainPaint.setColor(color_008);
						}
						drawable2.setBounds((int)KR4L, (int)KR4T, (int)KR4R, (int)KR4B);
						drawable2.draw(canvas);
						
						mainPaint.setTextSize(KS2);
						mainPaint.setStyle(Style.FILL);
						lengthText = mainPaint.measureText("TAT CA");
						canvas.drawText(getResources().getString(R.string.search_3), 
								(KR4L + KR4R)/2 - lengthText/2 , (float) ((KR3T + KR3B)/2 + KS2/2.5) , mainPaint);	
					}
							
				}break;
				case SONG : {
					if(TouchMainActivity.SAVETYPE == TouchMainActivity.SONG 
							&& MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && (!MyApplication.flagSmartK_801 && !MyApplication.flagSmartK_CB)
							&& MyApplication.flagAllowSearchOnline
							&& TouchViewBack.state != View.VISIBLE){
						MyApplication.flagSearchOnline = true;
						
						KR1R = 1290*widthLayout/1920;
						
				    	KR5L = 1300*widthLayout/1920; 
				    	KR5R = 1470*widthLayout/1920; 
				    	KR5T = KR2T;
				    	KR5B = KR2B;
				    	
				    	KR2L = 1480*widthLayout/1920; 
				    	KR2R = 1600*widthLayout/1920; 
				    	
				    	KR3L = 1610*widthLayout/1920; 
				    	KR3R = 1730*widthLayout/1920; 
				    	
				    	KR4L = 1740*widthLayout/1920; 
				    	KR4R = 1910*widthLayout/1920; 
				    	
						drawable = getResources().getDrawable(R.drawable.touch_search_icon_72x72);
						if (isActiveX) {
							drawable = getResources().getDrawable(R.drawable.touch_ico_clear);
						}
						drawable.setBounds(KD1L , KD1T , KD1R , KD1B);
						drawable.draw(canvas);
						
						//--------------------//
						
						mainPaint.setStyle(Style.FILL);
						mainPaint.setColor(color_002);
						canvas.drawRect(KR1L , KR1T , KR1R , KR1B , mainPaint);
						mainPaint.setStyle(Style.STROKE);
						if (!isActiveSearch) {
							mainPaint.setColor(color_003);
						} else {
							mainPaint.setColor(color_010);
						}
						mainPaint.setStrokeWidth((float) 1.0);
						canvas.drawRect((float) (KR1L) , (float) (KR1T) , (float) (KR1R) ,(float) (KR1B) , mainPaint);
						
						mainPaint.setStyle(Style.FILL);
						mainPaint.setColor(color_004);
						mainPaint.setTextSize(KS1);
						mainPaint.setTypeface(Typeface.DEFAULT);
						if (textSearch.equals("")) {
							mainPaint.setTextSize(KS1_hint);
							mainPaint.setColor(color_005);
							canvas.drawText(textHint , KTSearch , KTY1 , mainPaint);
						} else {						
							mainPaint.setColor(color_010);
							if(!MyApplication.flagRealKeyboard){
								canvas.drawText(textSearch , KTSearch , KTY1 , mainPaint);	
							}							
						}
						if(isActiveSearch){
							mainPaint.setColor(color_010);
							if(!MyApplication.flagRealKeyboard){
								canvas.drawLine(positionX , KLY1 , positionX , KLY2 , mainPaint);	
							}								
						}	
						
						//--------------------//						
						drawable2 = drawKhung;
						if(StateSearch1 == ONLINE){	
							mainPaint.setColor(color_004_b);
							drawable2 = drawKhungActive;
						} else {
							mainPaint.setColor(color_008);
						}
						drawable2.setBounds((int)KR5L, (int)KR5T, (int)KR5R, (int)KR5B);
						drawable2.draw(canvas);
						
						mainPaint.setTextSize(KS2);
						mainPaint.setStyle(Style.FILL);
						float lengthText = mainPaint.measureText("ONLINE");
						canvas.drawText("ONLINE", (KR5L + KR5R)/2 - lengthText/2 , (float) ((KR5T + KR5B)/2 + KS2/2.5) , mainPaint);
						
						//--------------------//						
						drawable2 = drawKhung;
						if(StateSearch1 == VIDEO){	
							mainPaint.setColor(color_004_b);
							drawable2 = drawKhungActive;
						} else {
							mainPaint.setColor(color_008);
						}
						drawable2.setBounds((int)KR2L, (int)KR2T, (int)KR2R, (int)KR2B);
						drawable2.draw(canvas);
						
						mainPaint.setTextSize(KS2);
						mainPaint.setStyle(Style.FILL);
						lengthText = mainPaint.measureText("KTV");
						canvas.drawText("KTV", (KR2L + KR2R)/2 - lengthText/2 , (float) ((KR2T + KR2B)/2 + KS2/2.5) , mainPaint);
							//--------------------//
						drawable2 = drawKhung;
						if(StateSearch1 == MIDI){
							mainPaint.setColor(color_004_b);
							drawable2 = drawKhungActive;
						} else {
							mainPaint.setColor(color_008);
						}
						drawable2.setBounds((int)KR3L, (int)KR3T, (int)KR3R, (int)KR3B);
						drawable2.draw(canvas);
						
						mainPaint.setTextSize(KS2);
						mainPaint.setStyle(Style.FILL);
						lengthText = mainPaint.measureText("MIDI");
						canvas.drawText("MIDI", (KR3L + KR3R)/2 - lengthText/2 , (float) ((KR3T + KR3B)/2 + KS2/2.5) , mainPaint);
							//--------------------//	
						drawable2 = drawKhung;
						if(StateSearch1 == TATCA){							
							mainPaint.setColor(color_004_b);
							drawable2 = drawKhungActive;
						} else {
							mainPaint.setColor(color_008);
						}
						drawable2.setBounds((int)KR4L, (int)KR4T, (int)KR4R, (int)KR4B);
						drawable2.draw(canvas);
						
						mainPaint.setTextSize(KS2);
						mainPaint.setStyle(Style.FILL);
						lengthText = mainPaint.measureText("TAT CA");
						canvas.drawText(getResources().getString(R.string.search_3), 
								(KR4L + KR4R)/2 - lengthText/2 , (float) ((KR3T + KR3B)/2 + KS2/2.5) , mainPaint);	
						
					} else {
						drawable = getResources().getDrawable(R.drawable.touch_search_icon_72x72);
						if (isActiveX) {
							drawable = getResources().getDrawable(R.drawable.touch_ico_clear);
						}
						drawable.setBounds(KD1L , KD1T , KD1R , KD1B);
						drawable.draw(canvas);
					
							//--------------------//
						
						mainPaint.setStyle(Style.FILL);
						mainPaint.setColor(color_002);
						canvas.drawRect(KR1L , KR1T , KR1R , KR1B , mainPaint);
						mainPaint.setStyle(Style.STROKE);
						if (!isActiveSearch) {
							mainPaint.setColor(color_003);
						} else {
							mainPaint.setColor(color_010);
						}
						mainPaint.setStrokeWidth((float) 1.0);
						canvas.drawRect((float) (KR1L) , (float) (KR1T) , (float) (KR1R) ,(float) (KR1B) , mainPaint);
		/*
						drawable = getResources().getDrawable(R.drawable.del_icon_72x72);
						drawable.setBounds(KD2L , KD2T , KD2R , KD2B);
						drawable.draw(canvas);
		*/				
		/* CHON TEN, LOI, CHU ...				
						canvas.drawLine(KLX1, KLY1, KLX2, KLY2, mainPaint);
						mainPaint.setStyle(Style.FILL);
						mainPaint.setColor(color_004);
						mainPaint.setTextSize(KS1);
							//--------------------//
						
						String text = "";
						switch (StateSearch2) {
							case LOI:
								text = getResources().getString(R.string.search_2a);
								break;
							case TEN:
								text = getResources().getString(R.string.search_2b);
								break;
							case CHU:
								text = getResources().getString(R.string.search_2c);
								break;
							default:	break;
						}
						float length = mainPaint.measureText(text);
						canvas.drawText(text, (KLX1 + KR1R)/2 - length/2 , KTY1 , mainPaint);
		*/				
						
						mainPaint.setStyle(Style.FILL);
						mainPaint.setColor(color_004);
						mainPaint.setTextSize(KS1);
						mainPaint.setTypeface(Typeface.DEFAULT);
						if (textSearch.equals("")) {
							mainPaint.setColor(color_005);
							mainPaint.setTextSize(KS1_hint);
							canvas.drawText(textHint , KTSearch , KTY1 , mainPaint);
						} else {						
							mainPaint.setColor(color_010);
							if(!MyApplication.flagRealKeyboard){
								canvas.drawText(textSearch , KTSearch , KTY1 , mainPaint);	
							}							
						}
						if(isActiveSearch){
							mainPaint.setColor(color_010);
							if(!MyApplication.flagRealKeyboard){
								canvas.drawLine(positionX , KLY1 , positionX , KLY2 , mainPaint);	
							}								
						}	
						
							//--------------------//
						drawable2 = drawKhung;
						if(StateSearch1 == VIDEO){	
							mainPaint.setColor(color_004_b);
							drawable2 = drawKhungActive;
						} else {
							mainPaint.setColor(color_008);
						}
						drawable2.setBounds((int)KR2L, (int)KR2T, (int)KR2R, (int)KR2B);
						drawable2.draw(canvas);
						
						mainPaint.setTextSize(KS2);
						mainPaint.setStyle(Style.FILL);
						float lengthText = mainPaint.measureText("KTV");
						canvas.drawText("KTV", (KR2L + KR2R)/2 - lengthText/2 , (float) ((KR2T + KR2B)/2 + KS2/2.5) , mainPaint);
							//--------------------//
						drawable2 = drawKhung;
						if(StateSearch1 == MIDI){
							mainPaint.setColor(color_004_b);
							drawable2 = drawKhungActive;
						} else {
							mainPaint.setColor(color_008);
						}
						drawable2.setBounds((int)KR3L, (int)KR3T, (int)KR3R, (int)KR3B);
						drawable2.draw(canvas);
						
						mainPaint.setTextSize(KS2);
						mainPaint.setStyle(Style.FILL);
						lengthText = mainPaint.measureText("MIDI");
						canvas.drawText("MIDI", (KR3L + KR3R)/2 - lengthText/2 , (float) ((KR3T + KR3B)/2 + KS2/2.5) , mainPaint);
							//--------------------//	
						drawable2 = drawKhung;
						if(StateSearch1 == TATCA){							
							mainPaint.setColor(color_004_b);
							drawable2 = drawKhungActive;
						} else {
							mainPaint.setColor(color_008);
						}
						drawable2.setBounds((int)KR4L, (int)KR4T, (int)KR4R, (int)KR4B);
						drawable2.draw(canvas);
						
						mainPaint.setTextSize(KS2);
						mainPaint.setStyle(Style.FILL);
						lengthText = mainPaint.measureText("TAT CA");
						canvas.drawText(getResources().getString(R.string.search_3), 
								(KR4L + KR4R)/2 - lengthText/2 , (float) ((KR3T + KR3B)/2 + KS2/2.5) , mainPaint);	
					}
					
				}break;
				case PLAYLIST : {
					mainPaint.setStyle(Style.FILL);
					mainPaint.setARGB(255, 180, 254, 255);
					mainPaint.setTextSize(nameS);
					canvas.drawText(textSum , nameX + 10 , nameY , mainPaint);				
				}break;		
				case SINGER : {
					drawable = getResources().getDrawable(R.drawable.touch_search_icon_72x72);
					if (isActiveX) {
						drawable = getResources().getDrawable(R.drawable.touch_ico_clear);
					}
					drawable.setBounds(KD1L , KD1T , KD1R , KD1B);
					drawable.draw(canvas);
		/*	DELETE ICON			
					drawable = getResources().getDrawable(R.drawable.del_icon_72x72);
					drawable.setBounds(KD3L , KD3T , KD3R , KD3B);
					drawable.draw(canvas);
		*/			
						//------------------//
					mainPaint.setStyle(Style.FILL);
					mainPaint.setColor(color_002);
					canvas.drawRect(KR1L , KR1T , KR1R , KR1B , mainPaint);
					mainPaint.setStyle(Style.STROKE);
					if (!isActiveSearch) {
						mainPaint.setColor(color_003);
					} else {
						mainPaint.setColor(color_010);
					}
					mainPaint.setStrokeWidth((float) 1.0);
					canvas.drawRect((float) (KR1L) , (float) (KR1T) , (float) (KR1R) ,(float) (KR1B) , mainPaint);
		/*
					mainPaint.setStyle(Style.FILL);
					mainPaint.setColor(color_002);
					canvas.drawRect(KR1L , KR1T , (float) (widthLayout - KR1L) , KR1B , mainPaint);
					mainPaint.setStyle(Style.STROKE);
					mainPaint.setColor(color_003);
					mainPaint.setStrokeWidth((float) 1.0);
					canvas.drawRect((float) (KR1L) , (float) (KR1T) , (float) (widthLayout - KR1L) ,
							(float) (KR1B - 1.0) , mainPaint);
					mainPaint.setStyle(Style.FILL);
					mainPaint.setTextSize(KS1);
					if (textSearch.equals("")) {
						mainPaint.setColor(color_005);
						canvas.drawText(textHint , KTSearch , KTY1 , mainPaint);
					} else {
						mainPaint.setColor(Color.RED);
						canvas.drawLine(positionX , KLY1 , positionX , KLY2 , mainPaint);
						mainPaint.setColor(color_004);
						canvas.drawText(textSearch , KTSearch , KTY1 , mainPaint);
					}
		*/			
					
					mainPaint.setStyle(Style.FILL);
					mainPaint.setColor(color_004);
					mainPaint.setTypeface(Typeface.DEFAULT);
					mainPaint.setTextSize(KS1);
					if (textSearch.equals("")) {
						mainPaint.setColor(color_005);
						mainPaint.setTextSize(KS1_hint);
						canvas.drawText(textHint , KTSearch , KTY1 , mainPaint);
					} else {					
						mainPaint.setColor(color_010);
						if(!MyApplication.flagRealKeyboard){
							canvas.drawText(textSearch , KTSearch , KTY1 , mainPaint);	
						}						
					}
					if(isActiveSearch){
						mainPaint.setColor(color_010);
						if(!MyApplication.flagRealKeyboard){
							canvas.drawLine(positionX , KLY1 , positionX , KLY2 , mainPaint);	
						}							
					}	
						//--------------------//
					drawable2 = drawKhung;
					if(StateSearch1 == VIETNAM){
						mainPaint.setColor(color_004_b);
						drawable2 = drawKhungActive;
					} else {
						mainPaint.setColor(color_008);
					}
					drawable2.setBounds((int)KR2LV, (int)KR2T, (int)KR2R, (int)KR2B);
					drawable2.draw(canvas);
					
					mainPaint.setTextSize(KS2);
					mainPaint.setStyle(Style.FILL);
					String string = getResources().getString(R.string.vietnam);
					float lengthText = mainPaint.measureText(string);
					canvas.drawText(string , (KR2LV + KR2R)/2 - lengthText/2 , (float) ((KR2T + KR2B)/2 + KS2/2.5) , mainPaint);
						//--------------------//
					if(MyApplication.flagEverConnect == false){
						mainPaint.setStyle(Style.FILL);
						mainPaint.setColor(color_006);
						canvas.drawRect(KR3L , KR3T , KR3R , KR3B , mainPaint);
						mainPaint.setStyle(Style.STROKE);
						mainPaint.setColor(color_007);
						mainPaint.setStrokeWidth((float) 1.0);
						if(MyApplication.flagEverConnect == false){
							mainPaint.setARGB(125, 180, 192, 194);
						}
						canvas.drawRect(KR3L , KR3T , KR3R , KR3B  , mainPaint);
						if(StateSearch1 == OTHER){
							path = new Path();
							path.moveTo(KR3L , KR3T + Line);
							path.lineTo(KR3L, KR3T);
							path.lineTo(KR3L + Line , KR3T);
							path.moveTo(KR3R , KR3T + Line);
							path.lineTo(KR3R , KR3T);
							path.lineTo(KR3R - Line , KR3T);
							path.moveTo(KR3R , KR3B - Line);
							path.lineTo(KR3R , KR3B);
							path.lineTo(KR3R - Line , KR3B);
							path.moveTo(KR3L + Line , KR3B);
							path.lineTo(KR3L , KR3B);
							path.lineTo(KR3L , KR3B - Line);		
							mainPaint.setColor(color_008);
							if(MyApplication.flagEverConnect == false){
								mainPaint.setARGB(125, 180, 192, 194);
							}
							canvas.drawPath(path, mainPaint);
							mainPaint.setColor(color_004_b);
						} else {
							mainPaint.setColor(color_008);
						}
						
						mainPaint.setARGB(125, 180, 192, 194);
					} else {
						drawable2 = drawKhung;
						if(StateSearch1 == OTHER){
							mainPaint.setColor(color_004_b);
							drawable2 = drawKhungActive;
						} else {
							mainPaint.setColor(color_008);
						}
						drawable2.setBounds((int)KR3L, (int)KR3T, (int)KR3R, (int)KR3B);
						drawable2.draw(canvas);
						
					}
					mainPaint.setTextSize(KS2);
					mainPaint.setStyle(Style.FILL);
					string = getResources().getString(R.string.khac);
					lengthText = mainPaint.measureText(string);
					canvas.drawText(string, (KR3L + KR3R)/2 - lengthText/2 , (float) ((KR3T + KR3B)/2 + KS2/2.5) , mainPaint);
						//--------------------//	
					if(MyApplication.flagEverConnect == false){
						mainPaint.setStyle(Style.FILL);
						mainPaint.setColor(color_006);
						canvas.drawRect(KR4L , KR4T , KR4R , KR4B , mainPaint);
						mainPaint.setStyle(Style.STROKE);
						mainPaint.setColor(color_007);
						mainPaint.setStrokeWidth((float) 1.0);
						if(MyApplication.flagEverConnect == false){
							mainPaint.setARGB(125, 180, 192, 194);
						}
						canvas.drawRect(KR4L , KR4T , KR4R , KR4B  , mainPaint);
						if(StateSearch1 == ALL){
							path = new Path();
							path.moveTo(KR4L , KR4T + Line);
							path.lineTo(KR4L, KR4T);
							path.lineTo(KR4L + Line , KR4T);
							path.moveTo(KR4R , KR4T + Line);
							path.lineTo(KR4R , KR4T);
							path.lineTo(KR4R - Line , KR4T);
							path.moveTo(KR4R , KR4B - Line);
							path.lineTo(KR4R , KR4B);
							path.lineTo(KR4R - Line , KR4B);
							path.moveTo(KR4L + Line , KR4B);
							path.lineTo(KR4L , KR4B);
							path.lineTo(KR4L , KR4B - Line);		
							mainPaint.setColor(color_008);
							if(MyApplication.flagEverConnect == false){
								mainPaint.setARGB(125, 180, 192, 194);
							}
							canvas.drawPath(path, mainPaint);
							mainPaint.setColor(color_004_b);
						} else {
							mainPaint.setColor(color_008);
						}
						
						mainPaint.setARGB(125, 180, 192, 194);
					} else {
						drawable2 = drawKhung;
						if(StateSearch1 == ALL){
							mainPaint.setColor(color_004_b);
							drawable2 = drawKhungActive;
						} else {
							mainPaint.setColor(color_008);
						}
						drawable2.setBounds((int)KR4L, (int)KR4T, (int)KR4R, (int)KR4B);
						drawable2.draw(canvas);
						
					}
					mainPaint.setTextSize(KS2);
					mainPaint.setStyle(Style.FILL);
					lengthText = mainPaint.measureText("TAT CA");
					canvas.drawText(getResources().getString(R.string.search_3), 
							(KR4L + KR4R)/2 - lengthText/2 , (float) ((KR3T + KR3B)/2 + KS2/2.5) , mainPaint);	
					
				}break;
				
				case REMIX : {
					drawable = getResources().getDrawable(R.drawable.touch_search_icon_72x72);
					if (isActiveX) {
						drawable = getResources().getDrawable(R.drawable.touch_ico_clear);
					}
					drawable.setBounds(KD1L , KD1T , KD1R , KD1B);
					drawable.draw(canvas);
						//------------------//
					mainPaint.setStyle(Style.FILL);
					mainPaint.setColor(color_002);
					canvas.drawRect(KR1L , KR1T , KR1R , KR1B , mainPaint);
					mainPaint.setStyle(Style.STROKE);
					if (!isActiveSearch) {
						mainPaint.setColor(color_003);
					} else {
						mainPaint.setColor(color_010);
					}
					mainPaint.setStrokeWidth((float) 1.0);
					canvas.drawRect((float) (KR1L) , (float) (KR1T) , (float) (KR1R) ,(float) (KR1B) , mainPaint);
						//------------------//
					mainPaint.setStyle(Style.FILL);
					mainPaint.setColor(color_004);
					mainPaint.setTypeface(Typeface.DEFAULT);
					mainPaint.setTextSize(KS1);
					if (textSearch.equals("")) {
						mainPaint.setColor(color_005);
						mainPaint.setTextSize(KS1_hint);
						canvas.drawText(textHint , KTSearch , KTY1 , mainPaint);
					} else {				
						mainPaint.setColor(color_010);
						if(!MyApplication.flagRealKeyboard){
							canvas.drawText(textSearch , KTSearch , KTY1 , mainPaint);	
						}						
					}
					if(isActiveSearch){
						mainPaint.setColor(color_010);
						if(!MyApplication.flagRealKeyboard){
							canvas.drawLine(positionX , KLY1 , positionX , KLY2 , mainPaint);	
						}							
					}
					
					drawable2 = drawKhung;
					if(StateSearch1 == VIDEO){	
						mainPaint.setColor(color_004_b);
						drawable2 = drawKhungActive;
					} else {
						mainPaint.setColor(color_008);
					}
					drawable2.setBounds((int)KR2L, (int)KR2T, (int)KR2R, (int)KR2B);
					drawable2.draw(canvas);
					
					mainPaint.setTextSize(KS2);
					mainPaint.setStyle(Style.FILL);
					float lengthText = mainPaint.measureText("KTV");
					canvas.drawText("KTV", (KR2L + KR2R)/2 - lengthText/2 , (float) ((KR2T + KR2B)/2 + KS2/2.5) , mainPaint);
						//--------------------//
					drawable2 = drawKhung;
					if(StateSearch1 == MIDI){
						mainPaint.setColor(color_004_b);
						drawable2 = drawKhungActive;
					} else {
						mainPaint.setColor(color_008);
					}
					drawable2.setBounds((int)KR3L, (int)KR3T, (int)KR3R, (int)KR3B);
					drawable2.draw(canvas);
					
					mainPaint.setTextSize(KS2);
					mainPaint.setStyle(Style.FILL);
					lengthText = mainPaint.measureText("MIDI");
					canvas.drawText("MIDI", (KR3L + KR3R)/2 - lengthText/2 , (float) ((KR3T + KR3B)/2 + KS2/2.5) , mainPaint);
						//--------------------//	
					drawable2 = drawKhung;
					if(StateSearch1 == TATCA){							
						mainPaint.setColor(color_004_b);
						drawable2 = drawKhungActive;
					} else {
						mainPaint.setColor(color_008);
					}
					drawable2.setBounds((int)KR4L, (int)KR4T, (int)KR4R, (int)KR4B);
					drawable2.draw(canvas);
					
					mainPaint.setTextSize(KS2);
					mainPaint.setStyle(Style.FILL);
					lengthText = mainPaint.measureText("TAT CA");
					canvas.drawText(getResources().getString(R.string.search_3), 
							(KR4L + KR4R)/2 - lengthText/2 , (float) ((KR3T + KR3B)/2 + KS2/2.5) , mainPaint);			
			
					
				}break;
				
				case SONGTYPE : {
					mainPaint.setStyle(Style.FILL);
					mainPaint.setColor(color_004);
					mainPaint.setTextSize(nameS);
					canvas.drawText(nameCharacter , nameX + 10, nameY , mainPaint);
				}break;
				
				case NOLAYOUT : {
					mainPaint.setStyle(Style.FILL);
					mainPaint.setColor(color_004);
					mainPaint.setTextSize(nameS);
					canvas.drawText(nameCharacter , nameX , nameY , mainPaint);
						//--------- KHIEM ------------
					drawable2 = drawKhung;
					if(StateSearch1 == VIDEO){	
						mainPaint.setColor(color_004_b);
						drawable2 = drawKhungActive;
					} else {
						mainPaint.setColor(color_008);
					}
					drawable2.setBounds((int)KR2L, (int)KR2T, (int)KR2R, (int)KR2B);
					drawable2.draw(canvas);
					
					mainPaint.setTextSize(KS2);
					mainPaint.setStyle(Style.FILL);
					float lengthText = mainPaint.measureText("KTV");
					canvas.drawText("KTV", (KR2L + KR2R)/2 - lengthText/2 , (float) ((KR2T + KR2B)/2 + KS2/2.5) , mainPaint);
						//--------------------//
					drawable2 = drawKhung;
					if(StateSearch1 == MIDI){
						mainPaint.setColor(color_004_b);
						drawable2 = drawKhungActive;
					} else {
						mainPaint.setColor(color_008);
					}
					drawable2.setBounds((int)KR3L, (int)KR3T, (int)KR3R, (int)KR3B);
					drawable2.draw(canvas);
					
					mainPaint.setTextSize(KS2);
					mainPaint.setStyle(Style.FILL);
					lengthText = mainPaint.measureText("MIDI");
					canvas.drawText("MIDI", (KR3L + KR3R)/2 - lengthText/2 , (float) ((KR3T + KR3B)/2 + KS2/2.5) , mainPaint);
						//--------------------//	
					drawable2 = drawKhung;
					if(StateSearch1 == TATCA){							
						mainPaint.setColor(color_004_b);
						drawable2 = drawKhungActive;
					} else {
						mainPaint.setColor(color_008);
					}
					drawable2.setBounds((int)KR4L, (int)KR4T, (int)KR4R, (int)KR4B);
					drawable2.draw(canvas);
					
					mainPaint.setTextSize(KS2);
					mainPaint.setStyle(Style.FILL);
					lengthText = mainPaint.measureText("TAT CA");
					canvas.drawText(getResources().getString(R.string.search_3), 
							(KR4L + KR4R)/2 - lengthText/2 , (float) ((KR3T + KR3B)/2 + KS2/2.5) , mainPaint);			
			
				} break;
				}	
				
				/*
				if(SaveLayout != PLAYLIST && viewBack.getVisiable() == GONE &&
					MyApplication.intWifiRemote == MyApplication.SONCA && 
					MyApplication.bOffUserList == true){
					drawUser.setBounds(rectUser);
					drawUser.draw(canvas);
				}
				 */
				
				if(SaveLayout != PLAYLIST && SaveLayout != NOLAYOUT && SaveLayout != SONGTYPE && 
					MyApplication.intWifiRemote == MyApplication.SONCA && 
					MyApplication.bOffUserList == true){
					drawUser.setBounds(rectUser);
					drawUser.draw(canvas);
				}
				
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){

				setK(getWidth() , getHeight());
				widthLayout = getWidth();
				heightLayout = getHeight();
				
				Path path = new Path();
				
				mainPaint.setStyle(Style.FILL);
				mainPaint.setColor(color_001);
				canvas.drawRect(0 , 0 , getWidth() , getHeight() , mainPaint);
				
				isActiveX = !textSearch.equals("");
				
				switch (SaveLayout) {
				case SONG : {
					
						zlightdrawable = getResources().getDrawable(R.drawable.zlight_search_icon_72x72);
						if (isActiveX) {
							zlightdrawable = getResources().getDrawable(R.drawable.zlight_ico_clear);
						}
						zlightdrawable.setBounds(KD1L , KD1T , KD1R , KD1B);
						zlightdrawable.draw(canvas);
					
							//--------------------//
						
						mainPaint.setStyle(Style.FILL);
						mainPaint.setColor(color_002);
						canvas.drawRect(KR1L , KR1T , KR1R , KR1B , mainPaint);
						mainPaint.setStyle(Style.STROKE);
						if (!isActiveSearch) {
							mainPaint.setColor(color_003);
						} else {
							mainPaint.setColor(color_005);
						}
						mainPaint.setStrokeWidth((float) 1.0);
						canvas.drawRect((float) (KR1L) , (float) (KR1T) , (float) (KR1R) ,(float) (KR1B) , mainPaint);
		/*
						drawable = getResources().getDrawable(R.drawable.del_icon_72x72);
						drawable.setBounds(KD2L , KD2T , KD2R , KD2B);
						drawable.draw(canvas);
		*/				
		/* CHON TEN, LOI, CHU ...				
						canvas.drawLine(KLX1, KLY1, KLX2, KLY2, mainPaint);
						mainPaint.setStyle(Style.FILL);
						mainPaint.setColor(color_004);
						mainPaint.setTextSize(KS1);
							//--------------------//
						
						String text = "";
						switch (StateSearch2) {
							case LOI:
								text = getResources().getString(R.string.search_2a);
								break;
							case TEN:
								text = getResources().getString(R.string.search_2b);
								break;
							case CHU:
								text = getResources().getString(R.string.search_2c);
								break;
							default:	break;
						}
						float length = mainPaint.measureText(text);
						canvas.drawText(text, (KLX1 + KR1R)/2 - length/2 , KTY1 , mainPaint);
		*/				
						
						mainPaint.setStyle(Style.FILL);
						mainPaint.setColor(color_004);
						mainPaint.setTextSize(KS1);
						mainPaint.setTypeface(Typeface.DEFAULT);
						if (textSearch.equals("")) {
							mainPaint.setColor(color_005);
							mainPaint.setTextSize(KS1_hint);
							canvas.drawText(textHint , KTSearch , KTY1 , mainPaint);
						} else {						
							mainPaint.setColor(color_010);
							canvas.drawText(textSearch , KTSearch , KTY1 , mainPaint);
						}
						if(isActiveSearch){
							mainPaint.setColor(color_010);
							canvas.drawLine(positionX , KLY1 , positionX , KLY2 , mainPaint);	
						}	
						
							//--------------------//
						mainPaint.setStyle(Style.FILL);
						mainPaint.setColor(color_006);
						canvas.drawRect(KR2L , KR2T , KR2R , KR2B , mainPaint);
						mainPaint.setStyle(Style.STROKE);
						mainPaint.setColor(color_007);
						mainPaint.setStrokeWidth((float) 1.0);
						canvas.drawRect(KR2L , KR2T , KR2R , KR2B  , mainPaint);
						if(StateSearch1 == VIDEO){
							zlightActive.setBounds((int)KR2L, (int)KR2T, (int)KR2R, (int)KR2B);
							zlightActive.draw(canvas);
							mainPaint.setColor(color_004);
						} else {
							zlightInActive.setBounds((int)KR2L, (int)KR2T, (int)KR2R, (int)KR2B);
							zlightInActive.draw(canvas);
							mainPaint.setColor(color_008);
						}
						mainPaint.setTextSize(KS2);
						mainPaint.setStyle(Style.FILL);
						float lengthText = mainPaint.measureText("KTV");
						canvas.drawText("KTV", (KR2L + KR2R)/2 - lengthText/2 , (float) ((KR2T + KR2B)/2 + KS2/2.5) , mainPaint);
							//--------------------//
						mainPaint.setStyle(Style.FILL);
						mainPaint.setColor(color_006);
						canvas.drawRect(KR3L , KR3T , KR3R , KR3B , mainPaint);
						mainPaint.setStyle(Style.STROKE);
						mainPaint.setColor(color_007);
						mainPaint.setStrokeWidth((float) 1.0);
						canvas.drawRect(KR3L , KR3T , KR3R , KR3B  , mainPaint);
						if(StateSearch1 == MIDI){
							zlightActive.setBounds((int)KR3L, (int)KR3T, (int)KR3R, (int)KR3B);
							zlightActive.draw(canvas);
							mainPaint.setColor(color_004);
						} else {
							zlightInActive.setBounds((int)KR3L, (int)KR3T, (int)KR3R, (int)KR3B);
							zlightInActive.draw(canvas);
							mainPaint.setColor(color_008);
						}
						mainPaint.setTextSize(KS2);
						mainPaint.setStyle(Style.FILL);
						lengthText = mainPaint.measureText("MIDI");
						canvas.drawText("MIDI", (KR3L + KR3R)/2 - lengthText/2 , (float) ((KR3T + KR3B)/2 + KS2/2.5) , mainPaint);
							//--------------------//	
						mainPaint.setStyle(Style.FILL);
						mainPaint.setColor(color_006);
						canvas.drawRect(KR4L , KR4T , KR4R , KR4B , mainPaint);
						mainPaint.setStyle(Style.STROKE);
						mainPaint.setColor(color_007);
						mainPaint.setStrokeWidth((float) 1.0);
						canvas.drawRect(KR4L , KR4T , KR4R , KR4B  , mainPaint);
						if(StateSearch1 == TATCA){
							zlightActive.setBounds((int)KR4L, (int)KR4T, (int)KR4R, (int)KR4B);
							zlightActive.draw(canvas);
							mainPaint.setColor(color_004);
						} else {
							zlightInActive.setBounds((int)KR4L, (int)KR4T, (int)KR4R, (int)KR4B);
							zlightInActive.draw(canvas);
							mainPaint.setColor(color_008);
						}
						mainPaint.setTextSize(KS2);
						mainPaint.setStyle(Style.FILL);
						lengthText = mainPaint.measureText("TAT CA");
						canvas.drawText(getResources().getString(R.string.search_3), 
								(KR4L + KR4R)/2 - lengthText/2 , (float) ((KR3T + KR3B)/2 + KS2/2.5) , mainPaint);			
				}break;
				case PLAYLIST : {
					mainPaint.setStyle(Style.FILL);
					mainPaint.setColor(color_004);
					mainPaint.setTextSize(nameS);
					canvas.drawText(textSum , nameX + 10 , nameY , mainPaint);				
				}break;		
				case SINGER : {
					zlightdrawable = getResources().getDrawable(R.drawable.zlight_search_icon_72x72);
					if (isActiveX) {
						zlightdrawable = getResources().getDrawable(R.drawable.zlight_ico_clear);
					}
					zlightdrawable.setBounds(KD1L , KD1T , KD1R , KD1B);
					zlightdrawable.draw(canvas);
		/*	DELETE ICON			
					drawable = getResources().getDrawable(R.drawable.del_icon_72x72);
					drawable.setBounds(KD3L , KD3T , KD3R , KD3B);
					drawable.draw(canvas);
		*/			
						//------------------//
					mainPaint.setStyle(Style.FILL);
					mainPaint.setColor(color_002);
					canvas.drawRect(KR1L , KR1T , KR1R , KR1B , mainPaint);
					mainPaint.setStyle(Style.STROKE);
					if (!isActiveSearch) {
						mainPaint.setColor(color_003);
					} else {
						mainPaint.setColor(color_010);
					}
					mainPaint.setStrokeWidth((float) 1.0);
					canvas.drawRect((float) (KR1L) , (float) (KR1T) , (float) (KR1R) ,(float) (KR1B) , mainPaint);
	
					mainPaint.setStyle(Style.FILL);
					mainPaint.setColor(color_004);
					mainPaint.setTypeface(Typeface.DEFAULT);
					mainPaint.setTextSize(KS1);
					if (textSearch.equals("")) {
						mainPaint.setColor(color_005);
						mainPaint.setTextSize(KS1_hint);
						canvas.drawText(textHint , KTSearch , KTY1 , mainPaint);
					} else {					
						mainPaint.setColor(color_010);
						canvas.drawText(textSearch , KTSearch , KTY1 , mainPaint);
					}
					if(isActiveSearch){
						mainPaint.setColor(color_010);
						canvas.drawLine(positionX , KLY1 , positionX , KLY2 , mainPaint);	
					}	
						//--------------------//
					mainPaint.setStyle(Style.FILL);
					mainPaint.setColor(color_006);
					canvas.drawRect(KR2LV , KR2T , KR2R , KR2B , mainPaint);
					mainPaint.setStyle(Style.STROKE);
					mainPaint.setColor(color_007);
					mainPaint.setStrokeWidth((float) 1.0);
					canvas.drawRect(KR2LV , KR2T , KR2R , KR2B  , mainPaint);
					if(StateSearch1 == VIETNAM){
						zlightActive.setBounds((int)KR2LV, (int)KR2T, (int)KR2R, (int)KR2B);
						zlightActive.draw(canvas);
						mainPaint.setColor(color_004);
					} else {
						zlightInActive.setBounds((int)KR2LV, (int)KR2T, (int)KR2R, (int)KR2B);
						zlightInActive.draw(canvas);
						mainPaint.setColor(color_008);
					}
					mainPaint.setTextSize(KS2);
					mainPaint.setStyle(Style.FILL);
					String string = getResources().getString(R.string.vietnam);
					float lengthText = mainPaint.measureText(string);
					canvas.drawText(string , (KR2LV + KR2R)/2 - lengthText/2 , (float) ((KR2T + KR2B)/2 + KS2/2.5) , mainPaint);
						//--------------------//
					mainPaint.setStyle(Style.FILL);
					mainPaint.setColor(color_006);
					canvas.drawRect(KR3L , KR3T , KR3R , KR3B , mainPaint);
					mainPaint.setStyle(Style.STROKE);
					mainPaint.setColor(color_007);
					mainPaint.setStrokeWidth((float) 1.0);
					if(MyApplication.flagEverConnect == false){
						mainPaint.setColor(Color.GRAY);
					}
					canvas.drawRect(KR3L , KR3T , KR3R , KR3B  , mainPaint);
					if(StateSearch1 == OTHER){
						if(MyApplication.flagEverConnect == false){
							zlightXam.setBounds((int)KR3L, (int)KR3T, (int)KR3R, (int)KR3B);
							zlightXam.draw(canvas);
						}else{
							zlightActive.setBounds((int)KR3L, (int)KR3T, (int)KR3R, (int)KR3B);
							zlightActive.draw(canvas);
						}
						mainPaint.setColor(color_004);
					} else {
						zlightInActive.setBounds((int)KR3L, (int)KR3T, (int)KR3R, (int)KR3B);
						zlightInActive.draw(canvas);
						mainPaint.setColor(color_008);
					}
					if(MyApplication.flagEverConnect == false){
						mainPaint.setColor(Color.GRAY);
					}
					mainPaint.setTextSize(KS2);
					mainPaint.setStyle(Style.FILL);
					string = getResources().getString(R.string.khac);
					lengthText = mainPaint.measureText(string);
					canvas.drawText(string, (KR3L + KR3R)/2 - lengthText/2 , (float) ((KR3T + KR3B)/2 + KS2/2.5) , mainPaint);
						//--------------------//	
					mainPaint.setStyle(Style.FILL);
					mainPaint.setColor(color_006);
					canvas.drawRect(KR4L , KR4T , KR4R , KR4B , mainPaint);
					mainPaint.setStyle(Style.STROKE);
					mainPaint.setColor(color_007);
					mainPaint.setStrokeWidth((float) 1.0);
					if(MyApplication.flagEverConnect == false){
						mainPaint.setColor(Color.GRAY);
					}
					canvas.drawRect(KR4L , KR4T , KR4R , KR4B  , mainPaint);
					if(StateSearch1 == ALL){
						if(MyApplication.flagEverConnect == false){
							zlightXam.setBounds((int)KR4L, (int)KR4T, (int)KR4R, (int)KR4B);
							zlightXam.draw(canvas);
						}else{
							zlightActive.setBounds((int)KR4L, (int)KR4T, (int)KR4R, (int)KR4B);
							zlightActive.draw(canvas);
						}
						mainPaint.setColor(color_004);
					} else {
						zlightInActive.setBounds((int)KR4L, (int)KR4T, (int)KR4R, (int)KR4B);
						zlightInActive.draw(canvas);
						mainPaint.setColor(color_008);
					}
					if(MyApplication.flagEverConnect == false){
						mainPaint.setColor(Color.GRAY);
					}
					mainPaint.setTextSize(KS2);
					mainPaint.setStyle(Style.FILL);
					lengthText = mainPaint.measureText("TAT CA");
					canvas.drawText(getResources().getString(R.string.search_3), 
							(KR4L + KR4R)/2 - lengthText/2 , (float) ((KR3T + KR3B)/2 + KS2/2.5) , mainPaint);	
					
				}break;
				
				case REMIX : {
					zlightdrawable = getResources().getDrawable(R.drawable.zlight_search_icon_72x72);
					if (isActiveX) {
						zlightdrawable = getResources().getDrawable(R.drawable.zlight_ico_clear);
					}
					zlightdrawable.setBounds(KD1L , KD1T , KD1R , KD1B);
					zlightdrawable.draw(canvas);
						//------------------//
					mainPaint.setStyle(Style.FILL);
					mainPaint.setColor(color_002);
					canvas.drawRect(KR1L , KR1T , KR1R , KR1B , mainPaint);
					mainPaint.setStyle(Style.STROKE);
					if (!isActiveSearch) {
						mainPaint.setColor(color_003);
					} else {
						mainPaint.setColor(color_010);
					}
					mainPaint.setStrokeWidth((float) 1.0);
					canvas.drawRect((float) (KR1L) , (float) (KR1T) , (float) (KR1R) ,(float) (KR1B) , mainPaint);
						//------------------//
					mainPaint.setStyle(Style.FILL);
					mainPaint.setColor(color_004);
					mainPaint.setTypeface(Typeface.DEFAULT);
					mainPaint.setTextSize(KS1);
					if (textSearch.equals("")) {
						mainPaint.setColor(color_005);
						mainPaint.setTextSize(KS1_hint);
						canvas.drawText(textHint , KTSearch , KTY1 , mainPaint);
					} else {				
						mainPaint.setColor(color_010);
						canvas.drawText(textSearch , KTSearch , KTY1 , mainPaint);
					}
					if(isActiveSearch){
						mainPaint.setColor(color_010);
						canvas.drawLine(positionX , KLY1 , positionX , KLY2 , mainPaint);	
					}
					
					mainPaint.setStyle(Style.FILL);
					mainPaint.setColor(color_006);
					canvas.drawRect(KR2L , KR2T , KR2R , KR2B , mainPaint);
					mainPaint.setStyle(Style.STROKE);
					mainPaint.setColor(color_007);
					mainPaint.setStrokeWidth((float) 1.0);
					canvas.drawRect(KR2L , KR2T , KR2R , KR2B  , mainPaint);
					if(StateSearch1 == VIDEO){
						zlightActive.setBounds((int)KR2L, (int)KR2T, (int)KR2R, (int)KR2B);
						zlightActive.draw(canvas);
						mainPaint.setColor(color_004);
					} else {
						zlightInActive.setBounds((int)KR2L, (int)KR2T, (int)KR2R, (int)KR2B);
						zlightInActive.draw(canvas);
						mainPaint.setColor(color_008);
					}
					mainPaint.setTextSize(KS2);
					mainPaint.setStyle(Style.FILL);
					float lengthText = mainPaint.measureText("KTV");
					canvas.drawText("KTV", (KR2L + KR2R)/2 - lengthText/2 , (float) ((KR2T + KR2B)/2 + KS2/2.5) , mainPaint);
						//--------------------//
					mainPaint.setStyle(Style.FILL);
					mainPaint.setColor(color_006);
					canvas.drawRect(KR3L , KR3T , KR3R , KR3B , mainPaint);
					mainPaint.setStyle(Style.STROKE);
					mainPaint.setColor(color_007);
					mainPaint.setStrokeWidth((float) 1.0);
					canvas.drawRect(KR3L , KR3T , KR3R , KR3B  , mainPaint);
					if(StateSearch1 == MIDI){
						zlightActive.setBounds((int)KR3L, (int)KR3T, (int)KR3R, (int)KR3B);
						zlightActive.draw(canvas);
						mainPaint.setColor(color_004);
					} else {
						zlightInActive.setBounds((int)KR3L, (int)KR3T, (int)KR3R, (int)KR3B);
						zlightInActive.draw(canvas);
						mainPaint.setColor(color_008);
					}
					mainPaint.setTextSize(KS2);
					mainPaint.setStyle(Style.FILL);
					lengthText = mainPaint.measureText("MIDI");
					canvas.drawText("MIDI", (KR3L + KR3R)/2 - lengthText/2 , (float) ((KR3T + KR3B)/2 + KS2/2.5) , mainPaint);
						//--------------------//	
					mainPaint.setStyle(Style.FILL);
					mainPaint.setColor(color_006);
					canvas.drawRect(KR4L , KR4T , KR4R , KR4B , mainPaint);
					mainPaint.setStyle(Style.STROKE);
					mainPaint.setColor(color_007);
					mainPaint.setStrokeWidth((float) 1.0);
					canvas.drawRect(KR4L , KR4T , KR4R , KR4B  , mainPaint);
					if(StateSearch1 == TATCA){
						zlightActive.setBounds((int)KR4L, (int)KR4T, (int)KR4R, (int)KR4B);
						zlightActive.draw(canvas);
						mainPaint.setColor(color_004);
					} else {
						zlightInActive.setBounds((int)KR4L, (int)KR4T, (int)KR4R, (int)KR4B);
						zlightInActive.draw(canvas);
						mainPaint.setColor(color_008);
					}
					mainPaint.setTextSize(KS2);
					mainPaint.setStyle(Style.FILL);
					lengthText = mainPaint.measureText("TAT CA");
					canvas.drawText(getResources().getString(R.string.search_3), 
							(KR4L + KR4R)/2 - lengthText/2 , (float) ((KR3T + KR3B)/2 + KS2/2.5) , mainPaint);			
			
					
				}break;
				
				case SONGTYPE : {
					mainPaint.setStyle(Style.FILL);
					mainPaint.setColor(color_004);
					mainPaint.setTextSize(nameS);
					canvas.drawText(nameCharacter , nameX , nameY , mainPaint);
				}break;
				
				case NOLAYOUT : {
					mainPaint.setStyle(Style.FILL);
					mainPaint.setColor(color_004);
					mainPaint.setTextSize(nameS);
					canvas.drawText(nameCharacter , nameX , nameY , mainPaint);
						//--------- KHIEM ------------
					mainPaint.setStyle(Style.FILL);
					mainPaint.setColor(color_006);
					canvas.drawRect(KR2L , KR2T , KR2R , KR2B , mainPaint);
					mainPaint.setStyle(Style.STROKE);
					mainPaint.setColor(color_007);
					mainPaint.setStrokeWidth((float) 1.0);
					canvas.drawRect(KR2L , KR2T , KR2R , KR2B  , mainPaint);
					if(StateSearch1 == VIDEO){
						zlightActive.setBounds((int)KR2L, (int)KR2T, (int)KR2R, (int)KR2B);
						zlightActive.draw(canvas);
						mainPaint.setColor(color_004);
					} else {
						zlightInActive.setBounds((int)KR2L, (int)KR2T, (int)KR2R, (int)KR2B);
						zlightInActive.draw(canvas);
						mainPaint.setColor(color_008);
					}
					mainPaint.setTextSize(KS2);
					mainPaint.setStyle(Style.FILL);
					float lengthText = mainPaint.measureText("KTV");
					canvas.drawText("KTV", (KR2L + KR2R)/2 - lengthText/2 , (float) ((KR2T + KR2B)/2 + KS2/2.5) , mainPaint);
						//--------------------//
					mainPaint.setStyle(Style.FILL);
					mainPaint.setColor(color_006);
					canvas.drawRect(KR3L , KR3T , KR3R , KR3B , mainPaint);
					mainPaint.setStyle(Style.STROKE);
					mainPaint.setColor(color_007);
					mainPaint.setStrokeWidth((float) 1.0);
					canvas.drawRect(KR3L , KR3T , KR3R , KR3B  , mainPaint);
					if(StateSearch1 == MIDI){
						zlightActive.setBounds((int)KR3L, (int)KR3T, (int)KR3R, (int)KR3B);
						zlightActive.draw(canvas);
						mainPaint.setColor(color_004);
					} else {
						zlightInActive.setBounds((int)KR3L, (int)KR3T, (int)KR3R, (int)KR3B);
						zlightInActive.draw(canvas);
						mainPaint.setColor(color_008);
					}
					mainPaint.setTextSize(KS2);
					mainPaint.setStyle(Style.FILL);
					lengthText = mainPaint.measureText("MIDI");
					canvas.drawText("MIDI", (KR3L + KR3R)/2 - lengthText/2 , (float) ((KR3T + KR3B)/2 + KS2/2.5) , mainPaint);
						//--------------------//	
					mainPaint.setStyle(Style.FILL);
					mainPaint.setColor(color_006);
					canvas.drawRect(KR4L , KR4T , KR4R , KR4B , mainPaint);
					mainPaint.setStyle(Style.STROKE);
					mainPaint.setColor(color_007);
					mainPaint.setStrokeWidth((float) 1.0);
					canvas.drawRect(KR4L , KR4T , KR4R , KR4B  , mainPaint);
					if(StateSearch1 == TATCA){
						zlightActive.setBounds((int)KR4L, (int)KR4T, (int)KR4R, (int)KR4B);
						zlightActive.draw(canvas);
						mainPaint.setColor(color_004);
					} else {
						zlightInActive.setBounds((int)KR4L, (int)KR4T, (int)KR4R, (int)KR4B);
						zlightInActive.draw(canvas);
						mainPaint.setColor(color_008);
					}
					mainPaint.setTextSize(KS2);
					mainPaint.setStyle(Style.FILL);
					lengthText = mainPaint.measureText("TAT CA");
					canvas.drawText(getResources().getString(R.string.search_3), 
							(KR4L + KR4R)/2 - lengthText/2 , (float) ((KR3T + KR3B)/2 + KS2/2.5) , mainPaint);			
			
				} break;
				}	
				
				/*
				if(SaveLayout != PLAYLIST && viewBack.getVisiable() == GONE &&
					MyApplication.intWifiRemote == MyApplication.SONCA && 
					MyApplication.bOffUserList == true){
					drawUser.setBounds(rectUser);
					drawUser.draw(canvas);
				}
				 */
				
				if(SaveLayout != PLAYLIST && SaveLayout != NOLAYOUT && SaveLayout != SONGTYPE && 
					MyApplication.intWifiRemote == MyApplication.SONCA && 
					MyApplication.bOffUserList == true){
					zlightdrawUser.setBounds(rectUser);
					zlightdrawUser.draw(canvas);
				}
				
		
		}
		
		if(MyApplication.flagRealKeyboard){
			if(listener != null){
				listener.OnCallEditTextSearchLayout();
			}	
		}	
		
	}

    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	switch (event.getAction()) {
    	case MotionEvent.ACTION_MOVE: {
    		float x = event.getX();
			float y = event.getY();
    		if (x>=KR1L && x<=KLX1 && y>=KR1T && y<=KR1B) {
	    		positionX = getPointerText(x , KTSearch , KS1 , textSearch);
	    		invalidate();
    		}
    	}break;
		case MotionEvent.ACTION_UP: {
			float x = event.getX();
			float y = event.getY();
			if(SaveLayout == REMIX){
				if(x > 0 && x< KD1R){
					if(isActiveX){ // x
						isActiveX = false;
						isActiveSearch = false;
						textSearch = "";
						positionT = 0;
						positionX = getPointerText(x - viewBackWidth, KTSearch, KS1, textSearch);
						if (listener != null) {
							listener.OnCloseKeyBoard();
							listener.OnSearch(StateSearch2, StateSearch1,
									textSearch);
						}
					}
					else{
						isActiveSearch = true;
						if (listener != null) {
							listener.OnShowKeyBoard();
						}
					}
				}
				
				if (x>=KR1L && x<KD2L && y>=KR1T && y<=KR1B) {
					// positionX = getPointerText(x , KTSearch , KS1 , textSearch);
					isActiveSearch = true;
					if(listener != null){
						listener.OnShowKeyBoard();
					}
				}
				if (x>=KR2L && x<=KR2R && y>=KR2T && y<=KR2B) {
					StateSearch1 = VIDEO;
					if(listener != null){
						listener.OnSearch(StateSearch2 , StateSearch1 , textSearch);
					}
				}
				if (x>=KR3L && x<=KR3R && y>=KR3T && y<=KR3B) {
					StateSearch1 = MIDI;
					if(listener != null){
						listener.OnSearch(StateSearch2 , StateSearch1 , textSearch);
					}
				}
				if (x>=KR4L && x<=KR4R && y>=KR4T && y<=KR4B) {
					StateSearch1 = TATCA;
					if(listener != null){
						listener.OnSearch(StateSearch2 , StateSearch1 , textSearch);
					}
				}
			}
			if (SaveLayout == SONG) {
				if(x > 0 && x< KD1R){
					if(isActiveX){ // x
						isActiveX = false;
						isActiveSearch = false;
						textSearch = "";
						positionT = 0;
						if (listener != null) {
							listener.OnCloseKeyBoard();
							listener.OnSearch(StateSearch2, StateSearch1,
									textSearch);
						}
					}
					else{
						isActiveSearch = true;
						if (listener != null) {
							listener.OnShowKeyBoard();
						}
					}
				}
								
				if (x>=KR1L && x<=KR1R && y>=KR1T && y<=KR1B) {
					// positionX = getPointerText(x , KTSearch , KS1 , textSearch);
					isActiveSearch = true;
					if(listener != null){
						listener.OnShowKeyBoard();
					}
				}
/*	CHUYEN SEARCH					
				if (x>=KLX1 && x<=KR1R && y>=KR1T && y<=KR1B) {
					StateSearch2++;
					if (StateSearch2 > 2) {
						StateSearch2 = 0;
					}
					if(listener != null){
						MyLog.d(VIEW_LOG_TAG, "OnSearch()");
						listener.OnSearch(StateSearch2 , StateSearch1 , textSearch);
					}
				}
*/				
				if (x>=KR2L && x<=KR2R && y>=KR2T && y<=KR2B) {
					StateSearch1 = VIDEO;
					if(listener != null){
						listener.OnSearch(StateSearch2 , StateSearch1 , textSearch);
					}
				}
				if (x>=KR3L && x<=KR3R && y>=KR3T && y<=KR3B) {
					StateSearch1 = MIDI;
					if(listener != null){
						listener.OnSearch(StateSearch2 , StateSearch1 , textSearch);
					}
				}
				if (x>=KR4L && x<=KR4R && y>=KR4T && y<=KR4B) {
					StateSearch1 = TATCA;
					if(listener != null){
						listener.OnSearch(StateSearch2 , StateSearch1 , textSearch);
					}
				}
				if (x>=KR5L && x<=KR5R && y>=KR5T && y<=KR5B) {
					StateSearch1 = ONLINE;
					if(listener != null){
						listener.OnSearch(StateSearch2 , StateSearch1 , textSearch);
					}
				}
			}
			if (SaveLayout == YOUTUBE){

				if(x > 0 && x< KD1R){
					if(isActiveX){ // x
						isActiveX = false;
						isActiveSearch = false;
						textSearch = "";
						positionT = 0;
						if (listener != null) {
							listener.OnCloseKeyBoard();
							listener.OnSearch(StateSearch2, StateSearch1,
									textSearch);
						}
					}
					else{
						isActiveSearch = true;
						if (listener != null) {
							listener.OnShowKeyBoard();
						}
					}
				}								
				if (x>=KR1L && x<=KR1R && y>=KR1T && y<=KR1B) {
					isActiveSearch = true;
					if(listener != null){
						listener.OnShowKeyBoard();
					}
				}
				if (x>=KR3L && x<=KR3R && y>=KR3T && y<=KR3B) {
					StateSearch1 = YOUTUBE_KARAOKE;
					if(listener != null){
						listener.OnSearch(StateSearch2 , StateSearch1 , textSearch);
					}
				}
				if (x>=KR4L && x<=KR4R && y>=KR4T && y<=KR4B) {
					StateSearch1 = TATCA;
					if(listener != null){
						listener.OnSearch(StateSearch2 , StateSearch1 , textSearch);
					}
				}
			
			}
			if (SaveLayout == PLAYLIST) {
				if (x>=KR2L && x<=KR2R && y>=KR2T && y<=KR2B) {
					StateSearch1 = VIDEO;
					if(listener != null){
						listener.OnSearch(StateSearch2 , StateSearch1 , textSearch);
					}
				}
				if (x>=KR3L && x<=KR3R && y>=KR3T && y<=KR3B) {
					StateSearch1 = MIDI;
					if(listener != null){
						listener.OnSearch(StateSearch2 , StateSearch1 , textSearch);
					}
				}
				if (x>=KR4L && x<=KR4R && y>=KR4T && y<=KR4B) {
					StateSearch1 = TATCA;
					if(listener != null){
						listener.OnSearch(StateSearch2 , StateSearch1 , textSearch);
					}
				}
			}
			if (SaveLayout == SINGER) {
				if(x > 0 && x< KD1R){
					if(isActiveX){ // x
						isActiveX = false;
						isActiveSearch = false;
						textSearch = "";
						positionT = 0;
						positionX = getPointerText(x - viewBackWidth, KTSearch,
								KS1, textSearch);
						if (listener != null) {
							listener.OnCloseKeyBoard();
							listener.OnSearch(StateSearch2, StateSearch1,
									textSearch);
						}
					}
					else{
						isActiveSearch = true;
						if (listener != null) {
							listener.OnShowKeyBoard();
						}
					}
				}
				if(x>=KD2L && x<=KD2R && y>=KD2T && y<=KD2B){ // Delete
					isActiveX = true;
					isActiveSearch = true;
					textSearch = "";
					positionT = 0;
					positionX = getPointerText(x - viewBackWidth, KTSearch, KS3, textSearch);
					if (listener != null) {
						listener.OnSearch(StateSearch2, StateSearch1,
								textSearch);
						listener.OnShowKeyBoard();
					}
				}
			
				if(x>=KD1L && x<=KD1R && y>=KD1T && y<=KD1B){
					/*	if(listener != null){
							listener.OnShowKeyBoard();
						}	*/			
				}
				if (x>=KR1L && x<=KR1R && y>=KR1T && y<=KR1B) {
					isActiveSearch = true;
					// positionX = getPointerText(x , KTSearch , KS1 , textSearch);
					if(listener != null){
						listener.OnShowKeyBoard();
					}
				}
/*				
				if(x>=KD3L && x<=KD3R && y>=KD3T && y<=KD3B){
					positionT = 0;		
					positionX = 0; 	
					textSearch = "";
					if(listener != null){
						listener.OnSearch(StateSearch2 , StateSearch1 , textSearch);
					}
				}
*/				
				if (x>=KR2LV && x<=KR2R && y>=KR2T && y<=KR2B) {
					StateSearch1 = VIETNAM;
					if(listener != null){
						listener.OnSearch(StateSearch2 , StateSearch1 , textSearch);
					}
				}
				if(MyApplication.flagEverConnect == false){
					return true;
				}
				if (x>=KR3L && x<=KR3R && y>=KR3T && y<=KR3B) {
					StateSearch1 = OTHER;
					if(listener != null){
						listener.OnSearch(StateSearch2 , StateSearch1 , textSearch);
					}
				}
				if (x>=KR4L && x<=KR4R && y>=KR4T && y<=KR4B) {
					StateSearch1 = ALL;
					if(listener != null){
						listener.OnSearch(StateSearch2 , StateSearch1 , textSearch);
					}
				}
			}
			if (SaveLayout == NOLAYOUT){
				if (x>=KR2L && x<=KR2R && y>=KR2T && y<=KR2B) {
					StateSearch1 = VIDEO;
					if(listener != null){
						listener.OnSearch(StateSearch2 , StateSearch1 , textSearch);
					}
				}
				if (x>=KR3L && x<=KR3R && y>=KR3T && y<=KR3B) {
					StateSearch1 = MIDI;
					if(listener != null){
						listener.OnSearch(StateSearch2 , StateSearch1 , textSearch);
					}
				}
				if (x>=KR4L && x<=KR4R && y>=KR4T && y<=KR4B) {
					StateSearch1 = TATCA;
					if(listener != null){
						listener.OnSearch(StateSearch2 , StateSearch1 , textSearch);
					}
				}
			}

			invalidate();
		}break;
		default : break;
		}
    	return true;
    }
    
    public static float KTSearch;
    float Line = 5;
    float KS1, KS1_hint , KTX1 , KTY1;
    float KS2 , KTX2 , KTY2;
    float KS3 , KTX3 , KTY3;
    float KS4 , KTX4 , KTY4;
    float KLX1 , KLY1 , KLX2 , KLY2;
    float KR1L , KR1R , KR1T , KR1B;
    float KR2L , KR2R , KR2T , KR2B;
    float KR3L , KR3R , KR3T , KR3B;
    float KR4L , KR4R , KR4T , KR4B; 
    float KR5L , KR5R , KR5T , KR5B;
    float KR2LV;
    int KH1 , KH2 , KH3;
    int KD1 , KD1L , KD1T , KD1R , KD1B;
    int KD2 , KD2L , KD2T , KD2R , KD2B;
    int KD3 , KD3L , KD3T , KD3R , KD3B;
    
    private Rect rectSearch;
    
    private void setK(int w , int h){
    	
    	
    	KTSearch = 190*w/1920;
    	
    	KH1 = 800*h/1080; 
    	KR1L = 168*w/1920; 
    	KR1R = 1330*w/1920; 
    	KR1T = (float) (0.45*h - KH1/1.9);
    	KR1B = (float) (0.45*h + KH1/1.9);
    	
    	KH3 =  700*h/1080; 
    	KLX1 = 1135*w/1920; 
    	KLY1 = (float) (0.45*h - KH3/2);
    	KLX2 = 1135*w/1920; 
    	KLY2 = (float) (0.45*h + KH3/2);
    	
    	if(MyApplication.flagHong == true){
    		KS1 = 400*h/1080;
    		KS1_hint = 300*h/1080;
    	}else{
    		KS1 = 500*h/1080;
    		KS1_hint = 400*h/1080;
    	}
    	KTX1 = 1155*w/1920; 
    	KTY1 = (float) (0.45*h + KS1/3);

    	//-----------//

    	KD1 = 900*h/1080; 
    	
    	KD1T = (int) (0.42*h - KD1/2.5);
    	KD1B = (int) (0.42*h + KD1/2.5);
    	KD1L = (int) (0.047*w - (KD1B - KD1T)/2); 
    	KD1R = (int) (0.047*w + (KD1B - KD1T)/2); 
    	
    	KD2L = 1230*w/1920; 
    	KD2R = KD2L + KD2; 
    	KD2T = h/2 - KD1/2;
    	KD2B = h/2 + KD1/2;
    	
    	KR2LV = 1340*w/1920; 
    	
    	KD2 = 1080*h/1080; 
    	KD3L = 1785*w/1920 ;
    	KD3R = KD3L + KD2;
    	KD3T = h/2 - KD2/2;
    	KD3B = h/2 + KD2/2;
    	
    	//-----------//
    	
    	// Line = 100*h/1080;
    	KH2 = 800*h/1080; 
    	KR2L = 1370*w/1920; 
    	KR2R = 1530*w/1920; 
    	KR2T = (float) (0.45*h - KH2/2.5);
    	KR2B = (float) (0.45*h + KH2/2.5);
    	
    	if(MyApplication.flagHong == true){
    		KS2 = (KR2B - KR2T)/3;
    	}else{
    		KS2 = (KR2B - KR2T)/2.6f;
    	}
    	KTX2 = 1450*w/1920; 
    	KTY2 = (float) (KR2B - 1.1*KR2T);
    	
    	//-----------//
    	
    	KR3L = 1540*w/1920; 
    	KR3R = 1700*w/1920; 
    	KR3T = KR2T;
    	KR3B = KR2B;
    	
    	KTX2 = 1450*w/1920; 
    	KTY2 = (float) (KR2B - 1.1*KR2T);
    	
    	//-----------//
    	
    	KR4L = 1710*w/1920; 
    	KR4R = 1910*w/1920; 
    	KR4T = KR2T;
    	KR4B = KR2B;

    }
    
////////////////////////// - POINTER - /////////////////////////////////
    
    private int positionT = 0;		// TODO Listener
	private float positionX = 0; 	// TODO Show
    
    private float getPointerText(float offsetX , float offset0 , float textSize , String text){
    	if(text == null || text.equals("")){
    		positionT = 0;
    		return offset0;
    	}
    	Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    	paint.setTextSize(textSize);
    	float maxOffset = paint.measureText(text) + offset0;
    	float out = 0;
    	if(offsetX <= offset0){
    		out = offset0;
    		positionT = 0;
    	} else if(offsetX >= maxOffset){
    		out = maxOffset;
    		positionT = text.length();
    	} else {
    		StringBuffer buffer = new StringBuffer(text);
    		for (int i = 0; i < buffer.length(); i++) {
    			float offset = paint.measureText(buffer.substring(0 , i)) + offset0;
    			if(offset >= offsetX){
    				break;
    			}else{
    				out = offset;
    				positionT = i;
    			}
			}
    	}
		return out;
    }
    
    private String insertCharText(char ch , float offset0 , String text , float textSize){
    	Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    	paint.setTextSize(textSize);
    	float f = paint.measureText(text);
    	if (SaveLayout == SONG && f >= (KR1R - KR1L - offset0)) {
    		return text;
		}
    	if(SaveLayout == SINGER && f >= (KR1R - KR1L - offset0)){
    		return text;
    	}
    	if(SaveLayout == YOUTUBE && f >= (KR1R - KR1L - offset0)){
    		return text;
    	}
    	StringBuffer buffer = null;
    	if (text == null) {
    		buffer = new StringBuffer("");
		} else {
			buffer = new StringBuffer(text);
		}
    	buffer.insert(positionT, ch);
    	positionX = paint.measureText(buffer.substring(0 , positionT) + ch) + offset0;
    	positionT++; 
		return buffer.toString();
    }
    
    private String clearCharText(float offset0 , String text , float textSize){
    	if(text == null || text.equals("")){
    		return "";
    	}
    	if(positionT == 0){
    		return text;
    	}
    	Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    	paint.setTextSize(textSize);
    	StringBuffer buffer = new StringBuffer(text);
    	buffer.deleteCharAt(positionT - 1);
    	positionT--; 
    	positionX = paint.measureText(buffer.substring(0 , positionT)) + offset0;
		return buffer.toString();
    }

    private TouchViewBack viewBack;
    public void setViewBack(TouchViewBack viewBack){
    	this.viewBack = viewBack;
    }

}
