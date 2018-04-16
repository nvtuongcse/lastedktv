//
//  KarToc.cpp
//  SmartKaraoke
//
//  Created by BHDMinh on 6/11/14.
//  Copyright (c) 2014 SoncaMedia. All rights reserved.
//

#include "KarToc.h"
#include "utf8.h"
#include <string>
#include <stdio.h>

#include "debug_log.h"

#ifdef printf
#undef printf
#endif
//#define printf(...) __android_log_print(ANDROID_LOG_DEBUG, "KarToc", __VA_ARGS__);

#define printf(...) do{}while(0);

const uint8_t KarToc::disk_keys[DISK_KEY_LENGTH] = { 0x68, 0x40, 0xFA, 0x9F, 0x01, 0x7C, 0x2E, 0x66, 0x82, 0x99, 0x08, 0xDF, 0x4B, 0xB5, 0x2D, 0x60, 0x7B, 0x21, 0xAB, 0x88, 0x02, 0xC2, 0x18, 0xE9, 0x87, 0xDA, 0x28, 0xC9, 0x20, 0x33, 0xD2, 0xA5 };

KarToc::KarToc()
{
	lyr10WordInfo.clear();
	maxMegMidIdx = 0;
	maxMegvolIdx = 0;
    gpKokIndexHdr = NULL;
    pMem = NULL;
    iPasswordStreamPos = 0;
    gpKokLyricIndexHdr = NULL;
    pLyricMem = NULL;

    pArtistInfo = NULL;
    pSongInfo = NULL;
    pSongName = NULL;
    pSongPyName = NULL;
    pSingerName = NULL;
    pSingerPYName = NULL;
    pVideoMKVName = NULL;
    pVideoJPGName = NULL;
    pMapIndex = NULL;
}

KarToc::~KarToc()
{
	printf("gpKokIndexHdr");
	if(gpKokIndexHdr != NULL) {
		free(gpKokIndexHdr);
		gpKokIndexHdr = NULL;
	}

	printf("pMem");
    if (pMem != NULL) {
        free(pMem);
        pMem = NULL;
    }

	printf("gpKokLyricIndexHdr");
    if(gpKokLyricIndexHdr != NULL) {
    	free(gpKokLyricIndexHdr);
    	gpKokLyricIndexHdr = NULL;
    }

	printf("pLyricMem");
    if(pLyricMem != NULL)
    {
    	free(pLyricMem);
    	pLyricMem = NULL;
    }

	printf("lyr10WordInfo");
    for (int i = 0; i < lyr10WordInfo.size(); i++) {
    	printf("lyr10WordInfo: %d", i);
    	if(lyr10WordInfo.at(i).lyricText != NULL)
    		free(lyr10WordInfo.at(i).lyricText);
    }

	printf("pArtistInfo");
    if(pArtistInfo != NULL) {
    	free(pArtistInfo);
    	pArtistInfo = NULL;
    }
	printf("pSongInfo");
    if(pSongInfo != NULL) {
    	free(pSongInfo);
    	pSongInfo = NULL;
    }
	printf("pSongName");
    if(pSongName != NULL) {
    	free(pSongName);
    	pSongName = NULL;
    }
	printf("pSongPyName");
    if(pSongPyName != NULL) {
    	free(pSongPyName);
    	pSongPyName = NULL;
    }
	printf("pSingerName");
    if(pSingerName != NULL) {
    	free(pSingerName);
    	pSingerName = NULL;
    }
	printf("pSingerPYName");
    if(pSingerPYName != NULL) {
    	free(pSingerPYName);
    	pSingerPYName = NULL;
    }
	printf("pVideoMKVName");
    if(pVideoMKVName != NULL) {
    	free(pVideoMKVName);
    	pVideoMKVName = NULL;
    }
	printf("pVideoJPGName");
    if(pVideoJPGName != NULL) {
    	free(pVideoJPGName);
    	pVideoJPGName = NULL;
    }
	printf("pMapIndex");
    if(pMapIndex != NULL) {
    	free(pMapIndex);
    	pMapIndex = NULL;
    }
	printf("done destroy");
}

Kok_IdxFileHdr_S * KarToc::getHeader()
{
    return gpKokIndexHdr; 
}

uint16_t KarToc::getSongCount()
{
    return gpKokIndexHdr->uSongNums;
}

uint16_t KarToc::getSingerCount()
{
    return gpKokIndexHdr->uSingerNums;
}

char * KarToc::offsetToPointer(uint32_t offset)
{
    return (char*)(offset + (char*)pMem);
}

