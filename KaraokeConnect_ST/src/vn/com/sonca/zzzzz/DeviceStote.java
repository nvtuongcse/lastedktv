package vn.com.sonca.zzzzz;

import java.util.ArrayList;
import java.util.Map;

import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.params.SKServer;
import android.content.Context;
import android.content.SharedPreferences;

public class DeviceStote {
	
	private String TAB = "DeviceStote";
	private static final String PREF_FILE = "DeviceStore";
	private SharedPreferences settings;
	private Context context;
	
	public DeviceStote(Context context) {
		this.context = context;
		settings = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
	}
	
	public ArrayList<SKServer> getListSaveDevice(){
		Map<String, ?> mapdevice = settings.getAll();
		ArrayList<SKServer> listServers = new ArrayList<SKServer>();
		for(String key : mapdevice.keySet()){
			String data = (String) mapdevice.get(key);
			String[] device = data.split(",");
			if (device.length == 5) {
				SKServer skServer = new SKServer();
				skServer.setConnectedIPStr(key);
				skServer.setName(device[0]);
				skServer.setConnectPass(device[1]);
					int da = 0;
					try{da = Integer.valueOf(device[2]);
					}catch(NumberFormatException ex){da = 0;}
				skServer.setModelDevice(da);
					da = -1;
					try{da = Integer.valueOf(device[3]);
					}catch(NumberFormatException ex){da = -1;}
				skServer.setIrcRemote(da);
					if(device[4].equals("-")){
						skServer.setNameRemote("");
					}else{
						skServer.setNameRemote(device[4]);
					}
				skServer.setState(SKServer.SAVED);
				skServer.setSave(true);
				listServers.add(skServer);
			}
			
			/*
			MyLog.e(TAB, "======= LIST SKSERVER ======");
			for (int i = 0; i < listServers.size(); i++) {
				SKServer skServer = listServers.get(i);
				if(skServer != null){
					MyLog.d(TAB, "name : " + skServer.getName());
					MyLog.d(TAB, "ip : " + skServer.getConnectedIPStr());
					MyLog.d(TAB, "model : " + skServer.getModelDevice());
					MyLog.d(TAB, "remote " + skServer.getIrcRemote() + 
							" : " + skServer.getNameRemote());
					MyLog.d(TAB, "    ");
				}
			}
			MyLog.e(TAB, "============================");
			*/
		}
		return listServers;
	}
	
	public void SaveDevice(SKServer skServer){
		// ArrayList<SKServer> listServers = getListSaveDevice();
		// if(!listServers.contains(skServer)){
			putDevice(skServer);
		// }
	}
	
	public void clearDive(SKServer skServer){
		if(settings != null){
			SharedPreferences.Editor editor = settings.edit();
			editor.remove(skServer.getConnectedIPStr());
			editor.commit();
		}
	}
	
	public void putDevice(SKServer skServer){
		SharedPreferences.Editor editor = settings.edit();
		String nameremote = (skServer.getNameRemote().trim().equals("")) ? 
				"-" : skServer.getNameRemote().trim();
		String value = 
				skServer.getName() + "," + 
				skServer.getConnectPass() + "," + 
				skServer.getModelDevice() + "," +
				skServer.getIrcRemote() + "," + 
				nameremote;
		// MyLog.i(TAB, "put: " + value);
		editor.putString(skServer.getConnectedIPStr(), value);
		editor.commit();
	}
	
	public SKServer getDevice(String ip){
		String data = settings.getString(ip, "");
		String[] device = data.split(",");
		MyLog.e(TAB, "size : " + device.length + " >< data : " + data);
		if (device.length == 5) {
			SKServer skServer = new SKServer();
			skServer.setConnectedIPStr(ip);
			skServer.setName(device[0]);
			skServer.setConnectPass(device[1]);
				int da = 0;
				try{da = Integer.valueOf(device[2]);
				}catch(NumberFormatException ex){da = 0;}
			skServer.setModelDevice(da);
				da = -1;
				try{da = Integer.valueOf(device[3]);
				}catch(NumberFormatException ex){da = -1;}
			skServer.setIrcRemote(da);
				if(device[4].equals("-")){
					skServer.setNameRemote("");
				}else{
					skServer.setNameRemote(device[4]);
				}
			skServer.setState(SKServer.SAVED);
			skServer.setSave(true);
			return skServer;
		}else{
			return null;
		}
	}

	//-------------------------------------//
	
	public void setShareRefresh(boolean value){
		SharedPreferences settings = context.getSharedPreferences("REFRESH_DEVICE", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean("bOffUserList", value);
		editor.commit();
	}
	
	public boolean getShareRefresh(){
		SharedPreferences settings = context.getSharedPreferences("REFRESH_DEVICE", Context.MODE_PRIVATE);
		return settings.getBoolean("bOffUserList", false);
	}
	
	
}
