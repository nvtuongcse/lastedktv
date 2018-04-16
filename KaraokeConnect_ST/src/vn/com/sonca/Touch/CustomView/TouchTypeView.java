package vn.com.sonca.Touch.CustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class TouchTypeView extends LinearLayout {
	
	private Context context;

	public TouchTypeView(Context context) {
		super(context , null);
		this.context = context;
		initView();
	}
	
	public TouchTypeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initView();
	}
	
	public TouchTypeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		initView();
	}

	private void initView(){
  
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int parentHeight = MeasureSpec.getSize(heightMeasureSpec);
	    int myWidth = (int) (parentHeight);
	    super.onMeasure(MeasureSpec.makeMeasureSpec(myWidth, MeasureSpec.EXACTLY), heightMeasureSpec);
	}

}
