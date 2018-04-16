package vn.com.sonca.Touch.Listener;

import java.util.ArrayList;

import com.moonbelly.youtubeFrag.MyYouTubeInfo;

import vn.com.sonca.params.Song;

public interface TouchIBaseFragment {
	
	public void onFirstClick(Song song , String typeFragment , int position, float x , float y);
	public void onClickItem(Song song , String id , String typeFragment , String search , int state, float x , float y);
	public void onPlaySong(Song song , String typeFragment, int position , float x , float y);
	public void onDeleteSong(Song song , int position);
	public void onMoveSong(ArrayList<Song> list);
	public void OnSingerLink(boolean bool , String nameSinger, int[] idSinger);
	public void OnNameSearch(String nameSinger , String typeFrag);
	public void OnClickFavourite();
	
	public void OnHideKeyBoard();
	public void OnShowLyric();
	public void OnShowReviewKaraoke(Song song);
	public void onPlayYouTube(Song song);
	public void onDownYouTube(Song song);
	
	public void onClickYouTube(MyYouTubeInfo info, int type, int position, float x, float y);
	
}
