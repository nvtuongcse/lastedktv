package vn.com.sonca.ColorLyric;

import java.util.ArrayList;
import java.util.Locale;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Spannable;
import android.widget.TextView;
import vn.com.sonca.utils.XmlUtils;

public class SongColor {
	public final static int TASK_INSERT = 0;
	public final static int TASK_UPDATE = 1;
	public final static int TASK_DELETE = 2;

	private Spannable mHighlightText;
	private boolean mPadding;
	private int mId;
	private int mSingerId;
	private int mMusicianId;
	private int mTypeId;
	private int mLangId;
	private int mLyricId;
	private int mMedoly;
	private int mNewSong;
	private int mPurchased;
	private String mName;
	private String mUnsignedName;
	private String mShortname;
	private String mLyric;
	private long mTime;
	private String mUrlMusic;
	private String mUrlLyric;
	private String mSingName;
	private String mAuthName;
	private String mTypeName;
	private String mPathMedia;
	private boolean mDownloadLyric;
	private String mLyricDetail;
	private String mLyricDetailXML;
	private int mTask;
	private boolean mIsLoadLyric;
	private ArrayList<SongDetail> mDetails;
	private String mLyricView;

	public SongColor() {
		mIsLoadLyric = false;
		mId = 0;
		mDetails = new ArrayList<SongDetail>();
	}

	public void setpadding(boolean value) {
		this.mPadding = value;
	}

	public boolean getpadding() {
		return mPadding;
	}

	public int getTask() {
		return mTask;
	}

	public void setTask(int task) {
		this.mTask = task;
	}

	public String getPathMedia() {
		if (mPathMedia != null)
			return mPathMedia;
		return "";
	}

	public void setPathMedia(String pathMedia) {
		this.mPathMedia = pathMedia;
	}

	public String getSingName() {
		if (mSingName != null)
			return mSingName;
		return "";
	}

	public void setSingName(String singName) {
		this.mSingName = singName;
	}

	public String getAuthName() {
		if (mAuthName != null)
			return mAuthName;
		return "";
	}

	public void setAuthName(String authName) {
		this.mAuthName = authName;
	}

	public String getTypeName() {
		if (mTypeName != null)
			return mTypeName;
		return "";
	}

	public void setTypeName(String typeName) {
		this.mTypeName = typeName;
	}

	public String getUrlMusic() {
		if (mUrlMusic != null)
			return mUrlMusic;
		return "";
	}

	public void setUrlMusic(String urlMusic) {
		this.mUrlMusic = urlMusic;
	}

	public String getUrlLyric() {
		if (mUrlLyric != null)
			return mUrlLyric;
		return "";
	}

	public void setUrlLyric(String urlLyric) {
		this.mUrlLyric = urlLyric;
	}

	public long getTime() {
		return mTime;
	}

	public void setTime(long time) {
		this.mTime = time;
	}

	public int getId() {
		return mId;
	}

	public void setId(int id) {
		this.mId = id;
	}

	public int getSingerId() {
		return mSingerId;
	}

	public void setSingerId(int singerId) {
		this.mSingerId = singerId;
	}

	public int getMusicianId() {
		return mMusicianId;
	}

	public void setMusicianId(int musicianId) {
		this.mMusicianId = musicianId;
	}

	public int getTypeId() {
		return mTypeId;
	}

	public void setTypeId(int typeId) {
		this.mTypeId = typeId;
	}

	public int getLangId() {
		return mLangId;
	}

	public void setLangId(int langId) {
		this.mLangId = langId;
	}

	public int getLyricId() {
		return mLyricId;
	}

	public void setLyricId(int lyricId) {
		this.mLyricId = lyricId;
	}

	public int getMedoly() {
		return mMedoly;
	}

	public void setMedoly(int medoly) {
		this.mMedoly = medoly;
	}

	public int getNewSong() {
		return mNewSong;
	}

	public void setNewSong(int newSong) {
		this.mNewSong = newSong;
	}

