package vn.com.sonca.Touch.touchcontrol;

import java.util.ArrayList;

import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.database.DBInterface;
import vn.com.sonca.params.Song;
import vn.com.sonca.zzzzz.MyApplication;
import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

public abstract class TouchFragmentBase extends Fragment {
	
	
	private String TAB = "FragmentBase";
	protected ArrayList<Song> listSongs = null;
	private TouchMainActivity mainActivity;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mainActivity = (TouchMainActivity) activity;
		} catch (Exception ex) {}
	}
	
	protected abstract void UpdateAdapter();

	protected ArrayList<Song> playlist = null; 
	protected void getPlayList(){
		if(playlist == null)
			playlist = ((MyApplication)mainActivity.getApplication()).getListActive();
//		MyLog.e(TAB, "playlist : " + playlist.size());
	}

	protected ArrayList<Song> favourite = null;
	protected void getFavourite(){
		if(favourite == null)
			favourite = DBInterface.DBGetFavouriteSongList(0, 0, getActivity().getApplicationContext());		
//		MyLog.e(TAB, "favourite : " + favourite.size());
	}
	
	protected void UpdatePlayListIntoView(ArrayList<Song> arrayList){
		if(playlist != null){
			Song[] songs = new Song[arrayList.size()];
			arrayList.toArray(songs);
			for (int i = 0; i < songs.length; i++) {
				boolean b = playlist.contains(arrayList.get(i));
				songs[i].setActive(b);
			}
		}
	}
	
	protected void UpdateFavouriteIntoView(ArrayList<Song> arrayList){
		if(favourite != null){
			Song[] songs = new Song[arrayList.size()];
			arrayList.toArray(songs);
			for (int i = 0; i < songs.length; i++) {
				boolean b = favourite.contains(arrayList.get(i));
				songs[i].setFavourite(b);
			}
		}
	}

	private LoadPlayListFromServer fromserver = null;
	private class LoadPlayListFromServer extends AsyncTask<Void, Void, Integer>{

		public LoadPlayListFromServer() {}

		@Override
		protected Integer doInBackground(Void... params) {
			playlist = ((MyApplication)mainActivity.getApplication()).getListActive();
			if(listSongs != null){
				UpdatePlayListIntoView(listSongs);
			}
			return 1;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			UpdateAdapter();
		}
	}
	
	protected void LoadPlayList(){
		if(fromserver != null){
			fromserver.cancel(true);
			fromserver = null;
		}
		fromserver = new LoadPlayListFromServer();
		fromserver.execute();
	}

//////////////////////////////////////////////////////////////////////////
	
	private String searchData = "";
	
	public void setBackData(String searchData){
		this.searchData = searchData;
	}
	
	public String getBackData(){
		return searchData;
	}
	
}
