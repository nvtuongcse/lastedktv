package vn.com.sonca.sk90xxOnline;

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
import app.sonca.Dialog.ScoreLayout.BaseDialog;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.Keyboard.BaseKey;
import vn.com.sonca.Touch.Keyboard.GroupKeyBoard;
import vn.com.sonca.Touch.Keyboard.GroupKeyBoardNumber;
import vn.com.sonca.Touch.Keyboard.OnClickKeyBoardListener;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.params.Song;
import vn.com.sonca.sk90xxOnline.CheckAutoView.OnCheckAutoListener;
import vn.com.sonca.sk90xxOnline.FragDeleteSong.OnFragDeleteSongListener;
import vn.com.sonca.sk90xxOnline.ViewAddTop.OnViewAddTopListener;
import vn.com.sonca.sk90xxOnline.ViewSearchAdd.OnSearchAddListener;
import vn.com.sonca.zzzzz.MyApplication;

public class DialogSK90xxOnline extends BaseDialog {
	
	private String TAB = "DialogSK90xxOnline";
	private Context context;
	private FragmentManager manager;
	
	private OnDialogSK90xxOnlineListener listener;
	public interface OnDialogSK90xxOnlineListener {
		public void OnAddSong(ArrayList<Song> info);
		public void OnCancelAddSong(ArrayList<Song> info);
		public void OnUpdateAutoDownload(boolean flagCheck, ArrayList<Song> info);
		public void OnUpdateAutoMessage(String msg);
		public void OnSKChangeDialog(int dialogType);
	}
	public void setOnDialogSK90xxOnlineListener(OnDialogSK90xxOnlineListener listener){
		this.listener = listener;
	}

	private ArrayList<Song> listSong;
	public DialogSK90xxOnline(Context context, Window window, FragmentManager manager) {
		super(context, window);
		listSong = new ArrayList<Song>();
		this.manager = manager;
		this.context = context;
	}

