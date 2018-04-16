//
//  TocMerge.c
//  SK9nTOCMerge
//
//  Created by BHDMinh on 11/26/15.
//  Copyright (c) 2015 SoncaMedia. All rights reserved.
//

#include "TocMerge.h"
#include <string>
#include <stdlib.h>
#include <stdio.h>
#include "utf8.h"
#include <dirent.h>
#include <sys/stat.h>

#if (defined(DEBUG_MODE) && DEBUG_MODE)
#include "debug_log.h"
#define printf(...) __android_log_print(ANDROID_LOG_DEBUG, "TOCMerge", __VA_ARGS__);
#else
#define printf(stringFormat, ...) do{} while(0)

#endif

#define KOK_1B  (1)
#define KOK_1K  (1<<10)
#define KOK_2K  (KOK_1K << 1)
#define KOK_1M 	(KOK_1K<<10)

#define KOK_B(x)   (x)
#define KOK_KB(x) ((x)<<10)
#define KOK_MB(x) ((x)<<20)

#define MAX_MEGVOL_SIZE (3584 * KOK_1M)
#define MAX_MEGMID_SIZE (2048 * KOK_1M)

INT32 Kok_CmpUTF8Str(char *s1,char *s2);

extern int make_dirs(const char *path);

int TOCMerge::rename_megvol_sub(const char *subPath, int maxIdx, int maxVol)
{
	Song_Info_S *pSongInfo;
	char mergeStatus = 0;

	KarToc *tmpToc = NULL;
	int i = 0;
	char *subidxdir;

	char *tmp_subidx;

	int tmpSize = strlen(subPath) + 16;
	char *subidx = (char *) malloc(tmpSize);
	sprintf(subidx, "%s/MEGIDX", subPath);

	tmp_subidx = (char *) malloc(tmpSize);
	sprintf(tmp_subidx, "%s/MEGIDX_TMP", subPath);

	mainToc = new KarToc();

	maxMegMid = maxIdx;
	maxMegVol = maxVol;

	tmpToc = new KarToc();
	mergeStatus = tmpToc->parse_file(subidx);
	if (mergeStatus != 0) {
		printf("Error while parse main TOC\n");
		goto exit_merge;
	}

	mergeStatus = prepare_insert_song(mainToc, tmpToc, NULL, 0, NULL, 0);
	if (mergeStatus != 0) {
		printf("Error while prepare merge main toc\n");
		goto exit_merge;
	}
	delete tmpToc;
	tmpToc = NULL;

	for(i = 0; i < mainToc->getHeader()->uSongNums; i++) {
		pSongInfo = &mainToc->pSongInfo[i];
		if(pSongInfo->property.media_type != VIDEO) {
			pSongInfo->MidiInfo.stFilepos.fileIdx += maxMegMid;
			pSongInfo->Mp3Info.stFilepos.fileIdx += maxMegVol;
		}
	}

	prepare_write_file(mainToc, -1);
	write_file(mainToc, tmp_subidx);

	exit_merge:
	finalize_process();

	if(tmpToc != NULL) {
		delete tmpToc;
		tmpToc = NULL;
	}
	delete mainToc;
	mainToc = NULL;

	free(subidx);
	if(tmp_subidx != NULL)
		free(tmp_subidx);

	return mergeStatus;
}

int TOCMerge::set_data_update_version(const char *subPath, int volDisc, int volXUser, int volUser)
{
	Song_Info_S *pSongInfo;
	char mergeStatus = 0;

	KarToc *tmpToc = NULL;
	int i = 0;
	char *subidxdir;

	char *tmp_subidx;
	Kok_IdxFileHdr_S *header = NULL;

	int tmpSize = strlen(subPath) + 16;
	char *subidx = (char *) malloc(tmpSize);
	sprintf(subidx, "%s/MEGIDX", subPath);

	tmp_subidx = (char *) malloc(tmpSize);
	sprintf(tmp_subidx, "%s/MEGIDX_TMP", subPath);

	mainToc = new KarToc();

	tmpToc = new KarToc();
	mergeStatus = tmpToc->parse_file(subidx);
	if (mergeStatus != 0) {
		printf("Error while parse main TOC\n");
		goto exit_merge;
	}

	mergeStatus = prepare_insert_song(mainToc, tmpToc, NULL, 0, NULL, 0);
	if (mergeStatus != 0) {
		printf("Error while prepare merge main toc\n");
		goto exit_merge;
	}
	delete tmpToc;
	tmpToc = NULL;

	header = mainToc->getHeader();

	header->Magic.Version = volDisc / 100;
	header->Magic.Revision = volDisc % 100;

	header->Magic.Version2 = volXUser / 100;
	header->Magic.Revision2 = volXUser % 100;

	header->Magic.Version3 = volUser / 100;
	header->Magic.Revision3 = volUser % 100;

	prepare_write_file(mainToc, -1);
	write_file(mainToc, tmp_subidx);

	exit_merge:
	finalize_process();

	if(tmpToc != NULL) {
		delete tmpToc;
		tmpToc = NULL;
	}
	delete mainToc;
	mainToc = NULL;

	free(subidx);
	if(tmp_subidx != NULL)
		free(tmp_subidx);

	return mergeStatus;
}

int TOCMerge::delete_update_song(const char *mainPath, Song_Info_Update_S *songInfo, int songCount)
{
	int mergeStatus = 0;
	KarToc *tmpToc = NULL;
	char *tmp_mainidx = NULL;

	int tmpSize = strlen(mainPath) + 16;
	char *mainidx = (char *) malloc(tmpSize);
	sprintf(mainidx, "%s/MEGIDX", mainPath);

	printf("MainIdx: %s\n", mainidx);
	mainToc = new KarToc();

	mergeStatus = mainToc->parse_file_header(mainidx);
	if (mergeStatus != 0) {
		printf("Error while parse main toc header info\n");
		mergeStatus = -3;
		goto exit_merge;
	}

	printf("insert_update_song==>Main TOC info: %d-%d-%d-%d",
			mainToc->getVersion(),
			mainToc->getVersion1(),
			mainToc->getVersion2(),
			mainToc->getVersion3());

	tmpToc = new KarToc();
	mergeStatus = tmpToc->parse_file(mainidx);
	if (mergeStatus != 0) {
		printf("Error while parse main TOC\n");
		goto exit_merge;
	}
	// Read idx run to get max megvol and megmid index
	tmpToc->getMaxMegFileIdx();
	maxMegMid = tmpToc->maxMegMidIdx;
	maxMegVol = tmpToc->maxMegvolIdx;

	mergeStatus = prepare_insert_song(mainToc, tmpToc, NULL, 0, NULL, 0);
	if (mergeStatus != 0) {
		printf("Error while prepare merge main toc\n");
		goto exit_merge;
	}
	delete tmpToc;
	tmpToc = NULL;

	delete_song(mainToc, songInfo, songCount);

	prepare_write_file(mainToc, -1);
	write_file(mainToc, mainidx);

	exit_merge:
	finalize_process();

	if(tmpToc != NULL) {
		delete tmpToc;
	}
	delete mainToc;
	mainToc = NULL;

	free(mainidx);
	if(tmp_mainidx != NULL)
		free(tmp_mainidx);

	return mergeStatus;
}

int TOCMerge::insert_update_song(const char *mainPath, const char *subPath, Song_Info_Update_S *songInfo, int size, Person_Info_Update_S *pArtist, int artistCount, int type)
{
	int mergeStatus = 0;
	KarToc *tmpToc = NULL;

	char *subidxdir = NULL;
	char *tmp_mainidx = NULL;
	char *tmp_subidx = NULL;

	// Create needed directory
	make_dirs(subPath);

	int tmpSize = strlen(mainPath) + 16;
	char *mainidx = (char *) malloc(tmpSize);
	sprintf(mainidx, "%s/MEGIDX", mainPath);

	tmp_mainidx = (char *) malloc(tmpSize);
	sprintf(tmp_mainidx, "%s/MEGIDX_TMP", mainPath);

	tmpSize = strlen(subPath) + 16;
	char *subidx = (char *) malloc(tmpSize);
	sprintf(subidx, "%s/MEGIDX", subPath);

	tmp_subidx = (char *) malloc(tmpSize);
	sprintf(tmp_subidx, "%s/MEGIDX_TMP", subPath);

//	printf("MainIdx: %s\n", mainidx);
//	printf("SubIdx: %s\n", subidx);
	mainToc = new KarToc();
	subToc = new KarToc();

//	mergeStatus = mainToc->parse_file_header(mainidx);
//	if (mergeStatus != 0) {
//		printf("Error while parse main toc header info\n");
//		mergeStatus = -3;
//		goto exit_merge;
//	}
//
//	mergeStatus = subToc->parse_file_header(subidx);
//	if (mergeStatus != 0 && mergeStatus != 0xfe) {
//		printf("Error while parse sub toc header info\n");
//		mergeStatus = -3;
//		goto exit_merge;
//	}

//	printf("insert_update_song==>Main TOC info: %d-%d-%d-%d",
//			mainToc->getVersion(),
//			mainToc->getVersion1(),
//			mainToc->getVersion2(),
//			mainToc->getVersion3());
//	printf("insert_update_song==>Sub TOC info: %d-%d-%d-%d",
//			subToc->getVersion(),
//			subToc->getVersion1(),
//			subToc->getVersion2(),
//			subToc->getVersion3());

	tmpToc = new KarToc();
	mergeStatus = tmpToc->parse_file(mainidx);
	if (mergeStatus != 0) {
		printf("Error while parse main TOC\n");
		goto exit_merge;
	}
	// Read idx run to get max megvol and megmid index
	tmpToc->getMaxMegFileIdx();
	maxMegMid = tmpToc->maxMegMidIdx;
	maxMegVol = tmpToc->maxMegvolIdx;

	mergeStatus = prepare_insert_song(mainToc, tmpToc, songInfo, size, pArtist, artistCount);
	if (mergeStatus != 0) {
		printf("Error while prepare merge main toc\n");
		goto exit_merge;
	}
	delete tmpToc;
	tmpToc = NULL;

	tmpToc = new KarToc();
	mergeStatus = tmpToc->parse_file(subidx);
	if (mergeStatus != 0 && mergeStatus != 0xfe) {
		printf("Error while parse main TOC\n");
		goto exit_merge;
	}

	mergeStatus = prepare_insert_song(subToc, tmpToc, songInfo, size, pArtist, artistCount);
	if (mergeStatus != 0) {
		printf("Error while prepare merge sub toc\n");
		goto exit_merge;
	}
	delete tmpToc;
	tmpToc = NULL;

	mergeStatus = insert_artist(subToc, pArtist, artistCount);
	if (mergeStatus != 0) {
		printf("Error while merge singer info\n");
		mergeStatus = -3;
		goto exit_merge;
	}

	mergeStatus = insert_song(subToc, songInfo, size, pArtist, artistCount);
	if (mergeStatus != 0) {
		printf("Error while merge song info\n");
		mergeStatus = -3;
		goto exit_merge;
	}

	mergeStatus = insert_artist(mainToc, pArtist, artistCount);
	if (mergeStatus != 0) {
		printf("Error while merge singer info main toc\n");
		mergeStatus = -3;
		goto exit_merge;
	}

	mergeStatus = insert_song(mainToc, songInfo, size, pArtist, artistCount);
	if (mergeStatus != 0) {
		printf("Error while merge song info main toc\n");
		mergeStatus = -3;
		goto exit_merge;
	}

	tmpSize = strlen(subPath) + 15;
	subidxdir = (char *)malloc(tmpSize);
	sprintf(subidxdir, "%s/IDX", subPath);

	prepare_write_file(subToc, type);
	prepare_write_file(mainToc, type);

	write_file(subToc, subidx);
	write_file(mainToc, mainidx);

	// Merge file Lyric
	sprintf(mainidx, "%s/FIRST10W", mainPath);
	sprintf(tmp_mainidx, "%s/FIRST10W_TMP", mainPath);
	sprintf(subidx, "%s/FIRST10W", subPath);
	sprintf(tmp_subidx, "%s/FIRST10W_TMP", subPath);
//
//	printf("MainIdx: %s\n", mainidx);
//	printf("SubIdx: %s\n", subidx);

	do_insert_lyric(mainidx, subidx, songInfo, size);

exit_merge:
	finalize_process();

	if(tmpToc != NULL) {
		printf("tmpToctmpToctmpToctmpToctmpToc: %s\n", subidx);
		delete tmpToc;
		tmpToc = NULL;
	}
	delete subToc;
	subToc = NULL;
	delete mainToc;
	mainToc = NULL;

	free(mainidx);
	free(subidx);
	if(tmp_mainidx != NULL)
		free(tmp_mainidx);
	if(tmp_subidx != NULL)
		free(tmp_subidx);

	return mergeStatus;
}

int TOCMerge::do_insert_lyric(const char *pMainLyric, const char *pSubLyric, Song_Info_Update_S *updateInfo, int songcount)
{
	FILE *pMainFile = NULL;
	FILE *pSubFile = NULL;
	int insertStatus = -1;
	int i = 0;
	int buff_size = 512;
	uint8_t *pTempBuf = NULL;
	int pos = 0;
	int lyrLength = 0;
	Song_Info_Update_S *pSongInfo = NULL;

	pMainFile = fopen(pMainLyric, "ab+");
	if(pMainFile == NULL) {
		printf("unable to open file: %s", pMainLyric);
		goto exit;
	}

	pSubFile = fopen(pSubLyric, "ab+");
	if(pSubFile == NULL) {
		printf("unable to open sub file: %s", pSubLyric);
		goto exit;
	}

	pTempBuf = (uint8_t *)(malloc(buff_size));
	memset(pTempBuf, 0, buff_size);

	insertStatus = 0;
	for(i = 0; i < songcount; i++) {
		pSongInfo = &updateInfo[i];
		if(pSongInfo->pLyricOffset == NULL) continue;

		// Prepare Lyric Data To Insert
		pos = 0;
		pTempBuf[pos++] = 0x01; // Header;

		// Package len
		lyrLength = strlen(pSongInfo->pLyricOffset);
		pTempBuf[pos++] = lyrLength + 3 + 1 + 1;

		pTempBuf[pos++] = pSongInfo->MidiInfo.stFilepos.songABCType;
		pTempBuf[pos++] = (pSongInfo->song_code5 >> 16) & 0xFF;
		pTempBuf[pos++] = (pSongInfo->song_code5 >> 8) & 0xFF;
		pTempBuf[pos++] = (pSongInfo->song_code5 >> 0) & 0xFF;

		memcpy(&pTempBuf[pos], pSongInfo->pLyricOffset, lyrLength);
		pos+= lyrLength;
		pTempBuf[pos++] = 0x00;

		// Insert To Main Lyric File
		lyrLength = fwrite(pTempBuf, 1, pos, pMainFile);
//		printf("data main write length: %d", lyrLength);

		// Insert To Sub Lyric File
		lyrLength = fwrite(pTempBuf, 1, pos, pSubFile);
//		printf("data sub write length: %d", lyrLength);
		insertStatus++;
	}

exit:
	if(pTempBuf != NULL) free(pTempBuf);
	if(pMainFile != NULL) fclose(pMainFile);
	if(pSubFile != NULL) fclose(pSubFile);

	return insertStatus;
}

