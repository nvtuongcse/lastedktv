package vn.com.sonca.Lyric;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.util.AttributeSet;

public class ZoomOutView extends ZoomBasicView {
	
	public ZoomOutView(Context context) {
		super(context);
		initView(context);
	}

	public ZoomOutView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public ZoomOutView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private void initView(Context context) {}

	@Override
	protected int getIcon() {
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			return R.drawable.icon_zoomout;
		} else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			return R.drawable.zlight_icon_zoomout;
		}
		return R.drawable.icon_zoomout;
	}

	@Override
	protected int getText() {
		return R.string.lyric_3;
	}

	@Override
	protected int getIconIN() {
		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			return R.drawable.zoomout_iactive;
		} else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			return R.drawable.zlight_icon_zoomout;
		}
		return R.drawable.zoomout_iactive;
	}

}
