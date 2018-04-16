package vn.com.sonca.Touch.CustomView;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.util.AttributeSet;

public class TouchSingerTypeView extends TouchBaseTypeView {

	public TouchSingerTypeView(Context context) {
		super(context);
	}

	public TouchSingerTypeView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TouchSingerTypeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected int getActiveIcon() {
		int result = R.drawable.touch_tab_singer_active_144x144;
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			result = R.drawable.touch_tab_singer_active_144x144;
		}else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			result = R.drawable.zlight_tab_singer_active_144x144;
		}	
		return result;
	}

	@Override
	protected int getInActiveIcon() {
		int result = R.drawable.touch_tab_singer_inactive_144x144;
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			result = R.drawable.touch_tab_singer_inactive_144x144;
		}else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			result = R.drawable.zlight_tab_singer_inactive_144x144;
		}	
		return result;
	}

	@Override
	protected int getHoverIcon() {
		int result = R.drawable.touch_tab_singer_hover_144x144;
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			result = R.drawable.touch_tab_singer_hover_144x144;
		}else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			result = R.drawable.zlight_tab_singer_hover_144x144;
		}	
		return result;
	}


	@Override
	protected String getTextView() {
		return getResources().getString(R.string.type_singer);
	}

	@Override
	protected String getTypeView() {
		return TouchMainActivity.SINGER;
	}

}
