//
//  CloudKaraoke.m
//  CloudKaraoke-Dev
//
//  Created by BHDMinh on 2/17/14.
//  Copyright (c) 2014 soncamedia. All rights reserved.
//

#include <fcntl.h>
#include <unistd.h>

#include "SmartKaraoke.h"
#include <sys/stat.h>
#include <stdint.h>
#include <string>
#include "decrypt.h"
#include "server_url.h"
//#include "xmlparse.h"
#include  "sqlite3.h"
#include "libsqlite.h"
#include "StringUtils.h"
#include "TocMerge.h"

#include "lib/utf8/utf8.h"

#include "android_database_SQLiteCommon.h"

#include "zlib.h"
#include "zconf.h"

#ifdef READ_ICBM
#include "atsha204.h"
#endif

using namespace android;

static CDecryptLyric decryptor;
static CServerUrl serverUrl;
sqlite3 *_db;

#ifdef ENVIROMENT_IOS
@implementation CloudKaraoke
#pragma mark - CloudKaraoke Lyric Decrypt

+ (void) getWordSize:(NSInteger) index
{
	return decryptor.getWordSize(index);
}

+ (NSInteger) getIntValue:(NSInteger) value
		{
	return decryptor.getInt32(value);
		}

+ (NSString *) getStringValue:(NSString *) str offset:(NSInteger) offset
		{
	char *strPtr = decryptor.getStringValue([str UTF8String], offset);
	NSString *outStr = [[NSString alloc] initWithUTF8String:strPtr];
	free(strPtr);
	return outStr;
		}

#pragma mark - Server URL Build
+ (NSString *) buildServerLoginUrl
		{
	char *strPtr = serverUrl.buildServerLoginUrl();
	NSString *outStr = [NSString stringWithUTF8String:strPtr];
	free(strPtr);
	return outStr;
		}

+ (NSString *) buildRequestUpdateString:(NSString *)transId task:(NSInteger) task
		{
	char *strPtr = serverUrl.buildRequestUpdateString([transId UTF8String], task);
	NSString *outStr = [NSString stringWithUTF8String:strPtr];
	free(strPtr);
	return outStr;
		}

+ (NSString *) buildRequestChargeInfoString:(NSString *)transId
		{
	char *strPtr = serverUrl.buildRequestChargeInfoString([transId UTF8String]);
	NSString *outStr = [NSString stringWithUTF8String:strPtr];
	free(strPtr);
	return outStr;
		}

+ (NSString *) buildRequestString:(NSString *)transId task:(NSInteger)task index:(NSInteger) index
		{
	char *strPtr = serverUrl.buildRequestString([transId UTF8String], task, index);
	NSString *outStr = [NSString stringWithUTF8String:strPtr];
	free(strPtr);
	return outStr;
		}

+ (NSString *) buildRequestString__:(NSString *)transId task:(NSInteger)task index:(NSInteger) index
		{
	char *strPtr = serverUrl.buildRequestString__([transId UTF8String], task, index);
	NSString *outStr = [NSString stringWithUTF8String:strPtr];
	free(strPtr);
	return outStr;
		}
@end
#endif

#ifdef ENVIROMENT_ANDROID
#include <jni.h>

#include "KarToc.h"

#if defined(CLOUD_DEBUG) && (CLOUD_DEBUG == 1)
#include "debug_log.h"

#ifdef printf
#undef printf
#endif
#define printf(...) __android_log_print(ANDROID_LOG_DEBUG, "SmartKaraoke", __VA_ARGS__);

#else
#define LOGV(TAG,...) do{}while(0);
#define LOGD(TAG,...) do{}while(0);
#define LOGI(TAG,...) do{}while(0);
#define LOGW(TAG,...) do{}while(0);
#define LOGE(TAG,...) do{}while(0);
#define printf(...) do{}while(0);
#endif

int deleteSongFromDatabase(const char *cDbPath, Song_Info_Update_S *songInfoUpdate, int songCount);
int insertNewSongToDatabase(const char *cDbPath, Song_Info_Update_S *songInfoUpdate, int songCount, Person_Info_Update_S *artistInfo, int artistCount);

jbyteArray Java_vn_Com_sonca_smartkaraoke_SmartKaraoke_getStringData(JNIEnv *env, jobject thiz, jint idx) {
	int status = 0;
	int index = 0;
	jbyteArray byteArray = NULL;

	uint8_t *disk_key_read = (uint8_t *)malloc(DISK_KEY_LENGTH);
	memset(disk_key_read, 0, DISK_KEY_LENGTH);

#ifdef READ_ICBM
	int retry = 0;
	int keyStatus = 0;
	printf("read key");
	do {
		keyStatus = get_key(disk_key_read, &index);
		if(keyStatus == DISK_KEY_LENGTH) {
			break;
		}
		printf("Failed to get authen key: %d, retry: %d", keyStatus, retry);
		retry++;
		usleep((int)(500000.0));
	} while(retry < 10);
	if(keyStatus != DISK_KEY_LENGTH) {
		free(disk_key_read);
		return NULL;
	}
#endif

	byteArray = env->NewByteArray(DISK_KEY_LENGTH);
	env->SetByteArrayRegion(byteArray, 0, DISK_KEY_LENGTH, (jbyte *)disk_key_read);
	free(disk_key_read);
	return byteArray;
}

int make_dirs(const char *path)
{
	int retval;
	char subpath[FILENAME_MAX];
	char * pch = NULL;
	int mode = 0777;

	memset(subpath, 0, FILENAME_MAX);
	if (mkdir(path, mode) < 0) {
		if (path[0] == '/')		/* Skip leading '/'. */
			pch=strchr(path + 1,'/');
		else
			pch=strchr(path,'/');
		while (pch!=NULL)
		{
			memcpy(subpath, path, pch - path);
			mkdir(subpath, mode);

			pch=strchr(pch+1,'/');
		}
		mkdir(path, mode);
	}
	return retval;
}

#define MAX_CLIPS_PER_SONG	4
typedef struct __spl_song {
	uint32_t song_code6;
	struct {
		struct {
			unsigned songABCType : 4; // ABC song type: A, B, C
			unsigned fileIdx   : 4; //so file MEGMID
			unsigned fileOffset: 24; //mp3 offset or video offset of table
		}stFilepos;
		uint32_t  filesize;
	}MidiInfo;
	struct {
		struct {
			unsigned fileIdx   : 8; //so file MEGVOL
			unsigned fileOffset: 24; //mp3 offset or video offset of table
		}stFilepos;
		uint32_t  filesize;
	}Mp3Info;
	uint16_t songClips[MAX_CLIPS_PER_SONG];
	struct {
		unsigned is2ndStream	: 1;
		unsigned reserved		: 7;
	}Extra;
} spl_song;


void Java_vn_Com_sonca_smartkaraoke_SmartKaraoke_removeSong(JNIEnv *env, jobject thiz, jstring dbPath, jstring mainPath, jobject info)
{
	const char *pMainPath = env->GetStringUTFChars(mainPath, NULL);
	printf("MainPath: %s", pMainPath);

	const char *pDbPath = env->GetStringUTFChars(dbPath, NULL);
	printf("DbPath: %s", pDbPath);

	jclass cls = env->GetObjectClass(info);

	jfieldID idField = env->GetFieldID(cls, "id" , "I");
	jint intId = env->GetIntField(info, idField);

	jfieldID typeABCField = env->GetFieldID(cls, "abc", "I");
	jint typeABC = env->GetIntField(info, typeABCField);

	Song_Info_Update_S songInfoUpdate;
	memset(&songInfoUpdate, 0, sizeof(songInfoUpdate));

	songInfoUpdate.song_code5 = intId;
	songInfoUpdate.song_code6 = intId;
	songInfoUpdate.MidiInfo.stFilepos.songABCType = typeABC;

	TOCMerge *tocMerge = new TOCMerge();
	tocMerge->delete_update_song(pMainPath, &songInfoUpdate, 1);
	delete tocMerge;

	deleteSongFromDatabase(pDbPath, &songInfoUpdate, 1);

	env->ReleaseStringUTFChars(dbPath, pDbPath);
	env->ReleaseStringUTFChars(mainPath, pMainPath);
}

void Java_vn_Com_sonca_smartkaraoke_SmartKaraoke_addNewSong(JNIEnv *env, jobject thiz, jstring dbPath, jstring mainPath, jstring subPath, jobject info, int type) {
	const char *pMainPath = env->GetStringUTFChars(mainPath, NULL);
//	printf("MainPath: %s", pMainPath);

	const char *pSubPath = env->GetStringUTFChars(subPath, NULL);
//	printf("SubPath: %s", pSubPath);

	const char *pDbPath = env->GetStringUTFChars(dbPath, NULL);
//	printf("DbPath: %s", pDbPath);

	jclass cls = env->GetObjectClass(info);
	jfieldID singerField = env->GetFieldID(cls, "singer", "Ljava/lang/String;");
	jstring singer = (jstring)env->GetObjectField (info, singerField);
	const char *pSinger = env->GetStringUTFChars(singer, NULL);
//	printf("Singer: %s", pSinger);

	jfieldID authorField = env->GetFieldID(cls, "author", "Ljava/lang/String;");
	jstring author = (jstring)env->GetObjectField (info, authorField);
	const char *pAuthor = env->GetStringUTFChars(author, NULL);
//	printf("Author: %s", pAuthor);

	jfieldID titleField = env->GetFieldID(cls, "title", "Ljava/lang/String;");
	jstring title = (jstring)env->GetObjectField (info, titleField);
	const char *pTitle = env->GetStringUTFChars(title, NULL);
//	printf("Title: %s", pTitle);

	jfieldID lyricField = env->GetFieldID(cls, "lyric", "Ljava/lang/String;");
	jstring lyric = (jstring)env->GetObjectField (info, lyricField);
	const char *pLyric = env->GetStringUTFChars(lyric, NULL);
//	printf("Lyric: %s", pLyric);

	jfieldID idField = env->GetFieldID(cls, "id" , "I");
	jint intId = env->GetIntField(info, idField);

	jfieldID index5Field = env->GetFieldID(cls, "index5", "I");
	jint index5 = env->GetIntField(info, index5Field);

	// mediatype
	jfieldID intField = env->GetFieldID(cls, "mediatype", "I");
	jint media_type = env->GetIntField(info, intField);

	//singer_vocal
	intField = env->GetFieldID(cls, "singer_vocal", "I");
	jint singerVocal = env->GetIntField(info, intField);

	//remix
	intField = env->GetFieldID(cls, "remix", "I");
	jint is_remix = env->GetIntField(info, intField);

	Person_Info_Update_S artistsUpdate[2];
	memset(artistsUpdate, 0, sizeof(artistsUpdate));
	int artistCount = 2;

	StringUtils* strutils = new StringUtils();
	artistsUpdate[0].num_songs.bAuthor = 1;
	artistsUpdate[0].num_songs.language_type = 0;
	artistsUpdate[0].num_songs.uAuthorSongNums = 1;
	artistsUpdate[0].num_songs.uNameWords = 10;
	artistsUpdate[0].pNameOffset = (char *)pAuthor;
	string singerPinyin = strutils->getPinyin(string(pAuthor));
	artistsUpdate[0].pPYOffset = (char *)(singerPinyin.c_str());
	artistsUpdate[0].singerIdx = UINT32_MAX;

	artistsUpdate[1].num_songs.bSinger = 1;
	artistsUpdate[1].num_songs.language_type = 0;
	artistsUpdate[1].num_songs.uSingerSongNums = 1;
	artistsUpdate[1].num_songs.uNameWords = 10;
	artistsUpdate[1].pNameOffset = (char *)pSinger;
	string authorPinyin = strutils->getPinyin(string(pSinger));
	artistsUpdate[1].pPYOffset = (char *)(authorPinyin.c_str());
	artistsUpdate[1].singerIdx = UINT32_MAX;

	Song_Info_Update_S songInfoUpdate;
	memset(&songInfoUpdate, 0, sizeof(songInfoUpdate));

	songInfoUpdate.song_code5 = index5;
	songInfoUpdate.song_code6 = intId;
	songInfoUpdate.pSongNameOffset = (char *)pTitle;
	string titlePinyin = strutils->getPinyin(string(pTitle));
	songInfoUpdate.pPYNameOffset = (char *)(titlePinyin.c_str());
	songInfoUpdate.pLyricOffset = (char *)pLyric;

	songInfoUpdate.property.is_newsong = 0;
	songInfoUpdate.property.is_remix = is_remix;
	songInfoUpdate.property.media_type = media_type;
	songInfoUpdate.property.name_words = 10;
	songInfoUpdate.property.song_type = TRE_TRUNG;
	songInfoUpdate.property.is_hide = 0;
	songInfoUpdate.property.onetouch_type = 0;

	for(int i = 0; i < MAX_ARTIST_PER_SONG; i++) {
		songInfoUpdate.pPersonIdx.author_idxes[i] = UINT16_MAX;
		songInfoUpdate.pPersonIdx.singer_idxes[i] = UINT16_MAX;
	}

	songInfoUpdate.pPersonIdx.author_idxes[0] = 0; // Index of Author in Artist Update List
	songInfoUpdate.pPersonIdx.singer_idxes[0] = 1; // Index of Singer in Artist Update List

	delete strutils;

	uint8_t pSongClip[4];
	Song_Info_Extra_S extra;
	memset(&extra, 0, sizeof(Song_Info_Extra_S));
	extra.bNewTocFormat = 1;
	extra.bVocalMp3Mkv = singerVocal & 0x01;
	extra.bUser = 0;
	Song_Info_Extra1_S extra1;
	memset(&extra1, 0, sizeof(Song_Info_Extra1_S));
	extra1.bNewTocFormat = 1;
	pSongClip[0] = 0;
	memcpy(&pSongClip[1], &extra, sizeof(Song_Info_Extra_S));
	memcpy(&pSongClip[2], &extra1, sizeof(Song_Info_Extra1_S));
	pSongClip[3] = 0;
	songInfoUpdate.pSongClips = pSongClip;

	TOCMerge *tocMerge = new TOCMerge();
	tocMerge->insert_update_song(pMainPath,pSubPath, &songInfoUpdate, 1, artistsUpdate, artistCount, type);
	delete tocMerge;

	insertNewSongToDatabase(pDbPath, &songInfoUpdate, 1, artistsUpdate, artistCount);

	env->ReleaseStringUTFChars(singer, pSinger);
	env->ReleaseStringUTFChars(author, pAuthor);
	env->ReleaseStringUTFChars(title, pTitle);
	env->ReleaseStringUTFChars(lyric, pLyric);
	env->ReleaseStringUTFChars(dbPath, pDbPath);
	env->ReleaseStringUTFChars(subPath, pSubPath);
	env->ReleaseStringUTFChars(mainPath, pMainPath);
}

void Java_vn_Com_sonca_smartkaraoke_SmartKaraoke_removeSongList(JNIEnv *env, jobject thiz, jstring dbPath, jstring mainPath, jobjectArray infoArray)
{
	int songCount = env->GetArrayLength(infoArray);
	if(songCount <= 0) return;

	vector<Song_Info_Update_S> songInfos;

	jobject firstObject = env->GetObjectArrayElement(infoArray, 0);
	jclass cls = env->GetObjectClass(firstObject);

	jfieldID idField = env->GetFieldID(cls, "id" , "I");
	jfieldID typeABCField = env->GetFieldID(cls, "abc", "I");


	for(int i = 0; i < songCount; i++) {
		jobject anObject = env->GetObjectArrayElement(infoArray, i);

		jint intId = env->GetIntField(anObject, idField);
		jint typeABC = env->GetIntField(anObject, typeABCField);
		env->DeleteLocalRef(anObject);

		Song_Info_Update_S songInfoUpdate;
		memset(&songInfoUpdate, 0, sizeof(songInfoUpdate));

		songInfoUpdate.song_code5 = intId;
		songInfoUpdate.song_code6 = intId;
		songInfoUpdate.MidiInfo.stFilepos.songABCType = typeABC;
		songInfos.push_back(songInfoUpdate);
	}

	const char *pMainPath = env->GetStringUTFChars(mainPath, NULL);
	printf("MainPath: %s", pMainPath);

	const char *pDbPath = env->GetStringUTFChars(dbPath, NULL);
	printf("DbPath: %s", pDbPath);

	TOCMerge *tocMerge = new TOCMerge();
	int status = tocMerge->delete_update_song(pMainPath, songInfos.data(), songInfos.size());
	delete tocMerge;

	if(status >= 0) {
		deleteSongFromDatabase(pDbPath, songInfos.data(), songInfos.size());
	}
	env->ReleaseStringUTFChars(dbPath, pDbPath);
	env->ReleaseStringUTFChars(mainPath, pMainPath);
}

