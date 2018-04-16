package vn.com.sonca.ColorLyric;

public class LyricData {

	private int idSong;
	private int offsetLyric;
	private int sizelyric;
	private int songProperty;

	private int typeABC;
	private boolean modelA;

	private byte[] lyricData;

	public byte[] getLyricData() {
		return lyricData;
	}

	public void setLyricData(byte[] lyricData) {
		this.lyricData = lyricData;
	}

	public static int sizeof() {
		return 4 * 4;
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

	public int getSongProperty() {
		return songProperty;
	}

	public void setSongProperty(int songProperty) {
		this.songProperty = songProperty;
	}

	public int getTypeABC() {
		return typeABC;
	}

	public void setTypeABC(int typeABC) {
		this.typeABC = typeABC;
	}

	public boolean isModelA() {

		if (songProperty > 0) {
			return (((songProperty >> 8) & 0x01) == 1);
		}

		return modelA;
	}
	
	public int getMediaType(){
		if (songProperty > 0) {
			return ((songProperty >> 9) & 0x0f);
		}	
		
		return 0;
	}

	public void setModelA(boolean modelA) {
		this.modelA = modelA;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}

		boolean flag = ((LyricData) o).getIdSong() == this.getIdSong()
				&& ((LyricData) o).getTypeABC() == this.getTypeABC();
		
		return flag;

	}
}