char * KarToc::offsetToPointerAbs(char *header, uint32_t offset)
{
    return (char*)(offset + (char*)header);
}

//char * KarToc::offsetToPointerLyricInfo(uint32_t offset)
//{
//    return (char*)(offset + (char*)gpKokLyricIndexHdr + gpKokLyricIndexHdr->pLyricInfoTable);
//}
//
//char * KarToc::offsetToPointerLyricPinyin(uint32_t offset)
//{
//    return (char*)(offset + (char*)gpKokLyricIndexHdr + gpKokLyricIndexHdr->pPinyinTable);
//}
//
//char * KarToc::offsetToPointerLyricText(uint32_t offset)
//{
//    return (char*)(offset + (char*)gpKokLyricIndexHdr + gpKokLyricIndexHdr->pLyricTable);
//}

char KarToc::parse_file_header(const char *pFileName)
{
	FILE *pFile;
	long totalRead = 0;
	int byteRead = 0;
	int status = 0;

	pFile = fopen(pFileName, "rb");
	if (pFile == NULL) {
		printf("Unable to open file update: %s\n", pFileName);
		if(gpKokIndexHdr == NULL) {
			gpKokIndexHdr = (Kok_IdxFileHdr_S*)malloc(sizeof(Kok_IdxFileHdr_S));
		}
		memset(gpKokIndexHdr, 0, sizeof(Kok_IdxFileHdr_S));
		gpKokIndexHdr->Magic.Index = ((uint32_t)'I' << 0) | ((uint32_t)'N' << 8)  | ((uint32_t)'D' << 16) | ((uint32_t)'X' << 24);
		return -2;
	}

	int tmpBufSize = sizeof(Kok_IdxFileHdr_S);
	char*tmpBuf = (char *)malloc(tmpBufSize);
	if (tmpBuf == NULL) {
			printf("Out of memory\n");
			fclose(pFile);
			return -1;
	}
	memset(tmpBuf, 0, tmpBufSize);

	do {
		byteRead = fread(tmpBuf, 1, tmpBufSize, pFile);
		totalRead += byteRead;
	}while(byteRead > 0 && totalRead < tmpBufSize);
	fclose(pFile);
	if(totalRead < tmpBufSize) {
		printf("Wrong file format");
		free(tmpBuf);
		return -1;
	}
	status = parse_data(tmpBuf, totalRead);
	free(tmpBuf);
	return status;
}

char KarToc::parse_file(const char *pFileName)
{
    FILE *pFile;
    long fileSize = 0;
    
    pFile = fopen(pFileName, "rb");
    if (pFile == NULL) {
        printf("Unable to open file update: %s\n", pFileName);
        if(gpKokIndexHdr == NULL) {
        	gpKokIndexHdr = (Kok_IdxFileHdr_S*)malloc(sizeof(Kok_IdxFileHdr_S));
        }
        memset(gpKokIndexHdr, 0, sizeof(Kok_IdxFileHdr_S));
		gpKokIndexHdr->Magic.Index = ((uint32_t)'I' << 0) | ((uint32_t)'N' << 8)  | ((uint32_t)'D' << 16) | ((uint32_t)'X' << 24);
        return -2;
    }
    
    fseek(pFile, 0, SEEK_END); // seek to end of file
    fileSize = ftell(pFile); // get current file pointer
    fseek(pFile, 0, SEEK_SET); // seek back to beginning of file
    
    pMem = (char *)malloc(fileSize);
    if(pMem == NULL)
    {
        printf("Unable to alloc memory\n");
        fclose(pFile);
    }
    
    fread(pMem, 1, fileSize, pFile);
    fclose(pFile);

    iPasswordStreamPos = 0;
    PasswordMaskBytesIdx((uint8_t *)pMem, 0, (uint32_t)fileSize, (char *)disk_keys, false);
    
//        const char *filePath = "/Users/bhdminh/Library/Developer/CoreSimulator/Devices/FFAA3819-AF87-4B04-80E8-71AE13CB7DBD/data/Containers/Data/Application/E57FB0FE-E08D-4A03-BAA1-FD9E666CB8B7/Documents/test";
//        FILE *pFileOut = fopen(filePath, "wb");
//        fwrite(pMem, 1, fileSize, pFileOut);
//        fclose(pFileOut);
    
    if(fileSize < sizeof(Kok_IdxFileHdr_S))
    {
        printf("Invalid data\n");
        return -1;
    }
    
    if((pMem[0] != 'I') || (pMem[1] != 'N') || (pMem[2] != 'D') || (pMem[3] != 'X'))
    {
        printf("Invalid header\n");
        return -1;
    }
    
//    gpKokIndexHdr = NULL;
//    gpKokIndexHdr = (Kok_IdxFileHdr_S*)(pMem);
    
    if(gpKokIndexHdr == NULL) {
    	gpKokIndexHdr = (Kok_IdxFileHdr_S*)malloc(sizeof(Kok_IdxFileHdr_S));
    }
    memcpy(gpKokIndexHdr, pMem, sizeof(Kok_IdxFileHdr_S));

    printf("Read Index file OK.\n");
    printf("dTotalBytes = %d.\n",fileSize);
    
#if 0
    KOK_INDEXFILE_HEADER(gpKokIndexHdr);
    uint32_t i;
    for(i = 0;i < GetAllSongNums(); i++)
    {
        KOK_SONG_INFO(getSongInfo(i),i);
    }
    printf("/********************************************/\n");
    printf("/****************SINGER INFO****************/\n");
    printf("/********************************************/\n");
    for(i = 0;i < GetAllSingerNums(); i++)
    {
        KOK_SINGER_INFO(getSingerInfo(i),i);
    }
#endif
    return 0;
}

