package vn.com.sonca.AddDataSong;

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
import app.sonca.Dialog.ScoreLayout.BaseDialog;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.AddDataSong.FragAddSong.OnFragAddSongListener;
import vn.com.sonca.AddDataSong.FragDeleteSong.OnFragDeleteSongListener;
import vn.com.sonca.AddDataSong.FragScanUSB.OnScanUSBListener;
import vn.com.sonca.AddDataSong.FragSelectAdd.OnFragSelectAddListener;
import vn.com.sonca.AddDataSong.ViewSearchAdd.OnSearchAddListener;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.Keyboard.BaseKey;
import vn.com.sonca.Touch.Keyboard.GroupKeyBoard;
import vn.com.sonca.Touch.Keyboard.GroupKeyBoardNumber;
import vn.com.sonca.Touch.Keyboard.OnClickKeyBoardListener;
import vn.com.sonca.params.ItemUSB;
import vn.com.sonca.params.Song;
import vn.com.sonca.params.SongInfo;

public class DialogAddSongData extends BaseDialog {
	
	private String TAB = "DialogAddSongData";
	private Context context;
	private FragmentManager manager;
	
	private OnDialogAddSongListener listener;
	public interface OnDialogAddSongListener {
		public void OnAddSong(ArrayList<SongInfo> info);
		public void OnDeleteSong(ArrayList<Song> list);
		public void OnClickUSB(ItemUSB usb);
		public void OnShowUSB();
	}
	public void setOnDialogAddSongListener(OnDialogAddSongListener listener){
		this.listener = listener;
	}

	private ArrayList<Song> listSong;
	private ArrayList<SongInfo> listInfo;
	public DialogAddSongData(Context context, Window window, FragmentManager manager) {
		super(context, window);
		listSong = new ArrayList<Song>();
		listInfo = new ArrayList<SongInfo>();
		this.manager = manager;
		this.context = context;
	}

	@Override
	protected int getIdLayout() {
		// TODO Auto-generated method stub
		return R.layout.dialog_addsongdata;
	}

	@Override
	protected int getTimerShow() {
		// TODO Auto-generated method stub
		return 0;
	}

