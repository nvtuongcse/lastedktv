package vn.com.sonca.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.util.Log;

public class ConvertString {
	private Integer[] TAB1 = { 254, 253, 252, 251, 250, 249, 248, 247, 246,
			245, 244, 243, 242, 241, 239, 238, 237, 236, 235, 234, 233, 232,
			231, 230, 229, 228, 227, 226, 225, 223, 222, 221, 220, 216, 215,
			214, 213, 212, 211, 210, 209, 208, 207, 204, 203, 202, 201, 200,
			199, 198, 190, 189, 188, 187, 185, 184, 183, 182, 181 };

	private Integer[] TAB2 = { 26, 27, 28, 29, 30, 126, 127, 128, 129, 130,
			131, 132, 133, 134, 135, 136, 137, 138, 139, 140, 141, 142, 143,
			144, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154, 155, 156,
			157, 158, 159, 160, 175, 176, 177, 179, 180, 186, 191, 192, 193,
			194, 195, 196, 197, 205, 217, 218, 219, 224, 240 };

	private Integer[] UNICODE_LUT = { 193, 225, 192, 224, 194, 226, 258, 259,
			195, 227, 7844, 7845, 7846, 7847, 7854, 7855, 7856, 7857, 7850,
			7851, 7860, 7861, 7842, 7843, 7848, 7849, 7858, 7859, 7840, 7841,
			7852, 7853, 7862, 7863, 272, 273, 201, 233, 200, 232, 202, 234,
			7868, 7869, 7870, 7871, 7872, 7873, 7876, 7877, 7866, 7867, 7874,
			7875, 7864, 7865, 7878, 7879, 205, 237, 204, 236, 296, 297, 7880,
			7881, 7882, 7883, 211, 243, 210, 242, 212, 244, 213, 245, 7888,
			7889, 7890, 7891, 7894, 7895, 7886, 7887, 416, 417, 7892, 7893,
			7884, 7885, 7898, 7899, 7900, 7901, 7904, 7905, 7896, 7897, 7902,
			7903, 7906, 7907, 218, 250, 217, 249, 360, 361, 7910, 7911, 431,
			432, 7908, 7909, 7912, 7913, 7914, 7915, 7918, 7919, 7916, 7917,
			7920, 7921, 221, 253, 7922, 7923, 7928, 7929, 7926, 7927, 7924,
			7925, };

	private Integer[] TCVN_LUT = { 218, 184, 240, 181, 162, 169, 161, 168, 219,
			183, 186, 202, 193, 199, 195, 190, 205, 187, 191, 201, 196, 189,
			224, 182, 192, 200, 197, 188, 217, 185, 180, 203, 194, 198, 167,
			174, 176, 208, 179, 204, 163, 170, 177, 207, 157, 213, 160, 210,
			158, 212, 178, 206, 159, 211, 175, 209, 156, 214, 152, 221, 155,
			215, 153, 220, 154, 216, 151, 222, 147, 227, 150, 223, 164, 171,
			148, 226, 142, 232, 145, 229, 143, 231, 149, 225, 165, 172, 144,
			230, 146, 228, 137, 237, 140, 234, 138, 236, 141, 233, 139, 235,
			136, 238, 132, 243, 135, 239, 133, 242, 134, 241, 166, 173, 131,
			244, 127, 248, 130, 245, 128, 247, 129, 246, 31, 249, 27, 253, 30,
			250, 28, 252, 29, 251, 26, 254, };

	private char[] tcvnchars = { 'µ', '¸', '¶', '·', '¹', '¨', '»', '¾', '¼',
			'½', 'Æ', '©', 'Ç', 'Ê', 'È', 'É', 'Ë', '®', 'Ì', 'Ð', 'Î', 'Ï',
			'Ñ', 'ª', 'Ò', 'Õ', 'Ó', 'Ô', 'Ö', '×', 'Ý', 'Ø', 'Ü', 'Þ', 'ß',
			'ã', 'á', 'â', 'ä', '«', 'å', 'è', 'æ', 'ç', 'é', '¬', 'ê', 'í',
			'ë', 'ì', 'î', 'ï', 'ó', 'ñ', 'ò', 'ô', '­', 'õ', 'ø', 'ö', '÷',
			'ù', 'ú', 'ý', 'û', 'ü', 'þ', '¡', '¢', '§', '£', '¤', '¥', '¦' };

	private char[] unichars = { 'à', 'á', 'ả', 'ã', 'ạ', 'ă', 'ằ', 'ắ', 'ẳ',
			'ẵ', 'ặ', 'â', 'ầ', 'ấ', 'ẩ', 'ẫ', 'ậ', 'đ', 'è', 'é', 'ẻ', 'ẽ',
			'ẹ', 'ê', 'ề', 'ế', 'ể', 'ễ', 'ệ', 'ì', 'í', 'ỉ', 'ĩ', 'ị', 'ò',
			'ó', 'ỏ', 'õ', 'ọ', 'ô', 'ồ', 'ố', 'ổ', 'ỗ', 'ộ', 'ơ', 'ờ', 'ớ',
			'ở', 'ỡ', 'ợ', 'ù', 'ú', 'ủ', 'ũ', 'ụ', 'ư', 'ừ', 'ứ', 'ử', 'ữ',
			'ự', 'ỳ', 'ý', 'ỷ', 'ỹ', 'ỵ', 'Ă', 'Â', 'Đ', 'Ê', 'Ô', 'Ơ', 'Ư' };
	
	private static char[] convertTable;

	private void setString() {
		convertTable = new char[256];
		for (int i = 0; i < 256; i++)
			convertTable[i] = (char) i;
		for (int i = 0; i < tcvnchars.length; i++)
			convertTable[tcvnchars[i]] = unichars[i];
	}

	public String TCVN3ToUnicode(String value) {
		setString();
		char[] chars = value.toCharArray();
		for (int i = 0; i < value.length(); i++)
			if (chars[i] < (char) 256)
				chars[i] = convertTable[chars[i]];
		return new String(chars);
	}

	public String ConvertTCVNtoUnicode(byte[] tcvnString) {
		//Log.e("ConvertTCVNtoUnicode", "==========");
		Integer idx = 0;
		List<Integer> TCVNCharList = new ArrayList<Integer>();
		//for (Integer b : TCVN_LUT) {
		//	TCVNCharList.add(b);
		//}
		TCVNCharList.addAll(Arrays.asList(TCVN_LUT));
		String result = "";

		try {
			for (idx = 0; idx < tcvnString.length; idx++) {
				if (TCVNCharList.contains(tcvnString[idx])) {					
					result += (char) (UNICODE_LUT[TCVNCharList
							.indexOf(tcvnString[idx])] & 0xFF);
				} else {					
					result += (char) (tcvnString[idx] & 0xFF);
				}
			}
		} catch (Exception ex) {
			// MessageBox.Show(ex.Message);
		}
		return result;
	}

	public int ConvertTab1ToTab2(int intTab1) {
		List<Integer> TAB1List = new ArrayList<Integer>();
		TAB1List.addAll(Arrays.asList(TAB1));

		int result = intTab1;

		if (TAB1List.contains(intTab1)) {
			result = TAB2[TAB1List.indexOf(intTab1)];
		}

		return result;
	}

	public int ConvertTab2ToTab1(int intTab2) {
		List<Integer> TAB2List = new ArrayList<Integer>();
		TAB2List.addAll(Arrays.asList(TAB2));

		int result = intTab2;

		if (TAB2List.contains(intTab2)) {
			result = TAB1[TAB2List.indexOf(intTab2)];
		}

		return result;
	}
}
