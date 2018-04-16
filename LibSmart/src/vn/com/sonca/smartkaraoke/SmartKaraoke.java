package vn.com.sonca.smartkaraoke;

import vn.com.sonca.params.SongInfo;
import vn.com.sonca.params.TOCVersion;

public class SmartKaraoke {
	public static final int SUB_TYPE_DISC = 0;  
	public static final int SUB_TYPE_SCDISC =1; 
	public static final int SUB_TYPE_XUSER = 2; 
	public static final int SUB_TYPE_USER = 3;
	public static final int SUB_TYPE_YOUTUBE = 4; 
	
	static 
	{
		System.loadLibrary("smartkaraoke"); 
	}
	
	public SmartKaraoke() {
		
	}
	
//	public native boolean importTocToDatabase(String filePath, String singerPath, String lyricPath, String dbPath); 
	public native boolean importdata(String dbPath, String idx, String f10w, String cpRightPath); 
	public native boolean extractdata(String singerPath, String picDir); 
//	public native boolean importSongModel(String dbPath, int model, int[] idList, int size); 
	public native int mergeData(String mainPath, String updatePath, int updateType); 
	private native int[] native_getVersion(String path); 
	public TOCVersion getVersion(String path) {
		int[] info = native_getVersion(path); 
		return new TOCVersion(info); 
	}
	
	public static native int mergeDataUpdate(String mainPath, String subPath, String updatePath, int updateType);
	public static native int renameDataUpdate(String subPath, int idx, int vol); 
	public static native int setDataUpdateVersion(String subPath, int volDisc, int volUser, int volXUser); 
	
	public static native int structSize(); 
	public static native byte[] parseFileInfo(String path); 
	public static native byte[] getDataBytes(boolean isMidi, String src, int index, int offset, int len); 
	public static native int getdata(String src, String dst, int index); 
	public static native int getdata(boolean isMidi, String src, String dst,int index, int offset, int len); 
	
	public native void addNewSong(String dbPath, String mainPath, String subPath, SongInfo info, int type);
	public native void removeSong(String dbPath, String mainPath, SongInfo info);
	
	public native void addNewSongList(String dbPath, String mainPath, String subPath, SongInfo[] info, int type);
	public native void removeSongList(String dbPath, String mainPath, SongInfo[] info);
	public static native byte[] getStringData(int idx);
}
