//
//  server_url.cpp
//  CloudKaraoke-Dev
//
//  Created by BHDMinh on 2/17/14.
//  Copyright (c) 2014 soncamedia. All rights reserved.
//

#include "server_url.h"
#include "base64.h"

int decryptInternal(const char*encStr, const char* pass, char * dest);
int encryptInternal(const char*encStr, const char* pass, char * dest);
int appendTaskAction(load_task task, char *strPtr);

#if defined (DEVICE_ZROAD)
static const char privateKey[] = "4OelxZDls/LMp6P6wdfm2g=="; // pass: cloudK@r@0keS0nC2 key:@pst0reCl0uKa@0k
const char * URL_HOST = "VHp8XE80JwNXYXsCT2FmT11jbUhVbyZPU2Mn";  // https://kos.soncamedia.com/
const char * URL_HOST_ = "VHp8XAYhJ0dTfSZfU2BrTVFrbEVdIGtDUSE=";  // http://kos.soncamedia.com/
const char * APP_VERSION = "Sj87HAwh";  // v1300/
const char * APP_VERSION_ATT = "Sj87HAw=";  // v1300
const char * DEVICE_TYPE = "ZnxnTVhRSUJYfGdFWA==";  // Zroad_Android
const char * LOGIN_SERVICE = "E3xtSxJve0FE";  // /reg.asmx
const char * LOGIN_PATH = "TmtvAl19eFQD";  // reg.aspx?
const char * LOAD_DATA_PATH = "THtqAl19eFQD";  // pub.aspx?
const char * LOAD_UPDATE_PATH = "X2ZtT1d7eEhdem0CXX14VAM=";  // checkupdate.aspx?
const char * LOAD_CHARGEINFO_PATH = "X2ZpXltrYUJaYSZNT35wEw==";  // chargeinfo.aspx?

const char * TASK_LOAD_VIDEO_LIST = "cGFpSGpnbElTQmFfSA==";  // LoadVideoList
const char * TASK_LOAD_VIDEO = "cGFpSGpnbElTSGFAWQ==";  // LoadVideoFile
const char * TASK_LOAD_SNAPSHOT = "cGFpSGpnbElTXWZNTA==";  // LoadVideoSnap
const char * TASK_LOAD_FREESONG = "cGFpSHp8bUlwZ3tY";  // LoadFreeList
const char * TASK_LOAD_LYRIC = "cGFpSHpnZElwd3pFXw==";  // LoadFileLyric
const char * TASK_LOAD_AUDIO = "cGFpSHpnZElxXjs=";  // LoadFileMP3
const char * TASK_LOAD_ADVT = "cGFpSH1q";  // LoadAd
const char * TASK_LOAD_CHECKUPDATE = "f2ZtT1d7eEhdem0=";  // Checkupdate
const char * TASK_LOAD_UPDATE = "cGFpSGl+bE1Iaw==";  // LoadUpdate
const char * TASK_LOAD_FIRSTUPDATE = "cGFpSHpne1hpfmxNSGs=";  // LoadFistUpdate
const char * TASK_LOAD_FIRSTUPDATE_ZIP = "cGFpSHpnel9IW3hIXXptdlV+"; // LoadFirstUpdateZip
const char * TASK_LOAD_FINISHUPDATE = "emdmRU9mXVxYb3xJ"; // FinishUpdate
const char * TASK_LOAD_HOT_FREE = "cGFpSG9hZkt0YXxqTmttYFV9fA=="; // LoadSongHotFreeList
const char * TASK_LOAD_HOT_FULL = "cGFpSG9hZkt0YXxqSWJkYFV9fA=="; // LoadSongHotFullList
const char * TASK_LOAD_ANOUNCMENT = "fWBmQ0ltbUFZYHw=";  // Annoucement
const char * TASK_LOAD_USER_LOGIN = "V2F7f1Nga00=";  // kosSonca
const char * TASK_LOAD_USER_PASS = "RWpvXUx6MBREVnkcVz17VARCMF1IaXB7DktOXn1C";  // ydgqpt88xXq0k3sx8L8qtgxW2EFrAL

