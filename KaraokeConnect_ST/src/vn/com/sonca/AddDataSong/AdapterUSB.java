package vn.com.sonca.AddDataSong;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.params.ItemUSB;

public class AdapterUSB extends ArrayAdapter<ItemUSB> {
	
	private Context context;
	private ArrayList<ItemUSB> arrayList;
	public AdapterUSB(Context context, int resource, ArrayList<ItemUSB> arrayList) {
		super(context, resource, arrayList);
		this.arrayList = arrayList;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewUsb viewUsb;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.dialog_usbsongdata_item, null);
			viewUsb = (ViewUsb) convertView.findViewById(R.id.viewUSB);
			convertView.setTag(R.id.viewUSB, viewUsb);
		} else {
			viewUsb = (ViewUsb) convertView.getTag(R.id.viewUSB);
		}
		if(viewUsb != null){
			ItemUSB usb = arrayList.get(position);
			viewUsb.setData(usb);
		}
		return convertView;
	}
	
}
