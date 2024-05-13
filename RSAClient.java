import java.io.*;
import java.net.*;
import java.security.*;
import java.security.spec.*;
import java.util.Arrays;
import java.util.Base64;
import java.time.LocalTime;
import javax.crypto.*;
import javax.crypto.spec.*;

public class RSAClient {
	public static void main(String[] args) {
		try {
			// Connection establishing
			String host = "localhost";
			int port = 7999;
			Socket s = new Socket(host, port);
			// Just talked to my irish uncle and he said this so here's the message
			String message = "Oi howreya (true dubliner)";
			
			// RSA generating
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
            KeyPair key_pair = keyPairGenerator.generateKeyPair();

            // Making the output and input streams
            ObjectOutputStream outputStream = new ObjectOutputStream(s.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(s.getInputStream());

            // Sent the public key to the server
            outputStream.writeObject(key_pair.getPublic());

            // Receive encrypted AES key from server
            byte[] encrypted_aes_key = (byte[]) inputStream.readObject();

            // Decrypting AES key with private key
            Cipher cipher = Cipher.getInstance("RSA");
            // get Private Returns a reference to the private key component of this key pair.
            cipher.init(Cipher.DECRYPT_MODE, key_pair.getPrivate());
            byte[] decrypted_aes_key = cipher.doFinal(encrypted_aes_key);
            SecretKey aes_key = new SecretKeySpec(decrypted_aes_key, 0, decrypted_aes_key.length, "AES");
            
            // Sending the message
            cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, aes_key);
            byte[] encryptedMessage = cipher.doFinal(message.getBytes());
            outputStream.writeObject(encryptedMessage);

            // Close streams and socket
            inputStream.close();
            outputStream.close();
            s.close();
		} catch (Exception e) {
            e.printStackTrace();
        }
	}
}
