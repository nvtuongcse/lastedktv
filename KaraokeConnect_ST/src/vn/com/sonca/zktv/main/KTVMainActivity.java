package vn.com.sonca.zktv.main;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.poi.hssf.record.pivottable.ViewFieldsRecord;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.moonbelly.youtube.YoutubePlayerActivity;
import com.moonbelly.youtubeFrag.MyYouTubeInfo;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import app.sonca.Dialog.ScoreLayout.BaseDialog.OnBaseDialogListener;
import app.sonca.flower.DialogFlower;
import app.sonca.flower.DialogFlower.OnDialogFlowerListener;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.AddDataSong.DialogAddSongData;
import vn.com.sonca.AddDataSong.DialogAddSongData.OnDialogAddSongListener;
import vn.com.sonca.ColorLyric.FragmentKaraoke;
import vn.com.sonca.ColorLyric.FragmentKaraoke.OnKaraokeFragmentListener;
import vn.com.sonca.ColorLyric.FragmentReviewKaraoke;
import vn.com.sonca.ColorLyric.FragmentReviewKaraoke.OnReviewKaraokeFragmentListener;
import vn.com.sonca.ColorLyric.LyricData;
import vn.com.sonca.Lyric.LoadCheckLyricNew;
import vn.com.sonca.Lyric.LoadLyRicFileServerNew;
import vn.com.sonca.Lyric.LoadLyric;
import vn.com.sonca.Lyric.StoreLyric;
import vn.com.sonca.Lyric.TouchDialogConnect;
import vn.com.sonca.Lyric.TouchDialogConnect.OnDialogConnectListener;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.RemoteControl.ConvertData;
import vn.com.sonca.RemoteControl.RemoteIRCode;
import vn.com.sonca.SettingAll.DialogSettingAll;
import vn.com.sonca.SettingAll.DialogSettingAll.OnDialogSettingAllListener;
import vn.com.sonca.SetttingApp.DialogSettingApp;
import vn.com.sonca.SetttingApp.DialogSettingApp.OnDialogSettingAppListener;
import vn.com.sonca.Touch.BlockCommand.CommandBox;
import vn.com.sonca.Touch.BlockCommand.DialogConfirm;
import vn.com.sonca.Touch.BlockCommand.DialogDownTOC;
import vn.com.sonca.Touch.BlockCommand.DialogNoAddPlayList;
import vn.com.sonca.Touch.BlockCommand.CommandBox.OnCommandBoxListener;
import vn.com.sonca.Touch.BlockCommand.DialogConfirm.OnDialogConfirmListener;
import vn.com.sonca.Touch.BlockCommand.DialogDownTOC.OnDialogDownTOCListener;
import vn.com.sonca.Touch.BlockCommand.DialogNoAddPlayList.OnDialogNoAddPlayListListener;
import vn.com.sonca.Touch.Connect.Model;
import vn.com.sonca.Touch.Connect.TouchAdminFragment;
import vn.com.sonca.Touch.Connect.TouchAdminFragment.OnAdminFragmentListener;
import vn.com.sonca.Touch.Connect.TouchDeviceFragment;
import vn.com.sonca.Touch.Connect.TouchDeviceFragment.OnDeviceFragmentListener;
import vn.com.sonca.Touch.Connect.TouchHelloFragment;
import vn.com.sonca.Touch.Connect.TouchHelloFragment.OnTouchHelloFragmentListener;
import vn.com.sonca.Touch.Connect.TouchListDeviceFragment;
import vn.com.sonca.Touch.Connect.TouchListDeviceFragment.OnListDeviceFragmentListener;
import vn.com.sonca.Touch.Connect.TouchModelFragment;
import vn.com.sonca.Touch.Connect.TouchModelFragment.OnModelFragmentListener;
import vn.com.sonca.Touch.CustomView.MyVideoView;
import vn.com.sonca.Touch.CustomView.SliderMuteView;
import vn.com.sonca.Touch.CustomView.SliderView;
import vn.com.sonca.Touch.CustomView.TouchAnimationView;
import vn.com.sonca.Touch.CustomView.TouchBaseTypeView;
import vn.com.sonca.Touch.CustomView.TouchChangePerfectView;
import vn.com.sonca.Touch.CustomView.TouchDanceLinkView;
import vn.com.sonca.Touch.CustomView.TouchDanceView;
import vn.com.sonca.Touch.CustomView.TouchDefaultControlView;
import vn.com.sonca.Touch.CustomView.TouchDeleteSearchView;
import vn.com.sonca.Touch.CustomView.TouchDeviceAdmin;
import vn.com.sonca.Touch.CustomView.TouchDownloadView;
import vn.com.sonca.Touch.CustomView.TouchFavouriteTypeView;
import vn.com.sonca.Touch.CustomView.TouchHotSongTypeView;
import vn.com.sonca.Touch.CustomView.TouchKeyView;
import vn.com.sonca.Touch.CustomView.TouchMelodyView;
import vn.com.sonca.Touch.CustomView.TouchMusicianTypeView;
import vn.com.sonca.Touch.CustomView.TouchMyDrawerLayout;
import vn.com.sonca.Touch.CustomView.TouchMyGroupView;
import vn.com.sonca.Touch.CustomView.TouchNextView;
import vn.com.sonca.Touch.CustomView.TouchPauseView;
import vn.com.sonca.Touch.CustomView.TouchPopupViewDownloadPercent;
import vn.com.sonca.Touch.CustomView.TouchPopupViewExitApp;
import vn.com.sonca.Touch.CustomView.TouchPopupViewUpdatePic;
import vn.com.sonca.Touch.CustomView.TouchPopupViewUpdateToc;
import vn.com.sonca.Touch.CustomView.TouchRemixTypeView;
import vn.com.sonca.Touch.CustomView.TouchRepeatView;
import vn.com.sonca.Touch.CustomView.TouchSTypeTypeView;
import vn.com.sonca.Touch.CustomView.TouchScoreView;
import vn.com.sonca.Touch.CustomView.TouchSearchView;
import vn.com.sonca.Touch.CustomView.TouchSingerLinkView;
import vn.com.sonca.Touch.CustomView.TouchSingerTypeView;
import vn.com.sonca.Touch.CustomView.TouchSingerView;
import vn.com.sonca.Touch.CustomView.TouchSongTypeView;
import vn.com.sonca.Touch.CustomView.TouchTempoView;
import vn.com.sonca.Touch.CustomView.TouchToneView;
import vn.com.sonca.Touch.CustomView.TouchViewBack;
import vn.com.sonca.Touch.CustomView.TouchVolumnView;
import vn.com.sonca.Touch.CustomView.ViewFlower;
import vn.com.sonca.Touch.CustomView.XoaVideoView;
import vn.com.sonca.Touch.CustomView.MyVideoView.OnMyVideoViewListener;
import vn.com.sonca.Touch.CustomView.SliderMuteView.OnMuteListener;
import vn.com.sonca.Touch.CustomView.SliderView.OnSliderListener;
import vn.com.sonca.Touch.CustomView.TouchChangePerfectView.OnChangePerfectListener;
import vn.com.sonca.Touch.CustomView.TouchDanceLinkView.OnStopShowDanceListener;
import vn.com.sonca.Touch.CustomView.TouchDanceView.OnDancetListener;
import vn.com.sonca.Touch.CustomView.TouchDefaultControlView.OnDefaultListener;
import vn.com.sonca.Touch.CustomView.TouchDeleteSearchView.OnDeleteSearchListener;
import vn.com.sonca.Touch.CustomView.TouchKeyView.OnKeyListener;
import vn.com.sonca.Touch.CustomView.TouchMelodyView.OnMelodyListener;
import vn.com.sonca.Touch.CustomView.TouchMyDrawerLayout.DrawerListener;
import vn.com.sonca.Touch.CustomView.TouchMyDrawerLayout.PointerListener;
import vn.com.sonca.Touch.CustomView.TouchNextView.OnNextListener;
import vn.com.sonca.Touch.CustomView.TouchPauseView.OnPauseListener;
import vn.com.sonca.Touch.CustomView.TouchPopupViewDownloadPercent.OnPopupDownloadPercentListener;
import vn.com.sonca.Touch.CustomView.TouchPopupViewExitApp.OnPopupExitAppListener;
import vn.com.sonca.Touch.CustomView.TouchPopupViewUpdatePic.OnPopupUpdatePicListener;
import vn.com.sonca.Touch.CustomView.TouchPopupViewUpdateToc.OnPopupUpdateTocListener;
import vn.com.sonca.Touch.CustomView.TouchRepeatView.OnRepeatListener;
import vn.com.sonca.Touch.CustomView.TouchScoreView.OnScoreListener;
import vn.com.sonca.Touch.CustomView.TouchSingerLinkView.OnStopShowListener;
import vn.com.sonca.Touch.CustomView.TouchSingerView.OnSingerListener;
import vn.com.sonca.Touch.CustomView.TouchTempoView.OnTempoListener;
import vn.com.sonca.Touch.CustomView.TouchToneView.OnToneListener;
import vn.com.sonca.Touch.CustomView.TouchViewBack.OnBackListener;
import vn.com.sonca.Touch.CustomView.TouchVolumnView.OnVolumnListener;
import vn.com.sonca.Touch.Dance.TouchFragmentDance;
import vn.com.sonca.Touch.Favourite.FavouriteStore;
import vn.com.sonca.Touch.Favourite.TouchFragmentFavourite;
import vn.com.sonca.Touch.Hi_W.DialogHiW;
import vn.com.sonca.Touch.Hi_W.DialogUpdateFirmwareFromServer;
import vn.com.sonca.Touch.Hi_W.Firmware;
import vn.com.sonca.Touch.Hi_W.HiW_FirmwareConfig;
import vn.com.sonca.Touch.Hi_W.HiW_FirmwareInfo;
import vn.com.sonca.Touch.Hi_W.LoadCheckUpdateFirmware;
import vn.com.sonca.Touch.Hi_W.SaveFirmware;
import vn.com.sonca.Touch.Hi_W.DialogHiW.OnHiWListener;
import vn.com.sonca.Touch.Hi_W.DialogUpdateFirmwareFromServer.OnUpdateFirmwareFromServer;
import vn.com.sonca.Touch.Hi_W.LoadCheckUpdateFirmware.OnLoadCheckUpdateFirmwareListener;
import vn.com.sonca.Touch.HotSong.FragmentHotSong;
import vn.com.sonca.Touch.Keyboard.GroupKeyBoard;
import vn.com.sonca.Touch.Keyboard.TouchKeyBoardFragment;
import vn.com.sonca.Touch.Language.LanguageStore;
import vn.com.sonca.Touch.Language.TouchFragmentLanguage;
import vn.com.sonca.Touch.Language.TouchFragmentLanguage.OnLanguageFragmentListener;
import vn.com.sonca.Touch.Listener.TouchIBaseFragment;
import vn.com.sonca.Touch.Musician.TouchFragmentMusician;
import vn.com.sonca.Touch.PlayList.TouchFragmentPlayList;
import vn.com.sonca.Touch.Remix.TouchFragmentRemix;
import vn.com.sonca.Touch.Singer.TouchFragmentSinger;
import vn.com.sonca.Touch.Song.TouchFragmentSong;
import vn.com.sonca.Touch.SongFollow.TouchFragmentSongFollow;
import vn.com.sonca.Touch.SongType.TouchFragmentSongType;
import vn.com.sonca.Touch.SongType.TouchFragmentSongType.OnNumberSongTypeListener;
import vn.com.sonca.Touch.touchcontrol.TouchFragmentBase;
import vn.com.sonca.Touch.touchcontrol.TouchItemBack;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.Touch.touchcontrol.TouchTestSongData;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity.OnClearTextListener;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity.OnColorLyricListener;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity.OnListDeviceListener;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity.OnMainKeyboardListener;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity.OnMainListener;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity.OnToHelloListener;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity.ToDeviveFragment;
import vn.com.sonca.WebServer.MyHTTPService;
import vn.com.sonca.database.DBInstance;
import vn.com.sonca.database.DBInterface;
import vn.com.sonca.database.DbHelper;
import vn.com.sonca.database.DBInstance.SearchMode;
import vn.com.sonca.database.DBInstance.SearchType;
import vn.com.sonca.params.ByteUtils;
import vn.com.sonca.params.ConvertString;
import vn.com.sonca.params.ItemUSB;
import vn.com.sonca.params.Language;
import vn.com.sonca.params.LyricXML;
import vn.com.sonca.params.SKServer;
import vn.com.sonca.params.ServerPackage;
import vn.com.sonca.params.ServerStatus;
import vn.com.sonca.params.Song;
import vn.com.sonca.params.SongID;
import vn.com.sonca.params.SongInfo;
import vn.com.sonca.params.SongType;
import vn.com.sonca.samba.StreamService;
import vn.com.sonca.selectlist.FragmentSelectList;
import vn.com.sonca.selectlist.FragmentSelectList.OnSelectListFragmentListener;
import vn.com.sonca.smartkaraoke.NetworkSocket;
import vn.com.sonca.smartkaraoke.SmartKaraoke;
import vn.com.sonca.utils.AppSettings;
import vn.com.sonca.utils.StringUtils;
import vn.com.sonca.utils.XmlUtils;
import vn.com.sonca.utils.AppConfig.LANG_INDEX;
import vn.com.sonca.utils.AppConfig.MEDIA_TYPE;
import vn.com.sonca.zktv.FragData.FragNhacHot;
import vn.com.sonca.zktv.FragData.FragSingerFollow;
import vn.com.sonca.zktv.FragData.FragSong;
import vn.com.sonca.zktv.FragImage.FragImage.OnFragImageListener;
import vn.com.sonca.zktv.FragImage.FragSinger;
import vn.com.sonca.zktv.FragPlaylist.FragPlaylist;
import vn.com.sonca.zktv.FragPlaylist.NumberPlaylist;
import vn.com.sonca.zktv.FragSwitch.FragSwitch;
import vn.com.sonca.zktv.FragTheLoai.FragTheLoaiMenu;
import vn.com.sonca.zktv.Keyboard.FragKeyboard;
import vn.com.sonca.zktv.Keyboard.FragKeyboard.OnKTVKeyboardListener;
import vn.com.sonca.zktv.main.DialogCaoCap.OnDialogCaoCaplistener;
import vn.com.sonca.zktv.main.DialogSingerLink.OnDialogSingerLinkListener;
import vn.com.sonca.zzzzz.AutoConnect;
import vn.com.sonca.zzzzz.DeviceStote;
import vn.com.sonca.zzzzz.MyApplication;
import vn.com.sonca.zzzzz.AutoConnect.OnConnectDeviceListener;
import vn.com.sonca.zzzzz.MyApplication.OnApplicationListener;
import vn.com.sonca.zzzzz.MyApplication.OnReceiverAdminPassListener;
import vn.com.sonca.zzzzz.MyApplication.OnReceiverDeviceIsUserListener;
import vn.com.sonca.zzzzz.MyApplication.OnReceiverHIW_FirmwareConfigListener;
import vn.com.sonca.zzzzz.MyApplication.OnReceiverHIW_FirmwareListener;
import vn.com.sonca.zzzzz.MyApplication.OnReceiverLyricVidLinkListener;
import vn.com.sonca.zzzzz.MyApplication.OnReceiverModifyTOCListener;
import vn.com.sonca.zzzzz.MyApplication.OnReceiverScoreInfoListener;
import vn.com.sonca.zzzzz.MyApplication.OnReceiverSongFromUSBListener;
import vn.com.sonca.zzzzz.MyApplication.OnReceiverUSBStorageListListener;

public class KTVMainActivity extends FragmentActivity implements 
	OnKTVKeyboardListener, OnSwitchListener, OnClickListener,
	OnFragDataListener, OnFragImageListener,  TouchIBaseFragment,
	OnApplicationListener, OnListDeviceFragmentListener, OnDeviceFragmentListener,
	OnTouchHelloFragmentListener, OnAdminFragmentListener, OnModelFragmentListener,
	OnLanguageFragmentListener, OnLoadCheckUpdateFirmwareListener, OnSelectListFragmentListener,
	OnKaraokeFragmentListener, OnReviewKaraokeFragmentListener{
	
	private String TAB = "KTVMainActivity";
	
	public static final String SONG = "SONG";
	public static final String SINGER = "SINGER";
	public static final String FOLLOW = "FOLLOW";
	public static final String PLAYLIST = "PLAYLIST";
	public static final String THELOAI_MENU = "THELOAI_MENU";
	
	private Context context;
	private FragmentManager fragmentManager;
	
	private OnKTVMainListener onKTVMainListener;
	public interface OnKTVMainListener {
		public void OnKTVSearch(String search);
		public void OnLayoutFrag(ServerStatus status);
		public void OnSK90009();
	}
	
	private OnKTVChangeSearchListener onKTVChangeSearchListener;
	public interface OnKTVChangeSearchListener {
		public void OnKTVChangeSearch(String search);
	}
	
	private LinearLayout layoutMenu;
	private RelativeLayout layoutData;
	private LinearLayout layoutSongKeyboard;
	private LinearLayout layoutSongNoKey;
	private LinearLayout layoutTop;
	
	private ViewMain viewCaocap;
	private ViewMain viewWifi;
	private ViewMain viewQuaBai;
	
	private ViewMain viewTachLoi;
	private ViewMain viewDance;
	private ViewMain viewDachon;
	
	private ViewMain viewGiamAm;
	private ViewMain viewTangAm;
	private ViewMain viewQuayLai;
	
	private ViewHome viewHome;
	private ViewDevice viewDevice;
	private ViewStatus viewStatus;
	
	private NumberPlaylist numberPlaylist;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.ktv_mainactivity);
		fragmentManager = getSupportFragmentManager();
		context = getApplicationContext();
		hideActionBar();

		layoutMain = (RelativeLayout) findViewById(R.id.mainLayout);
		layoutMenu = (LinearLayout)findViewById(R.id.layoutMenu);
		layoutData = (RelativeLayout)findViewById(R.id.layoutData);
		layoutSongKeyboard = (LinearLayout)findViewById(R.id.layoutSongKeyboard);
		layoutSongNoKey = (LinearLayout)findViewById(R.id.layoutSongNoKey);
		layoutTop = (LinearLayout)findViewById(R.id.layoutTop);
		
		viewCaocap = (ViewMain)findViewById(R.id.viewCaocap);
		viewWifi = (ViewMain)findViewById(R.id.viewWifi);
		viewQuaBai = (ViewMain)findViewById(R.id.viewQuaBai);
		viewTachLoi = (ViewMain)findViewById(R.id.viewTachLoi);
		viewDance = (ViewMain)findViewById(R.id.viewDance);
		viewDachon = (ViewMain)findViewById(R.id.viewDachon);
		viewGiamAm = (ViewMain)findViewById(R.id.viewGiamAm);
		viewTangAm = (ViewMain)findViewById(R.id.viewTangAm);
		viewQuayLai = (ViewMain)findViewById(R.id.viewQuayLai);
		
		numberPlaylist = (NumberPlaylist)findViewById(R.id.numberPlaylist);
		
		viewCaocap.setDataView(
				context.getResources().getString(R.string.ktv_main_1), 
				context.getResources().getDrawable(R.drawable.ktv_control_hethong),
				context.getResources().getDrawable(R.drawable.ktv_control_hethong_xam));
		viewWifi.setDataView(
				context.getResources().getString(R.string.ktv_main_2), 
				context.getResources().getDrawable(R.drawable.ktv_control_videowifi),
				context.getResources().getDrawable(R.drawable.ktv_control_videowifi_xam));
		viewQuaBai.setDataView(
				context.getResources().getString(R.string.ktv_main_3), 
				context.getResources().getDrawable(R.drawable.ktv_control_next),
				context.getResources().getDrawable(R.drawable.ktv_control_next_xam));
		viewTachLoi.setDataView(
				context.getResources().getString(R.string.ktv_main_4), 
				context.getResources().getDrawable(R.drawable.ktv_control_vocal),
				context.getResources().getDrawable(R.drawable.ktv_control_vocal_xam));
		viewDance.setDataView(
				context.getResources().getString(R.string.ktv_caocap_11),
				context.getResources().getDrawable(R.drawable.ktv_caocap_dance),
				context.getResources().getDrawable(R.drawable.ktv_caocap_dance_xam));
		viewDachon.setDataView(
				context.getResources().getString(R.string.ktv_main_6), 
				context.getResources().getDrawable(R.drawable.ktv_control_playlist),
				context.getResources().getDrawable(R.drawable.ktv_control_playlist_xam));
		viewGiamAm.setDataView(
				context.getResources().getString(R.string.ktv_main_7), 
				context.getResources().getDrawable(R.drawable.ktv_control_volume_giam),
				context.getResources().getDrawable(R.drawable.ktv_control_volume_giam_xam));
		viewTangAm.setDataView(
				context.getResources().getString(R.string.ktv_main_8), 
				context.getResources().getDrawable(R.drawable.ktv_control_volume_tang),
				context.getResources().getDrawable(R.drawable.ktv_control_volume_tang_xam));
		viewQuayLai.setDataView(
				context.getResources().getString(R.string.ktv_main_9), 
				context.getResources().getDrawable(R.drawable.ktv_control_back),
				context.getResources().getDrawable(R.drawable.ktv_control_back_xam));		
		viewCaocap.setOnClickListener(this);
		viewWifi.setOnClickListener(this);
		viewQuaBai.setOnClickListener(this);
		viewTachLoi.setOnClickListener(this);
		viewDance.setOnClickListener(this);
		viewDachon.setOnClickListener(this);
		viewGiamAm.setOnClickListener(this);
		viewTangAm.setOnClickListener(this);
		viewQuayLai.setOnClickListener(this);		

		viewDevice = (ViewDevice)findViewById(R.id.viewDevice);
		viewHome = (ViewHome)findViewById(R.id.viewHome);
		viewStatus = (ViewStatus)findViewById(R.id.viewStatus);
		
		viewDevice.setOnClickListener(this);
		viewHome.setOnClickListener(this);
		
		if(MyApplication.enableSamba)
			startService(new Intent(this, StreamService.class));//samba
		MyApplication.flagDance = false;

		SharedPreferences settingFlagConnect = getSharedPreferences(
				PREFS_FLAGHDD, 0);
		MyApplication.flagEverConnectHDD = settingFlagConnect.getBoolean(
				"isEverConnect", false);
		MyApplication.flagEverConnect = settingFlagConnect.getBoolean(
				"isEverConnect2", false);
		
		AppSettings set = AppSettings.getInstance(context);
		MyApplication.flagEverKBX9 = set.isEverConnectKBX9();
		

		DeviceStote deviceStote = new DeviceStote(context);
		MyApplication.bOffUserList = deviceStote.getShareRefresh();
		if (!MyApplication.flagEverConnect) {
			if (deviceStote.getListSaveDevice().size() > 0) {
				settingFlagConnect.edit().putBoolean("isEverConnect2", true)
						.commit();
				MyApplication.flagEverConnect = true;
			}
		}

		MyApplication.intColorScreen = MyApplication.SCREEN_KTVUI;

		this.registerReceiver(this.mWifiInfoReceiver, new IntentFilter(
				WifiManager.RSSI_CHANGED_ACTION));

		layoutColorLyric = (RelativeLayout) findViewById(R.id.layoutColorLyric);
		layoutColorLyric.setVisibility(View.GONE);

		if (myDialogUpdatePic == null) {
			myDialogUpdatePic = new Dialog(this);
			myDialogUpdatePic.requestWindowFeature(Window.FEATURE_NO_TITLE);
			myDialogUpdatePic.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));
			myDialogUpdatePic
					.setContentView(R.layout.touch_item_popup_update_pic);

			WindowManager.LayoutParams pa = myDialogUpdatePic.getWindow()
					.getAttributes();
			pa.height = WindowManager.LayoutParams.MATCH_PARENT;
			pa.width = WindowManager.LayoutParams.MATCH_PARENT;
			pa.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
					| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
					| WindowManager.LayoutParams.FLAG_FULLSCREEN;

			myDialogUpdatePic.setOnShowListener(new OnShowListener() {

				@Override
				public void onShow(DialogInterface dialog) {
					AlphaAnimation alpha = new AlphaAnimation(1F, 0.3F);
					alpha.setDuration(0);
					alpha.setFillAfter(true);
					// layoutMain.startAnimation(alpha);
				}
			});

			myDialogUpdatePic.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					AlphaAnimation alpha = new AlphaAnimation(0.3F, 1F);
					alpha.setDuration(0);
					alpha.setFillAfter(true);
					// layoutMain.startAnimation(alpha);
				}
			});

			popupViewUpadtePic = (TouchPopupViewUpdatePic) myDialogUpdatePic
					.findViewById(R.id.myPopupUpdatePic);
			popupViewUpadtePic
					.setOnPopupUpdatePicListener(new OnPopupUpdatePicListener() {
						@Override
						public void OnUpdatePicYes() {
							myDialogUpdatePic.dismiss();
							downloadPic();
						}

						@Override
						public void OnUpdatePicNo() {
							myDialogUpdatePic.dismiss();
						}
					});
		}

		isFirst = loadFirstTime();

		AppSettings setting = AppSettings.getInstance(getApplicationContext());
		saveSkipUpdate1 = lastUpdate1 = setting.loadServerLastUpdate(1);
		saveSkipUpdate2 = lastUpdate2 = setting.loadServerLastUpdate(2);
		saveSkipUpdate3 = lastUpdate3 = setting.loadServerLastUpdate(3);
		saveSkipUpdate4 = lastUpdate4 = setting.loadServerLastUpdate(4);

		danceLinkView = (TouchDanceLinkView) findViewById(R.id.DanceLinkView);
		danceLinkView.setLayout(TouchDanceLinkView.NODANCE);
		danceLinkView.setOnStopShowDanceListener(new OnStopShowDanceListener() {
			@Override
			public void OnStop(boolean isActive, boolean layout) {
				findViewById(R.id.layoutLinkDance).setVisibility(View.GONE);
				if (MyApplication.intWifiRemote == MyApplication.SONCA) {
					if (isActive) {
						if (layout == TouchDanceLinkView.NODANCE) {
							((MyApplication) getApplication()).sendCommand(
									NetworkSocket.REMOTE_CMD_DANCE, 1);
						} else {
							((MyApplication) getApplication()).sendCommand(
									NetworkSocket.REMOTE_CMD_DANCE, 0);
						}
					}
				} else {
					((MyApplication) getApplication()).sendCommandKartrol(
							(byte) RemoteIRCode.IRC_DANCE, 0);
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							((MyApplication) getApplication())
									.sendCommandKartrol(
											(byte) RemoteIRCode.IRC_ENTER, 0);
						}
					}, 400);
				}
			}
		});

		downloadView = (TouchDownloadView) findViewById(R.id.DownloadView);
		drawerLayout = (TouchMyDrawerLayout) findViewById(R.id.drawer_layout);

		layoutConnect = (LinearLayout) findViewById(R.id.layoutConnect);
		layoutDownload = (LinearLayout) findViewById(R.id.layoutDownload);
		layoutAnimation = (LinearLayout) findViewById(R.id.layoutAnimation);
		animationView = (TouchAnimationView) findViewById(R.id.AnimationView);
		downloadView = (TouchDownloadView) findViewById(R.id.DownloadView);
		layoutAnimation.setVisibility(View.GONE);
		layoutDownload.setVisibility(View.GONE);

		drawerLayout.setScrimColor(Color.TRANSPARENT);
		drawerLayout.setDrawerLockMode(
				TouchMyDrawerLayout.LOCK_MODE_LOCKED_CLOSED, layoutConnect);
		drawerLayout.setDrawerListener(new DrawerListener() {
			@Override
			public void onDrawerStateChanged(int newState) {

			}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {

			}

			@Override
			public void onDrawerOpened(View drawerView) {
				drawerLayout.setDrawerLockMode(
						TouchMyDrawerLayout.LOCK_MODE_UNLOCKED, drawerView);
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				isProcessingSomething = false;
				boolLanguage = false;

				if (editDong1 != null && editDong2 != null) {
					((MyApplication) getApplication())
							.hideVirtualKeyboard(editDong1);
					((MyApplication) getApplication())
							.hideVirtualKeyboard(editDong2);
					if (!flagRunHide) {
						flagRunHide = true;
					}
				}
				if (serverStatus != null) {
					drawerLayout.setDrawerLockMode(
							TouchMyDrawerLayout.LOCK_MODE_LOCKED_CLOSED,
							layoutConnect);
				}
			}

			@Override
			public void onDrawerClosePopupYoutube(int position) {
				// TODO Auto-generated method stub
				
			}
		});

		LanguageStore langStore = new LanguageStore(this);
		langStore.initStarting();

		isDestroyMainActivity = true;

		// Fake show list device - fix click 2 time show left drawer
		ShowListDevice("");

		// Fake call 2row dialog
		showDialogCaoCap();
		if (dialogCaoCap != null) {
			dialogCaoCap.dismissDialog(false);
		}
		
		showKeyboardFrag();
		// showFragSinger();
		// showFragSong();
		
		showFragSwitch();
		
	}
	
	// TODO START SHOWFRAGMENT
	
	private void createFragmentListener(Fragment frag){
		onKTVMainListener = null;
		onKTVMainListener = (OnKTVMainListener)frag;
	}
	
	private void showKeyboardFrag(){
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		FragKeyboard keyBoardFragment = new FragKeyboard();
		onKTVChangeSearchListener = (FragKeyboard)keyBoardFragment;
		fragmentTransaction.add(R.id.fragKeyboard, keyBoardFragment);
		fragmentTransaction.commit();
	}
	
	private void showFragSong(){
		resetAllBackStack();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		FragSong frag = new FragSong();
		createFragmentListener(frag);
		fragmentTransaction.replace(R.id.fragData, frag, SONG);
		fragmentTransaction.commit();
		if(onKTVChangeSearchListener != null){
			onKTVChangeSearchListener.OnKTVChangeSearch("");
		}
		switchLayoutSong();
		switchLayoutData();
		
		viewStatus.setStatusName(getResources().getString(R.string.ktv_switch_2));
		
	}
	
	private void showFragSong(int theloai){
		resetAllBackStack();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		FragSong frag = new FragSong();
		Bundle bundle = new Bundle();
		bundle.putInt("intTheLoai", theloai);
		frag.setArguments(bundle);
		createFragmentListener(frag);
		fragmentTransaction.replace(R.id.fragData, frag, SONG);
		fragmentTransaction.commit();
		if(onKTVChangeSearchListener != null){
			onKTVChangeSearchListener.OnKTVChangeSearch("");
		}
		switchLayoutSong();
		switchLayoutData();
		
//		viewStatus.setStatusName(getResources().getString(R.string.ktv_switch_2));
		processDisplayTheLoaiName();		
	}
	
	private void processDisplayTheLoaiName(){
		switch (curSelectTheLoai) {
		case UtilSwitch.THELOAI_SONGCA:
			viewStatus.setStatusTheLoai(getResources().getDrawable(R.drawable.ktv_item_songca), getResources().getString(R.string.ktv_theloai_1));
			break;
		case UtilSwitch.THELOAI_NHACDO:
			viewStatus.setStatusTheLoai(getResources().getDrawable(R.drawable.ktv_item_nhacdo), getResources().getString(R.string.ktv_theloai_2));
			break;
		case UtilSwitch.THELOAI_THIEUNHI:
			viewStatus.setStatusTheLoai(getResources().getDrawable(R.drawable.ktv_item_thieunhi), getResources().getString(R.string.ktv_theloai_3));
			break;
		case UtilSwitch.THELOAI_CHINA:
			viewStatus.setStatusTheLoai(getResources().getDrawable(R.drawable.ktv_item_china), getResources().getString(R.string.ktv_theloai_10));
			break;
		case UtilSwitch.THELOAI_SINHNHAT:
			viewStatus.setStatusTheLoai(getResources().getDrawable(R.drawable.ktv_item_sinhnhat), getResources().getString(R.string.ktv_theloai_4));
			break;
		case UtilSwitch.THELOAI_VOLMOI:
			viewStatus.setStatusTheLoai(getResources().getDrawable(R.drawable.ktv_item_volmoi), getResources().getString(R.string.ktv_theloai_5));
			break;
		case UtilSwitch.THELOAI_REMIX:
			viewStatus.setStatusTheLoai(getResources().getDrawable(R.drawable.ktv_item_remix), getResources().getString(R.string.ktv_theloai_6));
			break;
		case UtilSwitch.THELOAI_LIENKHUC:
			viewStatus.setStatusTheLoai(getResources().getDrawable(R.drawable.ktv_item_lienkhuc), getResources().getString(R.string.ktv_theloai_7));
			break;
		case UtilSwitch.THELOAI_DANCA:
			viewStatus.setStatusTheLoai(getResources().getDrawable(R.drawable.ktv_item_danca), getResources().getString(R.string.ktv_theloai_8));
			break;
		case UtilSwitch.THELOAI_NHACHOT:
			viewStatus.setStatusTheLoai(getResources().getDrawable(R.drawable.ktv_item_hot), getResources().getString(R.string.ktv_theloai_9));
			break;
		default:
			viewStatus.setStatusName(getResources().getString(R.string.ktv_switch_2));
			break;
		}
	}
	
	private void showFragSinger(){
		resetAllBackStack();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		FragSinger frag = new FragSinger();
		createFragmentListener(frag);
		fragmentTransaction.replace(R.id.fragData, frag, SINGER);
		fragmentTransaction.commit();
		if(onKTVChangeSearchListener != null){
			onKTVChangeSearchListener.OnKTVChangeSearch("");
		}
		switchLayoutSong();
		switchLayoutData();
		
		viewStatus.setStatusName(getResources().getString(R.string.ktv_switch_4));
	}
	
	private void showFragNhacHot(){
		resetAllBackStack();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		FragNhacHot frag = new FragNhacHot();
		createFragmentListener(frag);
		fragmentTransaction.replace(R.id.fragData, frag, HOTSONG);
		fragmentTransaction.commit();
		if(onKTVChangeSearchListener != null){
			onKTVChangeSearchListener.OnKTVChangeSearch("");
		}
		switchLayoutSong();
		switchLayoutData();
		
		viewStatus.setStatusName(getResources().getString(R.string.ktv_switch_6));
		
	}
	
	private void showFragPlaylist(){
		resetAllFragment();
		resetAllBackStack();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		FragPlaylist frag = new FragPlaylist();
		createFragmentListener(frag);
		fragmentTransaction.replace(R.id.fragPlaylist, frag, PLAYLIST);
		fragmentTransaction.commit();
		if(onKTVChangeSearchListener != null){
			onKTVChangeSearchListener.OnKTVChangeSearch("");
		}
		switchLayoutPlaylist();
		switchLayoutData();
		
		if(MyApplication.flagDance){
			viewStatus.setStatusAction(getResources().getDrawable(R.drawable.ktv_caocap_dance), getResources().getString(R.string.ktv_caocap_11c));	
		} else {
			viewStatus.setStatusName(getResources().getString(R.string.ktv_switch_7));
		}
		
	}
	
	private void showFragSingerFollow(int id){
		MyLog.e(TAB, "showFragSingerFollow -- id = " + id);
		if(id == -1){
			return;
		}
		resetAllBackStackFollow();
		
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = manager.beginTransaction();
		Fragment fragPlaylist = fragmentManager.findFragmentByTag(PLAYLIST);
		FragSinger fragSinger = (FragSinger)fragmentManager.findFragmentByTag(SINGER);
		Fragment fragSONG = fragmentManager.findFragmentByTag(SONG);
		Fragment fragNhacHot = fragmentManager.findFragmentByTag(HOTSONG);
		FragSingerFollow frag = new FragSingerFollow();
		Bundle bundle = new Bundle();
		bundle.putInt("idImage", id);
		frag.setArguments(bundle);
		onKTVMainListener = null;
		if(fragSinger != null){
			fragmentTransaction.hide(fragSinger);
			fragmentTransaction.add(R.id.fragPlaylist, frag, FOLLOW);
			fragmentTransaction.addToBackStack(FOLLOW);
		}else if(fragSONG != null){
			fragmentTransaction.hide(fragSONG);
			fragmentTransaction.add(R.id.fragPlaylist, frag, FOLLOW);
			fragmentTransaction.addToBackStack(FOLLOW);
		}else if(fragPlaylist != null){
			fragmentTransaction.hide(fragPlaylist);
			fragmentTransaction.add(R.id.fragPlaylist, frag, FOLLOW);
			fragmentTransaction.addToBackStack(FOLLOW);
		}else if(fragNhacHot != null){
			fragmentTransaction.hide(fragNhacHot);
			fragmentTransaction.add(R.id.fragPlaylist, frag, FOLLOW);
			fragmentTransaction.addToBackStack(FOLLOW);
		}else{
			fragmentTransaction.replace(R.id.fragPlaylist, frag, FOLLOW);
		}
		createFragmentListener(frag);
		fragmentTransaction.commit();
		if(onKTVChangeSearchListener != null){
			onKTVChangeSearchListener.OnKTVChangeSearch("");
		}
		switchLayoutPlaylist();
		switchLayoutData();
		
		viewStatus.setStatusName(getResources().getString(R.string.ktv_switch_2b));
		MyApplication.flagKTV_OnFragFollow = true;
	}
	
	private void showFragSwitch(){	
		resetAllBackStack();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		FragSwitch frag = new FragSwitch();
		createFragmentListener(frag);
		fragmentTransaction.replace(R.id.fragMenu, frag);
		fragmentTransaction.commit();
		if(onKTVChangeSearchListener != null){
			onKTVChangeSearchListener.OnKTVChangeSearch("");
		}
		switchLayoutMenu();
		onKTVMainListener = null;
		viewStatus.setStatusName("");
	}
	
	private void showFragTheLoaiMenu(){
		MyLog.i(TAB, "showFragTheLoaiMenu");
		resetAllBackStack();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		FragTheLoaiMenu frag = new FragTheLoaiMenu();
		createFragmentListener(frag);
		fragmentTransaction.replace(R.id.fragMenu, frag, THELOAI_MENU);
		fragmentTransaction.commit();
		if(onKTVChangeSearchListener != null){
			onKTVChangeSearchListener.OnKTVChangeSearch("");
		}
		switchLayoutMenu();
		onKTVMainListener = null;
		viewStatus.setStatusName(getResources().getString(R.string.ktv_switch_1));
	}
	
	private void resetAllFragment(){
		MyLog.d(TAB, "resetAllFragment");
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		Fragment fragPlaylist = fragmentManager.findFragmentByTag(PLAYLIST);
		Fragment fragFOLLOW = fragmentManager.findFragmentByTag(FOLLOW);
		Fragment fragSINGER = fragmentManager.findFragmentByTag(SINGER);
		Fragment fragSONG = fragmentManager.findFragmentByTag(SONG);
		Fragment fragNhacHot = fragmentManager.findFragmentByTag(HOTSONG);
		Fragment fragTheLoaiMenu = fragmentManager.findFragmentByTag(THELOAI_MENU);
		
		if(fragPlaylist != null){
			fragmentTransaction.remove(fragPlaylist);
		}
		
		if(fragFOLLOW != null){
			fragmentTransaction.remove(fragFOLLOW);
		}
		
		if(fragSINGER != null){
			fragmentTransaction.remove(fragSINGER);
		}
		
		if(fragSONG != null){
			fragmentTransaction.remove(fragSONG);
		}
		
		if(fragNhacHot != null){
			fragmentTransaction.remove(fragNhacHot);
		}
		
		if(fragTheLoaiMenu != null){
			fragmentTransaction.remove(fragTheLoaiMenu);
		}
		
		curSelectTheLoai = -99;
		
		fragmentTransaction.commit();
		onKTVMainListener = null;
		MyApplication.flagKTV_OnFragFollow = false;
	}
	
	private void resetAllBackStack() {
		MyLog.d(TAB, "resetAllBackStack");
		if (fragmentManager != null) {
			for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {   
				FragmentManager.BackStackEntry entry = fragmentManager.getBackStackEntryAt(i);
				fragmentManager.popBackStack(entry.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
			}
		}

		MyApplication.flagKTV_OnFragFollow = false;
	}
	
	private void resetAllBackStackFollow() {
		MyLog.d(TAB, "resetAllBackStackFollow");
		if (fragmentManager != null) {
			for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {   
				FragmentManager.BackStackEntry entry = fragmentManager.getBackStackEntryAt(i);
				if(entry.getName().equals(FOLLOW)){
					fragmentManager.popBackStack(entry.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);	
				}
				
			}
		}
		
		MyApplication.flagKTV_OnFragFollow = false;

	}
	
	private void displayStatusAction(Drawable drawAction, String nameAction){
		if(viewStatus != null){
			viewStatus.setStatusAction(drawAction, nameAction);
		}
		
		layoutTop.setBackgroundDrawable(getResources().getDrawable(R.drawable.ktv_bg_note));
		
		showDisplayStatusName(2000);
	}
	
	private Timer timerDisplayStatusName;

	private void hideDisplayStatusName() {
		if (timerDisplayStatusName != null) {
			timerDisplayStatusName.cancel();
			timerDisplayStatusName = null;
		}
	}
	
	private void showDisplayStatusName(long time) {
		hideDisplayStatusName();
		timerDisplayStatusName = new Timer();
		timerDisplayStatusName.schedule(new TimerTask() {

			@Override
			public void run() {
				handlerDisplayStatusName.sendEmptyMessage(0);
			}
		}, time);

	}

	private Handler handlerDisplayStatusName = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			Fragment fragPlaylist = fragmentManager.findFragmentByTag(PLAYLIST);
			Fragment fragFOLLOW = fragmentManager.findFragmentByTag(FOLLOW);
			Fragment fragSINGER = fragmentManager.findFragmentByTag(SINGER);
			Fragment fragSONG = fragmentManager.findFragmentByTag(SONG);
			Fragment fragNhacHot = fragmentManager.findFragmentByTag(HOTSONG);			
			Fragment fragTheLoaiMenu = fragmentManager.findFragmentByTag(THELOAI_MENU);

			layoutTop.setBackgroundDrawable(null);
			
			if(fragFOLLOW != null){
				viewStatus.setStatusName(getResources().getString(R.string.ktv_switch_2b));
				return;
			}
			
			if(fragPlaylist != null){
				if(MyApplication.flagDance){
					viewStatus.setStatusAction(getResources().getDrawable(R.drawable.ktv_caocap_dance), getResources().getString(R.string.ktv_caocap_11c));	
				} else {
					viewStatus.setStatusName(getResources().getString(R.string.ktv_switch_7));
				}
				return;
			}
			
			if(fragSINGER != null){
				viewStatus.setStatusName(getResources().getString(R.string.ktv_switch_4));
				return;
			}
			
			if(fragSONG != null){
				processDisplayTheLoaiName();
				return;
			}
			
			if(fragNhacHot != null){
				viewStatus.setStatusName(getResources().getString(R.string.ktv_switch_6));
				return;
			}
			
			if(fragTheLoaiMenu != null){
				viewStatus.setStatusName(getResources().getString(R.string.ktv_switch_1));
				return;
			}
			
			viewStatus.setStatusName("");
		}
	};
	
	private void switchLayoutData(){
		if(layoutMenu.getVisibility() != View.GONE){
			layoutMenu.setVisibility(View.GONE);
		}
		if(layoutData.getVisibility() != View.VISIBLE){
			layoutData.setVisibility(View.VISIBLE);
		}
	}
	
	private void switchLayoutMenu(){
		if(layoutMenu.getVisibility() != View.VISIBLE){
			layoutMenu.setVisibility(View.VISIBLE);
		}
		if(layoutData.getVisibility() != View.GONE){
			layoutData.setVisibility(View.GONE);
		}
	}
	
	private void switchLayoutSong(){
		if(layoutSongKeyboard.getVisibility() != View.VISIBLE){
			layoutSongKeyboard.setVisibility(View.VISIBLE);
		}
		if(layoutSongNoKey.getVisibility() != View.GONE){
			layoutSongNoKey.setVisibility(View.GONE);
		}
	}
	private void switchLayoutPlaylist(){
		if(layoutSongKeyboard.getVisibility() != View.GONE){
			layoutSongKeyboard.setVisibility(View.GONE);
		}
		if(layoutSongNoKey.getVisibility() != View.VISIBLE){
			layoutSongNoKey.setVisibility(View.VISIBLE);
		}
	}
	
	private void OnBackLayout(){
		if(drawerLayout != null){
			if(drawerLayout.isDrawerOpen(layoutConnect)){
				drawerLayout.closeDrawers();
				return;
			}
		}
		
		if(TouchDanceLinkView.DANCE == danceLinkView.getLayout()){
			findViewById(R.id.layoutLinkDance).setVisibility(View.VISIBLE);
			danceLinkView.ShowDanceLink();
			return;
		}
		
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		
		MyLog.d(TAB, "Current BackStack = " + fragmentManager.getBackStackEntryCount());
		
		Fragment fragPlaylist = fragmentManager.findFragmentByTag(PLAYLIST);
		Fragment fragFOLLOW = fragmentManager.findFragmentByTag(FOLLOW);
		Fragment fragSINGER = fragmentManager.findFragmentByTag(SINGER);
		Fragment fragSONG = fragmentManager.findFragmentByTag(SONG);
		Fragment fragNhacHot = fragmentManager.findFragmentByTag(HOTSONG);
		Fragment fragTheLoaiMenu = fragmentManager.findFragmentByTag(THELOAI_MENU);
		if(fragFOLLOW != null){
			MyLog.e(TAB, "OnBackLayout : " + FOLLOW);
			if(fragSINGER != null){
				MyLog.d(TAB, "OnBackLayout to : " + SINGER);
				createFragmentListener(fragSINGER);
				fragmentTransaction.remove(fragFOLLOW);
				fragmentTransaction.show(fragSINGER);
				fragmentTransaction.commit();
				if(onKTVChangeSearchListener != null){
					String search = ((FragBase)fragSINGER).getTextSearch();
					onKTVChangeSearchListener.OnKTVChangeSearch(search);
				}
				if(fragFOLLOW != null){
					switchLayoutSong();
				}
				viewStatus.setStatusName(getResources().getString(R.string.ktv_switch_4));
			}else if(fragSONG != null){
				MyLog.d(TAB, "OnBackLayout to : " + SONG);
				createFragmentListener(fragSONG);
				fragmentTransaction.remove(fragFOLLOW);
				fragmentTransaction.show(fragSONG);
				fragmentTransaction.commit();
				if(onKTVChangeSearchListener != null){
					String search = ((FragBase)fragSONG).getTextSearch();
					onKTVChangeSearchListener.OnKTVChangeSearch(search);
				}
				if(fragFOLLOW != null){
					switchLayoutSong();
				}
				processDisplayTheLoaiName();
			}else if(fragPlaylist != null){
				MyLog.d(TAB, "OnBackLayout to : " + PLAYLIST);
				createFragmentListener(fragPlaylist);
				fragmentTransaction.remove(fragFOLLOW);
				fragmentTransaction.show(fragPlaylist);
				fragmentTransaction.commit();		
				if(fragFOLLOW != null){
					switchLayoutPlaylist();
					switchLayoutData();
				}
				if(MyApplication.flagDance){
					viewStatus.setStatusAction(getResources().getDrawable(R.drawable.ktv_caocap_dance), getResources().getString(R.string.ktv_caocap_11c));	
				} else {
					viewStatus.setStatusName(getResources().getString(R.string.ktv_switch_7));
				}
			} else if(fragNhacHot != null){
				MyLog.d(TAB, "OnBackLayout to : " + HOTSONG);
				createFragmentListener(fragNhacHot);
				fragmentTransaction.remove(fragFOLLOW);
				fragmentTransaction.show(fragNhacHot);
				fragmentTransaction.commit();
				if(onKTVChangeSearchListener != null){
					String search = ((FragBase)fragNhacHot).getTextSearch();
					onKTVChangeSearchListener.OnKTVChangeSearch(search);
				}
				if(fragNhacHot != null){
					switchLayoutSong();
				}
				viewStatus.setStatusName(getResources().getString(R.string.ktv_switch_6));
			}
			fragmentManager.popBackStack();		
			MyApplication.flagKTV_OnFragFollow = false;
			return;
		}
		
		if(fragTheLoaiMenu != null){
			MyLog.e(TAB, "OnBackLayout curSelectTheLoai = " + curSelectTheLoai);
			if(curSelectTheLoai == -99){
				gotoHomeMenu();	
			} else {
				curSelectTheLoai = -99;
				switchLayoutMenu();
				showFragTheLoaiMenu();
			}			
			return;
		}

		gotoHomeMenu();
		viewStatus.setStatusName("");		
	}
	
	@Override
	public void OnKeyClick(String namekey) {
		if(onKTVMainListener != null){
			onKTVMainListener.OnKTVSearch(namekey);
		}
	}
	
	private boolean boolLanguage = false;
	private int curSelectTheLoai = -99;

	@Override
	public void OnSwitchClick(int id) {
		switch (id) {
		case UtilSwitch.CHON_TEN:
			switchLayoutData();
			showFragSong();
			break;
		case UtilSwitch.CA_SI:
			switchLayoutData();
			showFragSinger();
			break;
		case UtilSwitch.DA_CHON:
			switchLayoutData();
			showFragPlaylist();
			break;
		case UtilSwitch.NGON_NGU:
			drawerLayout.openDrawer(layoutConnect);
			boolLanguage = true;
			ShowLanguage();
			break;
		case UtilSwitch.NHAC_HOT:
			switchLayoutData();
			showFragNhacHot();
			break;
		case UtilSwitch.GIAO_DIEN:
			onCallShowGiaoDienDialog();
			break;
		case UtilSwitch.HE_THONG:
			OnMyShowLeftDrawer();
			break;
		case UtilSwitch.THE_LOAI:
			switchLayoutMenu();
			showFragTheLoaiMenu();
			break;
						
			
		case UtilSwitch.THELOAI_SONGCA:{
			curSelectTheLoai = UtilSwitch.THELOAI_SONGCA;
			switchLayoutData();
			int idTheloai = DbHelper.SongType_CoupleSinger;
			showFragSong(idTheloai);
		}
			break;	
		case UtilSwitch.THELOAI_CHINA:{
			curSelectTheLoai = UtilSwitch.THELOAI_CHINA;
			switchLayoutData();
			int idTheloai = DbHelper.SongType_China;
			showFragSong(idTheloai);
		}
			break;
		case UtilSwitch.THELOAI_NHACDO:{
			curSelectTheLoai = UtilSwitch.THELOAI_NHACDO;
			switchLayoutData();
			int idTheloai = 7;
			List<SongType> songTypeList = DBInterface.DBSearchSongType("",
					SearchMode.MODE_FULL, 0, 0, KTVMainActivity.this);
			for (SongType songType : songTypeList) {
				if(StringUtils.getRawString(songType.getName(), LANG_INDEX.ALL_LANGUAGE).equalsIgnoreCase("NHAC DO")){
					idTheloai = songType.getID();
				}
			}
			
			showFragSong(idTheloai);
		}
			break;	
		case UtilSwitch.THELOAI_THIEUNHI:{
			curSelectTheLoai = UtilSwitch.THELOAI_THIEUNHI;
			switchLayoutData();
			int idTheloai = 2;
			List<SongType> songTypeList = DBInterface.DBSearchSongType("",
					SearchMode.MODE_FULL, 0, 0, KTVMainActivity.this);
			for (SongType songType : songTypeList) {
				if(StringUtils.getRawString(songType.getName(), LANG_INDEX.ALL_LANGUAGE).equalsIgnoreCase("THIEU NHI")){
					idTheloai = songType.getID();
				}
			}
			
			showFragSong(idTheloai);
		}
			break;
		case UtilSwitch.THELOAI_VOLMOI:{
			curSelectTheLoai = UtilSwitch.THELOAI_VOLMOI;
			switchLayoutData();
			int idTheloai = DbHelper.SongType_NewVol;
			showFragSong(idTheloai);
		}
			break;	
		case UtilSwitch.THELOAI_DANCA:{
			curSelectTheLoai = UtilSwitch.THELOAI_DANCA;
			switchLayoutData();
			int idTheloai = 2;
			List<SongType> songTypeList = DBInterface.DBSearchSongType("",
					SearchMode.MODE_FULL, 0, 0, KTVMainActivity.this);
			for (SongType songType : songTypeList) {
				if(StringUtils.getRawString(songType.getName(), LANG_INDEX.ALL_LANGUAGE).equalsIgnoreCase("DAN CA")){
					idTheloai = songType.getID();
				}
			}
			
			showFragSong(idTheloai);
		}
			break;	
		case UtilSwitch.THELOAI_REMIX:{
			curSelectTheLoai = UtilSwitch.THELOAI_REMIX;
			switchLayoutData();
			int idTheloai = MyApplication.KTV_THELOAI_REMIX;			
			showFragSong(idTheloai);
		}
			break;
		case UtilSwitch.THELOAI_NHACHOT:{
			curSelectTheLoai = UtilSwitch.THELOAI_NHACHOT;
			switchLayoutData();
			int idTheloai = DbHelper.SongType_HotSong;
			showFragSong(idTheloai);
		}
			break;
		default:
			showDialogMessage(getResources().getString(R.string.ktv_msg_1));
			break;
		}
	}
	
	private void gotoHomeMenu(){
		// dang o dance - hoi thoat dance
		if(TouchDanceLinkView.DANCE == danceLinkView.getLayout()){
			findViewById(R.id.layoutLinkDance).setVisibility(View.VISIBLE);
			danceLinkView.ShowDanceLink();
			return;
		}
		
		resetAllFragment();
		resetAllBackStack();
		
		showFragSwitch();
	}

	@Override
	public void onClick(View view) {
		// CHUA KET NOI
		
		boolean flagBlock = false;
		try {
			if(((ViewMain)view).processEnableView() == false || ((ViewMain)view).getStateView() == View.INVISIBLE){
				flagBlock = true;
			}	
		} catch (Exception e) {
			
		}
		
		
		if(serverStatus == null){
			switch (view.getId()) {
			case R.id.viewHome:
				gotoHomeMenu();
				break;
			case R.id.viewDevice:
				OnMyShowLeftDrawer();
				break;
			case R.id.viewQuayLai:
				MyLog.e(TAB, "onClick : viewQuayLai");
				OnBackLayout();
				break;
			default:
				showDialogConnect();
				break;
			}
			
			return;
		}
		
		if(flagBlock){
			return;
		}
		
		// DA KET NOI
		switch (view.getId()) {
		case R.id.viewHome:
			MyLog.e(TAB, "onClick : viewHome");
			gotoHomeMenu();
			break;
		case R.id.viewCaocap:
			MyLog.e(TAB, "onClick : viewCaocap");
			showDialogCaoCap();
			break;
		case R.id.viewDevice:
			OnMyShowLeftDrawer();
			break;
		case R.id.viewQuayLai:
			MyLog.e(TAB, "onClick : viewQuayLai");
			OnBackLayout();
			break;
		case R.id.viewWifi:{
			MyLog.e(TAB, "onClick : viewWifi");
			final String appRootPath = android.os.Environment
					.getExternalStorageDirectory()
					.toString()
					.concat(String.format("/%s/%s", "Android/data",
							getPackageName()));
			
			File folderLyric = new File(
					appRootPath.concat("/COLORLYRIC"));
			if(folderLyric.listFiles().length > 0){
				showKaraoke();
			} else {
				showDialogMessage(getResources().getString(R.string.msg_kara_3));
			}
		}
			break;
		case R.id.viewQuaBai:{
			MyLog.e(TAB, "onClick : viewQuaBai");
			if(MyApplication.flagOnPopup){
				if(MyApplication.intWifiRemote == MyApplication.SONCA){
					String msg1 = getResources().getString(R.string.confirm_4) + "?";
					String msg2 = "";
					ShowConfirmDialog(msg1, msg2, 6, 0, 0, 0);
				}else{
					String msg1 = getResources().getString(R.string.confirm_4) + "?";
					String msg2 = "";
					ShowConfirmDialog(msg1, msg2, 7, 0, 0, 0);
				}
			} else {
				if(MyApplication.intWifiRemote == MyApplication.SONCA){
					((MyApplication) getApplication()).sendCommand(
						NetworkSocket.REMOTE_CMD_NEXT, 0);
				}else{
					MyLog.e(TAB, "nextView : send ok");
					((MyApplication) getApplication()).sendCommandKartrol(
							(byte) RemoteIRCode.IRC_STOP,
							0);
				}
				
				displayStatusAction(getResources().getDrawable(R.drawable.ktv_control_next), getResources().getString(R.string.ktv_main_3));
			}
		}
			break;
		case R.id.viewTachLoi:{
			MyLog.e(TAB, "onClick : viewTachLoi");
			boolean isSingerOn = serverStatus.isSingerOn();
			if(isSingerOn){
				displayStatusAction(getResources().getDrawable(R.drawable.ktv_caocap_tachloi), getResources().getString(R.string.ktv_main_4));
			} else {
				displayStatusAction(getResources().getDrawable(R.drawable.ktv_caocap_tachloi_off), getResources().getString(R.string.ktv_main_4b2));
			}
			
			isSingerOn = !isSingerOn;
			
			if(MyApplication.intWifiRemote == MyApplication.SONCA){
				((MyApplication) getApplication()).sendCommand(
						NetworkSocket.REMOTE_CMD_SINGER, isSingerOn ? 1 : 0);
			}else{
				((MyApplication) getApplication()).sendCommandKartrol(
						(byte) RemoteIRCode.IRC_MELODY,
						0);
			}
		}
			break;
		case R.id.viewDance:{
			MyLog.e(TAB, "onClick : viewDance");
			if(dialogCaoCap != null){
				dialogCaoCap.dismissDialog(false);
			}
			findViewById(R.id.layoutLinkDance).setVisibility(View.VISIBLE);
			danceLinkView.ShowDanceLink();			
		}
			break;
		case R.id.viewDachon:{
			MyLog.e(TAB, "onClick : viewDachon");
			showFragPlaylist();
		}
			break;
		case R.id.viewGiamAm:{
			MyLog.e(TAB, "onClick : viewGiamAm");
			int value = viewGiamAm.getVolume() - 1;
			if(value < 0){
				value = 0;
			}
			if(MyApplication.intWifiRemote == MyApplication.SONCA){
				((MyApplication) getApplication()).sendCommand(
						NetworkSocket.REMOTE_CMD_VOLUME, value);
				
				displayStatusAction(getResources().getDrawable(R.drawable.ktv_caocap_vol_giam), getResources().getString(R.string.ktv_main_7) + " : " + value);
			}else{
				value = -1;
				if (value > 0) {
					((MyApplication) getApplication()).sendCommandKartrol(
							(byte) RemoteIRCode.IRC_VOLUME_UP, 0);
				} else if (value < 0) {
					((MyApplication) getApplication()).sendCommandKartrol(
							(byte) RemoteIRCode.IRC_VOLUME_DN, 0);
				}
				
				displayStatusAction(getResources().getDrawable(R.drawable.ktv_caocap_vol_giam), getResources().getString(R.string.ktv_main_7));
			}
			
			
		}
			break;
		case R.id.viewTangAm:{
			MyLog.e(TAB, "onClick : viewTangAm");
			
			int maxVolume = 16;
			if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
					|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
					 || MyApplication.intSvrModel == MyApplication.SONCA_KBX9 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
					 || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK || MyApplication.intSvrModel == MyApplication.SONCA_TBT){
				maxVolume = 15;
			}
			
			int value = viewGiamAm.getVolume() + 1;
			if(value > maxVolume){
				value = maxVolume;
			}
			if(MyApplication.intWifiRemote == MyApplication.SONCA){
				((MyApplication) getApplication()).sendCommand(
						NetworkSocket.REMOTE_CMD_VOLUME, value);
				displayStatusAction(getResources().getDrawable(R.drawable.ktv_caocap_vol_tang), getResources().getString(R.string.ktv_main_8) + " : " + value);
			}else{
				value = 1;
				if (value > 0) {
					((MyApplication) getApplication()).sendCommandKartrol(
							(byte) RemoteIRCode.IRC_VOLUME_UP, 0);
				} else if (value < 0) {
					((MyApplication) getApplication()).sendCommandKartrol(
							(byte) RemoteIRCode.IRC_VOLUME_DN, 0);
				}
				displayStatusAction(getResources().getDrawable(R.drawable.ktv_caocap_vol_tang), getResources().getString(R.string.ktv_main_8));
			}
			
			
		}
			break;
		default:
			break;
		}
		
	}
	
	private DialogSingerLink dialogSingerLink;
	private void showDialogSingerLink(Song song) {
		if (song == null) {
			return;
		}
		String name = song.getSinger().getName();
		MyLog.e(TAB, "showDialogSingerLink : " + name);
		if (name.equals("-")) {
			return;
		}
		if (song.getSingerId().length > 1) {
			if (dialogSingerLink == null) {
				dialogSingerLink = new DialogSingerLink(context, getWindow(), song);
				dialogSingerLink.setOnBaseDialogListener(new OnBaseDialogListener() {

					@Override
					public void OnShowDialog() {
					}

					@Override
					public void OnFinishDialog() {
						dialogSingerLink = null;
					}
				});
				dialogSingerLink.setOnDialogSingerLinkListener(new OnDialogSingerLinkListener() {
					@Override
					public void OnSingerLink(int idSinger) {
						if(dialogSingerLink != null){
							dialogSingerLink.dismissDialog(false);
							dialogSingerLink = null;
						}
						
						showFragSingerFollow(idSinger);
					}
				});
				dialogSingerLink.showDialog();
			}
		} else {
			showFragSingerFollow(song.getSingerId()[0]);
		}
	}
	
	private DialogCaoCap dialogCaoCap;
	private void showDialogCaoCap(){
		if(dialogCaoCap == null){
			dialogCaoCap = new DialogCaoCap(context, getWindow());
			dialogCaoCap.setMainActivity(this);
			dialogCaoCap.setStartStatus(serverStatus);
			dialogCaoCap.setOnBaseDialogListener(new OnBaseDialogListener() {
				
				@Override
				public void OnShowDialog() {}
				
				@Override
				public void OnFinishDialog() {
					dialogCaoCap = null;
				}
			});
			dialogCaoCap.setOnDialogCaoCaplistener(new OnDialogCaoCaplistener() {
				
				@Override
				public void OnClick(int idView) {
					switch (idView) {
					case R.id.melodyGiam:{
						int value = viewMelodyGiam.getMelody() - 1;
						if(value < 0){
							value = 0;
						}
						
						if(MyApplication.intWifiRemote == MyApplication.SONCA){
							((MyApplication) getApplication()).sendCommand(
									NetworkSocket.REMOTE_CMD_MELODY, value);
							displayStatusAction(getResources().getDrawable(R.drawable.ktv_caocap_melody_giam), getResources().getString(R.string.ktv_caocap_1a) + " : " + value);
						}else{
							((MyApplication) getApplication()).sendCommandKartrol(
									(byte) RemoteIRCode.IRC_MELODY, 0);
							displayStatusAction(getResources().getDrawable(R.drawable.ktv_caocap_melody_giam), getResources().getString(R.string.ktv_caocap_1a));
						}
						
					}
						
						break;
					case R.id.melodyTang:{
						int value = viewMelodyGiam.getMelody() + 1;
						if(value > 10){
							value = 10;
						}
						
						if(MyApplication.intWifiRemote == MyApplication.SONCA){
							((MyApplication) getApplication()).sendCommand(
									NetworkSocket.REMOTE_CMD_MELODY, value);
							
							displayStatusAction(getResources().getDrawable(R.drawable.ktv_caocap_melody_tang), getResources().getString(R.string.ktv_caocap_1b) + " : " + value);
						}else{
							((MyApplication) getApplication()).sendCommandKartrol(
									(byte) RemoteIRCode.IRC_MELODY, 0);
							
							displayStatusAction(getResources().getDrawable(R.drawable.ktv_caocap_melody_tang), getResources().getString(R.string.ktv_caocap_1b));
						}
						
					}

						break;
					case R.id.tempoGiam:{
						int value = viewTempoGiam.getTempo() - 1;
						if(value < -4){
							value = 0;
						}
						
						if(MyApplication.intWifiRemote == MyApplication.SONCA){
							((MyApplication) getApplication()).sendCommand(
									NetworkSocket.REMOTE_CMD_TEMPO, value);
							
							displayStatusAction(getResources().getDrawable(R.drawable.ktv_caocap_temp_giam), getResources().getString(R.string.ktv_caocap_2a) + " : " + value);
						}else{
							value = 1;
							if (value > 0) {
								((MyApplication) getApplication()).sendCommandKartrol(
										(byte) RemoteIRCode.IRC_TEMPO_UP, 0);
							} else if (value < 0) {
								((MyApplication) getApplication()).sendCommandKartrol(
										(byte) RemoteIRCode.IRC_TEMPO_DOWN, 0);
							}	
							displayStatusAction(getResources().getDrawable(R.drawable.ktv_caocap_temp_giam), getResources().getString(R.string.ktv_caocap_2a));
						}
					}

						break;
					case R.id.tempoTang:{
						int value = viewTempoGiam.getTempo() + 1;
						if(value > 4){
							value = 0;
						}
						
						if(MyApplication.intWifiRemote == MyApplication.SONCA){
							((MyApplication) getApplication()).sendCommand(
									NetworkSocket.REMOTE_CMD_TEMPO, value);
							displayStatusAction(getResources().getDrawable(R.drawable.ktv_caocap_temp_tang), getResources().getString(R.string.ktv_caocap_2a) + " : " + value);
						}else{
							value = 1;
							if (value > 0) {
								((MyApplication) getApplication()).sendCommandKartrol(
										(byte) RemoteIRCode.IRC_TEMPO_UP, 0);
							} else if (value < 0) {
								((MyApplication) getApplication()).sendCommandKartrol(
										(byte) RemoteIRCode.IRC_TEMPO_DOWN, 0);
							}	
							displayStatusAction(getResources().getDrawable(R.drawable.ktv_caocap_temp_tang), getResources().getString(R.string.ktv_caocap_2b));
						}
					}

						break;
					case R.id.tone:{
						int value = viewTone.getToneView() + 1;
						if(value > 2){
							value = 0;
						}
						
						if(MyApplication.intWifiRemote == MyApplication.SONCA){
							((MyApplication) getApplication()).sendCommand(
									NetworkSocket.REMOTE_CMD_TONE, value);
							
							MyLog.e(TAB, "value = " + value);
							
							if(value == 1){
								displayStatusAction(getResources().getDrawable(R.drawable.ktv_caocap_tone_nu), getResources().getString(R.string.ktv_caocap_3) + " : " + getResources().getString(R.string.tone_nu));
							} else if (value == 2) {
								displayStatusAction(getResources().getDrawable(R.drawable.ktv_caocap_tone_nam), getResources().getString(R.string.ktv_caocap_3) + " : " + getResources().getString(R.string.tone_nam));
							} else {
								displayStatusAction(getResources().getDrawable(R.drawable.ktv_caocap_tone), getResources().getString(R.string.ktv_caocap_3) + " : " + getResources().getString(R.string.tone_man_nu));	
							}
							
						}else{
							((MyApplication) getApplication()).sendCommandKartrol(
									(byte) RemoteIRCode.IRC_TONE, 0);
							
							displayStatusAction(getResources().getDrawable(R.drawable.ktv_caocap_tone), getResources().getString(R.string.ktv_caocap_3));
						}
					}

						break;
					case R.id.hoaAm:{
						if(((MyApplication) KTVMainActivity.this.getApplication()).getListActive().size() == 99){
							return;
						}
						
						if(System.currentTimeMillis() - hoaAmTime <= 3000){
							return;
						}
						
						displayStatusAction(getResources().getDrawable(R.drawable.ktv_caocap_hoaam), getResources().getString(R.string.ktv_caocap_4));
							
						hoaAmTime = System.currentTimeMillis();
						
						try {
							MyLog.e(" ", " ");MyLog.e(" ", " ");
							MyLog.e("HOA AM", "START...............");
							
							MEDIA_TYPE aType = MEDIA_TYPE.values()[serverStatus.getMediaType()];
							if(aType == MEDIA_TYPE.MIDI){
								MyLog.e("", "MEDIA_TYPE.MIDI...............");
								String curName = serverStatus.getPlayingSongName();
								int curIdSong = serverStatus.getPlayingSongID();
								int curTypeABC = serverStatus.getPlayingSongTypeABC();
								MyLog.e("", "curName = " + curName);
								MyLog.e("", "curIdSong = " + curIdSong);
								MyLog.e("", "curTypeABC = " + curTypeABC);
								
								List<Integer> listIntType = ((MyApplication) KTVMainActivity.this.getApplication()).getTotalMIDIType(curIdSong, curName);
								MyLog.e("", "listIntType = " + listIntType.size());
								if(listIntType.size() < 2){
									// do nothing	
								} else {
									int maxRange = 0;
									int minRange = 5;
									for (Integer intType : listIntType) {
										MyLog.e("", "intType = " + intType);
										if(intType > maxRange){
											maxRange = intType;
										}
										if(intType < minRange){
											minRange = intType;
										}
									}
									
									if(curTypeABC < minRange || curTypeABC > maxRange){
										// do nothing
										return;
									}		
									
									int newTypeABC = curTypeABC + 1;
									if(newTypeABC > maxRange){
										newTypeABC = minRange;
									}				
									MyLog.e("","curTypeABC = " + curTypeABC + " -- newTypeABC = " + newTypeABC);
									((MyApplication) getApplication()).firstReservedSong(curIdSong, newTypeABC, -1);
									((MyApplication) getApplication()).sendCommand(
											NetworkSocket.REMOTE_CMD_STOP, 0);
									
								}	
							} else {
								// do nothing
							}		
							
							MyLog.e("HOA AM", "END...............");
							MyLog.e(" ", " ");MyLog.e(" ", " ");
						} catch (Exception e) {
							// do nothing	
						}	
					}
					
						break;
					case R.id.macDinh:{
						if(MyApplication.intWifiRemote == MyApplication.SONCA){
							((MyApplication) getApplication()).sendCommand(
									NetworkSocket.REMOTE_CMD_DEFAULT, 0);
							
							displayStatusAction(getResources().getDrawable(R.drawable.ktv_caocap_default), getResources().getString(R.string.ktv_caocap_5));
						}else{
							
						}
					}

						break;
						
					case R.id.chinhMiDi:{
						MyLog.e(TAB, "onClick : viewChinhMidi");
						
					}
						break;
					case R.id.ngatTieng:{
						if(viewNgatTieng != null){
							boolean flag = !viewNgatTieng.getMute();
							if(MyApplication.intWifiRemote == MyApplication.SONCA){
								((MyApplication) getApplication()).sendCommand(
										NetworkSocket.REMOTE_CMD_MUTE, flag ? 0 : 1);
								
								if(flag){
									displayStatusAction(getResources().getDrawable(R.drawable.ktv_caocap_mute), getResources().getString(R.string.ktv_caocap_7));	
								} else {
									displayStatusAction(getResources().getDrawable(R.drawable.ktv_caocap_mute), getResources().getString(R.string.ktv_caocap_7b));
								}
								
							} else {
								((MyApplication) getApplication()).sendCommandKartrol((byte) RemoteIRCode.IRC_MUTE, 0);
								displayStatusAction(getResources().getDrawable(R.drawable.ktv_caocap_mute), getResources().getString(R.string.ktv_caocap_7));
							}
						}
							
					}						
						break;
					case R.id.tamDung:{
						if(viewTamDung != null){
							boolean flag = !viewTamDung.isPlayPause();
							if(MyApplication.intWifiRemote == MyApplication.SONCA){
								if (flag) {
									((MyApplication) getApplication()).sendCommand(
											NetworkSocket.REMOTE_CMD_PLAY, 0);
								} else {
									((MyApplication) getApplication()).sendCommand(
											NetworkSocket.REMOTE_CMD_PAUSE, 0);
								}
								
								if(flag){
									displayStatusAction(getResources().getDrawable(R.drawable.ktv_caocap_play), getResources().getString(R.string.ktv_caocap_8b));
								} else {
									displayStatusAction(getResources().getDrawable(R.drawable.ktv_caocap_pause), getResources().getString(R.string.ktv_caocap_8));
								}
								
								
							}else{
								((MyApplication) getApplication()).sendCommandKartrol(
										(byte) RemoteIRCode.IRC_PAUSEPLAY,
										0);
								
								displayStatusAction(getResources().getDrawable(R.drawable.ktv_caocap_pause), getResources().getString(R.string.ktv_caocap_8c));
							}	
						}
							
					}
						break;
					case R.id.giamTone:{
						int value = viewGiamTone.getKey() - 1;
						
						if(value < -6){
							value = 0;
						}
						
						if(MyApplication.intWifiRemote == MyApplication.SONCA){
							((MyApplication) getApplication()).sendCommand(
									NetworkSocket.REMOTE_CMD_KEY, value);
							displayStatusAction(getResources().getDrawable(R.drawable.ktv_caocap_key_giam), getResources().getString(R.string.ktv_caocap_9) + " : " + value);							
						}else{
							value = -1;
							if (value > 0) {
								((MyApplication) getApplication()).sendCommandKartrol(
										(byte) RemoteIRCode.IRC_KEY_UP, 0);
							} else if (value < 0) {
								((MyApplication) getApplication()).sendCommandKartrol(
										(byte) RemoteIRCode.IRC_KEY_DN, 0);
							}
							
							displayStatusAction(getResources().getDrawable(R.drawable.ktv_caocap_key_giam), getResources().getString(R.string.ktv_caocap_9));
						}	
					}
						break;
					case R.id.tangTone:{
						int value = viewGiamTone.getKey() + 1;
						
						if(value > 6){
							value = 0;
						}
						
						if(MyApplication.intWifiRemote == MyApplication.SONCA){
							((MyApplication) getApplication()).sendCommand(
									NetworkSocket.REMOTE_CMD_KEY, value);
							
							displayStatusAction(getResources().getDrawable(R.drawable.ktv_caocap_key_tang), getResources().getString(R.string.ktv_caocap_10) + " : " + value);
						}else{
							value = 1;
							if (value > 0) {
								((MyApplication) getApplication()).sendCommandKartrol(
										(byte) RemoteIRCode.IRC_KEY_UP, 0);
							} else if (value < 0) {
								((MyApplication) getApplication()).sendCommandKartrol(
										(byte) RemoteIRCode.IRC_KEY_DN, 0);
							}
							
							displayStatusAction(getResources().getDrawable(R.drawable.ktv_caocap_key_tang), getResources().getString(R.string.ktv_caocap_10));
						}	
					}

						break;
					case R.id.hatLai:{
						if(MyApplication.flagOnPopup){
							if(MyApplication.intWifiRemote == MyApplication.SONCA){
								String msg1 = getResources().getString(R.string.confirm_3) + "?";
								String msg2 = "";
								ShowConfirmDialog(msg1, msg2, 4, 0, 0, 0);
							}else{
								String msg1 = getResources().getString(R.string.confirm_3) + "?";
								String msg2 = "";
								ShowConfirmDialog(msg1, msg2, 5, 0, 0, 0);
							}
						} else {
							if(MyApplication.intWifiRemote == MyApplication.SONCA){
								((MyApplication) getApplication()).sendCommand(
										NetworkSocket.REMOTE_CMD_REPEAT, 0);
							}else{
								MyLog.e(TAB, "repeatView : send ok");
								((MyApplication) getApplication()).sendCommandKartrol(
										(byte) RemoteIRCode.IRC_REPEAT,
										0);
							}
							
							displayStatusAction(getResources().getDrawable(R.drawable.ktv_caocap_repeat), getResources().getString(R.string.ktv_main_5));
						}
					}
						break;
					case R.id.chamDiem:{
						int value = viewChamDiem.getScoreView() + 1;
						if(value > 2){
							value = 0;
						}						
						if(MyApplication.intWifiRemote == MyApplication.SONCA){
							((MyApplication) getApplication()).sendCommand(
									NetworkSocket.REMOTE_CMD_SCORE, value);
							
							if(value == 1){			
								displayStatusAction(getResources().getDrawable(R.drawable.ktv_caocap_score), 
										getResources().getString(R.string.ktv_caocap_12) + " : " + getResources().getString(R.string.ktv_caocap_12b));
							} else if(value == 2){
								displayStatusAction(getResources().getDrawable(R.drawable.ktv_caocap_score_pro), 
										getResources().getString(R.string.ktv_caocap_12) + " : " + getResources().getString(R.string.ktv_caocap_12c));
							} else {
								displayStatusAction(getResources().getDrawable(R.drawable.ktv_caocap_score_tat), 
										getResources().getString(R.string.ktv_caocap_12) + " : " + getResources().getString(R.string.ktv_caocap_12a));
							}
							
						}else{
							((MyApplication) getApplication()).sendCommandKartrol(
									(byte) RemoteIRCode.IRC_SCORE,
									0);
							
							displayStatusAction(getResources().getDrawable(R.drawable.ktv_caocap_score), getResources().getString(R.string.ktv_caocap_12));
						}	
					}
						break;
					default:
						break;
					}
				}
			});
			dialogCaoCap.showDialog();
		}
	}
	
	public ViewCaoCap viewMelodyGiam, viewMelodyTang, viewTempoGiam, viewTempoTang, viewTone, viewHoaAm, viewMacDinh;
	public ViewCaoCap viewChinhMiDi, viewNgatTieng, viewTamDung, viewGiamTone, viewTangTone, viewHatLai, viewChamDiem;
	
	private void syncDialogCaoCap(ServerStatus status){
		if(dialogCaoCap != null){
			dialogCaoCap.syncFromServer(status);
		}
	}

	@Override
	public void OnClickData(Song song, String nameFrag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnClickImage(int id, String nameFrag) {
		MyLog.e(TAB, "OnClickImage : " + nameFrag);
		showFragSingerFollow(id);
	}
	
	private boolean isRunningSequence = false;

	@Override
	public void OnFirstClick(Song song, String typeFragment, int postion,
			float x, float y) {
		MyLog.e(TAB, "OnFirstClick : " + song.getName() + " -- " + typeFragment + " -- " +  postion + " -- " + x + " -- " + y);

		if(MyApplication.bOffFirst == false){
			return;
		}
		
		if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
				|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
				|| MyApplication.intSvrModel == MyApplication.SONCA_KBX9 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
				 || MyApplication.intSvrModel == MyApplication.SONCA_TBT){
			song.setTypeABC(0);
		}
		
		if (((MyApplication) getApplication()).getSocket() != null
				&& song != null) {
			if(serverStatus == null){
				showDialogConnect();
				return;
			}
			
			if(!boolShowKaraoke && MyApplication.flagDisplayTimeout){
				showDialogMessage(getResources().getString(R.string.msg_15) + "...\n" + getResources().getString(R.string.msg_15a));
				return;
			}	
			
			if (typeFragment.equals(PLAYLIST) && postion != 0) {
//				MyLog.e(TAB, "OnFirstClick() - " + song.getId() + " -- " + song.getIndex5() + " -- " + song.getTypeABC() + " -- " + postion);
				
				if(postion >= curSongIDs.size()){
					return;
				}
				
				SongID tempSongID = curSongIDs.get(postion);	
				
				if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && song.isYoutubeSong()){
					((MyApplication) getApplication()).firstReservedSongYouTube(song.getId(), song.getTypeABC(), postion);	
				} else {					
					((MyApplication) getApplication()).firstReservedSong(tempSongID.songID, song.getTypeABC(), postion);					
				}	
				
			}
			if (!typeFragment.equals(PLAYLIST)) {				
				if(((MyApplication) KTVMainActivity.this.getApplication()).getListActive().size() == 99){
					showDialogMessage(getResources().getString(R.string.msg_2) + "! " + getResources().getString(R.string.msg_3));				
					return;
				}
				
				boolean flagBlock = ((MyApplication) getApplication())
						.CheckDuplicateFirstPlayList(song.getId() + "", song.getIndex5() + "", song.getTypeABC());
				if(flagBlock){
					String strData = "";
					strData += getString(R.string.pop_msg_add_1);
					strData += " \"" + song.getName() + "\" ";
					strData += getString(R.string.pop_msg_add_2b) + " " + getString(R.string.pop_msg_add_3) + ".";
					
					ShowDialogNoAddPlayList(strData);
					
					if (onKTVMainListener != null) {
						onKTVMainListener.OnSK90009();
					}
					return;
				}
				
				if(!isRunningSequence && !MyApplication.flagOnPopup){
					animationView.stopAnimation();
					animationView.startAnimation(x, y, TouchAnimationView.FRIST);	
				}
				
				if(MyApplication.intWifiRemote == MyApplication.SONCA){
					if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
							|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
							|| MyApplication.intSvrModel == MyApplication.SONCA_KBX9 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
							 || MyApplication.intSvrModel == MyApplication.SONCA_TBT){
						if(MyApplication.flagOnPopup){
							String msg1 = getResources().getString(R.string.confirm_2);
							String msg2 = song.getName();
							ShowConfirmDialog(msg1, msg2, 2, song.getId(), song.getTypeABC(), -1);
						} else {
							((MyApplication) getApplication()).firstReservedSong(
									song.getId(), song.getTypeABC(), -1);
						}
						
					} else {
						if(MyApplication.flagOnPopup){
							String msg1 = getResources().getString(R.string.confirm_2);
							String msg2 = song.getName();
							ShowConfirmDialog(msg1, msg2, 2, song.getIndex5(), song.getTypeABC(), -1);
						} else {
							if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && song.isYoutubeSong()){
								((MyApplication) getApplication()).firstReservedSongYouTube(song.getId(), song.getTypeABC(), -1);	
							} else {
								((MyApplication) getApplication()).firstReservedSong(song.getIndex5(), song.getTypeABC(), -1);	
							}
						}						
					}
				} else {
					if(!isRunningSequence){
						isRunningSequence = true;
						
						final int lastRemoteModel = MyApplication.intRemoteModel;
						if(lastRemoteModel == 5){
							((MyApplication) getApplication()).sendCommandKartrol((byte)RemoteIRCode.IRC_HIDE_MENU, 0);
						}
						
						if(MyApplication.flagOnPopup){
							String msg1 = getResources().getString(R.string.confirm_2);
							String msg2 = song.getName();
							ShowConfirmDialog(msg1, msg2, 3, song.getIndex5(), song.getTypeABC(), -1);
						} else {
							((MyApplication) getApplication()).firstReservedSongKartrol(
									song.getIndex5(), song.getTypeABC(), -1);
						}
						
						runOnUiThread(new Runnable() {
							public void run() {
								new Handler().postDelayed(new Runnable() {
									@Override
									public void run() {
										if(lastRemoteModel == 5){
											((MyApplication) getApplication()).sendCommandKartrol((byte)RemoteIRCode.IRC_HIDE_MENU, 0);
										}
										isRunningSequence = false;
									}
								}, lastRemoteModel == 5 ? 9000 : 2500);
							}
						});
					}		
				}				
			}
		}
	
		
	}

	@Override
	public void OnSingerLink(Song song, String nameFrag) {
		if(song == null){
			return;
		}
		MyLog.e(TAB, "OnSingerLink : " + nameFrag);
		showDialogSingerLink(song);
	}
	
	private long breakYouTube = 0;
	
	@Override
	public void OnPlayYouTube(Song song, String nameFrag) {
		if(song == null){
			return;
		}
		
		if((System.currentTimeMillis() - breakYouTube) < 500){
			return;
		}
		
		breakYouTube = System.currentTimeMillis();
		
		if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && song.isYoutubeSong()){
			showMyPopupAsking(1, song, context.getString(R.string.youtube_xemthu_1a), context.getString(R.string.youtube_xemthu_1b));
			return;
		}
		
		if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && (MyApplication.flagSmartK_801 || MyApplication.flagSmartK_CB) 
				&& song.isOfflineSong() == false){
			showDialogMessage(getResources().getString(R.string.youtube_down_4));
			return;
		}
	}
	
	@Override
	public void OnDownYouTube(Song song, String nameFrag) {
		if(song == null){
			return;
		}
		
		if((System.currentTimeMillis() - breakYouTube) < 500){
			return;
		}
		
		breakYouTube = System.currentTimeMillis();
		
		MyLog.e(TAB, "onDownYouTube -- " + song.getId() + " -- " + song.getName());		
		
		if(MyApplication.flagPlayingYouTube){
			showDialogMessage(context.getString(R.string.youtube_down_3));
			return;
		} 

		if(song.getId() == MyApplication.youtube_Download_ID){
			showMyPopupAsking(3, song, context.getString(R.string.youtube_down_5), context.getString(R.string.youtube_down_6));
		} else {
			showMyPopupAsking(2, song, context.getString(R.string.youtube_down_1), context.getString(R.string.youtube_down_2));	
		}	
		
	}

	@Override
	public void OnSongLick(Song song, String typeFragment, float x, float y) {
		MyLog.e(TAB, "OnSongLick : " + song.getName() + " -- " + typeFragment + " -- " + x + " -- " + y);
				
		if (typeFragment.equals(SONG) || typeFragment.equals(FOLLOW) || typeFragment.equals(HOTSONG)){
			if (serverStatus == null) {
				showDialogConnect();
				return;
			}

			if (!boolShowKaraoke && MyApplication.flagDisplayTimeout) {
				showDialogMessage(getResources().getString(R.string.msg_15) + "...\n" + getResources().getString(R.string.msg_15a));
				return;
			}

			if (MyApplication.intSvrModel == MyApplication.SONCA_HIW
					|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
					|| MyApplication.intSvrModel == MyApplication.SONCA_KM2 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
					|| MyApplication.intSvrModel == MyApplication.SONCA_KBX9
					|| MyApplication.intSvrModel == MyApplication.SONCA_TBT) {
				song.setTypeABC(0);
			}

			if (((MyApplication) KTVMainActivity.this.getApplication())
					.getListActive().size() == 99) {
				showDialogMessage(getResources().getString(R.string.msg_1) + "! " + getResources().getString(R.string.msg_3));
				return;
			}

			boolean flagBlock = ((MyApplication) getApplication())
					.CheckDuplicateLastPlayList(song.getId() + "", song.getIndex5()
							+ "", song.getTypeABC());
			if (flagBlock) {
				String strData = "";
				strData += getString(R.string.pop_msg_add_1);
				strData += " \"" + song.getName() + "\" ";
				strData += getString(R.string.pop_msg_add_2) + " " + getString(R.string.pop_msg_add_3) + ".";

				ShowDialogNoAddPlayList(strData);

				if (onKTVMainListener != null) {
					onKTVMainListener.OnSK90009();
				}
				return;
			}

			if (serverStatus != null && !isRunningSequence
					&& !MyApplication.flagOnPopup) {
				animationView.stopAnimation();
				animationView.startAnimation(x, y, TouchAnimationView.SONG);
			}

			if (MyApplication.intSvrModel == MyApplication.SONCA
					|| MyApplication.intSvrModel == MyApplication.SONCA_HIW
					|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
					|| MyApplication.intSvrModel == MyApplication.SONCA_KM2 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
					|| MyApplication.intSvrModel == MyApplication.SONCA_KBX9
					|| MyApplication.intSvrModel == MyApplication.SONCA_TBT
					|| MyApplication.intSvrModel == MyApplication.SONCA_SMARTK) {
				if (MyApplication.intSvrModel == MyApplication.SONCA_HIW
						|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
						|| MyApplication.intSvrModel == MyApplication.SONCA_KM2 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
						|| MyApplication.intSvrModel == MyApplication.SONCA_TBT) {
					if (MyApplication.flagOnPopup) {
						String msg1 = getResources().getString(R.string.confirm_1);
						String msg2 = song.getName();
						ShowConfirmDialog(msg1, msg2, 0, song.getId(),
								song.getTypeABC(), 0);
					} else {
						((MyApplication) getApplication()).addSongToPlaylist(song.getId(), song.getTypeABC(), 0);
					}

				} else {
					if (MyApplication.flagOnPopup) {
						String msg1 = getResources().getString(R.string.confirm_1);
						String msg2 = song.getName();
						ShowConfirmDialog(msg1, msg2, 0, song.getIndex5(),
								song.getTypeABC(), 0);
					} else {
						if (MyApplication.intSvrModel == MyApplication.SONCA_SMARTK
								&& song.isYoutubeSong()) {
							MyLog.d(TAB, "Add YOUTUBE SONG = " + song.getId()
									+ " -- socketTypeABC = " + song.getTypeABC());
							((MyApplication) getApplication()).addSongToPlaylistYouTube(song.getId(), song.getTypeABC(), 0);
						} else {
							((MyApplication) getApplication()).addSongToPlaylist(song.getIndex5(), song.getTypeABC(), 0);
						}

					}

				}
			} else {
				if (!isRunningSequence) {
					isRunningSequence = true;

					final int lastRemoteModel = MyApplication.intRemoteModel;
					if (lastRemoteModel == 5) {
						((MyApplication) getApplication()).sendCommandKartrol(
								(byte) RemoteIRCode.IRC_HIDE_MENU, 0);
					}

					if (MyApplication.flagOnPopup) {
						String msg1 = getResources().getString(R.string.confirm_1) + " '" + song.getName() + "'?";
						String msg2 = song.getName();
						ShowConfirmDialog(msg1, msg2, 1, song.getIndex5(), song.getTypeABC(), 0);
					} else {
						((MyApplication) getApplication()).addSongToPlaylistKartrol(song.getIndex5(),song.getTypeABC(), 0);
					}

					runOnUiThread(new Runnable() {
						public void run() {
							new Handler().postDelayed(new Runnable() {
								@Override
								public void run() {
									if (lastRemoteModel == 5) {
										((MyApplication) getApplication()).sendCommandKartrol(
														(byte) RemoteIRCode.IRC_HIDE_MENU, 0);
									}
									isRunningSequence = false;
								}
							}, lastRemoteModel == 5 ? 9000 : 2500);
						}
					});
				}
			}
						
		}

	}

	@Override
	public void OnCreateFrag(String namefrag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnDeleteClick(Song song, String nameFrag, int position) {
		MyLog.e(TAB, "OnDeleteClick : " + song.getName() + " -- position = " + position);
		if(position >= curSongIDs.size()){
			return;
		}
		SongID tempSongID = curSongIDs.get(position);
		
		if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
				|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
				|| MyApplication.intSvrModel == MyApplication.SONCA_KBX9 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
				 || MyApplication.intSvrModel == MyApplication.SONCA_TBT){
			((MyApplication) getApplication()).removeSongfromPlaylist(tempSongID.songID, 0, position);
		} else {
			((MyApplication) getApplication()).removeSongfromPlaylist(tempSongID.songID, song.getTypeABC(), position);
		}
	}
	
	
	// TODO BREAK A KHIEM
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	// TODO BREAK HMINH
	
	

	private boolean isDestroyMainActivity = false;
	
	public static final String MUSICIAN = "MUSICIAN";
	public static final String SONGTYPE = "SONGTYPE";
	public static final String LANGUAGE = "LANGUAGE";
	public static final String FAVOURITE = "FAVOURITE";
	public static final String SONGFOLLOW = "SONGFOLLOW";
	public static final String NOTHING = "NOTHING";
	public static final String REMIX = "REMIX";
	public static final String HOTSONG = "HOTSONG";
	public static final String NEWVOL = "NEWVOL";

	public static final String LIST_DEVICE = "LIST_DEVICE";
	public static final String ONE_DEVICE = "ONE_DEVICE";
	public static final String ONE_HELLO = "ONE_HELLO";
	public static final String ONE_ADMIN = "ONE_ADMIN";
	public static final String ONE_MODEL = "ONE_MODEL";
	public static final String SELECTLIST = "SELECTLIST";
	private String SAVEDEVICE = "";

	private TouchDownloadView downloadView;
	private TouchAnimationView animationView;
//	private TouchMyGroupView groupView;

	private RelativeLayout layoutMain;
	private LinearLayout layoutDownload;
	private LinearLayout layoutAnimation;
	private LinearLayout layoutConnect;
	private TouchMyDrawerLayout drawerLayout;
	
	private RelativeLayout layoutColorLyric;
	private TouchDanceLinkView danceLinkView;

	public static ServerStatus serverStatus = null;

	public ServerStatus getServerStatus() {
		return this.serverStatus;
	}
	
	public EditText editDong1;
	public EditText editDong2;
	public boolean flagRunHide = true;

	private OnMainKeyboardListener mainKeyboardListener;
	private OnMainListener mainListener;	
	private OnToHelloListener onToHelloListener;
	private OnClearTextListener textListener;
	private OnListDeviceListener listDeviceListener;
	private ToDeviveFragment deviveFragmentListener;

	protected void copyFromResource(int resid, String topath) {
		InputStream fdstream = getResources().openRawResource(resid);
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try {
			bis = new BufferedInputStream(fdstream);
			bos = new BufferedOutputStream(new FileOutputStream(topath));

			byte[] buf = new byte[1024];
			bis.read(buf);
			do {
				bos.write(buf);
			} while (bis.read(buf) != -1);
		} catch (IOException e) {
		} finally {
			try {
				if (bis != null)
					bis.close();
				if (bos != null)
					bos.close();
			} catch (IOException e) {
			}
		}
	}
	
	private boolean isFirst = false;
	
	private long hoaAmTime = 0;
	private String IpDeviceConnect=null;
	@Override
	protected void onStart() {
		super.onStart();
		isDestroyMainActivity = true;
		layoutConnect.setOnClickListener(null);	
	}

	final String PREFS_FLAGHDD = "EverConnectHDD";
	
	@Override
	protected void onResume() {
		MyLog.e("ONE RESUME","........................");
		super.onResume();
		
		new Timer().schedule(new TimerTask() {
			@Override public void run() {
				
				runOnUiThread(new Runnable() {
					public void run() {
						SharedPreferences settingFlagConnect = getSharedPreferences(PREFS_FLAGHDD, 0);
						MyApplication.flagEverConnectHDD = settingFlagConnect.getBoolean("isEverConnect", false);		
						MyApplication.flagEverConnect = settingFlagConnect.getBoolean("isEverConnect2", false);
						
						SharedPreferences sharedselect = getSharedPreferences("sharedselect", Context.MODE_PRIVATE);
						MyApplication.intSelectList = sharedselect.getInt("intselect", 0);		
								
						AppSettings setting = AppSettings.getInstance(getApplicationContext());
						MyApplication.flagEverKBX9 = setting.isEverConnectKBX9();
						MyApplication.flagOnPopup = setting.getPopupSetting();
						MyApplication.flagOnWifiVideo = setting.getWIFIVideoSetting();
						MyApplication.flagOnAdminYouTube = setting.getAdminYouTube();
						MyApplication.flagOnAdminOnline= setting.getAdminOnline();				
						MyApplication.switchTime = setting.getSwitchTime();
						
						MyApplication.freezeTime = System.currentTimeMillis();
						startTimerFreeze();
						
						isScanning = false;
						hideActionBar();
						if(editDong1 != null && editDong2 !=null){
							((MyApplication)getApplication()).hideVirtualKeyboard(editDong1);
							((MyApplication)getApplication()).hideVirtualKeyboard(editDong2);
						}	
						isDestroyMainActivity = true;
						if (((MyApplication) getApplication()).getSocket() == null) {
							((MyApplication) getApplication()).onStart();
							isShowConnectSuccess = false;
						}else{
							serverStatus = null;
							if (MyApplication.intWifiRemote != MyApplication.SONCA) {
								((MyApplication) getApplication()).setListActive(new ArrayList<Song>());
								serverStatus = new ServerStatus();
							}
							SKServer skServer = ((MyApplication) getApplication())
									.getDeviceCurrent();
							if(skServer != null){
								skServer.setState(SKServer.CONNECTED);
								layoutAnimation.setVisibility(View.VISIBLE);
								if (viewDevice != null) {
									viewDevice.setNameDevice(skServer.getName());	
								}								
							}
							if (flagRunHide) {
								hideActionBar();
							}

							if (drawerLayout.isDrawerOpen(layoutConnect)) {
								drawerLayout.closeDrawer(layoutConnect);
							}
							if (listDeviceListener != null) {
								listDeviceListener.OnShowListDevice();
							}
						}		
						
						((MyApplication) getApplication()).setOnApplicationListener(KTVMainActivity.this);
						
						boolean hasLast = getLastConnectedServer(); 
						if(hasLast){
							if (isFirst) {
								onUpdateAllFromServer();
								isFirst = false;
							}
						} else {
							if(MyApplication.countStartApp < 2){ // start app first time
								onUpdateAllFromServer();
							} 
							MyApplication.countStartApp++;
						}
						
						// starting wifi value
						final WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
						int numberOfLevels = 5;
						WifiInfo wifiInfo = wifi.getConnectionInfo();
						int level = WifiManager.calculateSignalLevel(wifiInfo.getRssi(),
								numberOfLevels);
						MyApplication.levelWifi = level;
						if (viewDevice != null) {
							viewDevice.invalidate();
						}
						
						try {
							if(colorLyricListener != null){
								colorLyricListener.OnMain_CallVideoDefault();
							}
							
				    		KTVMainActivity.this.registerReceiver(mWifiInfoReceiver, 
				    			    new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));	
						} catch (Exception e) {
							
						}   
						
						startTimerPing();
					}
					
				});
				
			}
		}, 500);
		
		
	}

	@Override
	protected void onPause() {
		super.onPause();
		MyLog.e(TAB, "               ");
		stopTimerFreeze();
		isDestroyMainActivity = false;
		removeAutoConnectDevice();
		stopTimerPing();
	}

	@Override
	protected void onStop() {
		super.onStop();
		isDestroyMainActivity = false;
		animationView.stopAnimation();
		
		try {
			this.unregisterReceiver(this.mWifiInfoReceiver);	
		} catch (Exception e) {
			
		}	
		
		stopTimerPing();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// MyLog.e(TAB, "onDestroy()");
		isDestroyMainActivity = false;
		if (MyApplication.enableSamba)
			stopService(new Intent(this, StreamService.class));// samba

		if (serviceIntent != null) {
			KTVMainActivity.this.stopService(serviceIntent);
			if (MyApplication.updateFirmwareServerSocket != null) {
				try {
					MyApplication.updateFirmwareServerSocket.close();
				} catch (Exception e) {

				}
			}
		}
		stopTimerPing();
	}
	
	private void hideActionBar(){
		int apiLevel = android.os.Build.VERSION.SDK_INT;
		if(apiLevel >= 19){
			int mUIFlag =
					View.GONE
					| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
					| View.SYSTEM_UI_FLAG_IMMERSIVE;
			if (getWindow().getDecorView().getSystemUiVisibility() != mUIFlag) {
				getWindow().getDecorView().setSystemUiVisibility(mUIFlag);
			}
		}else{
			int mUIFlag = View.GONE;
			if (getWindow().getDecorView().getSystemUiVisibility() != mUIFlag) {
				getWindow().getDecorView().setSystemUiVisibility(mUIFlag);
			}
		}
	}

	private void SetKeepScreenOn(boolean keepScreenOn) {
		if (keepScreenOn) {
			getWindow()
					.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		} else {
			getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		}
	}
	
	
	private void ChangeWifiRmote(){
		processSwitchDB(MyApplication.intWifiRemote);
		
		while (isSwitchingDB) {
			try {
				Thread.sleep(100);
			} catch (Exception e) {
				
			}
		}		
		
		viewDevice.invalidate();
		
		viewWifi.invalidate();
		viewQuaBai.invalidate();
		viewTachLoi.invalidate();
		viewGiamAm.invalidate();
		viewTangAm.invalidate();
		
		if(viewChinhMiDi != null){
			viewHatLai.invalidate();
			viewChinhMiDi.invalidate();
			
			viewNgatTieng.invalidate();
			viewTamDung.invalidate();
			viewGiamTone.invalidate();
			viewTangTone.invalidate();
			viewDance.invalidate();
			
			viewMelodyGiam.invalidate();
			viewMelodyTang.invalidate();
			viewTempoGiam.invalidate();
			viewTempoTang.invalidate();
			viewTone.invalidate();
			viewHoaAm.invalidate();
			viewMacDinh.invalidate();
		}		
	}	
	
	// /////////////////////////// - FRAGMENT -
	// ////////////////////////////////////
	
	public void ShowAdmin(String Name, String IP, int flag){
		if (isDestroyMainActivity == false) {
			return;
		}	
		if(serverStatus == null){
			Toast.makeText(getApplicationContext(), 
					R.string.hello_9, Toast.LENGTH_SHORT).show();
			return;
		}	
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		if (fragmentDevice != null) {
			fragmentTransaction.remove(fragmentDevice);
		}
		fragmentDevice = new TouchAdminFragment();
		Bundle bundle = new Bundle();
		bundle.putString("Name", Name);
		bundle.putString("IP", IP);
		bundle.putInt("layout", flag);
		fragmentDevice.setArguments(bundle);
		fragmentDevice.setArguments(bundle);
		fragmentTransaction.replace(R.id.fragmentConnect, fragmentDevice,
				ONE_ADMIN);
		fragmentTransaction.commit();
		SAVEDEVICE = ONE_ADMIN;
	
	}

	Fragment fragmentDevice = null;

	private void ShowListDevice(String IP) {
		if (isDestroyMainActivity == false) {
			return;
		}
		if (SAVEDEVICE.equals(LIST_DEVICE)) {
			return;
		}
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		if (fragmentDevice != null) {
			fragmentTransaction.remove(fragmentDevice);
		}
		fragmentDevice = new TouchListDeviceFragment();
		listDeviceListener = (OnListDeviceListener) fragmentDevice;
		Bundle bundle = new Bundle();
		bundle.putString("IP", IP);
		bundle.putBoolean("Connect", serverStatus != null);
		fragmentDevice.setArguments(bundle);
		fragmentTransaction.replace(R.id.fragmentConnect, fragmentDevice,
				LIST_DEVICE);
		fragmentTransaction.commit();
		SAVEDEVICE = LIST_DEVICE;
	}

	private void ShowDevice(String Name, String IP) {
		if (isDestroyMainActivity == false) {
			return;
		}
		if (SAVEDEVICE.equals(ONE_DEVICE)) {
			return;
		}
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		if (fragmentDevice != null) {
			fragmentTransaction.remove(fragmentDevice);
		}
		fragmentDevice = new TouchDeviceFragment();
		Bundle bundle = new Bundle();
		bundle.putString("Name", Name);
		bundle.putString("IP", IP);
		fragmentDevice.setArguments(bundle);
		deviveFragmentListener = (TouchDeviceFragment) fragmentDevice;
		fragmentTransaction.replace(R.id.fragmentConnect, fragmentDevice,
				ONE_DEVICE);
		fragmentTransaction.commit();
		SAVEDEVICE = ONE_DEVICE;
	}
	
	public void ShowHello(){
		if (isDestroyMainActivity == false) {
			return;
		}
		if(serverStatus == null){
			Toast.makeText(getApplicationContext(), 
					R.string.hello_9, Toast.LENGTH_SHORT).show();
			return;
		}
		if (serverStatus != null) {
			if (!serverStatus.isCaptionAPIValid()) {
				makeToastMessage(getResources().getString(R.string.hello_11));
				return;
			}
		}
		if (SAVEDEVICE.equals(ONE_HELLO)) {
			return;
		}
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		if (fragmentDevice != null) {
			fragmentTransaction.remove(fragmentDevice);
		}
		SAVEDEVICE = ONE_HELLO;
		fragmentDevice = new TouchHelloFragment();
		
		Bundle bundle = new Bundle();
		bundle.putBoolean("Connect", serverStatus != null);
		fragmentDevice.setArguments(bundle);
		onToHelloListener = (OnToHelloListener) fragmentDevice;
		fragmentTransaction.replace(R.id.fragmentConnect, fragmentDevice,
				ONE_HELLO);
		fragmentTransaction.commit();
	}

	public void OnMyShowLeftDrawer() {	
		if (serverStatus == null) { // no connect
			ShowListDevice("");
			drawerLayout.openDrawer(layoutConnect);
			if (listDeviceListener != null && !isScanning) {
				MyLog.e(TAB, "OnShowConnect()");
				((MyApplication) getApplication()).setListServers(null);
				((MyApplication) getApplication()).searchNearbyDevice();
				listDeviceListener.OnDisplayProgressScan(true);
				isScanning = true;
			}
		} else { // connecting
			
			if(MyApplication.intWifiRemote != MyApplication.SONCA_KARTROL){
				if (serverStatus != null) {
					if (!serverStatus.isCaptionAPIValid()) {
						MyApplication.flagSupportOffUser = false;
						ShowListDevice("");
						drawerLayout.openDrawer(layoutConnect);
						if (listDeviceListener != null && !isScanning) {
							MyLog.e(TAB, "OnShowConnect()");
							((MyApplication) getApplication()).setListServers(null);
							((MyApplication) getApplication()).searchNearbyDevice();
							listDeviceListener.OnDisplayProgressScan(true);
							isScanning = true;
						}
						return;
					}
				}
			}
			
			((MyApplication)getApplication()).getAdminPass();
			((MyApplication)getApplication()).setOnReceiverAdminPassListener(
					new OnReceiverAdminPassListener() {
				
				@Override
				public void OnReceiverAdminPass(String adminPass) {
					if(adminPass == null){ // old format SK9xxx
						MyApplication.flagSupportOffUser = false;
						ShowListDevice("");
						drawerLayout.openDrawer(layoutConnect);
						if (listDeviceListener != null && !isScanning) {
							MyLog.e(TAB, "OnShowConnect()");
							((MyApplication) getApplication()).setListServers(null);
							((MyApplication) getApplication()).searchNearbyDevice();
							listDeviceListener.OnDisplayProgressScan(true);
							isScanning = true;
						}
					} else if(adminPass.equals("DIS")){
						MyLog.e("OnShowConnect", "DIS");
						DefaultViewWhenDisConect();
					} else { // new format - password admin
						MyApplication.flagSupportOffUser = true;
						SKServer skServer = ((MyApplication)getApplication()).getDeviceCurrent();
						if(skServer != null){
							String name = skServer.getName();
							String ip = skServer.getConnectedIPStr();
							ShowAdmin(name, ip, TouchDeviceAdmin.LEFT_SLIDER);
							drawerLayout.openDrawer(layoutConnect);
						}	
					}
					((MyApplication)getApplication()).setOnReceiverAdminPassListener(null);
				}
			});
		}
	}
	
	// ---------------------ListDeviceFragment-----------------------//

	@Override
	public void OnSetupWifi() {
		MyLog.e(TAB, "--- OnSetupWifi ---");
		DefaultViewWhenPause();
		startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
	}
	
	@Override
	public void OnBackListDevice() {
		drawerLayout.closeDrawer(layoutConnect);
	}
	
	@Override
	public void OnChangeHello() {
		if(serverStatus == null){
			return;
		}
		
		if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
				|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
				|| MyApplication.intSvrModel == MyApplication.SONCA_KBX9 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
				 || MyApplication.intSvrModel == MyApplication.SONCA_TBT){
			SKServer skServer = ((MyApplication)getApplication()).getDeviceCurrent();
			if(skServer != null){
				String name = skServer.getName();
				String ip = skServer.getConnectedIPStr();
				ShowAdmin(name, ip, TouchDeviceAdmin.HELLO);
			}	
			return;
		}
		
		if(!MyApplication.flagSupportOffUser){
			ShowHello();
		} else {
			SKServer skServer = ((MyApplication)getApplication()).getDeviceCurrent();
			if(skServer != null){
				String name = skServer.getName();
				String ip = skServer.getConnectedIPStr();
				ShowAdmin(name, ip, TouchDeviceAdmin.HELLO);
			}	
		}
		
	}
	
	@Override
	public void OnChangeFlagOffUser() {
		// Chua connect dau may
		if (serverStatus == null) {
			return;
		}
		
		if(MyApplication.intWifiRemote == MyApplication.SONCA_KARTROL){
			makeToastMessage(getResources().getString(R.string.hello_11));
			return;
		}
		
		// Dau may chua support chuc nang nay
		if (!MyApplication.flagSupportOffUser) {
			makeToastMessage(getResources().getString(R.string.hello_11));
			return;
		}	
		
		SKServer skServer = ((MyApplication)getApplication()).getDeviceCurrent();
		if(skServer != null){
			String name = skServer.getName();
			String ip = skServer.getConnectedIPStr();
			ShowAdmin(name, ip, TouchDeviceAdmin.USER);
		}
	}

	private boolean isScanning = false;
	private boolean autoAfterScan = false; 
	
	@Override
	public void OnRefrresh() {
		flagAllowConnect = true;	
		isShowConnectSuccess = true;
		if (((MyApplication) getApplication()).getSocket() != null) {
			if (listDeviceListener != null && !isScanning) {
				isScanning = true;
				MyLog.e(TAB, "OnRefrresh() NOT NULL");
				((MyApplication) getApplication()).setListServers(null);
				listDeviceListener.OnDisplayProgressScan(true);
				((MyApplication) getApplication()).searchNearbyDevice();			
			}
		}else{
			MyLog.e(TAB, "OnRefrresh() IS NULL");
		}
	}

	@Override
	public void OnAddDevice() {
		ShowDevice("", "");
	}
	
	@Override
	public void OnShowConnect(SKServer skServer) {
		if(skServer == null){
			return;
		}
		String ip = skServer.getConnectedIPStr();
		String pass = skServer.getConnectPass();
		String name = skServer.getName();
		if(skServer.getModelDevice() == MyApplication.SONCA_KARTROL || skServer.getModelDevice() == MyApplication.SONCA_KM1){
			pass = "1234";
		}
		if (name != null && ip != null) {
			boolean issave = skServer.isSave();
			if (issave) {				
				ShowDevice(name , ip);
			} else {
				ShowDevice(name , ip);
			}
		}
	}

	// ---------------------DeviceFragment-----------------------//

	@Override
	public void OnConnect(String IP) {
		MyLog.e(TAB, "OnConnect - " + IP);
		SKServer skServer = new SKServer();
		skServer.setConnectedIPStr(IP);
		skServer.setName("");
		((MyApplication) getApplication()).setDeviceCurrent(skServer);
		((MyApplication) getApplication()).disconnectFromRemoteHost();
		((MyApplication) getApplication()).connectToRemoteHost(IP);
	}

	@Override
	public void OnDeviceBackLayout() {
		flagAllowConnect = true;
		
		if(serverStatus==null){
			((MyApplication) getApplication()).disconnectFromRemoteHost();	
		}
		
		ShowListDevice("");
	}

	@Override
	public void OnSendPass(String pass) {
		((MyApplication) getApplication()).getDeviceCurrent().setConnectPass(
				pass);
		((MyApplication) getApplication()).authenToRemoteHost(pass);
		isShowConnectSuccess = true;
	}

	@Override
	public void OnConectIpPass(final String IP, final String Pass, final String name) {
		MyLog.e(" ", " ");
		MyLog.e(TAB, "OnConectIpPass(" + IP + "," + Pass + ")");
		
		if (IP.equals("2.0.1.5") && Pass.equals("8888")) {
			AppSettings setting = AppSettings
					.getInstance(getApplicationContext());
			setting.saveServerLastUpdate(1, 1);
			setting.saveServerLastUpdate(0, 2);
			setting.saveServerLastUpdate(0, 3);
			setting.saveServerLastUpdate(0, 4);
			
			AppSettings settings = AppSettings.getInstance(getApplicationContext());
			settings.saveYouTubeVersion(0);
			settings.saveYouTubeVersion_SK90xx(0);
			settings.saveUpdateTOCVersion(0);
			settings.saveListOfflineVersion(0);
			settings.saveListOfflineVersion_SK90xx(0);
			settings.saveLuckyDataVersion(0);
			settings.saveLuckyImageVersion(0);

			((MyApplication) getApplication()).disconnectFromRemoteHost();
			drawerLayout.closeDrawers();
			DefaultViewWhenDisConect();
			return;
		}
		if (IP.equals("2.0.1.5") && Pass.equals("4444")) {
			AppSettings setting = AppSettings
					.getInstance(getApplicationContext());
			setting.saveHiwLastUpdate(0);
			Toast.makeText(this, "Done!", Toast.LENGTH_LONG).show();
			return;
		}
		if (IP.equals("2.0.1.5") && Pass.equals("0412")) {
			AppSettings setting = AppSettings
					.getInstance(getApplicationContext());
			Toast.makeText(this, "Current TOC Version: "
					+ setting.loadServerLastUpdate(1) + "-"
					+ setting.loadServerLastUpdate(2) + "-"
					+ setting.loadServerLastUpdate(3) + "-"
					+ setting.loadServerLastUpdate(4),
					Toast.LENGTH_LONG).show();
			return;
		}
		if (IP.equals("1.3.0.1") && !Pass.equals("")) {
			final String PREFS_NAME = "UpdatePicFile";
			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
			settings.edit().putInt("saveVersion", Integer.parseInt(Pass))
					.commit();
			
			Toast.makeText(this, "Save version Picture Done!",
					Toast.LENGTH_LONG).show();
			return;
		}
		if (IP.equals("1.3.0.2") && !Pass.equals("")) {
			final String PREFS_NAME = "UpdatePicFile";
			SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
			int tempInt = settings.getInt("saveVersion", Integer.parseInt(Pass));
			
			Toast.makeText(this, "Current Version Picture = " + tempInt,
					Toast.LENGTH_LONG).show();
			return;
		}
		if (IP.equals("1.5.0.0.1") && Pass.equals("8888")) {
			processParseTocFromAsset();
			return;
		}
		
		if (IP.equals("1.6.1.0.0") && !Pass.equals("")) {
			setOnOffUserList(Pass, !MyApplication.bOffUserList);
			return;				
		}
		
		if (IP.equals("1.6.1.0.2") && Pass.equals("8888")) {
			int w = getResources().getDisplayMetrics().widthPixels;
			int h = getResources().getDisplayMetrics().heightPixels;
			float result = (float)w/h;
			Toast.makeText(this, "w/h ==== " + result, Toast.LENGTH_LONG)
					.show();
			return;
		}
		
		if (IP.equals("1.6.5.0.2") && Pass.equals("8888")) {
			drawerLayout.closeDrawers();
			MyApplication.intCommandMedium = 0x00000000;
			MyApplication.intSavedCommandMedium = 0x00000000;
			MyApplication.intCommandEnable = 0xffff;
			MyApplication.flagModelA = false;
			setOnOffAdminControl();
			return;
		}
		
		if (IP.equals("1.8.0.0.1") && Pass.equals("8888")) {
			drawerLayout.closeDrawers();
			startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER));
			return;
		}
		
		if (IP.equals("1.8.0.0.2") && !Pass.equals("")) {
			drawerLayout.closeDrawers();
			MyApplication.intCommandMedium = Integer.parseInt(Pass);
			setOnOffControlFull();
			return;
		}
		
		if (IP.equals("1.8.5.0.1") && Pass.equals("8888")) {
			MyApplication.flagEverConnectHDD = !MyApplication.flagEverConnectHDD;
			SharedPreferences settingFlagConnect = getSharedPreferences(
					PREFS_FLAGHDD, 0);
			settingFlagConnect.edit().putBoolean("isEverConnect", MyApplication.flagEverConnectHDD)
					.commit();
			
			StoreLyric storeLyric = new StoreLyric(getApplicationContext());
			if(MyApplication.flagEverConnectHDD){
				storeLyric.saveLyricHDD(true);	
			} else {
				storeLyric.saveLyricHDD(false);
			}	
			
			makeToastMessage("SET ON EVER HDD = " + MyApplication.flagEverConnectHDD);
			return;
		}
		if (IP.equals("1.9.0.0.2") && !Pass.equals("")) {
			isDownloadingPic = false;
			isDownloadingToc = false;
			isDownloadingLyric = false;
			makeToastMessage("reset all downloading state");
			return;
		}
		if (IP.equals("1.9.5.0.1") && !Pass.equals("")) {
			int value = Integer.parseInt(Pass);
			MyApplication.setCommandMediumScoreMethod(value);
			setOnOffControlFull();
			makeToastMessage("set score method = " + value);
			return;
		}
		if (IP.equals("1.9.5.0.5") && Pass.equals("8888")) {
			MyApplication.flagEverConnect = !MyApplication.flagEverConnect;
			SharedPreferences settingFlagConnect = getSharedPreferences(
					PREFS_FLAGHDD, 0);
			settingFlagConnect.edit().putBoolean("isEverConnect2", MyApplication.flagEverConnect)
					.commit();
			makeToastMessage("SET ON EVER CONNECT = " + MyApplication.flagEverConnect);
			return;
		}
		
		if (IP.equals("1.9.5.0.3") && Pass.equals("8888")) {
			forceUpdatingFirmware();
			return;
		}
		
		if (IP.equals("1.9.8.0.2") && Pass.equals("8888")) {
			drawerLayout.closeDrawers();
			SaveFirmware saveFirmware = SaveFirmware.getInstance(context);
			saveFirmware.clearVersionFirmware();
			makeToastMessage("reset firmware version");
			return;
		}
		
		if (IP.equals("1.9.8.0.1") && Pass.equals("8888")) {
			SaveFirmware saveFirmware = SaveFirmware.getInstance(context);
			String str = saveFirmware.getVersionFirmwareHiW() + " -- " + saveFirmware.getRevisionFirmwareHiW() + " \n" +
					saveFirmware.getVersionFirmwareKM1() + " -- " + saveFirmware.getRevisionFirmwareKM1();
			makeToastMessage(str);
			return;
		}
		
		if (IP.equals("2.1.0.0.2") && Pass.equals("8888")) {
			if(drawerLayout != null){
				drawerLayout.closeDrawers();
			}
			
			onUpdateVideoLyric(1);
			return;
		}
		
		if (IP.equals("2.1.0.0.3") && Pass.equals("8888")) {
			AppSettings setting = AppSettings
					.getInstance(getApplicationContext());
			setting.saveVideoLyricSize(0);		
			makeToastMessage("Reset Video size");
			return;
		}
		
		if (IP.equals("2.1.1.0.1") && Pass.equals("8888")) {
			processGetScoreInfo();		
			return;
		}
		
		if (IP.equals("2.1.3.0.1") && Pass.equals("8888")) {
			processNewSongTable();	
			reloadSongList();	
			makeToastMessage("processNewSongTable");
			return;
		}
		
		if (IP.equals("2.1.3.0.2") && Pass.equals("8888")) {
			MyApplication.flagNewSongTable = !MyApplication.flagNewSongTable;
			reloadSongList();		
			makeToastMessage("flagNewSongTable = " + MyApplication.flagNewSongTable);
			return;
		}
		
		if (IP.equals("2.1.3.0.3") && !Pass.equals("")) {
			MyApplication.bOffUserList = !MyApplication.bOffUserList;
			reloadSongList();		
			makeToastMessage("bOffUserList = " + MyApplication.bOffUserList);
			return;
				
		}
		
		if (IP.equals("2.2.1.0") && Pass.equals("8888")) {
			processGetLyricVidLink();
			return;
		}
		
		if(drawerLayout != null){
			drawerLayout.closeDrawers();
		}
		
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				SKServer skServer = new SKServer();
				skServer.setConnectedIPStr(IP);
				skServer.setConnectPass(Pass);
				skServer.setName(name);
				((MyApplication) getApplication()).setDeviceCurrent(skServer);
				((MyApplication) getApplication()).disconnectFromRemoteHost();
				((MyApplication) getApplication()).connectToRemoteHost(IP, Pass);
				isShowConnectSuccess = true;
			}
		}, 500);
	}
	
	private void processNewSongTable(){
		try {
			if(MyApplication.flagNewSongTable){
				MyLog.d(" ", " ");
				MyLog.d(TAB, "processNewSongTable VERY START");
				DBInterface.DBProcessNewSongTable(context);
				MyLog.d(TAB, "processNewSongTable VERY END");	
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void processParseTocFromAsset() {
		try {
			String rootPath = "";

			if (android.os.Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED)) {
				rootPath = android.os.Environment
						.getExternalStorageDirectory()
						.toString()
						.concat(String.format("/%s/%s", "Android/data",
								getPackageName()));
				File appBundle = new File(rootPath);
				if (!appBundle.exists())
					appBundle.mkdir();
			}

			final String picDir = rootPath.concat("/PICTURE");
			File picDirFile = new File(picDir);
			if (!picDirFile.exists()) {
				picDirFile.mkdir();
			}

			String savePath1 = rootPath.concat("/MEGIDX");
			String lyricPath = rootPath.concat("/LYRICS");

			final File updateFile = new File(savePath1);
			if (updateFile.exists()) {
				updateFile.delete();
			}
			final File lyricFile = new File(lyricPath);
			if (lyricFile.exists()) {
				lyricFile.delete();
			}

			AssetManager assetMgr = this.getAssets();
			InputStream is = assetMgr.open("MEGIDX");
			OutputStream os = new FileOutputStream(savePath1);
			copyFile(is, os);
			is.close();
			os.flush();
			os.close();

			is = assetMgr.open("FIRST10W");
			os = new FileOutputStream(lyricPath);
			copyFile(is, os);
			is.close();
			os.flush();
			os.close();

			final String cpRightPath = rootPath.concat("/DB/CPRIGHT");
			
			SmartKaraoke importDB = new SmartKaraoke();
			importDB.importdata(getDatabasePath(DbHelper.DBName)
					.getAbsolutePath(), savePath1, lyricPath, cpRightPath);

			reloadYoutubeTable();
			
			AppSettings setting = AppSettings
					.getInstance(getApplicationContext());
			setting.saveServerLastUpdate(MyApplication.intPackageSK9x_DISC, 1);
			setting.saveServerLastUpdate(0, 2);
			setting.saveServerLastUpdate(MyApplication.intPackageSK9x_HDD, 3);
			setting.saveServerLastUpdate(0, 4);

			DBInterface.createNewIndexSong(context);
			
			Toast.makeText(this, "Parse from assest done!",
					Toast.LENGTH_LONG).show();
		} catch (Exception e) {

		}
	}

	// /////////////////////////////// - SERVER -
	// //////////////////////////////////

	private boolean getLastConnectedServer() {
		AppSettings setting = AppSettings.getInstance(getApplicationContext());
		String connectedIP = setting.loadServerIP();
		if (connectedIP.length() == 0)
			return false;
		return true;
	}

	private int DownloadState = 0;
	private int authenId = 0;
	private int lastUpdate1 = 0;
	private int lastUpdate2 = 0;
	private int lastUpdate3 = 0;
	private int lastUpdate4 = 0;

	private boolean isNeedUpdateDauMay(int check1, int check2, int check3,
			int check4) {
		AppSettings settings = AppSettings.getInstance(getApplicationContext());
		if (check1 > settings.loadServerLastUpdate(1)
				|| check2 > settings.loadServerLastUpdate(2)
				|| check3 > settings.loadServerLastUpdate(3)
				|| check4 > settings.loadServerLastUpdate(4)) {
			return true;
		}
		return false;
	}
	
	private boolean isNeedUpdateDauMayDif(int check1, int check2, int check3,
			int check4) {
		AppSettings settings = AppSettings.getInstance(getApplicationContext());
		if (check1 != settings.loadServerLastUpdate(1)
				|| check2 != settings.loadServerLastUpdate(2)
				|| check3 != settings.loadServerLastUpdate(3)
				|| check4 != settings.loadServerLastUpdate(4)) {
			return true;
		}
		return false;
	}

	private boolean isNeedUpdateTocFlag = false;
	private int saveSkipUpdate1 = 0;
	private int saveSkipUpdate2 = 0;
	private int saveSkipUpdate3 = 0;
	private int saveSkipUpdate4 = 0;

	private int intIndexType;
	public static int intTocHDDVol = 0;
	public static int intTocDISCVol = 0;
	public static int intTocHDDType = 0;
	
	private boolean flagCheckDataMIDI = false;
	
	@Override
	public void deviceDidAuthenWithServer(ServerPackage response, int svrModel, int codeVersion) {
		MyLog.e(TAB, "deviceDidAuthenWithServer()");
		if (response == null || response.getStatus() != 0) {
			MyLog.e("", "Device Authenticate with server failed");
			if (response != null) {
			}			
			drawerLayout.openDrawer(layoutConnect);
			layoutDownload.setVisibility(View.GONE);
			layoutAnimation.setVisibility(View.VISIBLE);
			animationView.ShowMessage(TouchAnimationView.PASS);
			SKServer skServer = ((MyApplication)getApplication()).getDeviceCurrent();
			if(skServer != null){
				ShowDevice(skServer.getName(), skServer.getConnectedIPStr());
			}
			((MyApplication)getApplication()).setDeviceCurrent(new SKServer());
			return;
		}
		
		MyApplication.flagSmartK_CB = false;
		MyApplication.flagSmartK_801 = false;
		MyApplication.flagSmartK_KM4 = false;
		
		saveListOfflineVersion = 0;
		saveYTVersion = 0;
		isMainNeedUpdate = false;
		
		flagAllowConnect = false;
		
		if(!MyApplication.flagEverConnect){
			SharedPreferences settingFlagConnect = getSharedPreferences(
					PREFS_FLAGHDD, 0);
			settingFlagConnect.edit().putBoolean("isEverConnect2", true)
					.commit();
			MyApplication.flagEverConnect = true;
		}
		
		skipDownload = false;
		
		hideshowDialogConnect();
		
		MyLog.e("deviceDidAuthenWithServer", "SUCCESS - " + svrModel);
		if(svrModel == MyApplication.SONCA_SMARTK_9108_SYSTEM){
			svrModel = MyApplication.SONCA_SMARTK;
		}
		if(svrModel == MyApplication.SONCA_SMARTK_KM4_SYSTEM){
			svrModel = MyApplication.SONCA_SMARTK_KM4;
		}
		if(svrModel == MyApplication.SONCA_SMARTK_CB){
			MyApplication.flagSmartK_CB = true;
			svrModel = MyApplication.SONCA_SMARTK;
		}
		if(svrModel == MyApplication.SONCA_SMARTK_801){
			MyApplication.flagSmartK_801 = true;
			svrModel = MyApplication.SONCA_SMARTK;
		}
		if(svrModel == MyApplication.SONCA_SMARTK_KM4){
			MyApplication.flagSmartK_KM4 = true;
			svrModel = MyApplication.SONCA_SMARTK;
		}
		MyApplication.intSvrModel = svrModel;
		
		// STORE SAVE CODE VERSION
		final String PREFS_CODE_VERSION = "CodeVersionFile";
		SharedPreferences settingCode = getSharedPreferences(
				PREFS_CODE_VERSION, 0);
		if(codeVersion!=0){
			if (svrModel == MyApplication.SONCA  || svrModel == MyApplication.SONCA_SMARTK) {
				settingCode.edit().putInt("soncaVersion", codeVersion).commit();
			} else if (svrModel == MyApplication.SONCA_KARTROL || svrModel == MyApplication.SONCA_KM1) {
				settingCode.edit().putInt("kartrolVersion", codeVersion).commit();
			} else if (svrModel == MyApplication.SONCA_HIW || svrModel == MyApplication.SONCA_KM2
					|| svrModel == MyApplication.SONCA_KB_OEM || svrModel == MyApplication.SONCA_KM1_WIFI
					|| svrModel == MyApplication.SONCA_KBX9 || svrModel == MyApplication.SONCA_KB39C_WIFI
					 || svrModel == MyApplication.SONCA_TBT) {
				settingCode.edit().putInt("hiwVersion", codeVersion).commit();
			}	
		}	
		MyLog.e("soncaVersion", "....... " + settingCode.getInt("soncaVersion", 0));
		MyLog.e("kartrolVersion", "....... " + settingCode.getInt("kartrolVersion", 0));
		MyLog.e("hiwVersion", "....... " + settingCode.getInt("hiwVersion", 0));
		
		MyApplication.intSvrCode = codeVersion;		
		
		if(MyApplication.intSvrModel == MyApplication.SONCA && codeVersion >= 21){
			MyApplication.isAvailableHiddenAPI = true;
		} else {
			MyApplication.isAvailableHiddenAPI = false;
		}	
		
		byte[] responseData = response.getData();
		authenId = ByteUtils.byteToInt32(responseData, 0);
		lastUpdate1 = ByteUtils.byteToInt32(responseData, 4);
		lastUpdate2 = ByteUtils.byteToInt32(responseData, 8);
		lastUpdate3 = ByteUtils.byteToInt32(responseData, 12);
		lastUpdate4 = ByteUtils.byteToInt32(responseData, 16);
		
		MyApplication.intMTocType = 0;
		intTocHDDVol = 0;
		intTocDISCVol = 0;
		intTocHDDType = 0;
		intIndexType = 0;
		
		flagCheckDataMIDI = false;
		if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
			try {
				flagCheckDataMIDI =  (ByteUtils.byteToInt8(responseData, 48) == 1);
			} catch (Exception e) {
				
			}
		}
		
		try {
			if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
					|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
					|| MyApplication.intSvrModel == MyApplication.SONCA_KBX9 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
					 || MyApplication.intSvrModel == MyApplication.SONCA_TBT){
				MyApplication.intMTocType = responseData[11] & 0xff;
				
				intIndexType = responseData[12] & 0xff;
				
				int intTocHDDVersion = ByteUtils.byteToInt16(responseData, 14);
				int intTocHDDRevision = ByteUtils.byteToInt8(responseData, 16);
				intTocHDDVol = intTocHDDVersion * 100 + intTocHDDRevision;

				intTocHDDType = ByteUtils.byteToInt8(responseData, 17);
				
				int intTocDISCVersion = ByteUtils.byteToInt16(responseData, 8);
				int intTocDISCRevision = ByteUtils.byteToInt8(responseData, 10);
				intTocDISCVol = intTocDISCVersion * 100 + intTocDISCRevision;
				
				MyApplication.isMoveList = false;
				MyLog.d("Authen 8203R TocType", "intTocType = " + MyApplication.intMTocType);
				MyLog.d("", "intIndexType = " + intIndexType);
				MyLog.d("", "intTocHDDVol = " + intTocHDDVol);
				MyLog.d("", "intTocHDDType = " + intTocHDDType);
				MyLog.d("", "intTocDISCVol = " + intTocDISCVol);
				
				MyApplication.intMTocType = MyApplication.intMTocType + intIndexType * 255;
				
				AppSettings setting = AppSettings
						.getInstance(getApplicationContext());
				int intAppDISC = setting.loadHiWDISCVersion();
				int intAppHDD = setting.loadHiWHDDVersion();
				
				MyLog.d("", "intAppDISC = " + intAppDISC);
				MyLog.d("", "intAppHDD = " + intAppHDD);
				
				MyLog.d("MyApplication.countStartApp", MyApplication.countStartApp + "");
				if(intTocHDDVol > intAppDISC || intTocDISCVol > intAppHDD){
					if(MyApplication.countStartApp != 0){
						if(!boolShowKaraoke){
							showDialogMessage(getResources().getString(R.string.popup_upData_1));
							dialogNoAddPlayList.setAutoClose(false);						
							if(timerHideDialogMessage != null){
								timerHideDialogMessage.cancel();
								timerHideDialogMessage = null;
							}
							handlerAutoHideMessage.removeMessages(0);	
						}
							
					} else {
						MyApplication.countStartApp++;
					}						
				}
				
				if(intTocHDDVol > 0){
					StoreLyric storeLyric = new StoreLyric(getApplicationContext());
					storeLyric.saveLyricHDD(true);
				} 
			} else {
				MyApplication.isMoveList = true;
			}
		} catch (Exception e) {

		}
		
		SKServer skServer = ((MyApplication) getApplication())
				.getDeviceCurrent();
		
		AppSettings setting = AppSettings.getInstance(getApplicationContext());
		if(skServer != null){
			skServer.setModelDevice(MyApplication.intSvrModel);
			((MyApplication) this.getApplication()).SaveDevice(skServer);
			
			setting.saveLastServerName(skServer.getName());
			setting.saveServerIP(skServer.getConnectedIPStr());
			setting.saveServerPass(skServer.getConnectPass());
			setting.saveModeModel(skServer.getModelDevice());
			setting.saveIrcRemote(skServer.getIrcRemote());
			setting.saveNameRemote(skServer.getNameRemote());
			skServer.setState(SKServer.CONNECTED);
		}		
		showConnectSuccess();
		if(skServer != null){
			MyLog.e("deviceDidAuthenWithServer", skServer.getName());
			MyLog.e("deviceDidAuthenWithServer", skServer.getConnectedIPStr());
			MyLog.e("deviceDidAuthenWithServer", skServer.getConnectPass());
			MyLog.e("", "flagSupportOffUser = "	+ MyApplication.flagSupportOffUser);
			MyLog.e("", "bOffUserList = " + MyApplication.bOffUserList);
		}
		if(listDeviceListener != null){
			listDeviceListener.OnShowListDevice();
		}	
		
		if (MyApplication.intSvrModel == MyApplication.SONCA_KARTROL || MyApplication.intSvrModel == MyApplication.SONCA_KM1) {
			groupView_StopTimerAutoConnect();
			
			MyApplication.flagModelA = false;
			
			MyApplication.intCommandMedium = 0x00000000;
			MyApplication.intSavedCommandMedium = 0x00000000;
			MyApplication.intCommandEnable = 0xffff;
			MyApplication.bOffUserList = false;
			serverStatus = new ServerStatus();
			
			SharedPreferences sharedselect = getSharedPreferences("sharedselect", Context.MODE_PRIVATE);
			MyApplication.intSelectList = sharedselect.getInt("intselect", 0);
			
			if(MyApplication.intSelectList == 0){
				MyApplication.intSelectList = MyApplication.SelectList_SONCA;
				
				SharedPreferences.Editor editorSelect = sharedselect.edit();
				editorSelect.putInt("intselect", MyApplication.intSelectList);
				editorSelect.commit();
			}
			
			MyApplication.intRemoteModel = 0;
			viewDevice.setNameDevice("ACNOS");
			
			MyApplication.intWifiRemote = MyApplication.SONCA_KARTROL;
			ChangeWifiRmote();
			
			MyApplication.mSongTypeList = null;
			processTotalSongType(100);
			
			if (allowTimerPing != true) {
				allowTimerPing = true;
			}

			layoutAnimation.setVisibility(View.VISIBLE);
//			groupView.setConnected(TouchMyGroupView.CONNECTED,
//					skServer.getName(), skServer);

			if (flagRunHide) {
				hideActionBar();
			}

			((MyApplication) getApplication())
					.setListActive(new ArrayList<Song>());

			showFragSwitch();
			
			viewGiamAm.setEnableView(View.VISIBLE);
			viewTangAm.setEnableView(View.VISIBLE);
			viewTachLoi.setEnableView(View.VISIBLE);
			viewCaocap.setEnableView(View.VISIBLE);
			viewWifi.setEnableView(View.INVISIBLE);
			viewQuaBai.setEnableView(View.VISIBLE);
			viewDachon.setEnableView(View.INVISIBLE);
			viewQuayLai.setEnableView(View.VISIBLE);
			
			if(viewChinhMiDi != null){
				viewHatLai.setEnableView(View.VISIBLE);
				viewNgatTieng.setEnableView(View.VISIBLE);
				viewChinhMiDi.setEnableView(View.VISIBLE);
				viewTamDung.setEnableView(View.VISIBLE);
				
				viewGiamTone.setEnableView(View.VISIBLE);
				viewTangTone.setEnableView(View.VISIBLE);
				viewDance.setEnableView(View.INVISIBLE);
				viewChamDiem.setEnableView(View.VISIBLE);
				
				viewMelodyGiam.setEnableView(View.VISIBLE);
				viewMelodyTang.setEnableView(View.VISIBLE);
				viewTempoGiam.setEnableView(View.VISIBLE);
				viewTempoTang.setEnableView(View.VISIBLE);
				viewTone.setEnableView(View.VISIBLE);
				viewHoaAm.setEnableView(View.INVISIBLE);
				viewMacDinh.setEnableView(View.INVISIBLE);
			}
			
			return;

		}

		drawerLayout.closeDrawer(layoutConnect);
				
		saveSkipUpdate1 = setting.loadServerLastUpdate(1);
		saveSkipUpdate2 = setting.loadServerLastUpdate(2);
		saveSkipUpdate3 = setting.loadServerLastUpdate(3);
		saveSkipUpdate4 = setting.loadServerLastUpdate(4);

		MyLog.e("VERSION DAU MAY", lastUpdate1 + "-" + lastUpdate2 + "-"
				+ lastUpdate3 + "-" + lastUpdate4);
		MyLog.e("VERSION TREN APP", saveSkipUpdate1 + "-" + saveSkipUpdate2
				+ "-" + saveSkipUpdate3 + "-" + saveSkipUpdate4);
		
		// --------//
		String name = skServer.getName();
		if (name.equals("")) {
			name = getResources().getString(R.string.connected);
		}
		viewDevice.setNameDevice(name);

		if(MyApplication.intSvrModel == MyApplication.SONCA || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
			if (!MyApplication.flagEverConnectHDD && lastUpdate3 > 0) {
				MyLog.e("SET ON EVER HDD", "TRUE..................");
				MyApplication.flagEverConnectHDD = true;
				SharedPreferences settingFlagConnect = getSharedPreferences(
						PREFS_FLAGHDD, 0);
				settingFlagConnect.edit().putBoolean("isEverConnect", true)
						.commit();
			}
			
			if(lastUpdate3 > 0){
				StoreLyric storeLyric = new StoreLyric(getApplicationContext());
				storeLyric.saveLyricHDD(true);
				MyApplication.flagSK9xxx_RunningHDD = true;
			} else {
				MyApplication.flagSK9xxx_RunningHDD = false;
			}
		}
		
		boolean isNeedupdateYTFile = false;
		if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
			int appYTVersion = setting.getYouTubeVersion();
			int serverYTVersion = 0;
			
			try {
				serverYTVersion = ByteUtils.byteToInt32(responseData, 24);	
			} catch (Exception e) {
				
			}
			
			if(serverYTVersion != appYTVersion){
				isNeedupdateYTFile = true;
				saveYTVersion = serverYTVersion;
			}
			
			MyLog.d(" ", "appYTVersion = " + appYTVersion);
			MyLog.d(" ", "serverYTVersion = " + serverYTVersion);
		}
		
		isNeedupdateOfflineFile = false;
		if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){				
			int appListOfflineVersion = setting.getListOfflineVersion();
			int serverListOfflineVersion = 0;

			String rootPath = android.os.Environment
					.getExternalStorageDirectory()
					.toString()
					.concat(String.format("/%s/%s", "Android/data",
							getPackageName()));
			String myPath = rootPath.concat("/YOUTUBE/" + MyApplication.LIST_OFFLINE);
			File myFile = new File(myPath);
			if (!myFile.exists()) {
				appListOfflineVersion = 0;
			}
			
			try {
				serverListOfflineVersion = ByteUtils.byteToInt32(responseData, 32);	
			} catch (Exception e) {
				
			}
			
			if(serverListOfflineVersion != appListOfflineVersion){
				isNeedupdateOfflineFile = true;
				saveListOfflineVersion = serverListOfflineVersion;
			}
			
			MyLog.d(" ", "appListOfflineVersion = " + appListOfflineVersion);
			MyLog.d(" ", "serverListOfflineVersion = " + serverListOfflineVersion);
		}
		
		isNeedUpdateTocFlag = isNeedUpdateDauMayDif(lastUpdate1, lastUpdate2,
				lastUpdate3, lastUpdate4);
		
		if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
				|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
				|| MyApplication.intSvrModel == MyApplication.SONCA_KBX9 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
				 || MyApplication.intSvrModel == MyApplication.SONCA_TBT){
			isNeedUpdateTocFlag = false;
		}
		

		if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
			int appUpdateTocVersion = setting.getUpdateTOCVersion();
			int serverUpdateTocVersion = 0;
			
			try {
				serverUpdateTocVersion = ByteUtils.byteToInt32(responseData, 28);	
			} catch (Exception e) {
				
			}
			
			saveUpdateTocVersion = serverUpdateTocVersion;
			
			if(serverUpdateTocVersion != appUpdateTocVersion){
				isNeedUpdateTocFlag = true;
			}
			
			MyLog.d(" ", "appUpdateTocVersion = " + appUpdateTocVersion);
			MyLog.d(" ", "serverUpdateTocVersion = " + serverUpdateTocVersion);
		}
		
		if(isNeedUpdateTocFlag == false && MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && setting.getLastConnectType() == MyApplication.SONCA){
			isNeedUpdateTocFlag = true;
		}
		
		if(isNeedUpdateTocFlag == false && MyApplication.intSvrModel == MyApplication.SONCA && setting.getLastConnectType() == MyApplication.SONCA_SMARTK){
			isNeedUpdateTocFlag = true;
		}
		
		if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
			MyLog.d(" ", "flagCheckDataMIDI = " + flagCheckDataMIDI);
			MyLog.d(" ", "isLastMIDIData = " + setting.isLastMIDIData());
			
			if(flagCheckDataMIDI != setting.isLastMIDIData()){
				isNeedUpdateTocFlag = true;
			}
		}	
		
		isMainNeedUpdate = isNeedUpdateTocFlag;
		
		MyApplication.intWifiRemote = MyApplication.SONCA;
		ChangeWifiRmote();	
		
		if (!isNeedUpdateTocFlag && !isNeedupdateYTFile && !isNeedupdateOfflineFile) {
			MyLog.e("RUN LUON","......................");
			serverStatus = null;		
			((MyApplication) getApplication()).startSyncServerStatusThread();
			
			return;
		}
		
		String rootPath = "";
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			rootPath = android.os.Environment.getExternalStorageDirectory().toString();
			rootPath = rootPath.concat(String.format("/%s/%s", "Android/data", getPackageName()));
			File appBundle = new File(rootPath);
			if (!appBundle.exists())
				appBundle.mkdirs();
		}

		if(isNeedupdateYTFile){
			if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
				DownloadState = -1;

				String savePath = rootPath.concat("/YOUTUBE/" + MyApplication.YT_FILENAME);
				File updateFile = new File(savePath);
				if (updateFile.exists()) {
					updateFile.delete();
				}

				allowTimerPing = false;
				((MyApplication) getApplication()).downloadYouTubeFile(savePath);
				isDownloadingDauMay = true;
			}			
			
		} else if(isNeedupdateOfflineFile){
			if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
				DownloadState = -3;

				String savePath = rootPath.concat("/YOUTUBE/" + MyApplication.LIST_OFFLINE);
				File updateFile = new File(savePath);
				if (updateFile.exists()) {
					updateFile.delete();
				}

				allowTimerPing = false;
				((MyApplication) getApplication()).downloadOfflineFile(savePath);
				isDownloadingDauMay = true;
			}			
			
		} else {
			DownloadState = 0;
			String savePath = rootPath.concat("/MEGIDX");
			File updateFile = new File(savePath);
			if (updateFile.exists()) {
				updateFile.delete();
			}
			allowTimerPing = false;
			
			((MyApplication) getApplication()).downloadUpdateFile(savePath);
			isDownloadingDauMay = true;
			DownloadState++;
			
		}	
		
	}
	
	private int saveYTVersion = 0;
	private int saveListOfflineVersion = 0;
	private int saveUpdateTocVersion = 0;
	private boolean isMainNeedUpdate = false;
	private boolean isNeedupdateOfflineFile = false;

	private boolean isShowPassword = true;
	@Override
	public void deviceDidConnectWithServer(Boolean success, boolean flagFinish, String srvName) {
		MyLog.e(TAB, "deviceDidConnectWithServer() - flagFinish = " + flagFinish);
		if(flagFinish){
			if (success) {
				SKServer skServer = ((MyApplication) getApplication())
						.getDeviceCurrent();
				if (skServer != null) {				
					TouchDeviceFragment fragment = (TouchDeviceFragment) fragmentManager
							.findFragmentByTag(ONE_DEVICE);
					if (fragment != null) {
						if (deviveFragmentListener != null) {
							deviveFragmentListener.OnConnected();
						}
					} else {
						ShowDevice(skServer.getName(), skServer.getConnectedIPStr());
					}
				
				}
			} else {
				((MyApplication) getApplication()).setDeviceCurrent(null);
				viewDevice.setNameDevice("");
			}
			
		}
		else {
			if (success) {
				SKServer skServer = ((MyApplication) getApplication())
						.getDeviceCurrent();
				if (skServer != null) {	
					skServer.setName(srvName);
					((MyApplication) getApplication()).setDeviceCurrent(skServer);
				}
			}
		
		}
	}
	
	public static String convertStreamToString(InputStream is) throws Exception {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();
	    String line = null;
	    while ((line = reader.readLine()) != null) {
	      sb.append(line).append("\n");
	    }
	    reader.close();
	    return sb.toString();
	}

	public static String getStringFromFile (String filePath) throws Exception {
	    File fl = new File(filePath);
	    FileInputStream fin = new FileInputStream(fl);
	    String ret = convertStreamToString(fin);
	    //Make sure you close all streams.
	    fin.close();        
	    return ret;
	}

	@Override
	public void deviceDidTimedout() {
		MyLog.e(TAB, "deviceDidTimedout()");
		if(autoAfterScan){
			autoAfterScan = false;
			return;
		}
		DefaultViewWhenDisConect();
	}

	private int totalSize = 0;
	private boolean skipPic = false;

	@Override
	public void deviceDidDownloadFile(boolean success, String savePath) {
		MyLog.e(TAB, "deviceDidDownloadFile()");
		String rootPath = "";
		
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			rootPath = android.os.Environment.getExternalStorageDirectory()
					.toString();
			rootPath = rootPath.concat(String.format("/%s/%s", "Android/data",
					getPackageName()));
			File appBundle = new File(rootPath);
			if (!appBundle.exists())
				appBundle.mkdirs();
		}
		
		isDownloadingDauMay = false;
		if(success == false) {
			MyLog.e(TAB, "File download failed at location: " + savePath);
			MyLog.e(TAB, "Download State: " + DownloadState);
			
			if(DownloadState == -10){ // special download file midi
				DownloadState = 0;
				if(colorLyricListener != null){
					colorLyricListener.OnMain_DownloadMidiResult(false, idDownloadMidi);
				}
				return;
			}
			
			if(DownloadState < 0){
				if(DownloadState == -3){
					if(isMainNeedUpdate){
						DownloadState = 0;

						final String path = rootPath;
						String savePath1 = rootPath.concat("/MEGIDX");
						String lyricPath = rootPath.concat("/LYRICS");

						final File updateFile = new File(savePath1);
						final File lyricFile = new File(lyricPath);
						
						if (updateFile.exists()) {
							updateFile.delete();
						}
						allowTimerPing = false;
						
						String mSavePath = rootPath.concat("/MEGIDX");
						((MyApplication) getApplication()).downloadUpdateFile(mSavePath);
						isDownloadingDauMay = true;
						DownloadState++;
						return;
					}
				} else if (DownloadState == -1) {
					if(isNeedupdateOfflineFile){
						DownloadState = -3;

						String myPath = rootPath.concat("/YOUTUBE/" + MyApplication.LIST_OFFLINE);
						File updateFile = new File(myPath);
						if (updateFile.exists()) {
							updateFile.delete();
						}

						allowTimerPing = false;
						((MyApplication) getApplication()).downloadOfflineFile(myPath);
						isDownloadingDauMay = true;
						return;
					}
					
					if(isMainNeedUpdate){
						DownloadState = 0;

						final String path = rootPath;
						String savePath1 = rootPath.concat("/MEGIDX");
						String lyricPath = rootPath.concat("/LYRICS");

						final File updateFile = new File(savePath1);
						final File lyricFile = new File(lyricPath);
						
						if (updateFile.exists()) {
							updateFile.delete();
						}
						allowTimerPing = false;
						
						String mSavePath = rootPath.concat("/MEGIDX");
						((MyApplication) getApplication()).downloadUpdateFile(mSavePath);
						isDownloadingDauMay = true;
						DownloadState++;
						return;
					}
					
				}
				
				// normal skip
				processHinhDauMay(false);
			} else if(DownloadState < 3){				
				skipDownload = true;
//				processHinhDauMay(false);
				ShowBusyDownloadTOC(0);
			} 
			else {
				processHinhDauMay(false);
//				OnUpdatePicAlone(1);
				ShowBusyDownloadTOC(1);
			}
			return;
		}

		if(DownloadState == -10){ // special download file midi
			DownloadState = 0;
			if(colorLyricListener != null){
				colorLyricListener.OnMain_DownloadMidiResult(true, idDownloadMidi);
			}
			return;
		}
		
		final String path = rootPath;
		String savePath1 = rootPath.concat("/MEGIDX");
		// String singerPath = rootPath.concat("/ARTIST");
		String lyricPath = rootPath.concat("/LYRICS");

		final File updateFile = new File(savePath1);
		// final File singerFile = new File(singerPath);
		final File lyricFile = new File(lyricPath);
		/*
		 * if(DownloadState == 1) { if (singerFile.exists()) { Log.e("",
		 * singerFile + " =====>File singer existed"); singerFile.delete(); }
		 * ((MyApplication
		 * )getApplication()).downloadSingerUpdateFile(singerPath);
		 * DownloadState ++; }else
		 */
		
		if (DownloadState == -3) {
			AppSettings settings = AppSettings.getInstance(getApplicationContext());
			settings.saveListOfflineVersion(saveListOfflineVersion);			
			
			if (isMainNeedUpdate) {
				DownloadState = 0;
				if (updateFile.exists()) {
					updateFile.delete();
				}
				allowTimerPing = false;
				
				String mSavePath = rootPath.concat("/MEGIDX");
				((MyApplication) getApplication()).downloadUpdateFile(mSavePath);
				isDownloadingDauMay = true;
				DownloadState++;
			} else {
				reloadYoutubeTable();

				serverStatus = null;
				((MyApplication) getApplication()).startSyncServerStatusThread();
			}
		} else if (DownloadState == -2) {
			if (layoutDownload.getVisibility() != View.GONE) {
				layoutDownload.setVisibility(View.GONE);
			}
			if (layoutAnimation.getVisibility() != View.VISIBLE) {
				layoutAnimation.setVisibility(View.VISIBLE);
			}
			
			allowTimerPing = true;
			isDownloadingDauMay = false;
			
			ArrayList<SongInfo> listResult = getListAddFromFile();
			if(listResult.size() > 0){
				if(dialogAddSongData != null){
					dialogAddSongData.sendListAddSong(listResult);
				}
			} else {
				showDialogMessage(getResources().getString(R.string.usb_check_2));
			}
			
		} else if (DownloadState == -1) {

			if(isNeedupdateOfflineFile){
				DownloadState = -3;

				String myPath = rootPath.concat("/YOUTUBE/" + MyApplication.LIST_OFFLINE);
				File myFile = new File(myPath);
				if (myFile.exists()) {
					myFile.delete();
				}

				allowTimerPing = false;
				((MyApplication) getApplication()).downloadOfflineFile(myPath);
				isDownloadingDauMay = true;
				return;
			}
			
			AppSettings settings = AppSettings
					.getInstance(getApplicationContext());
			settings.saveYouTubeVersion(saveYTVersion);

			if (isMainNeedUpdate) {
				DownloadState = 0;
				if (updateFile.exists()) {
					// Log.e("", savePath + " =====>File megidx existed");
					updateFile.delete();
				}
				allowTimerPing = false;

				String mSavePath = rootPath.concat("/MEGIDX");
				((MyApplication) getApplication()).downloadUpdateFile(mSavePath);
				
				isDownloadingDauMay = true;
				DownloadState++;
			} else {
				reloadYoutubeTable();
				
				serverStatus = null;
				((MyApplication) getApplication()).startSyncServerStatusThread();
			}
		} else if (DownloadState == 1) {
			if (lyricFile.exists()) {
				// Log.e("", lyricFile + " =====>File lyric existed");
				lyricFile.delete();
			}
			isDownloadingDauMay = true;
			((MyApplication) getApplication()).downloadLyricFile(lyricPath);
			DownloadState++;
		} else if (DownloadState == 2) {
			isDownloadingDauMay = true;
			processTocDauMay();
		} else if (DownloadState == 3) {
			// Log.e("DOWN INFO", "down file update_t.xml");
			((MyApplication) getApplication()).cancelSyncServerStatus();
			isDownloadingDauMay = true;
			String fileInfoPath = rootPath.concat("/update_t.xml");
			((MyApplication) getApplication()).downloadArtistFileInfo(
					fileInfoPath, true);
			DownloadState++;
		} else if (DownloadState == 4) { // after download update_t.xml
			try {
				isDownloadingDauMay = true;
				layoutDownload.setVisibility(View.GONE);
				final String PREFS_NAME = "UpdatePicFile";
				SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

				// settings.edit().putInt("saveVersion", 0).commit();

				saveVersion = settings.getInt("saveVersion", 0);

				String updateInfo_path = rootPath.concat("/update_t.xml");

				// ----- READ FILE update_p.xml to find info
				InputStream is = new FileInputStream(updateInfo_path);
				Document doc = XmlUtils.convertToDocument(is);
				NodeList nodeList = doc.getElementsByTagName("picture");

				infoList.clear();
				totalSize = 0;
				for (int i = 0; i < nodeList.getLength(); i++) {
					Node node = nodeList.item(i);
					Element e = (Element) node;
					NodeList nlTemp = e.getElementsByTagName("id");
					Node nTemp = nlTemp.item(0);
					String strID = nTemp.getTextContent();
					int intVersion = Integer.parseInt(strID);

					nlTemp = e.getElementsByTagName("link");
					nTemp = nlTemp.item(0);
					String strLink = nTemp.getTextContent();

					nlTemp = e.getElementsByTagName("size");
					nTemp = nlTemp.item(0);
					String strSize = nTemp.getTextContent();

					if (intVersion > highestVersion) {
						highestVersion = intVersion;
					}

					if (intVersion > saveVersion) {
						infoList.add(strID + "-" + strLink);
						totalSize += Integer.valueOf(strSize);
					}
				}

				if (highestVersion <= saveVersion) { // UPTODATE
					if (fromSingerTab) {
						// popupViewUpadtePic
						// .setPopupLayout(TouchPopupViewUpdatePic.LAYOUT_UPTODATE);
						// myDialogUpdatePic.getWindow()
						// .setGravity(Gravity.CENTER);
						// WindowManager.LayoutParams params = myDialogUpdatePic
						// .getWindow().getAttributes();
						// myDialogUpdatePic.getWindow().setAttributes(params);
						// myDialogUpdatePic.show();

						isDownloadingDauMay = false;
						fromSingerTab = false;
						OnUpdatePicAlone(1);
					} else {
						processHinhDauMay(false);
					}
				} else { // UPDATE
					// ASK UPDATE OR NOT
					if(!isFinishing()){
						popupViewUpadtePic
								.setOnPopupUpdatePicListener(new OnPopupUpdatePicListener() {
									@Override
									public void OnUpdatePicYes() {
										countDownPicDauMay = 0;
										String txtFileName = infoList.get(
												countDownPicDauMay).split("-")[1];
										OnDownPicFILEFromDauMay(txtFileName);
										myDialogUpdatePic.dismiss();
									}
	
									@Override
									public void OnUpdatePicNo() {
										isDownloadingDauMay = false;
										myDialogUpdatePic.dismiss();
										processHinhDauMay(false);
									}
								});
	
						popupViewUpadtePic.setTotalDown((float)totalSize / 1024 / 1024);
						popupViewUpadtePic
								.setServerName(((MyApplication) getApplication())
										.getDeviceCurrent().getName());
						popupViewUpadtePic
								.setPopupLayout(TouchPopupViewUpdatePic.LAYOUT_UPDATEDAUMAY);					
						myDialogUpdatePic.getWindow().setGravity(Gravity.CENTER);
						allowCancelOutside = false;
						WindowManager.LayoutParams params = myDialogUpdatePic
								.getWindow().getAttributes();
						myDialogUpdatePic.getWindow().setAttributes(params);
						myDialogUpdatePic.show();
					}
				}
			} catch (Exception e) {
				isDownloadingDauMay = false;
				processHinhDauMay(false);
			}
		} else {
			if (countDownPicDauMay < infoList.size()) {
				String txtFileName = infoList.get(countDownPicDauMay)
						.split("-")[1];
				OnDownPicFILEFromDauMay(txtFileName);
			} else {
				processHinhDauMay(true);
			}
		}
	}

	private void processTocDauMay() {
		String rootPath = "";
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			rootPath = android.os.Environment.getExternalStorageDirectory()
					.toString();
			rootPath = rootPath.concat(String.format("/%s/%s", "Android/data",
					getPackageName()));
			File appBundle = new File(rootPath);
			if (!appBundle.exists())
				appBundle.mkdir();
		}
		final String rootFinalPath = rootPath;

		final String picDir = rootPath.concat("/PICTURE");
		File picDirFile = new File(picDir);
		if (!picDirFile.exists()) {
			picDirFile.mkdir();
		}

		final String path = rootPath;
		final String savePath1 = rootPath.concat("/MEGIDX");
		final String lyricPath = rootPath.concat("/LYRICS");

		final File updateFile = new File(savePath1);
		final File lyricFile = new File(lyricPath);

		if (layoutDownload.getVisibility() != View.GONE) {
			layoutDownload.setVisibility(View.GONE);
		}
		if (layoutAnimation.getVisibility() != View.VISIBLE) {
			layoutAnimation.setVisibility(View.VISIBLE);
		}
		
		allowCancelOutside = false;
		if(!isFinishing()){
			popupViewUpadtePic
					.setPopupLayout(TouchPopupViewUpdatePic.LAYOUT_PROCESSDATA);
			allowCancelOutside = false;
			myDialogUpdatePic.setCancelable(false);
			myDialogUpdatePic.getWindow().setGravity(Gravity.CENTER);
			WindowManager.LayoutParams params = myDialogUpdatePic.getWindow()
					.getAttributes();
			myDialogUpdatePic.getWindow().setAttributes(params);
			myDialogUpdatePic.show();
		}
		final Handler messageHandler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				processLoadFavList();
				
				allowCancelOutside = true;

				if (myDialogUpdatePic.isShowing()) {
					myDialogUpdatePic.dismiss();
				}

				String tempDBPath = rootFinalPath.concat("/database.db");
				if (new File(tempDBPath).exists()) {
					new File(tempDBPath).delete();
				}
				if (updateFile.exists()) {
					updateFile.delete();
				}
				if (lyricFile.exists()) {
					lyricFile.delete();
				}

				if(MyApplication.intSvrModel == MyApplication.SONCA){
					DownloadState = 3;
					deviceDidDownloadFile(true, "");	
				} else {
					processHinhDauMay(false);
				}
			}
		};

		new Thread(new Runnable() {
			public void run() {
				// FAKE SHOWSONGFRAGMENT
//				FragmentTransaction fragmentTransaction = fragmentManager
//						.beginTransaction();
//				if (fragmentBase != null && SAVETYPE == SONG) {
//					((TouchFragmentSong)fragmentBase).forceCloseDB();
//				}
//				fragmentTransaction.commit();	

				((MyApplication) getApplication()).cancelSyncServerStatus();

				serverStatus = null;
				
				final String cpRightPath = path.concat("/DB");
				
				SmartKaraoke importDB = new SmartKaraoke();
				if (updateFile.exists()) {
					flagParseDB = importDB.importdata(getDatabasePath(DbHelper.DBName)
							.getAbsolutePath(), savePath1, lyricPath, cpRightPath);
					MyLog.e("PARSE TOC FLAG", flagParseDB + "");
					if (!flagParseDB) { // FAIL PARSE DB
						revertDB();
					} else { // SUCCESS PARSE DB						
						try {					
//							Thread.sleep(200);	
							DBInterface.createNewIndexSong(context);
							processNewSongTable();
							
							AppSettings settings = AppSettings.getInstance(getApplicationContext());
							if(MyApplication.intSvrModel == MyApplication.SONCA){
								settings.setLastConnectType(MyApplication.SONCA);
							}
							
							if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
								settings.setLastConnectType(MyApplication.SONCA_SMARTK);
								settings.setLastMIDIData(flagCheckDataMIDI);
							}
							
							if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
								reloadYoutubeTable();
								settings.saveUpdateTOCVersion(saveUpdateTocVersion);
							}
							
							if (skipDownload) {
								settings.saveServerLastUpdate(saveSkipUpdate1, 1);
								settings.saveServerLastUpdate(saveSkipUpdate2, 2);
								settings.saveServerLastUpdate(saveSkipUpdate3, 3);
								settings.saveServerLastUpdate(saveSkipUpdate4, 4);
								skipDownload = false;
							} else {
								if (isNeedUpdateDauMayDif(lastUpdate1, lastUpdate2,
										lastUpdate3, lastUpdate4)) {
									settings.saveServerLastUpdate(lastUpdate1, 1);
									settings.saveServerLastUpdate(lastUpdate2, 2);
									settings.saveServerLastUpdate(lastUpdate3, 3);
									settings.saveServerLastUpdate(lastUpdate4, 4);
								}
							}	
							
							} catch (Exception e) {
							
						}
						
					}
				}
				messageHandler.sendEmptyMessage(0);
			}
		}).start();
	}

	private void processHinhDauMay(boolean isPic) {
		try {
			isDownloadingDauMay = true;
			final boolean coPic = isPic;
			String rootPath = "";
			if (android.os.Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED)) {
				rootPath = android.os.Environment.getExternalStorageDirectory()
						.toString();
				rootPath = rootPath.concat(String.format("/%s/%s",
						"Android/data", getPackageName()));
				File appBundle = new File(rootPath);
				if (!appBundle.exists())
					appBundle.mkdir();
			}
			final String rootFinalPath = rootPath;

			final String picDir = rootPath.concat("/PICTURE");
			File picDirFile = new File(picDir);
			if (!picDirFile.exists()) {
				picDirFile.mkdir();
			}

			final String path = rootPath;
			String savePath1 = rootPath.concat("/MEGIDX");
			String lyricPath = rootPath.concat("/LYRICS");

			final File updateFile = new File(savePath1);
			final File lyricFile = new File(lyricPath);

			if (layoutDownload.getVisibility() != View.GONE) {
				layoutDownload.setVisibility(View.GONE);
			}
			if (layoutAnimation.getVisibility() != View.VISIBLE) {
				layoutAnimation.setVisibility(View.VISIBLE);
			}

			if (coPic) {
				if(!isFinishing()){
					allowCancelOutside = false;
					popupViewUpadtePic
							.setPopupLayout(TouchPopupViewUpdatePic.LAYOUT_PROCESSDATA);
					
					myDialogUpdatePic.getWindow().setGravity(Gravity.CENTER);
					WindowManager.LayoutParams params = myDialogUpdatePic
							.getWindow().getAttributes();
					myDialogUpdatePic.getWindow().setAttributes(params);
					myDialogUpdatePic.show();
				}
			}

			final Handler messageHandler = new Handler() {
				public void handleMessage(Message msg) {
					super.handleMessage(msg);

					allowCancelOutside = true;
					
					if (coPic) {
						clearDownloadPicResource();
					}

					animationView.ShowMessage(TouchAnimationView.NOTHING);
					SetKeepScreenOn(false);

					if (myDialogUpdatePic.isShowing()) {
						myDialogUpdatePic.dismiss();
					}
					
					if(flagParseDB){
						showFragSwitch();
					}
					
					isDownloadingDauMay = false;
					
					((MyApplication) getApplication())
							.startSyncServerStatusThread();
				}
			};

			new Thread(new Runnable() {
				public void run() {
					if (coPic) {
						SmartKaraoke importDB = new SmartKaraoke();
						saveUpdatePicSuccess();
						for (int i = 0; i < infoList.size(); i++) {
							String txtFileName = infoList.get(i).split("-")[1];
							importDB.extractdata(rootFinalPath + "/"
									+ txtFileName, picDir);
						}
					}
					messageHandler.sendEmptyMessage(0);
				}
			}).start();
		} catch (Exception e) {
//			Log.e("ERROR PROCESS HINH DAU MAY", e.toString());
			isDownloadingDauMay = false;
		}
	}

	@Override
	public void deviceSearchCompleted(ArrayList<SKServer> servers) {
		MyLog.e(TAB, "deviceSearchCompleted");
		if (isDestroyMainActivity == false) {
			isScanning = false;
			return;
		}		
		((MyApplication) getApplication()).setListServers(servers);
		if(!((MyApplication)getApplication()).checkEnableConnectDevice()){
			SKServer sk = ((MyApplication)getApplication()).getDeviceCurrent();
			if(sk != null){
			}
			if (listDeviceListener != null) {
				listDeviceListener.OnShowListDevice();
				listDeviceListener.OnDisplayProgressScan(false);
			}
			MyLog.e(TAB, "Khong ket noi lai !!!!");
			isScanning = false;
			return;
		}	
		if (listDeviceListener != null) {
			listDeviceListener.OnDisplayProgressScan(true);
		}
		if (servers.size() == 1 && 
				(servers.get(0).getModelDevice() == MyApplication.SONCA_KARTROL || servers.get(0).getModelDevice() == MyApplication.SONCA_KM1)) {
			final SKServer skServer = servers.get(0);
			if (serverStatus == null && flagAllowConnect) {
				skServer.setState(SKServer.CONNECTED);
				skServer.setConnectPass("1234");
				new Timer().schedule(new TimerTask() {
					@Override public void run() {
						if(executeCommandPing(skServer.getConnectedIPStr())){
							if (isDestroyMainActivity == true) {
								((MyApplication) getApplication()).disconnectFromRemoteHost();								
								((MyApplication) getApplication())
										.connectToRemoteHost(
												skServer.getConnectedIPStr(),
												skServer.getConnectPass());
								((MyApplication) getApplication()).setDeviceCurrent(skServer);
							}
						}
					}
				}, 1);
			}
			if (listDeviceListener != null) {
				listDeviceListener.OnShowListDevice();
				listDeviceListener.OnDisplayProgressScan(false);
			}
			isScanning = false;
			return;
		}
		if(serverStatus == null){
			ArrayList<SKServer> list = ((MyApplication)getApplication()).getListServers();
			if(list != null && list.size() > 0){
				autoConnectDevice(list);
			}
		}else{
			MyLog.e(TAB, "serverStatus : NOT NULL");
			if (listDeviceListener != null) {
				listDeviceListener.OnShowListDevice();
				listDeviceListener.OnDisplayProgressScan(false);
			}
			isScanning = false;
		}
		
	}
	
	private AutoConnect autoConnect;
	private void autoConnectDevice(final ArrayList<SKServer> listDevice){
		if(autoConnect == null){
			MyLog.e(TAB, "autoConnectDevice");
			autoConnect = new AutoConnect(listDevice, this);
			autoConnect.setOnConnectDeviceListener(new OnConnectDeviceListener() {
				
				@Override
				public void OnConnectListener(boolean isConnect, String ip, String pass, String name) {
					if (serverStatus == null && flagAllowConnect) {
						MyLog.e(TAB, "================");
						MyLog.e(TAB, "ip auto : " + ip);
						MyLog.e(TAB, "pass auto : " + pass);
						MyLog.e(TAB, "================");
						if(isConnect){
							SKServer skServer = new SKServer();
							skServer.setConnectedIPStr(ip);
							skServer.setName(name);
							skServer.setConnectPass(pass);
							((MyApplication)getApplication()).setDeviceCurrent(skServer);
								//--------------//
							((MyApplication) getApplication()).disconnectFromRemoteHost();
							((MyApplication) getApplication())
										.connectToRemoteHost(ip,pass);
						}else{
							((MyApplication)getApplication()).setDeviceCurrent(new SKServer());
						}
					}
					if (listDeviceListener != null) {
						listDeviceListener.OnShowListDevice();
						listDeviceListener.OnDisplayProgressScan(false);
					}
					autoConnect = null;
					isScanning = false;
				}

				@Override
				public void OnFinishAutoConnect() {
					MyLog.e(TAB, "autoConnect : OnFinishAutoConnect()");
					if (listDeviceListener != null) {
						listDeviceListener.OnShowListDevice();
						listDeviceListener.OnDisplayProgressScan(false);
					}
					autoConnect = null;
					isScanning = false;
				}
			});
			autoConnect.execute();
		}
	}
	
	private void removeAutoConnectDevice(){
		if(autoConnect != null){
			autoConnect.cancel(true);
		}
	}

	@Override
	public void deviceDidCommand(boolean success) {
		
	}

	public void revertDB() {
		try {
			MyLog.d(" ", " ");
			MyLog.d("revertDB", "..............................");
			MyLog.d(" ", " ");
			
			if(true){
				processResetToPackageDB();
				return;
			}
			
			//ShowSongFragment(SINGER, singerView);
						
			// FAKE SHOWSONGFRAGMENT
//			FragmentTransaction fragmentTransaction = fragmentManager
//					.beginTransaction();
//			if (fragmentBase != null && SAVETYPE == SONG) {
//				((TouchFragmentSong)fragmentBase).forceCloseDB();
//			}
//			fragmentTransaction.commit();	
			
			String dbPath = getDatabasePath(DbHelper.DBName).getAbsolutePath();
			AssetManager assetMgr = KTVMainActivity.this.getAssets();

			InputStream is = assetMgr.open("database.db");
			OutputStream os = new FileOutputStream(dbPath);
			copyFile(is, os);
			is.close();
			os.flush();
			os.close();

			processLoadFavList();
			allowTimerPing = true;
			
			showFragSwitch();
			
		} catch (Exception e) {

		}
	}
			
	private int intEmptyList = 0;
	private TouchTestSongData testSongData;
	private boolean flagParseDB = false;

	private ArrayList<SongID> curSongIDs;
	private int countChangeDance = 0;
	
	@Override
	public void deviceDidReceiveSyncStatus(boolean success,
			final ServerStatus status) {
		if (isDestroyMainActivity == false) {
			return;
		}	
		if (success == false) {
			
		} else {
			if (allowTimerPing != true) {
				allowTimerPing = true;
			}
			
			if(MyApplication.countStartApp < 2){ // start app first time				
				onUpdateAllFromServer();
			} 
			MyApplication.countStartApp = 2;
			
			if (serverStatus == null) {
				
				// TODO xu ly file offline vua down ve
				if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && (MyApplication.flagSmartK_801 || MyApplication.flagSmartK_CB)){
					String appRootPath = android.os.Environment
							.getExternalStorageDirectory()
							.toString()
							.concat(String.format("/%s/%s", "Android/data",
									getPackageName()));
					String offlinePath = appRootPath.concat("/YOUTUBE/" + MyApplication.LIST_OFFLINE);
					File offlineFile = new File(offlinePath);
					if(offlineFile.exists()){
						try {
							MyApplication.generateListOfflineFromString(getStringFromFile(offlinePath));	
							for (int i = 0; i < MyApplication.listDownloadOffline.size(); i++) {
								MyLog.e(TAB, "i = " + i + " -- " + MyApplication.listDownloadOffline.get(i));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}	
					}	
				}
				
				AppSettings settings = AppSettings
						.getInstance(getApplicationContext());
				if(MyApplication.intSvrModel == MyApplication.SONCA){
					settings.setLastConnectType(MyApplication.SONCA);
				}
				
				if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
					settings.setLastConnectType(MyApplication.SONCA_SMARTK);
					settings.setLastMIDIData(flagCheckDataMIDI);
				}
				
				if (layoutDownload.getVisibility() != View.GONE) {
					layoutDownload.setVisibility(View.GONE);
				}
				
				groupView_StopTimerAutoConnect();
				
				MyApplication.mSongTypeList = null;
				processTotalSongType(100);
				
				if (flagParseDB) {
					if (skipDownload) {
						MyLog.e("SYNC FIRST", "SKIP DOWNLOAD..................");
						MyLog.e("SYNC FIRST", "save skip update...............");
						settings.saveServerLastUpdate(saveSkipUpdate1, 1);
						settings.saveServerLastUpdate(saveSkipUpdate2, 2);
						settings.saveServerLastUpdate(saveSkipUpdate3, 3);
						settings.saveServerLastUpdate(saveSkipUpdate4, 4);
						skipDownload = false;
					} else {
						MyLog.e("SYNC FIRST",
								"KHONG SKIP DOWNLOAD......................");
						if (isNeedUpdateDauMayDif(lastUpdate1, lastUpdate2,
								lastUpdate3, lastUpdate4)) {
							MyLog.e("SYNC FIRST",
									"save last update...............");
							settings.saveServerLastUpdate(lastUpdate1, 1);
							settings.saveServerLastUpdate(lastUpdate2, 2);
							settings.saveServerLastUpdate(lastUpdate3, 3);
							settings.saveServerLastUpdate(lastUpdate4, 4);
						}
					}
					flagParseDB = false;
				}
				
				MyLog.e("SYNC FIRST",
						"after save: " + settings.loadServerLastUpdate(1) + "-"
								+ settings.loadServerLastUpdate(2) + "-"
								+ settings.loadServerLastUpdate(3) + "-"
								+ settings.loadServerLastUpdate(4));

				if(!status.isCaptionAPIValid()){
					MyApplication.flagDeviceUser = false;
					viewDevice.invalidate();
				} else {
					allowTimerPing = false;
					((MyApplication)getApplication()).getDeviceIsUser();
					((MyApplication)getApplication()).setOnReceiverDeviceIsUserListener(new OnReceiverDeviceIsUserListener() {
						
						@Override
						public void OnReceiverDeviceIsUser(String isDeviceUser) {
							allowTimerPing = true;
							if(isDeviceUser.equals("null")){ // old API - admin
								MyApplication.flagDeviceUser = false;
							} else { // new API 0x62
								if(isDeviceUser.equals("1")){
									MyApplication.flagDeviceUser = true;	
								} else {
									MyApplication.flagDeviceUser = false;
								}
							}
							viewDevice.invalidate();
							((MyApplication)getApplication()).setOnReceiverDeviceIsUserListener(null);
						}
					});
				}		
				
				UpdateControlView(status.getMediaType());
				viewTachLoi.setSingerView(status.isSingerOn());
				viewGiamAm.setVolume(status.getCurrentVolume());
				if(viewChinhMiDi != null){
					if(status.getPlayingSongID() != 0){
						viewHatLai.setEnableView(View.VISIBLE);
					} else {
						viewHatLai.setEnableView(View.INVISIBLE);
					}
					
					viewNgatTieng.setMute(status.isMuted());
					viewTamDung.setPlayPause(!status.isPaused());
					viewDance.setEnableView(View.VISIBLE);
					viewChamDiem.setEnableView(View.VISIBLE);
					viewChamDiem.setScoreView(status.getCurrentScore());
					viewGiamTone.setKey(status.getCurrentKey());
					
					viewMelodyGiam.setMelody(status.getCurrentMelody());
					viewTempoGiam.setTempo(status.getCurrentTempo());
					viewTone.setToneView(status.getCurrentTone());
					viewHoaAm.invalidate();
					viewMacDinh.setEnableView(View.VISIBLE);
				}		
				numberPlaylist.setNumberPlaylist(status.getReservedSongCount());
				viewDachon.setSumSong(status.getReservedSongCount());
				if(colorLyricListener != null){
					colorLyricListener.OnMain_setCntPlaylist(status.getReservedSongCount());
				}
				String name = status.getPlayingSongName();
				if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
						|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
						|| MyApplication.intSvrModel == MyApplication.SONCA_KBX9 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
						 || MyApplication.intSvrModel == MyApplication.SONCA_TBT){
					int id = status.getPlayingSongID();
					ArrayList<Song> songs = DBInterface.DBSearchSongID(
							id + "", 0 + "", context);
					if (!songs.isEmpty()) {
						name = songs.get(0).getName();
					}
				}
				if (name != null && !name.equals(" ") && !name.equals("")) {

				} else {
					int id = status.getPlayingSongID();
					ArrayList<Song> songs = DBInterface.DBSearchSongID(id + "",
							status.getPlayingSongTypeABC() + "", context);
					if (!songs.isEmpty()) {
						name = songs.get(0).getName();
					} else {
						if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
							ArrayList<Song> songsYouTube = DBInterface.DBSearchSongID_YouTube(id + "", 0 + "", context);
							if (!songsYouTube.isEmpty()) {
								name = songsYouTube.get(0).getName();
							} else {
								name = "";
							}
						} else {
							name = "";
						}
					}
				}
				tempGroupViewSongName = name;
				if (name == null || name.equals("")) {
					viewQuaBai.invalidate();
					viewTachLoi.setEnableView(View.INVISIBLE);
					
					if(viewChinhMiDi != null){
						viewChinhMiDi.invalidate();
						viewHatLai.invalidate();
						
						viewTamDung.invalidate();
						viewGiamTone.setEnableView(View.INVISIBLE);
						viewTangTone.setEnableView(View.INVISIBLE);
						viewChamDiem.invalidate();						
						
						viewMelodyGiam.setEnableView(View.INVISIBLE);
						viewMelodyTang.setEnableView(View.INVISIBLE);
						viewTempoGiam.setEnableView(View.INVISIBLE);
						viewTempoTang.setEnableView(View.INVISIBLE);	
						viewTone.setEnableView(View.INVISIBLE);	
						viewHoaAm.invalidate();
						viewMacDinh.invalidate();
					}
				}
				layoutAnimation.setVisibility(View.VISIBLE);
				// -----------//
				SKServer skServer = ((MyApplication) getApplication())
						.getDeviceCurrent();
				if (skServer != null) {
					skServer.setState(SKServer.CONNECTED);
					viewDevice.setNameDevice(skServer.getName());
				}
				if (listDeviceListener != null) {
					listDeviceListener.OnShowListDevice();
				}
				
				if (status.danceMode() == 1) {
					danceLinkView.setLayout(TouchDanceLinkView.DANCE);
					if(viewChinhMiDi != null){
						viewGiamTone.invalidate();
						viewTangTone.invalidate();
						viewChamDiem.invalidate();
					}
				} else {
					danceLinkView.setLayout(TouchDanceLinkView.NODANCE);
					
					if(viewChinhMiDi != null){
						viewGiamTone.invalidate();
						viewTangTone.invalidate();
						viewChamDiem.invalidate();
					}
				}
				
				if(viewChinhMiDi != null){
					if(MyApplication.flagSongPlayPause != viewTamDung.isPlayPause()){
						MyApplication.flagSongPlayPause = viewTamDung.isPlayPause();
						
						viewChinhMiDi.invalidate();
						
						viewGiamAm.invalidate();
						viewTangAm.invalidate();
						viewTachLoi.invalidate();
						viewQuaBai.invalidate();
						viewHatLai.invalidate();						
						viewTamDung.invalidate();
						viewGiamTone.invalidate();
						viewTangTone.invalidate();
						
						viewMelodyGiam.invalidate();
						viewMelodyTang.invalidate();
						viewTempoGiam.invalidate();
						viewTempoTang.invalidate();
						viewTone.invalidate();
						viewHoaAm.invalidate();
						viewMacDinh.invalidate();
					}	
				}				
				
				if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
						|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
						|| MyApplication.intSvrModel == MyApplication.SONCA_KBX9 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
						 || MyApplication.intSvrModel == MyApplication.SONCA_TBT){
					((MyApplication)getApplication()).getHIW_Firmware();
					((MyApplication)getApplication()).setOnReceiverHIW_FirmwareListener(new OnReceiverHIW_FirmwareListener() {
						
						@Override
						public void OnReceiverHIW_Firmware(String resultString, String daumay_name,
								String daumay_version, String wifi_version, int wifi_revision) {
							MyLog.e("OnReceiverHIW_Firmware","START...........................");
							MyLog.e("","getResult = " + resultString);
							if(resultString == null){
								MyLog.e("OnReceiverHIW_Firmware","END...........................");
								return;
							}
							
							MyLog.e("","daumay_name = " + daumay_name);
							MyLog.e("","daumay_version = " + daumay_version);
							MyLog.e("","wifi_version = " + wifi_version);
							MyLog.e("","wifi_revision = " + wifi_revision);		
							
							int int_daumay_version = Integer.parseInt(daumay_version);
							int int_wifi_version = Integer.parseInt(wifi_version);
							
							MyApplication.curHiW_firmwareInfo = new HiW_FirmwareInfo(daumay_name, int_daumay_version, int_wifi_version, wifi_revision);
							
							viewWifi.invalidate();
							
							MyLog.e("OnReceiverHIW_Firmware","END...........................");
						}
					});	
				}
				
				if(boolShowKaraoke){
					if(colorLyricListener != null){
						colorLyricListener.OnMain_UpdateSocket(((MyApplication) getApplication()).getDeviceCurrent().getName());
						
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								if(status != null && colorLyricListener != null){
									if(MyApplication.intSvrModel == MyApplication.SONCA || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
										colorLyricListener.OnMain_NewSong(status.getPlayingSongID(), status.getMediaType(), status.getMidiShiftTime(), status.getPlayingSongTypeABC());
									} else {
										colorLyricListener.OnMain_NewSong(status.getPlayingSongID(), status.getMediaType(), status.getMidiShiftTime(), -1);
									}	
								
								karaoke_ColorID = status.getPlayingSongID();
							}
								
							}
						}, 500);	
					}
					
				}
			} else {
//				MyLog.i(" ", " ");
//				MyLog.i("SYNC MAIN", " isSingerOn = " + status.isSingerOn());
//				MyLog.i(" ", " ");
				
				if(MyApplication.flagModelA != status.isModelA()){
					MyApplication.flagModelA = status.isModelA();
				}		
				
				if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
					AppSettings setting = AppSettings.getInstance(getApplicationContext());
					if(setting.getCountAddFile() != status.getCountAddFile()){
						setting.saveCountAddFile(status.getCountAddFile());
						processGetFileAddSong();
						if(dialogNoAddPlayList != null){
							dialogNoAddPlayList.hideToastBox();
						}
					}	
					
					if(MyApplication.flagDownloadYouTube != status.isDownloadingYouTube()){
						MyApplication.flagDownloadYouTube = status.isDownloadingYouTube();
						
						if (onKTVMainListener != null) {
							onKTVMainListener.OnSK90009();
						}
						
						hideshowDialogConnect();
					}
					
					if(MyApplication.youtube_Download_ID != status.getYoutubeDownloadID()){
						MyApplication.youtube_Download_ID = status.getYoutubeDownloadID();						
													
						if (onKTVMainListener != null) {
							onKTVMainListener.OnSK90009();
						}
					}
					
					if(MyApplication.youtube_Download_percent != status.getYoutubeDownloadPercent()){
						MyApplication.youtube_Download_percent = status.getYoutubeDownloadPercent();						
						
						if (onKTVMainListener != null) {
							onKTVMainListener.OnSK90009();
						}
					}							
					
				}
				
				if(flagRunHide){
					hideActionBar();	
				}				
				
				if (status.isFlagOffUserList() != MyApplication.bOffUserList) {
					MyLog.e("SYNC", "change app bOffUserList");
					MyApplication.bOffUserList = status.isFlagOffUserList();
					reloadSongList();
					
//					if(MyApplication.intSvrModel != MyApplication.SONCA_SMARTK){
//						layoutDownload.setVisibility(View.GONE);
//						layoutAnimation.setVisibility(View.VISIBLE);
//						if (MyApplication.bOffUserList) {
//							animationView.showAdminUser(TouchAnimationView.OFF_USER_SUCCESS);
//						} else {
//							animationView.showAdminUser(TouchAnimationView.ON_USER_SUCCESS);
//						}	
//					}					
					TouchListDeviceFragment.refreshListView();
				}
								
				if(serverStatus.isOnOffControlFullAPI()){
					boolean bool = ((MyApplication)getApplication()).isOnFirst();
					if (MyApplication.bOffFirst != bool) {
						MyApplication.bOffFirst = bool;
						if(mainListener != null){
							mainListener.OnUpdateView();
						}
					}
				}else{
					boolean bool = ((MyApplication)getApplication()).getCommandEnable();
					if (MyApplication.bOffFirst != bool) {
						MyApplication.bOffFirst = bool;
						if(mainListener != null){
							mainListener.OnUpdateView();
						}
					}
				}
				if(serverStatus.isOnOffControlFullAPI()){
					if (status.getOnOffControlFullValue() != MyApplication.intSavedCommandMedium) {
						MyLog.e("SYNC", "change admin control medium");
						
						MyApplication.intCommandMedium = status.getOnOffControlFullValue();
						MyApplication.intSavedCommandMedium = status.getOnOffControlFullValue();
								
						viewWifi.invalidate();
						viewQuaBai.invalidate();
						viewTachLoi.invalidate();
						viewHatLai.invalidate();
						viewGiamAm.invalidate();
						viewTangAm.invalidate();
						
						if(viewChinhMiDi != null){
							viewChinhMiDi.invalidate();
							
							viewNgatTieng.invalidate();
							viewTamDung.invalidate();
							viewGiamTone.invalidate();
							viewTangTone.invalidate();
							viewDance.invalidate();
							viewChamDiem.invalidate();
							
							viewMelodyGiam.invalidate();
							viewMelodyTang.invalidate();
							viewTempoGiam.invalidate();
							viewTempoTang.invalidate();
							viewTone.invalidate();
							viewHoaAm.invalidate();
							viewMacDinh.invalidate();
						}
						
						if(commandBox != null){
							commandBox.syncFromServer();
						}					
						
						if(dialogSettingApp != null){
							dialogSettingApp.syncFromServer();
						}
						
						if(colorLyricListener!=null){
							colorLyricListener.OnMain_UpdateControl();
						}
						
					}
				}else{
					if ((~status.getAdminControlValue()) != MyApplication.intSavedCommandEnable) {
						MyLog.e("SYNC", "change admin control enable");
						
						MyApplication.intCommandEnable = (~status.getAdminControlValue());
						MyApplication.intSavedCommandEnable = (~status.getAdminControlValue());
							
						viewWifi.invalidate();
						viewQuaBai.invalidate();
						viewTachLoi.invalidate();
						viewHatLai.invalidate();
						viewGiamAm.invalidate();
						viewTangAm.invalidate();
						
						if(viewChinhMiDi != null){
							viewChinhMiDi.invalidate();
							
							viewNgatTieng.invalidate();
							viewTamDung.invalidate();
							viewGiamTone.invalidate();
							viewTangTone.invalidate();
							viewDance.invalidate();
							viewChamDiem.invalidate();
							
							viewMelodyGiam.invalidate();
							viewMelodyTang.invalidate();
							viewTempoGiam.invalidate();
							viewTempoTang.invalidate();
							viewTone.invalidate();
							viewHoaAm.invalidate();
							viewMacDinh.invalidate();
						}
						
						if(commandBox != null){
							commandBox.syncFromServer();
						}
					}
				}
				
				if (status.isFlagOffUserList() != MyApplication.bOffUserList) {
					MyLog.e("SYNC", "change app bOffUserList");
					MyApplication.bOffUserList = status.isFlagOffUserList();
					reloadSongList();
				}
				
				if (status.getMediaType() != serverStatus.getMediaType()) {
					serverStatus.setMediaType(status.getMediaType());
					if (status.getPlayingSongID() != 0) {
						UpdateControlView(serverStatus.getMediaType());
						viewQuaBai.setEnableView(View.VISIBLE);
						viewHatLai.setEnableView(View.VISIBLE);						
						viewGiamAm.setEnableView(View.VISIBLE);
						viewTangAm.setEnableView(View.VISIBLE);
						
						if(viewChinhMiDi != null){
							viewNgatTieng.setEnableView(View.VISIBLE);
							viewTamDung.setEnableView(View.VISIBLE);
							viewHoaAm.invalidate();
							viewMacDinh.invalidate();
						}
						
					} else {
//						groupView.setPlayPause(false);
						viewQuaBai.setEnableView(View.INVISIBLE);
						viewTachLoi.setEnableView(View.INVISIBLE);
						viewHatLai.setEnableView(View.INVISIBLE);
						
						if(viewChinhMiDi != null){
							viewChinhMiDi.invalidate();
							
							viewTamDung.setEnableView(View.INVISIBLE);
							viewChamDiem.setEnableView(View.INVISIBLE);
														
							viewGiamTone.setEnableView(View.INVISIBLE);
							viewTangTone.setEnableView(View.INVISIBLE);
							
							viewMelodyGiam.setEnableView(View.INVISIBLE);
							viewMelodyTang.setEnableView(View.INVISIBLE);
							viewTempoGiam.setEnableView(View.INVISIBLE);
							viewTempoTang.setEnableView(View.INVISIBLE);
							viewTone.setEnableView(View.INVISIBLE);
							viewHoaAm.invalidate();
							viewMacDinh.invalidate();
						}
					}
				}					
				
				if(status.getCurrentTempo() == 0 && viewWifi.getBlockStatus()){
					viewWifi.invalidate();
				}
				if(status.getCurrentTempo() != 0 && !viewWifi.getBlockStatus()){
					viewWifi.invalidate();
				}
				
				if (status.getCurrentVolume() != viewGiamAm.getVolume()) {
					int maxVolume = 16;
					if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
							|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
							 || MyApplication.intSvrModel == MyApplication.SONCA_KBX9 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
							 || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK || MyApplication.intSvrModel == MyApplication.SONCA_TBT){
						maxVolume = 15;
					}
					
					int newVolume = status.getCurrentVolume();
					if(newVolume == maxVolume){
						viewGiamAm.setEnableView(View.VISIBLE);
						viewTangAm.setEnableView(View.INVISIBLE);
					} else if (newVolume == 0){
						viewGiamAm.setEnableView(View.INVISIBLE);
						viewTangAm.setEnableView(View.VISIBLE);
					} else {
						viewGiamAm.setEnableView(View.VISIBLE);
						viewTangAm.setEnableView(View.VISIBLE);
					}
					
					viewGiamAm.setVolume(newVolume);
				}
				
				if(viewChinhMiDi != null){
					if (status.getCurrentTone() != viewTone.getToneView()){
						viewTone.setToneView(status.getCurrentTone());
					}						
					
					if (status.getCurrentTempo() != viewTempoGiam.getTempo()){
						viewTempoGiam.setTempo(status.getCurrentTempo());
						if(boolShowKaraoke){
							new Handler().postDelayed(new Runnable() {
								@Override
								public void run() {
									showDialogMessage(getResources().getString(R.string.msg_kara_1));
								}
							}, 1000);	
						}

						hideKaraoke();
					}			
					
					if (status.getCurrentMelody() != viewMelodyGiam.getMelody()){
						viewMelodyGiam.setMelody(status.getCurrentMelody());
					}					
					
					if (status.getCurrentKey() != viewGiamTone.getKey()){
						viewGiamTone.setKey(status.getCurrentKey());
					}
					
					if(viewNgatTieng.getMute()!= status.isMuted()){
						viewNgatTieng.setMute(status.isMuted());
					}
					
					if (status.isPlaying() != viewTamDung.isPlayPause()){
						viewTamDung.setPlayPause(!status.isPaused());
						
						if(colorLyricListener != null){
							colorLyricListener.OnMain_PausePlay(status.isPlaying());
						}
						
						viewChinhMiDi.invalidate();
						
						viewTachLoi.invalidate();
						viewChamDiem.invalidate();
						viewGiamTone.invalidate();
						viewTangTone.invalidate();
						
						viewMelodyGiam.invalidate();
						viewMelodyTang.invalidate();
						viewTempoGiam.invalidate();
						viewTempoTang.invalidate();
						viewTone.invalidate();
						viewHoaAm.invalidate();
						viewMacDinh.invalidate();
					}
										
					if ((status.danceMode() == 1) != viewDance.getDanceMode()) {
						boolean flagDance = status.danceMode() == 1;
						viewDance.setDanceMode(flagDance);
						
						if(colorLyricListener != null){
							colorLyricListener.OnMain_Dance(flagDance);
						}
						
						if(flagDance){
							danceLinkView.setLayout(TouchDanceLinkView.DANCE);
							showFragPlaylist();
						} else {
							danceLinkView.setLayout(TouchDanceLinkView.NODANCE);
							showFragSwitch();
						}
						
						if(viewChinhMiDi != null){
							viewGiamTone.invalidate();
							viewTangTone.invalidate();
							viewChamDiem.invalidate();
						}
						viewWifi.invalidate();
					}
					
					if (status.getCurrentScore() != viewChamDiem.getScoreView()){
						viewChamDiem.setScoreView(status.getCurrentScore());
					}							
					
				}				
				
				if (status.isSingerOn() != viewTachLoi.isSingerView()){
					viewTachLoi.setSingerView(status.isSingerOn());
					if(colorLyricListener != null){
						colorLyricListener.OnMain_VocalSinger(status.isSingerOn());
					}
				}	
				
				if(fragmentManager.findFragmentByTag(ONE_HELLO) != null){
					if (onToHelloListener != null) {
						onToHelloListener.OnUpdateCommad(status);
					}
				}		
				
				ArrayList<SongID> songIDs = status.getReservedSongs();		
				if (songIDs != null) {
					boolean notify = ((MyApplication)getApplication()).CheckNotifyPlayList(songIDs);
					ArrayList<Song> list = new ArrayList<Song>();
					for (int i = 0; i < songIDs.size(); i++) {
						SongID songID = songIDs.get(i);
						list.add(new Song(songID.songID , songID.typeABC));
					}
					
					if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
						if (status.getPlayingState() != serverStatus.getPlayingState()
								&& status.getPlayingState() == 1) {
							if(status.getMediaType() == 7){
								status.setPlayingState(serverStatus.getPlayingState());
							} else {
								MyLog.e(TAB, "CALL 1 CALL 1");
								MyLog.e(TAB, "CALL 1 CALL 1");
								karaoke_ColorID = status.getPlayingSongID();
								if(colorLyricListener != null){
									colorLyricListener.OnMain_NewSong(status.getPlayingSongID(), status.getMediaType(), status.getMidiShiftTime(), status.getPlayingSongTypeABC());	
								}
									
							}
							
						}
					}
					
					if (notify) {
						MyLog.e("SYNC NOFITY", notify + "");
						
						curSongIDs = songIDs;
						((MyApplication)getApplication()).setListActive(list);
						
						setNextSongDB();	
						
						numberPlaylist.setNumberPlaylist(songIDs.size());
						viewDachon.setSumSong(songIDs.size());
						
						if(colorLyricListener != null){
							colorLyricListener.OnMain_setCntPlaylist(songIDs.size());
						}
						
						if (onKTVMainListener != null) {
							onKTVMainListener.OnSK90009();
						}
						
					}
				}
				
				if (status.getPlayingSongID() != serverStatus
						.getPlayingSongID()
						|| status.getPlayingSongTypeABC() != serverStatus
								.getPlayingSongTypeABC()) {				
					if(colorLyricListener != null){
						MyLog.e(TAB, "CALL 2 CALL 2");
						MyLog.e(TAB, "CALL 2 CALL 2");
						
						karaoke_ColorID = status.getPlayingSongID();
						if(MyApplication.intSvrModel == MyApplication.SONCA || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
							colorLyricListener.OnMain_NewSong(status.getPlayingSongID(), status.getMediaType(), status.getMidiShiftTime(), status.getPlayingSongTypeABC());
						} else {
							colorLyricListener.OnMain_NewSong(status.getPlayingSongID(), status.getMediaType(), status.getMidiShiftTime(), -1);
						}
					}
					
					viewWifi.invalidate();
					
					String name = status.getPlayingSongName();
					if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
							|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
							|| MyApplication.intSvrModel == MyApplication.SONCA_KBX9 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
							 || MyApplication.intSvrModel == MyApplication.SONCA_TBT){
						int id = status.getPlayingSongID();
						ArrayList<Song> songs = DBInterface.DBSearchSongID(
								id + "", 0 + "", context);
						if (!songs.isEmpty()) {
							name = songs.get(0).getName();
						}
					}
					if(name != null && !name.equals(" ") && !name.equals("")){
						
					} else {
						int id = status.getPlayingSongID();
						ArrayList<Song> songs = DBInterface.DBSearchSongID(
								id + "", status.getPlayingSongTypeABC() + "", context);
						if (!songs.isEmpty()) {
							name = songs.get(0).getName();
						} else {
							if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
								ArrayList<Song> songsYouTube = DBInterface.DBSearchSongID_YouTube(id + "", 0 + "", context);
								if (!songsYouTube.isEmpty()) {
									name = songsYouTube.get(0).getName();
								} else {
									name = "";
								}
							} else {
								name = "";
							}
						}
					}
					tempGroupViewSongName = name;
					
					if(dialogFlower != null){
						dialogFlower.syncNameSong(name);
					}
					
				}			
				
				if(commandBox != null){
					commandBox.syncScoreFromServer(status);
				}
				
				if(dialogFlower != null){
					dialogFlower.syncPauPlay(status);
				}
				
				if(viewChinhMiDi != null){
					if(MyApplication.flagSongPlayPause != viewTamDung.isPlayPause()){
						MyApplication.flagSongPlayPause = viewTamDung.isPlayPause();
						
						viewChinhMiDi.invalidate();
						
						viewGiamAm.invalidate();
						viewTangAm.invalidate();
						viewTachLoi.invalidate();
						viewQuaBai.invalidate();
						viewHatLai.invalidate();
						
						viewTamDung.invalidate();
						viewGiamTone.invalidate();
						viewTangTone.invalidate();
						
						viewChamDiem.invalidate();
						
						viewMelodyGiam.invalidate();
						viewMelodyTang.invalidate();
						viewTempoGiam.invalidate();
						viewTempoTang.invalidate();
						viewTone.invalidate();
						viewHoaAm.invalidate();
						viewMacDinh.invalidate();
					}	
				}	
				
				if(MyApplication.flagPlayingYouTube != status.isPlayingYouTube()){
					MyLog.e(" ", " ");
					MyLog.i("SYNC MAIN", " isPlayingYouTube = " + status.isPlayingYouTube());
					
					MyApplication.flagPlayingYouTube = status.isPlayingYouTube();
					
					if(MyApplication.flagPlayingYouTube){						
						if(dialogFlower != null){
							dialogFlower.forceCloseDialog();
							showDialogMessage(getResources().getString(R.string.msg_tanghoa_3a) + "." + getResources().getString(R.string.msg_tanghoa_3b));
						}
						
						if(dialogAddSongData != null){
							drawerLayout.closeDrawers();
							dialogAddSongData.dismissDialog();
							showDialogMessage(getResources().getString(R.string.toc_mod_warn));
						}
						
					}
					
//					groupView.invalidate();
					viewWifi.invalidate();
					UpdateControlView(status.getMediaType());
				}
				
				try {
					syncDialogCaoCap(status);
					
					if(boolShowKaraoke){
						((FragmentKaraoke)fragment_Karaoke).syncFromServer(status);
					}	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			serverStatus = status;
		}
	}

	private void EnableControlView() {
		viewGiamAm.setEnableView(View.INVISIBLE);
		viewTangAm.setEnableView(View.INVISIBLE);
		viewTachLoi.setEnableView(View.INVISIBLE);
		viewCaocap.setEnableView(View.INVISIBLE);
		viewWifi.setEnableView(View.INVISIBLE);
		viewQuaBai.setEnableView(View.INVISIBLE);
		viewDachon.setEnableView(View.INVISIBLE);
		viewQuayLai.setEnableView(View.VISIBLE);
		
		if(viewChinhMiDi != null){
			viewHatLai.setEnableView(View.INVISIBLE);
			viewNgatTieng.setEnableView(View.VISIBLE);
			viewChinhMiDi.setEnableView(View.INVISIBLE);
			viewTamDung.setEnableView(View.INVISIBLE);
			
			viewGiamTone.setEnableView(View.INVISIBLE);
			viewTangTone.setEnableView(View.INVISIBLE);
			viewDance.setEnableView(View.INVISIBLE);
			viewChamDiem.setEnableView(View.INVISIBLE);
			
			viewMelodyGiam.setEnableView(View.INVISIBLE);
			viewMelodyTang.setEnableView(View.INVISIBLE);
			viewTempoGiam.setEnableView(View.INVISIBLE);
			viewTempoTang.setEnableView(View.INVISIBLE);
			viewTone.setEnableView(View.INVISIBLE);
			viewHoaAm.invalidate();
			viewMacDinh.setEnableView(View.INVISIBLE);
		}
	}

	private void UpdateControlView(int type) {		
		viewGiamAm.setEnableView(View.VISIBLE);
		viewTangAm.setEnableView(View.VISIBLE);
		viewTachLoi.setEnableView(View.VISIBLE);
		viewCaocap.setEnableView(View.VISIBLE);
		viewWifi.setEnableView(View.VISIBLE);
		viewQuaBai.setEnableView(View.VISIBLE);
		viewDachon.setEnableView(View.VISIBLE);
		viewQuayLai.setEnableView(View.VISIBLE);
		
		if(viewChinhMiDi != null){
			viewHatLai.setEnableView(View.VISIBLE);
			viewChinhMiDi.invalidate();
			
			viewNgatTieng.setEnableView(View.VISIBLE);
			viewChinhMiDi.setEnableView(View.INVISIBLE);
			viewTamDung.setEnableView(View.VISIBLE);
			viewDance.setEnableView(View.VISIBLE);
			
			viewChamDiem.setEnableView(View.VISIBLE);
			viewMelodyGiam.setEnableView(View.INVISIBLE);
			viewMelodyTang.setEnableView(View.INVISIBLE);
			viewTempoGiam.setEnableView(View.INVISIBLE);
			viewTempoTang.setEnableView(View.INVISIBLE);
			viewTone.setEnableView(View.VISIBLE);
			viewHoaAm.invalidate();
			viewMacDinh.setEnableView(View.VISIBLE);
		}
		
		if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && MyApplication.flagPlayingYouTube){					
			MyLog.i(" ", "UpdateControlView - YOUTUBE");
			viewWifi.setEnableView(View.INVISIBLE);
			viewTachLoi.setEnableView(View.INVISIBLE);
			viewGiamAm.setEnableView(View.VISIBLE);
			viewTangAm.setEnableView(View.VISIBLE);
			
			if(viewChinhMiDi != null){
				viewNgatTieng.setEnableView(View.VISIBLE);
				viewChinhMiDi.setEnableView(View.INVISIBLE);
				viewTamDung.setPlayPause(true);
				viewGiamTone.setEnableView(View.INVISIBLE);
				viewTangTone.setEnableView(View.INVISIBLE);
				viewChamDiem.setEnableView(View.INVISIBLE);
				
				viewMelodyGiam.setEnableView(View.INVISIBLE);
				viewMelodyTang.setEnableView(View.INVISIBLE);
				viewTempoGiam.setEnableView(View.INVISIBLE);
				viewTempoTang.setEnableView(View.INVISIBLE);
				viewTone.setEnableView(View.INVISIBLE);
			}
			
			return;
		}
		
		if (type == 0x07) {
			return;
		}
		
		
		MEDIA_TYPE aType = MEDIA_TYPE.values()[type];
		if (aType == MEDIA_TYPE.MIDI) {
			viewTachLoi.setEnableView(View.INVISIBLE);
			
			if(viewChinhMiDi != null){
				viewChinhMiDi.setEnableView(View.VISIBLE);
				viewGiamTone.setEnableView(View.VISIBLE);
				viewTangTone.setEnableView(View.VISIBLE);
				
				viewMelodyGiam.setEnableView(View.VISIBLE);
				viewMelodyTang.setEnableView(View.VISIBLE);
				viewTempoGiam.setEnableView(View.VISIBLE);
				viewTempoTang.setEnableView(View.VISIBLE);
			}
			
			return;
		} else if (aType == MEDIA_TYPE.MP3) {
			viewTachLoi.setEnableView(View.INVISIBLE);		
			
			if(viewChinhMiDi != null){
				viewGiamTone.setEnableView(View.VISIBLE);
				viewTangTone.setEnableView(View.VISIBLE);
			}
			
			return;
		} else if (aType == MEDIA_TYPE.SINGER) {
			viewTachLoi.setEnableView(View.VISIBLE);
			
			if(viewChinhMiDi != null){
				viewGiamTone.setEnableView(View.VISIBLE);
				viewTangTone.setEnableView(View.VISIBLE);
			}
			
			return;
		} else if (aType == MEDIA_TYPE.VIDEO) {
			viewTachLoi.setEnableView(View.VISIBLE);
			
			if(viewChinhMiDi != null){
				viewGiamTone.setEnableView(View.VISIBLE);
				viewTangTone.setEnableView(View.VISIBLE);
			}
			
			return;
		} else {
			viewTachLoi.setEnableView(View.INVISIBLE);
			
			if(viewChinhMiDi != null){
				viewGiamTone.setEnableView(View.INVISIBLE);
				viewTangTone.setEnableView(View.INVISIBLE);
				viewTone.setEnableView(View.INVISIBLE);
			}
		}
	}

	// ///////////////////// Network Socket Listener
	// ////////////////////////////

	
	private void DefaultViewWhenPause(){	
		
		MyApplication.flagDance = false;
		
		if(dialogCaoCap != null){
			dialogCaoCap.dismissDialog();
			dialogCaoCap = null;
		}
		
		MyApplication.flagModelA = false;
		
		MyApplication.curHiW_firmwareInfo = null;
		MyApplication.curHiW_firmwareConfig = null;
				
		MyApplication.intCommandMedium = 0x00000000;
		MyApplication.intSavedCommandMedium = 0x00000000;
		MyApplication.intCommandEnable = 0xffff;
		MyApplication.bOffFirst = true;
		hideBlockCommand();
		if(dialogHiW != null){
			dialogHiW.hideToastBox();
		}
		stopTimerPing();		
		serverStatus = null;
		isScanning = false;
		MyApplication.intWifiRemote = MyApplication.SONCA;
		MyApplication.intSvrModel = 0;
		ChangeWifiRmote();
		EnableControlView();
		numberPlaylist.setNumberPlaylist(-1);
		viewDachon.setSumSong(0);
//		groupView.setPlayPause(false);
		tempGroupViewSongName = "";
		viewDevice.setNameDevice("");
		
		Fragment fragPlaylist = fragmentManager.findFragmentByTag(PLAYLIST);
		if(fragPlaylist != null){
			gotoHomeMenu();
		}
		
		drawerLayout.closeDrawers();
		
		danceLinkView.setLayout(TouchDanceLinkView.NODANCE);
		findViewById(R.id.layoutLinkDance).setVisibility(View.GONE);
		viewDance.setDanceMode(false);
		
		if(colorLyricListener != null){
			colorLyricListener.OnMain_setCntPlaylist(0);
		}
		
		// -----------//
		((MyApplication) getApplication()).setListActive(new ArrayList<Song>());
		((MyApplication) getApplication()).setListActive(new ArrayList<Song>());
		((MyApplication) getApplication()).setDeviceCurrent(new SKServer());
		((MyApplication) getApplication()).cancelSyncServerStatus();
		((MyApplication) getApplication()).disconnectFromRemoteHost();
		((MyApplication) getApplication()).onDestroy();

	}
	
	private void DefaultViewWhenDisConect() {
		MyLog.e(TAB, "DefaultViewWhenDisConect()");
		
		MyApplication.flagDance = false;
		
		if(dialogCaoCap != null){
			dialogCaoCap.dismissDialog();
			dialogCaoCap = null;
		}
		
		if(dialogAddSongData != null){
			dialogAddSongData.dismissDialog();
			dialogAddSongData = null;
		}
		
		if (onKTVMainListener != null) {
			onKTVMainListener.OnSK90009();
		}
		
		MyApplication.flagModelA = false;
		
		MyApplication.curHiW_firmwareInfo = null;
		MyApplication.curHiW_firmwareConfig = null;
		
		boolean flagTimerIn = serverStatus == null;
		
		if(dialogFlower != null){
			dialogFlower.dismissDialog();
			dialogFlower = null;
		}
		MyApplication.intCommandMedium = 0x00000000;
		MyApplication.intSavedCommandMedium = 0x00000000;
		MyApplication.intCommandEnable = 0xffff;
		MyApplication.bOffFirst = true;
		hideBlockCommand();
		
		Fragment fragPlaylist = fragmentManager.findFragmentByTag(PLAYLIST);
		if(fragPlaylist != null){
			gotoHomeMenu();
		}
		
		HideDialogSettingApp();
		
		if(dialogHiW != null){
			dialogHiW.hideToastBox();
		}
		serverStatus = null;
		isScanning = false;
		MyApplication.intWifiRemote = MyApplication.SONCA;
		MyApplication.intSvrModel = 0;
		ChangeWifiRmote();
		EnableControlView();
		numberPlaylist.setNumberPlaylist(-1);
		viewDachon.setSumSong(0);
//		groupView.setPlayPause(false);
		tempGroupViewSongName = "";
		viewDevice.setNameDevice("");
		groupView_StopTimerAutoConnect();
		groupView_StartTimerAutoConnect();
		// -----------//
		layoutDownload.setVisibility(View.GONE);
		layoutAnimation.setVisibility(View.VISIBLE);
		
		if(colorLyricListener != null){
			colorLyricListener.OnMain_setCntPlaylist(0);
			colorLyricListener.OnMain_RemoveSocket();
		}
		
		danceLinkView.setLayout(TouchDanceLinkView.NODANCE);
		findViewById(R.id.layoutLinkDance).setVisibility(View.GONE);
		viewDance.setDanceMode(false);

		((MyApplication) getApplication()).setListActive(new ArrayList<Song>());
		((MyApplication) getApplication()).setDeviceCurrent(new SKServer());
		((MyApplication) getApplication()).cancelSyncServerStatus();
		((MyApplication) getApplication()).disconnectFromRemoteHost();

		if (onKTVMainListener != null) {
			onKTVMainListener.OnSK90009();
		}
	}

	private int countDownload = 0;

	@Override
	public void deviceDidReceiveDataOfLength(int loaded, int total) {
		isDownloadingDauMay = true;
		
		if(boolShowKaraoke){
			if (layoutDownload.getVisibility() != View.GONE) {
				layoutDownload.setVisibility(View.GONE);
			}
			return;
		}
		
		countDownload++;
		if (countDownload >= 100) {
			if (layoutDownload.getVisibility() != View.VISIBLE) {
				layoutDownload.setVisibility(View.VISIBLE);
			}
			if (layoutAnimation.getVisibility() != View.GONE) {
				layoutAnimation.setVisibility(View.GONE);
			}
			int per = loaded / (total / 100);
			downloadView.setDownloadPercent(per, total);
			countDownload = 0;
		}
	}

	@Override
	public void deviceDidConnected(ServerSocket arg0) {
		MyLog.e(TAB, "deviceDidConnected()");
		if (deviveFragmentListener != null) {
			deviveFragmentListener.OnConnected();
		}
	}

	private boolean flagAllowConnect = true;
	
	@Override
	public void OnDisConnect(SKServer sk) {
		
		MyApplication.flagDance = false;
		
		if(dialogCaoCap != null){
			dialogCaoCap.dismissDialog();
			dialogCaoCap = null;
		}
		
		MyApplication.flagModelA = false;
		
		flagAllowConnect = false;
		MyApplication.intCommandMedium = 0x00000000;
		MyApplication.intSavedCommandMedium = 0x00000000;
		MyApplication.intCommandEnable = 0xffff;
		MyApplication.bOffFirst = true;
		hideBlockCommand();
		if(dialogHiW != null){
			dialogHiW.hideToastBox();
		}
		serverStatus = null;
		isScanning = false;
		MyApplication.intSvrModel = 0;
		MyApplication.intWifiRemote = MyApplication.SONCA;
		ChangeWifiRmote();
		EnableControlView();
		numberPlaylist.setNumberPlaylist(-1);
		viewDachon.setSumSong(0);
		if(colorLyricListener != null){
			colorLyricListener.OnMain_setCntPlaylist(0);
		}
		
		danceLinkView.setLayout(TouchDanceLinkView.NODANCE);
		findViewById(R.id.layoutLinkDance).setVisibility(View.GONE);
		viewDance.setDanceMode(false);
		
//		groupView.setPlayPause(false);
		tempGroupViewSongName = "";
		viewDevice.setNameDevice("");

		groupView_StopTimerAutoConnect();
		groupView_StartTimerAutoConnect();
		
		// -----------//
		layoutDownload.setVisibility(View.GONE);
		layoutAnimation.setVisibility(View.VISIBLE);

		Fragment fragPlaylist = fragmentManager.findFragmentByTag(PLAYLIST);
		if(fragPlaylist != null){
			gotoHomeMenu();
		}
		
		if (!drawerLayout.isDrawerOpen(layoutConnect)) {	
			drawerLayout.openDrawer(layoutConnect);
			ShowListDevice("");
		}
		// -----------//
		((MyApplication) getApplication()).setListActive(new ArrayList<Song>());
		((MyApplication) getApplication()).setDeviceCurrent(new SKServer());
		((MyApplication) getApplication()).cancelSyncServerStatus();
		((MyApplication) getApplication()).disconnectFromRemoteHost();
		if(isDestroyMainActivity == true)
		if (((MyApplication) getApplication()).getSocket() != null) {
			if (listDeviceListener != null && !isScanning) {
				isScanning = true;
				((MyApplication) getApplication()).setListServers(null);
				listDeviceListener.OnDisplayProgressScan(true);
				((MyApplication) getApplication()).searchNearbyDevice();			
			}
		}
		if (listDeviceListener != null) {
			SKServer skServer = ((MyApplication) getApplication())
					.getDeviceCurrent();
			if (skServer != null) {
				skServer.setState(SKServer.BROADCASTED);
			}
			listDeviceListener.OnShowListDevice();
		}
		if (onKTVMainListener != null) {
			onKTVMainListener.OnSK90009();
		}
	}

	private boolean skipDownload = false;
	
	@Override
	public void deviceSendRequestDidFailedWithError(String msg) {
		MyLog.e(TAB, "deviceSendRequestDidFailedWithError = " + msg);
		allowTimerPing = true;
		if(msg.equals("busy socket")){			
			isScanning = false;
			return;
		}
		
		if(msg.equals("read socket")){
			// do something
			isScanning = false;
			return;
		}
		
		if(msg.equals("sync timeout")){
			((MyApplication) getApplication()).disconnectFromRemoteHost();
			return;
		}
		
		if(msg.equals("yt_down_full")){
			showDialogMessage(context.getString(R.string.youtube_down_9));
			return;
		}
		
		if(msg.equals("yt_down_limit")){
			showDialogMessage(context.getString(R.string.youtube_down_10));
			return;
		}
		
		if(msg.equals("yt_down_exist")){
			showDialogMessage(context.getResources().getString(R.string.youtube_down_11));
			return;
		}
		
		if(msg.equals("toc_modi_noUsb")){
			showDialogMessage(context.getString(R.string.toc_mod_2));
			return;
		}
		
		if(msg.equals("toc_modi_notInsideUsb")){
			showDialogMessage(context.getString(R.string.toc_mod_3a));
			return;
		}
		
		if(msg.equals("toc_modi_notInsideTOC")){
			showDialogMessage(context.getString(R.string.toc_mod_3b));
			return;
		}

		if(msg.equals("toc_modi_busy")){
			showDialogMessage(context.getString(R.string.toc_mod_4));
			return;
		}
		
		if(msg.equals("toc_modi_full")){
			showDialogMessage(context.getString(R.string.toc_mod_5));
			return;
		}
		
		if(msg.equals("toc_modi_limit")){
			showDialogMessage(context.getString(R.string.toc_mod_6));
			return;
		}
		
	}

	@Override
	public void deviceDidReceiveHeader(int contentLength) {
		MyLog.e(TAB, "deviceDidReceiveHeader()==> content length: "
		 + contentLength);
		SetKeepScreenOn(true);
	}

	public void deviceDidDisconnected() {
		MyLog.e(TAB, "deviceDidDisconnected()");
		flagAllowConnect = false;
		DefaultViewWhenDisConect();
				
		if (MyApplication.updateFirmwareServerSocket != null) {
			if (serviceIntent != null) {
				showDialogMessage(getResources().getString(R.string.msg_7));
			
				KTVMainActivity.this.stopService(serviceIntent);
				if (MyApplication.updateFirmwareServerSocket != null) {
					try {
						MyApplication.updateFirmwareServerSocket.close();
					} catch (Exception e) {

					}
				}
			}
			MyApplication.updateFirmwareServerSocket = null;
		}
		
		if(MyApplication.countStartApp < 2){ // start app first time			
			onUpdateAllFromServer();
		} 
		MyApplication.countStartApp = 2;
	}

	public void deviceUpdatePlayList(int[] list) {
		
	}

	// ----------------- HMINH
	private Dialog myDialogUpdatePic;
	private TouchPopupViewUpdatePic popupViewUpadtePic;
	private Dialog progressDialog;
	private boolean isDownloadingPic;
	private TouchPopupViewDownloadPercent popupPercent;
	private ArrayList<String> infoList = new ArrayList<String>();
	private int highestVersion = 0;
	private int saveVersion = 0;

	private boolean fromSingerTab = false;

	public void OnUpdatePicFromSingerTab() {
		fromSingerTab = true;
		OnUpdatePic();
	}

	public void OnUpdatePicAlone(int step) {
		try {
			if(!MyApplication.flagEverConnect){
				CheckUpdateFirmwareFromServer(context);
				return;
			}
			if (step == 1) {
				if (myDialogUpdateToc != null) {
					myDialogUpdateToc.dismiss();
				}
				// MyLog.e(TAB, "OnUpdatePicAlone()");

				pingTime = System.currentTimeMillis();
				new InterruptPingServerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
				startPingServer();
			} else if (step == 2) {
				// ----- DOWNLOAD FILE update_p.xml
				String appRootPath = android.os.Environment
						.getExternalStorageDirectory()
						.toString()
						.concat(String.format("/%s/%s", "Android/data",
								getPackageName()));
				File appBundle = new File(appRootPath);
				if (!appBundle.exists())
					appBundle.mkdir();

				final String updateInfo_path = appRootPath
						.concat("/update_t.xml");
				File tempF = new File(updateInfo_path);
				if (tempF.exists()) {
					tempF.delete();
				}

				new Thread(new Runnable() {
					public void run() {
						// MyLog.e(TAB, "OnUpdatePic() - Thread");
						isDownloadingPic = true;
						downloadFile(
								"https://kos.soncamedia.com/firmware/karconnect/update_t_v2.xml",
								updateInfo_path);
					}
				}).start();

			}
		} catch (Exception e) {
			isDownloadingPic = false;
            MyApplication.freezeTime = System.currentTimeMillis();
		}
	}
	
	class PingServerTask extends AsyncTask<String, Void, Void> {

		protected Void doInBackground(String... urls) {
			try {
				fromSingerTab = false;
				if (!isURLReachable(KTVMainActivity.this)) {					
					if(MyApplication.intWifiRemote == MyApplication.SONCA){
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								if(!isFinishing()){
								}
								
							}
						});
					}					
					
					return null;
				}

				flagInterrupt = false;
				OnUpdatePicAlone(2);
				return null;
			} catch (Exception e) {
				isDownloadingPic = false;
	            MyApplication.freezeTime = System.currentTimeMillis();
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
	            URL url = new URL("https://kos.soncamedia.com/firmware/karconnect/update_t_v2.xml");   // Change to "http://google.com" for www  test.
	            HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
	            urlc.setConnectTimeout(3 * 1000);          // 3 s.
	            urlc.connect();
	            if (urlc.getResponseCode() == 200) {        // 200 = "OK" code (http connection is fine).

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

	private LinearLayout belowLayout;
	private boolean allowCancelOutside = true;
	
	public void OnUpdatePic() {
		try {
			if (serverStatus != null && (MyApplication.intSvrModel == MyApplication.SONCA  || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK)) { // DANG CONNECT DAU MAY
				DownloadState = 3;
				deviceDidDownloadFile(true, "");
				return;
			}
			
			// ----- CHECK version

//			MyLog.e(TAB, "OnUpdatePic()");			
			
			onUpdateAllFromServer_Button();

		} catch (Exception e) {
			// MyLog.e(TAB, e);
		}
	}

	private void downloadPic() {
		try {
			final String appRootPath = android.os.Environment
					.getExternalStorageDirectory()
					.toString()
					.concat(String.format("/%s/%s", "Android/data",
							getPackageName()));
			File appBundle = new File(appRootPath);
			if (!appBundle.exists())
				appBundle.mkdir();

			if (progressDialog == null) {
				progressDialog = new Dialog(this);
				progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				progressDialog.getWindow().setBackgroundDrawable(
						new ColorDrawable(android.graphics.Color.TRANSPARENT));
				progressDialog
						.setContentView(R.layout.touch_item_popup_down_percent);

				WindowManager.LayoutParams pa = progressDialog.getWindow()
						.getAttributes();
				pa.height = WindowManager.LayoutParams.MATCH_PARENT;
				pa.width = WindowManager.LayoutParams.MATCH_PARENT;
				pa.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
						| WindowManager.LayoutParams.FLAG_FULLSCREEN;
				 
				progressDialog.setOnShowListener(new OnShowListener() {
					
					@Override
					public void onShow(DialogInterface dialog) {
						AlphaAnimation alpha = new AlphaAnimation(1F, 0.5F);
						alpha.setDuration(0);
						alpha.setFillAfter(true);
//						layoutMain.startAnimation(alpha);
					}
				});
				
				progressDialog.setOnDismissListener(new OnDismissListener() {
					
					@Override
					public void onDismiss(DialogInterface dialog) {
						AlphaAnimation alpha = new AlphaAnimation(0.5F, 1F);
						alpha.setDuration(0);
						alpha.setFillAfter(true);
//						layoutMain.startAnimation(alpha);
					}
				});
				 
				 popupPercent = (TouchPopupViewDownloadPercent) progressDialog
							.findViewById(R.id.myPopupPercent);
				 popupPercent
					.setOnPopupDownloadPercentListener(new OnPopupDownloadPercentListener() {
						@Override
						public void OnCancelDownload() {
							isDownloadingPic = false;
							progressDialog.dismiss();
							
							onUpdateVideoLyric(1);
						}
					});
			}
			
			progressDialog.getWindow().setGravity(Gravity.CENTER);
			WindowManager.LayoutParams params = progressDialog.getWindow()
					.getAttributes();
			progressDialog.getWindow().setAttributes(params);
			progressDialog.show();
			new Thread(new Runnable() {
				public void run() {
					isDownloadingPic = true;
					downloadMultipleFile();
				}
			}).start();
		} catch (Exception e) {
			isDownloadingPic = false;
            MyApplication.freezeTime = System.currentTimeMillis();
			progressDialog.dismiss();
		}
	}

	public void downloadMultipleFile() {
		try {
			String appRootPath = android.os.Environment
					.getExternalStorageDirectory()
					.toString()
					.concat(String.format("/%s/%s", "Android/data",
							getPackageName()));
			// Log.e("TEST LOG", infoList.size() + "");
			for (int i = 0; i < infoList.size(); i++) {
				String txtFileName = infoList.get(i).split("-")[1];
				String url = "https://kos.soncamedia.com/firmware/karconnect/"
						+ txtFileName;
				String dest_file_path = appRootPath.concat("/" + txtFileName);

				File dest_file = new File(dest_file_path);
				URL u = new URL(url);
				URLConnection conn = u.openConnection();
				final int contentLength = conn.getContentLength();
				DataInputStream fis = new DataInputStream(u.openStream());
				DataOutputStream fos = new DataOutputStream(
						new FileOutputStream(dest_file));

				int bytes_read = 0;
				int bytesReadTotal = 0;
				int buffer_size = 1024 * 1024;
				byte[] buffer = new byte[buffer_size];

				while ((bytes_read = fis.read(buffer, 0, buffer_size)) > 0) {
					if (!isDownloadingPic) {
						progressDialog.dismiss();
						fis.close();
						fos.flush();
						fos.close();
						return;
					}
					fos.write(buffer, 0, bytes_read);
					bytesReadTotal += bytes_read;
					final int progress = (int) (100.0f * bytesReadTotal / contentLength);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (progress == 0) {
								popupPercent.setTotalSize(contentLength);
							}
							popupPercent.setDownloadPercent(progress);
						}
					});
				}
				fis.close();
				fos.flush();
				fos.close();
			}
			hideProgressIndicator(2);
		} catch (Exception e) {
			isDownloadingPic = false;
            MyApplication.freezeTime = System.currentTimeMillis();
			return;
		}
	}

	public void downloadFile(String url, String dest_file_path) {
		try {
			File dest_file = new File(dest_file_path);
			URL u = new URL(url);
			DataInputStream fis = new DataInputStream(u.openStream());
			DataOutputStream fos = new DataOutputStream(new FileOutputStream(
					dest_file));

			int bytes_read = 0;
			int buffer_size = 1024 * 1024;
			byte[] buffer = new byte[buffer_size];

			while ((bytes_read = fis.read(buffer, 0, buffer_size)) > 0) {
				if (!isDownloadingPic) {
					progressDialog.dismiss();
					return;
				}
				fos.write(buffer, 0, bytes_read);
			}

			fis.close();
			fos.flush();
			fos.close();

			hideProgressIndicator(1);
		} catch (Exception e) {
			isDownloadingPic = false;
			MyApplication.freezeTime = System.currentTimeMillis();
			return;
		}
	}

	private void hideProgressIndicator(final int step) {
		runOnUiThread(new Runnable() {
			public void run() {
				if (step == 2) {
					progressDialog.dismiss();
				}
				try {
					final String appRootPath = android.os.Environment
							.getExternalStorageDirectory()
							.toString()
							.concat(String.format("/%s/%s", "Android/data",
									getPackageName()));
					String updateInfo_path = appRootPath
							.concat("/update_t.xml");
					if (step == 1) {
						// GET SAVE VERSION
						final String PREFS_NAME = "UpdatePicFile";
						SharedPreferences settings = getSharedPreferences(
								PREFS_NAME, 0);

						saveVersion = settings.getInt("saveVersion", 0);

						// ----- READ FILE update_t.xml to find info
						InputStream is = new FileInputStream(updateInfo_path);
						Document doc = XmlUtils.convertToDocument(is);
						NodeList nodeList = doc.getElementsByTagName("picture");

						infoList.clear();
						totalSize = 0;
						for (int i = 0; i < nodeList.getLength(); i++) {
							Node node = nodeList.item(i);
							Element e = (Element) node;
							NodeList nlTemp = e.getElementsByTagName("id");
							Node nTemp = nlTemp.item(0);
							String strID = nTemp.getTextContent();
							int intVersion = Integer.parseInt(strID);

							nlTemp = e.getElementsByTagName("link");
							nTemp = nlTemp.item(0);
							String strLink = nTemp.getTextContent();

							nlTemp = e.getElementsByTagName("size");
							nTemp = nlTemp.item(0);
							String strSize = nTemp.getTextContent();

							if (intVersion > highestVersion) {
								highestVersion = intVersion;
							}

							if (intVersion > saveVersion) {
								infoList.add(strID + "-" + strLink);
								totalSize += Integer.valueOf(strSize);
							}
						}
						
						if(MyApplication.flagProcessCheckAll){
							checkAllFromServer_total += totalSize;
						}
						
						if (highestVersion <= saveVersion) { // UPTODATE
							
							isDownloadingPic = false;
							MyApplication.freezeTime = System.currentTimeMillis();
							
							CheckUpdateFirmwareFromServer(context);
						} else { // UPDATE
							if(MyApplication.flagProcessCheckAll){
								CheckUpdateFirmwareFromServer(context);
								
							} else {
								if(!boolShowKaraoke){
									myDialogUpdatePic.dismiss();
									downloadPic();	
								}		
								
							}	
							
						}
					} else if (step == 2) {						
						if(!isFinishing()){
							allowCancelOutside = false;
							popupViewUpadtePic
									.setPopupLayout(TouchPopupViewUpdatePic.LAYOUT_PROCESSDATA);
							
							myDialogUpdatePic.getWindow()
									.setGravity(Gravity.CENTER);
							WindowManager.LayoutParams params = myDialogUpdatePic
									.getWindow().getAttributes();
							myDialogUpdatePic.getWindow().setAttributes(params);
							myDialogUpdatePic.show();
						}
						final Handler messageHandler = new Handler() {
							public void handleMessage(Message msg) {
								super.handleMessage(msg);
								try {
									isDownloadingPic = false;
									MyApplication.freezeTime = System.currentTimeMillis();
									allowCancelOutside = true;
									final String PREFS_NAME = "UpdatePicFile";
									SharedPreferences settings = getSharedPreferences(
											PREFS_NAME, 0);
									settings.edit()
											.putInt("saveVersion",
													highestVersion).commit();
									if (myDialogUpdatePic.isShowing()) {
										myDialogUpdatePic.dismiss();
									}
									clearDownloadPicResource();

									CheckUpdateFirmwareFromServer(context);
								} catch (Exception e) {
								}
							}
						};

						final String picDir = appRootPath.concat("/PICTURE");
						File picDirFile = new File(picDir);
						if (!picDirFile.exists()) {
							picDirFile.mkdir();
						}

						new Thread(new Runnable() {
							@Override
							public void run() {
								final SmartKaraoke importDB = new SmartKaraoke();
								for (int i = 0; i < infoList.size(); i++) {
									final String txtFileName = infoList.get(i)
											.split("-")[1];
									importDB.extractdata(appRootPath + "/"
											+ txtFileName, picDir);
								}
								messageHandler.sendEmptyMessage(0);
							}
						}).start();
					}
				} catch (Exception e) {
					isDownloadingPic = false;
					MyApplication.freezeTime = System.currentTimeMillis();
					final String PREFS_NAME = "UpdatePicFile";
					SharedPreferences settings = getSharedPreferences(
							PREFS_NAME, 0);
					settings.edit().putInt("saveVersion", saveVersion).commit();
					clearDownloadPicResource();
				}
			}
		});
	}

	private void OnDownPicINFOFromDauMay() {
		NetworkSocket socket = ((MyApplication) getApplication()).getSocket();
		if (socket != null) {
			String rootPath = "";

			if (android.os.Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED)) {
				rootPath = android.os.Environment.getExternalStorageDirectory()
						.toString();
				rootPath = rootPath.concat(String.format("/%s/%s",
						"Android/data", getPackageName()));
				File appBundle = new File(rootPath);
				if (!appBundle.exists())
					appBundle.mkdir();
			}

			String fileInfoPath = rootPath.concat("/update_t.xml");
			((MyApplication) getApplication()).downloadArtistFileInfo(
					fileInfoPath, true);
			DownloadState++;
		}
	}

	private int countDownPicDauMay = 0;

	private void OnDownPicFILEFromDauMay(String txtFileName) {
		NetworkSocket socket = ((MyApplication) getApplication()).getSocket();
		if (socket != null) {
			String rootPath = "";

			if (android.os.Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED)) {
				rootPath = android.os.Environment.getExternalStorageDirectory()
						.toString();
				rootPath = rootPath.concat(String.format("/%s/%s",
						"Android/data", getPackageName()));
				File appBundle = new File(rootPath);
				if (!appBundle.exists())
					appBundle.mkdir();
			}

			String savePath = rootPath.concat("/" + txtFileName);
			((MyApplication) getApplication()).downloadArtistFile(savePath,
					txtFileName);
			DownloadState++;
			countDownPicDauMay++;
		}
	}

	private void saveUpdatePicSuccess() {
		final String PREFS_NAME = "UpdatePicFile";
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		settings.edit().putInt("saveVersion", highestVersion).commit();
	}

	private void clearDownloadPicResource() {
		try {
			String appRootPath = android.os.Environment
					.getExternalStorageDirectory()
					.toString()
					.concat(String.format("/%s/%s", "Android/data",
							getPackageName()));
			String xmlPath = appRootPath.concat("/update_t.xml");
			File f = new File(xmlPath);
			if (f.exists()) {
				f.delete();
			}

			for (int i = 0; i < infoList.size(); i++) {
				String txtFileName = infoList.get(i).split("-")[1];
				String filePath = appRootPath.concat("/" + txtFileName);

				File file = new File(filePath);
				if (file.exists()) {
					file.delete();
				}
			}
		} catch (Exception e) {
			isDownloadingPic = false;
			MyApplication.freezeTime = System.currentTimeMillis();
		}
	}

	// /////////FIRST TIME///////////////
	final String PREFS_FIRST = "MyFirstFile";
	final String PREFS_FAV = "MyFavFile";

	private boolean loadFirstTime() {
		final SharedPreferences settings = getSharedPreferences(PREFS_FIRST, 0);

		String appVersion = "";
		try {
			PackageInfo pInfo = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			appVersion = pInfo.versionName;
		} catch (Exception e) {
			return false;
		}

		boolean flag = false;
		String saveVersion = settings.getString("last_app_version", "0.0.0");

		MyLog.d("PROCESS FIRST TIME", "app version: " + appVersion + "  ...............");
		MyLog.d("PROCESS FIRST TIME", "save version: " + saveVersion  + "  ...............");

		if (saveVersion.equals("0.0.0")) {
			flag = true;
		} else {
			// CHECK Version
			String[] appData = appVersion.split("\\.");
			String[] saveData = saveVersion.split("\\.");

			int intAppVersion = Integer.valueOf(appData[0]) * 100
					+ Integer.valueOf(appData[1]) * 10
					+ Integer.valueOf(appData[2]);
			int intSaveVersion = 0;
			try {
				intSaveVersion = Integer.valueOf(saveData[0]) * 100
						+ Integer.valueOf(saveData[1]) * 10
						+ Integer.valueOf(saveData[2]);
			} catch (Exception e) {
			}
			if (intAppVersion > intSaveVersion)
				flag = true;
		}
		MyLog.d("PROCESS FIRST TIME", "flag: " + flag);

		if (flag) {
			try {
				settings.edit().putString("last_app_version", appVersion).commit();
				
				String rootPath = "";

				if (android.os.Environment.getExternalStorageState().equals(
						android.os.Environment.MEDIA_MOUNTED)) {
					rootPath = android.os.Environment
							.getExternalStorageDirectory()
							.toString()
							.concat(String.format("/%s/%s", "Android/data",
									getPackageName()));
					File appBundle = new File(rootPath);
					if (!appBundle.exists())
						appBundle.mkdir();
				}

				String dbPath = getDatabasePath(DbHelper.DBName)
						.getAbsolutePath();

				final String picDir = rootPath.concat("/PICTURE");
				File picDirFile = new File(picDir);
				if (!picDirFile.exists()) {
					picDirFile.mkdir();
				}
				
				final String remoteDir = rootPath.concat("/REMOTE");
				File remoteDirFile = new File(remoteDir);
				if (!remoteDirFile.exists()) {
					remoteDirFile.mkdir();
				}
				
				final String dbDir = rootPath.concat("/DB");
				File dbDirFile = new File(dbDir);
				if (!dbDirFile.exists()) {
					dbDirFile.mkdir();
				}
				
				final String romDir = rootPath.concat("/ROM");
				File romDirFile = new File(romDir);
				if (!romDirFile.exists()) {
					romDirFile.mkdir();
				}
				
				final String lyricDir = rootPath.concat("/LYRIC");
				File lyricDirFile = new File(lyricDir);
				if (!lyricDirFile.exists()) {
					lyricDirFile.mkdir();
				}
				
				final String lyricDir1 = rootPath.concat("/LYRIC/SONCA");
				File lyricDirFile1 = new File(lyricDir1);
				if (!lyricDirFile1.exists()) {
					lyricDirFile1.mkdir();
				}
				
				final String lyricDir2 = rootPath.concat("/LYRIC/USER");
				File lyricDirFile2 = new File(lyricDir2);
				if (!lyricDirFile2.exists()) {
					lyricDirFile2.mkdir();
				}
				
				final String colorLyricDir = rootPath.concat("/COLORLYRIC");
				File colorlyricDirFile2 = new File(colorLyricDir);
				if (!colorlyricDirFile2.exists()) {
					colorlyricDirFile2.mkdir();
				}
				
				final String flipperDir = rootPath.concat("/FLIPPER");
				File flipperDirFile = new File(flipperDir);
				if (!flipperDirFile.exists()) {
					flipperDirFile.mkdir();
				}
				
				final String movieDir = rootPath.concat("/MOVIE");
				File movieDirFile = new File(movieDir);
				if (!movieDirFile.exists()) {
					movieDirFile.mkdir();
				}

				final String youtubeDir = rootPath.concat("/YOUTUBE");
				File youtubeDirFile = new File(youtubeDir);
				if (!youtubeDirFile.exists()) {
					youtubeDirFile.mkdir();
				}
				
				AssetManager assetMgr = this.getAssets();
				DBInterface.DBCloseDatabase(context);

				
				final String dbPathStar = getDatabasePath(DbHelper.DBNameStar).getAbsolutePath();
				File dbPathCopyFile = new File(dbPathStar);
				if (dbPathCopyFile.exists()) {
					dbPathCopyFile.delete();
				}		
				
				if (true) { // isNeedUpdateDauMay(9999, 0, 1100, 0)
					InputStream is = assetMgr.open("database.db");
					OutputStream os = new FileOutputStream(dbPath);
					copyFile(is, os);
					is.close();
					os.flush();
					os.close();

					AppSettings setting = AppSettings.getInstance(getApplicationContext());					
					setting.saveServerLastUpdate(MyApplication.intPackageSK9x_DISC, 1);
					setting.saveServerLastUpdate(0, 2);
					setting.saveServerLastUpdate(MyApplication.intPackageSK9x_HDD, 3);
					setting.saveServerLastUpdate(0, 4);
				}				

				AppSettings setting = AppSettings
						.getInstance(getApplicationContext());
				if(true){ // setting.loadHiwLastUpdate() < 1443851187
					InputStream is1 = assetMgr.open(DbHelper.DBNameStar);
					OutputStream os1 = new FileOutputStream(dbPathStar);
					copyFile(is1, os1);
					is1.close();
					os1.flush();
					os1.close();	
					
					setting.saveHiwLastUpdate(MyApplication.packageHIW_date);
					setting.saveHiwHDDVersion(MyApplication.intPackageHIW_DISC);
					setting.saveHiwDISCVersion(MyApplication.intPackageHIW_HDD);
				}
				
				DBInterface.DBReloadDatabase(this.getApplicationContext(), DbHelper.DBName, DBInstance.TOCTYPE_SK9000, 0, 0, 0); 

				InputStream is1 = assetMgr.open("ico_default.png");
				OutputStream os1 = new FileOutputStream(picDir + "/1");
				copyFile(is1, os1);
				is1.close();
				os1.flush();
				os1.close();
				
				is1 = assetMgr.open("ircodelist");
				os1 = new FileOutputStream(remoteDir + "/ircodelist");
				copyFile(is1, os1);
				is1.close();
				os1.flush();					
				os1.close();	
				
				is1 = assetMgr.open("CPRIGHT");
				os1 = new FileOutputStream(dbDir + "/CPRIGHT");
				copyFile(is1, os1);
				is1.close();
				os1.flush();
				os1.close();

				// -- ROM FILES
				is1 = assetMgr.open("firmupdate");
				os1 = new FileOutputStream(romDir + "/firmupdate");
				copyFile(is1, os1);
				is1.close();
				os1.flush();
				os1.close();
				
				is1 = assetMgr.open("km1_firmupdate");
				os1 = new FileOutputStream(romDir + "/km1_firmupdate");
				copyFile(is1, os1);
				is1.close();
				os1.flush();
				os1.close();
				
				is1 = assetMgr.open("km1_512_user1.bin");
				os1 = new FileOutputStream(romDir + "/km1_512_user1.bin");
				copyFile(is1, os1);
				is1.close();
				os1.flush();
				os1.close();
				
				is1 = assetMgr.open("km1_512_user2.bin");
				os1 = new FileOutputStream(romDir + "/km1_512_user2.bin");
				copyFile(is1, os1);
				is1.close();
				os1.flush();
				os1.close();
				
				is1 = assetMgr.open("km1_1024_user1.bin");
				os1 = new FileOutputStream(romDir + "/km1_1024_user1.bin");
				copyFile(is1, os1);
				is1.close();
				os1.flush();
				os1.close();
				
				is1 = assetMgr.open("km1_1024_user2.bin");
				os1 = new FileOutputStream(romDir + "/km1_1024_user2.bin");
				copyFile(is1, os1);
				is1.close();
				os1.flush();
				os1.close();
				
				is1 = assetMgr.open("hiw_512_user1.bin");
				os1 = new FileOutputStream(romDir + "/hiw_512_user1.bin");
				copyFile(is1, os1);
				is1.close();
				os1.flush();
				os1.close();
				
				is1 = assetMgr.open("hiw_512_user2.bin");
				os1 = new FileOutputStream(romDir + "/hiw_512_user2.bin");
				copyFile(is1, os1);
				is1.close();
				os1.flush();
				os1.close();
				
				is1 = assetMgr.open("hiw_1024_user1.bin");
				os1 = new FileOutputStream(romDir + "/hiw_1024_user1.bin");
				copyFile(is1, os1);
				is1.close();
				os1.flush();
				os1.close();
				
				is1 = assetMgr.open("hiw_1024_user2.bin");
				os1 = new FileOutputStream(romDir + "/hiw_1024_user2.bin");
				copyFile(is1, os1);
				is1.close();
				os1.flush();
				os1.close();	
				

				SaveFirmware saveFirmware = SaveFirmware.getInstance(context);
				saveFirmware.saveVersionFirmwareHiW(200);
				saveFirmware.saveRevisionFirmwareHiW(9688);
				saveFirmware.saveVersionFirmwareKM1(200);
				saveFirmware.saveRevisionFirmwareKM1(9688);
				
				// -- END ROM FILES
				
				for (int i = 0; i < 3; i++) {
					String nameImage = (i + 1) + ".png";
					is1 = assetMgr.open(nameImage);
					os1 = new FileOutputStream(flipperDir + "/" + nameImage);
					copyFile(is1, os1);
					is1.close();
					os1.flush();
					os1.close();
				}
				
				// Save default sonca db
				SharedPreferences settingDevice = getSharedPreferences(PREFS_DEVICE, 0);
				int lastDeviceType = settingDevice.getInt("saveType", -1);
				if(lastDeviceType == -1){
					settingDevice.edit().putInt("saveType", MyApplication.SONCA).commit();
				}
				
				processNewSongTable();
				
				processLoadFavList();
				
				AppSettings mSettings = AppSettings.getInstance(getApplicationContext());
				mSettings.saveYouTubeVersion(0);
				mSettings.saveYouTubeVersion_SK90xx(0);
				mSettings.saveUpdateTOCVersion(0);
				mSettings.saveListOfflineVersion(0);
				mSettings.saveListOfflineVersion_SK90xx(0);
				mSettings.saveLuckyDataVersion(0);
				mSettings.saveLuckyImageVersion(0);
				
				LanguageStore langStore = new LanguageStore(this);
				langStore.updateStore();

				SharedPreferences settingFlagConnect = getSharedPreferences(PREFS_FLAGHDD, 0);
				DeviceStote store = new DeviceStote(context);
				if(store.getListSaveDevice().size() > 0){
					if(!MyApplication.flagEverConnect){
						settingFlagConnect.edit().putBoolean("isEverConnect2", true)
								.commit();
						MyApplication.flagEverConnect = true;
					}
				}	
				
				return true;
			} catch (Exception e) {
				return true;
			}
		}

		return false;
	}

	// --------- LOAD FILE FAV LIST
	
	private void processLoadFavList() {		
		FavouriteStore favStore = FavouriteStore
				.getInstance(getApplicationContext());
		ArrayList<Song> favList = favStore.getFavListInStore();
		if (favList.size() == 0) {
			return;
		}
		for (Song song : favList) {
			DBInterface.DBSetFavouriteSong(song.getIndex5() + "", song.getTypeABC()
					+ "", true, context);
		}
	}

	private void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}
	
////////////////////////////////////////

	@Override
	public void OnHideDrawerLayout() {
		MyLog.e(TAB, "OnHideDrawerLayout()");
		ShowListDevice("");
	}

	@Override
	public void OnShowHideHello(boolean show) {
		MyLog.e(TAB, "OnShowHideHello() - " + show);
		((MyApplication) getApplication())
		.sendCommand(NetworkSocket.REMOTE_CMD_CAPTION,
				(show == true) ? 1 : 0);
	}

	@Override
	public void OnChangeHello(String dong1, String dong2) {
		MyLog.e(TAB, "OnChangeHello() - " + dong1 + " - " + dong2);
		if(dong1.getBytes().length >= 60 || dong2.getBytes().length >= 60){
			makeToastMessage(getResources().getString(R.string.hello_10));
		}else{
			if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
					|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
					 || MyApplication.intSvrModel == MyApplication.SONCA_TBT || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI){
				ConvertString converter = new ConvertString();
				if(converter.ConvertUnicodeToTCVN(dong1).length >= 20 || converter.ConvertUnicodeToTCVN(dong2).length >= 20){
					makeToastMessage(getResources().getString(R.string.hello_10));
					return;
				}				
			}
			
			((MyApplication)getApplication()).setUserCaption(dong1, dong2);
			
			if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
					|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
					|| MyApplication.intSvrModel == MyApplication.SONCA_KBX9 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
					|| MyApplication.intSvrModel == MyApplication.SONCA_TBT){
				makeToastMessage(getResources().getString(R.string.hello_7));
				return;
			}
			
			String data = ((MyApplication)getApplication()).getUserCaption();
			if(data != null){
				boolean success = false;
				String[] lines = data.split("\n");
				if(lines.length == 2){
					if(lines[0] == null || lines[1] == null){}else{
						if(lines[0].equals(dong1) && lines[1].equals(dong2)){
							success = true;
						}
					}
				} else {
					success = false;
				}
				if(success == true){
					makeToastMessage(getResources().getString(R.string.hello_7));
				}else{
					makeToastMessage(getResources().getString(R.string.hello_8));
				}
			}
		}
	}

	@Override
	public void OnExitApp() {
		MyLog.e(TAB, "OnExitApp()");
		
	}

//////////////////////- LYRIC - KHIEM - /////////////////////////////////
	
	// ------------------------------------ PROCESS TOC SERVER
	private Dialog myDialogUpdateToc;
	private TouchPopupViewUpdateToc popupViewUpdateToc;
	private boolean isDownloadingToc = false;
	private ArrayList<String> infoTocFileList = new ArrayList<String>();
	private int highestTocVersion = 0;
	private int saveTocVersion = 0;
	private int saveTocHDDVersion = 0;
	private long saveDateUpdate = 0;
	
	private void onUpdateTocServer(int step, final int tocType) {
		try {			

			MyLog.i(" ", " ");
			MyLog.i(TAB, "onUpdateTocServer: step = " + step + " -- tocType = " + tocType);
			MyLog.i(" ", " ");
			
			if (step == 1) {
				if (myDialogUpdateToc != null) {
					myDialogUpdateToc.dismiss();
				}
				
				myDialogUpdateToc = new Dialog(KTVMainActivity.this);
				myDialogUpdateToc.requestWindowFeature(Window.FEATURE_NO_TITLE);
				myDialogUpdateToc.getWindow().setBackgroundDrawable(
						new ColorDrawable(android.graphics.Color.TRANSPARENT));
				myDialogUpdateToc
						.setContentView(R.layout.touch_item_popup_update_toc);
				myDialogUpdateToc.setCanceledOnTouchOutside(true);
				
				WindowManager.LayoutParams pa = myDialogUpdateToc.getWindow()
						.getAttributes();
				pa.height = WindowManager.LayoutParams.MATCH_PARENT;
				pa.width = WindowManager.LayoutParams.MATCH_PARENT;
				pa.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
						| WindowManager.LayoutParams.FLAG_FULLSCREEN;

				myDialogUpdateToc.getWindow().setAttributes(pa);

				popupViewUpdateToc = (TouchPopupViewUpdateToc) myDialogUpdateToc
						.findViewById(R.id.myPopupUpdateToc);
				popupViewUpdateToc
						.setOnPopupUpdateTocListener(new OnPopupUpdateTocListener() {

							@Override
							public void OnUpdateTocYes() {
								downloadToc(tocType);
								myDialogUpdateToc.dismiss();
							}

							@Override
							public void OnUpdateTocNo() {
								isDownloadingToc = false;
								MyApplication.freezeTime = System.currentTimeMillis();
								
								clearDownloadTocResource();
								myDialogUpdateToc.dismiss();
								if(tocType == 0){
									onUpdateTocServer(1, 1);
								} else {
									OnUpdatePicAlone(1);	
								}
							}
						});

				pingTime = System.currentTimeMillis();
				new InterruptPingServerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
				startPingTocServer(tocType);
			} else if (step == 2) {
				// ----- DOWNLOAD FILE update_p.xml
				String appRootPath = android.os.Environment
						.getExternalStorageDirectory()
						.toString()
						.concat(String.format("/%s/%s", "Android/data",
								getPackageName()));
				File appBundle = new File(appRootPath);
				if (!appBundle.exists())
					appBundle.mkdir();

				final String updateInfo_path = appRootPath
						.concat("/check_update.xml");
				File tempF = new File(updateInfo_path);
				if (tempF.exists()) {
					tempF.delete();
				}

				new Thread(new Runnable() {
					public void run() {
						isDownloadingToc = true;
						downloadTocFile(
								"https://kos.soncamedia.com/firmware/KarConnect/check_update.xml",
								updateInfo_path, tocType);
					}
				}).start();
			}
		} catch (Exception e) {
			isDownloadingToc = false;
			MyApplication.freezeTime = System.currentTimeMillis();
		}
	}

	class PingTocServerTask extends AsyncTask<String, Void, Void> {

		private int tocType;

		public PingTocServerTask(int tocType) {
			this.tocType = tocType;
		}

		protected Void doInBackground(String... urls) {
			try {
				if (!isTocURLReachable(KTVMainActivity.this)) {
					if (tocType == 99) {
						if (colorLyricListener != null && boolShowKaraoke && serverStatus!=null) {
							karaoke_ColorID = serverStatus.getPlayingSongID();
							if(MyApplication.intSvrModel == MyApplication.SONCA || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
								colorLyricListener.OnMain_NewSong(serverStatus.getPlayingSongID(), serverStatus.getMediaType(), serverStatus.getMidiShiftTime(), serverStatus.getPlayingSongTypeABC());
							} else {
								colorLyricListener.OnMain_NewSong(serverStatus.getPlayingSongID(), serverStatus.getMediaType(), serverStatus.getMidiShiftTime(), -1);
							}
						}
					}
					
					return null;
				}
				
				if(tocType == 99){ // USED FOR UPDATE VIDEO LYRIC
					flagInterrupt = false;
					onUpdateVideoLyric(2);
					return null;
				}
				
				flagInterrupt = false;
				onUpdateTocServer(2, tocType);
				return null;
			} catch (Exception e) {
				isDownloadingToc = false;
				MyApplication.freezeTime = System.currentTimeMillis();
				return null;
			}
		}

		protected void onPostExecute(Void feed) {
		}
	}

	private boolean isTocURLReachable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			try {
				URL url = new URL(
						"https://kos.soncamedia.com/firmware/KarConnect/check_update.xml");
				HttpURLConnection urlc = (HttpURLConnection) url
						.openConnection();
				urlc.setConnectTimeout(3 * 1000); // 3 s.
				urlc.connect();
				if (urlc.getResponseCode() == 200) { // 200 = "OK" code (http
														// connection is fine).
					// Log.wtf("Connection", "Success !");
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

	public void downloadTocFile(String url, String dest_file_path, int tocType) {
		try {
			File dest_file = new File(dest_file_path);
			URL u = new URL(url);
			DataInputStream fis = new DataInputStream(u.openStream());
			DataOutputStream fos = new DataOutputStream(new FileOutputStream(
					dest_file));

			int bytes_read = 0;
			int buffer_size = 1024 * 1024;
			byte[] buffer = new byte[buffer_size];

			while ((bytes_read = fis.read(buffer, 0, buffer_size)) > 0) {
				if (!isDownloadingToc) {
					progressDialog.dismiss();
					return;
				}
				fos.write(buffer, 0, bytes_read);
			}

			fis.close();
			fos.flush();
			fos.close();

			hideProgressTocIndicator(1, tocType);
		} catch (Exception e) {
			isDownloadingToc = false;
			MyApplication.freezeTime = System.currentTimeMillis();
			clearDownloadTocResource();
			return;
		}
	}

	private void clearDownloadTocResource() {
		try {
			String appRootPath = android.os.Environment
					.getExternalStorageDirectory()
					.toString()
					.concat(String.format("/%s/%s", "Android/data",
							getPackageName()));
			String xmlPath = appRootPath.concat("/check_update.xml");
			File f = new File(xmlPath);
			if (f.exists()) {
				f.delete();
			}

			String updatePath = appRootPath.concat("/MEGIDX");
			String lyricPath = appRootPath.concat("/LYRICS");

			final File updateFile = new File(updatePath);
			final File lyricFile = new File(lyricPath);

			if (updateFile.exists()) {
				updateFile.delete();
			}

			if (lyricFile.exists()) {
				lyricFile.delete();
			}
			
			File dbFile = new File(appRootPath.concat("/database.db"));
			if (dbFile.exists()) {
				dbFile.delete();
			}
			
			dbFile = new File(appRootPath.concat("/database_star.db"));
			if (dbFile.exists()) {
				dbFile.delete();
			}

			for (int i = 0; i < infoTocFileList.size(); i++) {
				String txtFileName = infoTocFileList.get(i);
				String filePath = appRootPath.concat("/" + txtFileName);

				File file = new File(filePath);
				if (file.exists()) {
					file.delete();
				}
			}
		} catch (Exception e) {
			isDownloadingToc = false;
			MyApplication.freezeTime = System.currentTimeMillis();
		}
	}

	private void hideProgressTocIndicator(final int step, final int tocType) {
		runOnUiThread(new Runnable() {
			public void run() {
				if (step == 2) {
					progressDialog.dismiss();
				}

				try {
					final String appRootPath = android.os.Environment
							.getExternalStorageDirectory()
							.toString()
							.concat(String.format("/%s/%s", "Android/data",
									getPackageName()));
					String updateInfo_path = appRootPath
							.concat("/check_update.xml");

					if (step == 1) {
						boolean flagUpdate = false;
						
						if(tocType == 0){
							// ----- READ FILE check_update.xml to find info
							InputStream is = new FileInputStream(updateInfo_path);
							Document doc = XmlUtils.convertToDocument(is);
							NodeList nodeList = doc.getElementsByTagName("toc2");
							
							infoTocFileList.clear();
							totalSize = 0;
							
							Node node = nodeList.item(0);
							Element e = (Element) node;

							NodeList nlTemp = e.getElementsByTagName("vol");
							Node nTemp = nlTemp.item(0);
							String strVersion = nTemp.getTextContent();
							saveTocVersion = Integer.parseInt(strVersion);
							
							nlTemp = e.getElementsByTagName("vol_hdd");
							nTemp = nlTemp.item(0);
							strVersion = nTemp.getTextContent();
							saveTocHDDVersion = Integer.parseInt(strVersion);

							nlTemp = e.getElementsByTagName("link_and");
							nTemp = nlTemp.item(0);
							String strLink = nTemp.getTextContent();
														
							String strSize = nTemp.getAttributes().getNamedItem("size").getNodeValue();

							if(isNeedUpdateDauMay(saveTocVersion, 0, saveTocHDDVersion, 0)){
								flagUpdate = true;
								
								infoTocFileList.add(strLink);
								totalSize += Integer.valueOf(strSize);
							}
						} else {
							// ----- READ FILE check_update.xml to find info
							InputStream is = new FileInputStream(updateInfo_path);
							Document doc = XmlUtils.convertToDocument(is);
							NodeList nodeList = doc.getElementsByTagName("toc_hiw");
							
							infoTocFileList.clear();
							totalSize = 0;
							
							Node node = nodeList.item(0);
							Element e = (Element) node;

							NodeList nlTemp = e.getElementsByTagName("date");
							Node nTemp = nlTemp.item(0);
							String strDate = nTemp.getTextContent();
							saveDateUpdate = Long.parseLong(strDate);

							nlTemp = e.getElementsByTagName("vol");
							nTemp = nlTemp.item(0);
							String strVersion = nTemp.getTextContent();
							saveTocVersion = Integer.parseInt(strVersion);
							
							nlTemp = e.getElementsByTagName("vol_hdd");
							nTemp = nlTemp.item(0);
							strVersion = nTemp.getTextContent();
							saveTocHDDVersion = Integer.parseInt(strVersion);
							
							nlTemp = e.getElementsByTagName("link_and");
							nTemp = nlTemp.item(0);
							String strLink = nTemp.getTextContent();
							
							String strSize = nTemp.getAttributes().getNamedItem("size").getNodeValue();

							AppSettings setting = AppSettings
									.getInstance(getApplicationContext());
							
							if(setting.loadHiwLastUpdate() < saveDateUpdate){
								flagUpdate = true;
								
								infoTocFileList.add(strLink);
								totalSize += Integer.valueOf(strSize);
							}
						}						

						if(MyApplication.flagProcessCheckAll){
							checkAllFromServer_total += totalSize;	
						}						

						if (!flagUpdate) { // UPTODATE
							isDownloadingToc = false;
							MyApplication.freezeTime = System.currentTimeMillis();
							clearDownloadTocResource();	
							if(tocType == 0){
								onUpdateTocServer(1, 1);
							} else {
								OnUpdatePicAlone(1);
							}
							
						} else { // UPDATE
							if(MyApplication.flagProcessCheckAll){
								if(tocType == 0){
									onUpdateTocServer(1, 1);
								} else {
									OnUpdatePicAlone(1);
								}
								
							} else {								
								downloadToc(tocType);
								myDialogUpdateToc.dismiss();
								
							}
						}
					} else if (step == 2) {
						// MyLog.e("TEST","........STEP 2............");

						if(serverStatus != null){
							((MyApplication)getApplication()).cancelSyncServerStatus();
						}
						
						popupViewUpdateToc
								.setPopupLayout(TouchPopupViewUpdateToc.LAYOUT_PROCESSDATA);
						myDialogUpdateToc.setCanceledOnTouchOutside(false);
						myDialogUpdateToc.setCancelable(false);
						myDialogUpdateToc.getWindow()
								.setGravity(Gravity.CENTER);
						WindowManager.LayoutParams params = myDialogUpdateToc
								.getWindow().getAttributes();
						myDialogUpdateToc.getWindow().setAttributes(params);
						myDialogUpdateToc.show();
						
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								try {
									// FAKE SHOWSONGFRAGMENT
//									FragmentTransaction fragmentTransaction = fragmentManager
//											.beginTransaction();
//									if (fragmentBase != null && SAVETYPE == SONG) {
//										((TouchFragmentSong)fragmentBase).forceCloseDB();
//									}
//									fragmentTransaction.commit();
									
									// EXTRACT ZIP
									// MyLog.e("TEST","........EXTRACT ZIP............");
									String zipName = infoTocFileList.get(0);
									unpackZip(appRootPath + "/", zipName);
									
									final Handler messageHandler = new Handler() {
										public void handleMessage(Message msg) {
											super.handleMessage(msg);
											isDownloadingToc = false;
											MyApplication.freezeTime = System.currentTimeMillis();
											if (msg.what == 0) {
												processLoadFavList();
												clearDownloadTocResource();

												if(myDialogUpdateToc != null){
													myDialogUpdateToc.dismiss();
												}

												if(tocType == 0){
													onUpdateTocServer(1, 1);
												} else {
													OnUpdatePicAlone(1);
												}
												
												showFragSwitch();
												
												if(serverStatus != null){
													((MyApplication)getApplication()).startSyncServerStatusThread();
												}
											} else {
												clearDownloadTocResource();
												
												showFragSwitch();
												
												if(serverStatus != null){
													((MyApplication)getApplication()).startSyncServerStatusThread();
												}
											}
										}
									};

									if(tocType == 0){
										String dbPath = getDatabasePath(DbHelper.DBName).getAbsolutePath();
										
										InputStream is = new FileInputStream(
												appRootPath.concat("/database.db"));
										OutputStream os = new FileOutputStream(dbPath);
										copyFile(is, os);
										is.close();
										os.flush();
										os.close();
										
										AppSettings setting = AppSettings
												.getInstance(getApplicationContext());
										setting.saveServerLastUpdate(saveTocVersion, 1);
										setting.saveServerLastUpdate(saveTocHDDVersion, 3); 
										setting.saveServerLastUpdate(0, 2);
										setting.saveServerLastUpdate(0, 4);
										
										processNewSongTable();

									} else {
										String dbPathStar = getDatabasePath(DbHelper.DBNameStar).getAbsolutePath();
										
										InputStream is = new FileInputStream(
												appRootPath.concat("/database_star.db"));
										OutputStream os = new FileOutputStream(dbPathStar);
										copyFile(is, os);
										is.close();
										os.flush();
										os.close();
										
										AppSettings setting = AppSettings
												.getInstance(getApplicationContext());
										setting.saveHiwLastUpdate(saveDateUpdate);
										setting.saveHiwHDDVersion(saveTocHDDVersion);
										setting.saveHiwDISCVersion(saveTocVersion);
									}							

									new Handler().postDelayed(new Runnable() {
										@Override
										public void run() {
											messageHandler.sendEmptyMessage(0);
										}
									}, 1000);
								} catch (Exception e) {
									isDownloadingToc = false;
									MyApplication.freezeTime = System.currentTimeMillis();
									clearDownloadTocResource();
								}
								
							}
						}, 500);
						
					}
				} catch (Exception e) {
					isDownloadingToc = false;
					MyApplication.freezeTime = System.currentTimeMillis();
					clearDownloadTocResource();
				}
			}
		});
	}

	private void downloadToc(final int tocType) {
		try {
			final String appRootPath = android.os.Environment
					.getExternalStorageDirectory()
					.toString()
					.concat(String.format("/%s/%s", "Android/data",
							getPackageName()));
			File appBundle = new File(appRootPath);
			if (!appBundle.exists())
				appBundle.mkdir();

			progressDialog = new Dialog(KTVMainActivity.this);
			progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			progressDialog.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));
			progressDialog.setContentView(R.layout.touch_item_popup_down_percent);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);

			WindowManager.LayoutParams pa = progressDialog.getWindow()
					.getAttributes();
			pa.height = WindowManager.LayoutParams.MATCH_PARENT;
			pa.width = WindowManager.LayoutParams.MATCH_PARENT;
			pa.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
					| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
					| WindowManager.LayoutParams.FLAG_FULLSCREEN;

			progressDialog.getWindow().setAttributes(pa);
			
			popupPercent = (TouchPopupViewDownloadPercent) progressDialog
					.findViewById(R.id.myPopupPercent);
			popupPercent
					.setOnPopupDownloadPercentListener(new OnPopupDownloadPercentListener() {

						@Override
						public void OnCancelDownload() {
							isDownloadingToc = false;
							clearDownloadTocResource();
							progressDialog.dismiss();
							
							onUpdateVideoLyric(1);						
						}
					});

			progressDialog.getWindow().setGravity(Gravity.CENTER);
			WindowManager.LayoutParams params = progressDialog.getWindow()
					.getAttributes();
			progressDialog.getWindow().setAttributes(params);
			progressDialog.show();

			new Thread(new Runnable() {
				public void run() {
					isDownloadingToc = true;
					downloadMultipleTocFile(tocType);
				}
			}).start();
		} catch (Exception e) {
			isDownloadingToc = false;
			MyApplication.freezeTime = System.currentTimeMillis();
			progressDialog.dismiss();
		}
	}

	public void downloadMultipleTocFile(int tocType) {
		try {
			String appRootPath = android.os.Environment
					.getExternalStorageDirectory()
					.toString()
					.concat(String.format("/%s/%s", "Android/data",
							getPackageName()));
			for (int i = 0; i < infoTocFileList.size(); i++) {
				String txtFileName = infoTocFileList.get(i);
				String url = "https://kos.soncamedia.com/firmware/karconnect/"
						+ txtFileName;
				String dest_file_path = appRootPath.concat("/" + txtFileName);

				File dest_file = new File(dest_file_path);
				URL u = new URL(url);
				URLConnection conn = u.openConnection();
				final int contentLength = conn.getContentLength();
				DataInputStream fis = new DataInputStream(u.openStream());
				DataOutputStream fos = new DataOutputStream(
						new FileOutputStream(dest_file));

				int bytes_read = 0;
				int bytesReadTotal = 0;
				int buffer_size = 1024 * 1024;
				byte[] buffer = new byte[buffer_size];

				while ((bytes_read = fis.read(buffer, 0, buffer_size)) > 0) {
					if (!isDownloadingToc) {
						progressDialog.dismiss();
						fis.close();
						fos.flush();
						fos.close();
						return;
					}
					fos.write(buffer, 0, bytes_read);
					bytesReadTotal += bytes_read;
					final int progress = (int) (100.0f * bytesReadTotal / contentLength);

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (progress == 0) {
								popupPercent.setTotalSize(contentLength);
							}
							popupPercent.setDownloadPercent(progress);
						}
					});
				}

				fis.close();
				fos.flush();
				fos.close();
			}
			hideProgressTocIndicator(2, tocType);
		} catch (Exception e) {
			isDownloadingToc = false;
			MyApplication.freezeTime = System.currentTimeMillis();
			clearDownloadTocResource();
			return;
		}
	}

	private boolean unpackZip(String path, String zipname) {
		InputStream is;
		ZipInputStream zis;
		try {
			String filename;
			is = new FileInputStream(path + zipname);
			zis = new ZipInputStream(new BufferedInputStream(is));
			ZipEntry ze;
			byte[] buffer = new byte[1024];
			int count;

			while ((ze = zis.getNextEntry()) != null) {
				filename = ze.getName();
				if (ze.getName().equals("FIRST10W")) {
					filename = "LYRICS";
				}

				if (ze.isDirectory()) {
					File fmd = new File(path + filename);
					fmd.mkdirs();
					continue;
				}

				FileOutputStream fout = new FileOutputStream(path + filename);

				while ((count = zis.read(buffer)) != -1) {
					fout.write(buffer, 0, count);
				}

				fout.close();
				zis.closeEntry();
			}

			zis.close();
		} catch (IOException e) {
			isDownloadingToc = false;
			MyApplication.freezeTime = System.currentTimeMillis();
			e.printStackTrace();
			return false;
		}

		return true;
	}

	// ----------------------- TIMER PING
	private Timer timerPing;
	private boolean allowTimerPing = true;
	class PingTask extends TimerTask {

		@Override
		public void run() {
//			MyLog.e(" "," ");
//			MyLog.e("Timer 10s", "......................");

			if (!allowTimerPing) {
				groupView_StopTimerAutoConnect();
				
				return;
			}
			
			if(flagFinishReloadYoutube == false){
				return;
			}
			
			if (serverStatus == null) {
				
				boolean hasLast = getLastConnectedServer();
				if (!hasLast) {
					groupView_StopTimerAutoConnect();
					
					return;
				}
				
				if(drawerLayout.isDrawerOpen(layoutConnect)){					
					return;
				}	
				
				groupView_StartTimerAutoConnect();
				
				AppSettings setting = AppSettings.getInstance(getApplicationContext());
				String connectedIP = setting.loadServerIP();
				boolean flagPing = executeCommandPing(connectedIP);
				MyLog.e("flagPing flagPing flagPing", flagPing + "");
				if (flagPing) {
					if (serverStatus == null) {
						try {
							SKServer skServer = new SKServer();
							skServer.setConnectedIPStr(connectedIP);
							skServer.setName(setting.loadLastServerName());
							skServer.setConnectPass(setting.loadServerPass());
							skServer.setModelDevice(setting.loadModeModel());
							skServer.setIrcRemote(setting.loadIrcRemote());
							skServer.setNameRemote(setting.loadNameRemote());
//							skServer.setState(SKServer.CONNECTED);
							((MyApplication)getApplication()).setDeviceCurrent(skServer);
								//--------------//
							((MyApplication) getApplication()).disconnectFromRemoteHost();
							((MyApplication) getApplication()).onStart();
							isShowConnectSuccess = false;
						} catch (Exception ex) {
						}
					}
				}
			} else {
				groupView_StopTimerAutoConnect();
			}
		}
	};

	private void startTimerPing() {
//		MyLog.e("startTimerPing", ".....................");
		stopTimerPing();
		timerPing = new Timer();
		timerPing.schedule(new PingTask(), 10000, 10000);
	}

	private void stopTimerPing() {
//		MyLog.e("stopTimerPing", ".....................");
		if (timerPing != null) {
			timerPing.cancel();
			timerPing = null;
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

	// --------------- START DOWNLOAD LYRIC SERVER

	private void createPercentDialog() {
		if (progressDialog != null) {
			progressDialog.cancel();
			progressDialog = null;
		}
		MyLog.e(TAB, "createPercentDialog()");
		progressDialog = new Dialog(KTVMainActivity.this);
		progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		progressDialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		progressDialog.setContentView(R.layout.touch_item_popup_down_percent);
		WindowManager.LayoutParams pa = progressDialog.getWindow()
				.getAttributes();
		pa.height = WindowManager.LayoutParams.MATCH_PARENT;
		pa.width = WindowManager.LayoutParams.MATCH_PARENT;
		pa.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_FULLSCREEN;
		popupPercent = (TouchPopupViewDownloadPercent) progressDialog
				.findViewById(R.id.myPopupPercent);
		popupPercent.setOnPopupDownloadPercentListener(
			new OnPopupDownloadPercentListener() {
				@Override
				public void OnCancelDownload() {
					isDownloadingLyric = false;
					MyApplication.freezeTime = System.currentTimeMillis();
					SetKeepScreenOn(false);
					if(loadFileServer_New != null){
						loadFileServer_New.cancel(true);
						loadFileServer_New = null;
					}
					progressDialog.dismiss();
				}
			});
		progressDialog.getWindow().setGravity(Gravity.CENTER);
		WindowManager.LayoutParams params = progressDialog.getWindow()
				.getAttributes();
		progressDialog.getWindow().setAttributes(params);
		progressDialog.show();
	}
	
	private boolean isDownloadingLyric = false;
	LoadLyRicFileServerNew loadFileServer_New = null;
	private void OnUpdateLyric2() {
		isDownloadingLyric = true;
		LoadCheckLyricNew loadCheckLyric = new LoadCheckLyricNew(getApplicationContext());
		loadCheckLyric.setOnLoadCheckLyricListener(new LoadCheckLyricNew.OnLoadCheckLyricListener() {
			@Override
			public void OnPostExecute(int result, int totalsize, final ArrayList<LyricXML> list) {
				if(popupViewUpadtePic != null){
					if(result == LoadCheckLyricNew.NOWIFI){
						startUpdateAllFromServer();
						
					}
					if(result == LoadCheckLyricNew.NODOWN){
						allowCancelOutside = true;
						isDownloadingLyric = false;
						MyApplication.freezeTime = System.currentTimeMillis();

						startUpdateAllFromServer();
					}
					if(result == LoadCheckLyricNew.SUCCESS){
						allowCancelOutside = false; 
						
						if(MyApplication.flagProcessCheckAll){
							checkAllFromServer_total += totalsize;
							startUpdateAllFromServer();
						} else {
							isDownloadingLyric = true;
							
							loadFileServer_New = new LoadLyRicFileServerNew(getApplicationContext(), list);
							
							myDialogUpdatePic.dismiss();
							loadFileServer_New.setOnLoadLyRicFileServerListener(new LoadLyRicFileServerNew.OnLoadLyRicFileServerListener() {
								
								@Override
								public void OnProgressUpdate(boolean isCreate, float total, int percent) {
									if(isCreate){
										SetKeepScreenOn(true);
										createPercentDialog();
									}else{
										if (progressDialog != null) {
											popupPercent.setTotalSize(total);
											popupPercent.setDownloadPercent(percent);
										}
									}
								}
								
								@Override
								public void OnPostExecute() {
									isDownloadingLyric = false;
									MyApplication.freezeTime = System.currentTimeMillis();
									SetKeepScreenOn(false);
									if(loadFileServer_New != null){
										loadFileServer_New.cancel(true);
										loadFileServer_New = null;
									}
									if (progressDialog != null) {
										progressDialog.dismiss();
										progressDialog.cancel();
										progressDialog = null;
									}
									
									checkAllFromServer_total = 0;
									startUpdateAllFromServer();
								}
							});
							loadFileServer_New.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);		
							
						}
						
					}
				}
			}
		});
		loadCheckLyric.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}
	
	// ---------- SWITCH DB
	final String PREFS_DEVICE = "LastDeviceType";
	private boolean isSwitchingDB = false;
	
	private void processSwitchDB(int deviceType) {
		
		try {			
			isSwitchingDB = true;
			stopTimerPing();
			
			deviceType = MyApplication.intSvrModel;
			
			// CHECK LAST CONNECTED DEVICE TYPE
			SharedPreferences settings = getSharedPreferences(PREFS_DEVICE, 0);
			int lastDeviceType = settings.getInt("saveType", -1);
			
			MyLog.e("processSwitchDB", "..........deviceType = " + deviceType);
			MyLog.e("processSwitchDB", "..........lastDeviceType = " + lastDeviceType);
			if (lastDeviceType == deviceType && lastDeviceType != MyApplication.SONCA_HIW && lastDeviceType != MyApplication.SONCA_KM2
					&& lastDeviceType != MyApplication.SONCA_KB_OEM && lastDeviceType != MyApplication.SONCA_KM1_WIFI
					&& lastDeviceType != MyApplication.SONCA_KBX9 && lastDeviceType != MyApplication.SONCA_KB39C_WIFI
					&& lastDeviceType != MyApplication.SONCA_TBT) {
				MyLog.e("processSwitchDB", "..........no switch");
				
				startTimerPing();
				isSwitchingDB = false;
				return;
			}
			
			// FAKE 
//			FragmentTransaction fragmentTransaction = fragmentManager
//					.beginTransaction();
//			if (fragmentBase != null && SAVETYPE == SONG) {
//				((TouchFragmentSong)fragmentBase).forceCloseDB();
//			}
//			fragmentTransaction.commit();
			
			MyLog.e("processSwitchDB", "..........do switch");
			settings.edit().putInt("saveType", deviceType).commit();
			
			// DO SWITCH DB
			if (deviceType == MyApplication.SONCA_HIW || deviceType == MyApplication.SONCA_KM2
					|| deviceType == MyApplication.SONCA_KB_OEM || deviceType == MyApplication.SONCA_KM1_WIFI
					|| deviceType == MyApplication.SONCA_TBT || deviceType == MyApplication.SONCA_KB39C_WIFI
					|| deviceType == MyApplication.SONCA_KBX9) {	
				MyLog.e("", "switch for HiW");
				MyLog.d("8203R TocType", "intTocType = " + MyApplication.intMTocType);
				MyLog.d("", "intTocHDDVol = " + intTocHDDVol);
				DBInterface.DBReloadDatabase(this.getApplicationContext(),
						DbHelper.DBNameStar, MyApplication.intMTocType, intTocHDDVol, intTocHDDType, intIndexType);
			} else if (deviceType == MyApplication.SONCA_KARTROL || deviceType == MyApplication.SONCA_KM1) {	
				MyLog.e("", "switch for Kartrol");
				DBInterface.DBReloadDatabase(this.getApplicationContext(),
						DbHelper.DBNameStar, DBInstance.TOCTYPE_P2F, 0, 0, 0);			
			} else {
				MyLog.e("", "switch for SK9xxx");
				DBInterface.DBReloadDatabase(this.getApplicationContext(),
						DbHelper.DBName, DBInstance.TOCTYPE_SK9000, 0, 0, 0);
			}
			
			if(MyApplication.flagEverKBX9 == false && MyApplication.intMTocType == 19 && MyApplication.intSvrModel == MyApplication.SONCA_KBX9){ // KBX9 Phil
				MyApplication.flagEverKBX9 = true;
				AppSettings setting = AppSettings.getInstance(getApplicationContext());
				setting.setEverConnectKBX9(true); 
				
				List<Language> languageList = DBInterface.DBSearchSongLanguage("", SearchMode.MODE_FULL, 0, 0, context);
				LanguageStore langStore = new LanguageStore(context);
				for (Language language : languageList) {
					if(language.getID() == 4 || language.getID() == 1){
						boolean fActive = langStore.getActiveLanguage(language);
						if(fActive == false){
							langStore.setLanguage(language);
						}	
					}
					
				}
			} else {
				List<Language> languageList = DBInterface.DBSearchSongLanguage("", SearchMode.MODE_FULL, 0, 0, context);
				LanguageStore langStore = new LanguageStore(context);
				if(languageList != null && languageList.size() > 0){
					boolean flagEmpty = true;
					
					for (Language language : languageList) {
						boolean fActive = langStore.getActiveLanguage(language);
						//MyLog.d("TEST LANG", "language = " + language.getName() + "-- " + fActive);
						if(fActive){
							flagEmpty = false;
							break;
						}				
					}
					
					//MyLog.d("TEST LANG", "flagEmpty = " + flagEmpty);
					
					if(flagEmpty){
						langStore.setLanguage(languageList.get(0));
					}	
				}
				
			}

			showFragSwitch();

			isSwitchingDB = false;
			
			Thread.sleep(500);
			
			startTimerPing();
		} catch (Exception e) {
			MyLog.e("processSwitchDB", "error = " + e.toString());			
			isSwitchingDB = false;
			startTimerPing();
		}
	}
	
	
	private TouchDialogConnect dialogConnect = null;
	private void showDialogConnect(){
		if(serverStatus != null){
			return;
		}
		if(dialogConnect == null){
			MyLog.e(TAB, "showDialogConnect() : IS NULL");
			dialogConnect = new TouchDialogConnect(getApplicationContext(), getWindow());
			dialogConnect.setOnDialogConnectListener(new OnDialogConnectListener() {
				@Override
				public void OnYesListener() {
					MyLog.e(TAB, "Main - showDialogConnect() - OnYesListener()");
					ShowListDevice("");
					flagAllowConnect = true;
					drawerLayout.openDrawer(layoutConnect);
					((MyApplication) getApplication()).setListServers(null);
					((MyApplication) getApplication()).searchNearbyDevice();
					if (listDeviceListener != null) {
						listDeviceListener.OnDisplayProgressScan(true);
					}					
				}
				
				@Override
				public void OnFinishListener() {
					MyLog.e(TAB, "Main - showDialogConnect() - OnFinishListener()");
					dialogConnect = null;
				}
			});
			dialogConnect.showToast();
		}else{
			MyLog.e(TAB, "showDialogConnect() : NOT NULL");
		}
	}
	
	private void hideshowDialogConnect(){
		if(dialogConnect != null){
			dialogConnect.hideToastBox();
		}
	}
	
	private void hideDialogAddSong(){
		if(dialogAddSongData != null){
			dialogAddSongData.dismissDialog();
			dialogAddSongData = null;
		}
	}
	
//	private Toast toastConnectSuccess;
	private boolean isShowConnectSuccess = true;
	private void showConnectSuccess() {
		if(isShowConnectSuccess == false){
			return;
		}
		
		if(dialogNoAddPlayList != null){
			dialogNoAddPlayList = null;
			return;
		}
		
		SKServer skServer = ((MyApplication) getApplication())
				.getDeviceCurrent();
		if(skServer != null){
			layoutDownload.setVisibility(View.GONE);
			layoutAnimation.setVisibility(View.VISIBLE);
			animationView.showSuccessConnect(skServer.getName());
		}
		
	}

	/*
	 * FIX PING URL WITH WIFI KARTROL --> TIMEOUT LONG
	 */
	private PingTocServerTask pingTocServerTask;
	private PingServerTask pingServerTask;
	private long pingTime = 0;
	private boolean flagInterrupt = true;

	class InterruptPingServerTask extends AsyncTask<String, Void, Void> {

		private boolean flagPingVideo = false;
		public void setFlagPingVideo(boolean flagPingVideo){
			this.flagPingVideo = flagPingVideo;
		}
		
		protected Void doInBackground(String... urls) {
			try {
				MyLog.e(" "," ");
				MyLog.e(" "," ");
				MyLog.e("",
						"InterruptPingServerTask...........start...............");
				MyLog.e(" "," ");
				MyLog.e(" "," ");

				do {
					long temp = System.currentTimeMillis();
					if (temp - pingTime > 5000) {
						if(flagPingVideo){
							if(colorLyricListener != null && boolShowKaraoke && serverStatus!=null){
								MyLog.d(" ", "InterruptPingKARAOKE");
								karaoke_ColorID = serverStatus.getPlayingSongID();
								if(MyApplication.intSvrModel == MyApplication.SONCA || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
									colorLyricListener.OnMain_NewSong(serverStatus.getPlayingSongID(), serverStatus.getMediaType(), serverStatus.getMidiShiftTime(), serverStatus.getPlayingSongTypeABC());
								} else {
									colorLyricListener.OnMain_NewSong(serverStatus.getPlayingSongID(), serverStatus.getMediaType(), serverStatus.getMidiShiftTime(), -1);
								}
							}
						}
						stopPingTocServer();
						stopPingServer();
						return null;
					}
				} while (flagInterrupt);
				return null;
			} catch (Exception e) {
				return null;
			}
		}

		protected void onPostExecute(Void feed) {
		}
	}
	
	private void startPingTocServer(int tocType){
		stopPingTocServer();
		pingTocServerTask = new PingTocServerTask(tocType);
		pingTocServerTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
	}
	
	private void stopPingTocServer(){
		if(pingTocServerTask != null){
			pingTocServerTask.cancel(true);
			pingTocServerTask = null;
		}
	}
	
	private void startPingServer(){
		stopPingServer();
		pingServerTask = new PingServerTask();
		pingServerTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
	}
	
	private void stopPingServer(){
		if(pingServerTask != null){
			pingServerTask.cancel(true);
			pingServerTask = null;
		}
	}
	
	

	// ---------- SUPPORT BYTE UTILS
	final char[] hexArray = "0123456789ABCDEF".toCharArray();
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
	
	// ---------- ON/OFF USER SONG
	public void setOnOffUserList(String adminPass, boolean flagOffUserList){
		MyLog.e("setOnOffUserList", "adminPass = " + adminPass + " -- flagOffUserList = " + flagOffUserList);
		
		if(MyApplication.bOffUserList == flagOffUserList){
			return;
		}
		
		try {
			((MyApplication) getApplication()).setOnOffUserList(adminPass, flagOffUserList);
		} catch (Exception e) {
			
		}
	}
	
	private void reloadSongList(){
		MyLog.e(TAB, "        ");
		MyLog.e(TAB, "reloadSongList");
		DeviceStote deviceStote = new DeviceStote(context);
		deviceStote.setShareRefresh(MyApplication.bOffUserList);
		showFragSwitch();		
	}

	@Override
	public void OnBackList(int flag) {
		flagAllowConnect = true;
		if(flag == TouchDeviceAdmin.LEFT_SLIDER){
			drawerLayout.closeDrawer(layoutConnect);
		} else {		
			ShowListDevice("");	
		}	
	}

	@Override
	public void OnSendAdmin(final String pass, final int flag) {
		MyLog.i(TAB, "OnSendAdmin : " + pass + " : " + flag);
		((MyApplication) getApplication()).getAdminPass();
		((MyApplication) getApplication()).setOnReceiverAdminPassListener(new OnReceiverAdminPassListener() {

			@Override
			public void OnReceiverAdminPass(String adminPass) {
				int intTemp = Integer.parseInt(pass);
				int intserver = -1;
				try {
					intserver = Integer.parseInt(adminPass);
				} catch (NumberFormatException ex) {
					intserver = -1;
				}
				String sendPass = String.format("%04d", intTemp) + "";
				if (sendPass.equals(adminPass) || intserver == intTemp) {
					if (flag == TouchDeviceAdmin.USER) {
						if(true){
							if(MyApplication.bOffUserList){
								Song s = new Song();
								s.setName(pass);
								s.setDownLink(getResources().getString(R.string.onoff_user_4c));
								showMyPopupAsking(6, s, getResources().getString(R.string.onoff_user_4a), getResources().getString(R.string.onoff_user_4b));
							} else {

								Song s = new Song();
								s.setName(pass);
								s.setDownLink(getResources().getString(R.string.onoff_user_3c));
								showMyPopupAsking(6, s, getResources().getString(R.string.onoff_user_3a), getResources().getString(R.string.onoff_user_3b));	
							}	
						} else {
							if (drawerLayout != null) {
								drawerLayout.closeDrawers();
							}
							
							MyApplication.mSongTypeList = null;
							processTotalSongType(100);						
							
							layoutDownload.setVisibility(View.GONE);
							layoutAnimation.setVisibility(View.VISIBLE);
							if (MyApplication.bOffUserList) {
								animationView.showAdminUser(TouchAnimationView.ON_USER);
							} else {
								animationView.showAdminUser(TouchAnimationView.OFF_USER);
							}

							new Handler().postDelayed(new Runnable() {
								@Override
								public void run() {
									setOnOffUserList(pass, !MyApplication.bOffUserList);
								}
							}, 1500);
						}
						
					} else if (flag == TouchDeviceAdmin.HELLO) {
						ShowHello();
					} else if (flag == TouchDeviceAdmin.GIAODIEN) {
						drawerLayout.closeDrawers();
						ShowDialogSettingApp();
					} else if (flag == TouchDeviceAdmin.LEFT_SLIDER) {
						ShowListDevice("");
						drawerLayout.openDrawer(layoutConnect);
						if (listDeviceListener != null && !isScanning) {
							MyLog.e(TAB, "OnShowConnect()");
							((MyApplication) getApplication()).setListServers(null);
							((MyApplication) getApplication()).searchNearbyDevice();
							listDeviceListener.OnDisplayProgressScan(true);
							isScanning = true;
						}
					} else if (flag == TouchDeviceAdmin.ADMIN_CONTROL) {
						if (serverStatus == null) {
							return;
						}
						if (commandBox == null) {
							if (drawerLayout.isDrawerOpen(layoutConnect)) {
								drawerLayout.closeDrawer(layoutConnect);
							}
							commandBox = new CommandBox(context, getWindow(), KTVMainActivity.this);
							commandBox.setOnDialogConnectListener(new OnCommandBoxListener() {

								@Override
								public void OnFinishListener() {
									MyLog.e(TAB, "bOffFirst : " + MyApplication.bOffFirst);
									if (drawerLayout.isDrawerOpen(layoutConnect)) {
										drawerLayout.closeDrawer(layoutConnect);
									}
									if (mainListener != null) {
										mainListener.OnUpdateView();
									}
									commandBox = null;
								}

								@Override
								public void OnCommandEnable(int id) {
									
									viewWifi.invalidate();
									viewQuaBai.invalidate();
									viewTachLoi.invalidate();
									viewGiamAm.invalidate();
									viewTangAm.invalidate();
									
									if(viewChinhMiDi != null){
										viewHatLai.invalidate();
										viewNgatTieng.invalidate();
										viewTamDung.invalidate();
										viewGiamTone.invalidate();
										viewTangTone.invalidate();
										viewDance.invalidate();
										viewChamDiem.invalidate();
										
										viewMelodyGiam.invalidate();
										viewMelodyTang.invalidate();
										viewTempoGiam.invalidate();
										viewTempoTang.invalidate();
										viewTone.invalidate();
										viewHoaAm.invalidate();
										viewMacDinh.invalidate();
									}
									
									if(serverStatus!=null){
										if(serverStatus.isOnOffControlFullAPI()){
											setOnOffControlFull();		
										} else {
											setOnOffAdminControl();	
										}
									}
								}

								@Override
								public void OnScorePercentListener(int percent) {
									MyLog.e(TAB, "OnScorePercentListener : " + percent);
									MyApplication.setCommandMediumScoreMethod(percent);
									setOnOffControlFull();
								}

								@Override
								public void OnSendScore(int isScoreOn) {
									if(MyApplication.intWifiRemote == MyApplication.SONCA){
										((MyApplication) getApplication()).sendCommand(
												NetworkSocket.REMOTE_CMD_SCORE, isScoreOn);
									}else{
										((MyApplication) getApplication()).sendCommandKartrol(
												(byte) RemoteIRCode.IRC_SCORE, 0);
									}
								}

								@Override
								public void OnSendVolumn(int value) {
									if(MyApplication.intWifiRemote == MyApplication.SONCA){
										if(MyApplication.intSvrModel == MyApplication.SONCA || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){											
												
											if(MyApplication.intSvrCode >= 24){
												if(MyApplication.intSvrCode < 25){
													((MyApplication) getApplication()).sendCommand(
															NetworkSocket.REMOTE_CMD_VOL_DEFAULT, value);	
												}												
												
												if(MyApplication.intSvrCode >= 25){
													if(serverStatus != null){
														int playingMode = serverStatus.getPlayingMode();	
														MyLog.e("TEST TEST TEST", "playingMode = " + playingMode);
														if(playingMode == 0){ // KARAOKE
															MyLog.e("TEST TEST TEST", "DEFAULT");
															((MyApplication) getApplication()).sendCommand(
																	NetworkSocket.REMOTE_CMD_VOL_DEFAULT, value);
														} else if(playingMode == 1){ // DANCE
															MyLog.e("TEST TEST TEST", "DANCE");
															((MyApplication) getApplication()).sendCommand(
																	NetworkSocket.REMOTE_CMD_VOL_DANCE, value);
														} else if(playingMode == 2){ // PIANO
															MyLog.e("TEST TEST TEST", "PIANO");
															((MyApplication) getApplication()).sendCommand(
																	NetworkSocket.REMOTE_CMD_VOL_PIANO, value);
														} else {
															
														}	
													}													
													
													new Handler().postDelayed(new Runnable() {
														@Override
														public void run() {
															((MyApplication) getApplication()).sendCommand(NetworkSocket.REMOTE_CMD_VOL_OFFSETMIDI, 0);
														}
													}, 500);
													
													new Handler().postDelayed(new Runnable() {
														@Override
														public void run() {
															((MyApplication) getApplication()).sendCommand(NetworkSocket.REMOTE_CMD_VOL_OFFSETKTV, 0);
														}
													}, 1000);	
												}			
											} else {
												((MyApplication) getApplication()).sendCommand(
														NetworkSocket.REMOTE_CMD_VOLUME, value);
											}
										} else {
											((MyApplication) getApplication()).sendCommand(
													NetworkSocket.REMOTE_CMD_VOLUME, value);
										}
										
									}else{
										if (value > 0) {
											((MyApplication) getApplication()).sendCommandKartrol(
													(byte) RemoteIRCode.IRC_VOLUME_UP, 0);
										} else if (value < 0) {
											((MyApplication) getApplication()).sendCommandKartrol(
													(byte) RemoteIRCode.IRC_VOLUME_DN, 0);
										}
									}
								}

								@Override
								public void OnSendMute(boolean isMute) {
									if(MyApplication.intWifiRemote == MyApplication.SONCA){
										((MyApplication) getApplication()).sendCommand(
												NetworkSocket.REMOTE_CMD_MUTE, isMute ? 0 : 1);
									}else{
										MyLog.e(TAB, "Mute : " + isMute);
										((MyApplication) getApplication()).sendCommandKartrol(
												(byte) RemoteIRCode.IRC_MUTE, 0);
									}
								}
								
								@Override
								public void OnVolumnMIDI(int value) {
									((MyApplication) getApplication()).sendCommand(NetworkSocket.REMOTE_CMD_VOL_OFFSETMIDI, value);
								}
								
								@Override
								public void OnVolumnKTV(int value) {
									((MyApplication) getApplication()).sendCommand(NetworkSocket.REMOTE_CMD_VOL_OFFSETKTV, value);
								}

								@Override
								public void OnVolumnMASTER(int value) {									
									((MyApplication) getApplication()).sendCommand(NetworkSocket.REMOTE_CMD_VOL_DEFAULT, value);
									
									new Handler().postDelayed(new Runnable() {
										@Override
										public void run() {
											((MyApplication) getApplication()).sendCommand(NetworkSocket.REMOTE_CMD_VOL_OFFSETMIDI, 0);
										}
									}, 500);
									
									new Handler().postDelayed(new Runnable() {
										@Override
										public void run() {
											((MyApplication) getApplication()).sendCommand(NetworkSocket.REMOTE_CMD_VOL_OFFSETKTV, 0);
										}
									}, 1000);
								}

								@Override
								public void OnVolumnDANCE(int value) {
									((MyApplication) getApplication()).sendCommand(NetworkSocket.REMOTE_CMD_VOL_DANCE, value);
								}

								@Override
								public void OnVolumnPIANO(int value) {
									((MyApplication) getApplication()).sendCommand(NetworkSocket.REMOTE_CMD_VOL_PIANO, value);
								}

								@Override
								public void OnMelodyDefault(int value) {
									((MyApplication) getApplication()).sendCommand(NetworkSocket.REMOTE_CMD_VOL_MELODY, value);
								}
							});
							commandBox.showToast();
						}
					} else if (flag == TouchDeviceAdmin.HIW_SETTING) {
						gotoHiW_Dialog();
					}
					
				} else {
					if (drawerLayout.isDrawerOpen(layoutConnect)) {
						drawerLayout.closeDrawer(layoutConnect);
					}
					layoutDownload.setVisibility(View.GONE);
					layoutAnimation.setVisibility(View.VISIBLE);
					animationView.showAdminUser(TouchAnimationView.ER_PASS_USER);
				}
				((MyApplication) getApplication()).setOnReceiverAdminPassListener(null);
			}
		});
	}

	@Override
	public void OnSelectModel(SKServer skServer) {
		if(skServer != null){
			MyLog.e(TAB, "OnSelectModel : " + skServer.getConnectedIPStr());
			String name = skServer.getName();
			String ip = skServer.getConnectedIPStr();
			ShowModel(name, ip);
		}	
	}
	
	private void ShowModel(String Name, String IP){
		MyLog.e(TAB, "ShowModel : start");
		if (isDestroyMainActivity == false) {
			return;
		}	
		if (SAVEDEVICE.equals(ONE_MODEL)) {
			return;
		}
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		if (fragmentDevice != null) {
			fragmentTransaction.remove(fragmentDevice);
		}
		fragmentDevice = new TouchModelFragment();
		Bundle bundle = new Bundle();
		bundle.putString("Name", Name);
		bundle.putString("IP", IP);
		fragmentDevice.setArguments(bundle);
		fragmentTransaction.replace(R.id.fragmentConnect, fragmentDevice,
				ONE_MODEL);
		fragmentTransaction.commit();
		SAVEDEVICE = ONE_MODEL;	
		MyLog.e(TAB, "ShowModel : end");
	}
	
	private void ShowSelectList(String Name, String IP){
		MyLog.e(TAB, "ShowSelectList : start");
		if (isDestroyMainActivity == false) {
			return;
		}	
		if (SAVEDEVICE.equals(SELECTLIST)) {
			return;
		}
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		if (fragmentDevice != null) {
			fragmentTransaction.remove(fragmentDevice);
		}
		fragmentDevice = new FragmentSelectList();
		Bundle bundle = new Bundle();
		bundle.putString("Name", Name);
		bundle.putString("IP", IP);
		fragmentDevice.setArguments(bundle);		
		fragmentTransaction.replace(R.id.fragmentConnect, fragmentDevice,
				SELECTLIST);
		fragmentTransaction.commit();
		SAVEDEVICE = SELECTLIST;	
		MyLog.e(TAB, "ShowSelectList : end");
	}


	@Override
	public void OnModelFragmentBackList() {
		ShowListDevice("");
	}

	@Override
	public void OnClickModel(Model model) {
//		groupView.setNameRemote(model.getName());

		// FAKE
//		FragmentTransaction fragmentTransaction = fragmentManager
//				.beginTransaction();
//		if (fragmentBase != null && SAVETYPE == SONG) {
//			((TouchFragmentSong) fragmentBase).forceCloseDB();
//		}
//		fragmentTransaction.commit();

		DBInterface.DBReloadDatabase(this.getApplicationContext(),
				DbHelper.DBNameStar, model.getToc(), 0, 0, 0);
				
		showFragSwitch();
	}
	
	@Override
	public void OnClickModel() {
		drawerLayout.closeDrawer(layoutConnect);
	}

	private CommandBox commandBox = null;
	@Override
	public void OnBlockCommand() {
		if(serverStatus ==  null){
			return;
		}
		
		if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
				|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
				|| MyApplication.intSvrModel == MyApplication.SONCA_KBX9 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
				 || MyApplication.intSvrModel == MyApplication.SONCA_TBT){
			makeToastMessage(getResources().getString(R.string.hello_11));
			return;
		}
		
		if (!MyApplication.flagSupportOffUser) {
			makeToastMessage(getResources().getString(R.string.hello_11));
			return;
		}	
		
		if(MyApplication.flagDeviceUser){
			makeToastMessage(getResources().getString(R.string.msg_warn_user));
			return;
		}
		
		SKServer skServer = ((MyApplication)getApplication()).getDeviceCurrent();
		if(skServer != null){
			String name = skServer.getName();
			String ip = skServer.getConnectedIPStr();
			ShowAdmin(name, ip, TouchDeviceAdmin.ADMIN_CONTROL);
		}	
	}
	
	private void hideBlockCommand(){
		if(commandBox != null){
			commandBox.hideToastBox();
		}			
	}
	
	// ---------- ON/OFF ADMIN CONTROL
	public void setOnOffAdminControl() {
		MyLog.e("setOnOffAdminControl", "START...............................");
		
		((MyApplication) getApplication()).getAdminPass();
		((MyApplication) getApplication()).setOnReceiverAdminPassListener(
				new OnReceiverAdminPassListener() {
			@Override
			public void OnReceiverAdminPass(String sendPass) {
				int sendValue = ~MyApplication.intCommandEnable;
				MyLog.e("", "controlData = " + Integer.toBinaryString(sendValue));
				String controlData = sendValue + "";
				try {
					((MyApplication) getApplication()).setOnOffAdminControl(sendPass, controlData);
					MyLog.e("setOnOffAdminControl", "END...............................");
				} catch (Exception e) {
					MyLog.e("setOnOffAdminControl", "ERROR...............................");
				}
			}
		});
	}
		  
	  private BroadcastReceiver mWifiInfoReceiver = new BroadcastReceiver(){
		    @Override
		    public void onReceive(Context arg0, Intent intent) {
		    	final WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		    	int numberOfLevels=5;
                WifiInfo wifiInfo = wifi.getConnectionInfo();
                int level=WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);

                if(MyApplication.levelWifi != level){
                	MyApplication.levelWifi = level;
                	if(viewDevice!=null){
                		viewDevice.invalidate();	
                	} 
                	
                	if(colorLyricListener != null && boolShowKaraoke){
						colorLyricListener.OnMain_UpdateWifi();
					}
                }
		    }
		  };
	
	@Override
	public void loadDatabaseWhenLaucherNoConnect() {

	}
	
	// ---------- ON/OFF ADMIN CONTROL
	public void setOnOffControlFull() {
		MyLog.e("setOnOffControlFull", "START...............................");

		// MyApplication.intCommandMedium = 4;

		((MyApplication) getApplication()).getAdminPass();
		((MyApplication) getApplication())
				.setOnReceiverAdminPassListener(new OnReceiverAdminPassListener() {
					@Override
					public void OnReceiverAdminPass(String sendPass) {
						int sendValue = MyApplication.intCommandMedium;
						MyLog.e("",
								"controlData in binary = "
										+ Integer.toBinaryString(sendValue));
						MyLog.e("", "controlData in integer = " + sendValue);
						String controlData = sendValue + "";
						try {
							((MyApplication) getApplication())
									.setOnOffControlFull(sendPass, controlData);
							MyLog.e("setOnOffControlFull",
									"END...............................");
						} catch (Exception e) {
							MyLog.e("setOnOffControlFull",
									"ERROR...............................");
						}
					}
				});
	}

	private void makeToastMessage(String msg) {
		try {
			Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0,
					getResources().getDisplayMetrics().heightPixels / 2);
			toast.show();	
		} catch (Exception e) {
			
		}		
	}
	
	private DialogDownTOC dialogBusyDownloadToc;
	private void ShowBusyDownloadTOC(final int state) {
		if (dialogBusyDownloadToc == null) {
			dialogBusyDownloadToc = new DialogDownTOC(context, getWindow(), this);
			dialogBusyDownloadToc.setOnDialogDownTOCListener(new OnDialogDownTOCListener() {

				@Override
				public void OnRetryControl() {
					dialogBusyDownloadToc = null;
					DefaultViewWhenDisConect();	
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							((MyApplication) getApplication()).getLastConnectedServer();
							SKServer lastServer = ((MyApplication) getApplication()).getDeviceCurrent();
							if(lastServer != null){
								((MyApplication) getApplication()).connectToRemoteHost(lastServer.getConnectedIPStr(), lastServer.getConnectPass());	
							}
						}
					}, 1000);							
				}

				@Override
				public void OnContinueControl() {
					dialogBusyDownloadToc = null;
					if(state == 0){
						DefaultViewWhenDisConect();
					} else {
						OnUpdatePicAlone(1);
					}	
				}

				@Override
				public void OnFinishListener() {
					dialogBusyDownloadToc = null;
				}
			});
			dialogBusyDownloadToc.showToast();
		}
	}

	@Override
	public void OnShowLanguage() {
		ShowLanguage();
	}
	
	public void ShowLanguage(){
		if (isDestroyMainActivity == false) {
			return;
		}	
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		if (fragmentDevice != null) {
			fragmentTransaction.remove(fragmentDevice);
		}
		fragmentDevice = new TouchFragmentLanguage();
		fragmentTransaction.replace(R.id.fragmentConnect, fragmentDevice,
				LANGUAGE);
		fragmentTransaction.commit();
		SAVEDEVICE = LANGUAGE;
		MyLog.e(TAB, "ShowLanguage");
	}

	@Override
	public void OnLanguageFragmentBackList() {
		if(boolLanguage){
			drawerLayout.closeDrawer(layoutConnect);
		} else {
			ShowListDevice("");	
		}
		
	}

	@Override
	public void OnClickLanguage() {
		gotoHomeMenu();
	}

	@Override
	public void OnCloseLanguage() {
		drawerLayout.closeDrawer(layoutConnect);
	}
	
	private DialogNoAddPlayList dialogNoAddPlayList = null;
	private void ShowDialogNoAddPlayList(String data){
		if(dialogNoAddPlayList == null){
			MyLog.e(TAB, "ShowDialogNoAddPlayList");
			dialogNoAddPlayList = new DialogNoAddPlayList(context, getWindow());
			dialogNoAddPlayList.setOnDialogNoAddPlayListListener(new OnDialogNoAddPlayListListener() {
				
				@Override
				public void OnYesListener() {
					dialogNoAddPlayList = null;
				}
				
				@Override
				public void OnFinishListener() {
					dialogNoAddPlayList = null;
				}
			});
			dialogNoAddPlayList.showToast(data);
		}
	}	
	
	private DialogHiW dialogHiW = null;
	@Override
	public void OnShowHi_W(final SKServer skServer) {
		MyLog.e(TAB, "OnShowHi_W");
		if(serverStatus ==  null){
			return;
		}

		((MyApplication)getApplication()).getAdminPass();
		((MyApplication)getApplication()).setOnReceiverAdminPassListener(
				new OnReceiverAdminPassListener() {
			
			@Override
			public void OnReceiverAdminPass(String adminPass) {
				if(adminPass == null){ 
					showDialogMessage(getResources().getString(R.string.hello_11));
				} else if(adminPass.equals("DIS")){
					MyLog.e("OnShowConnect", "DIS");
					DefaultViewWhenDisConect();
				} else { 
					if(skServer != null){
						String name = skServer.getName();
						String ip = skServer.getConnectedIPStr();
						ShowAdmin(name, ip, TouchDeviceAdmin.HIW_SETTING);
					}	
				}
				((MyApplication)getApplication()).setOnReceiverAdminPassListener(null);
			}
		});
				
				
	}
	
	private void gotoHiW_Dialog(){
		if(drawerLayout!=null){
			drawerLayout.closeDrawers();
		}
		SKServer skServer = ((MyApplication)getApplication()).getDeviceCurrent();
		
		if(dialogHiW == null){
			dialogHiW = new DialogHiW(context, getWindow(), this);
			dialogHiW.setOnHiWListener(new OnHiWListener() {
				
				@Override
				public void OnFinishListener() {
					dialogHiW = null;
				}

				@Override
				public void OnUpdateFirwaveFromWiFi() {
					processUpdatingFirmware();
				}

				@Override
				public void OnDownloadFirwaveFromServer() {
					
				}

				@Override
				public void OnUpdateFirmwareConfig() {
					setHiW_FirmwareConfig();
				}

				@Override
				public void OnSendMessage(String msg) {
					showDialogMessage(msg);
				}
			});
			String name = "";
			if(skServer != null){
				name = skServer.getName();
			}
			dialogHiW.showToast(name);			

			countNullOnReceiverHIW_FirmwareConfig = 0;
			
			((MyApplication)getApplication()).getHIW_Firmware();
			
			Timer tempTimer = new Timer();
			tempTimer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					((MyApplication)getApplication()).getHIW_FirmwareConfig();
				}
				
			}, 1000);	
			
			((MyApplication)getApplication()).setOnReceiverHIW_FirmwareListener(new OnReceiverHIW_FirmwareListener() {
				
				@Override
				public void OnReceiverHIW_Firmware(String resultString, String daumay_name,
						String daumay_version, String wifi_version, int wifi_revision) {
					MyLog.e("OnReceiverHIW_Firmware","START...........................");
					MyLog.e("","getResult = " + resultString);
					if(resultString == null || dialogHiW == null){
						MyLog.e("OnReceiverHIW_Firmware","END...........................");
						return;
					}
					
					MyLog.e("","daumay_name = " + daumay_name);
					MyLog.e("","daumay_version = " + daumay_version);
					MyLog.e("","wifi_version = " + wifi_version);
					MyLog.e("","wifi_revision = " + wifi_revision);		
					
					int int_daumay_version = Integer.parseInt(daumay_version);
					int int_wifi_version = Integer.parseInt(wifi_version);
					
					MyApplication.curHiW_firmwareInfo = new HiW_FirmwareInfo(daumay_name, int_daumay_version, int_wifi_version, wifi_revision);
					dialogHiW.setFirmwareInfoData(MyApplication.curHiW_firmwareInfo);
					
					MyLog.e("OnReceiverHIW_Firmware","END...........................");
				}
			});
			((MyApplication)getApplication()).setOnReceiverHIW_FirmwareConfigListener(new OnReceiverHIW_FirmwareConfigListener() {
				
				@Override
				public void OnReceiverHIW_FirmwareConfig(String resultString, int mode,
						String stationID, String stationPass, String apID, String apPass,
						String passConnect, String passAdmin) {
					MyLog.e("OnReceiverHIW_FirmwareConfig","START...........................");
					if(resultString == null || dialogHiW == null){
						MyLog.e("OnReceiverHIW_FirmwareConfig","END NULL...........................");
						if(countNullOnReceiverHIW_FirmwareConfig < 2){
							((MyApplication)getApplication()).getHIW_FirmwareConfig();	
						}		
						return;
					}					
					
					MyLog.e("","mode = " + mode);
					MyLog.e("","stationID = " + stationID);
					MyLog.e("","stationPass = " + stationPass);
					MyLog.e("","apID = " + apID);
					MyLog.e("","apPass = " + apPass);
					MyLog.e("","passConnect = " + passConnect);
					MyLog.e("","passAdmin = " + passAdmin);
					
					MyApplication.curHiW_firmwareConfig = new HiW_FirmwareConfig(mode, stationID, stationPass, apID, apPass, passConnect, passAdmin); 
					dialogHiW.setFirmwareConfigData(MyApplication.curHiW_firmwareConfig);
					
					MyLog.e("OnReceiverHIW_FirmwareConfig","END...........................");
				}
			});
		}
		
	}
	
	private int countNullOnReceiverHIW_FirmwareConfig = 0;
	
	// ---------- SHOW DIALOG MESSAGE
	public void showDialogMessage(String data) {
		hideshowDialogMessage();

		if (dialogNoAddPlayList == null) {
			MyLog.e(TAB, "ShowDialogNoAddPlayList");
			dialogNoAddPlayList = new DialogNoAddPlayList(context, getWindow());
			dialogNoAddPlayList
					.setOnDialogNoAddPlayListListener(new OnDialogNoAddPlayListListener() {

						@Override
						public void OnYesListener() {
							dialogNoAddPlayList = null;
						}

						@Override
						public void OnFinishListener() {
							dialogNoAddPlayList = null;
						}
					});
			dialogNoAddPlayList.showToast(data);
			hideshowDialogMessageTimer();
		}
	}
	
	public void showDialogMessageNoAutoHide(String data){
		hideshowDialogMessage();
		
		if(dialogNoAddPlayList == null){
			MyLog.e(TAB, "showDialogMessageNoAutoHide");
			dialogNoAddPlayList = new DialogNoAddPlayList(context, getWindow());
			dialogNoAddPlayList.setOnDialogNoAddPlayListListener(new OnDialogNoAddPlayListListener() {
				
				@Override
				public void OnYesListener() {
					dialogNoAddPlayList = null;
				}
				
				@Override
				public void OnFinishListener() {
					dialogNoAddPlayList = null;
				}
			});
			dialogNoAddPlayList.setAutoClose(false);
			dialogNoAddPlayList.showToast(data);
		}
	}
	
	public void showDialogMessageKaraoke(String data) {
		hideshowDialogMessage();
		
		if(!boolShowKaraoke){
			return;
		}		

		if (dialogNoAddPlayList == null) {
			MyLog.e(TAB, "showDialogMessageKaraoke -- " + data);
			dialogNoAddPlayList = new DialogNoAddPlayList(context, getWindow());
			dialogNoAddPlayList
					.setOnDialogNoAddPlayListListener(new OnDialogNoAddPlayListListener() {

						@Override
						public void OnYesListener() {
							dialogNoAddPlayList = null;
							hideKaraoke();
							onUpdateVideoLyric(1);
						}

						@Override
						public void OnFinishListener() {
							dialogNoAddPlayList = null;
						}
					});
			dialogNoAddPlayList.setAutoClose(false);
			dialogNoAddPlayList.showToast(data);
			hideshowDialogMessageTimer(10000);
		}
	}

	private void hideshowDialogMessage() {
		if (dialogNoAddPlayList != null) {
			dialogNoAddPlayList.hideToastBox();
		}
	}

	private Timer timerHideDialogMessage;

	private void hideshowDialogMessageTimer() {
		if (timerHideDialogMessage != null) {
			timerHideDialogMessage.cancel();
			timerHideDialogMessage = null;
		}
		timerHideDialogMessage = new Timer();
		timerHideDialogMessage.schedule(new TimerTask() {

			@Override
			public void run() {
				handlerAutoHideMessage.sendEmptyMessage(0);
			}
		}, 3000);

	}
	
	private void hideshowDialogMessageTimer(long time) {
		if (timerHideDialogMessage != null) {
			timerHideDialogMessage.cancel();
			timerHideDialogMessage = null;
		}
		timerHideDialogMessage = new Timer();
		timerHideDialogMessage.schedule(new TimerTask() {

			@Override
			public void run() {
				handlerAutoHideMessage.sendEmptyMessage(0);
			}
		}, time);

	}

	private Handler handlerAutoHideMessage = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if(dialogNoAddPlayList != null){
				dialogNoAddPlayList.hideToastBox();
			}
		}
	};
	
	// -------- HMINH - HiW get thong tin firmware + get config firmware
	public boolean isProcessingSomething = false;

	public void setHiW_FirmwareConfig() {
//		showDialogMessage(getResources().getString(R.string.update_firmware_4) + "...");
		
		Timer tempTimer = new Timer();
		tempTimer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				if(MyApplication.curHiW_firmwareConfig != null){
					((MyApplication)getApplication()).setHIW_FirmwareConfig(MyApplication.curHiW_firmwareConfig);	
				}	
			}
			
		}, 1000);		
	}
	
	// -------- HMINH - update firmware
	private Intent serviceIntent;

	public void forceUpdatingFirmware() {
		MyLog.e(TAB, "forceUpdatingFirmware................");
		
		if (serverStatus == null
				|| ((MyApplication) getApplication()).getSocket() == null) {
			return;
		}
		
		if(MyApplication.intSvrModel == MyApplication.SONCA || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
			return;
		}
		
		if (serviceIntent != null) {
			KTVMainActivity.this.stopService(serviceIntent);
			if (MyApplication.updateFirmwareServerSocket != null) {
				try {
					MyApplication.updateFirmwareServerSocket.close();
				} catch (Exception e) {

				}
			}
		}

		// ESP_FIRM_BEGIN
		serviceIntent = new Intent(KTVMainActivity.this, MyHTTPService.class);
		startService(serviceIntent);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				MyLog.e("", "Call Begin Update");

				hideshowDialogMessage();

				dialogNoAddPlayList = new DialogNoAddPlayList(context,
						getWindow());
				dialogNoAddPlayList.setAutoClose(false);
				dialogNoAddPlayList
						.setOnDialogNoAddPlayListListener(new OnDialogNoAddPlayListListener() {

							@Override
							public void OnYesListener() {
								dialogNoAddPlayList = null;
							}

							@Override
							public void OnFinishListener() {
								dialogNoAddPlayList = null;
							}
						});
				dialogNoAddPlayList
						.showToast(getResources()
								.getString(R.string.msg_6a)
								+ "..."
								+ getResources().getString(R.string.msg_6b));

				((MyApplication) getApplication())
						.setFirmwareUpdate_Command("2");

			}
		}, 1500);
	}
	
	public void processUpdatingFirmware() {
		MyLog.e(TAB, "processUpdatingFirmware................");
		
		if (serverStatus == null
				|| ((MyApplication) getApplication()).getSocket() == null) {
			return;
		}

		if(MyApplication.intSvrModel == MyApplication.SONCA || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
			return;
		}

		if(MyApplication.curHiW_firmwareInfo == null){
			return;
		}
		
		if (MyApplication.intSvrModel == MyApplication.SONCA_KARTROL || MyApplication.intSvrModel == MyApplication.SONCA_KM1) {	
			MyLog.e(" ", "UPDATE FOR KM1................");
			
			// CHECK UPDATE CONDITION
			int versionDauMay = MyApplication.curHiW_firmwareInfo
					.getDaumay_version();
			int versionBoardWifi = MyApplication.curHiW_firmwareInfo
					.getWifi_version();

			SaveFirmware saveFirmware = SaveFirmware.getInstance(context);
			int versionApp = saveFirmware.getVersionFirmwareKM1();

			MyLog.e("versionDauMay", versionDauMay + "");
			MyLog.e("versionBoardWifi", versionBoardWifi + "");
			MyLog.e("versionApp", versionApp + "");

			boolean flagUpdate = false;
			if (versionApp > versionBoardWifi) {
				flagUpdate = true;
			} else {
				int revisionApp = saveFirmware.getRevisionFirmwareKM1();
				int revisionBoardWifi = MyApplication.curHiW_firmwareInfo
						.getWifi_revision();
				MyLog.e("revisionApp", revisionApp + "");
				MyLog.e("revisionBoardWifi", revisionBoardWifi + "");
				if (revisionApp > revisionBoardWifi) {
					flagUpdate = true;
				}
			}

			if (!flagUpdate) {
				showDialogMessage(getResources().getString(
						R.string.update_firmware_3));
				return;
			}

			// CHUA XU LY ESP_FIRM_CHECK
			((MyApplication) getApplication()).setFirmwareUpdate_Command("0");

			if (serviceIntent != null) {
				KTVMainActivity.this.stopService(serviceIntent);
				if (MyApplication.updateFirmwareServerSocket != null) {
					try {
						MyApplication.updateFirmwareServerSocket.close();
					} catch (Exception e) {

					}
				}
			}

			// ESP_FIRM_BEGIN
			serviceIntent = new Intent(KTVMainActivity.this, MyHTTPService.class);
			startService(serviceIntent);

			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					MyLog.e("", "Call Begin Update");

					hideshowDialogMessage();

					dialogNoAddPlayList = new DialogNoAddPlayList(context,
							getWindow());
					dialogNoAddPlayList.setAutoClose(false);
					dialogNoAddPlayList
							.setOnDialogNoAddPlayListListener(new OnDialogNoAddPlayListListener() {

								@Override
								public void OnYesListener() {
									dialogNoAddPlayList = null;
								}

								@Override
								public void OnFinishListener() {
									dialogNoAddPlayList = null;
								}
							});
					dialogNoAddPlayList
							.showToast(getResources()
									.getString(R.string.msg_6a)
									+ "..."
									+ getResources().getString(R.string.msg_6b));

					((MyApplication) getApplication())
							.setFirmwareUpdate_Command("2");

				}
			}, 1500);
			
			
		} else if (MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
				|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
				|| MyApplication.intSvrModel == MyApplication.SONCA_KBX9 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
				 || MyApplication.intSvrModel == MyApplication.SONCA_TBT) {
			MyLog.e(" ", "UPDATE FOR HIW................");

			// CHECK UPDATE CONDITION
			int versionDauMay = MyApplication.curHiW_firmwareInfo
					.getDaumay_version();
			int versionBoardWifi = MyApplication.curHiW_firmwareInfo
					.getWifi_version();
			
			SaveFirmware saveFirmware = SaveFirmware.getInstance(context);
			int versionApp = saveFirmware.getVersionFirmwareHiW();

			MyLog.e("versionDauMay", versionDauMay + "");
			MyLog.e("versionBoardWifi", versionBoardWifi + "");
			MyLog.e("versionApp", versionApp + "");
			
//			if(versionDauMay < 100){
//				if (versionDauMay != (versionApp / 100)) {
//					showDialogMessage(getResources().getString(R.string.update_firmware_2));
//					return;
//				}
//			} else {
//				if ((versionDauMay / 100) != (versionApp / 100)) {
//					showDialogMessage(getResources().getString(R.string.update_firmware_2));
//					return;
//				}
//			}
			
			boolean flagUpdate = false;
			if(versionApp > versionBoardWifi){
				flagUpdate = true;
			} else {
				int revisionApp = saveFirmware.getRevisionFirmwareHiW();
				int revisionBoardWifi = MyApplication.curHiW_firmwareInfo.getWifi_revision();
				MyLog.e("revisionApp", revisionApp + "");
				MyLog.e("revisionBoardWifi", revisionBoardWifi + "");
				if(revisionApp > revisionBoardWifi){
					flagUpdate = true;
				}
			}
			
			if(!flagUpdate){
				showDialogMessage(getResources().getString(R.string.update_firmware_3));
				return;
			}
			
			// CHUA XU LY ESP_FIRM_CHECK
			((MyApplication) getApplication()).setFirmwareUpdate_Command("0");
			
			if (serviceIntent != null) {
				KTVMainActivity.this.stopService(serviceIntent);
				if(MyApplication.updateFirmwareServerSocket != null){
					try {
						MyApplication.updateFirmwareServerSocket.close();	
					} catch (Exception e) {
						
					}							
				}					
			}	
			
			// ESP_FIRM_BEGIN
			serviceIntent = new Intent(KTVMainActivity.this, MyHTTPService.class);
			startService(serviceIntent);
					
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					MyLog.e("", "Call Begin Update");
									
					hideshowDialogMessage();

					dialogNoAddPlayList = new DialogNoAddPlayList(context,
							getWindow());
					dialogNoAddPlayList.setAutoClose(false);
					dialogNoAddPlayList
							.setOnDialogNoAddPlayListListener(new OnDialogNoAddPlayListListener() {

								@Override
								public void OnYesListener() {
									dialogNoAddPlayList = null;
								}

								@Override
								public void OnFinishListener() {
									dialogNoAddPlayList = null;
								}
							});
					dialogNoAddPlayList.showToast(getResources().getString(R.string.msg_6a) + "..." + getResources().getString(R.string.msg_6b));				
					
					
					((MyApplication) getApplication())
							.setFirmwareUpdate_Command("2");					
					
				}
			}, 1000);
			
		}	
		
	}

	@Override
	public void deviceInform_FailedAddSong() {
		if(((MyApplication) KTVMainActivity.this.getApplication()).getListActive().size() > 95){
			
			return;
		}
		
		showDialogMessage(getResources().getString(R.string.msg_10));	
	}
	
	@Override
	public void deviceInform_Timeout_Show(final String commandName) {
		MyLog.d(TAB, "deviceInform_Timeout_Show");
		
		if(!boolShowKaraoke 
				&& !commandName.equals("CMD_REQ_SYNCSERVERSTATUS")
				&& !commandName.contains("CMD_SYNC_PLAYLIST")
				&& !commandName.contains("CMD_SET_ONOFFUSERLIST")){
			runOnUiThread(new Runnable() {
				public void run() {
					if(commandName.equals("CMD_BLAHBLAH")){
						showDialogMessage(getResources().getString(R.string.msg_15) + "...\n" + getResources().getString(R.string.msg_15a));
						return;
					}
					
					MyApplication.flagDisplayTimeout = true;

					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							MyApplication.flagDisplayTimeout = false;
						}
					}, 1000);	
					
				}
			});	
		}	
	}

	@Override
	public void deviceInform_Timeout_Close() {
		MyLog.d(TAB, "deviceInform_Timeout_Close");
		
		runOnUiThread(new Runnable() {
			public void run() {
				hideshowDialogMessage();
			}
		});
	}
	
	@Override
	public void deviceInform_ShowMessage(final String msgString) {
		MyLog.d(TAB, "deviceInform_ShowMessage");
		
		runOnUiThread(new Runnable() {
			public void run() {
				if(msgString.equals("HIW_SETTING")){
					showDialogMessage(getResources().getString(R.string.msg_19));
				}
			}
		});
	}

		
//////////////////- FIRMWARE FOR HI_W - //////////////////////////////

	private LoadCheckUpdateFirmware checkUpdateFirmware;
	private void CheckUpdateFirmwareFromServer(Context context) {
		if (checkUpdateFirmware == null) {
			MyLog.e(TAB, "LoadCheckUpdateFirmware IS NUTT");
			checkUpdateFirmware = new LoadCheckUpdateFirmware(context);
			checkUpdateFirmware.setOnLoadCheckUpdateFirmwareListener(this);
			checkUpdateFirmware.execute();
		} else {
			MyLog.e(TAB, "LoadCheckUpdateFirmware : NOT NULL");
			if (checkUpdateFirmware.getStatus() == AsyncTask.Status.FINISHED) {
				MyLog.e(TAB, "LoadCheckUpdateFirmware : FINISHED");
				checkUpdateFirmware = null;
				checkUpdateFirmware = new LoadCheckUpdateFirmware(context);
				checkUpdateFirmware.setOnLoadCheckUpdateFirmwareListener(this);
				checkUpdateFirmware.execute();
			}
		}
	}

	@Override
	public void OnCheckFinish(ArrayList<Firmware> url, int version, int revision) {
		if(url != null && url.size() > 0){
			MyLog.i(TAB, "url : " + url);
			if(MyApplication.flagProcessCheckAll){
				checkAllFromServer_total += 1;
				OnUpdateLyric2();
			} else {
				ShowDialogUpdateFirmware(url, version, revision);				
			}
		}else{
			OnUpdateLyric2();
			// ShowToastInfo(context.getString(R.string.update_firmware_1));
		}
	}

	private DialogUpdateFirmwareFromServer dialogFirmware;
	private void ShowDialogUpdateFirmware(ArrayList<Firmware> url, int version, int revision) {
		if(dialogFirmware == null){
			dialogFirmware = new DialogUpdateFirmwareFromServer(context, getWindow(), this);
			dialogFirmware.setOnUpdateFirmwareFromServer(new OnUpdateFirmwareFromServer() {
				
				@Override
				public void OnFinishListener() {
					dialogFirmware = null;
					OnUpdateLyric2();
				}

				@Override
				public void OnCancel() {
					dialogFirmware = null;
					onUpdateVideoLyric(1);
				}
			});
			dialogFirmware.setLinkDownload(url, version, revision);
			dialogFirmware.showToast();
		}
	}

	private Toast toastInfo = null;

	private void ShowToastInfo(String info) {
		HideToastInfo();
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View toastRoot = layoutInflater.inflate(R.layout.touch_toast_info_hiw, null);
		toastInfo = new Toast(context);
		toastInfo.setView(toastRoot);
		TextView textView = (TextView) toastRoot.findViewById(R.id.textPercentLyric);
		textView.setText(info);
		toastInfo.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
		toastInfo.setDuration(Toast.LENGTH_SHORT);
		toastInfo.show();
	}

	private void HideToastInfo() {
		if (toastInfo != null) {
			toastInfo.cancel();
			toastInfo = null;
		}
	}
		
	// SET NEXT SONG DB
	private Timer timerSetNextSongDB;
	
	private void setNextSongDB() {
		if (timerSetNextSongDB != null) {
			timerSetNextSongDB.cancel();
			timerSetNextSongDB = null;
		}

		timerSetNextSongDB = new Timer();
		timerSetNextSongDB.schedule(new TimerTask() {
			private String resultName;
			private int resultID;

			@Override
			public void run() {
				try {
//					if (groupView == null) {
//						return;
//					}

					resultName = "";
//					resultID = groupView.getCurrentNextIdx5();

					ArrayList<Song> list = ((MyApplication) context
							.getApplicationContext()).getListActive();

					int tempID = list.get(0).getIndex5();
					MyLog.d(" ", " ");MyLog.d(" ", "timerSetNextSongDB");
					MyLog.d(" ", "tempID = " + tempID);
					if (resultID != tempID) {
						ArrayList<Song> songs = DBInterface.DBSearchSongID(
								tempID + "", 0 + "", context);
						if (!songs.isEmpty()) {
							resultName = songs.get(0).getName();
							resultID = tempID;
						}else {
							if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
								ArrayList<Song> songsYoutube = DBInterface.DBSearchSongID_YouTube(tempID + "", 0 + "", context);
								if (!songsYoutube.isEmpty()) {
									resultName = songsYoutube.get(0).getName();
									resultID = tempID;
								}
								
							}
						}
					}

					handler.sendEmptyMessage(0);
				} catch (Exception e) {
					handler.sendEmptyMessage(1);
				}
			}

			private Handler handler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					if (msg.what == 0) {
//						if (groupView != null) {
//							groupView.setNextSongName(resultName, resultID);
							if(colorLyricListener != null){
								colorLyricListener.OnMain_setNextSongPlaylist(resultName, resultID);
//							}
						}
					} else {
//						if (groupView != null) {
//							groupView.setNextSongName("", -1);
							if(colorLyricListener != null){
								colorLyricListener.OnMain_setNextSongPlaylist("", -1);
							}
//						}
					}
				}
			};
		}, 100);
	}
	
	public void onCallShowGiaoDienDialog(){
		if(serverStatus == null){
			ShowDialogSettingApp();
			return;
		}
		
		drawerLayout.openDrawer(layoutConnect);
		
		SKServer skServer = ((MyApplication)getApplication()).getDeviceCurrent();
		if(skServer != null){
			String name = skServer.getName();
			String ip = skServer.getConnectedIPStr();
			ShowAdmin(name, ip, TouchDeviceAdmin.GIAODIEN);
		}
	}
		
	private int switchColorScreen = 0;
	private DialogSettingApp dialogSettingApp = null;
	private void ShowDialogSettingApp(){
		if(dialogSettingApp == null){
			dialogSettingApp = new DialogSettingApp(context, getWindow(), this);
			dialogSettingApp.setOnDialogSettingAppListener(new OnDialogSettingAppListener() {
				@Override public void OnFinishListener() {
					dialogSettingApp = null;
				}

				@Override
				public void OnClickSleep() {
					// drawerLayout.closeDrawer(layoutConnect);
				}

				@Override
				public void OnChangeColorScreen(int colorScreen) {			
					switchColorScreen = colorScreen;
					AppSettings setting = AppSettings.getInstance(getApplicationContext());	
					int oldScreen = setting.getColorScreen();
					
					if(oldScreen != switchColorScreen){
						showMyPopupAsking(5, null, getResources().getString(R.string.switchscreen_1a), getResources().getString(R.string.switchscreen_2));	
					}					
					
				}

				@Override
				public void OnInfoUpdateApp() {
					gotoAppStore();
				}

				@Override
				public void OnInfoUpdateData() {
					onUpdateAllFromServer();
				}

				@Override
				public void OnInfoResetDB() {
					showMyPopupAsking(4, null, getResources().getString(R.string.khoiphuc_1), getResources().getString(R.string.khoiphuc_2));			
				}

				@Override
				public void OnChangeSwitchState() {
					if(viewWifi != null){
						viewWifi.invalidate();
					}
				}
				
				@Override
				public void OnChangeAdminYouTube() {
					
				}
				
				@Override
				public void OnUpdateKhoaYouTube() {
					if(serverStatus != null){
						setOnOffControlFull();
					}
				}
				
			});
			dialogSettingApp.showToast();
		}
	}
	
	private void processResetToPackageDB() {
		try {
			long tgian = System.currentTimeMillis();
			if((tgian - MyApplication.timeRevert) < 5000){
				return;
			}
			
//			FragmentTransaction fragmentTransaction = fragmentManager
//					.beginTransaction();
//			if (fragmentBase != null && SAVETYPE == SONG) {
//				((TouchFragmentSong)fragmentBase).forceCloseDB();
//			}
//			fragmentTransaction.commit();	
			
			popupViewUpadtePic
					.setPopupLayout(TouchPopupViewUpdatePic.LAYOUT_PROCESSDATA2);
			myDialogUpdatePic.getWindow().setGravity(Gravity.CENTER);
			WindowManager.LayoutParams params = myDialogUpdatePic.getWindow()
					.getAttributes();
			myDialogUpdatePic.getWindow().setAttributes(params);
			myDialogUpdatePic.show();
	
			MyApplication.timeRevert = tgian;
			
			String rootPath = "";

			if (android.os.Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED)) {
				rootPath = android.os.Environment
						.getExternalStorageDirectory()
						.toString()
						.concat(String.format("/%s/%s", "Android/data",
								getPackageName()));
				File appBundle = new File(rootPath);
				if (!appBundle.exists())
					appBundle.mkdir();
			}
			
			boolean flagA = false;
			
			if(MyApplication.intSvrModel == MyApplication.SONCA || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
				flagA = true;
				
				String dbPath = getDatabasePath(DbHelper.DBName).getAbsolutePath();
				
				AssetManager assetMgr = this.getAssets();
				DBInterface.DBCloseDatabase(context);

				InputStream is = assetMgr.open("database.db");
				OutputStream os = new FileOutputStream(dbPath);
				copyFile(is, os);
				is.close();
				os.flush();
				os.close();
								
				MyApplication.mSongTypeList = null;
				processTotalSongType(100);

				AppSettings setting = AppSettings
						.getInstance(getApplicationContext());
				setting.saveServerLastUpdate(MyApplication.intPackageSK9x_DISC, 1);
				setting.saveServerLastUpdate(0, 2);
				setting.saveServerLastUpdate(MyApplication.intPackageSK9x_HDD, 3);
				setting.saveServerLastUpdate(0, 4);				
			} else {
				String dbPathStar = getDatabasePath(DbHelper.DBNameStar).getAbsolutePath();
				
				AssetManager assetMgr = this.getAssets();
				DBInterface.DBCloseDatabase(context);
				
				InputStream is = assetMgr.open("database_star.db");
				OutputStream os = new FileOutputStream(dbPathStar);
				copyFile(is, os);
				is.close();
				os.flush();
				os.close();
				
				MyApplication.mSongTypeList = null;
				processTotalSongType(100);
				
				AppSettings setting = AppSettings.getInstance(getApplicationContext());
				setting.saveHiwLastUpdate(MyApplication.packageHIW_date);
				setting.saveHiwHDDVersion(MyApplication.intPackageHIW_DISC);
				setting.saveHiwDISCVersion(MyApplication.intPackageHIW_HDD);	
			}		
			
			SharedPreferences settings = getSharedPreferences(PREFS_DEVICE, 0);
			settings.edit().putInt("saveType", -1).commit();
			ChangeWifiRmote();	

			if(flagA){
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						processNewSongTable();
						if(myDialogUpdatePic != null){
							myDialogUpdatePic.dismiss();
						}
						makeToastMessage(getResources().getString(R.string.msg_khoiphuc_2));
						reloadSongList();
					}
				}, 2000);	
			} else {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						if(myDialogUpdatePic != null){
							myDialogUpdatePic.dismiss();
						}
						makeToastMessage(getResources().getString(R.string.msg_khoiphuc_2));
						reloadSongList();
					}
				}, 2000);	
			}
			
		} catch (Exception e) {

		}
	}
	
	private void gotoAppStore(){
		Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
		Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
		goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY
				| Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET
				| Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
		try {
			startActivity(goToMarket);
		} catch (ActivityNotFoundException e) {
			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse("http://play.google.com/store/apps/details?id="
							+ context.getPackageName())));
		}
	}
	
	private void HideDialogSettingApp(){
		if(dialogSettingApp != null){
			dialogSettingApp.hideToastBox();
		}
	}

	@Override
	public void OnSettingSleep() {
		ShowDialogSettingApp();
	}
	
	// ---------------------------
		private Timer timerCountTotalSongType;
		
		public void processTotalSongType(final int timeDelay){
			if(MyApplication.mSongTypeList != null && MyApplication.mSongTypeList.size() > 0){
				return;
			}
			
			if (timerCountTotalSongType != null) {
				timerCountTotalSongType.cancel();
				timerCountTotalSongType = null;
			}
			
			timerCountTotalSongType = new Timer();
			timerCountTotalSongType.schedule(new TimerTask() {
				
				@Override
				public void run() {
					List<SongType> songTypeList = new ArrayList<SongType>();
					
					songTypeList = DBInterface.DBSearchSongType("",
							SearchMode.MODE_FULL, 0, 0, KTVMainActivity.this);
					for (SongType temp : songTypeList) {
						if (temp.getID() == 0) {
							songTypeList.remove(temp);
							break;
						}
					}

					songTypeList.add(0, new SongType(DbHelper.SongType_NewSongClub,
							getResources().getString(R.string.clb_new_songs)));
					songTypeList.add(0, new SongType(
							DbHelper.SongType_CoupleSinger, getResources()
									.getString(R.string.type_songca)));
					songTypeList.add(0, new SongType(DbHelper.SongType_LiveSong,
							getResources().getString(R.string.type_livesong)));
					songTypeList.add(0, new SongType(DbHelper.SongType_Remix,
							"Remix"));
					songTypeList.add(0, new SongType(DbHelper.SongType_HotSong,
							getResources().getString(R.string.type_hotsong_2)));
					songTypeList.add(0, new SongType(DbHelper.SongType_NewVol,
							getResources().getString(R.string.type_newvol_2)));
					songTypeList.add(0, new SongType(DbHelper.SongType_UpdateSong,
							getResources().getString(R.string.type_updatesong)));
					
					for (int i = 0; i < songTypeList.size(); i++) {
						int tempTypeID = songTypeList.get(i).getID();

						int tempTotal = 0;

						if (tempTypeID == DbHelper.SongType_Remix) {
							tempTotal = DBInterface.DBCountTotalSongRemix(context,
									"", MEDIA_TYPE.ALL);
						} else {
							if(tempTypeID == DbHelper.SongType_NewVol 
									&& (MyApplication.intSvrModel == MyApplication.SONCA_KM1 || MyApplication.intSvrModel == MyApplication.SONCA_KM2
									|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
									|| MyApplication.intSvrModel == MyApplication.SONCA_KBX9 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI)){
								tempTotal = DBInterface.DBCountTotalSongTypeID_KM(context, "",
										SearchType.SEARCH_TYPE, MEDIA_TYPE.ALL,
										Integer.valueOf(DbHelper.SongType_NewVol));
							} else {
								tempTotal = DBInterface.DBCountTotalSongTypeID(context,
										"", SearchType.SEARCH_TYPE, MEDIA_TYPE.ALL,
										songTypeList.get(i).getID());
							}
						}

						songTypeList.get(i).setCountTotal(tempTotal);
					}
					
					MyApplication.mSongTypeList = songTypeList;
				}
			}, timeDelay);
		}

		@Override
		public void OnSelectListFragmentBackList() {
			ShowListDevice("");
		}

		@Override
		public void OnClickSelectItem() {
//			groupView.invalidate();
			
			if(TouchDanceLinkView.DANCE == danceLinkView.getLayout()){
				return;
			}
			
			showFragSwitch();
			
			MyLog.e("OnClickSelectItem", "select list = " + MyApplication.intSelectList);
		}

		@Override
		public void OnCloseSelectList() {
			drawerLayout.closeDrawer(layoutConnect);
		}

		@Override
		public void OnKM1_SelectList() {
			if(MyApplication.intWifiRemote == MyApplication.SONCA_KARTROL){
				ShowSelectList("", "");
				if (!drawerLayout.isDrawerOpen(layoutConnect)) {
					drawerLayout.openDrawer(layoutConnect);
				}	
			}	
		}

	public RelativeLayout getLayoutMain(){
		return this.layoutMain;
	}
	
	// --------------------------
	private DialogConfirm dialogConfirm = null;

	private void ShowConfirmDialog(String msg1, String msg2,
			final int typeDialog, final int songID, final int songTypeABC,
			final int songIdx) {
		if (serverStatus == null) {
			return;
		}

		if (dialogConfirm != null) {
			dialogConfirm.hideToastBox();
			dialogConfirm = null;
		}

		if (dialogConfirm == null) {
			MyLog.e(TAB, "dialogConfirm");
			dialogConfirm = new DialogConfirm(context, getWindow());
			dialogConfirm
					.setOnDialogConfirmListener(new OnDialogConfirmListener() {

						@Override
						public void OnYesListener() {
							switch (typeDialog) {
							case 0: // Them bai hat
								((MyApplication) getApplication()).addSongToPlaylist(songID, songTypeABC, songIdx);
								break;
							case 1: // Them bai hat - KARTROL
								((MyApplication) getApplication()).addSongToPlaylistKartrol(songID, songTypeABC, songIdx);
								break;
							case 2: // First bai hat
								((MyApplication) getApplication()).firstReservedSong(songID, songTypeABC, -1);	
								break;
							case 3: // First bai hat - KARTROL
								((MyApplication) getApplication()).firstReservedSongKartrol(songID, songTypeABC, -1);							
								break;
							case 4: // Hat Lai
								((MyApplication) getApplication()).sendCommand(NetworkSocket.REMOTE_CMD_REPEAT, 0);				
								break;
							case 5: // Hat Lai - KARTROL
								((MyApplication) getApplication()).sendCommandKartrol((byte) RemoteIRCode.IRC_REPEAT,0);
								break;
							case 6: // Qua Bai
								((MyApplication) getApplication()).sendCommand(NetworkSocket.REMOTE_CMD_NEXT, 0);
								break;
							case 7: // Qua Bai - KARTROL
								((MyApplication) getApplication()).sendCommandKartrol((byte) RemoteIRCode.IRC_STOP,0);
								break;
							default:
								break;
							}
							
							dialogConfirm = null;
						}

						@Override
						public void OnNoListener() {
							dialogConfirm = null;
						}

						@Override
						public void OnFinishListener() {
							dialogConfirm = null;
						}
					});
			dialogConfirm.showToast(msg1, msg2);
		}
	}
	
	/************** fragment Karaoke *************************/
	private Fragment fragment_Karaoke = null;
	public static boolean boolShowKaraoke = false;
	
	private OnColorLyricListener colorLyricListener;

	private int karaoke_ColorID = 0;
	private int karaoke_lastSize = 0;
	
	private void showKaraoke() {
		MyLog.d(TAB, "=showKaraoke===");
		if (isDestroyMainActivity == false) {
			return;
		}
		if (serverStatus == null) {
			return;
		}
		if (boolShowKaraoke != true) {
			int id_ = serverStatus.getPlayingSongID();// get id song playing
			karaoke_ColorID = id_;
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				if (fragment_Karaoke != null) {
					fragmentTransaction.remove(fragment_Karaoke);
				}

				fragment_Karaoke = new FragmentKaraoke();
				SKServer skServer = ((MyApplication)getApplication()).getDeviceCurrent();
				if(skServer != null){
					
					IpDeviceConnect = skServer.getConnectedIPStr();
					
				}
				Bundle bundle = new Bundle();
				bundle.putString("IP", IpDeviceConnect);
				bundle.putInt("id", id_);
				bundle.putInt("intMediaType", serverStatus.getMediaType());
				bundle.putInt("midiShifTime", serverStatus.getMidiShiftTime());
				bundle.putBoolean("Pause", serverStatus.isPlaying());
				bundle.putInt("cntPlaylist", serverStatus.getReservedSongs().size());
				if(MyApplication.intSvrModel == MyApplication.SONCA || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
					bundle.putInt("mainTypeABC", serverStatus.getPlayingSongTypeABC());	
				} else {
					bundle.putInt("mainTypeABC", -1);
				}	
				bundle.putBoolean("VocalSinger", serverStatus.isSingerOn());
				bundle.putString("ServerName", ((MyApplication) getApplication()).getDeviceCurrent().getName());
				colorLyricListener = (OnColorLyricListener)fragment_Karaoke;
				fragment_Karaoke.setArguments(bundle);
				fragmentTransaction.replace(R.id.fragmentKaraoke1,
						fragment_Karaoke, "KARAOKE");

				layoutColorLyric.setVisibility(View.VISIBLE);

				fragmentTransaction.commit();
				MyLog.i(TAB, "showKaraoke");

				boolShowKaraoke = true;
				
				viewWifi.invalidate();
		}
	}

	private void hideKaraoke() {
		MyApplication.freezeTime = System.currentTimeMillis();
		if (boolShowKaraoke != false) {
			MyLog.d(TAB, "=hideKaraoke===");
			((FragmentKaraoke)fragment_Karaoke).stopKaraoke();
			
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			fragmentTransaction.remove(fragment_Karaoke);
			fragmentTransaction.commit();
			MyLog.i(TAB, "hideKaraoke");
			boolShowKaraoke = false;
		}

		colorLyricListener = null;
		layoutColorLyric.setVisibility(View.GONE);
	}

	@Override
	public void OnBackKaraoke() {
		hideKaraoke();
	}
	
	@Override
	public void OnBackReviewKaraoke(boolean iError) {
			MyLog.d(TAB, "=hideKaraoke===");
			((FragmentReviewKaraoke)fragment_Karaoke).stopKaraoke();
			
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			fragmentTransaction.remove(fragment_Karaoke);
			fragmentTransaction.commit();
			MyLog.i(TAB, "hideKaraoke");
			
		layoutColorLyric.setVisibility(View.GONE);
		if(iError)
			ShowDialogNoAddPlayList(getResources().getString(R.string.activity_karaoke_ReviewKaraokeError));
	}
	
	/************** video lyric *************************/
	public boolean isDownloadingVideo = false;
	private String videoDirPath = "";
	
	public boolean flagLeftVideoDown = false;
	
	public void onUpdateVideoLyric(int step){
		try {
			if (step == 1) {
				if (myDialogUpdateToc != null) {
					myDialogUpdateToc.dismiss();
				}
				
				videoDirPath = android.os.Environment.getExternalStorageDirectory().toString().concat("/SONCA/VIDEO");
				File fDir = new File(videoDirPath);
				if(!fDir.exists()){
					fDir.mkdirs();
				}
				
				// ----- CHECK version
				myDialogUpdateToc = new Dialog(this);
				myDialogUpdateToc.requestWindowFeature(Window.FEATURE_NO_TITLE);
				myDialogUpdateToc.getWindow().setBackgroundDrawable(
						new ColorDrawable(android.graphics.Color.TRANSPARENT));
				myDialogUpdateToc
						.setContentView(R.layout.touch_item_popup_update_toc);
				myDialogUpdateToc.setCanceledOnTouchOutside(true);
				
				WindowManager.LayoutParams pa = myDialogUpdateToc.getWindow()
						.getAttributes();
				pa.height = WindowManager.LayoutParams.MATCH_PARENT;
				pa.width = WindowManager.LayoutParams.MATCH_PARENT;
				pa.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
						| WindowManager.LayoutParams.FLAG_FULLSCREEN;

				myDialogUpdateToc.getWindow().setAttributes(pa);

				popupViewUpdateToc = (TouchPopupViewUpdateToc) myDialogUpdateToc
						.findViewById(R.id.myPopupUpdateToc);
				popupViewUpdateToc
						.setOnPopupUpdateTocListener(new OnPopupUpdateTocListener() {

							@Override
							public void OnUpdateTocYes() {
								downloadVideo();
								myDialogUpdateToc.dismiss();
							}

							@Override
							public void OnUpdateTocNo() {
								isDownloadingVideo = false;
								MyApplication.freezeTime = System.currentTimeMillis();
								
								clearDownloadVideoResource();
								myDialogUpdateToc.dismiss();
							}
						});

				pingTime = System.currentTimeMillis();
				
				InterruptPingServerTask tTask = new InterruptPingServerTask();
				tTask.setFlagPingVideo(true);
				tTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
				startPingTocServer(99);
			} else if (step == 2) {
				String appRootPath = videoDirPath;
				File appBundle = new File(appRootPath);
				if (!appBundle.exists())
					appBundle.mkdir();
				
				final String updateInfo_path = appRootPath
						.concat("/videoinfo.xml");
				File tempF = new File(updateInfo_path);
				if (tempF.exists()) {
					tempF.delete();
				}

				new Thread(new Runnable() {
					public void run() {
						isDownloadingVideo = true;
						downloadVideoFile(
								"https://kos.soncamedia.com/firmware/karconnect/video/videoinfo.xml",
								updateInfo_path);
					}
				}).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
			isDownloadingVideo = false;
			MyApplication.freezeTime = System.currentTimeMillis();
		}
	}

	public void downloadVideoFile(String url, String dest_file_path) {
		try {
			File dest_file = new File(dest_file_path);
			URL u = new URL(url);
			DataInputStream fis = new DataInputStream(u.openStream());
			DataOutputStream fos = new DataOutputStream(new FileOutputStream(
					dest_file));

			int bytes_read = 0;
			int buffer_size = 1024 * 1024;
			byte[] buffer = new byte[buffer_size];

			while ((bytes_read = fis.read(buffer, 0, buffer_size)) > 0) {
				if (!isDownloadingVideo) {
					progressDialog.dismiss();
					return;
				}
				fos.write(buffer, 0, bytes_read);
			}

			fis.close();
			fos.flush();
			fos.close();

			hideProgressVideoIndicator(1);
		} catch (Exception e) {
			isDownloadingVideo = false;
			MyApplication.freezeTime = System.currentTimeMillis();
			clearDownloadVideoResource();
			return;
		}
	}
	
	private void clearDownloadVideoResource(){
		try {
			flagLeftVideoDown = false;
			
			String appRootPath = android.os.Environment
					.getExternalStorageDirectory()
					.toString()
					.concat(String.format("/%s/%s", "Android/data",
							getPackageName()));
			String xmlPath = appRootPath.concat("/videoinfo.xml");
			File f = new File(xmlPath);
			if (f.exists()) {
				f.delete();
			}
			
			File folderVideo = new File(videoDirPath);
			for (final File fileEntry : folderVideo.listFiles()) {
				if(fileEntry.getName().endsWith(".mp4") || fileEntry.getName().endsWith(".avi")){
					continue;
				}
				
				fileEntry.delete();
			}
			
			if(dialogSettingApp != null){
				dialogSettingApp.updateXoaVideoView();
			}
		} catch (Exception e) {
		}
	}
	
	private void downloadVideo(){
		try {
			progressDialog = new Dialog(this);
			progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			progressDialog.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));
			progressDialog.setContentView(R.layout.touch_item_popup_down_percent);
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.setCancelable(false);
			
			WindowManager.LayoutParams pa = progressDialog.getWindow()
					.getAttributes();
			pa.height = WindowManager.LayoutParams.MATCH_PARENT;
			pa.width = WindowManager.LayoutParams.MATCH_PARENT;
			pa.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
					| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
					| WindowManager.LayoutParams.FLAG_FULLSCREEN;

			progressDialog.getWindow().setAttributes(pa);

			popupPercent = (TouchPopupViewDownloadPercent) progressDialog
					.findViewById(R.id.myPopupPercent);
			popupPercent
					.setOnPopupDownloadPercentListener(new OnPopupDownloadPercentListener() {

						@Override
						public void OnCancelDownload() {
							isDownloadingVideo = false;
							clearDownloadVideoResource();
							progressDialog.dismiss();	
						}
					});

			progressDialog.getWindow().setGravity(Gravity.CENTER);
			WindowManager.LayoutParams params = progressDialog.getWindow()
					.getAttributes();
			progressDialog.getWindow().setAttributes(params);
			progressDialog.show();

			new Thread(new Runnable() {
				public void run() {
					isDownloadingVideo = true;
					downloadMultipleVideoFile();
				}
			}).start();
		} catch (Exception e) {
			isDownloadingVideo = false;
			MyApplication.freezeTime = System.currentTimeMillis();
			progressDialog.dismiss();
		}
	}
	
	private long totalVideoSize = 0;
	
	private void hideProgressVideoIndicator(final int step){
		MyLog.e("hideProgressVideoIndicator", "step = " + step);
		runOnUiThread(new Runnable() {
			public void run() {
				try {
					if (step == 2) {
						progressDialog.dismiss();
					}
										
					final String appRootPath = videoDirPath;
					String updateInfo_path = appRootPath.concat("/videoinfo.xml");
					
					if (step == 1) {
						boolean flagUpdate = false;
						boolean flagSpecial = false;						
						
						// get device screen portion
						int w = getResources().getDisplayMetrics().widthPixels;
						int h = getResources().getDisplayMetrics().heightPixels;
						boolean flagType_3_4 = false;
						if((float)w/h < 1.55){
							flagType_3_4 = true;
						}
						
						InputStream is = new FileInputStream(updateInfo_path);
						Document doc = XmlUtils.convertToDocument(is);
						NodeList nodeList = doc.getElementsByTagName("video");
						
						fileVideoPath = "";
						
						infoList = new ArrayList<String>();
						totalVideoSize = 0;
						
						long appVideoSize = 0;
						File parentFolder = new File(appRootPath);
						if(parentFolder.exists()){
							File[] listFile = parentFolder.listFiles();
							if(listFile.length > 0){
								for (File file : listFile) {
									String vFileName = file.getName();
									if(vFileName.toUpperCase().endsWith(".MP4")){
										appVideoSize = file.length();
									}
								}
							}
						}
						
						// UU TIEN TAG android_mhtouch
						for (int i = 0; i < nodeList.getLength(); i++) {
							Node node = nodeList.item(i);
							if (node.hasAttributes()) {
								Attr attr = (Attr) node.getAttributes().getNamedItem("name");
								String strName  = attr.getValue();
								if(strName.equalsIgnoreCase("android_mhtouch")){
									flagSpecial = true;
									
									Element e = (Element) node;
									NodeList nlTemp = e.getElementsByTagName("width");
									Node nTemp = nlTemp.item(0);
									String strWidth = nTemp.getTextContent();
									int intWidth = Integer.parseInt(strWidth);

									nlTemp = e.getElementsByTagName("height");
									nTemp = nlTemp.item(0);
									String strHeight = nTemp.getTextContent();
									int intHeight = Integer.parseInt(strHeight);

									nlTemp = e.getElementsByTagName("size");
									nTemp = nlTemp.item(0);
									String strSize = nTemp.getTextContent();
									long longSize = Long.parseLong(strSize);

									nlTemp = e.getElementsByTagName("link");
									nTemp = nlTemp.item(0);
									String strLink = nTemp.getTextContent();
									
									AppSettings setting = AppSettings
											.getInstance(getApplicationContext());
									
									if (longSize != appVideoSize) {										
										infoList.add(strLink);
										totalVideoSize += longSize;		
										flagUpdate = true;
									}
									
									break;
								}
							}
						}
						
						MyLog.d(" ", "SPECIAL CASE = " + flagSpecial);
						
						if(flagSpecial == false){
							for (int i = 0; i < nodeList.getLength(); i++) {
								Node node = nodeList.item(i);
								Element e = (Element) node;
								NodeList nlTemp = e.getElementsByTagName("width");
								Node nTemp = nlTemp.item(0);
								String strWidth = nTemp.getTextContent();
								int intWidth = Integer.parseInt(strWidth);

								nlTemp = e.getElementsByTagName("height");
								nTemp = nlTemp.item(0);
								String strHeight = nTemp.getTextContent();
								int intHeight = Integer.parseInt(strHeight);

								nlTemp = e.getElementsByTagName("size");
								nTemp = nlTemp.item(0);
								String strSize = nTemp.getTextContent();
								long longSize = Long.parseLong(strSize);

								nlTemp = e.getElementsByTagName("link");
								nTemp = nlTemp.item(0);
								String strLink = nTemp.getTextContent();
								
								AppSettings setting = AppSettings
										.getInstance(getApplicationContext());
								
								boolean flagTempType_3_4 = false;
								if((float)intWidth/intHeight < 1.55){
									flagTempType_3_4 = true;
								}
								
								if(flagType_3_4 && flagTempType_3_4){
									if (longSize != appVideoSize) {										
										infoList.add(strLink);
										totalVideoSize += longSize;		
										flagUpdate = true;
									}
									
								} else if(!flagType_3_4 && !flagTempType_3_4){
									if (longSize != appVideoSize) {										
										infoList.add(strLink);
										totalVideoSize += longSize;	
										flagUpdate = true;
									}								
								}
								
							}
						}
						
						if (!flagUpdate) { // UPTODATE
							isDownloadingVideo = false;
							MyApplication.freezeTime = System
									.currentTimeMillis();

							if (flagLeftVideoDown) {
								showDialogMessage(getResources().getString(R.string.popup_msg_a) + " " + getResources().getString(R.string.popup_msg_b));
							} else if (flagFromButton){
								showDialogMessage(getResources().getString(R.string.msg_12a) + " " + getResources().getString(R.string.msg_12b));
							}
							
							flagFromButton = false;
							flagLeftVideoDown = false;	
							
							clearDownloadVideoResource();

							new Handler().postDelayed(new Runnable() {
								@Override
								public void run() {
									myDialogUpdateToc.dismiss();
								}
							}, 1000);
							
							
						} else { // UPDATE
							popupViewUpdateToc
								.setTotalDown((float) totalVideoSize / 1024 / 1024);
							popupViewUpdateToc.setTitleUpdate(getResources().getString(R.string.popup_uptoc_4a));
							popupViewUpdateToc
									.setPopupLayout(TouchPopupViewUpdateToc.LAYOUT_UPDATE_VIDEO);
							myDialogUpdateToc.setCanceledOnTouchOutside(false);
							myDialogUpdateToc.getWindow().setGravity(
									Gravity.CENTER);
							WindowManager.LayoutParams params = myDialogUpdateToc
									.getWindow().getAttributes();
							myDialogUpdateToc.getWindow().setAttributes(params);
							myDialogUpdateToc.show();
						}
					} else if (step == 2) {
						
						popupViewUpdateToc
								.setPopupLayout(TouchPopupViewUpdateToc.LAYOUT_PROCESSDATA);
						myDialogUpdateToc.setCanceledOnTouchOutside(false);
						myDialogUpdateToc.setCancelable(false);
						myDialogUpdateToc.getWindow()
								.setGravity(Gravity.CENTER);
						WindowManager.LayoutParams params = myDialogUpdateToc
								.getWindow().getAttributes();
						myDialogUpdateToc.getWindow().setAttributes(params);
						myDialogUpdateToc.show();			
						
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								try {										
									final Handler messageHandler = new Handler() {
										public void handleMessage(Message msg) {
											super.handleMessage(msg);
											isDownloadingVideo = false;
											MyApplication.freezeTime = System.currentTimeMillis();
											if (msg.what == 0) {
												clearDownloadVideoResource();

												if(myDialogUpdateToc != null){
													myDialogUpdateToc.dismiss();
												}
												
											} else {
												clearDownloadVideoResource();
											}
										}
									};
									
									if(!fileVideoPath.equals("")){
										
										File folderVideo = new File(videoDirPath);
										for (final File fileEntry : folderVideo.listFiles()) {
											if(fileEntry.getName().endsWith(".mp4") || fileEntry.getName().endsWith(".avi")){
												fileEntry.delete();
											}
										}
										
										File fTemp = new File(fileVideoPath);
										if(fTemp.exists()){
											File fFile = new File(fileVideoPath.replace(".o4d", ""));
											if(fFile.exists()){
												fFile.delete();
											}
											fTemp.renameTo(fFile);
										}
									}
								
									AppSettings setting = AppSettings
											.getInstance(getApplicationContext());
									setting.saveVideoLyricSize(totalVideoSize);
									
									MyLog.d("saveVideoLyricSize", totalVideoSize + "");			

									new Handler().postDelayed(new Runnable() {
										@Override
										public void run() {
											messageHandler.sendEmptyMessage(0);
										}
									}, 1000);
								} catch (Exception e) {
									isDownloadingVideo = false;
									MyApplication.freezeTime = System.currentTimeMillis();
									clearDownloadVideoResource();
								}
							}
						}, 500);
					}
				} catch (Exception e) {
					isDownloadingVideo = false;
					MyApplication.freezeTime = System.currentTimeMillis();
					clearDownloadVideoResource();
				}
			}
		});
	}
	
	private String fileVideoPath = ""; 
	public void downloadMultipleVideoFile() {
		try {
			String appRootPath = videoDirPath;
			for (int i = 0; i < infoList.size(); i++) {
				String txtFileName = infoList.get(i);
				String url = "https://kos.soncamedia.com/firmware/karconnect/video/"
						+ txtFileName;
				String dest_file_path = appRootPath.concat("/" + txtFileName + ".o4d");

				fileVideoPath = dest_file_path;

				File dest_file = new File(dest_file_path);
				URL u = new URL(url);
				URLConnection conn = u.openConnection();
				final int contentLength = conn.getContentLength();
				DataInputStream fis = new DataInputStream(u.openStream());
				DataOutputStream fos = new DataOutputStream(
						new FileOutputStream(dest_file));

				int bytes_read = 0;
				int bytesReadTotal = 0;
				int buffer_size = 1024 * 1024;
				byte[] buffer = new byte[buffer_size];

				while ((bytes_read = fis.read(buffer, 0, buffer_size)) > 0) {
					if (!isDownloadingVideo) {
						progressDialog.dismiss();
						fis.close();
						fos.flush();
						fos.close();
						return;
					}
					fos.write(buffer, 0, bytes_read);
					bytesReadTotal += bytes_read;
					final int progress = (int) (100.0f * bytesReadTotal / contentLength);

					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (progress == 0) {
								popupPercent.setTotalSize(contentLength);
							}
							popupPercent.setDownloadPercent(progress);
						}
					});
				}

				fis.close();
				fos.flush();
				fos.close();
			}
			hideProgressVideoIndicator(2);
		} catch (Exception e) {
			isDownloadingVideo = false;
			MyApplication.freezeTime = System.currentTimeMillis();
			clearDownloadVideoResource();
			return;
		}
	}
	
	// -----------------
	private void processGetScoreInfo() {
		((MyApplication) getApplication()).getScoreInfo();
		((MyApplication) getApplication())
				.setOnReceiverScoreInfoListener(new OnReceiverScoreInfoListener() {

					@Override
					public void OnReceiverScoreInfo(List<Integer> listScore) {
						MyLog.e(TAB, "OnReceiverScoreInfo");
						if (listScore != null && listScore.size() > 1) {
							MyLog.d(" ", "HAVE SCORE");

							int scoreFromModel = listScore.get(0); // same with svrModel

							for (Integer score : listScore) {
								MyLog.d(" ", score + "");
							}
						} else {
							MyLog.d(" ", "NO SCORE");
						}
					}
				});
	}
	
	// -----------------
	private void groupView_StopTimerAutoConnect(){
		runOnUiThread(new Runnable() {
			public void run() {
				viewDevice.StopTimerAutoConnect();
				
				if(colorLyricListener != null && boolShowKaraoke){
					colorLyricListener.OnMain_StopTimerAutoConnect();
				}
			}
		});
		
	}
	
	private void groupView_StartTimerAutoConnect(){
		runOnUiThread(new Runnable() {
			public void run() {
				viewDevice.StartTimerAutoConnect();
				
				if(colorLyricListener != null && boolShowKaraoke){
					colorLyricListener.OnMain_StartTimerAutoConnect();
				}
			}
		});
		
	}
	
	private DialogFlower dialogFlower = null;
	private void showDialogFlower(){		
		if(serverStatus == null){
			showDialogConnect();
			return;
		}
		
		if(serverStatus.getPlayingSongID() == 0){			
			showDialogMessage(getResources().getString(R.string.msg_tanghoa_2a) + "." + getResources().getString(R.string.msg_tanghoa_2b));
			return;
		}
		
		if(MyApplication.flagPlayingYouTube){
			showDialogMessage(getResources().getString(R.string.msg_tanghoa_3a) + "." + getResources().getString(R.string.msg_tanghoa_3b));
			return;
		}		
		
		if(dialogFlower == null){
			SKServer skServer = ((MyApplication) getApplication()).getDeviceCurrent();
			dialogFlower = new DialogFlower(this, context, getWindow(), skServer.getName());
			dialogFlower.setOnBaseDialogListener(new OnBaseDialogListener() {
				
				@Override
				public void OnShowDialog() {}
				
				@Override
				public void OnFinishDialog() {
					dialogFlower = null;
				}
			});
			dialogFlower.setOnDialogFlowerListener(new OnDialogFlowerListener() {
				
				@Override
				public void OnFlower(String name, int avatar, int number) {
					MyLog.e(" ", " ");
					MyLog.e(TAB, "OnTangHoaInfo: name = " + name + " -- avatar = " + avatar + " -- numberFlower = " + number);
					
					((MyApplication) getApplication()).sendTangHoaData(name, avatar, number);
				}

				@Override
				public void OnDoXucxac() {
					((MyApplication) getApplication()).callXucXacCommand();					
				}

				@Override
				public void OnLuckyRoll() {
					((MyApplication) getApplication()).callLuckyRollCommand();
				}
			});
			dialogFlower.showDialog();
		}
	}
	
	private String tempGroupViewSongName = "";
	public String getCurrentSongName(){
		return tempGroupViewSongName;
	}
	
	// ----------------- PROCESS JOIN LYRIC

	private List<LyricData> listJoinLyric_Main;
	private List<LyricData> listJoinLyric_Sub;

	private void processJoinLyric2() {
		MyLog.e(" ", " ");
		MyLog.e(" ", " ");
		MyLog.e(" ", "processJoinLyric VERY START.................");
		String rootPath = android.os.Environment.getExternalStorageDirectory()
				.toString().concat("/testJoinLyric/Format moi");

		String mainLyricPath = rootPath
				.concat("/LYRICS_1457616480-english-all_mediatype.zip");
		String subLyricPath = rootPath
				.concat("/LYRICS_1457616480-chinese-all_mediatype.zip");

		// check file
		File fMainLyric = new File(mainLyricPath);
		if (!fMainLyric.exists()) {
			return;
		}

		File fSubLyric = new File(subLyricPath);
		if (!fSubLyric.exists()) {
			return;
		}

		String formatMainLyric = "";
		String formatSubLyric = "";

		long tgian = System.currentTimeMillis();

		RandomAccessFile accessFile = null;
		listJoinLyric_Main = new ArrayList<LyricData>();
		listJoinLyric_Sub = new ArrayList<LyricData>();

		// -------------------------------------------------------------------------
		// STEP 1 - CREATE MAIN LIST FROM FILE
		MyLog.d("STEP 1",
				"CREATE MAIN LIST FROM FILE -- "
						+ (System.currentTimeMillis() - tgian));
		try {
			accessFile = new RandomAccessFile(fMainLyric, "r");
			accessFile.seek(0);
			byte[] bytes = new byte[4];
			accessFile.read(bytes, 0, 4);
			formatMainLyric = new String(bytes);
			MyLog.e("fMainLyric", "FORMAT = " + formatMainLyric);
			accessFile.seek(4);
			accessFile.read(bytes, 0, 4);
			int sizesong = ConvertData.ByteToInt4(bytes);

			MyLog.e("fMainLyric", "sizesong = " + sizesong);

			accessFile.seek(8);
			accessFile.read(bytes, 0, 4);
			int pointerTable = ConvertData.ByteToInt4(bytes);

			MyLog.e("fMainLyric", "pointerTable = " + pointerTable);

			LyricData lyric = new LyricData();
			for (int i = 0; i < sizesong; i++) {
				accessFile.seek(pointerTable + i * LyricData.sizeof());
				accessFile.read(bytes, 0, 4);
				int id = ConvertData.ByteToInt4(bytes);

				lyric = new LyricData();
				// ---------//
				lyric.setIdSong(id);
				// ---------//
				accessFile.seek(4 + pointerTable + i * LyricData.sizeof());
				accessFile.read(bytes, 0, 1);
				lyric.setTypeABC(ConvertData.ByteToInt1(bytes));
				// ---------//
				accessFile.seek(4 + pointerTable + i * LyricData.sizeof());
				accessFile.read(bytes, 0, 4);
				lyric.setSongProperty(ConvertData.ByteToInt4(bytes));
				// ---------//
				accessFile.seek(8 + pointerTable + i * LyricData.sizeof());
				accessFile.read(bytes, 0, 4);
				lyric.setOffsetLyric(ConvertData.ByteToInt4(bytes));
				// ---------//
				accessFile.seek(12 + pointerTable + i * LyricData.sizeof());
				accessFile.read(bytes, 0, 4);
				lyric.setSizelyric(ConvertData.ByteToInt4(bytes));
				// ---------//
				accessFile.seek(lyric.getOffsetLyric());
				byte[] data = new byte[lyric.getSizelyric()];
				accessFile.read(data, 0, lyric.getSizelyric());
				lyric.setLyricData(data);
				listJoinLyric_Main.add(lyric);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (accessFile != null) {
				try {
					accessFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		// -------------------------------------------------------------------------
		// STEP 2 - CREATE SUB LIST FROM FILE
		MyLog.d("STEP 2",
				"CREATE SUB LIST FROM FILE -- "
						+ (System.currentTimeMillis() - tgian));
		try {
			accessFile = new RandomAccessFile(fSubLyric, "r");
			accessFile.seek(0);
			byte[] bytes = new byte[4];
			accessFile.read(bytes, 0, 4);
			formatSubLyric = new String(bytes);
			MyLog.e("fSubLyric", "FORMAT = " + formatSubLyric);
			accessFile.seek(4);
			accessFile.read(bytes, 0, 4);
			int sizesong = ConvertData.ByteToInt4(bytes);

			MyLog.e("fSubLyric", "sizesong = " + sizesong);

			accessFile.seek(8);
			accessFile.read(bytes, 0, 4);
			int pointerTable = ConvertData.ByteToInt4(bytes);

			MyLog.e("fSubLyric", "pointerTable = " + pointerTable);

			LyricData lyric = new LyricData();
			for (int i = 0; i < sizesong; i++) {
				accessFile.seek(pointerTable + i * LyricData.sizeof());
				accessFile.read(bytes, 0, 4);
				int id = ConvertData.ByteToInt4(bytes);

				lyric = new LyricData();
				// ---------//
				lyric.setIdSong(id);
				// ---------//
				accessFile.seek(4 + pointerTable + i * LyricData.sizeof());
				accessFile.read(bytes, 0, 1);
				lyric.setTypeABC(ConvertData.ByteToInt1(bytes));
				// ---------//
				accessFile.seek(4 + pointerTable + i * LyricData.sizeof());
				accessFile.read(bytes, 0, 4);
				lyric.setSongProperty(ConvertData.ByteToInt4(bytes));
				// ---------//
				accessFile.seek(8 + pointerTable + i * LyricData.sizeof());
				accessFile.read(bytes, 0, 4);
				lyric.setOffsetLyric(ConvertData.ByteToInt4(bytes));
				// ---------//
				accessFile.seek(12 + pointerTable + i * LyricData.sizeof());
				accessFile.read(bytes, 0, 4);
				lyric.setSizelyric(ConvertData.ByteToInt4(bytes));
				// ---------//
				accessFile.seek(lyric.getOffsetLyric());
				byte[] data = new byte[lyric.getSizelyric()];
				accessFile.read(data, 0, lyric.getSizelyric());
				lyric.setLyricData(data);
				// ---------//
				listJoinLyric_Sub.add(lyric);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (accessFile != null) {
				try {
					accessFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		// -------------------------------------------------------------------------
		// STEP 3 - CREATE NEW LIST
		MyLog.d("STEP 3", "CREATE NEW LIST -- "
				+ (System.currentTimeMillis() - tgian));

		List<LyricData> listJoinLyric_New = new ArrayList<LyricData>();
		if (formatMainLyric.equalsIgnoreCase(formatSubLyric)) { // same format

			listJoinLyric_New = listJoinLyric_Sub;
			LyricData tempLyric = null;

			for (int i = 0; i < listJoinLyric_Main.size(); i++) {
				tempLyric = listJoinLyric_Main.get(i);

				if (listJoinLyric_Sub.contains(tempLyric)) {
					continue;
				}

				listJoinLyric_New.add(tempLyric);
			}

		}

		Collections.sort(listJoinLyric_New, new Comparator<LyricData>() {

			@Override
			public int compare(LyricData o1, LyricData o2) {

				if (o2.getIdSong() > o1.getIdSong()) {
					return -1;
				} else if (o2.getIdSong() < o1.getIdSong()) {
					return 1;
				} else {
					if (o2.getTypeABC() > o1.getTypeABC()) {
						return -1;
					} else if (o2.getTypeABC() < o1.getTypeABC()) {
						return 1;
					} else {
						return 0;
					}

				}

			}

		});

		MyLog.e(" ", "File size -- new = " + listJoinLyric_New.size());

		// -------------------------------------------------------------------------
		// STEP 4 - CREATE NEW FILE
		MyLog.d("STEP 4", "CREATE NEW FILE -- "
				+ (System.currentTimeMillis() - tgian));

		if (listJoinLyric_New.size() > 0) {
			String newLyricPath = rootPath.concat("/aaa.zip");
			createFileLyric(listJoinLyric_New, formatMainLyric, newLyricPath);
		}

		// -------------------------------------------------------------------------
		// STEP 5 - CHECK NEW JOIN FILE
		MyLog.d("STEP 5",
				"CHECK NEW JOIN FILE -- "
						+ (System.currentTimeMillis() - tgian));

		mainLyricPath = rootPath.concat("/aaa.zip");

		// check file
		fMainLyric = new File(mainLyricPath);
		listJoinLyric_Main.clear();

		try {
			accessFile = new RandomAccessFile(fMainLyric, "r");
			accessFile.seek(0);
			byte[] bytes = new byte[4];
			accessFile.read(bytes, 0, 4);
			formatMainLyric = new String(bytes);
			MyLog.e("fMainLyric", "FORMAT = " + formatMainLyric);
			accessFile.seek(4);
			accessFile.read(bytes, 0, 4);
			int sizesong = ConvertData.ByteToInt4(bytes);

			MyLog.e("fMainLyric", "sizesong = " + sizesong);

			accessFile.seek(8);
			accessFile.read(bytes, 0, 4);
			int pointerTable = ConvertData.ByteToInt4(bytes);

			MyLog.e("fMainLyric", "pointerTable = " + pointerTable);

			LyricData lyric = new LyricData();
			for (int i = 0; i < sizesong; i++) {
				accessFile.seek(pointerTable + i * LyricData.sizeof());
				accessFile.read(bytes, 0, 4);
				int id = ConvertData.ByteToInt4(bytes);

				lyric = new LyricData();
				// ---------//
				lyric.setIdSong(id);
				// ---------//
				accessFile.seek(4 + pointerTable + i * LyricData.sizeof());
				accessFile.read(bytes, 0, 1);
				lyric.setTypeABC(ConvertData.ByteToInt1(bytes));
				// ---------//
				accessFile.seek(4 + pointerTable + i * LyricData.sizeof());
				accessFile.read(bytes, 0, 4);
				lyric.setSongProperty(ConvertData.ByteToInt4(bytes));
				// ---------//
				accessFile.seek(8 + pointerTable + i * LyricData.sizeof());
				accessFile.read(bytes, 0, 4);
				lyric.setOffsetLyric(ConvertData.ByteToInt4(bytes));
				// ---------//
				accessFile.seek(12 + pointerTable + i * LyricData.sizeof());
				accessFile.read(bytes, 0, 4);
				lyric.setSizelyric(ConvertData.ByteToInt4(bytes));
				// MyLog.d(" ", "OffsetLyric = " + lyric.getOffsetLyric() +
				// " -- Sizelyric = " + lyric.getSizelyric());
				// ---------//
				listJoinLyric_Main.add(lyric);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (accessFile != null) {
				try {
					accessFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		MyLog.e(" ",
				"processJoinLyric VERY END................. -- "
						+ (System.currentTimeMillis() - tgian));
		MyLog.e(" ", " ");
		MyLog.e(" ", " ");
	}

	private void createFileLyric(List<LyricData> listData, String formatLyric,
			String lyricPath) {
		MyLog.e(" ", "createFileLyric -- listData size = " + listData.size());

		File fLyric = new File(lyricPath);
		if (!fLyric.exists()) {
			try {
				fLyric.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			fLyric.delete();
			try {
				fLyric.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// STEP 1 - Header
		byte[] headerPart = new byte[28];

		byte[] bFormatLyric = formatLyric.getBytes();
		headerPart[0] = bFormatLyric[0];
		headerPart[1] = bFormatLyric[1];
		headerPart[2] = bFormatLyric[2];
		headerPart[3] = bFormatLyric[3];

		int totalLyric = listData.size();
		ByteUtils.intToBytesL(headerPart, 4, totalLyric);

		int pointerTable = 28;
		ByteUtils.intToBytesL(headerPart, 8, pointerTable);

		RandomAccessFile accessFile = null;
		try {
			accessFile = new RandomAccessFile(fLyric, "rw");
			accessFile.seek(accessFile.length());
			accessFile.write(headerPart);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (accessFile != null) {
				try {
					accessFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		// STEP 2 - LOOP LYRIC INFO
		byte[] bLyricInfo = null;
		int countStartOffsetLyricData = 28 + LyricData.sizeof() * totalLyric;

		for (int i = 0; i < listData.size(); i++) {
			LyricData tempLyric = listData.get(i);
			bLyricInfo = new byte[16];

			ByteUtils.intToBytesL(bLyricInfo, 0, tempLyric.getIdSong());
			ByteUtils.intToBytesL(bLyricInfo, 4, tempLyric.getSongProperty());
			ByteUtils.intToBytesL(bLyricInfo, 8, countStartOffsetLyricData);
			ByteUtils.intToBytesL(bLyricInfo, 12, tempLyric.getSizelyric());

			try {
				accessFile = new RandomAccessFile(fLyric, "rw");
				accessFile.seek(accessFile.length());
				accessFile.write(bLyricInfo);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (accessFile != null) {
					try {
						accessFile.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}

			countStartOffsetLyricData += tempLyric.getSizelyric();
		}

		// STEP 3 - LOOP LYRIC DATA
		byte[] dataInsert = null;
		for (int i = 0; i < listData.size(); i++) {
			LyricData tempLyric = listData.get(i);
			dataInsert = tempLyric.getLyricData();

			try {
				accessFile = new RandomAccessFile(fLyric, "rw");
				accessFile.seek(accessFile.length());
				accessFile.write(dataInsert);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (accessFile != null) {
					try {
						accessFile.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

			}

		}

	}
	
	//---------------------------------------
	private long checkAllFromServer_total = 0;
	private boolean flagFromButton = false;

	public void onUpdateAllFromServer_Button() {
		flagFromButton = true;
		onUpdateAllFromServer();
	}

	public void onUpdateAllFromServer() {
		MyLog.e(TAB, "onUpdateAllFromServer --- ");

		MyApplication.flagProcessCheckAll = true;
		checkAllFromServer_total = 0;

		onUpdateTocServer(1, 0);

	}

	private void startUpdateAllFromServer() {
		MyLog.e(" ", " ");
		MyLog.e(" ", " ");
		MyLog.e(TAB, "startUpdateAllFromServer ----");
		MyLog.e(" ", "totalSize = "
				+ ((float) checkAllFromServer_total / 1024 / 1024));

		MyApplication.flagProcessCheckAll = false;

		if (myDialogUpdatePic != null) {
			myDialogUpdatePic.dismiss();
		}
		if (myDialogUpdateToc != null) {
			myDialogUpdateToc.dismiss();
		}

		clearDownloadTocResource();
		clearDownloadPicResource();

		if (checkAllFromServer_total > 0) { // NEED UPDATE

			checkAllFromServer_total = 0;

			myDialogUpdatePic = new Dialog(this);
			myDialogUpdatePic.requestWindowFeature(Window.FEATURE_NO_TITLE);
			myDialogUpdatePic.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));
			myDialogUpdatePic.setContentView(R.layout.touch_item_popup_update_pic);
			myDialogUpdatePic.setCanceledOnTouchOutside(true);

			WindowManager.LayoutParams pa = myDialogUpdatePic.getWindow()
					.getAttributes();
			pa.height = WindowManager.LayoutParams.MATCH_PARENT;
			pa.width = WindowManager.LayoutParams.MATCH_PARENT;
			pa.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
					| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
					| WindowManager.LayoutParams.FLAG_FULLSCREEN;

			popupViewUpadtePic = (TouchPopupViewUpdatePic) myDialogUpdatePic
					.findViewById(R.id.myPopupUpdatePic);
			popupViewUpadtePic
					.setPopupLayout(TouchPopupViewUpdatePic.LAYOUT_UPDATE_ALL);
			popupViewUpadtePic
					.setOnPopupUpdatePicListener(new OnPopupUpdatePicListener() {

						@Override
						public void OnUpdatePicYes() {
							isDownloadingDauMay = false;
							isDownloadingLyric = false;
							isDownloadingPic = false;
							isDownloadingToc = false;
							isDownloadingVideo = false;
							
							if (myDialogUpdateToc != null) {
								myDialogUpdateToc.dismiss();
							}
							myDialogUpdatePic.dismiss();
							onUpdateTocServer(1, 0);
						}

						@Override
						public void OnUpdatePicNo() {
							isDownloadingDauMay = false;
							isDownloadingLyric = false;
							isDownloadingPic = false;
							isDownloadingToc = false;
							isDownloadingVideo = false;
							
							if (myDialogUpdateToc != null) {
								myDialogUpdateToc.dismiss();
							}
							myDialogUpdatePic.dismiss();
							onUpdateVideoLyric(1);
						}
					});

			myDialogUpdatePic.setCanceledOnTouchOutside(false);
			myDialogUpdatePic.getWindow().setGravity(Gravity.CENTER);
			WindowManager.LayoutParams params = myDialogUpdatePic.getWindow()
					.getAttributes();
			myDialogUpdatePic.getWindow().setAttributes(params);
			myDialogUpdatePic.show();

		} else { // NO NEED UPDATE
			onUpdateVideoLyric(1);

		}

	}
	
	// -----------------------------------
	public XoaVideoView xoaVideoView;

	public void showMyPopupAsking(int typeAsk, final Song song, String msg1, String msg2){
		hideshowDialogConnect();
		dialogConnect = null;
		
		if(typeAsk == 0){ // XOA CLIP
			if (dialogConnect == null) {
				dialogConnect = new TouchDialogConnect(getApplicationContext(),
						getWindow());
				dialogConnect.setDialogMessage(msg1, msg2);
				dialogConnect
						.setOnDialogConnectListener(new OnDialogConnectListener() {
							@Override
							public void OnYesListener() {
								dialogConnect = null;

								try {
									MyLog.d(TAB, "OnXoaVideo");
									String rootPath = android.os.Environment
											.getExternalStorageDirectory()
											.toString().concat("/SONCA");

									File parentFolder = new File(rootPath
											.concat("/VIDEO"));

									if (!parentFolder.exists()) {
										parentFolder.mkdirs();
									}

									for (File file : parentFolder.listFiles()) {
										file.delete();
									}

									AppSettings setting = AppSettings
											.getInstance(context);
									setting.saveVideoLyricSize(0);

									if (xoaVideoView != null) {
										if (checkVideoExist()) {
											xoaVideoView.setConnect(true);
										} else {
											xoaVideoView.setConnect(false);
										}
									}

								} catch (Exception e) {
									e.printStackTrace();
									if (xoaVideoView != null) {
										if (checkVideoExist()) {
											xoaVideoView.setConnect(true);
										} else {
											xoaVideoView.setConnect(false);
										}
									}
								}

							}

							@Override
							public void OnFinishListener() {
								dialogConnect = null;
							}
						});
				dialogConnect.showToast();
			}
			
		} else if(typeAsk == 1){ // XEM THU YOUTUBE
			if (dialogConnect == null) {
				dialogConnect = new TouchDialogConnect(getApplicationContext(),
						getWindow());
				dialogConnect.setDialogMessage(msg1, msg2);
				dialogConnect
						.setOnDialogConnectListener(new OnDialogConnectListener() {
							@Override
							public void OnYesListener() {							
								dialogConnect = null;
								previewYouTube(song);								
							}

							@Override
							public void OnFinishListener() {
								dialogConnect = null;
							}
						});
				dialogConnect.showToast();
			}
			
		} else if (typeAsk == 2){ // DOWN YOUTUBE
			if(dialogConnect == null){
				
				dialogConnect = new TouchDialogConnect(getApplicationContext(), getWindow());
				dialogConnect.setDialogMessage(msg1, msg2);
				dialogConnect.setOnDialogConnectListener(new OnDialogConnectListener() {
					@Override
					public void OnYesListener() {
						if(serverStatus != null){
							((MyApplication)getApplication()).sendDonwloadYouTubeCommand(song.getId(), 0);
						}
						
						if(MyApplication.flagDownloadYouTube && song.getId() != MyApplication.youtube_Download_ID){
							showDialogMessage(context.getString(R.string.youtube_down_7));
						}
						
						dialogConnect = null;						
						
					}
					
					@Override
					public void OnFinishListener() {
						dialogConnect = null;
					}
					
				});
				dialogConnect.showToast();
			}

		} else if (typeAsk == 3){ // REMOVE DOWN YOUTUBE
			if(dialogConnect == null){
				
				dialogConnect = new TouchDialogConnect(getApplicationContext(), getWindow());
				dialogConnect.setDialogMessage(msg1, msg2);
				dialogConnect.setOnDialogConnectListener(new OnDialogConnectListener() {
					@Override
					public void OnYesListener() {
						if(serverStatus != null){
							((MyApplication)getApplication()).sendDonwloadYouTubeCommand(song.getId(), 1);
						}
						dialogConnect = null;						
						
					}
					
					@Override
					public void OnFinishListener() {
						dialogConnect = null;
					}

				});
				dialogConnect.showToast();
			}

		} else if (typeAsk == 4){ // KHOI PHUC DU LIEU
			if(dialogConnect == null){
				
				dialogConnect = new TouchDialogConnect(getApplicationContext(), getWindow());
				dialogConnect.setDialogMessage(msg1, msg2);
				dialogConnect.setOnDialogConnectListener(new OnDialogConnectListener() {
					@Override
					public void OnYesListener() {
						if(dialogConnect != null){
							dialogConnect.hideToast();
						}
						
						dialogConnect = null;
						
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								processResetToPackageDB();
							}
						}, 200);						
						
					}
					
					@Override
					public void OnFinishListener() {
						dialogConnect = null;
					}
					
				});
				dialogConnect.showToast();
			}

		} else if (typeAsk == 5){ // SWITCH SCREEN
			if (dialogConnect == null) {
				dialogConnect = new TouchDialogConnect(getApplicationContext(),
						getWindow());
				dialogConnect.setDialogMessage(msg1, msg2);
				dialogConnect
						.setOnDialogConnectListener(new OnDialogConnectListener() {
							@Override
							public void OnYesListener() {							
								dialogConnect = null;
								AppSettings setting = AppSettings.getInstance(getApplicationContext());	
								int oldScreen = setting.getColorScreen();
								
								MyApplication.intColorScreen = switchColorScreen;
								
								if(oldScreen != MyApplication.intColorScreen){
									setting.saveColorScreen(MyApplication.intColorScreen);
									
									DefaultViewWhenDisConect();
									
									Intent mainIntent = null;
									if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE) {
										mainIntent = new Intent(KTVMainActivity.this, TouchMainActivity.class);
									}
									KTVMainActivity.this.startActivity(mainIntent);
									KTVMainActivity.this.finish();	
								}								
							}

							@Override
							public void OnFinishListener() {
								dialogConnect = null;
								if(dialogSettingApp != null){
									dialogSettingApp.updateScreen();
								}
							}
						});
				dialogConnect.showToast();
			} else if (typeAsk == 6){ // BAT TAT USER
				
				myDialogUpdatePic = new Dialog(this);
				myDialogUpdatePic.requestWindowFeature(Window.FEATURE_NO_TITLE);
				myDialogUpdatePic.getWindow().setBackgroundDrawable(
						new ColorDrawable(android.graphics.Color.TRANSPARENT));
				myDialogUpdatePic.setContentView(R.layout.touch_item_popup_update_pic);
				myDialogUpdatePic.setCanceledOnTouchOutside(true);

				WindowManager.LayoutParams pa = myDialogUpdatePic.getWindow()
						.getAttributes();
				pa.height = WindowManager.LayoutParams.MATCH_PARENT;
				pa.width = WindowManager.LayoutParams.MATCH_PARENT;
				pa.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
						| WindowManager.LayoutParams.FLAG_FULLSCREEN;

				popupViewUpadtePic = (TouchPopupViewUpdatePic) myDialogUpdatePic
						.findViewById(R.id.myPopupUpdatePic);
				popupViewUpadtePic
						.setPopupLayout(TouchPopupViewUpdatePic.LAYOUT_ONOFFUSER);
				popupViewUpadtePic
						.setOnPopupUpdatePicListener(new OnPopupUpdatePicListener() {

							@Override
							public void OnUpdatePicYes() {
								if (myDialogUpdateToc != null) {
									myDialogUpdateToc.dismiss();
								}
								myDialogUpdatePic.dismiss();
								if (drawerLayout != null) {
									drawerLayout.closeDrawers();
								}
								
								MyApplication.mSongTypeList = null;
								processTotalSongType(100);		

								new Handler().postDelayed(new Runnable() {
									@Override
									public void run() {
										setOnOffUserList(song.getName(), !MyApplication.bOffUserList);
									}
								}, 1500);	
							}

							@Override
							public void OnUpdatePicNo() {
								if (myDialogUpdateToc != null) {
									myDialogUpdateToc.dismiss();
								}
								myDialogUpdatePic.dismiss();
								if (drawerLayout != null) {
									drawerLayout.closeDrawers();
								}
							}
						});

				myDialogUpdatePic.setCanceledOnTouchOutside(false);
				myDialogUpdatePic.getWindow().setGravity(Gravity.CENTER);
				WindowManager.LayoutParams params = myDialogUpdatePic.getWindow()
						.getAttributes();
				myDialogUpdatePic.getWindow().setAttributes(params);
				myDialogUpdatePic.show();

			}
			
		}

	}

	private boolean checkVideoExist() {
		try {
			String rootPath = android.os.Environment
					.getExternalStorageDirectory().toString().concat("/SONCA");

			File parentFolder = new File(rootPath.concat("/VIDEO"));

			int fileCount = parentFolder.listFiles().length;
			if (fileCount == 0) {
				return false;
			}

			return true;
		} catch (Exception e) {
			return false;
		}

	}
	
	// ---------------------------------
	private void processGetLyricVidLink(){
		((MyApplication)getApplication()).getLyricVidLink();
		((MyApplication)getApplication()).setOnReceiverLyricVidLinkListener(new OnReceiverLyricVidLinkListener() {
			
			@Override
			public void OnReceiverLyricVidLink(String resultString) {
				MyLog.e(TAB, "RECEIVE VID LINK");
				MyLog.e(TAB, "resultString = " + resultString);
			}
		});	
	}
	
	// ---------------------------------
	private ArrayList<Song> listExcelSong;

	private boolean flagFinishReloadYoutube = true;
	private void reloadYoutubeTable() {
		flagFinishReloadYoutube = false;
		
		MyLog.e(TAB, "reloadYoutubeTable VERY START............");

		try {
			DBInterface.DBProcessNewYoutubeTable(context);

			String rootPath = "";
			if (android.os.Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED)) {
				rootPath = android.os.Environment
						.getExternalStorageDirectory()
						.toString()
						.concat(String.format("/%s/%s", "Android/data",
								getPackageName()));
				File appBundle = new File(rootPath);
				if (!appBundle.exists())
					appBundle.mkdir();
			}

			String ytPath = rootPath.concat("/YOUTUBE");
			File fYou = new File(ytPath);
			if (!fYou.exists()) {
				return;
			}

			listExcelSong = new ArrayList<Song>();
			processReadExcelFile(ytPath + "/" + MyApplication.YT_FILENAME);

			if(listExcelSong.size() > 0){
        		MyLog.e(" ", "listExcelSong size 1 = " + listExcelSong.size());
        		
        		ArrayList<Song> songExisted = DBInterface.DBSearchListSongID(listExcelSong, context);

    			if(songExisted.size() > 0){
    				ArrayList<Song> newList = new ArrayList<Song>();	 
    				
    				for (Song song : listExcelSong) {
    					if(songExisted.contains(song)){
    						MyLog.d("remove bot song trong youtube", song.getName() + " ");
    					} else {
    						newList.add(song);
    					}
    				}
    				
    				listExcelSong = newList;
    			}

				MyLog.e(" ", "listExcelSong size 2 = " + listExcelSong.size());
				
    			if(listExcelSong.size() > 0){
    				DBInterface.DBProcessInsertIntoYoutubeTable(context, listExcelSong);
    			}
        		
        	}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		flagFinishReloadYoutube = true;

		MyLog.e(TAB, "reloadYoutubeTable VERY END............");
	}

	private void processReadExcelFile(String filePath) {
		// MyLog.d(TAB, "processExcelFile VERY START..............");
		// MyLog.e(" ", "filePath = " + filePath);

		File fExcel = new File(filePath);
		if (!fExcel.exists()) {
			return;
		}

		// READ XLS
		try {
			FileInputStream myInput = new FileInputStream(fExcel);

			// Create a POIFSFileSystem object
			POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

			// Create a workbook using the File System
			HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

			// Get the first sheet from workbook
			HSSFSheet mySheet = myWorkBook.getSheetAt(0);

			/** We now need something to iterate through the cells. **/
			Iterator rowIter = mySheet.rowIterator();

			int countRow = 0;
			int countCol = 0;

			while (rowIter.hasNext()) {
				countRow++;
				HSSFRow myRow = (HSSFRow) rowIter.next();

				if (countRow > 1) { // skip first row
				// MyLog.e(" ", "Row " + countRow);
					Iterator cellIter = myRow.cellIterator();

					countCol = 0;
					Song tempSong = new Song();

					while (cellIter.hasNext()) {
						countCol++;

						HSSFCell myCell = (HSSFCell) cellIter.next();
						// MyLog.d(" ", "Cell " + countCol + " -- value = " +
						// myCell.toString());

						switch (countCol) {
						case 2:
							tempSong.setPlayLink(myCell.toString());
							break;
						case 3:
							tempSong.setDownLink(myCell.toString());
							break;
						case 4:
							tempSong.setIndex5(Integer.parseInt(myCell.toString().replace(".0", "")));
							break;
						case 5:
							tempSong.setId(Integer.parseInt(myCell.toString().replace(".0", "")));
							break;
						case 6:
							String name = myCell.toString();
							if(name.endsWith(".0")){
								name = name.substring(0, name.length() - 2);
							}
							tempSong.setName(name);

							String rawName = StringUtils.getRawString(name, LANG_INDEX.ALL_LANGUAGE);
							tempSong.setTitleRaw(rawName);
							
							tempSong.setShortName(StringUtils.getPinyinString(name, LANG_INDEX.ALL_LANGUAGE));

							break;
						case 11:
							String singer = myCell.toString();
							if(singer.endsWith(".0")){
								singer = singer.substring(0, singer.length() - 2);
							}
							tempSong.setSingerName(singer);							
							break;
						case 15:
							String theloai = myCell.toString();
							if(theloai.endsWith(".0")){
								theloai = theloai.substring(0, theloai.length() - 2);
							}
							tempSong.setTheloaiName(theloai);			
							break;
						case 20:
							tempSong.setLyric(myCell.toString());
							break;
						case 21:
							String strLang = myCell.toString().trim();
							int intLang = 0;
							if(strLang.contains("english")){
								intLang = 1;
							}
							tempSong.setLanguageID(intLang);
							break;
						case 24:
							tempSong.setRemix(Integer.parseInt(myCell.toString().replace(".0", "")) == 1 ? true: false);
							break;
						case 27:
//							tempSong.setShortName(myCell.toString());
							break;
						case 29:
							tempSong.setMediaType(MEDIA_TYPE.values()[Integer.parseInt(myCell.toString().replace(".0", ""))]);
							break;
						default:
							break;
						}

					}

					tempSong.setTypeABC(0);

					listExcelSong.add(tempSong);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			if(fExcel.exists()){
				fExcel.delete();
			}
			AppSettings settings = AppSettings.getInstance(getApplicationContext());
			settings.saveYouTubeVersion(0);
		}

		// MyLog.d(TAB, "processExcelFile VERY END..............");
	}
	
	// ---------- PREVIEW YOUTUBE
	
	private void previewYouTube(Song song) {

		Intent intent = new Intent(KTVMainActivity.this, YoutubePlayerActivity.class);
		Bundle b = new Bundle();
		b.putString("youtubePath", song.getPlayLink());
		intent.putExtras(b);
		startActivity(intent);

	}

	
	@Override
	public void OnUSBTOC() {
		showDialogAddSong();
	}
	
	private DialogAddSongData dialogAddSongData = null;
	private void showDialogAddSong(){
		if(serverStatus == null){
			return;
		}
		if(dialogAddSongData == null){
			dialogAddSongData = new DialogAddSongData(context, getWindow(), fragmentManager);
			dialogAddSongData.setOnBaseDialogListener(new OnBaseDialogListener() {
				
				@Override
				public void OnShowDialog() {
					
				}
				
				@Override
				public void OnFinishDialog() {
					dialogAddSongData = null;
				}
			});
			dialogAddSongData.setOnDialogAddSongListener(new OnDialogAddSongListener() {
				
				@Override
				public void OnDeleteSong(ArrayList<Song> list) {
					processRemoveSongToSmartK(list);
				}
				
				@Override
				public void OnAddSong(ArrayList<SongInfo> info) {
					processAddSongToSmartK(info);
				}

				@Override
				public void OnClickUSB(ItemUSB usb) {
					if(usb != null){
						requestDataUSBSK(usb.getUsbNumber() + "");
					}
				}

				@Override
				public void OnShowUSB() {
					requestUSBSKList();
				}
			});
			dialogAddSongData.showDialog();
		}
	}
	
	private void requestUSBSKList() {
		((MyApplication) getApplication()).getUSBStorageList();
		((MyApplication)getApplication()).setOnReceiverUSBStorageListListener(new OnReceiverUSBStorageListListener() {
			
			@Override
			public void OnReceiveStorageList(String resultString) {
				MyLog.e(TAB, "OnReceiveStorageList");
				MyLog.e(TAB, "resultString = " + resultString);
				
				ArrayList<ItemUSB> listResult = new ArrayList<ItemUSB>();
				
				if(!resultString.equals("")){
					String[] datas = resultString.split("==");
					for (int i = 0; i < datas.length; i++) {
						ItemUSB tempItem = new ItemUSB();
						String[] datas2 = datas[i].split("_");
						if(datas2.length == 2){
							tempItem.setUsbNumber(Integer.parseInt(datas2[0]));
							tempItem.setUsbName(datas2[1]);	
							
							listResult.add(tempItem);
						}
					}
					
				}	
				
				MyLog.e(TAB, "listResult size = " + listResult.size());
				if(listResult.size() > 0){
					if(dialogAddSongData != null){
						dialogAddSongData.sendListUSB(listResult);
					}
				} else {
					showDialogMessage(getResources().getString(R.string.usb_check_1));
				}
				
			}
		});
	}
	
	private void requestDataUSBSK(String idUSB){
		((MyApplication) getApplication()).getSongFromUSB(idUSB);
		((MyApplication)getApplication()).setOnReceiverSongFromUSBListener(new OnReceiverSongFromUSBListener() {
			
			@Override
			public void OnReceiveSongFromUSB(String resultString) {
				MyLog.e(TAB, "OnReceiveSongFromUSB");
				MyLog.e(TAB, "resultString = " + resultString);
				
				if(resultString != null){
					if(resultString.equals("OK")){
						processGetFileAddSong();	
					} else if(resultString.equals("WAIT")){
						showDialogMessageNoAutoHide(getResources().getString(R.string.usb_check_3a) + "\n" + getResources().getString(R.string.usb_check_3b));
					} 
								
				}
				
			}
		});
	}
	
	private void processAddSongToSmartK(ArrayList<SongInfo> listSong){
		if(listSong == null || listSong.size() == 0){
			showDialogMessage(getResources().getString(R.string.smartk_add_1));			
			return;
		}
		
		MyLog.e(TAB, "processAddSongToSmartK - size = " + listSong.size());
		
		int[] ids = new int[listSong.size()];
		for (int i = 0; i < listSong.size(); i++) {
			ids[i] = listSong.get(i).getId();
		}
		
		if(listSong.size() > 255){
			((MyApplication) getApplication()).requestModifySongListNew(0, ids);
		} else {
			((MyApplication) getApplication()).requestModifySongList(0, ids);	
		}	
		((MyApplication) getApplication()).setOnReceiverModifyTOCListener(new OnReceiverModifyTOCListener() {
			
			@Override
			public void OnReceiveModifyTOCResult(String resultString) {
				if(resultString != null && resultString.equals("OK")){
					deviceDidDisconnected();
					drawerLayout.closeDrawers();
					showDialogMessage(getResources().getString(R.string.toc_mod_add));
				}
			}
		});
	}
	
	private void processRemoveSongToSmartK(ArrayList<Song> listSong){
		if(listSong == null || listSong.size() == 0){
			showDialogMessage(getResources().getString(R.string.smartk_add_1));			
			return;
		}
		
		MyLog.e(TAB, "processRemoveSongToSmartK - size = " + listSong.size());
		
		int[] ids = new int[listSong.size()];
		for (int i = 0; i < listSong.size(); i++) {
			ids[i] = listSong.get(i).getId();
		}
		
		if(listSong.size() > 255){
			((MyApplication) getApplication()).requestModifySongListNew(1, ids);
		} else {
			((MyApplication) getApplication()).requestModifySongList(1, ids);	
		}	
		((MyApplication) getApplication()).setOnReceiverModifyTOCListener(new OnReceiverModifyTOCListener() {
			
			@Override
			public void OnReceiveModifyTOCResult(String resultString) {
				if(resultString != null && resultString.equals("OK")){
					deviceDidDisconnected();
					drawerLayout.closeDrawers();
					showDialogMessage(getResources().getString(R.string.toc_mod_remove));
				}
			}
		});

	}
	
	private void processGetFileAddSong(){
		DownloadState = -2;
		
		String rootPath = android.os.Environment
				.getExternalStorageDirectory()
				.toString()
				.concat(String.format("/%s/%s", "Android/data",
						getPackageName()));
		
		String savePath = rootPath.concat("/" + MyApplication.ADD_FILE);
		File updateFile = new File(savePath);
		if (updateFile.exists()) {
			updateFile.delete();
		}

		allowTimerPing = false;
		((MyApplication) getApplication()).downloadAddSongFile(savePath);
		isDownloadingDauMay = true;
	}
	
	private ArrayList<SongInfo> getListAddFromFile(){
		ArrayList<SongInfo> listResult = new ArrayList<SongInfo>();		
		MyLog.e(TAB, "getListAddFromFile VERY START");
		
		String rootPath = android.os.Environment
				.getExternalStorageDirectory()
				.toString()
				.concat(String.format("/%s/%s", "Android/data",
						getPackageName()));
		
		String filePath = rootPath.concat("/" + MyApplication.ADD_FILE);
		
		File fExcel = new File(filePath);
		if(!fExcel.exists()){
			return listResult;
		}
		
		// READ XLS
		try {
			FileInputStream myInput = new FileInputStream(fExcel);

			// Create a POIFSFileSystem object
			POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);

			// Create a workbook using the File System
			HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);

			// Get the first sheet from workbook
			HSSFSheet mySheet = myWorkBook.getSheetAt(0);

			// ** We now need something to iterate through the cells. **//*
			Iterator rowIter = mySheet.rowIterator();

			int countRow = 0;
			int countCol = 0;

			while (rowIter.hasNext()) {
				countRow++;
				HSSFRow myRow = (HSSFRow) rowIter.next();

				if (countRow > 1) { // skip first row
					// MyLog.e(" ", "Row " + countRow);
					Iterator cellIter = myRow.cellIterator();

					countCol = 0;
					SongInfo tempSong = new SongInfo();

					while (cellIter.hasNext()) {
						countCol++;

						HSSFCell myCell = (HSSFCell) cellIter.next();

						switch (countCol) {
						case 1:
							tempSong.setId(Integer.parseInt(myCell.toString().replace(".0", "")));
							break;
						case 2:
							String name = myCell.toString();
							if (name.endsWith(".0")) {
								name = name.substring(0, name.length() - 2);
							}
							tempSong.setTitle(name);
							break;
						case 3:
							tempSong.setAuthor(myCell.toString());
							break;
						case 4:
							tempSong.setSinger(myCell.toString());
							break;						
						default:
							break;
						}

					}

					listResult.add(tempSong);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			if (fExcel.exists()) {
				fExcel.delete();
			}
		} finally {

		}
		
		MyLog.e(TAB, "getListAddFromFile VERY END -- size = " + listResult.size());
		return listResult;
	}

	// ------------------------- TouchIBaseFragment - not use
	@Override
	public void onFirstClick(Song song, String typeFragment, int position,
			float x, float y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClickItem(Song song, String id, String typeFragment,
			String search, int state, float x, float y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPlaySong(Song song, String typeFragment, int position,
			float x, float y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDeleteSong(Song song, int position) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onMoveSong(ArrayList<Song> list) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnSingerLink(boolean bool, String nameSinger, int[] idSinger) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnNameSearch(String nameSinger, String typeFrag) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnClickFavourite() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnHideKeyBoard() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnShowLyric() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnShowReviewKaraoke(Song song) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPlayYouTube(Song song) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDownYouTube(Song song) {
		// TODO Auto-generated method stub
		
	}	
	
	@Override
	public void onClickYouTube(MyYouTubeInfo info, int type, int position,
			float x, float y) {
		// TODO Auto-generated method stub
		
	}
	// ------------------------- TouchIBaseFragment - not use


	// -----------------------------------------------
	@Override
	public void OnSettingAll() {
		showDialogSettingAll();
	}
	
	private DialogSettingAll dialogSettingAll = null;
	private void showDialogSettingAll(){
		if(dialogSettingAll != null){
			
		}
		
		if(dialogSettingAll == null){
			dialogSettingAll = new DialogSettingAll(context, getWindow(), this);
			dialogSettingAll.setOnDialogSettingAllListener(new OnDialogSettingAllListener() {
				
				@Override
				public void OnFinishListener() {
					dialogSettingAll = null;
				}

				@Override
				public void OnChangeSwitchState() {
					if(viewWifi != null){
						viewWifi.invalidate();
					}
					
				}
			});
						
			dialogSettingAll.showToast();
		}
	}
	
	private boolean isDownloadingDauMay = false;
	private Timer timerFreeze;
	
	class FreezeCheckTask extends TimerTask {

		@Override
		public void run() {
			MyLog.e("FreezeCheckTask", "..................................");	
			if(MyApplication.switchTime == 0){
				return;
			}
			if(MyApplication.flagOnWifiVideo == false){
				return;
			}
			if(serverStatus == null){
				return;
			}
//			if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && MyApplication.flagSmartK_801){
//				return;
//			}
			if(boolShowKaraoke){
				return;
			}		
			if(serverStatus.danceMode() == 1){
				return;
			}
			if(serverStatus.getPlayingSongID() == 0){
				return;
			}
			if(serverStatus.getCurrentTempo() != 0){
				return;
			}
			
			try {
				if(isProcessingSomething){
					MyApplication.freezeTime = System.currentTimeMillis();
					MyLog.e("", "isProcessing Something");
					return;
				}
				
				if(isDownloadingPic || isDownloadingToc || isDownloadingLyric || isDownloadingDauMay || isDownloadingVideo){
					MyApplication.freezeTime = System.currentTimeMillis();
					MyLog.e("", "isDownloading Something");
					MyLog.e("", "isDownloadingPic = " + isDownloadingPic);
					MyLog.e("", "isDownloadingToc = " + isDownloadingToc);
					MyLog.e("", "isDownloadingLyric = " + isDownloadingLyric);
					MyLog.e("", "isDownloadingDauMay = " + isDownloadingDauMay);
					MyLog.e("", "isDownloadingVideo = " + isDownloadingVideo);
					return;
				}
				
				if(!boolShowKaraoke){
					long curTime = System.currentTimeMillis();
					if((curTime - MyApplication.freezeTime) > (MyApplication.switchTime*1000)){
						MyLog.e(" ", "SWITCHY SWITCHY");
						runOnUiThread(new Runnable() {
							public void run() {
								final String appRootPath = android.os.Environment
										.getExternalStorageDirectory()
										.toString()
										.concat(String.format("/%s/%s", "Android/data",
												getPackageName()));
								
								File folderLyric = new File(
										appRootPath.concat("/COLORLYRIC"));
								if(folderLyric.listFiles().length > 0){
									showKaraoke();
								} else {
									//showDialogMessage(getResources().getString(R.string.msg_kara_3));
								}
							}
						});
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	private void startTimerFreeze() {
		stopTimerFreeze();
		MyLog.e("startTimerFreeze", ".....................");
		timerFreeze = new Timer();
		timerFreeze.schedule(new FreezeCheckTask(), 10000, 5000);
	}

	private void stopTimerFreeze() {
		MyLog.e("stopTimerFreeze", ".....................");
		if (timerFreeze != null) {
			timerFreeze.cancel();
			timerFreeze = null;
		}
	}
	
	
	private int idDownloadMidi = 0;
	public void callDownloadFileMidiFromPlayer(int id){
		if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
			idDownloadMidi = id;
			
			MyLog.e(" ", " ");MyLog.e(" ", " ");
			MyLog.e(TAB, "callDownloadFileMidiFromPlayer - id = " + id);
			MyLog.e(" ", " ");MyLog.e(" ", " ");
			
			DownloadState = -10;

			String rootPath = "";
			if (android.os.Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED)) {
				rootPath = android.os.Environment
						.getExternalStorageDirectory()
						.toString()
						.concat(String.format("/%s/%s", "Android/data",
								getPackageName()));
				File appBundle = new File(rootPath);
				if (!appBundle.exists())
					appBundle.mkdir();
			}
			
			String savePath = rootPath.concat("/" + MyApplication.MIDI_FILE);
			File updateFile = new File(savePath);
			if (updateFile.exists()) {
				updateFile.delete();
			}

			allowTimerPing = false;
			((MyApplication) getApplication()).downloadLyricMidiFile(savePath);
		}		
	}	
	
	
	
	
	
	
	
	
	
	
	
	
}

