package vn.com.sonca.samba;

import android.content.Context;
import android.util.Log;
import qpsamba.SambaUtil;
import qpsamba.httpd.NanoStreamer;

public class Samba {
	private Context context;
	public Samba() {
	}
	public Samba(Context context) {
		this.context = context;
	}
	public String getUrl(String path,boolean iSamba){
		 Log.d("Samba", "==getUrl=" + path);
	        if(iSamba){
	        String mime = SambaUtil.getVideoMimeType(path) + "";
	        Log.d("Samba", "==mime=" + mime);
	        if (!String.valueOf(mime).toLowerCase().startsWith("video")) {
	           // Toast.makeText(this, "NOT a video file  " + mime, Toast.LENGTH_SHORT).show();
	            return null;
	        }
	        path = SambaUtil.wrapStreamSmbURL(path, NanoStreamer.INSTANCE().getIp(), NanoStreamer.INSTANCE().getPort());
	        
	        }
	   return path;
	}
}
