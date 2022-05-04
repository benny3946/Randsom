import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
public class chiper {
	
	private static KeyPair rsakeys;
	private static Key aeskey, publickey, privatekey;
	private static byte[] IV;
	
	private static String publicKeyString = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCggEatC8Un08FTO6f00jbsGDNA2orBrPUiSG/IkQ7qsbpp4SvWfUA36NweFWapq/7zaY8SlH1RtqxEbwGuMMcrMAg1IuNVbfRL3UJKE25sTWroQH4wBSVZaduJvmoHjSafrXVzhu8T8KRJ4bdxu2EG/fiFIdRGM7rm9G6blIQQVQIDAQAB";
	private static String privateKeyString = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKCARq0LxSfTwVM7p/TSNuwYM0DaisGs9SJIb8iRDuqxumnhK9Z9QDfo3B4VZqmr/vNpjxKUfVG2rERvAa4wxyswCDUi41Vt9EvdQkoTbmxNauhAfjAFJVlp24m+ageNJp+tdXOG7xPwpEnht3G7YQb9+IUh1EYzuub0bpuUhBBVAgMBAAECgYBZCK+sIOJehI4y4N375n0HHSmZaWIdrBdTzEEOTsGqmariP0G1O0fbe/ZXTeHkb3//gWgXk32tTZtkXvvcqDxjKXgXlvDjVkCOvMKoH2Z/wQUhlVVtMiSW+WaPOg/UVUuPxYgZAYNzzMU/uhvKCIaSLCqxgom/bH2duTNOGu1AIQJBAPEnECzbdTW5WzMifP1uQ9DPk0bM5NkV8dVlM4w0nR/NdC/ZZgfnYFeLc/nywi6llsLD3/Cravqxdci2T8Cwz20CQQCqYgVJkd6bKp4ZRGeQ1MfF066kB9FnS9Xd3jCeDS1E83PUQFErXy2UULxkDqaFJOAgEzTnZXFyuqFSsgNL7OuJAkA8HyQOOux+52ZQWlHVES+BGK88IIsRgEIZlLCETP27VwqXf1jLsai4SsEwWJCG9quehR0IsIPsWl/mGWXKwYoFAkAT+WfLRymEoUtlhOprRTaiT32ixzPaWz6YQwsKDFtpQO0sTdn2LrNGNuzPAhSteTQ5Lmc+VVsmaxCshCf0x7KZAkBTsYybMkO6sif6a6UkqCO8VM+xF9xnlJ8UwDIU7XBE4z1ahoQcRe2ZfygKwX5WJOQh1/kcOp8EWbOU0gataRVG"; 
	private static String aesKeyString;
	
	public static void main(String args[]) {
		
		//rsaKeysGen();
		aesKeyGen();

		
		String input = "howtodoinjava.com";
		String cipher = encrypt(input);
		decrypt(cipher);
		
		printAESKey();
		System.out.println("Encrypted AES key");
		aesKeyString = encrpytAESKey();
		System.out.println(aesKeyString);
		decrypt(cipher);
		decrpytAESKey();
		decrypt(cipher);
	}
	
	private static String encrpytAESKey() {
		String aks = "";
		try{
			if(publickey == null) {
				publicInit(publicKeyString);
			}
			Cipher rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding ");
			rsa.init(Cipher.ENCRYPT_MODE, publickey);
			byte[] encryptedAESkeybyte = rsa.doFinal( aeskey.getEncoded());
			aks = encode( encryptedAESkeybyte );
			aeskey = new SecretKeySpec(encryptedAESkeybyte, "AES");
		}catch(Exception ex) {}
		return aks;
	}
	
	private static void decrpytAESKey() {
		try{
			if(privatekey == null) {
				privateInit(privateKeyString);
			}
			Cipher rsa = Cipher.getInstance("RSA/ECB/PKCS1Padding ");
			rsa.init(Cipher.DECRYPT_MODE, privatekey);
			aeskey = new SecretKeySpec(rsa.doFinal(decode(aesKeyString)), "AES");
		}catch(Exception ex) {}
	}
	
	public static String encrypt(String plaintext) {
		String c = "";
		try {
			Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
			if(IV == null) {
				aes.init(Cipher.ENCRYPT_MODE, aeskey);
				IV = aes.getIV();
			}
			else
				aes.init(Cipher.ENCRYPT_MODE, aeskey, new IvParameterSpec(IV));
			c = encode(aes.doFinal(plaintext.getBytes()));
			System.out.println(c); // "UTF-8"
			
		}catch(Exception ex) {}
		
		return c;
	}
	
	public static void decrypt(String cipher) {
		try {
			if(IV != null) {
				Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding");
				aes.init(Cipher.DECRYPT_MODE, aeskey, new IvParameterSpec(IV));
				System.out.println(new String(aes.doFinal(decode(cipher))));
			}
		}catch(Exception ex) {
			System.err.println("Password Error!");
		}
	}
	
	public static String encode(byte[] data) {
		return Base64.getEncoder().encodeToString(data);
	}
	
	public static byte[] decode(String s) {
		return Base64.getDecoder().decode(s);
	}
	
	public static void rsaKeysGen() {
		try {
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(1024);
			rsakeys = generator.generateKeyPair();
			printRSAKeys();
			
		}catch(Exception ex){}
	}
	
	public static void printRSAKeys() {
		try {
			System.out.println("Public key: " + encode(rsakeys.getPublic().getEncoded()) );
			System.out.println("Private Key: " + encode(rsakeys.getPrivate().getEncoded()) );
		}catch(Exception ex) {}
	}
	
	public static void printAESKey() {
		try {
			System.out.println("AES key: " + encode(aeskey.getEncoded()));
		}catch(Exception ex) {}
	}
	
	public static void publicInit(String pk) {
		try {
			X509EncodedKeySpec keySpecPublic = new X509EncodedKeySpec(Base64.getDecoder().decode(pk));
			KeyFactory kf = KeyFactory.getInstance("RSA");
			publickey = kf.generatePublic(keySpecPublic);
		}catch(Exception ex) {}
	}
	
	public static void privateInit(String pk) {
		try {
			PKCS8EncodedKeySpec  keySpecPublic = new PKCS8EncodedKeySpec (decode(pk));
			KeyFactory kf = KeyFactory.getInstance("RSA");
			privatekey = kf.generatePrivate(keySpecPublic);
		}catch(Exception ex) {}
	}
	
	public static void aesKeyGen() {
		try{
			KeyGenerator generator = KeyGenerator.getInstance("AES");
			generator.init(256);
			aeskey = generator.generateKey();
		}catch(Exception exc) {}
	}
}
