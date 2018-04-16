package vn.com.sonca.Dialog.BlockMelody;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.TextView;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.Dialog.BlockVolumn.VolumnTextD;
import vn.com.sonca.zzzzz.MyApplication;

public class BlockMelodyGroup extends ViewGroup {
	
	public BlockMelodyGroup(Context context) {
		super(context);
		initView(context);
	}

	public BlockMelodyGroup(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public BlockMelodyGroup(Context context, AttributeSet attrs, int defStyle) {
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
		int height = (int) (1.0*MeasureSpec.getSize(heightMeasureSpec));
		int width = (int) (1.5*height);
		setMeasuredDimension(width, height);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		
	}

	@Override
	protected void onLayout(boolean arg0, int l, int t, int r, int b) {
		int width = getWidth();
		int height = getHeight();
		
		int yline = (int) (0.3*height);
		View line = getChildAt(1);
		line.layout(0, yline, width, yline + 4);
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			line.setBackgroundResource(R.drawable.touch_shape_line_hor);
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			line.setBackgroundResource(R.drawable.zlight_shape_line_hor);
		}
		
		TextView title = (TextView)getChildAt(2);
		int textSize = (int) MyApplication.convertPixelsToSp(context, 0.12f*height);
		title.measure(
				MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), 
				MeasureSpec.makeMeasureSpec((int)(0.2*height), MeasureSpec.EXACTLY));
		title.layout(0, (int)(0.1*height), width, (int)(0.3*height));
		title.setTextSize(textSize);
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			int color = Color.argb(255, 180, 254, 255);
			title.setTextColor(color);
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			int color = Color.argb(255, 33, 186, 169);
			title.setTextColor(color);
		}
		
		int left = (int) (0.1*width);
		int right = (int) (0.9*width);
		int top = (int) (0.38*height);
		int bottom = (int) (0.68*height);
		View backgroud = getChildAt(0);
		backgroud.layout(left, top, right, bottom);
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			Drawable color = getResources().getDrawable(R.drawable.shape_block_volumn_view);
			backgroud.setBackgroundDrawable(color);
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			int color = Color.argb(255, 33, 186, 169);
			backgroud.setBackgroundColor(color);
		}
		
		left = (int) (0.1*width);
		right = (int) (0.38*width);
		top = (int) (0.4*height);
		bottom = (int) (0.66*height);
		textSize = (int) MyApplication.convertPixelsToSp(context, 0.04f*height);
		VolumnTextD textM = (VolumnTextD)getChildAt(3);
		textM.layout(left, top, right, bottom);
		textM.setTextView(
				context.getString(R.string.dialog_melody_block_2), 
				context.getString(R.string.dialog_melody_block_3), 
				context.getString(R.string.dialog_melody_block_4));
		
		View melody = getChildAt(4);
		melody.layout(
				(int)(0.41*width), top, 
				(int)(0.9*width), bottom);
		
		top = (int) (0.72*height);
		bottom = (int) (0.9*height);
		int hh = (int) (bottom - top);
		int ww = (int) (2.5*hh);
		View button = getChildAt(5);
		button.layout(
			(int)(0.5*width - 0.5*ww), top, 
			(int)(0.5*width + 0.5*ww), bottom);
		
	}
	


}
