package vn.com.sonca.Touch.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class TouchViewLeftNo extends View {
	
	public TouchViewLeftNo(Context context) {
		super(context);
		initView(context);
	}

	public TouchViewLeftNo(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchViewLeftNo(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int myWidth = 700 * heightMeasureSpec / 1180;
		setMeasuredDimension(myWidth, heightMeasureSpec);
	}
	
	private void initView(Context context) {}

}
