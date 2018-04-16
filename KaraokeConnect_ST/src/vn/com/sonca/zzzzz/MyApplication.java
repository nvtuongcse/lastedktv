package vn.com.sonca.zzzzz;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.GatheringByteChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.moonbelly.youtubeFrag.MyYouTubeInfo;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.Hi_W.HiW_FirmwareConfig;
import vn.com.sonca.Touch.Hi_W.HiW_FirmwareInfo;
import vn.com.sonca.Touch.touchcontrol.TouchItemBack;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.database.DBInterface;
import vn.com.sonca.params.ByteUtils;
import vn.com.sonca.params.Download90xxInfo;
import vn.com.sonca.params.FilterItem;
import vn.com.sonca.params.SKServer;
import vn.com.sonca.params.ServerPackage;
import vn.com.sonca.params.ServerStatus;
import vn.com.sonca.params.Song;
import vn.com.sonca.params.SongID;
import vn.com.sonca.params.SongType;
import vn.com.sonca.smartkaraoke.NetworkSocket;
import vn.com.sonca.smartkaraoke.NetworkSocket.Receiver;
import vn.com.sonca.utils.AppSettings;
import vn.com.sonca.utils.FileUtilities;
import vn.sonca.LoadDataServer.ItemApp;
import vn.sonca.LoadDataServer.LoadFileAPK;
import vn.sonca.LoadDataServer.LoadItemApp;
import vn.sonca.LoadDataServer.LoadFileAPK.OnLoadFileAPKListener;
import vn.sonca.LoadDataServer.LoadItemApp.OnLoadItemAppListener;
import android.R.bool;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class MyApplication extends Application implements Receiver {

	public static final long TIMER_SYNC = 500;
	public static final long TIMER_ABCD = 1000;
	public static final long TIMER_EMPTY_PLAYLIST = 2000;
	
	public static final int SelectList_NONE = 0;
	public static final int SelectList_SONCA = 1;
	public static final int SelectList_USER = 2;
	public static final int SelectList_ALL = 3;
	public static int intSelectList = SelectList_SONCA;
	
	public static final int SCREEN_BLUE = 0;
	public static final int SCREEN_GREEN = 1;
	public static final int SCREEN_RED = 2;
	public static final int SCREEN_LIGHT = 3;
	public static final int SCREEN_KTVUI = 4;
	public static int intColorScreen = SCREEN_KTVUI;
	
	public static final String MIDI_FILE = "midi";
	public static final String NameFileLyric = "TiengViet.zip";
	public static final int TextSize = 34;

	public static int intRemoteModel = 0;
	
	public static int intMTocType = 0;
	public static int intPackageSK9x_DISC = 6003;
	public static int intPackageSK9x_HDD = 1701;
	public static int intPackageHIW_DISC = 6207;
	public static int intPackageHIW_HDD = 2201;
	public static long packageHIW_date = 1498452000;
	
	public static boolean flagEverKBX9 = false;
	
	public static final int SONCA = 0;
	public static final int SONCA_KM1 = 1; // giong Kartrol + new spec
	public static final int SONCA_HIW = 2; // 8203R
	public static final int SONCA_KARTROL = 3;
	public static final int SONCA_KM2 = 4; 
	public static final int SONCA_TBT = 5; // giong 8203R
	public static final int SONCA_SMARTK = 6;
	public static final int SONCA_KBX9 = 7; // giong KM2
	public static final int SONCA_SMARTK_801 = 8;
	public static final int SONCA_SMARTK_KM4 = 9;
	public static final int SONCA_KB_OEM = 10; // giong KM2
	public static final int SONCA_KM1_WIFI = 11; // giong KM2
	public static final int SONCA_KB39C_WIFI = 12; // giong KM2, khac lyric
	public static final int SONCA_SMARTK_9108_SYSTEM = 13;
	public static final int SONCA_SMARTK_KM4_SYSTEM = 14;
	public static final int SONCA_NEW = 15;
	public static final int SONCA_SMARTK_CB = 16;
	
	public static int intWifiRemote = SONCA;

	public static int intSvrModel = 0;
	public static int intSvrCode = 0;	
	
	public static boolean flagSmartK_801 = false;
	public static boolean flagSmartK_KM4 = false;
	public static boolean flagSmartK_CB = false;
		
	public static int intCommandEnable = 0xffff; // 0xffff
	public static int intCommandMedium = 0x00000000;	
	public static int intSavedCommandEnable = 0xffff;
	public static int intSavedCommandMedium = 0x00000000;
	
	public static final int MODE_MACDINH_2NAC = -1;
	public static final int MODE_MACDINH_3NAC = 0x00000000;
	public static final int MODE_PHONGHAT_3NAC = 5309781; // 00000000010100010000010101010101
		
	public static boolean flagDeviceUser = false;
	
	public static int levelBatery = 0; // 100 scale
	public static boolean isChargingBatery = false;
	public static int levelWifi = 1; // 5 scale
	
	public static boolean flagSongPlayPause = false; // false -> pause
	
	public static boolean flagSK9xxx_RunningHDD = false;
	public static boolean flagEverConnectHDD = false;
	public static boolean flagEverConnect = false;
	
	public static boolean flagDance = false;
	
	public static HiW_FirmwareInfo curHiW_firmwareInfo;
	public static HiW_FirmwareConfig curHiW_firmwareConfig;
	
	public static List<SongType> mSongTypeList;
	
	public static boolean flagPopupYouTube = false;
	
	public static int intSearchYouTubeMode = 0;
	
	/*
	 * HMINH
	 * false - show all songs
	 * true - only show SonCa song (not user)
	 * */
	public static boolean bOffUserList = true;
	public static boolean flagSupportOffUser = false;
	
	public static boolean bOffFirst = true;
	public static boolean isMoveList = true;
	
	public static int countStartApp = 0;
	public static boolean flagHong = false;
	public static boolean flagNewsmy = false;
		
	public static long freezeTime = 0;
	public static long switchTime = 0;	
	
	public static long timeRevert;
	
	public static boolean flagOnPopup = false;
	
	public static boolean flagOnWifiVideo = false;
	
	public static boolean flagOnAdminYouTube = false; // cho phep vao tab youtbe
	
	public static boolean flagOnAdminOnline = false; // cho phep download bai online	
	
	public static boolean flagSearchOnline = false;
	public static boolean flagYoutubeKaraokeOnly = false;
	
	public static boolean flagNewSongTable = true;
	
	public static boolean flagModelA = false;
	
	public static boolean flagProcessCheckAll = true;
	
	public static boolean flagDisplayTimeout = false;
	
	public static int newAPI90xxVersion = 31; // version 9018 has online APIs, hidden list APIs
	public static final String YT_90xx = "sc_youtube.xml";
	public static final String YT_90xx_OFFLINE = "YOUTUBELIST";
	public static final String YT_90xx_HIDDEN = "HIDDENLIST";
	public static boolean flagInsideHidden = false;
	public static boolean flagAutoDownloading = false;
	public static int intStateHiddenFilter = 0;
	public static ArrayList<Song> listFullOnline = new ArrayList<Song>();
	public static int intVersionDownloaded = 0;
	public static int intDownloadRemain = 0;
	
	public static int new9018API_downloadVersion = 35; // version 9018 change get version list download
	public static byte[] bVersionDownloaded = new byte[32];		
	
	public static boolean flagKTV_OnFragFollow = false;
	public static final int KTV_THELOAI_REMIX = 412;
	
	public static final String ADD_FILE_SAMBA = "addListSamba.xls";
	
	// YOUTUBE
	public static final String YT_FILENAME = "sc_youtube.xls";
	public static final String LIST_OFFLINE = "listOffline.txt";
	public static final String LIST_LUCKYDATA = "vong_xoay.txt";
	public static final String LIST_LUCKYIMAGE = "vongxoayimg";
	public static boolean flagPlayingYouTube = false;
	
	public static boolean flagDownloadYouTube = false;
	public static int youtube_Download_ID = 0;
	public static int youtube_Download_percent = 0;
	
	public static final String ADD_FILE = "addList.xls";
		
	// API SECTION
	public static boolean isAvailableHiddenAPI = false; // API 21
	
	public static boolean flagFinishLoadDBOnline = true;
	public static boolean boolShowKeyBoard = false;
	public static boolean flagRealKeyboard = false;
	
	public static boolean flagAllowSearchOnline = true;
	public static boolean flagAllowAdvSong = true;
	
	public static ServerSocket updateFirmwareServerSocket = null;

	private NetworkSocket socket = null; 
	public SKServer connectedServer = null; 
	private ServerStatus serverStatus = null; 
	public static boolean enableSamba = true; 
	private static String TAB = "MyApplication";
	private static Context context;
	
	private LoadDatabaseSuccessful loadDatabase;
	public interface LoadDatabaseSuccessful{
		public void LoadSuccessful();
	}
	
	public void setLoadDatabaseSuccessful(LoadDatabaseSuccessful loadDatabase){
		this.loadDatabase = loadDatabase;
	}
	
	private DeviceStote deviceStote;
	
	@Override
	public void onCreate() {
		super.onCreate();
		context = getApplicationContext();
		deviceStote = new DeviceStote(getApplicationContext());
		listItemBacks = new ArrayList<TouchItemBack>();
		listDeviceCurrent = new ArrayList<SKServer>();
		listActive = new ArrayList<Song>();
		favourite = new ArrayList<Song>();
		
		loadListDataApp(context);
	}
	
	private boolean enableConnectDevice = true;
	public void onStart(){
		if(socket == null){
			socket = new NetworkSocket(getApplicationContext()); 
		}
		if(connectDevice == null){
			MyLog.e(TAB, "connectDevice == null");
			connectDevice = new ConnectDevice();
			connectDevice.execute();
		}else{
			MyLog.e(TAB, "connectDevice : NOT NULL");
			if(connectDevice.getStatus() == AsyncTask.Status.FINISHED){
				MyLog.e(TAB, "connectDevice : FINISHED");
				connectDevice = null;
				connectDevice = new ConnectDevice();
				connectDevice.execute();
			}
		}
	}
	
	public void onStartWithoutAuto(){
		try {
			socket = new NetworkSocket(getApplicationContext()); 
			socket.searchNearbyDevice(this);
		} catch (Exception ex) {
//			MyLog.e("MainActivity", ex);
		}
	}

	public void onDestroy(){
//		MyLog.e(TAB, "onDestroy()");
		socket = null; 
		if (listDeviceCurrent != null) {
			listDeviceCurrent.clear();
		}
		if (listActive != null) {
			listActive.clear();
		}
	}
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		System.gc();
	}
	
	@Override
	public void onTrimMemory(int level) {
		super.onTrimMemory(level);
		System.gc();
	}
	
	public void hideVirtualKeyboard(EditText et){
		MyLog.e(TAB, "hideVirtualKeyboard()");
		if(et == null){
			return;
		}		
		
		InputMethodManager imm = (InputMethodManager)
				getSystemService(MainActivity.INPUT_METHOD_SERVICE);
		if(imm != null){
			imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
		}
	}
	
