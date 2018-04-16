/*
 * string_utils.h
 *
 *  Created on: Jul 21, 2014
 *      Author: bhdminh
 */

#ifndef STRING_UTILS_H_
#define STRING_UTILS_H_

#include <string>

using namespace std;

extern int UTF8_CharEncodedLength(unsigned c);
extern void UTF8_EncodeChar(char** str, unsigned c);
extern unsigned UTF8_DecodeChar(const char** __str);
extern unsigned char UTF8_DecodeCharLength(const char* __str);

class StringUtils
{
private:
    char *encodeBuf;
    char *pEncodeBuf;
    unsigned processVietnameseRawChar(unsigned vChr); 
    
public:
    StringUtils();
    ~StringUtils();
	std::string getTitleRaw(string name);
	std::string getPinyin(string name);
};
#endif /* STRING_UTILS_H_ */
