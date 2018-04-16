package com.moonbelly.youtubeFrag;

public class MyYouTubeInfo {

	private int sort;
	private String videoId = "";
	private String title = "";
	private String imageLink = "";
	private String channelTitle = "";

	private long duration; // in ms
	private String strDur = "";

	private String youtubeLink = "";

	private boolean realYoutube;

	private long viewerCount;
	private String strViewCount = "";

	public MyYouTubeInfo() {
		super();
	}

	public MyYouTubeInfo(String videoId, String title, String imageLink,
			String channelTitle) {
		this.videoId = videoId;
		this.title = title;
		this.imageLink = imageLink;
		this.channelTitle = channelTitle;
		this.realYoutube = true;
	}

	public String getStrViewCount() {
		return strViewCount;
	}

	public void setStrViewCount(String strViewCount) {
		this.strViewCount = strViewCount;
	}

	public long getViewerCount() {
		return viewerCount;
	}

	public void setViewerCount(long viewerCount) {
		this.viewerCount = viewerCount;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getChannelTitle() {
		return channelTitle;
	}

	public void setChannelTitle(String channelTitle) {
		this.channelTitle = channelTitle;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public String getStrDur() {
		return strDur;
	}

	public void setStrDur(String strDur) {
		this.strDur = strDur;
	}

	public String getImageLink() {
		return imageLink;
	}

	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}

	public String getYoutubeLink() {
		if (!videoId.equals("")) {
			youtubeLink = "https://www.youtube.com/watch?v=" + videoId;
		}
		return youtubeLink;
	}

	public void setYoutubeLink(String youtubeLink) {
		this.youtubeLink = youtubeLink;
	}

	public boolean isRealYoutube() {
		return realYoutube;
	}

	public void setRealYoutube(boolean realYoutube) {
		this.realYoutube = realYoutube;
	}

	@Override
	public boolean equals(Object o) {
		boolean sameSame = false;

		if (o != null && o instanceof MyYouTubeInfo) {
			sameSame = this.videoId
					.equalsIgnoreCase(((MyYouTubeInfo) o).videoId);
		}

		return sameSame;
	}

	@Override
	public String toString() {
		return "MyYouTubeInfo [videoId=" + videoId + ", title=" + title
				+ ", imageLink=" + imageLink + ", channelTitle=" + channelTitle
				+ ", duration=" + duration + ", strDur=" + strDur
				+ ", youtubeLink=" + youtubeLink + "]";
	}
}
