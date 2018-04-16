package vn.com.sonca.WebServer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;

import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.preference.PreferenceManager;
import android.util.Log;

public class RequestFormatPageHandler implements HttpRequestHandler {
	private Context context = null;

	public RequestFormatPageHandler(Context context) {
		this.context = context;
	}
	
	private boolean isDownload = false;
	
	@Override
	public void handle(HttpRequest request, HttpResponse response,
			HttpContext httpContext) throws HttpException, IOException {
		MyLog.e("RequestFormatPageHandler", "handle..........................");
		MyLog.e("request", request.getRequestLine().toString());
		MyLog.e("request", request.getRequestLine().getUri());

		String uriString = request.getRequestLine().getUri();

		HttpEntity entity = getEntityFromUri(uriString, response);

		response.setEntity(entity);
		
		if(isDownload){
			
		}
	}

	private HttpEntity getEntityFromUri(String uri, HttpResponse response) {
		isDownload = false;
		String contentType = "text/html";
		String filepath = getFilePath();

		if (filepath == null) {
			return null;
		}

//		MyLog.e("", "URI = " + uri);
//		MyLog.e("", "filepath = " + filepath);

		HttpEntity entity = null;
		
		if(uri.contains("is_format_simple=true")){
			MyLog.e("", "is_format_simple");
			entity = new EntityTemplate(new ContentProducer() {
				public void writeTo(final OutputStream outstream)
						throws IOException {
					OutputStreamWriter writer = new OutputStreamWriter(
							outstream, "UTF-8");
					//String resp = "{\"recommended_rom_version\": \"v0.19\", \"status\": 200, \"productRoms\": [{\"recommended\": 1, \"version\": \"v0.19\", \"life_cycle\": \"beta\"}, {\"recommended\": 1, \"version\": \"v0.18\", \"life_cycle\": \"beta\"}, {\"recommended\": 1, \"version\": \"v0.17\", \"life_cycle\": \"beta\"}]}";
					String temp = "{\"recommended_rom_version\": \"v1.3\", \"recommended_rom_revision\":6217, \"status\": 200,\"romServer\":[{\"size\": 425568 , \"url\":\"https://kos.soncamedia.com/firmware/firmware/hiw/firmupdate.zip\"}], \"productRoms\": [{\"recommended\": 1, \"version\": \"v1.3\", \"revision\":6217, \"life_cycle\": \"beta\"}]}";
					
					String result = getResponseIsFormatSimple();
					if(result == null){
						result = temp;
					}
					
					//MyLog.e("response result", result);
					
					writer.write(result);
					writer.flush();
				}
			});

			((EntityTemplate) entity).setContentType(contentType);
			return entity;
		} else if(uri.contains("action=download_rom")){
			MyLog.e("", "download_rom");
			String[] datas = uri.split("&");
			String boardName = "";
			for (String tempStr : datas) {
				if(tempStr.contains("filename")){
					String[] datas2 = tempStr.split("=");
					boardName = datas2[1];
					break;
				}
			}
			
			for (String tempStr : datas) {
				if(tempStr.contains("romname")){
					String[] datas2 = tempStr.split("=");
					boardName = datas2[1];
					break;
				}
			}
			
			filepath = filepath + boardName;

			final File file = new File(filepath);
			
			if (file.isDirectory()) {
				entity = new EntityTemplate(new ContentProducer() {
					public void writeTo(final OutputStream outstream)
							throws IOException {
						OutputStreamWriter writer = new OutputStreamWriter(
								outstream, "UTF-8");
						String resp = "IS DIRECTORY";

						writer.write(resp);
						writer.flush();
					}
				});

				((EntityTemplate) entity).setContentType(contentType);
			} else if (file.exists()) {
				contentType = URLConnection.guessContentTypeFromName(file
						.getAbsolutePath());

				entity = new FileEntity(file, contentType);

				response.setHeader("Content-Type", "binary/octet-stream");
				response.setHeader("Content-Disposition",
						"attachment; filename=" + boardName);
				
				isDownload = true;
			} else {
				entity = new EntityTemplate(new ContentProducer() {
					public void writeTo(final OutputStream outstream)
							throws IOException {
						OutputStreamWriter writer = new OutputStreamWriter(
								outstream, "UTF-8");
						String resp = "NOT FOUND";

						writer.write(resp);
						writer.flush();
					}
				});

				((EntityTemplate) entity).setContentType(contentType);
			}
		} else {
			entity = new EntityTemplate(new ContentProducer() {
				public void writeTo(final OutputStream outstream)
						throws IOException {
					OutputStreamWriter writer = new OutputStreamWriter(
							outstream, "UTF-8");
					String resp = "NOT FOUND";

					writer.write(resp);
					writer.flush();
				}
			});

			((EntityTemplate) entity).setContentType(contentType);
		}
		
//		MyLog.e("", "Content Type = " + contentType);

		return entity;
	}

	private String getFilePath() {
		// PREPARE ROM BIN FILE
		try {
			String rootPath = "";

			if (android.os.Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED)) {
				rootPath = android.os.Environment
						.getExternalStorageDirectory()
						.toString()
						.concat(String.format("/%s/%s", "Android/data",
								context.getPackageName()));
				File appBundle = new File(rootPath);
				if (!appBundle.exists())
					appBundle.mkdir();
			}

			return rootPath.concat("/ROM/");

		} catch (Exception e) {
			return null;
		}
	}
	
	private String getResponseIsFormatSimple(){
		try {
			String rootPath = "";

			if (android.os.Environment.getExternalStorageState().equals(
					android.os.Environment.MEDIA_MOUNTED)) {
				rootPath = android.os.Environment
						.getExternalStorageDirectory()
						.toString()
						.concat(String.format("/%s/%s", "Android/data",
								context.getPackageName()));
				File appBundle = new File(rootPath);
				if (!appBundle.exists())
					appBundle.mkdir();
			}
			
			String filePath = rootPath.concat("/ROM/firmupdate");
			if(MyApplication.intSvrModel == MyApplication.SONCA_KARTROL || MyApplication.intSvrModel == MyApplication.SONCA_KM1){
				filePath = rootPath.concat("/ROM/km1_firmupdate");
			}			
			
			BufferedReader br = new BufferedReader(new FileReader(filePath));
		    
		    try {
		        StringBuilder sb = new StringBuilder();
		        String line = br.readLine();

		        while (line != null) {
		            sb.append(line);
		            sb.append("\n");
		            line = br.readLine();
		        }
		        return sb.toString();
		    } finally {
		        br.close();
		    }

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
