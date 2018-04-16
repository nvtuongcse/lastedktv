package vn.com.sonca.utils;

public class PinyinHelper {
	
	public static boolean checkChinese(String str) {
		int decCodepoint = str.codePointAt(0); // in decimal

		// CJK Unified Ideographs block - 20941
		if (checkInRange(decCodepoint, "4E00", "62FF")) {
			return true;
		}
		if (checkInRange(decCodepoint, "6300", "77FF")) {
			return true;
		}
		if (checkInRange(decCodepoint, "7800", "8CFF")) {
			return true;
		}
		if (checkInRange(decCodepoint, "8D00", "9FCC")) {
			return true;
		}

		// CJKUI Ext A block - 6582
		if (checkInRange(decCodepoint, "3400", "4DB5")) {
			return true;
		}

		// CJKUI Ext B block - 42711
		if (checkInRange(decCodepoint, "20000", "215FF")) {
			return true;
		}
		if (checkInRange(decCodepoint, "21600", "230FF")) {
			return true;
		}
		if (checkInRange(decCodepoint, "23100", "245FF")) {
			return true;
		}
		if (checkInRange(decCodepoint, "24600", "260FF")) {
			return true;
		}
		if (checkInRange(decCodepoint, "26100", "275FF")) {
			return true;
		}
		if (checkInRange(decCodepoint, "27600", "290FF")) {
			return true;
		}
		if (checkInRange(decCodepoint, "29100", "2A6DF")) {
			return true;
		}

		// CJKUI Ext C block - 4149
		if (checkInRange(decCodepoint, "2A700", "2B734")) {
			return true;
		}

		// CJKUI Ext D block - 222
		if (checkInRange(decCodepoint, "2B740", "2B81D")) {
			return true;
		}

		// CJKUI Ext E block
		if (checkInRange(decCodepoint, "2B820", "2CEAF")) {
			return true;
		}

		// CJKUI Ext F block
		if (checkInRange(decCodepoint, "2CEB0", "2DD8F")) {
			return true;
		}

		// CJK Compatibility Ideographs Supplement
		if (checkInRange(decCodepoint, "2F800", "2FA1F")) {
			return true;
		}

		// Unassigned 1
		if (checkInRange(decCodepoint, "2A6E0", "2A6FF")) {
			return true;
		}

		// Unassigned 2
		if (checkInRange(decCodepoint, "2DD90", "2F7FF")) {
			return true;
		}

		// Unassigned 3
		if (checkInRange(decCodepoint, "2FB20", "2FFFD")) {
			return true;
		}

		return false;
	}

	private static boolean checkInRange(int num, String start, String end) {
		int startRange = (int) Long.parseLong(start, 16);
		int endRange = (int) Long.parseLong(end, 16);

		if (num >= startRange && num <= endRange) {
			return true;
		}

		return false;
	}
	
	public static String replaceAll(String stringIn){
		String string = stringIn.toUpperCase();
		String stringOut = string.replaceAll("Đ", "D");
		stringOut = stringOut.replaceAll("[ÍÌỈĨỊ]", "I");
		stringOut = stringOut.replaceAll("[ÝỲỶỸỴ]", "Y");
		stringOut = stringOut.replaceAll("[ÚÙỦŨỤƯỨỪỬỮỰ]", "U");
		stringOut = stringOut.replaceAll("[ÉÈẺẼẸÊẾỀỂỄỆ]", "E");
		stringOut = stringOut.replaceAll("[ÓÒỎÕỌÔỐỒỔỖỘƠỚỜỞỠỢ]", "O");
		stringOut = stringOut.replaceAll("[ÁÀẢÃẠÂẤẦẨẪẬĂẮẰẲẴẶ]", "A");
		return stringOut;
	}
	
}
