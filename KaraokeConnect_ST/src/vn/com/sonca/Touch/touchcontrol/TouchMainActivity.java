package vn.com.sonca.Touch.touchcontrol;

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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.moonbelly.youtube.DeveloperKey;
import com.moonbelly.youtube.MyAppInfo;
import com.moonbelly.youtube.YoutubePlayerActivity;
import com.moonbelly.youtubeFrag.FragmentYouTube;
import com.moonbelly.youtubeFrag.MyYouTubeInfo;

import vn.com.sonca.AddDataSong.DialogAddSongData;
import vn.com.sonca.AddDataSong.DialogAddSongData.OnDialogAddSongListener;
import vn.com.sonca.ColorLyric.FragmentKaraoke;
import vn.com.sonca.ColorLyric.FragmentKaraoke.OnKaraokeFragmentListener;
import vn.com.sonca.ColorLyric.FragmentReviewKaraoke;
import vn.com.sonca.ColorLyric.FragmentReviewKaraoke.OnReviewKaraokeFragmentListener;
import vn.com.sonca.ColorLyric.LyricData;
import vn.com.sonca.Lyric.LoadCheckLyric;
import vn.com.sonca.Lyric.LoadCheckLyric.OnLoadCheckLyricListener;
import vn.com.sonca.Lyric.LoadCheckLyricNew;
import vn.com.sonca.Lyric.LoadLyRicFileServer;
import vn.com.sonca.Lyric.LoadLyRicFileServerNew;
import vn.com.sonca.Lyric.LoadLyric;
import vn.com.sonca.Lyric.StoreLyric;
import vn.com.sonca.Lyric.ToastBox;
import vn.com.sonca.Lyric.ToastBox.OnToastBoxListener;
import vn.com.sonca.Lyric.LoadLyRicFileServer.OnLoadLyRicFileServerListener;
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
import vn.com.sonca.Touch.BlockCommand.CommandBox.OnCommandBoxListener;
import vn.com.sonca.Touch.BlockCommand.DialogConfirm;
import vn.com.sonca.Touch.BlockCommand.DialogConfirm.OnDialogConfirmListener;
import vn.com.sonca.Touch.BlockCommand.DialogDownTOC;
import vn.com.sonca.Touch.BlockCommand.DialogDownTOC.OnDialogDownTOCListener;
import vn.com.sonca.Touch.BlockCommand.DialogNoAddPlayList;
import vn.com.sonca.Touch.BlockCommand.DialogNoAddPlayList.OnDialogNoAddPlayListListener;
import vn.com.sonca.Touch.Connect.Model;
import vn.com.sonca.Touch.Connect.TouchAdminFragment;
import vn.com.sonca.Touch.Connect.TouchAdminFragment.OnAdminFragmentListener;
import vn.com.sonca.Touch.Connect.TouchDeviceFragment;
import vn.com.sonca.Touch.Connect.TouchHelloFragment;
import vn.com.sonca.Touch.Connect.TouchHelloFragment.OnTouchHelloFragmentListener;
import vn.com.sonca.Touch.Connect.TouchListDeviceFragment;
import vn.com.sonca.Touch.Connect.TouchDeviceFragment.OnDeviceFragmentListener;
import vn.com.sonca.Touch.Connect.TouchListDeviceFragment.OnListDeviceFragmentListener;
import vn.com.sonca.Touch.Connect.TouchModelFragment;
import vn.com.sonca.Touch.Connect.TouchModelFragment.OnModelFragmentListener;
import vn.com.sonca.Touch.CustomView.GroupSearch;
import vn.com.sonca.Touch.CustomView.MyVideoView;
import vn.com.sonca.Touch.CustomView.MyVideoView.OnMyVideoViewListener;
import vn.com.sonca.Touch.CustomView.SliderMuteView;
import vn.com.sonca.Touch.CustomView.SliderMuteView.OnMuteListener;
import vn.com.sonca.Touch.CustomView.SliderView;
import vn.com.sonca.Touch.CustomView.SliderView.OnSliderListener;
import vn.com.sonca.Touch.CustomView.TouchAnimationView;
import vn.com.sonca.Touch.CustomView.TouchBaseTypeView;
import vn.com.sonca.Touch.CustomView.TouchBottomGroupView;
import vn.com.sonca.Touch.CustomView.TouchChangePerfectView;
import vn.com.sonca.Touch.CustomView.TouchChangePerfectView.OnChangePerfectListener;
import vn.com.sonca.Touch.CustomView.TouchDanceLinkView;
import vn.com.sonca.Touch.CustomView.TouchDanceView;
import vn.com.sonca.Touch.CustomView.TouchDefaultControlView;
import vn.com.sonca.Touch.CustomView.TouchDefaultControlView.OnDefaultListener;
import vn.com.sonca.Touch.CustomView.TouchDeleteSearchView;
import vn.com.sonca.Touch.CustomView.TouchDeleteSearchView.OnDeleteSearchListener;
import vn.com.sonca.Touch.CustomView.TouchDeviceAdmin;
import vn.com.sonca.Touch.CustomView.TouchDownloadView;
import vn.com.sonca.Touch.CustomView.TouchExitView;
import vn.com.sonca.Touch.CustomView.TouchExitView.OnExitListener;
import vn.com.sonca.Touch.CustomView.TouchFavouriteTypeView;
import vn.com.sonca.Touch.CustomView.TouchHotSongTypeView;
import vn.com.sonca.Touch.CustomView.TouchKeyView;
import vn.com.sonca.Touch.CustomView.TouchKeyboardView;
import vn.com.sonca.Touch.CustomView.TouchLangagueTypeView;
import vn.com.sonca.Touch.CustomView.TouchMelodyView;
import vn.com.sonca.Touch.CustomView.TouchMusicianTypeView;
import vn.com.sonca.Touch.CustomView.TouchMyDrawerLayout;
import vn.com.sonca.Touch.CustomView.TouchMyDrawerLayout.DrawerListener;
import vn.com.sonca.Touch.CustomView.TouchMyGroupView;
import vn.com.sonca.Touch.CustomView.TouchNextView;
import vn.com.sonca.Touch.CustomView.TouchPauseView;
import vn.com.sonca.Touch.CustomView.TouchPopupViewDownloadPercent;
import vn.com.sonca.Touch.CustomView.TouchPopupViewDownloadPercent.OnPopupDownloadPercentListener;
import vn.com.sonca.Touch.CustomView.TouchPopupViewExitApp;
import vn.com.sonca.Touch.CustomView.TouchPopupViewExitApp.OnPopupExitAppListener;
import vn.com.sonca.Touch.CustomView.TouchPopupViewUpdatePic;
import vn.com.sonca.Touch.CustomView.TouchPopupViewUpdatePic.OnPopupUpdateLyricListener;
import vn.com.sonca.Touch.CustomView.TouchPopupViewUpdatePic.OnPopupUpdatePicListener;
import vn.com.sonca.Touch.CustomView.TouchPopupViewUpdateToc;
import vn.com.sonca.Touch.CustomView.TouchPopupViewUpdateToc.OnPopupUpdateTocListener;
import vn.com.sonca.Touch.CustomView.TouchRemixTypeView;
import vn.com.sonca.Touch.CustomView.TouchRepeatView;
import vn.com.sonca.Touch.CustomView.TouchRepeatView.OnRepeatListener;
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
import vn.com.sonca.Touch.CustomView.TouchViewBack.OnBackListener;
import vn.com.sonca.Touch.CustomView.TouchVolumnView;
import vn.com.sonca.Touch.CustomView.TouchBaseTypeView.OnBaseTypeViewListener;
import vn.com.sonca.Touch.CustomView.TouchDanceLinkView.OnStopShowDanceListener;
import vn.com.sonca.Touch.CustomView.TouchDanceView.OnDancetListener;
import vn.com.sonca.Touch.CustomView.TouchDownloadView.OnClickListener;
import vn.com.sonca.Touch.CustomView.TouchKeyView.OnKeyListener;
import vn.com.sonca.Touch.CustomView.TouchMelodyView.OnMelodyListener;
import vn.com.sonca.Touch.CustomView.TouchMyDrawerLayout.PointerListener;
import vn.com.sonca.Touch.CustomView.TouchMyGroupView.OnMyGroupListener;
import vn.com.sonca.Touch.CustomView.TouchNextView.OnNextListener;
import vn.com.sonca.Touch.CustomView.TouchPauseView.OnPauseListener;
import vn.com.sonca.Touch.CustomView.TouchScoreView.OnScoreListener;
import vn.com.sonca.Touch.CustomView.TouchSearchView.OnSearchViewListener;
import vn.com.sonca.Touch.CustomView.TouchSingerLinkView.OnStopShowListener;
import vn.com.sonca.Touch.CustomView.TouchSingerView.OnSingerListener;
import vn.com.sonca.Touch.CustomView.TouchTempoView.OnTempoListener;
import vn.com.sonca.Touch.CustomView.TouchToneView.OnToneListener;
import vn.com.sonca.Touch.CustomView.TouchVolumnView.OnVolumnListener;
import vn.com.sonca.Touch.CustomView.ViewFlower;
import vn.com.sonca.Touch.CustomView.XoaVideoView;
import vn.com.sonca.Touch.CustomView.YouTubeTypeView;
import vn.com.sonca.Touch.Dance.TouchFragmentDance;
import vn.com.sonca.Touch.Dance.TouchFragmentDance.OnFragmentDanceListener;
import vn.com.sonca.Touch.Favourite.FavouriteStore;
import vn.com.sonca.Touch.Favourite.TouchFragmentFavourite;
import vn.com.sonca.Touch.Hi_W.DialogHiW;
import vn.com.sonca.Touch.Hi_W.DialogHiW.OnHiWListener;
import vn.com.sonca.Touch.Hi_W.DialogUpdateFirmwareFromServer;
import vn.com.sonca.Touch.Hi_W.DialogUpdateFirmwareFromServer.OnUpdateFirmwareFromServer;
import vn.com.sonca.Touch.Hi_W.Firmware;
import vn.com.sonca.Touch.Hi_W.HiW_FirmwareConfig;
import vn.com.sonca.Touch.Hi_W.HiW_FirmwareInfo;
import vn.com.sonca.Touch.Hi_W.LoadCheckUpdateFirmware;
import vn.com.sonca.Touch.Hi_W.LoadCheckUpdateFirmware.OnLoadCheckUpdateFirmwareListener;
import vn.com.sonca.Touch.Hi_W.SaveFirmware;
import vn.com.sonca.Touch.HotSong.FragmentHotSong;
import vn.com.sonca.Touch.Keyboard.GroupKeyBoard;
import vn.com.sonca.Touch.Keyboard.TouchKeyBoardFragment;
import vn.com.sonca.Touch.Keyboard.TouchKeyBoardFragment.OnKeyBoardFragmenttListener;
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
import vn.com.sonca.WebServer.MyHTTPService;
import vn.com.sonca.zktv.main.KTVMainActivity;
import vn.com.sonca.zzzzz.AutoConnect;
import vn.com.sonca.zzzzz.AutoConnect.OnConnectDeviceListener;
import vn.com.sonca.zzzzz.DeviceStote;
import vn.com.sonca.zzzzz.MyApplication;
import vn.com.sonca.zzzzz.MyApplication.OnReceiverHIW_FirmwareConfigListener;
import vn.com.sonca.zzzzz.MyApplication.OnReceiverHIW_FirmwareListener;
import vn.com.sonca.zzzzz.MyApplication.OnReceiverLyricVidLinkListener;
import vn.com.sonca.zzzzz.MyApplication.OnReceiverModifyTOCListener;
import vn.com.sonca.zzzzz.MyApplication.OnReceiverScoreInfoListener;
import vn.com.sonca.zzzzz.MyApplication.OnReceiverSongFromUSBListener;
import vn.com.sonca.zzzzz.MyApplication.OnReceiverUSBStorageListListener;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.zzzzz.MyApplication.OnApplicationListener;
import vn.com.sonca.zzzzz.MyApplication.OnReceiverAdminPassListener;
import vn.com.sonca.zzzzz.MyApplication.OnReceiverDeviceIsUserListener;
import vn.com.sonca.database.DBInstance;
import vn.com.sonca.database.DBInstance.SearchType;
import vn.com.sonca.database.DBInterface;
import vn.com.sonca.database.DbHelper;
import vn.com.sonca.database.DBInstance.SearchMode;
import vn.com.sonca.params.ByteUtils;
import vn.com.sonca.params.ConvertString;
import vn.com.sonca.params.Download90xxInfo;
import vn.com.sonca.params.ItemUSB;
import vn.com.sonca.params.Language;
import vn.com.sonca.params.LyricXML;
import vn.com.sonca.params.RealYouTubeInfo;
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
import vn.com.sonca.sk90xxHidden.DialogSK90xxHidden;
import vn.com.sonca.sk90xxHidden.DialogSK90xxHidden.OnDialogSK90xxHiddenListener;
import vn.com.sonca.sk90xxOnline.DialogSK90xxOnline;
import vn.com.sonca.sk90xxOnline.DialogSK90xxOnline.OnDialogSK90xxOnlineListener;
import vn.com.sonca.smartkaraoke.MapRemote;
import vn.com.sonca.smartkaraoke.NetworkSocket;
import vn.com.sonca.smartkaraoke.SmartKaraoke;
import vn.com.sonca.utils.AppConfig.LANG_INDEX;
import vn.com.sonca.utils.AppSettings;
import vn.com.sonca.utils.AppConfig.MEDIA_TYPE;
import vn.com.sonca.utils.StringUtils;
import vn.com.sonca.utils.XmlUtils;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.TextAppearanceSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import app.sonca.Dialog.ScoreLayout.BaseDialog.OnBaseDialogListener;
import app.sonca.flower.DialogFlower;
import app.sonca.flower.DialogFlower.OnDialogFlowerListener;
import app.sonca.flower.LuckRollView;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnShowListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.provider.Settings;

public class TouchMainActivity extends FragmentActivity implements
		TouchIBaseFragment, OnBaseTypeViewListener, OnMyGroupListener,
		OnDeviceFragmentListener, OnListDeviceFragmentListener,
		OnKeyBoardFragmenttListener, OnSearchViewListener,
		OnApplicationListener, OnFragmentDanceListener,
		OnTouchHelloFragmentListener , OnAdminFragmentListener,
		OnModelFragmentListener, OnLanguageFragmentListener,
		OnLoadCheckUpdateFirmwareListener, OnSelectListFragmentListener
		, OnKaraokeFragmentListener, OnReviewKaraokeFragmentListener {
	private String TAB = "MainActivity";
	private boolean isDestroyMainActivity = false;

	public static final String SONG = "SONG";
	public static final String SINGER = "SINGER";
	public static final String MUSICIAN = "MUSICIAN";
	public static final String SONGTYPE = "SONGTYPE";
	public static final String LANGUAGE = "LANGUAGE";
	public static final String FAVOURITE = "FAVOURITE";
	public static final String PLAYLIST = "PLAYLIST";
	public static final String SONGFOLLOW = "SONGFOLLOW";
	public static final String NOTHING = "NOTHING";
	public static final String REMIX = "REMIX";
	public static final String HOTSONG = "HOTSONG";
	public static final String NEWVOL = "NEWVOL";
	public static final String YOUTUBE = "YOUTUBE";
	public static String SAVETYPE = "";

	public static final String LIST_DEVICE = "LIST_DEVICE";
	public static final String ONE_DEVICE = "ONE_DEVICE";
	public static final String ONE_HELLO = "ONE_HELLO";
	public static final String ONE_ADMIN = "ONE_ADMIN";
	public static final String ONE_MODEL = "ONE_MODEL";
	public static final String SELECTLIST = "SELECTLIST";
	private String SAVEDEVICE = "";

	private FragmentManager fragmentManager;
	private TouchFragmentBase fragmentBase;

	private TouchSingerLinkView singerLinkView;
	private TouchDownloadView downloadView;
	private TouchAnimationView animationView;
	private TouchSongTypeView songView;
	private TouchSingerTypeView singerView;
	private TouchMusicianTypeView musicianView;
	private TouchSTypeTypeView typeView;
	private TouchHotSongTypeView hotSongTypeView;
//	private TouchLangagueTypeView langagueView;
	private TouchFavouriteTypeView favouriteView;
	private YouTubeTypeView youtubeView;
	private TouchRemixTypeView remixView;
	private TouchMyGroupView groupView;
	private TouchSearchView searchView;
	private TouchDeleteSearchView deleteSearchView;

	private RelativeLayout layoutMain;
	private LinearLayout layoutSingerLink;
	private LinearLayout layoutDownload;
	private LinearLayout layoutAnimation;
	private LinearLayout layoutConnect;
	private LinearLayout layoutDeleteSearchView;
	private TouchMyDrawerLayout drawerLayout;
	
	private RelativeLayout layoutColorLyric;

	// private NetworkSocket socket = null;
	// public SKServer connectedServer = null;
	public static ServerStatus serverStatus = null;

	public ServerStatus getServerStatus() {
		return this.serverStatus;
	}

	private TouchDanceView danceView;
	private TouchMelodyView melodyView;
	private TouchTempoView tempoView;
	private TouchKeyView keyView;
	private TouchVolumnView volumnView;
	private TouchSingerView btnSinger;
	private TouchToneView btnTone;
	private TouchScoreView btnScore;
	private MyVideoView btnVideo;
	private TouchPauseView pauseView;
	private TouchNextView nextView;
	private TouchRepeatView repeatView;
	private TouchDanceLinkView danceLinkView;
	private TouchViewBack viewBack;
//	private TouchExitView exitView;
	private TouchDefaultControlView defaultControlView;
	private TouchChangePerfectView changePerfectView;
	private ViewFlower viewFlower;
	
	public EditText editDong1;
	public EditText editDong2;
	public boolean flagRunHide = true;

	private OnMainKeyboardListener mainKeyboardListener;

	public interface OnMainKeyboardListener {
		public void OnTextShowing(String search);

		public void OnDrawerSlide();
		
		public void OnUpdateView();
	}

	private OnMainListener mainListener;

	public interface OnMainListener {
		public void OnLoadSucessful();

		public void OnUpdateImage();

		public void OnUpdateCommad(ServerStatus status);

		public void OnSearchMain(int state1, int state2, String search);

		public void OnSK90009();
		
		public void OnUpdateView();
		
		public void OnClosePopupYouTube(int position);
	}
	
	private OnToHelloListener onToHelloListener;
	public interface OnToHelloListener{
		public void OnUpdateCommad(ServerStatus status);
		public void OnUpdateView();
	}

	private OnClearTextListener textListener;

	public interface OnClearTextListener {
		public void OnClearText();
	}

	private OnListDeviceListener listDeviceListener;

	public interface OnListDeviceListener {
		public void OnDisplayProgressScan(boolean isScanning);
		public void OnUpdateView();
		public void OnShowListDevice();
	}

	private ToDeviveFragment deviveFragmentListener;

	public interface ToDeviveFragment {
		public void OnConnected();
		public void OnUpdateView();
	}

	private Context context;

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
	  
	private boolean isFirst = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		context = this.getApplicationContext();
		// MyApplication.bOffFirst = ((MyApplication)getApplication()).getCommandEnable();
		if(MyApplication.enableSamba)
		startService(new Intent(this, StreamService.class));//samba
		MyApplication.flagDance = false;
		
		SharedPreferences settingFlagConnect = getSharedPreferences(PREFS_FLAGHDD, 0);
		MyApplication.flagEverConnectHDD = settingFlagConnect.getBoolean("isEverConnect", false);
		MyApplication.flagEverConnect = settingFlagConnect.getBoolean("isEverConnect2", false);		
		
		DeviceStote deviceStote = new DeviceStote(context);
		MyApplication.bOffUserList = deviceStote.getShareRefresh();
		if (!MyApplication.flagEverConnect) {
			if (deviceStote.getListSaveDevice().size() > 0) {
				settingFlagConnect.edit()
						.putBoolean("isEverConnect2", true).commit();
				MyApplication.flagEverConnect = true;
			}
		}	
		
		AppSettings setting = AppSettings.getInstance(getApplicationContext());					
		MyApplication.intColorScreen = setting.getColorScreen();
		MyApplication.flagEverKBX9 = setting.isEverConnectKBX9();		
		MyApplication.flagRealKeyboard = setting.getRealKeyboard();		
		hideActionBar();
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 		
		
		setContentView(R.layout.touch_activity_main);
		
//		this.registerReceiver(this.mBatInfoReceiver, 
//			    new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		this.registerReceiver(this.mWifiInfoReceiver, 
			    new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
		
		findViewById(R.id.layoutNoDrawer).setOnClickListener(
				new View.OnClickListener() {	
				@Override public void onClick(View arg0) {}
		});
		
		layoutColorLyric = (RelativeLayout) findViewById(R.id.layoutColorLyric);
		layoutColorLyric.setVisibility(View.GONE);

		//----------------DIALOG SLIDER-------------------//
		myDialogSlider = new Dialog(this);
		myDialogSlider.requestWindowFeature(Window.FEATURE_NO_TITLE);
		myDialogSlider.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		myDialogSlider.setContentView(R.layout.item_popup_slider);
		myDialogSlider.setCanceledOnTouchOutside(true);

		WindowManager.LayoutParams myPa = myDialogSlider.getWindow()
				.getAttributes();
		myPa.flags =  WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE 
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_FULLSCREEN;
		
		myDialogSlider.setOnShowListener(new OnShowListener() {
			
			@Override
			public void onShow(DialogInterface dialog) {
				AlphaAnimation alpha = new AlphaAnimation(1F, 0.5F);
				alpha.setDuration(0);
				alpha.setFillAfter(true);
				layoutMain.startAnimation(alpha);
			}
		});
		
		myDialogSlider.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				AlphaAnimation alpha = new AlphaAnimation(0.5F, 1F);
				alpha.setDuration(0);
				alpha.setFillAfter(true);
				layoutMain.startAnimation(alpha);
				
				if(sliderView != null){
					sliderView.setVolumn(0);
				}
				
				if (SAVETYPE != NOTHING) {
					melodyView.setVisibility(View.VISIBLE);
					tempoView.setVisibility(View.VISIBLE);
					keyView.setVisibility(View.VISIBLE);
					volumnView.setVisibility(View.VISIBLE);
				} else {
					melodyView.setVisibility(View.INVISIBLE);
					tempoView.setVisibility(View.INVISIBLE);
					keyView.setVisibility(View.INVISIBLE);
					volumnView.setVisibility(View.VISIBLE);
				}
				if(timerHideDialogThanhTruot != null){
					timerHideDialogThanhTruot.cancel();
					timerHideDialogThanhTruot = null;
				}
			}
		});
						
		sliderView = (SliderView) myDialogSlider
				.findViewById(R.id.mySlider);
		muteView = (SliderMuteView) myDialogSlider.findViewById(R.id.myMute);
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)muteView.getLayoutParams();
		params.setMargins(-40 * getResources().getDisplayMetrics().widthPixels / 1980, 0, 0, 0);
		muteView.setLayoutParams(params);

		muteView.setOnMuteListener(new OnMuteListener() {

			@Override
			public void OnMuteSlider(boolean isMute) {
				boolean flag = muteView.getMute();
				if(MyApplication.intWifiRemote == MyApplication.SONCA){
					((MyApplication) getApplication()).sendCommand(
							NetworkSocket.REMOTE_CMD_MUTE, flag ? 0 : 1);
				}
				muteView.setMute(!flag);
			}
		});

		sliderView.setOnSliderListener(new OnSliderListener() {

			@Override
			public void OnSlider(int senderType, int value) {
				MyApplication.freezeTime = System.currentTimeMillis();
				hideDialogThanhTruot();
				// MyLog.e(TAB, "onSlider() receiver  value: " + senderType
				// + "  " + value);
				switch (senderType) {
				case SliderView.SENDER_MELODY:
					melodyView.setMelody(value);
					if(MyApplication.intWifiRemote == MyApplication.SONCA){
						((MyApplication) getApplication()).sendCommand(
								NetworkSocket.REMOTE_CMD_MELODY, value);
					}
					break;
				case SliderView.SENDER_TEMPO:
					tempoView.setTempo(value);
					if(MyApplication.intWifiRemote == MyApplication.SONCA){
						((MyApplication) getApplication()).sendCommand(
								NetworkSocket.REMOTE_CMD_TEMPO, value);
					}
					break;
				case SliderView.SENDER_KEY:
					keyView.setKey(value);
					if(MyApplication.intWifiRemote == MyApplication.SONCA){
						((MyApplication) getApplication()).sendCommand(
								NetworkSocket.REMOTE_CMD_KEY, value);
						}
					break;
				case SliderView.SENDER_VOLUMN:
					// if (!volumnView.getMute()) {
					// return;
					// }
					volumnView.setVolumn(value);
					if(MyApplication.intWifiRemote == MyApplication.SONCA){
						((MyApplication) getApplication()).sendCommand(
								NetworkSocket.REMOTE_CMD_VOLUME, value);
					}
					break;
				default:
					break;
				}
			}

			@Override
			public void OnSliderIncrease() {
				// not used anymore
			}

			@Override
			public void OnSliderDecrease() {
				// not used anymore
			}

		});
		
		
		// DBInterface.DBInitializeDatabase(this.getApplicationContext());
		
		if (myDialogUpdatePic == null) {
			myDialogUpdatePic = new Dialog(this);
			myDialogUpdatePic.requestWindowFeature(Window.FEATURE_NO_TITLE);
			myDialogUpdatePic.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));
			myDialogUpdatePic
					.setContentView(R.layout.touch_item_popup_update_pic);

			 WindowManager.LayoutParams pa = myDialogUpdatePic.getWindow().getAttributes();
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
					layoutMain.startAnimation(alpha);
				}
			});
			
			myDialogUpdatePic.setOnDismissListener(new OnDismissListener() {
				
				@Override
				public void onDismiss(DialogInterface dialog) {
					AlphaAnimation alpha = new AlphaAnimation(0.3F, 1F);
					alpha.setDuration(0);
					alpha.setFillAfter(true);
					layoutMain.startAnimation(alpha);
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
			
			belowLayout = (LinearLayout)myDialogUpdatePic.findViewById(R.id.belowPopupLayout);
			belowLayout.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(allowCancelOutside){
						myDialogUpdatePic.dismiss();	
					}					
				}
			});
		}
		
		isFirst = loadFirstTime();		
		
		saveSkipUpdate1 = lastUpdate1 = setting.loadServerLastUpdate(1);
		saveSkipUpdate2 = lastUpdate2 = setting.loadServerLastUpdate(2);
		saveSkipUpdate3 = lastUpdate3 = setting.loadServerLastUpdate(3);
		saveSkipUpdate4 = lastUpdate4 = setting.loadServerLastUpdate(4);
		
		/*
		 * //-----------------------------------------------------------------
		 * 
		 * AppSettings.getInstance(getApplicationContext()).saveUpdated(true);
		 * 
		 * /* if (!AppSettings.getInstance(getApplicationContext()).isUpdated())
		 * // * { Log.e("","import data"); String rootPath = ""; if
		 * (android.os.Environment.getExternalStorageState().equals(
		 * android.os.Environment.MEDIA_MOUNTED)){ rootPath =
		 * android.os.Environment.getExternalStorageDirectory().toString();
		 * rootPath = rootPath.concat(String.format("/%s/%s", "Android/data",
		 * "vn.com.sonca.TouchControl")); File appBundle = new File(rootPath);
		 * if(!appBundle.exists()) appBundle.mkdirs(); } String picDir =
		 * rootPath.concat("/PICTURE"); File picDirFile = new File(picDir);
		 * if(picDirFile.exists()) { picDirFile.delete(); } picDirFile.mkdirs();
		 * 
		 * String savePath1 = rootPath.concat("/MEGIDX"); String singerPath =
		 * rootPath.concat("/ARTIST"); String lyricPath =
		 * rootPath.concat("/LYRICS");
		 * 
		 * copyFromResource(R.raw.megidx, savePath1);
		 * copyFromResource(R.raw.lyrics, lyricPath);
		 * copyFromResource(R.raw.artist, singerPath);
		 * 
		 * SmartKaraoke importDB = new SmartKaraoke();
		 * importDB.importTocToDatabase
		 * (getDatabasePath(DbHelper.DBName).getAbsolutePath(), rootPath,
		 * picDir);
		 * 
		 * File updateFile = new File(savePath1); File singerFile = new
		 * File(singerPath); File lyricFile = new File(lyricPath);
		 * updateFile.delete(); singerFile.delete(); lyricFile.delete();
		 * 
		 * AppSettings.getInstance(getApplicationContext()).saveUpdated(true); }
		 */// -----------------------------------------------------------------

		int w = getResources().getDisplayMetrics().widthPixels;
		int h = getResources().getDisplayMetrics().heightPixels;
		MyLog.e("Main", w + " : " + h);
		MyLog.e("Main", "DP " + getResources().getDisplayMetrics().density);
		MyLog.e("Main", "DPI " + getResources().getDisplayMetrics().densityDpi);
		MyLog.e("Main", "XDPI " + getResources().getDisplayMetrics().xdpi);
		MyLog.e("Main", "YDPI " + getResources().getDisplayMetrics().ydpi);
		danceLinkView = (TouchDanceLinkView) findViewById(R.id.DanceLinkView);
		danceLinkView.setLayout(TouchDanceLinkView.NODANCE);
		danceLinkView.setOnStopShowDanceListener(new OnStopShowDanceListener() {
			@Override
			public void OnStop(boolean isActive, boolean layout) {
				findViewById(R.id.layoutLinkDance).setVisibility(View.GONE);
				if(MyApplication.intWifiRemote == MyApplication.SONCA){
					if (isActive) {
						if (layout == TouchDanceLinkView.NODANCE) {
							// MyLog.e(TAB, "DanceLinkView - NODANCE");
							((MyApplication) getApplication()).sendCommand(
									NetworkSocket.REMOTE_CMD_DANCE, 1);
						} else {
							// MyLog.e(TAB, "DanceLinkView - DANCE");
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
							((MyApplication) getApplication()).sendCommandKartrol(
									(byte) RemoteIRCode.IRC_ENTER, 0);
						}
					}, 400);
				}
			}
		});

		deleteSearchView = (TouchDeleteSearchView) findViewById(R.id.DeleteSearchView);
		deleteSearchView.setOnDeleteSearchListener(new OnDeleteSearchListener() {
					@Override
					public void OnDelete() {
						if(MyApplication.flagRealKeyboard){
							searchEditText.setText("");
							edSearchOldStr = "";	
						}	
						searchView.clearAllCharacterSearchView();
						layoutDeleteSearchView.setVisibility(View.INVISIBLE);
						if (mainKeyboardListener != null) {
							mainKeyboardListener.OnTextShowing("");
						}
						OnShowKeyBoard();
					}
				});
