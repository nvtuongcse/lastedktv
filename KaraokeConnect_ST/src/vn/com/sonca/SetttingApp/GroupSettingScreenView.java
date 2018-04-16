package vn.com.sonca.SetttingApp;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class GroupSettingScreenView extends FrameLayout {
	
	public GroupSettingScreenView(Context context) {
		super(context);
		initView(context);
	}
	
	public GroupSettingScreenView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	
	public GroupSettingScreenView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Context context;
	public void initView(Context context){
		this.context = context;
	}
	
	private int widthScreen;
	private int heightScreen;
	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
		widthScreen = getWidth();
		heightScreen = getHeight();
		
		View viewTop = getChildAt(0);
		viewTop.layout(0, 0, widthScreen, (int)(0.1*heightScreen));
		TextView textView = (TextView)getChildAt(2);
		textView.layout(widthScreen - 40, heightScreen - 40, widthScreen, heightScreen);
		textView.setTextSize(30);
		
		LinearLayout linearLayout = (LinearLayout)getChildAt(1);
		linearLayout.layout(0, (int)(0.1*heightScreen), (int)(0.3*widthScreen), heightScreen);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		
	}
	
	
	
	

}
