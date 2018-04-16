
LOCAL_PATH := $(call my-dir)

#####################################################################
#            smart karaoke                                       #
#####################################################################
include $(CLEAR_VARS)

LOCAL_SRC_FILES := SmartKaraoke.cpp decrypt.cpp server_url.cpp base64.c	\
					parse_toc/KarToc.cpp parse_toc/TocMerge.cpp	\
					lib/utf8/utf8.cpp lib/utf8/gb2312.c	\
					sqlite/CharacterTokenizer/character_tokenizer.cpp \
					StringUtils.cpp \
#					sqlite/sqlite3.c  \
#					lib/expat/xmltok/xmltok.c lib/expat/xmltok/xmlrole.c lib/expat/xmlparse.c  \
#					lib/sqlite3/sqlite3.c \

LOCAL_C_INCLUDES := $(LOCAL_PATH)/parse_toc/ \
					$(LOCAL_PATH)/lib/ \
					$(LOCAL_PATH)/lib/utf8 \
					$(LOCAL_PATH)/sqlite \
					$(LOCAL_PATH)/sqlite/CharacterTokenizer \
#					$(LOCAL_PATH)/lib/expat/ \
#					$(LOCAL_PATH)/lib/expat/xmltok/ \
#					$(LOCAL_PATH)/lib/sqlite3 \

LOCAL_CFLAGS :=-D__GXX_EXPERIMENTAL_CXX0X__ -D__STDC_LIMIT_MACROS -D_FILE_OFFSET_BITS=64
LOCAL_CPPFLAGS += -fexceptions
LOCAL_CPPFLAGS += -std=c++11
LOCAL_LDLIBS := -L$(SYSROOT)/usr/lib -llog -lz

ifeq ($(BUILD_RELEASE_APP),karaokemp4)
#	LOCAL_LDLIBS += -lcrypto -lsha204
#	LOCAL_CFLAGS += -DREAD_ICBM
endif

LOCAL_CFLAGS += -DHAVE_CONFIG_H -DKHTML_NO_EXCEPTIONS -DGKWQ_NO_JAVA
LOCAL_CFLAGS += -DNO_SUPPORT_JS_BINDING -DQT_NO_WHEELEVENT -DKHTML_NO_XBL
LOCAL_CFLAGS += -DSQLITE_ENABLE_FTS3 -DSQLITE_ENABLE_FTS3_PARENTHESIS
LOCAL_CFLAGS += -U__APPLE__
LOCAL_CFLAGS += -Wno-unused-parameter -Wno-int-to-pointer-cast
LOCAL_CFLAGS += -Wno-maybe-uninitialized -Wno-parentheses
LOCAL_CPPFLAGS += -Wno-conversion-null

ifeq ($(TARGET_ARCH), arm)
	LOCAL_CFLAGS += -DPACKED="__attribute__ ((packed))"
else
	LOCAL_CFLAGS += -DPACKED=""
endif

LOCAL_SRC_FILES +=                             \
#	sqlite/android_database_SQLiteCommon.cpp     \
#	sqlite/android_database_SQLiteConnection.cpp \
#	sqlite/android_database_SQLiteGlobal.cpp     \
#	sqlite/android_database_SQLiteDebug.cpp      \
#	sqlite/JNIHelp.cpp 							\
#	sqlite/JniConstants.cpp

LOCAL_SRC_FILES += sqlite/sqlite3.c

LOCAL_C_INCLUDES += $(LOCAL_PATH) $(LOCAL_PATH)/sqlite/nativehelper/


LOCAL_MODULE    := smartkaraoke

#include $(call all-subdir-makefiles)
#include $(LOCAL_PATH)/sqlite/Android.mk
include $(BUILD_SHARED_LIBRARY)

