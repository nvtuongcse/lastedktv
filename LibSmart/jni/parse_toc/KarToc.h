//
//  KarToc.h
//  SmartKaraoke
//
//  Created by BHDMinh on 6/11/14.
//  Copyright (c) 2014 SoncaMedia. All rights reserved.
//

#ifndef __SmartKaraoke__KarToc__
#define __SmartKaraoke__KarToc__

#include <iostream>
#include <vector>
#include "TocDefine.h"
using namespace std;

#define TOC_VER_1
#define TOC_VER_2
#define TOC_VER_3
#define TOC_VER_4

#define ENCODING_TCVN 1

#define MAX_ARTIST_PER_SONG     4

#define DISK_KEY_LENGTH  32

enum
{
	DEFAULT_MIDIMP3  = 0,
	DECRYPT_MIDIMP3_SONCA,
	DECRYPT_MIDIMP3_USER
};

/*---------------------------------------------------------------------------*
 *                       TYPE COMPATIBLE DECLARATIONS                        *
 *---------------------------------------------------------------------------*/
typedef uint32_t    UINT32;
typedef uint16_t    UINT16;
typedef uint8_t     BYTE;
typedef char      SCHAR;
typedef int32_t     INT32;
typedef int16_t     INT16;


/*---------------------------------------------------------------------------*
 *                           MACRO  DECLARATIONS                             *
 *---------------------------------------------------------------------------*/
#define  Offset2Pointer(offset)  (BYTE*)(offset + (BYTE*)gpKokIndexHdr)
//#define  Offset2PointerLyricInfo(offset)  (BYTE*)(offset + (BYTE*)gpKokLyricIndexHdr)
//#define  Offset2PointerLyricPinyin(offset)  (BYTE*)(offset + (BYTE*)gpKokLyricIndexHdr + gpKokLyricIndexHdr->pPinyinTable)
//#define  Offset2PointerLyricText(offset)  (BYTE*)(offset + (BYTE*)gpKokLyricIndexHdr + gpKokLyricIndexHdr->pLyricTable)
#define IsLoadedIndexFileOk()  (gpKokIndexHdr != NULL)

#define GetAllSongNums()        (gpKokIndexHdr->uSongNums)
#define GetAllSingerNums()      (gpKokIndexHdr->uSingerNums)
#define KOK_ITEMS_MAX 0x0FFFFFFF/* Phuc changed 0xFFFFFFFF*/
#define KOK_SINGERMAX 0x0FFFFFFF/*0xFFFFFFFF*/
#define KOK_SONGMAX   0x0FFFFFFF/*0xFFFFFFFF*/