char KarToc::parse_data(const char *pData, uint32_t len)
{
    pMem = (char *)malloc(len);
    if(pMem == NULL)
    {
        printf("Unable to alloc memory\n");
        return -1;
    }
    memcpy(pMem, pData, len);
    
	iPasswordStreamPos = 0;
    PasswordMaskBytesIdx((uint8_t *)pMem, 0, len, (char *)disk_keys, false);
    
//    const char *filePath = "/Users/bhdminh/Library/Application Support/iPhone Simulator/7.0.3/Applications/28770E53-1850-4DEE-8A53-233C6DE2304A/Documents/megidx.dec";
//    FILE *pFile = fopen(filePath, "wb");
//    fwrite(pMem, 1, len, pFile);
//    fclose(pFile);
    
    if(len < sizeof(Kok_IdxFileHdr_S))
    {
        printf("Invalid data\n");
        return -1;
    }
    
    if((pMem[0] != 'I') || (pMem[1] != 'N') || (pMem[2] != 'D') || (pMem[3] != 'X'))
    {
        printf("Invalid header\n");
        return -1;
    }
    
//    gpKokIndexHdr = NULL;
//    gpKokIndexHdr = (Kok_IdxFileHdr_S*)(pMem);
    if(gpKokIndexHdr == NULL) {
    	gpKokIndexHdr = (Kok_IdxFileHdr_S*)malloc(sizeof(Kok_IdxFileHdr_S));
    }
    memcpy(gpKokIndexHdr, pMem, sizeof(Kok_IdxFileHdr_S));
    
    printf("Read Index file OK.\n");
    printf("dTotalBytes = %d.\n",len);

#if 0
    KOK_INDEXFILE_HEADER(gpKokIndexHdr);
    uint32_t i;
    for(i = 0; i < GetAllSongNums(); i++)
    {
        KOK_SONG_INFO(getSongInfo(i),i);
    }
    printf("/********************************************/\n");
    printf("/****************SINGER INFO****************/\n");
    printf("/********************************************/\n");
    for(i = 0;i < GetAllSingerNums(); i++)
    {
        KOK_SINGER_INFO(getSingerInfo(i),i);
    }
#endif
    return 0;
}

void KarToc::getMaxMegFileIdx()
{
	maxMegvolIdx = 0;
	maxMegMidIdx = 0;
	for(int i = 0; i < gpKokIndexHdr->uSongNums; i++) {
		Song_Info_S *songInfo = getSongInfo(i);
		if(songInfo->property.media_type == MP3 || songInfo->property.media_type == SINGER) {
			maxMegvolIdx = songInfo->Mp3Info.stFilepos.fileIdx > maxMegvolIdx ? songInfo->Mp3Info.stFilepos.fileIdx : maxMegvolIdx;
		}

		if(songInfo->property.media_type != VIDEO) {
			maxMegMidIdx = songInfo->MidiInfo.stFilepos.fileIdx > maxMegMidIdx ? songInfo->MidiInfo.stFilepos.fileIdx : maxMegMidIdx;
		}
	}
}

uint32_t KarToc::getVersion()
{
    return (uint32_t)(gpKokIndexHdr->Magic.Version) * 100 + gpKokIndexHdr->Magic.Revision;
}

