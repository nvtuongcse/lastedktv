package vn.com.sonca.Touch.CustomView;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.util.AttributeSet;

public class YouTubeTypeView extends TouchBaseTypeView {
	
	public YouTubeTypeView(Context context) {
		super(context);
	}

	public YouTubeTypeView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public YouTubeTypeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected int getActiveIcon() {
		int result = R.drawable.tab_youtube_active_144x144;
		return result;
	}

	@Override
	protected int getInActiveIcon() {
		int result = R.drawable.tab_youtube_inactive_144x144;
		return result;			
	}

	@Override
	protected int getHoverIcon() {
		int result = R.drawable.tab_youtube_hover_144x144;
		return result;
	}

	@Override
	protected String getTextView() {
		return getResources().getString(R.string.type_youtube);
	}

	@Override
	protected String getTypeView() {
		return TouchMainActivity.YOUTUBE;
	}


}
