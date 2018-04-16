package vn.com.sonca.Touch.Keyboard;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.Touch.Keyboard.BaseKey.OnClickBaseKeyListener;
import vn.com.sonca.zzzzz.MyApplication;

public class GroupKeyBoardNumber extends ViewGroup implements OnClickBaseKeyListener{
	
	private int HEIGHT_MARGIN = 0;
	private int WIDTH_MARGIN = 10;
	
	private String[] NAMECODE2 = 
			{"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};
	
	public GroupKeyBoardNumber(Context context) {
		super(context);
		initView(context);
	}

	public GroupKeyBoardNumber(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public GroupKeyBoardNumber(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Drawable drawAC;
	private Drawable drawIN;
	
	private Drawable zlightdrawAC;
	private Drawable zlightdrawIN;
	
	public void initView(Context context){
		drawAC = getResources().getDrawable(R.drawable.image_boder_active_118x102);
		drawIN = getResources().getDrawable(R.drawable.image_boder_inactive_118x102);
		
		zlightdrawAC = getResources().getDrawable(R.drawable.zlight_keyboard_hover);
		zlightdrawIN = getResources().getDrawable(R.drawable.zlight_keyboard_active);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int myWidth = MeasureSpec.getSize(widthMeasureSpec);		
		int myHeight = (int) (9*myWidth/118 - 0.0001*myWidth);
		setMeasuredDimension(myWidth , myHeight);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
	}

// 118x90
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			int widthLayout = right - left - WIDTH_MARGIN;
			int widthView = widthLayout/10;
			int heightView = 90*widthView/118;
			int modWidth = WIDTH_MARGIN/2;
			int modHeight = HEIGHT_MARGIN/2;
				
			int t = top + modHeight;
			int b = t + heightView;
			int l = left + modWidth;
			int r = l + widthView;
				for (int j = 0; j < 10; j++) {
					String namekey = NAMECODE2[j];
					BaseKey view = (BaseKey)getChildAt(j);
					view.setOnClickBaseKeyListener(this);
					if(namekey.length() <= 3){
						view.setImage(drawAC, drawIN, namekey);
						view.layout(l, t, r, b);
					}
					l += widthView;
					r += widthView;
				}
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			int widthLayout = right - left - WIDTH_MARGIN;
			int widthView = widthLayout/10;
			int heightView = 90*widthView/118;
			int modWidth = WIDTH_MARGIN/2;
			int modHeight = HEIGHT_MARGIN/2;
				
			int t = top + modHeight;
			int b = t + heightView;
			int l = left + modWidth;
			int r = l + widthView;
				for (int j = 0; j < 10; j++) {
					String namekey = NAMECODE2[j];
					BaseKey view = (BaseKey)getChildAt(j);
					view.setOnClickBaseKeyListener(this);
					if(namekey.length() <= 3){
						view.setImage(zlightdrawAC, zlightdrawIN, namekey);
						view.layout(l, t, r, b);
					}
					l += widthView;
					r += widthView;
				}
		}
		
	}
	
	
	private OnClickKeyBoardListener listener;
	public void setOnClickKeyBoardListener(OnClickKeyBoardListener listener){
		this.listener = listener;
	}
	
	@Override
	public void OnNameKey(String namekey, View view) {
		if(listener != null){
			listener.OnNameKey(namekey, view);
		}
	}

	public void changeColorScreen(){
		for (int j = 0; j < 10; j++) {
			String namekey = NAMECODE2[j];
			BaseKey view = (BaseKey)getChildAt(j);
			if(namekey.length() <= 3){
				if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
					view.setImage(drawAC, drawIN, namekey);
				}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
					view.setImage(zlightdrawAC, zlightdrawIN, namekey);
				}
			}
		}
	}
}
