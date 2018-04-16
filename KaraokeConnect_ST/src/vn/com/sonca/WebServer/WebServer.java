package vn.com.sonca.WebServer;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpRequestHandlerRegistry;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;

import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.zzzzz.MyApplication;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.text.format.Formatter;

public class WebServer extends Thread {
	private static final String SERVER_NAME = "AndWebServer";
	private static final String ALL_PATTERN = "*";
	private static final String REQUEST_FORMAT_PATTERN = "/default.aspx*";

	private boolean isRunning = false;
	private Context context = null;
	private int serverPort = 0;

	private BasicHttpProcessor httpproc = null;
	private BasicHttpContext httpContext = null;
	private HttpService httpService = null;
	private HttpRequestHandlerRegistry registry = null;
	private NotificationManager notifyManager = null;

	public WebServer(Context context, NotificationManager notifyManager) {
		super(SERVER_NAME);
		MyLog.e("WebServer", "CREATE START.......................");

		this.setContext(context);
		this.setNotifyManager(notifyManager);

		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);

		serverPort = Integer
				.parseInt(pref.getString(Constants.PREF_SERVER_PORT, ""
						+ Constants.DEFAULT_SERVER_PORT));
		httpproc = new BasicHttpProcessor();
		httpContext = new BasicHttpContext();

		httpproc.addInterceptor(new ResponseDate());
		httpproc.addInterceptor(new ResponseServer());
		httpproc.addInterceptor(new ResponseContent());
		httpproc.addInterceptor(new ResponseConnControl());

		httpService = new HttpService(httpproc,
				new DefaultConnectionReuseStrategy(),
				new DefaultHttpResponseFactory());

		registry = new HttpRequestHandlerRegistry();

		registry.register(ALL_PATTERN, new NotFoundPageHandler(context));
		registry.register(REQUEST_FORMAT_PATTERN, new RequestFormatPageHandler(context));

		httpService.setHandlerResolver(registry);

		MyLog.e("", "serverPort = " + serverPort);
//		MyLog.e("getLocalIpAddress", Utility.getLocalIpAddress());
//		// GET IP ADDRESS OF WFIF SERVER
//		WifiManager wifiMgr = (WifiManager) context.getSystemService(android.content.Context.WIFI_SERVICE);
//		WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
//		int ip = wifiInfo.getIpAddress();
//		String ipAddress = Formatter.formatIpAddress(ip);
//		MyLog.e("ipAddress", ipAddress + "");

		MyLog.e("WebServer", "CREATE END.......................");
	}

	private ServerSocket serverSocket;
	public static String requestString;

	@Override
	public void run() {
		super.run();

		try {
			serverSocket = new ServerSocket();						
			serverSocket.setReuseAddress(true);
			serverSocket.bind(new InetSocketAddress(serverPort));
			
			MyApplication.updateFirmwareServerSocket = serverSocket;

			while (isRunning) {
				try {
					MyLog.e(" "," ");
					MyLog.e("WebServer", "Waiting...............");
					Socket socket = serverSocket.accept();		

					DefaultHttpServerConnection serverConnection = new DefaultHttpServerConnection();

					serverConnection.bind(socket, new BasicHttpParams());

					httpService.handleRequest(serverConnection, httpContext);

					serverConnection.shutdown();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			serverSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void startThread() {
//		MyLog.e("WebServer", "startThread...............");
		isRunning = true;

		super.start();
	}

	public synchronized void stopThread() {
//		MyLog.e("WebServer", "stopThread...............");
		isRunning = false;
	}

	public void setNotifyManager(NotificationManager notifyManager) {
		this.notifyManager = notifyManager;
	}

	public NotificationManager getNotifyManager() {
		return notifyManager;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public Context getContext() {
		return context;
	}
}
