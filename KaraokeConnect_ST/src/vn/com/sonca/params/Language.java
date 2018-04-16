package vn.com.sonca.params;

import java.util.ArrayList;

import vn.com.sonca.utils.AppConfig.LANG_INDEX;

public class Language {

	private int id;
	private String name;
	private String shortName;
	private String mTitleRaw; 
	private ArrayList<Song> mSongs; 
	private String mCover; 
	private String font; 
	private int Sort;
	private LANG_INDEX index; 
	private boolean active;
	public Language (){
	}	
	public Language (String name){
		this.name = name; 
	}
	public int getID() {
		return id;
	}
	public void setID(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
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

	public String getCover() {
		return mCover;
	}

	public void setCover(String mCover) {
		this.mCover = mCover;
	}

	public void setFont(String font) {
		this.font = font; 
	}
	
	public String getFont() {
		return this.font; 
	}

	public int getSort() {
		return Sort;
	}

	public void setSort(int sort) {
		Sort = sort;
	}

	public LANG_INDEX getIndex() {
		return index;
	}

	public void setIndex(LANG_INDEX index) {
		this.index = index;
	}
	public boolean isActive() {
		return active;
	}
	public void setActive(boolean active) {
		this.active = active;
	}
}
