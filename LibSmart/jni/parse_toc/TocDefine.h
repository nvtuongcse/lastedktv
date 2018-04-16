//
//  TocDefine.h
//  TouchControl
//
//  Created by BHDMinh on 9/1/14.
//  Copyright (c) 2014 SoncaMedia. All rights reserved.
//

#ifndef TouchControl_TocDefine_h
#define TouchControl_TocDefine_h

enum song_type
{
    //No, Tre Trung, Thieu Nhi, Tien Chien, Dan Ca, Tinh Ca, Am Huong Dan Ca, Truyen Thong, Nhac Bolero, Tring Cong Son, Nhac Hoa Loi Viet, Nhac Anh Loi Viet, Nhac Phap Loi Viet, Tan Co Giao Duyen
    INVALID,
    TRE_TRUNG,
    THIEU_NHI, TIEN_CHIEN, DAN_CA, TINH_CA, AM_HUONG_DAN_CA, TRUYEN_THONG,
    BOLERO,
    TRINH_CONG_SON,
    NHACHOA_LOIVIET,
    NHACANH_LOIVIET,
    NHACPHAP_LOIVIET,
    TANCO_GIAODUYEN,
    NHAC_LIVESONG = 0xF9,
    NHAC_HOTSONG = 0xFA,
    NHAC_NEWVOL = 0xFB,
    NHAC_SONG_CA = 0xFC,
    NHAC_REMIX = 0xFD,
    NHAC_NEWSONGCLUB = 0xFE,
};


enum language_type
{
    VIETNAMESE,
    ENGLISH,
    FRENCH,
    CHINESE,
    PHILIPINSE,
    KOREAN,
    JAPANESE,
    LANG_OTHER = 0xFF
};

enum media_type
{
    MIDI, MP3, SINGER,VIDEO,
    TYPE_INVALID = 0x07,
};

enum song_tone
{
    TONE_MAN,
    TONE_BOTH,
    TONE_WOMAN,
};

enum song_score
{
    SCORE_OFF,
    SCORE_DUMMY,
    SCORE_PROFESTIONAL, 
};

enum SONGTYPE_ABC
{
    SONG_TYPE_NONE,
    SONG_TYPE_A,
    SONG_TYPE_B,
    SONG_TYPE_C,
};

enum play_state
{
    STOPPED, 
    PLAYING,
    PAUSED,
    DISCONNECT, 
};
#endif
