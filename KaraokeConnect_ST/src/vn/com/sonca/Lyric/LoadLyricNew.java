package vn.com.sonca.Lyric;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.zip.GZIPInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import vn.com.sonca.ColorLyric.LoadColorLyric;
import vn.com.sonca.ColorLyric.LyricData;
import vn.com.sonca.ColorLyric.Lyrics;
import vn.com.sonca.ColorLyric.SongColor;
import vn.com.sonca.ColorLyric.LoadColorLyric.OnLoadColorLyricListener;
import vn.com.sonca.Lyric.ToastBox.OnToastBoxListener;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.RemoteControl.ConvertData;
import vn.com.sonca.params.ByteUtils;
import vn.com.sonca.params.Lyric;
import vn.com.sonca.params.Song;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.Window;

public class LoadLyricNew extends AsyncTask<Void, Void, Void> {

	private String TAB = "LoadLyric";
	private String rootPath = "";
	private Context context;
	private Window window;
	private int idSong;
	private static ToastBox toastBox = null;

	private File dirSonca;

	public LoadLyricNew(Context context, Window window) {
		rootPath = Environment.getExternalStorageDirectory().toString();
		rootPath = rootPath.concat(String.format("/%s/%s", "Android/data",
				context.getPackageName()));
		dirSonca = new File(rootPath.concat("/COLORLYRIC"));
		this.context = context;
		this.window = window;
	}

	private OnLoadLyricListener listener;

	public interface OnLoadLyricListener {
		public void OnShowReviewKaraoke(Song song);
		public void OnFavourite(Song song);
		public void OnPopupPlayYouTube(Song song);
		public void OnPopupDownYouTube(Song song);
	}

	public void setOnLoadLyricListener(OnLoadLyricListener listener) {
		this.listener = listener;
	}

	private Song mySong;

	public void setData(final Song song, String name) {
		File[] f1 = dirSonca.listFiles();
		this.idSong = song.getIndex5();
		this.mySong = song;
//		if (f1.length != 0) {
			toastBox = new ToastBox(context, window);
			toastBox.setOnToastBoxListener(new OnToastBoxListener() {
				@Override
				public void OnFavourite() {
					if (listener != null) {
						listener.OnFavourite(song);
					}
				}

				@Override
				public void OnShowReviewKaraoke(Song song) {
					if(listener != null){
						listener.OnShowReviewKaraoke(song);
					}
				}
				
				@Override
				public void OnPopupPlayYouTube(Song song) {
					if(listener != null){
						listener.OnPopupPlayYouTube(song);
					}
				}
				@Override
				public void OnPopupDownYouTube(Song song) {
					if(listener != null){
						listener.OnPopupDownYouTube(song);
					}
				}
				
			});
			
			toastBox.setDataSong(song);
			toastBox.showToast();
//		}

	}

	public static void hideDialog() {
		if (toastBox != null) {
			toastBox.hideToastBox();
		}
	}

	private String lyric = "";

	@Override
	protected Void doInBackground(Void... params) {
		lyric = "";

		int song5ID = mySong.getId();
		int typeABC = mySong.getTypeABC();
		
		File folderLyric = dirSonca;
		if (!folderLyric.exists()) {
			folderLyric.mkdir();
		}

		if (folderLyric.listFiles().length == 0) {
			return null;
		}

		String result = "NULL";
		
		final File[] sortedFileName = folderLyric.listFiles();
		if (sortedFileName != null && sortedFileName.length > 1) {
			Arrays.sort(sortedFileName, new Comparator<File>() {
				@Override
				public int compare(File object1, File object2) {
					return object1.getName().compareTo(object2.getName());
				}
			});
		}
		
		for (final File fileEntry : sortedFileName) {
			if (fileEntry.isDirectory()) {
				// do nothing
			} else {
				MyLog.e(TAB,
						"OnFindLyricSong VERY START -- " + fileEntry.getName());

				result = readFileLyric(
						rootPath.concat("/COLORLYRIC/" + fileEntry.getName()),
						song5ID, typeABC);		
				
				if (!result.equals("NULL")) {
					break;
				}
			}
		}
		
		if (result.equals("NULL") || result == null) {
			return null;
		}
		
//		MyLog.e(TAB, "OnFindLyricSong VERY END -- " + result);
		processParseData(result);
		
		if(!lyricItems.getLyricView().equals("")){
			String strResult = "Title: " + lyricItems.getSongTitle();
			strResult += "\n" + "Musician: " + lyricItems.getSongAuthor();
			strResult += "\n" + "Lyrician: -";
			strResult += "\n" + "Singer: " + lyricItems.getSongSinger();
			
			String strLyric = lyricItems.getLyricView();
			strResult += "\n" + strLyric.replaceAll("[,.]", "\n").replaceAll("^\\s+", "");
			
			lyric = strResult;
			
//			MyLog.e(TAB, lyric);
		}

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		if (toastBox != null && lyric != null) {
			toastBox.setDataLyric(lyric);
		}
	}

