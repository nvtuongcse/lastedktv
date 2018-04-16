package vn.com.sonca.mac;
/*
 *
 * DES exception
 * 
*/
public class AESException extends Exception {
    

    public AESException(Exception e) {
		super(e);
	}

	public AESException(String message) {
		super(message);
	}
}
