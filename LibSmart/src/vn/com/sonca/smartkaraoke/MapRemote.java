package vn.com.sonca.smartkaraoke;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import vn.com.sonca.RemoteControl.ConvertData;
import vn.com.sonca.RemoteControl.RemoteHeader;
import vn.com.sonca.RemoteControl.RemoteIRCode;
import vn.com.sonca.RemoteControl.RemoteModel;
import vn.com.sonca.params.ByteUtils;
import vn.com.sonca.smartkaraoke.NetworkSocket.Receiver;

public class MapRemote {
	private static final int IR_KEY_SIZE     = (4); 
	private static final int g_u32CustomCode = 0x00FD;
	
	public enum AppEvent {
	    UI_EVENT_NULL(0x00), UI_EVENT_1(0x01), UI_EVENT_4(0x02), UI_EVENT_7(0x03),  		 		//  0x00, 0x01, 0x02, 0x03
	    UI_EVENT_STOP(0x04), UI_EVENT_2(0x05), UI_EVENT_5(0x06), UI_EVENT_8(0x07),  				//  0x04, 0x05, 0x06, 0x07
	    UI_EVENT_DOWN(0x09/*UI_EVENT_8+2*/), UI_EVENT_ENTER(0x0a),UI_EVENT_TONE_GOTO(0x0b),  			//  0x08, 0x09, 0x0a, 0x0b
	    UI_EVENT_LEFT(0x0c), UI_EVENT_UP(0x0d),  UI_EVENT_RIGHT(0x0e),         		//  0x0c, 0x0d, 0x0e, 0x0f
	    
	    UI_EVENT_ABC_MINUS(0x10/*UI_EVENT_RIGHT+2*/),UI_EVENT_PAGE_DN(0x12/*UI_EVENT_ABC_MINUS+2*/),    UI_EVENT_TEMPO_UP(0x13), 	//  0x10, 0x11, 0x12, 0x13
	    UI_EVENT_3(0x15/*UI_EVENT_TEMPO_UP+2*/),       UI_EVENT_6(0x16),		      UI_EVENT_9(0x17),    	 		//  0x14, 0x15, 0x16, 0x17
	    UI_EVENT_PAGE_UP(0x18),    UI_EVENT_PLAY_PAUSE(0x19),     UI_EVENT_AUTO(0x1a),      UI_EVENT_VOL_UP(0x1b),	//  0x18, 0x19, 0x1a, 0x1b
	    UI_EVENT_DISPLAY(0x1f/*UI_EVENT_VOL_UP+4*/),		//  0x1c, 0x1d, 0x1e, 0x1f
	    
	    UI_EVENT_A_RED(0x20),      UI_EVENT_B_GREEN(0x21),        UI_EVENT_C_YELLOW(0x22),  UI_EVENT_D_BLUE(0x23),     //  0x20, 0x21, 0x22, 0x23
	    UI_EVENT_AZ_A(0x24),       UI_EVENT_AZ_B(0x25),           UI_EVENT_AZ_C(0x26),      UI_EVENT_AZ_D(0x27),       //  0x24, 0x25, 0x26, 0x27
	    UI_EVENT_AZ_E(0x28),       UI_EVENT_AZ_F(0x29),           UI_EVENT_AZ_G(0x2a),      UI_EVENT_AZ_H(0x2b),       //  0x28, 0x29, 0x2a, 0x2b
	    UI_EVENT_AZ_I(0x2c),       UI_EVENT_AZ_J(0x2d),           UI_EVENT_AZ_K(0x2e),      UI_EVENT_AZ_L(0x2f),       //  0x2c, 0x2d, 0x2e, 0x2f
	    
	    UI_EVENT_AZ_M(0x30),       UI_EVENT_AZ_N(0x31),           UI_EVENT_AZ_O(0x32),      UI_EVENT_AZ_P(0x33),       //  0x30, 0x31, 0x32, 0x33
	    UI_EVENT_AZ_Q(0x34),       UI_EVENT_AZ_R(0x35),           UI_EVENT_AZ_S(0x36),      UI_EVENT_AZ_T(0x37),       //  0x34, 0x35, 0x36, 0x37
	    UI_EVENT_AZ_U(0x38),       UI_EVENT_AZ_V(0x39),           UI_EVENT_AZ_W(0x3a),      UI_EVENT_AZ_X(0x3b),       //  0x38, 0x39, 0x3a, 0x3b
	    UI_EVENT_AZ_Y(0x3c),       UI_EVENT_AZ_Z(0x3d),           UI_EVENT_SYMBOL(0x3e),    UI_EVENT_SPACE(0x3f),       //  0x3c, 0x3d, 0x3e, 0x3f
	    