#ifdef TOC_VER_1
#define KOK_INDEXFILE_HEADER(Index_hd) \
{\
printf("Yeah,it is a Midi Index File\n");\
printf("file_len               %d\n",Index_hd->file_len);\
printf("uSingerNums            %d\n",Index_hd->uSingerNums);\
printf("uSingerInfoTableSize   %d\n",Index_hd->uSingerInfoTableSize);\
printf("pSingerInfoTable       %d\n",Index_hd->pSingerInfoTable);\
printf("uSingerNameTableSize   %d\n",Index_hd->uSingerNameTableSize);\
printf("pSingerNameTable       %d\n",Index_hd->pSingerNameTable);\
printf("uSongNums              %d\n",Index_hd->uSongNums);\
printf("uSongInfoTableSize     %d\n",Index_hd->uSongInfoTableSize);\
printf("pSongInfoTable         %d\n",Index_hd->pSongInfoTable);\
printf("uSongNameTableSize     %d\n",Index_hd->uSongNameTableSize);\
printf("pSongNameTable         %d\n",Index_hd->pSongNameTable);\
printf("uPYStrNums             %d\n",Index_hd->uPYStrNums);\
printf("uPYStrTableSize        %d\n",Index_hd->uPYStrTableSize);\
printf("pPYStrTable            %d\n",Index_hd->pPYStrTable);\
printf("uSingerNamePYStrNums             %d\n",Index_hd->uSingerNamePYStrNums);\
printf("uSingerNamePYStrTableSize        %d\n",Index_hd->uSingerNamePYStrTableSize);\
printf("pSingerNamePYStrTable            %d\n",Index_hd->pSingerNamePYStrTable);\
printf("uVideoMkvNameSize        %d\n",Index_hd->uVideoMkvNameSize);\
printf("pVideoMkvNameTable            %d\n",Index_hd->pVideoMkvNameTable);\
printf("uVideoJpegNameSize        %d\n",Index_hd->uVideoJpegNameSize);\
printf("pVideoJpegNameTable            %d\n",Index_hd->pVideoJpegNameTable);\
}
#define KOK_SONG_INFO(pSongInfo,idx) \
{\
printf("----------------- Song Info %d------------------\n",idx);\
printf("is_newsong           : %d\n",pSongInfo->property.is_newsong);\
printf("is_remix           : %d\n",pSongInfo->property.is_remix);\
printf("media_type           : %d\n",pSongInfo->property.media_type);\
printf("name_words        : %d\n",pSongInfo->property.name_words);\
printf("language_type           : %d\n",pSongInfo->property.language_type);\
printf("song_type           : %d\n",pSongInfo->property.song_type);\
printf("song_tone           : %d\n",pSongInfo->property.song_tone);\
printf("song_key           : %d\n",pSongInfo->property.song_key);\
printf("is_hide           : %d\n",pSongInfo->property.is_hide);\
printf("onetouch_type           : %d\n",pSongInfo->property.onetouch_type);\
printf("singer_idx           : %d#%d#%d#%d\n",pSongInfo->pPersonIdx.singer_idxes[0],pSongInfo->pPersonIdx.singer_idxes[1],pSongInfo->pPersonIdx.singer_idxes[2],pSongInfo->pPersonIdx.singer_idxes[3]);\
printf("author_idx           : %d#%d#%d#%d\n",pSongInfo->pPersonIdx.author_idxes[0],pSongInfo->pPersonIdx.author_idxes[1],pSongInfo->pPersonIdx.author_idxes[2],pSongInfo->pPersonIdx.author_idxes[3]);\
printf("uSongCode5          %d\n",pSongInfo->song_code5);\
printf("uSongCode6     %d\n",pSongInfo->song_code6);\
printf("pSongClips offset     :    %d\n",(pSongInfo->pSongClips));\
printf("pSongClips      :    %d\n",*(Offset2Pointer(pSongInfo->pSongClips)));\
if(pSongInfo->pPYNameOffset != 0)\
{\
printf("pPYStr     :       %s\n",Offset2Pointer(pSongInfo->pPYNameOffset));\
}\
printf("Song Name :   %s\n",Offset2Pointer(pSongInfo->pSongNameOffset));\
printf("Midi_info.stFilepos.songABCType  : %d\n",pSongInfo->MidiInfo.stFilepos.songABCType);\
printf("Midi_info.stFilepos.fileIdx  : %d\n",pSongInfo->MidiInfo.stFilepos.fileIdx);\
printf("Midi_info.stFilepos.fileOffset  : %d\n",pSongInfo->MidiInfo.stFilepos.fileOffset);\
printf("Midi_info.filesize  : %d\n",pSongInfo->MidiInfo.filesize);\
if(pSongInfo->property.media_type == VIDEO) {\
printf("VideoInfo.pVideoName offset  : %d\n",pSongInfo->VideoInfo.pVideoName);\
printf("VideoInfo.pVideoName  : %s\n",Offset2Pointer(pSongInfo->VideoInfo.pVideoName));\
printf("VideoInfo.filesize  : %d\n",pSongInfo->VideoInfo.filesize);\
}else {\
printf("Mp3Info.stFilepos.fileIdx  : %d\n",pSongInfo->Mp3Info.stFilepos.fileIdx);\
printf("Mp3Info.stFilepos.fileOffset  : %d\n",pSongInfo->Mp3Info.stFilepos.fileOffset);\
printf("Mp3Info.filesize  : %d\n",pSongInfo->Mp3Info.filesize);\
}\
}

