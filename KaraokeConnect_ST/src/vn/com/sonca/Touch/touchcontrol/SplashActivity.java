package vn.com.sonca.Touch.touchcontrol;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;

import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.database.DBInstance;
import vn.com.sonca.database.DBInterface;
import vn.com.sonca.database.DbHelper;
import vn.com.sonca.mac.AESUtils;
import vn.com.sonca.mac.DataCrypto;
import vn.com.sonca.mac.MacData;
import vn.com.sonca.mac.MacGSON;
import vn.com.sonca.mac.MacKeyboard;
import vn.com.sonca.mac.MacKeyboard.OnMACKeyboardViewListener;
import vn.com.sonca.mac.MacPopup;
import vn.com.sonca.mac.MacPopupLogin;
import vn.com.sonca.mac.MacPopupLogin.OnMACPopupLoginListener;
import vn.com.sonca.utils.AppSettings;
import vn.com.sonca.zktv.main.KTVMainActivity;
import vn.com.sonca.zzzzz.MyApplication;
import vn.com.hanhphuc.karremote.R;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class SplashActivity extends Activity {
	
	private String serverName = "";
	private final int SPLASH_DISPLAY_LENGTH = 3000;
	private String appRootPath;
	private String packageName;
	private String macFilePath;
	private ProcessHTTPTask processHttpTask;
	private String macAddress;
	private Dialog dialogMAC;
	private Dialog dialogLogin;
	private MacPopup macPopup;
	private MacPopupLogin macPopupLogin;
	private MacKeyboard macKeyboard;

	final String PREFS_MAC = "MacStore";	
	
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		getWindow().getDecorView().setSystemUiVisibility(View.GONE);
		
		setContentView(R.layout.splash_layout);
		
//		if (true) {
//			gotoApp();
//			return;
//		}
		
		hideActionBar();

		// --------------- PREPARE DATA
		packageName = getApplicationContext().getPackageName();
		appRootPath = android.os.Environment.getExternalStorageDirectory()
				.toString()
				.concat(String.format("/%s/%s", "Android/data", packageName));
		File appBundle = new File(appRootPath);
		if (!appBundle.exists())
			appBundle.mkdir();
		macFilePath = appRootPath.concat("/MACLIST");

		// Init dialog
		dialogMAC = new Dialog(this);
		dialogMAC.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogMAC.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialogMAC.setContentView(R.layout.mac_popup);
		dialogMAC.setCanceledOnTouchOutside(true);

		dialogMAC.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
				SplashActivity.this.finish();				
			}
		});
		macPopup = (MacPopup) dialogMAC.findViewById(R.id.macPopup);

		dialogLogin = new Dialog(this);
		dialogLogin.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogLogin.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialogLogin.setContentView(R.layout.mac_popup_login);
		dialogLogin.setCanceledOnTouchOutside(false);
		dialogLogin.setCancelable(false);

		macPopupLogin = (MacPopupLogin) dialogLogin
				.findViewById(R.id.macPopupLogin);
		macPopupLogin.setOnMACPopupLoginListener(new OnMACPopupLoginListener() {

			@Override
			public void OnOK(String user, String pass, String serial) {
				// POST DATA LEN SERVER
				postMacDataToServer(user, pass, macAddress, serial);
			}

			@Override
			public void OnCancel() {
				dialogLogin.dismiss();
				SplashActivity.this.finish();
			}
		});

		macKeyboard = (MacKeyboard) dialogLogin.findViewById(R.id.macKeyboard);
		macKeyboard
				.setOnMACKeyboardViewListener(new OnMACKeyboardViewListener() {

					@Override
					public void onKeyboardClick(char key) {
						macPopupLogin.insertChar(key);
					}
				});
		
		// Get MacAddress
		WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = manager.getConnectionInfo();
		macAddress = info.getMacAddress();
		
		SharedPreferences settingMac = getSharedPreferences(PREFS_MAC, 0);		
		if(macAddress == null){
			macAddress = settingMac.getString("MacAddress", "");
		} else {
			settingMac.edit().putString("MacAddress", macAddress).commit();
		}
				
		if(macAddress == null || macAddress.equals("")){
			macPopup.setLayout(MacPopup.LAYOUT_NOMAC);

			dialogMAC.setCanceledOnTouchOutside(true);
			dialogMAC.setCancelable(true);
			dialogMAC.getWindow().setGravity(Gravity.CENTER);
			WindowManager.LayoutParams params = dialogMAC
					.getWindow().getAttributes();
			dialogMAC.getWindow().setAttributes(params);
			dialogMAC.show();
			return;
		}
		
		CheckOldFileMac();
		
	}
	
	private void CheckOldFileMac() {		
		File macFile = new File(macFilePath);
		if (!macFile.exists()) {
			new RetrieveFeedTask().execute("");			
			return;
		}

		BufferedReader reader = null;
		InputStream is = null;
		StringBuilder sb = new StringBuilder();
		try {
			// PARSE FILE INTO LIST OBJECT
			is = new FileInputStream(macFilePath);
			reader = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
//			Log.d("FILE DATA", sb.toString() + "");
			
			Gson gson = new Gson();
			
			// TODO DECRYPT
//			System.out.println("-------- Decrypt data -------------");
			String dataRecvDecrypt = AESUtils.decryptData(sb.toString());
//			System.out.println("data Decrypt from recv :" + dataRecvDecrypt);
			DataCrypto dataRecv = gson.fromJson(dataRecvDecrypt,
					DataCrypto.class);
			String KeyDecrypt = AESUtils.decryptkey(dataRecv.getKEY());
//			System.out.println("key Decrypt from recv :" + KeyDecrypt);
			String dataDecrypt = AESUtils.decrypt(KeyDecrypt,
					dataRecv.getDATA());
//			System.out.println("data Decrypt from recv :" + dataDecrypt);
			
			MacGSON jsonData = gson.fromJson(dataDecrypt, MacGSON.class);

			List<MacData> macList = jsonData.getMacDatas();
			if (jsonData.getMacDatas().size() == 0) {
				new RetrieveFeedTask().execute("");
				return;
			}

			// CHECK INSIDE LIST
			for (MacData macTemp : macList) {
				if (macTemp.getMacAddress().equalsIgnoreCase(macAddress)) {
					gotoApp();
					return;
				}
			}
			
			new RetrieveFeedTask().execute("");
		} catch (Exception e) {
//			Log.e("MAC CHECK FILE", e.toString());
		} finally {
			try {
				is.close();
				reader.close();
			} catch (Exception e) {
			}
		}
	}
	
	class RetrieveFeedTask extends AsyncTask<String, Void, Void> {

		protected Void doInBackground(String... urls) {
			try {
				if (!isURLReachable(SplashActivity.this)) {
					if (!isURLReachableFromIP(SplashActivity.this)) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								macPopup.setLayout(MacPopup.LAYOUT_NOWIFI);

								dialogMAC.setCanceledOnTouchOutside(true);
								dialogMAC.setCancelable(true);
								dialogMAC.getWindow()
										.setGravity(Gravity.CENTER);
								WindowManager.LayoutParams params = dialogMAC
										.getWindow().getAttributes();
								dialogMAC.getWindow().setAttributes(params);
								dialogMAC.show();
							}
						});
						return null;
					}
				}

				getMacDataFromServer();
				return null;
			} catch (Exception e) {
				return null;
			}
		}

	    protected void onPostExecute(Void feed) {
	    }
	}
	
	private boolean isURLReachable(Context context) {
	    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnected()) {
	        try {
	            URL url = new URL("http://update.soncamedia.com");   // Change to "http://google.com" for www  test.
	            HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
	            urlc.setConnectTimeout(3 * 1000);          // 3 s.
	            urlc.connect();
	            if (urlc.getResponseCode() == 200) {        // 200 = "OK" code (http connection is fine).
	            	serverName = "update.soncamedia.com";
	                return true;
	            } else {
	                return false;
	            }
	        } catch (MalformedURLException e1) {
	            return false;
	        } catch (IOException e) {
	            return false;
	        }
	    }
	    return false;
	}
	
	private boolean isURLReachableFromIP(Context context) {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			try {
				URL url = new URL("http://221.132.17.130");
				HttpURLConnection urlc = (HttpURLConnection) url
						.openConnection();
				urlc.setConnectTimeout(3 * 1000);
				urlc.connect();
				if (urlc.getResponseCode() == 200) {
					serverName = "221.132.17.130";
					return true;
				} else {
					return false;
				}
			} catch (MalformedURLException e1) {
				return false;
			} catch (IOException e) {
				return false;
			}
		}
		return false;
	}

	final String PREFS_DEVICE = "LastDeviceType";
	private void gotoApp() {
		SplashActivity.this.runOnUiThread(new Runnable() {
			public void run() {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						DBInterface.DBReloadDatabase(SplashActivity.this.getApplicationContext(), DbHelper.DBName, DBInstance.TOCTYPE_SK9000, 0, 0, 0); 
						SharedPreferences settings = getSharedPreferences(PREFS_DEVICE, 0);
						settings.edit().putInt("saveType", 0).commit();
						
						AppSettings setting = AppSettings.getInstance(getApplicationContext());	
						int oldScreen = setting.getColorScreen();
						
						Intent mainIntent = null;
						if (oldScreen == MyApplication.SCREEN_BLUE) {
							mainIntent = new Intent(SplashActivity.this, TouchMainActivity.class);
						} else if (oldScreen == MyApplication.SCREEN_KTVUI) {
							mainIntent = new Intent(SplashActivity.this, KTVMainActivity.class);
						}
						SplashActivity.this.startActivity(mainIntent);
						SplashActivity.this.finish();
						
					}
				}, SPLASH_DISPLAY_LENGTH);
			}
		});
	}

	private void CheckFileMac() {
		File macFile = new File(macFilePath);
		if (!macFile.exists()) {
			displayCheckFileWrong();
			return;
		}

		BufferedReader reader = null;
		InputStream is = null;
		StringBuilder sb = new StringBuilder();
		try {
			// PARSE FILE INTO LIST OBJECT
			is = new FileInputStream(macFilePath);
			reader = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			// Log.d("FILE DATA", sb.toString() + "");

			Gson gson = new Gson();

			// TODO DECRYPT
//			 System.out.println("-------- Decrypt data -------------");
			String dataRecvDecrypt = AESUtils.decryptData(sb.toString());
//			 System.out.println("data Decrypt from recv :" + dataRecvDecrypt);
			DataCrypto dataRecv = gson.fromJson(dataRecvDecrypt,
					DataCrypto.class);
			String KeyDecrypt = AESUtils.decryptkey(dataRecv.getKEY());
//			 System.out.println("key Decrypt from recv :" + KeyDecrypt);
			String dataDecrypt = AESUtils.decrypt(KeyDecrypt,
					dataRecv.getDATA());
//			 System.out.println("data Decrypt from recv :" + dataDecrypt);

			MacGSON jsonData = gson.fromJson(dataDecrypt, MacGSON.class);

			List<MacData> macList = jsonData.getMacDatas();
			if (jsonData.getMacDatas().size() == 0) {
				displayCheckFileWrong();
				return;
			}

			// CHECK INSIDE LIST
			for (MacData macTemp : macList) {
				if (macTemp.getMacAddress().equalsIgnoreCase(macAddress)) {
					gotoApp();
					return;
				}
			}

			displayCheckFileWrong();
		} catch (Exception e) {
			// Log.e("MAC CHECK FILE", e.toString());
		} finally {
			try {
				is.close();
				reader.close();
			} catch (Exception e) {
			}
		}
	}

	private void displayCheckFileWrong() {
		SplashActivity.this.runOnUiThread(new Runnable() {
			public void run() {
				macPopup.setLayout(MacPopup.LAYOUT_FILEWRONG);

				dialogMAC.setCanceledOnTouchOutside(true);
				dialogMAC.setCancelable(true);
				dialogMAC.getWindow().setGravity(Gravity.CENTER);
				WindowManager.LayoutParams params = dialogMAC.getWindow()
						.getAttributes();
				dialogMAC.getWindow().setAttributes(params);
				dialogMAC.show();
			}
		});
	}

	public void getMacDataFromServer() {
		processHttpTask = new ProcessHTTPTask("http://" + serverName
				+ "/ServerMacSer/MACSerial", false);
		processHttpTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	private class ProcessHTTPTask extends AsyncTask<Void, Void, Void> {
		private String urlPath;
		private boolean isPost = false;
		private String postUser;
		private String postPass;
		private String postMacAddress;
		private String postSerial;

		public ProcessHTTPTask(String urlPath, boolean isPost) {
			this.urlPath = urlPath;
			this.isPost = isPost;
		}

		public void setPostData(String user, String pass, String macAddress,
				String serial) {
			this.postUser = user;
			this.postPass = pass;
			this.postMacAddress = macAddress;
			this.postSerial = serial;
		}

		@Override
		protected Void doInBackground(Void... urls) {
			BufferedReader reader = null;

			if (isPost) { // SET DATA
				try {
					HttpPost request = new HttpPost(urlPath);
					String jsonSetMac = MacGSON.createSetMacJSON(postUser,
							postPass, postMacAddress, postSerial);
					request.setEntity(new StringEntity(jsonSetMac));

					StringBuilder sb = new StringBuilder();

					// Send request to WCF service
					HttpResponse response = null;
					DefaultHttpClient httpClient = new DefaultHttpClient();
					response = httpClient.execute(request);

					InputStream in = response.getEntity().getContent();
					reader = new BufferedReader(new InputStreamReader(in));
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
					// Log.d("response in SET MAC method", sb.toString() + "");

					Gson gson = new Gson();

					// TODO DECRYPT
//					System.out.println("-------- Decrypt data -------------");
					String dataRecvDecrypt = AESUtils
							.decryptData(sb.toString());
					// System.out.println("data Decrypt from recv :"
					// + dataRecvDecrypt);
					DataCrypto dataRecv = gson.fromJson(dataRecvDecrypt,
							DataCrypto.class);
					String KeyDecrypt = AESUtils.decryptkey(dataRecv.getKEY());
					// System.out.println("key Decrypt from recv :" +
					// KeyDecrypt);
					String dataDecrypt = AESUtils.decrypt(KeyDecrypt,
							dataRecv.getDATA());
					// System.out
					// .println("data Decrypt from recv :" + dataDecrypt);

					String resultJson = dataDecrypt;
					MacGSON jsonObj = gson.fromJson(resultJson, MacGSON.class);
					if (jsonObj.getResult().equalsIgnoreCase("OK")) {
						// IF RESPONSE OK
						MyLog.e("// IF RESPONSE OK","// IF RESPONSE OK");
						getMacDataFromServer();
					} else {
						MyLog.e("// IF RESPONSE FAILK","// IF RESPONSE FAIL");
						// IF RESPONSE FAIL
						dialogLogin.dismiss();
						displayWrongInfo();

						SplashActivity.this.runOnUiThread(new Runnable() {
							public void run() {
								new Handler().postDelayed(new Runnable() {
									@Override
									public void run() {
										dialogMAC.dismiss();
										displayInputLogin();
									}
								}, 2000);
							}
						});
					}
				} catch (Exception e) {
					// Log.e("MAC ERROR", e.toString());
					SplashActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							macPopup.setLayout(MacPopup.LAYOUT_FAILSERVER);

							dialogMAC.setCanceledOnTouchOutside(true);
							dialogMAC.setCancelable(true);
							dialogMAC.getWindow().setGravity(Gravity.CENTER);
							WindowManager.LayoutParams params = dialogMAC
									.getWindow().getAttributes();
							dialogMAC.getWindow().setAttributes(params);
							dialogMAC.show();
						}
					});
				} finally {
					try {
						reader.close();
					} catch (Exception e) {
					}
				}
			} else { // GET DATA
				try {
					HttpPost request = new HttpPost(urlPath);
					String jsonGetMac = MacGSON.createGetMacJSON();
					request.setEntity(new StringEntity(jsonGetMac));

					StringBuilder sb = new StringBuilder();

					// Send request to WCF service
					HttpResponse response = null;
					DefaultHttpClient httpClient = new DefaultHttpClient();
					response = httpClient.execute(request);

					InputStream in = response.getEntity().getContent();
					reader = new BufferedReader(new InputStreamReader(in));
					String line = null;
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
					// Log.d("response in GET MAC method", sb.toString() + "");

					// SAVE FILE IN APP
					writeFile(macFilePath, sb.toString());

					CheckFileMacLogin();

				} catch (Exception e) {
//					Log.e("MAC ERROR", e.toString());
					SplashActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							macPopup.setLayout(MacPopup.LAYOUT_FAILSERVER);

							dialogMAC.setCanceledOnTouchOutside(true);
							dialogMAC.setCancelable(true);
							dialogMAC.getWindow().setGravity(Gravity.CENTER);
							WindowManager.LayoutParams params = dialogMAC
									.getWindow().getAttributes();
							dialogMAC.getWindow().setAttributes(params);
							dialogMAC.show();
						}
					});
				} finally {
					try {
						reader.close();
					} catch (Exception e) {
					}
				}
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
		}
	}

	private void CheckFileMacLogin() {
		File macFile = new File(macFilePath);
		if (!macFile.exists()) {
			displayInputLogin();
			return;
		}

		BufferedReader reader = null;
		InputStream is = null;
		try {
			// PARSE FILE INTO LIST OBJECT
			is = new FileInputStream(macFilePath);
			reader = new BufferedReader(new InputStreamReader(is));

			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}

			// Log.e("LOG CHECK", sb.toString());

			Gson gson = new Gson();

			// TODO DECRYPT
			// System.out.println("-------- Decrypt data -------------");
			String dataRecvDecrypt = AESUtils.decryptData(sb.toString());
			// System.out.println("data Decrypt from recv :" + dataRecvDecrypt);
			DataCrypto dataRecv = gson.fromJson(dataRecvDecrypt,
					DataCrypto.class);
			String KeyDecrypt = AESUtils.decryptkey(dataRecv.getKEY());
			// System.out.println("key Decrypt from recv :" + KeyDecrypt);
			String dataDecrypt = AESUtils.decrypt(KeyDecrypt,
					dataRecv.getDATA());
			// System.out.println("data Decrypt from recv :" + dataDecrypt);

			MacGSON jsonData = gson.fromJson(dataDecrypt, MacGSON.class);

			List<MacData> macList = jsonData.getMacDatas();
			if (jsonData.getMacDatas().size() == 0) {
				displayInputLogin();
				return;
			}

			// CHECK INSIDE LIST
			for (MacData macTemp : macList) {
				if (macTemp.getMacAddress().equalsIgnoreCase(macAddress)) {
					gotoApp();
					return;
				}
			}

			displayInputLogin();
		} catch (Exception e) {
			// Log.e("MAC CHECK FILE", e.toString());
		} finally {
			try {
				is.close();
				reader.close();
			} catch (Exception e) {
			}
		}
	}

	private void displayInputLogin() {
		SplashActivity.this.runOnUiThread(new Runnable() {
			public void run() {
				// PROCESS NHAP USER PASS SERIAL
				dialogLogin.setCanceledOnTouchOutside(false);
				dialogLogin.setCancelable(false);
				dialogLogin.getWindow().setGravity(Gravity.CENTER);
				dialogLogin.getWindow().setBackgroundDrawable(
						new ColorDrawable(Color.TRANSPARENT));
				dialogLogin.getWindow().setLayout(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT);
				WindowManager.LayoutParams params = dialogLogin.getWindow()
						.getAttributes();
				dialogLogin.getWindow().setAttributes(params);
				dialogLogin.show();
			}
		});
	}

	public void postMacDataToServer(String user, String pass,
			String macAddress, String serial) {
		processHttpTask = new ProcessHTTPTask("http://" + serverName
				+ "/ServerMacSer/MACSerial", true);
		processHttpTask.setPostData(user, pass, macAddress, serial);
		processHttpTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}

	private void displayWrongInfo() {
		SplashActivity.this.runOnUiThread(new Runnable() {
			public void run() {
				macPopup.setLayout(MacPopup.LAYOUT_WRONG);

				dialogMAC.setCanceledOnTouchOutside(false);
				dialogMAC.setCancelable(false);
				dialogMAC.getWindow().setGravity(Gravity.CENTER);
				WindowManager.LayoutParams params = dialogMAC.getWindow()
						.getAttributes();
				dialogMAC.getWindow().setAttributes(params);
				dialogMAC.show();
			}
		});
	}

	// //////////////// SUPPORT METHODS
	private void writeFile(String fileName, String data) {
		Writer writer = null;
		try {
			File outputFile = new File(fileName);
			writer = new BufferedWriter(new FileWriter(outputFile));
			writer.write(data);
			writer.close();
		} catch (IOException e) {
			try {
				writer.close();
			} catch (Exception ex) {
			}
		}

	}
	
	private void hideActionBar(){
		setupMainWindowDisplayMode();
//		int apiLevel = android.os.Build.VERSION.SDK_INT;
//		if(apiLevel >= 19){
//			int mUIFlag = View.GONE 
//					| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
//					| View.SYSTEM_UI_FLAG_IMMERSIVE;
//			if (getWindow().getDecorView().getSystemUiVisibility() != mUIFlag) {
//				getWindow().getDecorView().setSystemUiVisibility(mUIFlag);
//			}
//		}else{
//			int mUIFlag = View.GONE;
//			if (getWindow().getDecorView().getSystemUiVisibility() != mUIFlag) {
//				getWindow().getDecorView().setSystemUiVisibility(mUIFlag);
//			}
//		}
	}
	
	private void setupMainWindowDisplayMode() {
	    View decorView = setSystemUiVisilityMode();
	    decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
	      @Override
	      public void onSystemUiVisibilityChange(int visibility) {
	        setSystemUiVisilityMode(); // Needed to avoid exiting immersive_sticky when keyboard is displayed
	      }
	    });
	  }

	  private View setSystemUiVisilityMode() {
	    View decorView = getWindow().getDecorView();
	    int options;
	    options =
	        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
	      | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
	      | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
	      | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
	      | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
	      | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

	    decorView.setSystemUiVisibility(options);
	    return decorView;
	  }

}