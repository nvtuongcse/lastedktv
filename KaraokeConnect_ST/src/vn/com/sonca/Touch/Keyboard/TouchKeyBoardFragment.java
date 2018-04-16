package vn.com.sonca.Touch.Keyboard;

import vn.com.sonca.Touch.CustomView.TouchKeyboardView;
import vn.com.sonca.Touch.CustomView.TouchKeyboardView.OnKeyboardViewListener;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity.OnClearTextListener;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity.OnMainKeyboardListener;
import vn.com.sonca.zzzzz.MyApplication;
import vn.com.hanhphuc.karremote.R;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class TouchKeyBoardFragment extends Fragment implements OnClearTextListener , OnMainKeyboardListener {
	
	private String TAB = "KeyBoardFragment";
	private TouchMainActivity mainActivity;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mainActivity = (TouchMainActivity) activity;
			listener = (OnKeyBoardFragmenttListener) activity;
		} catch (Exception ex) {}
	}
	
	private OnKeyBoardFragmenttListener listener;
	public interface OnKeyBoardFragmenttListener {
		public void OnKeyboardView(String key);
	}
	
	public void setOnKeyBoardFragmenttListener(OnKeyBoardFragmenttListener listener){
		this.listener = listener;
	}
	
	private TouchKeyboardView keyboardView;
	private GroupKeyBoardNumber keyBoardNumber;
	private LinearLayout layoutBackground;
	private LinearLayout layoutNumber;
	private GroupKeyBoard keyBoard;
	private View viewLine;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.touch_fragment_keyboard, container, false);
		
		viewLine = (View)view.findViewById(R.id.view_line);
		layoutBackground = (LinearLayout)view.findViewById(R.id.layout_backgrounp);		
		
		layoutNumber = (LinearLayout)view.findViewById(R.id.layoutNumber);
		keyBoard = (GroupKeyBoard)view.findViewById(R.id.GroupKeyBoard);
		keyBoard.setOnClickKeyBoardListener(new OnClickKeyBoardListener() {
			
			@Override
			public void OnNameKey(String namekey, View view) {
				if(namekey.equals("123")){
					int visible = layoutNumber.getVisibility();
					if(visible == View.GONE){
						layoutNumber.setVisibility(View.VISIBLE);
						((BaseKey)view).setLayoutView(View.GONE);
					}else{
						layoutNumber.setVisibility(View.GONE);
						((BaseKey)view).setLayoutView(View.VISIBLE);
					}
				}else{
					if(listener != null){
						listener.OnKeyboardView(namekey);
					}
				}
			}
		});
		
		keyBoardNumber = (GroupKeyBoardNumber)view.findViewById(R.id.GroupKeyBoardNumber);
		keyBoardNumber.setOnClickKeyBoardListener(new OnClickKeyBoardListener() {
			
			@Override
			public void OnNameKey(String namekey, View view) {
				if(listener != null){
					listener.OnKeyboardView(namekey);
				}
			}
		});
		
		changeColorScreen();
		
		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void OnClearText() {
		// keyboardView.setClearKeyBoard();
	}
	
	@Override
	public void OnTextShowing(String search) {
		
	}

	@Override
	public void OnDrawerSlide() {
		
	}

	@Override
	public void OnUpdateView() {
		changeColorScreen();
	}
	
	public void showDefaultKeyboard(){
		if(isAdded()){
			if(layoutNumber != null && keyBoard != null){
				layoutNumber.setVisibility(View.VISIBLE);
				keyBoard.setKey123();
			}
		}
	}

	private void changeColorScreen(){
		if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			layoutBackground.setBackgroundColor(Color.parseColor("#004c8c"));
			layoutBackground.setAlpha(0.95f);
			viewLine.setBackgroundResource(R.drawable.touch_shape_line_hor);
			keyBoardNumber.changeColorScreen();
			keyBoard.changeColorScreen();
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			layoutBackground.setBackgroundColor(Color.parseColor("#C1FFE8"));
			layoutBackground.setAlpha(1.0f);
			viewLine.setBackgroundColor(Color.parseColor("#C1FFE8"));
			keyBoardNumber.changeColorScreen();
			keyBoard.changeColorScreen();
		}
	}
}
