package vn.com.sonca.Touch.PlayList;

import java.util.ArrayList;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.CustomView.TouchMyGroupPlaylist;
import vn.com.sonca.Touch.CustomView.TouchMyGroupPlaylist.OnGroupPlaylistListener;
import vn.com.sonca.Touch.Favourite.FavouriteStore;
import vn.com.sonca.Touch.Listener.TouchIAdapter;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.database.DBInterface;
import vn.com.sonca.params.Song;
import vn.com.hanhphuc.karremote.R;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class TouchAdapterPlayList extends ArrayAdapter<Song> {
	
	private String TAB = "AdapterPlayList";
	
	private Context context;
	private TouchIAdapter listener;
	private Typeface typeface;
	private TouchMainActivity mainActivity;
	private ArrayList<Song> arrayList;

	public void setOnAdapterListener(TouchIAdapter listener){
		this.listener = listener;
	}
	
	public TouchAdapterPlayList(Context context, int resource, ArrayList<Song> arrayList , TouchMainActivity mainActivity) {
		super(context, resource, arrayList);
		typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC);
		this.context = context;
		this.arrayList = arrayList;
		this.mainActivity = mainActivity;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		TouchMyGroupPlaylist myGroupPlaylist = null;
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.touch_item_playlist_list, null);
			myGroupPlaylist = (TouchMyGroupPlaylist) convertView.findViewById(R.id.myGroupPlaylist);
			convertView.setTag(R.id.myGroupPlaylist , myGroupPlaylist);
		}else{
			myGroupPlaylist = (TouchMyGroupPlaylist)convertView.getTag(R.id.myGroupPlaylist);
		}
		if(myGroupPlaylist != null){
			final Song song = arrayList.get(position);
			myGroupPlaylist.setTypeface(typeface);
			myGroupPlaylist.setIdSong(String.valueOf(song.getId()));
				//----------------//
			myGroupPlaylist.setIdSinger(song.getSingerId());
			myGroupPlaylist.setNameSinger(song.getSinger().getName());
				//----------------//
			myGroupPlaylist.setIdMusician(song.getMusicianId());
			myGroupPlaylist.setNameMusician(song.getMusician().getName());
				//----------------//
			myGroupPlaylist.setContentView(position + 1, song);
			myGroupPlaylist.setOnGroupPlaylistListener(new OnGroupPlaylistListener() {
				@Override public void OnFavourity(boolean bool, Song s) {
					arrayList.get(position).setFavourite(bool);
					
					FavouriteStore favStore = FavouriteStore.getInstance(mainActivity.getApplicationContext());
					favStore.setFavSongIntoStore(bool, String.valueOf(song.getId()), song.getTypeABC());
					
					DBInterface.DBSetFavouriteSong(String.valueOf(song.getId()), 
							String.valueOf(song.getTypeABC()), bool, context);
					if(listener != null){
						listener.OnItemFavourite(position, arrayList.get(position));
					}
				}
				
				@Override
				public void OnFristRes(boolean bool, Song s, int position, float x, float y) {
					if(listener != null){
						listener.OnFirstClick(song , position - 1, x, y);
					}
				}
	
				@Override public void OnActive(boolean bool, Song s , String ipSong, int position, float x, float y) {
					if(listener != null){
						listener.OnFirstClick(song, position - 1, x, y);
					}
				}
	
				@Override
				public void OnDelete(Song s , int position) {
					if(listener != null){
//						MyLog.e(TAB, "OnDelete - " + song.getId());
						listener.onDeleteSong(song, position - 1);
					}
				}
	
				@Override
				public void OnSingerLink(boolean bool, String name, int[] idSinger) {
					if(listener != null){
						listener.OnSingerLink(bool, name, idSinger);
					}
				}
				
				@Override
				public void OnShowLyric(int position, String idSong) {
					if(listener != null){
						listener.OnShowLyric(position, song);
					}
				}

				@Override
				public void OnDownYouTube(Song song) {
					if(listener != null){
						listener.onDownYouTube(song);
					}
				}
				
				@Override
				public void onPlayYouTube(Song song) {
					// TODO Auto-generated method stub
					if(listener != null){
						listener.onPlayYouTube(song);
					}
				}

			});
		}
		return convertView;
	}

}
