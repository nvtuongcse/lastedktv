package vn.com.sonca.utils;

import java.io.ByteArrayInputStream;

import android.graphics.drawable.Drawable;
import android.util.Log;

public class ImageManager {

	private static final String IMAGE_MANAGER = "IMAGE_MANAGER";
	
	public static Drawable getDrawableFromBytes(ByteArrayInputStream data) throws OutOfMemoryError , NullPointerException{
		return Drawable.createFromStream(data, "bitmap");
	}
	
}