package vn.com.sonca.params;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

public class SongType {
	private int mID; 
	private String mName; 
	private String mShortName; 
	private String mTitleRaw; 
	private ArrayList<Song> mSongs; 
//	private String mCover; 
	private ByteArrayInputStream mImageStream; 
//	private boolean mIsFilled = false;
//	private int mPriority = 1;
//	public int mSpace[] = {-1, -1, -1, -1};
//	private int mIndex;
	
	
	public SongType (){}
	
	public SongType (int id , String name) {
		this.mID = id;
		this.mName = name;
	}
	
	public SongType (String name){
		this.mName = name; 
	}
	
	public int getID()
	{
		return mID; 
	}
	public void setID(int id) {
		mID = id; 
	}
	
	public String getName()
	{
		return this.mName; 
	}
	public void setName(String aName) {
		this.mName = aName; 
	}
	
	public String getShortName() {
		return this.mShortName; 
	}
	public void setShortName(String aName) {
		this.mShortName = aName; 
	}
	
	public String getTitleRaw() {
		return this.mTitleRaw; 
	}
	public void setTitleRaw(String aTitle) {
		this.mTitleRaw = aTitle; 
	}
	
	public ArrayList<Song> getSongs() {
		return this.mSongs; 
	}
	public void setSongs(ArrayList<Song> songs) {
		this.mSongs = songs; 
	}

//	public String getCover() {
//		return mCover;
//	}
//
//	public void setCover(String mCover) {
//		this.mCover = mCover;
//	}
//	
	public ByteArrayInputStream getCoverImageStream() {
		return mImageStream;
	}

	public void setCoverImageStream(ByteArrayInputStream mImageStream) {
		this.mImageStream = mImageStream;
	}
	
//	public int[] getSpace(){
//		return mSpace;
//	}
//	public boolean isIsFilled() {
//		return mIsFilled;
//	}
//	public void setIsFilled(boolean mIsFilled) {
//		this.mIsFilled = mIsFilled;
//	}
//	public int getPriority() {
//		return mPriority;
//	}
//	public void setPriority(int mPriority) {
//		this.mPriority = mPriority;
//	}
//	public int getIndex() {
//		return mIndex;
//	}
//	public void setIndex(int mIndex) {
//		this.mIndex = mIndex;
//	}
	
	// HMINH
	private int countTotal = 0;

	public int getCountTotal() {
		return countTotal;
	}

	public void setCountTotal(int countTotal) {
		this.countTotal = countTotal;
	}
}
