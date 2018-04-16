package vn.com.sonca.zktv.FragData;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import vn.com.sonca.database.DBInterface;
import vn.com.sonca.database.DbHelper;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.database.DBInstance.SearchType;
import vn.com.sonca.utils.AppConfig.MEDIA_TYPE;
import vn.com.sonca.zktv.main.KTVMainActivity;

public class FragNhacHot extends FragData {
	
	private String TAB = "FragNhacHot";
	private Context context;

	@Override
	protected void OnCreateFragment(Bundle saved) {
		MyLog.e(TAB, "OnCreateFragment");
		context = getActivity().getApplicationContext();			}	
	
	@Override
	protected int loadTotalSong(String where) {
		searchData = where;
		int count = DBInterface.DBCountTotalSongTypeID(context, 
				where, SearchType.SEARCH_TYPE, MEDIA_TYPE.ALL, DbHelper.SongType_HotSong);
		return count;
	}

	@Override
	protected Cursor loadSongData(String where, int offset, int sum) {
		searchData = where;
		Cursor cursor = DBInterface.DBGetSongTypeIDCursor(context, 
				where, SearchType.SEARCH_TYPE, MEDIA_TYPE.ALL, 
				DbHelper.SongType_HotSong, offset, sum);
		return cursor;
	}

	@Override
	protected String getNameFrag() {
		return KTVMainActivity.HOTSONG;
	}


}
