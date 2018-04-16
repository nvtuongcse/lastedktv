package vn.com.sonca.AddDataSong;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.Language.LanguageStore;
import vn.com.sonca.database.DBInterface;
import vn.com.sonca.database.DBInstance.SearchMode;
import vn.com.sonca.params.Musician;
import vn.com.sonca.params.Singer;
import vn.com.sonca.params.Song;
import vn.com.sonca.params.SongInfo;
import vn.com.sonca.utils.PinyinHelper;
import vn.com.sonca.utils.AppConfig.MEDIA_TYPE;

public class LoadAddSong extends Thread {
	
	public static final int ROWSTART = 200;
	protected static final int ROWNEXT = 100;

	protected static final int START = 1;
	protected static final int LOADING = 2;
	protected static final int STOP = 3;
	protected static final int FINISH = 4;
	
	
	private String TAB = "LoadDelSong";
	
	private int IdThread = 0;
	private Object mainLock = new Object();
	
	private OnLoadAddListener listener;
	public interface OnLoadAddListener {
		public void OnStartLoad(int idThread);
		public void OnLoading();
	}
	
	public void setOnLoadAddListener(OnLoadAddListener listener){
		this.listener = listener;
	}

	private Context context;
	private String where;
	private ArrayList<SongInfo> listSongFragment;
	public LoadAddSong(int IdThread, Context context, 
		String where, ArrayList<SongInfo> listSongFragment) {
		this.where = where;
		this.context = context;
		this.IdThread = IdThread;
		this.listSongFragment = listSongFragment;
	}
	
	private void OnRunLoad() throws Exception {
		
		MyLog.i(TAB, "OnRunLoad() - 1");
		
		ArrayList<SongInfo> list = new ArrayList<SongInfo>();
//		for (int i = 0; i < 100; i++) {
//			SongInfo info = new SongInfo();
//			info.setId(i);
//			info.setSinger("Singer " + i);
//			info.setTitle("Bai Ca So " + i);
//			list.add(info);
//		}
		setListSong(list);
		publishProgress(START);

	}

	protected void publishProgress(int idMessage){
		// MyLog.i(TAB, IdThread + " - Send Message : " + idMessage + " : " + where);
		handler.sendEmptyMessageDelayed(idMessage, 5);
	}
	
	
	private Cursor DBSearch(String where, int pade, int sum){
		LanguageStore store = new LanguageStore(context); 
		ArrayList<String>langIDs = store.getListIDActive(); 
		int[] intIDs = new int[langIDs.size()]; 
		for(int i = 0; i < langIDs.size(); i++) {
			intIDs[i] = Integer.parseInt(langIDs.get(i)); 
		}
		return DBInterface.DBGetSongCursor(context, intIDs, where, SearchMode.MODE_MIXED, MEDIA_TYPE.ALL, pade*sum, sum);
	}
	
///////////////////////// - PARSE - ////////////////////////////

	private void parseSongInfoQueryResult(SongInfo songInfo, Cursor cursor){
		
	}
	
	private String cutText(int maxLength, String content) {
		if (content == null) {
			return "";
		}
		if (content.length() <= maxLength) {
			return content;
		}
		return content.substring(0, maxLength) + "...";
	}

	protected String convertIdSong(int id) {
		String stringId = String.valueOf(id);
		switch (stringId.length()) {
		case 1:
			return "00000" + stringId;
		case 2:
			return "0000" + stringId;
		case 3:
			return "000" + stringId;
		case 4:
			return "00" + stringId;
		case 5:
			return "0" + stringId;
		default:
			break;
		}
		return stringId;
	}

///////////////////////// - THREAD - ////////////////////////////

	@Override
	public void run() {
		MyLog.e(TAB, "NEW THREAD - " + IdThread + " - " + where);
		synchronized (mainLock) {
			try {
				setRunning(true);
				if (isCancel()) {
					setRunning(false);
					MyLog.e(TAB, "CANCEL THREAD - " + IdThread + " - " + where);
					return;
				}
				isCancel = false;
				listSongs = new ArrayList<SongInfo>();
				MyLog.e(TAB, "RUN THREAD - " + IdThread + " - " + where);
				OnRunLoad();
				MyLog.e(TAB, "STOP THREAD - " + IdThread + " - " + where);
				publishProgress(STOP);
				setRunning(false);
			} catch (Exception e) {
				e.printStackTrace();
				publishProgress(STOP);
				setRunning(false);
			}
		}
	}

	private final Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case START:
				if(listener != null){
					listSongFragment.addAll(listSongs);
					listener.OnStartLoad(IdThread);
				}
				break;
			case LOADING:
				if(listener != null){
					listSongFragment.addAll(listSongs);
					listener.OnLoading();
				}
				break;
			}
		};
	};

	//////////////////////////////////////////////////////

	protected boolean checkNotLetter(String where) {
		for (int i = 0; i < where.length(); i++) {
			if (Character.isLetter(where.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	private ArrayList<SongInfo> listSongs;

	protected void setListSong(ArrayList<SongInfo> list) {
		listSongs.clear();
		listSongs.addAll(list);
	}

	protected int checkSize() {
		if (listSongs == null) {
			return 0;
		} else {
			return listSongs.size();
		}
	}

	private boolean isCancel = false;

	protected boolean isCancel() {
		return isCancel;
	}

	public void cancel(boolean value) {
		isCancel = value;
		synchronized (mainLock) {
			mainLock.notifyAll();
		}
	}

	protected void waitLoad() throws InterruptedException {
		synchronized (mainLock) {
			mainLock.wait();
		}
	}

	public void loadNextPage() {
		// MyLog.e(TAB, "loadNextPage");
		synchronized (mainLock) {
			mainLock.notifyAll();
		}
	}

	public void execute() {
		this.start();
	}

	private boolean isRunnnig = false;

	public boolean isRunning() {
		return isRunnnig;
	}

	protected void setRunning(boolean value) {
		isRunnnig = value;
	}

	protected boolean moveToFirstCursor(Cursor cursor) {
		boolean re = false;
		if (cursor != null) {
			long tgian = System.currentTimeMillis();
			re = cursor.moveToFirst();
		}
		return re;
	}

}
