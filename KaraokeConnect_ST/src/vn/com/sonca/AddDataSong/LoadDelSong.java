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
import vn.com.sonca.utils.PinyinHelper;
import vn.com.sonca.utils.AppConfig.MEDIA_TYPE;
import vn.com.sonca.zzzzz.MyApplication;

public class LoadDelSong extends Thread {
	
	public static final int ROWSTART = 200;
	protected static final int ROWNEXT = 100;

	protected static final int START = 1;
	protected static final int LOADING = 2;
	protected static final int STOP = 3;
	protected static final int FINISH = 4;
	
	
	private String TAB = "LoadDelSong";
	
	private int IdThread = 0;
	private Object mainLock = new Object();
	
	private OnLoadDelListener listener;
	public interface OnLoadDelListener {
		public void OnStartLoad(int idThread);
		public void OnLoading();
		public void OnFinish();
	}
	
	public void setOnLoadDelListener(OnLoadDelListener listener){
		this.listener = listener;
	}

	private Context context;
	private String where;
	private ArrayList<Song> listSongFragment;
	public LoadDelSong(int IdThread, Context context, String where, ArrayList<Song> listSongFragment) {
		this.where = where;
		this.context = context;
		this.IdThread = IdThread;
		this.listSongFragment = listSongFragment;
	}
	
