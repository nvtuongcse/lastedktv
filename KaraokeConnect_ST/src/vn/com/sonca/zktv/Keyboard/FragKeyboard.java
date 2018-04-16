package vn.com.sonca.zktv.Keyboard;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.Keyboard.OnClickKeyBoardListener;
import vn.com.sonca.zktv.main.KTVMainActivity.OnKTVChangeSearchListener;

public class FragKeyboard extends Fragment implements OnKTVChangeSearchListener {
	
	private String TAB = "FragKeyboard";
	private FragmentManager fragmentManager;
	
	private OnKTVKeyboardListener listener; 
	public interface OnKTVKeyboardListener {
		public void OnKeyClick(String namekey);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		listener = (OnKTVKeyboardListener)activity;
	}
	
	private MyEditText editText;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.ktv_fragment_keyboard, container, false);
		editText = (MyEditText)view.findViewById(R.id.myEditText);
		CharKeyBoardGroup keyboard = (CharKeyBoardGroup)view.findViewById(R.id.keyBoardViewGroup);
		keyboard.setOnKeyBoardClickListener(new OnClickKeyBoardListener() {
			
			@Override
			public void OnNameKey(String namekey, View view) {
				if(listener != null){
					editText.setTextSearch(namekey);
					String search = editText.getTextSearch();
					listener.OnKeyClick(search);
				}
			}
		});
		MyLog.e(TAB, "FragKeyboard");
		
		return view;
	}
	
	@Override
	public void OnKTVChangeSearch(String search) {
		if(editText != null){
			MyLog.e(TAB, "OnKTVChangeSearch : " + search);
			editText.setAllTextSearch(search);
		}
	}

}
