package vn.com.sonca.ColorLyric;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.params.Song;
import vn.com.sonca.utils.AppConfig;

import com.midi.MidiFile;
import com.midi.MidiTrack;
import com.midi.event.MidiEvent;
import com.midi.event.meta.Marker;
import com.midi.event.meta.Tempo;
import com.midi.util.MidiUtil;

import android.content.Context;
import android.widget.TextView;

public class Lyrics {
	private final String TAG = "Lyrics";
	private String mSongTitle;
	private String mSongAuthor;
	private String mSongLyrician;
	private String mSongSinger;
	private String mLyricView = "";
	private boolean iEndLyric = false;
	public ArrayList<SongDetail> mDetails = new ArrayList<SongDetail>();

	public Lyrics(Lyrics lyrics) {
		this.mSongTitle = lyrics.mSongTitle;
		this.mSongSinger = lyrics.mSongSinger;
		this.mSongAuthor = lyrics.mSongAuthor;
		this.mLyricView = lyrics.mLyricView;
		this.mDetails = new ArrayList<SongDetail>(lyrics.mDetails);
	}
	
	public Lyrics(SongColor song, Context context) {}
	
	public String getSongTitle() {
		return mSongTitle;
	}

	public void setSongTitle(String songTitle) {
		this.mSongTitle = songTitle;
	}

	public String getSongAuthor() {
		return mSongAuthor;
	}

	public void setSongAuthor(String songAuthor) {
		this.mSongAuthor = songAuthor;
	}

	public String getSongLyrician() {
		return mSongLyrician;
	}

	public void setSongLyrician(String mSongLyrician) {
		this.mSongLyrician = mSongLyrician;
	}

	public String getSongSinger() {
		return mSongSinger;
	}

	public void setSongSinger(String songSinger) {
		mSongSinger = songSinger;
	}

	public String getLyricView() {
		return mLyricView;
	}
	public boolean iEndLyric() {
		return iEndLyric;
	}
	public void setLyricView(String lyricView) {
		mLyricView = lyricView;
	}

