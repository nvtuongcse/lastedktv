package vn.com.sonca.Touch.Connect;

import java.util.ArrayList;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.Touch.CustomView.TouchModelView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class TouchAdapterModel extends ArrayAdapter<Model>{

	private Context context;
	private ArrayList<Model> listModels;
	public TouchAdapterModel(Context context, int resource, ArrayList<Model> listModels) {
		super(context, resource, listModels);
		this.listModels = listModels;
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TouchModelView modelView = null;
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.touch_item_model, null);
			modelView = (TouchModelView)convertView.findViewById(R.id.ModelView);
			convertView.setTag(R.id.ModelView , modelView);
		}else{
			modelView = (TouchModelView)convertView.getTag(R.id.ModelView);
		}
		if(modelView != null){
			Model model = listModels.get(position);
			modelView.setData(model);
		}
		
		return convertView;
	}

}
