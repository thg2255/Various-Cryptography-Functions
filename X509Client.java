import java.io.*;
import java.io.FileOutputStream;
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
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class X509Client {
	public static void main(String[] args) {
        try {
        	// Get the cert path
            String certificate_path = "server_certificate.cer";
            FileInputStream fis = new FileInputStream(certificate_path);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate certificate = (X509Certificate) cf.generateCertificate(fis);
            fis.close();
            System.out.println("Server's certificate loaded successfully.");

            certificate.verify(certificate.getPublicKey());
            System.out.println("Server's certificate verified successfully.");

            // Print the content of the certificate
            System.out.println("Server's Certificate:");
            System.out.println(certificate);

        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
