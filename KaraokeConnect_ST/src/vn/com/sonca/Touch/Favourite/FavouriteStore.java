package vn.com.sonca.Touch.Favourite;

import java.util.ArrayList;

import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.database.DBInterface;
import vn.com.sonca.params.Song;
import android.content.Context;
import android.content.SharedPreferences;

public class FavouriteStore {
	protected static final String PREF_FILE = "MyFavFile";
	private static FavouriteStore instance;

	private static SharedPreferences settings;

	public FavouriteStore(Context context) {
		settings = context
				.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
		// clearStore();
	}

	public static FavouriteStore getInstance(Context context) {
		if (instance == null) {
			instance = new FavouriteStore(context);
		}
		return instance;
	}

	public void clearStore() {
		SharedPreferences.Editor editor = settings.edit();
		editor.clear();
		editor.commit();
	}

	public ArrayList<Song> getFavListInStore() {
		ArrayList<Song> list = new ArrayList<Song>();

		int listSize = settings.getInt("Fav_size", 0);
		if (listSize == 0) {
			return list;
		}

		for (int i = 0; i < listSize; i++) {
			int tempID = Integer.parseInt(settings.getString("Fav_ID_" + i,
					"-57"));
			int tempTypeABC = settings.getInt("Fav_ABC_" + i, 0);
			Song temp = new Song(tempID, tempTypeABC);
			list.add(temp);
		}

		return list;
	}

	public void setFavSongIntoStore(boolean onOff, String ipSong, int typeABC) {
		Song setSong = new Song(Integer.parseInt(ipSong), typeABC);
		if (onOff) { // ON YEU THICH
			if (checkInSideStore(setSong)) { // DA CO
				// do nothing
			} else { // CHUA CO
				// add vao store
				ArrayList<Song> listFav = getFavListInStore();
				listFav.add(setSong);
				setNewListFav(listFav);
			}
		} else { // OFF YEU THICH
			if (checkInSideStore(setSong)) { // DA CO
				// remove song
				ArrayList<Song> listFav = getFavListInStore();
				listFav.remove(setSong);
				setNewListFav(listFav);
			} else { // CHUA CO
				// do nothing
			}
		}
	}

	// //////////////////////////////
	private void setNewListFav(ArrayList<Song> newList) {
		SharedPreferences.Editor editor = settings.edit();
		editor.clear();
		editor.commit();

		editor.putInt("Fav_size", newList.size());

		for (int i = 0; i < newList.size(); i++) {
			editor.putString("Fav_ID_" + i, newList.get(i).getIndex5() + "");
			editor.putInt("Fav_ABC_" + i, newList.get(i).getTypeABC());
		}
		editor.commit();
	}

	private boolean checkInSideStore(Song song) {
		boolean flagCheck = false;
		ArrayList<Song> list = getFavListInStore();
		
		if (list.size() == 0) {
			// MyLog.e("checkInSideStore", "false list size");
			return false;
		}
		
		for (Song song2 : list) {
			// MyLog.e("", song2.getId() + " -- " + song2.getIndex5() + " -- " + song2.getTypeABC());
			if(song2.getIndex5() == song.getIndex5() && song2.getTypeABC() == song.getTypeABC()){
				flagCheck = true;
				break;
			}
		}
		
		// MyLog.e("checkInSideStore", flagCheck + "");
		return flagCheck;
	}
}