int TOCMerge::prepare_insert_song(KarToc *toc, KarToc *tmptoc, Song_Info_Update_S *songInfo, int size, Person_Info_Update_S *pArtist, int artistCount)
{
	Song_Info_Update_S *pUpdateInfo;
	Kok_IdxFileHdr_S *mainHeader = NULL;
	Person_Info_S * pArtistInfo = NULL;
	Song_Info_S *pSongInfo = NULL;
	uint8_t *pSongName = NULL;
	uint8_t *pSongPyName = NULL;
	uint8_t *pSingerName = NULL;
	uint8_t *pSingerPYName = NULL;
	uint8_t *pVideoMKVName = NULL;
	uint8_t *pVideoJPGName = NULL;
	uint32_t *pMapIndex = NULL;
	int16_t mergeStatus = 0;
	uint32_t numItem = 0;
	uint32_t tableSize = 0;

	uint32_t tmpNameSize = 0;
	uint32_t tmpPYNameSize = 0;
	uint32_t tmpVideoJpegSize = 0;
	uint32_t tmpVideoMKVSize = 0;
	int length = 0;
	int i = 0;

	// Main Header
	mainHeader = (Kok_IdxFileHdr_S *) malloc(sizeof(Kok_IdxFileHdr_S));
	if (mainHeader == NULL) {
		printf("Error NoMem MainHeader\n");
		mergeStatus = -2;
		goto exit_merge;
	}
	memset(mainHeader, 0, sizeof(Kok_IdxFileHdr_S));
	memcpy(mainHeader, tmptoc->getHeader(), sizeof(Kok_IdxFileHdr_S));
	if(toc->gpKokIndexHdr != NULL) {
		free(toc->gpKokIndexHdr);
		toc->gpKokIndexHdr = NULL;
	}
	toc->gpKokIndexHdr = mainHeader;

	// Calculate Memory To Update
	numItem = tmptoc->getSingerCount();
	numItem += artistCount;
	pArtistInfo = (Person_Info_S *) malloc(numItem * sizeof(Person_Info_S));
	if (!pArtistInfo) {
		printf("Error NoMem ArtistInfo\n");
		mergeStatus = -2;
		goto exit_merge;
	}
	memset(pArtistInfo, 0, numItem * sizeof(Person_Info_S));
	if(mainHeader->uSingerNums > 0)
		memcpy(pArtistInfo, tmptoc->offsetToPointer(mainHeader->pSingerInfoTable), mainHeader->uSingerNums * sizeof(Person_Info_S));
	for(i = 0; i < mainHeader->uSingerNums; i++) {
		pArtistInfo[i].pNameOffset -= mainHeader->pSingerNameTable;
		pArtistInfo[i].pPYOffset -= mainHeader->pSingerNamePYStrTable;
	}
	toc->pArtistInfo = pArtistInfo;

	tmpNameSize = 0;
	tmpPYNameSize = 0;
	for(i = 0; i < artistCount; i++) {
		tmpNameSize += strlen(pArtist[i].pNameOffset) + 1;
		tmpPYNameSize += strlen(pArtist[i].pPYOffset) + 1;

		pArtist[i].insertIndex = -1;
	}

	tableSize = mainHeader->uSingerNameTableSize;
	tableSize += tmpNameSize;
	pSingerName = (uint8_t *)malloc(tableSize);
	if (pSingerName == NULL) {
		printf("Error NoMem\n");
		mergeStatus = -2;
		goto exit_merge;
	}
	memset(pSingerName, 0, tableSize);
	if(mainHeader->uSingerNameTableSize > 0)
		memcpy(pSingerName, tmptoc->offsetToPointer(mainHeader->pSingerNameTable), mainHeader->uSingerNameTableSize);
	toc->pSingerName = pSingerName;

	tableSize = mainHeader->uSingerNamePYStrTableSize;
	tableSize += tmpPYNameSize;
	pSingerPYName = (uint8_t *)malloc(tableSize);
	if (pSingerPYName == NULL) {
		printf("Error NoMem\n");
		mergeStatus = -2;
		goto exit_merge;
	}
	memset(pSingerPYName, 0, tableSize);
	if(mainHeader->uSingerNamePYStrTableSize > 0)
		memcpy(pSingerPYName, tmptoc->offsetToPointer(mainHeader->pSingerNamePYStrTable), mainHeader->uSingerNamePYStrTableSize);
	toc->pSingerPYName = pSingerPYName;

	// Song Info
	numItem = tmptoc->getSongCount();
	numItem += size;
	pSongInfo = (Song_Info_S *) malloc(numItem * sizeof(Song_Info_S));
	if (!pSongInfo) {
		printf("Error NoMem SongInfo\n");
		mergeStatus = -2;
		goto exit_merge;
	}
	memset(pSongInfo, 0, numItem * sizeof(Song_Info_S));
	if(mainHeader->uSongNums > 0)
		memcpy(pSongInfo, tmptoc->offsetToPointer(mainHeader->pSongInfoTable), mainHeader->uSongNums * sizeof(Song_Info_S));
	for(i = 0; i < mainHeader->uSongNums; i++) {
		pSongInfo[i].pSongNameOffset -= mainHeader->pSongNameTable;
		pSongInfo[i].pPYNameOffset -= mainHeader->pPYStrTable;
		if(pSongInfo[i].pSongClips != UINT32_MAX)
			pSongInfo[i].pSongClips -= mainHeader->pVideoJpegNameTable;
		if(pSongInfo[i].property.media_type == VIDEO) {
			pSongInfo[i].VideoInfo.pVideoName -= mainHeader->pVideoMkvNameTable;
		}
	}
	toc->pSongInfo = pSongInfo;

	pMapIndex = (uint32_t *)malloc(size * sizeof(uint32_t));
	toc->pMapIndex = pMapIndex;

	tmpNameSize = 0;
	tmpPYNameSize = 0;
	tmpVideoMKVSize = 0;
	tmpVideoJpegSize = 0;
	for(i = 0; i < size; i++) {
		pUpdateInfo = &songInfo[i];

		tmpNameSize += strlen(pUpdateInfo->pSongNameOffset) + 1;
		tmpPYNameSize += strlen(pUpdateInfo->pPYNameOffset) + 1;

		uint8_t* pSongClip = pUpdateInfo->pSongClips;
		uint8_t numclip = *pSongClip;
		length = numclip * 2+1;
		pSongClip += length;
		while ((*pSongClip++) & (1<< 7)) {
			length++;
		}
		tmpVideoJpegSize += length;
	}

	tableSize = mainHeader->uSongNameTableSize;
	tableSize += tmpNameSize;
	pSongName = (uint8_t *)malloc(tableSize);
	if (pSongName == NULL) {
		printf("Error NoMem\n");
		mergeStatus = -2;
		goto exit_merge;
	}
	memset(pSongName, 0, tableSize);
	if(mainHeader->uSongNameTableSize > 0)
		memcpy(pSongName, tmptoc->offsetToPointer(mainHeader->pSongNameTable), mainHeader->uSongNameTableSize);
	toc->pSongName = pSongName;

	tableSize = mainHeader->uPYStrTableSize;
	tableSize += tmpPYNameSize;
	pSongPyName = (uint8_t *)malloc(tableSize);
	if (pSongPyName == NULL) {
		printf("Error NoMem\n");
		mergeStatus = -2;
		goto exit_merge;
	}
	memset(pSongPyName, 0, tableSize);
	if(mainHeader->uPYStrTableSize > 0)
		memcpy(pSongPyName, tmptoc->offsetToPointer(mainHeader->pPYStrTable), mainHeader->uPYStrTableSize);
	toc->pSongPyName = pSongPyName;

	tableSize = mainHeader->uVideoMkvNameSize;
	tableSize += tmpVideoMKVSize;
	pVideoMKVName = (uint8_t *)malloc(tableSize);
	if (pVideoMKVName == NULL) {
		printf("Error NoMem\n");
		mergeStatus = -2;
		goto exit_merge;
	}
	memset(pVideoMKVName, 0, tableSize);
	if(mainHeader->uVideoMkvNameSize > 0)
		memcpy(pVideoMKVName, tmptoc->offsetToPointer(mainHeader->pVideoMkvNameTable), mainHeader->uVideoMkvNameSize);
	toc->pVideoMKVName = pVideoMKVName;

	tableSize = mainHeader->uVideoJpegNameSize;
	tableSize += tmpVideoJpegSize;
	pVideoJPGName = (uint8_t *)malloc(tableSize);
	if (pVideoJPGName == NULL) {
		printf("Error NoMem\n");
		mergeStatus = -2;
		goto exit_merge;
	}
	memset(pVideoJPGName, 0, tableSize);
	if(mainHeader->uVideoJpegNameSize > 0)
		memcpy(pVideoJPGName, tmptoc->offsetToPointer(mainHeader->pVideoJpegNameTable), mainHeader->uVideoJpegNameSize);
	toc->pVideoJPGName = pVideoJPGName;

exit_merge:
	return mergeStatus;
}

int TOCMerge::insert_song(KarToc *toc, Song_Info_Update_S *updateInfo, int size, Person_Info_Update_S *pArtistInfo, uint32_t artistCount) {
	Kok_IdxFileHdr_S *mainHeader = toc->gpKokIndexHdr;
	Song_Info_Update_S *pUpdateInfo = NULL;
	int i = 0, j = 0, k = 0;
	int lastSort = 0;
	int lastInsertIdx = 0;

	// Next insert song
	for (i = 0; i < size; i++) {
		pUpdateInfo = &updateInfo[i];

		// Insert Song Info
		if (pUpdateInfo->property.language_type < 3) {
			lastInsertIdx = do_insert_song_info(toc, pUpdateInfo, lastSort);
			lastSort = lastInsertIdx;
		}else {
			lastInsertIdx = do_insert_song_info(toc, pUpdateInfo, mainHeader->uSongNums);
		}

		// Save insert index of update song
		toc->pMapIndex[i] = lastInsertIdx;

		// Update Existing Singer And Author Index
		for (k = 0; k < MAX_ARTIST_PER_SONG; k++) {
			if (pUpdateInfo->pPersonIdx.singer_idxes[k] == UINT16_MAX) {
				break;
			}

			int singeridx = pUpdateInfo->pPersonIdx.singer_idxes[k];
			Person_Info_Update_S *updateArtist = &pArtistInfo[singeridx];
			if(updateArtist->num_songs.bSinger) {
				toc->pSongInfo[lastInsertIdx].pPersonIdx.singer_idxes[k] = updateArtist->insertIndex;
			}
		}

		for (k = 0; k < MAX_ARTIST_PER_SONG; k++) {
			if (pUpdateInfo->pPersonIdx.author_idxes[k] == UINT16_MAX) {
				break;
			}

			int singeridx = pUpdateInfo->pPersonIdx.author_idxes[k];
			Person_Info_Update_S *updateArtist = &pArtistInfo[singeridx];
			if(updateArtist->num_songs.bAuthor) {
				toc->pSongInfo[lastInsertIdx].pPersonIdx.author_idxes[k] = updateArtist->insertIndex;
			}
		}
	}

	return 0;
}

uint32_t TOCMerge::do_insert_song_info(KarToc *toc, Song_Info_Update_S *updateInfo, uint32_t index)
{
	Kok_IdxFileHdr_S *mainHeader = toc->gpKokIndexHdr;
	int songCnt = mainHeader->uSongNums;
	uint16_t length = 0;
	uint32_t outIdx = index;
	char* name = NULL;

	if (index >= songCnt) {
		outIdx = index;
	} else {
#if (!INSERT_TOC_OEM)
		for(int i = 0; i < songCnt; i++) {
			Song_Info_S *pSongInfo = &toc->pSongInfo[i];
			if(pSongInfo != NULL && pSongInfo->song_code6 == updateInfo->song_code6) {
				printf("Song existed: %d==>%s", updateInfo->song_code6, updateInfo->pSongNameOffset);
				return i;
			}
		}
#endif
		name = updateInfo->pSongNameOffset;
		outIdx = find_song_index(toc, name, outIdx+1, updateInfo->property.language_type);

		memmove(&toc->pSongInfo[outIdx+1], &toc->pSongInfo[outIdx], (songCnt - outIdx) * sizeof(Song_Info_S));
	}

	// Copy songinfo
	//
	Song_Info_S songInfo;
	memset(&songInfo, 0, sizeof(Song_Info_S));
	memcpy(&(songInfo.property), &(updateInfo->property), sizeof(songInfo.property));
//	songInfo.property = updateInfo->property;
	memcpy(&(songInfo.VideoInfo), &(updateInfo->VideoInfo), sizeof(songInfo.VideoInfo));
//	songInfo.VideoInfo = updateInfo->VideoInfo;
	memcpy(&(songInfo.MidiInfo), &(updateInfo->MidiInfo), sizeof(songInfo.MidiInfo));
	songInfo.song_code5 = updateInfo->song_code5;
	songInfo.song_code6 = updateInfo->song_code6;

	memcpy(&(songInfo.pPersonIdx), &(updateInfo->pPersonIdx), sizeof(songInfo.pPersonIdx));

	memcpy(&toc->pSongInfo[outIdx], &songInfo, sizeof(Song_Info_S));

	toc->pSongInfo[outIdx].pSongNameOffset = mainHeader->uSongNameTableSize;
	length = strlen(updateInfo->pSongNameOffset);
	memcpy(&toc->pSongName[mainHeader->uSongNameTableSize], updateInfo->pSongNameOffset, length);
	mainHeader->uSongNameTableSize += length;
	toc->pSongName[mainHeader->uSongNameTableSize] = '\0';
	mainHeader->uSongNameTableSize += 1;

	toc->pSongInfo[outIdx].pPYNameOffset = mainHeader->uPYStrTableSize;
	length = strlen(updateInfo->pPYNameOffset);
	memcpy(&toc->pSongPyName[mainHeader->uPYStrTableSize], updateInfo->pPYNameOffset, length);
	mainHeader->uPYStrTableSize += length;
	toc->pSongPyName[mainHeader->uPYStrTableSize] = '\0';
	mainHeader->uPYStrTableSize += 1;

	toc->pSongInfo[outIdx].pSongClips = mainHeader->uVideoJpegNameSize;

	uint8_t* pSongClip = updateInfo->pSongClips;
	uint8_t numclip = *pSongClip;
	length = numclip * 2+1;
	pSongClip += length;
	while ((*pSongClip++) & (1<< 7)) {
		length++;
	}
	memcpy(&toc->pVideoJPGName[mainHeader->uVideoJpegNameSize], updateInfo->pSongClips, length);
	mainHeader->uVideoJpegNameSize += length;

	if(toc->pSongInfo[outIdx].property.media_type == VIDEO) {
		toc->pSongInfo[outIdx].VideoInfo.pVideoName = mainHeader->uVideoMkvNameSize;

//		length = 0;
//		Kok_IdxFileHdr_S *updateHeader = updateToc->getHeader();
//		if(updateHeader->uVideoMkvNameSize > 0 && updateHeader->pVideoMkvNameTable != songInfo->VideoInfo.pVideoName) {
//			char *pMKVName = (char *)(usongInfo->VideoInfo.pVideoName);
//			length = strlen(pMKVName);
//			memcpy(&toc->pVideoMKVName[mainHeader->uVideoMkvNameSize], updateToc->offsetToPointer(songInfo->VideoInfo.pVideoName), length);
//			length += 1;
//		}
//		mainHeader->uVideoMkvNameSize += length;
	}

	songCnt += 1;
	mainHeader->uSongNums = songCnt;
	return outIdx;
}

