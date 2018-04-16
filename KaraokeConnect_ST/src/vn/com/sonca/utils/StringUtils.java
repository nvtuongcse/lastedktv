package vn.com.sonca.utils;

import vn.com.sonca.utils.AppConfig.LANG_INDEX;


public class StringUtils {
	public static String getRawString(String aStr, LANG_INDEX langIndex)
	{
		if(langIndex == LANG_INDEX.CHINESE || langIndex == LANG_INDEX.HINDI) { 
			return getPinyinString(aStr, langIndex); 
		}
		
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < aStr.length(); i++)
		{
			sb.append(processVietnameseRawChar(aStr.charAt(i)));
		}
		return sb.toString().toUpperCase(AppConfig.curLocale); 
	}

	private static boolean isContainObject(char[] arr, char aChr)
	{
		for (char c : arr) {
			if(c == aChr) 
				return true; 
		}
		return false; 
	}

	public static String getPinyinString(String aStr, LANG_INDEX langIndex) 
	{
		String shortName = "";
		char chr = ' ';

		char[] specialChar = { '-', '(', ')', '{', '[', '&', ';' };

		//// Use this method to convert a chine to PINYIN
		//name = convertChineseToPinyin(name);

		aStr = aStr.trim(); 
		String[] nWords = aStr.split(" "); 

		if(langIndex == LANG_INDEX.CHINESE || langIndex == LANG_INDEX.HINDI) { 
			return String.valueOf(nWords.length); 
		}
		
		
		//        String[] nWords = name.Split(new String[] { " " }, StringSplitOptions.RemoveEmptyEntries);
		//if ((nWords.Length == 1) && (nWords[0].Length < 8))
		//{
		//    string fWord = nWords[0];
		//    for (int idx = 0; idx < name.Length; idx++)
		//    {
		//        shortName += processVietnameseChar(name[idx]);
		//    }
		//}
		//else
		{
			for (int idx = 0; idx < nWords.length; idx++)
			{
				if (nWords[idx].charAt(0) == '(')
					break;
				for (int i = 0; i < nWords[idx].length(); i++)
				{
					chr = nWords[idx].charAt(i);

					// Process some special character
					if (isContainObject(specialChar, chr)) continue;

					shortName += processVietnameseChar(chr);
					break;
				}
			}
		}

		return (shortName.toUpperCase(AppConfig.curLocale));
	}
	private static char processVietnameseRawChar(char vChr)
	{
		//        char[] numberChar = { 'K', 'M', 'H', 'B', 'B', 'N', 'S', 'B', 'T', 'C' };
		char retCharacter = vChr;

		//if ((vChr >= '0') && (vChr <= '9'))
		//{
		//    return numberChar[vChr - '0'];
		//}
		switch (vChr)
		{
		case 'A':
		case 'Á':
		case 'À':
		case 'Ả':
		case 'Ã':
		case 'Ạ':
		case 'Ă':
		case 'Ắ':
		case 'Ằ':
		case 'Ẳ':
		case 'Ẵ':
		case 'Ặ':
		case 'Â':
		case 'Ấ':
		case 'Ầ':
		case 'Ẩ':
		case 'Ẫ':
		case 'Ậ':
		case 'a':
		case 'á':
		case 'à':
		case 'ả':
		case 'ã':
		case 'ạ':
		case 'ă':
		case 'ắ':
		case 'ằ':
		case 'ẳ':
		case 'ẵ':
		case 'ặ':
		case 'â':
		case 'ấ':
		case 'ầ':
		case 'ẩ':
		case 'ẫ':
		case 'ậ':
			retCharacter = 'A';
			break;

		case 'Đ':
		case 'đ':
			retCharacter = 'D';
			break;

		case 'E':
		case 'É':
		case 'È':
		case 'Ẻ':
		case 'Ẽ':
		case 'Ẹ':
		case 'Ê':
		case 'Ế':
		case 'Ề':
		case 'Ể':
		case 'Ễ':
		case 'Ệ':
		case 'e':
		case 'é':
		case 'è':
		case 'ẻ':
		case 'ẽ':
		case 'ẹ':
		case 'ê':
		case 'ế':
		case 'ề':
		case 'ể':
		case 'ễ':
		case 'ệ':
			retCharacter = 'E';
			break;

		case 'I':
		case 'Í':
		case 'Ì':
		case 'Ỉ':
		case 'Ĩ':
		case 'Ị':
		case 'i':
		case 'í':
		case 'ì':
		case 'ỉ':
		case 'ĩ':
		case 'ị':
			retCharacter = 'I';
			break;

		case 'O':
		case 'Ó':
		case 'Ò':
		case 'Ỏ':
		case 'Õ':
		case 'Ọ':
		case 'Ô':
		case 'Ố':
		case 'Ồ':
		case 'Ổ':
		case 'Ỗ':
		case 'Ộ':
		case 'Ơ':
		case 'Ớ':
		case 'Ờ':
		case 'Ở':
		case 'Ỡ':
		case 'Ợ':
		case 'o':
		case 'ó':
		case 'ò':
		case 'ỏ':
		case 'õ':
		case 'ọ':
		case 'ô':
		case 'ố':
		case 'ồ':
		case 'ổ':
		case 'ỗ':
		case 'ộ':
		case 'ơ':
		case 'ớ':
		case 'ờ':
		case 'ở':
		case 'ỡ':
		case 'ợ':
			retCharacter = 'O';
			break;

		case 'U':
		case 'Ú':
		case 'Ù':
		case 'Ủ':
		case 'Ũ':
		case 'Ụ':
		case 'Ư':
		case 'Ứ':
		case 'Ừ':
		case 'Ử':
		case 'Ữ':
		case 'Ự':
		case 'u':
		case 'ú':
		case 'ù':
		case 'ủ':
		case 'ũ':
		case 'ụ':
		case 'ư':
		case 'ứ':
		case 'ừ':
		case 'ử':
		case 'ữ':
		case 'ự':
			retCharacter = 'U';
			break;

		case 'Y':
		case 'Ý':
		case 'Ỳ':
		case 'Ỷ':
		case 'Ỹ':
		case 'Ỵ':
		case 'y':
		case 'ý':
		case 'ỳ':
		case 'ỷ':
		case 'ỹ':
		case 'ỵ':
			retCharacter = 'Y';
			break;

		default:
			break;
		}
		return retCharacter;

	}

	private static char processVietnameseChar(char vChr)
	{
		//        char[] numberChar = { 'K', 'M', 'H', 'B', 'B', 'N', 'S', 'B', 'T', 'C' };
		char retCharacter = vChr;

		// Process number char
		//if ((vChr >= '0') && (vChr <= '9'))
		//{
		//    return numberChar[vChr - '0'];
		//}
		switch (vChr)
		{
		case 'A':
		case 'Á':
		case 'À':
		case 'Ả':
		case 'Ã':
		case 'Ạ':
		case 'Ă':
		case 'Ắ':
		case 'Ằ':
		case 'Ẳ':
		case 'Ẵ':
		case 'Ặ':
		case 'Â':
		case 'Ấ':
		case 'Ầ':
		case 'Ẩ':
		case 'Ẫ':
		case 'Ậ':
		case 'a':
		case 'á':
		case 'à':
		case 'ả':
		case 'ã':
		case 'ạ':
		case 'ă':
		case 'ắ':
		case 'ằ':
		case 'ẳ':
		case 'ẵ':
		case 'ặ':
		case 'â':
		case 'ấ':
		case 'ầ':
		case 'ẩ':
		case 'ẫ':
		case 'ậ':
			retCharacter = 'A';
			break;

		case 'Đ':
		case 'đ':
			retCharacter = 'D';
			break;

		case 'E':
		case 'É':
		case 'È':
		case 'Ẻ':
		case 'Ẽ':
		case 'Ẹ':
		case 'Ê':
		case 'Ế':
		case 'Ề':
		case 'Ể':
		case 'Ễ':
		case 'Ệ':
		case 'e':
		case 'é':
		case 'è':
		case 'ẻ':
		case 'ẽ':
		case 'ẹ':
		case 'ê':
		case 'ế':
		case 'ề':
		case 'ể':
		case 'ễ':
		case 'ệ':
			retCharacter = 'E';
			break;

		case 'I':
		case 'Í':
		case 'Ì':
		case 'Ỉ':
		case 'Ĩ':
		case 'Ị':
		case 'i':
		case 'í':
		case 'ì':
		case 'ỉ':
		case 'ĩ':
		case 'ị':
			retCharacter = 'I';
			break;

		case 'O':
		case 'Ó':
		case 'Ò':
		case 'Ỏ':
		case 'Õ':
		case 'Ọ':
		case 'Ô':
		case 'Ố':
		case 'Ồ':
		case 'Ổ':
		case 'Ỗ':
		case 'Ộ':
		case 'Ơ':
		case 'Ớ':
		case 'Ờ':
		case 'Ở':
		case 'Ỡ':
		case 'Ợ':
		case 'o':
		case 'ó':
		case 'ò':
		case 'ỏ':
		case 'õ':
		case 'ọ':
		case 'ô':
		case 'ố':
		case 'ồ':
		case 'ổ':
		case 'ỗ':
		case 'ộ':
		case 'ơ':
		case 'ớ':
		case 'ờ':
		case 'ở':
		case 'ỡ':
		case 'ợ':
			retCharacter = 'O';
			break;

		case 'U':
		case 'Ú':
		case 'Ù':
		case 'Ủ':
		case 'Ũ':
		case 'Ụ':
		case 'Ư':
		case 'Ứ':
		case 'Ừ':
		case 'Ử':
		case 'Ữ':
		case 'Ự':
		case 'u':
		case 'ú':
		case 'ù':
		case 'ủ':
		case 'ũ':
		case 'ụ':
		case 'ư':
		case 'ứ':
		case 'ừ':
		case 'ử':
		case 'ữ':
		case 'ự':
			retCharacter = 'U';
			break;

		case 'Y':
		case 'Ý':
		case 'Ỳ':
		case 'Ỷ':
		case 'Ỹ':
		case 'Ỵ':
		case 'y':
		case 'ý':
		case 'ỳ':
		case 'ỷ':
		case 'ỹ':
		case 'ỵ':
			retCharacter = 'Y';
			break;

		default:
			break;
		}
		return retCharacter;
	}
