package vn.com.sonca.Touch.Connect;

import java.util.ArrayList;
import vn.com.sonca.Touch.CustomView.TouchOneDeviceView;
import vn.com.sonca.Touch.CustomView.TouchOneDeviceView.OnOneDeviceViewListener;
import vn.com.sonca.params.SKServer;
import vn.com.hanhphuc.karremote.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class TouchAdapterDevice extends ArrayAdapter<SKServer> {
	
	private Context context;
	private ArrayList<SKServer> arrayList;
	
	private OnDeviceListner listner;
	public interface OnDeviceListner {
		public void OnConnect(SKServer skServer);
		public void OnRemoveDevice(SKServer skServer);
		public void OnDisConnect(SKServer skServer);
		public void OnSelectModel(SKServer skServer);
		public void OnShowHi_W(SKServer skServer);
	}
	
	public void setOnDeviceListner(OnDeviceListner listner){
		this.listner = listner;
	}

	public TouchAdapterDevice(Context context, int resource, ArrayList<SKServer> arrayList) {
		super(context, resource, arrayList);
		this.arrayList = arrayList;
		this.context = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(R.layout.touch_item_device_list, null);
		TouchOneDeviceView deviceView = (TouchOneDeviceView)convertView.findViewById(R.id.OneDeviceView);
		SKServer skServer = arrayList.get(position);
		deviceView.setDevice(skServer);
		
		deviceView.setOnOneDeviceViewListener(new OnOneDeviceViewListener() {
			@Override
			public void OnPerformConnect(SKServer skServer) {
				if(listner != null){
					listner.OnConnect(skServer);
				}
			}
			
			@Override
			public void OnDisConnect(SKServer skServer) {
				if(listner != null){
					listner.OnDisConnect(skServer);
				}
			}
			@Override
			public void OnRemoveDevice(SKServer skServer) {
				if(listner != null){
					listner.OnRemoveDevice(skServer);
				}
			}
			
			@Override
			public void OnSelectModel(SKServer skServer) {
				if(listner != null){
					listner.OnSelectModel(skServer);
				}
			}

			@Override
			public void OnShowHi_W(SKServer skServer) {
				if(listner != null){
					listner.OnShowHi_W(skServer);
				}
			}
		});
		return convertView;
	}

}