int TOCMerge::insert_artist(KarToc *toc, Person_Info_Update_S *pArtist, int artistCount)
{
	Kok_IdxFileHdr_S *mainHeader = toc->gpKokIndexHdr;
	Person_Info_Update_S *pArtistUpdate = NULL;
	Person_Info_S *pArtistInfo = NULL;
	int i = 0;
	int j = 0, k = 0;
	int lastSort = 0;
	int lastInsertIdx = 0;
	int existed_cnt = 0;

	for (i = 0; i < artistCount; i++) {
		pArtistUpdate = &pArtist[i];
		for(j = 0; j < mainHeader->uSingerNums; j++) {
			pArtistInfo = &(toc->pArtistInfo[j]);
            
			if(pArtistInfo && strcmp(pArtistUpdate->pNameOffset, (char *)(toc->pSingerName + pArtistInfo->pNameOffset)) == 0) {
				// Artist Has Existed
				if (pArtistUpdate->num_songs.bAuthor) {
					pArtistInfo->num_songs.bAuthor = 1;
				}
				if (pArtistUpdate->num_songs.bSinger) {
					pArtistInfo->num_songs.bSinger = 1;
				}
				existed_cnt ++;
				lastInsertIdx = j;

				pArtistUpdate->insertIndex = lastInsertIdx;
				break;
			}
		}

		if (j >= mainHeader->uSingerNums) {
			// Insert Singer Info
			if (pArtistUpdate->num_songs.language_type < 3) {
				lastInsertIdx = do_insert_artist_info(toc, pArtistUpdate, lastSort);
				lastSort = lastInsertIdx;
			}else {
				lastInsertIdx = do_insert_artist_info(toc, pArtistUpdate, mainHeader->uSingerNums);
			}

			for(j = 0; j < artistCount; j++) {
				if(pArtist[j].insertIndex >= lastInsertIdx) {
					pArtist[j].insertIndex += 1;
				}
			}

			pArtistUpdate->insertIndex = lastInsertIdx;

			for (j = 0; j < mainHeader->uSongNums; j++) {
				Song_Info_S *songInfo = &(toc->pSongInfo[j]);
				for (k = 0; k < MAX_ARTIST_PER_SONG; k++) {
					if (songInfo->pPersonIdx.author_idxes[k] == UINT16_MAX) {
						break;
					}
					if (songInfo->pPersonIdx.author_idxes[k] >= lastInsertIdx) {
						songInfo->pPersonIdx.author_idxes[k] +=1;
					}
				}

				for (k = 0; k < MAX_ARTIST_PER_SONG; k++) {
					if (songInfo->pPersonIdx.singer_idxes[k] == UINT16_MAX) {
						break;
					}
					if (songInfo->pPersonIdx.singer_idxes[k] >= lastInsertIdx) {
						songInfo->pPersonIdx.singer_idxes[k] += 1;
					}
				}

			}
		}
	}
	return 0;
}

uint32_t TOCMerge::do_insert_artist_info(KarToc *toc, Person_Info_Update_S *pArtistInfo, uint32_t index)
{
	Kok_IdxFileHdr_S *mainHeader = toc->gpKokIndexHdr;
	int artistCnt = mainHeader->uSingerNums;
	uint16_t length = 0;
	uint32_t outIdx = index;
	char* name = NULL;
	if (index >= artistCnt) {
		outIdx = index+1;
	} else {
		name = pArtistInfo->pNameOffset;
		outIdx = find_singer_index(toc, name, outIdx, pArtistInfo->num_songs.language_type);

		memmove(&toc->pArtistInfo[outIdx+1], &toc->pArtistInfo[outIdx], (artistCnt - outIdx) * sizeof(Person_Info_S));
	}

	// Copy Artist Info
	//
	Person_Info_S artistInfo;
	memset(&artistInfo, 0, sizeof(Person_Info_S));
	memcpy(&(artistInfo.num_songs), &(pArtistInfo->num_songs), sizeof(artistInfo.num_songs));
//	artistInfo.singerIdx = outIdx;

	memcpy(&toc->pArtistInfo[outIdx], &artistInfo, sizeof(Person_Info_S));
	toc->pArtistInfo[outIdx].num_songs.uAuthorSongNums += artistInfo.num_songs.uAuthorSongNums;
	toc->pArtistInfo[outIdx].num_songs.uSingerSongNums += artistInfo.num_songs.uSingerSongNums;

	length = strlen(pArtistInfo->pNameOffset) + 1;
	memcpy(&toc->pSingerName[mainHeader->uSingerNameTableSize], pArtistInfo->pNameOffset, length);
	toc->pArtistInfo[outIdx].pNameOffset = mainHeader->uSingerNameTableSize;
	mainHeader->uSingerNameTableSize += length;

	length = strlen(pArtistInfo->pPYOffset) + 1;
	memcpy(&toc->pSingerPYName[mainHeader->uSingerNamePYStrTableSize], pArtistInfo->pPYOffset, length);
	toc->pArtistInfo[outIdx].pPYOffset = mainHeader->uSingerNamePYStrTableSize;
	mainHeader->uSingerNamePYStrTableSize += length;

	artistCnt += 1;
	mainHeader->uSingerNums = artistCnt;
	return outIdx;
}

void TOCMerge::delete_song(KarToc *toc, Song_Info_Update_S *updateInfo, int songCount)
{
	Kok_IdxFileHdr_S *mainHeader = toc->gpKokIndexHdr;
	Song_Info_S *tmpSongInfo = NULL;
	Song_Info_Update_S *pUpdateInfo = NULL;
	int i = 0, j = 0;

	// Do delete song
	for (i = 0; i < songCount; i++) {
		pUpdateInfo = &updateInfo[i];

		for (j = 0; j < mainHeader->uSongNums; j++) {
			tmpSongInfo = &(toc->pSongInfo[j]);
			if (tmpSongInfo->song_code6 == pUpdateInfo->song_code6
					&& tmpSongInfo->MidiInfo.stFilepos.songABCType == pUpdateInfo->MidiInfo.stFilepos.songABCType) {
				do_delete_song(toc, tmpSongInfo, j);
				break;
			}
		}
	}
}

int TOCMerge::merge_toc_update(const char* mainPath, const char *subPath, const char * upPath, int type) {
	int mergeStatus = 0;
	bool hasUpdate = false;

	const uint16_t path_len = 256;
	char path[path_len];
	KarToc *tmpToc = NULL;

	char *subidxdir;
	char *upidxdir;

	char *tmp_mainidx;
	char *tmp_subidx;

	// Create needed directory
	sprintf(path, "%s/IDX", subPath);
	make_dirs(path);
	sprintf(path, "%s/KTV", subPath);
	make_dirs(path);

	int tmpSize = strlen(mainPath) + 16;
	char *mainidx = (char *) malloc(tmpSize);
	sprintf(mainidx, "%s/MEGIDX", mainPath);

	tmp_mainidx = (char *) malloc(tmpSize);
	sprintf(tmp_mainidx, "%s/MEGIDX_TMP", mainPath);

	tmpSize = strlen(subPath) + 16;
	char *subidx = (char *) malloc(tmpSize);
	sprintf(subidx, "%s/IDX/MEGIDX", subPath);

	tmp_subidx = (char *) malloc(tmpSize);
	sprintf(tmp_subidx, "%s/MEGIDX_TMP", subPath);

	tmpSize = strlen(upPath) + 16;
	char *updateidx = (char *)malloc(tmpSize);
	sprintf(updateidx, "%s/IDX/MEGIDX", upPath);

	printf("MainIdx: %s\n", mainidx);
	printf("SubIdx: %s\n", subidx);
	printf("updateIdx: %s\n", updateidx);
	mainToc = new KarToc();
	subToc = new KarToc();
	updateToc = new KarToc();

	printf("prepare_merge sub toc\n");
	tmpToc = new KarToc();
	mergeStatus = tmpToc->parse_file(mainidx);
	if (mergeStatus != 0) {
		printf("Error while parse main TOC\n");
		goto exit_merge;
	}
	// Read idx run to get max megvol and megmid index
	tmpToc->getMaxMegFileIdx();
	maxMegMid = tmpToc->maxMegMidIdx;
	maxMegVol = tmpToc->maxMegvolIdx;
	delete tmpToc;
	tmpToc = NULL;

	mergeStatus = mainToc->parse_file_header(mainidx);
	if (mergeStatus != 0) {
		printf("Error while parse main toc header info\n");
		mergeStatus = -3;
		goto exit_merge;
	}

	mergeStatus = subToc->parse_file_header(subidx);
	if (mergeStatus != 0 && mergeStatus != 0xfe) {
		printf("Error while parse sub toc header info\n");
		mergeStatus = -3;
		goto exit_merge;
	}

	mergeStatus = updateToc->parse_file_header(updateidx);
	if (mergeStatus != 0) {
		printf("Error while parse update toc header info\n");
		mergeStatus = -3;
		goto exit_merge;
	}

	printf("Main TOC info: %d-%d-%d-%d",
			mainToc->getVersion(),
			mainToc->getVersion1(),
			mainToc->getVersion2(),
			mainToc->getVersion3());
	printf("Update TOC info: %d-%d-%d-%d",
			updateToc->getVersion(),
			updateToc->getVersion1(),
			updateToc->getVersion2(),
			updateToc->getVersion3());
	printf("Sub TOC info: %d-%d-%d-%d",
				subToc->getVersion(),
				subToc->getVersion1(),
				subToc->getVersion2(),
				subToc->getVersion3());

	if(type == UPDATE_TYPE_DISC) {
		if(updateToc->getVersion() > subToc->getVersion())
			hasUpdate = true;
	}else if(type == UPDATE_TYPE_SCDISC) {
		if(updateToc->getVersion1() > subToc->getVersion1())
			hasUpdate = true;
	}else if(type == UPDATE_TYPE_XUSER) {
		if(updateToc->getVersion2() > subToc->getVersion2())
			hasUpdate = true;
	}else if(type == UPDATE_TYPE_USER) {
		if(updateToc->getVersion3() > subToc->getVersion3())
			hasUpdate = true;
	}
//	if(hasUpdate == false) {
//		printf("Already newest version of type: %d", type);
//		goto exit_merge;
//	}

	printf("Parse update file");
	mergeStatus = updateToc->parse_file(updateidx);
	if (mergeStatus != 0) {
		printf("Error while parse update TOC\n");
		goto exit_merge;
	}

	tmpToc = new KarToc();
	mergeStatus = tmpToc->parse_file(mainidx);
	if (mergeStatus != 0) {
		printf("Error while parse main TOC\n");
		goto exit_merge;
	}

	printf("prepare_merge main toc\n");
	mergeStatus = prepare_merge_toc(mainToc, updateToc, tmpToc);;
	if (mergeStatus != 0) {
		printf("Error while prepare merge main toc\n");
		goto exit_merge;
	}
	delete tmpToc;
	tmpToc = NULL;

	tmpToc = new KarToc();
	mergeStatus = tmpToc->parse_file(subidx);
	if (mergeStatus != 0 && mergeStatus != 0xFE) {
		printf("Error while parse sub TOC\n");
		goto exit_merge;
	}
	printf("prepare_merge sub toc\n");
	mergeStatus = prepare_merge_toc(subToc, updateToc, tmpToc);
	if (mergeStatus != 0) {
		printf("Error while prepare merge sub toc\n");
		goto exit_merge;
	}
	delete tmpToc;
	tmpToc = NULL;

	printf("merge_singer_info\n");
	mergeStatus = merge_singer_info(subToc);
	if (mergeStatus != 0) {
		printf("Error while merge singer info\n");
		mergeStatus = -3;
		goto exit_merge;
	}

	printf("merge_songInfo\n");
	mergeStatus = merge_songInfo(subToc);
	if (mergeStatus != 0) {
		printf("Error while merge song info\n");
		mergeStatus = -3;
		goto exit_merge;
	}

	printf("merge_singer_info main toc\n");
	mergeStatus = merge_singer_info(mainToc);
	if (mergeStatus != 0) {
		printf("Error while merge singer info main toc\n");
		mergeStatus = -3;
		goto exit_merge;
	}

	printf("merge_songInfo main toc\n");
	mergeStatus = merge_songInfo(mainToc);
	if (mergeStatus != 0) {
		printf("Error while merge song info main toc\n");
		mergeStatus = -3;
		goto exit_merge;
	}

	printf("Merge megvol\n");
	tmpSize = strlen(subPath) + 15;
	subidxdir = (char *)malloc(tmpSize);
	sprintf(subidxdir, "%s/IDX", subPath);

	tmpSize = strlen(upPath) + 15;
	upidxdir = (char *)malloc(tmpSize);
	sprintf(upidxdir, "%s/IDX", upPath);

#if (!INSERT_TOC_OEM)
		printf("SubIdxdir: %s\n", subidxdir);
		printf("updateIdxdir: %s\n", upidxdir);
		merge_song_megvol_mid(subidxdir, upidxdir, maxMegVol, maxMegMid, mainToc, subToc);
		free(subidxdir);
		free(upidxdir);
#endif

	printf("prepare_write_file\n");
	prepare_write_file(subToc, type);
	printf("prepare_write_file111\n");
	prepare_write_file(mainToc, type);

	write_file(subToc, subidx);
	write_file(mainToc, mainidx);

	// Merge file Lyric
	sprintf(mainidx, "%s/FIRST10W", mainPath);
	sprintf(tmp_mainidx, "%s/FIRST10W_TMP", mainPath);
	sprintf(subidx, "%s/IDX/FIRST10W", subPath);
	sprintf(tmp_subidx, "%s/FIRST10W_TMP", subPath);
	sprintf(updateidx, "%s/IDX/FIRST10W", upPath);

	printf("MainIdx: %s\n", mainidx);
	printf("SubIdx: %s\n", subidx);
	printf("updateIdx: %s\n", updateidx);

	merge_lyric_file1(subidx, updateidx);
	merge_lyric_file1(mainidx, updateidx);

exit_merge:
	printf("finalize_process\n");
	finalize_process();

	if(tmpToc != NULL) {
		delete tmpToc;
	}
	delete mainToc;
	mainToc = NULL;
	delete subToc;
	subToc = NULL;
	delete updateToc;
	updateToc = NULL;

	free(mainidx);
	free(subidx);
	free(updateidx);
	if(tmp_mainidx != NULL)
		free(tmp_mainidx);
	if(tmp_subidx != NULL)
		free(tmp_subidx);

	return mergeStatus;
}

int TOCMerge::merge_lyric_file1(const char *orgF10W, const char *uF10W) {
	FILE *pMainFile = NULL;
	FILE *pUpdateFile = NULL;
	uint8_t *tempBuff = NULL;
	uint16_t buff_size = 1024 * 1;
	int byteread = 0;
	printf("merge_lyric_file\n");

	pMainFile = fopen(orgF10W, "ab+");
	if (pMainFile == NULL) {
		printf("Open output Lyric File Failed\n");
		return -1;
	}

	// copy update file
	pUpdateFile = fopen(uF10W, "rb");
	if (pUpdateFile == NULL) {
		printf("Open Update lyric failed\n");
		fclose(pMainFile);
		return -1;
	}
	printf("Open file update lyric success: %s\n", uF10W);

    tempBuff = (uint8_t *)malloc(buff_size);
    if(tempBuff == NULL) {
    	printf("Open Update lyric failed\n");
    	fclose(pMainFile);
    	fclose(pUpdateFile);
    	return -1;
    }

	do {
		byteread = (int)fread(tempBuff, 1, buff_size, pUpdateFile);
		fwrite(tempBuff, 1, byteread, pMainFile);
	}while (byteread > 0);
	fclose(pUpdateFile);

	free(tempBuff);
	fclose(pMainFile);
	return 0;
}

