package vn.com.sonca.Lyric;

import java.io.File;
import java.util.ArrayList;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.database.DBInterface;
import vn.com.sonca.params.Musician;
import vn.com.sonca.params.Singer;
import vn.com.sonca.params.Song;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.View;

public class ImageSinger extends View {
	
	private final String sdcard = Environment.getExternalStorageDirectory() + "/Android/data/";
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public ImageSinger(Context context) {
		super(context);
		initView(context);
	}

	public ImageSinger(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public ImageSinger(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Context context;
	private void initView(Context context) {
		this.context = context;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(heightMeasureSpec, heightMeasureSpec);		
		int myWidth = (int) (0.4*getMeasuredWidth());	
		setMeasuredDimension(myWidth, myWidth);
	}
	
	private int widthView;
	private int heightView;
	private int tamX, tamY, radius;

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		widthView = w;
		heightView = h;
		tamX = widthView/2;
		tamY = heightView/2;
		radius = (int) (0.48*widthView);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		/*
		paintMain.setStyle(Style.FILL);
		paintMain.setColor(Color.WHITE);
		canvas.drawRect(0, 0, widthView, heightView, paintMain);
		*/
		if(bitmapShader != null){
			paintMain.setShader(bitmapShader);
		    paintMain.setAntiAlias(true);
		    paintMain.setARGB(255, 0, 0, 0);
		    canvas.drawCircle(tamX, tamY, radius, paintMain);
		    paintMain.setStyle(Style.FILL);
		}else{
			
			loadImage = new LoadImage(song);
			loadImage.setLayout(widthView, heightView);
			loadImage.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			
		}
		
	}
	
	
/////////////////////////////////////////////////////////////
	
	private Song song;
	public void setData(Song song){
		this.song = song;
		invalidate();
	}
	
	private void clearData(){
		if(loadImage != null){
			loadImage.cancel(true);
			loadImage = null;
		}
		if(bitmapShader != null){
			bitmapShader = null;
		}
		bitmapShader = null;
	}
	
	private LoadImage loadImage;
	private BitmapShader bitmapShader = null;
	private class LoadImage extends AsyncTask<Void, Void, Void> {
		
		private Song song;
		private int width, height;
		
		public LoadImage(Song song) {
			this.song = song;
		}
		
		public void setLayout(int width, int height){
			this.width = width;
			this.height = height;
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				ArrayList<Integer> idList = DBInterface
						.DBGetAuthorIDFromSong(context, song.getId(), song.getTypeABC());
				int cover = -1;
				if (!idList.isEmpty()) {
					int idMusician = idList.get(0);
					MyLog.e("LoadImage", "idMusician : " + idMusician);
					Musician musician = DBInterface.DBGetOneMusician(context, "" + idMusician);
					cover = musician.getCoverID();
				}
				String path = sdcard + context.getPackageName() + "/PICTURE/" + cover;
				MyLog.e("LoadImage", path);
				Bitmap bitmap;
				if(new File(path).exists()){
					bitmap = BitmapFactory.decodeFile(path);
					if(isCancelled()){
						if(bitmap != null)
							bitmap.recycle();
						return null;
					}
				}else{
					bitmap = BitmapFactory.decodeResource(getResources(), R.raw.a1);
				}
				Bitmap bit = Bitmap.createScaledBitmap(bitmap, width, height, true);
					if(isCancelled()){
						if(bit != null)
							bit.recycle();
						return null;
					}
				bitmapShader = new BitmapShader(bit, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
					if(isCancelled()){
						if(bitmapShader != null)
							bitmapShader = null;
						return null;
					}
					
				// if(bitmap != null)
					 // bitmap.recycle();
				// NameSinger = nameSaveTam;
			} catch (Exception ex) {
				ex.printStackTrace();
				if(bitmapShader != null)
					bitmapShader = null;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			invalidate();
		}

	}
	

}
