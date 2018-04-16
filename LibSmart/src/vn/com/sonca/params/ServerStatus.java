package vn.com.sonca.params;

import java.lang.reflect.Array;
import java.util.ArrayList;

import android.util.Log;

public class ServerStatus {
	private int danceMode = -1; 
	private int playBackState; 
	private boolean muted; 
	private boolean singerOn; 
	private int curScore; 
	private int curTone; 
	private int curMelody; 
	private int curTempo;
	private int curKey; 
	private int curVolume; 
	private int numSongCount;
	private ArrayList<SongID> listReservedSong; 
	private int mediaType = -1; 
	private int playingSongTypeABC; 
	private int playingSongID; 
	private String playingSongName = "";
	private int userCaption;
	private boolean captionAPIValid;

	private boolean flagOffUserList; 
	private boolean flagOffFirst;
	private boolean flagOffVolumn;
	private boolean flagOffKey;
	private boolean flagOffTempo;
	private boolean flagOffTone;
	private boolean flagOffMelody;
	private boolean flagOffDefault;
	private boolean flagOffSinger;
	private boolean flagOffScore;
	private boolean flagOffRepeat;
	private boolean flagOffNext;
	private boolean flagOffDance;
	private boolean flagOffPlayPause;
	
	private int onOffControlFullValue;
	private boolean onOffControlFullAPI;
	
	private int volset_default;
	private int volset_offsetMidi;
	private int volset_offsetKTV;
	private int volset_dance;
	private int volset_piano;
	private int volset_melody;	
	private int volset_offsetKey;
	
	private int playingMode;
	
	private int midiShiftTime;
	
	private boolean modelA;
	private boolean onHiW;	

	private boolean isDownloadingYouTube;
	private boolean isAutoDownloadYouTube;
	
	private boolean isPlayingYouTube;	
	private int youtubeDownloadID = 0;
	private int youtubeDownloadPercent = 0;
	private int youtubeDownloadRemain = 0;
	private int playingState = 0;	
	
	private int realYoutubeCount = 0;
	private int countAddFile = 0;
	private ArrayList<RealYouTubeInfo> realYoutubeList = new ArrayList<RealYouTubeInfo>();
	
	private int currentLucky = 0;
	private boolean isPlayingLucky = false;
	private boolean flagSupportLuckyRoll = false;
	
	private int versionListHidden = 0;
	private int versionListDownload = 0;
	
	private byte[] versionListDownloadByte = new byte[32];
	
	public byte[] getVersionListDownloadByte() {
		return versionListDownloadByte;
	}

	public void setVersionListDownloadByte(byte[] versionListDownloadByte) {
		this.versionListDownloadByte = versionListDownloadByte;
	}
	
	public int getVersionListDownload() {
		return versionListDownload;
	}

	public void setVersionListDownload(int versionListDownload) {
		this.versionListDownload = versionListDownload;
	}

	public int getVersionListHidden() {
		return versionListHidden;
	}

	public void setVersionListHidden(int versionListHidden) {
		this.versionListHidden = versionListHidden;
	}

	public boolean isFlagSupportLuckyRoll() {
		return flagSupportLuckyRoll;
	}

	public void setFlagSupportLuckyRoll(boolean flagSupportLuckyRoll) {
		this.flagSupportLuckyRoll = flagSupportLuckyRoll;
	}

	public int getCurrentLucky() {
		return currentLucky;
	}

	public void setCurrentLucky(int currentLucky) {
		this.currentLucky = currentLucky;
	}

	public boolean isPlayingLucky() {
		return isPlayingLucky;
	}

	public void setPlayingLucky(boolean isPlayingLucky) {
		this.isPlayingLucky = isPlayingLucky;
	}
	
	public int getCountAddFile() {
		return countAddFile;
	}

	public void setCountAddFile(int countAddFile) {
		this.countAddFile = countAddFile;
	}

	public int getRealYoutubeCount() {
		return realYoutubeCount;
	}