void Java_vn_Com_sonca_smartkaraoke_SmartKaraoke_addNewSongList(JNIEnv *env, jobject thiz, jstring dbPath, jstring mainPath, jstring subPath, jobjectArray infoArray, int type) {
	int songCount = env->GetArrayLength(infoArray);
	if(songCount <= 0) return;

	vector<string> songNames;
	vector<string> songNamePYs;
	vector<string> artistNames;
	vector<string> artistNamePYs;
	vector<string> lyrics;
	char* songclipInfo;
	vector<Song_Info_Update_S> songInfos;
	vector<Person_Info_Update_S> artistInfos;
	StringUtils* strutils = new StringUtils();

	jclass alCls = env->FindClass("java/util/ArrayList");
	jclass songInfoCls = env->FindClass("vn/com/sonca/params/SongInfo");
	jclass artistInfoCls = env->FindClass("vn/com/sonca/params/SingerMusicianInfo");
	jfieldID artistNameField = env->GetFieldID(artistInfoCls, "name", "Ljava/lang/String;");
	jfieldID artistCIDField = env->GetFieldID(artistInfoCls, "coverID", "I");
	jfieldID artistLangIDField = env->GetFieldID(artistInfoCls, "langID", "I");

	jmethodID alGetId  = env->GetMethodID(alCls, "get", "(I)Ljava/lang/Object;");
	jmethodID alSizeId = env->GetMethodID(alCls, "size", "()I");

	jfieldID singerField = env->GetFieldID(songInfoCls, "singer", "Ljava/lang/String;");
	jfieldID authorField = env->GetFieldID(songInfoCls, "author", "Ljava/lang/String;");
	jfieldID titleField = env->GetFieldID(songInfoCls, "title", "Ljava/lang/String;");
	jfieldID lyricField = env->GetFieldID(songInfoCls, "lyric", "Ljava/lang/String;");
	jfieldID idField = env->GetFieldID(songInfoCls, "id" , "I");
	jfieldID index5Field = env->GetFieldID(songInfoCls, "index5", "I");
	jfieldID mediatypeField = env->GetFieldID(songInfoCls, "mediatype", "I");
	jfieldID singervocalField = env->GetFieldID(songInfoCls, "singer_vocal", "I");
	jfieldID remixField = env->GetFieldID(songInfoCls, "remix", "I");

	jfieldID zshowField = env->GetFieldID(songInfoCls, "zShow", "Z");
	jfieldID zonetouchField = env->GetFieldID(songInfoCls, "zOneTouch", "I");
	jfieldID zNewSongField = env->GetFieldID(songInfoCls, "zNewSong", "Z");
	jfieldID zTypeABCField = env->GetFieldID(songInfoCls, "abc", "I");
	jfieldID zSCAField = env->GetFieldID(songInfoCls, "zSCA", "Z");
	jfieldID zLangIDField = env->GetFieldID(songInfoCls, "langID", "I");
	jfieldID zSongTypeIDField = env->GetFieldID(songInfoCls, "songTypeID", "I");

	jfieldID singerInfoField = env->GetFieldID(songInfoCls, "listSinger", "Ljava/util/ArrayList;");
	jfieldID authorInfoField = env->GetFieldID(songInfoCls, "listMusician", "Ljava/util/ArrayList;");

	songclipInfo = (char *)malloc(4*songCount);
	memset(songclipInfo, 0, 4*songCount);
	for(int i = 0; i < songCount; i++) {
		jobject anObject = env->GetObjectArrayElement(infoArray, i);

		jstring title = (jstring)env->GetObjectField (anObject, titleField);
		const char *pTitle = env->GetStringUTFChars(title, NULL);
		//	printf("Title: %s", pTitle);

		jstring lyric = (jstring)env->GetObjectField (anObject, lyricField);
		const char *pLyric = env->GetStringUTFChars(lyric, NULL);
		//	printf("Lyric: %s", pLyric);

		jint intId = env->GetIntField(anObject, idField);
		// printf("SongId: %d", intId);

		jint index5 = env->GetIntField(anObject, index5Field);
		jint media_type = env->GetIntField(anObject, mediatypeField);
		jint singerVocal = env->GetIntField(anObject, singervocalField);
		jint remix = env->GetIntField(anObject, remixField);

		jboolean zshow = env->GetBooleanField(anObject, zshowField);
		jint zonetouch = env->GetIntField(anObject, zonetouchField);
		jboolean znewsong = env->GetBooleanField(anObject, zNewSongField);
		jint ztypeABC = env->GetIntField(anObject, zTypeABCField);
		jboolean zSCA = env->GetBooleanField(anObject, zSCAField);
		jint zLangID = env->GetIntField(anObject, zLangIDField);
		jint zSongTypeID = env->GetIntField(anObject, zSongTypeIDField);

		songNames.push_back(string(pTitle) + '\0');
		songNamePYs.push_back(strutils->getPinyin(songNames[i]) + '\0');
		lyrics.push_back(string(pLyric) + '\0');

		env->ReleaseStringUTFChars(title, pTitle);
		env->DeleteLocalRef(title);
		env->ReleaseStringUTFChars(lyric, pLyric);
		env->DeleteLocalRef(lyric);

		Song_Info_Update_S songInfoUpdate;
		memset(&songInfoUpdate, 0, sizeof(songInfoUpdate));

		songInfoUpdate.song_code5 = index5;
		songInfoUpdate.song_code6 = intId;
		songInfoUpdate.pSongNameOffset = (char *)songNames[i].c_str();
		songInfoUpdate.pPYNameOffset = (char *)songNamePYs[i].c_str();
		songInfoUpdate.pLyricOffset = (char *)lyrics[i].c_str();

		songInfoUpdate.property.is_newsong = znewsong;
		songInfoUpdate.property.is_remix = remix;
		songInfoUpdate.property.media_type = media_type;
		songInfoUpdate.property.name_words = 10;
		songInfoUpdate.property.is_hide = (zshow == true) ? 0 : 1;
		songInfoUpdate.property.onetouch_type = zonetouch;
		songInfoUpdate.property.language_type = zLangID;
		songInfoUpdate.property.song_type = zSongTypeID >= 15 ? 0 : zSongTypeID;
#if (INSERT_TOC_OEM)
		songInfoUpdate.MidiInfo.stFilepos.songABCType = 10;
#else
		songInfoUpdate.MidiInfo.stFilepos.songABCType = ztypeABC;
#endif

		char *pSongClip = songclipInfo + i*4;
		Song_Info_Extra_S extra;
		memset(&extra, 0, sizeof(Song_Info_Extra_S));
		extra.bNewTocFormat = 1;
		extra.bVocalMp3Mkv = singerVocal & 0x01;
		extra.bUser = zSCA;
		Song_Info_Extra1_S extra1;
		memset(&extra1, 0, sizeof(Song_Info_Extra1_S));
		extra1.bNewTocFormat = 1;
		extra1.bNewDownload = znewsong;
		pSongClip[0] = 0;
		memcpy(pSongClip+1, &extra, sizeof(Song_Info_Extra_S));
		memcpy(pSongClip+2, &extra1, sizeof(Song_Info_Extra1_S));
		pSongClip[3] = 0;
		songInfoUpdate.pSongClips = (uint8_t *)pSongClip;

		for(int i = 0; i < MAX_ARTIST_PER_SONG; i++) {
			songInfoUpdate.pPersonIdx.author_idxes[i] = UINT16_MAX;
			songInfoUpdate.pPersonIdx.singer_idxes[i] = UINT16_MAX;
		}

		// Singer Info
		jobject arraySinger = env->GetObjectField(anObject, singerInfoField);
		jint artistSize = static_cast<int>(env->CallIntMethod(arraySinger, alSizeId));
		for(int i = 0; i < artistSize; i++) {
			if(i >= MAX_ARTIST_PER_SONG) break;

			jobject singerObj = env->CallObjectMethod(arraySinger, alGetId, i);

			jstring singer = (jstring)env->GetObjectField (singerObj, artistNameField);
			jint coverID = env->GetIntField(singerObj, artistCIDField);
			jint langID = env->GetIntField(singerObj, artistLangIDField);

			const char *pSinger = env->GetStringUTFChars(singer, NULL);
			printf("Singer: %s", pSinger);

			int singerIdx = 0;
			// check has exist
//			for(singerIdx = 0; singerIdx < artistNames.size(); singerIdx++) {
//				if(strcmp(artistNames[singerIdx].c_str(), pSinger) == 0) {
//					break;
//				}
//			}

			vector<Person_Info_Update_S>::iterator it;  // declare an iterator to a vector of strings
			for(it = artistInfos.begin(); it != artistInfos.end(); it++,singerIdx++ )    {
			    // found nth element..print and break.
				if(strcmp(it->pNameOffset, pSinger) == 0 && it->singerIdx == coverID) {
					break;
				}
			}

			if(singerIdx >= artistNames.size()) {
				printf("Artist not found: %s. Add new", pSinger);
				artistNames.push_back(string(pSinger) + '\0');
				artistNamePYs.push_back(strutils->getPinyin(artistNames[singerIdx]) + '\0');

				Person_Info_Update_S artistsUpdate;
				memset(&artistsUpdate, 0, sizeof(Person_Info_Update_S));
				artistsUpdate.num_songs.language_type = langID;
				artistsUpdate.num_songs.uAuthorSongNums = 1;
				artistsUpdate.num_songs.uNameWords = 10;
				artistsUpdate.pNameOffset = (char *)artistNames[singerIdx].c_str();
				artistsUpdate.pPYOffset = (char *)artistNamePYs[singerIdx].c_str();
				artistsUpdate.singerIdx = coverID;

				artistInfos.push_back(artistsUpdate);
			}
			printf("Singer index: %d", singerIdx);
			artistInfos[singerIdx].num_songs.bSinger = 1;

			songInfoUpdate.pPersonIdx.singer_idxes[i] = singerIdx; // Index of Singer in Artist Update List

			env->ReleaseStringUTFChars(singer, pSinger);
			env->DeleteLocalRef(singer);
			env->DeleteLocalRef(singerObj);
		}
		env->DeleteLocalRef(arraySinger);

		// Author Info
		jobject arrayAuthor = env->GetObjectField(anObject, authorInfoField);
		artistSize = static_cast<int>(env->CallIntMethod(arrayAuthor, alSizeId));
		for(int i = 0; i < artistSize; i++) {
			if(i >= MAX_ARTIST_PER_SONG) break;

			jobject authorObj = env->CallObjectMethod(arrayAuthor, alGetId, i);

			jstring author = (jstring)env->GetObjectField (authorObj, artistNameField);
			jint coverID = env->GetIntField(authorObj, artistCIDField);
			jint langID = env->GetIntField(authorObj, artistLangIDField);

			const char *pAuthor = env->GetStringUTFChars(author, NULL);
			printf("Author: %s", pAuthor);

			int authorIdx = 0;
			// check has exist
//			for(authorIdx = 0; authorIdx < artistNames.size(); authorIdx++) {
//				if(strcmp(artistNames[authorIdx].c_str(), pAuthor) == 0) {
//					break;
//				}
//			}

			vector<Person_Info_Update_S>::iterator it;  // declare an iterator to a vector of strings
			for(it = artistInfos.begin(); it != artistInfos.end(); it++,authorIdx++ )    {
				// found nth element..print and break.
				if(strcmp(it->pNameOffset, pAuthor) == 0 && it->singerIdx == coverID) {
					break;
				}
			}

			if(authorIdx >= artistNames.size()) {
				printf("Artist not found: %s. Add new", pAuthor);
				artistNames.push_back(string(pAuthor) + '\0');
				artistNamePYs.push_back(strutils->getPinyin(artistNames[authorIdx]) + '\0');

				Person_Info_Update_S artistsUpdate;
				memset(&artistsUpdate, 0, sizeof(Person_Info_Update_S));
				artistsUpdate.num_songs.language_type = langID;
				artistsUpdate.num_songs.uAuthorSongNums = 1;
				artistsUpdate.num_songs.uNameWords = 10;
				artistsUpdate.pNameOffset = (char *)artistNames[authorIdx].c_str();
				artistsUpdate.pPYOffset = (char *)artistNamePYs[authorIdx].c_str();
				artistsUpdate.singerIdx = coverID;

				artistInfos.push_back(artistsUpdate);
			}
			artistInfos[authorIdx].num_songs.bAuthor = 1;
			printf("Author index: %d", authorIdx);

			songInfoUpdate.pPersonIdx.author_idxes[i] = authorIdx; // Index of Author in Artist Update List

			env->ReleaseStringUTFChars(author, pAuthor);
			env->DeleteLocalRef(author);
			env->DeleteLocalRef(authorObj);
		}
		env->DeleteLocalRef(arrayAuthor);

		songInfos.push_back(songInfoUpdate);

		env->DeleteLocalRef(anObject);
	}
	delete strutils;

	env->DeleteLocalRef(alCls);
	env->DeleteLocalRef(songInfoCls);
	env->DeleteLocalRef(artistInfoCls);

	const char *pMainPath = env->GetStringUTFChars(mainPath, NULL);
	//	printf("MainPath: %s", pMainPath);

	const char *pSubPath = env->GetStringUTFChars(subPath, NULL);
	//	printf("SubPath: %s", pSubPath);

	const char *pDbPath = env->GetStringUTFChars(dbPath, NULL);
	//	printf("DbPath: %s", pDbPath);

	TOCMerge *tocMerge = new TOCMerge();
	int status = tocMerge->insert_update_song(pMainPath,pSubPath, songInfos.data(), songInfos.size(), artistInfos.data(), artistInfos.size(), type);
	delete tocMerge;
	if(status >= 0) {
		insertNewSongToDatabase(pDbPath, songInfos.data(), songInfos.size(), artistInfos.data(), artistInfos.size());
	}
	free(songclipInfo);
	env->ReleaseStringUTFChars(dbPath, pDbPath);
	env->ReleaseStringUTFChars(subPath, pSubPath);
	env->ReleaseStringUTFChars(mainPath, pMainPath);
}

int gzip_uncompress(uint8_t *destBuf, unsigned long *destLen, const uint8_t *srcBuf, unsigned long srcLen) {
	unsigned long avail_out = *destLen;
	int rv = Z_OK;
	z_stream stream;
	stream.zalloc = Z_NULL;
	stream.zfree = Z_NULL;
	stream.avail_in = (uint)srcLen;
	stream.next_in = (Bytef *)srcBuf;
	stream.total_out = 0;
	stream.avail_out = 0;

	if ((rv = inflateInit2(&stream, 47)) == Z_OK)
	{
		int status = Z_OK;
		while (status == Z_OK)
		{
			stream.next_out = (uint8_t *)destBuf + stream.total_out;
			stream.avail_out = (uInt)(avail_out - stream.total_out);
			status = inflate (&stream, Z_SYNC_FLUSH);
			rv = status;
		}
		if ((rv = inflateEnd(&stream)) == Z_OK)
		{
			if (status == Z_STREAM_END)
			{
				*destLen = stream.total_out;
			}
		}
	}
	return rv;
}

// Read file Midi and return data byte at offset
jbyteArray Java_vn_Com_sonca_smartkaraoke_SmartKaraoke_getDataBytes(JNIEnv *env, jobject thiz, jboolean isMidi, jstring src, jint index, jint offset, jint len) {

	printf("Read Midi File offset: %d<>size: %d\n", offset, len);
	const char* pSrc = env->GetStringUTFChars(src, NULL);

	int srcFd = open(pSrc, O_RDONLY);
	if(srcFd == -1) {
		printf("Error while open file: %s\n", pSrc);
		env->ReleaseStringUTFChars(src, pSrc);
		return 0;
	}
	printf("src file: %s", pSrc);

	KarToc *toc = new KarToc();
	int buffSize = 128*1024;
	uint8_t *block = (uint8_t *)malloc(buffSize);
	memset(block, 0, buffSize);

	off64_t fileOffset = ((off64_t)offset) << 11;
	long fileLen = ((unsigned long)len) << 11;

	lseek64(srcFd, fileOffset, SEEK_SET);
	int remainByte = fileLen;
	int bRead = 0;
	int byteWrite = 0;

	read(srcFd, block, 4);
	uint8_t encrypt = toc->DetectMidiMp3Format(block);
	printf("Encryption mode detect: %d", encrypt);
	if(encrypt == DECRYPT_MIDIMP3_SONCA || encrypt == DECRYPT_MIDIMP3_USER) {
		//		fseek(pFileSrc, fileOffset+4, SEEK_SET);
		lseek64(srcFd, fileOffset + 4, SEEK_SET);
		remainByte = fileLen - 4;
	}else {
		//		fseek(pFileSrc, fileOffset, SEEK_SET);
		lseek64(srcFd, fileOffset, SEEK_SET);
	}
	char strPwd[7];
	sprintf(strPwd, "%06d", index);
	toc->iPasswordStreamPos = 0;
	long pos = 0;
	do {
		bRead = read(srcFd, block, remainByte > buffSize ? buffSize : remainByte);
		if(encrypt == DECRYPT_MIDIMP3_SONCA) {
			if(isMidi) {
				toc->PasswordMaskBytes(block, pos, bRead, strPwd);
				unsigned long decompressedDataLen = buffSize;
				uint8_t *decompressedBuf = (uint8_t*)malloc(decompressedDataLen);
				int rv = gzip_uncompress(decompressedBuf, &decompressedDataLen, block, bRead);
				if (Z_OK != rv) {
					printf("decompression error: %d", rv);
					free(decompressedBuf);
					break;
				}
				memcpy(block, decompressedBuf, decompressedDataLen);
				free(decompressedBuf);
				byteWrite = decompressedDataLen;
			}
//			else {
//				toc->PasswordMaskBytesMp3(block, pos, bRead, strPwd);
//				byteWrite = bRead;
//			}
		}else if(encrypt == DECRYPT_MIDIMP3_USER) {
			toc->PasswordMaskBytesUser(block, 0, bRead);
		}else {
			byteWrite = bRead;
		}
		pos+=bRead;

		remainByte -= bRead;
	}while (remainByte > 0);

	jbyteArray byteArray = env->NewByteArray(byteWrite);
	env->SetByteArrayRegion(byteArray, 0, byteWrite, (jbyte *)block);

	close(srcFd);
	free(block);

	env->ReleaseStringUTFChars(src, pSrc);

	return byteArray;
}

jint Java_vn_Com_sonca_smartkaraoke_SmartKaraoke_getDataMidi(JNIEnv *env, jobject thiz, jstring src, jstring dst, jint index) {
	const char* pSrc = env->GetStringUTFChars(src, NULL);
	const char* pDst= env->GetStringUTFChars(dst, NULL);

	int srcFd = open(pSrc, O_RDONLY);
	if(srcFd == -1) {
		printf("Error while open file: %s\n", pSrc);
		env->ReleaseStringUTFChars(src, pSrc);
		return 0;
	}
	printf("src file: %s", pSrc);

	FILE *pFileDst = fopen(pDst, "wb");
	if(pFileDst == NULL) {
		printf("Error while open file: %s\n", pDst);
		//		fclose(pFileSrc);
		close(srcFd);
		env->ReleaseStringUTFChars(src, pSrc);
		env->ReleaseStringUTFChars(dst, pDst);
		return 0;
	}
	printf("dst file: %s", pDst);

	KarToc *toc = new KarToc();
	int buffSize = 128*1024;
	uint8_t *block = (uint8_t *)malloc(buffSize);
	memset(block, 0, buffSize);

	struct stat st;
	fstat(srcFd, &st);

	off64_t fileOffset = 0;
	printf("File size111: %lu: \n", st.st_size);
	long fileLen = ((unsigned long)st.st_size) ;
	printf("File size: %lu: \n", fileLen);
	printf("File size: %lu: \n", fileLen);
	uint32_t duration = 0;

	//	int seekstatus = fseek(pFileSrc, tmpFileOffset, SEEK_SET);
	lseek64(srcFd, fileOffset, SEEK_SET);
	int remainByte = fileLen;
	int bRead = 0;
	int byteWrite = 0;

	//	read = fread(block, 1, 4, pFileSrc);
	read(srcFd, block, 4);
	uint8_t encrypt = toc->DetectMidiMp3Format(block);
	printf("Encryption mode detect: %d", encrypt);
	if(encrypt == DECRYPT_MIDIMP3_SONCA || encrypt == DECRYPT_MIDIMP3_USER) {
		//		fseek(pFileSrc, fileOffset+4, SEEK_SET);
		lseek64(srcFd, fileOffset + 4, SEEK_SET);
		remainByte = fileLen - 4;
	}else {
		//		fseek(pFileSrc, fileOffset, SEEK_SET);
		lseek64(srcFd, fileOffset, SEEK_SET);
	}
	char strPwd[7];
	sprintf(strPwd, "%06d", index);
	toc->iPasswordStreamPos = 0;
	long pos = 0;
	do {
		//		read = fread(block, 1, remainByte > buffSize ? buffSize : remainByte, pFileSrc);
		bRead = read(srcFd, block, remainByte > buffSize ? buffSize : remainByte);
		if(encrypt == DECRYPT_MIDIMP3_SONCA) {
//			if(isMidi) {
				toc->PasswordMaskBytes(block, pos, bRead, strPwd);
				unsigned long decompressedDataLen = buffSize;
				uint8_t *decompressedBuf = (uint8_t*)malloc(decompressedDataLen);
				int rv = gzip_uncompress(decompressedBuf, &decompressedDataLen, block, bRead);
				if (Z_OK != rv) {
					printf("decompression error: %d", rv);
					free(decompressedBuf);
					break;
				}
				memcpy(block, decompressedBuf, decompressedDataLen);
				free(decompressedBuf);
				byteWrite = decompressedDataLen;
//			}else {
//				toc->PasswordMaskBytesMp3(block, pos, bRead, strPwd);
//				byteWrite = bRead;
//			}
		}else if(encrypt == DECRYPT_MIDIMP3_USER) {
			toc->PasswordMaskBytesUser(block, 0, bRead);
		}else {
			byteWrite = bRead;
		}
		pos+=bRead;
		if(fwrite(block, 1, byteWrite, pFileDst) == 0)
		{
			printf("Error writing to blob handle. Offset %i, len %i\n", fileOffset, fileLen);
			break;
		}
		remainByte -= bRead;
	}while (remainByte > 0);

	//	fclose(pFileSrc);
	close(srcFd);
	fclose(pFileDst);
	free(block);

	env->ReleaseStringUTFChars(src, pSrc);
	env->ReleaseStringUTFChars(dst, pDst);
	return duration;
}

