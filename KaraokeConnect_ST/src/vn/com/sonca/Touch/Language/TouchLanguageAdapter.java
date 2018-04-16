package vn.com.sonca.Touch.Language;

import java.util.List;
import java.util.Locale;

import vn.com.sonca.MyLog.MyLog;
import vn.com.sonca.Touch.CustomView.LanguageSongTypeFirstItemView;
import vn.com.sonca.Touch.CustomView.LanguageSongTypeRemainItemView;
import vn.com.sonca.Touch.CustomView.TouchSongTypeFirstItemView;
import vn.com.sonca.Touch.CustomView.TouchSongTypeRemainItemView;
import vn.com.sonca.params.Language;
import vn.com.hanhphuc.karremote.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class TouchLanguageAdapter extends BaseAdapter {

	private Context context;
	private List<Language> songTypeList;
	
	public TouchLanguageAdapter(Context context, List<Language> singerList)
	{
		this.context = context;
		this.songTypeList = singerList;
	}

	@Override
	public int getCount() {
		return songTypeList.size();
	}

	@Override
	public Object getItem(int position) {
		return songTypeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null)
		{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.language_listview_item, null);
			
			LanguageSongTypeFirstItemView firstItem = (LanguageSongTypeFirstItemView) convertView.findViewById(R.id.songtype_listview_firstItem);
			LanguageSongTypeRemainItemView remainItem = (LanguageSongTypeRemainItemView) convertView.findViewById(R.id.songtype_listview_remainItem);
			viewHolder = new ViewHolder(firstItem, remainItem);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		String nameLang = songTypeList.get(position).getName();
		viewHolder.getFirstItem().setLanguage(songTypeList.get(position));
		viewHolder.getRemainItem().setLanguage(songTypeList.get(position));
//		if(Locale.getDefault().getDisplayLanguage().equalsIgnoreCase("English")){
			nameLang = changeEnglishName(songTypeList.get(position).getID(), nameLang);
//		}
		viewHolder.getFirstItem().setText(nameLang);
		viewHolder.getRemainItem().setText(nameLang);
		if (position != 0)
		{
			viewHolder.getFirstItem().setVisibility(View.GONE);
			viewHolder.getRemainItem().setVisibility(View.VISIBLE);
		}
		else
		{
			viewHolder.getFirstItem().setVisibility(View.VISIBLE);
			viewHolder.getRemainItem().setVisibility(View.GONE);
		}
		return convertView;
	}
	
	private String changeEnglishName(int intLanguage, String defaultStr) {
		String resultStr = defaultStr;
		
		switch (intLanguage) {
		case 0:
			resultStr = context.getResources().getString(R.string.change_lang_1);
			break;
		case 1:
			resultStr = context.getResources().getString(R.string.change_lang_2);
			break;
		case 2:
			resultStr = context.getResources().getString(R.string.change_lang_7);
			break;
		case 3:
			resultStr = context.getResources().getString(R.string.change_lang_3);
			break;
		case 4:
			resultStr = context.getResources().getString(R.string.change_lang_4);
			break;
		case 5:
			resultStr = context.getResources().getString(R.string.change_lang_5);
			break;
		case 6:
			resultStr = context.getResources().getString(R.string.change_lang_6);
			break;
		default:
			resultStr = context.getResources().getString(R.string.change_lang_0);
			break;
		}		
		return resultStr;
	}
	
	private String changeEnglishName(String defaultStr) {
		String resultStr = defaultStr;
		
		String name = context.getResources().getString(R.string.change_lang_0);
		if(name.equalsIgnoreCase(defaultStr)){
			return "Other Languages";
		}
		
		name = context.getResources().getString(R.string.change_lang_1);
		if(name.equalsIgnoreCase(defaultStr)){
			return "Vietnamese";
		}
		
		name = context.getResources().getString(R.string.change_lang_2);
		if(name.equalsIgnoreCase(defaultStr)){
			return "English";
		}
		
		name = context.getResources().getString(R.string.change_lang_3);
		if(name.equalsIgnoreCase(defaultStr)){
			return "Chinese";
		}
		
		name = context.getResources().getString(R.string.change_lang_4);
		if(defaultStr.toUpperCase().contains(name.toUpperCase())){
			return "Philippines";
		}
		
		name = context.getResources().getString(R.string.change_lang_5);
		if(name.equalsIgnoreCase(defaultStr)){
			return "Korean";
		}
		
		name = context.getResources().getString(R.string.change_lang_6);
		if(name.equalsIgnoreCase(defaultStr)){
			return "Japanese";
		}
		return resultStr;
	}
	
	private class ViewHolder
	{
		private LanguageSongTypeFirstItemView firstItem;
		private LanguageSongTypeRemainItemView remainItem;
		
		public ViewHolder(LanguageSongTypeFirstItemView firstItem, LanguageSongTypeRemainItemView remainItem)
		{
			setFirstItem(firstItem);
			setRemainItem(remainItem);
		}
		
		public LanguageSongTypeFirstItemView getFirstItem() {
			return firstItem;
		}

		public void setFirstItem(LanguageSongTypeFirstItemView firstItem) {
			this.firstItem = firstItem;
		}

		public LanguageSongTypeRemainItemView getRemainItem() {
			return remainItem;
		}

		public void setRemainItem(LanguageSongTypeRemainItemView remainItem) {
			this.remainItem = remainItem;
		}
	}
}