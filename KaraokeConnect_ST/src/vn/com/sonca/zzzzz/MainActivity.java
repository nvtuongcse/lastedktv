package vn.com.sonca.zzzzz;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Intent intent = new Intent(MainActivity.this, TouchMainActivity.class);
		startActivityForResult(intent, 101);
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 101 && resultCode == RESULT_OK){
			String exit = data.getExtras().getString("ExitApp");
			if(exit != null && exit.equals("ExitApp")){
				finish();
			}
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		((MyApplication) this.getApplication()).onDestroy();
		((MyApplication)getApplication()).disconnectFromRemoteHost();
		System.exit(0);
	}


}
