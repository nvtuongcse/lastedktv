package vn.com.sonca.AddDataSong;

import java.util.ArrayList;

import android.R.integer;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.params.ItemUSB;

public class FragScanUSB extends Fragment {
	
	private OnScanUSBListener listener;
	public interface OnScanUSBListener {
		public void OnItemClick(ItemUSB usb);
	}
	public void setOnScanUSBListener(OnScanUSBListener listener){
		this.listener = listener;
	}
	
	private Context context;
	private GridView gridView;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dialog_scan_usb, container, false);
		context = getActivity().getApplicationContext();
		gridView = (GridView)view.findViewById(R.id.gridview);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				if(list != null){
					if(pos <= list.size()){
						if(listener != null){
							listener.OnItemClick(list.get(pos));
						}
					}
				}
			}
			
		});
		
//		ArrayList<ItemUSB> liUsbs = new ArrayList<ItemUSB>();
//		for (int i = 0; i < 10; i++) {
//			ItemUSB usb = new ItemUSB(i, "UBS " + i);
//			liUsbs.add(usb);
//		}
//		showDataFromSK(liUsbs);
		
		
		return view;
	}
	
	private ArrayList<ItemUSB> list;
	private AdapterUSB adapterUSB = null;
	public void showDataFromSK(ArrayList<ItemUSB> list){
		this.list = list;
		adapterUSB = new AdapterUSB(context, 
				R.layout.dialog_usbsongdata_item, list);
		gridView.setAdapter(adapterUSB);
	}

}
