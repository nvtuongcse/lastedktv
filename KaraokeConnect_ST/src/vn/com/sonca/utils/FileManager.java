package vn.com.sonca.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class FileManager {
	private static String extHDD_KOKDir; 
	
	private String path; 
	private File curFile; 
	
	public static void setHDDPath(String curPath) {
		extHDD_KOKDir = curPath; 
	}
	
	public static String getHDDPath() {
		return extHDD_KOKDir; 
	}
	
	public FileManager(String path) {
		this.path = path; 
		curFile = new File(path); 
	}
	
	public FileManager(URI uri) {
		curFile = new File(uri); 
	}
	
	public String[] listAllFileFromPath(String srcPath) {
		this.path = srcPath; 
		curFile = new File(path); 
		
		return curFile.list(); 
	}
	
	public static String getDir(Context context) {
		String rootPath =  ""; 
		
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED))
        	rootPath = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        else {
        	File files = context.getFilesDir();
        	if (!files.exists()) {
        		files.mkdir(); 
			}
        	rootPath = files.getAbsolutePath(); 
        }
        return rootPath;
    }
	
	public static String[] getAvalableUSBStorage() {
		File usbStorage = new File(AppConfig.USBStorage); 
		ArrayList<String> usbList = new ArrayList<String>(); 
		try{
		for(File subDir : usbStorage.listFiles()) {
			if(subDir.isDirectory()) { 
				File root = subDir.listFiles()[0]; 
				usbList.add(root.getAbsolutePath()); 
			}
		}
		
		}catch(Exception e){
			
		}

		String[] list = new String[usbList.size()]; 
		int idx = 0; 
		for (String str : usbList) {
			list[idx++] = str; 
		}
		return list; 
	}
	
	/*
	 * getExtStorageAppBundlePath
	 * store developer application data: picture, video, ...
	 * This directory locate at ./mnt/sdcard/android/data/{AppBunde}
	 */
	public static String getExtStorageAppBundlePath(){
		// Create new Folder At Android Path
		
		String rootPath =  ""; 

		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)){
			rootPath = android.os.Environment.getExternalStorageDirectory().toString();
			rootPath = rootPath.concat(String.format("/%s/%s", AppConfig.AndroidData, AppConfig.appBundle));
			File appBundle = new File(rootPath); 
			if(!appBundle.exists()) appBundle.mkdir(); 
		}

    	return rootPath; 
	}
	
	/*
	 * getExtStorageAppBundlePath
	 * create to store user data
	 * This directory locate at ./mnt/sdcard/{appbundle}
	 */
	public static String getUserStoragePath(){
		String rootPath =  ""; 

		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)){
			rootPath = android.os.Environment.getExternalStorageDirectory().toString();
			rootPath = rootPath.concat(String.format("/%s", AppConfig.appName));
			File appBundle = new File(rootPath); 
			if(!appBundle.exists()) appBundle.mkdir(); 
		}

    	return rootPath; 
	}
	
	/*
	 * Get installed application path, usually in internal memory
	 * This path is deleted as application uninstalled
	 * @param: currentApplication context
	 * @result: application path
	 */
	public static String getAppBundlePath(Context context) {
		File files = context.getFilesDir();
    	if (!files.exists()) {
    		files.mkdir(); 
		}
    	return files.getAbsolutePath(); 
	}
	
	public static String getExtStoragePicturesDir()
	{
		String path = getExtStorageAppBundlePath() + "/" +AppConfig.PicturesDirName;
		File file = new File(path); 
		if (!file.exists()) {
			file.mkdir(); 
		}
		return path; 
	}
	
	public static String getExtStorageKaraokeDir()
	{
		String path = getExtStorageAppBundlePath() + "/" +AppConfig.KOKDirName;
		File file = new File(path); 
		if (!file.exists()) {
			file.mkdir(); 
		}
		return path; 
	}
	
	public static String getExtStorageVideoDir()
	{
		String path = getExtStorageAppBundlePath() + "/" +AppConfig.VideoDirName;
		File file = new File(path); 
		if (!file.exists()) {
			file.mkdir(); 
		}
		return path; 
	}
	
	public static String getExtStorageDefaultVideo() {
		return FileManager.getExtStorageVideoDir() + "/" + AppConfig.videoName; 
	}
	
	public static File getExtStorageFilePath(String fileName) {
		String path = getExtStorageAppBundlePath() + "/" + fileName; 
		return new File(path); 
	}
	
	public static boolean fileExistAtPath(String path) {
		File file = new File(path); 
		return file.exists(); 
	}
	
	public static String getAssetsFile(String path, Context context) {
		AssetManager assetManager = context.getAssets();   // null ???  Get the AssetManager here.
		try{
			String[] assets = assetManager.list("file:///android_asset/"); 
//			if (assets != null) {   
//				   for ( int i = 0;i<assets.length;i++) {
//				        Log.d("",assets[i]); 
//				    }
//				}
			
			
			if (assets.length > 0) {
				return assets[0]; 
			}
			return "";
		} catch (Exception e){
			return ""; 
		}
	}
	
	public static boolean copyFile(File source, File dest) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try {
			bis = new BufferedInputStream(new FileInputStream(source));
			bos = new BufferedOutputStream(new FileOutputStream(dest, false));

			byte[] buf = new byte[1024];
			bis.read(buf);

			do {
				bos.write(buf);
			} while(bis.read(buf) != -1);
		} catch (IOException e) {
			return false;
		} finally {
			try {
				if (bis != null) bis.close();
				if (bos != null) bos.close();
			} catch (IOException e) {
				return false;
			}
		}

		return true;
	}
public static boolean copyFile(File source, File dest, boolean replace) {
		
		if(dest.exists() && replace)
			dest.delete(); 
		
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try {
			bis = new BufferedInputStream(new FileInputStream(source));
			bos = new BufferedOutputStream(new FileOutputStream(dest, false));

			byte[] buf = new byte[100*1024];
			bis.read(buf);

			do {
				bos.write(buf);
			} while(bis.read(buf) != -1);
		} catch (IOException e) {
			return false;
		} finally {
			try {
				if (bis != null) bis.close();
				if (bos != null) bos.close();
			} catch (IOException e) {
				return false;
			}
		}

		return true;
	}
public static String getExtStorageTempDir() {
	String path = getExtStorageAppBundlePath() + "/" +AppConfig.TempDirName;
	File file = new File(path); 
	if (!file.exists()) {
		file.mkdir(); 
	}
	return path; 
}

}