	public SentenceSong getSentenceFromPosition(int Position) {
		SentenceSong s = null;
		int i = getLyricBlock(Position);
		//MyLog.d(TAG, "getSentenceFromPosition=Position="+Position+"=i="+i+"=="+mDetails.size()+"=iEndLyric="+iEndLyric);
		if(i==mDetails.size()){
			iEndLyric=true;
		}
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
		int k = 0;
		boolean i=false;
		for (int j = 0; j < mDetails.size(); j++) {
			//MyLog.d(TAG, "=getLyricBlock=currentPosition="+currentPosition+"=j="+j+"=size="+mDetails.size()+"=getTimeStart="+mDetails.get(j).getSentences().get(0).getTimeStart()+"=getEnd="+mDetails.get(j).getEnd().getTimeStart());
			if (currentPosition > mDetails.get(j).getSentences().get(0).getTimeStart()) //start of cur block
			{
				if (currentPosition > mDetails.get(j).getEnd().getTimeStart()) //next block
				{
					k++;
				}
				i=true;
				//return j;
			}
		}
		if(i)
			return k;
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
	
	public void parseLyricFromMidiFile(InputStream is) throws IOException {
			//  MyLog.d(TAG, "=parseLyricFromMidiFile=midiShifTime="+midiShifTime);
			MidiFile midiFile = new MidiFile(is);
		    int lyricTrack = midiFile.getLyricTrack();
		    ArrayList<com.midi.event.meta.Lyrics> lyricEvents = new ArrayList<com.midi.event.meta.Lyrics>();
		    Iterator<MidiEvent> it = ((MidiTrack)midiFile.getTracks().get(lyricTrack)).getEvents().iterator();

    		mSongTitle = "-"; 
    		mSongAuthor = "-"; 
    		mSongLyrician = "-";
    		mSongSinger = "-"; 
		    
    		midiFile.buildLyricTimeMap(); 
    		
		    while (it.hasNext()) {
		    	MidiEvent anEvent = it.next(); 
//		    	System.out.println(anEvent.toString()); 
		    	
		    	if(anEvent.getClass().equals(Marker.class)) {
		    		if(mSongTitle.equals("-") == false) continue; 
		    		
		    		String marker = ((Marker)anEvent).getMarkerName(); 
		    		int titleStart = marker.indexOf("@t"); 
		    		int authorStart = marker.indexOf("@m"); 
		    		int lyricianStart = marker.indexOf("@l"); 
		    		int singerStart = marker.indexOf("@s"); 
		    		if(titleStart != -1 && authorStart != -1) {
		    			mSongTitle = marker.substring(titleStart + 2, authorStart);
		    			mSongTitle = mSongTitle.replace('/', '\n'); 
		    		}
		    		if(authorStart != -1 && lyricianStart != -1) {
		    			mSongAuthor = marker.substring(authorStart + 2, lyricianStart);
		    		}
		    		if(lyricianStart != -1 && singerStart != -1) {
		    			mSongLyrician = marker.substring(lyricianStart + 2, singerStart);
		    		}
		    		if(singerStart != -1) {
		    			mSongSinger = marker.substring(singerStart + 2);
		    		}
		    		continue; 
		    	}
		    	
		    	if(anEvent.getClass().equals(com.midi.event.meta.Lyrics.class)) {
		    		lyricEvents.add((com.midi.event.meta.Lyrics)anEvent); 
		    	}
		    }
		    
		    mDetails = new ArrayList<SongDetail>();
			SentenceSong sen = null;
			SongDetail detail = new SongDetail();
		    
			int cntDownVal = 0; 
			
			String lineText = ""; 
		    String lineHeader = "LINM"; 
			int lineStartTime = 0; 
			int lineDuration = 0; 
			
			boolean isLineStart = false; 
			boolean isParagraphEnd = false; 
			
		    int i, j; 
		    int startTime = 0; 
		    int duration = 0; 
		    int lastShowTime = 0; 
		    com.midi.event.meta.Lyrics curLyric; 
		    for(i = 0; i < lyricEvents.size(); i++) {
		    	curLyric = lyricEvents.get(i); 
		    	startTime = (int)curLyric.getTickInMs(); 
		    	int nextStartTime = startTime; 
		    	for(j = i+1; j < lyricEvents.size(); j++) {
		    		com.midi.event.meta.Lyrics nxtEvent = lyricEvents.get(j);
		    		String tmpText = ((com.midi.event.meta.Lyrics)nxtEvent).getLyric(); 
		    		if(tmpText == null || tmpText.length() == 0) continue; 
		    		if(tmpText.charAt(0) == '\n' || tmpText.charAt(0) == '\r') continue; 
		    		nextStartTime = (int) nxtEvent.getTickInMs(); 
		    		
		    		if(tmpText.charAt(0) == '#' && isLineStart == true) {
		    			nextStartTime -= 500; 
		    		}
		    		
		    		break;
		    	}
		    	duration = nextStartTime - startTime; 
		    	if(duration <= 0) {
		    		duration = 400; 
		    	}else if(duration > 5000) {
		    		duration = 5000; 
		    	}
		    	
//	    		Log.e(TAG, "Text: " + curLyric.getLyric() + ", starttime: " + startTime + ", duration: " + duration);  	
		    	String text = curLyric.getLyric(); 
		    	if(text == null || text.length() == 0) continue; 
		    	char firstChar = text.charAt(0); 
		    	
		    	if(firstChar == '#') {
		    		if(isLineStart) {
		    			isLineStart = false; 
		    			if (lineHeader.equals("LINN") ) {
		    				lineHeader = "LINM";
						}

						detail.add(lineText);
						sen.setTimeStart(lineStartTime);
						sen.setTimeLenght(lineDuration);
						sen.setSentence(lineText);
						sen.setTypeSinger(lineHeader);
						if (lineHeader.equals("LINW")) {
							sen.setType(SentenceSong.TYPE_WOMAN);
						} else if (lineHeader.equals("LINM")) {
							sen.setType(SentenceSong.TYPE_MAN);
						}else if (lineHeader.equals("LINA")) {
							sen.setType(SentenceSong.TYPE_ALL);
						}else{
							sen.setType(SentenceSong.TYPE_MAN);
						}
						detail.addSentence(sen);
		    		}
		    		
		    		if(detail.getSentences().size() > 0 && cntDownVal >= 4) {
		    			int iTimeStart = lastShowTime;
						int iTimeLen = startTime - lastShowTime;
						int iDelta = 100;
						if(iTimeLen > 5000) iTimeLen = 5000; 

						//update 1
						WordSong count = new WordSong();
						count.setTimeStart(iTimeStart);
						iTimeLen -= iDelta;
						count.setTimeLenght(iTimeLen);
						if (i < lyricEvents.size() - 1) {
							count.setWord("0");
							detail.addCountStart(count);
						} else {
							count.setWord("00"); //end song					
							detail.addCountStart(count);
						}

						//update 2
						iTimeStart += iDelta;
						WordSong end = new WordSong();
						end.setTimeStart(iTimeStart);
						end.setTimeLenght(iTimeLen);
						end.setWord("0");
						detail.setEnd(end);
						if (i < lyricEvents.size() - 1) {
							if(detail.getSentences().size() > 0) {
								mDetails.add(detail);
								isParagraphEnd = true; 
							}
							detail = new SongDetail();
							isParagraphEnd = false; 
						}
						cntDownVal = 0; 
		    		}
		    		
		    		if(cntDownVal < 4) {
		    			WordSong count = new WordSong();
						count.setTimeStart(startTime);
						count.setTimeLenght(duration);
						count.setWord("" + (4 - cntDownVal));
						
						detail.addCountStart(count);
						
						cntDownVal++; 
		    		}
		    		isLineStart = false; 
		    		continue; 
		    	}else if(firstChar == '\n' || firstChar == '\r') {
		    		if(isLineStart) {
		    			isLineStart = false; 
		    			if (lineHeader.equals("LINN") ) {
		    				lineHeader = "LINM";
						}

						detail.add(lineText);
						sen.setTimeStart(lineStartTime);
						sen.setTimeLenght(lineDuration);
						sen.setSentence(lineText);
						sen.setTypeSinger(lineHeader);
						if (lineHeader.equals("LINW")) {
							sen.setType(SentenceSong.TYPE_WOMAN);
						} else if (lineHeader.equals("LINM")) {
							sen.setType(SentenceSong.TYPE_MAN);
						}else if (lineHeader.equals("LINA")) {
							sen.setType(SentenceSong.TYPE_ALL);
						}else{
							sen.setType(SentenceSong.TYPE_MAN);
						}
						detail.addSentence(sen);
						
						if (lineHeader.equals("LINM")) {

							if (lineText.substring(0, 1).equals(lineText.substring(0, 1).toLowerCase(AppConfig.curLocale))) {
								mLyricView += ", " + lineText.trim();
							}
							if (lineText.substring(0, 1).equals(lineText.substring(0, 1).toUpperCase(AppConfig.curLocale))) {
								mLyricView += ". " + lineText.trim();
							}
						}
						
		    		}
		    		continue; 
		    	}else if(firstChar == '[' || firstChar == ']') {
		    		if(isParagraphEnd == false) {
		    			isLineStart = false; 
//		    			int iTimeStart = startTime;
//						int iTimeLen = duration;
		    			int iTimeStart = lastShowTime;
						int iTimeLen = 5000;
						int iDelta = 100;

						//update 1
						WordSong count = new WordSong();
						count.setTimeStart(iTimeStart);
						count.setTimeLenght(iDelta);
						iTimeLen -= iDelta;
						if (i < lyricEvents.size() - 1) {
							count.setWord("0");
							detail.addCountStart(count);
						} else {
							count.setWord("00"); //end song					
							detail.addCountStart(count);
						}

						//update 2
						iTimeStart += iDelta;
						WordSong end = new WordSong();
						end.setTimeStart(iTimeStart);
						end.setTimeLenght(iTimeLen);
						end.setWord("0");
						detail.setEnd(end);
						if (i < lyricEvents.size() - 1) {
							if(detail.getSentences().size() > 0) {
								mDetails.add(detail);
				    			isParagraphEnd = true; 
							}
							detail = new SongDetail();
							isParagraphEnd = false; 
						}
						
		    		}
		    		continue; 
		    	}else if(firstChar == '@') {
		    		char nxtChar = text.charAt(1); 
		    		if(nxtChar == 'm') {
		    			lineHeader = "LINM"; 
		    		}else if(nxtChar == 'w') {
		    			lineHeader = "LINW"; 
		    		}else if(nxtChar == 'c') {
		    			lineHeader = "LINA"; 
		    		}else {
		    			lineHeader = "LINN"; 
		    		}
		    		text = text.substring(2); 
		    	}
		    	
		    	if(isLineStart == false) {
		    		sen = new SentenceSong(); 
		    		lineText = ""; 
		    		lineStartTime = startTime; 
		    		lineDuration = 0; 
		    		isLineStart = true; 
		    	}
		    	
		    	WordSong word = new WordSong();
				word.setTimeStart(startTime);
				word.setTimeLenght(duration);
				word.setWord(text);

				if (sen != null) {
					sen.addWord(word);
				}
		    	
				lineText += text; 
				lineDuration += duration; 
				lastShowTime = word.getTimeStart() + word.getTimeLenght(); 
		    }
		    
		    int startLine1 = 0; 
		    int startLine2 = 0; 
		    int offset = 0; 
		    
		    boolean isLine1 = true; 
		    boolean isStartLine2 = false; 
		    SentenceSong nxtSen = null; 
		    for (SongDetail songDetail : mDetails) {
		    	
				for(i = 0; i < songDetail.getSentences().size(); i++) {
					sen = songDetail.getSentences().get(i); 
					nxtSen = i < songDetail.getSentences().size() - 1 ? songDetail.getSentences().get(i+1) : null;
					
					if(i == 0) {
						// Start show line
						WordSong countStart = songDetail.getCountStarts().get(0);
						int timeDiff = sen.getTimeStart() - countStart.getTimeStart(); 
						if(timeDiff <= 0) timeDiff = 0; 
						else if(timeDiff > 5000) timeDiff = 5000; 
						
			            sen.setTimeStart(sen.getTimeStart() - timeDiff);
			            sen.setTimeLenght(sen.getTimeLenght() + timeDiff);
			            
			            if(nxtSen != null)
			            	startLine1 = nxtSen.getTimeStart();  
			            isStartLine2 = true;
			            startTime = sen.getTimeStart();
			            isLine1 = false;
			            continue;
					}
					
					if (isStartLine2) {
			            isStartLine2 = false;
			            offset = sen.getTimeStart() - startTime;
			            sen.setTimeStart(startTime);
			            sen.setTimeLenght(sen.getTimeLenght() + offset);
			            
			            if(nxtSen != null)
			            	startLine2 = nxtSen.getTimeStart(); 
			            isLine1 = true;
			            continue;
			        }
			        
			        if (isLine1) {
			            offset = sen.getTimeStart() - startLine1;
			            sen.setTimeStart(startLine1);
			            sen.setTimeLenght(sen.getTimeLenght() + offset);
			            
			            if(nxtSen != null)
			            	startLine1 = nxtSen.getTimeStart(); 
			            
			            isLine1 = false;
			        } else {
			            offset = sen.getTimeStart() - startLine2;
			            sen.setTimeStart(startLine2);
			            sen.setTimeLenght(sen.getTimeLenght() + offset);
			            
			            if(nxtSen != null)
			            	startLine2 = nxtSen.getTimeStart(); 
			            
			            isLine1 = true;
			        }

				}
		    }
	}
	
	public void parseLyricWithDecrypt(Document document, Song currentSong, int midiShifTime) throws SecurityException {
		try {
			
			Element nodeTitle = (Element) document.getElementsByTagName("title").item(0);
			//MyLog.d(TAG, "parseLyricWithDecrypt="+document);
			NodeList nodeTitleItems = nodeTitle.getElementsByTagName("item");
			//MyLog.d(TAG, "parseLyricWithDecrypt1="+nodeTitleItems);
			int index = 0;
			
			for (int i = 0; i < nodeTitleItems.getLength(); i++) {
				Element elementItem = (Element) nodeTitleItems.item(i);				
				String header = elementItem.getAttribute("header");
				//MyLog.d(TAG, "parseLyricWithDecrypt2==header=="+header);
				if(header==null||header.equals(""))continue;
				String content = elementItem.getChildNodes().item(0).getNodeValue();// no trim here ...otherwise will make error
				//MyLog.d(TAG, "parseLyricWithDecrypt2==content="+content);
				if (header.equals("INDX")) {
					index = Integer.parseInt(content);
				}

				if (header.equals("TITL")) {
					mSongTitle = content;
				}
				if (header.equals("AUTH")) {
					mSongAuthor = content;
				}
				if (header.equals("SING")) {
					mSongSinger = content;
				}
				if (header.equals("MELO")) {}
				if (header.equals("TYPE")) {}
				if (header.equals("RHTM")) {}
				if (header.equals("TONE")) {}
				if (header.equals("KEY")) {}
				if (header.equals("LYRS")) {}
			}	
			
			DecryptLyric.getWordSize(index);
			
			Element nodeLyrics = (Element) document.getElementsByTagName("lyrics").item(0);
			NodeList nodeLyricsItems = nodeLyrics.getElementsByTagName("i");
			mDetails = new ArrayList<SongDetail>();
			SentenceSong sen = null;
			SongDetail detail = new SongDetail();
			mDetails.add(detail);

			int itemCount = 0;

			for (int i = 0; i < nodeLyricsItems.getLength(); i++) {
				Element elementItem = (Element) nodeLyricsItems.item(i);
				String header = elementItem.getAttribute("h");

				int startTime = Integer.parseInt(elementItem.getAttribute("a"));				
				int lengthTime = Integer.parseInt(elementItem.getAttribute("b"));
				String tempText = elementItem.getChildNodes().item(0).getNodeValue(); // no trim here ...otherwise will make error
				
				tempText = DecryptLyric.getStringValue(tempText, lengthTime);

				startTime = DecryptLyric.getInt32(startTime) * 10;
//				if(MyApplication.intSvrModel != MyApplication.SONCA){
//					if(currentSong.getMediaType()==MEDIA_TYPE.MIDI){
//						startTime *= 0.99;					
//					}
//				}
				startTime -= midiShifTime;
				lengthTime = DecryptLyric.getInt32(lengthTime);				
				lengthTime -= (5 * itemCount + 3);
				lengthTime *= 10;
				
				String content = tempText;
				if (header.equals("LINW") || header.equals("LINM") || header.equals("LINN") || header.equals("LINA")) {

					if (header.equals("LINN") ) {
						header = "LINM";
					}

					detail.add(content);
					sen = new SentenceSong();
					sen.setTimeStart(startTime);
					sen.setTimeLenght(lengthTime);
					sen.setSentence(content);
					sen.setTypeSinger(header);
					if (header.equals("LINW")) {
						sen.setType(SentenceSong.TYPE_WOMAN);
					} else if (header.equals("LINM")){
						sen.setType(SentenceSong.TYPE_MAN);
					}else{
						sen.setType(SentenceSong.TYPE_ALL);
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

					//update 2
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

					if (tempText.substring(0, 1).equals(tempText.substring(0, 1).toLowerCase(AppConfig.curLocale))) {
						mLyricView += ", " + tempText.trim();
					}
					if (tempText.substring(0, 1).equals(tempText.substring(0, 1).toUpperCase(AppConfig.curLocale))) {
						mLyricView += ". " + tempText.trim();
					}
				}
				itemCount++;
			}
		} catch (Exception e) {
			throw new SecurityException();
		}
	}
	
	
	public void parseLyricWithNoDecrypt(Document document, Song currentSong) throws SecurityException {
		try {
			MyLog.d(TAG, "parseLyricWithNoDecrypt");
			Element nodeTitle = (Element) document.getElementsByTagName("title").item(0);
			NodeList nodeTitleItems = nodeTitle.getElementsByTagName("item");
			int index = 0;
			for (int i = 0; i < nodeTitleItems.getLength(); i++) {
				Element elementItem = (Element) nodeTitleItems.item(i);
				String header = elementItem.getAttribute("header");
				if(header==null||header.equals(""))continue;
				String content = elementItem.getChildNodes().item(0).getNodeValue();// no trim here ...otherwise will make error

				if (header.equals("INDX")) {
					index = Integer.parseInt(content);
				}

				if (header.equals("TITL")) {
					mSongTitle = content;
				}
				if (header.equals("AUTH")) {
					mSongAuthor = content;
				}
				if (header.equals("SING")) {
					mSongSinger = content;
				}
				if (header.equals("MELO")) {}
				if (header.equals("TYPE")) {}
				if (header.equals("RHTM")) {}
				if (header.equals("TONE")) {}
				if (header.equals("KEY")) {}
				if (header.equals("LYRS")) {}
			}

			//DecryptLyric.getWordSize(index);

			Element nodeLyrics = (Element) document.getElementsByTagName("lyrics").item(0);
			NodeList nodeLyricsItems = nodeLyrics.getElementsByTagName("i");
			mDetails = new ArrayList<SongDetail>();
			SentenceSong sen = null;
			SongDetail detail = new SongDetail();
			mDetails.add(detail);

			int itemCount = 0;

			for (int i = 0; i < nodeLyricsItems.getLength(); i++) {
				Element elementItem = (Element) nodeLyricsItems.item(i);
				String header = elementItem.getAttribute("h");

				int startTime = Integer.parseInt(elementItem.getAttribute("a"));
				int lengthTime = Integer.parseInt(elementItem.getAttribute("b"));
				String tempText = elementItem.getChildNodes().item(0).getNodeValue(); // no trim here ...otherwise will make error
				
				//tempText = DecryptLyric.getStringValue(tempText, lengthTime);

				//startTime = DecryptLyric.getInt32(startTime) * 10;
				//lengthTime = DecryptLyric.getInt32(lengthTime);
				startTime *=   10;
				//lengthTime -= (5 * itemCount + 3);
				lengthTime *= 10;

				String content = tempText;
				if (header.equals("LINW") || header.equals("LINM") || header.equals("LINN") || header.equals("LINA")) {

					if (header.equals("LINN") || header.equals("LINA")) {
						header = "LINM";
					}

					detail.add(content);
					sen = new SentenceSong();
					sen.setTimeStart(startTime);
					sen.setTimeLenght(lengthTime);
					sen.setSentence(content);
					sen.setTypeSinger(header);
					if (header.equals("LINW")) {
						sen.setType(SentenceSong.TYPE_WOMAN);
					} else if (header.equals("LINM")){
						sen.setType(SentenceSong.TYPE_MAN);
					}else{
						sen.setType(SentenceSong.TYPE_ALL);
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

					//update 2
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

					if (tempText.substring(0, 1).equals(tempText.substring(0, 1).toLowerCase(AppConfig.curLocale))) {
						mLyricView += ", " + tempText.trim();
					}
					if (tempText.substring(0, 1).equals(tempText.substring(0, 1).toUpperCase(AppConfig.curLocale))) {
						mLyricView += ". " + tempText.trim();
					}
				}
				itemCount++;
			}
		} catch (Exception e) {
			throw new SecurityException();
		}
	}
	
	public void prepare(TextView lblLine1) {
		for (int i = 0; i < mDetails.size(); i++) {
			SongDetail detail = mDetails.get(i);
			for (int j = 0; j < detail.getSentences().size(); j++) {
				ArrayList<WordSong> words = detail.getSentences().get(j).getWords();

				//set width to line
				detail.getSentences().get(j).setWidthOfText(getWidthOfText(detail.getSentences().get(j).getSentence(), lblLine1));

				//set width to words
				float lineCurSize = 0;
				for (int k = 0; k < words.size(); k++) {
					words.get(k).setSizeStart(lineCurSize);
					float length = getWidthOfText(words.get(k).getWord(), lblLine1);
					words.get(k).setSizeLength(length);
					lineCurSize += length;
				}
			}
		}
	}

	protected float getWidthOfText(String text, TextView lblTextView) {
		lblTextView.setText(text);
		lblTextView.measure(0, 0);
		return lblTextView.getMeasuredWidth() - (lblTextView.getPaddingLeft() + lblTextView.getPaddingRight());
	}
	
}