uint32_t KarToc::getVersion1()
{
    return (uint32_t)(gpKokIndexHdr->Magic.Version1) * 100 + gpKokIndexHdr->Magic.Revision1;
}

uint32_t KarToc::getVersion2()
{
    return (uint32_t)(gpKokIndexHdr->Magic.Version2) * 100 + gpKokIndexHdr->Magic.Revision2;
}

uint32_t KarToc::getVersion3()
{
    return (uint32_t)(gpKokIndexHdr->Magic.Version3) * 100 + gpKokIndexHdr->Magic.Revision3;
}

bool KarToc::isNewFormat()
{
    if (gpKokIndexHdr->uSongNums > 0) {
        Song_Info_Extra_S *extra = getSongInfoExtra(0);
        return (extra->bNewTocFormat == 1); 
    }
    return false;
}

int KarToc::indexOfSongIdLyric(uint32_t id, uint8_t type)
{
    int low = 0;
    int high = (int)(lyr10WordInfo.size() - 1);
    while (low <= high) {
        int mid = low + (high - low) / 2;
        if (lyr10WordInfo.at(mid).songID.index5 > id) {
            high = mid - 1;
        }else {
                low = mid + 1;
            }
        }
    return low;
}

char* KarToc::getLyric10Word(uint32_t id, uint8_t type)
{
    int low = 0;
    int high = (int)lyr10WordInfo.size() - 1;
    while (low <= high) {
        int mid = low + (high - low) / 2;
        if (lyr10WordInfo.at(mid).songID.index5 > id) {
            high = mid - 1;
        }else if(lyr10WordInfo.at(mid).songID.index5 < id){
            low = mid + 1;
        }else {
            if (lyr10WordInfo.at(mid).songID.typeABC > type) {
                high = mid - 1;
            }else if(lyr10WordInfo.at(mid).songID.typeABC < type) {
                low = mid + 1;
            }else {
                return lyr10WordInfo.at(mid).lyricText;
            }
        }
    }
    return NULL;
}

char KarToc::parseLyric10Words(const char *pFilePath)
{
	printf("Parse Lyric 10word start\n");

	FILE *pLyricFile = fopen(pFilePath, "rb");
	if(pLyricFile == NULL)
	{
		printf("Invalid File Lyric\n");
		return -1;
	}

	fseek(pLyricFile, 0, SEEK_END);
	uint32_t len = (uint32_t)ftell(pLyricFile);
	fseek(pLyricFile, 0, SEEK_SET);
	char* lyr10WordPtr = (char *)malloc(len + 1);

	if(lyr10WordPtr == NULL)
	{
		printf("Unable to malloc lyric memory\n");
		return -1;
	}
	memset(lyr10WordPtr, 0, len);

	const int BUF_SIZE = 1024;
	uint8_t *buff = (uint8_t *)malloc(BUF_SIZE);
	if(buff == NULL)
	{
		printf("Out of memory\n");
		return -1;
	}
	uint32_t byteRead = -1;
	char *pLyric = lyr10WordPtr;
	while ((byteRead = (uint32_t)fread(buff, 1, BUF_SIZE, pLyricFile)) > 0) {
		memcpy(pLyric, buff, byteRead);
		pLyric += byteRead;
	}
	*pLyric = 0x00;

	fclose(pLyricFile);
	free(buff);

	for (int i = 0; i < lyr10WordInfo.size(); i++) {
		if(lyr10WordInfo.at(i).lyricText != NULL)
			free(lyr10WordInfo.at(i).lyricText);
	}
	lyr10WordInfo.clear();

	uint32_t pos = 0;
	std::vector<Lyric_10Word_Info_S>::iterator it = lyr10WordInfo.begin();
	while (pos < len) {
		//read header
		if(lyr10WordPtr[pos++] != 0x01)
		{
			printf("Invalid header chunk");
			break;
		}

		Lyric_10Word_Info_S lyrInfo;
		uint8_t len = lyr10WordPtr[pos++];
		lyrInfo.songID.typeABC = lyr10WordPtr[pos++];
		uint32_t songID = 0;
		songID |= ((uint32_t)lyr10WordPtr[pos++] << 16) & 0xFF0000;
		songID |= ((uint32_t)lyr10WordPtr[pos++] << 8) & 0x00FF00;
		songID |= ((uint32_t)lyr10WordPtr[pos++] << 0) & 0x0000FF;
		lyrInfo.songID.index5 = songID;

		len -= (1+3);
		lyrInfo.lyricText = (char *)malloc(len+1);
		memset(lyrInfo.lyricText, 0, len+1);
		memccpy(lyrInfo.lyricText, &lyr10WordPtr[pos], 1, len);
		pos+=len;

		//        lyr10WordInfo.push_back(lyrInfo);
		int index = indexOfSongIdLyric(lyrInfo.songID.index5, lyrInfo.songID.typeABC);

		it = lyr10WordInfo.begin() + index;
		lyr10WordInfo.insert(it, lyrInfo);
	}

	printf("Info count: %d\n", lyr10WordInfo.size());
	printf("Lyric file parse completed\n");
	return 0;
}