#define KOK_SINGER_INFO(pSingerInfo,idx) \
{\
printf("----------------- Singer Info %d------------------\n",idx);\
printf("uNameWords           : %d\n",pSingerInfo->num_songs.uNameWords);\
printf("isSinger           : %d\n",pSingerInfo->num_songs.bSinger);\
printf("uSingerSongNums           : %d\n",pSingerInfo->num_songs.uSingerSongNums);\
printf("uSingerType        : %d\n",pSingerInfo->num_songs.uSingerType);\
printf("isAuthor           : %d\n",pSingerInfo->num_songs.bAuthor);\
printf("uAuthorSongNums           : %d\n",pSingerInfo->num_songs.uAuthorSongNums);\
printf("langugae_type           : %d\n",pSingerInfo->num_songs.language_type);\
/*printf("pPictureIdx           : %d\n",pSingerInfo->picIdx);*/\
printf("pPictureIdxMobile           : %d\n",pSingerInfo->singerIdx);\
if(pSingerInfo->pPYOffset != 0)\
{\
printf("pPYStr     :       %s\n",Offset2Pointer(pSingerInfo->pPYOffset));\
}\
printf("Singer Name :   %s\n",Offset2Pointer(pSingerInfo->pNameOffset));\
}

#define KOK_LYRIC_INFO(pLyricInfo,idx) \
{\
printf("----------------- Song Lyric Info %d------------------\n",idx);\
printf("songCode5           : %d\n",pLyricInfo->songCode5);\
printf("pLyricPinyinOffset           : %d\n",pLyricInfo->pLyricPinyinOffset);\
printf("pLyricTextOffset           : %d\n",pLyricInfo->pLyricTextOffset);\
if(pLyricInfo->pLyricPinyinOffset != 0)\
{\
printf("pPYStr     :       %s\n",Offset2PointerLyricPinyin(pLyricInfo->pLyricPinyinOffset));\
}\
printf("Lyric Text:   %s\n",Offset2PointerLyricText(pLyricInfo->pLyricTextOffset));\
}

#else

#define KOK_INDEXFILE_HEADER(Index_hd) \
{\
	printf("Yeah,it is a Midi Index File\n");\
	printf("file_len               %d\n",Index_hd->file_len);\
	printf("uSingerNums            %d\n",Index_hd->uSingerNums);\
	printf("uSingerInfoTableSize   %d\n",Index_hd->uSingerInfoTableSize);\
	printf("pSingerInfoTable       %d\n",Index_hd->pSingerInfoTable);\
	printf("uSingerNameTableSize   %d\n",Index_hd->uSingerNameTableSize);\
	printf("pSingerNameTable       %d\n",Index_hd->pSingerNameTable);\
	printf("uSongNums              %d\n",Index_hd->uSongNums);\
	printf("uSongInfoTableSize     %d\n",Index_hd->uSongInfoTableSize);\
	printf("pSongInfoTable         %d\n",Index_hd->pSongInfoTable);\
	printf("uSongNameTableSize     %d\n",Index_hd->uSongNameTableSize);\
	printf("pSongNameTable         %d\n",Index_hd->pSongNameTable);\
	printf("uPYStrNums             %d\n",Index_hd->uPYStrNums);\
	printf("uPYStrTableSize        %d\n",Index_hd->uPYStrTableSize);\
	printf("pPYStrTable            %d\n",Index_hd->pPYStrTable);\
	printf("uSingerNamePYStrNums             %d\n",Index_hd->uSingerNamePYStrNums);\
	printf("uSingerNamePYStrTableSize        %d\n",Index_hd->uSingerNamePYStrTableSize);\
	printf("pSingerNamePYStrTable            %d\n",Index_hd->pSingerNamePYStrTable);\
	printf("uSongCodeNums          %d\n",Index_hd->uSongCodeNums);\
	printf("uSongCodeTableSize     %d\n",Index_hd->uSongCodeTableSize);\
	printf("pSongCodeTable         %d\n",Index_hd->pSongCodeTable);\
}
#define KOK_SONG_INFO(pSongInfo,idx) \
{\
	printf("----------------- Song Info %d------------------\n",idx);\
	printf("singer_idx           : %d\n",pSongInfo->property.singer_idx);\
	printf("name_words        : %d\n",pSongInfo->property.name_words);\
	printf("is_top100           : %d\n",pSongInfo->property.is_top100);\
	printf("is_newsong	         : %d\n",pSongInfo->property.is_newsong);\
	printf("language_type     : %d\n",pSongInfo->property.language_type);\
	printf("song_type           : %d\n",pSongInfo->property.song_type);\
	if(pSongInfo->pPYStr.len)\
	{\
		printf("pPYStr     :       %s\n",Offset2Pointer(pSongInfo->pPYStr.offset));\
	}\
	printf("Song Name :   %s\n",Offset2Pointer(pSongInfo->pSongName.offset));\
	printf("Midi_info.filepos  : %d\n",pSongInfo->filepos);\
	printf("Midi_info.filesize  : %d\n",pSongInfo->filesize);\
}