jint Java_vn_Com_sonca_smartkaraoke_SmartKaraoke_getData(JNIEnv *env, jobject thiz, jboolean isMidi, jstring src, jstring dst, jint index, jint offset, jint len) {
	const char* pSrc = env->GetStringUTFChars(src, NULL);
	const char* pDst= env->GetStringUTFChars(dst, NULL);

	int srcFd = open(pSrc, O_RDONLY);
	if(srcFd == -1) {
		printf("Error while open file: %s\n", pSrc);
		env->ReleaseStringUTFChars(src, pSrc);
		return 0;
	}
	//	printf("src file: %s", pSrc);

	//	FILE *pFileSrc = fopen(pSrc, "rb");
	//	if(pFileSrc == NULL) {
	//		printf("Error while open file: %s\n", pSrc);
	//		env->ReleaseStringUTFChars(src, pSrc);
	//		return;
	//	}
	//	printf("src file: %s", pSrc);

	FILE *pFileDst = fopen(pDst, "wb");
	if(pFileDst == NULL) {
		printf("Error while open file: %s\n", pDst);
		//		fclose(pFileSrc);
		close(srcFd);
		env->ReleaseStringUTFChars(src, pSrc);
		env->ReleaseStringUTFChars(dst, pDst);
		return 0;
	}
	//	printf("dst file: %s", pDst);

	KarToc *toc = new KarToc();
	int buffSize = 128*1024;
	uint8_t *block = (uint8_t *)malloc(buffSize);
	memset(block, 0, buffSize);

	off64_t fileOffset = ((off64_t)offset) << 11;
	long fileLen = ((unsigned long)len) << 11;

	lseek64(srcFd, fileOffset + fileLen - 4, SEEK_SET);
	uint8_t durationBytes[4];
	read(srcFd, durationBytes, 4);
	uint32_t duration = 0;
	duration |= (((uint32_t)durationBytes[0]) << 0);
	duration |= (((uint32_t)durationBytes[1]) << 8);
	duration |= (((uint32_t)durationBytes[2]) << 16);
	duration |= (((uint32_t)durationBytes[3]) << 24);

	//	int seekstatus = fseek(pFileSrc, tmpFileOffset, SEEK_SET);
	lseek64(srcFd, fileOffset, SEEK_SET);
	int remainByte = fileLen;
	int bRead = 0;
	int byteWrite = 0;

	//	read = fread(block, 1, 4, pFileSrc);
	read(srcFd, block, 4);
	uint8_t encrypt = toc->DetectMidiMp3Format(block);
	printf("Encryption mode detect: %d", encrypt);
	if(encrypt == DECRYPT_MIDIMP3_SONCA || encrypt == DECRYPT_MIDIMP3_USER) {
		//		fseek(pFileSrc, fileOffset+4, SEEK_SET);
		lseek64(srcFd, fileOffset + 4, SEEK_SET);
		remainByte = fileLen - 4;
	}else {
		//		fseek(pFileSrc, fileOffset, SEEK_SET);
		lseek64(srcFd, fileOffset, SEEK_SET);
	}
	char strPwd[7];
	sprintf(strPwd, "%06d", index);
	toc->iPasswordStreamPos = 0;
	long pos = 0;
	do {
		//		read = fread(block, 1, remainByte > buffSize ? buffSize : remainByte, pFileSrc);
		bRead = read(srcFd, block, remainByte > buffSize ? buffSize : remainByte);
		if(encrypt == DECRYPT_MIDIMP3_SONCA) {
			if(isMidi) {
				toc->PasswordMaskBytes(block, pos, bRead, strPwd);
				unsigned long decompressedDataLen = buffSize;
				uint8_t *decompressedBuf = (uint8_t*)malloc(decompressedDataLen);
				int rv = gzip_uncompress(decompressedBuf, &decompressedDataLen, block, bRead);
				if (Z_OK != rv) {
					printf("decompression error: %d", rv);
					free(decompressedBuf);
					break;
				}
				memcpy(block, decompressedBuf, decompressedDataLen);
				free(decompressedBuf);
				byteWrite = decompressedDataLen;
			}else {
				toc->PasswordMaskBytesMp3(block, pos, bRead, strPwd);
				byteWrite = bRead;
			}
		}else if(encrypt == DECRYPT_MIDIMP3_USER) {
			toc->PasswordMaskBytesUser(block, 0, bRead);
		}else {
			byteWrite = bRead;
		}
		pos+=bRead;
		if(fwrite(block, 1, byteWrite, pFileDst) == 0)
		{
			printf("Error writing to blob handle. Offset %i, len %i\n", fileOffset, fileLen);
			break;
		}
		remainByte -= bRead;
	}while (remainByte > 0);

	//	fclose(pFileSrc);
	close(srcFd);
	fclose(pFileDst);
	free(block);

	env->ReleaseStringUTFChars(src, pSrc);
	env->ReleaseStringUTFChars(dst, pDst);
	return duration;
}

jint Java_vn_Com_sonca_smartkaraoke_SmartKaraoke_getStructSize(JNIEnv *env, jobject thiz) {
	return sizeof(spl_song);
}

jbyteArray Java_vn_Com_sonca_smartkaraoke_SmartKaraoke_parseFileInfo(JNIEnv *env, jobject thiz, jstring filename) {
	const char* pFilename = env->GetStringUTFChars(filename, NULL);
	int status = 0;
	int i = 0;
	jbyteArray byteArray = NULL;
	uint32_t songCount = 0;
	Kok_IdxFileHdr_S *header = NULL;
	Song_Info_S *songInfo = NULL;
	Song_Info_Extra_S *pExtra = NULL;
	Song_Info_Extra1_S *pExtra1 = NULL;
	spl_song *pSimpleSong = NULL;
	uint8_t *pTmp = NULL;
	printf("parse file info: %s", pFilename);
	KarToc *toc = new KarToc();
	status = toc->parse_file(pFilename);
	if(status != 0) {
		printf("Error while parse TOC: %s", pFilename);
		goto EXIT;
	}

	header = toc->getHeader();
	songCount = 0;
	//	printf("Total song count: %d", header->uSongNums);
	for(i = 0; i < header->uSongNums; i++) {
		songInfo = toc->getSongInfo(i);
		//		if(songInfo->property.media_type != VIDEO) songCount++;
		songCount++;
	}
	//	printf("Total song without ktv count: %d", songCount);

	pSimpleSong = (spl_song *)malloc(songCount * sizeof(spl_song));
	if(pSimpleSong == NULL) {
		printf("Error while alloc simple songs");
		goto EXIT;
	}
	memset(pSimpleSong, 0, songCount * sizeof(spl_song));

	songCount = 0;
	for(i = 0; i < header->uSongNums; i++) {
		songInfo = toc->getSongInfo(i);
		if(songInfo->property.media_type != VIDEO) {
			pSimpleSong[songCount].song_code6 = songInfo->song_code6;
			pSimpleSong[songCount].MidiInfo.stFilepos.songABCType = songInfo->MidiInfo.stFilepos.songABCType;
			pSimpleSong[songCount].MidiInfo.stFilepos.fileIdx = songInfo->MidiInfo.stFilepos.fileIdx;
			pSimpleSong[songCount].MidiInfo.stFilepos.fileOffset = songInfo->MidiInfo.stFilepos.fileOffset;
			pSimpleSong[songCount].MidiInfo.filesize = songInfo->MidiInfo.filesize;

			pSimpleSong[songCount].Mp3Info.stFilepos.fileIdx = songInfo->Mp3Info.stFilepos.fileIdx;
			pSimpleSong[songCount].Mp3Info.stFilepos.fileOffset = songInfo->Mp3Info.stFilepos.fileOffset;
			pSimpleSong[songCount].Mp3Info.filesize = songInfo->Mp3Info.filesize;

			Song_Info_Extra1_S *extra1 = toc->getSongInfoExtra1(i);
			if(extra1 != NULL) {
				pSimpleSong[songCount].Extra.is2ndStream = extra1->bMp3VocalFile;
			}else {
				pSimpleSong[songCount].Extra.is2ndStream = false;
			}


			//			if(songInfo->song_code6 == 112535) {
			//				printf("Song id: %d typeABC: %d", songInfo->song_code6, songInfo->MidiInfo.stFilepos.songABCType);
			//				printf("mid:%d#%d#%d", songInfo->MidiInfo.stFilepos.fileIdx, songInfo->MidiInfo.stFilepos.fileOffset, songInfo->MidiInfo.filesize); // index#offset#size
			//				printf("mp3:%d#%d#%d", songInfo->Mp3Info.stFilepos.fileIdx, songInfo->Mp3Info.stFilepos.fileOffset, songInfo->Mp3Info.filesize); //index#offset#size
			//				printf("b2streams:%d", pSimpleSong[songCount].Extra.is2ndStream); //b2Stream
			//			}

			uint8_t* pSongClip = (uint8_t *)toc->offsetToPointer(songInfo->pSongClips);
			uint8_t numClips = *(pSongClip);
			//			printf("Song id: %d clips count: %d", songInfo->song_code6, numClips);

			numClips = numClips > MAX_CLIPS_PER_SONG ? MAX_CLIPS_PER_SONG : numClips;
			memcpy(&(pSimpleSong[songCount].songClips), (pSongClip + 1), numClips *2);

			songCount++;
		}
	}

	//	printf("first object\n");
	//	pTmp = (uint8_t *)pSimpleSong;
	//	for(i = 0; i < sizeof(spl_song); i++) {
	//		printf("%02x, ", pTmp[i]);
	//	}
	//	printf("end of first object\n");

	byteArray = env->NewByteArray(songCount * sizeof(spl_song));
	env->SetByteArrayRegion(byteArray, 0, songCount * sizeof(spl_song), (jbyte *)pSimpleSong);
	free(pSimpleSong);

	EXIT:
	if(toc != NULL) delete toc;
	env->ReleaseStringUTFChars(filename, pFilename);
	return byteArray;
}

jintArray Java_vn_com_sonca_smartkaraoke_SmartKaraoke_getVersionInfo(JNIEnv* env, jobject thiz, jstring filePath)
{
	int i = 0;
	int parseStat = 0;
	const char *cIdxPath = env->GetStringUTFChars(filePath, NULL);
	jint params[9];
	Kok_IdxFileHdr_S *header = NULL;
	memset(params, 0, sizeof(params));
	KarToc *tocParse = new KarToc();
	parseStat = tocParse->parse_file_header(cIdxPath);
	if(parseStat != 0) {
		printf("getVersionInfo parse failed");
		goto exit;
	}

	header = tocParse->getHeader();
	params[0] = header->Magic.Version;
	params[1] = header->Magic.Revision;

	params[2] = header->Magic.Version1;
	params[3] = header->Magic.Revision1;

	params[4] = header->Magic.Version2;
	params[5] = header->Magic.Revision2;

	params[6] = header->Magic.Version3;
	params[7] = header->Magic.Revision3;

	exit:
	env->ReleaseStringUTFChars(filePath, cIdxPath);
	delete tocParse;

	jintArray intArray = env->NewIntArray(8);
	if (NULL == intArray) {
		printf("failed to create int array");
		return NULL;
	}

	env->SetIntArrayRegion(intArray, 0, 8, params);

	return intArray;
}

jint Java_vn_com_sonca_smartkaraoke_SmartKaraoke_mergeDataUpdate(JNIEnv* env, jobject thiz, jstring mainPath, jstring subPath, jstring updatePath, jint type)
{
	const char *cMainPath = env->GetStringUTFChars(mainPath, NULL);
	const char *cSubPath = env->GetStringUTFChars(subPath, NULL);
	const char *cUpdatePath = env->GetStringUTFChars(updatePath, NULL);
	int mergeStatus = 0;

	TOCMerge *mergeToc = new TOCMerge();

	make_dirs(cMainPath);
	make_dirs(cSubPath);
	mergeStatus = mergeToc->merge_toc_update(cMainPath, cSubPath, cUpdatePath, type);

	delete mergeToc;
	env->ReleaseStringUTFChars(mainPath, cMainPath);
	env->ReleaseStringUTFChars(subPath, cSubPath);
	env->ReleaseStringUTFChars(updatePath, cUpdatePath);

	return mergeStatus;
}

jint Java_vn_com_sonca_smartkaraoke_SmartKaraoke_renameDataUpdate(JNIEnv* env, jobject thiz, jstring subPath, jint maxIdx, jint maxVol)
{
	const char *cSubPath = env->GetStringUTFChars(subPath, NULL);
	int mergeStatus = 0;

	TOCMerge *mergeToc = new TOCMerge();
	make_dirs(cSubPath);
	mergeStatus = mergeToc->rename_megvol_sub(cSubPath, maxIdx, maxVol);
	delete mergeToc;

	env->ReleaseStringUTFChars(subPath, cSubPath);

	return mergeStatus;
}

jint Java_vn_com_sonca_smartkaraoke_SmartKaraoke_setDataUpdateVersion(JNIEnv* env, jobject thiz, jstring subPath, jint volDisc, jint volXUser, jint volUser)
{
	const char *cSubPath = env->GetStringUTFChars(subPath, NULL);
	int mergeStatus = 0;

	TOCMerge *mergeToc = new TOCMerge();
	make_dirs(cSubPath);
	mergeStatus = mergeToc->set_data_update_version(cSubPath, volDisc, volXUser, volUser);
	delete mergeToc;

	env->ReleaseStringUTFChars(subPath, cSubPath);

	return mergeStatus;
}

jint Java_vn_com_sonca_smartkaraoke_SmartKaraoke_mergeData(JNIEnv* env, jobject thiz, jstring mainPath, jstring updatePath, jint type)
{
	const char *cMainPath = env->GetStringUTFChars(mainPath, NULL);
	const char *cUpdatePath = env->GetStringUTFChars(updatePath, NULL);
	char *orgfile = NULL;

	int tmpSize = strlen(cMainPath) + 10;
	int mergeStatus = 0;
	char *idxDest = (char *) malloc(tmpSize);
	memset(idxDest, 0 ,tmpSize);
	sprintf(idxDest, "%s/MEGIDX", cMainPath);

	tmpSize = strlen(cUpdatePath) + 10;
	char *uMegIdx = (char *) malloc(tmpSize);
	memset(uMegIdx, 0 ,tmpSize);
	sprintf(uMegIdx, "%s/MEGIDX", cUpdatePath);

	tmpSize = strlen(cMainPath) + 15;
	char *tmpMegIdx = (char *)malloc(tmpSize);
	memset(tmpMegIdx, 0, tmpSize);
	sprintf(tmpMegIdx, "%s/TMP_MEGIDX", cMainPath);

	tmpSize = strlen(cMainPath) + 10;
	char *f10wDest = (char *)malloc(tmpSize);
	memset(f10wDest, 0, tmpSize);
	sprintf(f10wDest, "%s/FIRST10W", cMainPath);

	tmpSize = strlen(cUpdatePath) + 10;
	char *uLyric = (char *)malloc(tmpSize);
	memset(uLyric, 0, tmpSize);
	sprintf(uLyric, "%s/FIRST10W", cUpdatePath);

	tmpSize = strlen(cMainPath) + 15;
	char *tmpLyric = (char *)malloc(tmpSize);
	memset(tmpLyric, 0, tmpSize);
	sprintf(tmpLyric, "%s/TMP_FIRST10W", cMainPath);

	TOCMerge *mergeToc = new TOCMerge();
	int status = 0;

	make_dirs(cMainPath);

	mergeStatus = mergeToc->merge_toc(idxDest, uMegIdx, tmpMegIdx, type);
	if(mergeStatus != 0) {
		printf("Merge TOC Failed: %d", mergeStatus);
		goto exit_merge;
	}

	mergeStatus = mergeToc->merge_lyric_file(f10wDest, uLyric, tmpLyric);
	if(mergeStatus != 0) {
		printf("Merge Lyric Failed: %d", mergeStatus);
		goto exit_merge;
	}

	tmpSize = strlen(cMainPath) + 15;
	orgfile = (char *)malloc(tmpSize);
	memset(orgfile, 0, tmpSize);
	sprintf(orgfile, "%s/FIRST10W_ORG", cMainPath);
	rename(f10wDest, orgfile);

	memset(orgfile, 0, tmpSize);
	sprintf(orgfile, "%s/MEGIDX_ORG", cMainPath);
	rename(idxDest, orgfile);
	free(orgfile);

	status = rename(tmpMegIdx, idxDest);
	if(status!=0) {
		printf("rename file MEGIDX Failed");
		goto exit_merge;
	}

	status = rename(tmpLyric, f10wDest);
	if(status!=0) {
		printf("rename file FIRST10W Failed");
		goto exit_merge;
	}

	exit_merge:

	delete mergeToc;
	free(idxDest);
	free(uMegIdx);
	free(tmpMegIdx);
	free(f10wDest);
	free(uLyric);
	free(tmpLyric);

	env->ReleaseStringUTFChars(mainPath, cMainPath);
	env->ReleaseStringUTFChars(updatePath, cUpdatePath);

	return mergeStatus;
}

