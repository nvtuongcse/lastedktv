package vn.com.sonca.Touch.CustomView;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.database.DBInterface;
import vn.com.sonca.params.SKServer;
import vn.com.sonca.params.Song;
import vn.com.sonca.zktv.main.KTVMainActivity;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

public class TouchMyGroupView extends View {

	private Paint mainPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintPath = new Paint(Paint.ANTI_ALIAS_FLAG);
	// private TextPaint mainText = new TextPaint(Paint.ANTI_ALIAS_FLAG);

	public static final boolean EXIT = true;
	public static final boolean BACK = false;
	private boolean state = EXIT;

	private Path mainPath;
	private Path pathBack;
	private Path pathPlaylist;
	private Drawable drawable = null;
	private int widthLayout = 350;
	private int heightLayout = 350;
	private float DP;

	private String currectSong = "";
	private int sumSong = 0;

	public static final int CONNECTED = 0;
	public static final int INCONNECT = 1;
	private final int CONNECT_HOVER = 2;

	public static final int ACTIVE = 0;
	public static final int INACTIVE = 1;
	private final int HOVER = 2;

	private boolean boolExit;
	private int stateConnect;
	private int saveConnect;
	private int statePlayList;
	private int savePlayList;
	private String serverName = "";
	private String nameRemote = "";

	private boolean isPlay = false;

	public TouchMyGroupView(Context context) {
		super(context);
		initView(context);
	}

	public TouchMyGroupView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public TouchMyGroupView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	private Context context;
	
	private boolean isCreated;
	private Drawable drawListAc;
	private Drawable drawListNo;
	private Drawable drawBlock;
	private Drawable drawWifi0, drawWifi1, drawWifi2, drawWifi3, drawWifi4;
	private Drawable drawWifi0_hiw, drawWifi1_hiw, drawWifi2_hiw, drawWifi3_hiw, drawWifi4_hiw;
	private Drawable drawWifi0_smartk, drawWifi1_smartk, drawWifi2_smartk, drawWifi3_smartk, drawWifi4_smartk;
	
	private Drawable zlight_drawBlock, zlight_drawListAc, zlight_drawListNo;
	private Drawable zlight_drawWifi0, zlight_drawWifi1, zlight_drawWifi2,
			zlight_drawWifi3, zlight_drawWifi4;
	private Drawable zlight_drawWifi0_hiw, zlight_drawWifi1_hiw, zlight_drawWifi2_hiw, zlight_drawWifi3_hiw, zlight_drawWifi4_hiw;
	private Drawable zlight_drawWifi0_smartk, zlight_drawWifi1_smartk, zlight_drawWifi2_smartk, zlight_drawWifi3_smartk, zlight_drawWifi4_smartk;
	private Drawable zlight_drawWifi0_hover, zlight_drawWifi1_hover, zlight_drawWifi2_hover,
	zlight_drawWifi3_hover, zlight_drawWifi4_hover;
	
	private Drawable drawWifi0_xam;
	
	private Drawable drawLine;
	
	private void initView(Context context) {
		this.context = context;
		drawBlock = getResources().getDrawable(
				R.drawable.touch_icon_block);
		drawListAc = getResources().getDrawable(
				R.drawable.touch_image_boder_giohang_328x171);
		drawListNo = getResources().getDrawable(
				R.drawable.touch_image_boder_giohang_inactive_328x171);		

		mainPath = new Path();
		pathPlaylist = new Path();
		isCreated = true;
		DisplayMetrics display = context.getResources().getDisplayMetrics();
		DP = display.density;
		boolExit = false;
		saveConnect = stateConnect = INCONNECT;
		savePlayList = statePlayList = INACTIVE;
				
		drawWifi0 = getResources().getDrawable(R.drawable.wifi_0);
		drawWifi1 = getResources().getDrawable(R.drawable.wifi_1);
		drawWifi2 = getResources().getDrawable(R.drawable.wifi_2);
		drawWifi3 = getResources().getDrawable(R.drawable.wifi_3);
		drawWifi4 = getResources().getDrawable(R.drawable.wifi_4);
		
		drawWifi0_hiw = getResources().getDrawable(R.drawable.ktvwifi_0);
		drawWifi1_hiw = getResources().getDrawable(R.drawable.ktvwifi_1);
		drawWifi2_hiw = getResources().getDrawable(R.drawable.ktvwifi_2);
		drawWifi3_hiw = getResources().getDrawable(R.drawable.ktvwifi_3);
		drawWifi4_hiw = getResources().getDrawable(R.drawable.ktvwifi_4);
		
		drawWifi0_smartk = getResources().getDrawable(R.drawable.wifi_9108_0);
		drawWifi1_smartk = getResources().getDrawable(R.drawable.wifi_9108_1);
		drawWifi2_smartk = getResources().getDrawable(R.drawable.wifi_9108_2);
		drawWifi3_smartk = getResources().getDrawable(R.drawable.wifi_9108_3);
		drawWifi4_smartk = getResources().getDrawable(R.drawable.wifi_9108_4);		
		
		drawWifi0_xam = getResources().getDrawable(R.drawable.wifi_0_xam);
				
		// THEME LIGHT
		zlight_drawBlock = getResources().getDrawable(
				R.drawable.zlight_icon_block);
		zlight_drawListAc = getResources().getDrawable(
				R.drawable.zlight_image_boder_giohang_active_328x171);
		zlight_drawListNo = getResources().getDrawable(
				R.drawable.zlight_image_boder_giohang_inactive_328x171);

		zlight_drawWifi0 = getResources().getDrawable(R.drawable.zlight_wifi_0);
		zlight_drawWifi1 = getResources().getDrawable(R.drawable.zlight_wifi_1);
		zlight_drawWifi2 = getResources().getDrawable(R.drawable.zlight_wifi_2);
		zlight_drawWifi3 = getResources().getDrawable(R.drawable.zlight_wifi_3);
		zlight_drawWifi4 = getResources().getDrawable(R.drawable.zlight_wifi_4);

		zlight_drawWifi0_hover = getResources().getDrawable(
				R.drawable.zlight_wifi_0_hover);
		zlight_drawWifi1_hover = getResources().getDrawable(
				R.drawable.zlight_wifi_1_hover);
		zlight_drawWifi2_hover = getResources().getDrawable(
				R.drawable.zlight_wifi_2_hover);
		zlight_drawWifi3_hover = getResources().getDrawable(
				R.drawable.zlight_wifi_3_hover);
		zlight_drawWifi4_hover = getResources().getDrawable(R.drawable.zlight_wifi_4_hover);
		
		zlight_drawWifi0_hiw = getResources().getDrawable(R.drawable.zlight_ktvwifi_0);
		zlight_drawWifi1_hiw = getResources().getDrawable(R.drawable.zlight_ktvwifi_1);
		zlight_drawWifi2_hiw = getResources().getDrawable(R.drawable.zlight_ktvwifi_2);
		zlight_drawWifi3_hiw = getResources().getDrawable(R.drawable.zlight_ktvwifi_3);
		zlight_drawWifi4_hiw = getResources().getDrawable(R.drawable.zlight_ktvwifi_4);
		
		zlight_drawWifi0_smartk = getResources().getDrawable(R.drawable.zlight_wifi_9108_0);
		zlight_drawWifi1_smartk = getResources().getDrawable(R.drawable.zlight_wifi_9108_1);
		zlight_drawWifi2_smartk = getResources().getDrawable(R.drawable.zlight_wifi_9108_2);
		zlight_drawWifi3_smartk = getResources().getDrawable(R.drawable.zlight_wifi_9108_3);
		zlight_drawWifi4_smartk = getResources().getDrawable(R.drawable.zlight_wifi_9108_4);
			
		drawLine = getResources().getDrawable(R.drawable.shape_line_search_view);
				
		CreateTimer();
	}