char KarToc::parseLyricToc(const char *pFilePath)
{
    printf("Parse Lyric start\n");
    
    FILE *pLyricFile = fopen(pFilePath, "rb");
    if(pLyricFile == NULL)
    {
        printf("Invalid File Lyric\n");
        return -1;
    }
    
    fseek(pLyricFile, 0, SEEK_END);
    uint32_t len = (uint32_t)ftell(pLyricFile);
    fseek(pLyricFile, 0, SEEK_SET);
    
    if(len < sizeof(Kok_LyrcFileHdr_S))
    {
        printf("Invalid data\n");
        return -1;
    }
    
    pLyricMem = (char *)malloc(len + 1);
    
    if(pLyricMem == NULL)
    {
        printf("Unable to malloc lyric memory\n");
        return -1;
    }
    memset(pLyricMem, 0, len);
    
    const int BUF_SIZE = 1024;
    uint8_t *buff = (uint8_t *)malloc(BUF_SIZE);
    if(buff == NULL)
    {
        printf("Out of memory\n");
        return -1;
    }
    uint32_t byteRead = -1;
    char *pLyric = pLyricMem;
    while ((byteRead = (uint32_t)fread(buff, 1, BUF_SIZE, pLyricFile)) > 0) {
        memcpy(pLyric, buff, byteRead);
        pLyric += byteRead;
    }
    *pLyric = 0x00;
    
    fclose(pLyricFile);
    free(buff);
    
    if((pLyricMem[0] != 'F') || (pLyricMem[1] != '1') || (pLyricMem[2] != '0') || (pLyricMem[3] != 'W'))
    {
        printf("Invalid Lyric header\n");
        return -1;
    }
    gpKokLyricIndexHdr = NULL;
    gpKokLyricIndexHdr = (Kok_LyrcFileHdr_S *) pLyricMem;
    
//    pSongCode5 = (uint32_t *)((char *)kokLyricHeader + kokLyricHeader->pLyricInfoTable);
//    pLyricPinyin = (char *)((char *)kokLyricHeader + kokLyricHeader->pPinyinTable);
//    pLyricText = (char *)((char *)kokLyricHeader + kokLyricHeader->pLyricTable);
    
    printf("Lyric file parse completed\n");

#if 0
    uint32_t i;
    for(i = 0;i < getLyricInfoCount(); i++)
    {
        KOK_LYRIC_INFO(getLyricInfo(i),i);
    }
#endif
    
    return 0;
}

#ifdef TOC_VER_1
uint16_t KarToc::getLyricInfoCount()
{
    return gpKokLyricIndexHdr->uLyricInfoCount;
}

char * KarToc::getLyricTextTable()
{
	return (char *)((char *)gpKokLyricIndexHdr + gpKokLyricIndexHdr->pLyricTable);
}

char * KarToc::getLyricPinyinTable()
{
	return (char *)((char *)gpKokLyricIndexHdr + gpKokLyricIndexHdr->pPinyinTable);
}

#ifdef TOC_VER_2
char* KarToc::getLyric10WordsTable()
{
	return (char *)((char *) gpKokLyricIndexHdr + gpKokLyricIndexHdr->pFirst10WordLyricTable);
}

LyricWord_Info_S* KarToc::getFirstWordsLyric(uint32_t uSongIndex)
{
	LyricWord_Info_S *pLyricWords = NULL;
	    if (gpKokLyricIndexHdr) {
	    	pLyricWords = (LyricWord_Info_S *)(offsetToPointerAbs((char *)gpKokLyricIndexHdr, gpKokLyricIndexHdr->pLyric10WordInfoTable));
	        if(uSongIndex < GetAllSongNums())
	        {
	        	pLyricWords += uSongIndex;
	        }
	    }
	    return pLyricWords;
}
#endif

