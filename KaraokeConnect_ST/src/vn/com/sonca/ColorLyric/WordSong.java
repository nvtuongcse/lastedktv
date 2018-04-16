package vn.com.sonca.ColorLyric;

public class WordSong {

	private int timeStart;
	private int timeLenght;
	private float sizeStart;
	private float sizeLength;

	public float getSizeStart() {
		return sizeStart;
	}

	public void setSizeStart(float sizeStart) {
		this.sizeStart = sizeStart;
	}

	public float getSizeLength() {
		return sizeLength;
	}

	public void setSizeLength(float sizeLength) {
		this.sizeLength = sizeLength;
	}

	private String word;

	public int getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(int timeStart) {
		this.timeStart = timeStart;
	}

	public int getTimeLenght() {
		return timeLenght;
	}

	public void setTimeLenght(int timeLenght) {
		this.timeLenght = timeLenght;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

}