	private OnMyGroupListener listener;

	public interface OnMyGroupListener {
		public void OnPlayList();

		public void OnShowConnect();
		
		public void OnFAKEShowConnect();

		public void OnExit(boolean value);
		
		public void OnShowTabModel();
		
		public void OnShowKM1_List();
		
		public void OnCallAutoVideoViral(int numSong);
	}

	public void setOnMyGroupListener(OnMyGroupListener listener) {
		this.listener = listener;
	}

	private boolean isConnected;

	public void setExit(boolean state) {
		this.state = state;
		invalidate();
	}

	public void setPlayPause(boolean isPlay) {
		this.isPlay = isPlay;
		invalidate();
	}
	
	public boolean getPlayPause(){
		return isPlay;
	}

	public void setConnected(int State, String NameServer, SKServer skServer) {
		isConnected = skServer != null;
		saveConnect = stateConnect = State;
		if(State == CONNECTED){
			if(NameServer == null || NameServer.equals("")){
				serverName = getResources().getString(R.string.connected);
			}else{
				serverName = NameServer;
			}
		}else{
			serverName = "";
		}
		invalidate();
	}
	
	public void setNameRemote(String name){
		nameRemote = name;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		isConnected = TouchMainActivity.serverStatus != null || KTVMainActivity.serverStatus != null;
		setK(getWidth(), getHeight());

		if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
			// DRAW 0

			mainPaint.setStyle(Style.FILL);
			mainPaint.setColor(Color.parseColor("#003c6e"));
			canvas.drawRect(rectBackgroud, mainPaint);
			
			if (MyApplication.intWifiRemote == MyApplication.SONCA) {

				resetPaint();

				float leftLayout = (float) (0.17 * widthLayout);
				float righttLayout = widthLayout - leftLayout;
				if (state == EXIT) {
				}

				mainPath = new Path();
				mainPath.moveTo(leftLayout - K15, KDP);
				mainPath.lineTo(leftLayout, heightLayout);
				mainPath.lineTo(leftLayout - K30, 0);
				mainPath.lineTo(righttLayout + K30, 0);
				mainPath.lineTo(righttLayout, heightLayout);
				mainPath.lineTo(righttLayout + K15, KDP);
				mainPath.lineTo(leftLayout - K15, KDP);

				mainPaint.setStyle(Style.STROKE);
				mainPaint.setStrokeWidth(1);
				mainPaint.setARGB(255, 1, 43, 81);

				// ------path lỉne 1--------
				canvas.drawPath(mainPath, mainPaint);
				// -----------------------//

				// statePlayList = ACTIVE;

				if (isConnected) {
					if (countAnimation != -1 && countAnimation < 4) {
						// MyLog.e("In ANIMATION","countAnimation = " +
						// countAnimation);
						if (countAnimation % 2 != 0) {
							drawListAc.setBounds(rectList);
							drawListAc.draw(canvas);
						} else {
							drawListNo.setBounds(rectList);
							drawListNo.draw(canvas);
						}
					} else {
						switch (statePlayList) {
						case ACTIVE:
							drawListAc.setBounds(rectList);
							drawListAc.draw(canvas);
							break;
						case INACTIVE:
							drawListNo.setBounds(rectList);
							drawListNo.draw(canvas);
							break;
						default:
							break;
						}
					}

				} else {
					drawListNo.setBounds(rectList);
					drawListNo.draw(canvas);
				}

/* sua code ui 3-20-2016
				mainPaint.setStrokeWidth(KS);
				mainPaint.setARGB(255, 1, 43, 81);
				canvas.drawLine(0, heightLayout + KL, widthLayout, heightLayout
						+ KL, mainPaint);
*/
				drawLine.setBounds(rectLine);
				drawLine.draw(canvas);

				/*
				mainPaint.setStrokeWidth(KDP);
				mainPaint.setColor(Color.GREEN);
				LinearGradient gradient = new LinearGradient(leftLayout - K30,
						KDP / 2, widthLayout / 2, KDP / 2, Color.TRANSPARENT,
						Color.CYAN, Shader.TileMode.MIRROR);
				mainPaint.setShader(gradient);
				canvas.drawLine(leftLayout - K30, KDP / 2, righttLayout + K30,
						KDP / 2, mainPaint);
				mainPaint.setShader(null);
				*/
				
				if (stateConnect == CONNECT_HOVER) {
					drawable = getResources().getDrawable(
							R.drawable.boder_caidat_ketnoi_active_new);
				} else {
					drawable = getResources().getDrawable(
							R.drawable.boder_caidat_ketnoi_inactive_new);
				}
				drawable.setBounds(rectLeft);
				drawable.draw(canvas);
				
				switch (stateConnect) {
				case CONNECTED:
					drawable = getResources().getDrawable(
							R.drawable.image_daumay_daketnoi_71x65_x);
					if (isConnected) {
						drawable = getResources().getDrawable(
								R.drawable.icon_daumay_daketnoi_71x65);
					}
					if(MyApplication.intSvrModel == MyApplication.SONCA_HIW){
						drawable = getResources().getDrawable(
								R.drawable.icon_daumay_ktvwifi_bottom);
					}
					if(MyApplication.intSvrModel == MyApplication.SONCA_KM2){
						drawable = getResources().getDrawable(
								R.drawable.icon_daumay_km2_bottom);
					}
					if(MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM){
						drawable = getResources().getDrawable(
								R.drawable.kb_oem_active);
					}	
					if(MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI){
						drawable = getResources().getDrawable(
								R.drawable.km1wifi_active);
					}
					if(MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI){
						drawable = getResources().getDrawable(
								R.drawable.kb39c_daluu);
					}
					if(MyApplication.intSvrModel == MyApplication.SONCA_KBX9){
						drawable = getResources().getDrawable(
								R.drawable.daumay_kb);
					}
					if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
						drawable = getResources().getDrawable(R.drawable.daumay_9108);
						if(MyApplication.flagSmartK_CB){
							drawable = getResources().getDrawable(R.drawable.cloudbox_active);
						}
						if(MyApplication.flagSmartK_801){
							drawable = getResources().getDrawable(R.drawable.sb801_active);
						}
						if(MyApplication.flagSmartK_KM4){
							drawable = getResources().getDrawable(R.drawable.km4_active);
						}
					}
					if(MyApplication.intSvrModel == MyApplication.SONCA_TBT){
						drawable = getResources().getDrawable(R.drawable.daumay_tbt);
					}
					break;
				case INCONNECT:
					drawable = getResources().getDrawable(
							R.drawable.image_daumay_inactive_71x65_x);
					break;
				case CONNECT_HOVER:
					drawable = getResources().getDrawable(
							R.drawable.image_daumay_inactive_71x65_x);
					if (isConnected) {
						drawable = getResources().getDrawable(
								R.drawable.icon_daumay_hover_71x65);
					}
					if(MyApplication.intSvrModel == MyApplication.SONCA_HIW){
						drawable = getResources().getDrawable(
								R.drawable.icon_daumay_ktvwifi_bottom);
					}
					if(MyApplication.intSvrModel == MyApplication.SONCA_KM2){
						drawable = getResources().getDrawable(
								R.drawable.icon_daumay_km2_bottom);
					}		
					if(MyApplication.intSvrModel == MyApplication.SONCA_KBX9){
						drawable = getResources().getDrawable(
								R.drawable.daumay_kb);
					}
					if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
						drawable = getResources().getDrawable(R.drawable.daumay_9108);
						if(MyApplication.flagSmartK_CB){
							drawable = getResources().getDrawable(R.drawable.cloudbox_active);
						}
						if(MyApplication.flagSmartK_801){
							drawable = getResources().getDrawable(R.drawable.sb801_active);
						}
						if(MyApplication.flagSmartK_KM4){
							drawable = getResources().getDrawable(R.drawable.km4_active);
						}
					}
					if(MyApplication.intSvrModel == MyApplication.SONCA_TBT){
						drawable = getResources().getDrawable(R.drawable.daumay_tbt);
					}
					break;
				default:
					break;
				}

