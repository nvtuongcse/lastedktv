package vn.com.sonca.params;

import java.util.ArrayList;

public class SideItem {
	private String title; 
	private Integer tag; 
	private Integer imageId; 
	private ArrayList<SideItem> items;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getTag() {
		return tag;
	}
	public void setTag(Integer tag) {
		this.tag = tag;
	}
	public Integer getImageId() {
		return imageId;
	}
	public void setImageId(Integer imageId) {
		this.imageId = imageId;
	}
	public ArrayList<SideItem> getItems() {
		return items;
	}
	public void setItems(ArrayList<SideItem> items) {
		this.items = items;
	} 
	
	public SideItem()
	{
		
	}
	
	public SideItem(String title, int image, int tag) {
		this.title = title; 
		this.imageId = image; 
		this.tag = tag; 
	}
}
