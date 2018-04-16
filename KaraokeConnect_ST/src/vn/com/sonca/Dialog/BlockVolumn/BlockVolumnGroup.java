package vn.com.sonca.Dialog.BlockVolumn;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.Dialog.BlockVolumn.VolumnTextD;
import vn.com.sonca.zzzzz.MyApplication;

public class BlockVolumnGroup extends ViewGroup {
	
	public BlockVolumnGroup(Context context) {
		super(context);
		initView(context);
	}

	public BlockVolumnGroup(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public BlockVolumnGroup(Context context, AttributeSet attrs, int defStyle) {
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
		
		View backgroud = getChildAt(0);
		backgroud.layout(0, 0, width, height);
		
		int yline = (int) (0.15*height);
		View line = getChildAt(1);
		line.layout(0, yline, width, yline + 4);
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			line.setBackgroundResource(R.drawable.touch_shape_line_hor);
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			line.setBackgroundResource(R.drawable.zlight_shape_line_hor);
		}
		
		//-------------------//
		
		int padding = (int) (0.1*width);
		int left = (int) (0.05*width);
		int right = (int) (0.95*width);
		
		int top = (int) (0.19*height);
		int bottom = (int) (0.576*height);
		View view0 = getChildAt(2);
		view0.layout(left, top, right, bottom);
		
		top = (int) (0.596*height);
		bottom = (int) (0.708*height);
		View view1= getChildAt(3);
		view1.layout(left, top, right, bottom);
		
		top = (int) (0.728*height);
		bottom = (int) (0.84*height);
		View view2 = getChildAt(4);
		view2.layout(left, top, right, bottom);
		
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			Drawable color = getResources().getDrawable(R.drawable.shape_block_volumn_view);
			view0.setBackgroundDrawable(color);
			view1.setBackgroundDrawable(color);
			view2.setBackgroundDrawable(color);
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			int color = Color.argb(255, 33, 186, 169);
			view0.setBackgroundColor(color);
			view1.setBackgroundColor(color);
			view2.setBackgroundColor(color);
		}
		
		//-------------------//

		TextView title = (TextView)getChildAt(5);
		int textSize = (int) MyApplication.convertPixelsToSp(context, 0.05f*height);
		title.measure(
				MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), 
				MeasureSpec.makeMeasureSpec((int)(0.15*height), MeasureSpec.EXACTLY));
		title.layout(0, (int)(0.00*height), width, (int)(0.15*height));
		title.setTextSize(textSize);
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			int color = Color.argb(255, 180, 254, 255);
			title.setTextColor(color);
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			int color = Color.argb(255, 33, 186, 169);
			title.setTextColor(color);
		}
		
		left = (int) (0.1*width);
		right = (int) (0.35*width);
		top = (int) (0.319*height);
		bottom = (int) (0.432*height);
		textSize = (int) MyApplication.convertPixelsToSp(context, 0.04f*height);
		VolumnTextD MIDI = (VolumnTextD)getChildAt(6);
		MIDI.layout(left, top, right, bottom);
		MIDI.setTextView(
				context.getString(R.string.dialog_volumn_block_midi_0), 
				context.getString(R.string.dialog_volumn_block_midi_1), 
				"");
		
		top = (int) (0.452*height);
		bottom = (int) (0.566*height);
		VolumnTextD KTV = (VolumnTextD)getChildAt(7);
		KTV.layout(left, top, right, bottom);
		KTV.setTextView(
				context.getString(R.string.dialog_volumn_block_ktv_0), 
				context.getString(R.string.dialog_volumn_block_ktv_1), 
				"");
		
		top = (int) (0.20*height);
		bottom = (int) (0.309*height);
		VolumnTextD MASTER = (VolumnTextD)getChildAt(8);
		MASTER.layout(left, top, right, bottom);
		MASTER.setTextView(
				context.getString(R.string.dialog_volumn_block_master_0), 
				context.getString(R.string.dialog_volumn_block_master_1), 
				"");
		
		top = (int) (0.596*height);
		bottom = (int) (0.708*height);
		VolumnTextD DANCE = (VolumnTextD)getChildAt(9);
		DANCE.layout(left, top, right, bottom);
		DANCE.setTextView(
				context.getString(R.string.dialog_volumn_block_dance_0), 
				context.getString(R.string.dialog_volumn_block_dance_1), 
				context.getString(R.string.dialog_volumn_block_dance_2));
		
		top = (int) (0.728*height);
		bottom = (int) (0.84*height);
		VolumnTextD PIANO = (VolumnTextD)getChildAt(10);
		PIANO.layout(left, top, right, bottom);
		PIANO.setTextView(
				context.getString(R.string.dialog_volumn_block_piano_0), 
				context.getString(R.string.dialog_volumn_block_piano_1), 
				context.getString(R.string.dialog_volumn_block_piano_2));
		
		//-------------------//
		
		left = (int) (0.37*width);
		right = (int) (0.9*width);
		top = (int) (0.319*height);
		bottom = (int) (0.432*height);
		View volumnMIDI = getChildAt(11);
		volumnMIDI.layout(
				(int)(0.45*width), top, 
				(int)(0.8*width), bottom);
		
		top = (int) (0.452*height);
		bottom = (int) (0.566*height);
		View volumnKTV = getChildAt(12);
		volumnKTV.layout(
				(int)(0.45*width), top, 
				(int)(0.8*width), bottom);
		
		top = (int) (0.20*height);
		bottom = (int) (0.309*height);
		View volumnMASTER = getChildAt(13);
		volumnMASTER.layout(left, top, right, bottom);
		
		top = (int) (0.596*height);
		bottom = (int) (0.708*height);
		View volumnDANCE = getChildAt(14);
		volumnDANCE.layout(left, top, right, bottom);
		
		top = (int) (0.728*height);
		bottom = (int) (0.84*height);
		View volumnPIANO = getChildAt(15);
		volumnPIANO.layout(left, top, right, bottom);
		
		//-------------------//
		
		top = (int) (0.87*height);
		bottom = (int) (0.97*height);
		int hh = (int) (bottom - top);
		int ww = (int) (2.5*hh);
		View button = getChildAt(16);
		button.layout(
			(int)(0.5*width - 0.5*ww), top, 
			(int)(0.5*width + 0.5*ww), bottom);
		
	}
	


}