	@Override
	protected int getIdLayout() {
		return R.layout.dialog_sk90xx_online;
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
	private ViewAddBut butLeft, butRight;
	private TextView txtTotal, txtAuto;
	private CheckAutoView checkAutoView;
	private LinearLayout layoutAuto;
	
	private boolean flagNet = false;
	private boolean flagAllowPick = false;
	
	private long startFrag = 0;
	
	@Override
	protected View getView(View contentView) {
		startFrag = System.currentTimeMillis();
		ViewAddTop viewAddTop = (ViewAddTop)contentView.findViewById(R.id.topView);
		viewAddTop.setOnViewAddTopListener(new OnViewAddTopListener() {
			
			@Override
			public void OnTopTaiBai() {
				
			}
			
			@Override
			public void OnTopBack() {
				if(System.currentTimeMillis() - startFrag < 2000){
					return;
				}
				
				removeFrag();
				dismissDialog(false);
			}
			
			@Override
			public void OnTopAnBai() {
				if(System.currentTimeMillis() - startFrag < 2000){
					return;
				}
				
				removeFrag();
				if(listener != null){
					listener.OnSKChangeDialog(1);
				}
			}
		});
		viewAddTop.setServerName(((MyApplication) context).getDeviceCurrent().getName());		
		
		butLeft = (ViewAddBut)contentView.findViewById(R.id.butLeft);
		butRight = (ViewAddBut)contentView.findViewById(R.id.butRight);
		txtTotal = (TextView)contentView.findViewById(R.id.txtTotal);
		txtAuto = (TextView)contentView.findViewById(R.id.txtAuto);
		checkAutoView = (CheckAutoView)contentView.findViewById(R.id.checkAutoView);
		layoutAuto = (LinearLayout)contentView.findViewById(R.id.layoutAuto);
		
		layoutAuto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(listener != null){	
					if(flagNet == false){
						listener.OnUpdateAutoMessage(context.getResources().getString(R.string.dialog_90xx_online_5b));	
					} else {
						boolean check = !checkAutoView.getCheckState();
						if(check){							
							listener.OnUpdateAutoDownload(check, MyApplication.listFullOnline);
						} else {							
							listener.OnUpdateAutoDownload(check, getFragListSong());
						}
						
					}
				}
			}
		});
		
		checkAutoView.setOnCheckAutoListener(new OnCheckAutoListener() {
			
			@Override
			public void OnCheckAuto(boolean flagCheck) {
				if(listener != null){	
					if(flagNet == false){
						listener.OnUpdateAutoMessage(context.getResources().getString(R.string.dialog_90xx_online_5b));	
					} else {
						boolean check = checkAutoView.getCheckState();
						if(check){							
							listener.OnUpdateAutoDownload(check, MyApplication.listFullOnline);
						} else {							
							listener.OnUpdateAutoDownload(check, getFragListSong());
						}	
						
					}
				}
			}
		});
		
		if(MyApplication.checkFullRealInternet(context)){
			flagNet = true;
			txtAuto.setText(context.getResources().getString(R.string.dialog_90xx_online_4));
		} else {
			flagNet = false;
			txtAuto.setText(context.getResources().getString(R.string.dialog_90xx_online_5));
		}
		
		if(TouchMainActivity.serverStatus != null){
			flagAllowPick = !TouchMainActivity.serverStatus.isAutoDownloadYouTube();
			if(flagNet == false){
				flagAllowPick = false;
			}
		}
		
		butLeft.setTitle(context.getResources().getString(R.string.dialog_90xx_online_2));
		butLeft.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View arg0) {
				if(listSong != null && listener != null){
					if(flagAllowPick){
						if(listSong.size() == 0){
							listener.OnUpdateAutoMessage(context.getResources().getString(R.string.dialog_90xx_online_11));	
						} else {
							listener.OnCancelAddSong(listSong);	
						}
						
					} else {
						displayMessage();
					}
					
				}
			}
		});
		
		butRight.setTitle(context.getResources().getString(R.string.dialog_90xx_online_3));
		butRight.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View arg0) {
				if(listSong != null && listener != null){					
					if(flagAllowPick){
						if(listSong.size() == 0){
							listener.OnUpdateAutoMessage(context.getResources().getString(R.string.dialog_90xx_online_11));	
						} else {
							listener.OnAddSong(listSong);
						}
						
					} else {
						displayMessage();
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
				FragDeleteSong frag = (FragDeleteSong)manager.findFragmentByTag("DEL");
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
		
	}

	@Override
	protected void OnDismiss() {
//		removeFrag();
	}

	@Override
	protected void OnReceiveDpad(KeyEvent event, int key) {
		
	}
		
	private void showFragDelSong(){
		if(getStringFrag().equals("DEL")){
			layoutKeyboard.setVisibility(View.VISIBLE);
			return;
		}
		if(searchAdd != null){
			searchAdd.clearAllChar();	
		}
		if(!getStringFrag().equals("DEL")){
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
			transaction.replace(R.id.layoutFragOnline, song, "DEL");
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
	
	public void sendNotifyUpdateFrag(){
		FragDeleteSong frag = (FragDeleteSong)manager.findFragmentByTag("DEL");
		if(frag != null){
			frag.sendNotifyUpdateFrag();
		}
	}
	
	public void sendNotifyUpdateFragFull(){
		FragDeleteSong frag = (FragDeleteSong)manager.findFragmentByTag("DEL");
		if(frag != null){
			frag.sendNotifyUpdateFrag();
		}
		
		if(saveSearch.equals("")){
			sendSearchToFrag(saveSearch);
		}
	}
	
	public void sendNotifyUpdateFragFullFull(){
		FragDeleteSong frag = (FragDeleteSong)manager.findFragmentByTag("DEL");
		if(frag != null){
			frag.sendNotifyUpdateFrag();
		}
		
		sendSearchToFrag(saveSearch);
	}
	
	private String saveSearch = "";
	private void sendSearchToFrag(String where){
		saveSearch = where;
		FragDeleteSong frag = (FragDeleteSong)manager.findFragmentByTag("DEL");
		if(frag != null){
			frag.loadDatabase(where, listSong, searchAdd.getStateFilter());
		}
	}
	
	private void sendNotifyToFrag(boolean checkAll){
		FragDeleteSong frag = (FragDeleteSong)manager.findFragmentByTag("DEL");
		if(frag != null){
			frag.sendNotifyToFrag(checkAll);
		}
	}
	
	public ArrayList<Song> getFragListSong(){
		FragDeleteSong frag = (FragDeleteSong)manager.findFragmentByTag("DEL");
		if(frag != null){
			return frag.getAllList();
		}
		
		return null;
	}
	
	private void notifyListSong(Song song){
		if(listSong != null){
			if(listSong.contains(song)){
				listSong.remove(song);
				MyLog.e(TAB, "notifyListSong : remove");
			}else{
				listSong.add(song);
				MyLog.e(TAB, "notifyListSong : add");
			}
			
			if(listSong.size() == 0){
				butLeft.setFlagEmpty(true);
				butRight.setFlagEmpty(true);
			} else {
				butLeft.setFlagEmpty(false);
				butRight.setFlagEmpty(false);
			}
			
			if(searchAdd.getStateFilter() == ViewSearchAdd.ONLINE_SEL){
				sendSearchToFrag(saveSearch);
			}
		}
	}
	
	public boolean getAutoStatus(){
		return checkAutoView.getCheckState();
	}
	
	public void setAutoStatus(boolean status){
		butLeft.invalidate();
		butRight.invalidate();
		if(searchAdd.getStateFilter() == ViewSearchAdd.ONLINE_SEL){
			changeFilterState(ViewSearchAdd.ONLINE_ALL);
			if(status){
				FragDeleteSong frag = (FragDeleteSong)manager.findFragmentByTag("DEL");
				if(frag != null){
					if(listSong != null){
						listSong.clear();
						if(MyApplication.listFullOnline.size() > 0){
							listSong.addAll(MyApplication.listFullOnline);	
						} else {
							listSong.addAll(frag.getAllList());
						}
					}
					sendNotifyToFrag(true);
				}
			}
		}
		searchAdd.invalidate();
		checkAutoView.setCheckState(status);
		flagAllowPick = !status;
		if(flagNet == false){
			flagAllowPick = false;
		}
	}

	private void displayMessage(){
		if(flagNet == false){
			listener.OnUpdateAutoMessage(context.getResources().getString(R.string.dialog_90xx_online_5b));	
		} else {
			if(flagAllowPick == false){
				listener.OnUpdateAutoMessage(context.getResources().getString(R.string.dialog_90xx_online_10));	
			}
		}
		
	}
	
	private void changeFilterState(int stateFilter){
		searchAdd.setStateFilter(stateFilter);
		sendSearchToFrag(saveSearch);
	}
	
	public void onYesAuto(){
		FragDeleteSong frag = (FragDeleteSong)manager.findFragmentByTag("DEL");
		if(frag != null){
			if(listSong != null){
				listSong.clear();
				listSong.addAll(frag.getAllList());
			}
			sendNotifyToFrag(true);
		}
		
	}
	
	public void onYesAutoNot(){
		if(listSong != null){
			listSong.clear();
		}
		sendNotifyToFrag(false);
		
		if(listSong.size() == 0){
			butLeft.setFlagEmpty(true);
			butRight.setFlagEmpty(true);
		} else {
			butLeft.setFlagEmpty(false);
			butRight.setFlagEmpty(false);
		}
	}
	
	public void onUpdateButton(){
		butLeft.invalidate();
	}
	
}
