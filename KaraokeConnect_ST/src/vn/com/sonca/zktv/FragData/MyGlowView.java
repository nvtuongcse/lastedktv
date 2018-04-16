package vn.com.sonca.zktv.FragData;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.params.Song;
import vn.com.sonca.zktv.FragData.ButFirst.OnButFirstListener;
import vn.com.sonca.zktv.FragData.MySongView.OnMySongViewListener;
import vn.com.sonca.zzzzz.MyApplication;

public class MyGlowView extends ViewGroup {
	
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public MyGlowView(Context context) {
		super(context);
		initView(context);
	}

	public MyGlowView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public MyGlowView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private OnGroupSongListener listener;
	public interface OnGroupSongListener {
		public void OnFirstClick(Song song, int position, int x, int y);
		public void OnSingerLink(Song song);
		public void OnSongLick(Song song, int x, int y);
		public void OnClearLayout(int pos);
		public void OnPlayYouTube(Song song);
		public void OnDownYouTube(Song song);
	}
	
	public void setOnGroupSongListener(OnGroupSongListener listener){
		this.listener = listener;
	}
	
	private boolean isLongClick = false;
	private void initView(Context context) {
		setWillNotDraw(false);
		isLongClick = false;
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	private int cirX, cirY, radius;
	private int startX, startY, stopX, stopY;
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		startX = (int) (1.25*h);
		stopX = (int) (0.99*w);
		startY = stopY = 2;
		
		cirY = (int) (0.5*h);
		cirX = (int) (0.6*h);
		radius = (int) (0.3*h);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(position != 1){
			paintMain.setColor(Color.parseColor("#016D7F"));
			canvas.drawLine(startX, startY, stopX, stopY, paintMain);
		}
	}
	
	private int color;
	private int position;
	public void setPosition(int position, int color, boolean isActiveView){
		this.position = position;
		this.color = color;
		invalidate();
		if(position < 0){
			setDataSong(null);
		}
	}
	
	private MySongView view;

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int width = getWidth();
		int height = getHeight();
		view = (MySongView)getChildAt(0);
		final ButFirst butFirst = (ButFirst)getChildAt(1);
		final ButSinger butSinger = (ButSinger)getChildAt(2);
		final ButPreview butPreview = (ButPreview)getChildAt(3);
		final ButDownload butDownload = (ButDownload)getChildAt(4);
		final ButCancelDownload butCancelDownload = (ButCancelDownload)getChildAt(5);
		
		if(isLongClick == true){
			if(song != null && ((MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && song.isYoutubeSong()) || 
					(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && (MyApplication.flagSmartK_801 || MyApplication.flagSmartK_CB) 
					&& song.isOfflineSong() == false))){
				butFirst.layout(
						(int)(width - 4.4*height), 0, 
						(int)(width - 3.3*height), height);
				
				butPreview.layout(
						(int)(width - 3.31*height), 0, 
						(int)(width - 1.91*height), height);
				
				if(MyApplication.youtube_Download_ID == song.getId()){
					butDownload.layout(0,0,0,0);
					butCancelDownload.layout((int)(width - 2.0*height), 0, width, height);
				} else {
					if(MyApplication.flagOnAdminOnline){
						butDownload.layout((int)(width - 2.0*height), 0, width, height);
					} else {
						butDownload.layout(0,0,0,0);	
					}					
					butCancelDownload.layout(0,0,0,0);
				}
				
				butSinger.layout(0,0,0,0);
				
			} else {
				butSinger.layout((int)(width - 2.1*height), 0, width, height);
				butFirst.layout(
						(int)(width - 3.4*height), 0, 
						(int)(width - 2.1*height), height);
				
				butPreview.layout(0,0,0,0);
				butDownload.layout(0,0,0,0);
				butCancelDownload.layout(0,0,0,0);
			}
			
		}else{
			butSinger.layout(0,0,0,0);
			butFirst.layout(0,0,0,0);
			butPreview.layout(0,0,0,0);
			butDownload.layout(0,0,0,0);
			butCancelDownload.layout(0,0,0,0);
		}
		
		// for song follow
		if(isShowLayoutLongClick == true){
			butSinger.layout(0,0,0,0);
			butPreview.layout(0,0,0,0);
			butDownload.layout(0,0,0,0);
			butCancelDownload.layout(0,0,0,0);
		}
		
