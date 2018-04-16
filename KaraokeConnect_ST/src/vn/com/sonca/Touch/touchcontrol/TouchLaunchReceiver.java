package vn.com.sonca.Touch.touchcontrol;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class TouchLaunchReceiver extends WakefulBroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
			Intent in = new Intent(context,
					vn.com.sonca.Touch.touchcontrol.SplashActivity.class);
			in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(in);
		}
	}

}