//		exitView = (TouchExitView) findViewById(R.id.ExitView);
//		exitView.setOnExitListener(new OnExitListener() {
//			@Override
//			public void OnClick() {
//				Intent intent = new Intent(TouchMainActivity.this,
//						KTVMainActivity.class);
//				startActivity(intent);
//				finish();
//			}
//		});

		danceView = (TouchDanceView) findViewById(R.id.DanceView);
		// danceView.setLayout(TouchDanceView.NODANCE);
		danceView.setOnDancetListener(new OnDancetListener() {
			@Override
			public void OnClick() {
				hideKeyBoard();
				findViewById(R.id.layoutLinkDance).setVisibility(View.VISIBLE);
				danceLinkView.ShowDanceLink();
			}

			@Override
			public void OnInActive() {
				hideKeyBoard();
				showDialogConnect();
			}
		});

		defaultControlView = (TouchDefaultControlView) findViewById(R.id.DefaultControlView);
		defaultControlView.setOnDefaultListener(new OnDefaultListener() {
			@Override
			public void onClick() {
				hideKeyBoard();
				if(MyApplication.intWifiRemote == MyApplication.SONCA){
					((MyApplication) getApplication()).sendCommand(
							NetworkSocket.REMOTE_CMD_DEFAULT, 0);
				}else{
					MyLog.e(TAB, "defaultControlView : send ok");
				}
				
//				changeToColorLyric();
				
//				if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
//					MyApplication.intColorScreen = MyApplication.SCREEN_BLUE;
//				} else {
//					MyApplication.intColorScreen = MyApplication.SCREEN_LIGHT;
//				}
//				
//				ChangeColorScreen();
			}

			@Override
			public void OnInActive() {
				hideKeyBoard();
				showDialogConnect();
				
//				changeToColorLyric();
				
//				if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
//					MyApplication.intColorScreen = MyApplication.SCREEN_BLUE;
//				} else {
//					MyApplication.intColorScreen = MyApplication.SCREEN_LIGHT;
//				}
//				
//				ChangeColorScreen();
			}
		});
		
		btnVideo = (MyVideoView) findViewById(R.id.VideoView);
		btnVideo.setOnMyVideoViewListener(new OnMyVideoViewListener() {
			
			@Override
			public void OnInActive() {
				hideKeyBoard();
				showDialogConnect();
			}
			
			@Override
			public void OnClick() {
				hideKeyBoard();

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
		});
		
		changePerfectView = (TouchChangePerfectView)findViewById(R.id.ChangePerfectView);
		changePerfectView.setOnChangePerfectListener(new OnChangePerfectListener() {
			
			@Override
			public void onClick() {
				hideKeyBoard();

				if(((MyApplication) TouchMainActivity.this.getApplication()).getListActive().size() == 99){
					return;
				}
				
				if(System.currentTimeMillis() - hoaAmTime <= 3000){
					return;
				}
					
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
						
						List<Integer> listIntType = ((MyApplication) TouchMainActivity.this.getApplication()).getTotalMIDIType(curIdSong, curName);
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
			
			@Override
			public void OnInActive() {
				hideKeyBoard();
				showDialogConnect();
			}
		});
		
		fragmentManager = getSupportFragmentManager();

		downloadView = (TouchDownloadView) findViewById(R.id.DownloadView);
		downloadView.setOnClickListener(new OnClickListener() {
			@Override
			public void OnStopClick() {
			}

			@Override
			public void OnExitClick() {
			}
		});
		
		viewBack = (TouchViewBack) findViewById(R.id.ViewBack);
		viewBack.setOnBackListener(new OnBackListener() {
			@Override
			public void OnBack() {
				OnBackFragment();
			}
		});
		viewFlower = (ViewFlower)findViewById(R.id.viewFlower);
		viewFlower.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MyLog.e(TAB, "viewFlower - onClick");
				showDialogFlower();
			}
		});
		
		if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
			viewFlower.setVisibility(View.VISIBLE);
		} else {
			viewFlower.setVisibility(View.GONE);			
		}
		
		drawerLayout = (TouchMyDrawerLayout) findViewById(R.id.drawer_layout);
		layoutMain = (RelativeLayout) findViewById(R.id.mainLayout);
		layoutSingerLink = (LinearLayout) findViewById(R.id.layoutSingerLink);
		layoutConnect = (LinearLayout) findViewById(R.id.layoutConnect);
		layoutDownload = (LinearLayout) findViewById(R.id.layoutDownload);
		layoutAnimation = (LinearLayout) findViewById(R.id.layoutAnimation);
		layoutAnimation.setVisibility(View.INVISIBLE);
		layoutDeleteSearchView = (LinearLayout) findViewById(R.id.LayoutDeleteSearchView);
		animationView = (TouchAnimationView) findViewById(R.id.AnimationView);
		downloadView = (TouchDownloadView) findViewById(R.id.DownloadView);
		layoutAnimation.setVisibility(View.GONE);
		layoutDownload.setVisibility(View.GONE);

		songView = (TouchSongTypeView) findViewById(R.id.imageSong);
		singerView = (TouchSingerTypeView) findViewById(R.id.imageSinger);
		musicianView = (TouchMusicianTypeView) findViewById(R.id.imageMusician);
		typeView = (TouchSTypeTypeView) findViewById(R.id.imageType);
		hotSongTypeView = (TouchHotSongTypeView)findViewById(R.id.imageHotSong);
		// langagueView = (TouchLangagueTypeView) findViewById(R.id.imageLanguage);
		favouriteView = (TouchFavouriteTypeView) findViewById(R.id.imageFavourite);
		remixView = (TouchRemixTypeView) findViewById(R.id.imageRmix);
		youtubeView = (YouTubeTypeView) findViewById(R.id.imageYouTube);
		searchView = (TouchSearchView) findViewById(R.id.searchView);
		searchView.setViewBack(viewBack);
		groupView = (TouchMyGroupView) findViewById(R.id.myGroupView);
		singerLinkView = (TouchSingerLinkView) findViewById(R.id.SingerLinkView);

		myGroupSearch = (GroupSearch) findViewById(R.id.groupSearch);
		fakeEditText = (EditText) this.findViewById(R.id.editFake);
		searchEditText = (EditText) this.findViewById(R.id.editTextSearch);
		
		searchEditText
		.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					OnCloseKeyBoard();
				} else {
					if(searchView.getActiveSearch() == false){
						searchView.setActiveSearch(true);	
					}
				}
			}
		});
		
		searchEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				setupMainWindowDisplayMode();
			}
		});
		
		searchEditText.addTextChangedListener(new TextWatcher() {
			public void afterTextChanged(Editable s) {
				String newStr = searchEditText.getText().toString();

				if (newStr.equalsIgnoreCase(edSearchOldStr)) {
					return;
				}
				
				if(flagStopSearch){
					flagStopSearch = false;
					return;
				}
				
				searchView.setFullSearch(newStr);
				if(searchView.getActiveSearch() == false && !newStr.isEmpty()){
					searchView.setActiveSearch(true);	
				}

			}

			public void beforeTextChanged(CharSequence s, int start,
					int count, int after) {
				String lastOldStr = edSearchOldStr;
				if(lastOldStr != null && !lastOldStr.isEmpty() && !lastOldStr.equals("") && lastOldStr.length() > 0){
					if(!s.equals("") && lastOldStr.length() - s.toString().length() > 1){
						return;
					}
				}
				edSearchOldStr = s.toString();
			}

			public void onTextChanged(CharSequence s, int start,
					int before, int count) {

			}
		});
		
		keyboard = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		
		
		melodyView = (TouchMelodyView) findViewById(R.id.MelodyView);
		// melodyView.setMelody(serverStatus.getCurrentMelody());
		melodyView.setOnMelodyListener(new OnMelodyListener() {
			@Override
			public void onMelody(int value) {
				hideKeyBoard();
				if(MyApplication.intWifiRemote == MyApplication.SONCA){
					((MyApplication) getApplication()).sendCommand(
							NetworkSocket.REMOTE_CMD_MELODY, value);
				}else{
					((MyApplication) getApplication()).sendCommandKartrol(
							(byte) RemoteIRCode.IRC_MELODY, 0);
				}
			}

			@Override
			public void OnInActive() {
				hideKeyBoard();
				showDialogConnect();
			}

			@Override
			public void OnShowTab(int intSliderVolumn) {
				muteView.setVisibility(View.GONE);

				melodyView.setVisibility(View.INVISIBLE);
				tempoView.setVisibility(View.INVISIBLE);
				keyView.setVisibility(View.INVISIBLE);
				volumnView.setVisibility(View.VISIBLE);

				sliderView.setSliderData(true, SliderView.SENDER_MELODY,
						SliderView.SLIDER_TYPE0, intSliderVolumn);
				
				myDialogSlider.getWindow().setGravity(
						Gravity.TOP | Gravity.LEFT);
				WindowManager.LayoutParams params = myDialogSlider
						.getWindow().getAttributes();
				params.x = (int) (5 * getResources().getDisplayMetrics().widthPixels / 1980);
				params.y = (int) (1.1 * getResources().getDisplayMetrics().heightPixels / 4);

				myDialogSlider.getWindow().setAttributes(params);
				myDialogSlider.show();
				hideDialogThanhTruot();
			}
		});

		tempoView = (TouchTempoView) findViewById(R.id.TempoView);
		// tempoView.setTempo(serverStatus.getCurrentTempo());
		tempoView.setOnTempoListener(new OnTempoListener() {
			@Override
			public void onTempo(int value) {
				hideKeyBoard();
				if(MyApplication.intWifiRemote == MyApplication.SONCA){
					((MyApplication) getApplication()).sendCommand(
							NetworkSocket.REMOTE_CMD_TEMPO, value);
				}else{
					MyLog.e(TAB, "tempoView : " + value);
					if (value > 0) {
						((MyApplication) getApplication()).sendCommandKartrol(
								(byte) RemoteIRCode.IRC_TEMPO_UP, 0);
					} else if (value < 0) {
						((MyApplication) getApplication()).sendCommandKartrol(
								(byte) RemoteIRCode.IRC_TEMPO_DOWN, 0);
					}	
				}
			}

			@Override
			public void OnInActive() {
				hideKeyBoard();
				showDialogConnect();
			}

			@Override
			public void OnShowTab(int intSliderVolumn) {				
				muteView.setVisibility(View.GONE);

				melodyView.setVisibility(View.INVISIBLE);
				tempoView.setVisibility(View.INVISIBLE);
				keyView.setVisibility(View.INVISIBLE);
				volumnView.setVisibility(View.VISIBLE);

				sliderView.setSliderData(true, SliderView.SENDER_TEMPO,
						SliderView.SLIDER_TYPE1, intSliderVolumn);

				myDialogSlider.getWindow().setGravity(
						Gravity.TOP | Gravity.LEFT);
				WindowManager.LayoutParams params = myDialogSlider
						.getWindow().getAttributes();
				params.x = (int) (5 * getResources().getDisplayMetrics().widthPixels / 1980);
				params.y = (int) (1.1 * getResources().getDisplayMetrics().heightPixels / 4);

				myDialogSlider.getWindow().setAttributes(params);
				myDialogSlider.show();
				hideDialogThanhTruot();
			}
		});

		keyView = (TouchKeyView) findViewById(R.id.KeyView);
		// keyView.setKey(serverStatus.getCurrentKey());
		keyView.setOnKeyListener(new OnKeyListener() {
			@Override
			public void onKey(int value) {
				hideKeyBoard();
				if(MyApplication.intWifiRemote == MyApplication.SONCA){
					((MyApplication) getApplication()).sendCommand(
							NetworkSocket.REMOTE_CMD_KEY, value);
				}else{
					MyLog.e(TAB, "keyView : " + value);
					if (value > 0) {
						((MyApplication) getApplication()).sendCommandKartrol(
								(byte) RemoteIRCode.IRC_KEY_UP, 0);
					} else if (value < 0) {
						((MyApplication) getApplication()).sendCommandKartrol(
								(byte) RemoteIRCode.IRC_KEY_DN, 0);
					}
				}
			}

			@Override
			public void OnInActive() {
				hideKeyBoard();
				showDialogConnect();
			}

			@Override
			public void OnShowTab(int intSliderVolumn) {				
				int mediaType = TouchMainActivity.serverStatus.getMediaType();
				if(mediaType != 0x07 && mediaType != -1){
					MEDIA_TYPE aType = MEDIA_TYPE.values()[mediaType];
					if(aType != MEDIA_TYPE.MIDI){
						return;
					}
				}
				
				muteView.setVisibility(View.GONE);

				melodyView.setVisibility(View.VISIBLE);
				tempoView.setVisibility(View.INVISIBLE);
				keyView.setVisibility(View.INVISIBLE);
				volumnView.setVisibility(View.INVISIBLE);

				sliderView.setSliderData(true,
						SliderView.SENDER_KEY, SliderView.SLIDER_TYPE1,
						intSliderVolumn);

				myDialogSlider.getWindow().setGravity(
						Gravity.TOP | Gravity.LEFT);
				WindowManager.LayoutParams params = myDialogSlider
						.getWindow().getAttributes();
				params.x = (int) (0 * getResources().getDisplayMetrics().widthPixels / 1980);
				params.y = (int) (1.75 * getResources().getDisplayMetrics().heightPixels / 4);

				myDialogSlider.getWindow().setAttributes(params);
				myDialogSlider.show();
				hideDialogThanhTruot();
			}
		});

		volumnView = (TouchVolumnView) findViewById(R.id.VolumnView);
		// volumnView.setVolumn(serverStatus.getCurrentVolume());
		volumnView.setOnVolumnListener(new OnVolumnListener() {
			@Override
			public void onVolumn(int value) {
				hideKeyBoard();
				if(MyApplication.intWifiRemote == MyApplication.SONCA){
//					((MyApplication) getApplication()).sendCommand(
//					NetworkSocket.REMOTE_CMD_VOL_DEFAULT, value);

					((MyApplication) getApplication()).sendCommand(
							NetworkSocket.REMOTE_CMD_VOLUME, value);
				}else{
					MyLog.e(TAB, "volumnView : " + value);
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
			public void onMute(boolean isMute) {
				hideKeyBoard();
				if(MyApplication.intWifiRemote == MyApplication.SONCA){
					((MyApplication) getApplication())
							.sendCommand(NetworkSocket.REMOTE_CMD_MUTE,
									(isMute == true) ? 0 : 1);
				}else{
					MyLog.e(TAB, "Mute : " + isMute);
					((MyApplication) getApplication()).sendCommandKartrol(
							(byte) RemoteIRCode.IRC_MUTE,
							0);
				}
			}

			@Override
			public void OnInActive() {
				hideKeyBoard();
				showDialogConnect();
			}

			@Override
			public void OnShowTab(int intSliderVolumn) {				
				muteView.setVisibility(View.VISIBLE);
				
				if(serverStatus != null){
					muteView.setMute(serverStatus.isMuted());
				}				

				if (SAVETYPE != NOTHING) {
					melodyView.setVisibility(View.VISIBLE);
				} else {
					melodyView.setVisibility(View.INVISIBLE);
				}
				tempoView.setVisibility(View.INVISIBLE);
				keyView.setVisibility(View.INVISIBLE);
				volumnView.setVisibility(View.INVISIBLE);

				sliderView.setSliderData(true,
						SliderView.SENDER_VOLUMN, SliderView.SLIDER_TYPE0,
						intSliderVolumn);

				myDialogSlider.getWindow().setGravity(
						Gravity.TOP | Gravity.LEFT);
				WindowManager.LayoutParams params = myDialogSlider
						.getWindow().getAttributes();
				params.x = (int) (5 * getResources().getDisplayMetrics().widthPixels / 1980);
				params.y = (int) (1.75 * getResources().getDisplayMetrics().heightPixels / 4);

				myDialogSlider.getWindow().setAttributes(params);
				myDialogSlider.show();
				hideDialogThanhTruot();
			}
		});

		btnSinger = (TouchSingerView) findViewById(R.id.SingerView);
		// btnSinger.setSingerView(serverStatus.isSingerOn());
		btnSinger.setOnSingerListener(new OnSingerListener() {

			@Override
			public void onSinger(boolean isSingerOn) {
				hideKeyBoard();
				if(MyApplication.intWifiRemote == MyApplication.SONCA){
					((MyApplication) getApplication()).sendCommand(
							NetworkSocket.REMOTE_CMD_SINGER, isSingerOn ? 1 : 0);
				}else{
					MyLog.e(TAB, "btnSinger : " + isSingerOn);
					((MyApplication) getApplication()).sendCommandKartrol(
							(byte) RemoteIRCode.IRC_MELODY,
							0);
				}
			}

			@Override
			public void OnInActive() {
				hideKeyBoard();
				showDialogConnect();
			}
		});

		btnTone = (TouchToneView) findViewById(R.id.ToneView);
		// btnTone.setToneView(serverStatus.getCurrentTone());
		btnTone.setOnToneListener(new OnToneListener() {
			@Override
			public void onTone(int toneType) {
				hideKeyBoard();
				if(MyApplication.intWifiRemote == MyApplication.SONCA){
					((MyApplication) getApplication()).sendCommand(
							NetworkSocket.REMOTE_CMD_TONE, toneType);
				}else{
					MyLog.e(TAB, "btnTone : " + toneType);
					((MyApplication) getApplication()).sendCommandKartrol(
							(byte) RemoteIRCode.IRC_TONE,
							0);
				}
			}

			@Override
			public void OnInActive() {
				hideKeyBoard();
				showDialogConnect();
			}
		});

		btnScore = (TouchScoreView) findViewById(R.id.ScoreView);
		// btnScore.setScoreView(serverStatus.isScoreOn());
		btnScore.setOnScoreListener(new OnScoreListener() {
			@Override
			public void onScore(int isScoreOn) {
				hideKeyBoard();
				if(MyApplication.intWifiRemote == MyApplication.SONCA){
					((MyApplication) getApplication()).sendCommand(
							NetworkSocket.REMOTE_CMD_SCORE, isScoreOn);
				}else{
					MyLog.e(TAB, "btnScore : " + isScoreOn);
					((MyApplication) getApplication()).sendCommandKartrol(
							(byte) RemoteIRCode.IRC_SCORE,
							0);
				}
			}

			@Override
			public void OnInActive() {
				hideKeyBoard();
				showDialogConnect();
			}
		});

		pauseView = (TouchPauseView) findViewById(R.id.pauseView);
		// pauseView.setPauseView(serverStatus.isPaused());
		pauseView.setOnPauseListener(new OnPauseListener() {
			@Override
			public void onPause(boolean isPlaying) {
				hideKeyBoard();
				if(MyApplication.intWifiRemote == MyApplication.SONCA){
					if (isPlaying) {
						((MyApplication) getApplication()).sendCommand(
								NetworkSocket.REMOTE_CMD_PLAY, 0);
					} else {
						((MyApplication) getApplication()).sendCommand(
								NetworkSocket.REMOTE_CMD_PAUSE, 0);
					}
				}else{
					MyLog.e(TAB, "pauseView : " + isPlaying);
					((MyApplication) getApplication()).sendCommandKartrol(
							(byte) RemoteIRCode.IRC_PAUSEPLAY,
							0);
				}
			}

			@Override
			public void OnInActive() {
				hideKeyBoard();
				showDialogConnect();
			}
		});

		nextView = (TouchNextView) findViewById(R.id.NextView);
		nextView.setOnNextListener(new OnNextListener() {
			@Override
			public void onNext() {
				hideKeyBoard();
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
				}
			}

			@Override
			public void OnInActive() {
				hideKeyBoard();
				showDialogConnect();
			}
		});
		
		repeatView = (TouchRepeatView) findViewById(R.id.repeatView);
		repeatView.setOnRepeatListener(new OnRepeatListener() {
			
			@Override
			public void onRepeat() {
				hideKeyBoard();
				
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
				}
			}

			@Override
			public void OnInActive() {
				hideKeyBoard();
				showDialogConnect();
			}
		});

		drawerLayout.setMyDialogSlider(myDialogSlider);
		drawerLayout.setScrimColor(Color.TRANSPARENT);
		drawerLayout.setDrawerLockMode(
				TouchMyDrawerLayout.LOCK_MODE_LOCKED_CLOSED, layoutConnect);
		drawerLayout.setDrawerListener(new DrawerListener() {
			@Override
			public void onDrawerStateChanged(int newState) {
				
			}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				if (mainKeyboardListener != null) {
					mainKeyboardListener.OnDrawerSlide();
				}
				if(serverStatus == null && !drawerLayout.isDrawerOpen(drawerView)){
					// drawerLayout.openDrawer(drawerView);
				}
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				if(mainListener != null && SAVETYPE == YOUTUBE){
					mainListener.OnClosePopupYouTube(-1);
				}
				
				drawerLayout.setDrawerLockMode(
						TouchMyDrawerLayout.LOCK_MODE_UNLOCKED, drawerView);	
			}

			@Override
			public void onDrawerClosed(View drawerView) {
				isProcessingSomething = false;
				
				if(editDong1 != null && editDong2 !=null){
					((MyApplication)getApplication()).hideVirtualKeyboard(editDong1);
					((MyApplication)getApplication()).hideVirtualKeyboard(editDong2);
					if(!flagRunHide){
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
				if(mainListener != null){
					mainListener.OnClosePopupYouTube(position);
				}
			}
			
		});
		drawerLayout.setPointerListener(new PointerListener() {
			@Override
			public void OnPointer(float x) {
				// MyLog.e(TAB, "setPointerListener()");
				/*
				if (drawerLayout.isDrawerOpen(layoutKeyboard)) {
					if (viewBack.getVisiable() == View.GONE) {
						searchView.setPointerText(x, 0);
					} else {
						searchView.setPointerText(x, viewBack.getWidthView());
					}
				}
				*/
			}
		});

		LanguageStore langStore = new LanguageStore(this);
		langStore.initStarting();
		
		isDestroyMainActivity = true;
		//AppSettings setting = AppSettings.getInstance(getApplicationContext());
		//if (setting.loadServerLastUpdate() != 0) {
			// if(setting.isUpdated()) {
			// MyLog.e(TAB, "Create - ShowSongFragment");
			ShowSongFragment(SONG, songView);
			// OnUpdatePic();
		//}
		// UpdateControlView(4);
					
		creatKeyBoard();
		EnableControlView();
		
		ChangeColorScreen();
		
		/*
		defaultControlView.setCommandEnable(false);
		melodyView.setCommandEnable(false);
		tempoView.setCommandEnable(false);
		keyView.setCommandEnable(false);
		volumnView.setCommandEnable(false);
		danceView.setCommandEnable(false);
		btnScore.setCommandEnable(false);
		btnSinger.setCommandEnable(false);
		btnTone.setCommandEnable(false);
		repeatView.setCommandEnable(false);
		pauseView.setCommandEnable(false);
		nextView.setCommandEnable(false);
		MyLog.e(TAB, "intCommandEnable : " + MyApplication.intCommandEnable);
		*/
	}
	
	private long hoaAmTime = 0;
	private String IpDeviceConnect=null;
	@Override
	protected void onStart() {
		super.onStart();
		// MyLog.e(TAB, "onStart()");
		isDestroyMainActivity = true;
		layoutConnect.setOnClickListener(null);
		songView.setOnBaseTypeViewListener(this);
		singerView.setOnBaseTypeViewListener(this);
		musicianView.setOnBaseTypeViewListener(this);
		typeView.setOnBaseTypeViewListener(this);
		hotSongTypeView.setOnBaseTypeViewListener(this);
		// langagueView.setOnBaseTypeViewListener(this);
		favouriteView.setOnBaseTypeViewListener(this);
		remixView.setOnBaseTypeViewListener(this);
		youtubeView.setOnBaseTypeViewListener(this);
		groupView.setOnMyGroupListener(this);
		searchView.setOnSearchViewListener(this);
		
			//--------//
		
		RelativeLayout layout = (RelativeLayout)findViewById(R.id.backgroundlayout);
		ImageView layout2 = (ImageView)findViewById(R.id.topMyGroupView);
		if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			layout.setBackgroundColor(Color.parseColor("#C1FFE8"));
			layout2.setBackgroundResource(R.drawable.zlight_baner_dance);
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE){
			layout.setBackgroundResource(R.drawable.mainbg);
			layout2.setBackgroundResource(R.drawable.touch_dance_1271x206);
		}
		
		
	}

	final String PREFS_FLAGHDD = "EverConnectHDD";
	
	@Override
	protected void onResume() {
		MyLog.e("ONE RESUME","........................");
		super.onResume();
		
		setupMainWindowDisplayMode();
		
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
						MyApplication.flagAllowAdvSong= setting.isAdvSong();
						MyApplication.flagAllowSearchOnline= setting.isSearchOnline();
						
						MyApplication.freezeTime = System.currentTimeMillis();
						startTimerFreeze();
						
						CheckYoutubeAPIListKey keyListCheck = new CheckYoutubeAPIListKey();
						keyListCheck.execute();
						
						CheckViralVideoTask viralVideoTask = new CheckViralVideoTask();
						viralVideoTask.execute();
						
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
								groupView.setConnected(TouchMyGroupView.CONNECTED,
										skServer.getName(), skServer);
							}
							if (flagRunHide) {
								hideActionBar();
							}

//							drawerLayout.openDrawer(layoutConnect);
							
							if (drawerLayout.isDrawerOpen(layoutConnect)) {
								drawerLayout.closeDrawer(layoutConnect);
							}
							if (listDeviceListener != null) {
								listDeviceListener.OnShowListDevice();
							}
						}		
						
						((MyApplication) getApplication()).setOnApplicationListener(TouchMainActivity.this);
						setActiveView(SAVETYPE);
						
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
						if (groupView != null) {
							groupView.invalidate();
						}
						
						try {
							if(colorLyricListener != null){
								colorLyricListener.OnMain_CallVideoDefault();
							}
							
				    		TouchMainActivity.this.registerReceiver(mWifiInfoReceiver, 
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
		if (processAuthenTask != null) {
			processAuthenTask.cancel(true);
			processAuthenTask = null;
		}
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
if(MyApplication.enableSamba)
			stopService(new Intent(this, StreamService.class));//samba
	
		if(serviceIntent != null){
			TouchMainActivity.this.stopService(serviceIntent);
			if(MyApplication.updateFirmwareServerSocket != null){
				try {
					MyApplication.updateFirmwareServerSocket.close();	
				} catch (Exception e) {
					
				}							
			}	
		}
		stopTimerPing();
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
		changePerfectView.invalidate();
		defaultControlView.invalidate();
		melodyView.invalidate();
		tempoView.invalidate();
		keyView.invalidate();
		volumnView.invalidate();
		
		btnScore.invalidate();
		btnSinger.invalidate();
		btnTone.invalidate();
		btnVideo.invalidate();
		danceView.invalidate();
		repeatView.invalidate();
		
		groupView.invalidate();
		pauseView.invalidate();
		nextView.invalidate();
//		exitView.invalidate();
		
		if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
			viewFlower.setVisibility(View.VISIBLE);
			youtubeView.setVisibility(View.VISIBLE);
		} else {
			viewFlower.setVisibility(View.GONE);
			youtubeView.setVisibility(View.GONE);
		}
		
		songView.requestLayout();
		singerView.requestLayout();
		musicianView.requestLayout();
		typeView.requestLayout();
		hotSongTypeView.requestLayout();
		remixView.requestLayout();
		favouriteView.requestLayout();
		youtubeView.requestLayout();	
		
	}
	
	private void ChangeColorScreen(){
		RelativeLayout layout = (RelativeLayout)findViewById(R.id.backgroundlayout);
		ImageView layout2 = (ImageView)findViewById(R.id.topMyGroupView);
		if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
			layout.setBackgroundColor(Color.parseColor("#C1FFE8"));
			layout2.setBackgroundResource(R.drawable.zlight_baner_dance);
		}else if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE){
			layout.setBackgroundResource(R.drawable.mainbg);
			layout2.setBackgroundResource(R.drawable.touch_dance_1271x206);
		}
		
		searchView.invalidate();
		viewBack.invalidate();
//		exitView.invalidate();
		btnVideo.invalidate();
		deleteSearchView.invalidate();
		changePerfectView.invalidate();
		defaultControlView.invalidate();
		melodyView.invalidate();
		tempoView.invalidate();
		keyView.invalidate();
		volumnView.invalidate();
				
		btnScore.invalidate();
		btnSinger.invalidate();
		btnTone.invalidate();
		danceView.invalidate();
		repeatView.invalidate();
		
		groupView.invalidate();
		pauseView.invalidate();
		nextView.invalidate();
		
		songView.invalidate();
		singerView.invalidate();
		musicianView.invalidate();
		typeView.invalidate();
		hotSongTypeView.invalidate();
		// langagueView.invalidate();
		favouriteView.invalidate();
		remixView.invalidate();
