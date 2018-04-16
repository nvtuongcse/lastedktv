package app.sonca.flower;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import app.sonca.flower.ViewGiveFlower.OnCheckGiveFlowerListener;
import android.view.View;
import android.view.View.MeasureSpec;

public class GroupGiveFlower extends ViewGroup implements OnCheckGiveFlowerListener {
	
	public GroupGiveFlower(Context context) {
		super(context);
		initView(context);
	}

	public GroupGiveFlower(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public GroupGiveFlower(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private void initView(Context context) {
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int width = 6*height/5;
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
		
		int hh = height/5;
		
		ViewGiveFlower flower0 = (ViewGiveFlower)getChildAt(0);
		flower0.measure(
				MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), 
				MeasureSpec.makeMeasureSpec(hh, MeasureSpec.EXACTLY));
		flower0.layout(0, 0*hh, width, 1*hh);
		
		ViewGiveFlower flower1 = (ViewGiveFlower)getChildAt(1);
		flower1.measure(
				MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), 
				MeasureSpec.makeMeasureSpec(hh, MeasureSpec.EXACTLY));
		flower1.layout(0, 1*hh, width, 2*hh);
		
		ViewGiveFlower flower2 = (ViewGiveFlower)getChildAt(2);
		flower2.measure(
				MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), 
				MeasureSpec.makeMeasureSpec(hh, MeasureSpec.EXACTLY));
		flower2.layout(0, 2*hh, width, 3*hh);

		ViewGiveFlower flower3 = (ViewGiveFlower)getChildAt(3);
		flower3.measure(
				MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), 
				MeasureSpec.makeMeasureSpec(hh, MeasureSpec.EXACTLY));
		flower3.layout(0, 3*hh, width, 4*hh);

		ViewGiveFlower flower4 = (ViewGiveFlower)getChildAt(4);
		flower4.measure(
				MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), 
				MeasureSpec.makeMeasureSpec(hh, MeasureSpec.EXACTLY));
		flower4.layout(0, 4*hh, width, 5*hh);
		
		flower0.setOnCheckGiveFlowerListener(this);
		flower1.setOnCheckGiveFlowerListener(this);
		flower2.setOnCheckGiveFlowerListener(this);
		flower3.setOnCheckGiveFlowerListener(this);
		flower4.setOnCheckGiveFlowerListener(this);
		
		flower0.setNumberFlowers(1);
		flower1.setNumberFlowers(2);
		flower2.setNumberFlowers(3);
		flower3.setNumberFlowers(4);
		flower4.setNumberFlowers(5);
		
		flower0.setActiveView(true);
		flower1.setActiveView(false);
		flower2.setActiveView(false);
		flower3.setActiveView(false);
		flower4.setActiveView(false);
		
		numberFlower = 1;
		
	}

	@Override
	public void OnCheck(View view) {
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			ViewGiveFlower flower = (ViewGiveFlower)getChildAt(i);
			int number = ((ViewGiveFlower)view).getNumberFlowers();
			if(flower.getNumberFlowers() == number){
				flower.setActiveView(true);
				numberFlower = number;
			}else{
				flower.setActiveView(false);
			}
		}
	}
	
	private int numberFlower = -1;
	public int getNumberFlowers(){
		return numberFlower;
	}


}
