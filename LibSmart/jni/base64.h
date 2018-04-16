/*
 * base64.h
 *
 *  Created on: Jul 15, 2013
 *      Author: Anh Minh
 */

#ifndef BASE64_H_
#define BASE64_H_
#include <stdio.h>

#include <string.h>

//typedef unsigned int size_t;

#define ERR_BASE64_BUFFER_TOO_SMALL               -0x002A
#define ERR_BASE64_INVALID_CHARACTER              -0x002C
#ifdef __cplusplus
extern "C" {
#endif

int base64_encode( unsigned char *dst, size_t *dlen,
                   const unsigned char *src, size_t slen );

int base64_decode( unsigned char *dst, size_t *dlen,
                   const unsigned char *src, size_t slen );

int base64_self_test( int verbose );

#ifdef __cplusplus
}
#endif

#endif /* BASE64_H_ */
