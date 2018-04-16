package vn.com.sonca.utils;

import vn.com.sonca.zzzzz.MyApplication;
import android.R.bool;
import android.content.Context;
import android.content.SharedPreferences;

public class AppSettings {

	private static AppSettings instance;

	private final String kUpdate = "kupdated";
	private final String kServerName = "kservername";
	private final String kServerIP = "kserverip";
	private final String kServerPass = "kserverpass";
	private final String kServerPrivate = "kprivatekey";
	private final String kServerLastUpdate = "klastupdate";
	
	private final String hiwLastUpdate = "hiwLastUpdate";
	private final String hiwHDDVersion = "hiwHDDVersion";
	private final String hiwDISCVersion = "hiwDISCVerison";
	
	protected static final String CONFIGURATION_NAME = "TouchContronConfig";

	private static SharedPreferences settings;

	public AppSettings(Context context) {
		settings = context.getSharedPreferences(CONFIGURATION_NAME,
				Context.MODE_PRIVATE);
	}

	public static AppSettings getInstance(Context context) {
		if (instance == null) {
			instance = new AppSettings(context);
		}

		return instance;
	}

	public boolean isUpdated() {
		return settings.getBoolean(kUpdate, false);
	}

	public void saveUpdated(boolean value) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(kUpdate, value);
		editor.commit();
	}

	public String loadLastServerName() {
		return settings.getString(kServerName, "");
	}

	public void saveLastServerName(String value) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(kServerName, value);
		editor.commit();
	}

	public String loadServerIP() {
		return settings.getString(kServerIP, "");
	}

	public void saveServerIP(String value) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(kServerIP, value);
		editor.commit();
	}

	public String loadServerPrivateKey() {
		return settings.getString(kServerPrivate, "");
	}

	public void saveServerPrivateKey(String value) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(kServerPrivate, value);
		editor.commit();
	}

	public String loadServerPass() {
		return settings.getString(kServerPass, "");
	}

	public void saveServerPass(String value) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(kServerPass, value);
		editor.commit();
	}

	public int loadServerLastUpdate() {
		return settings.getInt(kServerLastUpdate + 1, 0);
	}
	
	public int loadServerLastUpdate(int type) {
		return settings.getInt(kServerLastUpdate + type, 0);
	}

	public void saveServerLastUpdate(int value, int type) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(kServerLastUpdate + type, value);
		editor.commit();
	}
	
	
////////////////////
	
	private String modemodel = "modemodel";
	private String ircremote = "ircremote";
	private String nameremote = "nameremote";
	
	
	public void saveIrcRemote(int irc){
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(ircremote, irc);
		editor.commit();
	}
	
	public int loadIrcRemote() {
		return settings.getInt(ircremote, -1);
	}
	
	public void saveNameRemote(String value){
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(nameremote, value);
		editor.commit();
	}
	
	public String loadNameRemote() {
		return settings.getString(nameremote, "ACNOS");
	}
	
	public void saveModeModel(int value){
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(modemodel, value);
		editor.commit();
	}
	
	public int loadModeModel() {
		return settings.getInt(modemodel, 0);
	}
	
	// //////////////////
	public void saveHiwLastUpdate(long value) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong(hiwLastUpdate, value);
		editor.commit();
	}


	public void saveHiwHDDVersion(int value) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(hiwHDDVersion, value);
		editor.commit();
	}
	
	public void saveHiwDISCVersion(int value) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(hiwDISCVersion, value);
		editor.commit();
	}

	public long loadHiwLastUpdate() {
		return settings.getLong(hiwLastUpdate, 0);
	}
	
	public int loadHiWHDDVersion(){
		return settings.getInt(hiwHDDVersion, 0);
	}
	
	public int loadHiWDISCVersion(){
		return settings.getInt(hiwDISCVersion, 0);
	}
	
