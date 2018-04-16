package vn.com.sonca.Touch.Hi_W;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.utils.AppSettings;

public class SaveFirmware {
	
	private static SharedPreferences versionFirmware;
	private static SaveFirmware instance;
	
	public SaveFirmware(Context context) {
		versionFirmware = context.getSharedPreferences("VersionFirmware",
				Context.MODE_PRIVATE);
	}
	
	public static SaveFirmware getInstance(Context context) {
		if (instance == null) {
			instance = new SaveFirmware(context);
		}
		return instance;
	}

//----------------------//
	
	public void saveVersionFirmwareKM1(int value) {
		MyLog.e("SaveFirmware", "saveVersionFirmwareKM1 : " + value);
		SharedPreferences.Editor editor = versionFirmware.edit();
		editor.putInt("km1_version", value);
		editor.commit();
	}
	
	public void saveRevisionFirmwareKM1(int value) {
		MyLog.e("SaveFirmware", "saveRevisionFirmwareKM1 : " + value);
		SharedPreferences.Editor editor = versionFirmware.edit();
		editor.putInt("km1_revision", value);
		editor.commit();
	}
	
	public int getVersionFirmwareKM1(){
		int re = versionFirmware.getInt("km1_version", 0);
		MyLog.e("SaveFirmware", "getVersionFirmwareKM1 : " + re);
		return re;
	}
	
	public int getRevisionFirmwareKM1(){
		int re = versionFirmware.getInt("km1_revision", 0);
		MyLog.e("SaveFirmware", "getRevisionFirmwareKM1 : " + re);
		return re;
	}
	
//----------------------//

	public void saveVersionFirmwareHiW(int value) {
		SharedPreferences.Editor editor = versionFirmware.edit();
		editor.putInt("hiw_version", value);
		editor.commit();
	}
	
	public void saveRevisionFirmwareHiW(int value) {
		SharedPreferences.Editor editor = versionFirmware.edit();
		editor.putInt("hiw_revision", value);
		editor.commit();
	}
	
	public int getVersionFirmwareHiW(){
		return versionFirmware.getInt("hiw_version", 0);
	}
	
	public int getRevisionFirmwareHiW(){
		return versionFirmware.getInt("hiw_revision", 0);
	}

//----------------------//

	public void clearVersionFirmware(){
		saveVersionFirmwareHiW(0);
		saveRevisionFirmwareHiW(0);
		saveVersionFirmwareKM1(0);
		saveRevisionFirmwareKM1(0);
	}

}