				drawable.setBounds(KD2L, KD2T, KD2R, KD2B);
				drawable.draw(canvas);

				// ----------NAC WIFI-------------//
				if (isConnected) {
					if(MyApplication.intSvrModel == MyApplication.SONCA_HIW || MyApplication.intSvrModel == MyApplication.SONCA_KM2
							 || MyApplication.intSvrModel == MyApplication.SONCA_KBX9
							 || MyApplication.intSvrModel == MyApplication.SONCA_TBT){
						switch (stateConnect) {
						case CONNECTED:
						case CONNECT_HOVER:
							switch (MyApplication.levelWifi) {
							case 0:
								drawable = drawWifi0_hiw;
								break;
							case 1:
								drawable = drawWifi1_hiw;
								break;
							case 2:
								drawable = drawWifi2_hiw;
								break;
							case 3:
								drawable = drawWifi3_hiw;
								break;
							case 4:
								drawable = drawWifi4_hiw;
								break;
							case 5:
								drawable = drawWifi4_hiw;
								break;
							default:
								break;
							}
							break;
						default:
							break;
						}
						
					} else if (MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
						switch (MyApplication.levelWifi) {
						case 0:
							drawable = drawWifi0_smartk;
							break;
						case 1:
							drawable = drawWifi1_smartk;
							break;
						case 2:
							drawable = drawWifi2_smartk;
							break;
						case 3:
							drawable = drawWifi3_smartk;
							break;
						case 4:
							drawable = drawWifi4_smartk;
							break;
						case 5:
							drawable = drawWifi4_smartk;
							break;
						default:
							break;
						}
						
					} else {
						switch (stateConnect) {
						case CONNECTED:
						case CONNECT_HOVER:
							switch (MyApplication.levelWifi) {
							case 0:
								drawable = drawWifi0;
								break;
							case 1:
								drawable = drawWifi1;
								break;
							case 2:
								drawable = drawWifi2;
								break;
							case 3:
								drawable = drawWifi3;
								break;
							case 4:
								drawable = drawWifi4;
								break;
							case 5:
								drawable = drawWifi4;
								break;
							default:
								break;
							}
							break;
						default:
							break;
						}
						
					}
					
					drawable.setBounds(KD2L, KD2T, KD2R, KD2B);
					drawable.draw(canvas);
				} else {
					switch (countAutoConnect) {
					case 0:
						drawable = drawWifi0;
						break;
					case 1:
						drawable = drawWifi1;
						break;
					case 2:
						drawable = drawWifi2;
						break;
					case 3:
						drawable = drawWifi3;
						break;
					case 4:
						drawable = drawWifi4;
						break;
					default:
						drawable = drawWifi0_xam;
						break;
					}
					
					drawable.setBounds(KD2L, KD2T, KD2R, KD2B);
					drawable.draw(canvas);
				}

