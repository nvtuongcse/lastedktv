package vn.com.sonca.mac;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.spec.PBEKeySpec;

/**
 * DES encrypt and decrypt functions
  */
public class AESUtils{

    private static final String ENCRYPTION_ALGORITHM = "AES";
    private static final int KEY_SIZE = 128;
    private static final String _charset="UTF-8";
    private static final String secretKey = "@thisisthesecretkeyforkey";
    private static final String secretKeyData = "@thisisthesecretkeyfordata";
    
    
        /**
     * Generate the secrey key and return as a hex string
     *
     * @return Secret key
     * @throws AESException AES exception
     */
    public static String generateSecretKey() throws AESException {
        try {
            // Get the KeyGenerator
            KeyGenerator kgen = KeyGenerator.getInstance(ENCRYPTION_ALGORITHM);
            kgen.init(KEY_SIZE); // 192 and 256 bits may not be available
            // Generate the secret key specs.
            SecretKey skey = kgen.generateKey();
            byte[] raw = skey.getEncoded();
            String key = Base64.encodeToString(raw, true);
            return key;
        } catch (NoSuchAlgorithmException e) {
            throw new AESException(e);
        }
        
    }
    private static String generateSecretKey(String keyEncrypt) throws AESException {
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            KeySpec spec = new PBEKeySpec(keyEncrypt.toCharArray(), keyEncrypt.getBytes(), KEY_SIZE, KEY_SIZE);
            SecretKey skey = factory.generateSecret(spec);
            // Get the KeyGenerator
            byte[] raw = skey.getEncoded();
            String key = Base64.encodeToString(raw, true);
            return key;
        } catch (NoSuchAlgorithmException e) {
            throw new AESException(e);
        } catch (InvalidKeySpecException e) {
            throw new AESException(e);
        }
        
    }
    
    public static String encrypt(String key,String plainText) throws AESException {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(Base64.decode(key), ENCRYPTION_ALGORITHM);
            // Instantiate the cipher
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] raw = cipher.doFinal(plainText.getBytes(_charset));
            String encrytString = Base64.encodeToString(raw, true);
            return encrytString;
        } catch (NoSuchAlgorithmException e) {
            throw new AESException(e);
        } catch (NoSuchPaddingException e) {
            throw new AESException(e);
        } catch (InvalidKeyException e) {
            throw new AESException(e);
        } catch (IllegalBlockSizeException e) {
            throw new AESException(e);
        } catch (BadPaddingException e) {
            throw new AESException(e);
        } catch (UnsupportedEncodingException e) {
            throw new AESException(e);
        }
    }


    public static String decrypt(String key,String encrypted) throws AESException {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(Base64.decode(key), ENCRYPTION_ALGORITHM);
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] raw = Base64.decode(encrypted);
            byte[] dcryptBytes = cipher.doFinal(raw);
            String decryptString = new String(dcryptBytes,_charset);
            return decryptString;
        } catch (NoSuchAlgorithmException e) {
            throw new AESException(e);
        } catch (NoSuchPaddingException e) {
            throw new AESException(e);
        } catch (InvalidKeyException e) {
            throw new AESException(e);
        } catch (IllegalBlockSizeException e) {
            throw new AESException(e);
        } catch (BadPaddingException e) {
            throw new AESException(e);
        } catch (UnsupportedEncodingException e) {
            throw new AESException(e);
        } catch (IOException e) {
            throw new AESException(e);
        }

    }
    public static String encryptkey(String plainText) throws AESException {
        try {
            
            SecretKeySpec skeySpec = new SecretKeySpec(Base64.decode(generateSecretKey(secretKey)), ENCRYPTION_ALGORITHM);
            // Instantiate the cipher
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] raw = cipher.doFinal(plainText.getBytes(_charset));
            String encrytString = Base64.encodeToString(raw, true);
            return encrytString;
        } catch (NoSuchAlgorithmException e) {
            throw new AESException(e);
        } catch (NoSuchPaddingException e) {
            throw new AESException(e);
        } catch (InvalidKeyException e) {
            throw new AESException(e);
        } catch (IllegalBlockSizeException e) {
            throw new AESException(e);
        } catch (BadPaddingException e) {
            throw new AESException(e);
        } catch (UnsupportedEncodingException e) {
            throw new AESException(e);
        }
    }
    public static String encryptData(String plainText) throws AESException {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(Base64.decode(generateSecretKey(secretKeyData)), ENCRYPTION_ALGORITHM);
            // Instantiate the cipher
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] raw = cipher.doFinal(plainText.getBytes(_charset));
            String encrytString = Base64.encodeToString(raw, true);
            return encrytString;
        } catch (NoSuchAlgorithmException e) {
            throw new AESException(e);
        } catch (NoSuchPaddingException e) {
            throw new AESException(e);
        } catch (InvalidKeyException e) {
            throw new AESException(e);
        } catch (IllegalBlockSizeException e) {
            throw new AESException(e);
        } catch (BadPaddingException e) {
            throw new AESException(e);
        } catch (UnsupportedEncodingException e) {
            throw new AESException(e);
        }
    }


    public static String decryptkey(String encrypted) throws AESException {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(Base64.decode(generateSecretKey(secretKey)), ENCRYPTION_ALGORITHM);
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] raw = Base64.decode(encrypted);
            byte[] dcryptBytes = cipher.doFinal(raw);
            String decryptString = new String(dcryptBytes,_charset);
            return decryptString;
        } catch (NoSuchAlgorithmException e) {
            throw new AESException(e);
        } catch (NoSuchPaddingException e) {
            throw new AESException(e);
        } catch (InvalidKeyException e) {
            throw new AESException(e);
        } catch (IllegalBlockSizeException e) {
            throw new AESException(e);
        } catch (BadPaddingException e) {
            throw new AESException(e);
        } catch (UnsupportedEncodingException e) {
            throw new AESException(e);
        } catch (IOException e) {
            throw new AESException(e);
        }

    }
    public static String decryptData(String encrypted) throws AESException {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(Base64.decode(generateSecretKey(secretKeyData)), ENCRYPTION_ALGORITHM);
            Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] raw = Base64.decode(encrypted);
            byte[] dcryptBytes = cipher.doFinal(raw);
            String decryptString = new String(dcryptBytes,_charset);
            return decryptString;
        } catch (NoSuchAlgorithmException e) {
            throw new AESException(e);
        } catch (NoSuchPaddingException e) {
            throw new AESException(e);
        } catch (InvalidKeyException e) {
            throw new AESException(e);
        } catch (IllegalBlockSizeException e) {
            throw new AESException(e);
        } catch (BadPaddingException e) {
            throw new AESException(e);
        } catch (IOException e) {
             throw new AESException(e);
        }
    }
}
