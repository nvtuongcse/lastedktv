package vn.com.sonca.WebServer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;

import vn.com.sonca.MyLog.MyLog;

import android.content.Context;

public class NotFoundPageHandler implements HttpRequestHandler {
	private Context context = null;
	
	public NotFoundPageHandler(Context context){
		this.context = context;
	}

	@Override
	public void handle(HttpRequest request, HttpResponse response, HttpContext httpContext) throws HttpException, IOException {
		MyLog.e("NotFoundPageHandler","handle..........................");
		String contentType = "text/html";
		HttpEntity entity = new EntityTemplate(new ContentProducer() {
    		public void writeTo(final OutputStream outstream) throws IOException {
    			MyLog.e("NotFoundPageHandler","writeTo..........................");
    			OutputStreamWriter writer = new OutputStreamWriter(outstream, "UTF-8");
//    			String resp = Utility.openHTMLString(context, R.raw.home);
    			String resp = "Not Found";
            
    			writer.write(resp);
    			writer.flush();
    		}
    	});
		
		((EntityTemplate)entity).setContentType(contentType);
		
		response.setEntity(entity);
	}

}
