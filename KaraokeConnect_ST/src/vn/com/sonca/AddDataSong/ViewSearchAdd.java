package vn.com.sonca.AddDataSong;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;

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
		textHint = getResources().getString(R.string.dialog_add_song_1);
		textAdd = getResources().getString(R.string.dialog_add_song_2);
	}
	
	private OnSearchAddListener listener;
	public interface OnSearchAddListener {
		public void OnSearch(String search);
		public void OnShowKeyBoard();
		public void OnCloseKeyBoard();
		public void OnShowAddFrag();
		public void OnShowDelFrag();
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
		rectFrameSearch.set((int)(1.0*h), tamY-hh, (int)(0.49*w), tamY+hh);
		
		tamX = (int) (0.5*w);
		tamY = (int) (0.45*h);
		hh = (int) (0.35*h);
		ww = hh;
		rectDelete.set((int)(0.485*w - 2*ww), tamY-hh, (int)(0.485*w), tamY+hh);
		
		lineX = (int) (0.5*w);
		lineTop = tamY-hh;
		lineBottom = tamY+hh;
		
		textS = (int) (0.35*h);
		textY = (int) (0.4*h + 0.5*textS);
		textX1 = (int) (1.2*h);
		textX2 = (int) (0.62*w);
		
		widthText = (int) (0.5*w);
		
	}
	
    private int color_002;
    private int color_003;
    private int color_010;
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
		
		paintMain.setColor(Color.parseColor("#B4FEFF"));
		canvas.drawLine(lineX, lineTop, lineX, lineBottom, paintMain);
		
		
		textPaint.setTextSize(textS);
		if(isActiveSearch == true){
			drawSearch.setAlpha(255);
			drawDelete.setAlpha(255);
			drawSearch.setBounds(rectSearch);
			drawSearch.draw(canvas);
			drawDelete.setBounds(rectDelete);
			drawDelete.draw(canvas);
			textPaint.setColor(Color.parseColor("#00FDFD"));
			if(textSearch.length() > 0){
				canvas.drawText(textSearch.toString(), textX1, textY, textPaint);
			}else{
				canvas.drawText(textHint, textX1, textY, textPaint);
			}
			textPaint.setColor(Color.parseColor("#72C3DF"));
			canvas.drawText(textAdd, textX2, textY, textPaint);
		}else{
			drawSearch.setAlpha(100);
			drawDelete.setAlpha(100);
			drawSearch.setBounds(rectSearch);
			drawSearch.draw(canvas);
			drawDelete.setBounds(rectDelete);
			drawDelete.draw(canvas);
			textPaint.setColor(Color.parseColor("#72C3DF"));
			canvas.drawText(textHint, textX1, textY, textPaint);
			textPaint.setColor(Color.parseColor("#00FDFD"));
			canvas.drawText(textAdd, textX2, textY, textPaint);
		}
		
		
		MyLog.e("ViewSearchAdd", "onDraw");

		
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
    

}