int TOCMerge::merge_lyric_file(const char *orgF10W, const char *uF10W, const char * oF10W) {
    FILE *pMainFile = NULL;
    FILE *pUpdateFile = NULL;
    uint8_t *tempBuff = NULL;
    uint16_t buff_size = 1024 * 1;
    int byteread = 0;
    printf("merge_lyric_file\n");
    
    FILE *pFile;
    pFile = fopen(orgF10W, "rb");
    if (pFile == NULL) {
    	printf("File '%s' not exist\n", orgF10W);
    	FILE *pDst = fopen(oF10W, "wb");
    	if(pDst == NULL) {
    		printf("Error: create file f10w:%s", oF10W);
    		return -1;
    	}

    	FILE *pSrc = fopen(uF10W, "rb");
    	if(pDst == NULL) {
    		printf("Error: open file f10w");
    		fclose(pDst);
    		return -1;
    	}

    	int tmpBufSize = 10*1024;
    	uint8_t*tmpBuf = (uint8_t *) malloc(tmpBufSize);
    	if(tmpBuf == NULL) {
    		fclose(pDst);
    		fclose(pSrc);
    		return -1;
    	}
    	int byte_read = 0;
    	while((byte_read = fread(tmpBuf, 1, tmpBufSize, pSrc)) > 0) {
    		fwrite(tmpBuf, 1, byte_read, pDst);
    	}
    	fclose(pSrc);
    	fclose(pDst);
    	free(tmpBuf);
    	return 0;
    }
    fclose(pFile);

    pMainFile = fopen(oF10W, "wb");
    if (pMainFile == NULL) {
        printf("Open output Lyric File Failed\n");
        return -1;
    }
    printf("Open main file lyric success: %s\n", oF10W);
    
    // Copy original lyric file
    pUpdateFile = fopen(orgF10W, "rb");
    if (pUpdateFile == NULL) {
        printf("Open Update lyric failed: %s\n", orgF10W);
        fclose(pMainFile);
        return -1;
    }
    printf("Open file update lyric success: %s\n", orgF10W);
    
    tempBuff = (uint8_t *)malloc(buff_size);
    do {
        byteread = (int)fread(tempBuff, 1, buff_size, pUpdateFile);
        fwrite(tempBuff, 1, byteread, pMainFile);
    }while (byteread > 0);
    fclose(pUpdateFile);
    
    // copy update file
    pUpdateFile = fopen(uF10W, "rb");
    if (pUpdateFile == NULL) {
    	printf("Open Update lyric failed\n");
    	fclose(pMainFile);
    	free(tempBuff);
    	return -1;
    }
    printf("Open file update lyric success: %s\n", uF10W);

    do {
    	byteread = (int)fread(tempBuff, 1, buff_size, pUpdateFile);
    	fwrite(tempBuff, 1, byteread, pMainFile);
    }while (byteread > 0);
    fclose(pUpdateFile);

    free(tempBuff);
    fclose(pMainFile);
    return 0;
}

int copy_file(const char *dst, const char *src) {
	FILE *pDst = fopen(dst, "wb");
	if(pDst == NULL) {
		printf("Error: create file idx: %s", dst);
		return -1;
	}

	FILE *pSrc = fopen(src, "rb");
	if(pDst == NULL) {
		printf("Error: open file idx: %s", src);
		fclose(pDst);
		return -1;
	}

	int tmpBufSize = 10*1024;
	uint8_t*tmpBuf = (uint8_t *) malloc(tmpBufSize);
	if(tmpBuf == NULL) {
		fclose(pDst);
		fclose(pSrc);
		return -1;
	}
	int byte_read = 0;
	while((byte_read = fread(tmpBuf, 1, tmpBufSize, pSrc)) > 0) {
		fwrite(tmpBuf, 1, byte_read, pDst);
	}
	fclose(pSrc);
	fclose(pDst);
	free(tmpBuf);
    return 0;
}

int TOCMerge::merge_toc(const char* orgMegIdx, const char *uMegIdx, const char *oMegIdx, int type)
{
    int mergeStatus = 0;
    bool hasUpdate = false;
    
    FILE *pFile;
    pFile = fopen(orgMegIdx, "rb");
    if (pFile == NULL) {
    	printf("File '%s' not exist\n", orgMegIdx);

    	return copy_file(oMegIdx, uMegIdx);
    }
    
    mainToc = new KarToc();
    updateToc = new KarToc();
    KarToc *tmpToc = new KarToc();
    mergeStatus = tmpToc->parse_file_header(orgMegIdx);
    if (mergeStatus != 0) {
    	printf("Error while parse main toc header info: %s\n", orgMegIdx);
    	mergeStatus = -3;
    	goto exit_merge;
    }

    mergeStatus = updateToc->parse_file_header(uMegIdx);
    if (mergeStatus != 0) {
    	printf("Error while parse update toc header info: %s\n", uMegIdx);
    	mergeStatus = -3;
    	goto exit_merge;
    }

    printf("Main TOC info: %d-%d-%d-%d",
    		tmpToc->getVersion(),
    		tmpToc->getVersion1(),
    		tmpToc->getVersion2(),
    		tmpToc->getVersion3());
    printf("Update TOC info: %d-%d-%d-%d",
    		updateToc->getVersion(),
    		updateToc->getVersion1(),
    		updateToc->getVersion2(),
    		updateToc->getVersion3());

    if(type == UPDATE_TYPE_DISC) {
    	if(updateToc->getVersion() > tmpToc->getVersion())
    		hasUpdate = true;
    }else if(type == UPDATE_TYPE_SCDISC) {
    	if(updateToc->getVersion1() > tmpToc->getVersion1())
    	    		hasUpdate = true;
    }else if(type == UPDATE_TYPE_XUSER) {
    	if(updateToc->getVersion2() > tmpToc->getVersion2())
    	    		hasUpdate = true;
    }else if(type == UPDATE_TYPE_USER) {
    	if(updateToc->getVersion3() > tmpToc->getVersion3())
    	    		hasUpdate = true;
    }
    if(hasUpdate == false) {
    	printf("Already newest version of type: %d", type);
    	mergeStatus = copy_file(oMegIdx, orgMegIdx);
    	goto exit_merge;
    }

    printf("Parse update file");
    mergeStatus = tmpToc->parse_file(orgMegIdx);
    if (mergeStatus != 0) {
    	printf("Error while parse main TOC\n");
    	goto exit_merge;
    }

    mergeStatus = updateToc->parse_file(uMegIdx);
    if (mergeStatus != 0) {
    	printf("Error while parse update TOC\n");
    	goto exit_merge;
    }

    printf("prepare_merge\n");
    mergeStatus = prepare_merge_toc(mainToc, updateToc, tmpToc);
    if (mergeStatus != 0) {
        printf("Error while prepare merge info\n");
    	goto exit_merge;
    }
    
    printf("merge_singer_info\n");
    mergeStatus = merge_singer_info(mainToc);
    if (mergeStatus != 0) {
        printf("Error while merge singer info\n");
        mergeStatus = -3;
        goto exit_merge;
    }

    printf("merge_songInfo\n");
    mergeStatus = merge_songInfo(mainToc);
    if (mergeStatus != 0) {
        printf("Error while merge song info\n");
        mergeStatus = -3;
        goto exit_merge;
    }
    
    printf("prepare_write_file\n");
    prepare_write_file(mainToc, type);
    
    write_file(mainToc, oMegIdx);
    
exit_merge:
	printf("finalize_process\n");
	finalize_process();

	delete tmpToc;
	tmpToc = NULL;
    delete mainToc;
    mainToc = NULL;
    delete updateToc;
    updateToc = NULL;
    
    return mergeStatus;
}

int TOCMerge::prepare_merge(KarToc *toc, const char* orgMegIdx, const char *uMegIdx)
{
    KarToc *tmpToc = new KarToc();
    int16_t mergeStatus = 0;
    
    mergeStatus = tmpToc->parse_file(orgMegIdx);
    if (mergeStatus != 0) {
        printf("Error while parse main TOC\n");
        goto exit_merge;
    }
    
    mergeStatus = updateToc->parse_file(uMegIdx);
    if (mergeStatus != 0) {
        printf("Error while parse update TOC\n");
        goto exit_merge;
    }
    
    mergeStatus = prepare_merge_toc(toc, updateToc, tmpToc);
    
exit_merge:
    delete tmpToc;
    return mergeStatus;
}

int TOCMerge::prepare_merge_toc(KarToc *toc, KarToc *uptoc, KarToc *tmptoc)
{
    Kok_IdxFileHdr_S *mainHeader = NULL;
    Kok_IdxFileHdr_S *updateHeader = NULL;
    Person_Info_S * pArtistInfo = NULL;
    Song_Info_S *pSongInfo = NULL;
    uint8_t *pSongName = NULL;
    uint8_t *pSongPyName = NULL;
    uint8_t *pSingerName = NULL;
    uint8_t *pSingerPYName = NULL;
    uint8_t *pVideoMKVName = NULL;
    uint8_t *pVideoJPGName = NULL;
    uint32_t *pMapIndex = NULL;
    int16_t mergeStatus = 0;
    uint32_t numItem = 0;
    uint32_t tableSize = 0;
    int i = 0;

    // Main Header
    mainHeader = (Kok_IdxFileHdr_S *) malloc(sizeof(Kok_IdxFileHdr_S));
    if (mainHeader == NULL) {
        printf("Error NoMem MainHeader\n");
        mergeStatus = -2;
        goto exit_merge;
    }
    memset(mainHeader, 0, sizeof(Kok_IdxFileHdr_S));
    memcpy(mainHeader, tmptoc->getHeader(), sizeof(Kok_IdxFileHdr_S));
    if(toc->gpKokIndexHdr != NULL) {
    	free(toc->gpKokIndexHdr);
    	toc->gpKokIndexHdr = NULL;
    }
    toc->gpKokIndexHdr = mainHeader;

    updateHeader = uptoc->getHeader();

    // Calculate Memory To Update
    numItem = tmptoc->getSingerCount();
    numItem += uptoc->getSingerCount();
    pArtistInfo = (Person_Info_S *) malloc(numItem * sizeof(Person_Info_S));
    if (!pArtistInfo) {
        printf("Error NoMem ArtistInfo\n");
        mergeStatus = -2;
        goto exit_merge;
    }
    memset(pArtistInfo, 0, numItem * sizeof(Person_Info_S));
    if(tmptoc->offsetToPointer(mainHeader->pSingerInfoTable) != NULL)
    	memcpy(pArtistInfo, tmptoc->offsetToPointer(mainHeader->pSingerInfoTable), mainHeader->uSingerNums * sizeof(Person_Info_S));
    for(i = 0; i < mainHeader->uSingerNums; i++) {
        pArtistInfo[i].pNameOffset -= mainHeader->pSingerNameTable;
        pArtistInfo[i].pPYOffset -= mainHeader->pSingerNamePYStrTable;
    }
    toc->pArtistInfo = pArtistInfo;

    tableSize = mainHeader->uSingerNameTableSize;
    tableSize += updateHeader->uSingerNameTableSize;
    pSingerName = (uint8_t *)malloc(tableSize);
    if (pSingerName == NULL) {
        printf("Error NoMem\n");
        mergeStatus = -2;
        goto exit_merge;
    }
    memset(pSingerName, 0, tableSize);

    if(tmptoc->offsetToPointer(mainHeader->pSingerNameTable) != NULL)
    	memcpy(pSingerName, tmptoc->offsetToPointer(mainHeader->pSingerNameTable), mainHeader->uSingerNameTableSize);
    toc->pSingerName = pSingerName;

    tableSize = mainHeader->uSingerNamePYStrTableSize;
    tableSize += updateHeader->uSingerNamePYStrTableSize;
    pSingerPYName = (uint8_t *)malloc(tableSize);
    if (pSingerPYName == NULL) {
        printf("Error NoMem\n");
        mergeStatus = -2;
        goto exit_merge;
    }
    memset(pSingerPYName, 0, tableSize);

    if(tmptoc->offsetToPointer(mainHeader->pSingerNamePYStrTable) != NULL)
    	memcpy(pSingerPYName, tmptoc->offsetToPointer(mainHeader->pSingerNamePYStrTable), mainHeader->uSingerNamePYStrTableSize);
    toc->pSingerPYName = pSingerPYName;

    // Song Info
    numItem = tmptoc->getSongCount();
    numItem+= uptoc->getSongCount();
    pSongInfo = (Song_Info_S *) malloc(numItem * sizeof(Song_Info_S));
    if (!pSongInfo) {
        printf("Error NoMem SongInfo\n");
        mergeStatus = -2;
        goto exit_merge;
    }
    memset(pSongInfo, 0, numItem * sizeof(Song_Info_S));

    if(tmptoc->offsetToPointer(mainHeader->pSongInfoTable) != NULL)
    	memcpy(pSongInfo, tmptoc->offsetToPointer(mainHeader->pSongInfoTable), mainHeader->uSongNums * sizeof(Song_Info_S));
    for(i = 0; i < mainHeader->uSongNums; i++) {
        pSongInfo[i].pSongNameOffset -= mainHeader->pSongNameTable;
        pSongInfo[i].pPYNameOffset -= mainHeader->pPYStrTable;
        if(pSongInfo[i].pSongClips != UINT32_MAX)
        	pSongInfo[i].pSongClips -= mainHeader->pVideoJpegNameTable;
        if(pSongInfo[i].property.media_type == VIDEO) {
            pSongInfo[i].VideoInfo.pVideoName -= mainHeader->pVideoMkvNameTable;
        }
    }
    toc->pSongInfo = pSongInfo;

    pMapIndex = (uint32_t *)malloc(uptoc->getSongCount() * sizeof(uint32_t));
    toc->pMapIndex = pMapIndex;

    tableSize = mainHeader->uSongNameTableSize;
    tableSize += updateHeader->uSongNameTableSize;
    pSongName = (uint8_t *)malloc(tableSize);
    if (pSongName == NULL) {
        printf("Error NoMem\n");
        mergeStatus = -2;
        goto exit_merge;
    }
    memset(pSongName, 0, tableSize);

    if(tmptoc->offsetToPointer(mainHeader->pSongNameTable) != NULL)
    	memcpy(pSongName, tmptoc->offsetToPointer(mainHeader->pSongNameTable), mainHeader->uSongNameTableSize);
    toc->pSongName = pSongName;

    tableSize = mainHeader->uPYStrTableSize;
    tableSize += updateHeader->uPYStrTableSize;
    pSongPyName = (uint8_t *)malloc(tableSize);
    if (pSongPyName == NULL) {
        printf("Error NoMem\n");
        mergeStatus = -2;
        goto exit_merge;
    }
    memset(pSongPyName, 0, tableSize);

    if(tmptoc->offsetToPointer(mainHeader->pPYStrTable) != NULL)
    	memcpy(pSongPyName, tmptoc->offsetToPointer(mainHeader->pPYStrTable), mainHeader->uPYStrTableSize);
    toc->pSongPyName = pSongPyName;

    tableSize = mainHeader->uVideoMkvNameSize;
    tableSize += updateHeader->uVideoMkvNameSize;
    pVideoMKVName = (uint8_t *)malloc(tableSize);
    if (pVideoMKVName == NULL) {
        printf("Error NoMem\n");
        mergeStatus = -2;
        goto exit_merge;
    }
    memset(pVideoMKVName, 0, tableSize);

    if(tmptoc->offsetToPointer(mainHeader->pVideoMkvNameTable) != NULL)
    	memcpy(pVideoMKVName, tmptoc->offsetToPointer(mainHeader->pVideoMkvNameTable), mainHeader->uVideoMkvNameSize);
    toc->pVideoMKVName = pVideoMKVName;

    tableSize = mainHeader->uVideoJpegNameSize;
    tableSize += updateHeader->uVideoJpegNameSize;
    pVideoJPGName = (uint8_t *)malloc(tableSize);
    if (pVideoJPGName == NULL) {
        printf("Error NoMem\n");
        mergeStatus = -2;
        goto exit_merge;
    }
    memset(pVideoJPGName, 0, tableSize);
    if(tmptoc->offsetToPointer(mainHeader->pVideoJpegNameTable) != NULL)
    	memcpy(pVideoJPGName, tmptoc->offsetToPointer(mainHeader->pVideoJpegNameTable), mainHeader->uVideoJpegNameSize);
    toc->pVideoJPGName = pVideoJPGName;

exit_merge:
    return mergeStatus;
}