	public void setRealYoutubeCount(int realYoutubeCount) {
		this.realYoutubeCount = realYoutubeCount;
	}

	public ArrayList<RealYouTubeInfo> getRealYoutubeList() {
		return realYoutubeList;
	}

	public void setRealYoutubeList(ArrayList<RealYouTubeInfo> realYoutubeList) {
		this.realYoutubeList = realYoutubeList;
	}

	public int getPlayingState() {
		return playingState;
	}

	public void setPlayingState(int playingState) {
		this.playingState = playingState;
	}
	
	public int getYoutubeDownloadRemain() {
		return youtubeDownloadRemain;
	}

	public void setYoutubeDownloadRemain(int youtubeDownloadRemain) {
		this.youtubeDownloadRemain = youtubeDownloadRemain;
	}

	public int getYoutubeDownloadPercent() {
		return youtubeDownloadPercent;
	}

	public void setYoutubeDownloadPercent(int youtubeDownloadPercent) {
		this.youtubeDownloadPercent = youtubeDownloadPercent;
	}

	public boolean isAutoDownloadYouTube() {
		return isAutoDownloadYouTube;
	}

	public void setAutoDownloadYouTube(boolean isAutoDownloadYouTube) {
		this.isAutoDownloadYouTube = isAutoDownloadYouTube;
	}

	public boolean isDownloadingYouTube() {
		return isDownloadingYouTube;
	}

	public void setDownloadingYouTube(boolean isDownloadingYouTube) {
		this.isDownloadingYouTube = isDownloadingYouTube;
	}

	public int getYoutubeDownloadID() {
		return youtubeDownloadID;
	}

	public void setYoutubeDownloadID(int youtubeDownloadID) {
		this.youtubeDownloadID = youtubeDownloadID;
	}

	public boolean isPlayingYouTube() {
		return isPlayingYouTube;
	}

	public void setPlayingYouTube(boolean isPlayingYouTube) {
		this.isPlayingYouTube = isPlayingYouTube;
	}

	public boolean isModelA() {
		return modelA;
	}

	public void setModelA(boolean modelA) {
		this.modelA = modelA;
	}

	public boolean isOnHiW() {
		return onHiW;
	}

	public void setOnHiW(boolean onHiW) {
		this.onHiW = onHiW;
	}

	public int getMidiShiftTime() {
		return midiShiftTime;
	}

	public void setMidiShiftTime(int midiShiftTime) {
		this.midiShiftTime = midiShiftTime;
	}

	public int getPlayingMode() {
		return playingMode;
	}

	public void setPlayingMode(int playingMode) {
		this.playingMode = playingMode;
	}

	public int getVolset_offsetKey() {
		return volset_offsetKey;
	}

	public void setVolset_offsetKey(int volset_offsetKey) {
		this.volset_offsetKey = volset_offsetKey;
	}

	public int getVolset_default() {
		return volset_default;
	}

	public void setVolset_default(int volset_default) {
		this.volset_default = volset_default;
	}

	public int getVolset_offsetMidi() {
		return volset_offsetMidi;
	}

	public void setVolset_offsetMidi(int volset_offsetMidi) {
		this.volset_offsetMidi = volset_offsetMidi;
	}

	public int getVolset_offsetKTV() {
		return volset_offsetKTV;
	}

	public void setVolset_offsetKTV(int volset_offsetKTV) {
		this.volset_offsetKTV = volset_offsetKTV;
	}

	public int getVolset_dance() {
		return volset_dance;
	}

	public void setVolset_dance(int volset_dance) {
		this.volset_dance = volset_dance;
	}

	public int getVolset_piano() {
		return volset_piano;
	}

	public void setVolset_piano(int volset_piano) {
		this.volset_piano = volset_piano;
	}

	public int getVolset_melody() {
		return volset_melody;
	}

	public void setVolset_melody(int volset_melody) {
		this.volset_melody = volset_melody;
	}

