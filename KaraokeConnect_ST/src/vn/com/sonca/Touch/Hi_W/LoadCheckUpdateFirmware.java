package vn.com.sonca.Touch.Hi_W;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import vn.com.sonca.MyLog.MyLog;

public class LoadCheckUpdateFirmware extends AsyncTask<Void, Void, ArrayList<Firmware>> {
	
	private final String TAB = "LoadCheckUpdateFirmware";
	private SaveFirmware saveFirmware;
	private String romDir = "";
	
	public LoadCheckUpdateFirmware(Context context) {
		saveFirmware = SaveFirmware.getInstance(context);
		String rootPath = Environment.getExternalStorageDirectory().toString();
		rootPath = rootPath.concat(String.format("/%s/%s", "Android/data",
				context.getPackageName()));
		romDir = rootPath.concat("/ROM");
	}
	
	private OnLoadCheckUpdateFirmwareListener listener;
	public interface OnLoadCheckUpdateFirmwareListener {
		public void OnCheckFinish(ArrayList<Firmware> list, int version, int revision);
	}

	public void setOnLoadCheckUpdateFirmwareListener(OnLoadCheckUpdateFirmwareListener listener){
		this.listener = listener;
	}
	
	@Override
	protected ArrayList<Firmware> doInBackground(Void... params) {
		MyLog.e(TAB, "LoadCheckUpdateFirmware");
		ArrayList<Firmware> listKM1 = 
				downloadFile(
						"km1_firmupdate",
						"https://kos.soncamedia.com/firmware/firmware/km1/km1_firmupdate.txt", 
						Firmware.KM1);         
		ArrayList<Firmware> listHiW = 
				downloadFile(
						"firmupdate",
						"https://kos.soncamedia.com/firmware/firmware/hiw/firmupdate.txt", 
						Firmware.HiW);         
		ArrayList<Firmware> listReturn = new ArrayList<Firmware>();
		listReturn.addAll(listKM1);
		listReturn.addAll(listHiW);
		
		for (int i = 0; i < listReturn.size(); i++) {
			Firmware firmware = listReturn.get(i);
			MyLog.i(TAB, "Name : " + firmware.getName());
			MyLog.i(TAB, "Link : " + firmware.getLink());
			MyLog.i(TAB, "Size : " + firmware.getSize());
			MyLog.i(TAB, "         ");
		}
		
		return listReturn;
	}
	
	private int versionFirmware = 0;
	private int revisionFirmware = 0;
	@Override
	protected void onPostExecute(ArrayList<Firmware> result) {
		super.onPostExecute(result);
		if(listener != null){
			listener.OnCheckFinish(result , versionFirmware, revisionFirmware);
		}
	}
	
//////////////////////////////
	

	private String getNameFileFromLink(String link){
		int offset = link.lastIndexOf('/') + 1;
		if(offset <= 0){
			offset = 0;
		}
		String string = link.substring(offset);
		MyLog.e(TAB, "getNameFileFromLink : " + string);
		return string;
	}
	
	private ArrayList<Firmware> downloadFile(String nameFile, String linkdownload, int device) {
		URL url = null;
		InputStream input = null;
		FileOutputStream output = null;
		String infoFirmware = "";
		try {
			url = new URL(linkdownload);
			URLConnection connection;
			connection = url.openConnection();
			connection.connect();
			// -------//
			int length = connection.getContentLength();
			input = new BufferedInputStream(connection.getInputStream());
			output = new FileOutputStream(romDir + "/" + nameFile);
			byte data[] = new byte[length];
			input.read(data);
			output.write(data, 0, data.length);
			infoFirmware = new String(data);
			// -------//
			output.flush();
			output.close();
			input.close();
		} catch (Exception error) {
			error.printStackTrace();
			return new ArrayList<Firmware>();
		}
		MyLog.d(TAB, infoFirmware);

		try {
			JSONObject jsonObject = new JSONObject(infoFirmware);
			String version = jsonObject.getString("recommended_rom_version");
			int revision = jsonObject.getInt("recommended_rom_revision");
			JSONArray linkArray = jsonObject.getJSONArray("romServer");
			String v = version.substring(1);
			String[] data = v.split("\\.");
			int ver = 0;
			for (int i = 0; i < data.length; i++) {
				int ii = Integer.valueOf(data[i]);
				ver += ii * Math.pow(10, data.length - i);
			}
			ArrayList<Firmware> listReturn = new ArrayList<Firmware>();
			if (device == Firmware.HiW) {
				if (ver == saveFirmware.getVersionFirmwareHiW()) {
					if (revision > saveFirmware.getRevisionFirmwareHiW()) {
						listReturn.addAll(
							parseFirware(linkArray, ver, revision, Firmware.HiW));
					}
				} else if (ver > saveFirmware.getVersionFirmwareHiW()) {
					listReturn.addAll(
						parseFirware(linkArray, ver, revision, Firmware.HiW));
				}
			} else if (device == Firmware.KM1) {
				if (ver == saveFirmware.getVersionFirmwareKM1()) {
					if (revision > saveFirmware.getRevisionFirmwareKM1()) {
						listReturn.addAll(
							parseFirware(linkArray, ver, revision, Firmware.KM1));
					}
				} else if (ver > saveFirmware.getVersionFirmwareKM1()) {
					listReturn.addAll(
						parseFirware(linkArray, ver, revision, Firmware.KM1));
				}
			}
			return listReturn;
		} catch (Exception error) {
			error.printStackTrace();
			return new ArrayList<Firmware>();
		}
	}
	
	private ArrayList<Firmware> parseFirware(JSONArray linkArray, int ver, int rev, int device) 
			throws JSONException{
		ArrayList<Firmware> listReturn = new ArrayList<Firmware>();
		for (int k = 0; k < linkArray.length(); k++) {
			Firmware firmware = new Firmware();
			String linkUpdate = linkArray.getJSONObject(k).getString("url");
			int size = linkArray.getJSONObject(k).getInt("size");
			firmware.setLink(linkUpdate);
			firmware.setVersion(ver);
			firmware.setSize(size);
			firmware.setRevision(rev);
			firmware.setDevice(device);
			firmware.setName(getNameFileFromLink(linkUpdate));
			listReturn.add(firmware);
		}
		return listReturn;
	}
	
	
}