#else
const char * privateKey = "4OelxZDls/LMp6P6wdfm2g=="; // pass: cloudK@r@0keS0nC2 key:@pst0reCl0uKa@0k
const char * URL_HOST = "VHp8XE80JwNXYXsCT2FmT11jbUhVbyZPU2Mn";  // https://kos.soncamedia.com/
const char * URL_HOST_ = "VHp8XAYhJ0dTfSZfU2BrTVFrbEVdIGtDUSE=";  // http://kos.soncamedia.com/
const char * APP_VERSION = "Sj87HAwh";  // v1300/
const char * APP_VERSION_ATT = "Sj87HAw=";  // v1300
const char * DEVICE_TYPE = "fWBsXlNnbA==";  // Android
const char * LOGIN_SERVICE = "E3xtSxJve0FE";  // /reg.asmx
const char * LOGIN_PATH = "TmtvAl19eFQD";  // reg.aspx?
const char * LOAD_DATA_PATH = "THtqAl19eFQD";  // pub.aspx?
const char * LOAD_UPDATE_PATH = "X2ZtT1d7eEhdem0CXX14VAM=";  // checkupdate.aspx?
const char * LOAD_CHARGEINFO_PATH = "X2ZpXltrYUJaYSZNT35wEw==";  // chargeinfo.aspx?

const char * TASK_LOAD_VIDEO_LIST = "cGFpSGpnbElTQmFfSA==";  // LoadVideoList
const char * TASK_LOAD_VIDEO = "cGFpSGpnbElTSGFAWQ==";  // LoadVideoFile
const char * TASK_LOAD_SNAPSHOT = "cGFpSGpnbElTXWZNTA==";  // LoadVideoSnap
const char * TASK_LOAD_FREESONG = "cGFpSHp8bUlwZ3tY";  // LoadFreeList
const char * TASK_LOAD_LYRIC = "cGFpSHpnZElwd3pFXw==";  // LoadFileLyric
const char * TASK_LOAD_AUDIO = "cGFpSHpnZElxXjs=";  // LoadFileMP3
const char * TASK_LOAD_ADVT = "cGFpSH1q";  // LoadAd
const char * TASK_LOAD_CHECKUPDATE = "f2ZtT1d7eEhdem0=";  // Checkupdate
const char * TASK_LOAD_UPDATE = "cGFpSGl+bE1Iaw==";  // LoadUpdate
const char * TASK_LOAD_FIRSTUPDATE = "cGFpSHpne1hpfmxNSGs=";  // LoadFistUpdate
const char * TASK_LOAD_FIRSTUPDATE_ZIP = "cGFpSHpnel9IW3hIXXptdlV+"; // LoadFirstUpdateZip
const char * TASK_LOAD_FINISHUPDATE = "emdmRU9mXVxYb3xJ"; // FinishUpdate
const char * TASK_LOAD_HOT_FREE = "cGFpSG9hZkt0YXxqTmttYFV9fA=="; // LoadSongHotFreeList
const char * TASK_LOAD_HOT_FULL = "cGFpSG9hZkt0YXxqSWJkYFV9fA=="; // LoadSongHotFullList
const char * TASK_LOAD_ANOUNCMENT = "fWBmQ0ltbUFZYHw=";  // Annoucement
const char * TASK_LOAD_USER_LOGIN = "V2F7f1Nga00=";  // kosSonca
const char * TASK_LOAD_USER_PASS = "RWpvXUx6MBREVnkcVz17VARCMF1IaXB7DktOXn1C";  // ydgqpt88xXq0k3sx8L8qtgxW2EFrAL

#endif


char * CServerUrl::buildServerLoginUrl()
{
    char *url = (char *)malloc(256);
	memset(url,0, strlen(url));
	char *urlPtr = url;
	int len = 0;
    
	len = decryptInternal(URL_HOST, privateKey, urlPtr);urlPtr += len;
	len = decryptInternal(APP_VERSION, privateKey, urlPtr);urlPtr += len;
	decryptInternal(LOGIN_SERVICE, privateKey, urlPtr);
    return url;
}

char * CServerUrl::buildRequestUpdateString(const char *transID, int task)
{
    char *url = (char *)malloc(256);
	memset(url,0, strlen(url));
	char *urlPtr = url;
	int len = 0;
    
	len = decryptInternal(URL_HOST, privateKey, urlPtr);urlPtr += len;
	len = decryptInternal(APP_VERSION, privateKey, urlPtr);urlPtr += len;
	len = decryptInternal(LOAD_UPDATE_PATH, privateKey, urlPtr);urlPtr += len;
    
    //	?task=&appKey=121299000177&esn=MQ138YEC100489&duid="+BaseService.UDID+"&transID="+KaraokeSetting.getTranID(context)+"&device=TCL_Android");
    
	memcpy(urlPtr, "transID=",8);urlPtr += 8;
	memcpy(urlPtr, transID, strlen(transID));urlPtr += strlen(transID);
	memcpy(urlPtr, "&task=",6);urlPtr += 6;
    len = appendTaskAction((load_task)task, urlPtr);urlPtr += len;
	memcpy(urlPtr, "&device=",8);urlPtr += 8;
    len = decryptInternal(DEVICE_TYPE, privateKey, urlPtr);
    return url;
}

