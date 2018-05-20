package cn.justin.cypt;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;



public  class CryptHelper {

	 public static void main(String[] args) throws FileNotFoundException, IOException {  
	        // 密钥的种子，可以是任何形式，本质是字节数组  
	                String strKey = "username";  
	                String clearPwd = "password";  
	                // 密钥数据  
	                byte[] rawKey = getRawKey(strKey.getBytes());  
	                // 密码的明文  
	                
	                // 密码加密后的密文  
	                String  encryptedData = encrypt(rawKey, clearPwd);  
	               
	                System.out.println(encryptedData);  

	                String decryptedPwd = decrypt(encryptedData, strKey);  
	                
	                System.out.println(decryptedPwd);  
	                Properties user = new Properties();
	                String file="config.txt";
	                user.load(new FileReader(file));
	                user.setProperty("username", strKey);
	                user.setProperty("password", encryptedData);
	                user.store(new FileWriter(file), null);
	  
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
	     *            密文字节数组 
	     * @param rawKey 
	     *            密钥 
	     * @return 解密后的字符串 
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