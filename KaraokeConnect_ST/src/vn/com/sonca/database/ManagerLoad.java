package vn.com.sonca.database;

import java.util.ArrayList;

public class ManagerLoad {
	
//	private String TAB = "ManagerLoad";
	private ArrayList<ItemLoad> listOffset;
//	private int sumDatabase;
	private int sumPage;
	
	public ManagerLoad(int sumPage , int sumDatabase) {
		listOffset = new ArrayList<ItemLoad>();
//		this.sumDatabase = sumDatabase;
		this.sumPage = sumPage;
		int page = sumDatabase/sumPage;
		for (int i = 0; i < page; i++) {
			ItemLoad itemLoad = new ItemLoad(i*sumPage , sumPage);
			listOffset.add(itemLoad);
		}
		if(listOffset.size() != 0){
			listOffset.get(page - 1).setSum(sumPage + sumDatabase%sumPage);
		}else{
			ItemLoad itemLoad = new ItemLoad(0 , sumDatabase);
			listOffset.add(itemLoad);
		}
/*		
		for (int i = 0; i < listOffset.size(); i++) {
			ItemLoad itemLoad = listOffset.get(i);
//			MyLog.d("ManagerLoad", itemLoad.getOffset() + " : " + itemLoad.getSum());
		}
*/		
	}
	
	public int getSizeManagerLoad(){
		return listOffset.size();
	}
	
	public void removeNext(ItemLoad itemLoad){
		listOffset.remove(itemLoad);
	}

	public ItemLoad getNextManagerLoad(){
		if(!listOffset.isEmpty()){
			ItemLoad out = listOffset.get(0);
			return out;
		}else{
			return null;
		}
	}
	
	public ItemLoad[] getPositionLoad(int firstVisibleItem){
		ItemLoad[] itemLoads = new ItemLoad[]{null , null};
		if(listOffset.size() > 1){
			for (int i = 0; i < listOffset.size(); i++) {
				ItemLoad item = listOffset.get(i);
				int first = item.getOffset();
				int end = first + item.getSum();
				if(firstVisibleItem >= first && firstVisibleItem < end){
					itemLoads[0] = item;
					int deltaOffset = first + sumPage/2 - firstVisibleItem;
					if(deltaOffset > 0){
						try{
							itemLoads[1] = listOffset.get(i + 1);
						}catch(IndexOutOfBoundsException ex){
							itemLoads[1]  = null;
						}
					}else if(deltaOffset < 0){
						try{
							itemLoads[1] = listOffset.get(i - 1);
						}catch(IndexOutOfBoundsException ex){
							itemLoads[1]  = null;
						}
					}else{
						itemLoads[1]  = null;
					}
					return itemLoads;
				}
			}
		}else if(listOffset.size() == 1){
			itemLoads[0] = listOffset.get(0);
			itemLoads[1] = null;
		}else{
			itemLoads[0] = null;
			itemLoads[1] = null;
		}
		return itemLoads;
	}

	
	
}
