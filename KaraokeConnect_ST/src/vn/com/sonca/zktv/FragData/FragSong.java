package vn.com.sonca.zktv.FragData;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import vn.com.sonca.Touch.Language.LanguageStore;
import vn.com.sonca.database.DBInterface;
import vn.com.sonca.database.DbHelper;
import vn.com.sonca.database.DBInstance.SearchMode;
import vn.com.sonca.database.DBInstance.SearchType;
import vn.com.sonca.params.Song;
import vn.com.sonca.utils.AppConfig.MEDIA_TYPE;
import vn.com.sonca.zktv.main.KTVMainActivity;
import vn.com.sonca.zzzzz.MyApplication;

public class FragSong extends FragData {

	private Context context;
	private int[] intIDs = null;
	@Override
	protected void OnCreateFragment(Bundle saved) {
		context = getActivity().getApplicationContext();
				
		Bundle bundle = getArguments();
		if(bundle != null){
			intTheLoai = bundle.getInt("intTheLoai", -99);
		}
		
		LanguageStore store = new LanguageStore(context); 
		ArrayList<String>langIDs = store.getListIDActive(); 
		intIDs = new int[langIDs.size()]; 
		for(int i = 0; i < langIDs.size(); i++) {
			intIDs[i] = Integer.parseInt(langIDs.get(i)); 
		}
		
	}

	@Override
	protected int loadTotalSong(String where) {
		searchData = where;

		int count = 0;
		if(intTheLoai != -99){
			if(intTheLoai == MyApplication.KTV_THELOAI_REMIX){
				count = DBInterface.DBCountTotalSongRemix(context, where , MEDIA_TYPE.ALL);
			} else {
				if(intTheLoai == DbHelper.SongType_NewVol && (MyApplication.intSvrModel == MyApplication.SONCA_KM1
						|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
						|| MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
						|| MyApplication.intSvrModel == MyApplication.SONCA_KM2 || MyApplication.intSvrModel == MyApplication.SONCA_KBX9)){
					count = DBInterface.DBCountTotalSongTypeID_KM(context, where, SearchType.SEARCH_TYPE, MEDIA_TYPE.ALL, intTheLoai);
				} else if(intTheLoai == DbHelper.SongType_China){
					intIDs = new int[1]; 
					intIDs[0] = 3; // Nhac Hoa
					count = DBInterface.DBCountTotalSong(context, intIDs, where, SearchMode.MODE_MIXED, MEDIA_TYPE.ALL);
				} else {
					count = DBInterface.DBCountTotalSongTypeID(context, where, SearchType.SEARCH_TYPE, MEDIA_TYPE.ALL, intTheLoai);
				}	
			}
			
		} else {
			count = DBInterface.DBCountTotalSong(context, 
					intIDs, where, SearchMode.MODE_MIXED, MEDIA_TYPE.ALL);
		}
		
		return count;	
		
	}
	
	@Override
	protected Cursor loadSongData(String where, int offset, int sum) {
		searchData = where;
		
		if(intTheLoai != -99){
			if(intTheLoai == MyApplication.KTV_THELOAI_REMIX){
				return DBInterface.DBGetSongRemix(context, where , MEDIA_TYPE.ALL, offset, sum);
			} else {
				if(intTheLoai == DbHelper.SongType_NewVol && (MyApplication.intSvrModel == MyApplication.SONCA_KM1
						|| MyApplication.intSvrModel == MyApplication.SONCA_KB_OEM || MyApplication.intSvrModel == MyApplication.SONCA_KM1_WIFI
						|| MyApplication.intSvrModel == MyApplication.SONCA_KB39C_WIFI
						|| MyApplication.intSvrModel == MyApplication.SONCA_KM2 || MyApplication.intSvrModel == MyApplication.SONCA_KBX9)){
					return DBInterface.DBGetSongTypeIDCursor_KM(context, where, SearchType.SEARCH_TYPE, MEDIA_TYPE.ALL, intTheLoai, offset, sum);
				} else if(intTheLoai == DbHelper.SongType_China){
					intIDs = new int[1]; 
					intIDs[0] = 3; // Nhac Hoa
					return DBInterface.DBGetSongCursor(context, intIDs, where, 
							SearchMode.MODE_MIXED, MEDIA_TYPE.ALL, offset, sum);	
				} else {
					return DBInterface.DBGetSongTypeIDCursor(context, where, SearchType.SEARCH_TYPE, MEDIA_TYPE.ALL, intTheLoai, offset, sum);
				}	
			}
			
						
		} else {
			return DBInterface.DBGetSongCursor(context, intIDs, where, 
					SearchMode.MODE_MIXED, MEDIA_TYPE.ALL, offset, sum);	
		}		
		
	}

	@Override
	protected String getNameFrag() {
		return KTVMainActivity.SONG;
	}

}
