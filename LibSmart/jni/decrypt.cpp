//
//  decrypt.cpp
//  CloudKaraoke-Dev
//
//  Created by BHDMinh on 2/14/14.
//  Copyright (c) 2014 soncamedia. All rights reserved.
//

#include "decrypt.h"
#include "utf8.h"

#include <vector>
#include "debug_log.h"

const char nCharsText[] = "0123456789ABCDEF";

void CDecryptLyric::getWordSize(int index)
{
    index += 27894;
	nPasswordStr = index;
    
	//Get pwd bytes
	uint8_t pwd[3]; // = new int[3];
	pwd[0] = ((index >> 24) & 0xFF);
	pwd[1] = ((index >> 16) & 0xFF);
	pwd[2] = ((index >> 8) & 0xFF);
    
	//cook
	pwd[0] = ((pwd[0] ^ pwd[1]) & 0xFF);
	pwd[1] = ((pwd[1] ^ (~pwd[2])) & 0xFF);
	pwd[2] = ((pwd[2] ^ (~pwd[0])) & 0xFF);
	nPasswordInt32 = ((pwd[0] << 16) | (pwd[1] << 8) | (pwd[2])) & 0xFFFFFF;
}

uint32_t CDecryptLyric::getInt32(int value)
{
    return  nPasswordInt32 ^ value;
}

uint8_t hexToInt(uint8_t hex)
{
	if(hex >= '0' && hex <= '9')
		return hex - '0';
	else if(hex >= 'a' && hex <= 'f')
	{
		return hex - 'a' + 10;
	}else if(hex >= 'A' && hex <= 'F')
	{
		return hex - 'A' + 10;
	}else {
		return 0;
	}
}

char * CDecryptLyric::getStringValue(const char *str, int offset)
{
    char *strPtr = (char *)str;
	int i = 0;
	int textLen =  strlen(str);
    //	LOGI("Password int32: %d", nPasswordInt32);
    //	LOGI("Password String: %d", nPasswordStr);
//    LOGI("%s\n", strPtr); 
//    LOGI("Len: %d\n", textLen);
//    for(i = 0; i < textLen; i++) {
//    	LOGI("0x%2x,", *(strPtr + i));
//   	}
    //	LOGI("Offset: %d", offset);
	//Convert other String to Text in Hex
	offset = (offset % (58 - 16 + 1));
	//    NSLog(@"Offset: %d", offset);
    
    //	LOGI("Offsetafter: %d", offset);
	uint8_t *strHex = (uint8_t *) malloc(textLen + 1); //[1024];//textLen + 1
	if(strHex == NULL){
		LOGI("Khong the cap phat\n");
		return NULL;
	}
    
	int32_t index = 0;
	uint16_t byteCount = 0;
	while (byteCount < textLen)
	{
		index = *strPtr++ - 65 - offset;
		*(strHex + byteCount) = nCharsText[index];
		byteCount ++;
	}
	*(strHex + byteCount) = 0x00;
//	LOGI("Hex String: %s", strHex);
//   	LOGI("Hex String len: %d", byteCount);
    //	for(i = 0; i < byteCount; i++) {
    //		LOGI("%c", strHex[i]);
    //	}
    
	uint8_t oneByte;
	int len = byteCount;
//	uint8_t nBytes[len + 1]; //[1024];
    
	byteCount = 0;
	char hexChar[3];
//   	LOGI("Hex String len: %d\n", len);
	for (i = 0; i < len; i += 2)
	{
		/*
        //		strcpy(hexChar, &strHex[i]); //)   strHex.substring(i, i+2);
		hexChar[0] = *(strHex + i);
		hexChar[1] = *(strHex + i + 1);
//		LOGI("Hex char: %s", hexChar);
		oneByte = (uint8_t)(strtoul((const char *)hexChar, NULL, 16) & 0xFF);
		*/

		oneByte = (hexToInt(*(strHex + i)) << 4) + hexToInt(*(strHex + i + 1));

//        LOGI("One Byte: %02X", oneByte);
		oneByte = ((oneByte ^ (nPasswordStr & 0xFF)) & 0xFF);
//        LOGI("One Byte decrypt: %c", oneByte);
		*(strHex + byteCount) = oneByte;
		byteCount ++;
	}
	*(strHex + byteCount) = 0x00;

//	LOGI("%s", strHex);
//    printf("Decode Value:");
//    for(i = 0; i < byteCount; i++) {
//        printf("0x%02X,", nBytes[i]);
//    }
//    printf("\n");
    
	len = byteCount;
	uint16_t* resultStr = (uint16_t *)malloc(len * sizeof(uint16_t) + 1); //[512];
	if(resultStr == NULL){
		LOGI("Khong the cap phat\n");
		return NULL;
	}
    //	uint16_t *resultPtr = resultStr;
	uint16_t curVal = 0;
    
    //	LOGI("Get Real String");
	i = 0;
	byteCount = 0;
//   	LOGI("String len: %d\n", len);

	while (i < len)
	{
		curVal = *(strHex +i) & 0xFF;
		if (((curVal >= 32) && (curVal <= 127)) || (i == (byteCount - 1)))
		{
			//*resultPtr ++ = curVal;
			*(resultStr + byteCount) = curVal;
			byteCount++;
		}
		else
		{
			if (curVal < 32)
			{
				curVal = (curVal << 8) & 0xFF00;
				curVal |= (*(strHex + i + 1) & 0xFF);
                //				*resultPtr ++ = curVal;
				*(resultStr + byteCount) = curVal;
				byteCount++;
				i++;
			}
			else if (curVal > 127)
			{
                //				*resultPtr ++ = curVal;
				*(resultStr + byteCount) = curVal;
				byteCount++;
			}
		}
		i++;
	}
	*(resultStr + byteCount) = 0;

    vector<unsigned char> utf8result;
    utf8::utf16to8(resultStr, resultStr + byteCount, back_inserter(utf8result));
    len = utf8result.size();
    char *returnStr = (char *) malloc(len + 1);
    memset(returnStr, 0, len + 1);
    memcpy(returnStr, utf8result.data(), len);

//    LOGI("%s", returnStr);
    free(strHex); 
    free(resultStr);
    return returnStr;
}