	public boolean isOnOffControlFullAPI() {
		return onOffControlFullAPI;
	}

	public void setOnOffControlFullAPI(boolean onOffControlFullAPI) {
		this.onOffControlFullAPI = onOffControlFullAPI;
	}

	public int getOnOffControlFullValue() {
		return onOffControlFullValue;
	}

	public void setOnOffControlFullValue(int onOffControlFullValue) {
		this.onOffControlFullValue = onOffControlFullValue;
	}

	public boolean isFlagOffFirst() {
		return flagOffFirst;
	}

	public void setFlagOffFirst(boolean flagOffFirst) {
		this.flagOffFirst = flagOffFirst;
	}

	public boolean isFlagOffVolumn() {
		return flagOffVolumn;
	}

	public void setFlagOffVolumn(boolean flagOffVolumn) {
		this.flagOffVolumn = flagOffVolumn;
	}

	public boolean isFlagOffKey() {
		return flagOffKey;
	}

	public void setFlagOffKey(boolean flagOffKey) {
		this.flagOffKey = flagOffKey;
	}

	public boolean isFlagOffTempo() {
		return flagOffTempo;
	}

	public void setFlagOffTempo(boolean flagOffTempo) {
		this.flagOffTempo = flagOffTempo;
	}

	public boolean isFlagOffTone() {
		return flagOffTone;
	}

	public void setFlagOffTone(boolean flagOffTone) {
		this.flagOffTone = flagOffTone;
	}

	public boolean isFlagOffMelody() {
		return flagOffMelody;
	}

	public void setFlagOffMelody(boolean flagOffMelody) {
		this.flagOffMelody = flagOffMelody;
	}

	public boolean isFlagOffDefault() {
		return flagOffDefault;
	}

	public void setFlagOffDefault(boolean flagOffDefault) {
		this.flagOffDefault = flagOffDefault;
	}

	public boolean isFlagOffSinger() {
		return flagOffSinger;
	}

	public void setFlagOffSinger(boolean flagOffSinger) {
		this.flagOffSinger = flagOffSinger;
	}

	public boolean isFlagOffScore() {
		return flagOffScore;
	}

	public void setFlagOffScore(boolean flagOffScore) {
		this.flagOffScore = flagOffScore;
	}

	public boolean isFlagOffRepeat() {
		return flagOffRepeat;
	}

	public void setFlagOffRepeat(boolean flagOffRepeat) {
		this.flagOffRepeat = flagOffRepeat;
	}

	public boolean isFlagOffNext() {
		return flagOffNext;
	}

	public void setFlagOffNext(boolean flagOffNext) {
		this.flagOffNext = flagOffNext;
	}

	public boolean isFlagOffDance() {
		return flagOffDance;
	}

	public void setFlagOffDance(boolean flagOffDance) {
		this.flagOffDance = flagOffDance;
	}

	public boolean isFlagOffPlayPause() {
		return flagOffPlayPause;
	}

	public void setFlagOffPlayPause(boolean flagOffPlayPause) {
		this.flagOffPlayPause = flagOffPlayPause;
	}

	public boolean isFlagOffUserList() {
		return flagOffUserList;
	}

	public void setFlagOffUserList(boolean flagOffUserList) {
		this.flagOffUserList = flagOffUserList;
	}

	public void setPlayBackState(int playBackState) {
		this.playBackState = playBackState;
	} 
	
	public int getPlayBackState()
	{
		return this.playBackState; 
	}
	
	public boolean isPaused()
	{
		return playBackState == 2; 
	}
	public boolean isPlaying()
	{
		return playBackState == 1; 
	}
	public boolean isStopped()
	{
		return playBackState == 0; 
	}

	public boolean isMuted() {
		return muted;
	}

	public void setMuted(boolean muted) {
		this.muted = muted;
	}

	public boolean isSingerOn() {
		return singerOn;
	}