//- (BOOL) extractPictureFile:(NSString *) singerPath savePath:(NSString *)picDir
jboolean Java_vn_com_sonca_smartkaraoke_SmartKaraoke_extractPictureFile(JNIEnv* env, jobject thiz, jstring savePath, jstring picDir)
{
	FILE *singerFP = NULL;
	char *picHeader;
	const char *cPicDir = NULL;
	uint32_t picCount = 0;

	cPicDir = env->GetStringUTFChars(picDir, NULL);
	printf("save Pic dir: %s\n", cPicDir);


	const char * cSingerPath = env->GetStringUTFChars(savePath, NULL);
	printf("Singer Pic file path: %s\n", cSingerPath);

	singerFP = fopen(cSingerPath, "rb");
	if (singerFP == NULL) {
		printf("Could not open singer file\n");
		env->ReleaseStringUTFChars(picDir, cPicDir);
		env->ReleaseStringUTFChars(savePath, cSingerPath);
		return false;
	}

	picHeader = (char*)malloc(8);
	int byteRead = fread(picHeader, 1, 8, singerFP);
	int strCmp = memcmp(picHeader, "ART", 3);
	if(byteRead < 8 || strCmp != 0)
	{
		printf("Invalid pic file");
		free(picHeader);
		env->ReleaseStringUTFChars(picDir, cPicDir);
		env->ReleaseStringUTFChars(savePath, cSingerPath);
		return false;
	}
	picCount = 0;

	picCount |= (((uint32_t)(picHeader[4]) << 0) & 0x000000FF);
	picCount |= (((uint32_t)(picHeader[5]) << 8) & 0x0000FF00);
	picCount |= (((uint32_t)(picHeader[6]) << 16) & 0x00FF0000);
	picCount |= (((uint32_t)(picHeader[7]) << 24) & 0xFF000000);

	picHeader = (char*)realloc(picHeader, picCount);
	fseek(singerFP, 8, SEEK_SET);
	if(fread(picHeader, 1, picCount, singerFP) < 8)
	{
		printf("Invalid pic file");
		free(picHeader);
		env->ReleaseStringUTFChars(picDir, cPicDir);
		env->ReleaseStringUTFChars(savePath, cSingerPath);
		return false;
	}

	for(int i = 0; i < (picCount / 8); i++)
	{
		char *tmpPtr = (char *)(picHeader + i*8);
		uint16_t singerIdx = 0;
		singerIdx |= (((uint32_t)(*tmpPtr++) << 0) & 0x00FF);
		singerIdx |= (((uint32_t)(*tmpPtr++) << 8) & 0xFF00);

		uint16_t singerLen = 0;
		singerLen |= (((uint32_t)(*tmpPtr++) << 0) & 0x00FF);
		singerLen |= (((uint32_t)(*tmpPtr++) << 8) & 0xFF00);

		uint32_t singerOffset = 0;
		singerOffset |= (((uint32_t)(*tmpPtr++) << 0) & 0x000000FF);
		singerOffset |= (((uint32_t)(*tmpPtr++) << 8) & 0x0000FF00);
		singerOffset |= (((uint32_t)(*tmpPtr++) << 16) & 0x00FF0000);
		singerOffset |= (((uint32_t)(*tmpPtr++) << 24) & 0xFF000000);
		singerOffset += (8 + picCount);

		char path[256];
		sprintf(path, "%s/%d", cPicDir, singerIdx);
		//        printf("PicFilePath: %s\n", path);
		FILE *picFile = fopen(path, "wb");
		if (picFile != NULL) {
			const int BLOCKSIZE = 1024;
			int len;
			void *block = malloc(BLOCKSIZE);

			fseek(singerFP, singerOffset, SEEK_SET);
			int remainByte = singerLen;
			int offset = 0;
			do {
				len = fread(block, 1, remainByte > BLOCKSIZE ? BLOCKSIZE : remainByte, singerFP);
				if(fwrite(block, 1, len, picFile) == 0)
				{
					printf("Error writing to blob handle. Offset %i, len %i\n", offset, len);
					break;
				}
				offset+=len;
				remainByte -= len;
			}while (remainByte > 0);

			fclose(picFile);
			//                printf("Successfully wrote to file\n");
			free(block);
		}
	}
	free(picHeader);
	env->ReleaseStringUTFChars(picDir, cPicDir);
	env->ReleaseStringUTFChars(savePath, cSingerPath);
	return true;
}


bool containSongID(uint32_t * idxs, uint32_t len, uint32_t anID)
{
	if (idxs == NULL || len == 0) {
		return false;
	}
	if ((anID > idxs[len - 1]) && (anID < idxs[0])) {
		return false;
	}

	uint32_t *idxPtr = idxs;
	int low = 0;
	int high = len - 1;
	int mid = 0;
	while (low <= high) {
		mid = low + ((high - low) >> 1);
		if (*(idxPtr + mid) > anID) {
			high = mid - 1;
		}else if(*(idxPtr + mid) < anID){
			low = mid + 1;
		}else {
			return true;
		}
	}
	return false;
}

//- (BOOL) importSongByModel:(NSInteger)model idList:(NSString *)idPath
jboolean Java_vn_com_sonca_smartkaraoke_SmartKaraoke_importSongModel(JNIEnv* env, jobject thiz, jstring savePath, jint model, jintArray idList, jint size)
{
	sqlite::sqlite *db;
	const char *sql;
	sqlite3_stmt *sqlStmt = NULL;
	int result;
	bool importStatus = false;
	jint *idArray = NULL;

	const char * cDbPath = env->GetStringUTFChars(savePath, NULL);
	FILE *dbfp = fopen(cDbPath, "rb");
	if (dbfp == NULL) {
		printf("Unable to open database file\n");
		return false;
	}
	fclose(dbfp);
	printf("Database path: %s\n", cDbPath);

	//    const char *idListPath = env->GetStringUTFChars(idList, NULL);
	//    FILE *cIDListPath = fopen(idListPath, "rb");
	//    if (cIDListPath == NULL) {
	//        printf("Unable to open database file\n");
	//        return false;
	//    }
	//    fclose(cIDListPath);
	//    printf("IDList path: %s\n", idListPath);
	//    jarray *idList = [[NSString stringWithContentsOfFile:idPath encoding:NSUTF8StringEncoding error:nil] componentsSeparatedByString:@","];

	idArray = env->GetIntArrayElements(idList, NULL);


	printf("Import SONG-MODEL To Database\n");
	// Open database
	db = new sqlite::sqlite(cDbPath);

	char updateSql[256];
	memset(updateSql, 0, sizeof(updateSql));
	sprintf(updateSql, "delete from ZSONGMODEL where ZMODEL='%d'", model);
	db->executeUpdate((const char *)updateSql);

	db->beginTransaction();
	sql = "insert or replace into ZSONGMODEL(ZSO_MD,ZMODEL) values (?,?)";

	sqlStmt = NULL;
	result = sqlite3_prepare_v2(db->sqliteHandle(), sql, (int)strlen(sql), &sqlStmt, NULL);
	if (result != SQLITE_OK){
		printf("\nError for sqlCmd = [%s]\n", sql);
		importStatus = false;
		goto RELEASE_DB;
	}

	for (int i=0; i < size; i++) {
		// Insert primary key
		sqlite3_bind_int(sqlStmt, 1, idArray[i]);
		sqlite3_bind_int(sqlStmt, 2, model);

		result = 0;
		if ((result = sqlite3_step(sqlStmt)) != SQLITE_DONE){
			printf("\nError for sql = [%s]", "UpdateSortInfoWithOnConfict");
			printf("\nError for sql = [%s]\n", sqlite3_errmsg(db->sqliteHandle()));
			db->rollback();
			sqlite3_finalize(sqlStmt);
			importStatus = false;
			goto RELEASE_DB;
		}
		sqlite3_clear_bindings(sqlStmt);
		sqlite3_reset(sqlStmt);
	}

	RELEASE_DB:
	sqlite3_finalize(sqlStmt);
	db->commit();
	importStatus = true;

	env->ReleaseStringUTFChars(savePath, cDbPath);
	env->ReleaseIntArrayElements(idList, idArray, 0);

	return importStatus;
}

