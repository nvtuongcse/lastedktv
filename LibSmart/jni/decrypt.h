//
//  decrypt.h
//  CloudKaraoke-Dev
//
//  Created by BHDMinh on 2/14/14.
//  Copyright (c) 2014 soncamedia. All rights reserved.
//

#ifndef __CloudKaraoke__decrypt__
#define __CloudKaraoke__decrypt__

#include <iostream>
#include <stdlib.h>
#include <stdio.h>

using namespace std;
class CDecryptLyric
{
private:
    uint32_t nPasswordInt32;
    uint32_t nPasswordStr;
    
public:
    void getWordSize(int index);
    uint32_t getInt32(int value);
    char* getStringValue(const char* str, int offset);
};

#endif /* defined(__CloudKaraoke__decrypt__) */