	public void setSingerOn(boolean singerOn) {
		this.singerOn = singerOn;
	}

	public int getCurrentScore() {
		return curScore;
	}

	public void setCurrentScore(int score) {
		this.curScore = score;
	}

	public int getCurrentTone() {
		return curTone;
	}

	public void setCurrentTone(int curTone) {
		this.curTone = curTone;
	}

	public int getCurrentMelody() {
		return curMelody;
	}

	public void setCurrentMelody(int curMelody) {
		this.curMelody = curMelody;
	}

	public int getCurrentTempo() {
		return curTempo;
	}

	public void setCurrentTempo(int curTempo) {
		this.curTempo = curTempo;
	}

	public int getCurrentKey() {
		return curKey;
	}

	public void setCurrentKey(int curKey) {
		this.curKey = curKey;
	}

	public int getCurrentVolume() {
		return curVolume;
	}

	public void setCurrentVolume(int curVolume) {
		this.curVolume = curVolume;
	}

	public int getReservedSongCount() {
		return numSongCount;
	}

	public void setReservedSongCount(int numSongCount) {
		this.numSongCount = numSongCount;
	}

	public ArrayList<SongID> getReservedSongs() {
		return listReservedSong;
	}

	public void setReservedSongs(ArrayList<SongID> listReservedSong) {
		this.listReservedSong = listReservedSong;
	}

	public int getPlayingSongTypeABC() {
		return playingSongTypeABC;
	}

	public void setPlayingSongTypeABC(int typeABC) {
		this.playingSongTypeABC = typeABC;
	}

	public int getPlayingSongID() {
		return playingSongID;
	}

	public void setPlayingSongID(int playingSongID) {
		this.playingSongID = playingSongID;
	}

	public String getPlayingSongName() {
		return playingSongName;
	}

	public void setPlayingSongName(String playingSongName) {
		this.playingSongName = playingSongName;
	}

	public int getMediaType() {
		return mediaType;
	}

	public void setMediaType(int mediaType) {
		this.mediaType = mediaType;
	}

	public int danceMode() {
		return danceMode;
	}

	public void setDanceMode(int danceMode) {
		this.danceMode = danceMode;
	}

	public int getUserCaption() {
		return userCaption;
	}

	public void setUserCaption(int userCaption) {
		this.userCaption = userCaption;
	}

	public boolean isCaptionAPIValid() {
		return captionAPIValid;
	}

	public void setCaptionAPIValid(boolean captionAPIValid) {
		this.captionAPIValid = captionAPIValid;
	}
	
	public int getAdminControlValue() {
		String controlData = "0000000000000000";
		StringBuilder myData = new StringBuilder(controlData);

		if (flagOffUserList) {
			myData.setCharAt(15, '1');
		}
		if (flagOffFirst) {
			myData.setCharAt(14, '1');
		}
		if (flagOffVolumn) {
			myData.setCharAt(13, '1');
		}
		if (flagOffKey) {
			myData.setCharAt(12, '1');
		}
		if (flagOffTempo) {
			myData.setCharAt(11, '1');
		}
		if (flagOffTone) {
			myData.setCharAt(10, '1');
		}
		if (flagOffMelody) {
			myData.setCharAt(9, '1');
		}
		if (flagOffDefault) {
			myData.setCharAt(8, '1');
		}
		if (flagOffSinger) {
			myData.setCharAt(7, '1');
		}
		if (flagOffScore) {
			myData.setCharAt(6, '1');
		}
		if (flagOffRepeat) {
			myData.setCharAt(5, '1');
		}
		if (flagOffNext) {
			myData.setCharAt(4, '1');
		}
		if (flagOffDance) {
			myData.setCharAt(3, '1');
		}
		if (flagOffPlayPause) {
			myData.setCharAt(2, '1');
		}

		// Log.e("getAdminControlValue", myData.toString());
		
		return Integer.parseInt(myData.toString(), 2);
	}
}