#define KOK_SINGER_INFO(pSingerInfo,idx) \
{\
	printf("----------------- Singer Info %d------------------\n",idx);\
	printf("uNameWords           : %d\n",pSingerInfo->uNameWords);\
	printf("uSingerType        : %d\n",pSingerInfo->uSingerType);\
	printf("uSongNums           : %d\n",pSingerInfo->uSongNums);\
	if(pSingerInfo->pPYStr.len)\
	{\
		printf("pPYStr     :       %s\n",Offset2Pointer(pSingerInfo->pPYStr.offset));\
	}\
	printf("Singer Name :   %s\n",Offset2Pointer(pSingerInfo->pSingerName.offset));\
}

#define KOK_LYRIC_INFO(pLyricInfo,idx)\
{\
}\

#endif
/*---------------------------------------------------------------------------*
 *                         STRUCTURE   DECLARATIONS                          *
 *---------------------------------------------------------------------------*/

#ifdef TOC_VER_1
// lyric file header structure
typedef struct
{
    uint32_t songCode5;
    uint32_t pLyricPinyinOffset;
    uint32_t pLyricTextOffset; // point to start of the zipped lyric text
    uint32_t uLyricTextLen; // Len of the zipped lyric text
} Lyric_Info_S;

typedef struct
{
    uint32_t pLyricTextOffset;
} LyricWord_Info_S;

typedef struct
{
    struct {
        unsigned typeABC : 8;
        unsigned index5;
    } songID;
    
    char* lyricText;
} Lyric_10Word_Info_S;

typedef struct{
    UINT32 magic; //"F10W"
    uint32_t uLyricInfoCount; // So luong loi bai hat full
    UINT32 pLyricInfoTable; // vi tri bat dau cua bang lyricInfo full
    UINT32 pPinyinTable;    // Vi tri bat dau cua bang pinyin full
    UINT32 pLyricTable;     // Vi tri bat dau cua bang lyric full
#ifdef TOC_VER_2
    uint32_t pLyric10WordInfoTable;// vi tri bat dau cua bang lyricInfo 10 words
    uint32_t pFirst10WordLyricTable;
#endif
}Kok_LyrcFileHdr_S;

