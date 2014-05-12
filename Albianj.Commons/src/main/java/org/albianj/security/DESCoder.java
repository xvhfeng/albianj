package org.albianj.security;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.albianj.text.StringHelper;

/**
 * DES������������
 * 
 * <pre>
 * ���� DES��DESede(TripleDES,����3DES)��AES��Blowfish��RC2��RC4(ARCFOUR)
 * DES          		key size must be equal to 56
 * DESede(TripleDES) 	key size must be equal to 112 or 168
 * AES          		key size must be equal to 128, 192 or 256,but 192 and 256 bits may not be available
 * Blowfish     		key size must be multiple of 8, and can only range from 32 to 448 (inclusive)
 * RC2          		key size must be between 40 and 1024 bits
 * RC4(ARCFOUR) 		key size must be between 40 and 1024 bits
 * �������� �������� JDK Document http://.../docs/technotes/guides/security/SunProviders.html
 * </pre>
 */
public abstract class DESCoder extends Coder
{

	private static final String DEFAULT_DES_KEY = "!$^*%&^&*FSDFOIRMNSknfesjfghu!!";


	public static String decrypt(String message) throws Exception
	{
		return decrypt(DEFAULT_DES_KEY,message);
	}
	
	public static String decrypt(String key,String message) throws Exception
	{
		String k = StringHelper.padLeft(key, 8);
		byte[] bytesrc = Coder.decryptBASE64(message);
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec(k.getBytes("UTF-8"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(k.getBytes("UTF-8"));

		cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

		byte[] retByte = cipher.doFinal(bytesrc);
		return new String(retByte);
	}
	
	public static String encrypt(String message) throws Exception
	{
		return encrypt(DEFAULT_DES_KEY,message);
	}

	public static String encrypt(String key,String message) throws Exception
	{
		String k = StringHelper.padLeft(key, 8);
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		DESKeySpec desKeySpec = new DESKeySpec(k.getBytes("UTF-8"));
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey secretKey = keyFactory.generateSecret(desKeySpec);
		IvParameterSpec iv = new IvParameterSpec(k.getBytes("UTF-8"));
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

		return Coder.encryptBASE64(cipher.doFinal(message.getBytes("UTF-8")));
	}

}
