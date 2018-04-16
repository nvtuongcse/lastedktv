package vn.com.sonca.Lyric;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.zip.GZIPInputStream;

import vn.com.sonca.Lyric.ToastBox.OnToastBoxListener;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.params.Lyric;
import vn.com.sonca.params.Song;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.Window;

public class LoadLyric extends AsyncTask<Void, Void, Void>{
	
	private String TAB = "LoadLyric";
	private String rootPath = "";
	private Context context;
	private Window window;
	private int idSong;
	private boolean isSonca = true;
	private static ToastBox toastBox = null;
	
	private File dirSonca;
	private File dirUser;
	
	public LoadLyric(Context context, Window window) {
		rootPath = Environment.getExternalStorageDirectory().toString();
		rootPath = rootPath.concat(String.format("/%s/%s", "Android/data",
				context.getPackageName()));
		dirSonca = new File(rootPath.concat("/LYRIC/SONCA"));
		dirUser = new File(rootPath.concat("/LYRIC/USER"));
		this.context = context;
		this.window = window;
	}
	
	private OnLoadLyricListener listener;
	public interface OnLoadLyricListener{
		public void OnFavourite(Song song);
	}
	
	public void setOnLoadLyricListener(OnLoadLyricListener listener){
		this.listener = listener;
	}
	
	public void setData(final Song song , String name) {
		File[] f1 = dirSonca.listFiles();
		File[] f2 = dirUser.listFiles();
		this.isSonca = song.isSoncaSong();
		this.idSong = song.getIndex5();
		if(f1.length != 0 || f2.length != 0){
			toastBox = new ToastBox(context, window);
			toastBox.setOnToastBoxListener(new OnToastBoxListener() {
				@Override public void OnFavourite() {
					if(listener != null){
						listener.OnFavourite(song);
					}
				}

				@Override
				public void OnShowReviewKaraoke(Song song) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void OnPopupPlayYouTube(Song song) {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void OnPopupDownYouTube(Song song) {
					// TODO Auto-generated method stub
					
				}
				
			});
			toastBox.setDataSong(song);
			toastBox.showToast();
		}
	}
	
	public static void hideDialog(){
		if(toastBox != null){
			toastBox.hideToastBox();
		}
	}

	private String lyric = "";
	@Override
	protected Void doInBackground(Void... params) {
//		long tgian = System.currentTimeMillis();
		lyric = "";
		if(isSonca){
			boolean boolReadNext = true;
			File[] file = dirSonca.listFiles();
			if (file != null) {
				for (final File fileEntry : file) {
					String path = fileEntry.getAbsolutePath();
					String ly = readFileLyric(path);
					if (ly != null && !ly.equals("")) {
						MyLog.e(TAB, "Lyric - SONCA - 1 : " + path);
						lyric = ly.replaceAll("[+=]", "").replace("\n\n", "\n");
						boolReadNext = false;
						break;
					}
				}
			}
			if(boolReadNext){
				file = dirUser.listFiles();
				if (file != null) {
					for (final File fileEntry : file) {
						String path = fileEntry.getAbsolutePath();
						String ly = readFileLyric(path);
						if (ly != null && !ly.equals("")) {
							MyLog.e(TAB, "Lyric - USER - 2 : " + path);
							lyric = ly.replaceAll("[+=]", "").replace("\n\n", "\n");
							boolReadNext = false;
							break;
						}
					}
				}
			}
			// MyLog.e(TAB, lyric);
		}else{
			boolean boolReadNext = true;
			File[] file = dirUser.listFiles();
			if (file != null) {
				for (final File fileEntry : file) {
					String path = fileEntry.getAbsolutePath();
					String ly = readFileLyric(path);
					if (ly != null && !ly.equals("")) {
						MyLog.e(TAB, "Lyric - USER - 1 : " + path);
						lyric = ly.replaceAll("[+=]", "").replace("\n\n", "\n");
						boolReadNext = false;
						break;
					}
				}
			}
			if(boolReadNext){
				file = dirSonca.listFiles();
				if (file != null) {
					for (final File fileEntry : file) {
						String path = fileEntry.getAbsolutePath();
						String ly = readFileLyric(path);
						if (ly != null && !ly.equals("")) {
							MyLog.e(TAB, "Lyric - SONCA - 2 : " + path);
							lyric = ly.replaceAll("[+=]", "").replace("\n\n", "\n");
							boolReadNext = false;
							break;
						}
					}
				}
			}
			
		}
		MyLog.e(TAB, lyric);
		/*
		String ly = readFileLyric(name);
		if(ly != null){
			lyric = ly.replaceAll("[+=]", "");
		}
		*/
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		if(toastBox != null && lyric != null){
			toastBox.setDataLyric(lyric);
		}
	}
	
	private String readFileLyric(String path) {
		File file = new File(path);
		if(!file.exists()){
			return "";
		}
		RandomAccessFile accessFile;
		try {
			accessFile = new RandomAccessFile(file, "r");
			accessFile.seek(0);
			byte[] bytes = new byte[4];
			accessFile.seek(4);
			accessFile.read(bytes, 0, 4);
			int sizesong = ConvertData.ByteToInt(bytes);

			accessFile.seek(8);
			accessFile.read(bytes, 0, 4);
			int pointerTable = ConvertData.ByteToInt(bytes);

			Lyric lyric = new Lyric();
			boolean searchSuccess = false;
			for (int i = 0; i < sizesong; i++) {
				accessFile.seek(pointerTable + i * Lyric.sizeof());
				accessFile.read(bytes, 0, 4);
				int id = ConvertData.ByteToInt(bytes);
				// MyLog.d(TAB, "id : " + id);
				if(idSong == id){
				// if(i == sizesong - 1){
					searchSuccess = true;
					lyric.setIdSong(ConvertData.ByteToInt(bytes));
					// ---------//
					accessFile.seek(8 + pointerTable + i * Lyric.sizeof());
					accessFile.read(bytes, 0, 4);
					lyric.setOffsetLyric(ConvertData.ByteToInt(bytes));
					// ---------//
					accessFile.seek(12 + pointerTable + i * Lyric.sizeof());
					accessFile.read(bytes, 0, 4);
					lyric.setSizelyric(ConvertData.ByteToInt(bytes));
					// ---------//
					break;
				}
			}
			if(searchSuccess){
				accessFile.seek(lyric.getOffsetLyric());
				byte[] data = new byte[lyric.getSizelyric()];
				accessFile.read(data, 0, lyric.getSizelyric());
				String ly = new String(unpackRaw(data));
				return ly;
			}else{
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	public static byte[] unpackRaw(byte[] b) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ByteArrayInputStream bais = new ByteArrayInputStream(b);
		GZIPInputStream zis = new GZIPInputStream(bais);
		byte[] tmpBuffer = new byte[256];
		int n;
		while ((n = zis.read(tmpBuffer)) >= 0)
			baos.write(tmpBuffer, 0, n);
		zis.close();
		return baos.toByteArray();
	}

	
}