int TOCMerge::merge_singer_info(KarToc *toc) {
    Kok_IdxFileHdr_S *mainHeader = toc->gpKokIndexHdr;
    Person_Info_S *tmpArtistInfo = NULL;
    Person_Info_S *artistInfo = NULL;
    int i = 0;
    int j = 0, k = 0;
    int lastSort = 0;
    int lastInsertIdx = 0;
    int existed_cnt = 0;
    
    for (i = 0; i < updateToc->getSingerCount(); i++) {
        artistInfo = updateToc->getSingerInfo(i);
        for(j = 0; j < mainHeader->uSingerNums; j++) {
            tmpArtistInfo = &(toc->pArtistInfo[j]);
            if(tmpArtistInfo && tmpArtistInfo->singerIdx == artistInfo->singerIdx) {
                // Artist Has Existed
                if (artistInfo->num_songs.bAuthor) {
                    tmpArtistInfo->num_songs.bAuthor = 1;
                }
                if (artistInfo->num_songs.bSinger) {
                    tmpArtistInfo->num_songs.bSinger = 1;
                }
                existed_cnt ++;
                lastInsertIdx = j;
                break;
            }
        }
        
        if (j >= mainHeader->uSingerNums) {
            // Insert Singer Info
            if (artistInfo->num_songs.language_type < 3) {
                lastInsertIdx = do_insert_singer(toc, artistInfo, lastSort);
                lastSort = lastInsertIdx;
            }else {
                lastInsertIdx = do_insert_singer(toc, artistInfo, mainHeader->uSingerNums);
            }

            
            for (j = 0; j < mainHeader->uSongNums; j++) {
            	Song_Info_S *songInfo = &(toc->pSongInfo[j]);
            	for (k = 0; k < MAX_ARTIST_PER_SONG; k++) {
            		if (songInfo->pPersonIdx.author_idxes[k] == UINT16_MAX) {
            			break;
            		}
            		if (songInfo->pPersonIdx.author_idxes[k] >= lastInsertIdx) {
            			songInfo->pPersonIdx.author_idxes[k] +=1;
            		}
            	}

            	for (k = 0; k < MAX_ARTIST_PER_SONG; k++) {
            		if (songInfo->pPersonIdx.singer_idxes[k] == UINT16_MAX) {
            			break;
            		}
            		if (songInfo->pPersonIdx.singer_idxes[k] >= lastInsertIdx) {
            			songInfo->pPersonIdx.singer_idxes[k] += 1;
            		}
            	}

            }
        }
    }
    return 0;
}

int TOCMerge::merge_songInfo(KarToc *toc) {
    Kok_IdxFileHdr_S *mainHeader = toc->gpKokIndexHdr;
    Song_Info_S *tmpSongInfo = NULL;
    Song_Info_S *songInfo = NULL;
    Song_Info_Extra_S *songInfoExtra = NULL;
    int i = 0, j = 0, k = 0;
    int lastSort = 0;
    int lastInsertIdx = 0;
    
    // Do delete song first
    for (i = 0; i < updateToc->getSongCount(); i++) {
    	songInfo = updateToc->getSongInfo(i);
    	songInfoExtra = updateToc->getSongInfoExtra(i);

    	if (songInfoExtra != NULL && songInfoExtra->bDelete) {
    		for (j = 0; j < mainHeader->uSongNums; j++) {
    			tmpSongInfo = &(toc->pSongInfo[j]);
    			if (tmpSongInfo->song_code5 == songInfo->song_code5
    					&& tmpSongInfo->MidiInfo.stFilepos.songABCType == songInfo->MidiInfo.stFilepos.songABCType) {
    				do_delete_song(toc, tmpSongInfo, j);
    				break;
    			}
    		}
//    		do_delete_song_update(songInfo, i);
    	}
    }

    // Next insert song
    for (i = 0; i < updateToc->getSongCount(); i++) {
        songInfo = updateToc->getSongInfo(i);
        songInfoExtra = updateToc->getSongInfoExtra(i);

        // Skip delete song, already delete these song above
        if (songInfoExtra != NULL && songInfoExtra->bDelete) continue;

        for(j = 0; j < toc->getSongCount(); j++) {
        	Song_Info_S *pSongInfo = &toc->pSongInfo[j];
        	if(pSongInfo != NULL && pSongInfo->song_code6 == songInfo->song_code6) {
//        		printf("Song existed: %d==>%s", pSongInfo->song_code6, pSongInfo->pSongNameOffset);
        		break;
        	}
        }
        if(j < toc->getSongCount()) continue;

        // Insert Song Info
        if (songInfo->property.language_type < 3) {
        	lastInsertIdx = do_insert_song(toc, songInfo, lastSort);
        	lastSort = lastInsertIdx;
        }else {
        	lastInsertIdx = do_insert_song(toc, songInfo, mainHeader->uSongNums);
        }

        // Save insert index of update song
        toc->pMapIndex[i] = lastInsertIdx;

        for (k = 0; k < MAX_ARTIST_PER_SONG; k++) {
        	if (songInfo->pPersonIdx.singer_idxes[k] == UINT16_MAX) {
        		break;
        	}

        	int singeridx = songInfo->pPersonIdx.singer_idxes[k];
        	Person_Info_S *updateArtist = updateToc->getSingerInfo(singeridx);
        	for(int idx = 0; idx < mainHeader->uSingerNums; idx++) {
        		if(toc->pArtistInfo[idx].singerIdx == updateArtist->singerIdx) {
        			toc->pSongInfo[lastInsertIdx].pPersonIdx.singer_idxes[k] = idx;
        			break;
        		}
        	}
        }

        for (k = 0; k < MAX_ARTIST_PER_SONG; k++) {
        	if (songInfo->pPersonIdx.author_idxes[k] == UINT16_MAX) {
        		break;
        	}

        	int singeridx = songInfo->pPersonIdx.author_idxes[k];
        	Person_Info_S *updateArtist = updateToc->getSingerInfo(singeridx);
        	for(int idx = 0; idx < mainHeader->uSingerNums; idx++) {
        		if(toc->pArtistInfo[idx].singerIdx == updateArtist->singerIdx) {
        			toc->pSongInfo[lastInsertIdx].pPersonIdx.author_idxes[k] = idx;
        			break;
        		}
        	}
        }
    }
    
    return 0;
}


/*
 * check all file in path start with "MEGVOL"
 * @return available size according to MAX_MEGVOL_SIZE
 * @parameter
 * path: path to search MEGVOL
 * index: index of MEGVOL file, -1 if all file in path full
 */
int getAvailableMEGVOL(const char *path, int*index, uint32_t request_size) {
	DIR *d;
	struct dirent *dir;
	int len = strlen(path) + 10;
	char *pfilename = (char *)malloc(len);
	long fileSize = 0, space = 0;
	d = opendir(path);
	if (d)
	{
		while ((dir = readdir(d)) != NULL)
		{
			*index = -1;
			space = 0;
			char *pname = dir->d_name;
			printf("name:%s===type:%d\n", dir->d_name, dir->d_type);
			if(strncmp(pname, "MEGVOL", 6) == 0) {
				memset(pfilename, 0, len);
				sprintf(pfilename, "%s/%s", path, pname);
				FILE *pFile = fopen(pfilename, "rb");
				if(pFile != NULL) {
					fseek(pFile, 0, SEEK_END);
					fileSize = ftell(pFile);
					space = MAX_MEGVOL_SIZE - fileSize;
					if(space > request_size) {
						*index = atoi(&dir->d_name[6]);
						fclose(pFile);
						break;
					}
					fclose(pFile);
				}
			}
		}

		closedir(d);
	}
	free(pfilename);
	return space;
}

/*
 * check all file in path start with "MEGMID"
 * @return available size according to MAX_MEGMID_SIZE
 * @parameter
 * path: path to search MEGMID
 * index: index of MEGMID file, -1 if all file in path full
 */
int getAvailableMEGMID(const char *path, int*index, uint32_t request_size) {
	DIR *d;
	struct dirent *dir;
	int len = strlen(path) + 10;
	char *pfilename = (char *)malloc(len);
	long fileSize = 0, space = 0;
	d = opendir(path);
	if (d)
	{
		while ((dir = readdir(d)) != NULL)
		{
			*index = -1;
			space = 0;
			printf("name:%s===type:%d\n", dir->d_name, dir->d_type);
			int cmpn = strncmp(dir->d_name, "MEGMID", 6);
			printf("Compare value: %d", cmpn);
			if(cmpn == 0) {
				memset(pfilename, 0, len);
				sprintf(pfilename, "%s/%s", path, dir->d_name);
				FILE *pFile = fopen(pfilename, "rb");
				if(pFile != NULL) {
					fseek(pFile, 0, SEEK_END);
					fileSize = ftell(pFile);
					space = MAX_MEGMID_SIZE - fileSize;
					if(space > request_size) {
						*index = atoi(&dir->d_name[6]);
						printf("outidx: %d", *index);
						fclose(pFile);
						break;
					}
					fclose(pFile);
				}
			}
		}

		closedir(d);
	}
	free(pfilename);
	printf("outidx: %d space left: %d", *index, space);
	return space;
}

int TOCMerge::merge_song_megvol_mid(const char *subIdxPath, const char *updateIdxPath, int max_megvol, int max_megmid, KarToc *main, KarToc *sub)
{
	Song_Info_S *mainSongInfo = NULL;
	Song_Info_S *upSongInfo = NULL;
	int i = 0;
	int vol_idx = 0;
	int mid_idx = 0;
	long vol_remain = 0;
	long mid_remain = 0;

	uint32_t filesize = 0;
	uint32_t fileoffset = 0;

	// Check for valid song info merge
	for (i = 0; i < updateToc->getSongCount(); i++) {
		upSongInfo = updateToc->getSongInfo(i);
		mainSongInfo = &(subToc->pSongInfo[subToc->pMapIndex[i]]);
		if(upSongInfo->song_code6 != mainSongInfo->song_code6) {
			printf("Invalid song info merge");
			return -1;
		}

		// Check in valid maintoc
		mainSongInfo = &(mainToc->pSongInfo[mainToc->pMapIndex[i]]);
		if(upSongInfo->song_code6 != mainSongInfo->song_code6) {
			printf("Invalid song info merge main toc");
			return -1;
		}
	}

	int len = strlen(subIdxPath) + 16;
	char *curMegVolPath = (char *)malloc(len);
	memset(curMegVolPath, 0, len);

	len = strlen(updateIdxPath) + 16;
	char *srcMegVolPath = (char *)malloc(len);
	memset(srcMegVolPath, 0, len);

	len = strlen(subIdxPath) + 16;
	char *curMegMidPath = (char *)malloc(len);
	memset(curMegMidPath, 0, len);

	len = strlen(updateIdxPath) + 16;
	char *srcMegMidPath = (char *)malloc(len);
	memset(srcMegMidPath, 0, len);

	uint32_t buf_size = 32*1024;
	uint8_t *copy_buf = (uint8_t *)malloc(buf_size);

	// Next insert song
	for (i = 0; i < updateToc->getSongCount(); i++) {
		upSongInfo = updateToc->getSongInfo(i);

		// Update MP3 Song
		if(upSongInfo->property.media_type == MP3 || upSongInfo->property.media_type == SINGER) {
			filesize = upSongInfo->Mp3Info.filesize;
			filesize *= KOK_2K;

			printf("merge mp3 song id: %d", upSongInfo->song_code6);
			if(vol_remain < filesize) {
				vol_remain = getAvailableMEGVOL(subIdxPath, &vol_idx, filesize);
				if(vol_idx == -1 || vol_remain < filesize) {
					printf("All megvol file are full. Create new one at %s/MEGVOL%d\n", subIdxPath, max_megvol+1);
					vol_idx = max_megvol+1;
					vol_remain = MAX_MEGVOL_SIZE;
				}

				if(vol_idx > 0) {
					sprintf(curMegVolPath, "%s/MEGVOL%d", subIdxPath, vol_idx);
				}else {
					sprintf(curMegVolPath, "%s/MEGVOL", subIdxPath);
				}
				printf("Copy mp3 file to %s\n", curMegVolPath);
			}

			// Copy MP3 data from update to sub
			FILE *dstMegvol = fopen(curMegVolPath, "ab+");
			if(dstMegvol == NULL) {
				printf("unable to open dst file to write file %s", curMegVolPath);
				goto exit_merge;
			}

			if(upSongInfo->Mp3Info.stFilepos.fileIdx > 0) {
				sprintf(srcMegVolPath, "%s/MEGVOL%d", updateIdxPath, upSongInfo->Mp3Info.stFilepos.fileIdx);
			}else {
				sprintf(srcMegVolPath, "%s/MEGVOL", updateIdxPath);
			}

			FILE *srcMegvol = fopen(srcMegVolPath, "rb");
			if(srcMegvol == NULL) {
				printf("unable to open src file to read file %s", srcMegVolPath);
				goto exit_merge;
			}

			fileoffset = upSongInfo->Mp3Info.stFilepos.fileOffset * 2048;
			uint32_t len = 0;
			int byteRead = 0;

			fseek(srcMegvol, fileoffset, SEEK_SET);
			fseek(dstMegvol, 0, SEEK_END);
			long tmpSize = ftell(dstMegvol);

			if((tmpSize % KOK_2K) > 0) {
				printf("Invalid file size, not modulo with kok_2k");
			}

			// Update idx of
			subToc->pSongInfo[subToc->pMapIndex[i]].Mp3Info.stFilepos.fileIdx = vol_idx;
			subToc->pSongInfo[subToc->pMapIndex[i]].Mp3Info.stFilepos.fileOffset = tmpSize / KOK_2K;

			mainToc->pSongInfo[mainToc->pMapIndex[i]].Mp3Info.stFilepos.fileIdx = vol_idx;
			mainToc->pSongInfo[mainToc->pMapIndex[i]].Mp3Info.stFilepos.fileOffset = tmpSize / KOK_2K;

			do {
				byteRead = fread(copy_buf, 1, buf_size, srcMegvol);
				if(byteRead < 0) {
					break;
				}
				fwrite(copy_buf, 1, byteRead, dstMegvol);
				len += byteRead;
			}while(byteRead > 0 && len < filesize);
			vol_remain -= filesize;

			fclose(dstMegvol);
			fclose(srcMegvol);
		}

		// Update MIDI Song
		if(upSongInfo->property.media_type != VIDEO) {
			filesize = upSongInfo->MidiInfo.filesize;
			filesize *= KOK_2K;

			printf("merge midi song id: %d", upSongInfo->song_code6);
			if(mid_remain < filesize) {
				mid_remain = getAvailableMEGMID(subIdxPath, &mid_idx, filesize);
				if(mid_idx == -1 || mid_remain < filesize) {
					printf("All megmid file are full. Create new one at %s/MEGMID%d\n", subIdxPath, max_megmid+1);
					mid_idx = max_megmid+1;
					mid_remain = MAX_MEGMID_SIZE;
				}

				if(mid_idx > 0) {
					sprintf(curMegMidPath, "%s/MEGMID%d", subIdxPath, mid_idx);
				}else {
					sprintf(curMegMidPath, "%s/MEGMID", subIdxPath);
				}
				printf("Copy midi file to %s\n", curMegMidPath);
			}

			// Copy MID data from update to sub
			FILE *dstMegmid = fopen(curMegMidPath, "ab+");
			if(dstMegmid == NULL) {
				printf("unable to open dst file to write midi file %s", curMegMidPath);
				goto exit_merge;
			}

			if(upSongInfo->MidiInfo.stFilepos.fileIdx > 0) {
				sprintf(srcMegMidPath, "%s/MEGMID%d", updateIdxPath, upSongInfo->MidiInfo.stFilepos.fileIdx);
			}else {
				sprintf(srcMegMidPath, "%s/MEGMID", updateIdxPath);
			}

			FILE *srcMegMid = fopen(srcMegMidPath, "rb");
			if(srcMegMid == NULL) {
				printf("unable to open file to read midi file %s", srcMegMidPath);
				goto exit_merge;
			}

			fileoffset = upSongInfo->MidiInfo.stFilepos.fileOffset * 2048;
			uint32_t len = 0;
			int byteRead = 0;

			fseek(srcMegMid, fileoffset, SEEK_SET);
			fseek(dstMegmid, 0, SEEK_END);
			long tmpSize = ftell(dstMegmid);

			if((tmpSize % KOK_2K) > 0) {
				printf("Invalid file size, not modulo with kok_2k");
			}

			// Update idx of
			subToc->pSongInfo[subToc->pMapIndex[i]].MidiInfo.stFilepos.fileIdx = vol_idx;
			subToc->pSongInfo[subToc->pMapIndex[i]].MidiInfo.stFilepos.fileOffset = tmpSize / KOK_2K;

			mainToc->pSongInfo[mainToc->pMapIndex[i]].MidiInfo.stFilepos.fileIdx = vol_idx;
			mainToc->pSongInfo[mainToc->pMapIndex[i]].MidiInfo.stFilepos.fileOffset = tmpSize / KOK_2K;

			do {
				byteRead = fread(copy_buf, 1, buf_size, srcMegMid);
				if(byteRead < 0) {
					break;
				}
				fwrite(copy_buf, 1, byteRead, dstMegmid);
				len += byteRead;
			}while(byteRead > 0 && len < filesize);
			fclose(dstMegmid);
			fclose(srcMegMid);
		}
	}

	exit_merge:
	free(copy_buf);
	free(curMegVolPath);
	free(srcMegVolPath);
	free(curMegMidPath);
	free(srcMegMidPath);
	return 0;
}

