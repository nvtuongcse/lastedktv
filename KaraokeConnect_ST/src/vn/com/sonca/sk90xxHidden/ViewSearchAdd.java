package vn.com.sonca.sk90xxHidden;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.zzzzz.MyApplication;

public class ViewSearchAdd extends View {
	
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

	public ViewSearchAdd(Context context) {
		super(context);
		initView(context);
	}

	public ViewSearchAdd(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public ViewSearchAdd(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private String textHint = "";
	private StringBuffer textSearch = new StringBuffer("");
	private String textAdd = "";

	private Drawable drawDelete;
	private Drawable drawSearch;
	private Drawable drawBackgroud;
	
	private void initView(Context context) {
		drawBackgroud = getResources().getDrawable(R.drawable.boder_search_to);
		drawSearch = getResources().getDrawable(R.drawable.touch_search_icon_72x72);
		drawDelete = getResources().getDrawable(R.drawable.del_icon_72x72);
		textPaint.setStyle(Style.FILL);
		textHint = getResources().getString(R.string.dialog_90xx_hid_1);
		drawKhung = getResources().getDrawable(R.drawable.total_khung_inactive);
		drawKhungActive = getResources().getDrawable(R.drawable.total_khung_active);
		drawKhungXam = getResources().getDrawable(R.drawable.total_khung_xam);
	}
	
	private OnSearchAddListener listener;
	public interface OnSearchAddListener {
		public void OnSearch(String search);
		public void OnShowKeyBoard();
		public void OnCloseKeyBoard();
		public void OnShowAddFrag();
		public void OnShowDelFrag();
		public void OnFilterCancel();
		public void OnFilterAll();
		public void OnChangeFilter(int stateFilter);
	}
	
	public void setOnSearchAddListener(OnSearchAddListener listener){
		this.listener = listener;
	}
		
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
	private Rect rectSearch = new Rect();
	private Rect rectDelete = new Rect();
	private Rect rectFrameSearch = new Rect();
	private int lineX, lineTop, lineBottom;
	private int textY, textS;
	private int textX1, textX2;
	private int widthText;
	
	private float KR3L, KR3R, KR3T, KR3B;
	private float KR4L, KR4R, KR4T, KR4B;
	private float KS2;
	
    @Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		rectBackgroud.set(0, 0, w, h);
		int tamX = (int) (0.55*h);
		int tamY = (int) (0.4*h);
		int hh = (int) (0.35*h);
		int ww = hh;
		rectSearch.set(tamX-ww, tamY-hh, tamX+ww, tamY+hh);
		
		tamX = (int) (0.5*w);
		tamY = (int) (0.45*h);
		hh = (int) (0.35*h);
		ww = hh;
//		rectFrameSearch.set((int)(1.0*h), tamY-hh, (int)(0.49*w), tamY+hh);
		rectFrameSearch.set((int)(1.0*h), tamY-hh, (int)(0.72*w), tamY+hh);
		
		tamX = (int) (0.5*w);
		tamY = (int) (0.45*h);
		hh = (int) (0.35*h);
		ww = hh;
//		rectDelete.set((int)(0.485*w - 2*ww), tamY-hh, (int)(0.485*w), tamY+hh);
		rectDelete.set((int)(0.715*w - 2*ww), tamY-hh, (int)(0.715*w), tamY+hh);
				
		lineX = (int) (0.5*w);
		lineTop = tamY-hh;
		lineBottom = tamY+hh;
		
		textS = (int) (0.35*h);
		textY = (int) (0.4*h + 0.5*textS);
		textX1 = (int) (1.2*h);
		textX2 = (int) (0.62*w);
		
		widthText = (int) (0.55*w);
    	
    	//-----------//
		float KH2 = 800*h/1080; 
		float KR2T = (float) (0.45*h - KH2/2.5);
		float KR2B = (float) (0.45*h + KH2/2.5);
		
		KS2 = (KR2B - KR2T)/3;

		KR3L = 1410*w/1920; 
    	KR3R = 1650*w/1920;
    	KR3T = KR2T;
    	KR3B = KR2B;

    	KR4L = 1660*w/1920; 
    	KR4R = 1900*w/1920;
    	KR4T = KR2T;
    	KR4B = KR2B; 	
    	
    	cirY = (int) (0.2*h);
		cirX = (int) (1600*w/1920);
		radius = (int) (0.14*h);
		
		numberS = (int) (0.15*h);
		numberX = cirX;
		numberY = (int) (0.2*h + 0.38*numberS);
		textNumber.setTextSize(numberS);
	}
	
    private int color_002;
    private int color_003;
    private int color_010;
    
    private Drawable drawKhung, drawKhungActive, drawKhungXam, drawable2;
    
    private TextPaint textNumber = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private int numberX, numberY, numberS;
	private int cirX, cirY, radius;
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);	
		
		color_002 = Color.argb(255, 1, 91, 141);
		color_003 = Color.argb(255 , 79 , 115 , 149);
		color_010 = Color.GREEN;
		
		drawBackgroud.setBounds(rectBackgroud);
		drawBackgroud.draw(canvas);
		
		paintMain.setStyle(Style.FILL);
		paintMain.setColor(color_002);
		canvas.drawRect(rectFrameSearch , paintMain);
		paintMain.setStyle(Style.STROKE);
		paintMain.setColor(color_003);
		paintMain.setStrokeWidth((float) 2.0);
		canvas.drawRect(rectFrameSearch , paintMain);
				
