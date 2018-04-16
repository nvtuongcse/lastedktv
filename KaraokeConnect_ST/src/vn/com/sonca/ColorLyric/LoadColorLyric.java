package vn.com.sonca.ColorLyric;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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

import vn.com.sonca.Lyric.ToastBox.OnToastBoxListener;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.RemoteControl.ConvertData;
import vn.com.sonca.params.ByteUtils;
import vn.com.sonca.params.Song;
import vn.com.sonca.utils.AppConfig.LANG_INDEX;
import vn.com.sonca.utils.AppConfig.MEDIA_TYPE;
import vn.com.sonca.zzzzz.MyApplication;
import android.content.Context;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore.Files;
import android.view.MotionEvent;
import android.view.Window;

public class LoadColorLyric extends AsyncTask<Void, Void, Integer> {

	private String TAB = "LoadColorLyric";
	private Context context;
	private Window window;
	private boolean iDecrypt;
	private InputStream mFileInputStream; 
	private int mType; //1: XML, 2: MIDI
	
	private OnLoadColorLyricListener listener;
	public interface OnLoadColorLyricListener{
		public void OnReceiveColorData(Lyrics lyricItems);
	}
	
	public void setOnLoadColorLyricListener(OnLoadColorLyricListener listener){
		this.listener = listener;
	}
	
	public LoadColorLyric(Context context, Window window) {
		this.context = context;
		this.window = window;
		lyricItems = new Lyrics(null, null);
	}

	public static boolean flagShow = false;

	private Song currentSong;
	private int midiShifTime;
	private int idxPosition = 0;
	
	public void setData(Song song, final int position, boolean iDecrypt, int midiShifTime) {
		this.currentSong = song;
		this.iDecrypt=iDecrypt;
		this.midiShifTime = midiShifTime;
		this.idxPosition = position;
	}

	private String lyric = "";
	
	@Override
	protected Integer doInBackground(Void... params) {
		int result = loadDataColorLyric();
		return result;
	}

	@Override
	protected void onPostExecute(Integer result) {
		super.onPostExecute(result);

		if(result == SHOW_SUCCESS){
			if(listener != null){
				listener.OnReceiveColorData(lyricItems);
			}
		} else {
			if(listener != null){
				listener.OnReceiveColorData(null);
			}
		}
	}

	public static final int SHOW_SUCCESS = 1;
	public static final int SHOW_ERROR_MISSING = 2;
	public static final int SHOW_NOLYRIC = 3;

	private int resultInt = -1;

	private int loadDataColorLyric() {
		if(idxPosition == -99){
			lyricItems = checkFileMidi();
			return 1;
		}
		
		if (currentSong == null) {
			return -1;
		}
		resultInt = OnFindLyricDataSong(currentSong);
		return resultInt;
	}

	// ----------------------- PROCESS LYRIC SONG
	