				// -----------------------//

				if (isConnected) {
					switch (statePlayList) {
					case ACTIVE:
						drawable = getResources().getDrawable(
								R.drawable.touch_tab_playlist_hover_144x144);
						break;
					case INACTIVE:
						drawable = getResources().getDrawable(
								R.drawable.touch_tab_playlist_hover_144x144);
						break;
					default:
						break;
					}
				} else {
					drawable = getResources().getDrawable(
							R.drawable.touch_tab_playlist_inactive_65x65);
				}
				drawable.setBounds(KD4L, KD4T, KD4R, KD4B);
				drawable.draw(canvas);

				float textWidth = 0;
				if (isConnected) {
					mainPaint.setColor(Color.GREEN);
				} else {
					mainPaint.setARGB(125, 180, 192, 194);
				}
				
				String no_connect = getResources().getString(
						R.string.no_connect);
				if (serverName == null || serverName.equals("")) {
					serverName = no_connect;
					if(countAutoConnect != -1){
						serverName = getResources().getString(R.string.connect_auto);
					}
					
					if (stateConnect == CONNECTED) {
						serverName = getResources().getString(
								R.string.connected);
					}
				}
				mainPaint.setStyle(Style.FILL);
				mainPaint.setTextSize(KTS1);

				mainPaint.setTypeface(Typeface.DEFAULT);
				textWidth = mainPaint.measureText(serverName);
				canvas.drawText(serverName, (rectLeft.left + rectLeft.right)
						/ 2 - textWidth / 2, KT1, mainPaint);
				if (serverName.equals(no_connect)) {
					serverName = "";
				}
				if (isConnected) {					
					if (currectSong == null
							|| currectSong.trim().equals("")
							|| currectSong.trim().equals(" ")
							|| MyApplication.intWifiRemote != MyApplication.SONCA) {
						KT2 = (float) (50 * heightLayout / 184);
						KT3 = (float) (90 * heightLayout / 184);
						KT5 = (float) (135 * heightLayout / 184);

						String text = getResources().getString(
								R.string.groupview_4);
						mainPaint.setTextSize(KTS3);
						mainPaint.setTypeface(Typeface.DEFAULT_BOLD);
						mainPaint.setARGB(255, 180, 254, 255);
						textWidth = mainPaint.measureText(text);
						canvas.drawText(text,
								(righttLayout + K5 + widthLayout - K60) / 2
										- textWidth / 2, KT2, mainPaint);

						text = getResources().getString(R.string.groupview_5);
						textWidth = mainPaint.measureText(text);
						canvas.drawText(text,
								(righttLayout + K5 + widthLayout - K60) / 2
										- textWidth / 2, KT3, mainPaint);

						text = getResources().getString(R.string.groupview_6);
						textWidth = mainPaint.measureText(text);
						canvas.drawText(text,
								(righttLayout + K5 + widthLayout - K60) / 2
										- textWidth / 2, KT5, mainPaint);
					} else {
						mainPaint.setTextSize(KTS3);
						mainPaint.setARGB(255, 0, 253, 253);
						String str = cutText(KTS3, Kcut, currectSong);
						textWidth = mainPaint.measureText(String
								.valueOf(str));
						canvas.drawText(str, (righttLayout + K5
								+ widthLayout - K40)
								/ 2 - textWidth / 2, KT5, mainPaint);
						mainPaint.setARGB(255, 180, 254, 255);
						mainPaint.setTypeface(Typeface.DEFAULT_BOLD);
						textWidth = mainPaint.measureText(String
								.valueOf("DA CHON"));
						canvas.drawText(
								getResources().getString(R.string.main_left_1),
								righttLayout + K40, KT2, mainPaint);

						mainPaint.setTextSize(KTS2);
						mainPaint.setARGB(255, 37, 255, 47);
						textWidth = mainPaint
								.measureText(String.valueOf("000"));
						canvas.drawText(String.valueOf(sumSong), righttLayout
								+ K40, KT3, mainPaint);
						mainPaint.setTextSize(KTS3);
						mainPaint.setARGB(255, 180, 254, 255);
						canvas.drawText(
								getResources().getString(R.string.main_left_2),
								righttLayout + K40 + K5 + textWidth, KT3,
								mainPaint);

						mainPaint.setColor(Color.parseColor("#ffe002"));
						mainPaint.setTypeface(Typeface.DEFAULT);
						String strStatus = "";
						if (isPlay) {
							strStatus = getResources().getString(
									R.string.main_left_3a);
							drawable = getResources().getDrawable(
									R.drawable.touch_pbstatus_play_48x48);
						} else {
							strStatus = getResources().getString(
									R.string.main_left_3b);
							drawable = getResources().getDrawable(
									R.drawable.touch_pbstatus_pause_48x48);
						}
						if (flagStateNextSong) {
							strStatus = getResources().getString(
									R.string.main_left_3c);
							canvas.drawText(strStatus, righttLayout + K65, KT4,
									mainPaint);
							drawable = getResources().getDrawable(
									R.drawable.pbstatus_next_ipad);
						} else {
							canvas.drawText(strStatus, righttLayout + K60, KT4,
									mainPaint);
						}
						drawable.setBounds(KD3L, KD3T, KD3R, KD3B);
						drawable.draw(canvas);
					}
				} else {
					KT2 = (float) (50 * heightLayout / 184);
					KT3 = (float) (90 * heightLayout / 184);
					KT5 = (float) (135 * heightLayout / 184);

					String text = getResources()
							.getString(R.string.groupview_1);
					mainPaint.setTextSize(KTS3);
					mainPaint.setARGB(125, 180, 192, 194);
					mainPaint.setTypeface(Typeface.DEFAULT_BOLD);
					textWidth = mainPaint.measureText(text);
					canvas.drawText(text,
							(righttLayout + K5 + widthLayout - K60) / 2
									- textWidth / 2, KT2, mainPaint);

					text = getResources().getString(R.string.groupview_2);
					textWidth = mainPaint.measureText(text);
					canvas.drawText(text,
							(righttLayout + K5 + widthLayout - K60) / 2
									- textWidth / 2, KT3, mainPaint);

					text = getResources().getString(R.string.groupview_3);
					textWidth = mainPaint.measureText(text);
					canvas.drawText(text,
							(righttLayout + K5 + widthLayout - K60) / 2
									- textWidth / 2, KT5, mainPaint);
				}