//midi index file header structure;
typedef struct{
	struct {
        uint32_t Index;     //ASCI "INDEX"
        
        // DISC, //Vol main
        UINT16 Version;     // 52, 53, ...
        UINT16 Revision;    // 01, 02, ...
#ifdef TOC_VER_4
        //DISC_UPDATE, Vol Revisions
        UINT16 Version1;     // 52, 53, ...
        UINT16 Revision1;    // 01, 02, ...
        
        //KTV_USER, //XUSER : Sonca release illegal
        UINT16 Version2;     // 52, 53, ...
        UINT16 Revision2;    // 01, 02, ...
        
        // USER, //User update themselves
        UINT16 Version3;     // 52, 53, ...
        UINT16 Revision3;    // 01, 02, ...
#endif
    } Magic;
    
	uint32_t file_len;
    
	uint16_t uSingerNums;           // singer + author
	uint32_t uSingerInfoTableSize;  // singer + author
	uint32_t pSingerInfoTable;      // singer + author
    
	uint32_t uSongNums;                 // total song nums
	uint32_t uSongInfoTableSize;        //bytes of the table
	uint32_t pSongInfoTable;            //offset of the pos Song_Info_S table
    
    uint32_t uSingerNameTableSize;
	uint32_t pSingerNameTable;          //offset of the name string tabel,NULL Terminated
    
	uint32_t uSongNameTableSize;
	uint32_t pSongNameTable;            //offset of the name string tabel,NULL Terminated
    
	uint16_t uPYStrNums;                //
	uint32_t uPYStrTableSize;
	uint32_t pPYStrTable;
    
	uint16_t uSingerNamePYStrNums;		//
	uint32_t uSingerNamePYStrTableSize;
	uint32_t pSingerNamePYStrTable;
    
    UINT32 uVideoMkvNameSize;           //use for mkv karaoke
    UINT32 pVideoMkvNameTable;
    
    UINT32 uVideoJpegNameSize;          //  do dai bang video clip name
    UINT32 pVideoJpegNameTable;         //  con tro de bang video clip name ( ket thuc = null)
    
//	uint16_t uSongCodeNums;       //
//	uint32_t uSongCodeTableSize;
//	uint32_t pSongCodeTable;
//    
//	uint16_t SongNameWordsNums[10];
//	uint16_t SingerType_Nums[6];
}Kok_IdxFileHdr_S;

typedef struct{
	struct {
		unsigned uNameWords  		: 4; //so word cua ten singer
		unsigned bSinger  			: 1;
		unsigned bAuthor  			: 1;
		unsigned uSingerType 		: 4; //not use
		unsigned uSingerSongNums   	: 22;//so bai hat cua 1 person

		unsigned language_type		: 8;        // So ngon ngu
		unsigned uAuthorSongNums   	: 22;//so bai hat cua 1 person
		unsigned reserved			: 2;
	}num_songs;

    char *pNameOffset;
    char *pPYOffset;
    uint32_t singerIdx;
    uint32_t insertIndex;
}Person_Info_Update_S;

typedef struct{
#ifdef TOC_VER_2
	struct {
		unsigned uNameWords  		: 4; //so word cua ten singer
		unsigned bSinger  			: 1;
		unsigned bAuthor  			: 1;
		unsigned uSingerType 		: 4; //not use
		unsigned uSingerSongNums   	: 22;//so bai hat cua 1 person

		unsigned language_type		: 8;        // So ngon ngu
		unsigned uAuthorSongNums   	: 22;//so bai hat cua 1 person
		unsigned reserved			: 2;
	}num_songs;
#else
    struct {
        unsigned uNameWords  : 4; //so word cua ten singer
        unsigned bSinger  : 1;
        unsigned uSingerSongNums   : 27;//so bai hat cua 1 person
        unsigned uSingerType : 4; //not use
        unsigned bAuthor  : 1;
        unsigned uAuthorSongNums   : 27;//so bai hat cua 1 person
    }num_songs;
#endif
    uint32_t pNameOffset;
    uint32_t pPYOffset;
#ifdef TOC_VER_4
    uint32_t singerIdx; 
#else
    uint32_t pPictureOffset; // 2000.bin
    uint32_t pictureSize;
#endif
}Person_Info_S;

