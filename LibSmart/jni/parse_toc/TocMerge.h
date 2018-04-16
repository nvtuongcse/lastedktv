//
//  TocMerge.h
//  SK9nTOCMerge
//
//  Created by BHDMinh on 11/26/15.
//  Copyright (c) 2015 SoncaMedia. All rights reserved.
//

#ifndef __SK9nTOCMerge__TocMerge__
#define __SK9nTOCMerge__TocMerge__

#include <iostream>
#include <vector>
#include "TocDefine.h"
#include "KarToc.h"

using namespace std;

#define INSERT_TOC_OEM	0

enum UPDATE_TYPE { UPDATE_TYPE_DISC, UPDATE_TYPE_SCDISC, UPDATE_TYPE_XUSER, UPDATE_TYPE_USER, UPDATE_TYPE_YOUTUBE };

class TOCMerge
{
private:
    int maxMegVol;
    int maxMegMid;

    KarToc *mainToc;
    KarToc *subToc;
    KarToc *updateToc;
    
    int merge_singer_info(KarToc *toc);
    int merge_songInfo(KarToc *toc);
    int merge_song_megvol_mid(const char *subIdxPath, const char *updateIdxPath, int megvol, int megmid, KarToc *main, KarToc *sub);
    
    uint32_t do_insert_singer(KarToc *toc, Person_Info_S *artistInfo, uint32_t _index);
    int find_singer_index(KarToc *toc, char *pName, int startIdx, uint8_t lang_type);
    
    void do_delete_song(KarToc *toc, Song_Info_S * songInfo, uint32_t index);
    void do_delete_song_update(Song_Info_S * songInfo, uint32_t index);
    int find_song_index(KarToc *toc, char *pName, int startIdx, uint8_t lang_type);
    uint32_t do_insert_song(KarToc *toc, Song_Info_S *songInfo, uint32_t index);

    int prepare_merge_update(KarToc *toc, const char* mainMegIdx, const char *subMegIdx, const char *uMegIdx);
    int prepare_merge(KarToc *toc, const char* orgMegIdx, const char *uMegIdx);
    int prepare_merge_toc(KarToc *toc, KarToc *uptoc, KarToc *tmptoc);
    void prepare_write_file(KarToc *toc, int type);
    void write_file(KarToc *toc, const char * filePath);

    int prepare_insert_song(KarToc *toc, KarToc *tmptoc, Song_Info_Update_S *updateInfo, int size, Person_Info_Update_S *pArtist, int artistCount);
    int insert_song(KarToc *toc, Song_Info_Update_S *songInfo, int size, Person_Info_Update_S *pArtistInfo, uint32_t artistCount);
    uint32_t do_insert_song_info(KarToc *toc, Song_Info_Update_S *updateInfo, uint32_t index);

    void delete_song(KarToc *toc, Song_Info_Update_S *updateInfo, int songCount);

    int insert_artist(KarToc *toc, Person_Info_Update_S *pArtist, int artistCount);
    uint32_t do_insert_artist_info(KarToc *toc, Person_Info_Update_S *pArtistInfo, uint32_t index);

    int do_insert_lyric(const char *pMainLyric, const char *pSubLyric, Song_Info_Update_S *updateInfo, int songcount);
public:
    TOCMerge();
    ~TOCMerge();

    void finalize_process(); 
    int merge_toc_update(const char* mainPath, const char *subPath, const char * upPath, int type);
    int merge_toc(const char* orgMegIdx, const char *uMegIdx, const char *oMegIdx, int type);
    int merge_lyric_file(const char *orgF10W, const char * uF10W, const char * oF10W);
    int merge_lyric_file1(const char *orgF10W, const char * uF10W);

    int insert_update_song(const char *mainPath, const char *subPath, Song_Info_Update_S *songInfo, int size, Person_Info_Update_S *pArtist, int artistCount, int type);

    int delete_update_song(const char *mainPath, Song_Info_Update_S *songInfo, int songCount);

    int rename_megvol_sub(const char *subPath, int maxIdx, int maxVol);
    int set_data_update_version(const char *subPath, int volDisc, int volXUser, int volUser);
};

#endif /* defined(__SK9nTOCMerge__TocMerge__) */
