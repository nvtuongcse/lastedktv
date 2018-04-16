package vn.com.sonca.zktv.FragData;

import java.util.ArrayList;
import java.util.TimerTask;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.params.Musician;
import vn.com.sonca.params.Singer;
import vn.com.sonca.params.Song;
import vn.com.sonca.utils.PinyinHelper;
import vn.com.sonca.utils.AppConfig.MEDIA_TYPE;
import vn.com.sonca.zzzzz.MyApplication;

public abstract class BaseTaskSong extends TimerTask {
	protected String TAB = "BaseLoadSong";
	public static final int ROWSTART = 200;
	public static final int ROWSTART_1CHAR = 50;
	public static final int ROWSTART_2CHAR = 20;
	public static final int ROWSTART_3CHAR = 20;
	protected final int ROWNEXT = 100;
	public static final int ROWTRIGGER = 150;
	public static final int ROWTRIGGER_1CHAR = 30;
	public static final int ROWTRIGGER_2CHAR = 10;
	public static final int ROWTRIGGER_3CHAR = 10;
	
	protected static final int SUCCESS = 1;
	protected static final int ERROR = -1;
	protected static final int NOPAGE = 0;

	protected static final int LOADING_FULL = 5;
	protected static final int LOADING_NEXT = 6;
	protected static final int LOADING_FIN = 7;
	
	private OnBaseTaskListener listener;
	public interface OnBaseTaskListener {
		public void OnLoadFinish(int status);
	}
	public void setOnBaseTaskListener(OnBaseTaskListener listener){
		this.listener = listener;
	}
		
	public BaseTaskSong(ArrayList<Song> listSongs) {
		this.listSongs = listSongs;
	}
	
	protected abstract int runTheard();

	@Override
	public void run() {
		int status = runTheard();
		handler.sendEmptyMessage(status);
	}
	
	private final Handler handler = new Handler(){
		public void handleMessage(Message msg) {
			if(listener != null){
				listener.OnLoadFinish(msg.what);
			}
		};
	};
	
	
///////////////////////// - PARSE - ////////////////////////////

	protected void parseSongInfoQueryResult(String where, Song s, Cursor cursorResult) {
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
			//-----------------//
		String nameSinger = cursorResult.getString(10);
		s.setSinger(new Singer(nameSinger, cutText(14, nameSinger)));
		String singerID = cursorResult.getString(11);
		String[] idxs = singerID.split(",");
		int[] idxInt = new int[idxs.length];
		for (int i = 0; i < idxInt.length; i++) {
			idxInt[i] = Integer.parseInt(idxs[i]);
		}
		s.setSingerId(idxInt);
			//-----------------//
		String nameMusician = cursorResult.getString(12);
		s.setMusician(new Musician(nameMusician, cutText(14, nameMusician)));
		String musicianID = cursorResult.getString(13);
		idxs = musicianID.split(",");
		idxInt = new int[idxs.length];
		for (int i = 0; i < idxInt.length; i++) {
			idxInt[i] = Integer.parseInt(idxs[i]);
		}
		s.setMusicianId(idxInt);
		
			//-----------------//
		
		if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && (MyApplication.flagSmartK_801 || MyApplication.flagSmartK_CB)){
			boolean check = MyApplication.checkOfflineSong(s.getId());
			s.setOfflineSong(check);
		}
		