char * CServerUrl::buildRequestChargeInfoString(const char *transID)
{
    char *url = (char *)malloc(256);
	memset(url,0, strlen(url));
	char *urlPtr = url;
	int len = 0;
    
	len = decryptInternal(URL_HOST, privateKey, urlPtr);urlPtr += len;
	len = decryptInternal(APP_VERSION, privateKey, urlPtr);urlPtr += len;
	len = decryptInternal(LOAD_CHARGEINFO_PATH, privateKey, urlPtr);urlPtr += len;
    
	memcpy(urlPtr, "transID=",8);urlPtr += 8;
	memcpy(urlPtr, transID, strlen(transID));urlPtr += strlen(transID);
	memcpy(urlPtr, "&device=",8);urlPtr += 8;
    len = decryptInternal(DEVICE_TYPE, privateKey, urlPtr);
    
    return url;
}

char * CServerUrl::buildRequestString(const char* transID, int task, int index)
{
    char *url = (char *)malloc(256);
	memset(url,0, strlen(url));
	char *urlPtr = url;
	int len = 0;
    
	len = decryptInternal(URL_HOST, privateKey, urlPtr);urlPtr += len;
	len = decryptInternal(APP_VERSION, privateKey, urlPtr);urlPtr += len;
	len = decryptInternal(LOAD_DATA_PATH, privateKey, urlPtr);urlPtr += len;
    
	memcpy(urlPtr, "transID=",8);urlPtr += 8;
	memcpy(urlPtr, transID, strlen(transID));urlPtr += strlen(transID);
	memcpy(urlPtr, "&task=",6);urlPtr += 6;
	len = appendTaskAction((load_task)task, urlPtr);urlPtr += len;
	memcpy(urlPtr, "&index=",7);urlPtr += 7;
	len = sprintf(urlPtr, "%06d", index); urlPtr += len;
	memcpy(urlPtr, "&device=",8);urlPtr += 8;
    len = decryptInternal(DEVICE_TYPE, privateKey, urlPtr);urlPtr += len;
    
    return url;
}

char * CServerUrl::buildRequestString__(const char* transID, int task, int index)
{
    char *url = (char *)malloc(256);
	memset(url,0, strlen(url));
	char *urlPtr = url;
	int len = 0;
    
    len = decryptInternal(URL_HOST_, privateKey, urlPtr);urlPtr += len;
    len = decryptInternal(APP_VERSION, privateKey, urlPtr);urlPtr += len;
    len = decryptInternal(LOAD_DATA_PATH, privateKey, urlPtr);urlPtr += len;
    
    memcpy(urlPtr, "transID=",8);urlPtr += 8;
    memcpy(urlPtr, transID, strlen(transID));urlPtr += strlen(transID);
    memcpy(urlPtr, "&task=",6);urlPtr += 6;
    len = appendTaskAction((load_task)task, urlPtr);urlPtr += len;
    memcpy(urlPtr, "&index=",7);urlPtr += 7;
    len = sprintf(urlPtr, "%06d", index); urlPtr += len; 
    memcpy(urlPtr, "&device=",8);urlPtr += 8;
    len = decryptInternal(DEVICE_TYPE, privateKey, urlPtr);urlPtr += len;
    return url;
}

int appendTaskAction(load_task task, char *strPtr)
{
    int strLen = 0;
    switch (task) {
        case LOAD_VIDEO_LIST:
            strLen = decryptInternal(TASK_LOAD_VIDEO_LIST, privateKey, strPtr);
            break;
            
        case LOAD_VIDEO_ITEM:
            strLen = decryptInternal(TASK_LOAD_VIDEO, privateKey, strPtr);
            break;
            
        case LOAD_VIDEO_SNAP:
            strLen = decryptInternal(TASK_LOAD_SNAPSHOT, privateKey, strPtr);
            break;
            
        case LOAD_FREE_LIST:
            strLen = decryptInternal(TASK_LOAD_FREESONG, privateKey, strPtr);
            break;
            
        case LOAD_lYRIC:
            strLen = decryptInternal(TASK_LOAD_LYRIC, privateKey, strPtr);
            break;
            
        case LOAD_AUDIO:
            strLen = decryptInternal(TASK_LOAD_AUDIO, privateKey, strPtr);
            break;
            
        case LOAD_ADVT:
            strLen = decryptInternal(TASK_LOAD_ADVT, privateKey, strPtr);
            break;
            
        case LOAD_FIRST_UPDATE:
            strLen = decryptInternal(TASK_LOAD_FIRSTUPDATE, privateKey, strPtr);
            break;
            
        case LOAD_UPDATE:
            strLen = decryptInternal(TASK_LOAD_UPDATE, privateKey, strPtr);
            break;
            
        case LOAD_CHECK_UPDATE:
            strLen = decryptInternal(TASK_LOAD_CHECKUPDATE, privateKey, strPtr);
            break;
            
        case LOAD_FIRST_UPDATE_ZIP:
        	strLen = decryptInternal(TASK_LOAD_FIRSTUPDATE_ZIP, privateKey, strPtr);
        	break;

        case LOAD_FINISH_UPDATE:
        	strLen = decryptInternal(TASK_LOAD_FINISHUPDATE, privateKey, strPtr);
            break;

        case LOAD_HOT_FREE:
            strLen = decryptInternal(TASK_LOAD_HOT_FREE, privateKey, strPtr);
            break;
            
        case LOAD_HOT_FULL:
        	strLen = decryptInternal(TASK_LOAD_HOT_FULL, privateKey, strPtr);
        	break;

        case LOAD_ANOUNCEMENT:
            strLen = decryptInternal(TASK_LOAD_ANOUNCMENT, privateKey, strPtr);
            break;
            
        case GET_USER_LOGIN:
            strLen = decryptInternal(TASK_LOAD_USER_LOGIN, privateKey, strPtr);
            break;
            
        case GET_USER_PASS:
            strLen = decryptInternal(TASK_LOAD_USER_PASS, privateKey, strPtr);
            break;
            
        default:
            break;
    }
    return strLen;
}