	public int getPurchased() {
		return mPurchased;
	}

	public void setPurchased(int purchased) {
		this.mPurchased = purchased;
	}

	public String getName() {
		if (mName != null)
			return mName;
		return "";
	}

	public void setName(String name) {
		this.mName = name;
	}

	public String getShortname() {
		if (mShortname != null)
			return mShortname;
		return "";
	}

	public void setShortname(String shortname) {
		this.mShortname = shortname;
	}

	public String getLyric() {
		if (mLyric != null)
			return mLyric;
		return "";
	}

	public void setLyric(String lyric) {
		this.mLyric = lyric;
	}

	public ArrayList<SongDetail> getDetails() {
		return mDetails;
	}

	public void setDetails(ArrayList<SongDetail> details) {
		this.mDetails = details;
	}

	public String getLyricDetailXML() {
		if (mLyricDetailXML != null)
			return mLyricDetailXML;
		return "";
	}

	public void setLyricDetailXML(String lyricDetailXML) {
		this.mLyricDetailXML = lyricDetailXML;
	}

	public void addDetail(SongDetail detail) {
		if (mDetails == null) {
			mDetails = new ArrayList<SongDetail>();
		}
		mDetails.add(detail);
	}

	public SentenceSong getSentenceFromPosition1(int pos) {
		SentenceSong s = null;

		int i = -1;
		boolean isContinue = true;
		do {
			i++;
			if (i < mDetails.size() && pos > mDetails.get(i).getSentences().get(0).getTimeStart()) {

			} else {
				isContinue = false;
			}
		} while (isContinue);
		i--;
		if (i >= 0) {
			SongDetail detail = mDetails.get(i);
			int j = 0;
			while (j < detail.getSentences().size() && detail.getSentences().get(j).getTimeStart() != pos && pos > detail.getSentences().get(j).getTimeStart()) {
				j++;
			}
			if (j < detail.getSentences().size() && detail.getSentences().get(j).getTimeStart() == pos) {

				s = detail.getSentences().get(j);
				if (j % 2 == 0) {
					s.setLine(0);
				} else {
					s.setLine(1);
				}
			} else {
				j--;
				if (j >= 0 && j < detail.getSentences().size()) {
					s = detail.getSentences().get(j);
					if (j % 2 == 0) {
						s.setLine(0);
					} else {
						s.setLine(1);
					}
				}
			}
		}
		return s;
	}

	public SentenceSong getSentenceFromPosition(int Position) {
		SentenceSong s = null;
		int i = getLyricBlock(Position);
		if (i >= 0) {
			SongDetail detail = mDetails.get(i);
			int j = 0;

			int cntLoop = 0;
			int start = 0, end, midPt = 0, savePrevMidPt = 0;
			end = detail.getSentences().size() - 1;
			while (start <= end) {
				midPt = (start + end) / 2;
				if (detail.getSentences().get(midPt).getTimeStart() == Position || midPt == savePrevMidPt) {
					break;
				} else if (detail.getSentences().get(midPt).getTimeStart() < Position) {
					start = midPt + 1;
				} else {
					end = midPt - 1;
				}
				cntLoop++;
				if (cntLoop > 10)
					break;
			}
			if (cntLoop > 10)
				j = 0;
			else {
				j = (midPt > 0) ? (midPt - 1) : 0;
			}
			while (j < detail.getSentences().size() && detail.getSentences().get(j).getTimeStart() < Position) {
				j++;
			}

			if (j < detail.getSentences().size() && detail.getSentences().get(j).getTimeStart() == Position) {
				s = detail.getSentences().get(j);
				if (j % 2 == 0) {
					s.setLine(0);
				} else {
					s.setLine(1);
				}
			} else {
				j--;
				if (j >= 0 && j < detail.getSentences().size()) {
					s = detail.getSentences().get(j);
					if (j % 2 == 0) {
						s.setLine(0);
					} else {
						s.setLine(1);
					}
				}
			}
		}
		return s;
	}

