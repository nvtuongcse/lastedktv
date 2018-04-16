package vn.com.sonca.zktv.Keyboard;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.Touch.Keyboard.OnClickKeyBoardListener;
import android.view.View.OnClickListener;

public class CharKeyBoardGroup extends ViewGroup implements OnClickListener {
	
	private final int MAX_COL = 4;
	private final int MAX_ROW = 7;
	private int HEIGHT_MARGIN = 0;
	private int WIDTH_MARGIN = 10;
	public static final String CLEAR = "@";
	
	private String[] NAMECODE1 = { 
			"A", "B", "C", "D", 
			"E", "F", "G", "H", 
			"I", "J", "K", "L", 
			"M", "N", "O", "P", 
			"Q", "R", "S", "T", 
			"U", "V", "W", "X",
			"Y", "Z"};
	
	public CharKeyBoardGroup(Context context) {
		super(context);
		initView(context);
	}

	public CharKeyBoardGroup(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public CharKeyBoardGroup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private String xoa = "";
	private void initView(Context context) {
		xoa = getResources().getString(R.string.xoa);
	}
	
	private OnClickKeyBoardListener clickListener;
	public void setOnKeyBoardClickListener(OnClickKeyBoardListener clickListener){
		this.clickListener = clickListener;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// MyApplication.intFocusFragment = MyApplication.FOCUS_VIEW;
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int width = (int) (0.6*height);
		setMeasuredDimension(width, height);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {		
		int width = getWidth();
		int height = getHeight();
		int widthLayout = width - WIDTH_MARGIN;
		int widthView = (int) (widthLayout / 4.1);
		int heightView = (int) (0.9905*widthView);
		int modWidth = (int) ((width - 4*widthView)/2);
		int modHeight = (int) ((height - 7*heightView)/2);

		left = left - (int) (0.1* widthLayout);
		
		int t = modHeight;
		int b = t + heightView;
		
		for (int i = 0; i < MAX_ROW; i++) {
			int l = left + modWidth;
			int r = l + widthView;
			for (int j = 0; j < MAX_COL; j++) {
				int id = i * MAX_COL + j;
				if(id >= NAMECODE1.length){
					break;
				}
				String namekey = NAMECODE1[id];
				View view = getChildAt(id);
				view.setId(id);
				view.setOnClickListener(this);
				if (namekey.equals(CLEAR)) {
					break;
				}else{
					((CharKeyView)view).setData(namekey);
					((CharKeyView)view).layout(l, t, r, b);
				}
				l += widthView;
				r += widthView;
			}
			t += heightView;
			b += heightView;
		}
		
		int l = left + modWidth;
		int r = (int) (l + 1.25*widthView);
		int position = NAMECODE1.length + 1;
		l += 2.0*widthView;
		r += 2.75*widthView;
		t -= heightView;
		b -= heightView;
		View viewClear = getChildAt(position);
		((ClearKeyView)viewClear).layout(l, t, r, b);
		((ClearKeyView)viewClear).setData(xoa);
		viewClear.setId(position);
		viewClear.setOnClickListener(this);
		
	}

	@Override
	public void onClick(View view) {
		if(view instanceof CharKeyView){
			String key = ((CharKeyView)view).getData();
			if(clickListener != null){
				if(key.equals(xoa)){
					clickListener.OnNameKey("@", view);
				}else{
					clickListener.OnNameKey(key, view);
				}
			}
		}else{
			if(clickListener != null){
				clickListener.OnNameKey("@", view);
			}
		}
	}
	
}
