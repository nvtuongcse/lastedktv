package vn.com.sonca.params;

public class Lyric {
	
	private int idSong;
	private int offsetLyric;
	private int sizelyric;
	
	public static int sizeof(){
		return 4*4;
	}
	
	public int getIdSong() {
		return idSong;
	}
	public void setIdSong(int idSong) {
		this.idSong = idSong;
	}
	public int getOffsetLyric() {
		return offsetLyric;
	}
	public void setOffsetLyric(int offsetLyric) {
		this.offsetLyric = offsetLyric;
	}
	public int getSizelyric() {
		return sizelyric;
	}
	public void setSizelyric(int sizelyric) {
		this.sizelyric = sizelyric;
	}

}