	    UI_EVENT_CLEAR(0x40),      UI_EVENT_TEMPO_DOWN(0x41),     UI_EVENT_AUD_LANGUAGE(0x43/*UI_EVENT_TEMPO_DOWN+2*/), 	//  0x40, 0x41, 0x42, 0x43
	    UI_EVENT_0(0x44),    UI_EVENT_KEY_UP(0x45),  UI_EVENT_SONG(0x46), UI_EVENT_FF(0x47),  			//  0x44, 0x45, 0x46, 0x47
	    UI_EVENT_SETUP(0x48),      UI_EVENT_KEY_DN(0x49),      	  UI_EVENT_ZOOM(0x4a),      UI_EVENT_PREV(0x4b),        		//  0x48, 0x49, 0x4a, 0x4b
	    UI_EVENT_VIDEO_OUT(0x4c),  UI_EVENT_PAUSE(0x4d),		  UI_EVENT_EJECT(0x4e),     UI_EVENT_SUBP(0x4f), 					//  0x4c, 0x4d, 0x4e, 0x4f
	    
	    UI_EVENT_POWER(0x50),      UI_EVENT_ABC_PLUS(0x51),		  UI_EVENT_ROOT_MENU(0x52), UI_EVENT_FR(0x53),    	//  0x50, 0x51, 0x52, 0x53
	    UI_EVENT_1STRES(0x54),  	 UI_EVENT_PLAY(0x55),           UI_EVENT_REPEAT(0x56),    UI_EVENT_SLOW(0x57),    			//  0x54, 0x55, 0x56, 0x57
	    UI_EVENT_VOL_DN(0x59/*UI_EVENT_SLOW]+2*/),      UI_EVENT_MUTE(0x5a),      UI_EVENT_NEXT(0x5b),  		  	//  0x58, 0x59, 0x5a, 0x5b
	    UI_EVENT_BACK(0x5c),    	 UI_EVENT_HOME(0x5d);
	    
	    private int value;
	    private AppEvent(int value) {
	    
	        this.value = value;
	    }
	    
	    public int getValue() {
	        return this.value;
	    }

	};
	
	private static byte reverse_bits(int bKey)
	{
	    int bRevKey = 0;
	    int i;
	    for (i = 0; i < 8; i++)
	    {
	        bRevKey = (bRevKey << 1) | (bKey & 0x01);
	        bKey = bKey >> 1;
	    }
	    return (byte)bRevKey;
	}
	
	private static int resultSystemCode;
	private static int resultPackageSize;
//	private static byte[] package_IRKey = new byte[IR_KEY_SIZE];
	public static byte[] packageIRKey(Receiver receiver, int even, int model){
		//Log.e("packageIRKey", "IREvent = " + even + " -- remote model = " + model);
		int resultIRCode = processRemoteFile(receiver, model, even);
		if(resultIRCode == -1){
			// Not found or error
		}		
		
		//Log.e("", "result IRCode = " + resultIRCode);
		//Log.e("", "resultSystemCode = " + resultSystemCode);
		//Log.e("", "resultPackageSize = " + resultPackageSize);
		
		byte[] package_IRKey = new byte[resultPackageSize];
		int pos = 0; 
	    ByteUtils.int16ToBytes(package_IRKey, pos, resultSystemCode); 
	    pos+=2; 
	    
	    int w_key = reverse_bits(resultIRCode);
		package_IRKey[pos++] = (byte)w_key; 

		// Revert KeyCode
	    w_key = ~w_key;
	    package_IRKey[pos++] = (byte)w_key; 
		
	    return package_IRKey;
	}
	
	private static int processRemoteFile(Receiver receiver,int searchModel,
			int searchIREvent) {
		String appRootPath = android.os.Environment
				.getExternalStorageDirectory()
				.toString()
				.concat(String.format("/%s/%s", "Android/data",
						((Context)receiver).getPackageName()));

		File folderRemote = new File(appRootPath.concat("/REMOTE"));
		if (!folderRemote.exists()) {
			folderRemote.mkdir();
			return -1;
		}

		if (folderRemote.listFiles().length == 0) {
			return -1;
		}

		for (final File fileEntry : folderRemote.listFiles()) {
			if (fileEntry.isDirectory()) {
				// do nothing
			} else {
				int irCode = readRemoteFile(
						appRootPath.concat("/REMOTE/" + fileEntry.getName()),
						searchModel, searchIREvent);
				return irCode;
			}
		}

		return -1;
	}
	
