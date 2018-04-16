package vn.com.sonca.ColorLyric;

import java.util.ArrayList;

public class SongDetail {

	private String singName = "";
	private String authName = "";
	private String typeName = "";
	private String pathMedia = "";
	private ArrayList<String> lyrics = new ArrayList<String>();
	private ArrayList<SentenceSong> sentences = new ArrayList<SentenceSong>();
	private ArrayList<WordSong> countStarts = new ArrayList<WordSong>();
	private WordSong end;

	public WordSong getEnd() {
		return end;
	}

	public void setEnd(WordSong end) {
		this.end = end;
	}

	public String getPathMedia() {
		return pathMedia;
	}

	public void setPathMedia(String pathMedia) {
		this.pathMedia = pathMedia;
	}

	public ArrayList<String> getLyrics() {
		return lyrics;
	}

	public void setLyrics(ArrayList<String> lyrics) {
		this.lyrics = lyrics;
	}

	public String getSingName() {
		return singName;
	}

	public void setSingName(String singName) {
		this.singName = singName;
	}

	public String getAuthName() {
		return authName;
	}

	public void setAuthName(String authName) {
		this.authName = authName;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public ArrayList<String> getLyric() {
		return lyrics;
	}

	public void setLyric(ArrayList<String> lyric) {
		this.lyrics = lyric;
	}

	public void add(String string) {
		// TODO Auto-generated method stub		
		if (lyrics == null) {
			lyrics = new ArrayList<String>();
		}
		lyrics.add(string);
	}

	public String getShoterLyric() {
		String data = "";
		for (int i = 0; i < 5 && i < lyrics.size(); i++) {
			data += lyrics.get(i) + " ";
		}
		return data;
	}

	public String getFullLyric() {
		// TODO Auto-generated method stub
		String data = "";
		String f = "";
		//		Log.d("lyrics size", String.valueOf(lyrics.size()));
		for (int i = 0; i < lyrics.size(); i++) {
			String first = lyrics.get(i).substring(0, 1);
			if (lyrics.get(i).contains(".")) {
				lyrics.set(i, lyrics.get(i).replace(".", ""));
			}
			if (first.equals(first.toUpperCase()) && !".".equals(f) && f.length() > 0) {
				data = data.trim() + ". ";
			} else {

			}
			f = lyrics.get(i).substring(lyrics.get(i).length() - 1, lyrics.get(i).length());
			data += lyrics.get(i) + " ";
		}
		return data;
	}

	public void addCountStart(WordSong word) {
		if (countStarts == null) {
			countStarts = new ArrayList<WordSong>();
		}
		countStarts.add(word);
	}

	public void addSentence(SentenceSong sen) {
		if (sentences == null) {
			sentences = new ArrayList<SentenceSong>();
		}
		sentences.add(sen);
	}

	public ArrayList<SentenceSong> getSentences() {
		return sentences;
	}

	public void setSentences(ArrayList<SentenceSong> sentences) {
		this.sentences = sentences;
	}

	public ArrayList<WordSong> getCountStarts() {
		return countStarts;
	}

	public void setCountStarts(ArrayList<WordSong> countStarts) {
		this.countStarts = countStarts;
	}

}