Lyric_Info_S* KarToc::getLyricInfo(uint32_t uLyricIndex)
{
    Lyric_Info_S *pLyricInfo = NULL;
    if (gpKokLyricIndexHdr) {
        pLyricInfo = (Lyric_Info_S *)(offsetToPointerAbs((char *)gpKokLyricIndexHdr, gpKokLyricIndexHdr->pLyricInfoTable));
        if(uLyricIndex < getLyricInfoCount())
        {
            pLyricInfo += uLyricIndex;
        }
    }
    return pLyricInfo;
}

Person_Info_S* KarToc::getAuthorInfo(UINT32 uSingerIndex)
{
	Person_Info_S* pSingerInfo = NULL;
	UINT32 uiSingerTableSz = 0;
	UINT32 uiSingerCounts = 0;
	if(gpKokIndexHdr)
	{
		pSingerInfo = (Person_Info_S*)(offsetToPointer(gpKokIndexHdr->pSingerInfoTable));
		uiSingerTableSz = gpKokIndexHdr->uSingerInfoTableSize;
		uiSingerCounts  = gpKokIndexHdr->uSingerNums;
        
		if(uSingerIndex < uiSingerCounts)
		{
			pSingerInfo += uSingerIndex;
		}
	}
	return pSingerInfo;
}

Person_Info_S* KarToc::getSingerInfo(UINT32 uSingerIndex)
{
	Person_Info_S* pSingerInfo = NULL;
	UINT32 uiSingerTableSz = 0;
	UINT32 uiSingerCounts = 0;
	if(gpKokIndexHdr)
	{
		pSingerInfo = (Person_Info_S*)(offsetToPointer(gpKokIndexHdr->pSingerInfoTable));
		uiSingerTableSz = gpKokIndexHdr->uSingerInfoTableSize;
		uiSingerCounts  = gpKokIndexHdr->uSingerNums;
        
		if(uSingerIndex < uiSingerCounts)
		{
			pSingerInfo += uSingerIndex;
		}
	}
	return pSingerInfo;
}
#else
Singer_Info_S* KarToc::getSingerInfo(UINT32 uSingerIndex)
{
	Singer_Info_S* pSingerInfo = NULL;
	UINT32 uiSingerTableSz = 0;
	UINT32 uiSingerCounts = 0;
	if(gpKokIndexHdr)
	{
		pSingerInfo = (Singer_Info_S*)(Offset2Pointer(gpKokIndexHdr->pSingerInfoTable));
		uiSingerTableSz = gpKokIndexHdr->uSingerInfoTableSize;
		uiSingerCounts  = gpKokIndexHdr->uSingerNums;
        
		if(uSingerIndex < uiSingerCounts)
		{
			pSingerInfo += uSingerIndex;
		}
	}
	return pSingerInfo;
}
#endif

Song_Info_Extra_S* KarToc::getSongInfoExtra(UINT32 idx)
{
    Song_Info_Extra_S *songInfoExtra = NULL;
    Song_Info_S *songInfo = getSongInfo(idx);
    if (songInfo!= NULL) {
        uint8_t* pSongClip = (uint8_t *)offsetToPointer(songInfo->pSongClips);
        uint8_t numClips = *(pSongClip);
        
        // format for song clip: <len:1 byte>{<clipidx: 2bytes><...>}<extra song info: 1 byte>
        songInfoExtra = (Song_Info_Extra_S *)(pSongClip + 2*numClips + 1);
    }
    return songInfoExtra;
}

Song_Info_Extra1_S* KarToc::getSongInfoExtra1(UINT32 idx)
{
    Song_Info_Extra1_S *songInfoExtra = NULL;
    Song_Info_S *songInfo = getSongInfo(idx);
    if (songInfo!= NULL) {
        uint8_t* pSongClip = (uint8_t *)offsetToPointer(songInfo->pSongClips);
        uint8_t numClips = *(pSongClip);
        
        uint8_t *pExtra1 = (uint8_t *)(pSongClip + 2*numClips + 2);
        if((*pExtra1 & 0x80) != 0) {
            // format for song clip: <len:1 byte>{<clipidx: 2bytes><...>}<extra song info: 2 byte>
            songInfoExtra = (Song_Info_Extra1_S *)pExtra1;
        }
    }
    return songInfoExtra;
}

