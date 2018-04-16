package vn.com.sonca.Touch.CustomView;

import android.content.Context;
import android.util.AttributeSet;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.zzzzz.MyApplication;

public class TouchHotSongTypeView extends TouchBaseTypeView {
	
	public TouchHotSongTypeView(Context context) {
		super(context);
	}

	public TouchHotSongTypeView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TouchHotSongTypeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected int getActiveIcon() {
		int result = R.drawable.touch_tab_nhachot_active_144x144;
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			result = R.drawable.touch_tab_nhachot_active_144x144;
		}else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			result = R.drawable.zlight_tab_nhachot_active_144x144;
		}	
		return result;
	}

	@Override
	protected int getInActiveIcon() {
		int result = R.drawable.touch_tab_nhachot_inactive_144x144;
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			result = R.drawable.touch_tab_nhachot_inactive_144x144;
		}else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			result = R.drawable.zlight_tab_nhachot_inactive_144x144;
		}	
		return result;
	}

	@Override
	protected int getHoverIcon() {
		int result = R.drawable.touch_tab_nhachot_hover144x144;
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			result = R.drawable.touch_tab_nhachot_hover144x144;
		}else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			result = R.drawable.zlight_tab_nhachot_hover_144x144;
		}	
		return result;
	}

	@Override
	protected String getTextView() {
		return getResources().getString(R.string.type_hotsong);
	}

	@Override
	protected String getTypeView() {
		return TouchMainActivity.HOTSONG;
	}


}
