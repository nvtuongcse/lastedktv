package vn.com.sonca.params;

public class LyricXML implements Comparable<LyricXML> {
	
	private int id = -1;
	private long date = -1;
	private int size = -1;
	private int plus = -1;
	private String name = "";
	
	public long getDate() {
		return date;
	}
	public void setDate(long data) {
		this.date = data;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getPlus() {
		return plus;
	}
	public void setPlus(int plus) {
		this.plus = plus;
	}

	@Override
	public boolean equals(Object o) {
		if(o == null){
			return false;
		}
		return (this.plus == ((LyricXML)o).plus);
	}
	@Override
	public int compareTo(LyricXML lyricXML) {
		if(lyricXML == null){
			return -1;
		}
		if(this.plus > lyricXML.plus){
			return 1;
		}else if(this.plus == lyricXML.plus){
			return 0;
		}else if(this.plus < lyricXML.plus){
			return -1;
		} else {
			return -1;
		}
	}
	
}
