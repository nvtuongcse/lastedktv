package vn.sonca.LoadDataServer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import vn.com.sonca.MyLog.MyLog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.Window;

public class LoadFileAPK extends AsyncTask<String, Integer, Integer> {
	
	private static String TAB = "LoadFileAPK";
	
	private String linkFileDevice = "";
	private ItemApp itemApp = null;
	private Context context;
	
	public LoadFileAPK(Context context, ItemApp itemApp) {
		this.itemApp = itemApp;
		this.context = context;
	}

	private OnLoadFileAPKListener listener;
	public interface OnLoadFileAPKListener{
		public void OnLoading(int down, int lenght);
		public void OnLoadFinish(String linkFile, ItemApp itemApp);
	}
	
	public void setOnLoadFileAPKListener(OnLoadFileAPKListener listener){
		this.listener = listener;
	}
	
	@Override
	protected Integer doInBackground(String... params) {
		if(itemApp == null){
			return null;
		}
		MyLog.e(TAB, "=====================");
		MyLog.e(TAB, "   START DOWNLOAD    ");
		MyLog.e(TAB, "LINK : " + itemApp.getLinkPack());
		String name = itemApp.getNamePack().replace('.', '_') + ".apk";
		String dirAPK = createDirAPK();
		String link = itemApp.getLinkPack().replace(" ", "%20");
		linkFileDevice = downloadImage(link, dirAPK + "/" + name);
		if(linkFileDevice != null && !linkFileDevice.trim().equals("")){
			MyLog.e(TAB, "    END DOWNLOAD     ");
			MyLog.e(TAB, "=====================");
		}else{
			MyLog.e(TAB, "    ERROR DOWNLOAD     ");
			MyLog.e(TAB, "=====================");
		}
		return null;
	}
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		// MyLog.d(TAB, "onProgressUpdate : " + values[0]);
		if(listener != null){
			listener.OnLoading(countFile, lengthFile);
		}
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		// MyLog.e(TAB, "DONE : " + linkFileDevice);
		if(listener != null){
			listener.OnLoadFinish(linkFileDevice, itemApp);
		}
	}
	
	private int lengthFile;
	private int countFile;
	private String downloadImage(String link, String file) {
		File f = new File(file);
		if (f.exists()) {
			f.delete();
		}
		int MAXLENGHT = 1024;
		InputStream inputStream = null;
		FileOutputStream fileStream = null;
		HttpURLConnection connection = null;
		try {
			URL url = new URL(link);
			connection = (HttpURLConnection) url.openConnection();
			connection.connect();
			int state = connection.getResponseCode();
			if (state == HttpURLConnection.HTTP_OK) {
				lengthFile = connection.getContentLength();
				if (lengthFile <= 0) {
					return null;
				}
				byte bytes[] = new byte[MAXLENGHT];
				inputStream = connection.getInputStream();
				fileStream = new FileOutputStream(file);
				int buffer;
				countFile = 0;
				int intUpdate = 0;
				while ((buffer = inputStream.read(bytes, 0, MAXLENGHT)) != -1) {
					fileStream.write(bytes, 0, buffer);
					if(isCancelled() == true){
						clearData(connection, inputStream, fileStream);
						return null;
					}
					countFile += buffer;
					if(intUpdate >= 100){
						publishProgress(countFile);
						intUpdate = 0;
					}else{
						intUpdate++;
					}
				}
				clearData(connection, inputStream, fileStream);
				return file;
			}else{
				return null;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
			//================//
		try {
			clearData(connection, inputStream, fileStream);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	private void clearData(HttpURLConnection connection, 
		InputStream inputStream, FileOutputStream fileStream) 
		throws IOException{
		if (inputStream != null) {
			inputStream.close();
			inputStream = null;
		}
		if (fileStream != null) {
			fileStream.close();
			fileStream = null;
		}
		connection.disconnect();
	}
	
	private String createDirAPK(){
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			String rootPath = android.os.Environment
				.getExternalStorageDirectory().toString()
				.concat(String.format(
					"/%s/%s", "Android/data",
					context.getPackageName()));
			String sss = rootPath.concat("/APK");
			File image = new File(sss);
			if (!image.exists()){
				image.mkdir();
			}
			return sss;
		}
		return "";
	}
	
	public static boolean checkLinkApkInDevice(Context context, ItemApp app){
		if(app == null){
			return false;
		}
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			String rootPath = android.os.Environment
				.getExternalStorageDirectory().toString()
				.concat(String.format(
					"/%s/%s", "Android/data",
					context.getPackageName()));
			String dirAPK = rootPath.concat("/APK");
			String name = app.getNamePack().replace('.', '_') + ".apk";
			File image = new File(dirAPK + "/" + name);
			return image.exists();
		}
		return false;
	}
	
	public static void deleteAllFileAPK(Context context){
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			String rootPath = android.os.Environment
				.getExternalStorageDirectory().toString()
				.concat(String.format(
					"/%s/%s", "Android/data",
					context.getPackageName()));
			String sss = rootPath.concat("/APK");
			File dir = new File(sss);
			if(dir == null){
				return;
			}
			if(dir.isDirectory()){
				for(File file: dir.listFiles()) {
				    if (!file.isDirectory()) {
				        file.delete();
				    }
				}
			}
		}
	}
	
}
