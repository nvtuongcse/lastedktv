package vn.com.sonca.Touch.BlockCommand;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.View;
import android.view.View.MeasureSpec;

public class GroupScoreOnOff extends ViewGroup {
	
	public GroupScoreOnOff(Context context) {
		super(context);
		initView(context);
	}

	public GroupScoreOnOff(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public GroupScoreOnOff(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private void initView(Context context) {
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int myWidth = MeasureSpec.getSize(widthMeasureSpec);
		int myHeight = (int) (0.3*myWidth);
		setMeasuredDimension(myWidth , myHeight);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
		int width = getWidth();
		int height = getHeight();
		View view1 = getChildAt(0);
		view1.layout(0, 0, width, height);
		
		int tamY = (int) (0.5*height);
		int hr = (int) (0.4*height);
		int wr = hr;
		View view2 = getChildAt(1);
		view2.layout(5, tamY - hr, 2*wr + 5, tamY + hr);
	}

}
