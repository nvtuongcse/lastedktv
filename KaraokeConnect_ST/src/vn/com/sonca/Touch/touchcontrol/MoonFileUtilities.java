package vn.com.sonca.Touch.touchcontrol;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class MoonFileUtilities {
	private Writer writer;
	private String absolutePath;
	private final Context context;

	public MoonFileUtilities(Context context) {
		super();
		this.context = context;
	}

	public void write(String fileName, String data) {
		File root = Environment.getExternalStorageDirectory();
		File outDir = new File(root.getAbsolutePath() + File.separator
				+ "myLog");
		if (!outDir.exists()) {
			outDir.mkdirs();
		}
		try {
			if (!outDir.isDirectory()) {
				throw new IOException(
						"Unable to create directory EZ_time_tracker. Maybe the SD card is mounted?");
			}
			File outputFile = new File(outDir, fileName);
			if(outputFile.exists()){
				outputFile.delete();
			}
			writer = new BufferedWriter(new FileWriter(outputFile));
			writer.write(data);
			writer.close();
		} catch (IOException e) {
			Log.w("eztt", e.getMessage(), e);
		}

	}

	public void writeAppend(String fileName, String data) {
		File root = Environment.getExternalStorageDirectory();
		File outDir = new File(root.getAbsolutePath() + File.separator
				+ "myLog");
		if (!outDir.exists()) {
			outDir.mkdirs();
		}
		try {
			if (!outDir.isDirectory()) {
				throw new IOException(
						"Unable to create directory EZ_time_tracker. Maybe the SD card is mounted?");
			}
			File outputFile = new File(outDir, fileName);
			writer = new BufferedWriter(new FileWriter(outputFile, true));
			writer.write(data);
			writer.close();
		} catch (IOException e) {
			Log.w("eztt", e.getMessage(), e);
		}

	}
	
	public void write(File parentDir, String fileName, String data) {
		if(!parentDir.exists()){
			return;
		}
		
		try {
			Log.e("write", "data = " + data);
			File outputFile = new File(parentDir, fileName);
			writer = new BufferedWriter(new FileWriter(outputFile));
			writer.write(data);
			writer.close();
		} catch (IOException e) {
			Log.w("eztt", e.getMessage(), e);
		}

	}

	public Writer getWriter() {
		return writer;
	}

	public String getAbsolutePath() {
		return absolutePath;
	}

}