		boolean flagSpecial801 = false;
		if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && (MyApplication.flagSmartK_801 || MyApplication.flagSmartK_CB) 
				&& s.isOfflineSong() == false){
			flagSpecial801 = true;
		}
		
		//-----------------//
		String textID = "";
		boolean boolRed = s.getId() == s.getIndex5();
		if(MyApplication.intWifiRemote == MyApplication.SONCA_KARTROL 
				&& MyApplication.intRemoteModel != 0){
			textID = s.getIndex5() + "";
		} else if (flagSpecial801){
			textID = convertIdSong(s.getId()) + " | ";
		} else {
			if (boolRed) {
				textID = convertIdSong(s.getId()) + " | -";
			} else {			
				textID = convertIdSong(s.getId()) + " | " + s.getIndex5();
			}
		}
		SpannableString wordtoSpan = new SpannableString(textID);
		s.setSpannableNumber(wordtoSpan);
		
			//-----------------//
		
		int colorHighlight = Color.GREEN;
		if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			colorHighlight = colorHighlight_light;
		}
		
		if (where.equals("")) {
			s.setSpannable(createSpannable(name, nameraw, where, colorHighlight));
			s.setSpannableGreen(createSpannable(name, nameraw, where, 
					Color.argb(255, 250, 145, 0)));
		} else {
			CharSequence wh1 = name.subSequence(0, 1);
			CharSequence wh2 = name.subSequence(name.length() - 1, name.length());
			if (PinyinHelper.checkChinese(wh1.toString()) || 
				PinyinHelper.checkChinese(wh2.toString())) {
				if(name.contains(where)){
					s.setSpannable(createSpannable(name, 
							PinyinHelper.replaceAll(name), where, colorHighlight));
					s.setSpannableGreen(createSpannable(name, 
							PinyinHelper.replaceAll(name), where, 
							Color.argb(255, 250, 145, 0)));
				}else{
					s.setSpannable(createSpannableChinese(name, where, colorHighlight));
					s.setSpannableGreen(createSpannableChinese(name, where, 
							Color.argb(255, 250, 145, 0)));
				}
			} else {
				if(nameraw.equals("")){
					nameraw = PinyinHelper.replaceAll(name);
				}
				s.setSpannable(createSpannable(name, nameraw, where, colorHighlight));
				s.setSpannableGreen(createSpannable(name, nameraw, where , 
						Color.argb(255, 250, 145, 0)));
			}
		}
	}
	
	private int colorHighlight_light = Color.argb(255, 16, 176, 8);
	private int colorHighlight_YouTube = Color.GREEN;
	private int colorHighlight_YouTube_light = Color.argb(255, 255, 201, 14);
	private int colorHighlight_KTVUI = Color.argb(255, 139, 195, 74);

	protected void parseSongInfoQueryResult_YouTube(String where, Song s, Cursor cursorResult) {
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

		int colorHighlight = colorHighlight_YouTube;
		if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
			colorHighlight = colorHighlight_YouTube_light;
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
			colorHighlight = colorHighlight_KTVUI;
		}
		
		if (where.equals("")) {
			s.setSpannable(createSpannable(name, nameraw, where, colorHighlight));
			s.setSpannableGreen(createSpannable(name, nameraw, where, 
					Color.argb(255, 250, 145, 0)));
		} else {
			CharSequence wh1 = name.subSequence(0, 1);
			CharSequence wh2 = name.subSequence(name.length() - 1, name.length());
			if (PinyinHelper.checkChinese(wh1.toString()) || 
				PinyinHelper.checkChinese(wh2.toString())) {
				if(name.contains(where)){
					s.setSpannable(createSpannable(name, 
							PinyinHelper.replaceAll(name), where, colorHighlight));
					s.setSpannableGreen(createSpannable(name, 
							PinyinHelper.replaceAll(name), where, 
							Color.argb(255, 250, 145, 0)));
				}else{
					s.setSpannable(createSpannableChinese(name, where, colorHighlight));
					s.setSpannableGreen(createSpannableChinese(name, where, 
							Color.argb(255, 250, 145, 0)));
				}
			} else {
				if(nameraw.equals("")){
					nameraw = PinyinHelper.replaceAll(name);
				}
				s.setSpannable(createSpannable(name, nameraw, where, colorHighlight));
				s.setSpannableGreen(createSpannable(name, nameraw, where , 
						Color.argb(255, 250, 145, 0)));
			}
		}
	}
	
	private Spannable createSpannableChinese(String name , String where, int color){
		Spannable wordtoSpan = new SpannableString(name);
		int countIdx = 0;
		for (int i = 0; i < name.length(); i++) {
			if (countIdx >= where.length()) {
				break;
			}
			String strChar = name.substring(i, i + 1);
			String strSearchChar = where.substring(countIdx, countIdx + 1);
			if (strChar.equals(strSearchChar)) {
				wordtoSpan.setSpan(new ForegroundColorSpan(color), 
						i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				countIdx++;
			} else {
				if (PinyinHelper.checkChinese(strChar)) {
					wordtoSpan.setSpan(new ForegroundColorSpan(color), 
							i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					countIdx++;
				} else {
					continue;
				}
			}
		}
		return wordtoSpan;
	}
	
	private Spannable createSpannable(String textSong , String nameraw , String textSearch, int color){
		textSearch.trim();
		ArrayList<Integer> listOffset = new ArrayList<Integer>();
		Spannable wordtoSpan;
		if(textSong.equals("")){
			wordtoSpan = new SpannableString("");
		}else{
			if(textSearch.equals("")){
				return new SpannableString(textSong);
			}
			textSearch.trim();
			String newString = textSong.replaceAll("[ &+=_,-/]", "*");
			StringBuffer buffer = new StringBuffer(newString);
				//-------------//
			String[] strings = buffer.toString().split("[*]");
			if(strings.length < textSearch.length()){
				int offset = nameraw.indexOf(textSearch);
				wordtoSpan = new SpannableString(textSong);
				if(offset != -1){
					wordtoSpan.setSpan(new ForegroundColorSpan(color), offset, 
							offset + textSearch.length(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
				return wordtoSpan;
			}
			int count = 0;
 			for (int i = 0; i < strings.length; i++) {
 				int size = strings[i].length();
 				if(size <= 0){
 					count += 1;
 				}else{
 					listOffset.add(count);
 					count += size + 1;
 				}
 				if(listOffset.size() >= textSearch.length()){
 					break;
 				}
			}
 				//-------------//
			wordtoSpan = new SpannableString(textSong);
			for (int i = 0; i < listOffset.size(); i++) {
				int offset = getIndex(textSong, listOffset.get(i));
				if(nameraw.charAt(offset) != textSearch.charAt(i)){
					int of = nameraw.indexOf(textSearch);
					SpannableString word = new SpannableString(textSong);
					if(of != -1){
						word.setSpan(new ForegroundColorSpan(color), of, 
								of + textSearch.length(),
								Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
					return word;
				}else{
					wordtoSpan.setSpan(new ForegroundColorSpan(color), offset, offset + 1,
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
		}	
		return wordtoSpan;
	}
	
	private int getIndex(String string , int index){
		switch (string.charAt(index)) {
		case '(': 	return index + 1; 
		case '`': 	return index + 1; 
		case '[': 	return index + 1; 
		default:	return index;
		}
		
	}
	
	private String cutText(int maxLength, String content) {
		if(content == null){
			return "";
		}
		if(content.length() <= maxLength){
			return content;
		}		
		return content.substring(0, maxLength) + "...";
	}
		
	protected String convertIdSong(int id){
		String stringId = String.valueOf(id);
		switch (stringId.length()) {
		case 1: 	return "00000" + stringId;
		case 2: 	return "0000" + stringId;
		case 3: 	return "000" + stringId;
		case 4: 	return "00" + stringId;
		case 5: 	return "0" + stringId;
		default: 	break;
		}
		return stringId;
	}
	
//////////////////////////////////////////////////////
	
	protected boolean checkNotLetter(String where){
		for (int i = 0; i < where.length(); i++) {
			if(Character.isLetter(where.charAt(i))){
				return true;
			}
		}
		return false;
	}
	
	private ArrayList<Song> listSongs;
	protected void setListSong(ArrayList<Song> list){
		listSongs.clear();
		listSongs.addAll(list);
	}
	
	protected int checkSize(){
		if(listSongs == null){
			return 0;
		}else{
			return listSongs.size();
		}
	}
	
	protected boolean moveToFirstCursor(Cursor cursor){
		boolean re = false;
		if(cursor != null){
			long tgian = System.currentTimeMillis();
			re = cursor.moveToFirst();
		}
		return re;
	}

}
