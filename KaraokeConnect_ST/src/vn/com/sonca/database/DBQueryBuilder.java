package vn.com.sonca.database;

import java.util.Locale;

import vn.com.sonca.Touch.touchcontrol.TouchMainActivity;
import vn.com.sonca.zktv.main.KTVMainActivity;
import vn.com.sonca.zzzzz.MyApplication;

public class DBQueryBuilder {
	public static String getColumnString(String table, String column)
	{
		return String.format("[%s].[%s]", table, column); 
	}
	
	public static String getColumnString(String table, String[] columns) 
	{
		StringBuilder sb = new StringBuilder(); 
		int cnt = 0; 
		for (String col : columns) {
			if(col == null || columns.equals("")) continue; 
			sb.append(String.format("[%s].[%s]", table, col)); 
			if(cnt < columns.length - 1)
			{
				sb.append(","); 
			}
			cnt++; 
		}
		return sb.toString(); 
	}

	public static void appendSelect(StringBuilder sb) {
		sb.append("SELECT "); 
	}
	
	public static void appendColumns(StringBuilder sb, String table, String[] columns)
	{
		if(columns == null || columns.length == 0)
		{
			sb.append("*"); 
			return; 
		}
		int cnt = 0; 
		for (String col : columns) {
			if(col == null || columns.equals("")) continue; 
			sb.append("["+table +"].["+ col + "]"); 
			if(cnt < columns.length - 1)
			{
				sb.append(","); 
			}
			cnt++; 
		}
	}
	
	public static void appendColumns(StringBuilder sb, String[] columns)
	{
		if(columns == null || columns.length == 0)
		{
			sb.append("*"); 
			return; 
		}
		int cnt = 0; 
		for (String col : columns) {
			if(col == null || columns.equals("")) continue; 
			sb.append(col); 
			if(cnt < columns.length - 1)
			{
				sb.append(","); 
			}
			cnt++; 
		}
	}
	
	public static void appendTables(StringBuilder sb, String table)
	{
		sb.append(" FROM [" + table + "]"); 
	}
	
	public static void appendInnerJoin(StringBuilder sb, String table, String onParams) {
		sb.append(" INNER JOIN [" + table + "]"); 
		sb.append(" ON " + onParams); 
	}
	
	public static void appendFromFormatMark(StringBuilder sb, boolean mark, String format, Object...args) 
	{
		int start = 0; 
		int end = format.length(); 
		String tmp = ""; 
		int curArgs = 0; 
		while((end = format.indexOf('%', start)) != -1 || curArgs < args.length) {
			tmp = format.substring(start, end); 
			sb.append(tmp); 
			char chr = format.charAt(end+1);
			if(chr == 'K'){
				sb.append(args[curArgs].toString()); 
				curArgs++; 
			}else if(chr == '@') {
				sb.append(String.format("%s", args[curArgs].toString()));
				curArgs++; 
			}
			start = end + 1 + 1; 
		}
		end = format.length(); 
		if(end > start) {
			tmp = format.substring(start, end); 
			sb.append(tmp); 
		}
		sb.append(" "); 
	}
	
public static void appendWhereFromFormat(StringBuilder sb, boolean mark, String format, Object...args)
	{
		sb.append(" WHERE "); 
		appendFromFormatMark(sb, mark, format, args); 
	}

	public static void appendFromFormat(StringBuilder sb, String format, Object...args)
	{
		int start = 0; 
		int end = format.length(); 
		String tmp = ""; 
		int curArgs = 0; 
		boolean isParams = false; 
		
		while((end = format.indexOf('%', start)) != -1 || curArgs < args.length) {
			tmp = format.substring(start, end); 
			sb.append(tmp); 
			char chr = format.charAt(end+1);
			if(chr == 'K'){
				sb.append(args[curArgs].toString()); 
				curArgs++; 
				
				int valueStart = format.indexOf('%', end+1+1); 
				String comp = format.substring(end+1+1, valueStart-1); 
				comp = comp.trim(); 
				if(comp.toLowerCase(Locale.getDefault()).equals("in") || comp.toLowerCase(Locale.getDefault()).equals("not in"))
					isParams = true; 
				
			}else if(chr == '@') {
				if(isParams) {
					sb.append(String.format("%s", args[curArgs].toString()));
					isParams = false; 
				}else {
					sb.append(String.format("'%s'", args[curArgs].toString()));
				}
				
//				sb.append(String.format("'%s'", args[curArgs].toString()));
				curArgs++; 
			}
			start = end + 1 + 1; 
		}
		end = format.length(); 
		if(end > start) {
			tmp = format.substring(start, end); 
			sb.append(tmp); 
		}
		sb.append(" "); 
	}
	
	public static void appendWhereFromFormat(StringBuilder sb, String format, Object...args)
	{
		sb.append(" WHERE "); 
		appendFromFormat(sb, format, args); 
	}

	public static void appendGroupBy(StringBuilder sb, String groupBy) {
		sb.append(" GROUP BY " + groupBy); 
	} 
	
	public static void appendOrderBy(StringBuilder sb, String orderBy) {
		sb.append(" ORDER BY " + orderBy); 
	} 
	
