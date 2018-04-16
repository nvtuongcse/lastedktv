package vn.com.sonca.Touch.Favourite;

import java.util.ArrayList;

import vn.com.sonca.Touch.CustomView.TouchGlowView;
import vn.com.sonca.Touch.CustomView.TouchMyGroupFavourite;
import vn.com.sonca.Touch.CustomView.TouchMyGroupSong;
import vn.com.sonca.Touch.CustomView.TouchMyGroupFavourite.OnGroupFavouriteListener;
import vn.com.sonca.Touch.CustomView.TouchMyGroupSong.OnMyGroupSongListener;
import vn.com.sonca.Touch.Listener.TouchIAdapter;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import vn.com.sonca.database.DBInterface;
import vn.com.sonca.params.Song;
import vn.com.sonca.zktv.main.KTVMainActivity;
import vn.com.sonca.zzzzz.MyApplication;
import vn.com.hanhphuc.karremote.R;

public class TouchAdapterFavourite extends ArrayAdapter<Song>{
	
	private Context context;
	private TouchIAdapter listener;
	private Typeface typeface;
	private ArrayList<Song> arrayList;
		//-----------//
	private Drawable drawSinger,drawUser;
	private Drawable drawActive,drawInActive,drawHover;
	private Drawable drawRemix, drawMidi, drawMV, drawMVVid;
	private Drawable drawFavourite, drawFavouriteNO;
	
	public void setOnAdapterListener(TouchIAdapter listener){
		this.listener = listener;
	}

	public TouchAdapterFavourite(Context context, int resource, ArrayList<Song> arrayList , TouchMainActivity mainActivity) {
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
		this.context = context;
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
			myGroupSong.setIdSinger(song.getSingerId());
			myGroupSong.setNameSinger(song.getSinger().getName());
				//----------------//
			myGroupSong.setIdMusician(song.getMusicianId());
			myGroupSong.setNameMusician(song.getMusician().getName());
				//----------------//
			myGroupSong.setIdSong(String.valueOf(song.getId()));
			myGroupSong.setContentView(position, song);
			if(MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				myGroupSong.setSongName(song.getName(), bool, song.getSpannable());
				myGroupSong.setDrawable(drawActive, drawInActive , drawHover, drawFavourite,
						drawFavouriteNO, drawSinger , drawRemix , drawMidi , drawMV, drawUser, drawMVVid);
			}else if(MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT){
				myGroupSong.setSongName(song.getName(), bool, song.getSpannable());
			
			}
			myGroupSong.setOnMyGroupSongListener(new OnMyGroupSongListener() {
				@Override public void OnFavourity(boolean bool, Song song) {
					if(bool == false){
						if(listener != null){
							listener.OnItemFavourite(position, song);
						}
					}
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
