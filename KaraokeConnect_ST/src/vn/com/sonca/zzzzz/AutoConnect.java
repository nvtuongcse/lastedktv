package vn.com.sonca.zzzzz;

import java.io.IOException;
import java.util.ArrayList;

import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.params.SKServer;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

public class AutoConnect extends AsyncTask<Void, Void, Integer> {
	
	private String TAB = "AutoConnect";
	private Activity activity;
	private String ipdevice = "";
	private String passdevice = "";
	private String namedevice = "";
	private boolean isConnect = false;
	private ArrayList<SKServer> listDevice;
		
	public AutoConnect(ArrayList<SKServer> listDevice, Activity activity) {
		this.listDevice = listDevice;
		this.activity = activity;
	}
	
	private OnConnectDeviceListener listener;
	public interface OnConnectDeviceListener{
		public void OnFinishAutoConnect();
		public void OnConnectListener(boolean isConnect, String ip, String pass, String name);
	}
	
	public void setOnConnectDeviceListener(OnConnectDeviceListener listener){
		this.listener = listener;
	}

	@Override
	protected Integer doInBackground(Void... params) {
		isConnect = false;
		boolean isLast = ((MyApplication) activity.getApplication())
				.getLastConnectedServer();
		if (isLast) {
			SKServer server = ((MyApplication) activity.getApplication())
					.getDeviceCurrent();
			if (server != null) {
				if (isCancelled())
					return -1;
				namedevice = server.getName();
				ipdevice = server.getConnectedIPStr();
				passdevice = server.getConnectPass();
				isConnect = true;
				return 0;
			}
		}
		
		MyLog.d(TAB, "listDevice :  " + listDevice.size());
		for (int i = 0; i < listDevice.size(); i++) {
			if(isCancelled()) return -1;
			SKServer server = listDevice.get(i);
			if (server != null) {
				MyLog.d(TAB, "         ");
				MyLog.d(TAB, "ip : " + server.getConnectedIPStr());
				MyLog.d(TAB, "state : " + server.getState());
				MyLog.d(TAB, "pass : " + server.isSave());
				if (server.isSave() == true) {
					boolean enable = executeCommandPing(server.getConnectedIPStr());
					MyLog.d(TAB, "enable : " + enable);
					MyLog.d(TAB, "         ");
					if(isCancelled()) return -1;
					if (enable) {
						namedevice = server.getName();
						ipdevice = server.getConnectedIPStr();
						passdevice = server.getConnectPass();
						isConnect = true;
						return 0;
					}
				}
			}
		}
		return -1;
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		MyLog.d(TAB, "result = " + result);
		if(listener != null){
			if(result == 0){
				listener.OnConnectListener(isConnect, ipdevice, passdevice, namedevice);
			}else{
				listener.OnFinishAutoConnect();
			}
		}
	}
	
	@Override
	protected void onCancelled() {
		super.onCancelled();
		MyLog.d(TAB, "onCancelled()");
		if(listener != null){
			listener.OnFinishAutoConnect();
		}
	}
	
	private boolean executeCommandPing(String ipStr) {
		Runtime runtime = Runtime.getRuntime();
		try {
			Process mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 -W 1 "
					+ ipStr);
			int mExitValue = mIpAddrProcess.waitFor();
			if (mExitValue == 0) {
				return true;
			} else {
				return false;
			}
		} catch (InterruptedException ignore) {
			ignore.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

}
