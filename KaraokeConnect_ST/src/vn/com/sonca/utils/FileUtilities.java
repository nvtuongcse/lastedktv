package vn.com.sonca.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class FileUtilities {
	private Writer writer;
	private String absolutePath;
	private final Context context;

	public FileUtilities(Context context) {
		super();
		this.context = context;
	}

	public void write(String fileName, String data) {
		File root = Environment.getExternalStorageDirectory();
		File outDir = new File(root.getAbsolutePath() + File.separator
				+ "myOutput");
		if (!outDir.isDirectory()) {
			outDir.mkdir();
		}
		try {
			if (!outDir.isDirectory()) {
				throw new IOException(
						"Unable to create directory EZ_time_tracker. Maybe the SD card is mounted?");
			}
			File outputFile = new File(outDir, fileName);
			writer = new BufferedWriter(new FileWriter(outputFile));
			writer.write(data);
			writer.close();
		} catch (IOException e) {
			Log.w("eztt", e.getMessage(), e);
		}

	}
	
	public void write(String outDirPath, String fileName, String data) {
		File outDir = new File(outDirPath);
		if (!outDir.isDirectory()) {
			outDir.mkdir();
		}
		try {
			if (!outDir.isDirectory()) {
				throw new IOException(
						"Unable to create directory EZ_time_tracker. Maybe the SD card is mounted?");
			}
			File outputFile = new File(outDir, fileName);
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

	public static String convertStreamToString(InputStream is) throws Exception {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();
	    String line = null;
	    while ((line = reader.readLine()) != null) {
	      sb.append(line).append("\n");
	    }
	    reader.close();
	    return sb.toString();
	}

	public static String getStringFromFile (String filePath) throws Exception {
	    File fl = new File(filePath);
	    FileInputStream fin = new FileInputStream(fl);
	    String ret = convertStreamToString(fin);
	    //Make sure you close all streams.
	    fin.close();        
	    return ret;
	}
	
}
