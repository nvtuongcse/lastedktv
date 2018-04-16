package vn.com.sonca.sk90xxOnline;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.CustomView.TouchGlowView;
import vn.com.sonca.params.Song;
import vn.com.sonca.sk90xxOnline.ViewDelSong.OnViewDelListener;
import vn.com.sonca.zzzzz.MyApplication;

public class AdapterDelSong  extends ArrayAdapter<Song> {
	
	private String TAB = "AdapterSong";
	private ArrayList<Song> arrayList;
	private Typeface typeface;
	private Context context;
	private String search = "";
	private int language;
		//-----------//
	private Drawable drawSinger,drawUser;
	private Drawable drawRemix, drawMidi, drawMV, drawMVVid;
	private Drawable drawFavourite, drawFavouriteNO;
	private Drawable drawActive,drawInActive,drawHover;
	
	public void setOnDelSongListener(OnDelSongListener listener){
		this.listener = listener;
	}
	
	private OnDelSongListener listener;
	public interface OnDelSongListener {
		public void OnClickDel(Song song);
	}

	public AdapterDelSong(Context context, int resource, ArrayList<Song> arrayList, 
			String search) {
		super(context, resource, arrayList);
		drawActive = context.getResources().getDrawable(R.drawable.check_thembai);
		drawInActive = context.getResources().getDrawable(R.drawable.kc_check_inactive);
		drawHover = context.getResources().getDrawable(R.drawable.ydark_image_1st_cham);
		drawFavourite = context.getResources().getDrawable(R.drawable.touch_image_favourite_active_45x45);
		drawFavouriteNO = context.getResources().getDrawable(R.drawable.touch_image_favourite_45x45);
		drawSinger = context.getResources().getDrawable(R.drawable.touch_song_vocal_48x48);
		drawRemix = context.getResources().getDrawable(R.drawable.touch_image_remix_62x35);
		drawMidi = context.getResources().getDrawable(R.drawable.new_midi);
		drawUser = context.getResources().getDrawable(R.drawable.touch_icon_user);
		drawMV = context.getResources().getDrawable(R.drawable.icon_ktv_1note);
		drawMVVid = context.getResources().getDrawable(R.drawable.icon_ktv_video);
		
		typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC);
		this.context = context;
		this.arrayList = arrayList;
		this.search = search;
	}
	
	private GlowViewOnline glowView = null;
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewDelSong myGroupSong = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.dialog_addsongdata_sk90xx_del, null);
			myGroupSong = (ViewDelSong) convertView.findViewById(R.id.myGroupSong);
			glowView = (GlowViewOnline) convertView.findViewById(R.id.GlowView);
			convertView.setTag(R.id.myGroupSong, myGroupSong);
			convertView.setTag(R.id.GlowView, glowView);
		} else {
			myGroupSong = (ViewDelSong) convertView.getTag(R.id.myGroupSong);
			glowView = (GlowViewOnline) convertView.getTag(R.id.GlowView);
		}
		if (myGroupSong != null) {
			if (position >= arrayList.size()) {
				MyLog.e(" ", " ");
				MyLog.e(TAB, "getView ERROR");
				MyLog.e(" ", " ");
				return convertView;
			}
			final Song song = arrayList.get(position);
			int ordinarily = ((MyApplication) context.getApplicationContext()).CheckSongInPlayList(song);
			boolean bool = false;
			if (ordinarily != -1) {
				bool = true;
			}
			glowView.setActive(false);
			// ----------------//
			myGroupSong.setTypeface(typeface);
			myGroupSong.setOrdinarilyPlaylist(ordinarily + 1);
			myGroupSong.setIdSinger(song.getSingerId());
			myGroupSong.setNameSinger(song.getSinger().getName());
			// ----------------//
			myGroupSong.setIdMusician(song.getMusicianId());
			myGroupSong.setNameMusician(song.getMusician().getName());
			// ----------------//
			myGroupSong.setIdSong(String.valueOf(song.getId()));
			myGroupSong.setSongName(song.getName(), bool, song.getSpannable());
			myGroupSong.setDrawable(drawActive, drawInActive, drawHover, drawFavourite, drawFavouriteNO, drawSinger,
					drawRemix, drawMidi, drawMV, drawUser, drawMVVid);
			myGroupSong.setContentView(position, song);
			myGroupSong.setOnViewDelListener(new OnViewDelListener() {

				@Override
				public void OnActive(boolean bool, Song s, String ipSong, float x, float y) {
					// song.setActive(bool);
					// ((MyApplication)mainActivity.getApplication()).addSongIntoPlayList(song);
					if (listener != null) {
						song.setActive(bool);
						// glowView.setActive(bool);
						listener.OnClickDel(song);
					}
				}

				@Override
				public void OnLockNotify() {
					
				}

				@Override
				public void OnUnLockNotify() {
					
				}

			});
		}

		return convertView;
	}


}
