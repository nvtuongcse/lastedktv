package vn.com.sonca.zktv.main;

import vn.com.sonca.params.Song;

public interface OnFragDataListener {
	public void OnCreateFrag(String namefrag);
	public void OnClickData(Song song, String nameFrag);
	public void OnFirstClick(Song song, String nameFrag, int postion, float x, float y);
	public void OnSingerLink(Song song, String nameFrag);
	public void OnSongLick(Song song, String nameFrag, float x, float y);
	public void OnDeleteClick(Song song, String nameFrag, int position);
	public void OnPlayYouTube(Song song, String nameFrag);
	public void OnDownYouTube(Song song, String nameFrag);
}