uint32_t TOCMerge::do_insert_singer(KarToc *toc, Person_Info_S *artistInfo, uint32_t index)
{
    Kok_IdxFileHdr_S *mainHeader = toc->gpKokIndexHdr;
    int artistCnt = mainHeader->uSingerNums;
    uint16_t length = 0;
    uint32_t outIdx = index;
    char* name = NULL;
    if (index >= artistCnt) {
        outIdx = index;
    } else {
        name = updateToc->offsetToPointer(artistInfo->pNameOffset);
        outIdx = find_singer_index(toc, name, outIdx + 1, artistInfo->num_songs.language_type);
        
        memmove(&toc->pArtistInfo[outIdx+1], &toc->pArtistInfo[outIdx], (artistCnt - outIdx) * sizeof(Person_Info_S));
    }
    
    memcpy(&toc->pArtistInfo[outIdx], artistInfo, sizeof(Person_Info_S));
    toc->pArtistInfo[outIdx].num_songs.uAuthorSongNums += artistInfo->num_songs.uAuthorSongNums;
    toc->pArtistInfo[outIdx].num_songs.uSingerSongNums += artistInfo->num_songs.uSingerSongNums;
    
    length = strlen(updateToc->offsetToPointer(artistInfo->pNameOffset)) + 1;
    memcpy(&toc->pSingerName[mainHeader->uSingerNameTableSize], updateToc->offsetToPointer(artistInfo->pNameOffset), length);
    toc->pArtistInfo[outIdx].pNameOffset = mainHeader->uSingerNameTableSize;
    mainHeader->uSingerNameTableSize += length;
    
    length = strlen(updateToc->offsetToPointer(artistInfo->pPYOffset)) + 1;
    memcpy(&toc->pSingerPYName[mainHeader->uSingerNamePYStrTableSize], updateToc->offsetToPointer(artistInfo->pPYOffset), length);
    toc->pArtistInfo[outIdx].pPYOffset = mainHeader->uSingerNamePYStrTableSize;
    mainHeader->uSingerNamePYStrTableSize += length;
    
    artistCnt += 1;
    mainHeader->uSingerNums = artistCnt;
    return outIdx;
}

int TOCMerge::find_singer_index(KarToc *toc, char *pName, int startIdx, uint8_t lang_type) {
    Kok_IdxFileHdr_S *mainHeader = toc->gpKokIndexHdr;
    uint32_t idx = 0;
    int compare = 0;
    Person_Info_S artisInfo;
    uint8_t *pArtistName = NULL;
    
    for (idx = startIdx; idx < mainHeader->uSingerNums; idx++) {
        artisInfo = toc->pArtistInfo[idx];
        
        pArtistName = (uint8_t *)(toc->pSingerName + artisInfo.pNameOffset);
        
        compare = Kok_CmpUTF8Str((char *)pName, (char *)pArtistName);
        if (compare < 0) {
            break;
        }
    }
    
    return idx;
}

int TOCMerge::find_song_index(KarToc *toc, char *pName, int startIdx, uint8_t lang_type) {
    Kok_IdxFileHdr_S *mainHeader = toc->gpKokIndexHdr;
    uint32_t idx = 0;
    int compare = 0;
    Song_Info_S songInfo;
    uint8_t *songName = NULL;
    
    for (idx = startIdx; idx < mainHeader->uSongNums; idx++) {
        songInfo = toc->pSongInfo[idx];
        songName = (uint8_t *)(toc->pSongName + songInfo.pSongNameOffset);
        
        compare = Kok_CmpUTF8Str((char *)pName, (char *)songName);
        if (compare < 0) {
            break;
        }
    }
    
    return idx;
}

uint32_t TOCMerge::do_insert_song(KarToc *toc, Song_Info_S * songInfo, uint32_t index)
{
	Kok_IdxFileHdr_S *mainHeader = toc->gpKokIndexHdr;
    int songCnt = mainHeader->uSongNums;
    uint16_t length = 0;
    uint32_t outIdx = index;
    char* name = NULL;
    
//    printf("Do insert song: %d-%d, index: %d\n", songInfo->song_code5, songInfo->song_code6, index);
    if (index >= songCnt) {
        outIdx = index;
    } else {
        name = updateToc->offsetToPointer(songInfo->pSongNameOffset);
        outIdx = find_song_index(toc, name, outIdx+1, songInfo->property.language_type);

        memmove(&toc->pSongInfo[outIdx+1], &toc->pSongInfo[outIdx], (songCnt - outIdx) * sizeof(Song_Info_S));
    }
    
    memcpy(&toc->pSongInfo[outIdx], songInfo, sizeof(Song_Info_S));
    
    toc->pSongInfo[outIdx].pSongNameOffset = mainHeader->uSongNameTableSize;
    length = strlen(updateToc->offsetToPointer(songInfo->pSongNameOffset));
    memcpy(&toc->pSongName[mainHeader->uSongNameTableSize], updateToc->offsetToPointer(songInfo->pSongNameOffset), length);
    mainHeader->uSongNameTableSize += length;
    toc->pSongName[mainHeader->uSongNameTableSize] = '\0';
    mainHeader->uSongNameTableSize += 1;
    
    toc->pSongInfo[outIdx].pPYNameOffset = mainHeader->uPYStrTableSize;
    length = strlen(updateToc->offsetToPointer(songInfo->pPYNameOffset));
    memcpy(&toc->pSongPyName[mainHeader->uPYStrTableSize], updateToc->offsetToPointer(songInfo->pPYNameOffset), length);
    mainHeader->uPYStrTableSize += length;
    toc->pSongPyName[mainHeader->uPYStrTableSize] = '\0';
    mainHeader->uPYStrTableSize += 1;
    
    if(songInfo->pSongClips != UINT32_MAX) {
    	toc->pSongInfo[outIdx].pSongClips = mainHeader->uVideoJpegNameSize;

    	uint8_t* pSongClip = (uint8_t *)(updateToc->offsetToPointer(songInfo->pSongClips));
    	uint8_t numclip = *pSongClip;
    	length = numclip * 2+1;
    	pSongClip += length;
    	while ((*pSongClip++) & (1<< 7)) {
    		length++;
    	}
    	memcpy(&toc->pVideoJPGName[mainHeader->uVideoJpegNameSize], updateToc->offsetToPointer(songInfo->pSongClips), length);
    	mainHeader->uVideoJpegNameSize += length;
    }else {
    	toc->pSongInfo[outIdx].pSongClips = 0;//mainHeader->pVideoJpegNameTable;
    }

    if(toc->pSongInfo[outIdx].property.media_type == VIDEO) {
    	toc->pSongInfo[outIdx].VideoInfo.pVideoName = mainHeader->uVideoMkvNameSize;
        
        length = 0;
        Kok_IdxFileHdr_S *updateHeader = updateToc->getHeader();
        if(updateHeader->uVideoMkvNameSize > 0 && updateHeader->pVideoMkvNameTable != songInfo->VideoInfo.pVideoName) {
            char *pMKVName = (char *)(updateToc->offsetToPointer(songInfo->VideoInfo.pVideoName));
            length = strlen(pMKVName);
            memcpy(&toc->pVideoMKVName[mainHeader->uVideoMkvNameSize], updateToc->offsetToPointer(songInfo->VideoInfo.pVideoName), length);
            length += 1;
        }
        mainHeader->uVideoMkvNameSize += length;
    }
    
    songCnt += 1;
    mainHeader->uSongNums = songCnt;
    return outIdx;
}

void TOCMerge::do_delete_song_update(Song_Info_S * updateInfo, uint32_t index)
{
	if(updateInfo->song_code6 != 0) return;

	int count = updateToc->getHeader()->uSongNums;
    if (index > count) {
        printf("Invalid Index: %d", index);
        return;
    }
    
    memmove(updateToc->getSongInfo(index), updateToc->getSongInfo(index + 1), (count - index - 1) * sizeof(Song_Info_S));
    updateToc->getHeader()->uSongNums -=1;
}

void TOCMerge::do_delete_song(KarToc *toc, Song_Info_S * songInfo, uint32_t index)
{
	Kok_IdxFileHdr_S *mainHeader = toc->gpKokIndexHdr;
	int count = mainHeader->uSongNums;
	if (index > count) {
		printf("Invalid Index: %d", index);
		return;
	}

    /*
	for(int i = 0; i < MAX_ARTIST_PER_SONG; i++) {
		if(songInfo->pPersonIdx.author_idxes[i] == UINT16_MAX) break;
		for(int idxArtist = 0; idxArtist < mainHeader->uSingerNums; idxArtist++) {
			Person_Info_S *artist = &toc->pArtistInfo[idxArtist];

			if(songInfo->pPersonIdx.author_idxes[i] == idxArtist) {
				if(artist->num_songs.uAuthorSongNums > 0) {
					artist->num_songs.uAuthorSongNums -= 1;
					if(artist->num_songs.uAuthorSongNums == 0) {
						artist->num_songs.bAuthor = 0;
					}
				}
				break;
			}
		}
	}

	for(int i = 0; i < MAX_ARTIST_PER_SONG; i++) {
		if(songInfo->pPersonIdx.singer_idxes[i] == UINT16_MAX) break;
		for(int idxArtist = 0; idxArtist < mainHeader->uSingerNums; idxArtist++) {
			Person_Info_S *artist = &toc->pArtistInfo[idxArtist];

			if(songInfo->pPersonIdx.singer_idxes[i] == idxArtist) {
				if(artist->num_songs.uSingerSongNums > 0) {
					artist->num_songs.uSingerSongNums -= 1;
					if(artist->num_songs.uSingerSongNums == 0) {
						artist->num_songs.bSinger = 0;
					}
				}
				break;
			}
		}
	}
    */

	memmove((uint8_t *)(&toc->pSongInfo[index]), (uint8_t *)(&toc->pSongInfo[index + 1]), (count - index - 1) * sizeof(Song_Info_S));
	mainHeader->uSongNums -=1;
}

void TOCMerge::prepare_write_file(KarToc *toc, int type)
{
	Kok_IdxFileHdr_S *mainHeader = toc->gpKokIndexHdr;
    uint32_t offset = 0;
    int i = 0;
    if(updateToc != NULL) {
    	Kok_IdxFileHdr_S *updateHeader = updateToc->getHeader();

    	if(type == UPDATE_TYPE_DISC) {
    		mainHeader->Magic.Version = updateHeader->Magic.Version;
    		mainHeader->Magic.Revision = updateHeader->Magic.Revision;
    	}else if(type == UPDATE_TYPE_SCDISC) {
    		mainHeader->Magic.Version1 = updateHeader->Magic.Version1;
    		mainHeader->Magic.Revision1 = updateHeader->Magic.Revision1;
    	}else if(type == UPDATE_TYPE_XUSER) {
    		mainHeader->Magic.Version2 = updateHeader->Magic.Version2;
    		mainHeader->Magic.Revision2 = updateHeader->Magic.Revision2;
    	}else if(type == UPDATE_TYPE_USER) {
    		mainHeader->Magic.Version3 = updateHeader->Magic.Version3;
    		mainHeader->Magic.Revision3 = updateHeader->Magic.Revision3;
    	}
    }

    mainHeader->file_len = 0;

    offset = sizeof(Kok_IdxFileHdr_S);
    mainHeader->uSingerInfoTableSize = (mainHeader->uSingerNums * sizeof(Person_Info_S));
    mainHeader->pSingerInfoTable = offset;
    offset += mainHeader->uSingerInfoTableSize;
    
    mainHeader->uSongInfoTableSize = mainHeader->uSongNums * sizeof(Song_Info_S);
    mainHeader->pSongInfoTable = offset;
    offset += mainHeader->uSongInfoTableSize;
    
    mainHeader->pSingerNameTable = offset;
    offset += mainHeader->uSingerNameTableSize;
    
    mainHeader->pSongNameTable = offset;
    offset += mainHeader->uSongNameTableSize;
    
    mainHeader->uPYStrNums = 0;
    mainHeader->pPYStrTable = offset;
    offset += mainHeader->uPYStrTableSize;
    
    mainHeader->uSingerNamePYStrNums = 0;
    mainHeader->pSingerNamePYStrTable = offset;
    offset += mainHeader->uSingerNamePYStrTableSize;
    
    mainHeader->pVideoMkvNameTable = offset;
    offset += mainHeader->uVideoMkvNameSize;
    
    mainHeader->pVideoJpegNameTable = offset;
    offset += mainHeader->uVideoJpegNameSize;

    printf("prepare_write_file==> pArtistInfo");
    for (i = 0; i < mainHeader->uSingerNums; i++) {
    	toc->pArtistInfo[i].pNameOffset += mainHeader->pSingerNameTable;
    	toc->pArtistInfo[i].pPYOffset += mainHeader->pSingerNamePYStrTable;
    }

    printf("prepare_write_file==> pSongNameOffset");
    for (i = 0; i < mainHeader->uSongNums; i++) {
    	toc->pSongInfo[i].pSongNameOffset += mainHeader->pSongNameTable;
    	toc->pSongInfo[i].pPYNameOffset += mainHeader->pPYStrTable;
        if(toc->pSongInfo[i].pSongClips != UINT32_MAX)
        	toc->pSongInfo[i].pSongClips += mainHeader->pVideoJpegNameTable;
        if (toc->pSongInfo[i].property.media_type == VIDEO) {
        	toc->pSongInfo[i].VideoInfo.pVideoName += mainHeader->pVideoMkvNameTable;
        }
    }
    
    mainHeader->file_len = offset;
}

