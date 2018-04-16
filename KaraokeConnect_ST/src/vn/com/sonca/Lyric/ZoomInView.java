package vn.com.sonca.Lyric;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.util.AttributeSet;

public class ZoomInView extends ZoomBasicView {
	
	public ZoomInView(Context context) {
		super(context);
		initView(context);
	}

	public ZoomInView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public ZoomInView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private void initView(Context context) {}

	@Override
	protected int getIcon() {
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			return R.drawable.icon_zoomin;
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			return R.drawable.zlight_icon_zoomin;
		}
		return R.drawable.icon_zoomin;
	}

	@Override
	protected int getText() {
		return R.string.lyric_2;
	}

	@Override
	protected int getIconIN() {
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			return R.drawable.zoomin_inactive;
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			return R.drawable.zlight_icon_zoomin;
		}
		return R.drawable.zoomin_inactive;
	}

}