	public static void appendOrderBy(StringBuilder sb, String orderBy, boolean isDesc) {
		sb.append(" ORDER BY " + orderBy); 
		if(isDesc)
		{
			sb.append(" DESC "); 
		}
	} 
	
	public static void appendLimitOffset(StringBuilder sb, int limit, int offset) { 
		if (limit > 0) {
			sb.append(" LIMIT " + limit); 
		}
		if (offset > 0) {
			sb.append(" OFFSET " + offset); 
		}
	}
	
	public static void appendRefreshDevice(StringBuilder sb, boolean isRefresh){
		if (isRefresh) {
			sb.append(" AND ((([ZSONGS].[ZEXTRA] &128) <> 0 and ([ZSONGS].[ZEXTRA] &64)<>0) or ([ZSONGS].[ZEXTRA] &128) == 0)");
		}
	}
	
	public static void appendRefreshDevice_YouTube(StringBuilder sb, boolean isRefresh){
		if (isRefresh) {
			sb.append(" AND ((([ZSONGS_YOUTUBE].[ZEXTRA] &128) <> 0 and ([ZSONGS_YOUTUBE].[ZEXTRA] &64)<>0) or ([ZSONGS_YOUTUBE].[ZEXTRA] &128) == 0)");
		}
	}
	
	public static void appendRefreshDevice_New(StringBuilder sb, boolean isRefresh){
		if (isRefresh) {
			sb.append(" AND ((([ZSONGS_NEW].[ZEXTRA] &128) <> 0 and ([ZSONGS_NEW].[ZEXTRA] &64)<>0) or ([ZSONGS_NEW].[ZEXTRA] &128) == 0)");
		}
	}

	public static void appendKM1SelectList(StringBuilder sb, int listType){

		if(MyApplication.intWifiRemote == MyApplication.SONCA_KARTROL){
			switch (listType) {
			case MyApplication.SelectList_NONE:
			case MyApplication.SelectList_ALL:	
				break;
			case MyApplication.SelectList_SONCA:
				sb.append(" AND ((([ZSONGS].[ZEXTRA] &128) <> 0 and ([ZSONGS].[ZEXTRA] &64)<>0) or ([ZSONGS].[ZEXTRA] &128) == 0)");
				break;
			case MyApplication.SelectList_USER:
				sb.append(" AND (([ZSONGS].[ZEXTRA] &128) <> 0 and ([ZSONGS].[ZEXTRA] &64)==0) ");
				break;
			default:
				break;
			}
			
		}
			
	}
	
	public static void appendKM1SelectList_New(StringBuilder sb, int listType){

		if(MyApplication.intWifiRemote == MyApplication.SONCA_KARTROL){
			switch (listType) {
			case MyApplication.SelectList_NONE:
			case MyApplication.SelectList_ALL:	
				break;
			case MyApplication.SelectList_SONCA:
				sb.append(" AND ((([ZSONGS_NEW].[ZEXTRA] &128) <> 0 and ([ZSONGS_NEW].[ZEXTRA] &64)<>0) or ([ZSONGS_NEW].[ZEXTRA] &128) == 0)");
				break;
			case MyApplication.SelectList_USER:
				sb.append(" AND (([ZSONGS_NEW].[ZEXTRA] &128) <> 0 and ([ZSONGS_NEW].[ZEXTRA] &64)==0) ");
				break;
			default:
				break;
			}
			
		}
			
	}
	
	public static void appendFilterVol(StringBuilder sb){
		int intTocHdd = 0;
		int intTocDisc = 0;
		try {			
			if(MyApplication.intColorScreen == MyApplication.SCREEN_KTVUI){
				intTocHdd = KTVMainActivity.intTocHDDVol;
				intTocDisc = KTVMainActivity.intTocDISCVol;	
			} else {
				intTocHdd = TouchMainActivity.intTocHDDVol;
				intTocDisc = TouchMainActivity.intTocDISCVol;				
			}		
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		String sql = "";
		sql += " AND (";
		sql += "(("+ intTocHdd +" between [ZSONGMODEL].[BEGIN] AND [ZSONGMODEL].[END]) AND (";
		sql += "(";
		sql += "[ZSONGMODEL].[REMOVE] is not null and";
		sql += "[ZSONGMODEL].[REMOVE] not like '%"+ intTocHdd +"%'";
		sql += ")";
		sql += "OR";
		sql += "(";
		sql += "[ZSONGMODEL].[REMOVE] is null";
		sql += ")";
		sql += "))";
		sql += "OR";
		sql += "(("+ intTocDisc +" between [ZSONGMODEL].[BEGIN] AND [ZSONGMODEL].[END]) AND (";
		sql += "(";
		sql += "[ZSONGMODEL].[REMOVE] is not null and";
		sql += "[ZSONGMODEL].[REMOVE] not like '%"+ intTocDisc +"%'";
		sql += ")";
		sql += "OR";
		sql += "(";
		sql += "[ZSONGMODEL].[REMOVE] is null";
		sql += ")";
		sql += "))";
		sql += " ) ";
		
		sb.append(sql);
	}
	
}