////////////////////
	
	public void saveSleepTime(long sleeptime) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong("sleeptime", sleeptime);
		editor.commit();
	}

	public long getSleepTime() {
		return settings.getLong("sleeptime", 0);
	}
	
	public void saveSwitchTime(long switchtime) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong("switchtime", switchtime);
		editor.commit();
	}

	public long getSwitchTime() {
		return settings.getLong("switchtime", 60);
	}

	// //////////////////
	
	public void saveColorScreen(int colorType) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("colorType", colorType);
		editor.commit();
	}

	public int getColorScreen() {
		return settings.getInt("colorType", MyApplication.SCREEN_BLUE);
	}
	
	// //////////////////

	public void savePopupSetting(boolean flagOn) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("popSetting", flagOn);
		editor.commit();
	}

	public boolean getPopupSetting() {
		return settings.getBoolean("popSetting", false);
	}
	
	public void saveWIFIVideoSetting(boolean flagOn) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("wfvSetting", flagOn);
		editor.commit();
	}

	public boolean getWIFIVideoSetting() {
		return settings.getBoolean("wfvSetting", true);
	}
	
	// //////////////////

	public void saveVideoLyricSize(long size) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong("vidSize", size);
		editor.commit();
	}

	public long getVideoLyricSize() {
		return settings.getLong("vidSize", 0);
	}
	
	// //////////////////	
	public void saveYouTubeVersion_SK90xx(int version) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("ytVersion_SK90xx", version);
		editor.commit();
	}

	public int getYouTubeVersion_SK90xx() {
		return settings.getInt("ytVersion_SK90xx", 0);
	}
	
	public void saveListOfflineVersion_SK90xx(int version) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("listOffline_SK90xx", version);
		editor.commit();
	}

	public int getListOfflineVersion_SK90xx() {
		return settings.getInt("listOffline_SK90xx", 0);
	}
	
	public void saveListHiddenVersion_SK90xx(int version) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("listHidden_SK90xx", version);
		editor.commit();
	}

	public int getListHiddenVersion_SK90xx() {
		return settings.getInt("listHidden_SK90xx", 0);
	}	
	
	// //////////////////
	public void saveYouTubeVersion(int version) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("ytVersion", version);
		editor.commit();
	}

	public int getYouTubeVersion() {
		return settings.getInt("ytVersion", 0);
	}
	
	public void saveListOfflineVersion(int version) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("listOffline", version);
		editor.commit();
	}

	public int getListOfflineVersion() {
		return settings.getInt("listOffline", 0);
	}
	
	public void saveLuckyDataVersion(int version) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("luckData", version);
		editor.commit();
	}

	public int getLuckyDataVersion() {
		return settings.getInt("luckData", 0);
	}
	
	public void saveLuckyImageVersion(int version) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("luckIMG", version);
		editor.commit();
	}

	public int getLuckyImageVersion() {
		return settings.getInt("luckIMG", 0);
	}
	
	public void saveSambaDataVersion(int version) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("sambaData", version);
		editor.commit();
	}

	public int getSambaDataVersion() {
		return settings.getInt("sambaData", 0);
	}
	
	public void saveUpdateTOCVersion(int version) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("upTocVersion", version);
		editor.commit();
	}

	public int getUpdateTOCVersion() {
		return settings.getInt("upTocVersion", 0);
	}

	public void saveCountAddFile(int count) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("countAddFile", count);
		editor.commit();
	}

	public int getCountAddFile() {
		return settings.getInt("countAddFile", 0);
	}
	
	public boolean isEverConnectKBX9() {
		return settings.getBoolean("everKBX9", false);
	}

	public void setEverConnectKBX9(boolean value) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("everKBX9", value);
		editor.commit();
	}
	
	public static boolean checkContentUSB(int tocType){
		if(tocType == 2 || tocType == 7 || tocType == 9 || tocType == 16 || tocType == 17 || tocType == 18 || tocType == 19){
			return true;
		}
		
		return false;
	}
	
	public int getLastConnectType() {
		return settings.getInt("lastConnectType", MyApplication.SONCA);
	}

	public void setLastConnectType(int value) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("lastConnectType", value);
		editor.commit();
	}
	
	public void saveAdminYouTube(boolean flagOn) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("admYouTube", flagOn);
		editor.commit();
	}

	public boolean getAdminYouTube() {
		return settings.getBoolean("admYouTube", true);
	}
	
	public void saveAdminOnline(boolean flagOn) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("admOnline", flagOn);
		editor.commit();
	}

	public boolean getAdminOnline() {
		return settings.getBoolean("admOnline", false);
	}
	
	public String getStrDownload90xx() {
		return settings.getString("strDownload90xx", "");
	}

	public void setStrDownload90xx(String value) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("strDownload90xx", value);
		editor.commit();
	}
	
	public boolean isLastMIDIData() {
		return settings.getBoolean("lastMidiData", false);
	}

	public void setLastMIDIData(boolean value) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("lastMidiData", value);
		editor.commit();
	}
	
	public String getListOfflineVersion_SK90xx_New() {
		return settings.getString("strListOffline_SK90xx", "");
	}

	public void saveListOfflineVersion_SK90xx_New(String value) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("strListOffline_SK90xx", value);
		editor.commit();
	}
	
	public void setMCountAuthen(int count) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("mCountAuthen", count);
		editor.commit();
	}

	public int getMCountAuthen() {
		return settings.getInt("mCountAuthen", 0);
	}
	
	public void setMCountSuccess(int count) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("mCountSuccess", count);
		editor.commit();
	}

	public int getMCountSuccess() {
		return settings.getInt("mCountSuccess", 0);
	}
	
	public void setMCountFail(int count) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("mCountFail", count);
		editor.commit();
	}

	public int getMCountFail() {
		return settings.getInt("mCountFail", 0);
	}
	
	public void saveRealKeyboard(boolean flagOn) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("realKeyboard", flagOn);
		editor.commit();
	}

	public boolean getRealKeyboard() {
		return settings.getBoolean("realKeyboard", false);
	}
	
	public boolean isSearchOnline() {
		return settings.getBoolean("onlineSearch", true);
	}

	public void saveSearchOnline(boolean value) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("onlineSearch", value);
		editor.commit();
	}
	
	public boolean isAdvSong() {
		return settings.getBoolean("advSong", true);
	}

	public void saveAdvSong(boolean value) {
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("advSong", value);
		editor.commit();
	}
	
}
