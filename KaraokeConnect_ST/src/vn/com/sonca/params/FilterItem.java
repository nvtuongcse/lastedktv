package vn.com.sonca.params;

public class FilterItem {
	private int id;
	private String kmVol;
	private String discVol;
	private String hddVol;

	public FilterItem() {
		super();
	}

	public FilterItem(int id, String kmVol, String discVol, String hddVol) {
		super();
		this.id = id;
		this.kmVol = kmVol;
		this.discVol = discVol;
		this.hddVol = hddVol;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKmVol() {
		return kmVol;
	}

	public void setKmVol(String kmVol) {
		this.kmVol = kmVol;
	}

	public String getDiscVol() {
		return discVol;
	}

	public void setDiscVol(String discVol) {
		this.discVol = discVol;
	}

	public String getHddVol() {
		return hddVol;
	}

	public void setHddVol(String hddVol) {
		this.hddVol = hddVol;
	}

}
