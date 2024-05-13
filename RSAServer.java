import java.io.*;
import java.net.*;
import java.security.*;
import java.security.spec.*;
import java.util.Arrays;
import java.util.Base64;
import java.time.LocalTime;
import javax.crypto.*;
import javax.crypto.spec.*;

public class RSAServer {
	public static void main(String[] args) {
        try {
        	// Connection establishing
        	int port = 7999;
			ServerSocket server = new ServerSocket(port);
			Socket s = server.accept();
			
            // Generate RSA key pair
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair key_pair = keyPairGenerator.generateKeyPair();

            // ServerSocket ss = new ServerSocket(port);

            while (true) {
                // Accept client connection
                Socket client_socket = server.accept();

                // Streams
                ObjectOutputStream os = new ObjectOutputStream(client_socket.getOutputStream());
                ObjectInputStream is = new ObjectInputStream(client_socket.getInputStream());

                // Public Key (if this gives bad padding exception I will lose it
                PublicKey clientPublicKey = (PublicKey) is.readObject();

                // Generate AES key for session encryption
                KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
                keyGenerator.init(128);
                SecretKey aes_key = keyGenerator.generateKey();

                // Encrypt AES key with client's public key
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.ENCRYPT_MODE, clientPublicKey);
                byte[] encrypted_aes_key = cipher.doFinal(aes_key.getEncoded());

                // Send encrypted AES key to client
                os.writeObject(encrypted_aes_key);
                
                // Receive encrypted message from client
                byte[] encrypted_message = (byte[]) is.readObject();
                
                // Decrypt message with AES key
                cipher = Cipher.getInstance("AES");
                cipher.init(Cipher.DECRYPT_MODE, aes_key);
                byte[] decrypted_message = cipher.doFinal(encrypted_message);
                String message = new String(decrypted_message);

                System.out.println("Received message from client: " + message);

                // Close streams and socket
                is.close();
                os.close();
                client_socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
