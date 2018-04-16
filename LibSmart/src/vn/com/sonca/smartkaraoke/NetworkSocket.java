package vn.com.sonca.smartkaraoke;

import java.io.ByteArrayOutputStream;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.util.ByteArrayBuffer;

import vn.com.sonca.RemoteControl.ConvertData;
import vn.com.sonca.RemoteControl.RemoteIRCode;
import vn.com.sonca.params.ByteUtils;
import vn.com.sonca.params.ConvertString;
import vn.com.sonca.params.Download90xxInfo;
import vn.com.sonca.params.RealYouTubeInfo;
import vn.com.sonca.params.SKServer;
import vn.com.sonca.params.ServerPackage;
import vn.com.sonca.params.ServerStatus;
import vn.com.sonca.params.SongID;
import vn.com.sonca.smartkaraoke.MapRemote.AppEvent;
import vn.com.sonca.smartkaraoke.NetworkSocket.Receiver;
import android.R.integer;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class NetworkSocket {
//	private String TAB = "NetworkSocket";
	
	// MODEL SET
	private final int SONCA_SK9xxx = 0;
	private final int SONCA_KM1 = 1;
	private final int SONCA_HIW = 2;
	private final int SONCA_KARTROL = 3;
	private final int SONCA_KM2 = 4;
	private final int SONCA_TBT = 5;
	private final int SONCA_SMARTK = 6;
	private final int SONCA_KBX9 = 7;
	private final int SONCA_SMARTK_801 = 8;
	private final int SONCA_SMARTK_KM4 = 9;	
	private final int SONCA_KB_OEM = 10;
	private final int SONCA_KM1_WIFI = 11;
	private final int SONCA_KB39C_WIFI = 12;
	private final int SONCA_SMARTK_9108_SYSTEM = 13;
	private final int SONCA_SMARTK_KM4_SYSTEM = 14;
	private final int SONCA_NEW = 15;
	private final int SONCA_SMARTK_CB = 16;
	
	public enum TYPE_ABC {
		SONG_TYPE_NONE, 
		SONG_TYPE_A, 
		SONG_TYPE_B, 
		SONG_TYPE_C, 
	}
	
	private Socket clientSocket = null;
	private AsynTaskHidden90xx hidden90xxTask = null;
	private AsynTaskAmplyModify amplyModiyTask = null;
	private AsynTaskSongListModify modifySongListTask = null;
	private AsynTaskDownloadYouTube downloadYouTubeTask = null;
	private AsynTaskTangHoa tangHoaTask = null;
	private AsynTaskLuckyRoll luckyRollTask = null;
	private AsynTaskXucXac xucXacTask = null;
	private AsynTaskConfigWifi wifiConfigTask = null;
	private AsynTaskFirmwareUpdate firmwareUpdateTask = null;
	private AsynTaskOnOffControlFull onOffControlFullTask = null;
	private AsynTaskDeviceIsUser deviceIsUserTask = null; 
	private AsynTaskAdminPass adminPassTask = null; 
	private AsynTaskOnOffUserList onOffUserList = null; 
	private AsynTaskAdminControl adminControlTask = null;
	private AsynTaskHiddenList hiddenList = null; 
	private AsynTaskUserCaption userCaption = null; 
	private AsynTaskSearchDevice searchTask = null; 
	private AsynTaskConnectHost connectTask = null; 
	private AsynTaskAuthenHost authenTask = null; 
	private AsynTaskDownloadFileUpdate downloadTask = null; 
	private AsynTaskCommand commandTask = null;
	private AsynTaskCommandKartrol commandKartrolTask = null; 
	private AsynTaskSyncServerStatus syncTask = null; 
	private AsynTaskPlaylistHandle playlistTask = null; 
	private AsynTaskPlaylistKartrolHandle playlistKartrolTask = null;
	
	private AsynTaskHIW_FirmwareConfig hiw_firmwareConfigTask = null; 
	private AsynTaskHIW_FirmwareInfo hiw_firmwareInfoTask = null; 
	
	// LYRIC INFO
	private AsynTaskLyricInfo lyricInfoTask = null;
	private AsynTaskScoreInfo scoreInfoTask = null;
	private AsynTaskLyricVidLink lyricVidLinkTask = null;
	
	// USB
	private AsynTaskUSBStorageList usbStorageListTask = null;
	private AsynTaskSongFromUSB songFromUSBTask = null;
	
	private AsynTaskSoundControl soundControlTask = null;
	
	private int authenID = 0; 
	private int adminPassword = 0; 
	private Timer timerSyncStatus = null; 
	
	private final int SEARCH_TIMEOUT			= (1500); 

	private final int PDATA_HEADER_START     	= (0x00);
	private final int PDATA_LENGTH_START     	= (0x01);
	private final int PDATA_START     			= (0x05);
	
	private final int PDATA_RES_START     = (0x06);	
	
	private static final int REMOTE_CONTROL_PORT = 21111; 
	
	private final int CMD_LISTDEVICE = (0x01);

	private final int CMD_REQ_CONNECT     = (0x11);
	private final int CMD_REQ_AUTHEN      = (0x12);
	private final int CMD_REQ_CONNECT_ECHO     = (0x7E);
	private final int CMD_REQ_AUTHEN_ECHO      = (0x7F);
	/*======echo=========*/
	//Mixer data
	private final int CMD_REQ_MIXER_ASK_CONFIG = 0x13; //Get Config from Wifi to APP
	private final int CMD_REQ_MIXER_SET_NRPN= 0x14; //Each CMD
	private final int CMD_REQ_MIXER_CHECK_CRC= 0x15; //Check changes
	private final int CMD_REQ_MIXER_SEND_ALL= 0x16; //Send all new Config from APP to Wifi
	private final int CMD_REQ_MIXER_SET_DEFAULT= 0x17; //Send 1 CMD from APP to Wifi, force WIFI load Default
	private final int CMD_REQ_MIXER_CHECK_SYNC= 0x18; //Check changes after some secs (1sec)
	private final int CMD_REQ_MIXER_SAVE_CONFIG= 0x19; //S
	/*======echo=========*/	
	private final int CMD_REQ_LOADUPDATE  = (0x21);
	private final int CMD_REQ_LOADSINGERUPDATE  = (0x22);
	private final int CMD_REQ_LOADLYRIC  = (0x23);
	
	private final int CMD_REQ_LOADYOUTUBE  = (0x29);
	private final int CMD_REQ_LOADADDFILE  = (0x2a);
	private final int CMD_TOC_MODIFY  = (0x2b);
	private final int CMD_TOC_MODIFY_NEW  = (0x37);
	private final int CMD_REQ_LOADOFFLINE  = (0x2c);
	private final int CMD_REQ_LOADLUCKY  = (0x2d);
	private final int CMD_REQ_LOADLUCKY_IMG  = (0x2e);
	private final int CMD_REQ_LOADLYRIC_MIDI  = (0x2f);
	private final int CMD_REQ_LOADHIDDEN  = (0x64);
	private final int CMD_REQ_SAMBA  = (0x30);
	
	private final int CMD_REQ_SYNCSERVERSTATUS = (0x24); 
	
	private final int CMD_REQ_LOADMIDI		= (0x26); 
	private final int CMD_REQ_LOADARTIST_INFO  = (0x27);
	private final int CMD_REQ_LOADARTIST_FILE  = (0x28);
	
	private final int CMD_REQ_SYNCTIME	  = (0x25); 
	private final int CMD_REQ_COLORLYRIC_FILE  = (0x33);
	private final int CMD_REQ_VIDLINK	  = (0x34);
	private final int CMD_REQ_USBSTORAGE  = (0x35);
	private final int CMD_REQ_SONGUSB  = (0x36);
	
	private final int CMD_REMOTE_CONTROL  = (0x31);
	private final int CMD_REMOTE_PLAYLIST = (0x32); 
	private final int CMD_SYNC_PLAYLIST   = (0x41); // 0xfc - 0x41
	
	private final int CMD_SET_CAPTION  = (0x51);
	private final int CMD_GET_CAPTION   = (0x52);
	private final int CMD_ADMIN_VALIDATE        =  (0x53);
	private final int CMD_GET_HIDDENLIST        =  (0x56);
	private final int CMD_SET_LIST               = (0x57);
	private final int CMD_REQ_LOADHIDDENLIST     = (0x66);
	private final int CMD_SET_ONOFFUSERLIST  = (0x58);
	private final int CMD_GET_PASSADMIN  = (0x59);
	private final int CMD_SET_ADMINCONTROL  = (0x60);
	private final int CMD_SET_ONOFFCONTROL_FULL  = (0x61);
	private final int CMD_GET_DEVICEUSER  = (0x62);
	private final int CMD_GET_SCORE  = (0x65);
	private final int CMD_SET_FLOWER = (0x67);
	private final int CMD_DOWNLOAD_YOUTUBE = (0x68);
	private final int CMD_CALL_XUCXAC = (0x69);
	private final int CMD_CALL_LUCKYROLL = (0x6a);
	
	private final int CMD_SEND_HIDDEN = (0x6b);
	
	private final int CMD_ESP_FIRMWARE_UPDATE = (0x60);
	private final int CMD_HIW_ABOUT = (0xdc);
	private final int CMD_SET_CONFIGWIFI = (0xdd);
	private final int CMD_GET_CONFIGWIFI = (0xde);
	
	private final int IS_SET_DATA1 = (0xdb);
	private final int IS_GET_EQU = (0xd9);
	private final int IS_SET_EQU = (0xd8);
	
	private final int CMD_PLAYLIST_ADD_SONG			= (0x01); 
	private final int CMD_PLAYLIST_SYNC_SONG			= (0x02); 
	private final int CMD_PLAYLIST_REMOVE_SONG			= (0x03); 
	private final int CMD_PLAYLIST_FIRST_SONG			= (0x04); 
	private final int CMD_PLAYLIST_SORT_SONG      = (0x05);
	private final int CMD_PLAYLIST_PLAY_SONG    = (0x06);
	private final int CMD_PLAYLIST_HIDDEN_1SONG_ON	= (0x07);
	private final int CMD_PLAYLIST_HIDDEN_1SONG_OFF	= (0x08);
	private final int CMD_PLAYLIST_ADD_SONG_YT		= (0x09);
	private final int CMD_PLAYLIST_FIRST_SONG_YT	= (0x0a);
	private final int CMD_PLAYLIST_SONG_YT_REAL	= (0x0b);
	
	private final int CMD_SUCCESS         = (0x00);

	private final String SONCA_HEADER        = ("SCA_KARA");
	private final String SONCA_CONNECT       = ("SCA_CONN");

	private final int DEFAULT_TIMEOUT     = (10000);

	public static final byte REMOTE_CMD_PAUSE        = (0x01);
	public static final byte REMOTE_CMD_STOP         = (0x02);
	public static final byte REMOTE_CMD_PLAY         = (0x03);
	public static final byte REMOTE_CMD_VOLUME       = (0x04);
	public static final byte REMOTE_CMD_VOL_UP       = (0x04);
	public static final byte REMOTE_CMD_VOL_DOWN     = (0x05);
	public static final byte REMOTE_CMD_TEMPO	     = (0x06);
	public static final byte REMOTE_CMD_TEMPO_UP     = (0x06);
	public static final byte REMOTE_CMD_TEMPO_DOWN   = (0x07);
	public static final byte REMOTE_CMD_KEY		     = (0x08);
	public static final byte REMOTE_CMD_KEY_UP       = (0x08);
	public static final byte REMOTE_CMD_KEY_DOWN     = (0x09);
	public static final byte REMOTE_CMD_NEXT         = (0x10);
	public static final byte REMOTE_CMD_REPEAT       = (0x11);
	public static final byte REMOTE_CMD_SCORE        = (0x12);
	public static final byte REMOTE_CMD_MUTE         = (0x13);
	public static final byte REMOTE_CMD_TONE         = (0x14);
	public static final byte REMOTE_CMD_MELODY       = (0x15);
	public static final byte REMOTE_CMD_SINGER		 = (REMOTE_CMD_MELODY); 
	public static final byte REMOTE_CMD_DANCE		 = (0x16); 
	public static final byte REMOTE_CMD_DEFAULT		 = (0x17); 
	public static final byte REMOTE_CMD_CAPTION		 = (0x18);
	
	public static final byte REMOTE_CMD_VOL_DEFAULT	 	= (0x19);
	public static final byte REMOTE_CMD_VOL_OFFSETMIDI	= (0x20);
	public static final byte REMOTE_CMD_VOL_OFFSETKTV	= (0x21);
	public static final byte REMOTE_CMD_VOL_DANCE	 	= (0x22);
	public static final byte REMOTE_CMD_VOL_PIANO	 	= (0x23);
	public static final byte REMOTE_CMD_VOL_MELODY	 	= (0x24);
	public static final byte REMOTE_CMD_VOL_OFFSETKEY 	= (0x25);
	
	private boolean flagEncrypt = false;
	private final String DISK_KEYS = "soncamedia@4216487422";
	private final int MAX_RANDOM = 112412;
	
	private Context context;
	
	public NetworkSocket() {
	}
	
	public NetworkSocket(Context context) {
		this.context = context;
	}
	
	public byte[] prepareSend(int command, byte[] content) {
		int contentLength = content.length;
		byte[] sentPackage = new byte[contentLength + 6]; // 1byte header + 4
															// bytes length +
									// content + 1
															// checksum
		// add header
		sentPackage[0] = (byte) (command & 0xFF);

		// add content length
		ByteUtils.intToBytes(sentPackage, PDATA_LENGTH_START, contentLength);

		// add data
		for (int i = 0; i < contentLength; i++) {
			sentPackage[PDATA_START + i] = content[i];
		}

		// add check sum
		sentPackage[PDATA_START + content.length] = (byte) 0xFF;
		return sentPackage;
	}
	
	private int currentRandomNumber;
	
	public byte[] prepareSendWithEncryption(int command, byte[] content) {
		// prepare before encrypt
		Random random = new Random();
		currentRandomNumber = random.nextInt(MAX_RANDOM) + 1;
		byte[] sentRandom = ByteBuffer.allocate(4).putInt(currentRandomNumber).array();		
		
		byte[] normalSent = prepareSend(command, content);
		// Log.e("normal sent", bytesToHex2(normalSent));
		
		byte[] encryptNormalSent = sha256_encryptData(sentRandom, normalSent);		
		// Log.e("encrypt normal sent", bytesToHex2(encryptNormalSent));
		
		byte[] sentPackage = new byte[encryptNormalSent.length + 8];
		ByteUtils.intToBytes(sentPackage, 0, encryptNormalSent.length);		
				
		System.arraycopy(sentRandom, 0, sentPackage, 4, 4);	
		System.arraycopy(encryptNormalSent, 0, sentPackage, 8, encryptNormalSent.length);		
		// Log.e("new format sent", bytesToHex2(sentPackage));
		
		return sentPackage;
	}
	
	public byte[] decryptResponseData(byte[] data) {
		// prepare before encrypt
		int dataLen = ByteUtils.byteToInt32(data, 0);
		byte[] encryptData = Arrays.copyOfRange(data, 8, 8 + dataLen);
		// Log.e("encrypt data response", bytesToHex2(encryptData));

		//byte[] sentRandom = ByteBuffer.allocate(4).putInt(currentRandomNumber).array();	
		byte[] sentRandom = Arrays.copyOfRange(data, 4, 8);	
		
		//byte[] decryptData = sha256_decryptData(sentRandom, encryptData);		
		byte[] decryptData = sha256_8203rEncryptDecrypt(sentRandom, encryptData);	
		
		// Log.e("decrypt data response", bytesToHex2(encryptData));
		
		return decryptData;
	}
	
	public byte[] processResponseData(byte[] data){
		if(flagEncrypt){
			return decryptResponseData(data);
		} else {
			return data;
		}
	}
	
	public byte[] prepareSendFull(int command, byte[] content) {
		//Log.d("", "=prepareSendFull=command="+command);
		if(flagEncrypt){
			// prepare before encrypt
			Random random = new Random();
			currentRandomNumber = random.nextInt(MAX_RANDOM) + 1;
			byte[] sentRandom = ByteBuffer.allocate(4).putInt(currentRandomNumber).array();		
			
			byte[] normalSent = prepareSend(command, content);
			// Log.e("normal sent", bytesToHex2(normalSent));
			
			//byte[] encryptNormalSent = sha256_encryptData(sentRandom, normalSent);	
			byte[] encryptNormalSent = sha256_8203rEncryptDecrypt(sentRandom, normalSent);	
			
			// Log.e("encrypt normal sent", bytesToHex2(encryptNormalSent));
				
			byte[] sentPackage = new byte[encryptNormalSent.length + 8];
			ByteUtils.intToBytes(sentPackage, 0, encryptNormalSent.length);		
					
			System.arraycopy(sentRandom, 0, sentPackage, 4, 4);	
			System.arraycopy(encryptNormalSent, 0, sentPackage, 8, encryptNormalSent.length);		
			// Log.e("new format sent", bytesToHex2(sentPackage));
			
			return sentPackage;
		} else {
			int contentLength = content.length;
			byte[] sentPackage = new byte[contentLength + 6]; // 1byte header + 4
																// bytes length +
										// content + 1
																// checksum
			// add header
			sentPackage[0] = (byte) (command & 0xFF);

			// add content length
			ByteUtils.intToBytes(sentPackage, PDATA_LENGTH_START, contentLength);

			// add data
			for (int i = 0; i < contentLength; i++) {
				sentPackage[PDATA_START + i] = content[i];
			}

			// add check sum
			sentPackage[PDATA_START + content.length] = (byte) 0xFF;
			return sentPackage;	
		}		
	}

	@Override
	protected void finalize() {
		stopSearchSocket();
	}
	
////////////////////////////////// - FUNCTION - /////////////////////////////////////////////

	public void stopSearchSocket() {
		if (searchTask != null) {
			searchTask.cancel(true);
			searchTask = null;
		}
		if (connectTask != null) {
			connectTask.cancel(true);
			connectTask = null;
		}
	}

	public void searchNearbyDevice(Receiver receiver) {
		if (searchTask != null) {
			searchTask.cancel(true);
			searchTask = null;
		}
		searchTask = new AsynTaskSearchDevice(receiver);
		searchTask.execute();
	}
	
//-------------------------------------------//

	public boolean validateAdminPassword(Receiver receiver, String pass) {
		boolean status = true; 
		if (hiddenList != null) {
			hiddenList.cancel(true); 
			hiddenList = null; 
		}
		
		hiddenList = new AsynTaskHiddenList(receiver);
		hiddenList.setAdminPass(Integer.parseInt(pass)); 
		hiddenList.execute(CMD_ADMIN_VALIDATE); 
		while (!hiddenList.loadCompleted()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
				status = false; 
			}
		}
		return status; 
	}
	
	public boolean updateHiddenList(Receiver receiver, ArrayList<SongID> songlist) {
		boolean status = true; 
		if (hiddenList != null) {
			hiddenList.cancel(true); 
			hiddenList = null; 
		}
		
		hiddenList = new AsynTaskHiddenList(receiver);
		hiddenList.setHiddenList(songlist); 
		hiddenList.execute(CMD_SET_LIST); 
		while (!hiddenList.loadCompleted()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
				status = false; 
			}
		}
		return status; 
	}
	
	public void getConfigWifi(Receiver receiver) {
		if (wifiConfigTask == null) {
			wifiConfigTask = new AsynTaskConfigWifi(receiver);
			wifiConfigTask.execute("0");
		}else{
			if(wifiConfigTask.getStatus() == AsyncTask.Status.FINISHED){
				wifiConfigTask = null;
				wifiConfigTask = new AsynTaskConfigWifi(receiver);
				wifiConfigTask.execute("0");
			}
		}
	}
	
	public void sendHiddenList_90xx(Receiver receiver, ArrayList<Integer> songlist, int expand){		
		if(hidden90xxTask != null){
			hidden90xxTask.cancel(true);
			hidden90xxTask = null;
		}
		
		hidden90xxTask = new AsynTaskHidden90xx(receiver);
		hidden90xxTask.setSongList(songlist);
		hidden90xxTask.execute("0", "" + expand);
		
//		if (hidden90xxTask == null) {
//			hidden90xxTask = new AsynTaskHidden90xx(receiver);
//			hidden90xxTask.setSongList(songlist);
//			hidden90xxTask.execute("0", "" + expand);
//		}else{
//			if(hidden90xxTask.getStatus() == AsyncTask.Status.FINISHED){
//				hidden90xxTask = null;
//				hidden90xxTask = new AsynTaskHidden90xx(receiver);
//				hidden90xxTask.setSongList(songlist);
//				hidden90xxTask.execute("0", "" + expand);
//			}
//		}
	}
	
	public void requestModifySongList(Receiver receiver, int modType, int[] ids){		
		if (modifySongListTask == null) {
			modifySongListTask = new AsynTaskSongListModify(receiver, modType, ids);
			modifySongListTask.execute("0");
		}else{
			if(modifySongListTask.getStatus() == AsyncTask.Status.FINISHED){
				modifySongListTask = null;
				modifySongListTask = new AsynTaskSongListModify(receiver, modType, ids);
				modifySongListTask.execute("0");
			}
		}
	}
	
	public void requestModifySongListNew(Receiver receiver, int modType, int[] ids){		
		if (modifySongListTask == null) {
			modifySongListTask = new AsynTaskSongListModify(receiver, modType, ids);
			modifySongListTask.setNewWay(true);
			modifySongListTask.execute("0");
		}else{
			if(modifySongListTask.getStatus() == AsyncTask.Status.FINISHED){
				modifySongListTask = null;
				modifySongListTask = new AsynTaskSongListModify(receiver, modType, ids);
				modifySongListTask.setNewWay(true);
				modifySongListTask.execute("0");
			}
		}
	}
	
	public void requestModifyAmply(Receiver receiver, int modType, ArrayList<Integer> listValue){
		if (amplyModiyTask == null) {
			amplyModiyTask = new AsynTaskAmplyModify(receiver, modType,listValue);
			amplyModiyTask.execute("0");
		}else{
			if(amplyModiyTask.getStatus() == AsyncTask.Status.FINISHED){
				amplyModiyTask = null;
				amplyModiyTask = new AsynTaskAmplyModify(receiver, modType, listValue);
				amplyModiyTask.execute("0");
			}
		}
	}
	
	public void sendTangHoaData(Receiver receiver, String name, int avarter, int numberFlower){		
		if (tangHoaTask == null) {
			tangHoaTask = new AsynTaskTangHoa(receiver, name, avarter, numberFlower);
			tangHoaTask.execute("0");
		}else{
			if(tangHoaTask.getStatus() == AsyncTask.Status.FINISHED){
				tangHoaTask = null;
				tangHoaTask = new AsynTaskTangHoa(receiver, name, avarter, numberFlower);
				tangHoaTask.execute("0");
			}
		}
	}
	
	public void callLuckyRollCommand(Receiver receiver){		
		if (luckyRollTask == null) {
			luckyRollTask = new AsynTaskLuckyRoll(receiver);
			luckyRollTask.execute("0");
		}else{
			if(luckyRollTask.getStatus() == AsyncTask.Status.FINISHED){
				luckyRollTask = null;
				luckyRollTask = new AsynTaskLuckyRoll(receiver);
				luckyRollTask.execute("0");
			}
		}
	}
	
	public void callXucXacCommand(Receiver receiver){		
		if (xucXacTask == null) {
			xucXacTask = new AsynTaskXucXac(receiver);
			xucXacTask.execute("0");
		}else{
			if(xucXacTask.getStatus() == AsyncTask.Status.FINISHED){
				xucXacTask = null;
				xucXacTask = new AsynTaskXucXac(receiver);
				xucXacTask.execute("0");
			}
		}
	}
	
	public void sendDonwloadYouTubeCommand(Receiver receiver, int downloadID, int downloadType){
		if(downloadYouTubeTask != null){
			downloadYouTubeTask.cancel(true);
			downloadYouTubeTask = null;
		}
		
		downloadYouTubeTask = new AsynTaskDownloadYouTube(receiver, downloadID, downloadType);
		downloadYouTubeTask.execute("0");
		
//		if (downloadYouTubeTask == null) {
//			downloadYouTubeTask = new AsynTaskDownloadYouTube(receiver, downloadID, downloadType);
//			downloadYouTubeTask.execute("0");
//		}else{
//			if(downloadYouTubeTask.getStatus() == AsyncTask.Status.FINISHED){
//				downloadYouTubeTask = null;
//				downloadYouTubeTask = new AsynTaskDownloadYouTube(receiver, downloadID, downloadType);
//				downloadYouTubeTask.execute("0");
//			}
//		}
	}
	
	public void sendDonwloadYouTubeCommand(Receiver receiver, Download90xxInfo info, int downloadType, ArrayList<String> listDownload){
		if(downloadYouTubeTask != null){
			downloadYouTubeTask.cancel(true);
			downloadYouTubeTask = null;
		}
		
		downloadYouTubeTask = new AsynTaskDownloadYouTube(receiver, info, downloadType, listDownload);
		downloadYouTubeTask.execute("0");
		
//		if (downloadYouTubeTask == null) {
//			downloadYouTubeTask = new AsynTaskDownloadYouTube(receiver, info, downloadType, listDownload);
//			downloadYouTubeTask.execute("0");
//		}else{
//			if(downloadYouTubeTask.getStatus() == AsyncTask.Status.FINISHED){
//				downloadYouTubeTask = null;
//				downloadYouTubeTask = new AsynTaskDownloadYouTube(receiver, info, downloadType, listDownload);
//				downloadYouTubeTask.execute("0");
//			}
//		}
	}
	
	public void sendDonwloadYouTubeCommand(Receiver receiver, Download90xxInfo info, int downloadType){	
		if(downloadYouTubeTask != null){
			downloadYouTubeTask.cancel(true);
			downloadYouTubeTask = null;
		}
		
		downloadYouTubeTask = new AsynTaskDownloadYouTube(receiver, info, downloadType);
		downloadYouTubeTask.execute("0");
		
//		if (downloadYouTubeTask == null) {
//			downloadYouTubeTask = new AsynTaskDownloadYouTube(receiver, info, downloadType);
//			downloadYouTubeTask.execute("0");
//		}else{s
//			if(downloadYouTubeTask.getStatus() == AsyncTask.Status.FINISHED){
//				downloadYouTubeTask = null;
//				downloadYouTubeTask = new AsynTaskDownloadYouTube(receiver, info, downloadType);
//				downloadYouTubeTask.execute("0");
//			}
//		}
	}
	
	public void getAdminPass(Receiver receiver) {
		if (adminPassTask == null) {
			adminPassTask = new AsynTaskAdminPass(receiver);
			adminPassTask.execute("0");
		}else{
			if(adminPassTask.getStatus() == AsyncTask.Status.FINISHED){
				adminPassTask = null;
				adminPassTask = new AsynTaskAdminPass(receiver);
				adminPassTask.execute("0");
			}
		}
	}
	
	public void getLyricInfo(Receiver receiver) {
		if (lyricInfoTask == null) {
			lyricInfoTask = new AsynTaskLyricInfo(receiver);
			lyricInfoTask.execute("0");
		}else{
			if(lyricInfoTask.getStatus() == AsyncTask.Status.FINISHED){
				lyricInfoTask = null;
				lyricInfoTask = new AsynTaskLyricInfo(receiver);
				lyricInfoTask.execute("0");
			}
		}
	}
	
	public void getSoundData(Receiver receiver) {
		if (soundControlTask == null) {
			soundControlTask = new AsynTaskSoundControl(receiver);
			soundControlTask.execute("0");
		}else{
			if(soundControlTask.getStatus() == AsyncTask.Status.FINISHED){
				soundControlTask = null;
				soundControlTask = new AsynTaskSoundControl(receiver);
				soundControlTask.execute("0");
			}
		}
	}
	
	public void setSoundData(Receiver receiver, byte[] bData) {
		if (soundControlTask == null) {
			soundControlTask = new AsynTaskSoundControl(receiver);
			soundControlTask.setData(bData);
			soundControlTask.execute("1");
		}else{
			if(soundControlTask.getStatus() == AsyncTask.Status.FINISHED){
				soundControlTask = null;
				soundControlTask = new AsynTaskSoundControl(receiver);
				soundControlTask.setData(bData);
				soundControlTask.execute("1");
			}
		}
	}
	
	public void getLyricVidLink(Receiver receiver) {
		if (lyricVidLinkTask == null) {
			lyricVidLinkTask = new AsynTaskLyricVidLink(receiver);
			lyricVidLinkTask.execute("0");
		}else{
			if(lyricVidLinkTask.getStatus() == AsyncTask.Status.FINISHED){
				lyricVidLinkTask = null;
				lyricVidLinkTask = new AsynTaskLyricVidLink(receiver);
				lyricVidLinkTask.execute("0");
			}
		}
	}
	
	public void getLyricVidLink(Receiver receiver, String id, String typeABC) {
		if (lyricVidLinkTask == null) {
			lyricVidLinkTask = new AsynTaskLyricVidLink(receiver, id, typeABC);
			lyricVidLinkTask.execute("0");
		}else{
			if(lyricVidLinkTask.getStatus() == AsyncTask.Status.FINISHED){
				lyricVidLinkTask = null;
				lyricVidLinkTask = new AsynTaskLyricVidLink(receiver, id, typeABC);
				lyricVidLinkTask.execute("0");
			}
		}
	}
	
	public void getUSBStorageList(Receiver receiver) {
		if (usbStorageListTask == null) {
			usbStorageListTask = new AsynTaskUSBStorageList(receiver);
			usbStorageListTask.execute("0");
		}else{
			if(usbStorageListTask.getStatus() == AsyncTask.Status.FINISHED){
				usbStorageListTask = null;
				usbStorageListTask = new AsynTaskUSBStorageList(receiver);
				usbStorageListTask.execute("0");
			}
		}
	}
	
	public void getUSBStorageList(Receiver receiver, int type) {
		if (usbStorageListTask == null) {
			usbStorageListTask = new AsynTaskUSBStorageList(receiver);
			usbStorageListTask.execute("0", type + "");
		}else{
			if(usbStorageListTask.getStatus() == AsyncTask.Status.FINISHED){
				usbStorageListTask = null;
				usbStorageListTask = new AsynTaskUSBStorageList(receiver);
				usbStorageListTask.execute("0", type + "");
			}
		}
	}
	
	public void getSongFromUSB(Receiver receiver, String usbNumber) {
		if (songFromUSBTask == null) {
			songFromUSBTask = new AsynTaskSongFromUSB(receiver, usbNumber);
			songFromUSBTask.execute("0");
		}else{
			if(songFromUSBTask.getStatus() == AsyncTask.Status.FINISHED){
				songFromUSBTask = null;
				songFromUSBTask = new AsynTaskSongFromUSB(receiver, usbNumber);
				songFromUSBTask.execute("0");
			}
		}
	}
	
	public void getSongFromUSB(Receiver receiver, String usbNumber, String link) {
		if (songFromUSBTask == null) {
			songFromUSBTask = new AsynTaskSongFromUSB(receiver, usbNumber, link);
			songFromUSBTask.execute("0");
		}else{
			if(songFromUSBTask.getStatus() == AsyncTask.Status.FINISHED){
				songFromUSBTask = null;
				songFromUSBTask = new AsynTaskSongFromUSB(receiver, usbNumber, link);
				songFromUSBTask.execute("0");
			}
		}
	}
	
	public void getScoreInfo(Receiver receiver) {
		if (scoreInfoTask == null) {
			scoreInfoTask = new AsynTaskScoreInfo(receiver);
			scoreInfoTask.execute("0");
		}else{
			if(scoreInfoTask.getStatus() == AsyncTask.Status.FINISHED){
				scoreInfoTask = null;
				scoreInfoTask = new AsynTaskScoreInfo(receiver);
				scoreInfoTask.execute("0");
			}
		}
	}
	
	public void getHIW_FirmwareInfo(Receiver receiver) {
		if (hiw_firmwareInfoTask == null) {
			hiw_firmwareInfoTask = new AsynTaskHIW_FirmwareInfo(receiver);
			hiw_firmwareInfoTask.execute("0");
		}else{
			if(hiw_firmwareInfoTask.getStatus() == AsyncTask.Status.FINISHED){
				hiw_firmwareInfoTask = null;
				hiw_firmwareInfoTask = new AsynTaskHIW_FirmwareInfo(receiver);
				hiw_firmwareInfoTask.execute("0");
			}
		}
	}
	
	public void getHIW_FirmwareConfig(Receiver receiver) {
		if (hiw_firmwareConfigTask == null) {
			hiw_firmwareConfigTask = new AsynTaskHIW_FirmwareConfig(receiver);
			hiw_firmwareConfigTask.execute("0");
		}else{
			if(hiw_firmwareConfigTask.getStatus() == AsyncTask.Status.FINISHED){
				hiw_firmwareConfigTask = null;
				hiw_firmwareConfigTask = new AsynTaskHIW_FirmwareConfig(receiver);
				hiw_firmwareConfigTask.execute("0");
			}
		}
	}
	
	public void setHIW_FirmwareConfig(Receiver receiver, String mode,
			String stationID, String stationPass, String apID, String apPass,
			String passConnect, String passAdmin) {
		if (hiw_firmwareConfigTask != null) {
			hiw_firmwareConfigTask.cancel(true);
			hiw_firmwareConfigTask = null;
		}
		hiw_firmwareConfigTask = new AsynTaskHIW_FirmwareConfig(receiver);
		hiw_firmwareConfigTask.execute("1", mode, stationID, stationPass, apID, apPass, passConnect, passAdmin);
//		while (!hiw_firmwareConfigTask.loadCompleted()) {
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
	}
	
	public void getDeviceIsUser(Receiver receiver) {
		if (deviceIsUserTask == null) {
			deviceIsUserTask = new AsynTaskDeviceIsUser(receiver);
			deviceIsUserTask.execute("0");
		}else{
			if(deviceIsUserTask.getStatus() == AsyncTask.Status.FINISHED){
				deviceIsUserTask = null;
				deviceIsUserTask = new AsynTaskDeviceIsUser(receiver);
				deviceIsUserTask.execute("0");
			}
		}
	}
	
	public String getUserCaption(Receiver receiver) {
		if (userCaption != null) {
			userCaption.cancel(true);
			userCaption = null;
		}
		userCaption = new AsynTaskUserCaption(receiver);
		userCaption.execute("0");
		while (!userCaption.loadCompleted()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return userCaption.getResultString(); 
	}
	
	public void setUserCaption(Receiver receiver, String captionStr) {
		if (userCaption != null) {
			userCaption.cancel(true);
			userCaption = null;
		}
		userCaption = new AsynTaskUserCaption(receiver);
		userCaption.execute("1", captionStr);
		while (!userCaption.loadCompleted()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setOnOffUserList(Receiver receiver, String adminPass, boolean offUserList ) {
		if (onOffUserList != null) {
			onOffUserList.cancel(true);
			onOffUserList = null;
		}
		onOffUserList = new AsynTaskOnOffUserList(receiver);
		
		if(offUserList){
			onOffUserList.execute("1", adminPass);	
		} else {
			onOffUserList.execute("0", adminPass);
		}
		
		while (!onOffUserList.loadCompleted()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setFirmwareUpdate_Command(Receiver receiver, String command){
		if (firmwareUpdateTask != null) {
			firmwareUpdateTask.cancel(true);
			firmwareUpdateTask = null;
		}
		
		firmwareUpdateTask = new AsynTaskFirmwareUpdate(receiver);
		firmwareUpdateTask.execute(command);
		
		while (!firmwareUpdateTask.loadCompleted()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setOnOffControlFull(Receiver receiver, String sendPass, String controlData) {
		if (onOffControlFullTask != null) {
			onOffControlFullTask.cancel(true);
			onOffControlFullTask = null;
		}
		onOffControlFullTask = new AsynTaskOnOffControlFull(receiver);
		onOffControlFullTask.execute(sendPass, controlData);
				
		while (!onOffControlFullTask.loadCompleted()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setOnOffAdminControl(Receiver receiver, String sendPass, String controlData) {
		if (adminControlTask != null) {
			adminControlTask.cancel(true);
			adminControlTask = null;
		}
		adminControlTask = new AsynTaskAdminControl(receiver);
		adminControlTask.execute(sendPass, controlData);
				
		while (!adminControlTask.loadCompleted()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void disconnectFromRemoteHost(Receiver receiver) {
		if(soundControlTask != null) {
			soundControlTask.cancel(true);
			soundControlTask = null; 
		}
		if(hidden90xxTask != null) {
			hidden90xxTask.cancel(true);
			hidden90xxTask = null; 
		}
		if(amplyModiyTask != null) {
			amplyModiyTask.cancel(true);
			amplyModiyTask = null; 
		}
		if(xucXacTask != null) {
			xucXacTask.cancel(true);
			xucXacTask = null; 
		}
		if(modifySongListTask != null) {
			modifySongListTask.cancel(true);
			modifySongListTask = null; 
		}
		if(songFromUSBTask != null) {
			songFromUSBTask.cancel(true);
			songFromUSBTask = null; 
		}
		if(usbStorageListTask != null) {
			usbStorageListTask.cancel(true);
			usbStorageListTask = null; 
		}
		if(lyricVidLinkTask != null) {
			lyricVidLinkTask.cancel(true);
			lyricVidLinkTask = null; 
		}
		if(scoreInfoTask != null) {
			scoreInfoTask.cancel(true);
			scoreInfoTask = null; 
		}
		if(lyricInfoTask != null) {
			lyricInfoTask.cancel(true);
			lyricInfoTask = null; 
		}
		if(hiw_firmwareConfigTask != null) {
			hiw_firmwareConfigTask.cancel(true);
			hiw_firmwareConfigTask = null; 
		}
		if(hiw_firmwareInfoTask != null) {
			hiw_firmwareInfoTask.cancel(true);
			hiw_firmwareInfoTask = null; 
		}
		if(luckyRollTask != null) {
			luckyRollTask.cancel(true);
			luckyRollTask = null; 
		}
		if(tangHoaTask != null) {
			tangHoaTask.cancel(true);
			tangHoaTask = null; 
		}
		if(downloadYouTubeTask != null) {
			downloadYouTubeTask.cancel(true);
			downloadYouTubeTask = null; 
		}
		if(wifiConfigTask != null) {
			wifiConfigTask.cancel(true);
			wifiConfigTask = null; 
		}
		if(firmwareUpdateTask != null) {
			firmwareUpdateTask.cancel(true);
			firmwareUpdateTask = null; 
		}
		if(onOffControlFullTask != null) {
			onOffControlFullTask.cancel(true);
			onOffControlFullTask = null; 
		}
		if(adminControlTask != null) {
			adminControlTask.cancel(true);
			adminControlTask = null; 
		}
		if(deviceIsUserTask != null) {
			deviceIsUserTask.cancel(true);
			deviceIsUserTask = null; 
		}
		if(adminPassTask != null) {
			adminPassTask.cancel(true);
			adminPassTask = null; 
		}	
		if(onOffUserList != null) {
			onOffUserList.cancel(true);
			onOffUserList = null; 
		}		
		if(userCaption != null) {
			userCaption.cancel(true);
			userCaption = null; 
		}
		if (connectTask != null) {
			connectTask.cancel(true);
			connectTask = null;
		}
		if (authenTask != null) {
			authenTask.cancel(true);
			authenTask = null;
		}
		if (downloadTask != null) {
			downloadTask.cancel(true);
			downloadTask = null;
		}
		if (downloadTask != null) {
			downloadTask.cancel(true);
			downloadTask = null;
		}
		if(playlistTask != null)
		{
			playlistTask.cancel(true); 
			playlistTask = null; 
		}
		
		if(playlistKartrolTask != null) {
			playlistKartrolTask.cancel(true);
			playlistKartrolTask = null; 
		}
		
		cancelSyncServerStatus(); 
		
		if(commandTask != null){
			commandTask.cancel(true);
			commandTask = null;
		}
		
		if(commandKartrolTask != null) {
			commandKartrolTask.cancel(true); 
			commandKartrolTask = null; 
		}
		
		if(EspRequestConfigTask != null){
			EspRequestConfigTask.cancel(true);
			EspRequestConfigTask = null;
		}
		
		if(NRPNCheckCRCTask != null) {
			NRPNCheckCRCTask.cancel(true); 
			NRPNCheckCRCTask = null; 
		}
		
		if(espSendNRPNTask != null) {
			espSendNRPNTask.cancel(true); 
			espSendNRPNTask = null; 
		}
		
		if (clientSocket != null) {
			try {
				isProcessingData = false;
				clientSocket.close();
				clientSocket = null; 
//				Log.e(TAB, "disconnectFromRemoteHost()");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public void connectToRemoteHost(Receiver receiver, String host) {
		if (connectTask != null) {
			connectTask.cancel(true);
			connectTask = null;
		}
		connectTask = new AsynTaskConnectHost(receiver);
		connectTask.execute(host);
	}

	public void authenToRemoteHost(Receiver receiver, String password) {
		if (authenTask != null) {
			authenTask.cancel(true);
			authenTask = null;
		}
		authenTask = new AsynTaskAuthenHost(receiver);
		authenTask.execute(password);
	}

	public void connectToRemoteHost(Receiver receiver, String host, String password) {
		if (connectTask != null) {
			connectTask.cancel(true);
			connectTask = null;
		}
		connectTask = new AsynTaskConnectHost(receiver);
		connectTask.execute(host, password);
	}
	
	public void downloadArtistFileInfo(Receiver receiver, String savePath, Boolean sync)
	{
		if (downloadTask != null) {
			downloadTask.cancel(true);
			downloadTask = null;
		}
		downloadTask = new AsynTaskDownloadFileUpdate(receiver);
		downloadTask.execute(savePath, String.valueOf(CMD_REQ_LOADARTIST_INFO));
		if(sync) {
			while (!downloadTask.loadCompleted()) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void downloadArtistFile(Receiver receiver, String savePath, String link)
	{
		if (downloadTask != null) {
			downloadTask.cancel(true);
			downloadTask = null;
		}
		downloadTask = new AsynTaskDownloadFileUpdate(receiver);
		downloadTask.execute(savePath, String.valueOf(CMD_REQ_LOADARTIST_FILE), link);
	}
	
	public void downloadUpdateFile(Receiver receiver, String savePath) {
		if (downloadTask != null) {
			downloadTask.cancel(true);
			downloadTask = null;
		}
		downloadTask = new AsynTaskDownloadFileUpdate(receiver);
		downloadTask.execute(savePath, String.valueOf(CMD_REQ_LOADUPDATE));
	}
	
	public void downloadAddSongFile(Receiver receiver, String savePath) {
		if (downloadTask != null) {
			downloadTask.cancel(true);
			downloadTask = null;
		}
		downloadTask = new AsynTaskDownloadFileUpdate(receiver);
		downloadTask.execute(savePath, String.valueOf(CMD_REQ_LOADADDFILE));
	}
	
	public void downloadLuckyData(Receiver receiver, String savePath) {
		if (downloadTask != null) {
			downloadTask.cancel(true);
			downloadTask = null;
		}
		downloadTask = new AsynTaskDownloadFileUpdate(receiver);
		downloadTask.execute(savePath, String.valueOf(CMD_REQ_LOADLUCKY));
	}
	
	public void downloadLuckyImage(Receiver receiver, String savePath) {
		if (downloadTask != null) {
			downloadTask.cancel(true);
			downloadTask = null;
		}
		downloadTask = new AsynTaskDownloadFileUpdate(receiver);
		downloadTask.execute(savePath, String.valueOf(CMD_REQ_LOADLUCKY_IMG));
	}
	
	public void downloadLyricMidiFile(Receiver receiver, String savePath) {
		if (downloadTask != null) {
			downloadTask.cancel(true);
			downloadTask = null;
		}
		downloadTask = new AsynTaskDownloadFileUpdate(receiver);
		downloadTask.execute(savePath, String.valueOf(CMD_REQ_LOADLYRIC_MIDI));
	}
	
	public void downloadSambaData(Receiver receiver, String savePath) {
		if (downloadTask != null) {
			downloadTask.cancel(true);
			downloadTask = null;
		}
		downloadTask = new AsynTaskDownloadFileUpdate(receiver);
		downloadTask.execute(savePath, String.valueOf(CMD_REQ_SAMBA));
	}
	
	public void downloadYouTubeFile(Receiver receiver, String savePath) {
		if (downloadTask != null) {
			downloadTask.cancel(true);
			downloadTask = null;
		}
		downloadTask = new AsynTaskDownloadFileUpdate(receiver);
		downloadTask.execute(savePath, String.valueOf(CMD_REQ_LOADYOUTUBE));
	}
		
	public void downloadSK90xxFile(Receiver receiver, String savePath, String requestName) {
		if (downloadTask != null) {
			downloadTask.cancel(true);
			downloadTask = null;
		}
		downloadTask = new AsynTaskDownloadFileUpdate(receiver);
		downloadTask.execute(savePath, String.valueOf(CMD_REQ_LOADYOUTUBE), "0", requestName);
	}
	
	public void downloadOfflineFile(Receiver receiver, String savePath) {
		if (downloadTask != null) {
			downloadTask.cancel(true);
			downloadTask = null;
		}
		downloadTask = new AsynTaskDownloadFileUpdate(receiver);
		downloadTask.execute(savePath, String.valueOf(CMD_REQ_LOADOFFLINE));
	}
	
	public void downloadHiddenFile(Receiver receiver, String savePath) {
		if (downloadTask != null) {
			downloadTask.cancel(true);
			downloadTask = null;
		}
		downloadTask = new AsynTaskDownloadFileUpdate(receiver);
		downloadTask.execute(savePath, String.valueOf(CMD_REQ_LOADHIDDEN));
	}

	public void downloadSingerUpdateFile(Receiver receiver, String savePath) {
		if (downloadTask != null) {
			downloadTask.cancel(true);
			downloadTask = null;
		}
		downloadTask = new AsynTaskDownloadFileUpdate(receiver);
		downloadTask.execute(savePath, String.valueOf(CMD_REQ_LOADSINGERUPDATE));
	}
	
	public void downloadLyricFile(Receiver receiver, String savePath) {
		if (downloadTask != null) {
			downloadTask.cancel(true);
			downloadTask = null;
		}
		downloadTask = new AsynTaskDownloadFileUpdate(receiver);
		downloadTask.execute(savePath, String.valueOf(CMD_REQ_LOADLYRIC));
	}
	
	public void downloadColorLyricFile(Receiver receiver, String savePath) {
		if (downloadTask != null) {
			downloadTask.cancel(true);
			downloadTask = null;
		}
		downloadTask = new AsynTaskDownloadFileUpdate(receiver);
		downloadTask.execute(savePath, String.valueOf(CMD_REQ_COLORLYRIC_FILE));
	}
	
	public void downloadHiddenListFile(Receiver receiver, String savePath) {
		if (downloadTask != null) {
			downloadTask.cancel(true); 
			downloadTask = null; 
		}
		
		downloadTask = new AsynTaskDownloadFileUpdate(receiver); 
		downloadTask.execute(savePath, String.valueOf(CMD_REQ_LOADHIDDENLIST)); 
	}
	
	public void playSongImediately(Receiver receiver, int songID, int typeABC, int index)
	{
		if(playlistTask != null)
		{
			playlistTask.cancel(true); 
			playlistTask = null; 
		}
		
		playlistTask = new AsynTaskPlaylistHandle(receiver); 
		playlistTask.execute(CMD_PLAYLIST_PLAY_SONG, typeABC, songID, index);
	}
	
	public void firstReservedSong(Receiver receiver, int songID, int typeABC, int index)
	{
		if(playlistTask != null)
		{
			playlistTask.cancel(true); 
			playlistTask = null; 
		}
		
		playlistTask = new AsynTaskPlaylistHandle(receiver); 
		playlistTask.execute(CMD_PLAYLIST_FIRST_SONG, typeABC, songID, index);
	}
	
	public void firstReservedSongYouTube(Receiver receiver, int songID, int typeABC, int index)
	{
		if(playlistTask != null)
		{
			playlistTask.cancel(true); 
			playlistTask = null; 
		}
		
		playlistTask = new AsynTaskPlaylistHandle(receiver); 
		playlistTask.execute(CMD_PLAYLIST_FIRST_SONG_YT, typeABC, songID, index);
	}
	
	public void firstReservedSongKartrol(Receiver receiver, int songID, int typeABC, int index, int model)
	{
		if(playlistKartrolTask != null)
		{
			playlistKartrolTask.cancel(true); 
			playlistKartrolTask = null; 
		}
		
		playlistKartrolTask = new AsynTaskPlaylistKartrolHandle(receiver, flagEncrypt); 
		playlistKartrolTask.execute(CMD_PLAYLIST_FIRST_SONG, typeABC, songID, index, model);		
	}
	
	
	public void sortSongPlaylist(Receiver receiver, ArrayList<SongID> idList)
	{
		AsynTaskPlaylistSortHandle sortHandle = new AsynTaskPlaylistSortHandle(receiver, CMD_PLAYLIST_SORT_SONG, idList); 
		sortHandle.execute(); 
	}
	
	public void addSongToPlaylist(Receiver receiver, int songID, int typeABC, int index)
	{
		if (playlistTask == null) {
			playlistTask = new AsynTaskPlaylistHandle(receiver);
			playlistTask.execute(CMD_PLAYLIST_ADD_SONG, typeABC, songID, index);
		}else{
			if(playlistTask.getStatus() == AsyncTask.Status.FINISHED){
				playlistTask = new AsynTaskPlaylistHandle(receiver); 
				playlistTask.execute(CMD_PLAYLIST_ADD_SONG, typeABC, songID, index);
			}
		}
	}
	
	public void addSongToPlaylistYouTube(Receiver receiver, int songID, int typeABC, int index)
	{
		if(playlistTask != null)
		{
			playlistTask.cancel(true); 
			playlistTask = null; 
		}
		
		playlistTask = new AsynTaskPlaylistHandle(receiver); 
		playlistTask.execute(CMD_PLAYLIST_ADD_SONG_YT, typeABC, songID, index); 
	}
	
	public void addSongToPlaylistKartrol(Receiver receiver, int songID, int typeABC, int index, int model)
	{
		if(playlistKartrolTask != null)
		{
			playlistKartrolTask.cancel(true); 
			playlistKartrolTask = null; 
		}
		
		playlistKartrolTask = new AsynTaskPlaylistKartrolHandle(receiver, flagEncrypt); 
		playlistKartrolTask.execute(CMD_PLAYLIST_ADD_SONG, typeABC, songID, index, model); 
	}
	
	public void removeSongfromPlaylist(Receiver receiver, int songID, int typeABC, int index)
	{
		if(playlistTask != null)
		{
			playlistTask.cancel(true); 
			playlistTask = null; 
		}
		
		playlistTask = new AsynTaskPlaylistHandle(receiver); 
		playlistTask.execute(CMD_PLAYLIST_REMOVE_SONG, typeABC, songID, index); 
	}
	
	public void addSongToPlaylistRealYouTube(Receiver receiver, String vidID, String name)
	{
		if(playlistTask != null)
		{
			playlistTask.cancel(true); 
			playlistTask = null; 
		}
		
		playlistTask = new AsynTaskPlaylistHandle(receiver, vidID, name, 0); 
		playlistTask.execute(CMD_PLAYLIST_SONG_YT_REAL, 0); 
	}
	
	public void firstSongToPlaylistRealYouTube(Receiver receiver, String vidID, String name, int position)
	{
		if(playlistTask != null)
		{
			playlistTask.cancel(true); 
			playlistTask = null; 
		}
		
		playlistTask = new AsynTaskPlaylistHandle(receiver, vidID, name, position); 
		playlistTask.execute(CMD_PLAYLIST_SONG_YT_REAL, 1); 
	}
	
	public void startSyncServerStatusThread(final Receiver receiver)
	{
		if(timerSyncStatus != null)
		{
//			Log.e(TAB, "Sync Timer already start"); 
			return; 
		}
		
		int syncTime = 2000;		
		if(svrModel == SONCA_SK9xxx || svrModel == SONCA_SMARTK || svrModel == SONCA_SMARTK_9108_SYSTEM || svrModel == SONCA_SMARTK_KM4_SYSTEM
				|| svrModel == SONCA_SMARTK_801 || svrModel == SONCA_SMARTK_CB || svrModel == SONCA_SMARTK_KM4){
			syncTime = 1000;
		}
		
		timerSyncStatus = new Timer(); 
		timerSyncStatus.schedule(new TimerTask() {
			
			@Override
			public void run() {
				if(syncTask == null){
					syncTask = new AsynTaskSyncServerStatus(receiver);
					syncTask.execute();
				}else{
					if(syncTask.getStatus() == AsyncTask.Status.FINISHED){
						syncTask = null;
						syncTask = new AsynTaskSyncServerStatus(receiver);
						syncTask.execute();
					}
				}
			}
		}, 0, syncTime);
	}
	
	public void cancelSyncServerStatus() {
		if (timerSyncStatus != null) {
			timerSyncStatus.cancel();
			timerSyncStatus = null; 
		}
		
		if(syncTask != null)
		{
			syncTask.cancel(true); 
			syncTask = null; 
		}
	}
	
	public void sendCommand(Receiver receiver , byte CMD , int value){
		if(commandTask != null){
			commandTask.cancel(true);
			commandTask = null;
		}
		
//		Log.e("", "Remote cmd: " + CMD + " value: " + value); 
		
		if (CMD == REMOTE_CMD_TEMPO) {
			value += 4; 
		}else if(CMD == REMOTE_CMD_KEY) {
			value += 6; 
		} else if(CMD == REMOTE_CMD_VOL_OFFSETMIDI){
			value += 16; 
		} else if(CMD == REMOTE_CMD_VOL_OFFSETKTV){
			value += 16; 
		} else if(CMD == REMOTE_CMD_VOL_OFFSETKEY){
			value += 12; 
		} 

//		Log.e("", "Remote cmd sent: " + CMD + " value: " + value); 
		commandTask = new AsynTaskCommand(receiver, CMD , value);
		commandTask.execute();
	}
	
	public void sendCommandKartrol(Receiver receiver, byte CMD , int value, int model){
		if(commandKartrolTask != null){
			commandKartrolTask.cancel(true);
			commandKartrolTask = null;
		}
		
		// Log.e("", "Remote cmd: " + CMD + " value: " + value); 
		commandKartrolTask = new AsynTaskCommandKartrol(receiver, flagEncrypt, CMD , value, model);
		commandKartrolTask.execute();
	}
	
////////////////////////////////// - AsynTask - /////////////////////////////////////////////

	private class AsynTaskHiddenList extends AsyncTask<Integer, Integer, Integer> {
		private Receiver mReceiver = null;
		private ServerPackage response = null; 
//		private String errMsg = "";
		private ArrayList<SongID> hiddenList = new ArrayList<SongID>(); 
		private int adminPass = 0; 
		private boolean loading = false; 
		
		public void setAdminPass(int pass) {
			adminPass = pass; 
		}
		
		public boolean loadCompleted()
		{
			return loading == false; 
		}
		
		
		public ArrayList<SongID> getResultList() 
		{
			return hiddenList; 
		}
		
		public void setHiddenList(ArrayList<SongID> list) {
			hiddenList = list; 
		}
		
		public AsynTaskHiddenList(Receiver receiver) {
			mReceiver = receiver;
		}
		
		private int validateAdminPassword()
		{
			byte[] headerPackage = new byte[16]; 
			int pos = 0; 
			
			ByteUtils.intToBytes(headerPackage, pos, authenID);
			pos += 4; 
			
			ByteUtils.intToBytes(headerPackage, pos, adminPassword); 
			pos += 4; 

			byte[] sentPackage = prepareSend(CMD_ADMIN_VALIDATE, headerPackage); 
			response = sendData(sentPackage); 
			return response.getStatus(); 
		}
		
		private int updateHiddenList()
		{
			byte[] headerPackage = new byte[16]; 
			int pos = 0; 
			
			ByteUtils.intToBytes(headerPackage, pos, authenID);
			pos += 4; 
			
			ByteUtils.intToBytes(headerPackage, pos, adminPassword); 
			pos += 4; 
			
			byte needAdmin = 1; 
			headerPackage[pos++] = needAdmin; 
			
			byte listType =  1; 
			headerPackage[pos++] = listType; 
			
			byte[] reserved = new byte[2]; 
			pos += 2; 
			
			int listLen = 4*hiddenList.size(); 
			if(hiddenList.size() == 0) {
				listLen = 4; 
			}
			
			ByteUtils.intToBytes(headerPackage, pos, listLen); 
			pos += 4; 

			byte[] sentPackage = prepareSend(CMD_SET_LIST, headerPackage); 
			response = sendData(sentPackage); 
			if(response.getStatus() != SocketStatus.SUCCESS) {
				return response.getStatus(); 
			}

			// send real data
			byte[] realData = new byte[4 * hiddenList.size()]; 
			pos = 0; 
			for (SongID aSongID : hiddenList) {
				realData[pos++] = (byte)aSongID.typeABC; 
				ByteUtils.int24ToBytes(realData, pos, aSongID.songID);
				pos+=3; 
			}
			
			if(hiddenList.size() == 0) {
				realData = new byte[4]; 
			}

			response = sendData(realData); 
			return response.getStatus(); 
		}
		
		@Override
		protected Integer doInBackground(Integer... params) {
			try {
				loading = true; 
				if (clientSocket == null || !clientSocket.isConnected() || authenID == 0) {
//					Log.e("", "SOCKET IS NULL");
					return SocketStatus.ERROR_DISCONNECTED;
				}
//				Log.e("", "START DOWNLOAD");
				
				int command = params[0]; 
				switch (command) {
				case CMD_ADMIN_VALIDATE:
					validateAdminPassword(); 
					break;

				case CMD_SET_LIST:
					updateHiddenList(); 
					break; 
				default:
					break;
				}
				
				return SocketStatus.SUCCESS; 
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				loading = false; 
			}
			return SocketStatus.ERROR_DISCONNECTED;
		}
		
		private ServerPackage sendData(byte[]sentPackage) {
			byte[] receivePackage = new byte[1500];
			ServerPackage response = new ServerPackage();
			try {
				clientSocket.getOutputStream().write(sentPackage);
				InputStream socketStream = clientSocket.getInputStream();
				int byteRecv = socketStream.read(receivePackage , 0 , receivePackage.length);
				if (byteRecv == -1) {
//					Log.e("", "Read failed: " + byteRecv); 
					response.setStatus(SocketStatus.ERROR_DISCONNECTED); 
					return response; 
				}
				
				int command = receivePackage[0];
				int status = receivePackage[1];
				int dataLen = ByteUtils.byteToInt32(receivePackage, 2);
				response.parseServerPackage(receivePackage);

				if (status != CMD_SUCCESS) {
					byte[] errorStr = new byte[dataLen + 1]; 
					for(int i = 0; i < 5; i++) {
						errorStr[i] = receivePackage[6 + i]; 
					}

					response.setErrorMessage(new String(errorStr)); 
					// Log.e("", "Message: " + response.getErrorMessage()); 

					response.setStatus(SocketStatus.ERROR_FROM_SERVER); 
					return response; 
				} 
			} catch (IOException e) {
				e.printStackTrace();
				response.setStatus(-1); 
				response.setErrorMessage(e.getMessage());
				return response;
			}
			
			response.setStatus(SocketStatus.SUCCESS); 
			return response; 
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			loading = false; 
			if(result == SocketStatus.SUCCESS){
				
			}
			else{
				sendEvent(mReceiver, result, response.getErrorMessage());
			}
		}
	}
	
	private class AsynTaskSongListModify extends AsyncTask<String, Integer, Integer> {
		private Receiver mReceiver = null;
		private String errMsg = "";
		private String resultString = ""; 
		private boolean loading = false; 
		private ServerPackage response = null;
		
		private int modType;
		private int[] ids;
		
		public boolean loadCompleted()
		{
			return loading == false; 
		}
		
		public String getResultString()
		{
			return resultString; 
		}
		
		private boolean flagNew = false;
		public void setNewWay(boolean flagNew){
			this.flagNew = flagNew;
		}
		
		public AsynTaskSongListModify(Receiver receiver, int modType, int[] ids) {
			response = new ServerPackage();
			mReceiver = receiver;
			loading = true; 
			
			this.modType = modType;
			this.ids = ids;
		}

		@Override
		protected Integer doInBackground(String... params) {
			try {
				Log.e("", "START AsynTaskSongListModify");
				if (clientSocket == null || !clientSocket.isConnected() || authenID == 0) {
					 //Log.e("", "SOCKET IS NULL");
					return SocketStatus.ERROR_DISCONNECTED;
				}
				
				int valueCommand = CMD_TOC_MODIFY;
				String strCommand = "CMD_TOC_MODIFY";
				
				resultString = null;
				
				byte[] sentData = null;
				
				if(flagNew){
					valueCommand = CMD_TOC_MODIFY_NEW;
					strCommand = "CMD_TOC_MODIFY_NEW";
					
					byte[] authenArr = new byte[4]; 
					ByteUtils.intToBytes(authenArr, 0, authenID);		
					
					sentData = new byte[authenArr.length + 1 + 3 + ids.length * 3];
										
					System.arraycopy(authenArr, 0, sentData, 0, authenArr.length);
					sentData[4] = (byte)(modType & 0xff);
					ByteUtils.int24ToBytes(sentData, 5, ids.length);
					
					for (int i = 0; i < ids.length; i++) {
						ByteUtils.int24ToBytes(sentData, 8 + i * 3, ids[i]);
					}				
				} else {
					byte[] authenArr = new byte[4]; 
					ByteUtils.intToBytes(authenArr, 0, authenID);		
					
					sentData = new byte[authenArr.length + 1 + 1 + ids.length * 3];
										
					System.arraycopy(authenArr, 0, sentData, 0, authenArr.length);
					sentData[4] = (byte)(modType & 0xff);
					sentData[5] = (byte)(ids.length & 0xff);
					
					for (int i = 0; i < ids.length; i++) {
						ByteUtils.int24ToBytes(sentData, 6 + i * 3, ids[i]);
					}
				}
				
				byte[] sentPackage = prepareSendFull(valueCommand, sentData);
				
				byte[] receivePackage = new byte[1500];
				InputStream socketStream = null;
				
				int resultProcess = processSocket_SendReceive(mReceiver,
						strCommand, clientSocket, 
						sentPackage, receivePackage, socketStream,
						SocketStatus.ERROR_FROM_SERVER, SocketStatus.ERROR_DISCONNECTED);
				if (resultProcess != 999) {
					return resultProcess;
				}					
				
				receivePackage = processResponseData(receivePackage);
				response.parseServerPackage(receivePackage);
				
				if (response.getStatus() != CMD_SUCCESS) {
					if(response.getStatus() == 2){ 
						errMsg = "toc_modi_noUsb";
					} else if(response.getStatus() == 3){ 
						if(modType == 0){
							errMsg = "toc_modi_notInsideUsb";	
						} else {
							errMsg = "toc_modi_notInsideTOC";
						}
						
					} else if(response.getStatus() == 5){ 
						errMsg = "toc_modi_full";
					} else if(response.getStatus() == 4){ 
						errMsg = "toc_modi_busy";
					} else if(response.getStatus() == 6){ 
						errMsg = "toc_modi_limit";
					}
					
					return SocketStatus.ERROR_INVALID_RESPONSE;
				} 				
											
				return SocketStatus.SUCCESS;
				
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				loading = false; 
			}
			return SocketStatus.ERROR_DISCONNECTED;
		}

		@Override
		protected void onPostExecute(Integer result) {
			loading = false; 
			if(result == SocketStatus.SUCCESS){
				if(mReceiver != null){
					mReceiver.deviceSendModifyTOCResult("OK");
				}
			}else{
				if(mReceiver != null){
					mReceiver.deviceSendModifyTOCResult("NOT OK");
				}
				sendEvent(mReceiver, result, errMsg);
			}
		}
	}
	
	private class AsynTaskAmplyModify extends AsyncTask<String, Integer, Integer> {
		private Receiver mReceiver = null;
		private String errMsg = "";
		private String resultString = ""; 
		private boolean loading = false; 
		private ServerPackage response = null;
		
		private int modType;
		
		public ArrayList<Integer> listValue = new ArrayList<Integer>();
		
		public boolean loadCompleted()
		{
			return loading == false; 
		}
		
		public String getResultString()
		{
			return resultString; 
		}
		
		public AsynTaskAmplyModify(Receiver receiver, int modType, ArrayList<Integer> listValue) {
			response = new ServerPackage();
			mReceiver = receiver;
			loading = true; 
			
			this.modType = modType;
			this.listValue = listValue;
		}

		@Override
		protected Integer doInBackground(String... params) {
			try {
				Log.e("", "START AsynTaskAmplyModify");
				if (clientSocket == null || !clientSocket.isConnected() || authenID == 0) {
					 //Log.e("", "SOCKET IS NULL");
					return SocketStatus.ERROR_DISCONNECTED;
				}
				
				resultString = null;
								
				byte[] authenArr = new byte[4]; 
				ByteUtils.intToBytes(authenArr, 0, authenID);
				
				if(modType == 1){ // set value
					byte[] sentData = new byte[12];
					
//					System.arraycopy(authenArr, 0, sentData, 0, authenArr.length);
					
					byte[] byteAmplyData = new byte[12];
					for (int i = 0; i < byteAmplyData.length; i++) {
						byteAmplyData[i] = (byte)((int)listValue.get(i));					
					}
					System.arraycopy(byteAmplyData, 0, sentData, 0, byteAmplyData.length);
					Log.e("AsynTaskAmplyModify", bytesToHex2(sentData));
					
					byte[] sentPackage = prepareSendFull(IS_SET_DATA1, sentData);
					
					byte[] receivePackage = new byte[1500];
					InputStream socketStream = null;
					
					int resultProcess = processSocket_SendReceive(mReceiver,
							"IS_SET_DATA1", clientSocket, 
							sentPackage, receivePackage, socketStream,
							SocketStatus.ERROR_FROM_SERVER, SocketStatus.ERROR_DISCONNECTED);
					if (resultProcess != 999) {
						return resultProcess;
					}					
					
					receivePackage = processResponseData(receivePackage);
					response.parseServerPackage(receivePackage);
					
					if (response.getStatus() != CMD_SUCCESS) {
						return SocketStatus.ERROR_INVALID_RESPONSE;
					}	
					
					byte[] responseData = response.getData();
				}	
				
				return SocketStatus.SUCCESS;
				
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				loading = false; 
			}
			return SocketStatus.ERROR_DISCONNECTED;
		}

		@Override
		protected void onPostExecute(Integer result) {
			loading = false; 
			if(result == SocketStatus.SUCCESS){
				
			}else{
				sendEvent(mReceiver, result, errMsg);
			}
		}
	}
	
	private class AsynTaskTangHoa extends AsyncTask<String, Integer, Integer> {
		private Receiver mReceiver = null;
		private String errMsg = "";
		private String resultString = ""; 
		private boolean loading = false; 
		private ServerPackage response = null;
		
		private String nameSender;
		private int avatarIdx;
		private int numFlower;
		
		public boolean loadCompleted()
		{
			return loading == false; 
		}
		
		public String getResultString()
		{
			return resultString; 
		}
		
		public AsynTaskTangHoa(Receiver receiver, String name, int avarter, int numberFlower) {
			response = new ServerPackage();
			mReceiver = receiver;
			loading = true; 
			
			this.nameSender = name;
			this.avatarIdx = avarter;
			this.numFlower = numberFlower;
		}

		@Override
		protected Integer doInBackground(String... params) {
			try {
				Log.e("", "START TANG HOA TASK");
				if (clientSocket == null || !clientSocket.isConnected() || authenID == 0) {
					 //Log.e("", "SOCKET IS NULL");
					return SocketStatus.ERROR_DISCONNECTED;
				}
				
				resultString = null;
				
				String command = params[0];
				
				if(command.equals("0")){ // SET TANG HOA
					byte[] authenArr = new byte[4]; 
					ByteUtils.intToBytes(authenArr, 0, authenID);		
					
					byte[] sentData = new byte[authenArr.length + 1 + 1 + 20];
										
					System.arraycopy(authenArr, 0, sentData, 0, authenArr.length);
					sentData[4] = (byte)(avatarIdx & 0xff);
					sentData[5] = (byte)(numFlower & 0xff);

					byte[] nameArr = nameSender.getBytes();
					System.arraycopy(nameArr, 0, sentData, 6, nameArr.length);					
					
					byte[] sentPackage = prepareSendFull(CMD_SET_FLOWER, sentData);
					
					byte[] receivePackage = new byte[1500];
					InputStream socketStream = null;
					
					int resultProcess = processSocket_SendReceive(mReceiver,
							"CMD_SET_FLOWER", clientSocket, 
							sentPackage, receivePackage, socketStream,
							SocketStatus.ERROR_FROM_SERVER, SocketStatus.ERROR_DISCONNECTED);
					if (resultProcess != 999) {
						return resultProcess;
					}					
					
					receivePackage = processResponseData(receivePackage);
					response.parseServerPackage(receivePackage);
					
					if (response.getStatus() != CMD_SUCCESS) {
						return SocketStatus.ERROR_INVALID_RESPONSE;
					} 				
					
					byte[] responseData = response.getData(); 					
					
					return SocketStatus.SUCCESS;
				} else { 
					
				}				
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				loading = false; 
			}
			return SocketStatus.ERROR_DISCONNECTED;
		}

		@Override
		protected void onPostExecute(Integer result) {
			loading = false; 
			if(result == SocketStatus.SUCCESS){
				
			}else{
				sendEvent(mReceiver, result, errMsg);
			}
		}
	}
	
	private class AsynTaskXucXac extends AsyncTask<String, Integer, Integer> {
		private Receiver mReceiver = null;
		private String errMsg = "";
		private String resultString = ""; 
		private boolean loading = false; 
		private ServerPackage response = null;
				
		public boolean loadCompleted()
		{
			return loading == false; 
		}
		
		public String getResultString()
		{
			return resultString; 
		}
		
		public AsynTaskXucXac(Receiver receiver) {
			response = new ServerPackage();
			mReceiver = receiver;
			loading = true; 
		}

		@Override
		protected Integer doInBackground(String... params) {
			try {
				Log.e("", "START XUC XAC TASK");
				if (clientSocket == null || !clientSocket.isConnected() || authenID == 0) {
					 //Log.e("", "SOCKET IS NULL");
					return SocketStatus.ERROR_DISCONNECTED;
				}
				
				resultString = null;
				
				String command = params[0];
				
				if(command.equals("0")){ // SET TANG HOA
					byte[] authenArr = new byte[4]; 
					ByteUtils.intToBytes(authenArr, 0, authenID);
					
					byte[] sentPackage = prepareSendFull(CMD_CALL_XUCXAC, authenArr);
					
					byte[] receivePackage = new byte[1500];
					InputStream socketStream = null;
					
					int resultProcess = processSocket_SendReceive(mReceiver,
							"CMD_CALL_XUCXAC", clientSocket, 
							sentPackage, receivePackage, socketStream,
							SocketStatus.ERROR_FROM_SERVER, SocketStatus.ERROR_DISCONNECTED);
					if (resultProcess != 999) {
						return resultProcess;
					}					
					
					receivePackage = processResponseData(receivePackage);
					response.parseServerPackage(receivePackage);
					
					if (response.getStatus() != CMD_SUCCESS) {
						return SocketStatus.ERROR_INVALID_RESPONSE;
					} 				
					
					byte[] responseData = response.getData(); 					
					
					return SocketStatus.SUCCESS;
				} else { 
					
				}				
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				loading = false; 
			}
			return SocketStatus.ERROR_DISCONNECTED;
		}

		@Override
		protected void onPostExecute(Integer result) {
			loading = false; 
			if(result == SocketStatus.SUCCESS){
				
			}else{
				sendEvent(mReceiver, result, errMsg);
			}
		}
	}
	
	private class AsynTaskLuckyRoll extends AsyncTask<String, Integer, Integer> {
		private Receiver mReceiver = null;
		private String errMsg = "";
		private String resultString = ""; 
		private boolean loading = false; 
		private ServerPackage response = null;
				
		public boolean loadCompleted()
		{
			return loading == false; 
		}
		
		public String getResultString()
		{
			return resultString; 
		}
		
		public AsynTaskLuckyRoll(Receiver receiver) {
			response = new ServerPackage();
			mReceiver = receiver;
			loading = true; 
		}

		@Override
		protected Integer doInBackground(String... params) {
			try {
				Log.e("", "START LUCKY ROLL TASK");
				if (clientSocket == null || !clientSocket.isConnected() || authenID == 0) {
					 //Log.e("", "SOCKET IS NULL");
					return SocketStatus.ERROR_DISCONNECTED;
				}
				
				resultString = null;
				
				String command = params[0];
				
				if(command.equals("0")){ 
					byte[] authenArr = new byte[4]; 
					ByteUtils.intToBytes(authenArr, 0, authenID);
					
					byte[] sentPackage = prepareSendFull(CMD_CALL_LUCKYROLL, authenArr);
					
					byte[] receivePackage = new byte[1500];
					InputStream socketStream = null;
					
					int resultProcess = processSocket_SendReceive(mReceiver,
							"CMD_CALL_LUCKYROLL", clientSocket, 
							sentPackage, receivePackage, socketStream,
							SocketStatus.ERROR_FROM_SERVER, SocketStatus.ERROR_DISCONNECTED);
					if (resultProcess != 999) {
						return resultProcess;
					}					
					
					receivePackage = processResponseData(receivePackage);
					response.parseServerPackage(receivePackage);
					
					byte[] responseData = response.getData(); 		

					if (response.getStatus() != CMD_SUCCESS) {
						if(response.getStatus() == 1){ // chua co api
							errMsg = "lucky_notsupport";
						} else if(response.getStatus() == 2){ // chua co file data
							errMsg = "lucky_nodata";
						} else if (response.getStatus() == 3){ // file data co loi
							errMsg = "lucky_errordata";
						} else if(response.getStatus() == 4){ 
							errMsg = "toc_modi_busy";
						}
						return SocketStatus.ERROR_INVALID_RESPONSE;
					} 						
					
					return SocketStatus.SUCCESS;
				} else { 
					
				}				
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				loading = false; 
			}
			return SocketStatus.ERROR_DISCONNECTED;
		}

		@Override
		protected void onPostExecute(Integer result) {
			loading = false; 
			if(result == SocketStatus.SUCCESS){
				
			}else{
				sendEvent(mReceiver, result, errMsg);
			}
		}
	}
	
	private class AsynTaskDownloadYouTube extends AsyncTask<String, Integer, Integer> {
		private Receiver mReceiver = null;
		private String errMsg = "";
		private String resultString = ""; 
		private boolean loading = false; 
		private ServerPackage response = null;
		private ArrayList<String> listDownload;
		
		private int downloadID;
		private int downloadType;
		
		private Download90xxInfo info = null;
		
		public boolean loadCompleted()
		{
			return loading == false; 
		}
		
		public String getResultString()
		{
			return resultString; 
		}
		
		public AsynTaskDownloadYouTube(Receiver receiver, int downloadID, int downloadType) {
			response = new ServerPackage();
			mReceiver = receiver;
			loading = true; 
			
			this.downloadID = downloadID;
			this.downloadType = downloadType;
		}
		
		public AsynTaskDownloadYouTube(Receiver receiver, Download90xxInfo info, int downloadType) {
			response = new ServerPackage();
			mReceiver = receiver;
			loading = true; 
			
			this.info = info;
			this.downloadType = downloadType;
		}
		
		public AsynTaskDownloadYouTube(Receiver receiver, Download90xxInfo info, int downloadType, ArrayList<String> listDownload) {
			response = new ServerPackage();
			mReceiver = receiver;
			loading = true; 
			
			this.info = info;
			this.downloadType = downloadType;
			this.listDownload = listDownload;
		}

		@Override
		protected Integer doInBackground(String... params) {
			try {
				Log.e("", "START COMMAND YOUTUBE");
				if (clientSocket == null || !clientSocket.isConnected() || authenID == 0) {
					 //Log.e("", "SOCKET IS NULL");
					return SocketStatus.ERROR_DISCONNECTED;
				}
				
				resultString = null;
				
				String command = params[0];
				
				if(command.equals("0")){ 
					if(info != null){ // 90xx
						byte[] authenArr = new byte[4]; 
						ByteUtils.intToBytes(authenArr, 0, authenID);

						String urlPath = info.getUrlPath();
						
						Log.e(" ", info.toString());
						Log.d(" ", "downloadType = " + downloadType);
						Log.d(" ", "urlPath = " + urlPath);

						byte[] bURL = urlPath.getBytes();
						
						int offSetURL = 16;
						
						byte[] sentData = new byte[authenArr.length + 12 + bURL.length + 1];
						
						System.arraycopy(authenArr, 0, sentData, 0, authenArr.length);					
						ByteUtils.int24ToBytes(sentData, 4, info.getId());
						
						sentData[7] = (byte)downloadType;
						
						byte[] bDataSendDauMay = new byte[0];
						byte[] fileType = info.getFileType().getBytes();
						if(downloadType == 3 && info.isFlagVocal()){
							
							Log.d(" ", "listDownload = " + listDownload.size());
							
							String strDataSendDauMay = "";
							for (int i = 0; i < listDownload.size(); i++) {
								strDataSendDauMay += listDownload.get(i);
							}
							bDataSendDauMay = strDataSendDauMay.getBytes();
							
							ByteUtils.intToBytes(sentData, 8, bDataSendDauMay.length);
						} else {
							System.arraycopy(fileType, 0, sentData, 8, fileType.length);							
						}
						
						sentData[12] = (byte)(info.isFlagVocal()?1:0);
						sentData[13] = (byte)(info.getFolderType());
						
						ByteUtils.int16ToBytesL(sentData, 14, offSetURL);
						
						System.arraycopy(bURL, 0, sentData, offSetURL, bURL.length);
						
						Log.e("TEST 1", bytesToHex2(sentData));
						
						byte[] sentPackage = prepareSendFull(CMD_DOWNLOAD_YOUTUBE, sentData);
						
						byte[] receivePackage = new byte[1500];
						InputStream socketStream = null;
						
						int resultProcess = 0;
						
						// send data to dau may
						if(downloadType == 3 && info.isFlagVocal() && bDataSendDauMay.length > 1){							
							clientSocket.getOutputStream().write(sentPackage);
							
							for (int i = 0; i < listDownload.size(); i++) {
								byte[] smallSend = listDownload.get(i).getBytes();
								clientSocket.getOutputStream().write(smallSend);
							}
							
							return SocketStatus.SUCCESS;
						} else {							
							resultProcess = processSocket_SendReceive(mReceiver,
									"CMD_SPECIAL_DOWNLOAD_YOUTUBE", clientSocket, 
									sentPackage, receivePackage, socketStream,
									SocketStatus.ERROR_FROM_SERVER, SocketStatus.ERROR_FROM_SERVER);
							if (resultProcess != 999) {
								return resultProcess;
							}	
							
							receivePackage = processResponseData(receivePackage);
							response.parseServerPackage(receivePackage);
							
							Log.e(" ", "response.getStatus = " + response.getStatus());						
							
							if (response.getStatus() != CMD_SUCCESS) {
								errMsg = "yt_90xx_down_status@&@" + response.getStatus();
								return SocketStatus.ERROR_INVALID_RESPONSE;
							} 				
							
							byte[] responseData = response.getData();
							
							return SocketStatus.SUCCESS;
						}					
						
						
					} else {
						byte[] authenArr = new byte[4]; 
						ByteUtils.intToBytes(authenArr, 0, authenID);		
						
						byte[] sentData = new byte[authenArr.length + 3 + 1];
											
						System.arraycopy(authenArr, 0, sentData, 0, authenArr.length);					
						ByteUtils.int24ToBytes(sentData, 4, downloadID);
						
						sentData[7] = (byte)downloadType;	
						
						byte[] sentPackage = prepareSendFull(CMD_DOWNLOAD_YOUTUBE, sentData);
						
						byte[] receivePackage = new byte[1500];
						InputStream socketStream = null;
						
						int resultProcess = processSocket_SendReceive(mReceiver,
								"CMD_DOWNLOAD_YOUTUBE", clientSocket, 
								sentPackage, receivePackage, socketStream,
								SocketStatus.ERROR_FROM_SERVER, SocketStatus.ERROR_DISCONNECTED);
						if (resultProcess != 999) {
							return resultProcess;
						}					
						
						receivePackage = processResponseData(receivePackage);
						response.parseServerPackage(receivePackage);
						
						if (response.getStatus() != CMD_SUCCESS) {						
							if(downloadType == 0){ // ADD YOUTUBE
								if(response.getStatus() == 2){ // o cung full
									errMsg = "yt_down_full";
								} else if (response.getStatus() == 3){ // vuot qua limit download
									errMsg = "yt_down_limit";
								} else if (response.getStatus() == 4){ // download roi
									errMsg = "yt_down_exist";
								}
							}
							
							return SocketStatus.ERROR_INVALID_RESPONSE;
						} 				
						
						byte[] responseData = response.getData(); 					
						
						return SocketStatus.SUCCESS;
					}
					
				} else { 
					
				}				
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				loading = false; 
			}
			return SocketStatus.ERROR_DISCONNECTED;
		}

		@Override
		protected void onPostExecute(Integer result) {
			Log.e("", "END COMMAND YOUTUBE");
			loading = false; 
			if(result == SocketStatus.SUCCESS){
				
			}else{
				sendEvent(mReceiver, result, errMsg);
			}
		}
	}
	
	private class AsynTaskConfigWifi extends AsyncTask<String, Integer, Integer> {
		private Receiver mReceiver = null;
		private String errMsg = "";
		private String resultString = ""; 
		private boolean loading = false; 
		private ServerPackage response = null;
		
		public boolean loadCompleted()
		{
			return loading == false; 
		}
		
		public String getResultString()
		{
			return resultString; 
		}
		
		public AsynTaskConfigWifi(Receiver receiver) {
			response = new ServerPackage();
			mReceiver = receiver;
			loading = true; 
		}

		@Override
		protected Integer doInBackground(String... params) {
			try {
				//Log.e("", "START CONFIG WIFI");
				if (clientSocket == null || !clientSocket.isConnected() || authenID == 0) {
					 //Log.e("", "SOCKET IS NULL");
					return SocketStatus.ERROR_DISCONNECTED;
				}
				
				resultString = null;
				
				String command = params[0];
				
				if(command.equals("0")){ // GET CONFIG WIFI
					byte[] authenArr = new byte[4]; 
					ByteUtils.intToBytes(authenArr, 0, authenID);					
					
					byte[] sentPackage = prepareSendFull(CMD_GET_CONFIGWIFI, authenArr);
					
					//Log.e("sentPackage", bytesToHex2(sentPackage));
					
					byte[] receivePackage = new byte[1500];
					InputStream socketStream = null;
					
					int resultProcess = processSocket_SendReceive(mReceiver,
							"CMD_GET_CONFIGWIFI", clientSocket, 
							sentPackage, receivePackage, socketStream,
							SocketStatus.ERROR_FROM_SERVER, SocketStatus.ERROR_DISCONNECTED);
					if (resultProcess != 999) {
						return resultProcess;
					}					
					
					receivePackage = processResponseData(receivePackage);
					response.parseServerPackage(receivePackage);			
				
					
					if (response.getStatus() != CMD_SUCCESS) {
						Log.e("response data", "response.getStatus() != CMD_SUCCESS");	
						return SocketStatus.ERROR_INVALID_RESPONSE;
					} 				
					
					byte[] responseData = response.getData(); 
					Log.e("response data", bytesToHex2(responseData));	
					
					
					return SocketStatus.SUCCESS;
				} else { // SET CONFIG WIFI
					
				}				
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				loading = false; 
			}
			return SocketStatus.ERROR_DISCONNECTED;
		}

		@Override
		protected void onPostExecute(Integer result) {
			loading = false; 
			if(result == SocketStatus.SUCCESS){
				if(mReceiver != null){
					mReceiver.deviceSendConfigWifi(getResultString());
				}
			}else{
				sendEvent(mReceiver, result, errMsg);
			}
		}
	}
	
	private class AsynTaskHidden90xx extends AsyncTask<String, Integer, Integer> {
		private Receiver mReceiver = null;
		private String errMsg = "";
		private String resultString = ""; 
		private boolean loading = false; 
		private ServerPackage response = null;
		private ArrayList<Integer> songlist;
				
		public void setSongList(ArrayList<Integer> songlist)
		{
			this.songlist = songlist;
		}		
		
		public AsynTaskHidden90xx(Receiver receiver) {
			response = new ServerPackage();
			mReceiver = receiver;
			loading = true; 
		}

		@Override
		protected Integer doInBackground(String... params) {
			try {
				Log.e("", "START AsynTaskHidden90xx");
				if (clientSocket == null || !clientSocket.isConnected() || authenID == 0) {
					 //Log.e("", "SOCKET IS NULL");
					return SocketStatus.ERROR_DISCONNECTED;
				}
				
				if(songlist == null || songlist.size() == 0){
					return SocketStatus.SUCCESS;
				}
				
				resultString = null;
				
				String command = params[0];
				
				if(command.equals("0")){ // SET HIDDEN LIST
					byte[] authenArr = new byte[4]; 
					ByteUtils.intToBytes(authenArr, 0, authenID);	
					
					int intExpand = Integer.parseInt(params[1]);
					
					Log.e(" ", "songlist = " + songlist.size());
					Log.d(" ", "expand = " + intExpand);
					
					byte[] sentData = new byte[authenArr.length + 2 + 3 * songlist.size()];
					
					System.arraycopy(authenArr, 0, sentData, 0, authenArr.length);					
										
					sentData[4] = (byte)0;
					sentData[5] = (byte)intExpand;
					
					for (int i = 0; i < songlist.size(); i++) {
						ByteUtils.int24ToBytes(sentData, 6 + i * 3, songlist.get(i));
					}			
					
//					Log.e("sentData", bytesToHex2(sentData));							
					
					byte[] sentPackage = prepareSendFull(CMD_SEND_HIDDEN, sentData);
					
					byte[] receivePackage = new byte[1500];
					InputStream socketStream = null;
					
					int resultProcess = processSocket_SendReceive(mReceiver,
							"CMD_SEND_HIDDEN", clientSocket, 
							sentPackage, receivePackage, socketStream,
							SocketStatus.ERROR_FROM_SERVER, SocketStatus.ERROR_DISCONNECTED);
					if (resultProcess != 999) {
						return resultProcess;
					}					
					
					receivePackage = processResponseData(receivePackage);
//					Log.e("receivePackage", bytesToHex2(receivePackage));
					response.parseServerPackage(receivePackage);		
									
					if (response.getStatus() != CMD_SUCCESS) {
						Log.e("response data", "response.getStatus() != CMD_SUCCESS -- " + response.getStatus());	
						return SocketStatus.ERROR_INVALID_RESPONSE;
					} 				
					
					byte[] responseData = response.getData(); 					
					
					return SocketStatus.SUCCESS;
				} else { // SET CONFIG WIFI
					
				}				
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				loading = false; 
			}
			return SocketStatus.ERROR_DISCONNECTED;
		}

		@Override
		protected void onPostExecute(Integer result) {
			loading = false; 
			if(result == SocketStatus.SUCCESS){
				
			}else{
				sendEvent(mReceiver, result, errMsg);
			}
		}
	}
	
	private class AsynTaskLyricInfo extends AsyncTask<String, Integer, Integer> {
		private Receiver mReceiver = null;
		private String errMsg = "";
		private String resultString = ""; 
		private boolean loading = false; 
		private ServerPackage response = null;
		
		public boolean loadCompleted()
		{
			return loading == false; 
		}
		
		public String getResultString()
		{
			return resultString; 
		}
		
		public AsynTaskLyricInfo(Receiver receiver) {
			response = new ServerPackage();
			mReceiver = receiver;
			loading = true; 
		}

		@Override
		protected Integer doInBackground(String... params) {
			try {
				// Log.e("", "START LYRIC INFO");
				if (clientSocket == null || !clientSocket.isConnected() || authenID == 0) {
					// Log.e("", "SOCKET IS NULL");
					return SocketStatus.ERROR_DISCONNECTED;
				}
				
				resultString = null;
				
				byte[] authenArr = new byte[4]; 
				ByteUtils.intToBytes(authenArr, 0, authenID);					
				
				byte[] sentPackage = prepareSendFull(CMD_REQ_SYNCTIME, authenArr);				
//				Log.e("sentPackage", bytesToHex2(sentPackage));
				
				byte[] receivePackage = new byte[1000];				
				InputStream socketStream = null;
				
				int resultProcess = processSocket_SendReceive(mReceiver,
						"CMD_REQ_SYNCTIME", clientSocket, 
						sentPackage, receivePackage, socketStream,
						SocketStatus.ERROR_FROM_SERVER, SocketStatus.ERROR_SOCKET_READ);
				if (resultProcess != 999) {
					return resultProcess;
				}	
				
				receivePackage = processResponseData(receivePackage);
				response.parseServerPackage(receivePackage);			
								
				if(response.getCommand() != CMD_REQ_SYNCTIME){
					return SocketStatus.ERROR_SOCKET_BUSY;
				}	
				
				if (response.getStatus() != CMD_SUCCESS) {
					return SocketStatus.SUCCESS;
				} 				
				
				byte[] responseData = response.getData(); 
				//Log.e("response data", bytesToHex2(responseData));

				if(svrModel== SONCA_SK9xxx || svrModel == SONCA_SMARTK || svrModel == SONCA_SMARTK_9108_SYSTEM || svrModel == SONCA_SMARTK_KM4_SYSTEM
						|| svrModel == SONCA_SMARTK_801 || svrModel == SONCA_SMARTK_CB || svrModel == SONCA_SMARTK_KM4){
					if(responseData.length > 0){
//				    	int i = 0; 
//				    	int len = 0; 
//				        while (len < responseData.length && responseData[i++] != 0) {
//				            len++;
//				        }
//						
//						resultString = new String(responseData, 0, len); // value/90 --> ms
						
						long value = 0;
						for (int i = 0; i < responseData.length; i++)
						{
						   value += ((long) responseData[i] & 0xffL) << (8 * i);
						}
						
						resultString = value + "";
				    }	
				} else {
					if(responseData.length >= 4){
						int intTemp2 = ByteUtils.byteToInt32L(responseData, 0);
						
						resultString = intTemp2 + ""; // already in ms
					}
				}

				//Log.e("resultString", resultString);	
				
				return SocketStatus.SUCCESS;
				
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				loading = false; 
			}
			return SocketStatus.ERROR_DISCONNECTED;
		}

		@Override
		protected void onPostExecute(Integer result) {
			loading = false; 
			if(result == SocketStatus.SUCCESS){
				if(mReceiver != null){
					mReceiver.deviceSendLyricInfo(getResultString());
				}
			}else{
				sendEvent(mReceiver, result, errMsg);
				if(result == SocketStatus.ERROR_SOCKET_READ){
					if(mReceiver != null){
						mReceiver.deviceSendLyricInfo(getResultString());
					}	
				}				
			}
		}
	}
	
	private class AsynTaskLyricVidLink extends AsyncTask<String, Integer, Integer> {
		private Receiver mReceiver = null;
		private String errMsg = "";
		private String resultString = ""; 
		private boolean loading = false; 
		private ServerPackage response = null;
		
		private String strID = "";
		private String strTypeABC = "0";
		
		public boolean loadCompleted()
		{
			return loading == false; 
		}
		
		public String getResultString()
		{
			return resultString; 
		}
		
		public AsynTaskLyricVidLink(Receiver receiver) {
			response = new ServerPackage();
			mReceiver = receiver;
			loading = true; 
		}
		
		public AsynTaskLyricVidLink(Receiver receiver, String id, String typeABC) {
			response = new ServerPackage();
			mReceiver = receiver;
			loading = true;
			
			strID = id;
			strTypeABC = typeABC;
		}

		@Override
		protected Integer doInBackground(String... params) {
			try {
				Log.e("", "START AsynTaskLyricVidLink");
				if (clientSocket == null || !clientSocket.isConnected() || authenID == 0) {
					// Log.e("", "SOCKET IS NULL");
					return SocketStatus.ERROR_DISCONNECTED;
				}
				
				Log.e(" ", "Request Special strID = " + strID + " -- strTypeABC = " + strTypeABC);
				
				resultString = null;
				
				byte[] authenArr = new byte[4]; 
				ByteUtils.intToBytes(authenArr, 0, authenID);					
				
				byte[] sentData = authenArr;
				
				if(!strID.equals("")){
					sentData = new byte[authenArr.length + 4];
										
					System.arraycopy(authenArr, 0, sentData, 0, authenArr.length);
					ByteUtils.int24ToBytes(sentData, 4, Integer.parseInt(strID));	
					
					sentData[7] = (byte)Integer.parseInt(strTypeABC);
				}
				
				byte[] sentPackage = prepareSendFull(CMD_REQ_VIDLINK, sentData);				
				// Log.e("sentPackage", bytesToHex2(sentPackage));
				
				byte[] receivePackage = new byte[1000];				
				InputStream socketStream = null;
				
				int resultProcess = processSocket_SendReceive(mReceiver,
						"CMD_REQ_VIDLINK", clientSocket, 
						sentPackage, receivePackage, socketStream,
						SocketStatus.ERROR_FROM_SERVER, SocketStatus.ERROR_SOCKET_READ);
				if (resultProcess != 999) {
					return resultProcess;
				}	
				
				receivePackage = processResponseData(receivePackage);
				response.parseServerPackage(receivePackage);			
								
				if(response.getCommand() != CMD_REQ_VIDLINK){
					return SocketStatus.ERROR_SOCKET_BUSY;
				}	
				
				if (response.getStatus() != CMD_SUCCESS) {
					return SocketStatus.SUCCESS;
				} 				
				
				byte[] responseData = response.getData(); 
				// Log.e("response data", bytesToHex2(responseData));

		    	int i = 0; 
		    	int len = 0; 
		        while (len < responseData.length && responseData[i++] != 0) {
		            len++;
		        }
				
				resultString = new String(responseData, 0, len);

				// Log.e("resultString", resultString);	
				
				return SocketStatus.SUCCESS;
				
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				loading = false; 
			}
			return SocketStatus.ERROR_DISCONNECTED;
		}

		@Override
		protected void onPostExecute(Integer result) {
			loading = false; 
			if(result == SocketStatus.SUCCESS){
				if(mReceiver != null){
					mReceiver.deviceSendLyricVidLink(getResultString());
				}
			}else{
				sendEvent(mReceiver, result, errMsg);
				if(result == SocketStatus.ERROR_SOCKET_READ){
					if(mReceiver != null){
						mReceiver.deviceSendLyricVidLink(getResultString());
					}	
				}				
			}
		}
	}
	
	private class AsynTaskSoundControl extends AsyncTask<String, Integer, Integer> {
		private Receiver mReceiver = null;
		private String errMsg = "";
		private String resultString = ""; 
		private boolean loading = false; 
		private ServerPackage response = null;
				
		private String cmdType = "0";
		
		public boolean loadCompleted()
		{
			return loading == false; 
		}
		
		public String getResultString()
		{
			return resultString; 
		}
		
		public AsynTaskSoundControl(Receiver receiver) {
			response = new ServerPackage();
			mReceiver = receiver;
			loading = true; 
		}
		
		private byte[] bData;
		
		public void setData(byte[] bData){
			this.bData = bData;
		}
		
		private byte[] bResult = new byte[]{0};
		
		@Override
		protected Integer doInBackground(String... params) {
			try {
				Log.e("", "START AsynTaskSoundControl");
				if (clientSocket == null || !clientSocket.isConnected() || authenID == 0) {
					// Log.e("", "SOCKET IS NULL");
					return SocketStatus.ERROR_DISCONNECTED;
				}
				
				resultString = "";
				
				cmdType = params[0];
				if(cmdType.equals("0")){ // GET DATA
					Log.e("", "GET DATA");
					
					byte[] authenArr = new byte[4]; 
					ByteUtils.intToBytes(authenArr, 0, authenID);					
					
					byte[] sentData = authenArr;
									
					byte[] sentPackage = prepareSendFull(IS_GET_EQU, sentData);				
					// Log.e("sentPackage", bytesToHex2(sentPackage));
					
					byte[] receivePackage = new byte[1000];				
					InputStream socketStream = null;
					
					int resultProcess = processSocket_SendReceive(mReceiver,
							"IS_GET_EQU", clientSocket, 
							sentPackage, receivePackage, socketStream,
							SocketStatus.ERROR_FROM_SERVER, SocketStatus.ERROR_SOCKET_READ);
					if (resultProcess != 999) {
						return resultProcess;
					}	
					
					receivePackage = processResponseData(receivePackage);
					response.parseServerPackage(receivePackage);			
									
//					Log.e("receivePackage", bytesToHex2(receivePackage));
					
					if(response.getCommand() != IS_GET_EQU){
						resultString = "socket busy";
						return SocketStatus.ERROR_SOCKET_BUSY;
					}	
					
					if (response.getStatus() != CMD_SUCCESS) {
						resultString = "error";
						return SocketStatus.ERROR_FROM_SERVER;
					} 				
					
					byte[] responseData = response.getData(); 
//					Log.e("response data", bytesToHex2(responseData));

					resultString = bytesToHex2(responseData);
					bResult = responseData;
					
//					Log.e("resultString", resultString);	
				} else if(cmdType.equals("1")){ // SET DATA
					Log.e("", "SET DATA 0");
					
					if(bData == null || bData.length == 0){
						resultString = "Data gui di empty";
						return SocketStatus.ERROR_FROM_SERVER;
					}

//					byte[] authenArr = new byte[4]; 
//					ByteUtils.intToBytes(authenArr, 0, authenID);										
//					
//					byte[] sentData = new byte[authenArr.length + bData.length];
//					System.arraycopy(authenArr, 0, sentData, 0, authenArr.length);
//					System.arraycopy(bData, 0, sentData, 4, bData.length);
					
					byte[] sentData = bData;
									
					byte[] sentPackage = prepareSendFull(IS_SET_EQU, sentData);				
//					Log.e("sentPackage", bytesToHex2(sentPackage));
					
					byte[] receivePackage = new byte[1000];				
					InputStream socketStream = null;
					
					int resultProcess = processSocket_SendReceive(mReceiver,
							"IS_SET_EQU", clientSocket, 
							sentPackage, receivePackage, socketStream,
							SocketStatus.ERROR_FROM_SERVER, SocketStatus.ERROR_SOCKET_READ);
					if (resultProcess != 999) {
						return resultProcess;
					}	
					
					receivePackage = processResponseData(receivePackage);
					response.parseServerPackage(receivePackage);	
					
//					Log.e("receivePackage", bytesToHex2(receivePackage));
									
					if(response.getCommand() != IS_SET_EQU){
						resultString = "socket busy";
						return SocketStatus.ERROR_SOCKET_BUSY;
					}	
					
					if (response.getStatus() != CMD_SUCCESS) {
						resultString = "error";					
						byte[] responseData = response.getData();
						try {
							int i = 0; 
					    	int len = 0; 
					        while (len < responseData.length && responseData[i++] != 0) {
					            len++;
					        }				
							resultString = new String(responseData, 0, len);
						} catch (Exception e) {
							
						}
				    	
						return SocketStatus.ERROR_FROM_SERVER;
					} 	

					resultString = "OK";
					
					byte[] responseData = response.getData();
					try {
						resultString = bytesToHex2(responseData);
					} catch (Exception e) {
						
					}
					
				}
				
				return SocketStatus.SUCCESS;
				
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				loading = false; 
			}
			return SocketStatus.ERROR_DISCONNECTED;
		}

		@Override
		protected void onPostExecute(Integer result) {
			loading = false; 
			if(mReceiver != null){
				if(cmdType.equals("0")){
					mReceiver.deviceSendSoundDataByte(bResult);
				} else {
					mReceiver.deviceSendSoundData(getResultString());	
				}
				
			}
			if(result == SocketStatus.SUCCESS){
				
			}else{
				sendEvent(mReceiver, result, errMsg);			
			}
		}
	}
	
	private class AsynTaskUSBStorageList extends AsyncTask<String, Integer, Integer> {
		private Receiver mReceiver = null;
		private String errMsg = "";
		private String resultString = ""; 
		private boolean loading = false; 
		private ServerPackage response = null;
		
		public boolean loadCompleted()
		{
			return loading == false; 
		}
		
		public String getResultString()
		{
			return resultString; 
		}
		
		public AsynTaskUSBStorageList(Receiver receiver) {
			response = new ServerPackage();
			mReceiver = receiver;
			loading = true; 
		}

		@Override
		protected Integer doInBackground(String... params) {
			try {
				Log.e("", "START AsynTaskUSBStorageList");
				if (clientSocket == null || !clientSocket.isConnected() || authenID == 0) {
					// Log.e("", "SOCKET IS NULL");
					return SocketStatus.ERROR_DISCONNECTED;
				}
				
				int type = 0;
				try {
					type = Integer.parseInt(params[1]);
				} catch (Exception e) {
					
				}
				
				Log.d("", "type = " + type);
				
				resultString = null;

				byte[] authenArr = new byte[4]; 
				ByteUtils.intToBytes(authenArr, 0, authenID);		
				
				byte[] sentData = new byte[authenArr.length + 1];	
				System.arraycopy(authenArr, 0, sentData, 0, authenArr.length);
				sentData[4] = (byte)type;
				
				byte[] sentPackage = prepareSendFull(CMD_REQ_USBSTORAGE, sentData);
				
				byte[] receivePackage = new byte[1500];				
				InputStream socketStream = null;
				
				int resultProcess = processSocket_SendReceive(mReceiver,
						"CMD_REQ_USBSTORAGE", clientSocket, 
						sentPackage, receivePackage, socketStream,
						SocketStatus.ERROR_FROM_SERVER, SocketStatus.ERROR_SOCKET_READ);
				if (resultProcess != 999) {
					return resultProcess;
				}	
				
				receivePackage = processResponseData(receivePackage);
				response.parseServerPackage(receivePackage);			
								
				if(response.getCommand() != CMD_REQ_USBSTORAGE){
					return SocketStatus.ERROR_SOCKET_BUSY;
				}	
				
				if (response.getStatus() != CMD_SUCCESS) {
					return SocketStatus.SUCCESS;
				} 				
				
				byte[] responseData = response.getData(); 
//				Log.e("response data", bytesToHex2(responseData));

		    	int i = 0; 
		    	int len = 0; 
		        while (len < responseData.length && responseData[i++] != 0) {
		            len++;
		        }
				
				resultString = new String(responseData, 0, len);

				Log.e("resultString", resultString);	
				
				return SocketStatus.SUCCESS;
				
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				loading = false; 
			}
			return SocketStatus.ERROR_DISCONNECTED;
		}

		@Override
		protected void onPostExecute(Integer result) {
			loading = false; 
			if(result == SocketStatus.SUCCESS){
				if(mReceiver != null){
					mReceiver.deviceSendUSBStorageList(getResultString());
				}
			}else{
				sendEvent(mReceiver, result, errMsg);
				if(result == SocketStatus.ERROR_SOCKET_READ){
					if(mReceiver != null){
						mReceiver.deviceSendUSBStorageList(getResultString());
					}	
				}				
			}
		}
	}
	
	private class AsynTaskSongFromUSB extends AsyncTask<String, Integer, Integer> {
		private Receiver mReceiver = null;
		private String errMsg = "";
		private String resultString = "";
		private String linkStr = "";
		private boolean loading = false; 
		private ServerPackage response = null;
		
		private String usbNumber;
		
		public boolean loadCompleted()
		{
			return loading == false; 
		}
		
		public String getResultString()
		{
			return resultString; 
		}
		
		public AsynTaskSongFromUSB(Receiver receiver, String usbNumber) {
			response = new ServerPackage();
			mReceiver = receiver;
			loading = true; 
			this.usbNumber = usbNumber;
		}
		
		public AsynTaskSongFromUSB(Receiver receiver, String usbNumber, String linkStr) {
			response = new ServerPackage();
			mReceiver = receiver;
			loading = true; 
			this.linkStr = linkStr;
			this.usbNumber = usbNumber;
		}

		@Override
		protected Integer doInBackground(String... params) {
			try {
				Log.e("", "START AsynTaskSongFromUSB");
				if (clientSocket == null || !clientSocket.isConnected() || authenID == 0) {
					// Log.e("", "SOCKET IS NULL");
					return SocketStatus.ERROR_DISCONNECTED;
				}
				
				byte[] linkB = new byte[0];
				
				resultString = null;
				
				byte[] authenArr = new byte[4]; 
				ByteUtils.intToBytes(authenArr, 0, authenID);
				
				byte[] sentData = new byte[authenArr.length + 1];
				
				Log.d(" ", "linkStr = " + linkStr);
				
				if(!linkStr.equals("")){
					linkB = linkStr.getBytes();
					sentData = new byte[authenArr.length + 1 + linkB.length];
				}
				
				System.arraycopy(authenArr, 0, sentData, 0, authenArr.length);
				sentData[4] = (byte)Integer.parseInt(usbNumber);
				
				if(!linkStr.equals("")){
					System.arraycopy(linkB, 0, sentData, 5, linkB.length);	
				}				
				
				byte[] sentPackage = prepareSendFull(CMD_REQ_SONGUSB, sentData);
				
				byte[] receivePackage = new byte[1500];				
				InputStream socketStream = null;
				
				int resultProcess = processSocket_SendReceive(mReceiver,
						"CMD_REQ_SONGUSB", clientSocket, 
						sentPackage, receivePackage, socketStream,
						SocketStatus.ERROR_FROM_SERVER, SocketStatus.ERROR_SOCKET_READ);
				if (resultProcess != 999) {
					return resultProcess;
				}	
				
				receivePackage = processResponseData(receivePackage);
				response.parseServerPackage(receivePackage);			
								
				if(response.getCommand() != CMD_REQ_SONGUSB){
					return SocketStatus.ERROR_SOCKET_BUSY;
				}	
				
				if (response.getStatus() != CMD_SUCCESS) {
					return SocketStatus.SUCCESS;
				} 				
				
				byte[] responseData = response.getData(); 
//				Log.e("response data", bytesToHex2(responseData));

		    	int i = 0; 
		    	int len = 0; 
		        while (len < responseData.length && responseData[i++] != 0) {
		            len++;
		        }
				
				resultString = new String(responseData, 0, len);

				Log.e("resultString", resultString);	
				
				return SocketStatus.SUCCESS;
				
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				loading = false; 
			}
			return SocketStatus.ERROR_DISCONNECTED;
		}

		@Override
		protected void onPostExecute(Integer result) {
			loading = false; 
			if(result == SocketStatus.SUCCESS){
				if(mReceiver != null){
					mReceiver.deviceSendSongFromUSB(getResultString());
				}
			}else{
				sendEvent(mReceiver, result, errMsg);
				if(result == SocketStatus.ERROR_SOCKET_READ){
					if(mReceiver != null){
						mReceiver.deviceSendSongFromUSB(getResultString());
					}	
				}				
			}
		}
	}
	
	private class AsynTaskScoreInfo extends AsyncTask<String, Integer, Integer> {
		private Receiver mReceiver = null;
		private String errMsg = "";
		private List<Integer> listScore; 
		private boolean loading = false; 
		private ServerPackage response = null;
		
		public boolean loadCompleted()
		{
			return loading == false; 
		}
		
		public List<Integer> getListScore()
		{
			return listScore; 
		}
		
		public AsynTaskScoreInfo(Receiver receiver) {
			response = new ServerPackage();
			mReceiver = receiver;
			loading = true; 
		}

		@Override
		protected Integer doInBackground(String... params) {
			try {
				//Log.e("", "START SCORE INFO");
				if (clientSocket == null || !clientSocket.isConnected() || authenID == 0) {
					//Log.e("", "SOCKET IS NULL");
					return SocketStatus.ERROR_DISCONNECTED;
				}
				
				listScore = new ArrayList<Integer>();
				
				byte[] authenArr = new byte[4]; 
				ByteUtils.intToBytes(authenArr, 0, authenID);					
				
				byte[] sentPackage = prepareSendFull(CMD_GET_SCORE, authenArr);				
				//Log.e("sentPackage", bytesToHex2(sentPackage));
				
				byte[] receivePackage = new byte[1000];				
				InputStream socketStream = null;
				
				int resultProcess = processSocket_SendReceive(mReceiver,
						"CMD_GET_SCORE", clientSocket, 
						sentPackage, receivePackage, socketStream,
						SocketStatus.ERROR_FROM_SERVER, SocketStatus.ERROR_SOCKET_READ);
				if (resultProcess != 999) {
					return resultProcess;
				}	
				
				receivePackage = processResponseData(receivePackage);
				response.parseServerPackage(receivePackage);			
								
				if(response.getCommand() != CMD_GET_SCORE){
					return SocketStatus.ERROR_SOCKET_BUSY;
				}	
				
				if (response.getStatus() != CMD_SUCCESS) {
					return SocketStatus.SUCCESS;
				} 				
				
				byte[] responseData = response.getData(); 
				//Log.e("response data", bytesToHex2(responseData));

				if(svrModel== SONCA_SK9xxx || svrModel == SONCA_SMARTK 
						|| svrModel == SONCA_SMARTK_801 || svrModel == SONCA_SMARTK_KM4  || svrModel == SONCA_SMARTK_CB
						|| svrModel == SONCA_SMARTK_9108_SYSTEM || svrModel == SONCA_SMARTK_KM4_SYSTEM){ // RESULT WITH 6 BYTE
					if(responseData.length >= 6){
						listScore.add(svrModel);
						listScore.add(ByteUtils.byteToInt8(responseData, 0));
						listScore.add(ByteUtils.byteToInt8(responseData, 1));
						listScore.add(ByteUtils.byteToInt8(responseData, 2));
						listScore.add(ByteUtils.byteToInt8(responseData, 3));
						listScore.add(ByteUtils.byteToInt8(responseData, 4));
						listScore.add(ByteUtils.byteToInt8(responseData, 5));
				    }	
				} else {
					if(responseData.length > 0){
						listScore.add(svrModel);
						listScore.add(ByteUtils.byteToInt8(responseData, 0));
					}
				}
				
				return SocketStatus.SUCCESS;
				
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				loading = false; 
			}
			return SocketStatus.ERROR_DISCONNECTED;
		}

		@Override
		protected void onPostExecute(Integer result) {
			loading = false; 
			if(result == SocketStatus.SUCCESS){
				if(mReceiver != null){
					mReceiver.deviceSendScoreInfo(getListScore());
				}
			}else{
				sendEvent(mReceiver, result, errMsg);
				if(result == SocketStatus.ERROR_SOCKET_READ){
					if(mReceiver != null){
						mReceiver.deviceSendScoreInfo(getListScore());
					}	
				}				
			}
		}
	}
	
	private class AsynTaskAdminPass extends AsyncTask<String, Integer, Integer> {
		private Receiver mReceiver = null;
		private String errMsg = "";
		private String resultString = ""; 
		private boolean loading = false; 
		private ServerPackage response = null;
		
		public boolean loadCompleted()
		{
			return loading == false; 
		}
		
		public String getResultString()
		{
			return resultString; 
		}
		
		public AsynTaskAdminPass(Receiver receiver) {
			response = new ServerPackage();
			mReceiver = receiver;
			loading = true; 
		}

		@Override
		protected Integer doInBackground(String... params) {
			try {
				// Log.e("", "START ADMIN PASS");
				if (clientSocket == null || !clientSocket.isConnected() || authenID == 0) {
					// Log.e("", "SOCKET IS NULL");
					return SocketStatus.ERROR_DISCONNECTED;
				}
				
				resultString = null;
				
				byte[] authenArr = new byte[4]; 
				ByteUtils.intToBytes(authenArr, 0, authenID);					
				
				byte[] sentPackage = prepareSendFull(CMD_GET_PASSADMIN, authenArr);				
				// Log.e("sentPackage", bytesToHex2(sentPackage));
				
				byte[] receivePackage = new byte[1500];
				InputStream socketStream = null;
				
				int resultProcess = processSocket_SendReceive(mReceiver,
						"CMD_GET_PASSADMIN", clientSocket, 
						sentPackage, receivePackage, socketStream,
						SocketStatus.ERROR_FROM_SERVER, SocketStatus.ERROR_DISCONNECTED);
				if (resultProcess != 999) {
					return resultProcess;
				}	
				
				receivePackage = processResponseData(receivePackage);
				response.parseServerPackage(receivePackage);			
				
				if(response.getCommand() != CMD_GET_PASSADMIN){
					return SocketStatus.ERROR_SOCKET_BUSY;
				}	
				
				if (response.getStatus() != CMD_SUCCESS) {
					// Log.e("response data", "response.getStatus() != CMD_SUCCESS");	
					return SocketStatus.SUCCESS;
				} 				
				
				byte[] responseData = response.getData(); 
				// Log.e("response data", bytesToHex2(responseData));	
				if(responseData.length > 0){
			    	int i = 0; 
			    	int len = 0; 
			        while (len < responseData.length && responseData[i++] != 0) {
			            len++;
			        }
					resultString = new String(responseData, 0, len);
					// Log.e("resultString", resultString);	
			    } 
				
				return SocketStatus.SUCCESS;
				
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				loading = false; 
			}
			return SocketStatus.ERROR_DISCONNECTED;
		}

		@Override
		protected void onPostExecute(Integer result) {
			loading = false; 
			if(result == SocketStatus.SUCCESS){
				if(mReceiver != null){
					mReceiver.deviceSendAdminPass(getResultString());
				}
			}else{
				sendEvent(mReceiver, result, errMsg);
				if(result == SocketStatus.ERROR_SOCKET_READ){
					if(mReceiver != null){
						mReceiver.deviceSendAdminPass(getResultString());
					}	
				}				
			}
		}
	}
	
	private class AsynTaskHIW_FirmwareConfig extends AsyncTask<String, Integer, Integer> {
		private Receiver mReceiver = null;
		private String errMsg = "";
		private String resultString = ""; 
		private boolean loading = false; 
		private ServerPackage response = null;
		
		public boolean loadCompleted()
		{
			return loading == false; 
		}
		
		private int mode;
		private String stationID;
		private String stationPass;
		private String apID;
		private String apPass;
		private String passConnect;
		private String passAdmin;
		
		public int getMode() {
			return mode;
		}

		public String getStationID() {
			return stationID;
		}

		public String getStationPass() {
			return stationPass;
		}

		public String getApID() {
			return apID;
		}

		public String getApPass() {
			return apPass;
		}

		public String getPassConnect() {
			return passConnect;
		}

		public String getPassAdmin() {
			return passAdmin;
		}
		
		public String getResultString()
		{
			return resultString; 
		}
		
		public AsynTaskHIW_FirmwareConfig(Receiver receiver) {
			response = new ServerPackage();
			mReceiver = receiver;
			loading = true; 
		}
		
		private boolean flagSendData = false;
		public boolean isSendData(){
			return this.flagSendData;
		}

		@Override
		protected Integer doInBackground(String... params) {
			try {
				// Log.e("", "START HIW FIRMWARE CONFIG");
				if (clientSocket == null || !clientSocket.isConnected() || authenID == 0) {
					// Log.e("", "SOCKET IS NULL");
					return SocketStatus.ERROR_DISCONNECTED;
				}
				
				flagSendData = false;
				resultString = null;
				
				if(params[0].contains("0")){ // GET DATA
					mode = 0;
					stationID = "";
					stationPass = "";
					apID = "";
					apPass = "";
					passConnect = "";
					passAdmin = "";
					
					byte[] authenArr = new byte[4]; 
					ByteUtils.intToBytes(authenArr, 0, authenID);					
					
					byte[] sentPackage = prepareSendFull(CMD_GET_CONFIGWIFI, authenArr);				
					// Log.e("sentPackage", bytesToHex2(sentPackage));
					
					byte[] receivePackage = new byte[1500];
					InputStream socketStream = null;
					
					int resultProcess = processSocket_SendReceive(mReceiver,
							"CMD_GET_CONFIGWIFI", clientSocket, 
							sentPackage, receivePackage, socketStream,
							SocketStatus.ERROR_FROM_SERVER, SocketStatus.ERROR_SOCKET_READ);
					if (resultProcess != 999) {
						return resultProcess;
					}	
					
					receivePackage = processResponseData(receivePackage);
					response.parseServerPackage(receivePackage);			
										
					if (response.getStatus() != CMD_SUCCESS) {
						// Log.e("response data", "response.getStatus() != CMD_SUCCESS");	
						return SocketStatus.SUCCESS;
					} 				
					
					byte[] responseData = response.getData(); 
					// Log.e("response data", bytesToHex2(responseData));	
					if (responseData.length > 0) {
						resultString = "OK";
						
						// mode
						mode = responseData[0];
						
						// ap ID
						int i = 1; 
				    	int len = 0; 
				        while (len < 17 && responseData[i++] != 0) {
				            len++;
				        }
				        apID = new String(responseData, 1, len);
				        
				        // ap Pass
						i = 17; 
				    	len = 0; 
				        while (len < 17 && responseData[i++] != 0) {
				            len++;
				        }
				        apPass = new String(responseData, 17, len);
				        
				        // station ID
						i = 33; 
				    	len = 0; 
				        while (len < 17 && responseData[i++] != 0) {
				            len++;
				        }
				        stationID = new String(responseData, 33, len);
				        
				        // station Pass
						i = 49; 
				    	len = 0; 
				        while (len < 17 && responseData[i++] != 0) {
				            len++;
				        }
				        stationPass = new String(responseData, 49, len);
				        
				        // pass connect
				        passConnect = "" + ByteUtils.byteToInt16L(responseData, 65);
				        
				        // pass admin
				        passAdmin = "" + ByteUtils.byteToInt16L(responseData, 67);
					}
					
					return SocketStatus.SUCCESS;
				} else { // SEND DATA
					flagSendData = true;
					
					mode = Integer.parseInt(params[1]);
					stationID = params[2];
					stationPass = params[3];
					apID = params[4];
					apPass = params[5];
					passConnect = params[6];
					passAdmin = params[7];
					
					byte[] sentData = new byte[69]; 
					
					sentData[0] = (byte)mode;					

					byte[] strData = apID.getBytes("utf-8"); 					
					System.arraycopy(strData, 0, sentData, 1, strData.length);
					
					strData = apPass.getBytes("utf-8"); 					
					System.arraycopy(strData, 0, sentData, 17, strData.length);
					
					strData = stationID.getBytes("utf-8"); 					
					System.arraycopy(strData, 0, sentData, 33, strData.length);
					
					strData = stationPass.getBytes("utf-8"); 					
					System.arraycopy(strData, 0, sentData, 49, strData.length);
					
					ByteUtils.int16ToBytesL(sentData, 65, Integer.parseInt(passConnect));
					ByteUtils.int16ToBytesL(sentData, 67, Integer.parseInt(passAdmin));
					
					// Log.e("sentData", bytesToHex2(sentData));
					
					byte[] sentPackage = prepareSendFull(CMD_SET_CONFIGWIFI, sentData);	
					
					byte[] receivePackage = new byte[1500];
					InputStream socketStream = null;

					int resultProcess = processSocket_SendReceive(mReceiver,
							"CMD_SET_CONFIGWIFI", clientSocket, sentPackage,
							receivePackage, socketStream,
							SocketStatus.ERROR_FROM_SERVER,
							SocketStatus.ERROR_SOCKET_READ);
					if (resultProcess != 999) {
						return resultProcess;
					}
					
					receivePackage = processResponseData(receivePackage);
					response.parseServerPackage(receivePackage);			
					
					if (response.getStatus() != CMD_SUCCESS) {
						// Log.e("response data", "response.getStatus() != CMD_SUCCESS");	
						return SocketStatus.SUCCESS;
					} 		
					
					resultString = "OK";
					return SocketStatus.SUCCESS;
				}
				
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				loading = false; 
			}
			return SocketStatus.ERROR_DISCONNECTED;
		}

		@Override
		protected void onPostExecute(Integer result) {
			loading = false;
			
			if(result == SocketStatus.SUCCESS){
				if(isSendData()){					
					if(mReceiver != null){
						mReceiver.deviceInform_ShowMessage("HIW_SETTING");
					}	
				} else {
					if(mReceiver != null){
						mReceiver.deviceSendHIW_FirmareConfig(resultString, mode, stationID, stationPass, apID, apPass, passConnect, passAdmin);
					}	
				}				
			}else{
				sendEvent(mReceiver, result, errMsg);
				if(result == SocketStatus.ERROR_SOCKET_READ){
					if(mReceiver != null){
						mReceiver.deviceSendHIW_FirmareConfig(resultString, mode, stationID, stationPass, apID, apPass, passConnect, passAdmin);
					}
				}				
			}				
		}
	}
	
	private class AsynTaskHIW_FirmwareInfo extends AsyncTask<String, Integer, Integer> {
		private Receiver mReceiver = null;
		private String errMsg = "";
		private String resultString = ""; 
		private boolean loading = false; 
		private ServerPackage response = null;
		
		private String daumay_name;
		private String daumay_version;
		private String wifi_version;
		private int wifi_revision;
		
		public String getDaumay_name() {
			return daumay_name;
		}
		public String getDaumay_version() {
			return daumay_version;
		}
		public String getWifi_version() {
			return wifi_version;
		}
		public int getWifi_revision() {
			return wifi_revision;
		}
		
		public boolean loadCompleted()
		{
			return loading == false; 
		}
		
		public String getResultString()
		{
			return resultString; 
		}
		
		public AsynTaskHIW_FirmwareInfo(Receiver receiver) {
			response = new ServerPackage();
			mReceiver = receiver;
			loading = true; 
		}

		@Override
		protected Integer doInBackground(String... params) {
			try {
				// Log.e("", "START HIW FIRMWARE");
				if (clientSocket == null || !clientSocket.isConnected() || authenID == 0) {
					// Log.e("", "SOCKET IS NULL");
					return SocketStatus.ERROR_DISCONNECTED;
				}
				
				resultString = null;
				daumay_name = null;
				daumay_version = null;
				wifi_version = null;
				wifi_revision = -1;
				
				byte[] authenArr = new byte[4]; 
				ByteUtils.intToBytes(authenArr, 0, authenID);					
				
				byte[] sentPackage = prepareSendFull(CMD_HIW_ABOUT, authenArr);				
				// Log.e("sentPackage", bytesToHex2(sentPackage));
				
				byte[] receivePackage = new byte[1500];
				InputStream socketStream = null;

				int resultProcess = processSocket_SendReceive(mReceiver,
						"CMD_HIW_ABOUT", clientSocket, sentPackage,
						receivePackage, socketStream,
						SocketStatus.ERROR_FROM_SERVER,
						SocketStatus.ERROR_SOCKET_READ);
				if (resultProcess != 999) {
					return resultProcess;
				}
				
				receivePackage = processResponseData(receivePackage);
				response.parseServerPackage(receivePackage);			
						
				if (response.getStatus() != CMD_SUCCESS) {
					// Log.e("response data", "response.getStatus() != CMD_SUCCESS");	
					return SocketStatus.SUCCESS;
				} 				
				
				byte[] responseData = response.getData(); 
				// Log.e("response data", bytesToHex2(responseData));	
				if (responseData.length > 0) {
					resultString = "OK";
					
					// dau may name
					int i = 0; 
			    	int len = 0; 
			        while (len < 17 && responseData[i++] != 0) {
			            len++;
			        }
			        daumay_name = new String(responseData, 0, len);
					
					// dau may version
					daumay_version = "" + ByteUtils.byteToInt32L(responseData, 16);

					// wifi version
					wifi_version = "" + ByteUtils.byteToInt32L(responseData, 20);

					// wifi revision
					wifi_revision = ByteUtils.byteToInt32L(responseData, 24);
				}
				
				return SocketStatus.SUCCESS;
				
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				loading = false; 
			}
			return SocketStatus.ERROR_DISCONNECTED;
		}

		@Override
		protected void onPostExecute(Integer result) {
			loading = false; 
			if(result == SocketStatus.SUCCESS){
				if(mReceiver != null){
					mReceiver.deviceSendHIW_FirmareInfo(getResultString(), getDaumay_name(), getDaumay_version(), getWifi_version(), getWifi_revision());
				}
			}else{
				sendEvent(mReceiver, result, errMsg);
				if(result == SocketStatus.ERROR_SOCKET_READ){
					if(mReceiver != null){
						mReceiver.deviceSendHIW_FirmareInfo(getResultString(), getDaumay_name(), getDaumay_version(), getWifi_version(), getWifi_revision());
					}
				}				
			}
		}
	}
	
	private class AsynTaskDeviceIsUser extends AsyncTask<String, Integer, Integer> {
		private Receiver mReceiver = null;
		private String errMsg = "";
		private String resultString = ""; 
		private boolean loading = false; 
		private ServerPackage response = null;
		
		public boolean loadCompleted()
		{
			return loading == false; 
		}
		
		public String getResultString()
		{
			return resultString; 
		}
		
		public AsynTaskDeviceIsUser(Receiver receiver) {
			response = new ServerPackage();
			mReceiver = receiver;
			loading = true; 
		}

		@Override
		protected Integer doInBackground(String... params) {
			try {
//				Log.e("  ", "  ");Log.e("  ", "  ");
//				Log.e(" AsynTaskDeviceIsUser ", " VERY START ");
				if (clientSocket == null || !clientSocket.isConnected() || authenID == 0) {
					return SocketStatus.ERROR_DISCONNECTED;
				}
				
				resultString = "null";
				
				byte[] authenArr = new byte[4]; 
				ByteUtils.intToBytes(authenArr, 0, authenID);					
				
				byte[] sentPackage = prepareSendFull(CMD_GET_DEVICEUSER, authenArr);	
				
				byte[] receivePackage = new byte[1500];
				InputStream socketStream = null;

				int resultProcess = processSocket_SendReceive(mReceiver,
						"CMD_GET_DEVICEUSER", clientSocket, sentPackage,
						receivePackage, socketStream,
						SocketStatus.ERROR_FROM_SERVER,
						SocketStatus.ERROR_SOCKET_READ);
				if (resultProcess != 999) {
					return resultProcess;
				}
				
				receivePackage = processResponseData(receivePackage);
				response.parseServerPackage(receivePackage);			
				
				if(response.getCommand() != CMD_GET_DEVICEUSER){
					return SocketStatus.ERROR_SOCKET_BUSY;
				}
				
				if (response.getStatus() != CMD_SUCCESS) {
					return SocketStatus.SUCCESS;
				} 				
				
				byte[] responseData = response.getData(); 
//				Log.e("response data", bytesToHex2(responseData));	
				if(responseData.length > 0){
					resultString = ByteUtils.byteToInt8(responseData, 0) + "";
			    } 
				
				return SocketStatus.SUCCESS;
				
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				loading = false; 
			}
			return SocketStatus.ERROR_DISCONNECTED;
		}

		@Override
		protected void onPostExecute(Integer result) {
			loading = false; 
			if(result == SocketStatus.SUCCESS){
				//Log.e("getResultString()", "....." + getResultString());
				if(mReceiver != null){
					mReceiver.deviceSendDeviceUser(getResultString());
				}
			}else{
				sendEvent(mReceiver, result, errMsg);
				if(result == SocketStatus.ERROR_SOCKET_READ){
					if(mReceiver != null){
						mReceiver.deviceSendDeviceUser(getResultString());
					}	
				}	
			}
		}
	}	
	
	private class AsynTaskAdminControl extends AsyncTask<String, Integer, Integer> {
		private Receiver mReceiver = null;
		private String errMsg = "";
		private String resultString = ""; 
		private boolean loading = false; 
		private ServerPackage response = null;
		
		public boolean loadCompleted()
		{
			return loading == false; 
		}
		
		public String getResultString()
		{
			return resultString; 
		}
		
		public AsynTaskAdminControl(Receiver receiver) {
			response = new ServerPackage();
			mReceiver = receiver;
			loading = true; 
		}

		@Override
		protected Integer doInBackground(String... params) {
			try {
				// Log.e("", "     VERY START ADMIN CONTROL       ");
				if (clientSocket == null || !clientSocket.isConnected() || authenID == 0) {
					// Log.e("", "SOCKET IS NULL");
					return SocketStatus.ERROR_DISCONNECTED;
				}

				String password = params[0];
				String controlData = params[1];
				// Log.e("", "password = " + password);
				// Log.e("", "controlData = " + controlData);
				
				byte[] authenArr = new byte[4]; 
				ByteUtils.intToBytes(authenArr, 0, authenID);	
				
				byte[] passwordArr = password.getBytes();
				
				byte[] sentData = new byte[authenArr.length + 4 + 2];
				System.arraycopy(authenArr, 0, sentData, 0, authenArr.length);
				System.arraycopy(passwordArr, 0, sentData, 4, passwordArr.length);
				
				// controlData to 2 bytes
				int controlValue = Integer.parseInt(controlData);
				// Log.e("", "controlValue = " + controlValue);
				ByteUtils.int16ToBytes(sentData, 8, controlValue);
				
				byte tempByte = sentData[sentData.length - 2];
				sentData[sentData.length - 2] = sentData[sentData.length - 1];
				sentData[sentData.length - 1] = tempByte;
				
				// Log.e("sentData", bytesToHex2(sentData));
				
				byte[] sentPackage = prepareSendFull(CMD_SET_ADMINCONTROL, sentData);				
				// Log.e("sentPackage", bytesToHex2(sentPackage));
				
				byte[] receivePackage = new byte[1500];
				InputStream socketStream = null;

				int resultProcess = processSocket_SendReceive(mReceiver,
						"CMD_SET_ADMINCONTROL", clientSocket, sentPackage,
						receivePackage, socketStream,
						SocketStatus.ERROR_DISCONNECTED,
						SocketStatus.ERROR_DISCONNECTED);
				if (resultProcess != 999) {
					return resultProcess;
				}
				
				receivePackage = processResponseData(receivePackage);			
				
				int status = receivePackage[1];
								
				if (status != CMD_SUCCESS) {						
					return SocketStatus.SUCCESS;
				} 				
				resultString = "OK";
				return SocketStatus.SUCCESS;
				
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				loading = false; 
			}
			return SocketStatus.ERROR_DISCONNECTED;
		}

		@Override
		protected void onPostExecute(Integer result) {
			loading = false; 
			if(result == SocketStatus.SUCCESS){
				// Log.e("SocketStatus.SUCCESS", ".......................");
			}
			else{
				sendEvent(mReceiver, result, errMsg);
			}
		}
	}	
	
	private class AsynTaskFirmwareUpdate extends AsyncTask<String, Integer, Integer> {
		private Receiver mReceiver = null;
		private String errMsg = "";
		private String resultString = ""; 
		private boolean loading = false; 
		private ServerPackage response = null;
		
		public boolean loadCompleted()
		{
			return loading == false; 
		}
		
		public String getResultString()
		{
			return resultString; 
		}
		
		public AsynTaskFirmwareUpdate(Receiver receiver) {
			response = new ServerPackage();
			mReceiver = receiver;
			loading = true; 
		}

		@Override
		protected Integer doInBackground(String... params) {
			try {
//				Log.e("", "     VERY START AsynTaskFirmwareUpdate       ");
				if (clientSocket == null || !clientSocket.isConnected() || authenID == 0) {
					//Log.e("", "SOCKET IS NULL");
					return SocketStatus.ERROR_DISCONNECTED;
				}

				int intCommand = Integer.parseInt(params[0]);
				// intCommand - 0: CHECK - 1: PREPARE - 2: BEGIN
				
				byte byteCommand = ByteUtils.intToByte(intCommand);
				
				byte[] authenArr = new byte[4]; 
				ByteUtils.intToBytes(authenArr, 0, authenID);	
								
				byte[] sentData = new byte[authenArr.length + 1];
				System.arraycopy(authenArr, 0, sentData, 0, authenArr.length);
				sentData[sentData.length - 1] = byteCommand;
								
//				Log.e("sentData", bytesToHex2(sentData));
				
				byte[] sentPackage = prepareSendFull(CMD_ESP_FIRMWARE_UPDATE, sentData);				
//				Log.e("sentPackage", bytesToHex2(sentPackage));
				
				byte[] receivePackage = new byte[1500];
				InputStream socketStream = null;

				int resultProcess = processSocket_SendReceive(mReceiver,
						"CMD_ESP_FIRMWARE_UPDATE", clientSocket, sentPackage,
						receivePackage, socketStream,
						SocketStatus.ERROR_DISCONNECTED,
						SocketStatus.ERROR_DISCONNECTED);
				if (resultProcess != 999) {
					return resultProcess;
				}
				
				receivePackage = processResponseData(receivePackage);			
				
				int status = receivePackage[1];
								
				if (status != CMD_SUCCESS) {
					return SocketStatus.ERROR_FROM_SERVER;
				} 				
				resultString = "OK";
				return SocketStatus.SUCCESS;
				
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				loading = false; 
			}
			return SocketStatus.ERROR_DISCONNECTED;
		}

		@Override
		protected void onPostExecute(Integer result) {
			loading = false; 
			if(result == SocketStatus.SUCCESS){
//				Log.e("SocketStatus.SUCCESS", ".......................");
			}
			else{
//				Log.e("SocketStatus.FAILED", ".......................");
				sendEvent(mReceiver, result, errMsg);
			}
		}
	}	
	
	private class AsynTaskOnOffControlFull extends AsyncTask<String, Integer, Integer> {
		private Receiver mReceiver = null;
		private String errMsg = "";
		private String resultString = ""; 
		private boolean loading = false; 
		private ServerPackage response = null;
		
		public boolean loadCompleted()
		{
			return loading == false; 
		}
		
		public String getResultString()
		{
			return resultString; 
		}
		
		public AsynTaskOnOffControlFull(Receiver receiver) {
			response = new ServerPackage();
			mReceiver = receiver;
			loading = true; 
		}

		@Override
		protected Integer doInBackground(String... params) {
			try {
				//Log.e("", "     VERY START AsynTaskOnOffControlFull       ");
				if (clientSocket == null || !clientSocket.isConnected() || authenID == 0) {
					//Log.e("", "SOCKET IS NULL");
					return SocketStatus.ERROR_DISCONNECTED;
				}

				String password = params[0];
				String controlData = params[1];
				//Log.e("", "password = " + password);
				//Log.e("", "controlData = " + controlData);
				
				byte[] authenArr = new byte[4]; 
				ByteUtils.intToBytes(authenArr, 0, authenID);	
				
				byte[] passwordArr = password.getBytes();
				
				byte[] sentData = new byte[authenArr.length + 4 + 4];
				System.arraycopy(authenArr, 0, sentData, 0, authenArr.length);
				System.arraycopy(passwordArr, 0, sentData, 4, passwordArr.length);
				
				// controlData to 4 bytes
				int controlValue = Integer.parseInt(controlData);
				//Log.e("", "controlValue = " + controlValue);
				ByteUtils.intToBytes(sentData, 8, controlValue);
				
				// revert byte
				byte tempByte = sentData[sentData.length - 1];
				sentData[sentData.length - 1] = sentData[sentData.length - 4];
				sentData[sentData.length - 4] = tempByte;
				tempByte = sentData[sentData.length - 2];
				sentData[sentData.length - 2] = sentData[sentData.length - 3];
				sentData[sentData.length - 3] = tempByte;
				
				//Log.e("sentData", bytesToHex2(sentData));
				
				byte[] sentPackage = prepareSendFull(CMD_SET_ONOFFCONTROL_FULL, sentData);				
				//Log.e("sentPackage", bytesToHex2(sentPackage));
				
				byte[] receivePackage = new byte[1500];
				InputStream socketStream = null;

				int resultProcess = processSocket_SendReceive(mReceiver,
						"CMD_SET_ONOFFCONTROL_FULL", clientSocket, sentPackage,
						receivePackage, socketStream,
						SocketStatus.ERROR_DISCONNECTED,
						SocketStatus.ERROR_DISCONNECTED);
				if (resultProcess != 999) {
					return resultProcess;
				}
				
				receivePackage = processResponseData(receivePackage);			
				
				int status = receivePackage[1];
								
				if (status != CMD_SUCCESS) {
					return SocketStatus.SUCCESS;
				} 				
				resultString = "OK";
				return SocketStatus.SUCCESS;
				
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				loading = false; 
			}
			return SocketStatus.ERROR_DISCONNECTED;
		}

		@Override
		protected void onPostExecute(Integer result) {
			loading = false; 
			if(result == SocketStatus.SUCCESS){
				//Log.e("SocketStatus.SUCCESS", ".......................");
			}
			else{
				sendEvent(mReceiver, result, errMsg);
			}
		}
	}	
	
	private class AsynTaskOnOffUserList extends AsyncTask<String, Integer, Integer> {
		private Receiver mReceiver = null;
		private String errMsg = "";
		private String resultString = ""; 
		private boolean loading = false; 
		private ServerPackage response = null;
		
		public boolean loadCompleted()
		{
			return loading == false; 
		}
		
		public String getResultString()
		{
			return resultString; 
		}
		
		public AsynTaskOnOffUserList(Receiver receiver) {
			response = new ServerPackage();
			mReceiver = receiver;
			loading = true; 
		}

		@Override
		protected Integer doInBackground(String... params) {
			try {
				if (clientSocket == null || !clientSocket.isConnected() || authenID == 0) {
					// Log.e("", "SOCKET IS NULL");
					return SocketStatus.ERROR_DISCONNECTED;
				}
				// Log.e("", "START ON OFF USER LIST");
				
				byte[] authenArr = new byte[4]; 
				ByteUtils.intToBytes(authenArr, 0, authenID);	
				
				String password = params[1];
				byte[] passwordArr = password.getBytes();			
				
				int flagOnOffUserList = Integer.parseInt(params[0]);
				byte flagOnOffUserArr = ByteUtils.intToByte(flagOnOffUserList);				
				
				byte[] sentData = new byte[authenArr.length + 4 + 1];
				System.arraycopy(authenArr, 0, sentData, 0, authenArr.length);
				System.arraycopy(passwordArr, 0, sentData, 4, passwordArr.length);
				sentData[sentData.length - 1] = flagOnOffUserArr;
				
				// Log.e("sentData", bytesToHex2(sentData));
				
				byte[] sentPackage = prepareSendFull(CMD_SET_ONOFFUSERLIST,sentData);				
				// Log.e("sentPackage", bytesToHex2(sentPackage));
				
				byte[] receivePackage = new byte[1500];
				InputStream socketStream = null;

				int resultProcess = processSocket_SendReceive(mReceiver,
						"CMD_SET_ONOFFUSERLIST", clientSocket, sentPackage,
						receivePackage, socketStream,
						SocketStatus.ERROR_DISCONNECTED,
						SocketStatus.ERROR_DISCONNECTED);
				if (resultProcess != 999) {
					return resultProcess;
				}
				
				receivePackage = processResponseData(receivePackage);
				
				//response.parseServerPackage(receivePackage);
				// Log.e("response data", bytesToHex2(response.getData()));				
				
				int status = receivePackage[1];
								
				if (status != CMD_SUCCESS) {						
					return SocketStatus.ERROR_FROM_SERVER;
				} 				
				resultString = "OK";
				return SocketStatus.SUCCESS;
				
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				loading = false; 
			}
			return SocketStatus.ERROR_DISCONNECTED;
		}

		@Override
		protected void onPostExecute(Integer result) {
			loading = false; 
			if(result == SocketStatus.SUCCESS){
				
			}
			else{
				sendEvent(mReceiver, result, errMsg);
			}
		}
	}	
	
	private class AsynTaskUserCaption extends AsyncTask<String, Integer, Integer> {
		private Receiver mReceiver = null;
		private String errMsg = "";
		private String resultString = ""; 
		private boolean loading = false; 
		
		public boolean loadCompleted()
		{
			return loading == false; 
		}
		
		public String getResultString()
		{
			return resultString; 
		}
		
		public AsynTaskUserCaption(Receiver receiver) {
			mReceiver = receiver;
			loading = true; 
		}

		@Override
		protected Integer doInBackground(String... params) {
			try {
				loading = true; 
				if (clientSocket == null || !clientSocket.isConnected()
						|| authenID == 0) {
					// Log.e("", "SOCKET IS NULL");
					return SocketStatus.ERROR_DISCONNECTED;
				}
//				Log.e(" ", " ");
//				Log.e("", "VERY START USER CAPTION");

				byte[] authenArr = new byte[4];
				ByteUtils.intToBytes(authenArr, 0, authenID);	
				
//				Log.e(" authenArr ", bytesToHex2(authenArr));

				byte[] sentPackage = null;
				if (Integer.parseInt(params[0]) == 1) {
					byte[] captions = params[1].getBytes("utf-8");
					if(svrModel == SONCA_HIW || svrModel == SONCA_KM2 || svrModel == SONCA_TBT || svrModel == SONCA_KBX9
							|| svrModel == SONCA_KB_OEM || svrModel == SONCA_KM1_WIFI || svrModel == SONCA_KB39C_WIFI){
						String strTemp = params[1] + " ";
						captions = new ConvertString().ConvertUnicodeToTCVN(strTemp);
					}					
					byte[] sentData = new byte[authenArr.length
							+ captions.length];
					System.arraycopy(authenArr, 0, sentData, 0,
							authenArr.length);
					System.arraycopy(captions, 0, sentData, 4, captions.length);
					sentPackage = prepareSendFull(CMD_SET_CAPTION, captions);
				} else {
					sentPackage = prepareSendFull(CMD_GET_CAPTION, authenArr);
				}

				byte[] receivePackage = new byte[1500];
				InputStream socketStream = clientSocket.getInputStream();
				
				String commandName = "";
				if (Integer.parseInt(params[0]) == 1) {
					commandName = "CMD_SET_CAPTION";
				} else {
					commandName = "CMD_GET_CAPTION";
				}

//				Log.e(" sentPackage ", bytesToHex2(sentPackage));
				
				int resultProcess = processSocket_SendReceive(mReceiver,
						commandName, clientSocket, sentPackage,
						receivePackage, socketStream,
						SocketStatus.ERROR_DISCONNECTED,
						SocketStatus.ERROR_DISCONNECTED);
				if (resultProcess != 999) {
					return resultProcess;
				}

				receivePackage = processResponseData(receivePackage);

//				Log.e(" receivePackage ", bytesToHex2(receivePackage));
				
				int command = receivePackage[0];
				int status = receivePackage[1];
				int dataLen = ByteUtils.byteToInt32(receivePackage, 2);
				ServerPackage response = new ServerPackage();
				response.parseServerPackage(receivePackage);
				
				if (status != CMD_SUCCESS) {
					byte[] errorStr = new byte[dataLen + 1];
					for (int i = 0; i < 5; i++) {
						errorStr[i] = receivePackage[6 + i];
					}

					errMsg = new String(errorStr);
//					Log.e("", "Message: " + errMsg);

					return SocketStatus.ERROR_FROM_SERVER;
				}

				byte[] responseData = response.getData();
//				Log.e(" responseData ", bytesToHex2(responseData));
				if (responseData.length > 0) {
					int i = 0;
					int len = 0;
					while (len < responseData.length && responseData[i++] != 0) {
						len++;
					}

					if(svrModel == SONCA_HIW || svrModel == SONCA_KM2 || svrModel == SONCA_TBT || svrModel == SONCA_KBX9
							|| svrModel == SONCA_KB_OEM || svrModel == SONCA_KM1_WIFI || svrModel == SONCA_KB39C_WIFI){
						ConvertString converter = new ConvertString();
						
						// step 1
						byte[] tempData = new byte[len];					
						System.arraycopy(responseData, 0, tempData, 0, len);	
						
//						Log.e(" tempData 1", bytesToHex2(tempData));
												
						// step 2
						List<Integer> listConvertTab = new ArrayList<Integer>();
						listConvertTab.clear();
						
						for (int j = 0; j < tempData.length; j++) {
	
							int val = 0;
							val |= (((int) tempData[j]) << 0) & 0x000000FF;
	
							int tempInt = converter.ConvertTab2ToTab1(val);							
							tempData[j] = (byte) (tempInt);
							
							if(tempInt != val){
								listConvertTab.add(j);
							}
						}
						
//						Log.e(" tempData 2", bytesToHex2(tempData));
						
						// step 3
						String str = converter.ConvertTCVNtoUnicode(tempData);						
//						Log.e(" str", str);
						
						// step 4
						resultString = converter.TCVN3ToUnicode(str);
//						Log.e(" resultString 1", resultString);
						
						// step 5
						if(listConvertTab.size() > 0){
							for (int j = 0; j < listConvertTab.size(); j++) {
								int idx = listConvertTab.get(j);
								char tempChar = resultString.charAt(idx);
								tempChar = Character.toUpperCase(tempChar);
								
								StringBuilder newString = new StringBuilder(resultString);
								newString.setCharAt(idx, tempChar);
								
								resultString = newString.toString();
							}
						}
					} else {
						resultString = new String(responseData, 0, len);
					}					
					
//					Log.e("resultString final", resultString);
				}

				return SocketStatus.SUCCESS;

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				loading = false;
			}
			return SocketStatus.ERROR_DISCONNECTED;
		}

		@Override
		protected void onPostExecute(Integer result) {
			loading = false; 
			if(result == SocketStatus.SUCCESS){
				
			}
			else{
				sendEvent(mReceiver, result, errMsg);
			}
		}
	}
	
	private class AsynTaskDownloadFileUpdate extends AsyncTask<String, Integer, Integer> {
		private Receiver mReceiver = null;
		private String errMsg = "";
		private String filePath = ""; 
		private boolean loading = false; 

		public boolean loadCompleted()
		{
			return loading == false; 
		}
		
		public AsynTaskDownloadFileUpdate(Receiver receiver) {
			mReceiver = receiver;
		}

		@Override
		protected Integer doInBackground(String... params) {
			FileOutputStream fos = null; 
			try {
				if (clientSocket == null || !clientSocket.isConnected() || authenID == 0) {
//					Log.e(TAB, "SOCKET IS NULL");
					return SocketStatus.ERROR_DISCONNECTED;
				}
				Log.e(" ", "START DOWNLOAD -- " + params[1]);
				
				byte[] link = new byte[0]; 
				if (params.length == 3) {
					link = params[2].getBytes(); 
				}

				byte[] dataPackage = new byte[4 + link.length];
				
				if(params.length == 4 && Integer.parseInt(params[1]) == CMD_REQ_LOADYOUTUBE){					
					dataPackage = new byte[4 + 30];
					link = params[3].getBytes();
					ByteUtils.intToBytes(dataPackage, 0, authenID);
					if (link.length > 0) {
						System.arraycopy(link, 0, dataPackage, 4, link.length);
					}	
				} else {
					ByteUtils.intToBytes(dataPackage, 0, authenID);
					if (link.length > 0) {
						System.arraycopy(link, 0, dataPackage, 4, link.length);
					}					
				}			
				
				loading = true; 
				byte[] sentPackage = prepareSendFull(Integer.parseInt(params[1]),dataPackage);
				byte[] receivePackage = new byte[11];
				
				clientSocket.getOutputStream().write(sentPackage);
				InputStream socketStream = clientSocket.getInputStream();
				int byteRecv = socketStream.read(receivePackage, 0, receivePackage.length);
					//--------------------//
//				response = new ServerPackage();
//				response.parseServerPackage(receivePackage);
//				if (response.getStatus() != CMD_SUCCESS) {
//					Log.d(TAB ,"ERROR DATA : <" + response.getStatus() + ">");
//					return -1;
//				} 
//				int contentLength = ByteUtils.byteToInt32(response.getData(), 0);
				
				if (byteRecv == -1) {
//					Log.e("", "Read failed: " + byteRecv); 
					return SocketStatus.ERROR_DISCONNECTED; 
				}
				
				receivePackage = processResponseData(receivePackage);
				
				int command = receivePackage[0];
				int status = receivePackage[1];
				int dataLen = ByteUtils.byteToInt32(receivePackage, 2);
//				System.out.printf("\nData len: %d\n", dataLen);

				filePath = params[0]; 
				if (status != CMD_SUCCESS) {
//					byte[] errorStr = new byte[dataLen + 1]; 
//					for(int i = 0; i < 5; i++) {
//						errorStr[i] = receivePackage[6 + i]; 
//					}
//
//					errMsg = new String(errorStr); 
//					Log.e("", "Message: " + errMsg); 
					
					while(socketStream.available() > 0){
						socketStream.read(new byte[100] , 0 , socketStream.available());
					}
					
//					if(socketStream.available() > 0 && Integer.parseInt(params[1]) == CMD_REQ_LOADYOUTUBE){
//						Log.e("", "SPECIAL CASE ");
//						socketStream.read(receivePackage , 0 , receivePackage.length);
//					}
					
					return SocketStatus.ERROR_FROM_SERVER;
				} 
				int contentLength = ByteUtils.byteToInt32(receivePackage, 6); 
//				Log.d(" " , "DATA : " + contentLength);
				int TotalByteReceive = 0;
				if (contentLength <= 0) {
					return SocketStatus.ERROR_INVALID_RESPONSE;
				}
				publishProgress(0, 0, contentLength);
				
					//--------------------//
//				Log.d(" " ,"DOWNLOADING...");
				fos = new FileOutputStream(params[0]);
				receivePackage = new byte[1024];
				try {

					while (TotalByteReceive < contentLength) {
//						byteRecv = readInputStreamWithTimeout(socketStream, receivePackage, 3000);
						byteRecv = socketStream.read(receivePackage , 0 , receivePackage.length);
						if (byteRecv == -1) {
//							Log.d(TAB , "byte read = " + byteRecv);
							break;
						}
						fos.write(receivePackage, 0, byteRecv);
						TotalByteReceive += byteRecv;
//						Log.i("" , "byteRecv = " + byteRecv + " -- TotalByteReceive = " + TotalByteReceive);
						publishProgress(1, TotalByteReceive, contentLength);
					}
				} catch (Exception e) {
					Log.e("DOWNLOAD", "SPECIAL ERROR DOWNLOAD");
					e.printStackTrace();
					publishProgress(2);
				}
				
				fos.flush();
				fos.close();
				if (TotalByteReceive < contentLength) {
					return SocketStatus.ERROR_INVALID_RESPONSE;
				}
//				Log.e(TAB, "STOP DOWNLOAD");
				return SocketStatus.SUCCESS;
			} 
			catch (Exception e) {
				e.printStackTrace();
			} finally {
				loading = false; 
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException ignored) {
						ignored.printStackTrace();
					} 
				}
			}
			return SocketStatus.ERROR_DISCONNECTED;
		}
		
		public int readInputStreamWithTimeout(InputStream is, byte[] b,
				int timeoutMillis) throws IOException {
			int bufferOffset = 0;
			long maxTimeMillis = System.currentTimeMillis() + timeoutMillis;
			while (System.currentTimeMillis() < maxTimeMillis
					&& bufferOffset < b.length) {
				int readLength = java.lang.Math.min(is.available(), b.length
						- bufferOffset);
				// can alternatively use bufferedReader, guarded by isReady():
				int readResult = is.read(b, bufferOffset, readLength);
				if (readResult == -1)
					break;
				bufferOffset += readResult;
			}
			return bufferOffset;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// 0: command, 1: loaded, 2: total
			if (mReceiver != null) {
				if (values[0] == 0) {
					mReceiver.deviceDidReceiveHeader(values[2]);
				}else if(values[0] == 1) {
					mReceiver.deviceDidReceiveDataOfLength(values[1], values[2]);
				}else if(values[0] == 2) {
					mReceiver.deviceDidDownloadFile(false, filePath);
//					mReceiver.deviceInform_ShowMessage("DOWNLOAD_ERROR");
				}
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			loading = false; 
			if(result == SocketStatus.SUCCESS){
				if (mReceiver != null) {
					mReceiver.deviceDidDownloadFile(true, filePath);
				}
			}
			else if(result == SocketStatus.ERROR_FROM_SERVER){
				if (mReceiver != null) {
					mReceiver.deviceDidDownloadFile(false, filePath);
				}
			}
			else{
				sendEvent(mReceiver, result, errMsg);
			}
		}
	}

	private class AsynTaskAuthenHost extends AsyncTask<String, Integer, Integer> {
		private Receiver mReceiver = null;
		private ServerPackage response = null;
		private String errMsg = ""; 

		public AsynTaskAuthenHost(Receiver receiver) {
			response = new ServerPackage();
			mReceiver = receiver;
		}

		@Override
		protected Integer doInBackground(String... params) {
			Log.e("        ", "   AsynTaskAuthenHost         ");
			// Log.e("NEED ENCRYPTION", flagEncrypt + ".......................");
			// flagEncrypt = false;
			if(flagEncrypt){
				// Log.e("        ", "   AsynTaskAuthenHost         ");
				if (clientSocket == null || !clientSocket.isConnected()) {
					// Log.e("", "SOCKET IS NULL");
					return SocketStatus.ERROR_DISCONNECTED;
				}
				
				InputStream socketStream;
				
				// generate random number
				Random random = new Random();
				int randomAppNumber = random.nextInt(MAX_RANDOM) + 1;
				byte[] sentRandom = ByteBuffer.allocate(4)
						.putInt(randomAppNumber).array();

				// sent random number for server encryption
				byte[] sentPackage = sentRandom;
				byte[] receivePackage = new byte[1500];
				try {
					clientSocket.getOutputStream().write(sentPackage);
					socketStream = clientSocket.getInputStream();
					int byteRecv = socketStream.read(receivePackage, 0, 32);
					if (byteRecv == -1) {
						return SocketStatus.ERROR_INVALID_RESPONSE;
					}
				} catch (IOException e) {
					e.printStackTrace();
					return SocketStatus.ERROR_DISCONNECTED;				
				}

				// self-encrypt A
				byte[] encrypte_A = sha256_Encrypte_A(sentRandom);
				if (encrypte_A == null) {
					return SocketStatus.ERROR_DISCONNECTED;
				}

				// compare 28 bytes for sha256_A
				byte[] appCompare = Arrays.copyOfRange(encrypte_A, 4, 32);
				byte[] serverCompare = Arrays
						.copyOfRange(receivePackage, 4, 32);
				// Log.e("sha256_A app", bytesToHex2(appCompare));
				// Log.e("sha256_A server", bytesToHex2(serverCompare));
				if (!Arrays.equals(appCompare, serverCompare)) {
					// Log.e("","COMPARE sha256_A failed");
					return SocketStatus.ERROR_DISCONNECTED;
				}

				// Log.e("","...............COMPARE sha256_A success");

				// get random-hiW
				byte[] random_hiW = new byte[4];
				byte[] app4Byte = Arrays.copyOfRange(encrypte_A, 0, 4);
				byte[] server4Byte = Arrays.copyOfRange(receivePackage, 0, 4);
				for (int i = 0; i < server4Byte.length; i++) {
					random_hiW[i] = (byte) (app4Byte[i] ^ server4Byte[i]);
				}
				// Log.e("random_hiW", bytesToHex2(random_hiW));

				// self-encrypt B
				byte[] encrypte_B = sha256_Encrypte_B(random_hiW);
				// Log.e("sha256_B app", bytesToHex2(encrypte_B));
				if (encrypte_B == null) {
					return SocketStatus.ERROR_DISCONNECTED;
				}

				// sent sha256_b to server
				sentPackage = encrypte_B;
				receivePackage = new byte[1500];
				try {
					clientSocket.getOutputStream().write(sentPackage);
					socketStream = clientSocket.getInputStream();
					int byteRecv = socketStream.read(receivePackage, 0,
							receivePackage.length);
					if (byteRecv == -1) {
						return SocketStatus.ERROR_INVALID_RESPONSE;
					}
				} catch (IOException e) {
					e.printStackTrace();
					return SocketStatus.ERROR_DISCONNECTED;					
				}

				// Log.e("response after send sha256_B","");
				// Log.e("",bytesToHex2(receivePackage));
				
				int statusSha256_B = receivePackage[1];
				// Log.e("statusSha256_B", statusSha256_B + "");
				
				if(statusSha256_B == 3){ // FULL CONNECTION
					// Log.e("", "FULL CONNECTION");
					return SocketStatus.ERROR_DISCONNECTED;
				}

				// Log.e("","CHECK SOCKET SUCCESSFUL");
					//---------------------//
				String password = params[0];
				// Log.e("" , "START AUTHEN : " + password);
				if(iEchoDevice)
					sentPackage = prepareSendFull(CMD_REQ_AUTHEN_ECHO,password.getBytes());
				else
					sentPackage = prepareSendFull(CMD_REQ_AUTHEN,password.getBytes());
				receivePackage = new byte[1500];
				try {
					clientSocket.getOutputStream().write(sentPackage);
					socketStream = clientSocket.getInputStream();
					int byteRecv = socketStream.read(receivePackage , 0 , receivePackage.length);
					// Log.e("receivePackage raw", bytesToHex2(receivePackage));
					if(byteRecv == -1)
					{
						// Log.e(" ", "byteRecv == -1");
						return SocketStatus.ERROR_INVALID_RESPONSE; 
					}
				} catch (IOException e) {
					e.printStackTrace();
					return SocketStatus.ERROR_DISCONNECTED;	
				}
					//---------------------//
				receivePackage = decryptResponseData(receivePackage);
				// Log.e("receivePackage decrypt", bytesToHex2(receivePackage));
				
				response.parseServerPackage(receivePackage);
				// Log.e("response data", bytesToHex2(response.getData()));
				
				if (response.getStatus() != CMD_SUCCESS) {
					errMsg = new String(response.getData()); 
					return SocketStatus.ERROR_FROM_SERVER; 
				} else {
					authenID = ByteUtils.byteToInt32(response.getData(), 0);
					// Log.e("" , "AUTHEN SUCCESSFUL : " + authenID);
				}
			} else {
				// Log.e("        ", "            ");
				if (clientSocket == null || !clientSocket.isConnected()) {
					// Log.e("", "SOCKET IS NULL");
					return SocketStatus.ERROR_DISCONNECTED;
				}
					//---------------------//
				String password = params[0];
				// Log.e("" , "START AUTHEN : " + password);
				int tmp_cmd=CMD_REQ_AUTHEN;
				if(iEchoDevice)
					tmp_cmd=CMD_REQ_AUTHEN_ECHO;
				byte[] sentPackage = prepareSend(tmp_cmd,password.getBytes());
				byte[] receivePackage = new byte[1500];
				try {
					clientSocket.getOutputStream().write(sentPackage);
					InputStream socketStream = clientSocket.getInputStream();
					int byteRecv = socketStream.read(receivePackage , 0 , receivePackage.length);
					if(byteRecv == -1){
						return SocketStatus.ERROR_INVALID_RESPONSE; 
					}
				} catch (IOException e) {
					e.printStackTrace();
					return SocketStatus.ERROR_DISCONNECTED;					
				}
					//---------------------//
				response.parseServerPackage(receivePackage);
				if (response.getStatus() != CMD_SUCCESS) {
					errMsg = new String(response.getData()); 
					return SocketStatus.ERROR_FROM_SERVER; 
				} else {
					authenID = ByteUtils.byteToInt32(response.getData(), 0);
					//Log.e("" , "AUTHEN SUCCESSFUL : " + authenID);
				}
			}

			return SocketStatus.SUCCESS;
		}
		
		private byte[] sha256_Encrypte_A(byte[] data){
			byte[] digest = new byte[32];
			try {
				int sha256_keyA = 1567890123;
				byte[] sha256_keyAByte = ByteBuffer.allocate(4)
						.putInt(sha256_keyA).array();

				// revert key
				byte[] newKeyData = new byte[4];
				int count = sha256_keyAByte.length - 1;
				for (int i = 0; i < sha256_keyAByte.length; i++) {
					newKeyData[count] = sha256_keyAByte[i];
					count--;
				}

				MessageDigest md = MessageDigest.getInstance("SHA-256");
				md.update(data);
				md.update(newKeyData);
				digest = md.digest();
				
			} catch (Exception e) {
				return null;
			}
			return digest;
		}
		
		private byte[] sha256_Encrypte_B(byte[] data){
			byte[] digest = new byte[32];
			try {
				int sha256_keyA = 1785236951;
				byte[] sha256_keyAByte = ByteBuffer.allocate(4)
						.putInt(sha256_keyA).array();

				// revert key
				byte[] newKeyData = new byte[4];
				int count = sha256_keyAByte.length - 1;
				for (int i = 0; i < sha256_keyAByte.length; i++) {
					newKeyData[count] = sha256_keyAByte[i];
					count--;
				}

				MessageDigest md = MessageDigest.getInstance("SHA-256");
				md.update(newKeyData);
				md.update(data);
				digest = md.digest();
			} catch (Exception e) {
				return null;
			}
			return digest;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(result == SocketStatus.SUCCESS){
				if (mReceiver != null) {
					mReceiver.deviceDidAuthenWithServer(response, svrModel, codeVersion);
				}
			}else if(result == SocketStatus.ERROR_FROM_SERVER) {
				if (mReceiver != null) {
					response.setErrorMessage(errMsg);
					mReceiver.deviceDidAuthenWithServer(response, svrModel, codeVersion);
				}
			}else{
				sendEvent(mReceiver, result, errMsg);
			}
		}
	}
	
	public String bytesToHex2(byte[] bytes) {
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

	private boolean iEchoDevice=false;
	public void setEchoDevice(boolean iDevice){
		iEchoDevice=iDevice;
	}
	private int svrModel;
	private int codeVersion;
	private String svrPassConnect,wifiName,wifiPass;
	
	private class AsynTaskConnectHost extends AsyncTask<String, Integer, Integer> {
		private Receiver mReceiver = null;
		private ServerPackage response = null;
		private String errMsg = ""; 
		private String srvName = ""; 

		public AsynTaskConnectHost(Receiver receiver) {
			response = new ServerPackage(); 
			mReceiver = receiver;
		}

		@Override
		protected Integer doInBackground(String... params) {
			try {
//				Log.e("" , "VERY START AsynTaskConnectHost");
				if (clientSocket == null || !clientSocket.isConnected()) {
					String host = params[0];
//					Log.e("" , "START CONNECT : " + host);
					SocketAddress sockaddr = new InetSocketAddress(host,
							REMOTE_CONTROL_PORT);
					clientSocket = new Socket();
//					clientSocket.setTcpNoDelay(true); 
//					clientSocket.setKeepAlive(false);
//					setKeepaliveSocketOptions(clientSocket, 5000, 5000, 3);
					clientSocket.connect(sockaddr, DEFAULT_TIMEOUT);
					clientSocket.setSoTimeout(DEFAULT_TIMEOUT);
					
				} else {
//					Log.e("" , "CONNECT ALREADY EXISTS");
					return SocketStatus.ERROR_CONNECT_EXISTED;
				}
			} catch (SocketTimeoutException e) {
				clientSocket = null;
				errMsg = "Socket timeout";
				e.printStackTrace();
				return SocketStatus.ERROR_TIMEDOUT;
			} catch (IOException e) {
				clientSocket = null;
				e.printStackTrace();
				return SocketStatus.ERROR_DISCONNECTED;
			}
			// -----------------------//
			int tmp_cmd=CMD_REQ_CONNECT;
			if(iEchoDevice)
				tmp_cmd=CMD_REQ_CONNECT_ECHO;
			
			byte[] sentPackage = prepareSend(tmp_cmd,
					SONCA_CONNECT.getBytes());
//			Log.e("sent Request connect", bytesToHex2(sentPackage));
			byte[] receivePackage = new byte[1500];
			InputStream socketStream;
			try {
				clientSocket.getOutputStream().write(sentPackage);
				socketStream = clientSocket.getInputStream();
				int byteRecv = socketStream.read(receivePackage, 0,
						receivePackage.length);
				if (byteRecv == -1) {
					Log.e("", "byteRecv == -1");
					return SocketStatus.ERROR_INVALID_RESPONSE;
				}
			} catch (IOException e) {
				e.printStackTrace();
				return SocketStatus.ERROR_DISCONNECTED;
			}

			// -----------------------//
			svrModel = 0;
			codeVersion = 0;
			response.parseServerPackage(receivePackage);
			byte[] responseData = response.getData();
			//Log.e("", "=AsynTaskConnectHost ip="+params[0]+"=pass="+params[1]+"=responseData="+responseData.length+"=="+bytesToHex2(responseData));
			if (responseData.length == 40) { // ooold SK9xxx
				svrModel = 0;
			} else if (responseData.length >= 42 && responseData.length<78) { // new SK9xxx + KARTROL or 8203R
				svrModel = responseData[41] & 0xff;
				try {					
					codeVersion = responseData[42] & 0xff;	
				} catch (Exception e) {
				}
			}else if (responseData.length >= 78){//echo
				 /*struct _datasend {
			            char brand_name[8];
			            char device_name[68];//32 byte devicename + 32 byte pass wifi + 4 byte pass connect
			            uint8_t version_code;//1
			            uint8_t model;//1
			            uint8_t version[4];
			            uint8_t revision[4];
			        }datasend;*/
				svrModel = responseData[77] & 0xff;
				svrPassConnect =new String(responseData, 72, 4);
				
				try {					
					codeVersion = responseData[78] & 0xff;	
				} catch (Exception e) {
				}
			}
			
//			 Log.e("","");
//			 Log.e("Server Model","...................................");
//			 Log.e("",bytesToHex2(responseData));		 
//			 Log.e("","" + svrModel);
//			 Log.e("","codeVersion = " + codeVersion);
//			 Log.e("","");
			
			if(svrModel == SONCA_SK9xxx || svrModel == SONCA_SMARTK || svrModel == SONCA_SMARTK_9108_SYSTEM || svrModel == SONCA_SMARTK_KM4_SYSTEM
					|| svrModel == SONCA_SMARTK_801  || svrModel == SONCA_SMARTK_CB || svrModel == SONCA_SMARTK_KM4){
				flagEncrypt = false;	
			} else {
				flagEncrypt = true;
			}	
			
			if (response.getStatus() != CMD_SUCCESS) {
				errMsg = new String(response.getData());
				return SocketStatus.ERROR_FROM_SERVER;
			} else {
//				Log.e("" , "CONNECT SUCCESSFUL");

				responseData = response.getData();
				if (!ByteUtils.compareByte(responseData,
						"SCA_CONN".getBytes())) {
					Log.e("", "Invalid sonca connect response");
					return SocketStatus.ERROR_INVALID_RESPONSE;
				}
				if (responseData.length >= 32 + 8) {
					int i = 8;
					int len = 0;
					while (responseData[i++] != 0 && len < 32) {
						len++;
					}
					srvName = new String(responseData, 8, len);
				}
			}

			// authenticate after connect if password available
			if (params.length <= 1) {
//				Log.e("", "params.length <= 1");
				return SocketStatus.CONNECTED_TOHOST;
			}
			publishProgress(SocketStatus.CONNECTED_TOHOST);

			// Log.e("        ", "            ");
			if (clientSocket == null || !clientSocket.isConnected()) {
				// Log.e("", "SOCKET IS NULL");
				return SocketStatus.ERROR_DISCONNECTED;
			}
			
			// Log.e("NEED ENCRYPTION", flagEncrypt + ".......................");

			// flagEncrypt = false;
			if (flagEncrypt) { // WITH ENCRYPTION
				// generate random number
				Random random = new Random();
				int randomAppNumber = random.nextInt(MAX_RANDOM) + 1;
				byte[] sentRandom = ByteBuffer.allocate(4)
						.putInt(randomAppNumber).array();

				// sent random number for server encryption
				sentPackage = sentRandom;
				receivePackage = new byte[1500];
				try {
					clientSocket.getOutputStream().write(sentPackage);
					socketStream = clientSocket.getInputStream();
					int byteRecv = socketStream.read(receivePackage, 0, 32);
					if (byteRecv == -1) {
						return SocketStatus.ERROR_INVALID_RESPONSE;
					}
				} catch (IOException e) {
					e.printStackTrace();
					return SocketStatus.ERROR_DISCONNECTED;				
				}

				// self-encrypt A
				byte[] encrypte_A = sha256_Encrypte_A(sentRandom);
				if (encrypte_A == null) {
					return SocketStatus.ERROR_DISCONNECTED;
				}

				// compare 28 bytes for sha256_A
				byte[] appCompare = Arrays.copyOfRange(encrypte_A, 4, 32);
				byte[] serverCompare = Arrays
						.copyOfRange(receivePackage, 4, 32);
				// Log.e("sha256_A app", bytesToHex2(appCompare));
				// Log.e("sha256_A server", bytesToHex2(serverCompare));
				if (!Arrays.equals(appCompare, serverCompare)) {
					// Log.e("","COMPARE sha256_A failed");
					return SocketStatus.ERROR_DISCONNECTED;
				}

				// Log.e("","...............COMPARE sha256_A success");

				// get random-hiW
				byte[] random_hiW = new byte[4];
				byte[] app4Byte = Arrays.copyOfRange(encrypte_A, 0, 4);
				byte[] server4Byte = Arrays.copyOfRange(receivePackage, 0, 4);
				for (int i = 0; i < server4Byte.length; i++) {
					random_hiW[i] = (byte) (app4Byte[i] ^ server4Byte[i]);
				}
				// Log.e("random_hiW", bytesToHex2(random_hiW));

				// self-encrypt B
				byte[] encrypte_B = sha256_Encrypte_B(random_hiW);
				// Log.e("sha256_B app", bytesToHex2(encrypte_B));
				if (encrypte_B == null) {
					return SocketStatus.ERROR_DISCONNECTED;
				}

				// sent sha256_b to server
				sentPackage = encrypte_B;
				receivePackage = new byte[1500];
				try {
					clientSocket.getOutputStream().write(sentPackage);
					socketStream = clientSocket.getInputStream();
					int byteRecv = socketStream.read(receivePackage, 0,
							receivePackage.length);
					if (byteRecv == -1) {
						return SocketStatus.ERROR_INVALID_RESPONSE;
					}
				} catch (IOException e) {
					e.printStackTrace();
					return SocketStatus.ERROR_DISCONNECTED;						
				}

				// Log.e("response after send sha256_B","");
				// Log.e("",bytesToHex2(receivePackage));
				
				int statusSha256_B = receivePackage[1];
				// Log.e("statusSha256_B", statusSha256_B + "");
				
				if(statusSha256_B == 3){ // FULL CONNECTION
					// Log.e("", "FULL CONNECTION");
					return SocketStatus.ERROR_DISCONNECTED;
				}

				// Log.e("","CHECK SOCKET SUCCESSFUL");

				// ---------------------//
				String password = params[1];
				// Log.e("" , "START AUTHEN : " + password);
				if(iEchoDevice)
					sentPackage = prepareSendFull(CMD_REQ_AUTHEN_ECHO,password.getBytes());
				else
					sentPackage = prepareSendFull(CMD_REQ_AUTHEN,password.getBytes());
				receivePackage = new byte[1500];
				try {
					clientSocket.getOutputStream().write(sentPackage);
					socketStream = clientSocket.getInputStream();
					int byteRecv = socketStream.read(receivePackage, 0,
							receivePackage.length);
					if (byteRecv == -1) {
						return SocketStatus.ERROR_INVALID_RESPONSE;
					}
				} catch (IOException e) {
					e.printStackTrace();
					return SocketStatus.ERROR_DISCONNECTED;	
				}

				// ---------------------//
				receivePackage = decryptResponseData(receivePackage);

				response.parseServerPackage(receivePackage);
				if (response.getStatus() != CMD_SUCCESS) {
					errMsg = new String(response.getData());
					return SocketStatus.ERROR_FROM_SERVER;
				} else {
					authenID = ByteUtils.byteToInt32(response.getData(), 0);
					// Log.e(TAB , "AUTHEN SUCCESSFUL : " + authenID);
				}
				// Log.e("        ", "            ");
			} else { // WITHOUT ENCRYPTION	
				// ---------------------//
				String password = params[1];
				 Log.e("" , "START AUTHEN : " + password+"=svrPassConnect="+svrPassConnect);
				 if(responseData.length >= 78 && !password.equals(svrPassConnect)){//echo
					 return SocketStatus.ERROR_INVALID_RESPONSE;
				 }
				 if(iEchoDevice)
					 sentPackage = prepareSend(CMD_REQ_AUTHEN_ECHO, password.getBytes());
				 else
					 sentPackage = prepareSend(CMD_REQ_AUTHEN, password.getBytes());
				receivePackage = new byte[1500];
				try {
					clientSocket.getOutputStream().write(sentPackage);
					socketStream = clientSocket.getInputStream();
					int byteRecv = socketStream.read(receivePackage, 0,
							receivePackage.length);
					if (byteRecv == -1) {
						return SocketStatus.ERROR_INVALID_RESPONSE;
					}
				} catch (IOException e) {
					e.printStackTrace();
					return SocketStatus.ERROR_DISCONNECTED;	
				}

				// ---------------------//
				response.parseServerPackage(receivePackage);
				if (response.getStatus() != CMD_SUCCESS) {
					errMsg = new String(response.getData());
					return SocketStatus.ERROR_FROM_SERVER;
				} else {
					authenID = ByteUtils.byteToInt32(response.getData(), 0);
					// Log.e(TAB , "AUTHEN SUCCESSFUL : " + authenID);
				}
				// Log.e("        ", "            ");
			}

			return SocketStatus.AUTHENTICATED_TOHOST; 
		}
		
		private byte[] sha256_Encrypte_A(byte[] data){
		byte[] digest = new byte[32];
		try {
			int sha256_keyA = 1567890123;
			byte[] sha256_keyAByte = ByteBuffer.allocate(4)
					.putInt(sha256_keyA).array();

			// revert key
			byte[] newKeyData = new byte[4];
			int count = sha256_keyAByte.length - 1;
			for (int i = 0; i < sha256_keyAByte.length; i++) {
				newKeyData[count] = sha256_keyAByte[i];
				count--;
			}

			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(data);
			md.update(newKeyData);
			digest = md.digest();
		} catch (Exception e) {
			return null;
		}
		return digest;
	}
	
	private byte[] sha256_Encrypte_B(byte[] data){
		byte[] digest = new byte[32];
		try {
			int sha256_keyA = 1785236951;
			byte[] sha256_keyAByte = ByteBuffer.allocate(4)
					.putInt(sha256_keyA).array();

			// revert key
			byte[] newKeyData = new byte[4];
			int count = sha256_keyAByte.length - 1;
			for (int i = 0; i < sha256_keyAByte.length; i++) {
				newKeyData[count] = sha256_keyAByte[i];
				count--;
			}

			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(newKeyData);
			md.update(data);
			digest = md.digest();
		} catch (Exception e) {
			return null;
		}
		return digest;
	}

		@Override
		protected void onProgressUpdate(Integer... values) {			
			if (values[0] == SocketStatus.CONNECTED_TOHOST) {
				// Log.e("onProgressUpdate", "CONNECTED_TOHOST");
				if (mReceiver != null) {
					mReceiver.deviceDidConnectWithServer(true, false, srvName);
				}
			}else if(values[0] == SocketStatus.AUTHENTICATED_TOHOST) {
				// Log.e("onProgressUpdate", "AUTHENTICATED_TOHOST");
				if (mReceiver != null) {
					mReceiver.deviceDidAuthenWithServer(response, svrModel, codeVersion);
				}
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			if (result == SocketStatus.CONNECTED_TOHOST) {
				if (mReceiver != null) {
					mReceiver.deviceDidConnectWithServer(true, true, srvName);
				}
			}else if(result == SocketStatus.AUTHENTICATED_TOHOST) {
				if (mReceiver != null) {
					mReceiver.deviceDidAuthenWithServer(response, svrModel, codeVersion);
				}
			}else if(result == SocketStatus.ERROR_FROM_SERVER) {
				if (mReceiver != null) {
					response.setErrorMessage(errMsg);
					mReceiver.deviceDidAuthenWithServer(response, svrModel, codeVersion);
				}
			}else{
				sendEvent(mReceiver, result, errMsg);
			}
		}
	}

	private class AsynTaskSearchDevice extends AsyncTask<Void, Integer, ArrayList<SKServer>> {
		private Receiver mReceiver = null;

		public AsynTaskSearchDevice(Receiver receiver) {
			mReceiver = receiver;
		}

		@Override
		protected ArrayList<SKServer> doInBackground(Void... params) {
			ArrayList<SKServer> servers = new ArrayList<SKServer>();
			DatagramSocket searchSocket = null;
			try {
				if (searchSocket == null) {
					searchSocket = new DatagramSocket(REMOTE_CONTROL_PORT,
							InetAddress.getByName("0.0.0.0"));
					searchSocket.setBroadcast(true);
					searchSocket.setSoTimeout(SEARCH_TIMEOUT);
				}

				byte[] sendData = prepareSend(CMD_LISTDEVICE,
						SONCA_HEADER.getBytes());

				InetAddress broadcast = InetAddress
						.getByName("255.255.255.255");
				DatagramPacket sendPacket = new DatagramPacket(sendData,
						sendData.length, broadcast, REMOTE_CONTROL_PORT);

				searchSocket.send(sendPacket);
				// System.out.println(getClass().getName() +
				// ">>> Request packet sent to: 255.255.255.255 (DEFAULT)");

				/*
				 * // Broadcast the message over all the network interfaces
				 * Enumeration<NetworkInterface> interfaces =
				 * NetworkInterface.getNetworkInterfaces(); while
				 * (interfaces.hasMoreElements()) { NetworkInterface
				 * networkInterface = interfaces.nextElement();
				 * 
				 * if (networkInterface.isLoopback() ||
				 * !networkInterface.isUp()) { continue; // Don't want to
				 * broadcast to the loopback interface }
				 * 
				 * for (InterfaceAddress interfaceAddress :
				 * networkInterface.getInterfaceAddresses()) { broadcast =
				 * interfaceAddress.getBroadcast(); if (broadcast == null) {
				 * continue; }
				 * 
				 * // Send the broadcast package! sendPacket = new
				 * DatagramPacket(sendData, sendData.length, broadcast,
				 * REMOTE_CONTROL_PORT); searchSocket.send(sendPacket);
				 * 
				 * 
				 * System.out.println(getClass().getName() +
				 * ">>> Request packet sent to: " + broadcast.getHostAddress() +
				 * "; Interface: " + networkInterface.getDisplayName()); } }
				 */

			} catch (Exception e) {
//				Log.e("", "Network discovery exeption");
				searchSocket.close();
				searchSocket = null;
				e.printStackTrace();
				return new ArrayList<SKServer>();
			}

			// System.out.println(getClass().getName() +
			// ">>> Done looping over all network interfaces. Now waiting for a reply!");

			byte[] receiveBuffer = new byte[1500];
			DatagramPacket receivePacket = new DatagramPacket(receiveBuffer,
					receiveBuffer.length);
			try {
				while (true) {
					if (isCancelled()) {
						break;
					}
					Arrays.fill(receiveBuffer, (byte) 0);
					// Wait for a response
					searchSocket.receive(receivePacket);
					ServerPackage response = new ServerPackage();
					response.parseServerPackage(receivePacket.getData());
					if (response.getStatus() != CMD_SUCCESS) {
//						Log.e("", "Status falsed====> return");
						continue;
					}

					byte[] data = Arrays.copyOfRange(response.getData(), 0, 8);

					String message = new String(data).trim();
					if (!message.equals(SONCA_HEADER)) {
//						Log.e("", "invalid header");
						continue;
					}
					// Log.e("","Has valid header. Add new Server");
					data = Arrays.copyOfRange(response.getData(), 8, 32);
					int firstInvalid = 0;
					for (firstInvalid = 0; firstInvalid < data.length; firstInvalid++) {
						if (data[firstInvalid] == 0)
							break;
					}

					String name = new String(data, 0, firstInvalid);
					SKServer aServer = new SKServer();
					aServer.setName(name);
					aServer.setConnectedIPStr(receivePacket.getAddress().getHostAddress());
					aServer.setState(SKServer.BROADCASTED);
					aServer.setModelDevice(response.getModelDevice());
					servers.add(aServer);
				}
			} catch (SocketTimeoutException e) {
				// Log.e("", "Search time out");
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Close the port!
			searchSocket.close();
			return servers;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(ArrayList<SKServer> servers) {
			if (mReceiver != null) {
				mReceiver.deviceSearchCompleted(servers);
			}
		}
	}	
	
	private class AsynTaskSyncServerStatus extends AsyncTask<Void, Integer, Byte[]>
	{
		private Receiver mReceiver = null; 

		public AsynTaskSyncServerStatus(Receiver receiver) {
			mReceiver = receiver;
		}

		@Override
		protected Byte[] doInBackground(Void... params) {
			if(isProcessingData){
				Log.e("AsynTaskSyncServerStatus", "PROCESSING DATA -- OUT SYNC");
				return new Byte[0];		
			}
			
			if (clientSocket == null || !clientSocket.isConnected()) {
//				Log.e(TAB, "SOCKET IS NULL");
				return new Byte[1];
			}
			//Log.e("      ", "       ");
			//Log.e("      ", "flagEncrypt = " + flagEncrypt);
			//-----------------------//
			byte[]  dataSent = new byte[4]; 
			ByteUtils.intToBytes(dataSent, 0, authenID);
			byte[] sentPackage = prepareSendFull(CMD_REQ_SYNCSERVERSTATUS, dataSent);
			byte[] receivePackage = new byte[2500];
						
			InputStream socketStream = null;
			
			int resultProcess = processSocket_SendReceive(mReceiver,
					"CMD_REQ_SYNCSERVERSTATUS", clientSocket, 
					sentPackage, receivePackage, socketStream,
					SocketStatus.ERROR_FROM_SERVER, SocketStatus.ERROR_DISCONNECTED);
			
			if (resultProcess != 999) {
				if(resultProcess == SocketStatus.ERROR_DISCONNECTED){
					return new Byte[1];
				}
				return new Byte[0];				
			}			
			
			//Log.e("SYNC receivePackage", bytesToHex2(receivePackage));
			
			//-----------------------//
			ServerPackage response = new ServerPackage();

			receivePackage = processResponseData(receivePackage);
			
			response.parseServerPackage(receivePackage);
			
			if(response.getCommand() != CMD_REQ_SYNCSERVERSTATUS){
				Log.d("SYNC", " ");
				Log.d("SYNC", "response.getCommand() != CMD_REQ_SYNCSERVERSTATUS");
				Log.d("SYNC", response.getCommand() + " -- " + CMD_REQ_SYNCSERVERSTATUS);
				return new Byte[0];
			}			
			
			if (response.getStatus() != CMD_SUCCESS) {
				Log.d("SYNC", " ");
				Log.d("SYNC", "response.getStatus() != CMD_SUCCESS");
				return new Byte[0]; 
			}else{
//				Log.d(TAB , "DATA : " + new String(response.getData()));
//				Log.e(TAB , "Sync SUCCESSFUL");
			}
//			Log.e("          ", "             ");
			
			int dataLen = 0; 
			if(response.getData() != null) 
				dataLen = response.getData().length; 
			
			Byte[] byteObjects = new Byte[dataLen]; 
			int i = 0; 
			for (byte b: response.getData()) {
				byteObjects[i++] = b; 
			}
			return byteObjects;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Byte[] result) {
			super.onPostExecute(result);
			if(result == null){
				sendEvent(mReceiver, SocketStatus.ERROR_SYNC_TIMEOUT, "");
				return;
			}
			//Log.e("sync result", "result.length = " + result.length);
			if(result.length != 0){
				if (mReceiver != null) {	
					try {
							if(result.length == 1){
								sendEvent(mReceiver, SocketStatus.ERROR_DISCONNECTED, "");	
								return;
							}
							if (result.length < 16 || result == null) {
								mReceiver.deviceDidReceiveSyncStatus(false, null);
								return; 
							}							
							byte[] data = new byte[result.length]; 
							for(int i = 0; i < result.length; i++){
						        data[i] = result[i];
						    }
							ServerStatus status = new ServerStatus(); 
							int settings = ByteUtils.byteToInt32L(data, 0); 
							status.setPlayBackState((settings >> 0) & 0x03);
							status.setMuted(((settings >> 2) & 0x01) == 1);
							status.setSingerOn(((settings >> 3) & 0x01) == 1);
							status.setCurrentTone((settings >> 4) & 0x03);
							status.setCurrentScore((settings >> 6) & 0x03);
							status.setDanceMode((settings >> 8) & 0x01);
							
							status.setCurrentMelody(((settings >> 9) & 0x0F));
							status.setCurrentTempo(((settings >> 13) & 0x0F) - 4);
							status.setCurrentKey(((settings >> 17) & 0x0F) - 6); 
							status.setCurrentVolume(((settings >> 21) & 0xFF));
							status.setMediaType(((settings >> 29) & 0x07));
							
			
			//				int playlist = ByteUtils.byteToInt32(data, 4); 
			//				int songCount = playlist & 0xFFFF; 
			//				int listIDOffset = playlist >> 16; 
							
			//				int playlist = ByteUtils.byteToInt32(data, 4); 
							int songCount = ByteUtils.byteToInt16(data, 4); 
							int listIDOffset = ByteUtils.byteToInt16(data, 6); 
							//Log.e(" ", "listIDOffset = " + listIDOffset);
							
							if(listIDOffset > 16){
								int settings2 = ByteUtils.byteToInt32L(data, 16);
								
//								Log.e("settings2" , settings2 + "");
								String strBit = "";
								for (int i = 0; i < 32; i++) {
									strBit += (((settings2 >> i) & 0x01) == 1) ? "1" : "0";
								}
//								Log.e("settings2 in bit" , strBit + "");
								
								status.setUserCaption((settings2 >> 0) & 0x01);						
								status.setFlagOffUserList(((settings2 >> 1) & 0x01) == 1);						
								status.setFlagOffFirst(((settings2 >> 2) & 0x01) == 1);
								status.setFlagOffVolumn(((settings2 >> 3) & 0x01) == 1);
								status.setFlagOffKey(((settings2 >> 4) & 0x01) == 1);
								status.setFlagOffTempo(((settings2 >> 5) & 0x01) == 1);
								status.setFlagOffTone(((settings2 >> 6) & 0x01) == 1);
								status.setFlagOffMelody(((settings2 >> 7) & 0x01) == 1);
								status.setFlagOffDefault(((settings2 >> 8) & 0x01) == 1);
								status.setFlagOffSinger(((settings2 >> 9) & 0x01) == 1);
								status.setFlagOffScore(((settings2 >> 10) & 0x01) == 1);
								status.setFlagOffRepeat(((settings2 >> 11) & 0x01) == 1);
								status.setFlagOffNext(((settings2 >> 12) & 0x01) == 1);
								status.setFlagOffDance(((settings2 >> 13) & 0x01) == 1);
								status.setFlagOffPlayPause(((settings2 >> 14) & 0x01) == 1);						
								
								status.setCaptionAPIValid(true); 
								
								if(svrModel == SONCA_SK9xxx || svrModel == SONCA_SMARTK || svrModel == SONCA_SMARTK_9108_SYSTEM || svrModel == SONCA_SMARTK_KM4_SYSTEM
										|| svrModel == SONCA_SMARTK_801 || svrModel == SONCA_SMARTK_CB || svrModel == SONCA_SMARTK_KM4){
									if(codeVersion >= 25){
										
										int intMidiShiftTime = 0;
										try {
											String strBinaryMidiShiftTime = strBit.substring(23, 31); 
//											Log.e("", "strBinaryMidiShiftTime = " + strBinaryMidiShiftTime);
											intMidiShiftTime = Integer.parseInt(strBinaryMidiShiftTime, 2);	
										} catch (Exception e) {
											
										}										
										
										status.setMidiShiftTime(intMidiShiftTime);	
//										Log.e("", "midiShiftTime = " + intMidiShiftTime);
//										Log.e("", "model A = " + (((settings2 >> 15) & 0x01) == 1));
										status.setModelA(((settings2 >> 15) & 0x01) == 1);
									}
									
									if(codeVersion >= 31 && svrModel == SONCA_SK9xxx){
										status.setDownloadingYouTube(((settings2 >> 16) & 0x01) == 1);
										status.setAutoDownloadYouTube(((settings2 >> 17) & 0x01) == 1);
									}
									
								} else {
									int intMidiShiftTime = 0;
									try {
										String strBinaryMidiShiftTime = strBit.substring(23, 31); 
//										Log.e("", "strBinaryMidiShiftTime = " + strBinaryMidiShiftTime);
										intMidiShiftTime = Integer.parseInt(strBinaryMidiShiftTime, 2);	
									} catch (Exception e) {
										
									}										
									
									status.setMidiShiftTime(intMidiShiftTime);	
//									Log.e("", "midiShiftTime = " + intMidiShiftTime);
									
								}								
							}
							
							if(listIDOffset > 20){
								int settings3 = ByteUtils.byteToInt32L(data, 20);
								
								//Log.e("settings3" , settings3 + "");
								status.setOnOffControlFullValue(settings3);
								status.setOnOffControlFullAPI(true);
							} else {
								status.setOnOffControlFullValue(-1);
								status.setOnOffControlFullAPI(false);
							}
							
							if(listIDOffset > 24){ // settings4
								status.setVolset_default(ByteUtils.byteToInt8(data, 24));
								status.setVolset_offsetMidi(ByteUtils.byteToInt8(data, 25) - 16);
								status.setVolset_offsetKTV(ByteUtils.byteToInt8(data, 26) - 16);
								status.setVolset_dance(ByteUtils.byteToInt8(data, 27));
								status.setVolset_piano(ByteUtils.byteToInt8(data, 28));
								status.setVolset_melody(ByteUtils.byteToInt8(data, 29));
								status.setVolset_offsetKey(ByteUtils.byteToInt8(data, 30) - 12);
								status.setPlayingMode(ByteUtils.byteToInt8(data, 31));
							}	
							
							if(codeVersion >= 31 && svrModel == SONCA_SK9xxx){
								int versionListHidden = 0;
								try {
									versionListHidden = ByteUtils.byteToInt32L(data, 32);
								} catch (Exception e) {
									
								}
								status.setVersionListHidden(versionListHidden);
								
								int versionListDownload = 0;
								try {
									versionListDownload = ByteUtils.byteToInt8(data, 36);
								} catch (Exception e) {
									
								}
								status.setVersionListDownload(versionListDownload);
								
							}
							
							if(codeVersion >= 35 && svrModel == SONCA_SK9xxx){
								byte[] versionListDownloadByte = new byte[32];
								try {									
									versionListDownloadByte = Arrays.copyOfRange(data, 37, 37 + 32);
								} catch (Exception e) {
									
								}
								status.setVersionListDownloadByte(versionListDownloadByte);
							}
							
							int realYoutubeCount = 0;
							int realYoutubeOffset = 0;
							int countAddFile = 0;
							int luckyRollValue = 0;
							
							if(svrModel == SONCA_SMARTK || svrModel == SONCA_SMARTK_801 || svrModel == SONCA_SMARTK_CB || svrModel == SONCA_SMARTK_KM4
									 || svrModel == SONCA_SMARTK_9108_SYSTEM || svrModel == SONCA_SMARTK_KM4_SYSTEM){
								if(listIDOffset > 32){ 
									// Song_Youtube
									status.setPlayingYouTube(ByteUtils.byteToInt8(data, 32) == 1);	
									status.setDownloadingYouTube(ByteUtils.byteToInt8(data, 33) == 1);
									status.setYoutubeDownloadID(ByteUtils.byteToInt24(data, 34));
									status.setYoutubeDownloadPercent(ByteUtils.byteToInt8(data, 37));
									
									// Playing State
									status.setPlayingState(ByteUtils.byteToInt8(data, 38));
									
									realYoutubeCount = ByteUtils.byteToInt8(data, 39);
									realYoutubeOffset = ByteUtils.byteToInt32(data, 40); 
									countAddFile = ByteUtils.byteToInt8(data, 44);
									status.setRealYoutubeCount(realYoutubeCount);
									status.setCountAddFile(countAddFile);
									
									try {
										luckyRollValue = ByteUtils.byteToInt8(data, 45);
									} catch (Exception e) {
										
									}
									if(luckyRollValue > 0){
										status.setFlagSupportLuckyRoll(true);
										if(luckyRollValue > 100){
											status.setPlayingLucky(true);
											status.setCurrentLucky(luckyRollValue % 100);
										} else {
											status.setPlayingLucky(false);
											status.setCurrentLucky(luckyRollValue);
										}
										
										if(status.getCurrentLucky() > 12){
											status.setFlagSupportLuckyRoll(false);
											status.setCurrentLucky(0);
										}
									} else {
										status.setFlagSupportLuckyRoll(false);
									}
								}							
								
								if(realYoutubeCount > 0){
									ArrayList<RealYouTubeInfo> listRealYoutube = new ArrayList<RealYouTubeInfo>();
									for (int i = 0; i < realYoutubeCount; i++) {
										int startPos = realYoutubeOffset + i * 44;
										
										int mID = ByteUtils.byteToInt24(data, startPos); 
										startPos+=3;
										
										byte[] bVidID = Arrays.copyOfRange(data, startPos, startPos + 11);
										String strVidID = new String(bVidID);
										int byteCount = 0; 
										for(int j = 0; j < bVidID.length; j++)
										{
											if(bVidID[j] == 0) {
												break; 
											}
											byteCount++; 
										}
										if(byteCount > 0) {
											strVidID = new String(bVidID, 0, byteCount);
										}									
										
										
										startPos+=11;
										byte[] bName = Arrays.copyOfRange(data, startPos, startPos+30);
										String strName = new String(bName);
										byteCount = 0; 
										for(int j = 0; j < bName.length; j++)
										{
											if(bName[j] == 0) {
												break; 
											}
											byteCount++; 
										}
										if(byteCount > 0) {
											strName = new String(bName, 0, byteCount);
										}	
										
										RealYouTubeInfo tempInfo = new RealYouTubeInfo(mID, strVidID, strName);
										listRealYoutube.add(tempInfo);
									}
									status.setRealYoutubeList(listRealYoutube);
								}
							}
							
			//				Log.e("", "playlistOffset: " + listIDOffset + " song count: " + songCount); 
							
		    //				int[] listIDs = new int[songCount]; 
							
							ArrayList<SongID> listIDs = new ArrayList<SongID>(); 
							for(int i = 0; i < songCount; i++)
							{
								SongID idItem = new SongID(); 
								idItem.typeABC = data[listIDOffset + i * 4]; 
								idItem.songID = ByteUtils.byteToInt24(data, (listIDOffset + i * 4 + 1)); 
		//						Log.e("", "song id: " + idItem.songID); 
								listIDs.add(idItem); 
							}
							status.setReservedSongCount(songCount);
							status.setReservedSongs(listIDs);	
							status.setPlayingSongTypeABC(data[8]);
							status.setPlayingSongID(ByteUtils.byteToInt24(data, 9));					
							int songNameOffset = ByteUtils.byteToInt32(data, 12); 
							int byteCount = 0; 
							int idxAfterSongName = 0;
							for(int i = songNameOffset; i < data.length; i++)
							{
								if(data[i] == 0) {
									idxAfterSongName = i;
									break; 
								}
								byteCount++; 
							}
							if(byteCount > 0) {
								status.setPlayingSongName(new String(data, songNameOffset, byteCount));
							}
							
							try {								
								if(codeVersion >= 31 && svrModel == SONCA_SK9xxx){
									if(status.isDownloadingYouTube()){
										int startIDX = idxAfterSongName + 1;
										int SK90xx_downloadID = ByteUtils.byteToInt24(data, startIDX);
										status.setYoutubeDownloadID(SK90xx_downloadID);
										int SK90xx_downloadPercent = ByteUtils.byteToInt8(data, startIDX + 3);
										status.setYoutubeDownloadPercent(SK90xx_downloadPercent);
										int SK90xx_downloadRemain = ByteUtils.byteToInt16L(data, startIDX + 4);
										status.setYoutubeDownloadRemain(SK90xx_downloadRemain);
									}
								}
								
							} catch (Exception e) {
								e.printStackTrace();
							}
							
							mReceiver.deviceDidReceiveSyncStatus(true, status);
					} catch (Exception ex){
						ex.printStackTrace();
						Log.e("NetworkSocket", "Sync : ERROR DATA");
					}
				}
			}
			else{
				sendEvent(mReceiver, SocketStatus.ERROR_SOCKET_BUSY, "");
			}
		}
	}
	
	private class AsynTaskPlaylistSortHandle extends AsyncTask<Void, Void, Boolean> {
		private Receiver receiver = null;
		private int cmd; 
		private ArrayList<SongID> idList; 
		
		private boolean isSocketBusy = false;
		
		public AsynTaskPlaylistSortHandle(Receiver receiver, int cmd, ArrayList<SongID>idList) {
			this.receiver = receiver;
			this.cmd = cmd; 
			this.idList = idList;
		}

		@Override
		protected Boolean doInBackground(Void ...params) {
			if (clientSocket == null || !clientSocket.isConnected() || authenID == 0) {
//				Log.e(TAB, "SOCKET IS NULL");
				return false;
			}
			
			isSocketBusy = false;
			
			//Log.e("      ", "       ");
			//Log.e(" " , "START PLAYLIST");
			byte[]  dataSent = new byte[5 + 4*idList.size()]; 
			ByteUtils.intToBytes(dataSent, 0, authenID);
			dataSent[4] = (byte)(cmd & 0xFF); 
			
			int pos = 5; 
			for(int i = 0; i < idList.size(); i++) {
				dataSent[pos++] = (byte)idList.get(i).typeABC; 
				ByteUtils.int24ToBytes(dataSent, pos, idList.get(i).songID);
				pos+=3; 
			}
			
			byte[] sentPackage = prepareSendFull(CMD_SYNC_PLAYLIST, dataSent);
			
//			for(int i = 0; i < sentPackage.length; i++)
//			{
//				Log.d("",String.format("%02X", sentPackage[i])); 
//			}
			
			byte[] receivePackage = new byte[1500];
			
			InputStream socketStream = null;
			
			int resultProcess = processSocket_SendReceive(receiver,
					"CMD_SYNC_PLAYLIST_SORT", clientSocket, 
					sentPackage, receivePackage, socketStream,
					SocketStatus.ERROR_FROM_SERVER, SocketStatus.ERROR_DISCONNECTED);
			
			if (resultProcess != 999) {
				if(resultProcess == SocketStatus.ERROR_DISCONNECTED){
					return false;	
				} else if(resultProcess == SocketStatus.ERROR_SOCKET_BUSY){
					isSocketBusy = true;
					return false;
				}				
			}
			
			ServerPackage response = new ServerPackage();
			receivePackage = processResponseData(receivePackage);
			
			response.parseServerPackage(receivePackage);
			
			if(response.getCommand() != CMD_SYNC_PLAYLIST){
				isSocketBusy = true;
				return false;
			}
			
			if (response.getStatus() != CMD_SUCCESS) {
				isSocketBusy = true;
				return false;
			}else{
				byte[] data = response.getData();
				if(data.length%4 == 0){
					int songCount = data.length/4;
					list = new int[songCount]; 
					for(int i = 0; i < songCount; i++){
						list[i] = ByteUtils.byteToInt32(data, (i * 4)); 
					}
//					Log.e(TAB , "PLAYLIST SUCCESSFUL");
				}else{
					isSocketBusy = true;
					return false;
				}
			}
//			Log.e("          ", "             ");
			return true;
		}

		private int[] list = null;
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result){
				if(receiver != null && list != null){
					receiver.deviceUpdatePlayList(list);
				}
			}
			else{
				if(isSocketBusy){
					sendEvent(receiver, SocketStatus.ERROR_SOCKET_BUSY, "");
				} else {
					sendEvent(receiver, SocketStatus.ERROR_DISCONNECTED, "");	
				}				
			}
		}
	}
	
	private class AsynTaskPlaylistHandle extends AsyncTask<Integer, Void, Boolean> {
		
		private Receiver receiver = null;
		private boolean flagFailSending;
		private boolean isSocketBusy = false;
		
		private String vidID = "";
		private String name = "";
		private int position = 0;
		
		public AsynTaskPlaylistHandle(Receiver receiver, String vidID, String name, int position) {
			this.receiver = receiver;
			this.vidID = vidID;
			this.name = name;
			this.position = position;
		}
		
		public AsynTaskPlaylistHandle(Receiver receiver) {
			this.receiver = receiver;
		}
		
		public boolean getFlagFailSending(){
			return this.flagFailSending;
		}
		
		@Override
		protected Boolean doInBackground(Integer... params) {
			if (clientSocket == null || !clientSocket.isConnected() || authenID == 0) {
				//Log.e(TAB, "SOCKET IS NULL");
				return false;
			}			
			//Log.e("      ", "       ");
			//Log.e(" " , "START AsynTaskPlaylistHandle");
			//Log.e(" ", "flagEncrypt = " + flagEncrypt);
			
			isSocketBusy = false;
			flagFailSending = false;
			
			byte[] sentPackage = null;
			
			int code = params[0];
			if(code == CMD_PLAYLIST_SONG_YT_REAL){
				try {
					byte[] dataSent = new byte[48]; 
					ByteUtils.intToBytes(dataSent, 0, authenID);				
					dataSent[4] = (byte)(params[0] & 0xFF);		
					dataSent[5] = (byte)(params[1] & 0xFF); // 0 - add; 1 - first 
					
					String str = vidID.substring(0, Math.min(vidID.length(), 11));
					byte[] bTemp = str.getBytes();
					byte[] bVidID = new byte[11];	
					if(bTemp.length > 11){
						System.arraycopy(bTemp, 0, bVidID, 0, 11);
					} else {
						bVidID = bTemp;
					}	
					System.arraycopy(bVidID, 0, dataSent, 6, bVidID.length);
					
					int countByte = 0;
					str = name;
					String strResult = "";
					for (int i = 0, n = str.length(); i < n; i++) {
					    char tempC = str.charAt(i);
					    char[] c = (tempC + "").toCharArray();
					    byte[] bytes = Charset.forName("UTF-8").encode(CharBuffer.wrap(c)).array();
					    if(bytes[bytes.length - 1] != 0){
					    	countByte += bytes.length;
					    } else {
					    	countByte += bytes.length - 1;	
					    }					    
					    
					    if(countByte > 30){
					    	break;
					    } else {
					    	strResult += tempC;
					    }
					}
					
					bTemp = strResult.getBytes();
					byte[] bName = new byte[30];
					if(bTemp.length > 30){
						System.arraycopy(bTemp, 0, bName, 0, 30);
					} else {
						System.arraycopy(bTemp, 0, bName, 0, bTemp.length);
					}	
					System.arraycopy(bName, 0, dataSent, 17, bName.length);
					
					dataSent[47] = (byte)(position & 0xFF);		
					
					sentPackage = prepareSendFull(CMD_SYNC_PLAYLIST, dataSent);	
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				byte[] dataSent = new byte[13]; 
				ByteUtils.intToBytes(dataSent, 0, authenID);
				
				dataSent[4] = (byte)(params[0] & 0xFF); 
				
				dataSent[5] = (byte)(params[1] & 0xFF); 
				ByteUtils.int24ToBytes(dataSent, 6, params[2]);
				dataSent[9] = (byte)(params[3] & 0xFF); 
				
				sentPackage = prepareSendFull(CMD_SYNC_PLAYLIST, dataSent);
			}
			
			byte[] receivePackage = new byte[1500];
			InputStream socketStream = null;

			int resultProcess = processSocket_SendReceive(receiver,
					"CMD_SYNC_PLAYLIST", clientSocket, sentPackage,
					receivePackage, socketStream,
					SocketStatus.ERROR_FROM_SERVER,
					SocketStatus.ERROR_DISCONNECTED);

			if (resultProcess != 999) {
				if(resultProcess == SocketStatus.ERROR_DISCONNECTED){
					return false;	
				} else if(resultProcess == SocketStatus.ERROR_SOCKET_BUSY){
					isSocketBusy = true;
					return false;
				}	
			}
			
			ServerPackage response = new ServerPackage();
			receivePackage = processResponseData(receivePackage);
			
			response.parseServerPackage(receivePackage);
								
			if(response.getCommand() != CMD_SYNC_PLAYLIST){
				isSocketBusy = true;
				return false;
			}
			
			if (response.getStatus() != CMD_SUCCESS) {
				flagFailSending = true;
			}else{
				byte[] data = response.getData();
				if(data.length%4 == 0){
					int songCount = data.length/4;
					list = new int[songCount]; 
					for(int i = 0; i < songCount; i++){
						list[i] = ByteUtils.byteToInt32(data, (i * 4)); 
					}
					//Log.e(TAB , "PLAYLIST SUCCESSFUL");
				}else{
					isSocketBusy = true;
					return false;
				}
			}

			return true;
		}

		private int[] list = null;
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result){				
				if(receiver != null){
					if(list != null){
						receiver.deviceUpdatePlayList(list);	
					}					
					if(getFlagFailSending()){
						receiver.deviceInform_FailedAddSong();
					}
				}
			}
			else{
				if(isSocketBusy){
					sendEvent(receiver, SocketStatus.ERROR_SOCKET_BUSY, "");
				} else {
					sendEvent(receiver, SocketStatus.ERROR_DISCONNECTED, "");	
				}
								
			}
		}
	}
	
	private class AsynTaskCommand extends AsyncTask<Integer, Void, Boolean> {
		
		private Receiver receiver = null;
		private int value;
		private int CMD;
		
		private boolean isSocketBusy = false;
		
		public AsynTaskCommand(Receiver receiver , byte CMD , int value) {
			this.receiver = receiver;
			this.value = value;
			this.CMD = CMD;
		}

		@Override
		protected Boolean doInBackground(Integer... params) {
			if (clientSocket == null || !clientSocket.isConnected() || authenID == 0) {
				/*
				Log.e("NetworkSocket", "SOCKET IS NULL");
				Log.d("NetworkSocket", "authenID : " + authenID);
				Log.d("NetworkSocket", "is not Connected : " + (!clientSocket.isConnected()));
				Log.d("NetworkSocket", "clientSocket : " + (clientSocket == null));
				*/
				return false;
			}
			
			isSocketBusy = false;
			
//			Log.e("      ", "       ");
//			Log.e(TAB , "START COMMAND");
			byte[]  dataSent = new byte[6]; 
			ByteUtils.intToBytes(dataSent, 0, authenID);
			dataSent[4] = (byte)CMD; 
			dataSent[5] = ByteUtils.intToByte(value);
			
			byte[] sentPackage = prepareSendFull(CMD_REMOTE_CONTROL, dataSent);
			byte[] receivePackage = new byte[1500];
			InputStream socketStream = null;

			if((byte)CMD == NetworkSocket.REMOTE_CMD_DANCE || (byte)CMD == NetworkSocket.REMOTE_CMD_REPEAT){				
				try {
					socketStream = clientSocket.getInputStream();
					if(socketStream.available() > 0){
						socketStream.read(receivePackage , 0 , receivePackage.length);
					}
					
					clientSocket.getOutputStream().write(sentPackage);
					socketStream = clientSocket.getInputStream();
					int byteRecv = socketStream.read(receivePackage , 0 , receivePackage.length);
					if (byteRecv == -1) {
						isProcessingData = false;
						return true;
					}
				} catch (Exception e) {
					return true;
				}
				
				return true;
			} 
			
			int resultProcess = processSocket_SendReceive(receiver,
					"CMD_REMOTE_CONTROL", clientSocket, sentPackage,
					receivePackage, socketStream,
					SocketStatus.ERROR_FROM_SERVER,
					SocketStatus.ERROR_DISCONNECTED);

			if (resultProcess != 999) {
				if(resultProcess == SocketStatus.ERROR_DISCONNECTED){
					return false;	
				} else if(resultProcess == SocketStatus.ERROR_SOCKET_BUSY){
					isSocketBusy = true;
					return false;
				}	
			}
			
			ServerPackage response = new ServerPackage();
			receivePackage = processResponseData(receivePackage);
			
			response.parseServerPackage(receivePackage);
			
			if(response.getCommand() != CMD_REMOTE_CONTROL){
				isSocketBusy = true;
				return false;
			}
			
			if (response.getStatus() != CMD_SUCCESS) {
				
			}else{
			
			}
			
			return true;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result){
				if(receiver != null){
					receiver.deviceDidCommand(result);
				}
			}
			else{
				if(isSocketBusy){
					sendEvent(receiver, SocketStatus.ERROR_SOCKET_BUSY, "");
				} else {
					sendEvent(receiver, SocketStatus.ERROR_DISCONNECTED, "");	
				}
				
			}
		}
	}
	
	private class AsynTaskCommandKartrol extends AsyncTask<Integer, Void, Boolean> {
		
		private Receiver receiver = null;
		private int value;
		private int CMD;
		private int model;
		private boolean flagEncrypt = false;
		
		private boolean isSocketBusy = false;
		
		public AsynTaskCommandKartrol(Receiver receiver , boolean flagEncrypt, byte CMD , int value, int model) {
			Log.e("AsynTaskCommandKartrol", flagEncrypt + "..........................");
			this.receiver = receiver;
			this.value = value;
			this.CMD = CMD;
			this.model = model;
			this.flagEncrypt = flagEncrypt;
		}

		@Override
		protected Boolean doInBackground(Integer... params) {
			if (clientSocket == null || !clientSocket.isConnected() || authenID == 0) {
				return false;
			}
			
			isSocketBusy = false;
			
			System.out.printf("CMD: %x\n", CMD); 
			byte[]  dataSent = MapRemote.packageIRKey(receiver, CMD, model);
			
			//Log.e("sendCommandKartrol bytes", bytesToHex2(dataSent));

			System.out.print("DATA"); 
			for(int i = 0; i < dataSent.length; i++) {
				System.out.printf("%x-", dataSent[i]); 
			}
			System.out.println(); 
			
			byte[] sentPackage = prepareSendFull(CMD_REMOTE_CONTROL, dataSent);			
			byte[] receivePackage = new byte[1500];
			InputStream socketStream = null;

			int resultProcess = processSocket_SendReceive(receiver,
					"CMD_REMOTE_CONTROL", clientSocket, sentPackage,
					receivePackage, socketStream,
					SocketStatus.ERROR_FROM_SERVER,
					SocketStatus.ERROR_DISCONNECTED);

			if (resultProcess != 999) {
				if(resultProcess == SocketStatus.ERROR_DISCONNECTED){
					return false;	
				} else if(resultProcess == SocketStatus.ERROR_SOCKET_BUSY){
					isSocketBusy = true;
					return false;
				}	
			}
			
			ServerPackage response = new ServerPackage();
			receivePackage = processResponseData(receivePackage);
			
			response.parseServerPackage(receivePackage);
			
			if(response.getCommand() != CMD_REMOTE_CONTROL){
				isSocketBusy = true;
				return false;
			}
			
			if (response.getStatus() != CMD_SUCCESS) {
				 
			}
			
			return true;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if(result){
				if(receiver != null){
					receiver.deviceDidCommand(result);
				}
			}
			else{
				if(isSocketBusy){
					sendEvent(receiver, SocketStatus.ERROR_SOCKET_BUSY, "");
				} else {
					sendEvent(receiver, SocketStatus.ERROR_DISCONNECTED, "");	
				}
				
			}
		}
	}
	
	private class AsynTaskPlaylistKartrolHandle extends AsyncTask<Integer, Void, Boolean> {
		
		private Receiver receiver = null;
		private boolean flagEncrypt = false;
		private boolean loading = false;
		
		private boolean isSocketBusy = false;
		
		public boolean loadCompleted()
		{
			return loading == false; 
		}
		
		public AsynTaskPlaylistKartrolHandle(Receiver receiver, boolean flagEncrypt) {
			this.receiver = receiver;
			this.flagEncrypt = flagEncrypt;
			loading = true; 
		}

		@Override
		protected Boolean doInBackground(Integer... params) {
			try {
				if (clientSocket == null || !clientSocket.isConnected()
						|| authenID == 0) {
					// Log.e(TAB, "SOCKET IS NULL");
					return false;
				}
				
				isSocketBusy = false;
				
				// Log.e("      ", "       ");
				// Log.e(TAB , "START PLAYLIST");

				int playlistCMD = params[0];
				TYPE_ABC typeABC = TYPE_ABC.values()[params[1]];
				int songID = params[2];
				int model = params[4];

				String idStr = String.format("%06d", songID);

				int len = 0;
				for (int i = 0; i < idStr.length(); i++) {
					char ch = idStr.charAt(i);
//					if (ch == '0' && len == 0) {// remove '0' leading
//						continue;
//					} else {
						len++;
						break;
//					}
				}
				if (len == 0) {
					Log.e("", "INVALID CODE NUMBER");
					return false;
				}

				// Append Len
				int irCodeLen = 4;

				ByteArrayBuffer buffer = new ByteArrayBuffer(0);
				buffer.append((byte) irCodeLen);

				// Add clear command
				// byte[] keys =
				// MapRemote.packageIRKey(AppEvent.UI_EVENT_CLEAR.ordinal());
				// buffer.append(keys, 0, keys.length);

				byte[] keys;

				// VIETKTV - display menu input id
				if (model == 5) {
					keys = MapRemote.packageIRKey(receiver,
							RemoteIRCode.IRC_DIGIT_MENU, model);
					buffer.append(keys, 0, keys.length);
				}
				
				// THEM 1 SO 0 - fix play piano
				keys = MapRemote.packageIRKey(receiver,
						RemoteIRCode.IRC_0, model);
				buffer.append(keys, 0, keys.length);

				// Append real remote data
				len = 0;
				int numEventCode[] = { RemoteIRCode.IRC_0, RemoteIRCode.IRC_1,
						RemoteIRCode.IRC_2, RemoteIRCode.IRC_3,
						RemoteIRCode.IRC_4, RemoteIRCode.IRC_5,
						RemoteIRCode.IRC_6, RemoteIRCode.IRC_7,
						RemoteIRCode.IRC_8, RemoteIRCode.IRC_9 };

				for (int i = 0; i < idStr.length(); i++) {
					char ch = idStr.charAt(i);
//					if (ch == '0' && len == 0) {// remove '0' leading
//						continue;
//					}

					keys = MapRemote.packageIRKey(receiver,
							numEventCode[(ch - '0')], model);
					buffer.append(keys, 0, keys.length);
					len++;
				}

				// if (typeABC == TYPE_ABC.SONG_TYPE_A) {
				//
				// }else if(typeABC == TYPE_ABC.SONG_TYPE_B) {
				// keys =
				// MapRemote.packageIRKey(AppEvent.UI_EVENT_RIGHT.ordinal());
				// buffer.append(keys, 0, keys.length);
				// }else if (typeABC == TYPE_ABC.SONG_TYPE_C) {
				// keys =
				// MapRemote.packageIRKey(AppEvent.UI_EVENT_RIGHT.ordinal());
				// buffer.append(keys, 0, keys.length);
				// keys =
				// MapRemote.packageIRKey(AppEvent.UI_EVENT_RIGHT.ordinal());
				// buffer.append(keys, 0, keys.length);
				// }

				if (playlistCMD == CMD_PLAYLIST_ADD_SONG) {
					keys = MapRemote.packageIRKey(receiver,
							RemoteIRCode.IRC_ENTER, model);
					buffer.append(keys, 0, keys.length);
				} else if (playlistCMD == CMD_PLAYLIST_FIRST_SONG) {
					keys = MapRemote.packageIRKey(receiver,
							RemoteIRCode.IRC_1STRES, model);
					buffer.append(keys, 0, keys.length);
				} else {
					//Log.e("", "Invalid command");
					return false;
				}

				byte[] sentPackage = prepareSendFull(CMD_REMOTE_PLAYLIST,
						buffer.toByteArray());

				// for(int i = 0; i < sentPackage.length; i++)
				// {
				// Log.d("",String.format("%02X", sentPackage[i]));
				// }

				byte[] receivePackage = new byte[1500];
				InputStream socketStream = null;

				int resultProcess = processSocket_SendReceive(receiver,
						"CMD_REMOTE_PLAYLIST", clientSocket, sentPackage,
						receivePackage, socketStream,
						SocketStatus.ERROR_FROM_SERVER,
						SocketStatus.ERROR_DISCONNECTED);

				if (resultProcess != 999) {
					if(resultProcess == SocketStatus.ERROR_DISCONNECTED){
						return false;	
					} else if(resultProcess == SocketStatus.ERROR_SOCKET_BUSY){
						isSocketBusy = true;
						return false;
					}	
				}
				
				ServerPackage response = new ServerPackage();
				receivePackage = processResponseData(receivePackage);

				response.parseServerPackage(receivePackage);
				
				if(response.getCommand() != CMD_REMOTE_CONTROL){
					isSocketBusy = true;
					return false;
				}
				
				if (response.getStatus() != CMD_SUCCESS) {
					isSocketBusy = true;
					return false;
				} else {
					byte[] data = response.getData();
					if (data.length % 4 == 0) {
						int songCount = data.length / 4;
						list = new int[songCount];
						for (int i = 0; i < songCount; i++) {
							list[i] = ByteUtils.byteToInt32(data, (i * 4));
						}
						// Log.e(TAB , "PLAYLIST SUCCESSFUL");
					} else {
						isSocketBusy = true;
						return false;
					}
				}
				// Log.e("          ", "             ");

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				loading = false;
			}
			return true;
		}

		private int[] list = null;
		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			loading = false;
			if(result){
				if(receiver != null && list != null){
					receiver.deviceUpdatePlayList(list);
				}
			}
			else{
				if(isSocketBusy){
					sendEvent(receiver, SocketStatus.ERROR_SOCKET_BUSY, "");
				} else {
					sendEvent(receiver, SocketStatus.ERROR_DISCONNECTED, "");	
				}
				
			}
		}
	}
	
	private void sendEvent(Receiver mReceiver, int result, String errMsg) {
		if (mReceiver != null) {
			if (result == SocketStatus.ERROR_DISCONNECTED) {
				mReceiver.deviceDidDisconnected();
			}else if (result == SocketStatus.ERROR_TIMEDOUT) {
				mReceiver.deviceDidTimedout();
			}else if (result == SocketStatus.ERROR_FROM_SERVER) {
				mReceiver.deviceSendRequestDidFailedWithError(errMsg);
			}else if(result == SocketStatus.ERROR_INVALID_RESPONSE) {
				if(errMsg.startsWith("yt_90xx_down_status")
						 ||errMsg.equals("yt_down_full") || errMsg.equals("yt_down_limit") || errMsg.equals("yt_down_exist")
						 || errMsg.equals("toc_modi_noUsb") || errMsg.equals("toc_modi_notInsideUsb") || errMsg.equals("toc_modi_notInsideTOC") 
						 || errMsg.equals("toc_modi_full") || errMsg.equals("toc_modi_busy") || errMsg.equals("toc_modi_limit")
						 || errMsg.equals("lucky_notsupport") || errMsg.equals("lucky_nodata") || errMsg.equals("lucky_errordata")){
					mReceiver.deviceSendRequestDidFailedWithError(errMsg);
				} else {
					errMsg = "invalid response";
					mReceiver.deviceSendRequestDidFailedWithError(errMsg);	
				}				
			} else if(result == SocketStatus.ERROR_SYNC_TIMEOUT) { 
				errMsg = "sync timeout"; 
				mReceiver.deviceSendRequestDidFailedWithError(errMsg);
			} else if(result == SocketStatus.ERROR_SOCKET_READ) { 
				errMsg = "read socket"; 
				mReceiver.deviceSendRequestDidFailedWithError(errMsg);
			} else if(result == SocketStatus.ERROR_SOCKET_BUSY) { 
				errMsg = "busy socket"; 
				mReceiver.deviceSendRequestDidFailedWithError(errMsg);
			} else if(result == SocketStatus.MSG_TIMEOUT_SHOW) { 				
				mReceiver.deviceInform_Timeout_Show(errMsg);
			} else if(result == SocketStatus.MSG_TIMEOUT_CLOSE) { 				
				mReceiver.deviceInform_Timeout_Close();
			} 
		}
	}
	
////////////////////////////////// - Interface - /////////////////////////////////////////////
	public interface NetworkSocketListener
	{
		 
	}
	
	public interface Receiver {
		public void deviceSearchCompleted(ArrayList<SKServer> servers);
		public void deviceDidConnectWithServer(Boolean success, boolean flagFinish, String srvName);
		public void deviceDidAuthenWithServer(ServerPackage response, int srvModel, int codeVersion);
		public void deviceDidDownloadFile(boolean status, String savePath);
		public void deviceDidCommand(boolean success);
		public void deviceDidReceiveSyncStatus(boolean success, ServerStatus status); 
		public void deviceUpdatePlayList(int[] list);
		public void deviceDidConnected(ServerSocket socket); 
		public void deviceDidDisconnected(); 
		public void deviceDidTimedout(); 
		public void deviceDidReceiveHeader(int header); 
		public void deviceDidReceiveDataOfLength(int loaded, int total); 
		public void deviceSendRequestDidFailedWithError(String errMsg);
		public void deviceSendAdminPass(String adminpass);
		public void deviceSendDeviceUser(String isUserDevice);
		public void deviceSendConfigWifi(String configInfo);
		public void deviceSendHIW_FirmareInfo(String resultString, String daumay_name, String daumay_version, 
				String wifi_version, int wifi_revision);
		public void deviceSendHIW_FirmareConfig(String resultString, int mode, String stationID, String stationPass, 
				String apID, String apPass, String passConnect, String passAdmin);
		public void deviceSendLyricInfo(String resultString);
		public void deviceSendScoreInfo(List<Integer> listScore);
		public void deviceSendLyricVidLink(String resultString);
		public void deviceInform_FailedAddSong(); 
		
		public void deviceSendSoundData(String resultString);
		public void deviceSendSoundDataByte(byte[] resultByte);
		
		public void deviceInform_Timeout_Show(String commandName);
		public void deviceInform_Timeout_Close();
		public void deviceInform_ShowMessage(String msgString);
		
		public void deviceSendUSBStorageList(String listResult);
		public void deviceSendSongFromUSB(String resultString);
		
		public void deviceSendModifyTOCResult(String listResult);
		public void echoDeviceSendNRPNCheckCRC( byte[] bs,byte[] Result);
		public void echoDeviceSendEspRequestConfig( byte[] Result);
		public void echoDeviceespSendNRPN( Boolean Result,int nrpn, int value);
		
	}
	
	final char[] hexArray = "0123456789ABCDEF".toCharArray();
	public String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}

	public class SocketStatus {
		public final static int SUCCESS = 0;  
		public final static int ERROR_DISCONNECTED = 1;  
		public final static int ERROR_TIMEDOUT = 2;  
		public final static int ERROR_INVALID_RESPONSE = 3; 
		public static final int ERROR_WRONG_PASSWORD = 4; 
		public static final int ERROR_FROM_SERVER = 5; 
		public final static int ERROR_CONNECT_EXISTED = 6;  
		public final static int ERROR_SYNC_TIMEOUT = 7;
		public final static int ERROR_SOCKET_READ = 8;
		public final static int ERROR_SOCKET_BUSY = 9; 
		
		public final static int MSG_TIMEOUT_SHOW = 20;
		public final static int MSG_TIMEOUT_CLOSE = 21;
		
		public final static int CONNECTED_TOHOST = 99; 
		public final static int AUTHENTICATED_TOHOST = 100; 
		
		
	}
	
	private byte[] sha256_8203rEncryptDecrypt(byte[] random, byte[] data){
		byte[] sha256 = new byte[32];
		byte[] resultData = new byte[data.length];
		try {
			int[] newData = new int[data.length];
			for (int i = 0; i < data.length; i++) {
				newData[i] = (int)data[i];
			}
			
			int secretKey = 159753654;
			byte[] secretKey_Byte = ByteBuffer.allocate(4)
					.putInt(secretKey).array();

			// revert key
			byte[] newKeyData = new byte[4];
			int count = secretKey_Byte.length - 1;
			for (int i = 0; i < secretKey_Byte.length; i++) {
				newKeyData[count] = secretKey_Byte[i];
				count--;
			}

			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(DISK_KEYS.getBytes());
			md.update(newKeyData);
			md.update(random);
			sha256 = md.digest();
			
			int k, tmp;
			
			for (int i = 0; i < newData.length; i++) {
				k = i % 32;
				newData[i] = newData[i] ^ sha256[k];
			}			
			
			for (int i = 0; i < newData.length; i++) {
				resultData[i] = (byte)newData[i];
			}
		} catch (Exception e) {
			return null;
		}
		return resultData;
	}
	
	private byte[] sha256_encryptData(byte[] random, byte[] data){
		byte[] sha256 = new byte[32];
		byte[] resultData = new byte[data.length];
		try {
			int[] newData = new int[data.length];
			for (int i = 0; i < data.length; i++) {
				newData[i] = (int)data[i];
			}
			
			int secretKey = 971348625;
			byte[] secretKey_Byte = ByteBuffer.allocate(4)
					.putInt(secretKey).array();

			// revert key
			byte[] newKeyData = new byte[4];
			int count = secretKey_Byte.length - 1;
			for (int i = 0; i < secretKey_Byte.length; i++) {
				newKeyData[count] = secretKey_Byte[i];
				count--;
			}

			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(DISK_KEYS.getBytes());
			md.update(newKeyData);
			md.update(random);
			sha256 = md.digest();
			
			int k, tmp;
			
			for (int i = 0; i < newData.length; i++) {
				k = i % 32;
				newData[i] = ~newData[i];
				tmp = newData[i] & 0xf8;// 11111000
				tmp = tmp >> 3;
				newData[i] = newData[i] << 5;
				newData[i] = newData[i] | tmp;
				newData[i] = newData[i] ^ sha256[k];
			}			
			
			for (int i = 0; i < newData.length; i++) {
				resultData[i] = (byte)newData[i];
			}
		} catch (Exception e) {
			return null;
		}
		return resultData;
	}
	
	private byte[] sha256_decryptData(byte[] random, byte[] data){
		byte[] sha256 = new byte[32];
		byte[] resultData = new byte[data.length];
		try {
			int[] newData = new int[data.length];
			for (int i = 0; i < data.length; i++) {
				newData[i] = (int)data[i];
			}
			int secretKey = 159753654;
			byte[] secretKey_Byte = ByteBuffer.allocate(4)
					.putInt(secretKey).array();

			// revert key
			byte[] newKeyData = new byte[4];
			int count = secretKey_Byte.length - 1;
			for (int i = 0; i < secretKey_Byte.length; i++) {
				newKeyData[count] = secretKey_Byte[i];
				count--;
			}

			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(DISK_KEYS.getBytes());
			md.update(newKeyData);
			md.update(random);
			sha256 = md.digest();
			
			int k, tmp;
			
			for (int i = 0; i < newData.length; i++) {
				k = i % 32;
				newData[i] = newData[i] ^ sha256[k];
				tmp = newData[i] & 0xe0;// 11100000
				tmp = tmp >> 5;
				newData[i] = newData[i] << 3;
				newData[i] = newData[i] | tmp;
				newData[i] = ~newData[i];
			}
			
			for (int i = 0; i < newData.length; i++) {
				resultData[i] = (byte)newData[i];
			}
		} catch (Exception e) {
			return null;
		}
		return resultData;
	}
	
	private boolean isProcessingData = false;
	public int processSocket_SendReceive(Receiver mReceiver, String commandName, Socket mySocket, byte[] sentPackage, byte[] receivePackage, InputStream socketStream, int byteReceiveStatus, int catchExceptionStatus){
		if(isProcessingData){
			Log.d("processSocket_SendReceive", "isProcessingData");
			return SocketStatus.ERROR_SOCKET_BUSY;
		}
		
		isProcessingData = true;
		
//		Log.e(" ", " ");
//		Log.e(" ", "Start Command: " + commandName);
		try {
			socketStream = mySocket.getInputStream();
			if(socketStream.available() > 0){
				Log.d(" ", "CLEAR BUFFER 0- " + commandName);
				socketStream.read(receivePackage , 0 , receivePackage.length);
			}
			
			mySocket.getOutputStream().write(sentPackage);
			socketStream = mySocket.getInputStream();
			int byteRecv = socketStream.read(receivePackage , 0 , receivePackage.length);
			if (byteRecv == -1) {
				isProcessingData = false;
				return byteReceiveStatus; 
			}
		} catch (Exception e) {
			Log.d(" ", "TIMEOUT 1 TIME - " + commandName);
			try {
				sendEvent(mReceiver, SocketStatus.MSG_TIMEOUT_SHOW, commandName);
				mySocket.getOutputStream().write(sentPackage);
				socketStream = mySocket.getInputStream();
				int byteRecv = socketStream.read(receivePackage , 0 , receivePackage.length);
				if (byteRecv == -1) {
					isProcessingData = false;
					return byteReceiveStatus;				 
				}
			} catch (Exception e2) {
				try {
					Log.d(" ", "TIMEOUT 2 TIME - " + commandName);
					mySocket.getOutputStream().write(sentPackage);
					socketStream = mySocket.getInputStream();
					int byteRecv = socketStream.read(receivePackage , 0 , receivePackage.length);
					if (byteRecv == -1) {
						isProcessingData = false;
						return byteReceiveStatus; 
					}
				} catch (Exception e3) {
					try {
						Log.d(" ", "TIMEOUT 3 TIME - " + commandName);
						mySocket.getOutputStream().write(sentPackage);
						socketStream = mySocket.getInputStream();
						int byteRecv = socketStream.read(receivePackage , 0 , receivePackage.length);
						if (byteRecv == -1) {
							isProcessingData = false;
							return byteReceiveStatus; 
						}
					} catch (Exception e4) {
						try {
							Log.d(" ", "TIMEOUT 4 TIME - " + commandName);
							mySocket.getOutputStream().write(sentPackage);
							socketStream = mySocket.getInputStream();
							int byteRecv = socketStream.read(receivePackage , 0 , receivePackage.length);
							if (byteRecv == -1) {
								isProcessingData = false;
								return byteReceiveStatus; 
							}
						} catch (Exception e5) {
							e5.printStackTrace();
							if (clientSocket != null) {
								try {
									isProcessingData = false;
									clientSocket.close();
									clientSocket = null; 
//									Log.e(TAB, "disconnectFromRemoteHost()");
								} catch (IOException e6) {
									e6.printStackTrace();
								}
							}
							return catchExceptionStatus;
						}
					}
					
				}							
			}	
		}

		isProcessingData = false;
//		Log.e(" ", "End Command: " + commandName);
		return 999; // ok
	}
	
	
	private final static int SOL_TCP = 6;

	private final static int TCP_KEEPIDLE = 4;
	private final static int TCP_KEEPINTVL = 5;
	private final static int TCP_KEEPCNT = 6;
	
	private void setKeepaliveSocketOptions(Socket socket, int idleTimeout, int interval, int count) {
		  try {
			 Log.e("setKeepaliveSocketOptions", "setKeepaliveSocketOptions");
			 Log.e("setKeepaliveSocketOptions", "setKeepaliveSocketOptions");
			 Log.e("setKeepaliveSocketOptions", "setKeepaliveSocketOptions");
			 Log.e("setKeepaliveSocketOptions", "setKeepaliveSocketOptions");
		    socket.setKeepAlive(true);
		    try {
		      Field socketImplField = Class.forName("java.net.Socket").getDeclaredField("impl");
		      socketImplField.setAccessible(true);
		      if(socketImplField != null) {
		        Object plainSocketImpl = socketImplField.get(socket);
		        Field fileDescriptorField = Class.forName("java.net.SocketImpl").getDeclaredField("fd");
		        if(fileDescriptorField != null) {
		          fileDescriptorField.setAccessible(true);
		          FileDescriptor fileDescriptor = (FileDescriptor)fileDescriptorField.get(plainSocketImpl);
		          Class libCoreClass = Class.forName("libcore.io.Libcore");
		          Field osField = libCoreClass.getDeclaredField("os");
		          osField.setAccessible(true);
		          Object libcoreOs = osField.get(libCoreClass);
		          Method setSocketOptsMethod = Class.forName("libcore.io.ForwardingOs").getDeclaredMethod("setsockoptInt", FileDescriptor.class, int.class, int.class, int.class);
		          if(setSocketOptsMethod != null) {
		            setSocketOptsMethod.invoke(libcoreOs, fileDescriptor, SOL_TCP, TCP_KEEPIDLE, idleTimeout);
		            setSocketOptsMethod.invoke(libcoreOs, fileDescriptor, SOL_TCP, TCP_KEEPINTVL, interval);
		            setSocketOptsMethod.invoke(libcoreOs, fileDescriptor, SOL_TCP, TCP_KEEPCNT, count);
		          }
		        }
		      }
		    }
		    catch (Exception reflectionException) {
		    	reflectionException.printStackTrace();
		    }
		  } catch (Exception e) {
			  e.printStackTrace();
		  }
		}
	
	private AsynTaskNRPNCheckCRC NRPNCheckCRCTask = null;
	public void setNRPNCheckCRC(Receiver receiver, byte[] bData) {
		if(NRPNCheckCRCTask != null){
			NRPNCheckCRCTask.cancel(true);
			NRPNCheckCRCTask = null;
		}		
		NRPNCheckCRCTask = new AsynTaskNRPNCheckCRC(receiver);
		NRPNCheckCRCTask.setData(bData);
		NRPNCheckCRCTask.execute("0");
	}
	private class AsynTaskNRPNCheckCRC extends AsyncTask<String, Integer, Integer> {
		private Receiver mReceiver = null;
		private String errMsg = "";
		private byte[] responseData= null;
		private boolean loading = false; 
		private ServerPackage response = null;
		
		public boolean loadCompleted()
		{
			return loading == false; 
		}
		
		private byte[] bData;
		
		public void setData(byte[] bData){
			this.bData = bData;
		}
		
		public byte[] getResultData()
		{
			return responseData; 
		}
		
		public AsynTaskNRPNCheckCRC(Receiver receiver) {
			response = new ServerPackage();
			mReceiver = receiver;
			loading = true; 
		}

		@Override
		protected Integer doInBackground(String... params) {
			try {
				// Log.d("", "AsynTaskNRPNCheckCRC");
				if (clientSocket == null || !clientSocket.isConnected() || authenID == 0) {
					// Log.e("", "SOCKET IS NULL");
					return SocketStatus.ERROR_DISCONNECTED;
				}
				
				byte[] authenArr = new byte[4]; 
				ByteUtils.intToBytes(authenArr, 0, authenID);					
				if(bData == null || bData.length == 0){
					Log.e("", "Data gui di empty");
					return SocketStatus.ERROR_FROM_SERVER;
				}
				byte[] sentData = new byte[authenArr.length + bData.length];
				
				System.arraycopy(authenArr, 0, sentData, 0, authenArr.length);
				System.arraycopy(bData, 0, sentData, authenArr.length, bData.length);
				byte[] sentPackage = prepareSendFull(CMD_REQ_MIXER_CHECK_CRC, sentData);				
				// Log.e("sentPackage", bytesToHex2(sentPackage));
				
				byte[] receivePackage = new byte[1500];
				InputStream socketStream = null;
				
				int resultProcess = processSocket_SendReceive(mReceiver,
						"CMD_REQ_MIXER_CHECK_CRC", clientSocket, 
						sentPackage, receivePackage, socketStream,
						SocketStatus.ERROR_FROM_SERVER, SocketStatus.ERROR_DISCONNECTED);
				if (resultProcess != 999) {
					return resultProcess;
				}	
				 
				receivePackage = processResponseData(receivePackage);
				response.parseServerPackage(receivePackage);			
				//Log.d("", "=AsynTaskNRPNCheckCRC=="+response.getStatus()+"=="+response.getCommand());
				if(response.getCommand() != CMD_REQ_MIXER_CHECK_CRC){
					return SocketStatus.ERROR_SOCKET_BUSY;
				}	
				
				if (response.getStatus() != CMD_SUCCESS) {
					// Log.e("response data", "response.getStatus() != CMD_SUCCESS");	
					return SocketStatus.SUCCESS;
				} 				
				
				responseData = response.getData(); 
								
				return SocketStatus.SUCCESS;
				
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				loading = false; 
			}
			return SocketStatus.ERROR_DISCONNECTED;
		}

		@Override
		protected void onPostExecute(Integer result) {
			loading = false; 
			if(result == SocketStatus.SUCCESS){
				if(mReceiver != null){
					mReceiver.echoDeviceSendNRPNCheckCRC(bData,getResultData());
				}
			}else{
				sendEvent(mReceiver, result, errMsg);
				if(result == SocketStatus.ERROR_SOCKET_READ){
					if(mReceiver != null){
						mReceiver.echoDeviceSendNRPNCheckCRC(bData,getResultData());
					}	
				}				
			}
		}
	}
	private int ESP_DWN_FIRST_PACKET_SIZE = 11;
	private AsynTaskEspRequestConfig EspRequestConfigTask = null;
	public void setEspRequestConfig(Receiver receiver) {		
		if(EspRequestConfigTask != null){
			EspRequestConfigTask.cancel(true);
			EspRequestConfigTask = null;
		}		
		EspRequestConfigTask = new AsynTaskEspRequestConfig(receiver);
		EspRequestConfigTask.execute("0");
	}
	private class AsynTaskEspRequestConfig extends AsyncTask<String, Integer, Integer> {
		private Receiver mReceiver = null;
		private String errMsg = "";
		private byte[] responseData= null;
		private boolean loading = false; 
		private ServerPackage response = null;
		
		public boolean loadCompleted()
		{
			return loading == false; 
		}		
		
		public byte[] getResultData()
		{
			return responseData; 
		}
		
		public AsynTaskEspRequestConfig(Receiver receiver) {
			response = new ServerPackage();
			mReceiver = receiver;
			loading = true; 
		}

		@Override
		protected Integer doInBackground(String... params) {
			try {
				 Log.e("", "AsynTaskEspRequestConfig");
				if (clientSocket == null || !clientSocket.isConnected() || authenID == 0) {
					// Log.e("", "SOCKET IS NULL");
					return SocketStatus.ERROR_DISCONNECTED;
				}
				
				byte[] authenArr = new byte[4]; 
				ByteUtils.intToBytes(authenArr, 0, authenID);					
				byte[] sentPackage = prepareSendFull(CMD_REQ_MIXER_ASK_CONFIG, authenArr);				
				// Log.e("sentPackage", bytesToHex2(sentPackage));
				
				byte[] receivePackage = new byte[11];				
				InputStream socketStream = clientSocket.getInputStream();				
				clientSocket.getOutputStream().write(sentPackage);
				Log.e("", "receivePackage1="+socketStream.available());
				socketStream.read(receivePackage , 0 , receivePackage.length);
				response.parseServerPackage(receivePackage);			
				//Log.d("", "=CMD_REQ_MIXER_ASK_CONFIG=="+response.getStatus()+"=="+response.getCommand()
				//+"=="+response.getData().length);
				if(response.getCommand() != CMD_REQ_MIXER_ASK_CONFIG){
					return SocketStatus.ERROR_SOCKET_BUSY;
				}	
				
				if (response.getStatus() != CMD_SUCCESS) {
					// Log.e("response data", "response.getStatus() != CMD_SUCCESS");	
					return SocketStatus.SUCCESS;
				} 				
				int numberOfBytesRead =ByteUtils.byteToInt32(response.getData(),0); 
				Log.e("", "receivePackage="+bytesToHex2(receivePackage)+"=="+socketStream.available()+"=numberOfBytesRead="+numberOfBytesRead);				
				responseData=new byte[numberOfBytesRead];				
				int start=0;
				do
				{
					int length=socketStream.available();
					
					if(length>0){
						Log.e("", "receivePackage start="+start+"=length="+length);
						socketStream.read(responseData , start , length);
						start+=length;
					}
					
				}while(start<numberOfBytesRead);
				/*InputStream socketStream = null;				
				int resultProcess = processSocket_SendReceive(mReceiver,
						"CMD_REQ_MIXER_ASK_CONFIG", clientSocket, 
						sentPackage, receivePackage, socketStream,
						SocketStatus.ERROR_FROM_SERVER, SocketStatus.ERROR_DISCONNECTED);
				if (resultProcess != 999) {
					return resultProcess;
				}	
				
				receivePackage = processResponseData(receivePackage);
				response.parseServerPackage(receivePackage);			
				Log.d("", "=CMD_REQ_MIXER_ASK_CONFIG=="+response.getStatus()+"=="+response.getCommand()
				+"=="+response.getData().length);
				if(response.getCommand() != CMD_REQ_MIXER_ASK_CONFIG){
					return SocketStatus.ERROR_SOCKET_BUSY;
				}	
				
				if (response.getStatus() != CMD_SUCCESS) {
					// Log.e("response data", "response.getStatus() != CMD_SUCCESS");	
					return SocketStatus.SUCCESS;
				} 				
				int numberOfBytesRead =ByteUtils.byteToInt32(response.getData(),0); 
				Log.d("", "=CMD_REQ_MIXER_ASK_CONFIG=length="+numberOfBytesRead);
				if (numberOfBytesRead >= ESP_DWN_FIRST_PACKET_SIZE)
				{
					responseData=new byte[numberOfBytesRead];
					sentPackage = new byte[0];
					clientSocket.getOutputStream().write(sentPackage);
					int i=0,j=0;
					int tam=0;
				
					socketStream = clientSocket.getInputStream();
					do{
						numberOfBytesRead =numberOfBytesRead-i;
						receivePackage = new byte[numberOfBytesRead];
						int byteRecv = socketStream.read(receivePackage , 0 , receivePackage.length);
						for(i=0;i<numberOfBytesRead;i+=4){
							try{
								if(receivePackage[i]==0&& receivePackage[i+1]==0){
									break;
								}
							}catch(Exception e){
								break;
							}
						}
						//Log.d("","=CMD_REQ_MIXER_ASK_CONFIG==i=="+i+"=j="+j);
						
						System.arraycopy(receivePackage, 0, responseData, j, i);
						j=i;
						
					}while(i<numberOfBytesRead);
					
				}*/
				//for(int k=0;k<responseData.length;k++)
				//	Log.e("","=k="+k+"=value="+Integer.toHexString(responseData[k]));
				return SocketStatus.SUCCESS;
				
			}catch (Exception e) {
				Log.e("","here2");
				e.printStackTrace();
			} finally {
				loading = false; 
			}
			return SocketStatus.ERROR_DISCONNECTED;
		}

		@Override
		protected void onPostExecute(Integer result) {
			loading = false; 
			if(result == SocketStatus.SUCCESS){
				if(mReceiver != null){
					mReceiver.echoDeviceSendEspRequestConfig(getResultData());
				}
			}/*else{
				sendEvent(mReceiver, result, errMsg);
				if(result == SocketStatus.ERROR_SOCKET_READ){
					if(mReceiver != null){
						mReceiver.deviceSendEspRequestConfig(getResultData());
					}	
				}				
			}*/
		}
	}
	
	private AsynTaskespSendNRPN espSendNRPNTask = null;
	public void espSendNRPN(Receiver receiver, int nrpn, int value) {		
		if(espSendNRPNTask != null){
			espSendNRPNTask.cancel(true);
			espSendNRPNTask = null;
		}		
		espSendNRPNTask = new AsynTaskespSendNRPN(receiver);
		espSendNRPNTask.setData(nrpn,value);
		espSendNRPNTask.execute("0");
	}
	private class AsynTaskespSendNRPN extends AsyncTask<String, Integer, Integer> {
		private Receiver mReceiver = null;
		private String errMsg = "";
		private boolean iSuccess= false;
		private boolean loading = false; 
		private ServerPackage response = null;
		private int nrpn=0;
		private int value=0;
		public boolean loadCompleted()
		{
			return loading == false; 
		}
				
		public void setData(int nrpn, int value){
			this.nrpn=nrpn;
			this.value=value;
		}
		
		public boolean getResultData()
		{
			return iSuccess; 
		}
		
		public AsynTaskespSendNRPN(Receiver receiver) {
			response = new ServerPackage();
			mReceiver = receiver;
			loading = true; 
		}

		@Override
		protected Integer doInBackground(String... params) {
			try {
				// Log.e("", "NRPNCheckCRC");
				if (clientSocket == null || !clientSocket.isConnected() || authenID == 0) {
					// Log.e("", "SOCKET IS NULL");
					return SocketStatus.ERROR_DISCONNECTED;
				}
				Log.d("", "=AsynTaskespSendNRPN1=authenID="+authenID+"=nrpn="+nrpn+"=value="+value);
				byte[] authenArr = new byte[4]; 
				ByteUtils.intToBytes(authenArr, 0, authenID);	
				byte[] nrpnArr = new byte[2]; 
				ByteUtils.int16ToBytes(nrpnArr, 0, nrpn);
				byte[] valueArr = new byte[2]; 
				ByteUtils.int16ToBytes(valueArr, 0, value);
				
				byte[] sentData = new byte[authenArr.length + nrpnArr.length+valueArr.length];				
				System.arraycopy(authenArr, 0, sentData, 0, authenArr.length);
				System.arraycopy(nrpnArr, 0, sentData, authenArr.length, nrpnArr.length);
				System.arraycopy(valueArr, 0, sentData, authenArr.length + nrpnArr.length, valueArr.length);
				byte[] sentPackage = prepareSendFull(CMD_REQ_MIXER_SET_NRPN, sentData);				
				// Log.e("sentPackage", bytesToHex2(sentPackage));
				byte[] receivePackage = new byte[1500];
				InputStream socketStream = null;
				
				int resultProcess = processSocket_SendReceive(mReceiver,
						"CMD_REQ_MIXER_SET_NRPN", clientSocket, 
						sentPackage, receivePackage, socketStream,
						SocketStatus.ERROR_FROM_SERVER, SocketStatus.ERROR_DISCONNECTED);
				if (resultProcess != 999) {
					return resultProcess;
				}	
				
				receivePackage = processResponseData(receivePackage);
				response.parseServerPackage(receivePackage);			
				Log.d("", "=AsynTaskespSendNRPN7=="+response.getStatus()+"=="+response.getCommand());
				
				if(response.getCommand() != CMD_REQ_MIXER_SET_NRPN ||response.getStatus() != CMD_SUCCESS){
					iSuccess=false;
				}else
					iSuccess = true; 
								
				return SocketStatus.SUCCESS;
				
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				loading = false; 
			}
			return SocketStatus.ERROR_DISCONNECTED;
		}

		@Override
		protected void onPostExecute(Integer result) {
			loading = false; 
			if(result == SocketStatus.SUCCESS){
				if(mReceiver != null){
					mReceiver.echoDeviceespSendNRPN(getResultData(),nrpn, value);
				}
			}else{
				/*sendEvent(mReceiver, result, errMsg);
				if(result == SocketStatus.ERROR_SOCKET_READ){
					if(mReceiver != null){
						mReceiver.deviceespSendNRPN(getResultData(),nrpn, value);
					}	
				}	*/			
			}
		}
	}
}
