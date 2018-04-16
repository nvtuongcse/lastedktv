package vn.com.sonca.Touch.Dance;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import vn.com.sonca.Touch.CustomView.TouchItemDance;
import vn.com.sonca.Touch.CustomView.TouchItemDance.OnGroupDanceListener;
import vn.com.sonca.Touch.Listener.TouchIAdapter;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.params.Song;
import vn.com.hanhphuc.karremote.R;

public class TouchAdapterDance extends ArrayAdapter<Song> {
	
	private String TAB = "AdapterSong";
	private ArrayList<Song> arrayList;
	private Typeface typeface;
	private TouchIAdapter listener;
	private Context context;
	
	public void setOnAdapterListener(TouchIAdapter listener){
		this.listener = listener;
	}
	
	public TouchAdapterDance(Context context, int resource, ArrayList<Song> arrayList, TouchMainActivity mainActivity) {
		super(context, resource, arrayList);
		typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC);
		this.context = context;
		this.arrayList = arrayList;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		TouchItemDance myGroupSong = null;
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.touch_item_dance, null);
			myGroupSong = (TouchItemDance) convertView.findViewById(R.id.myGroupPlaylist);
			convertView.setTag(R.id.myGroupPlaylist , myGroupSong);
		}else{
			myGroupSong = (TouchItemDance)convertView.getTag(R.id.myGroupPlaylist);
		}
		if(myGroupSong != null){
			final Song song = arrayList.get(position);
			myGroupSong.setTypeface(typeface);
			myGroupSong.setContentView(position , song);
			myGroupSong.setOnGroupDanceListener(new OnGroupDanceListener() {
				@Override
				public void OnFristRes(int position, Song song) {
					if(listener != null){
						listener.OnFirstClick(song , position, -1 , -1);
					}
				}
				@Override
				public void OnActive(Song song) {}
			});
		}
		return convertView;
	}
}
