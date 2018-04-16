package vn.com.sonca.Touch.CustomView;

import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;

public class TouchViewNoThing extends View {
	
	public TouchViewNoThing(Context context) {
		super(context);
		initView(context);
	}

	public TouchViewNoThing(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchViewNoThing(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private void initView(Context context) {}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	    int myHeight = (int) (0.6188*getResources().getDisplayMetrics().heightPixels/5);
	    if(MyApplication.flagHong){
	    	myHeight = (int) (0.6188*getResources().getDisplayMetrics().heightPixels/6);
	    }
	    setMeasuredDimension(widthMeasureSpec, MeasureSpec.makeMeasureSpec(myHeight, MeasureSpec.UNSPECIFIED));
	}
	
	
}
