package vn.sonca.LoadDataServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;

import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class LoadItemApp extends AsyncTask<Void, Void, Integer> {
	
	private String TAB = "LoadItemApp";
	private String rootServer = "http://221.132.17.130:80/ServerLaucherMP4/APP";
	private String nameInfo = "/apilauncherMP4.txt";
	
	private Context context;
	private String urlString;
	private TreeMap<String, ArrayList<ItemApp>> mapListApp;
	private ArrayList<ItemApp> listAppUpdate;
	
	public LoadItemApp(Context context, TreeMap<String, ArrayList<ItemApp>> mapListApp) {
		listAppUpdate = new ArrayList<ItemApp>();
		this.urlString = rootServer + nameInfo;
		MyLog.e(TAB, "LoadItemApp : " + this.urlString);
		this.mapListApp = mapListApp;
		this.context = context;
	}
	
	private OnLoadItemAppListener listener;
	public interface OnLoadItemAppListener {
		public void OnLoadAppFinish();
		public void OnLoadError(int error);
	}
	
	public void setOnLoadItemAppListener(OnLoadItemAppListener listener){
		this.listener = listener;
	}

	public boolean isOnline() {
	    try {
	        int timeoutMs = 2000;
	        Socket sock = new Socket();
	        SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);

	        sock.connect(sockaddr, timeoutMs);
	        sock.close();

	        return true;
	    } catch (Exception e) { return false; }
	}
	
	@Override
	protected Integer doInBackground(Void... params) {
		MyLog.i(TAB, "doInBackground");
		if(isOnline() == false){
			return -3;
		}
		if(mapListApp == null){
			return -3;
		}
		String dirRoot = createDirRoot();
		if(dirRoot.equals("")){
			return -3;
		}
		
		String dirImage = createDirImage(dirRoot);
		String fileJSON = dirRoot + nameInfo;
		
		boolean successJSON = downloadFileJSON(urlString, fileJSON);
		boolean successParse = parseFileJSON(fileJSON, mapListApp);		
		if(successJSON && successParse){
			listAppUpdate = getMapListUpdate(mapListApp);
			showListData(listAppUpdate);
		}
		if(!successParse){
			return -1;
		}
		if(dirImage.equals("")){
			return -2;
		}
		if(successJSON == true){
			// downloadFullImage(rootServer, dirImage, mapListApp);
		}else{
			// parseLinkImage(dirImage, mapListApp);
		}
        parseInstallApp(context, mapListApp);
        
        
		return 0;
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		MyLog.i(TAB, "onPostExecute");
		if(result == 0){
			if(listener != null){
				listener.OnLoadAppFinish();
			}
		}else if(result < 0){
			if(listener != null){
				listener.OnLoadError(result);
			}
		}
	}
	
	public ArrayList<ItemApp> getListUpdate(){
		return listAppUpdate;
	}
	
	private ArrayList<ItemApp> getMapListUpdate(TreeMap<String, ArrayList<ItemApp>> current){
		ArrayList<ItemApp> update = new ArrayList<ItemApp>();
		if(current == null){
			return update;
		}
		for (Map.Entry<String, ArrayList<ItemApp>> entry : current.entrySet()) {
			try {
				String key = entry.getKey();
				ArrayList<ItemApp> listCurrent = current.get(key);
				for (int i = 0; i < listCurrent.size(); i++) {
					ItemApp item = listCurrent.get(i);
					String pack = item.getNamePack();
					if(MyApplication.isPackageInstalled(pack, context)){
						MyLog.e(TAB, "getMapListUpdate : " + pack);
						String versionNew = item.getVersion();
						String versionOld = MyApplication.getVersionApp(pack);
						MyLog.e(TAB, "versionNew : " + versionNew);
						MyLog.e(TAB, "versionOld : " + versionOld);
						if(MyApplication.checkVersionApp(versionOld, versionNew)){
							update.add(item);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return update;
	}
	
	public TreeMap<String, ArrayList<ItemApp>>  getListItemApp(){
		return mapListApp;
	}
	
	private boolean downloadFileJSON(String link, String file) {
		HttpURLConnection connection = null;
		try {
			URL url = new URL(link);
			connection = (HttpURLConnection) url.openConnection();
			// connection.connect();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("charset", "utf-8");
			int state = connection.getResponseCode();
			if (state == HttpURLConnection.HTTP_OK) {
				File f = new File(file);
				if(f.exists()){
					f.delete();
				}
				int MAXLENGHT = 1024;
				byte bytes[] = new byte[MAXLENGHT];
				InputStream inputStream = connection.getInputStream();
				FileOutputStream fileStream = new FileOutputStream(file);
				int count;
				while ((count = inputStream.read(bytes, 0, MAXLENGHT)) != -1) {
					fileStream.write(bytes, 0, count);
				}
				inputStream.close();
				fileStream.close();
				inputStream = null;
				fileStream = null;
			}
			connection.disconnect();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean parseFileJSON(String link, TreeMap<String, ArrayList<ItemApp>> listApp){
		try{
			File check = new File(link);
			if(!check.exists()){
				return false;
			}
			FileReader file = new FileReader(link);
			if(!file.ready()){
				return false;
			}
			BufferedReader in = new BufferedReader(file);
			String inputLine = "";
	        StringBuffer response = new StringBuffer();
	        while ((inputLine = in.readLine()) != null) {
	            response.append(inputLine);
	        }
	        in.close();
	        in = null;
			JSONObject jsonObject = new JSONObject(response.toString());
	        JSONArray jsonArray = jsonObject.getJSONArray("app");
	        for (int i = 0; i < jsonArray.length(); i++) {
	        	JSONObject object = jsonArray.getJSONObject(i);
	        	String typeApp = object.getString("typeApp");
	            JSONArray listapp = object.getJSONArray("listApp");
	            ArrayList<ItemApp> listItemApp = new ArrayList<ItemApp>();
	            for (int j = 0; j < listapp.length(); j++) {
	            	JSONObject app = listapp.getJSONObject(j);
					ItemApp item = new ItemApp();
					item.setStyleApp(typeApp);
					item.setNameApp(app.getString("nameApp"));
					item.setNamePack(app.getString("namePack"));
					item.setLinkPack(app.getString("linkPack"));
					item.setInfoPack(app.getString("infoPack"));
					item.setLinkIconServer(app.getString("linkIconAC"));
					item.setLinkIconServerDep(app.getString("linkIconIN"));
					item.setVersion(app.getString("version"));
					item.setInstaller(false);
					listItemApp.add(item);
				}
	            listApp.put(typeApp, listItemApp);
	        }
	        return true;
		}catch(Exception ex){
			ex.printStackTrace();
			return false;
		}
	}
	
	private String createDirRoot(){
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			String rootPath = android.os.Environment
					.getExternalStorageDirectory().toString()
					.concat(String.format(
						"/%s/%s", "Android/data",
						context.getPackageName()));
			File root = new File(rootPath);
			if (!root.exists()){
				root.mkdir();
			}
			return rootPath;
		}
		return "";
	}
	
	private String createDirImage(String rootPath){
		String sss = rootPath.concat("/IMAGE");
		File image = new File(sss);
		if (!image.exists()){
			image.mkdir();
		}
		return sss;
	}
	
	private void downloadFullImage(String link, String dir, 
		TreeMap<String, ArrayList<ItemApp>> listApp){
		for (Entry<String, ArrayList<ItemApp>> object : listApp.entrySet()) {
			ArrayList<ItemApp> list = object.getValue();
			for (int i = 0; i < list.size(); i++) {
				ItemApp app = list.get(i);
					//-----------------//
				String name1 = app.getNamePack().replace('.', '_') + ".png";
				String server1 = (link + "/" + app.getStyleApp() + "/" + app.getNameApp() + 
						"/" + app.getLinkIconServer()).replace(" ", "%20");
				String device1 = dir + "/" + name1;
				boolean success1 = downloadImage(server1, device1);
				if(success1 == true){
					app.setLinkIconDevice(device1);
				}
					//-----------------//
				String imageServer = app.getLinkIconServerDep();
				if(!imageServer.equals("")){
					String name2 = app.getNamePack().replace('.', '_') + "_1.png";
					String server2 = (link + "/" + app.getStyleApp() + "/" + app.getNameApp() + 
							"/" + imageServer).replace(" ", "%20");
					String device2 = dir + "/" + name2;
					boolean success2 = downloadImage(server2, device2);
					if(success2 == true){
						app.setLinkIconDeviceDep(device2);
					}else{
						app.setLinkIconDeviceDep("");
					}
				}else{
					app.setLinkIconDeviceDep("");
				}
			}
		}
	}
	
	private void parseLinkImage(String dir, 
		TreeMap<String, ArrayList<ItemApp>> listApp){
		for (Entry<String, ArrayList<ItemApp>> object : listApp.entrySet()) {
			ArrayList<ItemApp> list = object.getValue();
			for (int i = 0; i < list.size(); i++) {
				ItemApp app = list.get(i);
				String name = app.getNamePack().replace('.', '_') + ".png";
				String device = dir + "/" + name;
				app.setLinkIconDevice(device);
			}
		}
	}
	
	private boolean downloadImage(String link, String file) {
		// MyLog.e(TAB, "IMAGE : " + link);
		File f = new File(file);
		if (f.exists()) {
			return true;
		}
		int MAXLENGHT = 1024;
		HttpURLConnection connection = null;
		try {
			URL url = new URL(link);
			connection = (HttpURLConnection) url.openConnection();
			connection.connect();
			int state = connection.getResponseCode();
			if (state == HttpURLConnection.HTTP_OK) {
				int length = connection.getContentLength();
				if (length <= 0) {
					return false;
				}
				byte bytes[] = new byte[MAXLENGHT];
				InputStream inputStream = connection.getInputStream();
				FileOutputStream fileStream = new FileOutputStream(file);
				int count;
				while ((count = inputStream.read(bytes, 0, MAXLENGHT)) != -1) {
					fileStream.write(bytes, 0, count);
				}
				inputStream.close();
				fileStream.close();
				inputStream = null;
				fileStream = null;
			}else{
				MyLog.e(TAB, "CAN NOT DOWNLOAD : " + state + " | " + link);
			}
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} 
	}
	
	private void parseInstallApp(Context context, TreeMap<String, ArrayList<ItemApp>> listApp) {
		for (Entry<String, ArrayList<ItemApp>> object : listApp.entrySet()) {
			ArrayList<ItemApp> list = object.getValue();
			for (int i = 0; i < list.size(); i++) {
				ItemApp app = list.get(i);
				String name = app.getNamePack();
				app.setInstaller(isAppInstalled(context, name));
			}
		}
	}
	
	private boolean isAppInstalled(Context context, String packageName){
	    try {
	        context.getPackageManager().getApplicationInfo(packageName, 0);
	        return true;
	    }catch (PackageManager.NameNotFoundException e) {
	        return false;
	    }
	}
	
	private void logListItemApp(TreeMap<String, ArrayList<ItemApp>> listApp){
		for (Entry<String, ArrayList<ItemApp>> object : listApp.entrySet()) {
			MyLog.i(TAB, "===========================");
			MyLog.i(TAB, "            ");
			ArrayList<ItemApp> list = object.getValue();
			MyLog.e(TAB, "styleAPP : " + object.getKey());
			for (int i = 0; i < list.size(); i++) {
				ItemApp app = list.get(i);
				MyLog.i(TAB, "nameAPP : " + app.getNameApp());
				MyLog.i(TAB, "namePACK : " + app.getNamePack());
				MyLog.i(TAB, "linkPACK : " + app.getLinkPack());
				MyLog.i(TAB, "linkImage : " + app.getLinkIconServer());
				MyLog.i(TAB, "            ");
			}
			MyLog.i(TAB, "===========================");
		}
	}
	
	private void showListData(ArrayList<ItemApp> list){
		MyLog.e(TAB, "========================================");
		for (int i = 0; i < list.size(); i++) {
			ItemApp app = list.get(i);
			MyLog.e(TAB, "showListData : " + app.getNameApp());
			MyLog.e(TAB, "showListData : " + app.getNamePack());
			MyLog.e(TAB, "showListData : " + app.getVersion());
			MyLog.e(TAB, "========================================");
		}
	}
	
	
}
