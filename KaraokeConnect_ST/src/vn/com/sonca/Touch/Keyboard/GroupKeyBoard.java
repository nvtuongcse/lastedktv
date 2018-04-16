package vn.com.sonca.Touch.Keyboard;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.Keyboard.BaseKey.OnClickBaseKeyListener;
import vn.com.sonca.zzzzz.MyApplication;

public class GroupKeyBoard extends ViewGroup implements OnClickBaseKeyListener{
	
	private final String TAB = "GroupKeyBoard";
	public static final String CLEAR = "CLEAR";
	public static final String SPACE = "SPACE";
	
	private final int MAX_COL = 10;
	private int MAX_ROW = 3;
	
	private int HEIGHT_MARGIN = 0;
	private int WIDTH_MARGIN = 10;
		
	private String[] NAMECODE1 = { "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "A", "S", "D", "F", "G", "H", "J",
			"K", "L", CLEAR, "123", "Z", "X", "C", "V", "B", "N", "M", SPACE, "OK" };
	
	public GroupKeyBoard(Context context) {
		super(context);
		initView(context);
	}

	public GroupKeyBoard(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public GroupKeyBoard(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Drawable drawAC;
	private Drawable drawIN;
	private Drawable drawClearAC;
	private Drawable drawClearIN;
	private Drawable drawSpaceAC;
	private Drawable drawSpaceIN;
	
	private Drawable zlightdrawAC;
	private Drawable zlightdrawIN;
	private Drawable zlightdrawClearAC;
	private Drawable zlightdrawClearIN;
	private Drawable zlightdrawSpaceAC;
	private Drawable zlightdrawSpaceIN;

	public void initView(Context context) {
		drawClearAC = getResources().getDrawable(R.drawable.touch_xoa_hover_118x90_new);
		drawClearIN = getResources().getDrawable(R.drawable.touch_xoa_active_118x90_new);
		drawSpaceAC = getResources().getDrawable(R.drawable.touch_space_118x90_hover_1);
		drawSpaceIN = getResources().getDrawable(R.drawable.touch_space_118x90_active_1);
		drawAC = getResources().getDrawable(R.drawable.image_boder_active_118x102);
		drawIN = getResources().getDrawable(R.drawable.image_boder_inactive_118x102);
		
		zlightdrawClearAC = getResources().getDrawable(R.drawable.zlight_keyboard_xoa_hover);
		zlightdrawClearIN = getResources().getDrawable(R.drawable.zlight_keyboard_xoa);
		zlightdrawSpaceAC = getResources().getDrawable(R.drawable.zlight_keyboard_space_hover);
		zlightdrawSpaceIN = getResources().getDrawable(R.drawable.zlight_keyboard_space);
		zlightdrawAC = getResources().getDrawable(R.drawable.zlight_keyboard_hover);
		zlightdrawIN = getResources().getDrawable(R.drawable.zlight_keyboard_active);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int myWidth = MeasureSpec.getSize(widthMeasureSpec);
		int myHeight = 27*myWidth/118;
		setMeasuredDimension(myWidth , myHeight);
		
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
	}

// 118x90
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		// MyApplication.intColorScreen = MyApplication.SCREEN_LIGHT;
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			int widthLayout = right - left - WIDTH_MARGIN;
			int widthView = widthLayout / 10;
			int heightView = 90 * widthView / 118;
			int modWidth = WIDTH_MARGIN / 2;
			int modHeight = HEIGHT_MARGIN / 2;

			int t = 0;
			int b = t + heightView;
			for (int i = 0; i < MAX_ROW; i++) {
				int l = left + modWidth;
				int r = l + widthView;
				for (int j = 0; j < MAX_COL; j++) {
					int id = i * MAX_COL + j;
					String namekey = NAMECODE1[id];
					BaseKey view = (BaseKey) getChildAt(id);
					view.setOnClickBaseKeyListener(this);
					if (namekey.length() <= 3) {
						view.setImage(drawAC, drawIN, namekey);
						view.layout(l, t, r, b);
						if (namekey.equals("123")) {
							key123 = view;
						}
					} else if (namekey.equals(CLEAR)) {
						view.setImage(drawClearAC, drawClearIN, namekey);
						view.layout(l, t, r, b);
					} else if (namekey.equals(SPACE)) {
						view.setImage(drawSpaceAC, drawSpaceIN, namekey);
						view.layout(l, t, r, b);
					}
					l += widthView;
					r += widthView;
				}
				t += heightView;
				b += heightView;
			}

		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			int widthLayout = right - left - WIDTH_MARGIN;
			int widthView = widthLayout / 10;
			int heightView = 90 * widthView / 118;
			int modWidth = WIDTH_MARGIN / 2;
			int modHeight = HEIGHT_MARGIN / 2;

			int t = 0;
			int b = t + heightView;
			for (int i = 0; i < MAX_ROW; i++) {
				int l = left + modWidth;
				int r = l + widthView;
				for (int j = 0; j < MAX_COL; j++) {
					int id = i * MAX_COL + j;
					String namekey = NAMECODE1[id];
					BaseKey view = (BaseKey) getChildAt(id);
					view.setOnClickBaseKeyListener(this);
					if (namekey.length() <= 3) {
						view.setImage(zlightdrawAC, zlightdrawIN, namekey);
						view.layout(l, t, r, b);
						if (namekey.equals("123")) {
							key123 = view;
						}
					} else if (namekey.equals(CLEAR)) {
						view.setImage(zlightdrawClearAC, zlightdrawClearIN,
								namekey);
						view.layout(l, t, r, b);
					} else if (namekey.equals(SPACE)) {
						view.setImage(zlightdrawSpaceAC, zlightdrawSpaceIN,
								namekey);
						view.layout(l, t, r, b);
					}
					l += widthView;
					r += widthView;
				}
				t += heightView;
				b += heightView;
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
	
	private BaseKey key123;
	public void setKey123(){
		if(key123 != null){
			key123.setLayoutView(View.GONE);
		}
	}
	
	public void changeColorScreen(){
		for (int i = 0; i < MAX_ROW; i++) {
			for (int j = 0; j < MAX_COL; j++) {
				int id = i*MAX_COL + j;
				String namekey = NAMECODE1[id];
				BaseKey view = (BaseKey)getChildAt(id);
				if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
					if(namekey.length() <= 3){
						view.setImage(drawAC, drawIN, namekey);
						if(namekey.equals("123")){
							key123 = view;
						}
					} else if (namekey.equals(CLEAR)){
						view.setImage(drawClearAC, drawClearIN, namekey);
					} else if (namekey.equals(SPACE)){
						view.setImage(drawSpaceAC, drawSpaceIN, namekey);
					}
				}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
					if(namekey.length() <= 3){
						view.setImage(zlightdrawAC, zlightdrawIN, namekey);
						if(namekey.equals("123")){
							key123 = view;
						}
					} else if (namekey.equals(CLEAR)){
						view.setImage(zlightdrawClearAC, zlightdrawClearIN, namekey);
					} else if (namekey.equals(SPACE)){
						view.setImage(zlightdrawSpaceAC, zlightdrawSpaceIN, namekey);
					}
				}
			}
		}
	}
}