Song_Info_S* KarToc::getSongInfo(UINT32 uSongIndex)
{
	Song_Info_S* pSongInfo = NULL;
	UINT32 dSongTableSz = 0;
	UINT32 dSongCounts = 0;
	// Phuc add
	uSongIndex = uSongIndex & KOK_ITEMS_MAX;
	// End
	if(gpKokIndexHdr)
	{
		pSongInfo = (Song_Info_S*)(offsetToPointer(gpKokIndexHdr->pSongInfoTable));
		dSongTableSz = gpKokIndexHdr->uSongInfoTableSize;
		dSongCounts  = gpKokIndexHdr->uSongNums;
        
		if(uSongIndex < dSongCounts)
		{
			pSongInfo += uSongIndex;
		}
		else
		{
			pSongInfo = NULL;
		}
	}
	return pSongInfo;
}

BYTE PasswordByteEncryptDecrypt(BYTE value, int step, int nPwdSz, bool isEncrypted)
{
    
	step = step % 8;
	switch (step)
	{
		case 0: //00.11.0000
			value = (BYTE)(((~value) & 0xC0) | (value & 0x30) | ((~value) & 0x0F));
			break;
		case 1: //00.1.00.111
			value = (BYTE)(((~value) & 0xC0) | (value & 0x20) | ((~value) & 0x18) | (value & 0x07));
			break;
		case 2: // nPwdSz
			value = (BYTE)(value + ((isEncrypted == true) ? (+nPwdSz) : (-nPwdSz)));
			break;
		case 3: //1.00.111.00
			value = (BYTE)(((value) & 0x80) | ((~value) & 0x60) | ((value) & 0x1C) | (value & 0x03));
			break;
		case 4: //0000.1111
			value = (BYTE)(((~value) & 0xF0) | ((value) & 0x0F));
			break;
		case 5: //0.1111.000
			value = (BYTE)(((~value) & 0x80) | ((value) & 0x78) | ((~value) & 0x07));
			break;
		case 6: //000000.11
			value = (BYTE)(((~value) & 0xFC) | ((~value) & 0x03));
			break;
		case 7: //nPwdSz
			value = (BYTE)(value + ((isEncrypted == true) ? (-nPwdSz) : (+nPwdSz)));
			break;
	}
	return value;
}

void KarToc::PasswordMaskBytes(BYTE *nBytesSrc, int offset, int sz, char* strPwd)
{
	printf("PasswordMaskBytes: %s\n", strPwd);
	//return;
    
	int i = 0;
	BYTE pwdLen = strlen(strPwd)+32;
	BYTE nBytesPwd[pwdLen];
	for(i = 0; i < strlen(strPwd); i++)
	{
		nBytesPwd[i] = (BYTE)(~strPwd[i]);
	}
	for(; i < pwdLen; i++)
	{
		nBytesPwd[i] = (BYTE)(disk_keys[i-strlen(strPwd)]);
	}
    int Length = (offset + sz);
	for (i = offset; i < Length; i++)
	{
		//Step 1
		nBytesSrc[i] = PasswordByteEncryptDecrypt(nBytesSrc[i], (i + iPasswordStreamPos) % pwdLen, pwdLen, false);
        
		//Step 2
		nBytesSrc[i] = (BYTE)(nBytesSrc[i] ^ nBytesPwd[(i + iPasswordStreamPos) % pwdLen]);
	}
	iPasswordStreamPos += Length;
}

//void KarToc::PasswordMaskBytesMp3(BYTE *nBytesSrc, int offset, int sz, char* strPwd)
//{
//	printf("PasswordMaskBytes Mp3: %s\n", strPwd);
//
//	int i = 0;
//	BYTE pwdLen = strlen(strPwd)+32;
//	BYTE nBytesPwd[pwdLen];
//	for(i = 0; i < strlen(strPwd); i++)
//	{
//		nBytesPwd[i] = (BYTE)(~strPwd[i]);
//	}
//	for(; i < pwdLen; i++)
//	{
//		nBytesPwd[i] = (BYTE)(disk_keys[i-strlen(strPwd)]);
//	}
//	int nSectors = sz / 2048;
//	int n;
//	for (n = 0; n < nSectors; n++)
//	{
//        offset = n * 2048;
//	     BYTE block = 64;
//
//	     int Length = (offset + block);
//	    for (i = offset; i < Length; i++)
//	    {
//		    //Step 1
//		    nBytesSrc[i] = PasswordByteEncryptDecrypt(nBytesSrc[i], i % pwdLen, pwdLen, false);
//
//		    //Step 2
//		    nBytesSrc[i] = (BYTE)(nBytesSrc[i] ^ nBytesPwd[i % pwdLen]);
//	    }
//	}
//}

