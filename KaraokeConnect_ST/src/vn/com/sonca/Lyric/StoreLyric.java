package vn.com.sonca.Lyric;

import android.content.Context;
import android.content.SharedPreferences;

public class StoreLyric {
	
	private Context context;
	public StoreLyric(Context context) {
		this.context = context;
	}
	
	public void saveLyricHDD(boolean isHDD){
		SharedPreferences pre = context.getSharedPreferences("lyric_hdd", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pre.edit();
		editor.putBoolean("hdd", isHDD);
		editor.commit();
	}
	
	public boolean getLyricHDD(){
		SharedPreferences pre = context.getSharedPreferences("lyric_hdd", Context.MODE_PRIVATE);
		boolean data = pre.getBoolean("hdd", false);
		return data;		
	}
	
	private String convertKey(int id){
		switch (id) {
		case 1:		return "TIENGVIET"; 	
		default: 	return ""; 	
		}
		
	}

	// ---------------- HMINH
	public void saveLyricDate(long date) {
		SharedPreferences pre = context.getSharedPreferences("lyric_hdd",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pre.edit();
		editor.putLong("date", date);
		editor.commit();
	}

	public void saveLyricDate_Plus(long date) {
		SharedPreferences pre = context.getSharedPreferences("lyric_hdd",
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pre.edit();
		editor.putLong("date_plus", date);
		editor.commit();
	}

	public long getLyricDate() {
		SharedPreferences pre = context.getSharedPreferences("lyric_hdd",
				Context.MODE_PRIVATE);
		return pre.getLong("date", 0);
	}

	public long getLyricDate_Plus() {
		SharedPreferences pre = context.getSharedPreferences("lyric_hdd",
				Context.MODE_PRIVATE);
		return pre.getLong("date_plus", 0);
	}
}
