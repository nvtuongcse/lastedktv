package vn.com.sonca.Touch.CustomView;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.util.AttributeSet;

public class TouchLangagueTypeView extends TouchBaseTypeView {
	
	public TouchLangagueTypeView(Context context) {
		super(context);
	}

	public TouchLangagueTypeView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public TouchLangagueTypeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected int getActiveIcon() {
		int result = R.drawable.touch_tab_lang_active_144x144;
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			result = R.drawable.touch_tab_lang_active_144x144;
		}else if (MyApplication.intColorScreen == MyApplication.SCREEN_GREEN) {
			result = R.drawable.zgreen_tab_language_active_144x144;
		}	
		return result;
	}

	@Override
	protected int getInActiveIcon() {
		int result = R.drawable.touch_tab_lang_inactive_144x144;
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			result = R.drawable.touch_tab_lang_inactive_144x144;
		}else if (MyApplication.intColorScreen == MyApplication.SCREEN_GREEN) {
			result = R.drawable.zgreen_tab_language_inactive_144x144;
		}	
		return result;
	}

	@Override
	protected int getHoverIcon() {
			return R.drawable.touch_tab_lang_hover_144x144;
	}

	@Override
	protected String getTextView() {
		return getResources().getString(R.string.type_langague);
	}

	@Override
	protected String getTypeView() {
		return TouchMainActivity.LANGUAGE;
	}


}
