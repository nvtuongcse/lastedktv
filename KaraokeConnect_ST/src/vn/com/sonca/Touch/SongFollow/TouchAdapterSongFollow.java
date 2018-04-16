package vn.com.sonca.Touch.SongFollow;

import java.util.ArrayList;

import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.CustomView.TouchGlowView;
import vn.com.sonca.Touch.CustomView.TouchMyGroupSong;
import vn.com.sonca.Touch.CustomView.TouchMyGroupSong.OnMyGroupSongListener;
import vn.com.sonca.Touch.Favourite.FavouriteStore;
import vn.com.sonca.Touch.Listener.TouchIAdapter;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.database.DBInterface;
import vn.com.sonca.params.Song;
import vn.com.sonca.zzzzz.MyApplication;
import vn.com.hanhphuc.karremote.R;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class TouchAdapterSongFollow extends ArrayAdapter<Song> {
	
	private String TAB = "AdapterSongFollow";
	private TouchMainActivity mainActivity;
	private ArrayList<Song> arrayList;
	private Typeface typeface;
	private TouchIAdapter listener;
	private Context context;
	private String search;
		//--------//
	private Drawable drawSinger,drawUser;
	private Drawable drawActive,drawInActive,drawHover;
	private Drawable drawRemix, drawMidi, drawMV, drawMVVid;
	private Drawable drawFavourite, drawFavouriteNO;
	
	public void setOnAdapterListener(TouchIAdapter listener){
		this.listener = listener;
	}

	public TouchAdapterSongFollow(Context context, int resource, ArrayList<Song> arrayList, 
			String search, TouchMainActivity mainActivity) {
		super(context, resource, arrayList);
		drawActive = context.getResources().getDrawable(R.drawable.ydark_image_1st_active);
		drawInActive = context.getResources().getDrawable(R.drawable.ydark_image_1st_inactive);
		drawHover = context.getResources().getDrawable(R.drawable.ydark_image_1st_cham);
		drawFavourite = context.getResources().getDrawable(R.drawable.touch_image_favourite_active_45x45);
		drawFavouriteNO = context.getResources().getDrawable(R.drawable.touch_image_favourite_45x45);
		drawSinger = context.getResources().getDrawable(R.drawable.touch_song_vocal_48x48);
		drawRemix = context.getResources().getDrawable(R.drawable.touch_image_remix_62x35);
		drawMidi = context.getResources().getDrawable(R.drawable.new_midi);
		drawMV = context.getResources().getDrawable(R.drawable.icon_ktv_1note);
		drawMVVid = context.getResources().getDrawable(R.drawable.icon_ktv_video);
		drawUser = context.getResources().getDrawable(R.drawable.touch_icon_user);
		
		typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC);
		this.mainActivity = mainActivity;
		this.context = context;
		this.search = search;
		this.arrayList = arrayList;
	}
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		TouchGlowView glowView = null;
		TouchMyGroupSong myGroupSong = null;
		if(convertView == null){
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.touch_item_song_list, null);
			myGroupSong = (TouchMyGroupSong)convertView.findViewById(R.id.myGroupSong);
			glowView = (TouchGlowView)convertView.findViewById(R.id.GlowView);
			convertView.setTag(R.id.myGroupSong , myGroupSong);
			convertView.setTag(R.id.GlowView, glowView);
		}else{
			myGroupSong = (TouchMyGroupSong)convertView.getTag(R.id.myGroupSong);
			glowView = (TouchGlowView)convertView.getTag(R.id.GlowView);
		}
		if(myGroupSong != null){
			if(position >= arrayList.size()){
				return convertView;
			}
			final Song song = arrayList.get(position);
			int ordinarily = ((MyApplication)context.getApplicationContext()).CheckSongInPlayList(song);			
			boolean bool = false;
			if (ordinarily != -1) {
				bool = true;
			}
			glowView.setActive(bool);
				//----------------//
			myGroupSong.setTypeface(typeface);
			myGroupSong.setOrdinarilyPlaylist(ordinarily + 1);
			myGroupSong.setIdSinger(song.getMusicianId());
			myGroupSong.setNameSinger(song.getSinger().getName());
				//----------------//
			myGroupSong.setIdSong(String.valueOf(song.getId()));
			if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				myGroupSong.setSongName(song.getName(), bool, song.getSpannable());
				myGroupSong.setDrawable(drawActive, drawInActive , drawHover, drawFavourite,
						drawFavouriteNO, drawSinger , drawRemix , drawMidi , drawMV, drawUser, drawMVVid);
			}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
				myGroupSong.setSongName(song.getName(), bool, song.getSpannable());
			}
			myGroupSong.setContentView(position, song);
			myGroupSong.setOnMyGroupSongListener(new OnMyGroupSongListener() {
				@Override public void OnFavourity(boolean bool, Song song) {
//					MyLog.e(TAB, "OnFavourity - " + song.getId());
					arrayList.get(position).setFavourite(bool);
					
					FavouriteStore favStore = FavouriteStore.getInstance(mainActivity.getApplicationContext());
					favStore.setFavSongIntoStore(bool, String.valueOf(song.getId()), song.getTypeABC());
					
					DBInterface.DBSetFavouriteSong(String.valueOf(song.getId()), String.valueOf(song.getTypeABC()), bool, context);
				}
				@Override
				public void OnActive(boolean bool , Song song , String ipSong , float x , float y) {
					// song.setActive(bool);
					// ((MyApplication)mainActivity.getApplication()).addSongIntoPlayList(song);
					if(listener != null){
						listener.OnItemActive(song, ipSong , x , y);
					}
				}
				@Override 
				public void OnFristRes(boolean bool, Song song, int position, float x , float y) {
					if(listener != null){
						listener.OnFirstClick(song , position, x , y);
					}
				}
				@Override
				public void OnSingerLink(boolean bool, String name, int[] idSinger) {
					if(listener != null){
						listener.OnSingerLink(bool, name, idSinger);
					}
				}
				@Override
				public void OnLockNotify() {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void OnUnLockNotify() {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void OnShowLyric(int position, String idSong) {
					if(listener != null){
						listener.OnShowLyric(position, song);
					}
				}
				@Override
				public void onPlayYouTube(Song song) {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void onDownYouTube(Song song) {
					// TODO Auto-generated method stub
					
				}
			});
		}
		return convertView;
	}
}