//jboolean Java_vn_com_sonca_smartkaraoke_SmartKaraoke_importTocToDatabase(JNIEnv* env, jobject thiz, jstring dbPath, jstring tocDir, jstring picDirjstring savePath, jstring singerPath, jstring lyricPath, jstring dbPath)
jboolean Java_vn_com_sonca_smartkaraoke_SmartKaraoke_importTocToDatabase(JNIEnv* env, jobject thiz, jstring dbPath, jstring idxPath, jstring lyricPath, jstring cprightPath)
{
	bool importStatus = false;
	size_t fileLength = 0;
	uint8_t *pBuf;
	KarToc *toc;
	char status;
	sqlite::sqlite *db;
	const char *sql;
	const char *sqlAuthor;
	const char *sqlPicture;
	sqlite3_stmt *sqlStmt = NULL;
	sqlite3_stmt *sqlStmtAuthor = NULL;
	sqlite3_stmt *sqlStmtPicture = NULL;
	int result;
	StringUtils *strUtils;
	int tocVersion;
	int tocVersion1;
	int tocVersion2;
	int tocVersion3;
	uint32_t *idSonca = NULL;
	uint32_t idCount = 0;

#ifdef EXTRACT_AUDIO
	NSInteger extractID = 17111;
	NSInteger extractTypeABC = SONG_TYPE_NONE;
	NSString *sourceDir = [FileManager createDirectoryInDocuments:@"TEST_DIR"];
	NSString *mp3Path = [sourceDir stringByAppendingPathComponent:@"MEGVOL"];
	FILE * mp3FilePath = fopen([mp3Path UTF8String], "rb");
	NSString *midiPath = [sourceDir stringByAppendingPathComponent:@"MEGMID"];
	FILE * midiFilePath = fopen([midiPath UTF8String], "rb");

	NSString *saveFolder = [sourceDir stringByAppendingPathComponent:@"ouput"];
	NSFileManager *fileManager = [NSFileManager defaultManager];
	if ([fileManager fileExistsAtPath:saveFolder]) {
		[fileManager removeItemAtPath:saveFolder error:nil];
	}
	[fileManager createDirectoryAtPath:saveFolder withIntermediateDirectories:NO attributes:nil error:nil];

	NSString *anMIDFile = [saveFolder stringByAppendingPathComponent:[NSString stringWithFormat:@"%d%06d.mid", extractTypeABC, extractID]];
	NSString *anMP3File = [saveFolder stringByAppendingPathComponent:[NSString stringWithFormat:@"%d%06d.mp3", extractTypeABC, extractID]];
#endif

	const char *cpRightCPath = env->GetStringUTFChars(cprightPath, NULL);
	FILE *cpRightFile = fopen(cpRightCPath, "rb");
	char *contentOfFile;
	if (cpRightFile != NULL) {
		fseek(cpRightFile, 0, SEEK_END); // seek to end of file
		uint32_t fileLen = ftell(cpRightFile); // get current file pointer
		fseek(cpRightFile, 0, SEEK_SET); // seek back to beginning of file

		contentOfFile = (char *)malloc(fileLen);
		fread(contentOfFile, 1, fileLen, cpRightFile);

		char * pch;
		char *pContentOfFile = contentOfFile;
		pch = strtok (pContentOfFile,",");
		while (pch != NULL)
		{
			idCount++;
			pch = strtok (NULL, ",");
		}
		printf("id count: %d\n", idCount);

		idSonca = (uint32_t *)malloc(idCount*(sizeof(uint32_t)));
		idCount = 0;
		uint32_t *pIdSonca = idSonca;

		fseek(cpRightFile, 0, SEEK_SET); // seek back to beginning of file
		fread(contentOfFile, 1, fileLen, cpRightFile);
		pContentOfFile = contentOfFile;
		pch = strtok (pContentOfFile,",");
		while (pch != NULL)
		{
			uint32_t idx = atol(pch);
			if (idx > 0) {
				*pIdSonca++ = idx;
				idCount++;
			}
			pch = strtok (NULL, ",");
		}

		free(contentOfFile);
		fclose(cpRightFile);
	}else {
		printf("Unable to open Copy Right Path\n");
	}
	printf("Copy right path: %s\n", cpRightCPath);

	const char * cDbPath = env->GetStringUTFChars(dbPath, NULL);
	FILE *dbfp = fopen(cDbPath, "rb");
	if (dbfp == NULL) {
		printf("Unable to open database file\n");
		env->ReleaseStringUTFChars(cprightPath, cpRightCPath);
		env->ReleaseStringUTFChars(dbPath, cDbPath);
		return false;
	}
	fclose(dbfp);
	printf("Database path: %s\n", cDbPath);

	const char* cLyricPath = env->GetStringUTFChars(lyricPath, NULL);
	FILE *lyricfp = fopen(cLyricPath, "rb");
	if (lyricfp == NULL) {
		printf("Unable to open lyric file\n");
		env->ReleaseStringUTFChars(cprightPath, cpRightCPath);
		env->ReleaseStringUTFChars(dbPath, cDbPath);
		env->ReleaseStringUTFChars(lyricPath, cLyricPath);
		return false;
	}
	fclose(lyricfp);
	printf("Lyric file path: %s\n", cLyricPath);

	const char * cIdxPath = env->GetStringUTFChars(idxPath, NULL);
	FILE *fp = fopen(cIdxPath, "rb");
	if (fp == NULL) {
		printf("Unable to open file update\n");
		env->ReleaseStringUTFChars(cprightPath, cpRightCPath);
		env->ReleaseStringUTFChars(dbPath, cDbPath);
		env->ReleaseStringUTFChars(lyricPath, cLyricPath);
		env->ReleaseStringUTFChars(idxPath, cIdxPath);
		return false;
	}
	printf("Update path: %s\n", cIdxPath);
	fseek(fp, 0, SEEK_END); // seek to end of file
	fileLength = ftell(fp); // get current file pointer
	fseek(fp, 0, SEEK_SET); // seek back to beginning of file

	// proceed with allocating memory and reading the file
	pBuf = (uint8_t *) malloc(fileLength);
	if(pBuf == NULL)
	{
		printf("Unable to alloc memory\n");
		fclose(fp);
		importStatus = false;
		goto FINAL;
	}

	fread(pBuf, 1, fileLength, fp);
	toc = new KarToc();
	status = toc->parse_data((const char *) pBuf, (uint32_t)fileLength);
	printf("Parse toc Completed, status: %d\n", status);

	tocVersion = toc->getVersion();
	tocVersion1 = toc->getVersion1();
	tocVersion2 = toc->getVersion2();
	tocVersion3 = toc->getVersion3();
	printf("Version: %d-%d-%d-%d\n", tocVersion,tocVersion1,tocVersion2,tocVersion3);

	fclose(fp);
	free(pBuf);

	if(status != 0)
	{
		printf("Toc parse failed\n");
		importStatus = false;
		goto FINAL;
	}

	//    status = toc->parseLyricToc(cLyricPath);
	status = toc->parseLyric10Words(cLyricPath);
	if(status != 0)
	{
		printf("Parse Lyric Failed. Return\n");
		importStatus = false;
		goto FINAL;
	}

	printf("Import Toc To Database\n");
	// Open database
	db = new sqlite::sqlite(cDbPath);

	printf("Import Song Type\n");
	db->executeUpdate("delete from ZSONGTYPES");
	db->beginTransaction();
	sql = "insert or replace into ZSONGTYPES(Z_PK,ZNAME,ZSORT) values (?,?,?)";

	sqlStmt = NULL;
	result = sqlite3_prepare_v2(db->sqliteHandle(), sql, (int)strlen(sql), &sqlStmt, NULL);
	if (result != SQLITE_OK){
		printf("\nError for sqlCmd = [%s]\n", sql);
		importStatus = false;
		goto RELEASE_DB;
	}
	for(int i = 0; i < 14; i++) {
		const char* pTypeName = NULL;
		switch (i) {
		case INVALID:
			pTypeName = "Đang Cập Nhật";
			break;

		case TRE_TRUNG:
			pTypeName = "Trẻ Trung";
			break;

		case THIEU_NHI:
			pTypeName = "Thiếu Nhi";
			break;

		case TIEN_CHIEN:
			pTypeName = "Tiền Chiến";
			break;

		case DAN_CA:
			pTypeName = "Dân Ca";
			break;

		case TINH_CA:
			pTypeName = "Tình Ca";
			break;

		case AM_HUONG_DAN_CA:
			pTypeName = "Âm Hưởng Dân Ca";
			break;

		case TRUYEN_THONG:
			pTypeName = "Nhạc Đỏ";
			break;

		case BOLERO:
			pTypeName = "Bolero";
			break;

		case TRINH_CONG_SON:
			pTypeName = "Trịnh Công Sơn";
			break;

		case NHACHOA_LOIVIET:
			pTypeName = "Nhạc Hoa Lời Việt";
			break;

		case NHACANH_LOIVIET:
			pTypeName = "Nhạc Anh Lời Việt";
			break;

		case NHACPHAP_LOIVIET:
			pTypeName = "Nhạc Pháp Lời Việt";
			break;

		case TANCO_GIAODUYEN:
			pTypeName = "Tân Cổ Giao Duyên";
			break;

		default:
			break;
		}

		sqlite3_bind_int(sqlStmt, 1, i);
		sqlite3_bind_text(sqlStmt, 2, pTypeName, -1, SQLITE_STATIC);
		sqlite3_bind_int(sqlStmt, 3, i);

		result = 0;
		if ((result = sqlite3_step(sqlStmt)) != SQLITE_DONE){
			printf("\nError for sql = [%s]", "UpdateSortInfoWithOnConfict");
			printf("\nError for sql = [%s]\n", sqlite3_errmsg(db->sqliteHandle()));
			db->rollback();
			sqlite3_finalize(sqlStmt);
			importStatus = false;
			goto RELEASE_DB;
		}
		sqlite3_clear_bindings(sqlStmt);
		sqlite3_reset(sqlStmt);
	}

	sqlite3_finalize(sqlStmt);
	db->commit();

	// Insert Language Table
	printf("Import Language\n");
	db->executeUpdate("delete from ZLANGUAGES");
	db->beginTransaction();
	sql = "insert or replace into ZLANGUAGES(Z_PK,ZNAME,ZSORT) values (?,?,?)";

	sqlStmt = NULL;
	result = sqlite3_prepare_v2(db->sqliteHandle(), sql, (int)strlen(sql), &sqlStmt, NULL);
	if (result != SQLITE_OK){
		printf("\nError for sqlCmd = [%s]\n", sql);
		importStatus = false;
		goto RELEASE_DB;
	}
	for(int i = 0; i < 7; i++) {
		const char* pTypeName = NULL;
		switch (i) {
		case VIETNAMESE:
			pTypeName = "Nhạc Việt";
			break;

		case ENGLISH:
			pTypeName = "Nhạc Anh";
			break;

		case FRENCH:
			pTypeName = "Nhạc Pháp";
			break;

		case CHINESE:
			pTypeName = "Nhạc Hoa";
			break;

		case PHILIPINSE:
			pTypeName = "Nhạc Philippines";
			break;

		case KOREAN:
			pTypeName = "Nhạc Hàn";
			break;

		case JAPANESE:
			pTypeName = "Nhạc Nhật";
			break;

		default:

			break;
		}

		sqlite3_bind_int(sqlStmt, 1, i);
		sqlite3_bind_text(sqlStmt, 2, pTypeName, -1, SQLITE_STATIC);
		sqlite3_bind_int(sqlStmt, 3, i);

		result = 0;
		if ((result = sqlite3_step(sqlStmt)) != SQLITE_DONE){
			printf("\nError for sql = [%s]", "UpdateSortInfoWithOnConfict");
			db->rollback();
			sqlite3_finalize(sqlStmt);
			importStatus = false;
			goto RELEASE_DB;
		}
		sqlite3_clear_bindings(sqlStmt);
		sqlite3_reset(sqlStmt);
	}

	sqlite3_bind_int(sqlStmt, 1, LANG_OTHER);
	sqlite3_bind_text(sqlStmt, 2, "Ngôn Ngữ Khác", -1, SQLITE_STATIC);
	sqlite3_bind_int(sqlStmt, 3, LANG_OTHER);

	result = 0;
	if ((result = sqlite3_step(sqlStmt)) != SQLITE_DONE){
		printf("\nError for sql = [%s]", "UpdateSortInfoWithOnConfict");
		db->rollback();
		sqlite3_finalize(sqlStmt);
		importStatus = false;
		goto RELEASE_DB;
	}
	sqlite3_clear_bindings(sqlStmt);
	sqlite3_reset(sqlStmt);

	sqlite3_finalize(sqlStmt);
	db->commit();

	/*
	printf("Insert Song Lyric\n");
	// Insert Song Lyric
	printf("Delete all data from zvlyrics\n");
//	db->executeUpdate("DROP TABLE ZVLYRICS;");
	db->executeUpdate("CREATE VIRTUAL TABLE IF NOT EXISTS ZVLYRICS USING fts3(zid INTEGER PRIMARY UNIQUE, zpinyin TEXT, ztext TEXT, tokenize=porter);");
//    db->beginTransaction();
    db->executeUpdate("delete from ZVLYRICS");
//    db->commit();
	printf("Lyrics Songs Count: %d\n", toc->getLyricInfoCount());
	db->beginTransaction();
	sql = "insert into ZVLYRICS(ZID,zpinyin,ztext) values (?,?,?)";
	sqlStmt = NULL;
	result = sqlite3_prepare_v2(db->sqliteHandle(), sql, strlen(sql), &sqlStmt, NULL);
	if (result != SQLITE_OK){
		printf("\nError for sqlCmd = [%s]\n", sql);
		delete toc;
		delete db;
		return false;
	}
	for (int i = 0; i<toc->getLyricInfoCount(); i++) {
		Lyric_Info_S *lyrInfo = toc->getLyricInfo(i);

		if(lyrInfo == NULL){
			printf("null obj\n");
			continue;
		}

		sqlite3_bind_int(sqlStmt, 1, lyrInfo->songCode5);
		sqlite3_bind_text(sqlStmt, 2, toc->offsetToPointer(toc->getLyricPinyinTable(), lyrInfo->pLyricPinyinOffset), -1, SQLITE_STATIC);
		sqlite3_bind_text(sqlStmt, 3, toc->offsetToPointer(toc->getLyricTextTable(), lyrInfo->pLyricTextOffset), -1, SQLITE_STATIC);

		result = 0;
		if ((result = sqlite3_step(sqlStmt)) != SQLITE_DONE){
			printf("\nError for sql = [%s]", "UpdateSortInfoWithOnConfict");
			db->rollback();
			delete toc;
			delete db;
			return false;
		}

		sqlite3_clear_bindings(sqlStmt);
		sqlite3_reset(sqlStmt);
	}
	sqlite3_finalize(sqlStmt);
	db->commit();
	 */
	strUtils = new StringUtils();

	// Insert singer, Author
	printf("Delete all table\n");
	db->executeUpdate("delete from ZSONGS");
	db->executeUpdate("delete from ZSONGSINGERS");
	db->executeUpdate("delete from ZSONGMUSICIANS");
	db->executeUpdate("delete from ZSINGERS");
	db->executeUpdate("delete from ZMUSICIANS");

	printf("Singer count: %d\n", toc->getSingerCount());
	db->beginTransaction();

	sql = "insert or replace into ZSINGERS(Z_PK,ZNAME,ZSHORTNAME,ZTITLERAW,zcount,zsort,ZCID,ZLANGID) values (?,?,?,?,?,?,?,?)";
	sqlAuthor = "insert or replace into ZMUSICIANS(Z_PK,ZNAME,ZSHORTNAME,ZTITLERAW,zcount,zsort,ZCID,ZLANGID) values (?,?,?,?,?,?,?,?)";

	sqlStmt = NULL;
	sqlStmtAuthor = NULL;

	result = sqlite3_prepare_v2(db->sqliteHandle(), sql, (int)strlen(sql), &sqlStmt, NULL);
	if (result != SQLITE_OK){
		printf("\nError for sqlCmd = [%s]\n", sql);
		importStatus = false;
		goto RELEASE_STRING;
	}

	result = sqlite3_prepare_v2(db->sqliteHandle(), sqlAuthor, (int)strlen(sqlAuthor), &sqlStmtAuthor, NULL);
	if (result != SQLITE_OK){
		printf("\nError for sqlCmd = [%s]\n", sqlAuthor);
		importStatus = false;
		goto RELEASE_STRING;
	}

#ifdef TOC_VER_1

	for (int i = 0; i<toc->getSingerCount(); i++) {
		Person_Info_S * anObj = toc->getSingerInfo(i);
		if(!anObj)
		{
			printf("null obj\n");
			continue;
		}

		int lang_id = anObj->num_songs.language_type;
		if (lang_id != VIETNAMESE && lang_id != ENGLISH && lang_id != CHINESE && lang_id != PHILIPINSE && lang_id != KOREAN && lang_id != JAPANESE) {
			lang_id = LANG_OTHER;
			anObj->num_songs.language_type = lang_id;
		}

		sqlite3_int64 rowid = 0;

		if(anObj->num_songs.bSinger) {
			sqlite3_bind_int(sqlStmt, 1, i);
			sqlite3_bind_text(sqlStmt, 2, toc->offsetToPointer(anObj->pNameOffset), -1, SQLITE_STATIC);
			//		NSString *titleRaw = [charProcess getTitleRaw:[anObj name]];
			sqlite3_bind_text(sqlStmt, 3, toc->offsetToPointer(anObj->pPYOffset), -1, SQLITE_STATIC);

#if 1
			string titleRaw("");

			if(anObj->num_songs.language_type == CHINESE) {
#ifdef __OBJC__
				NSString *hanzi = [Hanzi2Pinyin convert:[NSString stringWithUTF8String:toc->offsetToPointer(anObj->pNameOffset)]];
				titleRaw = string([hanzi UTF8String]);
#endif
			}else {
				titleRaw = strUtils->getTitleRaw(string(toc->offsetToPointer(anObj->pNameOffset)));
			}
			//		printf("%s\n", titleRaw.c_str());
			sqlite3_bind_text(sqlStmt, 4, titleRaw.c_str(), -1, SQLITE_STATIC);
#else
			sqlite3_bind_text(sqlStmt, 4, strUtils->getTitleRaw(string(toc->offsetToPointer(anObj->pSingerName.offset))).c_str(), -1, SQLITE_STATIC);
#endif

			sqlite3_bind_int(sqlStmt, 5, anObj->num_songs.uSingerSongNums);
			sqlite3_bind_int(sqlStmt, 6, i + 1);

#ifdef TOC_VER_4
			//            sqlite3_bind_int(sqlStmt, 7, anObj->picIdxMobile);
			sqlite3_bind_int(sqlStmt, 7, anObj->singerIdx);
#else
			sqlite3_bind_int(sqlStmt, 7, singerOffset);
#endif

#ifdef TOC_VER_2
			sqlite3_bind_int(sqlStmt, 8, anObj->num_songs.language_type);
#else
			sqlite3_bind_int(sqlStmt, 8, 0);
#endif

			result = 0;
			if ((result = sqlite3_step(sqlStmt)) != SQLITE_DONE){
				printf("\nError for sql = [%s]", "UpdateSortInfoWithOnConfict step Singer");
				db->rollback();
				importStatus = false;
				goto RELEASE_STRING;
			}
			rowid = sqlite3_last_insert_rowid(db->sqliteHandle());

			sqlite3_clear_bindings(sqlStmt);
			sqlite3_reset(sqlStmt);
		}

		if(anObj->num_songs.bAuthor) {
			sqlite3_bind_int(sqlStmtAuthor, 1, i);
			sqlite3_bind_text(sqlStmtAuthor, 2, toc->offsetToPointer(anObj->pNameOffset), -1, SQLITE_STATIC);
			//		NSString *titleRaw = [charProcess getTitleRaw:[anObj name]];
			sqlite3_bind_text(sqlStmtAuthor, 3, toc->offsetToPointer(anObj->pPYOffset), -1, SQLITE_STATIC);

#if 1
			string titleRaw("");

			if(anObj->num_songs.language_type == CHINESE) {
#ifdef __OBJC__
				NSString *hanzi = [Hanzi2Pinyin convert:[NSString stringWithUTF8String:toc->offsetToPointer(anObj->pNameOffset)]];
				titleRaw = string([hanzi UTF8String]);
#endif
			}else {
				titleRaw = strUtils->getTitleRaw(string(toc->offsetToPointer(anObj->pNameOffset)));
			}
			//		printf("%s\n", titleRaw.c_str());
			sqlite3_bind_text(sqlStmtAuthor, 4, titleRaw.c_str(), -1, SQLITE_STATIC);
#else
			sqlite3_bind_text(sqlStmtAuthor, 4, strUtils->getTitleRaw(string(toc->offsetToPointer(anObj->pSingerName.offset))).c_str(), -1, SQLITE_STATIC);
#endif

			sqlite3_bind_int(sqlStmtAuthor, 5, anObj->num_songs.uAuthorSongNums);

			sqlite3_bind_int(sqlStmtAuthor, 6, i + 1);

#ifdef TOC_VER_4
			//            sqlite3_bind_int(sqlStmtAuthor, 7, anObj->picIdxMobile);
			sqlite3_bind_int(sqlStmtAuthor, 7, anObj->singerIdx);
#else
			sqlite3_bind_int(sqlStmtAuthor, 7, singerOffset);
#endif

#ifdef TOC_VER_2
			sqlite3_bind_int(sqlStmtAuthor, 8, anObj->num_songs.language_type);
#else
			sqlite3_bind_int(sqlStmtAuthor, 8, 0);
#endif

			result = 0;
			if ((result = sqlite3_step(sqlStmtAuthor)) != SQLITE_DONE){
				printf("\nError for sql = [%s]", "UpdateSortInfoWithOnConfict author");
				db->rollback();
				importStatus = false;
				goto RELEASE_STRING;
			}
			rowid = sqlite3_last_insert_rowid(db->sqliteHandle());

			sqlite3_clear_bindings(sqlStmtAuthor);
			sqlite3_reset(sqlStmtAuthor);
		}
	}
#else
	for (int i = 0; i<toc->getSingerCount(); i++) {
		Singer_Info_S * anObj = toc->getSingerInfo(i);
		if(!anObj)
		{
			printf("null obj\n");
			continue;
		}

		sqlite3_bind_int(sqlStmt, 1, i);

		int buf_len = 1024;
		char *tempBuf = (char *)malloc(buf_len);
		char *pbuff = tempBuf;
		char *pString;
		uint16_t name_len = 0;

		// Song name
		pbuff = tempBuf;
		bzero(tempBuf, buf_len);
		pString = toc->offsetToPointer(anObj->pSingerName.offset);
		name_len = anObj->pSingerName.len;

		//#ifdef ENCODING_TCVN
		//        if(anObj->property.language_type == 2){
		//            convertGB2312ToUTF8((const char **)&pString, (const char *)(pString + name_len), &pbuff, (char *)(pbuff + buf_len), true);
		//        }else {
		convertTCVN3ToUTF8((const char **)&pString, (const char *)(pString + name_len), &pbuff, (char *)(pbuff + buf_len), true);
		//        }
		//#else
		//        memcpy(tempBuf, pString, name_len);
		//#endif



		//		sqlite3_bind_text(sqlStmt, 2, toc->offsetToPointer(anObj->pSingerName.offset), -1, SQLITE_STATIC);
		sqlite3_bind_text(sqlStmt, 2, tempBuf, -1, SQLITE_STATIC);
		//		NSString *titleRaw = [charProcess getTitleRaw:[anObj name]];
		sqlite3_bind_text(sqlStmt, 3, toc->offsetToPointer(anObj->pPYStr.offset), -1, SQLITE_STATIC);

#if 1
		string titleRaw("");
		titleRaw = strUtils->getTitleRaw(string(toc->offsetToPointer(anObj->pSingerName.offset)));
		//		printf("%s\n", titleRaw.c_str());

		sqlite3_bind_text(sqlStmt, 4, titleRaw.c_str(), -1, SQLITE_STATIC);
#else
		sqlite3_bind_text(sqlStmt, 4, strUtils->getTitleRaw(string(toc->offsetToPointer(anObj->pSingerName.offset))).c_str(), -1, SQLITE_STATIC);
#endif

		sqlite3_bind_int(sqlStmt, 5, anObj->uSongNums);
		sqlite3_bind_int(sqlStmt, 6, i + 1);
		sqlite3_bind_blob(sqlStmt, 7, NULL, 1, NULL);

		result = 0;
		if ((result = sqlite3_step(sqlStmt)) != SQLITE_DONE){
			printf("\nError for sql = [%s]", "UpdateSortInfoWithOnConfictdg ");
			db->rollback();
			importStatus = false;
			goto RELEASE_DB;
		}
		free(tempBuf);
		sqlite3_clear_bindings(sqlStmt);
		sqlite3_reset(sqlStmt);

	}
#endif
	sqlite3_finalize(sqlStmt);
	sqlite3_finalize(sqlStmtAuthor);
	db->commit();

	// Insert Song
	printf("Song count: %d\n", toc->getSongCount());
	db->beginTransaction();

#ifdef TOC_VER_4
	sql = "insert or replace into ZSONGS(z_pk,ZID,zindex5,ZTYPE,ZREMIX,ZNEWSONG,ZNUMWORDS,ZSHOW,ZONETOUCH,ZTYPEID,ZLANGUAGEID,ZSORT,ZNAME,ZSHORTNAME,ZTITLERAW,ZLYRIC,ZABC,ZEXTRA,ZSO_MD,ZSCA) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	sqlAuthor = "insert or replace into ZSONGMUSICIANS(ZSO_PK,ZAT_PK) values (?,?)";
	sqlPicture = "insert or replace into ZSONGSINGERS(ZSO_PK,ZAT_PK) values (?,?)";
#elif def TOC_VER_3
	sql = "insert or replace into ZSONGS(ZID,zindex5,ZTYPE,ZREMIX,ZNEWSONG,ZNUMWORDS,ZSHOW,ZONETOUCH,ZTYPEID,ZLANGUAGEID,ZSORT,ZNAME,ZSHORTNAME,ZTITLERAW,ZLYRIC) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	sqlAuthor = "insert or replace into ZSONGMUSICIANS(ZSO_PK,ZAT_PK) values (?,?)";
	sqlPicture = "insert or replace into ZSONGSINGERS(ZSO_PK,ZAT_PK) values (?,?)";
#else
	sql = "insert or replace into ZSONGS(ZID,zindex5,ZTYPE,ZREMIX,ZNEWSONG,ZNUMWORDS,ZSHOW,ZONETOUCH,ZMUSICIANID,ZSINGERID,ZTYPEID,ZLANGUAGEID,ZSORT,ZNAME,ZSHORTNAME,ZTITLERAW,ZLYRIC) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
#endif
	sqlStmt = NULL;
	result = sqlite3_prepare_v2(db->sqliteHandle(), sql, (int)strlen(sql), &sqlStmt, NULL);
	if (result != SQLITE_OK){
		printf("\nError for sqlCmd = [%s]\n", sql);
		importStatus = false;
		goto RELEASE_DB;
	}

#ifdef TOC_VER_3
	sqlStmtAuthor = NULL;
	result = sqlite3_prepare_v2(db->sqliteHandle(), sqlAuthor, (int)strlen(sqlAuthor), &sqlStmtAuthor, NULL);
	if (result != SQLITE_OK){
		printf("\nError for sqlCmd = [%s]\n", sqlAuthor);
		importStatus = false;
		goto RELEASE_DB;
	}

	sqlStmtPicture = NULL;
	result = sqlite3_prepare_v2(db->sqliteHandle(), sqlPicture, (int)strlen(sqlPicture), &sqlStmtPicture, NULL);
	if (result != SQLITE_OK){
		printf("\nError for sqlCmd = [%s]\n", sqlPicture);
		importStatus = false;
		goto RELEASE_DB;
	}
#endif

	for (int i = 0; i<toc->getSongCount(); i++) {
		Song_Info_S *aSong = toc->getSongInfo(i);
		if(!aSong)
		{
			printf("null obj\n");
			continue;
		}

		int lang_id = aSong->property.language_type;
		if (lang_id != VIETNAMESE && lang_id != ENGLISH && lang_id != PHILIPINSE && lang_id != CHINESE && lang_id != KOREAN && lang_id != JAPANESE) {
			lang_id = LANG_OTHER;
			aSong->property.language_type = lang_id;
		}

		int bindColumn = 1;
#ifdef TOC_VER_1
#ifdef TOC_VER_4
		sqlite3_bind_int(sqlStmt, bindColumn++, i);
#endif
		sqlite3_bind_int(sqlStmt, bindColumn++, aSong->song_code6);
		sqlite3_bind_int(sqlStmt, bindColumn++, aSong->song_code5);
		sqlite3_bind_int(sqlStmt, bindColumn++, aSong->property.media_type);
		sqlite3_bind_int(sqlStmt, bindColumn++, aSong->property.is_remix);
		sqlite3_bind_int(sqlStmt, bindColumn++, aSong->property.is_newsong);
		sqlite3_bind_int(sqlStmt, bindColumn++, strlen(toc->offsetToPointer(aSong->pPYNameOffset))/*aSong->property.name_words*/);
		sqlite3_bind_int(sqlStmt, bindColumn++, aSong->property.is_hide);
		sqlite3_bind_int(sqlStmt, bindColumn++, aSong->property.onetouch_type);

#ifndef TOC_VER_3
		sqlite3_bind_int(sqlStmt, bindColumn++, aSong->pPersonIdx.author_idx);
		sqlite3_bind_int(sqlStmt, bindColumn++, aSong->pPersonIdx.singer_idx);
#endif

		sqlite3_bind_int(sqlStmt, bindColumn++, aSong->property.song_type);
		sqlite3_bind_int(sqlStmt, bindColumn++, aSong->property.language_type);

		sqlite3_bind_int(sqlStmt, bindColumn++, i);

		sqlite3_bind_text(sqlStmt, bindColumn++, toc->offsetToPointer(aSong->pSongNameOffset), -1, SQLITE_STATIC);
		sqlite3_bind_text(sqlStmt, bindColumn++, toc->offsetToPointer(aSong->pPYNameOffset), -1, SQLITE_STATIC);

#if 1
		string titleRaw("");

		if(aSong->property.language_type == CHINESE) {
#ifdef __OBJC__
			NSString *hanzi = [Hanzi2Pinyin convert:[NSString stringWithUTF8String:toc->offsetToPointer(aSong->pSongNameOffset)]];
			titleRaw = string([hanzi UTF8String]);
#endif
		}else {
			titleRaw = strUtils->getTitleRaw(string(toc->offsetToPointer(aSong->pSongNameOffset)));
			//		printf("%s\n", titleRaw.c_str());
		}
		sqlite3_bind_text(sqlStmt, bindColumn++, titleRaw.c_str(), -1, SQLITE_STATIC);
#else
		sqlite3_bind_text(sqlStmt, bindColumn++, strUtils->getTitleRaw(string(toc->offsetToPointer(aSong->pSongName.offset))).c_str(), -1, SQLITE_STATIC);
#endif

#ifdef TOC_VER_2

		//        LyricWord_Info_S *lyricWordInfo = toc->getFirstWordsLyric(i);
		//		if(lyricWordInfo != NULL){
		//        sqlite3_bind_text(sqlStmt, bindColumn++, toc->offsetToPointer(toc->getLyric10WordsTable(), lyricWordInfo->pLyricTextOffset), -1, SQLITE_STATIC);

		char *lyricPtr = toc->getLyric10Word(aSong->song_code5, aSong->MidiInfo.stFilepos.songABCType);
		if (lyricPtr != NULL) {
			sqlite3_bind_text(sqlStmt, bindColumn++, lyricPtr, -1, SQLITE_STATIC);
		}else {
			sqlite3_bind_text(sqlStmt, bindColumn++, " ", 1, SQLITE_STATIC);
		}
#else
		sqlite3_bind_text(sqlStmt, bindColumn++, " ", 1, SQLITE_STATIC);
#endif

#ifdef TOC_VER_4
		sqlite3_bind_int(sqlStmt, bindColumn++, aSong->MidiInfo.stFilepos.songABCType);

		Song_Info_Extra_S *infoExtra = toc->getSongInfoExtra(i);

		uint32_t extraData = 0;

		if (infoExtra->bNewTocFormat) {
#ifdef REMOVE_CRITICAL_BIT
			infoExtra->bCriticalSong = 0;
#endif
			extraData |= (*((uint8_t *)infoExtra));

			uint8_t *pExtra1 = (uint8_t *)(toc->getSongInfoExtra1(i));
			if(pExtra1 != NULL) {
				//                printf("extra: %d-extra1: %d-id: %d\n", extraData, (*pExtra1), aSong->song_code6);
				extraData |= ((uint32_t)(*pExtra1) << 8);
			}
		}

		sqlite3_bind_int(sqlStmt, bindColumn++, extraData);

		uint32_t songIndex = aSong->song_code6;
		if (aSong->MidiInfo.stFilepos.songABCType != SONG_TYPE_NONE) {
			songIndex = aSong->song_code6 + 1000000*(aSong->MidiInfo.stFilepos.songABCType - 1);
		}
		sqlite3_bind_int(sqlStmt, bindColumn++, songIndex);

		//use soncaid copy right file
		if (idCount == 0 || containSongID(idSonca, idCount, songIndex)) {
			sqlite3_bind_int(sqlStmt, bindColumn++, 0);
		}else {
			sqlite3_bind_int(sqlStmt, bindColumn++, 1);
		}
#endif

#else //END TOC_VER1
		sqlite3_bind_int(sqlStmt, 1, aSong->song_code6);
		sqlite3_bind_int(sqlStmt, 2, aSong->sonca_info.code5);
		sqlite3_bind_int(sqlStmt, 3, aSong->sonca_info.singer_vocal);
		sqlite3_bind_int(sqlStmt, 4, 1);
		sqlite3_bind_int(sqlStmt, 5, 0);
		sqlite3_bind_int(sqlStmt, 6, aSong->property.name_words);
		sqlite3_bind_int(sqlStmt, 7, 0);
		sqlite3_bind_int(sqlStmt, 8, 0);
		sqlite3_bind_int(sqlStmt, 9, 0);
		sqlite3_bind_int(sqlStmt, 10, aSong->property.singer_idx);
		sqlite3_bind_int(sqlStmt, 11, aSong->property.song_type);
		sqlite3_bind_int(sqlStmt, 12, aSong->property.language_type);

		int buf_len = 1024;
		char *tempBuf = (char *)malloc(buf_len);
		char *pbuff = tempBuf;
		char *pString;
		uint16_t name_len = 0;

		// Song name
		pbuff = tempBuf;
		bzero(tempBuf, buf_len);
		pString = toc->offsetToPointer(aSong->pSongName.offset);
		name_len = aSong->pSongName.len;

#ifdef ENCODING_TCVN
		if(aSong->property.language_type == 2){
			convertGB2312ToUTF8((const char **)&pString, (const char *)(pString + name_len), &pbuff, (char *)(pbuff + buf_len), true);
		}else {
			convertTCVN3ToUTF8((const char **)&pString, (const char *)(pString + name_len), &pbuff, (char *)(pbuff + buf_len), true);
		}
#else
		memcpy(tempBuf, pString, name_len);
#endif
		sqlite3_bind_text(sqlStmt, 13, tempBuf, -1, SQLITE_STATIC);
		sqlite3_bind_text(sqlStmt, 14, toc->offsetToPointer(aSong->pPYStr.offset), -1, SQLITE_STATIC);

#if 1
		string titleRaw("");
		titleRaw = strUtils->getTitleRaw(tempBuf);
		//		printf("%s\n", titleRaw.c_str());
		sqlite3_bind_text(sqlStmt, 15, titleRaw.c_str(), -1, SQLITE_STATIC);
#else
		sqlite3_bind_text(sqlStmt, 15, strUtils->getTitleRaw(string(toc->offsetToPointer(aSong->pSongName.offset))).c_str(), -1, SQLITE_STATIC);
#endif
		sqlite3_bind_text(sqlStmt, 16, " ", 1, SQLITE_STATIC);
		sqlite3_bind_text(sqlStmt, 17, " ", 1, SQLITE_STATIC);
		sqlite3_bind_text(sqlStmt, 18, " ", 1, SQLITE_STATIC);
		free(tempBuf);
#endif
		result = 0;
		if ((result = sqlite3_step(sqlStmt)) != SQLITE_DONE){
			printf("\nError for sql = [%s]", "UpdateSortInfoWithOnConfict");
			printf("\nError for sql = [%s] - song id: %d", sqlite3_errmsg(db->sqliteHandle()), aSong->song_code5);
			db->rollback();
			importStatus = false;
			goto RELEASE_STRING;
		}

		uint32_t rowid = (uint32_t)sqlite3_last_insert_rowid(db->sqliteHandle());

		sqlite3_clear_bindings(sqlStmt);
		sqlite3_reset(sqlStmt);


#ifdef TOC_VER_3
		char authorText[25];
		memset(authorText, 0, sizeof(authorText));
		int len = 0;
		for(int j = 0; j < MAX_ARTIST_PER_SONG; j++)
		{
			uint16_t idx = aSong->pPersonIdx.author_idxes[j];
			if (idx == UINT16_MAX) {
				break;
			}

			int charCount = sprintf((char *)(authorText + len), "%05d,",idx);
			len += charCount;

			//            sqlite3_bind_int(sqlStmtAuthor, 1, aSong->song_code6);
			sqlite3_bind_int(sqlStmtAuthor, 1, rowid);
			sqlite3_bind_int(sqlStmtAuthor, 2, idx);
			result = 0;
			if ((result = sqlite3_step(sqlStmtAuthor)) != SQLITE_DONE){
				printf("\nError for sql = [%s]\n", "UpdateSortInfoWithOnConfict12");
				printf("\nError for sql = [%s]\n", sqlite3_errmsg(db->sqliteHandle()));

				db->rollback();
				importStatus = false;
				goto RELEASE_STRING;
			}

			sqlite3_clear_bindings(sqlStmtAuthor);
			sqlite3_reset(sqlStmtAuthor);
		}
		if (len > 0) {
			authorText[len-1] = 0x00;
			len -= 1;
		}
		//        printf("author IDx: %s\n", authorText);
		//        sqlite3_bind_text(sqlStmt, bindColumn++, authorText, len, SQLITE_STATIC);

		char personText[25];
		memset(personText, 0, sizeof(personText));
		len = 0;
		for(int j = 0; j < MAX_ARTIST_PER_SONG; j++)
		{
			uint16_t idx = aSong->pPersonIdx.singer_idxes[j];
			if (idx == UINT16_MAX) {
				break;
			}

			int charCount = sprintf((char *)(personText + len), "%05d,",idx);
			len += charCount;

			//            sqlite3_bind_int(sqlStmtPicture, 1, aSong->song_code6);
			sqlite3_bind_int(sqlStmtPicture, 1, rowid);
			sqlite3_bind_int(sqlStmtPicture, 2, idx);
			result = 0;
			if ((result = sqlite3_step(sqlStmtPicture)) != SQLITE_DONE){
				printf("\nError for sql = [%s] --- id: %d -- value: %d-%d", sqlPicture, aSong->song_code5, rowid, idx);
				printf("\nError for sql = [%s]\n", sqlite3_errmsg(db->sqliteHandle()));
				db->rollback();
				importStatus = false;
				goto RELEASE_STRING;
			}

			sqlite3_clear_bindings(sqlStmtPicture);
			sqlite3_reset(sqlStmtPicture);
		}
		if (len > 0) {
			personText[len-1] = 0x00;
			len -= 1;
		}
		//        printf("Singer IDx: %s\n", personText);
		//        sqlite3_bind_text(sqlStmt, bindColumn++, personText, len, SQLITE_STATIC);
#endif

#ifdef EXTRACT_AUDIO

		extractID = aSong->song_code6;
		extractTypeABC = aSong->MidiInfo.stFilepos.songABCType;

		if (/*aSong->song_code6 == extractID && aSong->MidiInfo.stFilepos.songABCType == extractTypeABC &&*/ (aSong->property.media_type == MP3 || aSong->property.media_type == SINGER)) {
			printf("Extract mp3 audio : %s\n", toc->offsetToPointer(aSong->pSongNameOffset));

			anMP3File = [saveFolder stringByAppendingPathComponent:[NSString stringWithFormat:@"%d%06d.mp3", extractTypeABC, extractID]];
			FILE *audioFP = fopen([anMP3File UTF8String], "wb");
			if (audioFP == NULL) {
				printf("Could not create file at path: %s\n", [anMP3File UTF8String]);
				break;
			}

			uint32_t filesize = aSong->Mp3Info.filesize *2048;
			uint32_t fileOffset = aSong->Mp3Info.stFilepos.fileOffset * 2048;
			const uint32_t buffSize = 2048;
			uint8_t * tempBuff = (uint8_t *)malloc(buffSize);
			uint32_t len = 0;
			int byteRead = 0;

			mp3Path = [sourceDir stringByAppendingPathComponent:[NSString stringWithFormat:@"MEGVOL%@", aSong->Mp3Info.stFilepos.fileIdx != 0 ? [NSString stringWithFormat:@"%d", aSong->Mp3Info.stFilepos.fileIdx] : @""]];
			mp3FilePath = fopen([mp3Path UTF8String], "rb");
			fseek(mp3FilePath, fileOffset, SEEK_SET);
			do {
				byteRead = fread(tempBuff, 1, buffSize, mp3FilePath);
				if(byteRead < 0) {
					break;
				}
				fwrite(tempBuff, 1, byteRead, audioFP);
				len += byteRead;
			}while(byteRead > 0 && len < filesize);
			free(tempBuff);
			fclose(audioFP);

			fclose(mp3FilePath);
		}

		if (/*aSong->song_code6 == extractID && aSong->MidiInfo.stFilepos.songABCType == extractTypeABC && */(aSong->property.media_type == MP3 || aSong->property.media_type == SINGER || aSong->property.media_type == MIDI)) {
			printf("Extract midi audio : %s\n", toc->offsetToPointer(aSong->pSongNameOffset));

			anMIDFile = [saveFolder stringByAppendingPathComponent:[NSString stringWithFormat:@"%d%06d.mid", extractTypeABC, extractID]];
			FILE *audioFP = fopen([anMIDFile UTF8String], "wb");
			if (audioFP == NULL) {
				printf("Could not create file at path: %s\n", [anMIDFile UTF8String]);
				break;
			}

			uint32_t filesize = aSong->MidiInfo.filesize *2048;
#ifdef TOC_VER_4
			uint32_t fileOffset = aSong->MidiInfo.stFilepos.fileOffset * 2048;
#else
			uint32_t fileOffset = aSong->MidiInfo.filepos * 2048;
#endif
			const uint32_t buffSize = 2048;
			uint8_t * tempBuff = (uint8_t *)malloc(buffSize);
			uint32_t len = 0;
			int byteRead = 0;

			midiPath = [sourceDir stringByAppendingPathComponent:[NSString stringWithFormat:@"MEGMID%@", aSong->MidiInfo.stFilepos.fileIdx != 0 ? [NSString stringWithFormat:@"%d", aSong->MidiInfo.stFilepos.fileIdx] : @""]];
			midiFilePath = fopen([midiPath UTF8String], "rb");

			fseek(midiFilePath, fileOffset, SEEK_SET);
			do {
				byteRead = fread(tempBuff, 1, buffSize, midiFilePath);
				if(byteRead < 0) {
					break;
				}
				fwrite(tempBuff, 1, byteRead, audioFP);
				len += byteRead;
			}while(byteRead > 0 && len < filesize);
			free(tempBuff);
			fclose(audioFP);
			fclose(midiFilePath);
		}
#endif

	}

	sqlite3_finalize(sqlStmt);
#ifdef TOC_VER_3
	sqlite3_finalize(sqlStmtAuthor);
	sqlite3_finalize(sqlStmtPicture);
#endif
	db->commit();

	importStatus = true;

	RELEASE_STRING:
	delete strUtils;

	RELEASE_DB:
	delete db;

	FINAL:	
	free(idSonca);
	delete toc;
	//	free(cLyricPath);
	//	free(cPath);
	env->ReleaseStringUTFChars(dbPath, cDbPath);
	env->ReleaseStringUTFChars(cprightPath, cpRightCPath);
	env->ReleaseStringUTFChars(idxPath, cIdxPath);
	env->ReleaseStringUTFChars(lyricPath, cLyricPath);
	//	env->ReleaseStringUTFChars(singerPath, cSingerPath);
	printf("\nParse song completed: result:%d\n", importStatus);
	return importStatus;
}