	private int OnFindLyricDataSong(Song song) {
		int song5ID = song.getId();
		int typeABC = song.getTypeABC();
		MEDIA_TYPE mediaType=song.getMediaType();
		MyLog.e(TAB, "OnFindLyricSong: song5ID=" + song5ID+"-ABCType="+typeABC+"=mediaType="+mediaType);

		String appRootPath = android.os.Environment
				.getExternalStorageDirectory()
				.toString()
				.concat(String.format("/%s/%s", "Android/data",
						context.getPackageName()));
		File folderLyric = new File(appRootPath.concat("/COLORLYRIC"));
		if (!folderLyric.exists()) {
			folderLyric.mkdir();
			return SHOW_ERROR_MISSING;
		}

		if (folderLyric.listFiles().length == 0) {
			return SHOW_ERROR_MISSING;
		}

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
				MyLog.e(TAB, "OnFindLyricSong Full VERY START -- " + fileEntry.getName());
				
				String result = readFileLyricFull(appRootPath.concat("/COLORLYRIC/" + fileEntry.getName()),
						song5ID, typeABC,mediaType);
				
				processParseData(result);
				
				if (!result.equals("NULL")) {
					return SHOW_SUCCESS;
				}
			}
		}
		
		for (final File fileEntry : sortedFileName) {
			if (fileEntry.isDirectory()) {
				// do nothing
			} else {
				MyLog.e(TAB, "OnFindLyricSong VERY START -- " + fileEntry.getName());
				
				String result = readFileLyric(appRootPath.concat("/COLORLYRIC/" + fileEntry.getName()),
						song5ID, typeABC,mediaType);
				
				processParseData(result);
				
				if (!result.equals("NULL")) {
					return SHOW_SUCCESS;
				}
			}
		}

		return SHOW_NOLYRIC;
	}
	
	private int OnParseDataMIDILyricFile(InputStream mFileInputStream) {
		MyLog.e(TAB, "OnParseDataMIDILyricFile");
		try {
			lyricItems.parseLyricFromMidiFile(mFileInputStream); 
			return SHOW_SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return SHOW_NOLYRIC;
	}

	// --------------------- PROCESS LYRIC
	
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

	// --------------------- NEW PROCESS LYRIC
	private String readFileLyric(String filePath, int searchID, int typeABC, MEDIA_TYPE mediaType) {
		File file = new File(filePath);
		if(!file.exists()){
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
					if(searchID == id){
						MyLog.d(TAB, "id : " + id);
//					 if(i == sizesong - 1){
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
						break;
						
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
			
			if(searchSuccess){
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
			}else{
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
	
	private String readFileLyricFull(String filePath, int searchID, int typeABC, MEDIA_TYPE mediaType) {
		File file = new File(filePath);
		if(!file.exists()){
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
					if(searchID == id){
						MyLog.d(TAB, "id : " + id);
//					 if(i == sizesong - 1){
						lyric.setIdSong(ConvertData.ByteToInt4(bytes));
						// ---------//
						accessFile.seek(4 + pointerTable + i * LyricData.sizeof());
						accessFile.read(bytes, 0, 1);
						lyric.setTypeABC(ConvertData.ByteToInt1(bytes));
						// ---------//
						accessFile.seek(4 + pointerTable + i * LyricData.sizeof());
						accessFile.read(bytes, 0, 4);
						lyric.setSongProperty(ConvertData.ByteToInt4(bytes));
						// ---------//
						accessFile.seek(8 + pointerTable + i * LyricData.sizeof());
						accessFile.read(bytes, 0, 4);
						lyric.setOffsetLyric(ConvertData.ByteToInt4(bytes));
						// ---------//
						accessFile.seek(12 + pointerTable + i * LyricData.sizeof());
						accessFile.read(bytes, 0, 4);
						lyric.setSizelyric(ConvertData.ByteToInt4(bytes));
						// ---------//
						if(typeABC==100){
							searchSuccess = true;
							break;
						}else{
							if(lyric.getTypeABC() == typeABC && lyric.getMediaType() == mediaType.ordinal()){
								if(MyApplication.flagModelA && (typeABC == 0 || typeABC == 1)){
									if(lyric.isModelA()){
										searchSuccess = true;
										break;
									}
								} else {
									searchSuccess = true;
									break;							
								}
								
							}
						}
					}
				}
				
				if(!searchSuccess && MyApplication.flagModelA){
					for (int i = 0; i < sizesong; i++) {
						accessFile.seek(pointerTable + i * LyricData.sizeof());
						accessFile.read(bytes, 0, 4);
						int id = ConvertData.ByteToInt4(bytes);
						if(searchID == id){
							MyLog.d(TAB, "id : " + id);
//						 if(i == sizesong - 1){
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
							if(lyric.getTypeABC() == typeABC && lyric.getMediaType() == mediaType.ordinal()){
								searchSuccess = true;
								break;
							}
							
						}
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
			
			if(searchSuccess){
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
			}else{
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
	
	// --------------------- PROCESS PARSE LYRIC
	private String formatLyric = "";
	// newest - LYRM
	ByteArrayInputStream bisData = null;
	
	private Document mDocument;
	private SongColor songColor;
	private Lyrics lyricItems;
	private void processParseData(String data){
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
				lyricItems.parseLyricWithDecrypt(mDocument, currentSong, 0);
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
	
	public Lyrics getLyricItems() {
		return lyricItems;
	}

	public void setLyricItems(Lyrics lyricItems) {
		this.lyricItems = lyricItems;
	}
	
	public Document convertToDocument(InputStream is) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setValidating(false);
		documentBuilderFactory.setIgnoringComments(false);
		documentBuilderFactory.setIgnoringElementContentWhitespace(true);
		documentBuilderFactory.setNamespaceAware(true);

		DocumentBuilder documentBuilder = null;
		documentBuilder = documentBuilderFactory.newDocumentBuilder();
		documentBuilder.setEntityResolver(new NullResolver());
		return documentBuilder.parse(is);
	}
	
	public Document convertToDocument(String data) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
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
		public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
			return new InputSource(new StringReader(""));
		}
	}
	
	private void writeToFile(String filePath, String data) {
	    try {
	    	PrintWriter writer = new PrintWriter(filePath);
	    	writer.print(data);
	    	writer.close();
	    }
	    catch (Exception e) {
	        e.printStackTrace();
	    } 
	}
	
	//--------------------------------	
	private Lyrics checkFileMidi(){
		Lyrics result = null;
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
						
			String savePath = rootPath.concat("/" + MyApplication.MIDI_FILE);
			File file = new File(savePath);			
			if(!file.exists()){
				return null;
			}
			
			result = new Lyrics(null, null);			
			FileInputStream fileInputStream = new FileInputStream(file);
			
			if(MyApplication.flagSmartK_801 || MyApplication.flagSmartK_CB){
				mDocument = convertToDocument(fileInputStream);
				result.parseLyricWithDecrypt(mDocument, currentSong, 0);
				fileInputStream.close();
				return result;
			}
			
			result.parseLyricFromMidiFile(fileInputStream);	
			
			fileInputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
}