				/*
				 * mainPaint.setColor(Color.GREEN); canvas.drawLine(intconnect,
				 * 0, intconnect, getHeight(), mainPaint);
				 */

				// ----------ICON KHOA-------------//
				if (isConnected && MyApplication.flagDeviceUser == false) {
					if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
						if(KTVMainActivity.serverStatus.isCaptionAPIValid()){
							drawBlock.setBounds(KD5L, KD5T, KD5R, KD5B);
							drawBlock.draw(canvas);	
						}
					} else {
						if(TouchMainActivity.serverStatus.isCaptionAPIValid()){
							drawBlock.setBounds(KD5L, KD5T, KD5R, KD5B);
							drawBlock.draw(canvas);	
						}	
					}
					
				}

				resetPaint();
				if (isCreated) {
					isCreated = false;
					if (listener != null) {
						listener.OnFAKEShowConnect();
					}
				}

			} else { // KARTROL

				resetPaint();

				float leftLayout = (float) (0.17 * widthLayout);
				float righttLayout = widthLayout - leftLayout;
				if (state == EXIT) {
				}

				mainPath = new Path();
				mainPath.moveTo(leftLayout - K15, KDP);
				mainPath.lineTo(leftLayout, heightLayout);
				mainPath.lineTo(leftLayout - K30, 0);
				mainPath.lineTo(righttLayout + K30, 0);
				mainPath.lineTo(righttLayout, heightLayout);
				mainPath.lineTo(righttLayout + K15, KDP);
				mainPath.lineTo(leftLayout - K15, KDP);

				mainPaint.setStyle(Style.STROKE);
				mainPaint.setStrokeWidth(1);
				mainPaint.setARGB(255, 1, 43, 81);

				// ------path lỉne 1--------
				canvas.drawPath(mainPath, mainPaint);
				// -----------------------//

				if (isConnected) {
					switch (statePlayList) {
					case ACTIVE:
						drawListAc.setBounds(rectList);
						drawListAc.draw(canvas);
						break;
					case INACTIVE:
						drawListNo.setBounds(rectList);
						drawListNo.draw(canvas);
						break;
					default:
						break;
					}
				} else {
					drawListNo.setBounds(rectList);
					drawListNo.draw(canvas);
				}

				mainPaint.setStrokeWidth(KS);
				mainPaint.setARGB(255, 1, 43, 81);
				canvas.drawLine(0, heightLayout + KL, widthLayout, heightLayout
						+ KL, mainPaint);

				mainPaint.setStrokeWidth(KDP);
				mainPaint.setColor(Color.GREEN);
				LinearGradient gradient = new LinearGradient(leftLayout - K30,
						KDP / 2, widthLayout / 2, KDP / 2, Color.TRANSPARENT,
						Color.CYAN, Shader.TileMode.MIRROR);
				mainPaint.setShader(gradient);
				canvas.drawLine(leftLayout - K30, KDP / 2, righttLayout + K30,
						KDP / 2, mainPaint);
				mainPaint.setShader(null);
				if (stateConnect == CONNECT_HOVER) {
					drawable = getResources().getDrawable(
							R.drawable.boder_caidat_ketnoi_active_new);
				} else {
					drawable = getResources().getDrawable(
							R.drawable.boder_caidat_ketnoi_inactive_new);
				}
				drawable.setBounds(rectLeft);
				drawable.draw(canvas);

				switch (stateConnect) {
				case CONNECTED:
					drawable = getResources().getDrawable(
							R.drawable.image_daumay_daketnoi_71x65_x);
					if (isConnected) {
						drawable = getResources().getDrawable(
								R.drawable.icon_daumay_kartrol_bottom);
						if(MyApplication.intSvrModel == MyApplication.SONCA_KM1){
							drawable = getResources().getDrawable(
									R.drawable.icon_daumay_km1_bottom);
						}
					}
					break;
				case INCONNECT:
					drawable = getResources().getDrawable(
							R.drawable.image_daumay_inactive_71x65);
					break;
				case CONNECT_HOVER:
					drawable = getResources().getDrawable(
							R.drawable.image_daumay_chuaketnoi_71x65);
					if (isConnected) {
						drawable = getResources().getDrawable(
								R.drawable.icon_daumay_kartrol_bottom);
						if(MyApplication.intSvrModel == MyApplication.SONCA_KM1){
							drawable = getResources().getDrawable(
									R.drawable.icon_daumay_km1_bottom);
						}
					}
					break;
				default:
					break;
				}

				drawable.setBounds(KD2L, KD2T, KD2R, KD2B);
				drawable.draw(canvas);

				// ----------NAC WIFI-------------//
				if (isConnected) {
					switch (stateConnect) {
					case CONNECTED:
					case CONNECT_HOVER:
						switch (MyApplication.levelWifi) {
						case 0:
							drawable = drawWifi0;
							break;
						case 1:
							drawable = drawWifi1;
							break;
						case 2:
							drawable = drawWifi2;
							break;
						case 3:
							drawable = drawWifi3;
							break;
						case 4:
							drawable = drawWifi4;
							break;
						case 5:
							drawable = drawWifi4;
							break;
						default:
							break;
						}
						break;
					default:
						break;
					}
					drawable.setBounds(KD2L, KD2T, KD2R, KD2B);
					drawable.draw(canvas);
				}

				// -----------------------//

				String no_connect = getResources().getString(
						R.string.no_connect);
				if (serverName == null || serverName.equals("")) {
					serverName = no_connect;
					if (stateConnect == CONNECTED) {
						serverName = getResources().getString(
								R.string.connected);
					}
				}
				mainPaint.setStyle(Style.FILL);
				mainPaint.setTextSize(KTS1);
				mainPaint.setTypeface(Typeface.DEFAULT);
				float textWidth = mainPaint.measureText(serverName);
				canvas.drawText(serverName, (rectLeft.left + rectLeft.right)
						/ 2 - textWidth / 2, KT1, mainPaint);
				if (serverName.equals(no_connect)) {
					serverName = "";
				}

				drawable = getResources().getDrawable(
						R.drawable.icon_daumay_kartrol_bottom);
				if(MyApplication.intSvrModel == MyApplication.SONCA_KM1){
					drawable = getResources().getDrawable(
							R.drawable.icon_daumay_km1_bottom);
				}
				drawable.setBounds(rectPlaylistKar);
				drawable.draw(canvas);

				if (nameRemote.equals("")) {
					nameRemote = getResources().getString(R.string.select_list_1);
				}
				
				switch (MyApplication.intSelectList) {
				case MyApplication.SelectList_SONCA:
					nameRemote = getResources().getString(R.string.select_list_1);
					break;
				case MyApplication.SelectList_USER:
					nameRemote = getResources().getString(R.string.select_list_2);
					break;
				case MyApplication.SelectList_ALL:
					nameRemote = getResources().getString(R.string.select_list_3);
					break;
				default:
					break;
				}
				
				mainPaint.setStyle(Style.FILL);
				mainPaint.setTextSize(KTS1);
				mainPaint.setTypeface(Typeface.DEFAULT);
				textWidth = mainPaint.measureText(nameRemote);
				textKarX = (rectPlaylistKar.left + rectPlaylistKar.right) / 2
						- mainPaint.measureText(nameRemote) / 2;
				canvas.drawText(nameRemote, textKarX, textKarY, mainPaint);

				String str =  getResources().getString(R.string.select_list_title);
				mainPaint.setARGB(255, 0, 253, 253);
				textWidth = mainPaint.measureText(str);
				textKarX = (rectPlaylistKar.left + rectPlaylistKar.right) / 2
						- mainPaint.measureText(str) / 2;
				canvas.drawText(str, textKarX, textKarY2, mainPaint);
				
				resetPaint();
				if (isCreated) {
					isCreated = false;
					if (listener != null) {
						listener.OnFAKEShowConnect();
					}
				}
			}
		} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {

		}
	}

	private String cutText(float textSize, float maxLength, String content) {
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setTextSize(textSize);
		float length = paint.measureText(content);
		if (length > maxLength) {
			String[] strings = content.split(" ");
			StringBuffer buffer = new StringBuffer("");
			for (int i = 0; i < strings.length; i++) {
				length = paint.measureText(buffer.toString() + " " + strings[i]
						+ "...");
				if (length < maxLength) {
					buffer.append(strings[i] + " ");
				} else {
					int t = buffer.length() - 1;
					if (t >= 0)
						buffer.deleteCharAt(buffer.length() - 1);
					break;
				}
			}
			buffer.append("...");
			return buffer.toString();
		} else {
			return content;
		}
	}

	private Rect rectList;
	private Rect rectLeft = new Rect();
	private Rect rectBlock = new Rect();
	private Rect rectSetting = new Rect();
	private Rect rectPlaylistKar = new Rect();
	private int intconnect;
	
	private Rect rectLine = new Rect();
	private Rect rectBackgroud = new Rect();
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		setK(w, h);
		float leftLayout = (float) (0.17 * widthLayout);
		float righttLayout = widthLayout - leftLayout;
		intconnect = (int) (leftLayout - K35);

		rectList = new Rect((int) (0.828 * w), (int) (0), (int) (0.999 * w),
				(int) (0.93 * h));
		rectLeft = new Rect((int) (0.000 * w), (int) (0), (int) (0.172 * w),
				(int) (0.93 * h));

		int tamX = (int) (0.91 * w);
		int tamY = (int) (0.31 * h);
		int we = (int) (0.251 * h);
		rectPlaylistKar.set((int) (tamX - 1.01 * we), tamY - we,
				(int) (tamX + 1.01 * we), tamY + we);
		textKarS = KTS1;
		textKarY = (float) (0.75 * h);
		textKarY2 = (float) (0.25 * h);
		
		int tx = (int) (0.037 * w);
		int ty = (int) (0.45 * h);
		int wr = (int) (0.17 * h);
		int hr = wr;
		rectSetting.set(tx - wr, ty - hr, tx + wr, ty + hr);

		tx = (int) (0.030 * w);
		ty = (int) (0.12 * h);
		hr = (int) (0.06 * h);
		wr = 2 * hr;
		rectBlock.set(tx - wr, ty - hr, tx + wr, ty + hr);

		pathBack = new Path();
		pathBack.moveTo(K10, 0);
		pathBack.lineTo(K40, heightLayout);
		pathBack.lineTo(K130, heightLayout);
		pathBack.lineTo(K100, 0);
		pathBack.lineTo(K10, 0);
		// ------path lỉne 2--------
		mainPath = new Path();
		/*
		 * mainPath.moveTo(K105, 0); mainPath.lineTo(K135, heightLayout);
		 * mainPath.lineTo(leftLayout - K5, heightLayout);
		 * mainPath.lineTo(leftLayout - K35, 0); mainPath.lineTo(K105, 0);
		 */
		// ------path lỉne 3--------
		// mainPath.moveTo(leftLayout - K15, KDP);
		// mainPath.lineTo(leftLayout, heightLayout);
		// mainPath.lineTo(leftLayout - K30, 0);
		// mainPath.lineTo(righttLayout + K30, 0);
		// mainPath.lineTo(righttLayout, heightLayout);
		// mainPath.lineTo(righttLayout + K15, KDP);
		// mainPath.lineTo(leftLayout - K15, KDP);

		// ------path lỉne 4--------
		pathPlaylist = new Path();
		pathPlaylist.moveTo(righttLayout + K35, 0);
		pathPlaylist.lineTo(righttLayout + K5, heightLayout);
		pathPlaylist.lineTo(widthLayout - K40, heightLayout);
		pathPlaylist.lineTo(widthLayout - K10, 0);
		pathPlaylist.lineTo(righttLayout + K35, 0);
		// ------------------------------
		/*
		 * mainPaint = new Paint(); mainPaint.setAntiAlias(true);
		 * mainPaint.setDither(true); mainPaint.setColor(Color.argb(235, 74,
		 * 138, 255)); mainPaint.setStrokeWidth(6f);
		 * mainPaint.setStyle(Paint.Style.STROKE);
		 * mainPaint.setStrokeJoin(Paint.Join.ROUND);
		 * mainPaint.setStrokeCap(Paint.Cap.ROUND);
		 * 
		 * paintPath = new Paint(); paintPath.set(mainPaint);
		 * paintPath.setColor(Color.argb(235, 74, 138, 255));
		 * paintPath.setStrokeWidth(6f); paintPath.setMaskFilter(new
		 * BlurMaskFilter(3, BlurMaskFilter.Blur.NORMAL));
		 */
		
		rectBackgroud.set(0, 0, widthLayout, (int)(0.97*h - KL));
		rectLine.set(0, (int)(0.97*h - KL), widthLayout, (int)(0.97*h));
		
	}

	private boolean isSettingHover = false;
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			float x = event.getX();
			float y = event.getY();
			/*
			 * if (x > KD1L && x < KD1R && y > KD1T && y < KD1B) { boolExit =
			 * true; isSettingHover = true; }
			 * 
			 * if (x > KD2L && x < KD2R && y > KD4T && y < KD4B * 2) {
			 * stateConnect = CONNECT_HOVER; }
			 */

			if (x > KD1L && x < intconnect && y > KD4T && y < KD4B * 2) {
				stateConnect = CONNECT_HOVER;
			}

			if (x > KD4L - K60 * 3 && x < KD4R && y > KD4T && y < KD4B * 2
					&& stateConnect == CONNECTED) {
				statePlayList = ACTIVE;
			}
			invalidate();
		}
			break;

		case MotionEvent.ACTION_UP: {
			float x = event.getX();
			float y = event.getY();
			/*
			 * if (x > KD1L && x < KD1R && y > KD1T && y < KD1B) { boolExit =
			 * false; if (listener != null) { listener.OnExit(state); } } if (x
			 * > KD2L && x < KD2R && y > KD4T && y < KD4B * 2) { if (listener !=
			 * null) { listener.OnShowConnect(); } }
			 */
			if (x > KD1L && x < intconnect && y > KD4T && y < KD4B * 2) {
				if (listener != null) {
					listener.OnShowConnect();
				}
			}

			if (x > KD4L - K60 * 3 && x < KD4R && y > KD4T && y < KD4B * 2) {
				if (MyApplication.intWifiRemote == MyApplication.SONCA) {
					if (listener != null) {
						listener.OnPlayList();
					}
				} else {
					if (listener != null) {
						listener.OnShowKM1_List();
					}
				}
			}
			isSettingHover = false;
			stateConnect = saveConnect;
			statePlayList = savePlayList;
			invalidate();
		}
			break;
		default:
			break;
		}
		return true;
	}

	public void setCurrectSong(String currectSong) {
		this.nowSongName = currectSong;
		this.flagStateNextSong = false;
		if(listener != null){
			listener.OnCallAutoVideoViral(saveSumSong);
		}
		CreateTimer();
	}

	private int saveSumSong = 0;
	private int countAnimation = 0;
	
	public void setSumSong(int sumSong) {
		this.sumSong = sumSong;
		if(sumSong != saveSumSong){
			countAnimation = 0;
			CreateTimerAnimation();
		}
		saveSumSong = sumSong;
		invalidate();
	}
	
	public int getSumSong(){
		return this.saveSumSong;
	}

	public void setInActivePlayList() {
		statePlayList = savePlayList = INACTIVE;
		invalidate();
	}

	public void setActivePlayList() {
		statePlayList = savePlayList = ACTIVE;
		invalidate();
	}

	public int isActive() {
		return statePlayList;
	}

	private int KD1L, KD1R, KD1T, KD1B;
	private int KD2L, KD2R, KD2T, KD2B;
	private int KD3L, KD3R, KD3T, KD3B;
	private int KD4L, KD4R, KD4T, KD4B;
	private float Kcut;
	private float KH, KL, KS, KDP;
	private float KT1, KTS1, KT2, KTS2, KT3, KTS3, KT4, KT5;
	private float K5, K10, K15, K30, K35, K40, K60, K65, K80, K100, K105, K130,
			K135;
	private float KTS4;
	private float textKarS, textKarX, textKarY, textKarY2;
	private int KD5L, KD5R, KD5T, KD5B;
	
	private void setK(int width, int height) {
		widthLayout = width;
		heightLayout = height;
		K5 = 7 * width / 1920;
		K10 = 10 * width / 1920;
		K15 = 15 * width / 1920;
		K30 = 30 * width / 1920;
		K35 = 37 * width / 1920;
		K40 = 40 * width / 1920;
		K60 = 60 * width / 1920;
		K65 = 68 * width / 1920;
		K80 = 80 * width / 1920;
		K100 = 100 * width / 1920;
		K105 = 105 * width / 1920;
		K130 = 130 * width / 1920;
		K135 = 135 * width / 1920;

		Kcut = 270 * width / 1920;

		KH = (float) (25 * height / 184);
		KL = (float) (13 * height / 184);
		KS = (float) (4 * height / 184);
		KDP = (float) (8 * height / 184);
		heightLayout = (int) (height - KH);

		KT1 = (float) (140 * height / 184);
		KTS1 = 13 * width / 1080;
		KT2 = (float) (30 * height / 184);
		KTS2 = 46 * height / 184;
		KT3 = (float) (78 * height / 184);
		KT4 = (float) (110 * height / 184);
		KT5 = (float) (150 * height / 184);
		KTS3 = 28 * height / 184;
		KTS4 = 18 * height / 184;

		KD1L = 30 * width / 1920;
		KD1R = (30 + 72) * width / 1920;
		KD1T = -5 * height / 184;
		KD1B = (-5 + 144) * height / 184;
/*
		KD2L = 140 * width / 1920;
		KD2R = (140 + 144) * width / 1920;
		KD2T = -5 * height / 184;
		KD2B = (-5 + 144) * height / 184;
*/		
		KD2L = 115 * widthLayout / 1920;
		KD2R = (115 + 104) * widthLayout / 1920;
		KD2T = 20 * heightLayout / 184;
		KD2B = (20 + 104) * heightLayout / 184;
		
		KD3L = 1615 * width / 1920;
		KD3R = (1615 + 48) * width / 1920;
		KD3T = 75 * height / 184;
		KD3B = (75 + 48) * height / 184;

		KD4L = 1780 * width / 1920;
		KD4R = (1780 + 144) * width / 1920;
		KD4T = -20 * height / 184;
		KD4B = (-20 + 144) * height / 184;
		
		KD5L = (105 - 25) * widthLayout / 1920;
		KD5R = 105 * widthLayout / 1920;
		KD5T = (120 - 30) * heightLayout / 184;
		KD5B = 120 * heightLayout / 184;
		
	}

	private void resetPaint() {
		/*
		 * mainText.reset(); //---------// mainPaint.reset();
		 * mainPaint.setAntiAlias(true); mainPaint.setShader(null);
		 */
	}
	

	private boolean flagStateNextSong = false;	
	private String nextSongName = "";
	private String nowSongName = "";
	private int currentNextIdx5 = -1;
	
	public String getNextSongName(){
		return this.nextSongName;
	}

	public int getCurrentNextIdx5(){
		return this.currentNextIdx5;
	}
	
	public void setNextSongName(String newName, int newCurrentId) {
		MyLog.e("setNextSongName", newCurrentId + " -- " + newName);				
		if(newName != null){
			this.nextSongName  = newName;
			this.currentNextIdx5 = newCurrentId;
		} else {
			this.nextSongName = "";
		}
		CreateTimer();		
	}

	private Timer timerSong = null;
	private void CreateTimer(){
		if(timerSong != null){
			timerSong.cancel();
			timerSong = null;
		}
		timerSong = new Timer();
		timerSong.schedule(new TimerTask() {
			
			@Override
			public void run() {
				flagStateNextSong = !flagStateNextSong;
				
				if(!isPlay){
					flagStateNextSong = false;
				}
				
				ArrayList<Song> list = ((MyApplication) context.getApplicationContext())
						.getListActive();
				if(list!=null){
					if(list.size() == 0){
						flagStateNextSong = false;
					}
				}
				if(flagStateNextSong){					
					if(list!=null){
						if(list.size() > 0){
							if(nextSongName.equals("")){
								final Song song = list.get(0);
								//MyLog.e("search DB for Next Song",".........................");
								String name = DBInterface.DBGetNameSong(context, String.valueOf(song.getIndex5()));
								
								if(name.equals("")){
									if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
										name = DBInterface.DBGetNameSong_YouTube(context, String.valueOf(song.getIndex5()));
									}	
								}
								
								nextSongName  = name;
								currentNextIdx5 = song.getIndex5();	
							}
						}
					}					
					currectSong = nextSongName;
//					MyLog.e("timerSong", "currectSong = nextSongName = " + nextSongName);
				}else{
					currectSong = nowSongName;

//					MyLog.e("timerSong", "currectSong = nowSongName = " + nowSongName);
				}
				handlerSong.sendEmptyMessage(0);
			}
		}, 200, 4000);
	}
	
	private Handler handlerSong = new Handler(){
		public void handleMessage(Message msg) {
			invalidate();
		};
	};
	
	protected void finalize() throws Throwable {
		if(timerSong != null){
			timerSong.cancel();
			timerSong = null;
		}
		if(timerAnimationSong != null){
			timerAnimationSong.cancel();
			timerAnimationSong = null;
		}
	};
	
	private Timer timerAnimationSong = null;
	private void CreateTimerAnimation(){
		if(timerAnimationSong != null){
			timerAnimationSong.cancel();
			timerAnimationSong = null;
		}
		timerAnimationSong = new Timer();
		timerAnimationSong.schedule(new TimerTask() {
			
			@Override
			public void run() {
				if(countAnimation == -1){
					return;
				}
				
				if(countAnimation > 4){
					timerAnimationSong.cancel();
					timerAnimationSong = null;
					return;
				}
				
				countAnimation++;
				
				handlerSong.sendEmptyMessage(0);
			}
		}, 50, 700);
	}

	// --------------------------
	private int countAutoConnect = -1;
	private Timer timerAutoConnect = null;
	
	public void StartTimerAutoConnect(){
		if(countAutoConnect != -1){
			return;
		}
		
		MyLog.e("TEST TEST", "StartTimerAutoConnect FIRST TIME");
		
		if(timerAutoConnect != null){
			timerAutoConnect.cancel();
			timerAutoConnect = null;
		}
		timerAutoConnect = new Timer();
		timerAutoConnect.schedule(new TimerTask() {
			
			@Override
			public void run() {
								
				countAutoConnect++;
				
				if(countAutoConnect > 4){
					countAutoConnect = 0;
				}
				
				handlerSong.sendEmptyMessage(0);
			}
		}, 50, 500);
	}
	
	public void StopTimerAutoConnect(){
		if(timerAutoConnect != null){
			timerAutoConnect.cancel();
			timerAutoConnect = null;
		}
		
		if(serverName != null && serverName.equals(getResources().getString(R.string.connect_auto))){
			serverName = null;
		}
		
		countAutoConnect = -1;
		invalidate();
	}
	
	public String getCurrectSongName(){
		return this.nowSongName;
	}
	
}
