package hms.crypto;

import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class CryptoHandler extends CryptoInit {
	static String key = Base64.encodeBase64String(Key.getBytes(StandardCharsets.UTF_8));
	static String iv = Base64.encodeBase64String(Iv.getBytes(StandardCharsets.UTF_8));

	public static String encrypt(String plaintext) throws Exception {
		SecretKeySpec secretKey = new SecretKeySpec(Base64.decodeBase64(key), "AES");
		IvParameterSpec ivParameterSpec = new IvParameterSpec(Base64.decodeBase64(iv));
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
		byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes("UTF-8"));
		return Base64.encodeBase64String(encryptedBytes);
	}

	public static String decrypt(String ciphertext) throws Exception {
		SecretKeySpec secretKey = new SecretKeySpec(Base64.decodeBase64(key), "AES");
		IvParameterSpec ivParameterSpec = new IvParameterSpec(Base64.decodeBase64(iv));
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
		byte[] decodedBytes = Base64.decodeBase64(ciphertext);
		byte[] decryptedBytes = cipher.doFinal(decodedBytes);
		return new String(decryptedBytes, "UTF-8");
	}
}