//////////////////////////////////////////////////////////////////
	
	public SKServer getDeviceCurrent() {
		if(this.connectedServer != null){
			
			MyLog.e(TAB, "          ");
			MyLog.e(TAB, "DeviceCurrent");
			MyLog.d(TAB, "ip: " + connectedServer.getConnectedIPStr());
			MyLog.d(TAB, "name : " + connectedServer.getName());
			MyLog.d(TAB, "mode : " + connectedServer.getModelDevice());
			MyLog.d(TAB, "irc : " + connectedServer.getIrcRemote());
			MyLog.d(TAB, "remote : " + connectedServer.getNameRemote());
			MyLog.e(TAB, "          ");
			
		}
		return this.connectedServer;
	}

	public void setDeviceCurrent(SKServer deviceCurrent) {
		MyLog.e(TAB, "setDeviceCurrent() - " + deviceCurrent.getConnectedIPStr());
		this.connectedServer = deviceCurrent;
	}
	
///////////////////////////// - BACK FRAGMENT - ////////////////////////////////
	
	private ArrayList<TouchItemBack> listItemBacks;
	
	public void addListBack(TouchItemBack item){
		listItemBacks.add(item);
	}
	
	public TouchItemBack getItemListBack(){
		int size = listItemBacks.size();
		if(size <= 0){
			return null;
		}else{
			TouchItemBack itemBack = listItemBacks.get(size - 1).copyValue();
			listItemBacks.remove(size - 1); 
			return itemBack;
		}
	}
	
	public boolean checkListBack(){
		int size = listItemBacks.size();
		if(size <= 0){
			return false;
		}else{
			return true;
		}
	}
	
	public void clearListBack(){
		listItemBacks.clear();
	}

/////////////////////////////- LOAD DATABASE - /////////////////////////////////
	
	private boolean iSload;
	public boolean isload() {
		return iSload;
	}
	
////////////////////////////////- SERVER - /////////////////////////////////////
	
	private ArrayList<SKServer> listDeviceCurrent;		
	public ArrayList<SKServer> getListServers() {
//		MyLog.e(TAB, "getListServers() - " + listDeviceCurrent.size());
		if(listDeviceCurrent != null){
			if(listDeviceCurrent.size() > 0){
				for (int i = 0; i < listDeviceCurrent.size(); i++) {
					listDeviceCurrent.get(i).setActive(false);
				}
				listDeviceCurrent.get(0).setActive(true);
			}
		}
		return listDeviceCurrent;
	}		
	private ArrayList<SKServer> listInRange;

	public ArrayList<SKServer> getListServersInRange() {
		if (listInRange == null) {
			listInRange = new ArrayList<SKServer>();
		}
		return listInRange;
	}

	public void setListServers(ArrayList<SKServer> listServers) {
		if (listServers == null) {
			listServers = new ArrayList<SKServer>();
		}
		listInRange = listServers;
		listDeviceCurrent.clear();
//		MyLog.e(TAB, "listServers - " + listServers.size());
		ArrayList<SKServer> list = new ArrayList<SKServer>();
		if (deviceStote != null) {
			list = deviceStote.getListSaveDevice();
		}
		for (int i = 0; i < listServers.size(); i++) {
			SKServer skServer = listServers.get(i);
			int ii = list.indexOf(skServer);
			if(ii != -1){
				skServer.setSave(true);
				skServer.setConnectPass(list.get(ii).getConnectPass());
				skServer.setIrcRemote(list.get(ii).getIrcRemote());
				skServer.setNameRemote(list.get(ii).getNameRemote());
				list.remove(skServer);
			}
		}
		listDeviceCurrent.addAll(listServers);
		listDeviceCurrent.addAll(list);

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

	public void SaveDevice(SKServer skServer){
		if(deviceStote != null){
			if(listDeviceCurrent != null){
				skServer.setState(SKServer.CONNECTED);
				deviceStote.SaveDevice(skServer);
				listDeviceCurrent.remove(skServer);
				listDeviceCurrent.add(0, skServer);
			}
		}
	}
	
	public void ClearDevice(SKServer skServer){
		if(deviceStote != null){
			deviceStote.clearDive(skServer);
		}
	}

	
//////////////////////////////// - PLAYLIST - /////////////////////////////////////
	
	public boolean CheckNotifyPlayList(ArrayList<SongID> songIDs){
		if(listActive == null){
			return true;
		}
//		MyLog.e("","list Active");
//		for (Song songssss : listActive) {
//			MyLog.e("", songssss.getId() + " -- " + songssss.getIndex5() + " -- " + songssss.getTypeABC());
//		}		
//		MyLog.e("","END........................");
//		MyLog.d("","songIDs");
//		for (SongID songID : songIDs) {
//			MyLog.d("", songID.songID + " -- " + songID.typeABC);
//		}
//		MyLog.d("","END........................");
		if(listActive.size() == songIDs.size()){			
			for (int i = 0; i < listActive.size(); i++) {
				Song song1 = listActive.get(i);
				SongID song2 = songIDs.get(i);				
				
				if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
						|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
						|| MyApplication.intSvrModel == MyApplication.SONCA_KBX9 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
						|| MyApplication.intSvrModel == MyApplication.SONCA_TBT){
					if(song1.getIndex5() != song2.songID){
							return true;
						}
				} else {
					if(song1.getIndex5() != song2.songID ||
							song1.getTypeABC() != song2.typeABC){
							return true;
						}	
				}
			}
			return false;
		}else{
			return true;
		}
	}
	
	private ArrayList<Song> favourite = null;
	public void getFavourite(){
		AppSettings setting = AppSettings.getInstance(context);
//		if(setting.loadServerLastUpdate() != 0) { 
		if(setting.isUpdated()) {
			if(favourite != null){
				favourite = DBInterface.DBGetFavouriteSongList(0, 0, getApplicationContext());	
			}
		}
	}
	
		//-----------------//
	
	public int CheckSongInPlayListYouTube(MyYouTubeInfo info) {
		if (listActive == null || listActive.size() == 0 || info == null || TouchMainActivity.listRealYoutube == null || TouchMainActivity.listRealYoutube.size() == 0) {
			return -1;
		}

		int mID = -1;
		for (int i = 0; i < TouchMainActivity.listRealYoutube.size(); i++) {
			Song tempSong = TouchMainActivity.listRealYoutube.get(i);
			if(tempSong.getPlayLink().equals(info.getVideoId())){
				mID = tempSong.getId();
				break;
			}
		}
		
		if(mID == -1){
			return -1;
		}
		
		int result = -1;
		for (int i = 0; i < listActive.size(); i++) {	
			if ((listActive.get(i).getIndex5() == mID || listActive.get(i).getIndex5() ==  mID)) {
				result = i;
				break;
			}	
		}
		return result;
	}
	
	public int CheckSongInPlayList(Song song){
		if(listActive == null || listActive.size() == 0 || song == null){
			return -1;
		}
		//return listActive.indexOf(song);
		
		int result = -1;
		for (int i = 0; i < listActive.size(); i++) {		
			if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
					|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
					|| MyApplication.intSvrModel == MyApplication.SONCA_KBX9 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
					|| MyApplication.intSvrModel == MyApplication.SONCA_TBT){
				if ((listActive.get(i).getIndex5() == song.getId() || listActive.get(i).getIndex5() == song.getIndex5())) {
					result = i;
					break;
				}
			} else {
				if ((listActive.get(i).getIndex5() == song.getId() || listActive.get(i).getIndex5() == song.getIndex5())
						&& listActive.get(i).getTypeABC() == song.getTypeABC()) {
					result = i;
					break;
				}	
			}			
		}
		return result;
	}
	
	private ArrayList<Song> listActive;
	public ArrayList<Song> getListActive() {
		return listActive;
	}

	public void setListActive(ArrayList<Song> list) {
		this.listActive = list;
	}
	
	public void addSongIntoPlayList(Song song){
		Song so = song.copySong();
		this.listActive.add(so);
	}
	
	public void addAllIntoPlayList(ArrayList<Song> list){
		listActive.clear();
		listActive.addAll(list);
//		MyLog.e("Application", "new = " + listActive.size() + " | add = " + list.size());
	}
	
	public void clearSongFromPlayList(int song){
		listActive.remove(song);
	}
	
	public int getSizePlayList(){
		int count = 0;
		Song song;
		for (int i = 0; i < listActive.size(); i++) {
			song = listActive.get(i);
			if (song.isActive()) {
				count++;
			}
		}
		return count;
	}
