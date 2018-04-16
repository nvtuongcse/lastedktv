/*
 * string_utils.cpp
 *
 *  Created on: Jul 21, 2014
 *      Author: bhdminh
 */

#include "StringUtils.h"
#include <stdlib.h>
#include <string>

StringUtils::StringUtils()
{
    encodeBuf = (char *)malloc(5);
    pEncodeBuf = encodeBuf;
}

StringUtils::~StringUtils()
{
	if(encodeBuf != NULL)
		free(encodeBuf);
}

std::string StringUtils::getPinyin(std::string name) {
	const char *pStr = name.c_str();
	unsigned char len = 0;
	unsigned  ch = 0;
	char * pBuf;
	string rawString("");
	bool startWord = true;

	while (*pStr != 0) {
		pBuf = pEncodeBuf;
		ch = UTF8_DecodeChar(&pStr);
		ch = processVietnameseRawChar(ch);

		if(ch == ' ') {
			startWord = true;
			continue;
		}else if(ch == '-' || ch == '(' || ch ==  ')' || ch == '{' || ch == '[' || ch == '&' || ch == ';') {
			continue;
		}

		if(startWord) {
			startWord = false;
			len = UTF8_CharEncodedLength(ch);
			UTF8_EncodeChar(&pBuf, ch);
			rawString.append(pEncodeBuf, len);
			if(rawString.length() > 10) break;
		}
	}
	return rawString;
}

std::string StringUtils::getTitleRaw(std::string name)
{
    const char *pStr = name.c_str();
    unsigned char len = 0;
    unsigned  ch = 0;
    char * pBuf;
    string rawString("");

    while (*pStr != 0) {
    	pBuf = pEncodeBuf;
    	ch = UTF8_DecodeChar(&pStr);
    	ch = processVietnameseRawChar(ch);

    	len = UTF8_CharEncodedLength(ch);

    	UTF8_EncodeChar(&pBuf, ch);
    	rawString.append(pEncodeBuf, len);
    }

    return rawString;
}

unsigned StringUtils::processVietnameseRawChar(unsigned vChr)
{
    unsigned retCharacter = vChr;
    switch (vChr)
    {
        case 0x0041:
        case 0x00C1:
        case 0x00C0:
        case 0x1EA2:
        case 0x00C3:
        case 0x1EA0:
        case 0x0102:
        case 0x1EAE:
        case 0x1EB0:
        case 0x1EB2:
        case 0x1EB4:
        case 0x1EB6:
        case 0x00C2:
        case 0x1EA4:
        case 0x1EA6:
        case 0x1EA8:
        case 0x1EAA:
        case 0x1EAC:
        case 0x0061:
        case 0x00E1:
        case 0x00E0:
        case 0x1EA3:
        case 0x00E3:
        case 0x1EA1:
        case 0x0103:
        case 0x1EAF:
        case 0x1EB1:
        case 0x1EB3:
        case 0x1EB5:
        case 0x1EB7:
        case 0x00E2:
        case 0x1EA5:
        case 0x1EA7:
        case 0x1EA9:
        case 0x1EAB:
        case 0x1EAD:
            retCharacter = 'A';
            break;
            
        case 0x0044:
        case 0x0110:
        case 0x0064:
        case 0x0111:
            retCharacter = 'D';
            break;
            
        case 0x0045:
        case 0x00C9:
        case 0x00C8:
        case 0x1EBA:
        case 0x1EBC:
        case 0x1EB8:
        case 0x00CA:
        case 0x1EBE:
        case 0x1EC0:
        case 0x1EC2:
        case 0x1EC4:
        case 0x1EC6:
        case 0x0065:
        case 0x00E9:
        case 0x00E8:
        case 0x1EBB:
        case 0x1EBD:
        case 0x1EB9:
        case 0x00EA:
        case 0x1EBF:
        case 0x1EC1:
        case 0x1EC3:
        case 0x1EC5:
        case 0x1EC7:
            retCharacter = 'E';
            break;
            
        case 0x0049:
        case 0x00CD:
        case 0x00CC:
        case 0x1EC8:
        case 0x0128:
        case 0x1ECA:
        case 0x0069:
        case 0x00ED:
        case 0x00EC:
        case 0x1EC9:
        case 0x0129:
        case 0x1ECB:
            retCharacter = 'I';
            break;
            
        case 0x004F:
        case 0x00D3:
        case 0x00D2:
        case 0x1ECE:
        case 0x00D5:
        case 0x1ECC:
        case 0x00D4:
        case 0x1ED0:
        case 0x1ED2:
        case 0x1ED4:
        case 0x1ED6:
        case 0x1ED8:
        case 0x01A0:
        case 0x1EDA:
        case 0x1EDC:
        case 0x1EDE:
        case 0x1EE0:
        case 0x1EE2:
        case 0x006F:
        case 0x00F3:
        case 0x00F2:
        case 0x1ECF:
        case 0x00F5:
        case 0x1ECD:
        case 0x00F4:
        case 0x1ED1:
        case 0x1ED3:
        case 0x1ED5:
        case 0x1ED7:
        case 0x1ED9:
        case 0x01A1:
        case 0x1EDB:
        case 0x1EDD:
        case 0x1EDF:
        case 0x1EE1:
        case 0x1EE3:
            retCharacter = 'O';
            break;
            
        case 0x0055:
        case 0x00DA:
        case 0x00D9:
        case 0x1EE6:
        case 0x0168:
        case 0x1EE4:
        case 0x01AF:
        case 0x1EE8:
        case 0x1EEA:
        case 0x1EEC:
        case 0x1EEE:
        case 0x1EF0:
        case 0x0075:
        case 0x00FA:
        case 0x00F9:
        case 0x1EE7:
        case 0x0169:
        case 0x1EE5:
        case 0x01B0:
        case 0x1EE9:
        case 0x1EEB:
        case 0x1EED:
        case 0x1EEF:
        case 0x1EF1:
            retCharacter = 'U';
            break;
            
        case 0x0059:
        case 0x00DD:
        case 0x1EF2:
        case 0x1EF6:
        case 0x1EF8:
        case 0x1EF4:
        case 0x0079:
        case 0x00FD:
        case 0x1EF3:
        case 0x1EF7:
        case 0x1EF9:
        case 0x1EF5:
            retCharacter = 'Y';
            break;
            
        default:
            retCharacter = std::toupper(vChr);
            break;
    }
    return retCharacter;
    
}