	public SentenceSong getSentenceFromPosition(int pos, boolean isLine0) {
		SentenceSong s = null;

		int i = -1;
		for (int j = 0; j < mDetails.size(); j++) {
			if (pos > mDetails.get(j).getSentences().get(0).getTimeStart()) {
				i = j;
			}
		}
		if (i >= 0) {
			if (pos > mDetails.get(i).getEnd().getTimeStart()) {
				i++;
			}
		}
		if (i >= 0) {
			SongDetail detail = mDetails.get(i);
			int j = 0;
			while (j < detail.getSentences().size() && detail.getSentences().get(j).getTimeStart() != pos && pos > detail.getSentences().get(j).getTimeStart()) {
				j++;
			}
			if (j < detail.getSentences().size() && detail.getSentences().get(j).getTimeStart() == pos) {

				s = detail.getSentences().get(j);
				if (j % 2 == 0) {
					s.setLine(0);
				} else {
					s.setLine(1);
				}
			} else {
				j = j - 2;
				if (j >= 0 && j < detail.getSentences().size()) {
					s = detail.getSentences().get(j);
					if (j % 2 == 0) {
						s.setLine(0);
					} else {
						s.setLine(1);
					}
				}
			}
		}
		return s;
	}

	public WordSong getCountStartFromPosition(int currentPosition) {
		WordSong w = null;
		int i = -1;
		boolean isContinue = true;
		do {
			i++;
			if (i < mDetails.size() && currentPosition > mDetails.get(i).getSentences().get(0).getTimeStart()) {

			} else {
				isContinue = false;
			}
		} while (isContinue);
		i--;
		if (i >= 0) {
			SongDetail detail = mDetails.get(i);
			int index = 0;
			for (int j = 0; j < detail.getCountStarts().size(); j++) {
				if (currentPosition > detail.getCountStarts().get(j).getTimeStart()) {
					index = j;
				}
			}
			w = detail.getCountStarts().get(index);
			if (w.getTimeStart() > currentPosition) {
				w = null;
			}
			if (w != null && (w.getTimeLenght() + w.getTimeStart() < currentPosition) && index == detail.getCountStarts().size() - 1) {
				w = null;
			}

		}
		return w;
	}

	public SentenceSong getLastSentences(int currentPosition) {
		int i = -1;
		for (int j = 0; j < mDetails.size(); j++) {
			if (currentPosition > mDetails.get(j).getSentences().get(0).getTimeStart()) {
				i = j;
			}
		}
		if (i >= 0) {
			if (currentPosition > mDetails.get(i).getEnd().getTimeStart()) {
				i++;
			}
			if (i < mDetails.size()) {
				SentenceSong s = mDetails.get(i).getSentences().get(mDetails.get(i).getSentences().size() - 1);
				if (mDetails.get(i).getSentences().size() - 1 % 2 == 0) {
					s.setLine(0);
				} else {
					s.setLine(1);
				}
				return s;
			}
		}
		return null;
	}

	public int getLyricBlock(int currentPosition) {
		for (int j = 0; j < mDetails.size(); j++) {
			if (currentPosition > mDetails.get(j).getSentences().get(0).getTimeStart()) //start of cur block
			{
				if (currentPosition > mDetails.get(j).getEnd().getTimeStart()) //next block
				{
					j++;
				}
				return j;
			}
		}
		return -1;
	}

	public boolean isLastSentences(int currentPosition) {
		int curBlock = getLyricBlock(currentPosition);
		if (curBlock >= 0) {
			if (currentPosition >= mDetails.get(curBlock).getSentences().get(mDetails.get(curBlock).getSentences().size() - 1).getTimeStart()) {
				return true;
			}
		}
		return false;
	}

	public boolean isDownload_lyric() {
		return mDownloadLyric;
	}

	public void setDownload_lyric(boolean download_lyric) {
		this.mDownloadLyric = download_lyric;
	}

	public String getLyricDetail() {
		if (mLyricDetail != null)
			return mLyricDetail;
		return "";
	}

