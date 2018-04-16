package vn.com.sonca.params;

public class TOCVersion {
	public int discVersion;
	public int discRevision;
	public int scdiscVersion;
	public int scdiscRevision;
	public int xuserVersion;
	public int xuserRevision;
	public int userVersion;
	public int userRevision; 
	
	public TOCVersion (int[] params) {
		if(params == null) return; 
		
		discVersion = params[0]; 
		discRevision = params[1]; 
		scdiscVersion = params[2]; 
		scdiscRevision = params[3]; 
		xuserVersion = params[4]; 
		xuserRevision = params[5]; 
		userVersion = params[6]; 
		userRevision = params[7]; 
	}
	
	public int getVolDisc()
	{
		return discVersion*100 + discRevision; 
	}
	
	public int getVolSCDisc()
	{
		return scdiscVersion*100 + scdiscRevision; 
	}
	
	public int getVolXUser()
	{
		return xuserVersion*100 + xuserRevision; 
	}
	
	public int getVolUser()
	{
		return userVersion*100 + userRevision; 
	}
}
