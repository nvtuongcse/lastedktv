package vn.com.sonca.Touch.touchcontrol;

public class TouchItemBack {

	private int position = 0;
	private String typeFrag;
	private String typeFrag2 = "";
	private String idFrag2 = "";
	private int stateSearch;
	private String search;

	public TouchItemBack(String typeFrag, String search, int stateSearch,
			int position) {
		this.typeFrag = typeFrag;
		this.stateSearch = stateSearch;
		this.search = search;
		this.typeFrag2 = "";
		this.idFrag2 = "";
		this.position = position;
	}
	
	public TouchItemBack(String typeFrag, String search, int stateSearch) {
		this.typeFrag = typeFrag;
		this.stateSearch = stateSearch;
		this.search = search;
		this.typeFrag2 = "";
		this.idFrag2 = "";
		this.position = 0;
	}

	public TouchItemBack(String typeFrag, String typeFrag2, String idFrag2,
			String search, int stateSearch, int position) {
		this.typeFrag2 = typeFrag2;
		this.idFrag2 = idFrag2;
		this.typeFrag = typeFrag;
		this.stateSearch = stateSearch;
		this.search = search;
		this.position = position;
	}
	
	public TouchItemBack(String typeFrag, String typeFrag2, String idFrag2,
			String search, int stateSearch) {
		this.typeFrag2 = typeFrag2;
		this.idFrag2 = idFrag2;
		this.typeFrag = typeFrag;
		this.stateSearch = stateSearch;
		this.search = search;
		this.position = 0;
	}

	public String getTypeFrag() {
		return typeFrag;
	}

	public void setTypeFrag(String typeFrag) {
		this.typeFrag = typeFrag;
	}

	public int getStateSearch() {
		return stateSearch;
	}

	public void setStateSearch(int stateSearch) {
		this.stateSearch = stateSearch;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public TouchItemBack copyValue() {
		TouchItemBack itemBack = new TouchItemBack(this.getTypeFrag(),
				this.getTypeFrag2(), this.getIdFrag2(), this.getSearch(),
				this.getStateSearch(), this.getPosition());
		return itemBack;
	}

	public String getTypeFrag2() {
		return typeFrag2;
	}

	public void setTypeFrag2(String typeFrag2) {
		this.typeFrag2 = typeFrag2;
	}

	public String getIdFrag2() {
		return idFrag2;
	}

	public void setIdFrag2(String idFrag2) {
		this.idFrag2 = idFrag2;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

}
