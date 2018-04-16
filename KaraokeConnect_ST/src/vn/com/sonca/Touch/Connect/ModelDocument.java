package vn.com.sonca.Touch.Connect;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.params.SKServer;
import vn.com.sonca.utils.AppSettings;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.os.AsyncTask;

public class ModelDocument {
	
	private String TAB = "ModelDocument";
	private int ircRemote = -1;
	private String ircRemoteActiveName = "ACNOS";
	
	private InputStream xml;
	private ArrayList<Model> listModels = new ArrayList<Model>();
	public ModelDocument(Context context, int ircRemote) {
		xml = context.getResources().openRawResource(R.raw.kartrolmodel);
		this.ircRemote = ircRemote;
		
		AppSettings setting = AppSettings.getInstance(context.getApplicationContext());		
		this.ircRemoteActiveName = setting.loadNameRemote();
	}
	
	private OnModelDocumentListener listener;
	public interface OnModelDocumentListener {
		public void OnFinishRead(ArrayList<Model> list);
	}
	
	public void setOnModelDocumentListener(OnModelDocumentListener listener){
		this.listener = listener;
	}
	
	private LoadXML loadXML;
	public void readDocument() {
		if(loadXML == null){
			loadXML = new LoadXML();
			loadXML.execute();
		}else{
			if(loadXML.getStatus() == AsyncTask.Status.FINISHED){
				loadXML = null;
				loadXML = new LoadXML();
				loadXML.execute();
			}
		}
	}
	
	private class LoadXML extends AsyncTask<Void, Void, Void> {

		public LoadXML() {}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				listModels.clear();
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(xml);
				NodeList nod0 = doc.getElementsByTagName("manufactures");
				Element ele0 = (Element)nod0.item(0);
				NodeList nod1 = ele0.getElementsByTagName("models");
				Element ele1 = (Element)nod1.item(0);
				NodeList list = ele1.getElementsByTagName("model");
				int size = list.getLength();
				for (int i = 0; i < size; i++) {
					Element element = (Element) list.item(i);
					int toc = Integer.valueOf(element.getAttribute("toc").toString());
					int irc = Integer.valueOf(element.getAttribute("irc").toString());
					int en = Integer.valueOf(element.getAttribute("en").toString());
					String name = element.getElementsByTagName("name").item(0).getTextContent().toString();
					String details = element.getElementsByTagName("details").item(0).getTextContent().toString();
					Model model = new Model(toc, irc, en, name, details);
					listModels.add(model);
				}				
				for (int i = 0; i < listModels.size(); i++) {
					Model model = listModels.get(i);
					if(model != null){
						if(model.getIrc() == ircRemote && model.getName().equalsIgnoreCase(ircRemoteActiveName)){
							model.setActive(true);
						}
					}
				}
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if(listener != null && !listModels.isEmpty()){
				listener.OnFinishRead(listModels);
			}
		}

	}

}
