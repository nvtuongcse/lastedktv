package vn.com.sonca.Touch.Singer;

import java.util.ArrayList;

import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.CustomView.TouchSingerItemView;
import vn.com.sonca.params.Singer;
import vn.com.hanhphuc.karremote.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class TouchSingerAdapter extends ArrayAdapter<Singer> {

	private Context context;
	private String search = "";
	private ArrayList<Singer> singerList;

	public TouchSingerAdapter(Context context, int resource, String search, ArrayList<Singer> singerList) {
		super(context, resource, singerList);
		this.singerList = singerList;
		this.context = context;
		if(search != null){
			this.search = search;
		}else{
			this.search = "";
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TouchSingerItemView singerItemView;
		if (convertView == null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.touch_singer_gridview_item, null);
			singerItemView = (TouchSingerItemView) convertView.findViewById(R.id.singer_gridview_item);
			convertView.setTag(singerItemView);
		}else{
			singerItemView = (TouchSingerItemView)convertView.getTag();
		}
		if(singerItemView != null){
			Singer singer = singerList.get(position);
			singerItemView.setData(singer.getName(), search, singer.getCoverID());
		}
		return convertView;
	}

////////////////////////////////////////////////////////////
	
/*
	private Drawable getDrawable(int position){
		int id;
		switch (position) {
		
		case 0:		id = R.drawable.hinh_1;		break;
		case 1:		id = R.drawable.hinh_2;		break;
		case 2:		id = R.drawable.hinh_3;		break;
		case 3:		id = R.drawable.hinh_4;		break;
		case 4:		id = R.drawable.hinh_5;		break;
		case 5:		id = R.drawable.hinh_6;		break;
		case 6:		id = R.drawable.hinh_7;		break;
		case 7:		id = R.drawable.hinh_8;		break;
		case 8:		id = R.drawable.abba225;		break;
		case 9:		id = R.drawable.abba225;		break;
		case 10:		id = R.drawable.adam;		break;
		case 11:		id = R.drawable.adam287;		break;
		case 12:		id = R.drawable.anhhuy;		break;
		case 13:		id = R.drawable.anhhuy287;		break;
		case 14:		id = R.drawable.annythanh;		break;
		case 15:		id = R.drawable.annythanh287;		break;
		case 16:		id = R.drawable.artista;		break;
		case 17:		id = R.drawable.artista;		break;
		case 18:	id = R.drawable.artista287;		break;
		case 19:	id = R.drawable.boyzone;		break;
		case 20:	id = R.drawable.boyzone205;		break;
		case 21:	id = R.drawable.kelly;		break;
		case 22:	id = R.drawable.kelly287;		break;
		case 23:	id = R.drawable.kenvinkhoa;		break;
		case 24:	id = R.drawable.kenvinkhoa238;		break;
		case 25:	id = R.drawable.minhtam;		break;
		case 26:	id = R.drawable.minhtam208;		break;
		case 27:	id = R.drawable.namkhanh;		break;
		case 28:	id = R.drawable.namkhanh259;		break;
		case 29:	id = R.drawable.nhathanh;		break;
		case 30:	id = R.drawable.nhathanh245;		break;
		case 31:	id = R.drawable.nhatruc;		break;
		case 32:	id = R.drawable.nhatruc220;		break;
		case 33:	id = R.drawable.anh_khang;		break;
		case 34:	id = R.drawable.bang_cuong;		break;
		case 35:	id = R.drawable.nguyen_duc_cuong;		break;
		case 36:	id = R.drawable.nguyen_van_chung;		break;
		
		default: id = R.drawable.ic_launcher;		break;
		}
		return context.getResources().getDrawable(id);	
	}
	
	private String getName(int position){
		String id = "";
		switch (position) {
		case 0:		id = "hinh_1";		break;
		case 1:		id = "hinh_2";		break;
		case 2:		id = "hinh_3";		break;
		case 3:		id = "hinh_4";		break;
		case 4:		id = "hinh_5";		break;
		case 5:		id = "hinh_6";		break;
		case 6:		id = "hinh_7";		break;
		case 7:		id = "hinh_8";		break;
		case 8:		id = "abba";		break;
		case 9:		id = "abba225";		break;
		case 10:		id = "adam";		break;
		case 11:		id = "adam287";		break;
		case 12:		id = "anhhuy";		break;
		case 13:		id = "anhhuy287";		break;
		case 14:		id = "annythanh";		break;
		case 15:		id = "annythanh287";		break;
		case 16:		id = "artista";		break;
		case 17:		id = "artista";		break;
		case 18:	id = "artista287";		break;
		case 19:	id = "boyzone";		break;
		case 20:	id = "boyzone205";		break;
		case 21:	id = "kelly";		break;
		case 22:	id = "kelly287";		break;
		case 23:	id = "kenvinkhoa";		break;
		case 24:	id = "kenvinkhoa238";		break;
		case 25:	id = "minhtam";		break;
		case 26:	id = "minhtam208";		break;
		case 27:	id = "namkhanh";		break;
		case 28:	id = "namkhanh259";		break;
		case 29:	id = "nhathanh";		break;
		case 30:	id = "nhathanh245";		break;
		case 31:	id = "nhatruc";		break;
		case 32:	id = "nhatruc220";		break;
		case 33:	id = "anh_khang" ;		break;
		case 34:	id = "bang_cuong" ;		break;
		case 35:	id = "nguyen_duc_cuong" ;		break;
		case 36:	id = "nguyen_van_chung" ;		break;
		default: 	id = "ic_launcher";		break;
		}
		return id;
	}
*/
	
	
}