/*
	public void UpdateStateArrayList(ArrayList<Song> arrayList , int offset , int Sum){
		getFavourite();
		if(listActive != null && favourite != null){
			Song[] songs = new Song[arrayList.size()];
			arrayList.toArray(songs);
			int max = ((offset+Sum) > songs.length) ? songs.length : (offset+Sum);
			for (int i = offset ; i < max ; i++) {
				try{
					boolean a = listActive.contains(songs[i].getId());
					boolean f = favourite.contains(songs[i]);
					songs[i].setActive(a);
					songs[i].setFavourite(f);
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
			songs = null;
		}
	}
*/
	public void UpdateStateSong(Song song){
		if(song != null){
			try{
				boolean a = listActive.contains(song);
				boolean f = favourite.contains(song);
				song.setActive(a);
				song.setFavourite(f);
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	
////////////////////////////////////////////////////////////////////////////
	
	public NetworkSocket getSocket(){
		return socket;
	}
	
	public String[] getHello(){
		if(socket != null){
			String data = socket.getUserCaption(this);
			if(data != null){
				return data.split("\n");
			}
		}
		return null;
	}
	
	public boolean getLastConnectedServer(){
		AppSettings setting = AppSettings.getInstance(getApplicationContext());
		String connectedIP = setting.loadServerIP(); 
		if(connectedIP.length() == 0 ) return false; 
		if(executeCommandPing(connectedIP)){
			connectedServer = new SKServer(); 
			connectedServer.setConnectedIPStr(connectedIP);
			connectedServer.setName(setting.loadLastServerName());
			connectedServer.setConnectPass(setting.loadServerPass());
			connectedServer.setModelDevice(setting.loadModeModel());
			connectedServer.setIrcRemote(setting.loadIrcRemote());
			connectedServer.setNameRemote(setting.loadNameRemote());
//			connectedServer.setState(SKServer.CONNECTED);
			return true; 
		}else{
			return false; 
		}
	}
	
	public void setUserCaption(String dong1 , String dong2){
		if(socket != null){
			String newCauChao = String.format("%s\n%s", dong1, dong2); 
			socket.setUserCaption(this, newCauChao);
		}
	}
	
	public String getUserCaption(){
		if(socket != null){
			return socket.getUserCaption(this);
		}
		return null;
	}
	
	public void getAdminPass(){
		if(socket != null){
			socket.getAdminPass(this);
		}
	}
	
	public void getDeviceIsUser(){
		if(socket != null){
			socket.getDeviceIsUser(this);
		}
	}
	
	public void downloadArtistFileInfo(String fileInfoPath , boolean bool){
		if(socket != null){
			socket.downloadArtistFileInfo(this, fileInfoPath, true);
		}
	}
	
	public void downloadArtistFile(String fileInfoPath , String txtFileName){
		if(socket != null){
			socket.downloadArtistFile(this, fileInfoPath, txtFileName);
		}
	}
	
	public void sendCommand(byte CMD , int value){
		if(flagDisplayTimeout){
			if(listener != null){
				listener.deviceInform_Timeout_Show("CMD_BLAHBLAH");
			}
			return;
		}
		
		if(socket != null)
			socket.sendCommand(MyApplication.this, CMD, value);
	}
	
	public void addSongToPlaylist(int songID, int typeABC , int index){
		// MyLog.i(TAB, "addSongToPlaylist : <ID = " + songID + "> : <Type =" + typeABC + ">");
		intCheckLastAdd = System.currentTimeMillis();
		checkLastAddPlayList = songID + "-" + typeABC;
		if(socket != null)
			socket.addSongToPlaylist(MyApplication.this, songID, typeABC , index);
	}
	
	public void addSongToPlaylistYouTube(int songID, int typeABC , int index){
		intCheckLastAdd = System.currentTimeMillis();
		checkLastAddPlayList = songID + "-" + typeABC;
		if(socket != null)
			socket.addSongToPlaylistYouTube(MyApplication.this, songID, typeABC , index);
	}
	
	public void removeSongfromPlaylist(int songID, int typeABC ,  int index){
		// MyLog.i(TAB, "removeSongfromPlaylist : <ID = " + songID + "> : <Type =" + typeABC + ">");
		if(socket != null)
			socket.removeSongfromPlaylist(MyApplication.this, songID, typeABC , index);
	}
	
	public void sortSongPlaylist(ArrayList<SongID> idList ){
		if(socket != null)
			socket.sortSongPlaylist(this, idList);
	}
	
	public void firstReservedSong(int songID, int typeABC , int index){
		// MyLog.i(TAB, "firstReservedSong : <ID = " + songID + "> : <Type =" + typeABC + ">");
		intCheckFirstAdd = System.currentTimeMillis();
		checkLastFirstPlayList = songID + "-" + typeABC;
		if(socket != null)
			socket.firstReservedSong(this, songID, typeABC , index);
	}
	
	public void firstReservedSongYouTube(int songID, int typeABC , int index){
		intCheckFirstAdd = System.currentTimeMillis();
		checkLastFirstPlayList = songID + "-" + typeABC;
		if(socket != null)
			socket.firstReservedSongYouTube(this, songID, typeABC , index);
	}
	
	public void playSongImediately(int songID, int typeABC , int index){
		// MyLog.i(TAB, "playSongImediately : <ID = " + songID + "> : <Type =" + typeABC + ">");
		if(socket != null)
			socket.playSongImediately(this, songID, typeABC , index);
	}
	
	public void addSongToPlaylist_RealYouTube(String vidID, String name){
		if (socket != null) {
			socket.addSongToPlaylistRealYouTube(this, vidID, name);
		}
		
//		Toast.makeText(this, "add -- " + name, Toast.LENGTH_SHORT).show();
	}
	
	public void firstSongToPlaylist_RealYouTube(String vidID, String name, int position){
		if (socket != null) {
			socket.firstSongToPlaylistRealYouTube(this, vidID, name, position);
		}
		
//		Toast.makeText(this, "first -- " + name, Toast.LENGTH_SHORT).show();
	}
	
	public void searchNearbyDevice(){
		if(socket != null){
			isCheckSearchDeviceComplete = false;
			socket.searchNearbyDevice(this);
			MyLog.e(TAB, "searchNearbyDevice() : SEND OK");
		}else{
			MyLog.e(TAB, "searchNearbyDevice() : SOCKET IS NULL");
		}
	}
	
	//-----------------------------//
	
	public void disconnectFromRemoteHost(){
		enableConnectDevice = true;
		if(socket != null)
			socket.disconnectFromRemoteHost(this); 
	}
	
	public void connectToRemoteHost(String host){
		// enableConnectDevice = false;
		if(socket != null)
			socket.connectToRemoteHost(this, host);
	}
	
	public void connectToRemoteHost(String host , String pass){
		if(socket != null)
			socket.connectToRemoteHost(this, host , pass);
	}

	public void authenToRemoteHost(String password){
		if(socket != null)
			socket.authenToRemoteHost(this, password);
	}
	
	//-----------------------------//
	
	public void startSyncServerStatusThread(){
		if(socket != null)
			socket.startSyncServerStatusThread(this); 
	}
	
	public void downloadAddSongFile(String savePath){
		if(socket != null)
			socket.downloadAddSongFile(this, savePath);
	}
	
	public void downloadSK90xxFile(String savePath, String fileRequest){
		if(socket != null)
			socket.downloadSK90xxFile(this, savePath, fileRequest);
	}
	
	public void downloadSambaData(String savePath){
		if(socket != null)
			socket.downloadSambaData(this, savePath);
	}
	
	public void downloadHiddenFile(String savePath){
		if(socket != null)
			socket.downloadHiddenFile(this, savePath);
	}
	
	public void sendHiddenList_90xx(ArrayList<Integer> songList, int expand){
		if(socket != null)
			socket.sendHiddenList_90xx(this, songList, expand);
	}
	
	public void downloadYouTubeFile(String savePath){
		if(socket != null)
			socket.downloadYouTubeFile(this, savePath);
	}
	
	public void downloadOfflineFile(String savePath){
		if(socket != null)
			socket.downloadOfflineFile(this, savePath);
	}
	
	public void downloadLuckyData(String savePath){
		if(socket != null)
			socket.downloadLuckyData(this, savePath);
	}	
	
	public void downloadLuckyImage(String savePath){
		if(socket != null)
			socket.downloadLuckyImage(this, savePath);
	}
	
	public void downloadUpdateFile(String savePath){
		if(socket != null)
			socket.downloadUpdateFile(this, savePath);
	}
	
	public void downloadSingerUpdateFile(String savePath){
		if(socket != null)
			socket.downloadSingerUpdateFile(this, savePath);
	}
	
	public void downloadLyricFile(String savePath){
		if(socket != null)
			socket.downloadLyricFile(this, savePath);
	}
	
	public void downloadLyricMidiFile(String savePath){
		if(socket != null)
			socket.downloadLyricMidiFile(this, savePath);
	}
	
	public void downloadHiddenListFile(String savePath){
		if(socket != null)
			socket.downloadHiddenListFile(this, savePath);
	}
	
	public void cancelSyncServerStatus(){
		if(socket != null)
			socket.cancelSyncServerStatus();
	}
	
	//-------------------------------------//
	
	private OnApplicationListener listener;
	public interface OnApplicationListener{
		public void deviceSearchCompleted(ArrayList<SKServer> servers);
		public void deviceDidConnectWithServer(Boolean success , boolean flagFinish, String srvName);
		public void deviceDidAuthenWithServer(ServerPackage response, int svrModel, int codeVersion);
		public void deviceDidDownloadFile(boolean success, String savePath);
		public void deviceDidCommand(boolean success);
		public void deviceDidReceiveSyncStatus(boolean success, ServerStatus status);
		public void deviceUpdatePlayList(int[] list);
		public void deviceDidConnected(ServerSocket socket);
		public void deviceDidDisconnected();
		public void deviceDidReceiveHeader(int header);
		public void deviceDidReceiveDataOfLength(int loaded, int total);
		public void deviceSendRequestDidFailedWithError(String errMsg);
		public void deviceDidTimedout();
		public void deviceInform_FailedAddSong();
		
		public void deviceInform_Timeout_Show(String commandName);
		public void deviceInform_Timeout_Close();
		public void deviceInform_ShowMessage(String msgString);
		
		public void loadDatabaseWhenLaucherNoConnect();
	}
	
	public void setOnApplicationListener(OnApplicationListener listener){
		this.listener = listener;
	}

	@Override
	public void deviceSearchCompleted(ArrayList<SKServer> servers) {
		if(listener != null){
			isCheckSearchDeviceComplete = true;
			listener.deviceSearchCompleted(servers);
		}
	}
	
	@Override
	public void deviceDidConnectWithServer(Boolean success , boolean flagFinish, String srvName) {
//		MyLog.e(TAB, "deviceDidConnectWithServer()");
//		enableConnectDevice = false;
		if(listener != null){
			if(connectedServer != null){
				connectedServer.setName(srvName);
			}
			listener.deviceDidConnectWithServer(success, flagFinish, srvName);
		}
	}

	@Override
	public void deviceDidAuthenWithServer(ServerPackage response, int svrModel, int codeVersion) {
		if (response == null || response.getStatus() != 0) {
			enableConnectDevice = true;
		}else{
			enableConnectDevice = false;
		}		
		if(listener != null){
			listener.deviceDidAuthenWithServer(response, svrModel, codeVersion);
		}
	}

	@Override
	public void deviceDidDownloadFile(boolean success, String savePath) {
		if(listener != null){
			listener.deviceDidDownloadFile(success, savePath);
		}
	}

	@Override
	public void deviceDidCommand(boolean success) {
		if(listener != null){
			listener.deviceDidCommand(success);
		}
	}

	@Override
	public void deviceDidReceiveSyncStatus(boolean success, ServerStatus status) {
		if(listener != null){
			listener.deviceDidReceiveSyncStatus(success, status);
		}
	}

	@Override
	public void deviceUpdatePlayList(int[] list) {
		if(listener != null){
			listener.deviceUpdatePlayList(list);
		}
	}

	@Override
	public void deviceDidConnected(ServerSocket socket) {
		if(listener != null){
			listener.deviceDidConnected(socket);
		}
	}

	@Override
	public void deviceDidDisconnected() {
		enableConnectDevice = true;
		if(listener != null){
			listener.deviceDidDisconnected();
		}
	}

	@Override
	public void deviceDidReceiveHeader(int header) {
		if(listener != null){
			listener.deviceDidReceiveHeader(header);
		}
	}

	@Override
	public void deviceDidReceiveDataOfLength(int loaded, int total) {
		if(listener != null){
			listener.deviceDidReceiveDataOfLength(loaded, total);
		}
	}

	@Override
	public void deviceSendRequestDidFailedWithError(String errMsg) {
		enableConnectDevice = true;
		if(listener != null){
			listener.deviceSendRequestDidFailedWithError(errMsg);
		}
	}

	@Override
	public void deviceDidTimedout() {
		enableConnectDevice = true;
		if(listener != null){
			listener.deviceDidTimedout();
		}
	}
	
	// HMINH
	public void sendCommandKartrol(byte CMD, int value) {
		if (socket != null)
			socket.sendCommandKartrol(MyApplication.this, CMD, value, intRemoteModel);
	}
	
	public void firstReservedSongKartrol(int songID, int typeABC , int index){
		if(socket != null)
			socket.firstReservedSongKartrol(MyApplication.this, songID, typeABC , index, intRemoteModel);
	}
	
	public void addSongToPlaylistKartrol(int songID, int typeABC , int index){
		if(socket != null)
			socket.addSongToPlaylistKartrol(MyApplication.this, songID, typeABC , index, intRemoteModel);
	}
	
	private boolean isCheckSearchDeviceComplete = true;
	public boolean checkSearchDeviceComplete(){
		return isCheckSearchDeviceComplete;
	}

	public boolean checkEnableConnectDevice(){
		return enableConnectDevice;
	}
	
	private boolean executeCommandPing(String ipStr) {
		Runtime runtime = Runtime.getRuntime();
		try {
			Process mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 -W 1 "
					+ ipStr);
			int mExitValue = mIpAddrProcess.waitFor();
			MyLog.e(TAB, "enable connnect : " + mExitValue);
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
	
	private ConnectDevice connectDevice;
	private class ConnectDevice extends AsyncTask<Void, Void, Void>{
		
		private boolean hasLast = false;
		
		public ConnectDevice() {
			hasLast = false;
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			try {
				hasLast = getLastConnectedServer(); 
				if(hasLast == false){
					/*
					enableConnectDevice = true; 
					if(checkSearchDeviceComplete()){
						searchNearbyDevice();
					}
					*/
				}else{
					if(connectedServer != null){
						MyLog.i(TAB, "connectedServer IP : " + connectedServer.getConnectedIPStr());
						MyLog.i(TAB, "connectedServer Name : " + connectedServer.getName());
						MyLog.i(TAB, "connectedServer Pass : " + connectedServer.getConnectPass());
					}else{
						MyLog.i(TAB, "connectedServer is NULL");
					}
					if(executeCommandPing(connectedServer.getConnectedIPStr())){
						String pass = connectedServer.getConnectPass();	
						if(connectedServer.getModelDevice() == 1){
							pass = "1234";
						}
						if(pass != null && !pass.equals("")){
							connectToRemoteHost(connectedServer.getConnectedIPStr() , pass);
						}else{
							connectToRemoteHost(connectedServer.getConnectedIPStr());
						}
					}					
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(listener != null && hasLast == false){
				listener.loadDatabaseWhenLaucherNoConnect();
			}
		}
		
	}
	
	public void setOnOffUserList(String adminPass, boolean flagOffUserList){
		if(socket != null)
			socket.setOnOffUserList(MyApplication.this, adminPass, flagOffUserList);
	}
	
	public static boolean isShowModelFragment = false;

	
	private int resultTocType;
	public int getTocTypeFromIRModel(int ircModel){
		resultTocType = 4; // default ACNOS
		
		try {
			InputStream xml = context.getResources().openRawResource(R.raw.kartrolmodel);		
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(xml);
			NodeList list = doc.getElementsByTagName("model");
			int size = list.getLength();
			for (int i = 0; i < size; i++) {
				Element element = (Element) list.item(i);
				int toc = Integer.valueOf(element.getAttribute("toc").toString());
				int irc = Integer.valueOf(element.getAttribute("irc").toString());
				if(irc == ircModel){
					resultTocType = toc;
					break;
				}
			}	
		} catch (Exception e) {
			
		}		
		
		return resultTocType;
	}
	
	public int getTocTypeFromModelName(String modelName){
		resultTocType = 4; // default ACNOS
		
		try {
			InputStream xml = context.getResources().openRawResource(R.raw.kartrolmodel);		
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(xml);
			NodeList list = doc.getElementsByTagName("model");
			int size = list.getLength();
			for (int i = 0; i < size; i++) {
				Element element = (Element) list.item(i);
				int toc = Integer.valueOf(element.getAttribute("toc").toString());
				String name = element.getElementsByTagName("name").item(0).getTextContent().toString();
				if(name.equalsIgnoreCase(modelName)){
					resultTocType = toc;
					break;
				}
			}	
		} catch (Exception e) {
			
		}		
		
		return resultTocType;
	}
	
	public void setOnOffAdminControl(String sendPass, String controlData){
		if(socket != null)
			socket.setOnOffAdminControl(MyApplication.this, sendPass, controlData);
	}
	
	public void setOnOffControlFull(String sendPass, String controlData){
		if(socket != null)
			socket.setOnOffControlFull(MyApplication.this, sendPass, controlData);
	}	
	
	public void setCommandEnable(boolean bool){
		bOffFirst = bool;
		if (bool) {
			MyApplication.intCommandEnable |= 2;
		} else {
			MyApplication.intCommandEnable &= (~2);
		}
	}
	
	public boolean getCommandEnable(){
		return (MyApplication.intCommandEnable & 2) == 2;
	}
	
	public static final int INTMEDIUM = 0;
	public void setCommandMedium(int value){
		int clear = 0x00000003;
		MyApplication.intCommandMedium &= (~(clear << INTMEDIUM));
		MyApplication.intCommandMedium |= (value << INTMEDIUM);
		if((MyApplication.flagDeviceUser == true && value != 0) || 
			(MyApplication.flagDeviceUser == false && value == 2)){
			bOffFirst = true;
		}else{
			bOffFirst = false;
		}
	}
	
	public int getCommandMedium(){
		int clear = 0x00000003;
		int retur = (MyApplication.intCommandMedium & (clear << INTMEDIUM)) >> INTMEDIUM;
		return retur;
	}
	
	public boolean isOnFirst(){
		int re = getCommandMedium();
		if(flagDeviceUser){
			if(re == 0){
				return true;
			}else{
				return false;
			}
		}else{
			if(re == 2){
				return false;
			}else{
				return true;
			}
		}
	}

	private OnReceiverAdminPassListener listenerAdminPass;
	public interface OnReceiverAdminPassListener{
		public void OnReceiverAdminPass(String passServer);
	}
	
	public void setOnReceiverAdminPassListener(OnReceiverAdminPassListener listenerAdminPass){
		this.listenerAdminPass = listenerAdminPass;
	}
	
	@Override
	public void deviceSendAdminPass(String adminpass) {
		if(listenerAdminPass != null){
			listenerAdminPass.OnReceiverAdminPass(adminpass);
		}
	}
	
	
	// ---------- RECEIVER DEVICE IS USER
	private OnReceiverDeviceIsUserListener listenerDeviceIsUser;

	public interface OnReceiverDeviceIsUserListener {
		public void OnReceiverDeviceIsUser(String isDeviceUser);
	}

	public void setOnReceiverDeviceIsUserListener(
			OnReceiverDeviceIsUserListener listenerDeviceIsUser) {
		this.listenerDeviceIsUser = listenerDeviceIsUser;
	}
	
	@Override
	public void deviceSendDeviceUser(String isUserDevice) {
		if(listenerDeviceIsUser != null){
			listenerDeviceIsUser.OnReceiverDeviceIsUser(isUserDevice);
		}
	}
	
	// ---------- CHECK MODE PHONG HAT 3 NAC
	public static boolean checkModePhongHat(){
		int oriInt = MyApplication.intCommandMedium;
		
		String oriStr = Integer.toBinaryString(oriInt);
//		MyLog.e("checkModePhongHat", "oldStr = " + oriStr);
		if(oriStr.length() > 26){ // aLoc using last 6bit
			String newStr = oriStr.substring(oriStr.length() - 26, oriStr.length());
			int compareInt = Integer.parseInt(newStr, 2);
//			MyLog.e("", "newStr = " + newStr);
//			MyLog.e("", "compareInt = " + compareInt);
			if(compareInt == MODE_PHONGHAT_3NAC){
				return true;
			}
		} else {
			if(oriInt == MODE_PHONGHAT_3NAC){
				return true;
			}
		}
				
		return false;
	}

	@Override
	public void deviceSendConfigWifi(String configInfo) {
		// TODO Auto-generated method stub
		
	}

	private long intCheckLastAdd = 0;
	private String checkLastAddPlayList = "";
	public boolean CheckDuplicateLastPlayList(String idSong, String id5Song, int typeABC){
		if(MyApplication.intSvrModel == MyApplication.SONCA_KARTROL || MyApplication.intSvrModel == MyApplication.SONCA_KM1){
			return false;
		}
		
		if(checkLastAddPlayList.equals(idSong + "-" + typeABC) ||
			checkLastAddPlayList.equals(id5Song + "-" + typeABC) ){
			if(System.currentTimeMillis() - intCheckLastAdd < TIMER_ABCD){
				return true;
			}
		}
		
		if(listActive == null){
			return false;
		}
		
		if(listActive.size() <= 0){
			return false;
		}
		
		Song song2 = listActive.get(listActive.size() - 1);
		if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
				|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
				|| MyApplication.intSvrModel == MyApplication.SONCA_KBX9 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
				|| MyApplication.intSvrModel == MyApplication.SONCA_TBT){
			if(song2.getIndex5() == Integer.parseInt(idSong)){
				return true;
			}	
		} else {
			if((song2.getIndex5() == Integer.parseInt(id5Song) && song2.getTypeABC() == typeABC) 
				|| (song2.getIndex5() == Integer.parseInt(idSong) && song2.getTypeABC() == typeABC)
			){
				return true;
			}	
		}		
		return false;
	}
	
	private long intCheckFirstAdd = 0;
	private String checkLastFirstPlayList = "";
	public boolean CheckDuplicateFirstPlayList(String idSong, String id5Song, int typeABC){
		if(MyApplication.intSvrModel == MyApplication.SONCA_KARTROL || MyApplication.intSvrModel == MyApplication.SONCA_KM1){
			return false;
		}
		
		if(checkLastFirstPlayList.equals(idSong + "-" + typeABC) ||
			checkLastFirstPlayList.equals(id5Song + "-" + typeABC) ){
			if(System.currentTimeMillis() - intCheckFirstAdd < TIMER_ABCD){
				return true;
			}
		}
		
		if(listActive == null){
			return false;
		}
		
		if(listActive.size() <= 0){
			return false;
		}
		
		Song song1 = listActive.get(0);
		if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
				|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
				|| MyApplication.intSvrModel == MyApplication.SONCA_KBX9 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
				|| MyApplication.intSvrModel == MyApplication.SONCA_TBT){
			if(song1.getIndex5() == Integer.parseInt(idSong)){
				return true;
			}
		} else {
			if((song1.getIndex5() == Integer.parseInt(id5Song) && song1.getTypeABC() == typeABC) 
					|| (song1.getIndex5() == Integer.parseInt(idSong) && song1.getTypeABC() == typeABC)
				){
				return true;
			}	
		}		
		
		return false;
	}
		
	public static final int INTMEDIUM_SCORE_METHOD = 26;
	public static void setCommandMediumScoreMethod(int value){
		int clear = 0x00000003;
		MyApplication.intCommandMedium &= (~(clear << INTMEDIUM_SCORE_METHOD));
		MyApplication.intCommandMedium |= (value << INTMEDIUM_SCORE_METHOD);
	}
	
	public static int getCommandMediumScoreMethod(){
		int clear = 0x00000003;
		int retur = (MyApplication.intCommandMedium & (clear << INTMEDIUM_SCORE_METHOD)) >> INTMEDIUM_SCORE_METHOD;
		return retur;
	}
	
	public List<Integer> getTotalMIDIType(int idSong, String name){
		List<Integer> intType = new ArrayList<Integer>();		
		intType = DBInterface.DBGetTotalTypeABCSong(context, idSong + "", name);		
		return intType;
	}
		
	
	/*
	 * HMINH - HiW get thong tin firmware + get config firmware
	 */
	public void getHIW_Firmware(){
		if(socket != null){
			socket.getHIW_FirmwareInfo(this);
		}
	}
	
	@Override
	public void deviceSendHIW_FirmareInfo(String resultString, String daumay_name,
			String daumay_version, String wifi_version, int wifi_revision) {
		if(listenerHiw_Firmware != null){
			listenerHiw_Firmware.OnReceiverHIW_Firmware(resultString, daumay_name, daumay_version, wifi_version, wifi_revision);
		}
	}
	
	private OnReceiverHIW_FirmwareListener listenerHiw_Firmware;

	public interface OnReceiverHIW_FirmwareListener {
		public void OnReceiverHIW_Firmware(String resultString, String daumay_name,
				String daumay_version, String wifi_version, int wifi_revision);
	}

	public void setOnReceiverHIW_FirmwareListener(
			OnReceiverHIW_FirmwareListener listenerHiw_Firmware) {
		this.listenerHiw_Firmware = listenerHiw_Firmware;
	}
		
	public void getHIW_FirmwareConfig(){
		if(socket != null){
			socket.getHIW_FirmwareConfig(this);
		}
	}
	
	@Override
	public void deviceSendHIW_FirmareConfig(String resultString, int mode,
			String stationID, String stationPass, String apID, String apPass,
			String passConnect, String passAdmin) {
		if(listenerHiw_FirmwareConfig != null){
			listenerHiw_FirmwareConfig.OnReceiverHIW_FirmwareConfig(resultString, mode, stationID, stationPass, apID, apPass, passConnect, passAdmin);
		}
	}
	
	private OnReceiverHIW_FirmwareConfigListener listenerHiw_FirmwareConfig;

	public interface OnReceiverHIW_FirmwareConfigListener {
		public void OnReceiverHIW_FirmwareConfig(String resultString, int mode,
				String stationID, String stationPass, String apID, String apPass,
				String passConnect, String passAdmin);
	}

	public void setOnReceiverHIW_FirmwareConfigListener(
			OnReceiverHIW_FirmwareConfigListener listenerHiw_FirmwareConfig) {
		this.listenerHiw_FirmwareConfig = listenerHiw_FirmwareConfig;
	}
	
	/*
	 * HMINH - HiW set config firmware
	 */
	
	public void setHIW_FirmwareConfig(HiW_FirmwareConfig firmwareConfig) {
		if (socket != null) {
			MyLog.e("setHIW_FirmwareConfig", "START..........................");
			
			MyLog.e("", "getMode = " + firmwareConfig.getMode());
			MyLog.e("", "getStationID = " + firmwareConfig.getStationID());
			MyLog.e("", "getStationPass = " + firmwareConfig.getStationPass());
			MyLog.e("", "getApID = " + firmwareConfig.getApID());
			MyLog.e("", "getApPass = " + firmwareConfig.getApPass());
			MyLog.e("", "getPassConnect = " + firmwareConfig.getPassConnect());
			MyLog.e("", "getPassAdmin = " + firmwareConfig.getPassAdmin());
			
			socket.setHIW_FirmwareConfig(this, firmwareConfig.getMode() + "",
					firmwareConfig.getStationID(),
					firmwareConfig.getStationPass(), firmwareConfig.getApID(),
					firmwareConfig.getApPass(),
					firmwareConfig.getPassConnect(),
					firmwareConfig.getPassAdmin());
			
			MyLog.e("setHIW_FirmwareConfig", "END..........................");
		}
	}
	
	/*
	 * HMINH - HiW get lyric info
	 */
	public void getLyricInfo(){
		if(socket != null){
			socket.getLyricInfo(this);
		}
	}

	public void getLyricVidLink(){
		if(socket != null){
			socket.getLyricVidLink(this);
		}
	}
	
	public void getLyricVidLink(String id, String typeABC){
		if(socket != null){
			socket.getLyricVidLink(this, id, typeABC);
		}
	}
	
	// --------------------------------
	public void sendDonwloadYouTubeCommand(int downloadID, int downloadType){
		if(socket != null){
			socket.sendDonwloadYouTubeCommand(this, downloadID, downloadType);
		}
	}
	
	public void sendDonwloadYouTubeCommand(Download90xxInfo info, int downloadType){
		if(socket != null){
			socket.sendDonwloadYouTubeCommand(this, info, downloadType);
		}
	}
	
	public void sendDonwloadYouTubeCommand(Download90xxInfo info, int downloadType, ArrayList<String> listDownload){
		if(socket != null){
			socket.sendDonwloadYouTubeCommand(this, info, downloadType, listDownload);
		}
	}
	
	// --------------------------------
	@Override
	public void deviceSendLyricVidLink(String resultString) {
		if(listenerLyricVidLink != null){
			listenerLyricVidLink.OnReceiverLyricVidLink(resultString);
		}
	}
	
	private OnReceiverLyricVidLinkListener listenerLyricVidLink;

	public interface OnReceiverLyricVidLinkListener {
		public void OnReceiverLyricVidLink(String resultString);
	}

	public void setOnReceiverLyricVidLinkListener(
			OnReceiverLyricVidLinkListener listenerLyricVidLink) {
		this.listenerLyricVidLink = listenerLyricVidLink;
	}
	
	// --------------------------------
	@Override
	public void deviceSendLyricInfo(String resultString) {
		if(listenerLyricInfo != null){
			listenerLyricInfo.OnReceiverInfoLyric(resultString);
		}
	}
	
	private OnReceiverInfoLyricListener listenerLyricInfo;

	public interface OnReceiverInfoLyricListener {
		public void OnReceiverInfoLyric(String resultString);
	}

	public void setOnReceiverInfoLyricListener(
			OnReceiverInfoLyricListener listenerLyricInfo) {
		this.listenerLyricInfo = listenerLyricInfo;
	}
	
	public void setFirmwareUpdate_Command(String command){
		if(socket != null){
			socket.setFirmwareUpdate_Command(this, command);
		}
	}

	@Override
	public void deviceInform_FailedAddSong() {
		if(listener != null){
			listener.deviceInform_FailedAddSong();
		}
	}
	
	
	public static final int INTCOMMAND_HIDDEN = 536870912;

	public void setCommandMedium_Hidden(boolean bool) {
//		if(!MyApplication.isAvailableHiddenAPI){
//			return;
//		}
		
		if (bool) {
			MyApplication.intCommandMedium |= INTCOMMAND_HIDDEN;
		} else {
			MyApplication.intCommandMedium &= (~INTCOMMAND_HIDDEN);
		}
	}

	public boolean getCommandMedium_Hidden() {
//		if(!MyApplication.isAvailableHiddenAPI){
//			return false;
//		}
		
		return (MyApplication.intCommandMedium & INTCOMMAND_HIDDEN) == INTCOMMAND_HIDDEN;
	}

	@Override
	public void deviceInform_Timeout_Show(String commandName) {
		if(listener != null){
			listener.deviceInform_Timeout_Show(commandName);
		}
	}

	@Override
	public void deviceInform_Timeout_Close() {
		if(listener != null){
			listener.deviceInform_Timeout_Close();
		}
	}

	@Override
	public void deviceInform_ShowMessage(String msgString) {
		if(listener != null){
			listener.deviceInform_ShowMessage(msgString);
		}
	}
	
	public static float convertPixelsToSp(Context context, float px) {
	    float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
	    return px/scaledDensity;
	}
	
	// --------------------------------
		public void getUSBStorageList(){
			if(socket != null){
				socket.getUSBStorageList(this);
			}
		}
		
		@Override
		public void deviceSendUSBStorageList(String listResult) {
			if(listenerUSBStorageList != null){
				listenerUSBStorageList.OnReceiveStorageList(listResult);
			}
		}
		
		private OnReceiverUSBStorageListListener listenerUSBStorageList;

		public interface OnReceiverUSBStorageListListener {
			public void OnReceiveStorageList(String resultString);
		}

		public void setOnReceiverUSBStorageListListener(
				OnReceiverUSBStorageListListener listenerUSBStorageList) {
			this.listenerUSBStorageList = listenerUSBStorageList;
		}
		
		// --------------------------------
		public void getSongFromUSB(String usbNumber) {
			if (socket != null) {
				socket.getSongFromUSB(this, usbNumber);
			}
		}
		

		public void sendTangHoaData(String name, int avarter, int numberFlower){
			if(socket!=null){
				socket.sendTangHoaData(this, name, avarter, numberFlower);
			}
			
		}
		
		public void callXucXacCommand(){
			if(socket!=null){
				socket.callXucXacCommand(this);
			}
			
		}		
		
		public void callLuckyRollCommand(){
			if(socket!=null){
				socket.callLuckyRollCommand(this);
			}
			
		}	

		@Override
		public void deviceSendSongFromUSB(String resultString) {
			if(listenerSongFromUSB != null){
				listenerSongFromUSB.OnReceiveSongFromUSB(resultString);
			}
		}
		

		private OnReceiverSongFromUSBListener listenerSongFromUSB;

		public interface OnReceiverSongFromUSBListener {
			public void OnReceiveSongFromUSB(String resultString);
		}

		public void setOnReceiverSongFromUSBListener(
				OnReceiverSongFromUSBListener listenerSongFromUSB) {
			this.listenerSongFromUSB = listenerSongFromUSB;
		}

	// ---------------------------------
	public void getScoreInfo(){
		if(socket != null){
			socket.getScoreInfo(this);
		}
	}
	
	@Override
	public void deviceSendScoreInfo(List<Integer> listScore) {
		if(listenerScoreInfo != null){
			listenerScoreInfo.OnReceiverScoreInfo(listScore);
		}
	}
	
	private OnReceiverScoreInfoListener listenerScoreInfo;

	public interface OnReceiverScoreInfoListener {
		public void OnReceiverScoreInfo(List<Integer> listScore);
	}

	public void setOnReceiverScoreInfoListener(
			OnReceiverScoreInfoListener listenerScoreInfo) {
		this.listenerScoreInfo = listenerScoreInfo;
	}

	public void requestModifySongList(int modType, int[] ids){
		if(socket!=null){
			socket.requestModifySongList(this, modType, ids);
		}
	}

	public void requestModifySongListNew(int modType, int[] ids){
		if(socket!=null){
			socket.requestModifySongListNew(this, modType, ids);
		}
	}
	
	@Override
	public void deviceSendModifyTOCResult(String resultString) {
		if(listenerModifyTOCResult != null){
			listenerModifyTOCResult.OnReceiveModifyTOCResult(resultString);
		}
	}
	
	private OnReceiverModifyTOCListener listenerModifyTOCResult;

	public interface OnReceiverModifyTOCListener {
		public void OnReceiveModifyTOCResult(String resultString);
	}

	public void setOnReceiverModifyTOCListener(
			OnReceiverModifyTOCListener listenerModifyTOCResult) {
		this.listenerModifyTOCResult = listenerModifyTOCResult;
	}
	
	public static ArrayList<FilterItem> listFilter = new ArrayList<FilterItem>();
	public static ArrayList<Song> processFilterList(ArrayList<Song> listCheck){
		MyLog.e(" ", " ");
		MyLog.e(TAB, "processFilterList");
		if(listFilter == null || listFilter.size() == 0){
			return listCheck;
		}
		
		if(listCheck == null || listCheck.size() == 0){
			return listCheck;
		}
		
		MyLog.d(TAB, "break 1 -- listCheck size = " + listCheck.size());

		ArrayList<Song> listReturn = new ArrayList<Song>();
		for (int i = 0; i < listCheck.size(); i++) {
			boolean flagExist = false;
			int idCheck = listCheck.get(i).getId();
			for (int j = 0; j < listFilter.size(); j++) {
				if(listFilter.get(j).getId() == idCheck){
					flagExist = true;
					break;
				}
			}
			
			MyLog.d(TAB, "break 2 -- i = " + i + " -- exist = " + flagExist);
			
			if(flagExist){
				listReturn.add(listCheck.get(i));
			}
			
		}
		
		MyLog.d(TAB, "break 3 -- listReturn size = " + listReturn.size());
		
		return listReturn;
	}
	
	public static void createDemoFilterList(){
		listFilter = new ArrayList<FilterItem>();
		FilterItem tempItem = new FilterItem();
		for (int i = 1; i < 999999; i++) {
			tempItem = new FilterItem(i, "3+4+5", "58+59+60", "12+13+14");
			listFilter.add(tempItem);
		}
	}
	
	public static void createSubFilterList(){
		// after getting vol data, get sublist from full list
	}
	
	public static ArrayList<Integer> listDownloadOffline = new ArrayList<Integer>();
	public static void generateListOfflineFromString(String data){
		listDownloadOffline = new ArrayList<Integer>();
		if(data == null || data.trim().equals("")){
			return;
		}
		
		if(!data.contains("_")){
			listDownloadOffline.add(Integer.parseInt(data.trim()));
			return;
		}
		
		String[] list = data.split("_");
		for (int i = 0; i < list.length; i++) {
			listDownloadOffline.add(Integer.parseInt(list[i].trim()));
		}
	}
	
	final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex2(byte[] bytes) {
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		String str = new String(hexChars);
		String resultStr = "";
		for (int i = 0; i < str.length(); i++) {
			resultStr += str.charAt(i);
			if(i % 2 == 1){
				resultStr += " ";
			}
			
		}
		return resultStr;
	}
	
	public static void generateListOfflineFromByte(byte[] data){
		listDownloadOffline = new ArrayList<Integer>();
		if(data == null || data.length == 0){
			return;
		}
		
		//MyLog.e(TAB, "generateListOfflineFromByte = " + bytesToHex2(data));
		
		try {
			int count = 0;
			do {
				listDownloadOffline.add(ByteUtils.byteToInt32L(data, 1 + 4 * count));
				count++;
			} while (true);
		} catch (Exception e) {
//			e.printStackTrace();
		}
		
	}
	
	public static ArrayList<Integer> generateListOfflineFromString(String data, boolean noused){
		ArrayList<Integer> mList = new ArrayList<Integer>();
		try {
			if(data == null || data.trim().equals("")){
				return mList;
			}
			
			if(!data.contains("_")){
				mList.add(Integer.parseInt(data.trim()));
				return mList;
			}
			
			String[] list = data.split("_");
			for (int i = 0; i < list.length; i++) {
				mList.add(Integer.parseInt(list[i].trim()));
			}
		} catch (Exception e) {
			
		}		
		
		return mList;
	}
	
	public static boolean checkOfflineSong(int id){
		if(listDownloadOffline == null || listDownloadOffline.size() == 0){
			return false;
		}
		
		for (int i = 0; i < listDownloadOffline.size(); i++) {
			if(id == listDownloadOffline.get(i)){
				return true;
			}
		}
		
		return false;
	}
	
	//-------------------TODO check update app-------------------------	
	public static String getVersionApp(String pack){
		PackageInfo pInfo;
		try {
			pInfo = context.getPackageManager().getPackageInfo(pack, 0);
			return pInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}
	

	public static boolean checkVersionApp(String sold, String snew){
		int[] dold = convertVersion(sold);
		int[] dnew = convertVersion(snew);
		if(dold == null){
			return true;
		}
		if(dnew == null){
			return false;
		}
		int count = Math.min(dold.length, dnew.length);
		for (int i = 0; i < count; i++) {
			if(dnew[i] > dold[i]){
				return true;
			}else if(dnew[i] < dold[i]){
				return false;
			}else{}
		}
		return false;
	}
	
	private static int[] convertVersion(String version){
		if(version == null){
			return null;
		}
		if(version.trim().isEmpty()){
			return null;
		}
		String[] split = version.split(Pattern.quote("."));
		if(split.length == 0){
			return null;
		}
		int[] re = new int[split.length];
		for (int i = 0; i < split.length; i++) {
			try{
				int nguyen = Integer.valueOf(split[i]);
				re[i] = nguyen;
			}catch(NumberFormatException ex){
				ex.printStackTrace();
				re[i] = 0;
			}
		}
		return re;
	}
	
	public static boolean isPackageInstalled(String packagename, Context context) {
	    PackageManager pm = context.getPackageManager();
	    try {
	        pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
	        return true;
	    } catch (NameNotFoundException e) {
	        return false;
	    }
	}
	
	public static boolean checkFullRealInternet(Context context){
		try {
	        Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 -w 1 8.8.8.8");
	        int returnVal = p1.waitFor();
	        boolean reachable = (returnVal==0);
	        return reachable;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
        return false;
	}
	
	public static boolean checkFullInternet(Context context){
		MyLog.d(" ", "=checkFullInternet==");
		return checkFullRealInternet(context);
//		ConnectivityManager conMgr = (ConnectivityManager) context
//                .getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo info = conMgr.getActiveNetworkInfo();
//        //return (info != null && info.isConnected());
//        if(info != null && info.isConnected()){
//        	try {
//    	        Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
//    	        int returnVal = p1.waitFor();
//    	        boolean reachable = (returnVal==0);
//    	        return reachable;
//    	    } catch (Exception e) {
//    	        e.printStackTrace();
//    	    }
//        }
//        return false;
	}
	
	public static boolean isInternetOn(Context context) {

        // get Connectivity Manager object to check connection
		ConnectivityManager connec = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {


            return true;

        } else if (
                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {


            return false;
        }
        return false;
    }
	
	private LoadItemApp loadItemApp;
	private TreeMap<String, ArrayList<ItemApp>> mapListApp;
	public void loadListDataApp(Context context){
		if(!checkFullInternet(context)){
			return;
		}
		MyLog.e(TAB, "loadListDataApp");
		if(loadItemApp == null){
			mapListApp = null;
			mapListApp = new TreeMap<String, ArrayList<ItemApp>>();
			loadItemApp = new LoadItemApp(context, mapListApp);
			loadItemApp.setOnLoadItemAppListener(new OnLoadItemAppListener() {
				@Override public void OnLoadAppFinish() {
					updateApp();
				}

				@Override
				public void OnLoadError(int error) {}
			});
			loadItemApp.execute();
		}else{
			if(loadItemApp.getStatus() == AsyncTask.Status.FINISHED){
				mapListApp = null;
				mapListApp = new TreeMap<String, ArrayList<ItemApp>>();
				loadItemApp = new LoadItemApp(context, mapListApp);
				loadItemApp.setOnLoadItemAppListener(new OnLoadItemAppListener() {
					@Override public void OnLoadAppFinish() {
						updateApp();
					}

					@Override
					public void OnLoadError(int error) {}
				});
				loadItemApp.execute();
			}
		}
	}
	
	private boolean updateApp(){
		MyLog.e(TAB, "updateApp");
		if(mapListApp != null){
			ArrayList<ItemApp> list = mapListApp.get("KaraokeConnect");
			String appPackageName = this.getPackageName();
			MyLog.e(TAB, "updateApp - appPackageName = " + appPackageName);
			if(list != null && !list.isEmpty()){
				ItemApp app = list.get(0);
				if(app != null && app.getNamePack().equals(appPackageName)){
					String sold = MyApplication.getVersionApp(this.getPackageName());
					String snew = app.getVersion();
					boolean check = MyApplication.checkVersionApp(sold, snew);
					if(check == true){
						startDownloadApk(app);
					}
				}
			}
		}
		return false;
	}
	
	private void installAppFromFileAPK(final String link){
		if(link == null || link.equals("")){
			return;
		}
		File file = new File(link);
		if(!file.exists()){
			return;
		}
		MyLog.e(TAB, "installAppFromFileAPK : " + link);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file), 
				"application/vnd.android.package-archive");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);		
	}
	
	private LoadFileAPK loadFileAPK;
	private void startDownloadApk(ItemApp itemApp){
		if(itemApp == null){
			return;
		}
		MyLog.e(TAB, "startDownloadApk");
		if(loadFileAPK == null){
			loadFileAPK = new LoadFileAPK(context, itemApp);
			loadFileAPK.setOnLoadFileAPKListener(new OnLoadFileAPKListener() {
				@Override public void OnLoading(int down, int lenght) {
					
				}
				@Override 
				public void OnLoadFinish(String linkFile, ItemApp itemApp) {
					installAppFromFileAPK(linkFile);
				}
			});
			loadFileAPK.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
		}else{
			if(loadFileAPK.getStatus() == AsyncTask.Status.FINISHED){
				loadFileAPK = new LoadFileAPK(context, itemApp);
				loadFileAPK.setOnLoadFileAPKListener(new OnLoadFileAPKListener() {
					@Override public void OnLoading(int down, int lenght) {
						
					}
					@Override 
					public void OnLoadFinish(String linkFile, ItemApp itemApp) {
						installAppFromFileAPK(linkFile);
					}
				});
				loadFileAPK.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
			}
		}
	}
	
	//-----------------------------------------------
	public static final int INTMEDIUM_YOUTUBE = 28;

	public static void setCommandMediumYouTube(int value) {
		int clear = 0x00000003;
		MyApplication.intCommandMedium &= (~(clear << INTMEDIUM_YOUTUBE));
		MyApplication.intCommandMedium |= (value << INTMEDIUM_YOUTUBE);
	}

	public static int getCommandMediumYouTube() {
		int clear = 0x00000003;
		int retur = (MyApplication.intCommandMedium & (clear << INTMEDIUM_YOUTUBE)) >> INTMEDIUM_YOUTUBE;
		return retur;
	}
		
	//-----------------------------------------------
	public static ArrayList<Integer> processAddHidden90xx(ArrayList<Integer> listAdd){
		ArrayList<Integer> listResult = new ArrayList<Integer>();
		
		try {
			String rootPath = android.os.Environment.getExternalStorageDirectory().toString();
			rootPath = rootPath.concat(String.format("/%s/%s", "Android/data",context.getPackageName()));
			File tempFile = new File(rootPath);
			if (!tempFile.exists())
				tempFile.mkdirs();
			String filePath = rootPath.concat("/YOUTUBE/" + MyApplication.YT_90xx_HIDDEN);
			
			File f = new File(filePath);
			if(!f.exists()){
				return listAdd;
			}
			
			String str = FileUtilities.getStringFromFile(filePath);
			if(str == null || str.trim().equals("") || str.trim().equals(" ")){
				return listAdd;
			}
			
			// list from file
			ArrayList<Integer> listOld = new ArrayList<Integer>();	
			listResult = new ArrayList<Integer>();
			String[] listData = str.split("\n");
			for (int i = 0; i < listData.length; i++) {
				try {
					listOld.add(Integer.parseInt(listData[i].trim()));
					listResult.add(Integer.parseInt(listData[i].trim()));
				} catch (Exception e) {
					
				}
			}
			
			if(listOld.size() == 0){
				return listAdd;
			}
			
			// compare
			for (int i = 0; i < listAdd.size(); i++) {
				int tempInt = listAdd.get(i);
				if(!listOld.contains(tempInt)){
					listResult.add(tempInt);
				}
			}
		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return listResult;
	}
	
	public static ArrayList<Integer> processCancelHidden90xx(ArrayList<Integer> listCancel){
		ArrayList<Integer> listResult = new ArrayList<Integer>();
		
		try {
			String rootPath = android.os.Environment.getExternalStorageDirectory().toString();
			rootPath = rootPath.concat(String.format("/%s/%s", "Android/data",context.getPackageName()));
			File tempFile = new File(rootPath);
			if (!tempFile.exists())
				tempFile.mkdirs();
			String filePath = rootPath.concat("/YOUTUBE/" + MyApplication.YT_90xx_HIDDEN);
			
			File f = new File(filePath);
			if(!f.exists()){
				return null;
			}
			
			String str = FileUtilities.getStringFromFile(filePath);
			if(str == null || str.trim().equals("") || str.trim().equals(" ")){
				return null;
			}
			
			// list from file
			ArrayList<Integer> listOld = new ArrayList<Integer>();	
			listResult = new ArrayList<Integer>();
			String[] listData = str.split("\n");
			for (int i = 0; i < listData.length; i++) {
				try {
					listOld.add(Integer.parseInt(listData[i].trim()));
				} catch (Exception e) {
					
				}
			}
			
			if(listOld.size() == 0){
				return null;
			}
			
//			MyLog.e(TAB, "listOld = " + listOld.size());
//			for (int i = 0; i < listOld.size(); i++) {
//				MyLog.d(TAB, "listOld i = " + i + " -- " + listOld.get(i));
//			}
//			
//			MyLog.e(TAB, "listCancel = " + listCancel.size());
//			for (int i = 0; i < listCancel.size(); i++) {
//				MyLog.d(TAB, "listCancel i = " + i + " -- " + listCancel.get(i));
//			}
			
			// compare
			for (int i = 0; i < listOld.size(); i++) {
				int tempInt = listOld.get(i);
				if(listCancel.contains(tempInt)){
					// nothing
				} else {
					listResult.add(tempInt);
				}
			}
		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return listResult;
	}
	
	public static ArrayList<Integer> listHiddenSK90xx = new ArrayList<Integer>();
	public static ArrayList<Integer> processGetListHidden90xx(){
		ArrayList<Integer> listResult = new ArrayList<Integer>();
		
		try {
			String rootPath = android.os.Environment.getExternalStorageDirectory().toString();
			rootPath = rootPath.concat(String.format("/%s/%s", "Android/data",context.getPackageName()));
			File tempFile = new File(rootPath);
			if (!tempFile.exists())
				tempFile.mkdirs();
			String filePath = rootPath.concat("/YOUTUBE/" + MyApplication.YT_90xx_HIDDEN);
			
			File f = new File(filePath);
			if(!f.exists()){
				return listResult;
			}
			
			String str = FileUtilities.getStringFromFile(filePath);
			if(str == null || str.trim().equals("") || str.trim().equals(" ")){
				return listResult;
			}
			
			// list from file
			listResult = new ArrayList<Integer>();
			String[] listData = str.split("\n");
			for (int i = 0; i < listData.length; i++) {
				try {
					listResult.add(Integer.parseInt(listData[i].trim()));
				} catch (Exception e) {
					
				}
			}		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return listResult;
	}
	
	public static boolean checkInsideListHidden90xx(int id){
//		listHiddenSK90xx = processGetListHidden90xx();
		if(listHiddenSK90xx == null || listHiddenSK90xx.size() == 0){
			return false;
		}
		
		return listHiddenSK90xx.contains(id);
	}
	
	public static String generateSQLListHidden90xx(){
		if(listHiddenSK90xx == null || listHiddenSK90xx.size() == 0){
			return "";
		}
		
		String strResult = "(";
		for (int i = 0; i < listHiddenSK90xx.size(); i++) {
			strResult += listHiddenSK90xx.get(i) + ",";
		}
		if(strResult.endsWith(",")){
			strResult = strResult.substring(0, strResult.length() - 1);
		}
		strResult += ")";
		
		return strResult;
	}
	
	public static void processGenerateHidden90xx(ArrayList<Integer> listGenerate){
		if(listGenerate == null || listGenerate.size() == 0){
			return;
		}
				
		try {
			MyLog.e(" ", " ");MyLog.e(" ", " ");
			MyLog.e(" ", "processGenerateHidden90xx -- " + listGenerate.size());
			MyLog.e(" ", "processGenerateHidden90xx -- " + listGenerate.size());
			MyLog.e(" ", "processGenerateHidden90xx -- " + listGenerate.size());
			MyLog.e(" ", " ");MyLog.e(" ", " ");
			
			String rootPath = android.os.Environment.getExternalStorageDirectory().toString();
			rootPath = rootPath.concat(String.format("/%s/%s", "Android/data",context.getPackageName()));
			File tempFile = new File(rootPath);
			if (!tempFile.exists())
				tempFile.mkdirs();
			String filePath = rootPath.concat("/YOUTUBE/" + MyApplication.YT_90xx_HIDDEN);
			
			File f = new File(filePath);
//			if(f.exists()){
//				f.delete();
//			}
			
			String data = "";
			for (int i = 0; i < listGenerate.size(); i++) {
				data += listGenerate.get(i) + "\r\n";
			}			
			
			new FileUtilities(context).write(rootPath + "/YOUTUBE", MyApplication.YT_90xx_HIDDEN, data);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void deviceSendSoundData(String resultString) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deviceSendSoundDataByte(byte[] resultByte) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void echoDeviceSendNRPNCheckCRC(byte[] bs, byte[] Result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void echoDeviceSendEspRequestConfig(byte[] Result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void echoDeviceespSendNRPN(Boolean Result, int nrpn, int value) {
		// TODO Auto-generated method stub
		
	}
	
}
