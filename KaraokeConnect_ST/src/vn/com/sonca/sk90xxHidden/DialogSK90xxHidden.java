package vn.com.sonca.sk90xxHidden;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import app.sonca.Dialog.ScoreLayout.BaseDialog2;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.Keyboard.BaseKey;
import vn.com.sonca.Touch.Keyboard.GroupKeyBoard;
import vn.com.sonca.Touch.Keyboard.GroupKeyBoardNumber;
import vn.com.sonca.Touch.Keyboard.OnClickKeyBoardListener;
import vn.com.sonca.params.Song;
import vn.com.sonca.sk90xxHidden.FragDeleteSong.OnFragDeleteSongListener;
import vn.com.sonca.sk90xxHidden.ViewAddTop.OnViewAddTopListener;
import vn.com.sonca.sk90xxHidden.ViewSearchAdd.OnSearchAddListener;
import vn.com.sonca.zzzzz.MyApplication;

public class DialogSK90xxHidden extends BaseDialog2 {
	
	private String TAB = "DialogSK90xxHidden";
	private Context context;
	private FragmentManager manager;
	
	private OnDialogSK90xxHiddenListener listener;
	public interface OnDialogSK90xxHiddenListener {
		public void OnAddSong(ArrayList<Song> info);
		public void OnCancelAddSong(ArrayList<Song> info);
		public void OnCancelAll();
		public void OnUpdateHiddenMessage(String msg);
		public void OnSKChangeDialog(int dialogType);
		public void OnSKUpdateSearch();
	}
	public void setOnDialogSK90xxHiddenListener(OnDialogSK90xxHiddenListener listener){
		this.listener = listener;
	}

	private ArrayList<Song> listSong;
	public DialogSK90xxHidden(Context context, Window window, FragmentManager manager) {
		super(context, window);
		listSong = new ArrayList<Song>();
		this.manager = manager;
		this.context = context;
	}

	@Override
	protected int getIdLayout() {
		return R.layout.dialog_sk90xx_hidden;
	}

	@Override
	protected int getTimerShow() {
		return 0;
	}

	private ViewSearchAdd searchAdd;
	private GroupKeyBoard keyBoard;
	private LinearLayout layoutNumber;
	private LinearLayout layoutKeyboard;
	private GroupKeyBoardNumber keyBoardNumber;
	private ViewAddBut butAll, butLeft, butRight;
	private TextView txtTotal;
		
	private long startFrag = 0;
	
