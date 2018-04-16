package vn.com.sonca.utils;

import java.util.Locale;

public final class AppConfig {

	public static final String USBStorage		= "/mnt/usb_storage"; 
	public static final String AndroidData		= "Android/data"; 
	public static final String appBundle 		= "vn.com.hanhphuc.karremote";
	public static final String appName	 		= "AndroidDVD";
	public static final String PicturesDirName 	= "Pictures"; 
	public static final String VideoDirName 	= "Video"; 
	public static final String HDDDirName 		= "KARAOKE/KTV"; 
	public static final String HDDVocalDirName 	= "KARAOKE/SONGS"; 
	public static final String KOKDirName 		= "SONGS"; 
	public static final String TempDirName 		= "temp"; 
	public static final String videoName 		= "karaoke720.mp4"; 
	public static final String firstUpdateName 	= "LoadFirstUpdate.xml"; 
	public static final String registryFileName = "Registry.xml"; 
	
	public static final String SHARED_PREFERENCE_NAME = "CONFIGURATION"; 
	
	public static final int dbVersion = 1;
	public static Locale curLocale = new Locale("vi-VN"); 
	
	public static String DBDebugTag = "DatabaseHandle"; 
	
	public static final int LANG_INDEX_OTHER = 0xFF; 
	public enum LANG_INDEX {VIETNAMESE, ENGLISH, FRENCH, CHINESE, PHILIPINESE, HINDI, KOREAN, ALL_EXCEPT_VIETNAMESE, ALL_LANGUAGE}; 
	/*
	 * MKV: file video with mp3 audio format
	 * MKV_WMA: File video hdd with wma audio format 
	 */
//	public enum MEDIA_TYPE {NONE, MIDI, MP3, WMA, MKV, MKV_WMA, JPG}; 
	public enum MEDIA_TYPE {MIDI, MP3, SINGER, VIDEO , ALL}
	
	public enum UPDATE_STATUS {NEED_UPDATE, NEED_PRE_UPDATE, UP_TO_DATE, WRONG_FORMAT}; 
	
	public enum UPDATE_PROGRESS{INITIALIZE, DOWNLOAD, PROGRESS, EXTRACT, SAVEDB, MERGEDATA, COPYDATA, COPYMP3, SORTING, SUCCESS, FAILED}; 

	public enum MEDIA_STORE	{AUDIO, VIDEO, PICTURE, ALL}; 

	public enum UPDATE_TYPE {NONE, USB, INTERNET}; 
	
	public enum KARAOKESTATE {
		STOPPED, LOADING, PLAY, PAUSE
	};
}
