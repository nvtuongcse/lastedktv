package vn.com.sonca.zktv.FragImage;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import vn.com.hanhphuc.karremote.R;
import vn.com.sonca.LoadSong.BaseLoadSong.OnLoadListener;
import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.params.ServerStatus;
import vn.com.sonca.zktv.FragData.ButPage;
import vn.com.sonca.zktv.FragData.MyTextView;
import vn.com.sonca.zktv.FragImage.MyImageView.OnImageViewListener;
import vn.com.sonca.zktv.main.FragBase;
import vn.com.sonca.zktv.main.KTVMainActivity.OnKTVMainListener;

public abstract class FragImage extends FragBase implements 
	OnLoadListener, OnImageViewListener {
	
	private String TAB = "FragImage";
	
	private MyImageView imageView1;
	private MyImageView imageView2;
	private MyImageView imageView3;
	private MyImageView imageView4;
	private MyImageView imageView5;
	private MyImageView imageView6;
	private MyImageView imageView7;
	private MyImageView imageView8;
	
	private MyTextView textPageTotal;
	private MyTextView textPageCurrent;
	private ButPage butLeft, butRight;
	
	protected abstract List<Object> loadDatabase(String search);
	protected abstract String getName(Object object);
	protected abstract int getCover(Object object);
	protected abstract int getID(Object object);
	protected abstract String getNameFrag();
	
	private OnFragImageListener listener;
	public interface OnFragImageListener {
		public void OnClickImage(int id, String nameFrag);
	}
	public void setOnFragImageListener(OnFragImageListener listener){
		this.listener = listener;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		listener = (OnFragImageListener)activity;
	}
		
	private Context context;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.ktv_fragment_image, container, false);
		
		context = getActivity().getApplicationContext();
		
		imageView1 = (MyImageView)view.findViewById(R.id.imageView1);
		imageView2 = (MyImageView)view.findViewById(R.id.imageView2);
		imageView3 = (MyImageView)view.findViewById(R.id.imageView3);
		imageView4 = (MyImageView)view.findViewById(R.id.imageView4);
		imageView5 = (MyImageView)view.findViewById(R.id.imageView5);
		imageView6 = (MyImageView)view.findViewById(R.id.imageView6);
		imageView7 = (MyImageView)view.findViewById(R.id.imageView7);
		imageView8 = (MyImageView)view.findViewById(R.id.imageView8);
		
		imageView1.setOnImageViewListener(this);
		imageView2.setOnImageViewListener(this);
		imageView3.setOnImageViewListener(this);
		imageView4.setOnImageViewListener(this);
		imageView5.setOnImageViewListener(this);
		imageView6.setOnImageViewListener(this);
		imageView7.setOnImageViewListener(this);
		imageView8.setOnImageViewListener(this);
		
		butLeft = (ButPage)view.findViewById(R.id.butLeft);
		butRight = (ButPage)view.findViewById(R.id.butRight);
		
		butLeft.setImageView(context.getResources().getDrawable(R.drawable.ktv_icon_backpage_inactive), false);
		butRight.setImageView(context.getResources().getDrawable(R.drawable.ktv_icon_nextpage), true);
		
		textPageTotal = (MyTextView)view.findViewById(R.id.textPageTotal);
		textPageCurrent = (MyTextView)view.findViewById(R.id.textPageCurrent);
		
		butLeft.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View arg0) {
				if(flagBlockFlipPage){
					return;
				}
				intNumberPage--;
				startFlipPage(true); // true for left page
			}
		});
		butRight.setOnClickListener(new OnClickListener() {
			@Override public void onClick(View arg0) {
				if(flagBlockFlipPage){
					return;
				}
				intNumberPage++;
				startFlipPage(false);				
			}
		});
		
		intNumberPage = 0;
		LoadDatabase("");
		LoadPageList(intNumberPage);
		
		return view;
	}
	
	private boolean flagBlockFlipPage = false;
	private Timer timerFlipPage = null;
	private void stopFlipPage(){
		if(timerFlipPage != null){
			timerFlipPage.cancel();
			timerFlipPage = null;
		}
	}
	
	private void startFlipPage(boolean flagLeft){
		stopFlipPage();
		flagBlockFlipPage = true;

		
		boolean re = LoadPageList(intNumberPage);
		if(re == false){
			if(flagLeft){
				intNumberPage = 0;	
			} else {
				intNumberPage = intTotalPage - 1;
			}			
		}
		
		timerFlipPage = new Timer();
		timerFlipPage.schedule(new TimerTask() {
			
			@Override
			public void run() {
				flagBlockFlipPage = false;
			}
		}, 500);
	}
	
	private void showItem(int pos, int color, 
		MyImageView imageView, List<Object> arrayList){
		if (imageView != null) {
			if (arrayList.size() >= pos) {
				Object singer = arrayList.get(pos - 1);
				String name = getName(singer);
				int cover = getCover(singer);
				imageView.setData(pos, getResources().
						getColor(color),
						name, searchFrag, cover);
				imageView.setObject(singer);
			}else{
				imageView.setData(pos, getResources().
						getColor(color),
						"", "", 0);
				imageView.setObject(null);
			}
		}
	}

	private void showListImage(List<Object> arrayList){
		if(arrayList == null){
			return;
		}
		showItem(1, R.color.ktv_color_image_1, imageView1, arrayList);
		showItem(2, R.color.ktv_color_image_2, imageView2, arrayList);
		showItem(3, R.color.ktv_color_image_3, imageView3, arrayList);
		showItem(4, R.color.ktv_color_image_4, imageView4, arrayList);
		showItem(5, R.color.ktv_color_image_5, imageView5, arrayList);
		showItem(6, R.color.ktv_color_image_6, imageView6, arrayList);
		showItem(7, R.color.ktv_color_image_7, imageView7, arrayList);
		showItem(8, R.color.ktv_color_image_8, imageView8, arrayList);
	}
	
	@Override
	public void OnClearList() {}

	@Override
	public void OnStartLoad(int idThread) {}

	@Override
	public void OnLoading() {}

	@Override
	public void OnStopLoad(String search) {}

	@Override
	public void OnFinishLoad() {}

	@Override
	public void OnLoading_Full() {}

	@Override
	public void OnLoading_Next() {}

	@Override
	public void OnLoading_Fin() {}

	private String searchFrag = "";
	private int intNumberPage = 0;
	private int intTotalPage = 0;
	
	@Override
	protected void OnLoadSearch(String textSearch) {
		searchFrag = textSearch;
		startLoadData(textSearch);
	}
	
	private Timer timerLoadData = null;
	private void stopLoadData(){
		if(timerLoadData != null){
			timerLoadData.cancel();
			timerLoadData = null;
		}
	}
	
	private void startLoadData(final String search){
		stopLoadData();
		timerLoadData = new Timer();
		timerLoadData.schedule(new TimerTask() {
			@Override public void run() {
				intNumberPage = 0;
				LoadDatabase(search);
				handlerLoadData.sendEmptyMessage(0);
			}
		}, 200);
	}
	
	private Handler handlerLoadData = new Handler(){
		public void handleMessage(android.os.Message msg) {
			LoadPageList(intNumberPage);
		};
	};
	
	private List<Object> singerList;
	private void LoadDatabase(String search) {
		singerList = loadDatabase(search);
		if(singerList != null){
			int i = (singerList.size()%8 != 0) ? 1 : 0;
			intTotalPage = singerList.size()/8 + i;
			MyLog.e(TAB, "LoadDatabase : " + search + " : " + singerList.size());
			MyLog.e(TAB, "LoadDatabase : " + search + " : " + intTotalPage);
		}
	}
	
	private boolean LoadPageList(int page){
		try{
			List<Object> list = null;
			if((page+1) == intTotalPage){
				int p = singerList.size()%8;
				MyLog.e(TAB, "LoadPageList1 : " + page + " : " + (page*8) + " : " + (page*8 + p));
				if(p == 0){
					list = singerList.subList(page*8, page*8 + 8);	
				} else {
					list = singerList.subList(page*8, page*8 + p);		
				}
			}else if(page < intTotalPage){
				MyLog.e(TAB, "LoadPageList2 : " + page + " : " + (page*8) + " : " + (page*8 + 8));
				list = singerList.subList(page*8, page*8 + 8);
			}else{
				MyLog.e(TAB, "LoadPageList : SPECIAL 2");
				intTotalPage = 0;
				list = new ArrayList<Object>();
			}
			showListImage(list);
			if(intTotalPage == 0){
				textPageCurrent.setDataNumber("Trang:", " " + 0);
			}else{
				textPageCurrent.setDataNumber("Trang:", " " + (intNumberPage+1));
			}
			textPageTotal.setDataNumber(context.getResources().getString(R.string.ktv_search_1), " " + intTotalPage + " trang");
			
			processButPage();
			
			return true;
		}catch(IndexOutOfBoundsException ex){
			MyLog.e(TAB, "ERROR : IndexOutOfBoundsException");
			return false;
		}
	}
	
	private void processButPage(){
		if(intTotalPage <= 0){
			butLeft.setImageView(context.getResources().getDrawable(R.drawable.ktv_icon_backpage_inactive), false);
			butRight.setImageView(context.getResources().getDrawable(R.drawable.ktv_icon_nextpage_inactive), false);
		} else if (intTotalPage == 1){
			butLeft.setImageView(context.getResources().getDrawable(R.drawable.ktv_icon_backpage_inactive), false);
			butRight.setImageView(context.getResources().getDrawable(R.drawable.ktv_icon_nextpage_inactive), false);
		} else {
			if(intNumberPage <= 0){
				butLeft.setImageView(context.getResources().getDrawable(R.drawable.ktv_icon_backpage_inactive), false);
				butRight.setImageView(context.getResources().getDrawable(R.drawable.ktv_icon_nextpage), true);	
			} else if (intNumberPage == (intTotalPage - 1)){
				butLeft.setImageView(context.getResources().getDrawable(R.drawable.ktv_icon_backpage), true);
				butRight.setImageView(context.getResources().getDrawable(R.drawable.ktv_icon_nextpage_inactive), false);
			} else {
				butLeft.setImageView(context.getResources().getDrawable(R.drawable.ktv_icon_backpage), true);
				butRight.setImageView(context.getResources().getDrawable(R.drawable.ktv_icon_nextpage), true);
			}
		}		
	}
	
	@Override
	public void OnClick(int position, Object object) {
		if(listener != null){
			int id = getID(object);
			listener.OnClickImage(id, getNameFrag());
		}
	}
	
	@Override
	public void OnLayoutFrag(ServerStatus status) {
		// TODO Auto-generated method stub
		
	}
	
}
