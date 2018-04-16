/*
 * debug_log.h
 *
 *  Created on: Jul 15, 2013
 *      Author: Anh Minh
 */

#ifndef DEBUG_LOG_H_
#define DEBUG_LOG_H_
#include "config.h"
#if defined(ENVIROMENT_ANDROID)
#include <android/log.h>
#define TAG "debug"
#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, TAG,__VA_ARGS__)
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG  , TAG,__VA_ARGS__)
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO   , TAG,__VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN   , TAG,__VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR  , TAG,__VA_ARGS__)
#elif defined(ENVIROMENT_IOS)
#define LOGV(stringFormat,...) printf(stringFormat, ##__VA_ARGS__)
#define LOGD(stringFormat,...) printf(stringFormat, ##__VA_ARGS__)
#define LOGI(stringFormat,...) printf(stringFormat, ##__VA_ARGS__)
#define LOGW(stringFormat,...) printf(stringFormat, ##__VA_ARGS__)
#define LOGE(stringFormat,...) printf(stringFormat, ##__VA_ARGS__)
#endif
#endif /* DEBUG_LOG_H_ */
