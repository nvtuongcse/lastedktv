package vn.com.sonca.Dialog.BlockVolumn;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.zzzzz.MyApplication;

public class BlockBackgroudVolumnGroup extends ViewGroup {

	public BlockBackgroudVolumnGroup(Context context) {
		super(context);
		initView(context);
	}

	public BlockBackgroudVolumnGroup(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public BlockBackgroudVolumnGroup(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Context context;
	public void initView(Context context){
		this.context = context;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int height = (int) (0.9*MeasureSpec.getSize(heightMeasureSpec));
		int width = (int) (1.4*height);
		setMeasuredDimension(width, height);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		// MyApplication.intColorScreen = MyApplication.SCREEN_BLUE;
	}

	@Override
	protected void onLayout(boolean arg0, int l, int t, int r, int b) {
		int width = getWidth();
		int height = getHeight();
		
		View backgroud = getChildAt(0);
		backgroud.layout(0, 0, width, height);
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			backgroud.setBackgroundResource(R.drawable.icon_boder_popup);
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			backgroud.setBackgroundResource(R.drawable.zlight_boder_popup);
		}
		
		int hh = (int) (0.95*height);
		int ww = (int) (1.5*hh);
		ViewGroup viewgroup = (ViewGroup)getChildAt(1);
		viewgroup.measure(
				MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), 
				MeasureSpec.makeMeasureSpec(hh, MeasureSpec.EXACTLY));
		viewgroup.layout(0, (int)(0.5*height - 0.5*hh), width, (int)(0.5*height + 0.5*hh)); 

		
		
	}
	
}