		view.layout(0, 0, width, height);
		view.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View arg0) {
				
			}
		});		
		view.setOnMySongViewListener(new OnMySongViewListener() {
			
			@Override
			public void OnSongLick(Song song, int x, int y) {
				if(view.getDataSong() == null){
					return;
				}
				
				view.startAnimation(false);
				isLongClick = false;
				if(listener != null){
					listener.OnSongLick(view.getDataSong(), x, y);
				}
			}
		});
		// if(isShowLayoutLongClick == false){
			view.setOnLongClickListener(new OnLongClickListener() {
				@Override public boolean onLongClick(View arg0) {
					if(view.getDataSong() == null){
						return true;
					}
					
//					if(isShowLayoutLongClick == false){
						view.startAnimation(true);
						isLongClick = true;
						requestLayout();
						if(listener != null){
							listener.OnClearLayout(position);
						}
//					}
					return true;
				}
			});
		// }
			
		butFirst.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
			}
		});
		butFirst.setOnButFirstListener(new OnButFirstListener() {
			
			@Override
			public void OnFristRes(View arg0, int x, int y) {
				if(view.getDataSong() == null){
					return;
				}
				
				if(listener != null){
					listener.OnFirstClick(view.getDataSong(), position, x, y);
				}
			}
		});
		
		butSinger.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(view.getDataSong() == null){
					return;
				}
				
				if(listener != null){
					listener.OnSingerLink(view.getDataSong());
				}
			}
		});
		
		butPreview.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(view.getDataSong() == null){
					return;
				}
				
				if(listener != null){
					listener.OnPlayYouTube(view.getDataSong());
				}
			}
		});
		
		butDownload.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(view.getDataSong() == null){
					return;
				}
				
				if(listener != null){
					listener.OnDownYouTube(view.getDataSong());
				}
			}
		});
		
		butCancelDownload.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(view.getDataSong() == null){
					return;
				}
				
				if(listener != null){
					listener.OnDownYouTube(view.getDataSong());
				}
			}
		});
		
		
		
	}
	
	private boolean isShowLayoutLongClick = false;
	public void setShowLayoutLongClick(boolean isShowLayoutLongClick){
		this.isShowLayoutLongClick = isShowLayoutLongClick;
	}
	
	public void setBoolLongClick(boolean flag){
		this.isLongClick = flag;
		view.setBoolLongClick(flag);
//		requestLayout();
	}
	
	private Song song;
	
	public void setDataSong(Song song){ 
		this.song = song;
		MySongView view = (MySongView)getChildAt(0);
		ButFirst first = (ButFirst)getChildAt(1);
		ButSinger singer = (ButSinger)getChildAt(2);
		ButPreview butPreview = (ButPreview)getChildAt(3);
		ButDownload butDownload = (ButDownload)getChildAt(4);
		ButCancelDownload butCancelDownload = (ButCancelDownload)getChildAt(5);
		if(view != null){
			view.setDataSong(position, color, song);
			first.setDrawView(song != null);
			singer.setDrawView(song != null);
			butPreview.setDrawView(song != null);
			butDownload.setDrawView(song != null);
			butCancelDownload.setDrawView(song != null);
			isLongClick = false;
		}
	}
	
	public void clearLayoutView(){
		ButFirst first = (ButFirst)getChildAt(1);
		ButSinger singer = (ButSinger)getChildAt(2);
		ButPreview butPreview = (ButPreview)getChildAt(3);
		ButDownload butDownload = (ButDownload)getChildAt(4);
		ButCancelDownload butCancelDownload = (ButCancelDownload)getChildAt(5);
		if(view != null){
			Song song = view.getDataSong();
			view.setIsLongClick(false);
			first.setDrawView(song != null);
			singer.setDrawView(song != null);
			butPreview.setDrawView(song != null);
			butDownload.setDrawView(song != null);
			butCancelDownload.setDrawView(song != null);
			isLongClick = false;
			requestLayout();
		}
	}
	
	public void setApplication(MyApplication application, boolean isShowLayoutLongClick){
		this.isShowLayoutLongClick = isShowLayoutLongClick;
		MySongView view = (MySongView)getChildAt(0);
		if(view != null){
			view.setApplication(application);
		}
		requestLayout();
	}

}
