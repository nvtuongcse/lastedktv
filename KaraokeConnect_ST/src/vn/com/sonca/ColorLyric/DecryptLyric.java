package vn.com.sonca.ColorLyric;

public class DecryptLyric {
	private static int nPasswordInt32 = 0;
	private static int nPasswordStr = 0;
	private static String nCharsText = "0123456789ABCDEF";

	public static void getWordSize(int index) {
		index += 27894;
		nPasswordStr = index;

		//Get pwd bytes
		int[] pwd = new int[3];
		pwd[0] = ((index >> 24) & 0xFF);
		pwd[1] = ((index >> 16) & 0xFF);
		pwd[2] = ((index >> 8) & 0xFF);

		//cook
		pwd[0] = ((pwd[0] ^ pwd[1]) & 0xFF);
		pwd[1] = ((pwd[1] ^ (~pwd[2])) & 0xFF);
		pwd[2] = ((pwd[2] ^ (~pwd[0])) & 0xFF);
		nPasswordInt32 = ((pwd[0] << 16) | (pwd[1] << 8) | (pwd[2])) & 0xFFFFFF;
	}

	public static int getInt32(int value) {
		value = nPasswordInt32 ^ value;
		return ((int) value);
	}

	public static String getStringValue(String text, int offset) {
		//Convert other String to Text in Hex 
		offset = (offset % (58 - 16 + 1));
		//    NSLog(@"Offset: %d", offset);

		String strHex = "";
		int i = 0;
		while (i < text.length()) {
			int index = text.charAt(i) - 65 - offset;
			strHex += nCharsText.charAt(index);
			i++;
		}

		int oneByte;

		int byteCount = 0;
		int[] nBytes = new int[strHex.length()];

		for (i = 0; i < strHex.length(); i += 2) {
			String hexChar = strHex.substring(i, i + 2);

			oneByte = Integer.parseInt(hexChar, 16);

			oneByte = ((oneByte ^ ((int) nPasswordStr & 0xFF)) & 0xFF);
			nBytes[byteCount++] = oneByte;
		}

		strHex = "";
		i = 0;
		while (i < byteCount) {
			int curVal = nBytes[i];
			if (((curVal >= 32) && (curVal <= 127)) || (i == (byteCount - 1))) {
				strHex += (char) curVal;
			} else {
				if (curVal < 32) {
					curVal = curVal << 8;
					curVal |= nBytes[i + 1];
					strHex += (char) curVal;
					i++;
				} else if (curVal > 127) {
					strHex += (char) curVal;
				}
			}
			i++;
		}

		return strHex;
	}
}