	public void setLyricDetail(String lyricDetail) {
		this.mLyricDetail = lyricDetail;
	}

	/*
	 * Load Lyric From Server and decrypt
	 */
	public void parseLyricWithDecrypt(Document document) {
		try {
			Element nodeTitle = (Element) document.getElementsByTagName("title").item(0);
			NodeList nodeTitleItems = nodeTitle.getElementsByTagName("item");
			int index = 0;
			for (int i = 0; i < nodeTitleItems.getLength(); i++) {
				Element elementItem = (Element) nodeTitleItems.item(i);
				String header = elementItem.getAttribute("header");
				String content = elementItem.getChildNodes().item(0).getNodeValue().trim();

				if (header.equals("INDX")) {
					index = Integer.parseInt(content);
				}

				if (header.equals("TITL")) {
					mName = content;
				}
				if (header.equals("AUTH")) {}
				if (header.equals("SING")) {}
				if (header.equals("MELO")) {
					mMedoly = Integer.parseInt(content);
				}
				if (header.equals("TYPE")) {
					mTypeName = content;
				}
				if (header.equals("RHTM")) {

				}
				if (header.equals("TONE")) {

				}
				if (header.equals("KEY")) {

				}
				if (header.equals("LYRS")) {

				}
				if (header.equals("LANG")) {

				}
			}

			DecryptLyric.getWordSize(index);

			Element nodeLyrics = (Element) document.getElementsByTagName("lyrics").item(0);
			NodeList nodeLyricsItems = nodeLyrics.getElementsByTagName("i");
			mDetails = new ArrayList<SongDetail>();
			SentenceSong sen = null;
			SongDetail detail = new SongDetail();
			mDetails.add(detail);
			mLyricView = "";

			int itemCount = 0;
			for (int i = 0; i < nodeLyricsItems.getLength(); i++) {
				Element elementItem = (Element) nodeLyricsItems.item(i);
				String header = elementItem.getAttribute("h");

				int startTime = Integer.parseInt(elementItem.getAttribute("a"));
				int lengthTime = Integer.parseInt(elementItem.getAttribute("b"));
				String tempText = elementItem.getChildNodes().item(0).getNodeValue().trim();
				tempText = DecryptLyric.getStringValue(tempText, lengthTime);
				startTime = DecryptLyric.getInt32(startTime) * 10;
				lengthTime = DecryptLyric.getInt32(lengthTime);
				lengthTime -= (5 * itemCount + 3);
				lengthTime *= 10;

				String content = tempText; //elementItem.getChildNodes().item(0).getNodeValue().trim();

				if (header.equals("LINW") || header.equals("LINM") || header.equals("LINN") || header.equals("LINA")) {

					if (header.equals("LINN") || header.equals("LINA")) {
						header = "LINM";
					}

					detail.add(content);
					sen = new SentenceSong();
					sen.setTimeStart(startTime);
					sen.setTimeLenght(lengthTime);
					sen.setSentence(content);

					if (header.equals("LINW")) {
						sen.setType(SentenceSong.TYPE_WOMAN);
					} else {
						sen.setType(SentenceSong.TYPE_MAN);
					}
					detail.addSentence(sen);
				}
				if (header.equals("WORD")) {
					WordSong word = new WordSong();
					word.setTimeStart(startTime);
					word.setTimeLenght(lengthTime);
					word.setWord(content);

					if (sen != null) {
						sen.addWord(word);
					}
				}
				if (header.equals("CNTS")) {
					WordSong count = new WordSong();
					count.setTimeStart(startTime);
					count.setTimeLenght(lengthTime);
					count.setWord(content);

					detail.addCountStart(count);
				}
				if (header.equals("CNTE")) {

					int iTimeStart = startTime;
					int iTimeLen = lengthTime;
					int iDelta = 100;

					//update 1
					WordSong count = new WordSong();
					count.setTimeStart(iTimeStart);
					count.setTimeLenght(iDelta);
					iTimeLen -= iDelta;
					if (i < nodeLyricsItems.getLength() - 1) {
						count.setWord(content);
						detail.addCountStart(count);
					} else {
						count.setWord("00"); //end song					
						detail.addCountStart(count);
					}

					iTimeStart += iDelta;
					WordSong end = new WordSong();
					end.setTimeStart(iTimeStart);
					end.setTimeLenght(iTimeLen);
					end.setWord(content);
					detail.setEnd(end);
					if (i < nodeLyricsItems.getLength() - 1) {
						detail = new SongDetail();
						mDetails.add(detail);
					}
				}

				if (header.equals("LINM")) {

					if (tempText.substring(0, 1).equals(tempText.substring(0, 1).toLowerCase(Locale.getDefault()))) {
						mLyricView += ", " + tempText.trim();
					}
					if (tempText.substring(0, 1).equals(tempText.substring(0, 1).toUpperCase(Locale.getDefault()))) {
						mLyricView += ". " + tempText.trim();
					}
				}
				itemCount++;
			}

		} catch (Exception ex) {}
	}

