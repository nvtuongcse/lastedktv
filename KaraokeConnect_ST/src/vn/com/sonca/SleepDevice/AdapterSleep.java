package vn.com.sonca.SleepDevice;

import java.util.ArrayList;

import vn.com.hanhphuc.karremote.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class AdapterSleep extends ArrayAdapter<Sleep> {
	
	private Context context;
	private ArrayList<Sleep> listModels;
	public AdapterSleep(Context context, int resource, ArrayList<Sleep> listModels) {
		super(context, resource, listModels);
		this.listModels = listModels;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		SleepView modelView = null;
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item_sleep, null);
			modelView = (SleepView)convertView.findViewById(R.id.SleepView);
			convertView.setTag(R.id.SleepView , modelView);
		}else{
			modelView = (SleepView)convertView.getTag(R.id.SleepView);
		}
		if(modelView != null){
			Sleep model = listModels.get(position);
			modelView.setData(model);
		}
		
		return convertView;
	}

}
