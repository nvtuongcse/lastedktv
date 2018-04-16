package vn.com.sonca.params;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import vn.com.sonca.database.DBInstance.SearchMode;

public class Musician {
	private int mID; 
	private String mName; 
	private String mShortName; 
	private String mTitleRaw; 
//	private String mCover; 
	private ArrayList<Song> mSongs; 
	private ByteArrayInputStream mImageStream; 
//	private boolean mIsFilled = false;
//	private int mPriority = 1;
//	public int mSpace[] = {-1, -1, -1, -1};
//	private int mIndex;
	private int coverid; 
	private SearchMode mode; 
	
	public Musician() {
	}
	
	public Musician(String name) {
		this.mName = name; 
	}
	
	public Musician(String name , String namecut) {
		this.namecut = namecut;
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
//	public void setCover(String mCover) {
//		this.mCover = mCover;
//	}
	
	public ByteArrayInputStream getCoverImageStream() {
		return mImageStream;
	}

	public void setCoverImageStream(ByteArrayInputStream mImageStream) {
		this.mImageStream = mImageStream;
	}

	public void setCoverID(int cid)
	{
		coverid = cid; 
	}
	
	public int getCoverID()
	{
		return coverid; 
	}
	
	public SearchMode getCompareMode() {
		return mode;
	}

	public void setCompareMode(SearchMode mode) {
		this.mode = mode;
	}

	
//	public int getPriority() {
//		return mPriority;
//	}
//	public void setPriority(int mPriority) {
//		this.mPriority = mPriority;
//	}
//	public int[] getSpace(){
//		return mSpace;
//	}
//	
//	public boolean isFilled() {
//		return mIsFilled;
//	}
//
//	public void setIsFilled(boolean mIsFilled) {
//		this.mIsFilled = mIsFilled;
//	}
//
//	public int getIndex() {
//		return mIndex;
//	}
//
//	public void setIndex(int mIndex) {
//		this.mIndex = mIndex;
//	}
	
	private String namecut = "";
	
	public String getNamecut() {
		return namecut;
	}

	public void setNamecut(String namecut) {
		this.namecut = namecut;
	}
	
}
