package vn.com.sonca.Touch.SongType;

import java.util.ArrayList;
import java.util.List;

import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.Language.LanguageStore;
import vn.com.sonca.Touch.Listener.TouchIBaseFragment;
import vn.com.sonca.Touch.touchcontrol.TouchFragmentBase;
import vn.com.sonca.Touch.touchcontrol.TouchItemBack;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity.OnMainListener;
import vn.com.sonca.database.DBInstance.SearchMode;
import vn.com.sonca.database.DBInstance.SearchType;
import vn.com.sonca.database.DBInterface;
import vn.com.sonca.database.DbHelper;
import vn.com.sonca.params.ServerStatus;
import vn.com.sonca.params.Song;
import vn.com.sonca.params.SongType;
import vn.com.sonca.zzzzz.MyApplication;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.utils.AppConfig.MEDIA_TYPE;
import vn.com.sonca.utils.AppSettings;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;

public class TouchFragmentSongType extends TouchFragmentBase implements OnMainListener{
	
	private List<SongType> songTypeList;
	private TouchSongTypeAdapter adapter;
	private TouchIBaseFragment listener;
	private GridView listView;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			listener = (TouchIBaseFragment) activity;
		} catch (Exception ex) {}
	}
	

	private OnNumberSongTypeListener listenerNumber;
	public interface OnNumberSongTypeListener {
		public void OnNumberListener(int sum);
	}
	
	public void setOnNumberSongTypeListener(OnNumberSongTypeListener listener){
		this.listenerNumber = listener;
	}
	
	
	private Context context;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.touch_fragment_songtype, container, false);
		context = getActivity().getApplicationContext();
		if(listener != null){
			listener.OnNameSearch("", "");
		}
		listView = (GridView) view.findViewById(R.id.songtype_listview);		
//		AppSettings setting = AppSettings.getInstance(getActivity().getApplicationContext());
//		if(setting.loadServerLastUpdate() != 0) { 
//		if(setting.isUpdated()) {
			songTypeList = new ArrayList<SongType>();
			
			new LoadSongTypeFromDatabase("", 0, 0).execute();		
//		}
        
		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		if(listenerNumber != null){
			listenerNumber.OnNumberListener(songTypeList.size());
		}
	}
	
	
	@Override
	protected void UpdateAdapter() {
		
	}

	@Override
	public void OnLoadSucessful() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnUpdateImage() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnUpdateCommad(ServerStatus status) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnSearchMain(int state1, int state2, String search) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnSK90009() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnUpdateView() {
		MyLog.e("Fragment SongType", "OnUpdateView()");
		if(adapter != null){
			adapter.notifyDataSetChanged();
		}
	}
	
	// -----------------------------------
	private class LoadSongTypeFromDatabase extends
			AsyncTask<Void, Integer, Integer> {

		private String where;
		private int offset;
		private int sum;

		public LoadSongTypeFromDatabase(String where, int offset, int sum) {
			this.where = where;
			this.offset = offset;
			this.sum = sum;
		}

		@Override
		protected Integer doInBackground(Void... params) {
			try {
				int count = 0;

				if (MyApplication.mSongTypeList != null
						&& MyApplication.mSongTypeList.size() > 0) {
					songTypeList = MyApplication.mSongTypeList;
					return 1;
				}

				songTypeList = DBInterface.DBSearchSongType("",
						SearchMode.MODE_FULL, 0, 0, getActivity());
				for (SongType temp : songTypeList) {
					if (temp.getID() == 0) {
						songTypeList.remove(temp);
						break;
					}
				}

				songTypeList.add(0, new SongType(DbHelper.SongType_NewSongClub,getResources().getString(R.string.clb_new_songs)));
				songTypeList.add(0, new SongType(DbHelper.SongType_CoupleSinger, getResources().getString(R.string.type_songca)));	
				if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
					songTypeList.add(0, new SongType(DbHelper.SongType_Youtube, getResources().getString(R.string.newSongType_3)));
				}
				if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && (MyApplication.flagSmartK_801 == false && MyApplication.flagSmartK_CB == false)
						&& MyApplication.flagAllowSearchOnline){
					songTypeList.add(0, new SongType(DbHelper.SongType_Online, getResources().getString(R.string.newSongType_2)));
				}
				songTypeList.add(0, new SongType(DbHelper.SongType_LiveSong,getResources().getString(R.string.type_livesong)));
				songTypeList.add(0, new SongType(DbHelper.SongType_Remix,"Remix"));
				songTypeList.add(0, new SongType(DbHelper.SongType_HotSong,getResources().getString(R.string.type_hotsong_2)));
				songTypeList.add(0, new SongType(DbHelper.SongType_NewVol,getResources().getString(R.string.type_newvol_2)));
				songTypeList.add(0, new SongType(DbHelper.SongType_UpdateSong,getResources().getString(R.string.type_updatesong)));

				songTypeList.add(new SongType(DbHelper.SongType_China,getResources().getString(R.string.newSongType_1)));
								
				publishProgress(count);

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
								|| MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
								|| MyApplication.intSvrModel == MyApplication.SONCA_KBX9)){
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

					// MyLog.e("FragmentSongType", "typeID = " +
					// songTypeList.get(i).getID() + " -- total = " +
					// tempTotal);

					count++;
					publishProgress(count);
				}

				return 1;
			} catch (Exception e) {
				return 0;
			}
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);

			// int intValue = values[0];
			// MyLog.e("FragmentSongType", "onProgressUpdate -- " + intValue);

			updateMyAdapter(values[0]);

		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			if (result == 1) {
				updateMyAdapter(songTypeList.size() - 1);
			}
		}
	}

	public void updateMyAdapter(int uPositon) {
		MyLog.e("FragmentSongType", "updateMyAdapter -------------------- ");
		if (adapter == null) {
			adapter = new TouchSongTypeAdapter(getActivity(), songTypeList);
			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					boolean specialYoutube = false;
					try {
						if(songTypeList.get(position).getID() == DbHelper.SongType_Youtube){
							int valueYouTube = MyApplication.getCommandMediumYouTube();
							if(valueYouTube != 2){
								specialYoutube = true;
							}							
						}
					} catch (Exception e) {
						
					}
					
					if (songTypeList.get(position).getCountTotal() == 0 && !specialYoutube) {						
						return;
					}
					
					if (listener != null) {
						if(!songTypeList.get(position).getName().equals("Remix")){
							((MyApplication)context).addListBack(
									new TouchItemBack(TouchMainActivity.SONGTYPE, "", -1));
						}
						listener.onClickItem(null, String.valueOf(songTypeList.get(position).getID()), 
								TouchMainActivity.SONGTYPE , "", -1, 0 , 0);
					}
				}
			});
			listView.setAdapter(adapter);

			if (listenerNumber != null) {
				listenerNumber.OnNumberListener(songTypeList.size());
			}

		} else {
			adapter.notifyDataSetChanged();
		}

		MyApplication.mSongTypeList = songTypeList;
	}

	@Override
	public void OnClosePopupYouTube(int position) {
		// TODO Auto-generated method stub
		
	}
	
	private int countTotalYoutubeExcel(){
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
//						MyLog.d("remove bot song trong youtube", song.getName() + " ");
					} else {
						newList.add(song);
					}
				}
				
				songYouTube = newList;
			}
		}
		
		return songYouTube.size();
	}
	
}