// Copyright 2012 Google Inc. All Rights Reserved.

package com.moonbelly.youtube;

import java.util.ArrayList;

import vn.com.sonca.zzzzz.MyApplication;

/**
 * Static container class for holding a reference to your YouTube Developer Key.
 */
public class DeveloperKey {

  /**
   * Please replace this with a valid API key which is enabled for the 
   * YouTube Data API v3 service. Go to the 
   * <a href="https://code.google.com/apis/console/">Google APIs Console</a> to
   * register a new developer key.
   */
	
	public static final String DEVELOPER_KEY_EXTRA1 = "AIzaSyBQ9dEzJtZAQ29FKFOqci8-gABjuwvWOGc"; // main
	public static final String DEVELOPER_KEY_EXTRA2 = "AIzaSyCgDrSIeMOx0gnDxrGALdtjpRtOZLBsasU";
	public static final String DEVELOPER_KEY_EXTRA3 = "AIzaSyCMtBvTx01HFkj8cyn0jNRM5PMot4Omcd4";	
	public static final String DEVELOPER_KEY_MHTouch1 = "AIzaSyCesL6Yv9xEb5Vma4m9bJ_DFsKoh6bS-fY"; 
	public static final String DEVELOPER_KEY_MHTouch2 = "AIzaSyCT3UudxMWb4uIfV9-v6MJceza1GEFuuTI";
	public static final String DEVELOPER_KEY_TABLET1 = "AIzaSyB7ijvraWeOiNtXVyqlkwCzf2B1b1BR6Dk";
	public static final String DEVELOPER_KEY_TABLET2 = "AIzaSyBbRtzBcfhp0FkhXAvqgcchOC9G35nwV1Y";
	public static final String DEVELOPER_KEY_PHONE1 = "AIzaSyAhZf9S69OYKlJvSE5R0km6FPm8GVL6CCo";
	public static final String DEVELOPER_KEY_PHONE2 = "AIzaSyA3LjKH7bfVOZmn2vbZZuDleuTX6-QoWHM";
	
	public static ArrayList<String> listKeyAPI = new ArrayList<String>();
	
	public static int totalKeyBackUp = 8;
	
	public static int countKey = 0;
	public static String getAPIKey(){
		if(listKeyAPI.size() > 0){
			try {
				return listKeyAPI.get(countKey);	
			} catch (Exception e) {
				
			}
			return DEVELOPER_KEY_EXTRA1;
		}
		
		switch (countKey) {
		case 1:
			return DEVELOPER_KEY_EXTRA2;
		case 2:
			return DEVELOPER_KEY_EXTRA3;
		case 3:
			return DEVELOPER_KEY_MHTouch1;
		case 4:
			return DEVELOPER_KEY_MHTouch2;
		case 5:
			return DEVELOPER_KEY_TABLET1;
		case 6:
			return DEVELOPER_KEY_TABLET2;
		case 7:
			return DEVELOPER_KEY_PHONE1;
		case 8:
			return DEVELOPER_KEY_PHONE2;
		default:
			return DEVELOPER_KEY_EXTRA1;
		}
	}
	
	public static void switchAPIKey(){
		countKey++;
		if(countKey > totalKeyBackUp){
			countKey = 0;
		}
	}
	
	public static String getSafeSearchType(){
		if(MyApplication.intSearchYouTubeMode == 0){
			return "strict";
		}
		
		if(MyApplication.intSearchYouTubeMode == 1){
			return "moderate";
		}
		
		if(MyApplication.intSearchYouTubeMode == 2){
			return "none";
		}
		
		return "strict";
	}

}
