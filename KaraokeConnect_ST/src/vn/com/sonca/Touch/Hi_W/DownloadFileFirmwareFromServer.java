package vn.com.sonca.Touch.Hi_W;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import vn.com.sonca.MyLog.MyLog;

public class DownloadFileFirmwareFromServer extends AsyncTask<Void, Integer, Integer>{

	private final String TAB = "DownloadFileFirmwareFromServer";
	private SaveFirmware saveFirmware;
	private int lengthFileFirmware = 0;
	private ArrayList<Firmware> urlFirmware;
	private String romDir = "";
	
	private int versionFirmware = 0;
	private int revisionFirmware = 0;
	
	public DownloadFileFirmwareFromServer(Context context, ArrayList<Firmware> list, int version, int revision) {
		saveFirmware = SaveFirmware.getInstance(context);
		String rootPath = Environment.getExternalStorageDirectory().toString();
		rootPath = rootPath.concat(String.format("/%s/%s", "Android/data",
				context.getPackageName()));
		romDir = rootPath.concat("/ROM");
		versionFirmware = version;
		revisionFirmware = revision;
		urlFirmware = list;
	}
	
	private OnDownloadFileFirmwareFromServer listener;
	public interface OnDownloadFileFirmwareFromServer{
		public void OnRunning(int progress);
		public void OnFinish();
	}
	
	public void setOnDownloadFileFirmwareFromServer(OnDownloadFileFirmwareFromServer listener){
		this.listener = listener;
	}
	
	public int getLengthFileFirmware(){
		return lengthFileFirmware;
	}
	
	@Override
	protected Integer doInBackground(Void... arg0) {
		if (urlFirmware != null) {
			if (!urlFirmware.isEmpty()) {
				loadFileFirmware = 0;
				for (int i = 0; i < urlFirmware.size(); i++) {
					lengthFileFirmware += urlFirmware.get(i).getSize();
				}
				for (int i = 0; i < urlFirmware.size(); i++) {
					Firmware firmware = urlFirmware.get(i);
					String name = firmware.getName();
					String link = firmware.getLink();
					int ver = firmware.getVersion();
					int rev = firmware.getRevision();
					int status = downloadFile(name, link);
					if(status == 1){
						if(firmware.getDevice() == Firmware.KM1){
							saveFirmware.saveVersionFirmwareKM1(ver);
							saveFirmware.saveRevisionFirmwareKM1(rev);
						}else if(firmware.getDevice() == Firmware.HiW){
							saveFirmware.saveVersionFirmwareHiW(ver);
							saveFirmware.saveRevisionFirmwareHiW(rev);
						}
					}
				}
				
			}
		}
		
		
		
		return 0;
	}
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		if(listener != null){
			listener.OnRunning(values[0]);
		}
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		if(listener != null){
			listener.OnFinish();
		}
	}
	
	private void UnpackZip(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		ZipInputStream zis = new ZipInputStream(new BufferedInputStream(is));
		ZipEntry ze;
		while ((ze = zis.getNextEntry()) != null) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int count;
			String filename = ze.getName();
			FileOutputStream fout = new FileOutputStream(romDir + "/" + filename);
			while ((count = zis.read(buffer)) != -1) {
				baos.write(buffer, 0, count);
				byte[] bytes = baos.toByteArray();
				fout.write(bytes);
				baos.reset();
			}
			fout.close();
			zis.closeEntry();
		}
		zis.close();
	}
	
	private int loadFileFirmware = 0;
	private int downloadFile(String nameFile, String urlFirmware){
		try {
			MyLog.d(TAB, "----START----");
			MyLog.d(TAB, nameFile);
			File file = new File(romDir + "/" + nameFile);	
			if(file.exists()){
				file.delete();
			}
			URL url = new URL(urlFirmware);
			URLConnection connection;
			connection = url.openConnection();
			connection.connect();
			InputStream input = new BufferedInputStream(connection.getInputStream());
			FileOutputStream output = new FileOutputStream(file);
			byte data[] = new byte[1024];
			int count;
			while ((count = input.read(data)) > 0) {
				if(isCancelled()) {
					if(file.exists()){
						file.delete();
					}
					output.flush();
					output.close();
					input.close();
					MyLog.d(TAB, "----DONE----");
					return 0;
				}
				output.write(data, 0, count);
				loadFileFirmware += count;
				publishProgress((int)((100.0f) * 
						loadFileFirmware / lengthFileFirmware));
			}
			output.flush();
			output.close();
			input.close();
			MyLog.d(TAB, "----DONE----");
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
		File file = new File(romDir + "/" + nameFile);	
		if(file.exists()){
			try {
				UnpackZip(file);
			} catch (IOException e) {
				e.printStackTrace();
				return 0;
			}
		}
		return 1;
	}
	

}