typedef struct{
	struct {
		unsigned is_newsong     : 1;
		unsigned is_remix       : 1;
		unsigned media_type     : 2;        // MIDI, MP3, SINGER, VIDEO
		unsigned name_words     : 4;        // Chi xai cho tieng Hoa: 1..10
		unsigned language_type	: 8;        // So ngon ngu
		unsigned song_type      : 4;        // So the loai: {No, Tre Trung, Thieu Nhi, Tien Chien, Dan Ca, Tinh Ca, Am Huong Dan Ca, Truyen Thong, Nhac Bolero, Tring Cong Son, Nhac Hoa Loi Viet, Nhac Anh Loi Viet, Nhac Phap Loi Viet, Tan Co Giao Duyen}
        unsigned song_tone      : 2;        // Man, Woman, Both
        unsigned song_key       : 5;        // major(0)/minor(1) = [0/1], key [-6, 6] = 1.1111
        unsigned is_hide  		: 1; 		//not show in search and press digits, just use for OneTouch only
        unsigned onetouch_type 	: 4; 		//"Normal Karaoke", "Piano", "Dance", bolero, ...
	}property;

    struct{
		uint16_t singer_idxes[MAX_ARTIST_PER_SONG];
		uint16_t author_idxes[MAX_ARTIST_PER_SONG];
    }pPersonIdx;

    char * pSongNameOffset;
    char * pPYNameOffset;
    char * pLyricOffset;

    union {
        struct {
            struct {
                unsigned fileIdx   : 8; //so file MEGVOL
                unsigned fileOffset: 24; //mp3 offset or video offset of table
            }stFilepos;
            uint32_t  filesize;
        }Mp3Info;

        struct {
            uint32_t pVideoName;
            uint32_t  filesize;
        }VideoInfo;
    };

    struct {
        struct {
            unsigned songABCType : 4; // ABC song type: A, B, C
            unsigned fileIdx   : 4; //so file MEGVOL
            unsigned fileOffset: 24; //mp3 offset or video offset of table
        }stFilepos;
        uint32_t  filesize;
    }MidiInfo;

    uint32_t song_code5;
    uint32_t song_code6;
    uint8_t * pSongClips; //ma video/jpeg

}Song_Info_Update_S;

//type of the midi file info structure;
typedef struct{
	struct {
		unsigned is_newsong     : 1;
		unsigned is_remix       : 1;
		unsigned media_type     : 2;        // MIDI, MP3, SINGER, VIDEO
		unsigned name_words     : 4;        // Chi xai cho tieng Hoa: 1..10
		unsigned language_type	: 8;        // So ngon ngu
		unsigned song_type      : 4;        // So the loai: {No, Tre Trung, Thieu Nhi, Tien Chien, Dan Ca, Tinh Ca, Am Huong Dan Ca, Truyen Thong, Nhac Bolero, Tring Cong Son, Nhac Hoa Loi Viet, Nhac Anh Loi Viet, Nhac Phap Loi Viet, Tan Co Giao Duyen}
        unsigned song_tone      : 2;        // Man, Woman, Both
        unsigned song_key       : 5;        // major(0)/minor(1) = [0/1], key [-6, 6] = 1.1111
        unsigned is_hide  		: 1; 		//not show in search and press digits, just use for OneTouch only
        unsigned onetouch_type 	: 4; 		//"Normal Karaoke", "Piano", "Dance", bolero, ...
	}property;
    
    struct{
#ifdef TOC_VER_3
    uint16_t singer_idxes[MAX_ARTIST_PER_SONG];
    uint16_t author_idxes[MAX_ARTIST_PER_SONG];
#else
        unsigned singer_idx   : 16; //So thu tu cua singer trong bang singer Info
        unsigned author_idx  : 16;
#endif
    }pPersonIdx;
    
    uint32_t pSongNameOffset;
    uint32_t pPYNameOffset;
    
    union {
        struct {
            struct {
                unsigned fileIdx   : 8; //so file MEGVOL
                unsigned fileOffset: 24; //mp3 offset or video offset of table
            }stFilepos;
            uint32_t  filesize;
        }Mp3Info;
        
        struct {
            uint32_t pVideoName;
            uint32_t  filesize;
        }VideoInfo;
    };
    
    struct {
#ifdef TOC_VER_4
        struct {
            unsigned songABCType : 4; // ABC song type: A, B, C
            unsigned fileIdx   : 4; //so file MEGVOL
            unsigned fileOffset: 24; //mp3 offset or video offset of table
        }stFilepos;
#else
        uint32_t  filepos;
#endif
        uint32_t  filesize;
    }MidiInfo;
    
    uint32_t song_code5;
    uint32_t song_code6;
    uint32_t pSongClips; //ma video/jpeg

}Song_Info_S;

