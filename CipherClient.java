import java.io.*;
import java.net.*;
import java.security.*;
import java.util.Arrays;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.time.LocalTime;

// Crypto will be a good class to use, you can use the cipher class
public class CipherClient
{
	public static void main(String[] args) throws Exception 
	{
		try {
			String message = "The quick brown fox jumps over the lazy dog.";
			String host = "localhost";
			int port = 7999;
			// Socket s = new Socket(host, port);
	
			// YOU NEED TO DO THESE STEPS:
			// -Generate a DES key.
			// -Store it in a file.
			// -Use the key to encrypt the message above and send it over socket s to the server.	
			
			// Generating Key
			KeyGenerator keyGenerated = KeyGenerator.getInstance("DES");
	        keyGenerated.init(56);
			SecretKey key = keyGenerated.generateKey();
			
			// File Storage
            // To ensure a different key is being generated each time
            String key_filename = "keyfile_" + LocalTime.now().getMinute() + ".txt";
            
            try(ObjectOutputStream key_out = new ObjectOutputStream(new FileOutputStream(key_filename))){
            	key_out.writeObject(key);
            }
	        
	        // Encrypting message
	        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
	        cipher.init(Cipher.ENCRYPT_MODE, key);
	        byte[] encrypted_message = cipher.doFinal(message.getBytes());
	        
	        try (Socket s = new Socket(host, port)){
		        OutputStream o = s.getOutputStream();
		        o.write(encrypted_message);
		        o.close();
	        }
	        
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}