void TOCMerge::finalize_process()
{
//    if(mainHeader != NULL) {
//    	free(mainHeader);
//    	mainHeader = NULL;
//    }
//    if(pArtistInfo != NULL) {
//    	free(pArtistInfo);
//    	pArtistInfo = NULL;
//    }
//    if(pSongInfo != NULL) {
//    	free(pSongInfo);
//    	pSongInfo = NULL;
//    }
//    if(pMapIndex != NULL) {
//    	free(pMapIndex);
//    	pMapIndex = NULL;
//    }
//    if(pSingerName != NULL) {
//    	free(pSingerName);
//    	pSingerName = NULL;
//    }
//    if(pSongName != NULL) {
//    	free(pSongName);
//    	pSongName = NULL;
//    }
//    if(pSongPyName != NULL) {
//    	free(pSongPyName);
//    	pSongPyName = NULL;
//    }
//    if(pSingerPYName != NULL) {
//    	free(pSingerPYName);
//    	pSingerPYName = NULL;
//    }
//    if(pVideoJPGName != NULL) {
//    	free(pVideoJPGName);
//    	pVideoJPGName = NULL;
//    }
//    if(pVideoMKVName != NULL) {
//    	free(pVideoMKVName);
//    	pVideoMKVName = NULL;
//    }
}

void TOCMerge::write_file(KarToc *toc, const char *filePath)
{
	Kok_IdxFileHdr_S *mainHeader = toc->gpKokIndexHdr;
    char pRawPath[256];
    memset(pRawPath, 0, 256);
    sprintf(pRawPath, "%s_raw", filePath);
    FILE *pOutFile = fopen(pRawPath, "wb");
    if (pOutFile == NULL) {
        printf("Error while create output file\n");
        return;
    }
    uint32_t headerSize = sizeof(Kok_IdxFileHdr_S);
    uint32_t singerInfoSize = mainHeader->uSingerInfoTableSize;
    uint32_t songInfoSize = mainHeader->uSongInfoTableSize;
    uint32_t singerNameSize = mainHeader->uSingerNameTableSize;
    uint32_t songNameSize = mainHeader->uSongNameTableSize;
    uint32_t songPYNameSize = mainHeader->uPYStrTableSize;
    uint32_t singerPYNameSize = mainHeader->uSingerNamePYStrTableSize;
    uint32_t videoMKVSize = mainHeader->uVideoMkvNameSize;
    uint32_t videoJPEGSize = mainHeader->uVideoJpegNameSize;
    
//    mainToc->iPasswordStreamPos = 0;
//    mainToc->PasswordMaskBytesIdx((uint8_t *)mainHeader, 0, headerSize, (char *)mainToc->disk_keys, true);
    size_t len = fwrite(mainHeader, 1, headerSize, pOutFile);
    
//    mainToc->PasswordMaskBytesIdx((uint8_t *)pArtistInfo, 0, singerInfoSize, (char *)mainToc->disk_keys, true);
    len = fwrite(toc->pArtistInfo, 1, singerInfoSize, pOutFile);
    
//    mainToc->PasswordMaskBytesIdx((uint8_t *)pSongInfo, 0, songInfoSize, (char *)mainToc->disk_keys, true);
    len = fwrite(toc->pSongInfo, 1, songInfoSize, pOutFile);
    
//    mainToc->PasswordMaskBytesIdx((uint8_t *)pSingerName, 0, singerNameSize, (char *)mainToc->disk_keys, true);
    len = fwrite(toc->pSingerName, 1, singerNameSize, pOutFile);
    
//    mainToc->PasswordMaskBytesIdx((uint8_t *)pSongName, 0, songNameSize, (char *)mainToc->disk_keys, true);
    len = fwrite(toc->pSongName, 1, songNameSize, pOutFile);
    
//    mainToc->PasswordMaskBytesIdx((uint8_t *)pSongPyName, 0, songPYNameSize, (char *)mainToc->disk_keys, true);
    len = fwrite(toc->pSongPyName, 1, songPYNameSize, pOutFile);
    
//    mainToc->PasswordMaskBytesIdx((uint8_t *)pSingerPYName, 0, singerPYNameSize, (char *)mainToc->disk_keys, true);
    len = fwrite(toc->pSingerPYName, 1, singerPYNameSize, pOutFile);
    
//    mainToc->PasswordMaskBytesIdx((uint8_t *)pVideoMKVName, 0, videoMKVSize, (char *)mainToc->disk_keys, true);
    len = fwrite(toc->pVideoMKVName, 1, videoMKVSize, pOutFile);
    
//    mainToc->PasswordMaskBytesIdx((uint8_t *)pVideoJPGName, 0, videoJPEGSize, (char *)mainToc->disk_keys, true);
    len = fwrite(toc->pVideoJPGName, 1, videoJPEGSize, pOutFile);
    fclose(pOutFile);
    
    pOutFile = fopen(pRawPath, "rb");
    if (pOutFile == NULL) {
        printf("Error while create output file\n");
        return;
    }
    
    size_t filesize = 0;
    fseek(pOutFile, 0, SEEK_END);
    filesize = ftell(pOutFile);
    fseek(pOutFile, 0, SEEK_SET);
    
    uint8_t *pmem = (uint8_t *)malloc(filesize);
    memset(pmem, 0, filesize);
    fread(pmem, 1, filesize, pOutFile);
    fclose(pOutFile);
    
    mainToc->iPasswordStreamPos = 0;
    mainToc->PasswordMaskBytesIdx(pmem, 0, (uint32_t)filesize, (char *)(mainToc->disk_keys), true);
    
    pOutFile = fopen(filePath, "wb");
    if (pOutFile == NULL) {
        printf("Error while create output file\n");
        return;
    }
    fwrite(pmem, 1, filesize, pOutFile);
    fclose(pOutFile);
    free(pmem);

    remove(pRawPath);
}

TOCMerge::TOCMerge()
{
	maxMegMid = 0;
	maxMegVol = 0;
	mainToc = NULL;
	subToc = NULL;
	updateToc = NULL;
//	mainHeader = NULL;
//	pArtistInfo = NULL;
//	pSongInfo = NULL;
//	pMapIndex = NULL;
//	pSongName = NULL;
//	pSongPyName = NULL;
//	pSingerName = NULL;
//	pSingerPYName = NULL;
//	pVideoMKVName = NULL;
//	pVideoJPGName = NULL;
}

TOCMerge::~TOCMerge()
{
	if(mainToc) delete mainToc;
	mainToc = NULL;
	if(subToc) delete subToc;
	subToc = NULL;
	if(updateToc) delete updateToc;
	updateToc = NULL;
}

char Specicalchar[] = "()";//bo dau nay _!\"#$%&\-*+,./:;<=>?@[\\]^`{|}~\' do casy co ten nay
char Afamily[] = "ÁáÀàẢảÃãẠạ";
char AWfamily[] = "ĂăẮắẰằẲẳẴẵẶặ";
char AAfamily[] = "ÂâẤấẦầẨẩẪẫẬậ";
char Dfamily[] = "Đđ";
char Efamily[] = "ÉéÈèẺẻẼẽẸẹ";
char EEfamily[] = "ÊêẾếỀềỂểỄễỆệ";
char Ifamily[] = "ÍíÌìỈỉĨĩỊị";
char Ofamily[] = "ÓóÒòỎỏÕõỌọ";
char OOfamily[] = "ÔôỐốỒồỔổỖỗỘộ";
char OWfamily[] = "ƠơỚớỜờỞởỠỡỢợ";
char Ufamily[] = "ÚúÙùỦủŨũỤụ";
char UWfamily[] = "ƯưỨứỪừỬửỮữỰự";
char Yfamily[] = "ÝýỲỳỶỷỸỹỴỵ";

char utf8sort[] = " !#%&'()+,-.:;<=>@^[]_|{}0123456789AaÁáÀàẢảÃãẠạĂăẮắẰằẲẳẴẵẶặÂâẤấẦầẨẩẪẫẬậBbCcDDdĐđEeÉéÈèẺẻẼẽẸẹÊêẾếỀềỂểỄễỆệFfGgHhIiÍíÌìỈỉĨĩỊịJjKkLlMmNnOoÓóÒòỎỏÕõỌọÔôỐốỒồỔổỖỗỘộƠơỚớỜờỞởỠỡỢợPpQqRrSsTtUuÚúÙùỦủŨũỤụƯưỨứỪừỬửỮữỰựVvWwXxYyÝýỲỳỶỷỸỹỴỵZz";
char utf8lower[] = " !#%&'()+,-.:;<=>@^[]_|{}0123456789abcdefghijklmnopqrstuvwxyzáàảãạăắằẳẵặâấầẩẫậéèẻẽẹêếềểễệíìỉĩịóòỏõọôốồổỗộơớờởỡợúùủũụưứừửữựýỳỷỹỵđ";
char utf8upper[] = " !#%&'()+,-.:;<=>@^[]_|{}0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZÁÀẢÃẠĂẮẰẲẴẶÂẤẦẨẪẬÉÈẺẼẸÊẾỀỂỄỆÍÌỈĨỊÓÒỎÕỌÔỐỒỔỖỘƠỚỜỞỠỢÚÙỦŨỤƯỨỪỬỮỰÝỲỶỸỴĐ";

#define KOK_UTF8_SUPPORT

#define diag_printf printf
#define strncmpi	strncasecmp
/********************************************************************************
 *								FUNCTION  DECLARATIONS							*
 ********************************************************************************/
// Phuc add

INT32  Kok_Is_Utf8_Char(char *Ptr, UINT32 NumByte);
INT16 Kok_getNumByteUtf8Char(char *CurrentPtr); 

#ifdef KOK_UTF8_SUPPORT
const char A_family[] = "AaÁáÀàẢảÃãẠạĂăẮắẰằẲẳẴẵẶặÂâẤấẦầẨẩẪẫẬậ";
const char E_family[] = "EeÉéÈèẺẻẼẽẸẹÊêẾếỀềỂểỄễỆệ";
const char I_family[] = "IiÍíÌìỈỉĨĩỊị";
const char O_family[] = "OoÓóÒòỎỏÕõỌọÔôỐốỒồỔổỖỗỘộƠơỚớỜờỞởỠỡỢợ";
const char U_family[] = "UuÚúÙùỦủŨũỤụƯưỨứỪừỬửỮữỰự";
const char Y_family[] = "YyÝýỲỳỶỷỸỹỴỵ";
const char D_family[] = "DdĐđ";

void Kok_UpperCase2LowerCaseUTF8(char *src)
{
    int i;
    int NumByteUTF8;
    char tmp[5];
    //printf("\nKok_UpperCase2LowerCaseUTF8\n");
    //printf("\nsrc[%s]\n",des);
    char *pch;
    int lenUpperStr = strlen(utf8upper);
    int len1;
    int len2;
    for(i=0;i<strlen(src);) {
        NumByteUTF8 = Kok_getNumByteUtf8Char(src+i);
        memcpy(tmp,(src+i),NumByteUTF8);
        tmp[NumByteUTF8] = 0;
        pch = strstr(utf8upper,tmp);
        if(pch) {
            len1 = strlen(pch);
            len2 = (lenUpperStr - len1);
            memcpy((src+i),(utf8lower+len2),NumByteUTF8);
        }
        i += NumByteUTF8;
    }
    //printf("\ndes[%s]\n",des);
}

char Kok_getAlphaOfUTF8String(char *utf8String)
{
    char sdRet = ' ';
    if(((unsigned char)*utf8String < 0x80) && ((unsigned char)*utf8String != 0x00))
    {
        if((utf8String[0] >= '0')&&(utf8String[0] <= '9')) {
            sdRet = '0';
        }
        else
        {
            char c = toupper(utf8String[0]);
            if((c >= 'A')&&(c <= 'Z'))	{
                sdRet = c;
            }
        }
        return sdRet;
    }
    char utf8FirstChar[5];
    memset(utf8FirstChar,0,5);
    if(((unsigned char)*utf8String & 0xf8) == 0xf0 )
    {
        //if(!Kok_Is_Utf8_Char(utf8String,4)) diag_printf("\n UTF8 ERR4");
        memcpy(utf8FirstChar,utf8String,4);
    }
    else if(((unsigned char)*utf8String & 0xf0) == 0xe0 )
    {
        
        //if(!Kok_Is_Utf8_Char(utf8String,3)) diag_printf("\n UTF8 ERR3");
        memcpy(utf8FirstChar,utf8String,3);
    }
    else if(((unsigned char)*utf8String & 0xe0) == 0xc0  )
    {
        //if(!Kok_Is_Utf8_Char(utf8String,2)) diag_printf("\n UTF8 ERR2");
        memcpy(utf8FirstChar,utf8String,2);
    }
    if((strstr(A_family,utf8FirstChar)) != 0) {
        sdRet = 'A';
    }else if((strstr(E_family,utf8FirstChar)) != 0) {
        sdRet = 'E';
    }else if((strstr(I_family,utf8FirstChar)) != 0) {
        sdRet = 'I';
    }else if((strstr(O_family,utf8FirstChar)) != 0) {
        sdRet = 'O';
    }else if((strstr(U_family,utf8FirstChar)) != 0) {
        sdRet = 'U';
    }else if((strstr(Y_family,utf8FirstChar)) != 0) {
        sdRet = 'Y';
    }else if((strstr(D_family,utf8FirstChar)) != 0) {
        sdRet = 'D';
    }
    return sdRet;
}
#endif

