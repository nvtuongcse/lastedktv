package vn.com.sonca.Touch.Listener;

import vn.com.sonca.params.Song;

public interface TouchIAdapter {
	
	public void OnFirstClick(Song song , int position, float x , float y);
	public void OnItemActive(Song song, String id , float x , float y);
	public void OnItemFavourite(int position, Song song);
	
	public void onPlaySong(Song song, int position, float x , float y);
	public void onDeleteSong(Song song , int position);

	public void OnSingerLink(boolean bool , String nameSinger, int[] idSinger);
	public void OnShowLyric(int position, Song song);

	public void OnLockNotify();
	public void OnUnLockNotify();
	
	public void onPlayYouTube(Song song);
	public void onDownYouTube(Song song);
	
}
