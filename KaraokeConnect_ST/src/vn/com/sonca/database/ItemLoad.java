package vn.com.sonca.database;

public class ItemLoad {
	
	private int offset = -1;
	private int sum = -1;
	
	public ItemLoad(int offset , int sum){
		this.offset = offset;
		this.sum = sum;
	}
	public int getOffset() {
		return offset;
	}
	public int getSum() {
		return sum;
	}
	public void setSum(int sum){
		this.sum = sum;
	}

}
