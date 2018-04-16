//
//  enum_utils.h
//  CloudKaraoke-Dev
//
//  Created by BHDMinh on 2/17/14.
//  Copyright (c) 2014 soncamedia. All rights reserved.
//

#ifndef CloudKaraoke_Dev_enum_utils_h
#define CloudKaraoke_Dev_enum_utils_h

typedef enum load_task_t
{
	LOAD_VIDEO_LIST = 0,
	LOAD_VIDEO_ITEM,
	LOAD_VIDEO_SNAP,
	LOAD_FREE_LIST,
	LOAD_lYRIC,
	LOAD_AUDIO,
	LOAD_ADVT,
	LOAD_FIRST_UPDATE,
	LOAD_UPDATE,
	LOAD_CHECK_UPDATE,
	LOAD_HOT_FREE,
	LOAD_HOT_FULL,
	LOAD_ANOUNCEMENT,
	GET_USER_LOGIN,
	GET_USER_PASS,
	LOAD_FIRST_UPDATE_ZIP,
	LOAD_FINISH_UPDATE
} load_task;

#endif