	@Override
	protected View getView(View contentView) {
		startFrag = System.currentTimeMillis();
		MyApplication.flagInsideHidden = true;
		
		ViewAddTop viewAddTop = (ViewAddTop)contentView.findViewById(R.id.topView);
		viewAddTop.setOnViewAddTopListener(new OnViewAddTopListener() {
			
			@Override
			public void OnTopTaiBai() {
				if(System.currentTimeMillis() - startFrag < 2000){
					return;
				}
				
				removeFrag();
				if(listener != null){
					listener.OnSKChangeDialog(0);
				}
			}
			
			@Override
			public void OnTopBack() {
				if(System.currentTimeMillis() - startFrag < 2000){
					return;
				}
				
				removeFrag();
				
				MyApplication.flagInsideHidden = false;
				if(listener != null){
					listener.OnSKUpdateSearch();
				}
				dismissDialog(false);
			}
			
			@Override
			public void OnTopAnBai() {
				
			}
		});
		viewAddTop.setServerName(((MyApplication) context).getDeviceCurrent().getName());		
		
		butAll = (ViewAddBut)contentView.findViewById(R.id.butAll);
		butLeft = (ViewAddBut)contentView.findViewById(R.id.butLeft);
		butRight = (ViewAddBut)contentView.findViewById(R.id.butRight);
		txtTotal = (TextView)contentView.findViewById(R.id.txtTotal);
				
		butAll.setTitle(context.getResources().getString(R.string.dialog_90xx_hid_5));
		butAll.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View arg0) {
				if(listener != null){
					listener.OnCancelAll();
				}
			}
		});
		
		butLeft.setTitle(context.getResources().getString(R.string.dialog_90xx_hid_2));
		butLeft.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View arg0) {
				if(listSong != null && listener != null){
					if(listSong.size() == 0){
						listener.OnUpdateHiddenMessage(context.getResources().getString(R.string.dialog_90xx_online_11));	
					} else {
						listener.OnCancelAddSong(listSong);	
					}	
				}
			}
		});
		
		butRight.setTitle(context.getResources().getString(R.string.dialog_90xx_hid_3));
		butRight.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View arg0) {
				if(listSong != null && listener != null){					
					if(listSong.size() == 0){
						listener.OnUpdateHiddenMessage(context.getResources().getString(R.string.dialog_90xx_online_11));	
					} else {
						listener.OnAddSong(listSong);
					}
				}
			}
		});
		
		searchAdd = (ViewSearchAdd)contentView.findViewById(R.id.searchView);
		searchAdd.setAvtiveSearch(false);
		searchAdd.setOnSearchAddListener(new OnSearchAddListener() {
			
			@Override
			public void OnShowKeyBoard() {
				layoutKeyboard.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void OnSearch(String search) {
				sendSearchToFrag(search);
			}
			
			@Override
			public void OnCloseKeyBoard() {
				layoutKeyboard.setVisibility(View.GONE);
			}

			@Override
			public void OnShowAddFrag() {
				
			}

			@Override
			public void OnShowDelFrag() {
				
			}

			@Override
			public void OnFilterCancel() {
				if(listSong != null){
					listSong.clear();
				}
				sendNotifyToFrag(false);
			}

			@Override
			public void OnFilterAll() {
				FragDeleteSong frag = (FragDeleteSong)manager.findFragmentByTag("ADD");
				if(frag != null){
					if(listSong != null){
						listSong.clear();
						listSong.addAll(frag.getAllList());
					}
					sendNotifyToFrag(true);
				}
			}
			
			@Override
			public void OnChangeFilter(int stateFilter) {
				changeFilterState(stateFilter);
			}
			
		});
		
		layoutKeyboard = (LinearLayout)contentView.findViewById(R.id.layout_backgrounp);
		layoutKeyboard.setVisibility(View.GONE);
		layoutNumber = (LinearLayout)contentView.findViewById(R.id.layoutNumber);
		keyBoard = (GroupKeyBoard)contentView.findViewById(R.id.GroupKeyBoard);
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
				}else if(namekey.equals("OK")){
					layoutKeyboard.setVisibility(View.GONE);
				}else if(namekey.equals(GroupKeyBoard.CLEAR)){
					searchAdd.clearOneChar();
				}else if(namekey.equals(GroupKeyBoard.SPACE)){
					searchAdd.addTextSearch(" ");
				}else{
					searchAdd.addTextSearch(namekey);
				}
			}
		});
		keyBoardNumber = (GroupKeyBoardNumber)contentView.findViewById(R.id.GroupKeyBoardNumber);
		keyBoardNumber.setOnClickKeyBoardListener(new OnClickKeyBoardListener() {
			
			@Override
			public void OnNameKey(String namekey, View view) {
				searchAdd.addTextSearch(namekey);
			}
		});
		
		showFragDelSong();
		
		return contentView;
	}

	@Override
	protected void OnShow() {
		MyApplication.flagInsideHidden = true;
	}

	@Override
	protected void OnDismiss() {
		MyApplication.flagInsideHidden = false;
//		removeFrag();
	}

	@Override
	protected void OnReceiveDpad(KeyEvent event, int key) {
		
	}
		
	private void showFragDelSong(){
		if(getStringFrag().equals("ADD")){
			layoutKeyboard.setVisibility(View.VISIBLE);
			return;
		}
		if(searchAdd != null){
			searchAdd.clearAllChar();	
		}
		if(!getStringFrag().equals("ADD")){
			FragmentTransaction transaction = manager.beginTransaction();
			FragDeleteSong song = new FragDeleteSong();
			song.setOnFragDeleteSongListener(new OnFragDeleteSongListener() {
				
				@Override
				public void OnClickDel(Song song) {
					notifyListSong(song);
				}

				@Override
				public void OnHideKeyBoard() {
					layoutKeyboard.setVisibility(View.GONE);
				}

				@Override
				public void OnUpdateTotal(int total) {
					String str = total + " " + context.getResources().getString(R.string.dialog_90xx_online_9);
					txtTotal.setText(str);
				}
			});
			transaction.replace(R.id.layoutFragHidden, song, "ADD");
			transaction.commit();
		}
	}
	
	private String getStringFrag(){
		Fragment frag = manager.findFragmentByTag("DEL");
		if(frag != null){
			return "DEL";
		}
		frag = manager.findFragmentByTag("ADD");
		if(frag != null){
			return "ADD";
		}
		frag = manager.findFragmentByTag("USB");
		if(frag != null){
			return "USB";
		}
		frag = manager.findFragmentByTag("SELECT_ADD");
		if(frag != null){
			return "SELECT_ADD";
		}
		return "";
	}
	
	public void removeFrag(){
		try {
			String frag = getStringFrag();
			if(frag.equals("")){
				return;
			}
			FragmentTransaction transaction = manager.beginTransaction();
			Fragment fragment = manager.findFragmentByTag(frag);
			transaction.remove(fragment);
			transaction.commit();	
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private String saveSearch = "";
	private void sendSearchToFrag(String where){
		saveSearch = where;
		FragDeleteSong frag = (FragDeleteSong)manager.findFragmentByTag("ADD");
		if(frag != null){
			frag.loadDatabase(where, listSong, searchAdd.getStateFilter());
		}
	}
	
	private void sendNotifyToFrag(boolean checkAll){
		FragDeleteSong frag = (FragDeleteSong)manager.findFragmentByTag("ADD");
		if(frag != null){
			frag.sendNotifyToFrag(checkAll);
		}
	}
	
	private void notifyListSong(Song song){
//		if(listSong != null){
//			if(listSong.contains(song)){
//				listSong.remove(song);
//				MyLog.e(TAB, "notifyListSong : remove");
//			}else{
//				listSong.add(song);
//				MyLog.e(TAB, "notifyListSong : add");
//			}
//		}
		
		if(song.isHidden()){
			ArrayList<Song> tempList = new ArrayList<Song>();
			tempList.add(song);
			listener.OnCancelAddSong(tempList);			
		} else {				
			ArrayList<Song> tempList = new ArrayList<Song>();
			tempList.add(song);
			listener.OnAddSong(tempList);
		}
		
	}
	
	public void sendNotifyCheckList(){
		FragDeleteSong frag = (FragDeleteSong)manager.findFragmentByTag("ADD");
		if(frag != null){
			frag.sendNotifyCheckList();
		}
		
		if(searchAdd != null){
			searchAdd.invalidate();
		}
		
		if(butAll != null){
			butAll.invalidate();
		}
		
		if(searchAdd.getStateFilter() == ViewSearchAdd.HIDDEN_HID){
			sendSearchToFrag(saveSearch);
		}
	}
	
	private void changeFilterState(int stateFilter){
		searchAdd.setStateFilter(stateFilter);
		sendSearchToFrag(saveSearch);
	}
	
}