int UTF8_CharEncodedLength(unsigned c) {
	if (c < 0x80) {
		// 0xxxxxxx
		return 1;
	} else if (c < (1 << 11)) {
		// 110xxxxx 10xxxxxx
		return 2;
	} else if (sizeof(wchar_t) == 2 || c < (1 << 16)) {
		// 1110xxxx 10xxxxxx 10xxxxxx
		return 3;
	} else {
		// 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
		return 4;
	}
}

void UTF8_EncodeChar(char** str, unsigned c) {
	if (c < 0x80) {
		// 0xxxxxxx
		*(*str)++ = (char)c;
	} else if (c < (1 << 11)) {
		// 110xxxxx 10xxxxxx
		*(*str)++ = 0xC0 | ((c >>  6) /*& 0x1F*/);
		*(*str)++ = 0x80 | ((c >>  0) & 0x3F);
	} else if (c < (1 << 16)) {
		// 1110xxxx 10xxxxxx 10xxxxxx
		*(*str)++ = 0xE0 | ((c >> 12) /*& 0x0F*/);
		*(*str)++ = 0x80 | ((c >>  6) & 0x3F);
		*(*str)++ = 0x80 | ((c >>  0) & 0x3F);
	} else {
		// 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
		*(*str)++ = 0xF0 | ((c >> 18) & 0x07);
		*(*str)++ = 0x80 | ((c >> 12) & 0x3F);
		*(*str)++ = 0x80 | ((c >>  6) & 0x3F);
		*(*str)++ = 0x80 | ((c >>  0) & 0x3F);
	}
}

unsigned UTF8_DecodeChar(const char** __str) {
	const unsigned char** str = (const unsigned char**)__str;
	unsigned c;
    
	if (((*str)[0] & 0x80) == 0x00) {
		// 0xxxxxxx
		c = (*str)[0] & 0x7F;
		*str += 1;
	} else if (((*str)[0] & 0xE0) == 0xC0 && ((*str)[1] & 0xC0) == 0x80) {
		// 110xxxxx 10xxxxxx
		c = ((*str)[1] & 0x3F) | ((*str)[0] & 0x1F) << 6;
		*str += 2;
	} else if (((*str)[0] & 0xF0) == 0xE0 && ((*str)[1] & 0xC0) == 0x80 && ((*str)[2] & 0xC0) == 0x80) {
		// 1110xxxx 10xxxxxx 10xxxxxx
		c = ((*str)[2] & 0x3F) | ((*str)[1] & 0x3F) << 6 | ((*str)[0] & 0x0F) << 12;
		*str += 3;
	} else if (((*str)[0] & 0xF8) == 0xF0 && ((*str)[1] & 0xC0) == 0x80 && ((*str)[2] & 0xC0) == 0x80 && ((*str)[3] & 0xC0) == 0x80) {
		// 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
		c = ((*str)[3] & 0x3F) | ((*str)[2] & 0x3F) << 6 | ((*str)[1] & 0x3F) << 12 | ((*str)[0] & 0x07) << 18;
		*str += 4;
	} else {
		// Invalid
		c = '?';
		*str += 1;
	}
	
	return c;
}

unsigned char UTF8_DecodeCharLength(const char* __str) {
	const char* str = __str;
    
	if ((str[0] & 0x80) == 0x00) {
		// 0xxxxxxx
		return 1;
	} else if ((str[0] & 0xE0) == 0xC0 && (str[1] & 0xC0) == 0x80) {
		// 110xxxxx 10xxxxxx
		return 2;
	} else if ((str[0] & 0xF0) == 0xE0 && (str[1] & 0xC0) == 0x80 && (str[2] & 0xC0) == 0x80) {
		// 1110xxxx 10xxxxxx 10xxxxxx
		return 3;
	} else if ((str[0] & 0xF8) == 0xF0 && (str[1] & 0xC0) == 0x80 && (str[2] & 0xC0) == 0x80 && (str[3] & 0xC0) == 0x80) {
		// 11110xxx 10xxxxxx 10xxxxxx 10xxxxxx
		return 4;
	} else {
		// Invalid
		return 1;
	}
}
