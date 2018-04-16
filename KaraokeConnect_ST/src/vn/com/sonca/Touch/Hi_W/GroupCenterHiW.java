package vn.com.sonca.Touch.Hi_W;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class GroupCenterHiW extends ViewGroup {

	public GroupCenterHiW(Context context) {
		super(context);
		initView(context);
	}
	
	public GroupCenterHiW(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	
	public GroupCenterHiW(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Context context;
	public void initView(Context context){
		this.context = context;
	}
	
	

	@Override
	protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
		int width = getWidth();
		int height = getHeight();
		
		View title = getChildAt(0);
		title.layout(0, 0, width, (int) (0.09*height));
		((MyTextView)title).setTitleView("Thong tin dau may");
		
		int hedit = (int) (0.17*height);
		
		View edit = getChildAt(1);
		edit.layout(0, (int) (0.09*height), width, (int) (0.09*height) + hedit);
		((MyEditText)edit).setTitleView("Mat khau Wifi (toi thieu 8 chu hoac so)");
		((MyEditText)edit).setDataView("SK8830KTV-W");
		((MyEditText)edit).setFocusable(true);
	}

}
