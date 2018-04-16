package vn.com.sonca.Touch.CustomView;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.util.AttributeSet;

public class TouchMusicianTypeView extends TouchBaseTypeView {
	
	public TouchMusicianTypeView(Context context) {
		super(context);
	}

	public TouchMusicianTypeView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TouchMusicianTypeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected int getActiveIcon() {
		int result = R.drawable.touch_tab_musician_active_144x144;
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			result = R.drawable.touch_tab_musician_active_144x144;
		}else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			result = R.drawable.zlight_tab_musician_active_144x144;
		}	
		return result;
	}

	@Override
	protected int getInActiveIcon() {
		int result = R.drawable.touch_tab_musician_inactive_144x144;
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			result = R.drawable.touch_tab_musician_inactive_144x144;
		}else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			result = R.drawable.zlight_tab_musician_inactive_144x144;
		}	
		return result;
	}

	@Override
	protected int getHoverIcon() {
		int result = R.drawable.touch_tab_musician_hover_144x144;
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			result = R.drawable.touch_tab_musician_hover_144x144;
		}else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			result = R.drawable.zlight_tab_musician_hover_144x144;
		}	
		return result;
	}

	@Override
	protected String getTextView() {
		return getResources().getString(R.string.type_musician);
	}

	@Override
	protected String getTypeView() {
		return TouchMainActivity.MUSICIAN;
	}


}
