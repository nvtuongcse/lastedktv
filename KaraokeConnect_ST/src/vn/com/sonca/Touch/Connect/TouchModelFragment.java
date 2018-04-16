package vn.com.sonca.Touch.Connect;

import java.util.ArrayList;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.Connect.ModelDocument.OnModelDocumentListener;
import vn.com.sonca.Touch.CustomView.TouchButtonModel;
import vn.com.sonca.Touch.CustomView.TouchTopModelView;
import vn.com.sonca.Touch.CustomView.TouchTopModelView.OnTopModelListener;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.params.SKServer;
import vn.com.sonca.utils.AppSettings;
import vn.com.sonca.zktv.main.KTVMainActivity;
import vn.com.sonca.zzzzz.DeviceStote;
import vn.com.sonca.zzzzz.MyApplication;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class TouchModelFragment extends Fragment {
	
	private String TAB = "ModelFragment";
	private String NameDevice;
	private String IPDevice;
	private String Pass;
	
	private TouchMainActivity mainActivity;
	private KTVMainActivity ktvMainActivity;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		listener = (OnModelFragmentListener)activity;
		
		if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			ktvMainActivity = (KTVMainActivity) activity;
		} else {
			mainActivity = (TouchMainActivity) activity;	
		}
		
	}
	
	public FragmentActivity getMyActivity(){
		if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			return this.ktvMainActivity;
		} else {
			return this.mainActivity;
		}
	}
	
	private OnModelFragmentListener listener;
	public interface OnModelFragmentListener {
		public void OnModelFragmentBackList();
		public void OnClickModel(Model model);
		public void OnClickModel();
	}
	
	private Context context;
	private View viewLine;
	private ListView listView;
	private ArrayList<Model> listModels;
	private TouchAdapterModel adapter;
	private View viewBackgroup;
	private TouchTopModelView topModelView;
	private TouchButtonModel buttonModel;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		IPDevice = getArguments().getString("IP", "");
		NameDevice = getArguments().getString("Name", "");
		Pass = getArguments().getString("Pass", "");
		
		context = getActivity().getApplicationContext();
		View view = inflater.inflate(R.layout.touch_fragment_model, container, false);
		
		viewLine = (View)view.findViewById(R.id.viewColorLine);
		viewBackgroup = (View)view.findViewById(R.id.color_screen_layout);
		buttonModel = (TouchButtonModel)view.findViewById(R.id.ButtonModel);
		buttonModel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(listener != null){
					listener.OnClickModel();
				}
			}
		});
		
		topModelView = (TouchTopModelView)view.findViewById(R.id.TopModelView);
		topModelView.setNameDevice(NameDevice);
		topModelView.setOnTopModelListener(new OnTopModelListener() {
			
			@Override
			public void OnBackLayout() {
				if(listener != null){
					listener.OnModelFragmentBackList();
				}
			}
		});
		
		listView = (ListView)view.findViewById(R.id.ListView);
		listModels = new ArrayList<Model>();
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				if(listModels != null){
					if(listModels.get(arg2).getEn() != 0){
						for (int i = 0; i < listModels.size(); i++) {
							Model model = listModels.get(i);
							if(model != null){
								if(model.getEn() != 0){
									model.setActive(false);
								}
							}
						}
						Model model = listModels.get(arg2);
						if(model != null){
							model.setActive(true);
							saveModelRemote(model);
							adapter.notifyDataSetChanged();
						}						
					}
				}
			}
		});
		

		DeviceStote stote = new DeviceStote(context);
		SKServer skServer = stote.getDevice(IPDevice);
		int ircRemote = - 1;
		if(skServer != null){
			ircRemote = skServer.getIrcRemote();
			MyLog.e(TAB, "ircRemote : " + ircRemote);
		}
		ModelDocument document = new ModelDocument(context, ircRemote);
		document.setOnModelDocumentListener(new OnModelDocumentListener() {
			
			@Override
			public void OnFinishRead(ArrayList<Model> list) {
				listModels.clear();
				listModels.addAll(list);
				adapter = new TouchAdapterModel(context, 1, listModels);
				listView.setAdapter(adapter);
			}
		});
		document.readDocument();
		
		changeColorScreen();
		
		return view;
	}
	
	
	private void saveModelRemote(Model model){
		if(model != null && getMyActivity() != null){
			AppSettings setting = AppSettings.getInstance(context);
			setting.saveIrcRemote(model.getIrc());
			setting.saveNameRemote(model.getName());
				//---------//
			
			if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				if(KTVMainActivity.serverStatus != null){
					SKServer skServer = ((MyApplication)ktvMainActivity.
							getApplication()).getDeviceCurrent();
					if(skServer != null){
						skServer.setIrcRemote(model.getIrc());
						skServer.setNameRemote(model.getName());
					}
				}
			} else {
				if(TouchMainActivity.serverStatus != null){
					SKServer skServer = ((MyApplication)mainActivity.
							getApplication()).getDeviceCurrent();
					if(skServer != null){
						skServer.setIrcRemote(model.getIrc());
						skServer.setNameRemote(model.getName());
					}
				}	
			}
			
				//---------//
			SKServer skServer = new SKServer();
			MyApplication.intRemoteModel = model.getIrc();
			skServer.setConnectedIPStr(IPDevice);
			skServer.setName(NameDevice);
			skServer.setModelDevice(1);
			skServer.setIrcRemote(model.getIrc());
			skServer.setNameRemote(model.getName());
			DeviceStote stote = new DeviceStote(context);
			stote.putDevice(skServer);
			if (listener != null) {
				listener.OnClickModel(model);
			}
		}
	}
	
	private void changeColorScreen(){
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			viewLine.setBackgroundResource(R.drawable.touch_shape_line_ver);
			viewBackgroup.setBackgroundColor(Color.parseColor("#004c8c"));
			viewBackgroup.setAlpha(0.95f);
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			viewLine.setBackgroundResource(R.drawable.zlight_shape_line_ver);
			viewBackgroup.setBackgroundColor(Color.argb(255, 193, 255, 232));
			viewBackgroup.setAlpha(1.0f);
		}
		topModelView.invalidate();
		buttonModel.invalidate();
		if(adapter != null){
			adapter.notifyDataSetChanged();
		}
	}

}