void KarToc::PasswordMaskBytesMp3(BYTE *nBytesSrc, int offset, int sz, char* strPwd)
{
	printf("PasswordMaskBytes Mp3: %s\n", strPwd);

	int pos = 0;
	int i = 0;
	BYTE pwdLen = strlen(strPwd)+32;
	BYTE nBytesPwd[pwdLen];
	for(i = 0; i < strlen(strPwd); i++)
	{
		nBytesPwd[i] = (BYTE)(~strPwd[i]);
	}
	for(; i < pwdLen; i++)
	{
		nBytesPwd[i] = (BYTE)(disk_keys[i-strlen(strPwd)]);
	}
	int nSectors = sz / 2048;
	int n;
	for (n = 0; n < nSectors; n++)
	{
//        offset = n * 2048;
		pos = n*2048;
	     BYTE block = 64;

	     int Length = (pos + block);
	    for (i = pos; i < Length; i++)
	    {
		    //Step 1
		    nBytesSrc[i] = PasswordByteEncryptDecrypt(nBytesSrc[i], (i + offset) % pwdLen, pwdLen, false);

		    //Step 2
		    nBytesSrc[i] = (BYTE)(nBytesSrc[i] ^ nBytesPwd[(i + offset) % pwdLen]);
	    }
	}
}

void KarToc::PasswordMaskBytesUser(BYTE *nBytesSrc, int offset, int sz)
{
	printf("PasswordMaskBytesUser\n");
	int i;
	int Length = (offset + sz);
	for (i = offset; i < Length; i++)
	{
		nBytesSrc[i] = (BYTE)(nBytesSrc[i] ^ 0x78);
		nBytesSrc[i] = (BYTE)(nBytesSrc[i] - 14);
	}
}


//void KarToc::PasswordMaskBytesIdx(BYTE *nBytesSrc, int offset, int sz, char* Key)
//{
//	printf("PasswordMaskBytes: %s\n", Key);
//	//return;
//    
//	int i = 0;
//	BYTE pwdLen = 32;
//	BYTE nBytesPwd[pwdLen];
//    int Length = (offset + sz);
//	for(i = 0; i < pwdLen; i++)
//	{
//		nBytesPwd[i] = (BYTE)(Key[i]);
//	}
//	for (i = offset; i < Length; i++)
//	{
//		//Step 1
//		nBytesSrc[i] = PasswordByteEncryptDecrypt(nBytesSrc[i], (i + iPasswordStreamPos) % pwdLen, pwdLen, false);
//        
//		//Step 2
//		nBytesSrc[i] = (BYTE)(nBytesSrc[i] ^ nBytesPwd[(i + iPasswordStreamPos) % pwdLen]);
//	}
//	iPasswordStreamPos += Length;
//}

void KarToc::PasswordMaskBytesIdx(BYTE *nBytesSrc, UINT32 offset, UINT32 sz, char* Key, bool isEncrypted)
{
    printf("PasswordMaskBytes: %d\n", isEncrypted);
    //return;
    
    UINT32 i = 0;
    BYTE pwdLen = 32;
    BYTE nBytesPwd[pwdLen];
    UINT32 Length = (offset + sz);
    
    for(i = 0; i < pwdLen; i++)
    {
        nBytesPwd[i] = (BYTE)(Key[i]);
    }
    if(isEncrypted) {
        for (i = offset; i < Length; i++)
        {
            //Step 1
            nBytesSrc[i] = (BYTE)(nBytesSrc[i] ^ nBytesPwd[i % pwdLen]);
            
            //Step 2
            nBytesSrc[i] = (BYTE)PasswordByteEncryptDecrypt(nBytesSrc[i], i % pwdLen, pwdLen, true);
        }
    }else{
        for (i = offset; i < Length; i++)
        {
            //Step 1
            nBytesSrc[i] = PasswordByteEncryptDecrypt(nBytesSrc[i], (i + iPasswordStreamPos) % pwdLen, pwdLen, false);
            
            //Step 2
            nBytesSrc[i] = (BYTE)(nBytesSrc[i] ^ nBytesPwd[(i + iPasswordStreamPos) % pwdLen]);
        }
        iPasswordStreamPos += Length;
    }
}

BYTE KarToc::DetectMidiMp3Format(BYTE *pData)
{
	BYTE Type = DEFAULT_MIDIMP3;

	if(memcmp(pData, "ESCA", 4) == 0)
	{
		Type = DECRYPT_MIDIMP3_SONCA;
	}
	else if(memcmp(pData, "EUSR", 4) == 0)
	{
		Type = DECRYPT_MIDIMP3_USER;
	}
	return Type;
}
