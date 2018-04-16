//
//  server_url.h
//  CloudKaraoke-Dev
//
//  Created by BHDMinh on 2/17/14.
//  Copyright (c) 2014 soncamedia. All rights reserved.
//

#ifndef __CloudKaraoke_Dev__server_url__
#define __CloudKaraoke_Dev__server_url__

#include "enum_utils.h"
#include "config.h"
#include <iostream>

using namespace std;
class CServerUrl
{
public:
    char *buildServerLoginUrl();
    char *buildRequestUpdateString(const char *transId, int task);
    char *buildRequestChargeInfoString(const char *transId);
    char *buildRequestString(const char* transID, int task, int index);
    char *buildRequestString__(const char* transID, int task, int index);
    
};
#endif /* defined(__CloudKaraoke_Dev__server_url__) */