	public void parseLyric(Document document) {
		try {
			Element nodeTitle = (Element) document.getElementsByTagName("title").item(0);
			NodeList nodeTitleItems = nodeTitle.getElementsByTagName("item");
			for (int i = 0; i < nodeTitleItems.getLength(); i++) {
				Element elementItem = (Element) nodeTitleItems.item(i);
				String header = elementItem.getAttribute("header");
				String content = elementItem.getChildNodes().item(0).getNodeValue().trim();
				if (header.equals("TITL")) {
					mName = content;
				}
				if (header.equals("AUTH")) {}
				if (header.equals("SING")) {}
				if (header.equals("MELO")) {
					mMedoly = Integer.parseInt(content);
				}
				if (header.equals("TYPE")) {
					mTypeName = content;
				}
				if (header.equals("RHTM")) {

				}
				if (header.equals("TONE")) {

				}
				if (header.equals("KEY")) {

				}
				if (header.equals("LYRS")) {

				}
				if (header.equals("LANG")) {

				}
			}
			Element nodeLyrics = (Element) document.getElementsByTagName("lyrics").item(0);
			NodeList nodeLyricsItems = nodeLyrics.getElementsByTagName("item");
			mDetails = new ArrayList<SongDetail>();
			SentenceSong sen = null;
			SongDetail detail = new SongDetail();
			mDetails.add(detail);
			for (int i = 0; i < nodeLyricsItems.getLength(); i++) {
				Element elementItem = (Element) nodeLyricsItems.item(i);
				String header = elementItem.getAttribute("header");
				int startTime = Integer.parseInt(elementItem.getAttribute("startTime"));
				int lengthTime = Integer.parseInt(elementItem.getAttribute("lengthTime"));
				String content = elementItem.getChildNodes().item(0).getNodeValue().trim();
				if (header.equals("LINW") || header.equals("LINM")) {

					detail.add(content);
					sen = new SentenceSong();
					sen.setTimeStart(startTime);
					sen.setTimeLenght(lengthTime);
					sen.setSentence(content + " ");
					if (header.equals("LINW")) {
						sen.setType(SentenceSong.TYPE_WOMAN);
					} else {
						sen.setType(SentenceSong.TYPE_MAN);
					}
					detail.addSentence(sen);
				}
				if (header.equals("WORD")) {
					WordSong word = new WordSong();
					word.setTimeStart(startTime);
					word.setTimeLenght(lengthTime);
					word.setWord(content);
					if (sen != null) {
						sen.addWord(word);
					}
				}
				if (header.equals("CNTS")) {
					WordSong count = new WordSong();
					count.setTimeStart(startTime);
					count.setTimeLenght(lengthTime);
					count.setWord(content);
					detail.addCountStart(count);
				}
				if (header.equals("CNTE")) {
					WordSong end = new WordSong();
					end.setTimeStart(startTime);
					end.setTimeLenght(lengthTime);
					end.setWord(content);
					detail.setEnd(end);
					if (i < nodeLyricsItems.getLength() - 1) {
						detail = new SongDetail();
						mDetails.add(detail);
					}
				}

			}
		} catch (Exception ex) {}
	}

