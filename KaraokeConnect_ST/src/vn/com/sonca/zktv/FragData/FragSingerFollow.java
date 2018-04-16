package vn.com.sonca.zktv.FragData;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import vn.com.sonca.database.DBInterface;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.database.DBInstance.SearchType;
import vn.com.sonca.utils.AppConfig.MEDIA_TYPE;
import vn.com.sonca.zktv.main.KTVMainActivity;

public class FragSingerFollow extends FragData {
	
	private String TAB = "FragSingerFollow";
	private Context context;

	@Override
	protected void OnCreateFragment(Bundle saved) {
		MyLog.e("FragSingerFollow", "OnCreateFragment");
		context = getActivity().getApplicationContext();
		Bundle bundle = getArguments();
		if(bundle != null){
			idSinger = bundle.getInt("idImage");
		}
	}	
	
	@Override
	protected int loadTotalSong(String where) {
		int count = DBInterface.DBCountTotalSongTypeID(context, 
				where, SearchType.SEARCH_SINGER, MEDIA_TYPE.ALL, idSinger);
		return count;
	}

	@Override
	protected Cursor loadSongData(String where, int offset, int sum) {
		Cursor cursor = DBInterface.DBGetSongTypeIDCursor(context, 
				where, SearchType.SEARCH_SINGER, MEDIA_TYPE.ALL, 
				idSinger, offset, sum);
		return cursor;
	}

	@Override
	protected String getNameFrag() {
		return KTVMainActivity.FOLLOW;
	}


}