int deleteSongFromDatabase(const char *cDbPath, Song_Info_Update_S *songInfoUpdate, int songCount) {
	bool importStatus = false;
	sqlite::sqlite *db;
	const char *sql;
	sqlite3_stmt *sqlStmt = NULL;
	int result;

	FILE *dbfp = fopen(cDbPath, "rb");
	if (dbfp == NULL) {
		printf("Unable to open database file\n");
		return false;
	}
	fclose(dbfp);
	printf("Database path: %s\n", cDbPath);

	// Open database
	db = new sqlite::sqlite(cDbPath);

	db->beginTransaction();

	sql = "delete from zsongs where zid=? and zabc=?";
	sqlStmt = NULL;
	result = sqlite3_prepare_v2(db->sqliteHandle(), sql, (int)strlen(sql), &sqlStmt, NULL);
	if (result != SQLITE_OK){
		printf("\nError for sqlCmd = [%s]\n", sql);
		importStatus = false;
		goto RELEASE_STRING;
	}

	for(int i = 0; i < songCount; i++) {
		Song_Info_Update_S *aSong = &songInfoUpdate[i];
		if(!aSong)
		{
			printf("null obj\n");
			continue;
		}

		int bindColumn = 1;
		sqlite3_bind_int(sqlStmt, bindColumn++, aSong->song_code6);
		sqlite3_bind_int(sqlStmt, bindColumn++, aSong->MidiInfo.stFilepos.songABCType);

		result = 0;
		if ((result = sqlite3_step(sqlStmt)) != SQLITE_DONE){
			printf("\nError for sql = [%s] - song id: %d", sqlite3_errmsg(db->sqliteHandle()), aSong->song_code5);
			db->rollback();
			importStatus = false;
			goto RELEASE_STRING;
		}

		sqlite3_clear_bindings(sqlStmt);
		sqlite3_reset(sqlStmt);
	}
	sqlite3_finalize(sqlStmt);
	db->commit();

	importStatus = true;

	RELEASE_STRING:
	RELEASE_DB:
	delete db;

	FINAL:
	printf("\ndelete song completed: result:%d\n", importStatus);
	return importStatus;
}

