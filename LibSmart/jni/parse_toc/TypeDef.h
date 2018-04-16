//
//  TypeDef.h
//  VidiaKaraokeList
//
//  Created by BHDMinh on 9/7/14.
//  Copyright (c) 2014 Vidia. All rights reserved.
//

#ifndef VidiaKaraokeList_TypeDef_h
#define VidiaKaraokeList_TypeDef_h

typedef enum {
    SCCompareNotEqual = -1,
    SCComparePinyin = 0,
    SCCompareTitleRaw = 1,
    SCCompareName = 2,
    SCCompareSingerName = 3,
    SCCompareSingerRaw = 4,
    SCCompareSingerPinyin = 5,
    SCCompareSongID = 6,
    SCCompareNumWords = 7,
    SCCompareSongID5 = 8,
    SCCompareSongID6Contain = 9,
    SCCompareSongID5Contain = 10,
}SCCompareResult;

typedef enum {
    SCSearchSong = 0,
    SCSearchSongID = 1,
    SCSearchSinger = 2,
    SCSearchMusician = 3,
    SCSearchSongType = 4,
    SCSearchSongSinger = 5,
}SCSearchType;

typedef enum {
    SCFilterNone = 0,
    SCFilterMIDI = 1,
    SCFilterVideo = 2,
    SCFilterNumWords = 3,
    
    SCFilterLangVietNamese = 4,
    SCFilterLangOther = 5,
    SCFilterLangNone = 6,
}SCTypeFilter;


typedef enum
{
    LoadPicture,
    LoadNewToc,
    LoadNewAppVersion,
    LoadNewLyric,
    LoadNewTocHIW, 
}ServerUpdateType;

#endif
