package cn.justin.cypt;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;



public  class CryptHelper {

	 public static void main(String[] args) throws UnsupportedEncodingException {  
	        // ��Կ�����ӣ��������κ���ʽ���������ֽ�����  
	                String strKey = "xxx";  
	                String clearPwd = "xxxx";  
	                // ��Կ����  
	                byte[] rawKey = getRawKey(strKey.getBytes());  
	                // ���������  
	                
	                // ������ܺ������  
	                String  encryptedData = encrypt(rawKey, clearPwd);  
	               
	                System.out.println(encryptedData);  

	                String decryptedPwd = decrypt(encryptedData, strKey);  
	                
	                System.out.println(decryptedPwd);  
	  
	    }  
	  

	 public static String encrypt(byte[] rawKey, String clearPwd) {  
	        try {  
	            SecretKeySpec secretKeySpec = new SecretKeySpec(rawKey, "AES");  
	            Cipher cipher = Cipher.getInstance("AES");  
	            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);  
	            byte[] encypted = cipher.doFinal(clearPwd.getBytes());  
	            return Base64.getEncoder().withoutPadding().encodeToString(encypted);  
	        } catch (Exception e) {  
	            return null;  
	        }  
	    }  
	  
	    /** 
	     * @param encrypted 
	     *            �����ֽ����� 
	     * @param rawKey 
	     *            ��Կ 
	     * @return ���ܺ���ַ��� 
	     */  
	    public static String decrypt(String encrypted, String seed) {  
	        try {  
	        	byte [] rawKey=getRawKey(seed.getBytes());
	            SecretKeySpec secretKeySpec = new SecretKeySpec(rawKey, "AES");  
	            Cipher cipher = Cipher.getInstance("AES");  
	            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);  
	            byte[] encryptedbyte =Base64.getDecoder().decode(encrypted);
	            byte[] decrypted = cipher.doFinal(encryptedbyte);  
	            return new String(decrypted);  
	        } catch (Exception e) {  
	            return "";  
	        }  
	    }  
	  

	    private static byte[] getRawKey(byte[] seed) {  
	        byte[] rawKey = null;  
	        try {  
	            KeyGenerator kgen = KeyGenerator.getInstance("AES");  
	            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");  
	            secureRandom.setSeed(seed);   
	            kgen.init(128, secureRandom);  
	            SecretKey secretKey = kgen.generateKey();  
	            rawKey = secretKey.getEncoded();  
	        } catch (NoSuchAlgorithmException e) {  
	        }  
	        return rawKey;  
	    }  
	}  