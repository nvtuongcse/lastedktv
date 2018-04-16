package vn.com.sonca.zktv.FragSwitch;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.LoadSong.BaseLoadSong.OnLoadListener;
import vn.com.sonca.params.ServerStatus;
import vn.com.sonca.zktv.main.KTVMainActivity.OnKTVMainListener;
import vn.com.sonca.zktv.main.OnSwitchListener;
import vn.com.sonca.zktv.main.UtilSwitch;

public class FragSwitch extends Fragment implements OnKTVMainListener, OnClickListener {
	
	private ViewSwitch viewSwitch1, viewSwitch2, viewSwitch3;
	private ViewSwitch viewSwitch4, viewSwitch5, viewSwitch6;
	private ViewSwitch viewSwitch7, viewSwitch8, viewSwitch9;
	private OnSwitchListener listener;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		listener = (OnSwitchListener)activity;
	}
	
	private Context context;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.ktv_fragment_switch, container, false);
		context = getActivity().getApplicationContext();
		viewSwitch1 = (ViewSwitch)view.findViewById(R.id.viewSwitch1);
		viewSwitch2 = (ViewSwitch)view.findViewById(R.id.viewSwitch2);
		viewSwitch3 = (ViewSwitch)view.findViewById(R.id.viewSwitch3);
		viewSwitch4 = (ViewSwitch)view.findViewById(R.id.viewSwitch4);
		viewSwitch5 = (ViewSwitch)view.findViewById(R.id.viewSwitch5);
		viewSwitch6 = (ViewSwitch)view.findViewById(R.id.viewSwitch6);
		viewSwitch7 = (ViewSwitch)view.findViewById(R.id.viewSwitch7);
		viewSwitch8 = (ViewSwitch)view.findViewById(R.id.viewSwitch8);
		viewSwitch9 = (ViewSwitch)view.findViewById(R.id.viewSwitch9);
		
		viewSwitch1.setDataView(1, 
			getString(R.string.ktv_switch_1), true,
			context.getResources().getDrawable(R.drawable.ktv_item_1), 
			context.getResources().getDrawable(R.drawable.ktv_item_type));
		viewSwitch2.setDataView(2, 
				getString(R.string.ktv_switch_2), true,
				context.getResources().getDrawable(R.drawable.ktv_item_2), 
				context.getResources().getDrawable(R.drawable.ktv_item_seachsong));
		viewSwitch3.setDataView(3, 
				getString(R.string.ktv_switch_3), false, 
				context.getResources().getDrawable(R.drawable.ktv_item_xam), 
				context.getResources().getDrawable(R.drawable.ktv_item_searchnumber));
		viewSwitch4.setDataView(4, 
				getString(R.string.ktv_switch_4), true, 
				context.getResources().getDrawable(R.drawable.ktv_item_4), 
				context.getResources().getDrawable(R.drawable.ktv_item_singer));
		viewSwitch5.setDataView(5, 
				getString(R.string.ktv_switch_5), true,
				context.getResources().getDrawable(R.drawable.ktv_item_5), 
				context.getResources().getDrawable(R.drawable.ktv_item_language));
		viewSwitch6.setDataView(6, 
				getString(R.string.ktv_switch_6), true,
				context.getResources().getDrawable(R.drawable.ktv_item_6), 
				context.getResources().getDrawable(R.drawable.ktv_item_hot));
		viewSwitch7.setDataView(7, 
				getString(R.string.ktv_switch_7), true,
				context.getResources().getDrawable(R.drawable.ktv_item_7), 
				context.getResources().getDrawable(R.drawable.ktv_item_playlist));
		viewSwitch8.setDataView(8, 
				getString(R.string.ktv_switch_8), true,
				context.getResources().getDrawable(R.drawable.ktv_item_8), 
				context.getResources().getDrawable(R.drawable.ktv_item_theme));
		viewSwitch9.setDataView(9, 
				getString(R.string.ktv_switch_9), true,
				context.getResources().getDrawable(R.drawable.ktv_item_9), 
				context.getResources().getDrawable(R.drawable.ktv_item_setting));
		
		viewSwitch1.setOnClickListener(this);
		viewSwitch2.setOnClickListener(this);
		viewSwitch3.setOnClickListener(this);
		viewSwitch4.setOnClickListener(this);
		viewSwitch5.setOnClickListener(this);
		viewSwitch6.setOnClickListener(this);
		viewSwitch7.setOnClickListener(this);
		viewSwitch8.setOnClickListener(this);
		viewSwitch9.setOnClickListener(this);
		
		
		return view;
	}

	@Override
	public void OnKTVSearch(String search) {
		
	}

	@Override
	public void onClick(View view) {
		if(listener != null){
			switch (view.getId()) {
			case R.id.viewSwitch1:
				listener.OnSwitchClick(UtilSwitch.THE_LOAI);
				break;
			case R.id.viewSwitch2:
				listener.OnSwitchClick(UtilSwitch.CHON_TEN);
				break;
			case R.id.viewSwitch3:
				listener.OnSwitchClick(UtilSwitch.CHON_SO);
				break;
			case R.id.viewSwitch4:
				listener.OnSwitchClick(UtilSwitch.CA_SI);
				break;
			case R.id.viewSwitch5:
				listener.OnSwitchClick(UtilSwitch.NGON_NGU);
				break;
			case R.id.viewSwitch6:
				listener.OnSwitchClick(UtilSwitch.NHAC_HOT);
				break;
			case R.id.viewSwitch7:
				listener.OnSwitchClick(UtilSwitch.DA_CHON);
				break;
			case R.id.viewSwitch8:
				listener.OnSwitchClick(UtilSwitch.GIAO_DIEN);
				break;
			case R.id.viewSwitch9:
				listener.OnSwitchClick(UtilSwitch.HE_THONG);
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