int insertNewSongToDatabase(const char *cDbPath, Song_Info_Update_S *songInfoUpdate, int songCount, Person_Info_Update_S *artistInfo, int artistCount)
{
	bool importStatus = false;
	sqlite::sqlite *db;
	const char *sql;
	const char *sqlAuthor;
	const char *sqlPicture;
	const char *sqlCount;
	sqlite3_stmt *sqlStmt = NULL;
	sqlite3_stmt *sqlStmtAuthor = NULL;
	sqlite3_stmt *sqlStmtPicture = NULL;
	sqlite3_stmt *sqlStmtCount = NULL;
	int result;
	StringUtils *strUtils;
	uint32_t sortValue = 0;
	int cntSong = 0;

	FILE *dbfp = fopen(cDbPath, "rb");
	if (dbfp == NULL) {
		printf("Unable to open database file\n");
		return false;
	}
	fclose(dbfp);
//	printf("Database path: %s\n", cDbPath);

	strUtils = new StringUtils();

	// Open database
	db = new sqlite::sqlite(cDbPath);

	db->beginTransaction();

	sql = "insert or replace into ZSINGERS(ZNAME,ZSHORTNAME,ZTITLERAW,zcount,ZCID,ZLANGID) values (?,?,?,?,?,?)";
	sqlAuthor = "insert or replace into ZMUSICIANS(ZNAME,ZSHORTNAME,ZTITLERAW,zcount,ZCID,ZLANGID) values (?,?,?,?,?,?)";

	sqlStmt = NULL;
	sqlStmtAuthor = NULL;

	result = sqlite3_prepare_v2(db->sqliteHandle(), sql, (int)strlen(sql), &sqlStmt, NULL);
	if (result != SQLITE_OK){
		printf("\nError for sqlCmd = [%s]\n", sql);
		importStatus = false;
		goto RELEASE_STRING;
	}

	result = sqlite3_prepare_v2(db->sqliteHandle(), sqlAuthor, (int)strlen(sqlAuthor), &sqlStmtAuthor, NULL);
	if (result != SQLITE_OK){
		printf("\nError for sqlCmd = [%s]\n", sqlAuthor);
		importStatus = false;
		goto RELEASE_STRING;
	}

#ifdef TOC_VER_1

	for (int i = 0; i<artistCount; i++) {
		Person_Info_Update_S *anObj = &artistInfo[i];
		if(!anObj)
		{
			printf("null obj\n");
			continue;
		}

		int lang_id = anObj->num_songs.language_type;
		if (lang_id != VIETNAMESE && lang_id != ENGLISH && lang_id != CHINESE && lang_id != PHILIPINSE && lang_id != KOREAN && lang_id != JAPANESE) {
			lang_id = LANG_OTHER;
			anObj->num_songs.language_type = lang_id;
		}

		uint32_t rowartistid = 0;
		int bindColumn = 1;
		sqlStmtPicture = NULL;
		if(anObj->num_songs.bSinger) {
			sqlPicture = "select z_pk from zsingers where zname=?";
			result = sqlite3_prepare_v2(db->sqliteHandle(), sqlPicture, (int)strlen(sqlPicture), &sqlStmtPicture, NULL);
			if (result == SQLITE_OK){
				sqlite3_bind_text(sqlStmtPicture, 1, anObj->pNameOffset, -1, SQLITE_STATIC);
				if ((result = sqlite3_step(sqlStmtPicture)) == SQLITE_ROW){
					rowartistid = sqlite3_column_int(sqlStmtPicture, 0);
				}else {
					printf("\nError for step sql = [%s] <name> [%s]", sqlPicture, anObj->pNameOffset);
				}
			}else {
				printf("\nError for prepare sql = [%s]", sqlPicture);
			}
			sqlite3_clear_bindings(sqlStmtPicture);
			sqlite3_finalize(sqlStmtPicture);
			sqlStmtPicture = NULL;
			if(rowartistid == 0) {
				//			sqlite3_bind_int(sqlStmt, bindColumn++, i);
				sqlite3_bind_text(sqlStmt, bindColumn++, anObj->pNameOffset, -1, SQLITE_STATIC);
				//		NSString *titleRaw = [charProcess getTitleRaw:[anObj name]];
				sqlite3_bind_text(sqlStmt, bindColumn++, anObj->pPYOffset, -1, SQLITE_STATIC);

#if 1
				string titleRaw("");

				if(anObj->num_songs.language_type == CHINESE) {
#ifdef __OBJC__
					NSString *hanzi = [Hanzi2Pinyin convert:[NSString stringWithUTF8String:toc->offsetToPointer(anObj->pNameOffset)]];
					titleRaw = string([hanzi UTF8String]);
#endif
				}else {
					titleRaw = strUtils->getTitleRaw(string(anObj->pNameOffset));
				}
				//		printf("%s\n", titleRaw.c_str());
				sqlite3_bind_text(sqlStmt, bindColumn++, titleRaw.c_str(), -1, SQLITE_STATIC);
#else
				sqlite3_bind_text(sqlStmt, bindColumn++, strUtils->getTitleRaw(string(toc->offsetToPointer(anObj->pSingerName.offset))).c_str(), -1, SQLITE_STATIC);
#endif

				sqlite3_bind_int(sqlStmt, bindColumn++, anObj->num_songs.uSingerSongNums);
				//			sqlite3_bind_int(sqlStmt, bindColumn++, i + 1);

#ifdef TOC_VER_4
				//            sqlite3_bind_int(sqlStmt, 7, anObj->picIdxMobile);
				sqlite3_bind_int(sqlStmt, bindColumn++, anObj->singerIdx);
#else
				sqlite3_bind_int(sqlStmt, bindColumn++, singerOffset);
#endif

#ifdef TOC_VER_2
				sqlite3_bind_int(sqlStmt, bindColumn++, anObj->num_songs.language_type);
#else
				sqlite3_bind_int(sqlStmt, bindColumn++, 0);
#endif

				result = 0;
				if ((result = sqlite3_step(sqlStmt)) != SQLITE_DONE){
					printf("\nError for sql = [%s]", "UpdateSortInfoWithOnConfict step Singer");
					db->rollback();
					importStatus = false;
					goto RELEASE_STRING;
				}
				rowartistid = (uint32_t)sqlite3_last_insert_rowid(db->sqliteHandle());

				sqlite3_clear_bindings(sqlStmt);
				sqlite3_reset(sqlStmt);
			}

			for(int z = 0; z < songCount; z++) {
				for(int k = 0; k < MAX_ARTIST_PER_SONG; k++) {
					if(songInfoUpdate[z].pPersonIdx.singer_idxes[k] == UINT16_MAX) break;

					if(songInfoUpdate[z].pPersonIdx.singer_idxes[k] == i) {
						songInfoUpdate[z].pPersonIdx.singer_idxes[k] = rowartistid;
					}
				}
			}

		}

		bindColumn = 1;
		if(anObj->num_songs.bAuthor) {
			rowartistid = 0;
			sqlStmtPicture = NULL;
			sqlPicture = "select z_pk from zmusicians where zname=?";
			result = sqlite3_prepare_v2(db->sqliteHandle(), sqlPicture, (int)strlen(sqlPicture), &sqlStmtPicture, NULL);
			if (result == SQLITE_OK){
				sqlite3_bind_text(sqlStmtPicture, 1, anObj->pNameOffset, -1, SQLITE_STATIC);
				if ((result = sqlite3_step(sqlStmtPicture)) == SQLITE_ROW){
					rowartistid = sqlite3_column_int(sqlStmtPicture, 0);
				}else {
					printf("\nError for step sql = [%s] <name> [%s]", sqlPicture, anObj->pNameOffset);
				}
			}else {
				printf("\nError for prepare sql = [%s]", sqlPicture);
			}
			sqlite3_clear_bindings(sqlStmtPicture);
			sqlite3_finalize(sqlStmtPicture);
			sqlStmtPicture = NULL;

			if(rowartistid == 0) {

				//			sqlite3_bind_int(sqlStmtAuthor, bindColumn++, i);
				sqlite3_bind_text(sqlStmtAuthor, bindColumn++, anObj->pNameOffset, -1, SQLITE_STATIC);
				//		NSString *titleRaw = [charProcess getTitleRaw:[anObj name]];
				sqlite3_bind_text(sqlStmtAuthor, bindColumn++, anObj->pPYOffset, -1, SQLITE_STATIC);

#if 1
				string titleRaw("");

				if(anObj->num_songs.language_type == CHINESE) {
#ifdef __OBJC__
					NSString *hanzi = [Hanzi2Pinyin convert:[NSString stringWithUTF8String:toc->offsetToPointer(anObj->pNameOffset)]];
					titleRaw = string([hanzi UTF8String]);
#endif
				}else {
					titleRaw = strUtils->getTitleRaw(string(anObj->pNameOffset));
				}
				//		printf("%s\n", titleRaw.c_str());
				sqlite3_bind_text(sqlStmtAuthor, bindColumn++, titleRaw.c_str(), -1, SQLITE_STATIC);
#else
				sqlite3_bind_text(sqlStmtAuthor, bindColumn++, strUtils->getTitleRaw(string(toc->offsetToPointer(anObj->pSingerName.offset))).c_str(), -1, SQLITE_STATIC);
#endif

				sqlite3_bind_int(sqlStmtAuthor, bindColumn++, anObj->num_songs.uAuthorSongNums);

				//			sqlite3_bind_int(sqlStmtAuthor, bindColumn++, i + 1);

#ifdef TOC_VER_4
				//            sqlite3_bind_int(sqlStmtAuthor, 7, anObj->picIdxMobile);
				sqlite3_bind_int(sqlStmtAuthor, bindColumn++, anObj->singerIdx);
#else
				sqlite3_bind_int(sqlStmtAuthor, bindColumn++, singerOffset);
#endif

#ifdef TOC_VER_2
				sqlite3_bind_int(sqlStmtAuthor, bindColumn++, anObj->num_songs.language_type);
#else
				sqlite3_bind_int(sqlStmtAuthor, bindColumn++, 0);
#endif

				result = 0;
				if ((result = sqlite3_step(sqlStmtAuthor)) != SQLITE_DONE){
					printf("\nError for sql = [%s]", "UpdateSortInfoWithOnConfict author");
					db->rollback();
					importStatus = false;
					goto RELEASE_STRING;
				}
				rowartistid = (uint32_t)sqlite3_last_insert_rowid(db->sqliteHandle());
				//			mapAuthors[i] = rowid;

				sqlite3_clear_bindings(sqlStmtAuthor);
				sqlite3_reset(sqlStmtAuthor);
			}

			for(int z = 0; z < songCount; z++) {
				for(int k = 0; k < MAX_ARTIST_PER_SONG; k++) {
					if(songInfoUpdate[z].pPersonIdx.author_idxes[k] == UINT16_MAX) break;

					if(songInfoUpdate[z].pPersonIdx.author_idxes[k] == i) {
						songInfoUpdate[z].pPersonIdx.author_idxes[k] = rowartistid;
					}
				}
			}
		}
	}
#else
	for (int i = 0; i<toc->getSingerCount(); i++) {
		Singer_Info_S * anObj = toc->getSingerInfo(i);
		if(!anObj)
		{
			printf("null obj\n");
			continue;
		}

		sqlite3_bind_int(sqlStmt, 1, i);

		int buf_len = 1024;
		char *tempBuf = (char *)malloc(buf_len);
		char *pbuff = tempBuf;
		char *pString;
		uint16_t name_len = 0;

		// Song name
		pbuff = tempBuf;
		bzero(tempBuf, buf_len);
		pString = toc->offsetToPointer(anObj->pSingerName.offset);
		name_len = anObj->pSingerName.len;

		//#ifdef ENCODING_TCVN
		//        if(anObj->property.language_type == 2){
		//            convertGB2312ToUTF8((const char **)&pString, (const char *)(pString + name_len), &pbuff, (char *)(pbuff + buf_len), true);
		//        }else {
		convertTCVN3ToUTF8((const char **)&pString, (const char *)(pString + name_len), &pbuff, (char *)(pbuff + buf_len), true);
		//        }
		//#else
		//        memcpy(tempBuf, pString, name_len);
		//#endif



		//		sqlite3_bind_text(sqlStmt, 2, toc->offsetToPointer(anObj->pSingerName.offset), -1, SQLITE_STATIC);
		sqlite3_bind_text(sqlStmt, 2, tempBuf, -1, SQLITE_STATIC);
		//		NSString *titleRaw = [charProcess getTitleRaw:[anObj name]];
		sqlite3_bind_text(sqlStmt, 3, toc->offsetToPointer(anObj->pPYStr.offset), -1, SQLITE_STATIC);

#if 1
		string titleRaw("");
		titleRaw = strUtils->getTitleRaw(string(toc->offsetToPointer(anObj->pSingerName.offset)));
		//		printf("%s\n", titleRaw.c_str());

		sqlite3_bind_text(sqlStmt, 4, titleRaw.c_str(), -1, SQLITE_STATIC);
#else
		sqlite3_bind_text(sqlStmt, 4, strUtils->getTitleRaw(string(toc->offsetToPointer(anObj->pSingerName.offset))).c_str(), -1, SQLITE_STATIC);
#endif

		sqlite3_bind_int(sqlStmt, 5, anObj->uSongNums);
		sqlite3_bind_int(sqlStmt, 6, i + 1);
		sqlite3_bind_blob(sqlStmt, 7, NULL, 1, NULL);

		result = 0;
		if ((result = sqlite3_step(sqlStmt)) != SQLITE_DONE){
			printf("\nError for sql = [%s]", "UpdateSortInfoWithOnConfictdg ");
			db->rollback();
			importStatus = false;
			goto RELEASE_DB;
		}
		free(tempBuf);
		sqlite3_clear_bindings(sqlStmt);
		sqlite3_reset(sqlStmt);

	}
#endif
	sqlite3_finalize(sqlStmt);
	sqlite3_finalize(sqlStmtAuthor);
	db->commit();

	sqlStmtPicture = NULL;
	sqlPicture = "select max(zsort) from zsongs";
	result = sqlite3_prepare_v2(db->sqliteHandle(), sqlPicture, (int)strlen(sqlPicture), &sqlStmtPicture, NULL);
	if (result == SQLITE_OK){
		if ((result = sqlite3_step(sqlStmtPicture)) == SQLITE_ROW){
			sortValue = sqlite3_column_int(sqlStmtPicture, 0) + 1;
		}
	}
	sqlite3_clear_bindings(sqlStmtPicture);
	sqlite3_finalize(sqlStmtPicture);
	sqlStmtPicture = NULL;

	// Insert Song
	db->beginTransaction();

#ifdef TOC_VER_4
//	sql = "insert or replace into ZSONGS(z_pk,ZID,zindex5,ZTYPE,ZREMIX,ZNEWSONG,ZNUMWORDS,ZSHOW,ZONETOUCH,ZTYPEID,ZLANGUAGEID,ZSORT,ZNAME,ZSHORTNAME,ZTITLERAW,ZLYRIC,ZABC,ZEXTRA,ZSO_MD,ZSCA) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//	sqlAuthor = "insert or replace into ZSONGMUSICIANS(ZSO_PK,ZAT_PK) values (?,?)";
//	sqlPicture = "insert or replace into ZSONGSINGERS(ZSO_PK,ZAT_PK) values (?,?)";

	sql = "insert into ZSONGS(ZID,zindex5,ZTYPE,ZREMIX,ZNEWSONG,ZNUMWORDS,ZSHOW,ZONETOUCH,ZTYPEID,ZLANGUAGEID,ZSORT,ZNAME,ZSHORTNAME,ZTITLERAW,ZLYRIC,ZABC,ZEXTRA,ZSO_MD,ZSCA) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	sqlAuthor = "insert into ZSONGMUSICIANS(ZSO_PK,ZAT_PK) values (?,?)";
	sqlPicture = "insert into ZSONGSINGERS(ZSO_PK,ZAT_PK) values (?,?)";

	sqlCount = "select count() from zsongs where zsongs.zid=?";

#elif def TOC_VER_3
	sql = "insert or replace into ZSONGS(ZID,zindex5,ZTYPE,ZREMIX,ZNEWSONG,ZNUMWORDS,ZSHOW,ZONETOUCH,ZTYPEID,ZLANGUAGEID,ZSORT,ZNAME,ZSHORTNAME,ZTITLERAW,ZLYRIC) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	sqlAuthor = "insert or replace into ZSONGMUSICIANS(ZSO_PK,ZAT_PK) values (?,?)";
	sqlPicture = "insert or replace into ZSONGSINGERS(ZSO_PK,ZAT_PK) values (?,?)";
#else
	sql = "insert or replace into ZSONGS(ZID,zindex5,ZTYPE,ZREMIX,ZNEWSONG,ZNUMWORDS,ZSHOW,ZONETOUCH,ZMUSICIANID,ZSINGERID,ZTYPEID,ZLANGUAGEID,ZSORT,ZNAME,ZSHORTNAME,ZTITLERAW,ZLYRIC) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
#endif
	sqlStmt = NULL;
	result = sqlite3_prepare_v2(db->sqliteHandle(), sql, (int)strlen(sql), &sqlStmt, NULL);
	if (result != SQLITE_OK){
		printf("\nError for sqlCmd = [%s]\n", sql);
		importStatus = false;
		goto RELEASE_DB;
	}

#ifdef TOC_VER_3
	sqlStmtAuthor = NULL;
	result = sqlite3_prepare_v2(db->sqliteHandle(), sqlAuthor, (int)strlen(sqlAuthor), &sqlStmtAuthor, NULL);
	if (result != SQLITE_OK){
		printf("\nError for sqlCmd = [%s]\n", sqlAuthor);
		importStatus = false;
		goto RELEASE_DB;
	}

	sqlStmtPicture = NULL;
	result = sqlite3_prepare_v2(db->sqliteHandle(), sqlPicture, (int)strlen(sqlPicture), &sqlStmtPicture, NULL);
	if (result != SQLITE_OK){
		printf("\nError for sqlCmd = [%s]\n", sqlPicture);
		importStatus = false;
		goto RELEASE_DB;
	}
#endif

	sqlStmtCount = NULL;
	result = sqlite3_prepare_v2(db->sqliteHandle(), sqlCount, (int)strlen(sqlCount), &sqlStmtCount, NULL);
	if (result != SQLITE_OK){
		printf("\nError for sqlCmd = [%s]\n", sqlCount);
		importStatus = false;
		goto RELEASE_DB;
	}

	for (int i = 0; i<songCount; i++) {
		Song_Info_Update_S *aSong = &songInfoUpdate[i];
		if(!aSong)
		{
			printf("null obj\n");
			continue;
		}

		cntSong = 0;
		sqlite3_bind_int(sqlStmtCount, 1, aSong->song_code6);
		result = sqlite3_step(sqlStmtCount);
		if(result == SQLITE_ROW) {
			cntSong = sqlite3_column_int(sqlStmtCount, 0);
		}
		sqlite3_clear_bindings(sqlStmtCount);
		sqlite3_reset(sqlStmtCount);
		if(cntSong > 0) {
			printf("Song existed: %d==>%s", aSong->song_code6, aSong->pSongNameOffset);
			continue;
		}

		int lang_id = aSong->property.language_type;
		if (lang_id != VIETNAMESE && lang_id != ENGLISH && lang_id != PHILIPINSE && lang_id != CHINESE && lang_id != KOREAN && lang_id != JAPANESE) {
			lang_id = LANG_OTHER;
			aSong->property.language_type = lang_id;
		}

		int bindColumn = 1;
#ifdef TOC_VER_1
#ifdef TOC_VER_4
//		sqlite3_bind_int(sqlStmt, bindColumn++, i);
#endif
		sqlite3_bind_int(sqlStmt, bindColumn++, aSong->song_code6);
		sqlite3_bind_int(sqlStmt, bindColumn++, aSong->song_code5);
		sqlite3_bind_int(sqlStmt, bindColumn++, aSong->property.media_type);
		sqlite3_bind_int(sqlStmt, bindColumn++, aSong->property.is_remix);
		sqlite3_bind_int(sqlStmt, bindColumn++, aSong->property.is_newsong);
		sqlite3_bind_int(sqlStmt, bindColumn++, strlen(aSong->pPYNameOffset)/*aSong->property.name_words*/);
		sqlite3_bind_int(sqlStmt, bindColumn++, aSong->property.is_hide);
		sqlite3_bind_int(sqlStmt, bindColumn++, aSong->property.onetouch_type);

#ifndef TOC_VER_3
		sqlite3_bind_int(sqlStmt, bindColumn++, aSong->pPersonIdx.author_idx);
		sqlite3_bind_int(sqlStmt, bindColumn++, aSong->pPersonIdx.singer_idx);
#endif

		sqlite3_bind_int(sqlStmt, bindColumn++, aSong->property.song_type);
		sqlite3_bind_int(sqlStmt, bindColumn++, aSong->property.language_type);

		sqlite3_bind_int(sqlStmt, bindColumn++, sortValue++);

		sqlite3_bind_text(sqlStmt, bindColumn++, aSong->pSongNameOffset, -1, SQLITE_STATIC);
		sqlite3_bind_text(sqlStmt, bindColumn++, aSong->pPYNameOffset, -1, SQLITE_STATIC);

#if 1
		string titleRaw("");

		if(aSong->property.language_type == CHINESE) {
#ifdef __OBJC__
			NSString *hanzi = [Hanzi2Pinyin convert:[NSString stringWithUTF8String:toc->offsetToPointer(aSong->pSongNameOffset)]];
			titleRaw = string([hanzi UTF8String]);
#endif
		}else {
			titleRaw = strUtils->getTitleRaw(string(aSong->pSongNameOffset));
			//		printf("%s\n", titleRaw.c_str());
		}
		sqlite3_bind_text(sqlStmt, bindColumn++, titleRaw.c_str(), -1, SQLITE_STATIC);
#else
		sqlite3_bind_text(sqlStmt, bindColumn++, strUtils->getTitleRaw(string(toc->offsetToPointer(aSong->pSongName.offset))).c_str(), -1, SQLITE_STATIC);
#endif

#ifdef TOC_VER_2

		//        LyricWord_Info_S *lyricWordInfo = toc->getFirstWordsLyric(i);
		//		if(lyricWordInfo != NULL){
		//        sqlite3_bind_text(sqlStmt, bindColumn++, toc->offsetToPointer(toc->getLyric10WordsTable(), lyricWordInfo->pLyricTextOffset), -1, SQLITE_STATIC);

		const char *lyricPtr = aSong->pLyricOffset;
		if (lyricPtr != NULL) {
			sqlite3_bind_text(sqlStmt, bindColumn++, lyricPtr, -1, SQLITE_STATIC);
		}else {
			sqlite3_bind_text(sqlStmt, bindColumn++, " ", 1, SQLITE_STATIC);
		}
#else
		sqlite3_bind_text(sqlStmt, bindColumn++, " ", 1, SQLITE_STATIC);
#endif

#ifdef TOC_VER_4
		sqlite3_bind_int(sqlStmt, bindColumn++, aSong->MidiInfo.stFilepos.songABCType);


		uint8_t* pSongClip = (uint8_t *)aSong->pSongClips;
		uint8_t numClips = *(pSongClip);
		uint32_t extraData = 0;

		uint8_t *pExtra = (uint8_t *)(pSongClip + 2*numClips + 1);
		if((*pExtra & 0x80) != 0) {
#ifdef REMOVE_CRITICAL_BIT
			infoExtra->bCriticalSong = 0;
#endif
			extraData |= (*pExtra);

			uint8_t *pExtra1 = (uint8_t *)(pSongClip + 2*numClips + 2);
			if((*pExtra1 & 0x80) != 0) {
				extraData |= (((uint32_t)(*pExtra1)) << 8);
			}
		}

		sqlite3_bind_int(sqlStmt, bindColumn++, extraData);

		uint32_t songIndex = aSong->song_code6;
		if (aSong->MidiInfo.stFilepos.songABCType != SONG_TYPE_NONE) {
			songIndex = aSong->song_code6 + 1000000*(aSong->MidiInfo.stFilepos.songABCType - 1);
		}
		sqlite3_bind_int(sqlStmt, bindColumn++, songIndex);

		//use soncaid copy right file
//		if (idCount == 0 || containSongID(idSonca, idCount, songIndex)) {
//			sqlite3_bind_int(sqlStmt, bindColumn++, 0);
//		}else {
			sqlite3_bind_int(sqlStmt, bindColumn++, 1);
//		}
#endif

#else //END TOC_VER1
		sqlite3_bind_int(sqlStmt, 1, aSong->song_code6);
		sqlite3_bind_int(sqlStmt, 2, aSong->sonca_info.code5);
		sqlite3_bind_int(sqlStmt, 3, aSong->sonca_info.singer_vocal);
		sqlite3_bind_int(sqlStmt, 4, 1);
		sqlite3_bind_int(sqlStmt, 5, 0);
		sqlite3_bind_int(sqlStmt, 6, aSong->property.name_words);
		sqlite3_bind_int(sqlStmt, 7, 0);
		sqlite3_bind_int(sqlStmt, 8, 0);
		sqlite3_bind_int(sqlStmt, 9, 0);
		sqlite3_bind_int(sqlStmt, 10, aSong->property.singer_idx);
		sqlite3_bind_int(sqlStmt, 11, aSong->property.song_type);
		sqlite3_bind_int(sqlStmt, 12, aSong->property.language_type);

		int buf_len = 1024;
		char *tempBuf = (char *)malloc(buf_len);
		char *pbuff = tempBuf;
		char *pString;
		uint16_t name_len = 0;

		// Song name
		pbuff = tempBuf;
		bzero(tempBuf, buf_len);
		pString = toc->offsetToPointer(aSong->pSongName.offset);
		name_len = aSong->pSongName.len;

#ifdef ENCODING_TCVN
		if(aSong->property.language_type == 2){
			convertGB2312ToUTF8((const char **)&pString, (const char *)(pString + name_len), &pbuff, (char *)(pbuff + buf_len), true);
		}else {
			convertTCVN3ToUTF8((const char **)&pString, (const char *)(pString + name_len), &pbuff, (char *)(pbuff + buf_len), true);
		}
#else
		memcpy(tempBuf, pString, name_len);
#endif
		sqlite3_bind_text(sqlStmt, 13, tempBuf, -1, SQLITE_STATIC);
		sqlite3_bind_text(sqlStmt, 14, toc->offsetToPointer(aSong->pPYStr.offset), -1, SQLITE_STATIC);

#if 1
		string titleRaw("");
		titleRaw = strUtils->getTitleRaw(tempBuf);
		//		printf("%s\n", titleRaw.c_str());
		sqlite3_bind_text(sqlStmt, 15, titleRaw.c_str(), -1, SQLITE_STATIC);
#else
		sqlite3_bind_text(sqlStmt, 15, strUtils->getTitleRaw(string(toc->offsetToPointer(aSong->pSongName.offset))).c_str(), -1, SQLITE_STATIC);
#endif
		sqlite3_bind_text(sqlStmt, 16, " ", 1, SQLITE_STATIC);
		sqlite3_bind_text(sqlStmt, 17, " ", 1, SQLITE_STATIC);
		sqlite3_bind_text(sqlStmt, 18, " ", 1, SQLITE_STATIC);
		free(tempBuf);
#endif
		result = 0;
		if ((result = sqlite3_step(sqlStmt)) != SQLITE_DONE){
			printf("\nError for sql = [%s]", "UpdateSortInfoWithOnConfict");
			printf("\nError for sql = [%s] - song id: %d", sqlite3_errmsg(db->sqliteHandle()), aSong->song_code5);
			db->rollback();
			importStatus = false;
			goto RELEASE_STRING;
		}

		uint32_t rowid = (uint32_t)sqlite3_last_insert_rowid(db->sqliteHandle());

		sqlite3_clear_bindings(sqlStmt);
		sqlite3_reset(sqlStmt);


#ifdef TOC_VER_3
		char authorText[25];
		memset(authorText, 0, sizeof(authorText));
		for(int j = 0; j < MAX_ARTIST_PER_SONG; j++)
		{
			uint16_t idx = aSong->pPersonIdx.author_idxes[j];
			if (idx == UINT16_MAX) {
				break;
			}

			//            sqlite3_bind_int(sqlStmtAuthor, 1, aSong->song_code6);
			sqlite3_bind_int(sqlStmtAuthor, 1, rowid);
			sqlite3_bind_int(sqlStmtAuthor, 2, idx);
			result = 0;
			if ((result = sqlite3_step(sqlStmtAuthor)) != SQLITE_DONE){
				printf("\nError for sql = [%s]\n", "UpdateSortInfoWithOnConfict12");
				printf("\nError for sql = [%s]\n", sqlite3_errmsg(db->sqliteHandle()));

				db->rollback();
				importStatus = false;
				goto RELEASE_STRING;
			}

			sqlite3_clear_bindings(sqlStmtAuthor);
			sqlite3_reset(sqlStmtAuthor);
		}

		for(int j = 0; j < MAX_ARTIST_PER_SONG; j++)
		{
			uint16_t idx = aSong->pPersonIdx.singer_idxes[j];
			if (idx == UINT16_MAX) {
				break;
			}

			//            sqlite3_bind_int(sqlStmtPicture, 1, aSong->song_code6);
			sqlite3_bind_int(sqlStmtPicture, 1, rowid);
			sqlite3_bind_int(sqlStmtPicture, 2, idx);
			result = 0;
			if ((result = sqlite3_step(sqlStmtPicture)) != SQLITE_DONE){
				printf("\nError for sql = [%s] --- id: %d -- value: %d-%d", sqlPicture, aSong->song_code5, rowid, idx);
				printf("\nError for sql = [%s]\n", sqlite3_errmsg(db->sqliteHandle()));
				db->rollback();
				importStatus = false;
				goto RELEASE_STRING;
			}

			sqlite3_clear_bindings(sqlStmtPicture);
			sqlite3_reset(sqlStmtPicture);
		}
#endif

	}

	sqlite3_finalize(sqlStmtCount);
	sqlite3_finalize(sqlStmt);
#ifdef TOC_VER_3
	sqlite3_finalize(sqlStmtAuthor);
	sqlite3_finalize(sqlStmtPicture);
#endif
	db->commit();

	importStatus = true;

	RELEASE_STRING:
	delete strUtils;

	RELEASE_DB:
	delete db;

	FINAL:
	printf("\nInsert song completed: result:%d\n", importStatus);
	return importStatus;
}