/*
	private static char[] unicode_group = new char[] //12_line x 6_col
			{	
		'a', 'á', 'à', 'ả', 'ã', 'ạ',
		'ă', 'ắ', 'ằ', 'ẳ', 'ẵ', 'ặ',
		'â', 'ấ', 'ầ', 'ẩ', 'ẫ', 'ậ',			
		'e', 'é', 'è', 'ẻ', 'ẽ', 'ẹ',
		'ê', 'ế', 'ề', 'ể', 'ễ', 'ệ',
		'i', 'í', 'ì', 'ỉ', 'ĩ', 'ị',
		'o', 'ó', 'ò', 'ỏ', 'õ', 'ọ',
		'ô', 'ố', 'ồ', 'ổ', 'ỗ', 'ộ',
		'ơ', 'ớ', 'ờ', 'ở', 'ỡ', 'ợ',
		'u', 'ú', 'ù', 'ủ', 'ũ', 'ụ',
		'ư', 'ứ', 'ừ', 'ử', 'ữ', 'ự',
		'y', 'ý', 'ỳ', 'ỷ', 'ỹ', 'ỵ'
			};

	private static char[] unsign_unicode_group = new char[] //4_line x 10_col + 3
			{
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
		'a', 'ă', 'â', 'b', 'c', 'd', 'đ', 'e', 'ê', 'f', 
		'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'ô', 
		'ơ', 'p', 'q', 'r', 's', 't', 'u', 'ư', 'v', 'w', 
		'x', 'y', 'z'
			};

	private static char[] special_group = new char[] //3_line x 10_col + 4
			{
		'!', '\"', '#', '$', '%', '&', '\'', '(', ')', '*', 
		'+', ',', '-', '.', '/', ':', ';', '<', '=', '>', 
		'?', '@', '[', '\\', ']', '^', '_', '`', '{', '|', 
		'}', '~', '"', ' '
			};
*/
	/// <summary>
	/// Compare string between src and dst, means return (src-dst). 
	/// It has 2 step : compare unsiged-char and signed-char. 
	/// Before comparing, src and dst must be lower string.
	/// </summary>
	/// <param name="src">Source string in lower case (src.ToLower())</param>
	/// <param name="dst">Destination string in lower case (dst.ToLower())</param>
	/// <returns>return (src-dst)</returns>
	public static int compareItems(String src, String dst)
	{
		int ret = 0;

		ret = compareVNIStringUnsigned(src, dst); 
		
		if(ret != 0) return ret; 
		
		return compareVNIStringWithSigned(src, dst); 
	}

	private static int compareVNIStringUnsigned(String c1, String c2) {
		if ((c1 == "" && c2 == "") || (c1 == c2)) return 0;
		else if (c1 != "" && c2 == "") return 1;
		else if (c1 == "" && c2 != "") return -1;

		//convert to no sign
		String srcTmp2 = "";
		//			char[] srcArr = srcTmp1.toCharArray();
		//			for (char ch : srcArr)
		for (int i = 0; i < c1.length(); i++)
		{
			srcTmp2 += encodeToUnsignedVNIChar(c1.charAt(i)); 

		}
		String dstTmp2 = "";
		for (int i = 0; i < c2.length(); i++)
		{
			dstTmp2 += encodeToUnsignedVNIChar(c2.charAt(i)); 
		}

		int min = Math.min(dstTmp2.length(), srcTmp2.length());
		for(int i = 0; i < min; i++){
			int k = compare(srcTmp2.charAt(i), dstTmp2.charAt(i));
			if(k != 0) return k;
		}
		return srcTmp2.length() - dstTmp2.length();
	}
	
	private static int compareVNIStringWithSigned(String c1, String c2) {
		if ((c1 == "" && c2 == "") || (c1 == c2)) return 0;
        else if (c1 != "" && c2 == "") return 1;
        else if (c1 == "" && c2 != "") return -1;

    	int min = Math.min(c2.length(), c1.length());
    	for(int i = 0; i < min; i++){
    		int k = compare(c1.charAt(i), c2.charAt(i));
    		if(k != 0) return k;
    	}
    	return c1.length() - c2.length();
	}
	
	private static int compare(char c1, char c2) {
		if(c1 == c2) return 0;
		int enc1 = encode(c1);
		int enc2 = encode(c2);
		return (enc1 < 0 || enc2 < 0) ? c1 - c2 : enc1- enc2;
	}
	
	private static char encodeToUnsignedVNIChar (char c) {
		switch (c) {
		case 'A': return 'a';
		case 'a': return 'a';
		case 'Á': return 'a';
		case 'á': return 'a';
		case 'À': return 'a';
		case 'à': return 'a';
		case 'Ả': return 'a';
		case 'ả': return 'a';
		case 'Ã': return 'a';
		case 'ã': return 'a';
		case 'Ạ': return 'a';
		case 'ạ': return 'a';
		
		case 'Ă': return 'ă';
		case 'ă': return 'ă';
		case 'Ắ': return 'ă';
		case 'ắ': return 'ă';
		case 'Ằ': return 'ă';
		case 'ằ': return 'ă';
		case 'Ẳ': return 'ă';
		case 'ẳ': return 'ă';
		case 'Ẵ': return 'ă';
		case 'ẵ': return 'ă';
		case 'Ặ': return 'ă';
		case 'ặ': return 'ă';
		
		case 'Â': return 'â';
		case 'â': return 'â';
		case 'Ấ': return 'â';
		case 'ấ': return 'â';
		case 'Ầ': return 'â';
		case 'ầ': return 'â';
		case 'Ẩ': return 'â';
		case 'ẩ': return 'â';
		case 'Ẫ': return 'â';
		case 'ẫ': return 'â';
		case 'Ậ': return 'â';
		case 'ậ': return 'â';
		
		case 'E': return 'e';
		case 'e': return 'e';
		case 'É': return 'e';
		case 'é': return 'e';
		case 'È': return 'e';
		case 'è': return 'e';
		case 'Ẻ': return 'e';
		case 'ẻ': return 'e';
		case 'Ẽ': return 'e';
		case 'ẽ': return 'e';
		case 'Ẹ': return 'e';
		case 'ẹ': return 'e';
		
		case 'Ê': return 'ê';
		case 'ê': return 'ê';
		case 'Ế': return 'ê';
		case 'ế': return 'ê';
		case 'Ề': return 'ê';
		case 'ề': return 'ê';
		case 'Ể': return 'ê';
		case 'ể': return 'ê';
		case 'Ễ': return 'ê';
		case 'ễ': return 'ê';
		case 'Ệ': return 'ê';
		case 'ệ': return 'ê';
		
		case 'I': return 'i';
		case 'i': return 'i';
		case 'Í': return 'i';
		case 'í': return 'i';
		case 'Ì': return 'i';
		case 'ì': return 'i';
		case 'Ỉ': return 'i';
		case 'ỉ': return 'i';
		case 'Ĩ': return 'i';
		case 'ĩ': return 'i';
		case 'Ị': return 'i';
		case 'ị': return 'i';
		
		case 'O': return 'o';
		case 'o': return 'o';
		case 'Ó': return 'o';
		case 'ó': return 'o';
		case 'Ò': return 'o';
		case 'ò': return 'o';
		case 'Ỏ': return 'o';
		case 'ỏ': return 'o';
		case 'Õ': return 'o';
		case 'õ': return 'o';
		case 'Ọ': return 'o';
		case 'ọ': return 'o';
		
		case 'Ô': return 'ô';
		case 'ô': return 'ô';
		case 'Ố': return 'ô';
		case 'ố': return 'ô';
		case 'Ồ': return 'ô';
		case 'ồ': return 'ô';
		case 'Ổ': return 'ô';
		case 'ổ': return 'ô';
		case 'Ỗ': return 'ô';
		case 'ỗ': return 'ô';
		case 'Ộ': return 'ô';
		case 'ộ': return 'ô';
		
		case 'Ơ': return 'ơ';
		case 'ơ': return 'ơ';
		case 'Ớ': return 'ơ';
		case 'ớ': return 'ơ';
		case 'Ờ': return 'ơ';
		case 'ờ': return 'ơ';
		case 'Ở': return 'ơ';
		case 'ở': return 'ơ';
		case 'Ỡ': return 'ơ';
		case 'ỡ': return 'ơ';
		case 'Ợ': return 'ơ';
		case 'ợ': return 'ơ';
		
		case 'U': return 'u';
		case 'u': return 'u';
		case 'Ú': return 'u';
		case 'ú': return 'u';
		case 'Ù': return 'u';
		case 'ù': return 'u';
		case 'Ủ': return 'u';
		case 'ủ': return 'u';
		case 'Ũ': return 'u';
		case 'ũ': return 'u';
		case 'Ụ': return 'u';
		case 'ụ': return 'u';
		
		case 'Ư': return 'ư';
		case 'ư': return 'ư';
		case 'Ứ': return 'ư';
		case 'ứ': return 'ư';
		case 'Ừ': return 'ư';
		case 'ừ': return 'ư';
		case 'Ử': return 'ư';
		case 'ử': return 'ư';
		case 'Ữ': return 'ư';
		case 'ữ': return 'ư';
		case 'Ự': return 'ư';
		case 'ự': return 'ư';
		
		case 'Y': return 'y';
		case 'y': return 'y';
		case 'Ý': return 'y';
		case 'ý': return 'y';
		case 'Ỳ': return 'y';
		case 'ỳ': return 'y';
		case 'Ỷ': return 'y';
		case 'ỷ': return 'y';
		case 'Ỹ': return 'y';
		case 'ỹ': return 'y';
		case 'Ỵ': return 'y';
		case 'ỵ': return 'y';
		
		default: return c; 
		}
	}
	
	private static int encode(char c){
		switch (c) {
		case 'A': return 0;
		case 'a': return 1;
		case 'Á': return 2;
		case 'á': return 3;
		case 'À': return 4;
		case 'à': return 5;
		case 'Ả': return 6;
		case 'ả': return 7;
		case 'Ã': return 8;
		case 'ã': return 9;
		case 'Ạ': return 10;
		case 'ạ': return 11;
		case 'Ă': return 12;
		case 'ă': return 13;
		case 'Ắ': return 14;
		case 'ắ': return 15;
		case 'Ằ': return 16;
		case 'ằ': return 17;
		case 'Ẳ': return 18;
		case 'ẳ': return 19;
		case 'Ẵ': return 20;
		case 'ẵ': return 21;
		case 'Ặ': return 22;
		case 'ặ': return 23;
		case 'Â': return 24;
		case 'â': return 25;
		case 'Ấ': return 26;
		case 'ấ': return 27;
		case 'Ầ': return 28;
		case 'ầ': return 29;
		case 'Ẩ': return 30;
		case 'ẩ': return 31;
		case 'Ẫ': return 32;
		case 'ẫ': return 33;
		case 'Ậ': return 34;
		case 'ậ': return 35;
		case 'B': return 36;
		case 'b': return 37;
		case 'C': return 38;
		case 'c': return 39;
		case 'D': return 40;
		case 'd': return 41;
		case 'Đ': return 42;
		case 'đ': return 43;
		case 'E': return 44;
		case 'e': return 45;
		case 'É': return 46;
		case 'é': return 47;
		case 'È': return 48;
		case 'è': return 49;
		case 'Ẻ': return 50;
		case 'ẻ': return 51;
		case 'Ẽ': return 52;
		case 'ẽ': return 53;
		case 'Ẹ': return 54;
		case 'ẹ': return 55;
		case 'Ê': return 56;
		case 'ê': return 57;
		case 'Ế': return 58;
		case 'ế': return 59;
		case 'Ề': return 60;
		case 'ề': return 61;
		case 'Ể': return 62;
		case 'ể': return 63;
		case 'Ễ': return 64;
		case 'ễ': return 65;
		case 'Ệ': return 66;
		case 'ệ': return 67;
		case 'F': return 68;
		case 'f': return 69;
		case 'G': return 70;
		case 'g': return 71;
		case 'H': return 72;
		case 'h': return 73;
		case 'I': return 74;
		case 'i': return 75;
		case 'Í': return 76;
		case 'í': return 77;
		case 'Ì': return 78;
		case 'ì': return 79;
		case 'Ỉ': return 80;
		case 'ỉ': return 81;
		case 'Ĩ': return 82;
		case 'ĩ': return 83;
		case 'Ị': return 84;
		case 'ị': return 85;
		case 'J': return 86;
		case 'j': return 87;
		case 'K': return 88;
		case 'k': return 89;
		case 'L': return 90;
		case 'l': return 91;
		case 'M': return 92;
		case 'm': return 93;
		case 'N': return 94;
		case 'n': return 95;
		case 'O': return 96;
		case 'o': return 97;
		case 'Ó': return 98;
		case 'ó': return 99;
		case 'Ò': return 100;
		case 'ò': return 101;
		case 'Ỏ': return 102;
		case 'ỏ': return 103;
		case 'Õ': return 104;
		case 'õ': return 105;
		case 'Ọ': return 106;
		case 'ọ': return 107;
		case 'Ô': return 108;
		case 'ô': return 109;
		case 'Ố': return 110;
		case 'ố': return 111;
		case 'Ồ': return 112;
		case 'ồ': return 113;
		case 'Ổ': return 114;
		case 'ổ': return 115;
		case 'Ỗ': return 116;
		case 'ỗ': return 117;
		case 'Ộ': return 118;
		case 'ộ': return 119;
		case 'Ơ': return 120;
		case 'ơ': return 121;
		case 'Ớ': return 122;
		case 'ớ': return 123;
		case 'Ờ': return 124;
		case 'ờ': return 125;
		case 'Ở': return 126;
		case 'ở': return 127;
		case 'Ỡ': return 128;
		case 'ỡ': return 129;
		case 'Ợ': return 130;
		case 'ợ': return 131;
		case 'P': return 132;
		case 'p': return 133;
		case 'Q': return 134;
		case 'q': return 135;
		case 'R': return 136;
		case 'r': return 137;
		case 'S': return 138;
		case 's': return 139;
		case 'T': return 140;
		case 't': return 141;
		case 'U': return 142;
		case 'u': return 143;
		case 'Ú': return 144;
		case 'ú': return 145;
		case 'Ù': return 146;
		case 'ù': return 147;
		case 'Ủ': return 148;
		case 'ủ': return 149;
		case 'Ũ': return 150;
		case 'ũ': return 151;
		case 'Ụ': return 152;
		case 'ụ': return 153;
		case 'Ư': return 154;
		case 'ư': return 155;
		case 'Ứ': return 156;
		case 'ứ': return 157;
		case 'Ừ': return 158;
		case 'ừ': return 159;
		case 'Ử': return 160;
		case 'ử': return 161;
		case 'Ữ': return 162;
		case 'ữ': return 163;
		case 'Ự': return 164;
		case 'ự': return 165;
		case 'V': return 166;
		case 'v': return 167;
		case 'W': return 168;
		case 'w': return 169;
		case 'X': return 170;
		case 'x': return 171;
		case 'Y': return 172;
		case 'y': return 173;
		case 'Ý': return 174;
		case 'ý': return 175;
		case 'Ỳ': return 176;
		case 'ỳ': return 177;
		case 'Ỷ': return 178;
		case 'ỷ': return 179;
		case 'Ỹ': return 180;
		case 'ỹ': return 181;
		case 'Ỵ': return 182;
		case 'ỵ': return 183;
		case 'Z': return 184;
		case 'z': return 185;
		default: return -1;
		}
	}
}
