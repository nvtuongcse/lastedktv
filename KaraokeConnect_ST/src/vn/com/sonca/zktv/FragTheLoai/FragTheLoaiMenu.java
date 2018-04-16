package vn.com.sonca.zktv.FragTheLoai;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.params.ServerStatus;
import vn.com.sonca.zktv.FragSwitch.ViewSwitch;
import vn.com.sonca.zktv.main.KTVMainActivity.OnKTVMainListener;
import vn.com.sonca.zktv.main.OnSwitchListener;
import vn.com.sonca.zktv.main.UtilSwitch;

public class FragTheLoaiMenu extends Fragment implements OnKTVMainListener, OnClickListener {
	
	private ViewSwitch viewTheLoai1, viewTheLoai2, viewTheLoai3;
	private ViewSwitch viewTheLoai4, viewTheLoai5, viewTheLoai6;
	private ViewSwitch viewTheLoai7, viewTheLoai8, viewTheLoai9;
	private OnSwitchListener listener;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		listener = (OnSwitchListener)activity;
	}
	
	private Context context;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.ktv_fragment_theloai_menu, container, false);
		context = getActivity().getApplicationContext();
		viewTheLoai1 = (ViewSwitch)view.findViewById(R.id.viewTheLoai1);
		viewTheLoai2 = (ViewSwitch)view.findViewById(R.id.viewTheLoai2);
		viewTheLoai3 = (ViewSwitch)view.findViewById(R.id.viewTheLoai3);
		viewTheLoai4 = (ViewSwitch)view.findViewById(R.id.viewTheLoai4);
		viewTheLoai5 = (ViewSwitch)view.findViewById(R.id.viewTheLoai5);
		viewTheLoai6 = (ViewSwitch)view.findViewById(R.id.viewTheLoai6);
		viewTheLoai7 = (ViewSwitch)view.findViewById(R.id.viewTheLoai7);
		viewTheLoai8 = (ViewSwitch)view.findViewById(R.id.viewTheLoai8);
		viewTheLoai9 = (ViewSwitch)view.findViewById(R.id.viewTheLoai9);
		
		viewTheLoai1.setDataView(1, 
			getString(R.string.ktv_theloai_1), true,
			context.getResources().getDrawable(R.drawable.ktv_item_1), 
			context.getResources().getDrawable(R.drawable.ktv_item_songca));
		viewTheLoai2.setDataView(2, 
				getString(R.string.ktv_theloai_2), true,
				context.getResources().getDrawable(R.drawable.ktv_item_2), 
				context.getResources().getDrawable(R.drawable.ktv_item_nhacdo));
		viewTheLoai3.setDataView(3, 
				getString(R.string.ktv_theloai_3), true, 
				context.getResources().getDrawable(R.drawable.ktv_item_3), 
				context.getResources().getDrawable(R.drawable.ktv_item_thieunhi));
//		viewTheLoai4.setDataView(4, 
//				getString(R.string.ktv_theloai_4), false, 
//				context.getResources().getDrawable(R.drawable.ktv_item_xam), 
//				context.getResources().getDrawable(R.drawable.ktv_item_sinhnhat));
		viewTheLoai4.setDataView(4, 
				getString(R.string.ktv_theloai_10), true, 
				context.getResources().getDrawable(R.drawable.ktv_item_4), 
				context.getResources().getDrawable(R.drawable.ktv_item_china));
		viewTheLoai5.setDataView(5, 
				getString(R.string.ktv_theloai_5), true,
				context.getResources().getDrawable(R.drawable.ktv_item_5), 
				context.getResources().getDrawable(R.drawable.ktv_item_volmoi));
		viewTheLoai6.setDataView(6, 
				getString(R.string.ktv_theloai_6), true,
				context.getResources().getDrawable(R.drawable.ktv_item_6), 
				context.getResources().getDrawable(R.drawable.ktv_item_remix));
		viewTheLoai7.setDataView(7, 
				getString(R.string.ktv_theloai_7), false,
				context.getResources().getDrawable(R.drawable.ktv_item_xam), 
				context.getResources().getDrawable(R.drawable.ktv_item_lienkhuc));
		viewTheLoai8.setDataView(8, 
				getString(R.string.ktv_theloai_8), true,
				context.getResources().getDrawable(R.drawable.ktv_item_8), 
				context.getResources().getDrawable(R.drawable.ktv_item_danca));
		viewTheLoai9.setDataView(9, 
				getString(R.string.ktv_theloai_9), true,
				context.getResources().getDrawable(R.drawable.ktv_item_9), 
				context.getResources().getDrawable(R.drawable.ktv_item_hot));
		
		viewTheLoai1.setOnClickListener(this);
		viewTheLoai2.setOnClickListener(this);
		viewTheLoai3.setOnClickListener(this);
		viewTheLoai4.setOnClickListener(this);
		viewTheLoai5.setOnClickListener(this);
		viewTheLoai6.setOnClickListener(this);
		viewTheLoai7.setOnClickListener(this);
		viewTheLoai8.setOnClickListener(this);
		viewTheLoai9.setOnClickListener(this);
		
		
		return view;
	}

	@Override
	public void OnKTVSearch(String search) {
		
	}

	@Override
	public void onClick(View view) {
		if(listener != null){
			switch (view.getId()) {
			case R.id.viewTheLoai1:
				listener.OnSwitchClick(UtilSwitch.THELOAI_SONGCA);
				break;
			case R.id.viewTheLoai2:
				listener.OnSwitchClick(UtilSwitch.THELOAI_NHACDO);
				break;
			case R.id.viewTheLoai3:
				listener.OnSwitchClick(UtilSwitch.THELOAI_THIEUNHI);
				break;
			case R.id.viewTheLoai4:
//				listener.OnSwitchClick(UtilSwitch.THELOAI_SINHNHAT);
				listener.OnSwitchClick(UtilSwitch.THELOAI_CHINA);
				break;
			case R.id.viewTheLoai5:
				listener.OnSwitchClick(UtilSwitch.THELOAI_VOLMOI);
				break;
			case R.id.viewTheLoai6:
				listener.OnSwitchClick(UtilSwitch.THELOAI_REMIX);
				break;
			case R.id.viewTheLoai7:
				listener.OnSwitchClick(UtilSwitch.THELOAI_LIENKHUC);
				break;
			case R.id.viewTheLoai8:
				listener.OnSwitchClick(UtilSwitch.THELOAI_DANCA);
				break;
			case R.id.viewTheLoai9:
				listener.OnSwitchClick(UtilSwitch.THELOAI_NHACHOT);
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void OnLayoutFrag(ServerStatus status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnSK90009() {
		// TODO Auto-generated method stub
		
	}

}