void Java_vn_com_sonca_cloudkaraoke_CloudKaraoke_getWordSize(JNIEnv* env, jobject thiz, jint index)
{
	decryptor.getWordSize(index);
}

jint Java_vn_com_sonca_cloudkaraoke_CloudKaraoke_getIntValue(JNIEnv* env, jobject thiz, jint value)
{
	return decryptor.getInt32(value);
}

jstring Java_vn_com_sonca_cloudkaraoke_CloudKaraoke_getStringValue(JNIEnv* env, jobject thiz, jstring str, jint offset)
{
	const char* cStr = env->GetStringUTFChars(str, NULL);
	char *strPtr = decryptor.getStringValue(cStr, offset);
	jstring jStr = env->NewStringUTF(strPtr);
	free(strPtr);
	env->ReleaseStringUTFChars(str, cStr);
	return jStr;
}


jstring Java_vn_com_sonca_cloudkaraoke_CloudKaraoke_buildServerLoginUrl(JNIEnv* env, jobject thiz)
{
	char *strPtr = serverUrl.buildServerLoginUrl();
	jstring jStr = env->NewStringUTF(strPtr);
	free(strPtr);
	return jStr;
}

jstring Java_vn_com_sonca_cloudkaraoke_CloudKaraoke_buildServerValidateUrl(JNIEnv* env, jobject thiz)
{
	char url[256];
	memset(url,0, sizeof(url));
	char *urlPtr = url;
	memcpy(urlPtr, "https://www.vercoop.com/api2/usr_login?", 39);urlPtr += 39;

	jstring jStr = env->NewStringUTF(url);

	return jStr;
}

jstring Java_vn_com_sonca_cloudkaraoke_CloudKaraoke_buildRequestUpdateString(JNIEnv* env,
		jobject thiz, jstring transID, jint task)
{
	const char* cTransID = env->GetStringUTFChars(transID, NULL);

	char *strPtr = serverUrl.buildRequestUpdateString(cTransID, task);
	jstring jStr = env->NewStringUTF(strPtr);
	env->ReleaseStringUTFChars(transID, cTransID);
	free(strPtr);
	return jStr;
}

jstring Java_vn_com_sonca_cloudkaraoke_CloudKaraoke_buildRequestChargeInfoString(JNIEnv* env,
		jobject thiz, jstring transID)
{
	const char* cTransID = env->GetStringUTFChars(transID, NULL);
	char *strPtr = serverUrl.buildRequestChargeInfoString(cTransID);
	jstring jStr = env->NewStringUTF(strPtr);
	free(strPtr);
	env->ReleaseStringUTFChars(transID, cTransID);
	return jStr;
}

jstring Java_vn_com_sonca_cloudkaraoke_CloudKaraoke_buildRequestString(JNIEnv* env,
		jobject thiz, jstring transID, jint task, jint index) {

	const char* cTransID = env->GetStringUTFChars(transID, NULL);
	char *strPtr = serverUrl.buildRequestString(cTransID, task, index);
	jstring jStr = env->NewStringUTF(strPtr);
	env->ReleaseStringUTFChars(transID, cTransID);
	free(strPtr);
	return jStr;
}

jstring Java_vn_com_sonca_cloudkaraoke_CloudKaraoke_buildRequestString__(JNIEnv* env,
		jobject thiz, jstring transID, jint task, jint index) {

	const char* cTransID = env->GetStringUTFChars(transID, NULL);
	char *strPtr = serverUrl.buildRequestString__(cTransID, task, index);
	jstring jStr = env->NewStringUTF(strPtr);
	env->ReleaseStringUTFChars(transID, cTransID);
	free(strPtr);
	return jStr;
}

// now we have to register our "mult" function as a method with JNI so the JVM can figure out how to call us
// first step is to define an array of structures describing the methods we want to register, in this case one method
// each structure in the array describes one method with the name of the method, the method signature,
// a pointer to the native function that implements the method.
//
// The method signature describes the Java types of the arguments (if any) and the return value as a structured
// string. This string is given in the header generated by the "javah" tool. In this case the signature is
// described by the line "* Signature: (II)I".

static const JNINativeMethod method_table[] =
{
		//		{"importTocToDatabase", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Z", (void *) Java_vn_com_sonca_smartkaraoke_SmartKaraoke_importTocToDatabase},
		{"importdata", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Z", (void *) Java_vn_com_sonca_smartkaraoke_SmartKaraoke_importTocToDatabase},
		{"extractdata", "(Ljava/lang/String;Ljava/lang/String;)Z", (void *) Java_vn_com_sonca_smartkaraoke_SmartKaraoke_extractPictureFile},
		{"mergeData", "(Ljava/lang/String;Ljava/lang/String;I)I", (void *) Java_vn_com_sonca_smartkaraoke_SmartKaraoke_mergeData},
		{"mergeDataUpdate", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)I", (void *) Java_vn_com_sonca_smartkaraoke_SmartKaraoke_mergeDataUpdate},
		{"renameDataUpdate", "(Ljava/lang/String;II)I", (void *) Java_vn_com_sonca_smartkaraoke_SmartKaraoke_renameDataUpdate},
		{"setDataUpdateVersion", "(Ljava/lang/String;III)I", (void *) Java_vn_com_sonca_smartkaraoke_SmartKaraoke_setDataUpdateVersion},

		//		{"importSongModel", "(Ljava/lang/String;I[II)Z", (void *) Java_vn_com_sonca_smartkaraoke_SmartKaraoke_importSongModel},
		{"native_getVersion", "(Ljava/lang/String;)[I", (void *) Java_vn_com_sonca_smartkaraoke_SmartKaraoke_getVersionInfo},
		{"parseFileInfo", "(Ljava/lang/String;)[B", (void *) Java_vn_Com_sonca_smartkaraoke_SmartKaraoke_parseFileInfo},
		{"structSize", "()I", (void *) Java_vn_Com_sonca_smartkaraoke_SmartKaraoke_getStructSize},
		{"getDataBytes", "(ZLjava/lang/String;III)[B", (void *)Java_vn_Com_sonca_smartkaraoke_SmartKaraoke_getDataBytes},
		{"getdata", "(Ljava/lang/String;Ljava/lang/String;I)I", (void *)Java_vn_Com_sonca_smartkaraoke_SmartKaraoke_getDataMidi},
		{"getdata", "(ZLjava/lang/String;Ljava/lang/String;III)I", (void *)Java_vn_Com_sonca_smartkaraoke_SmartKaraoke_getData},
		{"addNewSong", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lvn/com/sonca/params/SongInfo;I)V", (void *)Java_vn_Com_sonca_smartkaraoke_SmartKaraoke_addNewSong},
		{"removeSong", "(Ljava/lang/String;Ljava/lang/String;Lvn/com/sonca/params/SongInfo;)V", (void *)Java_vn_Com_sonca_smartkaraoke_SmartKaraoke_removeSong},
		{"addNewSongList", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;[Lvn/com/sonca/params/SongInfo;I)V", (void *)Java_vn_Com_sonca_smartkaraoke_SmartKaraoke_addNewSongList},
		{"removeSongList", "(Ljava/lang/String;Ljava/lang/String;[Lvn/com/sonca/params/SongInfo;)V", (void *)Java_vn_Com_sonca_smartkaraoke_SmartKaraoke_removeSongList},
		{"getStringData", "(I)[B", (void *)Java_vn_Com_sonca_smartkaraoke_SmartKaraoke_getStringData},



		/*
		{"buildRequestString", "(Ljava/lang/String;II)Ljava/lang/String;", (void *) Java_vn_com_sonca_cloudkaraoke_CloudKaraoke_buildRequestString},
		{"buildRequestString__", "(Ljava/lang/String;II)Ljava/lang/String;", (void *) Java_vn_com_sonca_cloudkaraoke_CloudKaraoke_buildRequestString__},
		{"buildRequestChargeInfoString", "(Ljava/lang/String;)Ljava/lang/String;", (void *) Java_vn_com_sonca_cloudkaraoke_CloudKaraoke_buildRequestChargeInfoString},
		{"buildRequestUpdateString", "(Ljava/lang/String;I)Ljava/lang/String;", (void *) Java_vn_com_sonca_cloudkaraoke_CloudKaraoke_buildRequestUpdateString},
		{"buildServerLoginUrl", "()Ljava/lang/String;", (void *) Java_vn_com_sonca_cloudkaraoke_CloudKaraoke_buildServerLoginUrl},
		{"buildServerValidateUrl", "()Ljava/lang/String;", (void *) Java_vn_com_sonca_cloudkaraoke_CloudKaraoke_buildServerValidateUrl},

		{"getWordSize", "(I)V", (void *) Java_vn_com_sonca_cloudkaraoke_CloudKaraoke_getWordSize},
		{"getIntValue", "(I)I", (void *) Java_vn_com_sonca_cloudkaraoke_CloudKaraoke_getIntValue},
		{"getStringValue", "(Ljava/lang/String;I)Ljava/lang/String;", (void *) Java_vn_com_sonca_cloudkaraoke_CloudKaraoke_getStringValue},
		 */
};

// the next step is registering the native method when the OnLoad event occurs
extern "C" jint JNI_OnLoad(JavaVM* vm, void* reserved)
{
	JNIEnv* env = NULL;
	jint result = -1;
	jclass clazz;
	static const char* const strClassName = "vn/com/sonca/smartkaraoke/SmartKaraoke";
	if (vm->GetEnv((void**) &env, JNI_VERSION_1_6) != JNI_OK) {
		LOGE("ERROR: GetEnv failedn");
		return result;
	}

	if (env == NULL) {
		LOGE("ERROR: env is NULLn");
		return result;
	}
	/*
	android::gpJavaVM = vm;
	android::register_android_database_SQLiteConnection(env);
	android::register_android_database_SQLiteDebug(env);
	android::register_android_database_SQLiteGlobal(env);
	 */
	/* get class with (*env)->FindClass */
	clazz = env->FindClass(strClassName);
	if (clazz == NULL) {
		//        LOGE("", "Can't find class %sn", strClassName);
		return result;
	}
	/* register methods with (*env)->RegisterNatives */
	if (env->RegisterNatives(clazz, method_table,
			sizeof(method_table) / sizeof(method_table[0])) != JNI_OK)
	{
		//    	LOGE("", "Failed registering methods for %sn", strClassName);
		return result;
	}

	return JNI_VERSION_1_6;
}


#endif