//Loc add
bool   Kok_IsSpecialChar(char *src)
{
    diag_printf("\n----Kok_IsSpecialChar----\n");
    char *tmp;
    tmp = (char *)malloc(1);
    memcpy(tmp,src,1);
    if(strstr(Specicalchar,tmp) != 0) {
        free(tmp);
        return true;
    }else{
        free(tmp);
        return false;
    }
}
char UnsignedChar[3];
char	*Kok_convertUTF8ToCharAscii(char *src,INT16 size)
{
    //printf("\n---Kok_convertUTF8ToCharAscii---\n");
    
    //sdRet = (char *)malloc(3);
    memset(UnsignedChar,0x0,3);
    char *tmp;
    tmp = (char *)malloc(size);
    memcpy(tmp,src,size);
    memcpy(tmp+size,"\0",1);
    if((strstr(Afamily,tmp)) != 0) {
        memcpy(UnsignedChar,"a\0",2);
    }else if((strstr(AWfamily,tmp)) != 0) {
        memcpy(UnsignedChar,"ă\0",3);
    }else if((strstr(AAfamily,tmp)) != 0) {
        memcpy(UnsignedChar,"â\0",3);
    }else if((strstr(Dfamily,tmp)) != 0) {
        memcpy(UnsignedChar,"đ\0",3);
    }else if((strstr(Efamily,tmp)) != 0) {
        memcpy(UnsignedChar,"e\0",2);
    }else if((strstr(EEfamily,tmp)) != 0) {
        memcpy(UnsignedChar,"ê\0",3);
    }else if((strstr(Ifamily,tmp)) != 0) {
        memcpy(UnsignedChar,"i\0",2);
    }else if((strstr(Ofamily,tmp)) != 0) {
        memcpy(UnsignedChar,"o\0",2);
    }else if((strstr(OOfamily,tmp)) != 0) {
        memcpy(UnsignedChar,"ô\0",3);
    }else if((strstr(OWfamily,tmp)) != 0) {
        memcpy(UnsignedChar,"ơ\0",3);
    }else if((strstr(Ufamily,tmp)) != 0) {
        memcpy(UnsignedChar,"u\0",2);
    }else if((strstr(UWfamily,tmp)) != 0) {
        memcpy(UnsignedChar,"ư\0",3);
    }else if((strstr(Yfamily,tmp)) != 0) {
        memcpy(UnsignedChar,"y\0",2);
    }//else sdRet = ' ';
    //printf("\n---Kok_convertUTF8ToCharAscii finish---\n");
    free(tmp);
    return UnsignedChar;
}
void   Kok_convertToStringAscii(char *dest, char *src)
{
    //printf("\n-----src[%s]-----\n",src);
    INT32 dCurrentFileSize = strlen(src);
    char *CurrentPtr = src;
    char tmp[3];
    memset(tmp,0x0,3);
    //tmp = (char *)malloc(3);
    int i = 0;
    while(dCurrentFileSize>0)//dCurrentFileSize--  &&
    {
        INT16 ret = 0;
        if(((unsigned char)*CurrentPtr & 0xf8) == 0xf0 )
        {
            ret = Kok_Is_Utf8_Char(CurrentPtr, 4);
            if(ret == 1)
            {
                memcpy(tmp,Kok_convertUTF8ToCharAscii(CurrentPtr,4),3);
                memcpy(dest+i,tmp,strlen(tmp)+1);
                i += strlen(tmp);
                //printf("\ntmp[%s][%d]\n",tmp,strlen(tmp));
                CurrentPtr += 4;
                dCurrentFileSize -= 4;
            }
            else
            {
                CurrentPtr++;
                dCurrentFileSize--;
            }
        }
        else if(((unsigned char)*CurrentPtr & 0xf0) == 0xe0 )
        {
            ret = Kok_Is_Utf8_Char(CurrentPtr, 3);
            if(ret == 1)
            {
                memcpy(tmp,Kok_convertUTF8ToCharAscii(CurrentPtr,3),3);
                memcpy(dest+i,tmp,strlen(tmp)+1);
                i += strlen(tmp);
                //printf("\ntmp[%s]\n",tmp);
                CurrentPtr += 3;
                dCurrentFileSize -= 3;
            }
            else
            {
                CurrentPtr++;
                dCurrentFileSize--;
            }
        }
        else if(((unsigned char)*CurrentPtr & 0xe0) == 0xc0  )
        {
            ret = Kok_Is_Utf8_Char(CurrentPtr, 2);
            if(ret == 1)
            {
                memcpy(tmp,Kok_convertUTF8ToCharAscii(CurrentPtr,2),3);
                memcpy(dest+i,tmp,strlen(tmp)+1);
                i += strlen(tmp);
                //printf("\ntmp[%s]\n",tmp);
                CurrentPtr += 2;
                dCurrentFileSize -= 2;
            }
            else
            {
                CurrentPtr++;
                dCurrentFileSize--;
            }
        }
        else if(((unsigned char)*CurrentPtr < 0x80) && ((unsigned char)*CurrentPtr != 0x00) )
        {
            //if(!Kok_IsSpecialChar(CurrentPtr)){
            *(dest+i) = tolower(*CurrentPtr);//
            *(dest+i+1) = '\0';
            //}else i--;
            CurrentPtr++;
            dCurrentFileSize--;
            i++;
        }
        
    }
}
INT32  Kok_Is_Utf8_Char(char *Ptr, UINT32 NumByte)
{
    char *CurrentPtr;
    CurrentPtr = Ptr;
    int i = NumByte;
    int j = 0 ;
    while(--i)
    {
        if(((unsigned char)*(++CurrentPtr) &  0xc0 ) == 0x80)
        {
            j++;
        }
    }
    if((j+1) == NumByte)
        return 1;
    else
        return 0;
}
void Kok_CutString(SCHAR *src, UINT32 strMaxLen){
    if(strlen(src) <= strMaxLen) return;
    int i;
    for( i = strMaxLen - 3;i>0;i--) {
        if(src[i] == ' ' || src[i] == ',') {
            src[i] = '\0';
            strcat(src,"...");
            return;
        }
    }
    src[strMaxLen] = '\0';
}
INT32 LocTest = 0;
INT32 Kok_strlenUtf8(char *pDataPtr)
{
    if(LocTest) {
        //diag_printf("\n---Kok_strlenUtf8---\n");
    }
    char *CurrentPtr = pDataPtr;
    INT32 dCurrentFileSize = strlen(CurrentPtr);
    INT32 len = 0;
    if(LocTest) {
        //diag_printf("\n---Kok_strlenUtf8[%d]---\n",dCurrentFileSize);
    }
    while(dCurrentFileSize>0)//dCurrentFileSize--  &&
    {
        INT16 ret = 0;
        if(((unsigned char)*CurrentPtr & 0xf8) == 0xf0 )
        {
            ret = Kok_Is_Utf8_Char(CurrentPtr, 4);
            if(ret == 1)
            {
                CurrentPtr += 4;
                dCurrentFileSize -= 4;
            }
            else
            {
                dCurrentFileSize--;
                CurrentPtr++;
            }
        }
        else if(((unsigned char)*CurrentPtr & 0xf0) == 0xe0 )
        {
            ret = Kok_Is_Utf8_Char(CurrentPtr, 3);
            if(ret == 1)
            {
                CurrentPtr += 3;
                dCurrentFileSize -= 3;
            }
            else
            {
                dCurrentFileSize--;
                CurrentPtr++;
            }
        }
        else if(((unsigned char)*CurrentPtr & 0xe0) == 0xc0  )
        {
            ret = Kok_Is_Utf8_Char(CurrentPtr, 2);
            if(ret == 1)
            {
                CurrentPtr += 2;
                dCurrentFileSize -= 2;
            }
            else
            {
                dCurrentFileSize--;
                CurrentPtr++;
            }
        }
        else if(((unsigned char)*CurrentPtr < 0x80) && ((unsigned char)*CurrentPtr != 0x00) )
        {
            dCurrentFileSize--;
            CurrentPtr++;
        }
        len++;
    }
    
    return len;
}
INT32 Kok_getPosSort(char *src, INT32 size)
{
    char *utf8Char;
    utf8Char = (char *)malloc(size+1);
    memcpy(utf8Char,src,size);
    memcpy(utf8Char+size,"\0",1);
    char *pch = strstr(utf8sort,utf8Char);
    UINT32 tmp;
    if(pch) {
        tmp = strlen(pch);
    }else tmp = strlen(utf8sort) + 1;
    free(utf8Char);
    return tmp;
}
INT16 Kok_getNumByteUtf8Char(char *CurrentPtr)
{
    INT16 ret = 0;
    if(((unsigned char)*CurrentPtr & 0xf8) == 0xf0 )
    {
        ret = Kok_Is_Utf8_Char(CurrentPtr, 4);
        if(ret == 1)
        {
            return 4;
        }
        else
        {
            return 1;
        }
    }
    else if(((unsigned char)*CurrentPtr & 0xf0) == 0xe0 )
    {
        ret = Kok_Is_Utf8_Char(CurrentPtr, 3);
        if(ret == 1)
        {
            return 3;
        }
        else
        {
            return 1;
        }
    }
    else if(((unsigned char)*CurrentPtr & 0xe0) == 0xc0  )
    {
        ret = Kok_Is_Utf8_Char(CurrentPtr, 2);
        if(ret == 1)
        {
            return 2;
        }
        else
        {
            return 1;
        }
    }
    else// if(((unsigned char)*CurrentPtr < 0x80) && ((unsigned char)*CurrentPtr != 0x00) )
    {
        return 1;
    }
}

void Kok_LocTest(bool staus)//return <0 s1<s2, >0 s1>s2, =0 s1=s2
{
    diag_printf("\n-----Kok_LocTest-------\n");
    if(staus) {
        LocTest = 1;
    }else LocTest = 0;
}
int Kok_GetLenWord(char *s,int *lenWord)
{
    char * pch;
    char stmp[128];
    memset(stmp,0x0,128);
    memcpy(stmp,s,strlen(s)+1);
    pch=strchr(stmp,' ');
    int j = 0;
    if(!pch) {
        lenWord[j] = strlen(stmp);
        //printf("\n[%d]\n",lenWord[j]);
        j++;
    }else{
        while (pch)
        {
            int len2 = strlen(pch);
            lenWord[j] = strlen(stmp) - strlen(pch);
            memmove(stmp,pch+1,len2);
            //printf("\n[%d]\n",lenWord[j]);
            pch=strchr(stmp,' ');
            j++;
            if(!pch) {
                lenWord[j] = strlen(stmp);
                //printf("\n[%d]\n",lenWord[j]);
                j++;
            }
        }
    }
    //printf("\n NUM OF WORD: %d\n",j);
    return j;
}
void Kok_CpySkipSpecialChar(char *des,char *src)
{
    //diag_printf("\nKok_CpySkipSpecialChar 1\n");
    int i;
    int j = 0;
    int len = strlen(src);
    char *pch;
    for(i=0;i<len;i++) {
        pch = strchr(Specicalchar,src[i]);
        if(pch != NULL) {
            continue;
        }
        des[j] = src[i];
        j++;
    }
    des[j] = 0;
    //diag_printf("\nKok_CpySkipSpecialChar 2\n");
}

INT32 Kok_CmpUTF8Str(char *s1,char *s2)//return <0 s1<s2, >0 s1>s2, =0 s1=s2
{
    if(LocTest)printf("\n---Kok_CmpUTF8Str S1[%s]---\n",s1);
    if(LocTest)printf("\n---Kok_CmpUTF8Str S2[%s]---\n",s2);
    
    INT32 len1 = strlen(s1);
    INT32 len2 = strlen(s2);
    
    char mem1[128];
    char mem2[128];
    memset(mem1,0x0,128);
    memset(mem1,0x0,128);
    char *tmp1, *tmp2;
    tmp1 = mem1;//(char *)malloc(len1+1);
    tmp2 = mem2;//(char *)malloc(len2+1);
    
    //	strcpy(abc,s1);
    //	strcpy(abc1,s2);
    Kok_CpySkipSpecialChar(mem1,s1);
    Kok_UpperCase2LowerCaseUTF8(tmp1);
    
    Kok_CpySkipSpecialChar(mem2,s2);
    Kok_UpperCase2LowerCaseUTF8(tmp2);

    
    //printf("\n-%s--\n",tmp1);
    //printf("\n-%s--\n",tmp2);
    
    char ASCStr1[256];
    char ASCStr2[256];
    //tmp1 = s1;
    //ASCStr1 = (char *)malloc(len1);
    //tmp2 = s2;
    //ASCStr2 = (char *)malloc(len2);
    if(LocTest)diag_printf("\n---Kok_CmpUTF8Str [%d][%d]---\n",len1,len2);
//    ASCStr1 = (char *)malloc(len1);
//    memset(ASCStr1,0x0,len1);
//    ASCStr2 = (char *)malloc(len2);
//    memset(ASCStr2,0x0,len2);
    
    int NumWord1 = 0;
    int lenword1[12];
    memset(lenword1,0xFFFF,12);
    int lenwords1[12];
    memset(lenwords1,0xFFFF,12);
    
    int NumWord2 = 0;
    int lenword2[12];
    memset(lenword2,0xFFFF,12);
    int lenwords2[12];
    memset(lenwords2,0xFFFF,12);
    
    if(LocTest)diag_printf("\n---Kok_CmpUTF8Str 1---\n");
    Kok_convertToStringAscii(ASCStr1,tmp1);
    if(LocTest)diag_printf("\n---Kok_CmpUTF8Str[%d] 2---\n",strlen(ASCStr1));
    NumWord1 = Kok_GetLenWord(ASCStr1,lenword1);
    Kok_GetLenWord(tmp1,lenwords1);
    if(LocTest)diag_printf("\n---Kok_CmpUTF8Str 3---\n");
    Kok_convertToStringAscii(ASCStr2,tmp2);
    if(LocTest)diag_printf("\n---Kok_CmpUTF8Str 4---\n");
    NumWord2 = Kok_GetLenWord(ASCStr2,lenword2);
    Kok_GetLenWord(tmp2,lenwords2);
    if(LocTest)diag_printf("\n---Kok_CmpUTF8Str 5---\n");
    len1 = Kok_strlenUtf8(ASCStr1);
    if(LocTest)diag_printf("\n---Kok_CmpUTF8Str 6---\n");
    len2 = Kok_strlenUtf8(ASCStr2);
    if(LocTest)diag_printf("\n---Kok_CmpUTF8Str 7---\n");
    int lenCmp;
    if(NumWord1 > NumWord2)lenCmp = NumWord2;
    else lenCmp = NumWord1;
    INT32 dRet = 0;
    INT32 i = 0,j = 0;
    if(LocTest)diag_printf("\n---[%s]-\n",ASCStr1);
    if(LocTest)diag_printf("\n---[%s]-\n",ASCStr2);
    INT16 sizeChar1;
    INT16 sizeChar2;
    INT32 Pos1;
    INT32 Pos2;
    int sCMP = 0;
    int eCMP = 0;
    
    int sCMP2 = 0;
    int eCMP2 = 0;
    //cmp without signed
//    printf("cmp without signed\n");
    while(lenCmp > 0)//so am tiet can so sanh
    {
        sCMP = eCMP;
        if(lenword1[j] > lenword2[j])eCMP += lenword2[j];
        else eCMP += lenword1[j];
        if(LocTest)diag_printf("\n---Kok_CmpUTF8Str 8---\n");
        for(i=sCMP;i<eCMP;i++) {
            //so sanh ko dau cua am tiet
            if(LocTest)diag_printf("\n---Kok_CmpUTF8Str 9---\n");
            sizeChar1 = Kok_getNumByteUtf8Char(ASCStr1+i);
            if(LocTest)diag_printf("\n---Kok_CmpUTF8Str 10---\n");
            sizeChar2 = Kok_getNumByteUtf8Char(ASCStr2+i);
            if(LocTest)diag_printf("\n---Kok_CmpUTF8Str 11---\n");
            Pos1 = Kok_getPosSort(ASCStr1+i,sizeChar1);
            if(LocTest)diag_printf("\n---Kok_CmpUTF8Str 12---\n");
            Pos2 = Kok_getPosSort(ASCStr2+i,sizeChar2);
            if(LocTest)diag_printf("\n---Kok_CmpUTF8Str 13---\n");
            if(Pos1 != Pos2) {
                dRet = Pos2 - Pos1;
                //free(tmp1);
                //free(tmp2);
//                free(ASCStr1);
//                free(ASCStr2);
                return dRet;
            }
            i += sizeChar1-1;
        }
        dRet = lenword1[j] - lenword2[j];
        if(dRet != 0){
            //free(tmp1);
            //free(tmp2);
//            free(ASCStr1);
//            free(ASCStr2);
            return dRet;
        }
        eCMP++;
        if(LocTest)diag_printf("\n---Kok_CmpUTF8Str 14---\n");
        //so sanh co dau am tiet
        sCMP2 = eCMP2;
        if(lenwords1[j] > lenwords2[j])eCMP2 += lenwords2[j];
        else eCMP2 += lenwords1[j];
        for(i=sCMP2;i<eCMP2;i++) {
            //so sanh ko dau cua am tiet
            if(LocTest)diag_printf("\n---Kok_CmpUTF8Str 14---\n");
            sizeChar1 = Kok_getNumByteUtf8Char(tmp1);
            if(LocTest)diag_printf("\n---Kok_CmpUTF8Str 15---\n");
            sizeChar2 = Kok_getNumByteUtf8Char(tmp2);
            if(LocTest)diag_printf("\n---Kok_CmpUTF8Str 16---\n");
            
            Pos1 = Kok_getPosSort(tmp1,sizeChar1);
            if(LocTest)diag_printf("\n---Kok_CmpUTF8Str 17---\n");
            Pos2 = Kok_getPosSort(tmp2,sizeChar2);
            if(LocTest)diag_printf("\n---Kok_CmpUTF8Str 18---\n");
            if(Pos1 != Pos2) {
                dRet = Pos2 - Pos1;
                //free(tmp1);
                //free(tmp2);
//                free(ASCStr1);
//                free(ASCStr2);
                return dRet;
            }
            i += sizeChar1-1;
            tmp1 += sizeChar1;
            tmp2 += sizeChar2;
        }
        tmp1++;
        tmp2++;
        eCMP2++;
        
        j++;
        lenCmp--;
    }
    dRet = len1 - len2;
    if(LocTest && dRet > 426)diag_printf("\n----Kok_CmpUTF8Str [%d]----\n",dRet);
    //free(tmp1);
    //free(tmp2);
//    free(ASCStr1);
//    free(ASCStr2);
    return dRet;
}
