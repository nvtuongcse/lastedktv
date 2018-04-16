//
//  CloudKaraoke.h
//  CloudKaraoke-Dev
//
//  Created by BHDMinh on 2/17/14.
//  Copyright (c) 2014 soncamedia. All rights reserved.
//
#include "config.h"
#include <stdlib.h>
#include <math.h>
#include <string.h>
#include "enum_utils.h"

#define CLOUD_DEBUG	0
#define ENVIROMENT_ANDROID 1

#ifdef ENVIROMENT_IOS
#import <Foundation/Foundation.h>

@interface CloudKaraoke : NSObject

+ (void) getWordSize:(NSInteger) index;
+ (NSInteger) getIntValue:(NSInteger) value;
+ (NSString *) getStringValue:(NSString *) str offset:(NSInteger) offset;

+ (NSString *) buildServerLoginUrl;
+ (NSString *) buildRequestUpdateString:(NSString *)transId task:(NSInteger) task;
+ (NSString *) buildRequestChargeInfoString:(NSString *)transId;
+ (NSString *) buildRequestString:(NSString *)transId task:(NSInteger)task index:(NSInteger) index;
+ (NSString *) buildRequestString__:(NSString *)transId task:(NSInteger)task index:(NSInteger) index;
@end
#endif  // ENVIROMENT_IOS

#ifdef ENVIROMENT_ANDROID
#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif

//JNIEXPORT jboolean JNICALL
//Java_vn_com_sonca_smartkaraoke_SmartKaraoke_importTocToDatabase(JNIEnv* env,
//        jobject thiz, jstring savePath, jstring singerPath, jstring lyricPath, jstring dbPath);

JNIEXPORT jboolean JNICALL
Java_vn_com_sonca_smartkaraoke_SmartKaraoke_importTocToDatabase(JNIEnv* env,
		jobject thiz, jstring dbPath, jstring idxPath, jstring lyricPath, jstring cprightPath);

JNIEXPORT jboolean JNICALL
Java_vn_com_sonca_smartkaraoke_SmartKaraoke_extractPictureFile(JNIEnv* env,
		jobject thiz, jstring savePath, jstring picDir);

JNIEXPORT jboolean JNICALL
Java_vn_com_sonca_smartkaraoke_SmartKaraoke_importSongModel(JNIEnv* env,
		jobject thiz, jstring savePath, jint model, jintArray idList, jint size);

JNIEXPORT jint JNICALL
Java_vn_com_sonca_smartkaraoke_SmartKaraoke_mergeData(JNIEnv* env,
		jobject thiz, jstring mainPath, jstring updatePath, jint type);
JNIEXPORT jint JNICALL
Java_vn_com_sonca_smartkaraoke_SmartKaraoke_mergeDataUpdate(JNIEnv* env,
		jobject thiz, jstring mainPath, jstring subPath, jstring updatePath, jint type);

/*
JNIEXPORT void JNICALL
Java_vn_com_sonca_cloudkaraoke_CloudKaraoke_getWordSize(JNIEnv* env,
        jobject thiz, jint index);
JNIEXPORT jint JNICALL
Java_vn_com_sonca_cloudkaraoke_CloudKaraoke_getIntValue(JNIEnv* env,
        jobject thiz, jint value);
JNIEXPORT jstring JNICALL
Java_vn_com_sonca_cloudkaraoke_CloudKaraoke_getStringValue(JNIEnv* env,
        jobject thiz, jstring str, jint offset);


JNIEXPORT jstring JNICALL
Java_vn_com_sonca_cloudkaraoke_CloudKaraoke_buildRequestUpdateString(JNIEnv* env,
        jobject thiz, jstring transID, jint task);

JNIEXPORT jstring JNICALL
Java_vn_com_sonca_cloudkaraoke_CloudKaraoke_buildRequestChargeInfoString(JNIEnv* env,
		jobject thiz, jstring transID);

JNIEXPORT jstring JNICALL
Java_vn_com_sonca_cloudkaraoke_CloudKaraoke_buildRequestString(JNIEnv* env,
        jobject thiz, jstring transID, jint task, jint index);
JNIEXPORT jstring JNICALL
Java_vn_com_sonca_cloudkaraoke_CloudKaraoke_buildRequestString__(JNIEnv* env,
        jobject thiz, jstring transID, jint task, jint index);

JNIEXPORT jstring JNICALL
Java_vn_com_sonca_cloudkaraoke_CloudKaraoke_buildServerLoginUrl(JNIEnv* env, jobject thiz);

JNIEXPORT jstring JNICALL
Java_vn_com_sonca_cloudkaraoke_CloudKaraoke_buildServerValidateUrl(JNIEnv* env, jobject thiz);
*/

#ifdef __cplusplus
}
#endif
#endif // ENVIROMENT_ANDROID
