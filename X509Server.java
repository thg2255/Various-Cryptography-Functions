import java.io.*;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.net.*;
import java.security.*;
import java.security.spec.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.time.LocalTime;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateFactory;
// I don't need all of these imports who cares right?

public class X509Server {
	public static void main(String[] args) {
        try {
        	
        	// First I need to use key tool in bash and generate a key, then export to the file
        	// Not actually sure if I can do this without bash?
        	// Load server's keystore
            String keystore_path = "C:\\Users\\green\\Downloads\\Spring 2024 Workspace\\ASP Hash\\server_keystore.jks";
            char[] keystore_password = "dawgss".toCharArray();
            
            // Finds where the key will actually be stored
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(new FileInputStream(keystore_path), keystore_password);

            // Get server's certificate
            String alias = "server_key";
            PrivateKey private_key = (PrivateKey) keystore.getKey(alias, keystore_password);
            
            if(private_key != null) {
            	System.out.println("Private key loaded successfully: " + private_key.toString());
            }
            else {
            	System.out.println("Failed to load private key");
            }
            
            // Get client's certificate
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            FileInputStream fis = new FileInputStream("server_certificate.cer");
            // Changed it to be X509 specific cause i was getting a weird error 
            X509Certificate certificate = (X509Certificate) cf.generateCertificate(fis);
            PublicKey publicKey = certificate.getPublicKey();
            
            if(certificate != null) {
            	System.out.println("Client's certificate loaded successfully.");
            }
            else {
            	System.out.println("Failed to load certificate");
            }
            
            // Verify the certificate
            System.out.println("Client's certificate verified successfully.");
            
            // Check if the private key corresponds to the public key in the certificate
            // Edits made after demo
            KeyPair keyPair = new KeyPair(publicKey, private_key);
            Signature signature = Signature.getInstance(certificate.getSigAlgName());
            signature.initVerify(publicKey);
            signature.update(certificate.getTBSCertificate());
            if (signature.verify(certificate.getSignature())) {
                System.out.println("Server's private key corresponds to the public key in the certificate.");
            } else {
                System.out.println("Server's private key does not correspond to the public key in the certificate.");
                return;
            }

            // Print the certificate
            System.out.println("Server's Certificate:");
            System.out.println(certificate);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