		textPaint.setTextSize(textS);
		drawSearch.setAlpha(255);
		drawDelete.setAlpha(255);
		drawSearch.setBounds(rectSearch);
		drawSearch.draw(canvas);
		drawDelete.setBounds(rectDelete);
		drawDelete.draw(canvas);
		if(textSearch.length() > 0){
			textPaint.setColor(Color.GREEN);
			canvas.drawText(textSearch.toString(), textX1, textY, textPaint);
		}else{
			textPaint.setARGB(255, 114, 195, 223);
			canvas.drawText(textHint, textX1, textY, textPaint);
		}		
		
		//--------------------//	
		drawable2 = drawKhung;	
		if(stateFilter == HIDDEN_HID){
			drawable2 = drawKhungActive;
		}
		drawable2.setBounds((int)KR3L, (int)KR3T, (int)KR3R, (int)KR3B);
		drawable2.draw(canvas);

		textPaint.setColor(Color.parseColor("#B4FEFF"));
		if(stateFilter == HIDDEN_HID){
			textPaint.setColor(Color.parseColor("#ffe002"));			
		}
		textPaint.setTextSize(KS2);
		textPaint.setStyle(Style.FILL);
		String str = getResources().getString(R.string.dialog_90xx_hid_6);
		float lengthText = textPaint.measureText(str);
		canvas.drawText(str, (KR3L + KR3R)/2 - lengthText/2 , (float) ((KR3T + KR3B)/2 + KS2/2.5) , textPaint);
		
		//--------------------//	
		drawable2 = drawKhung;	
		if(stateFilter == HIDDEN_ALL){
			drawable2 = drawKhungActive;
		}		
		drawable2.setBounds((int)KR4L, (int)KR4T, (int)KR4R, (int)KR4B);
		drawable2.draw(canvas);

		textPaint.setColor(Color.parseColor("#B4FEFF"));
		if(stateFilter == HIDDEN_ALL){
			textPaint.setColor(Color.parseColor("#ffe002"));			
		}
		textPaint.setTextSize(KS2);
		textPaint.setStyle(Style.FILL);
		str = getResources().getString(R.string.dialog_90xx_hid_7);
		lengthText = textPaint.measureText(str);
		canvas.drawText(str, (KR4L + KR4R)/2 - lengthText/2 , (float) ((KR4T + KR4B)/2 + KS2/2.5) , textPaint);
		
		//--------------------//	
		if (MyApplication.listHiddenSK90xx != null) {
			paintMain.setStyle(Style.FILL);
			textNumber.setStyle(Style.FILL);
			textNumber.setTextAlign(Align.CENTER);
			textNumber.setColor(Color.WHITE);
			textNumber.setTypeface(Typeface.DEFAULT_BOLD);

			paintMain.setColor(Color.parseColor("#6200ea"));
			canvas.drawCircle(cirX, cirY, radius, paintMain);

			canvas.drawText("" + MyApplication.listHiddenSK90xx.size(),
					numberX, numberY, textNumber);
		}
	}

	private boolean isActiveSearch = true;
	public void setAvtiveSearch(boolean isActiveSearch){
		this.isActiveSearch = isActiveSearch;
		invalidate();
	}
	
	
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			
		}break;
		case MotionEvent.ACTION_UP: {
			float x = event.getX();
			float y = event.getY();
			if(x >= KR3L && x <= KR3R && y >= KR3T && y <= KR3B){
				stateFilter = HIDDEN_HID;
				invalidate();
				if(listener != null){
					listener.OnChangeFilter(stateFilter);
				}
				return true;
			}
			if(x >= KR4L && x <= KR4R && y >= KR4T && y <= KR4B){
				stateFilter = HIDDEN_ALL;
				invalidate();
				if(listener != null){
					listener.OnChangeFilter(stateFilter);
				}
				return true;
			}
			if(x >= rectFrameSearch.left && x <= rectFrameSearch.right && 
				y >= rectFrameSearch.top && y <= rectFrameSearch.bottom){
				if(x >= rectDelete.left && x <= rectDelete.right && 
					y >= rectDelete.top && y <= rectDelete.bottom){
					clearAllChar();
					invalidate();
					return true;
				}
				isActiveSearch = true;
				invalidate();
				if(listener != null){
					listener.OnShowDelFrag();
					listener.OnShowKeyBoard();
				}
			}else{
				isActiveSearch = false;
				invalidate();
				if(listener != null){
					listener.OnShowAddFrag();
					listener.OnCloseKeyBoard();
				}
			}
		}break;
		default:
			break;
		}
    	return true;
    }
    
    public void clearOneChar(){
		if(textSearch != null && textSearch.length() > 0){
			this.textSearch.deleteCharAt(textSearch.length() - 1);
		}
		invalidate();
		if(listener != null && this.textSearch != null){
			listener.OnSearch(this.textSearch.toString());
		}
	}
	
	public void clearAllChar(){
		if(textSearch != null && textSearch.length() >= 0){
			this.textSearch = null;
			this.textSearch = new StringBuffer("");
		}
		invalidate();
		if(listener != null && this.textSearch != null){
			listener.OnSearch(this.textSearch.toString());
		}
	}
	
	public void addTextSearch(String key){
		if(isActiveSearch == true){
			String search = textSearch.toString() + key;
			textPaint.setTextSize(textS);
			float i = textPaint.measureText(search);
			if(i < widthText){
				textSearch.append(key);
				invalidate();
				if(listener != null){
					listener.OnSearch(textSearch.toString());
				}
			}
		}
	}
    
	public static final int HIDDEN_ALL = 0;
	public static final int HIDDEN_HID = 1;
	private int stateFilter = 0;
	public void setStateFilter(int stateFilter){
		this.stateFilter = stateFilter;
		invalidate();
	}
	
	public int getStateFilter(){
		return this.stateFilter;
	}

}