void longToByte(uint32_t l, uint8_t *bArr)
{
    *bArr++ = (uint8_t) ((l >> 0) & 0xFF);
    *bArr++ = (uint8_t) ((l >> 8) & 0xFF);
    *bArr++ = (uint8_t) ((l >> 16) & 0xFF);
    *bArr++ = (uint8_t) ((l >> 24) & 0xFF);
}

void djb2HashFunction(const char *str, uint8_t * hashPtr)
{
	uint32_t hash = 5381;
	int c;
    
	while ((c = *str++) != 0)
		hash = ((hash << 5) + hash) + c; /* hash * 33 + c */
    
    longToByte(hash, hashPtr);
}

void keyGenerate(const char *pass, uint8_t* keyHash, uint8_t hashLen)
{
	djb2HashFunction(pass, keyHash);
	int i = 0;
	for (i = 0; i < hashLen; i++)
	{
		keyHash[i] = (keyHash[i] ^ 0xFF);
	}
}

int encryptInternal(const char*rawStr, const char* pass, char * dest){
	uint8_t blockLen = 4;
	uint8_t keyHash[blockLen];
	int i = 0;
    
	keyGenerate(pass, keyHash, blockLen);
	char*ptrData = (char *)rawStr;
    
	size_t srcLen = strlen(rawStr);
	int pos = 0;
	int byteRead = 0;
	while (pos < srcLen)
	{
		byteRead = (srcLen - pos) / blockLen > 0 ? blockLen : (srcLen - pos) % blockLen;
		for (i = 0; i < byteRead; i++)
		{
			*(ptrData + pos + i) = *(ptrData + pos + i) ^ keyHash[i];
		}
		pos += byteRead;
	}
    
	size_t dstLen = (srcLen << 3) / 6;
	switch( (srcLen << 3) - (dstLen * 6) )
	{
		case  2: dstLen += 3; break;
		case  4: dstLen += 2; break;
		default: break;
	}
	dstLen += 1;
    
	int error = base64_encode((uint8_t*)dest, &dstLen, (const uint8_t*)ptrData, srcLen);
	if(error != 0)
	{
		printf("encode base 64 error: %d", error);
		return -1;
	}
    
	return 0;
}

int decryptInternal(const char* encStr, const char* pass, char* dest) {
	uint8_t blockLen = 4;
	uint8_t keyHash[4];
	int i = 0;
    
	keyGenerate(pass, keyHash, blockLen);
    
	int srcLen = strlen(encStr);
    if(srcLen == 0)
        return 0;
    
	size_t dstLen = srcLen;
	int error = base64_decode((uint8_t *)dest, &dstLen, (const uint8_t*)encStr, srcLen);
	if(error != 0)
	{
		printf("Decode base 64 error");
		return -1;
	}
	if(dstLen < srcLen)
	{
		dest[dstLen] = 0x00; // terminated String
	}
    
	int pos = 0;
	int byteRead = 0;
	while (pos < dstLen)
	{
		byteRead = (dstLen - pos) / blockLen > 0 ? blockLen : (dstLen - pos) % blockLen;
		for (i = 0; i < byteRead; i++)
		{
			dest[pos + i] = dest[pos + i] ^ keyHash[i];
		}
		pos += byteRead;
	}
	return dstLen;
}

