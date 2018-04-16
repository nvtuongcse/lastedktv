package vn.com.sonca.Lyric;

import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.ColorLyric.FragmentReviewKaraoke.OnReviewKaraokeFragmentListener;
import vn.com.sonca.Lyric.DialogLyricView.OnDialogLyricView;
import vn.com.sonca.Lyric.LyricBack.OnBackLyric;
import vn.com.sonca.Lyric.ZoomBasicView.OnZoomViewListener;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.Favourite.FavouriteStore;
import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.database.DBInterface;
import vn.com.sonca.params.Song;
import vn.com.sonca.zktv.main.KTVMainActivity;
import vn.com.sonca.zzzzz.MyApplication;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewManager;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class ToastBox {
	
	private String TAB = "ToastBox";
	
	private Context context;
	private Window window;
	private LayoutInflater layoutInflater;	
	private WindowManager.LayoutParams params_relative;
	public ToastBox(Context context, Window window) {
		params_relative = new WindowManager.LayoutParams();
		params_relative.height = WindowManager.LayoutParams.MATCH_PARENT;
		params_relative.width = WindowManager.LayoutParams.MATCH_PARENT;
		params_relative.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		params_relative.type = WindowManager.LayoutParams.TYPE_TOAST;
		params_relative.format = PixelFormat.TRANSLUCENT;
		this.context = context;
		this.window = window;
	}
	
	public static DialogLyricView dialogLyricView;
	
	private OnToastBoxListener listener;
	public interface OnToastBoxListener{	
		public void OnFavourite();
		public void OnShowReviewKaraoke(Song song);
		public void OnPopupPlayYouTube(Song song);
		public void OnPopupDownYouTube(Song song);
	}
	
	public void setOnToastBoxListener(OnToastBoxListener listener){
		this.listener = listener;
	}
	
	private View viewToast = null;
	private Animation animaFaceIn;
	private Animation animaFaceOut;
	private boolean enableClick = false;
	private TouchMainActivity mainActivity;
	private KTVMainActivity ktvMainActivity;
	
	public void setDataLyric(String data){
//		String[] lyricFull = data.split("\n\n");
		if (data == null || data.equals("")) {
			textTitleLyric.setText(context.getString(R.string.lyric_6));
			return;
		}
		//--------------------//
		int start = "Title: ".length();
		int end = data.indexOf("Musician:");
		if (end == -1 && textTitleLyric != null) {
			textTitleLyric.setText(context.getString(R.string.lyric_6));
			return;
		}
		String textdata = data.substring(start, end);
		if (textName != null) {
			textName.setText(textdata);
		}
		//--------------------//
		start = end + "Musician:".length();
		end = data.indexOf("Lyrician:");
		if (end == -1 && textTitleLyric != null) {
			textTitleLyric.setText(context.getString(R.string.lyric_6));
			return;
		}
		textdata = data.substring(start, end);
		if (textMusician != null) {
			textMusician.setText(textdata);
		}
		//--------------------//
		start = end + "Lyrician:".length();
		end = data.indexOf("Singer:");
		if (end == -1 && textTitleLyric != null) {
			textTitleLyric.setText(context.getString(R.string.lyric_6));
			return;
		}
		textdata = data.substring(start, end);
		if (textLyric != null) {
			textLyric.setText(textdata);
		}
		//--------------------//
		String da = data.substring(end);
		start = da.indexOf("\n");
		if (start == -1 && textTitleLyric != null) {
			textTitleLyric.setText(context.getString(R.string.lyric_6));
			return;
		}
		textdata = da.substring(start);
		textTitleLyric.setText(context.getString(R.string.lyric_1));
		if (textLyricFull != null) {
			textLyricFull.setText(textdata.replaceAll("\r", "\n"));
		}
	}
	
	private Song song;
	public void setDataSong(Song song){
		this.song = song;
	}

	private TextView textName;
	private TextView textLyric;
	private TextView textMusician;
	private TextView textLyricFull;
	private TextView textTitleLyric;
	private ZoomInView zoomInView;
	private ZoomOutView zoomOutView;
	private LinearLayout layoutLyric;
	private LinearLayout LayoutDialog;
	public void showToast(){
		
		/*
		TypedValue typedValue = new TypedValue(); 
	    context.getTheme().resolveAttribute(android.R.attr.textAppearance, typedValue, true);
	    int[] textSizeAttr = new int[] { android.R.attr.textSize };
	    TypedArray a = context.obtainStyledAttributes(typedValue.data, textSizeAttr);
	    int textSize = a.getDimensionPixelSize(0, -1);
	    Log.e("Metrics", "text size in dp = " +  String.valueOf(textSize));
	    a.recycle();
		*/
		
		// MotionEvent.ACTION_SCROLL
		
		
		if (window == null || viewToast != null) {
			return;
		}
		if(viewToast == null){
			layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewToast = layoutInflater.inflate(R.layout.touch_dialog_lyric,null);
			LayoutDialog = (LinearLayout)viewToast.findViewById(R.id.LayoutDialog);
			LayoutDialog.setOnClickListener(new OnClickListener() {
				@Override public void onClick(View view) {
					if (enableClick && viewToast != null) {
						viewToast.startAnimation(animaFaceOut);
					}
				}
			});
			layoutLyric = (LinearLayout)viewToast.findViewById(R.id.layoutLyric);
			layoutLyric.setOnClickListener(new OnClickListener() {
				@Override public void onClick(View view) {}
			});
			ImageSinger imageSinger = (ImageSinger)viewToast.findViewById(R.id.imageSinger);
			if(song != null){
				imageSinger.setData(song);
			}
			textName = (TextView)viewToast.findViewById(R.id.textName);
			textMusician = (TextView)viewToast.findViewById(R.id.textSinger);
			textLyric = (TextView)viewToast.findViewById(R.id.nameLyric);
			textLyricFull = (TextView)viewToast.findViewById(R.id.textLyric);
			textTitleLyric = (TextView)viewToast.findViewById(R.id.textTitleLyric);
/*
			int zoom = getZoom();
			textLyricFull.setTextSize(zoom);
			textTitleLyric.setTextSize(zoom);
*/
			if(song != null){
				dialogLyricView = 
						(DialogLyricView)viewToast.findViewById(R.id.dialogLyricView);
				dialogLyricView.setContent(song);
				dialogLyricView.setOnDialogLyricView(new OnDialogLyricView() {
					@Override public void OnShowLyric(Song song) {
						LayoutDialog.setVisibility(View.INVISIBLE);
						layoutLyric.setVisibility(View.VISIBLE);
					}
					@Override
					public void OnFavourite(boolean fav, Song so) {
						if(song != null){
							song.setFavourite(fav);
							FavouriteStore favStore = FavouriteStore.getInstance(context);
							favStore.setFavSongIntoStore(fav, 
									String.valueOf(song.getId()), song.getTypeABC());
							DBInterface.DBSetFavouriteSong(String.valueOf(song.getId()), 
									String.valueOf(song.getTypeABC()), fav, context);			
//							if(listener != null){
//								listener.OnFavourite();
//							}
						}
					}
					@Override
					public void OnShowReviewKaraoke(Song song) {
						MyLog.d("ToastBox", "==OnShowReviewKaraoke=");
						LayoutDialog.setVisibility(View.INVISIBLE);
						if(listener != null){
							listener.OnShowReviewKaraoke(song);
						}
					}
					
					@Override
					public void OnPopupPlayYouTube(Song song) {
						LayoutDialog.setVisibility(View.INVISIBLE);
						if(listener != null){
							listener.OnPopupPlayYouTube(song);
						}
					}
					
					@Override
					public void OnPopupDownYouTube(Song song) {
						LayoutDialog.setVisibility(View.INVISIBLE);
						if(listener != null){
							listener.OnPopupDownYouTube(song);
						}
					}
					
				});
			}
			LyricBack lyricBack = (LyricBack)viewToast.findViewById(R.id.lyricBack);
			lyricBack.setOnBackLyric(new OnBackLyric() {
				@Override public void OnBack() {
					if (enableClick && viewToast != null) {
						viewToast.startAnimation(animaFaceOut);
					}
				}
			});
			zoomInView = (ZoomInView)viewToast.findViewById(R.id.ZoomInView);
			zoomInView.setOnZoomViewListener(new OnZoomViewListener() {
				@Override public void OnZoom() {
					if(textLyricFull != null){
						float x = (float) (pixelsToSp(context, 
								textLyricFull.getTextSize()) + 
								0.25*MyApplication.TextSize);
						MyLog.e(TAB, "font size : " + x);
						showPercentLyric(context, x);
						if(x <= 1.5*MyApplication.TextSize){
							// tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
							textLyricFull.setTextSize(x); 
							textTitleLyric.setTextSize(x); 
							zoomOutView.setActiveView(true);
							saveZoom((int)x);
						}
						if(x >= 1.5*MyApplication.TextSize){
							zoomInView.setActiveView(false);
						}						
					}
				}
			});
			zoomOutView = (ZoomOutView)viewToast.findViewById(R.id.ZoomOutView);
			zoomOutView.setOnZoomViewListener(new OnZoomViewListener() {
				@Override public void OnZoom() {
					if(textLyricFull != null){
						float x = (float) (pixelsToSp(context, 
								textLyricFull.getTextSize()) - 
								0.25*MyApplication.TextSize);
						MyLog.e(TAB, "font size : " + x);
						showPercentLyric(context, x);
						if(x >= 0.5*MyApplication.TextSize){
							textLyricFull.setTextSize(x); 
							textTitleLyric.setTextSize(x);
							zoomInView.setActiveView(true);
							saveZoom((int)x);
						}
						if(x <= 0.5*MyApplication.TextSize){
							zoomOutView.setActiveView(false);
						}
					}
				}
			});
			window.addContentView(viewToast, params_relative);
			animaFaceIn = AnimationUtils.loadAnimation(context,R.anim.fade_in); 
			animaFaceOut = AnimationUtils.loadAnimation(context,R.anim.fade_out); 
			animaFaceIn.setAnimationListener(new AnimationListener() {
				@Override public void onAnimationStart(Animation animation) {}
				@Override public void onAnimationRepeat(Animation animation) {}
				@Override public void onAnimationEnd(Animation animation) {
					enableClick = true;
				}
			});
			animaFaceOut.setAnimationListener(new AnimationListener() {
				@Override public void onAnimationStart(Animation animation) {}
				@Override public void onAnimationRepeat(Animation animation) {}
				@Override public void onAnimationEnd(Animation animation) {
					hideToast();
				}
			});
			
			View viewLine = (View)viewToast.findViewById(R.id.viewLine);
			TextView textS = (TextView)viewToast.findViewById(R.id.textS);
			TextView textL = (TextView)viewToast.findViewById(R.id.textL);
			if (MyApplication.intColorScreen == MyApplication.SCREEN_BLUE || MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI) {
				layoutLyric.setBackgroundResource(R.drawable.mainbg);
				viewLine.setBackgroundResource(R.drawable.touch_shape_line_ver);
				textName.setTextColor(context.getResources().getColor(R.color.lyric_text_0));
				textS.setTextColor(context.getResources().getColor(R.color.lyric_text_1));
				textL.setTextColor(context.getResources().getColor(R.color.lyric_text_1));
				textMusician.setTextColor(context.getResources().getColor(R.color.lyric_text_2));
				textLyric.setTextColor(context.getResources().getColor(R.color.lyric_text_2));
				textTitleLyric.setTextColor(context.getResources().getColor(R.color.lyric_text_2));
				textLyricFull.setTextColor(context.getResources().getColor(R.color.lyric_text_3));
			} else if (MyApplication.intColorScreen == MyApplication.SCREEN_LIGHT) {
				viewLine.setBackgroundResource(R.drawable.zlight_shape_line_ver);
				layoutLyric.setBackgroundColor(Color.parseColor("#C1FFE8"));
				textName.setTextColor(Color.parseColor("#005249"));
				textS.setTextColor(Color.parseColor("#005249"));
				textL.setTextColor(Color.parseColor("#005249"));
				textMusician.setTextColor(Color.parseColor("#005249"));
				textLyric.setTextColor(Color.parseColor("#005249"));
				textTitleLyric.setTextColor(Color.parseColor("#005249"));
				textLyricFull.setTextColor(Color.parseColor("#005249"));
			}
			viewToast.startAnimation(animaFaceIn);
		}
	}
	
	public void hideToastBox(){
		if (enableClick && viewToast != null && window != null) {
			viewToast.startAnimation(animaFaceOut);
			dialogLyricView = null;
		}
	}
	
	private void hideToast(){
		if(viewToast != null && window != null){
			((ViewManager)viewToast.getParent()).removeView(viewToast);
			viewToast = null;
			dialogLyricView = null;
		}
	}
	
	public void saveZoom(int zoom){
		SharedPreferences pre = context.getSharedPreferences("lyric_zoom", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = pre.edit();
		editor.putInt("zoom", zoom);
		editor.commit();
	}
	
	public int getZoom(){
		SharedPreferences pre = context.getSharedPreferences("lyric_zoom", Context.MODE_PRIVATE);
		int data = pre.getInt("zoom", 24);
		return data;		
	}
	
	public static float pixelsToSp(Context context, float px) {
	    float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
	    return px/scaledDensity;
	}
	
	
	Toast toastDisConnect;
	private void showPercentLyric(Context context, float pstext) {
		if(layoutInflater == null){
			return;
		}
		if(toastDisConnect != null){
			toastDisConnect.cancel();
			toastDisConnect = null;
		}
		View toastRoot = layoutInflater.inflate(R.layout.touch_toast_percent_lyric, null);
		toastDisConnect = new Toast(context);
		toastDisConnect.setView(toastRoot);
		TextView textView = (TextView)toastRoot.findViewById(R.id.textPercentLyric);
		int percent = (int) (pstext/MyApplication.TextSize*100);
		textView.setText(percent + " %");
		toastDisConnect.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,0, 0);
		toastDisConnect.setDuration(Toast.LENGTH_SHORT);
		toastDisConnect.show();
	}
	

}