	private static int readRemoteFile(String filePath, int searchModel,
			int searchIREvent) {		
		// MyLog.e("readRemoteFile", "searchModel = " + searchModel
		// + " -- searchIREvent = " + searchIREvent);
		RandomAccessFile accessFile;
		RemoteHeader myRemoteHeader = new RemoteHeader();
		boolean flagHasModel = false;
		boolean flagHasIRCode = false;
		try {
			File file = new File(filePath);
			accessFile = new RandomAccessFile(file, "r");

			byte[] byte4 = new byte[4];
			byte[] byte2 = new byte[2];
			byte[] byte1 = new byte[1];

			// MyLog.e("readRemoteFile", "start..................");

			// READ REMOTE HEADER
			// accessFile.seek(0);
			// accessFile.read(byte4, 0, 4);
			// myRemoteHeader.setHeader(ConvertData.ByteToInt4(byte4));
			//
			// accessFile.seek(4);
			// accessFile.read(byte1, 0, 1);
			// myRemoteHeader.setVersion(ConvertData.ByteToInt1(byte1));

			accessFile.seek(5);
			accessFile.read(byte1, 0, 1);
			myRemoteHeader.setNumModel(ConvertData.ByteToInt1(byte1));

			// accessFile.seek(6);
			// accessFile.read(byte2, 0, 2);
			// myRemoteHeader.setReserved(ConvertData.ByteToInt2(byte2));

			accessFile.seek(8);
			accessFile.read(byte4, 0, 4);
			myRemoteHeader.setpModelTable(ConvertData.ByteToInt4(byte4));

			// accessFile.seek(12);
			// accessFile.read(byte4, 0, 4);
			// myRemoteHeader.setuModelTableSize(ConvertData.ByteToInt4(byte4));

			// MyLog.e("RemoteHeader", myRemoteHeader.getHeader() + "");
			// MyLog.e("", myRemoteHeader.getVersion() + "");
			// MyLog.e("", myRemoteHeader.getNumModel() + "");
			// MyLog.e("", myRemoteHeader.getReserved() + "");
			// MyLog.e("", myRemoteHeader.getpModelTable() + "");
			// MyLog.e("", myRemoteHeader.getuModelTableSize() + "");

			ArrayList<RemoteModel> allRemoteModel = myRemoteHeader
					.getListRemoteModel();

			// READ EACH MODEL
			for (int i = 0; i < myRemoteHeader.getNumModel(); i++) {
				RemoteModel tempRemoteModel = new RemoteModel();
				int startModelOffset = myRemoteHeader.getpModelTable() + i * 16;

				// accessFile.seek(startModelOffset);
				// accessFile.read(byte1, 0, 1);
				// tempRemoteModel.setManufaturer(ConvertData.ByteToInt1(byte1));

				accessFile.seek(startModelOffset + 1);
				accessFile.read(byte1, 0, 1);
				tempRemoteModel.setModel(ConvertData.ByteToInt1(byte1));

				if (tempRemoteModel.getModel() != searchModel) {
					continue;
				}

				// MyLog.e("Found Model", "..................");
				flagHasModel = true;

				accessFile.seek(startModelOffset + 2);
				accessFile.read(byte1, 0, 1);
				resultPackageSize = ConvertData.ByteToInt1(byte1);
				tempRemoteModel.setPackageSize(resultPackageSize);

				accessFile.seek(startModelOffset + 3);
				accessFile.read(byte1, 0, 1);
				tempRemoteModel.setNumberCode(ConvertData.ByteToInt1(byte1));

				accessFile.seek(startModelOffset + 4);
				accessFile.read(byte2, 0, 2);
				resultSystemCode = ConvertData.ByteToInt2(byte2);
				tempRemoteModel.setSystemCode(resultSystemCode);

				// accessFile.seek(startModelOffset + 6);
				// accessFile.read(byte2, 0, 2);
				// tempRemoteModel.setReserved(ConvertData.ByteToInt2(byte2));

				accessFile.seek(startModelOffset + 8);
				accessFile.read(byte4, 0, 4);
				tempRemoteModel.setpCodeTable(ConvertData.ByteToInt4(byte4));

				// accessFile.seek(startModelOffset + 12);
				// accessFile.read(byte4, 0, 4);
				// tempRemoteModel.setuCodeTableSize(ConvertData.ByteToInt4(byte4));

				// MyLog.e("RemoteModel " + i, tempRemoteModel.getManufaturer()
				// + "");
				// MyLog.e("", tempRemoteModel.getModel() + "");
				// MyLog.e("", tempRemoteModel.getPackageSize() + "");
				// MyLog.e("", tempRemoteModel.getNumberCode() + "");
				// MyLog.e("", tempRemoteModel.getSystemCode() + "");
				// MyLog.e("", tempRemoteModel.getReserved() + "");
				// MyLog.e("", tempRemoteModel.getuCodeTableSize() + "");
				// MyLog.e("", tempRemoteModel.getpCodeTable() + "");

				for (int j = 0; j < tempRemoteModel.getNumberCode(); j++) {
					int startIROffset = tempRemoteModel.getpCodeTable() + j * 2;

					RemoteIRCode tempRemoteIRCode = new RemoteIRCode();

					accessFile.seek(startIROffset);
					accessFile.read(byte1, 0, 1);
					tempRemoteIRCode.setIrEvent(ConvertData.ByteToInt1(byte1));

					if (tempRemoteIRCode.getIrEvent() == searchIREvent) {
						// MyLog.e("Found IREvent", "..................");
						accessFile.seek(startIROffset + 1);
						accessFile.read(byte1, 0, 1);
						accessFile.close();
						return ConvertData.ByteToInt1(byte1);
					}
				}

				if (!flagHasIRCode) {
					accessFile.close();
					return -1;
				}

				allRemoteModel.add(tempRemoteModel);
			}

			// MyLog.e("readRemoteFile", "end..................");
			accessFile.close();
		} catch (Exception e) {
			// MyLog.e("", e.toString());
			return -1;
		}
		return -1;
	}
}
