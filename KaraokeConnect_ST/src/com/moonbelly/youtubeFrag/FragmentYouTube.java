package com.moonbelly.youtubeFrag;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

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

import com.moonbelly.youtube.DeveloperKey;
import com.moonbelly.youtubeFrag.AdapterYouTube.OnAdapterYouTubeListener;











import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.LoadSong.BaseLoadSong;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.CustomView.TouchSearchView;
import vn.com.sonca.Touch.CustomView.TouchTotalView;
import vn.com.sonca.Touch.CustomView.TouchTotalView.OnTotalViewListener;
import vn.com.sonca.Touch.Listener.TouchIBaseFragment;
import vn.com.sonca.Touch.touchcontrol.TouchFragmentBase;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity.OnMainListener;
import vn.com.sonca.params.ServerStatus;
import vn.com.sonca.params.Song;
import vn.com.sonca.utils.StringUtils;
import vn.com.sonca.utils.AppConfig.LANG_INDEX;
import vn.com.sonca.zzzzz.MyApplication;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AbsListView.OnScrollListener;

public class FragmentYouTube extends TouchFragmentBase implements OnMainListener{
	private String TAB = "FragmentYouTube";
	
	private TouchMainActivity mainActivity;
	private TouchIBaseFragment listener;	
	private Context context;
	
	private LinearLayout layoutShowThongBao;
	private LinearLayout layoutGuide, layoutGuideWifi;
	private String rootPath = "";
	private GridView gridView;
	private TouchTotalView totalView;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (TouchIBaseFragment) activity;
			mainActivity = (TouchMainActivity) activity;
		} catch (Exception ex) {
		}
	}	

	@Override
	public void onDetach() {
		super.onDetach();
		MyApplication.flagPopupYouTube = false;
		stopLoadData();
		stopReadYoutubeData();
		stopPingServer();
	}
	
	private PingServerTask pingSeverTask;
	
	private void startPingServer(){
		stopPingServer();
		pingSeverTask = new PingServerTask();
		pingSeverTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "");
	}
	
	private void stopPingServer(){
		if(pingSeverTask != null){
			pingSeverTask.cancel(true);
			pingSeverTask = null;
		}
	}
	
	private int mainType = 1; // 1 - karaoke beat - default
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity().getApplicationContext();
		View view = inflater.inflate(R.layout.fragment_youtube, container, false);
		
		layoutShowThongBao = (LinearLayout)view.findViewById(R.id.layoutShowThongBao);
		layoutGuide = (LinearLayout)view.findViewById(R.id.layoutGuide);	
		layoutGuideWifi = (LinearLayout)view.findViewById(R.id.layoutGuideWifi);	
		
		rootPath = android.os.Environment
				.getExternalStorageDirectory()
				.toString()
				.concat(String.format("/%s/%s", "Android/data",
						context.getPackageName()));
		
		String filePath = rootPath.concat("/YTP");
		File fDir = new File(filePath);
		if(!fDir.exists()){
			fDir.mkdir();
		}
				
		totalView = (TouchTotalView) view.findViewById(R.id.totalview);
		totalView.setVisibility(View.VISIBLE);
		totalView.setLayoutYouTube();
		
		totalView.setOnTotalViewListener(new OnTotalViewListener() {
			
			@Override
			public void OnUpdatePic() {
				
			}			
		});
		
		gridView = (GridView) view.findViewById(R.id.gridview);
		gridView.setFastScrollEnabled(true);
		gridView.setFocusable(false);
		gridView.setOnScrollListener(onScrollListener);
		
		startPingServer();
		
		return view;
	}

	@Override
	protected void UpdateAdapter() {
		
	}

	@Override
	public void OnLoadSucessful() {
		
	}
	
	private String search = "";
	
	@Override
	public void OnSearchMain(int state1, int state2, String search) {
		MyLog.e(TAB, "OnSearchMain -- search = [" + search + "] -- state1 = " + state1 + " -- state2 = " + state2);

		if(state2 == TouchSearchView.TATCA){ // tat ca
			mainType = 0;
		} else if(state2 == TouchSearchView.YOUTUBE_KARAOKE){ // karaoke beat
			mainType = 1;
		} else {
			mainType = 0;
		}
		
		MyApplication.flagPopupYouTube = false;
		
		if(search != null){
			search = search.trim();	
		}

		// delete old picture
		String filePath = rootPath.concat("/YTP");
		File fDir = new File(filePath);
		if (fDir.exists()) {
			if (fDir.isDirectory()) {
				File[] children = fDir.listFiles();
				for (int i = 0; i < children.length; i++) {
					children[i].delete();
				}
			}
		}
		
		// reset data
		stopLoadData();
		listProcessItem = new ArrayList<MyYouTubeInfo>();
		listData = new ArrayList<MyYouTubeInfo>();
		adapterData = new AdapterYouTube(context, R.layout.item_youtube_list, listData, search, mainActivity);
		gridView.setAdapter(adapterData);
		
		totalView.setTotalSum(0);
		
		adapterData = null;
		
		this.search = search;
		
		countProcess = 0;
		totalResult = 0;
		curPage = -1;
		nextPageToken = "";
		prevPageToken = "";
		
		if(search == null || search.equals("") || search.equals(" ")){
			startPingServer();
			return;
		}
		
		countSwitch = 0;
		startLoadYouTube(search);
		
	}

	@Override
	public void OnSK90009() {
		MyLog.e(TAB, "OnSK90009");
		if(adapterData != null){
			adapterData.notifyDataSetChanged();
		}
	}

	@Override
	public void OnUpdateView() {
		MyLog.e(TAB, "OnUpdateView");
	}
	
	@Override
	public void OnClosePopupYouTube(int position) {		
		MyLog.e(TAB, "OnClosePopupYouTube - " + position);
		MyApplication.flagPopupYouTube = false;
		
		processAllClosePopup();
	}
	
	private void processAllClosePopup(){		
		MyLog.d(TAB, "processAllClosePopup start");
		MyApplication.flagPopupYouTube = false;
		if(gridView != null && listData != null && listData.size() > 0){
			for (int i = 0; i < gridView.getChildCount(); i++) {
				View v = gridView.getChildAt(i);
				if(v != null){
					ItemYouTube item = (ItemYouTube) v.findViewById(R.id.itemYouTube);
					if (item != null) {
						if(item.isFlagOpenPopup()){
							item.setFlagOpenPopup(false);
						}
					}
				}
			}
		}
		MyLog.d(TAB, "processAllClosePopup done");
	}
	
	// ----------------------------------------------------
	
