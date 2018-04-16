package vn.com.sonca.Touch.Language;

import java.util.ArrayList;
import java.util.Map;

import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.database.DBInterface;
import vn.com.sonca.database.DBInstance.SearchMode;
import vn.com.sonca.params.Language;
import vn.com.sonca.params.SKServer;
import vn.com.sonca.utils.AppConfig.LANG_INDEX;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class LanguageStore {

	private String TAB = "LanguageStote";
	private static final String PREF_FILE = "LanguageStote";
	private SharedPreferences settings;
	private Context context;

	public LanguageStore(Context context) {
		this.context = context;
		settings = context
				.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
	}

	public void initStarting() {
		Map<String, ?> mapdevice = settings.getAll();
		if (mapdevice.size() > 0) {
			return;
		}

		SharedPreferences.Editor editor = settings.edit();
		editor.clear();
		editor.commit();

		ArrayList<Language> languageList = DBInterface.DBSearchSongLanguage("",
				SearchMode.MODE_FULL, 0, 0, context);
		for (Language lang : languageList) {
			if (lang.getID() == 0) { // VIETNAM
				putLanguage(lang, true);
			} else {
				putLanguage(lang, false);
			}
		}
	}

	public void updateStore() {
		SharedPreferences.Editor editor = settings.edit();
		editor.clear();
		editor.commit();

		ArrayList<Language> languageList = DBInterface.DBSearchSongLanguage("",
				SearchMode.MODE_FULL, 0, 0, context);
		for (Language lang : languageList) {
			if (lang.getID() == 0) { // VIETNAM
				putLanguage(lang, true);
			} else {
				putLanguage(lang, false);
			}
		}
	}

	private void putLanguage(Language lang, boolean active) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(lang.getID() + "", active + "");
		editor.commit();
	}
	
	private void putLanguage(String langID, boolean active) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(langID, active + "");
		editor.commit();
	}

	public boolean getActiveLanguage(Language lang) {
		Map<String, ?> mapdevice = settings.getAll();
		if (mapdevice.size() == 0) {
			return false;
		}
		for (String key : mapdevice.keySet()) {
			if (key.equals(lang.getID() + "")) {
				String data = (String) mapdevice.get(key);
				if (Boolean.parseBoolean(data)) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}
	
	public boolean getActiveLanguage(String langID) {
		Map<String, ?> mapdevice = settings.getAll();
		if (mapdevice.size() == 0) {
			return false;
		}
		for (String key : mapdevice.keySet()) {
			if (key.equals(langID)) {
				String data = (String) mapdevice.get(key);
				if (Boolean.parseBoolean(data)) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}

	public void setLanguage(Language lang) {
		boolean flag = getActiveLanguage(lang);
		putLanguage(lang, !flag);

		if (checkAllInactive()) {
			updateStore();
		}
	}
	
	public void setLanguage(String langID) {
		boolean flag = getActiveLanguage(langID);
		putLanguage(langID, !flag);

		if (checkAllInactive()) {
			updateStore();
		}
	}

	public boolean checkAllInactive() {
		Map<String, ?> mapdevice = settings.getAll();
		if (mapdevice.size() == 0) {
			return true;
		}
		int count = 0;
		for (String key : mapdevice.keySet()) {
			String data = (String) mapdevice.get(key);
			if (Boolean.parseBoolean(data)) {
				count++;
				break;
			}
		}
		return count <= 0;
	}

	public ArrayList<String> getListIDActive() {
		ArrayList<String> listID = new ArrayList<String>();
		Map<String, ?> mapdevice = settings.getAll();

		for (String key : mapdevice.keySet()) {
			String data = (String) mapdevice.get(key);
			if (Boolean.parseBoolean(data)) {
				listID.add(key);
			}
		}
		return listID;
	}
}
