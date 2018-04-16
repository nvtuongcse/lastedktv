package vn.com.sonca.zktv.FragImage;

import java.util.ArrayList;
import java.util.List;
import vn.com.sonca.database.DBInterface;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.database.DBInstance.SearchMode;
import vn.com.sonca.params.Singer;
import vn.com.sonca.utils.AppConfig.LANG_INDEX;
import vn.com.sonca.zktv.main.KTVMainActivity;

public class FragSinger extends FragImage {

	@Override
	protected List<Object> loadDatabase(String search) {
		LANG_INDEX index = LANG_INDEX.ALL_LANGUAGE;
		ArrayList<Singer> singerList = DBInterface.DBSearchSinger(search, 
				index, SearchMode.MODE_MIXED, 0, 0, getActivity());
		List<Object> objectList = new ArrayList<Object>(singerList);
		return objectList;
	}

	@Override
	protected String getName(Object object) {
		if(object != null){
			if(object instanceof Singer){
				return ((Singer)object).getName();
			}
		}
		return "";
	}

	@Override
	protected int getCover(Object object) {
		if(object != null){
			if(object instanceof Singer){
				return ((Singer)object).getCoverID();
			}
		}
		return 0;
	}

	@Override
	protected int getID(Object object) {
		if(object != null){
			if(object instanceof Singer){
				return ((Singer)object).getID();
			}
		}
		return 0;
	}

	@Override
	protected String getNameFrag() {
		return KTVMainActivity.SINGER;
	}

	@Override
	public void OnSK90009() {
		// TODO Auto-generated method stub
		
	}

}