	private ViewSearchAdd searchAdd;
	private GroupKeyBoard keyBoard;
	private LinearLayout layoutNumber;
	private LinearLayout layoutKeyboard;
	private GroupKeyBoardNumber keyBoardNumber;
	private ViewAddBut butLeft, butRight, butSelectAll;
	@Override
	protected View getView(View contentView) {
		ViewAddTop viewAddTop = (ViewAddTop)contentView.findViewById(R.id.topView);
		viewAddTop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String frag = getStringFrag();
				if(frag.equals("ADD")){
					showFragSelectThemBai();
				} else{
					dismissDialog(false);
				}
			}
		});
		
		butLeft = (ViewAddBut)contentView.findViewById(R.id.butLeft);
		butRight = (ViewAddBut)contentView.findViewById(R.id.butRight);
		butSelectAll = (ViewAddBut)contentView.findViewById(R.id.butSelectAll);
		
		butLeft.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View arg0) {
				dismissDialog(false);
			}
		});
		butRight.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View arg0) {
				if(listener != null){
					String frag = getStringFrag();
					if(frag.equals("DEL") && listSong != null){
						listener.OnDeleteSong(listSong);
					}else if(frag.equals("ADD") && listInfo != null){
						listener.OnAddSong(listInfo);
					}else{}
				}
			}
		});
		
		butSelectAll.setTitle(context.getResources().getString(R.string.dialog_add_song_8));
		butSelectAll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragAddSong frag = (FragAddSong)manager.findFragmentByTag("ADD");
				if(frag != null){
					frag.selectAllSong();
					if(listInfo != null){
						listInfo.clear();
						listInfo.addAll(frag.getAllList());
					}
				}
			}
		});
		
		showFragSelectThemBai();
		butLeft.setTitle(context.getResources().getString(R.string.dialog_add_song_4));
		
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
				showFragSelectThemBai();
			}

			@Override
			public void OnShowDelFrag() {
				showFragDelSong();
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
		
		return contentView;
	}

	@Override
	protected void OnShow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void OnDismiss() {
		removeFrag();
	}

	@Override
	protected void OnReceiveDpad(KeyEvent event, int key) {
		// TODO Auto-generated method stub
		
	}
	
	private void showFragDelSong(){
		if(getStringFrag().equals("DEL")){
			layoutKeyboard.setVisibility(View.VISIBLE);
			return;
		}
		if(searchAdd != null){
			searchAdd.clearAllChar();	
		}
		if(butSelectAll != null){
			butSelectAll.setVisibility(View.GONE);
		}
		
		if(!getStringFrag().equals("DEL")){
			if(butRight != null){
				butRight.setTitle(context.getResources().getString(R.string.dialog_add_song_6));
			}
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
			});
			transaction.replace(R.id.layoutFrag, song, "DEL");
			transaction.commit();
		}
	}
	
	private void showFragAddSong(){
		if(butSelectAll != null){
			butSelectAll.setVisibility(View.VISIBLE);
		}
		
		if(!getStringFrag().equals("ADD")){
			if(butRight != null){
				butRight.setTitle(context.getResources().getString(R.string.dialog_add_song_5));
			}
			FragmentTransaction transaction = manager.beginTransaction();
			FragAddSong song = new FragAddSong();
			song.setOnFragAddSongListener(new OnFragAddSongListener() {
				
				@Override
				public void OnAddClick(SongInfo info) {
					notifyListInfo(info);
				}
			});
			transaction.replace(R.id.layoutFrag, song, "ADD");
			transaction.commit();
		}
	}
	
	private void showFragSelectThemBai(){
		if(butSelectAll != null){
			butSelectAll.setVisibility(View.GONE);
		}
		if(!getStringFrag().equals("SELECT_ADD")){
			if(butRight != null){
				butRight.setTitle(context.getResources().getString(R.string.dialog_add_song_5));
			}
			
			FragmentTransaction transaction = manager.beginTransaction();
			FragSelectAdd frag = new FragSelectAdd();
			frag.setOnFragSelectAddListener(new OnFragSelectAddListener() {
				
				@Override
				public void OnItemClick(int pos) {
					if(pos == 0){ // USB
						showFragUSB();
					} else { // NET
						if(listener != null){
							ItemUSB usb = new ItemUSB(99, "NET");
							listener.OnClickUSB(usb);
							showFragAddSong();
						}
					}
				}
			});
			transaction.replace(R.id.layoutFrag, frag, "SELECT_ADD");
			transaction.commit();
		}
	}
	
	private void showFragUSB(){
		if(butSelectAll != null){
			butSelectAll.setVisibility(View.GONE);
		}
		
		if(!getStringFrag().equals("USB")){
			if(listener != null){
				listener.OnShowUSB();
			}
			if(butRight != null){
				butRight.setTitle(context.getResources().getString(R.string.dialog_add_song_5));
			}
			FragmentTransaction transaction = manager.beginTransaction();
			FragScanUSB song = new FragScanUSB();
			song.setOnScanUSBListener(new OnScanUSBListener() {
				
				@Override
				public void OnItemClick(ItemUSB usb) {
					if(listener != null){
						listener.OnClickUSB(usb);
						showFragAddSong();
					}
				}
			});
			transaction.replace(R.id.layoutFrag, song, "USB");
			transaction.commit();
		}
	}
	
	public void sendListUSB(ArrayList<ItemUSB> list){
		MyLog.e(TAB, "sendListUSB : " + list.size());
		FragScanUSB frag = (FragScanUSB)manager.findFragmentByTag("USB");
		if(frag != null){
			frag.showDataFromSK(list);
		}
	}
	
	public void sendListAddSong(ArrayList<SongInfo> list){
		MyLog.e(TAB, "sendListAddSong : " + list.size());
		FragAddSong frag = (FragAddSong)manager.findFragmentByTag("ADD");
		if(frag != null){
			frag.loadDatabase(list);
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
	
	private void removeFrag(){
		String frag = getStringFrag();
		if(frag.equals("")){
			return;
		}
		FragmentTransaction transaction = manager.beginTransaction();
		Fragment fragment = manager.findFragmentByTag(frag);
		transaction.remove(fragment);
		transaction.commit();
	}
	
	private void sendSearchToFrag(String where){
		FragDeleteSong frag = (FragDeleteSong)manager.findFragmentByTag("DEL");
		if(frag != null){
			frag.loadDatabase(where);
		}
	}
	
	private void notifyListInfo(SongInfo info){
		if(listInfo != null){
			if(listInfo.contains(info)){
				listInfo.remove(info);
				MyLog.e(TAB, "notifyListInfo : remove");
			}else{
				listInfo.add(info);
				MyLog.e(TAB, "notifyListInfo : add");
			}
		}
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
		}
	}

}