	private void OnRunLoad() throws Exception {
		Cursor cursor = null;
		
		ArrayList<Song> songMunberID = new ArrayList<Song>();
		if (!checkNotLetter(where) && !where.equals("")) {
			cursor = DBInterface.DBGetSongNumberCursor(context, where, 0, 0, MEDIA_TYPE.ALL);
			if (isCancel()) {
				cursor.close();
				cursor = null;
				setRunning(false);
				return;
			}
			if (cursor != null) {
				if (moveToFirstCursor(cursor)) {
					for (int i = 0; i < 1; i++) {
						if (isCancel()) {
							cursor.close();
							cursor = null;
							publishProgress(FINISH);
							setRunning(false);
							return;
						}
						Song song = new Song();
						parseSongInfoQueryResult(song, cursor);
						songMunberID.add(song);
						if (!cursor.moveToNext()) {
							break;
						}
					}
				}
				cursor.close();
				cursor = null;
				for (int i = 0; i < songMunberID.size(); i++) {
					Song song = songMunberID.get(i);
					String textID = "";
					boolean boolRed = song.getId() == song.getIndex5();
					if (MyApplication.intWifiRemote == MyApplication.SONCA_KARTROL
							&& MyApplication.intRemoteModel != 0) {
						textID = song.getIndex5() + "";
					} else {
						if (boolRed) {
							textID = convertIdSong(song.getId()) + " | -";
						} else {
							textID = convertIdSong(song.getId()) + " | " + song.getIndex5();
						}
					}
					SpannableString wordtoSpan = new SpannableString(textID);
					if (where.equals("" + song.getId())) {
						wordtoSpan.setSpan(new ForegroundColorSpan(Color.GREEN), 6 - where.length(), 6,
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					} else if (where.equals("" + song.getIndex5())) {
						wordtoSpan.setSpan(new ForegroundColorSpan(Color.GREEN), 9, textID.length(),
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
					song.setSpannableNumber(wordtoSpan);
				}
			}
		}		

		MyLog.i(TAB, "OnRunLoad() - 1");
		int tempRowStart = ROWNEXT;
		int countPage = 0;

		MyLog.e(TAB, "Loading STYLE OLD");
		while (!isCancel()) {
			cursor = DBSearch(where, countPage, tempRowStart);
			if (isCancel()) {
				cursor.close();
				cursor = null;
				publishProgress(FINISH);
				setRunning(false);
				return;
			}
			countPage++;
			if (cursor != null) {
				if (moveToFirstCursor(cursor)) {
					ArrayList<Song> list = new ArrayList<Song>();
					for (int i = 0; i < ROWNEXT; i++) {
						if (isCancel()) {
							cursor.close();
							cursor = null;
							publishProgress(FINISH);
							setRunning(false);
							return;
						}
						Song song = new Song();
						parseSongInfoQueryResult(song, cursor);
						list.add(song);
						if (!cursor.moveToNext()) {
							break;
						}
					}
					cursor.close();
					cursor = null;
					if (!songMunberID.isEmpty()) {
						list.addAll(0, songMunberID);
					}
					setListSong(list);
					publishProgress(LOADING);
				} else {
					cursor.close();
					cursor = null;
					ArrayList<Song> list = new ArrayList<Song>();
					if (!songMunberID.isEmpty()) {
						list.addAll(0, songMunberID);
						setListSong(list);
						publishProgress(LOADING);
					}	
				}
			}
			if (checkSize() < ROWNEXT) {
				MyLog.i(TAB, "Full Data : " + checkSize());
				publishProgress(FINISH);
				setRunning(false);
				return;
			} else {
				MyLog.i(TAB, "Next Loaded : " + checkSize());
			}
			waitLoad();
			if (isCancel()) {
				publishProgress(FINISH);
				setRunning(false);
				return;
			}
			MyLog.i(TAB, "Loading....");
		}
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

	protected void parseSongInfoQueryResult(Song s, Cursor cursorResult) {
		s.setId(cursorResult.getInt(0));
		s.setIndex5(cursorResult.getInt(1));
		String name = cursorResult.getString(2);
		s.setName(name);
		// s.setShortName(cursorResult.getString(3));
		String nameraw = cursorResult.getString(3);
		s.setLyric(cutText(40, cursorResult.getString(4)));
		s.setMediaType(MEDIA_TYPE.values()[cursorResult.getInt(5)]);
		s.setRemix(cursorResult.getInt(6) == 1);
		s.setFavourite(cursorResult.getInt(7) == 1);
		s.setTypeABC(cursorResult.getInt(8));
		s.setExtraInfo(cursorResult.getInt(9));
		// -----------------//
		String nameSinger = cursorResult.getString(10);
		s.setSinger(new Singer(nameSinger, cutText(14, nameSinger)));
		String singerID = cursorResult.getString(11);
		String[] idxs = singerID.split(",");
		int[] idxInt = new int[idxs.length];
		for (int i = 0; i < idxInt.length; i++) {
			idxInt[i] = Integer.parseInt(idxs[i]);
		}
		s.setSingerId(idxInt);
		// -----------------//
		String nameMusician = cursorResult.getString(12);
		s.setMusician(new Musician(nameMusician, cutText(14, nameMusician)));
		String musicianID = cursorResult.getString(13);
		idxs = musicianID.split(",");
		idxInt = new int[idxs.length];
		for (int i = 0; i < idxInt.length; i++) {
			idxInt[i] = Integer.parseInt(idxs[i]);
		}
		s.setMusicianId(idxInt);

		// -----------------//

		String textID = "";
		boolean boolRed = s.getId() == s.getIndex5();
		if (MyApplication.intWifiRemote == MyApplication.SONCA_KARTROL && MyApplication.intRemoteModel != 0) {
			textID = s.getIndex5() + "";
		} else {
			if (boolRed) {
				textID = convertIdSong(s.getId()) + " | -";
			} else {
				textID = convertIdSong(s.getId()) + " | " + s.getIndex5();
			}
		}
		SpannableString wordtoSpan = new SpannableString(textID);
		s.setSpannableNumber(wordtoSpan);

		// -----------------//

		if (where.equals("")) {
			s.setSpannable(createSpannable(name, nameraw, where, Color.GREEN));
			s.setSpannableGreen(createSpannable(name, nameraw, where, Color.argb(255, 250, 145, 0)));
		} else {
			CharSequence wh1 = name.subSequence(0, 1);
			CharSequence wh2 = name.subSequence(name.length() - 1, name.length());
			if (PinyinHelper.checkChinese(wh1.toString()) || PinyinHelper.checkChinese(wh2.toString())) {
				if (name.contains(where)) {
					s.setSpannable(createSpannable(name, PinyinHelper.replaceAll(name), where, Color.GREEN));
					s.setSpannableGreen(
							createSpannable(name, PinyinHelper.replaceAll(name), where, Color.argb(255, 250, 145, 0)));
				} else {
					s.setSpannable(createSpannableChinese(name, where, Color.GREEN));
					s.setSpannableGreen(createSpannableChinese(name, where, Color.argb(255, 250, 145, 0)));
				}
			} else {
				if (nameraw.equals("")) {
					nameraw = PinyinHelper.replaceAll(name);
				}
				s.setSpannable(createSpannable(name, nameraw, where, Color.GREEN));
				s.setSpannableGreen(createSpannable(name, nameraw, where, Color.argb(255, 250, 145, 0)));
			}
		}
	}

	private int colorHighlight_YouTube = Color.argb(255, 255, 255, 0);

	protected void parseSongInfoQueryResult_YouTube(Song s, Cursor cursorResult) {
		s.setId(cursorResult.getInt(0));
		s.setIndex5(cursorResult.getInt(1));
		String name = cursorResult.getString(2);
		s.setName(name);
		// s.setShortName(cursorResult.getString(3));
		String nameraw = cursorResult.getString(3);
		s.setLyric(cutText(40, cursorResult.getString(4)));
		s.setMediaType(MEDIA_TYPE.values()[cursorResult.getInt(5)]);
		s.setRemix(cursorResult.getInt(6) == 1);
		s.setFavourite(cursorResult.getInt(7) == 1);
		s.setTypeABC(cursorResult.getInt(8));
		s.setExtraInfo(cursorResult.getInt(9));
		// -----------------//
		Singer singer = new Singer("-", "-");
		s.setSinger(singer);
		int[] idxInt = new int[1];
		idxInt[0] = 0;
		s.setSingerId(idxInt);
		// -----------------//
		s.setPlayLink(cursorResult.getString(10));
		s.setDownLink(cursorResult.getString(11));
		// -----------------//

		if (where.equals("")) {
			s.setSpannable(createSpannable(name, nameraw, where, colorHighlight_YouTube));
			s.setSpannableGreen(createSpannable(name, nameraw, where, Color.argb(255, 250, 145, 0)));
		} else {
			CharSequence wh1 = name.subSequence(0, 1);
			CharSequence wh2 = name.subSequence(name.length() - 1, name.length());
			if (PinyinHelper.checkChinese(wh1.toString()) || PinyinHelper.checkChinese(wh2.toString())) {
				if (name.contains(where)) {
					s.setSpannable(createSpannable(name, PinyinHelper.replaceAll(name), where, colorHighlight_YouTube));
					s.setSpannableGreen(
							createSpannable(name, PinyinHelper.replaceAll(name), where, Color.argb(255, 250, 145, 0)));
				} else {
					s.setSpannable(createSpannableChinese(name, where, colorHighlight_YouTube));
					s.setSpannableGreen(createSpannableChinese(name, where, Color.argb(255, 250, 145, 0)));
				}
			} else {
				if (nameraw.equals("")) {
					nameraw = PinyinHelper.replaceAll(name);
				}
				s.setSpannable(createSpannable(name, nameraw, where, colorHighlight_YouTube));
				s.setSpannableGreen(createSpannable(name, nameraw, where, Color.argb(255, 250, 145, 0)));
			}
		}
	}

	private Spannable createSpannableChinese(String name, String where, int color) {
		Spannable wordtoSpan = new SpannableString(name);
		int countIdx = 0;
		for (int i = 0; i < name.length(); i++) {
			if (countIdx >= where.length()) {
				break;
			}
			String strChar = name.substring(i, i + 1);
			String strSearchChar = where.substring(countIdx, countIdx + 1);
			if (strChar.equals(strSearchChar)) {
				wordtoSpan.setSpan(new ForegroundColorSpan(color), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				countIdx++;
			} else {
				if (PinyinHelper.checkChinese(strChar)) {
					wordtoSpan.setSpan(new ForegroundColorSpan(color), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					countIdx++;
				} else {
					continue;
				}
			}
		}
		return wordtoSpan;
	}

	private Spannable createSpannable(String textSong, String nameraw, String textSearch, int color) {
		textSearch.trim();
		ArrayList<Integer> listOffset = new ArrayList<Integer>();
		Spannable wordtoSpan;
		if (textSong.equals("")) {
			wordtoSpan = new SpannableString("");
		} else {
			if (textSearch.equals("")) {
				return new SpannableString(textSong);
			}
			textSearch.trim();
			String newString = textSong.replaceAll("[ &+=_,-/]", "*");
			StringBuffer buffer = new StringBuffer(newString);
			// -------------//
			String[] strings = buffer.toString().split("[*]");
			if (strings.length < textSearch.length()) {
				int offset = nameraw.indexOf(textSearch);
				wordtoSpan = new SpannableString(textSong);
				if (offset != -1) {
					wordtoSpan.setSpan(new ForegroundColorSpan(color), offset, offset + textSearch.length(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
				return wordtoSpan;
			}
			int count = 0;
			for (int i = 0; i < strings.length; i++) {
				int size = strings[i].length();
				if (size <= 0) {
					count += 1;
				} else {
					listOffset.add(count);
					count += size + 1;
				}
				if (listOffset.size() >= textSearch.length()) {
					break;
				}
			}
			// -------------//
			wordtoSpan = new SpannableString(textSong);
			for (int i = 0; i < listOffset.size(); i++) {
				int offset = getIndex(textSong, listOffset.get(i));
				if (nameraw.charAt(offset) != textSearch.charAt(i)) {
					int of = nameraw.indexOf(textSearch);
					SpannableString word = new SpannableString(textSong);
					if (of != -1) {
						word.setSpan(new ForegroundColorSpan(color), of, of + textSearch.length(),
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
					return word;
				} else {
					wordtoSpan.setSpan(new ForegroundColorSpan(color), offset, offset + 1,
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
		}
		return wordtoSpan;
	}

	private int getIndex(String string, int index) {
		switch (string.charAt(index)) {
		case '(':
			return index + 1;
		case '`':
			return index + 1;
		case '[':
			return index + 1;
		default:
			return index;
		}

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
				listSongs = new ArrayList<Song>();
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
			case FINISH:
				if (listener != null) {
					listener.OnFinish();
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

	private ArrayList<Song> listSongs;

	protected void setListSong(ArrayList<Song> list) {
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
