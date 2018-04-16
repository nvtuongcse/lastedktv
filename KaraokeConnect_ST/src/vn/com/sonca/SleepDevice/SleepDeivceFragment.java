package vn.com.sonca.SleepDevice;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.SleepDevice.TopSleepView.OnTopSleepListener;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity.OnListDeviceListener;
import vn.com.sonca.utils.AppSettings;
import vn.com.sonca.zzzzz.MyApplication;

public class SleepDeivceFragment extends Fragment implements OnListDeviceListener{
	
	private String TAB = "SleepDeivceFragment";
	private int[] SLEEPTIME = new int[]{10, 20, 30, 40, 50, 60};
	private Context context;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		listener = (OnSleepFragmentListener)activity;
	}
	
	private OnSleepFragmentListener listener;
	public interface OnSleepFragmentListener {
		public void OnSleepFragmentBackList();
		public void OnClickSleep();
	}
	
	private View viewLine;
	private ListView listView;
	private ArrayList<Sleep> listModels;
	private AdapterSleep adapter;
	private TextView textView1;
	private TextView textView2;
	private ButtonSleep buttonModel;
	private TopSleepView topModelView;
	private View layoutBackground;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		context = getActivity().getApplicationContext();
		View view = inflater.inflate(R.layout.fragment_sleepdevice, container, false);

		int textSize = 14 * getResources().getDisplayMetrics().heightPixels / 480;
		int padTop = 15 * getResources().getDisplayMetrics().heightPixels / 480;
		viewLine = (View)view.findViewById(R.id.viewColorLine);
		textView1 = (TextView)view.findViewById(R.id.textView1);
		textView2 = (TextView)view.findViewById(R.id.textView2);
		textView1.setTextSize(textSize);
		textView1.setTextColor(Color.argb(255, 255, 255, 255));
		textView1.setPadding(padTop / 3, padTop, 0, 0);
		
		textView2.setTextSize(textSize);
		textView2.setTextColor(Color.argb(255, 255, 255, 255));
		textView2.setPadding(padTop / 3, 0, 0, 0);
		getGuideString();		
		
		layoutBackground = (View)view.findViewById(R.id.color_screen_layout);
		buttonModel = (ButtonSleep)view.findViewById(R.id.ButtonSleep);
		buttonModel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(listener != null){
					listener.OnClickSleep();
				}
			}
		});
		topModelView = (TopSleepView)view.findViewById(R.id.TopSleepView);
		topModelView.setOnTopSleepListener(new OnTopSleepListener() {
			
			@Override
			public void OnBackLayout() {
				if(listener != null){
					listener.OnSleepFragmentBackList();
				}
			}
		});		
		
		String giay = getString(R.string.text_change_sleep_0);
		String phut = getString(R.string.text_change_sleep_1);
		listModels = new ArrayList<Sleep>();
		Sleep sleep = new Sleep(0, getString(R.string.text_change_sleep_2), (MyApplication.switchTime == 0));
		listModels.add(sleep);
		sleep = new Sleep(15, getString(R.string.text_change_sleep_3) + " "+ 15 + giay, (MyApplication.switchTime == 15));
		listModels.add(sleep);
		sleep = new Sleep(30, getString(R.string.text_change_sleep_3) + " "+ 30 + giay, (MyApplication.switchTime == 30));
		listModels.add(sleep);
		sleep = new Sleep(60, getString(R.string.text_change_sleep_3) + " "+ 1 + phut, (MyApplication.switchTime == 60));
		listModels.add(sleep);
		sleep = new Sleep(300, getString(R.string.text_change_sleep_3) + " "+ 5 + phut, (MyApplication.switchTime == 300));
		listModels.add(sleep);
		
		adapter = new AdapterSleep(context, 1, listModels);
		listView = (ListView)view.findViewById(R.id.ListView);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				MyApplication.freezeTime = System.currentTimeMillis();
				if (listModels != null) {
					for (int i = 0; i < listModels.size(); i++) {
						Sleep model = listModels.get(i);
						if (model != null) {
							model.setActive(false);
						}
					}
					Sleep model = listModels.get(arg2);
					if (model != null) {
						model.setActive(true);
						MyApplication.switchTime = model.getGiay();
						getGuideString();
						AppSettings setting = AppSettings.getInstance(context);
						setting.saveSleepTime(MyApplication.switchTime);
					}
					adapter.notifyDataSetChanged();
				}
			}
		});
		listView.setAdapter(adapter);
		
		changeColorScreen();
		
		return view;
	}

	private void getGuideString(){
		if(textView1 == null || textView2 == null){
			return;
		}
		
		int value = (int)MyApplication.switchTime;
		String str1 = "";
		String str2 = "";
		
		switch (value) {
		case 0:
			str1 = getString(R.string.text_change_sleep_4);
			break;
		case 15:
		case 30:
			str1 = getString(R.string.text_change_sleep_5) + " " + value + getString(R.string.text_change_sleep_8);
			str2 = getString(R.string.text_change_sleep_7);
			break;
		case 60:
		case 300:
			str1 = getString(R.string.text_change_sleep_5) + " " + value/60 + getString(R.string.text_change_sleep_9);
			str2 = getString(R.string.text_change_sleep_7);
			break;
		default:
			break;
		}
		
		textView1.setText(str1);
		textView2.setText(str2);
	}

	@Override
	public void OnShowListDevice() {}

	@Override
	public void OnUpdateView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnDisplayProgressScan(boolean isScanning) {}
	
	
	private void changeColorScreen(){
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			viewLine.setBackgroundResource(R.drawable.touch_shape_line_ver);
			layoutBackground.setBackgroundColor(Color.parseColor("#004c8c"));
			layoutBackground.setAlpha(0.95f);
			textView1.setTextColor(Color.parseColor("#FFFFFF"));
			textView2.setTextColor(Color.parseColor("#FFFFFF"));
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			viewLine.setBackgroundResource(R.drawable.zlight_shape_line_ver);
			layoutBackground.setBackgroundColor(Color.argb(255, 193, 255, 232));
			layoutBackground.setAlpha(1.0f);
			textView1.setTextColor(Color.parseColor("#21BAA9"));
			textView2.setTextColor(Color.parseColor("#21BAA9"));
		}
		topModelView.invalidate();
		buttonModel.invalidate();
		if(adapter != null){
			adapter.notifyDataSetChanged();
		}
	}
	
}
