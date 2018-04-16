package vn.com.sonca.Touch.CustomView;

import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.EditText;

public class GroupSearch extends ViewGroup{
	
	public GroupSearch(Context context) {
		super(context);
		initView(context);
	}
	
	public GroupSearch(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	
	public GroupSearch(Context context, AttributeSet attrs, int defStyle) {
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
		int myWidth = widthMeasureSpec;
		setMeasuredDimension(myWidth, heightMeasureSpec);
	}
	
	private int widthLayout = 0;
	private int heightLayout = 0;
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		widthLayout = getWidth();
		heightLayout = getHeight();
		
		setK(widthLayout, heightLayout);
		
		final TouchSearchView searchView = (TouchSearchView)getChildAt(0);
		searchView.layout(0, 0, widthLayout, heightLayout);

		EditText editSearch = (EditText)getChildAt(1);
		editSearch.setTextSize(pixelsToSp(context, 0.4f*heightLayout));
		editSearch.setGravity(Gravity.CENTER_VERTICAL);
		if(MyApplication.flagRealKeyboard){
			editSearch.layout((int)TouchSearchView.KTSearch, KR1T, KR1R, KR1B);
		} else {
			editSearch.layout(0,0,0,0);
		}
		
	}
	
	private int pixelsToSp(Context context, double d) {
	    float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
	    return (int) (d/scaledDensity);
	}
	
	int KR1L, KR1R, KR1T, KR1B;
	int KD22L, KD22T, KD22R, KD22B;
	
	private void setK(int w, int h){
		KR1L = 65 * w / 408;
		KR1R = 380 * w / 408;
		KR1T = 17 * h / 80;
		KR1B = 67 * h / 80;
		
		KD22L = 345 * w / 408;
		KD22R = (345 + 50) * w / 408;
		KD22T = 15 * h / 80;
		KD22B = (15 + 50) * h / 80;
		
		int tamX = (int) (0.652*widthLayout);
		if(TouchMainActivity.SAVETYPE == TouchMainActivity.YOUTUBE){
			tamX = (int) (0.685*widthLayout);
			if(MyApplication.flagYoutubeKaraokeOnly){
				tamX = (int) (0.95*widthLayout);
			}
		}
		if(MyApplication.flagSearchOnline){
			tamX = (int) (0.63*widthLayout);
		}
		int tamY = (int) (0.47*heightLayout);
		int hr = (int) (0.7*heightLayout);
		rect = new Rect(tamX - hr/2, tamY - hr/2, tamX + hr/2, tamY + hr/2);
		
		KR1R = rect.left;
		
	}
	
	public void callRequestLayout(){
		requestLayout();
	}

	
	private Rect rect = new Rect();
	
}
