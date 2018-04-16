package app.sonca.flower;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

public class GroupAvatar extends ViewGroup implements OnClickListener{
	
	public GroupAvatar(Context context) {
		super(context);
		initView(context);
	}

	public GroupAvatar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public GroupAvatar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private void initView(Context context) {
		
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int width = 4*height;
		setMeasuredDimension(width, height);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int width = getWidth();
		int height = getHeight();
		
		int tamY = (int) (0.5*height);
		int hh = (int) (0.4*height);
		int ww = hh;
		
		int tamX = (int) (0.5*height);
		ViewAvatar view0 = (ViewAvatar)getChildAt(0);
		view0.layout(tamX-ww, tamY-hh, tamX+ww, tamY+hh);
		tamX = (int) (1.5*height);
		ViewAvatar view1 = (ViewAvatar)getChildAt(1);
		view1.layout(tamX-ww, tamY-hh, tamX+ww, tamY+hh);
		tamX = (int) (2.5*height);
		ViewAvatar view2 = (ViewAvatar)getChildAt(2);
		view2.layout(tamX-ww, tamY-hh, tamX+ww, tamY+hh);
		tamX = (int) (3.5*height);
		ViewAvatar view3 = (ViewAvatar)getChildAt(3);
		view3.layout(tamX-ww, tamY-hh, tamX+ww, tamY+hh);
		
		view0.setOnClickListener(this);
		view1.setOnClickListener(this);
		view2.setOnClickListener(this);
		view3.setOnClickListener(this);
		
		view0.setPosition(0);
		view1.setPosition(1);
		view2.setPosition(2);
		view3.setPosition(3);
		
		view0.setAvatar(0);
		view1.setAvatar(1);
		view2.setAvatar(2);
		view3.setAvatar(3);
		
		view0.setActiveView(true);
		view1.setActiveView(false);
		view2.setActiveView(false);
		view3.setActiveView(false);
		
		intAvatar = 0;
		
	}

	@Override
	public void onClick(View view) {
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			ViewAvatar v = (ViewAvatar)getChildAt(i);
			int avatar = ((ViewAvatar)view).getPosition();
			if(v.getPosition() == avatar){
				v.setActiveView(true);
				intAvatar = avatar;
			}else{
				v.setActiveView(false);
			}
		}
	}
	
	private int intAvatar = -1;
	public int getAvatar(){
		return intAvatar;
	}
	

}