//		exitView.invalidate();
		
		if(mainListener != null){
			mainListener.OnUpdateView();
		}
		if(deviveFragmentListener != null){
			deviveFragmentListener.OnUpdateView();
		}
		if(listDeviceListener != null){
			listDeviceListener.OnUpdateView();
		}
		if(onToHelloListener != null){
			onToHelloListener.OnUpdateView();
		}
		if(mainKeyboardListener != null){
			mainKeyboardListener.OnUpdateView();
		}
		
	}


	// ///////////////////////////////////////////////////////////////////////////////

	private void setActiveView(String type) {
		if (type == null) {
			return;
		}
		if (type.equals(SONG)) {
			if (singerView.isActive() == TouchBaseTypeView.INACTIVE
					&& musicianView.isActive() == TouchBaseTypeView.INACTIVE
					&& typeView.isActive() == TouchBaseTypeView.INACTIVE
					/*&& langagueView.isActive() == TouchBaseTypeView.INACTIVE*/) {
				if (songView.isActive() == TouchBaseTypeView.INACTIVE) {
					songView.setActive();
				}
			}
		} else if (type.equals(SINGER)) {
			if (singerView.isActive() == TouchBaseTypeView.INACTIVE) {
				singerView.setActive();
			}
		} else if (type.equals(MUSICIAN)) {
			if (musicianView.isActive() == TouchBaseTypeView.INACTIVE) {
				musicianView.setActive();
			}
		} else if (type.equals(SONGTYPE)) {
			if (typeView.isActive() == TouchBaseTypeView.INACTIVE) {
				typeView.setActive();
			}
		} else if (type.equals(LANGUAGE)) {
			/*if (langagueView.isActive() == TouchBaseTypeView.INACTIVE) {
				langagueView.setActive();
			}*/
		} else if (type.equals(FAVOURITE)) {
			if (favouriteView.isActive() == TouchBaseTypeView.INACTIVE) {
				favouriteView.setActive();
			}
		} else if (type.equals(PLAYLIST)) {
			if (groupView.isActive() == TouchMyGroupView.INACTIVE) {
				groupView.setActivePlayList();
			}
		} else if (type.equals(REMIX)) {
			if (remixView.isActive() == TouchBaseTypeView.INACTIVE) {
				remixView.setActive();
			}
		} else if (type.equals(SONGFOLLOW)) {
			if (singerView.isActive() == TouchBaseTypeView.INACTIVE) {
				singerView.setActive();
			}
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
	
	public void AddListenerKeyBoard() {
		TouchKeyBoardFragment keyBoardFragment = (TouchKeyBoardFragment) fragmentManager
				.findFragmentById(R.id.fragmentKeyBoard);
		textListener = (OnClearTextListener) keyBoardFragment;
	}

	private TouchKeyBoardFragment keyBoardFragment = null;

	private void creatKeyBoard() {
		if (isDestroyMainActivity == false) {
			return;
		}
		if (keyBoardFragment == null) {
			FragmentTransaction fragmentTransaction = 
					fragmentManager.beginTransaction();
			keyBoardFragment = (TouchKeyBoardFragment)
					fragmentManager.findFragmentById(R.id.fragmentKeyBoard);
			mainKeyboardListener = (OnMainKeyboardListener) keyBoardFragment;
			fragmentTransaction.setCustomAnimations(
					R.anim.framgent_in_keyboard,
					R.anim.framgent_out_keyboard);
			fragmentTransaction.hide(keyBoardFragment);
			fragmentTransaction.commit();
			MyApplication.boolShowKeyBoard = false;
		}
	}
	
	public InputMethodManager keyboard;
	private GroupSearch myGroupSearch;
	
	public EditText searchEditText;	
	public EditText fakeEditText;
	boolean flagBlockSearch = false;
	
	@Override
	public void OnCallEditTextSearchLayout() {
		if(myGroupSearch != null){
			myGroupSearch.callRequestLayout();
		}
		
		if (SAVETYPE != PLAYLIST && SAVETYPE != SONGTYPE
				&& SAVETYPE != SONGFOLLOW && SAVETYPE != LANGUAGE
				&& SAVETYPE != FAVOURITE) {			
			searchEditText.setCursorVisible(true);
			searchEditText.setEnabled(true);
			searchEditText.setVisibility(View.VISIBLE);
			
			if(!searchEditText.getText().toString().equals("") && searchView.getTextSearch().equals("") && SAVETYPE == SONG){
				flagBlockSearch = true;
				searchEditText.setText("");
			}
			
		} else {
			searchEditText.setCursorVisible(false);
			searchEditText.setEnabled(false);
			searchEditText.setVisibility(View.INVISIBLE);
			
			if(searchView != null && searchView.getActiveSearch() == true){
				searchView.setActiveSearch(false);
			}
		}
		
	}
	
	private void generateKeyboard() {
		searchEditText
		.setInputType(EditorInfo.TYPE_TEXT_FLAG_NO_SUGGESTIONS
				| EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
	}
	
	private void showKeyBoard(){
		if (isDestroyMainActivity == false) {
			return;
		}
		
		hideActionBar();
		
		if(MyApplication.flagRealKeyboard){
			if(MyApplication.boolShowKeyBoard != true) {
				generateKeyboard();
				searchEditText.requestFocus();
				searchEditText.requestFocusFromTouch();
				keyboard.showSoftInput(searchEditText, InputMethodManager.SHOW_IMPLICIT);
				
				searchView.setActiveSearch(true);
				MyApplication.boolShowKeyBoard = true;
			}	
		} else {
			if (keyBoardFragment != null && MyApplication.boolShowKeyBoard != true) {    
				searchView.setActiveSearch(true);
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.setCustomAnimations(
						R.anim.framgent_in_keyboard,
						R.anim.framgent_out_keyboard);
				keyBoardFragment.showDefaultKeyboard();
				fragmentTransaction.show(keyBoardFragment);
				fragmentTransaction.commit();
				MyApplication.boolShowKeyBoard = true;
			}
		}
		
	}
	
	private void hideKeyBoard(){
		if (isDestroyMainActivity == false) {
			return;
		}
		
		if(MyApplication.flagRealKeyboard){
			if (keyboard != null) {      
				keyboard.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
				searchView.setActiveSearch(false);
				searchView.setActiveX(false);
				MyApplication.boolShowKeyBoard = false;
			}
		} else {
			if (keyBoardFragment != null && MyApplication.boolShowKeyBoard != false) {     
				searchView.setActiveSearch(false);
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.setCustomAnimations(
						R.anim.framgent_in_keyboard,
						R.anim.framgent_out_keyboard);
				fragmentTransaction.hide(keyBoardFragment);
				fragmentTransaction.commit();
				MyApplication.boolShowKeyBoard = false;
			}
		}
		
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

	private void ToFragmentDance(boolean bool) {
		try {
			countChangeDance = 0;
			((MyApplication) context.getApplicationContext()).setListActive(new ArrayList<Song>());
			LoadLyric.hideDialog();	
		} catch (Exception e) {
			
		}	
		layoutSingerLink.setVisibility(View.GONE);
		findViewById(R.id.layoutLinkDance).setVisibility(View.GONE);
		if (isDestroyMainActivity == false) {
			return;
		}
		if(sliderView != null && myDialogSlider != null && myDialogSlider.isShowing()){
			myDialogSlider.dismiss();
		}
		if(sliderView != null && myDialogSliderDance != null && myDialogSliderDance.isShowing()){
			myDialogSliderDance.dismiss();
		}
		
		MyApplication.flagDance =  bool;
		
		if (bool) {
			findViewById(R.id.layoutOtherDance).setVisibility(View.GONE);
			findViewById(R.id.layoutTabOtherDance).setVisibility(View.GONE);
			findViewById(R.id.layoutDance).setVisibility(View.VISIBLE);
			findViewById(R.id.layoutTabDance).setVisibility(View.VISIBLE);
			resetActiveView();
			ShowSongFragment(NOTHING, null);
			ShowDanceFragment();
			// MyLog.e(TAB, "OnStop() - NODANCE");
		} else {
			findViewById(R.id.layoutOtherDance).setVisibility(View.VISIBLE);
			findViewById(R.id.layoutTabOtherDance).setVisibility(View.VISIBLE);
			findViewById(R.id.layoutDance).setVisibility(View.GONE);
			findViewById(R.id.layoutTabDance).setVisibility(View.GONE);
			HideDanceFragment();
			// ----------//
			SAVETYPE = "";
			resetActiveView();
			((MyApplication) getApplication()).clearListBack();
			viewBack.setVisiable(View.GONE);
			groupView.setExit(TouchMyGroupView.EXIT);
			ShowSongFragment(SONG, songView);
			// MyLog.e(TAB, "OnStop() - DANCE");
		}
	}

	private TouchFragmentBase fragmentDance;

	private void ShowDanceFragment() {
		if (isDestroyMainActivity == false) {
			return;
		}
		danceLinkView.setLayout(TouchDanceLinkView.DANCE);
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentDance = new TouchFragmentDance();
		Bundle bundle = new Bundle();
		bundle.putBoolean("Mute", volumnView.isMute());
		bundle.putInt("Volumn", volumnView.getVolumn());
		bundle.putBoolean("Pause", pauseView.getPauseView());
		fragmentDance.setArguments(bundle);
		mainListener = (OnMainListener) fragmentDance;
		fragmentTransaction.replace(R.id.fragmentDance, fragmentDance, "DANCE");
		fragmentTransaction.commit();
	}

	private void HideDanceFragment() {
		if (isDestroyMainActivity == false) {
			return;
		}
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		if (fragmentDance != null) {
			fragmentTransaction.remove(fragmentDance);
		}
		fragmentTransaction.commit();
	}

	private void ShowSongFragment(String type, TouchBaseTypeView view) {
		if (isDestroyMainActivity == false) {
			return;
		}
		if (view == null) {
		} else {
			view.setActive();
		}
//		if (SAVETYPE.equals(type)) {
//			return;
//		}
		
		if(MyApplication.flagRealKeyboard){
			OnCloseKeyBoard();
			searchEditText.setText("");
			edSearchOldStr = "";
			fakeEditText.requestFocus();
		}
		
		viewBack.setVisiable(View.GONE);
		searchView.setRequestLayout();
		layoutDeleteSearchView.setVisibility(View.INVISIBLE);
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		if (fragmentBase != null) {
			fragmentTransaction.remove(fragmentBase);
			if (textListener != null) {
				textListener.OnClearText();
			}
		}
		// ------------------//
		deleteSearchView.invalidate();
		// ------------------//
		if (type.equals(YOUTUBE)) {
			stateSearchView = TouchSearchView.YOUTUBE_KARAOKE;
			searchView.setLayoutSearchView(TouchSearchView.YOUTUBE,
					getString(R.string.search_1d), -1, YOUTUBE, "", -1);
			danceLinkView.setLayout(TouchDanceLinkView.NODANCE);
			
			fragmentBase = new FragmentYouTube();
			mainListener = (OnMainListener) fragmentBase;
			Bundle bundle = new Bundle();
			fragmentBase.setArguments(bundle);
			fragmentTransaction
					.replace(R.id.fragmentLayout, fragmentBase, YOUTUBE);			

			SAVEDEVICE = "";			
			SAVETYPE = type;
			fragmentTransaction.commit();
			((MyApplication) context).clearListBack();
			groupView.setExit(TouchMyGroupView.EXIT);
			hideKeyBoard();
			deleteSearchView.invalidate();
			return;
			
		} else if (type.equals(SONG)) {
			stateSearchView = TouchSearchView.TATCA;
			searchView.setLayoutSearchView(TouchSearchView.SONG,
					getString(R.string.search_1a), -1, SONG, "", -1);
			danceLinkView.setLayout(TouchDanceLinkView.NODANCE);
			fragmentBase = new TouchFragmentSong();
			mainListener = (OnMainListener) fragmentBase;
			Bundle bundle = new Bundle();
			bundle.putBoolean("data", false);
			bundle.putInt("state1", searchView.getState1());
			bundle.putInt("state2", searchView.getState2());
			fragmentBase.setArguments(bundle);
			fragmentTransaction
					.replace(R.id.fragmentLayout, fragmentBase, SONG);
		} else if (type.equals(SINGER)) {
			fragmentBase = new TouchFragmentSinger();
			Bundle bundle = new Bundle();
			bundle.putString("data", "");
			bundle.putInt("state", TouchSearchView.VIETNAM);
			fragmentBase.setArguments(bundle);
			mainListener = (OnMainListener) fragmentBase;
			fragmentTransaction.replace(R.id.fragmentLayout, fragmentBase,
					SINGER);
			stateSearchView = TouchSearchView.VIETNAM;
			searchView.setLayoutSearchView(TouchSearchView.SINGER,
					getString(R.string.search_1b), -1, SINGER, "",
					TouchSearchView.VIETNAM);
		} else if (type.equals(MUSICIAN)) {
			fragmentBase = new TouchFragmentMusician();
			Bundle bundle = new Bundle();
			bundle.putString("data", "");
			bundle.putInt("state", TouchSearchView.VIETNAM);
			fragmentBase.setArguments(bundle);
			mainListener = (OnMainListener) fragmentBase;
			fragmentTransaction.replace(R.id.fragmentLayout, fragmentBase,
					MUSICIAN);
			stateSearchView = TouchSearchView.VIETNAM;
			searchView.setLayoutSearchView(TouchSearchView.SINGER,
					getString(R.string.search_1c), -1, MUSICIAN, "",
					TouchSearchView.VIETNAM);
		} else if (type.equals(SONGTYPE)) {
			fragmentBase = new TouchFragmentSongType();
			mainListener = (OnMainListener) fragmentBase;
			fragmentTransaction.replace(R.id.fragmentLayout, fragmentBase,
					SONGTYPE);
			((TouchFragmentSongType)fragmentBase).setOnNumberSongTypeListener(
					new OnNumberSongTypeListener() {
				@Override public void OnNumberListener(int sum) {
					/*
					searchView.setLayoutSearchView(SearchView.SONGTYPE, "", -1,
							SONGTYPE, "", -1);
					*/
					searchView.setSongType(sum);
				}
			});
		} else if (type.equals(LANGUAGE)) {
			fragmentBase = new TouchFragmentLanguage();
			mainListener = (OnMainListener) fragmentBase;
			fragmentTransaction.replace(R.id.fragmentLayout, fragmentBase,
					LANGUAGE);
			searchView.setLayoutSearchView(TouchSearchView.SONGTYPE, "", -1,
					LANGUAGE, "", -1);
		} else if (type.equals(FAVOURITE)) {
			int sum = DBInterface.DBGetFavouriteSongList(0, 0, context).size();
			searchView.setLayoutSearchView(TouchSearchView.PLAYLIST, "", sum,
					FAVOURITE, "", -1);
			mainListener = null;
			fragmentBase = new TouchFragmentFavourite();
			mainListener = (OnMainListener) fragmentBase;
			fragmentTransaction.replace(R.id.fragmentLayout, fragmentBase,
					FAVOURITE);
		} else if (type.equals(PLAYLIST)) {
			ArrayList<Song> list = ((MyApplication) this.getApplication()).getListActive();
			int sum = 0;
			if (list != null) {
				sum = list.size();
			}
			searchView.setLayoutSearchView(TouchSearchView.PLAYLIST, "", sum,
					PLAYLIST, "", -1);
			mainListener = null;
			fragmentBase = new TouchFragmentPlayList();
			mainListener = (OnMainListener) fragmentBase;
			fragmentTransaction.replace(R.id.fragmentLayout, fragmentBase,
					PLAYLIST);
		} else if (type.equals(REMIX)) {
			fragmentBase = new TouchFragmentRemix();
			Bundle bundle = new Bundle();
			bundle.putString("search", "");
			fragmentBase.setArguments(bundle);
			mainListener = (OnMainListener) fragmentBase;
			fragmentTransaction.replace(R.id.fragmentLayout, fragmentBase,
					REMIX);
			stateSearchView = TouchSearchView.TATCA;
			searchView.setLayoutSearchView(TouchSearchView.SONG,
					getString(R.string.search_1a), -1, SONG, "", -1);
		} else if (type.equals(HOTSONG)) {
			fragmentBase = new FragmentHotSong();
			Bundle bundle = new Bundle();
			bundle.putString("search", "");
			fragmentBase.setArguments(bundle);
			mainListener = (OnMainListener) fragmentBase;
			fragmentTransaction.replace(R.id.fragmentLayout, fragmentBase,
					HOTSONG);
			stateSearchView = TouchSearchView.TATCA;
			searchView.setLayoutSearchView(TouchSearchView.SONG,
					getString(R.string.search_1a), -1, SONG, "", -1);
		}
		// ------------------//
		SAVETYPE = type;
		fragmentTransaction.commit();
		((MyApplication) context).clearListBack();
		groupView.setExit(TouchMyGroupView.EXIT);
		hideKeyBoard();
	}

	// /////////////////////////// - LISTENER -
	// ////////////////////////////////////

	private void resetActiveView() {
		songView.setInActive();
		singerView.setInActive();
		musicianView.setInActive();
		typeView.setInActive();
		hotSongTypeView.setInActive();
		// langagueView.setInActive();
		favouriteView.setInActive();
		remixView.setInActive();
		youtubeView.setInActive();
		groupView.setInActivePlayList();
	}

	@Override
	public void OnClickFavourite() {
		// MyLog.e("Main", "OnClickFavourite()");
		int sum = DBInterface.DBGetFavouriteSongList(0, 0, context).size();
		searchView.setSumFavourite(sum, FAVOURITE);
		hideKeyBoard();
	}

	@Override
	public void onClickItem(Song song, String id, String typeFragment,
			String search, int state, float x, float y) {
		if (!typeFragment.equals(SONG)) {
			layoutDeleteSearchView.setVisibility(View.INVISIBLE);
		}
		if (typeFragment.equals(SINGER) || typeFragment.equals(MUSICIAN)
				|| typeFragment.equals(SONGTYPE)
				|| typeFragment.equals(LANGUAGE)) {
			
			if(MyApplication.flagRealKeyboard){
				searchEditText.setCursorVisible(false);
				searchEditText.setEnabled(false);
				searchEditText.setVisibility(View.INVISIBLE);	
			}	
			
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			if (fragmentBase != null) {
				fragmentTransaction.remove(fragmentBase);
				if (textListener != null) {
					textListener.OnClearText();
				}
			}
			boolean priority = true;
			if(id.equals("" + DbHelper.SongType_Remix) && typeFragment.equals(SONGTYPE)){
				typeFragment = REMIX;
				typeView.setInActive();
				ShowSongFragment(REMIX, remixView);
				priority = false;
				return;
			}
			
			if(id.equals("" + DbHelper.SongType_HotSong) && typeFragment.equals(SONGTYPE)){
				typeFragment = HOTSONG;
				typeView.setInActive();
				ShowSongFragment(HOTSONG, hotSongTypeView);
				priority = false;
				return;
			}
			
			if(id.equals("" + DbHelper.SongType_Youtube) && typeFragment.equals(SONGTYPE)){
				typeFragment = YOUTUBE;
				typeView.setInActive();
				ShowSongFragment(YOUTUBE, youtubeView);
				priority = false;
				return;
			}
			
			if(priority){
				fragmentBase = new TouchFragmentSong();
				mainListener = (OnMainListener) fragmentBase;
				Bundle bundle = new Bundle();
				bundle.putBoolean("data", true);
				bundle.putString("id", id);
				bundle.putString("type", typeFragment);
				if(typeFragment.equals(SONGTYPE)){
					bundle.putBoolean("clickTheloai", true);
				}
				fragmentBase.setArguments(bundle);
				fragmentTransaction
						.replace(R.id.fragmentLayout, fragmentBase, SONG);
			}
			if (typeFragment.equals(SINGER) || typeFragment.equals(MUSICIAN)) {
				searchView
						.setLayoutSearchView(TouchSearchView.NOLAYOUT,
								getString(R.string.search_1a), -1,
								typeFragment, "", -1);
				viewBack.setVisiable(View.VISIBLE);
				if (typeFragment.equals(SINGER))
					viewBack.setLayout(SINGER, false);
				if (typeFragment.equals(MUSICIAN))
					viewBack.setLayout(MUSICIAN, false);
				searchView.setRequestLayout();
			} else if (typeFragment.equals(SONGTYPE)
					|| typeFragment.equals(LANGUAGE)) {
				searchView.setActiveX(false);
				// searchView.setActiveSearch(false);

				searchView.setLayoutSearchView(TouchSearchView.SONG,
						getString(R.string.search_1a), -1, SONG, "", -1);
				viewBack.setVisiable(View.VISIBLE);
				if (typeFragment.equals(SONGTYPE))
					viewBack.setLayout(SONGTYPE, false);
				if (typeFragment.equals(LANGUAGE))
					viewBack.setLayout(LANGUAGE, false);
				searchView.setRequestLayout();
			}
			SAVETYPE = SONG;
			fragmentTransaction.commit();
			groupView.setExit(TouchMyGroupView.BACK);
			hideKeyBoard();
		} else if (typeFragment.equals(SONG) || typeFragment.equals(FAVOURITE)) {
			hideKeyBoard();
			if(serverStatus == null){
				showDialogConnect();
				return;
			}
			
			if(!boolShowKaraoke && MyApplication.flagDisplayTimeout){
				showDialogMessage(getResources().getString(R.string.msg_15) + "...\n" + getResources().getString(R.string.msg_15a));
				return;
			}	
			
			if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
					|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
					 || MyApplication.intSvrModel == MyApplication.SONCA_KBX9 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
					 || MyApplication.intSvrModel == MyApplication.SONCA_TBT){
				song.setTypeABC(0);
			}
			
			if(((MyApplication) TouchMainActivity.this.getApplication()).getListActive().size() == 99){
				showDialogMessage(getResources().getString(R.string.msg_1) + "! " + getResources().getString(R.string.msg_3));
				return;
			}
			
			boolean flagBlock = ((MyApplication) getApplication())
					.CheckDuplicateLastPlayList(song.getId() + "", song.getIndex5() + "", song.getTypeABC());
			if(flagBlock){
				String strData = "";
				strData += getString(R.string.pop_msg_add_1);
				strData += " \"" + song.getName() + "\" ";
				strData += getString(R.string.pop_msg_add_2) + " " + getString(R.string.pop_msg_add_3) + ".";
				
				ShowDialogNoAddPlayList(strData);
				
				if (mainListener != null) {
					mainListener.OnSK90009();
				}
				return;
			}
			
			if(!flagSwipe){
				// int sum =
				// ((MyApplication)this.getApplication()).getSizePlayList();
				if(!searchView.getTextSearch().equals("")){
				// if (!drawerLayout.isDrawerOpen(layoutKeyboard)) {
					// drawerLayout.openDrawer(layoutKeyboard);
					layoutDeleteSearchView.setVisibility(View.INVISIBLE);
					searchView.clearCharacterView();
					if(MyApplication.flagRealKeyboard){
						flagStopSearch = true;
						searchEditText.setText("");
						edSearchOldStr = "";	
					}	
				}	
			}
			
			if(serverStatus != null && !isRunningSequence && !MyApplication.flagOnPopup){
				animationView.stopAnimation();
				animationView.startAnimation(x, y, TouchAnimationView.SONG);
			}						
			
			if(MyApplication.intSvrModel == MyApplication.SONCA || 
					MyApplication.intSvrModel == MyApplication.SONCA_HIW || 
					MyApplication.intSvrModel == MyApplication.SONCA_KM2 ||
					MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
					|| MyApplication.intSvrModel == MyApplication.SONCA_KBX9 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
					 || MyApplication.intSvrModel == MyApplication.SONCA_TBT
					 || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
				if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
						|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
						 || MyApplication.intSvrModel == MyApplication.SONCA_TBT || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI){
					if(MyApplication.flagOnPopup){
						String msg1 = getResources().getString(R.string.confirm_1);
						String msg2 = song.getName();
						ShowConfirmDialog(msg1, msg2, 0, song.getId(), song.getTypeABC(), 0);
					} else {
						((MyApplication) getApplication()).addSongToPlaylist(
								song.getId(), song.getTypeABC(), 0);	
					}
					
					
				} else {
					if(MyApplication.flagOnPopup){
						String msg1 = getResources().getString(R.string.confirm_1);
						String msg2 = song.getName();
						ShowConfirmDialog(msg1, msg2, 0, song.getIndex5(), song.getTypeABC(), 0);
					} else {
						if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && song.isYoutubeSong()){
							MyLog.d(TAB, "Add YOUTUBE SONG = "+ song.getId() + " -- socketTypeABC = " + song.getTypeABC());
							((MyApplication) getApplication()).addSongToPlaylistYouTube(song.getId(), song.getTypeABC(), 0);	
						} else {
							((MyApplication) getApplication()).addSongToPlaylist(
									song.getIndex5(), song.getTypeABC(), 0);	
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
						String msg1 = getResources().getString(R.string.confirm_1) + " '" + song.getName() + "'?";
						String msg2 = song.getName();
						ShowConfirmDialog(msg1, msg2, 1, song.getIndex5(), song.getTypeABC(), 0);
					} else {
						((MyApplication) getApplication()).addSongToPlaylistKartrol(
								song.getIndex5(), song.getTypeABC(), 0);
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
		} else if (typeFragment.equals(PLAYLIST)) {
			// socket.playSongImediately(this, Integer.parseInt(id));
		}
	}

	@Override
	public void onDeleteSong(Song song, int postion) {
		if(postion >= curSongIDs.size()){
			return;
		}
		SongID tempSongID = curSongIDs.get(postion);
		
		if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
				|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
				|| MyApplication.intSvrModel == MyApplication.SONCA_KBX9 || MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
				 || MyApplication.intSvrModel == MyApplication.SONCA_TBT){
			((MyApplication) getApplication()).removeSongfromPlaylist(tempSongID.songID, 0, postion);
		} else {
			((MyApplication) getApplication()).removeSongfromPlaylist(tempSongID.songID, song.getTypeABC(), postion);
		}
	}

	@Override
	public void onMoveSong(ArrayList<Song> list) {
		if (((MyApplication) getApplication()).getSocket() != null) {
			ArrayList<SongID> idList = new ArrayList<SongID>();
			for (int i = 0; i < list.size(); i++) {
				Song song = list.get(i);
				SongID songID = new SongID();
				songID.songID = song.getIndex5();
				songID.typeABC = song.getTypeABC();
				idList.add(songID);
			}
			((MyApplication) getApplication()).sortSongPlaylist(idList);
		}
	}

	private boolean flagSwipe = false;
	public void setFlagSwipe(boolean flagSwipe){
		this.flagSwipe = flagSwipe;
	}
	
	private boolean isRunningSequence = false;
	
	@Override
	public void onFirstClick(Song song, String typeFragment, int postion,
			float x, float y) {
		hideKeyBoard();
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
				if(((MyApplication) TouchMainActivity.this.getApplication()).getListActive().size() == 99){
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
					
					if (mainListener != null) {
						mainListener.OnSK90009();
					}
					return;
				}
				
				if(!searchView.getTextSearch().equals("")){
				// if (!drawerLayout.isDrawerOpen(layoutKeyboard)) {
					// drawerLayout.openDrawer(layoutKeyboard);
					layoutDeleteSearchView.setVisibility(View.INVISIBLE);
					searchView.clearCharacterView();
					if(MyApplication.flagRealKeyboard){
						flagStopSearch = true;
						searchEditText.setText("");
						edSearchOldStr = "";	
					}	
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
	public void onPlaySong(Song song, String typeFragment, int position,
			float x, float y) {
		((MyApplication) getApplication()).playSongImediately(song.getId(),
				song.getTypeABC(), position);
	}

	@Override
	public void onBaseTypeView(boolean isActive, String type,
			TouchBaseTypeView view) {
		searchView.setActiveX(false);
		// searchView.setActiveSearch(false);
		// MyLog.e("Main", "TYPE : " + type);
		if (CheckBackToSong(type)) {
			resetActiveView();
			ShowSongFragment(type, view);
		}
	}

	private boolean CheckBackToSong(String typeFragment) {
		if (!typeFragment.equals(SONG)) {
			return true;
		}
		TouchFragmentSong fragmentSong = (TouchFragmentSong) fragmentManager
				.findFragmentByTag(SONG);
		if (fragmentSong != null) {
			String type = fragmentSong.getTypeFragment();
			// MyLog.e(TAB, "type - " + type + " | SAVETYPE - " + SAVETYPE);
			if (type.equals("")) {
				return true;
			} else {
				if (typeFragment.equals(SONG)) {
					SAVETYPE = "";
					resetActiveView();
					songView.setActive();
					ShowSongFragment(SONG, null);
				} else {
					ShowSongFragment(type, null);
					if (type.equals(SINGER)) {
						singerView.setActive();
					} else if (type.equals(MUSICIAN)) {
						musicianView.setActive();
					} else if (type.equals(SONGTYPE)) {
						typeView.setActive();
					}
				}
				return false;
			}
		} else {
			return true;
		}
	}

	// ---------------------MyGroupView-----------------------//

	@Override
	public void OnPlayList() {
		MyLog.e(TAB, "Main - OnPlayList()");
		hideKeyBoard();
		if(serverStatus != null){
			if(serverStatus.danceMode() != 1){
				resetActiveView();
				groupView.setActivePlayList();
				ShowSongFragment(PLAYLIST, null);
			}
		}else{
			showDialogConnect();
		}
	}
	
	@Override
	public void OnFAKEShowConnect() {
		ShowListDevice("");		
	}

	@Override
	public void OnShowConnect() {	
		hideKeyBoard();
		if (serverStatus == null) { // no connect
			ShowListDevice("");
			// ShowModel("", "");
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
						// ShowModel("", "");
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
						// ShowModel("", "");
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

	@Override
	public void OnSingerLink(final boolean isMIDI, String nameSinger, int[] idSinger) {
		hideKeyBoard();
		if (!nameSinger.trim().equals("-")) {
			if (nameSinger.split(",").length > 1) {
				layoutSingerLink.setVisibility(View.VISIBLE);
				singerLinkView.ShowSingerLink(nameSinger, idSinger);
				singerLinkView.setOnStopShowListener(new OnStopShowListener() {
					@Override
					public void OnStop(int idSinger) {
						layoutSingerLink.setVisibility(View.GONE);
						if (idSinger != -1) {
							ShowFragmentSongFollow(isMIDI, String.valueOf(idSinger));
						} else {
							((MyApplication) context).getItemListBack();
						}
					}
				});
			} else {
				ShowFragmentSongFollow(isMIDI, String.valueOf(idSinger[0]));
			}
		}
	}

	private void ShowFragmentSongFollow(boolean isMIDI, String idSinger) {
		layoutDeleteSearchView.setVisibility(View.INVISIBLE);
		searchView.setRequestLayout();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		if (fragmentBase != null) {
			fragmentTransaction.remove(fragmentBase);
			if (textListener != null) {
				textListener.OnClearText();
			}
		}
		viewBack.setVisiable(View.VISIBLE);
		resetActiveView();
		if (isMIDI == true) {
			viewBack.setLayout(MUSICIAN, false);
			musicianView.setActive();
		} else {
			viewBack.setLayout(SINGER, false);
			singerView.setActive();
		}
		fragmentBase = new TouchFragmentSongFollow();
		Bundle bundle = new Bundle();
		bundle.putString("data", idSinger);
		if (isMIDI == true) {
			bundle.putString("fragment", MUSICIAN);
		} else {
			bundle.putString("fragment", SINGER);
		}
		fragmentBase.setArguments(bundle);
		mainListener = (OnMainListener) fragmentBase;
		if (isMIDI == true) {
			fragmentTransaction.replace(R.id.fragmentLayout, fragmentBase, MUSICIAN);
		} else {
			fragmentTransaction.replace(R.id.fragmentLayout, fragmentBase, SINGER);
		}
		searchView.setLayoutSearchView(TouchSearchView.NOLAYOUT,
				getString(R.string.search_1a), -1, SONG, "", -1);
		SAVETYPE = SONGFOLLOW;
		fragmentTransaction.commit();
		groupView.setExit(TouchMyGroupView.BACK);
		hideKeyBoard();
	}

	@Override
	public void OnExit(boolean value) {
		ShowHello();
		// drawerLayout.openDrawer(layoutConnect);
	}

	private void OnBackFragment() {
		hideActionBar();
		TouchItemBack itemBack = ((MyApplication) context).getItemListBack();
		layoutDeleteSearchView.setVisibility(View.INVISIBLE);
		if (itemBack != null) {
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			if (fragmentBase != null) {
				fragmentTransaction.remove(fragmentBase);
				if (textListener != null) {
					textListener.OnClearText();
				}
			}
			resetActiveView();
			String typeFrag = itemBack.getTypeFrag();
			String search = itemBack.getSearch();
			int statesearch = itemBack.getStateSearch();
			if (typeFrag.equals(SINGER)) {
				viewBack.setVisiable(View.GONE);
				searchView.setRequestLayout();
				// -------------//
				singerView.setActive();
				fragmentBase = new TouchFragmentSinger();
				Bundle bundle = new Bundle();
				bundle.putString("data", search);
				bundle.putInt("state", statesearch);
				bundle.putInt("idxPos", itemBack.getPosition());
				fragmentBase.setArguments(bundle);
				mainListener = (OnMainListener) fragmentBase;
				fragmentTransaction.replace(R.id.fragmentLayout, fragmentBase,
						SINGER);
				stateSearchView = statesearch;
				searchView.setLayoutSearchView(TouchSearchView.SINGER,
						getString(R.string.search_1b), -1, SINGER, search,
						statesearch);
				if (search.equals("")) {
					layoutDeleteSearchView.setVisibility(View.INVISIBLE);
				} else {
					layoutDeleteSearchView.setVisibility(View.VISIBLE);
				}
			} else if (typeFrag.equals(MUSICIAN)) {
				viewBack.setVisiable(View.GONE);
				searchView.setRequestLayout();
				// -------------//
				musicianView.setActive();
				fragmentBase = new TouchFragmentMusician();
				Bundle bundle = new Bundle();
				bundle.putString("data", search);
				bundle.putInt("state", statesearch);
				bundle.putInt("idxPos", itemBack.getPosition());
				fragmentBase.setArguments(bundle);
				mainListener = (OnMainListener) fragmentBase;
				fragmentTransaction.replace(R.id.fragmentLayout, fragmentBase,
						MUSICIAN);
				stateSearchView = statesearch;
				searchView.setLayoutSearchView(TouchSearchView.SINGER,
						getString(R.string.search_1c), -1, SINGER, search,
						statesearch);
				if (search.equals("")) {
					layoutDeleteSearchView.setVisibility(View.INVISIBLE);
				} else {
					layoutDeleteSearchView.setVisibility(View.VISIBLE);
				}
			} else if (typeFrag.equals(SONGTYPE)) {
				viewBack.setVisiable(View.GONE);
				searchView.setRequestLayout();
				// -------------//
				typeView.setActive();
				mainListener = null;
				fragmentBase = new TouchFragmentSongType();
				fragmentTransaction.replace(R.id.fragmentLayout, fragmentBase,
						SONGTYPE);
				((TouchFragmentSongType)fragmentBase).setOnNumberSongTypeListener(
						new OnNumberSongTypeListener() {
					@Override public void OnNumberListener(int sum) {
						/*
						searchView.setLayoutSearchView(SearchView.SONGTYPE, "", -1,
								SONGTYPE, "", -1);
						*/
						searchView.setSongType(sum);
					}
				});
			} else if (typeFrag.equals(LANGUAGE)) {
				viewBack.setVisiable(View.GONE);
				searchView.setRequestLayout();
				// -------------//
				// langagueView.setActive();
				fragmentBase = new TouchFragmentLanguage();
				mainListener = (OnMainListener) fragmentBase;
				fragmentTransaction.replace(R.id.fragmentLayout, fragmentBase,
						LANGUAGE);
				searchView.setLayoutSearchView(TouchSearchView.NOLAYOUT, "", -1,
						LANGUAGE, "", -1);
			} else if (typeFrag.equals(SONG)) {
				String typeFrag2 = itemBack.getTypeFrag2();
				String idFrag2 = itemBack.getIdFrag2();
				fragmentBase = new TouchFragmentSong();
				mainListener = (OnMainListener) fragmentBase;
				Bundle bundle = new Bundle();
				bundle.putBoolean("data", true);
				bundle.putString("id", idFrag2);
				bundle.putString("type", typeFrag2);
				bundle.putString("search", search);
				bundle.putInt("state", statesearch);
				if(typeFrag2.equals(SONGTYPE)){
					bundle.putBoolean("clickTheloai", true);		
				}
				fragmentBase.setArguments(bundle);
				fragmentTransaction.replace(R.id.fragmentLayout, fragmentBase,
						SONG);
				// --------------//

//				MyLog.e(TAB, "typeFrag2 - : " + typeFrag2);

				if (typeFrag2.equals("")) {
					songView.setActive();
				} else if (typeFrag2.equals(SINGER)) {
					viewBack.setLayout(SINGER, true);
					singerView.setActive();
				} else if (typeFrag2.equals(MUSICIAN)) {
					viewBack.setLayout(MUSICIAN, true);
					musicianView.setActive();
				} else if (typeFrag2.equals(SONGTYPE)) {
					viewBack.setLayout(SONGTYPE, true);
					typeView.setActive();
				} else if (typeFrag2.equals(LANGUAGE)) {
					viewBack.setLayout(LANGUAGE, true);
					// langagueView.setActive();
				}
				// --------------//
				if (typeFrag2.equals(SINGER) || typeFrag2.equals(MUSICIAN)) {
					searchView.setLayoutSearchView(TouchSearchView.NOLAYOUT,
							getString(R.string.search_1a), -1, typeFrag2, "",
							itemBack.getStateSearch());
				} else if (typeFrag2.equals(SONGTYPE)
						|| typeFrag2.equals(LANGUAGE) || typeFrag2.equals("")) {
					stateSearchView = statesearch;
					searchView.setLayoutSearchView(TouchSearchView.SONG,
							getString(R.string.search_1a), -1, SONG, search,
							statesearch);
					if (search.equals("")) {
						layoutDeleteSearchView.setVisibility(View.INVISIBLE);
					} else {
						layoutDeleteSearchView.setVisibility(View.VISIBLE);
					}
				}
			} else if (typeFrag.equals(FAVOURITE)) {
				viewBack.setVisiable(View.VISIBLE);
				viewBack.setLayout(MUSICIAN, false);
				searchView.setRequestLayout();
				// ---------------//
				favouriteView.setActive();
				int sum = DBInterface.DBGetFavouriteSongList(0, 0, context)
						.size();
				searchView.setLayoutSearchView(TouchSearchView.PLAYLIST, "", sum,
						FAVOURITE, "", -1);
				mainListener = null;
				fragmentBase = new TouchFragmentFavourite();
				mainListener = (OnMainListener) fragmentBase;
				fragmentTransaction.replace(R.id.fragmentLayout, fragmentBase,
						FAVOURITE);
			} else if (typeFrag.equals(PLAYLIST)) {
				viewBack.setVisiable(View.VISIBLE);
				viewBack.setLayout(MUSICIAN, false);
				searchView.setRequestLayout();
				// ---------------//
				groupView.setActivePlayList();
				ShowSongFragment(PLAYLIST, null);
			} else if (typeFrag.equals(REMIX)) {
				viewBack.setVisiable(View.VISIBLE);
				viewBack.setLayout(MUSICIAN, false);
				searchView.setRequestLayout();
				// ---------------//
				remixView.setActive();
				fragmentBase = new TouchFragmentRemix();
				Bundle bundle = new Bundle();
				bundle.putInt("state", statesearch);
				bundle.putString("search", search);
				fragmentBase.setArguments(bundle);
				mainListener = (OnMainListener) fragmentBase;
				fragmentTransaction.replace(R.id.fragmentLayout, fragmentBase,
						REMIX);
				stateSearchView = statesearch;
				searchView.setLayoutSearchView(TouchSearchView.SONG,
						getString(R.string.search_1a), -1, SONG, search, statesearch);
				if (search.equals("")) {
					layoutDeleteSearchView.setVisibility(View.INVISIBLE);
				} else {
					layoutDeleteSearchView.setVisibility(View.VISIBLE);
				}
			} else if (typeFrag.equals(HOTSONG)) {
				viewBack.setVisiable(View.VISIBLE);
				viewBack.setLayout(MUSICIAN, false);
				searchView.setRequestLayout();
				// ---------------//
				hotSongTypeView.setActive();
				fragmentBase = new FragmentHotSong();
				Bundle bundle = new Bundle();
				bundle.putInt("state", statesearch);
				bundle.putString("search", search);
				fragmentBase.setArguments(bundle);
				mainListener = (OnMainListener) fragmentBase;
				fragmentTransaction.replace(R.id.fragmentLayout, fragmentBase,
						HOTSONG);
				stateSearchView = statesearch;
				searchView.setLayoutSearchView(TouchSearchView.SONG,
						getString(R.string.search_1a), -1, SONG, search, statesearch);
				if (search.equals("")) {
					layoutDeleteSearchView.setVisibility(View.INVISIBLE);
				} else {
					layoutDeleteSearchView.setVisibility(View.VISIBLE);
				}
			}
			SAVETYPE = typeFrag;
			fragmentTransaction.commit();
		} else {}
		if (((MyApplication) context).checkListBack()) {
			groupView.setExit(TouchMyGroupView.BACK);
		} else {
			groupView.setExit(TouchMyGroupView.EXIT);
			viewBack.setVisiable(View.GONE);
			searchView.setRequestLayout();
		}
		hideKeyBoard();
	}
	
	private Dialog dialogExit;
	private TouchPopupViewExitApp popupExit;

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
//				if(skServer.getModelDevice() == 1){
//					((MyApplication) getApplication()).disconnectFromRemoteHost();
//					((MyApplication) getApplication()).connectToRemoteHost(ip, pass);
//				} else {
//					ShowDevice(name , ip);
//				}
				
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
		// MyLog.e(TAB, "OnBackLayout()");
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
			settings.saveListOfflineVersion_SK90xx_New("");
			settings.saveLuckyDataVersion(0);
			settings.saveLuckyImageVersion(0);
			settings.saveSambaDataVersion(0);
			
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
			OnCloseKeyBoard();
			return;
		}
		
		if (IP.equals("1.6.1.0.0") && !Pass.equals("")) {
			OnCloseKeyBoard();
			setOnOffUserList(Pass, !MyApplication.bOffUserList);
			return;				
		}
		if (IP.equals("1.6.1.0.1") && Pass.equals("8888")) {
			OnCloseKeyBoard();
			return;
		}
		
		if (IP.equals("1.6.1.0.2") && Pass.equals("8888")) {
			OnCloseKeyBoard();
			int w = getResources().getDisplayMetrics().widthPixels;
			int h = getResources().getDisplayMetrics().heightPixels;
			float result = (float)w/h;
			Toast.makeText(this, "w/h ==== " + result, Toast.LENGTH_LONG)
					.show();
			return;
		}
		
		if (IP.equals("1.6.5.0.2") && Pass.equals("8888")) {
			OnCloseKeyBoard();
			drawerLayout.closeDrawers();
			MyApplication.intCommandMedium = 0x00000000;
			MyApplication.intSavedCommandMedium = 0x00000000;
			MyApplication.intCommandEnable = 0xffff;
			MyApplication.flagModelA = false;
			setOnOffAdminControl();
			return;
		}
		
		if (IP.equals("1.8.0.0.1") && Pass.equals("8888")) {
			OnCloseKeyBoard();
			drawerLayout.closeDrawers();
			startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER));
			return;
		}
		
		if (IP.equals("1.8.0.0.2") && !Pass.equals("")) {
			OnCloseKeyBoard();
			drawerLayout.closeDrawers();
			MyApplication.intCommandMedium = Integer.parseInt(Pass);
			setOnOffControlFull();
			return;
		}
		
		if (IP.equals("1.8.5.0.1") && Pass.equals("8888")) {
			OnCloseKeyBoard();
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
		if (IP.equals("1.9.0.0.1") && !Pass.equals("")) {
			OnCloseKeyBoard();
			return;
		}		
		if (IP.equals("1.9.0.0.2") && !Pass.equals("")) {
			OnCloseKeyBoard();
			isDownloadingPic = false;
			isDownloadingToc = false;
			isDownloadingLyric = false;
			makeToastMessage("reset all downloading state");
			return;
		}
		if (IP.equals("1.9.5.0.1") && !Pass.equals("")) {
			OnCloseKeyBoard();
			int value = Integer.parseInt(Pass);
			MyApplication.setCommandMediumScoreMethod(value);
			setOnOffControlFull();
			makeToastMessage("set score method = " + value);
			return;
		}
		if (IP.equals("1.9.5.0.5") && Pass.equals("8888")) {
			OnCloseKeyBoard();
			MyApplication.flagEverConnect = !MyApplication.flagEverConnect;
			SharedPreferences settingFlagConnect = getSharedPreferences(
					PREFS_FLAGHDD, 0);
			settingFlagConnect.edit().putBoolean("isEverConnect2", MyApplication.flagEverConnect)
					.commit();
			makeToastMessage("SET ON EVER CONNECT = " + MyApplication.flagEverConnect);
			return;
		}
		
		if (IP.equals("1.9.5.0.3") && Pass.equals("8888")) {
			OnCloseKeyBoard();
			forceUpdatingFirmware();
			return;
		}
		
		if (IP.equals("1.9.8.0.2") && Pass.equals("8888")) {
			OnCloseKeyBoard();
			drawerLayout.closeDrawers();
			SaveFirmware saveFirmware = SaveFirmware.getInstance(context);
			saveFirmware.clearVersionFirmware();
			makeToastMessage("reset firmware version");
			return;
		}
		
		if (IP.equals("1.9.8.0.1") && Pass.equals("8888")) {
			OnCloseKeyBoard();			
			SaveFirmware saveFirmware = SaveFirmware.getInstance(context);
			String str = saveFirmware.getVersionFirmwareHiW() + " -- " + saveFirmware.getRevisionFirmwareHiW() + " \n" +
					saveFirmware.getVersionFirmwareKM1() + " -- " + saveFirmware.getRevisionFirmwareKM1();
			makeToastMessage(str);
			return;
		}
		
		if (IP.equals("2.1.0.0.2") && Pass.equals("8888")) {
			OnCloseKeyBoard();
			if(drawerLayout != null){
				drawerLayout.closeDrawers();
			}
			
			onUpdateVideoLyric(1);
			return;
		}
		
		if (IP.equals("2.1.0.0.3") && Pass.equals("8888")) {
			OnCloseKeyBoard();
			AppSettings setting = AppSettings
					.getInstance(getApplicationContext());
			setting.saveVideoLyricSize(0);		
			makeToastMessage("Reset Video size");
			return;
		}
		
		if (IP.equals("2.1.1.0.1") && Pass.equals("8888")) {
			OnCloseKeyBoard();
			processGetScoreInfo();		
			return;
		}
		
		if (IP.equals("2.1.3.0.1") && Pass.equals("8888")) {
			OnCloseKeyBoard();
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			if (fragmentBase != null && SAVETYPE == SONG) {
				((TouchFragmentSong)fragmentBase).forceCloseDB();
			}
			fragmentTransaction.commit();
			processNewSongTable();	
			reloadSongList();	
			makeToastMessage("processNewSongTable");
			return;
		}
		
		if (IP.equals("2.1.3.0.2") && Pass.equals("8888")) {
			OnCloseKeyBoard();
			MyApplication.flagNewSongTable = !MyApplication.flagNewSongTable;
			reloadSongList();		
			makeToastMessage("flagNewSongTable = " + MyApplication.flagNewSongTable);
			return;
		}
		
		if (IP.equals("2.1.3.0.3") && !Pass.equals("")) {
			OnCloseKeyBoard();
			MyApplication.bOffUserList = !MyApplication.bOffUserList;
			reloadSongList();		
			makeToastMessage("bOffUserList = " + MyApplication.bOffUserList);
			return;
				
		}
		
		if (IP.equals("2.2.1.0") && Pass.equals("8888")) {
			processGetLyricVidLink();
			return;
		}
		
		if (IP.equals("2.7.0.1") && Pass.equals("8888")) {
			ArrayList<Integer> listTemp = new ArrayList<Integer>();
			listTemp.add(0);
			((MyApplication)context).sendHiddenList_90xx(listTemp, 0);
			makeToastMessage("set list hidden empty");			
			return;
		}
		
		if (IP.equals("2.0.1.7") && Pass.equals("8888")) {
			((MyApplication)context).loadListDataApp(context);
			makeToastMessage("Start check update");			
			return;
		}
		
		if (IP.equals("2.0.1.7") && Pass.equals("1111")) {
			makeToastMessage("Start ping dau may");
			if (processAuthenTask != null) {
				processAuthenTask.cancel(true);
				processAuthenTask = null;
			}
			AppSettings setting = AppSettings.getInstance(getApplicationContext());
			setting.setMCountAuthen(0);
			setting.setMCountSuccess(0);
			setting.setMCountFail(0);
			
			String dateString = getCurrentTimeStamp();
			
			callAuthen(2000, "logFail_" + dateString + ".txt");		
			return;
		}
		
		if (IP.equals("2.0.1.7") && Pass.equals("2222")) {
			AppSettings setting = AppSettings.getInstance(getApplicationContext());
			String str = setting.loadServerIP() + "\nLan thu: " + setting.getMCountAuthen();
			str += "\nLan success: " + setting.getMCountSuccess();
			str += "\nLan fail: " + setting.getMCountFail();
			
			new MoonFileUtilities(context).writeAppend("logCurrent.txt", "\n==============\n" + str);
			
			makeToastMessage(str);
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

	// ---------------------KeyBoardFragment-----------------------//

	@Override
	public void OnKeyboardView(String name) {
		MyLog.i(TAB, "OnKeyboardView : " + name);
		if(name.equals("OK")){
			searchView.setActiveSearch(false);
			hideKeyBoard();
			return;
		}
		char key = name.charAt(0); 
		if (name == GroupKeyBoard.CLEAR) {
			searchView.clearCharacterSearchView();
		} else {
			if(name.equals(GroupKeyBoard.SPACE)){
				key = ' ';
			}
			String string = searchView.getTextSearch() + key;
			if (!string.trim().equals("")) {
				searchView.insertCharacterSearchView(key);
			}
		}

		String str = searchView.getTextSearch();
//		MyLog.e(TAB, "searchView - " + str);
		if (str.trim().equals("")) {
			layoutDeleteSearchView.setVisibility(View.INVISIBLE);
		} else {
			layoutDeleteSearchView.setVisibility(View.VISIBLE);
		}

		if (mainKeyboardListener != null) {
			mainKeyboardListener.OnTextShowing(searchView.getTextSearch());
		}
	}
	
	private Timer timerAcceptKeyBoard;
	private void startTimerAcceptKeyboard(final int state1, final int state2, final String search){
		if(timerAcceptKeyBoard != null){
			timerAcceptKeyBoard.cancel();
			timerAcceptKeyBoard = null;
		}
		timerAcceptKeyBoard = new Timer();
		timerAcceptKeyBoard.schedule(new TimerTask() {
			
			@Override
			public void run() {
				handler.sendEmptyMessage(0);
			}
			
			private Handler handler = new Handler(){
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					if (mainListener != null) {
						if(stateSearchView != state2){
							hideKeyBoard();
						}
						stateSearchView = state2;
						mainListener.OnSearchMain(state1, state2, search);
					}
				}
			};
		}, 1);
		
	}

	// ---------------------SearchView-----------------------//

	private int stateSearchView;
	private String edSearchOldStr = "";
	
	@Override
	public void OnSearch(final int state1, final int state2, final String search) {
		
		if(MyApplication.flagRealKeyboard){
			if(search.equals("")){
				edSearchOldStr = "";
				if(searchEditText != null){
					searchEditText.setText("");
				}
			}
			
			if (search.length() > 0) {
				deleteSearchView.setVisibility(View.VISIBLE);
			} 
					
			String str = searchView.getTextSearch();
			if (str.trim().equals("")) {
				layoutDeleteSearchView.setVisibility(View.INVISIBLE);
			} else {
				layoutDeleteSearchView.setVisibility(View.VISIBLE);
			}		
					
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					if (mainListener != null) {
						if(stateSearchView != state2){
							hideKeyBoard();
						}
						
						String mSearch = search;
						try {
							mSearch = mSearch.toUpperCase();
						} catch (Exception e) {
							
						}
						
						stateSearchView = state2;
						mainListener.OnSearchMain(state1, state2, mSearch);
					}
				}
			}, 200);
		} else {
			if (search.length() > 0) {
				deleteSearchView.setVisibility(View.VISIBLE);
			} 
			
			if (mainListener != null) {
				if(stateSearchView != state2){
					hideKeyBoard();
				}
				stateSearchView = state2;
				mainListener.OnSearchMain(state1, state2, search);
			}
			
//			startTimerAcceptKeyboard(state1, state2, search);
		}
		
	}

	@Override
	public void OnShowKeyBoard() {
		showKeyBoard();
	}

	@Override
	public void OnCloseKeyBoard() {
		deleteSearchView.setVisibility(View.INVISIBLE);
		hideKeyBoard();
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
//		MyLog.e(TAB, "deviceDidAuthenWithServer()");
		if (response == null || response.getStatus() != 0) {
			MyLog.e("", "Device Authenticate with server failed");
			if (response != null) {
				// Log.e("", "reason: " + new
				// String(response.getErrorMessage()));
			}			
			hideKeyBoard();
			drawerLayout.openDrawer(layoutConnect);
			layoutDownload.setVisibility(View.GONE);
			layoutAnimation.setVisibility(View.VISIBLE);
			animationView.ShowMessage(TouchAnimationView.PASS);
			SKServer skServer = ((MyApplication)getApplication()).getDeviceCurrent();
			if(skServer != null){
				ShowDevice(skServer.getName(), skServer.getConnectedIPStr());
			}
			((MyApplication)getApplication()).setDeviceCurrent(new SKServer());
//			MyLog.e(TAB, "ShowMessage() - WIFI");
			// show password input
			return;
		}
		
		MyApplication.flagSmartK_CB = false;
		MyApplication.flagSmartK_801 = false;
		MyApplication.flagSmartK_KM4 = false;
		
		flagDownloadingDD = false;
		
		saveLuckyDataVersion = 0;
		saveLuckyImageVersion = 0;
		saveListHiddenVersion = 0;
		saveListOfflineVersion = 0;
		saveListOfflineVersionByte = new byte[32];
		saveSambaDataVersion = 0;
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
		searchView.invalidate();
		
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
		
//		animationView.ShowMessage(TouchAnimationView.NOTHING);
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
//			intTocHDDVol = 0;
//			intTocDISCVol = 0;
		}
		
		SKServer skServer = ((MyApplication) getApplication())
				.getDeviceCurrent();
		
		AppSettings setting = AppSettings.getInstance(getApplicationContext());
		if(skServer != null){
			skServer.setModelDevice(MyApplication.intSvrModel);
			((MyApplication) this.getApplication()).SaveDevice(skServer);
			
			//skServer.setIrcRemote(setting.loadIrcRemote());
			
//			if(skServer.getModelDevice() != 1){
			setting.saveLastServerName(skServer.getName());
			setting.saveServerIP(skServer.getConnectedIPStr());
			setting.saveServerPass(skServer.getConnectPass());
			setting.saveModeModel(skServer.getModelDevice());
			setting.saveIrcRemote(skServer.getIrcRemote());
			setting.saveNameRemote(skServer.getNameRemote());
			skServer.setState(SKServer.CONNECTED);
//			}	
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
			
//			if (skServer != null) {
//				int irc = skServer.getIrcRemote();
//				if (irc == -1) {
//					String name = skServer.getName();
//					String ip = skServer.getConnectedIPStr();
//					ShowModel(name, ip);
//					if (!drawerLayout.isDrawerOpen(layoutConnect)) {
//						drawerLayout.openLeftDrawer(layoutConnect);
//					}
//				} else {
//					drawerLayout.closeDrawer(layoutConnect);
//					String name = skServer.getNameRemote();
//					MyApplication.intRemoteModel = irc;
//					groupView.setNameRemote(name);
//				}
//			}
			
			SharedPreferences sharedselect = getSharedPreferences("sharedselect", Context.MODE_PRIVATE);
			MyApplication.intSelectList = sharedselect.getInt("intselect", 0);
			
			if(MyApplication.intSelectList == 0){
				MyApplication.intSelectList = MyApplication.SelectList_SONCA;
				
				SharedPreferences.Editor editorSelect = sharedselect.edit();
				editorSelect.putInt("intselect", MyApplication.intSelectList);
				editorSelect.commit();
			}
			
			MyApplication.intRemoteModel = 0;
			groupView.setNameRemote("ACNOS");
			
			MyApplication.intWifiRemote = MyApplication.SONCA_KARTROL;
			ChangeWifiRmote();
			
			MyApplication.mSongTypeList = null;
			processTotalSongType(100);
			
			if (allowTimerPing != true) {
				allowTimerPing = true;
			}

			layoutAnimation.setVisibility(View.VISIBLE);
			groupView.setConnected(TouchMyGroupView.CONNECTED,
					skServer.getName(), skServer);

			if (flagRunHide) {
				hideActionBar();
			}

			((MyApplication) getApplication())
					.setListActive(new ArrayList<Song>());

			ToFragmentDance(false);

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
		groupView.setConnected(TouchMyGroupView.CONNECTED, name,
				skServer);

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
		
		// FILE YOUTUBE ONLINE
		boolean isNeedupdateYTFile = false;
		if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
			int appYTVersion = setting.getYouTubeVersion();
			int serverYTVersion = 0;
			
			String rootPath = android.os.Environment
					.getExternalStorageDirectory()
					.toString()
					.concat(String.format("/%s/%s", "Android/data",
							getPackageName()));
			String myPath = rootPath.concat("/YOUTUBE/" + MyApplication.YT_FILENAME);
			File myFile = new File(myPath);
			
			try {
				serverYTVersion = ByteUtils.byteToInt32(responseData, 24);	
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(serverYTVersion == 0){
				if (myFile.exists()) {
					myFile.delete();
				}
				setting.saveYouTubeVersion(0);
			}
			
			if (!myFile.exists()) {
				appYTVersion = 0;
			}
			
			if(serverYTVersion != appYTVersion){
				isNeedupdateYTFile = true;
				saveYTVersion = serverYTVersion;
			}
			
			MyLog.d(" ", "appYTVersion = " + appYTVersion);
			MyLog.d(" ", "serverYTVersion = " + serverYTVersion);
		}
		
		if(MyApplication.intSvrCode >= MyApplication.newAPI90xxVersion && MyApplication.intSvrModel == MyApplication.SONCA){
			int appYTVersion = setting.getYouTubeVersion_SK90xx();
			int serverYTVersion = 0;
			
			String rootPath = android.os.Environment
					.getExternalStorageDirectory()
					.toString()
					.concat(String.format("/%s/%s", "Android/data",
							getPackageName()));
			String myPath = rootPath.concat("/YOUTUBE/" + MyApplication.YT_90xx);
			File myFile = new File(myPath);
			
			try {
				serverYTVersion = ByteUtils.byteToInt32(responseData, 20);	
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(serverYTVersion == 0){
				if (myFile.exists()) {
					myFile.delete();
				}
				setting.saveYouTubeVersion_SK90xx(0);
			}
			
			if (!myFile.exists()) {
				appYTVersion = 0;
			}
			
			if(serverYTVersion != appYTVersion){
				isNeedupdateYTFile = true;
				saveYTVersion = serverYTVersion;
			}
			
			MyLog.d(" ", "appYTVersion 90xx = " + appYTVersion);
			MyLog.d(" ", "serverYTVersion 90xx = " + serverYTVersion);
		}
		
		MyApplication.intVersionDownloaded = 0;
		
		// LIST BAI DA DOWN
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
			
			try {
				serverListOfflineVersion = ByteUtils.byteToInt32(responseData, 32);	
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(serverListOfflineVersion == 0){
				if (myFile.exists()) {
					myFile.delete();
				}
				setting.saveListOfflineVersion(0);
			}
			
			if (!myFile.exists()) {
				appListOfflineVersion = 0;
			}			
			
			if(serverListOfflineVersion != appListOfflineVersion){
				isNeedupdateOfflineFile = true;
				saveListOfflineVersion = serverListOfflineVersion;
			}
			
			MyApplication.intVersionDownloaded = appListOfflineVersion;			
			
			MyLog.d(" ", "appListOfflineVersion = " + appListOfflineVersion);
			MyLog.d(" ", "serverListOfflineVersion = " + serverListOfflineVersion);
		}
		
		if(MyApplication.intSvrCode >= MyApplication.newAPI90xxVersion && MyApplication.intSvrModel == MyApplication.SONCA				
				&& MyApplication.intSvrCode < MyApplication.new9018API_downloadVersion){				
			int appListOfflineVersion = setting.getListOfflineVersion_SK90xx();
			int serverListOfflineVersion = 0;

			String rootPath = android.os.Environment
					.getExternalStorageDirectory()
					.toString()
					.concat(String.format("/%s/%s", "Android/data",
							getPackageName()));
			String myPath = rootPath.concat("/YOUTUBE/" + MyApplication.YT_90xx_OFFLINE);
			File myFile = new File(myPath);
			
			try {
				serverListOfflineVersion = ByteUtils.byteToInt8(responseData, 24);	
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(serverListOfflineVersion == 0){
				if (myFile.exists()) {
					myFile.delete();
				}
				setting.saveListOfflineVersion_SK90xx(0);
			}
			
			if (!myFile.exists()) {
				appListOfflineVersion = 0;
			}
			
			if(serverListOfflineVersion != appListOfflineVersion){
				isNeedupdateOfflineFile = true;
				saveListOfflineVersion = serverListOfflineVersion;
			}
			
			MyApplication.intVersionDownloaded = appListOfflineVersion;
			
			MyLog.d(" ", "appListOfflineVersion 90xx = " + appListOfflineVersion);
			MyLog.d(" ", "serverListOfflineVersion 90xx = " + serverListOfflineVersion);
		}
		
		if(MyApplication.intSvrCode >= MyApplication.new9018API_downloadVersion && MyApplication.intSvrModel == MyApplication.SONCA){
			byte[] bAppListOfflineVersion = new byte[32];
			try {
				String strTemp = setting.getListOfflineVersion_SK90xx_New();
				if(!strTemp.equals("")){
					bAppListOfflineVersion = Base64.decode(strTemp, Base64.DEFAULT);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			byte[] bServerListOfflineVersion = new byte[32];
			
			String rootPath = android.os.Environment
					.getExternalStorageDirectory()
					.toString()
					.concat(String.format("/%s/%s", "Android/data",
							getPackageName()));
			String myPath = rootPath.concat("/YOUTUBE/" + MyApplication.YT_90xx_OFFLINE);
			File myFile = new File(myPath);
			
			// get 32 bytes
			try {
				bServerListOfflineVersion = Arrays.copyOfRange(responseData, 32, 32 + 32);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(checkEmptyArray(bServerListOfflineVersion)){
				if (myFile.exists()) {
					myFile.delete();
				}
				setting.saveListOfflineVersion_SK90xx_New("");
			}
			
			if (!myFile.exists()) {
				bAppListOfflineVersion = new byte[32];
			}
			
			if(Arrays.equals(bServerListOfflineVersion, bAppListOfflineVersion) == false){
				isNeedupdateOfflineFile = true;
				saveListOfflineVersionByte = bServerListOfflineVersion;
			}
			
			MyApplication.bVersionDownloaded = bAppListOfflineVersion;
							
			MyLog.d(" ", "appListOfflineVersion 90xx = " + bytesToHex2(bAppListOfflineVersion));
			MyLog.d(" ", "serverListOfflineVersion 90xx = " + bytesToHex2(bServerListOfflineVersion));				
		}
		
		// LIST HIDDEN
		isNeedupdateHiddenFile = false;
		if(MyApplication.intSvrCode >= MyApplication.newAPI90xxVersion && MyApplication.intSvrModel == MyApplication.SONCA){				
			int appListHiddenVersion = setting.getListHiddenVersion_SK90xx();
			int serverListHiddenVersion = 0;
			
			String rootPath = android.os.Environment
					.getExternalStorageDirectory()
					.toString()
					.concat(String.format("/%s/%s", "Android/data",
							getPackageName()));
			String myPath = rootPath.concat("/YOUTUBE/" + MyApplication.YT_90xx_HIDDEN);
			File myFile = new File(myPath);
			
			try {
				serverListHiddenVersion = ByteUtils.byteToInt32L(responseData, 28);	
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			if(serverListHiddenVersion == 0){
				if (myFile.exists()) {
					myFile.delete();
				}
				setting.saveListHiddenVersion_SK90xx(0);
			}
			
			if (!myFile.exists()) {
				appListHiddenVersion = 0;
			}
			
			if(serverListHiddenVersion != appListHiddenVersion){
				isNeedupdateHiddenFile = true;
				saveListHiddenVersion = serverListHiddenVersion;
			}
			
			MyLog.d(" ", "appListHiddenVersion 90xx = " + appListHiddenVersion);
			MyLog.d(" ", "serverListHiddenVersion 90xx = " + serverListHiddenVersion);
		}
		
		// LIST VONG XOAY
		isNeedupdateLuckyData = false;
		if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
			int appLuckyDataVersion = setting.getLuckyDataVersion();
			int serverLuckyDataVersion = 0;
			
			String rootPath = android.os.Environment
					.getExternalStorageDirectory()
					.toString()
					.concat(String.format("/%s/%s", "Android/data",
							getPackageName()));
			String myPath = rootPath.concat("/VONGXOAY/" + MyApplication.LIST_LUCKYDATA);
			File myFile = new File(myPath);
			
			try {
				serverLuckyDataVersion = ByteUtils.byteToInt32(responseData, 36);	
			} catch (Exception e) {
				
			}
			
			if(serverLuckyDataVersion == 0){
				if (myFile.exists()) {
					myFile.delete();
				}
				setting.saveLuckyDataVersion(0);
			}
			
			if (!myFile.exists()) {
				appLuckyDataVersion = 0;
			}
			
			if(serverLuckyDataVersion != appLuckyDataVersion){
				isNeedupdateLuckyData = true;
				saveLuckyDataVersion = serverLuckyDataVersion;
			}
			
			MyLog.d(" ", "appLuckyDataVersion = " + appLuckyDataVersion);
			MyLog.d(" ", "serverLuckyDataVersion = " + serverLuckyDataVersion);
		}
		
		// LOGO VONG XOAY
		isNeedupdateLuckyImage = false;
		if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
			int appLuckyImageVersion = setting.getLuckyImageVersion();
			int serverLuckyImageVersion = 0;
			
			String rootPath = android.os.Environment
					.getExternalStorageDirectory()
					.toString()
					.concat(String.format("/%s/%s", "Android/data",
							getPackageName()));
			String myPath = rootPath.concat("/VONGXOAY/" + MyApplication.LIST_LUCKYIMAGE);
			File myFile = new File(myPath);
			
			try {
				serverLuckyImageVersion = ByteUtils.byteToInt32(responseData, 40);	
			} catch (Exception e) {
				
			}
			
			if(serverLuckyImageVersion == 0){
				if (myFile.exists()) {
					myFile.delete();
				}
				setting.saveLuckyImageVersion(0);
			}
			
			if (!myFile.exists()) {
				appLuckyImageVersion = 0;
			}
			
			if(serverLuckyImageVersion != appLuckyImageVersion){
				isNeedupdateLuckyImage = true;
				saveLuckyImageVersion = serverLuckyImageVersion;
			}
			
			MyLog.d(" ", "appLuckyImageVersion = " + appLuckyImageVersion);
			MyLog.d(" ", "serverLuckyImageVersion = " + serverLuckyImageVersion);
		}
		
		// SAMBA DATA
		isNeedupdateSambaData = false;
		if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
			int appSambaDataVersion = setting.getSambaDataVersion();
			int serverSambaDataVersion = 0;
			
			String rootPath = android.os.Environment
					.getExternalStorageDirectory()
					.toString()
					.concat(String.format("/%s/%s", "Android/data",
							getPackageName()));
			String myPath = rootPath.concat("/" + MyApplication.ADD_FILE_SAMBA);
			File myFile = new File(myPath);
			
			try {
				serverSambaDataVersion = ByteUtils.byteToInt32(responseData, 44);	
			} catch (Exception e) {
				
			}
			
			if(serverSambaDataVersion == 0){
				if (myFile.exists()) {
					myFile.delete();
				}
				setting.saveSambaDataVersion(0);
			}
			
			if (!myFile.exists()) {
				appSambaDataVersion = 0;
			}
			
			if(serverSambaDataVersion != appSambaDataVersion){
				isNeedupdateSambaData = true;
				saveSambaDataVersion = serverSambaDataVersion;
			}
			
			MyLog.d(" ", "appSambaDataVersion = " + appSambaDataVersion);
			MyLog.d(" ", "serverSambaDataVersion = " + serverSambaDataVersion);
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
		
		if (!isNeedUpdateTocFlag && !isNeedupdateYTFile && !isNeedupdateOfflineFile && !isNeedupdateSambaData 
				&& !isNeedupdateLuckyData && !isNeedupdateLuckyImage && !isNeedupdateHiddenFile) {
			MyLog.e("RUN LUON","......................");
			// if(setting.isUpdated()) {
			// Database up to date
			setActiveView(SAVETYPE);
			serverStatus = null;		
			// ToFragmentDance(false);

//			if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK || 
//					(MyApplication.intSvrModel == MyApplication.SONCA && MyApplication.intSvrCode >= MyApplication.newAPI90xxVersion)){
//				reloadYoutubeTable();
//			}
			
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
			if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK
					|| (MyApplication.intSvrCode >= MyApplication.newAPI90xxVersion && MyApplication.intSvrModel == MyApplication.SONCA)){
				
				DownloadState = -1;

				OnCloseKeyBoard();

				String savePath = rootPath.concat("/YOUTUBE/" + MyApplication.YT_FILENAME);
				if(MyApplication.intSvrModel == MyApplication.SONCA){
					savePath = rootPath.concat("/YOUTUBE/" + MyApplication.YT_90xx);
				}
				File mFile = new File(savePath);
				if (mFile.exists()) {
					mFile.delete();
				}

				allowTimerPing = false;
				if(MyApplication.intSvrModel == MyApplication.SONCA){
					((MyApplication) getApplication()).downloadSK90xxFile(savePath, MyApplication.YT_90xx);
				} else {
					((MyApplication) getApplication()).downloadYouTubeFile(savePath);					
				}
				isDownloadingDauMay = true;
			}			
			
		} else if(isNeedupdateOfflineFile){
			if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK
					|| (MyApplication.intSvrCode >= MyApplication.newAPI90xxVersion && MyApplication.intSvrModel == MyApplication.SONCA)){			
				DownloadState = -3;

				OnCloseKeyBoard();

				String savePath = rootPath.concat("/YOUTUBE/" + MyApplication.LIST_OFFLINE);
				if(MyApplication.intSvrModel == MyApplication.SONCA){
					savePath = rootPath.concat("/YOUTUBE/" + MyApplication.YT_90xx_OFFLINE);
				}
				File mFile = new File(savePath);
				if (mFile.exists()) {
					mFile.delete();
				}

				allowTimerPing = false;
				if(MyApplication.intSvrModel == MyApplication.SONCA){
					((MyApplication) getApplication()).downloadSK90xxFile(savePath, MyApplication.YT_90xx_OFFLINE);
				} else {
					((MyApplication) getApplication()).downloadOfflineFile(savePath);
				}					
				isDownloadingDauMay = true;
			}			
			
		} else if(isNeedupdateLuckyData){
			if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
				DownloadState = -4;

				OnCloseKeyBoard();

				String savePath = rootPath.concat("/VONGXOAY/" + MyApplication.LIST_LUCKYDATA);
				File mFile = new File(savePath);
				if (mFile.exists()) {
					mFile.delete();
				}

				allowTimerPing = false;
				((MyApplication) getApplication()).downloadLuckyData(savePath);
				isDownloadingDauMay = true;
			}			
			
		} else if(isNeedupdateLuckyImage){
			if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
				DownloadState = -5;

				OnCloseKeyBoard();

				String savePath = rootPath.concat("/VONGXOAY/" + MyApplication.LIST_LUCKYIMAGE);
				File mFile = new File(savePath);
				if (mFile.exists()) {
					mFile.delete();
				}

				allowTimerPing = false;
				((MyApplication) getApplication()).downloadLuckyImage(savePath);
				isDownloadingDauMay = true;
			}			
			
		} else if(isNeedupdateHiddenFile){
			DownloadState = -6;

			OnCloseKeyBoard();

			String savePath = rootPath.concat("/YOUTUBE/" + MyApplication.YT_90xx_HIDDEN + ".tmp");
			File updateFile = new File(savePath);

			allowTimerPing = false;
			((MyApplication) getApplication()).downloadHiddenFile(savePath);				
			isDownloadingDauMay = true;
		} else if(isNeedupdateSambaData){
			if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
				DownloadState = -7;

				OnCloseKeyBoard();

				String savePath = rootPath.concat("/" + MyApplication.ADD_FILE_SAMBA);
				File mFile = new File(savePath);
				if (mFile.exists()) {
					mFile.delete();
				}

				allowTimerPing = false;
				((MyApplication) getApplication()).downloadSambaData(savePath);
				isDownloadingDauMay = true;
			}			
			
		} else {
			DownloadState = 0;
			String savePath = rootPath.concat("/MEGIDX");
			allowTimerPing = false;
			
			((MyApplication) getApplication()).downloadUpdateFile(savePath);
			isDownloadingDauMay = true;
			DownloadState++;
			
		}	
		
	}
	
	private int saveYTVersion = 0;
	private int saveLuckyDataVersion = 0;
	private int saveLuckyImageVersion = 0;
	private int saveListOfflineVersion = 0;
	private byte[] saveListOfflineVersionByte = new byte[32];
	private int saveSambaDataVersion = 0;
	private int saveUpdateTocVersion = 0;
	private boolean isMainNeedUpdate = false;
	private boolean isNeedupdateOfflineFile = false;
	private boolean isNeedupdateLuckyData = false;
	private boolean isNeedupdateLuckyImage = false;
	private boolean isNeedupdateSambaData = false;
	
	private boolean isNeedupdateHiddenFile = false;
	private int saveListHiddenVersion = 0;
	
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
				groupView.setConnected(TouchMyGroupView.INCONNECT, "", null);
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
	
	public static byte[] getByteFromFile(String filePath) throws Exception {
		return readFile(new File(filePath));
	}
	
	public static byte[] readFile(File file) throws IOException {
        // Open file
        RandomAccessFile f = new RandomAccessFile(file, "r");
        try {
            // Get and check length
            long longlength = f.length();
            int length = (int) longlength;
            if (length != longlength)
                throw new IOException("File size >= 2 GB");
            // Read file and return data
            byte[] data = new byte[length];
            f.readFully(data);
            return data;
        } finally {
            f.close();
        }
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

	private long totalSize = 0;
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
				if(DownloadState == -7){
					
					if(isMainNeedUpdate){
						DownloadState = 0;

						allowTimerPing = false;
						
						String mSavePath = rootPath.concat("/MEGIDX");
						((MyApplication) getApplication()).downloadUpdateFile(mSavePath);
						isDownloadingDauMay = true;
						DownloadState++;
						return;
					}
				} else if(DownloadState == -6){
					
					MyApplication.processGenerateHidden90xx(MyApplication.listHiddenSK90xx);					
					
					if(flagDownloadingHidden){
						flagDownloadingHidden = false;
						return;
					}
					
					if(isNeedupdateSambaData){
						DownloadState = -7;

						OnCloseKeyBoard();

						String mPath = rootPath.concat("/" + MyApplication.ADD_FILE_SAMBA);
						File mFile = new File(savePath);
						if (mFile.exists()) {
							mFile.delete();
						}

						allowTimerPing = false;
						((MyApplication) getApplication()).downloadSambaData(mPath);
						isDownloadingDauMay = true;
						return;
					}
					
					if(isMainNeedUpdate){
						DownloadState = 0;
						allowTimerPing = false;
						
						String mSavePath = rootPath.concat("/MEGIDX");
						((MyApplication) getApplication()).downloadUpdateFile(mSavePath);
						isDownloadingDauMay = true;
						DownloadState++;
						return;
					}
				} else if(DownloadState == -5){
					if(isNeedupdateHiddenFile){
						DownloadState = -6;

						OnCloseKeyBoard();

						String mPath = rootPath.concat("/YOUTUBE/" + MyApplication.YT_90xx_HIDDEN + ".tmp");

						allowTimerPing = false;
						((MyApplication) getApplication()).downloadHiddenFile(mPath);				
						isDownloadingDauMay = true;
						return;
					}
					
					if(isNeedupdateSambaData){
						DownloadState = -7;

						OnCloseKeyBoard();

						String mPath = rootPath.concat("/" + MyApplication.ADD_FILE_SAMBA);
						File mFile = new File(savePath);
						if (mFile.exists()) {
							mFile.delete();
						}

						allowTimerPing = false;
						((MyApplication) getApplication()).downloadSambaData(mPath);
						isDownloadingDauMay = true;
						return;
					}
					
					if(isMainNeedUpdate){
						DownloadState = 0;
						allowTimerPing = false;
						
						String mSavePath = rootPath.concat("/MEGIDX");
						((MyApplication) getApplication()).downloadUpdateFile(mSavePath);
						isDownloadingDauMay = true;
						DownloadState++;
						return;
					}
				} else if(DownloadState == -4){
					if(isNeedupdateLuckyImage){
						if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
							DownloadState = -5;

							OnCloseKeyBoard();

							String mSavePath = rootPath.concat("/VONGXOAY/" + MyApplication.LIST_LUCKYIMAGE);
							File mFile = new File(mSavePath);
							if (mFile.exists()) {
								mFile.delete();
							}

							allowTimerPing = false;
							((MyApplication) getApplication()).downloadLuckyImage(mSavePath);
							isDownloadingDauMay = true;
						}			
						return;
					}
					
					if(isNeedupdateHiddenFile){
						DownloadState = -6;

						OnCloseKeyBoard();

						String mPath = rootPath.concat("/YOUTUBE/" + MyApplication.YT_90xx_HIDDEN + ".tmp");
						allowTimerPing = false;
						((MyApplication) getApplication()).downloadHiddenFile(mPath);				
						isDownloadingDauMay = true;
						return;
					}
					
					if(isNeedupdateSambaData){
						DownloadState = -7;

						OnCloseKeyBoard();

						String mPath = rootPath.concat("/" + MyApplication.ADD_FILE_SAMBA);
						File mFile = new File(savePath);
						if (mFile.exists()) {
							mFile.delete();
						}

						allowTimerPing = false;
						((MyApplication) getApplication()).downloadSambaData(mPath);
						isDownloadingDauMay = true;
						return;
					}
					
					if(isMainNeedUpdate){
						DownloadState = 0;
						allowTimerPing = false;
						
						String mSavePath = rootPath.concat("/MEGIDX");
						((MyApplication) getApplication()).downloadUpdateFile(mSavePath);
						isDownloadingDauMay = true;
						DownloadState++;
						return;
					}
				} else if(DownloadState == -3){
					if(flagDownloadingDD){
						flagDownloadingDD = false;
						return;
					}
					
					if(isNeedupdateLuckyData){
						if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
							DownloadState = -4;

							OnCloseKeyBoard();

							savePath = rootPath.concat("/VONGXOAY/" + MyApplication.LIST_LUCKYDATA);
							File mFile = new File(savePath);
							if (mFile.exists()) {
								mFile.delete();
							}

							allowTimerPing = false;
							((MyApplication) getApplication()).downloadLuckyData(savePath);
							isDownloadingDauMay = true;
						}			
						return;
					}
					
					if(isNeedupdateLuckyImage){
						if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
							DownloadState = -5;

							OnCloseKeyBoard();

							String mSavePath = rootPath.concat("/VONGXOAY/" + MyApplication.LIST_LUCKYIMAGE);
							File mFile = new File(mSavePath);
							if (mFile.exists()) {
								mFile.delete();
							}

							allowTimerPing = false;
							((MyApplication) getApplication()).downloadLuckyImage(mSavePath);
							isDownloadingDauMay = true;
						}			
						return;
					}
					
					if(isNeedupdateHiddenFile){
						DownloadState = -6;

						OnCloseKeyBoard();

						String mPath = rootPath.concat("/YOUTUBE/" + MyApplication.YT_90xx_HIDDEN + ".tmp");

						allowTimerPing = false;
						((MyApplication) getApplication()).downloadHiddenFile(mPath);				
						isDownloadingDauMay = true;
						return;
					}
					
					if(isNeedupdateSambaData){
						DownloadState = -7;

						OnCloseKeyBoard();

						String mPath = rootPath.concat("/" + MyApplication.ADD_FILE_SAMBA);
						File mFile = new File(savePath);
						if (mFile.exists()) {
							mFile.delete();
						}

						allowTimerPing = false;
						((MyApplication) getApplication()).downloadSambaData(mPath);
						isDownloadingDauMay = true;
						return;
					}
					
					if(isMainNeedUpdate){
						DownloadState = 0;
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

						OnCloseKeyBoard();

						String myPath = rootPath.concat("/YOUTUBE/" + MyApplication.LIST_OFFLINE);
						if(MyApplication.intSvrModel == MyApplication.SONCA){
							myPath = rootPath.concat("/YOUTUBE/" + MyApplication.YT_90xx_OFFLINE);
						}
						File myFile = new File(myPath);
						if (myFile.exists()) {
							myFile.delete();
						}

						allowTimerPing = false;
						if(MyApplication.intSvrModel == MyApplication.SONCA){
							((MyApplication) getApplication()).downloadSK90xxFile(savePath, MyApplication.YT_90xx_OFFLINE);
						} else {
							((MyApplication) getApplication()).downloadOfflineFile(savePath);
						}					
						isDownloadingDauMay = true;
						return;
					}
					
					if(isNeedupdateLuckyData){
						if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
							DownloadState = -4;

							OnCloseKeyBoard();

							savePath = rootPath.concat("/VONGXOAY/" + MyApplication.LIST_LUCKYDATA);
							File mUpdateFile = new File(savePath);
							if (mUpdateFile.exists()) {
								mUpdateFile.delete();
							}

							allowTimerPing = false;
							((MyApplication) getApplication()).downloadLuckyData(savePath);
							isDownloadingDauMay = true;
						}			
						return;
					}
					
					if(isNeedupdateLuckyImage){
						if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
							DownloadState = -5;

							OnCloseKeyBoard();

							String mSavePath = rootPath.concat("/VONGXOAY/" + MyApplication.LIST_LUCKYIMAGE);
							File mFile = new File(mSavePath);
							if (mFile.exists()) {
								mFile.delete();
							}

							allowTimerPing = false;
							((MyApplication) getApplication()).downloadLuckyImage(mSavePath);
							isDownloadingDauMay = true;
						}			
						return;
					}
					
					if(isNeedupdateHiddenFile){
						DownloadState = -6;

						OnCloseKeyBoard();

						String mPath = rootPath.concat("/YOUTUBE/" + MyApplication.YT_90xx_HIDDEN + ".tmp");

						allowTimerPing = false;
						((MyApplication) getApplication()).downloadHiddenFile(mPath);				
						isDownloadingDauMay = true;
						return;
					}
					
					if(isNeedupdateSambaData){
						DownloadState = -7;

						OnCloseKeyBoard();

						String mPath = rootPath.concat("/" + MyApplication.ADD_FILE_SAMBA);
						File mFile = new File(savePath);
						if (mFile.exists()) {
							mFile.delete();
						}

						allowTimerPing = false;
						((MyApplication) getApplication()).downloadSambaData(mPath);
						isDownloadingDauMay = true;
						return;
					}
					
					if(isMainNeedUpdate){
						DownloadState = 0;
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
		
		if(DownloadState == -7){
			AppSettings settings = AppSettings.getInstance(getApplicationContext());
			settings.saveSambaDataVersion(saveSambaDataVersion);
			
			if (isMainNeedUpdate) {
				DownloadState = 0;
				allowTimerPing = false;
				
				String mSavePath = rootPath.concat("/MEGIDX");
				((MyApplication) getApplication()).downloadUpdateFile(mSavePath);
				isDownloadingDauMay = true;
				DownloadState++;
			} else {
				reloadYoutubeTable();

				setActiveView(SAVETYPE);
				serverStatus = null;
				((MyApplication) getApplication()).startSyncServerStatusThread();
			}
			
		} else if(DownloadState == -6){
			AppSettings settings = AppSettings.getInstance(getApplicationContext());
			settings.saveListHiddenVersion_SK90xx(saveListHiddenVersion);
			
			try {
				String tPath = rootPath.concat("/YOUTUBE/" + MyApplication.YT_90xx_HIDDEN + ".tmp");
				String mPath = rootPath.concat("/YOUTUBE/" + MyApplication.YT_90xx_HIDDEN);
				InputStream is = new FileInputStream(tPath);
				OutputStream os = new FileOutputStream(mPath);
				copyFile(is, os);
				is.close();
				os.flush();
				os.close();
				
				sk90xxHiddenBreak = 0;
				
			} catch (Exception e) {
				e.printStackTrace();
				
				sk90xxHiddenBreak = System.currentTimeMillis();
			}
			
			if(flagDownloadingHidden){
				MyApplication.listHiddenSK90xx = MyApplication.processGetListHidden90xx();
				
				if(dialogSK90xxHidden != null){
					dialogSK90xxHidden.sendNotifyCheckList();
				} else {
					if(serverStatus != null){
						ToFragmentDance(serverStatus.danceMode() == 1);	
					}					
				}
				
				flagDownloadingHidden = false;
				return;
			}
			
			if(isNeedupdateSambaData){
				DownloadState = -7;

				OnCloseKeyBoard();

				String mPath = rootPath.concat("/" + MyApplication.ADD_FILE_SAMBA);
				File mFile = new File(savePath);
				if (mFile.exists()) {
					mFile.delete();
				}

				allowTimerPing = false;
				((MyApplication) getApplication()).downloadSambaData(mPath);
				isDownloadingDauMay = true;
				return;
			}
			
			if (isMainNeedUpdate) {
				DownloadState = 0;
				allowTimerPing = false;
				
				String mSavePath = rootPath.concat("/MEGIDX");
				((MyApplication) getApplication()).downloadUpdateFile(mSavePath);
				isDownloadingDauMay = true;
				DownloadState++;
			} else {
				reloadYoutubeTable();

				setActiveView(SAVETYPE);
				serverStatus = null;
				((MyApplication) getApplication()).startSyncServerStatusThread();
			}
		} else if (DownloadState == -5) {
			AppSettings settings = AppSettings.getInstance(getApplicationContext());
			settings.saveLuckyImageVersion(saveLuckyImageVersion);			
			
			if(isNeedupdateHiddenFile){
				DownloadState = -6;

				OnCloseKeyBoard();

				String mPath = rootPath.concat("/YOUTUBE/" + MyApplication.YT_90xx_HIDDEN + ".tmp");
				allowTimerPing = false;
				((MyApplication) getApplication()).downloadHiddenFile(mPath);				
				isDownloadingDauMay = true;
				return;
			}	
			
			if(isNeedupdateSambaData){
				DownloadState = -7;

				OnCloseKeyBoard();

				String mPath = rootPath.concat("/" + MyApplication.ADD_FILE_SAMBA);
				File mFile = new File(savePath);
				if (mFile.exists()) {
					mFile.delete();
				}

				allowTimerPing = false;
				((MyApplication) getApplication()).downloadSambaData(mPath);
				isDownloadingDauMay = true;
				return;
			}
			
			if (isMainNeedUpdate) {
				DownloadState = 0;
				allowTimerPing = false;
				
				String mSavePath = rootPath.concat("/MEGIDX");
				((MyApplication) getApplication()).downloadUpdateFile(mSavePath);
				isDownloadingDauMay = true;
				DownloadState++;
			} else {
				reloadYoutubeTable();

				setActiveView(SAVETYPE);
				serverStatus = null;
				((MyApplication) getApplication()).startSyncServerStatusThread();
			}
		} else if (DownloadState == -4) {
			AppSettings settings = AppSettings.getInstance(getApplicationContext());
			settings.saveLuckyDataVersion(saveLuckyDataVersion);			
			
			if(isNeedupdateLuckyImage){
				if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
					DownloadState = -5;

					OnCloseKeyBoard();

					String mSavePath = rootPath.concat("/VONGXOAY/" + MyApplication.LIST_LUCKYIMAGE);
					File mFile = new File(mSavePath);
					if (mFile.exists()) {
						mFile.delete();
					}

					allowTimerPing = false;
					((MyApplication) getApplication()).downloadLuckyImage(mSavePath);
					isDownloadingDauMay = true;
				}			
				return;
			}
			
			if(isNeedupdateHiddenFile){
				DownloadState = -6;

				OnCloseKeyBoard();

				String mPath = rootPath.concat("/YOUTUBE/" + MyApplication.YT_90xx_HIDDEN + ".tmp");
				allowTimerPing = false;
				((MyApplication) getApplication()).downloadHiddenFile(mPath);				
				isDownloadingDauMay = true;
				return;
			}
			
			if(isNeedupdateSambaData){
				DownloadState = -7;

				OnCloseKeyBoard();

				String mPath = rootPath.concat("/" + MyApplication.ADD_FILE_SAMBA);
				File mFile = new File(savePath);
				if (mFile.exists()) {
					mFile.delete();
				}

				allowTimerPing = false;
				((MyApplication) getApplication()).downloadSambaData(mPath);
				isDownloadingDauMay = true;
				return;
			}
			
			if (isMainNeedUpdate) {
				DownloadState = 0;
				allowTimerPing = false;
				
				String mSavePath = rootPath.concat("/MEGIDX");
				((MyApplication) getApplication()).downloadUpdateFile(mSavePath);
				isDownloadingDauMay = true;
				DownloadState++;
			} else {
				reloadYoutubeTable();

				setActiveView(SAVETYPE);
				serverStatus = null;
				((MyApplication) getApplication()).startSyncServerStatusThread();
			}
		} else if (DownloadState == -3) {
			AppSettings settings = AppSettings.getInstance(getApplicationContext());
			if(MyApplication.intSvrModel == MyApplication.SONCA){
				if(MyApplication.intSvrCode >= MyApplication.new9018API_downloadVersion){
					MyApplication.bVersionDownloaded = saveListOfflineVersionByte;
					if(flagDownloadingDD){						
						flagDownloadingDD = false;
						
						processListOffline_90xx();
						
						if(mainListener != null){
							mainListener.OnSK90009();
						}
						
						if(dialogSK90xxOnline != null){
							dialogSK90xxOnline.sendNotifyUpdateFragFullFull();
						}
						
						return;
					} else {
						String saveByte = Base64.encodeToString(saveListOfflineVersionByte, Base64.DEFAULT);						
						settings.saveListOfflineVersion_SK90xx_New(saveByte);
					}
				} else {
					MyApplication.intVersionDownloaded = saveListOfflineVersion;
					if(flagDownloadingDD){						
						flagDownloadingDD = false;
						
						processListOffline_90xx();
						
						if(mainListener != null){
							mainListener.OnSK90009();
						}
						
						if(dialogSK90xxOnline != null){
							dialogSK90xxOnline.sendNotifyUpdateFragFullFull();
						}
						
						return;
					} else {
						settings.saveListOfflineVersion_SK90xx(saveListOfflineVersion);
					}
				}
				
			} else {
				settings.saveListOfflineVersion(saveListOfflineVersion);	
			}	
			
			if(isNeedupdateLuckyData){
				if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
					DownloadState = -4;

					OnCloseKeyBoard();

					savePath = rootPath.concat("/VONGXOAY/" + MyApplication.LIST_LUCKYDATA);
					File mUpdateFile = new File(savePath);
					if (mUpdateFile.exists()) {
						mUpdateFile.delete();
					}

					allowTimerPing = false;
					((MyApplication) getApplication()).downloadLuckyData(savePath);
					isDownloadingDauMay = true;
				}			
				return;
			}
			
			if(isNeedupdateLuckyImage){
				if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
					DownloadState = -5;

					OnCloseKeyBoard();

					String mSavePath = rootPath.concat("/VONGXOAY/" + MyApplication.LIST_LUCKYIMAGE);
					File mFile = new File(mSavePath);
					if (mFile.exists()) {
						mFile.delete();
					}

					allowTimerPing = false;
					((MyApplication) getApplication()).downloadLuckyImage(mSavePath);
					isDownloadingDauMay = true;
				}			
				return;
			}
			
			if(isNeedupdateHiddenFile){
				DownloadState = -6;

				OnCloseKeyBoard();

				String mPath = rootPath.concat("/YOUTUBE/" + MyApplication.YT_90xx_HIDDEN + ".tmp");
				allowTimerPing = false;
				((MyApplication) getApplication()).downloadHiddenFile(mPath);				
				isDownloadingDauMay = true;
				return;
			}
			
			if(isNeedupdateSambaData){
				DownloadState = -7;

				OnCloseKeyBoard();

				String mPath = rootPath.concat("/" + MyApplication.ADD_FILE_SAMBA);
				File mFile = new File(savePath);
				if (mFile.exists()) {
					mFile.delete();
				}

				allowTimerPing = false;
				((MyApplication) getApplication()).downloadSambaData(mPath);
				isDownloadingDauMay = true;
				return;
			}
			
			if (isMainNeedUpdate) {
				DownloadState = 0;
				allowTimerPing = false;
				
				String mSavePath = rootPath.concat("/MEGIDX");
				((MyApplication) getApplication()).downloadUpdateFile(mSavePath);
				isDownloadingDauMay = true;
				DownloadState++;
			} else {
				reloadYoutubeTable();

				setActiveView(SAVETYPE);
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

			AppSettings settings = AppSettings.getInstance(getApplicationContext());
			if(MyApplication.intSvrModel == MyApplication.SONCA){
				settings.saveYouTubeVersion_SK90xx(saveYTVersion);
			} else {
				settings.saveYouTubeVersion(saveYTVersion);	
			}	
			
			if(isNeedupdateOfflineFile){
				DownloadState = -3;

				OnCloseKeyBoard();

				String mPath = rootPath.concat("/YOUTUBE/" + MyApplication.LIST_OFFLINE);
				if(MyApplication.intSvrModel == MyApplication.SONCA){
					mPath = rootPath.concat("/YOUTUBE/" + MyApplication.YT_90xx_OFFLINE);
				}
				File mFile = new File(mPath);
				if (mFile.exists()) {
					mFile.delete();
				}

				allowTimerPing = false;
				if(MyApplication.intSvrModel == MyApplication.SONCA){
					((MyApplication) getApplication()).downloadSK90xxFile(mPath, MyApplication.YT_90xx_OFFLINE);
				} else {
					((MyApplication) getApplication()).downloadOfflineFile(mPath);
				}		
				isDownloadingDauMay = true;
				return;
			}
			
			if(isNeedupdateLuckyData){
				if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
					DownloadState = -4;

					OnCloseKeyBoard();

					savePath = rootPath.concat("/VONGXOAY/" + MyApplication.LIST_LUCKYDATA);
					File mUpdateFile = new File(savePath);
					if (mUpdateFile.exists()) {
						mUpdateFile.delete();
					}

					allowTimerPing = false;
					((MyApplication) getApplication()).downloadLuckyData(savePath);
					isDownloadingDauMay = true;
				}			
				return;
			}
			
			if(isNeedupdateLuckyImage){
				if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
					DownloadState = -5;

					OnCloseKeyBoard();

					String mSavePath = rootPath.concat("/VONGXOAY/" + MyApplication.LIST_LUCKYIMAGE);
					File mFile = new File(mSavePath);
					if (mFile.exists()) {
						mFile.delete();
					}

					allowTimerPing = false;
					((MyApplication) getApplication()).downloadLuckyImage(mSavePath);
					isDownloadingDauMay = true;
				}			
				return;
			}
			
			if(isNeedupdateHiddenFile){
				DownloadState = -6;

				OnCloseKeyBoard();

				String mPath = rootPath.concat("/YOUTUBE/" + MyApplication.YT_90xx_HIDDEN + ".tmp");
				allowTimerPing = false;
				((MyApplication) getApplication()).downloadHiddenFile(mPath);				
				isDownloadingDauMay = true;
				return;
			}
			
			if(isNeedupdateSambaData){
				DownloadState = -7;

				OnCloseKeyBoard();

				String mPath = rootPath.concat("/" + MyApplication.ADD_FILE_SAMBA);
				File mFile = new File(savePath);
				if (mFile.exists()) {
					mFile.delete();
				}

				allowTimerPing = false;
				((MyApplication) getApplication()).downloadSambaData(mPath);
				isDownloadingDauMay = true;
				return;
			}

			if (isMainNeedUpdate) {
				DownloadState = 0;
				allowTimerPing = false;

				String mSavePath = rootPath.concat("/MEGIDX");
				((MyApplication) getApplication()).downloadUpdateFile(mSavePath);
				
				isDownloadingDauMay = true;
				DownloadState++;
			} else {
				reloadYoutubeTable();
				
				setActiveView(SAVETYPE);
				serverStatus = null;
				((MyApplication) getApplication()).startSyncServerStatusThread();
			}
		} else if (DownloadState == 1) {
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
		// Log.e("PROCESS TOC DAU MAY", "....................");

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
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				if (fragmentBase != null && SAVETYPE == SONG) {
					((TouchFragmentSong)fragmentBase).forceCloseDB();
				}
				fragmentTransaction.commit();	

				((MyApplication) getApplication()).cancelSyncServerStatus();

				serverStatus = null;

//				try {
//					String tempDBPath = rootFinalPath.concat("/database.db");
//					InputStream is = new FileInputStream(getDatabasePath(
//							DbHelper.DBName).getAbsolutePath());
//					OutputStream os = new FileOutputStream(tempDBPath);
//					copyFile(is, os);
//					is.close();
//					os.flush();
//					os.close();
//				} catch (Exception e) {
//					
//				}
				
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
							
							if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK
									|| (MyApplication.intSvrModel == MyApplication.SONCA && MyApplication.intSvrCode >= MyApplication.newAPI90xxVersion)){
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
			// Log.e("PROCESS HINH DAU MAY", "....................");
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
						ToFragmentDance(false);						
					}
					
					isDownloadingDauMay = false;

					((MyApplication) getApplication())
							.startSyncServerStatusThread();
				}
			};

			new Thread(new Runnable() {
				public void run() {
//					((MyApplication) getApplication()).cancelSyncServerStatus();
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
//				sk.setState(SKServer.CONNECTED);
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
//							skServer.setState(SKServer.CONNECTED);
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
		// MyLog.e("Main", "Read data success !");
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
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			if (fragmentBase != null && SAVETYPE == SONG) {
				((TouchFragmentSong)fragmentBase).forceCloseDB();
			}
			fragmentTransaction.commit();	
			
			String dbPath = getDatabasePath(DbHelper.DBName).getAbsolutePath();
			AssetManager assetMgr = TouchMainActivity.this.getAssets();

			InputStream is = assetMgr.open("database.db");
			OutputStream os = new FileOutputStream(dbPath);
			copyFile(is, os);
			is.close();
			os.flush();
			os.close();

//			AppSettings setting = AppSettings
//					.getInstance(getApplicationContext());
//			
//			setting.saveServerLastUpdate(5607, 1);
//			setting.saveServerLastUpdate(0, 2);
//			setting.saveServerLastUpdate(1000, 3);
//			setting.saveServerLastUpdate(0, 4);

			processLoadFavList();
			allowTimerPing = true;
			
			ToFragmentDance(false);
		} catch (Exception e) {

		}
	}
			
	private int intEmptyList = 0;
	private TouchTestSongData testSongData;
	private boolean flagParseDB = false;

	private ArrayList<SongID> curSongIDs;
	private int countChangeDance = 0;
	private boolean flagDownloadingHidden = false;
	private boolean flagDownloadingDD = false;
	private boolean flagSendingHidden = false;
	
	@Override
	public void deviceDidReceiveSyncStatus(boolean success,
			final ServerStatus status) {
		if (isDestroyMainActivity == false) {
			return;
		}	
		if (success == false) {
			//((MyApplication) getApplication()).cancelSyncServerStatus();
		} else {
			if (allowTimerPing != true) {
				allowTimerPing = true;
			}
			
			if(MyApplication.countStartApp < 2){ // start app first time				
				onUpdateAllFromServer();
			} 
			MyApplication.countStartApp = 2;
			
			if (serverStatus == null) {
				AppSettings settings = AppSettings
						.getInstance(getApplicationContext());
				if(MyApplication.intSvrModel == MyApplication.SONCA){
					settings.setLastConnectType(MyApplication.SONCA);
				}
				
				if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
					settings.setLastConnectType(MyApplication.SONCA_SMARTK);
					settings.setLastMIDIData(flagCheckDataMIDI);
				}
				
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
							MyLog.e(TAB, "listDownloadOffline = " + MyApplication.listDownloadOffline.size()); 		
							for (int i = 0; i < MyApplication.listDownloadOffline.size(); i++) {
								MyLog.d(TAB, "i = " + i + " -- " + MyApplication.listDownloadOffline.get(i));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}	
					}	
				}
				
				if(MyApplication.intSvrModel == MyApplication.SONCA && MyApplication.intSvrCode >= MyApplication.newAPI90xxVersion){
					MyApplication.listHiddenSK90xx = MyApplication.processGetListHidden90xx();
					
					if(dialogSK90xxHidden != null){
						dialogSK90xxHidden.sendNotifyCheckList();
					} else {
						if(serverStatus != null){
							ToFragmentDance(serverStatus.danceMode() == 1);	
						}					
					}
				}
				
				if (layoutDownload.getVisibility() != View.GONE) {
					layoutDownload.setVisibility(View.GONE);
				}
				
//				MyApplication.createDemoFilterList();
				
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
							((MyApplication)getApplication()).setOnReceiverDeviceIsUserListener(null);
						}
					});
				}		
				
