package vn.com.sonca.Lyric;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.params.LyricXML;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class LoadCheckLyric extends AsyncTask<Void, String, Integer>{
	
	private final String nameFile = "check_lyric_update_v2.xml";
	private String TAB = "LoadLyRicFileServer";
	private String rootPath = "";
	private int totalDown = 0;
	private Context context;
	private ArrayList<LyricXML> listXmls;
	

	public static final int SUCCESS = 0;
	public static final int NOWIFI = 1;
	public static final int NODOWN = 2;

	
	private OnLoadCheckLyricListener listener;
	public interface OnLoadCheckLyricListener{
		public void OnPostExecute(int result, int totalsize, ArrayList<LyricXML> list);
	}
	
	public void setOnLoadCheckLyricListener(OnLoadCheckLyricListener listener){
		this.listener = listener;
	}
	
	public LoadCheckLyric(Context context) {
		this.context = context;
		rootPath = Environment.getExternalStorageDirectory().toString();
		rootPath = rootPath.concat(String.format("/%s/%s", "Android/data",
				context.getPackageName()));
	}
	
	@Override
	protected Integer doInBackground(Void... arg0) {
		File dirLyric = new File(rootPath.concat("/LYRIC"));
		if (!dirLyric.exists()) {
			dirLyric.mkdir();
		}
		dirLyric = new File(rootPath.concat("/LYRIC/SONCA"));
		if (!dirLyric.exists()) {
			dirLyric.mkdir();
		}
		dirLyric = new File(rootPath.concat("/LYRIC/USER"));
		if (!dirLyric.exists()) {
			dirLyric.mkdir();
		}
		
			//---------------//
		
		boolean check = checkUpdateLyric(nameFile);
		if(check == false){
			deleteFileCheck(nameFile);
			return NOWIFI;
		}
		ArrayList<LyricXML> list = readLyricXML(nameFile);
		if(list == null){
			deleteFileCheck(nameFile);
			return NODOWN;
		}	
		listXmls = checkDownLyric(list);
		if(listXmls.isEmpty()){
			deleteFileCheck(nameFile);
			return NODOWN;
		}
		
		Collections.sort(listXmls);
		
/*		
		MyLog.e(TAB, "Length Package : " + listXmls.size());
		Log.d(TAB, "-----------------");
		for (int i = 0; i < listXmls.size(); i++) {
			LyricXML lyricXML = listXmls.get(i);
			Log.d(TAB, "id - " + lyricXML.getId());
			Log.d(TAB, "plus - " + lyricXML.getPlus());
			Log.d(TAB, "Date - " + lyricXML.getDate());
			Log.d(TAB, "Size - " + lyricXML.getSize());
			Log.d(TAB, "Name - " + lyricXML.getName());
			Log.d(TAB, "-----------------");
		}
*/		
		
		deleteFileCheck(nameFile);
		
		return SUCCESS;
	}
	
	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);
		if(listener != null){
			listener.OnPostExecute(result, totalDown, listXmls);
		}
	}
	
	private boolean checkUpdateLyric(String name) {
		URL url  = null;
		InputStream input = null;
		FileOutputStream output = null;
		try {
			url = new URL("https://kos.soncamedia.com/firmware/KarConnect/lyric/" + name);
			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(3 * 1000); 
			connection.connect();
			input = new BufferedInputStream(connection.getInputStream());
			output = new FileOutputStream(rootPath.concat("/LYRIC/" + name));
			byte data[] = new byte[1024];
			int count;
			MyLog.e(TAB, rootPath.concat(name));
			while ((count = input.read(data)) > 0) {
				output.write(data, 0, count);
				MyLog.e(TAB, "count : " + count);
			}
			MyLog.e(TAB, "   ");
			output.flush();
			output.close();
			input.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	private ArrayList<LyricXML> readLyricXML(String name) {
		File file = new File(rootPath.concat("/LYRIC/" + name));
		if (!file.exists()) {
			return null;
		}
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			NodeList nodeList = doc.getElementsByTagName("package");
			ArrayList<LyricXML> listXmls = new ArrayList<LyricXML>();
			for (int i = 0; i < nodeList.getLength(); i++) {
				LyricXML lyricXML = new LyricXML();
				Element element = (Element)nodeList.item(i);
				String data = element.getAttribute("id").toString();
				lyricXML.setId(Integer.valueOf(data));
					//------------//
				data = element.getElementsByTagName("plus").item(0).getTextContent();
				lyricXML.setPlus(Integer.valueOf(data));
					//------------//
				data = element.getElementsByTagName("date").item(0).getTextContent();
				lyricXML.setDate(Integer.valueOf(data));
					//------------//
				data = element.getElementsByTagName("size").item(0).getTextContent();
				lyricXML.setSize(Integer.valueOf(data));
					//------------//
				data = element.getElementsByTagName("link").item(0).getTextContent();
				lyricXML.setName(data);
					//------------//
				listXmls.add(lyricXML);
			}
			if (listXmls.isEmpty()) {
				return null;
			} else {
				return listXmls;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private ArrayList<LyricXML> checkDownLyric(ArrayList<LyricXML> list){
		ArrayList<LyricXML> listOut = new ArrayList<LyricXML>();
		if(list.isEmpty()){
			return listOut;
		}
		StoreLyric storeLyric = new StoreLyric(context);
		for (int i = 0; i < list.size(); i++) {
			LyricXML lyricXML = list.get(i);
			String name = lyricXML.getName();
			int plus = lyricXML.getPlus();
			String pathFile = "";
			if(plus == 0){
				pathFile = rootPath.concat("/LYRIC/SONCA/" + name);
			}else if(plus == 1){
				pathFile = rootPath.concat("/LYRIC/USER/" + name);
			}else{}
			File file = new File(pathFile);	
			if(file.exists()){
				continue;
			}else{
				if (plus == 0) {
					totalDown += lyricXML.getSize();
					listOut.add(lyricXML);
				} else {
					if (storeLyric.getLyricHDD()) {
						totalDown += lyricXML.getSize();
						listOut.add(lyricXML);
					}
				}
			}
		}
		return listOut;			
	}

	private void deleteFileCheck(String name){
		File file = new File(rootPath.concat("/LYRIC/" + name));
		if(file.exists()){
			file.delete();
		}
	}
	
}
