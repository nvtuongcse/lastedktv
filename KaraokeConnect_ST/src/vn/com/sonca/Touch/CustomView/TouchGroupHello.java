package vn.com.sonca.Touch.CustomView;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

public class TouchGroupHello extends ViewGroup{
	
	public TouchGroupHello(Context context) {
		super(context);
		initView(context);
	}
	
	public TouchGroupHello(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	
	public TouchGroupHello(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Context context;
	public void initView(Context context){
		this.context = context;
	}
	
	private OnShowKeybroadListener listener;
	public interface OnShowKeybroadListener{
		public void ShowKeybroad();
	}
	
	public void setOnShowKeybroadListener(OnShowKeybroadListener listener){
		this.listener = listener;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int myWidth = 700 * heightMeasureSpec / 1180;
		setMeasuredDimension(myWidth, heightMeasureSpec);
	}
	
	private boolean isdong1;
	private boolean isdong2;
	private int widthLayout = 0;
	private int heightLayout = 0;
	private EditText edit1;
	private EditText edit2;
	private ImageView imagedong1;
	private ImageView imagedong2;
	private TouchHelloView touchHelloView;
	
	private int color_01;
	private int color_02;
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		widthLayout = getWidth();
		heightLayout = getHeight();
		
		touchHelloView = (TouchHelloView)getChildAt(0);
		touchHelloView.layout(0, 0, widthLayout, heightLayout);
		
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			color_01 = Color.GREEN;
			color_02 = Color.GRAY;
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			color_01 = Color.parseColor("#21BAA9");
			color_02 = Color.GRAY;
		}
		
		int ty = (int) (0.27 * heightLayout);
		int hr = (int) (0.051*	widthLayout);
		int wr = (int) (0.02 * widthLayout + 20);
		edit1 = (EditText)getChildAt(1);
		edit1.layout(wr, ty - hr, (int) (0.89*widthLayout - wr), ty + hr);
		edit1.setTextSize(pixelsToSp(context, 1.2*hr));
		edit1.setGravity(Gravity.CENTER_VERTICAL);
		
		edit1.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override public void onFocusChange(View view, boolean hasFocus) {
				if(hasFocus){
					edit1.setTextColor(color_01);
					touchHelloView.setDongFocus(TouchHelloView.DONG_1);
					if(listener != null){
						listener.ShowKeybroad();
					}
				}else{
					edit1.setTextColor(color_02);
				}
			}
		});
		edit1.addTextChangedListener(new TextWatcher() {
			@Override public void onTextChanged(CharSequence sequence, int arg1, int arg2, int arg3) {
				isdong1 = !sequence.toString().equals("");
				touchHelloView.setEnableClickChange(isdong1, isdong2);
				if (!isdong1) {
					imagedong1.setVisibility(View.INVISIBLE);
				} else {
					imagedong1.setVisibility(View.VISIBLE);
				}
			}
			@Override public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
			@Override public void afterTextChanged(Editable arg0) {}
		});
		
		ty = (int) (0.39 * heightLayout);
		edit2 = (EditText)getChildAt(2);
		edit2.layout(wr, ty - hr, (int) (0.89*widthLayout - wr), ty + hr);
		edit2.setTextSize(pixelsToSp(context, 1.2*hr));
		edit2.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					edit2.setTextColor(color_01);
					touchHelloView.setDongFocus(TouchHelloView.DONG_2);
					if(listener != null){
						listener.ShowKeybroad();
					}
				}else{
					edit2.setTextColor(color_02);
				}
			}
		});
		edit2.addTextChangedListener(new TextWatcher() {
			@Override public void onTextChanged(CharSequence sequence, int arg1, int arg2, int arg3) {
				isdong2 = !sequence.toString().equals("");
				touchHelloView.setEnableClickChange(isdong1, isdong2);
				if (!isdong2) {
					imagedong2.setVisibility(View.INVISIBLE);
				} else {
					imagedong2.setVisibility(View.VISIBLE);
				}
			}
			@Override public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
			@Override public void afterTextChanged(Editable arg0) {}
		});
		
		
		isdong1 = !edit1.getText().toString().equals("");
		isdong2 = !edit2.getText().toString().equals("");
		touchHelloView.setEnableClickChange(isdong1, isdong2);
		
		int tx = (int) (0.9*widthLayout);
		ty = (int) (0.265*heightLayout);
		hr = (int) (0.06*widthLayout);
		wr = hr ;
		imagedong1 = (ImageView)getChildAt(3);
		imagedong1.layout(tx - wr, ty - hr, tx + wr, ty + hr);
		imagedong1.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View arg0) {
				edit1.setTextColor(color_01);
				edit1.requestFocus();
				edit1.setText("");
				if(listener != null){
					listener.ShowKeybroad();
				}
			}
		});
		ty = (int) (0.385*heightLayout);
		imagedong2 = (ImageView)getChildAt(4);
		imagedong2.layout(tx - wr, ty - hr, tx + wr, ty + hr);
		imagedong2.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View arg0) {
				edit2.setTextColor(color_01);
				edit2.requestFocus();
				edit2.setText("");
				if(listener != null){
					listener.ShowKeybroad();
				}
			}
		});
		
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			imagedong1.setBackgroundResource(R.drawable.del_icon_72x72);
			imagedong2.setBackgroundResource(R.drawable.del_icon_72x72);
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			imagedong1.setBackgroundResource(R.drawable.zlight_del_icon_72x72);
			imagedong2.setBackgroundResource(R.drawable.zlight_del_icon_72x72);
		}
		
	}
	
	private int pixelsToSp(Context context, double d) {
	    float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
	    return (int) (d/scaledDensity);
	}
	
	public void clearFocusView(){
		touchHelloView.setDongFocus(0);
		edit1.clearFocus();
		edit2.clearFocus();
	}
	
	public void updateChildView(){
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			imagedong1.setBackgroundResource(R.drawable.del_icon_72x72);
			imagedong2.setBackgroundResource(R.drawable.del_icon_72x72);
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			imagedong1.setBackgroundResource(R.drawable.zlight_del_icon_72x72);
			imagedong2.setBackgroundResource(R.drawable.zlight_del_icon_72x72);
		}
	}

}