//				animationView.ShowMessage(TouchAnimationView.NOTHING);
				UpdateControlView(status.getMediaType());
				danceView.setEnableView(true);
				pauseView.setConnect(true);
				nextView.setConnect(true);
				if(status.getPlayingSongID() != 0){
					repeatView.setConnect(true);	
				} else {
					repeatView.setConnect(false);
				}
				btnScore.setEnableView(View.VISIBLE);
				changePerfectView.invalidate();
				defaultControlView.setConnect(true);
				melodyView.setMelody(status.getCurrentMelody());
				tempoView.setTempo(status.getCurrentTempo());
				keyView.setKey(status.getCurrentKey());
				volumnView.setVolumn(status.getCurrentVolume());
				volumnView.setMute(!status.isMuted());
				muteView.setMute(status.isMuted());
				btnSinger.setSingerView(status.isSingerOn());
				btnTone.setToneView(status.getCurrentTone());
				btnScore.setScoreView(status.getCurrentScore());
				groupView.setPlayPause(status.isPlaying());
				pauseView.setPauseView(!status.isPaused());
				groupView.setSumSong(status.getReservedSongCount());
				if(colorLyricListener != null){
					colorLyricListener.OnMain_setCntPlaylist(status.getReservedSongCount());
				}
				
				if(MyApplication.intSvrModel == MyApplication.SONCA && MyApplication.intSvrCode >= MyApplication.newAPI90xxVersion){
					MyApplication.flagAutoDownloading = status.isAutoDownloadYouTube();
					MyApplication.listFullOnline = new ArrayList<Song>();
				}
				
				if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
					listRealYoutube = new ArrayList<Song>();
					ArrayList<RealYouTubeInfo> mList = status.getRealYoutubeList();
					for (int i = 0; i < mList.size(); i++) {
						Song mTempSong = new Song();
						mTempSong.setId(mList.get(i).getId());
						mTempSong.setIndex5(mList.get(i).getId());
						mTempSong.setPlayLink(mList.get(i).getVidID());
						mTempSong.setName(mList.get(i).getName());
						mTempSong.setRealYoutubeSong(true);
						mTempSong.setSingerName("-");
						listRealYoutube.add(mTempSong);								
					}
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
								Song mSong = processSongRealYoutube(id);
								if(mSong != null){
									name = mSong.getName();
								} else {
									name = "";
								}
							}
						} else {
							name = "";
						}
					}
				}
				groupView.setCurrectSong(name);
				if (name == null || name.equals("")) {
					melodyView.setEnableView(View.INVISIBLE);
					tempoView.setEnableView(View.INVISIBLE);
					keyView.setEnableView(View.INVISIBLE);
					btnSinger.setEnableView(View.INVISIBLE);
					btnTone.setEnableView(View.INVISIBLE);
					changePerfectView.invalidate();
					defaultControlView.invalidate();
					nextView.invalidate();
					repeatView.invalidate();
					pauseView.invalidate();
				}
				layoutAnimation.setVisibility(View.VISIBLE);
				// -----------//
				SKServer skServer = ((MyApplication) getApplication())
						.getDeviceCurrent();
				if (skServer != null) {
					skServer.setState(SKServer.CONNECTED);
					groupView.setConnected(TouchMyGroupView.CONNECTED,
							skServer.getName(), skServer);
				}
				if (listDeviceListener != null) {
					listDeviceListener.OnShowListDevice();
				}
				ToFragmentDance(status.danceMode() == 1);
				if (status.danceMode() == 1) {
					danceLinkView.setLayout(TouchDanceLinkView.DANCE);
				} else {
					danceLinkView.setLayout(TouchDanceLinkView.NODANCE);
				}
				
				if(MyApplication.flagSongPlayPause != groupView.getPlayPause()){
					MyApplication.flagSongPlayPause = groupView.getPlayPause();
					volumnView.invalidate();
					keyView.invalidate();
					tempoView.invalidate();
					btnTone.invalidate();
					melodyView.invalidate();		
					changePerfectView.invalidate();
					defaultControlView.invalidate();
					btnSinger.invalidate();
					btnScore.invalidate();
					repeatView.invalidate();
					nextView.invalidate();
					pauseView.invalidate();
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
							
							btnVideo.invalidate();
							
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
				viewFlower.invalidate();
				
			} else {
//				MyLog.e(" ", " ");
//				MyLog.i("SYNC MAIN", "intVersionDownloaded = " + MyApplication.intVersionDownloaded);				
//				MyLog.i("SYNC MAIN", "getVersionListDownload = " + status.getVersionListDownload());
//				MyLog.i("SYNC MAIN", "isDownloadingYouTube = " + status.isDownloadingYouTube());
//				MyLog.i("SYNC MAIN", "getYoutubeDownloadID = " + status.getYoutubeDownloadID());
//				MyLog.i("SYNC MAIN", "getYoutubeDownloadPercent = " + status.getYoutubeDownloadPercent());
//				MyLog.i("SYNC MAIN", "getYoutubeDownloadRemain = " + status.getYoutubeDownloadRemain());
						
				if(MyApplication.intSvrModel == MyApplication.SONCA && MyApplication.intSvrCode >= MyApplication.newAPI90xxVersion
						&& flagDownloadingHidden == false && flagSendingHidden == false){
					AppSettings setting = AppSettings.getInstance(getApplicationContext());
					int appHiddenVersion = setting.getListHiddenVersion_SK90xx();
					int daumayHiddenVersion = status.getVersionListHidden();
//					MyLog.e(" ", " ");
//					MyLog.i("SYNC MAIN", "appHiddenVersion = " + appHiddenVersion);
//					MyLog.i("SYNC MAIN", "daumayHiddenVersion = " + daumayHiddenVersion);
					
					if(appHiddenVersion != daumayHiddenVersion){
						if(daumayHiddenVersion == 0){
							String rootPath = android.os.Environment
									.getExternalStorageDirectory()
									.toString()
									.concat(String.format("/%s/%s", "Android/data",
											getPackageName()));
							String myPath = rootPath.concat("/YOUTUBE/" + MyApplication.YT_90xx_HIDDEN);
							File myFile = new File(myPath);
							if(myFile.exists()){
								myFile.delete();
							}
							setting.saveListHiddenVersion_SK90xx(0);
							MyApplication.listHiddenSK90xx = MyApplication.processGetListHidden90xx();

							if(dialogSK90xxHidden != null){
								dialogSK90xxHidden.sendNotifyCheckList();
							} else {
								if(serverStatus != null){
									ToFragmentDance(serverStatus.danceMode() == 1);	
								}					
							}
						} else {
							DownloadState = -6;

							OnCloseKeyBoard();
							
							String rootPath = android.os.Environment
									.getExternalStorageDirectory()
									.toString()
									.concat(String.format("/%s/%s", "Android/data",
											getPackageName()));
							String savePath = rootPath.concat("/YOUTUBE/" + MyApplication.YT_90xx_HIDDEN + ".tmp");
//							File updateFile = new File(savePath);
//							if (updateFile.exists()) {
//								updateFile.delete();
//							}
							saveListHiddenVersion = daumayHiddenVersion;
							allowTimerPing = false;
							MyApplication.listHiddenSK90xx = MyApplication.processGetListHidden90xx();							
							((MyApplication) getApplication()).downloadHiddenFile(savePath);				
							isDownloadingDauMay = true;
							flagDownloadingHidden = true;
						}
					}
				}
				
				if(MyApplication.intSvrModel == MyApplication.SONCA && MyApplication.intSvrCode >= MyApplication.newAPI90xxVersion){
					if(MyApplication.intDownloadRemain != status.getYoutubeDownloadRemain()){
						MyApplication.intDownloadRemain = status.getYoutubeDownloadRemain();
						if(dialogSK90xxOnline != null){
							dialogSK90xxOnline.onUpdateButton();
						}
					}
				}
				
				if(MyApplication.intSvrModel == MyApplication.SONCA && MyApplication.intSvrCode >= MyApplication.newAPI90xxVersion
						&& flagDownloadingDD == false
						&& MyApplication.intSvrCode < MyApplication.new9018API_downloadVersion){
					if(status.getVersionListDownload() != MyApplication.intVersionDownloaded){
						saveListOfflineVersion = status.getVersionListDownload();
						flagDownloadingDD = true;
						
						String rootPath = android.os.Environment
								.getExternalStorageDirectory()
								.toString()
								.concat(String.format("/%s/%s", "Android/data",
										getPackageName()));
						
						DownloadState = -3;

						OnCloseKeyBoard();

						String mPath = rootPath.concat("/YOUTUBE/" + MyApplication.YT_90xx_OFFLINE);
						File mFile = new File(mPath);
						if (mFile.exists()) {
							mFile.delete();
						}

						allowTimerPing = false;
						((MyApplication) getApplication()).downloadSK90xxFile(mPath, MyApplication.YT_90xx_OFFLINE);				
						isDownloadingDauMay = true;
					}					
				}
				
				if(MyApplication.intSvrModel == MyApplication.SONCA && MyApplication.intSvrCode >= MyApplication.new9018API_downloadVersion
						&& flagDownloadingDD == false){								
					if(Arrays.equals(status.getVersionListDownloadByte(), MyApplication.bVersionDownloaded) == false){				
						saveListOfflineVersionByte = status.getVersionListDownloadByte();
						flagDownloadingDD = true;
						
						String rootPath = android.os.Environment
								.getExternalStorageDirectory()
								.toString()
								.concat(String.format("/%s/%s", "Android/data",
										getPackageName()));
						
						DownloadState = -3;

						OnCloseKeyBoard();

						String mPath = rootPath.concat("/YOUTUBE/" + MyApplication.YT_90xx_OFFLINE);
						File mFile = new File(mPath);
						if (mFile.exists()) {
							mFile.delete();
						}

						allowTimerPing = false;
						((MyApplication) getApplication()).downloadSK90xxFile(mPath, MyApplication.YT_90xx_OFFLINE);				
						isDownloadingDauMay = true;
					}					
				}
				
				if(MyApplication.intSvrModel == MyApplication.SONCA && dialogSK90xxOnline!=null){
					if(dialogSK90xxOnline.getAutoStatus() != status.isAutoDownloadYouTube()){
						MyApplication.flagAutoDownloading = status.isAutoDownloadYouTube();
						dialogSK90xxOnline.setAutoStatus(status.isAutoDownloadYouTube());
					}
				}				
				
				if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && luckRollView!=null){
					if(serverStatus.isPlayingLucky() != status.isPlayingLucky()){
						if(status.isPlayingLucky()){
							luckRollView.startTimerLuckyRoll();
							if(dialogFlower != null){
								dialogFlower.processClearLuckyResult();
							}
						} else {
							luckRollView.setCurrentLuck(status.getCurrentLucky());
							if(dialogFlower != null){
								dialogFlower.processDisplayLuckyResult();
							}
						}
					}
				}
				
				if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK)					
				if(serverStatus.getRealYoutubeCount() != status.getRealYoutubeCount()){
					listRealYoutube = new ArrayList<Song>();
					ArrayList<RealYouTubeInfo> mList = status.getRealYoutubeList();
					for (int i = 0; i < mList.size(); i++) {
						Song mTempSong = new Song();
						mTempSong.setId(mList.get(i).getId());
						mTempSong.setIndex5(mList.get(i).getId());
						mTempSong.setPlayLink(mList.get(i).getVidID());
						mTempSong.setName(mList.get(i).getName());
						mTempSong.setRealYoutubeSong(true);
						mTempSong.setSingerName("-");
						listRealYoutube.add(mTempSong);		
						MyLog.d("", listRealYoutube.get(i).getId() + " -- " + listRealYoutube.get(i).getName() + " -- " + listRealYoutube.get(i).getPlayLink());						
					}	
				}
				
				if(MyApplication.flagModelA != status.isModelA()){
					MyApplication.flagModelA = status.isModelA();
				}		
				
				if(MyApplication.intSvrModel == MyApplication.SONCA && MyApplication.intSvrCode >= MyApplication.newAPI90xxVersion
						&& dialogSK90xxOnline != null && (System.currentTimeMillis() - sk90xxOnlineBreak) >= 2000){					
					
					if(MyApplication.flagDownloadYouTube != status.isDownloadingYouTube()){
						MyApplication.flagDownloadYouTube = status.isDownloadingYouTube();
						
						if(dialogSK90xxOnline != null){
							dialogSK90xxOnline.sendNotifyUpdateFragFull();
						}
						
					}
					
					if(MyApplication.youtube_Download_ID != status.getYoutubeDownloadID()){
						MyApplication.youtube_Download_ID = status.getYoutubeDownloadID();						
													
						if(dialogSK90xxOnline != null){
							dialogSK90xxOnline.sendNotifyUpdateFragFull();
						}
						
					}
					
					if(MyApplication.youtube_Download_percent != status.getYoutubeDownloadPercent()){
						MyApplication.youtube_Download_percent = status.getYoutubeDownloadPercent();						
						
						if(dialogSK90xxOnline != null){
							dialogSK90xxOnline.sendNotifyUpdateFrag();
						}
						
					}	
					
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
						
						if(mainListener != null && SAVETYPE != YOUTUBE){
							mainListener.OnSK90009();
						}
						
						updatePopupDialog();
						
						hideshowDialogConnect();
					}
					
					if(MyApplication.youtube_Download_ID != status.getYoutubeDownloadID()){
						MyApplication.youtube_Download_ID = status.getYoutubeDownloadID();						
													
						if(mainListener != null && SAVETYPE != YOUTUBE){
							mainListener.OnSK90009();
						}
						
						updatePopupDialog();
						
					}
					
					if(MyApplication.youtube_Download_percent != status.getYoutubeDownloadPercent()){
						MyApplication.youtube_Download_percent = status.getYoutubeDownloadPercent();						
						
						if(mainListener != null && SAVETYPE != YOUTUBE){
							mainListener.OnSK90009();
						}
						
						updatePopupDialog();
						
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
														
						volumnView.invalidate();
						keyView.invalidate();
						tempoView.invalidate();
						btnTone.invalidate();
						melodyView.invalidate();
						changePerfectView.invalidate();
						defaultControlView.invalidate();
						btnSinger.invalidate();
						btnScore.invalidate();
						repeatView.invalidate();
						nextView.invalidate();
						danceView.invalidate();
						btnVideo.invalidate();
						pauseView.invalidate();
						
						if(youtubeView != null){
							youtubeView.invalidate();
						}
						if(SAVETYPE == YOUTUBE){
							resetActiveView();
							ShowSongFragment(SONG, songView);
						}
						
						if(dialogSettingApp != null){
							dialogSettingApp.syncFromServer();
						}
						
						if(commandBox != null){
							commandBox.syncFromServer();
						}						

						if(sliderView != null && myDialogSlider != null && myDialogSlider.isShowing()){
							myDialogSlider.dismiss();
						}
						if(sliderView != null && myDialogSliderDance != null && myDialogSliderDance.isShowing()){
							myDialogSliderDance.dismiss();
						}
						
						if(colorLyricListener!=null){
							colorLyricListener.OnMain_UpdateControl();
						}
						
						if(SAVETYPE == SONGTYPE){
							resetActiveView();
							ShowSongFragment(SONGTYPE, typeView);
						}
						
					}
				}else{
					
					if ((~status.getAdminControlValue()) != MyApplication.intSavedCommandEnable) {
						MyLog.e("SYNC", "change admin control enable");
						
						MyApplication.intCommandEnable = (~status.getAdminControlValue());
						MyApplication.intSavedCommandEnable = (~status.getAdminControlValue());
																				
						volumnView.invalidate();
						keyView.invalidate();
						tempoView.invalidate();
						btnTone.invalidate();
						melodyView.invalidate();	
						changePerfectView.invalidate();
						defaultControlView.invalidate();
						btnSinger.invalidate();
						btnScore.invalidate();
						repeatView.invalidate();
						nextView.invalidate();
						danceView.invalidate();
						btnVideo.invalidate();
						pauseView.invalidate();
						
						if(commandBox != null){
							commandBox.syncFromServer();
						}
						
						if(sliderView != null && myDialogSlider != null && myDialogSlider.isShowing()){
							myDialogSlider.dismiss();
						}
						if(sliderView != null && myDialogSliderDance != null && myDialogSliderDance.isShowing()){
							myDialogSliderDance.dismiss();
						}
					}
				}
				
				// setActiveView(SAVETYPE);
				if (status.isFlagOffUserList() != MyApplication.bOffUserList) {
					MyLog.e("SYNC", "change app bOffUserList");
					MyApplication.bOffUserList = status.isFlagOffUserList();
					reloadSongList();
				}
				if (status.getMediaType() != serverStatus.getMediaType()) {
					serverStatus.setMediaType(status.getMediaType());
					if (status.getPlayingSongID() != 0) {
						UpdateControlView(serverStatus.getMediaType());
						volumnView.setEnableView(View.VISIBLE);
						pauseView.setConnect(true);
						nextView.setConnect(true);
						repeatView.setConnect(true);
					} else {
						groupView.setPlayPause(false);
//						volumnView.setEnableView(View.INVISIBLE);
						melodyView.setEnableView(View.INVISIBLE);
						tempoView.setEnableView(View.INVISIBLE);
						keyView.setEnableView(View.INVISIBLE);
						btnSinger.setEnableView(View.INVISIBLE);
						btnTone.setEnableView(View.INVISIBLE);
						btnScore.setEnableView(View.INVISIBLE);
						pauseView.setConnect(false);
						nextView.setConnect(false);
						repeatView.setConnect(false);
					}
				}
				
				if (status.getCurrentMelody() != melodyView.getMelody())
					melodyView.setMelody(status.getCurrentMelody());
				if (status.getCurrentTempo() != tempoView.getTempo()){
					tempoView.setTempo(status.getCurrentTempo());
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
				
				if(status.getCurrentTempo() == 0 && btnVideo.getBlockStatus()){
					btnVideo.invalidate();
				}
				if(status.getCurrentTempo() != 0 && !btnVideo.getBlockStatus()){
					btnVideo.invalidate();
				}

				if (status.getCurrentKey() != keyView.getKey())
					keyView.setKey(status.getCurrentKey());
				if (status.getCurrentVolume() != volumnView.getVolumn()) {
					volumnView.setVolumn(status.getCurrentVolume());
					volumnView.setMute(!status.isMuted());
					muteView.setMute(status.isMuted());
				}
				if (status.isMuted() == volumnView.isMute()) {
					volumnView.setVolumn(status.getCurrentVolume());
					volumnView.setMute(!status.isMuted());
					muteView.setMute(status.isMuted());
				}
				if(muteView.getMute()!= status.isMuted()){
					muteView.setMute(status.isMuted());
				}
				if (status.isSingerOn() != btnSinger.isSingerView() && btnSinger.getStateView() == View.VISIBLE){
					btnSinger.setSingerView(status.isSingerOn());
					if(colorLyricListener != null){
						colorLyricListener.OnMain_VocalSinger(status.isSingerOn());
					}
				}					
				if (status.getCurrentTone() != btnTone.getToneView())
					btnTone.setToneView(status.getCurrentTone());
				if (status.getCurrentScore() != btnScore.getScoreView())
					btnScore.setScoreView(status.getCurrentScore());	
				if (status.isPaused() == pauseView.getPauseView()){
					pauseView.setPauseView(!status.isPaused());
				}					
				if (status.isPlaying() != groupView.getPlayPause()) {	
					if(colorLyricListener != null){
						colorLyricListener.OnMain_PausePlay(status.isPlaying());
					}
					groupView.setPlayPause(status.isPlaying());
					changePerfectView.invalidate();
					defaultControlView.invalidate();
					melodyView.invalidate();
					keyView.invalidate();
					tempoView.invalidate();
					btnTone.invalidate();
					btnScore.invalidate();
					singerView.invalidate();
					if (mainListener != null) {
						mainListener.OnSK90009();
					}
					
					if(sliderView != null && myDialogSlider != null && myDialogSlider.isShowing()){
						myDialogSlider.dismiss();
					}
					if(sliderView != null && myDialogSliderDance != null && myDialogSliderDance.isShowing()){
						myDialogSliderDance.dismiss();
					}
				}

				if (danceLinkView.getLayout() == TouchDanceLinkView.DANCE) {
					if (mainListener != null) {
						mainListener.OnUpdateCommad(status);
					}
				}
				if(fragmentManager.findFragmentByTag(ONE_HELLO) != null){
					if (onToHelloListener != null) {
						onToHelloListener.OnUpdateCommad(status);
					}
				}
				
				ArrayList<SongID> songIDs = status.getReservedSongs();				
				
				/*
				 * MyLog.i(TAB, "--------------------"); for (int i = 0; i <
				 * songIDs.size(); i++) { MyLog.i(TAB, "sync - " +
				 * songIDs.get(i).songID); } MyLog.i(TAB,
				 * "--------------------");
				 */
//				animationView.ShowMessage(TouchAnimationView.NOTHING);
				if (songIDs != null) {
					boolean notify = ((MyApplication)getApplication()).CheckNotifyPlayList(songIDs);
					ArrayList<Song> list = new ArrayList<Song>();
					for (int i = 0; i < songIDs.size(); i++) {
						SongID songID = songIDs.get(i);
						list.add(new Song(songID.songID , songID.typeABC));
					}
					
//					MyLog.d(" ", " ");MyLog.d(" ", " ");
//					MyLog.d(TAB, "old PlayingState - " + serverStatus.getPlayingState());
//					MyLog.d(TAB, "new PlayingState - " + status.getPlayingState());
//					MyLog.d(TAB, "status.getMediaType() - " + status.getMediaType());
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
					
					if (notify && mainListener != null) {
						MyLog.e("SYNC NOFITY", notify + "");
						
						curSongIDs = songIDs;
						((MyApplication)getApplication()).setListActive(list);
						
						setNextSongDB();	
						
						groupView.setSumSong(songIDs.size());
						
						if(colorLyricListener != null){
							colorLyricListener.OnMain_setCntPlaylist(songIDs.size());
						}
						
						searchView.setSumPlayList(songIDs.size(), SAVETYPE);
						mainListener.OnSK90009();
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
					
					viewFlower.invalidate();
					btnVideo.invalidate();
					
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
									Song mSong = processSongRealYoutube(id);
									if(mSong != null){
										name = mSong.getName();
									} else {
										name = "";
									}
								}
							} else {
								name = "";
							}
						}
					}
					groupView.setCurrectSong(name);
					
					if(dialogFlower != null){
						dialogFlower.syncNameSong(name);
					}
					
					if(colorLyricListener != null){
						colorLyricListener.OnMain_setCurrSongPlaylist(name);
					}
					
				}
				
				if ((status.danceMode() == 1) != danceLinkView.getLayout()) {
					if(colorLyricListener != null){
						colorLyricListener.OnMain_Dance(status.danceMode() == 1);
					}
					
					ToFragmentDance(status.danceMode() == 1);
//					if(countChangeDance < 2){
//						countChangeDance++;
//					} else {
//						ToFragmentDance(status.danceMode() == 1);						
//					}					
				}
				
				if(MyApplication.flagSongPlayPause != groupView.getPlayPause()){
					MyApplication.flagSongPlayPause = groupView.getPlayPause();
					volumnView.invalidate();
					keyView.invalidate();
					tempoView.invalidate();
					btnTone.invalidate();
					melodyView.invalidate();			
					changePerfectView.invalidate();
					defaultControlView.invalidate();
					btnSinger.invalidate();
					btnScore.invalidate();
					repeatView.invalidate();
					nextView.invalidate();
					pauseView.invalidate();
				}				

				if(sliderView != null && myDialogSlider != null && myDialogSlider.isShowing()){
					int value = sliderView.getMainVolumn();
					switch (sliderView.getSenderType()) {
					case SliderView.SENDER_MELODY:
						if(value != status.getCurrentMelody()){
							sliderView.setVolumn(status.getCurrentMelody());
						}
						break;
					case SliderView.SENDER_TEMPO:
						if(value != status.getCurrentTempo()){
							sliderView.setVolumn(status.getCurrentTempo());
						}
						break;
					case SliderView.SENDER_KEY:
						if(value != status.getCurrentKey()){
							sliderView.setVolumn(status.getCurrentKey());
						}
						break;
					case SliderView.SENDER_VOLUMN:
						if(value != status.getCurrentVolume()){
							sliderView.setVolumn(status.getCurrentVolume());
						}
						break;
					}
				}
				
				if(commandBox != null){
					commandBox.syncScoreFromServer(status);
				}
				
				if(dialogFlower != null){
					dialogFlower.syncPauPlay(status);
				}
				
				if(MyApplication.flagPlayingYouTube != status.isPlayingYouTube()){
					
					MyApplication.flagPlayingYouTube = status.isPlayingYouTube();
					
					if(MyApplication.flagPlayingYouTube){
//						if(boolShowKaraoke){
//							new Handler().postDelayed(new Runnable() {
//								@Override
//								public void run() {
//									showDialogMessage(getResources().getString(R.string.msg_kara_1a));
//								}
//							}, 1000);	
//
//							hideKaraoke();
//						}
						
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
					
					viewFlower.invalidate();
					btnVideo.invalidate();
					groupView.invalidate();
					UpdateControlView(status.getMediaType());
				}
				
				try {
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
		volumnView.setEnableView(View.INVISIBLE);
		melodyView.setEnableView(View.INVISIBLE);
		tempoView.setEnableView(View.INVISIBLE);
		keyView.setEnableView(View.INVISIBLE);
		btnSinger.setEnableView(View.INVISIBLE);
		btnTone.setEnableView(View.INVISIBLE);
		btnScore.setEnableView(View.INVISIBLE);
		changePerfectView.invalidate();
		defaultControlView.setConnect(false);
		danceView.setEnableView(false);
		pauseView.setConnect(false);
		nextView.setConnect(false);
		repeatView.setConnect(false);
		btnVideo.invalidate();
	}

	private void UpdateControlView(int type) {
		btnVideo.invalidate();
		volumnView.setEnableView(View.VISIBLE);
		changePerfectView.invalidate();
		defaultControlView.setConnect(true);
		danceView.setEnableView(true);
		pauseView.setConnect(true);
		nextView.setConnect(true);
		repeatView.setConnect(true);
		
		
		if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && MyApplication.flagPlayingYouTube && flagCheckDataMIDI == false){					
			MyLog.i(" ", "UpdateControlView - YOUTUBE");
			
			melodyView.setEnableView(View.INVISIBLE);
			tempoView.setEnableView(View.INVISIBLE);
			keyView.setEnableView(View.INVISIBLE);
			volumnView.setEnableView(View.VISIBLE);
			btnSinger.setEnableView(View.INVISIBLE);
			btnTone.setEnableView(View.INVISIBLE);
			btnScore.setEnableView(View.INVISIBLE);
			
			groupView.setPlayPause(true);
			pauseView.setPauseView(true);
			return;
		}
		
		if (type == 0x07) {
			return;
		}

		btnScore.setEnableView(View.VISIBLE);
		
		MEDIA_TYPE aType = MEDIA_TYPE.values()[type];
		if (aType == MEDIA_TYPE.MIDI) {
			melodyView.setEnableView(View.VISIBLE);
			tempoView.setEnableView(View.VISIBLE);
			btnSinger.setEnableView(View.INVISIBLE);
			keyView.setEnableView(View.VISIBLE);
			btnTone.setEnableView(View.VISIBLE);
			return;
		} else if (aType == MEDIA_TYPE.MP3) {
			melodyView.setEnableView(View.INVISIBLE);
			tempoView.setEnableView(View.INVISIBLE);
			btnSinger.setEnableView(View.INVISIBLE);
			keyView.setEnableView(View.VISIBLE);
			btnTone.setEnableView(View.VISIBLE);
			if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
				keyView.setEnableView(View.INVISIBLE);
			}
			return;
		} else if (aType == MEDIA_TYPE.SINGER) {
			melodyView.setEnableView(View.INVISIBLE);
			tempoView.setEnableView(View.INVISIBLE);
			keyView.setEnableView(View.VISIBLE);
			btnSinger.setEnableView(View.VISIBLE);
			btnTone.setEnableView(View.VISIBLE);
			if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
				keyView.setEnableView(View.INVISIBLE);
			}
			return;
		} else if (aType == MEDIA_TYPE.VIDEO) {
			melodyView.setEnableView(View.INVISIBLE);
			tempoView.setEnableView(View.INVISIBLE);
			keyView.setEnableView(View.VISIBLE);
			btnSinger.setEnableView(View.VISIBLE);
			btnTone.setEnableView(View.VISIBLE);
			if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
				keyView.setEnableView(View.INVISIBLE);
			}
			return;
		} else {
			melodyView.setEnableView(View.INVISIBLE);
			tempoView.setEnableView(View.INVISIBLE);
			btnSinger.setEnableView(View.INVISIBLE);
			keyView.setEnableView(View.INVISIBLE);
			btnTone.setEnableView(View.INVISIBLE);			
		}
	}

	// ///////////////////// Network Socket Listener
	// ////////////////////////////

	
	private void DefaultViewWhenPause(){	
//		hideKaraoke();
		
		MyApplication.flagPopupYouTube = false;
		
		if(listRealYoutube != null){
			listRealYoutube.clear();
		}
		
		MyApplication.flagModelA = false;
		
		MyApplication.curHiW_firmwareInfo = null;
		MyApplication.curHiW_firmwareConfig = null;
		
		if(myDialogSlider!=null){
			myDialogSlider.dismiss();
		}
		if(myDialogSliderDance!=null){
			myDialogSliderDance.dismiss();
		}
		
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
		groupView.setSumSong(0);
		groupView.setPlayPause(false);
		groupView.setCurrectSong("");
		groupView.setConnected(TouchMyGroupView.INCONNECT, "", null);
		groupView.setNameRemote("");
//		animationView.ShowMessage(AnimationView.WIFI);
		
		drawerLayout.closeDrawers();
		
		danceLinkView.setLayout(TouchDanceLinkView.NODANCE);
		findViewById(R.id.layoutLinkDance).setVisibility(View.GONE);
		
		if (danceLinkView.getLayout() == TouchDanceLinkView.DANCE || SAVETYPE == PLAYLIST) {
			ShowSongFragment(SONG, songView);
		}
		
		if(colorLyricListener != null){
			colorLyricListener.OnMain_setCntPlaylist(0);
		}
		
		// -----------//
		searchView.setSumPlayList(0, SAVETYPE);
		((MyApplication) getApplication()).setListActive(new ArrayList<Song>());
		((MyApplication) getApplication()).setListActive(new ArrayList<Song>());
		((MyApplication) getApplication()).setDeviceCurrent(new SKServer());
		((MyApplication) getApplication()).cancelSyncServerStatus();
		((MyApplication) getApplication()).disconnectFromRemoteHost();
		((MyApplication) getApplication()).onDestroy();

//		if (listDeviceListener != null) {
//			SKServer skServer = ((MyApplication) getApplication())
//					.getDeviceCurrent();
//			if (skServer != null) {
//				skServer.setState(SKServer.BROADCASTED);
//			}
//			listDeviceListener.OnShowListDevice();
//		}
	}
	
	private void DefaultViewWhenDisConect() {
		MyLog.e(TAB, "DefaultViewWhenDisConect()");
		
//		hideKaraoke();
		
		MyApplication.flagPopupYouTube = false;
		
		if(listRealYoutube != null){
			listRealYoutube.clear();
		}
		
		if(dialogAddSongData != null){
			dialogAddSongData.dismissDialog(false);
			dialogAddSongData = null;
		}
		
		if(dialogSK90xxHidden != null){
			dialogSK90xxHidden.removeFrag();
			dialogSK90xxHidden.dismissDialog(false);
			dialogSK90xxHidden = null;
		}
		
		if(dialogSK90xxOnline != null){
			dialogSK90xxOnline.removeFrag();
			dialogSK90xxOnline.dismissDialog(false);
			dialogSK90xxOnline = null;
		}
		
		if (mainListener != null) {
			mainListener.OnSK90009();
		}
		
		searchView.setSumPlayList(0, SAVETYPE);
		
		MyApplication.flagModelA = false;
		
		MyApplication.curHiW_firmwareInfo = null;
		MyApplication.curHiW_firmwareConfig = null;
		
		boolean flagTimerIn = serverStatus == null;
		
		if(myDialogSlider!=null){
			myDialogSlider.dismiss();
		}
		if(myDialogSliderDance!=null){
			myDialogSliderDance.dismiss();
		}
		if(viewFlower != null){
			viewFlower.invalidate();
		}
		if(dialogFlower != null){
			dialogFlower.dismissDialog(false);
			dialogFlower = null;
		}
		MyApplication.intCommandMedium = 0x00000000;
		MyApplication.intSavedCommandMedium = 0x00000000;
		MyApplication.intCommandEnable = 0xffff;
		MyApplication.bOffFirst = true;
		hideBlockCommand();
		
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
		groupView.setSumSong(0);
		groupView.setPlayPause(false);
		groupView.setCurrectSong("");
		groupView.setConnected(TouchMyGroupView.INCONNECT, "", null);
		groupView.setNameRemote("");
		groupView_StopTimerAutoConnect();
		groupView_StartTimerAutoConnect();
		// -----------//
		layoutDownload.setVisibility(View.GONE);
		layoutAnimation.setVisibility(View.VISIBLE);
		//animationView.ShowMessage(TouchAnimationView.WIFI);	

		danceLinkView.setLayout(TouchDanceLinkView.NODANCE);
		findViewById(R.id.layoutLinkDance).setVisibility(View.GONE);
				
		if(colorLyricListener != null){
			colorLyricListener.OnMain_setCntPlaylist(0);
			colorLyricListener.OnMain_RemoveSocket();
		}
		
//		drawerLayout.closeDrawers();
		
//		if (!drawerLayout.isDrawerOpen(layoutConnect)) {	
//			drawerLayout.openDrawer(layoutConnect);
//			ShowListDevice("");
//		}
		// -----------//
		searchView.setSumPlayList(0, SAVETYPE);
		((MyApplication) getApplication()).setListActive(new ArrayList<Song>());
		((MyApplication) getApplication()).setDeviceCurrent(new SKServer());
		((MyApplication) getApplication()).cancelSyncServerStatus();
		((MyApplication) getApplication()).disconnectFromRemoteHost();
//		if(isDestroyMainActivity == true)
//		if (((MyApplication) getApplication()).getSocket() != null) {
//			if (listDeviceListener != null && !isScanning) {
//				isScanning = true;
//				((MyApplication) getApplication()).setListServers(null);
//				listDeviceListener.OnDisplayProgressScan(true);
//				((MyApplication) getApplication()).searchNearbyDevice();			
//			}
//		}
		
		if(!flagTimerIn){
			if (danceLinkView.getLayout() != TouchDanceLinkView.DANCE) {
				ToFragmentDance(false);
			}	
		}
		
		if (danceLinkView.getLayout() == TouchDanceLinkView.DANCE || SAVETYPE == PLAYLIST) {
			ShowSongFragment(SONG, songView);
		}
		
//		if (listDeviceListener != null) {
//			SKServer skServer = ((MyApplication) getApplication())
//					.getDeviceCurrent();
//			if (skServer != null) {
//				skServer.setState(SKServer.BROADCASTED);
//			}
//			listDeviceListener.OnShowListDevice();
//		}
		if (mainListener != null) {
			mainListener.OnSK90009();
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
			// MyLog.e(TAB, "per - " + per + " | total - " + total);
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
		
		MyApplication.flagPopupYouTube = false;
		
		if(listRealYoutube != null){
			listRealYoutube.clear();
		}
		
		MyApplication.flagModelA = false;
		
		if(viewFlower != null){
			viewFlower.invalidate();
		}
		
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
		groupView.setSumSong(0);
		if(colorLyricListener != null){
			colorLyricListener.OnMain_setCntPlaylist(0);
		}
		
		groupView.setPlayPause(false);
		groupView.setCurrectSong("");
		groupView.setConnected(TouchMyGroupView.INCONNECT, "", null);
		groupView.setNameRemote("");

		groupView_StopTimerAutoConnect();
		groupView.StartTimerAutoConnect();
		
		// -----------//
		layoutDownload.setVisibility(View.GONE);
		layoutAnimation.setVisibility(View.VISIBLE);
		//animationView.ShowMessage(TouchAnimationView.WIFI);	

		danceLinkView.setLayout(TouchDanceLinkView.NODANCE);
		findViewById(R.id.layoutLinkDance).setVisibility(View.GONE);
		
//		drawerLayout.closeDrawers();
		
		if (!drawerLayout.isDrawerOpen(layoutConnect)) {	
			drawerLayout.openDrawer(layoutConnect);
			ShowListDevice("");
		}
		// -----------//
		searchView.setSumPlayList(0, SAVETYPE);
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
		if (danceLinkView.getLayout() != TouchDanceLinkView.DANCE) {
			ToFragmentDance(false);
		}
		if (listDeviceListener != null) {
			SKServer skServer = ((MyApplication) getApplication())
					.getDeviceCurrent();
			if (skServer != null) {
				skServer.setState(SKServer.BROADCASTED);
			}
			listDeviceListener.OnShowListDevice();
		}
		if (mainListener != null) {
			mainListener.OnSK90009();
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
		
		if(msg.equals("lucky_notsupport")){
			showDialogMessage(context.getString(R.string.lucyroll_1));
			return;
		}
		
		if(msg.startsWith("yt_90xx_down_status")){
			try {
				int status = Integer.parseInt(msg.split("@&@")[1]);
				switch (status) {
				case 2:
					showDialogMessage(context.getString(R.string.dialog_90xx_err_1));	
					break;
				case 3:
					showDialogMessage(context.getString(R.string.dialog_90xx_err_2));	
					break;
				case 4:
					showDialogMessage(context.getString(R.string.dialog_90xx_err_3));	
					break;
				case 5:
					showDialogMessage(context.getString(R.string.dialog_90xx_err_4));	
					break;
				case 6:
					showDialogMessage(context.getString(R.string.dialog_90xx_err_5));	
					break;
				default:
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			return;
		}
		
//		((MyApplication) getApplication()).setDeviceCurrent(new SKServer());
//		if (listDeviceListener != null) {
//			listDeviceListener.OnShowListDevice();
//			listDeviceListener.OnDisplayProgressScan(false);
//		}
//		layoutDownload.setVisibility(View.GONE);
//		layoutAnimation.setVisibility(View.VISIBLE);
//		animationView.ShowMessage(TouchAnimationView.WIFI);	
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
		// OnShowConnect();
				
		if (MyApplication.updateFirmwareServerSocket != null) {
			if (serviceIntent != null) {
				showDialogMessage(getResources().getString(R.string.msg_7));
			
				TouchMainActivity.this.stopService(serviceIntent);
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
		// MyLog.e(TAB, "deviceUpdatePlayList()");
	}

	// /////////////////////////// - FRAGMENTDANCE -
	// //////////////////////////////////

	@Override
	public void OnCreateView() {

	}

	@Override
	public void onMute(boolean isMute) {
		((MyApplication) getApplication()).sendCommand(
				NetworkSocket.REMOTE_CMD_MUTE, (isMute == true) ? 0 : 1);
	}

	@Override
	public void OnStopDance() {
		// MyLog.e(TAB, "OnStopDance()");
		findViewById(R.id.layoutLinkDance).setVisibility(View.VISIBLE);
		danceLinkView.ShowDanceLink();
	}

	@Override
	public void OnVolumnView(int value) {
		((MyApplication) getApplication()).sendCommand(
				NetworkSocket.REMOTE_CMD_VOLUME, value);
	}
	
	@Override
	public void OnRepeat() {
		((MyApplication) getApplication()).sendCommand(
				NetworkSocket.REMOTE_CMD_REPEAT, 0);
	}

	@Override
	public void OnPause(boolean value) {
		if (value) {
			((MyApplication) getApplication()).sendCommand(
					NetworkSocket.REMOTE_CMD_PLAY, 0);
		} else {
			((MyApplication) getApplication()).sendCommand(
					NetworkSocket.REMOTE_CMD_PAUSE, 0);
		}
	}

	@Override
	public void OnNext() {
		((MyApplication) getApplication()).sendCommand(
				NetworkSocket.REMOTE_CMD_STOP, 0);
	}

	@Override
	public void OnDestroyView(int value, boolean bool) {
		volumnView.setVolumn(value);
		pauseView.setPauseView(bool);
	}

	@Override
	public void OnRanDom() {

	}

	@Override
	public void OnNameSearch(String idFrag, String typeFrag) {
		// MyLog.e(TAB, "typeFragment : " + typeFrag);
		// MyLog.e(TAB, "idFragment : " + idFrag);
		if (typeFrag.equals(SINGER)) {
			String name = DBInterface.DBGetNameSinger(context, idFrag);
			searchView.setNameCharacter(name);
			// MyLog.e(TAB, "name : " + name);
		} else if (typeFrag.equals(MUSICIAN)) {
			String name = DBInterface.DBGetNameMusician(context, idFrag);
			searchView.setNameCharacter(name);
			// MyLog.e(TAB, "name : " + name);
		} else {
			searchView.setNameCharacter("");
		}
	}

	@Override
	public void OnFirstClick(Song song, int position) {
		if(MyApplication.bOffFirst == false){
			return;
		}
		if (song != null) {
			if (position != 0) {
				((MyApplication) getApplication()).firstReservedSong(
						song.getIndex5(), song.getTypeABC(), position);
			}
		}
	}

	/*
	 * @Override public void deviceErrorConnect(String msg) { MyLog.e(TAB,
	 * "deviceErrorConnect() " + msg); layoutDownload.setVisibility(View.GONE);
	 * layoutConnect.setVisibility(View.VISIBLE);
	 * animationView.ShowMessage(AnimationView.WIFI); }
	 * 
	 * @Override public void deviceDisConnect() { MyLog.e(TAB,
	 * "deviceDisConnect()"); layoutDownload.setVisibility(View.GONE);
	 * layoutConnect.setVisibility(View.VISIBLE);
	 * animationView.ShowMessage(AnimationView.WIFI);
	 * groupView.setConnected(MyGroupView.INCONNECT, ""); }
	 * 
	 * @Override public void deviceStartDownLoad() { MyLog.e(TAB,
	 * "deviceStartDownLoad()"); SetKeepScreenOn(true);
	 * layoutDownload.setVisibility(View.VISIBLE);
	 * layoutConnect.setVisibility(View.GONE); }
	 */

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
				if (!isURLReachable(TouchMainActivity.this)) {
//					Log.e("MAC PING SERVER", ".............FAIL.............");
					
					if(MyApplication.intWifiRemote == MyApplication.SONCA){
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								if(!isFinishing()){
//									popupViewUpadtePic
//											.setPopupLayout(TouchPopupViewUpdatePic.LAYOUT_NOWIFI);
//									myDialogUpdatePic.getWindow().setGravity(
//											Gravity.CENTER);
//									allowCancelOutside = true;
//									WindowManager.LayoutParams params = myDialogUpdatePic
//											.getWindow().getAttributes();
//									myDialogUpdatePic.getWindow().setAttributes(params);
//									myDialogUpdatePic.show();
								}
								
							}
						});
					}					
					
					return null;
				}

//				Log.e("MAC PING SERVER", ".............SUCCESS.............");
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
//	                Log.wtf("Connection", "Success !");
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
			
//			pingTime = System.currentTimeMillis();
//			new InterruptPingServerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
//			startPingServer();
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
						layoutMain.startAnimation(alpha);
					}
				});
				
				progressDialog.setOnDismissListener(new OnDismissListener() {
					
					@Override
					public void onDismiss(DialogInterface dialog) {
						AlphaAnimation alpha = new AlphaAnimation(0.5F, 1F);
						alpha.setDuration(0);
						alpha.setFillAfter(true);
						layoutMain.startAnimation(alpha);
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
		// Download One Image file;
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
		// TODO Parse UPDATE.xml
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

						// settings.edit().putInt("saveVersion", 0).commit();

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
									if(processCheckFreeSpace(totalSize) == false){
										showDialogMessage(getResources().getString(R.string.down_full_1) + ".\n" + getResources().getString(R.string.down_full_2));
										myDialogUpdatePic.dismiss();
									} else {
										myDialogUpdatePic.dismiss();
										downloadPic();											
									}
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
									// SAVE update
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

			// Log.e("DOWN INFO", "down file update_t.xml");

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

			// Log.e("DOWN PIC", "down file pic from dau may: " + txtFileName);

			String savePath = rootPath.concat("/" + txtFileName);
			((MyApplication) getApplication()).downloadArtistFile(savePath,
					txtFileName);
			DownloadState++;
			countDownPicDauMay++;
		}
	}

	private void saveUpdatePicSuccess() {
		// SAVE update
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

		// Log.d("PROCESS FIRST TIME", "app version: " + appVersion
		// + "  ...............");
		// Log.d("PROCESS FIRST TIME", "save version: " + saveVersion
		// + "  ...............");

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
		// Log.d("PROCESS FIRST TIME", "flag: " + flag);

		if (flag) {
			// Log.d("PROCESS FIRST TIME",
			// "............PROCESS..................");
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
				
				final String vongXoayDir = rootPath.concat("/VONGXOAY");
				File vongXoayDirFile = new File(vongXoayDir);
				if (!vongXoayDirFile.exists()) {
					vongXoayDirFile.mkdir();
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

					AppSettings setting = AppSettings
							.getInstance(getApplicationContext());
					
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
				mSettings.saveListOfflineVersion_SK90xx_New("");
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
		// Log.d("LOAD FAV LIST", "...............................");
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

		dialogExit = new Dialog(this);
		dialogExit.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogExit.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		dialogExit.setContentView(R.layout.touch_item_popup_exit_app);
		
		WindowManager.LayoutParams pa = dialogExit.getWindow()
				.getAttributes();
		pa.height = WindowManager.LayoutParams.MATCH_PARENT;
		pa.width = WindowManager.LayoutParams.MATCH_PARENT;
		pa.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_FULLSCREEN;
		
		dialogExit.show();
		
		popupExit = (TouchPopupViewExitApp) dialogExit
				.findViewById(R.id.myPopupExit);
		popupExit.setOnPopupExitAppListener(new OnPopupExitAppListener() {
			
			@Override
			public void OnExitYes() {
				((MyApplication) getApplication()).disconnectFromRemoteHost();
				((MyApplication) getApplication()).onDestroy();
				TouchMainActivity.this.finish();
				dialogExit.dismiss();
			}
			
			@Override
			public void OnExitNo() {
				dialogExit.dismiss();
			}
		});
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
				
				myDialogUpdateToc = new Dialog(TouchMainActivity.this);
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
				if (!isTocURLReachable(TouchMainActivity.this)) {
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
								if(processCheckFreeSpace(totalSize) == false){
									showDialogMessage(getResources().getString(R.string.down_full_1) + ".\n" + getResources().getString(R.string.down_full_2));
									myDialogUpdateToc.dismiss();
								} else {
									downloadToc(tocType);
									myDialogUpdateToc.dismiss();	
								}								
								
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
									FragmentTransaction fragmentTransaction = fragmentManager
											.beginTransaction();
									if (fragmentBase != null && SAVETYPE == SONG) {
										((TouchFragmentSong)fragmentBase).forceCloseDB();
									}
									fragmentTransaction.commit();
									
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
												
												ToFragmentDance(false);
												
												if(serverStatus != null){
													((MyApplication)getApplication()).startSyncServerStatusThread();
												}
											} else {
												clearDownloadTocResource();
												
												ToFragmentDance(false);
												
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

			progressDialog = new Dialog(TouchMainActivity.this);
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
		progressDialog = new Dialog(TouchMainActivity.this);
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
						
//						popupViewUpadtePic.setPopupLayout(PopupViewUpdatePic.LAYOUT_NOWIFI);
//						allowCancelOutside = true;
//						isDownloadingLyric = false;
//						MyApplication.freezeTime = System.currentTimeMillis();
//						myDialogUpdatePic.getWindow().setGravity(Gravity.CENTER);
//						WindowManager.LayoutParams params = myDialogUpdatePic
//								.getWindow().getAttributes();
//						myDialogUpdatePic.getWindow().setAttributes(params);
//						myDialogUpdatePic.show();
					}
					if(result == LoadCheckLyricNew.NODOWN){
						// popupViewUpadtePic.setPopupLayout(PopupViewUpdatePic.LAYOUT_UPTODATE_LYRIC);
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
							if(processCheckFreeSpace(totalsize) == false){
								showDialogMessage(getResources().getString(R.string.down_full_1) + ".\n" + getResources().getString(R.string.down_full_2));
								return;
							} 
							
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
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			if (fragmentBase != null && SAVETYPE == SONG) {
				((TouchFragmentSong)fragmentBase).forceCloseDB();
				// fragmentTransaction.remove(fragmentBase);
			}
			fragmentTransaction.commit();
			
			MyLog.e("processSwitchDB", "..........do switch");
			settings.edit().putInt("saveType", deviceType).commit();
			
			// DO SWITCH DB
			if (deviceType == MyApplication.SONCA_HIW || deviceType == MyApplication.SONCA_KM2 || deviceType == MyApplication.SONCA_TBT
					|| deviceType == MyApplication.SONCA_KB_OEM || deviceType == MyApplication.SONCA_KM1_WIFI
					|| deviceType == MyApplication.SONCA_KB39C_WIFI
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
			
			// processLoadFavList();

			if (SAVETYPE == SONG) {
				ToFragmentDance(false);
			}

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
		
		/*
		if(toastConnectSuccess != null){
			toastConnectSuccess.cancel();
			toastConnectSuccess = null;
		}
		LayoutInflater layoutInflater = (LayoutInflater)context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View toastRoot = layoutInflater.inflate(R.layout.touch_toast_connect_success, null);
		toastConnectSuccess = new Toast(context);
		toastConnectSuccess.setView(toastRoot);
		toastConnectSuccess.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,0, 0);
		toastConnectSuccess.setDuration(Toast.LENGTH_LONG);
		toastConnectSuccess.show();
		*/
		
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
		MyLog.e(TAB, "SAVETYPE : " + SAVETYPE);
		MyLog.e(TAB, "        ");
		DeviceStote deviceStote = new DeviceStote(context);
		deviceStote.setShareRefresh(MyApplication.bOffUserList);
		if(SAVETYPE.equals(NOTHING) || SAVETYPE.equals(SINGER) || 
			SAVETYPE.equals(MUSICIAN)){
			return;
		}
		SAVETYPE = "";
		resetActiveView();
		songView.setActive();
		viewBack.invalidate();
		searchView.invalidate();
		ShowSongFragment(SONG, songView);
		
		MyApplication.mSongTypeList = null;
		processTotalSongType(100);
		
		/*
		if(fragmentBase == null){
			return;
		}
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.detach(fragmentBase);
		fragmentTransaction.attach(fragmentBase);
		fragmentTransaction.commit();
		*/
		
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
		// Toast.makeText(context, "MainActivity : OnSendAdmin", Toast.LENGTH_SHORT).show();
		((MyApplication) getApplication()).getAdminPass();
		((MyApplication) getApplication()).setOnReceiverAdminPassListener(new OnReceiverAdminPassListener() {

			@Override
			public void OnReceiverAdminPass(String adminPass) {
				// Toast.makeText(getApplicationContext(), adminPass, Toast.LENGTH_SHORT).show();
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
					} else if (flag == TouchDeviceAdmin.LEFT_SLIDER) {
						ShowListDevice("");
						// ShowModel("", "");
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
							commandBox = new CommandBox(context, getWindow(), TouchMainActivity.this);
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
									volumnView.invalidate();
									keyView.invalidate();
									tempoView.invalidate();
									btnTone.invalidate();
									melodyView.invalidate();
									changePerfectView.invalidate();
									defaultControlView.invalidate();
									btnSinger.invalidate();
									btnScore.invalidate();
									repeatView.invalidate();
									nextView.invalidate();
									danceView.invalidate();
									pauseView.invalidate();
									btnVideo.invalidate();
									// ---------//
									volumnView.getCommandEnable();
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
		groupView.setNameRemote(model.getName());

		// FAKE
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		if (fragmentBase != null && SAVETYPE == SONG) {
			((TouchFragmentSong) fragmentBase).forceCloseDB();
		}
		fragmentTransaction.commit();

		DBInterface.DBReloadDatabase(this.getApplicationContext(),
				DbHelper.DBNameStar, model.getToc(), 0, 0, 0);
		
		MyApplication.mSongTypeList = null;
		processTotalSongType(100);
		
		if (SAVETYPE == SONG) {
			ToFragmentDance(false);
		}
	}
	
	@Override
	public void OnClickModel() {
		drawerLayout.closeDrawer(layoutConnect);
	}

	@Override
	public void OnShowTabModel() {
		if(serverStatus != null){
			SKServer skServer = ((MyApplication)getApplication()).getDeviceCurrent();
			if(skServer != null){
				String name = skServer.getName();
				String ip = skServer.getConnectedIPStr();
				// ShowModel(name, ip);
				ShowSelectList(name, ip);
				if (!drawerLayout.isDrawerOpen(layoutConnect)) {
					drawerLayout.openDrawer(layoutConnect);
				}
			}
		}
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
                	if(groupView!=null){
                		groupView.invalidate();	
                	} 
                	
                	if(colorLyricListener != null && boolShowKaraoke){
						colorLyricListener.OnMain_UpdateWifi();
					}
                }
		    }
		  };

	@Override
	public void OnHideKeyBoard() {
		hideKeyBoard();
	}

	@Override
	public void OnShowLyric() {
		hideKeyBoard();
		
	}
	
	@Override
	public void loadDatabaseWhenLaucherNoConnect() {

	}
	
	// ---------- ON/OFF ADMIN CONTROL
		public void setOnOffControlFull() {
			MyLog.e("setOnOffControlFull", "START...............................");
			
//			MyApplication.intCommandMedium = 4;
			
			((MyApplication) getApplication()).getAdminPass();
			((MyApplication) getApplication())
					.setOnReceiverAdminPassListener(new OnReceiverAdminPassListener() {
						@Override
						public void OnReceiverAdminPass(String sendPass) {
							int sendValue = MyApplication.intCommandMedium;
							MyLog.e("", "controlData in binary = " + Integer.toBinaryString(sendValue));
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
		ShowListDevice("");
	}

	@Override
	public void OnClickLanguage() {
		if(TouchDanceLinkView.DANCE == danceLinkView.getLayout()){
			return;
		}
		
		if(SAVETYPE.equals(SINGER) || SAVETYPE.equals(MUSICIAN)){
			if(searchView != null){
				searchView.clearFullCharacterView(true);
				if(MyApplication.flagRealKeyboard){
					searchEditText.setText("");
					edSearchOldStr = "";	
				}	
			}
		} else {
			if(searchView != null){
				searchView.clearFullCharacterView(false);
				if(MyApplication.flagRealKeyboard){
					searchEditText.setText("");
					edSearchOldStr = "";	
				}	
			}
		}	
		
		if(fragmentBase != null){
			// drawerLayout.closeDrawer(layoutConnect);
			FragmentTransaction transaction = fragmentManager.beginTransaction();
			transaction.detach(fragmentBase);
			transaction.attach(fragmentBase);
			transaction.commit();
		}
	}

	@Override
	public void OnCloseLanguage() {
		drawerLayout.closeDrawer(layoutConnect);
	}
	
	private DialogNoAddPlayList dialogNoAddPlayList = null;
	private void ShowDialogNoAddPlayList(String data){
//		if(serverStatus == null){
//			return;
//		}
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
	

	// ---------- SLIDER MELODY, TEMPO, KEY, VOLUMN
	private Dialog myDialogSlider;
	private Dialog myDialogSliderDance;
	private SliderView sliderView;
	private SliderMuteView muteView;
		
	public void setMydialogSliderDance(Dialog dialog){
		if(drawerLayout != null){
			drawerLayout.setMyDialogSliderDance(dialog);
		}
		myDialogSliderDance = dialog;
	}
	
	
	private Timer timerHideDialogThanhTruot;
	private void hideDialogThanhTruot(){
		if(timerHideDialogThanhTruot != null){
			timerHideDialogThanhTruot.cancel();
			timerHideDialogThanhTruot = null;
		}
		timerHideDialogThanhTruot = new Timer();
		timerHideDialogThanhTruot.schedule(new TimerTask() {
			
			@Override
			public void run() {
				handler.sendEmptyMessage(0);
			}
			
			private Handler handler = new Handler(){
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					if(myDialogSlider != null){
						myDialogSlider.dismiss();
					}
				}
			};
		}, 5000);
		
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
			TouchMainActivity.this.stopService(serviceIntent);
			if (MyApplication.updateFirmwareServerSocket != null) {
				try {
					MyApplication.updateFirmwareServerSocket.close();
				} catch (Exception e) {

				}
			}
		}

		// ESP_FIRM_BEGIN
		serviceIntent = new Intent(TouchMainActivity.this, MyHTTPService.class);
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
				TouchMainActivity.this.stopService(serviceIntent);
				if (MyApplication.updateFirmwareServerSocket != null) {
					try {
						MyApplication.updateFirmwareServerSocket.close();
					} catch (Exception e) {

					}
				}
			}

			// ESP_FIRM_BEGIN
			serviceIntent = new Intent(TouchMainActivity.this, MyHTTPService.class);
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
				TouchMainActivity.this.stopService(serviceIntent);
				if(MyApplication.updateFirmwareServerSocket != null){
					try {
						MyApplication.updateFirmwareServerSocket.close();	
					} catch (Exception e) {
						
					}							
				}					
			}	
			
			// ESP_FIRM_BEGIN
			serviceIntent = new Intent(TouchMainActivity.this, MyHTTPService.class);
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
		if(((MyApplication) TouchMainActivity.this.getApplication()).getListActive().size() > 95){
			
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
				} else if(msgString.equals("DOWNLOAD_ERROR")){
//					if (layoutDownload.getVisibility() != View.GONE) {
//						layoutDownload.setVisibility(View.GONE);
//					}
//					showDialogMessage(getResources().getString(R.string.down_full_1) + ".\n" + getResources().getString(R.string.down_full_2));
					
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
				if(processCheckFreeSpace(5 * 1024 * 1024) == false){
					showDialogMessage(getResources().getString(R.string.down_full_1) + ".\n" + getResources().getString(R.string.down_full_2));
				} else {
					ShowDialogUpdateFirmware(url, version, revision);						
				}	
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
					if (groupView == null) {
						return;
					}

					resultName = "";
					resultID = groupView.getCurrentNextIdx5();

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
								} else {
									Song mSong = processSongRealYoutube(tempID);
									if(mSong != null){
										resultName = mSong.getName();
										handler.sendEmptyMessage(2);
									}
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
						if (groupView != null) {
							groupView.setNextSongName(resultName, resultID);
							if(colorLyricListener != null){
								colorLyricListener.OnMain_setNextSongPlaylist(resultName, resultID);
							}
						}
					} else {
						if (groupView != null) {
							groupView.setNextSongName("", -1);
							if(colorLyricListener != null){
								colorLyricListener.OnMain_setNextSongPlaylist("", -1);
							}
						}
					}
				}
			};
		}, 100);
	}
	
	private void changeToColorLyric(){		
//		Intent mainIntent = new Intent(TouchMainActivity.this,
//				PlayLyricActivity_Video.class);
//		
//		TouchMainActivity.this.startActivity(mainIntent);
//		TouchMainActivity.this.finish();
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
						showMyPopupAsking(5, null, getResources().getString(R.string.switchscreen_1b), getResources().getString(R.string.switchscreen_2));	
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
					if(btnVideo != null){
						btnVideo.invalidate();
					}
				}
				
				@Override
				public void OnChangeAdminYouTube() {
					if(youtubeView != null){
						youtubeView.invalidate();
					}
					if(SAVETYPE == YOUTUBE){
						resetActiveView();
						ShowSongFragment(SONG, songView);
					}
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
			
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			if (fragmentBase != null && SAVETYPE == SONG) {
				((TouchFragmentSong)fragmentBase).forceCloseDB();
			}
			fragmentTransaction.commit();	
			
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
				
				AppSettings setting = AppSettings
						.getInstance(getApplicationContext());
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
							SearchMode.MODE_FULL, 0, 0, TouchMainActivity.this);
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
					if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
						songTypeList.add(0, new SongType(DbHelper.SongType_Youtube, getResources().getString(R.string.newSongType_3)));
					}
					if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && (MyApplication.flagSmartK_801 == false && MyApplication.flagSmartK_CB == false)
							&& MyApplication.flagAllowSearchOnline){
						songTypeList.add(0, new SongType(DbHelper.SongType_Online, getResources().getString(R.string.newSongType_2)));
					}
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
					
					songTypeList.add(new SongType(DbHelper.SongType_China,
							getResources().getString(R.string.newSongType_1)));
										
					for (int i = 0; i < songTypeList.size(); i++) {
						int tempTypeID = songTypeList.get(i).getID();

						int tempTotal = 0;

						if (tempTypeID == DbHelper.SongType_Remix) {
							tempTotal = DBInterface.DBCountTotalSongRemix(context,
									"", MEDIA_TYPE.ALL);
						} else if (tempTypeID == DbHelper.SongType_China) {
							int[] intIDs = new int[1]; 
							intIDs[0] = 3; // Nhac Hoa
							tempTotal = DBInterface.DBCountTotalSong(context, intIDs, "", SearchMode.MODE_MIXED, MEDIA_TYPE.ALL);
						} else if (tempTypeID == DbHelper.SongType_Online) {
							tempTotal = countTotalYoutubeExcel();
						} else if (tempTypeID == DbHelper.SongType_Youtube) {
							tempTotal = 0;
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
			groupView.invalidate();
			
			MyApplication.mSongTypeList = null;
			processTotalSongType(100);
			
			if(TouchDanceLinkView.DANCE == danceLinkView.getLayout()){
				return;
			}
			if(fragmentBase != null){
				FragmentTransaction transaction = fragmentManager.beginTransaction();
				transaction.detach(fragmentBase);
				transaction.attach(fragmentBase);
				transaction.commit();
			}
			
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

		@Override
		public void OnShowKM1_List() {
			int temp = MyApplication.intSelectList;
			
//			temp++;
//			if(temp > 3){
//				temp = 1;
//			}
			
			if(temp == MyApplication.SelectList_SONCA){
				temp = MyApplication.SelectList_ALL;
			} else if(temp == MyApplication.SelectList_ALL){
				temp = MyApplication.SelectList_SONCA;
			}
			
			MyApplication.intSelectList = temp;
			
			SharedPreferences sharedselect = getSharedPreferences("sharedselect", Context.MODE_PRIVATE);
			SharedPreferences.Editor editor = sharedselect.edit();
			editor.putInt("intselect", MyApplication.intSelectList);
			editor.commit();
			
			groupView.invalidate();
			
			MyApplication.mSongTypeList = null;
			processTotalSongType(100);
			
			if(TouchDanceLinkView.DANCE == danceLinkView.getLayout()){
				return;
			}
			if(fragmentBase != null){
				FragmentTransaction transaction = fragmentManager.beginTransaction();
				transaction.detach(fragmentBase);
				transaction.attach(fragmentBase);
				transaction.commit();
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
	public static boolean boolShowPreviewKaraoke = false;
	
	private OnColorLyricListener colorLyricListener;

	private int karaoke_ColorID = 0;
	private int karaoke_lastSize = 0;
	
	public interface OnColorLyricListener {
		public void OnMain_PausePlay(boolean flagPlay);
		public void OnMain_NewSong(int songID, int intMediaType, int midiShifTime, int typeABC);
		public void OnMain_Dance(boolean flagDance);
		public void OnMain_GetTimeAgain();
		public void OnMain_setCntPlaylist(int i);
		public void OnMain_setCurrSongPlaylist(String resultName);
		public void OnMain_setNextSongPlaylist(String resultName, int resultID);
		public void OnMain_VocalSinger(boolean flagVocalSinger);
		public void OnMain_CallVideoDefault();
		public void OnMain_RemoveSocket();
		public void OnMain_UpdateSocket(String serverName);
		public void OnMain_UpdateWifi();
		public void OnMain_StartTimerAutoConnect();
		public void OnMain_StopTimerAutoConnect();
		public void OnMain_UpdateControl();
		public void OnMain_DownloadMidiResult(boolean result, int id);
	}

	private void showKaraoke() {
		MyLog.d(TAB, "=showKaraoke===");
		if (isDestroyMainActivity == false) {
			return;
		}
		if (serverStatus == null) {
			return;
		}
		if (/* fragmentKaraoke != null && */boolShowKaraoke != true) {
			int id_ = serverStatus.getPlayingSongID();// get id song playing
			karaoke_ColorID = id_;
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				if (fragment_Karaoke != null) {
					fragmentTransaction.remove(fragment_Karaoke);
				}

				fragment_Karaoke = new FragmentKaraoke();
				// fragmentKaraoke = (fragmentKaraoke)
				// fragmentManager.findFragmentById(R.id.fragmentKaraoke);
				SKServer skServer = ((MyApplication)getApplication()).getDeviceCurrent();
				if(skServer != null){
					
					IpDeviceConnect = skServer.getConnectedIPStr();
					
				}
				Bundle bundle = new Bundle();
				//int id = cursor.getInt(0);
				bundle.putString("IP", IpDeviceConnect);
				bundle.putInt("id", id_);
				bundle.putInt("intMediaType", serverStatus.getMediaType());
				bundle.putInt("midiShifTime", serverStatus.getMidiShiftTime());
				bundle.putBoolean("Pause", pauseView.getPauseView());
				bundle.putInt("cntPlaylist", serverStatus.getReservedSongs().size());
				if(MyApplication.intSvrModel == MyApplication.SONCA || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
					bundle.putInt("mainTypeABC", serverStatus.getPlayingSongTypeABC());	
				} else {
					bundle.putInt("mainTypeABC", -1);
				}	
				bundle.putBoolean("VocalSinger", btnSinger.isSingerView());
				bundle.putString("ServerName", ((MyApplication) getApplication()).getDeviceCurrent().getName());
				colorLyricListener = (OnColorLyricListener)fragment_Karaoke;
				fragment_Karaoke.setArguments(bundle);
				fragmentTransaction.replace(R.id.fragmentKaraoke1,
						fragment_Karaoke, "KARAOKE");

				layoutColorLyric.setVisibility(View.VISIBLE);

				// fragmentTransaction.show(fragment_Karaoke);
				fragmentTransaction.commit();
				MyLog.i(TAB, "showKaraoke");

				boolShowKaraoke = true;
				btnVideo.invalidate();		
				nextView.requestLayout();
				repeatView.requestLayout();
				pauseView.requestLayout();	
		}
	}

	private void hideKaraoke() {
		MyApplication.freezeTime = System.currentTimeMillis();
		if (/* fragmentKaraoke != null && */boolShowKaraoke != false) {
			MyLog.d(TAB, "=hideKaraoke===");
			((FragmentKaraoke)fragment_Karaoke).stopKaraoke();
			
			FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
			fragmentTransaction.remove(fragment_Karaoke);
			fragmentTransaction.commit();
			MyLog.i(TAB, "hideKaraoke");
			boolShowKaraoke = false;
		}

		btnVideo.invalidate();
		nextView.requestLayout();
		repeatView.requestLayout();
		pauseView.requestLayout();
		colorLyricListener = null;
		layoutColorLyric.setVisibility(View.GONE);
	}

	@Override
	public void OnBackKaraoke() {
		hideKaraoke();
	}
	@Override
	public void OnBackReviewKaraoke(boolean iError) {
		MyApplication.freezeTime = System.currentTimeMillis();
		boolShowPreviewKaraoke = false;
		
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
								if(processCheckFreeSpace(totalVideoSize) == false){
									showDialogMessage(getResources().getString(R.string.down_full_1) + ".\n" + getResources().getString(R.string.down_full_2));
								} else {
									downloadVideo();
									myDialogUpdateToc.dismiss();
								}
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
				groupView.StopTimerAutoConnect();
				
				if(colorLyricListener != null && boolShowKaraoke){
					colorLyricListener.OnMain_StopTimerAutoConnect();
				}
			}
		});
		
	}
	
	private void groupView_StartTimerAutoConnect(){
		runOnUiThread(new Runnable() {
			public void run() {
				groupView.StartTimerAutoConnect();
				
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
	
	public String getCurrentSongName(){
		return groupView.getCurrectSongName();
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

		} else if (typeAsk == 5) { // SWITCH SCREEN		
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
									if (MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
										mainIntent = new Intent(TouchMainActivity.this, KTVMainActivity.class);
									}
									TouchMainActivity.this.startActivity(mainIntent);
									TouchMainActivity.this.finish();	
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
			}
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

		} else if (typeAsk == 10){ // BAT TAT AUTO DOWNLOAD
			if(dialogConnect == null){
				
				final boolean flagCheck = song.isHidden();
				
				dialogConnect = new TouchDialogConnect(getApplicationContext(), getWindow());
				dialogConnect.setDialogMessage(msg1, msg2);
				dialogConnect.setOnDialogConnectListener(new OnDialogConnectListener() {
					@Override
					public void OnYesListener() {						
						dialogConnect = null;						

						Download90xxInfo info = new Download90xxInfo(0, "mkv", flagCheck, 3, " ", " ", " ");
						
						if(flagCheck){
							if(MyApplication.listFullOnline == null || MyApplication.listFullOnline.size() == 0){
								showDialogMessage(getResources().getString(R.string.dialog_90xx_online_12));
								return;
							}
							
							if(dialogSK90xxOnline != null){
								dialogSK90xxOnline.onYesAuto();
							}

							ArrayList<String> listD = new ArrayList<String>();	
							for (int i = 0; i < MyApplication.listFullOnline.size(); i++) {
//								listD.add("3,mkv," + MyApplication.listFullOnline.get(i).getDownLink() + "\r\n");
								
								final Song tempSong = MyApplication.listFullOnline.get(i);
								
								final MEDIA_TYPE tempMediaType = tempSong.getMediaType();
								if(tempMediaType == MEDIA_TYPE.MP3){ // mp3	

									String downloadFileType = "mp3";
									int folderType = 2;							

									if(!tempSong.getMidiDownLink().equals("")){
										downloadFileType = "mid";
										folderType = 2;									
										if(tempSong.isIs2Stream()){
											folderType = 1;
										}
										listD.add(folderType + "," + downloadFileType + "," + MyApplication.listFullOnline.get(i).getMidiDownLink() + "\r\n");
									}	
									
									
									downloadFileType = "mp3";
									folderType = 2;	
									if(tempSong.isIs2Stream()){
										folderType = 1;
									}
									listD.add(folderType + "," + downloadFileType + "," + MyApplication.listFullOnline.get(i).getDownLink() + "\r\n");
																		
																											
								} else { // others
									String downloadFileType = "mkv";
									int folderType = 3;
									
									if(tempMediaType == MEDIA_TYPE.MIDI){
										downloadFileType = "mid";
										folderType = 0;
									} 
									listD.add(folderType + "," + downloadFileType + "," + MyApplication.listFullOnline.get(i).getDownLink() + "\r\n");																		
								}
							}
							
							showDialogMessage(getResources().getString(R.string.dialog_90xx_online_13));
							
							((MyApplication) getApplication()).sendDonwloadYouTubeCommand(info, 3, listD);						
						} else {
							if(dialogSK90xxOnline != null){
								dialogSK90xxOnline.onYesAutoNot();
							}
							
							showDialogMessage(getResources().getString(R.string.dialog_90xx_online_14));
							
							((MyApplication) getApplication()).sendDonwloadYouTubeCommand(info, 3);	
						}
					}
					
					@Override
					public void OnFinishListener() {
						dialogConnect = null;						
					}

				});
				dialogConnect.showToast();
			}

		} else if (typeAsk == 11){ // HUY AN
			if(dialogConnect == null){
				
				dialogConnect = new TouchDialogConnect(getApplicationContext(), getWindow());
				dialogConnect.setDialogMessage(msg1, msg2);
				dialogConnect.setOnDialogConnectListener(new OnDialogConnectListener() {
					@Override
					public void OnYesListener() {						
						dialogConnect = null;						
												
						ArrayList<Integer> listSong = new ArrayList<Integer>();
						listSong.add(song.getId());
						
						ArrayList<Integer> listFinal = MyApplication.processCancelHidden90xx(listSong);
						
						if(listFinal == null){
							return;
						}
						
						if(listFinal.size() == 0){
							listFinal.add(0);
						}
						
//						for (int j = 0; j < listFinal.size(); j++) {
//							MyLog.d(" ", listFinal.get(j) + "");
//						}
						
						if(listFinal.size() <= 300){
							((MyApplication)context).sendHiddenList_90xx(listFinal, 0);	
							flagSendingHidden = false;
						} else {
							flagSendingHidden = true;
							int countTime = 0;
							do {	
								boolean flagOut = false;
								final ArrayList<Integer> listTemp = new ArrayList<Integer>();
								for (int i = countTime * 300; i < countTime * 300 + 300; i++) {
									if(i == listFinal.size()){
										flagOut = true;
										break;
									}
									listTemp.add(listFinal.get(i));
								}
								
								if(countTime == 0){
									((MyApplication) getApplication()).sendHiddenList_90xx(listTemp, 0);	
								} else {
									if(listTemp.size() == 0){
										return;
									}
									
									new Handler().postDelayed(new Runnable() {
										@Override
										public void run() {
											((MyApplication) getApplication()).sendHiddenList_90xx(listTemp, 1);	
											flagSendingHidden = false;
										}
									}, countTime * 2000);
								}									
								
								countTime++;
								
								if(flagOut){
									break;
								}
							} while (true);
						}
					}
					
					@Override
					public void OnFinishListener() {
						dialogConnect = null;						
					}

				});
				dialogConnect.showToast();
			}

		} else if (typeAsk == 12){ // HUY AN TAT CA
			if(dialogConnect == null){
				
				dialogConnect = new TouchDialogConnect(getApplicationContext(), getWindow());
				dialogConnect.setDialogMessage(msg1, msg2);
				dialogConnect.setOnDialogConnectListener(new OnDialogConnectListener() {
					@Override
					public void OnYesListener() {						
						dialogConnect = null;						

						ArrayList<Integer> listTemp = new ArrayList<Integer>();
						listTemp.add(0);
						((MyApplication)context).sendHiddenList_90xx(listTemp, 0);						
					}
					
					@Override
					public void OnFinishListener() {
						dialogConnect = null;						
					}

				});
				dialogConnect.showToast();
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

	private void reloadYoutubeTable90xx(){
    	MyLog.e(TAB, "reloadYoutubeTable90xx VERY START............");
    	
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
        	if(!fYou.exists()){
        		return;
        	}
        	
        	listExcelSong = new ArrayList<Song>();
        	processReadXMLFile(ytPath + "/" + MyApplication.YT_90xx);
        	
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
        	
        	// PROCESS LIST OFFLINE 90XX
        	processListOffline_90xx();
        	
		} catch (Exception e) {
			e.printStackTrace();
		}
    	   
    	flagFinishReloadYoutube = true;
    	
    	MyLog.e(TAB, "reloadYoutubeTable90xx VERY END............");
    }	
	
	private void processListOffline_90xx(){
		if(MyApplication.intSvrModel == MyApplication.SONCA && MyApplication.intSvrCode >= MyApplication.newAPI90xxVersion){
			String appRootPath = android.os.Environment
					.getExternalStorageDirectory()
					.toString()
					.concat(String.format("/%s/%s", "Android/data",
							getPackageName()));
			String offlinePath = appRootPath.concat("/YOUTUBE/" + MyApplication.YT_90xx_OFFLINE);
			File offlineFile = new File(offlinePath);
			if(offlineFile.exists()){
				try {
					AppSettings settings = AppSettings.getInstance(getApplicationContext());
					
					MyApplication.generateListOfflineFromByte(getByteFromFile(offlinePath));	
					MyLog.e(TAB, "listDownloadOffline 90xx = " + MyApplication.listDownloadOffline.size());
					String strSave = "";
					for (int i = 0; i < MyApplication.listDownloadOffline.size(); i++) {
//						MyLog.d(TAB, " " + MyApplication.listDownloadOffline.get(i));
						strSave += MyApplication.listDownloadOffline.get(i) + "_";
					}
					MyLog.d(TAB, "strSave = " + strSave);
					
					// check find delete song
					String strOldDownload = settings.getStrDownload90xx();
//					MyLog.d(TAB, "strOldDownload = " + strOldDownload);
					try {
						if(strOldDownload.length() > 0){
							ArrayList<Integer> oldList = new ArrayList<Integer>();
							oldList = MyApplication.generateListOfflineFromString(strOldDownload, false);
							
//							MyLog.d(TAB, "oldList = " + oldList.size());
							
							ArrayList<Integer> listDelete = new ArrayList<Integer>();
							for (int i = 0; i < oldList.size(); i++) {
								if(!MyApplication.listDownloadOffline.contains(oldList.get(i))){
									listDelete.add(oldList.get(i));
								}
							}
							
							MyLog.d(TAB, "listDelete = " + listDelete.size());
							if(listDelete.size() > 0){
								DBInterface.DBProcessDeleteIntoSongTable(context, listDelete);
							}
						}	
					} catch (Exception e) {
						e.printStackTrace();
					}					
					
					// check find insert song
					ArrayList<Song> listStep1 = new ArrayList<Song>();
					if(listExcelSong.size() > 0){
						for (int i = 0; i < listExcelSong.size(); i++) {
							if(MyApplication.listDownloadOffline.contains(listExcelSong.get(i).getId())){
								listStep1.add(listExcelSong.get(i));
							}
						}						
					}
					MyLog.e(TAB, "listStep1 = " + listStep1.size());
					
					if(listStep1.size() > 0){
						ArrayList<Song> listStep2 = new ArrayList<Song>();
						listStep2 = DBInterface.DBSearchListSongID(listStep1, context);		
						
						MyLog.e(TAB, "listStep2 = " + listStep2.size());
						
						ArrayList<Song> listInsertOffline = new ArrayList<Song>();
						if(listStep2.size() == 0){
							listInsertOffline = listStep1;
						} else {
							for (int i = 0; i < listStep1.size(); i++) {
								if(listStep2.contains(listStep1.get(i))){
									
								} else {
									listInsertOffline.add(listStep1.get(i));
								}
							}
						}
						
						MyLog.e(TAB, "listInsertOffline = " + listInsertOffline.size());
						
						if(listInsertOffline.size() > 0){
							DBInterface.DBProcessInsertIntoSongTable(context, listInsertOffline);
							
							if (strSave.endsWith("-")) {
								strSave = strSave.substring(0, strSave.length() - 1);
							}
							settings.setStrDownload90xx(strSave);
						}							
						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}	
		}
	}
	
	private boolean flagFinishReloadYoutube = true;
	private void reloadYoutubeTable() {
		flagFinishReloadYoutube = false;
		
		if(MyApplication.intSvrModel == MyApplication.SONCA && MyApplication.intSvrCode >= MyApplication.newAPI90xxVersion){
			reloadYoutubeTable90xx();
			return;
		}
		
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
			if(MyApplication.flagAllowSearchOnline){
				processReadExcelFile(ytPath + "/" + MyApplication.YT_FILENAME);	
			}			
			
			String appRootPath = android.os.Environment
					.getExternalStorageDirectory()
					.toString()
					.concat(String.format("/%s/%s", "Android/data",
							getPackageName()));
        	String sbPath = appRootPath + "/" + MyApplication.ADD_FILE_SAMBA;
        	File sbFile = new File(sbPath);
        	if(sbFile.exists()){
        		getListSambaFromFile();
        	}
        	
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
	
	private void processReadXMLFile(String filePath) {
		MyLog.d(TAB, "processReadXMLFile VERY START..............");
		MyLog.e(" ", "filePath = " + filePath);

		File fExcel = new File(filePath);
		if (!fExcel.exists()) {
			return;
		}

		// READ XML
		try {
			InputStream is = new FileInputStream(filePath);
			Document doc = XmlUtils.convertToDocument(is);
			NodeList nodeList = doc.getElementsByTagName("Table1");

			Song tempSong = new Song();
			
			for (int i = 0; i < nodeList.getLength(); i++) {
				tempSong = new Song();
				
				Node node = nodeList.item(i);
				Element e = (Element) node;
				NodeList nlTemp = e.getElementsByTagName("PlayLink");
				Node nTemp = nlTemp.item(0);
				String strTemp = nTemp.getTextContent();
				tempSong.setPlayLink(strTemp);

				nlTemp = e.getElementsByTagName("DownLink");
				nTemp = nlTemp.item(0);
				strTemp = nTemp.getTextContent();
				tempSong.setDownLink(strTemp);
				
				nlTemp = e.getElementsByTagName("Index5");
				nTemp = nlTemp.item(0);
				strTemp = nTemp.getTextContent();
				tempSong.setIndex5(Integer.parseInt(strTemp));
				
				nlTemp = e.getElementsByTagName("Index6");
				nTemp = nlTemp.item(0);
				strTemp = nTemp.getTextContent();
				tempSong.setId(Integer.parseInt(strTemp));
				
				nlTemp = e.getElementsByTagName("Title");
				nTemp = nlTemp.item(0);
				strTemp = nTemp.getTextContent().trim();
				strTemp = strTemp.replace("  ", " ");
				strTemp = strTemp.replaceAll("'", "");
				tempSong.setName(strTemp);				
				String rawName = StringUtils.getRawString(strTemp, LANG_INDEX.ALL_LANGUAGE);
				tempSong.setTitleRaw(rawName);				
				tempSong.setShortName(StringUtils.getPinyinString(strTemp, LANG_INDEX.ALL_LANGUAGE));
				
				nlTemp = e.getElementsByTagName("Singer");
				nTemp = nlTemp.item(0);
				strTemp = nTemp.getTextContent();
				strTemp = strTemp.replaceAll("'", "");
				tempSong.setSingerName(strTemp);		
				
				nlTemp = e.getElementsByTagName("SongType");
				nTemp = nlTemp.item(0);
				strTemp = nTemp.getTextContent();
				tempSong.setTheloaiName(strTemp);
				
				nlTemp = e.getElementsByTagName("Lyrics");
				nTemp = nlTemp.item(0);
				strTemp = nTemp.getTextContent();
				strTemp = strTemp.replaceAll("'", "");
				tempSong.setLyric(strTemp);
				
				nlTemp = e.getElementsByTagName("Language");
				nTemp = nlTemp.item(0);
				strTemp = nTemp.getTextContent();
				int intLang = 0;
				if(strTemp.contains("english")){
					intLang = 1;
				}
				tempSong.setLanguageID(intLang);
				
				nlTemp = e.getElementsByTagName("Remix");
				nTemp = nlTemp.item(0);
				strTemp = nTemp.getTextContent();
				boolean bolRemix = false;
				if(strTemp.contains("1")){
					bolRemix = true;
				}
				tempSong.setRemix(bolRemix);
				
				try {
					nlTemp = e.getElementsByTagName("Vocal");
					nTemp = nlTemp.item(0);
					strTemp = nTemp.getTextContent();
					boolean bolVocal = false;
					if(strTemp.contains("1")){
						bolVocal = true;
					}
					tempSong.setVocalSinger(bolVocal);
					
					nlTemp = e.getElementsByTagName("Stream2");
					nTemp = nlTemp.item(0);
					strTemp = nTemp.getTextContent();
					boolean bol2Stream = false;
					if(strTemp.contains("1")){
						bol2Stream = true;
					}
					tempSong.setIs2Stream(bol2Stream);
					
					nlTemp = e.getElementsByTagName("MidiDownLink");
					nTemp = nlTemp.item(0);
					strTemp = nTemp.getTextContent();
					tempSong.setMidiDownLink(strTemp);
				} catch (Exception e2) {
					
				}
				
				nlTemp = e.getElementsByTagName("MediaType");
				nTemp = nlTemp.item(0);
				strTemp = nTemp.getTextContent();
				int temp = Integer.parseInt(strTemp);
				if(temp == 1){
					tempSong.setMediaType(MEDIA_TYPE.MIDI);
				} else if(temp == 2){
					tempSong.setMediaType(MEDIA_TYPE.MP3);
				} else {
					tempSong.setMediaType(MEDIA_TYPE.VIDEO);
				}
						
				tempSong.setTypeABC(0);

				listExcelSong.add(tempSong);
			}
            
		} catch (Exception e) {
			e.printStackTrace();
			if(fExcel.exists()){
				fExcel.delete();
			}
			AppSettings settings = AppSettings.getInstance(getApplicationContext());
			if(MyApplication.intSvrModel == MyApplication.SONCA){
				settings.saveYouTubeVersion_SK90xx(0);
			} else {
				settings.saveYouTubeVersion(0);	
			}
			
		}

		// MyLog.d(TAB, "processExcelFile VERY END..............");
	}
	
	private long breakYouTube = 0;

	@Override
	public void onPlayYouTube(Song song) {
		if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && song.isSambaSong()){
			return;
		}
		
		if((System.currentTimeMillis() - breakYouTube) < 500){
			return;
		}
		
		breakYouTube = System.currentTimeMillis();
		
		MyLog.e(TAB, "onPlayYouTube -- " + song.getId() + " -- " + song.getName());
		
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
	public void onDownYouTube(Song song) {		
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
	
	// ---------- PREVIEW YOUTUBE
	
	private void previewYouTube(Song song) {

		Intent intent = new Intent(TouchMainActivity.this, YoutubePlayerActivity.class);
		Bundle b = new Bundle();
		b.putString("youtubePath", song.getPlayLink());
		intent.putExtras(b);
		startActivity(intent);

	}

	
	@Override
	public void OnUSBTOC() {
		if(MyApplication.intSvrModel == MyApplication.SONCA && MyApplication.intSvrCode >= MyApplication.newAPI90xxVersion){
			showDialogSK90xxOnline();
		} else {
			showDialogAddSong();	
		}
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
					
					if(searchView != null){
						searchView.invalidate();
					}
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
	
	//-----------------------------------------------------------
	private long sk90xxOnlineBreak = 0;
	
	private DialogSK90xxOnline dialogSK90xxOnline = null;
	private void showDialogSK90xxOnline(){
		MyLog.e(TAB, "showDialogSK90xxOnline");
		if(serverStatus == null){
			return;
		}
		if(dialogSK90xxOnline == null){
			dialogSK90xxOnline = new DialogSK90xxOnline(context, getWindow(), fragmentManager);
			dialogSK90xxOnline.setOnBaseDialogListener(new OnBaseDialogListener() {
				
				@Override
				public void OnShowDialog() {
					
				}
				
				@Override
				public void OnFinishDialog() {
					dialogSK90xxOnline = null;
					
					if(searchView != null){
						searchView.invalidate();
					}
				}
			});
			dialogSK90xxOnline.setOnDialogSK90xxOnlineListener(new OnDialogSK90xxOnlineListener() {

				@Override
				public void OnAddSong(ArrayList<Song> info) {
					MyLog.e(TAB, "DialogSK90xxOnline -- OnAddSong -- " + info.size());
					
					if(System.currentTimeMillis() - sk90xxOnlineBreak < 2000){		
						showDialogMessage(getResources().getString(R.string.dialog_90xx_online_19));
						return;
					}
					
					if(info.size() == 1){
						
					} else {
						if(System.currentTimeMillis() - sk90xxOnlineBreak >= 2000){
							showDialogMessage(getResources().getString(R.string.dialog_90xx_online_15));
						}						
					}
					
					sk90xxOnlineBreak = System.currentTimeMillis();
										
					for (int i = 0; i < info.size(); i++) {
						final Song tempSong = info.get(i);
						
						final MEDIA_TYPE tempMediaType = tempSong.getMediaType();
						if(tempMediaType == MEDIA_TYPE.MP3){ // mp3		
							if(!tempSong.getMidiDownLink().equals("")){
								new Handler().postDelayed(new Runnable() {
									@Override
									public void run() {
										sk90xxOnlineBreak = System.currentTimeMillis();
										
										String downloadFileType = "mid";
										int folderType = 2;									
										if(tempSong.isIs2Stream()){
											folderType = 1;
										}
										
										Download90xxInfo tempData = new Download90xxInfo(tempSong.getId(), downloadFileType, tempSong.isVocalSinger(), 
												folderType, " ", " ", tempSong.getMidiDownLink());
										((MyApplication) getApplication()).sendDonwloadYouTubeCommand(tempData, 0);
									}
								}, i * 500);
								
							}
							
							new Handler().postDelayed(new Runnable() {
								@Override
								public void run() {
									sk90xxOnlineBreak = System.currentTimeMillis();
									
									String downloadFileType = "mp3";
									int folderType = 2;									
									if(tempSong.isIs2Stream()){
										folderType = 1;
									}
									
									Download90xxInfo tempData = new Download90xxInfo(tempSong.getId(), downloadFileType, tempSong.isVocalSinger(), 
											folderType, " ", " ", tempSong.getDownLink());
									((MyApplication) getApplication()).sendDonwloadYouTubeCommand(tempData, 0);
								}
							}, i * 500 + 200);							
							
							
						} else { // others
							new Handler().postDelayed(new Runnable() {
								@Override
								public void run() {
									sk90xxOnlineBreak = System.currentTimeMillis();
									
									String downloadFileType = "mkv";
									int folderType = 3;
									
									if(tempMediaType == MEDIA_TYPE.MIDI){
										downloadFileType = "mid";
										folderType = 0;
									} 
									
									Download90xxInfo tempData = new Download90xxInfo(tempSong.getId(), downloadFileType, tempSong.isVocalSinger(), 
											folderType, " ", " ", tempSong.getDownLink());
									((MyApplication) getApplication()).sendDonwloadYouTubeCommand(tempData, 0);
								}
							}, i * 500);
							
						}	
						
					}
					
				}

				@Override
				public void OnCancelAddSong(ArrayList<Song> info) {
					MyLog.e(TAB, "DialogSK90xxOnline -- OnCancelAddSong -- " + info.size());
					
					if(System.currentTimeMillis() - sk90xxOnlineBreak < 2000){	
						showDialogMessage(getResources().getString(R.string.dialog_90xx_online_19));
						return;
					}
										
					if(info.size() == 1){
						
					} else {
						if(System.currentTimeMillis() - sk90xxOnlineBreak >= 2000){
							showDialogMessage(getResources().getString(R.string.dialog_90xx_online_16));
						}
					}
					
					sk90xxOnlineBreak = System.currentTimeMillis();
					
					for (int i = 0; i < info.size(); i++) {
						final Song tempSong = info.get(i);
						
						final MEDIA_TYPE tempMediaType = tempSong.getMediaType();
						if(tempMediaType == MEDIA_TYPE.MP3){ // mp3							
							if(!tempSong.getMidiDownLink().equals("")){
								new Handler().postDelayed(new Runnable() {
									@Override
									public void run() {
										sk90xxOnlineBreak = System.currentTimeMillis();
										String downloadFileType = "mid";
										int folderType = 2;									
										if(tempSong.isIs2Stream()){
											folderType = 1;
										}
										
										Download90xxInfo tempData = new Download90xxInfo(tempSong.getId(), downloadFileType, tempSong.isVocalSinger(), 
												folderType, " ", " ", tempSong.getMidiDownLink());
										((MyApplication) getApplication()).sendDonwloadYouTubeCommand(tempData, 1);
									}
								}, i * 500);
								
							}
							
							new Handler().postDelayed(new Runnable() {
								@Override
								public void run() {
									sk90xxOnlineBreak = System.currentTimeMillis();
									String downloadFileType = "mp3";
									int folderType = 2;									
									if(tempSong.isIs2Stream()){
										folderType = 1;
									}
									
									Download90xxInfo tempData = new Download90xxInfo(tempSong.getId(), downloadFileType, tempSong.isVocalSinger(), 
											folderType, " ", " ", tempSong.getDownLink());
									((MyApplication) getApplication()).sendDonwloadYouTubeCommand(tempData, 1);
								}
							}, i * 500 + 200);	
							
						} else { // others
							new Handler().postDelayed(new Runnable() {
								@Override
								public void run() {
									sk90xxOnlineBreak = System.currentTimeMillis();
									String downloadFileType = "mkv";
									int folderType = 3;
									
									if(tempMediaType == MEDIA_TYPE.MIDI){
										downloadFileType = "mid";
										folderType = 0;
									} 
									
									Download90xxInfo tempData = new Download90xxInfo(tempSong.getId(), downloadFileType, tempSong.isVocalSinger(), 
											folderType, " ", " ", tempSong.getDownLink());
									((MyApplication) getApplication()).sendDonwloadYouTubeCommand(tempData, 1);
								}
							}, i * 500);
							
						}	
						
					}
					
				}

				@Override
				public void OnUpdateAutoDownload(boolean flagCheck, ArrayList<Song> listDownload) {
					MyLog.e(TAB, "DialogSK90xxOnline -- OnUpdateAutoDownload -- " + flagCheck);
					
					if(System.currentTimeMillis() - sk90xxOnlineBreak < 2000){			
						showDialogMessage(getResources().getString(R.string.dialog_90xx_online_19));
						return;
					}
					
					sk90xxOnlineBreak = System.currentTimeMillis();
					
					Song s = new Song();
					s.setHidden(flagCheck);
					
					if(flagCheck){
						showMyPopupAsking(10, s, getResources().getString(R.string.dialog_90xx_online_17a), getResources().getString(R.string.dialog_90xx_online_17b));
					} else {
						showMyPopupAsking(10, s, getResources().getString(R.string.dialog_90xx_online_18a), getResources().getString(R.string.dialog_90xx_online_18b));	
					}	
					
				}

				@Override
				public void OnUpdateAutoMessage(String msg) {
					showDialogMessage(msg);
				}

				@Override
				public void OnSKChangeDialog(int dialogType) {
					if(dialogType == 1){
						showDialogSK90xxHidden();
						
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								if(dialogSK90xxOnline != null){
									dialogSK90xxOnline.dismissDialog(false);
									dialogSK90xxOnline = null;
								}
							}
						}, 2000);
					} else {
						showDialogSK90xxOnline();
						
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								if(dialogSK90xxHidden != null){
									dialogSK90xxHidden.dismissDialog(false);
									dialogSK90xxHidden = null;
								}
							}
						}, 2000);
					}					
					
				}
				
			});
			dialogSK90xxOnline.showDialog();
			
			if(drawerLayout != null){
				drawerLayout.closeDrawers();
			}
			
		}
	}
	
	private long sk90xxHiddenBreak = 0;
	private DialogSK90xxHidden dialogSK90xxHidden = null;
	private void showDialogSK90xxHidden(){
		MyLog.e(TAB, "showDialogSK90xxHidden");
		if(serverStatus == null){
			return;
		}
		if(dialogSK90xxHidden == null){
			dialogSK90xxHidden = new DialogSK90xxHidden(context, getWindow(), fragmentManager);
			dialogSK90xxHidden.setOnBaseDialogListener(new app.sonca.Dialog.ScoreLayout.BaseDialog2.OnBaseDialogListener() {
				
				@Override
				public void OnShowDialog() {
					
				}
				
				@Override
				public void OnFinishDialog() {
					dialogSK90xxHidden = null;
					
					if(searchView != null){
						searchView.invalidate();
					}
				}
			});
			
			dialogSK90xxHidden.setOnDialogSK90xxHiddenListener(new OnDialogSK90xxHiddenListener() {
				
				@Override
				public void OnUpdateHiddenMessage(String msg) {
					showDialogMessage(msg);
				}
				
				@Override
				public void OnCancelAddSong(ArrayList<Song> info) {
					MyLog.e(TAB, "dialogSK90xxHidden -- OnCancelAddSong -- " + info.size());
					if(info.size() > 0){
						if(System.currentTimeMillis() - sk90xxHiddenBreak < 2000){	
							showDialogMessage(getResources().getString(R.string.dialog_90xx_hid_10));
							return;
						}
						
						sk90xxHiddenBreak = System.currentTimeMillis();
						
						Song s = info.get(0);
						showMyPopupAsking(11, s, getResources().getString(R.string.dialog_90xx_hid_8a), "");
						
					}
				}
				
				@Override
				public void OnAddSong(ArrayList<Song> info) {
					MyLog.e(TAB, "dialogSK90xxHidden -- OnAddSong -- " + info.size());	
					
					if(System.currentTimeMillis() - sk90xxHiddenBreak < 2000){				
						showDialogMessage(getResources().getString(R.string.dialog_90xx_hid_10));
						return;
					}
					
					sk90xxHiddenBreak = System.currentTimeMillis();
								
					if(info.size() > 0){
						flagSendingHidden = false;
						
						ArrayList<Integer> listSong = new ArrayList<Integer>();
						for (int i = 0; i < info.size(); i++) {
							listSong.add(info.get(i).getId());
						}
						
						ArrayList<Integer> listFinal = MyApplication.processAddHidden90xx(listSong);
						
						if(listFinal == null || listFinal.size() == 0){
							return;
						}
						
//						for (int j = 0; j < listFinal.size(); j++) {
//							MyLog.d(" ", listFinal.get(j) + "");
//						}
						
//						listFinal.clear();
//						for (int i = 0; i < 700; i++) {
//							listFinal.add(100000 + i);
//						}

//						showDialogMessage(context.getResources().getString(R.string.dialog_90xx_hid_4));
						
						if(listFinal.size() <= 300){
							((MyApplication)context).sendHiddenList_90xx(listFinal, 0);	
							flagSendingHidden = false;
						} else {
							flagSendingHidden = true;
							int countTime = 0;
							do {	
								boolean flagOut = false;
								final ArrayList<Integer> listTemp = new ArrayList<Integer>();
								for (int i = countTime * 300; i < countTime * 300 + 300; i++) {
									if(i == listFinal.size()){
										flagOut = true;
										break;
									}
									listTemp.add(listFinal.get(i));
								}
								
								if(countTime == 0){
									((MyApplication) getApplication()).sendHiddenList_90xx(listTemp, 0);	
								} else {
									if(listTemp.size() == 0){
										return;
									}
									
									new Handler().postDelayed(new Runnable() {
										@Override
										public void run() {
											((MyApplication) getApplication()).sendHiddenList_90xx(listTemp, 1);	
											flagSendingHidden = false;
										}
									}, countTime * 2000);
								}									
								
								countTime++;
								
								if(flagOut){
									break;
								}
							} while (true);
						}
						
					}
					
				}

				@Override
				public void OnSKChangeDialog(int dialogType) {
					if(dialogType == 1){
						showDialogSK90xxHidden();
						
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								if(dialogSK90xxOnline != null){
									dialogSK90xxOnline.dismissDialog(false);
									dialogSK90xxOnline = null;
								}
							}
						}, 2000);
					} else {
						showDialogSK90xxOnline();
						
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								if(dialogSK90xxHidden != null){
									dialogSK90xxHidden.dismissDialog(false);
									dialogSK90xxHidden = null;
								}
							}
						}, 2000);
					}	
					
				}

				@Override
				public void OnSKUpdateSearch() {
					if(serverStatus != null){
						ToFragmentDance(serverStatus.danceMode() == 1);	
					}	
				}

				@Override
				public void OnCancelAll() {
					showMyPopupAsking(12, null, getResources().getString(R.string.dialog_90xx_hid_9a), getResources().getString(R.string.dialog_90xx_hid_9b));
					
				}
			});
			dialogSK90xxHidden.showDialog();
		}
	}
	//-----------------------------------------------------------
	
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
//			if (fExcel.exists()) {
//				fExcel.delete();
//			}
		}
		
		MyLog.e(TAB, "getListAddFromFile VERY END -- size = " + listResult.size());
		return listResult;
	}

	@Override
	public void OnShowReviewKaraoke(Song song) {
		MyLog.i(TAB, "OnShowReviewKaraoke=serverStatus="+serverStatus);
		hideKeyBoard();
//		if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && MyApplication.flagSmartK_801){
//			showDialogMessage(getResources().getString(R.string.msg_a_1) + " " + getResources().getString(R.string.msg_a_2));
//			return;
//		}
		if(serverStatus == null){
			ShowDialogNoAddPlayList(getResources().getString(R.string.activity_karaoke_ReviewKaraokeNotice));
			return;
		}
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		if (fragment_Karaoke != null) {
			fragmentTransaction.remove(fragment_Karaoke);
		}

		fragment_Karaoke = new FragmentReviewKaraoke();
		// fragmentKaraoke = (fragmentKaraoke)
		// fragmentManager.findFragmentById(R.id.fragmentKaraoke);
		SKServer skServer = ((MyApplication)getApplication()).getDeviceCurrent();
		if(skServer != null){
			
			IpDeviceConnect = skServer.getConnectedIPStr();
			
		}
		Bundle bundle = new Bundle();
		//int id = cursor.getInt(0);
		bundle.putString("IP", IpDeviceConnect);
		bundle.putInt("id", song.getId());
		bundle.putInt("intMediaType",(song.getMediaType()).ordinal());
		colorLyricListener = (OnColorLyricListener)fragment_Karaoke;
		fragment_Karaoke.setArguments(bundle);
		fragmentTransaction.replace(R.id.fragmentKaraoke1,
				fragment_Karaoke, "KARAOKE");

		layoutColorLyric.setVisibility(View.VISIBLE);

		// fragmentTransaction.show(fragment_Karaoke);
		fragmentTransaction.commit();
		
		boolShowPreviewKaraoke = true;		
	}
	
	//-------------------------------------------------------------------
	
	
	private ArrayList<MyAppInfo> listProcessApp;
	private void testDataFromYoutube(){	
			
		ArrayList<MyAppInfo> list = getListAppFromFile();
		Collections.sort(list, new Comparator<MyAppInfo>() {
	        @Override
	        public int compare(MyAppInfo s1, MyAppInfo s2) {
	            return s1.getSort() < s2.getSort() ? 1 : 0;
	        }
	    });
		
		listProcessApp = list;
		
		readYoutubeFeed();
		
//		readYoutubeFeed(list.get(0).getVidLink());
//		readYoutubeFeed(list.get(list.size() - 1).getVidLink());		
	}
	
	private ArrayList<MyAppInfo> getListAppFromFile(){
		ArrayList<MyAppInfo> listResult = new ArrayList<MyAppInfo>();		
		MyLog.e(TAB, "getListAppFromFile VERY START");
		
		String rootPath = android.os.Environment
				.getExternalStorageDirectory()
				.toString()
				.concat(String.format("/%s/%s", "Android/data",
						getPackageName()));
		
		String filePath = rootPath.concat("/sc_appList.xls");
		
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
			int idxSheet = -1;
			for (int i = 0; i < myWorkBook.getNumberOfSheets(); i++) {
	            HSSFSheet sheet = myWorkBook.getSheetAt(i);
	            if(sheet.getSheetName().equalsIgnoreCase("Giai Tri")){
	            	idxSheet = i;
	            }
	        }
			
			if(idxSheet == -1){
				return listResult;
			}
			
			HSSFSheet mySheet = myWorkBook.getSheetAt(idxSheet);

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
					MyAppInfo tempInfo = new MyAppInfo();
					tempInfo.setMainType(0); // GIAI TRI

					while (cellIter.hasNext()) {
						countCol++;

						HSSFCell myCell = (HSSFCell) cellIter.next();

						switch (countCol) {
						case 1:
							tempInfo.setSort(Integer.parseInt(myCell.toString().replace(".0", "")));
							break;
						case 2:
							String name = myCell.toString().trim();
							tempInfo.setName(name);
							break;
						case 3:
							String type = myCell.toString().trim();
							tempInfo.setType(type);
							break;
						case 4:
							String link = myCell.toString().trim();
							tempInfo.setLinkYoutube(link);
							
							String[] datas = link.split("v=");
							tempInfo.setVidLink(datas[1]);
							break;						
						default:
							break;
						}

					}

					listResult.add(tempInfo);
				}

			}
			
			myWorkBook.close();
		} catch (Exception e) {
			e.printStackTrace();
			if (fExcel.exists()) {
				fExcel.delete();
			}
		}
		
		MyLog.e(TAB, "getListAppFromFile VERY END -- size = " + listResult.size());
		return listResult;
	}
	
	private String TAG = "YOUTUBE";
	public void readYoutubeFeed() {
		
		if(listProcessApp == null || listProcessApp.size() == 0){
			return;
		}
		
		MyLog.e(" ", " ");MyLog.e(" ", " ");
		MyLog.e(" ", "size = " + listProcessApp.size());
		final String vidID = listProcessApp.get(0).getVidLink();
		listProcessApp.remove(0);
		
		new Thread(new Runnable() {
			public void run() {
				long time = System.currentTimeMillis();
				StringBuilder builder = new StringBuilder();
				HttpClient client = new DefaultHttpClient();
				String url = "https://www.googleapis.com/youtube/v3/videos?key="
						+ DeveloperKey.DEVELOPER_KEY_MHTouch1
						+ "&part=snippet,statistics,contentDetails&id="
						+ vidID;
				try {
					URLEncoder.encode(url, "UTF-8");
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
					Log.v(TAG, "encode error");
				}
				HttpGet httpGet = new HttpGet(url);
				try {
					HttpResponse response = client.execute(httpGet);
					StatusLine statusLine = response.getStatusLine();
					int statusCode = statusLine.getStatusCode();
					if (statusCode == 200) {
						HttpEntity entity = response.getEntity();
						InputStream content = entity.getContent();
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(content, "UTF-8"));
						String line;
						while ((line = reader.readLine()) != null) {
							builder.append(line);
						}
					} else {
						Log.v(TAG, "Failed to download file");
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
					Log.v(TAG, "readYoutubeFeed exeption1");
				} catch (IOException e) {
					e.printStackTrace();
					Log.v(TAG, "readYoutubeFeed exeption2");
				}
//				MyLog.e(TAG, "result = " + builder.toString());

				// process JSON data
				try {
					JSONObject reader = new JSONObject(builder.toString());
					JSONArray jsonArray = reader.optJSONArray("items");
					
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						
						JSONObject jsonSnippet = jsonObject.getJSONObject("snippet");
						JSONObject jsonContentDetails = jsonObject.getJSONObject("contentDetails");

						String channelTitle = "";
						try {
							channelTitle = jsonSnippet.optString("channelTitle").toString();	
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						
						String title = "";
						try {
							title = jsonSnippet.optString("title").toString();	
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						
						String imageLink = "";
						JSONObject jsonThumbnails = jsonSnippet.getJSONObject("thumbnails");
						try {
							JSONObject jsonTemp= jsonThumbnails.getJSONObject("high");
							imageLink = jsonTemp.getString("url");
						} catch (Exception e) {
							try {
								JSONObject jsonTemp = jsonThumbnails.getJSONObject("medium");
								imageLink = jsonTemp.getString("url");
							} catch (Exception e1) {
								try {
									JSONObject jsonTemp = jsonThumbnails.getJSONObject("default");
									imageLink = jsonTemp.getString("url");
								} catch (Exception e2) {
									e.printStackTrace();
								}
							}
						}
						
						String duration = "";
						long longDur = 0;
						try {
							duration = jsonContentDetails.optString("duration").toString();	
							longDur = getDuration(duration);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						MyLog.d(" ", "title = " + title);
						MyLog.d(" ", "channelTitle = " + channelTitle);
						MyLog.d(" ", "imageLink = " + imageLink);
						MyLog.d(" ", "duration = " + duration);
						MyLog.d(" ", "longDur = " + longDur);
						
						int seconds = (int) (longDur / 1000) % 60 ;
						int minutes = (int) ((longDur / (1000*60)) % 60);
						int hours   = (int) ((longDur / (1000*60*60)) % 24);
						
						MyLog.i(" ", "hours = " + hours);
						MyLog.i(" ", "minutes = " + minutes);
						MyLog.i(" ", "seconds = " + seconds);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				MyLog.e(" ", "time process : " + (System.currentTimeMillis() - time));
				handlerProcessApp.sendEmptyMessage(0);
				
			}
		}).start();
				
	}
	
	private Handler handlerProcessApp = new Handler(){
	    @Override
	    public void handleMessage(Message msg) {
	    	if(listProcessApp == null || listProcessApp.size() == 0){
				return;
			}
	    	
	    	readYoutubeFeed();
	    }
	};
	
	public long getDuration(String strDur) {
	    String time = strDur.substring(2);
	    long duration = 0L;
	    Object[][] indexs = new Object[][]{{"H", 3600}, {"M", 60}, {"S", 1}};
	    for(int i = 0; i < indexs.length; i++) {
	        int index = time.indexOf((String) indexs[i][0]);
	        if(index != -1) {
	            String value = time.substring(0, index);
	            duration += Integer.parseInt(value) * Integer.parseInt(indexs[i][1].toString()) * 1000;
	            time = time.substring(value.length() + 1);
	        }
	    }
	    return duration;
	}
	
	private void updatePopupDialog(){
		if(ToastBox.dialogLyricView != null){
			ToastBox.dialogLyricView.invalidate();
		}
	}
	
	@Override
	public void onClickYouTube(MyYouTubeInfo info, int type, int position,
			float x, float y) {
		
		hideKeyBoard();
		
		if(serverStatus == null){
			showDialogConnect();
			return;
		}
		
		if(type == 0){ // ADD
			animationView.stopAnimation();
			animationView.startAnimation(x, y, TouchAnimationView.SONG);	
			
			((MyApplication) getApplication()).addSongToPlaylist_RealYouTube(info.getVideoId(), info.getTitle());
		} else if(type == 1){ // FIRST
			animationView.stopAnimation();
			animationView.startAnimation(x, y, TouchAnimationView.FRIST);	
			
			((MyApplication) getApplication()).firstSongToPlaylist_RealYouTube(info.getVideoId(), info.getTitle(), position);
		}
	}
	
	public static ArrayList<Song> listRealYoutube = new ArrayList<Song>();
	private Song processSongRealYoutube(int id){
		MyLog.e(TAB, "processSongRealYoutube -- " + id);
		
		ArrayList<Song> resultYoutube = TouchMainActivity.listRealYoutube;
		
		for(int j = 0; j < resultYoutube.size(); j++) {
			Song song = resultYoutube.get(j); 
			if((song.getId() == id || song.getIndex5() == id)) {
				return song;
			}
			
		}
		
		return null;
		
	}
	
	public void setPopupYouTube(View v, int position){
		if(drawerLayout != null && v != null){
			int[] location = new int[2];
			v.getLocationOnScreen(location);
						
//			int left = (int) (location[0] / Resources.getSystem().getDisplayMetrics().density);
//			int top = (int) (location[1] / Resources.getSystem().getDisplayMetrics().density);
			int left = location[0];
			int top = location[1];
			int right = left + v.getWidth();
			int bottom = top + v.getHeight();
			
			drawerLayout.setRectPopup(left, top, right, bottom, position);
		}
	}
	
	private boolean processCheckFreeSpace(long checkSize){
		MyLog.e(" ", " ");MyLog.e(" ", " ");
		MyLog.e(TAB, "processCheckFreeSpace - checkSize = " + checkSize);
		long freeSpace = getFreeSpaceDevice();
		MyLog.e(TAB, "processCheckFreeSpace - freeSpace = " + freeSpace);
		
		if(checkSize >= freeSpace){
			return false;
		}			
		
		return true;
	}
	
	public long getFreeSpaceDevice(){
		long bytesAvailable = 0;
		try {
			StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getAbsolutePath());
			bytesAvailable = (long)stat.getBlockSize() *(long)stat.getAvailableBlocks();
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		return bytesAvailable;
	}

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
					if(btnVideo != null){
						btnVideo.invalidate();
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
//			MyLog.e("FreezeCheckTask", "..................................");
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
			if(boolShowPreviewKaraoke){
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
			if(((MyApplication)getApplication()).getListActive().size() == 0 && groupView.getCurrectSongName().toUpperCase().contains("PIANO")){
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
								hideKeyBoard();

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
	
	private int countTotalYoutubeExcel(){
		try {
			LanguageStore store = new LanguageStore(context); 
			ArrayList<String>langIDs = store.getListIDActive(); 
			int[] intIDs = new int[langIDs.size()]; 
			for(int j = 0; j < langIDs.size(); j++) {
				intIDs[j] = Integer.parseInt(langIDs.get(j)); 
			}		
			
			ArrayList<Song> songYouTube = new ArrayList<Song>();
			Cursor cursor = DBInterface.DBGetSongCursor_YouTube(context,
					intIDs, "", SearchMode.MODE_MIXED, MEDIA_TYPE.ALL, 0, 0);
			if(cursor.moveToFirst()){
				for (int j = 0; j < cursor.getCount(); j++) {
					Song song = new Song();
					song.setId(cursor.getInt(0));
					song.setIndex5(cursor.getInt(1));
					song.setYoutubeSong(true);
					songYouTube.add(song);
					if (!cursor.moveToNext()) {
						break;
					}
				}
				cursor.close();
				cursor = null;
			}
			
			if(songYouTube.size() > 0){
				ArrayList<Song> songExisted = DBInterface.DBSearchListSongID(songYouTube, context);
				if(songExisted.size() > 0){
					ArrayList<Song> newList = new ArrayList<Song>();	 
					
					for (Song song : songYouTube) {
						if(songExisted.contains(song)){
//							MyLog.d("remove bot song trong youtube", song.getName() + " ");
						} else {
							newList.add(song);
						}
					}
					
					songYouTube = newList;
				}
			}
			
			return songYouTube.size();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public LuckRollView luckRollView;
	
	private int idDownloadMidi = 0;
	public void callDownloadFileMidiFromPlayer(int id){
		if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
			idDownloadMidi = id;
			
			MyLog.e(" ", " ");MyLog.e(" ", " ");
			MyLog.e(TAB, "callDownloadFileMidiFromPlayer - id = " + id);
			MyLog.e(" ", " ");MyLog.e(" ", " ");
			
			DownloadState = -10;

			OnCloseKeyBoard();

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
	
	private void getListSambaFromFile(){
		MyLog.e(TAB, "getListSambaFromFile VERY START");
		
		String rootPath = android.os.Environment
				.getExternalStorageDirectory()
				.toString()
				.concat(String.format("/%s/%s", "Android/data",
						getPackageName()));
		
		String filePath = rootPath.concat("/" + MyApplication.ADD_FILE_SAMBA);
		
		File fExcel = new File(filePath);
		if(!fExcel.exists()){
			return;
		}

		AppSettings setting = AppSettings.getInstance(getApplicationContext());
		try {
//			String oldSambaPath = setting.getLastLinkLAN();
			
			FileInputStream myInput = new FileInputStream(fExcel);
			POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
			HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
			HSSFSheet mySheet = myWorkBook.getSheetAt(0);

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

						switch (countCol) {
						case 1:
							tempSong.setId(Integer.parseInt(myCell.toString().replace(".0", "")));
							tempSong.setIndex5(Integer.parseInt(myCell.toString().replace(".0", "")));
							break;
						case 2:
							String name = myCell.toString();
							if (name.endsWith(".0")) {
								name = name.substring(0, name.length() - 2);
							}
							tempSong.setName(name);
							
							String rawName = StringUtils.getRawString(name, LANG_INDEX.ALL_LANGUAGE);
							tempSong.setTitleRaw(rawName);
							
							tempSong.setShortName(StringUtils.getPinyinString(name, LANG_INDEX.ALL_LANGUAGE));
							
							break;
						case 3:
							String singer = myCell.toString();
							if(singer.endsWith(".0")){
								singer = singer.substring(0, singer.length() - 2);
							}
							tempSong.setSingerName(singer);							
							break;
						case 5:
//							tempSong.setSambaPath("smb://" + oldSambaPath + myCell.toString().trim());
							break;	
						default:
							break;
						}

					}

					tempSong.setLyric("-");
					tempSong.setMediaType(MEDIA_TYPE.VIDEO);
					tempSong.setLanguageID(0);
					tempSong.setTypeABC(0);
					tempSong.setSambaSong(true);
					listExcelSong.add(tempSong);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			if (fExcel.exists()) {
				fExcel.delete();
			}
			setting.saveSambaDataVersion(0);
		} 
		
		MyLog.e(TAB, "getListSambaFromFile VERY END");
	}
	
	private boolean checkEmptyArray(byte[] bList){
		for (byte b : bList) {
		    if (b != 0) {
		        return false;
		    }
		}
		
		return true;
	}
	
private class CheckYoutubeAPIListKey extends AsyncTask<Void, Void, Integer> {
		
		private String updateInfo_path = "";
		
		@Override
		protected Integer doInBackground(Void... params) {
			if(MyApplication.checkFullInternet(context) == false){
				return -1;
			}			
			
			String appRootPath = android.os.Environment
					.getExternalStorageDirectory()
					.toString()
					.concat(String.format("/%s/%s", "Android/data",
							getPackageName()));
			File appBundle = new File(appRootPath);
			if (!appBundle.exists())
				appBundle.mkdir();

			updateInfo_path = appRootPath.concat("/check_youtube_api_list.xml");
			File dest_file = new File(updateInfo_path);
			
			try {
				URL u = new URL(
						"https://kos.soncamedia.com/firmware/karconnect/check_youtube_api_list.xml");
				DataInputStream fis = new DataInputStream(u.openStream());
				DataOutputStream fos = new DataOutputStream(new FileOutputStream(dest_file));

				int bytes_read = 0;
				int buffer_size = 1024 * 1024;
				byte[] buffer = new byte[buffer_size];

				while ((bytes_read = fis.read(buffer, 0, buffer_size)) > 0) {
					fos.write(buffer, 0, bytes_read);
				}

				fis.close();
				fos.flush();
				fos.close();

			} catch (Exception e) {
				e.printStackTrace();
				if (dest_file.exists()) {
					dest_file.delete();
				}
			}

			if (!dest_file.exists()) {
				return -1;
			}

			return -1;
		}

		@Override
		protected void onPostExecute(Integer result) {
			DeveloperKey.listKeyAPI = new ArrayList<String>();
			DeveloperKey.totalKeyBackUp = 8;
			
			File dest_file = new File(updateInfo_path);
			if (!dest_file.exists()) {
				MyLog.e(" ", " ");MyLog.e(" ", " ");
				MyLog.e(TAB, "DeveloperKey.listKeyAPI size = " + DeveloperKey.listKeyAPI.size());
				MyLog.e(TAB, "DeveloperKey.totalKeyBackUp = " + DeveloperKey.totalKeyBackUp);
				MyLog.e(" ", " ");MyLog.e(" ", " ");
				
				return;
			}						
			
			try {
				InputStream is = new FileInputStream(updateInfo_path);
				Document doc = XmlUtils.convertToDocument(is);
				NodeList nodeList = doc.getElementsByTagName("youtubeapi");

				Node node = nodeList.item(0);
				Element e = (Element) node;
				
				NodeList nl = e.getChildNodes();
				
				if(nl != null){
					for (int i = 0; i < nl.getLength(); i++) {
			            if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
			                Element el = (Element) nl.item(i);
			                if (el.getNodeName().contains("keyAPI")) {
			                    String keyValue = el.getTextContent();
			                    DeveloperKey.listKeyAPI.add(keyValue);
			                }
			            }
			        }
				}
				
				if(DeveloperKey.listKeyAPI.size() > 0){
					DeveloperKey.totalKeyBackUp = DeveloperKey.listKeyAPI.size() - 1;
				}				

				MyLog.e(" ", " ");MyLog.e(" ", " ");
				MyLog.e(TAB, "DeveloperKey.listKeyAPI size = " + DeveloperKey.listKeyAPI.size());
				MyLog.e(TAB, "DeveloperKey.totalKeyBackUp = " + DeveloperKey.totalKeyBackUp);
				MyLog.e(" ", " ");MyLog.e(" ", " ");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}

	}


	// --------------- TEST PING LAST CONNECTED 
	private void callAuthen(long timeDelay, final String nameLog) {
		if (processAuthenTask != null) {
			processAuthenTask.cancel(true);
			processAuthenTask = null;
		}

		loadAuthenStatus();

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				MyLog.e(TAB, "callAuthen");
				processAuthenTask = new ProcessAuthenTask(nameLog);
				processAuthenTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			}
		}, timeDelay);
	}
	
	private void loadAuthenStatus(){
		AppSettings setting = AppSettings.getInstance(getApplicationContext());
		countAuthen = setting.getMCountAuthen();
		countSuccess = setting.getMCountSuccess();
		countFailed = setting.getMCountFail();
	}
	
	private void saveAuthenStatus(){
		AppSettings setting = AppSettings.getInstance(getApplicationContext());
		setting.setMCountAuthen(countAuthen);
		setting.setMCountSuccess(countSuccess);
		setting.setMCountFail(countFailed);
	}
	
	public boolean checkMyInternet(Context context){
		ConnectivityManager conMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conMgr.getActiveNetworkInfo();

        if(info != null && info.isConnected()){
        	try {
    	        Process p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 -W 1 8.8.8.8");
    	        int returnVal = p1.waitFor();
    	        boolean reachable = (returnVal==0);
    	        return reachable;
    	    } catch (Exception e) {
    	        e.printStackTrace();
    	    }
        }
        return false;
	}

	public String getCurrentTimeStamp() {
	    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Date now = new Date();
	    String strDate = sdfDate.format(now);
	    return strDate;
	}
	
	private int countAuthen = 0;
	private int countSuccess = 0;
	private int countFailedStart = 0;
	private int countFailed = 0;
	private long timeAuthenStart = 0;
	public static int RETRY_TIME = 3;
	
	private ProcessAuthenTask processAuthenTask;
	private class ProcessAuthenTask extends AsyncTask<Void, Void, String> {

		private String logName = "";
		private String connectedIP = "";
		
		public ProcessAuthenTask(String logName){
			this.logName = logName;
		}		
		
		@Override
		protected String doInBackground(Void... urls) {

			try {
				countAuthen++;
				timeAuthenStart = System.currentTimeMillis();
								
				boolean hasLast = getLastConnectedServer();
				if(hasLast == false){
					return "NO LAST";
				}	
				
				AppSettings setting = AppSettings.getInstance(getApplicationContext());
				connectedIP = setting.loadServerIP();
				boolean flagPing = executeCommandPing(connectedIP);
				
				if(flagPing){
					return "OK";
				}				

			} catch (Exception e) {
				e.printStackTrace();
			} 

			return "FAIL";
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (result.equals("OK")) {
				countFailedStart = 0;
				countSuccess++;
				
				String str = "Xac nhan OK" + "\nLan thu: " + countAuthen 
						+ "\ncountSuccess = " + countSuccess
						+ "\ncountFailed = " + countFailed;
				MyLog.d(TAB, str);
				
				saveAuthenStatus();
				
				callAuthen(2000, logName);
			} else if (result.equals("NO LAST")) {
				callAuthen(2000, logName);
			} else {
				if(countFailedStart >= RETRY_TIME){
					countFailed++;
					countFailedStart = 0;

					String dateString = getCurrentTimeStamp();
					
					String str = connectedIP + 
									"\ncountSuccess = " + countSuccess
									+ "\ncountFailed = " + countFailed;
					str += "\nTime : " + dateString;
					MyLog.d(TAB, str);
					new MoonFileUtilities(context).writeAppend(logName, "\n==============\n" + str);
										
					saveAuthenStatus();
					
					callAuthen(2000, logName);
				} else {
					countFailedStart++;
					
					String str = "Lan thu: " + countAuthen + 
							"\ntry in: " + (System.currentTimeMillis() - timeAuthenStart
									+ "\nRetry = " + countFailedStart);
					MyLog.i(TAB, str);
								
					saveAuthenStatus();
					
					callAuthen(2000, logName);
				}
			}
			
		}
	}
	
	@Override
	public void OnCallAutoVideoViral(int numSong) {			
		if(numSong != 0){
			stopTimerCallViralVideo();
			return;
		}
		
		if(groupView == null){
			stopTimerCallViralVideo();
			return;
		}
		
		startTimerCallViralVideo();
		
	}
	
	// ----------------------------------------------------
	private int idViral = 0;
	private long timeViral = 0;
	
	private Timer timerCheckCallViralVideo;

	class CallViralVideoTask extends TimerTask {

		@Override
		public void run() {
			MyLog.e(" ", " ");MyLog.e(" ", " ");
			MyLog.e(TAB, "CallViralVideoTask -----------------------------");
			MyLog.d(" ", "idViral = " + idViral);
			
			if(idViral == 0){
				return;
			}
			
			if(serverStatus == null){
				return;
			}
			
			if(groupView == null){
				return;
			}
			
			if(groupView.getSumSong() != 0){
				return;
			}
			
			if (MyApplication.intWifiRemote == MyApplication.SONCA_KARTROL) {
				return;
			}
			
			String curSongName = groupView.getCurrectSongName();
			MyLog.d(" ", "curSongName = " + curSongName);
			if(curSongName.trim().equals("") || curSongName.trim().equals(" ")
					|| curSongName.toUpperCase().contains("PIANO")){
				
				ArrayList<Song> songs = DBInterface.DBSearchSongID(idViral + "", 0 + "", context);
				if (songs.isEmpty()) {
					if (MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
						ArrayList<Song> songsYouTube = DBInterface.DBSearchSongID_YouTube(idViral + "", 0 + "", context);
						if (!songsYouTube.isEmpty()) {
							if(MyApplication.checkFullInternet(context) == true){
								((MyApplication) getApplication()).firstReservedSongYouTube(idViral, 0, -1);	
							}							
						}	
					}					
				} else {
					((MyApplication) getApplication()).firstReservedSong(idViral, 0, -1);
				}	
				
			}
			
			
		}
	};

	private void startTimerCallViralVideo() {
		if(timeViral == 0){
			return;
		}
		
		stopTimerCallViralVideo();
		timerCheckCallViralVideo = new Timer();
		timerCheckCallViralVideo.schedule(new CallViralVideoTask(), timeViral);
	}

	private void stopTimerCallViralVideo() {
		if (timerCheckCallViralVideo != null) {
			timerCheckCallViralVideo.cancel();
			timerCheckCallViralVideo = null;
		}
	}
	
	private class CheckViralVideoTask extends AsyncTask<Void, Void, Integer> {
		
		private String updateInfo_path = "";
		
		@Override
		protected Integer doInBackground(Void... params) {
			if(MyApplication.flagAllowAdvSong == false){
				return -1;
			}
			
			if(MyApplication.checkFullInternet(context) == false){
				return -1;
			}			
			
			String appRootPath = android.os.Environment
					.getExternalStorageDirectory()
					.toString()
					.concat(String.format("/%s/%s", "Android/data",
							getPackageName()));
			File appBundle = new File(appRootPath);
			if (!appBundle.exists())
				appBundle.mkdir();

			updateInfo_path = appRootPath.concat("/check_viral_video.xml");
			File dest_file = new File(updateInfo_path);
			
			try {
				URL u = new URL(
						"https://kos.soncamedia.com/firmware/karconnect/check_viral_video.xml");
				DataInputStream fis = new DataInputStream(u.openStream());
				DataOutputStream fos = new DataOutputStream(new FileOutputStream(dest_file));

				int bytes_read = 0;
				int buffer_size = 1024 * 1024;
				byte[] buffer = new byte[buffer_size];

				while ((bytes_read = fis.read(buffer, 0, buffer_size)) > 0) {
					fos.write(buffer, 0, bytes_read);
				}

				fis.close();
				fos.flush();
				fos.close();

			} catch (Exception e) {
				e.printStackTrace();
				if (dest_file.exists()) {
					dest_file.delete();
				}
			}

			if (!dest_file.exists()) {
				return -1;
			}

			return -1;
		}

		@Override
		protected void onPostExecute(Integer result) {			
			
			File dest_file = new File(updateInfo_path);
			if (!dest_file.exists()) {
				return;
			}						
			
			try {
				InputStream is = new FileInputStream(updateInfo_path);
				Document doc = XmlUtils.convertToDocument(is);
				NodeList nodeList = doc.getElementsByTagName("viralVideo");

				Node node = nodeList.item(0);
				Element e = (Element) node;
				
				NodeList nl = e.getChildNodes();
				
				if(nl != null){
					for (int i = 0; i < nl.getLength(); i++) {
			            if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
			                Element el = (Element) nl.item(i);
			                if (el.getNodeName().equals("videoID")) {
			                    String mValue = el.getTextContent();
			                    idViral = Integer.parseInt(mValue);
			                } else if (el.getNodeName().equals("delayTime")) {
			                    String mValue = el.getTextContent();
			                    timeViral = Integer.parseInt(mValue);
			                }
			            }
			        }
				}
				
				MyLog.e(TAB, "CheckViralVideoTask start-------------");
				MyLog.e(TAB, "idViral = " + idViral);
				MyLog.e(TAB, "timeViral = " + timeViral);
				MyLog.e(TAB, "CheckViralVideoTask end---------------");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}

	}
	
	private boolean flagStopSearch = false;
	public void processSwitchKeyboard(boolean flagRealKeyboard){
		OnCloseKeyBoard();
		if(MyApplication.flagRealKeyboard){
			searchEditText.setText("");
			edSearchOldStr = "";	
		}						
		searchView.clearAllCharacterSearchView();
		layoutDeleteSearchView.setVisibility(View.INVISIBLE);
		if (mainKeyboardListener != null) {
			mainKeyboardListener.OnTextShowing("");
		}
		
		MyApplication.flagRealKeyboard = flagRealKeyboard;
		AppSettings set = AppSettings.getInstance(context);
		set.saveRealKeyboard(flagRealKeyboard);
		
		myGroupSearch.requestLayout();
	}
	
	public void resetTheLoaiList(){
		try {			
			popupViewUpadtePic
					.setPopupLayout(TouchPopupViewUpdatePic.LAYOUT_PROCESSDATA2);
			myDialogUpdatePic.getWindow().setGravity(Gravity.CENTER);
			WindowManager.LayoutParams params = myDialogUpdatePic.getWindow()
					.getAttributes();
			myDialogUpdatePic.getWindow().setAttributes(params);
			myDialogUpdatePic.show();
	
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					reloadYoutubeTable();
					
					if(myDialogUpdatePic != null){
						myDialogUpdatePic.dismiss();
					}
					
					MyApplication.mSongTypeList = null;
					processTotalSongType(100);
				}
			}, 500);	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
	
	public void resetAdvAllow(){
		idViral = 0;
		timeViral = 0;
		
		CheckViralVideoTask viralVideoTask = new CheckViralVideoTask();
		viralVideoTask.execute();
	}
	
}


