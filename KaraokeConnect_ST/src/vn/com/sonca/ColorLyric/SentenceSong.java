package vn.com.sonca.ColorLyric;

import java.util.ArrayList;

public class SentenceSong {
	public static int TYPE_WOMAN = 1;
	public static int TYPE_MAN = 0;
	public static int TYPE_ALL = 2;
	private int timeStart;
	private int timeLenght;
	private String sentence;
	private ArrayList<WordSong> words = new ArrayList<WordSong>();
	private int type;
	private int line;
	private String typeSinger;
	private float widthOfText;

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

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

	public String getSentence() {
		return " " + sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public ArrayList<WordSong> getWords() {
		return words;
	}

	public void setWords(ArrayList<WordSong> words) {
		this.words = words;
	}

	public void addWord(WordSong word) {
		if (words == null) {
			words = new ArrayList<WordSong>();
		}
		words.add(word);
	}

	public int getTimeLenght2() {
		int length = 0;
		for (int i = 0; i < words.size(); i++) {
			length += words.get(i).getTimeLenght();
		}
		return length;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public String getTypeSinger() {
		return typeSinger;
	}

	public void setTypeSinger(String type) {
		this.typeSinger = type;
	}
	
	public int getWordFromPosition(int currentPostion) {
		// TODO Auto-generated method stub
		int index = -1;
		for (int i = 0; i < words.size(); i++) {
			if (currentPostion >= words.get(i).getTimeStart()) {
				index = i;
			}
		}
		return index;
	}

	public float getWidthOfText() {
		return widthOfText;
	}

	public void setWidthOfText(float widthOfText) {
		this.widthOfText = widthOfText;
	}

}