	public void parse(Element nodeTitle) {
		NodeList nodeTitleItems = nodeTitle.getElementsByTagName("item");
		for (int i = 0; i < nodeTitleItems.getLength(); i++) {
			Element elementItem = (Element) nodeTitleItems.item(i);
			String header = elementItem.getAttribute("header");
			String content = elementItem.getChildNodes().item(0).getNodeValue();
			if (header.equals("TITL")) {
				mName = content;
			}
			if (header.equals("AUTH")) {}
			if (header.equals("SING")) {}
			if (header.equals("MELO")) {
				mMedoly = Integer.parseInt(content);
			}
			if (header.equals("TYPE")) {
				mTypeName = content;
			}
			if (header.equals("RHTM")) {

			}
			if (header.equals("TONE")) {

			}
			if (header.equals("KEY")) {

			}
			if (header.equals("LYRS")) {
				mLyric = content;
			}
			if (header.equals("LANG")) {

			}
			if (header.equals("INDX")) {
				mId = Integer.parseInt(content);
			}
			if (header.equals("TASK")) {
				if ("INSERT".equals(content)) {
					mTask = TASK_INSERT;
				}
				if ("UPDATE".equals(content)) {
					mTask = TASK_UPDATE;
				}
				if ("DELETE".equals(content)) {
					mTask = TASK_DELETE;
				}
			}
		}
	}

	public void prepare(TextView lblLine1) {
		for (int i = 0; i < mDetails.size(); i++) {
			SongDetail detail = mDetails.get(i);
			for (int j = 0; j < detail.getSentences().size(); j++) {
				ArrayList<WordSong> words = detail.getSentences().get(j).getWords();
				detail.getSentences().get(j).setWidthOfText(getWidthOfText(detail.getSentences().get(j).getSentence(), lblLine1));
				float sizeWidth = detail.getSentences().get(j).getWidthOfText();
				float firstWord = 0;
				float unit = (sizeWidth / detail.getSentences().get(j).getSentence().length());
				for (int k = 0; k < words.size(); k++) {
					words.get(k).setSizeStart(firstWord);
					float length = getWidthOfText(words.get(k).getWord(), lblLine1); //words.get(k).getWord().length() * unit;
					words.get(k).setSizeLength(length);
					firstWord = firstWord + length + unit;
				}
			}
		}
		mIsLoadLyric = true;
	}

	protected float getWidthOfText(String text, TextView lblTextView) {
		Rect bounds = new Rect();
		Paint textPaint = lblTextView.getPaint();
		textPaint.getTextBounds(text, 0, text.length(), bounds);
		return bounds.width();
	}

	public boolean isLoadLyric() {
		return mIsLoadLyric;
	}

	public void setLoadLyric(boolean isLoadLyric) {
		this.mIsLoadLyric = isLoadLyric;
	}

	public void importNode(Element item) {
		mName = XmlUtils.getValue(item, "title");
		mAuthName = XmlUtils.getValue(item, "author");
		mSingName = XmlUtils.getValue(item, "singer");
		mLyric = XmlUtils.getValue(item, "words");
		mLyricDetail = XmlUtils.getValue(item, "lyric");
	}

	public String getLyricView() {
		if (mLyricView != null)
			return mLyricView;
		return "";
	}

	public void setLyricView(String lyricView) {
		this.mLyricView = lyricView;
	}

	public String getUnsignedName() {
		if (mUnsignedName != null)
			return mUnsignedName;
		return "";
	}

	public void setUnsignedName(String unsignedName) {
		this.mUnsignedName = unsignedName;
	}

	public Spannable getHighlightText() {
		return mHighlightText;
	}

	public void setHighlightText(Spannable highlightText) {
		this.mHighlightText = highlightText;
	}
}