typedef struct{
    uint32_t uiTypeIdx;
    char*  pTypeStr;
}Singer_Type_S;

typedef struct{
    unsigned bIsHotSong     : 1;
    unsigned bNewSongClub   : 1;
    unsigned bCriticalSong  : 1;
    unsigned bCoupleSingers : 1;
    unsigned bDelete        : 1;
    unsigned bVocalMp3Mkv   : 1;
    unsigned bUser          : 1;
    unsigned bNewTocFormat  : 1;
} Song_Info_Extra_S;

typedef struct{
    unsigned reserved       : 3;
    unsigned bNewDownload   : 1;
    unsigned bDownload      : 1;
    unsigned bLiveSong      : 1;
    unsigned bMp3VocalFile  : 1;
    unsigned bNewTocFormat  : 1;
} Song_Info_Extra1_S;


#else // First Version
typedef struct{
    unsigned uNameWords  : 4;
    unsigned uSingerType : 4;
    unsigned uSongNums   : 24;
    struct{
        unsigned len   : 8;
        unsigned offset: 24;
    }pSingerName;
    
    struct{
        unsigned len   : 8;
        unsigned offset: 24;
    }pPYStr;
}Singer_Info_S;

//midi index file header structure;
typedef struct{
	uint32_t Magic;//ASCI "Indx"
	uint32_t file_len;
    
	uint16_t uSingerNums;
	uint32_t uSingerInfoTableSize;
	uint32_t pSingerInfoTable;
    
	uint32_t uSingerNameTableSize;
	uint32_t pSingerNameTable; //offset of the name string tabel,NULL Terminated
    
	uint16_t uSongNums;               // total song nums
	uint32_t uSongInfoTableSize; //bytes of the table
	uint32_t pSongInfoTable; //offset of the pos Song_Info_S table
    
	uint32_t uSongNameTableSize;
	uint32_t pSongNameTable; //offset of the name string tabel,NULL Terminated
    
	uint16_t uPYStrNums;      //
	uint32_t uPYStrTableSize;
	uint32_t pPYStrTable;
    
	uint16_t uSingerNamePYStrNums;		//
	uint32_t uSingerNamePYStrTableSize;
	uint32_t pSingerNamePYStrTable;
    
	uint16_t uSongCodeNums;       //
	uint32_t uSongCodeTableSize;
	uint32_t pSongCodeTable;
    
	uint16_t SongNameWordsNums[10];
	uint16_t SingerType_Nums[6];
}Kok_IdxFileHdr_S;

//type of the midi file info structure;
typedef struct{
	struct {
		unsigned is_top100	: 1;
		unsigned is_newsong	: 1;
		unsigned name_words	: 4;
		unsigned singer_idx	: 12;// max 4096
		unsigned language_type	: 4;
		unsigned song_type	: 3;
		unsigned song_invalid	: 1;
		unsigned myfavor_times	: 6;
	}property;
    
	struct{
		unsigned len   : 8;
		unsigned offset: 24;
	}pSongName;
    
	struct{
		unsigned len   : 8;
		unsigned offset: 24;
	}pPYStr;
	struct{
#ifdef SUPPORT_MULTI_VOCAL_FILE
		struct{
			unsigned fileIdx   : 8;
			unsigned fileOffset: 24;
		}stFilepos;
#else
		uint32_t  filepos;
#endif
		//UINT16  filesize;
		uint32_t  filesize;
	}Mp3_info;
	uint16_t  filepos;
	//UINT16  filesize;
	uint32_t  filesize;
	struct{
		unsigned code5: 24;
		unsigned singer_vocal: 8; // have singer vocal or not
	}sonca_info;
	uint32_t 	song_code6;
}Song_Info_S;

