package vn.com.sonca.zktv.main;

import android.support.v4.app.Fragment;
import vn.com.sonca.zktv.main.KTVMainActivity.OnKTVMainListener;

public abstract class FragBase extends Fragment implements OnKTVMainListener {
	
	protected abstract void OnLoadSearch(String textSearch);

	private String textSearch;

	public String getTextSearch() {
		return textSearch;
	}
	public void setTextSearch(String textSearch) {
		this.textSearch = textSearch;
	}
	
	@Override
	public void OnKTVSearch(String search) {
		setTextSearch(search);
		OnLoadSearch(search);
	}
	
	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}


}