	// --------------------- NEW PROCESS LYRIC
	private String formatLyric = "";
	// newest - LYRM
	
	private String readFileLyric(String filePath, int searchID, int typeABC) {		
		File file = new File(filePath);
		if (!file.exists()) {
			return "NULL";
		}
		RandomAccessFile accessFile = null;
		try {
			accessFile = new RandomAccessFile(file, "r");
			accessFile.seek(0);
			byte[] bytes = new byte[4];
			accessFile.read(bytes, 0, 4);
			formatLyric = new String(bytes);
			MyLog.e(TAB, "FORMAT = " + formatLyric);
			accessFile.seek(4);
			accessFile.read(bytes, 0, 4);
			int sizesong = ConvertData.ByteToInt4(bytes);

			MyLog.e(TAB, "readFileLyric -- sizesong = " + sizesong);

			accessFile.seek(8);
			accessFile.read(bytes, 0, 4);
			int pointerTable = ConvertData.ByteToInt4(bytes);

			MyLog.e(TAB, "readFileLyric -- pointerTable = " + pointerTable);

			LyricData lyric = new LyricData();
			boolean searchSuccess = false;
			
			if(formatLyric.equals("LYRM")){
				for (int i = 0; i < sizesong; i++) {
					accessFile.seek(pointerTable + i * LyricData.sizeof());
					accessFile.read(bytes, 0, 4);
					int id = ConvertData.ByteToInt4(bytes);
					if (searchID == id) {
						MyLog.d(TAB, "id : " + id);
						// if(i == sizesong - 1){
						lyric.setIdSong(ConvertData.ByteToInt4(bytes));
						// ---------//
						accessFile.seek(4 + pointerTable + i * LyricData.sizeof());
						accessFile.read(bytes, 0, 1);
						lyric.setTypeABC(ConvertData.ByteToInt1(bytes));
						// ---------//
						accessFile.seek(8 + pointerTable + i * LyricData.sizeof());
						accessFile.read(bytes, 0, 4);
						lyric.setOffsetLyric(ConvertData.ByteToInt4(bytes));
						// ---------//
						accessFile.seek(12 + pointerTable + i * LyricData.sizeof());
						accessFile.read(bytes, 0, 4);
						lyric.setSizelyric(ConvertData.ByteToInt4(bytes));
						// ---------//

						if(lyric.getTypeABC() == typeABC){
							MyLog.d(TAB, "same typeABC");
							searchSuccess = true;
							break;
						}
						
						MyLog.d(TAB, "not same typeABC");
					}
				}
				
			} else {
				for (int i = 0; i < sizesong; i++) {
					accessFile.seek(pointerTable + i * LyricData.sizeof());
					accessFile.read(bytes, 0, 4);
					int id = ConvertData.ByteToInt4(bytes);
					if (searchID == id) {
						MyLog.d(TAB, "id : " + id);
						// if(i == sizesong - 1){
						lyric.setIdSong(ConvertData.ByteToInt4(bytes));
						// ---------//
						accessFile.seek(4 + pointerTable + i * LyricData.sizeof());
						accessFile.read(bytes, 0, 1);
						lyric.setTypeABC(ConvertData.ByteToInt1(bytes));
						// ---------//
						accessFile.seek(8 + pointerTable + i * LyricData.sizeof());
						accessFile.read(bytes, 0, 4);
						lyric.setOffsetLyric(ConvertData.ByteToInt4(bytes));
						// ---------//
						accessFile.seek(12 + pointerTable + i * LyricData.sizeof());
						accessFile.read(bytes, 0, 4);
						lyric.setSizelyric(ConvertData.ByteToInt4(bytes));
						// ---------//
						searchSuccess = true;
						MyLog.d(TAB,
								lyric.getIdSong() + " -- " + lyric.getOffsetLyric()
										+ " -- " + lyric.getSizelyric());
						break;
					}
				}
				
			}			
			
			if (searchSuccess) {
				accessFile.seek(lyric.getOffsetLyric());
				byte[] data = new byte[lyric.getSizelyric()];
				accessFile.read(data, 0, lyric.getSizelyric());
				
				if(formatLyric.equals("LYRM")){
					byte[] newData = DecryptLyricData(data, searchID, typeABC);
					
					bisData = new ByteArrayInputStream(newData);
					return "ABC";
				} else {
					String ly = new String(unpackGzip(data));
					return ly;	
				}
				
			} else {
				return "NULL";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "NULL";
		} finally {
			if (accessFile != null) {
				try {
					accessFile.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}
	}

	private static byte[] unpackGzip(byte[] b) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ByteArrayInputStream bais = new ByteArrayInputStream(b);
		GZIPInputStream zis = new GZIPInputStream(bais);
		byte[] tmpBuffer = new byte[1024];
		int n;
		while ((n = zis.read(tmpBuffer)) >= 0)
			baos.write(tmpBuffer, 0, n);
		zis.close();
		return baos.toByteArray();
	}

	// --------------------- PROCESS PARSE LYRIC
	private Document mDocument;
	private Lyrics lyricItems;

	ByteArrayInputStream bisData = null;

	private void processParseData(String data) {
		try {
			if(formatLyric.equals("LYRM")){
				
				lyricItems = new Lyrics(null, null);
				if(bisData != null){
					lyricItems.parseLyricFromMidiFile(bisData);	
				}	
				
//				for (SongDetail songDetail : lyricItems.mDetails) {
//					System.out.printf("############songdetails############\n");
//	
//					System.out.printf("############Countstart Details############\n");
//					for(WordSong count : songDetail.getCountStarts()) {
//							System.out.printf("countStart:%s start:%d len:%d\n", count.getWord(), count.getTimeStart(), count.getTimeLenght());
//					}
//					
//					System.out.printf("############Sentence Details############\n");
//					for(SentenceSong sentence : songDetail.getSentences()) {
//						System.out.printf("sentence:%s start:%d len:%d line:%d\n", sentence.getSentence(), sentence.getTimeStart(), sentence.getTimeLenght(), sentence.getLine());
//						for(WordSong word : sentence.getWords()) {
//							System.out.printf("word:%s start:%d len:%d\n", word.getWord(), word.getTimeStart(), word.getTimeLenght());
//						}
//					}
//					
//					System.out.printf("############Sentence End Details############\n");
//					System.out.printf("End:%s start:%d len:%d\n", songDetail.getEnd().getWord(), songDetail.getEnd().getTimeStart(), songDetail.getEnd().getTimeLenght());
//				}
				
			} else {
				lyricItems = new Lyrics(null, null);
				mDocument = convertToDocument(data);
				// songColor = new SongColor();
				// songColor.parseLyricWithDecrypt(mDocument);
				lyricItems.parseLyricWithDecrypt(mDocument, mySong, 0);
				// MyLog.d(TAB,"=LyricView="+
				// lyricItems.getLyricView()+"=CountStart="+
				// lyricItems.getLastSentences(0)
				// +"=Sentence="+
				// lyricItems.getSentenceFromPosition(0)+"=getSongAuthor="+
				// lyricItems.getSongAuthor()+"=getSongSinger="+
				// lyricItems.getSongSinger()+
				// "=getSongTitle="+ lyricItems.getSongTitle()+"=CountStart="+
				// lyricItems.getCountStartFromPosition(0));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static byte[] DecryptLyricData(byte[] nBytes, int password,
			int typeABC) {
//		if (typeABC > 0) {
//			password = password + 1000000 * (typeABC - 1);
//		}

		try {
			byte[] hashBytes;
			byte[] pwdBytes;
			byte[] retBytes;
			int hashLength;

			// Hash file
			hashLength = nBytes[0];
			hashBytes = new byte[hashLength];
			System.arraycopy(nBytes, 19, hashBytes, 0, hashLength);

			// Password bytes
			pwdBytes = new byte[4];
			ByteUtils.intToBytes(pwdBytes, 0, password);
			
			// enc
			if (hashBytes.length > 0) {
				// Get data first
				retBytes = new byte[nBytes.length - 1 - hashBytes.length];
				System.arraycopy(nBytes, 1, retBytes, 0, 18);
				System.arraycopy(nBytes, 19 + hashBytes.length, retBytes, 18,
						retBytes.length - 18);

				// Decr hash bytes
				for (int i = 0; i < hashBytes.length; i++) {
					hashBytes[i] = (byte) (hashBytes[i] ^ (pwdBytes[i % 4])); // 1nd
				}

				// Write dec data
				for (int i = 0; i < retBytes.length; i++) {
					retBytes[i] = (byte) (retBytes[i] ^ (pwdBytes[i % 4])); // 1st
					retBytes[i] = (byte) (retBytes[i] + 25); // 2nd
				}

				// DeCompress buffer
//				retBytes = csLib.Files.DeCompressBuffer(retBytes, false);
				byte[] resultBytes = unpackGzip(retBytes);

				// End
				return resultBytes;
			} else {
				throw new Exception("Encryp fail. Check more");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public Lyrics getLyricItems() {
		return lyricItems;
	}

	public void setLyricItems(Lyrics lyricItems) {
		this.lyricItems = lyricItems;
	}

	public Document convertToDocument(String data)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
				.newInstance();
		documentBuilderFactory.setValidating(false);
		documentBuilderFactory.setIgnoringComments(false);
		documentBuilderFactory.setIgnoringElementContentWhitespace(true);
		documentBuilderFactory.setNamespaceAware(true);

		DocumentBuilder documentBuilder = null;
		documentBuilder = documentBuilderFactory.newDocumentBuilder();
		documentBuilder.setEntityResolver(new NullResolver());
		InputSource is = new InputSource(new StringReader(data));
		return documentBuilder.parse(is);
	}

	class NullResolver implements EntityResolver {
		public InputSource resolveEntity(String publicId, String systemId)
				throws SAXException, IOException {
			return new InputSource(new StringReader(""));
		}
	}

	private void writeToFile(String filePath, String data) {
		try {
			PrintWriter writer = new PrintWriter(filePath);
			writer.print(data);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
