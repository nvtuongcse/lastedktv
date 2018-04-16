APP_OPTIM := release

#BUILD_RELEASE_APP=karaokemp4
BUILD_RELEASE_APP=karaoke_connect

ifeq ($(BUILD_RELEASE_APP),karaoke_connect)
    APP_ABI := armeabi-v7a
else
	APP_ABI:= all
endif
#APP_ABI := armeabi armeabi-v7a
APP_PLATFORM := android-9
NDK_TOOLCHAIN_VERSION=4.9
APP_STL:=gnustl_static