//	private boolean flagFreshData = false;
	private final int PAGELOAD = 40;
	private final int PAGETRIGGER = 20;
	private ArrayList<MyYouTubeInfo> listData;
	private ArrayList<MyYouTubeInfo> listProcessItem;
	private int curPage = -1;
	private int totalResult = 0;
	private String nextPageToken = "";
	private String prevPageToken = "";

	private Timer timerLoadData = null;

	private void stopLoadData() {
		if (timerLoadData != null) {
			timerLoadData.cancel();
			timerLoadData = null;
		}
	}
	
	private Handler handlerUpdateData = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			MyLog.e(" ", " ");
			MyLog.e(" ", " ");
			MyLog.e(TAB, "handlerUpdateData");
			if (listData != null) {
				MyLog.e(" ", "listData size = " + listData.size());
				
				if(listData.size() == 0){
					layoutShowThongBao.setVisibility(View.VISIBLE);
					gridView.setVisibility(View.INVISIBLE);
					layoutGuide.setVisibility(View.INVISIBLE);
					layoutGuideWifi.setVisibility(View.INVISIBLE);
				} else {
					layoutShowThongBao.setVisibility(View.INVISIBLE);
					gridView.setVisibility(View.VISIBLE);
					layoutGuide.setVisibility(View.INVISIBLE);
					layoutGuideWifi.setVisibility(View.INVISIBLE);
				}
				
				updateData(0);				
			}

		}
	};
	
	private int countSwitch = 0;
	
	// TODO search in excel + first page real search
	private void startLoadYouTube(final String strSearch) {
		if (curPage != -1) {
			MyLog.e(TAB, "startLoadYouTube : khong phai lan load dau");
			return;
		}

		if (strSearch == null || strSearch.equals("") || strSearch.equals(" ")) {
			OnSearchMain(0, 0, "");
			return;
		}

		stopLoadData();
		timerLoadData = new Timer();
		timerLoadData.schedule(new TimerTask() {

			@Override
			public void run() {
				listData = new ArrayList<MyYouTubeInfo>();
				listProcessItem = new ArrayList<MyYouTubeInfo>();

				// REAL SEARCH ON YOUTUBE
				StringBuilder builder = new StringBuilder();
				HttpClient client = new DefaultHttpClient();

				String value = strSearch;
				switch (mainType) {
				case 1:
					value = getResources().getString(
							R.string.myyoutube_search_1)
							+ " "
							+ strSearch;
					break;				
				default:
					value = strSearch;
					break;
				}

				String encodedValue = "";
				try {
					encodedValue = URLEncoder.encode(value, "UTF-8");
				} catch (UnsupportedEncodingException e3) {
					e3.printStackTrace();
				}

				if (encodedValue.equals("")) {
					OnSearchMain(0, 0, "");
					return;
				}

				String url = "https://www.googleapis.com/youtube/v3/search?key="
						+ DeveloperKey.getAPIKey()
						+ "&part=snippet"
						+ "&q="
						+ encodedValue
						+ "&safeSearch=" + DeveloperKey.getSafeSearchType()
						+ "&type=video"
						+ "&maxResults="
						+ PAGELOAD;

				MyLog.e(TAB, "url search = " + url);

				try {
					URLEncoder.encode(url, "UTF-8");
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
					MyLog.i(TAB, "encode error");
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
						MyLog.i(TAB, "Failed to download file");
						DeveloperKey.switchAPIKey();
						countSwitch++;
						if(countSwitch > DeveloperKey.totalKeyBackUp){
							
						} else {
							startLoadYouTube(FragmentYouTube.this.search);
							return;
						}
						
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
					MyLog.i(TAB, "readYoutubeFeed exeption1");
				} catch (IOException e) {
					e.printStackTrace();
					MyLog.i(TAB, "readYoutubeFeed exeption2");
				}
				// MyLog.e(TAB, "response = " + builder.toString());

				// process JSON data
				try {
					JSONObject reader = new JSONObject(builder.toString());
					JSONArray jsonArray = reader.optJSONArray("items");

					String strNexPageToken = reader.optString("nextPageToken");
					String strPrevPageToken = reader.optString("prevPageToken");

					JSONObject jsonPageInfo = reader.optJSONObject("pageInfo");
					if (jsonPageInfo != null) {
						long longTotalResults = jsonPageInfo
								.optLong("totalResults");
						int intResultsPerPage = jsonPageInfo
								.optInt("resultsPerPage");

						// MyLog.e(" ", "totalResults = " + longTotalResults);
						
						Message msg = new Message();
						Bundle bd = new Bundle();
						bd.putLong("total", longTotalResults);
						msg.setData(bd);
						handlerUpdateTotal.sendMessage(msg);
						
						// MyLog.e(" ", "resultsPerPage = " + intResultsPerPage);
						// MyLog.e(" ", "nextPageToken = " + strNexPageToken);
						// MyLog.e(" ", "prevPageToken = " + strPrevPageToken);

						nextPageToken = strNexPageToken;
						prevPageToken = strPrevPageToken;
					}

					if (jsonArray.length() > 0) {
						curPage = 0;
					}
					
					if(jsonArray.length() == 0){
						Message msg = new Message();
						Bundle bd = new Bundle();
						bd.putLong("total", 0);
						msg.setData(bd);
						handlerUpdateTotal.sendMessage(msg);
					}

					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						JSONObject jsonSnippet = jsonObject
								.getJSONObject("snippet");

						String channelTitle = jsonSnippet.optString(
								"channelTitle").toString();
						String title = jsonSnippet.optString("title")
								.toString();

						JSONObject jsonID = jsonObject.optJSONObject("id");
						String strID = "";
						if (jsonID != null) {
							strID = jsonID.optString("videoId");
						}

						// MyLog.d(" ", "videoId = " + strID);
						// MyLog.i(" ", "title = " + title);
						// MyLog.i(" ", "channelTitle = " + channelTitle);

						listData.add(new MyYouTubeInfo(strID, title, "",
								channelTitle));
						listProcessItem.add(new MyYouTubeInfo(strID, title,
								"", channelTitle));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				handlerUpdateData.sendEmptyMessage(0);

				// find data - image, duration, ...
				readYoutubeData();

			}
		}, 1000);
	}
	
	private AdapterYouTube adapterData;
	
	private void updateData(int idx) {
		if (listData == null) {
			return;
		}

		if (adapterData == null) {
			MyLog.i(TAB, "updateData new data");
			adapterData = new AdapterYouTube(context, R.layout.item_youtube_list, listData, search, mainActivity);
			adapterData.setOnAdapterYouTubeListener(new OnAdapterYouTubeListener() {
				
				@Override
				public void onClickYouTube(MyYouTubeInfo info, int type, int position,
						float x, float y) {
					if(info != null){
						if(listener != null){
							listener.onClickYouTube(info, type, position, x, y);
						}
					}
				}

				@Override
				public void onClickXemTruoc(MyYouTubeInfo info) {
					if(info != null){
						if(listener != null){
							Song song = new Song();
							song.setPlayLink(info.getVideoId());	
							listener.onPlayYouTube(song);
						}
					}					
					
				}
			});
			adapterData.notifyDataSetChanged();
			gridView.setAdapter(adapterData);
		} else {
			if(idx == 0){
				adapterData.notifyDataSetChanged();
			}
			
			MyLog.i(TAB, "updateData normal");
			if (listData.size() <= 12) {
				adapterData.notifyDataSetChanged();
				return;
			}

			try {
				for (int i = 0; i < 12; i++) {
					View v = gridView.getChildAt(i);
					ItemYouTube item = (ItemYouTube) v.findViewById(R.id.itemYouTube);
					if (item != null) {
						MyYouTubeInfo tempInfo = null;
						try {
							tempInfo = listData.get(i);	
						} catch (Exception e) {
							
						}
						
						if (tempInfo != null && tempInfo.getDuration() != 0) {
							if (item.getItemInfo().getVideoId()
									.equals(tempInfo.getVideoId())) {

								if (item.isFlagFinishImage() == false) {
									item.clearImage();
									item.setItemInfo(tempInfo);
								}
							}
						}			
					}
				}	
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
	
	private long timeLoadNext = 0;
	private void loadNextPage() {
		if (curPage == -1) {
			MyLog.e(TAB, "loadNextPage : chua load lan dau");
			return;
		}

		if (nextPageToken.equals("")) {
			MyLog.e(TAB, "loadNextPage : khong thay next page");
			return;
		}

		final String strSearch = search;

		if (strSearch == null || strSearch.equals("") || strSearch.equals(" ")) {
			OnSearchMain(0, 0, "");
			return;
		}
		
		if(System.currentTimeMillis() - timeLoadNext < 3000){
			return;
		}
		
		timeLoadNext = System.currentTimeMillis();		

		stopLoadData();
		timerLoadData = new Timer();
		timerLoadData.schedule(new TimerTask() {

			@Override
			public void run() {
				// STEP 1 - REAL SEARCH ON YOUTUBE
				StringBuilder builder = new StringBuilder();
				HttpClient client = new DefaultHttpClient();

				String value = strSearch;
				switch (mainType) {
				case 1:
					value = getResources().getString(
							R.string.myyoutube_search_1)
							+ " "
							+ strSearch;
					break;
				default:
					value = strSearch;
					break;
				}

				String encodedValue = "";
				try {
					encodedValue = URLEncoder.encode(value, "UTF-8");
				} catch (UnsupportedEncodingException e3) {
					e3.printStackTrace();
				}

				if (encodedValue.equals("")) {
					OnSearchMain(0, 0, "");
					return;
				}

				String url = "https://www.googleapis.com/youtube/v3/search?key="
						+ DeveloperKey.getAPIKey()
						+ "&part=snippet"
						+ "&q="
						+ encodedValue
						+ "&safeSearch=" + DeveloperKey.getSafeSearchType()
						+ "&type=video"
						+ "&maxResults="
						+ PAGELOAD + "&pageToken=" + nextPageToken;

				MyLog.e(TAB, "url loadNext = " + url);

				try {
					URLEncoder.encode(url, "UTF-8");
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
					MyLog.i(TAB, "encode error");
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
						MyLog.i(TAB, "Failed to download file");
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
					MyLog.i(TAB, "readYoutubeFeed exeption1");
				} catch (IOException e) {
					e.printStackTrace();
					MyLog.i(TAB, "readYoutubeFeed exeption2");
				}
				// MyLog.e(TAB, "response = " + builder.toString());

				// process JSON data
				try {
					JSONObject reader = new JSONObject(builder.toString());
					JSONArray jsonArray = reader.optJSONArray("items");

					String strNexPageToken = reader.optString("nextPageToken");
					String strPrevPageToken = reader.optString("prevPageToken");

					JSONObject jsonPageInfo = reader.optJSONObject("pageInfo");
					if (jsonPageInfo != null) {
						long longTotalResults = jsonPageInfo
								.optLong("totalResults");
						int intResultsPerPage = jsonPageInfo
								.optInt("resultsPerPage");

						// MyLog.e(" ", "totalResults = " + longTotalResults);
						// MyLog.e(" ", "resultsPerPage = " +
						// intResultsPerPage);
						// MyLog.e(" ", "nextPageToken = " + strNexPageToken);
						// MyLog.e(" ", "prevPageToken = " + strPrevPageToken);

						nextPageToken = strNexPageToken;
						prevPageToken = strPrevPageToken;
					}

					if (jsonArray.length() > 0) {
						curPage++;
					}

					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						JSONObject jsonSnippet = jsonObject
								.getJSONObject("snippet");

						String channelTitle = jsonSnippet.optString(
								"channelTitle").toString();
						String title = jsonSnippet.optString("title")
								.toString();

						JSONObject jsonID = jsonObject.optJSONObject("id");
						String strID = "";
						if (jsonID != null) {
							strID = jsonID.optString("videoId");
						}

						// MyLog.d(" ", "videoId = " + strID);
						// MyLog.i(" ", "title = " + title);
						// MyLog.i(" ", "channelTitle = " + channelTitle);

						listData.add(new MyYouTubeInfo(strID, title, "",
								channelTitle));
						listProcessItem.add(new MyYouTubeInfo(strID, title,
								"", channelTitle));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				handlerUpdateData.sendEmptyMessage(0);

				// find data - image, duration, ...
				readYoutubeData();

			}
		}, 500);

	}
	
	private MyYouTubeInfo tempYoutubeInfo;
	
	private Timer timerReadYoutubeData = null;

	private void stopReadYoutubeData() {
		if (timerReadYoutubeData != null) {
			timerReadYoutubeData.cancel();
			timerReadYoutubeData = null;
		}
	}

	public void readYoutubeData() {

		if (listProcessItem == null || listProcessItem.size() == 0) {
			return;
		}
		
		stopReadYoutubeData();

		// TODO check size
//		MyLog.e(" ", " ");
//		MyLog.e(" ", " ");
//		MyLog.e(" ", "size = " + listProcessItem.size());

		tempYoutubeInfo = listProcessItem.get(0);
		final String vidID = tempYoutubeInfo.getVideoId();
		listProcessItem.remove(0);

		// already read data
		if (tempYoutubeInfo.getDuration() != 0) {
			MyLog.i(" ", "already read");
			
			readYoutubeData();
			return;
		}

		timerReadYoutubeData = new Timer();
		timerReadYoutubeData.schedule(new TimerTask() {

			@Override
			public void run() {


				int idxUpdate = 0;
				if (listData != null && listData.size() > 0) {
					if (listData.contains(tempYoutubeInfo)) {
						int idx = listData.indexOf(tempYoutubeInfo);
						if (idx != -1) {
							idxUpdate = idx;
						}
					}
				}

				long time = System.currentTimeMillis();
				StringBuilder builder = new StringBuilder();
				HttpClient client = new DefaultHttpClient();
				String url = "https://www.googleapis.com/youtube/v3/videos?key="
						+ DeveloperKey.getAPIKey()
						+ "&part=snippet,statistics,contentDetails&id=" + vidID;
				try {
					URLEncoder.encode(url, "UTF-8");
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
					MyLog.i(TAB, "encode error");
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
						MyLog.i(TAB, "Failed to download file");
					}
				} catch (ClientProtocolException e) {
					MyLog.i(TAB, "readYoutubeFeed exeption1");
					e.printStackTrace();
				} catch (IOException e) {
					MyLog.i(TAB, "readYoutubeFeed exeption2");
					e.printStackTrace();
				}
				// MyLog.e(TAG, "result = " + builder.toString());

				// process JSON data
				try {
					JSONObject reader = new JSONObject(builder.toString());
					JSONArray jsonArray = reader.optJSONArray("items");

					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);

						JSONObject jsonSnippet = jsonObject
								.getJSONObject("snippet");
						JSONObject jsonContentDetails = jsonObject
								.getJSONObject("contentDetails");
						JSONObject jsonStatistics = jsonObject
								.getJSONObject("statistics");

						// TODO uu tien medium 320x180
						String imageLink = "";
						JSONObject jsonThumbnails = jsonSnippet
								.getJSONObject("thumbnails");
						try {
							JSONObject jsonTemp = jsonThumbnails
									.getJSONObject("medium");
							imageLink = jsonTemp.getString("url");
						} catch (Exception e) {
							try {
								JSONObject jsonTemp = jsonThumbnails
										.getJSONObject("high");
								imageLink = jsonTemp.getString("url");
							} catch (Exception e1) {
								try {
									JSONObject jsonTemp = jsonThumbnails
											.getJSONObject("default");
									imageLink = jsonTemp.getString("url");
								} catch (Exception e2) {
									
								}
							}
						}

						String duration = "";
						long longDur = 0;
						try {
							duration = jsonContentDetails.optString("duration")
									.toString();
							longDur = getDuration(duration);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						String viewCount = "";
						long longViewCount = 0;
						String strViewCount = "";
						try {
							viewCount = jsonStatistics.optString("viewCount")
									.toString();
							longViewCount = Long.parseLong(viewCount);
							strViewCount = String.format("%,d", longViewCount);
							strViewCount = strViewCount.replaceAll(",", ".");
						} catch (Exception e) {
							e.printStackTrace();
						}

						//MyLog.e(" ", " ");
						//MyLog.d(" ", "imageLink = " + imageLink);
						//MyLog.d(" ", "strViewCount = " + strViewCount);
						//MyLog.d(" ", "duration = " + duration);
						//MyLog.d(" ", "longDur = " + longDur);

						tempYoutubeInfo.setImageLink(imageLink);
						tempYoutubeInfo.setDuration(longDur);
						tempYoutubeInfo.setViewerCount(longViewCount);
						tempYoutubeInfo.setStrViewCount(strViewCount);

						int seconds = (int) (longDur / 1000) % 60;
						int minutes = (int) ((longDur / (1000 * 60)) % 60);
						int hours = (int) ((longDur / (1000 * 60 * 60)) % 24);

						String strDur = String.format("%02d", seconds) + "";
						if (minutes > 0) {
							strDur = String.format("%02d", minutes) + ":"
									+ strDur;
						}
						if (hours > 0) {
							strDur = String.format("%02d", hours) + ":"
									+ strDur;
						}
						//MyLog.d(" ", "strDur = " + strDur);
						tempYoutubeInfo.setStrDur(strDur);

						if (listData != null && listData.size() > 0) {
							if (listData.contains(tempYoutubeInfo)) {
								int idx = listData.indexOf(tempYoutubeInfo);
								if (idx != -1) {
									listData.set(idx, tempYoutubeInfo);
									// TODO found data
//									MyLog.d(" ", listData.get(idx).toString());

									String filePath = rootPath.concat("/YTP/"
											+ idx);
									downloadFile(imageLink, filePath);
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				// MyLog.e(" ", "time process : " + (System.currentTimeMillis()
				// -
				// time));
				handlerProcessItem.sendEmptyMessage(idxUpdate);

				if (listProcessItem == null || listProcessItem.size() == 0) {
//					if(countProcess == 0){
//						countProcess = 1;
//						listProcessItem = listData;
//						readYoutubeData();
//					}
					return;
				}

				readYoutubeData();

			
			}
		}, 10);
		
	}

	private int countProcess = 0;
	
	private Handler handlerProcessItem = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (listData == null || listData.size() == 0) {
				return;
			}

			updateData(msg.what);
		}
	};

	private void downloadFile(String url, String dest_file_path) {
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
				fos.write(buffer, 0, bytes_read);
			}

			fis.close();
			fos.flush();
			fos.close();
		} catch (Exception e) {
			File dest_file = new File(dest_file_path);
			if (dest_file.exists()) {
				dest_file.delete();
			}
			return;
		}
	}

	private long getDuration(String strDur) {
		String time = strDur.substring(2);
		long duration = 0L;
		Object[][] indexs = new Object[][] { { "H", 3600 }, { "M", 60 },
				{ "S", 1 } };
		for (int i = 0; i < indexs.length; i++) {
			int index = time.indexOf((String) indexs[i][0]);
			if (index != -1) {
				String value = time.substring(0, index);
				duration += Integer.parseInt(value)
						* Integer.parseInt(indexs[i][1].toString()) * 1000;
				time = time.substring(value.length() + 1);
			}
		}
		return duration;
	}
		
	private OnScrollListener onScrollListener = new OnScrollListener() {
		private int mLastFirstVisibleItem = 0;

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			case 1: // SCROLL_STATE_TOUCH_SCROLL
				if(listener != null){
					listener.OnHideKeyBoard();
				}
				processAllClosePopup();
				break;
			}
		}

		@Override
		public void onScroll(AbsListView view, int first, int visible, int total) {
			if (listData == null || listData.size() == 0) {
				if(totalView != null){
					totalView.setVisibility(View.VISIBLE);
					totalView.setDisplay(true);
					totalView.setTotalSum(0);	
				}
				return;
			} else {
				int tempRowTrigger = PAGETRIGGER;
				
				if(total - tempRowTrigger > 0){
					if((first + visible) > (total - tempRowTrigger)){
						loadNextPage();
					}	
				}
				
			}			
			
			try {
				if (mLastFirstVisibleItem < first) {
					totalView.setDisplay(false);
					totalView.setVisibility(View.GONE);
				}
				if (mLastFirstVisibleItem > first) {
					totalView.setVisibility(View.VISIBLE);
					totalView.setDisplay(true);
				}
				mLastFirstVisibleItem = first;			

				// --------------//
			} catch (Exception ex) {
				ex.printStackTrace();
				return;
			}
		}
	};
	
	private Handler handlerUpdateTotal = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if(msg != null){
				long total = msg.getData().getLong("total");
				totalView.setTotalSum(total);
			}
		}
	};	
	
	class PingServerTask extends AsyncTask<String, Void, Integer> {
		
		protected Integer doInBackground(String... urls) {
			try {
				boolean flag = isURLReachable(context);
				if (flag == false) {
					return 1;
				}
				
				return 0;
			} catch (Exception e) {
				return 0;
			}
		}

		protected void onPostExecute(Integer result) {
			if(result == 1){
				layoutGuideWifi.setVisibility(View.VISIBLE);
				layoutGuide.setVisibility(View.INVISIBLE);
				layoutShowThongBao.setVisibility(View.INVISIBLE);
				gridView.setVisibility(View.INVISIBLE);
			} else {
				layoutGuide.setVisibility(View.VISIBLE);
				layoutGuideWifi.setVisibility(View.INVISIBLE);
				layoutShowThongBao.setVisibility(View.INVISIBLE);
				gridView.setVisibility(View.INVISIBLE);
			}
		}
	}
	
	private boolean isURLReachable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			try {
				URL url = new URL(
						"https://www.google.com");
				HttpURLConnection urlc = (HttpURLConnection) url
						.openConnection();
				urlc.setConnectTimeout(1 * 1000); // 3 s.
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

	@Override
	public void OnUpdateCommad(ServerStatus status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnUpdateImage() {
		// TODO Auto-generated method stub
		
	}
	
}
