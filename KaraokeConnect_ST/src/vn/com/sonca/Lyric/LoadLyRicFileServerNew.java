package vn.com.sonca.Lyric;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.params.LyricXML;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

public class LoadLyRicFileServerNew extends AsyncTask<Void, String, Integer> {
	
	private String TAB = "LoadLyRicFileServer";
	private ArrayList<LyricXML> listXmls = null;
	private String rootPath = "";
	private Context context;
	
	private OnLoadLyRicFileServerListener listener;
	public interface OnLoadLyRicFileServerListener {
		public void OnProgressUpdate(boolean isCreate, float total, int percent);
		public void OnPostExecute();
	}
	
	public void setOnLoadLyRicFileServerListener(OnLoadLyRicFileServerListener listener){
		this.listener = listener;
	}
	
	public LoadLyRicFileServerNew(Context context, ArrayList<LyricXML> listXmls) {
		rootPath = Environment.getExternalStorageDirectory().toString();
		rootPath = rootPath.concat(String.format("/%s/%s", "Android/data",
				context.getPackageName()));
		this.listXmls = listXmls;
		this.context = context;
	}

	@Override
	protected Integer doInBackground(Void... arg0) {
		if(listXmls == null) return null;
		for (int i = 0; i < listXmls.size(); i++) {
			LyricXML lyricXML = listXmls.get(i);
			publishProgress("create-" + lyricXML.getSize());
			String name = lyricXML.getName();
			int plus = lyricXML.getPlus();
			String pathFile = "";

			String strLangCheck = lyricXML.getId() + "";
			String newName = strLangCheck + "_" + lyricXML.getDate() + ".zip";
			
			if(plus == 0){
				pathFile = rootPath.concat("/COLORLYRIC/" + newName);
			}else if(plus == 1){
				pathFile = rootPath.concat("/COLORLYRIC/" + newName);
			}else{}
			
			File file = new File(pathFile);	
			// ------------------------------------

			URL url = null;
			InputStream input = null;
			FileOutputStream output = null;
			try {
				url = new URL("https://kos.soncamedia.com/firmware/KarConnect/lyric/" + name);
//				url = new URL("http://192.168.10.26/firmware/karconnect/lyric/"+ name);
				URLConnection connection;
				connection = url.openConnection();
				connection.connect();
				int length = connection.getContentLength();
				input = new BufferedInputStream(connection.getInputStream());
				output = new FileOutputStream(file);
				byte data[] = new byte[1024];
				int count;
				int load = 0;
				MyLog.e(TAB, rootPath.concat(pathFile));
				while ((count = input.read(data)) > 0) {
					if (isCancelled()) {
						if (file.exists()) {
							file.delete();
						}
						output.flush();
						output.close();
						input.close();
						return 0;
					}
					output.write(data, 0, count);
					load += count;
					// MyLog.d(TAB, "load - " + load);
					publishProgress(length + "-" + load);
				}
				
				StoreLyric storeLyric = new StoreLyric(context);
				if (plus == 0) {
					storeLyric.saveLyricDate(lyricXML.getDate());
				} else if (plus == 1) {
					storeLyric.saveLyricDate_Plus(lyricXML.getDate());
				}			
								
				File folderLyric = new File(rootPath.concat("/COLORLYRIC"));
				for (final File fileEntry : folderLyric.listFiles()) {
					if (fileEntry.isDirectory()) {
						// do nothing
					} else {
						String fileName = fileEntry.getName();
						if (fileName.startsWith(strLangCheck + "_") && !fileName.equals(newName)) {
							fileEntry.delete();
						}
					}
				}						
				
				output.flush();
				output.close();
				input.close();
				MyLog.d(TAB, "----DONE----");
			} catch (IOException e) {
				e.printStackTrace();
			}

			// ------------------------------------
					
			
		}
		return null;
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
		if(listener != null){
			String[] data = values[0].split("-");
			if (data.length != 2) {
				return;
			}
			if(data[0].equals("create")) {
				int length = Integer.valueOf(data[1]);
				listener.OnProgressUpdate(true, ((float)length/1024/1024), 0);
			} else {
				int length = Integer.valueOf(data[0]);
				int total = Integer.valueOf(data[1]);
				int progress = (int) (100.0f * total / length);
				listener.OnProgressUpdate(false, length, progress);
			}
		}
	}
	
	
	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		if(listener != null){
			listener.OnPostExecute();
		}
	}
	
}