typedef struct{
    uint32_t uiTypeIdx;
    char*  pTypeStr;
}Singer_Type_S;
#endif
class KarToc
{
    char *pMem;
    
    vector<Lyric_10Word_Info_S> lyr10WordInfo;
    
    char *pLyricMem;
    Kok_LyrcFileHdr_S *gpKokLyricIndexHdr;
//    uint32_t *pSongCode5;
//    char * pLyricPinyin;
//    char * pLyricText;
    
    int indexOfSongIdLyric(uint32_t id, uint8_t type); 
    
public:
    int iPasswordStreamPos;
    static const uint8_t disk_keys[DISK_KEY_LENGTH];
    
    KarToc();
    ~KarToc();

    char parse_file_header(const char * file);
    char parse_file(const char *pFileName);
    char parse_data(const char *pData, uint32_t len);
    char * offsetToPointer(uint32_t offset);
    char * offsetToPointerAbs(char* start, uint32_t offset);
    //char * offsetToPointerLyricInfo(uint32_t offset);
    //char * offsetToPointerLyricPinyin(uint32_t offset);
    //char * offsetToPointerLyricText(uint32_t offset);
    
    char parseLyricToc(const char *pFilePath);
    char parseLyric10Words(const char *pFilePath);
    char* getLyric10Word(uint32_t id, uint8_t type);
    
    Song_Info_S * getSongInfo(UINT32 idx);
    Song_Info_Extra_S *getSongInfoExtra(UINT32 idx);
    Song_Info_Extra1_S *getSongInfoExtra1(UINT32 idx);
    
    Kok_IdxFileHdr_S * getHeader();

    int maxMegvolIdx;
    int maxMegMidIdx;
    Kok_IdxFileHdr_S *gpKokIndexHdr;
    Person_Info_S * pArtistInfo;
    Song_Info_S *pSongInfo;
    uint8_t *pSongName;
    uint8_t *pSongPyName;
    uint8_t *pSingerName;
    uint8_t *pSingerPYName;
    uint8_t *pVideoMKVName;
    uint8_t *pVideoJPGName;
    uint32_t *pMapIndex;

#ifdef TOC_VER_1
    Person_Info_S * getAuthorInfo(uint32_t idx);
    Person_Info_S * getSingerInfo(uint32_t idx);
    Lyric_Info_S * getLyricInfo(uint32_t idx);
    
    char* getLyric10WordsTable();
    char * getLyricTextTable();
    char * getLyricPinyinTable();
    LyricWord_Info_S* getFirstWordsLyric(uint32_t uSongIndex);
#else
    Singer_Info_S * getSingerInfo(UINT32 idx);
#endif

    bool isNewFormat();

    uint32_t getVersion();
    uint32_t getVersion1();
    uint32_t getVersion2();
    uint32_t getVersion3();
    
    uint16_t getSongCount();
    uint16_t getSingerCount();
    uint16_t getLyricInfoCount();
    
    void getMaxMegFileIdx();
    
    void PasswordMaskBytes(BYTE *nBytesSrc, int offset, int sz, char* strPwd);
//    void PasswordMaskBytesIdx(BYTE *nBytesSrc, int offset, int sz, char* Key);
    void PasswordMaskBytesIdx(BYTE *nBytesSrc, UINT32 offset, UINT32 sz, char* Key, bool isEncrypted);
    void PasswordMaskBytesMp3(BYTE *nBytesSrc, int offset, int sz, char* strPwd);
    void PasswordMaskBytesUser(BYTE *nBytesSrc, int offset, int sz);
    BYTE DetectMidiMp3Format(BYTE *pData);
};
#endif /* defined(__SmartKaraoke__KarToc__) */
