package vn.com.sonca.params;

public class RealYouTubeInfo {
	private int id;
	private String vidID;
	private String name;

	public RealYouTubeInfo(int id, String vidID, String name) {
		super();
		this.id = id;
		this.vidID = vidID;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getVidID() {
		return vidID;
	}

	public void setVidID(String vidID) {
		this.vidID = vidID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "RealYouTubeInfo [id=" + id + ", vidID=" + vidID + ", name="
				+ name + "]";
	}

}
