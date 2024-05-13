import java.io.*;
import java.net.*;
import java.security.*;
import java.math.BigInteger;

// NOTE I did not know we could use the security class before I finished this, I don't know if
// it's even necessary to be honest
public class ElGamalBob
{
	// IMPLEMENT THIS FUNCTION
	private static boolean verifySignature(BigInteger y, BigInteger g, BigInteger p, BigInteger a, BigInteger b, String message)
	{
		// Converts the message to compare for later
		StringBuilder num_message = new StringBuilder();
        for (char c : message.toCharArray()) {
        	num_message.append((int) c);
        }
        BigInteger converted_message = new BigInteger(num_message.toString());
        
        BigInteger g_message = g.modPow(converted_message, p);
        
        // I have to slowly go through each part of the formula, I really hope this works
        // Computes y^a mod p
        BigInteger y_a = y.modPow(a, p);

        // Computes a^b mod p
        BigInteger a_b = a.modPow(b, p);

        // Computes y^a * a^b mod p
        BigInteger ya_ab = y_a.multiply(a_b).mod(p);

        // Checks if g^M = y^a * a^b mod p, so expected vs real signature
        boolean sig = g_message.equals(ya_ab);
        
        return sig;
	}
	
	
	// Tester (to visualize the keys)
	private static String showSignature(BigInteger y, BigInteger g, BigInteger p, BigInteger a, BigInteger b, String message)
	{
		StringBuilder num_message = new StringBuilder();
        for (char c : message.toCharArray()) {
        	num_message.append((int) c);
        }
        BigInteger converted_message = new BigInteger(num_message.toString());
        
        BigInteger g_message = g.modPow(converted_message, p);
        
        // Compute y^a mod p
        BigInteger y_a = y.modPow(a, p);

        // Compute a^b mod p
        BigInteger a_b = a.modPow(b, p);

        // Compute y^a * a^b mod p
        BigInteger ya_ab = y_a.multiply(a_b).mod(p);
        
        return "Expected Signature: " + g_message.toString().substring(0, 20) + "..." + "\nActual Signature: " + ya_ab.toString().substring(0, 20) + "...";
	}

	public static void main(String[] args) throws Exception 
	{
		int port = 7999;
		ServerSocket s = new ServerSocket(port);
		Socket client = s.accept();
		ObjectInputStream is = new ObjectInputStream(client.getInputStream());

		// read public key
		BigInteger y = (BigInteger)is.readObject();
		BigInteger g = (BigInteger)is.readObject();
		BigInteger p = (BigInteger)is.readObject();

		// read message
		String message = (String)is.readObject();

		// read signature
		BigInteger a = (BigInteger)is.readObject();
		BigInteger b = (BigInteger)is.readObject();

		boolean result = verifySignature(y, g, p, a, b, message);

		System.out.println(message);
		
		// This is not required, I is just to show they are the same
		System.out.println(showSignature(y, g, p, a, b, message));

		if (result == true)
			System.out.println("Signature verified.");
		else
			System.out.println("Signature verification failed.");

		s.close();
	}
}