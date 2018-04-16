package vn.com.sonca.zktv.FragPlaylist;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.Layout.Alignment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.params.Song;
import vn.com.sonca.utils.AppConfig.MEDIA_TYPE;
import vn.com.sonca.zzzzz.MyApplication;

public class MyPlaylistView extends View {
	
	private String TAB = "MySongView";
	private TextPaint textNumber = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	private Paint paintMain = new Paint(Paint.ANTI_ALIAS_FLAG);
	
	public MyPlaylistView(Context context) {
		super(context);
		initView(context);
	}

	public MyPlaylistView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		initView(context);
	}

	public MyPlaylistView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}
	
	private Drawable drawChangPerfect, drawSinger, drawRemix, drawMidi, drawKTV, drawKTVVid;
	private Drawable drawable, drawUser, drawOnline, drawYouTube;
	
	private Animation anime;
	private void initView(Context context) {
		anime = AnimationUtils.loadAnimation(context, R.anim.animation_song);
		paintMain.setStyle(Style.FILL);
		textNumber.setStyle(Style.FILL);
		textNumber.setTextAlign(Align.CENTER);
		textNumber.setColor(Color.WHITE);
		textNumber.setTypeface(Typeface.DEFAULT_BOLD);
		
		drawChangPerfect = getResources().getDrawable(R.drawable.touch_icon_hoaam);
		drawSinger = getResources().getDrawable(R.drawable.touch_song_vocal_48x48);
		drawRemix = getResources().getDrawable(R.drawable.touch_image_remix_62x35);
		drawMidi = getResources().getDrawable(R.drawable.touch_image_midi_50x35);
		drawKTV = getResources().getDrawable(R.drawable.icon_ktv_1note);
		drawKTVVid = getResources().getDrawable(R.drawable.icon_ktv_video);
		
		drawUser = getResources().getDrawable(R.drawable.touch_icon_user);
		drawOnline = getResources().getDrawable(R.drawable.icon_icloud);
		drawYouTube = getResources().getDrawable(R.drawable.youtube_icon);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	public void startAnimation(boolean isLongClick){
		this.isLongClick = isLongClick;
		this.startAnimation(anime);
	}
	
	private boolean isLongClick = false;
	public void setIsLongClick(boolean isLongClick){
		this.isLongClick = isLongClick;
		invalidate();
	}
	
	private float transX;
	private float transY;
	private int widthLayout = 0;
    private int heightLayout = 0;
	private int tsongS, tsongX, tsongY;
	private int tsingerS, tsingerX, tsingerY;
	private int startX, startY, stopX, stopY;
	private int numberX, numberY, numberS;
	private int cirX, cirY, radius;
	private int textS, textX, textY;

	private Rect rectKTV = new Rect();
	private Rect rectMIDI = new Rect();
	private Rect rectSinger = new Rect();
	private Rect rectRemix = new Rect();
	private Rect rectChangPerfect = new Rect();
	
	private MEDIA_TYPE ismedia;
	private int intABC = 0;
	private boolean isRemix;
	private boolean isSinger;
	private boolean isUser;
	
	private Rect rectUser = new Rect();
	private Rect rectYouTube = new Rect();
	private Rect rectYouTube2 = new Rect();
	private Rect rectOnline = new Rect();
	
	private float ptS, ptY;
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		widthLayout = w;
		heightLayout = h;
	}
	
	private void setK(){
		int w = widthLayout;
		int h = heightLayout;
		
		transX = (float) (1.25*h);
		transY = (float) (0.13*h);
		tsongS = textS = (int) (0.5*h);
		tsingerS =  (int) (0.35*h);
		tsingerX = (int) (0.97*w);
		tsingerY = (int) (0.3*h + 0.4*tsingerS);
		
		textX = (int) (1.5*h);
		textY = (int) (0.7*h);
		
		startX = (int) (1.25*h);
		stopX = (int) (0.99*w);
		startY = stopY = 2;

		cirY = (int) (0.5*h);
		cirX = (int) (0.65*h);
		radius = (int) (0.3*h);
		
		numberS = (int) (0.4*h);
		numberX = (int) (0.65*h);
		numberY = (int) (0.5*h + 0.38*numberS);
		textNumber.setTextSize(numberS);
		
		float offsetX = (float) (0.82*w);
		float offsetY = (float) (0.75*h);
		float vuongH = (float) (0.15*h);
		float vuongW = vuongH * 84 / 38;
		rectMIDI = new Rect(
				(int)(offsetX - vuongW), 
				(int)(offsetY - vuongH), 
				(int)(offsetX + vuongW), 
				(int)(offsetY + vuongH));
		
		offsetX = (float) (0.82*w);
		offsetY = (float) (0.75*h);
		vuongH = (float) (0.15*h);
		vuongW = vuongH*84/38;
		rectKTV = new Rect(
				(int)(offsetX - vuongW), 
				(int)(offsetY - vuongH), 
				(int)(offsetX + vuongW), 
				(int)(offsetY + vuongH));
		offsetX = (float) (0.86*w);
		offsetY = (float) (0.74*h);
		vuongH = (float) (0.15*h);
		vuongW = vuongH*48/38;
		rectSinger = new Rect(
				(int)(offsetX - vuongW), 
				(int)(offsetY - vuongH), 
				(int)(offsetX + vuongW), 
				(int)(offsetY + vuongH));
		rectChangPerfect.set(
				(int)(offsetX - vuongW), 
				(int)(offsetY - vuongW), 
				(int)(offsetX + vuongW), 
				(int)(offsetY + vuongW));
		offsetX = (float) (0.9*w);
		offsetY = (float) (0.75*h);
		vuongH = (float) (0.15*h);
		vuongW = vuongH*88/38;
		rectRemix = new Rect(
				(int)(offsetX - vuongW), 
				(int)(offsetY - vuongH), 
				(int)(offsetX + vuongW), 
				(int)(offsetY + vuongH));
		
		offsetX = (float) (0.95*w);
		offsetY = (float) (0.80*h);
		vuongH = (float) (0.1*h);
		vuongW = vuongH*42/22;
		rectUser = new Rect(
				(int)(offsetX - vuongW), 
				(int)(offsetY - vuongH), 
				(int)(offsetX + vuongW), 
				(int)(offsetY + vuongH));
		
		offsetX = (float) (0.95*w);
		offsetY = (float) (0.79*h);
		vuongH = (float) (0.1*h);
		vuongW = vuongH*36/26;
		rectYouTube = new Rect(
				(int)(offsetX - vuongW), 
				(int)(offsetY - vuongH), 
				(int)(offsetX + vuongW), 
				(int)(offsetY + vuongH));
		
		offsetX = (float) (0.95*w);
		offsetY = (float) (0.79*h);
		vuongH = (float) (0.1*h);
		vuongW = vuongH*40/49;
		rectYouTube2 = new Rect(
				(int)(offsetX - vuongW), 
				(int)(offsetY - vuongH), 
				(int)(offsetX + vuongW), 
				(int)(offsetY + vuongH));
		
		offsetX = (float) (0.1*w);
		offsetY = (float) (0.8*h);
		vuongH = (float) (0.12*h);
		vuongW = vuongH*44/26;
		rectOnline = new Rect(
				(int)(offsetX - vuongW), 
				(int)(offsetY - vuongH), 
				(int)(offsetX + vuongW), 
				(int)(offsetY + vuongH));

		ptS = 15 * h / 100;
		ptY = 84 * h / 100;
		
		if(song != null && ((MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && song.isYoutubeSong()) || 
				(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && (MyApplication.flagSmartK_801 || MyApplication.flagSmartK_CB) 
				&& song.isOfflineSong() == false))){
			textY = (int) (0.52*h);
		}	
	}
		
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		setK();
		
		if(position > 0){
			paintMain.setColor(color);
			// paintMain.setColor(Color.GREEN);
			if(position == 4){
				numberX = (int) (0.64*heightLayout);
			}
			if(position == 6){
				numberX = (int) (0.65*heightLayout);
			}
			canvas.drawCircle(cirX, cirY, radius, paintMain);
			canvas.drawText("" + position, numberX, numberY, textNumber);
		}
		if(nameSong.equals("")){
			return;
		}
		
		if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && song.isYoutubeSong()){
			// YOUTUBE SONG
			
			if(isLongClick != true){
				textPaint.setStyle(Style.FILL);
				textPaint.setTextSize(tsingerS);
				textPaint.setColor(Color.parseColor("#b4feff"));
				String str = cutText(tsingerS, 0.31f * widthLayout, nameSinger);
				float w = tsingerX - textPaint.measureText(str);
				if(!MyApplication.flagDance){
					canvas.drawText(str, w, tsingerY, textPaint);
				}
			}
			
			textPaint.setStyle(Style.FILL);
			textPaint.setTextSize(tsongS);
			textPaint.setColor(getResources().getColor(R.color.ktv_color_song_6));
			String str = cutText(tsongS, 0.56f * widthLayout, nameSong);
			canvas.drawText(str, textX, textY, textPaint);
			
			// ---------------MIDI KTV HOAAM-------------------
			if(!MyApplication.flagDance){
				if (ismedia == MEDIA_TYPE.MIDI) {
					if (drawMidi != null) {
						drawMidi.setBounds(rectMIDI);
						drawMidi.draw(canvas);
					}
					if (intABC != 0
							&& (MyApplication.intSvrModel == MyApplication.SONCA || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK)) {
						drawChangPerfect.setBounds(rectChangPerfect);
						drawChangPerfect.draw(canvas);
					}
				} else {
					if (drawKTVVid != null) {
						drawKTVVid.setBounds(rectKTV);
						drawKTVVid.draw(canvas);
					}
				}

				// ---------------isSinger-------------------
				if (isSinger && drawSinger != null) {
					drawSinger.setBounds(rectSinger);
					drawSinger.draw(canvas);
				}

				// ---------------isRemix-------------------
				if (isRemix && drawRemix != null) {
					drawRemix.setBounds(rectRemix);
					drawRemix.draw(canvas);
				}
				
				// ---------------Online-------------------
				drawOnline.setBounds(rectOnline);
				drawOnline.draw(canvas);
				
				// ---------------Download-------------------
				if(MyApplication.youtube_Download_ID == song.getId()){
					if(countDownload == -1){
						startTimerDownload();
					} else {
						switch (countDownload) {
						case 0:
							drawable = getResources().getDrawable(R.drawable.youtube_down);
							break;
						case 1:
							drawable = getResources().getDrawable(R.drawable.youtube_down_1);
							break;
						case 2:
							drawable = getResources().getDrawable(R.drawable.youtube_down_2);
							break;
						case 3:
							drawable = getResources().getDrawable(R.drawable.youtube_down_3);
							break;
						default:
							drawable = getResources().getDrawable(R.drawable.youtube_down);
							break;
						}
						drawable.setBounds(rectYouTube2);
						drawable.draw(canvas);	
					}
					
					textPaint.setStyle(Style.FILL);
					textPaint.setTextSize(ptS);
					textPaint.setARGB(255, 0, 253, 253);
					str = MyApplication.youtube_Download_percent + "%";
					float mWidth = textPaint.measureText(str);
					canvas.drawText(str, rectYouTube2.left - mWidth - 2 * widthLayout / 480, ptY, textPaint);
				} else {
					stopTimerDownload();
					
					if (drawYouTube != null) {
						drawYouTube.setBounds(rectYouTube);
						drawYouTube.draw(canvas);
					}
				}
				
			}					
		
			
		} else if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && (MyApplication.flagSmartK_801 || MyApplication.flagSmartK_CB) 
				&& song.isOfflineSong() == false){

			// ONLINE SONG
			
			if(isLongClick != true){
				textPaint.setStyle(Style.FILL);
				textPaint.setTextSize(tsingerS);
				textPaint.setColor(Color.parseColor("#b4feff"));
				String str = cutText(tsingerS, 0.31f * widthLayout, nameSinger);
				float w = tsingerX - textPaint.measureText(str);
				if(!MyApplication.flagDance){
					canvas.drawText(str, w, tsingerY, textPaint);
				}
			}
			
			textPaint.setStyle(Style.FILL);
			textPaint.setTextSize(tsongS);
			textPaint.setColor(getResources().getColor(R.color.ktv_color_song_6));
			String str = cutText(tsongS, 0.56f * widthLayout, nameSong);
			canvas.drawText(str, textX, textY, textPaint);
			
			// ---------------MIDI KTV HOAAM-------------------
			if(!MyApplication.flagDance){
				if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
					if (ismedia == MEDIA_TYPE.MIDI || ismedia == MEDIA_TYPE.MP3) {
						if (drawMidi != null) {
							drawMidi.setBounds(rectMIDI);
							drawMidi.draw(canvas);
						}
						if (intABC != 0
								&& (MyApplication.intSvrModel == MyApplication.SONCA || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK)) {
							drawChangPerfect.setBounds(rectChangPerfect);
							drawChangPerfect.draw(canvas);
						}
					} else {
						if (ismedia == MEDIA_TYPE.VIDEO) {
							if (drawKTVVid != null) {
								drawKTVVid.setBounds(rectKTV);
								drawKTVVid.draw(canvas);
							}
						} else {
							if (drawKTV != null) {
								drawKTV.setBounds(rectKTV);
								drawKTV.draw(canvas);
							}	
						}
					}
				} else {
					if (ismedia == MEDIA_TYPE.MIDI) {
						if (drawMidi != null) {
							drawMidi.setBounds(rectMIDI);
							drawMidi.draw(canvas);
						}
						if (intABC != 0
								&& (MyApplication.intSvrModel == MyApplication.SONCA || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK)) {
							drawChangPerfect.setBounds(rectChangPerfect);
							drawChangPerfect.draw(canvas);
						}
					} else {
						if (ismedia == MEDIA_TYPE.VIDEO) {
							if (drawKTVVid != null) {
								drawKTVVid.setBounds(rectKTV);
								drawKTVVid.draw(canvas);
							}
						} else {
							if (drawKTV != null) {
								drawKTV.setBounds(rectKTV);
								drawKTV.draw(canvas);
							}	
						}
					}
				}

				// ---------------isSinger-------------------
				if (isSinger && drawSinger != null) {
					drawSinger.setBounds(rectSinger);
					drawSinger.draw(canvas);
				}

				// ---------------isRemix-------------------
				if (isRemix && drawRemix != null) {
					drawRemix.setBounds(rectRemix);
					drawRemix.draw(canvas);
				}
				
				// ---------------Online-------------------
				drawOnline.setBounds(rectOnline);
				drawOnline.draw(canvas);
				
				// ---------------Download-------------------
				if(MyApplication.youtube_Download_ID == song.getId()){
					if(countDownload == -1){
						startTimerDownload();
					} else {
						switch (countDownload) {
						case 0:
							drawable = getResources().getDrawable(R.drawable.youtube_down);
							break;
						case 1:
							drawable = getResources().getDrawable(R.drawable.youtube_down_1);
							break;
						case 2:
							drawable = getResources().getDrawable(R.drawable.youtube_down_2);
							break;
						case 3:
							drawable = getResources().getDrawable(R.drawable.youtube_down_3);
							break;
						default:
							drawable = getResources().getDrawable(R.drawable.youtube_down);
							break;
						}
						drawable.setBounds(rectYouTube2);
						drawable.draw(canvas);	
					}
					
					textPaint.setStyle(Style.FILL);
					textPaint.setTextSize(ptS);
					textPaint.setARGB(255, 0, 253, 253);
					str = MyApplication.youtube_Download_percent + "%";
					float mWidth = textPaint.measureText(str);
					canvas.drawText(str, rectYouTube2.left - mWidth - 2 * widthLayout / 480, ptY, textPaint);
				} else {
					stopTimerDownload();
					
					if (drawYouTube != null) {
						drawYouTube.setBounds(rectYouTube);
						drawYouTube.draw(canvas);
					}
				}
				
			}					
		
			
		
		} else {
			// NORMAL SONG
			
			if(isLongClick != true){
				textPaint.setStyle(Style.FILL);
				textPaint.setTextSize(tsingerS);
				textPaint.setColor(Color.parseColor("#b4feff"));
				String str = cutText(tsingerS, 0.31f * widthLayout, nameSinger);
				float w = tsingerX - textPaint.measureText(str);
				if(!MyApplication.flagDance){
					canvas.drawText(str, w, tsingerY, textPaint);
				}
			}
			
			textPaint.setStyle(Style.FILL);
			textPaint.setTextSize(tsongS);
			textPaint.setARGB(255, 255, 255, 255);
			String str = cutText(tsongS, 0.56f * widthLayout, nameSong);
			canvas.drawText(str, textX, textY, textPaint);
			
			// ---------------MIDI KTV HOAAM-------------------
			if(!MyApplication.flagDance){
				if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK){
					if (ismedia == MEDIA_TYPE.MIDI || ismedia == MEDIA_TYPE.MP3) {
						if (drawMidi != null) {
							drawMidi.setBounds(rectMIDI);
							drawMidi.draw(canvas);
						}
						if (intABC != 0
								&& (MyApplication.intSvrModel == MyApplication.SONCA || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK)) {
							drawChangPerfect.setBounds(rectChangPerfect);
							drawChangPerfect.draw(canvas);
						}
					} else {
						if (ismedia == MEDIA_TYPE.VIDEO) {
							if (drawKTVVid != null) {
								drawKTVVid.setBounds(rectKTV);
								drawKTVVid.draw(canvas);
							}
						} else {
							if (drawKTV != null) {
								drawKTV.setBounds(rectKTV);
								drawKTV.draw(canvas);
							}	
						}
					}
				} else {
					if (ismedia == MEDIA_TYPE.MIDI) {
						if (drawMidi != null) {
							drawMidi.setBounds(rectMIDI);
							drawMidi.draw(canvas);
						}
						if (intABC != 0
								&& (MyApplication.intSvrModel == MyApplication.SONCA || MyApplication.intSvrModel == MyApplication.SONCA_SMARTK)) {
							drawChangPerfect.setBounds(rectChangPerfect);
							drawChangPerfect.draw(canvas);
						}
					} else {
						if (ismedia == MEDIA_TYPE.VIDEO) {
							if (drawKTVVid != null) {
								drawKTVVid.setBounds(rectKTV);
								drawKTVVid.draw(canvas);
							}
						} else {
							if (drawKTV != null) {
								drawKTV.setBounds(rectKTV);
								drawKTV.draw(canvas);
							}	
						}
					}
				}
				

				// ---------------isSinger-------------------
				if (isSinger && drawSinger != null) {
					drawSinger.setBounds(rectSinger);
					drawSinger.draw(canvas);
				}

				// ---------------isRemix-------------------
				if (isRemix && drawRemix != null) {
					drawRemix.setBounds(rectRemix);
					drawRemix.draw(canvas);
				}
				
				// ---------------isUser-------------------
				if (isUser && drawUser != null) {
					drawUser.setBounds(rectUser);
					drawUser.draw(canvas);
				}
				
			}		
			
		}

	}
	
	private int color;
	private Song song;
	private int position = -1;
	private String nameSong = "";
	private String nameSinger = "";
	private boolean isActiveSong = false;
	public void setDataSong(int postition, int color, Song song){
		if(song == null){
			this.color = color;
			this.position = postition;
			this.nameSinger = "";
			this.nameSong = "";
			isActiveSong = false;
			this.isRemix = false;
			this.isSinger = false;
			this.intABC = 0;
			this.ismedia = MEDIA_TYPE.MIDI;
			this.isUser = false;
		}else{
			this.color = color;
			this.position = postition;
			this.nameSinger = song.getSinger().getName();
			if(MyApplication.intSvrModel == MyApplication.SONCA_SMARTK && song.isYoutubeSong()){
				nameSinger = song.getSingerName();
			}
			this.nameSong = song.getSpannable().toString();
			if(application != null){
				ArrayList<Song> list = application.getListActive();
				if(list != null && song != null){
					isActiveSong = list.contains(song);
				}
			}
			
			this.ismedia = song.getMediaType();
			this.isRemix = song.isRemix();
			this.isSinger = song.isMediaSinger();
			this.intABC = song.getTypeABC();
			this.isUser = !song.isSoncaSong();
			
		}
		this.song = song;
		invalidate();
	}
	
	public Song getDataSong(){
		return song;
	}
	
	private MyApplication application;
	public void setApplication(MyApplication application){
		this.application = application;
	}

	public void setBoolLongClick(boolean flag){
		this.isLongClick = flag;
//		requestLayout();
	}
	
	private String cutText(float textSize, float maxLength, String content) {
		if (content == null || content.equals("")) {
			return "";
		}
		Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		paint.setTextSize(textSize);
		float length = paint.measureText(content);
		if (length > maxLength) {
			String[] strings = content.split(" ");
			if (strings.length > 1) {
//				return cutTextWord(textSize, maxLength, strings, paint);
				return cutTextChar(textSize, maxLength, content, paint);
			} else {
				return cutTextChar(textSize, maxLength, content, paint);
			}
		} else {
			return content;
		}
	}
	
	private String cutTextChar(float textSize, float maxLength, String content, Paint paint) {
		float length = 0;
		StringBuffer buffer = new StringBuffer("");
		for (int i = 0; i < content.length(); i++) {
			length = paint.measureText(buffer.toString() + content.charAt(i)
					+ "...");
			if (length < maxLength) {
				buffer.append(content.charAt(i));
			} else {
				break;
			}
		}
		buffer.append("...");
		return buffer.toString();
	}

	private String cutTextWord(float textSize, float maxLength,
			String[] content, Paint paint) {
		float length = 0;
		StringBuffer buffer = new StringBuffer("");
		for (int i = 0; i < content.length; i++) {
			length = paint.measureText(buffer.toString() + " " + content[i]
					+ "...");
			if (length < maxLength) {
				buffer.append(content[i] + " ");
			} else {
				break;
			}
		}
		buffer.append("...");
		return buffer.toString();
	}
	
	private Timer timerDownload = null;
	
	private void stopTimerDownload(){
		if(timerDownload != null){
			timerDownload.cancel();
			timerDownload = null;
		}
		countDownload = -1;
	}
	
	private int countDownload = -1;
	private void startTimerDownload(){
		stopTimerDownload();
		countDownload = 0;
		timerDownload = new Timer();
		timerDownload.schedule(new TimerTask() {
			
			@Override
			public void run() {
				
				countDownload++;
				if(countDownload > 3){
					countDownload = 0;
				}
				
				handlerInvalidate.sendEmptyMessage(0);
			}
		}, 200, 400);
	}
	
	private Handler handlerInvalidate = new Handler(){
		public void handleMessage(Message msg) {
			invalidate();
		};
	};
	
}
